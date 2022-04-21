package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaCgPartenaireDTO;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaCgPartenaireMapper implements ILgrMapper<TaCgPartenaireDTO, TaCgPartenaire> {

	@Override
	public TaCgPartenaire mapDtoToEntity(TaCgPartenaireDTO dto, TaCgPartenaire entity) {
		if(entity==null)
			entity = new TaCgPartenaire();

		entity.setIdCgPartenaire(dto.getId()!=null?dto.getId():0);
		
		
		entity.setActif(dto.getActif());
		entity.setCgPartenaire(dto.getCgPartenaire());
		entity.setDateCgPartenaire(dto.getDateCgPartenaire());
		entity.setUrl(dto.getUrl());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCgPartenaireDTO mapEntityToDto(TaCgPartenaire entity, TaCgPartenaireDTO dto) {
		if(dto==null)
			dto = new TaCgPartenaireDTO();

		dto.setId(entity.getIdCgPartenaire());
		
		dto.setActif(entity.getActif());
		dto.setCgPartenaire(entity.getCgPartenaire());
		dto.setDateCgPartenaire(entity.getDateCgPartenaire());
		dto.setUrl(entity.getUrl());
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
