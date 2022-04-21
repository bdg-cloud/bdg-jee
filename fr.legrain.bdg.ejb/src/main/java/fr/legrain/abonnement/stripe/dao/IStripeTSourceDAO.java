package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.abonnement.stripe.model.TaStripeTSource;
import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface IStripeTSourceDAO extends IGenericDAO<TaStripeTSource> {

	public List<TaStripeTSourceDTO> findByCodeLight(String code);
	public List<TaStripeTSourceDTO> findAllLight();
	
	public TaStripeTSource rechercherSource(TaCompteBanque comteBanque);
	public TaStripeTSource rechercherSource(TaCarteBancaire carteBancaire);
	public List<TaStripeTSource> rechercherSource(TaTiers tiers);
	public List<TaStripeTSource> rechercherSourceCustomer(String idExterneCustomer);
	public TaStripeTSource rechercherSource(String idExterneSource);
	
	public List<TaStripeTSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer);
}
