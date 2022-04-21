package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaInfosProforma;


public class TaInfosProformaMapper implements ILgrMapper<TaProformaDTO, TaInfosProforma> {

	@Override
	public TaInfosProforma mapDtoToEntity(TaProformaDTO dto, TaInfosProforma entity) {
		if(entity==null)
			entity = new TaInfosProforma();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaProformaDTO mapEntityToDto(TaInfosProforma entity, TaProformaDTO dto) {
		if(dto==null)
			dto = new TaProformaDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
