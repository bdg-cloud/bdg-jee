package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.model.TaLAbonnement;

@Remote
public interface ITaStripePlanServiceRemote extends IGenericCRUDServiceRemote<TaStripePlan,TaStripePlanDTO>,
														IAbstractLgrDAOServer<TaStripePlan>,IAbstractLgrDAOServerDTO<TaStripePlanDTO>{
	public static final String validationContext = "STRIPE_PLAN";
	
	public TaStripePlan findByNickname(String nickname);
	public List<TaStripePlan> findByIdProduct(int idStripePtoduct);
	public List<TaStripePlanDTO> findByIdProductDTO(int idStripePtoduct);
	public List<TaStripePlanDTO> findByIdArticleDTO(int idArticle);
	
	//public TaStripePlan findByStripeSubscriptionItem(TaStripeSubscriptionItem taStripeSubscriptionItem);
	public TaStripePlan findByLigneAbo(TaLAbonnement ligneAbo);
	public TaStripePlan findByIdLigneAbo(Integer idLigne);
	public List<TaStripePlanDTO> findByIdProductDTOAndByIdFrequence(Integer idStripePtoduct, Integer idFrequence);
	public List<TaStripePlanDTO> findByIdArticleDTOAndByIdFrequence(Integer idArticle, Integer idFrequence);
	public List<TaStripePlanDTO> findByIdArticleDTOAndByNbMois(Integer idArticle, Integer nbMois);
}
