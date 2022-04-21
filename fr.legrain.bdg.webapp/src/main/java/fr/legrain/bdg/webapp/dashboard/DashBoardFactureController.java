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
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
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
import fr.legrain.document.model.TaFacture;
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
public class DashBoardFactureController/*<MoisExoCourant>*/ extends DashBoardDocumentController implements Serializable {

	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2461636944048269235L;
	private @EJB ITaFactureServiceRemote taFactureService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaEditionServiceRemote taEditionService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	private @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	
//	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

    private BarChartModel barChartModelFactureJour; //AJOUTER LE 06/09/2017 Jean Marc
    private LineChartModel lineChartModelFactureJour;
//	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    
    //TEST Chart.js
//    private DonutChartModel donutModel;
    
    private BarChartModel barChartModelFacture;
    private BarChartModel barChartModelFactureComp;
    private LineChartModel lineChartModelFacture;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    private Date dateDebut;
//    private Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
//    private String codeEtatFacture;
//    private List<DocumentDTO> valueLignesFacture;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFacture;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureNonTotalementPayes;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureNonTotalementPayesARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFacture;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayee;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayeeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureTotalementPayee;
    //rajout comparaison
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayeeComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayeeARelancerComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeFactureTotalementPayeeComp;
    
    private int nbFactureSurPeriode = 0;
    private int nbFactureNonTotalementPayesSurPeriode = 0 ;
    private int nbFactureTotalementPayesSurPeriode = 0;
    private int nbFactureNonTotalementPayesARelancerSurPeriode = 0;
    
    private List<String> listeGraphiqueComparatifCADate = new ArrayList<String>();
    private List<BigDecimal> listeGraphiqueComparatifCAMontant1 = new ArrayList<BigDecimal>();
    private List<BigDecimal> listeGraphiqueComparatifCAMontant2 = new ArrayList<BigDecimal>();
    
    //rajout comparaison
    private int nbFactureSurPeriodeComp = 0;
    private int nbFactureNonTotalementPayesSurPeriodeComp = 0 ;
    private int nbFactureTotalementPayesSurPeriodeComp = 0;
    private int nbFactureNonTotalementPayesARelancerSurPeriodeComp = 0;
    
    
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
    
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
		setTaDocumentService(taFactureService);
//    	infoEntreprise = taInfoEntrepriseService.findInstance();
//    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	listeEdition = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaFacture.TYPE_DOC.toUpperCase());

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
//    	barChartModelFacture = new BarChartModel();
//    	createDonutModel();
    	//Initialisation des données sur la période exercice
    	actRechercheFacture(codeEtatDocument);
    }
    
    
    public void imprimeEdition(int idDocument, TaEditionDTO edition, String modeEdition, String pourClient) {
		//ICI COMMENCE LA PARTIE actDialogImprimer
    	TaFacture facture = new TaFacture();
		try {
			facture = taFactureService.findById(idDocument);
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
						

						String pdf = taFactureService.generePDF(facture.getIdDocument(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
						PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");
//						masterEntity = taFactureService.findById(masterEntity.getIdDocument());
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


    public void refreshDashBoard(String etatFacture){
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
    	actRechercheFacture(etatFacture);
    	if(comparaison) {
    		initPeriodeComp();
    		actRechercheFactureComp(etatFacture);
    	}
    }
    
//    public void refreshDashBoardComp(String etatFacture){
//    	initPeriodeComp();
//    	if(comparaison) {
//    		actRechercheFactureComp(etatFacture);
//    	}else {
//    		actRechercheFacture(etatFacture);
//    	}
//    	
//    }


	public void actRechercheFacture(String codeEtatFacture){
	     BigDecimal totalResteARegler = BigDecimal.ZERO;
//		barChartModelFacture.clear();
    	setInfoEtatDocument(codeEtatFacture);
    	setCodeEtatDocument(codeEtatFacture);

   	
    	
    	//pour essai 
    	requetesDisponiblesPourMoteur();
    	
    	
    	
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodeFacture = taFactureService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	
//    	totauxCaPeriodeFacture = taFactureService.chiffreAffaireTotalDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%",true);
//    	totauxCaPeriodeFacture = taFactureService.chiffreAffaireTotalDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,"5.5",true);
//    	totauxCaPeriodeFacture = taFactureService.chiffreAffaireTotalDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TYPE_PAIEMENT,"CB",true);
    	
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		totauxCaPeriodeFactureNonTotalementPayee = taFactureService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		totauxCaPeriodeFactureNonTotalementPayeeARelancer = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodeFactureTotalementPayee = taFactureService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre total de documents sur la période
		nbFactureSurPeriode = (int) taFactureService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de Facture non totalement payés sur la période
		nbFactureNonTotalementPayesSurPeriode = (int) taFactureService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
	// Retourne le nombre de Facture à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbFactureNonTotalementPayesARelancerSurPeriode = (int) taFactureService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
	// Retourne le nombre de Facturetransformés sur la période
		nbFactureTotalementPayesSurPeriode = (int) taFactureService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);    	

		tauxTotalementPayesNb = BigDecimal.ZERO;
	    tauxTotalementPayesCa = BigDecimal.ZERO;
	    
		if (nbFactureSurPeriode > 0) {
			try {
				tauxTotalementPayesNb =  (new BigDecimal(nbFactureTotalementPayesSurPeriode) .divide(new BigDecimal(nbFactureSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTotalementPayesCa = totauxCaPeriodeFactureTotalementPayee.getMtTtcCalc() .divide(totauxCaPeriodeFacture.getMtTtcCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		switch (codeEtatFacture) {
		case ETAT_DOCUMENT_TOUS:
			// Renvoi la liste des documents présents sur la période en cours

//			valueLignesDocument = taFactureService.findAllDTOPeriodeParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_ARTICLE,"PORT");
//			valueLignesDocument = taFactureService.findAllDTOPeriodeParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TIERS,"T00001");
//			valueLignesDocument = taFactureService.findAllDTOPeriodeParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_TIERS,"PROF");
//			valueLignesDocument = taFactureService.findAllDTOPeriodeParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"F1");
//			valueLignesDocument = taFactureService.findAllDTOPeriodeParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,"5.5");
//			valueLignesDocument = taFactureService.findAllDTOPeriodeParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TYPE_PAIEMENT,"C");
			valueLignesDocument = taFactureService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			for (DocumentDTO l : valueLignesDocument) {
				totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());
			}
			infoLabel = "Liste des factures réalisées ( reste à règler : "+totalResteARegler+")";
			infoLabel2 = "Facturation mensuelle réalisées";
//			valueLignesDocument = taFactureService.findAllDTOPeriodeParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, Const.PAR_TYPE_PAIEMENT,"%");
//			valueLignesDocument = taFactureService.findAllDTOPeriodeAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, "%", Const.PAR_TYPE_PAIEMENT,"%");
			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois Comp", listeCaMoisDocumentComp, dateDebutComp, dateFinComp));
			/**RAJOUT YANN**/
//			listeDocumentFactureParTiers = taFactureService.listLigneArticleDTOTiers(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null);
			listeDocumentFactureParTiers = taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, null, 0);
			initInfosLabelRapportMensuelDataTable("Facture");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesDocument = taFactureService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			for (DocumentDTO l : valueLignesDocument) {
				totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());
			}

			infoLabel = "Liste des factures non payées ( reste à règler : "+totalResteARegler+")";
//			valueLignesDocument = taFactureService.findDocumentNonTransfosDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
//			listeDocumentFactureParTiers= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ETAT_NON_TRANSFORME, 0);
			//ISA le 08/08/2019 suite à prise en charge des différents états
			listeDocumentFactureParTiers= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ENCOURS, 0);
			
//			listeCaMoisDocument = taFactureService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_FAMILLE_TIERS,"%");
//			listeCaMoisDocument = taFactureService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_FAMILLE_ARTICLE,"%");
//			listeCaMoisDocument = taFactureService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_ARTICLE,"%");
//			listeCaMoisDocument = taFactureService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_TIERS,"%");
//			listeCaMoisDocument = taFactureService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_TAUX_TVA,"%");
//			listeCaMoisDocument = taFactureService.countDocumentAndCodeEtatParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"ANNULE",Const.PAR_TYPE_PAIEMENT,"%");
			
//			barChartModelFacture.addSeries(createBarChartSerieMensuelFacture("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesFacture = taFactureService.findFactureNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));			
			initInfosLabelRapportMensuelDataTable("Facture");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
							  CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);

			break;
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesDocument = taFactureService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			for (DocumentDTO l : valueLignesDocument) {
				totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());
			}
			infoLabel = "Liste des factures Déjà réglé ( reste à règler : "+totalResteARegler+")";
//			valueLignesDocument = taFactureService.findDocumentTransfosDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taFactureService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			listeDocumentFactureParTiers= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ETAT_TRANSFORME, 0);
			//ISA  le 08/08/2019 suite à prise en charge des différents états
			listeDocumentFactureParTiers= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.TERMINE_TOTALEMENT_TRANSFORME, 0);
//			valueLignesFacture = taFactureService.findFactureTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));			
			initInfosLabelRapportMensuelDataTable("Facture");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL,
					  		  CSS_DBOARD_BLOC_ACTIF_FACTURE);
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			valueLignesDocument = taFactureService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			for (DocumentDTO l : valueLignesDocument) {
				totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());
			}
			infoLabel = "Liste des factures à relancer ( reste à règler : "+totalResteARegler+")";
//			valueLignesDocument = taFactureService.findDocumentNonTransfosARelancerDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,15,null);
			listeDocumentFactureParTiers= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, TaEtat.ETAT_NON_TRANSFORME_A_RELANCER, 15);
//			barChartModelFacture.addSeries(createBarChartSerieMensuelFacture("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			valueLignesFacture = taFactureService.findFactureNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1);
//			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));			
			initInfosLabelRapportMensuelDataTable("Facture");
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
	public void actRechercheFactureComp(String codeEtatFacture){
		
		if(comparaison) {
//			barChartModelFacture.clear();
	    	setInfoEtatDocument(codeEtatFacture);
	    	setCodeEtatDocument(codeEtatFacture);

	   	
	    	
	    	//pour essai 
	    	requetesDisponiblesPourMoteur();
	    	
	    	
	    	
	    	listMoisAnneeExoComp = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
	    	
	    	
	    	// Calcule les CA (HT, TVA, TTC) total sur la période
	    	totauxCaPeriodeFactureComp = taFactureService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
	    	
			// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
			totauxCaPeriodeFactureNonTotalementPayeeComp = taFactureService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
			
			// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
			totauxCaPeriodeFactureNonTotalementPayeeARelancerComp = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 15,null);

			// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
			totauxCaPeriodeFactureTotalementPayeeComp = taFactureService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
			// Retourne le nombre total de documents sur la période
			nbFactureSurPeriodeComp = (int) taFactureService.countDocument(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
		// Retourne le nombre de Facture non totalement payés sur la période
			nbFactureNonTotalementPayesSurPeriodeComp = (int) taFactureService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
		// Retourne le nombre de Facture à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
			nbFactureNonTotalementPayesARelancerSurPeriodeComp = (int) taFactureService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 15,null);
		// Retourne le nombre de Facturetransformés sur la période
			nbFactureTotalementPayesSurPeriodeComp = (int) taFactureService.countDocumentTransforme(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);    	

			tauxTotalementPayesNbComp = BigDecimal.ZERO;
		    tauxTotalementPayesCaComp = BigDecimal.ZERO;
		    
			if (nbFactureSurPeriodeComp > 0) {
				try {
					tauxTotalementPayesNbComp =  (new BigDecimal(nbFactureTotalementPayesSurPeriodeComp) .divide(new BigDecimal(nbFactureSurPeriodeComp),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
					tauxTotalementPayesCaComp = totauxCaPeriodeFactureTotalementPayeeComp.getMtTtcCalc() .divide(totauxCaPeriodeFactureComp.getMtTtcCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
			
			switch (codeEtatFacture) {
			case ETAT_DOCUMENT_TOUS:
				// Renvoi la liste des documents présents sur la période en cours

				valueLignesDocumentComp = taFactureService.findAllDTOPeriode(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
				// Création et initialisation du graphique du CA HT / mois pour la période en cours
				listeCaMoisDocumentComp = taFactureService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,null);
				lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//				barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
				
				lineChartModelFacture = addLineSeriesMensuelComparaison(lineChartModelFacture,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				/**RAJOUT YANN**/
				listeDocumentFactureParTiersComp = taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, null, 0);
				initInfosLabelRapportMensuelDataTable("Facture");
				// Initialisent les classes css pour les block d'information
				initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP);
				break;

			case ETAT_DOCUMENT_NON_TRANSORME:
				valueLignesDocumentComp = taFactureService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
				lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
				listeCaMoisDocumentComp = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,null);
				
//				listeDocumentFactureParTiersComp= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(dateDebutComp, dateFinComp, null, null, TaEtat.ETAT_NON_TRANSFORME, 0);
				//ISA  le 08/08/2019 suite à prise en charge des différents états
				listeDocumentFactureParTiersComp= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, TaEtat.ENCOURS, 0);
				
				lineChartModelFacture = addLineSeriesMensuelComparaison(lineChartModelFacture,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
//				barChartModelFactureComp.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentComp, dateDebutComp, dateFinComp));
				
				// Création et initialisation du graphique du CA HT / mois pour la période en cours
		
				initInfosLabelRapportMensuelDataTable("Facture");
				initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL_COMP,
								  CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP);

				break;
				
			case ETAT_DOCUMENT_TRANSORME:
				valueLignesDocumentComp = taFactureService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null);
//				barChartModelFactureComp = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taFactureService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
				listeCaMoisDocumentComp = taFactureService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,null);
//				barChartModelFactureComp.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentComp, dateDebutComp, dateFinComp));
				
//				listeDocumentFactureParTiersComp= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(dateDebutComp, dateFinComp, null, null, TaEtat.ETAT_TRANSFORME, 0);
				//ISA  le 08/08/2019 suite à prise en charge des différents états
				listeDocumentFactureParTiersComp= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, TaEtat.TERMINE_TOTALEMENT_TRANSFORME, 0);

				lineChartModelFacture = addLineSeriesMensuelComparaison(lineChartModelFacture,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				initInfosLabelRapportMensuelDataTable("Facture");
				initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
						  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
						  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP,
						  		CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP);
				break;
				
			case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
				valueLignesDocumentComp = taFactureService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 15,null);
				lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//				barChartModelFactureComp = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				listeCaMoisDocument = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,15,null);
				listeCaMoisDocumentComp = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,15,null);
				listeDocumentFactureParTiersComp= taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, TaEtat.ETAT_NON_TRANSFORME_A_RELANCER, 15);
//				barChartModelFactureComp.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentComp, dateDebutComp, dateFinComp));
				
				lineChartModelFacture = addLineSeriesMensuelComparaison(lineChartModelFacture,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				initInfosLabelRapportMensuelDataTable("Facture");
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
					montantTotalHtMois = taFactureService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
					montantTotalHtMoisComp = taFactureService.chiffreAffaireTotalDTO(premierJourMoisComp, dernierJourMoisComp,null).getMtHtCalc();
					}else {
						montantTotalHtMoisComp = BigDecimal.ZERO;
					}
					initInfosLabelRapportMensuelDataTable("Factures");
					
					// on récupére dans listeDocumentDetailMois la liste des document sur la période
					
					listeDocumentDetailMois= taFactureService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
					listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
					listeCaJoursDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
					listeDocumentDetailMoisComp= taFactureService.findAllDTOPeriode(premierJourMoisComp, dernierJourMoisComp,null);
					listeDocumentArticleParTiersParMoisComp = taFactureService.findArticlesParTiersParMois(premierJourMoisComp, dernierJourMoisComp);
					listeCaJoursDocumentComp = taFactureService.listeChiffreAffaireTotalJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,null);
					}
					
					
					
//
					lineChartModelFactureJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelFactureJour = addLineSeriesJoursComparaison(lineChartModelFactureJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
//					barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					break;
					
					
				case ETAT_DOCUMENT_NON_TRANSORME:
					// Cumul du chiffre d'affaire par mois
					montantTotalHtMois = taFactureService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						montantTotalHtMoisComp = taFactureService.chiffreAffaireNonTransformeTotalDTO(premierJourMoisComp, dernierJourMoisComp,null).getMtHtCalc();
						}else {
							montantTotalHtMoisComp = BigDecimal.ZERO;
						}
					initInfosLabelRapportMensuelDataTable("Facture");
					// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
					listeDocumentDetailMois= taFactureService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
					listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
					listeCaJoursDocument = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						listeDocumentDetailMoisComp= taFactureService.findDocumentNonTransfosDTO(premierJourMoisComp, dernierJourMoisComp,null);
						listeDocumentArticleParTiersParMoisComp = taFactureService.findArticlesParTiersNonTransforme(premierJourMoisComp, dernierJourMoisComp);
						listeCaJoursDocumentComp = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,null);
						}
					
					lineChartModelFactureJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelFactureJour = addLineSeriesJoursComparaison(lineChartModelFactureJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
					
//					barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
				
					break;
					
				case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
					// Cumul du chiffre d'affaire par mois
					montantTotalHtMois = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						montantTotalHtMoisComp = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMoisComp, dernierJourMoisComp,15,null).getMtHtCalc();
						}else {
							montantTotalHtMoisComp = BigDecimal.ZERO;
						}
					initInfosLabelRapportMensuelDataTable("Facture");
					// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
					listeDocumentDetailMois= taFactureService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
					listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
					listeCaJoursDocument = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						listeDocumentDetailMoisComp= taFactureService.findDocumentNonTransfosARelancerDTO(premierJourMoisComp, dernierJourMoisComp,15,null);
						listeDocumentArticleParTiersParMoisComp = taFactureService.findArticlesParTiersNonTransformeARelancer(premierJourMoisComp, dernierJourMoisComp,15);
						listeCaJoursDocumentComp = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,15,null);
						}
					
					lineChartModelFactureJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelFactureJour = addLineSeriesJoursComparaison(lineChartModelFactureJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
//					barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					break;
					
				case ETAT_DOCUMENT_TRANSORME:
					// Cumul du chiffre d'affaire par mois
					montantTotalHtMois = taFactureService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						montantTotalHtMoisComp = taFactureService.chiffreAffaireTransformeTotalDTO(premierJourMoisComp, dernierJourMoisComp,null).getMtHtCalc();
						}else {
							montantTotalHtMoisComp = BigDecimal.ZERO;
						}
					initInfosLabelRapportMensuelDataTable("Facture");
					// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
					listeDocumentDetailMois= taFactureService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
					listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
					listeCaJoursDocument = taFactureService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						listeDocumentDetailMoisComp= taFactureService.findDocumentTransfosDTO(premierJourMoisComp, dernierJourMoisComp,null);
						listeDocumentArticleParTiersParMoisComp = taFactureService.findArticlesParTiersTransforme(premierJourMoisComp, dernierJourMoisComp);
						listeCaJoursDocumentComp = taFactureService.listeChiffreAffaireTransformeJmaDTO(premierJourMoisComp, dernierJourMoisComp, 2,null);
						}
					
					lineChartModelFactureJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelFactureJour = addLineSeriesJoursComparaison(lineChartModelFactureJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
					
//					barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					
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
		initInfosLabelRapportMensuelDataTable("Factures");
		System.out.println(codeMoisAnnee);
	
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
	montantTotalHtMois = taFactureService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
	initInfosLabelRapportMensuelDataTable("Facture");
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailMois= taFactureService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);

	listeCaJoursDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);

	
	barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taFactureService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
		initInfosLabelRapportMensuelDataTable("Facture");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taFactureService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
		initInfosLabelRapportMensuelDataTable("Facture");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taFactureService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
		listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		listeCaJoursDocument = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		montantTotalHtMois = taFactureService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
		initInfosLabelRapportMensuelDataTable("Facture");
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailMois= taFactureService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
		listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
		listeCaJoursDocument = taFactureService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
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
	montantTotalHtJour = taFactureService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taFactureService.findAllDTOPeriode(jourMois, jourMois,null);

	
	setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersParMois(jourMois, jourMois));
//	setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersParMoisParTypeRegroupement(jourMois, jourMois,Const.PAR_FAMILLE_ARTICLE,"%"));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Facture");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taFactureService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransformeParTypeRegroupement(jourMois, jourMois,Const.PAR_FAMILLE_ARTICLE,"%"));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taFactureService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransformeARelancerParTypeRegroupement(jourMois, jourMois, 15,Const.PAR_FAMILLE_ARTICLE,"%"));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaProforma.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taFactureService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersTransforme(jourMois, jourMois));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Facture");
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
	montantTotalHtJour = taFactureService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();
	if(jourMoisComp!= null) {
		montantTotalHtJourComp = taFactureService.chiffreAffaireTotalDTO(jourMoisComp, jourMoisComp,null).getMtHtCalc();
		listeDocumentDetailJourComp = taFactureService.findAllDTOPeriode(jourMoisComp, jourMoisComp,null);
		setListeDocumentArticleParTiersParJourComp(taFactureService.findArticlesParTiersParMois(jourMoisComp, jourMoisComp));
	}else {
		montantTotalHtJourComp = BigDecimal.ZERO;
		listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
		listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
	}
	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	listeDocumentDetailJour= taFactureService.findAllDTOPeriode(jourMois, jourMois,null);
	
	
	setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersParMois(jourMois, jourMois));
	initInfosLabelRapportMensuelDataTable("Facture");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		if(jourMoisComp!= null) {
			montantTotalHtJourComp = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMoisComp, jourMoisComp,null).getMtHtCalc();
			listeDocumentDetailJourComp = taFactureService.findDocumentNonTransfosDTO(jourMoisComp, jourMoisComp,null);
			setListeDocumentArticleParTiersParJourComp(taFactureService.findArticlesParTiersNonTransforme(jourMoisComp, jourMoisComp));
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taFactureService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransforme(jourMois, jourMois));
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		if(jourMoisComp!= null) {
			montantTotalHtJourComp = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMoisComp, jourMoisComp,15,null).getMtHtCalc();
			listeDocumentDetailJourComp = taFactureService.findDocumentNonTransfosARelancerDTO(jourMoisComp, jourMoisComp,15,null);
			setListeDocumentArticleParTiersParJourComp(taFactureService.findArticlesParTiersNonTransformeARelancer(jourMoisComp, jourMoisComp, 15));
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taFactureService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		if(jourMoisComp!= null) {
			montantTotalHtJourComp = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMoisComp, jourMoisComp,null).getMtHtCalc();
			listeDocumentDetailJourComp = taFactureService.findDocumentTransfosDTO(jourMoisComp, jourMoisComp,null);
			setListeDocumentArticleParTiersParJourComp(taFactureService.findArticlesParTiersTransforme(jourMoisComp, jourMoisComp));
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		listeDocumentDetailJour= taFactureService.findDocumentTransfosDTO(jourMois, jourMois,null);
		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersTransforme(jourMois, jourMois));
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
	}
		
    }
	
	public void actInitPaiementCarteBancaire(ActionEvent actionEvent) {
		try {
			Integer id = (Integer) actionEvent.getComponent().getAttributes().get("idDocument");
			
			System.out.println("DashBoardFactureController.actInitPaiementCarteBancaire() "+id);
				//setFactureReglee(true);
		//actInitReglement();
			//calculDateEcheance(true);
			
			if(id!=null) {
				PaiementParam param = new PaiementParam();
				TaFacture taFacture =  taFactureService.findById(id);
				param.setDocumentPayableCB(taFactureService.findById(id));
				param.setMontantPaiement(taFacture.getResteAReglerComplet());
				param.setMontantLibre(false);
				param.setTiersPourReglementLibre(null);
				param.setOriginePaiement("BDG Dashboard Facture");
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
				TaFacture taFacture =  taFactureService.findById(id);
				mapDialogue.put("masterEntity",taFacture );

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
		actRechercheFacture(codeEtatDocument);
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
		actRechercheFacture(codeEtatDocument);
	}
	
	public void actImprimerListeFacture(ActionEvent actionEvent) {
		
		
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
		sessionMap.put("listeFacture", valueLignesDocument);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimerListeFactureComparaison(ActionEvent actionEvent) {
		
		
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
		sessionMap.put("listeFacture", valueLignesDocument);
		sessionMap.put("listeFactureComp", valueLignesDocumentComp);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public void actImprimerListeFactureDetailComparaison(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		
		sessionMap.remove("parametre");
		sessionMap.remove("listeFacture");
		sessionMap.remove("listeFactureComp");
		sessionMap.remove("infosLabel");
		sessionMap.remove("infosLabel2");
		
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", infoLabel);
		mapParametre.put("infosLabel2", infoLabel2);
		mapParametre.put("debutComp", LibDate.localDateToDate(getDateDebutComp()));
		mapParametre.put("finComp", LibDate.localDateToDate(getDateFinComp()));
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeFacture", valueLignesDocument);
		sessionMap.put("listeFactureComp", valueLignesDocumentComp);
		BirtCompareCaMensuelDasboardFacture listeCompare = null;
		HashMap<String, BirtCompareCaMensuelDasboardFacture > valueLignesDocumentComparative = new HashMap<>();
		
		for (InfosMois r : petiteListeGraphiqueMensuel) {
			listeCompare = new BirtCompareCaMensuelDasboardFacture();
			if(r.getMois()!=null) {
				listeCompare.setDateDoc(r.getMois());
				listeCompare.setMontantHtPeriode1(r.getMontant());
				valueLignesDocumentComparative.put(listeCompare.getDateDoc(), listeCompare);
				
			}
			
		}
			
		for (InfosMois r : grandeListeGraphiqueMensuel) {
			if(valueLignesDocumentComparative.get(r.getMois())==null) {
				listeCompare = new BirtCompareCaMensuelDasboardFacture();
				if(r.getMois()!=null) {
					listeCompare.setDateDoc(r.getMois());
					listeCompare.setMontantHtPeriode2(r.getMontant());
					valueLignesDocumentComparative.put(listeCompare.getDateDoc(), listeCompare);
					
				}
			} else {
				if(r.getMois()!=null) {
					valueLignesDocumentComparative.get(r.getMois()).setDateDoc(r.getMois());
					valueLignesDocumentComparative.get(r.getMois()).setMontantHtPeriode2(r.getMontant());
					
					
				}
			}
			 							
			}
		
		listeGraphiqueComparatifCADate.clear();
		listeGraphiqueComparatifCAMontant1.clear();
		listeGraphiqueComparatifCAMontant2.clear();
		
		for (String g : valueLignesDocumentComparative.keySet()) {
			 listeGraphiqueComparatifCADate.add(valueLignesDocumentComparative.get(g).getDateDoc());
			 listeGraphiqueComparatifCAMontant1.add(valueLignesDocumentComparative.get(g).getMontantHtPeriode1());
			 listeGraphiqueComparatifCAMontant2.add(valueLignesDocumentComparative.get(g).getMontantHtPeriode2());
		}
		
//		petiteListeGraphiqueMensuel.addAll(grandeListeGraphiqueMensuel);
		sessionMap.put("listeFactureGraphiqueDate", listeGraphiqueComparatifCADate);
		sessionMap.put("listeFactureGraphiqueMontant1", listeGraphiqueComparatifCAMontant1);
		sessionMap.put("listeFactureGraphiqueMontant2", listeGraphiqueComparatifCAMontant2);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actImprimerListeFactureDetail(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", "Détail des factures sur la période");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeFactureDetail", listeDocumentFactureParTiers);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimerListeFactureComp(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebutComp()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFinComp()));
		mapParametre.put("infosLabel", infoLabel);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeFacture", valueLignesDocumentComp);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actImprimerListeFactureDetailComp(ActionEvent actionEvent) {
	
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebutComp()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFinComp()));
	mapParametre.put("infosLabel", "Détail des factures sur la période");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeFactureDetail", listeDocumentFactureParTiersComp);

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
			
	public BarChartModel getBarChartModelFacture() {
		return barChartModelFacture;
	}
	public void setBarChartModelFactureExo(BarChartModel barChartModelFacture) {
		this.barChartModelFacture = barChartModelFacture;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

//	public String getCodeEtatFacture() {
//		return codeEtatFacture;
//	}
//
//	public void setCodeEtatFacture(String codeEtatFacture) {
//		this.codeEtatFacture = codeEtatFacture;
//	}

//	public List<DocumentDTO> getValueLignesFacture() {
//		return valueLignesFacture;
//	}
//
//	public void setValueLignesFacture(List<DocumentDTO> valueLignesFacture) {
//		this.valueLignesFacture = valueLignesFacture;
//	}

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


	public BarChartModel getBarChartModelFactureJour() {
		return barChartModelFactureJour;
	}


	public void setBarChartModelFactureJour(BarChartModel barChartModelFactureJour) {
		this.barChartModelFactureJour = barChartModelFactureJour;
	}

	
//	protected ChartSeries createBarChartSerieMensuelFacture(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
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
	
	
//	protected ChartSeries createBarChartSerieJourFacture(String titre, List<DocumentChiffreAffaireDTO> listeMontantJmaDocument, Date dateDebut, Date dateFin){
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

//	List<TaFactureDTO> liste=taFactureService.getFactureAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
	
//	List<DocumentChiffreAffaireDTO> listeEssai;
//	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TIERS,"%");
//	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_ARTICLE,"%");
//	 	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
//	 	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_TIERS,"%");
////	 	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,BigDecimal.valueOf(7));
//	 	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TAUX_TVA,"%");
//	 	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_TYPE_PAIEMENT,"%");
//	 	listeEssai= taFactureService.chiffreAffaireParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_VENDEUR,"%");
	 
//	List<TaArticlesParTiersDTO> article;
//		article=taFactureService.findArticlesParTiersNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//		article=taFactureService.findArticlesParTiersNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),15);
//		article=taFactureService.findArticlesParTiersParMois(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//		article=taFactureService.findArticlesParTiersTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
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

	public BarChartModel getBarChartModelFactureComp() {
		return barChartModelFactureComp;
	}

	public void setBarChartModelFactureComp(BarChartModel barChartModelFactureComp) {
		this.barChartModelFactureComp = barChartModelFactureComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFactureComp() {
		return totauxCaPeriodeFactureComp;
	}

	public void setTotauxCaPeriodeFactureComp(DocumentChiffreAffaireDTO totauxCaPeriodeFactureComp) {
		this.totauxCaPeriodeFactureComp = totauxCaPeriodeFactureComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFactureNonTotalementPayeeComp() {
		return totauxCaPeriodeFactureNonTotalementPayeeComp;
	}

	public void setTotauxCaPeriodeFactureNonTotalementPayeeComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayeeComp) {
		this.totauxCaPeriodeFactureNonTotalementPayeeComp = totauxCaPeriodeFactureNonTotalementPayeeComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFactureNonTotalementPayeeARelancerComp() {
		return totauxCaPeriodeFactureNonTotalementPayeeARelancerComp;
	}

	public void setTotauxCaPeriodeFactureNonTotalementPayeeARelancerComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTotalementPayeeARelancerComp) {
		this.totauxCaPeriodeFactureNonTotalementPayeeARelancerComp = totauxCaPeriodeFactureNonTotalementPayeeARelancerComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeFactureTotalementPayeeComp() {
		return totauxCaPeriodeFactureTotalementPayeeComp;
	}

	public void setTotauxCaPeriodeFactureTotalementPayeeComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeFactureTotalementPayeeComp) {
		this.totauxCaPeriodeFactureTotalementPayeeComp = totauxCaPeriodeFactureTotalementPayeeComp;
	}

	public int getNbFactureSurPeriodeComp() {
		return nbFactureSurPeriodeComp;
	}

	public void setNbFactureSurPeriodeComp(int nbFactureSurPeriodeComp) {
		this.nbFactureSurPeriodeComp = nbFactureSurPeriodeComp;
	}

	public int getNbFactureNonTotalementPayesSurPeriodeComp() {
		return nbFactureNonTotalementPayesSurPeriodeComp;
	}

	public void setNbFactureNonTotalementPayesSurPeriodeComp(int nbFactureNonTotalementPayesSurPeriodeComp) {
		this.nbFactureNonTotalementPayesSurPeriodeComp = nbFactureNonTotalementPayesSurPeriodeComp;
	}

	public int getNbFactureTotalementPayesSurPeriodeComp() {
		return nbFactureTotalementPayesSurPeriodeComp;
	}

	public void setNbFactureTotalementPayesSurPeriodeComp(int nbFactureTotalementPayesSurPeriodeComp) {
		this.nbFactureTotalementPayesSurPeriodeComp = nbFactureTotalementPayesSurPeriodeComp;
	}

	public int getNbFactureNonTotalementPayesARelancerSurPeriodeComp() {
		return nbFactureNonTotalementPayesARelancerSurPeriodeComp;
	}

	public void setNbFactureNonTotalementPayesARelancerSurPeriodeComp(
			int nbFactureNonTotalementPayesARelancerSurPeriodeComp) {
		this.nbFactureNonTotalementPayesARelancerSurPeriodeComp = nbFactureNonTotalementPayesARelancerSurPeriodeComp;
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

	public LineChartModel getLineChartModelFacture() {
		return lineChartModelFacture;
	}

	public void setLineChartModelFacture(LineChartModel lineChartModelFacture) {
		this.lineChartModelFacture = lineChartModelFacture;
	}

	public LineChartModel getLineChartModelFactureJour() {
		return lineChartModelFactureJour;
	}

	public void setLineChartModelFactureJour(LineChartModel lineChartModelFactureJour) {
		this.lineChartModelFactureJour = lineChartModelFactureJour;
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



//	public DonutChartModel getDonutModel() {
//		return donutModel;
//	}
//
//
//	public void setDonutModel(DonutChartModel donutModel) {
//		this.donutModel = donutModel;
//	}
}
