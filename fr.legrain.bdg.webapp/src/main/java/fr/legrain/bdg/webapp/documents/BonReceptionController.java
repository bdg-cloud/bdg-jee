package fr.legrain.bdg.webapp.documents;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
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
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaLabelArticleDTO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.dto.TaRefArticleFournisseurDTO;
import fr.legrain.article.dto.TaTReceptionDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.article.model.TaTReception;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaRefArticleFournisseurServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTReceptionServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBonReceptionServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.EmailPieceJointeParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaHistREtatLigneDocument;
import fr.legrain.document.model.TaInfosBonReception;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaREtat;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class BonReceptionController extends AbstractDocumentController<TaBonReception,TaBonReceptionDTO,TaLBonReception,TaLBonReceptionDTO,TaLBonReceptionDTOJSF,TaInfosBonReception> {  

//	@Inject @Named(value="tabListModelCenterBean")
//	private TabListModelBean tabsCenter;
//	
//	@Inject @Named(value="leftBean")
//	private LeftBean leftBean;
//	
//	@Inject @Named(value="tabViewsBean")
//	private TabViewsBean tabViews;
//	
//	@Inject @Named(value="etiquetteCodeBarreBean")
//	private EtiquetteCodeBarreBean etiquetteCodeBarreBean;
//
//	private String paramId;
//
//	private List<TaBonReceptionDTO> values; 
//	private List<TaLBonReceptionDTOJSF> valuesLigne;
//	
//	private RemoteCommand rc;
//
//	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
//	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();

	private @EJB ITaBonReceptionServiceRemote taBonReceptionService;
	private @EJB ITaLBonReceptionServiceRemote taLBonReceptionService;
//	private @EJB ITaTLigneServiceRemote taTLigneService;
//	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaTReceptionServiceRemote taTReceptionService;
//	private @EJB ITaArticleServiceRemote taArticleService;
//	private @EJB ITaEntrepotServiceRemote taEntrepotService;
//	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaConformiteServiceRemote taConformiteService;
	private @EJB ITaRefArticleFournisseurServiceRemote taRefArticleFournisseurService;
//	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
//	private @EJB ITaMouvementStockServiceRemote taMouvementStockService;
//	private @EJB ITaGrMouvStockServiceRemote taGrMouvStockService;
//	private @EJB ITaUniteServiceRemote taUniteService;
//	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
//
//	private TaBonReceptionDTO[] selectedDocumentDTOs; 
//	private TaBonReception masterEntity = new TaBonReception();
//	private TaBonReceptionDTO newDocumentDTO = new TaBonReceptionDTO();
//	private TaBonReceptionDTO selectedDocumentDTO = new TaBonReceptionDTO();
//
//	private TaLBonReceptionDTOJSF[] selectedLigneJSFs; 
//	private TaLBonReception masterEntityLigne = new TaLBonReception();
//	private TaLBonReceptionDTOJSF newTaLBonReceptionDTOJSF = new TaLBonReceptionDTOJSF();
//	private TaLBonReceptionDTOJSF selectedLigneJSF = new TaLBonReceptionDTOJSF();
////	private TaLBonReceptionDTO selectedTaLBonReceptionDTO = new TaLBonReceptionDTO();
//
//	private TaTiers taTiers;
//	private TaTiersDTO taTiersDTO;
	
	private List<TaLabelArticleDTO> listeToutesCertification;
//	
	private TaTiers taTiersPrestationService;
	private TaTiersDTO taTiersPrestationServiceDTO;
//	
	private TaTReception taTReception;
	private TaTReceptionDTO taTReceptionDTO;
	
	private TaLabelArticle taLabelArticle;
	private List<TaLabelArticle> taLabelArticles;
//
//	private LgrDozerMapper<TaBonReceptionDTO,TaBonReception> mapperUIToModel  = new LgrDozerMapper<TaBonReceptionDTO,TaBonReception>();
//	private LgrDozerMapper<TaBonReception,TaBonReceptionDTO> mapperModelToUI  = new LgrDozerMapper<TaBonReception,TaBonReceptionDTO>();
//	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUITiers  = new LgrDozerMapper<TaTiers,TaTiersDTO>();
	private LgrDozerMapper<TaTReception,TaTReceptionDTO> mapperModelToUITReception  = new LgrDozerMapper<TaTReception,TaTReceptionDTO>();
//	
//	private boolean insertAuto = true;
	TaPreferences prefLot=null;
	
	public BonReceptionController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		super.postConstruct();
		listePreferences= taPreferencesService.findByGroupe("bonreception");
		nomDocument="Bon de réception";
		setTaDocumentService(taBonReceptionService);
		setTaLDocumentService(taLBonReceptionService);
		setTaInfosDocumentService(taInfosDocumentService);
		prefLot = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_AUTORISER_UTILISATION_CODE_LOT_BON_RECEPTION);

//pas de wizard
//		stepEntete = "idEnteteDevis";
//		stepLignes = "idLignesDevis";
//		stepTotaux = "idTotauxFormDevis";
//		stepValidation = "idValidationFormDevis";
		idMenuBouton = "form:tabView:idMenuBoutonBonReception";
		wvEcran = LgrTab.WV_TAB_BON_RECEPTION;
		classeCssDataTableLigne = "myclass2";
		
		refresh();
		listeToutesCertification = taLabelArticleService.selectAllDTO();
	}

	public void refresh(){
		//values = taBonReceptionService.selectAllDTO();
		values = taBonReceptionService.findAllLight();
		valuesLigne = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaLBonReceptionDTOJSF> IHMmodel() {
		LinkedList<TaLBonReception> ldao = new LinkedList<TaLBonReception>();
		LinkedList<TaLBonReceptionDTOJSF> lswt = new LinkedList<TaLBonReceptionDTOJSF>();

		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLBonReception,TaLBonReceptionDTO> mapper = new LgrDozerMapper<TaLBonReception,TaLBonReceptionDTO>();
			LgrDozerMapper<TaLabelArticle,TaLabelArticleDTO> mapperCertification = new LgrDozerMapper<TaLabelArticle,TaLabelArticleDTO>();
			TaLBonReceptionDTO dto = null;
			for (TaLBonReception o : ldao) {
				TaLBonReceptionDTOJSF t = new TaLBonReceptionDTOJSF();
//				try {
//					TaLot lot = taLotService.findByCode(o.getNumLot());
//					o.setTaLot(lot);
				dto = (TaLBonReceptionDTO) mapper.map(o, TaLBonReceptionDTO.class);
				t.setDto(dto);
				t.setTaLot(o.getTaLot());
//				t.setTaLot(lot);
				if(o.getTaLot()!=null && o.getTaLot().getTaLabelArticles()!=null && !o.getTaLot().getTaLabelArticles().isEmpty()) {
					List<TaLabelArticleDTO> listeCertDTO = new ArrayList<>();
					for (TaLabelArticle labelArt : o.getTaLot().getTaLabelArticles()) {
						listeCertDTO.add(mapperCertification.map(labelArt, TaLabelArticleDTO.class));
					}
					t.setListeCertificationDTO(listeCertDTO);
				}
				
				t.setTaArticle(o.getTaArticle());
				if(o.getTaArticle()!=null) {
					t.setTaRapport(o.getTaArticle().getTaRapportUnite());
				}
				t.setTaEntrepot(o.getTaEntrepot());
				if(o.getU1LDocument()!=null) {
					t.setTaUnite1(new TaUnite());
					t.getTaUnite1().setCodeUnite(o.getU1LDocument());
				}
				if(o.getU2LDocument()!=null) {
					t.setTaUnite2(new TaUnite());
					t.getTaUnite2().setCodeUnite(o.getU2LDocument());
					t.setTaCoefficientUnite(recupCoefficientUnite(t.getDto().getU1LDocument(),t.getDto().getU2LDocument()));
				}
				
				if(o.getUSaisieLDocument()!=null) {
					t.setTaUniteSaisie(new TaUnite());
					t.getTaUniteSaisie().setCodeUnite(o.getUSaisieLDocument());
					t.setTaCoefficientUniteSaisie(recupCoefficientUnite(t.getDto().getUSaisieLDocument(),t.getDto().getU1LDocument()));
				}


				t.setTaREtatLigneDocument(o.getTaREtatLigneDocument());
				if(o.getTaREtatLigneDocument()!=null && o.getTaREtatLigneDocument().getTaEtat()!=null)
					t.setCodeEtat(o.getTaREtatLigneDocument().getTaEtat().getCodeEtat());
				
				t.updateDTO(false);
				if(o.getTaLot()!=null)
					t.getDto().setCodeStatusConformite(o.getTaLot().getTaStatusConformite()!=null ? o.getTaLot().getTaStatusConformite().getCodeStatusConformite():TaStatusConformite.C_TYPE_ACTION_VIDE);
				t.getDto().setIdDocument(masterEntity.getIdDocument());
				List<TaRDocumentDTO>  r=rechercheSiDocLie();
				List<TaLigneALigneDTO>  l=rechercheSiLigneDocLie(t);
				List<TaLigneALigneEcheanceDTO>  le=rechercheSiLigneEcheanceDocLie(t);
				if(le!=null) {
					for (TaLigneALigneEcheanceDTO ligne : le) {
							if(t.getLigneAbonnement()==null)t.setLigneAbonnement(new LinkedList<>());
							t.getLigneAbonnement().add(ligne);
						}
				}
				if(l!=null) {
					for (TaLigneALigneDTO ligne : l) {
						if(ligne.getIdLigneSrc().equals(ligne.getIdLDocumentSrc())) {
							if(t.getLigneLieeFils()==null)t.setLigneLieeFils(new LinkedList<>());
							t.getLigneLieeFils().add(ligne);
						}
						else { 
							if(t.getLigneLieeMere()==null)t.setLigneLieeMere(new LinkedList<>());
							t.getLigneLieeMere().add(ligne);
						}	
						if(ligne.getEtat()!=null) {
//							t.setCodeEtat(ligne.getEtat());
							t.setLibelleEtat(" - Liée au document "+ligne.getCodeDocumentDest());
						}
					}
				}else
					if(r!=null) {
						for (TaRDocumentDTO ligne : r) {
//							t.setCodeEtat("*****");
							t.setLibelleEtat("Liée au document "+ligne.getCodeDocumentDest());
						}
					}
				
				lswt.add(t);
			}

		}
		return lswt;
	}
	
	public void actChangeCertificationLot(AjaxBehaviorEvent evt) {
		System.out.println("BonReceptionController.actChangeCertificationLot()");
		try {
			if(((TaLBonReceptionDTOJSF)selectedLigneJSF).getListeCertificationDTO()!=null && !((TaLBonReceptionDTOJSF)selectedLigneJSF).getListeCertificationDTO().isEmpty()) {
				List<TaLabelArticle> l = new ArrayList<>();
				for (TaLabelArticleDTO e : ((TaLBonReceptionDTOJSF)selectedLigneJSF).getListeCertificationDTO()) {
					System.out.println("----" + e.getCodeLabelArticle());
					l.add(taLabelArticleService.findById(e.getId()));
				} 
				selectedLigneJSF.setListeCertification(l);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

//	public List<TaBonReceptionDTO> getValues(){  
//		return values;
//	}
//	
//	public void actAutoInsererLigne(ActionEvent actionEvent) /*throws Exception*/ {
//		boolean existeDeja=false;
//		if(insertAuto) {
//			for (TaLBonReceptionDTOJSF ligne : valuesLigne) {
//				if(ligne.ligneEstVide())existeDeja=true;
//			}
//			if(!existeDeja){
//			actInsererLigne(actionEvent);
//			
//			String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
//			PrimeFaces.current().executeScript(oncomplete);
//			}
//		} else {
////			throw new Exception();
//		}
//	}
	
//	public int calcNextNumLigneDoc() {
//		int num = -1;
//		List<Integer> l = new ArrayList<>();
//		for (TaLBonReceptionDTOJSF lf : valuesLigne) {
//			l.add(lf.getDto().getNumLigneLDocument());
//		}
//		if(!l.isEmpty()) {
//			num = Collections.max(l);
////			Collections.sort(l);
////			num = l.get(l.size() - 1);
//		} else {
//			return 1;
//		}
//		return num+1;
//	}
	
//    public TaEtat etatLigneInsertion() {
//    	TaEtat etat = rechercheEtatInitialDocument();
//		if(masterEntity.getTaREtat()!=null && masterEntity.getTaREtat().getTaEtat()!=null && 
//				masterEntity.getTaREtat().getTaEtat().getPosition().compareTo(taEtatService.documentBrouillon(null).getPosition())>0) {
//			etat=taEtatService.documentEncours(null);
//		}
//
//    	return etat;
//    }

	public void actInsererLigne(ActionEvent actionEvent) {
				
		//		if(selectedLigneJSF==null || !selectedLigneJSF.isAutoInsert()) {
		TaLBonReceptionDTOJSF nouvelleLigne = new TaLBonReceptionDTOJSF();
		selectedDocumentDTO.setCommentaire(rechercheCommentaireDefautDocument());
		nouvelleLigne.setDto(new TaLBonReceptionDTO());
		//nouvelleLigne.getDto().setNumLigneLDocument(calcNextNumLigneDoc());
		nouvelleLigne.getDto().setNumLigneLDocument(valuesLigne.size()+1);
//		nouvelleLigne.getDto().setNumLot(selectedDocumentDTO.getCodeDocument()+"_"+valuesLigne.size()+1);
//		nouvelleLigne.getDto().setNumLot(selectedDocumentDTO.getCodeDocument()+"_"+nouvelleLigne.getDto().getNumLigneLDocument());
//		
//		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
//		Map<String, String> params = new LinkedHashMap<String, String>();
//		if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
//		if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
//		params.put(Const.C_CODEDOCUMENT, selectedDocumentDTO.getCodeDocument());
//		nouvelleLigne.getDto().setNumLot(taLotService.genereCode(params));
		
		//			nouvelleLigne.setAutoInsert(true);

		masterEntityLigne = new TaLBonReception(true); 
		try {
			//H = HT => pas nécessaire, mais il ne faut pas de ligne de commentaire  pour passer trigger "ligne_vide"
			masterEntityLigne.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
		
			//List<TaLBonReceptionDTOJSF> modelListeLigneDevis = IHMmodel();

			masterEntity.insertLigne(masterEntityLigne,nouvelleLigne.getDto().getNumLigneLDocument());
//			masterEntity.addLigne(masterEntityLigne);
		

			selectedDocumentDTO.setNumLigneLDocument(masterEntityLigne.getNumLigneLDocument());
			nouvelleLigne.getDto().setNumLigneLDocument(masterEntityLigne.getNumLigneLDocument());
	//		selectedDocumentDTO.setNumLigneLDocument(nouvelleLigne.getDto().getNumLigneLDocument());
			masterEntityLigne.setTaDocument(masterEntity);
			
			//initialisation des certification du lot/ de la ligne avec celles du document/fab
			nouvelleLigne.setListeCertificationDTO(selectedDocumentDTO.getTaLabelArticleDTOs());
			if(nouvelleLigne.getListeCertificationDTO()!=null && !nouvelleLigne.getListeCertificationDTO().isEmpty()) {
				List<TaLabelArticle> l = new ArrayList<>();
				for (TaLabelArticleDTO e : selectedDocumentDTO.getTaLabelArticleDTOs()) {
					l.add(taLabelArticleService.findById(e.getId()));
				} 
				nouvelleLigne.setListeCertification(l);
			}
			
			TaEtat etat = taBonReceptionService.etatLigneInsertion(masterEntity);

			masterEntityLigne.addREtatLigneDoc(etat);
			
			nouvelleLigne.setTaREtatLigneDocument(masterEntityLigne.getTaREtatLigneDocument());
			if(etat!=null)nouvelleLigne.getDto().setCodeEtat(etat.getCodeEtat());
			
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		masterEntityLigne.initialiseLigne(true);

		valuesLigne.add(nouvelleLigne);
		//pour gérér l'annulation
		oldLigneJSF=nouvelleLigne.copy(nouvelleLigne);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
		//		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actInsererLigne")); 
		}
	}

	public void actEnregistrerLigne(ActionEvent actionEvent) {

		try {
			selectedLigneJSF.updateDTO(true);
	
			masterEntityLigne.setTaArticle(selectedLigneJSF.getTaArticle());			
			if(masterEntityLigne.getTaArticle()==null && masterEntityLigne.getTaMouvementStock()!=null) {
				masterEntityLigne.setTaMouvementStock(null);
			}
			if(masterEntityLigne.getTaArticle()!=null && selectedLigneJSF.getDto().getPrixULDocument()==null) {
				masterEntityLigne.setPrixULDocument(BigDecimal.ZERO);
				selectedLigneJSF.getDto().setPrixULDocument(BigDecimal.ZERO);
			}
			
			masterEntityLigne.setTaEntrepot(selectedLigneJSF.getTaEntrepot());
			creerLot();			
			masterEntityLigne.setEmplacementLDocument(selectedLigneJSF.getDto().getEmplacement());			

			LgrDozerMapper<TaLBonReceptionDTO,TaLBonReception> mapper = new LgrDozerMapper<TaLBonReceptionDTO,TaLBonReception>();
			mapper.map((TaLBonReceptionDTO) selectedLigneJSF.getDto(),masterEntityLigne);
			IHMmodel(selectedLigneJSF,masterEntityLigne);

			if(selectedLigneJSF.getListeCertification()!=null) {
				Set<TaLabelArticle> sc = new HashSet<>();
				sc.addAll(selectedLigneJSF.getListeCertification());
				masterEntityLigne.getTaLot().setTaLabelArticles(sc);
			}


			masterEntity.enregistreLigne(masterEntityLigne);
			
			if(modeEcranLigne.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				if(!masterEntity.getLignes().contains(masterEntityLigne)) {
					masterEntity.addLigne(masterEntityLigne);	
				}
			}
			
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
			//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
			//		actInsererLigne(null);
	
			//        FacesMessage msg = new FacesMessage("Ligne Edited", ((TaLBonReceptionDTOJSF) event.getObject()).getIdLDocument().toString());
			//        FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actEnregisterLigne")); 
		}
	}
	
	public void creerLot() {
		try {
			if(masterEntity.getGestionLot() && selectedLigneJSF.getTaArticle()!=null && selectedLigneJSF.getTaArticle().getGestionLot()) {
				if( selectedLigneJSF.getTaArticle()!=null) {
					if(selectedLigneJSF.getDto()!=null ) {
						TaLot l = new TaLot();
						if(selectedLigneJSF.getTaLot()!=null) {
							l=selectedLigneJSF.getTaLot();
						} else {
							selectedLigneJSF.setTaLot(l);
						}
						if(taTiersPrestationServiceDTO!=null) {
							l.setTaTiersPrestationService(taTiersPrestationService);
						}
						if(selectedLigneJSF.getDto().getDluo()!=null) {
							l.setDluo(selectedLigneJSF.getDto().getDluo());
						}
						l.setNumLot(selectedLigneJSF.getDto().getNumLot());
						l.setLibelle(selectedLigneJSF.getDto().getLibelleLot());
						l.setUnite1(selectedLigneJSF.getDto().getU1LDocument());
						l.setUnite2(selectedLigneJSF.getDto().getU2LDocument());
						l.setTaArticle(selectedLigneJSF.getTaArticle());
						l=taLotService.initListeResultatControle(taConformiteService.controleArticleDerniereVersion(l.getTaArticle().getIdArticle()), l);
						//					l =  l.initListeResultatControle(taConformiteService.controleArticleDerniereVersion(l.getTaArticle().getIdArticle()));
						if(l.getTaResultatConformites()==null || l.getTaResultatConformites().isEmpty() ) {
							//aucun controle définit sur cet article
							//le lot est valide par défaut
							l.setLotConforme(true);
							l.setTaStatusConformite(taStatusConformiteService.findByCode(TaStatusConformite.C_TYPE_ACTION_AUCUN_CONTROLE_DEFINIT));
						} else {
							//le lot est invalide par defaut, il faut faire les controles
							l.setTaStatusConformite(taStatusConformiteService.findByCode(TaStatusConformite.C_TYPE_ACTION_VIDE));
						}
						l.setDateLot(selectedDocumentDTO.getDateDocument());
						if(selectedLigneJSF.getTaRapport()!=null){
							l.setNbDecimal(selectedLigneJSF.getTaRapport().getNbDecimal());
							l.setSens(selectedLigneJSF.getTaRapport().getSens());
							l.setRapport(selectedLigneJSF.getTaRapport().getRapport());
						}
						masterEntityLigne.setTaLot(l);
						if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
							if(!masterEntity.getGestionLot()  || !selectedLigneJSF.getTaArticle().getGestionLot()){
								l.setVirtuel(true);
								l.setVirtuelUnique(true);
							}
						}




						selectedLigneJSF.setTaLot(l);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actAnnulerLigne(ActionEvent actionEvent) {
		if(selectedLigneJSF.getDto().getIdLDocument()!=null) {
			masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
			//pour gérér l'annulation
			selectedLigneJSF = oldLigneJSF.copy(oldLigneJSF);
			actEnregistrerLigne(null);		
		} else {
			if(selectedLigneJSF.getDto()!=null && selectedLigneJSF.getDto().getNumLot()!=null) {
				taLotService.annuleCode(selectedLigneJSF.getDto().getNumLot());
			}
			masterEntityLigne =rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
			try {
				masterEntity.removeLigne(masterEntityLigne);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		valuesLigne= IHMmodel();
		if(!valuesLigne.isEmpty())
			selectionLigne(valuesLigne.get(0));		
		setInsertAuto(false);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actAnnulerLigne")); 
		}
	}

//	public void actModifierLigne(ActionEvent actionEvent) {
//
//		modeEcranLigne.setMode(EnumModeObjet.C_MO_EDITION);
//		
////		if(selectedLigneJSF.getDto().getIdLDocument()!=null) {
////			masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
////		} else {
////			masterEntityLigne =rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
////		}
//
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Bon de réception", "actModifierLigne")); 
//		}
//	}

//	public TaLBonReception rechercheLigne(int id){
//		TaLBonReception obj=masterEntityLigne;
//		for (TaLBonReception ligne : masterEntity.getLignes()) {
//			if(ligne.getIdLDocument()==id)
//				obj=ligne;
//		}
//		return obj;
//	}
//	
//	public TaLBonReception rechercheLigneNumLigne(int numLigne){
//		TaLBonReception obj=masterEntityLigne;
//		for (TaLBonReception ligne : masterEntity.getLignes()) {
//			if(ligne.getNumLigneLDocument()==numLigne)
//				obj=ligne;
//		}
//		return obj;
//	}
	
//	public void actSupprimerLigne() {
//		actSupprimerLigne(null);
//	}
	
	public TaRefArticleFournisseurDTO trouveCodeBarre(String codeBarre,String codeFournisseur) {
		if(codeFournisseur!=null && codeBarre!=null && !codeBarre.equals("")) {
			return  taRefArticleFournisseurService.findByCodeFournisseurAndCodeBarreLight(codeBarre, codeFournisseur);
		}else return null;
	}
	
	public void actValiderCodeBarre(ActionEvent actionEvent) {
		try {
			if(taTiersDTO!=null && taTiersDTO.getCodeTiers()!=null && selectedLigneJSF.getDto().getCodeBarre()!=null && !selectedLigneJSF.getDto().getCodeBarre().equals("")) {
				//récupérer l'article qui correspond au codeBarre fournisseur (TaRefArticleFournisseur)
				TaRefArticleFournisseurDTO obj= trouveCodeBarre(selectedLigneJSF.getDto().getCodeBarre(), taTiersDTO.getCodeTiers());
				if(obj!=null) {
					selectedLigneJSF.getDto().setCodeArticle(obj.getCodeArticle());
					selectedLigneJSF.setTaArticleDTO(taArticleService.findByCodeDTO(obj.getCodeArticle()));
					validateUIField(Const.C_CODE_ARTICLE, selectedLigneJSF.getDto().getCodeArticle());
					selectedLigneJSF.getDto().setQteLDocument(BigDecimal.ONE);
					validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
				} else {
					actDialogRefArticleFournisseur(taTiersDTO.getCodeTiers(),selectedLigneJSF.getDto().getCodeBarre());
				}
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void actSupprimerLigne(ActionEvent actionEvent) {
		try {
//			valuesLigne.remove(selectedLigneJSF);
			if(autoriseSuppressionLigne()) {
				
				if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT)) {
					  if(selectedLigneJSF.getDto().getIdLDocument() != null) {
//							//List<TaLEcheance> liste  = taLEcheanceService.findAllByIdLFacture(selectedLigneJSF.getDto().getIdLDocument());
//							List<TaLigneALigneEcheance> liste  = taLigneALigneEcheanceService.findAllByIdLDocumentAndTypeDoc(selectedLigneJSF.getDto().getIdLDocument(), TaBonReception.TYPE_DOC);
//							if(liste != null && !liste.isEmpty()) {
//								for (TaLigneALigneEcheance li : liste) {
//									if(!listeLigneALigneEcheanceASupprimer.contains(li)) {
//										listeLigneALigneEcheanceASupprimer.add(li);
//									}
//									
//								}
//
//							}
					  }
					}
				
				
				
				if(selectedLigneJSF.getDto().getNumLot()!=null) {
					taLotService.annuleCode(selectedLigneJSF.getDto().getNumLot());
				}	

				valuesLigne.remove(selectedLigneJSF);

				if(selectedLigneJSF.getDto().getIdLDocument()!=null && selectedLigneJSF.getDto().getIdLDocument()!=0) {
					masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
				} else {
					masterEntityLigne =rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
				}
				
			
			masterEntityLigne.setTaLot(null);
			if(masterEntityLigne.getTaMouvementStock()!=null) {
				masterEntityLigne.getTaMouvementStock().setTaLot(null);
				masterEntityLigne.setTaMouvementStock(null);
			}
				
				masterEntityLigne.setTaDocumentGeneral(null);
				masterEntity.removeLigne(masterEntityLigne);
				
				

				masterEntity.calculeTvaEtTotaux();
				valuesLigne=IHMmodel();
				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				
				modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
				
//				super.actSupprimerLigne(actionEvent);
			}
////			for (TaLBonReception lbr : masterEntity.getLignes()) {
////				if(selectedLigneJSF.getDto().getIdLDocument()==lbr.getIdLDocument()) {
////					masterEntityLigne = lbr;
////				}
////			}
//			
////			taEtatStockService.supprimeEtatStockLot(masterEntityLigne.getTaLot().getIdLot());
//			if(selectedLigneJSF.getDto().getIdLDocument()!=null) {
//				masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//			} else {
//				masterEntityLigne =rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
//			}
////			masterEntityLigne = taLBonReceptionService.findById(masterEntityLigne.getIdLDocument());
////			
////			TaLot lotLigne = masterEntityLigne.getTaLot();
////			TaMouvementStock mouvLigne = masterEntityLigne.getTaMouvementStock();
////			
////			masterEntityLigne.setTaLot(null);
////			masterEntityLigne.setTaMouvementStock(null);
//////			
////			masterEntityLigne.setTaDocument(null);
////			masterEntityLigne = taLBonReceptionService.merge(masterEntityLigne,ITaLBonReceptionServiceRemote.validationContext);
//////			
////			taMouvementStockService.supprimer(mouvLigne);
////			taLotService.supprimer(lotLigne);
//			
////			taLBonReceptionService.supprimer(masterEntityLigne);
//			
//			masterEntityLigne.setTaDocument(null);
////			
//			masterEntity.removeLigne(masterEntityLigne);
//			
//			//s'assurer que la ligne de mouvement correspondant est bien supprimée du groupe de mouvement aussi
//			TaGrMouvStock grpMouvStock = new TaGrMouvStock();
//			if(masterEntity.getTaGrMouvStock()!=null) {
//				grpMouvStock=masterEntity.getTaGrMouvStock();
//				grpMouvStock.getTaMouvementStockes().remove(masterEntityLigne.getTaMouvementStock());
//			}
////			
//			//
////			
////			taLBonReceptionService.remove(masterEntityLigne);
//			
////			actEnregistrer(actionEvent);
//			
//			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
//	
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Bon de réception", "actSupprimerLigne")); 
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

//	public void onRowEdit(RowEditEvent event) {
//			try {
//				selectedLigneJSF.updateDTO(true);
//				if(selectedLigneJSF.getDto().getIdLDocument()!=null &&  selectedLigneJSF.getDto().getIdLDocument()!=0) {
//					masterEntityLigne = rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//				} else if(selectedLigneJSF.getDto().getNumLigneLDocument()!=null/* &&  selectedLigneJSF.getDto().getNumLigneLDocument()!=0*/) {
//					masterEntityLigne = rechercheLigneNumLigne(selectedLigneJSF.getDto().getNumLigneLDocument());
//				}
//				
//				validateLigneBonReceptionAvantEnregistrement(selectedLigneJSF);
//				actEnregistrerLigne(null);
//				setInsertAuto(modeEcran.getMode()==EnumModeObjet.C_MO_INSERTION);
//			} catch (Exception e) {
//				e.printStackTrace();
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bon de réception", e.getMessage()));	
//				context.validationFailed();
//				//RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
//				setInsertAuto(false);
////				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
//			}
//	}
	
//	public void onRowCancel(RowEditEvent event) {
//		//((TaLBonReceptionDTOJSF) event.getObject()).setAutoInsert(false);
////        FacesMessage msg = new FacesMessage("Edit Cancelled", ((Car) event.getObject()).getId());
////        FacesContext.getCurrentInstance().addMessage(null, msg);
//		
//		actAnnulerLigne(null);
//    }
//	
//	public void onRowEditInit(RowEditEvent event) {
//		AjaxBehaviorEvent evt = (AjaxBehaviorEvent)event;
//		DataTable table = (DataTable) evt.getSource();
//		int activeRow = table.getRowIndex()+1;
//		if(table.getRowCount() == activeRow) {
//			//derniere ligne
//			setInsertAuto(true);
//		} else {
//			setInsertAuto(false);
//		}
//		
//		//selectionLigne(selectedLigneJSF);
//	}
//	
//	public void onRowSelectLigne(SelectEvent event) {  
//		selectionLigne((TaLBonReceptionDTOJSF) event.getObject());		
//	}
//	
//	public void selectionLigne(TaLBonReceptionDTOJSF ligne){
//		selectedLigneJSF=ligne;
//		if(masterEntity.getIdDocument()!=0 && selectedLigneJSF.getDto().getIdLDocument()!=null)
//			masterEntityLigne=rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taBonReceptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
					for (TaLBonReceptionDTOJSF ligne : valuesLigne) {
						if(ligne.getDto().getNumLot()!=null)
							taLotService.annuleCode(ligne.getDto().getNumLot());
					}
				}
				masterEntity=new TaBonReception();
				selectedDocumentDTO=new TaBonReceptionDTO();
				
				if(values.size()>0)	selectedDocumentDTO = values.get(0);
//				else selectedDocumentDTO=new TaBonReceptionDTO();
				
				onRowSelect(null);
				valuesLigne = IHMmodel();
				initInfosDocument();
//				mapperModelToUI.map(masterEntity,selectedDocumentDTO );
				break;
			case C_MO_EDITION:
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					for (TaLBonReceptionDTOJSF ligne : valuesLigne) {
						if(ligne.getDto().getNumLot()!=null)
							taLotService.annuleCode(ligne.getDto().getNumLot());
					}
					masterEntity = taBonReceptionService.findByIDFetch(selectedDocumentDTO.getId());
					selectedDocumentDTO = taBonReceptionService.findByIdDTO(selectedDocumentDTO.getId());
					}				
				break;

			default:
				break;
			}			
				
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
		updateTab();
//		mapperModelToUI.map(masterEntity,selectedDocumentDTO );
//		valuesLigne= IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actAnnuler")); 
		}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void autoCompleteMapUIToDTO() {
//		if(taTiers!=null) {
//			validateUIField(Const.C_CODE_TIERS,taTiers);
//			selectedDocumentDTO.setCodeTiers(taTiers.getCodeTiers());
//		}
		if(taTiersDTO!=null) {
			validateUIField(Const.C_CODE_TIERS,taTiersDTO);
			selectedDocumentDTO.setCodeTiers(taTiersDTO.getCodeTiers());
		}
		if(taTiersPrestationServiceDTO!=null) {
			validateUIField(Const.C_CODE_TIERS_PRESTATION,taTiersPrestationServiceDTO);
			selectedDocumentDTO.setCodeTiersPrestation(taTiersPrestationServiceDTO.getCodeTiers());
		}
		if(taTReceptionDTO!=null) {
			validateUIField(Const.C_CODE_T_RECEPTION,taTReceptionDTO);
			selectedDocumentDTO.setCodeTReception(taTReceptionDTO.getCodeTReception());
		}
		//		if(taTCivilite!=null) {
		//			validateUIField(Const.C_CODE_T_CIVILITE,taTCivilite.getCodeTCivilite());
		//			selectedDocumentDTO.setCodeTCivilite(taTCivilite.getCodeTCivilite());
		//		}
		//		if(taTEntite!=null) {
		//			validateUIField(Const.C_CODE_T_ENTITE,taTEntite.getCodeTEntite());
		//			selectedDocumentDTO.setCodeTEntite(taTEntite.getCodeTEntite());
		//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTiers = null;
			taTiersDTO = null;
			
			taTiersPrestationService = null;
			taTiersPrestationServiceDTO = null;
			
			taTReception = null;
			taTReceptionDTO = null;
			//			taTCivilite = null;
			//			taTEntite = null;
//			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
//				taTiers = taTiersService.findByCode(selectedDocumentDTO.getCodeTiers());
//			}
			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
				taTiers = taTiersService.findByCode(selectedDocumentDTO.getCodeTiers());
				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
			}
			if(selectedDocumentDTO.getCodeTiersPrestation()!=null && !selectedDocumentDTO.getCodeTiersPrestation().equals("")) {
				taTiersPrestationService = taTiersService.findByCode(selectedDocumentDTO.getCodeTiersPrestation());
				taTiersPrestationServiceDTO = mapperModelToUITiers.map(taTiersPrestationService, TaTiersDTO.class);
			}
			if(selectedDocumentDTO.getCodeTReception()!=null && !selectedDocumentDTO.getCodeTReception().equals("")) {
				taTReception = taTReceptionService.findByCode(selectedDocumentDTO.getCodeTReception());
				taTReceptionDTO = mapperModelToUITReception.map(taTReception, TaTReceptionDTO.class);
			}
//			if(selectedEtatDocument!=null) {
//				validateUIField(Const.C_CODE_ETAT,selectedEtatDocument.getCodeEtat());
//				selectedDocumentDTO.setCodeEtat(selectedEtatDocument.getCodeEtat());
//				masterEntity.setTaEtat(selectedEtatDocument);
//			}
			if(selectedDocumentDTO.getTaLabelArticleDTOs()!=null) {
				if(taLabelArticles==null) {
					taLabelArticles = new ArrayList<>();
				}
				if(selectedDocumentDTO.getTaLabelArticleDTOs()==null) {
					selectedDocumentDTO.setTaLabelArticleDTOs(new ArrayList<>());
				}
				for (TaLabelArticleDTO f : selectedDocumentDTO.getTaLabelArticleDTOs()) {
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
	/**
	 * @author yann
	 * On surcharge la méthode actModifier pour éviter le vérouillage quand mis a dispo
	 */
	public void actModifier(ActionEvent actionEvent) {
		//if(verifSiEstModifiable(masterEntity)) {
			if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION))
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			docEnregistre=false;
			masterEntity.setLegrain(true);
			initialisePositionBoutonWizard();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", "actModifier")); 	 
			}
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			changementStepWizard(true);
		//}
	}
	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaBonReception taTiers = new TaBonReception();
		try {

			autoCompleteMapUIToDTO();
			validateBonReceptionAvantEnregistrement(selectedDocumentDTO);
			mapperUIToModel.map(selectedDocumentDTO, masterEntity);

			masterEntity.setLegrain(false);
			TaTLigne typeLigneCommentaire =  taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);
			List<TaLBonReception> listeLigneVide = new ArrayList<TaLBonReception>(); 
			for (TaLBonReception ligne : masterEntity.getLignes()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide() && ligne.getNumLigneLDocument().compareTo(masterEntity.getLignes().size())==0) {
					listeLigneVide.add(ligne);
				} else if(ligne.getTaArticle()==null) {
					ligne.setTaTLigne(typeLigneCommentaire);
				} else if(masterEntity.getGestionLot() && ligne.getTaArticle()!=null && ligne.getTaArticle().getGestionLot() && ligne.getTaLot()==null){
					throw new Exception("Le numéro du lot doit être rempli");
				} 
//				else {
//					if(!gestionLot || (ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot())) {
//						//utiliser un lot virtuel
//						if(ligne.getTaLot()==null ){
//							Map<String, String> params = new LinkedHashMap<String, String>();
//							if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
//							if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
//							params.put(Const.C_CODEDOCUMENT, selectedDocumentDTO.getCodeDocument());							
//							ligne.setTaLot(taLotService.findById(taLotService.createLotVirtuelArticleFabOrBR(ligne.getTaArticle().getIdArticle(),params).getIdLot()));
//						}else{
//							if(!ligne.getTaLot().getNumLot().startsWith(TaLotService.DEBUT_VIRTUEL_LOT)){
//								ligne.getTaLot().setNumLot(TaLotService.DEBUT_VIRTUEL_LOT+ligne.getTaLot().getNumLot());
//							}
//						}
//					}
////					if(ligne.getTaArticle()!=null && ligne.getTaLot()!=null){
////						ligne.getTaLot().setVirtuel(!ligne.getTaArticle().getGestionLot());
////						ligne.getTaLot().setVirtuelUnique(!ligne.getTaArticle().getGestionLot());
////						if(ligne.getTaArticle().getGestionLot()&&ligne.getTaLot().getNumLot().startsWith(TaLotService.DEBUT_VIRTUEL_LOT))
////							ligne.getTaLot().setNumLot(ligne.getTaLot().getNumLot().replace(TaLotService.DEBUT_VIRTUEL_LOT, ""));
////					}
//				}
			}

			//supression des lignes vides
			for (TaLBonReception taLBonReception : listeLigneVide) {
				masterEntity.getLignes().remove(taLBonReception);
			}
			
			//suppression des liaisons entre ligne doc et ligne d'échéance
			for (TaLigneALigneEcheance ligneALigne : listeLigneALigneEcheanceASupprimer) {
				taLigneALigneEcheanceService.remove(ligneALigne);
			}

			//			if(masterEntity.getTaGrMouvStock()!=null){
			//			for (TaMouvementStock mouv : masterEntity.getTaGrMouvStock().getTaMouvementStockes()) {
			//				if(mouv.getTaLot()!=null){
			//				taLotService.remove(mouv.getTaLot());
			//				mouv.setTaLot(null);	
			//				}
			//			}
			//			taGrMouvStockService.remove(masterEntity.getTaGrMouvStock());
			//			masterEntity.setTaGrMouvStock(null);
			//			}
			/*
			 * TODO Gérer les mouvements corrrespondant aux lignes 
			 */
			TaGrMouvStock grpMouvStock = new TaGrMouvStock();
			if(masterEntity.getTaGrMouvStock()!=null)grpMouvStock=masterEntity.getTaGrMouvStock();
			grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeDocument());
			grpMouvStock.setDateGrMouvStock(masterEntity.getDateDocument());
			if(masterEntity.getLibelleDocument()!=null) {
				grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument());
			} else {
				grpMouvStock.setLibelleGrMouvStock("Mouv. "+masterEntity.getCodeDocument());
			}
			grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("R"));
			masterEntity.setTaGrMouvStock(grpMouvStock);
			grpMouvStock.setTaBonReception(masterEntity);

			for (TaLBonReception ligne : masterEntity.getLignes()) {
				//				if(ligne.getTaMouvementStock()==null){
				TaMouvementStock m = new TaMouvementStock();
				if(ligne.getTaMouvementStock()!=null)m=ligne.getTaMouvementStock();
				m.setLibelleStock(ligne.getLibLDocument());
				m.setDateStock(masterEntity.getDateDocument());
				m.setEmplacement(ligne.getEmplacementLDocument());
				m.setTaEntrepot(ligne.getTaEntrepot());
				if(ligne.getTaLot()!=null){
					ligne.getTaLot().setDateLot(masterEntity.getDateDocument());
					//					m.setNumLot(ligne.getTaLot().getNumLot());
					m.setTaLot(ligne.getTaLot());
				}
				m.setQte1Stock(ligne.getQteLDocument());
				m.setQte2Stock(ligne.getQte2LDocument());
				m.setUn1Stock(ligne.getU1LDocument());
				m.setUn2Stock(ligne.getU2LDocument());
				m.setTaGrMouvStock(grpMouvStock);
				//				ligne.setTaLot(null);
				ligne.setTaMouvementStock(m);

				grpMouvStock.getTaMouvementStockes().add(m);
				//				}
			}

			//			if(masterEntity.getLignes().size()<grpMouvStock.getTaMouvementStockes().size()) {
			//				//une ligne de document à été supprimer
			//				for (TaMouvementStock ligneMouv : grpMouvStock.getTaMouvementStockes()) {
			//					ligneMouv.gett
			//				}
			//			}




			//			masterEntity = taBonReceptionService.merge(masterEntity,ITaBonReceptionServiceRemote.validationContext);
			//			
			//			masterEntity = taBonReceptionService.findById(masterEntity.getIdDocument()); //pour etre sur que les valeur initialisé par les triggers "qui_cree,..." soit bien affecté avant une éventuelle nouvelle modification du document

			masterEntity = taBonReceptionService.mergeAndFindById(masterEntity,ITaBonReceptionServiceRemote.validationContext);
			masterEntity = taBonReceptionService.findByIDFetch(masterEntity.getIdDocument());
			taBonReceptionService.annuleCode(selectedDocumentDTO.getCodeDocument());


			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			selectedDocumentDTOs=new TaBonReceptionDTO[]{selectedDocumentDTO};

			List<TaLBonReceptionDTOJSF> listeLigneVideDto = new ArrayList<TaLBonReceptionDTOJSF>(); 
			for (TaLBonReceptionDTOJSF ligne : valuesLigne) {
				if(ligne.ligneEstVide())listeLigneVideDto.add(ligne);
			}
			for (TaLBonReceptionDTOJSF ligne : listeLigneVideDto) {
				valuesLigne.remove(ligne);
			}

			// isabelle le 14/01/2016
			valuesLigne= IHMmodel();
			//
			//			for (TaLBonReceptionDTOJSF ligne : valuesLigne) {
			//				if(ligne.getDto().getNumLot()!=null) {
			//					taLotService.annuleCode(ligne.getDto().getNumLot());
			//				}
			//			}

			autoCompleteMapDTOtoUI();
			selectedEtatDocument=null;
			if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedDocumentDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Bon de réception", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bon de réception", e.getMessage()));
		}


		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actEnregistrer")); 
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
//			leftBean.setExpandedForce(false);
			setInsertAuto(true);
			listePreferences= taPreferencesService.findByGroupe("bonReception");
			selectedDocumentDTO = new TaBonReceptionDTO();
			masterEntity = new TaBonReception();
			masterEntity.setUtiliseUniteSaisie(afficheUniteSaisie);//récupéré à partir d'une préférence
			selectedDocumentDTO.setUtiliseUniteSaisie(afficheUniteSaisie);


			TaEtat etat = taBonReceptionService.rechercheEtatInitialDocument();
			masterEntity.addREtat(etat);
			
			valuesLigne = IHMmodel();
			selectedEtatDocument=null;
			autoCompleteMapDTOtoUI();
			if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();


			//			selectedDocumentDTO.setCodeDocument(taBonReceptionService.genereCode()); //ejb
			//			validateUIField(Const.C_CODE_DOCUMENT,selectedDocumentDTO.getCodeDocument());//permet de verrouiller le code genere

//			selectedDocumentDTO.setCodeDocument(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			Map<String, String> paramsCode = new LinkedHashMap<String, String>();
			paramsCode.put(Const.C_NOMFOURNISSEUR, selectedDocumentDTO.getNomTiers());
			paramsCode.put(Const.C_CODETYPE, selectedDocumentDTO.getCodeTReception());
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
			
			if(selectedDocumentDTO.getCodeDocument()!=null) {
				taBonReceptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			
			String newCode = taBonReceptionService.genereCode(paramsCode);
			if(newCode!=null && !newCode.equals("")){
				selectedDocumentDTO.setCodeDocument(newCode);
			}

			//			selectedDocumentDTO.setCodeCompta("aa"); //ejb
			//			validateUIField(Const.C_CODE_COMPTA,selectedDocumentDTO.getCodeCompta()); //ejb
			//			selectedDocumentDTO.setCompte("111"); //ejb
			//			validateUIField(Const.C_COMPTE,selectedDocumentDTO.getCompte()); //ejb

			//			TaTTiers taTTiers;
			//
			//			taTTiers = taTTiersService.findByCode(codeTiersDefaut);
			//
			//			if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
			//				selectedDocumentDTO.setCompte(taTTiers.getCompteTTiers());
			//				this.taTTiers = taTTiers;
			//			} else {
			//				//selectedDocumentDTO.setCompte(TiersPlugin.getDefault().getPreferenceStore().
			//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			//			}
			//			selectedDocumentDTO.setActifTiers(true);
			//
			//			selectedDocumentDTO.setCodeTTvaDoc("F");

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				selectedDocumentDTO.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
				masterEntity.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
			}
			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Bon de réception", "actInserer")); 
			}

		} catch (Exception e) {
			e.printStackTrace();
			//		} catch (FinderException e) {
			//			e.printStackTrace();
		}
	}

	public void actModifier() {
		actModifier(null);
	}

//	public void actModifier(ActionEvent actionEvent) {
//
//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//		setInsertAuto(false);
//
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Bon de réception", "actModifier")); 	  
//		}
//	}

	public void actAide(ActionEvent actionEvent) {

		//		PrimeFaces.current().dialog().openDynamic("aide");

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 320);
		Map<String,List<String>> params = null;
		PrimeFaces.current().dialog().openDynamic("aide", options, params);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actAide")); 	    
		}
	}

	//	public void actAideRetour(ActionEvent actionEvent) {
	//		
	//	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BON_RECEPTION);
		b.setTitre("Bon de réception");
		b.setTemplate("documents/bon_reception.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BON_RECEPTION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_RECEPTION);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", 
					"Nouveau"
					)); 
		}
	} 

//	public void supprimer(ActionEvent actionEvent) {
//		actSupprimer(actionEvent);
//	}
//	
//	public void detail() {
//		detail(null);
//	}
//	
//	public void detail(ActionEvent actionEvent) {
//		onRowSelect(null);
//	}
//	
//	public void actSupprimer() {
//		actSupprimer(null);
//	}

	public void actSupprimer(ActionEvent actionEvent) {
		TaBonReception obj = new TaBonReception();
		try {
			if(autorisationLiaisonComplete()) {
				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
					obj = taBonReceptionService.findByIDFetch(selectedDocumentDTO.getId());
				}

				taBonReceptionService.remove(obj);
				values.remove(selectedDocumentDTO);

				if(!values.isEmpty()) {
					selectedDocumentDTO = values.get(0);
					selectedDocumentDTOs=new TaBonReceptionDTO[]{selectedDocumentDTO};
					updateTab();
				}else {
					selectedDocumentDTO = new TaBonReceptionDTO();
					selectedDocumentDTOs=new TaBonReceptionDTO[]{};
				}



				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Bon de réception", "actSupprimer"));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bon de réception", e.getMessage()));
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
	
		selectedDocumentDTO=new TaBonReceptionDTO();
		selectedDocumentDTOs=new TaBonReceptionDTO[]{selectedDocumentDTO};
		updateTab();
		
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeBR').filter()");
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Bon de réception", "actImprimer")); 
		}
		try {


			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			
			masterEntity=taBonReceptionService.findAvecResultatConformites(selectedDocumentDTO.getId());
			mapEdition.put("document", masterEntity);
			
			mapEdition.put("lignes",masterEntity.getLignes());
			sessionMap.put("edition", mapEdition);
			
			//			session.setAttribute("tiers", taTiersService.findById(selectedDocumentDTO.getId()));
			System.out.println("BonReceptionController.actImprimer()");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}  
	
	public void actImprimerListeDesBonsDeReception(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesBonsDeReception", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

//	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
//		
//		etiquetteCodeBarreBean.videListe();
//		try {
//			for (TaLBonReception l : masterEntity.getLignes()) {
//				etiquetteCodeBarreBean.getListeArticle().add(l.getTaArticle());
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		
//		if(ConstWeb.DEBUG) {
//		   	FacesContext context = FacesContext.getCurrentInstance();  
//		    context.addMessage(null, new FacesMessage("Bon de réception", "actImprimerEtiquetteCB")); 
//		}
//	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BON_RECEPTION);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

//	public void onRowSimpleSelect(SelectEvent event) {
//
//		try {
//			if(pasDejaOuvert()==false){
//				onRowSelect(event);
//	
//				autoCompleteMapDTOtoUI();
//				valuesLigne = IHMmodel();
//	
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Bon de réception", 
//						"Chargement du BR N°"+selectedDocumentDTO.getCodeTiers()
//						)); 
//			} else {
//				tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_BON_RECEPTION);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	} 

//	public void updateTab(){
//		try {
//			
//			autoCompleteMapDTOtoUI();
//			
//			if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>0) {
//				selectedDocumentDTO = selectedDocumentDTOs[0];
//			}
//			if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0) {
//				masterEntity = taBonReceptionService.findById(selectedDocumentDTO.getId());
//			}
//			valuesLigne = IHMmodel();
//			
//			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
//	
//			if(ConstWeb.DEBUG) {
//				FacesContext context = FacesContext.getCurrentInstance();  
//				context.addMessage(null, new FacesMessage("Bon de réception", 
//						"Chargement du BR N°"+selectedDocumentDTO.getCodeTiers()
//						)); 
//			}
//		
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void updateTab(){

		try {	
			super.updateTab();
			autoriseSuppressionLigne(true);
			selectedEtatDocument=null;
			if(masterEntity!=null && masterEntity.getTaREtat()!=null)
				selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
//		leftBean.setExpandedForce(false);
		
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_BON_RECEPTION);
		b.setTitre("Bon de réception");
		b.setTemplate("documents/bon_reception.xhtml");
		b.setIdTab(LgrTab.ID_TAB_BON_RECEPTION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_RECEPTION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);

		updateTab();
		scrollToTop();
	} 

//	public boolean editable() {
//		return !modeEcran.dataSetEnModif();
//	}

//	public void actDialogTiers(ActionEvent actionEvent) {
//
//		//		PrimeFaces.current().dialog().openDynamic("aide");
//
//		Map<String,Object> options = new HashMap<String, Object>();
//		options.put("modal", true);
//		options.put("draggable", true);
//		options.put("resizable", false);
//		options.put("contentHeight", 640);
//		options.put("modal", true);
//
//		//Map<String,List<String>> params = null;
//		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
//
//		PrimeFaces.current().dialog().openDynamic("tiers/dialog_tiers", options, params);
//
//		//		FacesContext context = FacesContext.getCurrentInstance();  
//		//		context.addMessage(null, new FacesMessage("Bon de réception", "actAbout")); 	    
//	}
//
//	public void handleReturnDialogTiers(SelectEvent event) {
//		if(event!=null && event.getObject()!=null) {
//			taTiers = (TaTiers) event.getObject();
//		}
//	}

//	public void actDialogTypeCivilite(ActionEvent actionEvent) {
//
//		//		PrimeFaces.current().dialog().openDynamic("aide");
//
//		Map<String,Object> options = new HashMap<String, Object>();
//		options.put("modal", true);
//		options.put("draggable", true);
//		options.put("resizable", false);
//		options.put("contentHeight", 640);
//		options.put("modal", true);
//
//		//Map<String,List<String>> params = null;
//		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
//
//		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_civilite", options, params);
//
//		//		FacesContext context = FacesContext.getCurrentInstance();  
//		//		context.addMessage(null, new FacesMessage("Bon de réception", "actAbout")); 	    
//	}
//
//	public void handleReturnDialogTypeCivilite(SelectEvent event) {
//		//		if(event!=null && event.getObject()!=null) {
//		//			taTCivilite = (TaTCivilite) event.getObject();
//		//		}
//	}



//	public void actDialogTypeEntite(ActionEvent actionEvent) {
//
//		//		PrimeFaces.current().dialog().openDynamic("aide");
//
//		Map<String,Object> options = new HashMap<String, Object>();
//		options.put("modal", true);
//		options.put("draggable", true);
//		options.put("resizable", false);
//		options.put("contentHeight", 640);
//		options.put("modal", true);
//
//		//Map<String,List<String>> params = null;
//		Map<String,List<String>> params = new HashMap<String,List<String>>();
//		List<String> list = new ArrayList<String>();
//		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//		params.put("modeEcranDefaut", list);
//
//		PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_entite", options, params);
//
//		//		FacesContext context = FacesContext.getCurrentInstance();  
//		//		context.addMessage(null, new FacesMessage("Bon de réception", "actAbout")); 	    
//	}
//
//	public void handleReturnDialogTypeEntite(SelectEvent event) {
//		//		if(event!=null && event.getObject()!=null) {
//		//			taTEntite = (TaTEntite) event.getObject();
//		//		}
//	}

//	public boolean etatBouton(String bouton) {
//		boolean retour = true;
//		switch (modeEcran.getMode()) {
//		case C_MO_INSERTION:
//			switch (bouton) {
//				case "annuler":
//				case "enregistrer":
//				case "fermer":
//					retour = false;
//					break;
//				default:
//					break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			default:
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
//				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null  && selectedDocumentDTO.getId()!=0 ) retour = false;
//				break;				
//			default:
//				break;
//		}
//			break;
//		default:
//			break;
//		}
//		
//		return retour;
//
//	}

//	public boolean etatBoutonLigne(String bouton) {
//	boolean retour = true;
//	if(bouton.equals("inserer"))
//		retour = true;
//	switch (modeEcranLigne.getMode()) {
//	case C_MO_INSERTION:
//		switch (bouton) {
//		case "annuler":
//		case "enregistrer":
//		case "fermer":
//			retour = false;
//			break;
//		case "rowEditor":
//			retour = modeEcran.dataSetEnModif()?true:false;
//			break;
//		default:
//			break;
//		}
//		break;
//	case C_MO_EDITION:
//		switch (bouton) {
//		case "annuler":
//		case "enregistrer":
//		case "fermer":
//			retour = modeEcran.dataSetEnModif()?false:true;
//			break;
//		case "rowEditor":
//			retour = modeEcran.dataSetEnModif()?true:false;
//			break;
//		case "supprimer":
//			retour = true;
//			break;
//		default:
//			//retour = false;
//			break;
//		}
//		break;
//	case C_MO_CONSULTATION:
//		switch (bouton) {
//		case "supprimer":
//		case "modifier":
//		case "inserer":
//		case "imprimer":
//		case "fermer":
//			retour = modeEcran.dataSetEnModif()?false:true;
//			break;
//		case "rowEditor":
//			retour = modeEcran.dataSetEnModif()?true:false;
//			break;
//		default:
//			break;
//		}
//		break;
//	default:
//		break;
//	}
//
//	return retour;
//}
	
//	public boolean etatBoutonLigne(String bouton) {
//		boolean retour = true;
//		if(bouton.equals("inserer"))
//			retour = true;
//		if(modeEcran.dataSetEnModif()) {
//			retour = false;
//		}
//		switch (modeEcranLigne.getMode()) {
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
//			default:
//				break;
//			}
//			break;
//		case C_MO_EDITION:
//			switch (bouton) {
//			case "annuler":
//			case "enregistrer":
//			case "fermer":
//				retour = false;
//				break;
//			case "rowEditor":
//				retour = modeEcran.dataSetEnModif()?true:false;
//				break;
//			default:
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
//				retour = false;
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
//
//	}
	public void validateLignesBonReception(FacesContext context, UIComponent component, Object value) throws ValidatorException {

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
//			TaLBonReceptionDTO dtoTemp =new TaLBonReceptionDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			taLBonReceptionService.validateDTOProperty(dtoTemp, nomChamp, ITaLBonReceptionServiceRemote.validationContext );
//
//			//selectedDocumentDTO.setAdresse1Adresse("abcd");
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
			Set<ConstraintViolation<TaLBonReceptionDTO>> violations = factory.getValidator().validateValue(TaLBonReceptionDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLBonReceptionDTO> cv : violations) {
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
	
	public void validateBonReception(FacesContext context, UIComponent component, Object value) throws ValidatorException {

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
//			TaBonReceptionDTO dtoTemp = new TaBonReceptionDTO();
//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
//			taBonReceptionService.validateDTOProperty(dtoTemp, nomChamp, ITaBonReceptionServiceRemote.validationContext );
//
//			//selectedDocumentDTO.setAdresse1Adresse("abcd");
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
			Set<ConstraintViolation<TaBonReceptionDTO>> violations = factory.getValidator().validateValue(TaBonReceptionDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaBonReceptionDTO> cv : violations) {
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

	public void validateBonReceptionAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
//			taBonReceptionService.validateDTOProperty(selectedDocumentDTO,Const.C_CODE_TIERS,  ITaBonReceptionServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateLigneBonReceptionAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
//			taLBonReceptionService.validateDTOProperty(selectedLigneJSF.getDto(),Const.C_CODE_ARTICLE,  ITaLBonReceptionServiceRemote.validationContext );
//			taLBonReceptionService.validateDTOProperty(selectedLigneJSF.getDto(),Const.C_NUM_LOT,  ITaLBonReceptionServiceRemote.validationContext );
			if(afficheUniteSaisie && selectedLigneJSF.getTaCoefficientUniteSaisie()!=null)
				validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
//	public void autcompleteSelection(SelectEvent event) {
//		Object value = event.getObject();
//		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
//
//		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
//		System.out.println("BonReceptionController.autcompleteSelection() : "+nomChamp);
//
//		
//		if(value!=null) {
//			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaTReceptionDTO && ((TaTReceptionDTO) value).getCodeTReception()!=null && ((TaTReceptionDTO) value).getCodeTReception().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaEntrepotDTO && ((TaEntrepotDTO) value).getCodeEntrepot()!=null && ((TaEntrepotDTO) value).getCodeEntrepot().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
//		}
//		validateUIField(nomChamp,value);
//	}
	
	public void actionChangeEmplacement(){
		selectedLigneJSF.getDto().getEmplacement();
		masterEntityLigne.setEmplacementLDocument(selectedLigneJSF.getDto().getEmplacement());
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {
			if(nomChamp.equals(Const.C_CODE_TIERS_PRESTATION)) {
				TaTiers entity = null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
//						entity=(TaTiers) value;
						entity = taTiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taTiersService.findByCode((String)value);
					}
					
				}
				masterEntity.setTaTiersPrestationService(entity);

			} else if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity = null;
				
				if(value!=null){
					if(value instanceof TaTiersDTO){
//						entity=(TaTiers) value;
						entity = taTiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taTiersService.findByCode((String)value);
					}
					
					changement=masterEntity.getTaTiers()==null || !entity.equalsCode(masterEntity.getTaTiers());
					if(changement){
						String nomTiers=entity.getNomTiers();
						((TaBonReceptionDTO)selectedDocumentDTO).setLibelleDocument("Bon de réception N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);																			
					}					
				}
				
				
				masterEntity.setTaTiers(entity);			
				
				if(changement){
					Map<String, String> params = new LinkedHashMap<String, String>();
					if(masterEntity!=null && masterEntity.getTaTiers()!=null)
						params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
					if(masterEntity!=null && masterEntity.getTaTReception()!=null)
						params.put(Const.C_CODETYPE, masterEntity.getTaTReception().getCodeTReception());

					if(selectedDocumentDTO.getCodeDocument()!=null) {
						taBonReceptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
					}
					String newCode=taBonReceptionService.genereCode(params);
					if(!newCode.equals(""))selectedDocumentDTO.setCodeDocument(newCode);
				}
			}else if(nomChamp.equals(Const.C_CODE_T_RECEPTION)) {
					TaTReception entity = null;
					if(value!=null){
						if(value instanceof TaTReceptionDTO){
//							entity=(TaTiers) value;
							entity = taTReceptionService.findByCode(((TaTReceptionDTO)value).getCodeTReception());
						}else{
							entity = taTReceptionService.findByCode((String)value);
						}
						changement=!entity.equalsCode(masterEntity.getTaTReception());
						masterEntity.setTaTReception(entity);
					}else {
						changement=masterEntity.getTaTReception()!=null;
						masterEntity.setTaTReception(null);
						selectedDocumentDTO.setCodeTReception("");
						selectedDocumentDTO.setLiblTReception("");
						taTReceptionDTO=null;
						taTReception=null;
						
					}
					
					
					if(changement){
						Map<String, String> params = new LinkedHashMap<String, String>();
						if(masterEntity!=null && masterEntity.getTaTiers()!=null && masterEntity.getTaTiers().getNomTiers()!=null)
							params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
						if(masterEntity!=null && masterEntity.getTaTReception()!=null)
							params.put(Const.C_CODETYPE, masterEntity.getTaTReception().getCodeTReception());					
						if(selectedDocumentDTO.getCodeDocument()!=null) {
							taBonReceptionService.annuleCode(selectedDocumentDTO.getCodeDocument());
						}				
						String newCode=taBonReceptionService.genereCode(params);
						if(!newCode.equals(""))selectedDocumentDTO.setCodeDocument(newCode);
					}
					
			}else if(nomChamp.equals(Const.C_NUM_LOT)) {
				
			}else if(nomChamp.equals(Const.C_LIBELLE_DOCUMENT)) {
//				if(value==null || value.equals("")){
//					throw new Exception("ddddddddddd");
//				}
			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {
				
				if(selectedLigneJSF.getTaLot()!=null) {
					selectedLigneJSF.getTaLot().setDateLot(selectedDocumentDTO.getDateDocument());
				}
			}
			else if(nomChamp.equals(Const.C_CODE_LABEL_ARTICLE)) {
				TaLabelArticle entity =null;
				if(value!=null){
					if(value instanceof TaLabelArticleDTO){
						entity = taLabelArticleService.findByCode(((TaLabelArticleDTO)value).getCodeLabelArticle());
						if(masterEntity.getTaLabelArticles()==null) {
							masterEntity.setTaLabelArticles(new ArrayList<>());
						}
						masterEntity.getTaLabelArticles().add(entity);
						//selectedTaFabricationDTO.getTaLabelArticleDTOs().add((TaLabelArticleDTO)value);
					}else{
						entity = taLabelArticleService.findByCode((String)value);
					}
				}
			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaPrix prix =null;
				TaArticle entity = null;
				boolean changementArticleLigne = false;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCode(((TaArticleDTO) value).getCodeArticle());
					}else{
						entity = taArticleService.findByCode((String)value);
					}
				}
				if(entity!=null) {
					 prix = entity.chercheTarif(masterEntity.getTaTiers());
					 if(prix==null)prix=new TaPrix(BigDecimal.ZERO,BigDecimal.ZERO);
				}
				if(selectedLigneJSF.getTaArticle()== null || selectedLigneJSF.getTaArticle().getIdArticle()!=entity.getIdArticle()) {
					changementArticleLigne = true;
				}
				selectedLigneJSF.setTaArticle(entity);
				//masterEntityLigne.setTaArticle(entity);
				if(changementArticleLigne) {
					selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
					selectedLigneJSF.getDto().setDluo(LibDate.incrementDate(selectedDocumentDTO.getDateDocument(), LibConversion.stringToInteger(entity.getParamDluo(), 0) , 0, 0));
					selectedLigneJSF.getDto().setLibelleLot(entity.getLibellecArticle());
					
					selectedLigneJSF.setTaUniteSaisie(null);
					selectedLigneJSF.getDto().setUSaisieLDocument(null);
					selectedLigneJSF.getDto().setQteSaisieLDocument(BigDecimal.ZERO);
					selectedLigneJSF.setTaCoefficientUniteSaisie(null);

					
					if(selectedLigneJSF.getDto().getNumLot()!=null)taLotService.annuleCode(selectedLigneJSF.getDto().getNumLot());
					
					selectedLigneJSF.getDto().setNumLot(selectedDocumentDTO.getCodeDocument()+"_"+selectedLigneJSF.getDto().getNumLigneLDocument());					
					taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
					Map<String, String> params = new LinkedHashMap<String, String>();
					if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_NOMFOURNISSEUR, masterEntity.getTaTiers().getNomTiers());
					if(masterEntity!=null && masterEntity.getTaTiers()!=null)params.put(Const.C_CODEFOURNISSEUR, masterEntity.getTaTiers().getCodeTiers());
					params.put(Const.C_CODEDOCUMENT, selectedDocumentDTO.getCodeDocument());
					params.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
					if(!entity.getGestionLot()){
						params.put(Const.C_VIRTUEL, "true");
						params.put(Const.C_IDARTICLEVIRTUEL, LibConversion.integerToString(entity.getIdArticle()));
					}
					
					// modification suite à suppression de la cascade remove dans TalBonReception.taLot
					if(masterEntity.getGestionLot() && selectedLigneJSF.getTaArticle()!=null && selectedLigneJSF.getTaArticle().getGestionLot()) {
						//si gestion des lots général et article aussi
						if(prefLot!=null && LibConversion.StringToBoolean(prefLot.getValeur()) != null && LibConversion.StringToBoolean(prefLot.getValeur())) 				
							selectedLigneJSF.getDto().setNumLot(taLBonReceptionService.genereCode(params));
						else						
							selectedLigneJSF.getDto().setNumLot(taLotService.genereCode(params));	
					}else {
						//sinon on récupère le lot virtuel correspondant sans en créer un nouveau
						TaLot l =taLotService.findOrCreateLotVirtuelArticle(entity.getIdArticle());
						if(l!=null)selectedLigneJSF.getDto().setNumLot(l.getNumLot());
						selectedLigneJSF.setTaLot(l);
						masterEntityLigne.setTaLot(l);
					}

				}
				selectedLigneJSF.setTaUnite1(null);
				selectedLigneJSF.setTaUnite2(null);
				selectedLigneJSF.getDto().setU2LDocument(null);
				selectedLigneJSF.getDto().setQte2LDocument(BigDecimal.ZERO);
				TaRapportUnite rapport=entity.getTaRapportUnite();
				TaCoefficientUnite coef = null;
				if(rapport!=null){
					if(rapport.getTaUnite1()!=null && rapport.getTaUnite2()!=null) {
						coef=recupCoefficientUnite(rapport.getTaUnite1().getCodeUnite(),rapport.getTaUnite2().getCodeUnite());
						selectedLigneJSF.setTaCoefficientUnite(coef);
					}
				}
				
				if(entity!=null && entity.getTaUniteSaisie()!=null && selectedDocumentDTO.getUtiliseUniteSaisie()) {
					selectedLigneJSF.getDto().setUSaisieLDocument(entity.getTaUniteSaisie().getCodeUnite());
					selectedLigneJSF.setTaUniteSaisie(taUniteService.findByCode(entity.getTaUniteSaisie().getCodeUnite()));
					selectedLigneJSF.getDto().setQteSaisieLDocument(BigDecimal.ONE);
				}

				
				if(coef!=null){
					selectedLigneJSF.setTaCoefficientUnite(coef);
					if(entity.getTaUnite1()!=null) {
						selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
					}
					if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
						selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
					}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(rapport.getTaUnite2().getCodeUnite())){
						selectedLigneJSF.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
						selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
					}
				}else
					if(entity!=null ){
						if(entity.getTaUnite1()!=null) {
							selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
							selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
						}
						for (TaRapportUnite obj : entity.getTaRapportUnites()) {
							if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedLigneJSF.getDto().getU1LDocument())){
								if(obj!=null){
									if(obj.getTaUnite2()!=null) {										
										coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
									}
								}
								selectedLigneJSF.setTaCoefficientUnite(coef);
								if(coef!=null) {
									if(coef.getUniteB()!=null && coef.getUniteB().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
									selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
									selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
									}else if(coef.getUniteA()!=null && coef.getUniteA().getCodeUnite().equals(obj.getTaUnite2().getCodeUnite())){
										selectedLigneJSF.getDto().setU2LDocument(coef.getUniteA().getCodeUnite());
										selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteA().getCodeUnite()));
									}
								}else if(obj.getTaUnite2()!=null){
									selectedLigneJSF.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());
									selectedLigneJSF.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
								}
							}							
						}
					}
				if(prix!=null){
					selectedLigneJSF.getDto().setQteLDocument(new BigDecimal(1));
					validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
					if(masterEntity.getTtc()==1) {
						selectedLigneJSF.getDto().setPrixULDocument(prix.getPrixttcPrix());
					} else {
						selectedLigneJSF.getDto().setPrixULDocument(prix.getPrixPrix());
					}

					selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
					selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				}				
//				if(entity.getTaPrix()!=null){
//					if(entity.getTaUnite1()!=null) {
//						selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
//						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
//					}
//					for (TaRapportUnite obj : entity.getTaRapportUnites()) {
//						if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedLigneJSF.getDto().getU1LDocument())){
//							if(obj!=null){
//								if(obj.getTaUnite2()!=null) {
//									coef=recupCoefficientUnite(obj.getTaUnite1().getCodeUnite(),obj.getTaUnite2().getCodeUnite());
//								}
//							}
//							selectedLigneJSF.setTaCoefficientUnite(coef);
//							if(coef!=null) {
//								selectedLigneJSF.getDto().setU2LDocument(coef.getUniteB().getCodeUnite());
//								selectedLigneJSF.setTaUnite2(taUniteService.findByCode(coef.getUniteB().getCodeUnite()));
//							}
//						}							
//					}
//				}
				masterEntityLigne.setTauxTvaLDocument(selectedLigneJSF.getDto().getTauxTvaLDocument());
				masterEntityLigne.setPrixULDocument(selectedLigneJSF.getDto().getPrixULDocument());
				masterEntityLigne.calculMontant();
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				if(entity!=null && entity.getTaUniteSaisie()!=null && selectedDocumentDTO.getUtiliseUniteSaisie()) {
					validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());	
				}

				
			}else if(nomChamp.equals(Const.C_CODE_ENTREPOT)) {
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
					masterEntityLigne.setTaEntrepot(entity);
					selectedLigneJSF.getDto().setCodeEntrepot("");
				}
				
		
			}else if(nomChamp.equals(Const.C_EMPLACEMENT)) {
				if(value!=null){
					masterEntityLigne.setEmplacementLDocument((String) value);
				}else{
					masterEntityLigne.setEmplacementLDocument(null);
					selectedLigneJSF.getDto().setEmplacement("");
				}
				
		
			
		   } else if(nomChamp.equals(Const.C_QTE_SAISIE_L_DOCUMENT)) {
				BigDecimal qte=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						qte=(BigDecimal)value;
					}
					selectedLigneJSF.setTaCoefficientUniteSaisie(recupCoefficientUnite(selectedLigneJSF.getDto().getUSaisieLDocument(),selectedLigneJSF.getDto().getU1LDocument()));
					if(selectedLigneJSF.getTaCoefficientUniteSaisie()!=null) {
						if(selectedLigneJSF.getTaCoefficientUniteSaisie().getOperateurDivise()) 
							selectedLigneJSF.getDto().setQteLDocument((qte).divide(selectedLigneJSF.getTaCoefficientUniteSaisie().getCoeff()
									,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaCoefficientUniteSaisie().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
						else selectedLigneJSF.getDto().setQteLDocument((qte).multiply(selectedLigneJSF.getTaCoefficientUniteSaisie().getCoeff()));
					}else  {
						selectedLigneJSF.getDto().setQteLDocument(BigDecimal.ZERO);
						masterEntityLigne.setQteLDocument(null);
					}
				} else {
					masterEntityLigne.setQteLDocument(null);
				}
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
//				masterEntityLigne.setQteLDocument(qte);
//				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
//				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
			
		} else if(nomChamp.equals(Const.C_U_SAISIE_L_DOCUMENT)) {
			TaUnite entity =null;
			if(value!=null){
				if(value instanceof TaUnite){
					entity=(TaUnite) value;
					entity = taUniteService.findByCode(entity.getCodeUnite());
				}else{
					entity = taUniteService.findByCode((String)value);
				}
				masterEntityLigne.setUSaisieLDocument(entity.getCodeUnite());
				selectedLigneJSF.getDto().setUSaisieLDocument(entity.getCodeUnite());
				selectedLigneJSF.getDto().setQteSaisieLDocument(BigDecimal.ONE);	
			}else {
				selectedLigneJSF.getDto().setUSaisieLDocument("");
				masterEntityLigne.setUSaisieLDocument(null);
				selectedLigneJSF.getDto().setQteSaisieLDocument(null);	
			}
			selectedLigneJSF.setTaCoefficientUniteSaisie(recupCoefficientUnite(selectedLigneJSF.getDto().getUSaisieLDocument(),selectedLigneJSF.getDto().getU1LDocument()));
			validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());	
			validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
			validateUIField(Const.C_QTE2_L_DOCUMENT, selectedLigneJSF.getDto().getQte2LDocument());
		}else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
					BigDecimal qte=BigDecimal.ZERO;
					if(value!=null){
						if(!value.equals("")){
							qte=(BigDecimal)value;
						}
						selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
						if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
							if(selectedLigneJSF.getTaCoefficientUnite().getOperateurDivise()) 
								selectedLigneJSF.getDto().setQte2LDocument((qte).divide(selectedLigneJSF.getTaCoefficientUnite().getCoeff()
										,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP));
							else selectedLigneJSF.getDto().setQte2LDocument((qte).multiply(selectedLigneJSF.getTaCoefficientUnite().getCoeff()));
						}					
//					switch (selectedLigneJSF.getTaCoefficientUnite().getSens()) {
//					case 1:
//						selectedLigneJSF.getDto().setQte2LDocument(((BigDecimal)value).multiply(selectedLigneJSF.getTaCoefficientUnite().getRapport()));
//						break;
//					case 0:
//						selectedLigneJSF.getDto().setQte2LDocument(((BigDecimal)value).divide(selectedLigneJSF.getTaCoefficientUnite().getRapport()
//								,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaRapport().getNbDecimal(),BigDecimal.ROUND_HALF_UP));
//						break;
//					default:
//						break;
//					} 
				} else {
					masterEntityLigne.setQte2LDocument(null);
				}
					masterEntityLigne.setQteLDocument(qte);
			} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite) value;
						entity = taUniteService.findByCode(entity.getCodeUnite());
					}else{
						entity = taUniteService.findByCode((String)value);
					}
					masterEntityLigne.setU1LDocument(entity.getCodeUnite());
					selectedLigneJSF.getDto().setU1LDocument(entity.getCodeUnite());
				}else {
					selectedLigneJSF.getDto().setU1LDocument("");
					masterEntityLigne.setU1LDocument(null);
				}
				selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
			} else if(nomChamp.equals(Const.C_QTE2_L_DOCUMENT)) {
				BigDecimal qte=BigDecimal.ZERO;
				if(value==null) {
					masterEntityLigne.setQte2LDocument(null);
				}else if(!value.equals("")){
					qte=(BigDecimal)value;
				}
				selectedLigneJSF.getDto().setQte2LDocument(qte);
//				if(value==null) {
//					masterEntityLigne.setQte2LDocument(null);
//				}
			} else if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {
				initLocalisationTVA();
			}else if(nomChamp.equals(Const.C_U2_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite) value;
						entity = taUniteService.findByCode(entity.getCodeUnite());
					}else{
						entity = taUniteService.findByCode((String)value);
					}
					masterEntityLigne.setU2LDocument(entity.getCodeUnite());
					selectedLigneJSF.getDto().setU2LDocument(entity.getCodeUnite());
				}else {
					selectedLigneJSF.getDto().setU2LDocument("");
					masterEntityLigne.setU2LDocument(null);
				}
				selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());		
			} 			
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

//	private boolean adresseEstRempli() {
//		boolean retour=false;
//		//		if(!selectedDocumentDTO.getAdresse1Adresse().equals(""))return true;
//		//		if(!selectedDocumentDTO.getAdresse2Adresse().equals(""))return true;
//		//		if(!selectedDocumentDTO.getAdresse3Adresse().equals(""))return true;
//		//		if(!selectedDocumentDTO.getCodepostalAdresse().equals(""))return true;
//		//		if(!selectedDocumentDTO.getVilleAdresse().equals(""))return true;
//		//		if(!selectedDocumentDTO.getPaysAdresse().equals(""))return true;
//		return retour;
//	}
//
//	public List<TaTiers> tiersAutoComplete(String query) {
//		List<TaTiers> allValues = taTiersService.selectAll();
//		List<TaTiers> filteredValues = new ArrayList<TaTiers>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaTiers civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
//		List<TaTiersDTO> allValues = taTiersService.findByCodeLight(query);
//		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaTiersDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
	
	public List<TaTReceptionDTO> typeReceptionAutoCompleteDTOLight(String query) {
		List<TaTReceptionDTO> allValues = taTReceptionService.findByCodeLight(query);
		List<TaTReceptionDTO> filteredValues = new ArrayList<TaTReceptionDTO>();
		TaTReceptionDTO civ = new TaTReceptionDTO();
		civ.setCodeTReception(Const.C_AUCUN);
		filteredValues.add(civ);
		for (int i = 0; i < allValues.size(); i++) {
			 civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTReception().toLowerCase().contains(query.toLowerCase())) {
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

//	public List<TaArticle> articleAutoComplete(String query) {
//		List<TaArticle> allValues = taArticleService.selectAll();
//		List<TaArticle> filteredValues = new ArrayList<TaArticle>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaArticle civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
//	
//	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
//		List<TaArticleDTO> allValues = taArticleService.findByCodeLight(query);
//		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
//
//		for (int i = 0; i < allValues.size(); i++) {
//			TaArticleDTO civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}

//	public List<TaEntrepot> entrepotAutoComplete(String query) {
//		List<TaEntrepot> allValues = taEntrepotService.selectAll();
//		List<TaEntrepot> filteredValues = new ArrayList<TaEntrepot>();
//		TaEntrepot civ = new TaEntrepot();
//		civ.setCodeEntrepot(Const.C_AUCUN);
//		filteredValues.add(civ);
//		for (int i = 0; i < allValues.size(); i++) {
//			 civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeEntrepot().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}

//	public List<String> emplacementAutoComplete(String query) {
//		List<String> filteredValues = new ArrayList<String>();
////		filteredValues.add(Const.C_AUCUN);
//		if(selectedLigneJSF!=null && selectedLigneJSF.getTaEntrepot()!=null) {
//			List<String> allValues = taEtatStockService.emplacementEntrepot(selectedLigneJSF.getTaEntrepot().getCodeEntrepot(),null);
//			for (int i = 0; i < allValues.size(); i++) {
//				String civ = allValues.get(i);
//				if(civ !=null && (query.equals("*") || civ.toLowerCase().contains(query.toLowerCase()))) {
//					filteredValues.add(civ);
//				}
//			}
//		}
//		return filteredValues;
//	}
	
//	public List<TaUnite> uniteAutoComplete(String query) {
//		List<TaUnite> allValues = taUniteService.selectAll();
//		List<TaUnite> filteredValues = new ArrayList<TaUnite>();
//		TaUnite civ=new TaUnite();
//		civ.setCodeUnite(Const.C_AUCUN);
//		filteredValues.add(civ);
//		for (int i = 0; i < allValues.size(); i++) {
//			 civ = allValues.get(i);
//			if(query.equals("*") || civ.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
//				filteredValues.add(civ);
//			}
//		}
//
//		return filteredValues;
//	}
	
//	public void actDialogControleLot(ActionEvent actionEvent) {
//		
////		PrimeFaces.current().dialog().openDynamic("aide");
//    
//        Map<String,Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
//        options.put("draggable", false);
//        options.put("closable", false);
//        options.put("resizable", true);
//        options.put("contentHeight", 710);
//        options.put("contentWidth", "100%");
//        //options.put("contentHeight", "100%");
//        options.put("width", 1024);
//        
//        //Map<String,List<String>> params = null;
//        Map<String,List<String>> params = new HashMap<String,List<String>>();
//        List<String> list = new ArrayList<String>();
//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//        params.put("modeEcranDefaut", list);
//        
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		TaLot l = new TaLot();
//		
////		creerLot();
////		sessionMap.put("lotBR", selectedLigneJSF.getTaLot());
//		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
//		//sessionMap.put("numLot", selectedLigneJSF.getDto().getNumLot());
//		sessionMap.put("numLot", numLot);
//        
//        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_controle_article", options, params);
//		
////		FacesContext context = FacesContext.getCurrentInstance();  
////		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
//	}
	
//	public void handleReturnDialogControleLot(SelectEvent event) {
//		if(event!=null && event.getObject()!=null) {
//			List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
//			if(listeControle!=null && !listeControle.isEmpty()) {
//				if(masterEntityLigne.getTaLot().getIdLot()==listeControle.get(0).getR().getTaLot().getIdLot()) {
//					masterEntityLigne.getTaLot().getTaResultatConformites().clear();
//					for(ControleConformiteJSF ctrl : listeControle) {
//						if(ctrl.getR()!=null) {
//							masterEntityLigne.getTaLot().getTaResultatConformites().add(ctrl.getR());
//						}
//					}
//				}
//			}
//		}
//	}
	
	public String validationLot(TaLot taLot) {
		return taLotService.validationLot(taLot);
	}

//	public TaLBonReceptionDTOJSF[] getSelectedTaLBonReceptionDTOJSFs() {
//		return selectedLigneJSFs;
//	}
//
//	public void setSelectedTaLBonReceptionDTOJSFs(
//			TaLBonReceptionDTOJSF[] selectedLigneJSFs) {
//		this.selectedLigneJSFs = selectedLigneJSFs;
//	}
//
//	public TaLBonReceptionDTOJSF getSelectedTaLBonReceptionDTOJSF() {
//		return selectedLigneJSF;
//	}
//
//	public void setSelectedTaLBonReceptionDTOJSF(
//			TaLBonReceptionDTOJSF selectedLigneJSF) {
//		this.selectedLigneJSF = selectedLigneJSF;
//	}
//	
//	public TaBonReceptionDTO[] getSelectedTaBonReceptionDTOs() {
//		return selectedDocumentDTOs;
//	}
//
//	public void setSelectedTaBonReceptionDTOs(TaBonReceptionDTO[] selectedDocumentDTOs) {
//		this.selectedDocumentDTOs = selectedDocumentDTOs;
//	}
//
//	public TaBonReceptionDTO getNewTaBonReceptionDTO() {
//		return newDocumentDTO;
//	}
//
//	public void setNewTaBonReceptionDTO(TaBonReceptionDTO newDocumentDTO) {
//		this.newDocumentDTO = newDocumentDTO;
//	}
//
//	public TaBonReceptionDTO getSelectedTaBonReceptionDTO() {
//		return selectedDocumentDTO;
//	}
//
//	public void setSelectedTaBonReceptionDTO(TaBonReceptionDTO selectedDocumentDTO) {
//		this.selectedDocumentDTO = selectedDocumentDTO;
//	}

//	public void setInsertAuto(boolean insertAuto) {
////		String oncomplete="jQuery('.myclass .ui-datatable-data tr').last().find('span.ui-icon-pencil').each(function(){jQuery(this).click()});";
////		if(insertAuto) {
////			rc.setOncomplete(oncomplete);
////			System.out.println("Ajoutera une nouvelle ligne automatiquement");
////		} else {
////			rc.setOncomplete(null);
////			System.out.println("N'ajoutera pas de nouvelle ligne automatiquement");
////		}
//		this.insertAuto = insertAuto;
//	}

	public TaTReception getTaTReception() {
		return taTReception;
	}

	public void setTaTReception(TaTReception taTReception) {
		this.taTReception = taTReception;
	}

	public TaTReceptionDTO getTaTReceptionDTO() {
		return taTReceptionDTO;
	}

	public void setTaTReceptionDTO(TaTReceptionDTO taTReceptionDTO) {
		this.taTReceptionDTO = taTReceptionDTO;
	}

	public TaTiers getTaTiersPrestationService() {
		return taTiersPrestationService;
	}

	public void setTaTiersPrestationService(TaTiers taTiersPrestationService) {
		this.taTiersPrestationService = taTiersPrestationService;
	}

	public TaTiersDTO getTaTiersPrestationServiceDTO() {
		return taTiersPrestationServiceDTO;
	}

	public void setTaTiersPrestationServiceDTO(TaTiersDTO taTiersPrestationServiceDTO) {
		this.taTiersPrestationServiceDTO = taTiersPrestationServiceDTO;
	}

	@Override
	public void IHMmodel(TaLBonReceptionDTOJSF t, TaLBonReception ligne) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateLignesDocument(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateDocumentAvantEnregistrement(Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateLigneDocumentAvantEnregistrement(Object value) throws ValidatorException {
		String msg = "";

		try {
//			taLBonReceptionService.validateDTOProperty(selectedLigneJSF.getDto(),Const.C_CODE_ARTICLE,  ITaLDevisServiceRemote.validationContext );
//			taLBonReceptionService.validateDTOProperty(selectedLigneJSF.getDto(),Const.C_NUM_LOT,  ITaLDevisServiceRemote.validationContext );
			if(afficheUniteSaisie && selectedLigneJSF.getTaCoefficientUniteSaisie()!=null)
				validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());
			else 
				if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
					if(!calculCoherenceAffectationCoefficientQte2(selectedLigneJSF.getDto().getQte2LDocument())) {
						setLigneAReenregistrer(selectedLigneJSF);
						PrimeFaces.current().executeScript("PF('wVdialogQte2BonReception').show()");
					}

				}
			
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	@Override
	public void initialisationDesInfosComplementaires(Boolean recharger, String typeARecharger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialisationDesCPaiement(Boolean recharger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculDateEcheance(Boolean appliquer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actAppliquerCPaiement() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculTypePaiement(boolean recharger) {
		// TODO Auto-generated method stub
		
	}

	public List<TaLabelArticleDTO> getListeToutesCertification() {
		return listeToutesCertification;
	}

	public void setListeToutesCertification(List<TaLabelArticleDTO> listeToutesCertification) {
		this.listeToutesCertification = listeToutesCertification;
	}
	
	public void actDialogEmail(ActionEvent actionEvent) {
		if(masterEntity.getTaTiersPrestationService()!=null && masterEntity.getTaTiersPrestationService().getTaEmail()!=null){
			EmailParam email = new EmailParam();
			email.setEmailExpediteur(null);
			email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
			email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" "+nomDocument+" "+masterEntity.getCodeDocument()); 
			email.setBodyPlain("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
			email.setBodyHtml("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
			email.setDestinataires(new String[]{masterEntity.getTaTiersPrestationService().getTaEmail().getAdresseEmail()});
			email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiersPrestationService().getTaEmail()});

			EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
			pj1.setFichier(new File((taBonReceptionService).generePDF(masterEntity.getIdDocument(),null, null, null)));
			pj1.setTypeMime("pdf");
			pj1.setNomFichier(pj1.getFichier().getName());
			email.setPiecesJointes(
					new EmailPieceJointeParam[]{pj1}
					);
			actDialogEmail(email);
		}
	}

	
	//public void actDialogRefArticleFournisseur(ActionEvent actionEvent) {
	public void actDialogRefArticleFournisseur(String codeFournisseur, String nouveauCodeBareScanne) {
		
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 440);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
       
        List<String> listFrs = new ArrayList<String>();
        listFrs.add(codeFournisseur);
        params.put("codeFrs", listFrs);
        
        List<String> listCodeBarre = new ArrayList<String>();
        listCodeBarre.add(nouveauCodeBareScanne);
        params.put("codeBarre", listCodeBarre);
        
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("mapDialogue", mapDialogue);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_ref_aticle_fournisseur", options, params);
	}
	
	public void handleReturnDialogRefArticleFournisseur(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaRefArticleFournisseur taRefArticleFournisseur = (TaRefArticleFournisseur) event.getObject();
			if(taRefArticleFournisseur!=null) {
				actValiderCodeBarre(null);
			}
		}
	}

//	public boolean autoriseSuppression(boolean silencieux) {
//		return autoriseSuppressionLigne(silencieux);
//	}
//	public boolean autoriseSuppression() {
//		return autoriseSuppressionLigne(false);
//	}

//	public boolean autoriseSuppression(boolean silencieux) {
//		TaLigneALigneDTO suppressionDocInterdit=null;
//		messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
//		if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals(""))
//			messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
//		List<TaLigneALigneDTO> docLie=rechercheSiDocLieLigneALigne();
//		String documents = "";
//		if(docLie!=null) {
//			PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", docLie!=null);
//
//			if(docLie!=null)
//				for (TaLigneALigneDTO doc : docLie) {
//					if(!documents.equals("") && !doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument()))
//						documents=documents+";"+doc.getCodeDocumentDest();
//					else if(!doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument())) documents=doc.getCodeDocumentDest();
//					
//					if(suppressionDocInterdit==null && doc.getIdLDocumentSrc().compareTo(doc.getIdLigneSrc())==0)
//						suppressionDocInterdit=doc;
//					
////					if(suppressionDocInterdit==null && doc.getTypeDocument().equals(TaBonReception.TYPE_DOC))
////						suppressionDocInterdit=doc;
//				}
//			if(!documents.equals("")) {
//				if(suppressionDocInterdit==null)
//					messageSuppression="Suppression d'un document lié à un autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
//							+ "\r\nSi vous le supprimez, la liaison sera également supprimée."
//							+ "\r\nEtes-vous sur de vouloir le supprimer ?";
//				else {
//					messageSuppression="Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocInterdit.getCodeDocumentDest()+"."
//							+ "\r\nSi vous souhaitez le supprimer, vous devez au préalable supprimer ce document.";
//					if(!silencieux) PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Suppression d'un document lié à un autre",messageSuppression ));
//				}
//			}
//		}
//		return suppressionDocInterdit==null;
//	}
//
//	public boolean autoriseSuppressionLigne(boolean silencieux,ILigneDocumentJSF selectedLigneJSF) {
//		TaLigneALigneDTO suppressionDocInterdit=null;
//		messageSuppression=Const.C_MESSAGE_SUPPRESSION_LIGNE_DOCUMENT;
//		if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals(""))
//			messageSuppression=Const.C_MESSAGE_SUPPRESSION_LIGNE_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
//		List<TaLigneALigneDTO> ligneLie=rechercheSiLigneDocLie(selectedLigneJSF);
//		String lignes = "";
//		if(ligneLie!=null) {
//			PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", ligneLie!=null);
//			if(ligneLie!=null)
//				for (TaLigneALigneDTO doc : ligneLie) {
//					if(!lignes.equals("") && doc.getIdDocumentDest()!=(selectedLigneJSF.getDto().getIdLDocument()))
//						lignes=lignes+";"+doc.getCodeDocumentDest();
//					else if(doc.getIdDocumentDest()!=(selectedLigneJSF.getDto().getIdLDocument())) lignes=doc.getCodeDocumentDest();
//					
//					if(suppressionDocInterdit==null && doc.getIdLDocumentSrc().compareTo(doc.getIdLigneSrc())==0)
//						suppressionDocInterdit=doc;
////					if(suppressionDocInterdit==null && doc.getTypeDocument().equals(TaBonReception.TYPE_DOC))
////						suppressionDocInterdit=doc;
//				}
//			if(!lignes.equals("")) {
//				if(suppressionDocInterdit==null)
//					messageSuppression="Suppression d'une ligne de document liée à une autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+lignes+"."
//							+ "\r\nSi vous la supprimez, la liaison sera également supprimée."
//							+ "\r\nEtes-vous sur de vouloir la supprimer ?";
//				else {
//					messageSuppression="Suppression d'une ligne de document liée à une autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocInterdit.getCodeDocumentDest()+"."
//							+ "\r\nSi vous souhaitez la supprimer, vous devez au préalable supprimer la ligne liée dans ce document.";
//					if(!silencieux) PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Suppression d'une ligne de document liée à une autre",messageSuppression ));
//				}
//			}
//		}
//		return suppressionDocInterdit==null;
//	}

	public boolean autoriseSuppressionLigne() {
		return autoriseSuppressionLigne(false,selectedLigneJSF);
	}


	

	public void actValiderDocument() {
		int position=1;
		int positionEnCours=taEtatService.documentEncours(null).getPosition();
		if(masterEntity!=null) {
			if(masterEntity.getTaREtat()==null ||masterEntity.getTaREtat().getTaEtat()==null  ||(masterEntity.getTaREtat().getTaEtat().getTaTEtat()!=null && masterEntity.getTaREtat().getTaEtat().getPosition().compareTo(positionEnCours)<0)) {
				masterEntity.addREtat(taEtatService.documentEncours(null));
				TaEtat etat = masterEntity.getTaREtat().getTaEtat();
				if(etat!=null)position=etat.getPosition();
				for (TaLBonReception l : masterEntity.getLignes()) {
					l.addREtatLigneDoc(etat);
//					if(l.getTaREtatLigneDocument()==null) {
//						TaREtatLigneDocument taREtat = new TaREtatLigneDocument();
//						taREtat.setTaEtat(masterEntity.getTaEtat());
//						taREtat.setTaLBonReception(l);
//						l.setTaREtatLigneDocument(taREtat);
//					}
//					if(l.getTaREtatLigneDocument()!=null  && l.getTaREtatLigneDocument().getTaEtat()!=null  && l.getTaREtatLigneDocument().getTaEtat().getPosition().compareTo(position)<0) {
////						TaREtatLigneDocument taREtat = new TaREtatLigneDocument();
//						//mettre l'ancien dans historique
//						TaREtatLigneDocument taREtat = l.getTaREtatLigneDocument();
//
//						if(taREtat!=null) {
//						//mettre l'ancien dans historique
//						TaHistREtatLigneDocument taHist = new TaHistREtatLigneDocument();
//						taHist.setTaEtat(taREtat.getTaEtat());
//						taHist.setTaLBonReception(taREtat.getTaLBonReception());
//						l.getTaHistREtatLigneDocuments().add(taHist);
//						}
//						
//						taREtat.setTaEtat(masterEntity.getTaEtat());
//						taREtat.setTaLBonReception(l);
//						l.setTaREtatLigneDocument(taREtat);
//					}
				}
				masterEntity=taBonReceptionService.merge(masterEntity);
				updateTab();
			}
		}

	}
	
	
	public boolean lotEstUnique(TaLot lot) {
		if(lot!=null) {
			return !lot.getVirtuel() || lot.getVirtuelUnique();
		}
		return true;
	}
}  
