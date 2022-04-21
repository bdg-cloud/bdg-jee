package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaCgvDTO;
import fr.legrain.moncompte.model.TaCgv;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaCgvMapper implements ILgrMapper<TaCgvDTO, TaCgv> {

	@Override
	public TaCgv mapDtoToEntity(TaCgvDTO dto, TaCgv entity) {
		if(entity==null)
			entity = new TaCgv();

		entity.setIdCgv(dto.getId()!=null?dto.getId():0);
		
		
		entity.setActif(dto.getActif());
		entity.setCgv(dto.getCgv());
		entity.setDateCgv(dto.getDateCgv());
		entity.setUrl(dto.getUrl());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCgvDTO mapEntityToDto(TaCgv entity, TaCgvDTO dto) {
		if(dto==null)
			dto = new TaCgvDTO();

		dto.setId(entity.getIdCgv());
		
		dto.setActif(entity.getActif());
		dto.setCgv(entity.getCgv());
		dto.setDateCgv(entity.getDateCgv());
		dto.setUrl(entity.getUrl());
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
