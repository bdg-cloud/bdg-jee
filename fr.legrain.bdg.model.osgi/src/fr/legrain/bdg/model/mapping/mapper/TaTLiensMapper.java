package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTLiensDTO;
import fr.legrain.tiers.model.TaTLiens;


public class TaTLiensMapper implements ILgrMapper<TaTLiensDTO, TaTLiens> {

	@Override
	public TaTLiens mapDtoToEntity(TaTLiensDTO dto, TaTLiens entity) {
		if(entity==null)
			entity = new TaTLiens();

		entity.setIdTLiens(dto.getId()!=null?dto.getId():0);
		entity.setCodeTLiens(dto.getCodeTLiens());
		entity.setLiblTLiens(dto.getLiblTLiens());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTLiensDTO mapEntityToDto(TaTLiens entity, TaTLiensDTO dto) {
		if(dto==null)
			dto = new TaTLiensDTO();

		dto.setId(entity.getIdTLiens());
		dto.setCodeTLiens(entity.getCodeTLiens());
		dto.setLiblTLiens(entity.getLiblTLiens());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
