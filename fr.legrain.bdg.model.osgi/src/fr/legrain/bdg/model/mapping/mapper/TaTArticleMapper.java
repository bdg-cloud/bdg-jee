package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTArticleDTO;
import fr.legrain.article.model.TaTArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTArticleMapper implements ILgrMapper<TaTArticleDTO, TaTArticle> {

	@Override
	public TaTArticle mapDtoToEntity(TaTArticleDTO dto, TaTArticle entity) {
		if(entity==null)
			entity = new TaTArticle();

		entity.setIdTArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTArticle(dto.getCodeArticle());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		//entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTArticleDTO mapEntityToDto(TaTArticle entity, TaTArticleDTO dto) {
		if(dto==null)
			dto = new TaTArticleDTO();

		dto.setId(entity.getIdTArticle());
		
		dto.setCodeArticle(entity.getCodeTArticle());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(dto.getVersionObj());

		return dto;
	}

}
