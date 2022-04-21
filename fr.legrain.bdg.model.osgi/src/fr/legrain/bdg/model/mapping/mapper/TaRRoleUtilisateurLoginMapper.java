package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.login.dto.TaRRoleUtilisateurLoginDTO;
import fr.legrain.login.model.TaRRoleUtilisateurLogin;



public class TaRRoleUtilisateurLoginMapper implements ILgrMapper<TaRRoleUtilisateurLoginDTO, TaRRoleUtilisateurLogin> {

	@Override
	public TaRRoleUtilisateurLogin mapDtoToEntity(TaRRoleUtilisateurLoginDTO dto, TaRRoleUtilisateurLogin entity) {
		if(entity==null)
			entity = new TaRRoleUtilisateurLogin();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRRoleUtilisateurLoginDTO mapEntityToDto(TaRRoleUtilisateurLogin entity, TaRRoleUtilisateurLoginDTO dto) {
		if(dto==null)
			dto = new TaRRoleUtilisateurLoginDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
