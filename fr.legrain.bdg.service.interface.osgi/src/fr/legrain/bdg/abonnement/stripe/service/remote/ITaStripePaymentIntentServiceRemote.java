package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.model.SWTDocument;

@Remote
public interface ITaStripePaymentIntentServiceRemote extends IGenericCRUDServiceRemote<TaStripePaymentIntent,TaStripePaymentIntentDTO>,
														IAbstractLgrDAOServer<TaStripePaymentIntent>,IAbstractLgrDAOServerDTO<TaStripePaymentIntentDTO>{
	public static final String validationContext = "STRIPE_PAYMENT_INTENT";

	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentCustomerDTO(String idExterneCustomer);
	public List<TaStripePaymentIntent> rechercherPaymentIntentCustomer(String idExterneCustomer);
	
	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentDTO();
	public List<TaStripePaymentIntent> rechercherPaymentIntent();
	
	public TaStripePaymentIntent findPaiementNonCapture(SWTDocument doc);
}
