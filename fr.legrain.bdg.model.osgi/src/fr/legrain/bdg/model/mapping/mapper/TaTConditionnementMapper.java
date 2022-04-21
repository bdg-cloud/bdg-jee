package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTConditionnementDTO;
import fr.legrain.article.model.TaTConditionnement;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaTConditionnementMapper implements ILgrMapper<TaTConditionnementDTO, TaTConditionnement> {

	@Override
	public TaTConditionnement mapDtoToEntity(TaTConditionnementDTO dto, TaTConditionnement entity) {
		if(entity==null)
			entity = new TaTConditionnement();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeType(dto.getCodeType());
		entity.setHauteur(dto.getHauteur());
		entity.setLargeur(dto.getLargeur());
		entity.setLibelle(dto.getLibelle());
		entity.setLongueur(dto.getLongueur());
		entity.setPoids(dto.getPoids());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTConditionnementDTO mapEntityToDto(TaTConditionnement entity, TaTConditionnementDTO dto) {
		if(dto==null)
			dto = new TaTConditionnementDTO();

		dto.setId(entity.getId());
		
		dto.setCodeType(entity.getCodeType());
		dto.setHauteur(entity.getHauteur());
		dto.setLargeur(entity.getLargeur());
		dto.setLibelle(entity.getLibelle());
		dto.setLongueur(entity.getLongueur());
		dto.setPoids(entity.getPoids());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
