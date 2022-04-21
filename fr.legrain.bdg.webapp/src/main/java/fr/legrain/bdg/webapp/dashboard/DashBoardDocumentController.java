package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.swing.event.ListDataEvent;

import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.mindmap.MindmapNode;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.lib.data.InfosJours;
import fr.legrain.lib.data.InfosMois;
import fr.legrain.lib.data.LibDate;

//@Named
//@ViewScoped
public abstract class DashBoardDocumentController extends AbstractController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7804116227656933088L;
	
	protected static final String CSS_DBOARD_BLOC_INACTIF_ALL = "whiteback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_FACTURE = "factureback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_AVOIR = "avoirback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_BONCDE = "bdcback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_ARTICLE = "blueback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_TIERS = "greenback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_DEVIS = "redback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_PROFORMA = "proformaback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_BONLIV = "bonlivback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_ACOMPTE = "acompteback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_APPORTEUR = "apporteurback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_REGLEMENT = "reglementback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_REMISE = "remiseback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_PRELEVEMENT = "prelback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_RECEPTION = "receptionback";
	
	protected static final String CSS_DBOARD_BLOC_INACTIF_ALL_COMP = "grisback";
	protected static final String CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP = "facturebackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_AVOIR_COMP = "avoirbackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_BONCDE_COMP = "bdcbackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_ARTICLE_COMP = "bluebackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_TIERS_COMP = "greenbackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_DEVIS_COMP = "redbackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_PROFORMA_COMP = "proformabackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_BONLIV_COMP = "bonlivbackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_ACOMPTE_COMP = "acomptebackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_APPORTEUR_COMP = "apporteurbackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_REGLEMENT_COMP = "reglementbackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_REMISE_COMP = "remisebackcomp";
	protected static final String CSS_DBOARD_BLOC_ACTIF_PRELEVEMENT_COMP = "prelbackcomp";

	protected static final String ETAT_DOCUMENT_AUCUN = "aucun";
	protected static final String ETAT_DOCUMENT_TOUS = "tous";
	protected static final String ETAT_DOCUMENT_TRANSORME = "transforme";
	protected static final String ETAT_DOCUMENT_NON_TRANSORME = "nontransforme";
	protected static final String ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER = "nontransformearelancer";
	
	protected static final String ETAT_ABONNEMENT_ANNULE = "annuler";
	protected static final String ETAT_ABONNEMENT_EN_COURS = "encours";
	protected static final String ETAT_ABONNEMENT_SUSPENDU = "suspendu";

	protected IDashboardDocumentServiceRemote taDocumentService;
	
	protected @EJB ITaFactureServiceRemote taFactureService;
	protected @EJB ITaDevisServiceRemote taDevisService;
	protected @EJB ITaAvoirServiceRemote taAvoirService;
	protected @EJB ITaPrelevementServiceRemote taPrelevementService;
	protected @EJB ITaProformaServiceRemote taProformaService;
	protected @EJB ITaBoncdeServiceRemote taBoncdeService;
	protected @EJB ITaReglementServiceRemote taReglementService;
	protected @EJB ITaRReglementServiceRemote taRReglementService;
	protected @EJB ITaBonlivServiceRemote taBonlivService;
	protected @EJB ITaAcompteServiceRemote taAcompteService;
	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	

	@Inject protected TenantInfo tenantInfo;
	
//	protected @EJB IDashboardDocumentServiceRemote dashBoardDocumentServiceRemote;
	public abstract void refreshDashBoard(String etatDocument);
	
	protected String queryArticle = null;
	protected String queryTiers = null;

	protected TaInfoEntreprise infoEntreprise;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected static final String PERIODE_TODAY = "Aujourd'hui";
	protected static final String PERIODE_MOIS_COURANT = "Mois courant";
	protected static final String PERIODE_ANNEE_COURANTE = "Année courante";
	protected static final String PERIODE_HIER = "Hier";
	protected static final String PERIODE_PERSONNALISE = "Personnalisé";
	protected static final String PERIODE_ANNEE_PRECEDENTE = "Année précédente";
	protected static final String PERIODE_PRECEDENTE = "Période précédente";
	protected static final String PERIODE_ANNEE_EXERCICE_COURANT = "Exercice courant";
	protected List<String> listeChoixPeriode = new ArrayList<String>();
	protected String selectedChoixPeriode = "";
    
    //Rajout comparaison période
    protected LocalDate dateDebutComp;
    protected LocalDate dateFinComp;
    protected boolean comparaison = false;
    protected List<String> listeChoixPeriodeComp = new ArrayList<String>();
	protected String selectedChoixPeriodeComp = "";

	protected List<DocumentDTO> valueLignesDocumentComp  = new ArrayList<DocumentDTO>();
	protected List<InfosMois> listMoisAnneeExoComp = new ArrayList<InfosMois>();
	protected List<InfosJours> listJoursAnneeExoComp = new ArrayList<InfosJours>();
	protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentComp = new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeCaJoursDocumentComp = new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiersComp = new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMoisComp = new ArrayList<TaArticlesParTiersDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
    protected List<DocumentDTO> listeDocumentDetailMoisComp = new ArrayList<DocumentDTO>();
    protected List<DocumentDTO> listeDocumentDetailJourComp= new ArrayList<DocumentDTO>();
    
    

	protected boolean chevauchementPeriode = false;
    protected Date dateDebutGrande;
    protected Date dateFinGrande;
    protected Date dateDebutPetite;
    protected Date dateFinPetite;
    protected List<InfosMois> grandeListeGraphiqueMensuel = new ArrayList<InfosMois>();
    protected List<InfosMois> petiteListeGraphiqueMensuel = new ArrayList<InfosMois>();
    protected List<InfosJours> grandeListeGraphiqueJour = new ArrayList<InfosJours>();
    protected List<InfosJours> petiteListeGraphiqueJour = new ArrayList<InfosJours>();
    protected BigDecimal montantTotalHtMoisComp = BigDecimal.ZERO;
    protected BigDecimal montantTotalHtJourComp = BigDecimal.ZERO;
    protected String nomMois;
    protected String nomMoisDetailComp;
    protected String infosLabelRapportMensuelDataTableComp; 
    protected String infosLabelRapportArticlesDataTableComp; 
    protected String infosLabelRapportJournalierDataTableComp; 
    protected String infosLabelRapportArticlesJourDataTableComp; 
    private int numeroJourDetailComp;
    protected String infoLabel2;
    //fin rajout comparaison
    
    protected List<TaEditionDTO> listeEdition = new ArrayList<TaEditionDTO>();

	public String getInfoLabel2() {
		return infoLabel2;
	}
	public void setInfoLabel2(String infoLabel2) {
		this.infoLabel2 = infoLabel2;
	}

	protected String codeEtatDocument;
    protected int numeroMois; //AJOUTER LE 06/09/2017 Jean Marc
    protected String codeMoisAnnee;
    protected String nomMoisDetail;
    private int numeroJourDetail; //AJOUTER LE 06/09/2017 Jean Marc
    protected BigDecimal montantTotalHtMois = BigDecimal.ZERO;
    protected BigDecimal montantTotalHtJour = BigDecimal.ZERO;

    protected String cSbNbDocumentSurPeriode;
    protected String cSbNbDocumentTransformeSurPeriode;
    protected String cSbNbDocumentNonTransformeSurPeriode;
    protected String cSbNbDocumentNonTransformeARelancerSurPeriode; 
    
    
//    private String nomMoisDetail;
//    private int numeroJourDetail; //AJOUTER LE 06/09/2017 Jean Marc
    protected boolean afficheDocumentJour;
    protected boolean afficheDocumentJourDataTable; 
    protected String infosLabelRapportMensuelDataTable; 
    protected String infosLabelRapportArticlesDataTable; 
    protected String infosLabelRapportJournalierDataTable; 
    protected String infosLabelRapportArticlesJourDataTable;
//  private int numeroJourDetail; //AJOUTER LE 29/05/2019 Jean Marc
    protected String infoLabel;
    
    
 
	protected List<InfosMois> listMoisAnneeExo;
	protected List<InfosJours> listJoursAnneeExo; 
    protected String infoEtatDocument = ETAT_DOCUMENT_TOUS;	  //AJOUTER LE 06/09/2017 Jean Marc	
    
    protected List<DocumentDTO> valueLignesDocumentTotalEtat = new ArrayList<DocumentDTO>();
    protected List<DocumentDTO> valueLignesDocument = new ArrayList<DocumentDTO>();
    protected List<DocumentDTO> listeDocumentDetailMois = new ArrayList<DocumentDTO>();
    protected List<DocumentDTO> listeDocumentDetailJour = new ArrayList<DocumentDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour= new ArrayList<TaArticlesParTiersDTO>();
    /**RAJOUT YANN pour le dash Facture (ligne docs par tiers sur periode)**/
    protected List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiers = new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiersTous = new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiersTousComp = new ArrayList<DocumentChiffreAffaireDTO>();


	//    private List<MoisExoCourant> listeMoisExoCourant;
//    private BarChartModel barModelFactureExo;
//    private BarChartModel barModelDevisExo; // a passer en paramètre dans createBarModel 
//    List<Object> listeCaMois;//a détruire
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocument = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentRelance = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentNonTransforme = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentTransforme = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo

    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentRelanceComp = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentNonTransformeComp = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentTransformeComp = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo

    
    protected List<DocumentChiffreAffaireDTO> listeCaJoursDocument = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo AJOUTER LE 06/09/2017 Jean Marc
    protected List<DocumentChiffreAffaireDTO> listeCaJoursDocumentRelance = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo AJOUTER LE 06/09/2017 Jean Marc
    protected List<DocumentChiffreAffaireDTO> listeCaJoursDocumentNonTransforme  = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo AJOUTER LE 06/09/2017 Jean Marc
    protected List<DocumentChiffreAffaireDTO> listeCaJoursDocumentTransforme  = new ArrayList<DocumentChiffreAffaireDTO>();// a passer en paramètre dans initBarModelFActureExo AJOUTER LE 06/09/2017 Jean Marc
    
    protected List<DocumentChiffreAffaireDTO> listeTotauxCaPeriodeFactureAvecEtat=new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentTout=new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeCaJoursDocumentTout=new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMoisTous=new ArrayList<TaArticlesParTiersDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJourTous=new ArrayList<TaArticlesParTiersDTO>();
    
    protected List<DocumentChiffreAffaireDTO> listeTotauxCaPeriodeFactureCompAvecEtat=new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeCaMoisDocumentCompTout=new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<DocumentChiffreAffaireDTO> listeCaJoursDocumentCompTout=new ArrayList<DocumentChiffreAffaireDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMoisCompTous=new ArrayList<TaArticlesParTiersDTO>();
    protected List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJourCompTous=new ArrayList<TaArticlesParTiersDTO>();
    protected List<DocumentDTO> valueLignesDocumentTotalCompEtat = new ArrayList<DocumentDTO>();
    

    
    private MindmapNode docRoot;
    protected boolean grosFichierArticles=false;
    protected boolean grosFichierTiers=false;
    
    @PostConstruct
	public void postConstruct(){
    	infoEntreprise = taInfoEntrepriseService.findInstance();
    	dateDebut = LibDate.dateToLocalDateTime(new Date(taInfoEntrepriseService.findInstance().getDatedebInfoEntreprise().getTime())).toLocalDate();
    	dateFin = LibDate.dateToLocalDateTime(new Date(taInfoEntrepriseService.findInstance().getDatefinInfoEntreprise().getTime())).toLocalDate();
    	dateDebutComp = LibDate.dateToLocalDateTime(new Date(taInfoEntrepriseService.findInstance().getDatedebInfoEntreprise().getTime())).toLocalDate();
    	dateFinComp = LibDate.dateToLocalDateTime(new Date(taInfoEntrepriseService.findInstance().getDatefinInfoEntreprise().getTime())).toLocalDate();
    	listeDocumentDetailMois = new ArrayList<DocumentDTO>();
    	listeDocumentDetailJour = new ArrayList<DocumentDTO>();
    	listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
    	listeDocumentArticleParTiersParJour = new ArrayList<TaArticlesParTiersDTO>();
    	/**RAJOUT YANN**/
    	listeDocumentFactureParTiers = new ArrayList<DocumentChiffreAffaireDTO>();
    	initChoixPeriode();
    	initChoixPeriodeComp();
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
//    	createBarModel();
    	grosFichierArticles=taPreferencesService.grosFichierArticle();
    	grosFichierTiers=taPreferencesService.grosFichierTiers();
    	rechercheAvecCommentaire(true);
    }
    public void initChoixPeriode(){
    	listeChoixPeriode.add(PERIODE_TODAY);
    	listeChoixPeriode.add(PERIODE_HIER);
    	listeChoixPeriode.add(PERIODE_MOIS_COURANT);
    	listeChoixPeriode.add(PERIODE_ANNEE_COURANTE);
    	listeChoixPeriode.add(PERIODE_ANNEE_EXERCICE_COURANT);
    	listeChoixPeriode.add(PERIODE_PERSONNALISE);
    	
    	selectedChoixPeriode = PERIODE_ANNEE_EXERCICE_COURANT;
    	
    }
    
    public void initChoixPeriodeComp(){
//    	listeChoixPeriodeComp.add(PERIODE_TODAY);
//  	listeChoixPeriodeComp.add(PERIODE_HIER);
//    	listeChoixPeriodeComp.add(PERIODE_MOIS_COURANT);
//    	listeChoixPeriodeComp.add(PERIODE_ANNEE_COURANTE);
//    	listeChoixPeriodeComp.add(PERIODE_PERSONNALISE);
    	
    	listeChoixPeriodeComp.add(PERIODE_ANNEE_PRECEDENTE);
    	listeChoixPeriodeComp.add(PERIODE_PRECEDENTE);
    	listeChoixPeriodeComp.add(PERIODE_PERSONNALISE);
    	
    	
    	selectedChoixPeriodeComp = PERIODE_ANNEE_PRECEDENTE;
    	
    }
    
    public void initPeriodePerso() {
    	//si la date de debut est après la date de fin, on met la date de fin en date debut
    	if(dateDebut.compareTo(dateFin) > 0) {
    		dateDebut = dateFin;
    	}
    	selectedChoixPeriode = PERIODE_PERSONNALISE;
    }
    public void initPeriodePersoComp() {
    	//si la date de debut est après la date de fin, on met la date de fin en date debut
    	if(dateDebutComp.compareTo(dateFinComp) > 0) {
    		dateDebutComp = dateFinComp;
    	}
    	selectedChoixPeriodeComp = PERIODE_PERSONNALISE;
    }
    
    public void initPeriode() {
    	switch (selectedChoixPeriode) {
		case PERIODE_TODAY:
			initBornePeriodeAujourdhui(false);
			break;
		case PERIODE_HIER:
			initBornePeriodeHier(false);
			break;
		case PERIODE_MOIS_COURANT:
			initBornePeriodeMoisCourant(false);
			break;
		case PERIODE_ANNEE_COURANTE:
			try {
				initBornePeriodeAnneeCourante(false);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case PERIODE_ANNEE_EXERCICE_COURANT :
			try {
				initBornePeriodeAnneeExerciceCourant(false);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
    	
    	if(comparaison) {
    		initPeriodeComp();
    	}
    }
    
    public void initPeriodeComp() {

    	switch (selectedChoixPeriodeComp) {
//		case PERIODE_TODAY:
//			initBorneCompPeriodeAujourdhui(false);
//			break;
//		case PERIODE_HIER:
//			initBorneCompPeriodeHier(false);
//			break;
//		case PERIODE_MOIS_COURANT:
//			initBorneCompPeriodeMoisCourant(false);
//			break;
//		case PERIODE_ANNEE_COURANTE:
//			try {
//				initBorneCompPeriodeAnneeCourante(false);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			break;
		case PERIODE_ANNEE_PRECEDENTE:
			initBorneCompPeriodeAnneePrecedante(false);
			break;
		case PERIODE_PRECEDENTE:
			initBorneCompPeriodePrecedante(false);
			break;
		default:
			break;
		}
    }

    // Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision) {
		List<DocumentChiffreAffaireDTO> list = null;
//		list = dashBoardDocumentServiceRemote.findChiffreAffaireJmaDocument(nomTypeDocument, dateDebut, dateFin, precision);
		return list;
//		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			list = taFactureService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//		
//
//		case TaDevis.TYPE_DOC:
//			list = taDevisService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaPrelevement.TYPE_DOC:
//			list = taPrelevementService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaProforma.TYPE_DOC:
//			list = taProformaService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaBoncde.TYPE_DOC:
//			list = taBoncdeService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaBonliv.TYPE_DOC:
//			list = taBonlivService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		case TaAcompte.TYPE_DOC:
//			list = taAcompteService.findChiffreAffaireJmaDTO(dateDebut, dateFin, precision);
//			return list;
//			
//		default:
//			break;
//		}
//		
//    	
//    	// penser à créer une exception
//		return null;
//    	
    }
	
	// Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
		public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision) {
			List<DocumentChiffreAffaireDTO> list = null;
//			list = dashBoardDocumentServiceRemote.findChiffreAffaireNonTransformeJmaDocument(nomTypeDocument, dateDebut, dateFin, precision);
			return list;
			
//			switch (nomTypeDocument) {
////			case TaFacture.TYPE_DOC:
////				list = taFactureService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
////				return list;
//			
//
//			case TaDevis.TYPE_DOC:
//				list = taDevisService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//				return list;
//				
//			case TaPrelevement.TYPE_DOC:
//				list = taPrelevementService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//				return list;
//				
//			case TaProforma.TYPE_DOC:
//				list = taProformaService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//				return list;
//				
//			case TaBoncde.TYPE_DOC:
//				list = taBoncdeService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//				return list;
//				
//			case TaBonliv.TYPE_DOC:
//				list = taBonlivService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//				return list;
//				
//			case TaAcompte.TYPE_DOC:
//				list = taAcompteService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
//				return list;
//				
//			default:
//				break;
//			}
//			
//	    	
//	    	// penser à créer une exception
//			return null;
	    	
	    }
		
		// Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
				public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision) {
					List<DocumentChiffreAffaireDTO> list = null;
//					list = dashBoardDocumentServiceRemote.findChiffreAffaireTransformeJmaDocument(nomTypeDocument, dateDebut, dateFin, precision);
					return list;
					
//					switch (nomTypeDocument) {
////					case TaFacture.TYPE_DOC:
////						list = taFactureService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
////						return list;
//					
//
//					case TaDevis.TYPE_DOC:
//						list = taDevisService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaPrelevement.TYPE_DOC:
//						list = taPrelevementService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaProforma.TYPE_DOC:
//						list = taProformaService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaBoncde.TYPE_DOC:
//						list = taBoncdeService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaBonliv.TYPE_DOC:
//						list = taBonlivService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaAcompte.TYPE_DOC:
//						list = taAcompteService.findChiffreAffaireTransformeJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					default:
//						break;
//					}
//					
//			    	
//			    	// penser à créer une exception
//					return null;
			    	
			    }
				
				// Retourne les CA HT, TVA et TTC éclatés sur jour, mois, année, pour un type de document , en fonction de la période et de la précision
				public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeARelancerJmaDocument(String nomTypeDocument, Date dateDebut, Date dateFin, int precision, int deltaNbJours) {
					List<DocumentChiffreAffaireDTO> list = null;
//					list = dashBoardDocumentServiceRemote.findChiffreAffaireNonTransformeARelancerJmaDocument(nomTypeDocument, dateDebut, dateFin, precision, deltaNbJours);
					return list;
//					switch (nomTypeDocument) {
////					case TaFacture.TYPE_DOC:
////						list = taFactureService.findChiffreAffaireNonTransformeJmaDTO(dateDebut, dateFin, precision);
////						return list;
//					
//
//					case TaDevis.TYPE_DOC:
//						list = taDevisService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaPrelevement.TYPE_DOC:
//						list = taPrelevementService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaProforma.TYPE_DOC:
//						list = taProformaService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision, deltaNbJours);
//						return list;
//						
//					case TaBoncde.TYPE_DOC:
//						list = taBoncdeService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaBonliv.TYPE_DOC:
//						list = taBonlivService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					case TaAcompte.TYPE_DOC:
//						list = taAcompteService.findChiffreAffaireTransformeARelancerJmaDTO(dateDebut, dateFin, precision);
//						return list;
//						
//					default:
//						break;
//					}
//					
//			    	
//			    	// penser à créer une exception
//					return null;
			    	
			    }
	
				
	// Retourne les CA HT, TVA et TTC pour un type de document , en fonction de la période
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDocument(String nomTypeDocument, Date dateDebut, Date dateFin) {
		List<DocumentChiffreAffaireDTO> liste = null;
//		liste = dashBoardDocumentServiceRemote.findChiffreAffaireTotalDocument(nomTypeDocument, dateDebut, dateFin);
		return liste;
//		
//		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			return taFactureService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//
//		case TaDevis.TYPE_DOC:
//			return taDevisService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//			
//		case TaAvoir.TYPE_DOC:
//			return taAvoirService.findChiffreAffaireTotalDTO(dateDebut, dateFin);			
//
//		case TaPrelevement.TYPE_DOC:
//			return taPrelevementService.findChiffreAffaireTotalDTO(dateDebut, dateFin);			
//					
//		case TaProforma.TYPE_DOC:
//			return taProformaService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//			
//		case TaBoncde.TYPE_DOC:
//			return taBoncdeService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//		
//		case TaBonliv.TYPE_DOC:
//			return taBonlivService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//			
//		case TaAcompte.TYPE_DOC:
//			return taAcompteService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//			
//		default:
//			break;
//		}
//		
//    	
//    	// penser à créer une exception
//		return listeVide = new ArrayList<DocumentChiffreAffaireDTO>();
    	
    }

	 // Retourne les CA HT, TVA et TTC pour un type de document , en fonction de la période
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTODocument(String nomTypeDocument, Date dateDebut, Date dateFin) {
		List<DocumentChiffreAffaireDTO> liste = null;
//		liste = dashBoardDocumentServiceRemote.findChiffreAffaireNonTransformeTotalDTODocument(nomTypeDocument, dateDebut, dateFin);
		return liste;
		
		
//		switch (nomTypeDocument) {
//		case TaFacture.TYPE_DOC:
//			
//			break;
//
//		case TaDevis.TYPE_DOC:
//			listeVide = taDevisService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			
//
//		case TaPrelevement.TYPE_DOC:
//			listeVide = taPrelevementService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			
//		
//		case TaProforma.TYPE_DOC:
//			listeVide = taProformaService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			
//		case TaBoncde.TYPE_DOC:
//			listeVide = taBoncdeService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			
//		case TaBonliv.TYPE_DOC:
//			listeVide = taBonlivService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			
//		case TaAcompte.TYPE_DOC:
//			listeVide = taAcompteService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			return listeVide;
//			
//		default:
//			break;
//		}
//		  				
//		// penser à créer une exception
//		return listeVide = new ArrayList<DocumentChiffreAffaireDTO>();
    	
    }
	
	
	// Retourne les CA HT, TVA et TTC pour un type de document , en fonction de la période
		public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTODocument(String nomTypeDocument, Date dateDebut, Date dateFin) {
			List<DocumentChiffreAffaireDTO> liste = null;
//			liste = dashBoardDocumentServiceRemote.findChiffreAffaireTransformeTotalDTODocument(nomTypeDocument, dateDebut, dateFin);
			return liste;
//			
//			
//			switch (nomTypeDocument) {
//			case TaFacture.TYPE_DOC:
//				
//				break;
//
//			case TaDevis.TYPE_DOC:
//				listeVide = taDevisService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//				return listeVide;
//				
//
//			case TaPrelevement.TYPE_DOC:
//				listeVide = taPrelevementService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//				return listeVide;
//				
//			
//			case TaProforma.TYPE_DOC:
//				listeVide = taProformaService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//				return listeVide;
//				
//			case TaBoncde.TYPE_DOC:
//				listeVide = taBoncdeService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//				return listeVide;
//				
//			case TaBonliv.TYPE_DOC:
//				listeVide = taBonlivService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//				return listeVide;
//				
//			case TaAcompte.TYPE_DOC:
//				listeVide = taAcompteService.findChiffreAffaireTransformeTotalDTO(dateDebut, dateFin);
//				return listeVide;
//				
//			default:
//				break;
//			}
//			
//	    	
//			
//			
//			// penser à créer une exception
//			return listeVide = new ArrayList<DocumentChiffreAffaireDTO>();
//	    	
	    }	
	

		
//	public List<Object> findChiffreAffaireTotal() {
//		
//    	List<Object> list = taFactureService.findChiffreAffaireTotal(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 1);
//    	
//		return list;
//    	
//    }
//	
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDevis() {
//		
//    	List<DocumentChiffreAffaireDTO> list = taDevisService.findChiffreAffaireJmaDTO(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 1);
//    	
//		return list;
//    	
//    }
		
		public void initBornePeriodeAujourdhui()
		{
			initBornePeriodeAujourdhui(true);
		}
		public void initBornePeriodeAujourdhui(boolean refreshDashboard)
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY,0);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			
			dateDebut = LibDate.dateToLocalDate(cal.getTime());
			dateFin = LibDate.dateToLocalDate(cal.getTime());
			//dateDebut = LibDate.dateDuJour();
			//dateFin = LibDate.dateDuJour();
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
			
		}
		public void initBornePeriodeHier()
		{
			initBornePeriodeHier(true);
		}
		public void initBornePeriodeHier(boolean refreshDashboard)
		{
			dateDebut = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0));
			dateFin = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0));
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}
		
		public void initBornePeriodeMoisCourant()
		{
			initBornePeriodeMoisCourant(true);
		}

		public void initBornePeriodeMoisCourant(boolean refreshDashboard)
		{
			dateDebut = LibDate.dateToLocalDate(LibDate.PremierJourMois(LibDate.dateDuJour()));
			dateFin = LibDate.dateToLocalDate(LibDate.DernierJourMois(LibDate.dateDuJour()));
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}

		public void initBornePeriodeMoisPrecedent()
		{
//			Date today = new Date();
//	        LocalDate localToday= today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//	        LocalDate localTodayPlus30 = localToday.plusDays(30);
//	        Date todayPlus30 = Date.from(localTodayPlus30.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
	        
//			Date dateDebTmp = LibDate.PremierJourMois(LibDate.dateDuJour());
//			Date dateFinTmp = LibDate.DernierJourMois(LibDate.dateDuJour());
//			LocalDate localDateDebTmp = localToday.minusMonths(1);
//			LocalDate dateFinTmp = LibDate.dateDuJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			dateDebut = LibDate.incrementDate(dateDebTmp, 0, -1, 0);
//			dateFin = LibDate.incrementDate(dateFinTmp, 0, -1, 0);
//			refreshDashBoard(codeEtatDocument);
		}
		
		public void initBornePeriodeAnneeCourante() throws ParseException
		{
			initBornePeriodeAnneeCourante(true);
		}

		public void initBornePeriodeAnneeCourante(boolean refreshDashboard) throws ParseException
		{
			String annee = LibDate.getAnnee(LibDate.dateDuJour());

			dateDebut = LibDate.dateToLocalDate(LibDate.stringToDate("01/01/"+annee));
			dateFin = LibDate.dateToLocalDate(LibDate.stringToDate("31/12/"+annee));
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}
		
		public void initBornePeriodeAnneeExerciceCourant() throws ParseException
		{
			initBornePeriodeAnneeExerciceCourant(true);
		}

		public void initBornePeriodeAnneeExerciceCourant(boolean refreshDashboard) throws ParseException
		{
//			String annee = LibDate.getAnnee(LibDate.dateDuJour());

			dateDebut = LibDate.dateToLocalDate(infoEntreprise.getDatedebInfoEntreprise());
			dateFin = LibDate.dateToLocalDate(infoEntreprise.getDatefinInfoEntreprise());
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}
		
		
		////RAJOUT POUR COMPARAISON PERIODE////
		
		public void initBorneCompPeriodeAujourdhui()
		{
			initBorneCompPeriodeAujourdhui(true);
		}
		public void initBorneCompPeriodeAujourdhui(boolean refreshDashboard)
		{
			
			dateDebutComp = LibDate.dateToLocalDate(LibDate.dateDuJour());
			dateFinComp = LibDate.dateToLocalDate(LibDate.dateDuJour());
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
			
		}
		public void initBorneCompPeriodeHier()
		{
			initBorneCompPeriodeHier(true);
		}
		public void initBorneCompPeriodeHier(boolean refreshDashboard)
		{
			dateDebutComp = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0));
			dateFinComp = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0));
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}
		
		public void initBorneCompPeriodeMoisCourant()
		{
			initBorneCompPeriodeMoisCourant(true);
		}

		public void initBorneCompPeriodeMoisCourant(boolean refreshDashboard)
		{
			dateDebutComp = LibDate.dateToLocalDate(LibDate.PremierJourMois(LibDate.dateDuJour()));
			dateFinComp = LibDate.dateToLocalDate(LibDate.DernierJourMois(LibDate.dateDuJour()));
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}

		public void initBorneCompPeriodeMoisPrecedent()
		{
//			Date today = new Date();
//	        LocalDate localToday= today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//	        LocalDate localTodayPlus30 = localToday.plusDays(30);
//	        Date todayPlus30 = Date.from(localTodayPlus30.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
	        
//			Date dateDebTmp = LibDate.PremierJourMois(LibDate.dateDuJour());
//			Date dateFinCompTmp = LibDate.DernierJourMois(LibDate.dateDuJour());
//			LocalDate localDateDebTmp = localToday.minusMonths(1);
//			LocalDate dateFinCompTmp = LibDate.dateDuJour().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			dateDebutComp = LibDate.incrementDate(dateDebTmp, 0, -1, 0);
//			dateFinComp = LibDate.incrementDate(dateFinCompTmp, 0, -1, 0);
//			refreshDashBoard(codeEtatDocument);
		}
		
		public void initBorneCompPeriodeAnneeCourante() throws ParseException
		{
			initBorneCompPeriodeAnneeCourante(true);
		}

		public void initBorneCompPeriodeAnneeCourante(boolean refreshDashboard) throws ParseException
		{
			String annee = LibDate.getAnnee(LibDate.dateDuJour());

			dateDebutComp = LibDate.dateToLocalDate(LibDate.stringToDate("01/01/"+annee));
			dateFinComp = LibDate.dateToLocalDate(LibDate.stringToDate("31/12/"+annee));
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}
		
		public void initBorneCompPeriodeAnneePrecedante(boolean refreshDashboard)
		{
			//creation de 2 objets calendar
			Calendar calFin = Calendar.getInstance();
			calFin.setTime(LibDate.localDateToDate(dateFin));
			Calendar calDebut = Calendar.getInstance();
			calDebut.setTime(LibDate.localDateToDate(dateDebut));
			
			//Calculer ecart en années entre l'année de fin moins l'année de debut de la periode en cours
			int ecartAnnee = calFin.get(Calendar.YEAR) - calDebut.get(Calendar.YEAR);
			//On fait -1 -ecartAnnee pour prendre en compte aussi l'année courante
			dateDebutComp = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.localDateToDate(dateDebut), 0, 0, -1-ecartAnnee));
			dateFinComp = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.localDateToDate(dateFin), 0, 0, -1-ecartAnnee));
			
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}
		
		public void initBorneCompPeriodePrecedante(boolean refreshDashboard)
		{
			
			
			int nbJours = LibDate.nbJoursBetween2Dates(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
			
			dateDebutComp = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.localDateToDate(dateDebut), -nbJours-1, 0, 0));
			dateFinComp = LibDate.dateToLocalDate(LibDate.incrementDate(LibDate.localDateToDate(dateFin), -nbJours-1, 0, 0));
			
			if(refreshDashboard) {
				refreshDashBoard(codeEtatDocument);
			}
		}
	
		
		
		////FIN RAJOUT POUR COMPARAISON PERIODE/////
		
		
		

//		public void actPeriodePredefinie(String quand) {
//	    	switch (quand) {
//			case "jourcourant":
//				dateDebut = LibDate.dateDuJour();
//				dateFin = LibDate.dateDuJour();
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "hier":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "semainecourante":
//				Calendar mCalendar = Calendar.getInstance();
////				mCalendar.getFirstDayOfWeek()
//				//mCalendar.
//				//enCeMoment = new Instant;
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "semaineprecedente":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "moiscourant":
//				dateDebut = LibDate.PremierJourMois(LibDate.dateDuJour());
//				dateFin = LibDate.DernierJourMois(LibDate.dateDuJour());
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "moisprecedent":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "trimestrecourant":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "trimestreprecedent":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "semestrecourant":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "semestreprecedent":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "anneecourante":
//				dateDebut =  LibDate.stringToDate2("01/01/2018");// LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.stringToDate2("31/12/2018");
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "exercicecourant":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			case "exerciceprecedent":
//				dateDebut = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				dateFin = LibDate.incrementDate(LibDate.dateDuJour(), -1, 0, 0);
//				refresh(ETAT_DOCUMENT_TOUS);
//				break;
//			default:
//				break;
//			}
//	    refresh("tous");
//	    	
//	    }
	    
	public void initClasseCssBloc(String cSbNbSurPeriode, String cSbNbNonTransformeSurPeriode, String cSbNbNonTransformeARelancerSurPeriode, String cSbNbTransformeSurPeriode) {
		cSbNbDocumentSurPeriode = cSbNbSurPeriode;
		cSbNbDocumentNonTransformeSurPeriode = cSbNbNonTransformeSurPeriode;
		cSbNbDocumentNonTransformeARelancerSurPeriode = cSbNbNonTransformeARelancerSurPeriode; 
		cSbNbDocumentTransformeSurPeriode = cSbNbTransformeSurPeriode;
	}
	
	public void initInfosLabelRapportMensuelDataTable(String typeDocument) {
		
//		if (afficheProformaJour==true) {
		if (afficheDocumentJour==true) {
			setInfosLabelRapportMensuelDataTable("Liste des "+typeDocument+" du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
			setInfosLabelRapportArticlesDataTable("Liste des lignes article du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
			setInfosLabelRapportJournalierDataTable("Liste des "+typeDocument+" du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
			setInfosLabelRapportArticlesJourDataTable("Liste des lignes article du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
			if(comparaison) {
				 infosLabelRapportMensuelDataTableComp = "Liste des "+typeDocument+" du mois de "+nomMoisDetailComp+" pour un montant de "+montantTotalHtMoisComp+" € de type "+infoEtatDocument;
				 infosLabelRapportArticlesDataTableComp= "Liste des lignes article du mois de "+nomMoisDetailComp+" pour un montant de "+montantTotalHtMoisComp+" € de type "+infoEtatDocument; 
				 infosLabelRapportJournalierDataTableComp= "Liste des "+typeDocument+" du "+(numeroJourDetailComp+1)+" "+nomMoisDetailComp+" pour un montant de "+montantTotalHtJourComp+" € de type "+infoEtatDocument;
				 infosLabelRapportArticlesJourDataTableComp= "Liste des lignes article du "+(numeroJourDetailComp+1)+" "+nomMoisDetailComp+" pour un montant de "+montantTotalHtJourComp+" € de type "+infoEtatDocument;
			}
		}
		else {
			setInfosLabelRapportMensuelDataTable("Cliquez sur une barre du graphique pour avoir le détail mensuel des "+typeDocument);
			setInfosLabelRapportArticlesDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
			setInfosLabelRapportJournalierDataTable("Cliquez sur une barre du graphique pour avoir le détail journalier des "+typeDocument);
			setInfosLabelRapportArticlesJourDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
			if(comparaison) {
				 infosLabelRapportMensuelDataTableComp = "Cliquez sur une barre du graphique pour avoir le détail mensuel des "+typeDocument;
				 infosLabelRapportArticlesDataTableComp= "Cliquez sur une barre du graphique pour avoir le détail des articles par tiers"; 
				 infosLabelRapportJournalierDataTableComp= "Cliquez sur une barre du graphique pour avoir le détail journalier des "+typeDocument;
				 infosLabelRapportArticlesJourDataTableComp= "Cliquez sur une barre du graphique pour avoir le détail des articles par tiers";
			}
		}
		
		if (valueLignesDocument.isEmpty()){
			setInfosLabelRapportMensuelDataTable("La liste des "+typeDocument+" "+infoEtatDocument+" est vide");
			setInfosLabelRapportArticlesDataTable("La liste des articles "+infoEtatDocument+" est vide");
			setInfosLabelRapportJournalierDataTable("La liste des "+typeDocument+" "+infoEtatDocument+" est vide");
		}
		if(comparaison) {
			if (valueLignesDocumentComp != null && valueLignesDocumentComp.isEmpty()){
				setInfosLabelRapportMensuelDataTableComp("La liste des "+typeDocument+" "+infoEtatDocument+" est vide");
				setInfosLabelRapportArticlesDataTableComp("La liste des articles "+infoEtatDocument+" est vide");
				setInfosLabelRapportJournalierDataTableComp("La liste des "+typeDocument+" "+infoEtatDocument+" est vide");
			}
		}
		
		
		
	}

	
	public void initLabelInfosListesDetails() {
		
	}
	
	public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
//	public BarChartModel getBarModelFactureExo() {
//		return barModelFactureExo;
//	}
//	public void setBarModelFactureExo(BarChartModel barModelFactureExo) {
//		this.barModelFactureExo = barModelFactureExo;
//	}
	
//	protected BarChartModel initBarModelDocumentEntreDeuxDate(Date dateDebut, Date dateFin, BarChartModel barChart, String nomTypeDocument, int precision){
//		barChart = initBarModelDocumentEntreDeuxDate(dateDebut,dateFin,barChart,nomTypeDocument,precision, null);
//		return barChart;
//	}
	
	protected BarChartModel initBarModelDocumentEntreDeuxDate(Date dateDebut, Date dateFin, BarChartModel barChart, String nomTypeDocument, int precision, String etatDocument, int deltaNbJours) {
        barChart.setExtender("formatbarre");
        if (barChart.getSeries().size()>0){
        	barChart.clear();
        }
        ChartSeries moisGraphique = new ChartSeries();
        moisGraphique.setLabel("Mois");
       
        listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(dateDebut, dateFin);
  if (infoEtatDocument!=null) {
	  System.out.println(infoEtatDocument);
  switch (infoEtatDocument) {
  
  	case "tous":
        listeCaMoisDocument = taProformaService.listeChiffreAffaireTotalJmaDTO(dateDebut, dateFin, precision,null);
  
  	break;
  	
  	case "nontransforme":
  		listeCaMoisDocument = findChiffreAffaireNonTransformeJmaDocument(nomTypeDocument, dateDebut, dateFin, precision);
  	break;
  	
  	case "nontransformearelancer":
  		listeCaMoisDocument = findChiffreAffaireNonTransformeARelancerJmaDocument(nomTypeDocument, dateDebut, dateFin, precision, deltaNbJours);
  	break;
  	
  	case "transforme":
  		listeCaMoisDocument = findChiffreAffaireTransformeJmaDocument(nomTypeDocument, dateDebut, dateFin, precision);
  	break;
  						}
  
//        for (int i = 0; i < listeCaMoisDocument.size(); i++) {
//        	if (listeCaMoisDocument.get(i).getMois().length()<2){
//        		listeCaMoisDocument.get(i).setMois('0'+listeCaMoisDocument.get(i).getMois());
//        	}
//			
//		}
        
  } 
        // complète la liste des mois manquant dans la requête puisque celle-ci ne remonte que les mois qui ont 
        //un chiffre d'affaire
        for (InfosMois ligneInfosMois : listMoisAnneeExo) {
        	DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee(), listeCaMoisDocument); 
        	if (objMoisAnneeCa!= null ){
        		moisGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),objMoisAnneeCa.getMtHtCalc());
        	} else {
        		moisGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),0);
        	}

		}
		Calendar mCalendar = Calendar.getInstance();
        for (DocumentChiffreAffaireDTO ligneInfosMois : listeCaMoisDocument) {
        		mCalendar.setTime(LibDate.stringToDate2("01"+ligneInfosMois.getMois()+ligneInfosMois.getAnnee()));
        		moisGraphique.set(mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), ligneInfosMois.getMtHtCalc());

        		//        	DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee()); 
//        	if (objMoisAnneeCa!= null ){
//        		moisGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),objMoisAnneeCa.getMtHtCalc());
//        	} else {
//        		moisGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),0);
//        	}

		}
         
        barChart.addSeries(moisGraphique);
         
        return barChart;
    }
	
// 						 /////////////////////////////////////////////////////////////////
//  									MODIFIER LE 06/09/2017 Jean Marc	
	protected BarChartModel initBarModelJourDocumentEntreDeuxDate(Date debutDeMois, Date finDeMois, BarChartModel barChartJour, String nomTypeDocument, int precision, String etatDocument, int deltaNbJours) {

      barChartJour.setExtender("formatbarre");
      if (barChartJour.getSeries().size()>0){
      	barChartJour.clear();
      }
      ChartSeries jourGraphique = new ChartSeries();
      jourGraphique.setLabel("Jour");
      

      listJoursAnneeExo = LibDate.listeJoursEntreDeuxDate(debutDeMois, finDeMois);
      
if (infoEtatDocument!=null) {
	  System.out.println(infoEtatDocument);
switch (infoEtatDocument) {

	case "tous":
		
      listeCaJoursDocument = findChiffreAffaireJmaDocument(nomTypeDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), precision);

	break;
	
	case "nontransforme":
		
		listeCaJoursDocument = findChiffreAffaireNonTransformeJmaDocument(nomTypeDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), precision);
	break;
	
	case "nontransformearelancer":
		
		listeCaJoursDocument = findChiffreAffaireNonTransformeARelancerJmaDocument(nomTypeDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), precision, deltaNbJours);
	break;
	
	case "transforme":
		
		listeCaJoursDocument = findChiffreAffaireTransformeJmaDocument(nomTypeDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), precision);
	break;
						}

      for (int i = 0; i < listeCaJoursDocument.size(); i++) {
      	if (listeCaJoursDocument.get(i).getJour().length()<2){
      		listeCaJoursDocument.get(i).setJour('0'+listeCaJoursDocument.get(i).getJour());
      	}
			
		}
      
} 
      // complète la liste des jours manquant dans la requête puisque celle-ci ne remonte que les jours qui ont 
      //un chiffre d'affaire
      for (InfosJours ligneInfosJour : listJoursAnneeExo) {
      	DocumentChiffreAffaireDTO objJourAnneeCa = jourOfIndex(ligneInfosJour.getCodeJourMoisAnnee(), listeCaJoursDocument); 
      	if (objJourAnneeCa!= null ){
//      		jourGraphique.set(ligneInfosJour.getNumeroJour(),objJourAnneeCa.getMtHtCalc());
      		jourGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),objJourAnneeCa.getMtHtCalc());
      	} else {
      		jourGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),0);
      	}

		}
      

      barChartJour.addSeries(jourGraphique);
       
      return barChartJour;
  }
//												FIN DE MODIFICATION	
//  						////////////////////////////////////////////////////////////////////	
	
//		  private BarChartModel initBarModelFActureExo() {
//        BarChartModel model = new BarChartModel();
//        model.setExtender("formatbarre");
//        ChartSeries moisGraphique = new ChartSeries();
//        moisGraphique.setLabel("Mois");
//        
//        List<InfosMois> listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise());
//        
//        listeCaMoisDocument = findChiffreAffaireTotalDocument();
//        // complète la liste des mois manquant dans la requête puisque celle-ci ne remonte que les mois ont un chiffre d'affaire
//        for (InfosMois ligneInfosMois : listMoisAnneeExo) {
//        	Object objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee()); 
//        	if (objMoisAnneeCa!= null ){
//        		moisGraphique.set(ligneInfosMois.getNomCourt(),(BigDecimal)((Object[])objMoisAnneeCa)[3]);
//        	} else {
//        		moisGraphique.set(ligneInfosMois.getNomCourt(),0);
//        	}
//
//		}
//        
// 
//        model.addSeries(moisGraphique);
//         
//        return model;
//    }
	
	protected DocumentChiffreAffaireDTO moisOfIndex(String moisannee, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument){
		
		for (DocumentChiffreAffaireDTO objectMoisAnneeCa : listeMontantJmaDocument) {
			String moisAnneeCa = objectMoisAnneeCa.getMois() + objectMoisAnneeCa.getAnnee();
			if (moisAnneeCa.equals(moisannee)){
				return objectMoisAnneeCa;
			}
		}
		return null;
	}
//                      /////////////////////////////////////////////////////////////////
//                          MODIFIER LE 06/09/2017 Jean Marc
	
	protected DocumentChiffreAffaireDTO jourOfIndex(String jourannee,  List<DocumentChiffreAffaireDTO> listeMontantJmaDocument){
			
			for (DocumentChiffreAffaireDTO objectJourAnneeCa : listeMontantJmaDocument) {
				if (objectJourAnneeCa.getMois().length()<2) {
					objectJourAnneeCa.setMois("0"+objectJourAnneeCa.getMois());
				}
				if (objectJourAnneeCa.getJour().length()<2) {
					objectJourAnneeCa.setJour("0"+objectJourAnneeCa.getJour());
				}
				String jourAnneeCa = objectJourAnneeCa.getJour() + objectJourAnneeCa.getMois() + objectJourAnneeCa.getAnnee();
				if (jourAnneeCa.equals(jourannee)){
					return objectJourAnneeCa;
				}
			}
			return null;
		}
//								FIN DE MODIFICATION	
//                     ////////////////////////////////////////////////////////////////////
	
//	private Object moisOfIndex(String moisannee){
//		
//		for (Object objectMoisAnneeCa : listeCaMois) {
//			String moisAnneeCa = (String)((Object[])objectMoisAnneeCa)[1] + (String)((Object[])objectMoisAnneeCa)[2]; 
//			if (moisAnneeCa.equals(moisannee)){
//				return objectMoisAnneeCa;
//			}
//		}
//		return null;
//	}
	
//	protected BarChartModel createBarModel(Date dateDebut, Date dateFin, BarChartModel barChartModelDocument){
//		barChartModelDocument = createBarModel(dateDebut, dateFin, barChartModelDocument);
//		return barChartModelDocument;
//	}
	
	public BarChartModel createBarChartModelSimpleBarre(String titre, boolean anime, int stickAngle ){
		BarChartModel barChartModel = new BarChartModel();
		barChartModel.setTitle(titre);
		barChartModel.setLegendPosition("ne");
		barChartModel.setAnimate(true);
		barChartModel.setExtender("formatbarre");
        Axis xAxis = barChartModel.getAxis(AxisType.X);
        Axis yAxis = barChartModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setTickFormat("%'d €");
        xAxis.setTickAngle(stickAngle);
		return barChartModel;
	}
	
	public LineChartModel createLineChartModelSimpleBarre(String titre, boolean anime, int stickAngle ){
		LineChartModel lineChartModel = new LineChartModel();
		lineChartModel.setExtender("formatline");
		lineChartModel.setTitle(titre);
		lineChartModel.setLegendPosition("ne");
		lineChartModel.setShowPointLabels(true); 
		lineChartModel.getAxes().put(AxisType.X, new CategoryAxis("Mois"));
		Axis yAxis = lineChartModel.getAxis(AxisType.Y);
		Axis xAxis = lineChartModel.getAxis(AxisType.X);
		xAxis.setTickAngle(-50);
        yAxis.setMin(0);
        yAxis.setTickFormat("%'d €");
        xAxis.setTickAngle(stickAngle);
		return lineChartModel;
	}
	
	public HorizontalBarChartModel createBarChartModelSimpleBarreHoriz(String titre, boolean anime, int stickAngle) {
		return createBarChartModelSimpleBarreHoriz(titre, anime, stickAngle, -50);
	}
	public HorizontalBarChartModel createBarChartModelSimpleBarreHoriz(String titre, boolean anime, int stickAngleY, int stickAngleX ){
		HorizontalBarChartModel barChartModel = new HorizontalBarChartModel();
		barChartModel.setTitle(titre);
		barChartModel.setLegendPosition("ne");
		barChartModel.setAnimate(true);
//		barChartModel.setExtender("formatbarre");
		barChartModel.setExtender("formatbarreHoriz");
		
        Axis xAxis = barChartModel.getAxis(AxisType.X);
        Axis yAxis = barChartModel.getAxis(AxisType.Y);
//        yAxis.setMin(0);
//        yAxis.setTickFormat("%'d €");
//        xAxis.setTickAngle(-50);
        
        xAxis.setMin(0);
        xAxis.setTickFormat("%'d €");
        yAxis.setTickAngle(stickAngleY);
        xAxis.setTickAngle(stickAngleX);
		return barChartModel;
	}

	public BarChartModel createBarChartModelSimpleBarreVide(String titre, boolean anime, int stickAngle ){
		BarChartModel barChartModel = new BarChartModel();
		barChartModel.setTitle(titre);
		barChartModel.setLegendPosition("ne");
		barChartModel.setAnimate(true);
		barChartModel.setExtender("formatbarre");
        Axis xAxis = barChartModel.getAxis(AxisType.X);
        Axis yAxis = barChartModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        xAxis.setTickAngle(stickAngle);
        
        ChartSeries barreGraphique = new ChartSeries();
        barreGraphique.setLabel(titre);
        barreGraphique.set("Période",0);
        barChartModel.addSeries(barreGraphique);
		return barChartModel;
	}
	
	
	//JE TAFFE ICI (YANN)
//	public PieChartModel createPieChartModelSimpleBarreVide(String titre, boolean anime, int stickAngle ){
//		PieChartModel pieChartModel = new PieChartModel();
//		pieChartModel.setTitle(titre);
//		pieChartModel.setLegendPosition("ne");
//		pieChartModel.setAnimate(true);
//		pieChartModel.setExtender("formatbarre");
//        Axis xAxis = pieChartModel.getAxis(AxisType.X);
//        Axis yAxis = pieChartModel.getAxis(AxisType.Y);
//        yAxis.setMin(0);
//        xAxis.setTickAngle(-50);
//        
//        ChartSeries barreGraphique = new ChartSeries();
//        barreGraphique.setLabel(titre);
//        barreGraphique.set("Période",0);
//        pieChartModel.addSeries(barreGraphique);
//		return pieChartModel;
//	}
	
	public HorizontalBarChartModel createBarChartModelSimpleBarreVideHoriz(String titre, boolean anime, int stickAngle ){
		HorizontalBarChartModel barChartModel = new HorizontalBarChartModel();
		barChartModel.setTitle(titre);
		barChartModel.setLegendPosition("ne");
		barChartModel.setAnimate(true);
		barChartModel.setExtender("formatbarre");
        Axis xAxis = barChartModel.getAxis(AxisType.X);
        Axis yAxis = barChartModel.getAxis(AxisType.Y);
        yAxis.setMin(0);
        xAxis.setTickAngle(-50);
        
        ChartSeries barreGraphique = new ChartSeries();
        barreGraphique.setLabel(titre);
        barreGraphique.set("Période",0);
        barChartModel.addSeries(barreGraphique);
		return barChartModel;
	}
	
	public boolean containsMoisAnnee(final List<InfosMois> list, final String codeMoisAnnee){
        return list.stream().filter(o -> o.getCodeMoisAnnee().equals(codeMoisAnnee)).findFirst().isPresent();
    }
	
	public boolean containsMois(final List<InfosMois> list, final String mois){
        return list.stream().filter(o -> o.getMois().equals(mois)).findFirst().isPresent();
    }
	
	public boolean containsAnnee(final List<InfosMois> list, final String annee){
        return list.stream().filter(o -> o.getAnnee().equals(annee)).findFirst().isPresent();
    }
	
	public List<String> listAnnee(List<InfosMois> periode){
		 List<String> lisAnneePeriode = new ArrayList<String>();
		 String annee = "";
		 for (InfosMois infos : periode) {
			if(!infos.getAnnee().equals(annee)) {
				annee = infos.getAnnee();
				lisAnneePeriode.add(infos.getAnnee());
			}
		}
		 
		 return lisAnneePeriode;
	}
	public LineChartModel addLineSeriesMensuelComparaison(LineChartModel lineChart, String titre, String titreComp, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument,
			List<DocumentChiffreAffaireDTO> listeMontantJmaDocumentComp, Date dateDebut, Date dateFin, Date dateDebutComp, Date dateFinComp) {
		
		ChartSeries barreGraphique = new ChartSeries();
		ChartSeries barreGraphiqueComp = new ChartSeries();
		
		
		//rempli une liste de mois entre deux dates
		List<InfosMois> listMoisPeriode = new ArrayList<InfosMois>();
       listMoisPeriode = LibDate.listeMoisEntreDeuxDate(dateDebut, dateFin);      
       
       //rempli une liste de mois entre deux dates
       List<InfosMois> listMoisPeriodeComp = new ArrayList<InfosMois>();
       listMoisPeriodeComp = LibDate.listeMoisEntreDeuxDate(dateDebutComp, dateFinComp);
       
       
       
       /**formatage des caractères**/  
       // Formate les codes des mois sur deux caractères sur la liste de résultat de la requête
       for (int i = 0; i < listeMontantJmaDocument.size(); i++) {
       	if (listeMontantJmaDocument.get(i).getMois().length()<2){
       		listeMontantJmaDocument.get(i).setMois('0'+listeMontantJmaDocument.get(i).getMois());
       	}
   	   }
       
       // Formate les codes des mois sur deux caractères sur la liste de résultat de la requête
       for (int i = 0; i < listeMontantJmaDocumentComp.size(); i++) {
       	if (listeMontantJmaDocumentComp.get(i).getMois().length()<2){
       		listeMontantJmaDocumentComp.get(i).setMois('0'+listeMontantJmaDocumentComp.get(i).getMois());
       	}
   	   }
       /**fin formatage**/
      
       
       /*********************1 ere étape : On cherche la période la plus grande***********************************************************/
       List<InfosMois> listeInfosGrande = new ArrayList<InfosMois>();
       List<InfosMois> listeInfosPetite = new ArrayList<InfosMois>();
       List<DocumentChiffreAffaireDTO> listeValeurGrande = new ArrayList<DocumentChiffreAffaireDTO>();
       List<DocumentChiffreAffaireDTO> listeValeurPetite = new ArrayList<DocumentChiffreAffaireDTO>();
       if(listMoisPeriodeComp.size() > listMoisPeriode.size()) {
    	   listeInfosGrande.addAll(listMoisPeriodeComp);
    	   listeInfosPetite.addAll(listMoisPeriode);
    	   barreGraphique.setLabel(titreComp);
    	   barreGraphiqueComp.setLabel(titre);
    	   listeValeurGrande.addAll(listeMontantJmaDocumentComp);
    	   listeValeurPetite.addAll(listeMontantJmaDocument);
    	   /**On garde les dates de la période la plus grande et de la plus petite**/
    	   dateDebutGrande = dateDebutComp;
    	   dateFinGrande = dateFinComp;
    	   dateDebutPetite = dateDebut;
    	   dateFinPetite = dateFin;
    	   
       }else {
    	   listeInfosGrande.addAll(listMoisPeriode);
    	   listeInfosPetite.addAll(listMoisPeriodeComp);
    	   barreGraphique.setLabel(titre);
    	   barreGraphiqueComp.setLabel(titreComp);
    	   listeValeurGrande.addAll(listeMontantJmaDocument);
    	   listeValeurPetite.addAll(listeMontantJmaDocumentComp);
    	   /**On garde les dates de la période la plus grande et de la plus petite**/
    	   dateDebutGrande = dateDebut;
    	   dateFinGrande = dateFin;
    	   dateDebutPetite = dateDebutComp;
    	   dateFinPetite = dateFinComp;
    	   
       }
       /*************************************************fin recherche periode plus grande**********************************************/
       
       /**On verifie si les périodes se chevauchent**/
       chevauchementPeriode = false;
       chevauchement:
       for (InfosMois infosMois : listeInfosGrande) {
    	   for (InfosMois infosMois2 : listeInfosPetite) {
			if(infosMois.getCodeMoisAnnee().equals(infosMois2.getCodeMoisAnnee())) {
				chevauchementPeriode = true;
				break chevauchement;
			}
		}
		 
       }
       /**fin verif chevauchement**/
       
       if(!chevauchementPeriode) {//si pas de chevauchement
    	   
    	   //On complete les 2 listes de mois avec les montants correspondants et 0 si pas de montants
           //liste1
           for (InfosMois infosMois : listMoisPeriode) {
        	   DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(infosMois.getCodeMoisAnnee(), listeMontantJmaDocument);
        	   if(objMoisAnneeCa!=null) {
        		   infosMois.setMontant(objMoisAnneeCa.getMtHtCalc());
        	   }else {
        		   infosMois.setMontant(BigDecimal.ZERO);
        		   
        	   }
           }
    	   //liste2
           for (InfosMois infosMois : listMoisPeriodeComp) {
        	   DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(infosMois.getCodeMoisAnnee(), listeMontantJmaDocumentComp);
        	   if(objMoisAnneeCa!=null) {
        		   infosMois.setMontant(objMoisAnneeCa.getMtHtCalc());
        	   }else {
        		   infosMois.setMontant(BigDecimal.ZERO);
        		   
        	   }
           }
           
           
           
           
           
              
           /**TEST ALGO PHILIPPE**/
           //LISTE INVERSE GRANDE
           List<InfosMois> reverseListMoisPeriode = new ArrayList<InfosMois>();
           reverseListMoisPeriode.addAll(listeInfosGrande);
           Collections.reverse(reverseListMoisPeriode);
           //LISTE INVERSE PETITE
           List<InfosMois> reverseListMoisPeriodeComp = new ArrayList<InfosMois>();
           reverseListMoisPeriodeComp.addAll(listeInfosPetite);
           Collections.reverse(reverseListMoisPeriodeComp);

           
           /**On compte le nombre de mois avant 1er mois en commun dans les deux listes**/
           /**D'abord de gauche a droite**/
           int nbPassageGaucheDroite=0;
           boucle1:
           for (InfosMois infosMoisRef : listeInfosGrande) {
           	
           	for (InfosMois infosMoisComp : listeInfosPetite) {
           		if(infosMoisRef.getMois().equals(infosMoisComp.getMois())) {
           			break boucle1;
           		}else {
           			nbPassageGaucheDroite++;
           		}
           	}
           }
           /**Puis de droite a gauche**/
           int nbPassageDroiteGauche=0;
           boucle2:
           for (InfosMois infosMoisRef : reverseListMoisPeriode) {
           	
           	for (InfosMois infosMoisComp : reverseListMoisPeriodeComp) {
           		if(infosMoisRef.getMois().equals(infosMoisComp.getMois())) {
           			break boucle2;
           		}else {
           			nbPassageDroiteGauche++;
           		}
           	}
           }
          
           System.out.println(" Nombre de passage 1 :"+nbPassageGaucheDroite+" et nombre de passage 2 :"+nbPassageDroiteGauche);
           boolean gaucheADroite = true;
           //vrai >
           if(nbPassageDroiteGauche > nbPassageGaucheDroite) {
           	//on parcours de droite a gauche
           	gaucheADroite = false;
           }
           /**FIN TEST ALGO PHILIPPE**/
           
      
           /**ALGO PARCOURS**/

           
           List<InfosMois> grandeListe2 = new ArrayList<InfosMois>();
           List<InfosMois> petiteListe2 = new ArrayList<InfosMois>();
           List<InfosMois> listeGrandeParcours = new ArrayList<InfosMois>();
           listeGrandeParcours.addAll(listeInfosGrande);
           List<InfosMois> listePetiteParcours = new ArrayList<InfosMois>();
           listePetiteParcours.addAll(listeInfosPetite);
           
          //Si parcours de gauche à droite, on retourne la liste
           if(!gaucheADroite) {
        	   Collections.reverse(listeGrandeParcours);
        	   Collections.reverse(listePetiteParcours);
           }
 
         
         
        	   
        	   int iPL = 0;
        	   for (int i = 0; i < listeGrandeParcours.size(); i++) {//parcours de la grande liste
     		   //si je trouve le mois correspondant
        		   if(listePetiteParcours.size() > iPL &&  listeGrandeParcours.get(i).getMois().equals(listePetiteParcours.get(iPL).getMois())) {
        			   InfosMois moisValeurGrandeListe = new InfosMois();
        			   InfosMois moisValeurPetiteListe = new InfosMois();
        			   
        			   /**grande liste**/
        			   moisValeurGrandeListe.setAnnee(listeGrandeParcours.get(i).getAnnee());
        			   moisValeurGrandeListe.setMois(listeGrandeParcours.get(i).getMois());
        			   moisValeurGrandeListe.setNomCourt(listeGrandeParcours.get(i).getNomCourt());
        			   moisValeurGrandeListe.setNomLong((listeGrandeParcours.get(i).getNomLong()));
        			   moisValeurGrandeListe.setCodeMoisAnnee(listeGrandeParcours.get(i).getCodeMoisAnnee());
        			   moisValeurGrandeListe.setCodeMoisAnnee1Annee2(listeGrandeParcours.get(i).getNomCourt()+" "+listeGrandeParcours.get(i).getAnnee().substring(2)+" - "+listePetiteParcours.get(iPL).getAnnee().substring(2));
        			   moisValeurGrandeListe.setMontant(listeGrandeParcours.get(i).getMontant());
        			   moisValeurGrandeListe.setPremierJourMois(listeGrandeParcours.get(i).getPremierJourMois());
        			   moisValeurGrandeListe.setDernierJourMois(listeGrandeParcours.get(i).getDernierJourMois());
        			   
        			   
        			   /**petite liste**/
        			   moisValeurPetiteListe.setAnnee(listePetiteParcours.get(iPL).getAnnee());
        			   moisValeurPetiteListe.setMois(listePetiteParcours.get(iPL).getMois());
        			   moisValeurPetiteListe.setNomCourt(listePetiteParcours.get(iPL).getNomCourt());
        			   moisValeurPetiteListe.setNomLong((listePetiteParcours.get(i).getNomLong()));
        			   moisValeurPetiteListe.setCodeMoisAnnee(listePetiteParcours.get(iPL).getCodeMoisAnnee());
        			   moisValeurPetiteListe.setCodeMoisAnnee1Annee2(listePetiteParcours.get(iPL).getNomCourt()+" "+listeGrandeParcours.get(i).getAnnee().substring(2)+" - "+listePetiteParcours.get(iPL).getAnnee().substring(2));
        			   moisValeurPetiteListe.setMontant(listePetiteParcours.get(iPL).getMontant());
        			   moisValeurPetiteListe.setPremierJourMois(listePetiteParcours.get(iPL).getPremierJourMois());
        			   moisValeurPetiteListe.setDernierJourMois(listePetiteParcours.get(iPL).getDernierJourMois());
        			  
        			   
        			   grandeListe2.add(moisValeurGrandeListe);
        			   petiteListe2.add(moisValeurPetiteListe);
        			   iPL++;
        		   //si ce n'est pas le meme mois que la grande liste
        		   }else {
        			   InfosMois moisValeurGrandeListe = new InfosMois();
        			   InfosMois moisValeurPetiteListe = new InfosMois();
        			   
        			   /**grande liste**/
        			   moisValeurGrandeListe.setAnnee(listeGrandeParcours.get(i).getAnnee());
        			   moisValeurGrandeListe.setMois(listeGrandeParcours.get(i).getMois());
        			   moisValeurGrandeListe.setNomCourt(listeGrandeParcours.get(i).getNomCourt());
        			   moisValeurGrandeListe.setNomLong((listeGrandeParcours.get(i).getNomLong()));
        			   moisValeurGrandeListe.setCodeMoisAnnee(listeGrandeParcours.get(i).getCodeMoisAnnee());
        			   moisValeurGrandeListe.setCodeMoisAnnee1Annee2(listeGrandeParcours.get(i).getNomCourt()+" "+listeGrandeParcours.get(i).getAnnee().substring(2));
        			   moisValeurGrandeListe.setMontant(listeGrandeParcours.get(i).getMontant());
        			   moisValeurGrandeListe.setPremierJourMois(listeGrandeParcours.get(i).getPremierJourMois());
        			   moisValeurGrandeListe.setDernierJourMois(listeGrandeParcours.get(i).getDernierJourMois());
        			   
        			   /**petite liste**/
        			   moisValeurPetiteListe.setAnnee(listeGrandeParcours.get(i).getAnnee());
        			   moisValeurPetiteListe.setMois(listeGrandeParcours.get(i).getMois());
        			   moisValeurPetiteListe.setNomCourt(listeGrandeParcours.get(i).getNomCourt());
        			   moisValeurPetiteListe.setNomLong((listeGrandeParcours.get(i).getNomLong()));
        			   moisValeurPetiteListe.setCodeMoisAnnee(listeGrandeParcours.get(i).getCodeMoisAnnee());
        			   moisValeurPetiteListe.setCodeMoisAnnee1Annee2(listeGrandeParcours.get(i).getNomCourt()+" "+listeGrandeParcours.get(i).getAnnee().substring(2));
        			   moisValeurPetiteListe.setMontant(BigDecimal.ZERO);
        			   
        			   grandeListe2.add(moisValeurGrandeListe);
        			   petiteListe2.add(moisValeurPetiteListe);
        			   
        		   }
    			
        	   }
        	
           // si le parcours c'est fait de droite a gauche on a retourné les listes, donc on les inversent de nouveau
           if(!gaucheADroite) {
        	  Collections.reverse(grandeListe2); 
        	  Collections.reverse(petiteListe2); 
        	   
           }
           
           grandeListeGraphiqueMensuel.clear();
           petiteListeGraphiqueMensuel.clear();
           
           grandeListeGraphiqueMensuel.addAll(grandeListe2);
           petiteListeGraphiqueMensuel.addAll(petiteListe2);
           //on passe les listes au deux series
           for (InfosMois moisValeur : grandeListe2) {
        	   barreGraphique.set(moisValeur.getCodeMoisAnnee1Annee2(), moisValeur.getMontant());
           }
           for (InfosMois moisValeur : petiteListe2) {
        	   barreGraphiqueComp.set(moisValeur.getCodeMoisAnnee1Annee2(), moisValeur.getMontant());
           }
           
           
           
           /**FIN ALGO PARCOURS**/
    	   
       }
       
     //on passe les deux series au graphique
       lineChart.addSeries(barreGraphique);
       lineChart.addSeries(barreGraphiqueComp); 
      
       
	
		return lineChart;
	}

	
	public ChartSeries createBarChartSerieMensuel(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin) {
		return createBarChartSerieMensuel(titre,  listeMontantJmaDocument,  dateDebut,  dateFin, false);
	}
	public ChartSeries createBarChartSerieMensuel(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin, Boolean comparaison){
        ChartSeries barreGraphique = new ChartSeries();
        barreGraphique.setLabel(titre);

        List<InfosMois> listMoisPeriode = new ArrayList<InfosMois>();
        listMoisPeriode = LibDate.listeMoisEntreDeuxDate(dateDebut, dateFin);
        
     // Formate les codes des mois sur deux caractères sur la liste de résultat de la requête
        for (int i = 0; i < listeMontantJmaDocument.size(); i++) {
        	if (listeMontantJmaDocument.get(i).getMois().length()<2){
        		listeMontantJmaDocument.get(i).setMois('0'+listeMontantJmaDocument.get(i).getMois());
        	}
    	}
        //si il y a comparaison de plusieurs barre graphiques (ChartSeries), alors on n'affiche pas l'année, juste le mois pour regroupé par mois
        if(comparaison) {
        	// complète la liste des mois manquant dans la requête puisque celle-ci ne remonte que les mois qui ont 
            //un chiffre d'affaire
        	for (InfosMois ligneInfosMois : listMoisPeriode) {
            	DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee(), listeMontantJmaDocument); 
            	if (objMoisAnneeCa!= null ){//on affiche que le mois
            		barreGraphique.set(ligneInfosMois.getNomCourt(),objMoisAnneeCa.getMtHtCalc());
            	} else {
            		barreGraphique.set(ligneInfosMois.getNomCourt(),0);
            	}

    		}
        	
        }else {
        	// complète la liste des mois manquant dans la requête puisque celle-ci ne remonte que les mois qui ont 
            //un chiffre d'affaire
        	for (InfosMois ligneInfosMois : listMoisPeriode) {
            	DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee(), listeMontantJmaDocument); 
            	if (objMoisAnneeCa!= null ){//on affiche le mois et l'année
            		barreGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),objMoisAnneeCa.getMtHtCalc());
            	} else {
            		barreGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),0);
            	}

    		}
        	
        }

        return barreGraphique;
	}
	
	
	protected ChartSeries createBarChartSerieJour(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
		
        ChartSeries barreGraphique = new ChartSeries();
        barreGraphique.setLabel(titre);

        List<InfosJours> listJoursPeriode = new ArrayList<InfosJours>();
        listJoursPeriode = LibDate.listeJoursEntreDeuxDate(dateDebut, dateFin);

        // complète la liste des jours manquant dans la requête puisque celle-ci ne remonte que les jours qui ont 
        //un chiffre d'affaire
        for (InfosJours ligneInfosJour : listJoursPeriode) {
        	DocumentChiffreAffaireDTO objJourAnneeCa = jourOfIndex(ligneInfosJour.getCodeJourMoisAnnee(),listeMontantJmaDocument); 
        	if (objJourAnneeCa!= null ){
//        		jourGraphique.set(ligneInfosJour.getNumeroJour(),objJourAnneeCa.getMtHtCalc());
        		barreGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),objJourAnneeCa.getMtHtCalc());
        	} else {
        		barreGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),0);
        	}

  		}
        
        return barreGraphique;
	}
	
	public LineChartModel addLineSeriesJoursComparaison(LineChartModel lineChart, String titre, String titreComp, List<DocumentChiffreAffaireDTO> listeCaJoursDocument,
			List<DocumentChiffreAffaireDTO> listeCaJoursDocumentComp, Date premierJourMois, Date dernierJourMois, Date premierJourMoisComp, Date dernierJourMoisComp) {
		LineChartSeries serie = new LineChartSeries();
		LineChartSeries serieComp = new LineChartSeries();
		
		grandeListeGraphiqueJour.clear();
		petiteListeGraphiqueJour.clear();
		
		
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTime(premierJourMois);
		int moisInt = mCalendar.get(Calendar.MONTH);
		String mois = LibDate.moisStringPourMoisInt(moisInt);
		
		/**etape 1 : construire deux listes d'infosJours avec les valeurs**/
		
		 List<InfosJours> listJoursPeriode = new ArrayList<InfosJours>();
	     listJoursPeriode = LibDate.listeJoursEntreDeuxDate(premierJourMois, dernierJourMois);
	     for (InfosJours ligneInfosJour : listJoursPeriode) {
	    	 DocumentChiffreAffaireDTO objJourAnneeCa = jourOfIndex(ligneInfosJour.getCodeJourMoisAnnee(),listeCaJoursDocument);
	    	 if (objJourAnneeCa!= null ){
	    		 ligneInfosJour.setMontant(objJourAnneeCa.getMtHtCalc());
	    	 }else {
	    		 ligneInfosJour.setMontant(BigDecimal.ZERO);
	    	 }
	     }
	     
	     
	     List<InfosJours> listJoursPeriodeComp = new ArrayList<InfosJours>();
	     serie.setLabel(titre);
			
	     if(premierJourMoisComp != null && dernierJourMoisComp != null) {
	    	 listJoursPeriodeComp = LibDate.listeJoursEntreDeuxDate(premierJourMoisComp, dernierJourMoisComp);
		     
		     for (InfosJours ligneInfosJour : listJoursPeriodeComp) {
		    	 DocumentChiffreAffaireDTO objJourAnneeCa = jourOfIndex(ligneInfosJour.getCodeJourMoisAnnee(),listeCaJoursDocumentComp);
		    	 if (objJourAnneeCa!= null ){
		    		 ligneInfosJour.setMontant(objJourAnneeCa.getMtHtCalc());
		    	 }else {
		    		 ligneInfosJour.setMontant(BigDecimal.ZERO);
		    	 }
		     }
		     serieComp.setLabel(titreComp);
	     }else {
	    	 serieComp.setLabel("pas de données");//a voir
	     }
	     
	    
	     
		/**Fin etape1**/
	     
	     
	     int nbJours = 30;
	     if(listJoursPeriode.size()>30 || listJoursPeriodeComp.size() > 30) {
	    	  nbJours = 31;
	     }
		
	
		
		for (int i = 0; i <= nbJours-1; i++) {
				serie.set(listJoursPeriode.get(i).getCodeJour()+" "+mois,listJoursPeriode.get(i).getMontant());
				InfosJours infoJours1 = new InfosJours();
				infoJours1.setDateDuJour(listJoursPeriode.get(i).getDateDuJour());
				InfosJours infoJours2 = new InfosJours();
				
				
				if(listJoursPeriodeComp.isEmpty()) {
					serieComp.set(listJoursPeriode.get(i).getCodeJour()+" "+mois,0);
					infoJours2.setDateDuJour(null);
				}else {
					serieComp.set(listJoursPeriodeComp.get(i).getCodeJour()+" "+mois,listJoursPeriodeComp.get(i).getMontant());
					infoJours2.setDateDuJour(listJoursPeriodeComp.get(i).getDateDuJour());
				}
				
				grandeListeGraphiqueJour.add(infoJours1);
				petiteListeGraphiqueJour.add(infoJours2);
				
		}
		lineChart.addSeries(serie);
		lineChart.addSeries(serieComp);
		return lineChart;
	}
	
	
	public ChartSeries createBarChartSerieFamilleArticle(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
        ChartSeries barreGraphique = new ChartSeries();
        barreGraphique.setLabel(titre);
        

        for (DocumentChiffreAffaireDTO docFamille : listeMontantJmaDocument) {
        	if(docFamille.getLibcFamille().length() > Const.C_NB_CHAR_LIBELLE_FAMILLE_CHART) {
        		barreGraphique.set(StringUtils.abbreviate(docFamille.getLibcFamille(), Const.C_NB_CHAR_LIBELLE_FAMILLE_CHART),docFamille.getMtHtCalc());
        	}else{
        		barreGraphique.set(docFamille.getLibcFamille(),docFamille.getMtHtCalc());
        		
        	}       	
        		
        		
		}
        
        return barreGraphique;
	}
	
	/*Utiliser dans le dash par tiers, onglet articles achetés*/
	public ChartSeries createBarChartSerieParArticle(String titre, List<TaArticlesParTiersDTO> listeArticle, Date dateDebut, Date dateFin){
        ChartSeries barreGraphique = new ChartSeries();
        barreGraphique.setLabel(titre);
        

        for (TaArticlesParTiersDTO article : listeArticle) {
        	if(article.getLibellecArticle().length() > Const.C_NB_CHAR_LIBELLE_FAMILLE_CHART) {
        		barreGraphique.set(StringUtils.abbreviate(article.getLibellecArticle(), Const.C_NB_CHAR_LIBELLE_FAMILLE_CHART),article.getMtHtLDocument());
        	}else{
        		barreGraphique.set(article.getLibellecArticle(),article.getMtHtLDocument());
        		
        	}       	
        		
        		
		}
        
        return barreGraphique;
	}
	
	

	protected BarChartModel createBarModel(Date dateDebut, Date dateFin, BarChartModel barChartModelDocument, String nomTypeDocument ,int precision){
		barChartModelDocument = createBarModel(dateDebut, dateFin, barChartModelDocument, nomTypeDocument, precision, "tous",15);
		return barChartModelDocument;
	}
	protected BarChartModel createBarModel(Date dateDebut, Date dateFin, BarChartModel barChartModelDocument, String nomTypeDocument ,int precision, String etatDocument, int deltaNbJours ) {
//        barModelFactureExo = initBarModelFActureExo();
		
		
			barChartModelDocument = initBarModelDocumentEntreDeuxDate(dateDebut, dateFin, barChartModelDocument, nomTypeDocument, precision, etatDocument, deltaNbJours);
		
		barChartModelDocument.setTitle("Totaux HT mensuel");
		barChartModelDocument.setLegendPosition("ne");
		barChartModelDocument.setAnimate(true);
         
        Axis xAxis = barChartModelDocument.getAxis(AxisType.X);
//        xAxis.setLabel("Gender");
         
        Axis yAxis = barChartModelDocument.getAxis(AxisType.Y);
//        yAxis.setLabel("Births");
        yAxis.setMin(0);
//        yAxis.setMax(1000);
        xAxis.setTickAngle(-50);
        return barChartModelDocument;
    }
	
// 						 /////////////////////////////////////////////////////////////////
// 									 MODIFIER LE 06/09/2017 Jean Marc
	
	protected BarChartModel createBarModelJour(Date dateDebut, Date dateFin, String codeMoisAnnee, BarChartModel barChartModelJourDocument, String nomTypeDocument ,int precision){
		barChartModelJourDocument = createBarModelJour(dateDebut, dateFin, barChartModelJourDocument, nomTypeDocument, precision, "tous",15);
		return barChartModelJourDocument;
	}
	
	protected BarChartModel createBarModelJour(Date premierJourMois, Date dernierJourMois, BarChartModel barChartModelJourDocument, String nomTypeDocument ,int precision, String etatDocument, int deltaNbJours ) {

		
		
			barChartModelJourDocument = initBarModelJourDocumentEntreDeuxDate(premierJourMois, dernierJourMois, barChartModelJourDocument, nomTypeDocument, precision, etatDocument, deltaNbJours);
		
		barChartModelJourDocument.setTitle("Totaux HT par jour");
		barChartModelJourDocument.setLegendPosition("ne");
		barChartModelJourDocument.setAnimate(true);
         
        Axis xAxis = barChartModelJourDocument.getAxis(AxisType.X);

         
        Axis yAxis = barChartModelJourDocument.getAxis(AxisType.Y);

        yAxis.setMin(0);
        xAxis.setTickAngle(-50);
        return barChartModelJourDocument;
    }
//												FIN DE MODIFICATION		
//	 					/////////////////////////////////////////////////////////////////
	
	public static String customFormatDate(Date date) {
		if (date != null) {
		    DateFormat format = new SimpleDateFormat(Const.C_DATE_PATTERN);
		    TimeZone timeZone = TimeZone.getTimeZone(Const.C_TIME_ZONE);
		    format.setTimeZone(timeZone);
		    return format.format(date);
		 }
		return "";
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}
	
	public String getInfoEtatDocument() {
		return infoEtatDocument;
	}
	
	public void setInfoEtatDocument(String infoCase) {
		this.infoEtatDocument = infoCase;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaJoursDocument() {
		return listeCaJoursDocument;
	}

	public void setListeCaJoursDocument(List<DocumentChiffreAffaireDTO> listeCaJoursDocument) {
		this.listeCaJoursDocument = listeCaJoursDocument;
	}
	
	public int getNumeroMois() {
		return numeroMois;
	}

	public void setNumeroMois(int numeroMois) {
		this.numeroMois = numeroMois;
	}

	public String getcSbNbDocumentSurPeriode() {
		return cSbNbDocumentSurPeriode;
	}

	public void setcSbNbDocumentSurPeriode(String cSbNbDocumentSurPeriode) {
		this.cSbNbDocumentSurPeriode = cSbNbDocumentSurPeriode;
	}

	public String getcSbNbDocumentTransformeSurPeriode() {
		return cSbNbDocumentTransformeSurPeriode;
	}

	public void setcSbNbDocumentTransformeSurPeriode(String cSbNbDocumentTransformeSurPeriode) {
		this.cSbNbDocumentTransformeSurPeriode = cSbNbDocumentTransformeSurPeriode;
	}

	public String getcSbNbDocumentNonTransformeSurPeriode() {
		return cSbNbDocumentNonTransformeSurPeriode;
	}

	public void setcSbNbDocumentNonTransformeSurPeriode(String cSbNbDocumentNonTransformeSurPeriode) {
		this.cSbNbDocumentNonTransformeSurPeriode = cSbNbDocumentNonTransformeSurPeriode;
	}

	public String getcSbNbDocumentNonTransformeARelancerSurPeriode() {
		return cSbNbDocumentNonTransformeARelancerSurPeriode;
	}

	public void setcSbNbDocumentNonTransformeARelancerSurPeriode(String cSbNbDocumentNonTransformeARelancerSurPeriode) {
		this.cSbNbDocumentNonTransformeARelancerSurPeriode = cSbNbDocumentNonTransformeARelancerSurPeriode;
	}

	public static String getEtatDocumentTous() {
		return ETAT_DOCUMENT_TOUS;
	}

	public static String getEtatDocumentTransorme() {
		return ETAT_DOCUMENT_TRANSORME;
	}

	public static String getEtatDocumentNonTransorme() {
		return ETAT_DOCUMENT_NON_TRANSORME;
	}

	public static String getEtatDocumentNonTransormeARelancer() {
		return ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER;
	}

	public MindmapNode getDocRoot() {
		return docRoot;
	}

	public void setDocRoot(MindmapNode docRoot) {
		this.docRoot = docRoot;
	}

	public String getCodeMoisAnnee() {
		return codeMoisAnnee;
	}

	public void setCodeMoisAnnee(String codeMoisAnnee) {
		this.codeMoisAnnee = codeMoisAnnee;
	}

	public boolean isAfficheDocumentJour() {
		return afficheDocumentJour;
	}

	public void setAfficheDocumentJour(boolean afficheDocumentJour) {
		this.afficheDocumentJour = afficheDocumentJour;
	}

	public List<DocumentDTO> getListeDocumentDetailMois() {
		return listeDocumentDetailMois;
	}

	public void setListeDocumentDetailMois(List<DocumentDTO> listeDocumentDetailMois) {
		this.listeDocumentDetailMois = listeDocumentDetailMois;
	}

	public List<DocumentDTO> getListeDocumentDetailJour() {
		return listeDocumentDetailJour;
	}

	public void setListeDocumentDetailJour(List<DocumentDTO> listeDocumentDetailJour) {
		this.listeDocumentDetailJour = listeDocumentDetailJour;
	}

	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParMois() {
		return listeDocumentArticleParTiersParMois;
	}

	public void setListeDocumentArticleParTiersParMois(List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois) {
		this.listeDocumentArticleParTiersParMois = listeDocumentArticleParTiersParMois;
	}

	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParJour() {
		return listeDocumentArticleParTiersParJour;
	}

	public void setListeDocumentArticleParTiersParJour(List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour) {
		this.listeDocumentArticleParTiersParJour = listeDocumentArticleParTiersParJour;
	}

	public boolean isAfficheDocumentJourDataTable() {
		return afficheDocumentJourDataTable;
	}

	public void setAfficheDocumentJourDataTable(boolean afficheDocumentJourDataTable) {
		this.afficheDocumentJourDataTable = afficheDocumentJourDataTable;
	}

	public String getNomMoisDetail() {
		return nomMoisDetail;
	}

	public void setNomMoisDetail(String nomMoisDetail) {
		this.nomMoisDetail = nomMoisDetail;
	}

	public int getNumeroJourDetail() {
		return numeroJourDetail;
	}

	public void setNumeroJourDetail(int numeroJourDetail) {
		this.numeroJourDetail = numeroJourDetail;
	}

	public BigDecimal getMontantTotalHtMois() {
		return montantTotalHtMois;
	}

	public void setMontantTotalHtMois(BigDecimal montantTotalHtMois) {
		this.montantTotalHtMois = montantTotalHtMois;
	}

	public BigDecimal getMontantTotalHtJour() {
		return montantTotalHtJour;
	}

	public void setMontantTotalHtJour(BigDecimal montantTotalHtJour) {
		this.montantTotalHtJour = montantTotalHtJour;
	}

	public String getInfosLabelRapportMensuelDataTable() {
		return infosLabelRapportMensuelDataTable;
	}

	public void setInfosLabelRapportMensuelDataTable(String infosLabelRapportMensuelDataTable) {
		this.infosLabelRapportMensuelDataTable = infosLabelRapportMensuelDataTable;
	}

	public String getInfosLabelRapportArticlesDataTable() {
		return infosLabelRapportArticlesDataTable;
	}

	public void setInfosLabelRapportArticlesDataTable(String infosLabelRapportArticlesDataTable) {
		this.infosLabelRapportArticlesDataTable = infosLabelRapportArticlesDataTable;
	}

	public String getInfosLabelRapportJournalierDataTable() {
		return infosLabelRapportJournalierDataTable;
	}

	public void setInfosLabelRapportJournalierDataTable(String infosLabelRapportJournalierDataTable) {
		this.infosLabelRapportJournalierDataTable = infosLabelRapportJournalierDataTable;
	}

	public String getInfosLabelRapportArticlesJourDataTable() {
		return infosLabelRapportArticlesJourDataTable;
	}

	public void setInfosLabelRapportArticlesJourDataTable(String infosLabelRapportArticlesJourDataTable) {
		this.infosLabelRapportArticlesJourDataTable = infosLabelRapportArticlesJourDataTable;
	}

	public List<DocumentDTO> getValueLignesDocument() {
		return valueLignesDocument;
	}

	public void setValueLignesDocument(List<DocumentDTO> valueLignesDocument) {
		this.valueLignesDocument = valueLignesDocument;
	}

	public String getCodeEtatDocument() {
		return codeEtatDocument;
	}

	public void setCodeEtatDocument(String codeEtatDocument) {
		this.codeEtatDocument = codeEtatDocument;
	}

	public IDashboardDocumentServiceRemote getTaDocumentService() {
		return taDocumentService;
	}

	public void setTaDocumentService(IDashboardDocumentServiceRemote taDocumentService) {
		this.taDocumentService = taDocumentService;
	}

	public List<DocumentChiffreAffaireDTO> getListeDocumentFactureParTiers() {
		return listeDocumentFactureParTiers;
	}

	public void setListeDocumentFactureParTiers(List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiers) {
		this.listeDocumentFactureParTiers = listeDocumentFactureParTiers;
	}

	public LocalDate getDateDebutComp() {
		return dateDebutComp;
	}

	public void setDateDebutComp(LocalDate dateDebutComp) {
		this.dateDebutComp = dateDebutComp;
	}

	public LocalDate getDateFinComp() {
		return dateFinComp;
	}

	public void setDateFinComp(LocalDate dateFinComp) {
		this.dateFinComp = dateFinComp;
	}

	public boolean isComparaison() {
		return comparaison;
	}

	public void setComparaison(boolean comparaison) {
		this.comparaison = comparaison;
	}

	public List<String> getListeChoixPeriode() {
		return listeChoixPeriode;
	}

	public void setListeChoixPeriode(List<String> listeChoixPeriode) {
		this.listeChoixPeriode = listeChoixPeriode;
	}

	public String getSelectedChoixPeriode() {
		return selectedChoixPeriode;
	}

	public void setSelectedChoixPeriode(String selectedChoixPeriode) {
		this.selectedChoixPeriode = selectedChoixPeriode;
	}
	public List<String> getListeChoixPeriodeComp() {
		return listeChoixPeriodeComp;
	}
	public void setListeChoixPeriodeComp(List<String> listeChoixPeriodeComp) {
		this.listeChoixPeriodeComp = listeChoixPeriodeComp;
	}
	public String getSelectedChoixPeriodeComp() {
		return selectedChoixPeriodeComp;
	}
	public void setSelectedChoixPeriodeComp(String selectedChoixPeriodeComp) {
		this.selectedChoixPeriodeComp = selectedChoixPeriodeComp;
	}
	public List<DocumentDTO> getValueLignesDocumentComp() {
		return valueLignesDocumentComp;
	}
	public void setValueLignesDocumentComp(List<DocumentDTO> valueLignesDocumentComp) {
		this.valueLignesDocumentComp = valueLignesDocumentComp;
	}
	public List<InfosMois> getListMoisAnneeExoComp() {
		return listMoisAnneeExoComp;
	}
	public void setListMoisAnneeExoComp(List<InfosMois> listMoisAnneeExoComp) {
		this.listMoisAnneeExoComp = listMoisAnneeExoComp;
	}
	public List<InfosJours> getListJoursAnneeExoComp() {
		return listJoursAnneeExoComp;
	}
	public void setListJoursAnneeExoComp(List<InfosJours> listJoursAnneeExoComp) {
		this.listJoursAnneeExoComp = listJoursAnneeExoComp;
	}
	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentComp() {
		return listeCaMoisDocumentComp;
	}
	public void setListeCaMoisDocumentComp(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentComp) {
		this.listeCaMoisDocumentComp = listeCaMoisDocumentComp;
	}
	public List<DocumentChiffreAffaireDTO> getListeCaJoursDocumentComp() {
		return listeCaJoursDocumentComp;
	}
	public void setListeCaJoursDocumentComp(List<DocumentChiffreAffaireDTO> listeCaJoursDocumentComp) {
		this.listeCaJoursDocumentComp = listeCaJoursDocumentComp;
	}
	public List<DocumentChiffreAffaireDTO> getListeDocumentFactureParTiersComp() {
		return listeDocumentFactureParTiersComp;
	}
	public void setListeDocumentFactureParTiersComp(List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiersComp) {
		this.listeDocumentFactureParTiersComp = listeDocumentFactureParTiersComp;
	}
	public List<DocumentDTO> getListeDocumentDetailMoisComp() {
		return listeDocumentDetailMoisComp;
	}
	public void setListeDocumentDetailMoisComp(List<DocumentDTO> listeDocumentDetailMoisComp) {
		this.listeDocumentDetailMoisComp = listeDocumentDetailMoisComp;
	}
	public List<DocumentDTO> getListeDocumentDetailJourComp() {
		return listeDocumentDetailJourComp;
	}
	public void setListeDocumentDetailJourComp(List<DocumentDTO> listeDocumentDetailJourComp) {
		this.listeDocumentDetailJourComp = listeDocumentDetailJourComp;
	}
	public boolean isChevauchementPeriode() {
		return chevauchementPeriode;
	}
	public void setChevauchementPeriode(boolean chevauchementPeriode) {
		this.chevauchementPeriode = chevauchementPeriode;
	}
	public Date getDateDebutGrande() {
		return dateDebutGrande;
	}
	public void setDateDebutGrande(Date dateDebutGrande) {
		this.dateDebutGrande = dateDebutGrande;
	}
	public Date getDateFinGrande() {
		return dateFinGrande;
	}
	public void setDateFinGrande(Date dateFinGrande) {
		this.dateFinGrande = dateFinGrande;
	}
	public Date getDateDebutPetite() {
		return dateDebutPetite;
	}
	public void setDateDebutPetite(Date dateDebutPetite) {
		this.dateDebutPetite = dateDebutPetite;
	}
	public Date getDateFinPetite() {
		return dateFinPetite;
	}
	public void setDateFinPetite(Date dateFinPetite) {
		this.dateFinPetite = dateFinPetite;
	}
	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParMoisComp() {
		return listeDocumentArticleParTiersParMoisComp;
	}
	public void setListeDocumentArticleParTiersParMoisComp(
			List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMoisComp) {
		this.listeDocumentArticleParTiersParMoisComp = listeDocumentArticleParTiersParMoisComp;
	}
	public List<InfosMois> getGrandeListeGraphiqueMensuel() {
		return grandeListeGraphiqueMensuel;
	}
	public void setGrandeListeGraphiqueMensuel(List<InfosMois> grandeListeGraphiqueMensuel) {
		this.grandeListeGraphiqueMensuel = grandeListeGraphiqueMensuel;
	}
	public List<InfosMois> getPetiteListeGraphiqueMensuel() {
		return petiteListeGraphiqueMensuel;
	}
	public void setPetiteListeGraphiqueMensuel(List<InfosMois> petiteListeGraphiqueMensuel) {
		this.petiteListeGraphiqueMensuel = petiteListeGraphiqueMensuel;
	}
	public BigDecimal getMontantTotalHtMoisComp() {
		return montantTotalHtMoisComp;
	}
	public void setMontantTotalHtMoisComp(BigDecimal montantTotalHtMoisComp) {
		this.montantTotalHtMoisComp = montantTotalHtMoisComp;
	}
	public BigDecimal getMontantTotalHtJourComp() {
		return montantTotalHtJourComp;
	}
	public void setMontantTotalHtJourComp(BigDecimal montantTotalHtJourComp) {
		this.montantTotalHtJourComp = montantTotalHtJourComp;
	}
	public String getNomMois() {
		return nomMois;
	}
	public void setNomMois(String nomMois) {
		this.nomMois = nomMois;
	}
	public String getNomMoisDetailComp() {
		return nomMoisDetailComp;
	}
	public void setNomMoisDetailComp(String nomMoisDetailComp) {
		this.nomMoisDetailComp = nomMoisDetailComp;
	}
	public String getInfosLabelRapportMensuelDataTableComp() {
		return infosLabelRapportMensuelDataTableComp;
	}
	public void setInfosLabelRapportMensuelDataTableComp(String infosLabelRapportMensuelDataTableComp) {
		this.infosLabelRapportMensuelDataTableComp = infosLabelRapportMensuelDataTableComp;
	}
	public String getInfosLabelRapportArticlesDataTableComp() {
		return infosLabelRapportArticlesDataTableComp;
	}
	public void setInfosLabelRapportArticlesDataTableComp(String infosLabelRapportArticlesDataTableComp) {
		this.infosLabelRapportArticlesDataTableComp = infosLabelRapportArticlesDataTableComp;
	}
	public String getInfosLabelRapportJournalierDataTableComp() {
		return infosLabelRapportJournalierDataTableComp;
	}
	public void setInfosLabelRapportJournalierDataTableComp(String infosLabelRapportJournalierDataTableComp) {
		this.infosLabelRapportJournalierDataTableComp = infosLabelRapportJournalierDataTableComp;
	}
	public String getInfosLabelRapportArticlesJourDataTableComp() {
		return infosLabelRapportArticlesJourDataTableComp;
	}
	public void setInfosLabelRapportArticlesJourDataTableComp(String infosLabelRapportArticlesJourDataTableComp) {
		this.infosLabelRapportArticlesJourDataTableComp = infosLabelRapportArticlesJourDataTableComp;
	}
	public int getNumeroJourDetailComp() {
		return numeroJourDetailComp;
	}
	public void setNumeroJourDetailComp(int numeroJourDetailComp) {
		this.numeroJourDetailComp = numeroJourDetailComp;
	}
	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParJourComp() {
		return listeDocumentArticleParTiersParJourComp;
	}
	public void setListeDocumentArticleParTiersParJourComp(
			List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJourComp) {
		this.listeDocumentArticleParTiersParJourComp = listeDocumentArticleParTiersParJourComp;
	}
	public List<InfosJours> getGrandeListeGraphiqueJour() {
		return grandeListeGraphiqueJour;
	}
	public void setGrandeListeGraphiqueJour(List<InfosJours> grandeListeGraphiqueJour) {
		this.grandeListeGraphiqueJour = grandeListeGraphiqueJour;
	}
	public List<InfosJours> getPetiteListeGraphiqueJour() {
		return petiteListeGraphiqueJour;
	}
	public void setPetiteListeGraphiqueJour(List<InfosJours> petiteListeGraphiqueJour) {
		this.petiteListeGraphiqueJour = petiteListeGraphiqueJour;
	}
	public List<TaEditionDTO> getListeEdition() {
		return listeEdition;
	}
	public void setListeEdition(List<TaEditionDTO> listeEdition) {
		this.listeEdition = listeEdition;
	}

	
//	public void onMoreTextArticle(AjaxBehaviorEvent event) {
//	    AutoComplete ac = (AutoComplete) event.getSource();
//	    ac.setMaxResults(ac.getMaxResults() + taPreferencesService.nbMaxChargeListeArticle());
//	   if(queryArticle==null) {
//		   queryArticle = "*";
//	   }
//	    PrimeFaces.current().executeScript("PF('"+ ac.getWidgetVar()+"').search('" + queryArticle+ "')");
//	}
//
//	public void onMoreTextTiers(AjaxBehaviorEvent event) {
//	    AutoComplete ac = (AutoComplete) event.getSource();
//	    ac.setMaxResults(ac.getMaxResults() + taPreferencesService.nbMaxChargeListeTiers());
//	   if(queryTiers==null) {
//		   queryTiers = "*";
//	   }
//	    PrimeFaces.current().executeScript("PF('"+ ac.getWidgetVar()+"').search('" + queryTiers+ "')");
//	}
	

	public boolean isGrosFichierArticles() {
		return grosFichierArticles;
	}

	public void setGrosFichierArticles(boolean grosFichierArticles) {
		this.grosFichierArticles = grosFichierArticles;
	}
	public boolean isGrosFichierTiers() {
		return grosFichierTiers;
	}
	public void setGrosFichierTiers(boolean grosFichierTiers) {
		this.grosFichierTiers = grosFichierTiers;
	}

	
	   
	public String getInfoLabel() {
		return infoLabel;
	}
	public void setInfoLabel(String infoLabel) {
		this.infoLabel = infoLabel;
	}


	public List<DocumentChiffreAffaireDTO> rechercheMemeDateDansListe(DocumentChiffreAffaireDTO valeur,List<DocumentChiffreAffaireDTO> listeDest) {
		for (DocumentChiffreAffaireDTO l : listeDest) {
			if((l.getJour().compareTo(valeur.getJour())==0)&&(l.getMois().compareTo(valeur.getMois())==0)&&(l.getAnnee().compareTo(valeur.getAnnee())==0)) {
    			l.setMtHtCalc(l.getMtHtCalc().add(valeur.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			l.setMtTvaCalc(l.getMtTvaCalc().add(valeur.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			l.setMtTtcCalc(l.getMtTtcCalc().add(valeur.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			l.setReglementComplet(l.getReglementComplet().add(valeur.getReglementComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			l.setResteAReglerComplet(l.getResteAReglerComplet().add(valeur.getResteAReglerComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			l.setQteLDocument(l.getQteLDocument().add(valeur.getQteLDocument().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			l.setQte2LDocument(l.getQte2LDocument().add(valeur.getQte2LDocument().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			l.setQteSaisieLDocument(l.getQteSaisieLDocument().add(valeur.getQteSaisieLDocument().setScale(2,BigDecimal.ROUND_HALF_UP)));

    			l.setCount(l.getCount()+valeur.getCount());

    			return listeDest;
			}
		}	
		//si arrive ici, c'est que la date n'exite pas encore dans la liste, donc on la rajoute
		listeDest.add(valeur);
		return listeDest;	
	}
		

}
