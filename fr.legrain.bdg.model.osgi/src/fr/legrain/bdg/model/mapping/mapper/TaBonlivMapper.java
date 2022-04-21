package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaBonliv;


public class TaBonlivMapper implements ILgrMapper<TaBonlivDTO, TaBonliv> {

	@Override
	public TaBonliv mapDtoToEntity(TaBonlivDTO dto, TaBonliv entity) {
		if(entity==null)
			entity = new TaBonliv();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		if(entity.getTaTransporteur()!=null) entity.getTaTransporteur().setCodeTransporteur(dto.getCodeTransporteur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBonlivDTO mapEntityToDto(TaBonliv entity, TaBonlivDTO dto) {
		if(dto==null)
			dto = new TaBonlivDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		if(entity.getTaTransporteur()!=null) dto.setCodeTransporteur(entity.getTaTransporteur().getCodeTransporteur());
		
		dto.setMtHtCalc(entity.getMtHtCalc());
		dto.setMtTtcCalc(entity.getMtTtcCalc());
		dto.setMtTvaCalc(entity.getMtTvaCalc());
		dto.setNetHtCalc(entity.getNetHtCalc());
		dto.setNetTtcCalc(entity.getNetTtcCalc());
		dto.setNetTvaCalc(entity.getNetTvaCalc());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
