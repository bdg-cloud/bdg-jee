package fr.legrain.bdg.abonnement.stripe.service.remote;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeDiscountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeDiscount;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeDiscountServiceRemote extends IGenericCRUDServiceRemote<TaStripeDiscount,TaStripeDiscountDTO>,
														IAbstractLgrDAOServer<TaStripeDiscount>,IAbstractLgrDAOServerDTO<TaStripeDiscountDTO>{
	public static final String validationContext = "STRIPE_DISCOUNT";

}
