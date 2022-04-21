package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeInvoiceServiceRemote extends IGenericCRUDServiceRemote<TaStripeInvoice,TaStripeInvoiceDTO>,
														IAbstractLgrDAOServer<TaStripeInvoice>,IAbstractLgrDAOServerDTO<TaStripeInvoiceDTO>{
	public static final String validationContext = "STRIPE_INVOICE";
	

	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscriptionDTO(String subscriptionStripeID, Integer limite);
	public List<TaStripeInvoice> rechercheInvoiceStripeForSubscription(String subscriptionStripeID, Integer limite);
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForCustomerDTO(String customerStripeID, Integer limite);
	public List<TaStripeInvoice> rechercheInvoiceStripeForCustomer(String customerStripeID, Integer limite);


}
