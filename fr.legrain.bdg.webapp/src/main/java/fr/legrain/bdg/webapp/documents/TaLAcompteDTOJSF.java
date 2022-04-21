package fr.legrain.bdg.webapp.documents;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.document.dto.TaLAcompteDTO;
import fr.legrain.document.model.TaEtat;


public class TaLAcompteDTOJSF extends TaLDocDTOJSF    implements java.io.Serializable, ILigneDocumentJSF<TaLAcompteDTO>{

	private static final long serialVersionUID = 894328367072057512L;

	private TaLAcompteDTO dto;

	private TaEtat taEtat;
	
	
	public TaLAcompteDTOJSF() {
		
	}
	
	// TODO gestion annulation ligne
	public TaLAcompteDTOJSF copy(ILigneDocumentJSF i) {
		TaLAcompteDTOJSF ihm = (TaLAcompteDTOJSF) i;
		TaLAcompteDTOJSF a = new TaLAcompteDTOJSF();
		a.setDto(TaLAcompteDTO.copy(ihm.dto));
		a.setTaArticle(ihm.taArticle);
		a.setTaArticleDTO(ihm.taArticleDTO);
		a.setTaEntrepot(ihm.taEntrepot);
		a.setTaLot(ihm.taLot);
		a.setTaRapport(ihm.taRapport);
		a.setTaUnite1(ihm.taUnite1);
		a.setTaUnite2(ihm.taUnite2);
		a.setTaUniteSaisie(ihm.taUniteSaisie);
		a.setTaCoefficientUniteSaisie(ihm.taCoefficientUniteSaisie);
		a.setTaCoefficientUnite(ihm.taCoefficientUnite);
		a.setTaEtat(ihm.taEtat);
		return a;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#updateDTO()
	 */
	@Override
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
			
				//taArticleDTO.setIdArticle(taArticle.getIdArticle());			
//			if(taEntrepot!=null) {
//				dto.setCodeEntrepot(taEntrepot.getCodeEntrepot());
//			}
//			if(taLot!=null) {
//				dto.setNumLot(taLot.getNumLot());
//			}			
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getDto()
	 */
	@Override
	public TaLAcompteDTO getDto() {
		return dto;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setDto(fr.legrain.document.dto.TaLAcompteDTO)
	 */
	@Override
	public void setDto(TaLAcompteDTO ligne) {
		this.dto = ligne;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#ligneEstVide()
	 */
	@Override
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

	public TaEtat getTaEtat() {
		return taEtat;
	}

	public void setTaEtat(TaEtat taEtat) {
		this.taEtat = taEtat;
	}
}
