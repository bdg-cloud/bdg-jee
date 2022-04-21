package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.dto.TaModeleFabricationDTO;
import fr.legrain.article.dto.TaTFabricationDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLModeleFabricationMP;
import fr.legrain.article.model.TaLModeleFabricationPF;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaModeleFabrication;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTFabrication;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaModeleFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.EtiquetteCodeBarreBean;
import fr.legrain.bdg.webapp.app.LeftBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.document.dto.ITaLFabrication;
import fr.legrain.document.dto.ITaLModeleFabrication;
import fr.legrain.document.dto.TaLModeleFabricationDTO;
import fr.legrain.document.model.SWTLigneDocument;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.stock.model.TaMouvementStock;


@Named
@ViewScoped  
public class ModeleFabricationController extends AbstractController implements Serializable { 
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	@Inject @Named(value="etiquetteCodeBarreBean")
	private EtiquetteCodeBarreBean etiquetteCodeBarreBean;
	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="leftBean")
	private LeftBean leftBean;
	
	private Integer qteModele = 1;

	private TaModeleFabrication masterEntity;
	private TaLModeleFabricationMP masterEntityLigneMP;
	private TaLModeleFabricationPF masterEntityLignePF;
	
	private TaLModeleFabricationDTOJSF selectedTaMatierePremiere;
	private TaLModeleFabricationDTOJSF selectedTaProduit;
	private TaModeleFabricationDTO selectedTaModeleFabricationDTO;
	private TaModeleFabricationDTO[] selectedTaModeleFabricationDTOs;

	private List<TaLModeleFabricationDTOJSF> valuesLigneMP;
	private List<TaLModeleFabricationDTOJSF> valuesLignePF;
	
	private boolean insertAutoMP = true;
	private boolean insertAutoPF = true;

	private List<TaModeleFabricationDTO> listeFabrication;
	
	private TaTFabrication taTFabrication;
	private TaTFabricationDTO taTFabricationDTO;

	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
	private List<ArticleLotEntrepotDTO> listArticleLotEntrepot;

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranMatierePremiere = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranProduit = new ModeObjetEcranServeur();

	protected @EJB ITaCoefficientUniteServiceRemote taCoefficientUniteService;
	private @EJB ITaModeleFabricationServiceRemote modeleFabricationService;
	private @EJB ITaFabricationServiceRemote fabricationService;
//	private @EJB ITaLModeleFabricationServiceRemote taLModeleFabricationService;
	private @EJB ITaTFabricationServiceRemote taTFabricationService;
	private @EJB ITaLotServiceRemote lotService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaMouvementStockServiceRemote mouvementService;
	private @EJB ITaEntrepotServiceRemote entrepotService;
	private @EJB ITaArticleServiceRemote articleService;
	private @EJB ITaTypeMouvementServiceRemote typeMouvementService;
	private @EJB ITaUniteServiceRemote uniteService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
	
	private LgrDozerMapper<TaModeleFabricationDTO,TaModeleFabrication> mapperUIToModel  = new LgrDozerMapper<TaModeleFabricationDTO,TaModeleFabrication>();
	private LgrDozerMapper<TaModeleFabrication,TaModeleFabricationDTO> mapperModelToUI  = new LgrDozerMapper<TaModeleFabrication,TaModeleFabricationDTO>();
	private LgrDozerMapper<TaTFabrication,TaTFabricationDTO> mapperModelToUITFabrication  = new LgrDozerMapper<TaTFabrication,TaTFabricationDTO>();
	
	

	public ModeleFabricationController() {  

	}  

	@PostConstruct
	public void postConstruct(){
		rechercheAvecCommentaire(true);
		
		refresh();
	}

	public void refresh(){
		listeFabrication = modeleFabricationService.selectAllDTO();
		valuesLigneMP = IHMmodelMP();
		valuesLignePF = IHMmodelPF();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public List<TaLModeleFabricationDTOJSF> IHMmodelMP() {
		LinkedList<TaLModeleFabricationMP> ldao = new LinkedList<TaLModeleFabricationMP>();
		LinkedList<TaLModeleFabricationDTOJSF> lswt = new LinkedList<TaLModeleFabricationDTOJSF>();

		if(masterEntity!=null && masterEntity.getLignesMatierePremieres() !=null && !masterEntity.getLignesMatierePremieres().isEmpty()) {
			masterEntity.reinitialiseNumLignesMP(0, 1);
			ldao.addAll(masterEntity.getLignesMatierePremieres());

			LgrDozerMapper<TaLModeleFabricationMP,TaLModeleFabricationDTO> mapper = new LgrDozerMapper<TaLModeleFabricationMP,TaLModeleFabricationDTO>();
			TaLModeleFabricationDTO dto = null;
			TaArticleDTO artDTO = null;
			for (TaLModeleFabricationMP ligne : ldao) {
				if(!ligne.ligneEstVide()){
				TaLModeleFabricationDTOJSF t = new TaLModeleFabricationDTOJSF();
//				try {
//					TaLot lot = taLotService.findByCode(o.getNumLot());
//					o.setTaLot(lot);
				dto = (TaLModeleFabricationDTO) mapper.map(ligne, TaLModeleFabricationDTO.class);
				t.setDto(dto);
//				t.setTaLot(ligne.getTaLot());
				if(t.getArticleLotEntrepotDTO()==null) {
					t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
				}
//				if(ligne.getTaLot()!=null) {
//					t.getArticleLotEntrepotDTO().setNumLot(ligne.getTaLot().getNumLot());
//				}
				t.getDto().setLibelleLot(ligne.getLibLDocument());
				if(ligne.getTaMouvementStock()!=null) {
					t.getDto().setEmplacement(ligne.getTaMouvementStock().getEmplacement());
					t.getDto().setU1LDocument(ligne.getTaMouvementStock().getUn1Stock());
					t.getDto().setU2LDocument(ligne.getTaMouvementStock().getUn2Stock());
				}
				if(ligne.getTaEntrepot()!=null) {
					t.getDto().setCodeEntrepot(ligne.getTaEntrepot().getCodeEntrepot());
				}
//				t.setTaLot(lot);
				t.setTaArticle(ligne.getTaArticle());
				artDTO = new TaArticleDTO();
				artDTO.setCodeArticle(ligne.getTaArticle().getCodeArticle());
				t.setTaArticleDTO(artDTO);
				if(ligne.getTaArticle()!=null) {
					t.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
				}
				if(ligne.getU1LDocument()!=null) {
					t.setTaUnite1(new TaUnite());
					t.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
				}
				if(ligne.getU2LDocument()!=null) {
					t.setTaUnite2(new TaUnite());
					t.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
				}
				t.setTaEntrepot(ligne.getTaEntrepot());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				lswt.add(t);
				}
			}

		}
		return lswt;
	}
	
	public List<TaLModeleFabricationDTOJSF> IHMmodelPF() {
		LinkedList<TaLModeleFabricationPF> ldao = new LinkedList<TaLModeleFabricationPF>();
		LinkedList<TaLModeleFabricationDTOJSF> lswt = new LinkedList<TaLModeleFabricationDTOJSF>();

		if(masterEntity!=null && masterEntity.getLignesProduits()!=null && !masterEntity.getLignesProduits().isEmpty()) {
			masterEntity.reinitialiseNumLignesPF(0, 1);
			ldao.addAll(masterEntity.getLignesProduits());

			LgrDozerMapper<TaLModeleFabricationPF,TaLModeleFabricationDTO> mapper = new LgrDozerMapper<TaLModeleFabricationPF,TaLModeleFabricationDTO>();
			TaLModeleFabricationDTO dto = null;
			TaArticleDTO artDTO = null;
			for (TaLModeleFabricationPF ligne : ldao) {
				if(!ligne.ligneEstVide()){
				TaLModeleFabricationDTOJSF t = new TaLModeleFabricationDTOJSF();
//				try {
//					TaLot lot = taLotService.findByCode(o.getNumLot());
//					o.setTaLot(lot);
				dto = (TaLModeleFabricationDTO) mapper.map(ligne, TaLModeleFabricationDTO.class);
				
				dto.setEmplacement(ligne.getEmplacementLDocument());
				
				t.setDto(dto);
//				t.setTaLot(ligne.getTaLot());
//				if(ligne.getTaLot()!=null) {
//					t.getDto().setNumLot(ligne.getTaLot().getNumLot());
//					t.getDto().setLibelleLot(ligne.getTaLot().getLibelle());
//				}
//				t.setTaLot(lot);
				t.setTaArticle(ligne.getTaArticle());
				artDTO = new TaArticleDTO();
				artDTO.setCodeArticle(ligne.getTaArticle().getCodeArticle());
				t.setTaArticleDTO(artDTO);
				if(ligne.getTaArticle()!=null) {
					t.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
				}
				if(ligne.getU1LDocument()!=null) {
					t.setTaUnite1(new TaUnite());
					t.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
				}
				if(ligne.getU2LDocument()!=null) {
					t.setTaUnite2(new TaUnite());
					t.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
				}
				t.setTaEntrepot(ligne.getTaEntrepot());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				lswt.add(t);
				}
			}

		}
		return lswt;
	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MODELE_FABRICATION);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MODELE_FABRICATION);
		b.setTitre("Modèle de fabrication");
		//b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setTemplate("solstyce/modele_fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MODELE_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MODELE_FABRICATION);
		b.setParamId("0");
		leftBean.setExpandedForce(false);

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		//actInserer(actionEvent);
		actInserer();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Modèle de fabrication", 
					"Nouveau"
					)); 
		}
	} 
	
	public boolean etatBouton(String bouton) {
		return etatBouton(bouton,modeEcran);
	}
	
//	public boolean etatBoutonMatierePremiere(String bouton) {
//		boolean retour = true;
//		switch (modeEcranMatierePremiere.getMode()) {
//		case C_MO_INSERTION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			case "majNumLot":
//				retour = false;
//			default:
//				break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = modeEcran.dataSetEnModif()?false:true;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			case "supprimer":
//				retour = true;
//				break;
//			default:
//				//retour = false;
//				break;
//			}
//			break;
//		case C_MO_CONSULTATION:
//			switch (bouton) {
//			case "inserer":
//			case "fermer":
//				retour = false;
//				break;
//			case "supprimerListe":retour = false;break;	
//			case "supprimer":
//			case "modifier":
//			case "imprimer":
//				if(selectedTaModeleFabricationDTO!=null && selectedTaModeleFabricationDTO.getId()!=null  && selectedTaModeleFabricationDTO.getId()!=0 ) retour = false;
//				break;	
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			default:
//				break;
//			}
//			break;
//		default:
//			break;
//		}
//
//		return retour;
//	}
//	
//	public boolean etatBoutonProduit(String bouton) {
//		boolean retour = true;
//		switch (modeEcranProduit.getMode()) {
//		case C_MO_INSERTION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			case "majNumLot":
//				retour = false;
//			default:
//				break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = modeEcran.dataSetEnModif()?false:true;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			case "supprimer":
//				retour = true;
//				break;
//			default:
//				//retour = false;
//				break;
//			}
//			break;
//		case C_MO_CONSULTATION:
//			switch (bouton) {
//			case "supprimer":
//			case "modifier":
//			case "inserer":
//			case "imprimer":
//			case "fermer":
//				retour = modeEcran.dataSetEnModif()?false:true;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			default:
//				break;
//			}
//			break;
//		default:
//			break;
//		}
//
//		return retour;
//	}
	
	public boolean etatBoutonMatierePremiere(String bouton) {
		boolean retour = true;
		if(modeEcran.dataSetEnModif()) {
			retour = false;
		}
		switch (modeEcranMatierePremiere.getMode()) {
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
			case "majNumLot":
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
				retour = modeEcran.dataSetEnModif()?false:true;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			case "supprimer":
				retour = true;
				break;
			default:
				//retour = false;
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
			case "affecterMP":
				retour = modeEcran.dataSetEnModif()?false:true;
				break;
			case "rowEditor":	
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			case "majNumLot":
				retour = false;
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

	
	public boolean etatBoutonProduit(String bouton) {
		boolean retour = true;
		if(modeEcran.dataSetEnModif()) {
			retour = false;
		}
		switch (modeEcranProduit.getMode()) {
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
				retour = modeEcran.dataSetEnModif()?false:true;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			case "supprimer":
				retour = true;
				break;
			default:
				//retour = false;
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
				retour = modeEcran.dataSetEnModif()?false:true;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			case "majNumLot":
				retour = false;
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
	public boolean etatBouton(String bouton, ModeObjetEcranServeur mode) {
		boolean retour = true;
		switch (mode.getMode()) {
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
			case "majNumLot":
				retour = false;
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
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = false;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			case "supprimer":
			case "modifier":
				if(selectedTaModeleFabricationDTO!=null && selectedTaModeleFabricationDTO.getId()!=null  && selectedTaModeleFabricationDTO.getId()!=0 ) retour = false;
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

	//************************************************************************************************************//
	//	Fabrication
	//************************************************************************************************************//
	public void actEnregistrer(){  
		try {
			//Mapping vers les entités
			autoCompleteMapUIToDTO();
			validateDocumentAvantEnregistrement(selectedTaModeleFabricationDTO);
			mapperUIToModel.map(selectedTaModeleFabricationDTO, masterEntity);
			
			masterEntity.setLegrain(false);
			
			if(masterEntity.getDateFinR()==null) {
				masterEntity.setDateFinR(masterEntity.getDateDebR());
			}
			if(masterEntity.getDateDebT()==null) {
				masterEntity.setDateDebT(masterEntity.getDateDebR());
			}
			if(masterEntity.getDateFinT()==null) {
				masterEntity.setDateFinT(masterEntity.getDateDebR());
			}
			
			//supression des lignes vides
			List<TaLModeleFabricationMP> listeLigneVideMp = new ArrayList<TaLModeleFabricationMP>(); 
			for (TaLModeleFabricationMP ligne : masterEntity.getLignesMatierePremieres()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide()) {
					listeLigneVideMp.add(ligne);
				}
			}
			for (TaLModeleFabricationMP taLBonReception : listeLigneVideMp) {
				masterEntity.getLignesMatierePremieres().remove(taLBonReception);
			}
			List<TaLModeleFabricationPF> listeLigneVidePf = new ArrayList<TaLModeleFabricationPF>(); 
			for (TaLModeleFabricationPF ligne : masterEntity.getLignesProduits()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide()) {
					listeLigneVidePf.add(ligne);
				}
			}
			for (TaLModeleFabricationPF taLBonReception : listeLigneVidePf) {
				masterEntity.getLignesProduits().remove(taLBonReception);
			}
			
//			for (TaLModeleFabricationPF lignePF : masterEntity.getLignesProduits()) {
//				lignePF.setTypeLDocument("PF");
//			}
//			for (TaLModeleFabricationMP ligneMP : masterEntity.getLignesMatierePremieres()) {
//				ligneMP.setTypeLDocument("MP");
//			}
			
			for (ITaLModeleFabrication l : masterEntity.getLignes()) {
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
				l.setTaMouvementStock(null);
			}
			
			/*
			 * Gérer les mouvements corrrespondant aux lignes 
			 */
			//Création du groupe de mouvement
//			TaGrMouvStock grpMouvStock = new TaGrMouvStock();
//			if(masterEntity.getTaGrMouvStock()!=null) {
//				grpMouvStock=masterEntity.getTaGrMouvStock();
//			}
//			grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeDocument());
//			grpMouvStock.setDateGrMouvStock(masterEntity.getDateDebR());
//			grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument());
//			grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("F"));
//			masterEntity.setTaGrMouvStock(grpMouvStock);
//			
//			for (TaLModeleFabrication l : masterEntity.getLignes()) {
//				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
//				if(l.getTaMouvementStock()!=null)
//					l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
//			}
//			
//			//Création des lignes de mouvement
//			for (TaLModeleFabrication ligne : masterEntity.getLignes()) {
////				if(ligne.getTaMouvementStock()==null){
//				TaMouvementStock mouv = new TaMouvementStock();
//				if(ligne.getTaMouvementStock()!=null)mouv=ligne.getTaMouvementStock();
//				mouv.setLibelleStock(ligne.getLibLDocument());
//				mouv.setDateStock(masterEntity.getDateDocument());
//				mouv.setEmplacement(ligne.getEmplacementLDocument());
//				mouv.setTaEntrepot(ligne.getTaEntrepot());
//				if(ligne.getTaLot()!=null){
//					mouv.setNumLot(ligne.getTaLot().getNumLot());
////					m.setTaLot(taLotService.merge(ligne.getTaLot(),"LOT"));
//				}
//				//mouv.setQte1Stock(ligne.getQteLDocument());
//				//mouv.setQte2Stock(ligne.getQte2LDocument());
//				mouv.setUn1Stock(ligne.getU1LDocument());
//				mouv.setUn2Stock(ligne.getU2LDocument());
//				mouv.setTaGrMouvStock(grpMouvStock);
////				ligne.setTaLot(null);
//				ligne.setTaMouvementStock(mouv);
//				
//				grpMouvStock.getTaMouvementStockes().add(mouv);
////				}
//			}
//			
//			//fusionner les 2 listes dans l'entité en évitant les doublons
//			for (TaLModeleFabrication lignePF : masterEntity.getLignesProduits()) {
//				if(lignePF.getQteLDocument()!=null) {
//					lignePF.getTaMouvementStock().setQte1Stock(lignePF.getQteLDocument().abs());
//				}
//				if(lignePF.getQte2LDocument()!=null) {
//					lignePF.getTaMouvementStock().setQte2Stock(lignePF.getQte2LDocument().abs());
//				}
//				lignePF.getTaMouvementStock().setTaLot(null);;
//			}
//			for (TaLModeleFabrication ligneMP : masterEntity.getLignesMatierePremieres()) {
//				if(ligneMP.getQteLDocument()!=null) {
//					ligneMP.getTaMouvementStock().setQte1Stock(ligneMP.getQteLDocument().abs().negate());
//				}
//				if(ligneMP.getQte2LDocument()!=null) {
//					ligneMP.getTaMouvementStock().setQte2Stock(ligneMP.getQte2LDocument().abs().negate());
//				}
//				ligneMP.getTaMouvementStock().setTaLot(null);
//			}
			
			masterEntity = modeleFabricationService.merge(masterEntity,ITaModeleFabricationServiceRemote.validationContext);
			masterEntity=modeleFabricationService.findById(masterEntity.getIdDocument());
			masterEntity.findProduit();
			//mapping vers l'IHM
			mapperModelToUI.map(masterEntity, selectedTaModeleFabricationDTO);
			selectedTaModeleFabricationDTO.setId(masterEntity.getIdDocument());
			selectedTaModeleFabricationDTOs=new TaModeleFabricationDTO[]{selectedTaModeleFabricationDTO};
			
			valuesLigneMP = IHMmodelMP();
			valuesLignePF = IHMmodelPF();
			
			autoCompleteMapDTOtoUI();

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
//				listeFabrication.add(new TaModeleFabricationMapper().mapEntityToDto(masterEntity, null));
				listeFabrication.add(selectedTaModeleFabricationDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Fabrication", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));
		}
	}
	
	public void actAnnuler() {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedTaModeleFabricationDTO.getCodeDocument()!=null) {
					modeleFabricationService.annuleCode(selectedTaModeleFabricationDTO.getCodeDocument());
				}
				masterEntity=new TaModeleFabrication();
				mapperModelToUI.map(masterEntity,selectedTaModeleFabricationDTO );
				if(listeFabrication.size()>0)selectedTaModeleFabricationDTO = listeFabrication.get(0);
				else selectedTaModeleFabricationDTO=new TaModeleFabricationDTO();
				onRowSelect(null);
				
				valuesLigneMP = IHMmodelMP();
				valuesLignePF = IHMmodelPF();
				break;
			case C_MO_EDITION:
				if(selectedTaModeleFabricationDTO.getId()!=null && selectedTaModeleFabricationDTO.getId()!=0){
					masterEntity = modeleFabricationService.findById(selectedTaModeleFabricationDTO.getId());
					selectedTaModeleFabricationDTO=modeleFabricationService.findByIdDTO(selectedTaModeleFabricationDTO.getId());
					//isa le 14/01/2016
					mapperModelToUI.map(masterEntity,selectedTaModeleFabricationDTO );
					masterEntity.findProduit();
					valuesLigneMP = IHMmodelMP();
					valuesLignePF = IHMmodelPF();
					//
				}				
				break;

			default:
				break;
			}			
				
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
		masterEntity.findProduit();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actAnnuler")); 
		}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void actFermer() {
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			actAnnuler();
			break;
		case C_MO_EDITION:
			actAnnuler();							
			break;

		default:
			break;
		}
		 selectedTaModeleFabricationDTO=new TaModeleFabricationDTO();
		 selectedTaModeleFabricationDTOs=new TaModeleFabricationDTO[]{selectedTaModeleFabricationDTO};
		updateTab();
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeModeleFabrication').filter()");
	}

	public void actInserer() {
		
		selectedTaModeleFabricationDTO = new TaModeleFabricationDTO();
		
		masterEntity = new TaModeleFabrication();
		masterEntity.findProduit();
		
		valuesLignePF = IHMmodelPF();
		valuesLigneMP = IHMmodelMP();
		autoCompleteMapDTOtoUI();
		
		selectedTaModeleFabricationDTO.setDateDebR(new Date());

		try {
			selectedTaModeleFabricationDTO.setCodeDocument(modeleFabricationService.genereCode(null));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		masterEntity.setLignesMatierePremieres(new ArrayList<TaLModeleFabricationMP>());
		masterEntity.setLignesProduits(new ArrayList<TaLModeleFabricationPF>());
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		scrollToTop();
	}
	
	public void actModifier() {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void supprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		try {
			if(selectedTaModeleFabricationDTO!=null && selectedTaModeleFabricationDTO.getId()!=null){
				masterEntity = modeleFabricationService.findById(selectedTaModeleFabricationDTO.getId());
			}
			masterEntity.findProduit();
//			for (TaLModeleFabrication ligne : masterEntity.getLignesMatierePremieres()) {
//				ligne.setTaLot(null);
//			}
			
			modeleFabricationService.remove(masterEntity);
			listeFabrication.remove(selectedTaModeleFabricationDTO);

			if(!listeFabrication.isEmpty()) {
				selectedTaModeleFabricationDTO = listeFabrication.get(0);
				selectedTaModeleFabricationDTOs=new TaModeleFabricationDTO[]{selectedTaModeleFabricationDTO};
			}else{
				selectedTaModeleFabricationDTO = new TaModeleFabricationDTO();
				selectedTaModeleFabricationDTOs=new TaModeleFabricationDTO[]{};
			}
			updateTab();
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Bon de réception", "modèle supprimé."));
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Modèle Fabrication", e.getMessage()));
		}	    
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimer")); 
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("fabrication", modeleFabricationService.findById(selectedTaModeleFabricationDTO.getId()));
			
			// - Dima - Début
			/** methodes du test pur les "chemins" dans l'edition /
			TaModeleFabrication tmp = new TaModeleFabrication();
			tmp = fabricationService.findById(selectedTaModeleFabricationDTO.getId());
			
			List<TaLModeleFabrication> lf = new ArrayList<TaLModeleFabrication>();
			lf = tmp.getLignesProduits();
			
			for(TaLModeleFabrication f : lf){
				f.getEmplacementLDocument();
				f.getTaMouvementStock().getEmplacement();
				
				f.getTaEntrepot().getCodeEntrepot();
				f.getTaEntrepot().getLibelle();
				
			}
			**/
			// - Dima -  Fin
			
			System.out.println("FabricationController.actImprimer()");
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}    
	
	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
		
		etiquetteCodeBarreBean.videListe();
		try {
			for (TaLModeleFabricationPF l : masterEntity.getLignesProduits()) {
				etiquetteCodeBarreBean.getListeArticle().add(l.getTaArticle());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Bon de réception", "actImprimerEtiquetteCB")); 
		}
	}
	
	public void actImprimerListeModeleFabrications(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeModeleFabrications", listeFabrication);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	//************************************************************************************************************//
	//	Matière Premiere
	//************************************************************************************************************//

	public List<ArticleLotEntrepotDTO> onChangeListArticleMP(String codeArticle, String numlot) {
		try {
			
			listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
			//listArticleLotEntrepot = mouvementService.getEtatStockByArticle((article.split("~"))[0]);
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_AUTORISER_UTILISATION_LOTS_NON_CONFORME);
			Boolean utiliserLotNonConforme = null;
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				utiliserLotNonConforme = LibConversion.StringToBoolean(p.getValeur());
			}
			
			listArticleLotEntrepot = mouvementService.getEtatStockByArticle(codeArticle,utiliserLotNonConforme);
			List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
			List<BigDecimal> qte = new ArrayList<BigDecimal>();
			for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){
	
				for(TaLModeleFabricationMP mp : masterEntity.getLignesMatierePremieres()){
					TaMouvementStock mouv = mp.getTaMouvementStock();
	
//						TaLot lot =lotService.findByCode(mouv.getNumLot());
//						mouv.setTaLot(lot);
					if(mouv.getEmplacement()!=null 
							&& mouv.getEmplacement().equals(dto.getEmplacement()) 
							&& mouv.getTaEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot()!=null 
							&& mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot()) 
							&& mouv.getTaLot().getTaArticle().getCodeArticle().equals(codeArticle)  
							&& ((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot())
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
				listArticleLotEntrepot.remove(dtoTemp);
				dtoTemp.setQte1(dtoTemp.getQte1().subtract(qte.get(i)));
				i++;
				listArticleLotEntrepot.add(dtoTemp);
	
			}
			
			List<ArticleLotEntrepotDTO> filteredValues = new ArrayList<ArticleLotEntrepotDTO>();

			for (int j = 0; j < listArticleLotEntrepot.size(); j++) {
				ArticleLotEntrepotDTO ale = listArticleLotEntrepot.get(j);
				if(numlot.equals("*") || ale.getNumLot().toLowerCase().contains(numlot.toLowerCase())) {
					filteredValues.add(ale);
				}
			}
			listArticleLotEntrepot = filteredValues;
			return listArticleLotEntrepot;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listArticleLotEntrepot;
	}

	public void actEnregistrerMatierePremiere() {

		try {
			selectedTaMatierePremiere.updateDTO();
			
			masterEntityLigneMP.setTaArticle(selectedTaMatierePremiere.getTaArticle());
			masterEntityLigneMP.setTaEntrepot(selectedTaMatierePremiere.getTaEntrepot());
//			masterEntityLigneMP.setTaLot(selectedTaMatierePremiere.getTaLot());
			
			LgrDozerMapper<TaLModeleFabricationDTO,TaLModeleFabricationMP> mapper = new LgrDozerMapper<TaLModeleFabricationDTO,TaLModeleFabricationMP>();
			mapper.map((TaLModeleFabricationDTO) selectedTaMatierePremiere.getDto(),masterEntityLigneMP);
			
			masterEntity.enregistreLigne(masterEntityLigneMP);
//			if(!masterEntity.getLignes().contains(masterEntityLigneMP)) {
//				masterEntity.addLigne(masterEntityLigneMP);
//			}
			if(!masterEntity.getLignesMatierePremieres().contains(masterEntityLigneMP)) {
				masterEntity.getLignesMatierePremieres().add(masterEntityLigneMP);
			}
			modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
			//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
			//		actInsererLigne(null);
	
			//        FacesMessage msg = new FacesMessage("Ligne Edited", ((TaLBonReceptionDTOJSF) event.getObject()).getIdLDocument().toString());
			//        FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actEnregisterMatierePremiere")); 
		}

	}

	public void actSupprimerMatierePremiere() {
		try {
			TaLModeleFabricationMP l = null;
			Boolean trouve = valuesLigneMP.remove(selectedTaMatierePremiere);
			if(trouve){
				for (TaLModeleFabricationMP var : masterEntity.getLignesMatierePremieres()) {
					if(selectedTaMatierePremiere.getDto().getNumLigneLDocument()!=null && (var.getNumLigneLDocument()==selectedTaMatierePremiere.getDto().getNumLigneLDocument())) {
						l = var;
					}
				}
				if(l!=null) {
					//				masterEntity.getLignesMatierePremieres().remove(l);
					removeLigneMP(l);
					valuesLigneMP=IHMmodelMP();
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int calcNextNumLigneDoc() {
		int num = -1;
		List<Integer> l = new ArrayList<>();
		for (TaLModeleFabricationDTOJSF lf : valuesLigneMP) {
			l.add(lf.getDto().getNumLigneLDocument());
		}
		for (TaLModeleFabricationDTOJSF lf : valuesLignePF) {
			l.add(lf.getDto().getNumLigneLDocument());
		}
		if(!l.isEmpty()) {
			num = Collections.max(l);
//			Collections.sort(l);
//			num = l.get(l.size() - 1);
		} else {
			return 1;
		}
		return num+1;
	}

	public void actInsererMatierePremiere(ActionEvent actionEvent) {
//		masterEntityLigneMP = new TaLModeleFabrication();
//		masterEntityLigneMP.setTaMouvementStock(new TaMouvementStock());
//
//		initOrigineMouvement(masterEntityLigneMP);
		
//		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_INSERTION);
		/***********************************************************************************************************************************************************/
		
		//		if(selectedTaLBonReceptionDTOJSF==null || !selectedTaLBonReceptionDTOJSF.isAutoInsert()) {
		TaLModeleFabricationDTOJSF nouvelleLigne = new TaLModeleFabricationDTOJSF();
		nouvelleLigne.setDto(new TaLModeleFabricationDTO());
//		nouvelleLigne.getDto().setNumLigneLDocument(valuesLigneMP.size()+valuesLignePF.size()+1);
//		nouvelleLigne.getDto().setNumLigneLDocument(calcNextNumLigneDoc());
		nouvelleLigne.getDto().setNumLigneLDocument(valuesLigneMP.size()+1);
//		nouvelleLigne.getDto().setNumLot(selectedTaBonReceptionDTO.getCodeDocument()+"_"+valuesLigne.size()+1);
		nouvelleLigne.getDto().setNumLot(selectedTaModeleFabricationDTO.getCodeDocument()+"_"+nouvelleLigne.getDto().getNumLigneLDocument());
		
		//			nouvelleLigne.setAutoInsert(true);

		masterEntityLigneMP = new TaLModeleFabricationMP(true); 
		try {
			//H = HT => pas nécessaire, mais il ne faut pas de ligne de commentaire  pour passer trigger "ligne_vide"
			masterEntityLigneMP.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
		
			//List<TaLBonReceptionDTOJSF> modelListeLigneDevis = IHMmodel();

			masterEntity.getLignesMatierePremieres().add(masterEntityLigneMP);
			masterEntity.addLigne(masterEntityLigneMP);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		selectedTaModeleFabricationDTO.setNumLigneLDocument(masterEntityLigneMP.getNumLigneLDocument());
		masterEntityLigneMP.setTaDocument(masterEntity);
		masterEntityLigneMP.initialiseLigne(true);
		
		initOrigineMouvement(masterEntityLigneMP);

		valuesLigneMP.add(nouvelleLigne);
		
//		l'affectation du selected avec nouvelleLigne est importante pour
//		la création sur modèle -> donc je l'ai remis
		
		selectedTaMatierePremiere = nouvelleLigne;

		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_INSERTION);
		//		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actInsererMatierePremiere")); 
		}
	}

	public void initOrigineMouvement(ITaLModeleFabrication l) {
		if(l.getTaMouvementStock()==null) {
			l.setTaMouvementStock(new TaMouvementStock());
		}
		l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
	}

	public void actModifierMatierePremiere () {
		
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_EDITION);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actModifierMatierePremiere"));
		}
		
//		article = selectedTaMatierePremiere.getTaLot().getTaArticle().getCodeArticle() + "~" + selectedTaMatierePremiere.getTaLot().getTaArticle().getLibellecArticle();
//		listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
//		listArticleLotEntrepot = mouvementService.getEtatStockByArticle((article.split("~"))[0]);
//		List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
//		List<BigDecimal> qte = new ArrayList<BigDecimal>();
//		for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){
//
//			for(TaLModeleFabrication mp : masterEntity.getLignesMatierePremieres()){
//				if (mp.getTaMouvementStock().getIdMouvementStock() != selectedTaMatierePremiere.getTaMouvementStock().getIdMouvementStock()){
//					TaMouvementStock mouv = mp.getTaMouvementStock();
//					if(mouv.getEmplacement().equals(dto.getEmplacement()) 
//							&& mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot() ) 
//							&& mouv.getTaLot().getTaArticle().getCodeArticle().equals((article.split("~"))[0])  
//							&& ((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot()) 
//							&& mouv.getUn1Stock().equals(dto.getCodeUnite())
//							) {
//						temp.add(dto);
//						qte.add(mouv.getQte1Stock()) ;
//					}
//				}
//			}
//
//		}
//
//		TaLModeleFabrication mouv = selectedTaMatierePremiere;
//		for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){
//			if(mouv.getTaMouvementStock().getEmplacement().equals(dto.getEmplacement()) 
//					&& mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot() ) 
//					&& mouv.getTaLot().getTaArticle().getCodeArticle().equals((article.split("~"))[0])  
//					&& ((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot()) 
//					&& mouv.getTaMouvementStock().getUn1Stock().equals(dto.getCodeUnite())
//					){
//				listArticleLotEntrepot.remove(dto);
//				dto.setQteSelec(mouv.getTaMouvementStock().getQte1Stock());
//				listArticleLotEntrepot.add(dto);
//			}
//		}
//
//		actSupprimerMatierePremiere();
//		selectedTaMatierePremiere = new TaLModeleFabricationDTOJSF();
	}

	public void actAnnulerMatierePremiere(){
		List<TaLModeleFabricationMP> lmp = masterEntity.getLignesMatierePremieres();
//		if(masterEntityLigneMP == null 
//				|| (masterEntityLigneMP.getTaMouvementStock()!=null && masterEntityLigneMP.getTaMouvementStock().getIdMouvementStock() != 0)){
//			lmp.add(masterEntityLigneMP);
//		}

		masterEntity.setLignesMatierePremieres(lmp);
		masterEntityLigneMP = new TaLModeleFabricationMP();
		
		valuesLigneMP = IHMmodelMP();
		
		setInsertAutoMP(false);
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	//************************************************************************************************************//
	//	Produit
	//************************************************************************************************************//
	
	public List<String> emplacementAutoComplete(String query) {
		List<String> filteredValues = new ArrayList<String>();
		if(selectedTaProduit.getTaEntrepot()!=null) {
//		if(selectedTaProduit.getDto().getCodeEntrepot()!=null) {
			List<String> allValues = taEtatStockService.emplacementEntrepot(selectedTaProduit.getTaEntrepot().getCodeEntrepot(),null);
			
			for (int i = 0; i < allValues.size(); i++) {
				String civ = allValues.get(i);
				if(civ !=null && (query.equals("*") || civ.toLowerCase().contains(query.toLowerCase()))) {
					filteredValues.add(civ);
				}
			}
		}
		return filteredValues;
	}

//	public void onChangeListArticleP() {
//		TaArticle art = null;
//		try {
//			if(articlePF!=null && !articlePF.equals("")){
//				art = articleService.findByCode((articlePF.split("~"))[0]);
//
//				if(art!=null && art.getTaRapportUnite()!=null) {
//					articleUnite1 = art.getTaRapportUnite().getTaUnite1().getCodeUnite();
//				} else {
//					//pas d'unité
//					articleUnite1= "";
//					FacesContext context = FacesContext.getCurrentInstance();  
//					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fabrication", 
//							"Cet article ne possède pas d'unité"
//							)); 
//				}
//			} else {
//				articleUnite1= "";
//			}
//		} catch (FinderException e) {
//			e.printStackTrace();
//
//		}
//	}

//	public void onChangeListEntrepotP() {
//		listEmplacement = new ArrayList<String>();
//		listEmplacement.clear();
//		listEmplacement = mouvementService.emplacementParEntrepot((entrepot.split("~"))[0]);
//	}

	public void actModifierProduit () {
		
		modeEcranProduit.setMode(EnumModeObjet.C_MO_EDITION);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actModifierProduit")); 
		}
		
//		articlePF = selectedTaProduit.getTaLot().getTaArticle().getCodeArticle() + "~" + selectedTaProduit.getTaLot().getTaArticle().getLibellecArticle();
//
//		TaMouvementStock mouv = selectedTaProduit.getTaMouvementStock();
//
//		commentaireLot = mouv.getDescription();
//		lot = mouv.getTaLot().getNumLot().substring(masterEntity.getCodeDocument().length());
//		qte1 = mouv.getQte1Stock();
//		articleUnite1 = mouv.getUn1Stock();
//		entrepot = mouv.getTaEntrepot().getCodeEntrepot()+"~"+mouv.getTaEntrepot().getLibelle();
//		emplacement = mouv.getEmplacement();
//		actSupprimerProduit();
//		selectedTaProduit = new TaLModeleFabricationDTOJSF();
	}

	public void actSupprimerProduit () {
		try {
			TaLModeleFabricationPF l = null;
			Boolean trouve = valuesLignePF.remove(selectedTaProduit);
			if(trouve){
				for (TaLModeleFabricationPF var : masterEntity.getLignesProduits()) {
					if(selectedTaProduit.getDto().getNumLigneLDocument()!=null && (var.getNumLigneLDocument()==selectedTaProduit.getDto().getNumLigneLDocument())) {
						l = var;
					}
				}
				if(l!=null) {
					//				masterEntity.getLignesProduits().remove(l);
					removeLignePF(l);
					valuesLignePF=IHMmodelPF();
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrerProduit() {	

		try {
			selectedTaProduit.updateDTO();
			
			masterEntityLignePF.setTaArticle(selectedTaProduit.getTaArticle());
			masterEntityLignePF.setTaEntrepot(selectedTaProduit.getTaEntrepot());
			masterEntityLignePF.setEmplacementLDocument(selectedTaProduit.getDto().getEmplacement());
			
			LgrDozerMapper<TaLModeleFabricationDTO,TaLModeleFabricationPF> mapper = new LgrDozerMapper<TaLModeleFabricationDTO,TaLModeleFabricationPF>();
			mapper.map((TaLModeleFabricationDTO) selectedTaProduit.getDto(),masterEntityLignePF);

			if(selectedTaProduit.getDto()!=null ) {
				//Pas de génération de lot pour les modèles de fabrication
//				TaLot lot = new TaLot();
//				if(selectedTaProduit.getTaLot()!=null)lot=selectedTaProduit.getTaLot();
//				if(selectedTaProduit.getDto().getDluo()!=null) {
//					lot.setDluo(selectedTaProduit.getDto().getDluo());
//				}
//				lot.setNumLot(selectedTaProduit.getDto().getNumLot());
//				lot.setLibelle(selectedTaProduit.getDto().getLibelleLot());
//				selectedTaProduit.getDto().setLibLDocument(selectedTaProduit.getDto().getLibelleLot());
//				lot.setUnite1(masterEntityLignePF.getU1LDocument());
//				lot.setUnite2(masterEntityLignePF.getU2LDocument());
//				lot.setTaArticle(masterEntityLignePF.getTaArticle());
//				if(selectedTaProduit.getTaRapport()!=null){
//					lot.setNbDecimal(selectedTaProduit.getTaRapport().getNbDecimal());
//					lot.setSens(selectedTaProduit.getTaRapport().getSens());
//					lot.setRapport(selectedTaProduit.getTaRapport().getRapport());
//				}
//				masterEntityLignePF.setTaLot(lot);
			}
			
			masterEntity.enregistreLigne(masterEntityLignePF);
//			if(!masterEntity.getLignes().contains(masterEntityLignePF)) {
//				masterEntity.addLigne(masterEntityLignePF);	
//			}
			if(!masterEntity.getLignesProduits().contains(masterEntityLignePF)) {
				masterEntity.getLignesProduits().add(masterEntityLignePF);
			}
			modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
			//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
			//		actInsererLigne(null);
	
			//        FacesMessage msg = new FacesMessage("Ligne Edited", ((TaLBonReceptionDTOJSF) event.getObject()).getIdLDocument().toString());
			//        FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actEnregisterProduit")); 
		}
	}

	public void actInsererProduit(ActionEvent actionEvent) {

//		if(selectedTaLBonReceptionDTOJSF==null || !selectedTaLBonReceptionDTOJSF.isAutoInsert()) {
		TaLModeleFabricationDTOJSF nouvelleLigne = new TaLModeleFabricationDTOJSF();
		nouvelleLigne.setDto(new TaLModeleFabricationDTO());
//		nouvelleLigne.getDto().setNumLigneLDocument(valuesLigneMP.size()+valuesLignePF.size()+1);
//		nouvelleLigne.getDto().setNumLigneLDocument(calcNextNumLigneDoc());
		nouvelleLigne.getDto().setNumLigneLDocument(valuesLignePF.size()+1);
		nouvelleLigne.getDto().setNumLot(selectedTaModeleFabricationDTO.getCodeDocument()+"_"+(valuesLignePF.size()+1));
//		nouvelleLigne.getDto().setNumLot(lotService.genereCode());
		
		//			nouvelleLigne.setAutoInsert(true);

		masterEntityLignePF = new TaLModeleFabricationPF(true); 
		try {
			//H = HT => pas nécessaire, mais il ne faut pas de ligne de commentaire  pour passer trigger "ligne_vide"
			masterEntityLignePF.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
		
			//List<TaLBonReceptionDTOJSF> modelListeLigneDevis = IHMmodel();

			masterEntity.getLignesProduits().add(masterEntityLignePF);
			masterEntity.addLigne(masterEntityLignePF);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}

		selectedTaModeleFabricationDTO.setNumLigneLDocument(masterEntityLignePF.getNumLigneLDocument());
		masterEntityLignePF.setTaDocument(masterEntity);
		masterEntityLignePF.initialiseLigne(true);
		
		initOrigineMouvement(masterEntityLignePF);

		valuesLignePF.add(nouvelleLigne);
		
//		l'affectation du selected avec nouvelleLigne est importante pour
//		la création sur modèle -> donc je l'ai remis
		selectedTaProduit = nouvelleLigne;

		modeEcranProduit.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	
	public void actAnnulerProduit () {
		
		List<TaLModeleFabricationPF> lmp = masterEntity.getLignesProduits();
//		if(masterEntityLignePF == null || (masterEntityLignePF.getTaMouvementStock()!=null && masterEntityLignePF.getTaMouvementStock().getIdMouvementStock() != 0)){
//			lmp.add(masterEntityLignePF);
//		}

		masterEntity.setLignesProduits(lmp);
		masterEntityLignePF = new TaLModeleFabricationPF();
		
		valuesLignePF = IHMmodelPF();
		
		setInsertAutoPF(false);
		modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
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

	public void onRowSimpleSelect(SelectEvent event) {

		try {
			if(pasDejaOuvert()==false){
				onRowSelect(event);
	
				autoCompleteMapDTOtoUI();
				valuesLigneMP = IHMmodelMP();
				valuesLignePF = IHMmodelPF();
	
				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Fabrication", 
							"Chargement de fabrication N°"+selectedTaModeleFabricationDTO.getCodeDocument()
							)); 
				}
			} else {
				tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_FABRICATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	public void updateTab(){
		try {
			
			masterEntity=null;
			
			if(selectedTaModeleFabricationDTOs!=null && selectedTaModeleFabricationDTOs.length>0) {
				selectedTaModeleFabricationDTO = selectedTaModeleFabricationDTOs[0];
			}
			if(selectedTaModeleFabricationDTO.getId()!=null && selectedTaModeleFabricationDTO.getId()!=0) {
				masterEntity = modeleFabricationService.findById(selectedTaModeleFabricationDTO.getId());
			}

			//séparer en 2 listes dans l'entité les lignes de fabrication
			if(masterEntity!=null){
				if(masterEntity.getTaTFabrication()!=null)
					System.out.println("A voir si ça fonctionne avec : "+masterEntity.getCodeDocument()+" - "+masterEntity.getTaTFabrication().getCodeTFabrication());
				
				masterEntity.findProduit();

				//			masterEntity = taBonReceptionService.findById(selectedTaBonReceptionDTO.getId());
				valuesLigneMP = IHMmodelMP();
				valuesLignePF = IHMmodelPF();
				if(masterEntity.getTaTFabrication()!=null)
					System.out.println("A voir si ça fonctionne avec : "+masterEntity.getCodeDocument()+" - "+masterEntity.getTaTFabrication().getCodeTFabrication());
				autoCompleteMapDTOtoUI();

				mapperModelToUI.map(masterEntity, selectedTaModeleFabricationDTO);
			}
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Fabrication", 
						"Chargement de fabrication N°"+selectedTaModeleFabricationDTO.getCodeDocument()
						)); 
			}
		
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_MODELE_FABRICATION);
		b.setTitre("Modèle de fabrication");
		b.setTemplate("solstyce/modele_fabrication.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_FABRICATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);

		updateTab();
		scrollToTop();
	} 
	
	public void onRowSelectLignePF(SelectEvent event) {  
		selectionLignePF((TaLModeleFabricationDTOJSF) event.getObject());		
	}
	
	public void selectionLignePF(TaLModeleFabricationDTOJSF ligne){
		selectedTaProduit=ligne;
		if(masterEntity.getIdDocument()!=0 && selectedTaProduit!=null && selectedTaProduit.getDto().getIdLDocument()!=null)
			masterEntityLignePF=rechercheLignePF(selectedTaProduit.getDto().getIdLDocument());
		else if(selectedTaProduit!=null && selectedTaProduit.getDto().getNumLigneLDocument()!=null /*&&  selectedTaProduit.getDto().getNumLigneLDocument()!=0*/) {
			masterEntityLignePF = rechercheLignePFNumLigne(selectedTaProduit.getDto().getNumLigneLDocument());
		}
	}

	public void onRowEditPF(RowEditEvent event) {
		try {
			selectedTaProduit.updateDTO();
			if(selectedTaProduit.getDto().getIdLDocument()!=null &&  selectedTaProduit.getDto().getIdLDocument()!=0) {
				masterEntityLignePF=rechercheLignePF(selectedTaProduit.getDto().getIdLDocument());
			}
			validateLigneFabricationAvantEnregistrement(selectedTaProduit);
			actEnregistrerProduit();
			setInsertAutoPF(modeEcran.dataSetEnInsertion());
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));	
			context.validationFailed();
			//RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
			setInsertAutoPF(false);
			//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
		}
	}

	public void onRowCancelPF(RowEditEvent event) {
		//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
		//    FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
		//    FacesContext.getCurrentInstance().addMessage(null, msg);

		actAnnulerProduit();
	}

	public void onRowEditInitPF(RowEditEvent event) {
		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
		DataTable table = (DataTable) evt.getSource();
		int activeRow = table.getRowIndex()+1;
		if(table.getRowCount() == activeRow) {
			//derniere ligne
			setInsertAutoPF(modeEcran.dataSetEnInsertion());
		} else {
			setInsertAutoPF(false);
		}
		if(event.getObject()!=null && !event.getObject().equals(selectedTaProduit))
			selectedTaProduit=(TaLModeleFabricationDTOJSF) event.getObject();
		actModifierProduit();
	}
	
	public TaLModeleFabricationMP rechercheLigneMPNumLigne(int numLigne){
		TaLModeleFabricationMP obj=masterEntityLigneMP;
		for (TaLModeleFabricationMP ligne : masterEntity.getLignesMatierePremieres()) {
			if(ligne.getNumLigneLDocument()==numLigne)
				obj=ligne;
		}
		return obj;
	}
	public TaLModeleFabricationPF rechercheLignePFNumLigne(int numLigne){
		TaLModeleFabricationPF obj=masterEntityLignePF;
		for (TaLModeleFabricationPF ligne : masterEntity.getLignesProduits()) {
			if(ligne.getNumLigneLDocument()==numLigne)
				obj=ligne;
		}
		return obj;
	}
	
	public TaLModeleFabricationPF rechercheLignePF(int id){
		TaLModeleFabricationPF obj=masterEntityLignePF;
		for (TaLModeleFabricationPF ligne : masterEntity.getLignesProduits()) {
			if(ligne.getIdLDocument()==id)
				obj=ligne;
		}
		return obj;
	}
	
	public void onRowSelectLigneMP(SelectEvent event) {  
		selectionLigneMP((TaLModeleFabricationDTOJSF) event.getObject());		
	}
	
	public void selectionLigneMP(TaLModeleFabricationDTOJSF ligne){
		selectedTaMatierePremiere=ligne;
		if(masterEntity.getIdDocument()!=0 && selectedTaMatierePremiere!=null && selectedTaMatierePremiere.getDto().getIdLDocument()!=null)
			masterEntityLigneMP=rechercheLigneMP(selectedTaMatierePremiere.getDto().getIdLDocument());
		else if(selectedTaMatierePremiere!=null && selectedTaMatierePremiere.getDto().getNumLigneLDocument()!=null /*&&  selectedTaProduit.getDto().getNumLigneLDocument()!=0*/) {
			masterEntityLigneMP = rechercheLigneMPNumLigne(selectedTaMatierePremiere.getDto().getNumLigneLDocument());
		}
	}

	public void onRowEditMP(RowEditEvent event) {
		try {
			selectedTaMatierePremiere.updateDTO();
			if(selectedTaMatierePremiere.getDto().getIdLDocument()!=null &&  selectedTaMatierePremiere.getDto().getIdLDocument()!=0) {
				masterEntityLigneMP=rechercheLigneMP(selectedTaMatierePremiere.getDto().getIdLDocument());
			}
			validateLigneFabricationAvantEnregistrement(selectedTaMatierePremiere);
			actEnregistrerMatierePremiere();
			setInsertAutoPF(modeEcran.dataSetEnInsertion());
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));	
			context.validationFailed();
			//RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
			setInsertAutoMP(false);
			//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
		}
	}

	public void onRowCancelMP(RowEditEvent event) {
		//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
		//    FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
		//    FacesContext.getCurrentInstance().addMessage(null, msg);

		actAnnulerMatierePremiere();
	}

	public void onRowEditInitMP(RowEditEvent event) {
		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
		DataTable table = (DataTable) evt.getSource();
		int activeRow = table.getRowIndex()+1;
		if(table.getRowCount() == activeRow) {
			//derniere ligne
			setInsertAutoMP(modeEcran.getMode()==EnumModeObjet.C_MO_INSERTION);
		} else {
			setInsertAutoMP(false);
		}
		if(event.getObject()!=null && !event.getObject().equals(selectedTaMatierePremiere))
			selectedTaMatierePremiere=(TaLModeleFabricationDTOJSF) event.getObject();
		actModifierMatierePremiere();
	}
	
	public TaLModeleFabricationMP rechercheLigneMP(int id){
		TaLModeleFabricationMP obj=masterEntityLigneMP;
		for (TaLModeleFabricationMP ligne : masterEntity.getLignesMatierePremieres()) {
			if(ligne.getIdLDocument()==id)
				obj=ligne;
		}
		return obj;
	}
	
	public void validateLigneFabricationAvantEnregistrement(Object value) throws ValidatorException {

		String msg = "";

		try {
//			taLFabricationService.validateDTOProperty(((TaLModeleFabricationDTOJSF)value).getDto(),Const.C_CODE_ARTICLE,  ITaLModeleFabricationServiceRemote.validationContext );
//			taLFabricationService.validateDTOProperty(((TaLModeleFabricationDTOJSF)value).getDto(),Const.C_NUM_LOT,  ITaLModeleFabricationServiceRemote.validationContext );
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void actAutoInsererLignePF(ActionEvent actionEvent) /*throws Exception*/ {
		boolean existeDeja=false;
		if(insertAutoPF) {
			for (TaLModeleFabricationDTOJSF ligne : valuesLignePF) {
				if(ligne.ligneEstVide())existeDeja=true;
			}
			if(!existeDeja){
			actInsererProduit(actionEvent);
			
			String oncomplete="jQuery('.myclassModelePF .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
			PrimeFaces.current().executeScript(oncomplete);
			}
		} else {
//			throw new Exception();
		}
	}
	
	public void actAutoInsererLigneMP(ActionEvent actionEvent) /*throws Exception*/ {
		boolean existeDeja=false;
		if(insertAutoMP) {
			for (TaLModeleFabricationDTOJSF ligne : valuesLigneMP) {
				if(ligne.ligneEstVide())existeDeja=true;
			}
			if(!existeDeja){
			actInsererMatierePremiere(actionEvent);
			
			String oncomplete="jQuery('.myclassModeleMP .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
			PrimeFaces.current().executeScript(oncomplete);
			}
		} else {
//			throw new Exception();
		}
	}
	//************************************************************************************************************//
	//	Commun
	//************************************************************************************************************//


	public void setMasterEntity(TaModeleFabrication fabrication) {
		this.masterEntity = fabrication;
	}

	public void setSelectedTaMatierePremiere(
			TaLModeleFabricationDTOJSF selectedTaMatierePremiere) {
		this.selectedTaMatierePremiere = selectedTaMatierePremiere;
	}

	public void setSelectedTaProduit(TaLModeleFabricationDTOJSF selectedTaProduit) {
		this.selectedTaProduit = selectedTaProduit;
	}

	public void setMasterEntityLigneMP(TaLModeleFabricationMP newTaMatierePremiere) {
		this.masterEntityLigneMP = newTaMatierePremiere;
	}

	public void setMasterEntityLignePF(TaLModeleFabricationPF newTaProduit) {
		this.masterEntityLignePF = newTaProduit;
	}

	public TaModeleFabrication getMasterEntity() {
		return masterEntity;
	}

	public TaLModeleFabricationDTOJSF getSelectedTaMatierePremiere() {
		return selectedTaMatierePremiere;
	}

	public TaLModeleFabricationDTOJSF getSelectedTaProduit() {
		return selectedTaProduit;
	}

	public TaLModeleFabricationMP getMasterEntityLigneMP() {
		return masterEntityLigneMP;
	}

	public TaLModeleFabricationPF getMasterEntityLignePF() {
		return masterEntityLignePF;
	}

	public List<ArticleLotEntrepotDTO> getListArticleLotEntrepot() {
		return listArticleLotEntrepot;
	}

	public void setListArticleLotEntrepot(List<ArticleLotEntrepotDTO> listArticleLotEntrepot) {
		this.listArticleLotEntrepot = listArticleLotEntrepot;
	}

	public List<TaModeleFabricationDTO> getListeFabrication() {
		return listeFabrication;
	}

	public TaModeleFabricationDTO getSelectedTaModeleFabricationDTO() {
		return selectedTaModeleFabricationDTO;
	}

	public void setSelectedTaModeleFabricationDTO(
			TaModeleFabricationDTO selectedTaModeleFabricationDTO) {
		this.selectedTaModeleFabricationDTO = selectedTaModeleFabricationDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public ModeObjetEcranServeur getModeEcranMatierePremiere() {
		return modeEcranMatierePremiere;
	}

	public ModeObjetEcranServeur getModeEcranProduit() {
		return modeEcranProduit;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public EtiquetteCodeBarreBean getEtiquetteCodeBarreBean() {
		return etiquetteCodeBarreBean;
	}

	public void setEtiquetteCodeBarreBean(
			EtiquetteCodeBarreBean etiquetteCodeBarreBean) {
		this.etiquetteCodeBarreBean = etiquetteCodeBarreBean;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}
	
	public List<TaArticle> articleAutoComplete(String query) {
		List<TaArticle> allValues = articleService.selectAll();
		List<TaArticle> filteredValues = new ArrayList<TaArticle>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticle civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = articleService.findByCodeLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaEntrepot> entrepotAutoComplete(String query) {
		List<TaEntrepot> allValues = entrepotService.selectAll();
		List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();
		TaEntrepot civ = new TaEntrepot();
		civ.setCodeEntrepot(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeEntrepot().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaUnite> uniteAutoComplete(String query) {
		List<TaUnite> allValues = uniteService.selectAll();
		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
		TaUnite civ = new TaUnite();
		civ.setCodeUnite(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeUnite().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<ArticleLotEntrepotDTO> lotAutoComplete(String query) {
		
		return onChangeListArticleMP(selectedTaMatierePremiere.getTaArticle().getCodeArticle(),query);
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("FabricationController.autcompleteSelection() : "+nomChamp);

		if(value!=null) {
		if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//		if(value instanceof TaTFabricationDTO && ((TaTFabricationDTO) value).getCodeTFabrication()!=null && ((TaTFabricationDTO) value).getCodeTFabrication().equals(Const.C_AUCUN))value=null;	
		if(value instanceof TaEntrepotDTO && ((TaEntrepotDTO) value).getCodeEntrepot()!=null && ((TaEntrepotDTO) value).getCodeEntrepot().equals(Const.C_AUCUN))value=null;	
		if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
		}
		
		validateUIField(nomChamp,value);
	}
	
	public void autcompleteSelectionMP(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("FabricationController.autcompleteSelectionMP() : "+nomChamp);

		validateUIFieldMP(nomChamp,value);
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTFabricationDTO!=null) {
			validateUIField(Const.C_CODE_T_FABRICATION,taTFabricationDTO);
			selectedTaModeleFabricationDTO.setCodeTFabrication(taTFabricationDTO.getCodeTFabrication());
		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTFabrication = null;
			taTFabricationDTO = null;
			System.out.println("A voir si ça fonctionne avec : "+selectedTaModeleFabricationDTO.getCodeTFabrication());
			if(selectedTaModeleFabricationDTO.getCodeTFabrication()!=null && !selectedTaModeleFabricationDTO.getCodeTFabrication().equals("")) {

				taTFabrication = taTFabricationService.findByCode(selectedTaModeleFabricationDTO.getCodeTFabrication());
				taTFabricationDTO = mapperModelToUITFabrication.map(taTFabrication, TaTFabricationDTO.class);
			}
		
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}


	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
//				TaTiers entity = null;
//				if(value!=null){
//					entity=(TaTiers) value;
//					entity = tiersService.findByCode(entity.getCodeTiers());
//				}
//				masterEntity.setTaTiers(entity);
			} if(nomChamp.equals(Const.C_CODE_T_FABRICATION)) {
				TaTFabrication entity = null;
				if(value!=null){
					if(value instanceof TaTFabricationDTO){
//						entity=(TaTiers) value;
						entity = taTFabricationService.findByCode(((TaTFabricationDTO)value).getCodeTFabrication());
					}else{
						entity = taTFabricationService.findByCode((String)value);
					}
					masterEntity.setTaTFabrication(entity);
					changement=!entity.equalsCode(masterEntity.getTaTFabrication());
				}else {
					changement=masterEntity.getTaTFabrication()!=null;
					masterEntity.setTaTFabrication(null);
					selectedTaModeleFabricationDTO.setCodeTFabrication("");
					selectedTaModeleFabricationDTO.setLiblTFabrication("");
					taTFabricationDTO=null;
					
				}
				
				if(changement){
					Map<String, String> params = new LinkedHashMap<String, String>();
					if(masterEntity!=null && masterEntity.getTaTFabrication()!=null)
						params.put(Const.C_CODETYPE, masterEntity.getTaTFabrication().getCodeTFabrication());						
					if(selectedTaModeleFabricationDTO.getCodeDocument()!=null) {
						fabricationService.annuleCode(selectedTaModeleFabricationDTO.getCodeDocument());
					}
					String newCode=fabricationService.genereCode(params);
					if(!newCode.equals(""))selectedTaModeleFabricationDTO.setCodeDocument(newCode);					
				}
			
			}else if(nomChamp.equals(Const.C_NUM_LOT)) {
				
			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
				
			}
			else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				boolean changementArticleLigne = false;
//				TaArticle entity = null;
//				if(value!=null){
//					if(value instanceof TaArticle){
//						entity=(TaArticle) value;
//						entity = articleService.findByCode(entity.getCodeArticle());
//					}else{
//						entity = articleService.findByCode((String)value);
//					}
//				}
				TaArticle entity = null;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = articleService.findByCode(((TaArticleDTO) value).getCodeArticle());
					}else{
						entity = articleService.findByCode((String)value);
					}
				}
				if(selectedTaProduit.getTaArticle()== null || selectedTaProduit.getTaArticle().getIdArticle()!=entity.getIdArticle()) {
					changementArticleLigne = true;
				}
				selectedTaProduit.setTaArticle(entity);
//				if(changementArticleLigne) {
					selectedTaProduit.getDto().setLibLDocument(entity.getLibellecArticle());
//				selectedTaProduit.getDto().setLibLDocument(entity.getLibellecArticle());
//				selectedTaProduit.getDto().setLibelleLot(entity.getLibellecArticle());
//				selectedTaProduit.setTaUnite1(null);
//				selectedTaProduit.setTaUnite2(null);
//				TaRapportUnite rapport=entity.getTaRapportUnite();
//				TaCoefficientUnite coef = null;
//				if(rapport!=null){
//					if(rapport.getTaUnite2()!=null)
//						coef=taCoefficientUniteService.findByCode(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
//				}
//				if(coef!=null){
//					selectedTaProduit.setTaCoefficientUnite(coef);
//					if(coef.getUniteA()!=null) {
//						selectedTaProduit.getDto().setU1LDocument(coef.getUniteA().getCodeUnite());
//						selectedTaProduit.setTaUnite1(uniteService.findByCode(coef.getUniteA().getCodeUnite()));
//					}
//					if(coef.getUniteB()!=null) {
//						selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//						selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
//					}
//				}else if(entity!=null){
//					if(entity.getTaUnite1()!=null) {
//						selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
//						selectedTaProduit.setTaUnite1(uniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//					}
//					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
//							if(obj!=null){
//								if(obj.getTaUnite2()!=null) {										
//									coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
//								}
//							}
//							selectedTaProduit.setTaCoefficientUnite(coef);
//							System.out.println("coef :"+coef);
//							if(coef!=null) {
//								selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//								selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
//							}
//						}							
//					}
//				}
				selectedTaProduit.setTaUnite1(null);
				selectedTaProduit.setTaUnite2(null);
				selectedTaProduit.getDto().setU2LDocument(null);
				selectedTaProduit.getDto().setQte2LDocument(BigDecimal.ZERO);
				TaRapportUnite rapport = null;
				if(entity!=null) rapport=entity.getTaRapportUnite();
				TaCoefficientUnite coef = null;
				if(rapport!=null){
					if(rapport.getTaUnite2()!=null) {
						coef=recupCoefficientUnite(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
						selectedTaProduit.setTaCoefficientUnite(coef);
					}
				}
				if(coef!=null){
					selectedTaProduit.setTaCoefficientUnite(coef);
					if(entity.getTaUnite1()!=null) {
						selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaProduit.setTaUnite1(uniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
						selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
					}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedTaProduit.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
						selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteA().getCodeUnite()));
					}
				}else
					if(entity!=null ){
						if(entity.getTaUnite1()!=null) {
							selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
							selectedTaProduit.setTaUnite1(uniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
						}
						for (TaRapportUnite obj : entity.getTaRapportUnites()) {
							if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
								if(obj!=null){
									if(obj.getTaUnite2()!=null) {										
										coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
									}
								}
								selectedTaProduit.setTaCoefficientUnite(coef);
								if(coef!=null) {
									if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
									selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
									selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
									}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
										selectedTaProduit.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
										selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteA().getCodeUnite()));
									}
								}else if(obj.getTaUnite2()!=null){
									selectedTaProduit.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());
									selectedTaProduit.setTaUnite2(uniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
								}
							}							
						}
					}				
				if(entity.getTaPrix()!=null){
					if(entity.getTaUnite1()!=null) {
						selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaProduit.setTaUnite1(uniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
							if(obj!=null){
								if(obj.getTaUnite2()!=null)
									coef=taCoefficientUniteService.findByCode(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
							}
							selectedTaProduit.setTaCoefficientUnite(coef);
							selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
							selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
							selectedTaProduit.getTaCoefficientUnite().recupRapportEtSens(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument());
						}							
					}
				}
				
			}else if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
				TaEntrepot entity =null;
				if(value!=null){
					if(value instanceof TaEntrepot){
						entity=(TaEntrepot) value;
						entity = entrepotService.findByCode(entity.getCodeEntrepot());
					}else{
						entity = entrepotService.findByCode((String)value);
					}
					masterEntityLignePF.setTaEntrepot(entity);
				}else{
					masterEntityLignePF.setTaEntrepot(null);
					selectedTaProduit.getDto().setCodeEntrepot("");
				}
				
		
			}
			 else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
					BigDecimal qte=BigDecimal.ZERO;
					if(value!=null){
						if(!value.equals("")){
							qte=(BigDecimal)value;
						}
						selectedTaProduit.setTaCoefficientUnite(recupCoefficientUnite(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument()));
						if(selectedTaProduit.getTaCoefficientUnite()!=null) {
							if(selectedTaProduit.getTaCoefficientUnite().getOperateurDivise()) 
								selectedTaProduit.getDto().setQte2LDocument((qte).divide(selectedTaProduit.getTaCoefficientUnite().getCoeff()
										,MathContext.DECIMAL128).setScale(selectedTaProduit.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
							else selectedTaProduit.getDto().setQte2LDocument((qte).multiply(selectedTaProduit.getTaCoefficientUnite().getCoeff()));
						}
				} else {
					masterEntityLignePF.setQte2LDocument(null);
				}
				
			} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite) value;
						entity = uniteService.findByCode(entity.getCodeUnite());
					}else{
						entity = uniteService.findByCode((String)value);
					}
					masterEntityLignePF.setU1LDocument(entity.getCodeUnite());
					selectedTaProduit.getDto().setU1LDocument(entity.getCodeUnite());
				}else{
					masterEntityLignePF.setU1LDocument(null);
					selectedTaProduit.getDto().setU1LDocument("");
				}
				selectedTaProduit.setTaCoefficientUnite(recupCoefficientUnite(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedTaProduit.getDto().getQteLDocument());
			} 		
			 else if(nomChamp.equals(Const.C_QTE2_L_DOCUMENT)) {

				
			} else if(nomChamp.equals(Const.C_U2_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite) value;
						entity = uniteService.findByCode(entity.getCodeUnite());
					}else{
						entity = uniteService.findByCode((String)value);
					}
					masterEntityLignePF.setU2LDocument(entity.getCodeUnite());
					selectedTaProduit.getDto().setU2LDocument(entity.getCodeUnite());
				}else{
					masterEntityLignePF.setU2LDocument(null);
					selectedTaProduit.getDto().setU2LDocument("");
				}
				selectedTaProduit.setTaCoefficientUnite(recupCoefficientUnite(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedTaProduit.getDto().getQteLDocument());
			}			
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean validateUIFieldMP(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
//				TaTiers entity = null;
//				if(value!=null){
//					entity=(TaTiers) value;
//					entity = tiersService.findByCode(entity.getCodeTiers());
//				}
//				masterEntity.setTaTiers(entity);

			}else if(nomChamp.equals(Const.C_NUM_LOT)) {
				selectedTaMatierePremiere.getDto().setEmplacement(null);
				if(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement()!=null && !selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement().equals("")) {
					selectedTaMatierePremiere.getDto().setEmplacement(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement());
				}
				selectedTaMatierePremiere.getDto().setCodeEntrepot(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getCodeEntrepot());
				
				TaEntrepot entity =null;
				entity = entrepotService.findByCode(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getCodeEntrepot());
				selectedTaMatierePremiere.setTaEntrepot(entity);
				
				TaLot lot =null;
				lot = lotService.findByCode(selectedTaMatierePremiere.getArticleLotEntrepotDTO().getNumLot());
				selectedTaMatierePremiere.setTaLot(lot);
				selectedTaMatierePremiere.getDto().setLibLDocument(lot.getLibelle());
			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
				
			}
			else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = null;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = articleService.findByCode(((TaArticleDTO) value).getCodeArticle());
					}else{
						entity = articleService.findByCode((String)value);
					}
				}
				selectedTaMatierePremiere.setTaArticle(entity);
				
				selectedTaMatierePremiere.getDto().setLibLDocument(entity.getLibellecArticle());
				selectedTaMatierePremiere.getDto().setLibelleLot(entity.getLibellecArticle());
				selectedTaMatierePremiere.setTaUnite1(null);
				selectedTaMatierePremiere.setTaUnite2(null);
				TaRapportUnite rapport=entity.getTaRapportUnite();
				TaCoefficientUnite coef = null;
				if(rapport!=null){
					if(rapport.getTaUnite1()!=null && rapport.getTaUnite2()!=null) {
						coef=recupCoefficientUnite(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
						selectedTaMatierePremiere.setTaCoefficientUnite(coef);
					}
				}
				if(coef!=null){
					selectedTaMatierePremiere.setTaCoefficientUnite(coef);
					if(coef.getUniteA()!=null) {
						selectedTaMatierePremiere.getDto().setU1LDocument(coef.getUniteA().getCodeUnite());
						selectedTaMatierePremiere.setTaUnite1(uniteService.findByCode(coef.getUniteA().getCodeUnite()));
					}
					if(coef.getUniteB()!=null) {
						selectedTaMatierePremiere.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
						selectedTaMatierePremiere.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
					}
				}else if(entity!=null){
					if(entity.getTaUnite1()!=null) {
						selectedTaMatierePremiere.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaMatierePremiere.setTaUnite1(uniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaMatierePremiere.getDto().getU1LDocument())){
							if(obj!=null){
								if(obj.getTaUnite2()!=null) {										
									coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
								}
							}
							selectedTaMatierePremiere.setTaCoefficientUnite(coef);
							System.out.println("coef :"+coef);
							if(coef!=null) {
								selectedTaMatierePremiere.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
								selectedTaMatierePremiere.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
							}
						}							
					}
				}

				if(entity.getTaPrix()!=null){
					if(entity.getTaUnite1()!=null) {
						selectedTaMatierePremiere.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaMatierePremiere.setTaUnite1(uniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
							if(obj!=null){
								if(obj.getTaUnite2()!=null)
									coef=taCoefficientUniteService.findByCode(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
							}
							selectedTaProduit.setTaCoefficientUnite(coef);
							selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
							selectedTaProduit.setTaUnite2(uniteService.findByCode(coef.getUniteB().getCodeUnite()));
							selectedTaProduit.getTaCoefficientUnite().recupRapportEtSens(selectedTaProduit.getDto().getU1LDocument(),selectedTaProduit.getDto().getU2LDocument());
						}							
					}
				}
				
			}else if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
//				TaEntrepot entity =null;
//				if(value!=null){
//					entity=(TaEntrepot) value;
//					entity = entrepotService.findByCode(entity.getCodeEntrepot());
//				}
//				masterEntityLigneMP.setTaEntrepot(entity);
		
			}
			 else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
					BigDecimal qte=BigDecimal.ZERO;
					if(value!=null){
						if(!value.equals("")){
							qte=(BigDecimal)value;
						}
						selectedTaMatierePremiere.setTaCoefficientUnite(recupCoefficientUnite(selectedTaMatierePremiere.getDto().getU1LDocument(),selectedTaMatierePremiere.getDto().getU2LDocument()));
						if(selectedTaMatierePremiere.getTaCoefficientUnite()!=null) {
							if(selectedTaMatierePremiere.getTaCoefficientUnite().getOperateurDivise()) 
								selectedTaMatierePremiere.getDto().setQte2LDocument((qte).divide(selectedTaMatierePremiere.getTaCoefficientUnite().getCoeff()
										,MathContext.DECIMAL128).setScale(selectedTaMatierePremiere.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
							else selectedTaMatierePremiere.getDto().setQte2LDocument((qte).multiply(selectedTaMatierePremiere.getTaCoefficientUnite().getCoeff()));
						}

				} else {
					masterEntityLigneMP.setQte2LDocument(null);
				}
					masterEntityLigneMP.setQteLDocument(qte);
			} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					entity=(TaUnite) value;
					entity = uniteService.findByCode(entity.getCodeUnite());
				}
				masterEntityLigneMP.setU1LDocument(entity.getCodeUnite());
				selectedTaMatierePremiere.getDto().setU1LDocument(entity.getCodeUnite());
				selectedTaMatierePremiere.setTaCoefficientUnite(recupCoefficientUnite(selectedTaMatierePremiere.getDto().getU1LDocument(),selectedTaMatierePremiere.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedTaMatierePremiere.getDto().getQteLDocument());				
			} 			
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public void validateLignesPF(FacesContext context, UIComponent component, Object value) throws ValidatorException {

//		String msg = "";
//
//		try {
//
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//
//
//			validateUIField(nomChamp,value);
//			TaLModeleFabricationDTO dtoTemp =new TaLModeleFabricationDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			taLModeleFabricationService.validateDTOProperty(dtoTemp, nomChamp, ITaLModeleFabricationServiceRemote.validationContext );
//
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
			Set<ConstraintViolation<TaLModeleFabricationDTO>> violations = factory.getValidator().validateValue(TaLModeleFabricationDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLModeleFabricationDTO> cv : violations) {
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
	
	public void validateLignesMP(FacesContext context, UIComponent component, Object value) throws ValidatorException {

//		String msg = "";
//
//		try {
//
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//
//
//			validateUIFieldMP(nomChamp,value);
//			TaLModeleFabricationDTO dtoTemp =new TaLModeleFabricationDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			taLModeleFabricationService.validateDTOProperty(dtoTemp, nomChamp, ITaLModeleFabricationServiceRemote.validationContext );
//
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
			Set<ConstraintViolation<TaLModeleFabricationDTO>> violations = factory.getValidator().validateValue(TaLModeleFabricationDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLModeleFabricationDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIFieldMP(nomChamp,value);
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
//			//String selectedRadio = (String) value;
//
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//
//			//msg = "Mon message d'erreur pour : "+nomChamp;
//
//			validateUIField(nomChamp,value);
//			TaModeleFabricationDTO dtoTemp = new TaModeleFabricationDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			modeleFabricationService.validateDTOProperty(dtoTemp, nomChamp, ITaModeleFabricationServiceRemote.validationContext );
//
//			//selectedTaBonReceptionDTO.setAdresse1Adresse("abcd");
//
//			//		  if(selectedRadio.equals("aa")) {
//			//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//			//		  }
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
			Set<ConstraintViolation<TaModeleFabricationDTO>> violations = factory.getValidator().validateValue(TaModeleFabricationDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaModeleFabricationDTO> cv : violations) {
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
//			fabricationService.validateDTOProperty(selectedTaModeleFabricationDTO,Const.C_CODE_TIERS,  ITaModeleFabricationServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
//	public void validateLigneAvantEnregistrement( Object value) throws ValidatorException {
//
//		String msg = "";
//
//		try {
//			taLFabricationService.validateDTOProperty(selectedTaProduit.getDto(),Const.C_CODE_ARTICLE,  ITaLModeleFabricationServiceRemote.validationContext );
//			taLFabricationService.validateDTOProperty(selectedTaProduit.getDto(),Const.C_NUM_LOT,  ITaLModeleFabricationServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
//	}
	
	public void actDialogParamModeleFab(ActionEvent actionEvent) {
		if(selectedTaModeleFabricationDTOs!=null && selectedTaModeleFabricationDTOs.length>0){
			if(selectedTaModeleFabricationDTOs.length>1){
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Sélection multiple","Vous ne devez sélectionner qu'un seul modèle pour créer la fabrication !"));
			}else{
				selectedTaModeleFabricationDTO=selectedTaModeleFabricationDTOs[0];

				Map<String,Object> options = new HashMap<String, Object>();
				options.put("modal", true);
				options.put("draggable", false);
				options.put("resizable", false);
				options.put("contentHeight", 300);
				options.put("modal", true);

				//Map<String,List<String>> params = null;
				Map<String,List<String>> params = new HashMap<String,List<String>>();
				List<String> list = new ArrayList<String>();
				list.add(LibConversion.integerToString(selectedTaModeleFabricationDTO.getId()));
				params.put("idModeleFabrication", list);
				//        List<String> list2 = new ArrayList<String>();
				//        list2.add(LibConversion.integerToString(listeConformiteController.getSelection().getIdConformite()));
				//        params.put("idEntityConformite", list2);
				//        List<String> list3 = new ArrayList<String>();
				//        list3.add("type_bareme");
				//        params.put("type", list3);
				//        if(listeConformiteController.getSelectionBareme()!=null) {
				//	        List<String> list4 = new ArrayList<String>();
				//	        list4.add(LibConversion.integerToString(listeConformiteController.getSelectionBareme().getIdBareme()));
				//	        params.put("idEntityBareme", list4);
				//        }

				PrimeFaces.current().dialog().openDynamic("solstyce/dialog_param_modele_fabrication", options, params);
			}
		}
	}
	
	public void actValiderDialogParamModeleFab(ActionEvent actionEvent) {
		System.out.println(qteModele);
		PrimeFaces.current().dialog().closeDynamic(qteModele);
	}
	
	public void actAnnulerCreerModele(ActionEvent actionEvent){
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	

	public void actNouveauModeleDepuisFabrication(ActionEvent actionEvent) {
		Integer idFabrication = (Integer) actionEvent.getComponent().getAttributes().get("idFabrication");
		if(idFabrication!=null) {
			try {
				TaFabrication fab = fabricationService.findById(idFabrication);
				System.out.println("ddddddddddddddddddddddddddddddd :: "+fab.getCodeDocument());
				nouveau(null);
				
				selectedTaModeleFabricationDTO.setCommentaire(fab.getCommentaire());
				selectedTaModeleFabricationDTO.setLibelleDocument(fab.getLibelleDocument());
				fab.findProduit();
				for (ITaLFabrication lf : fab.getLignesProduits()) {
					actInsererProduit(null);
					selectedTaProduit.setTaArticle(lf.getTaArticle());
					selectedTaProduit.getDto().setU1LDocument(lf.getU1LDocument());
					selectedTaProduit.getDto().setU2LDocument(lf.getU2LDocument());
					selectedTaProduit.getDto().setLibelleLot(lf.getLibLDocument());
					selectedTaProduit.getDto().setLibLDocument(lf.getLibLDocument());
					if(lf.getQteLDocument()!=null) {
						selectedTaProduit.getDto().setQteLDocument(lf.getQteLDocument());
					}
					if(lf.getQte2LDocument()!=null) {
						selectedTaProduit.getDto().setQte2LDocument(lf.getQte2LDocument());
					}
					if(lf.getTaEntrepot()!=null) {
						selectedTaProduit.getDto().setCodeEntrepot(lf.getTaEntrepot().getCodeEntrepot());
						selectedTaProduit.setTaEntrepot(lf.getTaEntrepot());
					}
					selectedTaProduit.getDto().setEmplacement(lf.getEmplacementLDocument());
					actEnregistrerProduit();
				}
				modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
				for (ITaLFabrication lf : fab.getLignesMatierePremieres()) {
					actInsererMatierePremiere(null);
					selectedTaMatierePremiere.setTaArticle(lf.getTaArticle());
					selectedTaMatierePremiere.getDto().setU1LDocument(lf.getU1LDocument());
					selectedTaMatierePremiere.getDto().setU2LDocument(lf.getU2LDocument());
					selectedTaMatierePremiere.getDto().setLibLDocument(lf.getLibLDocument());
					if(lf.getQteLDocument()!=null) {
						selectedTaMatierePremiere.getDto().setQteLDocument(lf.getQteLDocument());
					}
					if(lf.getQte2LDocument()!=null) {
						selectedTaMatierePremiere.getDto().setQte2LDocument(lf.getQte2LDocument());
					}
					actEnregistrerMatierePremiere();
				}
				modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
				
				valuesLignePF = IHMmodelPF();
				valuesLigneMP = IHMmodelMP();
				
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public List<TaLModeleFabricationDTOJSF> getValuesLigneMP() {
		return valuesLigneMP;
	}

	public void setValuesLigneMP(List<TaLModeleFabricationDTOJSF> valuesLigneMP) {
		this.valuesLigneMP = valuesLigneMP;
	}

	public List<TaLModeleFabricationDTOJSF> getValuesLignePF() {
		return valuesLignePF;
	}

	public void setValuesLignePF(List<TaLModeleFabricationDTOJSF> valuesLignePF) {
		this.valuesLignePF = valuesLignePF;
	}

	public boolean isInsertAutoMP() {
		return insertAutoMP;
	}

	public void setInsertAutoMP(boolean insertAutoMP) {
		this.insertAutoMP = insertAutoMP;
	}

	public boolean isInsertAutoPF() {
		return insertAutoPF;
	}

	public void setInsertAutoPF(boolean insertAutoPF) {
		this.insertAutoPF = insertAutoPF;
	}

	public LeftBean getLeftBean() {
		return leftBean;
	}

	public void setLeftBean(LeftBean leftBean) {
		this.leftBean = leftBean;
	}

	public Integer getQteModele() {
		return qteModele;
	}

	public void setQteModele(Integer qteModele) {
		this.qteModele = qteModele;
	}

	public TaTFabricationDTO getTaTFabricationDTO() {
		return taTFabricationDTO;
	}

	public void setTaTFabricationDTO(TaTFabricationDTO taTFabricationDTO) {
		this.taTFabricationDTO = taTFabricationDTO;
	}

	public TaTFabrication getTaTFabrication() {
		return taTFabrication;
	}

	public void setTaTFabrication(TaTFabrication taTFabrication) {
		this.taTFabrication = taTFabrication;
	}

	public TaModeleFabricationDTO[] getSelectedTaModeleFabricationDTOs() {
		return selectedTaModeleFabricationDTOs;
	}

	public void setSelectedTaModeleFabricationDTOs(
			TaModeleFabricationDTO[] selectedTaModeleFabricationDTOs) {
		this.selectedTaModeleFabricationDTOs = selectedTaModeleFabricationDTOs;
	}

	public Integer renvoiPremierIdSelection(){
		if(selectedTaModeleFabricationDTOs!=null && selectedTaModeleFabricationDTOs.length>0){
			return selectedTaModeleFabricationDTOs[0].getId();
		}
		return null;
	}
	public TaCoefficientUnite recupCoefficientUnite(String code1, String code2) {
		TaCoefficientUnite coef=null;;
		coef=taCoefficientUniteService.findByCode(code1,code2);
		if(coef!=null)coef.recupRapportEtSens(code1,code2);
		return coef;
	}

	public void removeLigneMP(SWTLigneDocument ligne) throws Exception {
//		if(beforeRemoveLigne(ligne)) {
			if( masterEntity.getLignesMatierePremieres().size()-1>=0 ){
				//passage ejb
				int rang = masterEntity.getLignesMatierePremieres().indexOf(ligne);
				 masterEntity.getLignesMatierePremieres().remove(ligne);
				 masterEntity.reinitialiseNumLignesMP(0, 1);
//			}
		} else {
			throw new ExceptLgr();
		}
	}
	
	public void removeLignePF(SWTLigneDocument ligne) throws Exception {
//		if(beforeRemoveLigne(ligne)) {
			if( masterEntity.getLignesProduits().size()-1>=0 ){
				//passage ejb
				int rang = masterEntity.getLignesProduits().indexOf(ligne);
				 masterEntity.getLignesProduits().remove(ligne);
				 masterEntity.reinitialiseNumLignesPF(0, 1);
//			}
		} else {
			throw new ExceptLgr();
		}
	}
}   

