package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.controle.dto.TaGenCodeDTO;
import fr.legrain.controle.dto.TaGenCodeExDTO;
import fr.legrain.controle.model.TaGenCode;
import fr.legrain.controle.model.TaGenCodeEx;


public class TaGenCodeExMapper implements ILgrMapper<TaGenCodeExDTO, TaGenCodeEx> {

	@Override
	public TaGenCodeEx mapDtoToEntity(TaGenCodeExDTO dto, TaGenCodeEx entity) {
		if(entity==null)
			entity = new TaGenCodeEx();

		entity.setIdGenCodeEx(dto.getId()!=null?dto.getId():0);
		
	
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaGenCodeExDTO mapEntityToDto(TaGenCodeEx entity, TaGenCodeExDTO dto) {
		if(dto==null)
			dto = new TaGenCodeExDTO();

		dto.setId(entity.getIdGenCodeEx());
		
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
