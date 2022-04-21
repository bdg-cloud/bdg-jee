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

import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.dossier.model.TaInfoEntreprise;

@Named
@ViewScoped
public class DashBoardReceptionController extends DashBoardDocumentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5979028746642381345L;
	/**
	 * 
	 */
	

	private @EJB ITaBonReceptionServiceRemote taBonReceptionService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	private TaInfoEntreprise infoEntreprise;
//    private List<MoisExoCourant> listeMoisExoCourant;
    private BarChartModel barChartModelBonReception;
    private List<DocumentChiffreAffaireDTO> listeCaMois;
    private Date dateDebut;
    private Date dateFin;
    private boolean typePeriodePrecedente;
    private String optionPeriodePrecedente;
    private String codeEtatBonReception;
    private List<TaBonReceptionDTO> valueLignesBonReception;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeBonReception;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeBonReceptionNonTransforme;
    private List<DocumentChiffreAffaireDTO> listeCaPeriodeBonReceptionNonTransformeARelancer;
    private int nbBonReceptionActif = 0;
    private int nbBonReceptionNonTransformeSurPeriode = 0 ;
    private int nbBonReceptionTransformeSurPeriode = 0;
    private int nbBonReceptionNonTransformeARelancerSurPeriode = 0;
    private BigDecimal tauxTransfoNb = BigDecimal.ZERO;
    private BigDecimal tauxTransfoCa = BigDecimal.ZERO;
    private BigDecimal MontantTotalHt = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfo = BigDecimal.ZERO;
    private BigDecimal MontantTotalHtNonTransfoARelancer = BigDecimal.ZERO;
    private String cSbNbBonReceptionSurPeriode;
    private String cSbNbBonReceptionTransformeSurPeriode;
    private String cSbNbBonReceptionNonTransformeSurPeriode;
    private String cSbNbBonReceptionNonTransformeARelancerSurPeriode;
    
    @PostConstruct
	public void postConstruct(){
    	infoEntreprise = taInfoEntrepriseService.findInstance();
    	dateDebut = infoEntreprise.getDatedebInfoEntreprise();
    	dateFin = infoEntreprise.getDatefinInfoEntreprise();
    	codeEtatBonReception = "tous";
    	//barChartModelBonReception = new BarChartModel();
    	//Initialisation des données sur la période exercice
    	actRechercheBonReception(codeEtatBonReception);
//    	createBarModel();
    }


    public void refresh(String etatBonReception){
    	actRechercheBonReception(etatBonReception);
    }

    public void refreshDashBoard(String etatDocument){

    }    
    public void actRechercheBonReception(String codeEtatBonReception){
    		nbBonReceptionActif = taBonReceptionService.selectCount();
////    	if (action != null) {
////    	codeEtatBonReception = (String) action.getComponent().getAttributes().get("codeetatBonReception");
////    	}
//    	switch (codeEtatBonReception) {
//		case "tous":
//			// Renvoi la liste des documents présents sur la période en cours
//			valueLignesBonReception = taBonReceptionService.findAllDTOPeriode(dateDebut, dateFin);
    		valueLignesBonReception = taBonReceptionService.findAllLight();
//			// Création et initialisation du graphique du CA HT / mois pour la période en cours
//			barChartModelBonReception = createBarModel(dateDebut, dateFin, barChartModelBonReception, TaBonReception.TYPE_DOC, 1 );
//			// Calcule les CA (HT, TVA, TTC) total sur la période
////			listeCaPeriodeBonReception = taBonReceptionService.findChiffreAffaireTotalDTO(dateDebut, dateFin);
//			listeCaPeriodeBonReception = findChiffreAffaireTotalDocument(TaBonReception.TYPE_DOC, dateDebut, dateFin);
//			// Calcule le CA (HT, TVA, TTC) total des BonReception non transformé sur la période
////			listeCaPeriodeBonReceptionNonTransforme = taBonReceptionService.findChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin);
//			listeCaPeriodeBonReceptionNonTransforme = findChiffreAffaireNonTransformeTotalDTODocument(TaBonReception.TYPE_DOC, dateDebut, dateFin);
//			// Calcule le CA (HT, TVA, TTC) total des BonReception non transformé sur la période
//			// La requête retourne un objet vide alors qu'il n'y a rien dans la table, à vérifier car cela génère une erreur lorsque
//			//l'on veut consulter les données ci-après.
//			listeCaPeriodeBonReceptionNonTransformeARelancer = taBonReceptionService.caBonReceptionNonTransfosARelancerDTO(dateDebut, dateFin, 15);
//			// Retourne le nombre total de documents sur la période
//			nbBonReceptionSurPeriode = (int) taBonReceptionService.countBonReception(dateDebut, dateFin);
//			// Retourne le nombre de BonReception non transformés sur la période
//			nbBonReceptionNonTransformeSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransforme(dateDebut, dateFin);
//			// Retourne le nombre de BonReception à relancer d'ici 15 jours (aujourd'hui d'ici la date d'échéance) sur la période
//			nbBonReceptionNonTransformeARelancerSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransformeARelancer(dateDebut, dateFin, 15);
//			
//			
//			
//			
//			if (nbBonReceptionSurPeriode > 0) {
//				try {
//					MontantTotalHt = listeCaPeriodeBonReception.get(0).getMtHtCalc();
//					nbBonReceptionTransformeSurPeriode = nbBonReceptionSurPeriode - nbBonReceptionNonTransformeSurPeriode;
//					tauxTransfoNb =  (new BigDecimal(nbBonReceptionTransformeSurPeriode) .divide(new BigDecimal(nbBonReceptionSurPeriode),MathContext.DECIMAL128)).setScale(2,BigDecimal.ROUND_HALF_UP) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).setScale(0,BigDecimal.ROUND_HALF_UP);
//					if ((listeCaPeriodeBonReceptionNonTransforme != null && ! listeCaPeriodeBonReceptionNonTransforme.isEmpty())) {
//						MontantTotalHtNonTransfo = listeCaPeriodeBonReceptionNonTransforme.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//					if ((listeCaPeriodeBonReception != null && ! listeCaPeriodeBonReception.isEmpty()) || (listeCaPeriodeBonReceptionNonTransforme != null && ! listeCaPeriodeBonReceptionNonTransforme.isEmpty())) {
//						MontantTotalHtTransfo = listeCaPeriodeBonReception.get(0).getMtHtCalc() .subtract(listeCaPeriodeBonReceptionNonTransforme.get(0).getMtHtCalc());
//						if (MontantTotalHtTransfo.intValue() > 0){
//						tauxTransfoCa =  (MontantTotalHtTransfo .divide(listeCaPeriodeBonReception.get(0).getMtHtCalc().setScale(0,BigDecimal.ROUND_HALF_UP),MathContext.DECIMAL128) .multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP)).setScale(0,BigDecimal.ROUND_HALF_UP);
//						}
//					}
//					if (listeCaPeriodeBonReceptionNonTransformeARelancer != null && ! listeCaPeriodeBonReceptionNonTransformeARelancer.isEmpty()) {
//						MontantTotalHtNonTransfoARelancer = listeCaPeriodeBonReceptionNonTransformeARelancer.get(0).getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					// TODO: handle exception
//				}
//				
//				
//			}
    		
    		initClasseCssBloc(CSS_DBOARD_BLOC_ACTIF_RECEPTION, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL, 
					  CSS_DBOARD_BLOC_INACTIF_ALL);
    		
    		
    		
//			// Initialisent les classes css pour les block d'information
//			cSbNbBonReceptionSurPeriode = "redback";
//			cSbNbBonReceptionTransformeSurPeriode = "whiteback";
//			cSbNbBonReceptionNonTransformeSurPeriode = "whiteback";
//			cSbNbBonReceptionNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//
//		case "nontransforme":
//			valueLignesBonReception = taBonReceptionService.findBonReceptionNonTransfosDTO(dateDebut, dateFin);
//			//listeCaPeriodeBonReception = taBonReceptionService.findCABonReceptionSurPeriode(dateDebut, dateFin);
//			nbBonReceptionSurPeriode = (int) taBonReceptionService.countBonReception(dateDebut, dateFin);
//			nbBonReceptionNonTransformeSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransforme(dateDebut, dateFin);
//			nbBonReceptionNonTransformeARelancerSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransformeARelancer(dateDebut, dateFin, 15);
//			cSbNbBonReceptionSurPeriode = "whiteback";
//			cSbNbBonReceptionTransformeSurPeriode = "whiteback";
//			cSbNbBonReceptionNonTransformeSurPeriode = "redback";			
//			cSbNbBonReceptionNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "transforme":
//			valueLignesBonReception = taBonReceptionService.findBonReceptionTransfosDTO(dateDebut, dateFin);
//			//listeCaPeriodeBonReception = taBonReceptionService.findCABonReceptionSurPeriode(dateDebut, dateFin);
//			nbBonReceptionSurPeriode = (int) taBonReceptionService.countBonReception(dateDebut, dateFin);
//			nbBonReceptionNonTransformeSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransforme(dateDebut, dateFin);
//			nbBonReceptionNonTransformeARelancerSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransformeARelancer(dateDebut, dateFin, 15);
//			cSbNbBonReceptionSurPeriode = "whiteback";
//			cSbNbBonReceptionTransformeSurPeriode = "redback";
//			cSbNbBonReceptionNonTransformeSurPeriode = "whiteback";			
//			cSbNbBonReceptionNonTransformeARelancerSurPeriode = "whiteback";
//			break;
//			
//		case "nontransformearelancer":
//			valueLignesBonReception = taBonReceptionService.findBonReceptionNonTransfosARelancerDTO(dateDebut, dateFin, 15);
//			//listeCaPeriodeBonReception = taBonReceptionService.findCABonReceptionSurPeriode(dateDebut, dateFin);
//			nbBonReceptionSurPeriode = (int) taBonReceptionService.countBonReception(dateDebut, dateFin);
//			nbBonReceptionNonTransformeSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransforme(dateDebut, dateFin);
//			nbBonReceptionNonTransformeARelancerSurPeriode = (int) taBonReceptionService.countBonReceptionNonTransformeARelancer(dateDebut, dateFin, 15);
//			cSbNbBonReceptionSurPeriode = "whiteback";
//			cSbNbBonReceptionTransformeSurPeriode = "whiteback";
//			cSbNbBonReceptionNonTransformeSurPeriode = "whiteback";			
//			cSbNbBonReceptionNonTransformeARelancerSurPeriode = "redback";
//			break;
//			
//		default:
//			break;
//		}
//    	
//    }

//    public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal() {
//		
////    	List<Object> list = taBonReceptionService.findCABonReceptionSurPeriode(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise());
////    	List<DocumentChiffreAffaireDTO> list = taBonReceptionService.findChiffreAffaireTotal(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//    	List<DocumentChiffreAffaireDTO> list = taBonReceptionService.findChiffreAffaireTotalDTO(infoEntreprise.getDatedebInfoEntreprise(), infoEntreprise.getDatefinInfoEntreprise(), 0);
//		return list;
//    	
    }
    
	public TaInfoEntreprise getInfoEntreprise() {
		return infoEntreprise;
	}
	public void setInfoEntreprise(TaInfoEntreprise infoEntreprise) {
		this.infoEntreprise = infoEntreprise;
	}
	public BarChartModel getBarChartModelBonReception() {
		return barChartModelBonReception;
	}
	public void setBarChartModelBonReceptionExo(BarChartModel barChartModelBonReception) {
		this.barChartModelBonReception = barChartModelBonReception;
	}
	
//	private BarChartModel initBarModelBonReceptionExo() {
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
//		barChartModelBonReception = initBarModelBonReceptionExo();
//         
//		barChartModelBonReception.setTitle("Chiffre d'affaire HT");
//		barChartModelBonReception.setLegendPosition("ne");
//		barChartModelBonReception.setAnimate(true);
//         
//        Axis xAxis = barChartModelBonReception.getAxis(AxisType.X);
////        xAxis.setLabel("Gender");
//         
//        Axis yAxis = barChartModelBonReception.getAxis(AxisType.Y);
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

	public String getCodeEtatBonReception() {
		return codeEtatBonReception;
	}

	public void setCodeEtatBonReception(String codeEtatBonReception) {
		this.codeEtatBonReception = codeEtatBonReception;
	}

	public List<TaBonReceptionDTO> getValueLignesBonReception() {
		return valueLignesBonReception;
	}

	public void setValueLignesBonReception(List<TaBonReceptionDTO> valueLignesBonReception) {
		this.valueLignesBonReception = valueLignesBonReception;
	}

	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeBonReception() {
		return listeCaPeriodeBonReception;
	}

	public void setListeCaPeriodeBonReception(List<DocumentChiffreAffaireDTO> listeCaPeriodeBonReception) {
		this.listeCaPeriodeBonReception = listeCaPeriodeBonReception;
	}

	public int getNbBonReceptionActif() {
		return nbBonReceptionActif;
	}

	public void setNbBonReceptionActif(int nbBonReceptionActif) {
		this.nbBonReceptionActif = nbBonReceptionActif;
	}

	public int getNbBonReceptionNonTransformeSurPeriode() {
		return nbBonReceptionNonTransformeSurPeriode;
	}

	public void setNbBonReceptionNonTransformeSurPeriode(int nbBonReceptionNonTransformeSurPeriode) {
		this.nbBonReceptionNonTransformeSurPeriode = nbBonReceptionNonTransformeSurPeriode;
	}


	public int getNbBonReceptionNonTransformeARelancerSurPeriode() {
		return nbBonReceptionNonTransformeARelancerSurPeriode;
	}


	public void setNbBonReceptionNonTransformeARelancerSurPeriode(int nbBonReceptionNonTransformeARelancerSurPeriode) {
		this.nbBonReceptionNonTransformeARelancerSurPeriode = nbBonReceptionNonTransformeARelancerSurPeriode;
	}


	public String getcSbNbBonReceptionSurPeriode() {
		return cSbNbBonReceptionSurPeriode;
	}


	public void setcSbNbBonReceptionSurPeriode(String cSbNbBonReceptionSurPeriode) {
		this.cSbNbBonReceptionSurPeriode = cSbNbBonReceptionSurPeriode;
	}


	public String getcSbNbBonReceptionNonTransformeSurPeriode() {
		return cSbNbBonReceptionNonTransformeSurPeriode;
	}


	public void setcSbNbBonReceptionNonTransformeSurPeriode(String cSbNbBonReceptionNonTransformeSurPeriode) {
		this.cSbNbBonReceptionNonTransformeSurPeriode = cSbNbBonReceptionNonTransformeSurPeriode;
	}


	public String getcSbNbBonReceptionNonTransformeARelancerSurPeriode() {
		return cSbNbBonReceptionNonTransformeARelancerSurPeriode;
	}


	public void setcSbNbBonReceptionNonTransformeARelancerSurPeriode(String cSbNbBonReceptionNonTransformeARelancerSurPeriode) {
		this.cSbNbBonReceptionNonTransformeARelancerSurPeriode = cSbNbBonReceptionNonTransformeARelancerSurPeriode;
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


	public List<DocumentChiffreAffaireDTO> getListeCaPeriodeBonReceptionNonTransforme() {
		return listeCaPeriodeBonReceptionNonTransforme;
	}


	public void setListeCaPeriodeBonReceptionNonTransforme(List<DocumentChiffreAffaireDTO> listeCaPeriodeBonReceptionNonTransforme) {
		this.listeCaPeriodeBonReceptionNonTransforme = listeCaPeriodeBonReceptionNonTransforme;
	}


	public BigDecimal getTauxTransfoCa() {
		return tauxTransfoCa;
	}


	public void setTauxTransfoCa(BigDecimal tauxTransfoCa) {
		this.tauxTransfoCa = tauxTransfoCa;
	}


	public int getNbBonReceptionTransformeSurPeriode() {
		return nbBonReceptionTransformeSurPeriode;
	}


	public void setNbBonReceptionTransformeSurPeriode(int nbBonReceptionTransformeSurPeriode) {
		this.nbBonReceptionTransformeSurPeriode = nbBonReceptionTransformeSurPeriode;
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


	public String getcSbNbBonReceptionTransformeSurPeriode() {
		return cSbNbBonReceptionTransformeSurPeriode;
	}


	public void setcSbNbBonReceptionTransformeSurPeriode(String cSbNbBonReceptionTransformeSurPeriode) {
		this.cSbNbBonReceptionTransformeSurPeriode = cSbNbBonReceptionTransformeSurPeriode;
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
