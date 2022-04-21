package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.model.TaArticle;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeProductDAO extends IGenericDAO<TaStripeProduct> {

	public List<TaStripeProductDTO> findByCodeLight(String code);
	public List<TaStripeProductDTO> findAllLight();
	
	public TaStripeProduct rechercherProduct(TaArticle article);
	public TaStripeProduct rechercherProduct(String idExterne);
	
	public List<TaStripeProductDTO> selectAllDTOAvecPlan();
	public List<TaStripeProductDTO> selectAllDTOAvecPlanByIdFrequence(Integer idFrequence);
}
