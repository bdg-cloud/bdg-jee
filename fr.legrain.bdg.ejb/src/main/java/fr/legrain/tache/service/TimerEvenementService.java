package fr.legrain.tache.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
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
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;
import fr.legrain.tache.model.TaNotification;
import fr.legrain.tiers.model.TaTiers;

@Stateless
public class TimerEvenementService {
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
	
	
	//@Schedule(second="*/15", minute="*",hour="*", persistent=false) //toutes les 15 secondes
	
	//Toutes les heures
	//@Schedule(second="0", minute="0",hour="*", persistent=false)
	
	//Toutes les jours à 2h du matin
	//@Schedule(second="0", minute="0",hour="2", persistent=false)
	
	//Toutes les lundi à 2h00
	//@Schedule(second="0", minute="0",hour="2",dayOfWeek="1", persistent=false)
	
	//Toutes les lundi à 2h00, tous les 1er du mois
	//@Schedule(second="0", minute="0",hour="2",dayOfMonth="1", persistent=false)
	
	//Toutes les lundi à 2h00, tous les 1er du mois
	//@Schedule(second="0", minute="0",hour="2",dayOfMonth="1",month="*", year="*", persistent=false)
    public void doWork(){
		initTenantRegistry();
		initTenant(TENANT_POUR_TRANSACTION_SCHEDULE);
		
		List<Object[]> l = dao.listeSchemaTailleConnection();
		 
		for (Object[] objects : l) {
		 
			String tenant = (String) objects[0];
			multitenantProxyTimerEvenement.doWork(tenant, null);
		}
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
