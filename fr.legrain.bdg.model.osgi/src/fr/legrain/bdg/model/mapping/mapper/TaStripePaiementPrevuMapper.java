package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripePaiementPrevuMapper implements ILgrMapper<TaStripePaiementPrevuDTO, TaStripePaiementPrevu> {

	@Override
	public TaStripePaiementPrevu mapDtoToEntity(TaStripePaiementPrevuDTO dto, TaStripePaiementPrevu entity) {
		if(entity==null)
			entity = new TaStripePaiementPrevu();

		entity.setIdStripePaiementPrevu(dto.getId()!=null?dto.getId():0);
		

		
		if(entity.getTaStripeCharge()!=null) {
			entity.getTaStripeCharge().setIdExterne(dto.getIdExterneCharge());
			entity.getTaStripeCharge().setIdStripeCharge(dto.getIdStripeCharge());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripePaiementPrevuDTO mapEntityToDto(TaStripePaiementPrevu entity, TaStripePaiementPrevuDTO dto) {
		if(dto==null)
			dto = new TaStripePaiementPrevuDTO();

		dto.setId(entity.getIdStripePaiementPrevu());
		
		
		if(entity.getTaStripeCharge()!=null) {
			dto.setIdExterneCharge(entity.getTaStripeCharge().getIdExterne());
			dto.setIdStripeCharge(entity.getTaStripeCharge().getIdStripeCharge());
		}
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
