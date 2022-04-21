package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.LineChartModel;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.lib.osgi.ConstActionInterne;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.mapper.TaEditionMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.EditionParam;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.paiement.PaiementCbController;
import fr.legrain.bdg.webapp.paiement.PaiementParam;
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.InfosMois;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;


@Named
@ViewScoped
public class DashBoardAvisEcheanceController/*<MoisExoCourant>*/ extends DashBoardDocumentController implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8123101146205192757L;

	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaEditionServiceRemote taEditionService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	private @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

    private BarChartModel barChartModelAvisEcheanceJour; //AJOUTER LE 06/09/2017 Jean Marc
    private LineChartModel lineChartModelAvisEcheanceJour;
//	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    
    //TEST Chart.js
//    private DonutChartModel donutModel;
    
    private BarChartModel barChartModelAvisEcheance;
    private BarChartModel barChartModelAvisEcheanceComp;
    private LineChartModel lineChartModelAvisEcheance;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    private Date dateDebut;
//    private Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
//    private String codeEtatAvisEcheance;
//    private List<DocumentDTO> valueLignesAvisEcheance;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAvisEcheance;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAvisEcheanceNonTotalementPayes;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAvisEcheanceNonTotalementPayesARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheance;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayee;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceTotalementPayee;
    //spécifique abonnement
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours;
    //rajout comparaison
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceTotalementPayeeComp;


    
    private int nbAvisEcheanceSurPeriode = 0;
    private int nbAvisEcheanceNonTotalementPayesSurPeriode = 0 ;
    private int nbAvisEcheanceTotalementPayesSurPeriode = 0;
    private int nbAvisEcheanceNonTotalementPayesARelancerSurPeriode = 0;
    //spécifique abonnement
    private int nbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours = 0;
    private int nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours = 0;
    
    private List<String> listeGraphiqueComparatifCADate = new ArrayList<String>();
    private List<BigDecimal> listeGraphiqueComparatifCAMontant1 = new ArrayList<BigDecimal>();
    private List<BigDecimal> listeGraphiqueComparatifCAMontant2 = new ArrayList<BigDecimal>();
    
    //rajout comparaison
    private int nbAvisEcheanceSurPeriodeComp = 0;
    private int nbAvisEcheanceNonTotalementPayesSurPeriodeComp = 0 ;
    private int nbAvisEcheanceTotalementPayesSurPeriodeComp = 0;
    private int nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp = 0;
    
    
    private BigDecimal tauxTotalementPayesNb = BigDecimal.ZERO;
    private BigDecimal tauxTotalementPayesCa = BigDecimal.ZERO;
    private BigDecimal MontantTotalHt = BigDecimal.ZERO;    
    private BigDecimal MontantTotalHtTotalementPayes = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTotalementPayes = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTotalementPayesARelancer = BigDecimal.ZERO;
    
    //rajout comparaison
    private BigDecimal tauxTotalementPayesNbComp = BigDecimal.ZERO;
    private BigDecimal tauxTotalementPayesCaComp = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtComp = BigDecimal.ZERO;    
    private BigDecimal MontantTotalHtTotalementPayesComp = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTotalementPayesComp = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTotalementPayesARelancerComp = BigDecimal.ZERO;
    
//    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste
//    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument;// Récupère le ca ht par jour dans un liste
    
    private TaEditionDTO editionDefautImpression = null;
    
   // private List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiers = new ArrayList<DocumentChiffreAffaireDTO>();
    
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
		setTaDocumentService(taAvisEcheanceService);
//    	infoEntreprise = taInfoEntrepriseService.findInstance();
//    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	listeEdition = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaAvisEcheance.TYPE_DOC.toUpperCase());

		for (TaEditionDTO ed : listeEdition) {
			for (TaActionEditionDTO actionDefaut : ed.getTaActionEditionDTO()) {
				if(actionDefaut.getCodeAction().equals(TaActionEdition.code_impression)) {
					editionDefautImpression = ed;
				}
			}
		}
		if(editionDefautImpression == null) {
			TaEditionMapper editionMapper = new TaEditionMapper();
			
			TaEdition editionDefautProgramme = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_FACTURE_DEFAUT).get(0);
			editionDefautImpression = editionMapper.mapEntityToDto(editionDefautProgramme, editionDefautImpression);
		}
//    	barChartModelAvisEcheance = new BarChartModel();
//    	createDonutModel();
    	//Initialisation des données sur la période exercice
    	actRechercheAvisEcheance(codeEtatDocument);
    }
    
    
    public void imprimeEdition(int idDocument, TaEditionDTO edition, String modeEdition, String pourClient) {
		//ICI COMMENCE LA PARTIE actDialogImprimer
    	TaAvisEcheance facture = new TaAvisEcheance();
		try {
			facture = taAvisEcheanceService.findById(idDocument);
		} catch (FinderException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		 Map<String,Object> options = new HashMap<String, Object>();
	       
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        //list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	        params.put("modeEcranDefaut", list);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametreEdition = null;
			
	
				
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
//			String modeEdition = (String)actionEvent.getComponent().getAttributes().get("mode_edition");
//			String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("facture");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", facture.getGestionLot());
			  

			if(!editionClient) {
				switch (modeEdition) {
				case "BROUILLON":
					if(facture.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "BROUILLONAPAYER");
					} else {
						mapParametreEdition.put("mode", "BROUILLON");
					}
					break;
				case "DUPLICATA":
					if(facture.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "DUPLICATAPAYER");
					} else {
						mapParametreEdition.put("mode", "DUPLICATA");
					}
					break;
				case "PAYER":
					if(facture.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "PAYER");
					} else {
						mapParametreEdition.put("mode", "PAYER");
					}
					break;
	
				default:
					break;
				}
			} else {
				if(facture.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
					mapParametreEdition.put("mode", "CLIENTAPAYER");
				} else {
					mapParametreEdition.put("mode", "CLIENT");
				}
			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_FACTURE_DEFAUT);
			editionParam.setMapParametreEdition(mapParametreEdition);
			
			sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, editionParam);
	        
			
	        //PrimeFaces.current().dialog().openDynamic("/dialog_choix_edition", options, params);
			
			//ICI s'arrete la partie actDialogImprimer
			
			//ICI COMMENCE LA PARTIE handleReturnDialogImprimer
		  TaEdition taEdition = null;
		  
			try {
				taEdition = taEditionService.findById(edition.getId());
			} catch (FinderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(taEdition == null) {
				taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_FACTURE_DEFAUT).get(0);
			}
				if(taEdition!=null) {
					//System.out.println("ChoixEditionController.actImprimer() "+taEdition.getLibelleEdition());
					taEdition.setMapParametreEdition(mapParametreEdition);
					List<String> listeResourcesPath = null;
					if(taEdition.getResourcesPath()!=null) {
						listeResourcesPath = new ArrayList<>();
						listeResourcesPath.add(taEdition.getResourcesPath());
					}
					BdgProperties bdgProperties = new BdgProperties();
					String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEdition.getFichierNom());

					try { 
						Files.write(Paths.get(localPath), taEdition.getFichierBlob());
						
						AnalyseFileReport afr = new AnalyseFileReport();
						afr.initializeBuildDesignReportConfig(localPath);
						afr.ajouteLogo();
						afr.closeDesignReportConfig();
						

						String pdf = taAvisEcheanceService.generePDF(facture.getIdDocument(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
						PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");
//						masterEntity = taAvisEcheanceService.findById(masterEntity.getIdDocument());
//						updateTab();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			//ICI FINI LA PARTIE handleReturnDialogImprimer
			
	}
    
    
    
    
    //TEST CHART JS
//    public void createDonutModel() {
//        donutModel = new DonutChartModel();
//        ChartData data = new ChartData();
//         
//        DonutChartDataSet dataSet = new DonutChartDataSet();
//        List<Number> values = new ArrayList<>();
//        values.add(300);
//        values.add(50);
//        values.add(100);
//        dataSet.setData(values);
//         
//        List<String> bgColors = new ArrayList<>();
//        bgColors.add("rgb(255, 99, 132)");
//        bgColors.add("rgb(54, 162, 235)");
//        bgColors.add("rgb(255, 205, 86)");
//        dataSet.setBackgroundColor(bgColors);
//         
//        data.addChartDataSet(dataSet);
//        List<String> labels = new ArrayList<>();
//        labels.add("Red");
//        labels.add("Blue");
//        labels.add("Yellow");
//        data.setLabels(labels);
//         
//        donutModel.setData(data);
//    }


    public void refreshDashBoard(String etatAvisEcheance){
    	initPeriode();
    	setAfficheDocumentJour(false); //AJOUTER LE 06/09/2017 Jean Marc
    	setAfficheDocumentJourDataTable(false); //AJOUTER LE 06/09/2017 Jean Marc
    	listeDocumentDetailMois.clear(); //AJOUTER LE 06/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentDetailJour.clear(); //AJOUTER LE 19/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParMois.clear(); //AJOUTER LE 14/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParJour.clear(); //AJOUTER LE 20/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentFactureParTiers.clear();/**RAJOUT YANN**/
    	listeDocumentDetailMoisComp.clear(); 
    	listeDocumentDetailJourComp.clear(); 
    	listeDocumentArticleParTiersParMoisComp.clear(); 
    	listeDocumentArticleParTiersParJourComp.clear(); 
    	listeDocumentFactureParTiersComp.clear();
    	actRechercheAvisEcheance(etatAvisEcheance);
    	if(comparaison) {
    		initPeriodeComp();
    		actRechercheAvisEcheanceComp(etatAvisEcheance);
    	}
    }
    
//    public void refreshDashBoardComp(String etatAvisEcheance){
//    	initPeriodeComp();
//    	if(comparaison) {
//    		actRechercheAvisEcheanceComp(etatAvisEcheance);
//    	}else {
//    		actRechercheAvisEcheance(etatAvisEcheance);
//    	}
//    	
//    }


	public void actRechercheAvisEcheance(String codeEtatAvisEcheance){
//		barChartModelAvisEcheance.clear();
    	setInfoEtatDocument(codeEtatAvisEcheance);
    	setCodeEtatDocument(codeEtatAvisEcheance);

   	
    	
    	//pour essai 
    	requetesDisponiblesPourMoteur();
    	
    	
    	
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeAvisEcheance = taAvisEcheanceService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	
//    	totauxCaPeriodeAvisEcheance = taAvisEcheanceService.chiffreAffaireTotalDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%",true);
//    	totauxCaPeriodeAvisEcheance = taAvisEcheanceService.chiffreAffaireTotalDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,"5.5",true);
//    	totauxCaPeriodeAvisEcheance = taAvisEcheanceService.chiffreAffaireTotalDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TYPE_PAIEMENT,"CB",true);
    	
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeAvisEcheanceNonTotalementPayee = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		//spécifique abo
		totauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTOAboEnCours(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours =  taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTOAboEnCours(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		nbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours = (int) taAvisEcheanceService.countDocumentNonTransformeAboEnCours(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours =  (int) taAvisEcheanceService.countDocumentNonTransformeARelancerAboEnCours(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
		
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeAvisEcheanceTotalementPayee = taAvisEcheanceService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre total de documents sur la période
		nbAvisEcheanceSurPeriode = (int) taAvisEcheanceService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de AvisEcheance non totalement payés sur la période
		nbAvisEcheanceNonTotalementPayesSurPeriode = (int) taAvisEcheanceService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de AvisEcheance à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbAvisEcheanceNonTotalementPayesARelancerSurPeriode = (int) taAvisEcheanceService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
	// Retourne le nombre de AvisEcheancetransformés sur la période
		nbAvisEcheanceTotalementPayesSurPeriode = (int) taAvisEcheanceService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);    	

		tauxTotalementPayesNb = BigDecimal.ZERO;
	    tauxTotalementPayesCa = BigDecimal.ZERO;
	    
		if (nbAvisEcheanceSurPeriode > 0) {
			try {
				tauxTotalementPayesNb =  (new BigDecimal(nbAvisEcheanceTotalementPayesSurPeriode) .divide(new BigDecimal(nbAvisEcheanceSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTotalementPayesCa = totauxCaPeriodeAvisEcheanceTotalementPayee.getMtTtcCalc() .divide(totauxCaPeriodeAvisEcheance.getMtTtcCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		switch (codeEtatAvisEcheance) {
		case ETAT_DOCUMENT_TOUS:
			// Renvoi la liste des documents présents sur la période en cours

			valueLignesDocument = taAvisEcheanceService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des avis d'échéance réalisées";
			infoLabel2 = "Avis d'échéance mensuels réalisées";

			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			/**RAJOUT YANN**/
			listeDocumentFactureParTiers = taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, null, 0);
			infoLabel = "Détail des avis d'écheance sur la période ";
			initInfosLabelRapportMensuelDataTable("Avis d'écheance");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			//valueLignesDocument = taAvisEcheanceService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			//spécifique abonnement
			valueLignesDocument = taAvisEcheanceService.findDocumentNonTransfosDTOAboEnCours(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			
			
			infoLabel = "Liste des avis d'échéances partiellement payées";
//			valueLignesDocument = taAvisEcheanceService.findDocumentNonTransfosDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			//listeDocumentFactureParTiers= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ETAT_NON_TRANSFORME, 0);
			//ISA le 08/08/2019 suite à prise en charge des différents états
			listeDocumentFactureParTiers= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ENCOURS, 0);
			infoLabel = "Détail des avis d'écheance sur la période ";
//			listeCaMoisDocument = taAvisEcheanceService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_FAMILLE_TIERS,"%");
//			listeCaMoisDocument = taAvisEcheanceService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_FAMILLE_ARTICLE,"%");
//			listeCaMoisDocument = taAvisEcheanceService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_ARTICLE,"%");
//			listeCaMoisDocument = taAvisEcheanceService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_TIERS,"%");
//			listeCaMoisDocument = taAvisEcheanceService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_TAUX_TVA,"%");
//			listeCaMoisDocument = taAvisEcheanceService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_TYPE_PAIEMENT,"%");
			
//			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuelAvisEcheance("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesAvisEcheance = taAvisEcheanceService.findAvisEcheanceNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));			
			initInfosLabelRapportMensuelDataTable("Avis d'écheance");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
							  CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);

			break;
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taAvisEcheanceService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des avis d'écheance Déjà réglé";
//			valueLignesDocument = taAvisEcheanceService.findDocumentTransfosDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			listeDocumentFactureParTiers= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ETAT_TRANSFORME, 0);
			//ISA  le 08/08/2019 suite à prise en charge des différents états
			listeDocumentFactureParTiers= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.TERMINE_TOTALEMENT_TRANSFORME, 0);
			infoLabel = "Détail des avis d'écheance sur la période ";
//			valueLignesAvisEcheance = taAvisEcheanceService.findAvisEcheanceTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));			
			initInfosLabelRapportMensuelDataTable("Avis d'écheance");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL,
					  		  CSS_DBOARD_BLOC_ACTIF_FACTURE);
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			//valueLignesDocument = taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			//spécifique abonnement
			valueLignesDocument = taAvisEcheanceService.findDocumentNonTransfosARelancerDTOAboEnCours(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			
			infoLabel = "Liste des avis d'écheance à relancer";
//			valueLignesDocument = taAvisEcheanceService.findDocumentNonTransfosARelancerDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,15,null);
			listeDocumentFactureParTiers= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ETAT_NON_TRANSFORME_A_RELANCER, 15);
			infoLabel = "Détail des avis d'écheance sur la période ";
//			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuelAvisEcheance("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			valueLignesAvisEcheance = taAvisEcheanceService.findAvisEcheanceNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelAvisEcheance.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));			
			initInfosLabelRapportMensuelDataTable("Avis d'écheance");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  		  CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  		  CSS_DBOARD_BLOC_ACTIF_FACTURE, 
			  		  		  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
			
		default:
			break;
		}
    	
    }
	
	///////ACT RECHERCHER COMPARAISON////////////
	public void actRechercheAvisEcheanceComp(String codeEtatAvisEcheance){
		
		if(comparaison) {
//			barChartModelAvisEcheance.clear();
	    	setInfoEtatDocument(codeEtatAvisEcheance);
	    	setCodeEtatDocument(codeEtatAvisEcheance);

	   	
	    	
	    	//pour essai 
	    	requetesDisponiblesPourMoteur();
	    	
	    	
	    	
	    	listMoisAnneeExoComp = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
	    	
	    	
	    	// Calcule les CA (HT, TVA, TTC) total sur la période
	    	totauxCaPeriodeAvisEcheanceComp = taAvisEcheanceService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
	    	
			// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
			totauxCaPeriodeAvisEcheanceNonTotalementPayeeComp = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
			
			// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
			totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 15,null);

			// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
			totauxCaPeriodeAvisEcheanceTotalementPayeeComp = taAvisEcheanceService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
			// Retourne le nombre total de documents sur la période
			nbAvisEcheanceSurPeriodeComp = (int) taAvisEcheanceService.countDocument(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
		// Retourne le nombre de AvisEcheance non totalement payés sur la période
			nbAvisEcheanceNonTotalementPayesSurPeriodeComp = (int) taAvisEcheanceService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
		// Retourne le nombre de AvisEcheance à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
			nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp = (int) taAvisEcheanceService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 15,null);
		// Retourne le nombre de AvisEcheancetransformés sur la période
			nbAvisEcheanceTotalementPayesSurPeriodeComp = (int) taAvisEcheanceService.countDocumentTransforme(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);    	

			tauxTotalementPayesNbComp = BigDecimal.ZERO;
		    tauxTotalementPayesCaComp = BigDecimal.ZERO;
		    
			if (nbAvisEcheanceSurPeriodeComp > 0) {
				try {
					tauxTotalementPayesNbComp =  (new BigDecimal(nbAvisEcheanceTotalementPayesSurPeriodeComp) .divide(new BigDecimal(nbAvisEcheanceSurPeriodeComp),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
					tauxTotalementPayesCaComp = totauxCaPeriodeAvisEcheanceTotalementPayeeComp.getMtTtcCalc() .divide(totauxCaPeriodeAvisEcheanceComp.getMtTtcCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
			
			switch (codeEtatAvisEcheance) {
			case ETAT_DOCUMENT_TOUS:
				// Renvoi la liste des documents présents sur la période en cours

				valueLignesDocumentComp = taAvisEcheanceService.findAllDTOPeriode(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
				// Création et initialisation du graphique du CA HT / mois pour la période en cours
				listeCaMoisDocumentComp = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,null);
				lineChartModelAvisEcheance = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//				barChartModelAvisEcheance = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
				
				lineChartModelAvisEcheance = addLineSeriesMensuelComparaison(lineChartModelAvisEcheance,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				/**RAJOUT YANN**/
				listeDocumentFactureParTiersComp = taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, null, 0);
				initInfosLabelRapportMensuelDataTable("Avis d'écheance");
				// Initialisent les classes css pour les block d'information
				initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP);
				break;

			case ETAT_DOCUMENT_NON_TRANSORME:
				valueLignesDocumentComp = taAvisEcheanceService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
				lineChartModelAvisEcheance = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
				listeCaMoisDocumentComp = taAvisEcheanceService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,null);
				
//				listeDocumentFactureParTiersComp= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(dateDebutComp, dateFinComp, null, null, TaEtat.ETAT_NON_TRANSFORME, 0);
				//ISA  le 08/08/2019 suite à prise en charge des différents états
				listeDocumentFactureParTiersComp= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, TaEtat.ENCOURS, 0);
				
				lineChartModelAvisEcheance = addLineSeriesMensuelComparaison(lineChartModelAvisEcheance,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
//				barChartModelAvisEcheanceComp.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentComp, dateDebutComp, dateFinComp));
				
				// Création et initialisation du graphique du CA HT / mois pour la période en cours
		
				initInfosLabelRapportMensuelDataTable("Avis d'écheance");
				initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL_COMP,
								  CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP);

				break;
				
			case ETAT_DOCUMENT_TRANSORME:
				valueLignesDocumentComp = taAvisEcheanceService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
//				barChartModelAvisEcheanceComp = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				lineChartModelAvisEcheance = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
				listeCaMoisDocumentComp = taAvisEcheanceService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,null);
//				barChartModelAvisEcheanceComp.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentComp, dateDebutComp, dateFinComp));
				
//				listeDocumentFactureParTiersComp= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(dateDebutComp, dateFinComp, null, null, TaEtat.ETAT_TRANSFORME, 0);
				//ISA  le 08/08/2019 suite à prise en charge des différents états
				listeDocumentFactureParTiersComp= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, TaEtat.TERMINE_TOTALEMENT_TRANSFORME, 0);

				lineChartModelAvisEcheance = addLineSeriesMensuelComparaison(lineChartModelAvisEcheance,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				initInfosLabelRapportMensuelDataTable("Avis d'écheance");
				initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
						  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
						  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP,
						  		CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP);
				break;
				
			case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
				valueLignesDocumentComp = taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 15,null);
				lineChartModelAvisEcheance = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,15,null);
				listeCaMoisDocumentComp = taAvisEcheanceService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,15,null);
				listeDocumentFactureParTiersComp= taAvisEcheanceService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, TaEtat.ETAT_NON_TRANSFORME_A_RELANCER, 15);
				
				lineChartModelAvisEcheance = addLineSeriesMensuelComparaison(lineChartModelAvisEcheance,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				initInfosLabelRapportMensuelDataTable("AvisEcheance");
				initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
				  		  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP,
				  		  		CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP, 
				  		  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP);
				break;
				
			default:
				break;
			}
			
		}

    	
    }
	///////FIN ACT RECHERCHER COMPARAISON////////
	public void itemSelectMoisComp(ItemSelectEvent event) {
		if(comparaison) {
			
			listeDocumentDetailMois.clear();
			listeDocumentArticleParTiersParMois.clear();
			listeCaJoursDocument.clear();
			
			listeDocumentDetailMoisComp.clear();
			listeDocumentArticleParTiersParMoisComp.clear();
			listeCaJoursDocumentComp.clear();
			
			montantTotalHtJour = BigDecimal.ZERO;
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJour.clear();
			listeDocumentDetailJourComp.clear();
			listeDocumentArticleParTiersParJour.clear();
			listeDocumentArticleParTiersParJourComp.clear();
			
			
			InfosMois mois =  grandeListeGraphiqueMensuel.get(event.getItemIndex());
			InfosMois moisComp = petiteListeGraphiqueMensuel.get(event.getItemIndex());
			setAfficheDocumentJour(true);
			setAfficheDocumentJourDataTable(false);
			
//			if(event.getSeriesIndex()==0) {
			
			Date premierJourMois = mois.getPremierJourMois();
			Date dernierJourMois = mois.getDernierJourMois();
			listJoursAnneeExo = LibDate.listeJoursEntreDeuxDate(premierJourMois, dernierJourMois);
			//nomMoisDetail = listMoisAnneeExo.get(event.getItemIndex()).getNomLong()+" "+listMoisAnneeExo.get(event.getItemIndex()).getAnnee();
			nomMoisDetail = mois.getNomLong()+" "+mois.getAnnee();
			nomMois = listMoisAnneeExo.get(event.getItemIndex()).getNomLong();
			
			setCodeMoisAnnee(listMoisAnneeExo.get(event.getItemIndex()).getCodeMoisAnnee());
				
			Date premierJourMoisComp = moisComp.getPremierJourMois();
			Date dernierJourMoisComp = moisComp.getDernierJourMois();
			
			if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
				listJoursAnneeExoComp = LibDate.listeJoursEntreDeuxDate(premierJourMoisComp, dernierJourMoisComp);
				nomMoisDetailComp = moisComp.getNomLong()+" "+moisComp.getAnnee();
			}else {
				nomMoisDetailComp =" pas de données";
			}
			
			
			
			switch (infoEtatDocument) {
			
				case ETAT_DOCUMENT_TOUS:
					// Cumul du chiffre d'affaire par mois
					montantTotalHtMois = taAvisEcheanceService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
					montantTotalHtMoisComp = taAvisEcheanceService.chiffreAffaireTotalDTO(premierJourMoisComp, dernierJourMoisComp,null).getMtHtCalc();
					}else {
						montantTotalHtMoisComp = BigDecimal.ZERO;
					}
					initInfosLabelRapportMensuelDataTable("AvisEcheances");
					
					// on récupére dans listeDocumentDetailMois la liste des document sur la période
					
					listeDocumentDetailMois= taAvisEcheanceService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
					listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
					listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
					listeDocumentDetailMoisComp= taAvisEcheanceService.findAllDTOPeriode(premierJourMoisComp, dernierJourMoisComp,null);
					listeDocumentArticleParTiersParMoisComp = taAvisEcheanceService.findArticlesParTiersParMois(premierJourMoisComp, dernierJourMoisComp);
					listeCaJoursDocumentComp = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,null);
					}
					
					
					
//
					lineChartModelAvisEcheanceJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelAvisEcheanceJour = addLineSeriesJoursComparaison(lineChartModelAvisEcheanceJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
//					barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					break;
					
					
				case ETAT_DOCUMENT_NON_TRANSORME:
					// Cumul du chiffre d'affaire par mois
					montantTotalHtMois = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						montantTotalHtMoisComp = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(premierJourMoisComp, dernierJourMoisComp,null).getMtHtCalc();
						}else {
							montantTotalHtMoisComp = BigDecimal.ZERO;
						}
					initInfosLabelRapportMensuelDataTable("AvisEcheance");
					// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
					listeDocumentDetailMois= taAvisEcheanceService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
					listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
					listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						listeDocumentDetailMoisComp= taAvisEcheanceService.findDocumentNonTransfosDTO(premierJourMoisComp, dernierJourMoisComp,null);
						listeDocumentArticleParTiersParMoisComp = taAvisEcheanceService.findArticlesParTiersNonTransforme(premierJourMoisComp, dernierJourMoisComp);
						listeCaJoursDocumentComp = taAvisEcheanceService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,null);
						}
					
					lineChartModelAvisEcheanceJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelAvisEcheanceJour = addLineSeriesJoursComparaison(lineChartModelAvisEcheanceJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
					
//					barChartModelAvisEcheanceJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
				
					break;
					
				case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
					// Cumul du chiffre d'affaire par mois
					montantTotalHtMois = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						montantTotalHtMoisComp = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMoisComp, dernierJourMoisComp,15,null).getMtHtCalc();
						}else {
							montantTotalHtMoisComp = BigDecimal.ZERO;
						}
					initInfosLabelRapportMensuelDataTable("AvisEcheance");
					// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
					listeDocumentDetailMois= taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
					listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
					listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						listeDocumentDetailMoisComp= taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(premierJourMoisComp, dernierJourMoisComp,15,null);
						listeDocumentArticleParTiersParMoisComp = taAvisEcheanceService.findArticlesParTiersNonTransformeARelancer(premierJourMoisComp, dernierJourMoisComp,15);
						listeCaJoursDocumentComp = taAvisEcheanceService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,15,null);
						}
					
					lineChartModelAvisEcheanceJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelAvisEcheanceJour = addLineSeriesJoursComparaison(lineChartModelAvisEcheanceJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
//					barChartModelAvisEcheanceJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					break;
					
				case ETAT_DOCUMENT_TRANSORME:
					// Cumul du chiffre d'affaire par mois
					montantTotalHtMois = taAvisEcheanceService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						montantTotalHtMoisComp = taAvisEcheanceService.chiffreAffaireTransformeTotalDTO(premierJourMoisComp, dernierJourMoisComp,null).getMtHtCalc();
						}else {
							montantTotalHtMoisComp = BigDecimal.ZERO;
						}
					initInfosLabelRapportMensuelDataTable("AvisEcheance");
					// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
					listeDocumentDetailMois= taAvisEcheanceService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
					listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
					listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						listeDocumentDetailMoisComp= taAvisEcheanceService.findDocumentTransfosDTO(premierJourMoisComp, dernierJourMoisComp,null);
						listeDocumentArticleParTiersParMoisComp = taAvisEcheanceService.findArticlesParTiersTransforme(premierJourMoisComp, dernierJourMoisComp);
						listeCaJoursDocumentComp = taAvisEcheanceService.listeChiffreAffaireTransformeJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,null);
						}
					
					lineChartModelAvisEcheanceJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelAvisEcheanceJour = addLineSeriesJoursComparaison(lineChartModelAvisEcheanceJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
					
//					barChartModelAvisEcheanceJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					
					break;
					
					
					
			}
			
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
		initInfosLabelRapportMensuelDataTable("AvisEcheances");
		System.out.println(codeMoisAnnee);
	
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taAvisEcheanceService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
	initInfosLabelRapportMensuelDataTable("AvisEcheance");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taAvisEcheanceService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);

	listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);

	
	barChartModelAvisEcheanceJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAvisEcheanceService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelAvisEcheanceJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelAvisEcheanceJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taAvisEcheanceService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taAvisEcheanceService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taAvisEcheanceService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taAvisEcheanceService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelAvisEcheanceJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelAvisEcheanceJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
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
	montantTotalHtJour = taAvisEcheanceService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taAvisEcheanceService.findAllDTOPeriode(jourMois, jourMois,null);

	
	setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersParMois(jourMois, jourMois));
//	setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersParMoisParTypeRegroupement(jourMois, jourMois,Const.PAR_FAMILLE_ARTICLE,"%"));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("AvisEcheance");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvisEcheanceService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersNonTransformeParTypeRegroupement(jourMois, jourMois,Const.PAR_FAMILLE_ARTICLE,"%"));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersNonTransformeARelancerParTypeRegroupement(jourMois, jourMois, 15,Const.PAR_FAMILLE_ARTICLE,"%"));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaProforma.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvisEcheanceService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		break;
	}
		
    }
	
	
	
	public void itemSelectJourComp(ItemSelectEvent event2) throws ParseException {

		setNumeroJourDetail(event2.getItemIndex());
		setNumeroJourDetailComp(event2.getItemIndex());
//		Date jourMois = listJoursAnneeExo.get(event2.getItemIndex()).getDateDuJour();
		Date jourMois = grandeListeGraphiqueJour.get(event2.getItemIndex()).getDateDuJour();
		Date jourMoisComp = petiteListeGraphiqueJour.get(event2.getItemIndex()).getDateDuJour();
		setAfficheDocumentJourDataTable(true);
		
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par jour
	montantTotalHtJour = taAvisEcheanceService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	if(jourMoisComp!= null) {
		montantTotalHtJourComp = taAvisEcheanceService.chiffreAffaireTotalDTO(jourMoisComp, jourMoisComp,null).getMtHtCalc();
		listeDocumentDetailJourComp = taAvisEcheanceService.findAllDTOPeriode(jourMoisComp, jourMoisComp,null);
		setListeDocumentArticleParTiersParJourComp(taAvisEcheanceService.findArticlesParTiersParMois(jourMoisComp, jourMoisComp));
	}else {
		montantTotalHtJourComp = BigDecimal.ZERO;
		listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
		listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
	}
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taAvisEcheanceService.findAllDTOPeriode(jourMois, jourMois,null);
	
	
	setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersParMois(jourMois, jourMois));
	initInfosLabelRapportMensuelDataTable("AvisEcheance");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		if(jourMoisComp!= null) {
			montantTotalHtJourComp = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(jourMoisComp, jourMoisComp,null).getMtHtCalc();
			listeDocumentDetailJourComp = taAvisEcheanceService.findDocumentNonTransfosDTO(jourMoisComp, jourMoisComp,null);
			setListeDocumentArticleParTiersParJourComp(taAvisEcheanceService.findArticlesParTiersNonTransforme(jourMoisComp, jourMoisComp));
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvisEcheanceService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersNonTransforme(jourMois, jourMois));
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		if(jourMoisComp!= null) {
			montantTotalHtJourComp = taAvisEcheanceService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMoisComp, jourMoisComp,15,null).getMtHtCalc();
			listeDocumentDetailJourComp = taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(jourMoisComp, jourMoisComp,15,null);
			setListeDocumentArticleParTiersParJourComp(taAvisEcheanceService.findArticlesParTiersNonTransformeARelancer(jourMoisComp, jourMoisComp, 15));
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvisEcheanceService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		if(jourMoisComp!= null) {
			montantTotalHtJourComp = taAvisEcheanceService.chiffreAffaireNonTransformeTotalDTO(jourMoisComp, jourMoisComp,null).getMtHtCalc();
			listeDocumentDetailJourComp = taAvisEcheanceService.findDocumentTransfosDTO(jourMoisComp, jourMoisComp,null);
			setListeDocumentArticleParTiersParJourComp(taAvisEcheanceService.findArticlesParTiersTransforme(jourMoisComp, jourMoisComp));
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taAvisEcheanceService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taAvisEcheanceService.findArticlesParTiersTransforme(jourMois, jourMois));
		initInfosLabelRapportMensuelDataTable("AvisEcheance");
		break;
	}
		
    }
	
	public void actInitPaiementCarteBancaire(ActionEvent actionEvent) {
		try {
			Integer id = (Integer) actionEvent.getComponent().getAttributes().get("idDocument");
			
			System.out.println("DashBoardAvisEcheanceController.actInitPaiementCarteBancaire() "+id);
				//setAvisEcheanceReglee(true);
		//actInitReglement();
			//calculDateEcheance(true);
			
			if(id!=null) {
				PaiementParam param = new PaiementParam();
				TaAvisEcheance taAvisEcheance =  taAvisEcheanceService.findById(id);
				//param.setDocumentPayableCB(taAvisEcheanceService.findById(id));
				param.setMontantPaiement(taAvisEcheance.getResteAReglerComplet());
				param.setMontantLibre(false);
				param.setTiersPourReglementLibre(null);
				param.setOriginePaiement("BDG Dashboard AvisEcheance");
				PaiementCbController.actDialoguePaiementEcheanceParCarte(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void actDialogGenerationReglement(ActionEvent actionEvent) {
		try {	
//			if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION)){
//				//si pas en consultation, enregistrer la facture avant de rentrer dans la gestion des règlements
//				actEnregistrer(null);
//			}
//			if(docEnregistre){
			Integer id = (Integer) actionEvent.getComponent().getAttributes().get("idDocument");
			
				Map<String,Object> options = new HashMap<String, Object>();
				options.put("modal", true);
				options.put("draggable", false);
				options.put("closable", false);
				options.put("resizable", true);
				options.put("contentHeight", 710);
				options.put("contentWidth", "100%");
				options.put("width", 1024);

				Map<String,Object> mapDialogue = new HashMap<String,Object>();
				TaAvisEcheance taAvisEcheance =  taAvisEcheanceService.findById(id);
				mapDialogue.put("masterEntity",taAvisEcheance );

				Map<String,List<String>> params = new HashMap<String,List<String>>();
				List<String> list = new ArrayList<String>();
				list.add(new ModeObjetEcranServeur().modeString(EnumModeObjet.C_MO_INSERTION));
				params.put("modeEcranDefaut", list);


				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("mapDialogue", mapDialogue);

				PrimeFaces.current().dialog().openDynamic("reglement/multiple/reglement_multiple_facture.xhtml", options, params);
//			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		actRechercheAvisEcheance(codeEtatDocument);
//		System.out.println("PaiementCbController.handleReturnDialoguePaiementEcheanceParCarte()");
//		try {
//			if(event!=null && event.getObject()!=null) {
//				RetourPaiementCarteBancaire e = (RetourPaiementCarteBancaire) event.getObject();
//	
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void handleReturnDialogGestionReglement(SelectEvent event) {
		actRechercheAvisEcheance(codeEtatDocument);
	}
	
	public void actImprimerListeAvisEcheance(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", infoLabel);
		
//		sessionMap.put("debut", getDateDebut());
//		sessionMap.put("fin", getDateFin());
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeAvisEcheance", valueLignesDocument);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimerListeAvisEcheanceComparaison(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", infoLabel);
		mapParametre.put("debutComp", LibDate.localDateToDate(getDateDebutComp()));
		mapParametre.put("finComp", LibDate.localDateToDate(getDateFinComp()));
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeAvisEcheance", valueLignesDocument);
		sessionMap.put("listeAvisEcheanceComp", valueLignesDocumentComp);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public void actImprimerListeAvisEcheanceDetailComparaison(ActionEvent actionEvent) {
		
		
//		try {
//					
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//	
//		Map<String,Object> mapParametre = new HashMap<String,Object>();
//		
//		sessionMap.remove("parametre");
//		sessionMap.remove("listeAvisEcheance");
//		sessionMap.remove("listeAvisEcheanceComp");
//		sessionMap.remove("infosLabel");
//		sessionMap.remove("infosLabel2");
//		
//		mapParametre.put("debut", getDateDebut());
//		mapParametre.put("fin", getDateFin());
//		mapParametre.put("infosLabel", infoLabel);
//		mapParametre.put("infosLabel2", infoLabel2);
//		mapParametre.put("debutComp", getDateDebutComp());
//		mapParametre.put("finComp", getDateFinComp());
//		
//		sessionMap.put("parametre", mapParametre);
//		sessionMap.put("listeAvisEcheance", valueLignesDocument);
//		sessionMap.put("listeAvisEcheanceComp", valueLignesDocumentComp);
//		BirtCompareCaMensuelDasboardAvisEcheance listeCompare = null;
//		//HashMap<String, BirtCompareCaMensuelDasboardAvisEcheance > valueLignesDocumentComparative = new HashMap<>();
//		
//		for (InfosMois r : petiteListeGraphiqueMensuel) {
//			listeCompare = new BirtCompareCaMensuelDasboardAvisEcheance();
//			if(r.getMois()!=null) {
//				listeCompare.setDateDoc(r.getMois());
//				listeCompare.setMontantHtPeriode1(r.getMontant());
//				valueLignesDocumentComparative.put(listeCompare.getDateDoc(), listeCompare);
//				
//			}
//			
//		}
//			
//		for (InfosMois r : grandeListeGraphiqueMensuel) {
//			if(valueLignesDocumentComparative.get(r.getMois())==null) {
//				listeCompare = new BirtCompareCaMensuelDasboardAvisEcheance();
//				if(r.getMois()!=null) {
//					listeCompare.setDateDoc(r.getMois());
//					listeCompare.setMontantHtPeriode2(r.getMontant());
//					valueLignesDocumentComparative.put(listeCompare.getDateDoc(), listeCompare);
//					
//				}
//			} else {
//				if(r.getMois()!=null) {
//					valueLignesDocumentComparative.get(r.getMois()).setDateDoc(r.getMois());
//					valueLignesDocumentComparative.get(r.getMois()).setMontantHtPeriode2(r.getMontant());
//					
//					
//				}
//			}
//			 							
//			}
//		
//		listeGraphiqueComparatifCADate.clear();
//		listeGraphiqueComparatifCAMontant1.clear();
//		listeGraphiqueComparatifCAMontant2.clear();
//		
//		for (String g : valueLignesDocumentComparative.keySet()) {
//			 listeGraphiqueComparatifCADate.add(valueLignesDocumentComparative.get(g).getDateDoc());
//			 listeGraphiqueComparatifCAMontant1.add(valueLignesDocumentComparative.get(g).getMontantHtPeriode1());
//			 listeGraphiqueComparatifCAMontant2.add(valueLignesDocumentComparative.get(g).getMontantHtPeriode2());
//		}
//		
////		petiteListeGraphiqueMensuel.addAll(grandeListeGraphiqueMensuel);
//		sessionMap.put("listeAvisEcheanceGraphiqueDate", listeGraphiqueComparatifCADate);
//		sessionMap.put("listeAvisEcheanceGraphiqueMontant1", listeGraphiqueComparatifCAMontant1);
//		sessionMap.put("listeAvisEcheanceGraphiqueMontant2", listeGraphiqueComparatifCAMontant2);
//	
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void actImprimerListeAvisEcheanceDetail(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", infoLabel);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeAvisEcheanceDetail", listeDocumentFactureParTiers);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimerListeAvisEcheanceComp(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebutComp()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFinComp()));
		mapParametre.put("infosLabel", infoLabel);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeAvisEcheance", valueLignesDocumentComp);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actImprimerListeAvisEcheanceDetailComp(ActionEvent actionEvent) {
	
	
//	try {
//				
//	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//	Map<String, Object> sessionMap = externalContext.getSessionMap();
//
//	Map<String,Object> mapParametre = new HashMap<String,Object>();
//	mapParametre.put("debut", getDateDebutComp());
//	mapParametre.put("fin", getDateFinComp());
//	mapParametre.put("infosLabel", "Détail des factures sur la période");
//	
//	sessionMap.put("parametre", mapParametre);
//	sessionMap.put("listeAvisEcheanceDetail", listeDocumentFactureParTiersComp);
//
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
}

	public void actImprimerListeDocMois(ActionEvent actionEvent) {
	
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	
	Map<String, Object> sessionMap = externalContext.getSessionMap();
	
	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("infosLabel", infosLabelRapportMensuelDataTable);
	
//	sessionMap.put("debut", getDateDebut());
//	sessionMap.put("fin", getDateFin());
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
//		Map<String,Object> mapParametre = new HashMap<String,Object>();
	
//		mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
		
//		sessionMap.put("parametre", mapParametre);
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
		
//		sessionMap.put("debut", getDateDebut());
//		sessionMap.put("fin", getDateFin());
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
//			Map<String,Object> mapParametre = new HashMap<String,Object>();
		
//			mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
			
//			sessionMap.put("parametre", mapParametre);
			sessionMapInfos.put("infosLabel", infosLabelRapportArticlesJourDataTable);
			sessionMap.put("listeArticleJour", listeDocumentArticleParTiersParJour);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void actImprimerListeDocMoisComp(ActionEvent actionEvent) {
			
			
			try {
						
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapParametre = new HashMap<String,Object>();
			mapParametre.put("infosLabel", infosLabelRapportMensuelDataTableComp);
			
//			sessionMap.put("debut", getDateDebut());
//			sessionMap.put("fin", getDateFin());
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("listeDocMois", listeDocumentDetailMoisComp);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void actImprimerListeArticleMoisComp(ActionEvent actionEvent) {
			
			
			try {
						
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> sessionMapInfos = externalContext.getSessionMap();
//			Map<String,Object> mapParametre = new HashMap<String,Object>();
		
//			mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
			
//			sessionMap.put("parametre", mapParametre);
			sessionMapInfos.put("infosLabel", getInfosLabelRapportArticlesDataTableComp());
			sessionMap.put("listeArticleMois", listeDocumentArticleParTiersParMoisComp);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void actImprimerListeDocJourComp(ActionEvent actionEvent) {
			
			
			try {
						
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapParametre = new HashMap<String,Object>();
			mapParametre.put("infosLabel", infosLabelRapportJournalierDataTableComp);
			
//			sessionMap.put("debut", getDateDebut());
//			sessionMap.put("fin", getDateFin());
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("listeDocJour", listeDocumentDetailJourComp);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
			public void actImprimerListeArticleJourComp(ActionEvent actionEvent) {
				
				
				try {
							
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				Map<String, Object> sessionMapInfos = externalContext.getSessionMap();
//				Map<String,Object> mapParametre = new HashMap<String,Object>();
			
//				mapParametre.put("infosLabel", getInfosLabelRapportArticlesDataTable());
				
//				sessionMap.put("parametre", mapParametre);
				sessionMapInfos.put("infosLabel", infosLabelRapportArticlesJourDataTableComp);
				sessionMap.put("listeArticleJour", listeDocumentArticleParTiersParJourComp);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
	public BarChartModel getBarChartModelAvisEcheance() {
		return barChartModelAvisEcheance;
	}
	public void setBarChartModelAvisEcheanceExo(BarChartModel barChartModelAvisEcheance) {
		this.barChartModelAvisEcheance = barChartModelAvisEcheance;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

//	public String getCodeEtatAvisEcheance() {
//		return codeEtatAvisEcheance;
//	}
//
//	public void setCodeEtatAvisEcheance(String codeEtatAvisEcheance) {
//		this.codeEtatAvisEcheance = codeEtatAvisEcheance;
//	}

//	public List<DocumentDTO> getValueLignesAvisEcheance() {
//		return valueLignesAvisEcheance;
//	}
//
//	public void setValueLignesAvisEcheance(List<DocumentDTO> valueLignesAvisEcheance) {
//		this.valueLignesAvisEcheance = valueLignesAvisEcheance;
//	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAvisEcheance() {
		return listeCaPeriodeAvisEcheance;
	}

	public void setListeCaPeriodeAvisEcheance(List<DocumentChiffreAffaireDTO> listeCaPeriodeAvisEcheance) {
		this.listeCaPeriodeAvisEcheance = listeCaPeriodeAvisEcheance;
	}

	public int getNbAvisEcheanceSurPeriode() {
		return nbAvisEcheanceSurPeriode;
	}

	public void setNbAvisEcheanceSurPeriode(int nbAvisEcheanceSurPeriode) {
		this.nbAvisEcheanceSurPeriode = nbAvisEcheanceSurPeriode;
	}

	public int getNbAvisEcheanceNonTotalementPayesSurPeriode() {
		return nbAvisEcheanceNonTotalementPayesSurPeriode;
	}

	public void setNbAvisEcheanceNonTotalementPayesSurPeriode(int nbAvisEcheanceNonPayesSurPeriode) {
		this.nbAvisEcheanceNonTotalementPayesSurPeriode = nbAvisEcheanceNonPayesSurPeriode;
	}


	public int getNbAvisEcheanceNonTotalementPayesARelancerSurPeriode() {
		return nbAvisEcheanceNonTotalementPayesARelancerSurPeriode;
	}


	public void setNbAvisEcheanceNonTotalementPayesARelancerSurPeriode(int nbAvisEcheanceNonPayesARelancerSurPeriode) {
		this.nbAvisEcheanceNonTotalementPayesARelancerSurPeriode = nbAvisEcheanceNonPayesARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAvisEcheanceNonTotalementPayes() {
		return listeCaPeriodeAvisEcheanceNonTotalementPayes;
	}


	public void setListeCaPeriodeAvisEcheanceNonTotalementPayes(List<DocumentChiffreAffaireDTO> listeCaPeriodeAvisEcheanceNonPayes) {
		this.listeCaPeriodeAvisEcheanceNonTotalementPayes = listeCaPeriodeAvisEcheanceNonPayes;
	}


	public BigDecimal getTauxTotalementPayesCa() {
		return tauxTotalementPayesCa;
	}


	public void setTauxTotalementPayesCa(BigDecimal tauxPayesCa) {
		this.tauxTotalementPayesCa = tauxPayesCa;
	}


	public int getNbAvisEcheanceTotalementPayesSurPeriode() {
		return nbAvisEcheanceTotalementPayesSurPeriode;
	}


	public void setNbAvisEcheancePayesSurPeriode(int nbAvisEcheancePayesSurPeriode) {
		this.nbAvisEcheanceTotalementPayesSurPeriode = nbAvisEcheancePayesSurPeriode;
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


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheance() {
		return totauxCaPeriodeAvisEcheance;
	}


	public void setTotauxCaPeriodeAvisEcheance(DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheance) {
		this.totauxCaPeriodeAvisEcheance = totauxCaPeriodeAvisEcheance;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceNonTotalementPayee() {
		return totauxCaPeriodeAvisEcheanceNonTotalementPayee;
	}


	public void setTotauxCaPeriodeAvisEcheanceNonTotalementPayee(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayee) {
		this.totauxCaPeriodeAvisEcheanceNonTotalementPayee = totauxCaPeriodeAvisEcheanceNonTotalementPayee;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer() {
		return totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer;
	}


	public void setTotauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer) {
		this.totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer = totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceTotalementPayee() {
		return totauxCaPeriodeAvisEcheanceTotalementPayee;
	}


	public void setTotauxCaPeriodeAvisEcheanceTotalementPayee(DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceTotalementPayee) {
		this.totauxCaPeriodeAvisEcheanceTotalementPayee = totauxCaPeriodeAvisEcheanceTotalementPayee;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}


	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}


	public BarChartModel getBarChartModelAvisEcheanceJour() {
		return barChartModelAvisEcheanceJour;
	}


	public void setBarChartModelAvisEcheanceJour(BarChartModel barChartModelAvisEcheanceJour) {
		this.barChartModelAvisEcheanceJour = barChartModelAvisEcheanceJour;
	}

	
//	protected ChartSeries createBarChartSerieMensuelAvisEcheance(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
//        ChartSeries barreGraphique = new ChartSeries();
//        barreGraphique.setLabel(titre);
//
//        List<InfosMois> listMoisPeriode = new ArrayList<InfosMois>();
//        listMoisPeriode = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//        
//     // Formate les codes des mois sur deux caractères sur la liste de résultat de la requête
//        for (int i = 0; i < listeMontantJmaDocument.size(); i++) {
//        	if (listeMontantJmaDocument.get(i).getMois().length()<2){
//        		listeMontantJmaDocument.get(i).setMois('0'+listeMontantJmaDocument.get(i).getMois());
//        	}
//    	}
//
//        // complète la liste des mois manquant dans la requête puisque celle-ci ne remonte que les mois qui ont 
//        //un chiffre d'affaire
//        for (InfosMois ligneInfosMois : listMoisPeriode) {
//        	DocumentChiffreAffaireDTO objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee(), listeMontantJmaDocument); 
//        	if (objMoisAnneeCa!= null ){
//        		barreGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),objMoisAnneeCa.getResteARegler());
//        	} else {
//        		barreGraphique.set(ligneInfosMois.getNomCourt()+" "+ ligneInfosMois.getCodeMoisAnnee().substring(2),0);
//        	}
//
//		}
////		Calendar mCalendar = Calendar.getInstance();
////        for (DocumentChiffreAffaireDTO ligneInfosMois : listeMontantJmaDocument) {
////        		mCalendar.setTime(LibDate.stringToDate2("01"+ligneInfosMois.getMois()+ligneInfosMois.getAnnee()));
////        		barreGraphique.set(mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()), ligneInfosMois.getMtHtCalc());
//
////        }
//        return barreGraphique;
//	}
	
	
//	protected ChartSeries createBarChartSerieJourAvisEcheance(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
//		
//        ChartSeries barreGraphique = new ChartSeries();
//        barreGraphique.setLabel(titre);
//
//        List<InfosJours> listJoursPeriode = new ArrayList<InfosJours>();
//        listJoursPeriode = LibDate.listeJoursEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//
//        // complète la liste des jours manquant dans la requête puisque celle-ci ne remonte que les jours qui ont 
//        //un chiffre d'affaire
//        for (InfosJours ligneInfosJour : listJoursPeriode) {
//        	DocumentChiffreAffaireDTO objJourAnneeCa = jourOfIndex(ligneInfosJour.getCodeJourMoisAnnee(),listeMontantJmaDocument); 
//        	if (objJourAnneeCa!= null ){
////        		jourGraphique.set(ligneInfosJour.getNumeroJour(),objJourAnneeCa.getMtHtCalc());
//        		barreGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),objJourAnneeCa.getResteARegler());
//        	} else {
//        		barreGraphique.set(ligneInfosJour.getNomCourt()+" "+ ligneInfosJour.getCodeJour(),0);
//        	}
//
//  		}
//        
//        return barreGraphique;
//	}
	
	
	public void requetesDisponiblesPourMoteur() {
	
	/////////// Debut Requete moteur ////////////*************

//	List<TaAvisEcheanceDTO> liste=taAvisEcheanceService.getAvisEcheanceAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
	
//	List<DocumentChiffreAffaireDTO> listeEssai;
//	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TIERS,"%");
//	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_ARTICLE,"%");
//	 	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
//	 	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_TIERS,"%");
////	 	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,BigDecimal.valueOf(7));
//	 	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,"%");
//	 	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TYPE_PAIEMENT,"%");
//	 	listeEssai= taAvisEcheanceService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_VENDEUR,"%");
	 
//	List<TaArticlesParTiersDTO> article;
//		article=taAvisEcheanceService.findArticlesParTiersNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//		article=taAvisEcheanceService.findArticlesParTiersNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),15);
//		article=taAvisEcheanceService.findArticlesParTiersParMois(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//		article=taAvisEcheanceService.findArticlesParTiersTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//		
//		listeEssai=taTiersService.findChiffreAffaireTotalParCodeTiersDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),"%");
//		listeEssai=taArticleService.findChiffreAffaireTotalParCodeArticleDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),"%");
	 
////	List<DocumentChiffreAffaireDTO> listeEssai;
//	listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TIERS,"%");
//	listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_ARTICLE,"%");
//	 listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
//	 listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_TIERS,"%");
////	 listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,BigDecimal.valueOf(7));
//	 listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,"%");
//	 listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TYPE_PAIEMENT,"%");
//	 listeEssai= taTicketDeCaisseService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_VENDEUR,"%");
	
	/////////// Fin Requete moteur ////////////*************
		
	}

	public BarChartModel getBarChartModelAvisEcheanceComp() {
		return barChartModelAvisEcheanceComp;
	}

	public void setBarChartModelAvisEcheanceComp(BarChartModel barChartModelAvisEcheanceComp) {
		this.barChartModelAvisEcheanceComp = barChartModelAvisEcheanceComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceComp() {
		return totauxCaPeriodeAvisEcheanceComp;
	}

	public void setTotauxCaPeriodeAvisEcheanceComp(DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceComp) {
		this.totauxCaPeriodeAvisEcheanceComp = totauxCaPeriodeAvisEcheanceComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceNonTotalementPayeeComp() {
		return totauxCaPeriodeAvisEcheanceNonTotalementPayeeComp;
	}

	public void setTotauxCaPeriodeAvisEcheanceNonTotalementPayeeComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeComp) {
		this.totauxCaPeriodeAvisEcheanceNonTotalementPayeeComp = totauxCaPeriodeAvisEcheanceNonTotalementPayeeComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp() {
		return totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp;
	}

	public void setTotauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp) {
		this.totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp = totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceTotalementPayeeComp() {
		return totauxCaPeriodeAvisEcheanceTotalementPayeeComp;
	}

	public void setTotauxCaPeriodeAvisEcheanceTotalementPayeeComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceTotalementPayeeComp) {
		this.totauxCaPeriodeAvisEcheanceTotalementPayeeComp = totauxCaPeriodeAvisEcheanceTotalementPayeeComp;
	}

	public int getNbAvisEcheanceSurPeriodeComp() {
		return nbAvisEcheanceSurPeriodeComp;
	}

	public void setNbAvisEcheanceSurPeriodeComp(int nbAvisEcheanceSurPeriodeComp) {
		this.nbAvisEcheanceSurPeriodeComp = nbAvisEcheanceSurPeriodeComp;
	}

	public int getNbAvisEcheanceNonTotalementPayesSurPeriodeComp() {
		return nbAvisEcheanceNonTotalementPayesSurPeriodeComp;
	}

	public void setNbAvisEcheanceNonTotalementPayesSurPeriodeComp(int nbAvisEcheanceNonTotalementPayesSurPeriodeComp) {
		this.nbAvisEcheanceNonTotalementPayesSurPeriodeComp = nbAvisEcheanceNonTotalementPayesSurPeriodeComp;
	}

	public int getNbAvisEcheanceTotalementPayesSurPeriodeComp() {
		return nbAvisEcheanceTotalementPayesSurPeriodeComp;
	}

	public void setNbAvisEcheanceTotalementPayesSurPeriodeComp(int nbAvisEcheanceTotalementPayesSurPeriodeComp) {
		this.nbAvisEcheanceTotalementPayesSurPeriodeComp = nbAvisEcheanceTotalementPayesSurPeriodeComp;
	}

	public int getNbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp() {
		return nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp;
	}

	public void setNbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp(
			int nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp) {
		this.nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp = nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeComp;
	}

	public BigDecimal getTauxTotalementPayesNbComp() {
		return tauxTotalementPayesNbComp;
	}

	public void setTauxTotalementPayesNbComp(BigDecimal tauxTotalementPayesNbComp) {
		this.tauxTotalementPayesNbComp = tauxTotalementPayesNbComp;
	}

	public BigDecimal getTauxTotalementPayesCaComp() {
		return tauxTotalementPayesCaComp;
	}

	public void setTauxTotalementPayesCaComp(BigDecimal tauxTotalementPayesCaComp) {
		this.tauxTotalementPayesCaComp = tauxTotalementPayesCaComp;
	}

	public BigDecimal getMontantTotalHtComp() {
		return MontantTotalHtComp;
	}

	public void setMontantTotalHtComp(BigDecimal montantTotalHtComp) {
		MontantTotalHtComp = montantTotalHtComp;
	}

	public BigDecimal getMontantTotalHtTotalementPayesComp() {
		return MontantTotalHtTotalementPayesComp;
	}

	public void setMontantTotalHtTotalementPayesComp(BigDecimal montantTotalHtTotalementPayesComp) {
		MontantTotalHtTotalementPayesComp = montantTotalHtTotalementPayesComp;
	}

	public BigDecimal getMontantTotalHtNonTotalementPayesComp() {
		return MontantTotalHtNonTotalementPayesComp;
	}

	public void setMontantTotalHtNonTotalementPayesComp(BigDecimal montantTotalHtNonTotalementPayesComp) {
		MontantTotalHtNonTotalementPayesComp = montantTotalHtNonTotalementPayesComp;
	}

	public BigDecimal getMontantTotalHtNonTotalementPayesARelancerComp() {
		return MontantTotalHtNonTotalementPayesARelancerComp;
	}

	public void setMontantTotalHtNonTotalementPayesARelancerComp(BigDecimal montantTotalHtNonTotalementPayesARelancerComp) {
		MontantTotalHtNonTotalementPayesARelancerComp = montantTotalHtNonTotalementPayesARelancerComp;
	}

	public LineChartModel getLineChartModelAvisEcheance() {
		return lineChartModelAvisEcheance;
	}

	public void setLineChartModelAvisEcheance(LineChartModel lineChartModelAvisEcheance) {
		this.lineChartModelAvisEcheance = lineChartModelAvisEcheance;
	}

	public LineChartModel getLineChartModelAvisEcheanceJour() {
		return lineChartModelAvisEcheanceJour;
	}

	public void setLineChartModelAvisEcheanceJour(LineChartModel lineChartModelAvisEcheanceJour) {
		this.lineChartModelAvisEcheanceJour = lineChartModelAvisEcheanceJour;
	}

	public List<String> getListeGraphiqueComparatifCADate() {
		return listeGraphiqueComparatifCADate;
	}

	public void setListeGraphiqueComparatifCADate(List<String> listeGraphiqueComparatifCADate) {
		this.listeGraphiqueComparatifCADate = listeGraphiqueComparatifCADate;
	}

	public List<BigDecimal> getListeGraphiqueComparatifCAMontant1() {
		return listeGraphiqueComparatifCAMontant1;
	}

	public void setListeGraphiqueComparatifCAMontant1(List<BigDecimal> listeGraphiqueComparatifCAMontant1) {
		this.listeGraphiqueComparatifCAMontant1 = listeGraphiqueComparatifCAMontant1;
	}

	public List<BigDecimal> getListeGraphiqueComparatifCAMontant2() {
		return listeGraphiqueComparatifCAMontant2;
	}

	public void setListeGraphiqueComparatifCAMontant2(List<BigDecimal> listeGraphiqueComparatifCAMontant2) {
		this.listeGraphiqueComparatifCAMontant2 = listeGraphiqueComparatifCAMontant2;
	}


	public TaEditionDTO getEditionDefautImpression() {
		return editionDefautImpression;
	}


	public void setEditionDefautImpression(TaEditionDTO editionDefautImpression) {
		this.editionDefautImpression = editionDefautImpression;
	}


	public int getNbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours() {
		return nbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours;
	}


	public void setNbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours(
			int nbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours) {
		this.nbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours = nbAvisEcheanceNonTotalementPayesSurPeriodeAboEnCours;
	}


	public int getNbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours() {
		return nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours;
	}


	public void setNbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours(
			int nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours) {
		this.nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours = nbAvisEcheanceNonTotalementPayesARelancerSurPeriodeAboEnCours;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours() {
		return totauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours;
	}


	public void setTotauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours) {
		this.totauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours = totauxCaPeriodeAvisEcheanceNonTotalementPayeeAboEnCours;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours() {
		return totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours;
	}


	public void setTotauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours(
			DocumentChiffreAffaireDTO totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours) {
		this.totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours = totauxCaPeriodeAvisEcheanceNonTotalementPayeeARelancerAboEnCours;
	}


//	public List<DocumentChiffreAffaireDTO> getListeDocumentFactureParTiers() {
//		return listeDocumentFactureParTiers;
//	}
//
//
//	public void setListeDocumentFactureParTiers(List<DocumentChiffreAffaireDTO> listeDocumentFactureParTiers) {
//		this.listeDocumentFactureParTiers = listeDocumentFactureParTiers;
//	}



//	public DonutChartModel getDonutModel() {
//		return donutModel;
//	}
//
//
//	public void setDonutModel(DonutChartModel donutModel) {
//		this.donutModel = donutModel;
//	}
}
