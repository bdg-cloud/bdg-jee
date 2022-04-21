package fr.legrain.bdg.webapp.documents;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.document.dto.TaLTicketDeCaisseDTO;
import fr.legrain.stock.model.TaMouvementStock;


public class TaLTicketDeCaisseDTOJSF extends TaLDocDTOJSF    implements java.io.Serializable, ILigneDocumentJSF<TaLTicketDeCaisseDTO>{

	private static final long serialVersionUID = 894328367072057512L;

	private TaLTicketDeCaisseDTO dto;

	
	public TaLTicketDeCaisseDTOJSF() {
		
	}
	// TODO gestion annulation ligne
	public TaLTicketDeCaisseDTOJSF copy(ILigneDocumentJSF i) {
		TaLTicketDeCaisseDTOJSF ihm = (TaLTicketDeCaisseDTOJSF) i;
		TaLTicketDeCaisseDTOJSF a = new TaLTicketDeCaisseDTOJSF();
		a.setDto(TaLTicketDeCaisseDTO.copy(ihm.dto));
		a.setTaArticle(ihm.taArticle);
		a.setTaArticleDTO(ihm.taArticleDTO);
		a.setTaEntrepot(ihm.taEntrepot);
		a.setTaLot(ihm.taLot);
		a.setTaRapport(ihm.taRapport);
		a.setTaUnite1(ihm.taUnite1);
		a.setTaUnite2(ihm.taUnite2);
		a.setTaMouvementStock(ihm.taMouvementStock);
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
				//taArticleDTO.setIdArticle(taArticle.getIdArticle());
			}
			if(!modification && taEntrepot!=null) {
				dto.setCodeEntrepot(taEntrepot.getCodeEntrepot());
			}
			if(!modification && taLot!=null) {
				dto.setNumLot(taLot.getNumLot());
				dto.setLibelleLot(taLot.getLibelle());
			}	
			if(!modification && taUnite1!=null) {
				dto.setU1LDocument(taUnite1.getCodeUnite());
			}
			if(!modification && taUnite2!=null) {
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

	public TaLTicketDeCaisseDTO getDto() {
		return dto;
	}

	public void setDto(TaLTicketDeCaisseDTO ligne) {
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

