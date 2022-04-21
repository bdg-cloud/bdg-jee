package fr.legrain.bdg.webapp.articles;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaComportementArticleCompose;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleComposeServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaComportementArticleComposeServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named("nomenclatureArticleController")
@ViewScoped  
public class NomenclatureArticleController extends AbstractController implements Serializable { 
	



	/**
	 * 
	 */
	private static final long serialVersionUID = -6659966775273173566L;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	

	
	private String paramId;
	
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaArticleComposeServiceRemote taArticleComposeService;
	private @EJB ITaUniteServiceRemote taUniteService;
	private @EJB ITaCoefficientUniteServiceRemote taCoefficientUniteService;
	private @EJB ITaComportementArticleComposeServiceRemote taComportementService;
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranPlan = new ModeObjetEcranServeur();
	
	private LgrDozerMapper<TaArticle,TaArticleDTO> mapperModelToUI  = new LgrDozerMapper<TaArticle,TaArticleDTO>();

	
	private DualListModel<TaArticleDTO> listeArticleCompose = new DualListModel<TaArticleDTO>();
	
	List<TaArticleDTO> sourceCompose = new ArrayList<TaArticleDTO>();
	List<TaArticleDTO> selectedArticlesSource = new ArrayList<TaArticleDTO>();
	
	private TaArticle masterEntity;
	
	private TaArticleDTO selectedTaArticleDTO = new TaArticleDTO();
	
	private TaArticleDTO selectedArticleSource = new TaArticleDTO();
	
	private TaArticleComposeDTO selectedTaArticleComposeDTO = new TaArticleComposeDTO();
	List<TaArticleComposeDTO> selectedTaArticlesComposeDTO = new ArrayList<TaArticleComposeDTO>();
	
	private TaArticleDTO taArticleDTO;
	private boolean modeDialogue;
	
	private List<TaComportementArticleCompose> comportements = new ArrayList<TaComportementArticleCompose>();
	
	private TaComportementArticleCompose selectedComportement = new TaComportementArticleCompose();
	
	//private List<TaArticleDTO> values; 
	
	public NomenclatureArticleController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		comportements = taComportementService.selectAll();
		refresh(null);

	}
	public void refresh(){
		refresh(null);
	}

	
	public void refresh(ActionEvent ev){
		if(masterEntity != null && masterEntity.getComportementArticleCompose() != null) {
			selectedComportement = masterEntity.getComportementArticleCompose();
		}else {
			if(!comportements.isEmpty()) {
				selectedComportement = comportements.get(0);
			}
		}
		
		selectedTaArticlesComposeDTO = new ArrayList<TaArticleComposeDTO>();
		selectedArticlesSource = new ArrayList<TaArticleDTO>();
		sourceCompose.clear();
		//remplissage du DTO avec masterEntity
		if(masterEntity != null) {
			mapperModelToUI.map(masterEntity, selectedTaArticleDTO);
			
			//a ce niveau, on doit remplir selectedTaArticleDTO.getNomenclature() avant de continuer (la nomeclature du masterEntity est deja remplie a ce niveau)
			List<TaArticleComposeDTO> nom = taArticleService.findNomenclatureDTOByIdArticle(selectedTaArticleDTO.getId());
			// on vide sa nomenclature avant de la remplir
			selectedTaArticleDTO.getNomenclature().clear();
			selectedTaArticleDTO.getNomenclature().addAll(nom);
			
			//Remplissage de la liste d'articles correspondant a la nomenclature de l'article (pour la picklist)
			List<TaArticleDTO> listeArticlesDTOinclus = new ArrayList<TaArticleDTO>();
			for (TaArticleComposeDTO articleCompose : selectedTaArticleDTO.getNomenclature()) {
				TaArticleDTO articleDTO;
				try {
					articleDTO = taArticleService.findByCodeDTO(articleCompose.getCodeArticle());
					listeArticlesDTOinclus.add(articleDTO);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//on va chercher tous les articles DTO non compose
			List<TaArticleDTO> tousNonCompose = taArticleService.findAllNonCompose();
			//On ajout chaque articles DTO non compose a la liste des articles séléctionnable SAUF l'article courant et les articles déja dans la nomenclature
			if(selectedTaArticleDTO != null ) {
				for (TaArticleDTO taArticleDTO : tousNonCompose) {
					//si l'article non compose n'est pas l'article courant ET si il n'est pas déjà inclus dans la nomenclature
						boolean existeOuCourant = false;
						for (TaArticleDTO articleInclus : listeArticlesDTOinclus) {
							if(articleInclus.getCodeArticle().equals(taArticleDTO.getCodeArticle()) 
								|| taArticleDTO.getCodeArticle().equals( selectedTaArticleDTO.getCodeArticle())) {
								existeOuCourant = true;
							}
						}
						if(!existeOuCourant) {
							sourceCompose.add(taArticleDTO);
						}
					
				}
			}
			
			/////OLD PICKLIST////
//			List<TaArticleDTO> targetCompose = new ArrayList<TaArticleDTO>();
//			//Si la liste d'articles de la nomenclature de l'article courant n'est pas vide, on s'en sert pour remplir la liste des articles déjà séléctionnés
//			if(!listeArticlesDTOinclus.isEmpty()) {
//				targetCompose = new ArrayList<TaArticleDTO>(listeArticlesDTOinclus);
//			}else {
//				targetCompose = new ArrayList<TaArticleDTO>();
//			}
//			//On construit la liste finale pour la picklist
//		    listeArticleCompose = new DualListModel<TaArticleDTO>(sourceCompose, targetCompose);
		}
		
		
	}
	public void changeComportement(AjaxBehaviorEvent event) {
		if(masterEntity != null) {
			if(selectedComportement != null) {
				masterEntity.setComportementArticleCompose(selectedComportement);
				masterEntity = taArticleService.merge(masterEntity);
				masterEntity = taArticleService.setNomenclature(masterEntity);
			}
			
			
			refresh();
		}
		
	}
	public List<String> uniteAutoComplete(TaArticleComposeDTO articleComposeDTO) {
		TaArticle article = null;
		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
		List<String> codesUnite = new ArrayList<String>();
		int idArticle;
		String query = "";
		try {
			if(articleComposeDTO != null) {
				 idArticle = articleComposeDTO.getIdArticle();
			}else {
				 idArticle = selectedTaArticleComposeDTO.getIdArticle();
			}
			article = taArticleService.findById(idArticle);
			if(article != null) {
				List<TaUnite> allValues = taUniteService.findByCodeUniteStock(article.getTaUniteReference().getCodeUnite(), "");
				TaUnite civ=new TaUnite();
				civ.setCodeUnite(Const.C_AUCUN);
				filteredValues.add(civ);
				for (int i = 0; i < allValues.size(); i++) {
					 civ = allValues.get(i);
					if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains("")) {
						filteredValues.add(civ);
					}
				}
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (TaUnite taUnite : filteredValues) {
			codesUnite.add(taUnite.getCodeUnite());
		}
		return codesUnite;
	}
	
	public void onRowEdit(RowEditEvent event) {
		try {
			
			
			TaArticleComposeDTO articleComposeDTO = (TaArticleComposeDTO) event.getObject();
			System.out.println(articleComposeDTO.getQte().toString());
			TaArticleCompose articleCompose = taArticleComposeService.findById(articleComposeDTO.getIdArticleCompose());
			if(articleCompose != null) {
				if(articleComposeDTO.getU1().equals("<vide>")) {
					articleComposeDTO.setU1("");
				}
				if(articleComposeDTO.getU2().equals("<vide>")) {
					articleComposeDTO.setU2("");
				}
				articleCompose.setQte(articleComposeDTO.getQte());
				articleCompose.setQte2(articleComposeDTO.getQte2());
				articleCompose.setU1(articleComposeDTO.getU1());
				articleCompose.setU2(articleComposeDTO.getU2());
				
				taArticleComposeService.merge(articleCompose);
				masterEntity = taArticleService.setNomenclature(masterEntity);
			}
			refresh();
				
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nomenclature", e.getMessage()));	
			context.validationFailed();
			//setInsertAuto(false);
		}
	}
	public void actAjouter() {
		actAjouter(null);
	}
	public void actAjouter(ActionEvent event) {
		for (TaArticleDTO ctrlDTO : selectedArticlesSource) {
			
			TaArticle ctrl;
			try {
				//je récupère ma liste d'articleDTO et je vais créer les articlesCompose correspondant avec leur code
				ctrl = taArticleService.findByCode(ctrlDTO.getCodeArticle());
				TaArticleCompose newArticleCompose = new TaArticleCompose();
				newArticleCompose.setQte(new BigDecimal(1));
				newArticleCompose.setQte2(new BigDecimal(1));
				newArticleCompose.setTaArticle(ctrl);
				newArticleCompose.setTaArticleParent(masterEntity);
				if(ctrl.getTaUnite1()!= null) {
					newArticleCompose.setU1(ctrl.getTaUnite1().getCodeUnite());
					newArticleCompose.setU2(ctrl.getTaUnite1().getCodeUnite());
				}else {
					newArticleCompose.setU1("U");
					newArticleCompose.setU2("U");
				}
				
				// j'ajoute ces articles à la nomenclature de l'article parent
				//selectedTaArticleDTO.getNomenclature().add(e)
				masterEntity.getNomenclature().add(newArticleCompose);
				
				
				
				
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(masterEntity != null) {
			masterEntity = taArticleService.merge(masterEntity);
			masterEntity = taArticleService.setNomenclature(masterEntity);
		}
		
		refresh();
	}
	
	public void onRowCancel(RowEditEvent event) {
//		selectionLigne((LigneJSF) event.getObject());
//		actAnnulerLigne(null);
		refresh();
    }
	
	public void onRowEditInit(RowEditEvent event) {
		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
		DataTable table = (DataTable) evt.getSource();
		int activeRow = table.getRowIndex()+1;
		if(table.getRowCount() == activeRow) {
			//derniere ligne
			//setInsertAuto(modeEcran.dataSetEnInsertion());
		} else {
			//setInsertAuto(false);
		}
//		if(event.getObject()!=null && !event.getObject().equals(selectedLigneJSF))
//			selectedLigneJSF=(LigneJSF) event.getObject();
//		actModifierLigne(null);
		
		//changeModeSaisieCodeBarre();
	}
	
	public void rowSelectCheckBoxSource(SelectEvent event) {
	}
	public void onRowSelectLigne(SelectEvent event) { 
		if(event.getObject()!=null) {
			//selectionLigne((LigneJSF) event.getObject());	
		}
				
	}
	
	
	
	
	

	public void onTransfer(TransferEvent event) {
//        StringBuilder builder = new StringBuilder();
//        for(Object item : event.getItems()) {
//            builder.append(((TaProduit) item).getCode()).append("<br />");
//        }
//         
//        FacesMessage msg = new FacesMessage();
//        msg.setSeverity(FacesMessage.SEVERITY_INFO);
//        msg.setSummary("Items Transferred");
//        msg.setDetail(builder.toString());
//         
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				refresh();
				break;
			case C_MO_EDITION:
							
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Référence article fournisseur", "actAnnuler"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void autoCompleteMapUIToDTO() {

	}
	
	public void autoCompleteMapDTOtoUI() {

	}

    public void updateListeSousProduit() {
    	//on prépare la liste des TaArticleComposeDTO à supprimer en fonction des articleDTO non selectionee 
		List<TaArticleComposeDTO> lasupprimer = new ArrayList<TaArticleComposeDTO>();
		if(selectedTaArticleDTO.getNomenclature()!=null) {
			if(listeArticleCompose.getTarget().isEmpty()) {
				lasupprimer.addAll(selectedTaArticleDTO.getNomenclature());
			}else {
				for (TaArticleComposeDTO articleCompose : selectedTaArticleDTO.getNomenclature()) {
					//si l' article compose de l'article n'est pas present dans la liste target de la picklist, on va le supprimer 
					boolean presentDansPicklist = false;
						for (TaArticleDTO target : listeArticleCompose.getTarget()) {
							if(target.getCodeArticle().equals(articleCompose.getCodeArticle())) {
								//lasupprimer.add(articleCompose);
								presentDansPicklist = true;
							}
							
						}
						if(!presentDansPicklist) {
							lasupprimer.add(articleCompose);
						}
				}
			}
			//supprimer les anciens 
			if(!lasupprimer.isEmpty()) {
				taArticleComposeService.deleteListDTO(lasupprimer);
			}
			for (TaArticleComposeDTO art : lasupprimer) {
				//selectedTaArticleDTO.getNomenclature().removeIf(a -> a.getIdArticleCompose() == art.getIdArticleCompose());
				masterEntity.getNomenclature().removeIf(a -> a.getIdArticleCompose() == art.getIdArticleCompose());
			}
			System.out.println(masterEntity.getNomenclature());
			
			
			
			//ajouter les nouveaux
			List<TaArticleDTO> listeAAjouter = new ArrayList<TaArticleDTO>();

			if(selectedTaArticleDTO.getNomenclature().isEmpty()) {
				listeAAjouter.addAll(listeArticleCompose.getTarget());
			}else {
				for (TaArticleDTO artDTO : listeArticleCompose.getTarget()) {
					boolean existeDeja = false;
					for (TaArticleComposeDTO taArticleComposeDTO : selectedTaArticleDTO.getNomenclature()) {
						if(taArticleComposeDTO.getCodeArticle().equals(artDTO.getCodeArticle()) ) {
							existeDeja = true;
						}
					}
					
					if(!existeDeja) {
						listeAAjouter.add(artDTO);
					}
					
				}
			}
			
			
			for (TaArticleDTO ctrlDTO : listeAAjouter) {
				TaArticle ctrl;
				try {
					//OLD je récupère ma liste d'articleDTO et je vais chercher les articles correspondant avec leur code
					//je récupère ma liste d'articleDTO et je vais créer les articlesCompose correspondant avec leur code
					ctrl = taArticleService.findByCode(ctrlDTO.getCodeArticle());
					TaArticleCompose newArticleCompose = new TaArticleCompose();
					newArticleCompose.setQte(new BigDecimal(1));
					newArticleCompose.setQte2(new BigDecimal(1));
					newArticleCompose.setTaArticle(ctrl);
					newArticleCompose.setTaArticleParent(masterEntity);
					if(ctrl.getTaUnite1()!= null) {
						newArticleCompose.setU1(ctrl.getTaUnite1().getCodeUnite());
						newArticleCompose.setU2(ctrl.getTaUnite1().getCodeUnite());
					}else {
						newArticleCompose.setU1("U");
						newArticleCompose.setU2("U");
					}
					
					// j'ajoute ces articles à la nomenclature de l'article parent
					//selectedTaArticleDTO.getNomenclature().add(e)
					masterEntity.getNomenclature().add(newArticleCompose);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
	  }
    }
    
	public void actEnregistrer(ActionEvent actionEvent) {		
		try {		
			
			updateListeSousProduit();
			
			if(masterEntity != null) {
				masterEntity = taArticleService.merge(masterEntity);
				masterEntity = taArticleService.setNomenclature(masterEntity);
			}
			
			refresh();
			
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0)) {
				//values.add(selectedTaProduitDTO);
			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			//modeEcranSetup.setMode(EnumModeObjet.C_MO_CONSULTATION);
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Articles", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur pendant l'enregistrement de la nomenclature de l'article", e.getMessage()));
		}
	}
	
	
	

	public void actInserer(ActionEvent actionEvent) {

	}
	
	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {
		
		try {

		
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Référence article fournisseur", "actModifier"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
	
	   
    public void nouveau(ActionEvent actionEvent) {  

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
	
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		try {
			
			if(selectedTaArticlesComposeDTO != null || !selectedTaArticlesComposeDTO.isEmpty()) {
				taArticleComposeService.deleteListDTO(selectedTaArticlesComposeDTO);
				for (TaArticleComposeDTO taArticleComposeDTO : selectedTaArticlesComposeDTO) {
					masterEntity.getNomenclature().removeIf(a -> a.getIdArticleCompose().equals(taArticleComposeDTO.getIdArticleCompose()));
				}
				if(masterEntity != null) {
					masterEntity = taArticleService.merge(masterEntity);
					masterEntity = taArticleService.setNomenclature(masterEntity);
				}
				
				refresh();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nomenclature ", e.getCause().getCause().getCause().getCause().getMessage()));
		} 	    
	}
	
	public void actSupprimerLigne(TaArticleComposeDTO article) {
		List<TaArticleComposeDTO> liste = new ArrayList<TaArticleComposeDTO>();
		try {
			if(article != null) {
				liste.add(article);
				//taArticleComposeService.removeDTO(article);
				taArticleComposeService.deleteListDTO(liste);
				masterEntity.getNomenclature().removeIf(a -> a.getIdArticleCompose().equals(article.getIdArticleCompose()));
				if(masterEntity != null) {
					masterEntity = taArticleService.merge(masterEntity);
					masterEntity = taArticleService.setNomenclature(masterEntity);
				}
				
				refresh();
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nomenclature ", e.getCause().getCause().getCause().getCause().getMessage()));
		} 	    
	}
	
	public TaCoefficientUnite recupCoefficientUnite(String code1, String code2) {
		try {
			TaCoefficientUnite coef = null;
			if(code1!=null && code2!=null && code1.equals(code2)) {
				//unite 1 = unite 2 => rapport "1"
				coef = new TaCoefficientUnite();
				coef.setUniteA(taUniteService.findByCode(code1));
				coef.setUniteB(coef.getUniteA());
				coef.setCoeffUniteAVersB(new BigDecimal(1));
				coef.setCoeffUniteBVersA(new BigDecimal(1));
				coef.setCoeff(new BigDecimal(1));
				coef.setNbDecimale(2);
				coef.setNbDecimaleAVersB(2);
				coef.setNbDecimaleBVersA(2);
				coef.setOperateurDivise(false);
				coef.setOperateurAVersBDivise(false);
				coef.setOperateurBVersADivise(false);
			} else {
				coef = taCoefficientUniteService.findByCode(code1,code2);
			}
			if(coef!=null) {
				coef.recupRapportEtSens(code1,code2);
			}
			return coef;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean validateUIField(String nomChamp,Object value, TaArticleComposeDTO articleComposeDTO) {

		boolean changement=false;
		//TaArticleCompose articleCompose = null;
		TaArticle article = null;
		if(articleComposeDTO != null) {
			try {
				//articleCompose = taArticleComposeService.findById(articleComposeDTO.getIdArticleCompose());
				article = taArticleService.findById(articleComposeDTO.getIdArticle());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		if(article != null) {
			try {				
				if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
					BigDecimal qte=BigDecimal.ZERO;
					if(value!=null){
						if(!value.equals("")){
							qte=(BigDecimal)value;
						}
						
						TaCoefficientUnite coef = recupCoefficientUnite(articleComposeDTO.getU1(),articleComposeDTO.getU2());
						if(coef!=null) {
							if(coef.getOperateurDivise()) 
								articleComposeDTO.setQte2((qte).divide(coef.getCoeff()
										,MathContext.DECIMAL128).setScale(coef.getNbDecimale(),BigDecimal.ROUND_HALF_UP));
							else articleComposeDTO.setQte2((qte).multiply(coef.getCoeff()));
						}else  {
							articleComposeDTO.setQte2(BigDecimal.ZERO);
							//masterEntityLigne.setQte2LDocument(null);
						}

					} else {
						//masterEntityLigne.setQte2LDocument(null);
						articleComposeDTO.setQte2(null);
					}
					articleComposeDTO.setQte2(qte);
//					masterEntityLigne.setQteLDocument(qte);
//					selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
//					selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());

				} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
					TaUnite entity =null;
					if(value!=null){
						if(value instanceof TaUnite){
							entity=(TaUnite) value;
							entity = taUniteService.findByCode(entity.getCodeUnite());
						}else{
							entity = taUniteService.findByCode((String)value);
						}
						if(entity != null) {
							articleComposeDTO.setU1(entity.getCodeUnite());
						}else {
							articleComposeDTO.setU1("");
						}
						
						//masterEntityLigne.setU1LDocument(entity.getCodeUnite());
					}else {
						articleComposeDTO.setU1("");
						//masterEntityLigne.setU1LDocument(null);
					}
					TaCoefficientUnite coef = recupCoefficientUnite(articleComposeDTO.getU1(),articleComposeDTO.getU2());
					validateUIField(Const.C_QTE_L_DOCUMENT, articleComposeDTO.getQte(), articleComposeDTO);

				} else if(nomChamp.equals(Const.C_QTE2_L_DOCUMENT)) {
					BigDecimal qte=BigDecimal.ZERO;
					if(value==null) {
						articleComposeDTO.setQte2(null);
					}else if(!value.equals("")){
						qte=(BigDecimal)value;
					}
					articleComposeDTO.setQte2(qte);

				}else if(nomChamp.equals(Const.C_U2_L_DOCUMENT)) {
					TaUnite entity =null;
					if(value!=null && !value.equals("")){
						if(value instanceof TaUnite){
							entity=(TaUnite) value;
							entity = taUniteService.findByCode(entity.getCodeUnite());
						}else{
							entity = taUniteService.findByCode((String)value);
						}
						if(entity != null) {
							articleComposeDTO.setU2(entity.getCodeUnite());
						}else {
							articleComposeDTO.setU2("");
						}
						
						//selectedLigneJSF.getDto().setU2LDocument(entity.getCodeUnite());
					}else {
						articleComposeDTO.setU2("");
						//masterEntityLigne.setU2LDocument(null);
						articleComposeDTO.setQte2(null);	
					}
					TaCoefficientUnite coef = recupCoefficientUnite(articleComposeDTO.getU1(),articleComposeDTO.getU2());
					//selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
					validateUIField(Const.C_QTE_L_DOCUMENT, articleComposeDTO.getQte(), articleComposeDTO);
				} 		
				return false;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		return false;
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

	
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Référence article fournisseur", "actImprimer"));
		}
		try {
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    


	
	public void onRowSimpleSelect(SelectEvent event) {

	} 
	
	public void updateTab(){
		try {

		autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		
		updateTab();
		

	} 

	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	

	public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "enregistrer":
//				if(!selectedTaStripeProductDTO.estVide())
					retour = true;
				break;
				case "annuler":
				case "fermer":
					retour = false;
					break;
				default:
					break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "enregistrer":
//				if(!selectedTaStripeProductDTO.estVide())
					retour = false;
				break;
			case "annuler":
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
				//if(selectedTaStripeProductDTO!=null && selectedTaStripeProductDTO.getId()!=0 ) retour = false;
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

	public void validateLignesNomenclature(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String msg = "";
		try {
			TaArticleComposeDTO articleComposeDTO = (TaArticleComposeDTO) component.getAttributes().get("articleComposeDTO");
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			validateUIField(nomChamp,value, articleComposeDTO);

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		TaArticleComposeDTO articleComposeDTO = (TaArticleComposeDTO) event.getComponent().getAttributes().get("articleComposeDTO");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
		}
		
		validateUIField(nomChamp,value,articleComposeDTO);
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


	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}
	
	

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle masterEntity) {
		this.masterEntity = masterEntity;
	}


	

	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}

	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}

	public boolean isModeDialogue() {
		return modeDialogue;
	}

	public void setModeDialogue(boolean modeDialogue) {
		this.modeDialogue = modeDialogue;
	}

	public DualListModel<TaArticleDTO> getListeArticleCompose() {
		return listeArticleCompose;
	}

	public void setListeArticleCompose(DualListModel<TaArticleDTO> listeArticleCompose) {
		this.listeArticleCompose = listeArticleCompose;
	}

//	public List<TaArticleDTO> getValues() {
//		return values;
//	}
//
//	public void setValues(List<TaArticleDTO> values) {
//		this.values = values;
//	}

	public TaArticleDTO getSelectedTaArticleDTO() {
		return selectedTaArticleDTO;
	}

	public void setSelectedTaArticleDTO(TaArticleDTO selectedTaArticleDTO) {
		this.selectedTaArticleDTO = selectedTaArticleDTO;
	}

	public TaArticleComposeDTO getSelectedTaArticleComposeDTO() {
		return selectedTaArticleComposeDTO;
	}

	public void setSelectedTaArticleComposeDTO(TaArticleComposeDTO selectedTaArticleComposeDTO) {
		this.selectedTaArticleComposeDTO = selectedTaArticleComposeDTO;
	}

	public List<TaArticleDTO> getSourceCompose() {
		return sourceCompose;
	}

	public void setSourceCompose(List<TaArticleDTO> sourceCompose) {
		this.sourceCompose = sourceCompose;
	}

	public TaArticleDTO getSelectedArticleSource() {
		return selectedArticleSource;
	}

	public void setSelectedArticleSource(TaArticleDTO selectedArticleSource) {
		this.selectedArticleSource = selectedArticleSource;
	}

	public List<TaArticleDTO> getSelectedArticlesSource() {
		return selectedArticlesSource;
	}

	public void setSelectedArticlesSource(List<TaArticleDTO> selectedArticlesSource) {
		this.selectedArticlesSource = selectedArticlesSource;
	}

	public List<TaArticleComposeDTO> getSelectedTaArticlesComposeDTO() {
		return selectedTaArticlesComposeDTO;
	}

	public void setSelectedTaArticlesComposeDTO(List<TaArticleComposeDTO> selectedTaArticlesComposeDTO) {
		this.selectedTaArticlesComposeDTO = selectedTaArticlesComposeDTO;
	}

	

	public TaComportementArticleCompose getSelectedComportement() {
		return selectedComportement;
	}

	public void setSelectedComportement(TaComportementArticleCompose selectedComportement) {
		this.selectedComportement = selectedComportement;
	}

	public List<TaComportementArticleCompose> getComportements() {
		return comportements;
	}

	public void setComportements(List<TaComportementArticleCompose> comportements) {
		this.comportements = comportements;
	}

//	public List<TaArticleDTO> getListeArticleCompose() {
//		return listeArticleCompose;
//	}
//
//	public void setListeArticleCompose(List<TaArticleDTO> listeArticleCompose) {
//		this.listeArticleCompose = listeArticleCompose;
//	}

	


}  
