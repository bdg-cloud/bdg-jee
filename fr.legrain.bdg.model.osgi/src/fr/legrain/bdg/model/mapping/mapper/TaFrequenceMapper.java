package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaFrequenceMapper implements ILgrMapper<TaFrequenceDTO, TaFrequence> {

	@Override
	public TaFrequence mapDtoToEntity(TaFrequenceDTO dto, TaFrequence entity) {
		if(entity==null)
			entity = new TaFrequence();

		entity.setIdFrequence(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeFrequence(dto.getCodeFrequence());
		entity.setLiblFrequence(dto.getLiblFrequence());
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFrequenceDTO mapEntityToDto(TaFrequence entity, TaFrequenceDTO dto) {
		if(dto==null)
			dto = new TaFrequenceDTO();

		dto.setId(entity.getIdFrequence());
		
		dto.setCodeFrequence(entity.getCodeFrequence());
		dto.setLiblFrequence(entity.getLiblFrequence());
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
