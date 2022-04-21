package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.TaPanier;


public class TaPanierMapper implements ILgrMapper<TaPanierDTO, TaPanier> {

	@Override
	public TaPanier mapDtoToEntity(TaPanierDTO dto, TaPanier entity) {
		if(entity==null)
			entity = new TaPanier();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
//		if(entity.getTaTransporteur()!=null) entity.getTaTransporteur().setCodeTransporteur(dto.getCodeTransporteur());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPanierDTO mapEntityToDto(TaPanier entity, TaPanierDTO dto) {
		if(dto==null)
			dto = new TaPanierDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
//		if(entity.getTaTransporteur()!=null) dto.setCodeTransporteur(entity.getTaTransporteur().getCodeTransporteur());
		
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
