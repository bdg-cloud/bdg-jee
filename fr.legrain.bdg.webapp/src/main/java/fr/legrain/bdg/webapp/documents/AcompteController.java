package fr.legrain.bdg.webapp.documents;

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
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.annotation.Obsolete;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAcompteServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.CodeToCheck;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.osgi.ConstActionInterne;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.EditionParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.reglementMultiple.TaReglementDTOJSF;
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.dto.TaLAcompteDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaInfosAcompte;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaReglement;
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
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class AcompteController extends AbstractDocumentController<TaAcompte,TaAcompteDTO,TaLAcompte,TaLAcompteDTO,TaLAcompteDTOJSF,TaInfosAcompte> {  


	/**
	 * 
	 */
	private static final long serialVersionUID = -178627206901795117L;
	private @EJB ITaAcompteServiceRemote taAcompteService;
	private @EJB ITaInfosAcompteServiceRemote taInfosDocumentService;
	private @EJB ITaLAcompteServiceRemote taLAcompteService;
	
	@Inject @Named(value="ouvertureTiersBean")
	private OuvertureTiersBean ouvertureTiersBean;
	
	@Inject @Named(value="ouvertureArticleBean")
	private OuvertureArticleBean ouvertureArticleBean;

	private boolean creeReglement=true;

	
	public AcompteController() {  
	}  
	


	@PostConstruct	
	public void postConstruct(){
		super.postConstruct();
		listePreferences= taPreferencesService.findByGroupe("acompte");
		nomDocument="Acompte";
		setTaDocumentService(taAcompteService);
		setTaLDocumentService(taLAcompteService);
		setTaInfosDocumentService(taInfosDocumentService);
		
		initListeEdition();
		
		stepSynthese = "idSyntheseAcompte";
		stepEntete = "idEnteteAcompte";
		stepLignes = "idLignesAcompte";
		stepTotaux = "idTotauxFormAcompte";
		stepValidation = "idValidationFormAcompte";
		idMenuBouton = "form:tabView:idMenuBoutonAcompte";
		wvEcran = LgrTab.WV_TAB_ACOMPTE;
		classeCssDataTableLigne = "myclassAcompte";
		refresh();
	}

	public void refresh(){
		values = taAcompteService.findAllLight();
//		values = taAcompteService.selectAllDTO();
		valuesLigne = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaLAcompteDTOJSF> IHMmodel() {
		LinkedList<TaLAcompte> ldao = new LinkedList<TaLAcompte>();
		LinkedList<TaLAcompteDTOJSF> lswt = new LinkedList<TaLAcompteDTOJSF>();

		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLAcompte,TaLAcompteDTO> mapper = new LgrDozerMapper<TaLAcompte,TaLAcompteDTO>();
			TaLAcompteDTO dto = null;
			for (TaLAcompte o : ldao) {
				TaLAcompteDTOJSF t = new TaLAcompteDTOJSF();
				dto = (TaLAcompteDTO) mapper.map(o, TaLAcompteDTO.class);
				t.setDto(dto);
				//				t.setTaLot(o.getTaLot()); //@@ champs à ajouter aux devis
				t.setTaArticle(o.getTaArticle());
				if(o.getTaArticle()!=null){
					t.setTaRapport(o.getTaArticle().getTaRapportUnite());
				}
				//				t.setTaEntrepot(o.getTaEntrepot()); //@@ champs à ajouter aux devis
				if(o.getU1LDocument()!=null) {
					t.setTaUnite1(new TaUnite());
					t.getTaUnite1().setCodeUnite(o.getU1LDocument());
				}
				if(o.getU2LDocument()!=null) {
					t.setTaUnite2(new TaUnite());
					t.getTaUnite2().setCodeUnite(o.getU2LDocument());
					t.setTaCoefficientUnite(recupCoefficientUnite(t.getDto().getU1LDocument(),t.getDto().getU2LDocument()));
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
	
	public void initListeEdition() {
		listeEdition = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaAcompte.TYPE_DOC.toUpperCase());
		TaEditionDTO editionDefautNulle = new TaEditionDTO();
		editionDefautNulle.setLibelleEdition("Acompte modèle par défaut (V0220)");
		listeEdition.add(editionDefautNulle);
	}
	
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
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("acompte");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			  

//			if(!editionClient) {
//				switch (modeEdition) {
//				case "BROUILLON":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "BROUILLONAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "BROUILLON");
//					}
//					break;
//				case "DUPLICATA":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "DUPLICATAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "DUPLICATA");
//					}
//					break;
//				case "PAYER":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "PAYER");
//					} else {
//						mapParametreEdition.put("mode", "PAYER");
//					}
//					break;
//	
//				default:
//					break;
//				}
//			} else {
//				if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//					mapParametreEdition.put("mode", "CLIENTAPAYER");
//				} else {
//					mapParametreEdition.put("mode", "CLIENT");
//				}
//			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_ACOMPTE_DEFAUT);
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
				
				taEdition = taEditionService.rechercheEditionActionDefaut(null,action, TaAcompte.TYPE_DOC);
				
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
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_ACOMPTE_DEFAUT).get(0);
				}
				
				
			}
			//On rempli selectedEdition qui va être utiliser dans AbstractDocumentController.actDialogEmailSelectedEdition
			if(taEdition!=null) {
				
				taEdition.setMapParametreEdition(mapParametreEdition);
				selectedEdition = taEdition;

				
			}
	}
	
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
			
			List<TaPreferences> liste= taPreferencesService.findByGroupe("acompte");
			mapParametreEdition.put("preferences", liste);
			mapParametreEdition.put("gestionLot", masterEntity.getGestionLot());
			  
		    if(!editionClient) {

		    }else {
		        //Mise à jour de la mise à dispostion
		        if(masterEntity.getTaMiseADisposition()==null) {
		            masterEntity.setTaMiseADisposition(new TaMiseADisposition());
		        }
		        Date dateMiseADispositionClient = new Date();
		        masterEntity.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
		        masterEntity = taAcompteService.mergeAndFindById(masterEntity,ITaAcompteServiceRemote.validationContext);
		    }

//			if(!editionClient) {
//				switch (modeEdition) {
//				case "BROUILLON":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "BROUILLONAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "BROUILLON");
//					}
//					break;
//				case "DUPLICATA":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "DUPLICATAPAYER");
//					} else {
//						mapParametreEdition.put("mode", "DUPLICATA");
//					}
//					break;
//				case "PAYER":
//					if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//						mapParametreEdition.put("mode", "PAYER");
//					} else {
//						mapParametreEdition.put("mode", "PAYER");
//					}
//					break;
//	
//				default:
//					break;
//				}
//			} else {
//				if(masterEntity.getResteAReglerComplet().compareTo(BigDecimal.ZERO)==0) {
//					mapParametreEdition.put("mode", "CLIENTAPAYER");
//				} else {
//					mapParametreEdition.put("mode", "CLIENT");
//				}
//			}
			
			
			EditionParam editionParam = new EditionParam();
			editionParam.setIdActionInterne(ConstActionInterne.EDITION_ACOMPTE_DEFAUT);
			editionParam.setMapParametreEdition(mapParametreEdition);
			
			sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, editionParam);
	        
			
			
			//ICI s'arrete la partie actDialogImprimer
			
			//ICI COMMENCE LA PARTIE handleReturnDialogImprimer
			String pdf="";
			if(edition == null) {//si aucune edition n'est choisie
				
				TaActionEdition actionImprimer = new TaActionEdition();
				actionImprimer.setCodeAction(TaActionEdition.code_impression);

				//on appelle une methode qui va aller chercher (et elle crée un fichier xml (writing)) elle même l'edition par defaut choisie en fonction de l'action si il y a 
				pdf = taAcompteService.generePDF(selectedDocumentDTO.getId(),mapParametreEdition,null,null,actionImprimer);
				
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
					taEdition = taEditionService.rechercheEditionDisponibleProgrammeDefaut("", ConstActionInterne.EDITION_ACOMPTE_DEFAUT).get(0);
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
							String localPath  = taAcompteService.writingFileEdition(taEdition);
							//on genere le pdf avec le fichier xml crée ci-dessus
							 pdf = taAcompteService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
							

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
				masterEntity = taAcompteService.findById(masterEntity.getIdDocument());
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			updateTab();
		  
			
			
			//ICI FINI LA PARTIE handleReturnDialogImprimer
	}	
	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			//Mise à jour de la mise à dispostion
			if(masterEntity.getTaMiseADisposition()==null) {
				masterEntity.setTaMiseADisposition(new TaMiseADisposition());
			}
			Date dateMiseADispositionClient = new Date();
			
			masterEntity.getTaMiseADisposition().setEnvoyeParEmail(dateMiseADispositionClient);
			masterEntity = taAcompteService.mergeAndFindById(masterEntity,ITaAcompteServiceRemote.validationContext);
			updateTab();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					));  
		}
	}
	
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
					

					String pdf = taAcompteService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
					PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");
					masterEntity = taAcompteService.findById(masterEntity.getIdDocument());
					updateTab();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}


		}
	}
	
	public void actEnregistrerLigne(ActionEvent actionEvent) {

		try {
			selectedLigneJSF.updateDTO(false);
			//			if(selectedLigneJSF.getDto().getIdLDocument()!=null &&  selectedLigneJSF.getDto().getIdLDocument()!=0) {
			//				masterEntityLigne=rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
			//			}
			
			if(masterEntityLigne.getTaArticle()!=null && selectedLigneJSF.getDto().getPrixULDocument()==null) {
				masterEntityLigne.setPrixULDocument(BigDecimal.ZERO);
				selectedLigneJSF.getDto().setPrixULDocument(BigDecimal.ZERO);
			}

			LgrDozerMapper<TaLAcompteDTO,TaLAcompte> mapper = new LgrDozerMapper<TaLAcompteDTO,TaLAcompte>();
			mapper.map((TaLAcompteDTO) selectedLigneJSF.getDto(),masterEntityLigne);

			//masterEntityLigne.setTaArticle(selectedLigneJSF.getTaArticle());

			//			masterEntityLigne.setTaEntrepot(selectedLigneJSF.getTaEntrepot());//@@ champs à ajouter aux devis
			//			if(selectedLigneJSF.getDto()!=null ) {
			//				TaLot l = new TaLot();
			//				if(selectedLigneJSF.getTaLot()!=null)l=selectedLigneJSF.getTaLot();
			////				if(selectedLigneJSF.getDto().getDluo()!=null) {//@@ champs à ajouter aux devis
			////					l.setDluo(selectedLigneJSF.getDto().getDluo());//@@ champs à ajouter aux devis
			////				}
			////				l.setNumLot(selectedLigneJSF.getDto().getNumLot());//@@ champs à ajouter aux devis
			//				l.setUnite1(masterEntityLigne.getU1LDocument());
			//				l.setUnite2(masterEntityLigne.getU2LDocument());
			//				l.setTaArticle(masterEntityLigne.getTaArticle());
			//				if(selectedLigneJSF.getTaRapport()!=null){
			//					l.setNbDecimal(selectedLigneJSF.getTaRapport().getNbDecimal());
			//					l.setSens(selectedLigneJSF.getTaRapport().getSens());
			//					l.setRapport(selectedLigneJSF.getTaRapport().getRapport());
			//				}
			////				masterEntityLigne.setTaLot(l);//@@ champs à ajouter aux devis
			//			}
			if(masterEntityLigne.getCodeTvaLDocument()!=null && !masterEntityLigne.getCodeTvaLDocument().isEmpty()) {
				TaTva tva=taTvaService.findByCode(masterEntityLigne.getCodeTvaLDocument());
				if(tva!=null)
					masterEntityLigne.setLibTvaLDocument(tva.getLibelleTva());
			}

			masterEntity.enregistreLigne(masterEntityLigne);
			if(!masterEntity.getLignes().contains(masterEntityLigne))
				masterEntity.addLigne(masterEntityLigne);	

			//			taBonReceptionService.calculeTvaEtTotaux(masterEntity);
			masterEntity.calculeTvaEtTotaux();
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);

			//			modeEcranLigne.setMode(EnumModeObjet.C_MO_INSERTION);
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Acompte", "actEnregisterLigne")); 
		}
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taAcompteService.annuleCode(selectedDocumentDTO.getCodeDocument());
				}
				masterEntity=new TaAcompte();
				selectedDocumentDTO=new TaAcompteDTO();

				if(values.size()>0)	selectedDocumentDTO = values.get(0);

				onRowSelect(null);

				valuesLigne=IHMmodel();
				initInfosDocument();
				break;
			case C_MO_EDITION:
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					masterEntity = taAcompteService.findByIDFetch(selectedDocumentDTO.getId());
					selectedDocumentDTO=taAcompteService.findByIdDTO(selectedDocumentDTO.getId());
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
				context.addMessage(null, new FacesMessage("Acompte", "actAnnuler")); 
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
		if(taTPaiement!=null) {
			validateUIField(Const.C_CODE_T_PAIEMENT,taTPaiement.getCodeTPaiement());
			selectedDocumentDTO.setCodeTPaiement(taTPaiement.getCodeTPaiement());
		}
		if(taCPaiementDoc!=null) {
			validateUIField(Const.C_CODE_C_PAIEMENT,taCPaiementDoc.getCodeCPaiement());
			selectedCPaiement.setCodeCPaiement(taCPaiementDoc.getCodeCPaiement());
		}

		if(taTTvaDoc!=null) {
			validateUIField(Const.C_CODE_T_TVA_DOC,taTTvaDoc.getCodeTTvaDoc());
			selectedDocumentDTO.setCodeTTvaDoc(taTTvaDoc.getCodeTTvaDoc());
		}
//		if(selectedEtatDocument!=null) {
//			validateUIField(Const.C_CODE_ETAT,selectedEtatDocument.getCodeEtat());
//			selectedDocumentDTO.setCodeEtat(selectedEtatDocument.getCodeEtat());
//			masterEntity.setTaEtat(selectedEtatDocument);
//		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTiers = null;
			taTiersDTO = null;
			taTPaiement = null;
			taTTvaDoc = null;
			taCPaiementDoc = null;
			//			taTEntite = null;
			if(selectedDocumentDTO.getCodeTiers()!=null && !selectedDocumentDTO.getCodeTiers().equals("")) {
				taTiers = taTiersService.findByCode(selectedDocumentDTO.getCodeTiers());
				taTiersDTO = mapperModelToUITiers.map(taTiers, TaTiersDTO.class);
			}
			if(selectedDocumentDTO.getCodeTPaiement()!=null && !selectedDocumentDTO.getCodeTPaiement().equals("")) {
				taTPaiement = taTPaiementService.findByCode(selectedDocumentDTO.getCodeTPaiement());
			}
			if(selectedCPaiement.getCodeCPaiement()!=null && !selectedCPaiement.getCodeCPaiement().equals("")) {
				taCPaiementDoc = taCPaiementService.findByCode(selectedCPaiement.getCodeCPaiement());
			}			

			if(selectedDocumentDTO.getCodeTTvaDoc()!=null && !selectedDocumentDTO.getCodeTTvaDoc().equals("")) {
				taTTvaDoc = taTTvaDocService.findByCode(selectedDocumentDTO.getCodeTTvaDoc());
			}
			if(masterEntity.getTaReglement()!=null){
				taReglement=masterEntity.getTaReglement();
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
				context.addMessage(null, new FacesMessage("Acompte", "actModifier")); 	 
			}
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			changementStepWizard(true);
		//}
	}
	public void actEnregistrer(ActionEvent actionEvent) {		
		try {
			masterEntity.calculeTvaEtTotaux();
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



			TaTLigne typeLigneCommentaire =  taTLigneService.findByCode(Const.C_CODE_T_LIGNE_C);
			masterEntity.setLegrain(false);
			List<TaLAcompte> listeLigneVide = new ArrayList<TaLAcompte>(); 
			for (TaLAcompte ligne : masterEntity.getLignes()) {
				ligne.setLegrain(false);
				if(ligne.ligneEstVide() && ligne.getNumLigneLDocument().compareTo(masterEntity.getLignes().size())==0) {
					listeLigneVide.add(ligne);
				} else {
					if(ligne.getTaArticle()==null) {
						ligne.setTaTLigne(typeLigneCommentaire);
					}
				}
			}

			//supression des lignes vides
			for (TaLAcompte taLBonReception : listeLigneVide) {
				masterEntity.getLignes().remove(taLBonReception);
			}

			//isa le 08/11/2016
			//j'ai rajouté cette réinitialisation tant que l'on enlève les lignes vides, sinon génère des trous dans la numérotation des lignes
			masterEntity.reinitialiseNumLignes(0, 1);
			masterEntity.setCreeReglement(creeReglement);
			if(masterEntity.getTaReglement()!=null) {
				//rajuste le montant à régler en fonction des changements dans le document
				masterEntity.getTaReglement().setNetTtcCalc(masterEntity.getNetTtcCalc());
			}
			//si pas déjà existant, crée un réglement lié à l'acompte
			if(masterEntity.getTaReglement()==null && creeReglement){
				masterEntity.setTaReglement(taAcompteService.creeRReglement(masterEntity, typePaiementDefaut, masterEntity.getTaReglement()));
				masterEntity.getTaReglement().setTaAcompte(masterEntity);
			}
			else if(masterEntity.getTaReglement()!=null && !creeReglement){
				TaReglement reglement= masterEntity.getTaReglement();
				taReglementService.remove(reglement);
				masterEntity.setTaReglement(null);
			}
			
			masterEntity = taAcompteService.mergeAndFindById(masterEntity,ITaAcompteServiceRemote.validationContext);
			
			taAcompteService.annuleCode(selectedDocumentDTO.getCodeDocument());


			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			autoCompleteMapDTOtoUI();
			selectedDocumentDTOs=new TaAcompteDTO[]{selectedDocumentDTO};


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
			

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Acompte", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acompte", e.getMessage()));
		}


		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Acompte", "actEnregistrer")); 
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			impressionDirectClient = false;
			miseADispositionCompteClient = false;
			envoyeParEmail = false;
			
			listePreferences= taPreferencesService.findByGroupe("acompte");
			selectedDocumentDTO = new TaAcompteDTO();
			selectedDocumentDTO.setCommentaire(rechercheCommentaireDefautDocument());
			masterEntity = new TaAcompte();
			masterEntity.setLegrain(true);
			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			selectedEtatDocument=null;
			autoCompleteMapDTOtoUI();

			initInfosDocument();


			//			selectedDocumentDTO.setCodeDocument(taBonReceptionService.genereCode()); //ejb
			//			validateUIField(Const.C_CODE_DOCUMENT,selectedDocumentDTO.getCodeDocument());//permet de verrouiller le code genere

			//			selectedDocumentDTO.setCodeDocument(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			Map<String, String> paramsCode = new LinkedHashMap<>();
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
			//			paramsCode.put(Const.C_NOMFOURNISSEUR, selectedDocumentDTO.getNomTiers());

			if(selectedDocumentDTO.getCodeDocument()!=null) {
				taAcompteService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			
			//			String newCode = taAcompteService.genereCode(paramsCode);
			//			if(newCode!=null && !newCode.equals("")){
			//				selectedDocumentDTO.setCodeDocument(newCode);
			//			}
			selectedDocumentDTO.setCodeDocument(taAcompteService.genereCode(paramsCode));
			changementTiers(true);
			updateReglements();
			
			//plus tard on peut créer une préférence pour gérer cela
			creeReglement=true;
			
			
			TaPreferences nb;
			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
			if(nb!=null && nb.getValeur() != null) {
				masterEntity.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
			}

//			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//			if(nb!=null && nb.getValeur() != null) {
//				masterEntity.setNbDecimalesQte(LibConversion.stringToInteger(nb.getValeur()));
//			}

			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);

			
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);
			
			
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Acompte", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			//		} catch (FinderException e) {
			//			e.printStackTrace();
		}
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ACOMPTE);
		b.setTitre("Acompte");
		b.setTemplate("documents/acompte.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ACOMPTE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ACOMPTE);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		//		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Acompte", 
					"Nouveau"
					)); 
		}
	} 
	public void actSupprimerLigne(ActionEvent actionEvent) {
		if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT)) {
			  if(selectedLigneJSF.getDto().getIdLDocument() != null) {
//					//List<TaLEcheance> liste  = taLEcheanceService.findAllByIdLFacture(selectedLigneJSF.getDto().getIdLDocument());
//					List<TaLigneALigneEcheance> liste  = taLigneALigneEcheanceService.findAllByIdLDocumentAndTypeDoc(selectedLigneJSF.getDto().getIdLDocument(), TaAcompte.TYPE_DOC);
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
		TaAcompte obj = new TaAcompte();
		try {
			if(autorisationLiaisonComplete()) {
			if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
				obj = taAcompteService.findByIDFetch(selectedDocumentDTO.getId());
			}
			
			for (TaReglementDTOJSF l : listeTaRReglementDTOJSF) {
				@CodeToCheck(detail = "Je ne comprends pas pourquoi on utilise cette procédure pour au final recuperer un règlement", rapporteur = "isa", attribution = "", ticket = "")
				IDocumentTiers doc =duplicationDocumentBean.getOuvertureDocumentBean().documentValide(l.getDto().getCodeDocument(),TaReglement.TYPE_DOC);
				if(doc!=null) {
					((TaReglement) doc).setTaAcompte(null);
					taReglementService.merge((TaReglement) doc);
				}
			}
			taAcompteService.remove(obj);
			values.remove(selectedDocumentDTO);

			if(!values.isEmpty()) {
				selectedDocumentDTO = values.get(0);
				selectedDocumentDTOs= new TaAcompteDTO[]{selectedDocumentDTO};
				updateTab();
			}else{
				selectedDocumentDTO = new TaAcompteDTO();
				selectedDocumentDTOs= new TaAcompteDTO[]{};	
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Acompte", "actSupprimer"));
			}
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acompte", e.getMessage()));
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

		selectedDocumentDTO=new TaAcompteDTO();
		selectedDocumentDTOs=new TaAcompteDTO[]{selectedDocumentDTO};
		updateTab();

		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeAcompte').filter()");

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Acompte", "actFermer")); 
		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Acompte", "actImprimer")); 
		}
		try {

			//Fiche_devis.rptdesign&=7332&typeTraite=null&PageBreakTotaux=27&ape=&ParamChoix=choix 1
			// &PageBreakMaxi=36&ParamCorr=null&CoupureLigne=54
			//		&nomEntreprise=EARL DE GRINORD&capital=&rcs=&siret=&__document=doc1469798570882&__format=pdf

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaAcompte doc =taAcompteService.findById(selectedDocumentDTO.getId());
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );
			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			//mapEdition.put("LibelleJournalTva", taTTvaDoc.getLibelleEdition());

			mapEdition.put("lignes", masterEntity.getLignes());

			//sessionMap.put("doc", taAcompteService.findById(selectedDocumentDTO.getId()));
			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("acompte");
			mapParametreEdition.put("preferences", liste);
//			mapParametreEdition.put("gestionLot", gestionLot);
			
			mapParametreEdition.put("Theme", "defaultTheme");
			mapParametreEdition.put("Librairie", "bdgFactTheme1");
			mapEdition.put("param", mapParametreEdition);
			
			sessionMap.put("edition", mapEdition);


			//			session.setAttribute("tiers", taTiersService.findById(selectedDocumentDTO.getId()));
			System.out.println("AcompteController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    

	public void actImprimerListeDesAcomptes(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesAcomptes", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ACOMPTE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ACOMPTE);
		b.setTitre("Acompte");
		b.setTemplate("documents/acompte.xhtml");
		b.setIdTab(LgrTab.ID_TAB_ACOMPTE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_ACOMPTE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		
		ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
		if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
		changementStepWizard(false);	
		
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
			TaLAcompteDTO dtoTemp =new TaLAcompteDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taLAcompteService.validateDTOProperty(dtoTemp, nomChamp, ITaLAcompteServiceRemote.validationContext );

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
		//			TaAcompteDTO dtoTemp = new TaAcompteDTO();
		//			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
		//			taBonReceptionService.validateDTOProperty(dtoTemp, nomChamp, ITaAcompteServiceRemote.validationContext );
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

			if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)||nomChamp.equals(Const.C_TX_REM_TTC_DOCUMENT)){
				if(value==null || value.equals(""))value=BigDecimal.ZERO;
			}
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaAcompteDTO>> violations = factory.getValidator().validateValue(TaAcompteDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaAcompteDTO> cv : violations) {
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
			//taBonReceptionService.validateDTOProperty(selectedDocumentDTO,Const.C_CODE_TIERS,  ITaAcompteServiceRemote.validationContext );

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
					PrimeFaces.current().executeScript("PF('wVdialogQte2Acompte').show()");
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
					((TaAcompteDTO)selectedDocumentDTO).setLibelleDocument("Acompte N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);										
					if(!disableTtc() && !disableTtcSiDocSansTVA()){
						masterEntity.setTtc(entity.getTtcTiers());
						((TaAcompteDTO)selectedDocumentDTO).setTtc(LibConversion.intToBoolean(masterEntity.getTtc()));
					}									
					changementTiers(true);
				}
			}else if(nomChamp.equals(Const.C_NUM_LOT)) {

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
				masterEntityLigne.setTaArticle(entity);
				if(changementArticleLigne) {
					if(entity!=null)selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
					//					selectedLigneJSF.getDto().setDluo(LibDate.incrementDate(selectedLigneJSF.getDateDocument(), LibConversion.stringToInteger(entity.getParamDluo(), 0)+1  , 0, 0));
					//					selectedLigneJSF.getDto().setLibelleLot(entity.getLibellecArticle());
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
			}  else	if(nomChamp.equals(Const.C_TX_REM_HT_DOCUMENT)) {
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
			if(((TaAcompteDTO)selectedDocumentDTO).getCodeDocument()!=null) {
				taInfosDocument = taInfosDocumentService.findByCodeAcompte(((TaAcompteDTO)selectedDocumentDTO).getCodeDocument());
			} else {
				taInfosDocument = new TaInfosAcompte();
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
			selectedCPaiement = new TaCPaiementDTO();
			TaInfosAcompte taInfosDocument = null;
			if (masterEntity != null) {
				if (masterEntity.getCodeDocument()!=null&& masterEntity.getCodeDocument() != "")
					taInfosDocument = taInfosDocumentService
					.findByCodeAcompte(masterEntity.getCodeDocument());
				else
					taInfosDocument = new TaInfosAcompte();


				taCPaiementDoc = null;
				List<TaCPaiement> listeCPaiement=taCPaiementService.rechercheParType(TaAcompte.TYPE_DOC);
				if(listeCPaiement!=null && !listeCPaiement.isEmpty())taCPaiementDoc=listeCPaiement.get(0);
				
				if (taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_ACOMPTE) != null
						&& taTCPaiementService.findByCode(
								TaTCPaiement.C_CODE_TYPE_ACOMPTE).getTaCPaiement() != null) {
					taCPaiementDoc = taTCPaiementService.findByCode(
							TaTCPaiement.C_CODE_TYPE_ACOMPTE).getTaCPaiement();
				}
				int report = 0;
				int finDeMois = 0;


				if (masterEntity.getTaTiers() != null) {
					if (masterEntity.getTaTiers().getTaTPaiement() != null) {
						if (masterEntity.getTaTiers().getTaTPaiement()
								.getReportTPaiement() != null)
							report = masterEntity.getTaTiers().getTaTPaiement()
							.getReportTPaiement();
						if (masterEntity.getTaTiers().getTaTPaiement()
								.getFinMoisTPaiement() != null)
							finDeMois = masterEntity.getTaTiers()
							.getTaTPaiement().getFinMoisTPaiement();
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


				if (taCPaiementDoc != null  )
					liste.add(
							mapperModelToUICPaiementInfosDocument.map(taCPaiementDoc,
									classModelCPaiement));
				masterEntity.setTaInfosDocument(taInfosDocument);
				// ajout de l'adresse de livraison inscrite dans l'infos facture
				if (taInfosDocument != null) {
					if (recharger)
						liste.add(
								mapperModelToUIInfosDocVersCPaiement.map(
										masterEntity.getTaInfosDocument(),
										classModelCPaiement));
					else
						liste.addFirst(
								mapperModelToUIInfosDocVersCPaiement.map(
										masterEntity.getTaInfosDocument(),
										classModelCPaiement));
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

	public void calculDateEcheance() {
		calculDateEcheance(false);
	}

	public void calculDateEcheance(Boolean appliquer) {
		if(selectedDocumentDTO!=null){
			if(((TaAcompteDTO)selectedDocumentDTO).getId()==0|| appliquer) {

				Integer report = null;
				Integer finDeMois = null;
				if(((TaCPaiementDTO)selectedCPaiement)!=null /*&& ((TaCPaiementDTO)selectedCPaiement).getCodeCPaiement()!=null*/) {  
					if(((TaCPaiementDTO)selectedCPaiement).getReportCPaiement()!=null)
						report = ((TaCPaiementDTO)selectedCPaiement).getReportCPaiement();
					if(((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement()!=null)
						finDeMois = ((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement();
				}
				masterEntity.setDateDocument(selectedDocumentDTO.getDateDocument());
				((TaAcompteDTO)selectedDocumentDTO).setDateEchDocument(taAcompteService.calculDateEcheance(masterEntity,report, finDeMois));
			}
		}
	}

	@Override
	public void calculTypePaiement(boolean recharger) {
		// TODO Auto-generated method stub
		initialisationDesCPaiement(recharger);
	}

	@Override
	public void IHMmodel(TaLAcompteDTOJSF dtoJSF, TaLAcompte ligne) {
		LgrDozerMapper<TaLAcompte,TaLAcompteDTO> mapper = new LgrDozerMapper<TaLAcompte,TaLAcompteDTO>();
		TaLAcompteDTO dto = dtoJSF.getDto();

		dto = (TaLAcompteDTO) mapper.map(ligne, TaLAcompteDTO.class);
		dtoJSF.setDto(dto);
		//		dtoJSF.setTaLot(ligne.getTaLot());
		//		if(dtoJSF.getArticleLotEntrepotDTO()==null) {
		//			dtoJSF.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
		//		}
		//		if(ligne.getTaLot()!=null) {
		//			dtoJSF.getArticleLotEntrepotDTO().setNumLot(ligne.getTaLot().getNumLot());
		//		}
		dtoJSF.setTaArticle(ligne.getTaArticle());
		if(ligne.getTaArticle()!=null){
			dtoJSF.setTaRapport(ligne.getTaArticle().getTaRapportUnite());
		}
		//		dtoJSF.setTaEntrepot(ligne.getTaEntrepot());
		if(ligne.getU1LDocument()!=null) {
			dtoJSF.setTaUnite1(new TaUnite());
			dtoJSF.getTaUnite1().setCodeUnite(ligne.getU1LDocument());
		}
		if(ligne.getU2LDocument()!=null) {
			dtoJSF.setTaUnite2(new TaUnite());
			dtoJSF.getTaUnite2().setCodeUnite(ligne.getU2LDocument());
		}
		dtoJSF.updateDTO(false);

	}

	@Override
	public void actAppliquerCPaiement() throws Exception {
		calculDateEcheance(true);
		//taAcompteService.mettreAJourDateEcheanceReglements(masterEntity);
		actModifier();
	}

	@Override
	public void updateTab(){
		creeReglement=true;
		super.updateTab();
		masterEntity.setTaReglement(taReglementService.findByCodeAcompte(masterEntity.getCodeDocument()));
		updateReglements();
	}



	public boolean isCreeReglement() {
		return creeReglement;
	}

	public void setCreeReglement(boolean creeReglement) {
		this.creeReglement = creeReglement;
	}

	public void updateReglements(){
		listeTaRReglementDTOJSF.clear();
		creeReglement=masterEntity.getTaReglement()!=null;
		masterEntity.setCreeReglement(creeReglement);
		if(masterEntity.getTaReglement()!=null){
			TaReglementDTOJSF obj = new TaReglementDTOJSF();
			obj=remplirIHMReglement(masterEntity.getTaReglement(), null);
			listeTaRReglementDTOJSF.add(obj);			
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
}
