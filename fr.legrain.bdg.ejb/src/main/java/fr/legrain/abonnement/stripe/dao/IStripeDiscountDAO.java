package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeDiscountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeDiscount;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeDiscountDAO extends IGenericDAO<TaStripeDiscount> {

	public List<TaStripeDiscountDTO> findByCodeLight(String code);
	public List<TaStripeDiscountDTO> findAllLight();
}
