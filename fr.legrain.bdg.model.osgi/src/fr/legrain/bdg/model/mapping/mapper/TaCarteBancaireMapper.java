package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCarteBancaire;


public class TaCarteBancaireMapper implements ILgrMapper<TaCarteBancaireDTO, TaCarteBancaire> {

	@Override
	public TaCarteBancaire mapDtoToEntity(TaCarteBancaireDTO dto, TaCarteBancaire entity) {
		if(entity==null)
			entity = new TaCarteBancaire();

		entity.setIdCarteBancaire(dto.getId()!=null?dto.getId():0);
		
		entity.setAdresse1(dto.getAdresse1());
		entity.setAdresse2(dto.getAdresse2()); 
		entity.setAnneeExpiration(dto.getAnneeExpiration()); 
		entity.setCodePostal(dto.getCodePostal()); 
		entity.setCptcomptable(dto.getCptcomptable()); 
		entity.setCvc(dto.getCvc()); 
		entity.setEmpreinte(dto.getEmpreinte()); 
		entity.setLast4(dto.getLast4()); 
		entity.setMoisExpiration(dto.getMoisExpiration()); 
		entity.setNomBanque(dto.getNomBanque()); 
		entity.setNomCompte(dto.getNomCompte()); 
		entity.setNumeroCarte(dto.getNumeroCarte()); 
		entity.setPays(dto.getPays());
		entity.setPaysOrigine(dto.getPaysOrigine());
		entity.setType(dto.getType()); 
		entity.setVille(dto.getVille()); 
		
		
		
		if(entity.getTaTBanque()!=null) entity.getTaTBanque().setCodeTBanque(dto.getCodeTBanque());

		
		entity.setVersionObj(dto.getVersionObj());

		return entity;		
	}

	@Override
	public TaCarteBancaireDTO mapEntityToDto(TaCarteBancaire entity, TaCarteBancaireDTO dto) {
		if(dto==null)
			dto = new TaCarteBancaireDTO();

		dto.setId(entity.getIdCarteBancaire());
		
		dto.setAdresse1(entity.getAdresse1());
		dto.setAdresse2(entity.getAdresse2()); 
		dto.setAnneeExpiration(entity.getAnneeExpiration()); 
		dto.setCodePostal(entity.getCodePostal()); 
		dto.setCptcomptable(entity.getCptcomptable()); 
		dto.setCvc(entity.getCvc()); 
		dto.setEmpreinte(entity.getEmpreinte()); 
		dto.setLast4(entity.getLast4()); 
		dto.setMoisExpiration(entity.getMoisExpiration()); 
		dto.setNomBanque(entity.getNomBanque()); 
		dto.setNomCompte(entity.getNomCompte()); 
		dto.setNumeroCarte(entity.getNumeroCarte()); 
		dto.setPays(entity.getPays());
		dto.setPaysOrigine(entity.getPaysOrigine());
		dto.setType(entity.getType()); 
		dto.setVille(entity.getVille()); 
		
		if(entity.getTaTBanque()!=null) dto.setCodeTBanque(entity.getTaTBanque().getCodeTBanque());

		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
