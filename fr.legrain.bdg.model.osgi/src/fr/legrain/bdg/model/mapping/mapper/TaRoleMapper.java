package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.dto.TaRoleDTO;
import fr.legrain.droits.model.TaRole;
import fr.legrain.droits.model.TaRole;


public class TaRoleMapper implements ILgrMapper<TaRoleDTO, TaRole> {

	@Override
	public TaRole mapDtoToEntity(TaRoleDTO dto, TaRole entity) {
		if(entity==null)
			entity = new TaRole();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRoleDTO mapEntityToDto(TaRole entity, TaRoleDTO dto) {
		if(dto==null)
			dto = new TaRoleDTO();

		dto.setId(entity.getId());
		dto.setRole(entity.getRole());
		dto.setDescription(entity.getDescription());
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
