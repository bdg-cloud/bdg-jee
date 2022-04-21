package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeCouponDAO extends IGenericDAO<TaStripeCoupon> {

	public List<TaStripeCouponDTO> findByCodeLight(String code);
	public List<TaStripeCouponDTO> findAllLight();
}
