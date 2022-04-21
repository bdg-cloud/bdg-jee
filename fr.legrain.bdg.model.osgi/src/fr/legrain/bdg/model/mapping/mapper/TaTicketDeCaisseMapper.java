package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaTicketDeCaisse;


public class TaTicketDeCaisseMapper implements ILgrMapper<TaTicketDeCaisseDTO, TaTicketDeCaisse> {

	@Override
	public TaTicketDeCaisse mapDtoToEntity(TaTicketDeCaisseDTO dto, TaTicketDeCaisse entity) {
		if(entity==null)
			entity = new TaTicketDeCaisse();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
		if(entity.getTaTiers()!=null) entity.getTaTiers().setCodeTiers(dto.getCodeTiers());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTicketDeCaisseDTO mapEntityToDto(TaTicketDeCaisse entity, TaTicketDeCaisseDTO dto) {
		if(dto==null)
			dto = new TaTicketDeCaisseDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setDateDocument(entity.getDateDocument());
		
		if(entity.getTaTiers()!=null) dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		
		dto.setVersionObj(entity.getVersionObj());
		return dto;
	}

}
