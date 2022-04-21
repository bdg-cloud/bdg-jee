package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.model.TaUtilisateur;


public class TaUtilisateurMapper implements ILgrMapper<TaUtilisateurDTO, TaUtilisateur> {

	@Override
	public TaUtilisateur mapDtoToEntity(TaUtilisateurDTO dto, TaUtilisateur entity) {
		if(entity==null)
			entity = new TaUtilisateur();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setUsername(dto.getUsername());
		entity.setActif(dto.getActif());
		entity.setAdminDossier(dto.getAdminDossier());
		entity.setAutorisations(dto.getAutorisations());
		entity.setDernierAcces(dto.getDernierAcces());
		entity.setEmail(dto.getEmail());
		entity.setNom(dto.getNom());
		entity.setPasswd(dto.getPasswd());
		entity.setPrenom(dto.getPrenom());
		entity.setSysteme(dto.getSysteme());
		
//		entity.setTaEntreprise(dto.getCodeDocument());
//		entity.setRoles(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaUtilisateurDTO mapEntityToDto(TaUtilisateur entity, TaUtilisateurDTO dto) {
		if(dto==null)
			dto = new TaUtilisateurDTO();

		dto.setId(entity.getId());
		
		dto.setUsername(entity.getUsername());
		dto.setActif(entity.getActif());
		dto.setAdminDossier(entity.getAdminDossier());
		dto.setAutorisations(entity.getAutorisations());
		dto.setDernierAcces(entity.getDernierAcces());
		dto.setEmail(entity.getEmail());
		dto.setNom(entity.getNom());
		dto.setPasswd(entity.getPasswd());
		dto.setPrenom(entity.getPrenom());
		dto.setSysteme(entity.getSysteme());
		dto.setDernierAccesMobile(entity.getDernierAccesMobile());
		dto.setAndroidRegistrationToken(entity.getAndroidRegistrationToken());
		
//		entity.setTaEntreprise(dto.getCodeDocument());
//		entity.setRoles(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
