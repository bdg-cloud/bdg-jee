package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.controle.dto.TaGenCodeDTO;
import fr.legrain.controle.model.TaGenCode;


public class TaGenCodeMapper implements ILgrMapper<TaGenCodeDTO, TaGenCode> {

	@Override
	public TaGenCode mapDtoToEntity(TaGenCodeDTO dto, TaGenCode entity) {
		if(entity==null)
			entity = new TaGenCode();

		entity.setIdGenCode(dto.getId()!=null?dto.getId():0);
		
	
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaGenCodeDTO mapEntityToDto(TaGenCode entity, TaGenCodeDTO dto) {
		if(dto==null)
			dto = new TaGenCodeDTO();

		dto.setId(entity.getIdGenCode());
		
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
