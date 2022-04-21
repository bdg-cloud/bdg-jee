package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.model.TaInfosApporteur;


public class TaInfosApporteurMapper implements ILgrMapper<TaApporteurDTO, TaInfosApporteur> {

	@Override
	public TaInfosApporteur mapDtoToEntity(TaApporteurDTO dto, TaInfosApporteur entity) {
		if(entity==null)
			entity = new TaInfosApporteur();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaApporteurDTO mapEntityToDto(TaInfosApporteur entity, TaApporteurDTO dto) {
		if(dto==null)
			dto = new TaApporteurDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
