package fr.legrain.bdg.webapp.articles;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.lib.data.EnumModeObjet;


@Named
@ViewScoped 
public class DuplicationArticleController extends AbstractController{
	
	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;
	
//	@Inject @Named(value="ouvertureDocumentBean")
//	private OuvertureDocumentBean ouvertureDocumentBean;
	
	
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	
	private ParamDuplicationArticle param ;
	private String titreEcran;
    
    private String codeArticle;
    private TaArticle article;
    private TaArticle articleDuplique;
	
	private @EJB ITaArticleServiceRemote taArticleService;
	
	@PostConstruct
	public void init() {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
		}
		param = new ParamDuplicationArticle();
		if(params.get("codeArticle")!=null) {
			try {
				codeArticle=params.get("codeArticle");
				article=taArticleService.findByCode(codeArticle);
				if(article!=null) {
					titreEcran="Duplication de l'article : "+article.getCodeArticle()+" "+article.getLibellecArticle();
				}
				
			} catch (FinderException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	

	
	public void actFermer(Object e) {
		PrimeFaces.current().dialog().closeDynamic(e);
	}
	
	public void actFermer(ActionEvent e) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	public void actAnnuler(ActionEvent e) {
			
		}
	
	
	public void actEnregistrer(ActionEvent e) {
		
	}
	
	
	
	public void actImprimer(ActionEvent e)  throws Exception {		

		if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
			PrimeFaces.current().dialog().closeDynamic(param);
		}		
	}


	
	public ITaPreferencesServiceRemote getPreferencesService() {
		return PreferencesService;
	}


	public void setPreferencesService(ITaPreferencesServiceRemote preferencesService) {
		PreferencesService = preferencesService;
	}





	public TaArticle getArticle() {
		return article;
	}




	public void setArticle(TaArticle article) {
		this.article = article;
	}




	public TaArticle getArticleDuplique() {
		return articleDuplique;
	}




	public void setArticleDuplique(TaArticle articleDuplique) {
		this.articleDuplique = articleDuplique;
	}


	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}




	public ParamDuplicationArticle getParam() {
		return param;
	}




	public void setParam(ParamDuplicationArticle param) {
		this.param = param;
	}




	public String getTitreEcran() {
		return titreEcran;
	}




	public void setTitreEcran(String titreEcran) {
		this.titreEcran = titreEcran;
	}




	public String getCodeArticle() {
		return codeArticle;
	}




	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

}
