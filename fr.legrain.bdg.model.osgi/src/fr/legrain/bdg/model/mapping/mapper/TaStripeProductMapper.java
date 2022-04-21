package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaStripeProductMapper implements ILgrMapper<TaStripeProductDTO, TaStripeProduct> {

	@Override
	public TaStripeProduct mapDtoToEntity(TaStripeProductDTO dto, TaStripeProduct entity) {
		if(entity==null)
			entity = new TaStripeProduct();

		entity.setIdStripeProduct(dto.getId()!=null?dto.getId():0);
		
		entity.setActive(dto.getActive());
		entity.setCaption(dto.getCaption());
		entity.setDescription(dto.getDescription());
		entity.setIdExterne(dto.getIdExterne());
		entity.setProductType(dto.getProductType());
		
		if(entity.getTaArticle()!=null) {
			entity.getTaArticle().setCodeArticle(dto.getCodeArticle());
			entity.getTaArticle().setIdArticle(dto.getIdArticle());
		}
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaStripeProductDTO mapEntityToDto(TaStripeProduct entity, TaStripeProductDTO dto) {
		if(dto==null)
			dto = new TaStripeProductDTO();

		dto.setId(entity.getIdStripeProduct());
		
		dto.setActive(entity.getActive());
		dto.setCaption(entity.getCaption());
		dto.setDescription(entity.getDescription());
		dto.setIdExterne(entity.getIdExterne());
		dto.setProductType(entity.getProductType());
		
		if(entity.getTaArticle()!=null) {
			dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
			dto.setIdArticle(entity.getTaArticle().getIdArticle());
			dto.setName(entity.getTaArticle().getLibellecArticle());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
