package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.lib.data.LibConversion;
import fr.legrain.moncompte.dto.TaUtilisateurDTO;
import fr.legrain.moncompte.model.TaUtilisateur;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaUtilisateurMapper implements ILgrMapper<TaUtilisateurDTO, TaUtilisateur> {

	@Override
	public TaUtilisateur mapDtoToEntity(TaUtilisateurDTO dto, TaUtilisateur entity) {
		if(entity==null)
			entity = new TaUtilisateur();

		entity.setId(dto.getId()!=null?dto.getId():0);
		

		entity.setEmail(dto.getEmail());
		entity.setPasswd(dto.getPasswd());
		entity.setUsername(dto.getUsername());
		if(dto.getActif()!=null)entity.setActif(LibConversion.booleanToInt(dto.getActif()));
		entity.setDernierAcces(dto.getDernierAcces());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaUtilisateurDTO mapEntityToDto(TaUtilisateur entity, TaUtilisateurDTO dto) {
		if(dto==null)
			dto = new TaUtilisateurDTO();

		dto.setId(entity.getId());
		
		dto.setEmail(entity.getEmail());
		dto.setPasswd(entity.getPasswd());
		dto.setUsername(entity.getUsername());
		if(entity.getActif()!=null)dto.setActif(LibConversion.intToBoolean(entity.getActif()));
		dto.setDernierAcces(entity.getDernierAcces());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
