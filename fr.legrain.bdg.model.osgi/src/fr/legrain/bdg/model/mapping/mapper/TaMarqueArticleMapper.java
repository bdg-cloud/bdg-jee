package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaMarqueArticleMapper implements ILgrMapper<TaMarqueArticleDTO, TaMarqueArticle> {

	@Override
	public TaMarqueArticle mapDtoToEntity(TaMarqueArticleDTO dto, TaMarqueArticle entity) {
		if(entity==null)
			entity = new TaMarqueArticle();

		entity.setIdMarqueArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setCheminImageMarqueArticle(dto.getCheminImageMarqueArticle());
		entity.setCodeMarqueArticle(dto.getCodeMarqueArticle());
		entity.setDesciptionMarqueArticle(dto.getDesciptionMarqueArticle());
		entity.setLibelleMarqueArticle(dto.getLibelleMarqueArticle());
		entity.setNomImageMarqueArticle(dto.getNomImageMarqueArticle());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaMarqueArticleDTO mapEntityToDto(TaMarqueArticle entity, TaMarqueArticleDTO dto) {
		if(dto==null)
			dto = new TaMarqueArticleDTO();

		dto.setId(entity.getIdMarqueArticle());
		
		dto.setCheminImageMarqueArticle(entity.getCheminImageMarqueArticle());
		dto.setCodeMarqueArticle(entity.getCodeMarqueArticle());
		dto.setDesciptionMarqueArticle(entity.getDesciptionMarqueArticle());
		dto.setLibelleMarqueArticle(entity.getLibelleMarqueArticle());
		dto.setNomImageMarqueArticle(entity.getNomImageMarqueArticle());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
