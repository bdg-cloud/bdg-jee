package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.chart.BarChartModel;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.articles.ArticleController;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.paiement.PaiementCbController;
import fr.legrain.bdg.webapp.paiement.PaiementParam;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dto.TaTiersDTO;

@Named
@ViewScoped
public class DashBoardParArticleController extends DashBoardDocumentController implements Serializable {


	

	/**
	 * 
	 */
	private static final long serialVersionUID = 8416557110138209660L;

	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTiersServiceRemote taTiersService;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	private TaInfoEntreprise infoEntreprise;
	private String codeArticleCourant;
//	private BigDecimal montantCaHt = BigDecimal.ZERO;
	private Date dateDebutGeneral;
	private Date dateFinGeneral;
    private BarChartModel barChartModelGeneral;
    private String identification="Aucune";
    private boolean inclureDescription=false;
    private boolean inclureStatistique=false;
	private boolean breakPageEtatFactureAvoir=false;
    private boolean breakPageEtatTiers=false;
    private boolean breakPageEtatDevis=false;
    private boolean breakPageEtatCommande=false;
    private boolean breakPageEtatProforma=false;
    private String titreListeSyntheseParArticleFacture = "Synthèse des ventes pour l'article et par unités";
    private String titreEditionListeDetailParArticleFacture ="Détail des ventes pour l'article et par unités";

//    private DocumentChiffreAffaireDTO totalCaPeriodeFacture;
    
    public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public boolean isInclureDescription() {
		return inclureDescription;
	}

	public void setInclureDescription(boolean inclureDescription) {
		this.inclureDescription = inclureDescription;
	}
	
	 public boolean isInclureStatistique() {
		return inclureStatistique;
	}

	public void setInclureStatistique(boolean inclureStatistique) {
		this.inclureStatistique = inclureStatistique;
	}
		
	public boolean isBreakPageEtatFactureAvoir() {
		return breakPageEtatFactureAvoir;
	}

	public void setBreakPageEtatFactureAvoir(boolean breakPageEtatFactureAvoir) {
		this.breakPageEtatFactureAvoir = breakPageEtatFactureAvoir;
	}

	public boolean isBreakPageEtatTiers() {
		return breakPageEtatTiers;
	}

	public void setBreakPageEtatTiers(boolean breakPageEtatTiers) {
		this.breakPageEtatTiers = breakPageEtatTiers;
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

	public boolean isBreakPageEtatProforma() {
		return breakPageEtatProforma;
	}

	public void setBreakPageEtatProforma(boolean breakPageEtatProforma) {
		this.breakPageEtatProforma = breakPageEtatProforma;
	}	
	private TaArticleDTO selectedTaArticleDTO = new TaArticleDTO();
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentFacture;
    
    //RAJOUT YANN POUR GRAPHIQUE A 3 BARRES (facturés, avoir et la somme des 2 (calculé))
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentFactureSeulement;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentAvoirSeulement;
    
    //GENERAL
    private BigDecimal montantTotalHtFactureMoinsAvoirGeneral = BigDecimal.ZERO;
    private BigDecimal montantTotalHtFactureGeneral = BigDecimal.ZERO;
    private BigDecimal montantTotalHtFactureAvoirGeneral = BigDecimal.ZERO;
    
    //FACTURE
    private Date dateDebutFacture;
	private Date dateFinFacture;
    private List<DocumentChiffreAffaireDTO> valueLignesFacture;
    private BarChartModel barChartModelFacture;
    private BigDecimal montantTotalHtFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfoFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonRegleFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancerFacture = BigDecimal.ZERO;
    private BigDecimal montantTotalHtFactureAvoir = BigDecimal.ZERO;
    private BigDecimal montantTotalHtFactureMoinsAvoir = BigDecimal.ZERO;
    private int nbFacture = 0;
    private int nbFactureNonTransforme = 0 ;
    private int nbFactureTransforme = 0;
    private int nbFactureNonTransformeARelancer = 0;
    private boolean factureAvoirRealise=false;
	//Tiers
	private Date dateDebutTiers;
	private Date dateFinTiers;
    private int nbTiersAyantAcheterArticle = 0;
    private List<DocumentChiffreAffaireDTO> valueLignesTiers;
    private boolean tiersAyantAcheter=false;
    //PROFORMA
	private Date dateDebutProforma;
	private Date dateFinProforma;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentProforma;
    private List<DocumentDTO> valueLignesProforma;
    private List<DocumentDTO> valueLignesProformaNonTransforme;
    private List<DocumentDTO> valueLignesProformaARelancer;
    private List<DocumentDTO> valueLignesProformaTransforme;
    private BarChartModel barChartModelProforma;
    private boolean proformaRealise;
	private boolean proformaNonTransforme;
    private boolean proformaARelancer;
    private boolean proformaTransforme;
    
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

    //DEVIS
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
    private boolean devisRealise;
    private boolean devisNonTransforme;
    private boolean devisARelancer;
    private boolean devisTransforme;
    
    //COMMANDE
  	private Date dateDebutCom;
  	private Date dateFinCom;
    private List<DocumentChiffreAffaireDTO> listeCaMoisDocumentCom;
    private List<DocumentDTO> valueLignesCom;
    private List<DocumentDTO> valueLignesComNonTransforme;
    private List<DocumentDTO> valueLignesComARelancer;
    private List<DocumentDTO> valueLignesComTransforme;
    private BarChartModel barChartModelCom;
    private BigDecimal montantTotalHtCom = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfoCom = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoCom = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancerCom = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcCom = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcTransfoCom = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoCom = BigDecimal.ZERO;
    private BigDecimal montantTotalTtcNonTransfoARelancerCom = BigDecimal.ZERO;
    private int nbCom = 0;
    private int nbComNonTransforme = 0 ;
    private int nbComTransforme = 0;
    private int nbComNonTransformeARelancer = 0;
    private boolean commandeRealise;
    private boolean commandeNonTransforme;
    private boolean commandeARelancer;
    private boolean commandeTransforme;
    
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelArticle;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
    
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatArticle;
    private List<TaArticleDTO> valueLignesArticle;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticle;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransformeARelancer;
    private int nbArticleActif = 0;
    private int nbArticleNonTransformeSurPeriode = 0 ;
    private int nbArticleTransformeSurPeriode = 0;
    private int nbArticleNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    private String cSbNbArticleSurPeriode;
    private String cSbNbArticleTransformeSurPeriode;
    private String cSbNbArticleNonTransformeSurPeriode;
    private String cSbNbArticleNonTransformeARelancerSurPeriode;
    
    private String typeFacture = TaFacture.TYPE_DOC;
    private String typeAvoir = TaAvoir.TYPE_DOC;
    
    private int deltaJourARelancer = 15;
    private String infosLabelDoc; 
	private List valeursListe=null;
	private Date valeursDateDebut;
	private Date valeursDateFin;
	protected org.primefaces.component.tabview.Tab tabActifDashboardArticle;

    
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
		
		dateDebutCom = LibDate.localDateToDate(dateDebut);
    	dateFinCom= LibDate.localDateToDate(dateFin);
		barChartModelCom = createBarChartModelSimpleBarreVide("N/A", true, -50);

    	
    	dateDebutFacture = LibDate.localDateToDate(dateDebut);
    	dateFinFacture = LibDate.localDateToDate(dateFin);
		barChartModelFacture = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
//        montantTotalHtFacture = totauxCaPeriodeFacture(codeArticleCourant).getMtHtCalc();

    	codeEtatArticle = "tous";
    	
//    	barChartModelArticle = new BarChartModel();
    	//Initialisation des données sur la période exercice
//    	actRechercheArticle(codeEtatArticle);
//    	createBarModel();
    	dateDebutTiers = LibDate.localDateToDate(dateDebut);
    	dateFinTiers= LibDate.localDateToDate(dateFin);
    }


    public void refreshDashBoard(String etatDocument){
    	
    }

    public void refresh(TabChangeEvent tabEvent){
//    	actRechercheArticle(etatArticle);
    	String nomTab = "";
//    	FacesEvent event = null;
    	
//		String codeArticle = (String)tabEvent.getComponent().getAttributes().get("codeArticle");
//    	Map<String,String> params =
//                FacesContext.getExternalContext().getRequestParameterMap();
//    	String codeArticle = params.get("codeArticle");
    	
    	initTousIndicateursZero();
    	
    	
    	if(tabEvent!=null && tabEvent.getTab()!=null) {
    		nomTab = tabEvent.getTab().getId();
    		tabActifDashboardArticle = tabEvent.getTab();
    	}
    	else {
    		if (tabActifDashboardArticle !=null) {
    			nomTab = tabActifDashboardArticle.getId();
    		}
    		else
    		{
    			nomTab = "idTabDashBoardArticleGeneral";
    		}
    	}
		switch (nomTab) {
    	// Général
		case "idTabDashBoardArticleGeneral":
			initIndicateursGeneral(codeArticleCourant);
			break;
		// Onglet Factures	
		case "idTabDashBoardArticleFacture":
			initIndicateursFacture(codeArticleCourant);
			break;
			
		// Onglet Devis	
			case "idTabDashBoardArticleDevis":
			initIndicateursDevis(codeArticleCourant);
			break;
		// Onglet Commandes	
			case "idTabDashBoardArticleCommande":
			initIndicateursCom(codeArticleCourant);
			break;
		// Onglet Proforma	
		case "idTabDashBoardArticleProforma": 
				initIndicateursProforma(codeArticleCourant);
				break;
		// Onglet Tiers
		case "idTabDashBoardArticleTiers":
			initIndicateurTiers(codeArticleCourant);
			break;
			
		default:
			initTousIndicateursZero();
			break;
		}
    }

    public void initTousIndicateursZero() {
    	//Section general
        montantTotalHtFactureMoinsAvoirGeneral = BigDecimal.ZERO;
        montantTotalHtFactureGeneral = BigDecimal.ZERO;
        montantTotalHtFactureAvoirGeneral = BigDecimal.ZERO;
    	
    	
    	// Section facture
        montantTotalHtFacture = BigDecimal.ZERO;
        montantTotalHtTransfoFacture = BigDecimal.ZERO;
        montantTotalHtNonTransfoFacture = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerFacture = BigDecimal.ZERO;
        montantTotalTtcNonRegleFacture = BigDecimal.ZERO;
        montantTotalHtFactureAvoir = BigDecimal.ZERO;
        montantTotalHtFactureMoinsAvoir = BigDecimal.ZERO;

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
        montantTotalHtCom = BigDecimal.ZERO;
        montantTotalHtTransfoCom = BigDecimal.ZERO;
        montantTotalHtNonTransfoCom = BigDecimal.ZERO;
        montantTotalHtNonTransfoARelancerCom = BigDecimal.ZERO;
        montantTotalTtcCom=BigDecimal.ZERO;
        montantTotalTtcTransfoCom=BigDecimal.ZERO;
        montantTotalTtcNonTransfoCom=BigDecimal.ZERO;
        montantTotalTtcNonTransfoARelancerCom=BigDecimal.ZERO;
        nbCom = 0;
        nbComNonTransforme = 0;
        nbComTransforme = 0;
        nbComNonTransformeARelancer = 0;
        if (valueLignesCom != null) {
        	valueLignesCom.clear();
        	
        }
		barChartModelCom = createBarChartModelSimpleBarreVide("N/A", true, -50);
		
		//Section Tiers
		nbTiersAyantAcheterArticle = 0;
		if(valueLignesTiers != null) {
			valueLignesTiers.clear();
		} 

    }
    
    public void actRechercheArticle(String codeEtatArticle){
    }
    
    public void initIndicateursFactureGeneral(String codeArticle) {
    	montantTotalHtFactureMoinsAvoirGeneral = totauxCaPeriodeFactureMoinsAvoirGeneral(codeArticle).getMtHtCalc();
        montantTotalHtFactureGeneral = totauxCaPeriodeFactureGeneral(codeArticle).getMtHtCalc();
        montantTotalHtFactureAvoirGeneral = totauxCaPeriodeFactureAvoirGeneral(codeArticle).getMtHtCalc();
    }
    
    public void initIndicateursFacture(String codeArticle) {
        montantTotalHtFacture = totauxCaPeriodeFacture(codeArticle).getMtHtCalc();
        montantTotalHtFactureAvoir = totauxCaPeriodeFactureAvoir(codeArticle).getMtHtCalc();
        montantTotalHtFactureMoinsAvoir = totauxCaPeriodeFactureMoinsAvoir(codeArticle).getMtHtCalc();
        
        montantTotalHtTransfoFacture = totauxCaPeriodeFactureTransforme(codeArticle).getMtHtCalc();
        montantTotalHtNonTransfoFacture = totauxCaPeriodeFactureNonTransforme(codeArticle).getMtHtCalc();
        montantTotalTtcNonRegleFacture = totauxCaPeriodeFactureNonTransforme(codeArticle).getResteAReglerComplet();

        montantTotalHtNonTransfoARelancerFacture = totauxCaPeriodeFactureNonTransformeARelancer(codeArticle).getMtHtCalc();
        nbFacture = nbFactureSurPeriode(codeArticle);
        nbFactureNonTransforme = nbFactureNonTransformeSurPeriode(codeArticle);
        nbFactureTransforme = nbFactureTransformeSurPeriode(codeArticle);
        nbFactureNonTransformeARelancer = nbFactureNonTransformeARelancerSurPeriode(codeArticle);
        
        
      
    }
	// Retourne le nombre total de documents sur la période
    public int nbFactureSurPeriode(String codeArticle) {

    	int nb = (int) taFactureService.countDocument(dateDebutFacture,  dateFinFacture,null, codeArticle);
    	return nb;
    }
    
    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureGeneral(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTotalDTO(dateDebutFacture, dateFinFacture,codeArticle);
    	
    	DocumentChiffreAffaireDTO totaux = taFactureService.sumChiffreAffaireTotalDTOArticle(dateDebutGeneral, dateFinGeneral,codeArticle).get(0);
    	//renvoi le CA HT (facture - facture avoir)
    	//DocumentChiffreAffaireDTO totaux = taFactureService.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebutFacture, dateFinFacture,codeArticle).get(0);


    	return totaux;
    }
    public DocumentChiffreAffaireDTO totauxCaPeriodeFacture(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTotalDTO(dateDebutFacture, dateFinFacture,codeArticle);
    	
    	DocumentChiffreAffaireDTO totaux = taFactureService.sumChiffreAffaireTotalDTOArticle(dateDebutFacture, dateFinFacture,codeArticle).get(0);
    	//renvoi le CA HT (facture - facture avoir)
    	//DocumentChiffreAffaireDTO totaux = taFactureService.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebutFacture, dateFinFacture,codeArticle).get(0);


    	return totaux;
    }
    
    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureMoinsAvoirGeneral(String codeArticle) {
    	//renvoi le CA HT (facture - facture avoir)
    	DocumentChiffreAffaireDTO totauxMoinsAvoir = taFactureService.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebutGeneral, dateFinGeneral,codeArticle).get(0);


    	return totauxMoinsAvoir;
    }
    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureMoinsAvoir(String codeArticle) {
    	//renvoi le CA HT (facture - facture avoir)
    	DocumentChiffreAffaireDTO totauxMoinsAvoir = taFactureService.sumChiffreAffaireTotalDTOArticleMoinsAvoir(dateDebutFacture, dateFinFacture,codeArticle).get(0);


    	return totauxMoinsAvoir;
    }
    
    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureAvoirGeneral(String codeArticle) {

    	//test pour remonter les avoirs  
    	DocumentChiffreAffaireDTO totauxAvoir = taAvoirService.sumChiffreAffaireTotalDTOArticle(dateDebutGeneral, dateFinGeneral,codeArticle).get(0);

    	return totauxAvoir;
    }
    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureAvoir(String codeArticle) {

    	//test pour remonter les avoirs  
    	DocumentChiffreAffaireDTO totauxAvoir = taAvoirService.sumChiffreAffaireTotalDTOArticle(dateDebutFacture, dateFinFacture,codeArticle).get(0);

    	return totauxAvoir;
    }
    
    public void initValueLignesFacture(String codeArticle) {
//    	valueLignesFacture =  taFactureService.findAllDTOPeriode(dateDebutFacture, dateFinFacture,codeArticle);
    	valueLignesFacture =  taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(dateDebutFacture, dateFinFacture, codeArticle);								
		barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		//Il nous faudra 3 listes pour le graphique, une du Ca calculé (facturé - avoirs), une du Ca facturé et une des avoirs
		//Donc 3 requetes différentes
		listeCaMoisDocumentFacture = taFactureService.listSumCAMoisTotalLigneArticlePeriodeParArticleMoinsAvoir(dateDebutFacture, dateFinFacture,codeArticle);
		listeCaMoisDocumentFactureSeulement = taFactureService.listSumCAMoisTotalLigneArticlePeriodeParArticleMoinsAvoir(dateDebutFacture, dateFinFacture,codeArticle);
 		listeCaMoisDocumentAvoirSeulement = taFactureService.listSumCAMoisTotalLigneArticlePeriodeParArticleMoinsAvoir(dateDebutFacture, dateFinFacture,codeArticle);
		
		barChartModelFacture.addSeries(createBarChartSerieMensuel("CA réel", listeCaMoisDocumentFacture, dateDebutFacture, dateFinFacture));  
		barChartModelFacture.addSeries(createBarChartSerieMensuel("CA facturé", listeCaMoisDocumentFactureSeulement, dateDebutFacture, dateFinFacture)); 
		barChartModelFacture.addSeries(createBarChartSerieMensuel("CA Avoirs", listeCaMoisDocumentAvoirSeulement, dateDebutFacture, dateFinFacture)); 
		valeursDateDebut=dateDebutFacture;
		valeursDateFin=dateFinFacture;
		infosLabelDoc="Factures réalisées";
    	valeursListe=valueLignesFacture;
    }
    
    public void initAllValueFacture(String codeArticle) {
    	initIndicateursFacture(codeArticle);
    	initValueLignesFacture(codeArticle);
    	
    	
    }

    public int nbFactureNonTransformeSurPeriode(String codeArticle) {

    	int nb = (int) taFactureService.countDocumentNonTransforme(dateDebutFacture, dateFinFacture, codeArticle);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTransforme(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireNonTransformeTotalDTO(dateDebutFacture, dateFinFacture,codeArticle);
    	return totaux;
    }
    
    
    public void initValueLignesFactureNonTransforme(String codeArticle) {
//    	valueLignesFacture =  taFactureService.findDocumentNonTransfosDTO(dateDebutFacture, dateFinFacture,codeArticle);
		barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentFacture = taFactureService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutFacture, dateFinFacture, 1,codeArticle);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentFacture, dateDebutFacture, dateFinFacture)); 
    	
    }

    public int nbFactureNonTransformeARelancerSurPeriode(String codeArticle) {

    	int nb = (int) taFactureService.countDocumentNonTransformeARelancer(dateDebutFacture, dateFinFacture, deltaJourARelancer, codeArticle);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureNonTransformeARelancer(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutFacture, dateFinFacture, deltaJourARelancer, codeArticle);
    	return totaux;
    }
    
    public void initValueLignesFactureNonTransformeARelancer(String codeArticle) {
//    	valueLignesFacture =  taFactureService.findDocumentNonTransfosARelancerDTO(dateDebutFacture, dateFinFacture, 15, codeArticle);
		barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentFacture = taFactureService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutFacture, dateFinFacture, 1, deltaJourARelancer, codeArticle);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentFacture, dateDebutFacture, dateFinFacture));  
    	
    }
    
    public void initValueLignesFactureTransforme(String codeArticle) {
//    	valueLignesFacture =  taFactureService.findDocumentTransfosDTO(dateDebutFacture, dateFinFacture,codeArticle);
    	barChartModelFacture = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentFacture = taFactureService.listeChiffreAffaireTransformeJmaDTO(dateDebutFacture, dateFinProforma, 1,codeArticle);
		barChartModelFacture.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentFacture, dateDebutProforma, dateFinProforma));
    }

    public int nbFactureTransformeSurPeriode(String codeArticle) {

    	int nb = (int) taFactureService.countDocumentTransforme(dateDebutFacture, dateFinFacture, codeArticle);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeFactureTransforme(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	DocumentChiffreAffaireDTO totaux = taFactureService.chiffreAffaireTransformeTotalDTO(dateDebutFacture, dateFinFacture,codeArticle);
    	return totaux;
    }

    //Section Proforma
    public void initIndicateursProforma(String codeArticle) {
        montantTotalHtProforma = totauxCaPeriodeProforma(codeArticle).getMtHtCalc();
        montantTotalHtTransfoProforma = totauxCaPeriodeProformaTransforme(codeArticle).getMtHtCalc();
        montantTotalHtNonTransfoProforma = totauxCaPeriodeProformaNonTransforme(codeArticle).getMtHtCalc();
        montantTotalHtNonTransfoARelancerProforma = totauxCaPeriodeProformaNonTransformeARelancer(codeArticle).getMtHtCalc();
        montantTotalTtcProforma = totauxCaPeriodeProforma(codeArticle).getMtTtcCalc();
        montantTotalTtcTransfoProforma = totauxCaPeriodeProformaTransforme(codeArticle).getMtTtcCalc();
        montantTotalTtcNonTransfoProforma = totauxCaPeriodeProformaNonTransforme(codeArticle).getMtTtcCalc();
        montantTotalTtcNonTransfoARelancerProforma = totauxCaPeriodeProformaNonTransformeARelancer(codeArticle).getMtTtcCalc();
        nbProforma = nbProformaSurPeriode(codeArticle);
        nbProformaNonTransforme = nbProformaNonTransformeSurPeriode(codeArticle) ;
        nbProformaTransforme = nbProformaTransformeSurPeriode(codeArticle);
        nbProformaNonTransformeARelancer = nbProformaNonTransformeARelancerSurPeriode(codeArticle);
    }
    
    
	// Retourne le nombre total de documents sur la période
    public int nbProformaSurPeriode(String codeArticle) {

//    	int nb = (int) taProformaService.countDocument(dateDebutProforma, dateFinProforma, codeArticle);
    	int nb = (int) taProformaService.countDocument(dateDebutProforma, dateFinProforma, null, codeArticle);

    	return nb;
    }
    
    public DocumentChiffreAffaireDTO totauxCaPeriodeProforma(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireTotalDTO(dateDebutProforma, dateFinProforma,codeArticle);
    	DocumentChiffreAffaireDTO totaux = taProformaService.sumChiffreAffaireTotalDTOArticle(dateDebutProforma, dateFinProforma,codeArticle).get(0);
    	
    	return totaux;
    }
    
    public void initValueLignesProforma(String codeArticle) {
//    	valueLignesProforma =  taProformaService.findAllDTOPeriode(dateDebutProforma, dateFinProforma,codeArticle);
    	valueLignesProforma =  taProformaService.findAllLigneDTOPeriode(dateDebutCom, dateFinCom, null, codeArticle);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireTotalJmaDTO(dateDebutProforma, dateFinProforma, 1,codeArticle);
		listeCaMoisDocumentProforma = taProformaService.listeSumChiffreAffaireTotalDTOArticleMois(dateDebutCom, dateFinCom, codeArticle, null);
		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proforma réalisés";
    	valeursListe=valueLignesProforma;
    }

    public void initAllValueProforma(String codeArticle) {
    	initIndicateursProforma(codeArticle);
    	initValueLignesProforma(codeArticle);
    	
    	
    }
    
    public int nbProformaNonTransformeSurPeriode(String codeArticle) {

//    	int nb = (int) taProformaService.countDocumentNonTransforme(dateDebutProforma, dateFinProforma, codeArticle);
    	int nb = (int) taProformaService.countDocumentNonTransforme(dateDebutProforma, dateFinProforma, null,codeArticle);

    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeProformaNonTransforme(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireNonTransformeTotalDTO(dateDebutProforma, dateFinProforma,codeArticle);
    	DocumentChiffreAffaireDTO totaux = taProformaService.sumChiffreAffaireTotalDTOArticleNonTransforme(dateDebutCom, dateFinCom, codeArticle).get(0);
    	return totaux;
    }
    
    public void initValueLignesProformaTransforme(String codeArticle) {
//    	valueLignesProforma =  taProformaService.findDocumentTransfosDTO(dateDebutProforma, dateFinProforma,codeArticle);
    	valueLignesProforma =  taProformaService.findDocumentTransfosLigneDTO(dateDebutCom, dateFinCom, null, codeArticle);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireTransformeJmaDTO(dateDebutProforma, dateFinProforma, 1,codeArticle);
		listeCaMoisDocumentProforma = taProformaService.listeSumChiffreAffaireTransformeDTOArticleMois(dateDebutCom, dateFinCom, codeArticle, null);

		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proforma transformés";
    	valeursListe=valueLignesProforma;
    	
    }

    public int nbProformaTransformeSurPeriode(String codeArticle) {

//    	int nb = (int) taProformaService.countDocumentTransforme(dateDebutProforma, dateFinProforma, codeArticle);
    	int nb = (int) taProformaService.countDocumentTransforme(dateDebutProforma, dateFinProforma, null, codeArticle);

    	return nb;
    }


    public DocumentChiffreAffaireDTO totauxCaPeriodeProformaNonTransformeARelancer(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutProforma, dateFinProforma, deltaJourARelancer, codeArticle);
    	DocumentChiffreAffaireDTO totaux = taProformaService.sumChiffreAffaireTotalDTOArticleARelancer(dateDebutCom, dateFinCom, deltaJourARelancer, codeArticle).get(0);

    	return totaux;
    }
    
    public void initValueLignesProformaNonTransformeARelancer(String codeArticle) {
//    	valueLignesProforma =  taProformaService.findDocumentNonTransfosARelancerDTO(dateDebutProforma, dateFinProforma, deltaJourARelancer, codeArticle);
    	valueLignesProforma =  taProformaService.findDocumentNonTransfosARelancerLigneDTO(dateDebutCom, dateFinCom, deltaJourARelancer, null, codeArticle);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutProforma, dateFinProforma, 1, deltaJourARelancer, codeArticle);
		listeCaMoisDocumentProforma = taProformaService.listeSumChiffreAffaireNonTransformeARelancerDTOArticleMois(dateDebutCom, dateFinCom, deltaJourARelancer, codeArticle, null);
		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proforma à relancer";
    	valeursListe=valueLignesProforma;
    }
    
    public int nbProformaNonTransformeARelancerSurPeriode(String codeArticle) {

//    	int nb = (int) taProformaService.countDocumentNonTransformeARelancer(dateDebutProforma, dateFinProforma, deltaJourARelancer, codeArticle);
    	int nb = (int) taProformaService.countDocumentNonTransformeARelancer(dateDebutProforma, dateFinProforma, deltaJourARelancer, null, codeArticle);

    	return nb;
    }

    
    public DocumentChiffreAffaireDTO totauxCaPeriodeProformaTransforme(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taProformaService.chiffreAffaireTransformeTotalDTO(dateDebutProforma, dateFinProforma,codeArticle);
    	DocumentChiffreAffaireDTO totaux = taProformaService.sumChiffreAffaireTotalDTOArticleTransforme(dateDebutCom, dateFinCom, codeArticle).get(0);
    	return totaux;
    }
    
    public void initValueLignesProformaNonTransforme(String codeArticle) {
//    	valueLignesProforma =  taProformaService.findDocumentNonTransfosDTO(dateDebutProforma, dateFinProforma,codeArticle);
    	valueLignesProforma =  taProformaService.findDocumentNonTransfosLigneDTO(dateDebutCom, dateFinCom, null, codeArticle);
		barChartModelProforma = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentProforma = taProformaService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutProforma, dateFinProforma, 1,codeArticle);
		listeCaMoisDocumentProforma = taProformaService.listeSumChiffreAffaireNonTransformeDTOArticleMois(dateDebutProforma, dateFinProforma, codeArticle, null);
		barChartModelProforma.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentProforma, dateDebutProforma, dateFinProforma));
		valeursDateDebut=dateDebutProforma;
		valeursDateFin=dateFinProforma;
		infosLabelDoc="Proforma non transformés";
    	valeursListe=valueLignesProforma;
    }
    
//    Début section Devis
    public void initIndicateursDevis(String codeArticle) {
        montantTotalHtDevis = totauxCaPeriodeDevis(codeArticle).getMtHtCalc();
        montantTotalHtTransfoDevis = totauxCaPeriodeDevisTransforme(codeArticle).getMtHtCalc();
        montantTotalHtNonTransfoDevis = totauxCaPeriodeDevisNonTransforme(codeArticle).getMtHtCalc();
        montantTotalHtNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeArticle).getMtHtCalc();
        montantTotalTtcDevis = totauxCaPeriodeDevis(codeArticle).getMtTtcCalc();
        montantTotalTtcTransfoDevis = totauxCaPeriodeDevisTransforme(codeArticle).getMtTtcCalc();
        montantTotalTtcNonTransfoDevis = totauxCaPeriodeDevisNonTransforme(codeArticle).getMtTtcCalc();
        montantTotalTtcNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeArticle).getMtTtcCalc();
        nbDevis = nbDevisSurPeriode(codeArticle);
        nbDevisNonTransforme = nbDevisNonTransformeSurPeriode(codeArticle) ;
        nbDevisTransforme = nbDevisTransformeSurPeriode(codeArticle);
        nbDevisNonTransformeARelancer = nbDevisNonTransformeARelancerSurPeriode(codeArticle);
    }
    
    
	// Retourne le nombre total de documents sur la période
    public int nbDevisSurPeriode(String codeArticle) {

    	int nb = (int) taDevisService.countDocument(dateDebutDevis, dateFinDevis, null, codeArticle);
    	return nb;
    }
    

    
    public DocumentChiffreAffaireDTO totauxCaPeriodeDevis(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTotalDTO(dateDebutDevis, dateFinDevis,codeArticle); OLD
    	
    	DocumentChiffreAffaireDTO totaux =  taDevisService.sumChiffreAffaireTotalDTOArticle(dateDebutDevis, dateFinDevis,codeArticle).get(0);
    	return totaux;
    }
    
    public void initValueLignesDevis(String codeArticle) {
//    	valueLignesDevis =  taDevisService.findAllDTOPeriode(dateDebutDevis, dateFinDevis,codeArticle);
    	valueLignesDevis =  taDevisService.findAllLigneDTOPeriode(dateDebutDevis, dateFinDevis,null, codeArticle);
    	
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeSumChiffreAffaireTotalDTOArticleMois(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticle, null);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis réalisées";
    	valeursListe=valueLignesDevis;
    }

    public void initAllValueDevis(String codeArticle) {
    	initIndicateursDevis(codeArticle);
    	initValueLignesDevis(codeArticle);
    	
    	
    }
    
    public int nbDevisNonTransformeSurPeriode(String codeArticle) {

    	int nb = (int) taDevisService.countDocumentNonTransforme(dateDebutDevis, dateFinDevis, null, codeArticle);
    	return nb;
    }

    public DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransforme(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeArticle);
    	DocumentChiffreAffaireDTO totaux = taDevisService.sumChiffreAffaireTotalDTOArticleNonTransforme(dateDebutDevis, dateFinDevis, codeArticle).get(0);
    	return totaux;
    }
    
    public void initValueLignesDevisTransforme(String codeArticle) {
//    	valueLignesDevis =  taDevisService.findDocumentTransfosDTO(dateDebutDevis, dateFinDevis,codeArticle);
    	valueLignesDevis =  taDevisService.findDocumentTransfosLigneDTO(dateDebutDevis, dateFinDevis, null, codeArticle);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeArticle);
		listeCaMoisDocumentDevis = taDevisService.listeSumChiffreAffaireTransformeDTOArticleMois(dateDebutDevis, dateFinDevis, codeArticle, null);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis transformés";
    	valeursListe=valueLignesDevis;
    	
    }

    public int nbDevisTransformeSurPeriode(String codeArticle) {

    	int nb = (int) taDevisService.countDocumentTransforme(dateDebutDevis, dateFinDevis, null, codeArticle);
    	return nb;
    }


    public DocumentChiffreAffaireDTO totauxCaPeriodeDevisNonTransformeARelancer(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutDevis, dateFinDevis, 15, codeArticle);
    	DocumentChiffreAffaireDTO totaux = taDevisService.sumChiffreAffaireTotalDTOArticleARelancer(dateDebutDevis, dateFinDevis, deltaJourARelancer, codeArticle).get(0);
    	return totaux;
    }
    
    public void initValueLignesDevisNonTransformeARelancer(String codeArticle) {
//    	valueLignesDevis =  taDevisService.findDocumentNonTransfosARelancerDTO(dateDebutDevis, dateFinDevis, 15, codeArticle);
    	valueLignesDevis =  taDevisService.findDocumentNonTransfosARelancerLigneDTO(dateDebutDevis, dateFinDevis, deltaJourARelancer,null, codeArticle);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutDevis, dateFinDevis, 1, deltaJourARelancer, codeArticle);
		listeCaMoisDocumentDevis = taDevisService.listeSumChiffreAffaireNonTransformeARelancerDTOArticleMois(dateDebutDevis, dateFinDevis, deltaJourARelancer, codeArticle, null);

		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis à relancer";
    	valeursListe=valueLignesDevis;
    }
    
    public int nbDevisNonTransformeARelancerSurPeriode(String codeArticle) {

    	int nb = (int) taDevisService.countDocumentNonTransformeARelancer(dateDebutDevis, dateFinDevis, deltaJourARelancer, null, codeArticle);
    	return nb;
    }

    
    public DocumentChiffreAffaireDTO totauxCaPeriodeDevisTransforme(String codeArticle) {
    	// Calcule les CA (HT, TVA, TTC) total sur la période
//    	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeArticle);
    	DocumentChiffreAffaireDTO totaux = taDevisService.sumChiffreAffaireTotalDTOArticleTransforme(dateDebutDevis, dateFinDevis, codeArticle).get(0);
    	return totaux;
    }
    
    public void initValueLignesDevisNonTransforme(String codeArticle) {
//    	valueLignesDevis =  taDevisService.findDocumentNonTransfosDTO(dateDebutDevis, dateFinDevis,codeArticle);
    	valueLignesDevis =  taDevisService.findDocumentNonTransfosLigneDTO(dateDebutDevis, dateFinDevis,null,codeArticle);
    	
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeArticle);
		listeCaMoisDocumentDevis = taDevisService.listeSumChiffreAffaireNonTransformeDTOArticleMois(dateDebutDevis, dateFinDevis, codeArticle, null);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));
		valeursDateDebut=dateDebutDevis;
		valeursDateFin=dateFinDevis;
		infosLabelDoc="Devis non transformés";
    	valeursListe=valueLignesDevis;
    	
    }    
//    Fin section Devis
    
//  Début section Commandes
  public void initIndicateursCom(String codeArticle) {
      montantTotalHtCom = totauxCaPeriodeCom(codeArticle).getMtHtCalc();
      montantTotalHtTransfoCom = totauxCaPeriodeComTransforme(codeArticle).getMtHtCalc();
      montantTotalHtNonTransfoCom = totauxCaPeriodeComNonTransforme(codeArticle).getMtHtCalc();
      montantTotalHtNonTransfoARelancerCom = totauxCaPeriodeComNonTransformeARelancer(codeArticle).getMtHtCalc();
      montantTotalTtcCom=totauxCaPeriodeCom(codeArticle).getMtTtcCalc();
      montantTotalTtcTransfoCom=totauxCaPeriodeComTransforme(codeArticle).getMtTtcCalc();
      montantTotalTtcNonTransfoCom=totauxCaPeriodeComNonTransforme(codeArticle).getMtTtcCalc();
      montantTotalTtcNonTransfoARelancerCom=totauxCaPeriodeComNonTransformeARelancer(codeArticle).getMtTtcCalc();
      nbCom = nbComSurPeriode(codeArticle);
      nbComNonTransforme = nbComNonTransformeSurPeriode(codeArticle) ;
      nbComTransforme = nbComTransformeSurPeriode(codeArticle);
      nbComNonTransformeARelancer = nbComNonTransformeARelancerSurPeriode(codeArticle);
  }
  
  
	// Retourne le nombre total de documents sur la période
  public int nbComSurPeriode(String codeArticle) {

  	int nb = (int) taBoncdeService.countDocument(dateDebutCom, dateFinCom, null, codeArticle);
  	return nb;
  }
  

  
  public DocumentChiffreAffaireDTO totauxCaPeriodeCom(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
//  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTotalDTO(dateDebutDevis, dateFinDevis,codeArticle); OLD
  	
  	DocumentChiffreAffaireDTO totaux =  taBoncdeService.sumChiffreAffaireTotalDTOArticle(dateDebutCom, dateFinCom,codeArticle).get(0);
  	return totaux;
  }
  
  public void initValueLignesCom(String codeArticle) {
//  	valueLignesCom =  taComService.findAllDTOPeriode(dateDebutCom, dateFinCom,codeArticle);
  	valueLignesCom =  taBoncdeService.findAllLigneDTOPeriode(dateDebutCom, dateFinCom,null, codeArticle);
  	
		barChartModelCom = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentCom = taBoncdeService.listeSumChiffreAffaireTotalDTOArticleMois(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticle, null);
		barChartModelCom.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCom, dateDebutCom, dateFinCom));
		valeursDateDebut=dateDebutCom;
		valeursDateFin=dateFinCom;
		infosLabelDoc="Commandes réalisés";
    	valeursListe=valueLignesCom;
  }

  public void initAllValueCom(String codeArticle) {
  	initIndicateursCom(codeArticle);
  	initValueLignesCom(codeArticle);
  	
  	
  }
  
  public int nbComNonTransformeSurPeriode(String codeArticle) {

  	int nb = (int) taBoncdeService.countDocumentNonTransforme(dateDebutCom, dateFinCom, null, codeArticle);
  	return nb;
  }

  public DocumentChiffreAffaireDTO totauxCaPeriodeComNonTransforme(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
//  	DocumentChiffreAffaireDTO totaux = taComService.chiffreAffaireNonTransformeTotalDTO(dateDebutCom, dateFinCom,codeArticle);
  	DocumentChiffreAffaireDTO totaux = taBoncdeService.sumChiffreAffaireTotalDTOArticleNonTransforme(dateDebutCom, dateFinCom, codeArticle).get(0);
  	return totaux;
  }
  
  public void initValueLignesComTransforme(String codeArticle) {
//  	valueLignesCom =  taComService.findDocumentTransfosDTO(dateDebutCom, dateFinCom,codeArticle);
  	valueLignesCom =  taBoncdeService.findDocumentTransfosLigneDTO(dateDebutCom, dateFinCom, null, codeArticle);
		barChartModelCom = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentCom = taComService.listeChiffreAffaireTransformeJmaDTO(dateDebutCom, dateFinCom, 1,codeArticle);
		listeCaMoisDocumentCom = taBoncdeService.listeSumChiffreAffaireTransformeDTOArticleMois(dateDebutCom, dateFinCom, codeArticle, null);
		barChartModelCom.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCom, dateDebutCom, dateFinCom));
		valeursDateDebut=dateDebutCom;
		valeursDateFin=dateFinCom;
		infosLabelDoc="Commandes transformés";
    	valeursListe=valueLignesCom;
  	
  }

  public int nbComTransformeSurPeriode(String codeArticle) {

  	int nb = (int) taBoncdeService.countDocumentTransforme(dateDebutCom, dateFinCom, null, codeArticle);
  	return nb;
  }


  public DocumentChiffreAffaireDTO totauxCaPeriodeComNonTransformeARelancer(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
//  	DocumentChiffreAffaireDTO totaux = taComService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutCom, dateFinCom, 15, codeArticle);
  	DocumentChiffreAffaireDTO totaux = taBoncdeService.sumChiffreAffaireTotalDTOArticleARelancer(dateDebutCom, dateFinCom, deltaJourARelancer, codeArticle).get(0);
  	return totaux;
  }
  
  public void initValueLignesComNonTransformeARelancer(String codeArticle) {
//  	valueLignesCom =  taComService.findDocumentNonTransfosARelancerDTO(dateDebutCom, dateFinCom, 15, codeArticle);
  	valueLignesCom =  taBoncdeService.findDocumentNonTransfosARelancerLigneDTO(dateDebutCom, dateFinCom, deltaJourARelancer,null, codeArticle);
		barChartModelCom = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentCom = taComService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutCom, dateFinCom, 1, deltaJourARelancer, codeArticle);
		listeCaMoisDocumentCom = taBoncdeService.listeSumChiffreAffaireNonTransformeARelancerDTOArticleMois(dateDebutCom, dateFinCom, deltaJourARelancer, codeArticle, null);

		barChartModelCom.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCom, dateDebutCom, dateFinCom));
		valeursDateDebut=dateDebutCom;
		valeursDateFin=dateFinCom;
		infosLabelDoc="Commandes à relancer";
    	valeursListe=valueLignesCom;
  }
  
  public int nbComNonTransformeARelancerSurPeriode(String codeArticle) {

  	int nb = (int) taBoncdeService.countDocumentNonTransformeARelancer(dateDebutCom, dateFinCom, deltaJourARelancer, null, codeArticle);
  	return nb;
  }

  
  public DocumentChiffreAffaireDTO totauxCaPeriodeComTransforme(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
//  	DocumentChiffreAffaireDTO totaux = taComService.chiffreAffaireTransformeTotalDTO(dateDebutCom, dateFinCom,codeArticle);
  	DocumentChiffreAffaireDTO totaux = taBoncdeService.sumChiffreAffaireTotalDTOArticleTransforme(dateDebutCom, dateFinCom, codeArticle).get(0);
  	return totaux;
  }
  
  public void initValueLignesComNonTransforme(String codeArticle) {
//  	valueLignesCom =  taComService.findDocumentNonTransfosDTO(dateDebutCom, dateFinCom,codeArticle);
  	valueLignesCom =  taBoncdeService.findDocumentNonTransfosLigneDTO(dateDebutCom, dateFinCom,null,codeArticle);
  	
		barChartModelCom = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
//		listeCaMoisDocumentCom = taComService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutCom, dateFinCom, 1,codeArticle);
		listeCaMoisDocumentCom = taBoncdeService.listeSumChiffreAffaireNonTransformeDTOArticleMois(dateDebutCom, dateFinCom, codeArticle, null);
		barChartModelCom.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentCom, dateDebutCom, dateFinCom));
		valeursDateDebut=dateDebutCom;
		valeursDateFin=dateFinCom;
		infosLabelDoc="Commandes non transformés";
    	valeursListe=valueLignesCom;
  	
  }    
//  Fin section Commandes
    
//  Début section Général
  public void initIndicateursGeneral(String codeArticle) {
	  initIndicateursFacture(codeArticle);
	  initIndicateursFactureGeneral(codeArticle);
      montantTotalHtDevis = totauxCaPeriodeDevis(codeArticle).getMtHtCalc();
      montantTotalHtTransfoDevis = totauxCaPeriodeDevisTransforme(codeArticle).getMtHtCalc();
      montantTotalHtNonTransfoDevis = totauxCaPeriodeDevisNonTransforme(codeArticle).getMtHtCalc();
      montantTotalHtNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeArticle).getMtHtCalc();
      montantTotalTtcDevis = totauxCaPeriodeDevis(codeArticle).getMtTtcCalc();
      montantTotalTtcTransfoDevis = totauxCaPeriodeDevisTransforme(codeArticle).getMtTtcCalc();
      montantTotalTtcNonTransfoDevis = totauxCaPeriodeDevisNonTransforme(codeArticle).getMtTtcCalc();
      montantTotalTtcNonTransfoARelancerDevis = totauxCaPeriodeDevisNonTransformeARelancer(codeArticle).getMtTtcCalc();
      nbDevis = nbDevisSurPeriode(codeArticle);
      nbDevisNonTransforme = nbDevisNonTransformeSurPeriode(codeArticle) ;
      nbDevisTransforme = nbDevisTransformeSurPeriode(codeArticle);
      nbDevisNonTransformeARelancer = nbDevisNonTransformeARelancerSurPeriode(codeArticle);
      
      
  }
  
  
	// Retourne le nombre total de documents sur la période
  public int nbGeneralSurPeriode(String codeArticle) {

  	int nb = (int) taDevisService.countDocument(dateDebutDevis, dateFinDevis, codeArticle);
  	return nb;
  }
  
  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneral(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTotalDTO(dateDebutDevis, dateFinDevis,codeArticle);
  	return totaux;
  }
  
  public void initValueLignesGeneral(String codeArticle) {
  	valueLignesDevis =  taDevisService.findAllDTOPeriode(dateDebutDevis, dateFinDevis,codeArticle);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireTotalJmaDTO(dateDebutDevis, dateFinDevis, 1,codeArticle);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));    	
  }

  public void initAllValueGeneral(String codeArticle) {
  	initIndicateursFacture(codeArticle);
  	initIndicateursFactureGeneral(codeArticle);
	initIndicateursDevis(codeArticle);
//  	initValueLignesDevis(codeArticle);
  	
  	
  }
  
  public int nbGeneralNonTransformeSurPeriode(String codeArticle) {

  	int nb = (int) taDevisService.countDocumentNonTransforme(dateDebutDevis, dateFinDevis, codeArticle);
  	return nb;
  }

  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneralNonTransforme(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeArticle);
  	return totaux;
  }
  
  public void initValueLignesGeneralTransforme(String codeArticle) {
  	valueLignesDevis =  taDevisService.findDocumentTransfosDTO(dateDebutDevis, dateFinDevis,codeArticle);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeArticle);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis)); 

  	
  }

  public int nbGeneralTransformeSurPeriode(String codeArticle) {

  	int nb = (int) taDevisService.countDocumentTransforme(dateDebutDevis, dateFinDevis, codeArticle);
  	return nb;
  }


  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneralNonTransformeARelancer(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireNonTransformeARelancerTotalDTO(dateDebutDevis, dateFinDevis, deltaJourARelancer, codeArticle);
  	return totaux;
  }
  
  public void initValueLignesGeneralNonTransformeARelancer(String codeArticle) {
  	valueLignesDevis =  taDevisService.findDocumentNonTransfosARelancerDTO(dateDebutDevis, dateFinDevis, deltaJourARelancer, codeArticle);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeARelancerJmaDTO(dateDebutDevis, dateFinDevis, 1, deltaJourARelancer, codeArticle);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis));     	
  }
  
  public int nbGeneralNonTransformeARelancerSurPeriode(String codeArticle) {

  	int nb = (int) taDevisService.countDocumentNonTransformeARelancer(dateDebutDevis, dateFinDevis, deltaJourARelancer, codeArticle);
  	return nb;
  }

  
  public DocumentChiffreAffaireDTO totauxCaPeriodeGeneralTransforme(String codeArticle) {
  	// Calcule les CA (HT, TVA, TTC) total sur la période
  	DocumentChiffreAffaireDTO totaux = taDevisService.chiffreAffaireTransformeTotalDTO(dateDebutDevis, dateFinDevis,codeArticle);
  	return totaux;
  }
  
  public void initValueLignesGeneralNonTransforme(String codeArticle) {
  	valueLignesDevis =  taDevisService.findDocumentNonTransfosDTO(dateDebutDevis, dateFinDevis,codeArticle);
		barChartModelDevis = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
		listeCaMoisDocumentDevis = taDevisService.listeChiffreAffaireNonTransformeJmaDTO(dateDebutDevis, dateFinDevis, 1,codeArticle);
		barChartModelDevis.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocumentDevis, dateDebutDevis, dateFinDevis)); 
  	
  }    
//  Fin section Général
  
// Debut section tiers
  public void initAllValueTiers(String codeArticle) {
	  initIndicateurTiers(codeArticle); 	
	  initValueLignesTiers(codeArticle);
  }
  public void initIndicateurTiers (String codeArticle) {
	  nbTiersAyantAcheterArticle = nbTiersAyantAcheterArticleSurPeriode(codeArticle);
  }
  public void initValueLignesTiers (String codeArticle) {
	  valueLignesTiers = taFactureService.listTiersAyantAcheterArticleDTO(dateDebutTiers, dateFinTiers, codeArticle);
	  valeursDateDebut=dateDebutTiers;
	  valeursDateFin=dateFinTiers;
	  infosLabelDoc="Tiers ayant achetés cet article";
      valeursListe=valueLignesTiers;
  }
 
  public int nbTiersAyantAcheterArticleSurPeriode(String codeArticle) {
	  int nb = (int) taFactureService.countTiersAyantAcheterArticleSurPeriode(dateDebutTiers, dateFinTiers, codeArticle);
		return nb;  	
  }
//Fin section tiers
  
  
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
//				param.setArticlePourReglementLibre(null);
				param.setOriginePaiement("BDG Dashboard Article - Facture");
				PaiementCbController.actDialoguePaiementEcheanceParCarte(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		initIndicateursFacture(codeArticleCourant);
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
  
public void actImprimerListeLignesParArticle(String libelleArticle) {		
		
		try {
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", getValeursDateDebut());
		mapParametre.put("fin", getValeursDateFin());
		mapParametre.put("libelleArticle", libelleArticle);
		mapParametre.put("infosLabel", infosLabelDoc);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("lignesParArticle", valeursListe);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public void actImprimerInterfacePrintArticle(String codeArticle, String liblArticle, double cahtArticle, int stockArticle) {

	if(codeArticle!=null) {

	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

	Map<String, Object> sessionMap = externalContext.getSessionMap();
	
	Map<String,Object> mapParametre = new HashMap<String,Object>();
	
	sessionMap.remove("article");
	sessionMap.remove("parametre");
	sessionMap.remove("parametreFactureAvoirRealise");
	
	sessionMap.remove("lignesParArticleFactureRealise");
	
	sessionMap.remove("lignesParTiersAchatArticle");
	
	sessionMap.remove("lignesParArticleDevisRealise");
	sessionMap.remove("lignesParArticleDevisNonTransforme");
	sessionMap.remove("lignesParArticleDevisARelancer");
	sessionMap.remove("lignesParArticleDevisTransforme");
	sessionMap.remove("lignesParArticleCommandeRealise");
	sessionMap.remove("lignesParArticleCommandeNonTransforme");
	sessionMap.remove("lignesParArticleCommandeARelancer");
	sessionMap.remove("lignesParArticleCommandeTransforme");
	
	sessionMap.remove("lignesParArticleProformaRealise");
	sessionMap.remove("lignesParArticleProformaNonTransforme");
	sessionMap.remove("lignesParArticleProformaARelancer");
	sessionMap.remove("lignesParArticleProformaTransforme");
	boolean identificationArticle=false;
	boolean breakPageEtatFactureAvoir=false;
	boolean breakPageEtatTiers=false;
	boolean breakPageEtatDevis=false;
	boolean breakPageEtatCommande=false;
	boolean breakPageEtatProforma=false;
	
	if (identification.equals("Aucune")) {
		sessionMap.remove("parametre");
		try {
			selectedTaArticleDTO.setCodeArticle(codeArticle);
		Object article = taArticleService.findByCode(selectedTaArticleDTO.getCodeArticle());				
		Object statistique = taFactureService.chiffreAffaireTotalDTO(dateDebutGeneral, dateFinGeneral,null);
		
			if (article!=null) {
				
				sessionMap.put("parametre", mapParametre);
				sessionMap.put("article", article);
				sessionMap.put("statistique", statistique);		
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (identification.equals("Sommaire")) {
		sessionMap.remove("parametre");
		try {
			selectedTaArticleDTO.setCodeArticle(codeArticle);
		Object article = taArticleService.findByCode(selectedTaArticleDTO.getCodeArticle());				
		Object statistique = taFactureService.chiffreAffaireTotalDTO(dateDebutGeneral, dateFinGeneral,null);
		
		if (article!=null) {
			identificationArticle=true;
			mapParametre.put("liblArticle", liblArticle);
			mapParametre.put("identificationArticleSommaire", identificationArticle);
			mapParametre.put("infosLabelIdentification", "Info sommaire de l'article");
			mapParametre.put("caHtArticlePeriode",montantTotalHtFactureGeneral);
		
		if (isInclureDescription()==true) {
			mapParametre.put("boolDescription",inclureDescription);
		}
						
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("article", article);
			sessionMap.put("statistique", statistique);		
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (identification.equals("Etendu")) {
		try {
			
			selectedTaArticleDTO.setCodeArticle(codeArticle);
		Object article = taArticleService.findByCode(selectedTaArticleDTO.getCodeArticle());
		Object statistique = taFactureService.chiffreAffaireTotalDTO(dateDebutGeneral, dateFinGeneral,null);
			
		if (article!=null) {
			
			nbDevis = nbDevisSurPeriode(codeArticle);
			nbFacture = nbFactureSurPeriode(codeArticle);
			nbProforma = nbProformaSurPeriode(codeArticle);
			nbCom = nbComSurPeriode(codeArticle);
			nbTiersAyantAcheterArticle = nbTiersAyantAcheterArticleSurPeriode(codeArticle);
			montantTotalTtcNonRegleFacture = totauxCaPeriodeFactureNonTransforme(codeArticle).getResteAReglerComplet();
			
			mapParametre.put("liblArticle", liblArticle);
			mapParametre.put("identificationArticleEtendu", identification);
			mapParametre.put("infosLabelIdentification", "Info étendu de l'article");
			mapParametre.put("devisEncours",nbDevis);
			mapParametre.put("factureEncours",nbFacture);
			mapParametre.put("proformaEncours",nbProforma);
			mapParametre.put("commandeEncours",nbCom);
			mapParametre.put("tiersEncours",nbTiersAyantAcheterArticle);
			mapParametre.put("caHtArticleGeneral",cahtArticle);
			mapParametre.put("caHtArticlePeriode",montantTotalHtFactureGeneral);
			mapParametre.put("caHtArticlePeriodeMoinsAvoir",montantTotalHtFactureMoinsAvoirGeneral);
			mapParametre.put("caHtArticlePeriodeAvoir",montantTotalHtFactureAvoirGeneral);
			mapParametre.put("stockArticle",stockArticle);
			
		if (isInclureDescription()==true) {
			mapParametre.put("boolDescription",inclureDescription);
		}
		
		if (isInclureStatistique()==true) {
			mapParametre.put("boolStatistique",inclureStatistique);
		}
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("article", article);
			sessionMap.put("statistique", statistique);
					
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (factureAvoirRealise==true) {
		try {
			valueLignesFacture =  taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(dateDebutGeneral, dateFinGeneral, codeArticle);					
			
		if (valueLignesFacture!=null) {
			breakPageEtatFactureAvoir=true;
			mapParametre.put("debutFactureRealise", getDateDebutGeneral());
			mapParametre.put("finFactureRealise", getDateFinGeneral());
			mapParametre.put("liblArticle", liblArticle);
			mapParametre.put("factureAvoirRealise", factureAvoirRealise);
			mapParametre.put("infosLabelFactureAvoirRealise", "réalisées");
			mapParametre.put("breakPageEtatFactureAvoir", breakPageEtatFactureAvoir);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleFactureRealise", valueLignesFacture);
						
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	
	
	if (tiersAyantAcheter==true) {
	try {
		valueLignesTiers = taFactureService.listTiersAyantAcheterArticleDTO(dateDebutGeneral, dateFinGeneral, codeArticle);			
		
	if (valueLignesTiers!=null) {
		breakPageEtatTiers=true;
		mapParametre.put("debut", getDateDebutGeneral());
		mapParametre.put("fin", getDateFinGeneral());
		mapParametre.put("libelleArticle", liblArticle);
		mapParametre.put("tiersAyantAcheter", tiersAyantAcheter);
		mapParametre.put("infosLabelTiers", "tiers ayant achetés");
		mapParametre.put("breakPageEtatTiers", breakPageEtatTiers);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("lignesParTiersAyantAcheteArticle", valueLignesTiers);
					
	}
	
	
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
		
	if (devisRealise==true) {
		try {
			valueLignesDevis = taDevisService.findAllLigneDTOPeriode(dateDebutGeneral, dateFinGeneral,null, codeArticle);				
		
		if (valueLignesDevis!=null) {
			breakPageEtatDevis=true;
			mapParametre.put("dateDebutDevis", getDateDebutGeneral());
			mapParametre.put("dateFinDevis", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("devisRealise", devisRealise);
			
			mapParametre.put("infosLabeldevisRealise", "Liste des devis Réalisé incluant l' article ");
			mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleDevisRealise", valueLignesDevis);
						
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (devisNonTransforme==true) {
		try {
			valueLignesDevisNonTransforme =  taDevisService.findDocumentNonTransfosLigneDTO(dateDebutGeneral, dateFinGeneral,null,codeArticle);				
		
		if (valueLignesDevisNonTransforme!=null) {
			breakPageEtatDevis=true;
			mapParametre.put("dateDebutDevis", getDateDebutGeneral());
			mapParametre.put("dateFinDevis", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("devisNonTransforme", devisNonTransforme);
			mapParametre.put("infosLabeldevisNonTransforme", "Devis non transformés de l'article ");
			mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleDevisNonTransforme", valueLignesDevisNonTransforme);
						
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (devisARelancer==true) {
		try {
			valueLignesDevisARelancer = taDevisService.findDocumentNonTransfosARelancerLigneDTO(dateDebutGeneral, dateFinGeneral, deltaJourARelancer,null, codeArticle);				
		
		if (valueLignesDevisARelancer!=null) {
			breakPageEtatDevis=true;
			mapParametre.put("dateDebutDevis", getDateDebutGeneral());
			mapParametre.put("dateFinDevis", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("devisARelancer", devisARelancer);
			mapParametre.put("infosLabeldevisARelancer", "Devis à relancer de cet article ");
			mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleDevisARelancer", valueLignesDevisARelancer);
						
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (devisTransforme==true) {
		try {
			valueLignesDevisTransforme =  taDevisService.findDocumentTransfosLigneDTO(dateDebutGeneral, dateFinGeneral, null, codeArticle);				
		
		if (valueLignesDevisTransforme!=null) {
			breakPageEtatDevis=true;
			mapParametre.put("dateDebutDevis", getDateDebutGeneral());
			mapParametre.put("dateFinDevis", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("devisTransforme", devisTransforme);
			mapParametre.put("infosLabeldevisTransforme", "Devis tranformés de l'article ");
			mapParametre.put("breakPageEtatDevis", breakPageEtatDevis);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleDevisTransforme", valueLignesDevisTransforme);
						
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (commandeRealise==true) {
		try {
			valueLignesCom =  taBoncdeService.findAllLigneDTOPeriode(dateDebutGeneral, dateFinGeneral,null, codeArticle);			
		
		if (valueLignesCom!=null) {
			breakPageEtatCommande=true;
			mapParametre.put("dateDebutCom", getDateDebutGeneral());
			mapParametre.put("dateFinCom", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("commandeRealise", commandeRealise);
			mapParametre.put("infosLabelcommandeRealise", "Commande Réalisé de l'article ");
			mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleCommandeRealise", valueLignesCom);							
		}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (commandeNonTransforme==true) {
		try {
			valueLignesComNonTransforme =  taBoncdeService.findDocumentNonTransfosLigneDTO(dateDebutGeneral, dateFinGeneral,null,codeArticle);			
		
		if (valueLignesComNonTransforme!=null) {
			breakPageEtatCommande=true;
			mapParametre.put("dateDebutCom", getDateDebutGeneral());
			mapParametre.put("dateFinCom", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("commandeNonTransforme", commandeNonTransforme);
			mapParametre.put("infosLabelcommandeNonTransforme", "Commande non tranformés de l'article ");
			mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleCommandeNonTransforme", valueLignesComNonTransforme);						
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (commandeARelancer==true) {
		try {
			valueLignesComARelancer =  taBoncdeService.findDocumentNonTransfosARelancerLigneDTO(dateDebutGeneral, dateFinGeneral, deltaJourARelancer,null, codeArticle);				
		
		if (valueLignesComARelancer!=null) {
			breakPageEtatCommande=true;
			mapParametre.put("dateDebutCom", getDateDebutGeneral());
			mapParametre.put("dateFinCom", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("commandeARelancer", commandeARelancer);
			mapParametre.put("infosLabelcommandeARelancer", "Commande à relancer de l'article ");
			mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleCommandeARelancer", valueLignesComARelancer);				
		}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (commandeTransforme==true) {
		try {
			valueLignesComTransforme =  taBoncdeService.findDocumentTransfosLigneDTO(dateDebutGeneral, dateFinGeneral, null, codeArticle);				
		
		if (valueLignesComTransforme!=null) {
			breakPageEtatCommande=true;
			mapParametre.put("dateDebutCom", getDateDebutGeneral());
			mapParametre.put("dateFinCom", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("commandeTransforme", commandeTransforme);
			mapParametre.put("infosLabelcommandeTransforme", "Commande tranformés de l'article ");
			mapParametre.put("breakPageEtatCommande", breakPageEtatCommande);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleCommandeTransforme", valueLignesComTransforme);							
		}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	
	if (proformaRealise==true) {
		try {
			valueLignesProforma =  taProformaService.findAllLigneDTOPeriode(dateDebutGeneral, dateFinGeneral, null, codeArticle);			
		
		if (valueLignesProforma!=null) {
			breakPageEtatProforma=true;
			mapParametre.put("dateDebutProforma", getDateDebutGeneral());
			mapParametre.put("dateFinProforma", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("proformaRealise", proformaRealise);
			mapParametre.put("infosLabelproformaRealise", "Proforma Réalisé de l'article ");
			mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleProformaRealise", valueLignesProforma);							
		}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (proformaNonTransforme==true) {
		try {
			valueLignesProformaNonTransforme =  taProformaService.findDocumentNonTransfosLigneDTO(dateDebutGeneral, dateFinGeneral, null, codeArticle);			
		
		if (valueLignesProformaNonTransforme!=null) {
			breakPageEtatProforma=true;
			mapParametre.put("dateDebutProforma", getDateDebutGeneral());
			mapParametre.put("dateFinProforma", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("proformaNonTransforme", proformaNonTransforme);
			mapParametre.put("infosLabelproformaNonTransforme", "Proforma non tranformés de l'article ");
			mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleProformaNonTransforme", valueLignesProformaNonTransforme);						
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (proformaARelancer==true) {
		try {
			valueLignesProformaARelancer =  taProformaService.findDocumentNonTransfosARelancerLigneDTO(dateDebutGeneral, dateFinGeneral, deltaJourARelancer, null, codeArticle);				
		
		if (valueLignesProformaARelancer!=null) {
			breakPageEtatProforma=true;
			mapParametre.put("dateDebutProforma", getDateDebutGeneral());
			mapParametre.put("dateFinProforma", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("proformaARelancer", proformaARelancer);
			mapParametre.put("infosLabelproformaARelancer", "Proforma à relancer de l'article ");
			mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleProformaARelancer", valueLignesProformaARelancer);				
		}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	if (proformaTransforme==true) {
		try {
			valueLignesProformaTransforme =  taProformaService.findDocumentTransfosLigneDTO(dateDebutGeneral, dateFinGeneral, null, codeArticle);				
		
		if (valueLignesProformaTransforme!=null) {
			breakPageEtatProforma=true;
			mapParametre.put("dateDebutProforma", getDateDebutGeneral());
			mapParametre.put("dateFinProforma", getDateFinGeneral());
			mapParametre.put("libelleArticle", liblArticle);
			mapParametre.put("proformaTransforme", proformaTransforme);
			mapParametre.put("infosLabelproformaTransforme", "Proforma tranformés de l'article ");
			mapParametre.put("breakPageEtatProforma", breakPageEtatProforma);
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("lignesParArticleProformaTransforme", valueLignesProformaTransforme);							
		}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	}
	
}
	
public void decocher() {
	
	identification="Aucune";
	factureAvoirRealise=false;
	tiersAyantAcheter=false;
	devisRealise=false;
	devisNonTransforme=false;
	devisARelancer=false;
	devisTransforme=false;
	commandeRealise=false;
	commandeNonTransforme=false;
	commandeARelancer=false;
	commandeTransforme=false;
	proformaRealise=false;
	proformaNonTransforme=false;
	proformaARelancer=false;
	proformaTransforme=false;
	inclureDescription=false;
	inclureStatistique=false;
	
}

public void cocher() {
	
	identification="Etendu";
	factureAvoirRealise=true;
	tiersAyantAcheter=true;
	devisRealise=true;
	devisNonTransforme=true;
	devisARelancer=true;
	devisTransforme=true;
	commandeRealise=true;
	commandeNonTransforme=true;
	commandeARelancer=true;
	commandeTransforme=true;
	proformaRealise=true;
	proformaNonTransforme=true;
	proformaARelancer=true;
	proformaTransforme=true;
	inclureDescription=true;
	inclureStatistique=true;
	
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
	public BarChartModel getBarChartModelArticle() {
		return barChartModelArticle;
	}
	public void setBarChartModelArticleExo(BarChartModel barChartModelArticle) {
		this.barChartModelArticle = barChartModelArticle;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public String getCodeEtatArticle() {
		return codeEtatArticle;
	}

	public void setCodeEtatArticle(String codeEtatArticle) {
		this.codeEtatArticle = codeEtatArticle;
	}

	public List<TaArticleDTO> getValueLignesArticle() {
		return valueLignesArticle;
	}

	public void setValueLignesArticle(List<TaArticleDTO> valueLignesArticle) {
		this.valueLignesArticle = valueLignesArticle;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeArticle() {
		return listeCaPeriodeArticle;
	}

	public void setListeCaPeriodeArticle(List<DocumentChiffreAffaireDTO> listeCaPeriodeArticle) {
		this.listeCaPeriodeArticle = listeCaPeriodeArticle;
	}

	public int getNbArticleActif() {
		return nbArticleActif;
	}

	public void setNbArticleActif(int nbArticleActif) {
		this.nbArticleActif = nbArticleActif;
	}

	public int getNbArticleNonTransformeSurPeriode() {
		return nbArticleNonTransformeSurPeriode;
	}

	public void setNbArticleNonTransformeSurPeriode(int nbArticleNonTransformeSurPeriode) {
		this.nbArticleNonTransformeSurPeriode = nbArticleNonTransformeSurPeriode;
	}


	public int getNbArticleNonTransformeARelancerSurPeriode() {
		return nbArticleNonTransformeARelancerSurPeriode;
	}


	public void setNbArticleNonTransformeARelancerSurPeriode(int nbArticleNonTransformeARelancerSurPeriode) {
		this.nbArticleNonTransformeARelancerSurPeriode = nbArticleNonTransformeARelancerSurPeriode;
	}


	public String getcSbNbArticleSurPeriode() {
		return cSbNbArticleSurPeriode;
	}


	public void setcSbNbArticleSurPeriode(String cSbNbArticleSurPeriode) {
		this.cSbNbArticleSurPeriode = cSbNbArticleSurPeriode;
	}


	public String getcSbNbArticleNonTransformeSurPeriode() {
		return cSbNbArticleNonTransformeSurPeriode;
	}


	public void setcSbNbArticleNonTransformeSurPeriode(String cSbNbArticleNonTransformeSurPeriode) {
		this.cSbNbArticleNonTransformeSurPeriode = cSbNbArticleNonTransformeSurPeriode;
	}


	public String getcSbNbArticleNonTransformeARelancerSurPeriode() {
		return cSbNbArticleNonTransformeARelancerSurPeriode;
	}


	public void setcSbNbArticleNonTransformeARelancerSurPeriode(String cSbNbArticleNonTransformeARelancerSurPeriode) {
		this.cSbNbArticleNonTransformeARelancerSurPeriode = cSbNbArticleNonTransformeARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeArticleNonTransforme() {
		return listeCaPeriodeArticleNonTransforme;
	}


	public void setListeCaPeriodeArticleNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransforme) {
		this.listeCaPeriodeArticleNonTransforme = listeCaPeriodeArticleNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbArticleTransformeSurPeriode() {
		return nbArticleTransformeSurPeriode;
	}


	public void setNbArticleTransformeSurPeriode(int nbArticleTransformeSurPeriode) {
		this.nbArticleTransformeSurPeriode = nbArticleTransformeSurPeriode;
	}


	public BigDecimal getmontantTotalHtNonTransfoARelancer() {
		return montantTotalHtNonTransfoARelancer;
	}


	public void setmontantTotalHtNonTransfoARelancer(BigDecimal montantTotalHtNonTransfoARelancer) {
		montantTotalHtNonTransfoARelancer = montantTotalHtNonTransfoARelancer;
	}


	public BigDecimal getmontantTotalHtTransfo() {
		return montantTotalHtTransfo;
	}


	public void setmontantTotalHtTransfo(BigDecimal montantTotalHtTransfo) {
		montantTotalHtTransfo = montantTotalHtTransfo;
	}


	public String getcSbNbArticleTransformeSurPeriode() {
		return cSbNbArticleTransformeSurPeriode;
	}


	public void setcSbNbArticleTransformeSurPeriode(String cSbNbArticleTransformeSurPeriode) {
		this.cSbNbArticleTransformeSurPeriode = cSbNbArticleTransformeSurPeriode;
	}


	public BigDecimal getmontantTotalHt() {
		return montantTotalHt;
	}


	public void setmontantTotalHt(BigDecimal montantTotalHt) {
		montantTotalHt = montantTotalHt;
	}


	public BigDecimal getmontantTotalHtNonTransfo() {
		return montantTotalHtNonTransfo;
	}


	public void setmontantTotalHtNonTransfo(BigDecimal montantTotalHtNonTransfo) {
		montantTotalHtNonTransfo = montantTotalHtNonTransfo;
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


	public List<DocumentChiffreAffaireDTO> getValueLignesFacture() {
		return valueLignesFacture;
	}


	public void setValueLignesFacture(List<DocumentChiffreAffaireDTO> valueLignesFacture) {
		this.valueLignesFacture = valueLignesFacture;
	}
//	public List<DocumentDTO> getValueLignesFacture() {
//		return valueLignesFacture;
//	}
//
//
//	public void setValueLignesFacture(List<DocumentDTO> valueLignesFacture) {
//		this.valueLignesFacture = valueLignesFacture;
//	}


	public BigDecimal getmontantTotalHtFacture() {
		return montantTotalHtFacture;
	}


	public void setmontantTotalHtFacture(BigDecimal montantTotalHtFacture) {
		montantTotalHtFacture = montantTotalHtFacture;
	}


	public BigDecimal getmontantTotalHtTransfoFacture() {
		return montantTotalHtTransfoFacture;
	}


	public void setmontantTotalHtTransfoFacture(BigDecimal montantTotalHtTransfoFacture) {
		montantTotalHtTransfoFacture = montantTotalHtTransfoFacture;
	}


	public BigDecimal getmontantTotalHtNonTransfoFacture() {
		return montantTotalHtNonTransfoFacture;
	}


	public void setmontantTotalHtNonTransfoFacture(BigDecimal montantTotalHtNonTransfoFacture) {
		montantTotalHtNonTransfoFacture = montantTotalHtNonTransfoFacture;
	}


	public BigDecimal getmontantTotalHtNonTransfoARelancerFacture() {
		return montantTotalHtNonTransfoARelancerFacture;
	}


	public void setmontantTotalHtNonTransfoARelancerFacture(BigDecimal montantTotalHtNonTransfoARelancerFacture) {
		montantTotalHtNonTransfoARelancerFacture = montantTotalHtNonTransfoARelancerFacture;
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
		montantTotalHtProforma = montantTotalHtProforma;
	}


	public BigDecimal getmontantTotalHtTransfoProforma() {
		return montantTotalHtTransfoProforma;
	}


	public void setmontantTotalHtTransfoProforma(BigDecimal montantTotalHtTransfoProforma) {
		montantTotalHtTransfoProforma = montantTotalHtTransfoProforma;
	}


	public BigDecimal getmontantTotalHtNonTransfoProforma() {
		return montantTotalHtNonTransfoProforma;
	}


	public void setmontantTotalHtNonTransfoProforma(BigDecimal montantTotalHtNonTransfoProforma) {
		montantTotalHtNonTransfoProforma = montantTotalHtNonTransfoProforma;
	}


	public BigDecimal getmontantTotalHtNonTransfoARelancerProforma() {
		return montantTotalHtNonTransfoARelancerProforma;
	}


	public void setmontantTotalHtNonTransfoARelancerProforma(BigDecimal montantTotalHtNonTransfoARelancerProforma) {
		montantTotalHtNonTransfoARelancerProforma = montantTotalHtNonTransfoARelancerProforma;
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


	public String getCodeArticleCourant() {
		return codeArticleCourant;
	}


	public void setCodeArticleCourant(String codeArticleCourant) {
		this.codeArticleCourant = codeArticleCourant;
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

	public void setBarChartModelArticle(BarChartModel barChartModelArticle) {
		this.barChartModelArticle = barChartModelArticle;
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

	public BigDecimal getMontantTotalHtFactureAvoir() {
		return montantTotalHtFactureAvoir;
	}

	public void setMontantTotalHtFactureAvoir(BigDecimal montantTotalHtFactureAvoir) {
		this.montantTotalHtFactureAvoir = montantTotalHtFactureAvoir;
	}

	public BigDecimal getMontantTotalHtFactureMoinsAvoir() {
		return montantTotalHtFactureMoinsAvoir;
	}

	public void setMontantTotalHtFactureMoinsAvoir(BigDecimal montantTotalHtFactureMoinsAvoir) {
		this.montantTotalHtFactureMoinsAvoir = montantTotalHtFactureMoinsAvoir;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentFactureSeulement() {
		return listeCaMoisDocumentFactureSeulement;
	}

	public void setListeCaMoisDocumentFactureSeulement(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentFactureSeulement) {
		this.listeCaMoisDocumentFactureSeulement = listeCaMoisDocumentFactureSeulement;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentAvoirSeulement() {
		return listeCaMoisDocumentAvoirSeulement;
	}

	public void setListeCaMoisDocumentAvoirSeulement(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentAvoirSeulement) {
		this.listeCaMoisDocumentAvoirSeulement = listeCaMoisDocumentAvoirSeulement;
	}

	public int getNbTiersAyantAcheterArticle() {
		return nbTiersAyantAcheterArticle;
	}

	public void setNbTiersAyantAcheterArticle(int nbTiersAyantAcheterArticle) {
		this.nbTiersAyantAcheterArticle = nbTiersAyantAcheterArticle;
	}

	public Date getDateDebutTiers() {
		return dateDebutTiers;
	}

	public void setDateDebutTiers(Date dateDebutTiers) {
		this.dateDebutTiers = dateDebutTiers;
	}

	public Date getDateFinTiers() {
		return dateFinTiers;
	}

	public void setDateFinTiers(Date dateFinTiers) {
		this.dateFinTiers = dateFinTiers;
	}

	public List<DocumentChiffreAffaireDTO> getValueLignesTiers() {
		return valueLignesTiers;
	}

	public void setValueLignesTiers(List<DocumentChiffreAffaireDTO> valueLignesTiers) {
		this.valueLignesTiers = valueLignesTiers;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}

	public String getTypeFacture() {
		return typeFacture;
	}

	public void setTypeFacture(String typeFacture) {
		this.typeFacture = typeFacture;
	}

	public String getTypeAvoir() {
		return typeAvoir;
	}

	public void setTypeAvoir(String typeAvoir) {
		this.typeAvoir = typeAvoir;
	}

	public int getDeltaJourARelancer() {
		return deltaJourARelancer;
	}

	public void setDeltaJourARelancer(int deltaJourARelancer) {
		this.deltaJourARelancer = deltaJourARelancer;
	}

	public Date getDateDebutCom() {
		return dateDebutCom;
	}

	public void setDateDebutCom(Date dateDebutCom) {
		this.dateDebutCom = dateDebutCom;
	}

	public Date getDateFinCom() {
		return dateFinCom;
	}

	public void setDateFinCom(Date dateFinCom) {
		this.dateFinCom = dateFinCom;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaMoisDocumentCom() {
		return listeCaMoisDocumentCom;
	}

	public void setListeCaMoisDocumentCom(List<DocumentChiffreAffaireDTO> listeCaMoisDocumentCom) {
		this.listeCaMoisDocumentCom = listeCaMoisDocumentCom;
	}

	public List<DocumentDTO> getValueLignesCom() {
		return valueLignesCom;
	}

	public void setValueLignesCom(List<DocumentDTO> valueLignesCom) {
		this.valueLignesCom = valueLignesCom;
	}

	public BarChartModel getBarChartModelCom() {
		return barChartModelCom;
	}

	public void setBarChartModelCom(BarChartModel barChartModelCom) {
		this.barChartModelCom = barChartModelCom;
	}

	public BigDecimal getMontantTotalHtCom() {
		return montantTotalHtCom;
	}

	public void setMontantTotalHtCom(BigDecimal montantTotalHtCom) {
		this.montantTotalHtCom = montantTotalHtCom;
	}

	public BigDecimal getMontantTotalHtTransfoCom() {
		return montantTotalHtTransfoCom;
	}

	public void setMontantTotalHtTransfoCom(BigDecimal montantTotalHtTransfoCom) {
		this.montantTotalHtTransfoCom = montantTotalHtTransfoCom;
	}

	public BigDecimal getMontantTotalHtNonTransfoCom() {
		return montantTotalHtNonTransfoCom;
	}

	public void setMontantTotalHtNonTransfoCom(BigDecimal montantTotalHtNonTransfoCom) {
		this.montantTotalHtNonTransfoCom = montantTotalHtNonTransfoCom;
	}

	public BigDecimal getMontantTotalHtNonTransfoARelancerCom() {
		return montantTotalHtNonTransfoARelancerCom;
	}

	public void setMontantTotalHtNonTransfoARelancerCom(BigDecimal montantTotalHtNonTransfoARelancerCom) {
		this.montantTotalHtNonTransfoARelancerCom = montantTotalHtNonTransfoARelancerCom;
	}

	public int getNbCom() {
		return nbCom;
	}

	public void setNbCom(int nbCom) {
		this.nbCom = nbCom;
	}

	public int getNbComNonTransforme() {
		return nbComNonTransforme;
	}

	public void setNbComNonTransforme(int nbComNonTransforme) {
		this.nbComNonTransforme = nbComNonTransforme;
	}

	public int getNbComTransforme() {
		return nbComTransforme;
	}

	public void setNbComTransforme(int nbComTransforme) {
		this.nbComTransforme = nbComTransforme;
	}

	public int getNbComNonTransformeARelancer() {
		return nbComNonTransformeARelancer;
	}

	public void setNbComNonTransformeARelancer(int nbComNonTransformeARelancer) {
		this.nbComNonTransformeARelancer = nbComNonTransformeARelancer;
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

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeArticleNonTransformeARelancer() {
		return listeCaPeriodeArticleNonTransformeARelancer;
	}

	public void setListeCaPeriodeArticleNonTransformeARelancer(List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransformeARelancer) {
		this.listeCaPeriodeArticleNonTransformeARelancer = listeCaPeriodeArticleNonTransformeARelancer;
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

	public BigDecimal getMontantTotalTtcCom() {
		return montantTotalTtcCom;
	}

	public void setMontantTotalTtcCom(BigDecimal montantTotalTtcCom) {
		this.montantTotalTtcCom = montantTotalTtcCom;
	}

	public BigDecimal getMontantTotalTtcTransfoCom() {
		return montantTotalTtcTransfoCom;
	}

	public void setMontantTotalTtcTransfoCom(BigDecimal montantTotalTtcTransfoCom) {
		this.montantTotalTtcTransfoCom = montantTotalTtcTransfoCom;
	}

	public BigDecimal getMontantTotalTtcNonTransfoCom() {
		return montantTotalTtcNonTransfoCom;
	}

	public void setMontantTotalTtcNonTransfoCom(BigDecimal montantTotalTtcNonTransfoCom) {
		this.montantTotalTtcNonTransfoCom = montantTotalTtcNonTransfoCom;
	}

	public BigDecimal getMontantTotalTtcNonTransfoARelancerCom() {
		return montantTotalTtcNonTransfoARelancerCom;
	}

	public void setMontantTotalTtcNonTransfoARelancerCom(BigDecimal montantTotalTtcNonTransfoARelancerCom) {
		this.montantTotalTtcNonTransfoARelancerCom = montantTotalTtcNonTransfoARelancerCom;
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

		public BigDecimal getMontantTotalHtFactureMoinsAvoirGeneral() {
			return montantTotalHtFactureMoinsAvoirGeneral;
		}

		public void setMontantTotalHtFactureMoinsAvoirGeneral(BigDecimal montantTotalHtFactureMoinsAvoirGeneral) {
			this.montantTotalHtFactureMoinsAvoirGeneral = montantTotalHtFactureMoinsAvoirGeneral;
		}

		public BigDecimal getMontantTotalHtFactureGeneral() {
			return montantTotalHtFactureGeneral;
		}

		public void setMontantTotalHtFactureGeneral(BigDecimal montantTotalHtFactureGeneral) {
			this.montantTotalHtFactureGeneral = montantTotalHtFactureGeneral;
		}

		public BigDecimal getMontantTotalHtFactureAvoirGeneral() {
			return montantTotalHtFactureAvoirGeneral;
		}

		public void setMontantTotalHtFactureAvoirGeneral(BigDecimal montantTotalHtFactureAvoirGeneral) {
			this.montantTotalHtFactureAvoirGeneral = montantTotalHtFactureAvoirGeneral;
		}
		public List<DocumentDTO> getValueLignesDevisNonTransforme() {
			return valueLignesDevisNonTransforme;
		}

		public void setValueLignesDevisNonTransforme(List<DocumentDTO> valueLignesDevisNonTransforme) {
			this.valueLignesDevisNonTransforme = valueLignesDevisNonTransforme;
		}

		public List<DocumentDTO> getValueLignesDevisARelancer() {
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

		public List<DocumentDTO> getValueLignesComNonTransforme() {
			return valueLignesComNonTransforme;
		}

		public void setValueLignesComNonTransforme(List<DocumentDTO> valueLignesComNonTransforme) {
			this.valueLignesComNonTransforme = valueLignesComNonTransforme;
		}

		public List<DocumentDTO> getValueLignesComARelancer() {
			return valueLignesComARelancer;
		}

		public void setValueLignesComARelancer(List<DocumentDTO> valueLignesComARelancer) {
			this.valueLignesComARelancer = valueLignesComARelancer;
		}

		public List<DocumentDTO> getValueLignesComTransforme() {
			return valueLignesComTransforme;
		}

		public void setValueLignesComTransforme(List<DocumentDTO> valueLignesComTransforme) {
			this.valueLignesComTransforme = valueLignesComTransforme;
		}
		
		public boolean isFactureAvoirRealise() {
			return factureAvoirRealise;
		}

		public void setFactureAvoirRealise(boolean factureAvoirRealise) {
			this.factureAvoirRealise = factureAvoirRealise;
		}

		public boolean isTiersAyantAcheter() {
			return tiersAyantAcheter;
		}

		public void setTiersAyantAcheter(boolean tiersAyantAcheter) {
			this.tiersAyantAcheter = tiersAyantAcheter;
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

	    // Synthèse et détail facturé
	    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneArticle(){
	    	List<DocumentChiffreAffaireDTO> listSynthese = taFactureService.listeSumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleCourant, null);
	    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
//	    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  synthese.getCodeArticle(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
	    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticleDTOTiersUnite(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeArticle(), synthese.getU1LDocument(), null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
	    		for (DocumentChiffreAffaireDTO detail : listDetail) {
	    			synthese.getListeDetail().add(detail);
				}
	    		
			}
	    		
	    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
	    	return syntheseEtDetail;
	    }
	    // Synthèse et détail avoirs
	    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneAvoirs(){
	    	List<DocumentChiffreAffaireDTO> listSynthese = taAvoirService.listeSumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleCourant, null);
	    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
//	    		List<DocumentChiffreAffaireDTO> listDetail = taAvoirService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  synthese.getCodeArticle(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
	    		List<DocumentChiffreAffaireDTO> listDetail = taAvoirService.listLigneArticleDTOTiersUnite( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  synthese.getCodeArticle(), synthese.getU1LDocument(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
	    		
	    		for (DocumentChiffreAffaireDTO detail : listDetail) {
	    			synthese.getListeDetail().add(detail);
				}
	    		
			}
	    		
	    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
	    	return syntheseEtDetail;
	    }
	    // Synthèse et détail TOUS
	    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneArticleTous(){
	    	List<DocumentChiffreAffaireDTO> listSynthese = taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleCourant, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE);
	    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
//	    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeArticle(), false, DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
	   		    List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticlePeriodeParArticleParUniteAvoirFactureDTO( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeArticle(),synthese.getU1LDocument(), DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
	    		
	    		for (DocumentChiffreAffaireDTO detail : listDetail) {
	    			synthese.getListeDetail().add(detail);
				}
	    		
			}
	    		
	    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
	    	return syntheseEtDetail;
	    }


	    
	  //FACTURE
	  //private String titreListeSyntheseParArticleFacture = "Synthèse des ventes par articles et par unités";
	  //private String titreListeSyntheseParArticleMoisFacture = "Synthèse des ventes par article, par unité et par mois ";
	  //private String titreListeDetailParArticleFacture ="Détail des ventes "; 
	  //private String titreEditionListeDetailParArticleFacture ="Détail des ventes par articles et par unités";
	  public void actImprimerListeArticleFactureGroupeArticle(ActionEvent actionEvent) {
	  				
	  		try {
	  					
	  		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	  		Map<String, Object> sessionMap = externalContext.getSessionMap();
	  	
	  		Map<String,Object> mapParametre = new HashMap<String,Object>();
	  		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	  		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	  		mapParametre.put("infosLabel", titreListeSyntheseParArticleFacture);
//	  		mapParametre.put("infosLabel", "Synthèse des ventes par articles et par unités");
	  		
	  		sessionMap.put("parametre", mapParametre);
	  		sessionMap.put("listeArticle", syntheseEtDetailValueLigneArticle());
	  		
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  		}
	  	}
	  
	  public void actImprimerRapportFactureDetailDesVenteParArticle(ActionEvent actionEvent) {
			
			try {
						
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapParametre = new HashMap<String,Object>();
			mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
			mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
			mapParametre.put("infosLabel", titreEditionListeDetailParArticleFacture);
//			mapParametre.put("infosLabel", "Détail des ventes par articles et par unités");
			
			sessionMap.put("parametre", mapParametre);
			sessionMap.put("listeArticle", syntheseEtDetailValueLigneArticle());
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	public String getTitreListeSyntheseParArticleFacture() {
		return titreListeSyntheseParArticleFacture;
	}

	public void setTitreListeSyntheseParArticleFacture(String titreListeSyntheseParArticleFacture) {
		this.titreListeSyntheseParArticleFacture = titreListeSyntheseParArticleFacture;
	}

	public String getTitreEditionListeDetailParArticleFacture() {
		return titreEditionListeDetailParArticleFacture;
	}

	public void setTitreEditionListeDetailParArticleFacture(String titreEditionListeDetailParArticleFacture) {
		this.titreEditionListeDetailParArticleFacture = titreEditionListeDetailParArticleFacture;
	}


}
