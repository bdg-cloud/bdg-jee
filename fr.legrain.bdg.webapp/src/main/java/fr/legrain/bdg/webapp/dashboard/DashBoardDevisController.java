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

import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardDevisController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5979028746642381345L;
	/**
	 * 
	 */
	

	private @EJB ITaDevisServiceRemote taDevisService;
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

//	private TaInfoEntreprise infoEntreprise;
//    public List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelDevis;
    private BarChartModel barChartModelDevisJour; 
    
//    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    public Date dateDebut;
//    public Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
//    private String codeEtatDevis;
//    private boolean afficheDevisJour; //AJOUTER LE 06/09/2017 Jean Marc
//    private boolean afficheDevisJourDataTable; //AJOUTER LE 06/09/2017 Jean Marc

    private DocumentChiffreAffaireDTO totauxCaPeriodeDevis;
    private DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransforme;
    private DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransformeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeDevisTransforme;
//    private List<DocumentDTO> valueLignesDevis;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeDevis;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeDevisNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeDevisNonTransformeARelancer;
    private int nbDevisSurPeriode = 0;
    private int nbDevisNonTransformeSurPeriode = 0 ;
    private int nbDevisTransformeSurPeriode = 0;
    private int nbDevisNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    
//    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste
//    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument;// Récupère le ca ht par jour dans un liste
    
//    private List<InfosMois> listMoisAnneeExo;
//    private List<InfosJours> listJoursAnneeExo;
    

//    private List<TaDevisDTO> listeDocumentDetailMois;
//    private List<TaDevisDTO> listeDocumentDetailJour;
//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois;
//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour;
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
//    	infoEntreprise = taInfoEntrepriseService.findInstance();
//    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
//    	codeEtatDevis = "tous";
////    	barChartModelDevis = new BarChartModel();
//    	listeDocumentDetailMois = new ArrayList<TaDevisDTO>();
//    	listeDocumentDetailJour = new ArrayList<TaDevisDTO>();
//    	listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
//    	listeDocumentArticleParTiersParJour = new ArrayList<TaArticlesParTiersDTO>();
    	//Initialisation des données sur la période exercice
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	actRechercheDevis(codeEtatDocument);
//    	createBarModel();
    }


    public void refreshDashBoard(String etatDevis){
    	setAfficheDocumentJour(false); 
    	setAfficheDocumentJourDataTable(false); 
    	listeDocumentDetailMois.clear(); 
    	listeDocumentDetailJour.clear(); 
    	listeDocumentArticleParTiersParMois.clear(); 
    	listeDocumentArticleParTiersParJour.clear(); 
    	actRechercheDevis(etatDevis);
    }

    public void actRechercheDevis(String codeEtatDevis){
    	setInfoEtatDocument(codeEtatDevis);
    	setCodeEtatDocument(codeEtatDevis);

    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeDevis = taDevisService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Devis non transformé sur la période
		totauxCaPeriodeDevisNonTransforme = taDevisService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Devis non transformé à relancer sur la période
		totauxCaPeriodeDevisNonTransformeARelancer = taDevisService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Devis transformé sur la période
		totauxCaPeriodeDevisTransforme = taDevisService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Retourne le nombre total de documents sur la période
		nbDevisSurPeriode = (int) taDevisService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Devis non transformés sur la période
		nbDevisNonTransformeSurPeriode = (int) taDevisService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Devis à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbDevisNonTransformeARelancerSurPeriode = (int) taDevisService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		// Retourne le nombre de Devis transformés sur la période
		nbDevisTransformeSurPeriode = (int) taDevisService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);    	

		tauxTransfoNb = BigDecimal.ZERO;
		tauxTransfoCa = BigDecimal.ZERO;

		if (nbDevisSurPeriode > 0) {
			try {
				tauxTransfoNb =  (new BigDecimal(nbDevisTransformeSurPeriode) .divide(new BigDecimal(nbDevisSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTransfoCa = totauxCaPeriodeDevisTransforme.getMtHtCalc() .divide(totauxCaPeriodeDevis.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		
		switch (codeEtatDevis) {
		case ETAT_DOCUMENT_TOUS:
			// Renvoi la liste des documents présents sur la période en cours
			valueLignesDocument = taDevisService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taDevisService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

			initInfosLabelRapportMensuelDataTable("Devis");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_DEVIS, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesDocument = taDevisService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taDevisService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Devis");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
					  CSS_DBOARD_BLOC_ACTIF_DEVIS, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taDevisService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taDevisService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Devis");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  CSS_DBOARD_BLOC_ACTIF_DEVIS);
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:

			try {
			valueLignesDocument = taDevisService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taDevisService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1, 15,null);
			barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Devis");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
	  		  		  CSS_DBOARD_BLOC_INACTIF_ALL,
	  		  		  CSS_DBOARD_BLOC_ACTIF_DEVIS, 
	  		  		  CSS_DBOARD_BLOC_INACTIF_ALL);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
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
//		setAfficheDevisJour(true);
		setAfficheDocumentJour(true);
		setAfficheDocumentJourDataTable(false);
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Devis");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taDevisService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Devis");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taDevisService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taDevisService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taDevisService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taDevisService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelDevisJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelDevisJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taDevisService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Devis");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taDevisService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taDevisService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taDevisService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelDevisJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelDevisJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taDevisService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Devis");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taDevisService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taDevisService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taDevisService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelDevisJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelDevisJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taDevisService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Devis");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taDevisService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taDevisService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taDevisService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelDevisJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelDevisJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
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
	montantTotalHtJour = taDevisService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taDevisService.findAllDTOPeriode(jourMois, jourMois,null);
	setListeDocumentArticleParTiersParJour(taDevisService.findArticlesParTiersParMois(jourMois, jourMois));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Devis");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taDevisService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taDevisService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taDevisService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Devis");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taDevisService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taDevisService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taDevisService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Devis");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaDevis.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taDevisService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taDevisService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taDevisService.findArticlesParTiersTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Devis");
		break;
	}
		
    }
	
public void actImprimerListeDevis(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", "Liste des devis en €");
//		sessionMap.put("debut", getDateDebut());
//		sessionMap.put("fin", getDateFin());
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeDevis", valueLignesDocument);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public void actImprimerListeDevisDocMois(ActionEvent actionEvent) {


try {
			
ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

Map<String, Object> sessionMap = externalContext.getSessionMap();

Map<String,Object> mapParametre = new HashMap<String,Object>();
mapParametre.put("infosLabel", infosLabelRapportMensuelDataTable);

sessionMap.put("parametre", mapParametre);
sessionMap.put("listeDevis", listeDocumentDetailMois);

} catch (Exception e) {
	e.printStackTrace();
}
}

public void actImprimerListeDevisDocDetailMois(ActionEvent actionEvent) {
	
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();
	Map<String, Object> sessionMapInfos = externalContext.getSessionMap();
	Map<String,Object> mapParametre = new HashMap<String,Object>();

	mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
	
	sessionMap.put("parametre", mapParametre);
	sessionMapInfos.put("infosLabel", getInfosLabelRapportArticlesDataTable());
	sessionMap.put("listeArticleDetail", listeDocumentArticleParTiersParMois);

	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void actImprimerListeDevisDocJour(ActionEvent actionEvent) {
	
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	
	Map<String, Object> sessionMap = externalContext.getSessionMap();
	
	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("infosLabel", infosLabelRapportJournalierDataTable);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeDevis", listeDocumentDetailJour);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	public void actImprimerListeDevisDocDetailJour(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String,Object> mapParametre = new HashMap<String,Object>();
	
		mapParametre.put("infosLabel", infosLabelRapportArticlesJourDataTable);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeArticleDetail", listeDocumentArticleParTiersParJour);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelDevis() {
		return barChartModelDevis;
	}
	public void setBarChartModelDevisExo(BarChartModel barChartModelDevis) {
		this.barChartModelDevis = barChartModelDevis;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeDevis() {
		return listeCaPeriodeDevis;
	}

	public void setListeCaPeriodeDevis(List<DocumentChiffreAffaireDTO> listeCaPeriodeDevis) {
		this.listeCaPeriodeDevis = listeCaPeriodeDevis;
	}

	public int getNbDevisSurPeriode() {
		return nbDevisSurPeriode;
	}

	public void setNbDevisSurPeriode(int nbDevisSurPeriode) {
		this.nbDevisSurPeriode = nbDevisSurPeriode;
	}

	public int getNbDevisNonTransformeSurPeriode() {
		return nbDevisNonTransformeSurPeriode;
	}

	public void setNbDevisNonTransformeSurPeriode(int nbDevisNonTransformeSurPeriode) {
		this.nbDevisNonTransformeSurPeriode = nbDevisNonTransformeSurPeriode;
	}


	public int getNbDevisNonTransformeARelancerSurPeriode() {
		return nbDevisNonTransformeARelancerSurPeriode;
	}


	public void setNbDevisNonTransformeARelancerSurPeriode(int nbDevisNonTransformeARelancerSurPeriode) {
		this.nbDevisNonTransformeARelancerSurPeriode = nbDevisNonTransformeARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeDevisNonTransforme() {
		return listeCaPeriodeDevisNonTransforme;
	}


	public void setListeCaPeriodeDevisNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeDevisNonTransforme) {
		this.listeCaPeriodeDevisNonTransforme = listeCaPeriodeDevisNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbDevisTransformeSurPeriode() {
		return nbDevisTransformeSurPeriode;
	}


	public void setNbDevisTransformeSurPeriode(int nbDevisTransformeSurPeriode) {
		this.nbDevisTransformeSurPeriode = nbDevisTransformeSurPeriode;
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

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeDevis() {
		return totauxCaPeriodeDevis;
	}


	public void setTotauxCaPeriodeDevis(DocumentChiffreAffaireDTO totauxCaPeriodeDevis) {
		this.totauxCaPeriodeDevis = totauxCaPeriodeDevis;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeDevisNonTransforme() {
		return totauxCaPeriodeDevisNonTransforme;
	}


	public void setTotauxCaPeriodeDevisNonTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransforme) {
		this.totauxCaPeriodeDevisNonTransforme = totauxCaPeriodeDevisNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeDevisNonTransformeARelancer() {
		return totauxCaPeriodeDevisNonTransformeARelancer;
	}


	public void setTotauxCaPeriodeDevisNonTransformeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransformeARelancer) {
		this.totauxCaPeriodeDevisNonTransformeARelancer = totauxCaPeriodeDevisNonTransformeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeDevisTransforme() {
		return totauxCaPeriodeDevisTransforme;
	}


	public void setTotauxCaPeriodeDevisTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeDevisTransforme) {
		this.totauxCaPeriodeDevisTransforme = totauxCaPeriodeDevisTransforme;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeDevisNonTransformeARelancer() {
		return listeCaPeriodeDevisNonTransformeARelancer;
	}


	public void setListeCaPeriodeDevisNonTransformeARelancer(
			List<DocumentChiffreAffaireDTO> listeCaPeriodeDevisNonTransformeARelancer) {
		this.listeCaPeriodeDevisNonTransformeARelancer = listeCaPeriodeDevisNonTransformeARelancer;
	}


	public BarChartModel getBarChartModelDevisJour() {
		return barChartModelDevisJour;
	}


	public void setBarChartModelDevisJour(BarChartModel barChartModelDevisJour) {
		this.barChartModelDevisJour = barChartModelDevisJour;
	}

}
