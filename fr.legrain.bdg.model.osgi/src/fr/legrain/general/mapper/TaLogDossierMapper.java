package fr.legrain.general.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.general.dto.TaLogDossierDTO;
import fr.legrain.general.model.TaLogDossier;
import fr.legrain.general.model.TaLogDossier;



public class TaLogDossierMapper implements ILgrMapper<TaLogDossierDTO, TaLogDossier> {

	@Override
	public TaLogDossier mapDtoToEntity(TaLogDossierDTO dto, TaLogDossier entity) {
		if(entity==null)
			entity = new TaLogDossier();
//
//		entity.setIdFichier(dto.getId()!=null?dto.getId():0);
//		entity.setCodeFichier(dto.getCodeFichier());
//		entity.setJournalFichier(dto.getJournalFichier());
//		entity.setLibelleEdition(dto.getLibelleEdition());	
//		entity.setLibelleFichier(dto.getLibelleFichier());	
//		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
		
	}

	@Override
	public TaLogDossierDTO mapEntityToDto(TaLogDossier entity, TaLogDossierDTO dto) {
		if(dto==null)
			dto = new TaLogDossierDTO();
//
		dto.setId(entity.getId());
		dto.setMessage(entity.getMessage());
		dto.setAppelDistant(entity.getAppelDistant());
		dto.setCodeEntite(entity.getCodeEntite());
		dto.setCorpsReponseApi(entity.getCorpsReponseApi());
		dto.setCorpsRequeteApi(entity.getCorpsRequeteApi());
		dto.setCronDossier(entity.getCronDossier());
		dto.setCronSysteme(entity.getCronSysteme());
		dto.setDossier(entity.getDossier());
		dto.setQuand(entity.getQuand());
		dto.setEntite(entity.getEntite());
		dto.setEtat(entity.getEtat());
		dto.setIdEntite(entity.getIdEntite());
		dto.setIpDistante(entity.getIpDistante());
		dto.setMethodeHttpApi(entity.getMethodeHttpApi());
		dto.setNiveauLog(entity.getNiveauLog());
		dto.setOrigine(entity.getOrigine());
		dto.setUuid(entity.getUuid());
		dto.setParametreEnteteApi(entity.getParametreEnteteApi());
		
		dto.setParametreRequeteApi(entity.getParametreRequeteApi());
		dto.setSource(entity.getSource());
		dto.setTrace(entity.getTrace());
		dto.setUrlApi(entity.getUrlApi());
		dto.setUtilisateur(entity.getUtilisateur());
		dto.setVersionApi(entity.getVersionApi());

		
		if(entity.getTaUtilisateur()!=null) {
			dto.setIdUtilisateurDossier(entity.getTaUtilisateur().getId());
			dto.setUsernameUtilisateurDossier(entity.getTaUtilisateur().getUsername());
		}
		
		if(entity.getTaUtilisateurWebService()!=null) {
			dto.setIdUtilisateurWebService(entity.getTaUtilisateurWebService().getId());
			dto.setLoginUtilisateurDossier(entity.getTaUtilisateurWebService().getLogin());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
		
	}

}
