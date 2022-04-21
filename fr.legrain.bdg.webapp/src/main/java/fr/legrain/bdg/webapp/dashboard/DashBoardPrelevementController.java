package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
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

import org.primefaces.model.chart.BarChartModel;

import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;

@Named
@ViewScoped
public class DashBoardPrelevementController extends DashBoardDocumentController implements Serializable {


	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2867680096424802141L;
	
	private @EJB ITaPrelevementServiceRemote taPrelevementService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    public List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelPrelevement;
//    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    public Date dateDebut;
//    public Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatPrelevement;
    private DocumentChiffreAffaireDTO totauxCaPeriodePrelevement;
    private DocumentChiffreAffaireDTO totauxCaPeriodePrelevementNonTransforme;
    private DocumentChiffreAffaireDTO totauxCaPeriodePrelevementNonTransformeARelancer;
    private DocumentChiffreAffaireDTO totauxCaPeriodePrelevementTransforme;
    
    private List<DocumentDTO> valueLignesPrelevement;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodePrelevement;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodePrelevementNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodePrelevementNonTransformeARelancer;
    private int nbPrelevementSurPeriode = 0;
    private int nbPrelevementNonTransformeSurPeriode = 0 ;
    private int nbPrelevementTransformeSurPeriode = 0;
    private int nbPrelevementNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal montantTotalHt = BigDecimal.ZERO;
    private BigDecimal montantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal montantTotalHtNonTransfoARelancer = BigDecimal.ZERO;

    private List<DocumentDTO> listeDocumentDetailMois;
    private List<DocumentDTO> listeDocumentDetailJour;
    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParMois;
    private List<TaArticlesParTiersDTO> listeDocumentArticleParTiersParJour;
    
    private String nomMoisDetail;
    private int numeroJourDetail; //AJOUTER LE 06/09/2017 Jean Marc
    private boolean affichePrelevementJour; //AJOUTER LE 06/09/2017 Jean Marc
    private boolean affichePrelevementJourDataTable; //AJOUTER LE 06/09/2017 Jean Marc
    private String infosLabelRapportMensuelDataTable; //AJOUTER LE 14/09/2017 Jean Marc
    private String infosLabelRapportArticlesDataTable; //AJOUTER LE 14/09/2017 Jean Marc
    private String infosLabelRapportJournalierDataTable; //AJOUTER LE 19/09/2017 Jean Marc
    private String infosLabelRapportArticlesJourDataTable; //AJOUTER LE 19/09/2017 Jean Marc
    
    private List<DocumentChiffreAffaireDTO> cumulMoisDetail;
    private List<DocumentChiffreAffaireDTO> cumulJourDetail; //AJOUTER LE 06/09/2017 Jean Marc
    private BigDecimal montantTotalHtMois = BigDecimal.ZERO;
    private BigDecimal montantTotalHtJour = BigDecimal.ZERO;

    private int deltaNbJours = -3;
    
    @PostConstruct
	public void postConstruct(){
    	super.postConstruct();
    	codeEtatPrelevement = "tous";
    	listeDocumentDetailMois = new ArrayList<DocumentDTO>();
    	listeDocumentDetailJour = new ArrayList<DocumentDTO>();
    	listeDocumentArticleParTiersParMois = new ArrayList<TaArticlesParTiersDTO>();
    	listeDocumentArticleParTiersParJour = new ArrayList<TaArticlesParTiersDTO>();
    	//Initialisation des données sur la période exercice
    	actRecherchePrelevement(codeEtatPrelevement);  
    }


    public void refreshDashBoard(String etatPrelevement){
    	setAffichePrelevementJour(false); //AJOUTER LE 06/09/2017 Jean Marc
    	setAffichePrelevementJourDataTable(false); //AJOUTER LE 06/09/2017 Jean Marc
    	listeDocumentDetailMois.clear(); //AJOUTER LE 06/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentDetailJour.clear(); //AJOUTER LE 19/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParMois.clear(); //AJOUTER LE 14/09/2017 Jean Marc vide la table à chaque changement de proforma
    	listeDocumentArticleParTiersParJour.clear(); //AJOUTER LE 20/09/2017 Jean Marc vide la table à chaque changement de proforma
   	
    	actRecherchePrelevement(etatPrelevement);
    }

    public void actRecherchePrelevement(String codeEtatPrelevement){

    	setInfoEtatDocument(codeEtatPrelevement);
    	
    	listMoisAnneeExo = LibDate.listeMoisEntreDeuxDate(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));

    	
    	// Calcule les CA (HT, TVA, TTC) total sur la période
    	totauxCaPeriodePrelevement = taPrelevementService.chiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		// Calcule le CA (HT, TVA, TTC) total des Prelevement non transformé sur la période
		totauxCaPeriodePrelevementNonTransforme = taPrelevementService.chiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Calcule le CA (HT, TVA, TTC) total des Prelevement non transformé à relancer sur la période
		totauxCaPeriodePrelevementNonTransformeARelancer = taPrelevementService.chiffreAffaireNonTransformeARelancerTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), deltaNbJours,null);

		// Calcule le CA (HT, TVA, TTC) total des Proforma transformé sur la période
		totauxCaPeriodePrelevementTransforme = taPrelevementService.chiffreAffaireTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		
		// Retourne le nombre total de documents sur la période
		nbPrelevementSurPeriode = (int) taPrelevementService.countDocument(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma non transformés sur la période
		nbPrelevementNonTransformeSurPeriode = (int) taPrelevementService.countDocumentNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
		// Retourne le nombre de Proforma à relancer d'ici 3 jours (aujourd'hui d'ici la date d'échéance) sur la période
		nbPrelevementNonTransformeARelancerSurPeriode = (int) taPrelevementService.countDocumentNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), deltaNbJours,null);
		// Retourne le nombre de Proforma transformés sur la période
		nbPrelevementTransformeSurPeriode = (int) taPrelevementService.countDocumentTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);

		
		if (nbPrelevementSurPeriode > 0) {
			try {
				tauxTransfoNb =  (new BigDecimal(nbPrelevementTransformeSurPeriode) .divide(new BigDecimal(nbPrelevementSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
				tauxTransfoCa = totauxCaPeriodePrelevementTransforme.getMtHtCalc() .divide(totauxCaPeriodePrelevement.getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
		
		
    	switch (codeEtatPrelevement) {
		case ETAT_DOCUMENT_TOUS:
//			// Renvoi la liste des documents présents sur la période en cours
			valueLignesPrelevement = taPrelevementService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des prélévements réalisés en €";
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
			barChartModelPrelevement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taPrelevementService.listeChiffreAffaireTotalJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelPrelevement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			barChartModelProforma.clear();

			etatInfosLabelRapportMensuelDataTable();
			// Initialisent les classes css pour les block d'information
			initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_PRELEVEMENT, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL, 
							  CSS_DBOARD_BLOC_INACTIF_ALL);

			break;

		case ETAT_DOCUMENT_NON_TRANSORME:
			valueLignesPrelevement = taPrelevementService.findDocumentNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des prélévements non transformé en €";
			barChartModelPrelevement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taPrelevementService.listeChiffreAffaireNonTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelPrelevement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));
//			barChartModelProforma.clear();
			etatInfosLabelRapportMensuelDataTable();
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL,
								CSS_DBOARD_BLOC_ACTIF_PRELEVEMENT, 
								CSS_DBOARD_BLOC_INACTIF_ALL, 
								CSS_DBOARD_BLOC_INACTIF_ALL);			

			//setInfoCase("non transformer");
			
			break;
			
			
			
		case ETAT_DOCUMENT_TRANSORME:
			valueLignesPrelevement = taPrelevementService.findDocumentTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin),null);
			infoLabel = "Liste des prélévements transformé en €";
			barChartModelPrelevement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taPrelevementService.listeChiffreAffaireTransformeJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1,null);
			barChartModelPrelevement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//			barChartModelProforma.clear();
//			barChartModelProformaJour.clear();
			etatInfosLabelRapportMensuelDataTable();
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  			CSS_DBOARD_BLOC_INACTIF_ALL, 
			  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
			  		  			CSS_DBOARD_BLOC_ACTIF_PRELEVEMENT);

			//setInfoCase("transformer");
			break;
			
		case ETAT_DOCUMENT_NON_TRANSORME_A_RELANCER:
			try {
			valueLignesPrelevement = taPrelevementService.findDocumentNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), deltaNbJours,null);
			infoLabel = "Liste des prélévements à verifier en €";
			barChartModelPrelevement = createBarChartModelSimpleBarre("Totaux HT mensuel", true, -50);
			listeCaMoisDocument = taPrelevementService.listeChiffreAffaireNonTransformeARelancerJmaDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 1, deltaNbJours,null);
			barChartModelPrelevement.addSeries(createBarChartSerieMensuel("Mois", listeCaMoisDocument, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin)));

//			//listeCaPeriodeProforma = taProformaService.findCAProformaSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			barChartModelProforma.clear();
//			barChartModelProformaJour.clear();
			etatInfosLabelRapportMensuelDataTable();
			initClasseCssBloc(	CSS_DBOARD_BLOC_INACTIF_ALL, 
	  		  		  			CSS_DBOARD_BLOC_INACTIF_ALL,
	  		  		  			CSS_DBOARD_BLOC_ACTIF_PRELEVEMENT, 
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
    
    public void actImprimerListePrelevement(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", infoLabel);
		
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listePrelevement", valueLignesPrelevement);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void etatInfosLabelRapportMensuelDataTable() {
		
		if (affichePrelevementJour==true) {
			setInfosLabelRapportMensuelDataTable("Liste des Prelevement du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
			setInfosLabelRapportArticlesDataTable("Liste des lignes article du mois de "+nomMoisDetail+" pour un montant de "+montantTotalHtMois+" € de type "+infoEtatDocument);
			setInfosLabelRapportJournalierDataTable("Liste des Prelevement du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
			setInfosLabelRapportArticlesJourDataTable("Liste des lignes article du "+(numeroJourDetail+1)+" "+nomMoisDetail+" pour un montant de "+montantTotalHtJour+" € de type "+infoEtatDocument);
		}
		else {
			setInfosLabelRapportMensuelDataTable("Cliquez sur une barre du graphique pour avoir le détail mensuel des ventes");
			setInfosLabelRapportArticlesDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
			setInfosLabelRapportJournalierDataTable("Cliquez sur une barre du graphique pour avoir le détail journalier des ventes");
			setInfosLabelRapportArticlesJourDataTable("Cliquez sur une barre du graphique pour avoir le détail des articles par tiers");
		}
		
		if (valueLignesPrelevement.isEmpty()){
			setInfosLabelRapportMensuelDataTable("La liste des Prelevement "+infoEtatDocument+" est vide");
			setInfosLabelRapportArticlesDataTable("La liste des articles "+infoEtatDocument+" est vide");
			setInfosLabelRapportJournalierDataTable("La liste des Prelevement "+infoEtatDocument+" est vide");
		}
		}
		
	
	public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelPrelevement() {
		return barChartModelPrelevement;
	}
	public void setBarChartModelPrelevementExo(BarChartModel barChartModelPrelevement) {
		this.barChartModelPrelevement = barChartModelPrelevement;
	}
	

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public String getCodeEtatPrelevement() {
		return codeEtatPrelevement;
	}

	public void setCodeEtatPrelevement(String codeEtatPrelevement) {
		this.codeEtatPrelevement = codeEtatPrelevement;
	}

	public List<DocumentDTO> getValueLignesPrelevement() {
		return valueLignesPrelevement;
	}

	public void setValueLignesPrelevement(List<DocumentDTO> valueLignesPrelevement) {
		this.valueLignesPrelevement = valueLignesPrelevement;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodePrelevement() {
		return listeCaPeriodePrelevement;
	}

	public void setListeCaPeriodePrelevement(List<DocumentChiffreAffaireDTO> listeCaPeriodePrelevement) {
		this.listeCaPeriodePrelevement = listeCaPeriodePrelevement;
	}

	public int getNbPrelevementSurPeriode() {
		return nbPrelevementSurPeriode;
	}

	public void setNbPrelevementSurPeriode(int nbPrelevementSurPeriode) {
		this.nbPrelevementSurPeriode = nbPrelevementSurPeriode;
	}

	public int getNbPrelevementNonTransformeSurPeriode() {
		return nbPrelevementNonTransformeSurPeriode;
	}

	public void setNbPrelevementNonTransformeSurPeriode(int nbPrelevementNonTransformeSurPeriode) {
		this.nbPrelevementNonTransformeSurPeriode = nbPrelevementNonTransformeSurPeriode;
	}


	public int getNbPrelevementNonTransformeARelancerSurPeriode() {
		return nbPrelevementNonTransformeARelancerSurPeriode;
	}


	public void setNbPrelevementNonTransformeARelancerSurPeriode(int nbPrelevementNonTransformeARelancerSurPeriode) {
		this.nbPrelevementNonTransformeARelancerSurPeriode = nbPrelevementNonTransformeARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodePrelevementNonTransforme() {
		return listeCaPeriodePrelevementNonTransforme;
	}


	public void setListeCaPeriodePrelevementNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodePrelevementNonTransforme) {
		this.listeCaPeriodePrelevementNonTransforme = listeCaPeriodePrelevementNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbPrelevementTransformeSurPeriode() {
		return nbPrelevementTransformeSurPeriode;
	}


	public void setNbPrelevementTransformeSurPeriode(int nbPrelevementTransformeSurPeriode) {
		this.nbPrelevementTransformeSurPeriode = nbPrelevementTransformeSurPeriode;
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


	public int getDeltaNbJours() {
		return deltaNbJours;
	}


	public void setDeltaNbJours(int deltaNbJours) {
		this.deltaNbJours = deltaNbJours;
	}


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodePrelevementNonTransformeARelancer() {
		return listeCaPeriodePrelevementNonTransformeARelancer;
	}


	public void setListeCaPeriodePrelevementNonTransformeARelancer(
			List<DocumentChiffreAffaireDTO> listeCaPeriodePrelevementNonTransformeARelancer) {
		this.listeCaPeriodePrelevementNonTransformeARelancer = listeCaPeriodePrelevementNonTransformeARelancer;
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


	public void setBarChartModelPrelevement(BarChartModel barChartModelPrelevement) {
		this.barChartModelPrelevement = barChartModelPrelevement;
	}


	public boolean isAffichePrelevementJour() {
		return affichePrelevementJour;
	}


	public void setAffichePrelevementJour(boolean affichePrelevementJour) {
		this.affichePrelevementJour = affichePrelevementJour;
	}


	public boolean isAffichePrelevementJourDataTable() {
		return affichePrelevementJourDataTable;
	}


	public void setAffichePrelevementJourDataTable(boolean affichePrelevementJourDataTable) {
		this.affichePrelevementJourDataTable = affichePrelevementJourDataTable;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodePrelevement() {
		return totauxCaPeriodePrelevement;
	}


	public void setTotauxCaPeriodePrelevement(DocumentChiffreAffaireDTO totauxCaPeriodePrelevement) {
		this.totauxCaPeriodePrelevement = totauxCaPeriodePrelevement;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodePrelevementNonTransforme() {
		return totauxCaPeriodePrelevementNonTransforme;
	}


	public void setTotauxCaPeriodePrelevementNonTransforme(
			DocumentChiffreAffaireDTO totauxCaPeriodePrelevementNonTransforme) {
		this.totauxCaPeriodePrelevementNonTransforme = totauxCaPeriodePrelevementNonTransforme;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodePrelevementNonTransformeARelancer() {
		return totauxCaPeriodePrelevementNonTransformeARelancer;
	}


	public void setTotauxCaPeriodePrelevementNonTransformeARelancer(
			DocumentChiffreAffaireDTO totauxCaPeriodePrelevementNonTransformeARelancer) {
		this.totauxCaPeriodePrelevementNonTransformeARelancer = totauxCaPeriodePrelevementNonTransformeARelancer;
	}


	public DocumentChiffreAffaireDTO getTotauxCaPeriodePrelevementTransforme() {
		return totauxCaPeriodePrelevementTransforme;
	}


	public void setTotauxCaPeriodePrelevementTransforme(DocumentChiffreAffaireDTO totauxCaPeriodePrelevementTransforme) {
		this.totauxCaPeriodePrelevementTransforme = totauxCaPeriodePrelevementTransforme;
	}


	public List<DocumentChiffreAffaireDTO> getCumulMoisDetail() {
		return cumulMoisDetail;
	}


	public void setCumulMoisDetail(List<DocumentChiffreAffaireDTO> cumulMoisDetail) {
		this.cumulMoisDetail = cumulMoisDetail;
	}


	public List<DocumentChiffreAffaireDTO> getCumulJourDetail() {
		return cumulJourDetail;
	}


	public void setCumulJourDetail(List<DocumentChiffreAffaireDTO> cumulJourDetail) {
		this.cumulJourDetail = cumulJourDetail;
	}

}
