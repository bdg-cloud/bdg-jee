package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;


public class TaTPaiementMapper implements ILgrMapper<TaTPaiementDTO, TaTPaiement> {

	@Override
	public TaTPaiement mapDtoToEntity(TaTPaiementDTO dto, TaTPaiement entity) {
		if(entity==null)
			entity = new TaTPaiement();

		entity.setIdTPaiement(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeTPaiement(dto.getCodeTPaiement());
		entity.setCompte(dto.getCompte());
		entity.setFinMoisTPaiement(dto.getFinMoisTPaiement());
		entity.setLibTPaiement(dto.getLibTPaiement());
		entity.setReportTPaiement(dto.getReportTPaiement());
			
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaTPaiementDTO mapEntityToDto(TaTPaiement entity, TaTPaiementDTO dto) {
		if(dto==null)
			dto = new TaTPaiementDTO();

		dto.setId(entity.getIdTPaiement());
		
		dto.setCodeTPaiement(entity.getCodeTPaiement());
		dto.setCompte(entity.getCompte());
		dto.setFinMoisTPaiement(entity.getFinMoisTPaiement());
		dto.setLibTPaiement(entity.getLibTPaiement());
		dto.setReportTPaiement(entity.getReportTPaiement());
			
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
