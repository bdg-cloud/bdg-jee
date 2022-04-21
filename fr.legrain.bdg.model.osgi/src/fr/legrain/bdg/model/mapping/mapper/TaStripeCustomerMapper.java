package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeCustomerMapper implements ILgrMapper<TaStripeCustomerDTO, TaStripeCustomer> {

	@Override
	public TaStripeCustomer mapDtoToEntity(TaStripeCustomerDTO dto, TaStripeCustomer entity) {
		if(entity==null)
			entity = new TaStripeCustomer();

		entity.setIdStripeCustomer(dto.getId()!=null?dto.getId():0);
		
		entity.setCurrency(dto.getCurrency());
		entity.setDescription(dto.getDescription());
		entity.setEmail(dto.getEmail());
		entity.setIdExterne(dto.getIdExterne());
		
		if(entity.getTaDiscount()!=null) {
			entity.getTaDiscount().setIdExterne(dto.getIdExterneDiscount());
			entity.getTaDiscount().setIdStripeDiscount(dto.getIdStripeDiscount());
		}
		if(entity.getTaSourceDefault()!=null) {
			entity.getTaSourceDefault().setIdExterne(dto.getIdExterneSourceDefault());
			entity.getTaSourceDefault().setIdStripeSource(dto.getIdStripeSourceDefault());
		}
		if(entity.getTaTiers()!=null) {
			entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
			entity.getTaTiers().setIdTiers(dto.getIdTiers());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeCustomerDTO mapEntityToDto(TaStripeCustomer entity, TaStripeCustomerDTO dto) {
		if(dto==null)
			dto = new TaStripeCustomerDTO();

		dto.setId(entity.getIdStripeCustomer());
		
		dto.setCurrency(entity.getCurrency());
		dto.setDescription(entity.getDescription());
		dto.setEmail(entity.getEmail());
		dto.setIdExterne(entity.getIdExterne());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		if(entity.getTaDiscount()!=null) {
			dto.setIdExterneDiscount(entity.getTaDiscount().getIdExterne());
			dto.setIdStripeDiscount(entity.getTaDiscount().getIdStripeDiscount());
		}
		if(entity.getTaSourceDefault()!=null) {
			dto.setIdExterneSourceDefault(entity.getTaSourceDefault().getIdExterne());
			dto.setIdStripeSourceDefault(entity.getTaSourceDefault().getIdStripeSource());
		}
		if(entity.getTaTiers()!=null) {
			dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
			dto.setIdTiers(entity.getTaTiers().getIdTiers());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
