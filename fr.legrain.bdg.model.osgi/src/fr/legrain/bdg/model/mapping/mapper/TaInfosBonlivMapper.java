package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.model.TaInfosBonliv;


public class TaInfosBonlivMapper implements ILgrMapper<TaBonlivDTO, TaInfosBonliv> {

	@Override
	public TaInfosBonliv mapDtoToEntity(TaBonlivDTO dto, TaInfosBonliv entity) {
		if(entity==null)
			entity = new TaInfosBonliv();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBonlivDTO mapEntityToDto(TaInfosBonliv entity, TaBonlivDTO dto) {
		if(dto==null)
			dto = new TaBonlivDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
