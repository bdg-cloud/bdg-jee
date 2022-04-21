//package fr.legrain.bdg.abonnement.stripe.service.remote;
//
//import java.util.List;
//
//import javax.ejb.Remote;
//
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem_old;
//import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
//import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
//import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
//
//@Remote
//public interface ITaStripeSubscriptionItemServiceRemote extends IGenericCRUDServiceRemote<TaStripeSubscriptionItem_old,TaStripeSubscriptionItemDTO>,
//														IAbstractLgrDAOServer<TaStripeSubscriptionItem_old>,IAbstractLgrDAOServerDTO<TaStripeSubscriptionItemDTO>{
//	public static final String validationContext = "STRIPE_SUBSCRIPTION_ITEM";
//	
////	public List<TaStripeSubscriptionItemDTO> rechercherSubscriptionItemDTO(int idSubscription);
////
////	public List<TaStripeSubscriptionItemDTO> rechercherSubscriptionItemDTO(String idExterneSubscription);
////	public List<TaStripeSubscriptionItem_old> rechercherSubscriptionItem(String idExterneSubscription);
//}
