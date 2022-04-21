package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripePaymentIntentMapper implements ILgrMapper<TaStripePaymentIntentDTO, TaStripePaymentIntent> {

	@Override
	public TaStripePaymentIntent mapDtoToEntity(TaStripePaymentIntentDTO dto, TaStripePaymentIntent entity) {
		if(entity==null)
			entity = new TaStripePaymentIntent();

		entity.setIdStripePaymentIntent(dto.getId()!=null?dto.getId():0);
		
		entity.setIdExterne(dto.getIdExterne());

		
		if(entity.getTaReglement()!=null) {
			entity.getTaReglement().setCodeDocument(dto.getCodeDocument());
			entity.getTaReglement().setIdDocument(dto.getIdDocument());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripePaymentIntentDTO mapEntityToDto(TaStripePaymentIntent entity, TaStripePaymentIntentDTO dto) {
		if(dto==null)
			dto = new TaStripePaymentIntentDTO();

		dto.setId(entity.getIdStripePaymentIntent());
		
		dto.setIdExterne(entity.getIdExterne());
		
		if(entity.getTaReglement()!=null)  {
			dto.setIdDocument(entity.getTaReglement().getIdDocument());
			dto.setCodeDocument(entity.getTaReglement().getCodeDocument());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
