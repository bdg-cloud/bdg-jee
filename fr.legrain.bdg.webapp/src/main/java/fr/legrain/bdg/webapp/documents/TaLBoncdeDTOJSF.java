package fr.legrain.bdg.webapp.documents;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.document.dto.TaLBoncdeDTO;


public class TaLBoncdeDTOJSF extends TaLDocDTOJSF    implements java.io.Serializable, ILigneDocumentJSF<TaLBoncdeDTO>{

	private static final long serialVersionUID = -3584548556600518493L;
	
	
	private TaLBoncdeDTO dto;
	protected TaTitreTransport taTitreTransport;


	public TaLBoncdeDTOJSF() {
		
	}
	
	// TODO gestion annulation ligne
	public TaLBoncdeDTOJSF copy(ILigneDocumentJSF i) {
		TaLBoncdeDTOJSF ihm = (TaLBoncdeDTOJSF) i;
		TaLBoncdeDTOJSF a = new TaLBoncdeDTOJSF();
		a.setDto(TaLBoncdeDTO.copy(ihm.dto));
		a.setTaArticle(ihm.taArticle);
		a.setTaArticleDTO(ihm.taArticleDTO);
		a.setTaEntrepot(ihm.taEntrepot);
		a.setTaLot(ihm.taLot);
		a.setTaRapport(ihm.taRapport);
		a.setTaUnite1(ihm.taUnite1);
		a.setTaUnite2(ihm.taUnite2);
		a.setTaCoefficientUnite(ihm.taCoefficientUnite);
		a.setTaTitreTransport(ihm.taTitreTransport);
		a.setTaUniteSaisie(ihm.taUniteSaisie);
		a.setTaCoefficientUniteSaisie(ihm.taCoefficientUniteSaisie);
		return a;
	}
	
	public void updateDTO(boolean modification) {
		if(dto!=null) {
			if(taArticle!=null) {
				dto.setCodeArticle(taArticle.getCodeArticle());
				dto.setIdArticle(taArticle.getIdArticle());
			}
			if(taArticle!=null) {
				if(taArticleDTO==null) {
					taArticleDTO = new TaArticleDTO();
				}
				taArticleDTO.setCodeArticle(taArticle.getCodeArticle());
			}
			if(taUnite1!=null) {
				dto.setU1LDocument(taUnite1.getCodeUnite());
			}
			if(taUnite2!=null) {
				dto.setU2LDocument(taUnite2.getCodeUnite());
			}
			if(taUniteSaisie!=null) {
				dto.setUSaisieLDocument(taUniteSaisie.getCodeUnite());
			}
			if(taREtat!=null && taREtat.getTaEtat()!=null) {
				dto.setCodeEtat(taREtat.getTaEtat().getCodeEtat());
			}
			if(!modification && taTitreTransport!=null) {
				dto.setCodeTitreTransport(taTitreTransport.getCodeTitreTransport());
			}			
			
				//taArticleDTO.setIdArticle(taArticle.getIdArticle());			
//			if(taEntrepot!=null) {
//				dto.setCodeEntrepot(taEntrepot.getCodeEntrepot());
//			}
//			if(taLot!=null) {
//				dto.setNumLot(taLot.getNumLot());
//			}			
		}
	}

	public TaLBoncdeDTO getDto() {
		return dto;
	}

	public void setDto(TaLBoncdeDTO ligne) {
		this.dto = ligne;
	}

	
	public boolean ligneEstVide() {
		if (taArticle!=null)return false;
		if (dto!=null  && dto.getLibLDocument()!=null && !dto.getLibLDocument().equals(""))return false;		
		return true;
	}

	
	public boolean ligneCommentaire() {
		if (taArticle!=null)return false;
		if (dto!=null && dto.getCodeTLigne()!=null && dto.getCodeTLigne().equals("C"))return true;		
		return true;
	}




	public TaTitreTransport getTaTitreTransport() {
		return taTitreTransport;
	}

	public void setTaTitreTransport(TaTitreTransport taTitreTransport) {
		this.taTitreTransport = taTitreTransport;
	}
}

