package fr.legrain.bdg.model.mapping.mapper;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.model.mapping.ILgrMapper;
import fr.legrain.lib.data.LibConversion;


public class TaArticleMapper implements ILgrMapper<TaArticleDTO, TaArticle> {

	@Override
	public TaArticle mapDtoToEntity(TaArticleDTO dto, TaArticle entity) {
		if(entity==null)
			entity = new TaArticle();

		entity.setIdArticle(dto.getId()!=null?dto.getId():0);
		
		entity.setCodeArticle(dto.getCodeArticle());
		entity.setDiversArticle(dto.getDiversArticle());
		entity.setLibellecArticle(dto.getLibellecArticle());
		entity.setLibellelArticle(dto.getLibellelArticle());
		entity.setActif(LibConversion.booleanToInt(dto.getActif()));
		entity.setCommentaireArticle(dto.getCommentaireArticle());

		entity.setHauteur(dto.getHauteur());
//		entity.setIdImport(dto.getiCommentaireArticle());
		entity.setLargeur(dto.getLargeur());
		entity.setLongueur(dto.getLongueur());
		entity.setNumcptArticle(dto.getNumcptArticle());
//		entity.setOrigineImport(dto.getCommentaireArticle());
		entity.setPoids(dto.getPoids());
		entity.setStockMinArticle(dto.getStockMinArticle());
		entity.setParamDluo(dto.getParamDluo());
		
		entity.setMatierePremiere(dto.getMatierePremiere());
		entity.setProduitFini(dto.getProduitFini());
		entity.setCodeBarre(dto.getCodeBarre());
		
////		entity.getTaCatalogueWeb().get (dto.getCommentaireArticle());
////		entity.setTaCategorieArticles(dto.getCommentaireArticle());
////		entity.setTaConditionnementsArticle(dto.getCommentaireArticle());
//		
//		if(entity.getTaTAdr()!=null) entity.getTaTAdr().setCodeTAdr(dto.getCodeTAdr());
//		if(entity.getTaFamille()!=null){
//			entity.getTaFamille(null);
//		}
//		entity.setTaFournisseurs(dto.getCommentaireArticle());
//		entity.setTaImageArticle(dto.getCommentaireArticle());
//		entity.setTaLabelArticles(dto.getCommentaireArticle());
//		entity.setTaNotes(dto.getCommentaireArticle());
//		entity.setTaPrix(dto.getCommentaireArticle());
//		entity.setTaRapportUnite(dto.getCommentaireArticle());
//		
//		entity.setTaRTaTitreTransport(dto.getCommentaireArticle());
//		entity.setTaTTva(dto.getCommentaireArticle());
//		entity.setTaTva(dto.getCommentaireArticle());
		
		entity.setVersionObj(dto.getVersionObj());

		return entity;
	}

	@Override
	public TaArticleDTO mapEntityToDto(TaArticle entity, TaArticleDTO dto) {
		if(dto==null)
			dto = new TaArticleDTO();

		dto.setId(entity.getIdArticle());
		
		dto.setCodeArticle(entity.getCodeArticle());
		dto.setDiversArticle(entity.getDiversArticle());
		dto.setLibcFamille(entity.getLibellecArticle());
		dto.setLibellelArticle(entity.getLibellelArticle());
		dto.setLibellecArticle(entity.getLibellecArticle());
		
		
		dto.setActif(LibConversion.intToBoolean(entity.getActif()));
		dto.setCommentaireArticle(entity.getCommentaireArticle());

		dto.setHauteur(entity.getHauteur());
		dto.setLargeur(entity.getLargeur());
		dto.setLongueur(entity.getLongueur());
		dto.setNumcptArticle(entity.getNumcptArticle());
		dto.setPoids(entity.getPoids());
		dto.setStockMinArticle(entity.getStockMinArticle());
		
		if(entity.getTaFamille()!=null) {
			dto.setCodeFamille(entity.getTaFamille().getCodeFamille());
			dto.setLibcFamille(entity.getTaFamille().getLibcFamille());
		}
		if(entity.getTaRTaTitreTransport()!=null) {
			dto.setQteTitreTransport(entity.getTaRTaTitreTransport().getQteTitreTransport());
			if(entity.getTaRTaTitreTransport().getTaTitreTransport()!=null) 
				dto.setCodeTitreTransport(entity.getTaRTaTitreTransport().getTaTitreTransport().getCodeTitreTransport());
		}
		if(entity.getTaTTva()!=null) {
			dto.setCodeTTva(entity.getTaTTva().getCodeTTva());
			dto.setLibTTva(entity.getTaTTva().getLibTTva());
		}
		if(entity.getTaTva()!=null) {
			dto.setCodeTva(entity.getTaTva().getCodeTva());
			dto.setNumcptTva(entity.getTaTva().getNumcptTva());
			dto.setTauxTva(entity.getTaTva().getTauxTva());
		}
		if(entity.getTaPrix()!=null){
			dto.setPrixPrix(entity.getTaPrix().getPrixPrix());
			dto.setPrixttcPrix(entity.getTaPrix().getPrixttcPrix());
			if(entity.getTaUnite1()!=null){
				dto.setCodeUnite(entity.getTaUnite1().getCodeUnite());
			}
		}
		if(entity.getTaRapportUnite()!=null )
			if (entity.getTaRapportUnite().getTaUnite2()!=null) {
				dto.setCodeUnite2(entity.getTaRapportUnite().getTaUnite2().getCodeUnite());
			dto.setRapport(entity.getTaRapportUnite().getRapport());
			if(entity.getTaRapportUnite().getSens()!=null)
				dto.setSens(LibConversion.intToBoolean(entity.getTaRapportUnite().getSens()));	
			dto.setNbDecimal(entity.getTaRapportUnite().getNbDecimal());	
		}

		dto.setParamDluo(entity.getParamDluo());
		
		dto.setMatierePremiere(entity.getMatierePremiere());
		dto.setProduitFini(entity.getProduitFini());
		
		dto.setCodeBarre(entity.getCodeBarre());
		
		dto.setVersionObj(entity.getVersionObj());

		return dto;
	}

}
