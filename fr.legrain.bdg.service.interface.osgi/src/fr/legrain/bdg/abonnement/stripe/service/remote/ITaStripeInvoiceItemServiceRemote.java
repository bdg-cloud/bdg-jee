package fr.legrain.bdg.abonnement.stripe.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaStripeInvoiceItemServiceRemote extends IGenericCRUDServiceRemote<TaStripeInvoiceItem,TaStripeInvoiceItemDTO>,
														IAbstractLgrDAOServer<TaStripeInvoiceItem>,IAbstractLgrDAOServerDTO<TaStripeInvoiceItemDTO>{
	public static final String validationContext = "STRIPE_INVOICE_ITEM";

	public List<TaStripeInvoiceItemDTO> rechercherInvoiceItemDTO(String idExterneInvoice);
	public List<TaStripeInvoiceItem> rechercherInvoiceItem(String idExterneInvoice);
}
