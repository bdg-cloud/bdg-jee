package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaCategorieArticleMapper implements ILgrMapper<TaCategorieArticleDTO, TaCategorieArticle> {

	@Override
	public TaCategorieArticle mapDtoToEntity(TaCategorieArticleDTO dto, TaCategorieArticle entity) {
		if(entity==null)
			entity = new TaCategorieArticle();

		entity.setIdCategorieArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setCheminImageCategorieArticle(dto.getCheminImageCategorieArticle());
		entity.setCodeCategorieArticle(dto.getCodeCategorieArticle());
		entity.setDesciptionCategorieArticle(dto.getDesciptionCategorieArticle());
		entity.setLibelleCategorieArticle(dto.getLibelleCategorieArticle());
		entity.setNomImageCategorieArticle(dto.getNomImageCategorieArticle());
		entity.setUrlRewritingCategorieArticle(dto.getUrlRewritingCategorieArticle());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCategorieArticleDTO mapEntityToDto(TaCategorieArticle entity, TaCategorieArticleDTO dto) {
		if(dto==null)
			dto = new TaCategorieArticleDTO();

		dto.setId(entity.getIdCategorieArticle());
		
		dto.setCheminImageCategorieArticle(entity.getCheminImageCategorieArticle());
		dto.setCodeCategorieArticle(entity.getCodeCategorieArticle());
		dto.setDesciptionCategorieArticle(entity.getDesciptionCategorieArticle());
		dto.setLibelleCategorieArticle(entity.getLibelleCategorieArticle());
		dto.setNomImageCategorieArticle(entity.getNomImageCategorieArticle());
		dto.setUrlRewritingCategorieArticle(entity.getUrlRewritingCategorieArticle());
		
		
		dto.setBlobImageGrand(entity.getBlobImageGrand());
		dto.setBlobImageMoyen(entity.getBlobImageMoyen());
		dto.setBlobImageOrigine(entity.getBlobImageOrigine());
		dto.setBlobImagePetit(entity.getBlobImagePetit());
		dto.setAltText(entity.getAltText());
		dto.setAriaText(entity.getAriaText());
		dto.setChecksum(entity.getChecksum());
		dto.setUrlImage(entity.getUrlImage());
		dto.setMime(entity.getMime());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
