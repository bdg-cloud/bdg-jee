package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeRefundDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripeRefund;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeRefundDAO extends IGenericDAO<TaStripeRefund> {

	public List<TaStripeRefundDTO> findByCodeLight(String code);
	public List<TaStripeRefundDTO> findAllLight();
	
}
