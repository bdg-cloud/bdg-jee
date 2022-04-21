package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaInfosFacture;


public class TaInfosFactureMapper implements ILgrMapper<TaFactureDTO, TaInfosFacture> {

	@Override
	public TaInfosFacture mapDtoToEntity(TaFactureDTO dto, TaInfosFacture entity) {
		if(entity==null)
			entity = new TaInfosFacture();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaFactureDTO mapEntityToDto(TaInfosFacture entity, TaFactureDTO dto) {
		if(dto==null)
			dto = new TaFactureDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
