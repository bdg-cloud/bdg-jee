package fr.legrain.bdg.webapp.solstyce;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.primefaces.event.UnselectEvent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.dto.TaLabelArticleDTO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.dto.TaTFabricationDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.article.model.TaLModeleFabricationMP;
import fr.legrain.article.model.TaLModeleFabricationPF;
import fr.legrain.article.model.TaLRecette;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaModeleFabrication;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaRecette;
import fr.legrain.article.model.TaTFabrication;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.service.TaFabricationService;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCoefficientUniteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLFabricationPFServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLabelArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaModeleFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRecetteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaStatusConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.agenda.EvenementParam;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.EmailPieceJointeParam;
import fr.legrain.bdg.webapp.app.EtiquetteCodeBarreBean;
import fr.legrain.bdg.webapp.app.LeftBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.document.dto.ILigneDocumentLotDTO;
import fr.legrain.document.dto.ITaLFabrication;
import fr.legrain.document.dto.TaLFabricationDTO;
import fr.legrain.document.model.SWTLigneDocument;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class FabricationController extends AbstractController implements Serializable { 
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	@Inject @Named(value="etiquetteCodeBarreBean")
	private EtiquetteCodeBarreBean etiquetteCodeBarreBean;
	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="leftBean")
	private LeftBean leftBean;
	
	private TaTFabrication taTFabrication;
	private TaTFabricationDTO taTFabricationDTO;
	
	private TaModeleFabrication modelFab;
	private TaRecette recetteFab;
	private BigDecimal qteModeleFabrication = BigDecimal.ONE;

	private TaFabrication masterEntity;
	private TaLFabricationMP masterEntityLigneMP;
	private TaLFabricationPF masterEntityLignePF;
	
	private TaLFabricationDTOJSF selectedTaMatierePremiere;
	private TaLFabricationDTOJSF selectedTaProduit;
	private TaFabricationDTO selectedTaFabricationDTO;
	private TaFabricationDTO[] selectedTaFabricationDTOs; 

	private List<TaLFabricationDTOJSF> valuesLigneMP;
	private List<TaLFabricationDTOJSF> valuesLignePF;
	
	private TaTiers taTiers;
	private TaTiersDTO taTiersDTO;
	private TaUtilisateur redacteur;
	private TaUtilisateur controleur;
	private TaUtilisateurDTO redacteurDTO;
	private TaUtilisateurDTO controleurDTO;
	
	private List<TaLabelArticleDTO> listeToutesCertification;
	private TaLabelArticle taLabelArticle;
	private List<TaLabelArticle> taLabelArticles;
	
	private boolean insertAutoMP = true;
	private boolean insertAutoPF = true;
	private boolean insertAutoEnabledMP = true;
	private boolean insertAutoEnabledPF = true;
	
//	protected List<ArticleLotEntrepotDTO> ArticleLotEntrepotDTO detailLigneOverLayPanel = null;
	
	//A gérer ailleurs, ou de façon plus globale, mis en place juste pour tester les lots dans les factures
	protected boolean gestionLot = true;

	private List<TaFabricationDTO> listeFabrication;

	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
	private List<ArticleLotEntrepotDTO> listArticleLotEntrepot;

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranMatierePremiere = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranProduit = new ModeObjetEcranServeur();

	protected @EJB ITaCoefficientUniteServiceRemote taCoefficientUniteService;
	private @EJB ITaFabricationServiceRemote fabricationService;
	private @EJB ITaTiersServiceRemote tiersService;
	private @EJB ITaUtilisateurServiceRemote utilisateurService;
	private @EJB ITaModeleFabricationServiceRemote modeleFabricationService;
	private @EJB ITaTFabricationServiceRemote taTFabricationService;
	private @EJB ITaRecetteServiceRemote recetteService;
	private @EJB ITaConformiteServiceRemote taConformiteService;
	private @EJB ITaLFabricationPFServiceRemote taLFabricationPFService;
	private @EJB ITaLotServiceRemote lotService;
	private @EJB ITaResultatConformiteServiceRemote taResultatConformiteService;
	protected @EJB ITaStatusConformiteServiceRemote taStatusConformiteService;
	private @EJB ITaMouvementStockServiceRemote mouvementService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaEntrepotServiceRemote entrepotService;
	private @EJB ITaArticleServiceRemote articleService;
	private @EJB ITaTypeMouvementServiceRemote typeMouvementService;
	private @EJB ITaUniteServiceRemote uniteService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
	private @EJB ITaUniteServiceRemote taUniteService;
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
	private @EJB ITaLabelArticleServiceRemote taLabelArticleService;
	protected @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	private LgrDozerMapper<TaFabricationDTO,TaFabrication> mapperUIToModel  = new LgrDozerMapper<TaFabricationDTO,TaFabrication>();
	private LgrDozerMapper<TaFabrication,TaFabricationDTO> mapperModelToUI  = new LgrDozerMapper<TaFabrication,TaFabricationDTO>();
	private LgrDozerMapper<TaTFabrication,TaTFabricationDTO> mapperModelToUITFabrication  = new LgrDozerMapper<TaTFabrication,TaTFabricationDTO>();
	private LgrDozerMapper<TaUtilisateur,TaUtilisateurDTO> mapperModelToUIUtilisateur  = new LgrDozerMapper<TaUtilisateur,TaUtilisateurDTO>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUITiers  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	private LgrDozerMapper<TaLabelArticle,TaLabelArticleDTO> mapperCertification = new LgrDozerMapper<TaLabelArticle,TaLabelArticleDTO>();

	private List<ArticleLotEntrepotDTO> detailLigneOverLayPanel;
//	private List<TaLFabrication> listeLFabASupprimer = new LinkedList<TaLFabrication>();
	protected boolean modeScanCodeBarre = false;
	protected boolean majDlc = true;
	
	TaPreferences prefLot=null;


	public FabricationController() {  

	}  

	@PostConstruct
	public void postConstruct(){
		nomDocument="Fabrication";
		TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_DDM_DLC, ITaPreferencesServiceRemote.MAJ_DLC_FAB);
		if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
			majDlc=LibConversion.StringToBoolean(p.getValeur());
		}
		prefLot = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_AUTORISER_UTILISATION_CODE_LOT_FAB_PF);

		refresh();
		
		listeToutesCertification = taLabelArticleService.selectAllDTO();
		
		//actInserer();
	}

	public void refresh(){
		//listeFabrication = fabricationService.selectAllDTO();
		listeFabrication = fabricationService.findAllLight();
		valuesLigneMP = IHMmodelMP();
		valuesLignePF = IHMmodelPF();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
	}
	
	public List<TaLFabricationDTOJSF> IHMmodelMP() {
		LinkedList<TaLFabricationMP> ldao = new LinkedList<TaLFabricationMP>();
		LinkedList<TaLFabricationDTOJSF> lswt = new LinkedList<TaLFabricationDTOJSF>();
		
		if(masterEntity!=null && masterEntity.getLignesMatierePremieres() !=null && !masterEntity.getLignesMatierePremieres().isEmpty()) {
			masterEntity.reinitialiseNumLignesMP(0, 1);
			ldao.addAll(masterEntity.getLignesMatierePremieres());

			LgrDozerMapper<TaLFabricationMP,TaLFabricationDTO> mapper = new LgrDozerMapper<TaLFabricationMP,TaLFabricationDTO>();
			TaLFabricationDTO dto = null;
			TaArticleDTO artDTO = null;
			for (TaLFabricationMP ligne : ldao) {
				if(!ligne.ligneEstVide()){
				TaLFabricationDTOJSF t = new TaLFabricationDTOJSF();
//				try {
//					TaLot lot = taLotService.findByCode(o.getNumLot());
//					o.setTaLot(lot);
				dto = (TaLFabricationDTO) mapper.map(ligne, TaLFabricationDTO.class);
				
				dto.setEmplacement(ligne.getEmplacementLDocument());
				
				t.setDto(dto);
				t.setTaLot(ligne.getTaLot());
				if(t.getArticleLotEntrepotDTO()==null) {
					t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
				}
				if(ligne.getTaLot()!=null) {
					t.getArticleLotEntrepotDTO().setNumLot(ligne.getTaLot().getNumLot());
				}
				//t.getDto().setNumLot(ligne.getTaLot().getNumLot());
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
				artDTO.setLibellecArticle(ligne.getTaArticle().getLibellecArticle());
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
//				t.getDto().setCodeStatusConformite(lotService.validationLot(t.getTaLot()));
				lswt.add(t);
				}
			}

		}
		return lswt;
	}
	
	public List<TaLFabricationDTOJSF> IHMmodelPF() {
		LinkedList<TaLFabricationPF> ldao = new LinkedList<TaLFabricationPF>();
		LinkedList<TaLFabricationDTOJSF> lswt = new LinkedList<TaLFabricationDTOJSF>();
		
		if(masterEntity!=null && masterEntity.getLignesProduits()!=null && !masterEntity.getLignesProduits().isEmpty()) {
			masterEntity.reinitialiseNumLignesPF(0, 1);
			ldao.addAll(masterEntity.getLignesProduits());

			
			LgrDozerMapper<TaLFabricationPF,TaLFabricationDTO> mapper = new LgrDozerMapper<TaLFabricationPF,TaLFabricationDTO>();
			TaLFabricationDTO dto = null;
			TaArticleDTO artDTO = null;
			for (TaLFabricationPF ligne : ldao) {
				if(!ligne.ligneEstVide()){
				TaLFabricationDTOJSF t = new TaLFabricationDTOJSF();
//				try {
//					TaLot lot = taLotService.findByCode(o.getNumLot());
//					o.setTaLot(lot);
				dto = (TaLFabricationDTO) mapper.map(ligne, TaLFabricationDTO.class);
				
				dto.setEmplacement(ligne.getEmplacementLDocument());
				
				t.setDto(dto);
				t.setTaLot(ligne.getTaLot());
				if(ligne.getTaLot()!=null) {
					t.getDto().setNumLot(ligne.getTaLot().getNumLot());
					t.getDto().setLibelleLot(ligne.getTaLot().getLibelle());
					if(ligne.getTaLot().getTaLabelArticles()!=null && !ligne.getTaLot().getTaLabelArticles().isEmpty()) {
						List<TaLabelArticleDTO> listeCertDTO = new ArrayList<>();
						for (TaLabelArticle labelArt : ligne.getTaLot().getTaLabelArticles()) {
							listeCertDTO.add(mapperCertification.map(labelArt, TaLabelArticleDTO.class));
						}
						t.setListeCertificationDTO(listeCertDTO);
					}
				}else
					t.getDto().setLibelleLot(ligne.getLibLDocument());
				
//				t.setTaLot(lot);
				t.setTaArticle(ligne.getTaArticle());
				artDTO = new TaArticleDTO();
				artDTO.setCodeArticle(ligne.getTaArticle().getCodeArticle());
				t.setTaArticleDTO(artDTO);
				if(ligne.getTaArticle()!=null) {
					t.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
				}
				t.setTaEntrepot(ligne.getTaEntrepot());
				if(ligne.getU1LDocument()!=null) {
					t.setTaUnite1(new TaUnite());
					t.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
				}
				if(ligne.getU2LDocument()!=null) {
					t.setTaUnite2(new TaUnite());
					t.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
				}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				t.getDto().setCodeStatusConformite(ligne.getTaLot().getTaStatusConformite()!=null ? ligne.getTaLot().getTaStatusConformite().getCodeStatusConformite():TaStatusConformite.C_TYPE_ACTION_VIDE);
				lswt.add(t);
				}
			}

		}
		return lswt;
	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FABRICATION);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FABRICATION);
		b.setTitre("Fabrication");
		//b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setTemplate("solstyce/fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FABRICATION);
		b.setParamId("0");
		leftBean.setExpandedForce(false);

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		//actInserer(actionEvent);
		actInserer();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", 
					"Nouveau"
					)); 
		}
	} 
	
	public boolean etatBouton(String bouton) {
		return etatBouton(bouton,modeEcran);
	}
	
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
			case "inserer":
			case "fermer":
				retour = false;
				break;
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
				break;
			case "supprimer":
			case "modifier":
			case "imprimer":
				if(selectedTaFabricationDTO!=null && selectedTaFabricationDTO.getId()!=null  && selectedTaFabricationDTO.getId()!=0 ) retour = false;
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
	
	public void actDialogControleLot(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("closable", false);
        options.put("resizable", true);
        options.put("contentHeight", 710);
        options.put("contentWidth", "100%");
        //options.put("contentHeight", "100%");
        options.put("width", 1024);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		TaLot l = new TaLot();
		
//		creerLot();
//		sessionMap.put("lotBR", selectedTaLBonReceptionDTOJSF.getTaLot());
		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
		//sessionMap.put("numLot", selectedTaLBonReceptionDTOJSF.getDto().getNumLot());
		sessionMap.put("numLot", numLot);
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_controle_article", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogControleLot(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaLot j = (TaLot) event.getObject();
//			if(listeControle!=null && !listeControle.isEmpty()) {
//				if(masterEntityLignePF.getTaLot().getIdLot()==listeControle.get(0).getR().getTaLot().getIdLot()) {
					
					
//					String action = lotService.validationLot(masterEntityLignePF.getTaLot());
//					TaStatusConformite s = taResultatConformiteService.etatLot(masterEntityLignePF.getTaLot().getIdLot());
					masterEntityLignePF.setTaLot(j);
					selectedTaProduit.setTaLot(j);
					
					if(selectedTaFabricationDTO instanceof ILigneDocumentLotDTO) {
						//((ILigneDocumentLotDTO)masterEntityLignePF).setCodeStatusConformite(action);
						((ILigneDocumentLotDTO)masterEntityLignePF).setCodeStatusConformite(masterEntityLignePF.getTaLot().getTaStatusConformite().getCodeStatusConformite());
					}
					
//					if(action.equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER)
//							|| action.equals(TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE)
//							|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
//							) {
//						masterEntityLignePF.getTaLot().setPresenceDeNonConformite(true);
//					} else {
//						masterEntityLignePF.getTaLot().setPresenceDeNonConformite(false);
//					} 
//					
//					if(action.equals(TaStatusConformite.C_TYPE_ACTION_OK)
//							|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
//							) {
//						masterEntityLignePF.getTaLot().setLotConforme(true);
//					} else {
//						masterEntityLignePF.getTaLot().setLotConforme(false);
//					} 
					
//					TaLot l = lotService.merge(masterEntityLignePF.getTaLot());
//					masterEntityLignePF.setTaLot(l);
//					masterEntityLignePF.getTaMouvementStock().setTaLot(l);
//					
//					masterEntityLignePF.getTaLot().getTaResultatConformites().clear();
//					for(ControleConformiteJSF ctrl : listeControle) {
//						if(ctrl.getR()!=null) {
//							ctrl.getR().setTaLot(masterEntityLignePF.getTaLot());
//							masterEntityLignePF.getTaLot().getTaResultatConformites().add(ctrl.getR());
//						}
//					}
					
					
					
//					TaLot l = lotService.merge(masterEntityLignePF.getTaLot());
//					masterEntityLignePF.setTaLot(l);
//					masterEntityLignePF.getTaMouvementStock().setTaLot(l);
//					m.put(masterEntityLignePF.getIdLDocument(), l);
//				}
//			}
		}
	}
	
	public void changeModeSaisieCodeBarre() {
		changeModeSaisieCodeBarre(null);
    }
	
	public void changeModeSaisieCodeBarre(ActionEvent actionEvent) {
		String activeScan ="activeScan('.zone-scan-codebarre')";
		String desactiveScan ="desactiveScan('.zone-scan-codebarre')";

		if(modeScanCodeBarre)
			PrimeFaces.current().executeScript(activeScan);
		else
			PrimeFaces.current().executeScript(desactiveScan);
    }
		
	public String validationLot(TaLot taLot) {
		return lotService.validationLot(taLot);
	}

	//************************************************************************************************************//
	//	Fabrication
	//************************************************************************************************************//
	public void initListeMultiple(){
//		taLabelArticle = null;
//		taLabelArticles = null;
////		taLabelArticleDTO = null;
//
//		
//		try{
//			if(selectedTaFabricationDTO.getTaLabelArticleDTOs()!=null) {
//				if(taLabelArticles==null) {
//					taLabelArticles = new ArrayList<>();
//				}
//				taLabelArticles.clear();
//
//				
//				for (TaLabelArticleDTO f : selectedTaFabricationDTO.getTaLabelArticleDTOs()) {
//					if(f.getCodeLabelArticle()!=null && !f.getCodeLabelArticle().equals("")) {
//						taLabelArticle=taLabelArticleService.findById(f.getId());
//						taLabelArticles.add(taLabelArticle);
//						
//	//					taFamilleDTO = mapperModelToUIFamille.map(taFamille, TaFamilleDTO.class);
//	//					taFamilleDTOs.add(taFamilleDTO);
//					}
//				}
//			}
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void actEnregistrer(){  
		try {
			//Mapping vers les entités
			autoCompleteMapUIToDTO();
			initListeMultiple();
			
			validateDocumentAvantEnregistrement(selectedTaFabricationDTO);
			mapperUIToModel.map(selectedTaFabricationDTO, masterEntity);
			
			masterEntity.setLegrain(false);
			
			if(masterEntity.getDateDebR()!=null) {
				masterEntity.setDateFinR(masterEntity.getDateDebR());
			}
			if(masterEntity.getDateDebR()!=null) {
				masterEntity.setDateDebT(masterEntity.getDateDebR());
			}
			if(masterEntity.getDateDebR()!=null) {
				masterEntity.setDateFinT(masterEntity.getDateDebR());
			}
			if(masterEntity.getDateDebR()!=null) {
				masterEntity.setDateDocument(masterEntity.getDateDebR());
			}
			
			//supression des lignes vides
			List<ITaLFabrication> listeLigneVide = new ArrayList<ITaLFabrication>(); 
			for (ITaLFabrication ligne : masterEntity.getLignesMatierePremieres()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide()) {
					listeLigneVide.add(ligne);
				}
			}
			
			for (ITaLFabrication ligne : masterEntity.getLignesMatierePremieres()) {
				ligne.setLegrain(false);
				if(gestionLot && ligne.getTaArticle()!=null && ligne.getTaArticle().getGestionLot() && ligne.getTaLot()==null){
					throw new Exception("Le numéro du lot doit être rempli");
				} else {
					if(!gestionLot || (ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot())) {
						//utiliser un lot virtuel
						if(ligne.getTaLot()==null ){
							ligne.setTaLot(lotService.findOrCreateLotVirtuelArticle(ligne.getTaArticle().getIdArticle()));
						}
					}
				}
			}
			
			
			for (ITaLFabrication ligne : masterEntity.getLignesProduits()) {
				ligne.setLegrain(false);
				if(gestionLot && ligne.getTaArticle()!=null && ligne.getTaArticle().getGestionLot() && ligne.getTaLot()==null){
					throw new Exception("Le numéro du lot doit être rempli");
				} 
//				else {
//					if(!gestionLot || (ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot())) {
//						//utiliser un lot virtuel
//						if(ligne.getTaLot()==null ){
//							Map<String, String> params = new LinkedHashMap<String, String>();
//							if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
//							if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
//							params.put(Const.C_CODEDOCUMENT, selectedTaFabricationDTO.getCodeDocument());
//							ligne.setTaLot(lotService.createLotVirtuelArticleFabOrBR(ligne.getTaArticle().getIdArticle(),params));
//						}else{
//							if(!ligne.getTaLot().getNumLot().startsWith(TaLotService.DEBUT_VIRTUEL_LOT)){
//								ligne.getTaLot().setNumLot(TaLotService.DEBUT_VIRTUEL_LOT+ligne.getTaLot().getNumLot());
//							}
//						}
//					}
//					if(ligne.getTaLot()!=null && ligne.getTaArticle()!=null){
//						ligne.getTaLot().setVirtuel(!ligne.getTaArticle().getGestionLot());
//						ligne.getTaLot().setVirtuelUnique(!ligne.getTaArticle().getGestionLot());
//					}
//				}
			}
			
			
			
			for (ITaLFabrication taLBonReception : listeLigneVide) {
				masterEntity.getLignesMatierePremieres().remove(taLBonReception);
			}
			listeLigneVide = new ArrayList<ITaLFabrication>(); 
			for (ITaLFabrication ligne : masterEntity.getLignesProduits()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide()) {
					listeLigneVide.add(ligne);
				}
			}
			for (ITaLFabrication taLBonReception : listeLigneVide) {
				masterEntity.getLignesProduits().remove(taLBonReception);
			}
			
			/*
			 * Gérer les mouvements corrrespondant aux lignes 
			 */
			//Création du groupe de mouvement
			TaGrMouvStock grpMouvStock = new TaGrMouvStock();
			if(masterEntity.getTaGrMouvStock()!=null) {
				grpMouvStock=masterEntity.getTaGrMouvStock();
			}
			grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeDocument());
			grpMouvStock.setDateGrMouvStock(masterEntity.getDateDebR());
			grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument()!=null?masterEntity.getLibelleDocument():"Fabrication "+masterEntity.getCodeDocument());
			grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("F"));
			masterEntity.setTaGrMouvStock(grpMouvStock);
			grpMouvStock.setTaFabrication(masterEntity);
			
			for (ITaLFabrication l : masterEntity.getLignes()) {
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
				if(l.getTaMouvementStock()!=null)
					l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
			}
			
			//Création des lignes de mouvement
			for (ITaLFabrication ligne : masterEntity.getLignes()) {
//				if(ligne.getTaMouvementStock()==null){
				TaMouvementStock mouv = new TaMouvementStock();
				if(ligne.getTaMouvementStock()!=null) {
					mouv=ligne.getTaMouvementStock();
				}
				if(ligne.getLibLDocument()!=null) {
					mouv.setLibelleStock(ligne.getLibLDocument());
				} else if (ligne.getTaArticle().getLibellecArticle()!=null){
					mouv.setLibelleStock(ligne.getTaArticle().getLibellecArticle());
				} else {
					mouv.setLibelleStock("");
				}
				mouv.setDateStock(masterEntity.getDateDocument());
				mouv.setEmplacement(ligne.getEmplacementLDocument());
				mouv.setTaEntrepot(ligne.getTaEntrepot());
				if(ligne.getTaLot()!=null){
//					mouv.setNumLot(ligne.getTaLot().getNumLot());
					mouv.setTaLot(ligne.getTaLot());
				}
				//mouv.setQte1Stock(ligne.getQteLDocument());
				//mouv.setQte2Stock(ligne.getQte2LDocument());
				mouv.setUn1Stock(ligne.getU1LDocument());
				mouv.setUn2Stock(ligne.getU2LDocument());
				mouv.setTaGrMouvStock(grpMouvStock);
//				ligne.setTaLot(null);
				ligne.setTaMouvementStock(mouv);
				
				grpMouvStock.getTaMouvementStockes().add(mouv);
//				}
			}
			
			//fusionner les 2 listes dans l'entité en évitant les doublons
			for (ITaLFabrication lignePF : masterEntity.getLignesProduits()) {
				if(lignePF.getQteLDocument()!=null) {
					lignePF.getTaMouvementStock().setQte1Stock(lignePF.getQteLDocument().abs());
				}
				if(lignePF.getQte2LDocument()!=null) {
					lignePF.getTaMouvementStock().setQte2Stock(lignePF.getQte2LDocument().abs());
				}
//				lignePF.getTaMouvementStock().setTaLot(null);;
				lignePF.getTaLot().setDateLot(masterEntity.getDateDocument());
			}
			for (ITaLFabrication ligneMP : masterEntity.getLignesMatierePremieres()) {
				if(ligneMP.getQteLDocument()!=null) {
					ligneMP.getTaMouvementStock().setQte1Stock(ligneMP.getQteLDocument().abs().negate());
				}
				if(ligneMP.getQte2LDocument()!=null) {
					ligneMP.getTaMouvementStock().setQte2Stock(ligneMP.getQte2LDocument().abs().negate());
				}
//				ligneMP.getTaMouvementStock().setTaLot(null);

			}
			
			masterEntity = fabricationService.mergeAndFindById(masterEntity,ITaFabricationServiceRemote.validationContext);
			masterEntity = fabricationService.findById(masterEntity.getIdDocument());
			fabricationService.annuleCode(selectedTaFabricationDTO.getCodeDocument());
//			listeLFabASupprimer.clear();
//			fabricationService.remplirListeLigneASupprimer(listeLFabASupprimer);
			masterEntity.findProduit();
			//mapping vers l'IHM
			mapperModelToUI.map(masterEntity, selectedTaFabricationDTO);
			selectedTaFabricationDTOs=new TaFabricationDTO[]{selectedTaFabricationDTO};
			
			valuesLigneMP = IHMmodelMP();
			valuesLignePF = IHMmodelPF();
			
			
			autoCompleteMapDTOtoUI();

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
//				listeFabrication.add(new TaFabricationMapper().mapEntityToDto(masterEntity, null));
				listeFabrication.add(selectedTaFabricationDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
			//réinitialise l'écran
			//onRowSelect(null);

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
				if(selectedTaFabricationDTO.getCodeDocument()!=null) {
					fabricationService.annuleCode(selectedTaFabricationDTO.getCodeDocument());
				}
				if(masterEntity.getLignesProduits()!=null)
					for (ITaLFabrication lf : masterEntity.getLignesProduits()) {
						if(lf.getTaLot()!=null && lf.getTaLot().getNumLot()!=null) {
							lotService.annuleCode(lf.getTaLot().getNumLot());
						}
					}
				
				masterEntity=new TaFabrication();
				
				if(listeFabrication.size()>0)
				selectedTaFabricationDTO = listeFabrication.get(0);
				else selectedTaFabricationDTO=new TaFabricationDTO();
				onRowSelect(null);
				
				mapperModelToUI.map(masterEntity,selectedTaFabricationDTO );
				valuesLigneMP = IHMmodelMP();
				valuesLignePF = IHMmodelPF();
				break;
			case C_MO_EDITION:
				if(selectedTaFabricationDTO.getId()!=null && selectedTaFabricationDTO.getId()!=0){
					if(masterEntity.getLignesProduits()!=null)
						for (ITaLFabrication lf : masterEntity.getLignesProduits()) {
							if(lf.getTaLot()!=null && lf.getTaLot().getNumLot()!=null) {
								lotService.annuleCode(lf.getTaLot().getNumLot());
							}
						}
					
					masterEntity = fabricationService.findById(selectedTaFabricationDTO.getId());
					selectedTaFabricationDTO=fabricationService.findByIdDTO(selectedTaFabricationDTO.getId());
					//isa le 14/01/2016
					mapperModelToUI.map(masterEntity,selectedTaFabricationDTO );
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
		
		selectedTaFabricationDTO=new TaFabricationDTO();
		selectedTaFabricationDTOs=new TaFabricationDTO[]{selectedTaFabricationDTO};
		updateTab();
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeFabrication').filter()");
        
	}

	public void actInserer() {
		insertAutoEnabledMP = true;
		insertAutoEnabledPF = true;
		
		selectedTaFabricationDTO = new TaFabricationDTO();
		
		masterEntity = new TaFabrication();
		masterEntity.findProduit();
		
		valuesLignePF = IHMmodelPF();
		valuesLigneMP = IHMmodelMP();
		autoCompleteMapDTOtoUI();
		
		selectedTaFabricationDTO.setDateDebR(new Date());

		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(Const.C_CODETYPE, selectedTaFabricationDTO.getCodeTFabrication());
		
		if(selectedTaFabricationDTO.getCodeDocument()!=null) {
			fabricationService.annuleCode(selectedTaFabricationDTO.getCodeDocument());
		}
		String newCode=fabricationService.genereCode(params);
		if(newCode!=null && !newCode.equals("")){
			selectedTaFabricationDTO.setCodeDocument(newCode);
			if(selectedTaFabricationDTO.getLibelleDocument().isEmpty()){
				selectedTaFabricationDTO.setLibelleDocument("Fabrication n°: "+newCode);
			}
		}
		
		
		masterEntity.setLignesMatierePremieres(new ArrayList<TaLFabricationMP>());
		masterEntity.setLignesProduits(new ArrayList<TaLFabricationPF>());
		
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
			if(selectedTaFabricationDTO!=null && selectedTaFabricationDTO.getId()!=null){
				masterEntity = fabricationService.findById(selectedTaFabricationDTO.getId());
			}
			masterEntity.findProduit();
//			for (TaLFabrication ligne : masterEntity.getListMatierePremieres()) {
//				ligne.setTaLot(null);
//			}
			
			fabricationService.remove(masterEntity);
			listeFabrication.remove(selectedTaFabricationDTO);
			

			if(!listeFabrication.isEmpty()) {
				selectedTaFabricationDTO = listeFabrication.get(0);
				selectedTaFabricationDTOs=new TaFabricationDTO[]{selectedTaFabricationDTO};
				updateTab();
			}else {
				selectedTaFabricationDTO=new TaFabricationDTO();
				selectedTaFabricationDTOs=new TaFabricationDTO[]{};
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			scrollToTop();

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Fabrication", "fabrication supprimée."));
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));
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
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			TaFabrication fab=fabricationService.findAvecResultatConformites(selectedTaFabricationDTO.getId());
			mapEdition.put("fabrication",fab );	
			fab.findProduit();
			
			mapEdition.put("lignesProduits", fab.getLignesProduits());		
			mapEdition.put("lignesMatierePremieres", fab.getLignesMatierePremieres());		
			sessionMap.put("edition", mapEdition);
			
			System.out.println("FabricationController.actImprimer()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}    
	
	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
		
		etiquetteCodeBarreBean.videListe();
		try {
			for (ITaLFabrication l : masterEntity.getLignesProduits()) {
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
	
	public void actImprimerListeDesFabrications(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesFabrications", listeFabrication);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public List<TaUnite> uniteAutoCompleteMP(String query) {
		//List<TaUnite> allValues = taUniteService.selectAll();
		List<TaUnite> allValues = taUniteService.findByCodeUniteStock(selectedTaMatierePremiere.getTaArticle().getTaUniteReference().getCodeUnite(), query.toUpperCase());
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
	
	public List<TaUnite> uniteAutoCompletePF(String query) {
		//List<TaUnite> allValues = taUniteService.selectAll();
		List<TaUnite> allValues = taUniteService.findByCodeUniteStock(selectedTaProduit.getTaArticle().getTaUniteReference().getCodeUnite(), query.toUpperCase());
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

	//************************************************************************************************************//
	//	Matière Premiere
	//************************************************************************************************************//

	public List<ArticleLotEntrepotDTO> onChangeListArticleMP(String codeArticle, String numlot) {
		try {
			
			listArticleLotEntrepot = new LinkedList<ArticleLotEntrepotDTO>();
			//listArticleLotEntrepot = mouvementService.getEtatStockByArticle((article.split("~"))[0]);
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_AUTORISER_UTILISATION_LOTS_NON_CONFORME);
			Boolean utiliserLotNonConforme = null;
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				utiliserLotNonConforme = LibConversion.StringToBoolean(p.getValeur());
			}
			
			listArticleLotEntrepot = mouvementService.getEtatStockByArticle(codeArticle,utiliserLotNonConforme);
			List<ArticleLotEntrepotDTO> temp= new LinkedList<ArticleLotEntrepotDTO>();
			List<BigDecimal> qte = new ArrayList<BigDecimal>();
			for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){ //liste des lots en stocks par articles
	
				for(ITaLFabrication mp : masterEntity.getLignesMatierePremieres()){ //liste des MP de la fabrication courante
					TaMouvementStock mouv = mp.getTaMouvementStock();
	
					//TaLot lot =lotService.findByCode(mouv.getNumLot());
					//mouv.setTaLot(lot);
					
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
				//DESACTIVE POUR L'INSTANT (SUITE UNITE DE REFERENCE) CAR NECESSITE UNE CONVERTION DES QTE EN JAVA EN DYNAMIQUE
				//RISQUE DE RESULTAT DIFFERENT ENTRE LES CALCULS JAVA ET CEUX EN BDD => REMPLACER CE CALCUL PAR UN INDICATEUR
				
//				//soustraction des quantités qui serait déjà utilisé dans d'autre lignes de MP de cette fabrication
//				listArticleLotEntrepot.remove(dtoTemp);
//				dtoTemp.setQte1(dtoTemp.getQte1().subtract(qte.get(i)));
//				i++;
//				listArticleLotEntrepot.add(dtoTemp);
			}
			
			List<ArticleLotEntrepotDTO> filteredValues = new LinkedList<ArticleLotEntrepotDTO>();

			for (int j = 0; j < listArticleLotEntrepot.size(); j++) {
				//filtre en fonction de la saisie
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
//			masterEntityLigneMP.setPf(false);
			masterEntityLigneMP.setTaArticle(selectedTaMatierePremiere.getTaArticle());
			masterEntityLigneMP.setTaEntrepot(selectedTaMatierePremiere.getTaEntrepot());
			masterEntityLigneMP.setTaLot(selectedTaMatierePremiere.getTaLot());
			masterEntityLigneMP.setEmplacementLDocument(selectedTaMatierePremiere.getDto().getEmplacement());
			
			LgrDozerMapper<TaLFabricationDTO,TaLFabricationMP> mapper = new LgrDozerMapper<TaLFabricationDTO,TaLFabricationMP>();
			mapper.map((TaLFabricationDTO) selectedTaMatierePremiere.getDto(),masterEntityLigneMP);
			
			//rajouter par isa car perte du numlot si modif ligne existante
			if(selectedTaMatierePremiere.getArticleLotEntrepotDTO()==null) {
				selectedTaMatierePremiere.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
			}
			if(masterEntityLigneMP.getTaLot()!=null) {
				selectedTaMatierePremiere.getArticleLotEntrepotDTO().setNumLot(masterEntityLigneMP.getTaLot().getNumLot());
			}
			/////
			
			masterEntity.enregistreLigne(masterEntityLigneMP);
			//if(masterEntityLigneMP.getIdLDocument()==0) { //** if pour empecher de doubler les lignes pendant une modification
			//if(modeEcranMatierePremiere.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0){
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0){
//				if(!masterEntity.getLignes().contains(masterEntityLigneMP)) {
//					masterEntity.addLigne(masterEntityLigneMP);
//				}
				if(!masterEntity.getLignesMatierePremieres().contains(masterEntityLigneMP)) {
					masterEntity.getLignesMatierePremieres().add(masterEntityLigneMP);
				}
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
		TaLFabricationMP l = null;
		Boolean trouve = valuesLigneMP.remove(selectedTaMatierePremiere);
		if(trouve){
			for (TaLFabricationMP var : masterEntity.getLignesMatierePremieres()) {
				if(selectedTaMatierePremiere.getDto().getNumLigneLDocument()!=null && (var.getNumLigneLDocument()==selectedTaMatierePremiere.getDto().getNumLigneLDocument())) {
					l = var;
				}
			}
			if(l!=null) {
//				l.setTaLot(null);
//				l.getTaMouvementStock().setTaLot(null);
//				masterEntity.getLignesMatierePremieres().remove(l);
				removeLigneMP(l);
				valuesLigneMP=IHMmodelMP();
				//s'assurer que la ligne de mouvement correspondant est bien supprimée du groupe de mouvement aussi
				TaGrMouvStock grpMouvStock = new TaGrMouvStock();
				if(masterEntity.getTaGrMouvStock()!=null) {
					grpMouvStock=masterEntity.getTaGrMouvStock();
					grpMouvStock.getTaMouvementStockes().remove(l.getTaMouvementStock());
				}
			}
			
		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actAffecterMatierePremiere() {
		FacesContext context = FacesContext.getCurrentInstance();  
		
		
		for(ITaLFabrication l : masterEntity.getLignesProduits()) {
			if(l.getTaArticle() !=null && l.getTaArticle().getTaRecetteArticle()!=null 
					&& l.getTaArticle().getTaRecetteArticle().getLignes()!=null
					&& !l.getTaArticle().getTaRecetteArticle().getLignes().isEmpty()) {
				BigDecimal nb = l.getQteLDocument();
				for(TaLRecette lr : l.getTaArticle().getTaRecetteArticle().getLignes()) {
					actInsererMatierePremiere(null);
					selectedTaMatierePremiere.setTaArticle(lr.getTaArticle());
					selectedTaMatierePremiere.getDto().setU1LDocument(lr.getU1LDocument());
					selectedTaMatierePremiere.getDto().setU2LDocument(lr.getU2LDocument());
					selectedTaMatierePremiere.getDto().setLibelleLot(lr.getLibLDocument());
					selectedTaMatierePremiere.getDto().setLibLDocument(lr.getLibLDocument());
					if(lr.getQteLDocument()!=null && nb!=null) {
						selectedTaMatierePremiere.getDto().setQteLDocument(lr.getQteLDocument().multiply(nb));
					}
					if(lr.getQte2LDocument()!=null && nb!=null) {
						selectedTaMatierePremiere.getDto().setQte2LDocument(lr.getQte2LDocument().multiply(nb));
					}
					selectedTaMatierePremiere.updateDTO();
					actEnregistrerMatierePremiere();
				}
			} else {
				if(l.getTaArticle()!=null) {
					context.addMessage(null, new FacesMessage("Fabrication", "L'article "+l.getTaArticle().getCodeArticle()+" n'as pas de liste de MP affecté"));
				}
			}
		}
	}

	public void actInsererMatierePremiere(ActionEvent actionEvent) {
//		masterEntityLigneMP = new TaLFabrication();
//		masterEntityLigneMP.setTaMouvementStock(new TaMouvementStock());
//
//		initOrigineMouvement(masterEntityLigneMP);
		
//		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_INSERTION);
		/***********************************************************************************************************************************************************/
		
		//		if(selectedTaLBonReceptionDTOJSF==null || !selectedTaLBonReceptionDTOJSF.isAutoInsert()) {
		TaLFabricationDTOJSF nouvelleLigne = new TaLFabricationDTOJSF();
		nouvelleLigne.setDto(new TaLFabricationDTO());
		//nouvelleLigne.getDto().setNumLigneLDocument(valuesLigneMP.size()+valuesLignePF.size()+1);
//		nouvelleLigne.getDto().setNumLigneLDocument(calcNextNumLigneDoc());
		nouvelleLigne.getDto().setNumLigneLDocument(valuesLigneMP.size()+1);
//		nouvelleLigne.getDto().setNumLot(selectedTaBonReceptionDTO.getCodeDocument()+"_"+valuesLigne.size()+1);
		if(selectedTaFabricationDTO.getCodeDocument()!=null) {
			nouvelleLigne.getDto().setNumLot(selectedTaFabricationDTO.getCodeDocument()+"_"+nouvelleLigne.getDto().getNumLigneLDocument());
		} else {
			nouvelleLigne.getDto().setNumLot(""+nouvelleLigne.getDto().getNumLigneLDocument());
		}
		
		//			nouvelleLigne.setAutoInsert(true);

		masterEntityLigneMP = new TaLFabricationMP(true); 
		try {
			//H = HT => pas nécessaire, mais il ne faut pas de ligne de commentaire  pour passer trigger "ligne_vide"
			masterEntityLigneMP.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
		
			//List<TaLBonReceptionDTOJSF> modelListeLigneDevis = IHMmodel();

			//masterEntity.getLignesMatierePremieres().add(masterEntityLigneMP);
//			masterEntity.addLigne(masterEntityLigneMP);
			masterEntity.insertLigneMP(masterEntityLigneMP,nouvelleLigne.getDto().getNumLigneLDocument());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		selectedTaFabricationDTO.setNumLigneLDocument(masterEntityLigneMP.getNumLigneLDocument());
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

	public void initOrigineMouvement(ITaLFabrication l) {
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
		
	}

	public void actAnnulerMatierePremiere(){
		List<TaLFabricationMP> lmp = masterEntity.getLignesMatierePremieres();
//		if(masterEntityLigneMP == null 
//				|| masterEntityLigneMP.getTaMouvementStock().getIdMouvementStock() != 0){
//			lmp.add(masterEntityLigneMP);
//		}
//		if(masterEntityLigneMP == null 
//				|| (!lmp.contains(masterEntityLigneMP) && masterEntityLigneMP.getTaMouvementStock()!=null && masterEntityLigneMP.getTaMouvementStock().getIdMouvementStock() != 0)){
//			lmp.add(masterEntityLigneMP);
//		}
		masterEntity.setLignesMatierePremieres(lmp);
		masterEntityLigneMP = new TaLFabricationMP();
		
		valuesLigneMP = IHMmodelMP();
		if(!valuesLigneMP.isEmpty())
			selectionLigneMP(valuesLigneMP.get(0));	
		setInsertAutoMP(false);
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	//************************************************************************************************************//
	//	Produit
	//************************************************************************************************************//
	
	public List<String> emplacementAutoComplete(String query) {
		List<String> filteredValues = new ArrayList<String>();
		if(selectedTaProduit.getTaEntrepot()!=null) {
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
	
	public TaFabricationDTO rempliDTO(){
		if(masterEntity!=null){			
			try {
				refresh();
				selectedTaFabricationDTO = rechercheDansListeValues(masterEntity.getCodeDocument());
				masterEntity = fabricationService.findByCode(masterEntity.getCodeDocument());
//				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				selectedTaFabricationDTOs=null ;
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectedTaFabricationDTO;
	}
	
	public TaFabricationDTO rechercheDansListeValues(String codeDocument){
		for (TaFabricationDTO doc : listeFabrication) {
			if(doc.getCodeDocument().equals(codeDocument))
				return doc;
		}
		return null;
	}

	public void actModifierProduit () {
		
		modeEcranProduit.setMode(EnumModeObjet.C_MO_EDITION);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actModifierProduit")); 
		}
		
	}

	public void actSupprimerProduit () {
		try {
			TaLFabricationPF l = null;
			Boolean trouve = valuesLignePF.remove(selectedTaProduit);
			if(trouve){
				for (TaLFabricationPF var : masterEntity.getLignesProduits()) {
					if(selectedTaProduit.getDto().getNumLigneLDocument()!=null && (var.getNumLigneLDocument()==selectedTaProduit.getDto().getNumLigneLDocument())) {
						l = var;
					}
				}
				if(l!=null) {
					//s'assurer que la ligne de mouvement correspondant est bien supprimée du groupe de mouvement aussi
					masterEntity.getListeLFabPFSuppr().add(l);
					TaGrMouvStock grpMouvStock = new TaGrMouvStock();
					if(masterEntity.getTaGrMouvStock()!=null) {
						grpMouvStock=masterEntity.getTaGrMouvStock();
						grpMouvStock.getTaMouvementStockes().remove(l.getTaMouvementStock());
						l.getTaMouvementStock().setTaLot(null);
					}
//					if(masterEntityLignePF.getTaLot()!=null) {
//						masterEntity.getListeLotSuppr().add(masterEntityLignePF.getTaLot().getIdLot());
//						masterEntityLignePF.setTaLot(lotService.findOrCreateLotVirtuelArticle(l.getTaArticle().getIdArticle()));
//					}
					
					removeLignePF(l);
					valuesLignePF=IHMmodelPF();
					//				listeLFabASupprimer.add(l);


					
					
				}

			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void creerLot() {
		try {
			if(selectedTaProduit.getTaArticle()!=null && selectedTaProduit.getTaArticle().getGestionLot()) {
				if(selectedTaProduit.getTaArticle()!=null ) {
					if(selectedTaProduit.getDto()!=null ) {
						TaLot lot = new TaLot();
						if(selectedTaProduit.getTaLot()!=null) {
							lot = selectedTaProduit.getTaLot();
						} else {
							selectedTaProduit.setTaLot(lot);
						}
						if(taTiersDTO!=null) {
							lot.setTaTiersPrestationService(taTiers);
						}
						if(selectedTaProduit.getDto().getDluo()!=null) {
							lot.setDluo(selectedTaProduit.getDto().getDluo());
						}
						lot.setNumLot(selectedTaProduit.getDto().getNumLot());
						lot.setLibelle(selectedTaProduit.getDto().getLibelleLot());
						lot.setUnite1(masterEntityLignePF.getU1LDocument());
						lot.setUnite2(masterEntityLignePF.getU2LDocument());
						lot.setTaArticle(masterEntityLignePF.getTaArticle());
						lot=lotService.initListeResultatControle(taConformiteService.controleArticleDerniereVersion(lot.getTaArticle().getIdArticle()), lot);
						//					lot =  lot.initListeResultatControle(taConformiteService.controleArticleDerniereVersion(lot.getTaArticle().getIdArticle()));
						if(lot.getTaResultatConformites()==null || lot.getTaResultatConformites().isEmpty() ) {
							//aucun controle définit sur cet article
							//le lot est valide par défaut
							lot.setLotConforme(true);
							lot.setTaStatusConformite(taStatusConformiteService.findByCode(TaStatusConformite.C_TYPE_ACTION_AUCUN_CONTROLE_DEFINIT));
						} else {
							//le lot est invalide par defaut, il faut faire les controles
							lot.setTaStatusConformite(taStatusConformiteService.findByCode(TaStatusConformite.C_TYPE_ACTION_VIDE));
						}
						lot.setDateLot(masterEntity.getDateDocument());
						if(selectedTaProduit.getTaRapport()!=null){
							lot.setNbDecimal(selectedTaProduit.getTaRapport().getNbDecimal());
							lot.setSens(selectedTaProduit.getTaRapport().getSens());
							lot.setRapport(selectedTaProduit.getTaRapport().getRapport());
						}
						if(!gestionLot  || !selectedTaProduit.getTaArticle().getGestionLot()){
							lot.setVirtuel(true);
							lot.setVirtuelUnique(true);
						}
						masterEntityLignePF.setTaLot(lot);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrerProduit() {	

		try {
			selectedTaProduit.updateDTO();
//			masterEntityLignePF.setPf(true);
			masterEntityLignePF.setTaArticle(selectedTaProduit.getTaArticle());
			masterEntityLignePF.setTaEntrepot(selectedTaProduit.getTaEntrepot());
			masterEntityLignePF.setEmplacementLDocument(selectedTaProduit.getDto().getEmplacement());
			
			LgrDozerMapper<TaLFabricationDTO,TaLFabricationPF> mapper = new LgrDozerMapper<TaLFabricationDTO,TaLFabricationPF>();
			mapper.map((TaLFabricationDTO) selectedTaProduit.getDto(),masterEntityLignePF);

			creerLot();
			
			if(selectedTaProduit.getListeCertification()!=null) {
				Set<TaLabelArticle> sc = new HashSet<>();
				sc.addAll(selectedTaProduit.getListeCertification());
				masterEntityLignePF.getTaLot().setTaLabelArticles(sc);
			}
			
			masterEntity.enregistreLigne(masterEntityLignePF);
			//if(modeEcranProduit.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
//				if(!masterEntity.getLignes().contains(masterEntityLignePF)) {
//					masterEntity.addLigne(masterEntityLignePF);	
//				}
				if(!masterEntity.getLignesProduits().contains(masterEntityLignePF)) {
					masterEntity.getLignesProduits().add(masterEntityLignePF);
				}
			}
			modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
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
	
	public int calcNextNumLigneDoc() {
		int num = -1;
		List<Integer> l = new ArrayList<>();
		for (TaLFabricationDTOJSF lf : valuesLigneMP) {
			l.add(lf.getDto().getNumLigneLDocument());
		}
		for (TaLFabricationDTOJSF lf : valuesLignePF) {
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

	public void actInsererProduit(ActionEvent actionEvent) {

//		if(selectedTaLBonReceptionDTOJSF==null || !selectedTaLBonReceptionDTOJSF.isAutoInsert()) {
		TaLFabricationDTOJSF nouvelleLigne = new TaLFabricationDTOJSF();
		nouvelleLigne.setDto(new TaLFabricationDTO());
		//nouvelleLigne.getDto().setNumLigneLDocument(valuesLigneMP.size()+valuesLignePF.size()+1);
//		nouvelleLigne.getDto().setNumLigneLDocument(calcNextNumLigneDoc());
		nouvelleLigne.getDto().setNumLigneLDocument(valuesLignePF.size()+1);
//		nouvelleLigne.getDto().setNumLot(selectedTaFabricationDTO.getCodeDocument()+"_"+(valuesLignePF.size()+1));
//		
//		//On utilisera le générateur quand on pourra reprendre le code du document courant dans les paramètres du générateur
//		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
//		Map<String, String> params = new LinkedHashMap<String, String>();
//		if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
//		if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
//		params.put(Const.C_CODEDOCUMENT, selectedTaFabricationDTO.getCodeDocument());
//		nouvelleLigne.getDto().setNumLot(lotService.genereCode(params));		
		
		//			nouvelleLigne.setAutoInsert(true);

		masterEntityLignePF = new TaLFabricationPF(true); 
		try {
			//H = HT => pas nécessaire, mais il ne faut pas de ligne de commentaire  pour passer trigger "ligne_vide"
			masterEntityLignePF.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
		
			//List<TaLBonReceptionDTOJSF> modelListeLigneDevis = IHMmodel();

			//masterEntity.getLignesProduits().add(masterEntityLignePF);
//			masterEntity.addLigne(masterEntityLignePF);
			masterEntity.insertLignePF(masterEntityLignePF,nouvelleLigne.getDto().getNumLigneLDocument()); //ne fonctionne pas directement comme les autres document car pour la fabrication il y a 2 liste de lignes PF et MP, a surcharger si besoin
		

			selectedTaFabricationDTO.setNumLigneLDocument(masterEntityLignePF.getNumLigneLDocument());
			masterEntityLignePF.setTaDocument(masterEntity);
			
			//initialisation des certification du lot/ de la ligne avec celles du document/fab
			nouvelleLigne.setListeCertificationDTO(selectedTaFabricationDTO.getTaLabelArticleDTOs());
			if(nouvelleLigne.getListeCertificationDTO()!=null && !nouvelleLigne.getListeCertificationDTO().isEmpty()) {
				List<TaLabelArticle> l = new ArrayList<>();
				for (TaLabelArticleDTO e : selectedTaFabricationDTO.getTaLabelArticleDTOs()) {
					l.add(taLabelArticleService.findById(e.getId()));
				} 
				nouvelleLigne.setListeCertification(l);
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		masterEntityLignePF.initialiseLigne(true);
		
		initOrigineMouvement(masterEntityLignePF);

		valuesLignePF.add(nouvelleLigne);
		
//		l'affectation du selected avec nouvelleLigne est importante pour
//		la création sur modèle -> donc je l'ai remis
		selectedTaProduit = nouvelleLigne;

		modeEcranProduit.setMode(EnumModeObjet.C_MO_INSERTION);
	}
	
	public void actAnnulerProduit () {
		
		if(masterEntityLignePF.getTaLot()!=null && masterEntityLignePF.getTaLot().getNumLot()!=null) {
			lotService.annuleCode(masterEntityLignePF.getTaLot().getNumLot());
		}
		
		List<TaLFabricationPF> lmp = masterEntity.getLignesProduits();
//		if(masterEntityLignePF == null || masterEntityLignePF.getTaMouvementStock().getIdMouvementStock() != 0){
//			lmp.add(masterEntityLignePF);
//		}
//		if(masterEntityLignePF == null || (!lmp.contains(masterEntityLignePF) && masterEntityLignePF.getTaMouvementStock()!=null && masterEntityLignePF.getTaMouvementStock().getIdMouvementStock() != 0)){
//			lmp.add(masterEntityLignePF);
//		}

		masterEntity.setLignesProduits(lmp);
		masterEntityLignePF = new TaLFabricationPF();
		
		valuesLignePF = IHMmodelPF();
		if(!valuesLignePF.isEmpty())
			selectionLignePF(valuesLignePF.get(0));	
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
	
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Fabrication", 
						"Chargement de fabrication N°"+selectedTaFabricationDTO.getCodeDocument()
						)); 
			} else {
				tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_FABRICATION);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	public void updateTab(){
		try {
			if(selectedTaFabricationDTOs!=null && selectedTaFabricationDTOs.length>0) {
				selectedTaFabricationDTO = selectedTaFabricationDTOs[0];
			}
			if(selectedTaFabricationDTO.getId()!=null && selectedTaFabricationDTO.getId()!=0) {
				masterEntity = fabricationService.findById(selectedTaFabricationDTO.getId());
			}
			//séparer en 2 listes dans l'entité les lignes de fabrication
			masterEntity.findProduit();
			
//			masterEntity = taBonReceptionService.findById(selectedTaBonReceptionDTO.getId());
			valuesLigneMP = IHMmodelMP();
			valuesLignePF = IHMmodelPF();
			
			mapperModelToUI.map(masterEntity, selectedTaFabricationDTO);
			
			autoCompleteMapDTOtoUI();
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Fabrication", 
						"Chargement de fabrication N°"+selectedTaFabricationDTO.getCodeDocument()
						)); 
			}
		
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FABRICATION);
		b.setTitre("Fabrication");
		b.setTemplate("solstyce/fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FABRICATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FABRICATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		updateTab();
		scrollToTop();

	} 
	
	public void onRowSelectLignePF(SelectEvent event) {  
		selectionLignePF((TaLFabricationDTOJSF) event.getObject());		
	}
	
	public void selectionLignePF(TaLFabricationDTOJSF ligne){
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
//			if(selectedTaProduit.getDto().getIdLDocument()!=null &&  selectedTaProduit.getDto().getIdLDocument()!=0) {
//				masterEntityLignePF=rechercheLignePF(selectedTaProduit.getDto().getIdLDocument());
//			}
			if(selectedTaProduit.getDto().getIdLDocument()!=null &&  selectedTaProduit.getDto().getIdLDocument()!=0) {
				masterEntityLignePF = rechercheLignePF(selectedTaProduit.getDto().getIdLDocument());
			} else if(selectedTaProduit.getDto().getNumLigneLDocument()!=null /*&&  selectedTaProduit.getDto().getNumLigneLDocument()!=0*/) {
				masterEntityLignePF = rechercheLignePFNumLigne(selectedTaProduit.getDto().getNumLigneLDocument());
			}
			
//			if(m.get(masterEntityLignePF.getIdLDocument())!=null) {
//				masterEntityLignePF.getTaMouvementStock().setTaLot(m.get(masterEntityLignePF.getIdLDocument()));
//				m.put(masterEntityLignePF.getIdLDocument(), m.get(masterEntityLignePF.getIdLDocument()));
//			}
			
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
			selectedTaProduit=(TaLFabricationDTOJSF) event.getObject();		
		actModifierProduit();
	}
	
	public TaLFabricationPF rechercheLignePF(int id){
		TaLFabricationPF obj=masterEntityLignePF;
		for (TaLFabricationPF ligne : masterEntity.getLignesProduits()) {
			if(ligne.getIdLDocument()==id)
				obj=ligne;
		}
		return obj;
	}
	
	public TaLFabricationPF rechercheLignePFNumLigne(int numLigne){
		TaLFabricationPF obj=masterEntityLignePF;
		for (TaLFabricationPF ligne : masterEntity.getLignesProduits()) {
			if(ligne.getNumLigneLDocument()==numLigne)
				obj=ligne;
		}
		return obj;
	}
	
	public void onRowSelectLigneMP(SelectEvent event) {  
		selectionLigneMP((TaLFabricationDTOJSF) event.getObject());		
	}
	
	public void selectionLigneMP(TaLFabricationDTOJSF ligne){
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
				masterEntityLigneMP = rechercheLigneMP(selectedTaMatierePremiere.getDto().getIdLDocument());
			} else if(selectedTaMatierePremiere.getDto().getNumLigneLDocument()!=null /*&&  selectedTaMatierePremiere.getDto().getNumLigneLDocument()!=0*/) {
				masterEntityLigneMP = rechercheLigneMPNumLigne(selectedTaMatierePremiere.getDto().getNumLigneLDocument());
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
			setInsertAutoMP(modeEcran.dataSetEnInsertion());
		} else {
			setInsertAutoMP(false);
		}
		if(event.getObject()!=null && !event.getObject().equals(selectedTaMatierePremiere))
			selectedTaMatierePremiere=(TaLFabricationDTOJSF) event.getObject();	
		actModifierMatierePremiere();
	}
	
	public TaLFabricationMP rechercheLigneMP(int id){
		TaLFabricationMP obj = masterEntityLigneMP;
		for (TaLFabricationMP ligne : masterEntity.getLignesMatierePremieres()) {
			if(ligne.getIdLDocument()==id)
				obj=ligne;
		}
		return obj;
	}
	
	public TaLFabricationMP rechercheLigneMPNumLigne(int numLigne){
		TaLFabricationMP obj=masterEntityLigneMP;
		for (TaLFabricationMP ligne : masterEntity.getLignesMatierePremieres()) {
			if(ligne.getNumLigneLDocument()==numLigne)
				obj=ligne;
		}
		return obj;
	}
	
	public void validateLigneFabricationAvantEnregistrement(Object value) throws ValidatorException {

		String msg = "";

		try {
			if(((TaLFabricationDTOJSF)value).getDto().getNumLot()==null ||((TaLFabricationDTOJSF)value).getDto().getNumLot().equals("")){
//				throw new Exception("Le lot doit être renseigné");
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lot", "Le lot doit être renseigné"));
			}
//			taLFabricationService.validateDTOProperty(((TaLFabricationDTOJSF)value).getDto(),Const.C_CODE_ARTICLE,  ITaLFabricationServiceRemote.validationContext );
//			taLFabricationService.validateDTOProperty(((TaLFabricationDTOJSF)value).getDto(),Const.C_NUM_LOT,  ITaLFabricationServiceRemote.validationContext );
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void actAutoInsererLignePF(ActionEvent actionEvent) /*throws Exception*/ {
		boolean existeDeja=false;
		if(insertAutoPF && insertAutoEnabledPF) {
			for (TaLFabricationDTOJSF ligne : valuesLignePF) {
				if(ligne.ligneEstVide())existeDeja=true;
			}
			if(!existeDeja){
				actInsererProduit(actionEvent);
			
			String oncomplete="jQuery('.myclassPF .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
			PrimeFaces.current().executeScript(oncomplete);
			}
		} else {
//			throw new Exception();
		}
	}
	
	public void actAutoInsererLigneMP(ActionEvent actionEvent) /*throws Exception*/ {
		boolean existeDeja=false;
		if(insertAutoMP && insertAutoEnabledMP) {
			for (TaLFabricationDTOJSF ligne : valuesLigneMP) {
				if(ligne.ligneEstVide())existeDeja=true;
			}
			if(!existeDeja){
				actInsererMatierePremiere(actionEvent);
			
			String oncomplete="jQuery('.myclassMP .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
			PrimeFaces.current().executeScript(oncomplete);
			}
		} else {
//			throw new Exception();
		}
	}
	//************************************************************************************************************//
	//	Commun
	//************************************************************************************************************//


	public void setMasterEntity(TaFabrication fabrication) {
		this.masterEntity = fabrication;
	}

	public void setSelectedTaMatierePremiere(
			TaLFabricationDTOJSF selectedTaMatierePremiere) {
		this.selectedTaMatierePremiere = selectedTaMatierePremiere;
	}

	public void setSelectedTaProduit(TaLFabricationDTOJSF selectedTaProduit) {
		this.selectedTaProduit = selectedTaProduit;
	}

	public void setMasterEntityLigneMP(TaLFabricationMP newTaMatierePremiere) {
		this.masterEntityLigneMP = newTaMatierePremiere;
	}

	public void setMasterEntityLignePF(TaLFabricationPF newTaProduit) {
		this.masterEntityLignePF = newTaProduit;
	}

	public TaFabrication getMasterEntity() {
		return masterEntity;
	}

	public TaLFabricationDTOJSF getSelectedTaMatierePremiere() {
		return selectedTaMatierePremiere;
	}

	public TaLFabricationDTOJSF getSelectedTaProduit() {
		return selectedTaProduit;
	}

	public TaLFabricationMP getMasterEntityLigneMP() {
		return masterEntityLigneMP;
	}

	public TaLFabricationPF getMasterEntityLignePF() {
		return masterEntityLignePF;
	}

	public List<ArticleLotEntrepotDTO> getListArticleLotEntrepot() {
		return listArticleLotEntrepot;
	}

	public void setListArticleLotEntrepot(List<ArticleLotEntrepotDTO> listArticleLotEntrepot) {
		this.listArticleLotEntrepot = listArticleLotEntrepot;
	}

	public List<TaFabricationDTO> getListeFabrication() {
		return listeFabrication;
	}

	public TaFabricationDTO getSelectedTaFabricationDTO() {
		return selectedTaFabricationDTO;
	}

	public void setSelectedTaFabricationDTO(
			TaFabricationDTO selectedTaFabricationDTO) {
		this.selectedTaFabricationDTO = selectedTaFabricationDTO;
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
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
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
	
	public List<TaArticleDTO> articleAutoCompleteDTOLightMP(String query) {
		List<TaArticleDTO> allValues = articleService.findByCodeLightMP(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaArticleDTO> articleAutoCompleteDTOLightPF(String query) {
		List<TaArticleDTO> allValues = articleService.findByCodeLightPF(query.toUpperCase());
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
			if(query.equals("*") || civ.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
//	public List<ArticleLotEntrepotDTO> lotAutoComplete(String query) {
//		TaArticle articleCourant = null;
//		List<ArticleLotEntrepotDTO> liste;
//		try {
//			articleCourant = articleService.findById(selectedTaMatierePremiere.getTaArticle().getIdArticle());
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(articleCourant.getGestionLot())
//		return onChangeListArticleMP(selectedTaMatierePremiere.getTaArticle().getCodeArticle(),query);
//		else {
//			TaLot lot=lotService.findOrCreateLotVirtuelArticle(selectedTaMatierePremiere.getTaArticle().getIdArticle());
//			liste=new ArrayList<>();
//			ArticleLotEntrepotDTO artlot=new ArticleLotEntrepotDTO();
//			artlot.setIdLot(lot.getIdLot());
//			artlot.setNumLot(lot.getNumLot());
//			artlot.setLibelleLot(lot.getLibelle());
//			liste.add(artlot);
//			return liste;
//		}
//	}
	
	public void actChangeCertificationLot(AjaxBehaviorEvent evt) {
		System.out.println("FabricationController.actChangeCertificationLot()");
		try {
			if(selectedTaProduit.getListeCertificationDTO()!=null && !selectedTaProduit.getListeCertificationDTO().isEmpty()) {
				List<TaLabelArticle> l = new ArrayList<>();
				for (TaLabelArticleDTO e : selectedTaProduit.getListeCertificationDTO()) {
					System.out.println("----" + e.getCodeLabelArticle());
					l.add(taLabelArticleService.findById(e.getId()));
				} 
				selectedTaProduit.setListeCertification(l);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
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
			if(value instanceof TaTFabricationDTO && ((TaTFabricationDTO) value).getCodeTFabrication()!=null && ((TaTFabricationDTO) value).getCodeTFabrication().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaEntrepotDTO && ((TaEntrepotDTO) value).getCodeEntrepot()!=null && ((TaEntrepotDTO) value).getCodeEntrepot().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
		}

		validateUIField(nomChamp,value);
	}
	
	public void autcompleteUnSelect(UnselectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

//		if(value!=null) {
//			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaFamilleDTO && ((TaFamilleDTO) value).getCodeFamille()!=null && ((TaFamilleDTO) value).getCodeFamille().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaTvaDTO && ((TaTvaDTO) value).getCodeTva()!=null && ((TaTvaDTO) value).getCodeTva().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaTTvaDTO && ((TaTTvaDTO) value).getCodeTTva()!=null && ((TaTTvaDTO) value).getCodeTTva().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaMarqueArticle && ((TaMarqueArticle) value).getCodeMarqueArticle()!=null && ((TaMarqueArticle) value).getCodeMarqueArticle().equals(Const.C_AUCUN))value=null;	
//		}
//		
//		//validateUIField(nomChamp,value);
		try {
			if(nomChamp.equals(Const.C_CODE_LABEL_ARTICLE)) {
				TaLabelArticle entity =null;
				if(value!=null){
					if(value instanceof TaLabelArticleDTO){
	//				entity=(TaFamille) value;
						entity = taLabelArticleService.findByCode(((TaLabelArticleDTO)value).getCodeLabelArticle());
					}else{
						entity = taLabelArticleService.findByCode((String)value);
					}
				}else{
//					selectedTaFabricationDTO.setCodeFamille("");
				}
				//taArticle.setTaFamille(entity);
				for (TaLabelArticle f : masterEntity.getTaLabelArticles()) {
					if(f.getIdLabelArticle()==((TaLabelArticleDTO)value).getId())
						entity = f;
				}
				masterEntity.getTaLabelArticles().remove(entity);
//				if(masterEntity.getTaFamille()!=null && masterEntity.getTaFamille().getCodeFamille().equals(entity.getCodeFamille())) {
//					masterEntity.setTaFamille(null);
//					taFamilleDTO = null;
//					if(!masterEntity.getTaLabelArticles().isEmpty()) {
//						masterEntity.setTaFamille(masterEntity.getTaLabelArticles().iterator().next());
//						//taFamilleDTO = null;
//					}
//				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = tiersService.findByCodeLight(query);
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaUtilisateurDTO> utilisateurAutoCompleteDTOLight(String query) {
		List<TaUtilisateurDTO> allValues = utilisateurService.findByCodeLight(query);
		List<TaUtilisateurDTO> filteredValues = new ArrayList<TaUtilisateurDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaUtilisateurDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getEmail().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaLabelArticleDTO> certificationAutoCompleteLight(String query) {
        List<TaLabelArticleDTO> allValues = taLabelArticleService.selectAllDTO();
        List<TaLabelArticleDTO> filteredValues = new ArrayList<TaLabelArticleDTO>();
        TaLabelArticleDTO civ = new TaLabelArticleDTO();
//        civ.setCodeFamille(Const.C_AUCUN); // il y en a pas besoin car on peut supprimer avec la croix
//        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeLabelArticle().toUpperCase().contains(query.toUpperCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
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
			selectedTaFabricationDTO.setCodeTFabrication(taTFabricationDTO.getCodeTFabrication());
		}
		if(taTiersDTO!=null) {
			validateUIField(Const.C_CODE_TIERS_PRESTATION,taTiersDTO);
			selectedTaFabricationDTO.setCodeTiersPrestation(taTiersDTO.getCodeTiers());
		}
		if(redacteurDTO!=null) {
			validateUIField(Const.C_NOM_REDACTEUR,redacteurDTO);
			selectedTaFabricationDTO.setNomRedacteur(redacteurDTO.getEmail());
		}
		if(controleurDTO!=null) {
			validateUIField(Const.C_NOM_CONTROLEUR,controleurDTO);
			selectedTaFabricationDTO.setNomControleur(controleurDTO.getEmail());
		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTFabrication = null;
			taTFabricationDTO = null;
			
			taTiers = null;
			taTiersDTO = null;
			
			redacteur = null;
			redacteurDTO = null;
			
			controleur = null;
			controleurDTO = null;
			
			//init des DTO multiple (autocomplete multiple)
			if(masterEntity!=null && masterEntity.getTaLabelArticles()!=null && !masterEntity.getTaLabelArticles().isEmpty()) {
				selectedTaFabricationDTO.setTaLabelArticleDTOs(new ArrayList<>());
				for (TaLabelArticle f : masterEntity.getTaLabelArticles()) {
					selectedTaFabricationDTO.getTaLabelArticleDTOs().add(mapperCertification.map(f, TaLabelArticleDTO.class));
				}
			}

			if(selectedTaFabricationDTO.getCodeTFabrication()!=null && !selectedTaFabricationDTO.getCodeTFabrication().equals("")) {
				taTFabrication = taTFabricationService.findByCode(selectedTaFabricationDTO.getCodeTFabrication());
				taTFabricationDTO = mapperModelToUITFabrication.map(taTFabrication, TaTFabricationDTO.class);
			}
			
			if(selectedTaFabricationDTO.getCodeTiersPrestation()!=null && !selectedTaFabricationDTO.getCodeTiersPrestation().equals("")) {
				taTiers = tiersService.findByCode(selectedTaFabricationDTO.getCodeTiersPrestation());
				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
			}
			
			if(selectedTaFabricationDTO.getNomRedacteur()!=null && !selectedTaFabricationDTO.getNomRedacteur().equals("")) {
				redacteur = utilisateurService.findByCode(selectedTaFabricationDTO.getNomRedacteur());
				redacteurDTO = mapperModelToUIUtilisateur.map(redacteur, TaUtilisateurDTO.class);
			}
			
			if(selectedTaFabricationDTO.getNomControleur()!=null && !selectedTaFabricationDTO.getNomControleur().equals("")) {
				controleur = utilisateurService.findByCode(selectedTaFabricationDTO.getNomControleur());
				controleurDTO = mapperModelToUIUtilisateur.map(controleur, TaUtilisateurDTO.class);
			}
			if(selectedTaFabricationDTO.getTaLabelArticleDTOs()!=null) {
				if(taLabelArticles==null) {
					taLabelArticles = new ArrayList<>();
				}
				if(selectedTaFabricationDTO.getTaLabelArticleDTOs()==null) {
					selectedTaFabricationDTO.setTaLabelArticleDTOs(new ArrayList<>());
				}
				for (TaLabelArticleDTO f : selectedTaFabricationDTO.getTaLabelArticleDTOs()) {
					if(f.getCodeLabelArticle()!=null && !f.getCodeLabelArticle().equals("")) {
						taLabelArticle = taLabelArticleService.findByCode(f.getCodeLabelArticle());
						taLabelArticles.add(taLabelArticle);
						
					}
				}
			}
		
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public List<TaTFabricationDTO> typeFabricationAutoCompleteDTOLight(String query) {
		List<TaTFabricationDTO> allValues = taTFabricationService.findByCodeLight(query);
		List<TaTFabricationDTO> filteredValues = new ArrayList<TaTFabricationDTO>();
		TaTFabricationDTO civ = new TaTFabricationDTO();
		civ.setCodeTFabrication(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTFabrication().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS_PRESTATION)) {
				TaTiers entity = null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
//						entity=(TaTiers) value;
						entity = tiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = tiersService.findByCode((String)value);
					}
					
				}
				changement=masterEntity.getTaTiersPrestationService()==null || !entity.equalsCode(masterEntity.getTaTiersPrestationService());
				masterEntity.setTaTiersPrestationService(entity);

				if(changement){
					String nomTiers=entity.getNomTiers();
					((TaFabricationDTO)selectedTaFabricationDTO).setLibelleDocument("Fabrication N°"+selectedTaFabricationDTO.getCodeDocument()+" - pour "+nomTiers);																			
				}					

			} else if(nomChamp.equals(Const.C_CODE_T_FABRICATION)) {
				TaTFabrication entity = null;
				if(value!=null){
					if(value instanceof TaTFabricationDTO){
//						entity=(TaTiers) value;
						entity = taTFabricationService.findByCode(((TaTFabricationDTO)value).getCodeTFabrication());
					}else{
						entity = taTFabricationService.findByCode((String)value);
					}
					changement=!entity.equalsCode(masterEntity.getTaTFabrication());
					masterEntity.setTaTFabrication(entity);
				}else {
					changement=masterEntity.getTaTFabrication()!=null;
					masterEntity.setTaTFabrication(null);
					selectedTaFabricationDTO.setCodeTFabrication("");
					selectedTaFabricationDTO.setLiblTFabrication("");
					taTFabricationDTO=null;
					
				}
				
				if(changement){
					Map<String, String> params = new LinkedHashMap<String, String>();
					if(masterEntity!=null && masterEntity.getTaTFabrication()!=null)
						params.put(Const.C_CODETYPE, masterEntity.getTaTFabrication().getCodeTFabrication());						
					if(selectedTaFabricationDTO.getCodeDocument()!=null) {
						fabricationService.annuleCode(selectedTaFabricationDTO.getCodeDocument());
					}
					String newCode=fabricationService.genereCode(params);
					if(!newCode.equals(""))selectedTaFabricationDTO.setCodeDocument(newCode);					
				}
			}else if(nomChamp.equals(Const.C_NUM_LOT)) {
			
			}else if(nomChamp.equals(Const.C_NOM_REDACTEUR)) {
				TaUtilisateur entity = null;
				if(value!=null){
					if(value instanceof TaUtilisateurDTO){
//						entity=(TaUtilisateur) value;
						entity = utilisateurService.findByCode(((TaUtilisateurDTO)value).getEmail());
					}else{
						entity = utilisateurService.findByCode((String)value);
					}
					
				}
				masterEntity.setTaUtilisateurRedacteur(entity);
			}else if(nomChamp.equals(Const.C_NOM_CONTROLEUR)) {
				TaUtilisateur entity = null;
				if(value!=null){
					if(value instanceof TaUtilisateurDTO){
//						entity=(TaUtilisateur) value;
						entity = utilisateurService.findByCode(((TaUtilisateurDTO)value).getEmail());
					}else{
						entity = utilisateurService.findByCode((String)value);
					}
					
				}
				masterEntity.setTaUtilisateurControleur(entity);
			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {

				
			}else if(nomChamp.equals(Const.C_DATE_DEB_RELLE_FABRICATION)) {
				if(majDlc)majDLUO();
				
			}else if(nomChamp.equals(Const.C_CODE_LABEL_ARTICLE)) {
				TaLabelArticle entity =null;
				if(value!=null){
					if(value instanceof TaLabelArticleDTO){
						entity = taLabelArticleService.findByCode(((TaLabelArticleDTO)value).getCodeLabelArticle());
						masterEntity.getTaLabelArticles().add(entity);
						//selectedTaFabricationDTO.getTaLabelArticleDTOs().add((TaLabelArticleDTO)value);
					}else{
						entity = taLabelArticleService.findByCode((String)value);
					}
				}
			} else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
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

				if(changementArticleLigne) {
					selectedTaProduit.getDto().setLibLDocument(entity.getLibellecArticle());
					selectedTaProduit.getDto().setDluo(LibDate.incrementDate(selectedTaFabricationDTO.getDateDebR(), LibConversion.stringToInteger(entity.getParamDluo(), 0) , 0, 0));
					selectedTaProduit.getDto().setLibelleLot(entity.getLibellecArticle());

					if(selectedTaProduit.getDto().getNumLot()!=null)lotService.annuleCode(selectedTaProduit.getDto().getNumLot());
					

//					selectedTaProduit.getDto().setNumLot(selectedTaFabricationDTO.getCodeDocument()+"_"+selectedTaProduit.getDto().getNumLigneLDocument());					
					taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
					Map<String, String> params = new LinkedHashMap<String, String>();
					if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
					if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
					if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
					params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedTaFabricationDTO.getDateDebR()));
					params.put(Const.C_CODEDOCUMENT, selectedTaFabricationDTO.getCodeDocument());
					if(!entity.getGestionLot()){
						params.put(Const.C_VIRTUEL, "true");
						params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(entity.getIdArticle()));
					}
//					if(prefLot!=null && LibConversion.StringToBoolean(prefLot.getValeur()) != null && LibConversion.StringToBoolean(prefLot.getValeur())) {
//						selectedTaProduit.getDto().setNumLot(taLFabricationPFService.genereCode(params));
//					}else	selectedTaProduit.getDto().setNumLot(lotService.genereCode(params));
					
					// modification suite à suppression de la cascade remove dans TalBonReception.taLot
					if(selectedTaProduit.getTaArticle()!=null && selectedTaProduit.getTaArticle().getGestionLot()) {
						masterEntityLignePF.setTaLot(null);
						selectedTaProduit.setTaLot(null);

						//si gestion des lots général et article aussi
						if(prefLot!=null && LibConversion.StringToBoolean(prefLot.getValeur()) != null && LibConversion.StringToBoolean(prefLot.getValeur())) 				
							selectedTaProduit.getDto().setNumLot(taLFabricationPFService.genereCode(params));
						else						
							selectedTaProduit.getDto().setNumLot(lotService.genereCode(params));	
					}else {
						//sinon on récupère le lot virtuel correspondant sans en créer un nouveau
						TaLot l =lotService.findOrCreateLotVirtuelArticle(entity.getIdArticle());
						if(l!=null)selectedTaProduit.getDto().setNumLot(l.getNumLot());
						selectedTaProduit.setTaLot(l);
						masterEntityLignePF.setTaLot(l);
					}
				}
				
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
//						selectedTaProduit.setTaUnite1(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
//					}
//					if(coef.getUniteB()!=null) {
//						selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//						selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//					}
//				}else if(entity!=null){
//					if(entity.getTaUnite1()!=null) {
//						selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
//						selectedTaProduit.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
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
//								selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
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
						selectedTaProduit.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
						selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
					}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedTaProduit.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
						selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
					}
				}else
					if(entity!=null ){
						if(entity.getTaUnite1()!=null) {
							selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
							selectedTaProduit.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
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
									selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
									}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
										selectedTaProduit.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
										selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
									}
								}else if(obj.getTaUnite2()!=null){
									selectedTaProduit.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());
									selectedTaProduit.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
								}
							}							
						}
					}				
				if(entity.getTaPrix()!=null){
					if(entity.getTaUnite1()!=null) {
						selectedTaProduit.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaProduit.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
							if(obj!=null){
								if(obj.getTaUnite2()!=null)
									coef=taCoefficientUniteService.findByCode(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
							}
							selectedTaProduit.setTaCoefficientUnite(coef);
							if(coef!=null) {
								selectedTaProduit.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
								selectedTaProduit.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
							}
							if(selectedTaProduit.getTaCoefficientUnite()!=null)
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
//				if(value!=null && selectedTaProduit.getTaRapport()!=null && selectedTaProduit.getTaRapport().getRapport()!=null){
//					switch (selectedTaProduit.getTaRapport().getSens()) {
//					case 1:
//						selectedTaProduit.getDto().setQte2LDocument(((BigDecimal)value).multiply(selectedTaProduit.getTaRapport().getRapport()));
//						break;
//					case 0:
//						selectedTaProduit.getDto().setQte2LDocument(((BigDecimal)value).divide(selectedTaProduit.getTaRapport().getRapport()
//								,MathContext.DECIMAL128).setScale(selectedTaProduit.getTaRapport().getNbDecimal(),BigDecimal.ROUND_HALF_UP));
//						break;
//					default:
//						break;
//					} 
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
					BigDecimal qte=BigDecimal.ZERO;
					if(value==null) {
						masterEntityLignePF.setQte2LDocument(null);
					}else if(!value.equals("")){
						qte=(BigDecimal)value;
					}
					selectedTaProduit.getDto().setQte2LDocument(qte);
				
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

			}if(nomChamp.equals(Const.C_CODE_T_FABRICATION)) {
				TaTFabrication entity = null;
				if(value!=null){
					if(value instanceof TaTFabricationDTO){
//						entity=(TaTiers) value;
						entity = taTFabricationService.findByCode(((TaTFabricationDTO)value).getCodeTFabrication());
					}else{
						entity = taTFabricationService.findByCode((String)value);
					}
				}
				masterEntity.setTaTFabrication(entity);
				Map<String, String> params = new LinkedHashMap<String, String>();
				if(masterEntity!=null && masterEntity.getTaTFabrication()!=null)
					params.put(Const.C_CODETYPE, masterEntity.getTaTFabrication().getCodeTFabrication());
				if(selectedTaFabricationDTO.getCodeDocument()!=null) {
					fabricationService.annuleCode(selectedTaFabricationDTO.getCodeDocument());
				}				
				String newCode=fabricationService.genereCode(params);
				if(!newCode.equals(""))selectedTaFabricationDTO.setCodeDocument(newCode);
				
		}else if(nomChamp.equals(Const.C_NUM_LOT)) {
				selectedTaMatierePremiere.getDto().setEmplacement(null);
				if( selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement()!=null 
						&& !selectedTaMatierePremiere.getArticleLotEntrepotDTO().getEmplacement().equals("")) {
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
				if(selectedTaMatierePremiere.getDto().getLibLDocument()==null || selectedTaMatierePremiere.getDto().getLibLDocument().isEmpty())
					selectedTaMatierePremiere.getDto().setLibLDocument("lot : "+lot.getNumLot());
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
				changement=selectedTaMatierePremiere.getTaArticle()==null || !selectedTaMatierePremiere.getTaArticle().equalsCodeArticle(entity);
				selectedTaMatierePremiere.setTaArticle(entity);
				if(changement){
					if(selectedTaMatierePremiere.getArticleLotEntrepotDTO()!=null)
						selectedTaMatierePremiere.getArticleLotEntrepotDTO().setNumLot(null);
					selectedTaMatierePremiere.setTaLot(null);
					selectedTaMatierePremiere.getDto().setNumLot(null);
					if(!entity.getGestionLot()){
						ArticleLotEntrepotDTO articleLotEntrepotDTO;
						List<ArticleLotEntrepotDTO> liste= lotAutoComplete("");
						if(liste!=null && !liste.isEmpty()){
							articleLotEntrepotDTO=liste.get(0);
							articleLotEntrepotDTO.setGestionLot(false);
							TaLot lot=lotService.findByCode(articleLotEntrepotDTO.getNumLot());
							selectedTaMatierePremiere.setArticleLotEntrepotDTO(articleLotEntrepotDTO);
							selectedTaMatierePremiere.setTaLot(lot);
							selectedTaMatierePremiere.getDto().setNumLot(articleLotEntrepotDTO.getNumLot());
							selectedTaMatierePremiere.getDto().setCodeEntrepot(articleLotEntrepotDTO.getCodeEntrepot());
							selectedTaMatierePremiere.getDto().setEmplacement(articleLotEntrepotDTO.getEmplacement());
						}
					}else{
						if(selectedTaMatierePremiere.getArticleLotEntrepotDTO()!=null)
							selectedTaMatierePremiere.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
					}
				}
				//masterEntityLigne.setTaArticle(entity);
				selectedTaMatierePremiere.getDto().setLibLDocument(entity.getLibellecArticle());
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
						selectedTaMatierePremiere.setTaUnite1(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
					}
					if(coef.getUniteB()!=null) {
						selectedTaMatierePremiere.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
						selectedTaMatierePremiere.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
					}
				}else if(entity!=null){
					if(entity.getTaUnite1()!=null) {
						selectedTaMatierePremiere.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaMatierePremiere.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
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
								selectedTaMatierePremiere.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
							}
						}							
					}
				}
				if(entity.getTaPrix()!=null){
					if(entity.getTaUnite1()!=null) {
						selectedTaMatierePremiere.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedTaMatierePremiere.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaMatierePremiere.getDto().getU1LDocument())){
							if(obj!=null){
								if(obj.getTaUnite2()!=null) {
									coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
								}
							}
							selectedTaMatierePremiere.setTaCoefficientUnite(coef);
							if(coef!=null) {
								selectedTaMatierePremiere.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
								selectedTaMatierePremiere.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
							}
						}							
					}
//					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaMatierePremiere.getDto().getU1LDocument())){
//							selectedTaMatierePremiere.setTaRapport(obj);
//							selectedTaMatierePremiere.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());
//							selectedTaMatierePremiere.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
//						}							
//					}
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
//				if(value!=null && selectedTaMatierePremiere.getTaRapport()!=null && selectedTaMatierePremiere.getTaRapport().getRapport()!=null && selectedTaMatierePremiere.getTaRapport().getSens()!=null){
//					switch (selectedTaMatierePremiere.getTaRapport().getSens()) {
//					case 1:
//						selectedTaMatierePremiere.getDto().setQte2LDocument(((BigDecimal)value).multiply(selectedTaMatierePremiere.getTaRapport().getRapport()));
//						break;
//					case 0:
//						selectedTaMatierePremiere.getDto().setQte2LDocument(((BigDecimal)value).divide(selectedTaMatierePremiere.getTaRapport().getRapport()
//								,MathContext.DECIMAL128).setScale(selectedTaMatierePremiere.getTaRapport().getNbDecimal(),BigDecimal.ROUND_HALF_UP));
//						break;
//					default:
//						break;
//					} 
//				} else {
//					masterEntityLigneMP.setQte2LDocument(null);
//				}
				
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
//			TaLFabricationDTO dtoTemp =new TaLFabricationDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			taLFabricationService.validateDTOProperty(dtoTemp, nomChamp, ITaLFabricationServiceRemote.validationContext );
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
			Set<ConstraintViolation<TaLFabricationDTO>> violations = factory.getValidator().validateValue(TaLFabricationDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLFabricationDTO> cv : violations) {
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
//			TaLFabricationDTO dtoTemp =new TaLFabricationDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			taLFabricationService.validateDTOProperty(dtoTemp, nomChamp, ITaLFabricationServiceRemote.validationContext );
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
			Set<ConstraintViolation<TaLFabricationDTO>> violations = factory.getValidator().validateValue(TaLFabricationDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLFabricationDTO> cv : violations) {
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
//			TaFabricationDTO dtoTemp = new TaFabricationDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			fabricationService.validateDTOProperty(dtoTemp, nomChamp, ITaFabricationServiceRemote.validationContext );
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
			Set<ConstraintViolation<TaFabricationDTO>> violations = factory.getValidator().validateValue(TaFabricationDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaFabricationDTO> cv : violations) {
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
//			fabricationService.validateDTOProperty(selectedTaFabricationDTO,Const.C_CODE_TIERS,  ITaFabricationServiceRemote.validationContext );

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
//			taLFabricationService.validateDTOProperty(selectedTaProduit.getDto(),Const.C_CODE_ARTICLE,  ITaLFabricationServiceRemote.validationContext );
//			taLFabricationService.validateDTOProperty(selectedTaProduit.getDto(),Const.C_NUM_LOT,  ITaLFabricationServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
//	}
	
	public void handleReturnDialogParamModeleFab(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			qteModeleFabrication = (BigDecimal) ((Map) event.getObject()).get("qteModele");
			try {
				if(((Map) event.getObject()).get("idModele")!=null) {
					modelFab = modeleFabricationService.findById((Integer) ((Map) event.getObject()).get("idModele"));
				}
				if(((Map) event.getObject()).get("idRecette")!=null) {
					recetteFab = recetteService.findById((Integer) ((Map) event.getObject()).get("idRecette"));
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LgrTab b = new LgrTab();
			b.setTypeOnglet(LgrTab.TYPE_TAB_FABRICATION);
			b.setTitre("Fabrication");
			//b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
			b.setTemplate("solstyce/fabrication.xhtml");
			b.setIdTab(LgrTab.ID_TAB_FABRICATION);
			b.setStyle(LgrTab.CSS_CLASS_TAB_FABRICATION);
			b.setParamId("0");
			leftBean.setExpandedForce(false);

			tabsCenter.ajouterOnglet(b);
			tabViews.selectionneOngletCentre(b);
			
			FacesContext context = FacesContext.getCurrentInstance();  
			try {
				if(modelFab!=null) {
					actCreerAvecModele(null);
					if(ConstWeb.DEBUG) {
						context.addMessage(null, new FacesMessage("Création d'une fabrication à partir du modèle "+modelFab.getCodeDocument()+" (qte X "+qteModeleFabrication+").",
							"actCreerAvecModele"));
					}
				} else if(recetteFab !=null) {
					actCreerAvecRecette(null);
					if(ConstWeb.DEBUG) {
						context.addMessage(null, new FacesMessage("Création d'une fabrication à partir de la recette "+recetteFab.getIdRecette()+" (qte X "+qteModeleFabrication+").",
							"actCreerAvecRecette")); 
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//actInserer(actionEvent);
//			actInserer();
			
			
			
//			try {
//				listeConformiteController.setMasterEntity(taArticleService.findById( ((TaBareme)event.getObject()).getTaConformite().getTaArticle().getIdArticle()));
//				listeConformiteController.refresh();
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}
	}
	
	public void actCreerAvecModele(ActionEvent actionEvent) throws Exception{
		
		if(modelFab!=null) {
			actInserer();
			
			insertAutoEnabledMP = false;
			insertAutoEnabledPF = false;
			
			masterEntity.setLibelleDocument(modelFab.getLibelleDocument());
			masterEntity.setCommentaire(modelFab.getCommentaire());
			if(modelFab.getTaTFabrication()!=null) {
				masterEntity.setTaTFabrication(modelFab.getTaTFabrication());
			}
			
			selectedTaFabricationDTO.setLibelleDocument(modelFab.getLibelleDocument());
			selectedTaFabricationDTO.setCommentaire(modelFab.getCommentaire());
			if(modelFab.getTaTFabrication()!=null) {
				selectedTaFabricationDTO.setCodeTFabrication(modelFab.getTaTFabrication().getCodeTFabrication());
			}
			
			autoCompleteMapDTOtoUI();
			
			int i = 1;
			modelFab.findProduit();
			for (TaLModeleFabricationPF ligneModele : modelFab.getLignesProduits()) {
				actInsererProduit(null);
				selectedTaProduit.setTaArticle(ligneModele.getTaArticle());
				

				selectedTaProduit.getDto().setNumLot(selectedTaFabricationDTO.getCodeDocument()+"_"+selectedTaProduit.getDto().getNumLigneLDocument());					
				taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
				Map<String, String> params = new LinkedHashMap<String, String>();
				if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
				if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
				params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedTaFabricationDTO.getDateDebR()));
				params.put(Const.C_CODEDOCUMENT, selectedTaFabricationDTO.getCodeDocument());
				if(!ligneModele.getTaArticle().getGestionLot()){
					params.put(Const.C_VIRTUEL, "true");
					params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(ligneModele.getTaArticle().getIdArticle()));
				}
				selectedTaProduit.getDto().setNumLot(lotService.genereCode(params));
				
				selectedTaProduit.getDto().setU1LDocument(ligneModele.getU1LDocument());
				selectedTaProduit.getDto().setU2LDocument(ligneModele.getU2LDocument());
				selectedTaProduit.getDto().setLibLDocument(ligneModele.getLibLDocument());
				selectedTaProduit.getDto().setLibelleLot(ligneModele.getLibLDocument());
				if(selectedTaProduit.getTaArticle()!=null)
					selectedTaProduit.getDto().setDluo(LibDate.incrementDate(selectedTaFabricationDTO.getDateDocument(), 
							LibConversion.stringToInteger(selectedTaProduit.getTaArticle().getParamDluo(), 0) , 0, 0));
				
				if(ligneModele.getTaEntrepot()!=null) {
					selectedTaProduit.setTaEntrepot(ligneModele.getTaEntrepot());
				}
				selectedTaProduit.getDto().setEmplacement(ligneModele.getEmplacementLDocument());
				if(ligneModele.getQteLDocument()!=null) {
					selectedTaProduit.getDto().setQteLDocument(ligneModele.getQteLDocument().multiply(qteModeleFabrication));
				}
				if(ligneModele.getQte2LDocument()!=null) {
					selectedTaProduit.getDto().setQte2LDocument(ligneModele.getQte2LDocument().multiply(qteModeleFabrication));
				}
				actEnregistrerProduit();
			}
			modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
			for (TaLModeleFabricationMP ligneModele : modelFab.getLignesMatierePremieres()) {
				actInsererMatierePremiere(null);
				selectedTaMatierePremiere.setTaArticle(ligneModele.getTaArticle());
				selectedTaMatierePremiere.getDto().setU1LDocument(ligneModele.getU1LDocument());
				selectedTaMatierePremiere.getDto().setU2LDocument(ligneModele.getU2LDocument());
				selectedTaMatierePremiere.getDto().setLibLDocument(ligneModele.getLibLDocument());
				if(ligneModele.getQteLDocument()!=null) {
					selectedTaMatierePremiere.getDto().setQteLDocument(ligneModele.getQteLDocument().multiply(qteModeleFabrication));
				}
				if(ligneModele.getQte2LDocument()!=null) {
					selectedTaMatierePremiere.getDto().setQte2LDocument(ligneModele.getQte2LDocument().multiply(qteModeleFabrication));
				}
				actEnregistrerMatierePremiere();
			}
			modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
			valuesLigneMP = IHMmodelMP();
			valuesLignePF = IHMmodelPF();
		}
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Création d'une fabrication à partir d'un modèle (qte X "+qteModeleFabrication+").", "actCreerAvecModele"));
		}
		
//		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actCreerAvecRecette(ActionEvent actionEvent) throws Exception{
		if(recetteFab!=null) {
			actInserer();
			
			insertAutoEnabledMP = false;
			insertAutoEnabledPF = false;
			
			masterEntity.setLibelleDocument(recetteFab.getTaArticle().getLibellecArticle());
//			masterEntity.setCommentaire(modelFab.getCommentaire());
//			
			selectedTaFabricationDTO.setLibelleDocument(recetteFab.getTaArticle().getLibellecArticle());
//			selectedTaFabricationDTO.setCommentaire(modelFab.getCommentaire());
			
//			modelFab.findProduit();
//			for (TaLModeleFabrication ligneModele : modelFab.getListProduits()) {
				actInsererProduit(null);
				selectedTaProduit.setTaArticle(recetteFab.getTaArticle());
				
				selectedTaProduit.getDto().setNumLot(selectedTaFabricationDTO.getCodeDocument()+"_"+selectedTaProduit.getDto().getNumLigneLDocument());					
				taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
				Map<String, String> params = new LinkedHashMap<String, String>();
				if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
				if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
				params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedTaFabricationDTO.getDateDebR()));
				params.put(Const.C_CODEDOCUMENT, selectedTaFabricationDTO.getCodeDocument());
				if(!recetteFab.getTaArticle().getGestionLot()){
					params.put(Const.C_VIRTUEL, "true");
					params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(recetteFab.getTaArticle().getIdArticle()));
				}
				selectedTaProduit.getDto().setNumLot(lotService.genereCode(params));
				
				
				selectedTaProduit.getDto().setLibLDocument(recetteFab.getTaArticle().getLibellecArticle());
				selectedTaProduit.getDto().setLibelleLot(selectedTaProduit.getDto().getLibLDocument());
				if(recetteFab.getTaArticle()!=null)
					selectedTaProduit.getDto().setDluo(LibDate.incrementDate(selectedTaFabricationDTO.getDateDocument(), LibConversion.stringToInteger(recetteFab.getTaArticle().getParamDluo(), 0) , 0, 0));
//				selectedTaProduit.updateDTO();
				
//				selectedTaProduit.getDto().setU1LDocument(recetteFab.getTaArticle().gett);
//				if(rapport!=null){
//					selectedTaProduit.setTaRapport(rapport);
//					if(rapport.getTaUnite1()!=null) {
//						selectedTaProduit.getDto().setU1LDocument(rapport.getTaUnite1().getCodeUnite());
//						selectedTaProduit.setTaUnite1(taUniteService.findByCode(rapport.getTaUnite1().getCodeUnite()));
//					}
//					if(rapport.getTaUnite2()!=null) {
//						selectedTaProduit.getDto().setU2LDocument(rapport.getTaUnite2().getCodeUnite());
//						selectedTaProduit.setTaUnite2(taUniteService.findByCode(rapport.getTaUnite2().getCodeUnite()));
//					}
//				}else
				if(recetteFab.getTaArticle().getTaPrix()!=null){
					if(recetteFab.getTaArticle().getTaUnite1()!=null) {
						selectedTaProduit.getDto().setU1LDocument(recetteFab.getTaArticle().getTaUnite1().getCodeUnite());
						selectedTaProduit.setTaUnite1(taUniteService.findByCode(recetteFab.getTaArticle().getTaUnite1().getCodeUnite()));
					}
					for (TaRapportUnite obj : recetteFab.getTaArticle().getTaRapportUnites()) {
						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedTaProduit.getDto().getU1LDocument())){
							selectedTaProduit.setTaRapport(obj);
							selectedTaProduit.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());
							selectedTaProduit.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
						}							
					}
				}
//				selectedTaProduit.getDto().setU2LDocument(ligneModele.getU2LDocument());
				
//				if(ligneModele.getQteLDocument()!=null) {
					selectedTaProduit.getDto().setQteLDocument(qteModeleFabrication);
//				}
//				if(ligneModele.getQte2LDocument()!=null) {
//					selectedTaProduit.getDto().setQte2LDocument(ligneModele.getQte2LDocument().multiply(new BigDecimal(qteModeleFabrication)));
//				}
				actEnregistrerProduit();
//			}
			for (TaLRecette ligneRecette : recetteFab.getLignes()) {
				actInsererMatierePremiere(null);
				selectedTaMatierePremiere.setTaArticle(ligneRecette.getTaArticle());
				selectedTaMatierePremiere.getDto().setU1LDocument(ligneRecette.getU1LDocument());
				selectedTaMatierePremiere.getDto().setU2LDocument(ligneRecette.getU2LDocument());
				selectedTaMatierePremiere.getDto().setLibLDocument(ligneRecette.getLibLDocument());
				if(ligneRecette.getQteLDocument()!=null) {
					selectedTaMatierePremiere.getDto().setQteLDocument(ligneRecette.getQteLDocument().multiply(qteModeleFabrication));
				}
				if(ligneRecette.getQte2LDocument()!=null) {
					selectedTaMatierePremiere.getDto().setQte2LDocument(ligneRecette.getQte2LDocument().multiply(qteModeleFabrication));
				}
				actEnregistrerMatierePremiere();
			}
			modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
			valuesLigneMP = IHMmodelMP();
			valuesLignePF = IHMmodelPF();
		}
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Création d'une fabrication à partir de la recette (qte X "+qteModeleFabrication+").", "actCreerAvecRecette"));
		}
		
//		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public List<TaLFabricationDTOJSF> getValuesLigneMP() {
		return valuesLigneMP;
	}

	public void setValuesLigneMP(List<TaLFabricationDTOJSF> valuesLigneMP) {
		this.valuesLigneMP = valuesLigneMP;
	}

	public List<TaLFabricationDTOJSF> getValuesLignePF() {
		return valuesLignePF;
	}

	public void setValuesLignePF(List<TaLFabricationDTOJSF> valuesLignePF) {
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

	public TaModeleFabrication getModelFab() {
		return modelFab;
	}

	public void setModelFab(TaModeleFabrication modelFab) {
		this.modelFab = modelFab;
	}

	public BigDecimal getQteModeleFabrication() {
		return qteModeleFabrication;
	}

	public void setQteModeleFabrication(BigDecimal qteModele) {
		this.qteModeleFabrication = qteModele;
	}

	public TaRecette getRecetteFab() {
		return recetteFab;
	}

	public void setRecetteFab(TaRecette recetteFab) {
		this.recetteFab = recetteFab;
	}

	public TaTFabrication getTaTFabrication() {
		return taTFabrication;
	}

	public void setTaTFabrication(TaTFabrication taTFabrication) {
		this.taTFabrication = taTFabrication;
	}

	public TaTFabricationDTO getTaTFabricationDTO() {
		return taTFabricationDTO;
	}

	public void setTaTFabricationDTO(TaTFabricationDTO taTFabricationDTO) {
		this.taTFabricationDTO = taTFabricationDTO;
	}


	public void majNumLot(){
		Map<String, String> params = new LinkedHashMap<String, String>();
		if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
		if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
		params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedTaFabricationDTO.getDateDebR()));
		params.put(Const.C_CODEDOCUMENT, selectedTaFabricationDTO.getCodeDocument());
//		nouvelleLigne.getDto().setNumLot(lotService.genereCode(params));
		for (TaLFabricationDTOJSF ligne : valuesLignePF) {
			if(ligne.getTaLot()!=null && ligne.getTaLot().getNumLot()!=null) {
				lotService.annuleCode(ligne.getTaLot().getNumLot());
			}
		}		
		for (TaLFabricationDTOJSF ligne : valuesLignePF) {
			TaLFabricationPF obj=null;
			String numLot = null;
			numLot=lotService.genereCode(params);
			ligne.getDto().setNumLot(numLot);
			if(ligne.getTaLot()!=null){
				ligne.getTaLot().setNumLot(numLot);
			}
			obj=rechercheLignePFNumLigne(ligne.getDto().getNumLigneLDocument());
			if(obj!=null && obj.getTaLot()!=null)obj.getTaLot().setNumLot(numLot);
		}
		
	}

	public void majDLUO(){		
		for (TaLFabricationDTOJSF ligne : valuesLignePF) {
			if(ligne.getDto()!=null && ligne.getTaArticle()!=null)
				ligne.getDto().setDluo(LibDate.incrementDate(selectedTaFabricationDTO.getDateDebR(), LibConversion.stringToInteger(ligne.getTaArticle().getParamDluo(), 0) , 0, 0));
		}
		
	}
	
	public TaFabricationDTO[] getSelectedTaFabricationDTOs() {
		return selectedTaFabricationDTOs;
	}

	public void setSelectedTaFabricationDTOs(
			TaFabricationDTO[] selectedTaFabricationDTOs) {
		this.selectedTaFabricationDTOs = selectedTaFabricationDTOs;
	}

	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}

	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}

	public TaUtilisateur getRedacteur() {
		return redacteur;
	}

	public void setRedacteur(TaUtilisateur redacteur) {
		this.redacteur = redacteur;
	}

	public TaUtilisateur getControleur() {
		return controleur;
	}

	public void setControleur(TaUtilisateur controleur) {
		this.controleur = controleur;
	}

	public TaUtilisateurDTO getRedacteurDTO() {
		return redacteurDTO;
	}

	public void setRedacteurDTO(TaUtilisateurDTO redacteurDTO) {
		this.redacteurDTO = redacteurDTO;
	}

	public TaUtilisateurDTO getControleurDTO() {
		return controleurDTO;
	}

	public void setControleurDTO(TaUtilisateurDTO controleurDTO) {
		this.controleurDTO = controleurDTO;
	}

	
	public Integer renvoiPremierIdSelection(){
		if(selectedTaFabricationDTOs!=null && selectedTaFabricationDTOs.length>0){
			if(selectedTaFabricationDTOs.length>1){
				PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Sélection multiple","Vous ne devez sélectionner qu'une seule fabrication pour créer le modèle !"));
				
			}else
				return selectedTaFabricationDTOs[0].getId();
		}
		return null;
	}

	public List<ArticleLotEntrepotDTO> getDetailLigneOverLayPanel() {
		return detailLigneOverLayPanel;
	}

	public void setDetailLigneOverLayPanel(List<ArticleLotEntrepotDTO> detailLigneOverLayPanel) {
		this.detailLigneOverLayPanel = detailLigneOverLayPanel;
	}

	public void etatStockQte1(ActionEvent actionEvent) {
		List<ArticleLotEntrepotDTO> liste= lotAutoComplete("");
		if(liste!=null && !liste.isEmpty())	detailLigneOverLayPanel=liste;	
	}

	public List<TaLabelArticleDTO> getListeToutesCertification() {
		return listeToutesCertification;
	}

	public void setListeToutesCertification(List<TaLabelArticleDTO> listeToutesCertification) {
		this.listeToutesCertification = listeToutesCertification;
	}


	public void actDialogEvenement(ActionEvent actionEvent) {	    
		EvenementParam param = new EvenementParam();
		param.setListeTiers(new ArrayList<>());
//		param.getListeTiers().add(masterEntity.getTaTiers()); //il n'y a pas de tiers sur une fabrication
		param.setListeDocuments(new ArrayList<>());
		param.getListeDocuments().add(masterEntity);
		actDialogEvenement(param);			    
	}

	public void actDialogEmail(ActionEvent actionEvent) {		
		EmailParam email = new EmailParam();
		email.setEmailExpediteur(null);
		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" "+nomDocument+" "+masterEntity.getCodeDocument()); 
		email.setBodyPlain("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
		email.setBodyHtml("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
		email.setDestinataires(new String[]{masterEntity.getTaTiersPrestationService().getTaEmail().getAdresseEmail()});
		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiersPrestationService().getTaEmail()});
		
		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
		pj1.setFichier(new File((fabricationService).generePDF(masterEntity.getIdDocument(),null, null, null)));
		pj1.setTypeMime("pdf");
		pj1.setNomFichier(pj1.getFichier().getName());
		email.setPiecesJointes(
				new EmailPieceJointeParam[]{pj1}
				);
		actDialogEmail(email);
	}

	
	public TaCoefficientUnite recupCoefficientUnite(String code1, String code2) {
		TaCoefficientUnite coef=null;;
		coef=taCoefficientUniteService.findByCode(code1,code2);
		if(coef!=null)coef.recupRapportEtSens(code1,code2);
		return coef;
	}

	public boolean isModeScanCodeBarre() {
		return modeScanCodeBarre;
	}

	public void setModeScanCodeBarre(boolean modeScanCodeBarre) {
		this.modeScanCodeBarre = modeScanCodeBarre;
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

	public boolean isMajDlc() {
		return majDlc;
	}

	public void setMajDlc(boolean majDlc) {
		this.majDlc = majDlc;
	}


	public void actEssai(ActionEvent actionEvent) {
		try {
			TaFabrication fab=fabricationService.findByCode("FAB2100003");
			TaLFabricationPF pf=fab.getLignesProduits().get(2);
			fab.getListeLotSuppr().add(pf.getTaLot().getIdLot());
//			pf.setTaLot(lotService.findOrCreateLotVirtuelArticle(pf.getTaArticle().getIdArticle()));
			fab.getLignesProduits().remove(pf);
			fab=fabricationService.merge(fab);
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fabrication", e.getMessage()));
		}
	}
	
	public boolean lotEstUnique(TaLot lot) {
		if(lot!=null) {
			return !lot.getVirtuel() || lot.getVirtuelUnique();
		}
		return true;
	}

}  
