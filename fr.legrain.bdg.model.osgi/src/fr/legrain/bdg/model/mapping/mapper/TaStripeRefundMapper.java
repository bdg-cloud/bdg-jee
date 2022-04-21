package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeRefundDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeRefundDTO;
import fr.legrain.abonnement.stripe.model.TaStripeRefund;
import fr.legrain.abonnement.stripe.model.TaStripeRefund;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;


public class TaStripeRefundMapper implements ILgrMapper<TaStripeRefundDTO, TaStripeRefund> {

	@Override
	public TaStripeRefund mapDtoToEntity(TaStripeRefundDTO dto, TaStripeRefund entity) {
		if(entity==null)
			entity = new TaStripeRefund();

		entity.setIdStripeRefund(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());

		
		if(entity.getTaStripeCharge()!=null) {
			entity.getTaStripeCharge().setIdExterne(dto.getIdExterneCharge());
			entity.getTaStripeCharge().setIdStripeCharge(dto.getIdStripeCharge());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeRefundDTO mapEntityToDto(TaStripeRefund entity, TaStripeRefundDTO dto) {
		if(dto==null)
			dto = new TaStripeRefundDTO();

		dto.setId(entity.getIdStripeRefund());
		
		dto.setIdExterne(entity.getIdExterne());
		
		if(entity.getTaStripeCharge()!=null) {
			dto.setIdExterneCharge(entity.getTaStripeCharge().getIdExterne());
			dto.setIdStripeCharge(entity.getTaStripeCharge().getIdStripeCharge());
		}
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
