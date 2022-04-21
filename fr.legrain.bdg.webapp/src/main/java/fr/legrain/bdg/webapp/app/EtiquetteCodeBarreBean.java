package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.document.model.TaBonReception;

@Named("etiquetteCodeBarreBean")
@SessionScoped 
public class EtiquetteCodeBarreBean implements Serializable {

	private List<TaArticle> listeArticle;
	
	private TaFabrication taFabrication;
	private TaBonReception taBonReception;
	
	public void videListe() {
		listeArticle = new ArrayList<TaArticle>();
		taFabrication = null;
		taBonReception = null;
	}

	public List<TaArticle> getListeArticle() {
		return listeArticle;
	}

	public void setListeArticle(List<TaArticle> listeArticle) {
		this.listeArticle = listeArticle;
	}
	
}
