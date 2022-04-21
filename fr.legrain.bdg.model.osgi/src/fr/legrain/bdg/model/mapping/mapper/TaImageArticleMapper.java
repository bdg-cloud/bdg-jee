package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaImageArticleMapper implements ILgrMapper<TaImageArticleDTO, TaImageArticle> {

	@Override
	public TaImageArticle mapDtoToEntity(TaImageArticleDTO dto, TaImageArticle entity) {
		if(entity==null)
			entity = new TaImageArticle();

		entity.setIdImageArticle(dto.getId()!=null?dto.getId():0);
		
//		entity.setChecksum();
		entity.setCheminImageArticle(dto.getCheminImageArticle());
		entity.setDefautImageArticle(dto.getDefautImageArticle());
		entity.setNomImageArticle(dto.getNomImageArticle());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaImageArticleDTO mapEntityToDto(TaImageArticle entity, TaImageArticleDTO dto) {
		if(dto==null)
			dto = new TaImageArticleDTO();

		dto.setId(entity.getIdImageArticle());
		
		dto.setCheminImageArticle(entity.getCheminImageArticle());
		dto.setDefautImageArticle(entity.getDefautImageArticle());
		dto.setNomImageArticle(entity.getNomImageArticle());
		dto.setPosition(entity.getPosition());
		
		
		dto.setBlobImageOrigine(entity.getBlobImageOrigine());
		dto.setBlobImagePetit(entity.getBlobImagePetit());
		dto.setBlobImageMoyen(entity.getBlobImageMoyen());
		dto.setBlobImageGrand(entity.getBlobImageGrand());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
