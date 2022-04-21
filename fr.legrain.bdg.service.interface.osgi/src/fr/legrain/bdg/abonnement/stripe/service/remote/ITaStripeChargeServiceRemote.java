package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeChargeServiceRemote extends IGenericCRUDServiceRemote<TaStripeCharge,TaStripeChargeDTO>,
														IAbstractLgrDAOServer<TaStripeCharge>,IAbstractLgrDAOServerDTO<TaStripeChargeDTO>{
	public static final String validationContext = "STRIPE_CHARGE";

	public List<TaStripeChargeDTO> rechercherChargeCustomerDTO(String idExterneCustomer);
	public List<TaStripeCharge> rechercherChargeCustomer(String idExterneCustomer);
	
	public List<TaStripeChargeDTO> rechercherChargeDTO();
	public List<TaStripeCharge> rechercherCharge();
}
