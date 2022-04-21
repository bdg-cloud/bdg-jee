package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLBoncdeAchatDTO;
import fr.legrain.document.model.TaLBoncdeAchat;


public class TaLBoncdeAchatMapper implements ILgrMapper<TaLBoncdeAchatDTO, TaLBoncdeAchat> {

	@Override
	public TaLBoncdeAchat mapDtoToEntity(TaLBoncdeAchatDTO dto, TaLBoncdeAchat entity) {
		if(entity==null)
			entity = new TaLBoncdeAchat();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLBoncdeAchatDTO mapEntityToDto(TaLBoncdeAchat entity, TaLBoncdeAchatDTO dto) {
		if(dto==null)
			dto = new TaLBoncdeAchatDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
