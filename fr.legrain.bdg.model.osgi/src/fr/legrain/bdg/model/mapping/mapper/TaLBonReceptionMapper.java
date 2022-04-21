package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLBonReception;


public class TaLBonReceptionMapper implements ILgrMapper<TaLBonReceptionDTO, TaLBonReception> {

	@Override
	public TaLBonReception mapDtoToEntity(TaLBonReceptionDTO dto, TaLBonReception entity) {
		if(entity==null)
			entity = new TaLBonReception();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLBonReceptionDTO mapEntityToDto(TaLBonReception entity, TaLBonReceptionDTO dto) {
		if(dto==null)
			dto = new TaLBonReceptionDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
