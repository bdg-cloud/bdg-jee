package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeChargeDAO extends IGenericDAO<TaStripeCharge> {

	public List<TaStripeChargeDTO> findByCodeLight(String code);
	public List<TaStripeChargeDTO> findAllLight();
	
	public List<TaStripeChargeDTO> rechercherChargeCustomerDTO(String idExterneCustomer);
	public List<TaStripeCharge> rechercherChargeCustomer(String idExterneCustomer);
	
	public List<TaStripeChargeDTO> rechercherChargeDTO();
	public List<TaStripeCharge> rechercherCharge();
}
