package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.PieChartModel;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;

import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.paiement.PaiementCbController;
import fr.legrain.bdg.webapp.paiement.PaiementParam;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;

@Named
@ViewScoped
public class DashBoardParTiersController extends DashBoardDocumentController implements Serializable {


	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8416557110138209660L;

	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taTiersService;

	private TaInfoEntreprise infoEntreprise;
	private String codeTiersCourant;
//	private BigDecimal montantCaHt = BigDecimal.ZERO;
	private Date dateDebutGeneral;
	private Date dateFinGeneral;
    private BarChartModel barChartModelGeneral;
    private String identification="Aucune";
    private boolean inclureCommentaire=false;
    private boolean breakPageEtatFacture=false;
    private boolean breakPageEtatReglement=false;
    private boolean breakPageEtatArticle=false;
    private boolean breakPageEtatDevis=false;
    private boolean breakPageEtatCommande=false;
    private boolean breakPageEtatLivraison=false;
    private boolean breakPageEtatProforma=false;
    
    public boolean isBreakPage() {
		return breakPageEtatFacture;
	}

	public void setBreakPage(boolean breakPage) {
		this.breakPageEtatFacture = breakPage;
	}

	public boolean isInclureCommentaire() {
		return inclureCommentaire;
	}

	public void setInclureCommentaire(boolean inclureCommentaire) {
		this.inclureCommentaire = inclureCommentaire;
	}
	
	private TaTiersDTO selectedTaTiersDTO = new TaTiersDTO();
    
    //////FACTURES GENERAL
    private BigDecimal montantTotalHtFactureGeneral = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoArticleGeneral = BigDecimal.ZERO;
    private int nbFactureGeneral = 0;
    
	///////FACTURES
	private Date dateDebutFacture;
	private Date dateFinFacture;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentFacture;
    private List<DocumentDTO> valueLignesFacture;
    private List<DocumentDTO> valueLignesFactureNonTotalRegle;
    private List<DocumentDTO> valueLignesFactureARelancer;
    private List<DocumentDTO> valueLignesFactureTotalRegle;
    private BarChartModel barChartModelFacture;
    private BigDecimal montantTotalHtFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfoFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonRegleFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancerFacture = BigDecimal.ZERO;
    private int nbFacture = 0;
    private int nbFactureNonTransforme = 0 ;
    private int nbFactureTransforme = 0;
    private int nbFactureNonTransformeARelancer = 0;
    private boolean factureRealise=false;
	private boolean factureNonTotalRegle=false;
	private boolean factureARelancer=false;
	private boolean factureTotalRegle=false;
	private boolean inclureReglementFactReal=false;
	private boolean inclureReglementNonTotReg=false;
	private boolean inclureReglementARelancer=false;
	private boolean inclureReglementTotReg=false;
	
    private String titreListeSyntheseParTiersTous = "Synthèse des ventes (et avoirs) par Tiers";
    private String titreListeSyntheseParTiersArticleTous = "Synthèse des ventes (et avoirs) par Tiers et articles";
//    private String titreListeDetailParTiersTous ="Détail des ventes (et avoirs) par Tiers"; 
//    private String titreEditionListeDetailParTiersTous ="Détail des ventes (et avoirs)par tiers, par articles et par unités"; 
    private String titreEditionListeDetailParTiersArticleTous ="Détail des ventes (et avoirs) par tiers et par articles"; 
    private List<DocumentChiffreAffaireDTO> valueLignesTiersTous;
    private List<DocumentChiffreAffaireDTO> valueLignesTiersArticlesTous;
    private List<DocumentChiffreAffaireDTO> valueLignesTiersTousDetail;
    
    
    ////////PROFORMA
	private Date dateDebutProforma;
	private Date dateFinProforma;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentProforma;
    private List<DocumentDTO> valueLignesProforma;
    private List<DocumentDTO> valueLignesProformaNonTransforme;
	private List<DocumentDTO> valueLignesProformaARelancer;
    private List<DocumentDTO> valueLignesProformaTransforme;
    private BarChartModel barChartModelProforma;
    private BigDecimal montantTotalHtProforma = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfoProforma = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoProforma = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancerProforma = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcProforma = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcTransfoProforma = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoProforma = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoARelancerProforma = BigDecimal.ZERO;
    private int nbProforma = 0;
    private int nbProformaNonTransforme = 0 ;
    private int nbProformaTransforme = 0;
    private int nbProformaNonTransformeARelancer = 0;
    private boolean proformaRealise=false;
	private boolean proformaNonTransforme=false;
	private boolean proformaARelancer=false;
	private boolean proformaTransforme=false;
    
    /////////COMMANDES
    private Date dateDebutCommande;
	private Date dateFinCommande;
	private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentCommande;
    private List<DocumentDTO> valueLignesCommande;
    private List<DocumentDTO> valueLignesCommandeNonTransforme;
    private List<DocumentDTO> valueLignesCommandeARelancer;
    private List<DocumentDTO> valueLignesCommandeTransforme;
    private BarChartModel barChartModelCommande;
    private BigDecimal montantTotalHtCommande = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfoCommande = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoCommande = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancerCommande = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcCommande = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcTransfoCommande = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoCommande = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoARelancerCommande = BigDecimal.ZERO;
    private int nbCommande = 0;
    private int nbCommandeNonTransforme = 0 ;
    private int nbCommandeTransforme = 0;
    private int nbCommandeNonTransformeARelancer = 0;
    private boolean commandeRealise=false;
	private boolean commandeNonTransforme=false;
	private boolean commandeARelancer=false;
	private boolean commandeTransforme=false;
	
	/////////REGLEMENT
	private Date dateDebutReglement;
	private Date dateFinReglement;
	private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentReglement;
	private List<DocumentDTO> valueLignesReglement;
	private List<DocumentDTO> valueLignesReglementNonTransforme;
	private List<DocumentDTO> valueLignesReglementARelancer;
	private List<DocumentDTO> valueLignesReglementTransforme;
	private BarChartModel barChartModelReglement;
	private BigDecimal montantTotalHtReglement = BigDecimal.ZERO;
	private BigDecimal montantTotalHtTransfoReglement = BigDecimal.ZERO;
	private BigDecimal montantTotalHtNonTransfoReglement = BigDecimal.ZERO;
	private BigDecimal montantTotalHtNonTransfoARelancerReglement = BigDecimal.ZERO;
	private BigDecimal montantTotalTtcReglement = BigDecimal.ZERO;
	private BigDecimal montantTotalTtcTransfoReglement = BigDecimal.ZERO;
	private BigDecimal montantTotalTtcNonTransfoReglement = BigDecimal.ZERO;
	private BigDecimal montantTotalTtcNonTransfoARelancerReglement = BigDecimal.ZERO;
	private int nbReglement = 0;
	private int nbReglementNonTransforme = 0 ;
	private int nbReglementTransforme = 0;
	private int nbReglementNonTransformeARelancer = 0;
	private boolean reglementRealise=false;
	private boolean reglementNonTransforme=false;
	private boolean reglementARelancer=false;
	private boolean reglementTransforme=false;
	private boolean inclureFactRegle=false;
	private boolean inclureFactNonTotReg=false;
	private boolean inclureFactTotReg=false;


	//////////DEVIS
	private Date dateDebutDevis;
	private Date dateFinDevis;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentDevis;
    private List<DocumentDTO> valueLignesDevis;
    private List<DocumentDTO> valueLignesDevisNonTransforme;
    private List<DocumentDTO> valueLignesDevisARelancer;
    private List<DocumentDTO> valueLignesDevisTransforme;
	private BarChartModel barChartModelDevis;
    private BigDecimal montantTotalHtDevis = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfoDevis = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoDevis = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancerDevis = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcDevis = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcTransfoDevis = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoDevis = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoARelancerDevis = BigDecimal.ZERO;
    private int nbDevis = 0;
    private int nbDevisNonTransforme = 0 ;
    private int nbDevisTransforme = 0;
    private int nbDevisNonTransformeARelancer = 0;
    private boolean devisRealise=false;
	private boolean devisNonTransforme=false;
	private boolean devisARelancer=false;
	private boolean devisTransforme=false;
    
    //////////LIVRAISON
	private Date dateDebutLivraison;
	private Date dateFinLivraison;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentLivraison;
    private List<DocumentDTO> valueLignesLivraison;
    private List<DocumentDTO> valueLignesLivraisonNonFacture;
    private List<DocumentDTO> valueLignesLivraisonFacture;
	private BarChartModel barChartModelLivraison;
    private BigDecimal montantTotalHtLivraison = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfoLivraison = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoLivraison = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancerLivraison = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcLivraison = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcTransfoLivraison = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoLivraison = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoARelancerLivraison = BigDecimal.ZERO;
    private int nbLivraison = 0;
    private int nbLivraisonNonTransforme = 0 ;
    private int nbLivraisonTransforme = 0;
    private int nbLivraisonNonTransformeARelancer = 0;
    private boolean livraisonRealise=false;
	private boolean livraisonNonFacture=false;
   	private boolean livraisonFacture=false;
    
	//////////ARTICLE
	private Date dateDebutArticle;
	private Date dateFinArticle;
	private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentArticle;
	private List<DocumentChiffreAffaireDTO> listeCaFamilleDocumentArticle;
	private List<TaArticlesParTiersDTO> valueLignesArticle;
	private List<TaArticlesParTiersDTO> valueLignesArticleRegroupee;
	private List<TaArticlesParTiersDTO> valueLignesArticleRegroupeeParFamille;
	private String titreTabListeRegroupeeParArticle = "Liste regroupée par articles";
	private String titreTabListeRegroupeeParFamille = "Articles regroupés par famille";
	
	/**BARCHART**/
	private BarChartModel barChartModelArticle;
	private HorizontalBarChartModel barChartModelArticleHoriz;
	private HorizontalBarChartModel barChartModelArticleParArticleHoriz;
	private String styleHeightChart = "height:300px";
	private String styleHeightChartFamille = "height:300px";
	private boolean afficheGraphFamille = false;
	private boolean afficheGraphParArticle = true;


	private PieChartModel pieChartModelArticle;
	private BigDecimal montantTotalHtArticle = BigDecimal.ZERO;
	private BigDecimal montantTotalHtTransfoArticle = BigDecimal.ZERO;
	private BigDecimal montantTotalHtNonTransfoArticle = BigDecimal.ZERO;
//	private BigDecimal montantTotalHtNonTransfoARelancerDevis = BigDecimal.ZERO;
	private int nbArticle = 0;
	private int nbArticleNonTransforme = 0 ;
	private int nbArticleTransforme = 0;
//	private int nbDevisNonTransformeARelancer = 0;
	private boolean regroupeArticle=false;
	private boolean histoArticle=false;
	private boolean familleArticle=false;
	
	
    
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelTiers;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
    
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatTiers;
    private List<TaTiersDTO> valueLignesTiers;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeTiers;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeTiersNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeTiersNonTransformeARelancer;
    private int nbTiersActif = 0;
    private int nbTiersNonTransformeSurPeriode = 0 ;
    private int nbTiersTransformeSurPeriode = 0;
    private int nbTiersNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    private String cSbNbTiersSurPeriode;
    private String cSbNbTiersTransformeSurPeriode;
    private String cSbNbTiersNonTransformeSurPeriode;
    private String cSbNbTiersNonTransformeARelancerSurPeriode;
    private String infosLabelDoc;
    private List valeursListe=null;
    private Date valeursDateDebut;
	private Date valeursDateFin;
	protected org.primefaces.component.tabview.Tab tabActifDashboardTiers;

    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
		
    	dateDebutGeneral = LibDate.localDateToDate(dateDebut);
    	dateFinGeneral = LibDate.localDateToDate(dateFin);
		barChartModelGeneral = createBarChartModelSimpleBarreVide("N/A", true, -50);

		dateDebutProforma = LibDate.localDateToDate(dateDebut);
    	dateFinProforma = LibDate.localDateToDate(dateFin);
		barChartModelProforma = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
    	dateDebutDevis = LibDate.localDateToDate(dateDebut);
    	dateFinDevis = LibDate.localDateToDate(dateFin);
		barChartModelDevis = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		dateDebutCommande = LibDate.localDateToDate(dateDebut);
    	dateFinCommande = LibDate.localDateToDate(dateFin);
		barChartModelCommande = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		dateDebutReglement = LibDate.localDateToDate(dateDebut);
    	dateFinReglement = LibDate.localDateToDate(dateFin);
		barChartModelReglement = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		dateDebutLivraison = LibDate.localDateToDate(dateDebut);
    	dateFinLivraison = LibDate.localDateToDate(dateFin);
		barChartModelLivraison = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		dateDebutArticle = LibDate.localDateToDate(dateDebut);
    	dateFinArticle = LibDate.localDateToDate(dateFin);
//		barChartModelArticle = createBarChartModelSimpleBarreVide("N/A", true, -50);
		barChartModelArticleHoriz = createBarChartModelSimpleBarreVideHoriz("N/A", true, -50);
		barChartModelArticleParArticleHoriz = createBarChartModelSimpleBarreVideHoriz("N/A", true, -50);
		
		styleHeightChart = "height:300px";
		styleHeightChartFamille = "height:300px";
		//JE TAFFE ICI (YANN)
//		pieChartModelArticle = createBarChartModelSimpleBarreVideHoriz()
	
		

    	
    	dateDebutFacture = LibDate.localDateToDate(dateDebut);
    	dateFinFacture = LibDate.localDateToDate(dateFin);
		barChartModelFacture = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
//        montantTotalHtFacture = totauxCaPeriodeFacture(codeTiersCourant).getMtHtCalc();

    	codeEtatTiers = "tous";
    	
//    	barChartModelTiers = new BarChartModel();
    	//Initialisation des données sur la période exercice
//    	actRechercheTiers(codeEtatTiers);
//    	createBarModel();
    	
    }

    public void refreshDashBoard(String etatDocument){
    	
    }

    public void refresh(TabChangeEvent tabEvent){
//    	actRechercheTiers(etatTiers);
    	String nomTab = "";
//    	FacesEvent event = null;
    	
//		String codeTiers = (String)tabEvent.getComponent().getAttributes().get("codeTiers");
//    	Map<String,String> params =
//                FacesContext.getExternalContext().getRequestParameterMap();
//    	String codeTiers = params.get("codeTiers");
    	
    	initTousIndicateursZero();
    	
    	
    	if(tabEvent!=null && tabEvent.getTab()!=null) {
    		nomTab = tabEvent.getTab().getId();
    		tabActifDashboardTiers = tabEvent.getTab();
    	}
    	else {
    		if (tabActifDashboardTiers !=null) {
    			nomTab = tabActifDashboardTiers.getId();
    		}
    		else
    		{
    			nomTab = "idTabDashBoardTiersGeneral";
    		}
    	}
		switch (nomTab) {
    	// Général
		case "idTabDashBoardTiersGeneral":
			initIndicateursGeneral(codeTiersCourant);
			break;
		// Onglet Factures	
		case "idTabDashBoardTiersFacture":
			initIndicateursFacture(codeTiersCourant);
			break;
			
		// Onglet Devis	
			case "idTabDashBoardTiersDevis":
			initIndicateursDevis(codeTiersCourant);
			break;
		// Onglet Commande
			case "idTabDashBoardTiersCommande":
			initIndicateursCommande(codeTiersCourant);
			break;
		// Onglet Commande
			case "idTabDashBoardTiersReglement":
			initIndicateursReglement(codeTiersCourant);
			break;
		// Onglet Livraison	
			case "idTabDashBoardTiersLivraison":
			initIndicateursLivraison(codeTiersCourant);
			break;
		// Onglet Article	
			case "idTabDashBoardTiersArticle":
			initIndicateursArticle(codeTiersCourant);
			pieChartModelArticle = new PieChartModel();
	         
			pieChartModelArticle.set("Famille 1", 540);
			pieChartModelArticle.set("Famille 2", 325);
			pieChartModelArticle.set("Famille 3", 702);
			pieChartModelArticle.set("Famille 4", 421);
		         
			pieChartModelArticle.setTitle("Articles par famille");
			pieChartModelArticle.setLegendPosition("w");
			break;
		// Onglet Proforma	
		case "idTabDashBoardTiersProforma":
			initIndicateursProforma(codeTiersCourant);
			break;
			
		default:
			initTousIndicateursZero();
			break;
		}
    }

    public void initTousIndicateursZero() {
    	
    	//Section général 
    	montantTotalHtFactureGeneral =  BigDecimal.ZERO;
    	montantTotalHtNonTransfoArticleGeneral =  BigDecimal.ZERO;
    	nbFactureGeneral =  0;
    	// Section facture
        montantTotalHtFacture = BigDecimal.ZERO;
        montantTotalHtTransfoFacture = BigDecimal.ZERO;
        montantTotalHtNonTransfoFacture = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerFacture = BigDecimal.ZERO;
        montantTotalTtcNonRegleFacture = BigDecimal.ZERO;

        nbFacture = 0;
        nbFactureNonTransforme = 0;
        nbFactureTransforme = 0;
        nbFactureNonTransformeARelancer = 0;
        if (valueLignesFacture != null) {
            valueLignesFacture.clear();
        	
        }
		barChartModelFacture = createBarChartModelSimpleBarreVide("N/A", true, -50);
      
    	// Section proforma
        montantTotalHtProforma = BigDecimal.ZERO;
        montantTotalHtTransfoProforma = BigDecimal.ZERO;
        montantTotalHtNonTransfoProforma = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerProforma = BigDecimal.ZERO;
        montantTotalTtcProforma = BigDecimal.ZERO;
        montantTotalTtcTransfoProforma = BigDecimal.ZERO;
        montantTotalTtcNonTransfoProforma = BigDecimal.ZERO;
        montantTotalTtcNonTransfoARelancerProforma = BigDecimal.ZERO;
        nbProforma = 0;
        nbProformaNonTransforme = 0;
        nbProformaTransforme = 0;
        nbProformaNonTransformeARelancer = 0;
        if (valueLignesProforma != null) {
            valueLignesProforma.clear();
        	
        }
		barChartModelProforma = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
    	// Section Devis
        montantTotalHtDevis = BigDecimal.ZERO;
        montantTotalHtTransfoDevis = BigDecimal.ZERO;
        montantTotalHtNonTransfoDevis = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerDevis = BigDecimal.ZERO;
        montantTotalTtcDevis = BigDecimal.ZERO;
        montantTotalTtcTransfoDevis = BigDecimal.ZERO;
        montantTotalTtcNonTransfoDevis = BigDecimal.ZERO;
        montantTotalTtcNonTransfoARelancerDevis = BigDecimal.ZERO;
        nbDevis = 0;
        nbDevisNonTransforme = 0;
        nbDevisTransforme = 0;
        nbDevisNonTransformeARelancer = 0;
        if (valueLignesDevis != null) {
            valueLignesDevis.clear();
        	
        }
		barChartModelDevis = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		// Section Commande
        montantTotalHtCommande = BigDecimal.ZERO;
        montantTotalHtTransfoCommande = BigDecimal.ZERO;
        montantTotalHtNonTransfoCommande = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerCommande = BigDecimal.ZERO;
        montantTotalTtcCommande = BigDecimal.ZERO;
        montantTotalTtcTransfoCommande = BigDecimal.ZERO;
        montantTotalTtcNonTransfoCommande = BigDecimal.ZERO;
        montantTotalTtcNonTransfoARelancerCommande = BigDecimal.ZERO;
        nbCommande = 0;
        nbCommandeNonTransforme = 0;
        nbCommandeTransforme = 0;
        nbCommandeNonTransformeARelancer = 0;
        if (valueLignesCommande != null) {
            valueLignesCommande.clear();
        	
        }
		barChartModelCommande = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		// Section Reglement
        montantTotalHtReglement = BigDecimal.ZERO;
        montantTotalHtTransfoReglement = BigDecimal.ZERO;
        montantTotalHtNonTransfoReglement = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerReglement = BigDecimal.ZERO;
        montantTotalTtcReglement = BigDecimal.ZERO;
        montantTotalTtcTransfoReglement = BigDecimal.ZERO;
        montantTotalTtcNonTransfoReglement = BigDecimal.ZERO;
        montantTotalTtcNonTransfoARelancerReglement = BigDecimal.ZERO;
        nbReglement = 0;
        nbReglementNonTransforme = 0;
        nbReglementTransforme = 0;
        nbReglementNonTransformeARelancer = 0;
        if (valueLignesReglement != null) {
            valueLignesReglement.clear();
        	
        }
		barChartModelReglement = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		// Section Livraison
        montantTotalHtLivraison = BigDecimal.ZERO;
        montantTotalHtTransfoLivraison = BigDecimal.ZERO;
        montantTotalHtNonTransfoLivraison = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerLivraison = BigDecimal.ZERO;
        montantTotalTtcLivraison = BigDecimal.ZERO;
        montantTotalTtcTransfoLivraison = BigDecimal.ZERO;
        montantTotalTtcNonTransfoLivraison = BigDecimal.ZERO;
        montantTotalTtcNonTransfoARelancerLivraison = BigDecimal.ZERO;
        nbLivraison = 0;
        nbLivraisonNonTransforme = 0;
        nbLivraisonTransforme = 0;
        nbLivraisonNonTransformeARelancer = 0;
        if (valueLignesLivraison != null) {
            valueLignesLivraison.clear();
        	
        }
		barChartModelLivraison = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		// Section Article
        montantTotalHtArticle = BigDecimal.ZERO;
        montantTotalHtTransfoArticle = BigDecimal.ZERO;
        montantTotalHtNonTransfoArticle = BigDecimal.ZERO;
//        montantTotalHtNonTransfoARelancerArticle = BigDecimal.ZERO;
        nbArticle = 0;
        nbArticleNonTransforme = 0;
        nbArticleTransforme = 0;
//        nbArticleNonTransformeARelancer = 0;
        if (valueLignesArticle != null) {
            valueLignesArticle.clear();
        	
        }
        if (valueLignesArticleRegroupee != null) {
        	valueLignesArticleRegroupee.clear();
        	
        }
        if(valueLignesArticleRegroupeeParFamille != null) {
        	valueLignesArticleRegroupeeParFamille.clear();
        }
//		barChartModelArticle = createBarChartModelSimpleBarreVide("N/A", true, -50);
		barChartModelArticleHoriz = createBarChartModelSimpleBarreVideHoriz("N/A", true, -50);
		barChartModelArticleParArticleHoriz = createBarChartModelSimpleBarreVideHoriz("N/A", true, -50);
		styleHeightChart = "height:300px";
		styleHeightChartFamille = "height:300px;";

    }
    
    public void actRechercheTiers(String codeEtatTiers){
    }
    public void initIndicateursFactureGeneral(String codeTiers) {
    	montantTotalHtFactureGeneral =  totauxCaPeriodeFactureGeneral(codeTiers).getMtHtCalc();
    	montantTotalHtNonTransfoArticleGeneral =  totauxCaPeriodeArticleNonTransformeGeneral(codeTiers).getMtHtCalc();
    	nbFactureGeneral =  nbFactureSurPeriodeGeneral(codeTiers);
    }
    public void initIndicateursFacture(String codeTiers) {
        montantTotalHtFacture = totauxCaPeriodeFacture(codeTiers).getMtHtCalc();
        
        montantTotalHtTransfoFacture = totauxCaPeriodeFactureTransforme(codeTiers).getMtHtCalc();
        montantTotalHtNonTransfoFacture = totauxCaPeriodeFactureNonTransforme(codeTiers).getMtHtCalc();
        montantTotalTtcNonRegleFacture = totauxCaPeriodeFactureNonTransforme(codeTiers).getResteAReglerComplet();

        montantTotalHtNonTransfoARelancerFacture = totauxCaPeriodeFactureNonTransformeARelancer(codeTiers).getResteAReglerComplet();
        nbFacture = nbFactureSurPeriode(codeTiers);
        nbFactureNonTransforme = nbFactureNonTransformeSurPeriode(codeTiers);
        nbFactureTransforme = nbFactureTransformeSurPeriode(codeTiers);
        nbFactureNonTransformeARelancer = nbFactureNonTransformeARelancerSurPeriode(codeTiers);
      
    }
    public int nbFactureSurPeriodeGeneral(String codeTiers){

    	int nb = (int) taFactureService.countDocument(dateDebutGeneral, dateFinGeneral, codeTiers);
    	return nb;
    }
	// Retourne le nombre total de documents sur la période
    public int nbFactureSurPeriode(String codeTiers) {

    	int nb = (int) taFactureService.countDocument(dateDebutFacture, dateFinFacture, codeTiers);
    	return nb;
    }
    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureGeneral(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTotalDTO(dateDebutGeneral, dateFinGeneral,codeTiers);
    	return totaux;
    }
    public DocumentChiffreAffaireDTO totauxCaPeriodeFacture(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTotalDTO(dateDebutFacture, dateFinFacture,codeTiers);
    	return totaux;
    }
    
    public void initValueLignesFacture(String codeTiers) {
    	valueLignesFacture =  taFactureService.findAllDTOPeriode(dateDebutFacture, dateFinFacture,codeTiers);
		barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentFacture = taFactureService.listeChiffreAffaireTotalJmaDTO(dateDebutFacture, dateFinFacture, 1,codeTiers);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentFacture, dateDebutFacture, dateFinProforma));
		valeursDateDebut=dateDebutFacture;
		valeursDateFin=dateFinFacture;
    	infosLabelDoc="Factures réalisées";
    	valeursListe=valueLignesFacture;
    	
		valueLignesTiersTous=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiers, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS);
		valueLignesTiersArticlesTous=taFactureService.listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiers, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);
		valueLignesTiersTousDetail=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiers, false, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);

		
    }
    
    public void initAllValueFacture(String codeTiers) {
    	initIndicateursFacture(codeTiers);
    	initValueLignesFacture(codeTiers);
    	
    	
    }

    public int nbFactureNonTransformeSurPeriode(String codeTiers) {

    	int nb = (int) taFactureService.countDocumentNonTransforme(dateDebutFacture, dateFinFacture, codeTiers);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTransforme(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireNonTransformeTotalDTO(dateDebutFacture, dateFinFacture,codeTiers);
    	return totaux;
    }
    
    
    public void initValueLignesFactureNonTransforme(String codeTiers) {
    	valueLignesFacture =  taFactureService.findDocumentNonTransfosDTO(dateDebutFacture, dateFinFacture,codeTiers);
		barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentFacture = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutFacture, dateFinFacture, 1,codeTiers);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentFacture, dateDebutFacture, dateFinFacture));
		valeursDateDebut=dateDebutFacture;
		valeursDateFin=dateFinFacture;
    	infosLabelDoc="Factures non totalement réglées";
    	valeursListe=valueLignesFacture;
    }

    public int nbFactureNonTransformeARelancerSurPeriode(String codeTiers) {

    	int nb = (int) taFactureService.countDocumentNonTransformeARelancer(dateDebutFacture, dateFinFacture, 15, codeTiers);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTransformeARelancer(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutFacture, dateFinFacture, 15, codeTiers);
    	return totaux;
    }
    
    public void initValueLignesFactureNonTransformeARelancer(String codeTiers) {
    	valueLignesFacture =  taFactureService.findDocumentNonTransfosARelancerDTO(dateDebutFacture, dateFinFacture, 15, codeTiers);
		barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentFacture = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutFacture, dateFinFacture, 1, 15, codeTiers);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentFacture, dateDebutFacture, dateFinFacture));
		valeursDateDebut=dateDebutFacture;
		valeursDateFin=dateFinFacture;
    	infosLabelDoc="Factures à relancer";
    	valeursListe=valueLignesFacture;
    }
    
    public void initValueLignesFactureTransforme(String codeTiers) {
    	valueLignesFacture =  taFactureService.findDocumentTransfosDTO(dateDebutFacture, dateFinFacture,codeTiers);
    	barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentFacture = taFactureService.listeChiffreAffaireTransformeJmaDTO(dateDebutFacture, dateFinProforma, 1,codeTiers);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentFacture, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutFacture;
		valeursDateFin=dateFinFacture;
		infosLabelDoc="Factures entièrement réglées";
		valeursListe=valueLignesFacture;
    }

    public int nbFactureTransformeSurPeriode(String codeTiers) {

    	int nb = (int) taFactureService.countDocumentTransforme(dateDebutFacture, dateFinFacture, codeTiers);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureTransforme(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTransformeTotalDTO(dateDebutFacture, dateFinFacture,codeTiers);
    	return totaux;
    }

    ///////////// PROFORMA //////////////////////////
    public void initIndicateursProforma(String codeTiers) {
        montantTotalHtProforma = totauxCaPeriodeProforma(codeTiers).getMtHtCalc();
        montantTotalHtTransfoProforma = totauxCaPeriodeProformaTransforme(codeTiers).getMtHtCalc();
        montantTotalHtNonTransfoProforma = totauxCaPeriodeProformaNonTransforme(codeTiers).getMtHtCalc();
        montantTotalHtNonTransfoARelancerProforma = totauxCaPeriodeProformaNonTransformeARelancer(codeTiers).getMtHtCalc();
        
        montantTotalTtcProforma = totauxCaPeriodeProforma(codeTiers).getMtTtcCalc();
        montantTotalTtcTransfoProforma = totauxCaPeriodeProformaTransforme(codeTiers).getMtTtcCalc();
        montantTotalTtcNonTransfoProforma = totauxCaPeriodeProformaNonTransforme(codeTiers).getMtTtcCalc();
        montantTotalTtcNonTransfoARelancerProforma = totauxCaPeriodeProformaNonTransformeARelancer(codeTiers).getMtTtcCalc();
        nbProforma = nbProformaSurPeriode(codeTiers);
        nbProformaNonTransforme = nbProformaNonTransformeSurPeriode(codeTiers) ;
        nbProformaTransforme = nbProformaTransformeSurPeriode(codeTiers);
        nbProformaNonTransformeARelancer = nbProformaNonTransformeARelancerSurPeriode(codeTiers);
    }
    
    
	// Retourne le nombre total de documents sur la période
    public int nbProformaSurPeriode(String codeTiers) {

    	int nb = (int) taProformaService.countDocument(dateDebutProforma, dateFinProforma, codeTiers);
    	return nb;
    }
    
    public DocumentChiffreAffaireDTO totauxCaPeriodeProforma(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireTotalDTO(dateDebutProforma, dateFinProforma,codeTiers);
    	return totaux;
    }
    
    public void initValueLignesProforma(String codeTiers) {
    	valueLignesProforma =  taProformaService.findAllDTOPeriode(dateDebutProforma, dateFinProforma,codeTiers);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireTotalJmaDTO(dateDebutProforma, dateFinProforma, 1,codeTiers);
		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proformas réalisées";
		valeursListe=valueLignesProforma;
    }

    public void initAllValueProforma(String codeTiers) {
    	initIndicateursProforma(codeTiers);
    	initValueLignesProforma(codeTiers);
    	
    	
    }
    
    public int nbProformaNonTransformeSurPeriode(String codeTiers) {

    	int nb = (int) taProformaService.countDocumentNonTransforme(dateDebutProforma, dateFinProforma, codeTiers);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeProformaNonTransforme(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireNonTransformeTotalDTO(dateDebutProforma, dateFinProforma,codeTiers);
    	return totaux;
    }
    
    public void initValueLignesProformaTransforme(String codeTiers) {
    	valueLignesProforma =  taProformaService.findDocumentTransfosDTO(dateDebutProforma, dateFinProforma,codeTiers);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireTransformeJmaDTO(dateDebutProforma, dateFinProforma, 1,codeTiers);
		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proformas transformées";
		valeursListe=valueLignesProforma;

    	
    }

    public int nbProformaTransformeSurPeriode(String codeTiers) {

    	int nb = (int) taProformaService.countDocumentTransforme(dateDebutProforma, dateFinProforma, codeTiers);
    	return nb;
    }


    public DocumentChiffreAffaireDTO totauxCaPeriodeProformaNonTransformeARelancer(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutProforma, dateFinProforma, 15, codeTiers);
    	return totaux;
    }
    
    public void initValueLignesProformaNonTransformeARelancer(String codeTiers) {
    	valueLignesProforma =  taProformaService.findDocumentNonTransfosARelancerDTO(dateDebutProforma, dateFinProforma, 15, codeTiers);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutProforma, dateFinProforma, 1, 15, codeTiers);
		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proformas a relancer";
		valeursListe=valueLignesProforma;
    }
    
    public int nbProformaNonTransformeARelancerSurPeriode(String codeTiers) {

    	int nb = (int) taProformaService.countDocumentNonTransformeARelancer(dateDebutProforma, dateFinProforma, 15, codeTiers);
    	return nb;
    }

    
    public DocumentChiffreAffaireDTO totauxCaPeriodeProformaTransforme(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireTransformeTotalDTO(dateDebutProforma, dateFinProforma,codeTiers);
    	return totaux;
    }
    
    public void initValueLignesProformaNonTransforme(String codeTiers) {
    	valueLignesProforma =  taProformaService.findDocumentNonTransfosDTO(dateDebutProforma, dateFinProforma,codeTiers);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutProforma, dateFinProforma, 1,codeTiers);
		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proformas non transformées";
		valeursListe=valueLignesProforma;
    	
    }
    
    ///////////COMMANDES
	public void initAllValueCommande(String codeTiers) {
		initIndicateursCommande(codeTiers);
		initValueLignesCommande(codeTiers);
		 	
	}
	public int nbCommandeSurPeriode(String codeTiers) {
	
		int nb = (int) taBoncdeService.countDocument(dateDebutCommande, dateFinCommande, codeTiers);
		return nb;
	}
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeCommande(String codeTiers) {
		// Calcule les CA (HT, TVA, TTC) total sur la période
		DocumentChiffreAffaireDTO totaux = taBoncdeService.chiffreAffaireTotalDTO(dateDebutCommande, dateFinCommande,codeTiers);
		return totaux;
	}
	public void initIndicateursCommande(String codeTiers) {
	  montantTotalHtCommande = totauxCaPeriodeCommande(codeTiers).getMtHtCalc();
	  montantTotalHtTransfoCommande = totauxCaPeriodeCommandeTransforme(codeTiers).getMtHtCalc();
	  montantTotalHtNonTransfoCommande = totauxCaPeriodeCommandeNonTransforme(codeTiers).getMtHtCalc();
	  montantTotalHtNonTransfoARelancerCommande = totauxCaPeriodeCommandeNonTransformeARelancer(codeTiers).getMtHtCalc();
	  
	  montantTotalTtcCommande = totauxCaPeriodeCommande(codeTiers).getMtTtcCalc();
      montantTotalTtcTransfoCommande = totauxCaPeriodeCommandeTransforme(codeTiers).getMtTtcCalc();
      montantTotalTtcNonTransfoCommande = totauxCaPeriodeCommandeNonTransforme(codeTiers).getMtTtcCalc();
      montantTotalTtcNonTransfoARelancerCommande = totauxCaPeriodeCommandeNonTransformeARelancer(codeTiers).getMtTtcCalc();
	  nbCommande = nbCommandeSurPeriode(codeTiers);
	  nbCommandeNonTransforme = nbCommandeNonTransformeSurPeriode(codeTiers) ;
	  nbCommandeTransforme = nbCommandeTransformeSurPeriode(codeTiers);
	  nbCommandeNonTransformeARelancer = nbCommandeNonTransformeARelancerSurPeriode(codeTiers);
	}
	public void initValueLignesCommande(String codeTiers) {
		valueLignesCommande =  taBoncdeService.findAllDTOPeriode(dateDebutCommande, dateFinCommande,codeTiers);
		barChartModelCommande = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentCommande = taBoncdeService.listeChiffreAffaireTotalJmaDTO(dateDebutCommande, dateFinCommande, 1,codeTiers);
		barChartModelCommande.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCommande, dateDebutCommande, dateFinCommande));
		valeursDateDebut=dateDebutCommande;
		valeursDateFin=dateFinCommande;
		infosLabelDoc="Commandes réalisées";
		valeursListe=valueLignesCommande;
	}
	

	

	
	
	public int nbCommandeNonTransformeSurPeriode(String codeTiers) {
	
		int nb = (int) taBoncdeService.countDocumentNonTransforme(dateDebutCommande, dateFinCommande, codeTiers);
		return nb;
	}
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeCommandeNonTransforme(String codeTiers) {
		// Calcule les CA (HT, TVA, TTC) total sur la période
		DocumentChiffreAffaireDTO totaux = taBoncdeService.chiffreAffaireNonTransformeTotalDTO(dateDebutCommande, dateFinCommande,codeTiers);
		return totaux;
	}
	
	public void initValueLignesCommandeTransforme(String codeTiers) {
		valueLignesCommande =  taBoncdeService.findDocumentTransfosDTO(dateDebutCommande, dateFinCommande,codeTiers);
		barChartModelCommande = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentCommande = taBoncdeService.listeChiffreAffaireTransformeJmaDTO(dateDebutCommande, dateFinCommande, 1,codeTiers);
		barChartModelCommande.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCommande, dateDebutCommande, dateFinCommande));
		valeursDateDebut=dateDebutCommande;
		valeursDateFin=dateFinCommande;
		infosLabelDoc="Commandes tranformées";
		valeursListe=valueLignesCommande;
		
	}
	
	public int nbCommandeTransformeSurPeriode(String codeTiers) {
	
		int nb = (int) taBoncdeService.countDocumentTransforme(dateDebutCommande, dateFinCommande, codeTiers);
		return nb;
	}
	
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeCommandeNonTransformeARelancer(String codeTiers) {
		// Calcule les CA (HT, TVA, TTC) total sur la période
		DocumentChiffreAffaireDTO totaux = taBoncdeService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutCommande, dateFinCommande, 15, codeTiers);
		return totaux;
	}
	
	public void initValueLignesCommandeNonTransformeARelancer(String codeTiers) {
		valueLignesCommande =  taBoncdeService.findDocumentNonTransfosARelancerDTO(dateDebutCommande, dateFinCommande, 15, codeTiers);
		barChartModelCommande = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentCommande = taBoncdeService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutCommande, dateFinCommande, 1, 15, codeTiers);
		barChartModelCommande.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCommande, dateDebutCommande, dateFinCommande));
		valeursDateDebut=dateDebutCommande;
		valeursDateFin=dateFinCommande;
		infosLabelDoc="Commandes à relancer";
		valeursListe=valueLignesCommande;
	}
	
	public int nbCommandeNonTransformeARelancerSurPeriode(String codeTiers) {
	
		int nb = (int) taBoncdeService.countDocumentNonTransformeARelancer(dateDebutCommande, dateFinCommande, 15, codeTiers);
		return nb;
	}
	
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeCommandeTransforme(String codeTiers) {
		// Calcule les CA (HT, TVA, TTC) total sur la période
		DocumentChiffreAffaireDTO totaux = taBoncdeService.chiffreAffaireTransformeTotalDTO(dateDebutCommande, dateFinCommande,codeTiers);
		return totaux;
	}
	
	public void initValueLignesCommandeNonTransforme(String codeTiers) {
		valueLignesCommande =  taBoncdeService.findDocumentNonTransfosDTO(dateDebutCommande, dateFinCommande,codeTiers);
		barChartModelCommande = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentCommande = taBoncdeService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutCommande, dateFinCommande, 1,codeTiers);
		barChartModelCommande.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCommande, dateDebutCommande, dateFinCommande));
		valeursDateDebut=dateDebutCommande;
		valeursDateFin=dateFinCommande;
		infosLabelDoc="Commandes non transformées";
		valeursListe=valueLignesCommande;
	}
//////////////////FIN COMMANDES////////////////////////

//////////////////REGLEMENTS///////////////////////////

		public void initAllValueReglement(String codeTiers) {
			initIndicateursReglement(codeTiers);
			initValueLignesReglement(codeTiers);
			 	
		}
		public int nbReglementSurPeriode(String codeTiers) {
		
			int nb = (int) taReglementService.countDocument(dateDebutReglement, dateFinReglement, codeTiers);
			return nb;
		}
		
		public DocumentChiffreAffaireDTO totauxCaPeriodeReglement(String codeTiers) {
			// Calcule les CA (HT, TVA, TTC) total sur la période
			DocumentChiffreAffaireDTO totaux = taReglementService.chiffreAffaireTotalDTO(dateDebutReglement, dateFinReglement,codeTiers);
			return totaux;
		}
		public void initIndicateursReglement(String codeTiers) {
		  montantTotalHtReglement = totauxCaPeriodeReglement(codeTiers).getMtHtCalc();
		  montantTotalHtTransfoReglement = totauxCaPeriodeReglementTransforme(codeTiers).getMtHtCalc();
		  montantTotalHtNonTransfoReglement = totauxCaPeriodeReglementNonTransforme(codeTiers).getMtHtCalc();
		  montantTotalHtNonTransfoARelancerReglement = totauxCaPeriodeReglementNonTransformeARelancer(codeTiers).getMtHtCalc();
		  
		  montantTotalTtcReglement = totauxCaPeriodeReglement(codeTiers).getMtTtcCalc();
	      montantTotalTtcTransfoReglement = totauxCaPeriodeReglementTransforme(codeTiers).getMtTtcCalc();
	      montantTotalTtcNonTransfoReglement = totauxCaPeriodeReglementNonTransforme(codeTiers).getResteAReglerComplet();
	      montantTotalTtcNonTransfoARelancerReglement = totauxCaPeriodeReglementNonTransformeARelancer(codeTiers).getResteAReglerComplet();
		  nbReglement = nbReglementSurPeriode(codeTiers);
		  nbReglementNonTransforme = nbReglementNonTransformeSurPeriode(codeTiers) ;
		  nbReglementTransforme = nbReglementTransformeSurPeriode(codeTiers);
		  nbReglementNonTransformeARelancer = nbReglementNonTransformeARelancerSurPeriode(codeTiers);
		}
		public void initValueLignesReglement(String codeTiers) {
			valueLignesReglement =  taReglementService.findAllDTOPeriode(dateDebutReglement, dateFinReglement,codeTiers);
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocumentReglement = taReglementService.listeChiffreAffaireTotalJmaDTO(dateDebutReglement, dateFinReglement, 1,codeTiers);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentReglement, dateDebutReglement, dateFinReglement));
			valeursDateDebut=dateDebutReglement;
			valeursDateFin=dateFinReglement;
			infosLabelDoc="Reglements réalisées";
			valeursListe=valueLignesReglement;
		}
		
		public List<TaRReglementDTO> listeFactureReglement(String codeDocument){
			List<TaRReglementDTO> liste=taRReglementService.rechercheDocumentDTO(codeDocument,codeTiersCourant);
			
			return liste;
			
			
		}
		
		

		
		
		public int nbReglementNonTransformeSurPeriode(String codeTiers) {
		
			int nb = (int) taReglementService.countDocumentNonTransforme(dateDebutReglement, dateFinReglement, codeTiers);
			return nb;
		}
		
		public DocumentChiffreAffaireDTO totauxCaPeriodeReglementNonTransforme(String codeTiers) {
			// Calcule les CA (HT, TVA, TTC) total sur la période
			DocumentChiffreAffaireDTO totaux = taReglementService.chiffreAffaireNonTransformeTotalDTO(dateDebutReglement, dateFinReglement,codeTiers);
			return totaux;
		}
		
		public void initValueLignesReglementTransforme(String codeTiers) {
			valueLignesReglement =  taReglementService.findDocumentTransfosDTO(dateDebutReglement, dateFinReglement,codeTiers);
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocumentReglement = taReglementService.listeChiffreAffaireTransformeJmaDTO(dateDebutReglement, dateFinReglement, 1,codeTiers);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentReglement, dateDebutReglement, dateFinReglement));
			valeursDateDebut=dateDebutReglement;
			valeursDateFin=dateFinReglement;
			infosLabelDoc="Reglements tranformées";
			valeursListe=valueLignesReglement;
			
		}
		
		public int nbReglementTransformeSurPeriode(String codeTiers) {
		
			int nb = (int) taBoncdeService.countDocumentTransforme(dateDebutReglement, dateFinReglement, codeTiers);
			return nb;
		}
		
		
		public DocumentChiffreAffaireDTO totauxCaPeriodeReglementNonTransformeARelancer(String codeTiers) {
			// Calcule les CA (HT, TVA, TTC) total sur la période
			DocumentChiffreAffaireDTO totaux = taReglementService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutReglement, dateFinReglement, 0, codeTiers);
			return totaux;
		}
		
		public void initValueLignesReglementNonTransformeARelancer(String codeTiers) {
			valueLignesReglement =  taReglementService.findDocumentNonTransfosARelancerDTO(dateDebutReglement, dateFinReglement, 0, codeTiers);
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocumentReglement = taReglementService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutReglement, dateFinReglement, 1, 0, codeTiers);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentReglement, dateDebutReglement, dateFinReglement));
			valeursDateDebut=dateDebutReglement;
			valeursDateFin=dateFinReglement;
			infosLabelDoc="Règlements à relancer";
			valeursListe=valueLignesReglement;
		}
		
		public int nbReglementNonTransformeARelancerSurPeriode(String codeTiers) {
		
			int nb = (int) taReglementService.countDocumentNonTransformeARelancer(dateDebutReglement, dateFinReglement, 0, codeTiers);
			return nb;
		}
		
		
		public DocumentChiffreAffaireDTO totauxCaPeriodeReglementTransforme(String codeTiers) {
			// Calcule les CA (HT, TVA, TTC) total sur la période
			DocumentChiffreAffaireDTO totaux = taBoncdeService.chiffreAffaireTransformeTotalDTO(dateDebutReglement, dateFinReglement,codeTiers);
			return totaux;
		}
		
		public void initValueLignesReglementNonTransforme(String codeTiers) {
			valueLignesReglement =  taReglementService.findDocumentNonTransfosDTO(dateDebutReglement, dateFinReglement,codeTiers);
			barChartModelReglement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocumentReglement = taReglementService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutReglement, dateFinReglement, 1,codeTiers);
			barChartModelReglement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentReglement, dateDebutReglement, dateFinReglement));
			valeursDateDebut=dateDebutReglement;
			valeursDateFin=dateFinReglement;
			infosLabelDoc="Reglements non transformées";
			valeursListe=valueLignesReglement;
		}
	
	
/////////////////FIN REGLEMENTS///////////////////////
  
	
	
//////////////    Début section Devis ///////////////////
	
	public void initAllValueDevis(String codeTiers) {
		initIndicateursDevis(codeTiers);
		initValueLignesDevis(codeTiers);
		 	
	}
    public void initIndicateursDevis(String codeTiers) {
        montantTotalHtDevis = totauxCaPeriodeDevis(codeTiers).getMtHtCalc();
        montantTotalHtTransfoDevis = totauxCaPeriodeDevisTransforme(codeTiers).getMtHtCalc();
        montantTotalHtNonTransfoDevis = totauxCaPeriodeDevisNonTransforme(codeTiers).getMtHtCalc();
        montantTotalHtNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeTiers).getMtHtCalc();
        
        montantTotalTtcDevis = totauxCaPeriodeDevis(codeTiers).getMtTtcCalc();
        montantTotalTtcTransfoDevis = totauxCaPeriodeDevisTransforme(codeTiers).getMtTtcCalc();
        montantTotalTtcNonTransfoDevis =  totauxCaPeriodeDevisNonTransforme(codeTiers).getMtTtcCalc();
        montantTotalTtcNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeTiers).getMtTtcCalc();
        nbDevis = nbDevisSurPeriode(codeTiers);
        nbDevisNonTransforme = nbDevisNonTransformeSurPeriode(codeTiers) ;
        nbDevisTransforme = nbDevisTransformeSurPeriode(codeTiers);
        nbDevisNonTransformeARelancer = nbDevisNonTransformeARelancerSurPeriode(codeTiers);
    }
    
    
	// Retourne le nombre total de documents sur la période
    public int nbDevisSurPeriode(String codeTiers) {

    	int nb = (int) taDevisService.countDocument(dateDebutDevis, dateFinDevis, codeTiers);
    	return nb;
    }
    
    public DocumentChiffreAffaireDTO totauxCaPeriodeDevis(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTotalDTO(dateDebutDevis, dateFinDevis,codeTiers);
    	return totaux;
    }
    
    public void initValueLignesDevis(String codeTiers) {
    	valueLignesFacture =  taFactureService.findAllDTOPeriode(dateDebutFacture, dateFinFacture,codeTiers);
    	valueLignesDevis =  taDevisService.findAllDTOPeriode(dateDebutDevis, dateFinDevis,codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireTotalJmaDTO(dateDebutDevis, dateFinDevis, 1,codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis réalisées";
		valeursListe=valueLignesDevis;
    }
    
    public int nbDevisNonTransformeSurPeriode(String codeTiers) {

    	int nb = (int) taDevisService.countDocumentNonTransforme(dateDebutDevis, dateFinDevis, codeTiers);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransforme(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeTiers);
    	return totaux;
    }
    
    public void initValueLignesDevisTransforme(String codeTiers) {
    	valueLignesDevis =  taDevisService.findDocumentTransfosDTO(dateDebutDevis, dateFinDevis,codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis transformées";
		valeursListe=valueLignesDevis;
    	
    }

    public int nbDevisTransformeSurPeriode(String codeTiers) {

    	int nb = (int) taDevisService.countDocumentTransforme(dateDebutDevis, dateFinDevis, codeTiers);
    	return nb;
    }


    public DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransformeARelancer(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutDevis, dateFinDevis, 15, codeTiers);
    	return totaux;
    }
    
    public void initValueLignesDevisNonTransformeARelancer(String codeTiers) {
    	valueLignesDevis =  taDevisService.findDocumentNonTransfosARelancerDTO(dateDebutDevis, dateFinDevis, 15, codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutDevis, dateFinDevis, 1, 15, codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis à relancer";
		valeursListe=valueLignesDevis;
    }
    
    public int nbDevisNonTransformeARelancerSurPeriode(String codeTiers) {

    	int nb = (int) taDevisService.countDocumentNonTransformeARelancer(dateDebutDevis, dateFinDevis, 15, codeTiers);
    	return nb;
    }

    
    public DocumentChiffreAffaireDTO totauxCaPeriodeDevisTransforme(String codeTiers) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeTiers);
    	return totaux;
    }
    
    public void initValueLignesDevisNonTransforme(String codeTiers) {
    	valueLignesDevis =  taDevisService.findDocumentNonTransfosDTO(dateDebutDevis, dateFinDevis,codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis non tranformés";
		valeursListe=valueLignesDevis;
    }    
//    Fin section Devis
    
    
	//////////////LIVRAISON ///////////////////
		
	public void initAllValueLivraison(String codeTiers) {
	initIndicateursLivraison(codeTiers);
	initValueLignesLivraison(codeTiers);
	
	}
	public void initIndicateursLivraison(String codeTiers) {
	montantTotalHtLivraison = totauxCaPeriodeLivraison(codeTiers).getMtHtCalc();
	montantTotalHtTransfoLivraison = totauxCaPeriodeLivraisonTransforme(codeTiers).getMtHtCalc();
	montantTotalHtNonTransfoLivraison = totauxCaPeriodeLivraisonNonTransforme(codeTiers).getMtHtCalc();
	montantTotalHtNonTransfoARelancerLivraison = totauxCaPeriodeLivraisonNonTransformeARelancer(codeTiers).getMtHtCalc();
	
    montantTotalTtcLivraison = totauxCaPeriodeLivraison(codeTiers).getMtTtcCalc();
    montantTotalTtcTransfoLivraison = totauxCaPeriodeLivraisonTransforme(codeTiers).getMtTtcCalc();
    montantTotalTtcNonTransfoLivraison = totauxCaPeriodeLivraisonNonTransforme(codeTiers).getMtTtcCalc();
    montantTotalTtcNonTransfoARelancerLivraison = totauxCaPeriodeLivraisonNonTransformeARelancer(codeTiers).getMtTtcCalc();
	nbLivraison = nbLivraisonSurPeriode(codeTiers);
	nbLivraisonNonTransforme = nbLivraisonNonTransformeSurPeriode(codeTiers) ;
	nbLivraisonTransforme = nbLivraisonTransformeSurPeriode(codeTiers);
	nbLivraisonNonTransformeARelancer = nbLivraisonNonTransformeARelancerSurPeriode(codeTiers);
	}
	
	
	// Retourne le nombre total de documents sur la période
	public int nbLivraisonSurPeriode(String codeTiers) {
	
	int nb = (int) taBonlivService.countDocument(dateDebutLivraison, dateFinLivraison, codeTiers);
	return nb;
	}
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeLivraison(String codeTiers) {
	// Calcule les CA (HT, TVA, TTC) total sur la période
	DocumentChiffreAffaireDTO totaux = taBonlivService.chiffreAffaireTotalDTO(dateDebutLivraison, dateFinLivraison,codeTiers);
	return totaux;
	}
	
	public void initValueLignesLivraison(String codeTiers) {
	valueLignesLivraison =  taBonlivService.findAllDTOPeriode(dateDebutLivraison, dateFinLivraison,codeTiers);
	barChartModelLivraison = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
	listeCaMoisDocumentLivraison = taBonlivService.listeChiffreAffaireTotalJmaDTO(dateDebutLivraison, dateFinLivraison, 1,codeTiers);
	barChartModelLivraison.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentLivraison, dateDebutLivraison, dateFinLivraison));
	valeursDateDebut=dateDebutLivraison;
	valeursDateFin=dateFinLivraison;
	infosLabelDoc="Livraisons réalisées";
	valeursListe=valueLignesLivraison;
	}
	
	public int nbLivraisonNonTransformeSurPeriode(String codeTiers) {
	
	int nb = (int) taBonlivService.countDocumentNonTransforme(dateDebutLivraison, dateFinLivraison, codeTiers);
	return nb;
	}
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeLivraisonNonTransforme(String codeTiers) {
	// Calcule les CA (HT, TVA, TTC) total sur la période
	DocumentChiffreAffaireDTO totaux = taBonlivService.chiffreAffaireNonTransformeTotalDTO(dateDebutLivraison, dateFinLivraison,codeTiers);
	return totaux;
	}
	
	public void initValueLignesLivraisonTransforme(String codeTiers) {
	valueLignesLivraison =  taBonlivService.findDocumentTransfosDTO(dateDebutLivraison, dateFinLivraison,codeTiers);
	barChartModelLivraison = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
	listeCaMoisDocumentLivraison = taBonlivService.listeChiffreAffaireTransformeJmaDTO(dateDebutLivraison, dateFinLivraison, 1,codeTiers);
	barChartModelLivraison.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentLivraison, dateDebutLivraison, dateFinLivraison));
	valeursDateDebut=dateDebutLivraison;
	valeursDateFin=dateFinLivraison;
	infosLabelDoc="Livraisons facturées";
	valeursListe=valueLignesLivraison;
	
	}
	
	public int nbLivraisonTransformeSurPeriode(String codeTiers) {
	
	int nb = (int) taBonlivService.countDocumentTransforme(dateDebutLivraison, dateFinLivraison, codeTiers);
	return nb;
	}
	
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeLivraisonNonTransformeARelancer(String codeTiers) {
	// Calcule les CA (HT, TVA, TTC) total sur la période
	DocumentChiffreAffaireDTO totaux = taBonlivService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutLivraison, dateFinLivraison, 15, codeTiers);
	return totaux;
	}
	
	public void initValueLignesLivraisonNonTransformeARelancer(String codeTiers) {
	valueLignesLivraison =  taBonlivService.findDocumentNonTransfosARelancerDTO(dateDebutLivraison, dateFinLivraison, 15, codeTiers);
	barChartModelLivraison = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
	listeCaMoisDocumentLivraison = taBonlivService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutLivraison, dateFinLivraison, 1, 15, codeTiers);
	barChartModelLivraison.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentLivraison, dateDebutLivraison, dateFinLivraison));
	valeursDateDebut=dateDebutLivraison;
	valeursDateFin=dateFinLivraison;
	infosLabelDoc="Livraisons";
	valeursListe=valueLignesLivraison;
	}
	
	public int nbLivraisonNonTransformeARelancerSurPeriode(String codeTiers) {
	
	int nb = (int) taBonlivService.countDocumentNonTransformeARelancer(dateDebutLivraison, dateFinLivraison, 15, codeTiers);
	return nb;
	}
	
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeLivraisonTransforme(String codeTiers) {
	// Calcule les CA (HT, TVA, TTC) total sur la période
	DocumentChiffreAffaireDTO totaux = taBonlivService.chiffreAffaireTransformeTotalDTO(dateDebutLivraison, dateFinLivraison,codeTiers);
	return totaux;
	}
	
	public void initValueLignesLivraisonNonTransforme(String codeTiers) {
		valueLignesLivraison =  taBonlivService.findDocumentNonTransfosDTO(dateDebutLivraison, dateFinLivraison,codeTiers);
	barChartModelLivraison = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
	listeCaMoisDocumentLivraison = taBonlivService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutLivraison, dateFinLivraison, 1,codeTiers);
	barChartModelLivraison.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentLivraison, dateDebutLivraison, dateFinLivraison));
	valeursDateDebut=dateDebutLivraison;
	valeursDateFin=dateFinLivraison;
	infosLabelDoc="Livraisons non facturées";
	valeursListe=valueLignesLivraison;
	
	}    
	///////////// Fin section Livraison //////////////////
	
	
    ///////////// ARTICLES ACHETES //////////////////////
	
	public void initAllValueArticle(String codeTiers) {
		initIndicateursArticle(codeTiers);
		initValueLignesArticle(codeTiers);
		
		}
	public void initIndicateursArticle(String codeTiers) {
	montantTotalHtArticle = totauxCaPeriodeArticle(codeTiers).getMtHtCalc();
//	montantTotalHtTransfoArticle = totauxCaPeriodeArticleTransforme(codeTiers).getMtHtCalc();
	montantTotalHtNonTransfoArticle = totauxCaPeriodeArticleNonTransforme(codeTiers).getMtHtCalc();
//	montantTotalHtNonTransfoARelancerArticle = totauxCaPeriodeArticleNonTransformeARelancer(codeTiers).getMtHtCalc();
	nbArticle = nbArticleSurPeriode(codeTiers);
//	nbArticleNonTransforme = nbArticleNonTransformeSurPeriode(codeTiers) ;
	nbArticleTransforme = nbArticleTransformeSurPeriode(codeTiers);
//	nbArticleNonTransformeARelancer = nbArticleNonTransformeARelancerSurPeriode(codeTiers);
	}
	
	// Retourne le nombre total de documents sur la période
	public int nbArticleSurPeriode(String codeTiers) {
	
//	int nb = (int) taArticleService.countDocument(dateDebutArticle, dateFinArticle, codeTiers);
	int nb = (int) taFactureService.findArticlesParTiersParMois(dateDebutArticle, dateFinArticle, codeTiers).size();// A Remplacer par un count
//	listeDocumentArticleParTiersParMois = taFactureService.findArticlesParTiersParMois(premierJourMois, dernierJourMois);
	return nb;
	}
	
	public int nbArticleNonTransformeSurPeriode(String codeTiers) {
		
		int nb = (int) taFactureService.findArticlesParTiersNonTransforme(dateDebutArticle, dateFinArticle, codeTiers).size();
		return nb;
	}
	
	public int nbArticleTransformeSurPeriode(String codeTiers) {
		
		int nb = (int) taFactureService.findArticlesParTiersTransforme(dateDebutArticle, dateFinArticle, codeTiers).size();
		return nb;
	}
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeArticle(String codeTiers) {
	// Calcule les CA (HT, TVA, TTC) total sur la période
	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTotalDTO(dateDebutArticle, dateFinArticle,codeTiers);
	return totaux;
	}
	
	public DocumentChiffreAffaireDTO totauxCaPeriodeArticleTransforme(String codeTiers) {
		// Calcule les CA (HT, TVA, TTC) total sur la période
		DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTransformeTotalDTO(dateDebutArticle, dateFinArticle, codeTiers);
	return totaux;
	}
	public DocumentChiffreAffaireDTO totauxCaPeriodeArticleNonTransformeGeneral(String codeTiers) {
		// Calcule les CA (HT, TVA, TTC) total sur la période
		DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireNonTransformeTotalDTO(dateDebutGeneral, dateFinGeneral, codeTiers);
		return totaux;
	}
	public DocumentChiffreAffaireDTO totauxCaPeriodeArticleNonTransforme(String codeTiers) {
		// Calcule les CA (HT, TVA, TTC) total sur la période
	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireNonTransformeTotalDTO(dateDebutArticle, dateFinArticle, codeTiers);
	return totaux;
	}
    
	public void initValueLignesArticle(String codeTiers) {
	valueLignesArticle =  taFactureService.findArticlesParTiersParMois(dateDebutArticle,dateFinArticle,codeTiers);//A CHANGER 
	valueLignesArticleRegroupee = taFactureService.findArticlesParTiersParMoisParTypeRegroupement(dateDebutArticle,dateFinArticle,codeTiers, Const.PAR_ARTICLE, "%", true);
	valueLignesArticleRegroupeeParFamille = taFactureService.findArticlesParTiersParMoisParTypeRegroupement(dateDebutArticle,dateFinArticle,codeTiers, Const.PAR_FAMILLE_ARTICLE, "%", true);

//	barChartModelArticle = createBarChartModelSimpleBarre("Totaux HT mensuel par famille d'article", true, -50);
	barChartModelArticleHoriz = createBarChartModelSimpleBarreHoriz("Totaux HT par famille d'article pour la période choisie", true, 50, 50);
	barChartModelArticleParArticleHoriz = createBarChartModelSimpleBarreHoriz("Totaux HT par article pour la période choisie", true, 50, 50);
	
	
	if(valueLignesArticleRegroupee == null || valueLignesArticleRegroupee.isEmpty()) {
		barChartModelArticleParArticleHoriz = createBarChartModelSimpleBarreVideHoriz("N/A", true, -50);
	}else {
		if(valueLignesArticleRegroupee.size() > 6) {
			styleHeightChart = "height:600px";
		}
		if(valueLignesArticleRegroupee.size() > 20) {
			styleHeightChart = "height:900px";
		}
		barChartModelArticleParArticleHoriz.addSeries(createBarChartSerieParArticle("Articles", valueLignesArticleRegroupee, dateDebutArticle, dateFinArticle));
	}
    
	
	if(valueLignesArticleRegroupeeParFamille.size() > 6) {
		styleHeightChartFamille = "height:600px";
	}
	if(valueLignesArticleRegroupeeParFamille.size() > 20) {
		styleHeightChartFamille = "height:900px";
	}
    
	listeCaMoisDocumentArticle = taFactureService.listeChiffreAffaireTotalJmaDTO(dateDebutArticle, dateFinArticle, 1,codeTiers);
//	listeCaFamilleDocumentArticle = taFactureService.listeChiffreAffaireTotalJmaDTOParRegroupement(dateDebutArticle, dateFinArticle, 1, codeTiers, Const.PAR_FAMILLE_ARTICLE, "%");
	listeCaFamilleDocumentArticle = taFactureService.listeChiffreAffaireTotalDTOParTypeRegroupement(dateDebutArticle, dateFinArticle,codeTiers, Const.PAR_FAMILLE_ARTICLE, "%", true);
//	barChartModelArticle.addSeries(createBarChartSerieFamilleArticle("Familles", listeCaFamilleDocumentArticle, dateDebutArticle, dateFinArticle)); 
	barChartModelArticleHoriz.addSeries(createBarChartSerieFamilleArticle("Familles", listeCaFamilleDocumentArticle, dateDebutArticle, dateFinArticle));
//	barChartModelArticleParArticleHoriz.addSeries(createBarChartSerieMensuel("Articles", listeCaMoisDocumentArticle, dateDebutArticle, dateFinArticle));
	
	


	System.out.println(barChartModelArticleHoriz);
	

	

	}
    
    
//////////////////  Début section Général //////////////////////////////////////////////
  public void initIndicateursGeneral(String codeTiers) {
	  initIndicateursFacture(codeTiers);
	  initIndicateursFactureGeneral(codeTiers);
      montantTotalHtDevis = totauxCaPeriodeDevis(codeTiers).getMtHtCalc();
      montantTotalHtTransfoDevis = totauxCaPeriodeDevisTransforme(codeTiers).getMtHtCalc();
      montantTotalHtNonTransfoDevis = totauxCaPeriodeDevisNonTransforme(codeTiers).getMtHtCalc();
      montantTotalHtNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeTiers).getMtHtCalc();
      montantTotalTtcDevis = totauxCaPeriodeDevis(codeTiers).getMtTtcCalc();
      montantTotalTtcTransfoDevis = totauxCaPeriodeDevisTransforme(codeTiers).getMtTtcCalc();
      montantTotalTtcNonTransfoDevis =  totauxCaPeriodeDevisNonTransforme(codeTiers).getMtTtcCalc();
      montantTotalTtcNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeTiers).getMtTtcCalc();
      nbDevis = nbDevisSurPeriode(codeTiers);
      nbDevisNonTransforme = nbDevisNonTransformeSurPeriode(codeTiers);
      nbDevisTransforme = nbDevisTransformeSurPeriode(codeTiers);
      nbDevisNonTransformeARelancer = nbDevisNonTransformeARelancerSurPeriode(codeTiers);
      //ENCOURS
      nbArticleNonTransforme = nbArticleNonTransformeSurPeriode(codeTiers) ;
  	  montantTotalHtNonTransfoArticle = totauxCaPeriodeArticleNonTransforme(codeTiers).getMtHtCalc();
  }
  
  
	// Retourne le nombre total de documents sur la période
  public int nbGeneralSurPeriode(String codeTiers) {

  	int nb = (int) taDevisService.countDocument(dateDebutDevis, dateFinDevis, codeTiers);
  	return nb;
  }
  
  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneral(String codeTiers) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTotalDTO(dateDebutDevis, dateFinDevis,codeTiers);
  	return totaux;
  }
  
  public void initValueLignesGeneral(String codeTiers) {
  	valueLignesDevis =  taDevisService.findAllDTOPeriode(dateDebutDevis, dateFinDevis,codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireTotalJmaDTO(dateDebutDevis, dateFinDevis, 1,codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));    	
  }

  public void initAllValueGeneral(String codeTiers) {
  	initIndicateursFacture(codeTiers);
  	initIndicateursFactureGeneral(codeTiers);
//	initIndicateursDevis(codeTiers);
//  	initValueLignesDevis(codeTiers);
  	
  	
  }
  
  public int nbGeneralNonTransformeSurPeriode(String codeTiers) {

  	int nb = (int) taDevisService.countDocumentNonTransforme(dateDebutDevis, dateFinDevis, codeTiers);
  	return nb;
  }

  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneralNonTransforme(String codeTiers) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeTiers);
  	return totaux;
  }
  
  public void initValueLignesGeneralTransforme(String codeTiers) {
  	valueLignesDevis =  taDevisService.findDocumentTransfosDTO(dateDebutDevis, dateFinDevis,codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis)); 

  	
  }

  public int nbGeneralTransformeSurPeriode(String codeTiers) {

  	int nb = (int) taDevisService.countDocumentTransforme(dateDebutDevis, dateFinDevis, codeTiers);
  	return nb;
  }


  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneralNonTransformeARelancer(String codeTiers) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutDevis, dateFinDevis, 15, codeTiers);
  	return totaux;
  }
  
  public void initValueLignesGeneralNonTransformeARelancer(String codeTiers) {
  	valueLignesDevis =  taDevisService.findDocumentNonTransfosARelancerDTO(dateDebutDevis, dateFinDevis, 15, codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutDevis, dateFinDevis, 1, 15, codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));     	
  }
  
  public int nbGeneralNonTransformeARelancerSurPeriode(String codeTiers) {

  	int nb = (int) taDevisService.countDocumentNonTransformeARelancer(dateDebutDevis, dateFinDevis, 15, codeTiers);
  	return nb;
  }

  
  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneralTransforme(String codeTiers) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeTiers);
  	return totaux;
  }
  
  public void initValueLignesGeneralNonTransforme(String codeTiers) {
  	valueLignesDevis =  taDevisService.findDocumentNonTransfosDTO(dateDebutDevis, dateFinDevis,codeTiers);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeTiers);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis)); 
  	
  }    
//  Fin section Général
  
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
				param.setOriginePaiement("BDG Dashboard Tiers - Facture");
				PaiementCbController.actDialoguePaiementEcheanceParCarte(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		initIndicateursFacture(codeTiersCourant);
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
  
	public void actImprimerListeLignesParTiers(String nomTiers) {
		
		
		try {
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", getValeursDateDebut());
		mapParametre.put("fin", getValeursDateFin());
		mapParametre.put("nomTiers", nomTiers);
		mapParametre.put("infosLabel", infosLabelDoc);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("lignesParTiers", valeursListe);
		sessionMap.put("nom", valueLignesFacture);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimerListeLignesParTiersReglement(String nomTiers) {
		
		
		try {
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", getValeursDateDebut());
		mapParametre.put("fin", getValeursDateFin());
		mapParametre.put("nomTiers", nomTiers);
		mapParametre.put("infosLabel", infosLabelDoc);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("lignesParTiers", valeursListe);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void actImprimerListeLignesParTiersReglement(String nomTiers, String codeTiers, String codeDoc, String libelleDoc, Date dateDoc, BigDecimal netTtc, BigDecimal resteAAffecter, String codeBanque) {
		
		try {
		
		List<TaRReglementDTO> liste=taRReglementService.rechercheDocumentDTO(codeDoc,codeTiers);
		List<TaReglementDTO> ligneEnTete = new ArrayList<TaReglementDTO>();
		
		TaReglementDTO ligne = new TaReglementDTO();
		ligne.setCodeDocument(codeDoc);
		ligne.setLibelleDocument(libelleDoc);
		ligne.setDateDocument(dateDoc);
		ligne.setNetTtcCalc(netTtc);
		ligne.setResteAAffecter(resteAAffecter);
		ligne.setCodeBanque(codeBanque);
		ligneEnTete.add(ligne);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		
		mapParametre.remove("parametre");
		sessionMap.remove("lignesParTiers");
		sessionMap.remove("listeReglement");
		
		mapParametre.put("debut", getValeursDateDebut());
		mapParametre.put("fin", getValeursDateFin());
		mapParametre.put("nomTiers", nomTiers);
		mapParametre.put("codeTiers", codeTiers);
		mapParametre.put("infosLabel", infosLabelDoc);
		
		sessionMap.put("parametre", mapParametre);
		
		sessionMap.put("lignesParTiers", ligneEnTete);
		sessionMap.put("listeReglement", liste);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actImprimerListeLignesParTiersArticle(String nomTiers) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
//		mapParametre.put("nom", getInfoEntreprise().getTaTiers().getNomTiers());
		mapParametre.put("debut", getDateDebutArticle());
		mapParametre.put("fin", getDateFinArticle());
		mapParametre.put("nomTiers", nomTiers);
		mapParametre.put("infosLabel", "Liste regroupée par articles");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("lignesParTiersArticle", valueLignesArticleRegroupee);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actImprimerListeLignesParTiersArticleHistorique(String nomTiers) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
//		mapParametre.put("nom", getInfoEntreprise().getTaTiers().getNomTiers());
		mapParametre.put("debut", getDateDebutArticle());
		mapParametre.put("fin", getDateFinArticle());
		mapParametre.put("nomTiers", nomTiers);
		mapParametre.put("infosLabel", "Historique des articles achetés");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("lignesParTiersArticle", valueLignesArticle);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actImprimerListeLignesParTiersArticleDetail(String nomTiers) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
//		mapParametre.put("nom", getInfoEntreprise().getTaTiers().getNomTiers());
		
		mapParametre.put("debut", getDateDebutArticle());
		mapParametre.put("fin", getDateFinArticle());
		mapParametre.put("nomTiers", nomTiers);
		mapParametre.put("infosLabel", "Liste des articles regroupés par famille");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("lignesParTiersArticle", valueLignesArticleRegroupeeParFamille);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimerInterfacePrintTiers(String codeTiers, String nomTiers) {
		System.out.println("regroupeArticle "+regroupeArticle);
		System.out.println("histoArticle "+histoArticle);
		System.out.println("familleArticle "+familleArticle);
		
		if(codeTiers!=null) {
	
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
//		Map<String,Object> mapParametreFactureRealise = new HashMap<String,Object>();
		
		sessionMap.remove("tiers");
		sessionMap.remove("parametre");
		sessionMap.remove("parametreFactureRealise");
		sessionMap.remove("parametreFactureNonTotalRegle");
		sessionMap.remove("lignesParTiersFactureRealise");
		sessionMap.remove("lignesParTiersFactureNonTotalregle");
		sessionMap.remove("lignesParTiersFactureARelancer");
		sessionMap.remove("lignesParTiersFactureTotalregle");
		sessionMap.remove("lignesParTiersReglementRealise");
		sessionMap.remove("listeReglementFactureRealise");
		sessionMap.remove("lignesParTiersReglementNonTotalAffecter");
//		sessionMap.remove("listeReglementFactureRealise");
		sessionMap.remove("lignesParTiersReglementNonAffecter");
//		sessionMap.remove("listeReglementFactureRealise");
		sessionMap.remove("lignesParTiersArticle");
		sessionMap.remove("lignesParTiersArticleHistorique");
		sessionMap.remove("lignesParTiersArticleDetail");
		sessionMap.remove("lignesParTiersDevisRealise");
		sessionMap.remove("lignesParTiersDevisNonTransforme");
		sessionMap.remove("lignesParTiersDevisARelancer");
		sessionMap.remove("lignesParTiersDevisTransforme");
		sessionMap.remove("lignesParTiersCommandeRealise");
		sessionMap.remove("lignesParTiersCommandeNonTransforme");
		sessionMap.remove("lignesParTiersCommandeARelancer");
		sessionMap.remove("lignesParTiersCommandeTransforme");
		sessionMap.remove("lignesParTiersLivraisonRealise");
		sessionMap.remove("lignesParTiersLivraisonNonFacture");
		sessionMap.remove("lignesParTiersLivraisonFacture");
		sessionMap.remove("lignesParTiersProformaRealise");
		sessionMap.remove("lignesParTiersProformaNonTransforme");
		sessionMap.remove("lignesParTiersProformaARelancer");
		sessionMap.remove("lignesParTiersProformaTransforme");
		boolean identificationTiers=false;
		boolean breakPageEtatFacture=false;
		boolean breakPageEtatReglement=false;
		boolean breakPageEtatArticle=false;
		boolean breakPageEtatDevis=false;
		boolean breakPageEtatCommande=false;
		boolean breakPageEtatLivraison=false;
		boolean breakPageEtatProforma=false;
		
		if (identification.equals("Aucune")) {
			sessionMap.remove("parametre");
			try {
				selectedTaTiersDTO.setCodeTiers(codeTiers);
				Object tiers = taTiersService.findByCode(selectedTaTiersDTO.getCodeTiers());				
				
				if (tiers!=null) {
					
//					mapParametre.put("boolCommentaire",inclureCommentaire);
					sessionMap.put("parametre", mapParametre);
					sessionMap.put("tiers", tiers);
							
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (identification.equals("Sommaire")) {
			sessionMap.remove("parametre");
			try {
				selectedTaTiersDTO.setCodeTiers(codeTiers);
				Object tiers = taTiersService.findByCode(selectedTaTiersDTO.getCodeTiers());				
				
			if (tiers!=null) {
				identificationTiers=true;
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("identificationTiersSommaire", identificationTiers);
				mapParametre.put("infosLabelIdentification", "Info sommaire du tiers");
			if (isInclureCommentaire()==true) {
				mapParametre.put("boolCommentaire",inclureCommentaire);
			}
				
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("tiers", tiers);
						
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (identification.equals("Etendu")) {
			try {
				selectedTaTiersDTO.setCodeTiers(codeTiers);
				Object tiers = taTiersService.findByCode(selectedTaTiersDTO.getCodeTiers());				
				
			if (tiers!=null) {
				
				nbDevis = nbDevisSurPeriode(codeTiers);
				nbFacture = nbFactureSurPeriode(codeTiers);
				nbProforma = nbProformaSurPeriode(codeTiers);
				nbCommande = nbCommandeSurPeriode(codeTiers);
				nbLivraison = nbLivraisonSurPeriode(codeTiers);
				nbReglement = nbReglementSurPeriode(codeTiers);
				nbArticle = nbArticleSurPeriode(codeTiers);
				montantTotalTtcNonRegleFacture = totauxCaPeriodeFactureNonTransforme(codeTiers).getResteAReglerComplet();
				
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("identificationTiersEtendu", identification);
				mapParametre.put("infosLabelIdentification", "Info étendu du tiers");
				mapParametre.put("devisEncours",nbDevis);
				mapParametre.put("factureEncours",nbFacture);
				mapParametre.put("proformaEncours",nbProforma);
				mapParametre.put("commandeEncours",nbCommande);
				mapParametre.put("livraison",nbLivraison);
				mapParametre.put("articleEncours",nbArticle);
				mapParametre.put("reglementEncours",nbReglement);
				mapParametre.put("resteARegler",montantTotalTtcNonRegleFacture);
			if (isInclureCommentaire()==true) {
				mapParametre.put("boolCommentaire",inclureCommentaire);
			}
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("tiers", tiers);
						
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (factureRealise==true) {
			try {
				valueLignesFacture =  taFactureService.findAllDTOPeriode(dateDebutFacture, dateFinFacture,codeTiers);				
				
//				sessionMap.remove("parametre");
//				sessionMap.remove("valueLignesFacture");
			if (valueLignesFacture!=null) {
				breakPageEtatFacture=true;
				mapParametre.put("debutFactureRealise", getDateDebutFacture());
				mapParametre.put("finFactureRealise", getDateFinFacture());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("factureRealise", factureRealise);
				mapParametre.put("infosLabelFactureRealise", "réalisées");
				mapParametre.put("breakPageEtatFacture", breakPageEtatFacture);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersFactureRealise", valueLignesFacture);
				
				if(inclureReglementFactReal==true) {
					
				}
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (factureNonTotalRegle==true) {
			try {
				valueLignesFactureNonTotalRegle =  taFactureService.findDocumentNonTransfosDTO(dateDebutFacture, dateFinFacture,codeTiers);				
				
//				sessionMap.remove("parametre");
//				sessionMap.remove("valueLignesFactureNonTotalRegle");
			if (valueLignesFactureNonTotalRegle!=null) {
				breakPageEtatFacture=true;
				mapParametre.put("debut", getDateDebutFacture());
				mapParametre.put("fin", getDateFinFacture());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("factureNonTotalRegle", factureNonTotalRegle);
				mapParametre.put("infosLabelFactureNonTotalRegle", "non totalement réglées");
				mapParametre.put("breakPageEtatFacture", breakPageEtatFacture);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersFactureNonTotalRegle", valueLignesFactureNonTotalRegle);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (factureARelancer==true) {
			try {								
				valueLignesFactureARelancer =  taFactureService.findDocumentNonTransfosARelancerDTO(dateDebutFacture, dateFinFacture, 15, codeTiers);
				
//				sessionMap.remove("parametre");
//				sessionMap.remove("valueLignesFactureARelancer");
			if (valueLignesFactureARelancer!=null) {
				breakPageEtatFacture=true;
				mapParametre.put("debut", getDateDebutFacture());
				mapParametre.put("fin", getDateFinFacture());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("factureARelancer", factureARelancer);
				mapParametre.put("infosLabelFactureARelancer", "à relancer");
				mapParametre.put("breakPageEtatFacture", breakPageEtatFacture);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersFactureARelancer", valueLignesFactureARelancer);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }

		if (factureTotalRegle==true) {
			try {
				valueLignesFactureTotalRegle =  taFactureService.findDocumentTransfosDTO(dateDebutFacture, dateFinFacture,codeTiers);				
				
//				sessionMap.remove("parametre");
//				sessionMap.remove("valueLignesFactureTotalRegle");
			if (valueLignesFactureTotalRegle!=null) {
				breakPageEtatFacture=true;
				mapParametre.put("debut", getDateDebutFacture());
				mapParametre.put("fin", getDateFinFacture());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("factureTotalRegle", factureTotalRegle);
				mapParametre.put("infosLabelFactureTotalRegle", "totalement réglées");
				mapParametre.put("breakPageEtatFacture", breakPageEtatFacture);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersFactureTotalRegle", valueLignesFactureTotalRegle);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (reglementRealise==true) {
			try {
				valueLignesReglement = taReglementService.findAllDTOPeriode(dateDebutReglement, dateFinReglement,codeTiers);
//				Map<String,Object> mapFactureDuTiers = new HashMap<String,Object>();
				List<TaRReglementDTO> liste= new ArrayList<>();
				
				for (Object listeFactureDuTiers : valueLignesReglement) {					
					 liste.addAll(taRReglementService.rechercheDocumentDTO(( (TaReglementDTO) listeFactureDuTiers).getCodeDocument(),codeTiers));
//					mapFactureDuTiers.put(((TaReglementDTO) listeFactureDuTiers).getCodeDocument(), liste);
//					System.out.println(((TaReglementDTO) listeFactureDuTiers).getCodeDocument());
				}
				
//				List<Object> listeReglementFactureRealise = new ArrayList<Object>();
//				
//				for(Entry<String, Object> entry : mapFactureDuTiers.entrySet()) {
//					reglementRealiseFacLie.setCodeReglement(entry.getKey());
//					reglementRealiseFacLie.setListeRegRealFactLie( ((List<TaRReglementDTO>) entry.getValue()));
//					
//					listeReglementFactureRealise.add(reglementRealiseFacLie);
//				}
				
			if (valueLignesReglement!=null) {
				breakPageEtatReglement=true;
				mapParametre.put("debut", getDateDebutReglement());
				mapParametre.put("fin", getDateFinReglement());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("reglementRealise", reglementRealise);
				mapParametre.put("infosLabelReglementRealise", "encaissés");
				mapParametre.put("breakPageEtatReglement", breakPageEtatReglement);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersReglementRealise", valueLignesReglement);
				
			if(inclureReglementFactReal==true) {
				
				mapParametre.put("inclureReglementFactReal", inclureReglementFactReal);
				mapParametre.put("infosLabelReglementRealise", "et facture lié à celui-ci");
				//sessionMap.put("listeReglementFactureRealise", listeReglementFactureRealise);
				sessionMap.put("listeReglementFactureRealise", liste);
			}
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (reglementNonTransforme==true) {
			try {
				valueLignesReglementNonTransforme = taReglementService.findDocumentNonTransfosDTO(dateDebutReglement, dateFinReglement,codeTiers);			
				
				List<TaRReglementDTO> liste= new ArrayList<>();
				
				for (Object listeFactureDuTiers : valueLignesReglementNonTransforme) {					
					 liste.addAll(taRReglementService.rechercheDocumentDTO(( (TaReglementDTO) listeFactureDuTiers).getCodeDocument(),codeTiers));
				}

			if (valueLignesReglementNonTransforme!=null) {
				breakPageEtatReglement=true;
				mapParametre.put("debut", getDateDebutReglement());
				mapParametre.put("fin", getDateFinReglement());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("reglementNonTransforme", reglementNonTransforme);
				mapParametre.put("infosLabelReglementPartiellementAffectes", "partiellement affectés");
				mapParametre.put("breakPageEtatReglement", breakPageEtatReglement);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersReglementNonTotalAffecter", valueLignesReglementNonTransforme);
				
			if(inclureFactNonTotReg==true) {
					
				mapParametre.put("inclureFactNonTotReg", inclureFactNonTotReg);
				mapParametre.put("infosLabelReglementPartiellementAffectes", "partiellement affectés et facture lié à celui-ci");
				sessionMap.put("listeReglementPartiellementAffectes", liste);
			}
			
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (reglementARelancer==true) {
			try {								
				valueLignesReglementARelancer =  taReglementService.findDocumentNonTransfosARelancerDTO(dateDebutReglement, dateFinReglement, 0, codeTiers);
				List<TaRReglementDTO> liste=taRReglementService.rechercheDocumentDTO(codeTiers, nomTiers);
				
//				sessionMap.remove("parametre");
//				sessionMap.remove("valueLignesFactureARelancer");
			if (valueLignesReglementARelancer!=null) {
				breakPageEtatReglement=true;
				mapParametre.put("debut", getDateDebutReglement());
				mapParametre.put("fin", getDateFinReglement());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("reglementARelancer", reglementARelancer);
				mapParametre.put("infosLabelReglementNonAffectes", "non affectés");
				mapParametre.put("breakPageEtatReglement", breakPageEtatReglement);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersReglementNonAffecter", valueLignesReglementARelancer);//facture
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (regroupeArticle==true) {
		try {
			valueLignesArticleRegroupee = taFactureService.findArticlesParTiersParMoisParTypeRegroupement(dateDebutArticle,dateFinArticle,codeTiers, Const.PAR_ARTICLE, "%", true);			
			
//			sessionMap.remove("parametre");
//			sessionMap.remove("lignesParTiersArticle");
		if (valueLignesArticleRegroupee!=null) {
			breakPageEtatArticle=true;
			mapParametre.put("debut", getDateDebutArticle());
			mapParametre.put("fin", getDateFinArticle());
			mapParametre.put("nomTiers", nomTiers);
			mapParametre.put("regroupeArticle", regroupeArticle);
			mapParametre.put("infosLabelArticle", "articles");
			mapParametre.put("breakPageEtatArticle", breakPageEtatArticle);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParTiersArticle", valueLignesArticleRegroupee);
						
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
		
		if (histoArticle==true) {
			try {
				valueLignesArticle = taFactureService.findArticlesParTiersParMois(dateDebutArticle,dateFinArticle,codeTiers);				
			
//			sessionMap.remove("parametre");
//			sessionMap.remove("lignesParTiersArticleHistorique");
			if (valueLignesArticle!=null) {
				breakPageEtatArticle=true;
				mapParametre.put("debut", getDateDebutArticle());
				mapParametre.put("fin", getDateFinArticle());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("histoArticle", histoArticle);
				mapParametre.put("infosLabelArticleHisto", "achetés");
				mapParametre.put("breakPageEtatArticle", breakPageEtatArticle);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersArticleHistorique", valueLignesArticle);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (familleArticle==true) {
			try {
				valueLignesArticleRegroupeeParFamille = taFactureService.findArticlesParTiersParMoisParTypeRegroupement(dateDebutArticle,dateFinArticle,codeTiers, Const.PAR_FAMILLE_ARTICLE, "%", true);				
			
//			sessionMap.remove("parametre");
//			sessionMap.remove("lignesParTiersArticleDetail");
			if (valueLignesArticleRegroupeeParFamille!=null) {
				breakPageEtatArticle=true;
				mapParametre.put("debut", getDateDebutArticle());
				mapParametre.put("fin", getDateFinArticle());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("familleArticle", familleArticle);
				mapParametre.put("infosLabelArticleDetail", "regroupés par famille");
				mapParametre.put("breakPageEtatArticle", breakPageEtatArticle);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersArticleDetail", valueLignesArticleRegroupeeParFamille);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (devisRealise==true) {
			try {
				valueLignesDevis =  taDevisService.findAllDTOPeriode(dateDebutDevis, dateFinDevis,codeTiers);				
			
//			sessionMap.remove("parametre");
//			sessionMap.remove("lignesParTiersArticleDetail");
			if (valueLignesDevis!=null) {
				breakPageEtatDevis=true;
				mapParametre.put("debut", getDateDebutDevis());
				mapParametre.put("fin", getDateFinDevis());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("devisRealise", devisRealise);
				mapParametre.put("infosLabeldevisRealise", "Liste des devis Réalisé du tiers ");
				mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersDevisRealise", valueLignesDevis);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (devisNonTransforme==true) {
			try {
				valueLignesDevisNonTransforme =  taDevisService.findDocumentNonTransfosDTO(dateDebutDevis, dateFinDevis,codeTiers);				
			
//			sessionMap.remove("parametre");
//			sessionMap.remove("lignesParTiersArticleDetail");
			if (valueLignesDevisNonTransforme!=null) {
				breakPageEtatDevis=true;
				mapParametre.put("debut", getDateDebutDevis());
				mapParametre.put("fin", getDateFinDevis());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("devisNonTransforme", devisNonTransforme);
				mapParametre.put("infosLabeldevisNonTransforme", "Devis non transformés du tiers ");
				mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersDevisNonTransforme", valueLignesDevisNonTransforme);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (devisARelancer==true) {
			try {
				valueLignesDevisARelancer =  taDevisService.findDocumentNonTransfosARelancerDTO(dateDebutDevis, dateFinDevis, 15, codeTiers);				
			
//			sessionMap.remove("parametre");
//			sessionMap.remove("lignesParTiersArticleDetail");
			if (valueLignesDevisARelancer!=null) {
				breakPageEtatDevis=true;
				mapParametre.put("debut", getDateDebutDevis());
				mapParametre.put("fin", getDateFinDevis());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("devisARelancer", devisARelancer);
				mapParametre.put("infosLabeldevisARelancer", "Devis à relancer du tiers ");
				mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersDevisARelancer", valueLignesDevisARelancer);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (devisTransforme==true) {
			try {
				valueLignesDevisTransforme =  taDevisService.findDocumentTransfosDTO(dateDebutDevis, dateFinDevis,codeTiers);				
			
//			sessionMap.remove("parametre");
//			sessionMap.remove("lignesParTiersArticleDetail");
			if (valueLignesDevisTransforme!=null) {
				breakPageEtatDevis=true;
				mapParametre.put("debut", getDateDebutDevis());
				mapParametre.put("fin", getDateFinDevis());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("devisTransforme", devisTransforme);
				mapParametre.put("infosLabeldevisTransforme", "Devis tranformés du tiers ");
				mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersDevisTransforme", valueLignesDevisTransforme);
							
			}
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (commandeRealise==true) {
			try {
				valueLignesCommande =  taBoncdeService.findAllDTOPeriode(dateDebutCommande, dateFinCommande,codeTiers);				
			
			if (valueLignesCommande!=null) {
				breakPageEtatCommande=true;
				mapParametre.put("debut", getDateDebutCommande());
				mapParametre.put("fin", getDateFinCommande());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("commandeRealise", commandeRealise);
				mapParametre.put("infosLabelcommandeRealise", "Commande Réalisé du tiers ");
				mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersCommandeRealise", valueLignesCommande);							
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (commandeNonTransforme==true) {
			try {
				valueLignesCommandeNonTransforme =  taBoncdeService.findDocumentNonTransfosDTO(dateDebutCommande, dateFinCommande,codeTiers);				
			
			if (valueLignesCommandeNonTransforme!=null) {
				breakPageEtatCommande=true;
				mapParametre.put("debut", getDateDebutCommande());
				mapParametre.put("fin", getDateFinCommande());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("commandeNonTransforme", commandeNonTransforme);
				mapParametre.put("infosLabelcommandeNonTransforme", "Commande non tranformés du tiers ");
				mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersCommandeNonTransforme", valueLignesCommandeNonTransforme);						
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (commandeARelancer==true) {
			try {
				valueLignesCommandeARelancer =  taBoncdeService.findDocumentNonTransfosARelancerDTO(dateDebutCommande, dateFinCommande, 15, codeTiers);				
			
			if (valueLignesCommandeARelancer!=null) {
				breakPageEtatCommande=true;
				mapParametre.put("debut", getDateDebutCommande());
				mapParametre.put("fin", getDateFinCommande());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("commandeARelancer", commandeARelancer);
				mapParametre.put("infosLabelcommandeARelancer", "Commande à relancer du tiers ");
				mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersCommandeARelancer", valueLignesCommandeARelancer);				
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (commandeTransforme==true) {
			try {
				valueLignesCommandeTransforme =  taBoncdeService.findDocumentTransfosDTO(dateDebutCommande, dateFinCommande,codeTiers);				
			
			if (valueLignesCommandeTransforme!=null) {
				breakPageEtatCommande=true;
				mapParametre.put("debut", getDateDebutCommande());
				mapParametre.put("fin", getDateFinCommande());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("commandeTransforme", commandeTransforme);
				mapParametre.put("infosLabelcommandeTransforme", "Commande tranformés du tiers ");
				mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersCommandeTransforme", valueLignesCommandeTransforme);							
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (livraisonRealise==true) {
			try {
				valueLignesLivraison =  taBonlivService.findAllDTOPeriode(dateDebutLivraison, dateFinLivraison,codeTiers);				
			
			if (valueLignesLivraison!=null) {
				breakPageEtatLivraison=true;
				mapParametre.put("debut", getDateDebutCommande());
				mapParametre.put("fin", getDateFinCommande());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("livraisonRealise", livraisonRealise);
				mapParametre.put("infosLabellivraisonRealise", "Livraison Réalisé du tiers ");
				mapParametre.put("breakPageEtatLivraison", breakPageEtatLivraison);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersLivraisonRealise", valueLignesLivraison);							
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (livraisonNonFacture==true) {
			try {
				valueLignesLivraisonNonFacture =  taBonlivService.findDocumentNonTransfosDTO(dateDebutLivraison, dateFinLivraison,codeTiers);			
			
			if (valueLignesLivraisonNonFacture!=null) {
				breakPageEtatLivraison=true;
				mapParametre.put("debut", getDateDebutLivraison());
				mapParametre.put("fin", getDateFinLivraison());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("livraisonNonFacture", livraisonNonFacture);
				mapParametre.put("infosLabellivraisonNonFacture", "Livraison non facturés du tiers ");
				mapParametre.put("breakPageEtatLivraison", breakPageEtatLivraison);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersLivraisonNonFacture", valueLignesLivraisonNonFacture);						
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (livraisonFacture==true) {
			try {
				valueLignesLivraisonFacture =  taBonlivService.findDocumentTransfosDTO(dateDebutLivraison, dateFinLivraison,codeTiers);				
			
			if (valueLignesLivraisonFacture!=null) {
				breakPageEtatLivraison=true;
				mapParametre.put("debut", getDateDebutLivraison());
				mapParametre.put("fin", getDateFinLivraison());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("livraisonFacture", livraisonFacture);
				mapParametre.put("infosLabellivraisonFacture", "Livraison facturés du tiers ");
				mapParametre.put("breakPageEtatLivraison", breakPageEtatLivraison);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersLivraisonFacture", valueLignesLivraisonFacture);							
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		if (proformaRealise==true) {
			try {
				valueLignesProforma =  taProformaService.findAllDTOPeriode(dateDebutProforma, dateFinProforma,codeTiers);				
			
			if (valueLignesProforma!=null) {
				breakPageEtatProforma=true;
				mapParametre.put("debut", getDateDebutProforma());
				mapParametre.put("fin", getDateFinProforma());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("proformaRealise", proformaRealise);
				mapParametre.put("infosLabelproformaRealise", "Proforma Réalisé du tiers ");
				mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersProformaRealise", valueLignesProforma);							
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (proformaNonTransforme==true) {
			try {
				valueLignesProformaNonTransforme =  taProformaService.findDocumentNonTransfosDTO(dateDebutProforma, dateFinProforma,codeTiers);				
			
			if (valueLignesProformaNonTransforme!=null) {
				breakPageEtatProforma=true;
				mapParametre.put("debut", getDateDebutProforma());
				mapParametre.put("fin", getDateFinProforma());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("proformaNonTransforme", proformaNonTransforme);
				mapParametre.put("infosLabelproformaNonTransforme", "Proforma non tranformés du tiers ");
				mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersProformaNonTransforme", valueLignesProformaNonTransforme);						
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (proformaARelancer==true) {
			try {
				valueLignesProformaARelancer =  taProformaService.findDocumentNonTransfosARelancerDTO(dateDebutProforma, dateFinProforma, 15, codeTiers);				
			
			if (valueLignesProformaARelancer!=null) {
				breakPageEtatProforma=true;
				mapParametre.put("debut", getDateDebutCommande());
				mapParametre.put("fin", getDateFinCommande());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("proformaARelancer", proformaARelancer);
				mapParametre.put("infosLabelproformaARelancer", "Proforma à relancer du tiers ");
				mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersProformaARelancer", valueLignesProformaARelancer);				
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		if (proformaTransforme==true) {
			try {
				valueLignesProformaTransforme =  taProformaService.findDocumentTransfosDTO(dateDebutProforma, dateFinProforma,codeTiers);				
			
			if (valueLignesProformaTransforme!=null) {
				breakPageEtatProforma=true;
				mapParametre.put("debut", getDateDebutProforma());
				mapParametre.put("fin", getDateFinProforma());
				mapParametre.put("nomTiers", nomTiers);
				mapParametre.put("proformaTransforme", proformaTransforme);
				mapParametre.put("infosLabelproformaTransforme", "Proforma tranformés du tiers ");
				mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("lignesParTiersProformaTransforme", valueLignesProformaTransforme);							
			}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		  }
		
		}
		
	}
	
	
	public void decocher() {
		
		identification="Aucune";
		factureRealise=false;
		factureNonTotalRegle=false;
		factureTotalRegle=false;
		factureARelancer=false;
		inclureReglementFactReal=false;
		inclureReglementNonTotReg=false;
		reglementRealise=false;
		reglementNonTransforme=false;
		reglementARelancer=false;
		inclureFactRegle=false;
		inclureFactNonTotReg=false;
		inclureFactTotReg=false;
		histoArticle=false;
		regroupeArticle=false;
		familleArticle=false;
		devisRealise=false;
		devisNonTransforme=false;
		devisARelancer=false;
		devisTransforme=false;
		commandeRealise=false;
		commandeNonTransforme=false;
		commandeARelancer=false;
		commandeTransforme=false;
		livraisonRealise=false;
		livraisonNonFacture=false;
		livraisonFacture=false;
		proformaRealise=false;
		proformaNonTransforme=false;
		proformaARelancer=false;
		proformaTransforme=false;
		inclureCommentaire=false;
		
	}
	
public void cocher() {
		
		identification="Etendu";
		factureRealise=true;
		factureNonTotalRegle=true;
		factureTotalRegle=true;
		factureARelancer=true;
		inclureReglementFactReal=true;
		inclureReglementNonTotReg=true;
		reglementRealise=true;
		reglementNonTransforme=true;
		reglementARelancer=true;
		inclureFactRegle=true;
		inclureFactNonTotReg=true;
		inclureFactTotReg=true;
		histoArticle=true;
		regroupeArticle=true;
		familleArticle=true;
		devisRealise=true;
		devisNonTransforme=true;
		devisARelancer=true;
		devisTransforme=true;
		commandeRealise=true;
		commandeNonTransforme=true;
		commandeARelancer=true;
		commandeTransforme=true;
		livraisonRealise=true;
		livraisonNonFacture=true;
		livraisonFacture=true;
		proformaRealise=true;
		proformaNonTransforme=true;
		proformaARelancer=true;
		proformaTransforme=true;
		inclureCommentaire=true;
		
	}
	
	/**
	 * Méthode appelée au clic sur un onglet des listes dans l'onglet Articles achetés
	 * Affiche la graphique correspondant à l'onglet actif et cache (rendered) les autres
	 * **/
	public void onTabListeChange(TabChangeEvent event) {     
        if(event != null && event.getTab() != null && event.getTab().getTitle() != null) {
        	if(event.getTab().getTitle().equals(titreTabListeRegroupeeParArticle)) {
        		afficheGraphFamille = false;
            	afficheGraphParArticle = true;
        	}else if(event.getTab().getTitle().equals(titreTabListeRegroupeeParFamille)) {
        		afficheGraphFamille = true;
            	afficheGraphParArticle = false;
        	}else {
        		afficheGraphFamille = false;
            	afficheGraphParArticle = false;
        	}
        }
    }
	
	
	public List<DocumentDTO> getValueLignesProforma() {
		return valueLignesProforma;
	}

    public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelTiers() {
		return barChartModelTiers;
	}
	public void setBarChartModelTiersExo(BarChartModel barChartModelTiers) {
		this.barChartModelTiers = barChartModelTiers;
	}
	


	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public String getCodeEtatTiers() {
		return codeEtatTiers;
	}

	public void setCodeEtatTiers(String codeEtatTiers) {
		this.codeEtatTiers = codeEtatTiers;
	}

	public List<TaTiersDTO> getValueLignesTiers() {
		return valueLignesTiers;
	}

	public void setValueLignesTiers(List<TaTiersDTO> valueLignesTiers) {
		this.valueLignesTiers = valueLignesTiers;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeTiers() {
		return listeCaPeriodeTiers;
	}

	public void setListeCaPeriodeTiers(List<DocumentChiffreAffaireDTO> listeCaPeriodeTiers) {
		this.listeCaPeriodeTiers = listeCaPeriodeTiers;
	}

	public int getNbTiersActif() {
		return nbTiersActif;
	}

	public void setNbTiersActif(int nbTiersActif) {
		this.nbTiersActif = nbTiersActif;
	}

	public int getNbTiersNonTransformeSurPeriode() {
		return nbTiersNonTransformeSurPeriode;
	}

	public void setNbTiersNonTransformeSurPeriode(int nbTiersNonTransformeSurPeriode) {
		this.nbTiersNonTransformeSurPeriode = nbTiersNonTransformeSurPeriode;
	}


	public int getNbTiersNonTransformeARelancerSurPeriode() {
		return nbTiersNonTransformeARelancerSurPeriode;
	}


	public void setNbTiersNonTransformeARelancerSurPeriode(int nbTiersNonTransformeARelancerSurPeriode) {
		this.nbTiersNonTransformeARelancerSurPeriode = nbTiersNonTransformeARelancerSurPeriode;
	}


	public String getcSbNbTiersSurPeriode() {
		return cSbNbTiersSurPeriode;
	}


	public void setcSbNbTiersSurPeriode(String cSbNbTiersSurPeriode) {
		this.cSbNbTiersSurPeriode = cSbNbTiersSurPeriode;
	}


	public String getcSbNbTiersNonTransformeSurPeriode() {
		return cSbNbTiersNonTransformeSurPeriode;
	}


	public void setcSbNbTiersNonTransformeSurPeriode(String cSbNbTiersNonTransformeSurPeriode) {
		this.cSbNbTiersNonTransformeSurPeriode = cSbNbTiersNonTransformeSurPeriode;
	}


	public String getcSbNbTiersNonTransformeARelancerSurPeriode() {
		return cSbNbTiersNonTransformeARelancerSurPeriode;
	}


	public void setcSbNbTiersNonTransformeARelancerSurPeriode(String cSbNbTiersNonTransformeARelancerSurPeriode) {
		this.cSbNbTiersNonTransformeARelancerSurPeriode = cSbNbTiersNonTransformeARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeTiersNonTransforme() {
		return listeCaPeriodeTiersNonTransforme;
	}


	public void setListeCaPeriodeTiersNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeTiersNonTransforme) {
		this.listeCaPeriodeTiersNonTransforme = listeCaPeriodeTiersNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbTiersTransformeSurPeriode() {
		return nbTiersTransformeSurPeriode;
	}


	public void setNbTiersTransformeSurPeriode(int nbTiersTransformeSurPeriode) {
		this.nbTiersTransformeSurPeriode = nbTiersTransformeSurPeriode;
	}


	public BigDecimal getmontantTotalHtNonTransfoARelancer() {
		return montantTotalHtNonTransfoARelancer;
	}


	public void setmontantTotalHtNonTransfoARelancer(BigDecimal montantTotalHtNonTransfoARelancer) {
		this.montantTotalHtNonTransfoARelancer = montantTotalHtNonTransfoARelancer;
	}


	public BigDecimal getmontantTotalHtTransfo() {
		return montantTotalHtTransfo;
	}


	public void setmontantTotalHtTransfo(BigDecimal montantTotalHtTransfo) {
		this.montantTotalHtTransfo = montantTotalHtTransfo;
	}


	public String getcSbNbTiersTransformeSurPeriode() {
		return cSbNbTiersTransformeSurPeriode;
	}


	public void setcSbNbTiersTransformeSurPeriode(String cSbNbTiersTransformeSurPeriode) {
		this.cSbNbTiersTransformeSurPeriode = cSbNbTiersTransformeSurPeriode;
	}


	public BigDecimal getmontantTotalHt() {
		return montantTotalHt;
	}


	public void setmontantTotalHt(BigDecimal montantTotalHt) {
		this.montantTotalHt = montantTotalHt;
	}


	public BigDecimal getmontantTotalHtNonTransfo() {
		return montantTotalHtNonTransfo;
	}


	public void setmontantTotalHtNonTransfo(BigDecimal montantTotalHtNonTransfo) {
		this.montantTotalHtNonTransfo = montantTotalHtNonTransfo;
	}


	public Date getDateDebutProforma() {
		return dateDebutProforma;
	}


	public void setDateDebutProforma(Date dateDebutProforma) {
		this.dateDebutProforma = dateDebutProforma;
	}


	public Date getDateFinProforma() {
		return dateFinProforma;
	}


	public void setDateFinProforma(Date dateFinProforma) {
		this.dateFinProforma = dateFinProforma;
	}


	public Date getDateDebutFacture() {
		return dateDebutFacture;
	}


	public void setDateDebutFacture(Date dateDebutFacture) {
		this.dateDebutFacture = dateDebutFacture;
	}


	public Date getDateFinFacture() {
		return dateFinFacture;
	}


	public void setDateFinFacture(Date dateFinFacture) {
		this.dateFinFacture = dateFinFacture;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentFacture() {
		return listeCaMoisDocumentFacture;
	}


	public void setListeCaMoisDocumentFacture(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentFacture) {
		this.listeCaMoisDocumentFacture = listeCaMoisDocumentFacture;
	}


	public List<DocumentDTO> getValueLignesFacture() {
		return valueLignesFacture;
	}


	public void setValueLignesFacture(List<DocumentDTO> valueLignesFacture) {
		this.valueLignesFacture = valueLignesFacture;
	}


	public BigDecimal getmontantTotalHtFacture() {
		return montantTotalHtFacture;
	}


	public void setmontantTotalHtFacture(BigDecimal montantTotalHtFacture) {
		this.montantTotalHtFacture = montantTotalHtFacture;
	}


	public BigDecimal getmontantTotalHtTransfoFacture() {
		return montantTotalHtTransfoFacture;
	}


	public void setmontantTotalHtTransfoFacture(BigDecimal montantTotalHtTransfoFacture) {
		this.montantTotalHtTransfoFacture = montantTotalHtTransfoFacture;
	}


	public BigDecimal getmontantTotalHtNonTransfoFacture() {
		return montantTotalHtNonTransfoFacture;
	}


	public void setmontantTotalHtNonTransfoFacture(BigDecimal montantTotalHtNonTransfoFacture) {
		this.montantTotalHtNonTransfoFacture = montantTotalHtNonTransfoFacture;
	}


	public BigDecimal getmontantTotalHtNonTransfoARelancerFacture() {
		return montantTotalHtNonTransfoARelancerFacture;
	}


	public void setmontantTotalHtNonTransfoARelancerFacture(BigDecimal montantTotalHtNonTransfoARelancerFacture) {
		this.montantTotalHtNonTransfoARelancerFacture = montantTotalHtNonTransfoARelancerFacture;
	}


	public int getNbFacture() {
		return nbFacture;
	}


	public void setNbFacture(int nbFacture) {
		this.nbFacture = nbFacture;
	}


	public int getNbFactureNonTransforme() {
		return nbFactureNonTransforme;
	}


	public void setNbFactureNonTransforme(int nbFactureNonTransforme) {
		this.nbFactureNonTransforme = nbFactureNonTransforme;
	}


	public int getNbFactureTransforme() {
		return nbFactureTransforme;
	}


	public void setNbFactureTransforme(int nbFactureTransforme) {
		this.nbFactureTransforme = nbFactureTransforme;
	}


	public int getNbFactureNonTransformeARelancer() {
		return nbFactureNonTransformeARelancer;
	}


	public void setNbFactureNonTransformeARelancer(int nbFactureNonTransformeARelancer) {
		this.nbFactureNonTransformeARelancer = nbFactureNonTransformeARelancer;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentProforma() {
		return listeCaMoisDocumentProforma;
	}


	public void setListeCaMoisDocumentProforma(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentProforma) {
		this.listeCaMoisDocumentProforma = listeCaMoisDocumentProforma;
	}


	public BigDecimal getmontantTotalHtProforma() {
		return montantTotalHtProforma;
	}


	public void setmontantTotalHtProforma(BigDecimal montantTotalHtProforma) {
		this.montantTotalHtProforma = montantTotalHtProforma;
	}


	public BigDecimal getmontantTotalHtTransfoProforma() {
		return montantTotalHtTransfoProforma;
	}


	public void setmontantTotalHtTransfoProforma(BigDecimal montantTotalHtTransfoProforma) {
		this.montantTotalHtTransfoProforma = montantTotalHtTransfoProforma;
	}


	public BigDecimal getmontantTotalHtNonTransfoProforma() {
		return montantTotalHtNonTransfoProforma;
	}


	public void setmontantTotalHtNonTransfoProforma(BigDecimal montantTotalHtNonTransfoProforma) {
		this.montantTotalHtNonTransfoProforma = montantTotalHtNonTransfoProforma;
	}


	public BigDecimal getmontantTotalHtNonTransfoARelancerProforma() {
		return montantTotalHtNonTransfoARelancerProforma;
	}


	public void setmontantTotalHtNonTransfoARelancerProforma(BigDecimal montantTotalHtNonTransfoARelancerProforma) {
		this.montantTotalHtNonTransfoARelancerProforma = montantTotalHtNonTransfoARelancerProforma;
	}


	public int getNbProforma() {
		return nbProforma;
	}


	public void setNbProforma(int nbProforma) {
		this.nbProforma = nbProforma;
	}


	public int getNbProformaNonTransforme() {
		return nbProformaNonTransforme;
	}


	public void setNbProformaNonTransforme(int nbProformaNonTransforme) {
		this.nbProformaNonTransforme = nbProformaNonTransforme;
	}


	public int getNbProformaTransforme() {
		return nbProformaTransforme;
	}


	public void setNbProformaTransforme(int nbProformaTransforme) {
		this.nbProformaTransforme = nbProformaTransforme;
	}


	public int getNbProformaNonTransformeARelancer() {
		return nbProformaNonTransformeARelancer;
	}


	public void setNbProformaNonTransformeARelancer(int nbProformaNonTransformeARelancer) {
		this.nbProformaNonTransformeARelancer = nbProformaNonTransformeARelancer;
	}


	public void setValueLignesProforma(List<DocumentDTO> valueLignesProforma) {
		this.valueLignesProforma = valueLignesProforma;
	}


	public String getCodeTiersCourant() {
		return codeTiersCourant;
	}


	public void setCodeTiersCourant(String codeTiersCourant) {
		this.codeTiersCourant = codeTiersCourant;
	}


	public BarChartModel getBarChartModelFacture() {
		return barChartModelFacture;
	}


	public void setBarChartModelFacture(BarChartModel barChartModelFacture) {
		this.barChartModelFacture = barChartModelFacture;
	}


	public BarChartModel getBarChartModelProforma() {
		return barChartModelProforma;
	}


	public void setBarChartModelProforma(BarChartModel barChartModelProforma) {
		this.barChartModelProforma = barChartModelProforma;
	}


	public BigDecimal getMontantTotalTtcNonRegleFacture() {
		return montantTotalTtcNonRegleFacture;
	}


	public void setMontantTotalTtcNonRegleFacture(BigDecimal montantTotalTtcNonRegleFacture) {
		this.montantTotalTtcNonRegleFacture = montantTotalTtcNonRegleFacture;
	}

	public Date getDateDebutDevis() {
		return dateDebutDevis;
	}

	public void setDateDebutDevis(Date dateDebutDevis) {
		this.dateDebutDevis = dateDebutDevis;
	}

	public Date getDateFinDevis() {
		return dateFinDevis;
	}

	public void setDateFinDevis(Date dateFinDevis) {
		this.dateFinDevis = dateFinDevis;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentDevis() {
		return listeCaMoisDocumentDevis;
	}

	public void setListeCaMoisDocumentDevis(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentDevis) {
		this.listeCaMoisDocumentDevis = listeCaMoisDocumentDevis;
	}

	public List<DocumentDTO> getValueLignesDevis() {
		return valueLignesDevis;
	}

	public void setValueLignesDevis(List<DocumentDTO> valueLignesDevis) {
		this.valueLignesDevis = valueLignesDevis;
	}

	public BarChartModel getBarChartModelDevis() {
		return barChartModelDevis;
	}

	public void setBarChartModelDevis(BarChartModel barChartModelDevis) {
		this.barChartModelDevis = barChartModelDevis;
	}

	public BigDecimal getMontantTotalHtDevis() {
		return montantTotalHtDevis;
	}

	public void setMontantTotalHtDevis(BigDecimal montantTotalHtDevis) {
		this.montantTotalHtDevis = montantTotalHtDevis;
	}

	public int getNbDevis() {
		return nbDevis;
	}

	public void setNbDevis(int nbDevis) {
		this.nbDevis = nbDevis;
	}

	public int getNbDevisNonTransforme() {
		return nbDevisNonTransforme;
	}

	public void setNbDevisNonTransforme(int nbDevisNonTransforme) {
		this.nbDevisNonTransforme = nbDevisNonTransforme;
	}

	public int getNbDevisTransforme() {
		return nbDevisTransforme;
	}

	public void setNbDevisTransforme(int nbDevisTransforme) {
		this.nbDevisTransforme = nbDevisTransforme;
	}

	public int getNbDevisNonTransformeARelancer() {
		return nbDevisNonTransformeARelancer;
	}

	public void setNbDevisNonTransformeARelancer(int nbDevisNonTransformeARelancer) {
		this.nbDevisNonTransformeARelancer = nbDevisNonTransformeARelancer;
	}

	public void setBarChartModelTiers(BarChartModel barChartModelTiers) {
		this.barChartModelTiers = barChartModelTiers;
	}

	public BigDecimal getMontantTotalHtTransfoDevis() {
		return montantTotalHtTransfoDevis;
	}

	public void setMontantTotalHtTransfoDevis(BigDecimal montantTotalHtTransfoDevis) {
		this.montantTotalHtTransfoDevis = montantTotalHtTransfoDevis;
	}

	public BigDecimal getMontantTotalHtNonTransfoDevis() {
		return montantTotalHtNonTransfoDevis;
	}

	public void setMontantTotalHtNonTransfoDevis(BigDecimal montantTotalHtNonTransfoDevis) {
		this.montantTotalHtNonTransfoDevis = montantTotalHtNonTransfoDevis;
	}

	public BigDecimal getMontantTotalHtNonTransfoARelancerDevis() {
		return montantTotalHtNonTransfoARelancerDevis;
	}

	public void setMontantTotalHtNonTransfoARelancerDevis(BigDecimal montantTotalHtNonTransfoARelancerDevis) {
		this.montantTotalHtNonTransfoARelancerDevis = montantTotalHtNonTransfoARelancerDevis;
	}

	public Date getDateDebutGeneral() {
		return dateDebutGeneral;
	}

	public void setDateDebutGeneral(Date dateDebutGeneral) {
		this.dateDebutGeneral = dateDebutGeneral;
	}

	public Date getDateFinGeneral() {
		return dateFinGeneral;
	}

	public void setDateFinGeneral(Date dateFinGeneral) {
		this.dateFinGeneral = dateFinGeneral;
	}

	public BarChartModel getBarChartModelGeneral() {
		return barChartModelGeneral;
	}

	public void setBarChartModelGeneral(BarChartModel barChartModelGeneral) {
		this.barChartModelGeneral = barChartModelGeneral;
	}

	public Date getDateDebutCommande() {
		return dateDebutCommande;
	}

	public void setDateDebutCommande(Date dateDebutCommande) {
		this.dateDebutCommande = dateDebutCommande;
	}

	public Date getDateFinCommande() {
		return dateFinCommande;
	}

	public void setDateFinCommande(Date dateFinCommande) {
		this.dateFinCommande = dateFinCommande;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentCommande() {
		return listeCaMoisDocumentCommande;
	}

	public void setListeCaMoisDocumentCommande(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentCommande) {
		this.listeCaMoisDocumentCommande = listeCaMoisDocumentCommande;
	}

	public List<DocumentDTO> getValueLignesCommande() {
		return valueLignesCommande;
	}

	public void setValueLignesCommande(List<DocumentDTO> valueLignesCommande) {
		this.valueLignesCommande = valueLignesCommande;
	}

	public BigDecimal getMontantTotalHtCommande() {
		return montantTotalHtCommande;
	}

	public void setMontantTotalHtCommande(BigDecimal montantTotalHtCommande) {
		this.montantTotalHtCommande = montantTotalHtCommande;
	}

	public BigDecimal getMontantTotalHtTransfoCommande() {
		return montantTotalHtTransfoCommande;
	}

	public void setMontantTotalHtTransfoCommande(BigDecimal montantTotalHtTransfoCommande) {
		this.montantTotalHtTransfoCommande = montantTotalHtTransfoCommande;
	}

	public BigDecimal getMontantTotalHtNonTransfoCommande() {
		return montantTotalHtNonTransfoCommande;
	}

	public void setMontantTotalHtNonTransfoCommande(BigDecimal montantTotalHtNonTransfoCommande) {
		this.montantTotalHtNonTransfoCommande = montantTotalHtNonTransfoCommande;
	}

	public BigDecimal getMontantTotalHtNonTransfoARelancerCommande() {
		return montantTotalHtNonTransfoARelancerCommande;
	}

	public void setMontantTotalHtNonTransfoARelancerCommande(BigDecimal montantTotalHtNonTransfoARelancerCommande) {
		this.montantTotalHtNonTransfoARelancerCommande = montantTotalHtNonTransfoARelancerCommande;
	}

	public int getNbCommande() {
		return nbCommande;
	}

	public void setNbCommande(int nbCommande) {
		this.nbCommande = nbCommande;
	}

	public int getNbCommandeNonTransforme() {
		return nbCommandeNonTransforme;
	}

	public void setNbCommandeNonTransforme(int nbCommandeNonTransforme) {
		this.nbCommandeNonTransforme = nbCommandeNonTransforme;
	}

	public int getNbCommandeTransforme() {
		return nbCommandeTransforme;
	}

	public void setNbCommandeTransforme(int nbCommandeTransforme) {
		this.nbCommandeTransforme = nbCommandeTransforme;
	}

	public int getNbCommandeNonTransformeARelancer() {
		return nbCommandeNonTransformeARelancer;
	}

	public void setNbCommandeNonTransformeARelancer(int nbCommandeNonTransformeARelancer) {
		this.nbCommandeNonTransformeARelancer = nbCommandeNonTransformeARelancer;
	}

	public BarChartModel getBarChartModelCommande() {
		return barChartModelCommande;
	}

	public void setBarChartModelCommande(BarChartModel barChartModelCommande) {
		this.barChartModelCommande = barChartModelCommande;
	}

	public Date getDateDebutLivraison() {
		return dateDebutLivraison;
	}

	public void setDateDebutLivraison(Date dateDebutLivraison) {
		this.dateDebutLivraison = dateDebutLivraison;
	}

	public Date getDateFinLivraison() {
		return dateFinLivraison;
	}

	public void setDateFinLivraison(Date dateFinLivraison) {
		this.dateFinLivraison = dateFinLivraison;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentLivraison() {
		return listeCaMoisDocumentLivraison;
	}

	public void setListeCaMoisDocumentLivraison(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentLivraison) {
		this.listeCaMoisDocumentLivraison = listeCaMoisDocumentLivraison;
	}

	public List<DocumentDTO> getValueLignesLivraison() {
		return valueLignesLivraison;
	}

	public void setValueLignesLivraison(List<DocumentDTO> valueLignesLivraison) {
		this.valueLignesLivraison = valueLignesLivraison;
	}

	public BarChartModel getBarChartModelLivraison() {
		return barChartModelLivraison;
	}

	public void setBarChartModelLivraison(BarChartModel barChartModelLivraison) {
		this.barChartModelLivraison = barChartModelLivraison;
	}

	public BigDecimal getMontantTotalHtLivraison() {
		return montantTotalHtLivraison;
	}

	public void setMontantTotalHtLivraison(BigDecimal montantTotalHtLivraison) {
		this.montantTotalHtLivraison = montantTotalHtLivraison;
	}

	public BigDecimal getMontantTotalHtTransfoLivraison() {
		return montantTotalHtTransfoLivraison;
	}

	public void setMontantTotalHtTransfoLivraison(BigDecimal montantTotalHtTransfoLivraison) {
		this.montantTotalHtTransfoLivraison = montantTotalHtTransfoLivraison;
	}

	public BigDecimal getMontantTotalHtNonTransfoLivraison() {
		return montantTotalHtNonTransfoLivraison;
	}

	public void setMontantTotalHtNonTransfoLivraison(BigDecimal montantTotalHtNonTransfoLivraison) {
		this.montantTotalHtNonTransfoLivraison = montantTotalHtNonTransfoLivraison;
	}

	public BigDecimal getMontantTotalHtNonTransfoARelancerLivraison() {
		return montantTotalHtNonTransfoARelancerLivraison;
	}

	public void setMontantTotalHtNonTransfoARelancerLivraison(BigDecimal montantTotalHtNonTransfoARelancerLivraison) {
		this.montantTotalHtNonTransfoARelancerLivraison = montantTotalHtNonTransfoARelancerLivraison;
	}

	public int getNbLivraison() {
		return nbLivraison;
	}

	public void setNbLivraison(int nbLivraison) {
		this.nbLivraison = nbLivraison;
	}

	public int getNbLivraisonNonTransforme() {
		return nbLivraisonNonTransforme;
	}

	public void setNbLivraisonNonTransforme(int nbLivraisonNonTransforme) {
		this.nbLivraisonNonTransforme = nbLivraisonNonTransforme;
	}

	public int getNbLivraisonTransforme() {
		return nbLivraisonTransforme;
	}

	public void setNbLivraisonTransforme(int nbLivraisonTransforme) {
		this.nbLivraisonTransforme = nbLivraisonTransforme;
	}

	public int getNbLivraisonNonTransformeARelancer() {
		return nbLivraisonNonTransformeARelancer;
	}

	public void setNbLivraisonNonTransformeARelancer(int nbLivraisonNonTransformeARelancer) {
		this.nbLivraisonNonTransformeARelancer = nbLivraisonNonTransformeARelancer;
	}

	public Date getDateDebutArticle() {
		return dateDebutArticle;
	}

	public void setDateDebutArticle(Date dateDebutArticle) {
		this.dateDebutArticle = dateDebutArticle;
	}

	public Date getDateFinArticle() {
		return dateFinArticle;
	}

	public void setDateFinArticle(Date dateFinArticle) {
		this.dateFinArticle = dateFinArticle;
	}

	public BarChartModel getBarChartModelArticle() {
		return barChartModelArticle;
	}

	public void setBarChartModelArticle(BarChartModel barChartModelArticle) {
		this.barChartModelArticle = barChartModelArticle;
	}

	public int getNbArticle() {
		return nbArticle;
	}

	public void setNbArticle(int nbArticle) {
		this.nbArticle = nbArticle;
	}

	public BigDecimal getMontantTotalHtArticle() {
		return montantTotalHtArticle;
	}

	public void setMontantTotalHtArticle(BigDecimal montantTotalHtArticle) {
		this.montantTotalHtArticle = montantTotalHtArticle;
	}

	public List<TaArticlesParTiersDTO> getValueLignesArticle() {
		return valueLignesArticle;
	}

	public void setValueLignesArticle(List<TaArticlesParTiersDTO> valueLignesArticle) {
		this.valueLignesArticle = valueLignesArticle;
	}

	public PieChartModel getPieChartModelArticle() {
		return pieChartModelArticle;
	}

	public void setPieChartModelArticle(PieChartModel pieChartModelArticle) {
		this.pieChartModelArticle = pieChartModelArticle;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentArticle() {
		return listeCaMoisDocumentArticle;
	}

	public void setListeCaMoisDocumentArticle(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentArticle) {
		this.listeCaMoisDocumentArticle = listeCaMoisDocumentArticle;
	}

	public List<TaArticlesParTiersDTO> getValueLignesArticleRegroupee() {
		return valueLignesArticleRegroupee;
	}

	public void setValueLignesArticleRegroupee(List<TaArticlesParTiersDTO> valueLignesArticleRegroupee) {
		this.valueLignesArticleRegroupee = valueLignesArticleRegroupee;
	}

	public BigDecimal getMontantTotalHtNonTransfoArticle() {
		return montantTotalHtNonTransfoArticle;
	}

	public void setMontantTotalHtNonTransfoArticle(BigDecimal montantTotalHtNonTransfoArticle) {
		this.montantTotalHtNonTransfoArticle = montantTotalHtNonTransfoArticle;
	}

	public BigDecimal getMontantTotalHtTransfoArticle() {
		return montantTotalHtTransfoArticle;
	}

	public void setMontantTotalHtTransfoArticle(BigDecimal montantTotalHtTransfoArticle) {
		this.montantTotalHtTransfoArticle = montantTotalHtTransfoArticle;
	}

	public int getNbArticleNonTransforme() {
		return nbArticleNonTransforme;
	}

	public void setNbArticleNonTransforme(int nbArticleNonTransforme) {
		this.nbArticleNonTransforme = nbArticleNonTransforme;
	}

	public int getNbArticleTransforme() {
		return nbArticleTransforme;
	}

	public void setNbArticleTransforme(int nbArticleTransforme) {
		this.nbArticleTransforme = nbArticleTransforme;
	}

	public List<TaArticlesParTiersDTO> getValueLignesArticleRegroupeeParFamille() {
		return valueLignesArticleRegroupeeParFamille;
	}

	public void setValueLignesArticleRegroupeeParFamille(List<TaArticlesParTiersDTO> valueLignesArticleRegroupeeParFamille) {
		this.valueLignesArticleRegroupeeParFamille = valueLignesArticleRegroupeeParFamille;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaFamilleDocumentArticle() {
		return listeCaFamilleDocumentArticle;
	}

	public void setListeCaFamilleDocumentArticle(List<DocumentChiffreAffaireDTO> listeCaFamilleDocumentArticle) {
		this.listeCaFamilleDocumentArticle = listeCaFamilleDocumentArticle;
	}

	public HorizontalBarChartModel getBarChartModelArticleHoriz() {
		return barChartModelArticleHoriz;
	}

	public void setBarChartModelArticleHoriz(HorizontalBarChartModel barChartModelArticleHoriz) {
		this.barChartModelArticleHoriz = barChartModelArticleHoriz;
	}

	public String getInfosLabelDoc() {
		return infosLabelDoc;
	}

	public void setInfosLabelDoc(String infosLabelDoc) {
		this.infosLabelDoc = infosLabelDoc;
	}

	public List getValeursListe() {
		return valeursListe;
	}

	public void setValeursListe(List valeursListe) {
		this.valeursListe = valeursListe;
	}

	public boolean isRegroupeArticle() {
		return regroupeArticle;
	}

	public void setRegroupeArticle(boolean regroupeArticle) {
		this.regroupeArticle = regroupeArticle;
	}

	public boolean isHistoArticle() {
		return histoArticle;
	}

	public void setHistoArticle(boolean histoArticle) {
		this.histoArticle = histoArticle;
	}

	public boolean isFamilleArticle() {
		return familleArticle;
	}

	public void setFamilleArticle(boolean familleArticle) {
		this.familleArticle = familleArticle;
	}

	public boolean isFactureRealise() {
		return factureRealise;
	}

	public void setFactureRealise(boolean factureRealise) {
		this.factureRealise = factureRealise;
	}

	public boolean isFactureNonTotalRegle() {
		return factureNonTotalRegle;
	}

	public void setFactureNonTotalRegle(boolean factureNonTotalRegle) {
		this.factureNonTotalRegle = factureNonTotalRegle;
	}

	public boolean isFactureARelancer() {
		return factureARelancer;
	}

	public void setFactureARelancer(boolean factureARelancer) {
		this.factureARelancer = factureARelancer;
	}

	public boolean isFactureTotalRegle() {
		return factureTotalRegle;
	}

	public void setFactureTotalRegle(boolean factureTotalRegle) {
		this.factureTotalRegle = factureTotalRegle;
	}

	public List<DocumentDTO> getValueLignesFactureNonTotalRegle() {
		return valueLignesFactureNonTotalRegle;
	}

	public void setValueLignesFactureNonTotalRegle(List<DocumentDTO> valueLignesFactureNonTotalRegle) {
		this.valueLignesFactureNonTotalRegle = valueLignesFactureNonTotalRegle;
	}

	public List<DocumentDTO> getValueLignesFactureARelancer() {
		return valueLignesFactureARelancer;
	}

	public void setValueLignesFactureARelancer(List<DocumentDTO> valueLignesFactureARelancer) {
		this.valueLignesFactureARelancer = valueLignesFactureARelancer;
	}

	public List<DocumentDTO> getValueLignesFactureTotalRegle() {
		return valueLignesFactureTotalRegle;
	}

	public void setValueLignesFactureTotalRegle(List<DocumentDTO> valueLignesFactureTotalRegle) {
		this.valueLignesFactureTotalRegle = valueLignesFactureTotalRegle;
	}
	public HorizontalBarChartModel getBarChartModelArticleParArticleHoriz() {
		return barChartModelArticleParArticleHoriz;
	}

	public void setBarChartModelArticleParArticleHoriz(HorizontalBarChartModel barChartModelArticleParArticleHoriz) {
		this.barChartModelArticleParArticleHoriz = barChartModelArticleParArticleHoriz;
	}

	public String getStyleHeightChart() {
		return styleHeightChart;
	}

	public void setStyleHeightChart(String styleHeightChart) {
		this.styleHeightChart = styleHeightChart;
	}

	public boolean isDevisRealise() {
		return devisRealise;
	}

	public void setDevisRealise(boolean devisRealise) {
		this.devisRealise = devisRealise;
	}

	public boolean isDevisNonTransforme() {
		return devisNonTransforme;
	}

	public void setDevisNonTransforme(boolean devisNonTransforme) {
		this.devisNonTransforme = devisNonTransforme;
	}

	public boolean isDevisARelancer() {
		return devisARelancer;
	}

	public void setDevisARelancer(boolean devisARelancer) {
		this.devisARelancer = devisARelancer;
	}

	public boolean isDevisTransforme() {
		return devisTransforme;
	}

	public void setDevisTransforme(boolean devisTransforme) {
		this.devisTransforme = devisTransforme;
	}

	public List<DocumentDTO> getValueLignesDevisNonTransforme() {
		return valueLignesDevisNonTransforme;
	}

	public void setValueLignesDevisNonTransforme(List<DocumentDTO> valueLignesDevisNonTransforme) {
		this.valueLignesDevisNonTransforme = valueLignesDevisNonTransforme;
	}

	public List<DocumentDTO> valueLignesDevisARelancer() {
		return valueLignesDevisARelancer;
	}

	public void setValueLignesDevisARelancer(List<DocumentDTO> valueLignesDevisARelancer) {
		this.valueLignesDevisARelancer = valueLignesDevisARelancer;
	}

	public List<DocumentDTO> getValueLignesDevisTransforme() {
		return valueLignesDevisTransforme;
	}

	public void setValueLignesDevisTransforme(List<DocumentDTO> valueLignesDevisTransforme) {
		this.valueLignesDevisTransforme = valueLignesDevisTransforme;
	}

	public boolean isCommandeRealise() {
		return commandeRealise;
	}

	public void setCommandeRealise(boolean commandeRealise) {
		this.commandeRealise = commandeRealise;
	}

	public boolean isCommandeNonTransforme() {
		return commandeNonTransforme;
	}

	public void setCommandeNonTransforme(boolean commandeNonTransforme) {
		this.commandeNonTransforme = commandeNonTransforme;
	}

	public boolean isCommandeARelancer() {
		return commandeARelancer;
	}

	public void setCommandeARelancer(boolean commandeARelancer) {
		this.commandeARelancer = commandeARelancer;
	}

	public boolean isCommandeTransforme() {
		return commandeTransforme;
	}

	public void setCommandeTransforme(boolean commandeTransforme) {
		this.commandeTransforme = commandeTransforme;
	}

	public List<DocumentDTO> getValueLignesCommandeNonTransforme() {
		return valueLignesCommandeNonTransforme;
	}

	public void setValueLignesCommandeNonTransforme(List<DocumentDTO> valueLignesCommandeNonTransforme) {
		this.valueLignesCommandeNonTransforme = valueLignesCommandeNonTransforme;
	}

	public List<DocumentDTO> getValueLignesCommandeARelancer() {
		return valueLignesCommandeARelancer;
	}

	public void setValueLignesCommandeARelancer(List<DocumentDTO> valueLignesCommandeARelancer) {
		this.valueLignesCommandeARelancer = valueLignesCommandeARelancer;
	}

	public List<DocumentDTO> getValueLignesCommandeTransforme() {
		return valueLignesCommandeTransforme;
	}

	public void setValueLignesCommandeTransforme(List<DocumentDTO> valueLignesCommandeTransforme) {
		this.valueLignesCommandeTransforme = valueLignesCommandeTransforme;
	}
	
	public boolean isAfficheGraphFamille() {
		return afficheGraphFamille;
	}

	public void setAfficheGraphFamille(boolean afficheGraphFamille) {
		this.afficheGraphFamille = afficheGraphFamille;
	}

	public boolean isAfficheGraphParArticle() {
		return afficheGraphParArticle;
	}

	public void setAfficheGraphParArticle(boolean afficheGraphParArticle) {
		this.afficheGraphParArticle = afficheGraphParArticle;
	}


	public String getTitreTabListeRegroupeeParArticle() {
		return titreTabListeRegroupeeParArticle;
	}

	public void setTitreTabListeRegroupeeParArticle(String titreTabListeRegroupeeParArticle) {
		this.titreTabListeRegroupeeParArticle = titreTabListeRegroupeeParArticle;
	}

	public String getTitreTabListeRegroupeeParFamille() {
		return titreTabListeRegroupeeParFamille;
	}

	public void setTitreTabListeRegroupeeParFamille(String titreTabListeRegroupeeParFamille) {
		this.titreTabListeRegroupeeParFamille = titreTabListeRegroupeeParFamille;
	}

	public Date getValeursDateDebut() {
		return valeursDateDebut;
	}

	public void setValeursDateDebut(Date valeursDateDebut) {
		this.valeursDateDebut = valeursDateDebut;
	}

	public Date getValeursDateFin() {
		return valeursDateFin;
	}

	public void setValeursDateFin(Date valeursDateFin) {
		this.valeursDateFin = valeursDateFin;
	}

	public BigDecimal getMontantTotalTtcDevis() {
		return montantTotalTtcDevis;
	}

	public void setMontantTotalTtcDevis(BigDecimal montantTotalTtcDevis) {
		this.montantTotalTtcDevis = montantTotalTtcDevis;
	}

	public BigDecimal getMontantTotalTtcTransfoDevis() {
		return montantTotalTtcTransfoDevis;
	}

	public void setMontantTotalTtcTransfoDevis(BigDecimal montantTotalTtcTransfoDevis) {
		this.montantTotalTtcTransfoDevis = montantTotalTtcTransfoDevis;
	}

	public BigDecimal getMontantTotalTtcNonTransfoDevis() {
		return montantTotalTtcNonTransfoDevis;
	}

	public void setMontantTotalTtcNonTransfoDevis(BigDecimal montantTotalTtcNonTransfoDevis) {
		this.montantTotalTtcNonTransfoDevis = montantTotalTtcNonTransfoDevis;
	}

	public BigDecimal getMontantTotalTtcNonTransfoARelancerDevis() {
		return montantTotalTtcNonTransfoARelancerDevis;
	}

	public void setMontantTotalTtcNonTransfoARelancerDevis(BigDecimal montantTotalTtcNonTransfoARelancerDevis) {
		this.montantTotalTtcNonTransfoARelancerDevis = montantTotalTtcNonTransfoARelancerDevis;
	}

	public BigDecimal getMontantTotalTtcCommande() {
		return montantTotalTtcCommande;
	}

	public void setMontantTotalTtcCommande(BigDecimal montantTotalTtcCommande) {
		this.montantTotalTtcCommande = montantTotalTtcCommande;
	}

	public BigDecimal getMontantTotalTtcTransfoCommande() {
		return montantTotalTtcTransfoCommande;
	}

	public void setMontantTotalTtcTransfoCommande(BigDecimal montantTotalTtcTransfoCommande) {
		this.montantTotalTtcTransfoCommande = montantTotalTtcTransfoCommande;
	}

	public BigDecimal getMontantTotalTtcNonTransfoCommande() {
		return montantTotalTtcNonTransfoCommande;
	}

	public void setMontantTotalTtcNonTransfoCommande(BigDecimal montantTotalTtcNonTransfoCommande) {
		this.montantTotalTtcNonTransfoCommande = montantTotalTtcNonTransfoCommande;
	}

	public BigDecimal getMontantTotalTtcNonTransfoARelancerCommande() {
		return montantTotalTtcNonTransfoARelancerCommande;
	}

	public void setMontantTotalTtcNonTransfoARelancerCommande(BigDecimal montantTotalTtcNonTransfoARelancerCommande) {
		this.montantTotalTtcNonTransfoARelancerCommande = montantTotalTtcNonTransfoARelancerCommande;
	}

	public BigDecimal getMontantTotalTtcLivraison() {
		return montantTotalTtcLivraison;
	}

	public void setMontantTotalTtcLivraison(BigDecimal montantTotalTtcLivraison) {
		this.montantTotalTtcLivraison = montantTotalTtcLivraison;
	}

	public BigDecimal getMontantTotalTtcTransfoLivraison() {
		return montantTotalTtcTransfoLivraison;
	}

	public void setMontantTotalTtcTransfoLivraison(BigDecimal montantTotalTtcTransfoLivraison) {
		this.montantTotalTtcTransfoLivraison = montantTotalTtcTransfoLivraison;
	}

	public BigDecimal getMontantTotalTtcNonTransfoLivraison() {
		return montantTotalTtcNonTransfoLivraison;
	}

	public void setMontantTotalTtcNonTransfoLivraison(BigDecimal montantTotalTtcNonTransfoLivraison) {
		this.montantTotalTtcNonTransfoLivraison = montantTotalTtcNonTransfoLivraison;
	}

	public BigDecimal getMontantTotalTtcNonTransfoARelancerLivraison() {
		return montantTotalTtcNonTransfoARelancerLivraison;
	}

	public void setMontantTotalTtcNonTransfoARelancerLivraison(BigDecimal montantTotalTtcNonTransfoARelancerLivraison) {
		this.montantTotalTtcNonTransfoARelancerLivraison = montantTotalTtcNonTransfoARelancerLivraison;
	}

	public BigDecimal getMontantTotalTtcProforma() {
		return montantTotalTtcProforma;
	}

	public void setMontantTotalTtcProforma(BigDecimal montantTotalTtcProforma) {
		this.montantTotalTtcProforma = montantTotalTtcProforma;
	}

	public BigDecimal getMontantTotalTtcTransfoProforma() {
		return montantTotalTtcTransfoProforma;
	}

	public void setMontantTotalTtcTransfoProforma(BigDecimal montantTotalTtcTransfoProforma) {
		this.montantTotalTtcTransfoProforma = montantTotalTtcTransfoProforma;
	}

	public BigDecimal getMontantTotalTtcNonTransfoProforma() {
		return montantTotalTtcNonTransfoProforma;
	}

	public void setMontantTotalTtcNonTransfoProforma(BigDecimal montantTotalTtcNonTransfoProforma) {
		this.montantTotalTtcNonTransfoProforma = montantTotalTtcNonTransfoProforma;
	}

	public BigDecimal getMontantTotalTtcNonTransfoARelancerProforma() {
		return montantTotalTtcNonTransfoARelancerProforma;
	}

	public void setMontantTotalTtcNonTransfoARelancerProforma(BigDecimal montantTotalTtcNonTransfoARelancerProforma) {
		this.montantTotalTtcNonTransfoARelancerProforma = montantTotalTtcNonTransfoARelancerProforma;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeTiersNonTransformeARelancer() {
		return listeCaPeriodeTiersNonTransformeARelancer;
	}

	public void setListeCaPeriodeTiersNonTransformeARelancer(List<DocumentChiffreAffaireDTO> listeCaPeriodeTiersNonTransformeARelancer) {
		this.listeCaPeriodeTiersNonTransformeARelancer = listeCaPeriodeTiersNonTransformeARelancer;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMois() {
		return listeCaMois;
	}

	public void setListeCaMois(List<DocumentChiffreAffaireDTO> listeCaMois) {
		this.listeCaMois = listeCaMois;
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
	
	public void handleReturnDialogGestionReglement(SelectEvent event) {
		initAllValueFacture(codeTiersCourant);
	}

	public BigDecimal getMontantTotalHtFactureGeneral() {
		return montantTotalHtFactureGeneral;
	}

	public void setMontantTotalHtFactureGeneral(BigDecimal montantTotalHtFactureGeneral) {
		this.montantTotalHtFactureGeneral = montantTotalHtFactureGeneral;
	}

	public BigDecimal getMontantTotalHtNonTransfoArticleGeneral() {
		return montantTotalHtNonTransfoArticleGeneral;
	}

	public void setMontantTotalHtNonTransfoArticleGeneral(BigDecimal montantTotalHtNonTransfoArticleGeneral) {
		this.montantTotalHtNonTransfoArticleGeneral = montantTotalHtNonTransfoArticleGeneral;
	}

	public int getNbFactureGeneral() {
		return nbFactureGeneral;
	}

	public void setNbFactureGeneral(int nbFactureGeneral) {
		this.nbFactureGeneral = nbFactureGeneral;
	}
	
	public boolean isLivraisonRealise() {
		return livraisonRealise;
	}

	public void setLivraisonRealise(boolean livraisonRealise) {
		this.livraisonRealise = livraisonRealise;
	}

	public boolean isLivraisonNonFacture() {
		return livraisonNonFacture;
	}

	public void setLivraisonNonFacture(boolean livraisonNonFacture) {
		this.livraisonNonFacture = livraisonNonFacture;
	}

	public boolean isLivraisonFacture() {
		return livraisonFacture;
	}

	public void setLivraisonFacture(boolean livraisonFacture) {
		this.livraisonFacture = livraisonFacture;
	}

	public List<DocumentDTO> getValueLignesLivraisonNonFacture() {
		return valueLignesLivraisonNonFacture;
	}

	public void setValueLignesLivraisonNonFacture(List<DocumentDTO> valueLignesLivraisonNonFacture) {
		this.valueLignesLivraisonNonFacture = valueLignesLivraisonNonFacture;
	}
	
	public List<DocumentDTO> getValueLignesLivraisonFacture() {
		return valueLignesLivraisonFacture;
	}

	public void setValueLignesLivraisonFacture(List<DocumentDTO> valueLignesLivraisonFacture) {
		this.valueLignesLivraisonFacture = valueLignesLivraisonFacture;
	}

	public boolean isProformaRealise() {
		return proformaRealise;
	}

	public void setProformaRealise(boolean proformaRealise) {
		this.proformaRealise = proformaRealise;
	}

	public boolean isProformaNonTransforme() {
		return proformaNonTransforme;
	}

	public void setProformaNonTransforme(boolean proformaNonTransforme) {
		this.proformaNonTransforme = proformaNonTransforme;
	}

	public boolean isProformaARelancer() {
		return proformaARelancer;
	}

	public void setProformaARelancer(boolean proformaARelancer) {
		this.proformaARelancer = proformaARelancer;
	}

	public boolean isProformaTransforme() {
		return proformaTransforme;
	}

	public void setProformaTransforme(boolean proformaTransforme) {
		this.proformaTransforme = proformaTransforme;
	}
	
	public List<DocumentDTO> getValueLignesProformaNonTransforme() {
		return valueLignesProformaNonTransforme;
	}

	public void setValueLignesProformaNonTransforme(List<DocumentDTO> valueLignesProformaNonTransforme) {
		this.valueLignesProformaNonTransforme = valueLignesProformaNonTransforme;
	}

	public List<DocumentDTO> getValueLignesProformaARelancer() {
		return valueLignesProformaARelancer;
	}

	public void setValueLignesProformaARelancer(List<DocumentDTO> valueLignesProformaARelancer) {
		this.valueLignesProformaARelancer = valueLignesProformaARelancer;
	}

	public List<DocumentDTO> getValueLignesProformaTransforme() {
		return valueLignesProformaTransforme;
	}

	public void setValueLignesProformaTransforme(List<DocumentDTO> valueLignesProformaTransforme) {
		this.valueLignesProformaTransforme = valueLignesProformaTransforme;
	}

	public List<DocumentDTO> getValueLignesDevisARelancer() {
		return valueLignesDevisARelancer;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public Date getDateDebutReglement() {
		return dateDebutReglement;
	}

	public void setDateDebutReglement(Date dateDebutReglement) {
		this.dateDebutReglement = dateDebutReglement;
	}

	public Date getDateFinReglement() {
		return dateFinReglement;
	}

	public void setDateFinReglement(Date dateFinReglement) {
		this.dateFinReglement = dateFinReglement;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentReglement() {
		return listeCaMoisDocumentReglement;
	}

	public void setListeCaMoisDocumentReglement(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentReglement) {
		this.listeCaMoisDocumentReglement = listeCaMoisDocumentReglement;
	}

	public List<DocumentDTO> getValueLignesReglement() {
		return valueLignesReglement;
	}

	public void setValueLignesReglement(List<DocumentDTO> valueLignesReglement) {
		this.valueLignesReglement = valueLignesReglement;
	}

	public List<DocumentDTO> getValueLignesReglementNonTransforme() {
		return valueLignesReglementNonTransforme;
	}

	public void setValueLignesReglementNonTransforme(List<DocumentDTO> valueLignesReglementNonTransforme) {
		this.valueLignesReglementNonTransforme = valueLignesReglementNonTransforme;
	}

	public List<DocumentDTO> getValueLignesReglementARelancer() {
		return valueLignesReglementARelancer;
	}

	public void setValueLignesReglementARelancer(List<DocumentDTO> valueLignesReglementARelancer) {
		this.valueLignesReglementARelancer = valueLignesReglementARelancer;
	}

	public List<DocumentDTO> getValueLignesReglementTransforme() {
		return valueLignesReglementTransforme;
	}

	public void setValueLignesReglementTransforme(List<DocumentDTO> valueLignesReglementTransforme) {
		this.valueLignesReglementTransforme = valueLignesReglementTransforme;
	}

	public BarChartModel getBarChartModelReglement() {
		return barChartModelReglement;
	}

	public void setBarChartModelReglement(BarChartModel barChartModelReglement) {
		this.barChartModelReglement = barChartModelReglement;
	}

	public BigDecimal getMontantTotalHtReglement() {
		return montantTotalHtReglement;
	}

	public void setMontantTotalHtReglement(BigDecimal montantTotalHtReglement) {
		this.montantTotalHtReglement = montantTotalHtReglement;
	}

	public BigDecimal getMontantTotalHtTransfoReglement() {
		return montantTotalHtTransfoReglement;
	}

	public void setMontantTotalHtTransfoReglement(BigDecimal montantTotalHtTransfoReglement) {
		this.montantTotalHtTransfoReglement = montantTotalHtTransfoReglement;
	}

	public BigDecimal getMontantTotalHtNonTransfoReglement() {
		return montantTotalHtNonTransfoReglement;
	}

	public void setMontantTotalHtNonTransfoReglement(BigDecimal montantTotalHtNonTransfoReglement) {
		this.montantTotalHtNonTransfoReglement = montantTotalHtNonTransfoReglement;
	}

	public BigDecimal getMontantTotalHtNonTransfoARelancerReglement() {
		return montantTotalHtNonTransfoARelancerReglement;
	}

	public void setMontantTotalHtNonTransfoARelancerReglement(BigDecimal montantTotalHtNonTransfoARelancerReglement) {
		this.montantTotalHtNonTransfoARelancerReglement = montantTotalHtNonTransfoARelancerReglement;
	}

	public BigDecimal getMontantTotalTtcReglement() {
		return montantTotalTtcReglement;
	}

	public void setMontantTotalTtcReglement(BigDecimal montantTotalTtcReglement) {
		this.montantTotalTtcReglement = montantTotalTtcReglement;
	}

	public BigDecimal getMontantTotalTtcTransfoReglement() {
		return montantTotalTtcTransfoReglement;
	}

	public void setMontantTotalTtcTransfoReglement(BigDecimal montantTotalTtcTransfoReglement) {
		this.montantTotalTtcTransfoReglement = montantTotalTtcTransfoReglement;
	}

	public BigDecimal getMontantTotalTtcNonTransfoReglement() {
		return montantTotalTtcNonTransfoReglement;
	}

	public void setMontantTotalTtcNonTransfoReglement(BigDecimal montantTotalTtcNonTransfoReglement) {
		this.montantTotalTtcNonTransfoReglement = montantTotalTtcNonTransfoReglement;
	}

	public BigDecimal getMontantTotalTtcNonTransfoARelancerReglement() {
		return montantTotalTtcNonTransfoARelancerReglement;
	}

	public void setMontantTotalTtcNonTransfoARelancerReglement(BigDecimal montantTotalTtcNonTransfoARelancerReglement) {
		this.montantTotalTtcNonTransfoARelancerReglement = montantTotalTtcNonTransfoARelancerReglement;
	}

	public int getNbReglement() {
		return nbReglement;
	}

	public void setNbReglement(int nbReglement) {
		this.nbReglement = nbReglement;
	}

	public int getNbReglementNonTransforme() {
		return nbReglementNonTransforme;
	}

	public void setNbReglementNonTransforme(int nbReglementNonTransforme) {
		this.nbReglementNonTransforme = nbReglementNonTransforme;
	}

	public int getNbReglementTransforme() {
		return nbReglementTransforme;
	}

	public void setNbReglementTransforme(int nbReglementTransforme) {
		this.nbReglementTransforme = nbReglementTransforme;
	}

	public int getNbReglementNonTransformeARelancer() {
		return nbReglementNonTransformeARelancer;
	}

	public void setNbReglementNonTransformeARelancer(int nbReglementNonTransformeARelancer) {
		this.nbReglementNonTransformeARelancer = nbReglementNonTransformeARelancer;
	}

	public boolean isReglementRealise() {
		return reglementRealise;
	}

	public void setReglementRealise(boolean reglementRealise) {
		this.reglementRealise = reglementRealise;
	}

	public boolean isReglementNonTransforme() {
		return reglementNonTransforme;
	}

	public void setReglementNonTransforme(boolean reglementNonTransforme) {
		this.reglementNonTransforme = reglementNonTransforme;
	}

	public boolean isReglementARelancer() {
		return reglementARelancer;
	}

	public void setReglementARelancer(boolean reglementARelancer) {
		this.reglementARelancer = reglementARelancer;
	}

	public boolean isReglementTransforme() {
		return reglementTransforme;
	}

	public void setReglementTransforme(boolean reglementTransforme) {
		this.reglementTransforme = reglementTransforme;
	}

	public String getStyleHeightChartFamille() {
		return styleHeightChartFamille;
	}

	public void setStyleHeightChartFamille(String styleHeightChartFamille) {
		this.styleHeightChartFamille = styleHeightChartFamille;
	}

	public boolean isInclureReglementFactReal() {
		return inclureReglementFactReal;
	}

	public void setInclureReglementFactReal(boolean inclureReglementFactReal) {
		this.inclureReglementFactReal = inclureReglementFactReal;
	}

	public boolean isInclureReglementNonTotReg() {
		return inclureReglementNonTotReg;
	}

	public void setInclureReglementNonTotReg(boolean inclureReglementNonTotReg) {
		this.inclureReglementNonTotReg = inclureReglementNonTotReg;
	}

	public boolean isInclureFactRegle() {
		return inclureFactRegle;
	}

	public void setInclureFactRegle(boolean inclureFactRegle) {
		this.inclureFactRegle = inclureFactRegle;
	}

	public boolean isInclureFactNonTotReg() {
		return inclureFactNonTotReg;
	}

	public void setInclureFactNonTotReg(boolean inclureFactNonTotReg) {
		this.inclureFactNonTotReg = inclureFactNonTotReg;
	}

	public boolean isInclureFactTotReg() {
		return inclureFactTotReg;
	}

	public void setInclureFactTotReg(boolean inclureFactTotReg) {
		this.inclureFactTotReg = inclureFactTotReg;
	}

	public boolean isInclureReglementARelancer() {
		return inclureReglementARelancer;
	}

	public void setInclureReglementARelancer(boolean inclureReglementARelancer) {
		this.inclureReglementARelancer = inclureReglementARelancer;
	}

	public boolean isInclureReglementTotReg() {
		return inclureReglementTotReg;
	}

	public void setInclureReglementTotReg(boolean inclureReglementTotReg) {
		this.inclureReglementTotReg = inclureReglementTotReg;
	}

	public boolean isBreakPageEtatReglement() {
		return breakPageEtatReglement;
	}

	public void setBreakPageEtatReglement(boolean breakPageEtatReglement) {
		this.breakPageEtatReglement = breakPageEtatReglement;
	}

	public boolean isBreakPageEtatArticle() {
		return breakPageEtatArticle;
	}

	public void setBreakPageEtatArticle(boolean breakPageEtatArticle) {
		this.breakPageEtatArticle = breakPageEtatArticle;
	}

	public boolean isBreakPageEtatDevis() {
		return breakPageEtatDevis;
	}

	public void setBreakPageEtatDevis(boolean breakPageEtatDevis) {
		this.breakPageEtatDevis = breakPageEtatDevis;
	}

	public boolean isBreakPageEtatCommande() {
		return breakPageEtatCommande;
	}

	public void setBreakPageEtatCommande(boolean breakPageEtatCommande) {
		this.breakPageEtatCommande = breakPageEtatCommande;
	}

	public boolean isBreakPageEtatLivraison() {
		return breakPageEtatLivraison;
	}

	public void setBreakPageEtatLivraison(boolean breakPageEtatLivraison) {
		this.breakPageEtatLivraison = breakPageEtatLivraison;
	}

	public boolean isBreakPageEtatProforma() {
		return breakPageEtatProforma;
	}

	public void setBreakPageEtatProforma(boolean breakPageEtatProforma) {
		this.breakPageEtatProforma = breakPageEtatProforma;
	}

	public String getTitreListeSyntheseParTiersTous() {
		return titreListeSyntheseParTiersTous;
	}

	public void setTitreListeSyntheseParTiersTous(String titreListeSyntheseParTiersTous) {
		this.titreListeSyntheseParTiersTous = titreListeSyntheseParTiersTous;
	}

	public String getTitreListeSyntheseParTiersArticleTous() {
		return titreListeSyntheseParTiersArticleTous;
	}

	public void setTitreListeSyntheseParTiersArticleTous(String titreListeSyntheseParTiersArticleTous) {
		this.titreListeSyntheseParTiersArticleTous = titreListeSyntheseParTiersArticleTous;
	}

	public String getTitreEditionListeDetailParTiersArticleTous() {
		return titreEditionListeDetailParTiersArticleTous;
	}

	public void setTitreEditionListeDetailParTiersArticleTous(String titreEditionListeDetailParTiersArticleTous) {
		this.titreEditionListeDetailParTiersArticleTous = titreEditionListeDetailParTiersArticleTous;
	}

	public List<DocumentChiffreAffaireDTO> getValueLignesTiersTous() {
		return valueLignesTiersTous;
	}

	public void setValueLignesTiersTous(List<DocumentChiffreAffaireDTO> valueLignesTiersTous) {
		this.valueLignesTiersTous = valueLignesTiersTous;
	}

	public List<DocumentChiffreAffaireDTO> getValueLignesTiersArticlesTous() {
		return valueLignesTiersArticlesTous;
	}

	public void setValueLignesTiersArticlesTous(List<DocumentChiffreAffaireDTO> valueLignesTiersArticlesTous) {
		this.valueLignesTiersArticlesTous = valueLignesTiersArticlesTous;
	}

	public List<DocumentChiffreAffaireDTO> getValueLignesTiersTousDetail() {
		return valueLignesTiersTousDetail;
	}

	public void setValueLignesTiersTousDetail(List<DocumentChiffreAffaireDTO> valueLignesTiersTousDetail) {
		this.valueLignesTiersTousDetail = valueLignesTiersTousDetail;
	}



public void actImprimerListeArticleTousGroupeTiersArticles(ActionEvent actionEvent) {
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", titreListeSyntheseParTiersArticleTous);

		
		
		sessionMap.put("parametre", mapParametre);
		valueLignesTiersArticlesTous=taFactureService.listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiersCourant, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);
		sessionMap.put("listeArticle", valueLignesTiersArticlesTous);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
public void actImprimerListeArticleTousGroupeTiers(ActionEvent actionEvent) {
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", titreListeSyntheseParTiersTous);
//		mapParametre.put("infosLabel", "Synthèse des ventes (et avoirs) par articles et par unités");
		
		
		sessionMap.put("parametre", mapParametre);
		valueLignesTiersTous=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiersCourant, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS);
		sessionMap.put("listeArticle", valueLignesTiersTous);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public void actImprimerListeArticleTousDetailGroupeTiers(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreEditionListeDetailParTiersArticleTous);
//	mapParametre.put("infosLabel", "Détail des ventes (et avoirs) par article");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneArticleTous());
	
	} catch (Exception e) {
		e.printStackTrace();
	}
}


// Synthèse et détail TOUS
public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneArticleTous(){
	valueLignesTiersTous=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiersCourant, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS);
//	valueLignesTiersArticlesTous=taFactureService.listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiersCourant, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);
	valueLignesTiersTousDetail=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,codeTiersCourant, false, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);

	
	for (DocumentChiffreAffaireDTO synthese : valueLignesTiersTous ) {
		for (DocumentChiffreAffaireDTO documentChiffreAffaireDTO : valueLignesTiersTousDetail) {
			if(documentChiffreAffaireDTO.getCodeTiers().equals(synthese.getCodeTiers()))
				synthese.getListeDetail().add(documentChiffreAffaireDTO);
		}
		
	}
		
//	List<DocumentChiffreAffaireDTO> syntheseEtDetail = valueLignesTiersTous;
	return valueLignesTiersTous;
}

}
