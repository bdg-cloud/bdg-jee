package fr.legrain.bdg.webapp.documents;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.articles.ArticleController;

@Named
@Dependent
public class OuvertureArticleBean implements Serializable {

//	private static final long serialVersionUID = 12814265716218582L;
	
//
//	@Inject @Named(value="fabricationController")
//	private FabricationController fabricationController;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8329771002065078300L;

	@Inject @Named(value="articleController")
	private ArticleController articleController;	
	

	
	
	@EJB private ITaArticleServiceRemote taArticleService;
	
	private LgrTabEvent event = new LgrTabEvent();
	
	@PostConstruct
	public void init() {
		
	}

	
	
	public void onEventSelect(SelectEvent selectEvent) {
        event = (LgrTabEvent) selectEvent.getObject();
    }

	public void openDocument(ActionEvent e) {
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_ARTICLE)) {
			articleController.setTaArticle(((TaArticle) event.getData()));
			articleController.rempliDTO();
			if(event.isAfficheDoc())articleController.onRowSelect(null);
		}
	}


	public LgrTabEvent getEvent() {
		return event;
	}


	public void setEvent(LgrTabEvent event) {
		this.event = event;
	}




	public ArticleController getArticleController() {
		return articleController;
	}


	public void setArticleController(ArticleController articleController) {
		this.articleController = articleController;
	}

		public void detailArticle(TaArticle detailArticle){
			String tabEcran="";
			if(detailArticle!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_ARTICLE;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailArticle.getCodeArticle());
					getEvent().setData(detailArticle);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}
		

		

		
		
		public TaArticle recupCodeArticle(String code){
			FacesContext context = FacesContext.getCurrentInstance();
			if(code!=null && !code.isEmpty())
				try {
					return taArticleService.findByCode(code);
				} catch (FinderException e) {
					context.addMessage(null, new FacesMessage("erreur", "code article vide")); 	
				}
			return null;
		}
		
		
}
