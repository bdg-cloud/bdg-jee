package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
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

import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.InfosJours;
import fr.legrain.lib.data.InfosMois;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardBoncdeController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6955344392736955663L;
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1363000298058005675L;
	/**
	 * 
	 */
	
	private @EJB ITaBoncdeServiceRemote taBoncdeService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    public List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelBoncde;
    private BarChartModel barChartModelBoncdeJour;
//    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    public Date dateDebut;
//    public Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatBoncde;
    private List<DocumentDTO> valueLignesBoncde;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBoncde;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBoncdeNonTransforme;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBoncdeNonTransformeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBoncdeTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeBoncde;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeBoncdeNonTransforme;

    private int nbBoncdeSurPeriode = 0;
    private int nbBoncdeNonTransformeSurPeriode = 0 ;
    private int nbBoncdeTransformeSurPeriode = 0;
    private int nbBoncdeNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    private String cSbNbBoncdeSurPeriode;
    private String cSbNbBoncdeTransformeSurPeriode;
    private String cSbNbBoncdeNonTransformeSurPeriode;
    private String cSbNbBoncdeNonTransformeARelancerSurPeriode;

//    private List<TaBoncdeDTO> listeDocumentDetailMois;
//    private List<TaBoncdeDTO> listeDocumentDetailJour;

//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois;
//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour;
    
//    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste
//    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument;// Récupère le ca ht par jour dans un liste
    
    private List<InfosMois> listMoisAnneeExo;
    private List<InfosJours> listJoursAnneeExo;
    
    private String listeQteFabriquerParFamilleArticleSynthese="ListeQteFabriquerParFamilleArticleSynthese";
    private String listeQteFabriquerParFamilleArticleDetail="ListeQteFabriquerParFamilleArticleDetail";
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
//    	listeDocumentDetailMois = new ArrayList<DocumentDTO>();
//    	listeDocumentDetailJour = new ArrayList<DocumentDTO>();
//    	listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
//    	listeDocumentArticleParTiersParJour = new ArrayList<TaArticlesParTiersDTO>();
    	
    	//Initialisation des données sur la période exercice
    	actRechercheBoncde(codeEtatDocument);
    }


    public void refreshDashBoard(String etatBoncde){
    	setAfficheDocumentJour(false); 
    	setAfficheDocumentJourDataTable(false); 
    	listeDocumentDetailMois.clear(); 
    	listeDocumentDetailJour.clear(); 
    	listeDocumentArticleParTiersParMois.clear(); 
    	listeDocumentArticleParTiersParJour.clear(); 
    	actRechercheBoncde(etatBoncde);
    }

    public void actRechercheBoncde(String codeEtatBoncde){
//    	if (action != null) {
//    	codeEtatBoncde = (String) action.getComponent().getAttributes().get("codeetatBoncde");
//    	}
    	setCodeEtatDocument(codeEtatBoncde);
    	setInfoEtatDocument(codeEtatBoncde);
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeBoncde = taBoncdeService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeBoncdeNonTransforme = taBoncdeService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeBoncdeNonTransformeARelancer = taBoncdeService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeBoncdeTransforme = taBoncdeService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Retourne le nombre total de documents sur la période
		nbBoncdeSurPeriode = (int) taBoncdeService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma non transformés sur la période
		nbBoncdeNonTransformeSurPeriode = (int) taBoncdeService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbBoncdeNonTransformeARelancerSurPeriode = (int) taBoncdeService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		// Retourne le nombre de Proforma transformés sur la période
		nbBoncdeTransformeSurPeriode = (int) taBoncdeService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		
		if (nbBoncdeSurPeriode > 0) {
			try {
				tauxTransfoNb =  (new BigDecimal(nbBoncdeTransformeSurPeriode) .divide(new BigDecimal(nbBoncdeSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTransfoCa = totauxCaPeriodeBoncdeTransforme.getMtHtCalc() .divide(totauxCaPeriodeBoncde.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}    	
    	
    	switch (codeEtatBoncde) {
		case ETAT_DOCUMENT_TOUS:
			// Renvoi la liste des documents présents sur la période en cours
			valueLignesDocument = taBoncdeService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelBoncde = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taBoncdeService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelBoncde.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Boncde");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_BONCDE, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesDocument = taBoncdeService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			//listeCaPeriodeBoncde = taBoncdeService.findCABoncdeSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
			barChartModelBoncde = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taBoncdeService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelBoncde.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Boncde");

			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
					  CSS_DBOARD_BLOC_ACTIF_BONCDE, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taBoncdeService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelBoncde = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taBoncdeService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelBoncde.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Boncde");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  CSS_DBOARD_BLOC_ACTIF_BONCDE);
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			valueLignesDocument = taBoncdeService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			//listeCaPeriodeBoncde = taBoncdeService.findCABoncdeSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
			nbBoncdeSurPeriode = (int) taBoncdeService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			nbBoncdeNonTransformeSurPeriode = (int) taBoncdeService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			nbBoncdeNonTransformeARelancerSurPeriode = (int) taBoncdeService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			initInfosLabelRapportMensuelDataTable("Boncde");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
	  		  		  CSS_DBOARD_BLOC_INACTIF_ALL,
	  		  		  CSS_DBOARD_BLOC_ACTIF_BONCDE, 
	  		  		  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
			
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
//		setAfficheBoncdeJour(true);
		setAfficheDocumentJour(true);
		setAfficheDocumentJourDataTable(false);
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Boncde");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taBoncdeService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Boncde");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taBoncdeService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taBoncdeService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taBoncdeService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taBoncdeService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelBoncdeJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelBoncdeJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taBoncdeService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Boncde");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taBoncdeService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taBoncdeService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taBoncdeService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelBoncdeJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelBoncdeJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taBoncdeService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Boncde");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taBoncdeService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taBoncdeService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taBoncdeService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelBoncdeJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelBoncdeJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taBoncdeService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Boncde");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taBoncdeService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taBoncdeService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taBoncdeService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelBoncdeJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelBoncdeJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
	}
   }
	
	public void itemSelectJour(ItemSelectEvent event2) throws ParseException {

		setNumeroJourDetail(event2.getItemIndex());
		Date jourMois = listJoursAnneeExo.get(event2.getItemIndex()).getDateDuJour();
		setAfficheDocumentJourDataTable(true);
		
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par jour
	montantTotalHtJour = taBoncdeService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taBoncdeService.findAllDTOPeriode(jourMois, jourMois,null);
	setListeDocumentArticleParTiersParJour(taBoncdeService.findArticlesParTiersParMois(jourMois, jourMois));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Boncde");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taBoncdeService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taBoncdeService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taBoncdeService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Boncde");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taBoncdeService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taBoncdeService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taBoncdeService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Boncde");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaBoncde.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taBoncdeService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taBoncdeService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taBoncdeService.findArticlesParTiersTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Boncde");
		break;
	}
		
    }
     	
    	public void actImprimerListeBoncde(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		
//		sessionMap.put("debut", getDateDebut());
//		sessionMap.put("fin", getDateFin());
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeBoncde", valueLignesDocument);
	
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
        	
//        	sessionMap.put("debut", getDateDebut());
//        	sessionMap.put("fin", getDateFin());
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
//        		Map<String,Object> mapParametre = new HashMap<String,Object>();
        	
//        		mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
        		
//        		sessionMap.put("parametre", mapParametre);
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
        		
//        		sessionMap.put("debut", getDateDebut());
//        		sessionMap.put("fin", getDateFin());
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
//        			Map<String,Object> mapParametre = new HashMap<String,Object>();
        		
//        			mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
        			
//        			sessionMap.put("parametre", mapParametre);
        			sessionMapInfos.put("infosLabel", infosLabelRapportArticlesJourDataTable);
        			sessionMap.put("listeArticleJour", listeDocumentArticleParTiersParJour);

        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
        		
//    public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal() {
//		
////    	List<Object> list = taBoncdeService.findCABoncdeSurPeriode(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise());
////    	List<DocumentChiffreAffaireDTO> list = taBoncdeService.findChiffreAffaireTotal(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//    	List<DocumentChiffreAffaireDTO> list = taBoncdeService.findChiffreAffaireTotalDTO(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//		return list;
//    	
//    }
	public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelBoncde() {
		return barChartModelBoncde;
	}
	public void setBarChartModelBoncdeExo(BarChartModel barChartModelBoncde) {
		this.barChartModelBoncde = barChartModelBoncde;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public String getCodeEtatBoncde() {
		return codeEtatBoncde;
	}

	public void setCodeEtatBoncde(String codeEtatBoncde) {
		this.codeEtatBoncde = codeEtatBoncde;
	}

	public List<DocumentDTO> getValueLignesBoncde() {
		return valueLignesBoncde;
	}

	public void setValueLignesBoncde(List<DocumentDTO> valueLignesBoncde) {
		this.valueLignesBoncde = valueLignesBoncde;
	}

	public int getNbBoncdeSurPeriode() {
		return nbBoncdeSurPeriode;
	}

	public void setNbBoncdeSurPeriode(int nbBoncdeSurPeriode) {
		this.nbBoncdeSurPeriode = nbBoncdeSurPeriode;
	}

	public int getNbBoncdeNonTransformeSurPeriode() {
		return nbBoncdeNonTransformeSurPeriode;
	}

	public void setNbBoncdeNonTransformeSurPeriode(int nbBoncdeNonTransformeSurPeriode) {
		this.nbBoncdeNonTransformeSurPeriode = nbBoncdeNonTransformeSurPeriode;
	}


	public int getNbBoncdeNonTransformeARelancerSurPeriode() {
		return nbBoncdeNonTransformeARelancerSurPeriode;
	}


	public void setNbBoncdeNonTransformeARelancerSurPeriode(int nbBoncdeNonTransformeARelancerSurPeriode) {
		this.nbBoncdeNonTransformeARelancerSurPeriode = nbBoncdeNonTransformeARelancerSurPeriode;
	}


	public String getcSbNbBoncdeSurPeriode() {
		return cSbNbBoncdeSurPeriode;
	}


	public void setcSbNbBoncdeSurPeriode(String cSbNbBoncdeSurPeriode) {
		this.cSbNbBoncdeSurPeriode = cSbNbBoncdeSurPeriode;
	}


	public String getcSbNbBoncdeNonTransformeSurPeriode() {
		return cSbNbBoncdeNonTransformeSurPeriode;
	}


	public void setcSbNbBoncdeNonTransformeSurPeriode(String cSbNbBoncdeNonTransformeSurPeriode) {
		this.cSbNbBoncdeNonTransformeSurPeriode = cSbNbBoncdeNonTransformeSurPeriode;
	}


	public String getcSbNbBoncdeNonTransformeARelancerSurPeriode() {
		return cSbNbBoncdeNonTransformeARelancerSurPeriode;
	}


	public void setcSbNbBoncdeNonTransformeARelancerSurPeriode(String cSbNbBoncdeNonTransformeARelancerSurPeriode) {
		this.cSbNbBoncdeNonTransformeARelancerSurPeriode = cSbNbBoncdeNonTransformeARelancerSurPeriode;
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



	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbBoncdeTransformeSurPeriode() {
		return nbBoncdeTransformeSurPeriode;
	}


	public void setNbBoncdeTransformeSurPeriode(int nbBoncdeTransformeSurPeriode) {
		this.nbBoncdeTransformeSurPeriode = nbBoncdeTransformeSurPeriode;
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


	public String getcSbNbBoncdeTransformeSurPeriode() {
		return cSbNbBoncdeTransformeSurPeriode;
	}


	public void setcSbNbBoncdeTransformeSurPeriode(String cSbNbBoncdeTransformeSurPeriode) {
		this.cSbNbBoncdeTransformeSurPeriode = cSbNbBoncdeTransformeSurPeriode;
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


//	public List<TaBoncdeDTO> getListeDocumentDetailMois() {
//		return listeDocumentDetailMois;
//	}
//
//
//	public void setListeDocumentDetailMois(List<TaBoncdeDTO> listeDocumentDetailMois) {
//		this.listeDocumentDetailMois = listeDocumentDetailMois;
//	}
//
//
//	public List<TaBoncdeDTO> getListeDocumentDetailJour() {
//		return listeDocumentDetailJour;
//	}
//
//
//	public void setListeDocumentDetailJour(List<TaBoncdeDTO> listeDocumentDetailJour) {
//		this.listeDocumentDetailJour = listeDocumentDetailJour;
//	}


//	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParMois() {
//		return listeDocumentArticleParTiersParMois;
//	}
//
//
//	public void setListeDocumentArticleParTiersParMois(List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois) {
//		this.listeDocumentArticleParTiersParMois = listeDocumentArticleParTiersParMois;
//	}


//	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParJour() {
//		return listeDocumentArticleParTiersParJour;
//	}
//
//
//	public void setListeDocumentArticleParTiersParJour(List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour) {
//		this.listeDocumentArticleParTiersParJour = listeDocumentArticleParTiersParJour;
//	}


	public List<InfosMois> getListMoisAnneeExo() {
		return listMoisAnneeExo;
	}


	public void setListMoisAnneeExo(List<InfosMois> listMoisAnneeExo) {
		this.listMoisAnneeExo = listMoisAnneeExo;
	}


	public List<InfosJours> getListJoursAnneeExo() {
		return listJoursAnneeExo;
	}


	public void setListJoursAnneeExo(List<InfosJours> listJoursAnneeExo) {
		this.listJoursAnneeExo = listJoursAnneeExo;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeBoncde() {
		return listeCaPeriodeBoncde;
	}


	public void setListeCaPeriodeBoncde(List<DocumentChiffreAffaireDTO> listeCaPeriodeBoncde) {
		this.listeCaPeriodeBoncde = listeCaPeriodeBoncde;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeBoncdeNonTransforme() {
		return listeCaPeriodeBoncdeNonTransforme;
	}


	public void setListeCaPeriodeBoncdeNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeBoncdeNonTransforme) {
		this.listeCaPeriodeBoncdeNonTransforme = listeCaPeriodeBoncdeNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBoncde() {
		return totauxCaPeriodeBoncde;
	}


	public void setTotauxCaPeriodeBoncde(DocumentChiffreAffaireDTO totauxCaPeriodeBoncde) {
		this.totauxCaPeriodeBoncde = totauxCaPeriodeBoncde;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBoncdeNonTransforme() {
		return totauxCaPeriodeBoncdeNonTransforme;
	}


	public void setTotauxCaPeriodeBoncdeNonTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeBoncdeNonTransforme) {
		this.totauxCaPeriodeBoncdeNonTransforme = totauxCaPeriodeBoncdeNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBoncdeNonTransformeARelancer() {
		return totauxCaPeriodeBoncdeNonTransformeARelancer;
	}


	public void setTotauxCaPeriodeBoncdeNonTransformeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeBoncdeNonTransformeARelancer) {
		this.totauxCaPeriodeBoncdeNonTransformeARelancer = totauxCaPeriodeBoncdeNonTransformeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBoncdeTransforme() {
		return totauxCaPeriodeBoncdeTransforme;
	}


	public void setTotauxCaPeriodeBoncdeTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeBoncdeTransforme) {
		this.totauxCaPeriodeBoncdeTransforme = totauxCaPeriodeBoncdeTransforme;
	}


	public BarChartModel getBarChartModelBoncdeJour() {
		return barChartModelBoncdeJour;
	}


	public void setBarChartModelBoncdeJour(BarChartModel barChartModelBoncdeJour) {
		this.barChartModelBoncdeJour = barChartModelBoncdeJour;
	}


	public void setBarChartModelBoncde(BarChartModel barChartModelBoncde) {
		this.barChartModelBoncde = barChartModelBoncde;
	}


	public void actImprimerListeEtatDesTourneesDesBoncde(String identification,TaTransporteurDTO transporteurDTO, Date date,boolean avecTransporteur,boolean surPeriode, Date dateDebut, Date dateFin) {
		List<IDocumentTiersDTO> l=null;
		String codeTransporteur=null;
		try {
				
			infoEntreprise=taInfoEntrepriseService.findInstance();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametre = new HashMap<String,Object>();
			
			sessionMap.remove("parametre");

			if(! surPeriode)dateDebut=date;
			if(! surPeriode)dateFin=date;

			mapParametre.put("identification", identification);
			mapParametre.put("periodeDebut", dateDebut);
			mapParametre.put("periodeFin", dateFin);
			mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
			mapParametre.put("dateSouhaitee", LibDate.localDateToDate(getDateFin()));

			
			if(avecTransporteur && transporteurDTO!=null) {
				codeTransporteur= transporteurDTO.getCodeTransporteur();
				mapParametre.put("titreEditionComplement", "pour le transporteur : "+codeTransporteur);
			}else if(avecTransporteur){
				codeTransporteur="%";
				mapParametre.put("titreEditionComplement", "pour tous les transporteurs ");
			}else {
				codeTransporteur="sans";
				mapParametre.put("titreEditionComplement", "sans transporteur ");
			}

			mapParametre.put("transporteur",codeTransporteur);
			mapParametre.put("titreEdition", "Etat des tournées des bons de commande ");
			l=taBoncdeService.selectTourneeTransporteur(codeTransporteur, 
					dateDebut, dateFin);

			sessionMap.put("parametre", mapParametre);			 

			sessionMap.put("listeEtatTournee", l);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}


	public void actImprimerListeEtatDesQuantiteAFabriquerDesBoncde(String identification, Date date,boolean surPeriode, Date dateDebut, Date dateFin) {
		List<DocumentChiffreAffaireDTO> l=null;
		boolean synthese=true;
		try {
				
			infoEntreprise=taInfoEntrepriseService.findInstance();
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametre = new HashMap<String,Object>();
			
			sessionMap.remove("parametre");
			if(! surPeriode)dateDebut=date;
			if(! surPeriode)dateFin=date;

			mapParametre.put("identification", identification);
			mapParametre.put("debut", dateDebut);
			mapParametre.put("fin", dateFin);
			mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
			mapParametre.put("dateSouhaitee", LibDate.localDateToDate(getDateFin()));

			

			mapParametre.put("infoLabel", "Etat des quantités à fabriquer par famille d’article et par article à partir des Bons de Commande ");
			synthese=identification.equals(listeQteFabriquerParFamilleArticleSynthese);
			
			l = taBoncdeService.listLigneArticlePeriodeBonlivDTOParFamille(dateDebut, dateFin, "%" , synthese,DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE);
			
//			l=taBoncdeService.selectTourneeTransporteur(codeTransporteur,date, date);

			sessionMap.put("parametre", mapParametre);			 

			sessionMap.put("listeArticle", l);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}


	public String getListeQteFabriquerParFamilleArticleSynthese() {
		return listeQteFabriquerParFamilleArticleSynthese;
	}


	public void setListeQteFabriquerParFamilleArticleSynthese(String listeQteFabriquerParFamilleArticleSynthese) {
		this.listeQteFabriquerParFamilleArticleSynthese = listeQteFabriquerParFamilleArticleSynthese;
	}


	public String getListeQteFabriquerParFamilleArticleDetail() {
		return listeQteFabriquerParFamilleArticleDetail;
	}


	public void setListeQteFabriquerParFamilleArticleDetail(String listeQteFabriquerParFamilleArticleDetail) {
		this.listeQteFabriquerParFamilleArticleDetail = listeQteFabriquerParFamilleArticleDetail;
	}
	

}
