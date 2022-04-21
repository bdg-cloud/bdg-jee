package fr.legrain.bdg.webapp.synchronisation;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;


import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneShippingboServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaShippingBoServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.documents.OuvertureArticleBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.documents.TaLigneServiceWebExterneDTOJSF;
import fr.legrain.bdg.webapp.documents.TaLigneServiceWebExterneDTOJSF;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.generation.service.CreationDocumentFlashMultiple;
import fr.legrain.generation.service.CreationDocumentLigneServiceWebExterneMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.servicewebexterne.dto.TaLiaisonServiceExterneDTO;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.mapper.TaLiaisonServiceExterneMapper;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class GestionLiaisonExterneController extends AbstractController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3850888132933080252L;
	

	@EJB private ITaLiaisonServiceExterneServiceRemote liaisonService;
	@EJB private ITaServiceWebExterneServiceRemote serviceWebExterneService;
	protected @EJB ITaTiersServiceRemote taTiersService;
	protected @EJB ITaArticleServiceRemote taArticleService;
	protected @EJB ITaFactureServiceRemote taFactureService;
	protected @EJB ITaTPaiementServiceRemote taTPaiementService;
	@EJB private CreationDocumentLigneServiceWebExterneMultiple creation;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

    @Inject @Named(value="ouvertureArticleBean")
    private OuvertureArticleBean ouvertureArticleBean;

	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();
	
	
	protected ImageLgr selectedTypeCreation;
	
	protected TaTiersDTO taTiersDTO;
	protected String codeTiers="";
	
	private Map<String,String> listeStringEntite = new HashMap<String, String>();
	private List<TaServiceWebExterne> listeService = new ArrayList<TaServiceWebExterne>();
	private List<TaServiceWebExterneDTO> listeServiceDTO = new ArrayList<TaServiceWebExterneDTO>();
	
	private List<TaLiaisonServiceExterneDTO> listeLiaison = new ArrayList<TaLiaisonServiceExterneDTO>();
	
	private String selectedEntite = "";
	private TaServiceWebExterneDTO selectedServiceDTO;
	
	private TaLiaisonServiceExterneDTO selectedLiaison;
	
	private TaLiaisonServiceExterneDTO newLiaison = new TaLiaisonServiceExterneDTO();
	
	
	@PostConstruct
	public void postConstruct(){
		rechercheAvecCommentaire(true);

		refresh();
	}
	
	
	public void refresh()  {
		
		newLiaison = new TaLiaisonServiceExterneDTO();

		listeStringEntite.put(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS, TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
		listeStringEntite.put(TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE, TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE);
		listeStringEntite.put(TaLiaisonServiceExterne.TYPE_ENTITE_LIGNE_SERVICE_EXTERNE, TaLiaisonServiceExterne.TYPE_ENTITE_LIGNE_SERVICE_EXTERNE);
		listeStringEntite.put(TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT, TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT);
		
		listeServiceDTO = serviceWebExterneService.findAllLightActif();
		selectedLiaison = null;

		
	}
	
    public void actEnregistrer() {  	
    	if(selectedEntite != "" && selectedServiceDTO != null) {
    		TaLiaisonServiceExterneMapper mapper = new TaLiaisonServiceExterneMapper();
			TaLiaisonServiceExterne li = new TaLiaisonServiceExterne();
			newLiaison.setTypeEntite(selectedEntite);
			Integer id = null;
			try {
				TaServiceWebExterne service = serviceWebExterneService.findById(selectedServiceDTO.getId());
				
				if(selectedEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS)) {
	    			if(newLiaison.getTiersDTO() != null) {
						
	    				id= newLiaison.getTiersDTO().getId();
	    				
	    			}
				}else if(selectedEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE)) {
					if(newLiaison.getArticleDTO() != null) {
						
						id= newLiaison.getArticleDTO().getId();

	    			}
				
				}else if(selectedEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT)) {
					if(newLiaison.getTaTPaiementDTO() != null) {
						
						id= newLiaison.getTaTPaiementDTO().getId();

	    			}
				
				}
				
				if(id != null) {
					newLiaison.setIdEntite(id);
					li = mapper.mapDtoToEntity(newLiaison, li);
					li.setServiceWebExterne(service);
					
					//enregistrement
					if(li.getRefExterne() != null) {
						li.setRefExterne(li.getRefExterne().trim());
						try {
							liaisonService.merge(li);
						} catch (Exception e) {
							FacesContext context = FacesContext.getCurrentInstance();  
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant enregistrement de nouvelle Liaison", "Cette liaison existe peut-être déjà. Veuillez la supprimer afin d'en créer une nouvelle avec ces critères."));
							e.printStackTrace();
						}
	    				
	    				refresh();
	    				actRechercher();
					}else {
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant enregistrement de nouvelle Liaison", "Veuillez renseignez une référence externe.")); 
					}
				}else {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant enregistrement de nouvelle Liaison", "Veuillez choisir une entité BDG."));
				}
				
				
				
				
				
			} catch (FinderException e2) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant enregistrement de nouvelle Liaison", "Compte de service externe introuvable.")); 
				e2.printStackTrace();
			}
			
    		
    	}else {
    		FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur pendant enregistrement de nouvelle Liaison", "Veuillez choisir un type d'entité et un service externe.")); 
    	}
		
    }
    
    public void actRechercher() {  	
		if(selectedEntite != "" && selectedServiceDTO != null) {
			String typeEntite ="";
			if(selectedEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS)) {
				typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_TIERS;
			}else if(selectedEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE)) {
				typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE;
			
			}else if(selectedEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_LIGNE_SERVICE_EXTERNE)) {
				typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_LIGNE_SERVICE_EXTERNE;
			
			}else if(selectedEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT)) {
				typeEntite = TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT;
			
			}
			
			listeLiaison = liaisonService.findAllByTypeEntiteAndByIdServiceWebExterneDTO(typeEntite,selectedServiceDTO.getId());
			
		}
		
    }
    public void actSupprimerLigne() {
    	actSupprimerLigne(null);
    }
	public void actSupprimerLigne(ActionEvent actionEvent) {
		try {
			if(selectedLiaison != null) {
				liaisonService.removeDTO(selectedLiaison);
			}
			
			refresh();
			actRechercher();
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Liaisons services externe", "actSupprimerLigne")); 
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
    
  
    
    public void onRowSelectLigneShippingboAll(SelectEvent event){
    	System.out.println("onRowSelectLigneShippingboAll a etait cliquéééé");
    }
    public void onRowUnSelectLigneShippingboAll(UnselectEvent event){
    	System.out.println("onRowUnSelectLigneShippingboAll a etait cliquéééé");
    }
    public void onToggleSelectLigneShippingboAll(ToggleSelectEvent event){
    	System.out.println("onToggleSelectLigneShippingboAll a etait cliquéééé");
    }
    public void actionGroupee2() {
    	System.out.println("action groupée 2");
    }
    
	public void onRowEditInit(RowEditEvent event) {
		actModifier(null);
	}
	public void actModifier(ActionEvent actionEvent) {		
		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION))
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
}

	public void onRowEdit(RowEditEvent event) throws Exception {
		
		
		
	}
	
	public void enregistreLigne()  throws Exception {

	}

	public void onRowCancel(RowEditEvent event) {

		actAnnuler(null);
    }

	//code d'Isa a adapté 
	public void actAnnuler(ActionEvent e) {

	}

	
	public void onClearTiers(AjaxBehaviorEvent event){
		taTiersDTO=null;
		setCodeTiers("");
	}

	public List<TaTPaiementDTO> typePaiementAutoCompleteDTO(String query) {
		List<TaTPaiementDTO> allValues = taTPaiementService.selectAllDTO();
		List<TaTPaiementDTO> filteredValues = new ArrayList<TaTPaiementDTO>();
		TaTPaiementDTO cp = new TaTPaiementDTO();
		cp.setCodeTPaiement(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTPaiement().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}

	
	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeAndLibelleLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())
					|| (civ.getLibellelArticle()!=null && civ.getLibellelArticle().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getLibellecArticle()!=null && civ.getLibellecArticle().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public void onClearArticle(AjaxBehaviorEvent event){
	    validateUIField(Const.C_CODE_ARTICLE, null);

	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("autcompleteSelection() : "+nomChamp);

		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity = null;
			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = null;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCodeAvecLazy(((TaArticleDTO) value).getCodeArticle(),false);
					}else{
						entity = taArticleService.findByCodeAvecLazy((String)value,false);
					}
				}


			}			
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())
					|| (civ.getPrenomTiers() != null && civ.getPrenomTiers().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getNomEntreprise() != null && civ.getNomEntreprise().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public boolean etatBoutonLigne(String bouton) {
		boolean retour = true;
		if(modeEcran.dataSetEnModif()) {
			retour = false;
		}
		switch (modeEcranLigne.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			case "rowEditor":
				//retour = modeEcran.dataSetEnModif()?true:false;
				retour = true;
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
			case "rowEditor":
				//retour = modeEcran.dataSetEnModif()?true:false;
				retour = true;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = modeEcran.dataSetEnModif()?false:true;
				break;
			case "rowEditor":
				//retour = modeEcran.dataSetEnModif()?true:false;
				retour = true;
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
	

	
	public void actFermer(ActionEvent actionEvent) {

	}


	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}


	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}


	public String getCodeTiers() {
		return codeTiers;
	}


	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}


	public List<TaServiceWebExterne> getListeService() {
		return listeService;
	}


	public void setListeService(List<TaServiceWebExterne> listeService) {
		this.listeService = listeService;
	}


	public String getSelectedEntite() {
		return selectedEntite;
	}


	public void setSelectedEntite(String selectedEntite) {
		this.selectedEntite = selectedEntite;
	}



	public List<TaServiceWebExterneDTO> getListeServiceDTO() {
		return listeServiceDTO;
	}


	public void setListeServiceDTO(List<TaServiceWebExterneDTO> listeServiceDTO) {
		this.listeServiceDTO = listeServiceDTO;
	}


	public TaServiceWebExterneDTO getSelectedServiceDTO() {
		return selectedServiceDTO;
	}


	public void setSelectedServiceDTO(TaServiceWebExterneDTO selectedServiceDTO) {
		this.selectedServiceDTO = selectedServiceDTO;
	}


	public List<TaLiaisonServiceExterneDTO> getListeLiaison() {
		return listeLiaison;
	}


	public void setListeLiaison(List<TaLiaisonServiceExterneDTO> listeLiaison) {
		this.listeLiaison = listeLiaison;
	}


	public Map<String, String> getListeStringEntite() {
		return listeStringEntite;
	}


	public void setListeStringEntite(Map<String, String> listeStringEntite) {
		this.listeStringEntite = listeStringEntite;
	}


	public TaLiaisonServiceExterneDTO getSelectedLiaison() {
		return selectedLiaison;
	}


	public void setSelectedLiaison(TaLiaisonServiceExterneDTO selectedLiaison) {
		this.selectedLiaison = selectedLiaison;
	}


	public TaLiaisonServiceExterneDTO getNewLiaison() {
		return newLiaison;
	}


	public void setNewLiaison(TaLiaisonServiceExterneDTO newLiaison) {
		this.newLiaison = newLiaison;
	}



	
	

}
