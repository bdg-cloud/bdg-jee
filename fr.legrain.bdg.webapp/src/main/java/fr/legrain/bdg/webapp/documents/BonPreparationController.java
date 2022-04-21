package fr.legrain.bdg.webapp.documents;

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
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
//import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosPreparationServiceRemote;
//import fr.legrain.bdg.documents.service.remote.ITaLBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.document.dto.TaLPreparationDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaPreparationDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosPreparation;
import fr.legrain.document.model.TaLPreparation;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
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
public class BonPreparationController extends AbstractDocumentController<TaPreparation,TaPreparationDTO,TaLPreparation,TaLPreparationDTO,TaLPreparationDTOJSF,TaInfosPreparation> {  

	@Inject @Named(value="ouvertureTiersBean")
	private OuvertureTiersBean ouvertureTiersBean;
	
	@Inject @Named(value="ouvertureArticleBean")
	private OuvertureArticleBean ouvertureArticleBean;
	
	

	private @EJB ITaPreparationServiceRemote taPreparationService;
	private @EJB ITaInfosPreparationServiceRemote taInfosDocumentService;
	private @EJB ITaLPreparationServiceRemote taLPreparationService;
	private List<TaRDocumentDTO> docLie;
	private List<TaLigneALigneDTO> ligneLie;


	public BonPreparationController() {  

	}  

	@PostConstruct
	public void postConstruct(){
		super.postConstruct();
		listePreferences= taPreferencesService.findByGroupe("preparation");
		nomDocument="Bon de préparation";
		setTaDocumentService(taPreparationService);
		setTaLDocumentService(taLPreparationService);
		setTaInfosDocumentService(taInfosDocumentService);
		
		stepSynthese = "idSynthesePreparation";
		stepEntete = "idEntetePreparation";
		stepLignes = "idLignesPreparation";
		stepTotaux = "idTotauxFormPreparation";
		stepValidation = "idValidationFormPreparation";
		idMenuBouton = "form:tabView:idMenuBoutonPreparation";
		wvEcran = LgrTab.WV_TAB_PREPARATION;
		classeCssDataTableLigne = "myclassPreparation";
		refresh();
	}

	public void refresh(){
//		values = taPreparationService.selectAllDTO();
		values = taPreparationService.findAllLight();
		valuesLigne = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaLPreparationDTOJSF> IHMmodel() {
		LinkedList<TaLPreparation> ldao = new LinkedList<TaLPreparation>();
		LinkedList<TaLPreparationDTOJSF> lswt = new LinkedList<TaLPreparationDTOJSF>();

		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLPreparation,TaLPreparationDTO> mapper = new LgrDozerMapper<TaLPreparation,TaLPreparationDTO>();
			TaLPreparationDTO dto = null;
			for (TaLPreparation o : ldao) {
				TaLPreparationDTOJSF t = new TaLPreparationDTOJSF();
				dto = (TaLPreparationDTO) mapper.map(o, TaLPreparationDTO.class);
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
				t.setTaEntrepot(o.getTaEntrepot()); //@@ champs à ajouter aux devis
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
//			ligneLie=rechercheSiLigneDocLie(selectedLigneJSF);
		}
		return lswt;
	}
	public boolean disabledLigne() {
		return selectedLigneJSF!=null && (selectedLigneJSF.ligneLieeFils!=null || selectedLigneJSF.ligneLieeMere!=null);
	}

	public void actEnregistrerLigne(ActionEvent actionEvent) {

		try {
			selectedLigneJSF.updateDTO(false);

			LgrDozerMapper<TaLPreparationDTO,TaLPreparation> mapper = new LgrDozerMapper<TaLPreparationDTO,TaLPreparation>();
			mapper.map((TaLPreparationDTO) selectedLigneJSF.getDto(),masterEntityLigne);
	

			
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
			context.addMessage(null, new FacesMessage("Preparation", "actEnregisterLigne")); 
		}
	}


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taPreparationService.annuleCode(selectedDocumentDTO.getCodeDocument());
				}
				masterEntity=new TaPreparation();
				selectedDocumentDTO=new TaPreparationDTO();
				
				if(values.size()>0)	selectedDocumentDTO = values.get(0);

				onRowSelect(null);
				
				valuesLigne=IHMmodel();
				initInfosDocument();
				break;
			case C_MO_EDITION:
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					masterEntity = taPreparationService.findByIDFetch(selectedDocumentDTO.getId());
					selectedDocumentDTO=taPreparationService.findByIdDTO(selectedDocumentDTO.getId());
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
			context.addMessage(null, new FacesMessage("Preparation", "actAnnuler")); 
		}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void autoCompleteMapUIToDTO() {

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
		if(taTransporteurDTO!=null) {
			validateUIField(Const.C_CODE_TRANSPORTEUR,taTransporteurDTO);
			selectedDocumentDTO.setCodeTransporteur(taTransporteurDTO.getCodeTransporteur());
		}
	}

	public void autoCompleteMapDTOtoUI() {
		try {
			taTiers = null;
			taTiersDTO = null;
			taTPaiement = null;
			taTTvaDoc = null;
			taCPaiementDoc = null;
			taTransporteur = null;
			taTransporteurDTO = null;

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
			if(selectedDocumentDTO.getCodeTransporteur()!=null && !selectedDocumentDTO.getCodeTransporteur().equals("")) {
				taTransporteurDTO = taTransporteurService.findByCodeDTO(selectedDocumentDTO.getCodeTransporteur());
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
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
			List<TaLPreparation> listeLigneVide = new ArrayList<TaLPreparation>(); 
			for (TaLPreparation ligne : masterEntity.getLignes()) {
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
			for (TaLPreparation taLBonReception : listeLigneVide) {
				masterEntity.getLignes().remove(taLBonReception);
			}
			
			//isa le 08/11/2016
			//j'ai rajouté cette réinitialisation tant que l'on enlève les lignes vides, sinon génère des trous dans la numérotation des lignes
			masterEntity.reinitialiseNumLignes(0, 1);
			
			
			masterEntity = taPreparationService.mergeAndFindById(masterEntity,ITaPreparationServiceRemote.validationContext);
			taPreparationService.annuleCode(selectedDocumentDTO.getCodeDocument());

			
			mapperModelToUI.map(masterEntity, selectedDocumentDTO);
			autoCompleteMapDTOtoUI();
			selectedEtatDocument=null;
			if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			selectedDocumentDTOs=new TaPreparationDTO[]{selectedDocumentDTO};

			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedDocumentDTO);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			modeEcranLigne.setMode(EnumModeObjet.C_MO_CONSULTATION);
			updateTab();
			if(impressionDirect) {
				//préparation des valeurs en session pour l'edition
				actImprimer(null);
				/*
				 * l'ouverture de la fenêtre est déclenché par le oncomplete sur le bouton enregistrer,
				 * la checkbox du dernier step (Totaux) est liée à la même valeur qu'une autre checkbox invisible dans le step 1
				 * le oncomplete est déclenché après les update ajax, or l'action suivante est "wizardDocument.setStep(this.stepEntete);"
				 * donc le oncomplete s'execute alors que nous somme déjà revenu sur le step 1
				 * Il faut donc reprendre cette valeur si on souhaite y accéder à partir de PF('moncomposantcheckbox') dans le javascript du oncomplete
				 */
			}
			
			
			
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);
			


		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preparation", e.getMessage()));
		}


		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Preparation", "actEnregistrer")); 
		}
	}


    
	public void actInserer(ActionEvent actionEvent) {
		try {
			listePreferences= taPreferencesService.findByGroupe("Preparation");
			selectedDocumentDTO = new TaPreparationDTO();
			selectedDocumentDTO.setCommentaire(rechercheCommentaireDefautDocument());
			masterEntity = new TaPreparation();
			masterEntity.setLegrain(true);
			
			//Tous les documents ont cet état par défaut vue avec philippe le 06/08/2019
//			TaEtat etat = taEtatService.findByCode(TaEtat.ETAT_ENCOURS_NON_TRANSFORME);
			TaEtat etat = taPreparationService.rechercheEtatInitialDocument();
			masterEntity.addREtat(etat);
			
			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			selectedEtatDocument=null;
			autoCompleteMapDTOtoUI();
			if(masterEntity.getTaREtat()!=null)selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			initInfosDocument();


			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			Map<String, String> paramsCode = new LinkedHashMap<>();
			paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selectedDocumentDTO.getDateDocument()));
			
			if(selectedDocumentDTO.getCodeDocument()!=null) {
				taPreparationService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			
			selectedDocumentDTO.setCodeDocument(taPreparationService.genereCode(paramsCode));
			changementTiers(true);


			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			
			TaPreferences nb;
			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
			if(nb!=null && nb.getValeur() != null) {
				masterEntity.setNbDecimalesPrix(LibConversion.stringToInteger(nb.getValeur()));
			}

//			nb = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//			if(nb!=null && nb.getValeur() != null) {
//				masterEntity.setNbDecimalesQte(LibConversion.stringToInteger(nb.getValeur()));
//			}
			
			ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
			if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
			changementStepWizard(false);
			

			
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Preparation", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}


	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_PREPARATION);
		b.setTitre("Bon de Préparation");
		b.setTemplate("documents/preparation.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PREPARATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PREPARATION);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

//		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Preparation", 
					"Nouveau"
					)); 
		}
	} 


//	public boolean autoriseSuppressionLigne(boolean silencieux) {
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
//					if(suppressionDocInterdit==null && doc.getTypeDocument().equals(TaBonReception.TYPE_DOC))
//						suppressionDocInterdit=doc;
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

	public boolean autoriseSuppressionLigne() {
		return autoriseSuppressionLigne(false,selectedLigneJSF);
	}

	public void actSupprimerLigne(ActionEvent actionEvent) {
		try {
			if(autoriseSuppressionLigne()) {
				super.actSupprimerLigne(actionEvent);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
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
//					if(suppressionDocInterdit==null && doc.getTypeDocument().equals(TaBonReception.TYPE_DOC))
//						suppressionDocInterdit=doc;
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

	
	
	public void actSupprimer(ActionEvent actionEvent) {
		TaPreparation obj = new TaPreparation();
		try {
			if(autorisationLiaisonComplete()) {
				if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
					obj = taPreparationService.findByIDFetch(selectedDocumentDTO.getId());
				}

				taPreparationService.remove(obj);
				values.remove(selectedDocumentDTO);

				if(!values.isEmpty()) {
					selectedDocumentDTO = values.get(0);
					selectedDocumentDTOs= new TaPreparationDTO[]{selectedDocumentDTO};
					updateTab();
				}else{
					selectedDocumentDTO = new TaPreparationDTO();
					selectedDocumentDTOs= new TaPreparationDTO[]{};	
				}

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Preparation", "actSupprimer"));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preparation", e.getMessage()));
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
	
		selectedDocumentDTO=new TaPreparationDTO();
		selectedDocumentDTOs=new TaPreparationDTO[]{selectedDocumentDTO};
		updateTab();
		


		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Preparation", "actFermer")); 
		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Preparation", "actImprimer")); 
		}
		try {
			

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			TaPreparation doc =taPreparationService.findById(selectedDocumentDTO.getId());
			doc.calculeTvaEtTotaux();
			
			mapEdition.put("doc",doc );
			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			
			mapEdition.put("lignes", masterEntity.getLignes());
			
			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			List<TaPreferences> liste= taPreferencesService.findByGroupe("preparation");
			mapParametreEdition.put("preferences", liste);
//			mapParametreEdition.put("gestionLot", gestionLot);
			mapParametreEdition.put("Theme", "defaultTheme");
			mapParametreEdition.put("Librairie", "bdgFactTheme1");
			
			mapEdition.put("param", mapParametreEdition);
			sessionMap.put("edition", mapEdition);


			System.out.println("PreparationController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
	
	public void actImprimerListeDesPreparation(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesPreparation", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_PREPARATION);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void updateTab(){

		try {	
			super.updateTab();
			autoriseSuppressionLigne(true);
			selectedEtatDocument=null;
			if(masterEntity!=null && masterEntity.getTaREtat()!=null)
				selectedEtatDocument=masterEntity.getTaREtat().getTaEtat();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Preparation", 
						"Chargement du Bon de préparation N°"+selectedDocumentDTO.getCodeTiers()
						)); 
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_PREPARATION);
		b.setTitre("Bon de préparation");
		b.setTemplate("documents/preparation.xhtml");
		b.setIdTab(LgrTab.ID_TAB_PREPARATION);
		b.setStyle(LgrTab.CSS_CLASS_TAB_PREPARATION);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		if(wizardDocument!=null) {
			wizardDocument.setStep(this.stepEntete);
		}		
		updateTab();
		scrollToTop();
	} 

	
	public void validateLignesDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String msg = "";

		try {

			String nomChamp =  (String) component.getAttributes().get("nomChamp");


			validateUIField(nomChamp,value);
			TaLPreparationDTO dtoTemp =new TaLPreparationDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taLPreparationService.validateDTOProperty(dtoTemp, nomChamp, ITaLPreparationServiceRemote.validationContext );


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
			Set<ConstraintViolation<TaPreparationDTO>> violations = factory.getValidator().validateValue(TaPreparationDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaPreparationDTO> cv : violations) {
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
					PrimeFaces.current().executeScript("PF('wVdialogQte2BonPreparation').show()");
				}

			}
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("PreparationController.autcompleteSelection() : "+nomChamp);

		if(value!=null) {
		if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
		if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;	
		}
		validateUIField(nomChamp,value);
	}

	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity = null;
				if(value!=null){
					if(value instanceof TaTiersDTO){
						entity = taTiersService.findByCode(((TaTiersDTO)value).getCodeTiers());
					}else{
						entity = taTiersService.findByCode((String)value);
					}
					
					changement=!entity.equalsCode(masterEntity.getTaTiers());


				}
				masterEntity.setTaTiers(entity);
				if(changement){
					String nomTiers=masterEntity.getTaTiers().getNomTiers();
					((TaPreparationDTO)selectedDocumentDTO).setLibelleDocument("Bon de préparation N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);										
					if(!disableTtc() && !disableTtcSiDocSansTVA()){
						masterEntity.setTtc(entity.getTtcTiers());
						((TaPreparationDTO)selectedDocumentDTO).setTtc(LibConversion.intToBoolean(masterEntity.getTtc()));
					}									
					changementTiers(true);
				}
			}else if(nomChamp.equals(Const.C_NUM_LOT)) {
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
//		else if(nomChamp.equals(Const.C_CODE_TRANSPORTEUR)) {
//			TaTransporteur entity = null;
//			if(value!=null){
//				if(value instanceof TaTransporteurDTO){
//					entity = taTransporteurService.findByCode(((TaTransporteurDTO)value).getCodeTransporteur());
//				}else{
//					entity = taTransporteurService.findByCode((String)value);
//				}
//				
//				changement=!entity.equalsCode(masterEntity.getTaTiers());
//
//
//			}
//			masterEntity.setTaTransporteur(entity);
//		}					
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
			if(((TaPreparationDTO)selectedDocumentDTO).getCodeDocument()!=null) {
				taInfosDocument = taInfosDocumentService.findByCodePreparation(((TaPreparationDTO)selectedDocumentDTO).getCodeDocument());
			} else {
				taInfosDocument = new TaInfosPreparation();
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
		TaInfosPreparation taInfosDocument = null;
		if (masterEntity != null) {
			if (masterEntity.getCodeDocument()!=null&& masterEntity.getCodeDocument() != "")
				taInfosDocument = taInfosDocumentService
						.findByCodePreparation(masterEntity.getCodeDocument());
			else
				taInfosDocument = new TaInfosPreparation();

			
			taCPaiementDoc = null;
			List<TaCPaiement> listeCPaiement=taCPaiementService.rechercheParType(TaPreparation.TYPE_DOC);
			if(listeCPaiement!=null && !listeCPaiement.isEmpty())taCPaiementDoc=listeCPaiement.get(0);
			

			if (taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_BON_COMMANDE_ACHAT) != null
					&& taTCPaiementService.findByCode(
							TaTCPaiement.C_CODE_TYPE_BON_COMMANDE_ACHAT).getTaCPaiement() != null) {
				taCPaiementDoc = taTCPaiementService.findByCode(
						TaTCPaiement.C_CODE_TYPE_BON_COMMANDE_ACHAT).getTaCPaiement();
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

	
	public void calculDateEcheance(Boolean appliquer) {
		if(selectedDocumentDTO!=null){
		if(((TaPreparationDTO)selectedDocumentDTO).getId()==0|| appliquer) {

			Integer report = null;
			Integer finDeMois = null;
			if(((TaCPaiementDTO)selectedCPaiement)!=null /*&& ((TaCPaiementDTO)selectedCPaiement).getCodeCPaiement()!=null*/) { 
				if(((TaCPaiementDTO)selectedCPaiement).getReportCPaiement()!=null)
					report = ((TaCPaiementDTO)selectedCPaiement).getReportCPaiement();
				if(((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement()!=null)
					finDeMois = ((TaCPaiementDTO)selectedCPaiement).getFinMoisCPaiement();
			}
			masterEntity.setDateDocument(selectedDocumentDTO.getDateDocument());
			((TaPreparationDTO)selectedDocumentDTO).setDateEchDocument(taPreparationService.calculDateEcheance(masterEntity,report, finDeMois));
		}
		}
	}
	

	
	public void actAppliquerCPaiement() throws Exception {
		calculDateEcheance(true);
		actModifier();
	}

	@Override
	public void IHMmodel(TaLPreparationDTOJSF t, TaLPreparation ligne) {
		LgrDozerMapper<TaLPreparation,TaLPreparationDTO> mapper = new LgrDozerMapper<TaLPreparation,TaLPreparationDTO>();
		TaLPreparationDTO dto = t.getDto();

		dto = (TaLPreparationDTO) mapper.map(ligne, TaLPreparationDTO.class);
		t.setDto(dto);

		t.setTaArticle(ligne.getTaArticle());
		if(ligne.getTaArticle()!=null){
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
		t.updateDTO(false);
		List<TaLigneALigneDTO>  l=rechercheSiLigneDocLie(t);
		if(l!=null) {
			for (TaLigneALigneDTO ll : l) {
				if(ll.getIdLigneSrc().equals(ll.getIdLDocumentSrc())) {
					if(t.getLigneLieeFils()==null)t.setLigneLieeFils(new LinkedList<>());
					t.getLigneLieeFils().add(ll);
				}
				else { 
					if(t.getLigneLieeMere()==null)t.setLigneLieeMere(new LinkedList<>());
					t.getLigneLieeMere().add(ll);
				}							
			}
		}
	}

	@Override
	public void calculTypePaiement(boolean recharger) {
		// TODO Auto-generated method stub
		
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
		super.actInsererLigne(actionEvent);

		TaEtat etat = taPreparationService.etatLigneInsertion(masterEntity);
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
	
	
	public void onRowEdit(RowEditEvent event) {
		try {
			if(autoriseSuppressionLigne(false, selectedLigneJSF)){
				super.onRowEdit(event);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enregistrement document", e.getMessage()));	
			context.validationFailed();
		}
}

	
//	public TaEtat rechercheEtatInitialDocument() {
//		try {
//			return taEtatService.documentBrouillon(null);
//		} catch (Exception e) {
//			return null;
//		}
//
//	}

	public void actValiderDocument() {
		int position=1;
		int positionEnCours=taEtatService.documentEncours(null).getPosition();
		if(masterEntity!=null) {
			if(masterEntity.getTaREtat()==null ||masterEntity.getTaREtat().getTaEtat()==null  ||(masterEntity.getTaREtat().getTaEtat().getTaTEtat()!=null && masterEntity.getTaREtat().getTaEtat().getPosition().compareTo(positionEnCours)<0)) {
				masterEntity.addREtat(taEtatService.documentEncours(null));
				TaEtat etat = masterEntity.getTaREtat().getTaEtat();
				if(etat!=null)position=etat.getPosition();
				for (TaLPreparation l : masterEntity.getLignes()) {
					l.addREtatLigneDoc(etat);
//					if(l.getTaREtatLigneDocument()==null) {
//						TaREtatLigneDocument taREtat = new TaREtatLigneDocument();
//						taREtat.setTaEtat(etat);
//						taREtat.setTaLPreparation(l);
//						l.setTaREtatLigneDocument(taREtat);
//					}
//					if(l.getTaREtatLigneDocument()!=null  && l.getTaREtatLigneDocument().getTaEtat()!=null  && l.getTaREtatLigneDocument().getTaEtat().getPosition().compareTo(position)<0) {
////						l.removeTaREtatLigneDoc(l.getTaREtatLigneDocument());
//						//mettre l'ancien dans historique
//						TaREtatLigneDocument taREtat = l.getTaREtatLigneDocument();
//
//						if(taREtat!=null) {
//						//mettre l'ancien dans historique
//						TaHistREtatLigneDocument taHist = new TaHistREtatLigneDocument();
//						taHist.setTaEtat(taREtat.getTaEtat());
//						taHist.setTaLPreparation(taREtat.getTaLPreparation());
//						l.getTaHistREtatLigneDocuments().add(taHist);
//						}
//						
//						taREtat.setTaEtat(masterEntity.getTaEtat());
//						taREtat.setTaLPreparation(l);
//						l.setTaREtatLigneDocument(taREtat);
//					}
				}
				masterEntity=taPreparationService.merge(masterEntity);
				updateTab();
			}
		}

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

	public void actDialogGenerationDocument(ActionEvent actionEvent) {

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String,ParamAfficheChoixGenerationDocument> mapEdition = new HashMap<String,ParamAfficheChoixGenerationDocument>();
		ParamAfficheChoixGenerationDocument paramGeneration=new ParamAfficheChoixGenerationDocument();
		paramGeneration.setCodeDocument(masterEntity.getCodeDocument());
		paramGeneration.setTypeSrc(masterEntity.getTypeDocument());
		paramGeneration.setDocumentSrc(masterEntity);
//		paramGeneration.setDateDocument(masterEntity.getDateDocument());
		for (TaLPreparation l : masterEntity.getLignes()) {
			l.setGenereLigne(true);
		}
		paramGeneration.setCodeTiers(masterEntity.getTaTiers().getCodeTiers());
		paramGeneration.setGenerationModele(false);
		paramGeneration.setTitreFormulaire("Génération d'un document à partir du document : "+masterEntity.getCodeDocument());
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}

}
