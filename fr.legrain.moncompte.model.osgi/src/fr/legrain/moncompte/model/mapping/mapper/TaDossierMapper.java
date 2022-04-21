package fr.legrain.moncompte.model.mapping.mapper;

import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.mapping.ILgrMapper;


public class TaDossierMapper implements ILgrMapper<TaDossierDTO, TaDossier> {

	@Override
	public TaDossier mapDtoToEntity(TaDossierDTO dto, TaDossier entity) {
		if(entity==null)
			entity = new TaDossier();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
		entity.setCode(dto.getCode());
		entity.setDescription(dto.getDescription());
		entity.setActif(dto.getActif());
		
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setEmail(dto.getEmail());
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setUsername(dto.getUsername());
//		if(entity.getTaUtilisateur()!=null) entity.getTaUtilisateur().setPasswd(dto.getPasswd());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaDossierDTO mapEntityToDto(TaDossier entity, TaDossierDTO dto) {
		if(dto==null)
			dto = new TaDossierDTO();

		dto.setId(entity.getId());
		
		dto.setCode(entity.getCode());
		dto.setDescription(entity.getDescription());
		dto.setActif(entity.getActif());;
		
//		if(entity.getTaUtilisateur()!=null) dto.setUsername(entity.getTaUtilisateur().getUsername());
//		if(entity.getTaUtilisateur()!=null) dto.setEmail(entity.getTaUtilisateur().getEmail());
//		if(entity.getTaUtilisateur()!=null) dto.setPasswd(entity.getTaUtilisateur().getPasswd());
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
