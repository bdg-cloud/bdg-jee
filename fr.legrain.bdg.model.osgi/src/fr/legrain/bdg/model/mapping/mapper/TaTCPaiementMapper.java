package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.tiers.dto.TaTCPaiementDTO;
import fr.legrain.tiers.model.TaTCPaiement;


public class TaTCPaiementMapper implements ILgrMapper<TaTCPaiementDTO, TaTCPaiement> {

	@Override
	public TaTCPaiement mapDtoToEntity(TaTCPaiementDTO dto, TaTCPaiement entity) {
		if(entity==null)
			entity = new TaTCPaiement();

		entity.setIdTCPaiement(dto.getId()!=null?dto.getId():0);
		entity.setCodeTCPaiement(dto.getCodeCPaiement());
		entity.setLiblTCPaiement(dto.getLibCPaiement());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaTCPaiementDTO mapEntityToDto(TaTCPaiement entity, TaTCPaiementDTO dto) {
		if(dto==null)
			dto = new TaTCPaiementDTO();

		dto.setId(entity.getIdTCPaiement());
		dto.setCodeCPaiement(entity.getCodeTCPaiement());
		dto.setLibCPaiement(entity.getLiblTCPaiement());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
