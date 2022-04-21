package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface IStripeSourceDAO extends IGenericDAO<TaStripeSource> {

	public List<TaStripeSourceDTO> findByCodeLight(String code);
	public List<TaStripeSourceDTO> findAllLight();
	
	public TaStripeSource rechercherSource(TaCompteBanque comteBanque);
	public TaStripeSource rechercherSource(TaCarteBancaire carteBancaire);
	public List<TaStripeSource> rechercherSource(TaTiers tiers);
	public List<TaStripeSource> rechercherSourceCustomer(String idExterneCustomer);
	public TaStripeSource rechercherSource(String idExterneSource);
	
	public List<TaStripeSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer);
	public TaStripeSource rechercherSourceManuelle(String codeStripeTSource);
}
