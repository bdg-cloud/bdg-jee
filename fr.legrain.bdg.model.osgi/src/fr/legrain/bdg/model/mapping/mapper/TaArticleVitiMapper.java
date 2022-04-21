package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticleViti;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaArticleVitiMapper implements ILgrMapper<TaArticleDTO, TaArticleViti> {

	@Override
	public TaArticleViti mapDtoToEntity(TaArticleDTO dto, TaArticleViti entity) {
		if(entity==null)
			entity = new TaArticleViti();

		entity.setIdArticle(dto.getId()!=null?dto.getId():0);
		
//		entity.setAdresse1Adresse(dto.getAdresse1Adresse());
//		entity.setAdresse2Adresse(dto.getAdresse2Adresse());
//		entity.setAdresse3Adresse(dto.getAdresse3Adresse());
//		entity.setCodepostalAdresse(dto.getCodepostalAdresse());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaArticleDTO mapEntityToDto(TaArticleViti entity, TaArticleDTO dto) {
		if(dto==null)
			dto = new TaArticleDTO();

		dto.setId(entity.getIdArticle());
		
//		dto.setAdresse1Adresse(entity.getAdresse1Adresse());
//		dto.setAdresse2Adresse(entity.getAdresse2Adresse());
//		dto.setAdresse3Adresse(entity.getAdresse3Adresse());
//		dto.setCodepostalAdresse(entity.getCodepostalAdresse());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
