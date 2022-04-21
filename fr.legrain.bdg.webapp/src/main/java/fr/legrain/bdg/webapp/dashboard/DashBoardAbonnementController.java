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
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
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
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaAbonnement;
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
public class DashBoardAbonnementController/*<MoisExoCourant>*/ extends DashBoardDocumentController implements Serializable {

	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2461636944048269235L;
	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaEditionServiceRemote taEditionService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	
	private @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	

    private BarChartModel barChartModelAbonnementJour; //AJOUTER LE 06/09/2017 Jean Marc
    private LineChartModel lineChartModelAbonnementJour;
    
    private BarChartModel barChartModelAbonnement;
    private BarChartModel barChartModelAbonnementComp;
    private LineChartModel lineChartModelAbonnement;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAbonnement;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAbonnementNonTotalementPayes;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeAbonnementNonTotalementPayesARelancer;
    
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementAnnule;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementSuspendu;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementEnCours;
    
    private List<TaAbonnementDTO> valueLignesAbonnement;
    
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnement;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayee;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayeeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementTotalementPayee;
    //rajout comparaison
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayeeComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayeeARelancerComp;
    private DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementTotalementPayeeComp;
    
    private int nbAbonnementAnnuleSurPeriode = 0;
    private int nbAbonnementSuspenduSurPeriode = 0;
    private int nbAbonnementEnCoursSurPeriode = 0;
    
    private int nbAbonnementSurPeriode = 0;
    private int nbAbonnementNonTotalementPayesSurPeriode = 0 ;
    private int nbAbonnementTotalementPayesSurPeriode = 0;
    private int nbAbonnementNonTotalementPayesARelancerSurPeriode = 0;
    
    private List<String> listeGraphiqueComparatifCADate = new ArrayList<String>();
    private List<BigDecimal> listeGraphiqueComparatifCAMontant1 = new ArrayList<BigDecimal>();
    private List<BigDecimal> listeGraphiqueComparatifCAMontant2 = new ArrayList<BigDecimal>();
    
    //rajout comparaison
    private int nbAbonnementSurPeriodeComp = 0;
    private int nbAbonnementNonTotalementPayesSurPeriodeComp = 0 ;
    private int nbAbonnementTotalementPayesSurPeriodeComp = 0;
    private int nbAbonnementNonTotalementPayesARelancerSurPeriodeComp = 0;
    
    
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
    
    
    private TaEditionDTO editionDefautImpression = null;
    
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
		setTaDocumentService(taAbonnementService);
    	codeEtatDocument = ETAT_ABONNEMENT_EN_COURS;
    	//listeEdition = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaAbonnement.TYPE_DOC.toUpperCase());

//		for (TaEditionDTO ed : listeEdition) {
//			for (TaActionEditionDTO actionDefaut : ed.getTaActionEditionDTO()) {
//				if(actionDefaut.getCodeAction().equals(TaActionEdition.code_impression)) {
//					editionDefautImpression = ed;
//				}
//			}
//		}
//		if(editionDefautImpression == null) {
//			TaEditionMapper editionMapper = new TaEditionMapper();
//			
//			TaEdition editionDefautProgramme = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_FACTURE_DEFAUT).get(0);
//			editionDefautImpression = editionMapper.mapEntityToDto(editionDefautProgramme, editionDefautImpression);
//		}
    	//Initialisation des données sur la période exercice
    	actRechercheAbonnement(codeEtatDocument);
    }
    
    
    public void imprimeEdition(int idDocument, TaEditionDTO edition, String modeEdition, String pourClient) {
		//ICI COMMENCE LA PARTIE actDialogImprimer
    	TaAbonnement abonnement = new TaAbonnement();
		try {
			abonnement = taAbonnementService.findById(idDocument);
		} catch (FinderException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		 Map<String,Object> options = new HashMap<String, Object>();
	       
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        params.put("modeEcranDefaut", list);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametreEdition = null;
			
	
				
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("abonnement");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", abonnement.getGestionLot());
			  

			if(!editionClient) {
				switch (modeEdition) {
				case "BROUILLON":
					if(abonnement.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "BROUILLONAPAYER");
					} else {
						mapParametreEdition.put("mode", "BROUILLON");
					}
					break;
				case "DUPLICATA":
					if(abonnement.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "DUPLICATAPAYER");
					} else {
						mapParametreEdition.put("mode", "DUPLICATA");
					}
					break;
				case "PAYER":
					if(abonnement.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "PAYER");
					} else {
						mapParametreEdition.put("mode", "PAYER");
					}
					break;
	
				default:
					break;
				}
			} else {
				if(abonnement.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
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
						

						String pdf = taAbonnementService.generePDF(abonnement.getIdDocument(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
						PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");
//						masterEntity = taAbonnementService.findById(masterEntity.getIdDocument());
//						updateTab();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			//ICI FINI LA PARTIE handleReturnDialogImprimer
			
	}
    
    
    


    public void refreshDashBoard(String etatAbonnement){
    	initPeriode();
    	setAfficheDocumentJour(false); //AJOUTER LE 06/09/2017 Jean Marc
    	setAfficheDocumentJourDataTable(false); //AJOUTER LE 06/09/2017 Jean Marc
    	listeDocumentDetailMois.clear(); //AJOUTER LE 06/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentDetailJour.clear(); //AJOUTER LE 19/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParMois.clear(); //AJOUTER LE 14/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParJour.clear(); //AJOUTER LE 20/09/2017 Jean Marc vide la table à chaque changement de proforma
    	//listeDocumentAbonnementParTiers.clear();/**RAJOUT YANN**/
    	listeDocumentDetailMoisComp.clear(); 
    	listeDocumentDetailJourComp.clear(); 
    	listeDocumentArticleParTiersParMoisComp.clear(); 
    	listeDocumentArticleParTiersParJourComp.clear(); 
    //	listeDocumentAbonnementParTiersComp.clear();
    	actRechercheAbonnement(etatAbonnement);
    }


	public void actRechercheAbonnement(String codeEtatAbonnement){
    	setInfoEtatDocument(codeEtatAbonnement);
    	setCodeEtatDocument(codeEtatAbonnement);	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    	
    	totauxCaPeriodeAbonnementAnnule = taAbonnementService.chiffreAffaireTotalAnnuleDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	nbAbonnementAnnuleSurPeriode = (int) taAbonnementService.countDocumentAnnule(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	
    	totauxCaPeriodeAbonnementEnCours = taAbonnementService.chiffreAffaireTotalEnCoursDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	nbAbonnementEnCoursSurPeriode = (int) taAbonnementService.countDocumentEnCours(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	
    	totauxCaPeriodeAbonnementSuspendu = taAbonnementService.chiffreAffaireTotalSuspenduDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	nbAbonnementSuspenduSurPeriode = (int) taAbonnementService.countDocumentSuspendu(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	//totauxCaPeriodeAbonnement = taAbonnementService.chiffreAffaireTotalDTO(dateDebut, dateFin,null);
    	
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé sur la période
		//totauxCaPeriodeAbonnementNonTotalementPayee = taAbonnementService.chiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin,null);
		
		// Calcule le CA (HT, TVA, TTC) total des Proforma non transformé à relancer sur la période
		//totauxCaPeriodeAbonnementNonTotalementPayeeARelancer = taAbonnementService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, 15,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		//totauxCaPeriodeAbonnementTotalementPayee = taAbonnementService.chiffreAffaireTransformeTotalDTO(dateDebut, dateFin,null);
		// Retourne le nombre total de documents sur la période
		//nbAbonnementSurPeriode = (int) taAbonnementService.countDocument(dateDebut, dateFin,null);
	// Retourne le nombre de Abonnement non totalement payés sur la période
		//nbAbonnementNonTotalementPayesSurPeriode = (int) taAbonnementService.countDocumentNonTransforme(dateDebut, dateFin,null);
	// Retourne le nombre de Abonnement à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
		//nbAbonnementNonTotalementPayesARelancerSurPeriode = (int) taAbonnementService.countDocumentNonTransformeARelancer(dateDebut, dateFin, 15,null);
	// Retourne le nombre de Abonnementtransformés sur la période
		//nbAbonnementTotalementPayesSurPeriode = (int) taAbonnementService.countDocumentTransforme(dateDebut, dateFin,null);    	

		//tauxTotalementPayesNb = BigDecimal.ZERO;
	   // tauxTotalementPayesCa = BigDecimal.ZERO;
	    
//		if (nbAbonnementSurPeriode > 0) {
//			try {
//				tauxTotalementPayesNb =  (new BigDecimal(nbAbonnementTotalementPayesSurPeriode) .divide(new BigDecimal(nbAbonnementSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//				tauxTotalementPayesCa = totauxCaPeriodeAbonnementTotalementPayee.getMtTtcCalc() .divide(totauxCaPeriodeAbonnement.getMtTtcCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//			} catch (Exception e) {
//				e.printStackTrace();
//				// TODO: handle exception
//			}
//		}
		
		switch (codeEtatAbonnement) {
		
			
		case ETAT_ABONNEMENT_EN_COURS:
			// Renvoi la liste des documents présents sur la période en cours

			valueLignesAbonnement = taAbonnementService.findAllEnCoursDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des abonnements en cours";
			infoLabel2 = "Abonnements mensuel en cours";

			initInfosLabelRapportMensuelDataTable("Abonnement");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
			
		case ETAT_ABONNEMENT_SUSPENDU:
			// Renvoi la liste des documents présents sur la période en cours

			valueLignesAbonnement = taAbonnementService.findAllSuspenduDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des abonnements suspendus";
			infoLabel2 = "Abonnements mensuel suspendus";

			initInfosLabelRapportMensuelDataTable("Abonnement");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
							CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
		case ETAT_ABONNEMENT_ANNULE:
			// Renvoi la liste des documents présents sur la période en cours

			valueLignesAbonnement = taAbonnementService.findAllAnnuleDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des abonnements annulés";
			infoLabel2 = "Abonnements mensuel annulés";
			initInfosLabelRapportMensuelDataTable("Abonnement");
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_ACTIF_FACTURE, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;	
			
		default:
			break;
		}
    	
    }

	
	public void actImprimerListeAbonnement(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", infoLabel);
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeAbonnement", valueLignesAbonnement);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
			
	public BarChartModel getBarChartModelAbonnement() {
		return barChartModelAbonnement;
	}
	public void setBarChartModelAbonnementExo(BarChartModel barChartModelAbonnement) {
		this.barChartModelAbonnement = barChartModelAbonnement;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAbonnement() {
		return listeCaPeriodeAbonnement;
	}

	public void setListeCaPeriodeAbonnement(List<DocumentChiffreAffaireDTO> listeCaPeriodeAbonnement) {
		this.listeCaPeriodeAbonnement = listeCaPeriodeAbonnement;
	}

	public int getNbAbonnementSurPeriode() {
		return nbAbonnementSurPeriode;
	}

	public void setNbAbonnementSurPeriode(int nbAbonnementSurPeriode) {
		this.nbAbonnementSurPeriode = nbAbonnementSurPeriode;
	}

	public int getNbAbonnementNonTotalementPayesSurPeriode() {
		return nbAbonnementNonTotalementPayesSurPeriode;
	}

	public void setNbAbonnementNonTotalementPayesSurPeriode(int nbAbonnementNonPayesSurPeriode) {
		this.nbAbonnementNonTotalementPayesSurPeriode = nbAbonnementNonPayesSurPeriode;
	}


	public int getNbAbonnementNonTotalementPayesARelancerSurPeriode() {
		return nbAbonnementNonTotalementPayesARelancerSurPeriode;
	}


	public void setNbAbonnementNonTotalementPayesARelancerSurPeriode(int nbAbonnementNonPayesARelancerSurPeriode) {
		this.nbAbonnementNonTotalementPayesARelancerSurPeriode = nbAbonnementNonPayesARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeAbonnementNonTotalementPayes() {
		return listeCaPeriodeAbonnementNonTotalementPayes;
	}


	public void setListeCaPeriodeAbonnementNonTotalementPayes(List<DocumentChiffreAffaireDTO> listeCaPeriodeAbonnementNonPayes) {
		this.listeCaPeriodeAbonnementNonTotalementPayes = listeCaPeriodeAbonnementNonPayes;
	}


	public BigDecimal getTauxTotalementPayesCa() {
		return tauxTotalementPayesCa;
	}


	public void setTauxTotalementPayesCa(BigDecimal tauxPayesCa) {
		this.tauxTotalementPayesCa = tauxPayesCa;
	}


	public int getNbAbonnementTotalementPayesSurPeriode() {
		return nbAbonnementTotalementPayesSurPeriode;
	}


	public void setNbAbonnementPayesSurPeriode(int nbAbonnementPayesSurPeriode) {
		this.nbAbonnementTotalementPayesSurPeriode = nbAbonnementPayesSurPeriode;
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


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnement() {
		return totauxCaPeriodeAbonnement;
	}


	public void setTotauxCaPeriodeAbonnement(DocumentChiffreAffaireDTO totauxCaPeriodeAbonnement) {
		this.totauxCaPeriodeAbonnement = totauxCaPeriodeAbonnement;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementNonTotalementPayee() {
		return totauxCaPeriodeAbonnementNonTotalementPayee;
	}


	public void setTotauxCaPeriodeAbonnementNonTotalementPayee(
			DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayee) {
		this.totauxCaPeriodeAbonnementNonTotalementPayee = totauxCaPeriodeAbonnementNonTotalementPayee;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementNonTotalementPayeeARelancer() {
		return totauxCaPeriodeAbonnementNonTotalementPayeeARelancer;
	}


	public void setTotauxCaPeriodeAbonnementNonTotalementPayeeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayeeARelancer) {
		this.totauxCaPeriodeAbonnementNonTotalementPayeeARelancer = totauxCaPeriodeAbonnementNonTotalementPayeeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementTotalementPayee() {
		return totauxCaPeriodeAbonnementTotalementPayee;
	}


	public void setTotauxCaPeriodeAbonnementTotalementPayee(DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementTotalementPayee) {
		this.totauxCaPeriodeAbonnementTotalementPayee = totauxCaPeriodeAbonnementTotalementPayee;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}


	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}


	public BarChartModel getBarChartModelAbonnementJour() {
		return barChartModelAbonnementJour;
	}


	public void setBarChartModelAbonnementJour(BarChartModel barChartModelAbonnementJour) {
		this.barChartModelAbonnementJour = barChartModelAbonnementJour;
	}

	

	public BarChartModel getBarChartModelAbonnementComp() {
		return barChartModelAbonnementComp;
	}

	public void setBarChartModelAbonnementComp(BarChartModel barChartModelAbonnementComp) {
		this.barChartModelAbonnementComp = barChartModelAbonnementComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementComp() {
		return totauxCaPeriodeAbonnementComp;
	}

	public void setTotauxCaPeriodeAbonnementComp(DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementComp) {
		this.totauxCaPeriodeAbonnementComp = totauxCaPeriodeAbonnementComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementNonTotalementPayeeComp() {
		return totauxCaPeriodeAbonnementNonTotalementPayeeComp;
	}

	public void setTotauxCaPeriodeAbonnementNonTotalementPayeeComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayeeComp) {
		this.totauxCaPeriodeAbonnementNonTotalementPayeeComp = totauxCaPeriodeAbonnementNonTotalementPayeeComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementNonTotalementPayeeARelancerComp() {
		return totauxCaPeriodeAbonnementNonTotalementPayeeARelancerComp;
	}

	public void setTotauxCaPeriodeAbonnementNonTotalementPayeeARelancerComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementNonTotalementPayeeARelancerComp) {
		this.totauxCaPeriodeAbonnementNonTotalementPayeeARelancerComp = totauxCaPeriodeAbonnementNonTotalementPayeeARelancerComp;
	}

	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementTotalementPayeeComp() {
		return totauxCaPeriodeAbonnementTotalementPayeeComp;
	}

	public void setTotauxCaPeriodeAbonnementTotalementPayeeComp(
			DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementTotalementPayeeComp) {
		this.totauxCaPeriodeAbonnementTotalementPayeeComp = totauxCaPeriodeAbonnementTotalementPayeeComp;
	}

	public int getNbAbonnementSurPeriodeComp() {
		return nbAbonnementSurPeriodeComp;
	}

	public void setNbAbonnementSurPeriodeComp(int nbAbonnementSurPeriodeComp) {
		this.nbAbonnementSurPeriodeComp = nbAbonnementSurPeriodeComp;
	}

	public int getNbAbonnementNonTotalementPayesSurPeriodeComp() {
		return nbAbonnementNonTotalementPayesSurPeriodeComp;
	}

	public void setNbAbonnementNonTotalementPayesSurPeriodeComp(int nbAbonnementNonTotalementPayesSurPeriodeComp) {
		this.nbAbonnementNonTotalementPayesSurPeriodeComp = nbAbonnementNonTotalementPayesSurPeriodeComp;
	}

	public int getNbAbonnementTotalementPayesSurPeriodeComp() {
		return nbAbonnementTotalementPayesSurPeriodeComp;
	}

	public void setNbAbonnementTotalementPayesSurPeriodeComp(int nbAbonnementTotalementPayesSurPeriodeComp) {
		this.nbAbonnementTotalementPayesSurPeriodeComp = nbAbonnementTotalementPayesSurPeriodeComp;
	}

	public int getNbAbonnementNonTotalementPayesARelancerSurPeriodeComp() {
		return nbAbonnementNonTotalementPayesARelancerSurPeriodeComp;
	}

	public void setNbAbonnementNonTotalementPayesARelancerSurPeriodeComp(
			int nbAbonnementNonTotalementPayesARelancerSurPeriodeComp) {
		this.nbAbonnementNonTotalementPayesARelancerSurPeriodeComp = nbAbonnementNonTotalementPayesARelancerSurPeriodeComp;
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

	public LineChartModel getLineChartModelAbonnement() {
		return lineChartModelAbonnement;
	}

	public void setLineChartModelAbonnement(LineChartModel lineChartModelAbonnement) {
		this.lineChartModelAbonnement = lineChartModelAbonnement;
	}

	public LineChartModel getLineChartModelAbonnementJour() {
		return lineChartModelAbonnementJour;
	}

	public void setLineChartModelAbonnementJour(LineChartModel lineChartModelAbonnementJour) {
		this.lineChartModelAbonnementJour = lineChartModelAbonnementJour;
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


	public int getNbAbonnementAnnuleSurPeriode() {
		return nbAbonnementAnnuleSurPeriode;
	}


	public void setNbAbonnementAnnuleSurPeriode(int nbAbonnementAnnuleSurPeriode) {
		this.nbAbonnementAnnuleSurPeriode = nbAbonnementAnnuleSurPeriode;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementAnnule() {
		return totauxCaPeriodeAbonnementAnnule;
	}


	public void setTotauxCaPeriodeAbonnementAnnule(DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementAnnule) {
		this.totauxCaPeriodeAbonnementAnnule = totauxCaPeriodeAbonnementAnnule;
	}


	public List<TaAbonnementDTO> getValueLignesAbonnement() {
		return valueLignesAbonnement;
	}


	public void setValueLignesAbonnement(List<TaAbonnementDTO> valueLignesAbonnement) {
		this.valueLignesAbonnement = valueLignesAbonnement;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementSuspendu() {
		return totauxCaPeriodeAbonnementSuspendu;
	}


	public void setTotauxCaPeriodeAbonnementSuspendu(DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementSuspendu) {
		this.totauxCaPeriodeAbonnementSuspendu = totauxCaPeriodeAbonnementSuspendu;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodeAbonnementEnCours() {
		return totauxCaPeriodeAbonnementEnCours;
	}


	public void setTotauxCaPeriodeAbonnementEnCours(DocumentChiffreAffaireDTO totauxCaPeriodeAbonnementEnCours) {
		this.totauxCaPeriodeAbonnementEnCours = totauxCaPeriodeAbonnementEnCours;
	}


	public int getNbAbonnementSuspenduSurPeriode() {
		return nbAbonnementSuspenduSurPeriode;
	}


	public void setNbAbonnementSuspenduSurPeriode(int nbAbonnementSuspenduSurPeriode) {
		this.nbAbonnementSuspenduSurPeriode = nbAbonnementSuspenduSurPeriode;
	}


	public int getNbAbonnementEnCoursSurPeriode() {
		return nbAbonnementEnCoursSurPeriode;
	}


	public void setNbAbonnementEnCoursSurPeriode(int nbAbonnementEnCoursSurPeriode) {
		this.nbAbonnementEnCoursSurPeriode = nbAbonnementEnCoursSurPeriode;
	}

}
