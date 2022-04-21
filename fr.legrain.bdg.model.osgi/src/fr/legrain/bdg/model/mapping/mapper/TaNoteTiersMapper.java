package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaNoteTiersDTO;
import fr.legrain.tiers.model.TaNoteTiers;


public class TaNoteTiersMapper implements ILgrMapper<TaNoteTiersDTO, TaNoteTiers> {

	@Override
	public TaNoteTiers mapDtoToEntity(TaNoteTiersDTO dto, TaNoteTiers entity) {
		if(entity==null)
			entity = new TaNoteTiers();

		entity.setIdNoteTiers(dto.getId()!=null?dto.getId():0);
		
		entity.setDateNoteTiers(dto.getDateNoteTiers());
		entity.setNoteTiers(dto.getNoteTiers()); 
	
		if(entity.getTaTNoteTiers()!=null) entity.getTaTNoteTiers().setCodeTNoteTiers(dto.getCodeTNoteTiers());
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaNoteTiersDTO mapEntityToDto(TaNoteTiers entity, TaNoteTiersDTO dto) {
		if(dto==null)
			dto = new TaNoteTiersDTO();

		dto.setId(entity.getIdNoteTiers());
		
		dto.setDateNoteTiers(entity.getDateNoteTiers());
		dto.setNoteTiers(entity.getNoteTiers()); 
		
		if(entity.getTaTNoteTiers()!=null) dto.setCodeTNoteTiers(entity.getTaTNoteTiers().getCodeTNoteTiers());
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
