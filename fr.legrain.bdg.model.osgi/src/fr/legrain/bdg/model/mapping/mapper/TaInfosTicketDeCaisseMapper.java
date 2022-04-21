package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaInfosTicketDeCaisse;


public class TaInfosTicketDeCaisseMapper implements ILgrMapper<TaTicketDeCaisseDTO, TaInfosTicketDeCaisse> {

	@Override
	public TaInfosTicketDeCaisse mapDtoToEntity(TaTicketDeCaisseDTO dto, TaInfosTicketDeCaisse entity) {
		if(entity==null)
			entity = new TaInfosTicketDeCaisse();

//		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTicketDeCaisseDTO mapEntityToDto(TaInfosTicketDeCaisse entity, TaTicketDeCaisseDTO dto) {
		if(dto==null)
			dto = new TaTicketDeCaisseDTO();

//		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
