package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.model.TaActionEdition;



public class TaActionEditionMapper implements ILgrMapper<TaActionEditionDTO, TaActionEdition> {

	@Override
	public TaActionEdition mapDtoToEntity(TaActionEditionDTO dto, TaActionEdition entity) {
		if(entity==null)
			entity = new TaActionEdition();

		entity.setIdActionEdition(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelle(dto.getLibelle());
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaActionEditionDTO mapEntityToDto(TaActionEdition entity, TaActionEditionDTO dto) {
		if(dto==null)
			dto = new TaActionEditionDTO();

		dto.setId(entity.getIdActionEdition());
		
		dto.setLibelle(entity.getLibelle());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
