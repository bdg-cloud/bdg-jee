package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.sms.dto.TaContactMessageSMSDTO;
import fr.legrain.sms.model.TaContactMessageSMS;


public class TaContactMessageSMSMapper implements ILgrMapper<TaContactMessageSMSDTO, TaContactMessageSMS> {

	@Override
	public TaContactMessageSMS mapDtoToEntity(TaContactMessageSMSDTO dto, TaContactMessageSMS entity) {
		if(entity==null)
			entity = new TaContactMessageSMS();

		entity.setIdContactMessageSms(dto.getId()!=null?dto.getId():0);
		
		entity.setNumeroTelephone(dto.getNumeroTelephone());

		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaContactMessageSMSDTO mapEntityToDto(TaContactMessageSMS entity, TaContactMessageSMSDTO dto) {
		if(dto==null)
			dto = new TaContactMessageSMSDTO();

		dto.setId(entity.getIdContactMessageSms());
		
		dto.setNumeroTelephone(entity.getNumeroTelephone());

		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
