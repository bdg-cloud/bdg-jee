package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLPrelevementDTO;
import fr.legrain.document.model.TaLPrelevement;


public class TaLPrelevementMapper implements ILgrMapper<TaLPrelevementDTO, TaLPrelevement> {

	@Override
	public TaLPrelevement mapDtoToEntity(TaLPrelevementDTO dto, TaLPrelevement entity) {
		if(entity==null)
			entity = new TaLPrelevement();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLPrelevementDTO mapEntityToDto(TaLPrelevement entity, TaLPrelevementDTO dto) {
		if(dto==null)
			dto = new TaLPrelevementDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
