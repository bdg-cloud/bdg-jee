package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.DocumentEditableDTO;
import fr.legrain.document.model.TaDocumentEditable;
import fr.legrain.document.model.TaDocumentEditable;


public class TaDocumentEditableMapper implements ILgrMapper<DocumentEditableDTO, TaDocumentEditable> {

	@Override
	public TaDocumentEditable mapDtoToEntity(DocumentEditableDTO dto, TaDocumentEditable entity) {
		if(entity==null)
			entity = new TaDocumentEditable();

		entity.setIdDocumentDoc(dto.getId()!=null?dto.getId():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public DocumentEditableDTO mapEntityToDto(TaDocumentEditable entity, DocumentEditableDTO dto) {
		if(dto==null)
			dto = new DocumentEditableDTO();

		dto.setIdDocumentDoc(entity.getIdDocumentDoc());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
