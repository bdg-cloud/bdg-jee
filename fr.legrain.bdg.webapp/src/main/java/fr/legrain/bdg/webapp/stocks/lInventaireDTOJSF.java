package fr.legrain.bdg.webapp.stocks;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.stock.dto.LInventaireDTO;


public class lInventaireDTOJSF  implements java.io.Serializable{

	private static final long serialVersionUID = -7642948958864882781L;
	
	private LInventaireDTO dto;
	private TaLot taLot;
	private TaArticle taArticle;
	private TaEntrepot taEntrepotOrg;
	private boolean modifie=false;
	private boolean supprime=false;
	
	public lInventaireDTOJSF() {
		
	}

	public LInventaireDTO getDto() {
		return dto;
	}

	public void setDto(LInventaireDTO ligne) {
		this.dto = ligne;
	}

	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

	public TaEntrepot getTaEntrepotOrg() {
		return taEntrepotOrg;
	}

	public void setTaEntrepotOrg(TaEntrepot taEntrepot) {
		this.taEntrepotOrg = taEntrepot;
	}


	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}
	

	public void updateDTO() {
		if(dto!=null) {
			if(taArticle!=null) {
				dto.setCodeArticle(taArticle.getCodeArticle());
			}
			if(taEntrepotOrg!=null) {
				dto.setCodeEntrepot(taEntrepotOrg.getCodeEntrepot());
			}
			if(taLot!=null) {
				dto.setNumLot(taLot.getNumLot());
				dto.setDluo(taLot.getDluo());
				//dto.setLotTermine(taLot.getTermine());
			}
			
		}
	}

	public boolean isModifie() {
		return modifie;
	}

	public void setModifie(boolean modifie) {
		this.modifie = modifie;
	}

	public boolean isSupprime() {
		return supprime;
	}

	public void setSupprime(boolean supprime) {
		this.supprime = supprime;
	}

	public boolean ligneEstVide() {
		if (taArticle!=null)return false;
		if (dto!=null && dto.getLibelleLInventaire()!=null && !dto.getLibelleLInventaire().equals(""))return false;		
		return true;
	}
}
