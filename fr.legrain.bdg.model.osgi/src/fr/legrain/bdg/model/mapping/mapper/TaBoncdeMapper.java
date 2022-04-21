package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncde;


public class TaBoncdeMapper implements ILgrMapper<TaBoncdeDTO, TaBoncde> {

	@Override
	public TaBoncde mapDtoToEntity(TaBoncdeDTO dto, TaBoncde entity) {
		if(entity==null)
			entity = new TaBoncde();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		if(entity.getTaTransporteur()!=null) entity.getTaTransporteur().setCodeTransporteur(dto.getCodeTransporteur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBoncdeDTO mapEntityToDto(TaBoncde entity, TaBoncdeDTO dto) {
		if(dto==null)
			dto = new TaBoncdeDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		if (entity.getTaTiers()!= null) {
		dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		}
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		if(entity.getTaTransporteur()!=null) dto.setCodeTransporteur(entity.getTaTransporteur().getCodeTransporteur());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
