package fr.legrain.bdg.abonnement.stripe.service.remote;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeBankAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeBankAccountServiceRemote extends IGenericCRUDServiceRemote<TaStripeBankAccount,TaStripeBankAccountDTO>,
														IAbstractLgrDAOServer<TaStripeBankAccount>,IAbstractLgrDAOServerDTO<TaStripeBankAccountDTO>{
	public static final String validationContext = "STRIPE_BANK_ACCOUNT";

}
