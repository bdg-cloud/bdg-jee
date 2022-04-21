package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.model.TaEmail;


public class TaEmailMapper implements ILgrMapper<TaEmailDTO, TaEmail> {

	@Override
	public TaEmail mapDtoToEntity(TaEmailDTO dto, TaEmail entity) {
		if(entity==null)
			entity = new TaEmail();

		entity.setIdEmail(dto.getId()!=null?dto.getId():0);
		entity.setAdresseEmail(dto.getAdresseEmail()); 
	
		if(entity.getTaTEmail()!=null) entity.getTaTEmail().setCodeTEmail(dto.getCodeTEmail());
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setCommAdministratifEmail(LibConversion.booleanToInt(dto.getCommAdministratifEmail())); 
		entity.setCommCommercialEmail(LibConversion.booleanToInt(dto.getCommCommercialEmail())); 
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaEmailDTO mapEntityToDto(TaEmail entity, TaEmailDTO dto) {
		if(dto==null)
			dto = new TaEmailDTO();

		dto.setId(entity.getIdEmail());
		
		dto.setAdresseEmail(entity.getAdresseEmail()); 
		
		if(entity.getTaTEmail()!=null) dto.setCodeTEmail(entity.getTaTEmail().getCodeTEmail());
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setCommAdministratifEmail(LibConversion.intToBoolean(entity.getCommAdministratifEmail())); 
		dto.setCommCommercialEmail(LibConversion.intToBoolean(entity.getCommCommercialEmail())); 
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
