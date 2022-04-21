package fr.legrain.servicewebexterne.mapper;



import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;




public class TaLigneServiceWebExterneMapper implements ILgrMapper<TaLigneServiceWebExterneDTO, TaLigneServiceWebExterne> {

	@Override
	public TaLigneServiceWebExterne mapDtoToEntity(TaLigneServiceWebExterneDTO dto, TaLigneServiceWebExterne entity) {
		if(entity==null)
			entity = new TaLigneServiceWebExterne();
		
//
//		entity.setIdFichier(dto.getId()!=null?dto.getId():0);
//		entity.setCodeFichier(dto.getCodeFichier());
//		entity.setJournalFichier(dto.getJournalFichier());
//		entity.setLibelleEdition(dto.getLibelleEdition());	
//		entity.setLibelleFichier(dto.getLibelleFichier());	
//		
//		entity.setVersionObj(dto.getVersionObj());
//
		return entity;
		
	}

	@Override
	public TaLigneServiceWebExterneDTO mapEntityToDto(TaLigneServiceWebExterne entity, TaLigneServiceWebExterneDTO dto) {
		if(dto==null)
			dto = new TaLigneServiceWebExterneDTO();
		
		dto.setId(entity.getIdLigneServiceWebExterne());
		dto.setLibelle(entity.getLibelle());
		dto.setRefArticle(entity.getRefArticle());
		dto.setNomArticle(entity.getNomArticle());
		dto.setPrenomTiers(entity.getPrenomTiers());
		dto.setNomTiers(entity.getNomTiers());
		dto.setRefTiers(entity.getRefTiers());
		dto.setRefCommandeExterne(entity.getRefCommandeExterne());
		dto.setDateCreationExterne(entity.getDateCreationExterne());
		dto.setMontantTtc(entity.getMontantTtc());
		dto.setQteArticle(entity.getQteArticle());
		if(entity.getTaArticle() != null) {
			dto.setCodeArticle(entity.getTaArticle().getCodeArticle());
			dto.setLibellecArticle(entity.getTaArticle().getLibellecArticle());
		}
		if(entity.getTaTiers() != null) {
			dto.setCodeTiers(entity.getTaTiers().getCodeTiers());
		}
		if(entity.getTaTPaiement() != null) {
			dto.setCodeTPaiement(entity.getTaTPaiement().getCodeTPaiement());
			dto.setLiblTPaiement(entity.getTaTPaiement().getLibTPaiement());
		}
		
		if(entity.getTaLot() != null) {
			dto.setNumLot(entity.getTaLot().getNumLot());
		}
		
		dto.setValeur1(entity.getValeur1());
		dto.setValeur2(entity.getValeur2());
		dto.setValeur3(entity.getValeur3());
		dto.setValeur4(entity.getValeur4());
		
		if(entity.getTaCompteServiceWebExterne() != null) {
			dto.setLibelleCompteServiceWebExterne(entity.getTaCompteServiceWebExterne().getLibelleCompteServiceWebExterne());
		}
		
		
		dto.setTermine(entity.getTermine());
	
		
		
//
//		dto.setId(entity.getIdFichier());
//		dto.setCodeFichier(entity.getCodeFichier());
//		dto.setJournalFichier(entity.getJournalFichier());
//		dto.setLibelleEdition(entity.getLibelleEdition());	
//		dto.setLibelleFichier(entity.getLibelleFichier());	
//		
//		dto.setVersionObj(entity.getVersionObj());
//
		return dto;
		
	}

}
