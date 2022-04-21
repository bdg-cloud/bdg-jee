package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO;
import fr.legrain.servicewebexterne.model.TaTAuthentification;


public class TaTAuthentificationMapper implements ILgrMapper<TaTAuthentificationDTO, TaTAuthentification> {

	@Override
	public TaTAuthentification mapDtoToEntity(TaTAuthentificationDTO dto, TaTAuthentification entity) {
		if(entity==null)
			entity = new TaTAuthentification();

		entity.setIdTAuthentification(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTAuthentification(dto.getCodeTAuthentification());
		entity.setDescriptionTAuthentification(dto.getDescriptionTAuthentification());
		entity.setLibelleTAuthentification(dto.getLibelleTAuthentification());
		entity.setSysteme(dto.isSysteme());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTAuthentificationDTO mapEntityToDto(TaTAuthentification entity, TaTAuthentificationDTO dto) {
		if(dto==null)
			dto = new TaTAuthentificationDTO();

		dto.setId(entity.getIdTAuthentification());
		
		dto.setCodeTAuthentification(entity.getCodeTAuthentification());
		dto.setLibelleTAuthentification(entity.getLibelleTAuthentification());
		dto.setDescriptionTAuthentification(entity.getDescriptionTAuthentification());
		dto.setSysteme(entity.isSysteme());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
