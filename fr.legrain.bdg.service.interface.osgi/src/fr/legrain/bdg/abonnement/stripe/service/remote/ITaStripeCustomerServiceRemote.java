package fr.legrain.bdg.abonnement.stripe.service.remote;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaStripeCustomerServiceRemote extends IGenericCRUDServiceRemote<TaStripeCustomer,TaStripeCustomerDTO>,
														IAbstractLgrDAOServer<TaStripeCustomer>,IAbstractLgrDAOServerDTO<TaStripeCustomerDTO>{
	public static final String validationContext = "STRIPE_CUSTOMER";

	public TaStripeCustomer rechercherCustomer(TaTiers tiers);
	public TaStripeCustomer rechercherCustomer(String idExterne);
}
