package fr.legrain.bdg.abonnement.stripe.service.remote;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeCouponServiceRemote extends IGenericCRUDServiceRemote<TaStripeCoupon,TaStripeCouponDTO>,
														IAbstractLgrDAOServer<TaStripeCoupon>,IAbstractLgrDAOServerDTO<TaStripeCouponDTO>{
	public static final String validationContext = "STRIPE_COUPON";

}
