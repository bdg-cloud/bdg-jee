package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripePlanMapper implements ILgrMapper<TaStripePlanDTO, TaStripePlan> {

	@Override
	public TaStripePlan mapDtoToEntity(TaStripePlanDTO dto, TaStripePlan entity) {
		if(entity==null)
			entity = new TaStripePlan();

		entity.setIdStripePlan(dto.getId()!=null?dto.getId():0);
		
		entity.setActive(dto.getActive());
		entity.setCurrency(dto.getCurrency());
		entity.setInterval(dto.getLiblFrequence());
		entity.setIntervalCount(dto.getIntervalCount());
		entity.setNickname(dto.getNickname());
		entity.setTrialPrediodDays(dto.getTrialPrediodDays());
		
		if(entity.getTaArticle()!=null) {
			entity.getTaArticle().setCodeArticle(dto.getCodeArticle());
			entity.getTaArticle().setIdArticle(dto.getIdArticle());
		}
		if(entity.getTaStripeProduct()!=null) {
			entity.getTaStripeProduct().setIdExterne(dto.getIdExterne());
			entity.getTaStripeProduct().setIdStripeProduct(dto.getIdStripeProduct());
		}
		
		entity.setNbMois(dto.getNbMois());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripePlanDTO mapEntityToDto(TaStripePlan entity, TaStripePlanDTO dto) {
		if(dto==null)
			dto = new TaStripePlanDTO();

		dto.setId(entity.getIdStripePlan());
		
		dto.setActive(entity.getActive());
		dto.setCurrency(entity.getCurrency());
		//dto.setInterval(entity.getInterval());
		dto.setIntervalCount(entity.getIntervalCount());
		dto.setNickname(entity.getNickname());
		dto.setTrialPrediodDays(entity.getTrialPrediodDays());
		
		if(entity.getTaArticle()!=null) {
			dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
			dto.setIdArticle(entity.getTaArticle().getIdArticle());
		}
		if(entity.getTaStripeProduct()!=null) {
			dto.setIdExterne(entity.getTaStripeProduct().getIdExterne());
			dto.setIdStripeProduct(entity.getTaStripeProduct().getIdStripeProduct());
		}
		
		if(entity.getTaFrequence() != null) {
			dto.setCodeFrequence(entity.getTaFrequence().getCodeFrequence());
			dto.setIdFrequence(entity.getTaFrequence().getIdFrequence());
			dto.setLiblFrequence(entity.getTaFrequence().getLiblFrequence());
			dto.setInterval(entity.getTaFrequence().getLiblFrequence());
		}
		
		dto.setNbMois(entity.getNbMois());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
