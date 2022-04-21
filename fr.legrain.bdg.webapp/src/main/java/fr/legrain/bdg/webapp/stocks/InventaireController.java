package fr.legrain.bdg.webapp.stocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaInventaireServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLInventaireServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaInventaireMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.stock.dto.EtatStocksDTO;
import fr.legrain.stock.dto.InventaireDTO;
import fr.legrain.stock.dto.LInventaireDTO;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaInventaire;
import fr.legrain.stock.model.TaLInventaire;
import fr.legrain.stock.model.TaMouvementStock;

@Named
@ViewScoped  
public class InventaireController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private String paramId;
	private static final String TITRE_FENETRE = "Inventaire";

	private List<InventaireDTO> values; 
	private List<lInventaireDTOJSF> valuesLigne;
	private List<lInventaireDTOJSF> valuesLigneSupprime;
	private List<LInventaireDTO> valuesLigneDTO;
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();

	private @EJB ITaInventaireServiceRemote taInventaireService;
	private @EJB ITaLInventaireServiceRemote taLInventaireService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaEntrepotServiceRemote taEntrepotService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaMouvementStockServiceRemote taMouvStockService;
	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;

	private InventaireDTO[] selectedInventaireDTOs; 
	private TaInventaire masterEntity = new TaInventaire();
	private InventaireDTO newInventaireDTO = new InventaireDTO();
	private InventaireDTO selectedInventaireDTO = new InventaireDTO();
	
	private lInventaireDTOJSF[] selectedlInventaireDTOJSFs; 
	private TaLInventaire masterEntityLigne = new TaLInventaire();
	private lInventaireDTOJSF newlInventaireDTOJSF = new lInventaireDTOJSF();
	private lInventaireDTOJSF selectedlInventaireDTOJSF = new lInventaireDTOJSF();
	
//	private TaEtat taEtat;
	private TaArticle taArticleLigne;
	private TaEntrepot taEntrepot;
	private TaLot taLot;

	private LgrDozerMapper<InventaireDTO,TaInventaire> mapperUIToModel  = new LgrDozerMapper<InventaireDTO,TaInventaire>();
	private TaInventaireMapper mapperModel  =new  TaInventaireMapper();

	private boolean insertAuto = true;
	private boolean cellEdit = false;
	
	public InventaireController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
	    
		refresh();
	}

	public void refresh(){
//		values = taInventaireService.selectAllDTO();
		values = taInventaireService.findAllLight();		
//		valuesLigne = IHMmodel();
//		valuesLigneDTO = IHMmodelDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void selectLignesMouvementsDansEntrepot(){
		if(modeEcran.getMode()==EnumModeObjet.C_MO_INSERTION){
			// on recalcul l'état des stock avant inventaire
			taEtatStockService.recalculEtatStock();
			int rang=1;
			//on rempli à partir des lignes d'étatStock
			List<EtatStocksDTO> liste=null;
			Boolean lotTermine = false;
			if(taEntrepot!=null && taEntrepot.getCodeEntrepot()!=null && !taEntrepot.getCodeEntrepot().equals("Tous"))
				liste=taEtatStockService.qteLotentrepotStockLight(taEntrepot.getCodeEntrepot(),null,lotTermine);
			else
				liste=taEtatStockService.findAllLight(lotTermine);
			for (EtatStocksDTO etatStocksDTO : liste) {
				
			}
//			masterEntity.removeAllLigneInsereAuto();
			if(valuesLigne==null)valuesLigne=new LinkedList<lInventaireDTOJSF>();
				valuesLigne.clear();
			for (EtatStocksDTO taEtatStock : liste) {
				lInventaireDTOJSF obj = new lInventaireDTOJSF();
				TaLot lot;
				if(taEtatStock.getNumLot()!=null)
					try {
//						lot =taLotService.findByCode(taEtatStock.getNumLot());
//						lot=taEtatStock.getTaLot();
//						obj.setTaLot(lot);
						obj.setDto(new LInventaireDTO());
						obj.getDto().setCodeFamille(taEtatStock.getCodeFamille());
						obj.getDto().setNumLot(taEtatStock.getNumLot());
						obj.getDto().setDluo(taEtatStock.getDluo());
						obj.getDto().setLotTermine(taEtatStock.getLotTermine());
						obj.getDto().setEmplacement(taEtatStock.getEmplacementOrg());
						obj.getDto().setLibelleLInventaire(taEtatStock.getLibelleEtatStock());
						obj.getDto().setQteReelle(taEtatStock.getQteRef());
						obj.getDto().setUn1Inventaire(taEtatStock.getUnRef());
						obj.getDto().setQteTheorique(taEtatStock.getQteRef());
						obj.getDto().setUn2Inventaire(taEtatStock.getUn2EtatStock());
						obj.getDto().setQte2LInventaire(taEtatStock.getQte2EtatStock());
						obj.getDto().setEcart(obj.getDto().getQteReelle().subtract(obj.getDto().getQteTheorique()));
//						obj.setTaArticle(taEtatStock.getTaArticle());
//						if(obj.getTaArticle()!=null)obj.getDto().setCodeArticle(obj.getTaArticle().getCodeArticle());
						obj.getDto().setCodeArticle(taEtatStock.getCodeLotArticle());
						
//						obj.setTaEntrepotOrg(taEntrepotService.findByCode(taEtatStock.getCodeEntrepotOrg()));
//						obj.setTaEntrepotOrg(taEtatStock.getTaEntrepot());						
//						if(obj.getTaEntrepotOrg()!=null)obj.getDto().setCodeEntrepot(obj.getTaEntrepotOrg().getCodeEntrepot());
						obj.getDto().setCodeEntrepot(taEtatStock.getCodeEntrepotOrg());
						obj.getDto().setId(rang);
						obj.getDto().setVersionObj(0);
						obj.getDto().setInsereAuto(true);
						obj.setModifie(true);
						valuesLigne.add(obj);
						rang++;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		//valuesLigne=IHMmodel();

	}
	
//	public String onFlowProcess(FlowEvent event) {
//
//	      String currentWizardStep = event.getOldStep();
//	      String nextWizardStep = event.getNewStep();
//	      try {
//	         if(currentWizardStep.endsWith("idEntete") && !nextWizardStep.endsWith("idEntete")){
//	        	 selectLignesMouvementsDansEntrepot();
//	         }
//	      } catch (Exception e) {
//	         FacesMessage msg = new FacesMessage(e.getMessage(), "Detail....");
//	         FacesContext.getCurrentInstance().addMessage(null, msg);
//	         nextWizardStep = currentWizardStep; // keep wizard on current step if error
//	      }
//	      
//	      return nextWizardStep; // return new step if all ok
//	}
	
	public void calculer() {

	      try {

	        	 selectLignesMouvementsDansEntrepot();

	      } catch (Exception e) {
	         FacesMessage msg = new FacesMessage(e.getMessage(), "Detail....");
	         FacesContext.getCurrentInstance().addMessage(null, msg);
	      }

	}
	
//	public List<lInventaireDTOJSF> IHMmodelLight() {
//		LinkedList<TaLInventaire> ldao = new LinkedList<TaLInventaire>();
//		LinkedList<lInventaireDTOJSF> lswt = new LinkedList<lInventaireDTOJSF>();
//		int rang=1;
//		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {
//			try {
//				for (TaLInventaire obj : masterEntity.getLignes()) {
//					if(!ldao.contains(obj))ldao.add(obj);
//				}
////				ldao.addAll(masterEntity.getLignes());
//
//				TaLInventaireMapper mapper = new TaLInventaireMapper();
//				LInventaireDTO dto = null;
//				for (TaLInventaire o : ldao) {
//					lInventaireDTOJSF t = new lInventaireDTOJSF();
//					dto = (LInventaireDTO) mapper.mapEntityToDto(o, new LInventaireDTO());
//					t.setDto(dto);
//					TaLot lot = o.getTaLot();
//					
//					if(o.getTaLot()!=null){
////						lot = taLotService.findByCode(o.getNumLot());
//						o.setNumLot(o.getTaLot().getNumLot());
//						t.setTaArticle(lot.getTaArticle());						
//					}
//					t.getDto().setId(o.getIdLInventaire());
//					t.setTaLot(lot);
//					t.setTaEntrepotOrg(o.getTaEntrepot());
//					lswt.add(t);
////					selectedlInventaireDTOJSF=t;
////					masterEntityLigne=o;
//				}
//				if(!lswt.isEmpty()){
//					selectedlInventaireDTOJSF=lswt.get(0);
//					if(selectedlInventaireDTOJSF.getDto().getId()!=0) masterEntityLigne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return lswt;
//	}
	
//	public List<lInventaireDTOJSF> IHMmodel() {
//		LinkedList<TaLInventaire> ldao = new LinkedList<TaLInventaire>();
//		LinkedList<lInventaireDTOJSF> lswt = new LinkedList<lInventaireDTOJSF>();
//		int rang=1;
//		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {
//			try {
//				for (TaLInventaire obj : masterEntity.getLignes()) {
//					if(!ldao.contains(obj))ldao.add(obj);
//				}
////				ldao.addAll(masterEntity.getLignes());
//
//				TaLInventaireMapper mapper = new TaLInventaireMapper();
//				LInventaireDTO dto = null;
//				for (TaLInventaire o : ldao) {
//					lInventaireDTOJSF t = new lInventaireDTOJSF();
//					dto = (LInventaireDTO) mapper.mapEntityToDto(o, new LInventaireDTO());
//					t.setDto(dto);
//					TaLot lot = o.getTaLot();
//					
//					if(o.getTaLot()!=null){
////						lot = taLotService.findByCode(o.getNumLot());
//						o.setNumLot(o.getTaLot().getNumLot());
////						t.setTaArticle(lot.getTaArticle());	
//						if(lot.getTaArticle()!=null)t.getDto().setCodeArticle(lot.getTaArticle().getCodeArticle());;
//					}
//					t.getDto().setId(o.getIdLInventaire());
//					t.setTaLot(lot);
//					t.setTaEntrepotOrg(o.getTaEntrepot());
//					lswt.add(t);
////					selectedlInventaireDTOJSF=t;
////					masterEntityLigne=o;
//				}
//				if(!lswt.isEmpty()){
//					selectedlInventaireDTOJSF=lswt.get(0);
//					if(selectedlInventaireDTOJSF.getDto().getId()!=0) masterEntityLigne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return lswt;
//	}
	
	public List<lInventaireDTOJSF> IHMmodel() {
		List<LInventaireDTO> ldao ;
		LinkedList<lInventaireDTOJSF> lswt = new LinkedList<lInventaireDTOJSF>();
		if(valuesLigne!=null)valuesLigne.clear();
		if(valuesLigneSupprime==null)
			valuesLigneSupprime=new LinkedList<lInventaireDTOJSF>();
		valuesLigneSupprime.clear();
		
		int rang=1;

//		if(selectedInventaireDTO!=null && !selectedInventaireDTO.getLignes().isEmpty()) {
			try {
				ldao= taLInventaireService.findAllLight(selectedInventaireDTO.getId());
				for (LInventaireDTO obj : ldao) {
					lInventaireDTOJSF t = new lInventaireDTOJSF();
					t.setDto(obj);
					t.getDto().setEcart(t.getDto().getQteReelle().subtract(t.getDto().getQteTheorique()));
					lswt.add(t);
				}

				if(!lswt.isEmpty()){
					selectedlInventaireDTOJSF=lswt.get(0);
//					if(selectedlInventaireDTOJSF.getDto().getId()!=0) masterEntityLigne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		return lswt;
	}

	

//	public List<LInventaireDTO> IHMmodelDTO() {
//		LinkedList<TaLInventaire> ldao = new LinkedList<TaLInventaire>();
//		LinkedList<LInventaireDTO> lswt = new LinkedList<LInventaireDTO>();
//		
//		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {
//
//			ldao.addAll(masterEntity.getLignes());
//
//			LgrDozerMapper<TaLInventaire,LInventaireDTO> mapper = new LgrDozerMapper<TaLInventaire,LInventaireDTO>();
//			for (TaLInventaire o : ldao) {
//				LInventaireDTO t = null;
//				t = (LInventaireDTO) mapper.map(o, LInventaireDTO.class);
//				lswt.add(t);
//			}
//			
//		}
//		return lswt;
//	}
	
	public void changeEtatLigne(lInventaireDTOJSF ligneDTOJSF) {
		System.out.println("InventaireController.changeEtatLigne() : "+ligneDTOJSF.getDto().getCodeArticle());
		//modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		selectedlInventaireDTOJSF = ligneDTOJSF;
		actEnregistrerLigne(null);
	}
	
	public List<InventaireDTO> getValues(){  
		return values;
	}
	
	public void actAutoInsererLigne(ActionEvent actionEvent) {
		boolean existeDeja=false;
		if(insertAuto) {
			for (lInventaireDTOJSF ligne : valuesLigne) {
				if(ligne.ligneEstVide())existeDeja=true;
			}
			if(!existeDeja){			
			actInsererLigne(actionEvent);
			}
		}
	}
	
	public void actInsererLigne(ActionEvent actionEvent) {
		
//		if(selectedlInventaireDTOJSF==null || !selectedlInventaireDTOJSF.isAutoInsert()) {
			lInventaireDTOJSF nouvelleLigne = new lInventaireDTOJSF();
			nouvelleLigne.setDto(new LInventaireDTO());
			nouvelleLigne.getDto().setId(valuesLigne.size()+1);
			nouvelleLigne.getDto().setNumLot(selectedInventaireDTO.getCodeInventaire()+"_"+valuesLigne.size()+1);
//			nouvelleLigne.setAutoInsert(true);
			newlInventaireDTOJSF=nouvelleLigne;
			selectedlInventaireDTOJSF=nouvelleLigne;
			masterEntityLigne = new TaLInventaire(); 
			try {
			
				List<lInventaireDTOJSF> modelListeLigneDevis = IHMmodel();

//				masterEntity.addLigneMouvement(masterEntityLigne);
			} catch (Exception e) {
				e.printStackTrace();
			}

//			masterEntityLigne.setTaInventaire(masterEntity);

			valuesLigne.add(nouvelleLigne);

			modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
			//		}

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actInsererLigne")); 
			}
		}
	
	public void actEnregistrerLigne(ActionEvent actionEvent) {
		
		try {
			selectedlInventaireDTOJSF.updateDTO();
			selectedlInventaireDTOJSF.setModifie(true);
//			LgrDozerMapper<LInventaireDTO,TaLInventaire> mapper = new LgrDozerMapper<LInventaireDTO,TaLInventaire>();
//			mapper.map((LInventaireDTO) selectedlInventaireDTOJSF.getDto(),masterEntityLigne);
			selectedlInventaireDTOJSF.getDto().setEcart(selectedlInventaireDTOJSF.getDto().
					getQteReelle().subtract(selectedlInventaireDTOJSF.getDto().getQteTheorique()));
//			if(selectedlInventaireDTOJSF.getTaLot()!=null){
//				masterEntityLigne.setTaLot(selectedlInventaireDTOJSF.getTaLot());
//				masterEntityLigne.setNumLot(selectedlInventaireDTOJSF.getTaLot().getNumLot());
//				masterEntityLigne.getTaLot().setTaArticle(selectedlInventaireDTOJSF.getTaArticle());
//			}
//			masterEntityLigne.setQteReelle(selectedlInventaireDTOJSF.getDto().getQteReelle());
//			masterEntityLigne.setQteTheorique(selectedlInventaireDTOJSF.getDto().getQteTheorique());
//			if(!masterEntity.getLignes().contains(masterEntityLigne))
//				masterEntity.addLigneMouvement(masterEntityLigne);		
			modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actEnregisterLigne")); 
		}
	}
	
	public void actAnnulerLigne(ActionEvent actionEvent) {
		insertAuto = false;
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actAnnulerLigne")); 
		}
	}
	
	public void actModifierLigne(ActionEvent actionEvent) {
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actModifierLigne")); 
		}
	}
	
	public TaLInventaire rechercheLigne(int id){
		TaLInventaire lInventaire=null;
		if(masterEntity!=null){
			for (TaLInventaire ligne : masterEntity.getLignes()) {
				if(ligne.getIdLInventaire()==id)
					lInventaire=ligne;
			}
		}
		return lInventaire;
	}
	
	public void actSupprimerLigne() {
		actSupprimerLigne(null);
	}
	
	public void actSupprimerLigne(ActionEvent actionEvent) {

		selectedlInventaireDTOJSF.setSupprime(true);
		valuesLigneSupprime.add(selectedlInventaireDTOJSF);
		valuesLigne.remove(selectedlInventaireDTOJSF);
//		TaLInventaire ligne = null;
//		try {
//			
//			if(!selectedlInventaireDTOJSF.getDto().isInsereAuto())
//				ligne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
////				ligne=taLInventaireService.findById(selectedlInventaireDTOJSF.getDto().getId());
//			if(ligne!=null){
//				TaMouvementStock mouv= ligne.getTaMouvementStock();
//				
//				if(mouv!=null){
//					ligne.setTaMouvementStock(null);
//					mouv.setTaGrMouvStock(null);
//				}
//				masterEntity.getLignes().remove(ligne);
//				ligne.setTaInventaire(null);
//			}
//			
//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actSupprimerLigne")); 
		}
	}
	
	public void onCellEdit(CellEditEvent  event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		selectedlInventaireDTOJSF=getValuesLigne().get(event.getRowIndex());

		if(newValue != null && !newValue.equals(oldValue)) {
			try {
				selectedlInventaireDTOJSF.updateDTO();
//				masterEntityLigne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
//				masterEntityLigne.setQteReelle((BigDecimal)newValue);
				actEnregistrerLigne(null);
				cellEdit=false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public void onRowEdit(RowEditEvent event) {

//		if(newValue != null && !newValue.equals(oldValue)) {
			try {
				selectedlInventaireDTOJSF.updateDTO();
//				masterEntityLigne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
//				masterEntityLigne.setQteReelle(selectedlInventaireDTOJSF.getDto().getQteReelle());
				actEnregistrerLigne(null);
				cellEdit=false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
//		try {
//			selectedlInventaireDTOJSF.updateDTO();
//			if(selectedlInventaireDTOJSF.getDto().getId()!=null &&  selectedlInventaireDTOJSF.getDto().getId()!=0)
//			masterEntityLigne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
//			//actEnregistrerLigne(null);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
}

	public void onRowCancel(RowEditEvent event) {
		
		actAnnulerLigne(null);
    }
	
	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedInventaireDTO.getCodeInventaire()!=null) {
					taInventaireService.annuleCode(selectedInventaireDTO.getCodeInventaire());
				}
				masterEntity=new TaInventaire();
				
				if(values.size()>0)	selectedInventaireDTO = values.get(0);
				else selectedInventaireDTO=new InventaireDTO();
				onRowSelect(null);
				
				break;
			case C_MO_EDITION:
//				if(selectedInventaireDTO.getId()!=null && selectedInventaireDTO.getId()!=0){
//					masterEntity = taInventaireService.findById(selectedInventaireDTO.getId());
//					}				
				break;

			default:
				break;
			}
//			mapperModel.mapEntityToDto(masterEntity, selectedInventaireDTO);
			valuesLigne= IHMmodel();
			
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actAnnuler")); 
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void autoCompleteMapUIToDTO() {
		
		if(taEntrepot!=null) {
			validateUIField(Const.C_CODE_ENTREPOT,taEntrepot.getCodeEntrepot());
			selectedInventaireDTO.setCodeEntrepot(taEntrepot.getCodeEntrepot());
			selectedInventaireDTO.setIdEntrepot(taEntrepot.getIdEntrepot());
		}
//		masterEntity.setTaEntrepot(taEntrepot);
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taEntrepot = null;
			
			if(selectedInventaireDTO.getCodeEntrepot()!=null && !selectedInventaireDTO.getCodeEntrepot().equals("")) {
				taEntrepot = taEntrepotService.findByCode(selectedInventaireDTO.getCodeEntrepot());
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		

		try {
			autoCompleteMapUIToDTO();
			System.out.println("début récup masterEntity "+ LibDate.getHeure(new Date())+"-"+LibDate.getMinute(new Date())+"-"+LibDate.getSeconde(new Date()));
			if(selectedInventaireDTO.getId()!=null && selectedInventaireDTO.getId()!=0) {
				masterEntity=taInventaireService.findById(selectedInventaireDTO.getId());
			}
			System.out.println("Après récup masterEntity "+ LibDate.getHeure(new Date())+"-"+LibDate.getMinute(new Date())+"-"+LibDate.getSeconde(new Date()));
			mapperModel.mapDtoToEntity(selectedInventaireDTO, masterEntity);
			System.out.println("Après mapping masterEntity "+ LibDate.getHeure(new Date())+"-"+LibDate.getMinute(new Date())+"-"+LibDate.getSeconde(new Date()));
			
			//			masterEntity.getLignes().clear();
			for (lInventaireDTOJSF obj : valuesLigneSupprime) {
				TaLInventaire ligne= null;
				if(obj.isSupprime()&& obj.getDto().isInsereAuto()==false){
					System.out.println("ligne supprimée :"+obj.getDto().getId());
					if(!obj.getDto().isInsereAuto()){
						ligne=rechercheLigne(obj.getDto().getId());
						masterEntity.removeLigneMouvement(ligne);
					}						
				}
			}
			for (lInventaireDTOJSF obj : valuesLigne) {
				TaLInventaire ligne= null;
				if(obj.isModifie()){
					System.out.println("ligne modifiée :"+obj.getDto().getId());
					taEntrepot=null;
					taLot=null;
					if(!obj.getDto().isInsereAuto()){
						ligne=rechercheLigne(obj.getDto().getId());
					}
					if(ligne==null){
						ligne=new TaLInventaire();
					}
					LgrDozerMapper<LInventaireDTO,TaLInventaire> mapper = new LgrDozerMapper<LInventaireDTO,TaLInventaire>();
					mapper.map(obj.getDto(),ligne);
					ligne.setModifie(true);
					//				ligne.setIdLInventaire(obj.getDto().getId());
					obj.getDto().setEcart(obj.getDto().getQteReelle().subtract(obj.getDto().getQteTheorique()));
					if(obj.getDto().getNumLot()!=null && !obj.getDto().getNumLot().equals("")) {
						taLot = taLotService.findByCode(obj.getDto().getNumLot());
					}
					if(taLot!=null){
						ligne.setTaLot(taLot);
						ligne.setNumLot(taLot.getNumLot());
						//					ligne.getTaLot().setTaArticle(lot.getTaArticle());
					}
					if(obj.getDto().getCodeEntrepot()!=null && !obj.getDto().getCodeEntrepot().equals("")) {
						taEntrepot = taEntrepotService.findByCode(obj.getDto().getCodeEntrepot());
					}
					ligne.setTaEntrepot(taEntrepot);				

					ligne.setLibelleInventaire(obj.getDto().getLibelleLInventaire());
					ligne.setQteReelle(obj.getDto().getQteReelle());
					ligne.setQteTheorique(obj.getDto().getQteTheorique());
					ligne.setTaInventaire(masterEntity);
					ligne.setVersionObj(obj.getDto().getVersionObj());
					if(!masterEntity.getLignes().contains(ligne)) {
						masterEntity.addLigneMouvement(ligne);
					}
				}	
			}

			System.out.println("Après parcours ligne "+ LibDate.getHeure(new Date())+"-"+LibDate.getMinute(new Date())+"-"+LibDate.getSeconde(new Date()));
			/*
			 * TODO Gérer les mouvements corrrespondant aux lignes 
			 */
			TaGrMouvStock grpMouvStock = new TaGrMouvStock();
			if(masterEntity.getTaGrMouvStock()!=null)grpMouvStock=masterEntity.getTaGrMouvStock();
			grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeInventaire());
			grpMouvStock.setDateGrMouvStock(masterEntity.getDateInventaire());
			grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleInventaire());
			grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("I"));
			for (TaLInventaire ligne : masterEntity.getLignes()) {	
				if(ligne.isModifie()){
					if(ligne.getQteReelle()!=null && ligne.getQteTheorique()!=null 
							&& ligne.getQteReelle().subtract(ligne.getQteTheorique()).signum()!=0){
						TaMouvementStock m = new TaMouvementStock();
						if(ligne.getTaMouvementStock()!=null) {
							m=ligne.getTaMouvementStock();
						}
						m.setLibelleStock(ligne.getLibelleInventaire());
						m.setDateStock(masterEntity.getDateInventaire());
						m.setEmplacement(ligne.getEmplacement());
						m.setTaEntrepot(ligne.getTaEntrepot());
						//										TaLot lot =taLotService.findByCode(ligne.getNumLot());
						//										m.setTaLot(ligne.getTaLot());
						if(ligne.getTaLot()!=null)
							m.setTaLot(ligne.getTaLot());
						m.setQte1Stock(ligne.getQteReelle().subtract(ligne.getQteTheorique()));
						m.setQte2Stock(ligne.getQte2LInventaire());
						m.setUn1Stock(ligne.getUn1Inventaire());
						m.setUn2Stock(ligne.getUn2Inventaire());
						m.setTaGrMouvStock(grpMouvStock);

						ligne.setTaMouvementStock(m);

						grpMouvStock.getTaMouvementStockes().add(m);
					}else{
						if(ligne.getTaMouvementStock()!=null){
							grpMouvStock.getTaMouvementStockes().remove(ligne.getTaMouvementStock());
							ligne.setTaMouvementStock(null);
						}
					}
				}
			}
			//			}
			masterEntity.setTaGrMouvStock(grpMouvStock);
			grpMouvStock.setManuel(false);
			grpMouvStock.setTaInventaire(masterEntity);
			if(grpMouvStock.getTaMouvementStockes().isEmpty()) {
				masterEntity.setTaGrMouvStock(null);
				grpMouvStock.setTaInventaire(null);
				grpMouvStock=null;
			}

			System.out.println("Après parcours mouvement "+ LibDate.getHeure(new Date())+"-"+LibDate.getMinute(new Date())+"-"+LibDate.getSeconde(new Date()));

			masterEntity = taInventaireService.merge(masterEntity,ITaInventaireServiceRemote.validationContext);
			
			System.out.println("Après merge "+ LibDate.getHeure(new Date())+"-"+LibDate.getMinute(new Date())+"-"+LibDate.getSeconde(new Date()));

			mapperModel.mapEntityToDto(masterEntity, selectedInventaireDTO);
			
			System.out.println("Après dernier mapping "+ LibDate.getHeure(new Date())+"-"+LibDate.getMinute(new Date())+"-"+LibDate.getSeconde(new Date()));
//			autoCompleteMapDTOtoUI();

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedInventaireDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			updateTab();

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, TITRE_FENETRE, e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actEnregistrer")); 
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			if(valuesLigne!=null)valuesLigne.clear();
					
			selectedInventaireDTO = new InventaireDTO();
			selectedlInventaireDTOJSF = new lInventaireDTOJSF();
			masterEntity = new TaInventaire();
			
			if(valuesLigneSupprime==null)
				valuesLigneSupprime=new LinkedList<lInventaireDTOJSF>();
			valuesLigneSupprime.clear();
			autoCompleteMapDTOtoUI();

//			selectedInventaireDTO.setCodeInventaire(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
			selectedInventaireDTO.setCodeInventaire(taInventaireService.genereCode(null));
			selectedInventaireDTO.setDateInventaire(new Date());
			selectedInventaireDTO.setCodeEntrepot("Tous");
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actInserer")); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
//	public void actModifier() {
//		actModifier(null);
//	}

	public void actModifier(ActionEvent actionEvent) {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actModifier")); 
		}
	}
	
	public void actAide(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        Map<String,List<String>> params = null;
        PrimeFaces.current().dialog().openDynamic("aide", options, params);
		
        if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actAide")); 
        }
	}
	
//	public void actAideRetour(ActionEvent actionEvent) {
//		
//	}
	
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_INVENTAIRE);
		b.setTitre("Inventaire");
		b.setTemplate("stock/inventaire.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MOUVEMENT_INVENTAIRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_INVENTAIRE);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
	    	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage(TITRE_FENETRE, 
		    		"Nouveau"
		    		)); 
		}
    } 
    
    public void supprimer(ActionEvent actionEvent) {
    	actSupprimer(actionEvent);
    }
    
    public void detail() {
		detail(null);
	}
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }
    
    public void supprimer() {
		actSupprimer(null);
	}
    
    public void actSupprimer() {
		actSupprimer(null);
	}

	public void actSupprimer(ActionEvent actionEvent) {
		TaInventaire TaInventaire = new TaInventaire();
		try {
			if(selectedInventaireDTO!=null && selectedInventaireDTO.getId()!=null){
				TaInventaire = taInventaireService.findById(selectedInventaireDTO.getId());
			}

			taInventaireService.remove(TaInventaire);
			values.remove(selectedInventaireDTO);
//			masterEntity=new TaInventaire();
			
			if(!values.isEmpty()) {
				selectedInventaireDTO = values.get(0);
//				masterEntity=taInventaireService.findById(selectedInventaireDTO.getId());
			}else{
				selectedInventaireDTO=new InventaireDTO();
			}
			updateTab();

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Inventaire", e.getMessage()));
		}	    
	}

	public void actFermer(ActionEvent actionEvent) {
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			actAnnuler(actionEvent);
			break;
		case C_MO_EDITION:
			actAnnuler(actionEvent);							
			break;

		default:
			break;
		}
		
	
		selectedInventaireDTO=new InventaireDTO();
		selectedInventaireDTOs=new InventaireDTO[]{selectedInventaireDTO};
		updateTab();
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeInventaire').filter()");
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actImprimer")); 
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		TaInventaire in = taInventaireService.findById(selectedInventaireDTO.getId());
		sessionMap.put("inventaire", in);
		
//		SortedSet.sort(, new LigneInventaireComparator());
		sessionMap.put("listeInventaire", in.getLignes());
		
		System.out.println("inventaire : "+in);
		// - Dima - Début
		
//		for(TaLInventaire lin : in.getLignes()){
//			String s = lin.getQuandCree().toLocaleString();
//			System.out.println("****************************");
//			System.out.println("Test Date : "+s);
//			System.out.println("****************************");
//			
//			lin.getTaLot().getTaArticle().getMatierePremiere();
//			lin.getTaLot().getTaArticle().getProduitFini();
//			lin.getTaLot().getNumLot();
//			
//		}
		
		// - Dima -  Fin
		
//			session.setAttribute("tiers", taTiersService.findById(selectedInventaireDTO.getId()));
			System.out.println("MouvementStockController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
	
	public void actImprimerInventaire(ActionEvent actionEvent) {
		
		try {
					
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		sessionMap.remove("inventaire");
		sessionMap.remove("listeDesInventaires");
		
		if(selectedInventaireDTO.getId()!=null) {
		TaInventaire in = taInventaireService.findById(selectedInventaireDTO.getId());
		sessionMap.put("inventaire", in);
		}
		sessionMap.put("listeDesInventaires", valuesLigne);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public void actImprimerListeDesInventaires(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesInventaires", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_INVENTAIRE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	

	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			onRowSelect(event);
			
			autoCompleteMapDTOtoUI();
			valuesLigne = IHMmodel();
			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(TITRE_FENETRE, 
						"Chargement du groupe de mouvement N°"+selectedInventaireDTO.getCodeInventaire()
						)); 
			}
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_MOUVEMENT_INVENTAIRE);
		}
	} 
	
	public void onRowSelectLigne(SelectEvent event) {
		selectionLigne((lInventaireDTOJSF) event.getObject());
		cellEdit=true;
	}
	
	public void selectionLigne(lInventaireDTOJSF ligne){
		selectedlInventaireDTOJSF=ligne;
//		if(masterEntity.getIdInventaire()!=0 && selectedlInventaireDTOJSF.getDto().getId()!=null)
//			masterEntityLigne=rechercheLigne(selectedlInventaireDTOJSF.getDto().getId());
	}
	
	public void updateTab(){
		try {
			
			autoCompleteMapDTOtoUI();
			if(selectedInventaireDTOs!=null && selectedInventaireDTOs.length>0) {
				selectedInventaireDTO = selectedInventaireDTOs[0];
			}
			if(selectedInventaireDTO.getId()!=null && selectedInventaireDTO.getId()!=0) {
				selectedInventaireDTO = taInventaireService.findByCodeLight(selectedInventaireDTO.getCodeInventaire());
			}

			valuesLigne = IHMmodel();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(TITRE_FENETRE, 
						"Chargement du groupe de mouvement N°"+selectedInventaireDTO.getCodeInventaire()
						)); 
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_INVENTAIRE);
		b.setTitre("Inventaire");
		b.setTemplate("stock/inventaire.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MOUVEMENT_INVENTAIRE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_INVENTAIRE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		updateTab();
		scrollToTop();

	} 

	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	

	
	public void actDialogArticles(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_article", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actAbout")); 	    
	}
	
	public void handleReturnDialogArticles(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taArticleLigne = (TaArticle) event.getObject();
		}
	}
	

	
	public void actDialogEntrepot(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_entrepot", options, params);
		    
	}
	
	public void handleReturnDialogEntrepot(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taEntrepot = (TaEntrepot) event.getObject();
		}
	}
	
	 public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
				case "annuler":
				case "enregistrer":
				case "fermer":
				case "calculer":	
					retour = false;
					break;									
				default:
					break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;										
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "inserer":
			case "fermer":
				retour = false;
				break;	
			case "supprimerListe":retour = false;break;	
			case "supprimer":
			case "modifier":
			case "imprimer":
				if(selectedInventaireDTO!=null && selectedInventaireDTO.getId()!=null  && selectedInventaireDTO.getId()!=0 ) retour = false;
				break;				
			default:
				break;
		}
			break;
		default:
			break;
		}
		
		return retour;

	}
	 
		public boolean etatBoutonLigne(String bouton) {
			boolean retour = true;
			switch (modeEcranLigne.getMode()) {
			case C_MO_INSERTION:
				switch (bouton) {
				case "annuler":
				case "enregistrer":
				case "fermer":
					retour = false;
					break;
				case "rowEditor":
					retour = modeEcran.dataSetEnModif()?true:false;
					break;					
				default:
					break;
				}
				break;
			case C_MO_EDITION:
				switch (bouton) {
				case "annuler":
				case "enregistrer":
				case "fermer":
					retour = false;
					break;
				case "rowEditor":
					retour = modeEcran.dataSetEnModif()?true:false;
					break;
				default:
					break;
				}
				break;
			case C_MO_CONSULTATION:
				switch (bouton) {
				case "supprimer":
				case "modifier":
				case "inserer":
				case "imprimer":
				case "fermer":
					retour = false;
					break;
				case "rowEditor":
					retour = modeEcran.dataSetEnModif()?true:false;
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}

			return retour;

		}

	public InventaireDTO[] getSelectedInventaireDTOs() {
		return selectedInventaireDTOs;
	}

	public void setSelectedInventaireDTOs(InventaireDTO[] selectedInventaireDTOs) {
		this.selectedInventaireDTOs = selectedInventaireDTOs;
	}

	public InventaireDTO getNewInventaireDTO() {
		return newInventaireDTO;
	}

	public void setNewInventaireDTO(InventaireDTO newInventaireDTO) {
		this.newInventaireDTO = newInventaireDTO;
	}

	public InventaireDTO getSelectedInventaireDTO() {
		return selectedInventaireDTO;
	}

	public void setSelectedInventaireDTO(InventaireDTO selectedInventaireDTO) {
		this.selectedInventaireDTO = selectedInventaireDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	
	public void validateMouvement(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
//		String msg = "";
//		
//		try {
//		 
//		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
//		  
//		  
//		  validateUIField(nomChamp,value);
//		  taInventaireService.validateDTOProperty(selectedInventaireDTO, nomChamp, ITaInventaireServiceRemote.validationContext );
//		  
//
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<InventaireDTO>> violations = factory.getValidator().validateValue(InventaireDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<InventaireDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	
	public void validateLigneMouvement(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
//		String msg = "";
//		
//		try {
//		 
//		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
//		  
//		  
//		  validateUIField(nomChamp,value);
//		  taLInventaireService.validateDTOProperty(selectedlInventaireDTOJSF.getDto(), nomChamp, ITaLInventaireServiceRemote.validationContext );
//		  
//
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<LInventaireDTO>> violations = factory.getValidator().validateValue(LInventaireDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<LInventaireDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		validateUIField(nomChamp,value);
//		if(nomChamp.equals(Const.C_CODE_ENTREPOT)){
//			selectLignesMouvementsDansEntrepot();
//		}
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;

//		try {				
//			 if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
//				TaArticle entity = new TaArticle();
//				entity = taArticleService.findByCode((String)value);
//				selectedlInventaireDTOJSF.getDto().setCodeArticle(entity.getCodeArticle());
//				selectedlInventaireDTOJSF.getDto().setLibelleLInventaire(entity.getLibellelArticle());
//			}				
//			return false;
//
//		} catch (Exception e) {
//			
//		}
//		return false;
//	}
		try {				
			 if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = new TaArticle();
				if(value instanceof TaArticle)entity = (TaArticle)value;
				else
				entity = taArticleService.findByCode((String)value);
				selectedlInventaireDTOJSF.getDto().setCodeArticle(entity.getCodeArticle());
				selectedlInventaireDTOJSF.getDto().setLibelleLInventaire(entity.getLibellecArticle());
				if(entity.getTaPrix()!=null){
					if(entity.getTaUnite1()!=null)
						selectedlInventaireDTOJSF.getDto().setUn1Inventaire(entity.getTaUnite1().getCodeUnite());
				}
			}	else
				 if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
						TaEntrepot entity = new TaEntrepot();
						if(value instanceof TaEntrepot)entity = (TaEntrepot)value;
						else
						entity = taEntrepotService.findByCode((String)value);
						selectedlInventaireDTOJSF.getDto().setCodeEntrepot(entity.getCodeEntrepot());
						selectedlInventaireDTOJSF.getDto().setIdEntrepot(entity.getIdEntrepot());
					} 

//
			return true;

		} catch (Exception e) {
		
	}
	return false;
}	
	
	
	public List<TaLot> lotAutoComplete(String query) {
		List<TaLot> filteredValues = new ArrayList<TaLot>();
		if(selectedlInventaireDTOJSF!=null && selectedlInventaireDTOJSF.getTaArticle()!=null){
			List<TaLot> allValues = taLotService.lotsArticle(selectedlInventaireDTOJSF.getTaArticle().getCodeArticle(),null,null,false);

			if(allValues!=null){
				for (int i = 0; i < allValues.size(); i++) {
					TaLot obj = allValues.get(i);
					if(query.equals("*") || obj.getNumLot().toLowerCase().contains(query.toLowerCase())) {
						filteredValues.add(obj);
					}
				}
			}
		}
		return filteredValues;
	}
	

	
	public List<TaArticle> articleAutoComplete(String query) {
        List<TaArticle> allValues = taArticleService.selectAll();
        List<TaArticle> filteredValues = new ArrayList<TaArticle>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaArticle obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaEntrepot> entrepotAutoComplete(String query) {
        List<TaEntrepot> allValues = taEntrepotService.selectAll();
        TaEntrepot entrepotTous=new TaEntrepot();
        entrepotTous.setCodeEntrepot("Tous");
        entrepotTous.setLibelle("Inventaire global");
        allValues.add(0,entrepotTous);
        List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaEntrepot obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
        return filteredValues;
    }
	
	public List<String> emplacementAutoCompleteOrg(String query) {
		List<String> filteredValues = new ArrayList<String>();
		if(selectedlInventaireDTOJSF!=null && selectedlInventaireDTOJSF.getTaEntrepotOrg()!=null) {
			List<String> allValues = taEtatStockService.emplacementLotArticle(selectedlInventaireDTOJSF.getTaArticle().getCodeArticle(),
					selectedlInventaireDTOJSF.getDto().getNumLot(),query,false);


			for (int i = 0; i < allValues.size(); i++) {
				String civ = allValues.get(i);
				if(query.equals("*") || civ.toLowerCase().contains(query.toLowerCase())) {
					filteredValues.add(civ);
				}
			}
		}
		return null;

		//        return filteredValues;
	}
	

	public List<lInventaireDTOJSF> getValuesLigne() {
		return valuesLigne;
	}

	public lInventaireDTOJSF[] getSelectedlInventaireDTOJSFs() {
		return selectedlInventaireDTOJSFs;
	}

	public void setSelectedlInventaireDTOJSFs(
			lInventaireDTOJSF[] selectedlInventaireDTOJSFs) {
		this.selectedlInventaireDTOJSFs = selectedlInventaireDTOJSFs;
	}

	public lInventaireDTOJSF getNewlInventaireDTOJSF() {
		return newlInventaireDTOJSF;
	}

	public void setNewlInventaireDTOJSF(lInventaireDTOJSF newlInventaireDTOJSF) {
		this.newlInventaireDTOJSF = newlInventaireDTOJSF;
	}

	public lInventaireDTOJSF getSelectedlInventaireDTOJSF() {
		return selectedlInventaireDTOJSF;
	}

	public void setSelectedlInventaireDTOJSF(
			lInventaireDTOJSF selectedlInventaireDTOJSF) {
		this.selectedlInventaireDTOJSF = selectedlInventaireDTOJSF;
	}

	public TaArticle getTaArticleLigne() {
		return taArticleLigne;
	}

	public void setTaArticleLigne(TaArticle taArticleLigne) {
		this.taArticleLigne = taArticleLigne;
	}


	public TaEntrepot getTaEntrepot() {
		return taEntrepot;
	}

	public void setTaEntrepot(TaEntrepot taEntrepotOrgLigne) {
		this.taEntrepot = taEntrepotOrgLigne;
	}


	public List<LInventaireDTO> getValuesLigneDTO() {
		return valuesLigneDTO;
	}

	public void setValuesLigneDTO(List<LInventaireDTO> valuesLigneDTO) {
		this.valuesLigneDTO = valuesLigneDTO;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public ModeObjetEcranServeur getModeEcranLigne() {
		return modeEcranLigne;
	}

	public void setModeEcranLigne(ModeObjetEcranServeur modeEcranLigne) {
		this.modeEcranLigne = modeEcranLigne;
	}


	public boolean renderingCellEdit(){
		return cellEdit;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}

	public List<lInventaireDTOJSF> getValuesLigneSupprime() {
		return valuesLigneSupprime;
	}

	public void setValuesLigneSupprime(List<lInventaireDTOJSF> valuesLigneSupprime) {
		this.valuesLigneSupprime = valuesLigneSupprime;
	}
	
	

	
}  
