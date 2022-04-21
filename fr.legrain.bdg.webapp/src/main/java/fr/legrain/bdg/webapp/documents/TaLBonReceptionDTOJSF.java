package fr.legrain.bdg.webapp.documents;

import java.util.List;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaLabelArticleDTO;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.document.dto.TaLBonReceptionDTO;


public class TaLBonReceptionDTOJSF extends TaLDocDTOJSF    implements java.io.Serializable, ILigneDocumentJSF<TaLBonReceptionDTO>{

	private static final long serialVersionUID = 894328367072057512L;

	private TaLBonReceptionDTO dto;

	private List<TaLabelArticle> listeCertification;
	private List<TaLabelArticleDTO> listeCertificationDTO;
 
	
	public TaLBonReceptionDTOJSF() {
		
	}
	
	// TODO gestion annulation ligne
	public TaLBonReceptionDTOJSF copy(ILigneDocumentJSF i) {
		TaLBonReceptionDTOJSF ihm = (TaLBonReceptionDTOJSF) i;
		TaLBonReceptionDTOJSF a = new TaLBonReceptionDTOJSF();
		a.setDto(TaLBonReceptionDTO.copy(ihm.dto));
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

	public TaLBonReceptionDTO getDto() {
		return dto;
	}

	public void setDto(TaLBonReceptionDTO ligne) {
		this.dto = ligne;
	}

	
	public boolean ligneEstVide() {
		if (taArticle!=null)return false;
		if (dto!=null && dto.getLibLDocument()!=null && !dto.getLibLDocument().equals(""))return false;		
		return true;
	}

	public List<TaLabelArticle> getListeCertification() {
		return listeCertification;
	}

	public void setListeCertification(List<TaLabelArticle> listeCertification) {
		this.listeCertification = listeCertification;
	}

	public List<TaLabelArticleDTO> getListeCertificationDTO() {
		return listeCertificationDTO;
	}

	public void setListeCertificationDTO(List<TaLabelArticleDTO> listeCertificationDTO) {
		this.listeCertificationDTO = listeCertificationDTO;
	}

	

	
	public boolean ligneCommentaire() {
		if (taArticle!=null)return false;
		if (dto!=null && dto.getCodeTLigne()!=null && dto.getCodeTLigne().equals("C"))return true;	
		return true;
	}

	
}

