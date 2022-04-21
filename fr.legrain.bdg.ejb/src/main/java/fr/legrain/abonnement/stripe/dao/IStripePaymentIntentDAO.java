package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.SWTDocument;

//@Remote
public interface IStripePaymentIntentDAO extends IGenericDAO<TaStripePaymentIntent> {

	public List<TaStripePaymentIntentDTO> findByCodeLight(String code);
	public List<TaStripePaymentIntentDTO> findAllLight();
	
	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentCustomerDTO(String idExterneCustomer);
	public List<TaStripePaymentIntent> rechercherPaymentIntentCustomer(String idExterneCustomer);
	
	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentDTO();
	public List<TaStripePaymentIntent> rechercherPaymentIntent();
	
	public TaStripePaymentIntent findPaiementNonCapture(SWTDocument doc);
}
