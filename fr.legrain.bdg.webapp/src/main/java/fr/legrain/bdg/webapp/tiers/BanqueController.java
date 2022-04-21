package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.abonnement.stripe.model.TaStripeTSource;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeBankAccountServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeTSourceServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;
import fr.legrain.tiers.dto.TaTBanqueDTO;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTBanque;
import fr.legrain.tiers.model.TaTiers;

@Named
//@ViewScoped
@Dependent
public class BanqueController extends AbstractController implements Serializable { 
	


	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementEnLigneDossierService;
//	private @EJB PaiementPaypalDossierService paiementEnLigneDossierService;
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeTSourceServiceRemote taStripeTSourceService;
	private @EJB ITaStripeBankAccountServiceRemote taStripeBankAccountService;
	
	private String paramId;
	
	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaCompteBanqueDTO> values; 
	private List<TaCompteBanqueDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
	private @EJB ITaTBanqueServiceRemote taTBanqueService;
	private @EJB ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;

	
	private TaCompteBanqueDTO[] selectedTaCompteBanqueDTOs; 
	private TaCompteBanque taCompteBanque = new TaCompteBanque();
	private TaCompteBanqueDTO newTaCompteBanqueDTO = new TaCompteBanqueDTO();
	private TaCompteBanqueDTO selectedTaCompteBanqueDTO = new TaCompteBanqueDTO();
//	private TaCompteBanqueDTO oldSelectedTaCompteBanqueDTO = new TaCompteBanqueDTO();
	
	private TaTBanque taTBanque;
	
	private LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque> mapperUIToModel  = new LgrDozerMapper<TaCompteBanqueDTO,TaCompteBanque>();
	private LgrDozerMapper<TaCompteBanque,TaCompteBanqueDTO> mapperModelToUI  = new LgrDozerMapper<TaCompteBanque,TaCompteBanqueDTO>();
	
	private TaTiers masterEntity;




	private TaTBanqueDTO taTBanqueDTO;
	
	public BanqueController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){

		refresh(null);

	}
	public void refresh(){
		refresh(null);
	}
	public void refresh(ActionEvent ev){
//		selectedTaCompteBanqueDTO=null;
		 
   	    if(this.masterEntity!=null) {
   	    	try {
				if(masterEntity.getIdTiers()!=0) masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			} catch (FinderException e) {
				
			}
			values = new ArrayList<>();
			for (TaCompteBanque taCompteBanque : masterEntity.getTaCompteBanques()) {
				TaCompteBanqueDTO dto=new TaCompteBanqueDTO();
				mapperModelToUI.map(taCompteBanque, dto);
				values.add(dto);
			}

			if(!values.isEmpty()&&selectedTaCompteBanqueDTO==null)
				selectedTaCompteBanqueDTO=values.get(0);
			else if(values==null || values.isEmpty())
				selectedTaCompteBanqueDTO=null;
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaCompteBanqueDTO> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				taCompteBanque = new TaCompteBanque();
				mapperModelToUI.map(taCompteBanque,selectedTaCompteBanqueDTO );
				selectedTaCompteBanqueDTO=new TaCompteBanqueDTO();
				
				if(!values.isEmpty()) selectedTaCompteBanqueDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaCompteBanqueDTO.getId()!=null && selectedTaCompteBanqueDTO.getId()!=0){
					taCompteBanque = taCompteBanqueService.findById(selectedTaCompteBanqueDTO.getId());
					selectedTaCompteBanqueDTO=taCompteBanqueService.findByIdDTO(selectedTaCompteBanqueDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taCompteBanque, selectedTaCompteBanqueDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public boolean compteExisteDansStripe() {
		TaStripeSource s = null;
		try {
			s = taStripeSourceService.rechercherSource(taCompteBanqueService.findById(selectedTaCompteBanqueDTO.getId()));
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s!=null;
	}
	
	public void actCreerCompteDansStripe(ActionEvent actionEvent) {
		try {
			taCompteBanque = taCompteBanqueService.findById(selectedTaCompteBanqueDTO.getId());
			TaCompteServiceWebExterne compte = taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
			if(compte!=null) {
				TaStripeSource s = taStripeSourceService.rechercherSource(taCompteBanque);
				TaStripeSourceDTO taStripeSourceDTO = null;
				if((s == null) && (compte!=null)) {//si pas de source pour ce moyen de paiement
					//créer une source 
					s = new TaStripeSource();
					String idSourceStripe = paiementEnLigneDossierService.creerSourcePrelevementSEPA(compte,selectedTaCompteBanqueDTO.getIban(),masterEntity.getNomTiers());
					s.setIdExterne(idSourceStripe);
					s.setTaCompteBanque(taCompteBanqueService.findById(selectedTaCompteBanqueDTO.getId()));
					s.setTaStripeTSource(taStripeTSourceService.findByCode(TaStripeTSource.TYPE_SOURCE_PREL_SEPA));
					
					//Provoque une transaction rolled back car un autre merge sur la même entité vien forcément plus bas
					//s = taStripeSourceService.merge(s);
					
					taStripeSourceDTO = paiementEnLigneDossierService.rechercherSource(compte,idSourceStripe);
					taCompteBanque.setLast4(taStripeSourceDTO.getTaCompteBanqueDTO().getLast4());
					taCompteBanque.setBankCode(taStripeSourceDTO.getTaCompteBanqueDTO().getBankCode());
					taCompteBanque.setBranchCode(taStripeSourceDTO.getTaCompteBanqueDTO().getBranchCode());
					taCompteBanque.setCountry(taStripeSourceDTO.getTaCompteBanqueDTO().getCountry());
					taCompteBanque = taCompteBanqueService.merge(taCompteBanque);
					mapperModelToUI.map(taCompteBanque,selectedTaCompteBanqueDTO );
				}
				
				TaStripeCustomer taStripeCustomer =  taStripeCustomerService.rechercherCustomer(masterEntity);
				if(taStripeCustomer!=null) {
					//l'ajouter à ce customer
					paiementEnLigneDossierService.attacherSourceStripe(compte,s.getIdExterne(), taStripeCustomer.getIdExterne());
					s.setTaStripeCustomer(taStripeCustomer);
					//s (source) est mergé en cascade par le merge du customer
					//s = taStripeSourceService.merge(s);
					//si ce tiers n'a pas de source par defaut, affecter cette nouvelle source en tant que source par defaut
					taStripeCustomer.getSources().add(s);
					taStripeCustomer.setTaSourceDefault(s);
					taStripeCustomer = taStripeCustomerService.merge(taStripeCustomer);
				} else {
					//créer un customer avec cette source par defaut
					if(masterEntity.getTaEmail()!=null && masterEntity.getTaEmail().getAdresseEmail()!=null) {
						String idCustomerStripe = paiementEnLigneDossierService.creerCustomerStripe(compte,masterEntity.getTaEmail().getAdresseEmail(), s.getIdExterne(), masterEntity.getNomTiers()+" "+masterEntity.getCodeTiers());
						taStripeCustomer = new TaStripeCustomer();
						taStripeCustomer.setIdExterne(idCustomerStripe);
						taStripeCustomer.setEmail(masterEntity.getTaEmail().getAdresseEmail());
						taStripeCustomer.setTaTiers(masterEntity);
						if(taStripeCustomer.getSources()==null) {
							taStripeCustomer.setSources(new HashSet<>());
						}
						taStripeCustomer.getSources().add(s);
						taStripeCustomer.setTaSourceDefault(s);
						
						s.setTaStripeCustomer(taStripeCustomer);
						
						taStripeCustomer = taStripeCustomerService.merge(taStripeCustomer);
						
						//s (source) est mergé en cascade par le merge du customer donc pas besoin de celui-ci
						//s = taStripeSourceService.merge(s);
					} else {
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Compte banque", "Le tiers doit avoir une adresse email pour pouvoir être créé dans le service bancaire."));
					}
				}
			} else {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Compte banque", "Veuillez d'abord saisir vos identifiants/clé API Stripe dans les paramètres."));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTBanqueDTO!=null) {
			validateUIField(Const.C_CODE_T_BANQUE,taTBanqueDTO.getCodeTBanque());
			selectedTaCompteBanqueDTO.setCodeTBanque(taTBanqueDTO.getCodeTBanque());
		} else {
			selectedTaCompteBanqueDTO.setCodeTBanque(null);
		}
		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTBanqueDTO = null;
			if(selectedTaCompteBanqueDTO!=null){
				if(selectedTaCompteBanqueDTO.getCodeTBanque()!=null && !selectedTaCompteBanqueDTO.getCodeTBanque().equals("")) {
					taTBanqueDTO=taTBanqueService.findByCodeDTO(selectedTaCompteBanqueDTO.getCodeTBanque());
				}	
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaCompteBanque rechercheCompteBanqueDansTiers(TaCompteBanqueDTO dto){
		for (TaCompteBanque adr : masterEntity.getTaCompteBanques()) {
			if(adr.getIdCompteBanque()==dto.getId()) {
				return adr;
			}
		}
		return null;
	}
	

	

	

	

	
	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaCompteBanque taCompteBanque = new TaCompteBanque();

		autoCompleteMapUIToDTO();
//		initCompteBanqueNull();
				
		mapperUIToModel.map(selectedTaCompteBanqueDTO, taCompteBanque);
		

		
		try {
			taCompteBanque.setTaTiers(masterEntity);
			masterEntity.addCompteBanque(taCompteBanque);	
			

			
			
			taCompteBanque = taCompteBanqueService.merge(taCompteBanque,ITaTiersServiceRemote.validationContext);
			
			mapperModelToUI.map(taCompteBanque, selectedTaCompteBanqueDTO);
			autoCompleteMapDTOtoUI();
			

			masterEntity = taTiersService.findById(masterEntity.getIdTiers());

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaCompteBanqueDTO);
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch(Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", e.getMessage()));
		}

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer"));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
		try {
			selectedTaCompteBanqueDTO = new TaCompteBanqueDTO();
			taCompteBanque = new TaCompteBanque();
//			taCompteBanque.setIdCompteBanque(0);
//			selectedTaCompteBanqueDTO.setId(0);


			mapperUIToModel.map(selectedTaCompteBanqueDTO, taCompteBanque);
			
//			values.add(selectedTaCompteBanqueDTO);
			autoCompleteMapDTOtoUI();
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actInserer"));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {
		
		try {
			//taCompteBanque = taCompteBanqueService.findById(selectedTaCompteBanqueDTO.getId());
			for (TaCompteBanque adr : masterEntity.getTaCompteBanques()) {
				if(adr.getIdCompteBanque()==selectedTaCompteBanqueDTO.getId()) {
					taCompteBanque = adr;
				}
			}
		
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actModifier"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actAide(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        Map<String,List<String>> params = null;
        PrimeFaces.current().dialog().openDynamic("aide", options, params);
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actAide"));
		}
	}
	
//	public void actAideRetour(ActionEvent actionEvent) {
//		
//	}
	   
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		b.setTitre("Tiers");
		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
		b.setTemplate("tiers/tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TIERS);
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Tiers", 
	    		"Nouveau"
	    		)); }
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
		TaCompteBanque taCompteBanque = new TaCompteBanque();
		try {
			if(selectedTaCompteBanqueDTO!=null && selectedTaCompteBanqueDTO.getId()!=null){
				taCompteBanque = taCompteBanqueService.findById(selectedTaCompteBanqueDTO.getId());
			}
			taCompteBanque.setTaTiers(null);
			taCompteBanqueService.remove(taCompteBanque);
			values.remove(selectedTaCompteBanqueDTO);
			
			if(!values.isEmpty()) {
				selectedTaCompteBanqueDTO = values.get(0);
			}else {
				selectedTaCompteBanqueDTO=new TaCompteBanqueDTO();
			}
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tiers", e.getCause().getCause().getCause().getCause().getMessage()));
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
		selectedTaCompteBanqueDTO=new TaCompteBanqueDTO();
		selectedTaCompteBanqueDTOs=new TaCompteBanqueDTO[]{selectedTaCompteBanqueDTO};
	
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("tiers", taTiersService.findById(selectedTaCompteBanqueDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaCompteBanqueDTO.getId()));
//		BirtUtil.setAppContextEdition(hm);
//		
//		BirtUtil.startReportEngine();
//		
////		BirtUtil.renderReportToFile(
////				"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.webapp/src/main/webapp/reports/tiers/FicheTiers.rptdesign", 
////				"/tmp/tiers.pdf", 
////				new HashMap<>(), 
////				BirtUtil.PDF_FORMAT);
//		
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTiers.rptdesign");
//		BirtUtil.renderReportToFile(
//				inputStream, 
//				"/tmp/tiers.pdf", 
//				new HashMap<>(), 
//				BirtUtil.PDF_FORMAT);
		////////////////////////////////////////////////////////////////////////////////////////
		//java.net.URL.setURLStreamHandlerFactory();
//		taTiersService.generePDF(selectedTaCompteBanqueDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaCompteBanqueDTO.getId()));
			System.out.println("TiersController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			onRowSelect(event);
			
			autoCompleteMapDTOtoUI();
			if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("banque", 
				"Chargement du compte bancaire N°"+selectedTaCompteBanqueDTO.getCodeBanque()
				)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
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
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaCompteBanqueDTO.getCodeBanque()
				)); 
		}
	} 
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actDialogTypeCompteBanque(ActionEvent actionEvent) {
		
  
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        

        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_banque", options, params);
 	    
	}
	
	
	public void handleReturnDialogTypeCompteBanque(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTBanque = (TaTBanque) event.getObject();
			try {
				taTBanqueDTO = taTBanqueService.findByCodeDTO(taTBanque.getCodeTBanque());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	
	public void handleReturnDialogCompteBanqueReorder(SelectEvent event) {
		refresh(null);
	}

	
	 public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
				case "annuler":
				case "enregistrer":
				case "fermer":
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
				if(selectedTaCompteBanqueDTO!=null && selectedTaCompteBanqueDTO.getId()!=null  && selectedTaCompteBanqueDTO.getId()!=0 ) retour = false;
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

	public void validateCompteBanque(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {
		 
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaCompteBanqueDTO>> violations = factory.getValidator().validateValue(TaCompteBanqueDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaCompteBanqueDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));

			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
					 validateUIField(nomChamp,value);
			}


		} catch(Exception e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	

	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaTBanqueDTO && ((TaTBanqueDTO) value).getCodeTBanque()!=null && ((TaTBanqueDTO) value).getCodeTBanque().equals(Const.C_AUCUN))value=null;
		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
			if(nomChamp.equals(Const.C_CODE_T_BANQUE)) {
				TaTBanque entity = null;
				if(value!=null){
					if(value instanceof TaTBanque){
						entity=(TaTBanque) value;
	//								entity = taTTiersService.findByCode(entity.getCodeTTiers());
					} else if(value instanceof TaTBanqueDTO){
							entity = taTBanqueService.findByCode(((TaTBanqueDTO) value).getCodeTBanque());
					}else{
						entity = taTBanqueService.findByCode((String)value);
					}
				} else {
					selectedTaCompteBanqueDTO.setCodeTBanque("");
					taTBanqueDTO.setCodeTBanque("");
					taTBanque=null;
				}						
				taCompteBanque.setTaTBanque(entity);
			} 
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	
	public List<TaTBanqueDTO> typeCompteBanqueAutoCompleteLight(String query) {
        List<TaTBanqueDTO> allValues = taTBanqueService.selectAllDTO();
        List<TaTBanqueDTO> filteredValues = new ArrayList<TaTBanqueDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTBanqueDTO civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTBanque().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public TaCompteBanqueDTO[] getSelectedTaCompteBanqueDTOs() {
		return selectedTaCompteBanqueDTOs;
	}

	public void setSelectedTaCompteBanqueDTOs(TaCompteBanqueDTO[] selectedTaCompteBanqueDTOs) {
		this.selectedTaCompteBanqueDTOs = selectedTaCompteBanqueDTOs;
	}

	public TaCompteBanqueDTO getNewTaCompteBanqueDTO() {
		return newTaCompteBanqueDTO;
	}

	public void setNewTaCompteBanqueDTO(TaCompteBanqueDTO newTaCompteBanqueDTO) {
		this.newTaCompteBanqueDTO = newTaCompteBanqueDTO;
	}

	public TaCompteBanqueDTO getSelectedTaCompteBanqueDTO() {
		return selectedTaCompteBanqueDTO;
	}

	public void setSelectedTaCompteBanqueDTO(TaCompteBanqueDTO selectedTaCompteBanqueDTO) {
		this.selectedTaCompteBanqueDTO = selectedTaCompteBanqueDTO;
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

	public TaTBanque getTaTBanque() {
		return taTBanque;
	}

	public void setTaTBanque(TaTBanque taTTiers) {
		this.taTBanque = taTTiers;
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
	
	public TaTBanqueDTO getTaTBanqueDTO() {
		return taTBanqueDTO;
	}

	public void setTaTBanqueDTO(TaTBanqueDTO taTBanqueDTO) {
		this.taTBanqueDTO = taTBanqueDTO;
	}

	public TaCompteBanque getTaTiers() {
		return taCompteBanque;
	}

	public void setTaTiers(TaCompteBanque taCompteBanque) {
		this.taCompteBanque = taCompteBanque;
	}

	public TabView getTabViewTiers() {
		return tabViewTiers;
	}

	public void setTabViewTiers(TabView tabViewTiers) {
		this.tabViewTiers = tabViewTiers;
	}

	public TaTiers getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaTiers masterEntity) {
		this.masterEntity = masterEntity;
	}


	public List<TaCompteBanqueDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaCompteBanqueDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}


}  
