package fr.legrain.bdg.webapp.app;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;


import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeChargeServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCouponServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceItemServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeProductServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.ILgrStripe;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;



@Named
@ViewScoped  
public class GestionAbonnementController implements Serializable {  
	
	private @EJB ILgrStripe lgrStripe;
	
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaStripeProductServiceRemote taStripeProductService;
	private @EJB ITaStripePlanServiceRemote taStripePlanService;
	private @EJB ITaStripeCouponServiceRemote taStripeCouponService;
	
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	//private @EJB ITaStripeSubscriptionItemServiceRemote taStripeSubscriptionItemService;
	//private @EJB ITaStripeSubscriptionServiceRemote taStripeSubscriptionService;
	private @EJB ITaStripeChargeServiceRemote taStripeChargeService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeInvoiceServiceRemote taStripeInvoiceService;
	private @EJB ITaStripeInvoiceItemServiceRemote taStripeInvoiceItemService;
	
	private @EJB ITaAbonnementServiceRemote taAbonnementService;
	
	private @EJB ITaLAbonnementServiceRemote taLAbonnementService;
	
	private TaAbonnementDTO selectedDocumentDTO = new TaAbonnementDTO();
	private List<TaAbonnementDTO> listeSubscription = new ArrayList<>();
	
	private TaLAbonnementDTO selectedTaStripeSubscriptionItemDTO = new TaLAbonnementDTO();
	private List<TaLAbonnementDTO> listeSubscriptionItem = new ArrayList<>();
	
	private TaStripeInvoiceDTO selectedTaStripeInvoiceDTO = new TaStripeInvoiceDTO();
	private List<TaStripeInvoiceDTO> listeInvoice = new ArrayList<>();
	
	private TaStripeInvoiceDTO selectedTaStripeInvoiceCustomerDTO = new TaStripeInvoiceDTO();
	private List<TaStripeInvoiceDTO> listeInvoiceCustomer = new ArrayList<>();
	
	private TaStripeChargeDTO selectedTaStripeChargeCustomerDTO = new TaStripeChargeDTO();
	private List<TaStripeChargeDTO> listeChargeCustomer = new ArrayList<>();

	private List<TaLotDTO> values; 
	private List<TaLotDTO> filteredValues; 
	private TaLotDTO nouveau ;
	private TaLot taLot = new TaLot();
	private String numLot;
	
	private TaUnite taUnite;
	private TaUnite taUnite2;
	private TaLotDTO selection ;
	
	private String paramRefresh;
//	private Boolean refreshExecute;
	
	private TaStripePaiementPrevu taStripePaiementPrevu;


	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaLEcheanceServiceRemote taLEcheanceService;
	private @EJB ITaStripePaiementPrevuServiceRemote taStripePaiementPrevuService;
	
	private @EJB ITaLAvisEcheanceServiceRemote taLAvisEcheanceService;

	private Integer nbEcheances = 0;
	private Integer nbTiersEcheances = 0;
	private BigDecimal montantHtEcheances = new BigDecimal(0);
	private List<TaLEcheance> listeLEcheance = new ArrayList<>();
	private List<TaAbonnementDTO> listeSubscriptionNonStripe = new ArrayList<>();
	private List<TaAbonnementDTO>  listeSubscriptionASuspendre = new ArrayList<>();
	private List<TaStripePaiementPrevuDTO> listePaiementPrevu = new ArrayList<>();
	private TaStripePaiementPrevuDTO selectedTaStripePaiementPrevuDTO = new TaStripePaiementPrevuDTO();
	
	private List<TaLAbonnementDTO> listeLAbonnement = new ArrayList<>();
	
	private LgrDozerMapper<TaLotDTO,TaLot> mapperUIToModel  = new LgrDozerMapper<TaLotDTO,TaLot>();
	private LgrDozerMapper<TaLot,TaLotDTO> mapperModelToUI  = new LgrDozerMapper<TaLot,TaLotDTO>();
	
	private String typeDoc = TaAbonnement.TYPE_DOC;
	
	private BigDecimal totalTTCAbonnement = BigDecimal.ZERO;

	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
			if(values != null && !values.isEmpty()){
				selection = values.get(0);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setFilteredValues(values);
	}
	
	public GestionAbonnementController() {  
	}
	public void calculTotauxAbonnement() {
		totalTTCAbonnement = taLAbonnementService.sumTtcLigneAboEnCoursEtSuspendu();
	}
	public void refresh(){
		calculTotauxAbonnement();
		listeLEcheance = taLEcheanceService.findAllEcheanceNonTotTransforme();
		//nbEcheances = listeLEcheance.size();
		nbTiersEcheances =  (int) taLEcheanceService.countTiersEcheanceNonLieAAvisEcheance();
		montantHtEcheances =  taLEcheanceService.montantHtApresRemiseEcheanceNonLieAAvisEcheanceTiers(null);
		nbEcheances = (int) taLEcheanceService.countEcheanceNonLieAAvisEcheanceTiers(null);
		
//		listeSubscriptionNonStripe = taStripeSubscriptionService.rechercherSubscriptionNonStripeCustomerDTO(null /*taStripeCustomer.getIdExterne()*/); //recherche en local
//		
//		listeSubscriptionASuspendre = taStripeSubscriptionService.selectAllASuspendre();
		
		listeLAbonnement = taLAbonnementService.selectAllDTOAvecEtat();
		
		listeSubscriptionNonStripe = taAbonnementService.rechercherAbonnementNonStripeCustomerDTO(null /*taStripeCustomer.getIdExterne()*/); //recherche en local
		
		//listeSubscriptionASuspendre = taAbonnementService.selectAllASuspendre();
		
		
		
		listeChargeCustomer = taStripeChargeService.rechercherChargeCustomerDTO(null /*taStripeCustomer.getIdExterne()*/); //recherche dans la bdd locale
		
		listePaiementPrevu = taStripePaiementPrevuService.rechercherPaiementPrevuCustomerDTO(null /*taStripeCustomer.getIdExterne()*/);
	}
	
	public boolean isTotTransforme(String codeEtat) {
		boolean disabled = false;
		if(codeEtat != null && codeEtat.equals("doc_tot_Transforme")) {
			disabled = true;
		}
		return disabled;
	}
	
	public void actSupprimerEcheance(ActionEvent actionEvent) {
		Integer idLEcheance = (Integer) actionEvent.getComponent().getAttributes().get("idLEcheance");
		if(idLEcheance!=null) {
			TaLEcheance ech;
			try {
				ech = taLEcheanceService.findById(idLEcheance);
				//ici je vais aller chercher d'éventuel avis liés à l'échéance (ancienne version des abonnements fonctionner comme ça) et les déliés sinon je ne peu pas supprimé
				// on pourra virer ce code dès que la nouvelle version est bien en place et que plus aucun ancien avis n'est lié a des échéances
				List<TaLAvisEcheance> listeLigneAvis = taLAvisEcheanceService.findAllByIdEcheance(ech.getIdLEcheance());
				
				for (TaLAvisEcheance ligneAvis : listeLigneAvis) {
					ligneAvis.setTaLEcheance(null);
					taLAvisEcheanceService.merge(ligneAvis);
				}
				taLEcheanceService.remove(ech);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			refresh();
			
		}
	}
	
	public void actDeclencherPaiementPrevuMaintenant(ActionEvent actionEvent) {
	try {
		Integer idPaiementPrevu = (Integer) actionEvent.getComponent().getAttributes().get("idPaiementPrevu");
		if(idPaiementPrevu!=null) {
			 taStripePaiementPrevu = taStripePaiementPrevuService.findById(idPaiementPrevu);
			if(taStripePaiementPrevu.getTaStripeCharge()==null) {
				taStripePaiementPrevuService.declencherPaiementStripe(taStripePaiementPrevu);
			} //else ce paiement a deja ete declenche
//			
//			//TODO mettre à jour les abonnements utilisant cette source ? voir comment fonctionne Stripe dans ce cas
//			
//			selectedTaStripeCustomerDTO = taStripeCustomerService.findByIdDTO(taStripeCustomer.getIdStripeCustomer());
//			listeSource = taStripeSourceService.rechercherSourceCustomerDTO(taStripeCustomer.getIdExterne());
			
			refresh();
			
		}
	
	} catch(Exception e) {
		e.printStackTrace();
	}
}
	

  
	
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaLotDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaLotDTO retour = null;
			taLot=new TaLot();
		
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taLot);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "GestionLot", e.getMessage()));
		}
	}

	public void actInserer(ActionEvent actionEvent) {
//		nouveau = new TaLot();
//		selection = nouveau;
//		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//		try {
//			Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//			if(params.get("idEntity")!=null) {
//									
//					
//						nouveau = taLotService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
//						taLot = taLotService.findById(LibConversion.stringToInteger(params.get("idEntity")));
//				}
//			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}		
	}

	public void actSupprimer(){

	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			try {
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
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
			case "rowEditor":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "supprimer":
			case "inserer":
				retour = true; //On ne peut pas créer ou supprimer des lots sur cet écran, on peut uniquement les modifier pour les marqués terminés
				break;
			case "modifier":
			case "imprimer":
			case "fermer":
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

	public List<TaLotDTO> getValues(){  
		return values;
	}

	public TaLotDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaLotDTO newTaTTiers) {
		this.nouveau = newTaTTiers;
	}

	public TaLotDTO getSelection() {
		return selection;
	}

	public void setSelection(TaLotDTO selectedTaTTiers) {
		this.selection = selectedTaTTiers;
	}

	public void setValues(List<TaLotDTO> values) {
		this.values = values;
	}

	public List<TaLotDTO> getFilteredValues() {
		return filteredValues;
	}
	
	public void setFilteredValues(List<TaLotDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}

	public List<TaUnite> UniteAutoComplete(String query) {
		List<TaUnite> allValues =new LinkedList<TaUnite>();
		if(taLot.getTaArticle().getTaUnite1()!=null)
			allValues.add(taLot.getTaArticle().getTaUnite1());
//		if(taLot.getTaArticle().getTaPrixes()!=null){
//			for (TaPrix prix : taLot.getTaArticle().getTaPrixes()) {
//				if(prix.getTaUnite1()!=null && !allValues.contains(prix.getTaUnite1()))
//					allValues.add(prix.getTaUnite1());
//			}
//		}
        List<TaUnite> filteredValues = new ArrayList<TaUnite>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaUnite obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaUnite> Unite2AutoComplete(String query) {
		List<TaUnite> allValues =new LinkedList<TaUnite>();

		
        List<TaUnite> filteredValues = new ArrayList<TaUnite>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaUnite obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeUnite().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }

	public List<TaArticle> ArticleAutoComplete(String query) {
        List<TaArticle> allValues = taArticleService.selectAll();
        List<TaArticle> filteredValues = new ArrayList<TaArticle>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaArticle obj = allValues.get(i);
            if(query.equals("*") || obj.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(obj);
            }
        }
         
        return filteredValues;
    }
	
	public void actDialogModifier(ActionEvent actionEvent){
		
		try {
		nouveau = selection;
		
		Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_EDITION));
        params.put("modeEcranDefaut", list);
        List<String> list2 = new ArrayList<String>();
        list2.add(LibConversion.integerToString(selection.getId()));
        params.put("idEntity", list2);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_lot", options, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void handleReturnDialogLot(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//nouveau=(TaLot) event.getObject();
			
		}
		refresh();
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		//validateUIField(nomChamp,value);
	}


	public String getNumLot() {
		return numLot;
	}

	public void setNumLot(String numLot) {
		this.numLot = numLot;
	}	

	public void onChangeQ ()
	{
	    
	}

	public TaUnite getTaUnite() {
		return taUnite;
	}

	public void setTaUnite(TaUnite taUnite) {
		this.taUnite = taUnite;
	}

	public TaUnite getTaUnite2() {
		return taUnite2;
	}

	public void setTaUnite2(TaUnite taUnite2) {
		this.taUnite2 = taUnite2;
	}
	
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaLotDTO>> violations = factory.getValidator().validateValue(TaLotDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLotDTO> cv : violations) {
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
	
	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				
			}

			return false;

		} catch (Exception e) {

		}
		return false;
	}			 


	public void onRowEdit(RowEditEvent event) {

			try {
				selection.getTermine();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

}
	
	
	public void actDialogTypes(ActionEvent actionEvent) {
	    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("articles/dialog_lot", options, params);
        
	}
	
	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taLot = (TaLot) event.getObject();
			
		}
		refresh();
	}	

	public String getParamRefresh() {
		return paramRefresh;
	}

	public void setParamRefresh(String paramRefresh) {
		this.paramRefresh = paramRefresh;
	}

	public TaAbonnementDTO getSelectedDocumentDTO() {
		return selectedDocumentDTO;
	}

	public void setSelectedDocumentDTO(TaAbonnementDTO selectedDocumentDTO) {
		this.selectedDocumentDTO = selectedDocumentDTO;
	}

	public List<TaAbonnementDTO> getListeSubscription() {
		return listeSubscription;
	}

	public void setListeSubscription(List<TaAbonnementDTO> listeSubscription) {
		this.listeSubscription = listeSubscription;
	}

	public TaLAbonnementDTO getSelectedTaStripeSubscriptionItemDTO() {
		return selectedTaStripeSubscriptionItemDTO;
	}

	public void setSelectedTaStripeSubscriptionItemDTO(TaLAbonnementDTO selectedTaStripeSubscriptionItemDTO) {
		this.selectedTaStripeSubscriptionItemDTO = selectedTaStripeSubscriptionItemDTO;
	}

	public List<TaLAbonnementDTO> getListeSubscriptionItem() {
		return listeSubscriptionItem;
	}

	public void setListeSubscriptionItem(List<TaLAbonnementDTO> listeSubscriptionItem) {
		this.listeSubscriptionItem = listeSubscriptionItem;
	}

	public TaStripeInvoiceDTO getSelectedTaStripeInvoiceDTO() {
		return selectedTaStripeInvoiceDTO;
	}

	public void setSelectedTaStripeInvoiceDTO(TaStripeInvoiceDTO selectedTaStripeInvoiceDTO) {
		this.selectedTaStripeInvoiceDTO = selectedTaStripeInvoiceDTO;
	}

	public List<TaStripeInvoiceDTO> getListeInvoice() {
		return listeInvoice;
	}

	public void setListeInvoice(List<TaStripeInvoiceDTO> listeInvoice) {
		this.listeInvoice = listeInvoice;
	}

	public TaStripeInvoiceDTO getSelectedTaStripeInvoiceCustomerDTO() {
		return selectedTaStripeInvoiceCustomerDTO;
	}

	public void setSelectedTaStripeInvoiceCustomerDTO(TaStripeInvoiceDTO selectedTaStripeInvoiceCustomerDTO) {
		this.selectedTaStripeInvoiceCustomerDTO = selectedTaStripeInvoiceCustomerDTO;
	}

	public List<TaStripeInvoiceDTO> getListeInvoiceCustomer() {
		return listeInvoiceCustomer;
	}

	public void setListeInvoiceCustomer(List<TaStripeInvoiceDTO> listeInvoiceCustomer) {
		this.listeInvoiceCustomer = listeInvoiceCustomer;
	}

	public TaStripeChargeDTO getSelectedTaStripeChargeCustomerDTO() {
		return selectedTaStripeChargeCustomerDTO;
	}

	public void setSelectedTaStripeChargeCustomerDTO(TaStripeChargeDTO selectedTaStripeChargeCustomerDTO) {
		this.selectedTaStripeChargeCustomerDTO = selectedTaStripeChargeCustomerDTO;
	}

	public List<TaStripeChargeDTO> getListeChargeCustomer() {
		return listeChargeCustomer;
	}

	public void setListeChargeCustomer(List<TaStripeChargeDTO> listeChargeCustomer) {
		this.listeChargeCustomer = listeChargeCustomer;
	}

	public static String getcDialog() {
		return C_DIALOG;
	}

	public List<TaLEcheance> getListeLEcheance() {
		return listeLEcheance;
	}

	public void setListeLEcheance(List<TaLEcheance> listeLEcheance) {
		this.listeLEcheance = listeLEcheance;
	}

	public List<TaAbonnementDTO> getListeSubscriptionNonStripe() {
		return listeSubscriptionNonStripe;
	}

	public void setListeSubscriptionNonStripe(List<TaAbonnementDTO> listeSubscriptionNonStripe) {
		this.listeSubscriptionNonStripe = listeSubscriptionNonStripe;
	}

	public List<TaStripePaiementPrevuDTO> getListePaiementPrevu() {
		return listePaiementPrevu;
	}

	public void setListePaiementPrevu(List<TaStripePaiementPrevuDTO> listePaiementPrevu) {
		this.listePaiementPrevu = listePaiementPrevu;
	}

	public TaStripePaiementPrevuDTO getSelectedTaStripePaiementPrevuDTO() {
		return selectedTaStripePaiementPrevuDTO;
	}

	public void setSelectedTaStripePaiementPrevuDTO(TaStripePaiementPrevuDTO selectedTaStripePaiementPrevuDTO) {
		this.selectedTaStripePaiementPrevuDTO = selectedTaStripePaiementPrevuDTO;
	}

	public List<TaAbonnementDTO> getListeSubscriptionASuspendre() {
		return listeSubscriptionASuspendre;
	}

	public void setListeSubscriptionASuspendre(List<TaAbonnementDTO> listeSubscriptionASuspendre) {
		this.listeSubscriptionASuspendre = listeSubscriptionASuspendre;
	}

	public Integer getNbEcheances() {
		return nbEcheances;
	}

	public void setNbEcheances(Integer nbEcheances) {
		this.nbEcheances = nbEcheances;
	}

	public Integer getNbTiersEcheances() {
		return nbTiersEcheances;
	}

	public void setNbTiersEcheances(Integer nbTiersEcheances) {
		this.nbTiersEcheances = nbTiersEcheances;
	}

	public BigDecimal getMontantHtEcheances() {
		return montantHtEcheances;
	}

	public void setMontantHtEcheances(BigDecimal montantHtEcheances) {
		this.montantHtEcheances = montantHtEcheances;
	}

	public TaStripePaiementPrevu getTaStripePaiementPrevu() {
		return taStripePaiementPrevu;
	}

	public void setTaStripePaiementPrevu(TaStripePaiementPrevu taStripePaiementPrevu) {
		this.taStripePaiementPrevu = taStripePaiementPrevu;
	}

	public String getTypeDoc() {
		return typeDoc;
	}

	public void setTypeDoc(String typeDoc) {
		this.typeDoc = typeDoc;
	}

	public List<TaLAbonnementDTO> getListeLAbonnement() {
		return listeLAbonnement;
	}

	public void setListeLAbonnement(List<TaLAbonnementDTO> listeLAbonnement) {
		this.listeLAbonnement = listeLAbonnement;
	}

	public BigDecimal getTotalTTCAbonnement() {
		return totalTTCAbonnement;
	}

	public void setTotalTTCAbonnement(BigDecimal totalTTCAbonnement) {
		this.totalTTCAbonnement = totalTTCAbonnement;
	}

}  
