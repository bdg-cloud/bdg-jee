package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;

import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.ILgrStripe;
import fr.legrain.bdg.tiers.service.remote.ITaCarteBancaireServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.paiement.CreerSourceStripeController;
import fr.legrain.bdg.webapp.paiement.CreerSourceStripeParam;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.dto.TaTBanqueDTO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaTBanque;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class CarteBancaireController extends AbstractController implements Serializable { 
	


	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	private @EJB ILgrStripe lgrStripe;
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	
	private String paramId;
	
	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaCarteBancaireDTO> values; 
	private List<TaCarteBancaireDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaCarteBancaireServiceRemote taCarteBancaireService;
	private @EJB ITaTBanqueServiceRemote taTBanqueService;

	
	private TaCarteBancaireDTO[] selectedTaCarteBancaireDTOs; 
	private TaCarteBancaire taCarteBancaire = new TaCarteBancaire();
	private TaCarteBancaireDTO newTaCarteBancaireDTO = new TaCarteBancaireDTO();
	private TaCarteBancaireDTO selectedTaCarteBancaireDTO = new TaCarteBancaireDTO();
//	private TaCarteBancaireDTO oldSelectedTaCarteBancaireDTO = new TaCarteBancaireDTO();
	
	private TaTBanque taTBanque;
	
	private LgrDozerMapper<TaCarteBancaireDTO,TaCarteBancaire> mapperUIToModel  = new LgrDozerMapper<TaCarteBancaireDTO,TaCarteBancaire>();
	private LgrDozerMapper<TaCarteBancaire,TaCarteBancaireDTO> mapperModelToUI  = new LgrDozerMapper<TaCarteBancaire,TaCarteBancaireDTO>();
	
	private TaTiers masterEntity;




	private TaTBanqueDTO taTBanqueDTO;
	
	public CarteBancaireController() {  
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
//		selectedTaCarteBancaireDTO=null;
		 
   	    if(masterEntity!=null) {
   	    	try {
				if(masterEntity.getIdTiers()!=0) masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			} catch (FinderException e) {
				
			}
			values = new ArrayList<>();
			for (TaCarteBancaire taCarteBancaire : masterEntity.getTaCarteBancaires()) {
				TaCarteBancaireDTO dto=new TaCarteBancaireDTO();
				mapperModelToUI.map(taCarteBancaire, dto);
				values.add(dto);
			}

			if(!values.isEmpty()&&selectedTaCarteBancaireDTO==null)
				selectedTaCarteBancaireDTO=values.get(0);
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaCarteBancaireDTO> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				taCarteBancaire = new TaCarteBancaire();
				mapperModelToUI.map(taCarteBancaire,selectedTaCarteBancaireDTO );
				selectedTaCarteBancaireDTO=new TaCarteBancaireDTO();
				
				if(!values.isEmpty()) selectedTaCarteBancaireDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaCarteBancaireDTO.getId()!=null && selectedTaCarteBancaireDTO.getId()!=0){
					taCarteBancaire = taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId());
					selectedTaCarteBancaireDTO=taCarteBancaireService.findByIdDTO(selectedTaCarteBancaireDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taCarteBancaire, selectedTaCarteBancaireDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public boolean carteExisteDansStripe() {
		TaStripeSource s = null;
		try {
			s = taStripeSourceService.rechercherSource(taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId()));
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s!=null;
	}
	
	public void actCreerCarteDansStripe(ActionEvent actionEvent) {
		try {
			TaStripeSource s = null;//taStripeSourceService.rechercherSource(taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId()));
			TaStripeCustomer taStripeCustomer =  taStripeCustomerService.rechercherCustomer(masterEntity);
			
			CreerSourceStripeParam param = new CreerSourceStripeParam();
//			param.setTaCarteBancaire(taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId()));
			param.setTaCarteBancaire(null); //nouvelle carte
			param.setTaStripeCustomer(taStripeCustomer);
			param.setTaStripeSource(s);
			param.setTaTiers(masterEntity);
			CreerSourceStripeController.actDialogueCreerSourceStripe(param);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		/*********************************************************************************************/
		
//		try {
//			TaStripeSource s = taStripeSourceService.rechercherSource(taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId()));
//			if(s == null) {//si pas de source pour ce moyen de paiement
//				//créer une source 
//				s = new TaStripeSource();
//				String idSourceStripe = lgrStripe.creerSourcePrelevementSEPA(selectedTaCompteBanqueDTO.getIban(),masterEntity.getNomTiers());
//				s.setIdExterne(idSourceStripe);
//				s.setTaCarteBancaire(taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId()));
//				
//				
//				s = taStripeSourceService.merge(s);
//			}
//			
//			TaStripeCustomer taStripeCustomer =  taStripeCustomerService.rechercherCustomer(masterEntity);
//			if(taStripeCustomer!=null) {
//				//l'ajouter à ce customer
//				lgrStripe.attacherSourceStripe(s.getIdExterne(), taStripeCustomer.getIdExterne());
//			} else {
//				//créer un customer avec cette source par defaut
//				String idCustomerStripe = lgrStripe.creerCustomerStripe(masterEntity.getTaEmail().getAdresseEmail(), s.getIdExterne(), masterEntity.getNomTiers()+" "+masterEntity.getCodeTiers());
//				taStripeCustomer = new TaStripeCustomer();
//				taStripeCustomer.setIdExterne(idCustomerStripe);
//				taStripeCustomer.setEmail(masterEntity.getTaEmail().getAdresseEmail());
//				taStripeCustomer.setTaTiers(masterEntity);
//				
//				taStripeCustomer = taStripeCustomerService.merge(taStripeCustomer);
//				
//				s.setTaStripeCustomer(taStripeCustomer);
//				s = taStripeSourceService.merge(s);
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTBanqueDTO!=null) {
			validateUIField(Const.C_CODE_T_BANQUE,taTBanqueDTO.getCodeTBanque());
			selectedTaCarteBancaireDTO.setCodeTBanque(taTBanqueDTO.getCodeTBanque());
		} else {
			selectedTaCarteBancaireDTO.setCodeTBanque(null);
		}
		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTBanqueDTO = null;
			if(selectedTaCarteBancaireDTO!=null){
				if(selectedTaCarteBancaireDTO.getCodeTBanque()!=null && !selectedTaCarteBancaireDTO.getCodeTBanque().equals("")) {
					taTBanqueDTO=taTBanqueService.findByCodeDTO(selectedTaCarteBancaireDTO.getCodeTBanque());
				}	
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaCarteBancaire rechercheCompteBanqueDansTiers(TaCarteBancaireDTO dto){
		for (TaCarteBancaire adr : masterEntity.getTaCarteBancaires()) {
			if(adr.getIdCarteBancaire()==dto.getId()) {
				return adr;
			}
		}
		return null;
	}
	

	

	

	

	
	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaCarteBancaire taCarteBancaire = new TaCarteBancaire();

		autoCompleteMapUIToDTO();
//		initCompteBanqueNull();
				
		mapperUIToModel.map(selectedTaCarteBancaireDTO, taCarteBancaire);
		

		
		try {
			taCarteBancaire.setTaTiers(masterEntity);
			masterEntity.addCarteBancaire(taCarteBancaire);	
			

			
			
			taCarteBancaire = taCarteBancaireService.merge(taCarteBancaire,ITaTiersServiceRemote.validationContext);
			
			mapperModelToUI.map(taCarteBancaire, selectedTaCarteBancaireDTO);
			autoCompleteMapDTOtoUI();
			

			masterEntity = taTiersService.findById(masterEntity.getIdTiers());

			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaCarteBancaireDTO);
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
			selectedTaCarteBancaireDTO = new TaCarteBancaireDTO();
			taCarteBancaire = new TaCarteBancaire();
//			taCarteBancaire.setIdCompteBanque(0);
//			selectedTaCarteBancaireDTO.setId(0);


			mapperUIToModel.map(selectedTaCarteBancaireDTO, taCarteBancaire);
			
//			values.add(selectedTaCarteBancaireDTO);
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
			//taCarteBancaire = taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId());
			for (TaCarteBancaire adr : masterEntity.getTaCarteBancaires()) {
				if(adr.getIdCarteBancaire()==selectedTaCarteBancaireDTO.getId()) {
					taCarteBancaire = adr;
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
		TaCarteBancaire taCarteBancaire = new TaCarteBancaire();
		try {
			if(selectedTaCarteBancaireDTO!=null && selectedTaCarteBancaireDTO.getId()!=null){
				taCarteBancaire = taCarteBancaireService.findById(selectedTaCarteBancaireDTO.getId());
			}
			taCarteBancaire.setTaTiers(null);
			taCarteBancaireService.remove(taCarteBancaire);
			values.remove(selectedTaCarteBancaireDTO);
			
			if(!values.isEmpty()) {
				selectedTaCarteBancaireDTO = values.get(0);
			}else {
				selectedTaCarteBancaireDTO=new TaCarteBancaireDTO();
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
		selectedTaCarteBancaireDTO=new TaCarteBancaireDTO();
		selectedTaCarteBancaireDTOs=new TaCarteBancaireDTO[]{selectedTaCarteBancaireDTO};
	
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
		sessionMap.put("tiers", taTiersService.findById(selectedTaCarteBancaireDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaCarteBancaireDTO.getId()));
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
//		taTiersService.generePDF(selectedTaCarteBancaireDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaCarteBancaireDTO.getId()));
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
				"Chargement de la carte bancaire N°"+selectedTaCarteBancaireDTO.getLast4()
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
				"Chargement de la carte bancaire"+selectedTaCarteBancaireDTO.getLast4()
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
	

	
	public void handleReturnDialogCreerSource(SelectEvent event) {
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
				if(selectedTaCarteBancaireDTO!=null && selectedTaCarteBancaireDTO.getId()!=null  && selectedTaCarteBancaireDTO.getId()!=0 ) retour = false;
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
		  Set<ConstraintViolation<TaCarteBancaireDTO>> violations = factory.getValidator().validateValue(TaCarteBancaireDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaCarteBancaireDTO> cv : violations) {
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
					selectedTaCarteBancaireDTO.setCodeTBanque("");
					taTBanqueDTO.setCodeTBanque("");
					taTBanque=null;
				}						
				taCarteBancaire.setTaTBanque(entity);
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
	
	public TaCarteBancaireDTO[] getSelectedTaCarteBancaireDTOs() {
		return selectedTaCarteBancaireDTOs;
	}

	public void setSelectedTaCarteBancaireDTOs(TaCarteBancaireDTO[] selectedTaCarteBancaireDTOs) {
		this.selectedTaCarteBancaireDTOs = selectedTaCarteBancaireDTOs;
	}

	public TaCarteBancaireDTO getNewTaCarteBancaireDTO() {
		return newTaCarteBancaireDTO;
	}

	public void setNewTaCarteBancaireDTO(TaCarteBancaireDTO newTaCarteBancaireDTO) {
		this.newTaCarteBancaireDTO = newTaCarteBancaireDTO;
	}

	public TaCarteBancaireDTO getSelectedTaCarteBancaireDTO() {
		return selectedTaCarteBancaireDTO;
	}

	public void setSelectedTaCarteBancaireDTO(TaCarteBancaireDTO selectedTaCarteBancaireDTO) {
		this.selectedTaCarteBancaireDTO = selectedTaCarteBancaireDTO;
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

	public TaCarteBancaire getTaTiers() {
		return taCarteBancaire;
	}

	public void setTaTiers(TaCarteBancaire taCarteBancaire) {
		this.taCarteBancaire = taCarteBancaire;
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


	public List<TaCarteBancaireDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaCarteBancaireDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}


}  
