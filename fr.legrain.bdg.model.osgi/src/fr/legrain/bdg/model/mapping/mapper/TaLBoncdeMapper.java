package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLBoncdeDTO;
import fr.legrain.document.model.TaLBoncde;


public class TaLBoncdeMapper implements ILgrMapper<TaLBoncdeDTO, TaLBoncde> {

	@Override
	public TaLBoncde mapDtoToEntity(TaLBoncdeDTO dto, TaLBoncde entity) {
		if(entity==null)
			entity = new TaLBoncde();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLBoncdeDTO mapEntityToDto(TaLBoncde entity, TaLBoncdeDTO dto) {
		if(dto==null)
			dto = new TaLBoncdeDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
