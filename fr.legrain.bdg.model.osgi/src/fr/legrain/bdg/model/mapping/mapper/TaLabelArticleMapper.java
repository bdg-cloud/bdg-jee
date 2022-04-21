package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaLabelArticleDTO;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaLabelArticleMapper implements ILgrMapper<TaLabelArticleDTO, TaLabelArticle> {

	@Override
	public TaLabelArticle mapDtoToEntity(TaLabelArticleDTO dto, TaLabelArticle entity) {
		if(entity==null)
			entity = new TaLabelArticle();

		entity.setIdLabelArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setCheminImageLabelArticle(dto.getCheminImageLabelArticle());
		entity.setCodeLabelArticle(dto.getCodeLabelArticle());
		entity.setDesciptionLabelArticle(dto.getDesciptionLabelArticle());
		entity.setLibelleLabelArticle(dto.getLibelleLabelArticle());
		entity.setNomImageLabelArticle(dto.getNomImageLabelArticle());
		entity.setBlobLogo(dto.getBlobLogo());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLabelArticleDTO mapEntityToDto(TaLabelArticle entity, TaLabelArticleDTO dto) {
		if(dto==null)
			dto = new TaLabelArticleDTO();

		dto.setId(entity.getIdLabelArticle());
		
		dto.setCheminImageLabelArticle(entity.getCheminImageLabelArticle());
		dto.setCodeLabelArticle(entity.getCodeLabelArticle());
		dto.setDesciptionLabelArticle(entity.getDesciptionLabelArticle());
		dto.setLibelleLabelArticle(entity.getLibelleLabelArticle());
		dto.setNomImageLabelArticle(entity.getNomImageLabelArticle());
		dto.setBlobLogo(entity.getBlobLogo());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
