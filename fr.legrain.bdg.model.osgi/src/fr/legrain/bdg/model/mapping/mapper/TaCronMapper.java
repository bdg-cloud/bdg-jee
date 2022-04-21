package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.cron.model.TaCron;
import fr.legrain.cron.model.dto.TaCronDTO;


public class TaCronMapper implements ILgrMapper<TaCronDTO, TaCron> {

	@Override
	public TaCron mapDtoToEntity(TaCronDTO dto, TaCron entity) {
		if(entity==null)
			entity = new TaCron();

		entity.setIdCron(dto.getId()!=null?dto.getId():0);
		
		entity.setCode(dto.getCode());
		entity.setLibelle(dto.getLibelle());
		entity.setDescription(dto.getDescription());
		entity.setTypeCron(dto.getTypeCron());
		entity.setSchemaTenant(dto.getSchemaTenant());
		entity.setSysteme(dto.isSysteme());
		entity.setActif(dto.isActif());
		entity.setIdentifiantUnique(dto.getIdentifiantUnique());
		entity.setVisible(dto.isVisible());
		entity.setEstUnique(dto.isEstUnique());
		
		entity.setTimerHandle(dto.getTimerHandle());
		
		entity.setSecond(dto.getSecond());
		entity.setMinute(dto.getMinute());
		entity.setHour(dto.getHour());
		entity.setDayOfWeek(dto.getDayOfWeek());
		entity.setDayOfMonth(dto.getDayOfMonth());
		entity.setMonth(dto.getMonth());
		entity.setYear(dto.getYear());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaCronDTO mapEntityToDto(TaCron entity, TaCronDTO dto) {
		if(dto==null)
			dto = new TaCronDTO();

		dto.setId(entity.getIdCron());
		
		dto.setCode(entity.getCode());
		dto.setLibelle(entity.getLibelle());
		dto.setDescription(entity.getDescription());
		dto.setTypeCron(entity.getTypeCron());
		dto.setSchemaTenant(entity.getSchemaTenant());
		dto.setSysteme(entity.isSysteme());
		dto.setActif(entity.isActif());
		dto.setIdentifiantUnique(entity.getIdentifiantUnique());
		dto.setVisible(entity.isVisible());
		dto.setEstUnique(entity.isEstUnique());
		
		dto.setTimerHandle(entity.getTimerHandle());
		
		dto.setSecond(entity.getSecond());
		dto.setMinute(entity.getMinute());
		dto.setHour(entity.getHour());
		dto.setDayOfWeek(entity.getDayOfWeek());
		dto.setDayOfMonth(entity.getDayOfMonth());
		dto.setMonth(entity.getMonth());
		dto.setYear(entity.getYear());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
