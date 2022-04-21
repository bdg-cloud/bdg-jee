package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.push.dto.TaContactMessagePushDTO;
import fr.legrain.push.model.TaContactMessagePush;


public class TaContactMessagePushMapper implements ILgrMapper<TaContactMessagePushDTO, TaContactMessagePush> {

	@Override
	public TaContactMessagePush mapDtoToEntity(TaContactMessagePushDTO dto, TaContactMessagePush entity) {
		if(entity==null)
			entity = new TaContactMessagePush();

		entity.setIdContactMessagePush(dto.getId()!=null?dto.getId():0);
		
//		entity.setNumeroTelephone(dto.getNumeroTelephone());

		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaContactMessagePushDTO mapEntityToDto(TaContactMessagePush entity, TaContactMessagePushDTO dto) {
		if(dto==null)
			dto = new TaContactMessagePushDTO();

		dto.setId(entity.getIdContactMessagePush());
		
//		dto.setNumeroTelephone(entity.getNumeroTelephone());

		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
