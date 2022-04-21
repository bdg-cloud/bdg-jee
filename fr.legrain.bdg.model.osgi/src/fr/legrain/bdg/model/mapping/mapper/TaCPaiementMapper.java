package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;


public class TaCPaiementMapper implements ILgrMapper<TaCPaiementDTO, TaCPaiement> {

	@Override
	public TaCPaiement mapDtoToEntity(TaCPaiementDTO dto, TaCPaiement entity) {
		if(entity==null)
			entity = new TaCPaiement();

		entity.setIdCPaiement(dto.getId()!=null?dto.getId():0);
		entity.setCodeCPaiement(dto.getCodeCPaiement());
		entity.setLibCPaiement(dto.getLibCPaiement());
		entity.setReportCPaiement(dto.getReportCPaiement());
		entity.setFinMoisCPaiement(dto.getFinMoisCPaiement());
		
		if(entity.getTaTCPaiement()!=null) entity.getTaTCPaiement().setCodeTCPaiement(dto.getCodeTCPaiement());
		if(entity.getTaTCPaiement()!=null) entity.getTaTCPaiement().setLiblTCPaiement(dto.getLiblTCPaiement());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaCPaiementDTO mapEntityToDto(TaCPaiement entity, TaCPaiementDTO dto) {
		if(dto==null)
			dto = new TaCPaiementDTO();

		dto.setId(entity.getIdCPaiement());
		dto.setCodeCPaiement(entity.getCodeCPaiement());
		dto.setLibCPaiement(entity.getLibCPaiement());
		dto.setReportCPaiement(entity.getReportCPaiement());
		dto.setFinMoisCPaiement(entity.getFinMoisCPaiement());
		
		if(entity.getTaTCPaiement()!=null) dto.setCodeTCPaiement(entity.getTaTCPaiement().getCodeTCPaiement());
		if(entity.getTaTCPaiement()!=null) dto.setLiblTCPaiement(entity.getTaTCPaiement().getLiblTCPaiement());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
