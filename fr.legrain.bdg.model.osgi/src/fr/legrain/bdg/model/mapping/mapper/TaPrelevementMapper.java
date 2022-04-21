package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPrelevement;


public class TaPrelevementMapper implements ILgrMapper<TaPrelevementDTO, TaPrelevement> {

	@Override
	public TaPrelevement mapDtoToEntity(TaPrelevementDTO dto, TaPrelevement entity) {
		if(entity==null)
			entity = new TaPrelevement();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPrelevementDTO mapEntityToDto(TaPrelevement entity, TaPrelevementDTO dto) {
		if(dto==null)
			dto = new TaPrelevementDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
