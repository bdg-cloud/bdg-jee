//package fr.legrain.bdg.abonnement.stripe.service.remote;
//
//import java.util.List;
//import java.util.Map;
//
//import javax.ejb.Remote;
//
//import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription_old;
//import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
//import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
//import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
//import fr.legrain.document.model.TaLEcheance;
//
//@Remote
//public interface ITaStripeSubscriptionServiceRemote extends IGenericCRUDServiceRemote<TaStripeSubscription_old,TaStripeSubscriptionDTO>,
//														IAbstractLgrDAOServer<TaStripeSubscription_old>,IAbstractLgrDAOServerDTO<TaStripeSubscriptionDTO>{
//	public static final String validationContext = "STRIPE_SUBSCRIPTION";
//
////	public List<TaStripeSubscriptionDTO> rechercherSubscriptionCustomerDTO(String idExterneCustomer);
////	public List<TaStripeSubscriptionDTO> rechercherSubscriptionNonStripeCustomerDTO(String idExterneCustomer);
////	public List<TaStripeSubscription_old> rechercherSubscriptionCustomer(String idExterneCustomer);
////	
////	public TaStripeSubscriptionDTO findDTOByIdAbonnement(Integer idAbonnement);
////	public List<TaStripeSubscriptionDTO> findAllDTOByIdTiers(Integer idTiers);
////	public List<TaStripeSubscriptionDTO> selectAllASuspendre();
////	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers);
////	
////	public TaStripeSubscription_old findByIdLEcheance(Integer idLEcheance);
////	
////	public List<TaStripeSubscriptionDTO> rechercherSubscriptionPlanDTO(String idExternePlan);
////	public List<TaStripeSubscription_old> rechercherSubscriptionPlan(String idExternePlan);
////	
////	public List<TaLEcheance> genereProchainesEcheances(TaStripeSubscription_old taStripeSubscription, TaLEcheance echeancePrecedente);
////	public List<TaLEcheance> generePremieresEcheances(TaStripeSubscription_old taStripeSubscription);
////	
////	public boolean annulerSubscriptionNonStripe(int idSubscriptionNonStripe, String commentaireAnnulation, boolean annuleDerniereEcheance);
////	
////	public String genereCode( Map<String, String> params);
////	public void annuleCode(String code);
////	public void verrouilleCode(String code);
//}
