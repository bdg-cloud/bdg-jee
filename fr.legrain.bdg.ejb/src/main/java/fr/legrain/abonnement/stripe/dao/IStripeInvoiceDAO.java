package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeInvoiceDAO extends IGenericDAO<TaStripeInvoice> {

	public List<TaStripeInvoiceDTO> findByCodeLight(String code);
	public List<TaStripeInvoiceDTO> findAllLight();
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscriptionDTO(String subscriptionStripeID, Integer limite);
	public List<TaStripeInvoice> rechercheInvoiceStripeForSubscription(String subscriptionStripeID, Integer limite);
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForCustomerDTO(String customerStripeID, Integer limite);
	public List<TaStripeInvoice> rechercheInvoiceStripeForCustomer(String customerStripeID, Integer limite);
}
