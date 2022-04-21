package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeInvoiceItemMapper implements ILgrMapper<TaStripeInvoiceItemDTO, TaStripeInvoiceItem> {

	@Override
	public TaStripeInvoiceItem mapDtoToEntity(TaStripeInvoiceItemDTO dto, TaStripeInvoiceItem entity) {
		if(entity==null)
			entity = new TaStripeInvoiceItem();

		entity.setIdStripeInvoiceItem(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());

		
		if(entity.getTaStripeInvoice()!=null) {
			entity.getTaStripeInvoice().setIdExterne(dto.getIdExterne());
			entity.getTaStripeInvoice().setIdStripeInvoice(dto.getIdStripeInvoice());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeInvoiceItemDTO mapEntityToDto(TaStripeInvoiceItem entity, TaStripeInvoiceItemDTO dto) {
		if(dto==null)
			dto = new TaStripeInvoiceItemDTO();

		dto.setId(entity.getIdStripeInvoiceItem());
		
		dto.setIdExterne(entity.getIdExterne());
		
		
		if(entity.getTaStripeInvoice()!=null) {
			dto.setIdExterneInvoice(entity.getTaStripeInvoice().getIdExterne());
			dto.setIdStripeInvoice(entity.getTaStripeInvoice().getIdStripeInvoice());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
