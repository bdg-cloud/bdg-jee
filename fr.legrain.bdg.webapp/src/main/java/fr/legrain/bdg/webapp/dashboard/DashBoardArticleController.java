package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.BarChartModel;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardArticleController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5979028746642381345L;
	/**
	 * 
	 */
	

	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelArticle;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    private Date dateDebut;
//    private Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatArticle;
    ///////articles
    //tous
    private String titreListeSyntheseParArticleTous = "Synthèse des ventes (et avoirs) par articles et par unités";
    private String titreListeDetailParArticleTous ="Détail des ventes (et avoirs)"; 
    private String titreEditionListeDetailParArticleTous ="Détail des ventes (et avoirs) par articles et par unités"; 
    private List<DocumentChiffreAffaireDTO> valueLignesArticleTous;
    private List<DocumentChiffreAffaireDTO> valueLignesArticleTousDetail;
    
    //facturé
    private String titreListeSyntheseParArticleFacture = "Synthèse des ventes par articles et par unités";
    private String titreListeSyntheseParArticleMoisFacture = "Synthèse des ventes par article, par unité et par mois ";
    private String titreListeDetailParArticleFacture ="Détail des ventes "; 
    private String titreEditionListeDetailParArticleFacture ="Détail des ventes par articles et par unités";
    private String titreEditionListeDetailParArticleFactureFournisseur ="Détail des ventes par articles, par unités et Fournisseur";
    //private String titreEditionListeDetailParArticleMoisFacture ="Détail des ventes par article, par unité et par mois";
    private List<DocumentChiffreAffaireDTO> valueLignesArticle;
    private List<DocumentChiffreAffaireDTO> valueLignesArticleDetail;
    private List<DocumentChiffreAffaireDTO> valueLignesArticleMois;
    //avoirs
    private String titreListeSyntheseParArticleAvoir = "Synthèse des avoirs  par articles et par unités";
    private String titreListeSyntheseParArticleMoisAvoir  = "Synthèse des avoirs par article, par unité et par mois ";
    private String titreListeDetailParArticleAvoir  ="Détail des avoirs  ";
    private String titreEditionListeDetailParArticleAvoir  ="Détail des avoirs par articles et par unités ";
    private List<DocumentChiffreAffaireDTO> valueLignesArticleAvoirs;
    private List<DocumentChiffreAffaireDTO> valueLignesArticleAvoirsDetail;
    private List<DocumentChiffreAffaireDTO> valueLignesArticleAvoirsMois;
    
    private DocumentChiffreAffaireDTO nombreArticlesDoc;
    private DocumentChiffreAffaireDTO nombreFamillesDoc;
    
    private DocumentChiffreAffaireDTO caTotalArticlesDoc;
    private DocumentChiffreAffaireDTO caTotalArticlesDocMoinsAvoir;
    private DocumentChiffreAffaireDTO caTotalArticlesDocAvoir;
    //////familles d'article
    ///Synthese
    //tous
    private String titreListeSyntheseParFamilleTous = "Synthèse des ventes (et avoirs) par famille(par défaut) et par unité";
    private String titreEditionListeDetailParFamilleTous = "Détail des ventes (et avoirs) par famille(par défaut) et par unité";
    private List<DocumentChiffreAffaireDTO> valueLignesFamilleArticleTous;
    //facturé
    private String titreListeSyntheseParFamilleFacture = "Synthèse des ventes par famille(par défaut) et par unité";
    private String titreEditionListeDetailParFamilleFacture = "Détail des ventes par famille(par défaut) et par unité";
    private List<TaArticlesParTiersDTO> valueLignesFamilleArticle;
    //avoirs
    private String titreListeSyntheseParFamilleAvoir = "Synthèse des avoirs par famille(par défaut) et par unité";
    private String titreEditionListeDetailParFamilleAvoir = "Détail des avoirs par famille(par défaut) et par unité";
    private List<TaArticlesParTiersDTO> valueLignesFamilleArticleAvoir;
    //Details
    //tous
    private List<DocumentChiffreAffaireDTO> valueDetailfamille;
    
    
    private String typeFacture = TaFacture.TYPE_DOC;
    private String typeAvoir = TaAvoir.TYPE_DOC;
    
    
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticle;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransformeARelancer;
     
    private int nbArticleActif = 0;
    private int nbArticleNonTransformeSurPeriode = 0 ;
    private int nbArticleTransformeSurPeriode = 0;
    private int nbArticleNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal MontantTotalHt = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    
    private String titreTabListe1 = "Groupé par";
    private String titreTabListe2 = "Groupé par";
    
    private Boolean afficheTabGroupeFamille = false;
    private Boolean afficheTabGroupeArticle = false;
    private Boolean afficheTabGroupeArticleMois = false;
    
    private String codeArticleDTO;
    private TaArticleDTO taArticleDTO;
    
    
//    private String cSbNbArticleSurPeriode;
//    private String cSbNbArticleTransformeSurPeriode;
//    private String cSbNbArticleNonTransformeSurPeriode;
//    private String cSbNbArticleNonTransformeARelancerSurPeriode;
    
    
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
    	infoEntreprise = taInfoEntrepriseService.findInstance();
//    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
//    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
    	codeEtatArticle = "tous";
    	codeEtatDocument = ETAT_DOCUMENT_TOUS;
    	//barChartModelArticle = new BarChartModel();
    	//Initialisation des données sur la période exercice
    	actRechercheArticle(codeEtatArticle);
//    	createBarModel();
    }


    public void refresh(String etatArticle){
    	actRechercheArticle(etatArticle);
    }

    public void refreshDashBoard(String etatDocument){
    	actRechercheArticle(etatDocument);
    }  
    
    public String actRechercheMoisString(String mois, String annee) {
		Calendar mCalendar = Calendar.getInstance();
		String moisString;
		mCalendar.setTime(LibDate.stringToDate2("01/"+mois+"/"+annee+""));
        moisString = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        return moisString;
    }
    ///////Listes dans les rowExpansion/////////////
    //Tous
//    public List<DocumentChiffreAffaireDTO> detailParArticle(String codeArticle){
//    	List<DocumentChiffreAffaireDTO> detailParArticle =  taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticle, false, DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE);
//		return detailParArticle;
//    }
    //facture
//    public List<DocumentChiffreAffaireDTO> detailParArticleFacture(String codeArticle){
//    	List<DocumentChiffreAffaireDTO> detailParArticleFacture =  taFactureService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  codeArticle,  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//		return detailParArticleFacture;
//    }
    //Avoirs
//    public List<DocumentChiffreAffaireDTO> detailParArticleAvoir(String codeArticle){
//    	List<DocumentChiffreAffaireDTO> detailParArticleAvoir =  taAvoirService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  codeArticle,  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//		return detailParArticleAvoir;
//    }
    
    
    public List<DocumentChiffreAffaireDTO> DetailValueLigneArticleFournisseur(){
    	List<DocumentChiffreAffaireDTO> listSynthese = taFactureService.listLigneArticlePeriodeAvoirFactureFournisseurDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, false, DocumentChiffreAffaireDTO.ORDER_BY_CODE_DOCUMENT);
    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
//   		    List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticlePeriodeParArticleParUniteAvoirFactureDTO( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeArticle(),synthese.getU1LDocument(), DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//    		
//    		for (DocumentChiffreAffaireDTO detail : listDetail) {
//    			synthese.getListeDetail().add(detail);
//			}
    		
		}
    		
    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
    	return syntheseEtDetail;
    }
    
    // Synthèse et détail facturé
    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneArticle(){
    	List<DocumentChiffreAffaireDTO> listSynthese = taFactureService.listeSumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null);
    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
//    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  synthese.getCodeArticle(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
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
    	List<DocumentChiffreAffaireDTO> listSynthese = taAvoirService.listeSumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null);
    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
//    		List<DocumentChiffreAffaireDTO> listDetail = taAvoirService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  synthese.getCodeArticle(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
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
    	List<DocumentChiffreAffaireDTO> listSynthese = taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE);
    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
//    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeArticle(), false, DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
   		    List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticlePeriodeParArticleParUniteAvoirFactureDTO( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeArticle(),synthese.getU1LDocument(), DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
    		
    		for (DocumentChiffreAffaireDTO detail : listDetail) {
    			synthese.getListeDetail().add(detail);
			}
    		
		}
    		
    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
    	return syntheseEtDetail;
    }
    
    ///Synthèse et détail Familles tous
    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneFamilleTous(){
    	List<DocumentChiffreAffaireDTO> listSynthese = taFactureService.listLigneArticlePeriodeParFamilleArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO , null);
    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.detailParFamilleUniteArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeFamille(),synthese.getU1LDocument(),  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
			
    		for (DocumentChiffreAffaireDTO detail : listDetail) {
    			synthese.getListeDetail().add(detail);
			}
    		
		}
    		
    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
    	return syntheseEtDetail;
    }
    //Synthèse et détail Famille Facture 
    public List<TaArticlesParTiersDTO> syntheseEtDetailValueLigneFamilleFacture(){
    	List<TaArticlesParTiersDTO> listSynthese = taFactureService.findArticlesParTiersParMoisParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, Const.PAR_FAMILLE_ARTICLE, "%", true);
    	for (TaArticlesParTiersDTO synthese : listSynthese ) {
    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticleDTOTiersUniteFamille(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), "%",synthese.getU1LDocument(), synthese.getCodeFamille(),"%",  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
			
    		for (DocumentChiffreAffaireDTO detail : listDetail) {
    			synthese.getListeDetail().add(detail);
			}
    		
		}
    		
    	List<TaArticlesParTiersDTO> syntheseEtDetail = listSynthese;
    	return syntheseEtDetail;
    }
    
    //Synthèse et détail Famille Avoir 
    public List<TaArticlesParTiersDTO> syntheseEtDetailValueLigneFamilleAvoir(){
    	List<TaArticlesParTiersDTO> listSynthese = taAvoirService.findArticlesParTiersParMoisParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, Const.PAR_FAMILLE_ARTICLE, "%", true);
    	for (TaArticlesParTiersDTO synthese : listSynthese ) {
    		List<DocumentChiffreAffaireDTO> listDetail = taAvoirService.listLigneArticleDTOTiersUniteFamille(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), "%",synthese.getU1LDocument(), synthese.getCodeFamille(),"%",  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
			
    		for (DocumentChiffreAffaireDTO detail : listDetail) {
    			synthese.getListeDetail().add(detail);
			}
    		
		}
    		
    	List<TaArticlesParTiersDTO> syntheseEtDetail = listSynthese;
    	return syntheseEtDetail;
    }
    /////////Listes dans les rowExpansion//////////
    
    
    public void actRechercheArticle(String codeEtatArticle) {
    		nbArticleActif = taArticleService.countAllArticleActif();
////    	if (action != null) {
////    	codeEtatArticle = (String) action.getComponent().getAttributes().get("codeetatArticle");
////    	}
    	switch (codeEtatArticle) {
		case "tous":
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesArticle = taArticleService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
			
			
			valueDetailfamille = taFactureService.detailParFamilleArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
			
			
//    		valueLignesArticle = taFactureService.findArticlesParTiersTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    		// synthèse facturé
    		valueLignesArticle = taFactureService.listeSumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null);
    		//le détail facturé : 
    		valueLignesArticleDetail = taFactureService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  codeArticleDTO,  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
    		valueLignesArticleMois = taFactureService.listeSumChiffreAffaireTotalDTOArticleMois(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null);
    		// le détail Tous : 
    		valueLignesArticleTousDetail = taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, false, DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE);
    		// la synthèse Tous (somme) :
    		valueLignesArticleTous = taFactureService.listLigneArticlePeriodeParArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE);
    		//synthèse avoirs
    		valueLignesArticleAvoirs = taAvoirService.listeSumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null);
    		valueLignesArticleAvoirsMois = taAvoirService.listeSumChiffreAffaireTotalDTOArticleMois(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null);
    		// détail avoirs
    		valueLignesArticleAvoirsDetail = taAvoirService.listLigneArticleDTOTiers( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),  codeArticleDTO,  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
    		
    		valueLignesFamilleArticle = taFactureService.findArticlesParTiersParMoisParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, Const.PAR_FAMILLE_ARTICLE, "%", true);
    		valueLignesFamilleArticleAvoir = taAvoirService.findArticlesParTiersParMoisParTypeRegroupement(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), null, Const.PAR_FAMILLE_ARTICLE, "%", true);
    		valueLignesFamilleArticleTous = taFactureService.listLigneArticlePeriodeParFamilleArticleAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO , null);
    		nombreArticlesDoc = taFactureService.countChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null).get(0);
    		nombreFamillesDoc = taFactureService.countFamilleArticleDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO, null).get(0);
    		caTotalArticlesDoc = taFactureService.sumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),codeArticleDTO).get(0);
    		caTotalArticlesDocMoinsAvoir = taFactureService.sumChiffreAffaireTotalDTOArticleMoinsAvoir(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO).get(0);
    		caTotalArticlesDocAvoir = taAvoirService.sumChiffreAffaireTotalDTOArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),codeArticleDTO).get(0);
    		
    	    afficheTabGroupeArticle = true;
    	    afficheTabGroupeArticleMois = true;
    		afficheTabGroupeFamille = false;
    		
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelArticle = createBarModel(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), barChartModelArticle, TaArticle.TYPE_DOC, 1 );
//			// Calcule les CA (HT, TVA, TTC) total sur la période
////			listeCaPeriodeArticle = taArticleService.findChiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			listeCaPeriodeArticle = findChiffreAffaireTotalDocument(TaArticle.TYPE_DOC, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Calcule le CA (HT, TVA, TTC) total des Article non transformé sur la période
////			listeCaPeriodeArticleNonTransforme = taArticleService.findChiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			listeCaPeriodeArticleNonTransforme = findChiffreAffaireNonTransformeTotalDTODocument(TaArticle.TYPE_DOC, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Calcule le CA (HT, TVA, TTC) total des Article non transformé sur la période
//			// La requête retourne un objet vide alors qu'il n'y a rien dans la table, à vérifier car cela génère une erreur lorsque
//			//l'on veut consulter les données ci-après.
//			listeCaPeriodeArticleNonTransformeARelancer = taArticleService.caArticleNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			// Retourne le nombre total de documents sur la période
//			nbArticleSurPeriode = (int) taArticleService.countArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Retourne le nombre de Article non transformés sur la période
//			nbArticleNonTransformeSurPeriode = (int) taArticleService.countArticleNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Retourne le nombre de Article à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
//			nbArticleNonTransformeARelancerSurPeriode = (int) taArticleService.countArticleNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			
//			
//			
//			
//			if (nbArticleSurPeriode > 0) {
//				try {
//					MontantTotalHt = listeCaPeriodeArticle.get(0).getMtHtCalc();
//					nbArticleTransformeSurPeriode = nbArticleSurPeriode - nbArticleNonTransformeSurPeriode;
//					tauxTransfoNb =  (new BigDecimal(nbArticleTransformeSurPeriode) .divide(new BigDecimal(nbArticleSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//					if ((listeCaPeriodeArticleNonTransforme != null && ! listeCaPeriodeArticleNonTransforme.isEmpty())) {
//						MontantTotalHtNonTransfo = listeCaPeriodeArticleNonTransforme.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//					if ((listeCaPeriodeArticle != null && ! listeCaPeriodeArticle.isEmpty()) || (listeCaPeriodeArticleNonTransforme != null && ! listeCaPeriodeArticleNonTransforme.isEmpty())) {
//						MontantTotalHtTransfo = listeCaPeriodeArticle.get(0).getMtHtCalc() .subtract(listeCaPeriodeArticleNonTransforme.get(0).getMtHtCalc());
//						if (MontantTotalHtTransfo.intValue() > 0){
//						tauxTransfoCa =  (MontantTotalHtTransfo .divide(listeCaPeriodeArticle.get(0).getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)).setScale(0,BigDecimal.ROUND_HALF_UP);
//						}
//					}
//					if (listeCaPeriodeArticleNonTransformeARelancer != null && ! listeCaPeriodeArticleNonTransformeARelancer.isEmpty()) {
//						MontantTotalHtNonTransfoARelancer = listeCaPeriodeArticleNonTransformeARelancer.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					// TODO: handle exception
//				}
//				
//				
//			}
    		
    		initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_ARTICLE,
    				  CSS_DBOARD_BLOC_INACTIF_ALL, 					  
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
    		break;
    		
    		
    		
//			// Initialisent les classes css pour les block d'information
//			cSbNbArticleSurPeriode = "greenback";
//			cSbNbArticleTransformeSurPeriode = "whiteback";
//			cSbNbArticleNonTransformeSurPeriode = "whiteback";
//			cSbNbArticleNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//
		case "famille":
//			valueLignesArticle = taArticleService.findArticleNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
			//listeCaPeriodeArticle = taArticleService.findCAArticleSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleSurPeriode = (int) taArticleService.countArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeSurPeriode = (int) taArticleService.countArticleNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeARelancerSurPeriode = (int) taArticleService.countArticleNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
			afficheTabGroupeFamille = true;
			afficheTabGroupeArticle = false;
    	    afficheTabGroupeArticleMois = false;
			initClasseCssBloc(CSS_DBOARD_BLOC_INACTIF_ALL,
						CSS_DBOARD_BLOC_ACTIF_ARTICLE,
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
			break;
			
			}//fin du switch


//    public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal() {
//		
////    	List<Object> list = taArticleService.findCAArticleSurPeriode(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise());
////    	List<DocumentChiffreAffaireDTO> list = taArticleService.findChiffreAffaireTotal(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//    	List<DocumentChiffreAffaireDTO> list = taArticleService.findChiffreAffaireTotalDTO(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//		return list;
//    	
    }

//////////////////////////////////ARTICLES 1ER ONGLET////////////////////////////
///TOUS    
public void actImprimerListeArticleTousGroupeArticle(ActionEvent actionEvent) {
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", titreListeSyntheseParArticleTous);
		mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
//		mapParametre.put("infosLabel", "Synthèse des ventes (et avoirs) par articles et par unités");
		
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeArticle", valueLignesArticleTous);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public void actImprimerListeArticleTousDetailGroupeArticle(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeDetailParArticleTous);
//	mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
//	mapParametre.put("infosLabel", "Détail des ventes (et avoirs) par article");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", valueLignesArticleTousDetail);
	
	} catch (Exception e) {
		e.printStackTrace();
	}
} 

public void actImprimerRapportDetailDesVenteEtAvoirParArticle(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreEditionListeDetailParArticleTous);
	mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
//	mapParametre.put("infosLabel", "Détail des ventes (et avoirs) par articles et par unités");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneArticleTous());
	
	} catch (Exception e) {
		e.printStackTrace();
	}
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
//		mapParametre.put("infosLabel", "Synthèse des ventes par articles et par unités");
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeArticle", valueLignesArticle);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public void actImprimerListeArticleTousFactureDetail(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeDetailParArticleFacture);
//	mapParametre.put("infosLabel", "Détail des ventes par articles");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", valueLignesArticleDetail);
	
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
//	mapParametre.put("infosLabel", "Détail des ventes par articles et par unités");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneArticle());
	
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void actImprimerRapportFactureDetailDesVenteParArticleFournisseur(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreEditionListeDetailParArticleFactureFournisseur);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", DetailValueLigneArticleFournisseur());
	
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void actImprimerListeArticleFactureGroupeMois(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeSyntheseParArticleMoisFacture);
//	mapParametre.put("infosLabel", "Synthèse des ventes par article et par mois");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticleMois", valueLignesArticleMois);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
//AVOIRS
//private String titreListeSyntheseParArticleAvoir = "Synthèse des avoirs  par articles et par unités";
//private String titreListeSyntheseParArticleMoisAvoir  = "Synthèse des avoirs par article, par unité et par mois ";
//private String titreListeDetailParArticleAvoir  ="Détail des avoirs  ";
//private String titreEditionListeDetailParArticleAvoir  ="Détail des avoirs par articles et par unités ";
public void actImprimerListeArticleAvoirsGroupeArticle(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeSyntheseParArticleAvoir);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", valueLignesArticleAvoirs);

	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void actImprimerListeArticleAvoirsDetailArticle(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeDetailParArticleAvoir);
//	mapParametre.put("infosLabel", "Détail des avoirs par article");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", valueLignesArticleAvoirsDetail);

	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void actImprimerRapportFactureDetailDesVenteEtAvoirsParArticleEtFamille(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreEditionListeDetailParArticleAvoir);
//	mapParametre.put("infosLabel", "Détail des avoirs par articles et par unités");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneAvoirs());

	} catch (Exception e) {
		e.printStackTrace();
	}
}

public void actImprimerListeArticleAvoirsGroupeMois(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeSyntheseParArticleMoisAvoir);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticleMois", valueLignesArticleAvoirsMois);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
////////////////////////////////// ARTICLES 2EME ONGLET FAMILLE////////////////////////////
//private String titreListeSyntheseParFamilleTous = "Synthèse des ventes (et avoirs) par famille(par défaut) et par unité";
//private String titreEditionListeDetailParFamilleTous = "Détail des ventes (et avoirs) par famille(par défaut) et par unité";
////facturé
//private String titreListeSyntheseParFamilleFacture = "Synthèse des ventes par famille(par défaut) et par unité";
//private String titreEditionListeDetailParFamilleFacture = "Détail des ventes par famille(par défaut) et par unité";
////avoirs
//private String titreListeSyntheseParFamilleAvoir = "Synthèse des avoirs par famille(par défaut) et par unité";
//private String titreEditionListeDetailParFamilleAvoir = "Détail des avoirs par famille(par défaut) et par unité";

///TOUS
public void actImprimerListeTousFamilleArticleAvoir(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeSyntheseParFamilleTous);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeFamilleArticle", valueLignesFamilleArticleTous);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
///TOUS
public void actImprimerTousSyntheseEtDetailFamilles(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreEditionListeDetailParFamilleTous);
//	mapParametre.put("infosLabel", "Détail des ventes (et avoirs) par famille (par défaut) et par unité");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneFamilleTous());

	} catch (Exception e) {
		e.printStackTrace();
	}
}
///FACTURE
public void actImprimerListeFactureFamilleArticleGroupe(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeSyntheseParFamilleFacture);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeFamilleArticle", valueLignesFamilleArticle);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
///FACTURE
public void actImprimerFactureSyntheseEtDetailFamilles(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreEditionListeDetailParFamilleFacture);
//	mapParametre.put("infosLabel", "Détail des ventes par famille (par défaut) et par unité");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneFamilleFacture());
	} catch (Exception e) {
		e.printStackTrace();
	}
}
///AVOIR
public void actImprimerListeAvoirFamilleArticleAvoir(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreListeSyntheseParFamilleAvoir);
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeFamilleArticle", valueLignesFamilleArticleAvoir);

	} catch (Exception e) {
		e.printStackTrace();
	}
}
///AVOIR
public void actImprimerAvoirSyntheseEtDetailFamilles(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));	
	mapParametre.put("infosLabel", titreEditionListeDetailParFamilleAvoir);
//	mapParametre.put("infosLabel", "Détail des avoirs par famille (par défaut) et par unité");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneFamilleAvoir());
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
	public BarChartModel getBarChartModelArticle() {
		return barChartModelArticle;
	}
	public void setBarChartModelArticleExo(BarChartModel barChartModelArticle) {
		this.barChartModelArticle = barChartModelArticle;
	}
	
//	private BarChartModel initBarModelArticleExo() {
//        BarChartModel model = new BarChartModel();
//        model.setExtender("formatbarre");
//        ChartSeries moisGraphique = new ChartSeries();
//        moisGraphique.setLabel("Mois");
//        
//        List<InfosMois> listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise());
//        
//        for (InfosMois ligneInfosMois : listMoisAnneeExo) {
//        	Object objMoisAnneeCa = moisOfIndex(ligneInfosMois.getCodeMoisAnnee()); 
//        	if (objMoisAnneeCa!= null ){
//        		moisGraphique.set(ligneInfosMois.getNomCourt(),(BigDecimal)((Object[])objMoisAnneeCa)[3]);
//        	} else {
//        		moisGraphique.set(ligneInfosMois.getNomCourt(),0);
//        	}
//
//		}
//        
// 
//        model.addSeries(moisGraphique);
//         
//        return model;
//    }
	
//	private Object moisOfIndex(String moisannee){
//		
//		for (Object objectMoisAnneeCa : listeCaMois) {
//			String moisAnneeCa = (String)((Object[])objectMoisAnneeCa)[1] + (String)((Object[])objectMoisAnneeCa)[2]; 
//			if (moisAnneeCa.equals(moisannee)){
//				return objectMoisAnneeCa;
//			}
//		}
//		return null;
//	}
	
	
//	private void createBarModel() {
//		barChartModelArticle = initBarModelArticleExo();
//         
//		barChartModelArticle.setTitle("Chiffre d'affaire HT");
//		barChartModelArticle.setLegendPosition("ne");
//		barChartModelArticle.setAnimate(true);
//         
//        Axis xAxis = barChartModelArticle.getAxis(AxisType.X);
////        xAxis.setLabel("Gender");
//         
//        Axis yAxis = barChartModelArticle.getAxis(AxisType.Y);
////        yAxis.setLabel("Births");
//        yAxis.setMin(0);
////        yAxis.setMax(1000);
//    }
//	public List<MoisExoCourant> getListe() {
//		return listeMoisExoCourant;
//	}

//	public void setListe(List<MoisExoCourant> listeMoisExoCourant) {
//		this.listeMoisExoCourant = listeMoisExoCourant;
//	}

//	public Date getDateDebut() {
//		return dateDebut;
//	}
//
//	public void setDateDebut(Date dateDebut) {
//		this.dateDebut = dateDebut;
//	}



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

	public List<DocumentChiffreAffaireDTO> getValueLignesArticle() {
		return valueLignesArticle;
	}

	public void setValueLignesArticle(List<DocumentChiffreAffaireDTO> valueLignesArticle) {
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


//	public String getcSbNbArticleSurPeriode() {
//		return cSbNbArticleSurPeriode;
//	}
//
//
//	public void setcSbNbArticleSurPeriode(String cSbNbArticleSurPeriode) {
//		this.cSbNbArticleSurPeriode = cSbNbArticleSurPeriode;
//	}
//
//
//	public String getcSbNbArticleNonTransformeSurPeriode() {
//		return cSbNbArticleNonTransformeSurPeriode;
//	}
//
//
//	public void setcSbNbArticleNonTransformeSurPeriode(String cSbNbArticleNonTransformeSurPeriode) {
//		this.cSbNbArticleNonTransformeSurPeriode = cSbNbArticleNonTransformeSurPeriode;
//	}
//
//
//	public String getcSbNbArticleNonTransformeARelancerSurPeriode() {
//		return cSbNbArticleNonTransformeARelancerSurPeriode;
//	}
//
//
//	public void setcSbNbArticleNonTransformeARelancerSurPeriode(String cSbNbArticleNonTransformeARelancerSurPeriode) {
//		this.cSbNbArticleNonTransformeARelancerSurPeriode = cSbNbArticleNonTransformeARelancerSurPeriode;
//	}


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


	public BigDecimal getMontantTotalHtNonTransfoARelancer() {
		return MontantTotalHtNonTransfoARelancer;
	}


	public void setMontantTotalHtNonTransfoARelancer(BigDecimal montantTotalHtNonTransfoARelancer) {
		MontantTotalHtNonTransfoARelancer = montantTotalHtNonTransfoARelancer;
	}


	public BigDecimal getMontantTotalHtTransfo() {
		return MontantTotalHtTransfo;
	}


	public void setMontantTotalHtTransfo(BigDecimal montantTotalHtTransfo) {
		MontantTotalHtTransfo = montantTotalHtTransfo;
	}


//	public String getcSbNbArticleTransformeSurPeriode() {
//		return cSbNbArticleTransformeSurPeriode;
//	}
//
//
//	public void setcSbNbArticleTransformeSurPeriode(String cSbNbArticleTransformeSurPeriode) {
//		this.cSbNbArticleTransformeSurPeriode = cSbNbArticleTransformeSurPeriode;
//	}


	public BigDecimal getMontantTotalHt() {
		return MontantTotalHt;
	}


	public void setMontantTotalHt(BigDecimal montantTotalHt) {
		MontantTotalHt = montantTotalHt;
	}


	public BigDecimal getMontantTotalHtNonTransfo() {
		return MontantTotalHtNonTransfo;
	}


	public void setMontantTotalHtNonTransfo(BigDecimal montantTotalHtNonTransfo) {
		MontantTotalHtNonTransfo = montantTotalHtNonTransfo;
	}


	public List<DocumentChiffreAffaireDTO> getValueLignesArticleMois() {
		return valueLignesArticleMois;
	}


	public void setValueLignesArticleMois(List<DocumentChiffreAffaireDTO> valueLignesArticleMois) {
		this.valueLignesArticleMois = valueLignesArticleMois;
	}


	public DocumentChiffreAffaireDTO getNombreArticlesDoc() {
		return nombreArticlesDoc;
	}


	public void setNombreArticlesDoc(DocumentChiffreAffaireDTO nombreArticlesDoc) {
		this.nombreArticlesDoc = nombreArticlesDoc;
	}


	public DocumentChiffreAffaireDTO getCaTotalArticlesDoc() {
		return caTotalArticlesDoc;
	}


	public void setCaTotalArticlesDoc(DocumentChiffreAffaireDTO caTotalArticlesDoc) {
		this.caTotalArticlesDoc = caTotalArticlesDoc;
	}


	public String getTitreTabListe1() {
		return titreTabListe1;
	}


	public void setTitreTabListe1(String titreTabListe1) {
		this.titreTabListe1 = titreTabListe1;
	}


	public String getTitreTabListe2() {
		return titreTabListe2;
	}


	public void setTitreTabListe2(String titreTabListe2) {
		this.titreTabListe2 = titreTabListe2;
	}


	public List<TaArticlesParTiersDTO> getValueLignesFamilleArticle() {
		return valueLignesFamilleArticle;
	}


	public void setValueLignesFamilleArticle(List<TaArticlesParTiersDTO> valueLignesFamilleArticle) {
		this.valueLignesFamilleArticle = valueLignesFamilleArticle;
	}


	public Boolean getAfficheTabGroupeFamille() {
		return afficheTabGroupeFamille;
	}


	public void setAfficheTabGroupeFamille(Boolean afficheTabGroupeFamille) {
		this.afficheTabGroupeFamille = afficheTabGroupeFamille;
	}


	public Boolean getAfficheTabGroupeArticle() {
		return afficheTabGroupeArticle;
	}


	public void setAfficheTabGroupeArticle(Boolean afficheTabGroupeArticle) {
		this.afficheTabGroupeArticle = afficheTabGroupeArticle;
	}


	public Boolean getAfficheTabGroupeArticleMois() {
		return afficheTabGroupeArticleMois;
	}


	public void setAfficheTabGroupeArticleMois(Boolean afficheTabGroupeArticleMois) {
		this.afficheTabGroupeArticleMois = afficheTabGroupeArticleMois;
	}


	public DocumentChiffreAffaireDTO getCaTotalArticlesDocMoinsAvoir() {
		return caTotalArticlesDocMoinsAvoir;
	}


	public void setCaTotalArticlesDocMoinsAvoir(DocumentChiffreAffaireDTO caTotalArticlesDocMoinsAvoir) {
		this.caTotalArticlesDocMoinsAvoir = caTotalArticlesDocMoinsAvoir;
	}


	public DocumentChiffreAffaireDTO getCaTotalArticlesDocAvoir() {
		return caTotalArticlesDocAvoir;
	}


	public void setCaTotalArticlesDocAvoir(DocumentChiffreAffaireDTO caTotalArticlesDocAvoir) {
		this.caTotalArticlesDocAvoir = caTotalArticlesDocAvoir;
	}


	public List<DocumentChiffreAffaireDTO> getValueLignesArticleAvoirs() {
		return valueLignesArticleAvoirs;
	}


	public void setValueLignesArticleAvoirs(List<DocumentChiffreAffaireDTO> valueLignesArticleAvoirs) {
		this.valueLignesArticleAvoirs = valueLignesArticleAvoirs;
	}


	public List<DocumentChiffreAffaireDTO> getValueLignesArticleAvoirsMois() {
		return valueLignesArticleAvoirsMois;
	}


	public void setValueLignesArticleAvoirsMois(List<DocumentChiffreAffaireDTO> valueLignesArticleAvoirsMois) {
		this.valueLignesArticleAvoirsMois = valueLignesArticleAvoirsMois;
	}


	public List<TaArticlesParTiersDTO> getValueLignesFamilleArticleAvoir() {
		return valueLignesFamilleArticleAvoir;
	}


	public void setValueLignesFamilleArticleAvoir(List<TaArticlesParTiersDTO> valueLignesFamilleArticleAvoir) {
		this.valueLignesFamilleArticleAvoir = valueLignesFamilleArticleAvoir;
	}


	public List<DocumentChiffreAffaireDTO> getValueLignesArticleTous() {
		return valueLignesArticleTous;
	}


	public void setValueLignesArticleTous(List<DocumentChiffreAffaireDTO> valueLignesArticleTous) {
		this.valueLignesArticleTous = valueLignesArticleTous;
	}


	public List<DocumentChiffreAffaireDTO> getValueLignesArticleTousDetail() {
		return valueLignesArticleTousDetail;
	}


	public void setValueLignesArticleTousDetail(List<DocumentChiffreAffaireDTO> valueLignesArticleTousDetail) {
		this.valueLignesArticleTousDetail = valueLignesArticleTousDetail;
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


	public List<DocumentChiffreAffaireDTO> getValueLignesFamilleArticleTous() {
		return valueLignesFamilleArticleTous;
	}


	public void setValueLignesFamilleArticleTous(List<DocumentChiffreAffaireDTO> valueLignesFamilleArticleTous) {
		this.valueLignesFamilleArticleTous = valueLignesFamilleArticleTous;
	}


	public DocumentChiffreAffaireDTO getNombreFamillesDoc() {
		return nombreFamillesDoc;
	}


	public void setNombreFamillesDoc(DocumentChiffreAffaireDTO nombreFamillesDoc) {
		this.nombreFamillesDoc = nombreFamillesDoc;
	}


	public List<DocumentChiffreAffaireDTO> getValueLignesArticleDetail() {
		return valueLignesArticleDetail;
	}


	public void setValueLignesArticleDetail(List<DocumentChiffreAffaireDTO> valueLignesArticleDetail) {
		this.valueLignesArticleDetail = valueLignesArticleDetail;
	}


	public List<DocumentChiffreAffaireDTO> getValueLignesArticleAvoirsDetail() {
		return valueLignesArticleAvoirsDetail;
	}


	public void setValueLignesArticleAvoirsDetail(List<DocumentChiffreAffaireDTO> valueLignesArticleAvoirsDetail) {
		this.valueLignesArticleAvoirsDetail = valueLignesArticleAvoirsDetail;
	}


	public List<DocumentChiffreAffaireDTO> getValueDetailfamille() {
		return valueDetailfamille;
	}


	public void setValueDetailfamille(List<DocumentChiffreAffaireDTO> valueDetailfamille) {
		this.valueDetailfamille = valueDetailfamille;
	}


	public String getTitreListeSyntheseParArticleTous() {
		return titreListeSyntheseParArticleTous;
	}


	public void setTitreListeSyntheseParArticleTous(String titreListeSyntheseParArticleTous) {
		this.titreListeSyntheseParArticleTous = titreListeSyntheseParArticleTous;
	}


	public String getTitreListeDetailParArticleTous() {
		return titreListeDetailParArticleTous;
	}


	public void setTitreListeDetailParArticleTous(String titreListeDetailParArticleTous) {
		this.titreListeDetailParArticleTous = titreListeDetailParArticleTous;
	}


	public String getTitreListeSyntheseParArticleFacture() {
		return titreListeSyntheseParArticleFacture;
	}


	public void setTitreListeSyntheseParArticleFacture(String titreListeSyntheseParArticleFacture) {
		this.titreListeSyntheseParArticleFacture = titreListeSyntheseParArticleFacture;
	}


	public String getTitreListeSyntheseParArticleMoisFacture() {
		return titreListeSyntheseParArticleMoisFacture;
	}


	public void setTitreListeSyntheseParArticleMoisFacture(String titreListeSyntheseParArticleMoisFacture) {
		this.titreListeSyntheseParArticleMoisFacture = titreListeSyntheseParArticleMoisFacture;
	}


	public String getTitreListeDetailParArticleFacture() {
		return titreListeDetailParArticleFacture;
	}


	public void setTitreListeDetailParArticleFacture(String titreListeDetailParArticleFacture) {
		this.titreListeDetailParArticleFacture = titreListeDetailParArticleFacture;
	}


	public String getTitreListeSyntheseParArticleAvoir() {
		return titreListeSyntheseParArticleAvoir;
	}


	public void setTitreListeSyntheseParArticleAvoir(String titreListeSyntheseParArticleAvoir) {
		this.titreListeSyntheseParArticleAvoir = titreListeSyntheseParArticleAvoir;
	}


	public String getTitreListeSyntheseParArticleMoisAvoir() {
		return titreListeSyntheseParArticleMoisAvoir;
	}


	public void setTitreListeSyntheseParArticleMoisAvoir(String titreListeSyntheseParArticleMoisAvoir) {
		this.titreListeSyntheseParArticleMoisAvoir = titreListeSyntheseParArticleMoisAvoir;
	}


	public String getTitreListeDetailParArticleAvoir() {
		return titreListeDetailParArticleAvoir;
	}


	public void setTitreListeDetailParArticleAvoir(String titreListeDetailParArticleAvoir) {
		this.titreListeDetailParArticleAvoir = titreListeDetailParArticleAvoir;
	}


	public String getTitreListeSyntheseParFamilleTous() {
		return titreListeSyntheseParFamilleTous;
	}


	public void setTitreListeSyntheseParFamilleTous(String titreListeSyntheseParFamilleTous) {
		this.titreListeSyntheseParFamilleTous = titreListeSyntheseParFamilleTous;
	}


	public String getTitreListeSyntheseParFamilleFacture() {
		return titreListeSyntheseParFamilleFacture;
	}


	public void setTitreListeSyntheseParFamilleFacture(String titreListeSyntheseParFamilleFacture) {
		this.titreListeSyntheseParFamilleFacture = titreListeSyntheseParFamilleFacture;
	}


	public String getTitreListeSyntheseParFamilleAvoir() {
		return titreListeSyntheseParFamilleAvoir;
	}


	public void setTitreListeSyntheseParFamilleAvoir(String titreListeSyntheseParFamilleAvoir) {
		this.titreListeSyntheseParFamilleAvoir = titreListeSyntheseParFamilleAvoir;
	}



	public String getTitreEditionListeDetailParArticleTous() {
		return titreEditionListeDetailParArticleTous;
	}


	public void setTitreEditionListeDetailParArticleTous(String titreEditionListeDetailParArticleTous) {
		this.titreEditionListeDetailParArticleTous = titreEditionListeDetailParArticleTous;
	}


	public String getTitreEditionListeDetailParArticleFacture() {
		return titreEditionListeDetailParArticleFacture;
	}


	public void setTitreEditionListeDetailParArticleFacture(String titreEditionListeDetailParArticleFacture) {
		this.titreEditionListeDetailParArticleFacture = titreEditionListeDetailParArticleFacture;
	}





	public String getTitreEditionListeDetailParArticleAvoir() {
		return titreEditionListeDetailParArticleAvoir;
	}


	public void setTitreEditionListeDetailParArticleAvoir(String titreEditionListeDetailParArticleAvoir) {
		this.titreEditionListeDetailParArticleAvoir = titreEditionListeDetailParArticleAvoir;
	}



	public String getTitreEditionListeDetailParFamilleTous() {
		return titreEditionListeDetailParFamilleTous;
	}


	public void setTitreEditionListeDetailParFamilleTous(String titreEditionListeDetailParFamilleTous) {
		this.titreEditionListeDetailParFamilleTous = titreEditionListeDetailParFamilleTous;
	}


	public String getTitreEditionListeDetailParFamilleFacture() {
		return titreEditionListeDetailParFamilleFacture;
	}


	public void setTitreEditionListeDetailParFamilleFacture(String titreEditionListeDetailParFamilleFacture) {
		this.titreEditionListeDetailParFamilleFacture = titreEditionListeDetailParFamilleFacture;
	}


	public String getTitreEditionListeDetailParFamilleAvoir() {
		return titreEditionListeDetailParFamilleAvoir;
	}


	public void setTitreEditionListeDetailParFamilleAvoir(String titreEditionListeDetailParFamilleAvoir) {
		this.titreEditionListeDetailParFamilleAvoir = titreEditionListeDetailParFamilleAvoir;
	}


	public String getTitreEditionListeDetailParArticleFactureFournisseur() {
		return titreEditionListeDetailParArticleFactureFournisseur;
	}


	public String getCodeArticleDTO() {
		return codeArticleDTO;
	}


	public void setCodeArticleDTO(String codeArticleDTO) {
		this.codeArticleDTO = codeArticleDTO;
	}

	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeAndLibelleLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())
					|| (civ.getLibellelArticle()!=null && civ.getLibellelArticle().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getLibellecArticle()!=null && civ.getLibellecArticle().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public void onClearArticle(AjaxBehaviorEvent event){
		taArticleDTO=null;
		codeArticleDTO=null;
		actRechercheArticle(codeEtatArticle);
	}


	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}


	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();


		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaArticleDTO && ((TaArticleDTO) value).getCodeArticle()!=null ) {
				codeArticleDTO=((TaArticleDTO) value).getCodeArticle();
				actRechercheArticle(codeEtatArticle);
			}
		}

	}
}
