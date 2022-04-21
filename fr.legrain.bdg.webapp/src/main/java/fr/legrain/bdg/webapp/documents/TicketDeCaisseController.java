package fr.legrain.bdg.webapp.documents;

import java.io.File;
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

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TaTva;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaSessionCaisseServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosTicketDeCaisseServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLTicketDeCaisseServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.osgi.ConstActionInterne;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.preferences.divers.ConstPreferencesTiers;
import fr.legrain.bdg.tiers.service.remote.ITaTEmailServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTelServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.EditionParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.paiement.PaiementCbController;
import fr.legrain.bdg.webapp.paiement.PaiementParam;
import fr.legrain.bdg.webapp.reglementMultiple.TaReglementDTOJSF;
import fr.legrain.birt.AnalyseFileReport;
import fr.legrain.caisse.dto.TaSessionCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.caisse.model.TaSessionCaisse;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLTicketDeCaisseDTO;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneEcheanceDTO;
import fr.legrain.document.dto.TaRDocumentDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosTicketDeCaisse;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaMiseADisposition;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.email.service.EnvoiEmailService;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.LibExecLinux;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
//import fr.legrain.tiers.dto.AdresseInfosFacturationDTO;
import fr.legrain.tiers.dto.AdresseInfosLivraisonDTO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.tiers.model.TaTEmail;
import fr.legrain.tiers.model.TaTTel;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class TicketDeCaisseController extends AbstractDocumentController<TaTicketDeCaisse,TaTicketDeCaisseDTO,TaLTicketDeCaisse,TaLTicketDeCaisseDTO,TaLTicketDeCaisseDTOJSF,TaInfosTicketDeCaisse> {  
	
	public static final String MESSAGE_MULTIPLE_REGLEMENT   = "Document réglé en plusieurs fois";
	public static final String MESSAGE_SIMPLE_REGLEMENT   = "";
	
	private @EJB ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	private @EJB ITaSessionCaisseServiceRemote taSessionCaisseService;
	
	private @EJB ITaAvoirServiceRemote taAvoirService;
	private @EJB ITaReglementServiceRemote taReglementService;
	
	private @EJB ITaFamilleServiceRemote taFamilleService;
	protected @EJB ITaTEmailServiceRemote taTEmailService;
	protected @EJB ITaTTelServiceRemote taTTelService;
	
	private @EJB ITaInfosTicketDeCaisseServiceRemote taInfosTicketDeCaisseService;
	private @EJB ITaLTicketDeCaisseServiceRemote taLTicketDeCaisseService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	//private @EJB LgrMailjetService lgrMailjetService;
	@EJB private EnvoiEmailService emailServiceFinder;

	private boolean differenceReglementResteARegle=false;
    private int countAvoirsAndReglements=0;
    
    private TaFamilleDTO selectedFamilleDTO = null;
    private TaArticleDTO selectedArticleDTO = null;
    private List<TaFamilleDTO> listeFamilleArticleCaisse;
    private List<TaArticleDTO> listeArticleCaisse;
    
    private String libellePaiement;
    private BigDecimal especesRecuClient;
    private BigDecimal especesARendreClient;
    
    private String zoneSaisie;
    private boolean modeTactile = true;
    private boolean modeManuel = true;
    private boolean afficheCaisse = true;
    private TaArticleDTO taArticleDTO;
    
    private TaSessionCaisse taSessionCaisse;
    private TaSessionCaisseDTO taSessionCaisseDTO;
    
    private TaFondDeCaisse taFondDeCaisseOuverture = new TaFondDeCaisse();
    private TaFondDeCaisse taFondDeCaisseCloture = new TaFondDeCaisse();
    
    private TaDepotRetraitCaisse depotCaisse = new TaDepotRetraitCaisse();
    private TaDepotRetraitCaisse retraitCaisse = new TaDepotRetraitCaisse();

	
	public TicketDeCaisseController() {  
	}  

	@PostConstruct
	public void postConstruct(){
		super.postConstruct();
		nomDocument="TicketCaisse";
		setTaDocumentService(taTicketDeCaisseService);
		setTaLDocumentService(taLTicketDeCaisseService);
		setTaInfosDocumentService(taInfosTicketDeCaisseService);
		
		stepSynthese = "idSyntheseTicketCaisse";
		stepEntete = "idEnteteTicketCaisse";
		stepLignes = "idLignesTicketCaisse";
		stepTotaux = "idTotauxFormTicketCaisse";
		stepValidation = "idValidationFormTicketCaisse";
		idMenuBouton = "form:tabView:idMenuBoutonTicketCaisse";
		wvEcran = LgrTab.WV_TAB_TICKET_DE_CAISSE;
		classeCssDataTableLigne = "myclassTicketDeCaisse";
		idBtnWizardButtonPrecedent = "form:tabView:idBtnWizardButtonPrecedentTicketCaisse";
		idBtnWizardButtonSuivant = "form:tabView:idBtnWizardButtonSuivantTicketCaisse";
		
		JS_FONCTION_INITIALISE_POSITION_BOUTON_WIZARD = "initialisePositionBoutonWizard('.wizard-ticket-de-caisse')";
		gestionBoutonWizardDynamique = true;

		refresh();
		initChoixEdition();
//		gestionLot = autorisationBean.isModeGestionLot();
		
	}
	
	public void initChoixEdition() {
		choixEdition = false;
		List<TaEdition> listeEditionDisponible = taEditionService.rechercheEditionDisponible("", ConstActionInterne.EDITION_FACTURE_DEFAUT,null);
		
		if(listeEditionDisponible!=null && !listeEditionDisponible.isEmpty() && listeEditionDisponible.size()>1) {
			choixEdition = true;
		}
	}
	
	public void refresh() {
//		values = taTicketDeCaisseService.selectAllDTO();
		values = taTicketDeCaisseService.findAllLight();
		listeFamilleArticleCaisse = taFamilleService.selectAllDTO();
		listeArticleCaisse = taArticleService.findAllLight();
		valuesLigne = IHMmodel();
		taSessionCaisse = taSessionCaisseService.findSessionCaisseActive(sessionInfo.getUtilisateur().getId(), null);
		if(taSessionCaisse==null) {
			afficheCaisse = false;
		}
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}


	public void IHMmodel(TaLTicketDeCaisseDTOJSF t, TaLTicketDeCaisse ligne) {

		LgrDozerMapper<TaLTicketDeCaisse,TaLTicketDeCaisseDTO> mapper = new LgrDozerMapper<TaLTicketDeCaisse,TaLTicketDeCaisseDTO>();
		TaLTicketDeCaisseDTO dto = t.getDto();

		dto = (TaLTicketDeCaisseDTO) mapper.map(ligne, TaLTicketDeCaisseDTO.class);
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
		t.updateDTO(false);

	}
	
	public List<TaLTicketDeCaisseDTOJSF> IHMmodel() {
		LinkedList<TaLTicketDeCaisse> ldao = new LinkedList<TaLTicketDeCaisse>();
		LinkedList<TaLTicketDeCaisseDTOJSF> lswt = new LinkedList<TaLTicketDeCaisseDTOJSF>();

		if(masterEntity!=null && !masterEntity.getLignes().isEmpty()) {

			ldao.addAll(masterEntity.getLignes());

			LgrDozerMapper<TaLTicketDeCaisse,TaLTicketDeCaisseDTO> mapper = new LgrDozerMapper<TaLTicketDeCaisse,TaLTicketDeCaisseDTO>();
			TaLTicketDeCaisseDTO dto = null;
			for (TaLTicketDeCaisse o : ldao) {
				TaLTicketDeCaisseDTOJSF t = new TaLTicketDeCaisseDTOJSF();
				dto = (TaLTicketDeCaisseDTO) mapper.map(o, TaLTicketDeCaisseDTO.class);
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
	
	public void actInitDemarrerSessionCaisse(ActionEvent actionEvent) {
		taFondDeCaisseOuverture = new TaFondDeCaisse();
	}
	
	public void actDemarrerSessionCaisse(ActionEvent actionEvent) {
		taSessionCaisse = taSessionCaisseService.demarrerSessionCaisse(taFondDeCaisseOuverture);
		
		taSessionCaisse = taSessionCaisseService.findSessionCaisseActive(sessionInfo.getUtilisateur().getId(), null);
		if(taSessionCaisse!=null) {
			afficheCaisse = true;
		}
	}
	
	public void actInitCloturerSessionCaisse(ActionEvent actionEvent) {
		taFondDeCaisseCloture = new TaFondDeCaisse();
	}
	
	public void actCloturerSessionCaisse(ActionEvent actionEvent) {
		taSessionCaisse = taSessionCaisseService.cloturerSessionCaisse(taFondDeCaisseCloture);
		
		taSessionCaisse = taSessionCaisseService.findSessionCaisseActive(sessionInfo.getUtilisateur().getId(), null);
		if(taSessionCaisse==null) {
			afficheCaisse = false;
		}
	}
	
	public void actInitDepotCaisse(ActionEvent actionEvent) {
		depotCaisse = new TaDepotRetraitCaisse();
	}
	
	public void actDepotCaisse(ActionEvent actionEvent) {
		taSessionCaisse = taSessionCaisseService.findSessionCaisseActive(sessionInfo.getUtilisateur().getId(), null);
		taSessionCaisseService.deposerEnCaisse(depotCaisse);
	}
	
	public void actInitRetraitCaisse(ActionEvent actionEvent) {
		retraitCaisse = new TaDepotRetraitCaisse();
	}
	
	public void actRetraitCaisse(ActionEvent actionEvent) {
		//taSessionCaisse = taSessionCaisseService.demarrerSessionCaisse();
		taSessionCaisseService.retirerCaisse(retraitCaisse);
	}
	
	public void actInitTicketX(ActionEvent actionEvent) {
		
	}
	
	public void actTicketX(ActionEvent actionEvent) {
		//taSessionCaisse = taSessionCaisseService.demarrerSessionCaisse();
	}
	
	public void actChangeFamilleArticle() {
		System.out.println("TicketDeCaisseController.actChangeFamilleArticle() "+selectedFamilleDTO.getCodeFamille());
		listeArticleCaisse = taArticleService.findArticleCaisseLight(selectedFamilleDTO.getCodeFamille());
	}
	
	public void actValidationManuelleCodeBarre(ActionEvent actionEvent) {
		System.out.println("TicketDeCaisseController.actValidationManuelleCodeBarre() "+zoneSaisie);
		TaArticle art =  taArticleService.findByCodeBarre(zoneSaisie);
		try {
			selectedArticleDTO = taArticleService.findByIdDTO(art.getIdArticle());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//validateUIField(Const.C_CODE_ARTICLE,selectedArticleDTO.getCodeArticle());
		actChoixArticle();
		zoneSaisie = "";
	}
	
	public void actChoixArticle() {
		boolean premiereLigne = false;
		if(masterEntity.getLignes()==null || masterEntity.getLignes().isEmpty()) {
			premiereLigne = true;
		}
		boolean lignePourArticleExisteDeja = false;
		for (TaLTicketDeCaisseDTOJSF ldto : valuesLigne) {
			//recherche d'une ligne déjà existante pour ce code article/code barre pour incrémenter la quantité au lieu de faire une nouvelle ligne
			if(ldto.getTaArticle()!=null 
					&& selectedArticleDTO.getCodeArticle()!=null 
					&& ldto.getTaArticleDTO().getCodeArticle().equals(selectedArticleDTO.getCodeArticle())) {
				lignePourArticleExisteDeja = true;
				selectedLigneJSF = ldto;
				masterEntityLigne =rechercheLigne(selectedLigneJSF.getDto().getIdLDocument());
//				try {
//					masterEntityLigne = taLTicketDeCaisseService.findById(selectedLigneJSF.getDto().getId());
//				} catch (FinderException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
		if(!lignePourArticleExisteDeja) {
			actInsererLigne(null);
			selectedLigneJSF = valuesLigne.get(valuesLigne.size()-1);
			validateUIField(Const.C_CODE_ARTICLE,selectedArticleDTO.getCodeArticle());
			actEnregistrerLigne(null);
			//validateUIField(Const.C_PRIX_U_L_DOCUMENT,selectedArticleDTO.getPrixPrix());
			validateUIField(Const.C_QTE_L_DOCUMENT,BigDecimal.ONE);
			actEnregistrerLigne(null);
			if(premiereLigne) {
				actEnregistrer(null);
				actModifier();
			}
		} else {
			selectionLigne(selectedLigneJSF);
			actModifierLigne(null);
			selectedLigneJSF.getDto().setQteLDocument(selectedLigneJSF.getDto().getQteLDocument().add(BigDecimal.ONE));
			validateUIField(Const.C_QTE_L_DOCUMENT,selectedLigneJSF.getDto().getQteLDocument());
			actEnregistrerLigne(null);
		}
	}
	
	public void actShowAdminCaisse(ActionEvent actionEvent) {
		afficheCaisse = false;
	}
	
	public void actShowCaisse(ActionEvent actionEvent) {
		afficheCaisse = true;
	}
	
	public void actOuvreTiers(ActionEvent actionEvent) {
		
	}
	
	public void actOuvreArticle(ActionEvent actionEvent) {
		
	}
	
	public void actOuvreDetailLigne() {
		selectionLigne(selectedLigneJSF);
		actModifierLigne(null);
	}
	
	public void actOuvreDetailLigne(SelectEvent actionEvent) {
		onRowSelectLigne(actionEvent);
		
		actOuvreDetailLigne();
	}
	
	public void actOuvreDetailLigne(ActionEvent actionEvent) {
		
		actOuvreDetailLigne();
	}
	
	public void actRechercheArticle(ActionEvent actionEvent) {
		
	}
	
	public void actQteLignePlusUn(ActionEvent actionEvent) {
		selectedLigneJSF.getDto().setQteLDocument(selectedLigneJSF.getDto().getQteLDocument().add(BigDecimal.ONE));
		validateUIField(Const.C_QTE_L_DOCUMENT,selectedLigneJSF.getDto().getQteLDocument());
	}
	
	public void actQteLigneMoinsUn(ActionEvent actionEvent) {
		selectedLigneJSF.getDto().setQteLDocument(selectedLigneJSF.getDto().getQteLDocument().subtract(BigDecimal.ONE));
		validateUIField(Const.C_QTE_L_DOCUMENT,selectedLigneJSF.getDto().getQteLDocument());
	}
	
	public void actInitRemiseGlobale(ActionEvent actionEvent) {
		
	}
	
	public void actRemiseGlobale(ActionEvent actionEvent) {
		//actEnregistrer(null);
		mapperUIToModel.map(selectedDocumentDTO, masterEntity);
		masterEntity.calculeTvaEtTotaux();
		mapperModelToUI.map(masterEntity, selectedDocumentDTO);
	}
	
	public void actInitPaiementEspeces(ActionEvent actionEvent) {
		String CODE_TYPE_PAIEMENT_DEFAUT = "E";
		typePaiementDefaut = CODE_TYPE_PAIEMENT_DEFAUT;
		
		setFactureReglee(true);
		actInitReglement();
		calculDateEcheance(true);
	}
	
	public void actAnnulerPaiementEspeces(ActionEvent actionEvent) {
		setFactureReglee(false);
		actInitReglement();
	}

	public void actPaiementEspeces(ActionEvent actionEvent) {
		try {
			actEnregistrer(actionEvent);
			masterEntity = taTicketDeCaisseService.findByIDFetch(masterEntity.getIdDocument());
			
			if(masterEntity.getTaRReglements()!=null) {
				masterEntity.getTaRReglements().iterator().next().getTaReglement().setLibelleDocument(libellePaiement);
			}
			
			//****
			actEnregistrer(actionEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actCalculeMonaieARendreEspeces(AjaxBehaviorEvent e) {
		if(especesRecuClient!=null && selectedDocumentDTO.getMontantReglement()!=null) {
			especesARendreClient = especesRecuClient.subtract(selectedDocumentDTO.getMontantReglement());
		}
	}
	
	public void actInitPaiementCheque(ActionEvent actionEvent) {
		String CODE_TYPE_PAIEMENT_DEFAUT = "C";
		typePaiementDefaut = CODE_TYPE_PAIEMENT_DEFAUT;
		
		setFactureReglee(true);
		actInitReglement();
		calculDateEcheance(true);
//		if(masterEntity.getTaRReglements()!=null) {
//			libellePaiement = masterEntity.getTaRReglements().iterator().next().getTaReglement().getLibelleDocument();
//		}
	}
	
	public void actAnnulerPaiementCheque(ActionEvent actionEvent) {
		setFactureReglee(false);
		actInitReglement();
	}
	
	public void actPaiementCheque(ActionEvent actionEvent) {
		try {
			actEnregistrer(actionEvent);
			
//			
//			Date datePaiement = new Date();
//			BigDecimal montant = masterEntity.getNetTtcCalc();
//			
//			TaReglement r = new TaReglement();
//			r.setNetTtcCalc(montant);
//			r.setCodeDocument(taReglementService.genereCode(null));
//			r.setLibelleDocument("");
//			r.setDateDocument(datePaiement);
//			r.setDateLivDocument(datePaiement);
//			r.setTaTPaiement(taTPaiementService.findByCode(CODE_TYPE_PAIEMENT_DEFAUT));
//			r.setTaTiers(masterEntity.getTaTiers());
//			//r.setService(EmailLgr.TYPE_STRIPE);
//			r.setCompteClient(false);
//			r.setRefPaiement(null);
//			
//			//r = taReglementService.merge(r);
//			
//			TaRReglement rr = new TaRReglement();
//			rr.setAffectation(montant);
//			rr.setTaTicketDeCaisse(masterEntity);
//			rr.setTaReglement(r);
//			if(masterEntity.getTaRReglements()==null) {
//				masterEntity.setTaRReglements(new HashSet<>());
//			}
//			masterEntity.addRReglement(rr);
//			r.addRReglement(rr);
//			
//			r = taReglementService.merge(r);
			
			
			
			masterEntity = taTicketDeCaisseService.findByIDFetch(masterEntity.getIdDocument());
			
			if(masterEntity.getTaRReglements()!=null) {
				masterEntity.getTaRReglements().iterator().next().getTaReglement().setLibelleDocument(libellePaiement);
			}
			
			//****
			actEnregistrer(actionEvent);
		} catch(Exception e) {
			e.printStackTrace();
		}
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
		
		
		
		setFactureReglee(true);
		actInitReglement();
		calculDateEcheance(true);
		PaiementParam param = new PaiementParam();
		param.setDocumentPayableCB(masterEntity);
		param.setMontantPaiement(masterEntity.getNetTtcCalc());
		param.setOriginePaiement("BDG Caisse Enregistreuse");
		PaiementCbController.actDialoguePaiementEcheanceParCarte(param);
	}
	
	public void handleReturnDialoguePaiementParCarte(SelectEvent event) {
		System.out.println("PaiementCbController.handleReturnDialoguePaiementEcheanceParCarte()");
		try {
//			if(event!=null && event.getObject()!=null) {
//				TaEcheance e = (TaEcheance) event.getObject();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actAnnulerPaiementCarteBancaire(ActionEvent actionEvent) {
//		setFactureReglee(false);
//		actInitReglement();
	}

	public void actPaiementCarteBancaire(ActionEvent actionEvent) {
		
	}
	
	public void actSupprimerLigneTicket(ActionEvent actionEvent) {
		if(autorisationBean.autoriseMenu(IModulesProgramme.ID_MODULE_ABONNEMENT)) {
			  if(selectedLigneJSF.getDto().getIdLDocument() != null) {
//					//List<TaLEcheance> liste  = taLEcheanceService.findAllByIdLFacture(selectedLigneJSF.getDto().getIdLDocument());
//					List<TaLigneALigneEcheance> liste  = taLigneALigneEcheanceService.findAllByIdLDocumentAndTypeDoc(selectedLigneJSF.getDto().getIdLDocument(), TaTicketDeCaisse.TYPE_DOC);
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
		actSupprimerLigne();
	}
	
	public void actInitEncaisser(ActionEvent actionEvent) {
		
	}

	public void actEncaisser(ActionEvent actionEvent) {
		actEnregistrer(null);
	}
	
	public void actModeSaisieQte(ActionEvent actionEvent) {
		
	}
	
	public void actModeSaisiePrix(ActionEvent actionEvent) {
		
	}

	public void actModeSaisieCodeBarre(ActionEvent actionEvent) {
	
	}
	
	
	
	public void actBtn0(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=0;
	}

	public void actBtn1(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=1;
	}

	public void actBtn2(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=2;
	}

	public void actBtn3(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=3;
	}

	public void actBtn4(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=4;
	}

	public void actBtn5(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=5;
	}

	public void actBtn6(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=6;
	}

	public void actBtn7(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=7;
	}

	public void actBtn8(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=8;
	}

	public void actBtn9(ActionEvent actionEvent) {
		if(zoneSaisie==null) zoneSaisie="";
		zoneSaisie+=9;
	}

	public void actBtnAnnuler(ActionEvent actionEvent) {
		zoneSaisie="";
	}

	public void actBtnMultipliQte(ActionEvent actionEvent) {

	}
	
	public void actBtnValider(ActionEvent actionEvent) {
		zoneSaisie="";
	}
	
	public void actNouveauTiers(ActionEvent actionEvent) {
		try {
			taTiersDTO = new TaTiersDTO();
			taTiers = new TaTiers();
	
			String codeTiersDefaut = "C";
	
			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			taTiersDTO.setCodeTiers(taTiersService.genereCode(null)); //ejb
			//validateUIField(Const.C_CODE_TIERS,taTiersDTO.getCodeTiers());//permet de verrouiller le code genere
	
			taTiersDTO.setCodeTTiers(codeTiersDefaut);
	
			taTiersDTO.setCodeCompta(taTiersDTO.getCodeTiers()); //ejb
			validateUIField(Const.C_CODE_COMPTA,taTiersDTO.getCodeCompta()); //ejb
			taTiersDTO.setCompte("411"); //ejb
			validateUIField(Const.C_COMPTE,taTiersDTO.getCompte()); //ejb
			TaTTiers taTTiers;
	
			taTTiers = taTTiersService.findByCode(codeTiersDefaut);
			taTiers.setTaTTiers(taTTiers);
	
			if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
				taTiersDTO.setCompte(taTTiers.getCompteTTiers());
			} else {
				//selectedTaTiersDTO.setCompte(TiersPlugin.getDefault().getPreferenceStore().
				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
			}
			taTiersDTO.setActifTiers(true);
	
			taTiersDTO.setCodeTTvaDoc("F");
	
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actInserer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actEnregistrerTiers(ActionEvent actionEvent) {
		//TaTiers taTiers = new TaTiers();
		taTiers.initAdresseTiers(null,adresseEstRempli(),ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT,
				ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT);
		if(taTiers.getTaAdresse()!=null){
			try {
				if(taTiers.getTaAdresse()!=null && taTiers.getTaAdresse().getTaTAdr()==null){
				TaTAdr tAdr= taTAdrService.findByCode(ConstPreferencesTiers.TADR_DEFAUT);
				taTiers.getTaAdresse().setTaTAdr(tAdr);
				}
				int ordre=taAdresseService.rechercheOdrePourType(taTiers.getTaAdresse().getTaTAdr().getCodeTAdr(),taTiers.getCodeTiers());
				if(taTiers.getTaAdresse().getOrdre()==null || taTiers.getTaAdresse().getOrdre()==0)taTiers.getTaAdresse().setOrdre(ordre);
			} catch (FinderException e) {
				
			}
		}
		//autoCompleteMapUIToDTO();
		initAdresseNull();
		if(taTiers.getTaEmail()==null){
			taTiersDTO.setAdresseEmail(null);
		}
		if(taTiers.getTaTelephone()==null){
			taTiersDTO.setNumeroTelephone(null);
		}
		if(taTiers.getTaWeb()==null){
			taTiersDTO.setAdresseWeb(null);
		}

		mapperUIToModelTiers.map(taTiersDTO, taTiers);
		
		/*
		 * A faire, mapper les objets dans taTiers avant le merge :
		 * mapping de tous les objets avec des "codes", typeTiers, typeCvilite, ...
		 * tous les objets imbriqués par défaut, adresse défaut, email défaut, ...
		 * 
		 * Pour les converter, essayé de faire fonctionner les injections via omniface
		 * ou, récupérer dans le client RCP le système pour générer les chaines JNDI
		 * 
		 * Ma questions sur les forums
		 * http://forum.primefaces.org/viewtopic.php?f=3&t=40677&sid=fc4d0075c1e57d0433bd8a8d3bdf0393
		 * http://stackoverflow.com/questions/27551615/primefaces-autocomplete-using-a-list-pojo-and-a-string-as-default-value
		 * 
		 */
		
		try {
			if(taTiers.getCodeCompta()==null 
					|| (taTiers.getCodeCompta()!=null && taTiers.getCodeCompta().equals(""))
					) {
					taTiers.setCodeCompta(taTiers.getCodeTiers().substring(0, 7));
				if(taTiers.getCodeTiers().length()>6) {
					taTiers.setCodeCompta(taTiers.getCodeTiers().substring(0, 7));
				} else  {
					taTiers.setCodeCompta(taTiers.getCodeTiers().substring(0, taTiers.getCodeTiers().length()));
				}
			}
			if(taTiers.getCleLiaisonCompteClient()==null || taTiers.getCleLiaisonCompteClient().equals("")) {
				taTiers.setCleLiaisonCompteClient(LibExecLinux.pwgen("5"));
			}

			taTiers = taTiersService.merge(taTiers,ITaTiersServiceRemote.validationContext);
			
			if(taTiersDTO.getCodeTiers()!=null) {
				taTiersService.annuleCode(taTiersDTO.getCodeTiers());
			}
			
			mapperModelToUITiers.map(taTiers, taTiersDTO);
			//autoCompleteMapDTOtoUI();
			//modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			
			validateUIField(Const.C_CODE_TIERS, taTiers.getCodeTiers());

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer"));
		}
	}
	
	private void initAdresseNull() {
		if(taTiers.getTaAdresse()==null){
			taTiersDTO.setAdresse1Adresse(null);
			taTiersDTO.setAdresse2Adresse(null);
			taTiersDTO.setAdresse3Adresse(null);
			taTiersDTO.setCodepostalAdresse(null);
			taTiersDTO.setVilleAdresse(null);
			taTiersDTO.setPaysAdresse(null);
		}
	}
	
	public void actSwitchScanManuel(ActionEvent actionEvent) {
		modeManuel = !modeManuel;
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

			LgrDozerMapper<TaLTicketDeCaisseDTO,TaLTicketDeCaisse> mapper = new LgrDozerMapper<TaLTicketDeCaisseDTO,TaLTicketDeCaisse>();
			mapper.map((TaLTicketDeCaisseDTO) selectedLigneJSF.getDto(),masterEntityLigne);
			
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
			context.addMessage(null, new FacesMessage("TicketCaisse", "actEnregisterLigne")); 
		}
	}


	
	private void verifSiDifferenceReglement(){
		differenceReglementResteARegle=false;
		if(masterEntity.getTaReglement()!=null && modeEcran.dataSetEnModif()){
			differenceReglementResteARegle=masterEntity.getTaReglement().getNetTtcCalc().compareTo(masterEntity.getNetTtcCalc())!=0;
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

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				if(selectedDocumentDTO.getCodeDocument()!=null) {
					taTicketDeCaisseService.annuleCode(selectedDocumentDTO.getCodeDocument());
				}				
				masterEntity=new TaTicketDeCaisse();
				selectedDocumentDTO=new TaTicketDeCaisseDTO();

				if(values.size()>0)	selectedDocumentDTO = values.get(0);

				onRowSelect(null);

				valuesLigne=IHMmodel();
				initInfosDocument();
				break;
			case C_MO_EDITION:
				if(modeTactile) {
					actEnregistrer(null);
				}
				if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0){
					masterEntity = taTicketDeCaisseService.findByIDFetch(selectedDocumentDTO.getId());
					selectedDocumentDTO=taTicketDeCaisseService.findByIdDTO(selectedDocumentDTO.getId());
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
				context.addMessage(null, new FacesMessage("TicketCaisse", "actAnnuler")); 
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
				List<TaLTicketDeCaisse> listeLigneVide = new ArrayList<TaLTicketDeCaisse>(); 
				for (TaLTicketDeCaisse ligne : masterEntity.getLignes()) {
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
				for (TaLTicketDeCaisse taLBonReception : listeLigneVide) {
					masterEntity.getLignes().remove(taLBonReception);
				}
				
				//suppression des liaisons entre ligne doc et ligne d'échéance
				for (TaLigneALigneEcheance ligneALigne : listeLigneALigneEcheanceASupprimer) {
					taLigneALigneEcheanceService.remove(ligneALigne);
				}

				//isa le 08/11/2016
				//j'ai rajouté cette réinitialisation tant que l'on enlève les lignes vides, sinon génère des trous dans la numérotation des lignes
				masterEntity.reinitialiseNumLignes(0, 1);

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
				grpMouvStock.setLibelleGrMouvStock(masterEntity.getLibelleDocument()!=null?masterEntity.getLibelleDocument():"TicketCaisse "+masterEntity.getCodeDocument());
				grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("T")); // type mouvement TicketCaisse
				masterEntity.setTaGrMouvStock(grpMouvStock);
				grpMouvStock.setTaTicketDeCaisse(masterEntity);

				for (TaLTicketDeCaisse l : masterEntity.getLignes()) {
					if(!l.getTaTLigne().equals(typeLigneCommentaire)){
						l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
						if(l.getTaMouvementStock()!=null)
							l.getTaMouvementStock().setTaGrMouvStock(masterEntity.getTaGrMouvStock());
					}
				}
				
				//
				grpMouvStock.getTaMouvementStockes().clear();
				
				//Création des lignes de mouvement
				for (TaLTicketDeCaisse ligne : masterEntity.getLignes()) {
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
				masterEntity = taTicketDeCaisseService.mergeAndFindById(masterEntity,ITaTicketDeCaisseServiceRemote.validationContext);
				
				taTicketDeCaisseService.annuleCode(selectedDocumentDTO.getCodeDocument());

				mapperModelToUI.map(masterEntity, selectedDocumentDTO);
				autoCompleteMapDTOtoUI();
				selectedDocumentDTOs=new TaTicketDeCaisseDTO[]{selectedDocumentDTO};


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
					 * le oncomplete est déclenché après les update ajax, or l'action suivante est "wizardDevis.setStep(this.stepEntete);"
					 * donc le oncomplete s'execute alors que nous somme déjà revenu sur le step 1
					 * Il faut donc reprendre cette valeur si on souhaite y accéder à partir de PF('moncomposantcheckbox') dans le javascript du oncomplete
					 */
				}
				
				ecranSynthese=modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION);
				if(wizardDocument!=null)wizardDocument.setStep(stepEntete);
				changementStepWizard(false);
				

			}
			docEnregistre=true;
		} catch(Exception e) {
			docEnregistre=false;
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TicketCaisse", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TicketCaisse", "actEnregistrer")); 
		}
	}
	
	public void initialisePositionBoutonWizard() {
		if(!modeTactile) { //pas de wizard pour le tactile de la caisse , => JS plante
			super.initialisePositionBoutonWizard();
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedDocumentDTO = new TaTicketDeCaisseDTO();
			masterEntity = new TaTicketDeCaisse();
			masterEntity.setTaGrMouvStock(new TaGrMouvStock());
			masterEntity.setLegrain(true);
			valuesLigne = IHMmodel();
			selectedDocumentDTO.setCodeTTvaDoc("F");
			factureReglee=false;
			libelleMultipleReglement=MESSAGE_SIMPLE_REGLEMENT;
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

			if(selectedDocumentDTO!=null) {
				taTicketDeCaisseService.annuleCode(selectedDocumentDTO.getCodeDocument());
			}			
			//			String newCode = taTicketDeCaisseService.genereCode(paramsCode);
			//			if(newCode!=null && !newCode.equals("")){
			//				selectedDocumentDTO.setCodeDocument(newCode);
			//			}
			selectedDocumentDTO.setCodeDocument(taTicketDeCaisseService.genereCode(paramsCode));
			
			if(modeTactile) {
//				gestionLot = false;
				masterEntity.setGestionLot(false);
				validateUIField(Const.C_CODE_DOCUMENT, selectedDocumentDTO.getCodeDocument());
				masterEntity.setCodeDocument(selectedDocumentDTO.getCodeDocument());
				selectedDocumentDTO.setLibelleDocument("Ticket "+selectedDocumentDTO.getCodeDocument());
				masterEntity.setLibelleDocument(selectedDocumentDTO.getLibelleDocument());
				String codeTiersDefaut =taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_CAISSE, ITaPreferencesServiceRemote.CAISSE_CODE_TIERS_DEFAUT);
				if(codeTiersDefaut==null) {
					System.out.println("TicketDeCaisseController.actInserer() ****** Le code tiers par défaut pour la caisse n'est pas renseigner");
				}
				//String codeTiersDefaut = "ACA";
				validateUIField(Const.C_CODE_TIERS, codeTiersDefaut);
				
			}
			changementTiers(true);
			masterEntity.setTtc(1);

			especesARendreClient = null;
			especesRecuClient = null;
			libellePaiement = null;
			
			updateReglements();

			//			selectedDocumentDTO.setCodeTTvaDoc("F");
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
			if(wizardDocument!=null) {
				wizardDocument.setStep(stepEntete);
			}
			changementStepWizard(false);
			
			
			btnPrecedentVisible = false;
			btnSuivantVisible = true;
			initialisePositionBoutonWizard(); 

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TicketCaisse", "actInserer"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TICKET_DE_CAISSE);
		b.setTitre("Ticket de caisse");
		b.setTemplate("documents/ticket_caisse.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TICKET_DE_CAISSE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_TICKET_DE_CAISSE);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		//		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TicketCaisse", 
					"Nouveau"
					)); 
		}
	} 

	public void recupMasterEntity() {
		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION)) {
			try {
				masterEntity=taTicketDeCaisseService.findByIDFetch(masterEntity.getIdDocument());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void actModifier(ActionEvent actionEvent) {
		recupMasterEntity();
		if(modeTactile) {
//			gestionLot = false;
			masterEntity.setGestionLot(false);
		}
		super.actModifier(actionEvent);
	}
	public void actSupprimer(ActionEvent actionEvent) {
		TaTicketDeCaisse obj = new TaTicketDeCaisse();
		try {
			if(autorisationLiaisonComplete()) {
			if(selectedDocumentDTO!=null && selectedDocumentDTO.getId()!=null){
				obj = taTicketDeCaisseService.findByIDFetch(selectedDocumentDTO.getId());
			}
			if(verifSiEstModifiable(obj)) {
				if(obj!=null) {
					taTicketDeCaisseService.remove(obj);
					values.remove(selectedDocumentDTO);
				} else {
					if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>=1) {
						for (TaTicketDeCaisseDTO d : selectedDocumentDTOs) {
							taTicketDeCaisseService.removeDTO(d);
							values.remove(d);
						}
					}
				}

				if(!values.isEmpty()) {
					selectedDocumentDTO = values.get(0);
					selectedDocumentDTOs = new TaTicketDeCaisseDTO[]{selectedDocumentDTO};
					updateTab();
				}else{
					selectedDocumentDTO = new TaTicketDeCaisseDTO();
					selectedDocumentDTOs = new TaTicketDeCaisseDTO[]{};
				}

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("TicketCaisse", "actSupprimer"));
				}
			}
			}
		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TicketCaisse", e.getMessage()));
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

		selectedDocumentDTO=new TaTicketDeCaisseDTO();
		selectedDocumentDTOs=new TaTicketDeCaisseDTO[]{selectedDocumentDTO};
		
		gestionBoutonWizardDynamique = false;
		updateTab();
		
		
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeTicketCaisse').filter()");

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TicketCaisse", "actFermer")); 
		}
	}
	
	public void actImprimerTactile(ActionEvent actionEvent) {
		try {
			selectedDocumentDTO = taTicketDeCaisseService.findByIdDTO(masterEntity.getIdDocument());
			actImprimer(actionEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("TicketCaisse", "actImprimer")); 
		}
		boolean editionClient=false;
		try {
			editionClient=false;
			if(actionEvent!=null){
				String pourClient = (String)actionEvent.getComponent().getAttributes().get("pour_client");
				editionClient = LibConversion.StringToBoolean(pourClient);
			}

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaTicketDeCaisse doc =taTicketDeCaisseService.findById(selectedDocumentDTO.getId());
			doc.calculeTvaEtTotaux();

			mapEdition.put("doc",doc );

			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
			//mapEdition.put("LibelleJournalTva", taTTvaDoc.getLibelleEdition());

			mapEdition.put("lignes", masterEntity.getLignes());



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
			//taTicketDeCaisseService.generePDF(selectedDocumentDTO.getId());
			////////////////////////////////////////////////////////////////////////////////////////
			
			if(editionClient || impressionDirectClient) {
				//Mise à jour de la mise à dispostion
				if(masterEntity.getTaMiseADisposition()==null) {
					masterEntity.setTaMiseADisposition(new TaMiseADisposition());
				}
				Date dateMiseADispositionClient = new Date();
				masterEntity.getTaMiseADisposition().setImprimePourClient(dateMiseADispositionClient);
				masterEntity = taTicketDeCaisseService.mergeAndFindById(masterEntity,ITaTicketDeCaisseServiceRemote.validationContext);
				updateTab();
			}

			//			session.setAttribute("tiers", taTiersService.findById(selectedDocumentDTO.getId()));
			System.out.println("TicketCaisseController.actImprimer()");
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}    

	public void actImprimerListeDesTicketsDeCaisse(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesTicketsDeCaisse", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TICKET_DE_CAISSE);
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
			updateReglements();
			docEnregistre=true;
			
			autoCompleteMapDTOtoUI();
			if(masterEntity.getTaRReglement()!=null){
				differenceReglementResteARegle=masterEntity.getTaRReglement().getAffectation().compareTo(masterEntity.getNetTtcCalc())!=0;
			}
			
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TicketCaisse", 
						"Chargement de la TicketCaisse N°"+selectedDocumentDTO.getCodeTiers()
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
		ihmReglement.getDto().setTypeDocument("");
		ihmReglement.getDto().setDateDocument(avoir.getDateDocument());
		ihmReglement.getDto().setDateLivDocument(avoir.getDateLivDocument());
		if(avoir.getTaTPaiement()!=null){
			ihmReglement.getDto().setIdTPaiement(avoir.getTaTPaiement().getIdTPaiement());
			ihmReglement.getDto().setCodeTPaiement(avoir.getTaTPaiement().getCodeTPaiement());
		}
		ihmReglement.getDto().setEtat(avoir.getEtatDeSuppression());
		ihmReglement.getDto().setDateExport(avoir.getDateExport());
		ihmReglement.getDto().setLibelleDocument(avoir.getLibelleDocument());

		ihmReglement.getDto().setAffectation(avoir.calculAffectationEnCoursReel((TaTicketDeCaisse) taDocument));
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
		b.setTypeOnglet(LgrTab.TYPE_TAB_TICKET_DE_CAISSE);
		b.setTitre("Ticket de caisse");
		b.setTemplate("documents/ticket_caisse.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TICKET_DE_CAISSE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_TICKET_DE_CAISSE);
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
			TaLTicketDeCaisseDTO dtoTemp =new TaLTicketDeCaisseDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taLTicketDeCaisseService.validateDTOProperty(dtoTemp, nomChamp, ITaLTicketDeCaisseServiceRemote.validationContext );

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
//			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//			Set<ConstraintViolation<TaTicketDeCaisseDTO>> violations = factory.getValidator().validateValue(TaTicketDeCaisseDTO.class,nomChamp,value);
//			if (violations.size() > 0) {
//				messageComplet = "Erreur de validation : ";
//				for (ConstraintViolation<TaTicketDeCaisseDTO> cv : violations) {
//					messageComplet+=" "+cv.getMessage()+"\n";
//				}
//				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
//			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
//			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}

	
	public void validateDocumentAvantEnregistrement( Object value) throws ValidatorException {

		String msg = "";

		try {
			//taBonReceptionService.validateDTOProperty(selectedDocumentDTO,Const.C_CODE_TIERS,  ITaTicketDeCaisseServiceRemote.validationContext );

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
					PrimeFaces.current().executeScript("PF('wVdialogQte2TicketCaisse').show()");
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
					((TaTicketDeCaisseDTO)selectedDocumentDTO).setLibelleDocument("TicketCaisse N°"+selectedDocumentDTO.getCodeDocument()+" - "+nomTiers);										
					if(!disableTtc() && !disableTtcSiDocSansTVA()){
						masterEntity.setTtc(entity.getTtcTiers());
						((TaTicketDeCaisseDTO)selectedDocumentDTO).setTtc(LibConversion.intToBoolean(masterEntity.getTtc()));
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

			}else if(nomChamp.equals(Const.C_CODE_ARTICLE+"_RECHERCHE")) {	
				TaArticle entity = null;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCode(((TaArticleDTO) value).getCodeArticle());
					}else{
						entity = taArticleService.findByCode((String)value);
					}
				}
				selectedArticleDTO = taArticleService.findByIdDTO(entity.getIdArticle());
				actChoixArticle();
				zoneSaisie = "";
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
					selectedLigneJSF.setTaArticle(entity);
				}

				if(changementArticleLigne) {
					if(entity!=null) selectedLigneJSF.getDto().setLibLDocument(entity.getLibellecArticle());
					selectedLigneJSF.setTaLot(null);
					selectedLigneJSF.setTaEntrepot(null);
					selectedLigneJSF.getDto().setCodeEntrepot(null);
					selectedLigneJSF.getDto().setNumLot(null);
					//					selectedLigneJSF.getDto().setDluo(LibDate.incrementDate(selectedLigneJSF.getDateDocument(), LibConversion.stringToInteger(entity.getParamDluo(), 0)+1  , 0, 0));
					//					selectedLigneJSF.getDto().setLibelleLot(entity.getLibellecArticle());
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
				if(rapport!=null){
					selectedLigneJSF.setTaRapport(rapport);
					if(rapport.getTaUnite1()!=null) {
						selectedLigneJSF.getDto().setU1LDocument(rapport.getTaUnite1().getCodeUnite());
						selectedLigneJSF.setTaUnite1(taUniteService.findByCode(rapport.getTaUnite1().getCodeUnite()));
					}
					if(rapport.getTaUnite2()!=null){
						selectedLigneJSF.getDto().setU2LDocument(rapport.getTaUnite2().getCodeUnite());
						selectedLigneJSF.setTaUnite2(taUniteService.findByCode(rapport.getTaUnite2().getCodeUnite()));
					}
				}else
					if(entity!=null){
						if(entity.getTaUnite1()!=null) {
							selectedLigneJSF.getDto().setU1LDocument(entity.getTaUnite1().getCodeUnite());
							selectedLigneJSF.setTaUnite1(taUniteService.findByCode(entity.getTaUnite1().getCodeUnite()));
						}
						for (TaRapportUnite obj : entity.getTaRapportUnites()) {
							if(obj.getTaUnite1()!=null && obj.getTaUnite1().getCodeUnite().equals(selectedLigneJSF.getDto().getU1LDocument())){
								selectedLigneJSF.setTaRapport(obj);
								selectedLigneJSF.getDto().setU2LDocument(obj.getTaUnite2().getCodeUnite());	
								selectedLigneJSF.setTaUnite2(taUniteService.findByCode(obj.getTaUnite2().getCodeUnite()));
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
					if(selectedLigneJSF.getTaRapport()!=null && selectedLigneJSF.getTaRapport().getRapport()!=null){
						switch (selectedLigneJSF.getTaRapport().getSens()) {
						case 1:
							selectedLigneJSF.getDto().setQte2LDocument((qte).multiply(selectedLigneJSF.getTaRapport().getRapport()));
							break;
						case 0:
							selectedLigneJSF.getDto().setQte2LDocument((qte).divide(selectedLigneJSF.getTaRapport().getRapport()
									,MathContext.DECIMAL128).setScale(selectedLigneJSF.getTaRapport().getNbDecimal(),BigDecimal.ROUND_HALF_UP));
							break;
						default:
							break;
						}
					}
				} else {
					masterEntityLigne.setQteLDocument(null);
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
				}else {
					selectedLigneJSF.getDto().setU2LDocument("");
					masterEntityLigne.setU2LDocument(null);
				}
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
				
			}
			else if(nomChamp.equals(Const.C_CODE_C_PAIEMENT)) {
				TaCPaiement entity = null;
				if(value!=null){
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

				////////////////////////////////////////////////////////////////////
			}else if(nomChamp.equals(Const.C_NUMERO_TELEPHONE)) {
				taTiers.initTelephoneTiers(value,ConstPreferencesTiers.CORRESPONDANCE_TELEPHONE_ADMINISTRATIF_DEFAUT,
						ConstPreferencesTiers.CORRESPONDANCE_TELEPHONE_COMMERCIAL_DEFAUT);
					if(value!=null && !value.equals("")) { 
						PropertyUtils.setSimpleProperty(taTiers.getTaTelephone(), nomChamp, value);
					}
					if(taTiers.getTaTelephone()==null)taTiersDTO.setNumeroTelephone(null);
					if(taTiers.getTaTelephone()!=null &&
							taTiers.getTaTelephone().getTaTTel()==null){
						TaTTel taTTel = new TaTTel();
						taTTel.setCodeTTel(ConstPreferencesTiers.TTEL_DEFAUT);
						if(!taTTel.getCodeTTel().equals("")){
							//TaTTelDAO taTTelDAO = new TaTTelDAO(getEm());
							taTTel=taTTelService.findByCode(taTTel.getCodeTTel());
							taTiers.getTaTelephone().setTaTTel(taTTel);
						}
					}					
			}else if(nomChamp.equals(Const.C_NOM_ENTREPRISE)) {
				taTiers.initEntrepriseTiers(value);
					if(value!=null && !value.equals("")) { 
					PropertyUtils.setSimpleProperty(taTiers.getTaEntreprise(), nomChamp, value);
					}
					if(taTiers.getTaEntreprise()==null)taTiersDTO.setNomEntreprise(null);
			}else if(nomChamp.equals(Const.C_ADRESSE_EMAIL)) {
				taTiers.initEmailTiers(value,ConstPreferencesTiers.CORRESPONDANCE_EMAIL_ADMINISTRATIF_DEFAUT,ConstPreferencesTiers.CORRESPONDANCE_EMAIL_COMMERCIAL_DEFAUT);
					if(value!=null && !value.equals("")) {
						PropertyUtils.setSimpleProperty(taTiers.getTaEmail(), nomChamp, value);
					}
					if(taTiers.getTaEmail()==null)taTiersDTO.setAdresseEmail(null);
					if(taTiers.getTaEmail()!=null &&
							taTiers.getTaEmail().getTaTEmail()==null){
						TaTEmail taTEmail = new TaTEmail();
						taTEmail.setCodeTEmail(ConstPreferencesTiers.TEMAIL_DEFAUT);
						if(!taTEmail.getCodeTEmail().equals("")){
							taTEmail=taTEmailService.findByCode(taTEmail.getCodeTEmail());
							taTiers.getTaEmail().setTaTEmail(taTEmail);
						}
					}	
			}else if(nomChamp.equals(Const.C_ADRESSE1_ADRESSE)
					|| nomChamp.equals(Const.C_ADRESSE2_ADRESSE)
					|| nomChamp.equals(Const.C_ADRESSE3_ADRESSE)
					|| nomChamp.equals(Const.C_CODEPOSTAL_ADRESSE)
					|| nomChamp.equals(Const.C_VILLE_ADRESSE)
					|| nomChamp.equals(Const.C_PAYS_ADRESSE)) {
	
				taTiers.initAdresseTiers(value,adresseEstRempli(),ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT,
						ConstPreferencesTiers.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT);
					if(value!=null 
	//							&& !value.equals("")
							/*
							 * Bug #1192
							 * Si on laisse !value.equals("") on ne peu plus effacer de ligne d'adresse
							 */
							) {	
								if(taTiers.getTaAdresse()!=null) {
									PropertyUtils.setSimpleProperty(taTiers.getTaAdresse(), nomChamp, value);
								}
					}
					if(taTiers.getTaAdresse()==null){
						initAdresseNull();
					}
					if(taTiers.getTaAdresse()!=null &&
							taTiers.getTaAdresse().getTaTAdr()==null){
						TaTAdr taTAdr = new TaTAdr();
						taTAdr.setCodeTAdr(ConstPreferencesTiers.TADR_DEFAUT);
						if(!taTAdr.getCodeTAdr().equals("")){
							taTAdr = taTAdrService.findByCode(taTAdr.getCodeTAdr());
							taTiers.getTaAdresse().setTaTAdr(taTAdr);
						}
					}			
				
			} 
			//////////////////////////////////////////////
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected boolean adresseEstRempli() {
		boolean retour=false;
		if(taTiersDTO.getAdresse1Adresse()!=null && !taTiersDTO.getAdresse1Adresse().equals(""))return true;
		if(taTiersDTO.getAdresse2Adresse()!=null && !taTiersDTO.getAdresse2Adresse().equals(""))return true;
		if(taTiersDTO.getAdresse3Adresse()!=null && !taTiersDTO.getAdresse3Adresse().equals(""))return true;
		if(taTiersDTO.getCodepostalAdresse()!=null && !taTiersDTO.getCodepostalAdresse().equals(""))return true;
		if(taTiersDTO.getVilleAdresse()!=null && !taTiersDTO.getVilleAdresse().equals(""))return true;
		if(taTiersDTO.getPaysAdresse()!=null && !taTiersDTO.getPaysAdresse().equals(""))return true;
		return retour;
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
				taInfosDocument = taInfosTicketDeCaisseService.findByCodeFacture(((IDocumentDTO)selectedDocumentDTO).getCodeDocument());
			} else {
				taInfosDocument = new TaInfosTicketDeCaisse();
			}

			if(taInfosDocument!=null && taInfosDocument.getNomTiers()!=null)selectedDocumentDTO.setNomTiers(taInfosDocument.getNomTiers());
			if(taInfosDocument!=null && taInfosDocument.getCodeTTvaDoc()!=null)selectedDocumentDTO.setCodeTTvaDoc(taInfosDocument.getCodeTTvaDoc());
			if(recharger) {				
				if(masterEntity.getTaTiers()!=null){
					masterEntity.setTaTiers(taTiersService.findById(masterEntity.getTaTiers().getIdTiers()));
					selectedDocumentDTO.setNomTiers(masterEntity.getTaTiers().getNomTiers());
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

			TaInfosTicketDeCaisse taInfosDocument = null;
			if (masterEntity != null) {
				if (masterEntity.getCodeDocument()!=null&& masterEntity.getCodeDocument() != "")
					taInfosDocument = taInfosTicketDeCaisseService.findByCodeFacture(masterEntity.getCodeDocument());
				else
					taInfosDocument = new TaInfosTicketDeCaisse();


				taCPaiementDoc = null;
				if (taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_TICKET_DE_CAISSE) != null
						&& taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_TICKET_DE_CAISSE).getTaCPaiement() != null) {
					taCPaiementDoc = taTCPaiementService.findByCode(TaTCPaiement.C_CODE_TYPE_TICKET_DE_CAISSE).getTaCPaiement();
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
				((TaTicketDeCaisseDTO)selectedDocumentDTO).setDateEchDocument(taTicketDeCaisseService.calculDateEcheance(masterEntity,report, finDeMois,taTPaiement));
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
			masterEntity.getTaRReglement().setEtatDeSuppression(IHMEtat.suppression);
			masterEntity.calculRegleDocument();
			selectedDocumentDTO.setMontantReglement(BigDecimal.ZERO);
			selectedDocumentDTO.setLibelleReglement("");
		}

		selectedDocumentDTO.setResteARegler(masterEntity.getResteARegler());
	}

	public void actAppliquerCPaiement() throws Exception {
		calculDateEcheance(true);
		taTicketDeCaisseService.mettreAJourDateEcheanceReglements(masterEntity);
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
	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			//Mise à jour de la mise à dispostion
			if(masterEntity.getTaMiseADisposition()==null) {
				masterEntity.setTaMiseADisposition(new TaMiseADisposition());
			}
			Date dateMiseADispositionClient = new Date();
			
			masterEntity.getTaMiseADisposition().setEnvoyeParEmail(dateMiseADispositionClient);
			masterEntity = taTicketDeCaisseService.mergeAndFindById(masterEntity,ITaTicketDeCaisseServiceRemote.validationContext);
			updateTab();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					)); 
		}
	}
	
	public void actEnvoyerParEmail(ActionEvent actionEvent) {	
		try {
			if(masterEntity.getTaTiers().getTaEmail()!=null 
					&& masterEntity.getTaTiers().getTaEmail().getAdresseEmail()!=null) {
				
//				lgrMailjetService.send(
//					null, 
//					taInfoEntrepriseService.findInstance().getNomInfoEntreprise(), 
//					taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" TicketCaisse "+masterEntity.getCodeDocument(), 
//					"Votre facture "+masterEntity.getCodeDocument(), 
//					"Votre facture "+masterEntity.getCodeDocument(),
//					new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()}, 
//					new File[]{new File(((ITaTicketDeCaisseServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument()))}
//					);
				
				List<String> l = new ArrayList<>();
				l.add(masterEntity.getTaTiers().getTaEmail().getAdresseEmail());
				
				emailServiceFinder.sendAndLogEmailDossier(
						null,
						null,
						taInfoEntrepriseService.findInstance().getNomInfoEntreprise(), 
						taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" TicketCaisse "+masterEntity.getCodeDocument(), 
						"Votre ticket "+masterEntity.getCodeDocument(), 
						"Votre ticket "+masterEntity.getCodeDocument(),
						l,//new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()}, 
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						new File[]{new File(((ITaTicketDeCaisseServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument(),null, null, null))},
						null,//replyTo
						null
						);
				
			
				//Mise à jour de la mise à dispostion
				if(masterEntity.getTaMiseADisposition()==null) {
					masterEntity.setTaMiseADisposition(new TaMiseADisposition());
				}
				Date dateMiseADispositionClient = new Date();
				
				if(envoyeParEmail) {
					masterEntity.getTaMiseADisposition().setEnvoyeParEmail(dateMiseADispositionClient);
				}
				masterEntity = taTicketDeCaisseService.mergeAndFindById(masterEntity,ITaTicketDeCaisseServiceRemote.validationContext);
				updateTab();
				
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TicketCaisse", 
						"TicketCaisse N°"+selectedDocumentDTO.getCodeDocument()+" envoyée par email à : "+masterEntity.getTaTiers().getTaEmail().getAdresseEmail()
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
		
//		creerLot();
//		sessionMap.put("lotBR", selectedTaLBonReceptionDTOJSF.getTaLot());
//		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
		//sessionMap.put("numLot", selectedTaLBonReceptionDTOJSF.getDto().getNumLot());
//		sessionMap.put("numLot", numLot);
		
		EditionParam edition = new EditionParam();
		edition.setIdActionInterne(ConstActionInterne.EDITION_FACTURE_DEFAUT);
		
		sessionMap.put(EditionParam.NOM_OBJET_EN_SESSION, edition);
        
        PrimeFaces.current().dialog().openDynamic("/dialog_choix_edition", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
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
					

					String pdf = taTicketDeCaisseService.generePDF(selectedDocumentDTO.getId(),localPath,taEdition.getMapParametreEdition(),listeResourcesPath,taEdition.getTheme());
					PrimeFaces.current().executeScript("window.open('/edition?fichier="+URLEncoder.encode(pdf, "UTF-8")+"')");

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

	public List<TaFamilleDTO> getListeFamilleArticleCaisse() {
		return listeFamilleArticleCaisse;
	}

	public void setListeFamilleArticleCaisse(List<TaFamilleDTO> listeFamilleArticleCaisse) {
		this.listeFamilleArticleCaisse = listeFamilleArticleCaisse;
	}

	public List<TaArticleDTO> getListeArticleCaisse() {
		return listeArticleCaisse;
	}

	public void setListeArticleCaisse(List<TaArticleDTO> listeArticleCaisse) {
		this.listeArticleCaisse = listeArticleCaisse;
	}

	public TaFamilleDTO getSelectedFamilleDTO() {
		return selectedFamilleDTO;
	}

	public void setSelectedFamilleDTO(TaFamilleDTO selectedFamilleDTO) {
		this.selectedFamilleDTO = selectedFamilleDTO;
	}

	public TaArticleDTO getSelectedArticleDTO() {
		return selectedArticleDTO;
	}

	public void setSelectedArticleDTO(TaArticleDTO selectedArticleDTO) {
		this.selectedArticleDTO = selectedArticleDTO;
	}

	public String getZoneSaisie() {
		return zoneSaisie;
	}

	public void setZoneSaisie(String zoneSaisie) {
		this.zoneSaisie = zoneSaisie;
	}

	public boolean isModeTactile() {
		return modeTactile;
	}

	public void setModeTactile(boolean modeTactile) {
		this.modeTactile = modeTactile;
	}

	public boolean isModeManuel() {
		return modeManuel;
	}

	public void setModeManuel(boolean modeManuel) {
		this.modeManuel = modeManuel;
	}

	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}

	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}

	public String getLibellePaiement() {
		return libellePaiement;
	}

	public void setLibellePaiement(String libellePaiement) {
		this.libellePaiement = libellePaiement;
	}

	public BigDecimal getEspecesRecuClient() {
		return especesRecuClient;
	}

	public void setEspecesRecuClient(BigDecimal especesRecuClient) {
		this.especesRecuClient = especesRecuClient;
	}

	public BigDecimal getEspecesARendreClient() {
		return especesARendreClient;
	}

	public void setEspecesARendreClient(BigDecimal especesARendreClient) {
		this.especesARendreClient = especesARendreClient;
	}

	public boolean isAfficheCaisse() {
		return afficheCaisse;
	}

	public void setAfficheCaisse(boolean afficheCaisse) {
		this.afficheCaisse = afficheCaisse;
	}

	public TaSessionCaisse getTaSessionCaisse() {
		return taSessionCaisse;
	}

	public void setTaSessionCaisse(TaSessionCaisse taSessionCaisse) {
		this.taSessionCaisse = taSessionCaisse;
	}

	public TaSessionCaisseDTO getTaSessionCaisseDTO() {
		return taSessionCaisseDTO;
	}

	public void setTaSessionCaisseDTO(TaSessionCaisseDTO taSessionCaisseDTO) {
		this.taSessionCaisseDTO = taSessionCaisseDTO;
	}

	public TaFondDeCaisse getTaFondDeCaisseCloture() {
		return taFondDeCaisseCloture;
	}

	public void setTaFondDeCaisseCloture(TaFondDeCaisse taFondDeCaisseCloture) {
		this.taFondDeCaisseCloture = taFondDeCaisseCloture;
	}

	public TaDepotRetraitCaisse getDepotCaisse() {
		return depotCaisse;
	}

	public void setDepotCaisse(TaDepotRetraitCaisse depotCaisse) {
		this.depotCaisse = depotCaisse;
	}

	public TaDepotRetraitCaisse getRetraitCaisse() {
		return retraitCaisse;
	}

	public void setRetraitCaisse(TaDepotRetraitCaisse retraitCaisse) {
		this.retraitCaisse = retraitCaisse;
	}

	public TaFondDeCaisse getTaFondDeCaisseOuverture() {
		return taFondDeCaisseOuverture;
	}

	public void setTaFondDeCaisseOuverture(TaFondDeCaisse taFondDeCaisseOuverture) {
		this.taFondDeCaisseOuverture = taFondDeCaisseOuverture;
	}

	

//	public boolean autoriseSuppression() {
//		IDocumentTiers docLie=rechercheSiDocLie();
//		if(docLie!=null) {
//			RequestContext context = RequestContext.getCurrentInstance();
//			context.addCallbackParam("Autorise la suppression", docLie!=null);
//			if(docLie!=null)
//				context.showMessageInDialog(new FacesMessage("Suppression d'un document lié à une autre", "Ce bon de livraison est lié à la facture n° "+docLie.getCodeDocument()+"."
//						+ "\r\nSi vous souhaitez le supprimer, vous devez au préalable supprimer cette facture."));
//		}
//		return docLie==null;
//	}
	
//	public IDocumentTiers dejaGenereDocument(IDocumentTiers ds) {
//		IDocumentTiers dejaGenere = null;
//		String jpql = "select x.taFacture from TaRDocument x where x.taTicketDeCaisse.idDocument="+ds.getIdDocument()+" and x.taFacture is not null";
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

}
