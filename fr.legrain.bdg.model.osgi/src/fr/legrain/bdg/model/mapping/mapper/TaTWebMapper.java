package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTWebDTO;
import fr.legrain.tiers.model.TaTWeb;


public class TaTWebMapper implements ILgrMapper<TaTWebDTO, TaTWeb> {

	@Override
	public TaTWeb mapDtoToEntity(TaTWebDTO dto, TaTWeb entity) {
		if(entity==null)
			entity = new TaTWeb();

		entity.setIdTWeb(dto.getId()!=null?dto.getId():0);
		entity.setCodeTWeb(dto.getCodeTWeb());
		entity.setLiblTWeb(dto.getLiblTWeb());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTWebDTO mapEntityToDto(TaTWeb entity, TaTWebDTO dto) {
		if(dto==null)
			dto = new TaTWebDTO();

		dto.setId(entity.getIdTWeb());
		dto.setCodeTWeb(entity.getCodeTWeb());
		dto.setLiblTWeb(entity.getLiblTWeb());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
