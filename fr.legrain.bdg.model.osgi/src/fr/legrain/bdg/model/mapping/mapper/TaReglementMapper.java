package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaReglement;


public class TaReglementMapper implements ILgrMapper<TaReglementDTO, TaReglement> {

	@Override
	public TaReglement mapDtoToEntity(TaReglementDTO dto, TaReglement entity) {
		if(entity==null)
			entity = new TaReglement();

		entity.setIdDocument(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleDocument(dto.getLibelleDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaReglementDTO mapEntityToDto(TaReglement entity, TaReglementDTO dto) {
		if(dto==null)
			dto = new TaReglementDTO();

		dto.setId(entity.getIdDocument());
		
		dto.setLibelleDocument(entity.getLibelleDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
