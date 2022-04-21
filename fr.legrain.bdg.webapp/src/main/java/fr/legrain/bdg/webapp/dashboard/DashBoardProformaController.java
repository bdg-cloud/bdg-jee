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

import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardProformaController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1363000298058005675L;
	/**
	 * 
	 */
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	private @EJB ITaProformaServiceRemote taProformaService;
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

//	private TaInfoEntreprise infoEntreprise;
//    public List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelProforma;
    private BarChartModel barChartModelProformaJour; //AJOUTER LE 06/09/2017 Jean Marc
//    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    public Date dateDebut;
//    public Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
//    private String codeEtatProforma;
    private DocumentChiffreAffaireDTO totauxCaPeriodeProforma;
    private DocumentChiffreAffaireDTO totauxCaPeriodeProformaNonTransforme;
    private DocumentChiffreAffaireDTO totauxCaPeriodeProformaNonTransformeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeProformaTransforme;
//    private List<DocumentDTO> valueLignesProforma;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeProforma;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeProformaNonTransforme;
    private int nbProformaSurPeriode = 0;
    private int nbProformaNonTransformeSurPeriode = 0 ;
    private int nbProformaTransformeSurPeriode = 0;
    private int nbProformaNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;

//    private List<DocumentDTO> listeDocumentDetailMois;
//    private List<DocumentDTO> listeDocumentDetailJour;
//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois;
//    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour;
    
//    private String codeMoisAnnee;
   
    private String nomMoisDetail;
    private int numeroJourDetail; //AJOUTER LE 06/09/2017 Jean Marc
//    private boolean afficheProformaJour; //AJOUTER LE 06/09/2017 Jean Marc
//    private boolean afficheProformaJourDataTable; //AJOUTER LE 06/09/2017 Jean Marc
//    private String infosLabelRapportMensuelDataTable; //AJOUTER LE 14/09/2017 Jean Marc
//    private String infosLabelRapportArticlesDataTable; //AJOUTER LE 14/09/2017 Jean Marc
//    private String infosLabelRapportJournalierDataTable; //AJOUTER LE 19/09/2017 Jean Marc
//    private String infosLabelRapportArticlesJourDataTable; //AJOUTER LE 19/09/2017 Jean Marc
    
    private List<DocumentChiffreAffaireDTO> cumulMoisDetail;
    private List<DocumentChiffreAffaireDTO> cumulJourDetail; //AJOUTER LE 06/09/2017 Jean Marc
//    private BigDecimal montantTotalHtMois = BigDecimal.ZERO;
//    private BigDecimal montantTotalHtJour = BigDecimal.ZERO;
   
//    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste
//    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument;// Récupère le ca ht par jour dans un liste
    
//    private List<InfosMois> listMoisAnneeExo;
//    private List<InfosJours> listJoursAnneeExo;
    
    @PostConstruct
	public void postConstruct(){

    	super.postConstruct();
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	//Initialisation des données sur la période exercice
    	actRechercheProforma(codeEtatDocument);
    	
    }


    public void refreshDashBoard(String etatProforma){
    	setAfficheDocumentJour(false); //AJOUTER LE 06/09/2017 Jean Marc
    	setAfficheDocumentJourDataTable(false); //AJOUTER LE 06/09/2017 Jean Marc
    	listeDocumentDetailMois.clear(); //AJOUTER LE 06/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentDetailJour.clear(); //AJOUTER LE 19/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParMois.clear(); //AJOUTER LE 14/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParJour.clear(); //AJOUTER LE 20/09/2017 Jean Marc vide la table à chaque changement de proforma
    	actRechercheProforma(etatProforma);
    	
    }


    public void actRechercheProforma(String codeEtatProforma){

    	setInfoEtatDocument(codeEtatProforma);
    	setCodeEtatDocument(codeEtatProforma);
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));

    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeProforma = taProformaService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeProformaNonTransforme = taProformaService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeProformaNonTransformeARelancer = taProformaService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeProformaTransforme = taProformaService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Retourne le nombre total de documents sur la période
		nbProformaSurPeriode = (int) taProformaService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma non transformés sur la période
		nbProformaNonTransformeSurPeriode = (int) taProformaService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbProformaNonTransformeARelancerSurPeriode = (int) taProformaService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		// Retourne le nombre de Proforma transformés sur la période
		nbProformaTransformeSurPeriode = (int) taProformaService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		tauxTransfoNb = BigDecimal.ZERO;
		tauxTransfoCa = BigDecimal.ZERO;
		
		if (nbProformaSurPeriode > 0) {
			try {
				tauxTransfoNb =  (new BigDecimal(nbProformaTransformeSurPeriode) .divide(new BigDecimal(nbProformaSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTransfoCa = totauxCaPeriodeProformaTransforme.getMtHtCalc() .divide(totauxCaPeriodeProforma.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		
    	switch (codeEtatProforma) {
		case ETAT_DOCUMENT_TOUS:
//			// Renvoi la liste des documents présents sur la période en cours
			valueLignesDocument = taProformaService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des proforma réalisés en €";
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taProformaService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			barChartModelProforma.clear();

//			etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Proforma");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_PROFORMA, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);

			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesDocument = taProformaService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des proforma non transformé en €";
			barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taProformaService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			barChartModelProforma.clear();
//			etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Proforma");
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL,
								CSS_DBOARD_BLOC_ACTIF_PROFORMA, 
								CSS_DBOARD_BLOC_INACTIF_ALL, 
								CSS_DBOARD_BLOC_INACTIF_ALL);			

			//setInfoCase("non transformer");
			
			break;
			
			
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taProformaService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des proforma transformé en €";
			barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taProformaService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//			barChartModelProforma.clear();
//			barChartModelProformaJour.clear();
//			etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Proforma");
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  			CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  			CSS_DBOARD_BLOC_ACTIF_PROFORMA);

			//setInfoCase("transformer");
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			try {
			valueLignesDocument = taProformaService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			infoLabel = "Liste des proforma à relancer en €";
			barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taProformaService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1, 15,null);
			barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//			//listeCaPeriodeProforma = taProformaService.findCAProformaSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			barChartModelProforma.clear();
//			barChartModelProformaJour.clear();
//			etatInfosLabelRapportMensuelDataTable();
			initInfosLabelRapportMensuelDataTable("Proforma");
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
	  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
	  		  		  			CSS_DBOARD_BLOC_ACTIF_PROFORMA, 
	  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL);

			//setInfoCase("non transformer à relancer");
			break;
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		default:
			break;
		}
    	
    }
    
    public void actImprimerListeProforma(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", infoLabel);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeProforma", valueLignesDocument);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void actImprimerListeProformaDocMois(ActionEvent actionEvent) {


    	try {
    				
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

    	Map<String, Object> sessionMap = externalContext.getSessionMap();

    	Map<String,Object> mapParametre = new HashMap<String,Object>();
    	mapParametre.put("infosLabel", infosLabelRapportMensuelDataTable);

    	sessionMap.put("parametre", mapParametre);
    	sessionMap.put("listeProforma", listeDocumentDetailMois);

    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	}

    	public void actImprimerListeProformaDocDetailMois(ActionEvent actionEvent) {
    		
    		
    		try {
    					
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    		Map<String, Object> sessionMap = externalContext.getSessionMap();
    		Map<String, Object> sessionMapInfos = externalContext.getSessionMap();
    		Map<String,Object> mapParametre = new HashMap<String,Object>();

    		mapParametre.put("infosLabel", infosLabelRapportArticlesDataTable);
    		
    		sessionMap.put("parametre", mapParametre);
    		sessionMapInfos.put("infosLabel", getInfosLabelRapportArticlesDataTable());
    		sessionMap.put("listeArticleDetail", listeDocumentArticleParTiersParMois);

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}

    	public void actImprimerListeProformaDocJour(ActionEvent actionEvent) {
    		
    		
    		try {
    					
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    		
    		Map<String, Object> sessionMap = externalContext.getSessionMap();
    		
    		Map<String,Object> mapParametre = new HashMap<String,Object>();
    		mapParametre.put("infosLabel", infosLabelRapportJournalierDataTable);
    		
    		sessionMap.put("parametre", mapParametre);
    		sessionMap.put("listeProforma", listeDocumentDetailJour);

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    		
    		public void actImprimerListeProformaDocDetailJour(ActionEvent actionEvent) {
    			
    			
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
	public BarChartModel getBarChartModelProforma() {
		return barChartModelProforma;
	}
	public void setBarChartModelProforma(BarChartModel barChartModelProforma) {
		this.barChartModelProforma = barChartModelProforma;
	}
	
	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

//	public String getCodeEtatProforma() {
//		return codeEtatProforma;
//	}
//
//	public void setCodeEtatProforma(String codeEtatProforma) {
//		this.codeEtatProforma = codeEtatProforma;
//	}

//	public List<DocumentDTO> getValueLignesProforma() {
//		return valueLignesProforma;
//	}
//
//	public void setValueLignesProforma(List<DocumentDTO> valueLignesProforma) {
//		this.valueLignesProforma = valueLignesProforma;
//	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeProforma() {
		return listeCaPeriodeProforma;
	}

	public void setListeCaPeriodeProforma(List<DocumentChiffreAffaireDTO> listeCaPeriodeProforma) {
		this.listeCaPeriodeProforma = listeCaPeriodeProforma;
	}

	public int getNbProformaSurPeriode() {
		return nbProformaSurPeriode;
	}

	public void setNbProformaSurPeriode(int nbProformaSurPeriode) {
		this.nbProformaSurPeriode = nbProformaSurPeriode;
	}

	public int getNbProformaNonTransformeSurPeriode() {
		return nbProformaNonTransformeSurPeriode;
	}

	public void setNbProformaNonTransformeSurPeriode(int nbProformaNonTransformeSurPeriode) {
		this.nbProformaNonTransformeSurPeriode = nbProformaNonTransformeSurPeriode;
	}


	public int getNbProformaNonTransformeARelancerSurPeriode() {
		return nbProformaNonTransformeARelancerSurPeriode;
	}


	public void setNbProformaNonTransformeARelancerSurPeriode(int nbProformaNonTransformeARelancerSurPeriode) {
		this.nbProformaNonTransformeARelancerSurPeriode = nbProformaNonTransformeARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeProformaNonTransforme() {
		return listeCaPeriodeProformaNonTransforme;
	}


	public void setListeCaPeriodeProformaNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeProformaNonTransforme) {
		this.listeCaPeriodeProformaNonTransforme = listeCaPeriodeProformaNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbProformaTransformeSurPeriode() {
		return nbProformaTransformeSurPeriode;
	}


	public void setNbProformaTransformeSurPeriode(int nbProformaTransformeSurPeriode) {
		this.nbProformaTransformeSurPeriode = nbProformaTransformeSurPeriode;
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
		initInfosLabelRapportMensuelDataTable("Proforma");
		System.out.println(codeMoisAnnee);
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taProformaService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Proforma");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taProformaService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taProformaService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	listeCaJoursDocument = taProformaService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 2,null);
	listeCaJoursDocument = taProformaService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	barChartModelProformaJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelProformaJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taProformaService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Proforma");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taProformaService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taProformaService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taProformaService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelProformaJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelProformaJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taProformaService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Proforma");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taProformaService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taProformaService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taProformaService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelProformaJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelProformaJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taProformaService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Proforma");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taProformaService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taProformaService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taProformaService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelProformaJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelProformaJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
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
	montantTotalHtJour = taProformaService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taProformaService.findAllDTOPeriode(jourMois, jourMois,null);
	setListeDocumentArticleParTiersParJour(taProformaService.findArticlesParTiersParMois(jourMois, jourMois));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Proforma");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taProformaService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taProformaService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taProformaService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Proforma");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taProformaService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taProformaService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taProformaService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Proforma");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaProforma.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taProformaService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taProformaService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taProformaService.findArticlesParTiersTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Proforma");
		break;
	}
		
    }

	public String getNomMoisDetail() {
		return nomMoisDetail;
	}


	public void setNomMoisDetail(String nomMoisDetail) {
		this.nomMoisDetail = nomMoisDetail;
	}


	public List<DocumentChiffreAffaireDTO> getCumulMoisDetail() {
		return cumulMoisDetail;
	}


	public void setCumulMoisDetail(List<DocumentChiffreAffaireDTO> cumulMoisDetail) {
		this.cumulMoisDetail = cumulMoisDetail;
	}


	public BarChartModel getBarChartModelProformaJour() {
		return barChartModelProformaJour;
	}


	public void setBarChartModelProformaJour(BarChartModel barChartModelProformaJour) {
		this.barChartModelProformaJour = barChartModelProformaJour;
	}


	public List<DocumentChiffreAffaireDTO> getCumulJourDetail() {
		return cumulJourDetail;
	}


	public void setCumulJourDetail(List<DocumentChiffreAffaireDTO> cumulJourDetail) {
		this.cumulJourDetail = cumulJourDetail;
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


//	public List<DocumentDTO> getListeDocumentDetailJour() {
//		return listeDocumentDetailJour;
//	}
//
//
//	public void setListeDocumentDetailJour(List<DocumentDTO> listeDocumentDetailJour) {
//		this.listeDocumentDetailJour = listeDocumentDetailJour;
//	}


	public int getNumeroJourDetail() {
		return numeroJourDetail;
	}


	public void setNumeroJourDetail(int numeroJourDetail) {
		this.numeroJourDetail = numeroJourDetail;
	}


//	public boolean isAfficheProformaJour() {
//		return afficheProformaJour;
//	}
//
//
//	public void setAfficheProformaJour(boolean afficheProformaJour) {
//		this.afficheProformaJour = afficheProformaJour;
//	}


//	public boolean isAfficheProformaJourDataTable() {
//		return afficheProformaJourDataTable;
//	}
//
//
//	public void setAfficheProformaJourDataTable(boolean afficheProformaJourDataTable) {
//		this.afficheProformaJourDataTable = afficheProformaJourDataTable;
//	}


//	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParMois() {
//		return listeDocumentArticleParTiersParMois;
//	}
//
//
//	public void setListeDocumentArticleParTiersParMois(List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois) {
//		this.listeDocumentArticleParTiersParMois = listeDocumentArticleParTiersParMois;
//	}


//	public String getInfosLabelRapportMensuelDataTable() {
//		return infosLabelRapportMensuelDataTable;
//	}
//
//
//	public void setInfosLabelRapportMensuelDataTable(String infosLabelRapportMensuelDataTable) {
//		this.infosLabelRapportMensuelDataTable = infosLabelRapportMensuelDataTable;
//	}
//
//
//	public String getInfosLabelRapportArticlesDataTable() {
//		return infosLabelRapportArticlesDataTable;
//	}
//
//
//	public void setInfosLabelRapportArticlesDataTable(String infosLabelRapportArticlesDataTable) {
//		this.infosLabelRapportArticlesDataTable = infosLabelRapportArticlesDataTable;
//	}
//
//
//	public String getInfosLabelRapportJournalierDataTable() {
//		return infosLabelRapportJournalierDataTable;
//	}
//
//
//	public void setInfosLabelRapportJournalierDataTable(String infosLabelRapportJournalierDataTable) {
//		this.infosLabelRapportJournalierDataTable = infosLabelRapportJournalierDataTable;
//	}
//
//
//	public String getInfosLabelRapportArticlesJourDataTable() {
//		return infosLabelRapportArticlesJourDataTable;
//	}
//
//
//	public void setInfosLabelRapportArticlesJourDataTable(String infosLabelRapportArticlesJourDataTable) {
//		this.infosLabelRapportArticlesJourDataTable = infosLabelRapportArticlesJourDataTable;
//	}


//	public List<TaArticlesParTiersDTO> getListeDocumentArticleParTiersParJour() {
//		return listeDocumentArticleParTiersParJour;
//	}
//
//
//	public void setListeDocumentArticleParTiersParJour(List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour) {
//		this.listeDocumentArticleParTiersParJour = listeDocumentArticleParTiersParJour;
//	}


//	public String getCodeMoisAnnee() {
//		return codeMoisAnnee;
//	}
//
//
//	public void setCodeMoisAnnee(String codeMoisAnnee) {
//		this.codeMoisAnnee = codeMoisAnnee;
//	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeProforma() {
		return totauxCaPeriodeProforma;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeProformaNonTransforme() {
		return totauxCaPeriodeProformaNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeProformaTransforme() {
		return totauxCaPeriodeProformaTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeProformaNonTransformeARelancer() {
		return totauxCaPeriodeProformaNonTransformeARelancer;
	}


	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}


	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

}
