package fr.legrain.abonnement.stripe.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;

import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
import fr.legrain.cron.model.TaCron;
import fr.legrain.general.dao.IDatabaseDAO;
import fr.legrain.hibernate.multitenant.SchemaResolver;

@Stateless
public class TimerDeclenchePaiementPrevuService {
	/*
	 * https://docs.oracle.com/javaee/7/tutorial/ejb-basicexamples004.htm
	 * http://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html
	 * http://docs.oracle.com/javaee/7/api/javax/ejb/Schedule.html
	 */
	
	private TransactionSynchronizationRegistry reg;
	
	@EJB private ITaStripePaiementPrevuServiceRemote taStripePaiementPrevuService;
	
	@EJB private MultitenantProxyTimerAbonnement multitenantProxyTimerEvenement;
	
	//@EJB private DatabaseService s;
	@Inject private IDatabaseDAO dao;
	
	@Resource TimerService timerService;
	
	@Timeout
	public void timeout(Timer timer) {
		TimerInfoPaiementPrevuTenant n = (TimerInfoPaiementPrevuTenant) timer.getInfo();
		multitenantProxyTimerEvenement.doWork(n.getTenant(), n.getTaStripePaiementPrevu());
	   // System.out.println("TimerBean: timeout occurred : "+n.getTaNotification().getTaEvenementUtilisateur().getLibelleEvenement()+" "+n.getTenant());
	}
	
	 public TimerHandle creerTimer(TaCron cron, Object param) {
    	if(param instanceof TaStripePaiementPrevu) {
    		TaStripePaiementPrevu n = (TaStripePaiementPrevu) param;
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
			date = n.getDateDeclenchement();
			
			LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			localDateTime = localDateTime.withHour(2);
			localDateTime = localDateTime.withMinute(0);
			localDateTime = localDateTime.withSecond(0); 
			
			date= Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
				
			SchemaResolver sr = new SchemaResolver();
			String tenant = sr.resolveCurrentTenantIdentifier();
			TimerInfoPaiementPrevuTenant info = new TimerInfoPaiementPrevuTenant(n,tenant);
			TimerConfig cfg = new TimerConfig();
			cfg.setInfo(info);
			Timer timer = timerService.createSingleActionTimer(date,cfg);
			return timer.getHandle();
    	}
		return null;
	}
	
//	private void initTenant(String tenant) {
//		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
//	}
//	
//	private void initTenantRegistry() {
//		try {
//			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}

}
