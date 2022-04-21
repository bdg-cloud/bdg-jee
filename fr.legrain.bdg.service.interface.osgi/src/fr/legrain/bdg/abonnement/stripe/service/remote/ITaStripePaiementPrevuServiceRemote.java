package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.model.TaAbonnement;

@Remote
public interface ITaStripePaiementPrevuServiceRemote extends IGenericCRUDServiceRemote<TaStripePaiementPrevu,TaStripePaiementPrevuDTO>,
														IAbstractLgrDAOServer<TaStripePaiementPrevu>,IAbstractLgrDAOServerDTO<TaStripePaiementPrevuDTO>{
	public static final String validationContext = "STRIPE_PAIEMENT_PREVU";

	public List<TaStripePaiementPrevuDTO> rechercherPaiementPrevuCustomerDTO(String idExterneCustomer);
	public List<TaStripePaiementPrevu> rechercherPaiementPrevuCustomer(String idExterneCustomer);
	
	public void declencherPaiementStripe(TaStripePaiementPrevu paiementPrevu);
	
	public List<TaStripePaiementPrevu> findByAbonnementAndDate(TaAbonnement taAbonnement, Date dateEcheance);
	
//	public List<TaStripeChargeDTO> rechercherChargeDTO();
//	public List<TaStripeCharge> rechercherCharge();
}
