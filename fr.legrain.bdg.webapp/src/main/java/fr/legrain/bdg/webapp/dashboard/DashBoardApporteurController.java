package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.BarChartModel;

import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardApporteurController extends DashBoardDocumentController implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7098891768436512286L;
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1363000298058005675L;
	/**
	 * 
	 */
	
	private @EJB ITaApporteurServiceRemote taApporteurService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    public List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelApporteur;
    private BarChartModel barChartModelApporteurJour;

    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
//    private String codeEtatApporteur;
    private DocumentChiffreAffaireDTO totauxCaPeriodeApporteur;
    private DocumentChiffreAffaireDTO totauxCaPeriodeApporteurNonTransforme;
    private DocumentChiffreAffaireDTO totauxCaPeriodeApporteurNonTransformeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeApporteurTransforme;
    
    private List<DocumentDTO> valueLignesApporteur = new ArrayList<DocumentDTO>();
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument = new ArrayList<DocumentChiffreAffaireDTO>();// Récupère le ca ht par mois dans un liste
    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument =  new ArrayList<DocumentChiffreAffaireDTO>();// Récupère le ca ht par jour dans un liste
    
    private boolean afficheApporteurJour; //AJOUTER LE 06/09/2017 Jean Marc
    private boolean afficheApporteurJourDataTable; //AJOUTER LE 06/09/2017 Jean Marc
    
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeApporteur= new ArrayList<DocumentChiffreAffaireDTO>();
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeApporteurNonTransforme= new ArrayList<DocumentChiffreAffaireDTO>();
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeApporteurNonTransformeARelancer= new ArrayList<DocumentChiffreAffaireDTO>();
    private int nbApporteurSurPeriode = 0;
    private int nbApporteurNonTransformeSurPeriode = 0 ;
    private int nbApporteurTransformeSurPeriode = 0;
    private int nbApporteurNonTransformeARelancerSurPeriode = 0;

    
//    private List<DocumentDTO> listeDocumentDetailMois;
//    private List<DocumentDTO> listeDocumentDetailJour;
//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois;
//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour;
    
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;

//    private String nomMoisDetail;
//    private int numeroJourDetail; 
//    private String infosLabelRapportMensuelDataTable; 
//    private String infosLabelRapportArticlesDataTable; 
//    private String infosLabelRapportJournalierDataTable; 
//    private String infosLabelRapportArticlesJourDataTable; 
    
    
//    private BigDecimal montantTotalHtMois = BigDecimal.ZERO;
//    private BigDecimal montantTotalHtJour = BigDecimal.ZERO;
    
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
//    	codeEtatApporteur = "tous";
//
//    	listeDocumentDetailMois = new ArrayList<DocumentDTO>();
//    	listeDocumentDetailJour = new ArrayList<DocumentDTO>();
//    	listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
//    	listeDocumentArticleParTiersParJour = new ArrayList<TaArticlesParTiersDTO>();   
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	//Initialisation des données sur la période exercice
    	actRechercheApporteur(codeEtatDocument);
//    	createBarModel();
    }


    public void refreshDashBoard(String etatApporteur){
    	setAfficheDocumentJour(false); 
    	setAfficheDocumentJourDataTable(false);
    	listeDocumentDetailMois.clear(); 
    	listeDocumentDetailJour.clear(); 
    	listeDocumentArticleParTiersParMois.clear(); 
    	listeDocumentArticleParTiersParJour.clear(); 
    	actRechercheApporteur(etatApporteur);
    }

    public void actRechercheApporteur(String codeEtatApporteur){
    	setCodeEtatDocument(codeEtatApporteur);
    	setInfoEtatDocument(codeEtatApporteur);
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));

    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeApporteur = taApporteurService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
//		totauxCaPeriodeApporteurNonTransforme = taApporteurService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
//		totauxCaPeriodeApporteurNonTransformeARelancer = taApporteurService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
//		totauxCaPeriodeApporteurTransforme = taApporteurService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Retourne le nombre total de documents sur la période
		nbApporteurSurPeriode = (int) taApporteurService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma non transformés sur la période
//		nbApporteurNonTransformeSurPeriode = (int) taApporteurService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
//		nbApporteurNonTransformeARelancerSurPeriode = (int) taApporteurService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		// Retourne le nombre de Proforma transformés sur la période
//		nbApporteurTransformeSurPeriode = (int) taApporteurService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		
		if (nbApporteurSurPeriode > 0) {
//			try {
//				tauxTransfoNb =  (new BigDecimal(nbApporteurTransformeSurPeriode) .divide(new BigDecimal(nbApporteurSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//				tauxTransfoCa = totauxCaPeriodeApporteurTransforme.getMtHtCalc() .divide(totauxCaPeriodeApporteur.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//			} catch (Exception e) {
//				e.printStackTrace();
//				// TODO: handle exception
//			}
		}    	

	    switch (codeEtatApporteur) {
		case ETAT_DOCUMENT_TOUS:
//			// Renvoi la liste des documents présents sur la période en cours
			valueLignesDocument = taApporteurService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelApporteur = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taApporteurService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelApporteur.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Apporteur");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_APPORTEUR, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
				break;

//		case ETAT_DOCUMENT_NON_TRANSORME:
//			valueLignesDocument = taApporteurService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
//			barChartModelApporteur = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taApporteurService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
//			barChartModelApporteur.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			initInfosLabelRapportMensuelDataTable("Apporteur");
//			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL,
//								CSS_DBOARD_BLOC_ACTIF_APPORTEUR, 
//								CSS_DBOARD_BLOC_INACTIF_ALL, 
//								CSS_DBOARD_BLOC_INACTIF_ALL);			
//			
//			
//			break;			
//		case ETAT_DOCUMENT_TRANSORME:
//			valueLignesDocument = taApporteurService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
//			barChartModelApporteur = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taApporteurService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
//			barChartModelApporteur.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			initInfosLabelRapportMensuelDataTable("Apporteur");
//
//			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
//			  		  			CSS_DBOARD_BLOC_INACTIF_ALL, 
//			  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
//			  		  			CSS_DBOARD_BLOC_ACTIF_APPORTEUR);
//
//			break;		
//
//
//		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
//			valueLignesDocument = taApporteurService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
//			barChartModelApporteur = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taApporteurService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1, 15,null);
//			barChartModelApporteur.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//
//			initInfosLabelRapportMensuelDataTable("Apporteur");
//			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
//	  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
//	  		  		  			CSS_DBOARD_BLOC_ACTIF_APPORTEUR, 
//	  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL);
//
//			break;
			
			default:
				break;
			} 	
    }
    
    
    
    public void itemSelectMois(ItemSelectEvent event) {

		Date premierJourMois = listMoisAnneeExo.get(event.getItemIndex()).getPremierJourMois();
		Date dernierJourMois = listMoisAnneeExo.get(event.getItemIndex()).getDernierJourMois();
        listJoursAnneeExo = LibDate.listeJoursEntreDeuxDate(premierJourMois, dernierJourMois);
		nomMoisDetail = listMoisAnneeExo.get(event.getItemIndex()).getNomLong()+" "+listMoisAnneeExo.get(event.getItemIndex()).getAnnee();
		setCodeMoisAnnee(listMoisAnneeExo.get(event.getItemIndex()).getCodeMoisAnnee());
		setNumeroMois(event.getItemIndex());
//		setAfficheApporteurJour(true);
		setAfficheDocumentJour(true);
		setAfficheDocumentJourDataTable(false);
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Apporteur");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taApporteurService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Apporteur");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taApporteurService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taApporteurService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taApporteurService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taApporteurService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelApporteurJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelApporteurJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
//	case ETAT_DOCUMENT_NON_TRANSORME:
//		// Cumul du chiffre d'affaire par mois
//		montantTotalHtMois = taApporteurService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
////		etatInfosLabelRapportMensuelDataTable();
//		initInfosLabelRapportMensuelDataTable("Apporteur");
//		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
//		listeDocumentDetailMois= taApporteurService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
//		listeDocumentArticleParTiersParMois = taApporteurService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
//		listeCaJoursDocument = taApporteurService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
//		barChartModelApporteurJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//		barChartModelApporteurJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
//	
//		break;
		
//	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
//		montantTotalHtMois = taApporteurService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
////		etatInfosLabelRapportMensuelDataTable();
//		initInfosLabelRapportMensuelDataTable("Apporteur");
//		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
//		listeDocumentDetailMois= taApporteurService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
//		listeDocumentArticleParTiersParMois = taApporteurService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
//		listeCaJoursDocument = taApporteurService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
//		barChartModelApporteurJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//		barChartModelApporteurJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
//		
//		break;
		
//	case ETAT_DOCUMENT_TRANSORME:
//		// Cumul du chiffre d'affaire par mois
//		montantTotalHtMois = taApporteurService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
////		etatInfosLabelRapportMensuelDataTable();
//		initInfosLabelRapportMensuelDataTable("Apporteur");
//		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
//		listeDocumentDetailMois= taApporteurService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
//		listeDocumentArticleParTiersParMois = taApporteurService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
//		listeCaJoursDocument = taApporteurService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
//		barChartModelApporteurJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//		barChartModelApporteurJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
//		
//		break;
	}
   }



//	 						/////////////////////////////////////////////////////////////////
//									MODIFIER LE 06/09/2017 Jean Marc	
	
	public void itemSelectJour(ItemSelectEvent event2) throws ParseException {

		setNumeroJourDetail(event2.getItemIndex());
		Date jourMois = listJoursAnneeExo.get(event2.getItemIndex()).getDateDuJour();
		setAfficheDocumentJourDataTable(true);
		
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par jour
	montantTotalHtJour = taApporteurService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taApporteurService.findAllDTOPeriode(jourMois, jourMois,null);
	setListeDocumentArticleParTiersParJour(taApporteurService.findArticlesParTiersParMois(jourMois, jourMois));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Apporteur");
	break;
	
//	case ETAT_DOCUMENT_NON_TRANSORME:
//		// Cumul du chiffre d'affaire par Jour
//		montantTotalHtJour = taApporteurService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
//		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
//		listeDocumentDetailJour= taApporteurService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
//		setListeDocumentArticleParTiersParJour(taApporteurService.findArticlesParTiersNonTransforme(jourMois, jourMois));
////		etatInfosLabelRapportMensuelDataTable();
//		initInfosLabelRapportMensuelDataTable("Apporteur");
//		break;
		
//	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
//		// Cumul du chiffre d'affaire par Jour
//		montantTotalHtJour = taApporteurService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
//		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
//		listeDocumentDetailJour= taApporteurService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
//		setListeDocumentArticleParTiersParJour(taApporteurService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
////		etatInfosLabelRapportMensuelDataTable();
//		initInfosLabelRapportMensuelDataTable("Apporteur");
//		break;
		
//	case ETAT_DOCUMENT_TRANSORME:
//		// Cumul du chiffre d'affaire par Jour
////		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaApporteur.TYPE_DOC, jourMois, jourMois);
////		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
//		montantTotalHtJour = taApporteurService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
//		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
//		listeDocumentDetailJour= taApporteurService.findDocumentTransfosDTO(jourMois, jourMois,null);
//		setListeDocumentArticleParTiersParJour(taApporteurService.findArticlesParTiersTransforme(jourMois, jourMois));
////		etatInfosLabelRapportMensuelDataTable();
//		initInfosLabelRapportMensuelDataTable("Apporteur");
//		break;
	}
		
    }
    
    
    
    
    
    public void actImprimerListeApporteur(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", "Liste des apporteurs");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeApporteur", valueLignesDocument);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
public void actImprimerListeDocMois(ActionEvent actionEvent) {
    	
    	
    	try {
    				
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    	
    	Map<String, Object> sessionMap = externalContext.getSessionMap();
    	
    	Map<String,Object> mapParametre = new HashMap<String,Object>();
    	mapParametre.put("infosLabel", infosLabelRapportMensuelDataTable);
    	
//    	sessionMap.put("debut", getDateDebut());
//    	sessionMap.put("fin", getDateFin());
    	sessionMap.put("parametre", mapParametre);
    	sessionMap.put("listeDocMois", listeDocumentDetailMois);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    	
    	public void actImprimerListeArticleMois(ActionEvent actionEvent) {
    		
    		
    		try {
    					
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    		Map<String, Object> sessionMap = externalContext.getSessionMap();
    		Map<String, Object> sessionMapInfos = externalContext.getSessionMap();
//    		Map<String,Object> mapParametre = new HashMap<String,Object>();
    	
//    		mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
    		
//    		sessionMap.put("parametre", mapParametre);
    		sessionMapInfos.put("infosLabel", getInfosLabelRapportArticlesDataTable());
    		sessionMap.put("listeArticleMois", listeDocumentArticleParTiersParMois);

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	public void actImprimerListeDocJour(ActionEvent actionEvent) {
    		
    		
    		try {
    					
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    		
    		Map<String, Object> sessionMap = externalContext.getSessionMap();
    		
    		Map<String,Object> mapParametre = new HashMap<String,Object>();
    		mapParametre.put("infosLabel", infosLabelRapportJournalierDataTable);
    		
//    		sessionMap.put("debut", getDateDebut());
//    		sessionMap.put("fin", getDateFin());
    		sessionMap.put("parametre", mapParametre);
    		sessionMap.put("listeDocJour", listeDocumentDetailJour);

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    		
    		public void actImprimerListeArticleJour(ActionEvent actionEvent) {
    			
    			
    			try {
    						
    			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    			Map<String, Object> sessionMap = externalContext.getSessionMap();
    			Map<String, Object> sessionMapInfos = externalContext.getSessionMap();
//    			Map<String,Object> mapParametre = new HashMap<String,Object>();
    		
//    			mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
    			
//    			sessionMap.put("parametre", mapParametre);
    			sessionMapInfos.put("infosLabel", infosLabelRapportArticlesJourDataTable);
    			sessionMap.put("listeArticleJour", listeDocumentArticleParTiersParJour);

    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		
//    private void etatInfosLabelRapportMensuelDataTable() {
//    	
//    	if (afficheApporteurJour==true) {
//    		setInfosLabelRapportMensuelDataTable("Liste des Apporteurs du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
//    		setInfosLabelRapportArticlesDataTable("Liste des lignes article du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
//    		setInfosLabelRapportJournalierDataTable("Liste des Apporteurs du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
//    		setInfosLabelRapportArticlesJourDataTable("Liste des lignes article du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
//    	}
//    	else {
//    		setInfosLabelRapportMensuelDataTable("Cliquez sur une barre du graphique pour avoir le détail mensuel des ventes");
//    		setInfosLabelRapportArticlesDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
//    		setInfosLabelRapportJournalierDataTable("Cliquez sur une barre du graphique pour avoir le détail journalier des ventes");
//    		setInfosLabelRapportArticlesJourDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
//    	}
//    	
//    	if (valueLignesApporteur.isEmpty()){
//    		setInfosLabelRapportMensuelDataTable("La liste des Apporteur "+infoEtatDocument+" est vide");
//    		setInfosLabelRapportArticlesDataTable("La liste des articles "+infoEtatDocument+" est vide");
//    		setInfosLabelRapportJournalierDataTable("La liste des Apporteur "+infoEtatDocument+" est vide");
//    	}
//    	}

    public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelApporteur() {
		return barChartModelApporteur;
	}
	public void setBarChartModelApporteurExo(BarChartModel barChartModelApporteur) {
		this.barChartModelApporteur = barChartModelApporteur;
	}
	
	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}


	public List<DocumentDTO> getValueLignesApporteur() {
		return valueLignesApporteur;
	}

	public void setValueLignesApporteur(List<DocumentDTO> valueLignesApporteur) {
		this.valueLignesApporteur = valueLignesApporteur;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeApporteur() {
		return listeCaPeriodeApporteur;
	}

	public void setListeCaPeriodeApporteur(List<DocumentChiffreAffaireDTO> listeCaPeriodeApporteur) {
		this.listeCaPeriodeApporteur = listeCaPeriodeApporteur;
	}

	public int getNbApporteurSurPeriode() {
		return nbApporteurSurPeriode;
	}

	public void setNbApporteurSurPeriode(int nbApporteurSurPeriode) {
		this.nbApporteurSurPeriode = nbApporteurSurPeriode;
	}

	public int getNbApporteurNonTransformeSurPeriode() {
		return nbApporteurNonTransformeSurPeriode;
	}

	public void setNbApporteurNonTransformeSurPeriode(int nbApporteurNonTransformeSurPeriode) {
		this.nbApporteurNonTransformeSurPeriode = nbApporteurNonTransformeSurPeriode;
	}


	public int getNbApporteurNonTransformeARelancerSurPeriode() {
		return nbApporteurNonTransformeARelancerSurPeriode;
	}


	public void setNbApporteurNonTransformeARelancerSurPeriode(int nbApporteurNonTransformeARelancerSurPeriode) {
		this.nbApporteurNonTransformeARelancerSurPeriode = nbApporteurNonTransformeARelancerSurPeriode;
	}



	public String getOptionPeriodePrecedente() {
		return optionPeriodePrecedente;
	}


	public void setOptionPeriodePrecedente(String optionPeriodePrecedente) {
		this.optionPeriodePrecedente = optionPeriodePrecedente;
	}


	public BigDecimal getTauxTransfoNb() {
		return tauxTransfoNb;
	}


	public void setTauxTransfoNb(BigDecimal tauxTransfoNb) {
		this.tauxTransfoNb = tauxTransfoNb;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeApporteurNonTransforme() {
		return listeCaPeriodeApporteurNonTransforme;
	}


	public void setListeCaPeriodeApporteurNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeApporteurNonTransforme) {
		this.listeCaPeriodeApporteurNonTransforme = listeCaPeriodeApporteurNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbApporteurTransformeSurPeriode() {
		return nbApporteurTransformeSurPeriode;
	}


	public void setNbApporteurTransformeSurPeriode(int nbApporteurTransformeSurPeriode) {
		this.nbApporteurTransformeSurPeriode = nbApporteurTransformeSurPeriode;
	}


	public BigDecimal getMontantTotalHtNonTransfoARelancer() {
		return montantTotalHtNonTransfoARelancer;
	}


	public void setMontantTotalHtNonTransfoARelancer(BigDecimal montantTotalHtNonTransfoARelancer) {
		montantTotalHtNonTransfoARelancer = montantTotalHtNonTransfoARelancer;
	}


	public BigDecimal getMontantTotalHtTransfo() {
		return montantTotalHtTransfo;
	}


	public void setMontantTotalHtTransfo(BigDecimal montantTotalHtTransfo) {
		montantTotalHtTransfo = montantTotalHtTransfo;
	}



	public BigDecimal getMontantTotalHt() {
		return montantTotalHt;
	}


	public void setMontantTotalHt(BigDecimal montantTotalHt) {
		montantTotalHt = montantTotalHt;
	}


	public BigDecimal getMontantTotalHtNonTransfo() {
		return montantTotalHtNonTransfo;
	}


	public void setMontantTotalHtNonTransfo(BigDecimal montantTotalHtNonTransfo) {
		montantTotalHtNonTransfo = montantTotalHtNonTransfo;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeApporteurNonTransformeARelancer() {
		return listeCaPeriodeApporteurNonTransformeARelancer;
	}


	public void setListeCaPeriodeApporteurNonTransformeARelancer(
			List<DocumentChiffreAffaireDTO> listeCaPeriodeApporteurNonTransformeARelancer) {
		this.listeCaPeriodeApporteurNonTransformeARelancer = listeCaPeriodeApporteurNonTransformeARelancer;
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


	public void setBarChartModelApporteur(BarChartModel barChartModelApporteur) {
		this.barChartModelApporteur = barChartModelApporteur;
	}


	public boolean isAfficheApporteurJour() {
		return afficheApporteurJour;
	}


	public void setAfficheApporteurJour(boolean afficheApporteurJour) {
		this.afficheApporteurJour = afficheApporteurJour;
	}


	public boolean isAfficheApporteurJourDataTable() {
		return afficheApporteurJourDataTable;
	}


	public void setAfficheApporteurJourDataTable(boolean afficheApporteurJourDataTable) {
		this.afficheApporteurJourDataTable = afficheApporteurJourDataTable;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeApporteur() {
		return totauxCaPeriodeApporteur;
	}


	public void setTotauxCaPeriodeApporteur(DocumentChiffreAffaireDTO totauxCaPeriodeApporteur) {
		this.totauxCaPeriodeApporteur = totauxCaPeriodeApporteur;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeApporteurNonTransforme() {
		return totauxCaPeriodeApporteurNonTransforme;
	}


	public void setTotauxCaPeriodeApporteurNonTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeApporteurNonTransforme) {
		this.totauxCaPeriodeApporteurNonTransforme = totauxCaPeriodeApporteurNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeApporteurNonTransformeARelancer() {
		return totauxCaPeriodeApporteurNonTransformeARelancer;
	}


	public void setTotauxCaPeriodeApporteurNonTransformeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeApporteurNonTransformeARelancer) {
		this.totauxCaPeriodeApporteurNonTransformeARelancer = totauxCaPeriodeApporteurNonTransformeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeApporteurTransforme() {
		return totauxCaPeriodeApporteurTransforme;
	}


	public void setTotauxCaPeriodeApporteurTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeApporteurTransforme) {
		this.totauxCaPeriodeApporteurTransforme = totauxCaPeriodeApporteurTransforme;
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


	public BarChartModel getBarChartModelApporteurJour() {
		return barChartModelApporteurJour;
	}


	public void setBarChartModelApporteurJour(BarChartModel barChartModelApporteurJour) {
		this.barChartModelApporteurJour = barChartModelApporteurJour;
	}

}
