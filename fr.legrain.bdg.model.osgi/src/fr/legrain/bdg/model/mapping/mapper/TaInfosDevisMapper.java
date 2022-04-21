package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaInfosDevis;


public class TaInfosDevisMapper implements ILgrMapper<TaDevisDTO, TaInfosDevis> {

	@Override
	public TaInfosDevis mapDtoToEntity(TaDevisDTO dto, TaInfosDevis entity) {
		if(entity==null)
			entity = new TaInfosDevis();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaDevisDTO mapEntityToDto(TaInfosDevis entity, TaDevisDTO dto) {
		if(dto==null)
			dto = new TaDevisDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
