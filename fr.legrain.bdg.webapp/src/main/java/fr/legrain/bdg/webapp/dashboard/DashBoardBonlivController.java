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
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardBonlivController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2966399979318955372L;

	private static final String listeEtatTourneeParTransporteur ="ListeEtatTourneeParTransporteur";
	private static final String listeEtatChargementParTransporteur ="ListeEtatChargementParTransporteur";
	private static final String listeBLNonTermineLotParTransporteur ="ListeBLNonTermineLotParTransporteur";
	
	
	
	private @EJB ITaBonlivServiceRemote taBonlivService;
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

//	private TaInfoEntreprise infoEntreprise;
//    public List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelBonliv;
    private BarChartModel barChartModelBonlivJour; 
//    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    public Date dateDebut;
//    public Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
//    private String codeEtatBonliv;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBonliv;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBonlivNonTransforme;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBonlivNonTransformeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeBonlivTransforme;
//    private List<DocumentDTO> valueLignesBonliv;

    private int nbBonlivSurPeriode = 0;
    private int nbBonlivNonTransformeSurPeriode = 0 ;
    private int nbBonlivTransformeSurPeriode = 0;
    private int nbBonlivNonTransformeARelancerSurPeriode = 0;

    
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;

        
    private BigDecimal montantTotalHtMois = BigDecimal.ZERO;
    private BigDecimal montantTotalHtJour = BigDecimal.ZERO;
    
    private String TaTransporteur;

    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
//    	codeEtatBonliv = "tous";
//    	listeDocumentDetailMois = new ArrayList<DocumentDTO>();
//    	listeDocumentDetailJour = new ArrayList<DocumentDTO>();
//    	listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
//    	listeDocumentArticleParTiersParJour = new ArrayList<TaArticlesParTiersDTO>();    	
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	//Initialisation des données sur la période exercice
    	actRechercheBonliv(codeEtatDocument);
//    	createBarModel();
    }


    public void refreshDashBoard(String etatBonliv){
    	setAfficheDocumentJour(false); 
    	setAfficheDocumentJourDataTable(false); 
    	listeDocumentDetailMois.clear(); 
    	listeDocumentDetailJour.clear(); 
    	listeDocumentArticleParTiersParMois.clear(); 
    	listeDocumentArticleParTiersParJour.clear(); 
    	actRechercheBonliv(etatBonliv);
    }

    public void actRechercheBonliv(String codeEtatBonliv){
    	setInfoEtatDocument(codeEtatBonliv);
    	setCodeEtatDocument(codeEtatBonliv);

    	setInfoEtatDocument(codeEtatBonliv);
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));

    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeBonliv = taBonlivService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeBonlivNonTransforme = taBonlivService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeBonlivNonTransformeARelancer = taBonlivService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeBonlivTransforme = taBonlivService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Retourne le nombre total de documents sur la période
		nbBonlivSurPeriode = (int) taBonlivService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma non transformés sur la période
		nbBonlivNonTransformeSurPeriode = (int) taBonlivService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbBonlivNonTransformeARelancerSurPeriode = (int) taBonlivService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		// Retourne le nombre de Proforma transformés sur la période
		nbBonlivTransformeSurPeriode = (int) taBonlivService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		tauxTransfoNb = BigDecimal.ZERO;
		tauxTransfoCa = BigDecimal.ZERO;

		
		if (nbBonlivSurPeriode > 0) {
			try {
				tauxTransfoNb =  (new BigDecimal(nbBonlivTransformeSurPeriode) .divide(new BigDecimal(nbBonlivSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTransfoCa = totauxCaPeriodeBonlivTransforme.getMtHtCalc() .divide(totauxCaPeriodeBonliv.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}    	
    	
    switch (codeEtatBonliv) {
	case ETAT_DOCUMENT_TOUS:
//		// Renvoi la liste des documents présents sur la période en cours
		valueLignesDocument = taBonlivService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
//		// Création et initialisation du graphique du CA HT / mois pour la période en cours
		barChartModelBonliv = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocument = taBonlivService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
		barChartModelBonliv.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//		barChartModelProforma.clear();

		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		// Initialisent les classes css pour les block d'information
		initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_BONLIV, 
						  CSS_DBOARD_BLOC_INACTIF_ALL, 
						  CSS_DBOARD_BLOC_INACTIF_ALL, 
						  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

	case ETAT_DOCUMENT_NON_TRANSORME:
		valueLignesDocument = taBonlivService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		barChartModelBonliv = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocument = taBonlivService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
		barChartModelBonliv.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//		barChartModelProforma.clear();
		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL,
							CSS_DBOARD_BLOC_ACTIF_BONLIV, 
							CSS_DBOARD_BLOC_INACTIF_ALL, 
							CSS_DBOARD_BLOC_INACTIF_ALL);			

		//setInfoCase("non transformer");
		
		break;			
	case ETAT_DOCUMENT_TRANSORME:
		valueLignesDocument = taBonlivService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		barChartModelBonliv = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocument = taBonlivService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
		barChartModelBonliv.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//		barChartModelProforma.clear();
//		barChartModelProformaJour.clear();
		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
		  		  			CSS_DBOARD_BLOC_INACTIF_ALL, 
		  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
		  		  			CSS_DBOARD_BLOC_ACTIF_BONLIV);

		//setInfoCase("transformer");
		break;		


	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		valueLignesDocument = taBonlivService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		barChartModelBonliv = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocument = taBonlivService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1, 15,null);
		barChartModelBonliv.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//		//listeCaPeriodeProforma = taProformaService.findCAProformaSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//		barChartModelProforma.clear();
//		barChartModelProformaJour.clear();
		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
  		  		  			CSS_DBOARD_BLOC_ACTIF_BONLIV, 
  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL);

		//setInfoCase("non transformer à relancer");
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
//		setAfficheBonlivJour(true);
		setAfficheDocumentJour(true);
		setAfficheDocumentJourDataTable(false);
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taBonlivService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Bon de livraison");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taBonlivService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taBonlivService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taBonlivService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taBonlivService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelBonlivJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelBonlivJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taBonlivService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taBonlivService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taBonlivService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taBonlivService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelBonlivJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelBonlivJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taBonlivService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taBonlivService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taBonlivService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taBonlivService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelBonlivJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelBonlivJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taBonlivService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Bon de livraison");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taBonlivService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taBonlivService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taBonlivService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelBonlivJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelBonlivJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
	}
   }    
	
//		/////////////////////////////////////////////////////////////////
//	MODIFIER LE 06/09/2017 Jean Marc	

public void itemSelectJour(ItemSelectEvent event2) throws ParseException {

		setNumeroJourDetail(event2.getItemIndex());
		Date jourMois = listJoursAnneeExo.get(event2.getItemIndex()).getDateDuJour();
		setAfficheDocumentJourDataTable(true);

		switch (infoEtatDocument) {

		case ETAT_DOCUMENT_TOUS:
			// Cumul du chiffre d'affaire par jour
			montantTotalHtJour = taBonlivService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
			// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
			listeDocumentDetailJour= taBonlivService.findAllDTOPeriode(jourMois, jourMois,null);
			setListeDocumentArticleParTiersParJour(taBonlivService.findArticlesParTiersParMois(jourMois, jourMois));
			//etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Bon de livraison");
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			// Cumul du chiffre d'affaire par Jour
			montantTotalHtJour = taBonlivService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
			// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
			listeDocumentDetailJour= taBonlivService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
			setListeDocumentArticleParTiersParJour(taBonlivService.findArticlesParTiersNonTransforme(jourMois, jourMois));
			//etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Bon de livraison");
			break;

		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			// Cumul du chiffre d'affaire par Jour
			montantTotalHtJour = taBonlivService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
			// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
			listeDocumentDetailJour= taBonlivService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
			setListeDocumentArticleParTiersParJour(taBonlivService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
			//etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Bon de livraison");
			break;

		case ETAT_DOCUMENT_TRANSORME:
			// Cumul du chiffre d'affaire par Jour
			//cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaBonliv.TYPE_DOC, jourMois, jourMois);
			//montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
			montantTotalHtJour = taBonlivService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
			// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
			listeDocumentDetailJour= taBonlivService.findDocumentTransfosDTO(jourMois, jourMois,null);
			setListeDocumentArticleParTiersParJour(taBonlivService.findArticlesParTiersTransforme(jourMois, jourMois));
			//etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Bon de livraison");
			break;
		}

	}	

	public void actImprimerListeBonLiv(ActionEvent actionEvent) {
	
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", "Liste des bons de livraisons");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeBonLiv", valueLignesDocument);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	public void actImprimerListeBonLivDocMois(ActionEvent actionEvent) {


    	try {
    				
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

    	Map<String, Object> sessionMap = externalContext.getSessionMap();
    	Map<String,Object> mapParametre = new HashMap<String,Object>();
    	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
    	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
    	mapParametre.put("infosLabel", infosLabelRapportMensuelDataTable);

    	sessionMap.put("parametre", mapParametre);
    	sessionMap.put("listeBonLiv", listeDocumentDetailMois);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	}

    	public void actImprimerListeBonLivDocDetailMois(ActionEvent actionEvent) {
    		
    		
    		try {
    					
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    		Map<String, Object> sessionMap = externalContext.getSessionMap();
    		Map<String,Object> mapParametre = new HashMap<String,Object>();
    		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
    		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
    		mapParametre.put("infosLabel", infosLabelRapportArticlesDataTable);
    		
    		sessionMap.put("parametre", mapParametre);
    		sessionMap.put("listeArticleDetail", listeDocumentArticleParTiersParMois);

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

    	public void actImprimerListeBonLivDocJour(ActionEvent actionEvent) {
    		
    		
    		try {
    					
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    		
    		Map<String, Object> sessionMap = externalContext.getSessionMap();
    		
    		Map<String,Object> mapParametre = new HashMap<String,Object>();
    		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
    		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
    		mapParametre.put("infosLabel", infosLabelRapportJournalierDataTable);
    		
    		sessionMap.put("parametre", mapParametre);
    		sessionMap.put("listeBonLiv", listeDocumentDetailJour);

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    		
    		public void actImprimerListeBonLivDocDetailJour(ActionEvent actionEvent) {
    			
    			
    			try {
    						
    			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    			Map<String, Object> sessionMap = externalContext.getSessionMap();
    			Map<String,Object> mapParametre = new HashMap<String,Object>();
    			mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
    			mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
    			mapParametre.put("infosLabel", infosLabelRapportArticlesJourDataTable);
    			
    			sessionMap.put("parametre", mapParametre);
    			sessionMap.put("listeArticleDetail", listeDocumentArticleParTiersParJour);

    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		
//private void etatInfosLabelRapportMensuelDataTable() {
//	
//	if (afficheBonlivJour==true) {
//		setInfosLabelRapportMensuelDataTable("Liste des BL du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
//		setInfosLabelRapportArticlesDataTable("Liste des lignes article du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
//		setInfosLabelRapportJournalierDataTable("Liste des BL du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
//		setInfosLabelRapportArticlesJourDataTable("Liste des lignes article du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
//	}
//	else {
//		setInfosLabelRapportMensuelDataTable("Cliquez sur une barre du graphique pour avoir le détail mensuel des ventes");
//		setInfosLabelRapportArticlesDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
//		setInfosLabelRapportJournalierDataTable("Cliquez sur une barre du graphique pour avoir le détail journalier des ventes");
//		setInfosLabelRapportArticlesJourDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
//	}
//	
//	if (valueLignesBonliv.isEmpty()){
//		setInfosLabelRapportMensuelDataTable("La liste des BL "+infoEtatDocument+" est vide");
//		setInfosLabelRapportArticlesDataTable("La liste des articles "+infoEtatDocument+" est vide");
//		setInfosLabelRapportJournalierDataTable("La liste des BL "+infoEtatDocument+" est vide");
//	}
//	}

	public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelBonliv() {
		return barChartModelBonliv;
	}
	public void setBarChartModelBonlivExo(BarChartModel barChartModelBonliv) {
		this.barChartModelBonliv = barChartModelBonliv;
	}
	
	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public int getNbBonlivSurPeriode() {
		return nbBonlivSurPeriode;
	}

	public void setNbBonlivSurPeriode(int nbBonlivSurPeriode) {
		this.nbBonlivSurPeriode = nbBonlivSurPeriode;
	}

	public int getNbBonlivNonTransformeSurPeriode() {
		return nbBonlivNonTransformeSurPeriode;
	}

	public void setNbBonlivNonTransformeSurPeriode(int nbBonlivNonTransformeSurPeriode) {
		this.nbBonlivNonTransformeSurPeriode = nbBonlivNonTransformeSurPeriode;
	}


	public int getNbBonlivNonTransformeARelancerSurPeriode() {
		return nbBonlivNonTransformeARelancerSurPeriode;
	}


	public void setNbBonlivNonTransformeARelancerSurPeriode(int nbBonlivNonTransformeARelancerSurPeriode) {
		this.nbBonlivNonTransformeARelancerSurPeriode = nbBonlivNonTransformeARelancerSurPeriode;
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


	public int getNbBonlivTransformeSurPeriode() {
		return nbBonlivTransformeSurPeriode;
	}


	public void setNbBonlivTransformeSurPeriode(int nbBonlivTransformeSurPeriode) {
		this.nbBonlivTransformeSurPeriode = nbBonlivTransformeSurPeriode;
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


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBonliv() {
		return totauxCaPeriodeBonliv;
	}


	public void setTotauxCaPeriodeBonliv(DocumentChiffreAffaireDTO totauxCaPeriodeBonliv) {
		this.totauxCaPeriodeBonliv = totauxCaPeriodeBonliv;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBonlivNonTransforme() {
		return totauxCaPeriodeBonlivNonTransforme;
	}


	public void setTotauxCaPeriodeBonlivNonTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeBonlivNonTransforme) {
		this.totauxCaPeriodeBonlivNonTransforme = totauxCaPeriodeBonlivNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBonlivNonTransformeARelancer() {
		return totauxCaPeriodeBonlivNonTransformeARelancer;
	}


	public void setTotauxCaPeriodeBonlivNonTransformeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeBonlivNonTransformeARelancer) {
		this.totauxCaPeriodeBonlivNonTransformeARelancer = totauxCaPeriodeBonlivNonTransformeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeBonlivTransforme() {
		return totauxCaPeriodeBonlivTransforme;
	}


	public void setTotauxCaPeriodeBonlivTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeBonlivTransforme) {
		this.totauxCaPeriodeBonlivTransforme = totauxCaPeriodeBonlivTransforme;
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


	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocument() {
		return listeCaMoisDocument;
	}


	public void setListeCaMoisDocument(List<DocumentChiffreAffaireDTO> listeCaMoisDocument) {
		this.listeCaMoisDocument = listeCaMoisDocument;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaJoursDocument() {
		return listeCaJoursDocument;
	}


	public void setListeCaJoursDocument(List<DocumentChiffreAffaireDTO> listeCaJoursDocument) {
		this.listeCaJoursDocument = listeCaJoursDocument;
	}


	public String getNomMoisDetail() {
		return nomMoisDetail;
	}


	public void setNomMoisDetail(String nomMoisDetail) {
		this.nomMoisDetail = nomMoisDetail;
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



	public BarChartModel getBarChartModelBonlivJour() {
		return barChartModelBonlivJour;
	}
	
	
	public void actImprimerListeEtatDesTourneesDesBL(String identification,TaTransporteurDTO transporteurDTO, Date date,boolean avecTransporteur,boolean surPeriode, Date dateDebut, Date dateFin) {
		List<IDocumentTiersDTO> l=null;
		List<DocumentChiffreAffaireDTO> lc=null;
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
			mapParametre.put("dateSouhaitee", date);
			
			
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
			
//			if(avecTransporteur && transporteurDTO!=null) {
				mapParametre.put("codeTransporteur", codeTransporteur);
				
				if(identification.equals(listeEtatTourneeParTransporteur)) {
					l=taBonlivService.selectTourneeTransporteur(codeTransporteur, dateDebut, dateFin);
					sessionMap.put("listeEtatTournee", l);
					mapParametre.put("titreEdition", "Etat des tournées des bons de livraison ");
				}
				else if(identification.equals(listeEtatChargementParTransporteur)) {
					lc=taBonlivService.listLigneArticlePeriodeBonlivTransporteurDTO(codeTransporteur,dateDebut, dateFin, null, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE);
					sessionMap.put("listeArticle", lc);	
					mapParametre.put("titreEdition", "Etat de chargement des bons de livraison ");
				}
				else if(identification.equals(listeBLNonTermineLotParTransporteur)) {
					l=taBonlivService.selectBLNonTermineSansLotTransporteur(codeTransporteur, dateDebut, dateFin);
					sessionMap.put("listeEtatTournee", l);	
					mapParametre.put("titreEdition", "Etat des bons de livraison non terminés (lot) ");
				}
//			}

			sessionMap.put("parametre", mapParametre);
		

			} catch (Exception e) {
				e.printStackTrace();
			}
	}


	public  String getListeEtatTourneeParTransporteur() {
		return listeEtatTourneeParTransporteur;
	}


	public  String getListeEtatChargementParTransporteur() {
		return listeEtatChargementParTransporteur;
	}


	public String getListeBLNonTermineLotParTransporteur() {
		return listeBLNonTermineLotParTransporteur;
	}



	



}
