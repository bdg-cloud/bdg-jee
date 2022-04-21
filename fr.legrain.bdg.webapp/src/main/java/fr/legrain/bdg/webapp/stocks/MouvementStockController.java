package fr.legrain.bdg.webapp.stocks;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.remotecommand.RemoteCommand;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTypeMouvement;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaGrMouvStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.article.titretransport.service.remote.ITaTitreTransportServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaGrMouvStockMapper;
import fr.legrain.bdg.model.mapping.mapper.TaMouvementStockMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.stock.dto.GrMouvStockDTO;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;

@Named
@ViewScoped  
public class MouvementStockController extends AbstractController implements Serializable {  

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	private String paramId;
	private static final String TITRE_FENETRE = "Mouvements de stock";

	private List<GrMouvStockDTO> values; 
	private List<TaMouvementStockDTOJSF> valuesLigne;
	private List<MouvementStocksDTO> valuesLigneDTO;
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();

	private @EJB ITaGrMouvStockServiceRemote taGrMouvStockService;
	private @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaUniteServiceRemote taUniteService;
	private @EJB ITaEntrepotServiceRemote taEntrepotService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaTitreTransportServiceRemote taTitreTransportService;
	
	private GrMouvStockDTO[] selectedGrMouvStockDTOs; 
	private TaGrMouvStock masterEntity = new TaGrMouvStock();
	private GrMouvStockDTO newGrMouvStockDTO = new GrMouvStockDTO();
	private GrMouvStockDTO selectedGrMouvStockDTO = new GrMouvStockDTO();
	
	private TaMouvementStockDTOJSF[] selectedTaMouvementStockDTOJSFs; 
	private TaMouvementStock masterEntityLigne = new TaMouvementStock();
	private TaMouvementStockDTOJSF OldTaMouvementStockDTOJSF = new TaMouvementStockDTOJSF();
	private TaMouvementStockDTOJSF newTaMouvementStockDTOJSF = new TaMouvementStockDTOJSF();
	private TaMouvementStockDTOJSF selectedTaMouvementStockDTOJSF = new TaMouvementStockDTOJSF();
	
//	private TaEtat taEtat;
	private TaArticle taArticleLigne;
	private TaEntrepot taEntrepotDestLigne;
	private TaEntrepot taEntrepotLigne;
	private TaTypeMouvement taTypeMouvement;
//	private TaTEntite taTEntite;
	private Boolean gestionLot = false;
	protected List<TaPreferences> listePreferences;
	
	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
	private List<ArticleLotEntrepotDTO> listArticleLotEntrepot;


	private LgrDozerMapper<GrMouvStockDTO,TaGrMouvStock> mapperUIToModel  = new LgrDozerMapper<GrMouvStockDTO,TaGrMouvStock>();
	private TaGrMouvStockMapper mapperModelToUI  =new  TaGrMouvStockMapper();

	private boolean insertAuto = false;
	private boolean typeReserve=false;
	
	private RemoteCommand rc;
	
	// - Dima - Début
	private String codeLot;
	
	public String getCodeLot() {
		return codeLot;
	}

	public void setCodeLot(String codeLot) {
		this.codeLot = codeLot;
	}
	// - Dima -  Fin
	
	public MouvementStockController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		rechercheAvecCommentaire(true);

		listePreferences= taPreferencesService.findByGroupe("facture");
		TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
		if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
			gestionLot=LibConversion.StringToBoolean(p.getValeur());
		}
		gestionCapsules = autorisationBean.isModeGestionCapsules();
		refresh();
	}

	public void refresh(){
//		values = taGrMouvStockService.selectAllDTO();
		values = taGrMouvStockService.findAllLight();
		valuesLigne = IHMmodel();
		valuesLigneDTO = IHMmodelDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	

	
	public List<TaMouvementStockDTOJSF> IHMmodel() {
		Boolean deplacement=false;
		TaMouvementStock ligneDeplacee=null;
		LinkedList<TaMouvementStock> ldao = new LinkedList<TaMouvementStock>();
		LinkedList<TaMouvementStockDTOJSF> lswt = new LinkedList<TaMouvementStockDTOJSF>();

		if(masterEntity!=null && !masterEntity.getTaMouvementStockes().isEmpty()) {
			if(masterEntity.getTaTypeMouvStock()!=null && masterEntity.getTaTypeMouvStock().getCode().equals("D")) {
				deplacement = true;
			}
			
			ldao.addAll(masterEntity.getTaMouvementStockes());

			TaMouvementStockMapper mapper = new TaMouvementStockMapper();
			MouvementStocksDTO dto = null;
			for (TaMouvementStock o : ldao) {
			
				if(!o.getUtilise()){
					TaMouvementStockDTOJSF t = new TaMouvementStockDTOJSF();
					try {
//						TaLot lot =taLotService.findByCode(o.getNumLot());
//						o.setTaLot(lot);
					dto = (MouvementStocksDTO) mapper.mapEntityToDto(o, new MouvementStocksDTO());
					t.setDto(dto);
					if(o.getCodeTitreTransport()!=null) {
						t.setTaTitreTransport(taTitreTransportService.findByCode(o.getCodeTitreTransport()));
						dto.setCodeTitreTransport(o.getCodeTitreTransport());
						dto.setQteTitreTransport(o.getQteTitreTransport());
					}
					if(o.getUn1Stock()!=null) {
						t.setTaUnite1(taUniteService.findByCode(o.getUn1Stock()));
					}
					if(o.getUn2Stock()!=null) {
						t.setTaUnite2(taUniteService.findByCode(o.getUn2Stock()));
					}
					if(o.getTaLot()!=null)
						t.setTaArticle(o.getTaLot().getTaArticle());
					if(dto.getCodeArticle()!=null )
						t.setTaArticleDTO(taArticleService.findByCodeDTO(t.getTaArticle().getCodeArticle()));
					
					t.setTaLot(o.getTaLot());
					if(t.getArticleLotEntrepotDTO()==null) {
						t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
					}
					if(o.getTaLot()!=null) {
						t.getArticleLotEntrepotDTO().setNumLot(o.getTaLot().getNumLot());
					}
					//t.getDto().setNumLot(o.getTaLot().getNumLot());
					//t.setTaEntrepotDest(o.getTaEntrepotDest());
					t.setTaEntrepotOrg(o.getTaEntrepot());
					if(deplacement && (deplacement && o.getQte1Stock().compareTo(BigDecimal.ZERO)<0)) {
						//Dans le cas d'un déplacement, l'entrepot de destination est l'entrepot d'origine de la ligne inverse
						TaMouvementStock inv = masterEntity.chercheLigneInverseDeplacement(o);
						inv = taMouvementStockService.findById(inv.getIdMouvementStock());
						if(inv!=null) {
							t.setTaEntrepotDest(inv.getTaEntrepot());
							o.setTaEntrepotDest(inv.getTaEntrepot());
						}
					}
					if(!deplacement || (deplacement && o.getQte1Stock().compareTo(BigDecimal.ZERO)<0)) {
						//Pour les mouvement des type déplacement qui sont constinué de lignes de sens opposés, 
						// on affiche uniquement la ligne négative car le sens du type de mouvement "déplacement" est négatif/false
						//la 2ème ligne (la ligne inverse qui elle est positive) n'apparait jamais dans c'est écran
						lswt.add(t);
					}
					t.getDto().setUtilise(true);
					o.setUtilise(true);
//					if(o.getLettrageDeplacement()!=null){
//						ligneDeplacee=ligneLettrageDeplacement(o.getLettrageDeplacement(),o.getIdMouvementStock(),ldao);
//						if(ligneDeplacee!=null){
//							t.setTaEntrepotDest(ligneDeplacee.getTaEntrepotDest());
//							t.getDto().setEmplacementDest(ligneDeplacee.getEmplacementDest());
//							ligneDeplacee.setUtilise(true);
//						}
//					}
					} catch (FinderException e) {
						
					}

				}
			}

		}
		return lswt;
	}
	
	
	public TaMouvementStock ligneLettrageDeplacement(Long lettrage,int courant , List<TaMouvementStock> list){
		for (TaMouvementStock o : list) {				
			if(!o.getUtilise()){
				if(o.getLettrageDeplacement().equals(lettrage) && o.getIdMouvementStock()!=courant)
					return o;
			}
		}
		return null;
	}

	public List<MouvementStocksDTO> IHMmodelDTO() {
		LinkedList<TaMouvementStock> ldao = new LinkedList<TaMouvementStock>();
		LinkedList<MouvementStocksDTO> lswt = new LinkedList<MouvementStocksDTO>();
		
		if(masterEntity!=null && !masterEntity.getTaMouvementStockes().isEmpty()) {

			ldao.addAll(masterEntity.getTaMouvementStockes());

			LgrDozerMapper<TaMouvementStock,MouvementStocksDTO> mapper = new LgrDozerMapper<TaMouvementStock,MouvementStocksDTO>();
			for (TaMouvementStock o : ldao) {
				MouvementStocksDTO t = null;
				t = (MouvementStocksDTO) mapper.map(o, MouvementStocksDTO.class);
				try {
				if(t.getNumLot()!=null){
					TaLot lot;
					lot = taLotService.findByCode(t.getNumLot());

					if(lot!=null){
						t.setCodeArticle(lot.getTaArticle().getCodeArticle());
						t.setIdArticle(lot.getTaArticle().getIdArticle());
						t.setIdLot(lot.getIdLot());
					}					
				}
				
				} catch (FinderException e) {
					
				}
				lswt.add(t);
			}
		}
		return lswt;
	}
	
	public List<GrMouvStockDTO> getValues(){  
		return values;
	}
	
	public void actAutoInsererLigne(ActionEvent actionEvent) {
		boolean existeDeja=false;
		if(isInsertAuto()) {
			for (TaMouvementStockDTOJSF ligne : valuesLigne) {
				if(ligne.ligneEstVide())existeDeja=true;
			}
			actInsererLigne(actionEvent);
			String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
			PrimeFaces.current().executeScript(oncomplete);
		}
	}
	
	public void actInsererLigne(ActionEvent actionEvent) {
		
//		if(selectedTaMouvementStockDTOJSF==null || !selectedTaMouvementStockDTOJSF.isAutoInsert()) {
			TaMouvementStockDTOJSF nouvelleLigne = new TaMouvementStockDTOJSF();
			typeReserve=false;
			nouvelleLigne.setDto(new MouvementStocksDTO());
			nouvelleLigne.getDto().setId(valuesLigne.size()+1);
//			nouvelleLigne.getDto().setNumLigneLDocument(valuesLigne.size()+1);
//			nouvelleLigne.getDto().setNumLot(selectedGrMouvStockDTO.getCodeGrMouvStock()+"_"+valuesLigne.size()+1);
//			nouvelleLigne.setAutoInsert(true);
			selectedTaMouvementStockDTOJSF=nouvelleLigne;
			masterEntityLigne = new TaMouvementStock();

			try {
			
				List<TaMouvementStockDTOJSF> modelListeLigneDevis = IHMmodel();

//				masterEntity.addLigneMouvement(masterEntityLigne);
			} catch (Exception e) {
				e.printStackTrace();
			}

			masterEntityLigne.setTaGrMouvStock(masterEntity);

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
			validateLigneDocumentAvantEnregistrement(selectedTaMouvementStockDTOJSF);
//			selectedTaMouvementStockDTOJSF.updateDTO();
			LgrDozerMapper<MouvementStocksDTO,TaMouvementStock> mapper = new LgrDozerMapper<MouvementStocksDTO,TaMouvementStock>();
			mapper.map((MouvementStocksDTO) selectedTaMouvementStockDTOJSF.getDto(),masterEntityLigne);
	

			if(selectedTaMouvementStockDTOJSF.getTaLot()!=null){
				masterEntityLigne.setTaLot(selectedTaMouvementStockDTOJSF.getTaLot());
//				masterEntityLigne.setNumLot(selectedTaMouvementStockDTOJSF.getTaLot().getNumLot());
				masterEntityLigne.getTaLot().setTaArticle(selectedTaMouvementStockDTOJSF.getTaArticle());
			}

			masterEntityLigne.setTaEntrepot(selectedTaMouvementStockDTOJSF.getTaEntrepotOrg());
			masterEntityLigne.setTaEntrepotDest(selectedTaMouvementStockDTOJSF.getTaEntrepotDest());
			Boolean sensMouv=null;
			if(masterEntity.getTaTypeMouvStock()!=null) {
				sensMouv=masterEntity.getTaTypeMouvStock().getSens();
			}
//			if(sensMouv!=null){
//				if(!sensMouv && masterEntityLigne.getQte1Stock()!=null)masterEntityLigne.setQte1Stock(masterEntityLigne.getQte1Stock().negate());
//				if(!sensMouv && masterEntityLigne.getQte2Stock()!=null)masterEntityLigne.setQte2Stock(masterEntityLigne.getQte2Stock().negate());
//			}
			changeSensLigne(masterEntityLigne,sensMouv);
			masterEntityLigne.setDateStock(masterEntity.getDateGrMouvStock());
			if(!masterEntity.getTaMouvementStockes().contains(masterEntityLigne)) {
				masterEntity.addLigneMouvement(masterEntityLigne);			
			}
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
	public void changeSensLigne(TaMouvementStock ligne,Boolean sensMouv){
		if(sensMouv!=null){
			if(ligne.getQte1Stock()!=null){
				if(!sensMouv)ligne.setQte1Stock(ligne.getQte1Stock().negate());
				else ligne.setQte1Stock(ligne.getQte1Stock().abs());
			}
			if(ligne.getQte2Stock()!=null){
				if(!sensMouv)ligne.setQte2Stock(ligne.getQte2Stock().negate());
				else ligne.setQte2Stock(ligne.getQte2Stock().abs());
			}			
		}
	}
	
	public void actAnnulerLigne(ActionEvent actionEvent) {
		if(selectedTaMouvementStockDTOJSF.getDto().getId()!=null ) {
			masterEntityLigne =rechercheLigne(selectedTaMouvementStockDTOJSF.getDto().getId());
			if(selectedTaMouvementStockDTOJSF.getDto().getVersionObj()!=null) {
				//pour gérér l'annulation
				selectedTaMouvementStockDTOJSF = OldTaMouvementStockDTOJSF.copy(OldTaMouvementStockDTOJSF);
				actEnregistrerLigne(null);
			}
			else {
				try {
					valuesLigne.remove(selectedTaMouvementStockDTOJSF);
					masterEntity.removeLigne(masterEntityLigne);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 		

//		valuesLigne= IHMmodel();
		if(!valuesLigne.isEmpty())
			selectionLigne(valuesLigne.get(0));		
		setInsertAuto(false);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actAnnulerLigne")); 
		}
	}
	
	
	public void actModifierLigne(ActionEvent actionEvent) {
		OldTaMouvementStockDTOJSF= OldTaMouvementStockDTOJSF.copy(selectedTaMouvementStockDTOJSF);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actModifierLigne")); 
		}
	}
	
	public void actSupprimerLigne() {
		actSupprimerLigne(null);
	}
	
	public void actSupprimerLigne(ActionEvent actionEvent) {
		valuesLigne.remove(selectedTaMouvementStockDTOJSF);
		TaMouvementStock ligne;
		try {
			ligne=rechercheLigne(selectedTaMouvementStockDTOJSF.getDto().getId());
			masterEntity.removeLigneMouvement(ligne);
			ligne.setTaGrMouvStock(null);
			taMouvementStockService.supprimer(ligne);
			
			if(masterEntity.getTaTypeMouvStock()!=null && masterEntity.getTaTypeMouvStock().getCode().equals("D")) {
				TaMouvementStock ligneInverse = masterEntity.chercheLigneInverseDeplacement(ligne);
				if(ligneInverse!=null) { //TODO ** a faire dans les services
					masterEntity.removeLigneMouvement(ligneInverse);
					ligneInverse.setTaGrMouvStock(null);
					taMouvementStockService.supprimer(ligneInverse);
				}
			}

			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actSupprimerLigne")); 
		}
	}
	
	public void onRowEdit(RowEditEvent event) {
		try {
			selectedTaMouvementStockDTOJSF.updateDTO();
			if(selectedTaMouvementStockDTOJSF.getDto().getId()!=null &&  selectedTaMouvementStockDTOJSF.getDto().getId()!=0) {
				masterEntityLigne = rechercheLigne(selectedTaMouvementStockDTOJSF.getDto().getId());
			}
			validateLigneDocumentAvantEnregistrement(selectedTaMouvementStockDTOJSF);
			actEnregistrerLigne(null);
			setInsertAuto(modeEcran.getMode()==EnumModeObjet.C_MO_INSERTION);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mouvement de stock", e.getMessage()));	
			context.validationFailed();
			setInsertAuto(false);
		}
}

	public void onRowSelectLigne(SelectEvent event) {
		selectedTaMouvementStockDTOJSF=((TaMouvementStockDTOJSF) event.getObject());
		if(masterEntity.getIdGrMouvStock()!=0 && selectedTaMouvementStockDTOJSF.getDto().getId()!=null)
			masterEntityLigne=rechercheLigne(selectedTaMouvementStockDTOJSF.getDto().getId());	
	}
	
	public void onRowEditInit(RowEditEvent event) {
		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
		DataTable table = (DataTable) evt.getSource();
		int activeRow = table.getRowIndex()+1;
		if(table.getRowCount() == activeRow) {
			//derniere ligne
			setInsertAuto(modeEcran.getMode()==EnumModeObjet.C_MO_INSERTION);
		} else {
			setInsertAuto(false);
		}
		if(event.getObject()!=null && !event.getObject().equals(selectedTaMouvementStockDTOJSF))
			selectedTaMouvementStockDTOJSF=((TaMouvementStockDTOJSF) event.getObject());
		actModifierLigne(null);
}
	public TaMouvementStock rechercheLigne(int id){
		TaMouvementStock obj=masterEntityLigne;
		for (TaMouvementStock ligne : masterEntity.getTaMouvementStockes()) {
			if(ligne.getIdMouvementStock()==id)
				obj=ligne;
		}
		return obj;
	}
	
	public void onRowCancel(RowEditEvent event) {
		
		actAnnulerLigne(null);
    }
	
	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedGrMouvStockDTO.getCodeGrMouvStock()!=null) {
					taGrMouvStockService.annuleCode(selectedGrMouvStockDTO.getCodeGrMouvStock());
				}				
				masterEntity=new TaGrMouvStock();
				if(values.size()>0) selectedGrMouvStockDTO = values.get(0);
				else selectedGrMouvStockDTO=new GrMouvStockDTO();
				onRowSelect(null);
				
				break;
			case C_MO_EDITION:
				if(selectedGrMouvStockDTO.getId()!=null && selectedGrMouvStockDTO.getId()!=0){
					masterEntity = taGrMouvStockService.findById(selectedGrMouvStockDTO.getId());
					selectedGrMouvStockDTO = taGrMouvStockService.findByIdDTO(selectedGrMouvStockDTO.getId());
					}				
				break;

			default:
				break;
			}			
				
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		mapperModelToUI.mapEntityToDto(masterEntity,selectedGrMouvStockDTO );
		if(masterEntity.getTaTypeMouvStock()!=null && masterEntity.getTaTypeMouvStock().getCode()!=null){
			typeReserve=taTypeMouvementService.typeMouvementReserve(masterEntity.getTaTypeMouvStock().getCode());
		}
		valuesLigne = IHMmodel();
		setInsertAuto(false);
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actAnnuler")); 
		}
	    
	} catch (FinderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


	
	
	public void autoCompleteMapUIToDTO() {
		
//		if(taEtat!=null) {
//			validateUIField(Const.C_CODE_ETAT,taEtat.getCodeEtat());
//			selectedGrMouvStockDTO.setCodeEtat(taEtat.getCodeEtat());
//		}
		if(taTypeMouvement!=null) {
			validateUIField(Const.C_CODE_TYPE_MOUVEMENT,taTypeMouvement.getCode());
			selectedGrMouvStockDTO.setIdtypeMouvStock(taTypeMouvement.getIdTypeMouvement());
			selectedGrMouvStockDTO.setCodetypeMouvStock(taTypeMouvement.getCode());
		}
		masterEntity.setTaTypeMouvStock(taTypeMouvement);
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
//			taEtat = null;
			taTypeMouvement = null;
			
//			if(selectedGrMouvStockDTO.getCodeEtat()!=null && !selectedGrMouvStockDTO.getCodeEtat().equals("")) {
//				taEtat = taTypeMouvementService.findByCode(selectedGrMouvStockDTO.getCodeEtat());
//			}
			if(selectedGrMouvStockDTO.getCodetypeMouvStock()!=null && !selectedGrMouvStockDTO.getCodetypeMouvStock().equals("")) {
				taTypeMouvement = taTypeMouvementService.findByCode(selectedGrMouvStockDTO.getCodetypeMouvStock());
				masterEntity.setTaTypeMouvStock(taTypeMouvement);
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		
		
		try {
			autoCompleteMapUIToDTO();
			validateDocumentAvantEnregistrement(selectedGrMouvStockDTO);
			mapperUIToModel.map(selectedGrMouvStockDTO, masterEntity);
			masterEntity.setTaBonReception(null);
			masterEntity.setTaFabrication(null);
			masterEntity.setTaInventaire(null);
			masterEntity.setManuel(true);
			List<TaMouvementStock> mouvInverseDeplacementGenere = new ArrayList<>();
			for (TaMouvementStock l : masterEntity.getTaMouvementStockes()) {
				l.setDateStock(masterEntity.getDateGrMouvStock());
				
				if(masterEntity.getTaTypeMouvStock()!=null && masterEntity.getTaTypeMouvStock().getCode().equals("D")) {
					//Déplacement, pour chacune des lignes on générère une 2ème ligne de sens oposé si elle n'existe pas déjà.
					//le type D a un sens négatif (Sortie) dans la bdd donc il faut s'assuré de généré la ligne positive (Entrée) correspondante
					if(l.getQte1Stock().compareTo(BigDecimal.ZERO)<0) { //on est bien sur une ligne "principale"
						TaMouvementStock ligneInverse = masterEntity.chercheLigneInverseDeplacement(l, true);
						if(ligneInverse!=null) {
							mouvInverseDeplacementGenere.add(ligneInverse);
							
						}
					}
				}
			}
			
			for (TaMouvementStock ligneInverse : mouvInverseDeplacementGenere) {
				if(ligneInverse.getIdMouvementStock()==0) { //nouvelle ligne
					masterEntity.getTaMouvementStockes().add(ligneInverse);
					ligneInverse.setTaGrMouvStock(masterEntity);
				} 
			}
			//masterEntity.getTaMouvementStockes().addAll(mouvInverseDeplacementGenere);

			masterEntity = taGrMouvStockService.merge(masterEntity,ITaGrMouvStockServiceRemote.validationContext);
			
			masterEntity = taGrMouvStockService.findById(masterEntity.getIdGrMouvStock());
			valuesLigne = IHMmodel();
			
			mapperModelToUI.mapEntityToDto(masterEntity, selectedGrMouvStockDTO);
			selectedGrMouvStockDTOs = new GrMouvStockDTO[]{selectedGrMouvStockDTO};
			autoCompleteMapDTOtoUI();
			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedGrMouvStockDTO);
			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

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
			selectedGrMouvStockDTO = new GrMouvStockDTO();
			masterEntity = new TaGrMouvStock();
			valuesLigne = IHMmodel();
			autoCompleteMapDTOtoUI();

			if(selectedGrMouvStockDTO.getCodeGrMouvStock()!=null) {
				taGrMouvStockService.annuleCode(selectedGrMouvStockDTO.getCodeGrMouvStock());
			}

			selectedGrMouvStockDTO.setDateGrMouvStock(taInfoEntrepriseService.dateDansExercice(new Date()));

			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedGrMouvStockDTO.getDateGrMouvStock()));

			selectedGrMouvStockDTO.setCodeGrMouvStock(taGrMouvStockService.genereCode(params));
			
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			typeReserve=false;
			setInsertAuto(true);
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
	
	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {
		
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		setInsertAuto(false);
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
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_STOCK);
		b.setTitre("Mouvement de stock");
		b.setTemplate("stock/mouvement_stock.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MOUVEMENT_STOCK);
		b.setStyle(LgrTab.CSS_CLASS_TAB_STOCK);
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
    	TaGrMouvStock taGrMouvStock = new TaGrMouvStock();
    	String message="";
    	try {
    		if(selectedGrMouvStockDTO!=null && selectedGrMouvStockDTO.getId()!=null){
    			taGrMouvStock = taGrMouvStockService.findById(selectedGrMouvStockDTO.getId());
    			message="le mouvement "+selectedGrMouvStockDTO.getCodeGrMouvStock()+" ne peut pas être supprimé directement";
    		}
    		if(taGrMouvStock!=null && taGrMouvStock.getTaTypeMouvStock()!=null
    				&& !taTypeMouvementService.typeMouvementReserve(taGrMouvStock.getTaTypeMouvStock().getCode())){

    			taGrMouvStockService.remove(taGrMouvStock);
    			values.remove(selectedGrMouvStockDTO);

    			if(!values.isEmpty()) {
    				selectedGrMouvStockDTO = values.get(0);
    				selectedGrMouvStockDTOs = new GrMouvStockDTO[]{selectedGrMouvStockDTO};
    			}else{
    				selectedGrMouvStockDTO = new GrMouvStockDTO();
    				selectedGrMouvStockDTOs = new GrMouvStockDTO[]{};
    			}
    			updateTab();
    		}else {
        		FacesContext context = FacesContext.getCurrentInstance();  
        		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mauvaise sélection",message ));
    		}
    		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
    		setInsertAuto(false);
    		if(ConstWeb.DEBUG) {
    			FacesContext context = FacesContext.getCurrentInstance();  
    			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actSupprimer"));
    		}
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		FacesContext context = FacesContext.getCurrentInstance();  
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mouvements", e.getMessage()));
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
		
	
		selectedGrMouvStockDTO = new GrMouvStockDTO();
		selectedGrMouvStockDTOs=new GrMouvStockDTO[]{selectedGrMouvStockDTO};
		updateTab();
		
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeMouvStock').filter()");
	}

	public void actImprimer(ActionEvent actionEvent) throws FinderException {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actImprimer")); 
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		setCodeLot(selectedGrMouvStockDTO.getCodeGrMouvStock());

		// - Dima - Début

		//sessionMap.put("grMouvStock", taGrMouvStockService.findById(selectedGrMouvStockDTO.getId())); // Dima - ça marche
		//List<TaLot> lot = new ArrayList<TaLot>();
		TaGrMouvStock tmp = taGrMouvStockService.findById(selectedGrMouvStockDTO.getId());
		List<TaMouvementStock> lMouv = new ArrayList<TaMouvementStock>();
		for (TaMouvementStock mouv : tmp.getTaMouvementStockes()) {
			TaMouvementStock m = mouv;
			
//			TaLot l = (mouv.getNumLot()!=null? taLotService.findByCode(mouv.getNumLot()):null);
			TaArticle a = (m.getTaLot().getTaArticle().getCodeArticle()!=null?taArticleService.findByCode(m.getTaLot().getTaArticle().getCodeArticle()):null);
			TaEntrepot e = (mouv.getTaEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot()!=null?taEntrepotService.findByCode(mouv.getTaEntrepot().getCodeEntrepot()):null);
			TaGrMouvStock gm = (mouv.getTaGrMouvStock().getIdGrMouvStock()!=0?taGrMouvStockService.findById(mouv.getTaGrMouvStock().getIdGrMouvStock()):null);
			TaTypeMouvement tm = (gm.getTaTypeMouvStock()!=null && gm.getTaTypeMouvStock().getCode()!=null?taTypeMouvementService.findByCode(gm.getTaTypeMouvStock().getCode()):null);
			
//			mouv.getNumLot(); // - 1
//			mouv.getLibelleStock(); // - 2
//			mouv.getQte1Stock(); // - 3
//			mouv.getUn1Stock();  // - 5
//			mouv.getQte2Stock(); // - 4
//			mouv.getUn2Stock();  // - 6
//			mouv.getDateStock(); // - 7
//			mouv.getTaEntrepot().getCodeEntrepot(); // - 10 (8)
//			mouv.getTaGrMouvStock().getTaTypeMouvStock().getLibelle(); // - 11 (9)
//			mouv.getTaLot().getNumLot(); // ou setNumLot(); // - 8 (1)
//			mouv.getTaLot().getTaArticle().getLibellecArticle(); // - 9
//			lot.add(taLotService.findByCode(mouv.getNumLot()));
			
//			m.setTaLot(l);
			m.getTaLot().setTaArticle(a);
			m.setTaEntrepot(e);
			m.setTaGrMouvStock(gm);
			m.getTaGrMouvStock().setTaTypeMouvStock(tm);
			
			System.out.println("***************************************> m - "+m);
			
			lMouv.add(m);
		}
		sessionMap.put("lMouvement", lMouv);

		// - Dima -  Fin
		
//			session.setAttribute("tiers", taTiersService.findById(selectedGrMouvStockDTO.getId()));
			System.out.println("MouvementStockController.actImprimer()");
			System.out.println("***************************************> lMouvement - "+lMouv);
			
	}    

	/*
	public void actImprimer(ActionEvent actionEvent) { // méthode d'origine
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actImprimer")); 
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("tiers", taGrMouvStockService.findById(selectedGrMouvStockDTO.getId()));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedGrMouvStockDTO.getId()));
			System.out.println("MouvementStockController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}  
	*/
	
	public void actImprimerListeMouvementStock(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeMouvementStock", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_STOCK);
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
							"Chargement du groupe de mouvement N°"+selectedGrMouvStockDTO.getCodeGrMouvStock()
							)); 
			}
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_MOUVEMENT_STOCK);
		}
	} 
	
	public void updateTab(){
		try {
			if(selectedGrMouvStockDTOs!=null && selectedGrMouvStockDTOs.length>0) {
				selectedGrMouvStockDTO = selectedGrMouvStockDTOs[0];
			}
			if(selectedGrMouvStockDTO.getId()!=null && selectedGrMouvStockDTO.getId()!=0) {
				masterEntity = taGrMouvStockService.findById(selectedGrMouvStockDTO.getId());
			}
			if(masterEntity.getTaTypeMouvStock()!=null && masterEntity.getTaTypeMouvStock().getCode()!=null){
				typeReserve=taTypeMouvementService.typeMouvementReserve(masterEntity.getTaTypeMouvStock().getCode());
			}
			autoCompleteMapDTOtoUI();
			valuesLigne = IHMmodel();
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Mouvement de stock", 
						"Chargement du groupe de mouvement N°"+selectedGrMouvStockDTO.getCodeGrMouvStock()
						)); 
			}
		
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MOUVEMENT_STOCK);
		b.setTitre("Mouvement de stock");
		b.setTemplate("stock/mouvement_stock.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MOUVEMENT_STOCK);
		b.setStyle(LgrTab.CSS_CLASS_TAB_STOCK);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);

		updateTab();
		scrollToTop();
	} 

	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public boolean typeMouvementNonModifiable() {
		return typeReserve;
	}
	public boolean typeMouvementNonModifiableSurSelection(GrMouvStockDTO mouv){
		if(mouv!=null && mouv.getCodetypeMouvStock()!=null)
			return taTypeMouvementService.typeMouvementReserve(mouv.getCodetypeMouvStock());
		return false;
	}
	
	public boolean typeMouvementNonModifiableSurListeOverlay(){
		if(values!=null){
			for (GrMouvStockDTO grMouvStockDTO : values) {
				if(grMouvStockDTO!=null && grMouvStockDTO.getCodetypeMouvStock()!=null)
					if(taTypeMouvementService.typeMouvementReserve(grMouvStockDTO.getCodetypeMouvStock()))
						return true;
			}
		}
		return false;
	}
	public void actDialogTypeMouvement(ActionEvent actionEvent) {
		
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
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_type_mouvement", options, params);
			    
	}
	
	public void handleReturnDialogTypeMouvement(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTypeMouvement = (TaTypeMouvement) event.getObject();
		}
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
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage(TITRE_FENETRE, "actAbout")); 	    
	}
	
	public void handleReturnDialogEntrepot(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taEntrepotLigne = (TaEntrepot) event.getObject();
		}
	}
	public void handleReturnDialogEntrepotDest(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taEntrepotDestLigne = (TaEntrepot) event.getObject();
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
				if(selectedGrMouvStockDTO!=null && selectedGrMouvStockDTO.getId()!=null  && selectedGrMouvStockDTO.getId()!=0 ) retour = false;
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
			if(modeEcran.dataSetEnModif()) {
				retour = false;
			}
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

	public GrMouvStockDTO[] getSelectedGrMouvStockDTOs() {
		return selectedGrMouvStockDTOs;
	}

	public void setSelectedGrMouvStockDTOs(GrMouvStockDTO[] selectedGrMouvStockDTOs) {
		this.selectedGrMouvStockDTOs = selectedGrMouvStockDTOs;
	}

	public GrMouvStockDTO getNewGrMouvStockDTO() {
		return newGrMouvStockDTO;
	}

	public void setNewGrMouvStockDTO(GrMouvStockDTO newGrMouvStockDTO) {
		this.newGrMouvStockDTO = newGrMouvStockDTO;
	}

	public GrMouvStockDTO getSelectedGrMouvStockDTO() {
		return selectedGrMouvStockDTO;
	}

	public void setSelectedGrMouvStockDTO(GrMouvStockDTO selectedGrMouvStockDTO) {
		this.selectedGrMouvStockDTO = selectedGrMouvStockDTO;
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
//		  MouvementStocksDTO dtoTemp =new MouvementStocksDTO();
//		  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//		  taMouvementStockService.validateDTOProperty(dtoTemp, nomChamp, ITaMouvementStockServiceRemote.validationContext );
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
			Set<ConstraintViolation<MouvementStocksDTO>> violations = factory.getValidator().validateValue(MouvementStocksDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<MouvementStocksDTO> cv : violations) {
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
	
	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
//		String msg = "";
//		
//		try {
//		 
//		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
//		  
//		  
//		  validateUIField(nomChamp,value);
//		  GrMouvStockDTO dtoTemp =new GrMouvStockDTO();
//		  PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//		  taGrMouvStockService.validateDTOProperty(dtoTemp, nomChamp, ITaGrMouvStockServiceRemote.validationContext );
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
			Set<ConstraintViolation<GrMouvStockDTO>> violations = factory.getValidator().validateValue(GrMouvStockDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<GrMouvStockDTO> cv : violations) {
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
	
	public void validateDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
			taGrMouvStockService.validateDTOProperty(selectedGrMouvStockDTO,Const.C_CODE_TYPE_MOUVEMENT_STOCK,  ITaGrMouvStockServiceRemote.validationContext );
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateLigneDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
			selectedTaMouvementStockDTOJSF.updateDTO();
			if(!gestionLot && selectedTaMouvementStockDTOJSF.getTaLot()==null && selectedTaMouvementStockDTOJSF.getTaArticle()!=null)
				selectedTaMouvementStockDTOJSF.setTaLot(taLotService.findOrCreateLotVirtuelArticle(selectedTaMouvementStockDTOJSF.getTaArticle().getIdArticle()));
			else
			if(selectedTaMouvementStockDTOJSF.getTaLot()==null && gestionLot) throw new Exception("Le lot doit être renseigné !!!");

			if(selectedTaMouvementStockDTOJSF.ligneEstVide()) throw new Exception("La ligne est vide !!!");
//			taMouvementStockService.validateDTOProperty(selectedTaMouvementStockDTOJSF.getDto(),Const.C_CODE_ARTICLE,  ITaMouvementStockServiceRemote.validationContext );
//			taMouvementStockService.validateDTOProperty(selectedTaMouvementStockDTOJSF.getDto(),Const.C_NUM_LOT,  ITaMouvementStockServiceRemote.validationContext );
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
		if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
		if(value instanceof TaEntrepotDTO && ((TaEntrepotDTO) value).getCodeEntrepot()!=null && ((TaEntrepotDTO) value).getCodeEntrepot().equals(Const.C_AUCUN))value=null;	
		if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
		}
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		TaArticle articleOld=null;
		try {	
			if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
				
				Date newValue;
				if(value!=null && value instanceof Date) {
					newValue=taInfoEntrepriseService.dateDansExercice((Date) value);
					selectedGrMouvStockDTO.setDateGrMouvStock(newValue);
					value=newValue;
				}
				if(selectedGrMouvStockDTO.getCodeGrMouvStock()!=null) {
					taGrMouvStockService.annuleCode(selectedGrMouvStockDTO.getCodeGrMouvStock());
				}
				Map<String, String> params = new LinkedHashMap<String, String>();
				params.put(Const.C_DATEDOCUMENT, LibDate.dateToString((Date) value));
				if(masterEntity!=null && masterEntity.getTaTypeMouvStock()!=null)
					params.put(Const.C_CODETYPE, masterEntity.getTaTypeMouvStock().getCode());
				selectedGrMouvStockDTO.setCodeGrMouvStock(taGrMouvStockService.genereCode(params));
			}else
			if(nomChamp.equals(Const.C_CODE_DOCUMENT)) {
				
			}else
			if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = new TaArticle();
				TaCoefficientUnite coef = null;
				if(value!=null){
					if(value instanceof TaArticleDTO) {
						entity = taArticleService.findByCode(((TaArticleDTO)value).getCodeArticle());
					} else {
						entity = taArticleService.findByCode((String)value);
					}
					if(masterEntityLigne.getTaLot()!=null) {
						articleOld=masterEntityLigne.getTaLot().getTaArticle();
					}
					changement=!entity.equals(articleOld);
					if(changement){//raz ligne
						selectedTaMouvementStockDTOJSF.getDto().setCodeArticle(entity.getCodeArticle());
						selectedTaMouvementStockDTOJSF.getDto().setLibelleStock(entity.getLibellecArticle());
						selectedTaMouvementStockDTOJSF.getDto().setIdLot(null);
						selectedTaMouvementStockDTOJSF.getDto().setNumLot("");
						selectedTaMouvementStockDTOJSF.getDto().setIdEntrepot(null);
						selectedTaMouvementStockDTOJSF.getDto().setCodeEntrepot("");
						selectedTaMouvementStockDTOJSF.getDto().setEmplacement(null);
						selectedTaMouvementStockDTOJSF.getDto().setEmplacementDest(null);
						selectedTaMouvementStockDTOJSF.setArticleLotEntrepotDTO(null);
						selectedTaMouvementStockDTOJSF.setTaLot(null);
						selectedTaMouvementStockDTOJSF.setTaEntrepotOrg(null);
						selectedTaMouvementStockDTOJSF.setTaEntrepotDest(null);
						selectedTaMouvementStockDTOJSF.setTaArticle(entity);
						selectedTaMouvementStockDTOJSF.setTaUnite1(null);
						selectedTaMouvementStockDTOJSF.setTaUnite2(null);
						selectedTaMouvementStockDTOJSF.getDto().setUn1Stock("");
						selectedTaMouvementStockDTOJSF.getDto().setQte1Stock(null);
						selectedTaMouvementStockDTOJSF.getDto().setUn2Stock("");
						selectedTaMouvementStockDTOJSF.getDto().setQte2Stock(null);
						
						TaRapportUnite rapport = null;
						if(entity!=null) rapport=entity.getTaRapportUnite();
						if(rapport!=null){
							if(rapport.getTaUnite2()!=null) {
								coef=recupCoefficientUnite(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
								selectedTaMouvementStockDTOJSF.setTaCoefficientUnite(coef);
							}
						}
						
						if(coef!=null){
							selectedTaMouvementStockDTOJSF.getDto().setQte1Stock(BigDecimal.ONE);
							selectedTaMouvementStockDTOJSF.setTaCoefficientUnite(coef);
							if(entity.getTaUnite1()!=null) {
								selectedTaMouvementStockDTOJSF.getDto().setUn1Stock(entity.getTaUnite1().getCodeUnite());
								selectedTaMouvementStockDTOJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
							}
							if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
								selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(coef.getUniteB().getCodeUnite());
								selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
							}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
								selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(coef.getUniteA().getCodeUnite());
								selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
							}
						}else
							if(entity!=null ){
								if(entity.getTaUnite1()!=null) {
									selectedTaMouvementStockDTOJSF.getDto().setUn1Stock(entity.getTaUnite1().getCodeUnite());
									selectedTaMouvementStockDTOJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
								}
								for (TaRapportUnite obj : entity.getTaRapportUnites()) {
									if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaMouvementStockDTOJSF.getDto().getUn1Stock())){
										if(obj!=null){
											if(obj.getTaUnite2()!=null) {										
												coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
											}
										}
										selectedTaMouvementStockDTOJSF.setTaCoefficientUnite(coef);
										if(coef!=null) {
											if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
											selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(coef.getUniteB().getCodeUnite());
											selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
											}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
												selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(coef.getUniteA().getCodeUnite());
												selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
											}
										}else if(obj.getTaUnite2()!=null){
											selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(obj.getTaUnite2().getCodeUnite());
											selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
										}
									}							
								}
							}						
//						if(rapport!=null){
//							selectedTaMouvementStockDTOJSF.setTaRapport(rapport);
//							if(rapport.getTaUnite1()!=null){
//								selectedTaMouvementStockDTOJSF.getDto().setUn1Stock(rapport.getTaUnite1().getCodeUnite());
//								selectedTaMouvementStockDTOJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//								selectedTaMouvementStockDTOJSF.getDto().setQte1Stock(BigDecimal.ONE);
//							}
//							if(rapport.getTaUnite2()!=null){
//								selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(rapport.getTaUnite2().getCodeUnite());
//								selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(rapport.getTaUnite2().getCodeUnite()));
//							}
//						}
//						else
//							if(entity.getTaPrix()!=null){
//								if(entity.getTaUnite1()!=null){
//									selectedTaMouvementStockDTOJSF.getDto().setUn1Stock(entity.getTaUnite1().getCodeUnite());
//									selectedTaMouvementStockDTOJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//									selectedTaMouvementStockDTOJSF.getDto().setQte1Stock(BigDecimal.ONE);
//								}
//								for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//									if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaMouvementStockDTOJSF.getDto().getUn1Stock())){
//										if(obj!=null){
//											if(obj.getTaUnite2()!=null) {										
//												coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
//												selectedTaMouvementStockDTOJSF.setTaCoefficientUnite(coef);
//											}
//										}
//										selectedTaMouvementStockDTOJSF.setTaCoefficientUnite(coef);
//										if(coef!=null) {
//											if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
//												selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(coef.getUniteB().getCodeUnite());
//												selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//											}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
//												selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(coef.getUniteA().getCodeUnite());
//												selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
//											}
//										}else if(obj.getTaUnite2()!=null){
//											selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(obj.getTaUnite2().getCodeUnite());
//											selectedTaMouvementStockDTOJSF.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
//										}
//									}							
//								}
//							}
						if(entity!=null && entity.getTaRTaTitreTransport()!=null) {
							BigDecimal qteCrd = BigDecimal.ZERO;	
							selectedTaMouvementStockDTOJSF.setTaTitreTransport(entity.getTaRTaTitreTransport().getTaTitreTransport());
								if(entity.getTaRTaTitreTransport().getQteTitreTransport()!=null )
									qteCrd=entity.getTaRTaTitreTransport().getQteTitreTransport();
								selectedTaMouvementStockDTOJSF.getDto().setQteTitreTransport(selectedTaMouvementStockDTOJSF.getDto().getQte1Stock().multiply(qteCrd));
						} else {
							selectedTaMouvementStockDTOJSF.setTaTitreTransport(null);
							selectedTaMouvementStockDTOJSF.getDto().setQteTitreTransport(BigDecimal.ZERO);
						}
					validateUIField(Const.C_QTE1_STOCK, selectedTaMouvementStockDTOJSF.getDto().getQte1Stock());	
					}

				}
			} else if(nomChamp.equals(Const.C_CODE_TYPE_MOUVEMENT_STOCK)) {
				changement=false;
				TaTypeMouvement entity = null;
				if(value!=null){
					if(value instanceof TaTypeMouvement){
						entity=(TaTypeMouvement) value;
					}else {
						entity = taTypeMouvementService.findByCode((String)value);
					}
				}
				changement=!entity.equals(masterEntity.getTaTypeMouvStock());
				masterEntity.setTaTypeMouvStock(entity);
				if(changement){
					Boolean sensMouv=null;
					if(masterEntity.getTaTypeMouvStock()!=null) {
						sensMouv=masterEntity.getTaTypeMouvStock().getSens();
					}
					for (TaMouvementStock ligne : masterEntity.getTaMouvementStockes()) {
						changeSensLigne(ligne,sensMouv);
					}

				}
			} else if(nomChamp.equals(Const.C_UN1_STOCK)) {
				TaUnite entity = new TaUnite();
				if(value!=null){
					if(value instanceof TaUnite) {
						entity = (TaUnite)value;
					}

					selectedTaMouvementStockDTOJSF.getDto().setUn1Stock(entity.getCodeUnite());
				}else{
					selectedTaMouvementStockDTOJSF.getDto().setUn1Stock(null);
				}
				selectedTaMouvementStockDTOJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedTaMouvementStockDTOJSF.getDto().getUn1Stock(),selectedTaMouvementStockDTOJSF.getDto().getUn2Stock()));
				validateUIField(Const.C_QTE1_STOCK, selectedTaMouvementStockDTOJSF.getDto().getQte1Stock());

			} else if(nomChamp.equals(Const.C_UN2_STOCK)) {
				TaUnite entity = new TaUnite();
				if(value!=null){
					if(value instanceof TaUnite) {
						entity = (TaUnite)value;
					}

					selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(entity.getCodeUnite());
				}else{
					selectedTaMouvementStockDTOJSF.getDto().setUn2Stock(null);
				}

			} else if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
				TaEntrepot entity =null;
				if(value!=null){
					if(value instanceof TaEntrepot){
						entity=(TaEntrepot) value;
						entity = taEntrepotService.findByCode(entity.getCodeEntrepot());
					}else{
						entity = taEntrepotService.findByCode((String)value);
					}
					masterEntityLigne.setTaEntrepot(entity);
				}else{
					masterEntityLigne.setTaEntrepot(null);
					selectedTaMouvementStockDTOJSF.getDto().setCodeEntrepot("");
				}

			} else if(nomChamp.equals(Const.C_EMPLACEMENT)) {

				if(value!=null){

				}else{
					selectedTaMouvementStockDTOJSF.getDto().setEmplacement("");
				}
			} else if(nomChamp.equals(Const.C_CODE_ENTREPOT_DEST)) {
				TaEntrepot entity =null;
				if(value!=null){
					if(value instanceof TaEntrepot){
						entity=(TaEntrepot) value;
						entity = taEntrepotService.findByCode(entity.getCodeEntrepot());
					}else{
						entity = taEntrepotService.findByCode((String)value);
					}
					masterEntityLigne.setTaEntrepotDest(entity);
				}else{
					masterEntityLigne.setTaEntrepotDest(null);
					selectedTaMouvementStockDTOJSF.getDto().setCodeEntrepotDest("");
				}

			} else if(nomChamp.equals(Const.C_EMPLACEMENT_DEST)) {

				if(value!=null){

				}else{
					selectedTaMouvementStockDTOJSF.getDto().setEmplacement("");
				}
			} else if(nomChamp.equals(Const.C_QTE1_STOCK)) {
				BigDecimal qte=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						qte=(BigDecimal)value;
					}
					selectedTaMouvementStockDTOJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedTaMouvementStockDTOJSF.getDto().getUn1Stock(),
							selectedTaMouvementStockDTOJSF.getDto().getUn2Stock()));
					if(selectedTaMouvementStockDTOJSF.getTaCoefficientUnite()!=null) {
						if(selectedTaMouvementStockDTOJSF.getTaCoefficientUnite().getOperateurDivise()) 
							selectedTaMouvementStockDTOJSF.getDto().setQte2Stock((qte).divide(selectedTaMouvementStockDTOJSF.getTaCoefficientUnite().getCoeff()
									,MathContext.DECIMAL128).setScale(selectedTaMouvementStockDTOJSF.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
						else selectedTaMouvementStockDTOJSF.getDto().setQte2Stock((qte).multiply(selectedTaMouvementStockDTOJSF.getTaCoefficientUnite().getCoeff()));
					}else  {
						selectedTaMouvementStockDTOJSF.getDto().setQte2Stock(BigDecimal.ZERO);
						masterEntityLigne.setQte2Stock(null);
					}

				} else {
					masterEntityLigne.setQte2Stock(null);
				}
				if(selectedTaMouvementStockDTOJSF.getTaTitreTransport()!=null) {
					BigDecimal qteCrd =BigDecimal.ZERO;
					if(selectedTaMouvementStockDTOJSF.getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null )
						qteCrd=selectedTaMouvementStockDTOJSF.getTaArticle().getTaRTaTitreTransport().getQteTitreTransport();
					selectedTaMouvementStockDTOJSF.getDto().setQteTitreTransport(qte.multiply(qteCrd));
				} else {
					selectedTaMouvementStockDTOJSF.getDto().setQteTitreTransport(BigDecimal.ZERO);
				}				

			} else if(nomChamp.equals(Const.C_NUM_LOT)) {
				selectedTaMouvementStockDTOJSF.getDto().setEmplacement(null);
				if( selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getEmplacement()!=null 
						&& !selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getEmplacement().equals("")) {
					selectedTaMouvementStockDTOJSF.getDto().setEmplacement(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getEmplacement());
				}
				selectedTaMouvementStockDTOJSF.getDto().setCodeEntrepot(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getCodeEntrepot());

				TaEntrepot entity =null;
				entity = taEntrepotService.findByCode(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getCodeEntrepot());
				selectedTaMouvementStockDTOJSF.setTaEntrepotOrg(entity);

				TaLot lot =null;
				lot = taLotService.findByCode(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getNumLot());
				selectedTaMouvementStockDTOJSF.setTaLot(lot);
				selectedTaMouvementStockDTOJSF.getDto().setLibelleStock(lot.getLibelle());
				if(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getIdUnite()!=null){
					selectedTaMouvementStockDTOJSF.setTaUnite1(taUniteService.findById(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getIdUnite()));
					selectedTaMouvementStockDTOJSF.getDto().setUn1Stock(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getCodeUnite());
				}
//				selectedTaMouvementStockDTOJSF.getDto().setQte1Stock(selectedTaMouvementStockDTOJSF.getArticleLotEntrepotDTO().getQte1());
			}
			//
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	public List<ArticleLotEntrepotDTO> lotAutoComplete(String query) {
		return onChangeListArticleMP(selectedTaMouvementStockDTOJSF.getTaArticleDTO().getCodeArticle(),query.toUpperCase());
//		List<TaLot> filteredValues = new ArrayList<TaLot>();
//		if(selectedTaMouvementStockDTOJSF!=null && selectedTaMouvementStockDTOJSF.getTaArticleDTO()!=null){
//			List<TaLot> allValues = taLotService.lotsArticle(selectedTaMouvementStockDTOJSF.getTaArticleDTO().getCodeArticle(),null,null,false);
//
//			if(allValues!=null){
//				for (int i = 0; i < allValues.size(); i++) {
//					TaLot obj = allValues.get(i);
//					if(query.equals("*") || obj.getNumLot().toLowerCase().contains(query.toLowerCase())) {
//						filteredValues.add(obj);
//					}
//				}
//			}
//		}
//		return filteredValues;
	}
	
	public List<ArticleLotEntrepotDTO> onChangeListArticleMP(String codeArticle, String numlot) {
		try {
			
			listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();

			listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(codeArticle, null);
			List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
			List<BigDecimal> qte = new ArrayList<BigDecimal>();
			for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){ //liste des lots en stocks par articles
	
				for(TaMouvementStock mouv : masterEntity.getTaMouvementStockes()){ //liste des MP de la fabrication courante
					
					//meme emplacement, meme entrepot, meme article, meme lot, meme Unite 1
					if(mouv.getEmplacement()!=null && mouv.getEmplacement().equals(dto.getEmplacement()) 
							&& mouv.getTaEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot()) 
							&& mouv.getTaLot().getTaArticle().getCodeArticle().equals(codeArticle)  
							&& ((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot())
//							&& (mouv.getNumLot()).equals(dto.getNumLot())
							&& dto.getCodeUnite()!=null 
							&& (mouv.getUn1Stock()).equals(dto.getCodeUnite())
						){
						temp.add(dto);
						qte.add(mouv.getQte1Stock()) ;
					}
				}
			}
	
			int i = 0;
			for(ArticleLotEntrepotDTO dtoTemp: temp){
				//soustraction des quantités qui serait déjà utilisé dans d'autre lignes de MP de cette fabrication
				listArticleLotEntrepot.remove(dtoTemp);
				dtoTemp.setQte1(dtoTemp.getQte1().subtract(qte.get(i)));
				i++;
				listArticleLotEntrepot.add(dtoTemp);
			}
			
			List<ArticleLotEntrepotDTO> filteredValues = new ArrayList<ArticleLotEntrepotDTO>();

			for (int j = 0; j < listArticleLotEntrepot.size(); j++) {
				//filtre en fonction de la saisie
				ArticleLotEntrepotDTO ale = listArticleLotEntrepot.get(j);
				if(numlot.equals("*") || ale.getNumLot().toLowerCase().contains(numlot.toLowerCase())) {
					filteredValues.add(ale);
				}
			}
			if(filteredValues.isEmpty()) {
				TaArticle a = taArticleService.findByCode(codeArticle);
				TaLot lotVirt =  taLotService.findOrCreateLotVirtuelArticle(a.getIdArticle());
				ArticleLotEntrepotDTO aleVirt = new ArticleLotEntrepotDTO();
				aleVirt.setIdALE(1);
				aleVirt.setNumLot(lotVirt.getNumLot());
				aleVirt.setLibelleLot(lotVirt.getLibelle());
				filteredValues.add(aleVirt);
			}
			listArticleLotEntrepot = filteredValues;
			return listArticleLotEntrepot;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listArticleLotEntrepot;
	}
	
	public List<TaTypeMouvement> typeMouvementAutoComplete(String query) {
        List<TaTypeMouvement> allValues = taTypeMouvementService.selectAll();
        List<TaTypeMouvement> filteredValues = new ArrayList<TaTypeMouvement>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTypeMouvement obj = allValues.get(i);
            if(query.equals("*") || obj.getCode().toLowerCase().contains(query.toLowerCase()) && 
            		!obj.getSysteme()) {
                filteredValues.add(obj);
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
	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaUnite> uniteAutoComplete(String query) {
		List<TaUnite> allValues = taUniteService.selectAll();
		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
		TaUnite civ = new TaUnite();
		civ.setCodeUnite(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	
	public List<TaEntrepot> entrepotAutoComplete(String query) {
		String numLot="";
		if(selectedTaMouvementStockDTOJSF.getTaLot()!=null) {
			numLot=selectedTaMouvementStockDTOJSF.getTaLot().getNumLot();
		}
        List<TaEntrepot> allValues = taEtatStockService.entrepotLotArticle(selectedTaMouvementStockDTOJSF.getDto().getCodeArticle(),
        		numLot,null,null);
        List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();
		TaEntrepot obj = new TaEntrepot();
		obj.setCodeEntrepot(Const.C_AUCUN);
		filteredValues.add(obj);
        for (int i = 0; i < allValues.size(); i++) {
        	obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaEntrepot> entrepotAutoCompleteDest(String query) {
		String numLot="";
		//		if(selectedTaMouvementStockDTOJSF.getTaLot()!=null) {
		//			numLot=selectedTaMouvementStockDTOJSF.getTaLot().getNumLot();
		//		}

		List<TaEntrepot> allValues = taEntrepotService.selectAll();
		List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();
		TaEntrepot obj = new TaEntrepot();
		obj.setCodeEntrepot(Const.C_AUCUN);
		filteredValues.add(obj);
		for (int i = 0; i < allValues.size(); i++) {
			obj = allValues.get(i);
			if(query.equals("*") || obj.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
				if(selectedTaMouvementStockDTOJSF!=null && selectedTaMouvementStockDTOJSF.getTaEntrepotOrg()!=null 
						&& selectedTaMouvementStockDTOJSF.getTaEntrepotOrg().getCodeEntrepot()!=null) {
					if(!obj.getCodeEntrepot().equals(selectedTaMouvementStockDTOJSF.getTaEntrepotOrg().getCodeEntrepot()))
						filteredValues.add(obj);
				}else 
					filteredValues.add(obj);
			}
		}

		return filteredValues;
	}
	
	public List<String> emplacementAutoCompleteOrg(String query) {
		String numLot="";
		if(selectedTaMouvementStockDTOJSF.getTaLot()!=null)numLot=selectedTaMouvementStockDTOJSF.getTaLot().getNumLot();
		List<String> filteredValues = new ArrayList<String>();
		if(selectedTaMouvementStockDTOJSF!=null && selectedTaMouvementStockDTOJSF.getTaEntrepotOrg()!=null) {
			List<String> allValues = taEtatStockService.emplacementLotArticle(selectedTaMouvementStockDTOJSF.getTaArticle().getCodeArticle(),
					numLot,selectedTaMouvementStockDTOJSF.getTaEntrepotOrg().getCodeEntrepot(),null);


			for (int i = 0; i < allValues.size(); i++) {
				String civ = allValues.get(i);
				if(query.equals("*") || civ.toLowerCase().contains(query.toLowerCase())) {
					filteredValues.add(civ);
				}
			}
		}
		return filteredValues;

	}
	
	public List<String> emplacementAutoCompleteDest(String query) {
		List<String> filteredValues = new ArrayList<String>();
		if(selectedTaMouvementStockDTOJSF!=null && selectedTaMouvementStockDTOJSF.getTaEntrepotDest()!=null){
			List<String> allValues = taEtatStockService.emplacementEntrepot(selectedTaMouvementStockDTOJSF.getTaEntrepotDest().getCodeEntrepot());

			for (int i = 0; i < allValues.size(); i++) {
				String civ = allValues.get(i);
				if(query.equals("*") || civ.toLowerCase().contains(query.toLowerCase())) {
					filteredValues.add(civ);
				}
			}
		}

		        return filteredValues;
	}

	public List<TaMouvementStockDTOJSF> getValuesLigne() {
		return valuesLigne;
	}

	public TaMouvementStockDTOJSF[] getSelectedTaMouvementStockDTOJSFs() {
		return selectedTaMouvementStockDTOJSFs;
	}

	public void setSelectedTaMouvementStockDTOJSFs(
			TaMouvementStockDTOJSF[] selectedTaMouvementStockDTOJSFs) {
		this.selectedTaMouvementStockDTOJSFs = selectedTaMouvementStockDTOJSFs;
	}

	public TaMouvementStockDTOJSF getNewTaMouvementStockDTOJSF() {
		return newTaMouvementStockDTOJSF;
	}

	public void setNewTaMouvementStockDTOJSF(TaMouvementStockDTOJSF newTaMouvementStockDTOJSF) {
		this.newTaMouvementStockDTOJSF = newTaMouvementStockDTOJSF;
	}

	public TaMouvementStockDTOJSF getSelectedTaMouvementStockDTOJSF() {
		return selectedTaMouvementStockDTOJSF;
	}

	public void setSelectedTaMouvementStockDTOJSF(
			TaMouvementStockDTOJSF selectedTaMouvementStockDTOJSF) {
		this.selectedTaMouvementStockDTOJSF = selectedTaMouvementStockDTOJSF;
	}

	public TaArticle getTaArticleLigne() {
		return taArticleLigne;
	}

	public void setTaArticleLigne(TaArticle taArticleLigne) {
		this.taArticleLigne = taArticleLigne;
	}

	public TaEntrepot getTaEntrepotDestLigne() {
		return taEntrepotDestLigne;
	}

	public void setTaEntrepotDestLigne(TaEntrepot taEntrepotDestLigne) {
		this.taEntrepotDestLigne = taEntrepotDestLigne;
	}

	public TaEntrepot getTaEntrepotLigne() {
		return taEntrepotLigne;
	}

	public void setTaEntrepotLigne(TaEntrepot taEntrepotOrgLigne) {
		this.taEntrepotLigne = taEntrepotOrgLigne;
	}

	public TaTypeMouvement getTaTypeMouvement() {
		return taTypeMouvement;
	}

	public void setTaTypeMouvement(TaTypeMouvement taTypeMouvement) {
		this.taTypeMouvement = taTypeMouvement;
	}

	public List<MouvementStocksDTO> getValuesLigneDTO() {
		return valuesLigneDTO;
	}

	public void setValuesLigneDTO(List<MouvementStocksDTO> valuesLigneDTO) {
		this.valuesLigneDTO = valuesLigneDTO;
	}

	public Boolean renderedEcranChangementDepot(){
		if(taTypeMouvement!=null && taTypeMouvement.getCode().equals("D"))
			return true;
		return false;
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

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}
	
	public RemoteCommand getRc() {
		return rc;
	}

	public void setRc(RemoteCommand rc) {
		this.rc = rc;
	}

	public boolean isInsertAuto() {
		return insertAuto;
	}

	public void setInsertAuto(boolean insertAuto) {
		this.insertAuto = insertAuto;
	}

	public boolean isTypeReserve() {
		return typeReserve;
	}

	public void setTypeReserve(boolean typeReserve) {
		this.typeReserve = typeReserve;
	}

	public List<ArticleLotEntrepotDTO> getListArticleLotEntrepot() {
		return listArticleLotEntrepot;
	}

	public void setListArticleLotEntrepot(
			List<ArticleLotEntrepotDTO> listArticleLotEntrepot) {
		this.listArticleLotEntrepot = listArticleLotEntrepot;
	}

//	public void removeLigne(TaMouvementStock ligne) throws Exception {
//			if(masterEntity.getTaMouvementStockes().size()-1>=0 ){
//				masterEntity.getTaMouvementStockes().remove(ligne);
//				reinitialiseNumLignes(0, 1);
//			}
//
//	}
//	
//	public void reinitialiseNumLignes(Integer aPartirDe,int increment){
//		int i=aPartirDe+increment;
//		//int i=aPartirDe;
//		for (TaMouvementStock ligne : masterEntity.getTaMouvementStockes()) {
//			if(ligne.getNumLigneLDocument()>=aPartirDe)
//			{
//				ligne.setNumLigneLDocument(i);
//				i++;
//			}
//		}
//	}
	
//	public TaMouvementStock rechercheLigneNumLigne(int numLigne){
//		TaMouvementStock obj=masterEntityLigne;
//		for (TaMouvementStock ligne : masterEntity.getTaMouvementStockes()) {
//			if(ligne.getNumLigneLDocument()==numLigne)
//				obj=(TaMouvementStock) ligne;
//		}
//		return obj;
//	}
	
	public void selectionLigne(TaMouvementStockDTOJSF ligne){
		selectedTaMouvementStockDTOJSF=ligne;
		if(masterEntity.getIdGrMouvStock()!=0 && selectedTaMouvementStockDTOJSF!=null && selectedTaMouvementStockDTOJSF.getDto()!=null
				&& selectedTaMouvementStockDTOJSF.getDto().getId()!=null)
			masterEntityLigne=rechercheLigne(selectedTaMouvementStockDTOJSF.getDto().getId());;
	}

	public void controleDate(SelectEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp, event.getObject());
		
	}
	public void controleDate(AjaxBehaviorEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp,  ((UIOutput) event.getSource()).getValue());
	}

	public Boolean getGestionLot() {
		return gestionLot;
	}

	public void setGestionLot(Boolean gestionLot) {
		this.gestionLot = gestionLot;
	}
	
	
	public List<TaTitreTransport> titreTransportAutoComplete(String query) {
		List<TaTitreTransport> allValues = taTitreTransportService.selectAll();
		List<TaTitreTransport> filteredValues = new ArrayList<TaTitreTransport>();
		TaTitreTransport civ=new TaTitreTransport();
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTitreTransport().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}
		return filteredValues;
	}
	
	public void onClearTaTitreTransport(AjaxBehaviorEvent event){
		selectedTaMouvementStockDTOJSF.getDto().setCodeTitreTransport(null);
		selectedTaMouvementStockDTOJSF.getDto().setQteTitreTransport(null);
		selectedTaMouvementStockDTOJSF.setTaTitreTransport(null);

	}
	

	public boolean afficheCrd() {
		if(taTypeMouvement==null)return false;
		if(taTypeMouvement.getCrd()!=null) {
			return taTypeMouvement.getCrd();
		}
		return false;
	}
}  
