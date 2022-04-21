package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tache.dto.TaTypeEvenementDTO;
import fr.legrain.tache.model.TaTypeEvenement;
import fr.legrain.tache.model.TaTypeEvenement;


public class TaTypeEvenementMapper implements ILgrMapper<TaTypeEvenementDTO, TaTypeEvenement> {

	@Override
	public TaTypeEvenement mapDtoToEntity(TaTypeEvenementDTO dto, TaTypeEvenement entity) {
		if(entity==null)
			entity = new TaTypeEvenement();

		entity.setIdTypeEvenement(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTypeEvenement(dto.getCodeTypeEvenement());
		entity.setLibelleTypeEvenement(dto.getLibelleCategorieEvenement());
//		entity.setAdresse3Adresse(dto.getAdresse3Adresse());
//		entity.setCodepostalAdresse(dto.getCodepostalAdresse());
//		entity.setPaysAdresse(dto.getPaysAdresse());
//		entity.setVilleAdresse(dto.getVilleAdresse());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTypeEvenementDTO mapEntityToDto(TaTypeEvenement entity, TaTypeEvenementDTO dto) {
		if(dto==null)
			dto = new TaTypeEvenementDTO();

		dto.setId(entity.getIdTypeEvenement());
		
		dto.setCodeTypeEvenement(entity.getCodeTypeEvenement());
		dto.setLibelleTypeEvenement(entity.getLibelleTypeEvenement());
//		dto.setAdresse3Adresse(entity.getAdresse3Adresse());
//		dto.setCodepostalAdresse(entity.getCodepostalAdresse());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
		
		if(entity.getTaCategorieEvenement()!=null) dto.setCodeCategorieEvenement(entity.getTaCategorieEvenement().getCodeCategorieEvenement());
		if(entity.getTaCategorieEvenement()!=null) dto.setLibelleTypeEvenement(entity.getTaCategorieEvenement().getLibelleCategorieEvenement());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
