package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaLiensDTO;
import fr.legrain.tiers.model.TaLiens;


public class TaLiensMapper implements ILgrMapper<TaLiensDTO, TaLiens> {

	@Override
	public TaLiens mapDtoToEntity(TaLiensDTO dto, TaLiens entity) {
		if(entity==null)
			entity = new TaLiens();

		entity.setIdLiens(dto.getId()!=null?dto.getId():0);
		entity.setAdresseLiens(dto.getAdresseLiens()); 
	
		if(entity.getTaTLiens()!=null) entity.getTaTLiens().setCodeTLiens(dto.getCodeTLiens());
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers()); 
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaLiensDTO mapEntityToDto(TaLiens entity, TaLiensDTO dto) {
		if(dto==null)
			dto = new TaLiensDTO();

		dto.setId(entity.getIdLiens());
		
		dto.setAdresseLiens(entity.getAdresseLiens()); 
		
		if(entity.getTaTLiens()!=null) dto.setCodeTLiens(entity.getTaTLiens().getCodeTLiens());
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
