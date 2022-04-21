package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.model.TaInfosAcompte;


public class TaInfosAcompteMapper implements ILgrMapper<TaAcompteDTO, TaInfosAcompte> {

	@Override
	public TaInfosAcompte mapDtoToEntity(TaAcompteDTO dto, TaInfosAcompte entity) {
		if(entity==null)
			entity = new TaInfosAcompte();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAcompteDTO mapEntityToDto(TaInfosAcompte entity, TaAcompteDTO dto) {
		if(dto==null)
			dto = new TaAcompteDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
