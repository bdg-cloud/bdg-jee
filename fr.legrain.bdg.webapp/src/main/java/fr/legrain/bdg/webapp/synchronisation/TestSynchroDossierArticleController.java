package fr.legrain.bdg.webapp.synchronisation;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.synchronisation.dossier.service.remote.ISynchronisationDossierServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;

@Named
@ViewScoped  
public class TestSynchroDossierArticleController extends AbstractController implements Serializable {  
	
	//		<p:outputLabel value="TEST SYNCHRO : #{testSynchroDossierArticleController.refresh()}" />
	/* TODO
	 * -Faire une table avec une liste de dossier "ami"
	 * -Date des dernières synchro, cf principe de prestashop (peut être ajouter table de mapping d'ID ou JSON)
	 * -Pour chacun des dossier "ami" faire une liste de "droit" ou des choses à récupérer
	 * -Dans les dossiers "ami" avoir une liste de dossier "maitre" avec quelle données on autorise
	 * -ressemble beaucoup aux autorisation OAuth, utilisation de OAuth ?
	 */
	
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ISynchronisationDossierServiceRemote synchronisationDossierService;
	
	private String codeDossierCible;
	
	private List<TaArticle> listeArticleDossierCourant;
	private List<TaArticle> listeArticleDossierCible;
	
	
	public TestSynchroDossierArticleController() {  
		
	}  
	
	@PostConstruct
	public void postConstruct(){
		refresh();
	}
	
	public void refresh() {
		codeDossierCible = "demo2";
		listeArticleDossierCourant = taArticleService.selectAll();
		listeArticleDossierCible = synchronisationDossierService.listeArticle(codeDossierCible);
		
		System.out.println("listeArticleDossierCourant : "+listeArticleDossierCourant.size());
		System.out.println("listeArticleDossierCible : "+listeArticleDossierCible.size());
		
		System.out.println(" ===============  listeArticleDossierCourant  ===============");
		for (TaArticle taArticle : listeArticleDossierCourant) {
			System.out.println(taArticle.getCodeArticle()+" "+taArticle.getLibellecArticle());
		}
		System.out.println(" ===============  listeArticleDossierCible  ===============");
		for (TaArticle taArticle : listeArticleDossierCible) {
			System.out.println(taArticle.getCodeArticle()+" "+taArticle.getLibellecArticle());
		}
	}

	public String getCodeDossierCible() {
		return codeDossierCible;
	}

	public void setCodeDossierCible(String codeDossierCible) {
		this.codeDossierCible = codeDossierCible;
	}

	public List<TaArticle> getListeArticleDossierCourant() {
		return listeArticleDossierCourant;
	}

	public void setListeArticleDossierCourant(List<TaArticle> listeArticleDossierCourant) {
		this.listeArticleDossierCourant = listeArticleDossierCourant;
	}

	public List<TaArticle> getListeArticleDossierCible() {
		return listeArticleDossierCible;
	}

	public void setListeArticleDossierCible(List<TaArticle> listeArticleDossierCible) {
		this.listeArticleDossierCible = listeArticleDossierCible;
	}

	
}  
