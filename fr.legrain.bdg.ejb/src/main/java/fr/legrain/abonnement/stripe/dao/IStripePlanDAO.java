package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaLAbonnement;

//@Remote
public interface IStripePlanDAO extends IGenericDAO<TaStripePlan> {

	
	public TaStripePlan findByNickname(String nickname);
	
	public List<TaStripePlanDTO> findByCodeLight(String code);
	public List<TaStripePlanDTO> findAllLight();
	
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
