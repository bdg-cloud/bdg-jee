package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.login.dto.TaUtilisateurLoginDTO;
import fr.legrain.login.model.TaUtilisateurLogin;


public class TaUtilisateurLoginMapper implements ILgrMapper<TaUtilisateurLoginDTO, TaUtilisateurLogin> {
	
	public TaUtilisateurLogin mapTaUtilisateurToTaUtilisateurLogin(TaUtilisateur taUtilisateur, TaUtilisateurLogin taUtilisateurLogin) {
		if(taUtilisateurLogin==null)
			taUtilisateurLogin = new TaUtilisateurLogin();

		taUtilisateurLogin.setPasswd(taUtilisateur.getPasswd());
		taUtilisateurLogin.setUsername(taUtilisateur.getUsername());
		
		//taUtilisateurLogin.setVersionObj(taUtilisateur.getVersionObj());

		return taUtilisateurLogin;
	}

	@Override
	public TaUtilisateurLogin mapDtoToEntity(TaUtilisateurLoginDTO dto, TaUtilisateurLogin entity) {
		if(entity==null)
			entity = new TaUtilisateurLogin();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaUtilisateurLoginDTO mapEntityToDto(TaUtilisateurLogin entity, TaUtilisateurLoginDTO dto) {
		if(dto==null)
			dto = new TaUtilisateurLoginDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
