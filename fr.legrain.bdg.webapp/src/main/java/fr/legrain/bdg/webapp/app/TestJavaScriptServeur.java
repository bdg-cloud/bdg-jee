package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import fr.legrain.article.model.TaArticle;

@Named("testJavaScriptServeur")
//@ViewScoped
@SessionScoped
public class TestJavaScriptServeur implements Serializable{

	private static final long serialVersionUID = -2159125423417505350L;
	
	private List<String> listeString;
	private List<TaArticle> listeArticle;
	
//	private @Inject ITaArticleServiceRemote taArticleService;

	@PostConstruct
    public void init() {
		listeString = new ArrayList<>();
		listeString.add("a");
		listeString.add("bb");
		listeString.add("ccc");
		
		//listeArticle = taArticleService.selectAll();
	}

	public List<String> getListeString() {
		return listeString;
	}

	public void setListeString(List<String> listeString) {
		this.listeString = listeString;
	}

	public List<TaArticle> getListeArticle() {
		return listeArticle;
	}

	public void setListeArticle(List<TaArticle> listeArticle) {
		this.listeArticle = listeArticle;
	}

}
