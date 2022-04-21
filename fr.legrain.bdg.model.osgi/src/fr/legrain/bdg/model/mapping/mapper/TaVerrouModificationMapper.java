package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.controle.dto.TaVerrouModificationDTO;
import fr.legrain.controle.model.TaVerrouModification;


public class TaVerrouModificationMapper implements ILgrMapper<TaVerrouModificationDTO, TaVerrouModification> {

	@Override
	public TaVerrouModification mapDtoToEntity(TaVerrouModificationDTO dto, TaVerrouModification entity) {
		if(entity==null)
			entity = new TaVerrouModification();

		entity.setIdVerrouModification(dto.getId()!=null?dto.getId():0);
		entity.setEntite(dto.getEntite());
		entity.setChamp(dto.getChamp());
		entity.setValeur(dto.getValeur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaVerrouModificationDTO mapEntityToDto(TaVerrouModification entity, TaVerrouModificationDTO dto) {
		if(dto==null)
			dto = new TaVerrouModificationDTO();

		dto.setId(entity.getIdVerrouModification());
		dto.setEntite(entity.getEntite());
		dto.setChamp(entity.getChamp());
		dto.setValeur(entity.getValeur());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
