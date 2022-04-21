package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tache.dto.TaRecurrenceEvenementDTO;
import fr.legrain.tache.model.TaRecurrenceEvenement;


public class TaRecurrenceEvenementMapper implements ILgrMapper<TaRecurrenceEvenementDTO, TaRecurrenceEvenement> {

	@Override
	public TaRecurrenceEvenement mapDtoToEntity(TaRecurrenceEvenementDTO dto, TaRecurrenceEvenement entity) {
		if(entity==null)
			entity = new TaRecurrenceEvenement();

		entity.setIdRecurrence(dto.getId()!=null?dto.getId():0);
		
//		entity.setAdresse1Adresse(dto.getAdresse1Adresse());
//		entity.setAdresse2Adresse(dto.getAdresse2Adresse());
//		entity.setAdresse3Adresse(dto.getAdresse3Adresse());
//		entity.setCodepostalAdresse(dto.getCodepostalAdresse());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaRecurrenceEvenementDTO mapEntityToDto(TaRecurrenceEvenement entity, TaRecurrenceEvenementDTO dto) {
		if(dto==null)
			dto = new TaRecurrenceEvenementDTO();

		dto.setId(entity.getIdRecurrence());
		
//		dto.setAdresse1Adresse(entity.getAdresse1Adresse());
//		dto.setAdresse2Adresse(entity.getAdresse2Adresse());
//		dto.setAdresse3Adresse(entity.getAdresse3Adresse());
//		dto.setCodepostalAdresse(entity.getCodepostalAdresse());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
