package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTTelDTO;
import fr.legrain.tiers.model.TaTTel;


public class TaTTelMapper implements ILgrMapper<TaTTelDTO, TaTTel> {

	@Override
	public TaTTel mapDtoToEntity(TaTTelDTO dto, TaTTel entity) {
		if(entity==null)
			entity = new TaTTel();

		entity.setIdTTel(dto.getId()!=null?dto.getId():0);
		entity.setCodeTTel(dto.getCodeTTel());
		entity.setLiblTTel(dto.getLiblTTel());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTTelDTO mapEntityToDto(TaTTel entity, TaTTelDTO dto) {
		if(dto==null)
			dto = new TaTTelDTO();

		dto.setId(entity.getIdTTel());
		dto.setCodeTTel(entity.getCodeTTel());
		dto.setLiblTTel(entity.getLiblTTel());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
