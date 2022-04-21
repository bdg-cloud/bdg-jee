package fr.legrain.bdg.model.mapping.mapper;

import java.math.BigDecimal;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.paiement.dto.TaLogPaiementInternetDTO;
import fr.legrain.paiement.model.TaLogPaiementInternet;


public class TaLogPaiementInternetMapper implements ILgrMapper<TaLogPaiementInternetDTO, TaLogPaiementInternet> {

	@Override
	public TaLogPaiementInternet mapDtoToEntity(TaLogPaiementInternetDTO dto, TaLogPaiementInternet entity) {
		if(entity==null)
			entity = new TaLogPaiementInternet();

		entity.setIdLogPaiementInternet(dto.getId()!=null?dto.getId():0);
		
		entity.setDatePaiement(dto.getDatePaiement());
		entity.setMontantPaiement(dto.getMontantPaiement());
		entity.setRefPaiementService(dto.getRefPaiementService());
		entity.setServicePaiement(dto.getServicePaiement());
		
		entity.setDevise(dto.getDevise());
		entity.setIdReglement(dto.getIdReglement());
		entity.setCodeReglement(dto.getCodeDocument());
		entity.setIdTiers(dto.getIdTiers());
		entity.setCodeTiers(dto.getCodeTiers());
		entity.setNomTiers(dto.getNomTiers());
		entity.setIdDocument(dto.getIdDocument());
		entity.setCodeDocument(dto.getCodeDocument());
		entity.setTypeDocument(dto.getTypeDocument());
		entity.setOriginePaiement(dto.getOriginePaiement());
		entity.setCommentaire(dto.getCommentaire());
;
//		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaLogPaiementInternetDTO mapEntityToDto(TaLogPaiementInternet entity, TaLogPaiementInternetDTO dto) {
		if(dto==null)
			dto = new TaLogPaiementInternetDTO();

		dto.setId(entity.getIdLogPaiementInternet());
		
		dto.setDatePaiement(entity.getDatePaiement());
		dto.setMontantPaiement(entity.getMontantPaiement());
		dto.setRefPaiementService(entity.getRefPaiementService());
		dto.setServicePaiement(entity.getServicePaiement());
		dto.setDevise(entity.getDevise());
		dto.setIdReglement(entity.getIdReglement());
		dto.setCodeReglement(entity.getCodeDocument());
		dto.setIdTiers(entity.getIdTiers());
		dto.setCodeTiers(entity.getCodeTiers());
		dto.setNomTiers(entity.getNomTiers());
		dto.setIdDocument(entity.getIdDocument());
		dto.setCodeDocument(entity.getCodeDocument());
		dto.setTypeDocument(entity.getTypeDocument());
		dto.setOriginePaiement(entity.getOriginePaiement());
		dto.setCommentaire(entity.getCommentaire());
		
//		dto.setCodepostalAdresse(entity.getCodepostalAdresse());
//		dto.setPaysAdresse(entity.getPaysAdresse());
//		dto.setVilleAdresse(entity.getVilleAdresse());
//		
//		if(entity.getTaTAdr()!=null) dto.setCodeTAdr(entity.getTaTAdr().getCodeTAdr());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
