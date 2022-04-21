package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.model.TaInfosBonReception;
import fr.legrain.document.model.TaInfosBonReception;


public class TaInfosBonReceptionMapper implements ILgrMapper<TaBonReceptionDTO, TaInfosBonReception> {

	@Override
	public TaInfosBonReception mapDtoToEntity(TaBonReceptionDTO dto, TaInfosBonReception entity) {
		if(entity==null)
			entity = new TaInfosBonReception();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBonReceptionDTO mapEntityToDto(TaInfosBonReception entity, TaBonReceptionDTO dto) {
		if(dto==null)
			dto = new TaBonReceptionDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
