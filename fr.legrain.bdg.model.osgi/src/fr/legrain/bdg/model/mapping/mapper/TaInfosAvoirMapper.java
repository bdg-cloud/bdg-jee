package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.document.model.TaInfosAvoir;


public class TaInfosAvoirMapper implements ILgrMapper<TaAvoirDTO, TaInfosAvoir> {

	@Override
	public TaInfosAvoir mapDtoToEntity(TaAvoirDTO dto, TaInfosAvoir entity) {
		if(entity==null)
			entity = new TaInfosAvoir();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAvoirDTO mapEntityToDto(TaInfosAvoir entity, TaAvoirDTO dto) {
		if(dto==null)
			dto = new TaAvoirDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
