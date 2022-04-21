package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTAdrDTO;
import fr.legrain.tiers.model.TaTAdr;


public class TaTAdrMapper implements ILgrMapper<TaTAdrDTO, TaTAdr> {

	@Override
	public TaTAdr mapDtoToEntity(TaTAdrDTO dto, TaTAdr entity) {
		if(entity==null)
			entity = new TaTAdr();

		entity.setIdTAdr(dto.getId()!=null?dto.getId():0);
		entity.setCodeTAdr(dto.getCodeTAdr());
		entity.setLiblTAdr(dto.getLiblTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTAdrDTO mapEntityToDto(TaTAdr entity, TaTAdrDTO dto) {
		if(dto==null)
			dto = new TaTAdrDTO();

		dto.setId(entity.getIdTAdr());
		dto.setCodeTAdr(entity.getCodeTAdr());
		dto.setLiblTAdr(entity.getLiblTAdr());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
