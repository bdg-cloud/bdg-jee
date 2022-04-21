package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tache.dto.TaTypeNotificationDTO;
import fr.legrain.tache.model.TaTypeNotification;
import fr.legrain.tache.model.TaTypeNotification;


public class TaTypeNotificationMapper implements ILgrMapper<TaTypeNotificationDTO, TaTypeNotification> {

	@Override
	public TaTypeNotification mapDtoToEntity(TaTypeNotificationDTO dto, TaTypeNotification entity) {
		if(entity==null)
			entity = new TaTypeNotification();

		entity.setIdTypeNotification(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTypeNotification(dto.getCodeTypeNotification());
		entity.setLibelleTypeNotification(dto.getLibelleTypeNotification());
//		entity.setAdresse3Adresse(dto.getAdresse3Adresse());
//		entity.setCodepostalAdresse(dto.getCodepostalAdresse());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypeNotificationDTO mapEntityToDto(TaTypeNotification entity, TaTypeNotificationDTO dto) {
		if(dto==null)
			dto = new TaTypeNotificationDTO();

		dto.setId(entity.getIdTypeNotification());
		
		dto.setCodeTypeNotification(entity.getCodeTypeNotification());
		dto.setLibelleTypeNotification(entity.getLibelleTypeNotification());
//		dto.setAdresse3Adresse(entity.getAdresse3Adresse());
//		dto.setCodepostalAdresse(entity.getCodepostalAdresse());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
