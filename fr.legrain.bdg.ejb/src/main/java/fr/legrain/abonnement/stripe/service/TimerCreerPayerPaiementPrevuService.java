//package fr.legrain.abonnement.stripe.service;
//
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.ejb.ScheduleExpression;
//import javax.ejb.Stateless;
//import javax.ejb.Timeout;
//import javax.ejb.Timer;
//import javax.ejb.TimerConfig;
//import javax.ejb.TimerHandle;
//import javax.ejb.TimerService;
//import javax.enterprise.event.Event;
//import javax.inject.Inject;
//import javax.transaction.TransactionSynchronizationRegistry;
//
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription;
//import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
//import fr.legrain.cron.model.TaCron;
//import fr.legrain.general.dao.IDatabaseDAO;
//import fr.legrain.hibernate.multitenant.SchemaResolver;
//import fr.legrain.tache.model.TaNotification;
//
//@Stateless
//public class TimerCreerPayerPaiementPrevuService {
//	/*
//	 * https://docs.oracle.com/javaee/7/tutorial/ejb-basicexamples004.htm
//	 * http://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html
//	 * http://docs.oracle.com/javaee/7/api/javax/ejb/Schedule.html
//	 */
//	@Inject private Event<TaNotification> events;
//	
//	private TaCron taCron;
//	
//	private TransactionSynchronizationRegistry reg;
//	private static final String TENANT_POUR_TRANSACTION_SCHEDULE = "demo"; //le dossier 'demo' existe toujours
//	
//	@EJB private ITaStripePaiementPrevuServiceRemote taStripePaiementPrevuService;
//	
//	@EJB private MultitenantProxyTimerAbonnement multitenantProxyTimerEvenement;
//	
//	//@EJB private DatabaseService s;
//	@Inject private IDatabaseDAO dao;
//	@Resource TimerService timerService;
//	
//	//@Schedule(second="*/15", minute="*",hour="*", persistent=false) //toutes les 15 secondes
//	
//	//Toutes les heures
//	//@Schedule(second="0", minute="0",hour="*", persistent=false)
//	
//	//Toutes les jours à 2h du matin
//	//@Schedule(second="0", minute="0",hour="2", persistent=false)
//	
//	//Toutes les lundi à 2h00
//	//@Schedule(second="0", minute="0",hour="2",dayOfWeek="1", persistent=false)
//	
//	//Toutes les lundi à 2h00, tous les 1er du mois
//	//@Schedule(second="0", minute="0",hour="2",dayOfMonth="1", persistent=false)
//	
//	//Toutes les lundi à 2h00, tous les 1er du mois
//	//@Schedule(second="0", minute="0",hour="2",dayOfMonth="1",month="*", year="*", persistent=false)
////    public void doWork(){
////		initTenantRegistry();
////		initTenant(TENANT_POUR_TRANSACTION_SCHEDULE);
////		
////		List<Object[]> l = dao.listeSchemaTailleConnection();
//////		 
////		for (Object[] objects : l) {
////		 
////			String tenant = (String) objects[0];
//////			multitenantProxyTimerEvenement.doWork(tenant, null);
////		}
////		
//////		List<TaStripePaiementPrevu> l = taStripePaiementPrevuService.selectAll();
////    }
//	@Timeout
//	public void timeout(Timer timer) {
//		TimerInfoAbonnementTenant n = (TimerInfoAbonnementTenant) timer.getInfo();
//		multitenantProxyTimerEvenement.doWork(n.getTenant()/*, n.getTaStripeSubscription()*/);
//	   // System.out.println("TimerBean: timeout occurred : "+n.getTaNotification().getTaEvenementUtilisateur().getLibelleEvenement()+" "+n.getTenant());
//	}
//    
//    public TimerHandle creerTimer(TaCron cron, Object param) {
//    	if(param instanceof TaStripeSubscription) {
//    		TaStripeSubscription n = (TaStripeSubscription) param;
//			ScheduleExpression schedule = new ScheduleExpression();
//			schedule.dayOfWeek("*");
//			schedule.hour("2").minute("0").second("0");
//	//		Timer timer = timerService.createCalendarTimer(schedule);
//			
//	////		SimpleDateFormat formatter =   new SimpleDateFormat("MM/dd/yyyy 'at' HH:mm");
//	//		Date date = null;
//	////		try {
//	////			date = formatter.parse("08/22/2017 at 15:30");
//	////		} catch (ParseException e) {
//	////			// TODO Auto-generated catch block
//	////			e.printStackTrace();
//	////		}
//	//		date = n.getDateDeclenchement();
//	//		
//	//		LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//	//		localDateTime = localDateTime.withHour(2);
//	//		localDateTime = localDateTime.withMinute(0);
//	//		localDateTime = localDateTime.withSecond(0); 
//	//		
//	//		date= Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//				
//			SchemaResolver sr = new SchemaResolver();
//			String tenant = sr.resolveCurrentTenantIdentifier();
//			TimerInfoAbonnementTenant info = new TimerInfoAbonnementTenant(n,tenant);
//			TimerConfig cfg = new TimerConfig();
//			cfg.setInfo(info);
//			Timer timer = timerService.createCalendarTimer(schedule,cfg);
//			return timer.getHandle();
//    	}
//		return null;
//	}
//	
////	private void initTenant(String tenant) {
////		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
////	}
////	
////	private void initTenantRegistry() {
////		try {
////			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
////		} catch (NamingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} 
////	}
//
//}
