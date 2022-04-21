package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.model.TaInfosBoncde;


public class TaInfosBoncdeMapper implements ILgrMapper<TaBoncdeDTO, TaInfosBoncde> {

	@Override
	public TaInfosBoncde mapDtoToEntity(TaBoncdeDTO dto, TaInfosBoncde entity) {
		if(entity==null)
			entity = new TaInfosBoncde();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBoncdeDTO mapEntityToDto(TaInfosBoncde entity, TaBoncdeDTO dto) {
		if(dto==null)
			dto = new TaBoncdeDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
