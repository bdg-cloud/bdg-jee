package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.abonnement.stripe.model.TaStripeTSource;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaStripeTSourceServiceRemote extends IGenericCRUDServiceRemote<TaStripeTSource,TaStripeTSourceDTO>,
														IAbstractLgrDAOServer<TaStripeTSource>,IAbstractLgrDAOServerDTO<TaStripeTSourceDTO>{
	public static final String validationContext = "STRIPE_T_SOURCE";

	public TaStripeTSource rechercherSource(TaCompteBanque compteBanque);
	public TaStripeTSource rechercherSource(TaCarteBancaire carteBanque);
	public List<TaStripeTSource> rechercherSource(TaTiers tiers);
	public List<TaStripeTSource> rechercherSourceCustomer(String idExterneCustomer);
	public TaStripeTSource rechercherSource(String idExterneSource);
	
	public List<TaStripeTSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer);
}
