package fr.legrain.bdg.abonnement.stripe.service.remote;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeAccountServiceRemote extends IGenericCRUDServiceRemote<TaStripeAccount,TaStripeAccountDTO>,
														IAbstractLgrDAOServer<TaStripeAccount>,IAbstractLgrDAOServerDTO<TaStripeAccountDTO>{
	public static final String validationContext = "STRIPE_ACCOUNT";
	public TaStripeAccount findInstance();

}
