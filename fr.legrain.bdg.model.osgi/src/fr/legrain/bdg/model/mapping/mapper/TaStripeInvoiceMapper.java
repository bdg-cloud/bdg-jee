package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeInvoiceMapper implements ILgrMapper<TaStripeInvoiceDTO, TaStripeInvoice> {

	@Override
	public TaStripeInvoice mapDtoToEntity(TaStripeInvoiceDTO dto, TaStripeInvoice entity) {
		if(entity==null)
			entity = new TaStripeInvoice();

		entity.setIdStripeInvoice(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());
		
		if(entity.getTaCustomer()!=null) {
			entity.getTaCustomer().setIdExterne(dto.getIdExterneCustomer());
			entity.getTaCustomer().setIdStripeCustomer(dto.getIdStripeCustomer());
		}
		if(entity.getTaAbonnement()!=null) {
			entity.getTaAbonnement().setIdExterne(dto.getIdExterneSubscription());
			entity.getTaAbonnement().setIdDocument(dto.getIdAbonnement());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeInvoiceDTO mapEntityToDto(TaStripeInvoice entity, TaStripeInvoiceDTO dto) {
		if(dto==null)
			dto = new TaStripeInvoiceDTO();

		dto.setId(entity.getIdStripeInvoice());
		
		dto.setIdExterne(entity.getIdExterne());
				
		if(entity.getTaCustomer()!=null) {
			dto.setIdExterneCustomer(entity.getTaCustomer().getIdExterne());
			dto.setIdStripeCustomer(entity.getTaCustomer().getIdStripeCustomer());
		}
		if(entity.getTaAbonnement()!=null) {
			dto.setIdExterneSubscription(entity.getTaAbonnement().getIdExterne());
			dto.setIdAbonnement(entity.getTaAbonnement().getIdDocument());
		}
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
