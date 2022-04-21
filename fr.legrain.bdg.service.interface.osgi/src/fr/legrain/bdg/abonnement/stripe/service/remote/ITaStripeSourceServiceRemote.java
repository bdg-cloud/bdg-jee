package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaStripeSourceServiceRemote extends IGenericCRUDServiceRemote<TaStripeSource,TaStripeSourceDTO>,
														IAbstractLgrDAOServer<TaStripeSource>,IAbstractLgrDAOServerDTO<TaStripeSourceDTO>{
	public static final String validationContext = "STRIPE_SOURCE";

	public TaStripeSource rechercherSource(TaCompteBanque compteBanque);
	public TaStripeSource rechercherSource(TaCarteBancaire carteBanque);
	public List<TaStripeSource> rechercherSource(TaTiers tiers);
	public List<TaStripeSource> rechercherSourceCustomer(String idExterneCustomer);
	public TaStripeSource rechercherSource(String idExterneSource);
	
	public List<TaStripeSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer);
	public TaStripeSource rechercherSourceManuelle(String codeStripeTSource);
}
