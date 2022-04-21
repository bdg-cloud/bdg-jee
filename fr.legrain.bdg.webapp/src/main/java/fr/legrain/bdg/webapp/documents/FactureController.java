package fr.legrain.bdg.webapp.documents;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
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

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLigneALigneEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.osgi.ConstActionInterne;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.model.mapping.mapper.TaEditionMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.EditionParam;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.EmailPieceJointeParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.AbstractDocumentController.RetourAutorisationLiaison;
import fr.legrain.bdg.webapp.paiement.PaiementCbController;
import fr.legrain.bdg.webapp.paiement.PaiementParam;
import fr.legrain.bdg.webapp.reglementMultiple.TaReglementDTOJSF;
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.email.service.EnvoiEmailService;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
//import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class FactureController extends AbstractDocumentController<TaFacture,TaFactureDTO,TaLFacture,TaLFactureDTO,TaLFactureDTOJSF,TaInfosFacture> {  
	
	@Inject @Named(value="ouvertureTiersBean")
	private OuvertureTiersBean ouvertureTiersBean;
	
	@Inject @Named(value="ouvertureArticleBean")
	private OuvertureArticleBean ouvertureArticleBean;

//	public static final String MESSAGE_MULTIPLE_REGLEMENT   = "Document réglé en plusieurs fois";
//	public static final String MESSAGE_SIMPLE_REGLEMENT   = "";
	
	private @EJB ITaFactureServiceRemote taFactureService;

	
	private @EJB ITaAvoirServiceRemote taAvoirService;
	private @EJB ITaReglementServiceRemote taReglementService;

	private @EJB ITaLEcheanceServiceRemote taLEcheanceService;
	private @EJB ITaInfosFactureServiceRemote taInfosFactureService;
	private @EJB ITaLFactureServiceRemote taLFactureService;
	//private @EJB LgrMailjetService lgrMailjetService;
	@EJB private EnvoiEmailService emailServiceFinder;

//	private boolean differenceReglementResteARegle=false;
//	private boolean differenceReglementResteARegleForPrint=false;
    private int countAvoirsAndReglements=0;
    
//    private boolean duplicata=false;
//	private int compteurFacture =0;
	private String theme="";
	private String librairie="";
	

	private String messageConfirmEnregistrement = " ";
//	private String optionImprimer;
//    private List<String> optionsImprimer;
	public FactureController() {  
	}  

	@PostConstruct
	public void postConstruct(){
		super.postConstruct();
		listePreferences= taPreferencesService.findByGroupe("facture");
		nomDocument="Facture";
		setTaDocumentService(taFactureService);
		setTaLDocumentService(taLFactureService);
		setTaInfosDocumentService(taInfosFactureService);
//		 sousMenuEdition = new DefaultSubMenu("Dynamic Submenu");
		
		initListeEdition();
		
		stepSynthese = "idSyntheseFacture";
		stepEntete = "idEnteteFacture";
		stepLignes = "idLignesFacture";
		stepTotaux = "idTotauxFormFacture";
		stepValidation = "idValidationFormFacture";
		idMenuBouton = "form:tabView:idMenuBoutonFacture";
		wvEcran = LgrTab.WV_TAB_FACTURE;
		classeCssDataTableLigne = "myclassFacture";
		idBtnWizardButtonPrecedent = "form:tabView:idBtnWizardButtonPrecedentFacture";
		idBtnWizardButtonSuivant = "form:tabView:idBtnWizardButtonSuivantFacture";
		
		JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = "initialisePositionBoutonWizard('.wizard-facture')";
		gestionBoutonWizardDynamique = true;

		refresh();
		//initChoixEdition();
//		gestionLot = autorisationBean.isModeGestionLot();
		gestionCapsules = autorisationBean.isModeGestionCapsules();
		// init choix du théme
		theme1();

		initialisePositionBoutonWizard();
	}
	public void initListeEdition() {
		listeEdition = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaFacture.TYPE_DOC.toUpperCase());
		TaEditionDTO editionDefautNulle = new TaEditionDTO();
		editionDefautNulle.setLibelleEdition("Facture modèle par défaut (V0220)");
		listeEdition.add(editionDefautNulle);
	}
	
	public boolean isLiaisonASupprimerOuMisADispo() {
		Boolean confirm = false;
		messageConfirmEnregistrement = "";
		if(impressionDirectClient || envoyeParEmail) {
			messageConfirmEnregistrement= " Etes-vous sur de vouloir mettre à disposition ce document au client ? Ce document ne sera plus modifiable.";
			confirm = true;
			
		}
		if(listeLigneALigneEcheanceASupprimer != null && !listeLigneALigneEcheanceASupprimer.isEmpty()) {
			messageConfirmEnregistrement+= " Vous allez supprimer une ou plusieurs lignes du document, et ainsi la liaison à leur"
					+ " éventuelles lignes d'échéances. Pensez à aller annuler l'abonnement ou les lignes d'abonnements concernés si besoin.";
			
			confirm = true;
		}
		
		return confirm;
		
	}
	
	/**
	 * Methode a abstraire et a redécoupée
	 * Cette méthode prépare l'envoi de l'édition par mail en mettant en session une map de paramètre (mode, pouClient, l'edition choisie etc...)
	 * et surtout en remplissant l' objet TaEdition selectedEdition avec cette map de param, si une edition est choisie (id) sinon elle rempli cet objet avec l'edition par defaut
	 * @param edition
	 * @param modeEdition
	 * @param pourClient
	 */
	public void envoiMailEdition(TaEditionDTO edition, String modeEdition, String pourClient) {
		 Map<String,Object> options = new HashMap<String, Object>();
	       
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	        params.put("modeEcranDefaut", list);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametreEdition = null;
			
	
				
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
//			String modeEdition = (String)actionEvent.getComponent().getAttributes().get("mode_edition");
//			String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("facture");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			  

			if(!editionClient) {
				switch (modeEdition) {
				case "BROUILLON":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "BROUILLONAPAYER");
					} else {
						mapParametreEdition.put("mode", "BROUILLON");
					}
					break;
				case "DUPLICATA":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "DUPLICATAPAYER");
					} else {
						mapParametreEdition.put("mode", "DUPLICATA");
					}
					break;
				case "PAYER":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "PAYER");
					} else {
						mapParametreEdition.put("mode", "PAYER");
					}
					break;
	
				default:
					break;
				}
			} else {
				if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
					mapParametreEdition.put("mode", "CLIENTAPAYER");
				} else {
					mapParametreEdition.put("mode", "CLIENT");
				}
				
			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_FACTURE_DEFAUT);
			editionParam.setMapParametreEdition(mapParametreEdition);
			
			sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, editionParam);
	        
			
	        //PrimeFaces.current().dialog().openDynamic("/dialog_choix_edition", options, params);
			
			//ICI s'arrete la partie actDialogImprimer
			
			//ICI COMMENCE LA PARTIE handleReturnDialogImprimer
			String pdf="";
			TaEdition taEdition = null;
			if(edition == null) {//si aucune edition n'est choisie
				TaActionEdition action = new TaActionEdition();
				action.setCodeAction(TaActionEdition.code_mail);
				
				taEdition = taEditionService.rechercheEditionActionDefaut(null,action, TaFacture.TYPE_DOC);
				
			}else {//si une édition est choisie
				
				 if(edition.getId() != null) {//si ce n'est pas l'édition defaut programme
					 try {
							taEdition = taEditionService.findById(edition.getId());
						} catch (FinderException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				 } 
				
				if(taEdition == null) {
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_FACTURE_DEFAUT).get(0);
				}
				
				
			}
			//On rempli selectedEdition qui va être utiliser dans AbstractDocumentController.actDialogEmailSelectedEdition
			if(taEdition!=null) {
				
				taEdition.setMapParametreEdition(mapParametreEdition);
				selectedEdition = taEdition;

				
			}
	}
	
	/**
	 * Méthode a finir d'abstraire et redécoupé
	 * (elle ne peut pas être déplacé et extraite telle quelle car il y a de multiples références au type de document comme factures etc...)
	 * Cette méthode fait quasiment la même chose que envoiMailEdition mais pour l'impression.
	 * Cette méthode qui va remplir l'edition choisie  avec une map de param (mode, pourClient), 
	 * puis va écrire un birt xml de l'édition dans un fichier tmp, qui renvoit un localPath, et enfin appelé generePDF avec le localPath et l'edition en param.
	 * Si aucune édition n'est choisie, la méthode generePDF est appellé sans passage du parametre édition.
	 * Ce generePDF sans édition va se charger de trouver l'édition par defaut en fonction de l'action passé en param.
	 * Dans les deux cas, a la fin, cette méthode ouvre un onglet pour afficher le PDF
	 * @author yann
	 * @param edition
	 * @param modeEdition
	 * @param pourClient
	 */
	public void imprimeEdition(TaEditionDTO edition, String modeEdition, String pourClient) {
		//ICI COMMENCE LA PARTIE actDialogImprimer
		 Map<String,Object> options = new HashMap<String, Object>();
	       
	        
	        Map<String,List<String>> params = new HashMap<String,List<String>>();
	        List<String> list = new ArrayList<String>();
	        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
	        params.put("modeEcranDefaut", list);
	        
	        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String, Object> mapParametreEdition = null;
			
	
				
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("facture");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			  

			if(!editionClient) {
				switch (modeEdition) {
				case "BROUILLON":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "BROUILLONAPAYER");
					} else {
						mapParametreEdition.put("mode", "BROUILLON");
					}
					break;
				case "DUPLICATA":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "DUPLICATAPAYER");
					} else {
						mapParametreEdition.put("mode", "DUPLICATA");
					}
					break;
				case "PAYER":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "PAYER");
					} else {
						mapParametreEdition.put("mode", "PAYER");
					}
					break;
	
				default:
					break;
				}
			} else {
				if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
					mapParametreEdition.put("mode", "CLIENTAPAYER");
				} else {
					mapParametreEdition.put("mode", "CLIENT");
				}
				
				//Mise à jour de la mise à dispostion
				if(masterEntity.getTaMiseADisposition()==null) {
					masterEntity.setTaMiseADisposition(new TaMiseADisposition());
				}
				Date dateMiseADispositionClient = new Date();
				masterEntity.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
				masterEntity = taFactureService.mergeAndFindById(masterEntity,ITaFactureServiceRemote.validationContext);
				
			
			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_FACTURE_DEFAUT);
			editionParam.setMapParametreEdition(mapParametreEdition);
			
			sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, editionParam);
	        
			
			
			//ICI s'arrete la partie actDialogImprimer
			
			//ICI COMMENCE LA PARTIE handleReturnDialogImprimer
			String pdf="";
			if(edition == null) {//si aucune edition n'est choisie
				
				TaActionEdition actionImprimer = new TaActionEdition();
				actionImprimer.setCodeAction(TaActionEdition.code_impression);

				//on appelle une methode qui va aller chercher (et elle crée un fichier xml (writing)) elle même l'edition par defaut choisie en fonction de l'action si il y a 
				pdf = taFactureService.generePDF(selectedDocumentDTO.getId(),mapParametreEdition,null,null,actionImprimer);
				
			}else {//si une edition est choisie
				
				  TaEdition taEdition = null;
				  
				  if( edition.getId() != null) {//si l'édition choisie n'est pas l'edition defaut programme
					  try {
							//on récupère l'objet édition
							taEdition = taEditionService.findById(edition.getId());
						} catch (FinderException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				  }
				
				if(taEdition == null) {
					//si cette edition n'existe pas, on prend celle programme
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_FACTURE_DEFAUT).get(0);
				}
					if(taEdition!=null) {
						taEdition.setMapParametreEdition(mapParametreEdition);
						List<String> listeResourcesPath = null;
						if(taEdition.getResourcesPath()!=null) {
							listeResourcesPath = new ArrayList<>();
							listeResourcesPath.add(taEdition.getResourcesPath());
						}

						try { 
							//on créer le fichier birt xml de cette édition dans /tmp/bdg/demo
							String localPath  = taFactureService.writingFileEdition(taEdition);
							//on genere le pdf avec le fichier xml crée ci-dessus
							 pdf = taFactureService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
							

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
			}
			//Enfin on ouvre un onglet pour afficher le PDF
			try {
				String urlEncoded = URLEncoder.encode(pdf, "UTF-8");
				PrimeFaces.current().executeScript("window.open('/edition?fichier="+urlEncoded+"')");
				masterEntity = taFactureService.findById(masterEntity.getIdDocument());
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			updateTab();
		  
			
			
			//ICI FINI LA PARTIE handleReturnDialogImprimer
			
	}
	/**
	 * Méthode dépréciée
	 * Méthode qui rempli choixEdition si une choix personnalisé d'édition à été fait
	 * @author yann
	 */
	public void initChoixEdition() {
		choixEdition = false;
		List<TaEdition> listeEditionDisponible = taEditionService.rechercheEditionDisponible("", ConstActionInterne.EDITION_FACTURE_DEFAUT,null);
		
		if(listeEditionDisponible!=null && !listeEditionDisponible.isEmpty() && listeEditionDisponible.size()>1) {
			choixEdition = true;
		}
	}
	
	public void refresh() {
//		values = taFactureService.selectAllDTO();
		values = taFactureService.findAllLight();
		for (TaFactureDTO f : values) {
			f.setNbDecimalesPrix(6);
		}
		valuesLigne = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}


	public void IHMmodel(TaLFactureDTOJSF t, TaLFacture ligne) {

		LgrDozerMapper<TaLFacture,TaLFactureDTO> mapper = new LgrDozerMapper<TaLFacture,TaLFactureDTO>();
		TaLFactureDTO dto = t.getDto();

		dto = (TaLFactureDTO) mapper.map(ligne, TaLFactureDTO.class);
		dto.setEmplacement(ligne.getEmplacementLDocument());
		t.setDto(dto);
		t.setTaLot(ligne.getTaLot());
		if(t.getArticleLotEntrepotDTO()==null) {
			t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
		}
		if(ligne.getTaLot()!=null) {
			t.getArticleLotEntrepotDTO().setNumLot(ligne.getTaLot().getNumLot());
		}
		t.setTaArticle(ligne.getTaArticle());
		if(ligne.getTaArticle()!=null){
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
		if(ligne.getCodeTitreTransport()!=null) {
			t.setTaTitreTransport(new TaTitreTransport());
			t.getTaTitreTransport().setCodeTitreTransport(ligne.getCodeTitreTransport());
		}
		t.updateDTO(false);

	}
	
	public List<TaLFactureDTOJSF> IHMmodel() {
		LinkedList<TaLFacture> ldao = new LinkedList<TaLFacture>();
		LinkedList<TaLFactureDTOJSF> lswt = new LinkedList<TaLFactureDTOJSF>();

		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLFacture,TaLFactureDTO> mapper = new LgrDozerMapper<TaLFacture,TaLFactureDTO>();
			TaLFactureDTO dto = null;
			for (TaLFacture o : ldao) {
				TaLFactureDTOJSF t = new TaLFactureDTOJSF();
				dto = (TaLFactureDTO) mapper.map(o, TaLFactureDTO.class);
				dto.setEmplacement(o.getEmplacementLDocument());
				t.setDto(dto);
				t.setTaLot(o.getTaLot());
				if(t.getArticleLotEntrepotDTO()==null) {
					t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
				}
				if(o.getTaLot()!=null) {
					t.getArticleLotEntrepotDTO().setNumLot(o.getTaLot().getNumLot());
				}
				t.setTaArticle(o.getTaArticle());

				if(o.getTaArticle()!=null){
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

				if(o.getCodeTitreTransport()!=null) {
					try {
						t.setTaTitreTransport(taTitreTransportService.findByCode(o.getCodeTitreTransport()));
						if(t.getTaArticle()!=null && t.getTaArticle().getTaRTaTitreTransport()!=null)
							t.getTaArticle().getTaRTaTitreTransport().setTaTitreTransport(t.getTaTitreTransport());
					} catch (FinderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					t.setTaTitreTransport(new TaTitreTransport());
//					t.getTaTitreTransport().setCodeTitreTransport(o.getCodeTitreTransport());
				}
				t.setTaREtatLigneDocument(o.getTaREtatLigneDocument());
				if(o.getTaREtatLigneDocument()!=null && o.getTaREtatLigneDocument().getTaEtat()!=null)
					t.setCodeEtat(o.getTaREtatLigneDocument().getTaEtat().getCodeEtat());
				t.updateDTO(false);
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

	public void actEnregistrerLigne(ActionEvent actionEvent) {

		try {
			selectedLigneJSF.updateDTO(false);
			
			masterEntityLigne.setTaArticle(selectedLigneJSF.getTaArticle());
			if(masterEntityLigne.getTaArticle()==null && masterEntityLigne.getTaMouvementStock()!=null) {
				masterEntityLigne.setTaMouvementStock(null);
			}
			if(masterEntityLigne.getTaArticle()!=null && selectedLigneJSF.getDto().getPrixULDocument()==null) {
				masterEntityLigne.setPrixULDocument(BigDecimal.ZERO);
				selectedLigneJSF.getDto().setPrixULDocument(BigDecimal.ZERO);
			}
			
			masterEntityLigne.setTaEntrepot(selectedLigneJSF.getTaEntrepot());
			masterEntityLigne.setTaLot(selectedLigneJSF.getTaLot());
			masterEntityLigne.setEmplacementLDocument(selectedLigneJSF.getDto().getEmplacement());

			LgrDozerMapper<TaLFactureDTO,TaLFacture> mapper = new LgrDozerMapper<TaLFactureDTO,TaLFacture>();
			mapper.map((TaLFactureDTO) selectedLigneJSF.getDto(),masterEntityLigne);
			
			IHMmodel(selectedLigneJSF,masterEntityLigne);
			//masterEntityLigne.setTaArticle(selectedLigneJSF.getTaArticle());
			
			if(masterEntityLigne.getCodeTvaLDocument()!=null && !masterEntityLigne.getCodeTvaLDocument().isEmpty()) {
				TaTva tva=taTvaService.findByCode(masterEntityLigne.getCodeTvaLDocument());
				if(tva!=null)
					masterEntityLigne.setLibTvaLDocument(tva.getLibelleTva());
			}

			masterEntity.enregistreLigne(masterEntityLigne);
			if(!masterEntity.getLignes().contains(masterEntityLigne))
				masterEntity.addLigne(masterEntityLigne);	

			//taBonReceptionService.calculeTvaEtTotaux(masterEntity);
			masterEntity.calculeTvaEtTotaux();
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);


			verifSiDifferenceReglement();

			modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
			//modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actEnregisterLigne")); 
		}
	}


	
	private void verifSiDifferenceReglement(){
		differenceReglementResteARegle=false;
																																																																					                                                                 if(masterEntity!=null && masterEntity.getTaReglement()!=null && modeEcran.dataSetEnModif()){
//		if(masterEntity!=null  && modeEcran.dataSetEnModif()){	
		differenceReglementResteARegle=masterEntity.getRegleCompletDocument().compareTo(masterEntity.getNetTtcCalc())!=0;
			if(differenceReglementResteARegle){
				factureReglee=false;
				actInitReglement();
			}
		}
		
		PrimeFaces.current().ajax().addCallbackParam("differenceReglementResteARegle", differenceReglementResteARegle);
		if(differenceReglementResteARegle)
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Règlement", "La facture est associée à un règlement dont le montant diffère du total de la facture."
					+ "\r\nVous devrez revalider le règlement."));
	}
	
	private void verifSiDifferenceReglementForImpression(){
		differenceReglementResteARegleForPrint=false;
//		if(masterEntity!=null && masterEntity.getTaReglement()!=null && modeEcran.dataSetEnModif()){
		if(masterEntity!=null){	
			differenceReglementResteARegleForPrint=masterEntity.getRegleCompletDocument().compareTo(masterEntity.getNetTtcCalc())!=0;
			
		}
		
	}
	
	public void actMiseADispositionServiceClient(ActionEvent actionEvent) {
		if(masterEntity.getTaMiseADisposition()==null) {
			masterEntity.setTaMiseADisposition(new TaMiseADisposition());
		}
		if(masterEntity.getTaMiseADisposition().getAccessibleSurCompteClient()==null) {
			Date dateMiseADispositionClient = new Date();
			masterEntity.getTaMiseADisposition().setAccessibleSurCompteClient(dateMiseADispositionClient);
			masterEntity = taFactureService.mergeAndFindById(masterEntity,ITaFactureServiceRemote.validationContext);
			updateTab();
		}
	}

	public void actRefresh(ActionEvent actionEvent) {
		TaPreferences nb;
		nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
		if(nb!=null && nb.getValeur() != null) {
			prefNbDecimalesPrix=LibConversion.stringToInteger(nb.getValeur());
		}

//		nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//		if(nb!=null && nb.getValeur() != null) {
//			prefNbDecimalesQte=LibConversion.stringToInteger(nb.getValeur());
//		}
		selectedDocumentDTO.setNbDecimalesPrix(prefNbDecimalesPrix);
	}
	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taFactureService.annuleCode(selectedDocumentDTO.getCodeDocument());
				}				
				masterEntity=new TaFacture();
				selectedDocumentDTO=new TaFactureDTO();

				if(values.size()>0)	selectedDocumentDTO = values.get(0);

				onRowSelect(null);

				valuesLigne=IHMmodel();
				initInfosDocument();
				break;
			case C_MO_EDITION:
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					masterEntity = taFactureService.findByIDFetch(selectedDocumentDTO.getId());
					selectedDocumentDTO=taFactureService.findByIdDTO(selectedDocumentDTO.getId());
				}				
				break;

			default:
				break;
			}			

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", "actAnnuler")); 
			}
		} catch (FinderException e) {
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
		if(taTPaiement!=null) {
			validateUIField(Const.C_CODE_T_PAIEMENT,taTPaiement.getCodeTPaiement());
			selectedDocumentDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
		}

		if(taTTvaDoc!=null) {
			validateUIField(Const.C_CODE_T_TVA_DOC,taTTvaDoc.getCodeTTvaDoc());
			selectedDocumentDTO.setCodeTTvaDoc(taTTvaDoc.getCodeTTvaDoc());
		}

		if(taCPaiementDoc!=null) {
			validateUIField(Const.C_CODE_C_PAIEMENT,taCPaiementDoc.getCodeCPaiement());
			selectedCPaiement.setCodeCPaiement(taCPaiementDoc.getCodeCPaiement());
		}
//		if(selectedEtatDocument!=null) {
//			validateUIField(Const.C_CODE_ETAT,selectedEtatDocument.getCodeEtat());
//			selectedDocumentDTO.setCodeEtat(selectedEtatDocument.getCodeEtat());
//			masterEntity.setTaEtat(selectedEtatDocument);
//		}
		if(masterEntity.getTaRReglement()!=null){
			TaReglement reglement=masterEntity.getTaRReglement().getTaReglement();
			if(reglement!=null){
			selectedDocumentDTO.setMontantReglement(reglement.getNetTtcCalc());
			selectedDocumentDTO.setLibelleReglement(reglement.getLibelleDocument());
			if(reglement.getTaTPaiement()!=null)
				selectedDocumentDTO.setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
			}
			masterEntity.calculRegleDocument();
		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTiers = null;
			taTiersDTO = null;
			taTPaiement = null;
			taTTvaDoc = null;
			taReglement = null;
			taCPaiementDoc = null;


			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
				taTiers = taTiersService.findByCode(selectedDocumentDTO.getCodeTiers());
				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
			}
			if(selectedDocumentDTO.getCodeTPaiement()!=null && !selectedDocumentDTO.getCodeTPaiement().equals("")) {
				taTPaiement = taTPaiementService.findByCode(selectedDocumentDTO.getCodeTPaiement());
			}


			if(selectedDocumentDTO.getCodeTTvaDoc()!=null && !selectedDocumentDTO.getCodeTTvaDoc().equals("")) {
				taTTvaDoc = taTTvaDocService.findByCode(selectedDocumentDTO.getCodeTTvaDoc());
			}

			if(selectedCPaiement.getCodeCPaiement()!=null && !selectedCPaiement.getCodeCPaiement().equals("")) {
				taCPaiementDoc = taCPaiementService.findByCode(selectedCPaiement.getCodeCPaiement());
			}	

			//rajouté temporairement pour aller vite, sera remplacé par la gestion des réglements multiples
			factureReglee=(masterEntity.getTaRReglement()!=null && masterEntity.getTaRReglement().getTaReglement()!=null
					&& masterEntity.getTaRReglement().getTaReglement().getIdDocument()!=0);
			if(masterEntity.getTaRReglement()!=null && masterEntity.getTaRReglement().getTaReglement()!=null){
				taReglement=masterEntity.getTaRReglement().getTaReglement();

				if(taReglement.getTaTPaiement()!=null && taReglement.getTaTPaiement().getIdTPaiement()!=0)
					taTPaiement=taTPaiementService.findById(taReglement.getTaTPaiement().getIdTPaiement());
				if(taTPaiement!=null){
					selectedDocumentDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
				}
				selectedDocumentDTO.setLibelleReglement(taReglement.getLibelleDocument());
				selectedDocumentDTO.setMontantReglement(masterEntity.getTaRReglement().getAffectation());
			}else{
				selectedDocumentDTO.setLibelleReglement("");
				selectedDocumentDTO.setMontantReglement(BigDecimal.ZERO);
			}
			masterEntity.calculRegleDocument();
			factureARegler=masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)!=0;
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {
		docEnregistre=false;
		try {
			masterEntity.calculeTvaEtTotaux();
			verifSiDifferenceReglement();

			if(!differenceReglementResteARegle){
				autoCompleteMapUIToDTO();
				validateDocumentAvantEnregistrement(selectedDocumentDTO);
				mapperUIToModel.map(selectedDocumentDTO, masterEntity);


				initInfosDocument();			
				mapperUIToModelDocumentVersInfosDoc.map(masterEntity, taInfosDocument);
				taInfosDocument.setNomTiers(selectedDocumentDTO.getNomTiers());
				taInfosDocument.setCodeTTvaDoc(selectedDocumentDTO.getCodeTTvaDoc());

				mapperUIToModelAdresseFactVersInfosDoc.map((AdresseInfosFacturationDTO) selectedAdresseFact, taInfosDocument);
				mapperUIToModelAdresseLivVersInfosDoc.map((AdresseInfosLivraisonDTO) selectedAdresseLiv, taInfosDocument);						
				mapperUIToModelCPaiementVersInfosDoc.map((TaCPaiementDTO) selectedCPaiement, taInfosDocument);

				//			//vérifier remplissage du codeTTva
				//			if(!((IStructuredSelection)viewerComboLocalisationTVA.getSelection()).isEmpty()) {
				//				String codeTTvaDoc = ((TaTTvaDoc)((IStructuredSelection)viewerComboLocalisationTVA.getSelection()).getFirstElement()).getCodeTTvaDoc();
				//				taInfosDocument.setCodeTTvaDoc(codeTTvaDoc);
				//			}			
				TaTLigne typeLigneCommentaire =  taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);
				masterEntity.setLegrain(false);
				List<TaLFacture> listeLigneVide = new ArrayList<TaLFacture>(); 
				for (TaLFacture ligne : masterEntity.getLignes()) {
					ligne.setLegrain(false);
					
					if(ligne.ligneEstVide() && ligne.getNumLigneLDocument().compareTo(masterEntity.getLignes().size())==0) {
						listeLigneVide.add(ligne);
					} else if(ligne.getTaArticle()==null) {
						ligne.setTaTLigne(typeLigneCommentaire);
					} else if(masterEntity.getGestionLot() && ligne.getTaArticle()!=null && ligne.getTaArticle().getGestionLot() && ligne.getTaLot()==null){
						throw new Exception("Le numéro du lot doit être rempli");
					} else {
						if(!masterEntity.getGestionLot() || (ligne.getTaArticle()!=null && !ligne.getTaArticle().getGestionLot())) {
							//utiliser un lot virtuel
							if(ligne.getTaLot()==null ){								
								listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
								listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(ligne.getTaArticle().getCodeArticle(),false);
								if(listArticleLotEntrepot!=null && listArticleLotEntrepot.size()>0) {
									ArticleLotEntrepotDTO lot=listArticleLotEntrepot.get(0);
									ligne.setTaLot(taLotService.findByCode(lot.getNumLot()));
									if(lot.getIdEntrepot()!=null)ligne.setTaEntrepot(taEntrepotService.findById(lot.getIdEntrepot()));
									if(lot.getEmplacement()!=null && !lot.getEmplacement().isEmpty())ligne.setEmplacementLDocument(lot.getEmplacement());
								}else {
									ligne.setTaLot(taLotService.findOrCreateLotVirtuelArticle(ligne.getTaArticle().getIdArticle()));
								}
							}
						}
					}
				}

				//supression des lignes vides
				for (TaLFacture taLBonReception : listeLigneVide) {
					masterEntity.getLignes().remove(taLBonReception);
				}
				
				//suppression des liaisons entre ligne doc et ligne d'échéance
				for (TaLigneALigneEcheance ligneALigne : listeLigneALigneEcheanceASupprimer) {
					//ech.setTaLFacture(null);
					//taLEcheanceService.merge(ech);
					taLigneALigneEcheanceService.remove(ligneALigne);
				}

				//isa le 08/11/2016
				//j'ai rajouté cette réinitialisation tant que l'on enlève les lignes vides, sinon génère des trous dans la numérotation des lignes
				masterEntity.reinitialiseNumLignes(0, 1);

				boolean mouvementerLigneDocument = masterEntity.getMouvementerStock(); //TODO uniquement paramétré par les préférences pour l'instant, ajouter un cb au niveau du document alimenter par la préférence et un cb sur chaque ligne
				
				if(masterEntity.getIdDocument()==0) {
					if(masterEntity.getTaGrMouvStock()!=null) {
						masterEntity.getTaGrMouvStock().setTaFacture(null);
						masterEntity.getTaGrMouvStock().getTaMouvementStockes().clear();
					}
					masterEntity.setTaGrMouvStock(null);
				}
				
				if(mouvementerLigneDocument) {
				/*
				 * Gérer les mouvements corrrespondant aux lignes
				 * si le document n'est pas déjà lié à un document qui en contient ou s'il en a déjà un 
				 */
				dejaLie=rechercheSiDocLie()!=null;
				if(masterEntity.getTaGrMouvStock()!=null || !dejaLie) {
					//Création du groupe de mouvement
					TaGrMouvStock grpMouvStock = new TaGrMouvStock();
					if(masterEntity.getTaGrMouvStock()!=null) {
						grpMouvStock=masterEntity.getTaGrMouvStock();
					}
					grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeDocument());
					grpMouvStock.setDateGrMouvStock(masterEntity.getDateDocument());
					grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument()!=null?masterEntity.getLibelleDocument():"Facture "+masterEntity.getCodeDocument());
					grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("T")); // type mouvement Facture
					masterEntity.setTaGrMouvStock(grpMouvStock);
					grpMouvStock.setTaFacture(masterEntity);

					for (TaLFacture l : masterEntity.getLignes()) {
						if(!l.getTaTLigne().equals(typeLigneCommentaire)){
							l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
							if(l.getTaMouvementStock()!=null)
								l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
						}
					}

					//
					grpMouvStock.getTaMouvementStockes().clear();

					//Création des lignes de mouvement
					for (TaLFacture ligne : masterEntity.getLignes()) {
						if(!ligne.getTaTLigne().equals(typeLigneCommentaire)){
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
							if(ligne.getQteLDocument()!=null)mouv.setQte1Stock(ligne.getQteLDocument().multiply(BigDecimal.valueOf(-1)));
							if(ligne.getQte2LDocument()!=null)mouv.setQte2Stock(ligne.getQte2LDocument().multiply(BigDecimal.valueOf(-1)));
							mouv.setUn1Stock(ligne.getU1LDocument());
							mouv.setUn2Stock(ligne.getU2LDocument());
							mouv.setTaGrMouvStock(grpMouvStock);
							//				ligne.setTaLot(null);
							ligne.setTaMouvementStock(mouv);

							grpMouvStock.getTaMouvementStockes().add(mouv);
							//				}
						}
					}
				}
				}
				if(masterEntity.getTaRReglement()!=null){
					if((masterEntity.getTaRReglement().getEtatDeSuppression()==IHMEtat.suppression)){

					}else{
						if(!masterEntity.getMiseADispo())
							masterEntity.getTaRReglement().setEtat(masterEntity.getTaRReglement().getEtat()|IHMEtat.integre);
						else masterEntity.getTaRReglement().setEtat(masterEntity.getTaRReglement().getEtat()&IHMEtat.integre);

						TaReglement reglement=masterEntity.getTaRReglement().getTaReglement();
						reglement.setTaTiers(masterEntity.getTaTiers());
						reglement.setTaTPaiement(masterEntity.getTaTPaiement());

						if(reglement.getIdDocument()==0 || 
								(reglement.getCodeDocument()==null||reglement.getCodeDocument().equals("")))
							reglement.setCodeDocument(taReglementService.genereCode(null) );
						masterEntity.addRReglement(masterEntity.getTaRReglement());
					}
				}
				
				
				if(miseADispositionCompteClient || (impressionDirect && impressionDirectClient) || envoyeParEmail) {
					if(masterEntity.getTaMiseADisposition()==null) {
						masterEntity.setTaMiseADisposition(new TaMiseADisposition());
					}
					Date dateMiseADispositionClient = new Date();
					if(miseADispositionCompteClient) {
						masterEntity.getTaMiseADisposition().setAccessibleSurCompteClient(dateMiseADispositionClient);
					}
					if(impressionDirect && impressionDirectClient) {
						masterEntity.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
					}
					if(envoyeParEmail) {
						masterEntity.getTaMiseADisposition().setEnvoyeParEmail(dateMiseADispositionClient);
					}
				}
				masterEntity = taFactureService.mergeAndFindById(masterEntity,ITaFactureServiceRemote.validationContext);
				
				taFactureService.annuleCode(selectedDocumentDTO.getCodeDocument());

				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				autoCompleteMapDTOtoUI();
				
				selectedEtatDocument=null;
				if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
				
				selectedDocumentDTOs=new TaFactureDTO[]{selectedDocumentDTO};


				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
					values.add(selectedDocumentDTO);
				}

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
				
				/*
				 * l'ouverture de la fenêtre est déclenché par le oncomplete sur le bouton enregistrer,
				 * la checkbox du dernier step (Totaux) est liée à la même valeur qu'une autre checkbox invisible dans le step 1
				 * le oncomplete est déclenché après les update ajax, or l'action suivante est "wizardDevis.setStep(this.stepEntete);"
				 * donc le oncomplete s'execute alors que nous somme déjà revenu sur le step 1
				 * Il faut donc reprendre cette valeur si on souhaite y accéder à partir de PF('moncomposantcheckbox') dans le javascript du oncomplete
				 */
				if(impressionDirectClient) {
					impressionDirect = true;
				}
				if(impressionDirect) {
					if(impressionDirectClient) {
						imprimeEdition(null, "CLIENT", "true"); 
					}else {
						imprimeEdition(null, "BROUILLON", "false");
					}
					
				}
				impressionDirectClient = false;
				impressionDirect = false;

				if(envoyeParEmail) {
					envoiMailEdition(null, "CLIENT", "true");
				}
				
				updateTab();
				
				ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
				if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
				changementStepWizard(false);
				

			}
			docEnregistre=true;
		} catch(Exception e) {
			docEnregistre=false;
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Facture", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actEnregistrer")); 
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			impressionDirectClient = false;
			impressionDirect = false;
			miseADispositionCompteClient = false;
			envoyeParEmail = false;
			
			listePreferences= taPreferencesService.findByGroupe("facture");
			selectedDocumentDTO = new TaFactureDTO();
			selectedDocumentDTO.setCommentaire(rechercheCommentaireDefautDocument());
			masterEntity = new TaFacture();
			
			//Tous les documents ont cet état par défaut vue avec philippe le 06/08/2019
//			TaEtat etat = taEtatService.findByCode(TaEtat.ETAT_ENCOURS_NON_TRANSFORME);
			TaEtat etat = taFactureService.rechercheEtatInitialDocument();
			masterEntity.addREtat(etat);
			
			masterEntity.setTaGrMouvStock(new TaGrMouvStock());
			masterEntity.setLegrain(true);
			masterEntity.setUtiliseUniteSaisie(afficheUniteSaisie);//récupéré à partir d'une préférence
			selectedDocumentDTO.setUtiliseUniteSaisie(afficheUniteSaisie);

			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			factureReglee=false;
			libelleMultipleReglement=MESSAGE_SIMPLE_REGLEMENT;
			selectedEtatDocument=null;
			autoCompleteMapDTOtoUI();
			if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			initInfosDocument();

			//			selectedDocumentDTO.setCodeDocument(taBonReceptionService.genereCode()); //ejb
			//			validateUIField(Const.C_CODE_DOCUMENT,selectedDocumentDTO.getCodeDocument());//permet de verrouiller le code genere

			//			selectedDocumentDTO.setCodeDocument(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			Map<String, String> paramsCode = new LinkedHashMap<>();
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
			//			paramsCode.put(Const.C_NOMFOURNISSEUR, selectedDocumentDTO.getNomTiers());

			if(selectedDocumentDTO.getCodeDocument()!=null) {
				taFactureService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			
			//			String newCode = taFactureService.genereCode(paramsCode);
			//			if(newCode!=null && !newCode.equals("")){
			//				selectedDocumentDTO.setCodeDocument(newCode);
			//			}
			selectedDocumentDTO.setCodeDocument(taFactureService.genereCode(paramsCode));
			changementTiers(true);

			
			updateReglements();

			//			selectedDocumentDTO.setCodeTTvaDoc("F");
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				selectedDocumentDTO.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
				masterEntity.setGestionLot(LibConversion.StringToBoolean(p.getValeur()));
			}
			
			TaPreferences nb;
			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
			if(nb!=null && nb.getValeur() != null) {
				masterEntity.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
			}
			
			TaPreferences imp = taPreferencesService.findByGoupeAndCle("facture", ITaPreferencesServiceRemote.P_IMPRESSION_DIRECT);
			if(imp!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				impressionDirect = LibConversion.StringToBoolean(imp.getValeur());
			}

//			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//			if(nb!=null && nb.getValeur() != null) {
//				masterEntity.setNbDecimalesQte(LibConversion.stringToInteger(nb.getValeur()));
//			}
			
			btnPrecedentVisible = false;
			btnSuivantVisible = true;
			initialisePositionBoutonWizard();
			


			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FACTURE);
		b.setTitre("Facture");
		b.setTemplate("documents/facture.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FACTURE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FACTURE);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		//		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", 
					"Nouveau"
					)); 
		}
	} 

	public void recupMasterEntity() {
		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION)) {
			try {
				masterEntity=taFactureService.findByIDFetch(masterEntity.getIdDocument());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void actModifier(ActionEvent actionEvent) {
		recupMasterEntity();

		super.actModifier(actionEvent);
	}
	public void actSupprimer(ActionEvent actionEvent) {
		TaFacture obj = new TaFacture();
		try {
			if(autorisationLiaisonComplete()) {
				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
					obj = taFactureService.findByIDFetch(selectedDocumentDTO.getId());
				}
				if(verifSiEstModifiable(obj)) {
					if(obj!=null) {
						taFactureService.remove(obj,false);
						values.remove(selectedDocumentDTO);
					} else {
						if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>=1) {
							for (TaFactureDTO d : selectedDocumentDTOs) {
								taFactureService.removeDTO(d);
								values.remove(d);
							}
						}
					}

					if(!values.isEmpty()) {
						selectedDocumentDTO = values.get(0);
						selectedDocumentDTOs = new TaFactureDTO[]{selectedDocumentDTO};
						updateTab();
					}else{
						selectedDocumentDTO = new TaFactureDTO();
						selectedDocumentDTOs = new TaFactureDTO[]{};
					}

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

					if(ConstWeb.DEBUG) {
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage("Facture", "actSupprimer"));
					}
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Facture", e.getMessage()));
		}	    
	}

	public void actFermer(ActionEvent actionEvent) {
		//fermeture de l'onglet en JavaScript
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

		selectedDocumentDTO=new TaFactureDTO();
		selectedDocumentDTOs=new TaFactureDTO[]{selectedDocumentDTO};
		
		gestionBoutonWizardDynamique = false;
		updateTab();
		
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeFacture').filter()");

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actFermer")); 
		}
	}

// *******************************************************************************
	
	public void actImprimerListeFacture(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Facture", "actImprimer"));
		}
		try {
//			if (compteurFacture >= 3) {
//				compteurFacture =0;
//			}
//			compteurFacture++;
//			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//			Map<String, Object> sessionMap = externalContext.getSessionMap();
//			sessionMap.put("listeFacture", values);
//			if (compteurFacture == 1) {
//				sessionMap.put("mode", "1");
//				
//			} else if (compteurFacture == 2){
//				sessionMap.put("mode", "2");
//			} else if (compteurFacture == 3){
//				sessionMap.put("mode", "3");
//			}
			System.out.println("FactureController.actImprimer()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}    
	
	public void actImprimerGlobal(ActionEvent actionEvent,/*boolean editionClient,*/ String modeRegle, String modeNonRegle) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Facture", "actImprimer")); 
		}
		try {
			boolean editionClient = false;
			if(actionEvent!=null){
				String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
				editionClient = LibConversion.StringToBoolean(pourClient);
			}
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaFacture doc =taFactureService.findById(selectedDocumentDTO.getId());
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );

			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			//mapEdition.put("LibelleJournalTva", taTTvaDoc.getLibelleEdition());

			mapEdition.put("lignes", masterEntity.getLignes());
			
//			mapEdition.put("lignesArticle", masterEntityLigne.getTaArticle().getCodeArticle());
//			mapEdition.put("lignesLibelleArticle", masterEntityLigne.getTaArticle().getLibellelArticle());

			//Traitement des différents règlements
			List<TaRReglement> listeReglement =new LinkedList<>(); 
			List<TaRReglement> listeAcompte =new LinkedList<>();
			List<TaRAvoir> listeAvoir =new LinkedList<>();
			for (TaRReglement ligne : masterEntity.getTaRReglements()) {
				if(ligne.getTaReglement().getTaAcompte()==null)
					listeReglement.add(ligne);
				else
					listeAcompte.add(ligne);
			}
			
			mapEdition.put("listeReglement", listeReglement);
			mapEdition.put("listeAcompte", listeAcompte);
			mapEdition.put("listeAvoir", masterEntity.getTaRAvoirs());
			
			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("facture");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			
			mapParametreEdition.put("Theme", "defaultTheme");
			mapParametreEdition.put("Librairie", "bdgFactTheme1");
			
			if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
				mapParametreEdition.put("mode", modeNonRegle);
			} else {
				mapParametreEdition.put("mode", modeRegle);
			}
			
			mapEdition.put("param", mapParametreEdition);

			sessionMap.put("edition", mapEdition);
			////////////////////////////////////////////////////////////////////////////////////////
			//Test génération de fichier PDF
			
			//HashMap<String,Object> hm = new HashMap<>();
//			mapEdition.put("doc",doc );
//
//			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
//
//			mapEdition.put("lignes", masterEntity.getLignes());
//
//			mapEdition.put("taRReglement", masterEntity.getTaRReglements());
			
			//hm.put("edition", mapEdition);

//			mapParametreEdition.put("preferences", liste);
//			mapParametreEdition.put("gestionLot", gestionLot);
//			hm.put("param", mapParametreEdition);

//			BirtUtil.setAppContextEdition(hm);
			//taFactureService.generePDF(selectedDocumentDTO.getId());
			////////////////////////////////////////////////////////////////////////////////////////
			
			if(editionClient || impressionDirectClient) {
				//Mise à jour de la mise à dispostion
				if(masterEntity.getTaMiseADisposition()==null) {
					masterEntity.setTaMiseADisposition(new TaMiseADisposition());
				}
				Date dateMiseADispositionClient = new Date();
				masterEntity.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
				masterEntity = taFactureService.mergeAndFindById(masterEntity,ITaFactureServiceRemote.validationContext);
				updateTab();
			}

			//			session.setAttribute("tiers", taTiersService.findById(selectedDocumentDTO.getId()));
			System.out.println("FactureController.actImprimer()");
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}    
	
	public void actImprimerListeDesFactures(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesFactures", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public void actImprimer(ActionEvent actionEvent) {
		actImprimerGlobal(actionEvent,"CLIENT","CLIENTAPAYER");
	}    
	
	public void actImprimerBrouillon(ActionEvent actionEvent) {
		actImprimerGlobal(actionEvent,"BROUILLON","BROUILLONAPAYER");
	}    
	
	public void actImprimerDuplicata(ActionEvent actionEvent) {
		actImprimerGlobal(actionEvent,"DUPLICATA","DUPLICATAPAYER");
	} 
	
	public void actImprimerPayer(ActionEvent actionEvent) {
		actImprimerGlobal(actionEvent,"PAYER","PAYER");
	}

	public void actSelectedEdition() {
		
	}
	
	// *******************************************************************************
	//			THEME IMPRESSION
	public void theme1() {
		
		setTheme("defaultTheme");
		setLibrairie("bdgFactTheme1");
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		sessionMap.put("Theme", getTheme());
		sessionMap.put("Librairie", getLibrairie());
	}
	
	public void theme2() {
		setTheme("theme1");
		setLibrairie("bdgFactTheme1");
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		sessionMap.put("Theme", getTheme());
		sessionMap.put("Librairie", getLibrairie());
	}
	
	public void theme3() {
		setTheme("theme2");
		setLibrairie("bdgFactTheme1");
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
	
		sessionMap.put("Theme", getTheme());
		sessionMap.put("Librairie", getLibrairie());
	}
	// *******************************************************************************

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FACTURE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void updateReglements(){
		listeTaRReglementDTOJSF.clear();
		for (TaRReglement ligne : masterEntity.getTaRReglements()) {
			TaReglementDTOJSF obj = new TaReglementDTOJSF();
			obj=remplirIHMReglement(ligne.getTaReglement(), masterEntity);
			obj.getDto().setEtat(ligne.getEtat());
			listeTaRReglementDTOJSF.add(obj);
		}
		for (TaRAvoir ligne : masterEntity.getTaRAvoirs()) {
			TaReglementDTOJSF obj = new TaReglementDTOJSF();
			obj=remplirIHMAvoir(ligne.getTaAvoir(), masterEntity);
			obj.getDto().setEtat(ligne.getEtat());
			listeTaRReglementDTOJSF.add(obj);
		}

		if(!masterEntity.multiReglementReel()){
			for (TaRReglement ligne : masterEntity.getTaRReglements()) {
				masterEntity.setTaRReglement(ligne);
				selectedDocumentDTO.setMontantReglement(ligne.getAffectation());
				if(ligne.getTaReglement()!=null){
					selectedDocumentDTO.setLibelleReglement(ligne.getTaReglement().getLibelleDocument());
				}
			}
			libelleMultipleReglement=MESSAGE_SIMPLE_REGLEMENT;
		} else libelleMultipleReglement=MESSAGE_MULTIPLE_REGLEMENT;
		
		//interroger pour indiquer s'il y a des règlements ou avoirs non totalement affectés
		countAvoirsAndReglements =taAvoirService.selectCountDisponible(masterEntity.getTaTiers())+taReglementService.selectCountDisponible(masterEntity.getTaTiers());
		if(countAvoirsAndReglements>0)libelleReglementEnAttente="* Règlement ou avoir en attente";
	 else libelleReglementEnAttente="";
	}
	
	public void updateTab(){
		try {	

			super.updateTab();
			autorisationLiaisonComplete(true);
			
			selectedEtatDocument=null;
			if(masterEntity!=null && masterEntity.getTaREtat()!=null)
				selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			
			masterEntity.calculeTvaEtTotaux(); // pour que tous les totaux en transient soient remplis aussi
			valuesLigne = IHMmodel();
			
			masterEntity.calculeTvaEtTotaux();
			
			selectedDocumentDTO.setRegleDocument(masterEntity.getRegleCompletDocument());
			selectedDocumentDTO.setResteAReglerComplet(masterEntity.getResteAReglerComplet());
			updateReglements();
			docEnregistre=true;
			verifSiDifferenceReglementForImpression();
			autoCompleteMapDTOtoUI();
			if(masterEntity.getTaRReglement()!=null){
				differenceReglementResteARegle=masterEntity.getTaRReglement().getAffectation().compareTo(masterEntity.getNetTtcCalc())!=0;
			}
			//gestionLot = masterEntity.getGestionLot();

			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", 
						"Chargement de la Facture N°"+selectedDocumentDTO.getCodeTiers()
						)); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public TaReglementDTOJSF remplirIHMAvoir(TaAvoir avoir,IDocumentTiers taDocument){
		TaReglementDTOJSF ihmReglement = new TaReglementDTOJSF();
		ihmReglement.getDto().setId(avoir.getIdDocument());
		ihmReglement.getDto().setCodeDocument(avoir.getCodeDocument());
		ihmReglement.getDto().setTypeDocument(TaAvoir.TYPE_DOC);
		ihmReglement.setTypeDocument(TaAvoir.TYPE_DOC);
		ihmReglement.getDto().setDateDocument(avoir.getDateDocument());
		ihmReglement.getDto().setDateLivDocument(avoir.getDateLivDocument());
		if(avoir.getTaTPaiement()!=null){
			ihmReglement.getDto().setIdTPaiement(avoir.getTaTPaiement().getIdTPaiement());
			ihmReglement.getDto().setCodeTPaiement(avoir.getTaTPaiement().getCodeTPaiement());
		}
		ihmReglement.getDto().setEtat(avoir.getEtatDeSuppression());
		ihmReglement.getDto().setDateExport(avoir.getDateExport());
		ihmReglement.getDto().setLibelleDocument(avoir.getLibelleDocument());

		ihmReglement.getDto().setAffectation(avoir.calculAffectationEnCoursReel((TaFacture) taDocument));
		ihmReglement.getDto().setNetTtcCalc(avoir.getNetTtcCalc());

		ihmReglement.getDto().setMulti(avoir.getTaRAvoirs().size()>1);
		ihmReglement.getDto().setResteAAffecter(avoir.getResteAAffecter());
		return ihmReglement;		

	}

	public boolean reglementEstDirect(TaReglementDTOJSF rReglement){
		if(rReglement!=null && rReglement.getDto()!=null)
		  return((rReglement.getDto().getEtat()&IHMEtat.integre)!=0 );
		return false;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FACTURE);
		b.setTitre("Facture");
		b.setTemplate("documents/facture.xhtml");
		b.setIdTab(LgrTab.ID_TAB_FACTURE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_FACTURE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);

		scrollToTop();
		updateTab();
	} 

	public void validateLignesDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String msg = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			validateUIField(nomChamp,value);
			TaLFactureDTO dtoTemp =new TaLFactureDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taLFactureService.validateDTOProperty(dtoTemp, nomChamp, ITaLFactureServiceRemote.validationContext );

			//selectedDocumentDTO.setAdresse1Adresse("abcd");

			//		  if(selectedRadio.equals("aa")) {
			//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//		  }

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
//	public void controleDate(SelectEvent event) {
//		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
//		validateUIField(nomChamp, event.getObject());
//		
//	}
	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)||nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)){
				if(value==null || value.equals(""))value=BigDecimal.ZERO;
			}

			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaFactureDTO>> violations = factory.getValidator().validateValue(TaFactureDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaFactureDTO> cv : violations) {
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
			//taBonReceptionService.validateDTOProperty(selectedDocumentDTO,Const.C_CODE_TIERS,  ITaFactureServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	
	public void validateLigneDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {

			if(enabledUniteSaisie() && afficheUniteSaisie && selectedLigneJSF.getTaCoefficientUniteSaisie()!=null)
				validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());
				else
					if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
						if(!calculCoherenceAffectationCoefficientQte2(selectedLigneJSF.getDto().getQte2LDocument())) {
//							 PrimeFaces.current().executeScript("PF('wVdialogQte2').show()");
//							 PrimeFaces.current().dialog().openDynamic("PF('wVdialogQte2').show()",);
							//remplir variable ligneAReenregistrer
							setLigneAReenregistrer(selectedLigneJSF);
							String nomDialog="wVdialogQte2"+nomDocument;
//							PrimeFaces.current().executeScript("PF('"+nomDialog+"').show()");
							 PrimeFaces.current().executeScript("PF('wVdialogQte2Facture').show()");
//							actDialogCoherenceQte2(selectedLigneJSF);
						}

					}

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	

	
	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity = null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
						//						entity=(TaTiers) value;
						entity = taTiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taTiersService.findByCode((String)value);
					}

					changement=!entity.equalsCode(masterEntity.getTaTiers());


				}
				masterEntity.setTaTiers(entity);
				if(changement){
					String nomTiers=masterEntity.getTaTiers().getNomTiers();
					((TaFactureDTO)selectedDocumentDTO).setLibelleDocument("Facture N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);
					
					if(!disableTtc() && !disableTtcSiDocSansTVA()){
						masterEntity.setTtc(entity.getTtcTiers());
						((TaFactureDTO)selectedDocumentDTO).setTtc(LibConversion.intToBoolean(masterEntity.getTtc()));
					}									
					changementTiers(true);
				}
			} else if(nomChamp.equals(Const.C_NUM_LOT)) {
				selectedLigneJSF.getDto().setEmplacement(null);
				if( selectedLigneJSF.getArticleLotEntrepotDTO().getEmplacement()!=null 
						&& !selectedLigneJSF.getArticleLotEntrepotDTO().getEmplacement().equals("")) {
					selectedLigneJSF.getDto().setEmplacement(selectedLigneJSF.getArticleLotEntrepotDTO().getEmplacement());
				}
				selectedLigneJSF.getDto().setCodeEntrepot(selectedLigneJSF.getArticleLotEntrepotDTO().getCodeEntrepot());
				TaEntrepot entrepot =null;
				entrepot = taEntrepotService.findByCode(selectedLigneJSF.getArticleLotEntrepotDTO().getCodeEntrepot());
				selectedLigneJSF.setTaEntrepot(entrepot);

				TaLot lot =null;
				lot = taLotService.findByCode(selectedLigneJSF.getArticleLotEntrepotDTO().getNumLot());
				selectedLigneJSF.setTaLot(lot);
				if(lot!=null) {
					selectedLigneJSF.getDto().setLibLDocument(lot.getLibelle());
					masterEntityLigne.setLibLDocument(lot.getLibelle());
				}

			}else if(nomChamp.equals(Const.C_DATE_DOCUMENT)) {				
				dateDansPeriode((Date)value,nomChamp);

			}else if(nomChamp.equals(Const.C_DATE_ECH_DOCUMENT)) {				
				dateDansPeriode((Date)value,nomChamp);

			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = null;
				TaPrix prix =null;
				boolean changementArticleLigne = false;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCodeAvecLazy(((TaArticleDTO) value).getCodeArticle(),gestionCapsules);
					}else{
						entity = taArticleService.findByCodeAvecLazy((String)value,gestionCapsules);
					}
				}
				if(entity!=null) {
					 prix = entity.chercheTarif(masterEntity.getTaTiers());
					 if(prix==null)prix=new TaPrix(BigDecimal.ZERO,BigDecimal.ZERO);
				}
				if(selectedLigneJSF.getTaArticle()== null || selectedLigneJSF.getTaArticle().getIdArticle()!=entity.getIdArticle()) {
					changementArticleLigne = true;
					selectedLigneJSF.setTaArticle(entity);
				}

				if(changementArticleLigne) {
					if(entity!=null) selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
					
					selectedLigneJSF.setTaUniteSaisie(null);
					selectedLigneJSF.getDto().setUSaisieLDocument(null);
					selectedLigneJSF.getDto().setQteSaisieLDocument(BigDecimal.ZERO);
					selectedLigneJSF.setTaCoefficientUniteSaisie(null);

					selectedLigneJSF.setTaLot(null);
					selectedLigneJSF.setTaEntrepot(null);
					selectedLigneJSF.getDto().setCodeEntrepot(null);
					selectedLigneJSF.getDto().setNumLot(null);

					
					if(!masterEntity.getGestionLot() || (selectedLigneJSF.getTaArticle()!=null && !selectedLigneJSF.getTaArticle().getGestionLot())) {
						//utiliser un lot virtuel
						if(selectedLigneJSF.getTaLot()==null ){								
							listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
							listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(selectedLigneJSF.getTaArticle().getCodeArticle(),false);
							if(listArticleLotEntrepot!=null && listArticleLotEntrepot.size()>0) {
								ArticleLotEntrepotDTO lot=listArticleLotEntrepot.get(0);
								selectedLigneJSF.setTaLot(taLotService.findByCode(lot.getNumLot()));
								if(lot.getIdEntrepot()!=null)selectedLigneJSF.setTaEntrepot(taEntrepotService.findById(lot.getIdEntrepot()));
//								if(lot.getEmplacement()!=null && !lot.getEmplacement().isEmpty())selectedLigneJSF.getDto().setEmplacementLDocument(lot.getEmplacement());
							}else {
								selectedLigneJSF.setTaLot(taLotService.findOrCreateLotVirtuelArticle(selectedLigneJSF.getTaArticle().getIdArticle()));
							}
						}
					}
					
				}

				selectedLigneJSF.setTaUnite1(null);
				selectedLigneJSF.setTaUnite2(null);
				selectedLigneJSF.getDto().setU2LDocument(null);
				selectedLigneJSF.getDto().setQte2LDocument(BigDecimal.ZERO);
				TaRapportUnite rapport = null;
				if(entity!=null) rapport=entity.getTaRapportUnite();
				TaCoefficientUnite coef = null;
				if(rapport!=null){
					if(rapport.getTaUnite2()!=null) {
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
				
				if(entity!=null && entity.getTaTva()!=null && masterEntity.isGestionTVA()){
					selectedLigneJSF.getDto().setCodeTvaLDocument(entity.getTaTva().getCodeTva());
					selectedLigneJSF.getDto().setTauxTvaLDocument(entity.getTaTva().getTauxTva());
				}else {
					selectedLigneJSF.getDto().setCodeTvaLDocument(null);
					selectedLigneJSF.getDto().setTauxTvaLDocument(null);
					
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
				}else {
					selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
					selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				}
				
				
				
				if(entity!=null && entity.getTaRTaTitreTransport()!=null) {
					BigDecimal qteCrd = BigDecimal.ZERO;	
					selectedLigneJSF.setTaTitreTransport(entity.getTaRTaTitreTransport().getTaTitreTransport());
						if(entity.getTaRTaTitreTransport().getQteTitreTransport()!=null )
							qteCrd=entity.getTaRTaTitreTransport().getQteTitreTransport();
						selectedLigneJSF.getDto().setQteTitreTransport(selectedLigneJSF.getDto().getQteLDocument().multiply(qteCrd));
				} else {
					selectedLigneJSF.setTaTitreTransport(null);
					selectedLigneJSF.getDto().setQteTitreTransport(BigDecimal.ZERO);
				}
				
				masterEntityLigne.setTauxTvaLDocument(selectedLigneJSF.getDto().getTauxTvaLDocument());
				masterEntityLigne.setPrixULDocument(selectedLigneJSF.getDto().getPrixULDocument());
				masterEntityLigne.calculMontant();
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				if(entity!=null && entity.getTaUniteSaisie()!=null && selectedDocumentDTO.getUtiliseUniteSaisie()) {
					validateUIField(Const.C_QTE_SAISIE_L_DOCUMENT, selectedLigneJSF.getDto().getQteSaisieLDocument());	
				}


			}else if(nomChamp.equals(Const.C_PRIX_U_L_DOCUMENT)) {
				BigDecimal prix=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						prix=(BigDecimal)value;
					}}				
				masterEntityLigne.setPrixULDocument(prix);
				masterEntityLigne.calculMontant();
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				
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
//					masterEntityLigne.setQteLDocument(qte);
//					selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
//					selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
				
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
					}else  {
						selectedLigneJSF.getDto().setQte2LDocument(BigDecimal.ZERO);
						masterEntityLigne.setQte2LDocument(null);
					}

				} else {
					masterEntityLigne.setQte2LDocument(null);
				}
				if(selectedLigneJSF.getTaTitreTransport()!=null) {
					BigDecimal qteCrd =BigDecimal.ZERO;
					if(selectedLigneJSF.getTaArticle().getTaRTaTitreTransport().getQteTitreTransport()!=null )
						qteCrd=selectedLigneJSF.getTaArticle().getTaRTaTitreTransport().getQteTitreTransport();
					selectedLigneJSF.getDto().setQteTitreTransport(qte.multiply(qteCrd));
				} else {
					selectedLigneJSF.getDto().setQteTitreTransport(BigDecimal.ZERO);
				}
				masterEntityLigne.setQteLDocument(qte);
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
//				if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
//					PrimeFaces.current().focus("@widgetVar(wvQte2LFacture)");
//					validateUIField(Const.C_QTE2_L_DOCUMENT, selectedLigneJSF.getDto().getQte2LDocument());
//				}

			} else if(nomChamp.equals(Const.C_U1_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null){
					if(value instanceof TaUnite){
						entity=(TaUnite) value;
						entity = taUniteService.findByCode(entity.getCodeUnite());
					}else{
						entity = taUniteService.findByCode((String)value);
					}
					selectedLigneJSF.getDto().setU1LDocument(entity.getCodeUnite());
					masterEntityLigne.setU1LDocument(entity.getCodeUnite());
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

			}else if(nomChamp.equals(Const.C_U2_L_DOCUMENT)) {
				TaUnite entity =null;
				if(value!=null && !value.equals("")){
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
					selectedLigneJSF.getDto().setQte2LDocument(null);	
				}
				selectedLigneJSF.setTaCoefficientUnite(recupCoefficientUnite(selectedLigneJSF.getDto().getU1LDocument(),selectedLigneJSF.getDto().getU2LDocument()));
				validateUIField(Const.C_QTE_L_DOCUMENT, selectedLigneJSF.getDto().getQteLDocument());
			} else if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
				TaTPaiement entity = null;
				if(value!=null){
					if(value instanceof TaTPaiement){
						entity = (TaTPaiement) value;
					}else{
						entity = taTPaiementService.findByCode((String)value);
					}
				}
				if(entity!=null && masterEntity.getTaTPaiement()!=null){
					if(entity.getCodeTPaiement()!=null) {
						changement=!entity.getCodeTPaiement().equals(masterEntity.getTaTPaiement().getCodeTPaiement());
					}
				}
				masterEntity.setTaTPaiement(entity);
				taTPaiement=entity;
				if(changement || masterEntity.getTaRReglement()==null) {
					actInitReglement();
				}
			}else if(nomChamp.equals(Const.C_LIBELLE_REGLEMENT)) {
				if(masterEntity.getTaRReglement()!=null && masterEntity.getTaRReglement().getTaReglement()!=null) {
					if(value!=null)
						masterEntity.getTaRReglement().getTaReglement().setLibelleDocument((String) value);
					else masterEntity.getTaRReglement().getTaReglement().setLibelleDocument(null);
				}
				
			}else if(nomChamp.equals(Const.C_MONTANT_REGLEMENT)) {
				
			} else if(nomChamp.equals(Const.C_CODE_T_TVA_DOC)) {
				if(value!=null){
					if(value instanceof TaTTvaDoc){
						selectedDocumentDTO.setCodeTTvaDoc(((TaTTvaDoc) value).getCodeTTvaDoc());
					}else if(value instanceof String){
						selectedDocumentDTO.setCodeTTvaDoc((String) value);
					}
				}
				initLocalisationTVA();
				//si TTC est vrai et que codeTvaDoc différent de France alors on remets TTC à faux
				//car la saisie dans ce cas là doit se faire sur le HT
				if(selectedDocumentDTO.getTtc() && disableTtcSiDocSansTVA()) {
					selectedDocumentDTO.setTtc(false);
					validateUIField(Const.C_TTC, selectedDocumentDTO.getTtc());
				}
			} else if(nomChamp.equals(Const.C_TTC)) {
				masterEntity.setTtc(LibConversion.booleanToInt(selectedDocumentDTO.getTtc()));

			}
			
			else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {
				TaCPaiement entity = null;
				if(value!=null){
//					entity = (TaCPaiement) value;
					if(value instanceof TaCPaiement){
						entity = (TaCPaiement) value;
					}else{
						entity = taCPaiementService.findByCode((String)value);
					}
				}
				setTaCPaiementDoc(entity);
				if(entity!=null) {
					selectedCPaiement.setCodeCPaiement(entity.getCodeCPaiement());
					selectedCPaiement.setFinMoisCPaiement(entity.getFinMoisCPaiement());
					selectedCPaiement.setReportCPaiement(entity.getReportCPaiement());
					selectedCPaiement.setLibCPaiement(entity.getLibCPaiement());
				}			
			} else if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {
				TaTitreTransport entity = null;
				if(value!=null){
					entity = (TaTitreTransport) value;
				}
				if(entity!=null) {
					masterEntityLigne.setCodeTitreTransport(entity.getCodeTitreTransport());
					selectedLigneJSF.setTaTitreTransport(entity);
					selectedLigneJSF.setTaTitreTransport(entity);
				}else {
					masterEntityLigne.setCodeTitreTransport(null);
					masterEntityLigne.setQteTitreTransport(null);
					selectedLigneJSF.setTaTitreTransport(entity);
				}
			} 
			else	if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)) {
				BigDecimal tx=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						tx=(BigDecimal)value;
					}
					masterEntity.setTxRemHtDocument(tx);
					mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				}
			} else	if(nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)) {
				BigDecimal tx=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						tx=(BigDecimal)value;
					}
					masterEntity.setTxRemTtcDocument(tx);
					mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				}
			} else	if(nomChamp.equals(Const.C_REM_TX_L_DOCUMENT)) {
				BigDecimal tx=BigDecimal.ZERO;
				if(value!=null){
					if(!value.equals("")){
						tx=(BigDecimal)value;
					}
				}
				masterEntityLigne.setRemTxLDocument(tx);
				selectedLigneJSF.getDto().setMtHtLDocument(masterEntityLigne.getMtHtLDocument());
				selectedLigneJSF.getDto().setMtTtcLDocument(masterEntityLigne.getMtTtcLDocument());
			}else	if(nomChamp.equals(Const.C_EXPORT)) {
			}			
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void initialisationDesInfosComplementaires(Boolean recharger,String typeARecharger){
		/*
		 * verifier que le type d'adresse existe
		 * verifier que le tiers à des adresses de ce tpe
		 * remplir les maps et changer les clauses where des DAO de modeles
		 * 
		 * remplir les modèles 
		 * avoir dans l'ordre :
		 * - adresse de l'infos facture si elle existe
		 * - adresse du type demandé s'il y en a
		 * - le reste des adresses du tiers
		 */

		try {
			if(((IDocumentDTO)selectedDocumentDTO).getCodeDocument()!=null) {
				taInfosDocument = taInfosFactureService.findByCodeFacture(((IDocumentDTO)selectedDocumentDTO).getCodeDocument());
			} else {
				taInfosDocument = new TaInfosFacture();
			}

			if(taInfosDocument!=null && taInfosDocument.getNomTiers()!=null)selectedDocumentDTO.setNomTiers(taInfosDocument.getNomTiers());
			if(taInfosDocument!=null && taInfosDocument.getCodeTTvaDoc()!=null)selectedDocumentDTO.setCodeTTvaDoc(taInfosDocument.getCodeTTvaDoc());
			if(recharger) {				
				if(masterEntity.getTaTiers()!=null){
					masterEntity.setTaTiers(taTiersService.findById(masterEntity.getTaTiers().getIdTiers()));
					selectedDocumentDTO.setCodeTiers(masterEntity.getTaTiers().getCodeTiers());
					selectedDocumentDTO.setNomTiers(masterEntity.getTaTiers().getNomTiers());
					selectedDocumentDTO.setPrenomTiers(masterEntity.getTaTiers().getPrenomTiers());
					selectedDocumentDTO.setNomEntreprise("");
					if(masterEntity.getTaTiers().getTaEntreprise()!=null)
						selectedDocumentDTO.setNomEntreprise(masterEntity.getTaTiers().getTaEntreprise().getNomEntreprise());
					
					if (masterEntity.getTaTiers().getTaTTvaDoc()!=null){
						selectedDocumentDTO.setCodeTTvaDoc(masterEntity.getTaTiers().getTaTTvaDoc().getCodeTTvaDoc());
					}
				}
			}

			if(typeARecharger==Const.RECHARGE_ADR_FACT||typeARecharger=="")
				initialisationDesAdrFact(recharger);
			if(typeARecharger==Const.RECHARGE_ADR_LIV||typeARecharger=="")
				initialisationDesAdrLiv(recharger);
			if(typeARecharger==Const.RECHARGE_C_PAIEMENT||typeARecharger=="")
				initialisationDesCPaiement(recharger);
			if(typeARecharger==Const.RECHARGE_INFOS_TIERS||typeARecharger=="")
				initialisationDesInfosTiers(recharger);
		}  catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void onClearTaTitreTransport(AjaxBehaviorEvent event){
		selectedLigneJSF.getDto().setCodeTitreTransport(null);
		selectedLigneJSF.getDto().setQteTitreTransport(null);
		selectedLigneJSF.setTaTitreTransport(null);
		masterEntityLigne.setCodeTitreTransport(null);
		masterEntityLigne.setQteTitreTransport(null);
	}

	public void initialisationDesCPaiement(Boolean recharger) {
		LinkedList<TaCPaiementDTO> liste = new LinkedList<TaCPaiementDTO>();
		try {

			TaInfosFacture taInfosDocument = null;
			if (masterEntity != null) {
				if (masterEntity.getCodeDocument()!=null&& masterEntity.getCodeDocument() != "")
					taInfosDocument = taInfosFactureService.findByCodeFacture(masterEntity.getCodeDocument());
				else
					taInfosDocument = new TaInfosFacture();


				taCPaiementDoc = null;
				List<TaCPaiement> listeCPaiement=taCPaiementService.rechercheParType(TaFacture.TYPE_DOC);
				if(listeCPaiement!=null && !listeCPaiement.isEmpty())taCPaiementDoc=listeCPaiement.get(0);
				
				if (taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_FACTURE) != null
						&& taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_FACTURE).getTaCPaiement() != null) {
					taCPaiementDoc = taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_FACTURE).getTaCPaiement();
				}
				int report = 0;
				int finDeMois = 0;

				TaTPaiement taTPaiementReglement = new TaTPaiement();
				if (masterEntity.reglementExiste()&& masterEntity.getTaRReglement().getTaReglement()
						.getTaTPaiement() != null) {
					taTPaiementReglement = masterEntity.getTaRReglement().getTaReglement().getTaTPaiement();
				}

				if (taTPaiementReglement != null
						&& ((taTPaiementReglement.getReportTPaiement() != null && taTPaiementReglement.getReportTPaiement() != 0) || 
								(taTPaiementReglement.getFinMoisTPaiement() != null && taTPaiementReglement.getFinMoisTPaiement() != 0))) {
					TaCPaiementDTO ihm = new TaCPaiementDTO();
					ihm.setReportCPaiement(taTPaiementReglement.getReportTPaiement());
					ihm.setFinMoisCPaiement(taTPaiementReglement.getFinMoisTPaiement());
					liste.add(ihm);
				} else {


					if (masterEntity.getTaTiers() != null) {
						if (masterEntity.getTaTiers().getTaTPaiement() != null) {
							if (masterEntity.getTaTiers().getTaTPaiement().getReportTPaiement() != null)
								report = masterEntity.getTaTiers().getTaTPaiement().getReportTPaiement();
							if (masterEntity.getTaTiers().getTaTPaiement().getFinMoisTPaiement() != null)
								finDeMois = masterEntity.getTaTiers().getTaTPaiement().getFinMoisTPaiement();
						}

						if (masterEntity.getTaTiers().getTaCPaiement() == null
								|| (report != 0 || finDeMois != 0)) {
							if (taCPaiementDoc == null
									|| (report != 0 || finDeMois != 0)) {// alors on
								// met
								// au
								// moins
								// une
								// condition
								// vide
								TaCPaiementDTO ihm = new TaCPaiementDTO();
								ihm.setReportCPaiement(report);
								ihm.setFinMoisCPaiement(finDeMois);
								liste.add(ihm);
							}
						} else
							liste.add(
									mapperModelToUICPaiementInfosDocument.map(
											masterEntity.getTaTiers()
											.getTaCPaiement(),
											classModelCPaiement));
					}
				}

				if (taCPaiementDoc != null  )
					liste.add(mapperModelToUICPaiementInfosDocument.map(taCPaiementDoc,classModelCPaiement));
				masterEntity.setTaInfosDocument(taInfosDocument);
				// ajout de l'adresse de livraison inscrite dans l'infos facture
				if (taInfosDocument != null) {
					if (recharger)
						liste.add(
								mapperModelToUIInfosDocVersCPaiement.map(masterEntity.getTaInfosDocument(),classModelCPaiement));
					else
						liste.addFirst(
								mapperModelToUIInfosDocVersCPaiement.map(masterEntity.getTaInfosDocument(),classModelCPaiement));
				}
			}
			if (!liste.isEmpty()) {
				((TaCPaiementDTO) selectedCPaiement)
				.setSWTCPaiement(liste.getFirst());
			} else {
				((TaCPaiementDTO) selectedCPaiement)
				.setSWTCPaiement(new TaCPaiementDTO());
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void calculDateEcheance(Boolean appliquer) {
		if(selectedDocumentDTO!=null){
			if(((IDocumentDTO)selectedDocumentDTO).getId()==0|| appliquer) {

				Integer report = null;
				Integer finDeMois = null;
				if(((TaCPaiementDTO)selectedCPaiement)!=null /*&& ((TaCPaiementDTO)selectedCPaiement).getCodeCPaiement()!=null*/) { 
					if(((TaCPaiementDTO)selectedCPaiement).getReportCPaiement()!=null)
						report = ((TaCPaiementDTO)selectedCPaiement).getReportCPaiement();
					if(((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement()!=null)
						finDeMois = ((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement();
				}
				masterEntity.setDateDocument(selectedDocumentDTO.getDateDocument());
				((TaFactureDTO)selectedDocumentDTO).setDateEchDocument(taFactureService.calculDateEcheance(masterEntity,report, finDeMois,taTPaiement));
			}
		}
	}

	//	public void actReinitCPaiement() throws Exception {
	//		initialisationDesInfosComplementaires(true,Const.RECHARGE_C_PAIEMENT);
	//		calculDateEcheance(true);
	//		actModifier();
	//	}

	public void actInitReglement(){
		if(factureReglee){

			TaReglement reglement=null;
			for (TaRReglement ligne : masterEntity.getTaRReglements()) {
				masterEntity.setTaRReglement(ligne);
				reglement=ligne.getTaReglement();
			}
			
			if(reglement==null){
				reglement=new TaReglement();
				if(masterEntity.getTaRReglement()==null)masterEntity.setTaRReglement(new TaRReglement());
				masterEntity.getTaRReglement().setTaReglement(reglement);
				reglement.addRReglement(masterEntity.getTaRReglement());
			}
			reglement.setTaTiers(masterEntity.getTaTiers());
			reglement.setTaTPaiement(taTPaiement);


			if(taTPaiement==null){

				if(masterEntity.getTaTiers()!=null && masterEntity.getTaTiers().getTaTPaiement()!=null ){
					taTPaiement=masterEntity.getTaTiers().getTaTPaiement();
				}

				else {
					if (typePaiementDefaut == null || typePaiementDefaut=="")
						typePaiementDefaut="C";
					try {
						taTPaiement = taTPaiementService.findByCode(typePaiementDefaut);

					} catch (Exception e) {
					}
				}
			}
			reglement.setTaTPaiement(taTPaiement);
			reglement.setDateDocument(masterEntity.getDateDocument());
			reglement.setDateLivDocument(masterEntity.getDateDocument());
			reglement.setDateEchDocument(masterEntity.getDateDocument());
			reglement.setDateExport(null);
			reglement.setTaCompteBanque(taCompteBanqueService.findByTiersEntreprise(taTPaiement));
			reglement.setLibelleDocument(taTPaiement.getLibTPaiement());
			BigDecimal reste=masterEntity.calculSommeReglementsComplet(masterEntity.getTaRReglement());
			reglement.setNetTtcCalc(masterEntity.getNetTtcCalc().subtract(reste));
			masterEntity.getTaRReglement().setAffectation(reglement.getNetTtcCalc());
			masterEntity.calculRegleDocument();
			masterEntity.setTaTPaiement(taTPaiement);
			selectedDocumentDTO.setMontantReglement(reglement.getNetTtcCalc());
			selectedDocumentDTO.setLibelleReglement(reglement.getLibelleDocument());


		}else{
			if(masterEntity.getTaRReglement()!=null)masterEntity.getTaRReglement().setEtatDeSuppression(IHMEtat.suppression);
			masterEntity.calculRegleDocument();
			selectedDocumentDTO.setMontantReglement(BigDecimal.ZERO);
			selectedDocumentDTO.setLibelleReglement("");
		}

		selectedDocumentDTO.setResteARegler(masterEntity.getResteARegler());
	}

	public void actAppliquerCPaiement() throws Exception {
		calculDateEcheance(true);
		taFactureService.mettreAJourDateEcheanceReglements(masterEntity);
		actModifier();
	}

	public void calculTypePaiement(boolean recharger) {
		if (masterEntity != null) {
			if (masterEntity.getTaTiers() != null
					&& masterEntity.getTaTiers().getTaTPaiement() != null
					&& masterEntity.getTaRReglement() != null
					&& masterEntity.getTaRReglement().getTaReglement() != null &&masterEntity.getTaRReglement().
					getTaReglement().getNetTtcCalc().signum()!=0 ) {
				masterEntity.getTaRReglement().getTaReglement().setTaTPaiement(
						masterEntity.getTaTiers().getTaTPaiement());
				masterEntity.getTaRReglement().getTaReglement()
				.setLibelleDocument(masterEntity.getTaTiers().getTaTPaiement().getLibTPaiement());
			} else {
				try {
					typePaiementDefaut = taPreferencesService.findByCode(TaTPaiement.getCodeTPaiementDefaut()).getValeur();
				} catch (FinderException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (typePaiementDefaut == null || typePaiementDefaut=="")
					typePaiementDefaut="C";
				TaTPaiement taTPaiement = null;
				try {
					taTPaiement = taTPaiementService
							.findByCode(typePaiementDefaut);

				} catch (Exception e) {
				}

				if(masterEntity.reglementExiste()&&masterEntity.getTaRReglement().
						getTaReglement().getNetTtcCalc().signum()!=0){
					masterEntity.getTaRReglement().getTaReglement()
					.setTaTPaiement(taTPaiement);
					masterEntity.getTaRReglement().getTaReglement()
					.setLibelleDocument(taTPaiement.getLibTPaiement());
					validateUIField(Const.C_CODE_T_PAIEMENT, taTPaiement.getCodeTPaiement());
				}else{
					if(masterEntity.getTaRReglement()!=null && masterEntity.getTaRReglement().getTaReglement()!=null){
						masterEntity.getTaRReglement().getTaReglement().setTaTPaiement(null);
						masterEntity.getTaRReglement().getTaReglement().setLibelleDocument(null);
					}
				}

			}
		}
		initialisationDesCPaiement(recharger);
	}
	/**
	 * Cette méthode est appellée une fois que le dialog d'envoi de mail se ferme
	 * elle met le document en miseADisposition envoyé par mail
	 */
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			//Mise à jour de la mise à dispostion
			if(masterEntity.getTaMiseADisposition()==null) {
				masterEntity.setTaMiseADisposition(new TaMiseADisposition());
			}
			Date dateMiseADispositionClient = new Date();
			
			masterEntity.getTaMiseADisposition().setEnvoyeParEmail(dateMiseADispositionClient);
			masterEntity = taFactureService.mergeAndFindById(masterEntity,ITaFactureServiceRemote.validationContext);
			updateTab();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					));  
		}
	}
	
	/**
	 * Méthode apparemment dépréciée (plus utilisée)
	 * @param actionEvent
	 */
	public void actEnvoyerParEmail(ActionEvent actionEvent) {	
		try {
			if(masterEntity.getTaTiers().getTaEmail()!=null 
					&& masterEntity.getTaTiers().getTaEmail().getAdresseEmail()!=null) {
				
//				lgrMailjetService.send(
//					null, 
//					taInfoEntrepriseService.findInstance().getNomInfoEntreprise(), 
//					taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument(), 
//					"Votre facture "+masterEntity.getCodeDocument(), 
//					"Votre facture "+masterEntity.getCodeDocument(),
//					new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()}, 
//					new File[]{new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument()))}
//					);
				
//				emailServiceFinder.sendAndLogEmailProgramme(null, 
//					taInfoEntrepriseService.findInstance().getNomInfoEntreprise(), 
//					taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument(), 
//					"Votre facture "+masterEntity.getCodeDocument(), 
//					"Votre facture "+masterEntity.getCodeDocument(),
//					new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()}, 
//					new File[]{new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument()))}
//					);
				
				List<String> l = new ArrayList<>();
				l.add(masterEntity.getTaTiers().getTaEmail().getAdresseEmail());
				
	//A REMETTRE CI DESSOUS			
//				emailServiceFinder.sendAndLogEmailDossier(
//						null,
//						null,
//						taInfoEntrepriseService.findInstance().getNomInfoEntreprise(), 
//						taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument(), 
//						"Votre facture "+masterEntity.getCodeDocument(), 
//						"Votre facture "+masterEntity.getCodeDocument(),
//						l,//new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()}, 
//						null,
//						null,
//						null,
//						null,
//						null,
//						null,
//						null,
//						null,
//						new File[]{new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument(),null, null, null))},
//						null,//replyTo
//						null
//						);
			
				//Mise à jour de la mise à dispostion
//				if(masterEntity.getTaMiseADisposition()==null) {
//					masterEntity.setTaMiseADisposition(new TaMiseADisposition());
//				}
//				Date dateMiseADispositionClient = new Date();
//				
//				if(envoyeParEmail) {
//					masterEntity.getTaMiseADisposition().setEnvoyeParEmail(dateMiseADispositionClient);
//				}
//				masterEntity = taFactureService.mergeAndFindById(masterEntity,ITaFactureServiceRemote.validationContext);
				updateTab();
				
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Facture", 
						"Facture N°"+selectedDocumentDTO.getCodeDocument()+" envoyée par email à : "+masterEntity.getTaTiers().getTaEmail().getAdresseEmail()
						)); 
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	

	
//	public void handleReturnDialogEmail(SelectEvent event) {
//		if(event!=null && event.getObject()!=null) {
//			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
//			TaMessageEmail j = (TaMessageEmail) event.getObject();
//			
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Email", 
//					"Email envoyée "
//					)); 
//		}
//	}
	/**
	 * Méthode apparemment dépréciée (plus utilisée)
	 * @param actionEvent
	 */
	public void actDialogImprimer(ActionEvent actionEvent) {
	    
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
		Map<String, Object> mapParametreEdition = null;
		
		if(actionEvent!=null){
			
			mapParametreEdition = new HashMap<>();
			boolean editionClient = false;	
			String modeEdition = (String)actionEvent.getComponent().getAttributes().get("mode_edition");
			String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
			editionClient = LibConversion.StringToBoolean(pourClient,false);
			mapParametreEdition.put("editionClient", editionClient);
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("facture");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			
//			public void actImprimer(ActionEvent actionEvent) {
//				actImprimerGlobal(actionEvent,"CLIENT","CLIENTAPAYER");
//			}    

			if(!editionClient) {
				switch (modeEdition) {
				case "BROUILLON":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "BROUILLONAPAYER");
					} else {
						mapParametreEdition.put("mode", "BROUILLON");
					}
					break;
				case "DUPLICATA":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "DUPLICATAPAYER");
					} else {
						mapParametreEdition.put("mode", "DUPLICATA");
					}
					break;
				case "PAYER":
					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
						mapParametreEdition.put("mode", "PAYER");
					} else {
						mapParametreEdition.put("mode", "PAYER");
					}
					break;
	
				default:
					break;
				}
			} else {
				if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
					mapParametreEdition.put("mode", "CLIENTAPAYER");
				} else {
					mapParametreEdition.put("mode", "CLIENT");
				}
			}
		}
		
		EditionParam edition = new EditionParam();
		edition.setIdActionInterne(ConstActionInterne.EDITION_FACTURE_DEFAUT);
		edition.setMapParametreEdition(mapParametreEdition);
		
		sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, edition);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_choix_edition", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	/**
	 * Méthode apparemment dépréciée (plus utilisée)
	 * @param actionEvent
	 */
	public void handleReturnDialogImprimer(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaEdition taEdition = (TaEdition) event.getObject();

			if(taEdition!=null) {
				System.out.println("ChoixEditionController.actImprimer() "+taEdition.getLibelleEdition());
				List<String> listeResourcesPath = null;
				if(taEdition.getResourcesPath()!=null) {
					listeResourcesPath = new ArrayList<>();
					listeResourcesPath.add(taEdition.getResourcesPath());
				}
				BdgProperties bdgProperties = new BdgProperties();
				String localPath = bdgProperties.osTempDirDossier(tenantInfo.getTenantId())+"/"+bdgProperties.tmpFileName(taEdition.getFichierNom());

				try { 
					Files.write(Paths.get(localPath), taEdition.getFichierBlob());
					
					AnalyseFileReport afr = new AnalyseFileReport();
					afr.initializeBuildDesignReportConfig(localPath);
					afr.ajouteLogo();
					afr.closeDesignReportConfig();
					

					String pdf = taFactureService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
					PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");
					masterEntity = taFactureService.findById(masterEntity.getIdDocument());
					updateTab();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


		}
	}

	public void handleReturnDialogGestionReglement(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
	 
		}
		if(docEnregistre)updateTab();
	}
	
	public void actDialogGenerationReglement(ActionEvent actionEvent) {
		try {	
			if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION)){
				//si pas en consultation, enregistrer la facture avant de rentrer dans la gestion des règlements
				actEnregistrer(null);
			}
			if(docEnregistre){
				Map<String,Object> options = new HashMap<String, Object>();
				options.put("modal", true);
				options.put("draggable", false);
				options.put("closable", false);
				options.put("resizable", true);
				options.put("contentHeight", 710);
				options.put("contentWidth", "100%");
				options.put("width", 1024);

				Map<String,Object> mapDialogue = new HashMap<String,Object>();

				mapDialogue.put("masterEntity",masterEntity );

				Map<String,List<String>> params = new HashMap<String,List<String>>();
				List<String> list = new ArrayList<String>();
				list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
				params.put("modeEcranDefaut", list);


				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("mapDialogue", mapDialogue);

				PrimeFaces.current().dialog().openDynamic("reglement/multiple/reglement_multiple_facture.xhtml", options, params);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public int getCountAvoirsAndReglements() {
		return countAvoirsAndReglements;
	}

	public void setCountAvoirsAndReglements(int countAvoirsAndReglements) {
		this.countAvoirsAndReglements = countAvoirsAndReglements;
	}

	public void onClearArticle(AjaxBehaviorEvent event){
		super.onClearArticle(event);
		selectedLigneJSF.setTaLot(null);
		selectedLigneJSF.getDto().setNumLot(null);
		if(selectedLigneJSF.getArticleLotEntrepotDTO()!=null)selectedLigneJSF.getArticleLotEntrepotDTO().setNumLot(null);
		
		masterEntityLigne.calculMontant();
		masterEntity.calculeTvaEtTotaux();
		validateUIField(Const.C_CODE_ARTICLE, null);
	}
	
	public void actInitPaiementCarteBancaire(ActionEvent actionEvent) {
		String CODE_TYPE_PAIEMENT_DEFAUT = "CB";
		TaPreferences prefCB;
		try {
			prefCB = taPreferencesService.findByCode("paiement_cb");
			if(prefCB!=null) CODE_TYPE_PAIEMENT_DEFAUT = prefCB.getValeur();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		typePaiementDefaut = CODE_TYPE_PAIEMENT_DEFAUT;
		
		
//		setFactureReglee(true);
		actInitReglement();
		calculDateEcheance(true);
		PaiementParam param = new PaiementParam();
		param.setDocumentPayableCB(masterEntity);
		param.setMontantPaiement(masterEntity.getNetTtcCalc());
		param.setOriginePaiement("BDG Facture");
		param.setMontantLibre(true);
		PaiementCbController.actDialoguePaiementEcheanceParCarte(param);
	}
	
	public void actInitPaiementGeneral(ActionEvent actionEvent) {
		
		
//		setFactureReglee(true);
		actInitReglement();
		calculDateEcheance(true);
		PaiementParam param = new PaiementParam();
		param.setDocumentPayableCB(masterEntity);
		param.setMontantPaiement(masterEntity.getNetTtcCalc());
		param.setOriginePaiement("BDG Facture");
		PaiementCbController.actDialoguePaiementEcheanceParCarte(param);
	}
	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		System.out.println("PaiementCbController.handleReturnDialoguePaiementEcheanceParCarte()");
		try {
//			if(event!=null && event.getObject()!=null) {
//				RetourPaiementCarteBancaire e = (RetourPaiementCarteBancaire) event.getObject();
//				
//				//taDossierRcd = taDossierRcdService.findById(taDossierRcd.getIdDossierRcd());
//				
//				for (TaEcheance taEcheance : taDossierRcd.getTaEcheances()) {
//					if(taEcheance.getIdEcheance()==selectedTaEcheanceDTO.getId()) {
//						taEcheance.setTaReglementAssurance(e.getTaReglementAssurance());
//						taEcheance.getTaReglementAssurance().setTaEcheance(taEcheance);
//					}
//				}
//				
//				actEnregistrer(null);
//				actActionApresPaiementAccepte();
//				listeTaEcheanceDTO = taEcheanceService.findAllEcheanceRCDIdDTO(taDossierRcd.getIdDossierRcd());
//			}
			setFactureReglee(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public OuvertureTiersBean getOuvertureTiersBean() {
		return ouvertureTiersBean;
	}

	public void setOuvertureTiersBean(OuvertureTiersBean ouvertureTiersBean) {
		this.ouvertureTiersBean = ouvertureTiersBean;
	}

	public OuvertureArticleBean getOuvertureArticleBean() {
		return ouvertureArticleBean;
	}

	public void setOuvertureArticleBean(OuvertureArticleBean ouvertureArticleBean) {
		this.ouvertureArticleBean = ouvertureArticleBean;
	}

//	public boolean isDuplicata() {
//		return duplicata;
//	}
//
//	public void setDuplicata(boolean duplicata) {
//		this.duplicata = duplicata;
//	}
//
//	public int getCompteurFacture() {
//		return compteurFacture;
//	}
//
//	public void setCompteurFacture(int compteurFacture) {
//		this.compteurFacture = compteurFacture;
//	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getLibrairie() {
		return librairie;
	}

	public void setLibrairie(String librairie) {
		this.librairie = librairie;
	}



	public boolean isDifferenceReglementResteARegleForPrint() {
		return differenceReglementResteARegleForPrint;
	}

	public void setDifferenceReglementResteARegleForPrint(boolean differenceReglementResteARegleForPrint) {
		this.differenceReglementResteARegleForPrint = differenceReglementResteARegleForPrint;
	}


	public void actInsererLigne(ActionEvent actionEvent) {
		super.actInsererLigne(actionEvent);

		TaEtat etat = taFactureService.etatLigneInsertion(masterEntity);
		masterEntityLigne.addREtatLigneDoc(etat);
		
		selectedLigneJSF.setTaREtatLigneDocument(masterEntityLigne.getTaREtatLigneDocument());
		if(etat!=null)selectedLigneJSF.getDto().setCodeEtat(etat.getCodeEtat());
//		selectedLigneJSF.getDto().setDatePrevue(selectedDocumentDTO.getDateLivDocument());
	}
	
	public boolean documentLie(boolean silencieux) {
		docLie=rechercheSiDocLie();
		return docLie!=null && docLie.size()>0;
	}
	
	public boolean ligneDocumentLie(boolean silencieux) {
		ligneLie=rechercheSiLigneDocLie(selectedLigneJSF);
		return ligneLie!=null && ligneLie.size()>0;
	}
	

//	public boolean isFactureCompletementRegle() {
//		return factureCompletementRegle;
//	}
//
//	public void setFactureCompletementRegle(boolean factureCompletementRegle) {
//		this.factureCompletementRegle = factureCompletementRegle;
//	}

//	public String getOptionImprimer() {
//		return optionImprimer;
//	}
//
//	public void setOptionImprimer(String optionImprimer) {
//		this.optionImprimer = optionImprimer;
//	}
//
//	public List<String> getOptionsImprimer() {
//		return optionsImprimer;
//	}
//
//	public void setOptionsImprimer(List<String> optionsImprimer) {
//		this.optionsImprimer = optionsImprimer;
//	}

	
//	public TaCoefficientUnite recupCoefficientUnite(String code1, String code2) {
//		TaCoefficientUnite coef=null;;
//		coef=taCoefficientUniteService.findByCode(code1,code2);
//		coef.recupRapportEtSens(code1,code2);
//		return coef;
//	}

//	public IDocumentTiers dejaGenereDocument(IDocumentTiers ds) {
//		IDocumentTiers dejaGenere = null;
//		String jpql = "select x.taBonliv from TaRDocument x where x.taFacture.idDocument="+ds.getIdDocument()+" and x.taBonliv is not null";
//		List<IDocumentTiers> l = taRDocumentService.dejaGenereDocument(jpql);
//		System.err.println(l.size());
//		if(l.size()>0) {
//			dejaGenere = l.get(0);
//		}
//		return dejaGenere;
//	}
//	public IDocumentTiers rechercheSiDocLie() {
//		return dejaGenereDocument(masterEntity);
//	}

	public RetourAutorisationLiaison autoriseSuppression() {
		return autoriseSuppression(false);
	}

//	public RetourAutorisationLiaison autoriseSuppression(boolean silencieux) {
//		RetourAutorisationLiaison retour=new RetourAutorisationLiaison();
//		TaRDocumentDTO suppressionDocInterdit=null;
//		messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT;
//	    messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT;
//		if(selectedDocumentDTO!=null && selectedDocumentDTO.getCodeDocument()!=null && !selectedDocumentDTO.getCodeDocument().equals("")) {
//			messageSuppression=Const.C_MESSAGE_SUPPRESSION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
//	        messageModification=Const.C_MESSAGE_MODIFIFCATION_DOCUMENT+" "+selectedDocumentDTO.getCodeDocument();
//		}
//		List<TaRDocumentDTO> docLie=rechercheSiDocLie();
//		String documents = "";
//		if(docLie!=null) {
//			PrimeFaces.current().ajax().addCallbackParam("Autorise la suppression", docLie!=null);
//			if(docLie!=null)
//				for (TaRDocumentDTO doc : docLie) {
//					if(!documents.equals("") && !doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument()))
//						documents=documents+";"+doc.getCodeDocumentDest();
//					else if(!doc.getCodeDocumentDest().equals(selectedDocumentDTO.getCodeDocument())) documents=doc.getCodeDocumentDest();
//					if(suppressionDocInterdit==null && doc.getTypeDocumentDest().equals(TaFacture.TYPE_DOC))
//						suppressionDocInterdit=doc;
//				}
//			if(!documents.equals("")) {
//				if(suppressionDocInterdit==null) {
//					messageSuppression="Suppression d'un document lié à un autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
//							+ "\r\nEtes-vous sur de vouloir le supprimer ?";
//		            messageModification="Modification d'un document lié à un autre : Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+documents+"."
//		                    + "\r\nEtes-vous sur de vouloir le modifier ?";
//				}
//				else {
//					messageSuppression="Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au document n° "+suppressionDocInterdit.getCodeDocumentDest()+"."
//							+ "\r\nSi vous souhaitez le supprimer, vous devez au préalable supprimer ce document.";
//		            messageModification="Le document "+selectedDocumentDTO.getCodeDocument()+" est lié au(x) document(s) n° "+suppressionDocInterdit.getCodeDocumentDest()+"."
//		            		+ "\r\nSi vous souhaitez le modifier, vous devez au préalable supprimer ce document.";
//					if(!silencieux)PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Suppression d'un document lié à un autre",messageSuppression ));
//				}
//			}
//		}
//		retour.autorise=suppressionDocInterdit==null;
//		return retour;
//	}
	
	public void actSupprimerLigne(ActionEvent actionEvent) {
		try {
			if(autorisationLiaisonComplete(false)) { 
				if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT)) {
					if(selectedLigneJSF.getDto().getIdLDocument() != null) {
//						//List<TaLEcheance> liste  = taLEcheanceService.findAllByIdLFacture(selectedLigneJSF.getDto().getIdLDocument());
//						List<TaLigneALigneEcheance> liste  = taLigneALigneEcheanceService.findAllByIdLDocumentAndTypeDoc(selectedLigneJSF.getDto().getIdLDocument(), TaFacture.TYPE_DOC);
//						if(liste != null && !liste.isEmpty()) {
//							for (TaLigneALigneEcheance li : liste) {
//								if(!listeLigneALigneEcheanceASupprimer.contains(li)) {
//									listeLigneALigneEcheanceASupprimer.add(li);
//								}
//								
//							}
//							
//						}
					}
				}
				
				super.actSupprimerLigne(actionEvent);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



	public String getMessageConfirmEnregistrement() {
		return messageConfirmEnregistrement;
	}

	public void setMessageConfirmEnregistrement(String messageConfirmEnregistrement) {
		this.messageConfirmEnregistrement = messageConfirmEnregistrement;
	}
	
	

//	public boolean calculCoherenceAffectationCoefficientQte2(BigDecimal qte2Actuelle) {
//		return calculAffectationCoefficientQte2(selectedLigneJSF).compareTo(qte2Actuelle)==0;	
//	}
//	
//	public BigDecimal  calculAffectationCoefficientQte2(TaLFactureDTOJSF ligne) {
//		BigDecimal qteCalculee =ligne.getDto().getQte2LDocument();
//		if(ligne==null)return null;
//		if(ligne.getTaCoefficientUnite()!=null) {
//			qteCalculee =ligne.getDto().getQteLDocument();
//			if(ligne.getTaCoefficientUnite().getOperateurDivise()) 
//				return (qteCalculee).divide(ligne.getTaCoefficientUnite().getCoeff()
//						,MathContext.DECIMAL128).setScale(ligne.getTaCoefficientUnite().getNbDecimale(),BigDecimal.ROUND_HALF_UP);
//			else return (qteCalculee).multiply(ligne.getTaCoefficientUnite().getCoeff());
//		}else  {
//			return qteCalculee;
//		}		
//	}
//	
//	public void actArreterEnregistrementEtFocusQte2() throws Exception {
//		//récupérer la ligne con
//		//passer dans le validate de qté2 et ensuite réenregistrer la ligne avec la nouvelle valeurs de qté2
//		TaLFacture masterEntityLigneAReenregistrer = null;
//		if(ligneAReenregistrer!=null) {
//			ligneAReenregistrer.getDto().setQte2LDocument(calculAffectationCoefficientQte2(ligneAReenregistrer));
//			if(ligneAReenregistrer.getDto().getIdLDocument()!=null &&  ligneAReenregistrer.getDto().getIdLDocument()!=0) {
//				masterEntityLigneAReenregistrer=rechercheLigne(ligneAReenregistrer.getDto().getIdLDocument());
//			}else if(ligneAReenregistrer.getDto().getNumLigneLDocument()!=null/* &&  selectedTaLBonReceptionDTOJSF.getDto().getNumLigneLDocument()!=0*/) {
//				masterEntityLigneAReenregistrer = rechercheLigneNumLigne(ligneAReenregistrer.getDto().getNumLigneLDocument());
//			}
//			if(masterEntityLigneAReenregistrer!=null)
//				masterEntityLigneAReenregistrer.setQte2LDocument(ligneAReenregistrer.getDto().getQte2LDocument());
//		}
//
//	}
//
//	public TaLFactureDTOJSF getLigneAReenregistrer() {
//		return ligneAReenregistrer;
//	}
//
//	public void setLigneAReenregistrer(TaLFactureDTOJSF ligneAReenregistrer) {
//		this.ligneAReenregistrer = ligneAReenregistrer;
//	}

}
