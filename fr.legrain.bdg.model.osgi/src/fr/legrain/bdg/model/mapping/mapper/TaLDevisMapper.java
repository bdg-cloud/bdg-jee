package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLDevisDTO;
import fr.legrain.document.model.TaLDevis;
import fr.legrain.document.model.TaLDevis;


public class TaLDevisMapper implements ILgrMapper<TaLDevisDTO, TaLDevis> {

	@Override
	public TaLDevis mapDtoToEntity(TaLDevisDTO dto, TaLDevis entity) {
		if(entity==null)
			entity = new TaLDevis();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLDevisDTO mapEntityToDto(TaLDevis entity, TaLDevisDTO dto) {
		if(dto==null)
			dto = new TaLDevisDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
