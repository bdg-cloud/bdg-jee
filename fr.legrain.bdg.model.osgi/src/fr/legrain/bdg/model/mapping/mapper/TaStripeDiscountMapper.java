package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeDiscountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeDiscount;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeDiscountMapper implements ILgrMapper<TaStripeDiscountDTO, TaStripeDiscount> {

	@Override
	public TaStripeDiscount mapDtoToEntity(TaStripeDiscountDTO dto, TaStripeDiscount entity) {
		if(entity==null)
			entity = new TaStripeDiscount();

		entity.setIdStripeDiscount(dto.getId()!=null?dto.getId():0);
		
		entity.setEnd(dto.getEnd());
		entity.setIdExterne(dto.getIdExterne());
		entity.setStart(dto.getStart());

		
		if(entity.getTaCoupon()!=null) {
			entity.getTaCoupon().setIdExterne(dto.getIdExterneCoupon());
			entity.getTaCoupon().setIdStripeCoupon(dto.getIdStripeCoupon());
		}
		
		if(entity.getTaCustomer()!=null) {
			entity.getTaCustomer().setIdExterne(dto.getIdExterne());
			entity.getTaCustomer().setIdStripeCustomer(dto.getIdStripeCustomer());
		}
		
		if(entity.getTaAbonnement()!=null) {
			entity.getTaAbonnement().setIdExterne(dto.getIdExterne());
			entity.getTaAbonnement().setIdDocument(dto.getIdAbonnement());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeDiscountDTO mapEntityToDto(TaStripeDiscount entity, TaStripeDiscountDTO dto) {
		if(dto==null)
			dto = new TaStripeDiscountDTO();

		dto.setId(entity.getIdStripeDiscount());
		
		dto.setEnd(entity.getEnd());
		dto.setIdExterne(entity.getIdExterne());
		dto.setStart(entity.getStart());

		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		if(entity.getTaCoupon()!=null) {
			dto.setIdExterneCoupon(entity.getTaCoupon().getIdExterne());
			dto.setIdStripeCoupon(entity.getTaCoupon().getIdStripeCoupon());
		}
		
		if(entity.getTaCustomer()!=null) {
			dto.setIdExterne(entity.getTaCustomer().getIdExterne());
			dto.setIdStripeCustomer(entity.getTaCustomer().getIdStripeCustomer());
		}
		
		if(entity.getTaAbonnement()!=null) {
			dto.setIdExterne(entity.getTaAbonnement().getIdExterne());
			dto.setIdAbonnement(entity.getTaAbonnement().getIdDocument());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
