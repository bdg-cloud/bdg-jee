package fr.legrain.autorisations.model.mapping.mapper;

import fr.legrain.autorisations.autorisation.dto.TaRoleDTO;
import fr.legrain.autorisations.autorisation.model.TaRole;
import fr.legrain.autorisations.model.mapping.ILgrMapper;


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
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
