package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTerminalMobileDTO;
import fr.legrain.tiers.model.TaTerminalMobile;
import fr.legrain.tiers.model.TaTerminalMobile;
import fr.legrain.tiers.model.TaTerminalMobile;


public class TaTerminalMobileMapper implements ILgrMapper<TaTerminalMobileDTO, TaTerminalMobile> {

	@Override
	public TaTerminalMobile mapDtoToEntity(TaTerminalMobileDTO dto, TaTerminalMobile entity) {
		if(entity==null)
			entity = new TaTerminalMobile();

		entity.setIdTerminalMobile(dto.getId()!=null?dto.getId():0);
		
		entity.setDateDerniereConnexion(dto.getDateDerniereConnexion());
		entity.setAndroidRegistrationToken(dto.getAndroidRegistrationToken());
		
//		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTerminalMobileDTO mapEntityToDto(TaTerminalMobile entity, TaTerminalMobileDTO dto) {
		if(dto==null)
			dto = new TaTerminalMobileDTO();

		dto.setId(entity.getIdTerminalMobile());
		
		dto.setDateDerniereConnexion(entity.getDateDerniereConnexion());
		dto.setAndroidRegistrationToken(entity.getAndroidRegistrationToken());
		
//		if(entity.getTaTiers()!=null) dto.setCodeTAdr(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
