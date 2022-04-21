package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTNoteArticleDTO;
import fr.legrain.article.model.TaTNoteArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTNoteArticleMapper implements ILgrMapper<TaTNoteArticleDTO, TaTNoteArticle> {

	@Override
	public TaTNoteArticle mapDtoToEntity(TaTNoteArticleDTO dto, TaTNoteArticle entity) {
		if(entity==null)
			entity = new TaTNoteArticle();

		entity.setIdTNoteArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setLiblTNoteArticle(dto.getLiblTNoteArticle());
		entity.setCodeTNoteArticle(dto.getCodeTNoteArticle());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTNoteArticleDTO mapEntityToDto(TaTNoteArticle entity, TaTNoteArticleDTO dto) {
		if(dto==null)
			dto = new TaTNoteArticleDTO();

		dto.setId(entity.getIdTNoteArticle());
		
		dto.setLiblTNoteArticle(entity.getLiblTNoteArticle());
		dto.setCodeTNoteArticle(entity.getCodeTNoteArticle());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
