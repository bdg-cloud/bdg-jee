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
import fr.legrain.bdg.documents.service.remote.ITaInfosAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAvoirServiceRemote;
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
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaLAvoirDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosAvoir;
import fr.legrain.document.model.TaLAvoir;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
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
public class AvoirController extends AbstractDocumentController<TaAvoir,TaAvoirDTO,TaLAvoir,TaLAvoirDTO,TaLAvoirDTOJSF,TaInfosAvoir> {  

	private @EJB ITaAvoirServiceRemote taAvoirService;
	private @EJB ITaInfosAvoirServiceRemote taInfosAvoirService;
	private @EJB ITaLAvoirServiceRemote taLAvoirService;

	private boolean differenceReglementResteARegle=false;
	private boolean differenceReglementResteARegleForPrint=false;
	
//	private String theme="defaultTheme";
//	private String librairie="bdgFactTheme1";
	private String theme="";
	private String librairie="";
	
	
	public AvoirController() {  
	}  

	@PostConstruct
	public void postConstruct(){
		super.postConstruct();
		listePreferences= taPreferencesService.findByGroupe("avoir");
		nomDocument="Facture d'avoir";
		setTaDocumentService(taAvoirService);
		setTaLDocumentService(taLAvoirService);
		setTaInfosDocumentService(taInfosAvoirService);
		
		initListeEdition();
		
		stepSynthese = "idSyntheseAvoir";
		stepEntete = "idEnteteAvoir";
		stepLignes = "idLignesAvoir";
		stepTotaux = "idTotauxFormAvoir";
		stepValidation = "idValidationFormAvoir";
		idMenuBouton = "form:tabView:idMenuBoutonAvoir";
		classeCssDataTableLigne = "myclassAvoir";
		gestionCapsules = autorisationBean.isModeGestionCapsules();
		refresh();
		theme1();
		initialisePositionBoutonWizard();
//		gestionLot = autorisationBean.isModeGestionLot();
	}
	/**
	 * Méthode qui rempli la liste d'édition DTO pour un type de document
	 * @author yann
	 */
	public void initListeEdition() {
		listeEdition = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaAvoir.TYPE_DOC.toUpperCase());
		TaEditionDTO editionDefautNulle = new TaEditionDTO();
		editionDefautNulle.setLibelleEdition("Avoir modèle par défaut (V0220)");
		listeEdition.add(editionDefautNulle);
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
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("avoir");
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
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_AVOIR_DEFAUT);
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
				
				taEdition = taEditionService.rechercheEditionActionDefaut(null,action, TaAvoir.TYPE_DOC);
				
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
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_AVOIR_DEFAUT).get(0);
				}
				
				
			}
			//On rempli selectedEdition qui va être utiliser dans AbstractDocumentController.actDialogEmailSelectedEdition
			if(taEdition!=null) {
				
				taEdition.setMapParametreEdition(mapParametreEdition);
				selectedEdition = taEdition;

				
			}
	}
	/**
	 * Méthode dépréciée
	 * Méthode qui rempli choixEdition si une choix personnalisé d'édition à été fait
	 * @author yann
	 */
	public void initChoixEdition() {
		choixEdition = false;
		List<TaEdition> listeEditionDisponible = taEditionService.rechercheEditionDisponible("", ConstActionInterne.EDITION_AVOIR_DEFAUT,null);
		
		if(listeEditionDisponible!=null && !listeEditionDisponible.isEmpty() && listeEditionDisponible.size()>1) {
			choixEdition = true;
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
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("avoir");
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
		        masterEntity = taAvoirService.mergeAndFindById(masterEntity,ITaAvoirServiceRemote.validationContext);
			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_AVOIR_DEFAUT);
			editionParam.setMapParametreEdition(mapParametreEdition);
			
			sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, editionParam);
	        
			
			
			//ICI s'arrete la partie actDialogImprimer
			
			//ICI COMMENCE LA PARTIE handleReturnDialogImprimer
			String pdf="";
			if(edition == null) {//si aucune edition n'est choisie
				
				TaActionEdition actionImprimer = new TaActionEdition();
				actionImprimer.setCodeAction(TaActionEdition.code_impression);

				//on appelle une methode qui va aller chercher (et elle crée un fichier xml (writing)) elle même l'edition par defaut choisie en fonction de l'action si il y a 
				pdf = taAvoirService.generePDF(selectedDocumentDTO.getId(),mapParametreEdition,null,null,actionImprimer);
				
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
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_AVOIR_DEFAUT).get(0);
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
							String localPath  = taAvoirService.writingFileEdition(taEdition);
							//on genere le pdf avec le fichier xml crée ci-dessus
							 pdf = taAvoirService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
							

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
				masterEntity = taAvoirService.findById(masterEntity.getIdDocument());
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			updateTab();
		  
			
			
			//ICI FINI LA PARTIE handleReturnDialogImprimer
			
	}
	
	public void refresh() {
//		values = taAvoirService.selectAllDTO();
		values = taAvoirService.findAllLight();
		valuesLigne = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}


	public void IHMmodel(TaLAvoirDTOJSF t, TaLAvoir ligne) {

		LgrDozerMapper<TaLAvoir,TaLAvoirDTO> mapper = new LgrDozerMapper<TaLAvoir,TaLAvoirDTO>();
		TaLAvoirDTO dto = t.getDto();

		dto = (TaLAvoirDTO) mapper.map(ligne, TaLAvoirDTO.class);
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
	
	public List<TaLAvoirDTOJSF> IHMmodel() {
		LinkedList<TaLAvoir> ldao = new LinkedList<TaLAvoir>();
		LinkedList<TaLAvoirDTOJSF> lswt = new LinkedList<TaLAvoirDTOJSF>();

		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLAvoir,TaLAvoirDTO> mapper = new LgrDozerMapper<TaLAvoir,TaLAvoirDTO>();
			TaLAvoirDTO dto = null;
			for (TaLAvoir o : ldao) {
				TaLAvoirDTOJSF t = new TaLAvoirDTOJSF();
				dto = (TaLAvoirDTO) mapper.map(o, TaLAvoirDTO.class);
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
				if(o.getCodeTitreTransport()!=null) {
					try {
						t.setTaTitreTransport(taTitreTransportService.findByCode(o.getCodeTitreTransport()));
						if(t.getTaArticle()!=null && t.getTaArticle().getTaRTaTitreTransport()!=null)
							t.getTaArticle().getTaRTaTitreTransport().setTaTitreTransport(t.getTaTitreTransport());
					} catch (FinderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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


			LgrDozerMapper<TaLAvoirDTO,TaLAvoir> mapper = new LgrDozerMapper<TaLAvoirDTO,TaLAvoir>();
			mapper.map((TaLAvoirDTO) selectedLigneJSF.getDto(),masterEntityLigne);
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
			context.addMessage(null, new FacesMessage("Avoir", "actEnregisterLigne")); 
		}
	}

	private void verifSiDifferenceReglement(){
		differenceReglementResteARegle=false;
//		if(masterEntity.getTaReglement()!=null && modeEcran.dataSetEnModif()){
//			differenceReglementResteARegle=masterEntity.getTaReglement().getNetTtcCalc().compareTo(masterEntity.getNetTtcCalc())!=0;
//			if(differenceReglementResteARegle){
//				factureReglee=false;
//				actInitReglement();
//			}
//		}
		
		PrimeFaces.current().ajax().addCallbackParam("differenceReglementResteARegle", differenceReglementResteARegle);
		if(differenceReglementResteARegle)
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Règlement", "La facture est associée à un règlement dont le montant diffère du total de la facture."
					+ "\r\nVous devrez revalider le règlement."));
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taAvoirService.annuleCode(selectedDocumentDTO.getCodeDocument());
				}				
				masterEntity=new TaAvoir();
				selectedDocumentDTO=new TaAvoirDTO();

				if(values.size()>0)	selectedDocumentDTO = values.get(0);

				onRowSelect(null);

				valuesLigne=IHMmodel();
				initInfosDocument();
				break;
			case C_MO_EDITION:
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					masterEntity = taAvoirService.findByIDFetch(selectedDocumentDTO.getId());
					selectedDocumentDTO=taAvoirService.findByIdDTO(selectedDocumentDTO.getId());
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
				context.addMessage(null, new FacesMessage("Avoir", "actAnnuler")); 
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
//
//		if(masterEntity.getTaReglement()!=null){
//			selectedDocumentDTO.setMontantReglement(masterEntity.getTaReglement().getNetTtcCalc());
//			selectedDocumentDTO.setLibelleReglement(masterEntity.getTaReglement().getLibelleDocument());
//			if(masterEntity.getTaReglement().getTaTPaiement()!=null)
//				selectedDocumentDTO.setCodeTPaiement(masterEntity.getTaReglement().getTaTPaiement().getCodeTPaiement());
//		}
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

//			//rajouté temporairement pour aller vite, sera remplacé par la gestion des réglements multiples
//			factureReglee=(masterEntity.getTaReglement()!=null && 
//					masterEntity.getTaReglement().getIdDocument()!=0);
//			if(masterEntity.getTaReglement()!=null){
//				taReglement=masterEntity.getTaReglement();

//				if(taReglement.getTaTPaiement()!=null && taReglement.getTaTPaiement().getIdTPaiement()!=0)
//					taTPaiement=taTPaiementService.findById(taReglement.getTaTPaiement().getIdTPaiement());
				if(taTPaiement!=null){
					selectedDocumentDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
//				}
//				selectedDocumentDTO.setLibelleReglement(taReglement.getLibelleDocument());
//				selectedDocumentDTO.setMontantReglement(taReglement.getNetTtcCalc());
			}else{
//				selectedDocumentDTO.setLibelleReglement("");
//				selectedDocumentDTO.setMontantReglement(BigDecimal.ZERO);
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actEnregistrer(ActionEvent actionEvent) {		
		try {
			masterEntity.calculeTvaEtTotaux();
			verifSiDifferenceReglement();

//			if(differenceReglementResteARegle){
//
//			}else{
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
				List<TaLAvoir> listeLigneVide = new ArrayList<TaLAvoir>(); 
				for (TaLAvoir ligne : masterEntity.getLignes()) {
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
				for (TaLAvoir taLBonReception : listeLigneVide) {
					masterEntity.getLignes().remove(taLBonReception);
				}
				
				//suppression des liaisons entre ligne doc et ligne d'échéance
				for (TaLigneALigneEcheance ligneALigne : listeLigneALigneEcheanceASupprimer) {
					taLigneALigneEcheanceService.remove(ligneALigne);
				}

				//isa le 08/11/2016
				//j'ai rajouté cette réinitialisation tant que l'on enlève les lignes vides, sinon génère des trous dans la numérotation des lignes
				masterEntity.reinitialiseNumLignes(0, 1);

				boolean mouvementerLigneAvoir = masterEntity.getMouvementerStock(); //TODO uniquement paramétré par les préférences pour l'instant, ajouter un cb au niveau du document alimenter par la préférence et un cb sur chaque ligne
				
				if(mouvementerLigneAvoir) {
					/*
					 * Gérer les mouvements corrrespondant aux lignes 
					 */
					//Création du groupe de mouvement
					TaGrMouvStock grpMouvStock = new TaGrMouvStock();
					if(masterEntity.getTaGrMouvStock()!=null) {
						grpMouvStock=masterEntity.getTaGrMouvStock();
					}
					grpMouvStock.setCodeGrMouvStock(masterEntity.getCodeDocument());
					grpMouvStock.setDateGrMouvStock(masterEntity.getDateDocument());
					grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument()!=null?masterEntity.getLibelleDocument():"Avoir "+masterEntity.getCodeDocument());
					grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("T")); // type mouvement Avoir
					masterEntity.setTaGrMouvStock(grpMouvStock);
					grpMouvStock.setTaAvoir(masterEntity);
	
					for (TaLAvoir l : masterEntity.getLignes()) {
						if(!l.getTaTLigne().equals(typeLigneCommentaire)){
							l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
							if(l.getTaMouvementStock()!=null)
								l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
						}
					}
					
					grpMouvStock.getTaMouvementStockes().clear();
	
					//Création des lignes de mouvement
					for (TaLAvoir ligne : masterEntity.getLignes()) {
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
							mouv.setQte1Stock(ligne.getQteLDocument());
							mouv.setQte2Stock(ligne.getQte2LDocument());
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

//				if(masterEntity.getTaReglement()!=null){
//					masterEntity.getTaReglement().setTaTiers(masterEntity.getTaTiers());
//					masterEntity.getTaReglement().setTaTPaiement(masterEntity.getTaTPaiement());
//					//masterEntity.getTaReglement().setLibelleDocument(selectedDocumentDTO.getLibelleReglement());
//					if(masterEntity.getTaReglement().getIdDocument()==0 || 
//							(masterEntity.getTaReglement().getCodeDocument()==null||masterEntity.getTaReglement().getCodeDocument().equals("")))
//						masterEntity.getTaReglement().setCodeDocument(taReglementService.genereCode(null) );
//				}
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
				masterEntity = taAvoirService.mergeAndFindById(masterEntity,ITaAvoirServiceRemote.validationContext);
				taAvoirService.annuleCode(selectedDocumentDTO.getCodeDocument());

				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				autoCompleteMapDTOtoUI();
				
				selectedEtatDocument=null;
				if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
				
				selectedDocumentDTOs=new TaAvoirDTO[]{selectedDocumentDTO};


				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
					values.add(selectedDocumentDTO);
				}

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(impressionDirect) {
					if(impressionDirectClient) {
						imprimeEdition(null, "CLIENT", "true"); 
					}else {
						imprimeEdition(null, "BROUILLON", "false");
					}
					
				}

				if(envoyeParEmail) {
					envoiMailEdition(null, "CLIENT", "true");
				}
				updateTab();
				
				ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
				if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
				changementStepWizard(false);
				

//			}

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Avoir", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Avoir", "actEnregistrer")); 
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			
			impressionDirectClient = false;
			miseADispositionCompteClient = false;
			envoyeParEmail = false;
			
			listePreferences= taPreferencesService.findByGroupe("avoir");
			selectedDocumentDTO = new TaAvoirDTO();
			selectedDocumentDTO.setCommentaire(rechercheCommentaireDefautDocument());
			masterEntity = new TaAvoir();
			masterEntity.setLegrain(true);
			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			selectedEtatDocument=null;
			factureReglee=false;

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
				taAvoirService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			
			//			String newCode = taAvoirService.genereCode(paramsCode);
			//			if(newCode!=null && !newCode.equals("")){
			//				selectedDocumentDTO.setCodeDocument(newCode);
			//			}
			selectedDocumentDTO.setCodeDocument(taAvoirService.genereCode(paramsCode));
			changementTiers(true);

			//			selectedDocumentDTO.setCodeTTvaDoc("F");
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);
			
			TaPreferences p1 = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_GESTION_DES_LOTS);
			if(p1!=null && LibConversion.StringToBoolean(p1.getValeur()) != null) {
				selectedDocumentDTO.setGestionLot(LibConversion.StringToBoolean(p1.getValeur()));
				masterEntity.setGestionLot(LibConversion.StringToBoolean(p1.getValeur()));
			}
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_STOCKS, ITaPreferencesServiceRemote.STOCK_MOUVEMENTER_STOCK_POUR_LIGNE_AVOIR);
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				selectedDocumentDTO.setMouvementerStock(LibConversion.StringToBoolean(p.getValeur()));
				masterEntity.setMouvementerStock(LibConversion.StringToBoolean(p.getValeur()));
			}
			
			TaPreferences crd = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_CRD, ITaPreferencesServiceRemote.STOCK_MOUVEMENTER_CRD_POUR_LIGNE_AVOIR);
			if(crd!=null && LibConversion.StringToBoolean(crd.getValeur()) != null) {
				selectedDocumentDTO.setMouvementerCRD(LibConversion.StringToBoolean(crd.getValeur()));
				masterEntity.setMouvementerStock(LibConversion.StringToBoolean(crd.getValeur()));
			}

			
			TaPreferences nb;
			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
			if(nb!=null && nb.getValeur() != null) {
				masterEntity.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
			}


//			btnPrecedentVisible = false;
//			btnSuivantVisible = true;
//			initialisePositionBoutonWizard();
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Avoir", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_AVOIR);
		b.setTitre("Facture d'avoir");
		b.setTemplate("documents/avoir.xhtml");
		b.setIdTab(LgrTab.ID_TAB_AVOIR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_AVOIR);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		//		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Avoir", 
					"Nouveau"
					)); 
		}
	} 
	public void actSupprimerLigne(ActionEvent actionEvent) {
		if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT)) {
			  if(selectedLigneJSF.getDto().getIdLDocument() != null) {
//					//List<TaLEcheance> liste  = taLEcheanceService.findAllByIdLFacture(selectedLigneJSF.getDto().getIdLDocument());
//					List<TaLigneALigneEcheance> liste  = taLigneALigneEcheanceService.findAllByIdLDocumentAndTypeDoc(selectedLigneJSF.getDto().getIdLDocument(), TaAvoir.TYPE_DOC);
//					if(liste != null && !liste.isEmpty()) {
//						for (TaLigneALigneEcheance li : liste) {
//							if(!listeLigneALigneEcheanceASupprimer.contains(li)) {
//								listeLigneALigneEcheanceASupprimer.add(li);
//							}
//							
//						}
//
//					}
			  }
			}
		super.actSupprimerLigne(actionEvent);
	}
	public void actSupprimer(ActionEvent actionEvent) {
		TaAvoir obj = new TaAvoir();
		try {
			if(autorisationLiaisonComplete()) {
			if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
				obj = taAvoirService.findByIDFetch(selectedDocumentDTO.getId());
			}
			if(verifSiEstModifiable(obj)) {
				taAvoirService.remove(obj);
				values.remove(selectedDocumentDTO);

				if(!values.isEmpty()) {
					selectedDocumentDTO = values.get(0);
					selectedDocumentDTOs = new TaAvoirDTO[]{selectedDocumentDTO};
					updateTab();
				}else{
					selectedDocumentDTO = new TaAvoirDTO();
					selectedDocumentDTOs = new TaAvoirDTO[]{};
				}

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Avoir", "actSupprimer"));
				}
			}
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Avoir", e.getMessage()));
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

		selectedDocumentDTO=new TaAvoirDTO();
		selectedDocumentDTOs=new TaAvoirDTO[]{selectedDocumentDTO};
		updateTab();

//		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeAvoir').filter()");
        
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Avoir", "actFermer")); 
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

	
	public void actImprimerGlobal(ActionEvent actionEvent,/*boolean editionClient,*/ String modeRegle, String modeNonRegle) {	
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Avoir", "actImprimer")); 
		}
		try {
			boolean editionClient=false;
			if(actionEvent!=null){
				String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
				editionClient = LibConversion.StringToBoolean(pourClient);
			}
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaAvoir doc =taAvoirService.findById(selectedDocumentDTO.getId());
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );

			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			//mapEdition.put("LibelleJournalTva", taTTvaDoc.getLibelleEdition());

			mapEdition.put("lignes", masterEntity.getLignes());

			//"select r from TaRReglement r join r.taAvoir f where f.idDocument  = "+idDocument;
			mapEdition.put("taRReglement", null);

			//sessionMap.put("doc", taAvoirService.findById(selectedDocumentDTO.getId()));

			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("avoir");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			mapParametreEdition.put("Theme", theme);
			mapParametreEdition.put("Librairie", librairie);
//			mapParametreEdition.put("intra", doc.getTaTiers().getTaCompl().getTvaIComCompl());
			
			mapEdition.put("param", mapParametreEdition);

			sessionMap.put("edition", mapEdition);

			if(editionClient || impressionDirectClient) {
//				Mise à jour de la mise à dispostion
				if(masterEntity.getTaMiseADisposition()==null) {
					masterEntity.setTaMiseADisposition(new TaMiseADisposition());
				}
				Date dateMiseADispositionClient = new Date();
				masterEntity.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
				masterEntity = taAvoirService.mergeAndFindById(masterEntity,ITaAvoirServiceRemote.validationContext);
				updateTab();
				System.out.println("editionClient ou impressionDirectClient TRUE !!!!!!!!!");
			}
			//			session.setAttribute("tiers", taTiersService.findById(selectedDocumentDTO.getId()));
			System.out.println("AvoirController.actImprimer()");
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}  
	
	public void actImprimerListeDesAvoirs(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesAvoirs", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	/**Ajouts pour choix d'impressions**/
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
		
//		creerLot();
//		sessionMap.put("lotBR", selectedTaLBonReceptionDTOJSF.getTaLot());
//		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
		//sessionMap.put("numLot", selectedTaLBonReceptionDTOJSF.getDto().getNumLot());
//		sessionMap.put("numLot", numLot);
		
		EditionParam edition = new EditionParam();
		edition.setIdActionInterne(ConstActionInterne.EDITION_AVOIR_DEFAUT);
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
					

					String pdf = taAvoirService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
					PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");
					masterEntity = taAvoirService.findById(masterEntity.getIdDocument());
					updateTab();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


		}
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
	/**
	 * Cette méthode est appellée une fois que le dialog d'envoi de mail est fermé
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
			masterEntity = taAvoirService.mergeAndFindById(masterEntity,ITaAvoirServiceRemote.validationContext);
			updateTab();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					)); 
		}
	}
	
	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FACTURE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void updateTab(){
		try {		
			super.updateTab();
			autoriseSuppressionLigne(true);
			selectedEtatDocument=null;
			if(masterEntity!=null && masterEntity.getTaREtat()!=null)
				selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			
//			if(masterEntity.getTaReglement()!=null){
//				differenceReglementResteARegle=masterEntity.getTaReglement().getNetTtcCalc().compareTo(masterEntity.getNetTtcCalc())!=0;
//			}
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Avoir", 
						"Chargement du Avoir N°"+selectedDocumentDTO.getCodeTiers()
						)); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_AVOIR);
		b.setTitre("Facture d'avoir");
		b.setTemplate("documents/avoir.xhtml");
		b.setIdTab(LgrTab.ID_TAB_AVOIR);
		b.setStyle(LgrTab.CSS_CLASS_TAB_AVOIR);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);

		updateTab();
		scrollToTop();
	} 

	public void validateLignesDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String msg = "";

		try {
			//String selectedRadio = (String) value;

			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			//msg = "Mon message d'erreur pour : "+nomChamp;

			validateUIField(nomChamp,value);
			TaLAvoirDTO dtoTemp =new TaLAvoirDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taLAvoirService.validateDTOProperty(dtoTemp, nomChamp, ITaLAvoirServiceRemote.validationContext );

			//selectedDocumentDTO.setAdresse1Adresse("abcd");

			//		  if(selectedRadio.equals("aa")) {
			//			  throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
			//		  }

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");

			if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)||nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)){
				if(value==null || value.equals(""))value=BigDecimal.ZERO;
			}

			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaAvoirDTO>> violations = factory.getValidator().validateValue(TaAvoirDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaAvoirDTO> cv : violations) {
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
			//taBonReceptionService.validateDTOProperty(selectedDocumentDTO,Const.C_CODE_TIERS,  ITaAvoirServiceRemote.validationContext );

		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	public void validateLigneDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
			if(selectedLigneJSF.getTaCoefficientUnite()!=null) {
				if(!calculCoherenceAffectationCoefficientQte2(selectedLigneJSF.getDto().getQte2LDocument())) {
					setLigneAReenregistrer(selectedLigneJSF);
					PrimeFaces.current().executeScript("PF('wVdialogQte2Avoir').show()");
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
					((TaAvoirDTO)selectedDocumentDTO).setLibelleDocument("Avoir N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);										
					if(!disableTtc() && !disableTtcSiDocSansTVA()){
						masterEntity.setTtc(entity.getTtcTiers());
						((TaAvoirDTO)selectedDocumentDTO).setTtc(LibConversion.intToBoolean(masterEntity.getTtc()));
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
				}
				selectedLigneJSF.setTaArticle(entity);
				masterEntityLigne.setTaArticle(entity);
				if(changementArticleLigne) {
					if(entity!=null)selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
					//					selectedLigneJSF.getDto().setDluo(LibDate.incrementDate(selectedLigneJSF.getDateDocument(), LibConversion.stringToInteger(entity.getParamDluo(), 0)+1  , 0, 0));
					//					selectedLigneJSF.getDto().setLibelleLot(entity.getLibellecArticle());
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
				}

				
				if(entity!=null && entity.getTaRTaTitreTransport()!=null && gestionCapsuleAvoir()) {
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
			} else if(nomChamp.equals(Const.C_QTE_L_DOCUMENT)) {
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
//				if(changement || masterEntity.getTaReglement()==null) {
//					actInitReglement();
//				}
			}else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {
				TaCPaiement entity = null;
				if(value!=null){
					entity = (TaCPaiement) value;
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
				taInfosDocument = taInfosAvoirService.findByCodeAvoir(((IDocumentDTO)selectedDocumentDTO).getCodeDocument());
			} else {
				taInfosDocument = new TaInfosAvoir();
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

	public void initialisationDesCPaiement(Boolean recharger) {
		LinkedList<TaCPaiementDTO> liste = new LinkedList<TaCPaiementDTO>();
		try {

			TaInfosAvoir taInfosDocument = null;
			if (masterEntity != null) {
				if (masterEntity.getCodeDocument()!=null&& masterEntity.getCodeDocument() != "")
					taInfosDocument = taInfosAvoirService.findByCodeAvoir(masterEntity.getCodeDocument());
				else
					taInfosDocument = new TaInfosAvoir();


				taCPaiementDoc = null;
				List<TaCPaiement> listeCPaiement=taCPaiementService.rechercheParType(TaAvoir.TYPE_DOC);
				if(listeCPaiement!=null && !listeCPaiement.isEmpty())taCPaiementDoc=listeCPaiement.get(0);
				

				if (taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_AVOIR) != null
						&& taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_AVOIR).getTaCPaiement() != null) {
					taCPaiementDoc = taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_AVOIR).getTaCPaiement();
				}
				int report = 0;
				int finDeMois = 0;

				TaTPaiement taTPaiementReglement = new TaTPaiement();
//				if (masterEntity.reglementExiste()&& masterEntity.getTaRReglement().getTaReglement()
//						.getTaTPaiement() != null) {
//					taTPaiementReglement = masterEntity.getTaRReglement().getTaReglement().getTaTPaiement();
//				}

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
				((TaAvoirDTO)selectedDocumentDTO).setDateEchDocument(taAvoirService.calculDateEcheance(masterEntity,report, finDeMois));
			}
		}
	}

	//	public void actReinitCPaiement() throws Exception {
	//		initialisationDesInfosComplementaires(true,Const.RECHARGE_C_PAIEMENT);
	//		calculDateEcheance(true);
	//		actModifier();
	//	}

	public void actInitReglement(){
//		if(factureReglee){
//
//			TaReglement reglement=masterEntity.getTaReglement();
//			if(reglement==null)reglement=new TaReglement();
//			reglement.setTaTiers(masterEntity.getTaTiers());
//			reglement.setTaTPaiement(taTPaiement);
//
//			//			TaTPaiement typePaiement = taTPaiement;
//			if(taTPaiement==null){
//
//				if(masterEntity.getTaTiers()!=null && masterEntity.getTaTiers().getTaTPaiement()!=null ){
//					taTPaiement=masterEntity.getTaTiers().getTaTPaiement();
//				}
//
//				else {
//					if (typePaiementDefaut == null || typePaiementDefaut=="")
//						typePaiementDefaut="C";
//					try {
//						taTPaiement = taTPaiementService.findByCode(typePaiementDefaut);
//
//					} catch (Exception e) {
//					}
//				}
//			}
//			reglement.setTaTPaiement(taTPaiement);
//			reglement.setDateDocument(masterEntity.getDateDocument());
//			//			reglement.setCodeDocument(taReglementService.genereCode(null));
//			reglement.setTaCompteBanque(taCompteBanqueService.findByTiersEntreprise(taTPaiement));
//			reglement.setLibelleDocument(taTPaiement.getLibTPaiement());
//			reglement.setNetTtcCalc(BigDecimal.ZERO);
//			reglement.setNetTtcCalc(masterEntity.getResteAReglerComplet());
//			masterEntity.setTaReglementAndCalcul(reglement);
//			selectedDocumentDTO.setMontantReglement(reglement.getNetTtcCalc());
//			selectedDocumentDTO.setLibelleReglement(reglement.getLibelleDocument());
//			//			if(reglement.getTaTPaiement()!=null)
//			//				selectedDocumentDTO.setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
//
//		}else{
//			masterEntity.setTaReglementAndCalcul(null);
//			selectedDocumentDTO.setMontantReglement(BigDecimal.ZERO);
//			selectedDocumentDTO.setLibelleReglement("");
//		}
//
//		selectedDocumentDTO.setResteARegler(masterEntity.getResteARegler());
	}

	public void actAppliquerCPaiement() throws Exception {
		calculDateEcheance(true);
//		taAvoirService.mettreAJourDateEcheanceReglements(masterEntity);
		actModifier();
	}

	public void calculTypePaiement(boolean recharger) {
//		if (masterEntity != null) {
//			if (masterEntity.getTaTiers() != null
//					&& masterEntity.getTaTiers().getTaTPaiement() != null
//					&& masterEntity.getTaRReglement() != null
//					&& masterEntity.getTaRReglement().getTaReglement() != null &&masterEntity.getTaRReglement().
//					getTaReglement().getNetTtcCalc().signum()!=0 ) {
//				masterEntity.getTaRReglement().getTaReglement().setTaTPaiement(
//						masterEntity.getTaTiers().getTaTPaiement());
//				masterEntity.getTaRReglement().getTaReglement()
//				.setLibelleDocument(masterEntity.getTaTiers().getTaTPaiement().getLibTPaiement());
//			} else {
//				try {
//					typePaiementDefaut = taPreferencesService.findByCode(TaTPaiement.getCodeTPaiementDefaut()).getValeur();
//				} catch (FinderException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				if (typePaiementDefaut == null || typePaiementDefaut=="")
//					typePaiementDefaut="C";
//				TaTPaiement taTPaiement = null;
//				try {
//					taTPaiement = taTPaiementService
//							.findByCode(typePaiementDefaut);
//
//				} catch (Exception e) {
//				}
//
//				if(masterEntity.reglementExiste()&&masterEntity.getTaRReglement().
//						getTaReglement().getNetTtcCalc().signum()!=0){
//					masterEntity.getTaRReglement().getTaReglement()
//					.setTaTPaiement(taTPaiement);
//					masterEntity.getTaRReglement().getTaReglement()
//					.setLibelleDocument(taTPaiement.getLibTPaiement());
//					validateUIField(Const.C_CODE_T_PAIEMENT, taTPaiement.getCodeTPaiement());
//				}else{
//					if(masterEntity.getTaRReglement()!=null && masterEntity.getTaRReglement().getTaReglement()!=null){
//						masterEntity.getTaRReglement().getTaReglement().setTaTPaiement(null);
//						masterEntity.getTaRReglement().getTaReglement().setLibelleDocument(null);
//					}
//				}
//
//			}
//		}
		initialisationDesCPaiement(recharger);
	}

	
	public void recupMasterEntity() {
		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION)) {
			try {
				masterEntity=taAvoirService.findByIDFetch(masterEntity.getIdDocument());
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

	public void onClearArticle(AjaxBehaviorEvent event){
		super.onClearArticle(event);
		selectedLigneJSF.setTaLot(null);
		selectedLigneJSF.getDto().setNumLot(null);
		if(selectedLigneJSF.getArticleLotEntrepotDTO()!=null)selectedLigneJSF.getArticleLotEntrepotDTO().setNumLot(null);
		
		masterEntityLigne.calculMontant();
		masterEntity.calculeTvaEtTotaux();
		validateUIField(Const.C_CODE_ARTICLE, null);
	}

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

	public void onClearTaTitreTransport(AjaxBehaviorEvent event){
		selectedLigneJSF.getDto().setCodeTitreTransport(null);
		selectedLigneJSF.getDto().setQteTitreTransport(null);
		selectedLigneJSF.setTaTitreTransport(null);
		masterEntityLigne.setCodeTitreTransport(null);
		masterEntityLigne.setQteTitreTransport(null);
	}
	
	public boolean gestionCapsuleAvoir() {
		return gestionCapsules&&selectedDocumentDTO.getMouvementerCRD();
	}

}
