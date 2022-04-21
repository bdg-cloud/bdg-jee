package fr.legrain.bdg.webapp.recherche.multicritere;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.recherche.multicritere.model.LigneArticle;
import fr.legrain.recherche.multicritere.model.LigneCritere;
import fr.legrain.recherche.multicritere.model.LigneDocument;
import fr.legrain.recherche.multicritere.model.LigneTiers;


@Named
@ViewScoped  
public class RechercheMultiCritereController extends AbstractController{

	private static final long serialVersionUID = -6689548569270524690L;

	@Inject @Named(value="tabListModelCenterBean")
	protected TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	protected TabViewsBean tabViews;
	
	@Inject @New private Instance<RechercheMulticritereJSF> rechercheInstances;
	
//	@EJB private IRechercheMultiCritereServiceRemote rechercheMultiCritereService;
	
	private List<RechercheMulticritereJSF> listeRecherche = new LinkedList<>();
	
	@PostConstruct
	public void init() {
		ajouteRecherche(null);
	}
	
	public void ajouteRecherche() {
		ajouteRecherche(null);
	}
	
	public void ajouteRecherche(RechercheMulticritereJSF modele) {
		RechercheMulticritereJSF r = rechercheInstances.get();
		if(modele!=null) {
			r.setSelectedTypeResultat(modele.getSelectedTypeResultat());
			r.actInitTypeAction();
			r.setSelectedModeUtilisation(modele.getSelectedModeUtilisation());
			r.setSelectedTypeCommunication(modele.getSelectedTypeCommunication());
			r.getListeGroupe().clear();
			if(modele.getListeGroupe()!=null) {
				for (GroupeLigneJSF groupeLigneJSF : modele.getListeGroupe()) {
					GroupeLigneJSF gJSF = new GroupeLigneJSF(groupeLigneJSF.getGroupeLigne().getNumero());
					gJSF.getGroupeLigne().setLibelleGroupe(groupeLigneJSF.getGroupeLigne().getLibelleGroupe());
					gJSF.getGroupeLigne().setSelectedAndOrGroup(groupeLigneJSF.getGroupeLigne().getSelectedAndOrGroup());
					gJSF.getGroupeLigne().setAndOrGroupVisible(groupeLigneJSF.getGroupeLigne().isAndOrGroupVisible());
					gJSF.getGroupeLigne().setLblandOr(groupeLigneJSF.getGroupeLigne().getLblandOr());
					
					LigneCritere newlc = null;
					for (LigneCritere lc : groupeLigneJSF.getGroupeLigne().getGroupe()) {
						if(lc instanceof LigneTiers) {
							newlc = new LigneTiers(gJSF.getGroupeLigne());
						} else if(lc instanceof LigneArticle) {
							newlc = new LigneArticle(gJSF.getGroupeLigne());
						} else if(lc instanceof LigneDocument) {
							newlc = new LigneDocument(gJSF.getGroupeLigne(),((LigneDocument)lc).getType());
							((LigneDocument)newlc).setType(((LigneDocument)lc).getType());
						}
						
						newlc.setType(lc.getType());
						newlc.setBtnDelete(lc.isBtnDelete());
						newlc.setBtnUp(lc.isBtnUp());
						newlc.setBtnDown(lc.isBtnDown());
						
						newlc.setSelectedAndOr(lc.getSelectedAndOr());
						newlc.setSelectedCritere(lc.getSelectedCritere());
						newlc.setSelectedComparaison(lc.getSelectedComparaison());
						newlc.setSelectedBooleen(lc.getSelectedBooleen());
						
						newlc.setValeur1(lc.getValeur1());
						newlc.setValeur2(lc.getValeur2());
						newlc.setWarning(lc.getWarning());
						
						newlc.setValeur1Visible(lc.isValeur1Visible());
						newlc.setValeur1Enabled(lc.isValeur1Enabled());
						newlc.setValeur2Visible(lc.isValeur2Visible());
						newlc.setBooleenVisible(lc.isBooleenVisible());
						newlc.setBooleenEnabled(lc.isBooleenEnabled());
						newlc.setAndOrVisible(lc.isAndOrVisible());
						newlc.setWarningVisible(lc.isWarningVisible());
						newlc.setComparaisonEnabled(lc.isComparaisonEnabled());
						
						gJSF.getGroupeLigne().addLigne(newlc);
					}
					
					r.getListeGroupe().add(gJSF);
				}
			}
		}
		listeRecherche.add(r);
	}
	
	public List<RechercheMulticritereJSF> getListeRecherche() {
		return listeRecherche;
	}

	public void setListeRecherche(List<RechercheMulticritereJSF> listeRecherche) {
		this.listeRecherche = listeRecherche;
	}

}
