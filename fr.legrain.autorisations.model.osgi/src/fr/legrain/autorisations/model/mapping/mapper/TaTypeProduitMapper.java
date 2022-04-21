package fr.legrain.autorisations.model.mapping.mapper;

import fr.legrain.autorisations.autorisation.dto.TaAutorisationsDTO;
import fr.legrain.autorisations.autorisation.dto.TaTypeProduitDTO;
import fr.legrain.autorisations.autorisation.model.TaAutorisations;
import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.autorisations.model.mapping.ILgrMapper;


public class TaTypeProduitMapper implements ILgrMapper<TaTypeProduitDTO, TaTypeProduit> {

	@Override
	public TaTypeProduit mapDtoToEntity(TaTypeProduitDTO dto, TaTypeProduit entity) {
		if(entity==null)
			entity = new TaTypeProduit();

		entity.setIdType(dto.getId()!=null?dto.getId():0);
		
		entity.setCode(dto.getCode());
		entity.setLibelle(dto.getLibelle());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypeProduitDTO mapEntityToDto(TaTypeProduit entity, TaTypeProduitDTO dto) {
		if(dto==null)
			dto = new TaTypeProduitDTO();

		dto.setId(entity.getIdType());
		
		dto.setCode(entity.getCode());
		dto.setLibelle(entity.getLibelle());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
