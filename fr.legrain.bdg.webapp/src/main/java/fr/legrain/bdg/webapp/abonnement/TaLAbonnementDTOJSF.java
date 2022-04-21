package fr.legrain.bdg.webapp.abonnement;

import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.webapp.documents.ILigneDocumentJSF;
import fr.legrain.bdg.webapp.documents.TaLDocDTOJSF;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.dto.TaLDevisDTO;


public class TaLAbonnementDTOJSF extends TaLDocDTOJSF  implements java.io.Serializable, ILigneDocumentJSF<TaLAbonnementDTO>{

	private static final long serialVersionUID = 894328367072057512L;

//	private TaStripeSubscriptionItemDTO dto2;
//	private TaStripeSubscriptionItem taStripeSubscriptionItem;
	private TaLAbonnementDTO dto;
	private TaLot taLot;
	private TaArticle taArticle;
	private TaArticleDTO taArticleDTO;
	private TaStripeProduct taStripeProduct;
	private TaStripeProductDTO taStripeProductDTO;
	private TaStripePlan taStripePlan;
	private TaStripePlanDTO taStripePlanDTO;
	private TaEntrepot taEntrepot;
	private TaRapportUnite taRapport;
	private TaUnite taUnite1;
	private TaUnite taUnite2;
	private TaCoefficientUnite taCoefficientUnite;
	
	public TaLAbonnementDTOJSF() {
		dto = new TaLAbonnementDTO();
//		dto2 = new TaStripeSubscriptionItemDTO();
	}
	
	// TODO gestion annulation ligne
	@Override
	public TaLAbonnementDTOJSF copy(ILigneDocumentJSF i) {
		TaLAbonnementDTOJSF ihm = (TaLAbonnementDTOJSF) i;
		TaLAbonnementDTOJSF a = new TaLAbonnementDTOJSF();
		a.setDto(TaLAbonnementDTO.copy(ihm.dto));
//		a.setDto2(TaLAbonnementDTO.copy(ihm.dto));
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
		a.setTaREtatLigneDocument(ihm.taREtat);
		return a;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#updateDTO()
	 */
	public void updateDTO(boolean modification) {
		if(dto!=null) {
			if(taArticle!=null) {
				dto.setCodeArticle(taArticle.getCodeArticle());
//				dto.setIdArticle(taArticle.getIdArticle());
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

//			if(taStripeSubscriptionItem!=null) {
//				if(dto2==null) {
//					dto2 = new TaStripeSubscriptionItemDTO();
//				}
//				dto2.setId(taStripeSubscriptionItem.getIdStripeSubscriptionItem());
//			}
//			if(taUnite1!=null) {
//				dto.setU1LDocument(taUnite1.getCodeUnite());
//			}
//			if(taUnite2!=null) {
//				dto.setU2LDocument(taUnite2.getCodeUnite());
//			}
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
	public TaLAbonnementDTO getDto() {
		return dto;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setDto(fr.legrain.document.dto.TaLDevisDTO)
	 */
	public void setDto(TaLAbonnementDTO ligne) {
		this.dto = ligne;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getTaArticle()
	 */
	public TaArticle getTaArticle() {
		return taArticle;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setTaArticle(fr.legrain.article.model.TaArticle)
	 */
	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getTaEntrepot()
	 */
	public TaEntrepot getTaEntrepot() {
		return taEntrepot;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setTaEntrepot(fr.legrain.article.model.TaEntrepot)
	 */
	public void setTaEntrepot(TaEntrepot taEntrepot) {
		this.taEntrepot = taEntrepot;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getTaLot()
	 */
	public TaLot getTaLot() {
		return taLot;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setTaLot(fr.legrain.article.model.TaLot)
	 */
	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getTaRapport()
	 */
	public TaRapportUnite getTaRapport() {
		return taRapport;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setTaRapport(fr.legrain.article.model.TaRapportUnite)
	 */
	public void setTaRapport(TaRapportUnite taRapport) {
		this.taRapport = taRapport;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#ligneEstVide()
	 */
	public boolean ligneEstVide() {
		if (taArticle!=null)return false;
//		if (dto!=null  && dto.getLibLDocument()!=null && !dto.getLibLDocument().equals(""))return false;		
		return true;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getTaArticleDTO()
	 */
	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setTaArticleDTO(fr.legrain.article.dto.TaArticleDTO)
	 */
	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getTaUnite1()
	 */
	public TaUnite getTaUnite1() {
		return taUnite1;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setTaUnite1(fr.legrain.article.model.TaUnite)
	 */
	public void setTaUnite1(TaUnite taUnite1) {
		this.taUnite1 = taUnite1;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#getTaUnite2()
	 */
	public TaUnite getTaUnite2() {
		return taUnite2;
	}

	/* (non-Javadoc)
	 * @see fr.legrain.bdg.webapp.documents.ILigneDocumentJSF#setTaUnite2(fr.legrain.article.model.TaUnite)
	 */
	public void setTaUnite2(TaUnite taUnite2) {
		this.taUnite2 = taUnite2;
	}
	
	public TaCoefficientUnite getTaCoefficientUnite() {
		return taCoefficientUnite;
	}
	public void setTaCoefficientUnite(TaCoefficientUnite taCoefficientUnite) {
		this.taCoefficientUnite = taCoefficientUnite;
	}

//	public TaStripeSubscriptionItemDTO getDto2() {
//		return dto2;
//	}
//
//	public void setDto2(TaStripeSubscriptionItemDTO dto2) {
//		this.dto2 = dto2;
//	}

	public TaStripePlan getTaStripePlan() {
		return taStripePlan;
	}

	public void setTaStripePlan(TaStripePlan taStripePlan) {
		this.taStripePlan = taStripePlan;
	}

	public TaStripePlanDTO getTaStripePlanDTO() {
		return taStripePlanDTO;
	}

	public void setTaStripePlanDTO(TaStripePlanDTO taStripePlanDTO) {
		this.taStripePlanDTO = taStripePlanDTO;
	}

	public TaStripeProduct getTaStripeProduct() {
		return taStripeProduct;
	}

	public void setTaStripeProduct(TaStripeProduct taStripeProduct) {
		this.taStripeProduct = taStripeProduct;
	}

	public TaStripeProductDTO getTaStripeProductDTO() {
		return taStripeProductDTO;
	}

	public void setTaStripeProductDTO(TaStripeProductDTO taStripeProductDTO) {
		this.taStripeProductDTO = taStripeProductDTO;
	}

//	public TaStripeSubscriptionItem getTaStripeSubscriptionItem() {
//		return taStripeSubscriptionItem;
//	}
//
//	public void setTaStripeSubscriptionItem(TaStripeSubscriptionItem taStripeSubscriptionItem) {
//		this.taStripeSubscriptionItem = taStripeSubscriptionItem;
//	}



}
