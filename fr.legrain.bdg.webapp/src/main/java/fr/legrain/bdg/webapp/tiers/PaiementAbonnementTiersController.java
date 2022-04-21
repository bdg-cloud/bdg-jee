package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Response;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.cron.service.remote.ITaCronServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.documents.TaLigneServiceWebExterneDTOJSF;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.generation.service.CreationDocumentLigneEcheanceAbonnementMultiple;
import fr.legrain.generation.service.CreationDocumentLigneServiceWebExterneMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.EnteteDocExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class PaiementAbonnementTiersController extends AbstractController implements Serializable { 
	


	/**
	 * 
	 */
	private static final long serialVersionUID = -7185392082333360732L;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	@EJB private CreationDocumentLigneEcheanceAbonnementMultiple creation;
	
	private String css;
	
	private String paramId;
	
	private TabView tabViewArticle; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaPrelevementServiceRemote taPrelevementService;
	private @EJB ITaReglementServiceRemote taReglementService;

	private @EJB ITaTiersServiceRemote taTiersService;
	
	private @EJB ITaPanierServiceRemote taPanierService;
	private @EJB ITaLPanierServiceRemote taLPanierService;

	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	private @EJB ITaLAbonnementServiceRemote taLAbonnementService;
	private @EJB ITaLEcheanceServiceRemote taLEcheanceService;
	private @EJB ITaEtatServiceRemote taEtatService;
	private @EJB ITaTPaiementServiceRemote taTPaiementService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	private @EJB ITaCronServiceRemote taCronService;
	
	protected ImageLgr selectedTypeCreation;
	

	protected TaLEcheance ligneDocument = null;
	
	@EJB private IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote genereEcheanceVersPanier;

	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;

	private List<TaAbonnementFullDTO> listeAllAbonnement = new ArrayList<>();

	private List<TaLEcheance> listeEcheanceNonEmiseAbonnement = new ArrayList<>();
	private List<TaLEcheance> listeLEcheance = new ArrayList<>();
	
	private List<TaLEcheance> selectedLignesEcheances = new ArrayList<>();

	private TaTiers masterEntity;
	private TaTiersDTO taTiersDTO;
	private boolean modeDialogue;
	
	private MoyenPaiementAbonnementDTOJSF selectedMoyenPaiementAbonnementDTOJSF;
	
	private boolean regleDoc = true;
	
	private List<TaStripeSourceDTO> taTPaiement = new ArrayList<>();
	
	private TaStripeSourceDTO selectedTaTPaiement;
	
	private boolean cronValide = true;
	
	private List<TaAvisEcheanceDTO> listeAvis = new ArrayList<>();
	private TaAvisEcheanceDTO selectedAvis;
	
	private BigDecimal totalTTCSelectedEch = BigDecimal.ZERO;
	private BigDecimal totalHTSelectedEch = BigDecimal.ZERO;




	public PaiementAbonnementTiersController() {  
	}  

	@PostConstruct
	public void postConstruct(){
		//refresh(null);
		
		selectedTaTPaiement = null;
		selectedAvis = null;
	}
	

	public void refresh(){
		refresh(null);
	}
	public void refresh(ActionEvent ev){
		verifPassageCRON();
		initTaStripeSource();
		initAvisEcheance();
		
		
		
			if(selectedLignesEcheances != null) {
				selectedLignesEcheances.clear();
			}
		
	   	    if(masterEntity!=null) {
	   	    	
					if(masterEntity.getIdTiers()!=0)
						try {
							masterEntity = taTiersService.findById(masterEntity.getIdTiers());
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}					
						//listeLEcheance = taLEcheanceService.rechercheEcheanceNonLieAAvisEcheanceTiers(masterEntity.getCodeTiers());
//					if(selectedTaTPaiement != null) {
//						TaTPaiement tpaiement = rechercheTPaiement(selectedTaTPaiement);
//						if(tpaiement != null) {
//							listeLEcheance = taLEcheanceService.rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement(masterEntity.getCodeTiers(), tpaiement.getCodeTPaiement());
//						}else {
//							listeLEcheance = new ArrayList<>();
//						}
						
					//}else {
					
					initLigneEcheance();
					
					
						
					//}
						
						//listeEcheanceNonEmiseAbonnement = taLEcheanceService.rechercheEcheanceNonLieAAvisEcheanceSubscription(taStripeSubscriptionService.findById(selectedTaStripeSubscriptionDTO.getId()));
			} else {
				
	//			values = new ArrayList<>();
//				autoCompleteMapDTOtoUI();
//				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			}
	}
	
	
	public void initLigneEcheance() {
		initLigneEcheance(null);
	}
	/**
	 * a la base ce code etait dans le refresh mais j'ai constaté qu'il ne fonctionnait pas correctement dans le selectOneMenu avec refresh en listener, il vaut donc mieu créer un listener spécifique 
	 * Le selectedAvis sera toujours null car j'ai enlevé la combo permettant de choisir un avis dans la vue, car on ne fait plus de laision entre avis et ligne d'échéance
	 * @param ev
	 */
	public void initLigneEcheance(ActionEvent ev) {
		if(selectedAvis != null) {
			listeLEcheance = taLEcheanceService.rechercheEcheanceBycodeAvisEcheance(selectedAvis.getCodeDocument());
		}else {
			listeLEcheance = taLEcheanceService.rechercheEcheanceEnCoursOuSuspendusTiersAndByCodeTPaiement(masterEntity.getCodeTiers(), null);
		}
	}
	
	/**
	 * Cette méthode vérifie si le CRON responsable de la génération des prochaines échéances, de la suspension et suppression des échéances,
	 *  et suspension ou annulation des lignes d'abo est bien passé il y a moins de 24 heures
	 */
	public void verifPassageCRON() {
		cronValide = taCronService.verifPassageCRONGenerationEcheance();				
			
	}
	
	public void initAvisEcheance() {
		listeAvis = taAvisEcheanceService.rechercheNonLieAFactureByIdTiers(masterEntity.getIdTiers());
	}
	
	public TaTPaiement rechercheTPaiement(TaStripeSourceDTO stripeSource) {
		TaTPaiement tpaiement = null;
		if(stripeSource.getAutomatique() == null || !stripeSource.getAutomatique()) {
				//Mode manuel, pas de prélèvement automatique
//					** Comment stocker le type de moyen de paiement "manuel" ? différent des moyen de paiement automatique type source stripe
//					** Créer des sources "virtuelles" ? ou créer une table avec des moyens de paiement "fixe" ? utiliser TPaiement 
//							** ? Ajouter des booleens dans les StripeSubscription ? ou un ID vers une de ces nouvelles tables ?
				//rajout yann
				if(stripeSource.getIdExterne() != null) {
					if(stripeSource.getIdExterne().equals("Virement")) {
						try {
							tpaiement = taTPaiementService.findByCode("VIR");
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(stripeSource.getIdExterne().equals("Chèque")) {
						try {
							tpaiement = taTPaiementService.findByCode("C");
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(stripeSource.getIdExterne().equals("CB")) {
						try {
							tpaiement = taTPaiementService.findByCode("CB");
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				
			} else {
				//prélevement SEPA ou debit sur une carte de credit enregistrée
				TaStripeSource stripeSrc = taStripeSourceService.rechercherSource(stripeSource.getIdExterne());
				//rajout yann
				if(stripeSrc!= null) {
					switch (stripeSrc.getTaStripeTSource().getCodeStripeTSource()) {
					case "CB":
						try {
							tpaiement = taTPaiementService.findByCode("CB");
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;

					case "PREL_SEPA":
						try {
							tpaiement = taTPaiementService.findByCode("PREL");
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					
				}
			}
		//rajout yann
		return tpaiement;
	}
	
	public void initTaStripeSource() {
		if(masterEntity != null) {
			TaStripeCustomer taStripeCustomer = taStripeCustomerService.rechercherCustomer(masterEntity);
			if(taStripeCustomer!=null) {
				taTPaiement = taStripeSourceService.rechercherSourceCustomerDTO(taStripeCustomer.getIdExterne());
			} else {
				taTPaiement = taStripeSourceService.rechercherSourceCustomerDTO(null);
			}
			
			TaStripeSourceDTO paiementCheque = new TaStripeSourceDTO();
			paiementCheque.setIdExterne("Chèque");
			paiementCheque.setLiblStripeTSource("Non automatique");
			taTPaiement.add(paiementCheque);

			TaStripeSourceDTO paiementVirement = new TaStripeSourceDTO();
			paiementVirement.setIdExterne("Virement");
			paiementVirement.setLiblStripeTSource("Non automatique");
			taTPaiement.add(paiementVirement);
			
			TaStripeSourceDTO paiementCb= new TaStripeSourceDTO();
			paiementCb.setIdExterne("CB");
			paiementCb.setLiblStripeTSource("Non automatique");
			taTPaiement.add(paiementCb);
		}
		
	}
	
	
//	public List<TaStripeSourceDTO> taStripeSourceAutoCompleteLight(String query) {
//		List<TaStripeSourceDTO> filteredValues = new ArrayList<TaStripeSourceDTO>();
//		List<TaStripeSourceDTO> allValues = null;
//
//		if(taStripeCustomer!=null) {
//			allValues = taStripeSourceService.rechercherSourceCustomerDTO(taStripeCustomer.getIdExterne());
//		} else {
//			allValues = taStripeSourceService.rechercherSourceCustomerDTO(null);
//		}
//
//			if(allValues!=null) {
//				for (int i = 0; i < allValues.size(); i++) {
//					TaStripeSourceDTO civ = allValues.get(i);
//					if(query.equals("*") 
//							|| (civ.getIdExterne()!=null && civ.getIdExterne().toLowerCase().contains(query.toLowerCase()))
//							|| (civ.getAutomatique()!=null && !civ.getAutomatique())
//							) {
//						filteredValues.add(civ);
//					}
//				}
//			}
//			
//		if(allValues.isEmpty()) {
//			selectedTaStripeSourceSubscriptionDTO = null;
//		}
//		
//		TaStripeSourceDTO paiementCheque = new TaStripeSourceDTO();
//		paiementCheque.setIdExterne("Chèque");
//		filteredValues.add(paiementCheque);
//
//		TaStripeSourceDTO paiementVirement = new TaStripeSourceDTO();
//		paiementVirement.setIdExterne("Virement");
//		filteredValues.add(paiementVirement);
//		
//		TaStripeSourceDTO paiementCb= new TaStripeSourceDTO();
//		paiementCb.setIdExterne("CB");
//		filteredValues.add(paiementCb);
//
//		return filteredValues;
//	}
	
	
	
	
	public void calculTotauxEcheance() {
		totalTTCSelectedEch = BigDecimal.ZERO;
		totalHTSelectedEch = BigDecimal.ZERO;
		
		for (TaLEcheance ech : selectedLignesEcheances) {
			 totalTTCSelectedEch = totalTTCSelectedEch.add(ech.getMtTtcLDocument());
			 totalHTSelectedEch = totalHTSelectedEch.add(ech.getMtHtLDocument());
		}
		System.out.println("total ttc : "+totalTTCSelectedEch);
	}
	
    public void onRowSelectLigneEcheanceAll(SelectEvent event){
    	calculTotauxEcheance();
    	System.out.println("onRowSelectLigneEcheanceAll a etait cliquéééé");
    }
    public void onRowUnSelectLigneEcheanceAll(UnselectEvent event){
    	calculTotauxEcheance();
    	System.out.println("onRowUnSelectLigneEcheanceAll a etait cliquéééé");
    }
    public void onToggleSelectLigneEcheanceAll(ToggleSelectEvent event){
    	calculTotauxEcheance();
    	System.out.println("onToggleSelectLigneEcheanceAll a etait cliquéééé");
    }
    
    public void actionGroupee1() {    	
		taLEcheanceService.regleEcheances(selectedLignesEcheances);	
		refresh();    	
    }
    
    
    public void actionGroupee2() {
    	selectedTypeCreation = new ImageLgr();
		selectedTypeCreation.setDisplayName("Facture");
		selectedTypeCreation.setName("Facture");
    	System.out.println("actEnregistrer  ");
		FacesContext context = FacesContext.getCurrentInstance();
				//créer le document à partir des documents selectionné dans le modelDocument
				final  ParamAfficheChoixGenerationDocument paramTmp = new ParamAfficheChoixGenerationDocument();
				ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
				String finDeLigne = "\r\n";
				param.setTypeSrc("");
				param.setTypeDest("");
				String libelle="Reprise de ";
				
				
				paramTmp.setLibelle("Lignes d'échéances séléctionnées");
				
		if(selectedLignesEcheances != null && !selectedLignesEcheances.isEmpty()) {
			actDialogGenerationDocument(null);
		}else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide", "Veuillez choisir au moins une ligne pour pouvoir générer un document.")); 
		} 
    }
    
    
	public void actionGroupee3() {
		selectedTypeCreation = new ImageLgr();
		selectedTypeCreation.setDisplayName("Avis Echeance");
		selectedTypeCreation.setName("AvisEcheance");
		System.out.println("actEnregistrer  ");
		FacesContext context = FacesContext.getCurrentInstance();
				//créer le document à partir des documents selectionné dans le modelDocument
				final  ParamAfficheChoixGenerationDocument paramTmp = new ParamAfficheChoixGenerationDocument();
				ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
				String finDeLigne = "\r\n";
				param.setTypeSrc("");
				param.setTypeDest("");
				String libelle="Reprise de ";
				
				
				paramTmp.setLibelle("Lignes d'échéances séléctionnées");
				
		if(selectedLignesEcheances != null && !selectedLignesEcheances.isEmpty()) {
			actDialogGenerationDocument(null);
		}else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide", "Veuillez choisir au moins une ligne pour pouvoir générer un document.")); 
		} 
		
	}
	
	public void actionGroupee4() {
//		selectedTypeCreation = new ImageLgr();
//		selectedTypeCreation.setDisplayName("Panier");
//		selectedTypeCreation.setName("Panier");
//		System.out.println("actEnregistrer  ");
		FacesContext context = FacesContext.getCurrentInstance();
//				//créer le document à partir des documents selectionné dans le modelDocument
//				final  ParamAfficheChoixGenerationDocument paramTmp = new ParamAfficheChoixGenerationDocument();
//				ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
//				String finDeLigne = "\r\n";
//				param.setTypeSrc("");
//				param.setTypeDest("");
//				String libelle="Reprise de ";
//				
//				
//				paramTmp.setLibelle("Lignes d'échéances séléctionnées");
//				
//		if(selectedLignesEcheances != null && !selectedLignesEcheances.isEmpty()) {
//			actDialogGenerationDocument(null);
//		}else {
//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide", "Veuillez choisir au moins une ligne pour pouvoir générer un document.")); 
//		} 
	    LgrDozerMapper<TaPanierDTO,TaPanier> mapperUIToModel = new LgrDozerMapper<>();

		TaPanierDTO dto = null;

		TaPanier entity = null;
  		
		//rechercher le panier
		int id = 1;
		String codeTiers = masterEntity.getCodeTiers();
		entity = taPanierService.findByActif(codeTiers);


		if(entity!=null) {
			//un panier actif existe
			try {
				dto = taPanierService.findByIdDTO(entity.getIdDocument());
				dto.setLignesDTO(new ArrayList<TaLPanierDTO>());
				TaLPanierDTO ldto = null;
				for (TaLPanier l : entity.getLignes()) {
					ldto = taLPanierService.findByIdDTO(l.getIdLDocument());
					dto.getLignesDTO().add(ldto);
				}
				entity.getTaInfosDocument().setTaDocument(null); //pour ne pas surcharger le DTO, faire des DTO pour les infos document ?
				dto.setInfos(entity.getTaInfosDocument());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		} else {
			//pas de panier actif il faut créer un nouveau panier vide
			dto = new TaPanierDTO();
			TaInfosPanier taInfosDocument = null;
			entity = new TaPanier();
			TaTiers tiers;
			try {
				tiers = taTiersService.findByCode(codeTiers);
				if(taInfosDocument==null) {
					//taInfosDocument = newInfosDocument();
					taInfosDocument = new TaInfosPanier();
				}
				if(entity!=null) {
					taInfosDocument.setTaDocumentGeneral(entity);
					entity.setTaInfosDocument(taInfosDocument);
				}

				dto.setCodeDocument(taPanierService.genereCode(null)); //ejb
				dto.setIdTiers(tiers.getIdTiers());
				dto.setCodeTiers(tiers.getCodeTiers());
				dto.setNomTiers(tiers.getNomTiers());
				dto.setPrenomTiers(tiers.getPrenomTiers());
				entity.setTaTiers(tiers);
				dto.setCodeCompta(entity.getTaTiers().getCodeCompta());
				dto.setCompte(entity.getTaTiers().getCompte());
				entity.getTaInfosDocument().setCodeCompta(entity.getTaTiers().getCodeCompta());
				entity.getTaInfosDocument().setCompte(entity.getTaTiers().getCompte());
				entity.getTaInfosDocument().setNomTiers(entity.getTaTiers().getNomTiers());
				entity.getTaInfosDocument().setPrenomTiers(entity.getTaTiers().getPrenomTiers());

				dto.setLibelleDocument("Panier boutique");

				dto.setLignesDTO(new ArrayList<TaLPanierDTO>());

				mapperUIToModel.map(dto, entity);

				entity = taPanierService.merge(entity);

			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			


		}

		
		
		TaPanier t = entity;
		//TaPanierDTO dto = new TaPanierDTO();
	
		try {
			t = taPanierService.findByIDFetch(entity.getIdDocument());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(t.getNbDecimalesPrix()==null) {
			t.setNbDecimalesPrix(2);
		}
		
		
		if(selectedLignesEcheances != null && !selectedLignesEcheances.isEmpty()) {
			
			for (TaLEcheance ech : selectedLignesEcheances) {
				 try {
					ech = taLEcheanceService.findById(ech.getIdLEcheance());
					boolean existe = false;
					//on vérifie qu'une ligne de ce panier n'est pas deja lié a cette échéance
					for (TaLPanier ligne : t.getLignes()) {
						ligne = taLPanierService.findById(ligne.getIdLDocument());
						for(TaLigneALigneEcheance lal : ligne.getTaLigneALignesEcheance()) {
							if(lal.getTaLEcheance().getIdLEcheance() == ech.getIdLEcheance()) {
								existe = true;
								break;
							}
						}
					}
					
					
					if(!existe) {
						List<TaLEcheance> listeEch = new ArrayList<TaLEcheance>();			
						listeEch.add(ech);			
						t = (TaPanier) genereEcheanceVersPanier.copieDocumentSpecifique(listeEch, t, false, "");
						t.calculeTvaEtTotaux();
						t = taPanierService.merge(t);
					}
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				 
			}
			
			
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Ajout au panier", "Les échéances choisies ont été mises au panier du tiers (ou elles y sont déjà).")); 
			
			
			

			//dto = recupereDerniereVersionPanierDTO(t);
			
		}else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide", "Veuillez choisir au moins une ligne pour pouvoir générer un document.")); 
		} 

		
		
		
		
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
		paramGeneration.setTypeSrc("");
		paramGeneration.setTypeDest(selectedTypeCreation.getName());
//		String libelle="Reprise de ";
		paramGeneration.setDateDocument(new Date());
		paramGeneration.setDateLivraison(new Date());
		paramGeneration.setLibelle("");
		paramGeneration.setTiersModifiable(false);
		paramGeneration.setMultiple(true);
		

		paramGeneration.setTitreFormulaire("Génération multiple");
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}
    
	public void handleReturnDialogGenerationDocument(SelectEvent event) {
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			
			if(selectedLignesEcheances != null && !selectedLignesEcheances.isEmpty()){
				
				if(event!=null && event.getObject()!=null) {
					if(event.getObject() instanceof ParamAfficheChoixGenerationDocument){
						ParamAfficheChoixGenerationDocument param =  (ParamAfficheChoixGenerationDocument) event.getObject();
						if(param.getRetour()){
							String finDeLigne = "\r\n";
		
							List<IDocumentTiers> listeDocCrees=new LinkedList<IDocumentTiers>();
		
		
							param.getListeLigneEcheanceSrc().clear();
							param.setDateLivraison(param.getDateDocument());

							for (TaLEcheance ligne : selectedLignesEcheances) {
								TaLEcheance l = ligneDocumentValide(ligne.getIdLEcheance());
								if(l != null) {
									param.getListeLigneEcheanceSrc().add(ligne);
								}
								
							}
												

							RetourGenerationDoc retour=null;
							String idCommande = null;
							String codeTiers = selectedLignesEcheances.get(0).getTaLAbonnement().getTaDocument().getTaTiers().getCodeTiers();
							
							//creation document						
							if(param.getListeLigneEcheanceSrc().size()!=0){
								retour=null; 
								param.setCodeTiers(codeTiers);
								//param.setIdDocumentExterne(idCommande);
								//param.setCodeServiceExterne(serviceWeb.getCodeServiceWebExterne());
								//param.setTypeEntiteExterne(typeEntite);
								creation.setCodeTiers(codeTiers);
								creation.setIdDocumentExterne(idCommande);
								creation.setParam(param);
								creation.setRegleDoc(regleDoc);
								retour=creation.creationDocument(false);
								if(retour!=null && retour.isRetour()&&retour.getDoc()!=null){
									listeDocCrees.add(retour.getDoc());								
								}
							}
							
							param.getListeLigneEcheanceSrc().clear();
								
								
							

							
							
									
		
							String message="";
							for (IDocumentTiers iDocumentTiers : listeDocCrees) {
								message+=iDocumentTiers.getCodeDocument()+finDeLigne;
							}
							if (!message.equals("")){
								LgrTab typeDocPresent = LgrTab.getInstance();
								String tab = typeDocPresent.getTabDocCorrespondance().get(selectedTypeCreation.getName());
								//"tab tab-facture"
								retour.setTypeTabCSS(tab);
								css = tab;
								//retour.setOuvrirDoc(true);
								if(retour.isOuvrirDoc()) {
									ouvertureDocumentBean.detailDocument(retour.getDoc());
								}
															
							}
							
							if(retour.getMessage()!=null && !retour.getMessage().isEmpty())
								context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", retour.getMessage()));
//		
		
						}
		
		
					}
		
				}
				refresh();
			}else{
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide","Aucunes lignes séléctionnés")); 	
			}
				
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	private TaLEcheance ligneDocumentValide(Integer id){
		try {	
				//ligneDocument=ligneServiceWebExterneService.findById(id);
			ligneDocument= taLEcheanceService.findById(id);
			return ligneDocument;
		} catch (FinderException e) {
			return null;
		}
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
			context.addMessage(null, new FacesMessage("Paiement abonnement", "actAnnuler"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public void autoCompleteMapUIToDTO() {

	}
	public void autoCompleteMapDTOtoUI() {
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
	public void updateTab(){
		try {

		autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
//	
//	public void actDialogTypeCompteBanque(ActionEvent actionEvent) {
//		
//  
//        Map<String,Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
//        options.put("draggable", false);
//        options.put("resizable", false);
//        options.put("contentHeight", 640);
//        options.put("modal", true);
//        
//
//        Map<String,List<String>> params = new HashMap<String,List<String>>();
//        List<String> list = new ArrayList<String>();
//        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
//        params.put("modeEcranDefaut", list);
//        
//        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_banque", options, params);
// 	    
//	}
	
	public List<TaTiersDTO> articleAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight(query.toUpperCase());
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toUpperCase().contains(query.toUpperCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}
	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}
	public TabViewsBean getTabViews() {
		return tabViews;
	}
	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public TabView getTabViewArticle() {
		return tabViewArticle;
	}
	public void setTabViewArticle(TabView tabViewArticle) {
		this.tabViewArticle = tabViewArticle;
	}
	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}
	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}
	public List<TaAbonnementFullDTO> getListeAllAbonnement() {
		return listeAllAbonnement;
	}
	public void setListeAllAbonnement(List<TaAbonnementFullDTO> listeAllAbonnement) {
		this.listeAllAbonnement = listeAllAbonnement;
	}
	public List<TaLEcheance> getListeEcheanceNonEmiseAbonnement() {
		return listeEcheanceNonEmiseAbonnement;
	}
	public void setListeEcheanceNonEmiseAbonnement(List<TaLEcheance> listeEcheanceNonEmiseAbonnement) {
		this.listeEcheanceNonEmiseAbonnement = listeEcheanceNonEmiseAbonnement;
	}
	public List<TaLEcheance> getListeLEcheance() {
		return listeLEcheance;
	}
	public void setListeLEcheance(List<TaLEcheance> listeLEcheance) {
		this.listeLEcheance = listeLEcheance;
	}
	public TaTiers getMasterEntity() {
		return masterEntity;
	}
	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}
	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}
	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}
	public boolean isModeDialogue() {
		return modeDialogue;
	}
	public void setModeDialogue(boolean modeDialogue) {
		this.modeDialogue = modeDialogue;
	}
	public MoyenPaiementAbonnementDTOJSF getSelectedMoyenPaiementAbonnementDTOJSF() {
		return selectedMoyenPaiementAbonnementDTOJSF;
	}
	public void setSelectedMoyenPaiementAbonnementDTOJSF(
			MoyenPaiementAbonnementDTOJSF selectedMoyenPaiementAbonnementDTOJSF) {
		this.selectedMoyenPaiementAbonnementDTOJSF = selectedMoyenPaiementAbonnementDTOJSF;
	}
	public List<TaLEcheance> getSelectedLignesEcheances() {
		return selectedLignesEcheances;
	}
	public void setSelectedLignesEcheances(List<TaLEcheance> selectedLignesEcheances) {
		this.selectedLignesEcheances = selectedLignesEcheances;
	}

	public ImageLgr getSelectedTypeCreation() {
		return selectedTypeCreation;
	}
	public void setSelectedTypeCreation(ImageLgr selectedTypeCreation) {
		this.selectedTypeCreation = selectedTypeCreation;
	}
	public TaLEcheance getLigneDocument() {
		return ligneDocument;
	}
	public void setLigneDocument(TaLEcheance ligneDocument) {
		this.ligneDocument = ligneDocument;
	}
	public boolean isRegleDoc() {
		return regleDoc;
	}

	public void setRegleDoc(boolean regleDoc) {
		this.regleDoc = regleDoc;
	}

	public List<TaStripeSourceDTO> getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(List<TaStripeSourceDTO> taTPaiement) {
		this.taTPaiement = taTPaiement;
	}

	public TaStripeSourceDTO getSelectedTaTPaiement() {
		return selectedTaTPaiement;
	}

	public void setSelectedTaTPaiement(TaStripeSourceDTO selectedTaTPaiement) {
		this.selectedTaTPaiement = selectedTaTPaiement;
	}
	public boolean isCronValide() {
		return cronValide;
	}

	public void setCronValide(boolean cronValide) {
		cronValide = cronValide;
	}

	public List<TaAvisEcheanceDTO> getListeAvis() {
		return listeAvis;
	}

	public void setListeAvis(List<TaAvisEcheanceDTO> listeAvis) {
		this.listeAvis = listeAvis;
	}

	public TaAvisEcheanceDTO getSelectedAvis() {
		return selectedAvis;
	}

	public void setSelectedAvis(TaAvisEcheanceDTO selectedAvis) {
		this.selectedAvis = selectedAvis;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}


	public BigDecimal getTotalTTCSelectedEch() {
		return totalTTCSelectedEch;
	}

	public void setTotalTTCSelectedEch(BigDecimal totalTTCSelectedEch) {
		this.totalTTCSelectedEch = totalTTCSelectedEch;
	}

	public BigDecimal getTotalHTSelectedEch() {
		return totalHTSelectedEch;
	}

	public void setTotalHTSelectedEch(BigDecimal totalHTSelectedEch) {
		this.totalHTSelectedEch = totalHTSelectedEch;
	}

}  
