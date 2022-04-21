package fr.legrain.bdg.compteclient.model.mapping.mapper;

import fr.legrain.bdg.compteclient.dto.droits.TaRRoleUtilisateurDTO;
import fr.legrain.bdg.compteclient.model.droits.TaRRoleUtilisateur;
import fr.legrain.bdg.compteclient.model.mapping.ILgrMapper;


public class TaRRoleUtilisateurMapper implements ILgrMapper<TaRRoleUtilisateurDTO, TaRRoleUtilisateur> {

	@Override
	public TaRRoleUtilisateur mapDtoToEntity(TaRRoleUtilisateurDTO dto, TaRRoleUtilisateur entity) {
		if(entity==null)
			entity = new TaRRoleUtilisateur();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRRoleUtilisateurDTO mapEntityToDto(TaRRoleUtilisateur entity, TaRRoleUtilisateurDTO dto) {
		if(dto==null)
			dto = new TaRRoleUtilisateurDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
