package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTReceptionDTO;
import fr.legrain.article.model.TaTReception;
import fr.legrain.bdg.model.mapping.ILgrMapper;



public class TaTReceptionMapper implements ILgrMapper<TaTReceptionDTO, TaTReception> {

	@Override
	public TaTReception mapDtoToEntity(TaTReceptionDTO dto, TaTReception entity) {
		if(entity==null)
			entity = new TaTReception();

		entity.setIdTReception(dto.getId()!=null?dto.getId():0);
		entity.setCodeTReception(dto.getCodeTReception());
		entity.setLiblTReception(dto.getLiblTReception());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTReceptionDTO mapEntityToDto(TaTReception entity, TaTReceptionDTO dto) {
		if(dto==null)
			dto = new TaTReceptionDTO();

		dto.setId(entity.getIdTReception());
		dto.setCodeTReception(entity.getCodeTReception());
		dto.setLiblTReception(entity.getLiblTReception());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
