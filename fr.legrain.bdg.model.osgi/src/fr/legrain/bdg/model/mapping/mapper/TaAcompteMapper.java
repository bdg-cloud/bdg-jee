package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.model.TaAcompte;


public class TaAcompteMapper implements ILgrMapper<TaAcompteDTO, TaAcompte> {

	@Override
	public TaAcompte mapDtoToEntity(TaAcompteDTO dto, TaAcompte entity) {
		if(entity==null)
			entity = new TaAcompte();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAcompteDTO mapEntityToDto(TaAcompte entity, TaAcompteDTO dto) {
		if(dto==null)
			dto = new TaAcompteDTO();

		dto.setId(entity.getIdDocument());
	
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		
		
		if(entity.getTaTiers()!=null) {
			dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
			dto.setNomTiers(entity.getTaTiers().getNomTiers());
		}
		
		dto.setDateDocument(entity.getDateDocument());
		dto.setDateEchDocument(entity.getDateEchDocument());
		dto.setNetHtCalc(entity.getNetHtCalc());
		dto.setNetTtcCalc(entity.getNetTtcCalc());
		dto.setNetTvaCalc(entity.getNetTvaCalc());
		dto.setDateLivDocument(entity.getDateLivDocument());
		
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
