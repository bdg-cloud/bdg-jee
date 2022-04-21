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
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.BarChartModel;

import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardReglementController/*<MoisExoCourant>*/ extends DashBoardDocumentController implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6622873062091859179L;

	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	private @EJB ITaReglementServiceRemote taReglementService;
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

//	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelReglement;
    private BarChartModel barChartModelReglementJour; //AJOUTER LE 06/09/2017 Jean Marc
    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    private Date dateDebut;
//    private Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    //private String codeEtatFacture;
//    private List<DocumentDTO> valueLignesFacture;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFacture;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureNonTotalementPayes;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureNonTotalementPayesARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFacture;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayee;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayeeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureTotalementPayee;
    private int nbFactureSurPeriode = 0;
    private int nbFactureNonTotalementPayesSurPeriode = 0 ;
    private int nbFactureTotalementPayesSurPeriode = 0;
    private int nbFactureNonTotalementPayesARelancerSurPeriode = 0;
    private BigDecimal tauxTotalementPayesNb = BigDecimal.ZERO;
    private BigDecimal tauxTotalementPayesCa = BigDecimal.ZERO;
    private BigDecimal MontantTotalHt = BigDecimal.ZERO;    
    private BigDecimal MontantTotalHtTotalementPayes = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTotalementPayes = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTotalementPayesARelancer = BigDecimal.ZERO;
    
    private List<DocumentDTO> listeDocumentDetailMois;
    private List<DocumentDTO> listeDocumentDetailJour;
    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois;
    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour;
    
    private String codeMoisAnnee;
    private String nomMoisDetail;
    private int numeroJourDetail; 
    private boolean afficheDocumentJour; 
    private boolean afficheDocumentJourDataTable; 
    private String infosLabelRapportMensuelDataTable; 
    private String infosLabelRapportArticlesDataTable; 
    private String infosLabelRapportJournalierDataTable;
    private String infosLabelRapportArticlesJourDataTable; 
    private BigDecimal montantTotalHtMois = BigDecimal.ZERO;
    private BigDecimal montantTotalHtJour = BigDecimal.ZERO;
    
//  private Date valeursDateDebut;
//	private Date valeursDateFin;
    
//    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste
//    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument;// Récupère le ca ht par jour dans un liste
    
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	
    	//Initialisation des données sur la période exercice
    	actRechercheReglement(codeEtatDocument);
    }


    public void refresh(String etatReglement){
    	actRechercheReglement(etatReglement);
    }

    public void refreshDashBoard(String etatReglement){
    	setAfficheDocumentJour(false); //AJOUTER LE 06/09/2017 Jean Marc
    	setAfficheDocumentJourDataTable(false); //AJOUTER LE 06/09/2017 Jean Marc
    	if(listeDocumentDetailMois!=null)listeDocumentDetailMois.clear(); //AJOUTER LE 06/09/2017 Jean Marc vide la table à chaque changement de proforma
    	if(listeDocumentDetailJour!=null)listeDocumentDetailJour.clear(); //AJOUTER LE 19/09/2017 Jean Marc vide la table à chaque changement de proforma
    	if(listeDocumentArticleParTiersParMois!=null)listeDocumentArticleParTiersParMois.clear(); //AJOUTER LE 14/09/2017 Jean Marc vide la table à chaque changement de proforma
    	if(listeDocumentArticleParTiersParJour!=null)listeDocumentArticleParTiersParJour.clear(); //AJOUTER LE 20/09/2017 Jean Marc vide la table à chaque changement de proforma
    	actRechercheReglement(etatReglement);
    }

	public void actRechercheReglement(String codeEtatReglement){
//		barChartModelFacture.clear();
    	setInfoEtatDocument(codeEtatReglement);

    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeFacture = taReglementService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeFactureNonTotalementPayee = taReglementService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeFactureNonTotalementPayeeARelancer = taReglementService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 0,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeFactureTotalementPayee = taReglementService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre total de documents sur la période
		nbFactureSurPeriode = (int) taReglementService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de Facture non totalement payés sur la période
		nbFactureNonTotalementPayesSurPeriode = (int) taReglementService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de Facture à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbFactureNonTotalementPayesARelancerSurPeriode = (int) taReglementService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 0,null);
	// Retourne le nombre de Facturetransformés sur la période
		nbFactureTotalementPayesSurPeriode = (int) taReglementService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);    	

		if (nbFactureSurPeriode > 0) {
			try {
				tauxTotalementPayesNb =  (new BigDecimal(nbFactureTotalementPayesSurPeriode) .divide(new BigDecimal(nbFactureSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTotalementPayesCa = totauxCaPeriodeFactureTotalementPayee.getMtTtcCalc() .divide(totauxCaPeriodeFacture.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		switch (codeEtatReglement) {
		case ETAT_DOCUMENT_TOUS:
			// Renvoi la liste des documents présents sur la période en cours
			valueLignesDocument = taReglementService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux mensuel", true, -50);
			listeCaMoisDocument = taReglementService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			
			initInfosLabelRapportMensuelDataTable("Règlement");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_REGLEMENT, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesDocument = taReglementService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux mensuel", true, -50);
			listeCaMoisDocument = taReglementService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesFacture = taReglementService.findFactureNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taReglementService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Règlement");
			
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
							  CSS_DBOARD_BLOC_ACTIF_REGLEMENT, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);

			break;
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taReglementService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux mensuel", true, -50);
			listeCaMoisDocument = taReglementService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			
//			valueLignesFacture = taReglementService.findFactureTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taReglementService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Règlement");
			
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL,
					  		  CSS_DBOARD_BLOC_ACTIF_REGLEMENT);
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			valueLignesDocument = taReglementService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 0,null);
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux mensuel", true, -50);
			listeCaMoisDocument = taReglementService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,0,null);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			
//			valueLignesFacture = taReglementService.findFactureNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taReglementService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Règlement");
			
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  		  CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  		  CSS_DBOARD_BLOC_ACTIF_REGLEMENT, 
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
//		setAfficheProformaJour(true);
		setAfficheDocumentJour(true);
		setAfficheDocumentJourDataTable(false);
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Règlement");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taReglementService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Règlement");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taReglementService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taReglementService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taReglementService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taReglementService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelReglementJour = createBarChartModelSimpleBarre("Totaux par jour", true, -50);
	barChartModelReglementJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taReglementService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Règlement");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taReglementService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taReglementService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taReglementService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelReglementJour = createBarChartModelSimpleBarre("Totaux par jour", true, -50);
		barChartModelReglementJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taReglementService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Règlement");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taReglementService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taReglementService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taReglementService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelReglementJour = createBarChartModelSimpleBarre("Totaux par jour", true, -50);
		barChartModelReglementJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taReglementService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Règlement");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taReglementService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taReglementService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taReglementService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelReglementJour = createBarChartModelSimpleBarre("Totaux par jour", true, -50);
		barChartModelReglementJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
	}
   }
	
  

	
	public void itemSelectJour(ItemSelectEvent event2) throws ParseException {

		setNumeroJourDetail(event2.getItemIndex());
		
		Date jourMois = listJoursAnneeExo.get(event2.getItemIndex()).getDateDuJour();
		setAfficheDocumentJourDataTable(true);
		
	switch (infoEtatDocument) {
	
	case "tous":
	// Cumul du chiffre d'affaire par jour
//	setCumulJourDetail(findChiffreAffaireTotalDocument(TaProforma.TYPE_DOC, jourMois, jourMois));
//	montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
	montantTotalHtJour = taReglementService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taReglementService.findAllDTOPeriode(jourMois, jourMois,null);
	setListeDocumentArticleParTiersParJour(taReglementService.findArticlesParTiersParMois(jourMois, jourMois));
	initInfosLabelRapportMensuelDataTable("Règlement");
	
	break;
	
	case "nontransforme":
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireNonTransformeTotalDTODocument(TaProforma.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taReglementService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taReglementService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taReglementService.findArticlesParTiersNonTransforme(jourMois, jourMois));
		initInfosLabelRapportMensuelDataTable("Règlement");
		
		break;
		
	case "nontransformearelancer":
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = taReglementService.listeChiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taReglementService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taReglementService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taReglementService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
		initInfosLabelRapportMensuelDataTable("Règlement");
	
		break;
		
	case "transforme":
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaProforma.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taReglementService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taReglementService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taReglementService.findArticlesParTiersTransforme(jourMois, jourMois));
		initInfosLabelRapportMensuelDataTable("Règlement");
		
		break;
	}
		
    }
	
	public void actImprimerListeReglement(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", "Liste des Règlements");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeReglement", valueLignesDocument);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public void actImprimerListeReglementMois(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();

		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", "Liste des Règlements du mois de "+nomMoisDetail);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeReglement", listeDocumentDetailMois);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public void actImprimerListeReglementTiersMois(ActionEvent actionEvent) {
	
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", "Liste des Règlements par tiers et mois");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeReglement", listeDocumentArticleParTiersParMois);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
public void actImprimerListeReglementJour(ActionEvent actionEvent) {
	
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", "Liste des Règlements du "+(numeroJourDetail+1)+" "+nomMoisDetail);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeReglement", listeDocumentDetailJour);

	} catch (Exception e) {
		e.printStackTrace();
	}
}

	public BarChartModel getBarChartModelReglement() {
		return barChartModelReglement;
	}
	public void setBarChartModelFactureExo(BarChartModel barChartModelReglement) {
		this.barChartModelReglement = barChartModelReglement;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public List<DocumentDTO> getValueLignesDocument() {
		return valueLignesDocument;
	}

	public void setValueLignesDocument(List<DocumentDTO> valueLignesDocument) {
		this.valueLignesDocument = valueLignesDocument;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeFacture() {
		return listeCaPeriodeFacture;
	}

	public void setListeCaPeriodeFacture(List<DocumentChiffreAffaireDTO> listeCaPeriodeFacture) {
		this.listeCaPeriodeFacture = listeCaPeriodeFacture;
	}

	public int getNbFactureSurPeriode() {
		return nbFactureSurPeriode;
	}

	public void setNbFactureSurPeriode(int nbFactureSurPeriode) {
		this.nbFactureSurPeriode = nbFactureSurPeriode;
	}

	public int getNbFactureNonTotalementPayesSurPeriode() {
		return nbFactureNonTotalementPayesSurPeriode;
	}

	public void setNbFactureNonTotalementPayesSurPeriode(int nbFactureNonPayesSurPeriode) {
		this.nbFactureNonTotalementPayesSurPeriode = nbFactureNonPayesSurPeriode;
	}


	public int getNbFactureNonTotalementPayesARelancerSurPeriode() {
		return nbFactureNonTotalementPayesARelancerSurPeriode;
	}


	public void setNbFactureNonTotalementPayesARelancerSurPeriode(int nbFactureNonPayesARelancerSurPeriode) {
		this.nbFactureNonTotalementPayesARelancerSurPeriode = nbFactureNonPayesARelancerSurPeriode;
	}


	public String getOptionPeriodePrecedente() {
		return optionPeriodePrecedente;
	}


	public void setOptionPeriodePrecedente(String optionPeriodePrecedente) {
		this.optionPeriodePrecedente = optionPeriodePrecedente;
	}


	public BigDecimal getTauxTotalementPayesNb() {
		return tauxTotalementPayesNb;
	}


	public void setTauxTotalementPayesNb(BigDecimal tauxPayesNb) {
		this.tauxTotalementPayesNb = tauxPayesNb;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeFactureNonTotalementPayes() {
		return listeCaPeriodeFactureNonTotalementPayes;
	}


	public void setListeCaPeriodeFactureNonTotalementPayes(List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureNonPayes) {
		this.listeCaPeriodeFactureNonTotalementPayes = listeCaPeriodeFactureNonPayes;
	}


	public BigDecimal getTauxTotalementPayesCa() {
		return tauxTotalementPayesCa;
	}


	public void setTauxTotalementPayesCa(BigDecimal tauxPayesCa) {
		this.tauxTotalementPayesCa = tauxPayesCa;
	}


	public int getNbFactureTotalementPayesSurPeriode() {
		return nbFactureTotalementPayesSurPeriode;
	}


	public void setNbFacturePayesSurPeriode(int nbFacturePayesSurPeriode) {
		this.nbFactureTotalementPayesSurPeriode = nbFacturePayesSurPeriode;
	}


	public BigDecimal getMontantTotalHtNonTotalementPayesARelancer() {
		return MontantTotalHtNonTotalementPayesARelancer;
	}


	public void setMontantTotalHtNonTotalementPayesARelancer(BigDecimal montantTotalHtNonPayesARelancer) {
		MontantTotalHtNonTotalementPayesARelancer = montantTotalHtNonPayesARelancer;
	}


	public BigDecimal getMontantTotalHtTotalementPayes() {
		return MontantTotalHtTotalementPayes;
	}


	public void setMontantTotalHtTotalementPayes(BigDecimal montantTotalHtPayes) {
		MontantTotalHtTotalementPayes = montantTotalHtPayes;
	}


	public BigDecimal getMontantTotalHt() {
		return MontantTotalHt;
	}


	public void setMontantTotalHt(BigDecimal montantTotalHt) {
		MontantTotalHt = montantTotalHt;
	}


	public BigDecimal getMontantTotalHtTotalementNonPayes() {
		return MontantTotalHtNonTotalementPayes;
	}


	public void setMontantTotalHtNonTotalementPayes(BigDecimal montantTotalHtNonPayes) {
		MontantTotalHtNonTotalementPayes = montantTotalHtNonPayes;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFacture() {
		return totauxCaPeriodeFacture;
	}


	public void setTotauxCaPeriodeFacture(DocumentChiffreAffaireDTO totauxCaPeriodeFacture) {
		this.totauxCaPeriodeFacture = totauxCaPeriodeFacture;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFactureNonTotalementPayee() {
		return totauxCaPeriodeFactureNonTotalementPayee;
	}


	public void setTotauxCaPeriodeFactureNonTotalementPayee(
			DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayee) {
		this.totauxCaPeriodeFactureNonTotalementPayee = totauxCaPeriodeFactureNonTotalementPayee;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFactureNonTotalementPayeeARelancer() {
		return totauxCaPeriodeFactureNonTotalementPayeeARelancer;
	}


	public void setTotauxCaPeriodeFactureNonTotalementPayeeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayeeARelancer) {
		this.totauxCaPeriodeFactureNonTotalementPayeeARelancer = totauxCaPeriodeFactureNonTotalementPayeeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFactureTotalementPayee() {
		return totauxCaPeriodeFactureTotalementPayee;
	}


	public void setTotauxCaPeriodeFactureTotalementPayee(DocumentChiffreAffaireDTO totauxCaPeriodeFactureTotalementPayee) {
		this.totauxCaPeriodeFactureTotalementPayee = totauxCaPeriodeFactureTotalementPayee;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}


	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
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


	public boolean isAfficheDocumentJourDataTable() {
		return afficheDocumentJourDataTable;
	}


	public void setAfficheDocumentJourDataTable(boolean afficheDocumentJourDataTable) {
		this.afficheDocumentJourDataTable = afficheDocumentJourDataTable;
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


	public BarChartModel getBarChartModelReglementJour() {
		return barChartModelReglementJour;
	}


	public void setBarChartModelReglementJour(BarChartModel barChartModelReglementJour) {
		this.barChartModelReglementJour = barChartModelReglementJour;
	}


	public void setBarChartModelReglement(BarChartModel barChartModelReglement) {
		this.barChartModelReglement = barChartModelReglement;
	}

}
