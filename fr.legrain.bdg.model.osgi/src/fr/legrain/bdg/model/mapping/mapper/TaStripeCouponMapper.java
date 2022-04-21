package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeCouponMapper implements ILgrMapper<TaStripeCouponDTO, TaStripeCoupon> {

	@Override
	public TaStripeCoupon mapDtoToEntity(TaStripeCouponDTO dto, TaStripeCoupon entity) {
		if(entity==null)
			entity = new TaStripeCoupon();

		entity.setIdStripeCoupon(dto.getId()!=null?dto.getId():0);
		
		entity.setAmountOff(dto.getAmountOff());
		entity.setCurrency(dto.getCurrency());
		entity.setDuration(dto.getDuration());
		entity.setDurationInMonths(dto.getDurationInMonths());
		entity.setIdExterne(dto.getIdExterne());
		entity.setName(dto.getName());
		entity.setPercentOff(dto.getPercentOff());
		entity.setRedeemBy(dto.getRedeemBy());
		entity.setValid(dto.getValid());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeCouponDTO mapEntityToDto(TaStripeCoupon entity, TaStripeCouponDTO dto) {
		if(dto==null)
			dto = new TaStripeCouponDTO();

		dto.setId(entity.getIdStripeCoupon());
		
		dto.setAmountOff(entity.getAmountOff());
		dto.setCurrency(entity.getCurrency());
		dto.setDuration(entity.getDuration());
		dto.setDurationInMonths(entity.getDurationInMonths());
		dto.setIdExterne(entity.getIdExterne());
		dto.setName(entity.getName());
		dto.setPercentOff(entity.getPercentOff());
		dto.setRedeemBy(entity.getRedeemBy());
		dto.setValid(entity.getValid());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
