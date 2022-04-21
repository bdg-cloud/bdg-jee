package fr.legrain.bdg.abonnement.stripe.service.remote;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeRefundDTO;
import fr.legrain.abonnement.stripe.model.TaStripeRefund;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeRefundServiceRemote extends IGenericCRUDServiceRemote<TaStripeRefund,TaStripeRefundDTO>,
														IAbstractLgrDAOServer<TaStripeRefund>,IAbstractLgrDAOServerDTO<TaStripeRefundDTO>{
	public static final String validationContext = "STRIPE_REFUND";
}
