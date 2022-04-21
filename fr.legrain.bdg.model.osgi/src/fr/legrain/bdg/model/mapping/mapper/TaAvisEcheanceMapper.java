package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvisEcheance;


public class TaAvisEcheanceMapper implements ILgrMapper<TaAvisEcheanceDTO, TaAvisEcheance> {

	@Override
	public TaAvisEcheance mapDtoToEntity(TaAvisEcheanceDTO dto, TaAvisEcheance entity) {
		if(entity==null)
			entity = new TaAvisEcheance();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAvisEcheanceDTO mapEntityToDto(TaAvisEcheance entity, TaAvisEcheanceDTO dto) {
		if(dto==null)
			dto = new TaAvisEcheanceDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		dto.setNetTtcCalc(entity.getNetTtcCalc());
		dto.setNetHtCalc(entity.getNetHtCalc());
		dto.setNetTvaCalc(entity.getNetTvaCalc());
		//dto.setResteAReglerComplet(entity.getResteAReglerComplet());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
