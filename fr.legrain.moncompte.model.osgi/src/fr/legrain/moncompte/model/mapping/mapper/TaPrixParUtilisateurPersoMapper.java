package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaPrixParUtilisateurPersoDTO;
import fr.legrain.moncompte.dto.TaPrixParUtilisateurPersoDTO;
import fr.legrain.moncompte.model.TaPrixParUtilisateurPerso;
import fr.legrain.moncompte.model.TaPrixParUtilisateurPerso;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaPrixParUtilisateurPersoMapper implements ILgrMapper<TaPrixParUtilisateurPersoDTO, TaPrixParUtilisateurPerso> {

	@Override
	public TaPrixParUtilisateurPerso mapDtoToEntity(TaPrixParUtilisateurPersoDTO dto, TaPrixParUtilisateurPerso entity) {
		if(entity==null)
			entity = new TaPrixParUtilisateurPerso();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setPrixHT(dto.getPrixHT());
		entity.setPrixTTC(dto.getPrixTTC());
		entity.setTauxTVA(dto.getTauxTVA());
		entity.setNbUtilisateur(dto.getNbUtilisateur());
		
		
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setEmail(dto.getEmail());
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setUsername(dto.getUsername());
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setPasswd(dto.getPasswd());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPrixParUtilisateurPersoDTO mapEntityToDto(TaPrixParUtilisateurPerso entity, TaPrixParUtilisateurPersoDTO dto) {
		if(dto==null)
			dto = new TaPrixParUtilisateurPersoDTO();

		dto.setId(entity.getId());
		
		dto.setPrixHT(entity.getPrixHT());
		dto.setPrixTTC(entity.getPrixTTC());
		dto.setTauxTVA(entity.getTauxTVA());
		dto.setNbUtilisateur(entity.getNbUtilisateur());
		
//		if(entity.getTaUtilisateur()!=null) dto.setUsername(entity.getTaUtilisateur().getUsername());
//		if(entity.getTaUtilisateur()!=null) dto.setEmail(entity.getTaUtilisateur().getEmail());
//		if(entity.getTaUtilisateur()!=null) dto.setPasswd(entity.getTaUtilisateur().getPasswd());
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
