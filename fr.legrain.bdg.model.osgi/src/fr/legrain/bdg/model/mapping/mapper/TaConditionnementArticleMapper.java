package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaConditionnementArticleDTO;
import fr.legrain.article.model.TaConditionnementArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaConditionnementArticleMapper implements ILgrMapper<TaConditionnementArticleDTO, TaConditionnementArticle> {

	@Override
	public TaConditionnementArticle mapDtoToEntity(TaConditionnementArticleDTO dto, TaConditionnementArticle entity) {
		if(entity==null)
			entity = new TaConditionnementArticle();

		entity.setIdConditionnementArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setCode(dto.getCode());
		entity.setHauteur(dto.getHauteur());
		entity.setLargeur(dto.getLargeur());
		entity.setLibelle(dto.getLibelle());
		entity.setLongueur(dto.getLongueur());
		entity.setNbUnite(dto.getNbUnite());
		entity.setPoids(dto.getPoids());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaConditionnementArticleDTO mapEntityToDto(TaConditionnementArticle entity, TaConditionnementArticleDTO dto) {
		if(dto==null)
			dto = new TaConditionnementArticleDTO();

		dto.setId(entity.getIdConditionnementArticle());
		
		dto.setCode(entity.getCode());
		dto.setHauteur(entity.getHauteur());
		dto.setLargeur(entity.getLargeur());
		dto.setLibelle(entity.getLibelle());
		dto.setLongueur(entity.getLongueur());
		dto.setNbUnite(entity.getNbUnite());
		dto.setPoids(entity.getPoids());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
