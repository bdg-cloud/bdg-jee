package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeAccountDAO extends IGenericDAO<TaStripeAccount> {

	public List<TaStripeAccountDTO> findByCodeLight(String code);
	public List<TaStripeAccountDTO> findAllLight();
	
	public TaStripeAccount findInstance();
}
