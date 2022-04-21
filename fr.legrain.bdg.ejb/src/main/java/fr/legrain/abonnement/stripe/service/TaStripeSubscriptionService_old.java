//package fr.legrain.abonnement.stripe.service;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.security.DeclareRoles;
//import javax.ejb.CreateException;
//import javax.ejb.EJB;
//import javax.ejb.EJBException;
//import javax.ejb.FinderException;
//import javax.ejb.RemoveException;
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import javax.interceptor.Interceptors;
//import javax.jws.WebMethod;
//
//import org.apache.log4j.Logger;
//import org.hibernate.OptimisticLockException;
//
//import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
//import fr.legrain.abonnement.model.TaFrequence;
//import fr.legrain.abonnement.stripe.dao.IStripePlanDAO;
//import fr.legrain.abonnement.stripe.dao.IStripeSubscriptionDAO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO;
//import fr.legrain.abonnement.stripe.model.TaStripePlan;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription_old;
//import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSubscriptionServiceRemote;
//import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
//import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
//import fr.legrain.bdg.general.service.remote.BusinessValidationException;
//import fr.legrain.bdg.model.mapping.mapper.TaStripeSubscriptionMapper;
//import fr.legrain.data.AbstractApplicationDAOServer;
//import fr.legrain.document.model.TaLEcheance;
//import fr.legrain.documents.dao.ILEcheanceDAO;
//import fr.legrain.droits.service.SessionInfo;
//import fr.legrain.droits.service.TenantInfo;
//import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
//
//
///**
// * Session Bean implementation class TaStripeSubscriptionBean
// */
//@SuppressWarnings("deprecation")
//@Stateless
//@DeclareRoles("admin")
//@Interceptors(ServerTenantInterceptor.class)
//public class TaStripeSubscriptionService_old extends AbstractApplicationDAOServer<TaStripeSubscription_old, TaStripeSubscriptionDTO> implements ITaStripeSubscriptionServiceRemote {
//
//	@Override
//	public void persistDTO(TaStripeSubscriptionDTO transientInstance) throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void removeDTO(TaStripeSubscriptionDTO persistentInstance) throws RemoveException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mergeDTO(TaStripeSubscriptionDTO detachedInstance) throws EJBException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void persistDTO(TaStripeSubscriptionDTO transientInstance, String validationContext) throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mergeDTO(TaStripeSubscriptionDTO detachedInstance, String validationContext) throws EJBException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public List<TaStripeSubscriptionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TaStripeSubscriptionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<TaStripeSubscriptionDTO> selectAllDTO() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionDTO findByIdDTO(int id) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscriptionDTO findByCodeDTO(String code) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void validateDTO(TaStripeSubscriptionDTO dto) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateDTOProperty(TaStripeSubscriptionDTO dto, String propertyName)
//			throws BusinessValidationException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateDTO(TaStripeSubscriptionDTO dto, String validationContext) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateDTOProperty(TaStripeSubscriptionDTO dto, String propertyName, String validationContext)
//			throws BusinessValidationException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void error(TaStripeSubscriptionDTO dto) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void persist(TaStripeSubscription_old transientInstance) throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void remove(TaStripeSubscription_old persistentInstance) throws RemoveException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public TaStripeSubscription_old merge(TaStripeSubscription_old detachedInstance) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void persist(TaStripeSubscription_old transientInstance, String validationContext) throws CreateException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public TaStripeSubscription_old merge(TaStripeSubscription_old detachedInstance, String validationContext) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscription_old findById(int id) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public TaStripeSubscription_old findByCode(String code) throws FinderException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void validateEntity(TaStripeSubscription_old value) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateEntityProperty(TaStripeSubscription_old value, String propertyName) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateEntity(TaStripeSubscription_old value, String validationContext) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void validateEntityProperty(TaStripeSubscription_old value, String propertyName, String validationContext) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public List<TaStripeSubscription_old> selectAll() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected TaStripeSubscription_old refresh(TaStripeSubscription_old persistentInstance) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
////	static Logger logger = Logger.getLogger(TaStripeSubscriptionService.class);
////
////	@Inject private IStripeSubscriptionDAO dao;
////	@Inject private ILEcheanceDAO daoLEcheance;
////	@Inject private IStripePlanDAO daoPlan;
////	
////	@EJB private ITaGenCodeExServiceRemote gencode;
////	@Inject private	SessionInfo sessionInfo;
////	@Inject private	TenantInfo tenantInfo;
////	
////	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
////
////	/**
////	 * Default constructor. 
////	 */
////	public TaStripeSubscriptionService() {
////		super(TaStripeSubscription_old.class,TaStripeSubscriptionDTO.class);
////	}
////	
////	//	private String defaultJPQLQuery = "select a from TaStripeSubscription_old a";
////	
////	public List<TaStripeSubscriptionDTO> findByCodeLight(String code) {
////		return dao.findByCodeLight(code);
////	}
////	
////	public List<TaStripeSubscriptionDTO> findAllLight() {
////		return dao.findAllLight();
////	}
////	
////	public boolean annulerSubscriptionNonStripe(int idSubscriptionNonStripe, String commentaireAnnulation, boolean annuleDerniereEcheance) {
////		TaStripeSubscription_old taStripeSubscription = dao.findById(idSubscriptionNonStripe);
////		taStripeSubscription.setCommentaireAnnulation(commentaireAnnulation);
////		taStripeSubscription.setDateAnnulation(new Date());
////		
////		dao.merge(taStripeSubscription);
////		if(annuleDerniereEcheance) {
////			//daoLEcheance.
////		}
////		
////		return true;
////	}
////	
////	public List<TaStripeSubscriptionDTO> rechercherSubscriptionCustomerDTO(String idExterneCustomer) {
////		return dao.rechercherSubscriptionCustomerDTO(idExterneCustomer);
////	}
////	
////	public List<TaStripeSubscriptionDTO> rechercherSubscriptionNonStripeCustomerDTO(String idExterneCustomer) {
////		return dao.rechercherSubscriptionNonStripeCustomerDTO(idExterneCustomer);
////	}
////	
////	public List<TaStripeSubscription_old> rechercherSubscriptionCustomer(String idExterneCustomer) {
////		return dao.rechercherSubscriptionCustomer(idExterneCustomer);
////	}
////	
////	public List<TaStripeSubscriptionDTO> rechercherSubscriptionPlanDTO(String idExternePlan) {
////		return dao.rechercherSubscriptionPlanDTO(idExternePlan);
////	}
////	public List<TaStripeSubscription_old> rechercherSubscriptionPlan(String idExternePlan) {
////		return dao.rechercherSubscriptionPlan(idExternePlan);
////	}
////	//rajout yann
////	public TaStripeSubscriptionDTO findDTOByIdAbonnement(Integer idAbonnement) {
////		return dao.findDTOByIdAbonnement(idAbonnement);
////	}
////	public List<TaStripeSubscriptionDTO> findAllDTOByIdTiers(Integer idTiers) {
////		return dao.findAllDTOByIdTiers(idTiers);
////	}
////	public List<TaStripeSubscriptionDTO> selectAllASuspendre(){
////		return dao.selectAllASuspendre();
////	}
////	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers){
////		return dao.findAllFullDTOByIdTiers(idTiers);
////	}
////	
////	public TaStripeSubscription_old findByIdLEcheance(Integer idLEcheance) {
////		return dao.findByIdLEcheance(idLEcheance);
////	}
////	
////	
////	public List<TaLEcheance> generePremieresEcheances(TaStripeSubscription_old taStripeSubscription) {
////		Date now = new Date();
////		List<TaLEcheance> listePremieresEcheances = new ArrayList<>();
////////		si un des abonnements actif a une échéance à : date du jour + NB_JOUR_AVANT_ECHEANCE
//////		if(/*taStripeSubscription.getDateDebut().equals(now)
//////				||*/ (taStripeSubscription.getDateDebut().before(now) && taStripeSubscription.getDateFin() == null)
//////				|| (taStripeSubscription.getDateDebut().before(now) && taStripeSubscription.getDateFin().after(now))
//////				) {
//////			//l'abonnement est bien actif en ce moment
////			
////			if(taStripeSubscription.getDateAnnulation()==null) { //l'abonnement n'est pas suspendu/annuler
////			
////				Date date = taStripeSubscription.getDateDebut();
////				
////				LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
////				LocalDateTime localDateTimeFinPeriode = taStripeSubscription.getDateDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
////				LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
////				TaFrequence taFrequence = taStripeSubscription.getTaAbonnement().getTaFrequence();
////				if(!taStripeSubscription.getItems().isEmpty()) {
////	
////						for (TaStripeSubscriptionItem item : taStripeSubscription.getItems()) {
////							Date dateFinPeriode = null;
////							TaStripePlan itemPlan = daoPlan.findByStripeSubscriptionItem(taStripeSubscription.getItems().iterator().next());
////							//String interval = itemPlan.getInterval();
////							switch (taFrequence.getCodeFrequence()) {
////				//			case "day":
////				//				break;
////				//			case "week":
////				//				break;
////							case TaFrequence.CODE_FREQUENCE_MOIS:
////								boolean finDeMois = localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear())==localDateTime.getDayOfMonth();
////								
////								localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(1);
////				
////								//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
////								// => touver la prochaine date d'échéance ce mois ci ou cette année
////								if(finDeMois) {
////									localDateTimeFinPeriode = localDateTimeFinPeriode.withDayOfMonth(localDateTimeFinPeriode.getMonth().length(localDateTimeFinPeriode.toLocalDate().isLeapYear()));
////								}
////								
////								break;
////							case TaFrequence.CODE_FREQUENCE_AN:
////								localDateTimeFinPeriode = localDateTimeFinPeriode.withYear(localDateTime.getYear()+1);
////								//rajout yann
////								localDateTimeFinPeriode = localDateTimeFinPeriode.minusDays(1);
////								break;
////							default:
////								break;
////							}
////							dateFinPeriode = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
////									
////							TaLEcheance ech = new TaLEcheance();
////							
////							ech.setTaStripeSubscription(taStripeSubscription);
////							ech.setTaStripeSubscriptionItem(item);
////							ech.setTaArticle(itemPlan.getTaArticle());
////							ech.setQteLDocument(new BigDecimal(item.getQuantity()));
////							ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
////							ech.setNbDecimalesPrix(2);
////							ech.setNbDecimalesQte(2);
////							ech.setRemTxLDocument(item.getTaLAbonnement().getRemTxLDocument());
////							ech.setPrixULDocument(itemPlan.getAmount());
////							ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
////							ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
////							ech.setDebAbonnement(taStripeSubscription.getDateDebut());
////							ech.setFinAbonnement(taStripeSubscription.getDateFin());
////							ech.setDebutPeriode(taStripeSubscription.getDateDebut()); //premiere période au début de l'abonnement
////							ech.setFinPeriode(dateFinPeriode);
////							ech.setDateEcheance(now); //premier paiement au moment de la création de l'abonnement
////							ech.calculMontant();
////							
////							listePremieresEcheances.add(ech);
////						}
////				}
////			}
//////		}
////		return listePremieresEcheances;
////	}
////	
////	public List<TaLEcheance> genereProchainesEcheances(TaStripeSubscription_old taStripeSubscription, TaLEcheance echeancePrecedente) {
////		/*
////		 * TODO Attention de ne pas générer plusieurs "la même échéance suivante"
////		 */
////		Date now = new Date();
////		Date nowPlusDeltaAvantEcheance = new Date();
////		LocalDateTime.from(nowPlusDeltaAvantEcheance.toInstant().atZone(ZoneId.systemDefault())).plusDays(MultitenantProxyTimerAbonnement.NB_JOUR_AVANT_ECHEANCE);
////		List<TaLEcheance> listeProchainesEcheances = new ArrayList<>();
//////		if(echeancePrecedente.getIdLEcheance() == 76) {
//////					System.out.println("je suis sur l'écheance recherchée");
//////		}
////		//si la date d'échéance de cette échéance précédantes est dans 10 jours ou moins
////		// on génère
////		if(echeancePrecedente.getDateEcheance().before(nowPlusDeltaAvantEcheance)) {
////				
////
////			TaFrequence taFrequence = echeancePrecedente.getTaStripeSubscription().getTaAbonnement().getTaFrequence();
////			
////			//si l'abo a une frequence et n'est pas annulé
////			if(taFrequence != null && taStripeSubscription.getDateAnnulation()==null) {
////				//rajout yann pour calcul dynamique des prochaines échéances à générer
////				Date dateDebutAbo = echeancePrecedente.getTaStripeSubscription().getDateDebut();
////				Date dateFinAbo = echeancePrecedente.getTaStripeSubscription().getDateFin();
////				Boolean generationProchaine = false;
////				
////				//si il y a une date de fin d'abo et que cette date de fin est après aujourd'hui  Attention : à vérifier quand la fréquence choisie est jours
////				//on vérifie qu'il reste des lignes d'échéances à générer
////				if(dateFinAbo != null) {
////					//si la date de fin est dans le futur 
////					
//////					if(dateFinAbo.after(nowPlusDeltaAvantEcheance)) {
////					if(dateFinAbo.after(now)) {
////						//on va calculer le nb d'échéances restantes a générer.
////						Integer nbEcheancesDejaCree = 0;
////						Integer nbEcheancesTotale = 0;
////						Integer nbEcheancesRestante = 0;
////						
////						LocalDate localDateDebut = dateDebutAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////						LocalDate localDateFin = dateFinAbo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////						
////						//calcul du nombre echéances total pour cet item qu'il devrait y avoir, avec la date début abo, date fin abo et la fréquence.
////						//pour cela, il faut trouver combien de fois la fréquences rentre entre la date de début et la date de fin +1 jour.
////						switch (taFrequence.getCodeFrequence()) {
////						case TaFrequence.CODE_FREQUENCE_AN:
////							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.YEARS.between(localDateDebut, localDateFin.plusDays(1));
////							break;
////						case TaFrequence.CODE_FREQUENCE_JOUR:
////							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.DAYS.between(localDateDebut, localDateFin.plusDays(1));			
////							break;
////						case TaFrequence.CODE_FREQUENCE_MOIS:
////							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.MONTHS.between(localDateDebut, localDateFin.plusDays(1));			
////						    break;
////						case TaFrequence.CODE_FREQUENCE_SEMAINE:
////							nbEcheancesTotale = (int) java.time.temporal.ChronoUnit.WEEKS.between(localDateDebut, localDateFin.plusDays(1));
////							break;
////						}
////						
////						System.out.println(" Nombre de ligne d'échéances total que cet abonnement devrait généré par ligne : "+nbEcheancesTotale);
////						
////						//on va chercher le nombre de lignes d'échéances existantes pour ce subscriptionItem (attention, cette requete remonte même les lignes d'échéances désactivées).
////						nbEcheancesDejaCree = taLEcheanceService.countAllByIdSubscriptionItem(echeancePrecedente.getTaStripeSubscriptionItem().getIdStripeSubscriptionItem());
////						//on calcule le nombre de lignes d'échéances qu'il reste à générer 
////						nbEcheancesRestante = nbEcheancesTotale - nbEcheancesDejaCree;
////						//si il reste des lignes d'échéances à générer, on peut générer la prochaine.
////						if(nbEcheancesRestante > 0) {
////							generationProchaine = true;
////						}
////						
////					//la date de fin est passé 
////					}else {
////						generationProchaine = false;
////					}
////					
////				//il n' y a pas de dates de fin d'abo donc génération de la prochaines ligne d'échéance.
////				}else {
////					generationProchaine = true;
////				}
////				
////				//on lance la génération de la prochaine échéance si :
////				//l'abo n'a pas de date fin ou si il en a une dans le futur et si il reste des lignes d'échéances à générer 
////				if(generationProchaine) {
////								
////						Date date = taStripeSubscription.getQuandCree();
////						
////						LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
////						LocalDateTime localDateTimeDebutPeriode = new Date(echeancePrecedente.getDebutPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
////						LocalDateTime localDateTimeFinPeriode = new Date(echeancePrecedente.getFinPeriode().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
////						LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
////						
////						if(!taStripeSubscription.getItems().isEmpty()) {
////			
////								for (TaStripeSubscriptionItem item : taStripeSubscription.getItems()) {
////									Date dateDebutPeriode = null;
////									Date dateFinPeriode = null;
////									Date dateEcheance= null;
////									
////									localDateTimeDebutPeriode = localDateTimeFinPeriode.plusDays(1);
////										
////									TaStripePlan itemPlan = daoPlan.findByStripeSubscriptionItem(taStripeSubscription.getItems().iterator().next());
////									//String interval = itemPlan.getInterval();
////									switch (taFrequence.getCodeFrequence()) {
////						//			case "day":
////						//				break;
////						//			case "week":
////						//				break;
////									case TaFrequence.CODE_FREQUENCE_MOIS:
////										boolean finDeMois = localDateTime.getMonth().length(localDateTime.toLocalDate().isLeapYear())==localDateTime.getDayOfMonth();
////										
////										localDateTimeFinPeriode = localDateTimeFinPeriode.plusMonths(1);
////						
////										//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
////										// => touver la prochaine date d'échéance ce mois ci ou cette année
////										if(finDeMois) {
////											localDateTimeFinPeriode = localDateTimeFinPeriode.withDayOfMonth(localDateTimeFinPeriode.getMonth().length(localDateTimeFinPeriode.toLocalDate().isLeapYear()));
////										}
////										
////										break;
////									case TaFrequence.CODE_FREQUENCE_AN:
////										//localDateTimeFinPeriode = localDateTimeFinPeriode.withYear(localDateTimeFinPeriode.getYear());
////										localDateTimeFinPeriode = localDateTimeFinPeriode.withYear(localDateTimeFinPeriode.plusYears(1).getYear());
////										break;
////									default:
////										break;
////									}
////									
////									dateFinPeriode = Date.from(localDateTimeFinPeriode.atZone(ZoneId.systemDefault()).toInstant());
////									dateDebutPeriode = Date.from(localDateTimeDebutPeriode.atZone(ZoneId.systemDefault()).toInstant());
////									//à payer au plus tard la veille du début de la période suivante
////									dateEcheance = Date.from(localDateTimeDebutPeriode.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
////											
////									TaLEcheance ech = new TaLEcheance();
////									
////									ech.setTaStripeSubscription(taStripeSubscription);
////									ech.setTaStripeSubscriptionItem(item);
////									ech.setTaArticle(itemPlan.getTaArticle());
////									ech.setQteLDocument(new BigDecimal(item.getQuantity()));
////									ech.setPrixULDocument(itemPlan.getAmount());
////									ech.setRemTxLDocument(item.getTaLAbonnement().getRemTxLDocument());
////									ech.setLibLDocument(itemPlan.getTaArticle().getLibellecArticle());
////									ech.setTauxTvaLDocument(itemPlan.getTaArticle().getTaTva()!=null?itemPlan.getTaArticle().getTaTva().getTauxTva():null);
////									ech.setDebAbonnement(taStripeSubscription.getDateDebut());
////									ech.setFinAbonnement(taStripeSubscription.getDateFin());
////									ech.setU1LDocument(itemPlan.getTaArticle().getTaUnite1().getCodeUnite());
////									ech.setNbDecimalesPrix(2);
////									ech.setNbDecimalesQte(2);
////									ech.setDebutPeriode(dateDebutPeriode); //le jour suivant la fin de période précédente
////									ech.setFinPeriode(dateFinPeriode);
////									ech.setDateEcheance(dateEcheance); //premier paiement au moment de la création de l'abonnement
////									ech.calculMontant();
////			
////									listeProchainesEcheances.add(ech);
////								}//fin boucle sur les items
////								
////						}//fin if items n'est pas empty 
////							
////							
////						
////		
////					
////				}//fin if generationprochaine
////				
////	
////			}//fin if si l'échéance a un abo sans fréquences ou annulé
////				
////				
////				
////		}//fin si date échéance est dans 10 jours ou moins
////
////
////		return listeProchainesEcheances;
////	}
////	
////	public String genereCode( Map<String, String> params) {
////		try {
////			return gencode.genereCodeJPA(TaStripeSubscription_old.class.getSimpleName(),params);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		return "NOUVEAU CODE";
////	}
////
////	public void annuleCode(String code) {
////		try {
////
////			gencode.annulerCodeGenere(gencode.findByCode(TaStripeSubscription_old.class.getSimpleName()),code);
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}
////
////	public void verrouilleCode(String code) {
////		try {
////			gencode.rentreCodeGenere(gencode.findByCode(TaStripeSubscription_old.class.getSimpleName()),code, sessionInfo.getSessionID());
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}
////
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////	// 										ENTITY
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////	
////	public void persist(TaStripeSubscription_old transientInstance) throws CreateException {
////		persist(transientInstance, null);
////	}
////
////	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
////	@WebMethod(operationName = "persistValidationContext")
////	public void persist(TaStripeSubscription_old transientInstance, String validationContext) throws CreateException {
////
////		validateEntity(transientInstance, validationContext);
////
////		dao.persist(transientInstance);
////	}
////
////	public void remove(TaStripeSubscription_old persistentInstance) throws RemoveException {
////		dao.remove(dao.findById(persistentInstance.getIdStripeSubscription()));
////	}
////	
////	public TaStripeSubscription_old merge(TaStripeSubscription_old detachedInstance) {
////		return merge(detachedInstance, null);
////	}
////
////	@Override
////	@WebMethod(operationName = "mergeValidationContext")
////	public TaStripeSubscription_old merge(TaStripeSubscription_old detachedInstance, String validationContext) {
////		validateEntity(detachedInstance, validationContext);
////
////		return dao.merge(detachedInstance);
////	}
////
////	public TaStripeSubscription_old findById(int id) throws FinderException {
////		return dao.findById(id);
////	}
////
////	public TaStripeSubscription_old findByCode(String code) throws FinderException {
////		return dao.findByCode(code);
////	}
////
//////	@RolesAllowed("admin")
////	public List<TaStripeSubscription_old> selectAll() {
////		return dao.selectAll();
////	}
////
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////	// 										DTO
////	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////
////	public List<TaStripeSubscriptionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
////		return null;
////	}
////
////	@Override
////	public List<TaStripeSubscriptionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
////		List<TaStripeSubscription_old> entityList = dao.findWithJPQLQuery(JPQLquery);
////		List<TaStripeSubscriptionDTO> l = null;
////		if(entityList!=null) {
////			l = listEntityToListDTO(entityList);
////		}
////		return l;
////	}
////
////	public TaStripeSubscriptionDTO entityToDTO(TaStripeSubscription_old entity) {
//////		TaStripeSubscriptionDTO dto = new TaStripeSubscriptionDTO();
//////		dto.setId(entity.getIdTCivilite());
//////		dto.setCodeTCivilite(entity.getCodeTCivilite());
//////		return dto;
////		TaStripeSubscriptionMapper mapper = new TaStripeSubscriptionMapper();
////		return mapper.mapEntityToDto(entity, null);
////	}
////
////	public List<TaStripeSubscriptionDTO> listEntityToListDTO(List<TaStripeSubscription_old> entity) {
////		List<TaStripeSubscriptionDTO> l = new ArrayList<TaStripeSubscriptionDTO>();
////
////		for (TaStripeSubscription_old taTCivilite : entity) {
////			l.add(entityToDTO(taTCivilite));
////		}
////
////		return l;
////	}
////
//////	@RolesAllowed("admin")
////	public List<TaStripeSubscriptionDTO> selectAllDTO() {
////		System.out.println("List of TaStripeSubscriptionDTO EJB :");
////		ArrayList<TaStripeSubscriptionDTO> liste = new ArrayList<TaStripeSubscriptionDTO>();
////
////		List<TaStripeSubscription_old> projects = selectAll();
////		for(TaStripeSubscription_old project : projects) {
////			liste.add(entityToDTO(project));
////		}
////
////		return liste;
////	}
////
////	public TaStripeSubscriptionDTO findByIdDTO(int id) throws FinderException {
////		return entityToDTO(findById(id));
////	}
////
////	public TaStripeSubscriptionDTO findByCodeDTO(String code) throws FinderException {
////		return entityToDTO(findByCode(code));
////	}
////
////	@Override
////	public void error(TaStripeSubscriptionDTO dto) {
////		throw new EJBException("Test erreur EJB");
////	}
////
////	@Override
////	public int selectCount() {
////		return dao.selectAll().size();
////		//return 0;
////	}
////	
////	public void mergeDTO(TaStripeSubscriptionDTO dto) throws EJBException {
////		mergeDTO(dto, null);
////	}
////
////	@Override
////	@WebMethod(operationName = "mergeDTOValidationContext")
////	public void mergeDTO(TaStripeSubscriptionDTO dto, String validationContext) throws EJBException {
////		try {
////			TaStripeSubscriptionMapper mapper = new TaStripeSubscriptionMapper();
////			TaStripeSubscription_old entity = null;
////			if(dto.getId()!=null) {
////				entity = dao.findById(dto.getId());
////				if(dto.getVersionObj()!=entity.getVersionObj()) {
////					throw new OptimisticLockException(entity,
////							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
////				} else {
////					 entity = mapper.mapDtoToEntity(dto,entity);
////				}
////			}
////			
////			//dao.merge(entity);
////			dao.detach(entity); //pour passer les controles
////			enregistrerMerge(entity, validationContext);
////		} catch (Exception e) {
////			e.printStackTrace();
////			//throw new CreateException(e.getMessage());
////			throw new EJBException(e.getMessage());
////		}
////	}
////	
////	public void persistDTO(TaStripeSubscriptionDTO dto) throws CreateException {
////		persistDTO(dto, null);
////	}
////
////	@Override
////	@WebMethod(operationName = "persistDTOValidationContext")
////	public void persistDTO(TaStripeSubscriptionDTO dto, String validationContext) throws CreateException {
////		try {
////			TaStripeSubscriptionMapper mapper = new TaStripeSubscriptionMapper();
////			TaStripeSubscription_old entity = mapper.mapDtoToEntity(dto,null);
////			//dao.persist(entity);
////			enregistrerPersist(entity, validationContext);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new CreateException(e.getMessage());
////		}
////	}
////
////	@Override
////	public void removeDTO(TaStripeSubscriptionDTO dto) throws RemoveException {
////		try {
////			TaStripeSubscriptionMapper mapper = new TaStripeSubscriptionMapper();
////			TaStripeSubscription_old entity = mapper.mapDtoToEntity(dto,null);
////			//dao.remove(entity);
////			supprimer(entity);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new RemoveException(e.getMessage());
////		}
////	}
////
////	@Override
////	protected TaStripeSubscription_old refresh(TaStripeSubscription_old persistentInstance) {
////		// TODO Auto-generated method stub
////		return null;
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntityValidationContext")
////	public void validateEntity(TaStripeSubscription_old value, String validationContext) /*throws ExceptLgr*/ {
////		try {
////			if(validationContext==null) validationContext="";
////			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
////			//dao.validate(value); //validation automatique via la JSR bean validation
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntityPropertyValidationContext")
////	public void validateEntityProperty(TaStripeSubscription_old value, String propertyName, String validationContext) {
////		try {
////			if(validationContext==null) validationContext="";
////			validate(value, propertyName, validationContext);
////			//dao.validateField(value,propertyName);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////	}
////
////	@Override
////	@WebMethod(operationName = "validateDTOValidationContext")
////	public void validateDTO(TaStripeSubscriptionDTO dto, String validationContext) {
////		try {
////			TaStripeSubscriptionMapper mapper = new TaStripeSubscriptionMapper();
////			TaStripeSubscription_old entity = mapper.mapDtoToEntity(dto,null);
////			validateEntity(entity,validationContext);
////			
////			//validation automatique via la JSR bean validation
//////			BeanValidator<TaStripeSubscriptionDTO> validator = new BeanValidator<TaStripeSubscriptionDTO>(TaStripeSubscriptionDTO.class);
//////			validator.validate(dto);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////	}
////
////	@Override
////	@WebMethod(operationName = "validateDTOPropertyValidationContext")
////	public void validateDTOProperty(TaStripeSubscriptionDTO dto, String propertyName, String validationContext) {
////		try {
////			TaStripeSubscriptionMapper mapper = new TaStripeSubscriptionMapper();
////			TaStripeSubscription_old entity = mapper.mapDtoToEntity(dto,null);
////			validateEntityProperty(entity,propertyName,validationContext);
////			
////			//validation automatique via la JSR bean validation
//////			BeanValidator<TaStripeSubscriptionDTO> validator = new BeanValidator<TaStripeSubscriptionDTO>(TaStripeSubscriptionDTO.class);
//////			validator.validateField(dto,propertyName);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new EJBException(e.getMessage());
////		}
////
////	}
////	
////	@Override
////	@WebMethod(operationName = "validateDTO")
////	public void validateDTO(TaStripeSubscriptionDTO dto) {
////		validateDTO(dto,null);
////		
////	}
////
////	@Override
////	@WebMethod(operationName = "validateDTOProperty")
////	public void validateDTOProperty(TaStripeSubscriptionDTO dto, String propertyName) {
////		validateDTOProperty(dto,propertyName,null);
////		
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntity")
////	public void validateEntity(TaStripeSubscription_old value) {
////		validateEntity(value,null);
////	}
////
////	@Override
////	@WebMethod(operationName = "validateEntityProperty")
////	public void validateEntityProperty(TaStripeSubscription_old value, String propertyName) {
////		validateEntityProperty(value,propertyName,null);
////		
////	}
//
//}
