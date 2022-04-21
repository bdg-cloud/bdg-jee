package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTNoteTiersDTO;
import fr.legrain.tiers.model.TaTNoteTiers;


public class TaTNoteTiersMapper implements ILgrMapper<TaTNoteTiersDTO, TaTNoteTiers> {

	@Override
	public TaTNoteTiers mapDtoToEntity(TaTNoteTiersDTO dto, TaTNoteTiers entity) {
		if(entity==null)
			entity = new TaTNoteTiers();

		entity.setIdTNoteTiers(dto.getId()!=null?dto.getId():0);
		entity.setCodeTNoteTiers(dto.getCodeTNoteTiers());
		entity.setLiblTNoteTiers(dto.getLiblTNoteTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTNoteTiersDTO mapEntityToDto(TaTNoteTiers entity, TaTNoteTiersDTO dto) {
		if(dto==null)
			dto = new TaTNoteTiersDTO();

		dto.setId(entity.getIdTNoteTiers());
		dto.setCodeTNoteTiers(entity.getCodeTNoteTiers());
		dto.setLiblTNoteTiers(entity.getLiblTNoteTiers());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
