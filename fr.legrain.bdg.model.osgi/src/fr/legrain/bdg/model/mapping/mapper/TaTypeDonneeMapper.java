package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.dossierIntelligent.dto.TaTypeDonneeDTO;
import fr.legrain.dossierIntelligent.model.TaTypeDonnee;



public class TaTypeDonneeMapper implements ILgrMapper<TaTypeDonneeDTO, TaTypeDonnee> {

	@Override
	public TaTypeDonnee mapDtoToEntity(TaTypeDonneeDTO dto, TaTypeDonnee entity) {
		if(entity==null)
			entity = new TaTypeDonnee();

		entity.setId(dto.getId());
		entity.setTypeDonnee(dto.getTypeDonnee());		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypeDonneeDTO mapEntityToDto(TaTypeDonnee entity, TaTypeDonneeDTO dto) {
		if(dto==null)
			dto = new TaTypeDonneeDTO();

		dto.setId(entity.getId());
		dto.setTypeDonnee(entity.getTypeDonnee());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
