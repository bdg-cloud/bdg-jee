package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeChargeMapper implements ILgrMapper<TaStripeChargeDTO, TaStripeCharge> {

	@Override
	public TaStripeCharge mapDtoToEntity(TaStripeChargeDTO dto, TaStripeCharge entity) {
		if(entity==null)
			entity = new TaStripeCharge();

		entity.setIdStripeCharge(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());

		
		if(entity.getTaReglement()!=null) {
			entity.getTaReglement().setCodeDocument(dto.getCodeDocument());
			entity.getTaReglement().setIdDocument(dto.getIdDocument());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeChargeDTO mapEntityToDto(TaStripeCharge entity, TaStripeChargeDTO dto) {
		if(dto==null)
			dto = new TaStripeChargeDTO();

		dto.setId(entity.getIdStripeCharge());
		
		dto.setIdExterne(entity.getIdExterne());
		
		if(entity.getTaReglement()!=null)  {
			dto.setIdDocument(entity.getTaReglement().getIdDocument());
			dto.setCodeDocument(entity.getTaReglement().getCodeDocument());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
