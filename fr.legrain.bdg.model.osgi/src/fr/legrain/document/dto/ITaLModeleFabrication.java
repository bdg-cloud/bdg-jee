package fr.legrain.document.dto;

import java.math.BigDecimal;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.stock.model.TaMouvementStock;

public interface ITaLModeleFabrication {
	
	public void setLegrain(boolean legrain);
	
	public TaArticle getTaArticle();

	public void setTaArticle(TaArticle taArticle);
	
	public boolean ligneEstVide();
	
	public TaTLigne getTaTLigne();

	public void setTaTLigne(TaTLigne taTLigne);

//	public TaLot getTaLot();
//
//	public void setTaLot(TaLot taLot);

	public TaEntrepot getTaEntrepot();

	public void setTaEntrepot(TaEntrepot taEntrepot);

	public String getEmplacementLDocument();

	public void setEmplacementLDocument(String emplacementLDocument);
	
	public TaMouvementStock getTaMouvementStock();

	public void setTaMouvementStock(TaMouvementStock taMouvementStock);
	
	
	public Integer getNumLigneLDocument();

	public void setNumLigneLDocument(Integer numLigneLBonliv);

	
	public String getLibLDocument();

	public void setLibLBonliv(String libLBonliv);

	public void setLibLDocument(String libLBonliv);

	
	public BigDecimal getQteLDocument();
	public void setQteLDocument(BigDecimal qteLBonliv);

	
	public BigDecimal getQte2LDocument();

	public void setQte2LDocument(BigDecimal qte2LBonliv);
	
	public String getU1LDocument();

	public void setU1LDocument(String u1LBonliv);
	public String getU2LDocument();

	public void setU2LDocument(String u2LBonliv);
}
