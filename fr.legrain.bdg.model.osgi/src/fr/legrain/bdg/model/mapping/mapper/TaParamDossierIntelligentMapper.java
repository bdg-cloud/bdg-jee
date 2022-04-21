package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.dossierIntelligent.dto.TaParamDossierIntelligentDTO;
import fr.legrain.dossierIntelligent.model.TaParamDossierIntel;


public class TaParamDossierIntelligentMapper implements ILgrMapper<TaParamDossierIntelligentDTO, TaParamDossierIntel> {

	@Override
	public TaParamDossierIntel mapDtoToEntity(TaParamDossierIntelligentDTO dto, TaParamDossierIntel entity) {
		if(entity==null)
			entity = new TaParamDossierIntel();

		entity.setId(dto.getId());
		entity.setMot(dto.getMot());
		entity.setNbZones(dto.getNbZones());
		entity.setSql(dto.getSql());
		
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaParamDossierIntelligentDTO mapEntityToDto(TaParamDossierIntel entity, TaParamDossierIntelligentDTO dto) {
		if(dto==null)
			dto = new TaParamDossierIntelligentDTO();

		dto.setId(entity.getId());
		dto.setMot(entity.getMot());
		dto.setNbZones(entity.getNbZones());
		dto.setSql(entity.getSql());
		

		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
