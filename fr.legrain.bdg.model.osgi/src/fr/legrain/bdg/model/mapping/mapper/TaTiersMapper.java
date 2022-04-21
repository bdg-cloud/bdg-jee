package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


public class TaTiersMapper implements ILgrMapper<TaTiersDTO, TaTiers> {

	@Override
	public TaTiers mapDtoToEntity(TaTiersDTO dto, TaTiers entity) {
		if(entity==null)
			entity = new TaTiers();

		entity.setIdTiers(dto.getId()!=null?dto.getId():0);
		
		entity.setNomTiers(dto.getNomTiers());
		entity.setPrenomTiers(dto.getPrenomTiers()); 
		entity.setCodeTiers(dto.getCodeTiers()); 
		entity.setCodeCompta(dto.getCodeCompta());
		entity.setCompte(dto.getCompte());
		entity.setActifTiers(LibConversion.booleanToInt(dto.getActifTiers()));
		entity.setTtcTiers(LibConversion.booleanToInt(dto.getTtcTiers())); 
		//TODO a finir ejb
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTiersDTO mapEntityToDto(TaTiers entity, TaTiersDTO dto) {
		if(dto==null)
			dto = new TaTiersDTO();

		dto.setId(entity.getIdTiers());
		
		dto.setNomTiers(entity.getNomTiers());
		dto.setPrenomTiers(entity.getPrenomTiers()); 
		dto.setCodeTiers(entity.getCodeTiers()); 
		dto.setCodeCompta(entity.getCodeCompta());
		dto.setCompte(entity.getCompte()); 
		dto.setActifTiers(LibConversion.intToBoolean(entity.getActifTiers())); 
		dto.setTtcTiers(LibConversion.intToBoolean(entity.getTtcTiers())); 
		if(entity.getTaTTiers()!=null) {
			dto.setCodeTiers(entity.getTaTTiers().getCodeTTiers());
		}
		//TODO a finir ejb
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
