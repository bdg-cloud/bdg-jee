package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.model.TaTelephone;


public class TaTelephoneMapper implements ILgrMapper<TaTelephoneDTO, TaTelephone> {

	@Override
	public TaTelephone mapDtoToEntity(TaTelephoneDTO dto, TaTelephone entity) {
		if(entity==null)
			entity = new TaTelephone();

		entity.setIdTelephone(dto.getId()!=null?dto.getId():0);
		
		entity.setNumeroTelephone(dto.getNumeroTelephone());
		entity.setContact(dto.getContact()); 
		entity.setPosteTelephone(dto.getPosteTelephone());
	
		if(entity.getTaTTel()!=null) entity.getTaTTel().setCodeTTel(dto.getCodeTTel());
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		if(entity.getTaTiers()!=null) entity.getTaTiers().setNomTiers(dto.getNomTiers());
		if(entity.getTaTiers()!=null) entity.getTaTiers().setPrenomTiers(dto.getPrenomTiers());
		
		entity.setCommAdministratifTelephone(LibConversion.booleanToInt(dto.getCommAdministratifTelephone())); 
		entity.setCommCommercialTelephone(LibConversion.booleanToInt(dto.getCommCommercialTelephone())); 
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTelephoneDTO mapEntityToDto(TaTelephone entity, TaTelephoneDTO dto) {
		if(dto==null)
			dto = new TaTelephoneDTO();

		dto.setId(entity.getIdTelephone());
		
		dto.setNumeroTelephone(entity.getNumeroTelephone());
		dto.setContact(entity.getContact()); 
		dto.setPosteTelephone(entity.getPosteTelephone());
		
		if(entity.getTaTTel()!=null) dto.setCodeTTel(entity.getTaTTel().getCodeTTel());
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		if(entity.getTaTiers()!=null) dto.setNomTiers(entity.getTaTiers().getNomTiers());
		if(entity.getTaTiers()!=null) dto.setPrenomTiers(entity.getTaTiers().getPrenomTiers());
		
		dto.setCommAdministratifTelephone(LibConversion.intToBoolean(entity.getCommAdministratifTelephone())); 
		dto.setCommCommercialTelephone(LibConversion.intToBoolean(entity.getCommCommercialTelephone())); 
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
