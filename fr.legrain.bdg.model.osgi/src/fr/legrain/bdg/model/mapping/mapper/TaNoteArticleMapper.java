package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaNoteArticleDTO;
import fr.legrain.article.model.TaNoteArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaNoteArticleMapper implements ILgrMapper<TaNoteArticleDTO, TaNoteArticle> {

	@Override
	public TaNoteArticle mapDtoToEntity(TaNoteArticleDTO dto, TaNoteArticle entity) {
		if(entity==null)
			entity = new TaNoteArticle();

		entity.setIdNoteArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setDateNoteArticle(dto.getDateNoteArticle());
		entity.setNoteArticle(dto.getNoteArticle());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaNoteArticleDTO mapEntityToDto(TaNoteArticle entity, TaNoteArticleDTO dto) {
		if(dto==null)
			dto = new TaNoteArticleDTO();

		dto.setId(entity.getIdNoteArticle());
		
		dto.setDateNoteArticle(entity.getDateNoteArticle());
		dto.setNoteArticle(entity.getNoteArticle());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
