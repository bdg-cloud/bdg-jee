package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLAvoirDTO;
import fr.legrain.document.model.TaLAvoir;


public class TaLAvoirMapper implements ILgrMapper<TaLAvoirDTO, TaLAvoir> {

	@Override
	public TaLAvoir mapDtoToEntity(TaLAvoirDTO dto, TaLAvoir entity) {
		if(entity==null)
			entity = new TaLAvoir();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLAvoirDTO mapEntityToDto(TaLAvoir entity, TaLAvoirDTO dto) {
		if(dto==null)
			dto = new TaLAvoirDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
