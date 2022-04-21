package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaBoncdeAchatDTO;
import fr.legrain.document.model.TaInfosBoncdeAchat;


public class TaInfosBoncdeAchatMapper implements ILgrMapper<TaBoncdeAchatDTO, TaInfosBoncdeAchat> {

	@Override
	public TaInfosBoncdeAchat mapDtoToEntity(TaBoncdeAchatDTO dto, TaInfosBoncdeAchat entity) {
		if(entity==null)
			entity = new TaInfosBoncdeAchat();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaBoncdeAchatDTO mapEntityToDto(TaInfosBoncdeAchat entity, TaBoncdeAchatDTO dto) {
		if(dto==null)
			dto = new TaBoncdeAchatDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
