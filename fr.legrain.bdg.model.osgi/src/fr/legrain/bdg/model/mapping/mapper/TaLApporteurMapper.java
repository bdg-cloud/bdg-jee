package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLApporteurDTO;
import fr.legrain.document.dto.TaLApporteurDTO;
import fr.legrain.document.dto.TaLApporteurDTO;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLApporteur;


public class TaLApporteurMapper implements ILgrMapper<TaLApporteurDTO, TaLApporteur> {

	@Override
	public TaLApporteur mapDtoToEntity(TaLApporteurDTO dto, TaLApporteur entity) {
		if(entity==null)
			entity = new TaLApporteur();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLApporteurDTO mapEntityToDto(TaLApporteur entity, TaLApporteurDTO dto) {
		if(dto==null)
			dto = new TaLApporteurDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
