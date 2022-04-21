package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;


public class TaBonReceptionMapper implements ILgrMapper<TaBonReceptionDTO, TaBonReception> {

	@Override
	public TaBonReception mapDtoToEntity(TaBonReceptionDTO dto, TaBonReception entity) {
		if(entity==null)
			entity = new TaBonReception();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		entity.setDateDocument(dto.getDateDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBonReceptionDTO mapEntityToDto(TaBonReception entity, TaBonReceptionDTO dto) {
		if(dto==null)
			dto = new TaBonReceptionDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
