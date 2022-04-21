package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLAcompteDTO;
import fr.legrain.document.model.TaLAcompte;


public class TaLAcompteMapper implements ILgrMapper<TaLAcompteDTO, TaLAcompte> {

	@Override
	public TaLAcompte mapDtoToEntity(TaLAcompteDTO dto, TaLAcompte entity) {
		if(entity==null)
			entity = new TaLAcompte();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLAcompteDTO mapEntityToDto(TaLAcompte entity, TaLAcompteDTO dto) {
		if(dto==null)
			dto = new TaLAcompteDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
