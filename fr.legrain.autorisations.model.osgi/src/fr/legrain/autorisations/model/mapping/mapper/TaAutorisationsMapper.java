package fr.legrain.autorisations.model.mapping.mapper;

import fr.legrain.autorisations.autorisation.dto.TaAutorisationsDTO;
import fr.legrain.autorisations.autorisation.model.TaAutorisations;
import fr.legrain.autorisations.model.mapping.ILgrMapper;


public class TaAutorisationsMapper implements ILgrMapper<TaAutorisationsDTO, TaAutorisations> {

	@Override
	public TaAutorisations mapDtoToEntity(TaAutorisationsDTO dto, TaAutorisations entity) {
		if(entity==null)
			entity = new TaAutorisations();

		entity.setIdAutorisation(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeDossier(dto.getCodeDossier());
		entity.setModules(dto.getModules());
		entity.setValide(dto.getValide());
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAutorisationsDTO mapEntityToDto(TaAutorisations entity, TaAutorisationsDTO dto) {
		if(dto==null)
			dto = new TaAutorisationsDTO();

		dto.setId(entity.getIdAutorisation());
		
		dto.setCodeDossier(entity.getCodeDossier());
		dto.setModules(entity.getModules());
		dto.setValide(entity.getValide());
		
		if(entity.getTaTypeProduit()!=null){
			dto.setCodeType(entity.getTaTypeProduit().getCode());
			dto.setIdType(entity.getTaTypeProduit().getIdType());
		}
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
