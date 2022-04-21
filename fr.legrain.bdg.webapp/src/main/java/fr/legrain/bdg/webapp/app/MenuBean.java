package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.mailjet.client.resource.Email;

import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.webapp.Auth;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.tiers.AbonnementTiersController;
import fr.legrain.bdg.webapp.tiers.TiersController;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.bdg.webapp.articles.ArticleController;
import fr.legrain.bdg.webapp.solstyce.ListeConformiteController;

@Named
@ViewScoped 
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 318974501051997376L;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabListModelLeftBean")
	private TabListModelBean tabsLeft;

	@Inject @Named(value="tabListModelRightBean")
	private TabListModelBean tabsRight;

	@Inject @Named(value="tabListModelBottomBean")
	private TabListModelBean tabsBottom;

	@Inject @Named(value="leftBean")
	private LeftBean leftBean;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	private BdgProperties bdgProperties;
	
//	private TaUtilisateur u = null;

	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationDossierService;
	@EJB private ITaUtilisateurServiceRemote userService;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	private String nomDomaine;
	private String prefixeSousDomaine;
	private String portHttp;
	
	private String cleWidgetAtlassian = null;
	
	@PostConstruct
	public void init() {
//		u = Auth.findUserInSession();
		
//		if(true) {
//			openTableauDeBord(null);
//		}
		
		taParametreService.initParamBdg();
		cleWidgetAtlassian = paramBdg.getTaParametre().getWidgetAtlassianDataKey();
		
		SessionListener.updateSessionKeys();
		Auth auth = null;
		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if(req.getSession().getAttribute("auth")!=null) {
        	auth = (Auth) req.getSession().getAttribute("auth");//session scoped managed bean
        	auth.getSessionInfo().setSessionID(req.getSession(false).getId());
        	//auth.getSessionInfo().setSessionID(((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false)).getId());
        }
        
        bdgProperties = new BdgProperties();
        nomDomaine = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE);
        prefixeSousDomaine = bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE);
        portHttp = bdgProperties.getProperty(BdgProperties.KEY_PORT_HTTP);
        
	}

	private  List<String> listeAutorisation;
	
	
	@Inject @Named(value="articleController")
	private ArticleController articleController;
	
	@Inject @Named(value="tiersController")
	private TiersController tiersController;
	
	@Inject @Named(value="emailController")
	private EmailController emailController;
	
	
	public void openListeArticle(ActionEvent event) {  
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.remove("liste_article_boutique");
		articleController.setParametrageDepuisMenuPrincipal(true);
		articleController.setAfficheSeulementBoutique(false);
		articleController.refresh();
	}
	
	public void openListeArticleBoutique(ActionEvent event) {  
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("liste_article_boutique", true);
//		articleController.refresh();
		articleController.setParametrageDepuisMenuPrincipal(true);
		articleController.setAfficheSeulementBoutique(true);
		articleController.refresh();
	}
	
	public void openListeTiers(ActionEvent event) { 
		tiersController.setParametrageDepuisMenuPrincipal(true);
		tiersController.setAfficheSeulementBoutique(false);
		tiersController.refresh();
	}
	
	public void openListeTiersVisiteurBoutique(ActionEvent event) {  
		tiersController.setParametrageDepuisMenuPrincipal(true);
		tiersController.setAfficheSeulementBoutique(true);
		tiersController.refresh();
	}
	
	public void openTableauDeBord(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.dashboard");
		b.setTitre("Accueil");
		b.setTemplate("dashboard.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TABLEAUBORD);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASHBOARD_ACCUEIL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeBanque(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeBanque");
		b.setTitre("Type Banque");
		b.setTemplate("tiers/type_banque.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_BANQUE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeCivilite(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeCivilite");
		b.setTitre("Civilite, Titre, Qualité");
		b.setTemplate("tiers/type_civilite.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_CIVILITE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeCodeBarre(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeCodeBarre");
		b.setTitre("Type Code Barre ");
		b.setTemplate("articles/type_code_barre.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_CODEBARRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeEntite(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeEntite");
		b.setTitre("Forme Juridique");
		b.setTemplate("tiers/type_entite.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_ENTITE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeTelephone(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeTelephone");
		b.setTitre("Type Telephone");
		b.setTemplate("tiers/type_telephone.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_TEL);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

//	public void openListeTiers(ActionEvent event) { 
//		leftBean.setExpandedForce(true);
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet("fr.legrain.onglet.listeTiers");
//		b.setTitre("Liste tiers");
//		b.setTemplate("tiers/liste_tiers.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_LISTE_TIERS);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
//		tabsLeft.ajouterOnglet(b);
//		tabViews.selectionneOngletLeft(b);
//	}

//	public void openListeArticle(ActionEvent event) {  
//		leftBean.setExpandedForce(true);
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet("fr.legrain.onglet.listeArticle");
//		b.setTitre("Liste article");
//		b.setTemplate("articles/liste_articles.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_LISTE_ARTICLES);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
//		tabsLeft.ajouterOnglet(b);
//		tabViews.selectionneOngletLeft(b);
//	}
	
	public void openCronDossier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_CRON_DOSSIER);
		b.setTitre("Tâches planifiées");
		b.setTemplate("abonnement/cron_dossier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_CRON_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openFamilleUnite(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.FamilleUnite");
		b.setTitre("Famille unite");
		b.setTemplate("articles/famille_unite.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FAMILLE_UNITE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openMarque(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.Marque");
		b.setTitre("Marque article");
		b.setTemplate("articles/marque_article.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MARQUE_ARTICLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	
	public void openUnite(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.Unite");
		b.setTitre("Unités");
		b.setTemplate("articles/unite.xhtml");
		b.setIdTab(LgrTab.ID_TAB_UNITE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openCoefficientUnite(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.coefficientunite");
		b.setTitre("Coefficient unités");
		b.setTemplate("articles/coefficient_unite.xhtml");
		b.setIdTab(LgrTab.ID_TAB_COEFFICIENT_UNITE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openCreationFabrication(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.creationFabrication");
		b.setTitre("Fabrication");
		b.setTemplate("solstyce/fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FABRICATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openDashBoardFabrication(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_FABRICATION);
		b.setTitre("Tableau de bord des fabrications");
		b.setTemplate("dashboard/dashboard_fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
//	public void openDashBoardDevis(ActionEvent event) {  
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_DEVIS);
//		b.setTitre("TB Devis");
//		b.setTemplate("dashboard/dashboard_devis.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_DASH_DEVIS);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);
//	}
	
	public void openCreationModeleFabrication(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.creationModeleFabrication");
		b.setTitre("Modèle Fabrication");
		b.setTemplate("solstyce/modele_fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MODELE_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MODELE_FABRICATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openEntrepot(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeEntrepot");
		b.setTitre("Entrepot");
		b.setTemplate("articles/entrepot.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ENTREPOT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeFabrication(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeFabrication");
		b.setTitre("Type fabrication");
		b.setTemplate("articles/type_fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeEditionCatalogue(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeEditionCatalogue");
		b.setTitre("Type édition catalogue");
		b.setTemplate("admin_legrain/type_edition_catalogue.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_EDITION_CATALOGUE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeEditionDossier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeEditionDossier");
		b.setTitre("Type édition dossier");
		b.setTemplate("admin/type_edition_dossier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_EDITION_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeReception(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeReception");
		b.setTitre("Type réception");
		b.setTemplate("articles/type_reception.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_RECEPTION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeNoteT(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeNoteT");
		b.setTitre("Type Note");
		b.setTemplate("tiers/type_note_tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_NOTE_TIERS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openTypeTiers(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeTiers");
		b.setTitre("Type Tiers");
		b.setTemplate("tiers/type_tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_TIERS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeWeb(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TYPE_WEB);
		b.setTitre("Type de site Web");
		b.setTemplate("tiers/type_web.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_WEB);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openNiveauRelance(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_NIVEAU_RELANCE);
		b.setTitre("Niveau de relance");
		b.setTemplate("relance/niveau_relance.xhtml");
		b.setIdTab(LgrTab.ID_TAB_NIVEAU_RELANCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeTarif(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeTarif");
		b.setTitre("Grille tarifaire");
		b.setTemplate("tiers/type_tarif.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_TARIF);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeEmail(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeEmail");
		b.setTitre("Type Email");
		b.setTemplate("tiers/type_email.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_EMAIL);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeLiens(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeLiens");
		b.setTitre("Type Liens");
		b.setTemplate("tiers/type_liens.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_LIENS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openFamilleTiers(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeFamilleTiers");
		b.setTitre("Famille Tiers");
		b.setTemplate("tiers/famille_tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FAMILLE_TIERS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openConditionPaiementTiers(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeConditionPaiementTiers");
		b.setTitre("Condition paiement Tiers");
		b.setTemplate("tiers/condition_paiement_tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_CONDITIONPAIEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openConditionPaiementDoc(ActionEvent event) {  //Paiement - Echéance
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeConditionPaiementDoc");
		b.setTitre("Paiement - Echéance");
		b.setTemplate("tiers/condition_paiement_doc.xhtml");
		b.setIdTab(LgrTab.ID_TAB_CONDITIONPAIEMENT_DOC);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeEvenement(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.typeevenement");
		b.setTitre("Type évènement");
		b.setTemplate("agenda/type_evenement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_EVENEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openAgenda(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.agenda");
		b.setTitre("Agenda");
		b.setTemplate("agenda/agenda.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETIQUETTE_EMAIL);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openRechercheParCriteres(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.recherche.multicritere");
		b.setTitre("Recherche par critères");
		b.setTemplate("recherche/multicritere/recherche_multicritere.xhtml");
		b.setIdTab(LgrTab.ID_TAB_RECHERCHE_MULTICRITERE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_RECHERCHE_MULTICRITERE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openEtiquetteEmail(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.etiquette.email");
		b.setTitre("Etiquette email");
		b.setTemplate("admin/etiquette_message_email.xhtml");
		b.setIdTab(LgrTab.ID_TAB_AGENDA);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openEditionDuDossier(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.edition.dossier.utilisateur");
		b.setTitre("Edition du dossier");
		b.setTemplate("admin/edition_du_dossier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EDITION_DU_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionActionInterneProg(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.action.interne.prog");
		b.setTitre("Action interne");
		b.setTemplate("admin_legrain/gestion_action_interne_prog.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_ACTION_INTERNE_PROG);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openEditionDossier(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.edition.dossier");
		b.setTitre("Edition dossier");
		b.setTemplate("admin/edition_dossier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EDITION_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EDITION_DOSSIER);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionCatalogueEdition(ActionEvent event) { 
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.edition.catalogue");
		b.setTitre("Gestion catalogue edition");
		b.setTemplate("admin_legrain/edition_catalogue.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EDITION_CATALOGUE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EDITION_CATALOGUE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeNoteA(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeNoteA");
		b.setTitre("Type Note");
		b.setTemplate("articles/type_note_articles.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_NOTE_ARTICLES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTypeAdresse(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeTypeAdresse");
		b.setTitre("Type Adresse");
		b.setTemplate("tiers/type_adresse.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_ADRESSE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openFamilleArticle(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeFamilleArticle");
		b.setTitre("Famille Article");
		b.setTemplate("articles/famille_articles.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FAMILLE_ARTICLES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openTva(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeCodeTva");
		b.setTitre("Code Tva");
		b.setTemplate("articles/type_tva.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_TVA);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openPartenariat(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.partenariat");
		b.setTitre("Partenariat");
		b.setTemplate("admin/partenariat.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PARTENARIAT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openLogDossier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.log.dossier");
		b.setTitre("Logs dossier");
		b.setTemplate("admin/log_dossier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LOGS_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openUtilisateur(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeUtilisateur");
		b.setTitre("Utilisateurs");
		b.setTemplate("admin/utilisateurs.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_UTILISATEURS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openUtilisateurWebService(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeUtilisateurWebService");
		b.setTitre("Utilisateurs WS");
		b.setTemplate("admin/utilisateurs_web_services.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_UTILISATEURS_WEB_SERVICES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openUtilisateurLogin(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeUtilisateurLogin");
		b.setTitre("Utilisateurs Login");
		b.setTemplate("admin/dev/utilisateurs_login.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_UTILISATEURS_LOGIN);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openRole(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeRole");
		b.setTitre("Roles");
		b.setTemplate("admin/roles.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_ROLES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openMonAgenda(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.monagenda");
		b.setTitre("Agenda");
		b.setTemplate("agenda/mon_agenda.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MON_AGENDA);
		b.setStyle(LgrTab.CSS_CLASS_TAB_AGENDA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionOAuthDialog(ActionEvent event) {  
	       Map<String,Object> options = new HashMap<String, Object>();
	        options.put("modal", true);
	        options.put("draggable", false);
	        options.put("closable", false);
	        options.put("resizable", true);
	        options.put("contentHeight", 710);
	        options.put("contentWidth", "100%");
	        //options.put("contentHeight", "100%");
	        options.put("width", 1024);
	        
	        //Map<String,List<String>> params = null;
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
//	        List<String> list = new ArrayList<String>();
//	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//	        params.put("modeEcranDefaut", list);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
//			EmailParam email = new EmailParam();
//			email.setEmailExpediteur(null);
//			email.setDestinataires(new String[]{taTiers.getTaEmail().getAdresseEmail()});
//			email.setEmailDestinataires(new TaEmail[]{taTiers.getTaEmail()});
			
//			sessionMap.put(EmailParam.NOM_OBJET_EN_SESSION, email);
	        
	        PrimeFaces.current().dialog().openDynamic("/dialog_gestion_oauth", options, params);    
		}
		
		public void handleReturnGestionOAuthDialog(SelectEvent event) {
//			if(event!=null && event.getObject()!=null) {
//				TaMessageEmail j = (TaMessageEmail) event.getObject();
//				
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Email", "Email envoyée ")); 
//			}
		}

	public void openPreferences(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.preferences");
		b.setTitre("Préférences");
		b.setTemplate("admin/preferences.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_PREFERENCES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MON_AGENDA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openBugs(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.bugs");
		b.setTitre("Bugs");
		b.setTemplate("admin/bugs.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_BUGS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openVerrouillageManuel(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.verrouillageManuel");
		b.setTitre("Verrouillage manuel");
		b.setTemplate("verrouillage/verrouillage_manuel.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_VERROUILLAGE_MANUEL);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openChangelog(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.changelog");
		b.setTitre("Changelog");
		b.setTemplate("admin/changelog.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_CHANGELOG);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openSvg(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.svg");
		b.setTitre("Sauvegarde/Restauration");
		b.setTemplate("admin/svg.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_SVG);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openMajDossierDiverses(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.maj.diverses.dossier");
		b.setTitre("Mise à jour diverses des dossiers");
		b.setTemplate("admin/dev/maj_dossier_diverses.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_MAJ_DIVERSES_DOSSIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	
	
	public void openMajBdd(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.majbdd");
		b.setTitre("Mise à jour de la base de données");
		b.setTemplate("admin/majbdd.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ADMIN_MAJBDD);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openListeFabrication(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeFabrication");
		b.setTitre("Liste fabrications");
		b.setTemplate("solstyce/liste_fabrications.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FABRICATION);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openListeModeleFabrication(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeModeleFabrication");
		b.setTitre("Liste modèle fabrications");
		b.setTemplate("solstyce/liste_modele_fabrications.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_MODELE_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MODELE_FABRICATION);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}

	public void openListeReception(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeReception");
		b.setTitre("Liste des réceptions");
		b.setTemplate("documents/liste_bon_reception.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_BR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_RECEPTION);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}

	public void openBonReception(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BON_RECEPTION);
		b.setTitre("Bon de réception");
		b.setTemplate("documents/bon_reception.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BON_RECEPTION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_RECEPTION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	
	public void openListeAvoir(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeAvoir");
		b.setTitre("Liste des avoirs");
		b.setTemplate("documents/liste_avoir.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_AVOIR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_AVOIR);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openListeDevis(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeDevis");
		b.setTitre("Liste des devis");
		b.setTemplate("documents/liste_devis.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_DEVIS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DEVIS);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openAbonnement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ABONNEMENT);
		b.setTitre("Abonnement");
		b.setTemplate("abonnement/abonnement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ABONNEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ABONNEMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openDevis(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DEVIS);
		b.setTitre("Devis");
		b.setTemplate("documents/devis.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DEVIS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DEVIS);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openDashBoardAbonnement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_ABONNEMENT);
		b.setTitre("TB Abonnements");
		b.setTemplate("dashboard/dashboard_abonnement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_ABONNEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openDashBoardDevis(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_DEVIS);
		b.setTitre("TB Devis");
		b.setTemplate("dashboard/dashboard_devis.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_DEVIS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardTiers(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_TIERS);
		b.setTitre("TB Tiers");
		b.setTemplate("dashboard/dashboard_tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_TIERS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardArticle(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_ARTICLE);
		b.setTitre("TB Articles");
		b.setTemplate("dashboard/dashboard_article.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_ARTICLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardReception(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_RECEPTION);
		b.setTitre("TB Réceptions");
		b.setTemplate("dashboard/dashboard_reception.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_RECEPTION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	

	public void openListeFacture(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeFacture");
		b.setTitre("Liste des factures");
		b.setTemplate("documents/liste_facture.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_FACTURE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FACTURE);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}
	
	public void openCaisseComplete(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TICKET_DE_CAISSE);
		b.setTitre("Ticket de caisse");
		b.setTemplate("documents/ticket_caisse.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TICKET_DE_CAISSE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_TICKET_DE_CAISSE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openSessionCaisse(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TICKET_DE_CAISSE_SESSION);
		b.setTitre("Session de caisse");
		b.setTemplate("documents/ticket_caisse_session.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TICKET_DE_CAISSE_SESSION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_TICKET_DE_CAISSE_SESSION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openFacture(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FACTURE);
		b.setTitre("Facture");
		b.setTemplate("documents/facture.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FACTURE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FACTURE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardFacture(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_FACTURE);
		b.setTitre("TB Facture");
		b.setTemplate("dashboard/dashboard_facture.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_FACTURE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	
	public void openDashBoardFactureAvecEtat(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_FACTURE_AVEC_ETAT);
		b.setTitre("TB Facture");
		b.setTemplate("dashboard/dashboard_facture_avec_etat.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_FACTURE_AVEC_ETAT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	
	public void openDashBoardReglement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_REGLEMENT);
		b.setTitre("TB Reglement");
		b.setTemplate("dashboard/dashboard_reglement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_REGLEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openBonLivraison(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BON_LIVRAISON);
		b.setTitre("Bon de livraison");
		b.setTemplate("documents/bon_livraison.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BON_LIVRAISON);
		b.setStyle(LgrTab.CSS_CLASS_TAB_LIVRAISON);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardBonliv(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_BONLIV);
		b.setTitre("TB livraison");
		b.setTemplate("dashboard/dashboard_bon_liv.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_BONLIV);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGrilleReference(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GRILLE_REFERENCE_MT);
		b.setTitre("Grille de référence");
		b.setTemplate("multi_tarifs/grille_reference.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GRILLE_REFERENCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GRILLE_REFERENCE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGrilleTarifaire(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GRILLE_TARIFAIRE_MT);
		b.setTitre("Grille tarifaire");
		b.setTemplate("multi_tarifs/grille_tarifaire.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GRILLE_TARIFAIRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GRILLE_TARIFAIRE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openRemplacementGrilleTarifaireTiers(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_REMP_GRILLE_TARIFAIRE_TIERS_MT);
		b.setTitre("Remplacement grille tarifaire tiers");
		b.setTemplate("multi_tarifs/remplacement_grille_tarifaire_tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REMP_GRILLE_TARIFAIRE_TIERS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GRILLE_TARIFAIRE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openAvoir(ActionEvent event) {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_AVOIR);
		b.setTitre("Facture d'avoir");
		b.setTemplate("documents/avoir.xhtml");
		b.setIdTab(LgrTab.ID_TAB_AVOIR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_AVOIR);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);	
	}

	public void openDashBoardAvoir(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_AVOIR);
		b.setTitre("TB Avoir");
		b.setTemplate("dashboard/dashboard_avoir.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_AVOIR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openBoncde(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BONCDE);
		b.setTitre("Bon de commande");
		b.setTemplate("documents/boncde.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BONCDE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_BONCDE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	

	public void openBonPreparation(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_PREPARATION);
		b.setTitre("Bon de préparation");
		b.setTemplate("documents/preparation.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PREPARATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PREPARATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openPanier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_PANIER);
		b.setTitre("Panier");
		b.setTemplate("documents/panier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PANIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PANIER);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardPanier(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_PANIER);
		b.setTitre("TB Panier");
		b.setTemplate("dashboard/dashboard_panier.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_PANIER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	
	
	public void openBoncdeAchat(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BONCDE_ACHAT);
		b.setTitre("Bon de commande Achat");
		b.setTemplate("documents/boncdeAchat.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BONCDE_ACHAT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_BONCDE_ACHAT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardBoncde(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_BONCDE);
		b.setTitre("TB commande");
		b.setTemplate("dashboard/dashboard_bon_cde.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_BONCDE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardBoncdeAchat(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_BONCDE_ACHAT);
		b.setTitre("TB Commande achat");
		b.setTemplate("dashboard/dashboard_bon_cde_achat.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_BONCDE_ACHAT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}	
	public void openAcompte(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ACOMPTE);
		b.setTitre("Acompte");
		b.setTemplate("documents/acompte.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ACOMPTE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ACOMPTE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardAcompte(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_ACOMPTE);
		b.setTitre("TB acompte");
		b.setTemplate("dashboard/dashboard_acompte.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_ACOMPTE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardApporteur(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_APPORTEUR);
		b.setTitre("TB apporteur");
		b.setTemplate("dashboard/dashboard_apporteur.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_APPORTEUR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openApporteur(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_APPORTEUR);
		b.setTitre("Facture apporteur");
		b.setTemplate("documents/apporteur.xhtml");
		b.setIdTab(LgrTab.ID_TAB_APPORTEUR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_APPORTEUR);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openProforma(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_PROFORMA);
		b.setTitre("Facture proforma");
		b.setTemplate("documents/proforma.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PROFORMA);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PROFORMA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardProforma(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_PROFORMA);
		b.setTitre("TB Proforma");
		b.setTemplate("dashboard/dashboard_proforma.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_PROFORMA);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openAvisEcheance(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_AVIS_ECHEANCE);
		b.setTitre("Avis d'échéance");
		b.setTemplate("documents/avis_echeance.xhtml");
		b.setIdTab(LgrTab.ID_TAB_AVIS_ECHEANCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_AVIS_ECHEANCE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openDashBoardAvisEcheance(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_AVIS_ECHEANCE);
		b.setTitre("TB Avis Echéance");
		b.setTemplate("dashboard/dashboard_avis_echeance.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_AVIS_ECHEANCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openPrelevement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_PRELEVEMENT);
		b.setTitre("Prélèvement");
		b.setTemplate("documents/prelevement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PRELEVEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PRELEVEMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openDashBoardPrelevement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_DASH_PRELEVEMENT);
		b.setTitre("TB Prélèvement");
		b.setTemplate("dashboard/dashboard_prelevement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_DASH_PRELEVEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DASH_ALL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}	
	public void openGenerationDocumentMultiple(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GENERATION_DOCUMENT_MULTIPLE);
		b.setTitre("Génération documents multiple");
		b.setTemplate("generation/generation_documents_multiple.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GENERATION_DOCUMENT_MULTIPLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GENERATION_DOCUMENT_MULTIPLE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGenerationLigneALigneMultiple(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE);
		b.setTitre("Génération ligne à ligne multiple");
		b.setTemplate("generation/generation_ligne_a_ligne.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGenerationLALBcaBrMultiple(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GENERATION_LALBCABR);
		b.setTitre("Réception des commandes fournisseurs");
		b.setTemplate("generation/generation_bca_br.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GENERATION_LIGNE_A_LIGNE_BCDEACHAT_BR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openImportation(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_IMPORTATION);
		b.setTitre("Importation");
		b.setTemplate("importation/importations.xhtml");
		b.setIdTab(LgrTab.ID_TAB_IMPORTATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_IMPORTATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTunnelVenteRenouvellement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TUNNEL_VENTE_RENOUVELLEMENT);
		b.setTitre("Renouvellement BDG (BETA)");
		b.setTemplate("tunnelVente/tunnel_vente_renouvellement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TUNNEL_VENTE_RENOUVELLEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_TUNNEL_VENTE_RENOUVELLEMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openLiaisonDossierMaitre(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_LIAISON_DOSSIER_MAITRE);
		b.setTitre("Espace client");
		b.setTemplate("liaisonDossierMaitre/liaison_dossier_maitre.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LIAISON_DOSSIER_MAITRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_LIAISON_DOSSIER_MAITRE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openCreationEspaceClient(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_CREATION_ESPACE_CLIENT);
		b.setTitre("Création compte espace client");
		b.setTemplate("liaisonDossierMaitre/creation_espace_client.xhtml");
		b.setIdTab(LgrTab.ID_TAB_CREATION_ESPACE_CLIENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_CREATION_ESPACE_CLIENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openSynchronisationShippingBo(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_SYNCHRONISATION_SHIPPINGBO);
		b.setTitre("Synchonisation ShippingBo");
		b.setTemplate("synchronisation/sync_shippingbo.xhtml");
		b.setIdTab(LgrTab.ID_TAB_SYNCHRONISATION_SHIPPINGBO);
		b.setStyle(LgrTab.CSS_CLASS_TAB_IMPORTATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openSynchronisationWooCommerce(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_SYNCHRONISATION_WOOCOMMERCE);
		b.setTitre("Synchonisation WooCommerce");
		b.setTemplate("synchronisation/sync_woocommerce.xhtml");
		b.setIdTab(LgrTab.ID_TAB_SYNCHRONISATION_WOOCOMMERCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_IMPORTATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openSynchronisationPrestashop(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_SYNCHRONISATION_PRESTASHOP);
		b.setTitre("Synchonisation Prestashop");
		b.setTemplate("synchronisation/sync_prestashop.xhtml");
		b.setIdTab(LgrTab.ID_TAB_SYNCHRONISATION_PRESTASHOP);
		b.setStyle(LgrTab.CSS_CLASS_TAB_IMPORTATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openSynchronisationMensura(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_SYNCHRONISATION_MENSURA);
		b.setTitre("Synchonisation Mensura");
		b.setTemplate("synchronisation/sync_mensura.xhtml");
		b.setIdTab(LgrTab.ID_TAB_SYNCHRONISATION_MENSURA);
		b.setStyle(LgrTab.CSS_CLASS_TAB_IMPORTATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	
	
	public void openExportComptaEpicea(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EXPORT_COMPTA_EPICEA);
		b.setTitre("Exportation Epicéa");
		b.setTemplate("export/epicea/export_compta.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EXPORT_COMPTA_EPICEA);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EXPORT_COMPTA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openExportComptaIsagri1(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EXPORT_COMPTA_ISAGRI1);
		b.setTitre("Exportation Isagri 1");
		b.setTemplate("export/isagri/export_compta_isagri.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EXPORT_COMPTA_ISAGRI1);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EXPORT_COMPTA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openExportComptaIsagri2(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EXPORT_COMPTA_ISAGRI2);
		b.setTitre("Exportation Isagri 2");
		b.setTemplate("export/isagri/export_compta_isagri2.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EXPORT_COMPTA_ISAGRI2);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EXPORT_COMPTA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openExportComptaIsagriFinal(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EXPORT_COMPTA_ISAGRIFINAL);
		b.setTitre("Exportation Isagri");
		b.setTemplate("export/isagri/export_compta_isagriFinal.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EXPORT_COMPTA_ISAGRIFINAL);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EXPORT_COMPTA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openExportComptaAgrigest(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EXPORT_COMPTA_AGRIGEST);
		b.setTitre("Exportation Agrigest");
		b.setTemplate("export/agrigest/export_compta_agrigest.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EXPORT_COMPTA_AGRIGEST);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EXPORT_COMPTA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openExportComptaCegid(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EXPORT_COMPTA_CEGID);
		b.setTitre("Exportation Cegid");
		b.setTemplate("export/cegid/export_compta_cegid.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EXPORT_COMPTA_CEGID);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EXPORT_COMPTA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	
	public void openExportComptaSage(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EXPORT_COMPTA_SAGE);
		b.setTitre("Exportation Sage");
		b.setTemplate("export/sage/export_compta_sage.xhtml");
		b.setIdTab(LgrTab.ID_TAB_EXPORT_COMPTA_SAGE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EXPORT_COMPTA);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openListeMouvements(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeMouvement");
		b.setTitre("Liste des groupes de mouvements");
		b.setTemplate("stock/liste_mouvement_stock.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_MOUVEMENTS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_STOCK);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}


	public void openMouvementStock(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_STOCK);
		b.setTitre("Mouvements de stocks");
		b.setTemplate("stock/mouvement_stock.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MOUVEMENT_STOCK);
		b.setStyle(LgrTab.CSS_CLASS_TAB_STOCK);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openMouvementInventaire(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_INVENTAIRE);
		b.setTitre("Inventaire");
		b.setTemplate("stock/inventaire.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MOUVEMENT_INVENTAIRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_INVENTAIRE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openListeInventaires(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeInventaire");
		b.setTitre("Liste des inventaires");
		b.setTemplate("stock/liste_inventaire.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_INVENTAIRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_INVENTAIRE);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}


	public void openGestionDms(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.gestionDms");
		b.setTitre("Gestion des drm");
		b.setTemplate("gestionDms/etat_dms.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_DMS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_DMS);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openGestionCapsules(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.gestionCapsules");
		b.setTitre("Gestion des capsules");
		b.setTemplate("gestionCapsules/gestion_capsules.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_CAPSULES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_CAPSULES);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionLot(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.gestionLot");
		b.setTitre("Gestion des lots");
		b.setTemplate("articles/gestion_lot.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_LOT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_LOTS);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionAbonnement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.gestionAbonnement");
		b.setTitre("Gestion des abonnements");
		b.setTemplate("abonnement/gestion_abonnement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_ABONNEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ABONNEMENTS);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionLiaisonExterne(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.gestionLiaisonExterne");
		b.setTitre("Gestion des liaisons externe");
		b.setTemplate("synchronisation/gestion_liaison_externe.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_LIAISON_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_LIAISON_EXTERNE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionPaiementInternet(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.gestion.paiement.internet");
		b.setTitre("Gestion des paiements internet");
		b.setTemplate("admin/gestion_paiement_internet.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_PAIEMENT_INTERNET);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PAIEMENT_INTERNET);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionEmailEnvoyes(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_EMAILS_ENVOYES);
		b.setTitre("Gestion des emails envoyés");
		b.setTemplate("/gestion_emails_envoyes.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_EMAILS_ENVOYES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_EMAILS_ENVOYES);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		
		emailController.refresh();
	}
	
	public void openEtatNonConformite(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.etatNonConformite");
		b.setTitre("Résultat des controles de conformités");
		b.setTemplate("etats/etat_non_conformite.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETAT_NON_CONFORMITE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ETAT_NON_CONFORMITE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openEtatStocks(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.etatStocks");
		b.setTitre("Etat des stocks");
		b.setTemplate("etats/etat_stocks.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETAT_STOCKS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ETAT_STOCKS);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openEtatFournisseursProduit(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.etatFournisseursProduit");
		b.setTitre("Fournisseurs pour un article");
		b.setTemplate("etats/etat_fournisseurs.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETAT_FOURNISSEURS_PRODUITS);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ETAT_FOURNISSEURS_PRODUITS);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openEtatProduitsFournisseur(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.etatProduitsFournisseur");
		b.setTitre("Articles d'un fournisseur");
		b.setTemplate("etats/etat_produits_fournisseur.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETAT_PRODUITS_FOURNISSEUR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ETAT_PRODUITS_FOURNISSEUR);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}	
	public void openTypePaiement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.typePaiement");
		b.setTitre("Type Paiement");
		b.setTemplate("articles/type_paiement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_PAIEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openEtatDocument(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.etatDocument");
		b.setTitre("Etat Document");
		b.setTemplate("articles/etat_document.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETAT_DOCUMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeTransport(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.typeTransport");
		b.setTitre("Type de transport");
		b.setTemplate("articles/type_transport.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_TRANSPORT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTransporteur(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.transporteur");
		b.setTitre("Transporteur");
		b.setTemplate("articles/transporteur.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TRANSPORTEUR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openLabelArticle(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.labetArticle");
		b.setTitre("Type de certification");
		b.setTemplate("articles/label_article.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LABEL_ARTICLES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTitreTransport(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.titreTransport");
		b.setTitre("Titre de transport");
		b.setTemplate("articles/type_crd.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TITRE_TRANSPORT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	
	public void openCategorieArticle(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.categorieArticle");
		b.setTitre("Catégorie article");
		b.setTemplate("articles/categorie_article.xhtml");
		b.setIdTab(LgrTab.ID_TAB_CATEGORIE_ARTICLES);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openEtatTracabiliteFabrication(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.etatTracabiliteFabrication");
		//b.setTitre("Etat Traçabilité Fabrication");
		b.setTitre("Etats Solstyce");
		b.setTemplate("solstyce/etat_tracabilite_fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETAT_TRACABILITE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	

	public void openCodeBarre(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.codeBarre");
		b.setTitre("Code barre");
		b.setTemplate("articles/code_barre.xhtml");
		b.setIdTab(LgrTab.ID_TAB_CODEBARRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openInfoEntreprise(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.infoEntreprise");
		b.setTitre("Infos entreprise");
		b.setTemplate("tiers/info_entreprise.xhtml");
		b.setIdTab(LgrTab.ID_TAB_INFOENTREPRISE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_INFOS_ENTREPRISE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}	

	public void openModeleControle(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeModeleControle");
		b.setTitre("Liste modèle de controle");
		//b.setTemplate("solstyce/modele_controle.xhtml");
		b.setTemplate("solstyce/liste_modele_controle.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_MODELE_CONTROLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MODELE_CONTROLE);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}

	//	public void openModeleBareme(ActionEvent event) {  
	//		LgrTab b = new LgrTab();
	//		b.setTypeOnglet("fr.legrain.onglet.modeleBareme");
	//		b.setTitre("Modèle de barème");
	//		b.setTemplate("solstyce/modele_bareme.xhtml");
	//		b.setStyle(LgrTab.CSS_CLASS_TAB_CONTROLE_CONFORMITE);
	//				tabsCenter.ajouterOnglet(b);
	//    }

	public void openGroupeControle(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.groupeControle");
		b.setTitre("Groupe de controle");
		b.setTemplate("solstyce/groupe_controle.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GROUPE_CONTROLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_CONTROLE_CONFORMITE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public void openModeleGroupeControle(ActionEvent event) {  
		leftBean.setExpandedForce(true);
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.listeModeleGroupeControle");
		b.setTitre("Liste modèle de groupe de controle");
		b.setTemplate("solstyce/liste_modele_groupe_controle.xhtml");
		b.setIdTab(LgrTab.ID_TAB_LISTE_MODELE_GROUPE_CONTROLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MODELE_GROUPE_CONTROLE);
		tabsLeft.ajouterOnglet(b);
		tabViews.selectionneOngletLeft(b);
	}

	public void openGenCodeEx(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.genCodeEx");
		b.setTitre("Gestion génération des codes");
		b.setTemplate("admin/dev/gencode_extended.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GENCODEEX);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openGestionCompteEspaceClient(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.gestion.compteclient");
		b.setTitre("Gestion des comptes espace client");
		b.setTemplate("admin/gestion_compte_client.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_COMPTE_CLIENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openParametreEspaceClient(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.parametre.compteclient");
		b.setTitre("Parametre des espaces clients");
		b.setTemplate("admin/parametre_compte_client.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PARAMETRE_COMPTE_CLIENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openImprimerDocument(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_IMPRIMER_DOCUMENT);
		b.setTitre("Impression document");
		b.setTemplate("documents/imprimer_document.xhtml");
		b.setIdTab(LgrTab.ID_TAB_IMPRIMER_DOCUMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_IMPRIMER_DOCUMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openReglementMultiple(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GESTION_REGLEMENT);
		b.setTitre("Règler une facture");
		b.setTemplate("reglement/multiple/selection_doc_nonReglee.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GESTION_REGLEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GESTION_REGLEMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openReglement(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_REGLEMENT);
		b.setTitre("Gestion des réglements");
		b.setTemplate("reglement/multiple/reglement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REGLEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_REGLEMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}	
		public void openTypeAuthentification(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.type.authentification");
		b.setTitre("Type d'authentification");
		b.setTemplate("admin/type_authentification.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_AUTHENTIFICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openTypeServiceWebExterne(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.type.servicewebexterne");
		b.setTitre("Type de service web externe");
		b.setTemplate("admin/type_servicewebexterne.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_SERVICE_WEB_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openServiceWebExterne(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.servicewebexterne");
		b.setTitre("Service web externe");
		b.setTemplate("admin/service_web_externe.xhtml");
		b.setIdTab(LgrTab.ID_TAB_SERVICE_WEB_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openCompteServiceWebExterne(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.compte.servicewebexterne");
		b.setTitre("Compte service web externe");
		b.setTemplate("admin/compte_service_web_externe.xhtml");
		b.setIdTab(LgrTab.ID_TAB_COMPTE_SERVICE_WEB_EXTERNE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	
	public void openRemise(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_REMISE);
		b.setTitre("Gestion des remises");
		b.setTemplate("remise/remise.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REMISE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_REMISE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openRelance(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_RELANCE);
		b.setTitre("Gestion des relances");
		b.setTemplate("relance/relance.xhtml");
		b.setIdTab(LgrTab.ID_TAB_RELANCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_RELANCE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public void openEtatDoc(ActionEvent event) {  //Paiement - Echéance
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.EtatDoc");
		b.setTitre("Etat documents");
		b.setTemplate("tiers/type_etat.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ETAT_DOCUMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ETAT_DOCUMENT);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	

	
	public void openGandiSimpleHosting(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GANDI_SIMPLE_HOSTING);
		b.setTitre("Gandi Simple Hosting");
		b.setTemplate("admin/dev/gandi_simple_hosting.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GANDI_SIMPLE_HOSTING);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GANDI_SIMPLE_HOSTING);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openGandiServer(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GANDI_SERVER);
		b.setTitre("Gandi Server");
		b.setTemplate("admin/dev/gandi_server.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GANDI_SERVER);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GANDI_SERVER);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openGandiDomain(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GANDI_DOMAIN);
		b.setTitre("Gandi Domaine");
		b.setTemplate("admin/dev/gandi_domain.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GANDI_DOMAIN);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GANDI_DOMAIN);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	public void openGandiCertifSSL(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GANDI_CERTIF_SSL);
		b.setTitre("Gandi Certif SSL");
		b.setTemplate("admin/dev/gandi_certif_ssl.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GANDI_CERTIF_SSL);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GANDI_CERTIF_SSL);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}
	
	public TabListModelBean getTabsLeft() {
		return tabsLeft;
	}

	public void setTabsLeft(TabListModelBean tabsLeft) {
		this.tabsLeft = tabsLeft;
	}

	public TabListModelBean getTabsRight() {
		return tabsRight;
	}

	public void setTabsRight(TabListModelBean tabsRight) {
		this.tabsRight = tabsRight;
	}

	public TabListModelBean getTabsBottom() {
		return tabsBottom;
	}

	public void setTabsBottom(TabListModelBean tabsBottom) {
		this.tabsBottom = tabsBottom;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}
	public LeftBean getLeftBean() {
		return leftBean;
	}
	public void setLeftBean(LeftBean leftBean) {
		this.leftBean = leftBean;
	}  

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public String getNomDomaine() {
		return nomDomaine;
	}

	public String getPrefixeSousDomaine() {
		return prefixeSousDomaine;
	}

	public String getPortHttp() {
		return portHttp;
	}

	public void openGenerationLALFlashPrepaMultiple(ActionEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GENERATION_LALFLASHPREPA);
		b.setTitre("Préparation des commandes scannées");
		b.setTemplate("generation/generation_flash_preparation.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GENERATION_LIGNE_A_LIGNE_FLASH_PREPARATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GENERATION_LIGNE_A_LIGNE_MULTIPLE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
	}

	public ArticleController getArticleController() {
		return articleController;
	}

	public void setArticleController(ArticleController articleController) {
		this.articleController = articleController;
	}

	public TiersController getTiersController() {
		return tiersController;
	}

	public void setTiersController(TiersController tiersController) {
		this.tiersController = tiersController;
	}

	public EmailController getEmailController() {
		return emailController;
	}

	public void setEmailController(EmailController emailController) {
		this.emailController = emailController;
	}

	public String getCleWidgetAtlassian() {
		return cleWidgetAtlassian;
	}
	
}
