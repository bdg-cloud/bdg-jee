package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.bdg.model.mapping.ILgrMapper;



public class TaTransporteurMapper implements ILgrMapper<TaTransporteurDTO, TaTransporteur> {

	@Override
	public TaTransporteur mapDtoToEntity(TaTransporteurDTO dto, TaTransporteur entity) {
		if(entity==null)
			entity = new TaTransporteur();

		entity.setIdTransporteur(dto.getId()!=null?dto.getId():0);
		entity.setCodeTransporteur(dto.getCodeTransporteur());
		entity.setLiblTransporteur(dto.getLiblTransporteur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTransporteurDTO mapEntityToDto(TaTransporteur entity, TaTransporteurDTO dto) {
		if(dto==null)
			dto = new TaTransporteurDTO();

		dto.setId(entity.getIdTransporteur());
		dto.setCodeTransporteur(entity.getCodeTransporteur());
		dto.setLiblTransporteur(entity.getLiblTransporteur());	
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
