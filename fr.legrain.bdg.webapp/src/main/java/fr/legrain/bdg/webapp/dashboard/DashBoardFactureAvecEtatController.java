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
public class DashBoardFactureAvecEtatController extends DashBoardDocumentController implements Serializable {

	
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
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFacture=new ArrayList<DocumentChiffreAffaireDTO>();
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureNonTotalementPayes=new ArrayList<DocumentChiffreAffaireDTO>();
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeFactureNonTotalementPayesARelancer=new ArrayList<DocumentChiffreAffaireDTO>();

    
  

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
    private List<DocumentChiffreAffaireDTO> nbFactureSurPeriodeAvecEtat=new ArrayList<DocumentChiffreAffaireDTO>();
    private List<DocumentChiffreAffaireDTO> nbFactureSurPeriodeCompAvecEtat=new ArrayList<DocumentChiffreAffaireDTO>();
    
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
    
    private BigDecimal totalResteARegler = BigDecimal.ZERO;
    
//    private List<DocumentChiffreAffaireDTO> listeCaMoisDocument;// Récupère le ca ht par mois dans un liste
//    private List<DocumentChiffreAffaireDTO> listeCaJoursDocument;// Récupère le ca ht par jour dans un liste
    
    private TaEditionDTO editionDefautImpression = null;
    
    
    @PostConstruct
	public void postConstruct(){
    	System.out.println("postConstruct Début");
    	super.postConstruct();
		setTaDocumentService(taFactureService);
//    	infoEntreprise = taInfoEntrepriseService.findInstance();
//    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
    	codeEtatDocument = ETAT_DOCUMENT_AUCUN;
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
    	System.out.println("postConstruct Fin");
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
//		barChartModelFacture.clear();
		System.out.println("actRechercheFacture Début");
    	setInfoEtatDocument(codeEtatFacture);
    	setCodeEtatDocument(codeEtatFacture);
    	totauxCaPeriodeFactureTotalementPayee=new DocumentChiffreAffaireDTO();
    	nbFactureTotalementPayesSurPeriode=0;
    	totauxCaPeriodeFactureNonTotalementPayeeARelancer=new DocumentChiffreAffaireDTO();
    	nbFactureNonTotalementPayesARelancerSurPeriode=0;
    	totauxCaPeriodeFactureNonTotalementPayee=new DocumentChiffreAffaireDTO();
    	nbFactureNonTotalementPayesSurPeriode=0;
    	totauxCaPeriodeFacture=new DocumentChiffreAffaireDTO();
    	nbFactureSurPeriode=0;
    	
    	listeCaMoisDocument.clear();
    	valueLignesDocument.clear();
    	listeDocumentFactureParTiers.clear();
    	
    	//pour essai 
//    	requetesDisponiblesPourMoteur();
    	
    	totalResteARegler=new BigDecimal(0);
    	
    	
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	System.out.println("Début requetes");
    	listeTotauxCaPeriodeFactureAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"%");
		valueLignesDocumentTotalEtat=taFactureService.findAllDTOPeriodeAvecEtat(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,"%");
		nbFactureSurPeriodeAvecEtat=taFactureService.countDocumentAvecEtat(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, "%");
		listeCaMoisDocumentTout = taFactureService.listeChiffreAffaireTotalJmaAvecEtatDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null,"%");
		System.out.println("fin requetes");
		
    	BigDecimal ht=BigDecimal.ZERO;
    	BigDecimal tva=BigDecimal.ZERO;
    	BigDecimal ttc=BigDecimal.ZERO;
    	
    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
    		//tous les documents totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			totauxCaPeriodeFactureTotalementPayee.setMtHtCalc(totauxCaPeriodeFactureTotalementPayee.getMtHtCalc().add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			totauxCaPeriodeFactureTotalementPayee.setMtTvaCalc(totauxCaPeriodeFactureTotalementPayee.getMtTvaCalc().add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			totauxCaPeriodeFactureTotalementPayee.setMtTtcCalc(totauxCaPeriodeFactureTotalementPayee.getMtTtcCalc().add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			totauxCaPeriodeFactureTotalementPayee.setReglementComplet(totauxCaPeriodeFactureTotalementPayee.getReglementComplet().add(l.getReglementComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			totauxCaPeriodeFactureTotalementPayee.setResteAReglerComplet(totauxCaPeriodeFactureTotalementPayee.getResteAReglerComplet().add(l.getResteAReglerComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
    		}
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
        		//tous les documents à relancer mais pas terminé
    			if(l.getRelancer()) {
        			totauxCaPeriodeFactureNonTotalementPayeeARelancer.setMtHtCalc(totauxCaPeriodeFactureNonTotalementPayeeARelancer.getMtHtCalc().add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayeeARelancer.setMtTvaCalc(totauxCaPeriodeFactureNonTotalementPayeeARelancer.getMtTvaCalc().add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayeeARelancer.setMtTtcCalc(totauxCaPeriodeFactureNonTotalementPayeeARelancer.getMtTtcCalc().add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayeeARelancer.setReglementComplet(totauxCaPeriodeFactureNonTotalementPayeeARelancer.getReglementComplet().add(l.getReglementComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayeeARelancer.setResteAReglerComplet(totauxCaPeriodeFactureNonTotalementPayeeARelancer.getResteAReglerComplet().add(l.getResteAReglerComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			}
//    			else {
        			totauxCaPeriodeFactureNonTotalementPayee.setMtHtCalc(totauxCaPeriodeFactureNonTotalementPayee.getMtHtCalc().add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayee.setMtTvaCalc(totauxCaPeriodeFactureNonTotalementPayee.getMtTvaCalc().add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayee.setMtTtcCalc(totauxCaPeriodeFactureNonTotalementPayee.getMtTtcCalc().add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayee.setReglementComplet(totauxCaPeriodeFactureNonTotalementPayee.getReglementComplet().add(l.getReglementComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
        			totauxCaPeriodeFactureNonTotalementPayee.setResteAReglerComplet(totauxCaPeriodeFactureNonTotalementPayee.getResteAReglerComplet().add(l.getResteAReglerComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
//    			}
    		}
    		
    		ht=ht.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
    		tva=tva.add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
    		ttc=ttc.add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
		}
    	
    	for (DocumentChiffreAffaireDTO l : nbFactureSurPeriodeAvecEtat) {
    		//tous les documents totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			nbFactureTotalementPayesSurPeriode=nbFactureTotalementPayesSurPeriode+(int) l.getCount();
    		}
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
        		//tous les documents à relancer mais pas terminé
    			if(l.getRelancer()) {
        			nbFactureNonTotalementPayesARelancerSurPeriode=nbFactureNonTotalementPayesARelancerSurPeriode+(int) l.getCount();
    			}
//    			else {
        			nbFactureNonTotalementPayesSurPeriode=nbFactureNonTotalementPayesSurPeriode+(int) l.getCount();
//    			}
    		}    		
    		nbFactureSurPeriode=(int) (nbFactureSurPeriode+l.getCount());	
		}
    	totauxCaPeriodeFacture=new DocumentChiffreAffaireDTO();
    	totauxCaPeriodeFacture.setMtHtCalc(ht);
    	totauxCaPeriodeFacture.setMtTvaCalc(tva);
    	totauxCaPeriodeFacture.setMtTtcCalc(ttc);
    	
    	
  	

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
		
		listeCaMoisDocumentTransforme.clear();
		listeCaMoisDocumentRelance.clear();
		listeCaMoisDocumentNonTransforme.clear();

		for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentTout) {
    		//tous les documents totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeCaMoisDocumentTransforme.add(l);
    		}
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
        		//tous les documents à relancer mais pas terminé
    			if(l.getRelancer()) {
    				listeCaMoisDocumentRelance.add(l);
    			}
//    			else {
    				listeCaMoisDocumentNonTransforme.add(l);
//    			}
    		}    		
		}

		listeDocumentFactureParTiersTous= taFactureService.listLigneArticleDTOTiersAvecEtat(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, null, null, 15);
		
		
		switch (codeEtatFacture) {
		case ETAT_DOCUMENT_AUCUN:
			
			infoLabel = "Cliquer sur un des pavés pour afficher la liste correspondante ; 2 ";
			infoLabel2 = "";
			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);

			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

			initInfosLabelRapportMensuelDataTable("Facture");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
		case ETAT_DOCUMENT_TOUS:
			// Renvoi la liste des documents présents sur la période en cours
			
			for (DocumentDTO l : valueLignesDocumentTotalEtat) {
				//tous les documents sans condition
				valueLignesDocument.add(l);
				totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());
			}
			infoLabel = "Liste des factures réalisées ( reste à règler :"+totalResteARegler+")" ;
			infoLabel2 = "Facturation mensuelle réalisées";
			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentTout) {
				listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
			}
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTous) {
				listeDocumentFactureParTiers.add(l);
			}
			initInfosLabelRapportMensuelDataTable("Facture");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
	    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
//	    			if(l.getRelancer()==false) {
	    				valueLignesDocument.add(l);
	    				totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());

//	    			}
	    		}
			}
	    	
			for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTous) {
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				listeDocumentFactureParTiers.add(l);
	    		}
			}
			
			infoLabel = "Liste des factures non payées ( reste à règler :"+totalResteARegler+")" ;
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			
			for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentNonTransforme) {
				listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
			}
			
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Facture");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
							  CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);

			break;
			
		case ETAT_DOCUMENT_TRANSORME:
			
	    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
	    		//tous les documents totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
	    			valueLignesDocument.add(l);
					totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());

	    		}
			}
	    	
			for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTous) {
				if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
				listeDocumentFactureParTiers.add(l);
				}
			}
	    	
//			valueLignesDocument = taFactureService.findAllDTOPeriodeAvecEtat(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME);
//			valueLignesDocument = taFactureService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des factures Déjà réglé ( reste à règler :"+totalResteARegler+")";
//			valueLignesDocument = taFactureService.findDocumentTransfosDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taFactureService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentTransforme) {
				listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
			}
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Facture");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  		  CSS_DBOARD_BLOC_INACTIF_ALL,
					  		  CSS_DBOARD_BLOC_ACTIF_FACTURE);
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			
	    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
	        		//tous les documents à relancer mais pas terminé
	    			if(l.getRelancer()) {
	    				valueLignesDocument.add(l);
	    				totalResteARegler=totalResteARegler.add(l.getResteAReglerComplet());

	    			}
	    		}
			}
	    	
			for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTous) {
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {				
	        		//tous les documents à relancer mais pas terminé
	    			if(l.getRelancer()) {
		    			listeDocumentFactureParTiers.add(l);
	    			}
				}
			}
			
//			valueLignesDocument = taFactureService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null);
			infoLabel = "Liste des factures à relancer ( reste à règler :"+totalResteARegler+")";
//			valueLignesDocument = taFactureService.findDocumentNonTransfosARelancerDTOParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15,null,Const.PAR_FAMILLE_ARTICLE,"%");
			barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//			listeCaMoisDocument = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,15,null);
			
			for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentRelance) {
				listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
			}
			
			barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
			initInfosLabelRapportMensuelDataTable("Facture");
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  		  CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  		  CSS_DBOARD_BLOC_ACTIF_FACTURE, 
			  		  		  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
			
		default:
			break;
		}
		System.out.println("actRechercheFacture Fin");
    }
	
	///////ACT RECHERCHER COMPARAISON////////////
	public void actRechercheFactureComp(String codeEtatFacture){
		
		if(comparaison) {
//			barChartModelFacture.clear();
	    	setInfoEtatDocument(codeEtatFacture);
	    	setCodeEtatDocument(codeEtatFacture);  
	    	
	    	totauxCaPeriodeFactureTotalementPayeeComp=new DocumentChiffreAffaireDTO();
	    	nbFactureTotalementPayesSurPeriodeComp=0;
	    	totauxCaPeriodeFactureNonTotalementPayeeARelancerComp=new DocumentChiffreAffaireDTO();
	    	nbFactureNonTotalementPayesARelancerSurPeriodeComp=0;
	    	totauxCaPeriodeFactureNonTotalementPayeeComp=new DocumentChiffreAffaireDTO();
	    	nbFactureNonTotalementPayesSurPeriodeComp=0;
	    	totauxCaPeriodeFactureComp=new DocumentChiffreAffaireDTO();
	    	nbFactureSurPeriodeComp=0;
	    	
	    	listeCaMoisDocumentComp.clear();
	    	valueLignesDocumentComp.clear();
	    	listeDocumentFactureParTiersComp.clear();
	    	
	    	//pour essai 
	    	requetesDisponiblesPourMoteur(); 	
	    		    	
	    	listMoisAnneeExoComp = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
	    	
	    	
	    	listeTotauxCaPeriodeFactureCompAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null,"%");
			valueLignesDocumentTotalCompEtat=taFactureService.findAllDTOPeriodeAvecEtat(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp),null,"%");
			nbFactureSurPeriodeCompAvecEtat=taFactureService.countDocumentAvecEtat(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, "%");

			
	    	BigDecimal ht=BigDecimal.ZERO;
	    	BigDecimal tva=BigDecimal.ZERO;
	    	BigDecimal ttc=BigDecimal.ZERO;
	    	
	    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
	    		//tous les documents totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
	    			totauxCaPeriodeFactureTotalementPayeeComp.setMtHtCalc(totauxCaPeriodeFactureTotalementPayeeComp.getMtHtCalc().add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	    			totauxCaPeriodeFactureTotalementPayeeComp.setMtTvaCalc(totauxCaPeriodeFactureTotalementPayeeComp.getMtTvaCalc().add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	    			totauxCaPeriodeFactureTotalementPayeeComp.setMtTtcCalc(totauxCaPeriodeFactureTotalementPayeeComp.getMtTtcCalc().add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	    			totauxCaPeriodeFactureTotalementPayeeComp.setReglementComplet(totauxCaPeriodeFactureTotalementPayeeComp.getReglementComplet().add(l.getReglementComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
	    			totauxCaPeriodeFactureTotalementPayeeComp.setResteAReglerComplet(totauxCaPeriodeFactureTotalementPayeeComp.getResteAReglerComplet().add(l.getResteAReglerComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
	    		}
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
	        		//tous les documents à relancer mais pas terminé
	    			if(l.getRelancer()) {
	        			totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.setMtHtCalc(totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.getMtHtCalc().add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.setMtTvaCalc(totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.getMtTvaCalc().add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.setMtTtcCalc(totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.getMtTtcCalc().add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.setReglementComplet(totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.getReglementComplet().add(l.getReglementComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.setResteAReglerComplet(totauxCaPeriodeFactureNonTotalementPayeeARelancerComp.getResteAReglerComplet().add(l.getResteAReglerComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
	    			}
//	    			else {
	        			totauxCaPeriodeFactureNonTotalementPayeeComp.setMtHtCalc(totauxCaPeriodeFactureNonTotalementPayeeComp.getMtHtCalc().add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeComp.setMtTvaCalc(totauxCaPeriodeFactureNonTotalementPayeeComp.getMtTvaCalc().add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeComp.setMtTtcCalc(totauxCaPeriodeFactureNonTotalementPayeeComp.getMtTtcCalc().add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeComp.setReglementComplet(totauxCaPeriodeFactureNonTotalementPayeeComp.getReglementComplet().add(l.getReglementComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
	        			totauxCaPeriodeFactureNonTotalementPayeeComp.setResteAReglerComplet(totauxCaPeriodeFactureNonTotalementPayeeComp.getResteAReglerComplet().add(l.getResteAReglerComplet().setScale(2,BigDecimal.ROUND_HALF_UP)));
//	    			}
	    		}
	    		
	    		ht=ht.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
	    		tva=tva.add(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
	    		ttc=ttc.add(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			}
	    	
	    	for (DocumentChiffreAffaireDTO l : nbFactureSurPeriodeCompAvecEtat) {
	    		//tous les documents totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
	    			nbFactureTotalementPayesSurPeriodeComp=nbFactureTotalementPayesSurPeriodeComp+(int) l.getCount();
	    		}
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
	        		//tous les documents à relancer mais pas terminé
	    			if(l.getRelancer()) {
	        			nbFactureNonTotalementPayesARelancerSurPeriodeComp=nbFactureNonTotalementPayesARelancerSurPeriodeComp+(int) l.getCount();
	    			}
//	    			else {
	        			nbFactureNonTotalementPayesSurPeriodeComp=nbFactureNonTotalementPayesSurPeriodeComp+(int) l.getCount();
//	    			}
	    		}    		
	    		nbFactureSurPeriodeComp=(int) (nbFactureSurPeriodeComp+l.getCount());	
			}
	    	totauxCaPeriodeFactureComp=new DocumentChiffreAffaireDTO();
	    	totauxCaPeriodeFactureComp.setMtHtCalc(ht);
	    	totauxCaPeriodeFactureComp.setMtTvaCalc(tva);
	    	totauxCaPeriodeFactureComp.setMtTtcCalc(ttc);


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
			
			
			listeCaMoisDocumentCompTout = taFactureService.listeChiffreAffaireTotalJmaAvecEtatDTO(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), 1,null,"%");
			listeCaMoisDocumentTransformeComp.clear();
			listeCaMoisDocumentRelanceComp.clear();
			listeCaMoisDocumentNonTransformeComp.clear();

			for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentCompTout) {
	    		//tous les documents totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
	    			listeCaMoisDocumentTransformeComp.add(l);
	    		}
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
	        		//tous les documents à relancer mais pas terminé
	    			if(l.getRelancer()) {
	    				listeCaMoisDocumentRelanceComp.add(l);
	    			}
//	    			else {
	    				listeCaMoisDocumentNonTransformeComp.add(l);
//	    			}
	    		}    		
			}
			listeDocumentFactureParTiersTousComp= taFactureService.listLigneArticleDTOTiersAvecEtat(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, null, 15);
			
			
			listeCaMoisDocumentTout = taFactureService.listeChiffreAffaireTotalJmaAvecEtatDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null,"%");
			for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentTout) {
	    		//tous les documents totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
	    			listeCaMoisDocumentTransforme.add(l);
	    		}
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
	    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
	        		//tous les documents à relancer mais pas terminé
	    			if(l.getRelancer()) {
	    				listeCaMoisDocumentRelance.add(l);
	    			}
//	    			else {
	    				listeCaMoisDocumentNonTransforme.add(l);
//	    			}
	    		}    		
			}

			

			
			switch (codeEtatFacture) {
			case ETAT_DOCUMENT_TOUS:
				// Renvoi la liste des documents présents sur la période en cours

				
				for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
					//tous les documents sans condition
					valueLignesDocumentComp.add(l);
				}
				
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentCompTout) {
					listeCaMoisDocumentComp=rechercheMemeDateDansListe(l, listeCaMoisDocumentComp);
				}
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentTout) {
					listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
				}
				lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				
				lineChartModelFacture = addLineSeriesMensuelComparaison(lineChartModelFacture,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTousComp) {
					listeDocumentFactureParTiersComp.add(l);
				}
//				listeDocumentFactureParTiersComp = taFactureService.listLigneArticleDTOTiersParTypeEtatDoc(LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp), null, null, null, 0);
				initInfosLabelRapportMensuelDataTable("Facture");
				// Initialisent les classes css pour les block d'information
				initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
								  CSS_DBOARD_BLOC_INACTIF_ALL_COMP);
				break;

			case ETAT_DOCUMENT_NON_TRANSORME:
				
		    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
		    		//tous les documents pas payé ou pas totalement payé
		    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
		    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
		    				valueLignesDocumentComp.add(l);
		    		}
				}
				lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentNonTransformeComp) {
					listeCaMoisDocumentComp=rechercheMemeDateDansListe(l, listeCaMoisDocumentComp);
				}
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentNonTransforme) {
					listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
				}
				lineChartModelFacture = addLineSeriesMensuelComparaison(lineChartModelFacture,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTousComp) {
		    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
		    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
					listeDocumentFactureParTiersComp.add(l);
		    		}
				}

				initInfosLabelRapportMensuelDataTable("Facture");
				initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
						  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP, 
						  		  CSS_DBOARD_BLOC_INACTIF_ALL_COMP,
						  		CSS_DBOARD_BLOC_ACTIF_FACTURE_COMP);
				break;
				
			case ETAT_DOCUMENT_TRANSORME:
				
		    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
		    		//tous les documents totalement payé
		    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
		    			valueLignesDocumentComp.add(l);
		    		}
				}
		 		lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentTransformeComp) {
					listeCaMoisDocumentComp=rechercheMemeDateDansListe(l, listeCaMoisDocumentComp);
				}
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentTransforme) {
					listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
				}

				for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTousComp) {
					if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
					listeDocumentFactureParTiersComp.add(l);
					}
				}

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
				
		    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
		    		//tous les documents pas payé ou pas totalement payé
		    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
		    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
		        		//tous les documents à relancer mais pas terminé
		    			if(l.getRelancer()) {
		    				valueLignesDocumentComp.add(l);
		    			}
		    		}
				}		    	
				lineChartModelFacture = createLineChartModelSimpleBarre("Totaux HT mensuel", true, -50);
				
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentRelanceComp) {
					listeCaMoisDocumentComp=rechercheMemeDateDansListe(l, listeCaMoisDocumentComp);
				}
				for (DocumentChiffreAffaireDTO l : listeCaMoisDocumentRelance) {
					listeCaMoisDocument=rechercheMemeDateDansListe(l, listeCaMoisDocument);
				}
				lineChartModelFacture = addLineSeriesMensuelComparaison(lineChartModelFacture,
						customFormatDate(LibDate.localDateToDate(dateDebut))+" au "+customFormatDate(LibDate.localDateToDate(dateFin)),
						customFormatDate(LibDate.localDateToDate(dateDebutComp))+" au "+customFormatDate(LibDate.localDateToDate(dateFinComp)),
						listeCaMoisDocument, listeCaMoisDocumentComp, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), LibDate.localDateToDate(dateDebutComp), LibDate.localDateToDate(dateFinComp));
				
				
				for (DocumentChiffreAffaireDTO l : listeDocumentFactureParTiersTousComp) {
		    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
		    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {				
		        		//tous les documents à relancer mais pas terminé
		    			if(l.getRelancer()) {
		    				listeDocumentFactureParTiersComp.add(l);
		    			}
					}
				}
				
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
  
    		montantTotalHtMois=new BigDecimal(0);
    		montantTotalHtMoisComp=new BigDecimal(0);
			
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
			
    		listeCaJoursDocumentCompTout = taFactureService.listeChiffreAffaireTotalJmaAvecEtatDTO(premierJourMoisComp, dernierJourMoisComp, 2,null,"%");
    		listeDocumentArticleParTiersParMoisCompTous= taFactureService.findArticlesParTiersParMoisAvecEtat(premierJourMoisComp, dernierJourMoisComp,null,"%",15);
		    listeTotauxCaPeriodeFactureCompAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(premierJourMoisComp, dernierJourMoisComp,null,"%");
    		valueLignesDocumentTotalCompEtat=taFactureService.findAllDTOPeriodeAvecEtat(premierJourMoisComp, dernierJourMoisComp,null,"%");
    		
    	    listeTotauxCaPeriodeFactureAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(premierJourMois, dernierJourMois,null,"%");
    		valueLignesDocumentTotalEtat=taFactureService.findAllDTOPeriodeAvecEtat(premierJourMois, dernierJourMois,null,"%");
    		listeCaJoursDocumentTout = taFactureService.listeChiffreAffaireTotalJmaAvecEtatDTO(premierJourMois, dernierJourMois, 2,null,"%");
    		listeDocumentArticleParTiersParMoisTous= taFactureService.findArticlesParTiersParMoisAvecEtat(premierJourMois, dernierJourMois,null,"%",15);
			
			
			switch (infoEtatDocument) {
			
				case ETAT_DOCUMENT_TOUS:
										
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
							//tous les documents
							montantTotalHtMoisComp=(montantTotalHtMoisComp.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));    		
						}
						for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
							listeDocumentDetailMoisComp.add(l);
						}

						for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
							listeDocumentArticleParTiersParMoisComp.add(l);
						}

						for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentCompTout) {
							listeCaJoursDocumentComp=rechercheMemeDateDansListe(l, listeCaJoursDocumentComp);
//							listeCaJoursDocumentComp.add(l);
						}						
					}
					
					for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
						//tous les documents
						montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));    		
					}
					for (DocumentDTO l : valueLignesDocumentTotalEtat) {
						listeDocumentDetailMois.add(l);
					}

					for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
						listeDocumentArticleParTiersParMois.add(l);
					}

					for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
						listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//						listeCaJoursDocument.add(l);
					}
					
					
				initInfosLabelRapportMensuelDataTable("Factures");

					lineChartModelFactureJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelFactureJour = addLineSeriesJoursComparaison(lineChartModelFactureJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
//					barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					break;
					
					
				case ETAT_DOCUMENT_NON_TRANSORME:
					
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
				    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				    			montantTotalHtMoisComp=(montantTotalHtMoisComp.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
				    		}
						}
				    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				    			listeDocumentDetailMoisComp.add(l);
				    		}
						}
				    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				    			listeDocumentArticleParTiersParMoisComp.add(l);
				    		}
						}
				    	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentCompTout) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
								listeCaJoursDocumentComp=rechercheMemeDateDansListe(l, listeCaJoursDocumentComp);
//								listeCaJoursDocumentComp.add(l);
				    		}
						}
						
						}					
					
			    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
			    		}
					}
			    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			listeDocumentDetailMois.add(l);
			    		}
					}
			    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			listeDocumentArticleParTiersParMois.add(l);
			    		}
					}
			    	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//			    			listeCaJoursDocument.add(l);
			    		}
					}
					initInfosLabelRapportMensuelDataTable("Facture");

					lineChartModelFactureJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelFactureJour = addLineSeriesJoursComparaison(lineChartModelFactureJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
					
//					barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
				
					break;
					
				case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
					
					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
				    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				    			if(l.getRelancer()) {
				    			montantTotalHtMoisComp=(montantTotalHtMoisComp.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
				    			}
				    		}
						}
				    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				    			if(l.getRelancer()) {
				    			listeDocumentDetailMoisComp.add(l);
				    			}
				    		}
						}
				    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				    			if(l.getRelancer()) {
				    			listeDocumentArticleParTiersParMoisComp.add(l);
				    			}
				    		}
						}
				    	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentCompTout) {
				    		//tous les documents pas payé ou pas totalement payé
				    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
				    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				    			if(l.getRelancer()) {
									listeCaJoursDocumentComp=rechercheMemeDateDansListe(l, listeCaJoursDocumentComp);
//									listeCaJoursDocumentComp.add(l);
				    			}
				    		}
						}						
						}
					
			    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			if(l.getRelancer()) {
			    			montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
			    			}
			    		}
					}
			    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			if(l.getRelancer()) {
			    			listeDocumentDetailMois.add(l);
			    			}
			    		}
					}
			    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			if(l.getRelancer()) {
			    			listeDocumentArticleParTiersParMois.add(l);
			    			}
			    		}
					}
			    	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
			    		//tous les documents pas payé ou pas totalement payé
			    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
			    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
			    			if(l.getRelancer()) {
			    				listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//			    				listeCaJoursDocument.add(l);
			    			}
			    		}
					}		    	
										
					initInfosLabelRapportMensuelDataTable("Facture");

					lineChartModelFactureJour = createLineChartModelSimpleBarre("Totaux HT par jour", true, -50);
					lineChartModelFactureJour = addLineSeriesJoursComparaison(lineChartModelFactureJour,
							customFormatDate(premierJourMois)+" au "+customFormatDate(dernierJourMois),
							customFormatDate(premierJourMoisComp)+" au "+customFormatDate(dernierJourMoisComp),
							listeCaJoursDocument, listeCaJoursDocumentComp, premierJourMois, dernierJourMois, premierJourMoisComp, dernierJourMoisComp);
//					barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
//					barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
					break;
					
				case ETAT_DOCUMENT_TRANSORME:
					

					if(premierJourMoisComp != null && dernierJourMoisComp != null) {//si ces dates sont nulles c'est que l'on est sur mois "non comparé" (sans année)
						for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
							//tous les documents pas payé ou pas totalement payé
							//tous les documents totalement payé
							if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
								montantTotalHtMoisComp=(montantTotalHtMoisComp.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));

							}
						}
						for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
							//tous les documents pas payé ou pas totalement payé
							if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
								listeDocumentDetailMoisComp.add(l);
							}
						}
						for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
							//tous les documents pas payé ou pas totalement payé
							if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
								listeDocumentArticleParTiersParMoisComp.add(l);
							}
						}
						for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentCompTout) {
							//tous les documents pas payé ou pas totalement payé
							if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
								listeCaJoursDocumentComp=rechercheMemeDateDansListe(l, listeCaJoursDocumentComp);
//								listeCaJoursDocumentComp.add(l);
							}
						}
						}
					
					for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
						//tous les documents pas payé ou pas totalement payé
						//tous les documents totalement payé
						if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
							montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));

						}
					}
					for (DocumentDTO l : valueLignesDocumentTotalEtat) {
						//tous les documents pas payé ou pas totalement payé
						if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
							listeDocumentDetailMois.add(l);
						}
					}
					for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
						//tous les documents pas payé ou pas totalement payé
						if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
							listeDocumentArticleParTiersParMois.add(l);
						}
					}
					for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
						//tous les documents pas payé ou pas totalement payé
						if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
							listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//							listeCaJoursDocument.add(l);
						}
					}

					
					initInfosLabelRapportMensuelDataTable("Facture");
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
		
		
//rajouté par isa le 30/07/2021 avant départ en vacances à reprendre dès retour		
	    listeTotauxCaPeriodeFactureAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(premierJourMois, dernierJourMois,null,"%");
	    		valueLignesDocumentTotalEtat=taFactureService.findAllDTOPeriodeAvecEtat(premierJourMois, dernierJourMois,null,"%");
//	    		listeCaMoisDocumentTout = taFactureService.listeChiffreAffaireTotalJmaAvecEtatDTO(premierJourMois, dernierJourMois, 1,null,"%");
	    		listeCaJoursDocumentTout = taFactureService.listeChiffreAffaireTotalJmaAvecEtatDTO(premierJourMois, dernierJourMois, 2,null,"%");
	    		listeDocumentArticleParTiersParMoisTous= taFactureService.findArticlesParTiersParMoisAvecEtat(premierJourMois, dernierJourMois,null,"%",15);
	    		
	    		listeDocumentDetailMois.clear();
	    		listeDocumentArticleParTiersParMois.clear();
	    		listeCaJoursDocument.clear();
	    		montantTotalHtMois=new BigDecimal(0);
	    		
	    		

	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par mois
		
    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
    		//tous les documents
    			montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));    		
		}
//	montantTotalHtMois = taFactureService.chiffreAffaireTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
	// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
			listeDocumentDetailMois.add(l);
	}
//	listeDocumentDetailMois= taFactureService.findAllDTOPeriode(premierJourMois, dernierJourMois,null);
	
	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
			listeDocumentArticleParTiersParMois.add(l);
	}
//	listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);

	
	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
		listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//			listeCaJoursDocument.add(l);
	}
//	listeCaJoursDocument = taFactureService.listeChiffreAffaireTotalJmaDTO(premierJourMois, dernierJourMois, 2,null);
	initInfosLabelRapportMensuelDataTable("Facture");

	
	barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
	barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par mois
		
    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    		}
		}
//		montantTotalHtMois = taFactureService.chiffreAffaireNonTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard

    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentDetailMois.add(l);
    		}
		}
//		listeDocumentDetailMois= taFactureService.findDocumentNonTransfosDTO(premierJourMois, dernierJourMois,null);
    	
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentArticleParTiersParMois.add(l);
    		}
		}
//		listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersNonTransforme(premierJourMois, dernierJourMois);
    	
    	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//    			listeCaJoursDocument.add(l);
    		}
		}
		initInfosLabelRapportMensuelDataTable("Facture");

//		listeCaJoursDocument = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
		barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
	
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par moi
    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			if(l.getRelancer()) {
    			montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    			}
    		}
		}
//		montantTotalHtMois = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(premierJourMois, dernierJourMois, 15,null).getMtHtCalc(); 
		initInfosLabelRapportMensuelDataTable("Facture");
		// on récupére danslisteDocumentDetailMois la liste des document non transforlé à relancer sur la période et racorder au data table dans la vue du dashboard
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			if(l.getRelancer()) {
    			listeDocumentDetailMois.add(l);
    			}
    		}
		}
//		listeDocumentDetailMois= taFactureService.findDocumentNonTransfosARelancerDTO(premierJourMois, dernierJourMois, 15,null);
    	
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			if(l.getRelancer()) {
    			listeDocumentArticleParTiersParMois.add(l);
    			}
    		}
		}
//    	listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersNonTransformeARelancer(premierJourMois, dernierJourMois, 15);
		
    	
    	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
    				||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			if(l.getRelancer()) {
    				listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//    				listeCaJoursDocument.add(l);
    			}
    		}
		}
//    	listeCaJoursDocument = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(premierJourMois, dernierJourMois, 2, 15,null);
		barChartModelFactureJour = createBarChartModelSimpleBarre("Totaux HT par jour", true, -50);
		barChartModelFactureJour.addSeries(createBarChartSerieJour("Jours", listeCaJoursDocument, premierJourMois, dernierJourMois));
		
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par mois
	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
		//tous les documents pas payé ou pas totalement payé
		//tous les documents totalement payé
		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
			montantTotalHtMois=(montantTotalHtMois.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
			
		}
	}
//		montantTotalHtMois = taFactureService.chiffreAffaireTransformeTotalDTO(premierJourMois, dernierJourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailMois la liste des document sur la période et racorder au data table dans la vue du dashboard
		
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentDetailMois.add(l);
    		}
		}
//		listeDocumentDetailMois= taFactureService.findDocumentTransfosDTO(premierJourMois, dernierJourMois,null);
    	
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentArticleParTiersParMois.add(l);
    		}
		}
//		listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersTransforme(premierJourMois, dernierJourMois);
    	
    	for (DocumentChiffreAffaireDTO l : listeCaJoursDocumentTout) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeCaJoursDocument=rechercheMemeDateDansListe(l, listeCaJoursDocument);
//    			listeCaJoursDocument.add(l);
    		}
		}
		initInfosLabelRapportMensuelDataTable("Facture");

//		listeCaJoursDocument = taFactureService.listeChiffreAffaireTransformeJmaDTO(premierJourMois, dernierJourMois, 2,null);
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
		
		
	    listeTotauxCaPeriodeFactureAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(jourMois, jourMois,null,"%");
		valueLignesDocumentTotalEtat=taFactureService.findAllDTOPeriodeAvecEtat(jourMois, jourMois,null,"%");
		listeDocumentArticleParTiersParMoisTous= taFactureService.findArticlesParTiersParMoisAvecEtat(jourMois, jourMois,null,"%",15);
		
		montantTotalHtJour=new BigDecimal(0);
		listeDocumentDetailJour.clear();
		listeDocumentArticleParTiersParJour.clear();
		
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
	// Cumul du chiffre d'affaire par jour
		for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
				montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
		}
//	montantTotalHtJour = taFactureService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();

		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
    			listeDocumentDetailJour.add(l);
		}
//	listeDocumentDetailJour= taFactureService.findAllDTOPeriode(jourMois, jourMois,null);

	
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
    			listeDocumentArticleParTiersParJour.add(l);
		}
//	setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersParMois(jourMois, jourMois));
//	setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersParMoisParTypeRegroupement(jourMois, jourMois,Const.PAR_FAMILLE_ARTICLE,"%"));
//	etatInfosLabelRapportMensuelDataTable();
	initInfosLabelRapportMensuelDataTable("Facture");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				//    			if(l.getRelancer()==false) {
				montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
			}
		}
//		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
		
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentDetailJour.add(l);
			}
		}
//		listeDocumentDetailJour= taFactureService.findDocumentNonTransfosDTO(jourMois, jourMois,null);
		
		
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentArticleParTiersParJour.add(l);
			}
		}
//		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransforme(jourMois, jourMois));
//		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransformeParTypeRegroupement(jourMois, jourMois,Const.PAR_FAMILLE_ARTICLE,"%"));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				if(l.getRelancer()==false) {
					montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
				}
			}
		}
//		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(jourMois, jourMois, 15,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				if(l.getRelancer()==false) {
    			listeDocumentDetailJour.add(l);
				}
			}
		}
//		listeDocumentDetailJour= taFactureService.findDocumentNonTransfosARelancerDTO(jourMois, jourMois, 15,null);
    	
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				if(l.getRelancer()==false) {
    			listeDocumentArticleParTiersParJour.add(l);
				}
			}
		}
//		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransformeARelancer(jourMois, jourMois, 15));
//		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersNonTransformeARelancerParTypeRegroupement(jourMois, jourMois, 15,Const.PAR_FAMILLE_ARTICLE,"%"));
//		etatInfosLabelRapportMensuelDataTable();
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
//		cumulJourDetail = findChiffreAffaireTransformeTotalDTODocument(TaProforma.TYPE_DOC, jourMois, jourMois);
//		montantTotalHtJour = cumulJourDetail.get(0).getMtHtCalc();
		for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {			
			montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    		}
		}
//		montantTotalHtJour = taFactureService.chiffreAffaireNonTransformeTotalDTO(jourMois, jourMois,null).getMtHtCalc();
		// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentDetailJour.add(l);
    		}
		}
//		listeDocumentDetailJour= taFactureService.findDocumentTransfosDTO(jourMois, jourMois,null);
		
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentArticleParTiersParJour.add(l);
    		}
		}
//		setListeDocumentArticleParTiersParJour(taFactureService.findArticlesParTiersTransforme(jourMois, jourMois));
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
		
		montantTotalHtJour=new BigDecimal(0);
		listeDocumentDetailJour.clear();
		listeDocumentArticleParTiersParJour.clear();
		montantTotalHtJourComp=new BigDecimal(0);
		listeDocumentDetailJourComp.clear();
		listeDocumentArticleParTiersParJourComp.clear();
		
	    listeTotauxCaPeriodeFactureAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(jourMois, jourMois,null,"%");
		valueLignesDocumentTotalEtat=taFactureService.findAllDTOPeriodeAvecEtat(jourMois, jourMois,null,"%");
		listeDocumentArticleParTiersParMoisTous= taFactureService.findArticlesParTiersParMoisAvecEtat(jourMois, jourMois,null,"%",15);
		
		if(jourMoisComp!=null) {
	    listeTotauxCaPeriodeFactureCompAvecEtat = taFactureService.chiffreAffaireTotalAvecEtatDTO(jourMoisComp, jourMoisComp,null,"%");
	    valueLignesDocumentTotalCompEtat=taFactureService.findAllDTOPeriodeAvecEtat(jourMoisComp, jourMoisComp,null,"%");
		listeDocumentArticleParTiersParMoisCompTous= taFactureService.findArticlesParTiersParMoisAvecEtat(jourMoisComp, jourMoisComp,null,"%",15);
		}
		
	switch (infoEtatDocument) {
	
	case ETAT_DOCUMENT_TOUS:
    	
	if(jourMoisComp!= null) {
    	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
    		montantTotalHtJourComp=(montantTotalHtJourComp.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    	}
    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		listeDocumentDetailJourComp.add(l);
		}
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
    		//tous les documents pas payé ou pas totalement payé
    			listeDocumentArticleParTiersParJourComp.add(l);
		}
	}else {
		montantTotalHtJourComp = BigDecimal.ZERO;
		listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
		listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
	}
	
	
	for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
		montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	}
	//montantTotalHtJour = taFactureService.chiffreAffaireTotalDTO(jourMois, jourMois,null).getMtHtCalc();

	// on récupére danslisteDocumentDetailJour la liste des document sur la période et racorder au data table dans la vue du dashboard
	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
		//tous les documents pas payé ou pas totalement payé
		listeDocumentDetailJour.add(l);
	}
	//listeDocumentDetailJour= taFactureService.findAllDTOPeriode(jourMois, jourMois,null);


	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
		//tous les documents pas payé ou pas totalement payé
		listeDocumentArticleParTiersParJour.add(l);
	}
		
	initInfosLabelRapportMensuelDataTable("Facture");
	break;
	
	case ETAT_DOCUMENT_NON_TRANSORME:
		// Cumul du chiffre d'affaire par Jour
		if(jourMoisComp!= null) {
			for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
				if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
						||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
					//    			if(l.getRelancer()==false) {
					montantTotalHtJourComp=(montantTotalHtJourComp.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
				}
			}
	    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
	    		//tous les documents pas payé ou pas totalement payé
				if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
						||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
					listeDocumentDetailJourComp.add(l);
				}
			}
	    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
	    		//tous les documents pas payé ou pas totalement payé
				if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
						||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
	    			listeDocumentArticleParTiersParJourComp.add(l);
				}
			}
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}

		
		for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				//    			if(l.getRelancer()==false) {
				montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
			}
		}
		
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentDetailJour.add(l);
			}
		}
		
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentArticleParTiersParJour.add(l);
			}
		}
		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
		// Cumul du chiffre d'affaire par Jour
		if(jourMoisComp!= null) {
			for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
				if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
						||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
					if(l.getRelancer()==false) {
						montantTotalHtJourComp=(montantTotalHtJourComp.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
					}
				}
			}
			
	    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
	    		//tous les documents pas payé ou pas totalement payé
				if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
						||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
					if(l.getRelancer()==false) {
						listeDocumentDetailJourComp.add(l);
					}
				}
			}
	    	
	    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
	    		//tous les documents pas payé ou pas totalement payé
				if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
						||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
					if(l.getRelancer()==false) {
	    			listeDocumentArticleParTiersParJourComp.add(l);
					}
				}
			}
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}

		
		for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				if(l.getRelancer()==false) {
					montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
				}
			}
		}
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				if(l.getRelancer()==false) {
    			listeDocumentDetailJour.add(l);
				}
			}
		}
    	
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
			if(l.getIdentifiant().equals(TaEtat.DOCUMENT_ENCOURS)||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_TRANSFORME)
					||l.getIdentifiant().equals(TaEtat.DOCUMENT_PARTIELLEMENT_ABANDONNE)) {
				if(l.getRelancer()==false) {
    			listeDocumentArticleParTiersParJour.add(l);
				}
			}
		}

		initInfosLabelRapportMensuelDataTable("Facture");
		break;
		
	case ETAT_DOCUMENT_TRANSORME:
		
		
		// Cumul du chiffre d'affaire par Jour
		if(jourMoisComp!= null) {
			for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureCompAvecEtat) {
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {			
	    			montantTotalHtJourComp=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
	    		}
			}
	    	for (DocumentDTO l : valueLignesDocumentTotalCompEtat) {
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
	    			listeDocumentDetailJourComp.add(l);
	    		}
			}
			
	    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisCompTous) {
	    		//tous les documents pas payé ou pas totalement payé
	    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
	    			listeDocumentArticleParTiersParJourComp.add(l);
	    		}
			}	    	
		}else {
			montantTotalHtJourComp = BigDecimal.ZERO;
			listeDocumentDetailJourComp = new ArrayList<DocumentDTO>();
			listeDocumentArticleParTiersParJourComp = new ArrayList<TaArticlesParTiersDTO>();
		}
		
		for (DocumentChiffreAffaireDTO l : listeTotauxCaPeriodeFactureAvecEtat) {
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {			
			montantTotalHtJour=(montantTotalHtJour.add(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP)));
    		}
		}
    	for (DocumentDTO l : valueLignesDocumentTotalEtat) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentDetailJour.add(l);
    		}
		}
		
    	for (TaArticlesParTiersDTO l : listeDocumentArticleParTiersParMoisTous) {
    		//tous les documents pas payé ou pas totalement payé
    		if(l.getIdentifiant().equals(TaEtat.DOCUMENT_TOTALEMENT_TRANSFORME)||l.getIdentifiant().equals(TaEtat.DOCUMENT_TERMINE_PARTIELLEMENT_ABANDONNE)) {
    			listeDocumentArticleParTiersParJour.add(l);
    		}
		}
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


	public BigDecimal getTotalResteARegler() {
		return totalResteARegler;
	}


	public void setTotalResteARegler(BigDecimal totalResteARegler) {
		this.totalResteARegler = totalResteARegler;
	}


public boolean afficherListe() {	
	return codeEtatDocument!=ETAT_DOCUMENT_AUCUN;
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
