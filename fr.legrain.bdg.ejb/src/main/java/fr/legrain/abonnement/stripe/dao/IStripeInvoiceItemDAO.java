package fr.legrain.abonnement.stripe.dao;

import java.util.List;

import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IStripeInvoiceItemDAO extends IGenericDAO<TaStripeInvoiceItem> {

	public List<TaStripeInvoiceItemDTO> findByCodeLight(String code);
	public List<TaStripeInvoiceItemDTO> findAllLight();
	
	public List<TaStripeInvoiceItemDTO> rechercherInvoiceItemDTO(String idExterneInvoice);
	public List<TaStripeInvoiceItem> rechercherInvoiceItem(String idExterneInvoice);
}
