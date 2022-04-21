package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaApporteur;


public class TaApporteurMapper implements ILgrMapper<TaApporteurDTO, TaApporteur> {

	@Override
	public TaApporteur mapDtoToEntity(TaApporteurDTO dto, TaApporteur entity) {
		if(entity==null)
			entity = new TaApporteur();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaApporteurDTO mapEntityToDto(TaApporteur entity, TaApporteurDTO dto) {
		if(dto==null)
			dto = new TaApporteurDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
