package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaPreparationDTO;
import fr.legrain.document.model.TaPreparation;


public class TaPreparationMapper implements ILgrMapper<TaPreparationDTO, TaPreparation> {

	@Override
	public TaPreparation mapDtoToEntity(TaPreparationDTO dto, TaPreparation entity) {
		if(entity==null)
			entity = new TaPreparation();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		if(entity.getTaTransporteur()!=null) entity.getTaTransporteur().setCodeTransporteur(dto.getCodeTransporteur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPreparationDTO mapEntityToDto(TaPreparation entity, TaPreparationDTO dto) {
		if(dto==null)
			dto = new TaPreparationDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		if(entity.getTaTransporteur()!=null) dto.setCodeTransporteur(entity.getTaTransporteur().getCodeTransporteur());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
