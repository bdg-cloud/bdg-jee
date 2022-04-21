package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dto.TaTiersDTO;

@Named
@ViewScoped
public class DashBoardTiersController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5979028746642381345L;
	/**
	 * 
	 */
	

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelTiers;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
//    private Date dateDebut;
//    private Date dateFin;
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
    private BigDecimal MontantTotalHt = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    private String cSbNbTiersSurPeriode;
    private String cSbNbTiersTransformeSurPeriode;
    private String cSbNbTiersNonTransformeSurPeriode;
    private String cSbNbTiersNonTransformeARelancerSurPeriode;
    
    //rajout isa
    private String codeArticleDTO;
    private TaArticleDTO taArticleDTO;
    private String codeTiersDTO;
    private TaTiersDTO taTiersDTO;
    private String titreListeSyntheseParTiersTous = "Synthèse des ventes (et avoirs) par Client";
    private String titreListeSyntheseParTiersArticleTous = "Synthèse des ventes (et avoirs) par Client et articles";
    private String titreListeDetailParTiersTous ="Détail des ventes (et avoirs) par Client"; 
    private String titreEditionListeDetailParTiersTous ="Détail des ventes (et avoirs)par Client, par articles et par unités"; 
    private String titreEditionListeDetailParTiersArticleTous ="Détail des ventes (et avoirs) par Client"; 
    private List<DocumentChiffreAffaireDTO> valueLignesTiersTous;
    private List<DocumentChiffreAffaireDTO> valueLignesTiersArticlesTous;
    private List<DocumentChiffreAffaireDTO> valueLignesTiersTousDetail;
    
    
    @PostConstruct
	public void postConstruct(){
    	super.initChoixPeriode();
    	infoEntreprise = taInfoEntrepriseService.findInstance();
    	dateDebut = LibDate.dateToLocalDate(infoEntreprise.getDatedebInfoEntreprise());
    	dateFin = LibDate.dateToLocalDate(infoEntreprise.getDatefinInfoEntreprise());
    	codeEtatTiers = "tous";
    	//barChartModelTiers = new BarChartModel();
    	//Initialisation des données sur la période exercice
    	actRechercheTiers(codeEtatTiers);
//    	createBarModel();
    }

    
    public void refreshDashBoard(String etatTiers){
    	
    }

    public void refresh(String etatTiers){
    	actRechercheTiers(etatTiers);
    }

    public void actRechercheTiers(String codeEtatTiers){
//    	nbTiersActif = taTiersService.countAllTiersActifTaTiersDTO();
////    	if (action != null) {
////    	codeEtatTiers = (String) action.getComponent().getAttributes().get("codeetatTiers");
////    	}
//    	switch (codeEtatTiers) {
//		case "tous":
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesTiers = taTiersService.findAllDTOPeriode(dateDebut, dateFin);
//    		valueLignesTiers = taTiersService.findAllLight();
    	
    	//rajout isa pour édition
    		if(codeTiersDTO!=null) {
    			valueLignesTiers.clear();;
    			valueLignesTiers.add(taTiersDTO);
    			nbTiersActif=1;
    		}else {		
    			valueLignesTiers = taTiersService.findAllLight();
    			nbTiersActif = valueLignesTiers.size();
    		}
//    		valueLignesTiersTous=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS);
//    		valueLignesTiersArticlesTous=taFactureService.listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);
//    		valueLignesTiersTousDetail=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, false, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);
//    		syntheseEtDetailValueLigneArticleTous();
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelTiers = createBarModel(dateDebut, dateFin, barChartModelTiers, TaTiers.TYPE_DOC, 1 );
//			// Calcule les CA (HT, TVA, TTC) total sur la période
////			listeCaPeriodeTiers = taTiersService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//			listeCaPeriodeTiers = findChiffreAffaireTotalDocument(TaTiers.TYPE_DOC, dateDebut, dateFin);
//			// Calcule le CA (HT, TVA, TTC) total des Tiers non transformé sur la période
////			listeCaPeriodeTiersNonTransforme = taTiersService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			listeCaPeriodeTiersNonTransforme = findChiffreAffaireNonTransformeTotalDTODocument(TaTiers.TYPE_DOC, dateDebut, dateFin);
//			// Calcule le CA (HT, TVA, TTC) total des Tiers non transformé sur la période
//			// La requête retourne un objet vide alors qu'il n'y a rien dans la table, à vérifier car cela génère une erreur lorsque
//			//l'on veut consulter les données ci-après.
//			listeCaPeriodeTiersNonTransformeARelancer = taTiersService.caTiersNonTransfosARelancerDTO(dateDebut, dateFin, 15);
//			// Retourne le nombre total de documents sur la période
//			nbTiersSurPeriode = (int) taTiersService.countTiers(dateDebut, dateFin);
//			// Retourne le nombre de Tiers non transformés sur la période
//			nbTiersNonTransformeSurPeriode = (int) taTiersService.countTiersNonTransforme(dateDebut, dateFin);
//			// Retourne le nombre de Tiers à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
//			nbTiersNonTransformeARelancerSurPeriode = (int) taTiersService.countTiersNonTransformeARelancer(dateDebut, dateFin, 15);
//			
//			
//			
//			
//			if (nbTiersSurPeriode > 0) {
//				try {
//					MontantTotalHt = listeCaPeriodeTiers.get(0).getMtHtCalc();
//					nbTiersTransformeSurPeriode = nbTiersSurPeriode - nbTiersNonTransformeSurPeriode;
//					tauxTransfoNb =  (new BigDecimal(nbTiersTransformeSurPeriode) .divide(new BigDecimal(nbTiersSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//					if ((listeCaPeriodeTiersNonTransforme != null && ! listeCaPeriodeTiersNonTransforme.isEmpty())) {
//						MontantTotalHtNonTransfo = listeCaPeriodeTiersNonTransforme.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//					if ((listeCaPeriodeTiers != null && ! listeCaPeriodeTiers.isEmpty()) || (listeCaPeriodeTiersNonTransforme != null && ! listeCaPeriodeTiersNonTransforme.isEmpty())) {
//						MontantTotalHtTransfo = listeCaPeriodeTiers.get(0).getMtHtCalc() .subtract(listeCaPeriodeTiersNonTransforme.get(0).getMtHtCalc());
//						if (MontantTotalHtTransfo.intValue() > 0){
//						tauxTransfoCa =  (MontantTotalHtTransfo .divide(listeCaPeriodeTiers.get(0).getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)).setScale(0,BigDecimal.ROUND_HALF_UP);
//						}
//					}
//					if (listeCaPeriodeTiersNonTransformeARelancer != null && ! listeCaPeriodeTiersNonTransformeARelancer.isEmpty()) {
//						MontantTotalHtNonTransfoARelancer = listeCaPeriodeTiersNonTransformeARelancer.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					// TODO: handle exception
//				}
//				
//				
//			}
    		
    		initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_TIERS, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
    		
    		
    		
    		
//			// Initialisent les classes css pour les block d'information
			cSbNbTiersSurPeriode = "greenback";
			cSbNbTiersTransformeSurPeriode = "whiteback";
			cSbNbTiersNonTransformeSurPeriode = "whiteback";
			cSbNbTiersNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//
//		case "nontransforme":
//			valueLignesTiers = taTiersService.findTiersNonTransfosDTO(dateDebut, dateFin);
//			//listeCaPeriodeTiers = taTiersService.findCATiersSurPeriode(dateDebut, dateFin);
//			nbTiersSurPeriode = (int) taTiersService.countTiers(dateDebut, dateFin);
//			nbTiersNonTransformeSurPeriode = (int) taTiersService.countTiersNonTransforme(dateDebut, dateFin);
//			nbTiersNonTransformeARelancerSurPeriode = (int) taTiersService.countTiersNonTransformeARelancer(dateDebut, dateFin, 15);
//			cSbNbTiersSurPeriode = "whiteback";
//			cSbNbTiersTransformeSurPeriode = "whiteback";
//			cSbNbTiersNonTransformeSurPeriode = "redback";			
//			cSbNbTiersNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "transforme":
//			valueLignesTiers = taTiersService.findTiersTransfosDTO(dateDebut, dateFin);
//			//listeCaPeriodeTiers = taTiersService.findCATiersSurPeriode(dateDebut, dateFin);
//			nbTiersSurPeriode = (int) taTiersService.countTiers(dateDebut, dateFin);
//			nbTiersNonTransformeSurPeriode = (int) taTiersService.countTiersNonTransforme(dateDebut, dateFin);
//			nbTiersNonTransformeARelancerSurPeriode = (int) taTiersService.countTiersNonTransformeARelancer(dateDebut, dateFin, 15);
//			cSbNbTiersSurPeriode = "whiteback";
//			cSbNbTiersTransformeSurPeriode = "redback";
//			cSbNbTiersNonTransformeSurPeriode = "whiteback";			
//			cSbNbTiersNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "nontransformearelancer":
//			valueLignesTiers = taTiersService.findTiersNonTransfosARelancerDTO(dateDebut, dateFin, 15);
//			//listeCaPeriodeTiers = taTiersService.findCATiersSurPeriode(dateDebut, dateFin);
//			nbTiersSurPeriode = (int) taTiersService.countTiers(dateDebut, dateFin);
//			nbTiersNonTransformeSurPeriode = (int) taTiersService.countTiersNonTransforme(dateDebut, dateFin);
//			nbTiersNonTransformeARelancerSurPeriode = (int) taTiersService.countTiersNonTransformeARelancer(dateDebut, dateFin, 15);
//			cSbNbTiersSurPeriode = "whiteback";
//			cSbNbTiersTransformeSurPeriode = "whiteback";
//			cSbNbTiersNonTransformeSurPeriode = "whiteback";			
//			cSbNbTiersNonTransformeARelancerSurPeriode = "redback";
//			break;
//			
//		default:
//			break;
//		}
//    	
//    }

//    public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal() {
//		
////    	List<Object> list = taTiersService.findCATiersSurPeriode(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise());
////    	List<DocumentChiffreAffaireDTO> list = taTiersService.findChiffreAffaireTotal(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//    	List<DocumentChiffreAffaireDTO> list = taTiersService.findChiffreAffaireTotalDTO(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//		return list;
//    	
    }
    
public void actImprimerListeTiers(ActionEvent actionEvent) {
		
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		
//		sessionMap.put("debut", getDateDebut());
//		sessionMap.put("fin", getDateFin());
		sessionMap.put("parametre", mapParametre);
		sessionMap.put("listeTiers", valueLignesTiers);
	
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
	public BarChartModel getBarChartModelTiers() {
		return barChartModelTiers;
	}
	public void setBarChartModelTiersExo(BarChartModel barChartModelTiers) {
		this.barChartModelTiers = barChartModelTiers;
	}
	
//	private BarChartModel initBarModelTiersExo() {
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
//		barChartModelTiers = initBarModelTiersExo();
//         
//		barChartModelTiers.setTitle("Chiffre d'affaire HT");
//		barChartModelTiers.setLegendPosition("ne");
//		barChartModelTiers.setAnimate(true);
//         
//        Axis xAxis = barChartModelTiers.getAxis(AxisType.X);
////        xAxis.setLabel("Gender");
//         
//        Axis yAxis = barChartModelTiers.getAxis(AxisType.Y);
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


	public String getcSbNbTiersTransformeSurPeriode() {
		return cSbNbTiersTransformeSurPeriode;
	}


	public void setcSbNbTiersTransformeSurPeriode(String cSbNbTiersTransformeSurPeriode) {
		this.cSbNbTiersTransformeSurPeriode = cSbNbTiersTransformeSurPeriode;
	}


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


	public String getCodeArticleDTO() {
		return codeArticleDTO;
	}


	public void setCodeArticleDTO(String codeArticleDTO) {
		this.codeArticleDTO = codeArticleDTO;
	}


	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}


	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}


	public String getCodeTiersDTO() {
		return codeTiersDTO;
	}


	public void setCodeTiersDTO(String codeTiersDTO) {
		this.codeTiersDTO = codeTiersDTO;
	}


	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}


	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
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
	
	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())
					|| (civ.getPrenomTiers() != null && civ.getPrenomTiers().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getNomEntreprise() != null && civ.getNomEntreprise().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public void onClearArticle(AjaxBehaviorEvent event){
		taArticleDTO=null;
		codeArticleDTO=null;
		actRechercheTiers(codeEtatTiers);
	}

	public void onClearTiers(AjaxBehaviorEvent event){
		taTiersDTO=null;
		codeTiersDTO=null;
		actRechercheTiers(codeEtatTiers);
	}


	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();


		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaArticleDTO && ((TaArticleDTO) value).getCodeArticle()!=null ) {
				codeArticleDTO=((TaArticleDTO) value).getCodeArticle();
				actRechercheTiers(codeEtatTiers);
			}
			if(value instanceof TaTiersDTO && ((TaTiersDTO) value).getCodeTiers()!=null ) {
				codeTiersDTO=((TaTiersDTO) value).getCodeTiers();
				actRechercheTiers(codeEtatTiers);
			}			
		}

	}


//    // Synthèse et détail facturé
//    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneArticle(){
//    	List<DocumentChiffreAffaireDTO> listSynthese = taFactureService.listeSumChiffreAffaireTotalDTOArticle(dateDebut, dateFin, codeArticleDTO, codeTiersDTO);
//    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
////    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticleDTOTiers( dateDebut, dateFin,  synthese.getCodeArticle(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//    		List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticleDTOTiersUnite(dateDebut, dateFin, synthese.getCodeArticle(), synthese.getU1LDocument(), null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//    		for (DocumentChiffreAffaireDTO detail : listDetail) {
//    			synthese.getListeDetail().add(detail);
//			}
//    		
//		}
//    		
//    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
//    	return syntheseEtDetail;
//    }
//    // Synthèse et détail avoirs
//    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneAvoirs(){
//    	List<DocumentChiffreAffaireDTO> listSynthese = taAvoirService.listeSumChiffreAffaireTotalDTOArticle(dateDebut, dateFin, codeArticleDTO, codeTiersDTO);
//    	for (DocumentChiffreAffaireDTO synthese : listSynthese ) {
////    		List<DocumentChiffreAffaireDTO> listDetail = taAvoirService.listLigneArticleDTOTiers( dateDebut, dateFin,  synthese.getCodeArticle(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//    		List<DocumentChiffreAffaireDTO> listDetail = taAvoirService.listLigneArticleDTOTiersUnite( dateDebut, dateFin,  synthese.getCodeArticle(), synthese.getU1LDocument(),  null,  DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//    		
//    		for (DocumentChiffreAffaireDTO detail : listDetail) {
//    			synthese.getListeDetail().add(detail);
//			}
//    		
//		}
//    		
//    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = listSynthese;
//    	return syntheseEtDetail;
//    }
    // Synthèse et détail TOUS
    public List<DocumentChiffreAffaireDTO> syntheseEtDetailValueLigneArticleTous(){
   		valueLignesTiersTous=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS);
//		valueLignesTiersArticlesTous=taFactureService.listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);
		valueLignesTiersTousDetail=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, false, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);

		
    	for (DocumentChiffreAffaireDTO synthese : valueLignesTiersTous ) {
//   		    List<DocumentChiffreAffaireDTO> listDetail = taFactureService.listLigneArticlePeriodeParTiersParUniteAvoirFactureDTO( LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), synthese.getCodeArticle(),synthese.getCodeTiers(),synthese.getU1LDocument(), DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT);
//    		List<DocumentChiffreAffaireDTO> listDetail = new LinkedList<>();
    		for (DocumentChiffreAffaireDTO documentChiffreAffaireDTO : valueLignesTiersTousDetail) {
				if(documentChiffreAffaireDTO.getCodeTiers().equals(synthese.getCodeTiers()))
					synthese.getListeDetail().add(documentChiffreAffaireDTO);
			}
    		
		}
    		
//    	List<DocumentChiffreAffaireDTO> syntheseEtDetail = valueLignesTiersTous;
    	return valueLignesTiersTous;
    }

public void actImprimerListeArticleTousGroupeTiersArticles(ActionEvent actionEvent) {
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		Map<String,Object> mapParametre = new HashMap<String,Object>();
		mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
		mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
		mapParametre.put("infosLabel", titreListeSyntheseParTiersArticleTous);
		mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
		
		
		sessionMap.put("parametre", mapParametre);
		
		valueLignesTiersArticlesTous=taFactureService.listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE);
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
		mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
//		mapParametre.put("infosLabel", "Synthèse des ventes (et avoirs) par articles et par unités");
		
		
		sessionMap.put("parametre", mapParametre);
		
   		valueLignesTiersTous=taFactureService.listLigneArticlePeriodeParTiersAvoirFactureDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), codeArticleDTO,codeTiersDTO, true, DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS);
		sessionMap.put("listeArticle", valueLignesTiersTous);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

public void actImprimerRapportDetailDesVenteEtAvoirParTiers(ActionEvent actionEvent) {
	
	try {
				
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	Map<String, Object> sessionMap = externalContext.getSessionMap();

	Map<String,Object> mapParametre = new HashMap<String,Object>();
	mapParametre.put("debut", LibDate.localDateToDate(getDateDebut()));
	mapParametre.put("fin", LibDate.localDateToDate(getDateFin()));
	mapParametre.put("infosLabel", titreEditionListeDetailParTiersArticleTous);
	mapParametre.put("entreprise", infoEntreprise.getNomInfoEntreprise());
//	mapParametre.put("infosLabel", "Détail des ventes (et avoirs) par article");
	
	sessionMap.put("parametre", mapParametre);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneArticleTous());
//	sessionMap.put("listeArticle", valueLignesTiersTousDetail);
	
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
//	sessionMap.put("listeArticle", valueLignesTiersTousDetail);
	sessionMap.put("listeArticle", syntheseEtDetailValueLigneArticleTous());
	
	} catch (Exception e) {
		e.printStackTrace();
	}
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


public String getTitreListeDetailParTiersTous() {
	return titreListeDetailParTiersTous;
}


public void setTitreListeDetailParTiersTous(String titreListeDetailParTiersTous) {
	this.titreListeDetailParTiersTous = titreListeDetailParTiersTous;
}


public String getTitreEditionListeDetailParTiersTous() {
	return titreEditionListeDetailParTiersTous;
}


public void setTitreEditionListeDetailParTiersTous(String titreEditionListeDetailParTiersTous) {
	this.titreEditionListeDetailParTiersTous = titreEditionListeDetailParTiersTous;
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


public List<DocumentChiffreAffaireDTO> getValueLignesTiersTousDetail() {
	return valueLignesTiersTousDetail;
}


public void setValueLignesTiersTousDetail(List<DocumentChiffreAffaireDTO> valueLignesTiersTousDetail) {
	this.valueLignesTiersTousDetail = valueLignesTiersTousDetail;
}


public List<DocumentChiffreAffaireDTO> getValueLignesTiersArticlesTous() {
	return valueLignesTiersArticlesTous;
}


public void setValueLignesTiersArticlesTous(List<DocumentChiffreAffaireDTO> valueLignesTiersArticlesTous) {
	this.valueLignesTiersArticlesTous = valueLignesTiersArticlesTous;
} 


}
