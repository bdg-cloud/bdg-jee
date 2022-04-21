package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
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
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.InfosJours;
import fr.legrain.lib.data.InfosMois;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardAvoirController/*<MoisExoCourant>*/ extends DashBoardDocumentController implements Serializable {

	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2461636944048269235L;
	private @EJB ITaAvoirServiceRemote taAvoirService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

//	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelAvoir;
    private BarChartModel barChartModelAvoirJour;



	private List<DocumentChiffreAffaireDTO> listeCaMois;
//    private Date dateDebut;
//    private Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatAvoir;
    private List<DocumentDTO> valueLignesAvoir;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAvoir;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAvoirNonAffectes;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAvoirNonAffectesARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvoir;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvoirNonTotalementPayee;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvoirNonTotalementPayeeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvoirTotalementPayee;    
    private int nbAvoirSurPeriode = 0;
    private int nbAvoirNonAffectesSurPeriode = 0 ;
    private int nbAvoirAffectesSurPeriode = 0;
    private int nbAvoirNonAffectesARelancerSurPeriode = 0;
    private BigDecimal tauxAffectesNb = BigDecimal.ZERO;
    private BigDecimal tauxAffectesCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;    
    private BigDecimal montantTotalHtAffectes = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonAffectes = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonAffectesARelancer = BigDecimal.ZERO;
    private String cSbNbAvoirSurPeriode;
    private String cSbNbAvoirAffecteSurPeriode;
    private String cSbNbAvoirNonAffecteSurPeriode;
    private String cSbNbAvoirNonAffecteARelancerSurPeriode;
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
//    	infoEntreprise = taInfoEntrepriseService.findInstance();
//    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
//    	codeEtatAvoir = "tous";
//    	barChartModelAvoir = new BarChartModel();
    	//Initialisation des données sur la période exercice
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	actRechercheAvoir(codeEtatDocument);
    }


    public void refreshDashBoard(String etatAvoir){
    	setAfficheDocumentJour(false); 
    	setAfficheDocumentJourDataTable(false); 
    	listeDocumentDetailMois.clear(); 
    	listeDocumentDetailJour.clear(); 
    	listeDocumentArticleParTiersParMois.clear(); 
    	listeDocumentArticleParTiersParJour.clear(); 
    	actRechercheAvoir(etatAvoir);
    }


	public void actRechercheAvoir(String codeEtatAvoir){
		
//		barChartModelAvoir.clear();
		setCodeEtatDocument(codeEtatAvoir);
    	setInfoEtatDocument(codeEtatAvoir);

    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeAvoir = taAvoirService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeAvoirNonTotalementPayee = taAvoirService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeAvoirNonTotalementPayeeARelancer = taAvoirService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 0,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeAvoirTotalementPayee = taAvoirService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre total de documents sur la période
		nbAvoirSurPeriode = (int) taAvoirService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de Avoir non totalement payés sur la période
		nbAvoirNonAffectesSurPeriode = (int) taAvoirService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de Avoir à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbAvoirNonAffectesARelancerSurPeriode = (int) taAvoirService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 0,null);
	// Retourne le nombre de Avoirtransformés sur la période
		nbAvoirAffectesSurPeriode = (int) taAvoirService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);    	

		if (nbAvoirSurPeriode > 0) {
			try {
				tauxAffectesNb =  (new BigDecimal(nbAvoirAffectesSurPeriode) .divide(new BigDecimal(nbAvoirSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxAffectesCa = totauxCaPeriodeAvoirTotalementPayee.getMtTtcCalc() .divide(totauxCaPeriodeAvoir.getMtTtcCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		switch (codeEtatAvoir) {
		case ETAT_DOCUMENT_TOUS:
			// Renvoi la liste des documents présents sur la période en cours
			valueLignesDocument = taAvoirService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelAvoir = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvoirService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAvoir.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Avoir");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_AVOIR, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesDocument = taAvoirService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelAvoir = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvoirService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAvoir.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesFacture = taAvoirService.findFactureNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taAvoirService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Avoir");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
							  CSS_DBOARD_BLOC_ACTIF_AVOIR, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);

			break;
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taAvoirService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelAvoir = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvoirService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAvoir.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			valueLignesFacture = taAvoirService.findFactureTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taAvoirService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Avoir");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL,
					  		  CSS_DBOARD_BLOC_ACTIF_AVOIR);
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			valueLignesDocument = taAvoirService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 0,null);
			barChartModelAvoir = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvoirService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,0,null);
			barChartModelAvoir.addSeries(createBarChartSerieMensuelAvoir("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			valueLignesFacture = taAvoirService.findFactureNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taAvoirService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Avoir");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  		  CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  		  CSS_DBOARD_BLOC_ACTIF_AVOIR, 
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
//		setAfficheAvoirJour(true);
		setAfficheDocumentJour(true);
		setAfficheDocumentJourDataTable(false);
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Avoir");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taAvoirService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Avoir");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taAvoirService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taAvoirService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taAvoirService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taAvoirService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelAvoirJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelAvoirJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAvoirService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Avoir");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAvoirService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taAvoirService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taAvoirService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelAvoirJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAvoirJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAvoirService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Avoir");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAvoirService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taAvoirService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taAvoirService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelAvoirJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAvoirJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAvoirService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Avoir");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAvoirService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taAvoirService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taAvoirService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelAvoirJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAvoirJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
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
	montantTotalHtJour = taAvoirService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taAvoirService.findAllDTOPeriode(jourMois, jourMois,null);
	setListeDocumentArticleParTiersParJour(taAvoirService.findArticlesParTiersParMois(jourMois, jourMois));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Avoir");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAvoirService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvoirService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAvoirService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Avoir");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAvoirService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvoirService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taAvoirService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Avoir");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaAvoir.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taAvoirService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvoirService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAvoirService.findArticlesParTiersTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Avoir");
		break;
	}
		
    }
	
	public void actImprimerListeAvoir(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", "Liste des avoirs");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeAvoir", valueLignesDocument);
	
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
	
//    	switch (codeEtatAvoir) {
//		case "tous":
////			// Renvoi la liste des documents présents sur la période en cours
////			valueLignesAvoir = taAvoirService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			// Création et initialisation du graphique du CA HT / mois pour la période en cours
////			barChartModelAvoir = createBarModel(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), barChartModelAvoir, TaAvoir.TYPE_DOC, 1 );
////			// Calcule les CA (HT, TVA, TTC) total sur la période
//			listeCaPeriodeAvoir = findChiffreAffaireTotalDocument(TaAvoir.TYPE_DOC, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			// Calcule le CA (HT, TVA, TTC) total des Avoir non transformé sur la période
//////			listeCaPeriodeAvoirNonTransforme = taAvoirService.findChiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			listeCaPeriodeAvoirNonTransforme = findChiffreAffaireNonTransformeTotalDTODocument(TaAvoir.TYPE_DOC, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			// Calcule le CA (HT, TVA, TTC) total des Avoir non transformé sur la période
////			listeCaPeriodeAvoirNonTransformeARelancer = taAvoirService.caAvoirNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
////			// Retourne le nombre de Avoir non payés sur la période
//			nbAvoirNonPayesSurPeriode = (int) taAvoirService.countAvoirNonPaye(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			// Retourne le nombre de Avoir à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
//			nbAvoirNonPayesARelancerSurPeriode = (int) taAvoirService.countAvoirNonPayesARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			
//			
//			
//			
//			if (nbAvoirSurPeriode > 0) {
//				try {
//					montantTotalHt = listeCaPeriodeAvoir.get(0).getMtHtCalc();
//					nbAvoirPayesSurPeriode = nbAvoirSurPeriode - nbAvoirNonPayesSurPeriode;
//					tauxPayesNb =  (new BigDecimal(nbAvoirPayesSurPeriode) .divide(new BigDecimal(nbAvoirSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//					if ((listeCaPeriodeAvoirNonPayes != null && ! listeCaPeriodeAvoirNonPayes.isEmpty())) {
//						MontantTotalHtNonPayes = listeCaPeriodeAvoirNonPayes.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//					if ((listeCaPeriodeAvoir != null && ! listeCaPeriodeAvoir.isEmpty()) || (listeCaPeriodeAvoirNonPayes != null && ! listeCaPeriodeAvoirNonPayes.isEmpty())) {
//						MontantTotalHtPayes = listeCaPeriodeAvoir.get(0).getMtHtCalc() .subtract(listeCaPeriodeAvoirNonPayes.get(0).getMtHtCalc());
//						tauxPayesCa =  (MontantTotalHtPayes .divide(listeCaPeriodeAvoir.get(0).getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)).setScale(0,BigDecimal.ROUND_HALF_UP);
//					}
//					if (listeCaPeriodeAvoirNonPayesARelancer != null && ! listeCaPeriodeAvoirNonPayesARelancer.isEmpty()) {
//						MontantTotalHtNonPayesARelancer = listeCaPeriodeAvoirNonPayesARelancer.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					// TODO: handle exception
//				}
//				
//				
//			}
//			// Initialisent les classes css pour les block d'information
//			cSbNbAvoirSurPeriode = "redback";
//			cSbNbAvoirPayesSurPeriode = "whiteback";
//			cSbNbAvoirNonPayesSurPeriode = "whiteback";
//			cSbNbAvoirNonPayesARelancerSurPeriode = "whiteback";
//			break;
//
//		case "nonpaye":
////			valueLignesAvoir = taAvoirService.findAvoirNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirSurPeriode = (int) taAvoirService.countAvoir(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirNonTransformeSurPeriode = (int) taAvoirService.countAvoirNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirNonTransformeARelancerSurPeriode = (int) taAvoirService.countAvoirNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			cSbNbAvoirSurPeriode = "whiteback";
//			cSbNbAvoirPayesSurPeriode = "whiteback";
//			cSbNbAvoirNonPayesSurPeriode = "redback";			
//			cSbNbAvoirNonPayesARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "payes":
////			valueLignesAvoir = taAvoirService.findAvoirTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirSurPeriode = (int) taAvoirService.countAvoir(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirNonTransformeSurPeriode = (int) taAvoirService.countAvoirNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirNonTransformeARelancerSurPeriode = (int) taAvoirService.countAvoirNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			cSbNbAvoirSurPeriode = "whiteback";
//			cSbNbAvoirPayesSurPeriode = "redback";
//			cSbNbAvoirNonPayesSurPeriode = "whiteback";			
//			cSbNbAvoirNonPayesARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "nonpayearelancer":
////			valueLignesAvoir = taAvoirService.findAvoirNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
////			nbAvoirSurPeriode = (int) taAvoirService.countAvoir(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirNonTransformeSurPeriode = (int) taAvoirService.countAvoirNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
////			nbAvoirNonTransformeARelancerSurPeriode = (int) taAvoirService.countAvoirNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			cSbNbAvoirSurPeriode = "whiteback";
//			cSbNbAvoirPayesSurPeriode = "whiteback";
//			cSbNbAvoirNonPayesSurPeriode = "whiteback";			
//			cSbNbAvoirNonPayesARelancerSurPeriode = "redback";
//			break;
//			
//		default:
//			break;
//		}
//    	
//    }

	public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelAvoir() {
		return barChartModelAvoir;
	}
	public void setBarChartModelAvoirExo(BarChartModel barChartModelAvoir) {
		this.barChartModelAvoir = barChartModelAvoir;
	}
	



	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public String getCodeEtatAvoir() {
		return codeEtatAvoir;
	}

	public void setCodeEtatAvoir(String codeEtatAvoir) {
		this.codeEtatAvoir = codeEtatAvoir;
	}

	public List<DocumentDTO> getValueLignesAvoir() {
		return valueLignesAvoir;
	}

	public void setValueLignesAvoir(List<DocumentDTO> valueLignesAvoir) {
		this.valueLignesAvoir = valueLignesAvoir;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAvoir() {
		return listeCaPeriodeAvoir;
	}

	public void setListeCaPeriodeAvoir(List<DocumentChiffreAffaireDTO> listeCaPeriodeAvoir) {
		this.listeCaPeriodeAvoir = listeCaPeriodeAvoir;
	}

	public int getNbAvoirSurPeriode() {
		return nbAvoirSurPeriode;
	}

	public void setNbAvoirSurPeriode(int nbAvoirSurPeriode) {
		this.nbAvoirSurPeriode = nbAvoirSurPeriode;
	}



	public String getcSbNbAvoirSurPeriode() {
		return cSbNbAvoirSurPeriode;
	}


	public void setcSbNbAvoirSurPeriode(String cSbNbAvoirSurPeriode) {
		this.cSbNbAvoirSurPeriode = cSbNbAvoirSurPeriode;
	}



	public String getOptionPeriodePrecedente() {
		return optionPeriodePrecedente;
	}


	public void setOptionPeriodePrecedente(String optionPeriodePrecedente) {
		this.optionPeriodePrecedente = optionPeriodePrecedente;
	}



	public void setMontantTotalHtNonPayesARelancer(BigDecimal montantTotalHtNonPayesARelancer) {
		montantTotalHtNonPayesARelancer = montantTotalHtNonPayesARelancer;
	}




	public void setMontantTotalHtPayes(BigDecimal montantTotalHtPayes) {
		montantTotalHtPayes = montantTotalHtPayes;
	}



	public BigDecimal getMontantTotalHt() {
		return montantTotalHt;
	}


	public void setMontantTotalHt(BigDecimal montantTotalHt) {
		montantTotalHt = montantTotalHt;
	}



	public void setMontantTotalHtNonPayes(BigDecimal montantTotalHtNonPayes) {
		montantTotalHtNonPayes = montantTotalHtNonPayes;
	}


	public String getcSbNbAvoirAffecteSurPeriode() {
		return cSbNbAvoirAffecteSurPeriode;
	}


	public void setcSbNbAvoirAffecteSurPeriode(String cSbNbAvoirAffecteSurPeriode) {
		this.cSbNbAvoirAffecteSurPeriode = cSbNbAvoirAffecteSurPeriode;
	}


	public String getcSbNbAvoirNonAffecteSurPeriode() {
		return cSbNbAvoirNonAffecteSurPeriode;
	}


	public void setcSbNbAvoirNonAffecteSurPeriode(String cSbNbAvoirNonAffecteSurPeriode) {
		this.cSbNbAvoirNonAffecteSurPeriode = cSbNbAvoirNonAffecteSurPeriode;
	}


	public String getcSbNbAvoirNonAffecteARelancerSurPeriode() {
		return cSbNbAvoirNonAffecteARelancerSurPeriode;
	}


	public void setcSbNbAvoirNonAffecteARelancerSurPeriode(String cSbNbAvoirNonAffecteARelancerSurPeriode) {
		this.cSbNbAvoirNonAffecteARelancerSurPeriode = cSbNbAvoirNonAffecteARelancerSurPeriode;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAvoirNonAffectes() {
		return listeCaPeriodeAvoirNonAffectes;
	}


	public void setListeCaPeriodeAvoirNonAffectes(List<DocumentChiffreAffaireDTO> listeCaPeriodeAvoirNonAffectes) {
		this.listeCaPeriodeAvoirNonAffectes = listeCaPeriodeAvoirNonAffectes;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAvoirNonAffectesARelancer() {
		return listeCaPeriodeAvoirNonAffectesARelancer;
	}


	public void setListeCaPeriodeAvoirNonAffectesARelancer(
			List<DocumentChiffreAffaireDTO> listeCaPeriodeAvoirNonAffectesARelancer) {
		this.listeCaPeriodeAvoirNonAffectesARelancer = listeCaPeriodeAvoirNonAffectesARelancer;
	}


	public int getNbAvoirNonAffectesSurPeriode() {
		return nbAvoirNonAffectesSurPeriode;
	}


	public void setNbAvoirNonAffectesSurPeriode(int nbAvoirNonAffectesSurPeriode) {
		this.nbAvoirNonAffectesSurPeriode = nbAvoirNonAffectesSurPeriode;
	}


	public int getNbAvoirAffectesSurPeriode() {
		return nbAvoirAffectesSurPeriode;
	}


	public void setNbAvoirAffectesSurPeriode(int nbAvoirAffectesSurPeriode) {
		this.nbAvoirAffectesSurPeriode = nbAvoirAffectesSurPeriode;
	}


	public int getNbAvoirNonAffectesARelancerSurPeriode() {
		return nbAvoirNonAffectesARelancerSurPeriode;
	}


	public void setNbAvoirNonAffectesARelancerSurPeriode(int nbAvoirNonAffectesARelancerSurPeriode) {
		this.nbAvoirNonAffectesARelancerSurPeriode = nbAvoirNonAffectesARelancerSurPeriode;
	}


	public BigDecimal getMontantTotalHtAffectes() {
		return montantTotalHtAffectes;
	}


	public void setMontantTotalHtAffectes(BigDecimal montantTotalHtAffectes) {
		this.montantTotalHtAffectes = montantTotalHtAffectes;
	}


	public BigDecimal getMontantTotalHtNonAffectes() {
		return montantTotalHtNonAffectes;
	}


	public void setMontantTotalHtNonAffectes(BigDecimal montantTotalHtNonAffectes) {
		this.montantTotalHtNonAffectes = montantTotalHtNonAffectes;
	}


	public BigDecimal getMontantTotalHtNonAffectesARelancer() {
		return montantTotalHtNonAffectesARelancer;
	}


	public void setMontantTotalHtNonAffectesARelancer(BigDecimal montantTotalHtNonAffectesARelancer) {
		this.montantTotalHtNonAffectesARelancer = montantTotalHtNonAffectesARelancer;
	}


	public BigDecimal getTauxAffectesNb() {
		return tauxAffectesNb;
	}


	public void setTauxAffectesNb(BigDecimal tauxAffectesNb) {
		this.tauxAffectesNb = tauxAffectesNb;
	}


	public BigDecimal getTauxAffectesCa() {
		return tauxAffectesCa;
	}


	public void setTauxAffectesCa(BigDecimal tauxAffectesCa) {
		this.tauxAffectesCa = tauxAffectesCa;
	}

	protected ChartSeries createBarChartSerieMensuelAvoir(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
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

        // complète la liste des mois manquant dans la requête puisque celle-ci ne remonte que les mois qui ont 
        //un chiffre d'affaire
        for (InfosMois ligneInfosMois : listMoisPeriode) {
        	DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee(), listeMontantJmaDocument); 
        	if (objMoisAnneeCa!= null ){
        		barreGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),objMoisAnneeCa.getResteAReglerComplet());
        	} else {
        		barreGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),0);
        	}

		}
//		Calendar mCalendar = Calendar.getInstance();
//        for (DocumentChiffreAffaireDTO ligneInfosMois : listeMontantJmaDocument) {
//        		mCalendar.setTime(LibDate.stringToDate2("01"+ligneInfosMois.getMois()+ligneInfosMois.getAnnee()));
//        		barreGraphique.set(mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), ligneInfosMois.getMtHtCalc());

//        }
        return barreGraphique;
	}
	
	
	protected ChartSeries createBarChartSerieJourAvoir(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
		
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
        		barreGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),objJourAnneeCa.getResteAReglerComplet());
        	} else {
        		barreGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),0);
        	}

  		}
        
        return barreGraphique;
	}


	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}


	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvoir() {
		return totauxCaPeriodeAvoir;
	}


	public void setTotauxCaPeriodeAvoir(DocumentChiffreAffaireDTO totauxCaPeriodeAvoir) {
		this.totauxCaPeriodeAvoir = totauxCaPeriodeAvoir;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvoirNonTotalementPayee() {
		return totauxCaPeriodeAvoirNonTotalementPayee;
	}


	public void setTotauxCaPeriodeAvoirNonTotalementPayee(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvoirNonTotalementPayee) {
		this.totauxCaPeriodeAvoirNonTotalementPayee = totauxCaPeriodeAvoirNonTotalementPayee;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvoirNonTotalementPayeeARelancer() {
		return totauxCaPeriodeAvoirNonTotalementPayeeARelancer;
	}


	public void setTotauxCaPeriodeAvoirNonTotalementPayeeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvoirNonTotalementPayeeARelancer) {
		this.totauxCaPeriodeAvoirNonTotalementPayeeARelancer = totauxCaPeriodeAvoirNonTotalementPayeeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvoirTotalementPayee() {
		return totauxCaPeriodeAvoirTotalementPayee;
	}


	public void setTotauxCaPeriodeAvoirTotalementPayee(DocumentChiffreAffaireDTO totauxCaPeriodeAvoirTotalementPayee) {
		this.totauxCaPeriodeAvoirTotalementPayee = totauxCaPeriodeAvoirTotalementPayee;
	}


	public void setBarChartModelAvoir(BarChartModel barChartModelAvoir) {
		this.barChartModelAvoir = barChartModelAvoir;
	}
	
    public BarChartModel getBarChartModelAvoirJour() {
		return barChartModelAvoirJour;
	}


	public void setBarChartModelAvoirJour(BarChartModel barChartModelAvoirJour) {
		this.barChartModelAvoirJour = barChartModelAvoirJour;
	}
}