package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.dto.TaUtilisateurWebServiceDTO;
import fr.legrain.droits.model.TaUtilisateurWebService;


public class TaUtilisateurWebServiceMapper implements ILgrMapper<TaUtilisateurWebServiceDTO, TaUtilisateurWebService> {

	@Override
	public TaUtilisateurWebService mapDtoToEntity(TaUtilisateurWebServiceDTO dto, TaUtilisateurWebService entity) {
		if(entity==null)
			entity = new TaUtilisateurWebService();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setUsername(dto.getUsername());
//		entity.setActif(dto.getActif());
//		entity.setAdminDossier(dto.getAdminDossier());
//		entity.setAutorisations(dto.getAutorisations());
//		entity.setDernierAcces(dto.getDernierAcces());
//		entity.setEmail(dto.getEmail());
//		entity.setNom(dto.getNom());
//		entity.setPasswd(dto.getPasswd());
//		entity.setPrenom(dto.getPrenom());
//		entity.setSysteme(dto.getSysteme());
		
//		entity.setTaEntreprise(dto.getCodeDocument());
//		entity.setRoles(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaUtilisateurWebServiceDTO mapEntityToDto(TaUtilisateurWebService entity, TaUtilisateurWebServiceDTO dto) {
		if(dto==null)
			dto = new TaUtilisateurWebServiceDTO();

		dto.setId(entity.getId());
		
		dto.setLogin(entity.getLogin());
		dto.setActif(entity.getActif());
//		dto.setAdminDossier(entity.getAdminDossier());
		dto.setAutorisations(entity.getAutorisations());
		dto.setDernierAcces(entity.getDernierAcces());
		dto.setEmail(entity.getEmail());
//		dto.setNom(entity.getNom());
		dto.setPasswd(entity.getPasswd());
//		dto.setPrenom(entity.getPrenom());
		dto.setSysteme(entity.getSysteme());
//		dto.setDernierAccesMobile(entity.getDernierAccesMobile());
//		dto.setAndroidRegistrationToken(entity.getAndroidRegistrationToken());
		
//		entity.setTaEntreprise(dto.getCodeDocument());
//		entity.setRoles(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
