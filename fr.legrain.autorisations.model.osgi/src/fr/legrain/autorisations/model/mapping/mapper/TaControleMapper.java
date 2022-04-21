package fr.legrain.autorisations.model.mapping.mapper;

import fr.legrain.autorisations.controle.dto.TaControleDTO;
import fr.legrain.autorisations.controle.model.TaControle;
import fr.legrain.autorisations.model.mapping.ILgrMapper;


public class TaControleMapper implements ILgrMapper<TaControleDTO, TaControle> {

	@Override
	public TaControle mapDtoToEntity(TaControleDTO dto, TaControle entity) {
		if(entity==null)
			entity = new TaControle();

		entity.setIdControle(dto.getId()!=null?dto.getId():0);
		
	
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaControleDTO mapEntityToDto(TaControle entity, TaControleDTO dto) {
		if(dto==null)
			dto = new TaControleDTO();

		dto.setId(entity.getIdControle());
		
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
