package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface IStripeCustomerDAO extends IGenericDAO<TaStripeCustomer> {

	public List<TaStripeCustomerDTO> findByCodeLight(String code);
	public List<TaStripeCustomerDTO> findAllLight();
	
	public TaStripeCustomer rechercherCustomer(TaTiers tiers);
	public TaStripeCustomer rechercherCustomer(String idExterne);
}
