package fr.legrain.bdg.webapp.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.chart.BarChartModel;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;

@Named
@ViewScoped
public class DashBoardFabricationController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5979028746642381345L;
	/**
	 * 
	 */
	

	private @EJB ITaFabricationServiceRemote taFabricationService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelArticle;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
    private Date dateDebut;
    private Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatFabrication;
    private List<TaFabricationDTO> valueLignesFabrications;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticle;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeArticleNonTransformeARelancer;
    private int nbFabricationActif = 0;
    private int nbArticleNonTransformeSurPeriode = 0 ;
    private int nbArticleTransformeSurPeriode = 0;
    private int nbArticleNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal MontantTotalHt = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    private String cSbNbArticleSurPeriode;
    private String cSbNbArticleTransformeSurPeriode;
    private String cSbNbArticleNonTransformeSurPeriode;
    private String cSbNbArticleNonTransformeARelancerSurPeriode;
    
    @PostConstruct
	public void postConstruct(){
    	infoEntreprise = taInfoEntrepriseService.findInstance();
    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
    	codeEtatFabrication = "tous";
    	//barChartModelArticle = new BarChartModel();
    	//Initialisation des données sur la période exercice
    	actRechercheFabrication(codeEtatFabrication);
//    	createBarModel();
    }


    public void refresh(String etatFabrication){
    	actRechercheFabrication(etatFabrication);
    }

    public void refreshDashBoard(String etatDocument){

    }    
    public void actRechercheFabrication(String codeEtatFabrication){
//    		nbFabricationActif = taFabricationService.;
    	
////    	if (action != null) {
////    	codeEtatFabrication = (String) action.getComponent().getAttributes().get("codeetatFabrication");
////    	}
//    	switch (codeEtatFabrication) {
//		case "tous":
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesFabrications = taFabricationService.findAllDTOPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
    		valueLignesFabrications = taFabricationService.findAllLight();
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelArticle = createBarModel(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), barChartModelArticle, TaArticle.TYPE_DOC, 1 );
//			// Calcule les CA (HT, TVA, TTC) total sur la période
////			listeCaPeriodeArticle = taFabricationService.findChiffreAffaireTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			listeCaPeriodeArticle = findChiffreAffaireTotalDocument(TaArticle.TYPE_DOC, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Calcule le CA (HT, TVA, TTC) total des Article non transformé sur la période
////			listeCaPeriodeArticleNonTransforme = taFabricationService.findChiffreAffaireNonTransformeTotalDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			listeCaPeriodeArticleNonTransforme = findChiffreAffaireNonTransformeTotalDTODocument(TaArticle.TYPE_DOC, LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Calcule le CA (HT, TVA, TTC) total des Article non transformé sur la période
//			// La requête retourne un objet vide alors qu'il n'y a rien dans la table, à vérifier car cela génère une erreur lorsque
//			//l'on veut consulter les données ci-après.
//			listeCaPeriodeArticleNonTransformeARelancer = taFabricationService.caArticleNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			// Retourne le nombre total de documents sur la période
//			nbArticleSurPeriode = (int) taFabricationService.countArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Retourne le nombre de Article non transformés sur la période
//			nbArticleNonTransformeSurPeriode = (int) taFabricationService.countArticleNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			// Retourne le nombre de Article à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
//			nbArticleNonTransformeARelancerSurPeriode = (int) taFabricationService.countArticleNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
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
    		
    		initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_DEVIS, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
    		
    		
    		
//			// Initialisent les classes css pour les block d'information
			cSbNbArticleSurPeriode = "redback";
			cSbNbArticleTransformeSurPeriode = "whiteback";
			cSbNbArticleNonTransformeSurPeriode = "whiteback";
			cSbNbArticleNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//
//		case "nontransforme":
//			valueLignesFabrications = taFabricationService.findArticleNonTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			//listeCaPeriodeArticle = taFabricationService.findCAArticleSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleSurPeriode = (int) taFabricationService.countArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeSurPeriode = (int) taFabricationService.countArticleNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeARelancerSurPeriode = (int) taFabricationService.countArticleNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			cSbNbArticleSurPeriode = "whiteback";
//			cSbNbArticleTransformeSurPeriode = "whiteback";
//			cSbNbArticleNonTransformeSurPeriode = "redback";			
//			cSbNbArticleNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "transforme":
//			valueLignesFabrications = taFabricationService.findArticleTransfosDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			//listeCaPeriodeArticle = taFabricationService.findCAArticleSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleSurPeriode = (int) taFabricationService.countArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeSurPeriode = (int) taFabricationService.countArticleNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeARelancerSurPeriode = (int) taFabricationService.countArticleNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			cSbNbArticleSurPeriode = "whiteback";
//			cSbNbArticleTransformeSurPeriode = "redback";
//			cSbNbArticleNonTransformeSurPeriode = "whiteback";			
//			cSbNbArticleNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "nontransformearelancer":
//			valueLignesFabrications = taFabricationService.findArticleNonTransfosARelancerDTO(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			//listeCaPeriodeArticle = taFabricationService.findCAArticleSurPeriode(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleSurPeriode = (int) taFabricationService.countArticle(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeSurPeriode = (int) taFabricationService.countArticleNonTransforme(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin));
//			nbArticleNonTransformeARelancerSurPeriode = (int) taFabricationService.countArticleNonTransformeARelancer(LibDate.localDateToDate(dateDebut), LibDate.localDateToDate(dateFin), 15);
//			cSbNbArticleSurPeriode = "whiteback";
//			cSbNbArticleTransformeSurPeriode = "whiteback";
//			cSbNbArticleNonTransformeSurPeriode = "whiteback";			
//			cSbNbArticleNonTransformeARelancerSurPeriode = "redback";
//			break;
//			
//		default:
//			break;
//		}
//    	
//    }

//    public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal() {
//		
////    	List<Object> list = taFabricationService.findCAArticleSurPeriode(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise());
////    	List<DocumentChiffreAffaireDTO> list = taFabricationService.findChiffreAffaireTotal(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//    	List<DocumentChiffreAffaireDTO> list = taFabricationService.findChiffreAffaireTotalDTO(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//		return list;
//    	
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

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}


	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public boolean isTypePeriodePrecedente() {
		return typePeriodePrecedente;
	}

	public void setTypePeriodePrecedente(boolean typePeriodePrecedente) {
		this.typePeriodePrecedente = typePeriodePrecedente;
	}

	public String getCodeEtatArticle() {
		return codeEtatFabrication;
	}

	public void setCodeEtatArticle(String codeEtatFabrication) {
		this.codeEtatFabrication = codeEtatFabrication;
	}

	public List<TaFabricationDTO> getValueLignesFabrications() {
		return valueLignesFabrications;
	}

	public void setValueLignesFabrications(List<TaFabricationDTO> valueLignesFabrications) {
		this.valueLignesFabrications = valueLignesFabrications;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeArticle() {
		return listeCaPeriodeArticle;
	}

	public void setListeCaPeriodeArticle(List<DocumentChiffreAffaireDTO> listeCaPeriodeArticle) {
		this.listeCaPeriodeArticle = listeCaPeriodeArticle;
	}

	public int getNbArticleActif() {
		return nbFabricationActif;
	}

	public void setNbArticleActif(int nbFabricationActif) {
		this.nbFabricationActif = nbFabricationActif;
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


	public String getcSbNbArticleTransformeSurPeriode() {
		return cSbNbArticleTransformeSurPeriode;
	}


	public void setcSbNbArticleTransformeSurPeriode(String cSbNbArticleTransformeSurPeriode) {
		this.cSbNbArticleTransformeSurPeriode = cSbNbArticleTransformeSurPeriode;
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

}
