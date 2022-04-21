package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeProductServiceRemote extends IGenericCRUDServiceRemote<TaStripeProduct,TaStripeProductDTO>,
														IAbstractLgrDAOServer<TaStripeProduct>,IAbstractLgrDAOServerDTO<TaStripeProductDTO>{
	public static final String validationContext = "STRIPE_PRODUCT";
	
	public TaStripeProduct rechercherProduct(TaArticle article);
	public TaStripeProduct rechercherProduct(String idExterne);
	
	public List<TaStripeProductDTO> selectAllDTOAvecPlan();
	public List<TaStripeProductDTO> selectAllDTOAvecPlanByIdFrequence(Integer idFrequence);

}
