package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaFacture;


public class TaFactureMapper implements ILgrMapper<TaFactureDTO, TaFacture> {

	@Override
	public TaFacture mapDtoToEntity(TaFactureDTO dto, TaFacture entity) {
		if(entity==null)
			entity = new TaFacture();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFactureDTO mapEntityToDto(TaFacture entity, TaFactureDTO dto) {
		if(dto==null)
			dto = new TaFactureDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		dto.setNetTtcCalc(entity.getNetTtcCalc());
		dto.setNetHtCalc(entity.getNetHtCalc());
		dto.setNetTvaCalc(entity.getNetTvaCalc());
		dto.setResteAReglerComplet(entity.getResteAReglerComplet());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());
		return dto;
	}

}
