package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLProformaDTO;
import fr.legrain.document.model.TaLProforma;


public class TaLProformaMapper implements ILgrMapper<TaLProformaDTO, TaLProforma> {

	@Override
	public TaLProforma mapDtoToEntity(TaLProformaDTO dto, TaLProforma entity) {
		if(entity==null)
			entity = new TaLProforma();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLProformaDTO mapEntityToDto(TaLProforma entity, TaLProformaDTO dto) {
		if(dto==null)
			dto = new TaLProformaDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
