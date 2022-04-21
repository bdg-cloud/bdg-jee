package fr.legrain.autorisations.model.mapping.mapper;

import fr.legrain.autorisations.autorisation.dto.TaUtilisateurDTO;
import fr.legrain.autorisations.autorisation.model.TaUtilisateur;
import fr.legrain.autorisations.model.mapping.ILgrMapper;


public class TaUtilisateurMapper implements ILgrMapper<TaUtilisateurDTO, TaUtilisateur> {

	@Override
	public TaUtilisateur mapDtoToEntity(TaUtilisateurDTO dto, TaUtilisateur entity) {
		if(entity==null)
			entity = new TaUtilisateur();

		entity.setId(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaUtilisateurDTO mapEntityToDto(TaUtilisateur entity, TaUtilisateurDTO dto) {
		if(dto==null)
			dto = new TaUtilisateurDTO();

		dto.setId(entity.getId());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
