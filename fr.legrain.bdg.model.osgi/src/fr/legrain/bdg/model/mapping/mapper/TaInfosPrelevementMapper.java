package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.model.TaInfosPrelevement;


public class TaInfosPrelevementMapper implements ILgrMapper<TaPrelevementDTO, TaInfosPrelevement> {

	@Override
	public TaInfosPrelevement mapDtoToEntity(TaPrelevementDTO dto, TaInfosPrelevement entity) {
		if(entity==null)
			entity = new TaInfosPrelevement();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaPrelevementDTO mapEntityToDto(TaInfosPrelevement entity, TaPrelevementDTO dto) {
		if(dto==null)
			dto = new TaPrelevementDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
