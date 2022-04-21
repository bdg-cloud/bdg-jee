package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaReglementTracaDTO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaReglementTraca;


public class TaReglementTracaMapper implements ILgrMapper<TaReglementTracaDTO, TaReglementTraca> {

	@Override
	public TaReglementTraca mapDtoToEntity(TaReglementTracaDTO dto, TaReglementTraca entity) {
		if(entity==null)
			entity = new TaReglementTraca();

		entity.setIdReglementTraca(dto.getId()!=null?dto.getId():0);
		
		entity.setLibelleReglement(dto.getLibelleReglement());
		entity.setCodeReglement(dto.getCodeReglement());
		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaReglementTracaDTO mapEntityToDto(TaReglementTraca entity, TaReglementTracaDTO dto) {
		if(dto==null)
			dto = new TaReglementTracaDTO();

		dto.setId(entity.getIdReglementTraca());
		
		dto.setLibelleReglement(entity.getLibelleReglement());
		dto.setCodeReglement(entity.getCodeReglement());
		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
