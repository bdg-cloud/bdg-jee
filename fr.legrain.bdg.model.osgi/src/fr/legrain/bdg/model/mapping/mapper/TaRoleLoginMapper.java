package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.login.dto.TaRoleLoginDTO;
import fr.legrain.login.model.TaRoleLogin;



public class TaRoleLoginMapper implements ILgrMapper<TaRoleLoginDTO, TaRoleLogin> {

	@Override
	public TaRoleLogin mapDtoToEntity(TaRoleLoginDTO dto, TaRoleLogin entity) {
		if(entity==null)
			entity = new TaRoleLogin();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRoleLoginDTO mapEntityToDto(TaRoleLogin entity, TaRoleLoginDTO dto) {
		if(dto==null)
			dto = new TaRoleLoginDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
