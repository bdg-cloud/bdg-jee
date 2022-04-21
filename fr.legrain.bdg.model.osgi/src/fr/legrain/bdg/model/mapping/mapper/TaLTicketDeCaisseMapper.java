package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaLTicketDeCaisseDTO;
import fr.legrain.document.model.TaLTicketDeCaisse;


public class TaLTicketDeCaisseMapper implements ILgrMapper<TaLTicketDeCaisseDTO, TaLTicketDeCaisse> {

	@Override
	public TaLTicketDeCaisse mapDtoToEntity(TaLTicketDeCaisseDTO dto, TaLTicketDeCaisse entity) {
		if(entity==null)
			entity = new TaLTicketDeCaisse();

		entity.setIdLDocument(dto.getIdLDocument()!=null?dto.getIdLDocument():0);
		
//		entity.setLibelleDocument(dto.getLibelleDocument());
//		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLTicketDeCaisseDTO mapEntityToDto(TaLTicketDeCaisse entity, TaLTicketDeCaisseDTO dto) {
		if(dto==null)
			dto = new TaLTicketDeCaisseDTO();

		dto.setIdLDocument(entity.getIdLDocument());
		
//		dto.setLibelleDocument(entity.getLibelleDocument());
//		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
