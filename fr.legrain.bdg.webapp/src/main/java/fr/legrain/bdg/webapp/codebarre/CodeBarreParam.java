package fr.legrain.bdg.webapp.codebarre;

import java.io.Serializable;
import java.math.BigDecimal;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaUnite;

public class CodeBarreParam implements Serializable {
	
	public static final String NOM_OBJET_EN_SESSION = "paramCodebarre";
	
	private TaArticle taArticle;
	private TaLot taLot;
	private TaUnite taUnite;
	private BigDecimal quantite;
	
	public TaArticle getTaArticle() {
		return taArticle;
	}
	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	public TaLot getTaLot() {
		return taLot;
	}
	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}
	public TaUnite getTaUnite() {
		return taUnite;
	}
	public void setTaUnite(TaUnite taUnite) {
		this.taUnite = taUnite;
	}
	public BigDecimal getQuantite() {
		return quantite;
	}
	public void setQuantite(BigDecimal quantite) {
		this.quantite = quantite;
	}
	
	
}
