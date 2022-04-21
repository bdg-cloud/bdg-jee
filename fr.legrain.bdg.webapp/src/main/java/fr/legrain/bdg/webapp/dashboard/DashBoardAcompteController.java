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

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardAcompteController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3663683824246179817L;
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1363000298058005675L;
	/**
	 * 
	 */
	
	private @EJB ITaAcompteServiceRemote taAcompteService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    public List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelAcompte;
    private BarChartModel barChartModelAcompteJour;

    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatAcompte;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAcompte;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAcompteNonTransforme;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAcompteNonTransformeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAcompteTransforme;
    
    private List<DocumentDTO> valueLignesAcompte;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste
    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument;// Récupère le ca ht par jour dans un liste
    

    
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAcompte;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAcompteNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAcompteNonTransformeARelancer;
    private int nbAcompteSurPeriode = 0;
    private int nbAcompteNonTransformeSurPeriode = 0 ;
    private int nbAcompteTransformeSurPeriode = 0;
    private int nbAcompteNonTransformeARelancerSurPeriode = 0;

    
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
//    private int numeroJourDetail; //AJOUTER LE 06/09/2017 Jean Marc
//    private String infosLabelRapportMensuelDataTable; //AJOUTER LE 14/09/2017 Jean Marc
//    private String infosLabelRapportArticlesDataTable; //AJOUTER LE 14/09/2017 Jean Marc
//    private String infosLabelRapportJournalierDataTable; //AJOUTER LE 19/09/2017 Jean Marc
//    private String infosLabelRapportArticlesJourDataTable; //AJOUTER LE 19/09/2017 Jean Marc
    
    
//    private BigDecimal montantTotalHtMois = BigDecimal.ZERO;
//    private BigDecimal montantTotalHtJour = BigDecimal.ZERO;
    
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
//    	codeEtatAcompte = "tous";
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
//    	listeDocumentDetailMois = new ArrayList<DocumentDTO>();
//    	listeDocumentDetailJour = new ArrayList<DocumentDTO>();
//    	listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
//    	listeDocumentArticleParTiersParJour = new ArrayList<TaArticlesParTiersDTO>();   
    	
    	//Initialisation des données sur la période exercice
    	actRechercheAcompte(codeEtatDocument);
//    	createBarModel();
    }


    public void refreshDashBoard(String etatAcompte){
    	setAfficheDocumentJour(false); 
    	setAfficheDocumentJourDataTable(false);
    	listeDocumentDetailMois.clear(); //AJOUTER LE 06/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentDetailJour.clear(); //AJOUTER LE 19/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParMois.clear(); //AJOUTER LE 14/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParJour.clear(); //AJOUTER LE 20/09/2017 Jean Marc vide la table à chaque changement de prof
    	actRechercheAcompte(etatAcompte);
    }

    public void actRechercheAcompte(String codeEtatAcompte){
    	setInfoEtatDocument(codeEtatAcompte);
    	setCodeEtatDocument(codeEtatAcompte);
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));

    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeAcompte = taAcompteService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeAcompteNonTransforme = taAcompteService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeAcompteNonTransformeARelancer = taAcompteService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeAcompteTransforme = taAcompteService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Retourne le nombre total de documents sur la période
		nbAcompteSurPeriode = (int) taAcompteService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma non transformés sur la période
		nbAcompteNonTransformeSurPeriode = (int) taAcompteService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbAcompteNonTransformeARelancerSurPeriode = (int) taAcompteService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		// Retourne le nombre de Proforma transformés sur la période
		nbAcompteTransformeSurPeriode = (int) taAcompteService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		
		if (nbAcompteSurPeriode > 0) {
			try {
				tauxTransfoNb =  (new BigDecimal(nbAcompteTransformeSurPeriode) .divide(new BigDecimal(nbAcompteSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTransfoCa = totauxCaPeriodeAcompteTransforme.getMtHtCalc() .divide(totauxCaPeriodeAcompte.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}    	

	    switch (codeEtatAcompte) {
		case ETAT_DOCUMENT_TOUS:
//			// Renvoi la liste des documents présents sur la période en cours
			valueLignesDocument = taAcompteService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelAcompte = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAcompteService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAcompte.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			barChartModelProforma.clear();
			initInfosLabelRapportMensuelDataTable("Acompte");
//			etatInfosLabelRapportMensuelDataTable();
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_ACOMPTE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
				break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesDocument = taAcompteService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelAcompte = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAcompteService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAcompte.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			barChartModelProforma.clear();
			initInfosLabelRapportMensuelDataTable("Acompte");
//			etatInfosLabelRapportMensuelDataTable();
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL,
								CSS_DBOARD_BLOC_ACTIF_ACOMPTE, 
								CSS_DBOARD_BLOC_INACTIF_ALL, 
								CSS_DBOARD_BLOC_INACTIF_ALL);			

			//setInfoCase("non transformer");
			
			break;			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taAcompteService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelAcompte = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAcompteService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAcompte.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//			barChartModelProforma.clear();
//			barChartModelProformaJour.clear();
			initInfosLabelRapportMensuelDataTable("Acompte");
//			etatInfosLabelRapportMensuelDataTable();
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  			CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  			CSS_DBOARD_BLOC_ACTIF_ACOMPTE);

			//setInfoCase("transformer");
			break;		


		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			valueLignesDocument = taBonlivService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			barChartModelAcompte = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAcompteService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1, 15,null);
			barChartModelAcompte.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//			//listeCaPeriodeProforma = taProformaService.findCAProformaSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			barChartModelProforma.clear();
//			barChartModelProformaJour.clear();
			initInfosLabelRapportMensuelDataTable("Acompte");
//			etatInfosLabelRapportMensuelDataTable();
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
	  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
	  		  		  			CSS_DBOARD_BLOC_ACTIF_ACOMPTE, 
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
//		setAfficheAcompteJour(true);
		setAfficheDocumentJour(true);
		setAfficheDocumentJourDataTable(false);
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Acompte");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taAcompteService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Acompte");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taAcompteService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taAcompteService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taAcompteService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taAcompteService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelAcompteJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelAcompteJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAcompteService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Acompte");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAcompteService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taAcompteService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taAcompteService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelAcompteJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAcompteJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAcompteService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Acompte");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAcompteService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taAcompteService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taAcompteService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelAcompteJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAcompteJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAcompteService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Acompte");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAcompteService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taAcompteService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taAcompteService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelAcompteJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAcompteJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
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
	montantTotalHtJour = taAcompteService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taAcompteService.findAllDTOPeriode(jourMois, jourMois,null);
	setListeDocumentArticleParTiersParJour(taAcompteService.findArticlesParTiersParMois(jourMois, jourMois));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Acompte");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAcompteService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAcompteService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAcompteService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Acompte");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAcompteService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAcompteService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taAcompteService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Acompte");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaAcompte.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taAcompteService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAcompteService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAcompteService.findArticlesParTiersTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Acompte");
		break;
	}
		
    }
    
    
    
    
    
    public void actImprimerListeAcompte(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", "Liste des acomptes");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeAcompte", valueLignesDocument);
	
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
    		
    public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelAcompte() {
		return barChartModelAcompte;
	}
	public void setBarChartModelAcompteExo(BarChartModel barChartModelAcompte) {
		this.barChartModelAcompte = barChartModelAcompte;
	}
	
	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public String getCodeEtatAcompte() {
		return codeEtatAcompte;
	}

	public void setCodeEtatAcompte(String codeEtatAcompte) {
		this.codeEtatAcompte = codeEtatAcompte;
	}

	public List<DocumentDTO> getValueLignesAcompte() {
		return valueLignesAcompte;
	}

	public void setValueLignesAcompte(List<DocumentDTO> valueLignesAcompte) {
		this.valueLignesAcompte = valueLignesAcompte;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAcompte() {
		return listeCaPeriodeAcompte;
	}

	public void setListeCaPeriodeAcompte(List<DocumentChiffreAffaireDTO> listeCaPeriodeAcompte) {
		this.listeCaPeriodeAcompte = listeCaPeriodeAcompte;
	}

	public int getNbAcompteSurPeriode() {
		return nbAcompteSurPeriode;
	}

	public void setNbAcompteSurPeriode(int nbAcompteSurPeriode) {
		this.nbAcompteSurPeriode = nbAcompteSurPeriode;
	}

	public int getNbAcompteNonTransformeSurPeriode() {
		return nbAcompteNonTransformeSurPeriode;
	}

	public void setNbAcompteNonTransformeSurPeriode(int nbAcompteNonTransformeSurPeriode) {
		this.nbAcompteNonTransformeSurPeriode = nbAcompteNonTransformeSurPeriode;
	}


	public int getNbAcompteNonTransformeARelancerSurPeriode() {
		return nbAcompteNonTransformeARelancerSurPeriode;
	}


	public void setNbAcompteNonTransformeARelancerSurPeriode(int nbAcompteNonTransformeARelancerSurPeriode) {
		this.nbAcompteNonTransformeARelancerSurPeriode = nbAcompteNonTransformeARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAcompteNonTransforme() {
		return listeCaPeriodeAcompteNonTransforme;
	}


	public void setListeCaPeriodeAcompteNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeAcompteNonTransforme) {
		this.listeCaPeriodeAcompteNonTransforme = listeCaPeriodeAcompteNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbAcompteTransformeSurPeriode() {
		return nbAcompteTransformeSurPeriode;
	}


	public void setNbAcompteTransformeSurPeriode(int nbAcompteTransformeSurPeriode) {
		this.nbAcompteTransformeSurPeriode = nbAcompteTransformeSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAcompteNonTransformeARelancer() {
		return listeCaPeriodeAcompteNonTransformeARelancer;
	}


	public void setListeCaPeriodeAcompteNonTransformeARelancer(
			List<DocumentChiffreAffaireDTO> listeCaPeriodeAcompteNonTransformeARelancer) {
		this.listeCaPeriodeAcompteNonTransformeARelancer = listeCaPeriodeAcompteNonTransformeARelancer;
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


	public void setBarChartModelAcompte(BarChartModel barChartModelAcompte) {
		this.barChartModelAcompte = barChartModelAcompte;
	}


	


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAcompte() {
		return totauxCaPeriodeAcompte;
	}


	public void setTotauxCaPeriodeAcompte(DocumentChiffreAffaireDTO totauxCaPeriodeAcompte) {
		this.totauxCaPeriodeAcompte = totauxCaPeriodeAcompte;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAcompteNonTransforme() {
		return totauxCaPeriodeAcompteNonTransforme;
	}


	public void setTotauxCaPeriodeAcompteNonTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeAcompteNonTransforme) {
		this.totauxCaPeriodeAcompteNonTransforme = totauxCaPeriodeAcompteNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAcompteNonTransformeARelancer() {
		return totauxCaPeriodeAcompteNonTransformeARelancer;
	}


	public void setTotauxCaPeriodeAcompteNonTransformeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeAcompteNonTransformeARelancer) {
		this.totauxCaPeriodeAcompteNonTransformeARelancer = totauxCaPeriodeAcompteNonTransformeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAcompteTransforme() {
		return totauxCaPeriodeAcompteTransforme;
	}


	public void setTotauxCaPeriodeAcompteTransforme(DocumentChiffreAffaireDTO totauxCaPeriodeAcompteTransforme) {
		this.totauxCaPeriodeAcompteTransforme = totauxCaPeriodeAcompteTransforme;
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


	public BarChartModel getBarChartModelAcompteJour() {
		return barChartModelAcompteJour;
	}


	public void setBarChartModelAcompteJour(BarChartModel barChartModelAcompteJour) {
		this.barChartModelAcompteJour = barChartModelAcompteJour;
	}

}
