package fr.legrain.bdg.webapp.documents;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.document.dto.TaLBoncdeAchatDTO;


public class TaLBoncdeAchatDTOJSF extends TaLDocDTOJSF    implements java.io.Serializable, ILigneDocumentJSF<TaLBoncdeAchatDTO>{

	private static final long serialVersionUID = -3584548556600518493L;
	
	
	private TaLBoncdeAchatDTO dto;

	
	public TaLBoncdeAchatDTOJSF() {
		
	}
	
	// TODO gestion annulation ligne
	public TaLBoncdeAchatDTOJSF copy(ILigneDocumentJSF i) {
		TaLBoncdeAchatDTOJSF ihm = (TaLBoncdeAchatDTOJSF) i;
		TaLBoncdeAchatDTOJSF a = new TaLBoncdeAchatDTOJSF();
		a.setDto(TaLBoncdeAchatDTO.copy(ihm.dto));
		a.setTaArticle(ihm.taArticle);
		a.setTaArticleDTO(ihm.taArticleDTO);
		a.setTaEntrepot(ihm.taEntrepot);
		a.setTaLot(ihm.taLot);
		a.setTaRapport(ihm.taRapport);
		a.setTaUnite1(ihm.taUnite1);
		a.setTaUnite2(ihm.taUnite2);
		a.setTaCoefficientUnite(ihm.taCoefficientUnite);
		a.setTaREtatLigneDocument(ihm.taREtat);
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
		}
	}

	public TaLBoncdeAchatDTO getDto() {
		return dto;
	}

	public void setDto(TaLBoncdeAchatDTO ligne) {
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


}

