package fr.legrain.tache.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;

import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaNotificationServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaRecurrenceEvenementServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.service.LgrEmailSMTPService;
import fr.legrain.general.dao.IDatabaseDAO;
import fr.legrain.general.service.DatabaseService;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaNotification;
import fr.legrain.tiers.model.TaTiers;

@Stateless
public class TimerProgEvenementService {
	/*
	 * https://docs.oracle.com/javaee/7/tutorial/ejb-basicexamples004.htm
	 * http://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html
	 * http://docs.oracle.com/javaee/7/api/javax/ejb/Schedule.html
	 */
	@Inject private Event<TaNotification> events;
	
	private TransactionSynchronizationRegistry reg;
	private static final String TENANT_POUR_TRANSACTION_SCHEDULE = "demo"; //le dossier 'demo' existe toujours
	
	@EJB private ITaEvenementServiceRemote taEvenementService;
	@EJB private ITaRecurrenceEvenementServiceRemote taRecurrenceEvenementService;
	@EJB private ITaNotificationServiceRemote taNotificationService;
	
	@EJB private MultitenantProxyTimerEvenement multitenantProxyTimerEvenement;
	
	//@EJB private DatabaseService s;
	@Inject private IDatabaseDAO dao;
	
	
	@Resource TimerService timerService;
	
	@Timeout
	public void timeout(Timer timer) {
		TimerInfoNotificationTenant n = (TimerInfoNotificationTenant) timer.getInfo();
		multitenantProxyTimerEvenement.doWork(n.getTenant(), n.getTaNotification());
	   // System.out.println("TimerBean: timeout occurred : "+n.getTaNotification().getTaEvenementUtilisateur().getLibelleEvenement()+" "+n.getTenant());
	}
	
	public TimerHandle creerTimer(TaNotification n) {
//		ScheduleExpression schedule = new ScheduleExpression();
//		schedule.dayOfWeek("*");
//		schedule.hour("14").minute("28");
//		Timer timer = timerService.createCalendarTimer(schedule);
		
//		SimpleDateFormat formatter =   new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm");
		Date date = null;
//		try {
//			date = formatter.parse("08/22/2017 at 15:30");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		date = n.getDateNotification();
			
		SchemaResolver sr = new SchemaResolver();
		String tenant = sr.resolveCurrentTenantIdentifier();
		TimerInfoNotificationTenant info = new TimerInfoNotificationTenant(n,tenant);
		TimerConfig cfg = new TimerConfig();
		cfg.setInfo(info);
		Timer timer = timerService.createSingleActionTimer(date,cfg);
		return timer.getHandle();
	}
		
	
    public void doWork(String tenant, TaNotification n){
		initTenantRegistry();
		initTenant(tenant);
		
		multitenantProxyTimerEvenement.doWork(tenant,n);
		
    }
	
	private void initTenant(String tenant) {
		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
	}
	
	private void initTenantRegistry() {
		try {
			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
