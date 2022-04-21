package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.abonnement.dto.TaJourRelanceDTO;
import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.bdg.model.mapping.ILgrMapper;


public class TaJourRelanceMapper implements ILgrMapper<TaJourRelanceDTO, TaJourRelance> {

	@Override
	public TaJourRelance mapDtoToEntity(TaJourRelanceDTO dto, TaJourRelance entity) {
		if(entity==null)
			entity = new TaJourRelance();

		entity.setIdJourRelance(dto.getIdJourRelance()!=null?dto.getIdJourRelance():0);
		
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaJourRelanceDTO mapEntityToDto(TaJourRelance entity, TaJourRelanceDTO dto) {
		if(dto==null)
			dto = new TaJourRelanceDTO();

		dto.setIdJourRelance(entity.getIdJourRelance());
		
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
