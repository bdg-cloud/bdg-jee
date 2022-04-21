package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;
import fr.legrain.tiers.model.TaCompteBanque;


public class TaCompteBanqueMapper implements ILgrMapper<TaCompteBanqueDTO, TaCompteBanque> {

	@Override
	public TaCompteBanque mapDtoToEntity(TaCompteBanqueDTO dto, TaCompteBanque entity) {
		if(entity==null)
			entity = new TaCompteBanque();

		entity.setIdCompteBanque(dto.getId()!=null?dto.getId():0);
		
		entity.setAdresse1Banque(dto.getAdresse1Banque());
		entity.setAdresse2Banque(dto.getAdresse2Banque()); 
		entity.setCleRib(dto.getCleRib()); 
		entity.setCodeBanque(dto.getCodeBanque()); 
		entity.setCodeBIC(dto.getCodeBIC()); 
		entity.setCodeGuichet(dto.getCodeGuichet()); 
		entity.setCompte(dto.getCompte()); 
		entity.setCpBanque(dto.getCpBanque()); 
		entity.setCptcomptable(dto.getCptcomptable()); 
		entity.setIban(dto.getIban()); 
		entity.setNomBanque(dto.getNomBanque()); 
		entity.setTitulaire(dto.getTitulaire()); 
		entity.setVilleBanque(dto.getVilleBanque());
		entity.setNomCompte(dto.getNomCompte()); 
		
		entity.setBankCode(dto.getBankCode()); 
		entity.setBranchCode(dto.getBranchCode()); 
		entity.setCountry(dto.getCountry()); 
		entity.setLast4(dto.getLast4()); 
		
		if(entity.getTaTBanque()!=null) entity.getTaTBanque().setCodeTBanque(dto.getCodeTBanque());

		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaCompteBanqueDTO mapEntityToDto(TaCompteBanque entity, TaCompteBanqueDTO dto) {
		if(dto==null)
			dto = new TaCompteBanqueDTO();

		dto.setId(entity.getIdCompteBanque());
		
		dto.setAdresse1Banque(entity.getAdresse1Banque());
		dto.setAdresse2Banque(entity.getAdresse2Banque()); 
		dto.setCleRib(entity.getCleRib()); 
		dto.setCodeBanque(entity.getCodeBanque()); 
		dto.setCodeBIC(entity.getCodeBIC()); 
		dto.setCodeGuichet(entity.getCodeGuichet()); 
		dto.setCompte(entity.getCompte()); 
		dto.setCpBanque(entity.getCpBanque()); 
		dto.setCptcomptable(entity.getCptcomptable()); 
		dto.setIban(entity.getIban()); 
		dto.setNomBanque(entity.getNomBanque()); 
		dto.setTitulaire(entity.getTitulaire()); 
		dto.setVilleBanque(entity.getVilleBanque()); 
		dto.setNomCompte(entity.getNomCompte()); 
		
		dto.setBankCode(entity.getBankCode()); 
		dto.setBranchCode(entity.getBranchCode()); 
		dto.setCountry(entity.getCountry()); 
		dto.setLast4(entity.getLast4()); 
		
		if(entity.getTaTBanque()!=null) dto.setCodeTBanque(entity.getTaTBanque().getCodeTBanque());

		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
