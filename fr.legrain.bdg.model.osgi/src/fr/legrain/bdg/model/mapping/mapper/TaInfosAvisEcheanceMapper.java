package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.model.TaInfosAvisEcheance;


public class TaInfosAvisEcheanceMapper implements ILgrMapper<TaAvisEcheanceDTO, TaInfosAvisEcheance> {

	@Override
	public TaInfosAvisEcheance mapDtoToEntity(TaAvisEcheanceDTO dto, TaInfosAvisEcheance entity) {
		if(entity==null)
			entity = new TaInfosAvisEcheance();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaAvisEcheanceDTO mapEntityToDto(TaInfosAvisEcheance entity, TaAvisEcheanceDTO dto) {
		if(dto==null)
			dto = new TaAvisEcheanceDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
