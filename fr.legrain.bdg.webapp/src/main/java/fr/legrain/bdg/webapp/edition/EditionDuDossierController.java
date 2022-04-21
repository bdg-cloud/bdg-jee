package fr.legrain.bdg.webapp.edition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaEditionServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class EditionDuDossierController implements Serializable {  

	private static final long serialVersionUID = 2593062107811750940L;
	
	private List<TaEditionDTO> values; 
	private List<TaEditionDTO> filteredValues; 
	
	//une liste de chaque type d'edition
	private List<TaEditionDTO> devisValues; 
	private List<TaEditionDTO> factureValues; 
	private List<TaEditionDTO> avoirValues; 
	private List<TaEditionDTO> bonlivValues; 
	private List<TaEditionDTO> proformaValues;
	private List<TaEditionDTO> bonCdeValues;
	private List<TaEditionDTO> acompteValues;
	private List<TaEditionDTO> apporteurValues;
	private List<TaEditionDTO> prelevementValues;
	private List<TaEditionDTO> avisEcheanceValues; 
	
	private TaActionEdition actionImpression;
	private TaActionEdition actionMail;
	private TaActionEdition actionEspaceclie;
	
	private TaEditionDTO selectedEditionDefautDevis = null;
	private TaEditionDTO selectedEditionDefautDevisImpression = null;
	private TaEditionDTO selectedEditionDefautDevisMail = null;
	private TaEditionDTO selectedEditionDefautDevisDisposition = null;
	
	private TaEditionDTO selectedEditionDefautFacture = null;
	private TaEditionDTO selectedEditionDefautFactureImpression = null;
	private TaEditionDTO selectedEditionDefautFactureMail = null;
	private TaEditionDTO selectedEditionDefautFactureDisposition = null;
	
	private TaEditionDTO selectedEditionDefautAvoir = null;
	private TaEditionDTO selectedEditionDefautAvoirImpression = null;
	private TaEditionDTO selectedEditionDefautAvoirMail = null;
	private TaEditionDTO selectedEditionDefautAvoirDisposition = null;
	
	private TaEditionDTO selectedEditionDefautBonliv = null;
	private TaEditionDTO selectedEditionDefautBonlivImpression = null;
	private TaEditionDTO selectedEditionDefautBonlivMail = null;
	private TaEditionDTO selectedEditionDefautBonlivDisposition = null;
	
	private TaEditionDTO selectedEditionDefautProforma = null;
	private TaEditionDTO selectedEditionDefautProformaImpression = null;
	private TaEditionDTO selectedEditionDefautProformaMail = null;
	private TaEditionDTO selectedEditionDefautProformaDisposition = null;
	
	private TaEditionDTO selectedEditionDefautBoncde = null;
	private TaEditionDTO selectedEditionDefautBoncdeImpression = null;
	private TaEditionDTO selectedEditionDefautBoncdeMail = null;
	private TaEditionDTO selectedEditionDefautBoncdeDisposition = null;
	
	private TaEditionDTO selectedEditionDefautAcompte = null;
	private TaEditionDTO selectedEditionDefautAcompteImpression = null;
	private TaEditionDTO selectedEditionDefautAcompteMail = null;
	private TaEditionDTO selectedEditionDefautAcompteDisposition = null;
	
	private TaEditionDTO selectedEditionDefautApporteur = null;
	private TaEditionDTO selectedEditionDefautApporteurImpression = null;
	private TaEditionDTO selectedEditionDefautApporteurMail = null;
	private TaEditionDTO selectedEditionDefautApporteurDisposition = null;
	
	private TaEditionDTO selectedEditionDefautPrelevement = null;
	private TaEditionDTO selectedEditionDefautPrelevementImpression = null;
	private TaEditionDTO selectedEditionDefautPrelevementMail = null;
	private TaEditionDTO selectedEditionDefautPrelevementDisposition = null;
	
	private TaEditionDTO selectedEditionDefautAvisEcheance = null;
	private TaEditionDTO selectedEditionDefautAvisEcheanceImpression = null;
	private TaEditionDTO selectedEditionDefautAvisEcheanceMail = null;
	private TaEditionDTO selectedEditionDefautAvisEcheanceDisposition = null;
	
	private TaEditionDTO nouveau ;
	private TaEditionDTO selection ;

	private TaEdition taEdition = new TaEdition();

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaEditionServiceRemote taEditionService;
	private @EJB ITaActionEditionServiceRemote taActionEditionService;
	
	private String typeCanalImpression = TaActionEdition.code_impression;
	private String typeCanalMail = TaActionEdition.code_mail;
	private String typeCanalEspacecli = TaActionEdition.code_espacecli;
	
	private String typeEditionDevis = TaDevis.TYPE_DOC;
	private String typeEditionFacture = TaFacture.TYPE_DOC;
	private String typeEditionAvoir = TaAvoir.TYPE_DOC;
	private String typeEditionBonliv = TaBonliv.TYPE_DOC;
	private String typeEditionProforma = TaProforma.TYPE_DOC;
	private String typeEditionBoncde = TaBoncde.TYPE_DOC;
	private String typeEditionAcompte = TaAcompte.TYPE_DOC;
	private String typeEditionApporteur = TaApporteur.TYPE_DOC;
	private String typeEditionPrelevement = TaPrelevement.TYPE_DOC;
	private String typeEditionAvisEcheance = TaAvisEcheance.TYPE_DOC;
	
	private Map<String, String> listeTypeEdition = new HashMap<String, String>();
	private Map<String, String> listeTypeCanaux = new HashMap<String, String>();
	
	private LgrDozerMapper<TaEditionDTO,TaEdition> mapperUIToModel  = new LgrDozerMapper<TaEditionDTO,TaEdition>();
	private LgrDozerMapper<TaEdition,TaEditionDTO> mapperModelToUI  = new LgrDozerMapper<TaEdition,TaEditionDTO>();

	@PostConstruct
	public void postConstruct(){
		try {

			initListeTypeEdition();
			initListeTypeCanaux();
			
			actionImpression = taActionEditionService.findByCode(TaActionEdition.code_impression);
			actionMail = taActionEditionService.findByCode(TaActionEdition.code_mail);
			actionEspaceclie = taActionEditionService.findByCode(TaActionEdition.code_espacecli);
			
			refresh();
			if(values != null && !values.isEmpty()){
				selection = values.get(0);	
			}	
			
		
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}
	public void initListeTypeEdition() {
		listeTypeEdition.put("typeEditionDevis", typeEditionDevis);
		listeTypeEdition.put("typeEditionFacture", typeEditionFacture);
		listeTypeEdition.put("typeEditionAvoir", typeEditionAvoir);
		listeTypeEdition.put("typeEditionBonliv", typeEditionBonliv);
		listeTypeEdition.put("typeEditionProforma", typeEditionProforma);
		listeTypeEdition.put("typeEditionBoncde", typeEditionBoncde);
		listeTypeEdition.put("typeEditionAcompte", typeEditionAcompte);
		listeTypeEdition.put("typeEditionApporteur", typeEditionApporteur);
		listeTypeEdition.put("typeEditionPrelevement", typeEditionPrelevement);
		listeTypeEdition.put("typeEditionAvisEcheance", typeEditionAvisEcheance);
	}
	public void initListeTypeCanaux() {
		listeTypeCanaux.put("typeCanalImpression", typeCanalImpression);
		listeTypeCanaux.put("typeCanalMail", typeCanalMail);
		listeTypeCanaux.put("typeCanalEspacecli", typeCanalEspacecli);
	}
	public EditionDuDossierController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaEditionDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		TaEdition taEdition = new TaEdition();
		try {
			if(selection!=null && selection.getId()!=null){
				taEdition = taEditionService.findById(selection.getId());
			}

			taEditionService.remove(taEdition);
			values.remove(selection);
			
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaEditionDTO();
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("TVA", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TVA", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaEditionDTO retour = null;
			taEdition=new TaEdition();
			if(nouveau.getId()==null || taEditionService.findById(nouveau.getId()) == null){
				mapperUIToModel.map(nouveau, taEdition);
				taEdition = taEditionService.merge(taEdition, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taEdition, nouveau);
				values= taEditionService.selectAllDTO();
				nouveau = values.get(0);
				nouveau = new TaEditionDTO();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taEdition = taEditionService.findById(nouveau.getId());
					mapperUIToModel.map(nouveau, taEdition);
					taEdition = taEditionService.merge(taEdition, ITaTCiviliteServiceRemote.validationContext);
					mapperModelToUI.map(taEdition, nouveau);
					values= taEditionService.selectAllDTO();
					nouveau = values.get(0);
					nouveau = new TaEditionDTO();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(taEdition);
			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "TypeCodeTVA", e.getMessage()));
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaEditionDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaEditionDTO> getValues(){  
		return values;
	}

	public TaEditionDTO getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaEditionDTO newTaEdition) {
		this.nouveau = newTaEdition;
	}

	public TaEditionDTO getSelection() {
		return selection;
	}

	public void setSelection(TaEditionDTO selectedTaEdition) {
		this.selection = selectedTaEdition;
	}

	public void setValues(List<TaEditionDTO> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}
	
	public void onTabChange(TabChangeEvent event) {  
		refresh();
	}

	public List<TaEditionDTO> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaEditionDTO> filteredValues) {
		this.filteredValues = filteredValues;
	}
	
	public void definiEditionDevisDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition devis
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						selectedEditionDefautDevisMail = taEditionDTO;
						break;
	
					case TaActionEdition.code_impression:
						selectedEditionDefautDevisImpression = taEditionDTO;
						break;
						
					case TaActionEdition.code_espacecli:
						selectedEditionDefautDevisDisposition = taEditionDTO;
						break;
					}
			}
		}
	}
	
	public void definiEditionFactureDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition factures
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						selectedEditionDefautFactureMail = taEditionDTO;
						break;
	
					case TaActionEdition.code_impression:
						selectedEditionDefautFactureImpression = taEditionDTO;
						break;
						
					case TaActionEdition.code_espacecli:
						selectedEditionDefautFactureDisposition = taEditionDTO;
						break;
					}
			}
		}
	}
	public void definiEditionAvoirDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition factures
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautAvoirMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautAvoirImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautAvoirDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	public void definiEditionBonlivDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition 
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautBonlivMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautBonlivImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautBonlivDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	public void definiEditionProformaDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition 
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautProformaMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautProformaImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautProformaDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	
	public void definiEditionBonCdeDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition 
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautBoncdeMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautBoncdeImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautBoncdeDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	
	public void definiEditionAcompteDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition 
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautAcompteMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautAcompteImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautAcompteDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	
	public void definiEditionApporteurDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition 
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautApporteurMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautApporteurImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautApporteurDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	
	public void definiEditionPrelevementDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition 
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautPrelevementMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautPrelevementImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautPrelevementDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	
	public void definiEditionAvisEcheanceDefautSelectionnee(List<TaEditionDTO> listeEdition) {
		for (TaEditionDTO taEditionDTO : listeEdition) {//on parcours chaque édition 
			for (TaActionEditionDTO taActionDTO : taEditionDTO.getTaActionEditionDTO()) {//on parcours chaque action edition par defaut éventuelles
					switch (taActionDTO.getCodeAction()) {
					case TaActionEdition.code_mail:
						setSelectedEditionDefautAvisEcheanceMail(taEditionDTO);
						break;
	
					case TaActionEdition.code_impression:
						setSelectedEditionDefautAvisEcheanceImpression(taEditionDTO);
						break;
						
					case TaActionEdition.code_espacecli:
						setSelectedEditionDefautAvisEcheanceDisposition(taEditionDTO);
						break;
					}
			}
		}
	}
	
	public void refresh(){
		values = taEditionService.selectAllDTO();
		
		devisValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaDevis.TYPE_DOC.toUpperCase());
		factureValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaFacture.TYPE_DOC.toUpperCase());
		avoirValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaAvoir.TYPE_DOC.toUpperCase());
		bonlivValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaBonliv.TYPE_DOC.toUpperCase());
		proformaValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaProforma.TYPE_DOC.toUpperCase());
		bonCdeValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaBoncde.TYPE_DOC.toUpperCase());
		acompteValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaAcompte.TYPE_DOC.toUpperCase());
		apporteurValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaApporteur.TYPE_DOC.toUpperCase());
		prelevementValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaPrelevement.TYPE_DOC.toUpperCase());
		avisEcheanceValues = taEditionService.findByCodeTypeDTOAvecActionsEdition(TaAvisEcheance.TYPE_DOC.toUpperCase());
		
		definiEditionDevisDefautSelectionnee(devisValues);
		definiEditionFactureDefautSelectionnee(factureValues);
		definiEditionAvoirDefautSelectionnee(avoirValues);
		definiEditionBonlivDefautSelectionnee(bonlivValues);
		definiEditionProformaDefautSelectionnee(proformaValues);
		definiEditionBonCdeDefautSelectionnee(bonCdeValues);
		definiEditionAcompteDefautSelectionnee(acompteValues);
		definiEditionApporteurDefautSelectionnee(apporteurValues);
		definiEditionPrelevementDefautSelectionnee(prelevementValues);
		definiEditionAvisEcheanceDefautSelectionnee(avisEcheanceValues);
		
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(params.get("idEntity")!=null) {
				try {
					selection = taEditionService.findByIdDTO(LibConversion.stringToInteger(params.get("idEntity")));
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			actModifier(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}
	}
	
//NEW
	public void appliquerChoixEditionTousCanaux(String typeEdition){
		if(typeEdition != null) {
			
			switch (typeEdition) {
			
			case TaDevis.TYPE_DOC:
				
				selectedEditionDefautDevisDisposition = selectedEditionDefautDevis;
				selectedEditionDefautDevisImpression = selectedEditionDefautDevis;
				selectedEditionDefautDevisMail = selectedEditionDefautDevis;
					
					
					try {
						//on enregistre les nouvelles éditions par defaut pour chaque canaux
						for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
							enregistreEditionDefaut(devisValues, selectedEditionDefautDevisImpression, selectedEditionDefautDevisMail, selectedEditionDefautDevisDisposition, canal.getValue());
						}  
						
						
					} catch (FinderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				break;
				
			//si c'est l'onglet facture qui est concerné
			case TaFacture.TYPE_DOC:
				
				selectedEditionDefautFactureDisposition = selectedEditionDefautFacture;
				selectedEditionDefautFactureImpression = selectedEditionDefautFacture;
				selectedEditionDefautFactureMail = selectedEditionDefautFacture;
					
					
					try {
						//on enregistre les nouvelles éditions par defaut pour chaque canaux
						for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
							enregistreEditionDefaut(factureValues, selectedEditionDefautFactureImpression, selectedEditionDefautFactureMail, selectedEditionDefautFactureDisposition, canal.getValue());
						}  
						
						
					} catch (FinderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				break;
				
			case TaBonliv.TYPE_DOC:
				
				selectedEditionDefautBonlivDisposition = selectedEditionDefautBonliv;
				selectedEditionDefautBonlivImpression = selectedEditionDefautBonliv;
				selectedEditionDefautBonlivMail = selectedEditionDefautBonliv;
				
				try {
					//on enregistre les nouvelles éditions par defaut pour chaque canaux
					for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
						enregistreEditionDefaut(bonlivValues, selectedEditionDefautBonlivImpression, selectedEditionDefautBonlivMail, selectedEditionDefautBonlivDisposition, canal.getValue());
					}  
					
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
							
				break;
				
				
			case TaAvoir.TYPE_DOC:
				selectedEditionDefautAvoirDisposition = selectedEditionDefautAvoir;
				selectedEditionDefautAvoirImpression = selectedEditionDefautAvoir;
				selectedEditionDefautAvoirMail = selectedEditionDefautAvoir;
				
				try {
					//on enregistre les nouvelles éditions par defaut pour chaque canaux
					for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
						enregistreEditionDefaut(avoirValues, selectedEditionDefautAvoirImpression, selectedEditionDefautAvoirMail, selectedEditionDefautAvoirDisposition, canal.getValue());
					}  
					
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
				
				
			case TaProforma.TYPE_DOC:
				selectedEditionDefautProformaDisposition = selectedEditionDefautProforma;
				selectedEditionDefautProformaImpression = selectedEditionDefautProforma;
				selectedEditionDefautProformaMail = selectedEditionDefautProforma;
				try {
					//on enregistre les nouvelles éditions par defaut pour chaque canaux
					for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
						enregistreEditionDefaut(proformaValues, selectedEditionDefautProformaImpression, selectedEditionDefautProformaMail, selectedEditionDefautProformaDisposition, canal.getValue());
					}  
					
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
			case TaBoncde.TYPE_DOC:
				selectedEditionDefautBoncdeDisposition = selectedEditionDefautBoncde;
				selectedEditionDefautBoncdeImpression = selectedEditionDefautBoncde;
				selectedEditionDefautBoncdeMail = selectedEditionDefautBoncde;
				try {
					//on enregistre les nouvelles éditions par defaut pour chaque canaux
					for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
						enregistreEditionDefaut(bonCdeValues, selectedEditionDefautBoncdeImpression, selectedEditionDefautBoncdeMail, selectedEditionDefautBoncdeDisposition, canal.getValue());
					}  
					
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;

			case TaAcompte.TYPE_DOC:
				selectedEditionDefautAcompteDisposition = selectedEditionDefautAcompte;
				selectedEditionDefautAcompteImpression = selectedEditionDefautAcompte;
				selectedEditionDefautAcompteMail = selectedEditionDefautAcompte;
				try {
					//on enregistre les nouvelles éditions par defaut pour chaque canaux
					for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
						enregistreEditionDefaut(acompteValues, selectedEditionDefautAcompteImpression, selectedEditionDefautAcompteMail, selectedEditionDefautAcompteDisposition, canal.getValue());
					}  
					
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
			case TaApporteur.TYPE_DOC:
				selectedEditionDefautApporteurDisposition = selectedEditionDefautApporteur;
				selectedEditionDefautApporteurImpression = selectedEditionDefautApporteur;
				selectedEditionDefautApporteurMail = selectedEditionDefautApporteur;
				try {
					//on enregistre les nouvelles éditions par defaut pour chaque canaux
					for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
						enregistreEditionDefaut(apporteurValues, selectedEditionDefautApporteurImpression, selectedEditionDefautApporteurMail, selectedEditionDefautApporteurDisposition, canal.getValue());
					}  
					
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
			case TaPrelevement.TYPE_DOC:
				selectedEditionDefautPrelevementDisposition = selectedEditionDefautPrelevement;
				selectedEditionDefautPrelevementImpression = selectedEditionDefautPrelevement;
				selectedEditionDefautPrelevementMail = selectedEditionDefautPrelevement;
				try {
					//on enregistre les nouvelles éditions par defaut pour chaque canaux
					for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
						enregistreEditionDefaut(prelevementValues, selectedEditionDefautPrelevementImpression, selectedEditionDefautPrelevementMail, selectedEditionDefautPrelevementDisposition, canal.getValue());
					}  
					
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
			case TaAvisEcheance.TYPE_DOC:
				
				selectedEditionDefautAvisEcheanceDisposition = selectedEditionDefautAvisEcheance;
				selectedEditionDefautAvisEcheanceImpression = selectedEditionDefautAvisEcheance;
				selectedEditionDefautAvisEcheanceMail = selectedEditionDefautAvisEcheance;
					
					
					try {
						//on enregistre les nouvelles éditions par defaut pour chaque canaux
						for (Map.Entry<String,String> canal : listeTypeCanaux.entrySet()) {
							enregistreEditionDefaut(avisEcheanceValues, selectedEditionDefautAvisEcheanceImpression, selectedEditionDefautAvisEcheanceMail, selectedEditionDefautAvisEcheanceDisposition, canal.getValue());
						}  
						
						
					} catch (FinderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				break;
				
			default:
				break;
			}
			
			//refresh();
			
		}
		
	}	
	

	
	public void appliquerChoixEditionTousCanauxGlobal(TaEditionDTO edition) {
		
	}
	
	public void enregistreEditionDefaut(List<TaEditionDTO> listeEdition, TaEditionDTO selectedImpression,TaEditionDTO selectedMail,TaEditionDTO selectedEspacecli, String typeCanal ) throws FinderException {
		for (TaEditionDTO taEditionDTO : listeEdition) {// pour chaque edition 
			TaEdition ed = taEditionService.findById(taEditionDTO.getId());//je récupère l'entité
			//Cette boucle permet de supprimer des items de la liste que l'on parcours de façon sure, très utile
			for (Iterator<TaActionEdition> iterator = ed.getActionEdition().iterator(); iterator.hasNext();) {
				TaActionEdition action = iterator.next();
			
					if(action.getCodeAction().equals(typeCanal)) {//je supprime l'éventuel action defaut impression
						iterator.remove();
					}
			}
			
			switch (typeCanal) {
			case TaActionEdition.code_espacecli:
				if(selectedEspacecli != null) {
					if(ed.getIdEdition() == selectedEspacecli.getId()) {//si c'est l'édition séléctionnée
						ed.getActionEdition().add(actionEspaceclie);//j'ajoute l'action EspaceClient a sa liste d'action
					}
				}
				
				break;
				
			case TaActionEdition.code_impression:
				if(selectedImpression != null) {
					if(ed.getIdEdition() == selectedImpression.getId()) {//si c'est l'édition séléctionnée
						ed.getActionEdition().add(actionImpression);//j'ajoute l'action impression a sa liste d'action
					}
				}
					
				break;
				
			case TaActionEdition.code_mail:
				if(selectedMail != null) {
					if(ed.getIdEdition() == selectedMail.getId()) {//si c'est l'édition séléctionnée
						ed.getActionEdition().add(actionMail);//j'ajoute l'action mail a sa liste d'action
					}
				}
				
				break;
				

			default:
				break;
			}
			
			taEditionService.merge(ed);//j'enregistre
		}
			
	}
	
	public void changeEditionDefaut(AjaxBehaviorEvent event) throws FinderException {
		//méthode déclenchée à chaque choix dans le selectOneMenu pour l'impression, dans l'onglet correspondant (facture, avoir...)
		String typeCanal = (String) event.getComponent().getAttributes().get("typeCanal");
		String typeEdition = (String) event.getComponent().getAttributes().get("typeEdition");
			
			switch (typeEdition) {
			case TaDevis.TYPE_DOC:
				enregistreEditionDefaut(devisValues, selectedEditionDefautDevisImpression, selectedEditionDefautDevisMail, selectedEditionDefautDevisDisposition, typeCanal);
				break;
			case TaFacture.TYPE_DOC:
				enregistreEditionDefaut(factureValues, selectedEditionDefautFactureImpression, selectedEditionDefautFactureMail, selectedEditionDefautFactureDisposition, typeCanal);
				break;
			case TaBonliv.TYPE_DOC:
				enregistreEditionDefaut(bonlivValues, selectedEditionDefautBonlivImpression, selectedEditionDefautBonlivMail, selectedEditionDefautBonlivDisposition, typeCanal);
				break;
			case TaAvoir.TYPE_DOC:
				enregistreEditionDefaut(avoirValues, selectedEditionDefautAvoirImpression, selectedEditionDefautAvoirMail, selectedEditionDefautAvoirDisposition, typeCanal);
				break;
			case TaProforma.TYPE_DOC:
				enregistreEditionDefaut(proformaValues, selectedEditionDefautProformaImpression, selectedEditionDefautProformaMail, selectedEditionDefautProformaDisposition, typeCanal);
				break;
			case TaBoncde.TYPE_DOC:
				enregistreEditionDefaut(bonCdeValues, selectedEditionDefautBoncdeImpression, selectedEditionDefautBoncdeMail, selectedEditionDefautBoncdeDisposition, typeCanal);
				break;
			case TaAcompte.TYPE_DOC:
				enregistreEditionDefaut(acompteValues, selectedEditionDefautAcompteImpression, selectedEditionDefautAcompteMail, selectedEditionDefautAcompteDisposition, typeCanal);
				break;
			case TaApporteur.TYPE_DOC:
				enregistreEditionDefaut(apporteurValues, selectedEditionDefautApporteurImpression, selectedEditionDefautApporteurMail, selectedEditionDefautApporteurDisposition, typeCanal);
				break;
			case TaPrelevement.TYPE_DOC:
				enregistreEditionDefaut(prelevementValues, selectedEditionDefautPrelevementImpression, selectedEditionDefautPrelevementMail, selectedEditionDefautPrelevementDisposition, typeCanal);
				break;
			case TaAvisEcheance.TYPE_DOC:
				enregistreEditionDefaut(avisEcheanceValues, selectedEditionDefautAvisEcheanceImpression, selectedEditionDefautAvisEcheanceMail, selectedEditionDefautAvisEcheanceDisposition, typeCanal);
				break;
			default:
				break;
			}	
			
			//refresh();// je rafraichis
		
	}
	

	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
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
			case "supprimer":
			case "modifier":
			case "inserer":
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

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("CodeTVA", "actImprimer"));
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("codeTVA", taEditionService.findById(selection.getId()));

			//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("CodeTVAController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//		String msg = "";
//		try {
//			String nomChamp =  (String) component.getAttributes().get("nomChamp");
//			validateUIField(nomChamp,value);
//			TaEditionDTO temp=new TaEditionDTO();
//			PropertyUtils.setProperty(temp, nomChamp, value);
//			taTCodeTVAService.validateEntityProperty(temp, nomChamp, ITaEditionServiceRemote.validationContext );
//		} catch(Exception e) {
//			msg += e.getMessage();
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
//		}
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaEditionDTO>> violations = factory.getValidator().validateValue(TaEditionDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaEditionDTO> cv : violations) {
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
		try {
			if(nomChamp.equals(Const.C_CODE_TVA)) {
					boolean changement=false;
					if(selection.getCodeEdition()!=null && value!=null && !selection.getCodeEdition().equals(""))
					{
						if(value instanceof TaEdition)
							changement=((TaEdition) value).getCodeEdition().equals(selection.getCodeEdition());
						else if(value instanceof String)
						changement=!value.equals(selection.getCodeEdition());
					}
					if(changement && modeEcran.dataSetEnModeModification()){
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Code tva", Const.C_MESSAGE_CHANGEMENT_CODE));
					}
				}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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

		PrimeFaces.current().dialog().openDynamic("articles/dialog_type_code_tva", options, params);

	}

	public void handleReturnDialogTypes(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taEdition = (TaEdition) event.getObject();
			
		}
		refresh();
	}
	
	public void actDialogCatalogueEdition(ActionEvent actionEvent) {
	    
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
		
//		PaiementParam email = new PaiementParam();
//		email.setEmailExpediteur(null);
//		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" Facture "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre facture "+masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre facture "+masterEntity.getCodeDocument());
//		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
//		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
//		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File(((ITaFactureServiceRemote)taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		
//		sessionMap.put(PaiementParam.NOM_OBJET_EN_SESSION, email);
        
		PrimeFaces.current().dialog().openDynamic("/dialog_catalogue_edition", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogCatalogueEdition(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
		}
		refresh();
	}

	public void actDialogModifier(ActionEvent actionEvent){

		nouveau = selection;
		//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

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

		PrimeFaces.current().dialog().openDynamic("articles/dialog_type_code_tva", options, params);

	}
	// Dima -  Fin	

	public List<TaEditionDTO> getFactureValues() {
		return factureValues;
	}

	public void setFactureValues(List<TaEditionDTO> factureValues) {
		this.factureValues = factureValues;
	}

	public List<TaEditionDTO> getAvoirValues() {
		return avoirValues;
	}

	public void setAvoirValues(List<TaEditionDTO> avoirValues) {
		this.avoirValues = avoirValues;
	}

	public List<TaEditionDTO> getBonlivValues() {
		return bonlivValues;
	}

	public void setBonlivValues(List<TaEditionDTO> bonlivValues) {
		this.bonlivValues = bonlivValues;
	}

	public List<TaEditionDTO> getProformaValues() {
		return proformaValues;
	}

	public void setProformaValues(List<TaEditionDTO> proformaValues) {
		this.proformaValues = proformaValues;
	}

	public TaEditionDTO getSelectedEditionDefautFactureImpression() {
		return selectedEditionDefautFactureImpression;
	}

	public void setSelectedEditionDefautFactureImpression(TaEditionDTO selectedEditionDefautFactureImpression) {
		this.selectedEditionDefautFactureImpression = selectedEditionDefautFactureImpression;
	}

	public TaEditionDTO getSelectedEditionDefautFactureMail() {
		return selectedEditionDefautFactureMail;
	}

	public void setSelectedEditionDefautFactureMail(TaEditionDTO selectedEditionDefautFactureMail) {
		this.selectedEditionDefautFactureMail = selectedEditionDefautFactureMail;
	}

	public TaEditionDTO getSelectedEditionDefautFactureDisposition() {
		return selectedEditionDefautFactureDisposition;
	}

	public void setSelectedEditionDefautFactureDisposition(TaEditionDTO selectedEditionDefautFactureDisposition) {
		this.selectedEditionDefautFactureDisposition = selectedEditionDefautFactureDisposition;
	}

	public TaActionEdition getActionImpression() {
		return actionImpression;
	}

	public void setActionImpression(TaActionEdition actionImpression) {
		this.actionImpression = actionImpression;
	}

	public TaActionEdition getActionMail() {
		return actionMail;
	}

	public void setActionMail(TaActionEdition actionMail) {
		this.actionMail = actionMail;
	}

	public TaActionEdition getActionEspaceclie() {
		return actionEspaceclie;
	}

	public void setActionEspaceclie(TaActionEdition actionEspaceclie) {
		this.actionEspaceclie = actionEspaceclie;
	}


	public String getTypeCanalImpression() {
		return typeCanalImpression;
	}

	public void setTypeCanalImpression(String typeCanalImpression) {
		this.typeCanalImpression = typeCanalImpression;
	}

	public String getTypeCanalMail() {
		return typeCanalMail;
	}

	public void setTypeCanalMail(String typeCanalMail) {
		this.typeCanalMail = typeCanalMail;
	}

	public String getTypeCanalEspacecli() {
		return typeCanalEspacecli;
	}

	public void setTypeCanalEspacecli(String typeCanalEspacecli) {
		this.typeCanalEspacecli = typeCanalEspacecli;
	}

	public TaEditionDTO getSelectedEditionDefautAvoirImpression() {
		return selectedEditionDefautAvoirImpression;
	}

	public void setSelectedEditionDefautAvoirImpression(TaEditionDTO selectedEditionDefautAvoirImpression) {
		this.selectedEditionDefautAvoirImpression = selectedEditionDefautAvoirImpression;
	}

	public TaEditionDTO getSelectedEditionDefautAvoirMail() {
		return selectedEditionDefautAvoirMail;
	}

	public void setSelectedEditionDefautAvoirMail(TaEditionDTO selectedEditionDefautAvoirMail) {
		this.selectedEditionDefautAvoirMail = selectedEditionDefautAvoirMail;
	}

	public TaEditionDTO getSelectedEditionDefautAvoirDisposition() {
		return selectedEditionDefautAvoirDisposition;
	}

	public void setSelectedEditionDefautAvoirDisposition(TaEditionDTO selectedEditionDefautAvoirDisposition) {
		this.selectedEditionDefautAvoirDisposition = selectedEditionDefautAvoirDisposition;
	}

	public TaEditionDTO getSelectedEditionDefautBonlivImpression() {
		return selectedEditionDefautBonlivImpression;
	}

	public void setSelectedEditionDefautBonlivImpression(TaEditionDTO selectedEditionDefautBonlivImpression) {
		this.selectedEditionDefautBonlivImpression = selectedEditionDefautBonlivImpression;
	}

	public TaEditionDTO getSelectedEditionDefautBonlivMail() {
		return selectedEditionDefautBonlivMail;
	}

	public void setSelectedEditionDefautBonlivMail(TaEditionDTO selectedEditionDefautBonlivMail) {
		this.selectedEditionDefautBonlivMail = selectedEditionDefautBonlivMail;
	}

	public TaEditionDTO getSelectedEditionDefautBonlivDisposition() {
		return selectedEditionDefautBonlivDisposition;
	}

	public void setSelectedEditionDefautBonlivDisposition(TaEditionDTO selectedEditionDefautBonlivDisposition) {
		this.selectedEditionDefautBonlivDisposition = selectedEditionDefautBonlivDisposition;
	}

	public TaEditionDTO getSelectedEditionDefautProformaImpression() {
		return selectedEditionDefautProformaImpression;
	}

	public void setSelectedEditionDefautProformaImpression(TaEditionDTO selectedEditionDefautProformaImpression) {
		this.selectedEditionDefautProformaImpression = selectedEditionDefautProformaImpression;
	}

	public TaEditionDTO getSelectedEditionDefautProformaMail() {
		return selectedEditionDefautProformaMail;
	}

	public void setSelectedEditionDefautProformaMail(TaEditionDTO selectedEditionDefautProformaMail) {
		this.selectedEditionDefautProformaMail = selectedEditionDefautProformaMail;
	}

	public TaEditionDTO getSelectedEditionDefautProformaDisposition() {
		return selectedEditionDefautProformaDisposition;
	}

	public void setSelectedEditionDefautProformaDisposition(TaEditionDTO selectedEditionDefautProformaDisposition) {
		this.selectedEditionDefautProformaDisposition = selectedEditionDefautProformaDisposition;
	}
	
	public String getTypeEditionDevis() {
		return typeEditionDevis;
	}
	public void setTypeEditionDevis(String typeEditionDevis) {
		this.typeEditionDevis = typeEditionDevis;
	}
	
	public String getTypeEditionFacture() {
		return typeEditionFacture;
	}

	public void setTypeEditionFacture(String typeEditionFacture) {
		this.typeEditionFacture = typeEditionFacture;
	}

	public String getTypeEditionAvoir() {
		return typeEditionAvoir;
	}

	public void setTypeEditionAvoir(String typeEditionAvoir) {
		this.typeEditionAvoir = typeEditionAvoir;
	}

	public String getTypeEditionBonliv() {
		return typeEditionBonliv;
	}

	public void setTypeEditionBonliv(String typeEditionBonliv) {
		this.typeEditionBonliv = typeEditionBonliv;
	}

	public String getTypeEditionProforma() {
		return typeEditionProforma;
	}

	public void setTypeEditionProforma(String typeEditionProforma) {
		this.typeEditionProforma = typeEditionProforma;
	}

	public Map<String, String> getListeTypeEdition() {
		return listeTypeEdition;
	}

	public void setListeTypeEdition(Map<String, String> listeTypeEdition) {
		this.listeTypeEdition = listeTypeEdition;
	}

	public Map<String, String> getListeTypeCanaux() {
		return listeTypeCanaux;
	}

	public void setListeTypeCanaux(Map<String, String> listeTypeCanaux) {
		this.listeTypeCanaux = listeTypeCanaux;
	}
	public TaEditionDTO getSelectedEditionDefautFacture() {
		return selectedEditionDefautFacture;
	}
	public void setSelectedEditionDefautFacture(TaEditionDTO selectedEditionDefautFacture) {
		this.selectedEditionDefautFacture = selectedEditionDefautFacture;
	}
	public TaEditionDTO getSelectedEditionDefautAvoir() {
		return selectedEditionDefautAvoir;
	}
	public void setSelectedEditionDefautAvoir(TaEditionDTO selectedEditionDefautAvoir) {
		this.selectedEditionDefautAvoir = selectedEditionDefautAvoir;
	}
	public TaEditionDTO getSelectedEditionDefautProforma() {
		return selectedEditionDefautProforma;
	}
	public void setSelectedEditionDefautProforma(TaEditionDTO selectedEditionDefautProforma) {
		this.selectedEditionDefautProforma = selectedEditionDefautProforma;
	}
	public TaEditionDTO getSelectedEditionDefautBonliv() {
		return selectedEditionDefautBonliv;
	}
	public void setSelectedEditionDefautBonliv(TaEditionDTO selectedEditionDefautBonliv) {
		this.selectedEditionDefautBonliv = selectedEditionDefautBonliv;
	}
	public List<TaEditionDTO> getDevisValues() {
		return devisValues;
	}
	public void setDevisValues(List<TaEditionDTO> devisValues) {
		this.devisValues = devisValues;
	}
	public TaEditionDTO getSelectedEditionDefautDevis() {
		return selectedEditionDefautDevis;
	}
	public void setSelectedEditionDefautDevis(TaEditionDTO selectedEditionDefautDevis) {
		this.selectedEditionDefautDevis = selectedEditionDefautDevis;
	}
	public TaEditionDTO getSelectedEditionDefautDevisImpression() {
		return selectedEditionDefautDevisImpression;
	}
	public void setSelectedEditionDefautDevisImpression(TaEditionDTO selectedEditionDefautDevisImpression) {
		this.selectedEditionDefautDevisImpression = selectedEditionDefautDevisImpression;
	}
	public TaEditionDTO getSelectedEditionDefautDevisMail() {
		return selectedEditionDefautDevisMail;
	}
	public void setSelectedEditionDefautDevisMail(TaEditionDTO selectedEditionDefautDevisMail) {
		this.selectedEditionDefautDevisMail = selectedEditionDefautDevisMail;
	}
	public TaEditionDTO getSelectedEditionDefautDevisDisposition() {
		return selectedEditionDefautDevisDisposition;
	}
	public void setSelectedEditionDefautDevisDisposition(TaEditionDTO selectedEditionDefautDevisDisposition) {
		this.selectedEditionDefautDevisDisposition = selectedEditionDefautDevisDisposition;
	}
	public List<TaEditionDTO> getBonCdeValues() {
		return bonCdeValues;
	}
	public void setBonCdeValues(List<TaEditionDTO> bonCdeValues) {
		this.bonCdeValues = bonCdeValues;
	}
	public TaEditionDTO getSelectedEditionDefautBoncde() {
		return selectedEditionDefautBoncde;
	}
	public void setSelectedEditionDefautBoncde(TaEditionDTO selectedEditionDefautBoncde) {
		this.selectedEditionDefautBoncde = selectedEditionDefautBoncde;
	}
	public TaEditionDTO getSelectedEditionDefautBoncdeImpression() {
		return selectedEditionDefautBoncdeImpression;
	}
	public void setSelectedEditionDefautBoncdeImpression(TaEditionDTO selectedEditionDefautBoncdeImpression) {
		this.selectedEditionDefautBoncdeImpression = selectedEditionDefautBoncdeImpression;
	}
	public TaEditionDTO getSelectedEditionDefautBoncdeMail() {
		return selectedEditionDefautBoncdeMail;
	}
	public void setSelectedEditionDefautBoncdeMail(TaEditionDTO selectedEditionDefautBoncdeMail) {
		this.selectedEditionDefautBoncdeMail = selectedEditionDefautBoncdeMail;
	}
	public TaEditionDTO getSelectedEditionDefautBoncdeDisposition() {
		return selectedEditionDefautBoncdeDisposition;
	}
	public void setSelectedEditionDefautBoncdeDisposition(TaEditionDTO selectedEditionDefautBoncdeDisposition) {
		this.selectedEditionDefautBoncdeDisposition = selectedEditionDefautBoncdeDisposition;
	}
	public String getTypeEditionBoncde() {
		return typeEditionBoncde;
	}
	public void setTypeEditionBoncde(String typeEditionBoncde) {
		this.typeEditionBoncde = typeEditionBoncde;
	}
	public List<TaEditionDTO> getAcompteValues() {
		return acompteValues;
	}
	public void setAcompteValues(List<TaEditionDTO> acompteValues) {
		this.acompteValues = acompteValues;
	}
	
	public TaEditionDTO getSelectedEditionDefautAcompteDisposition() {
		return selectedEditionDefautAcompteDisposition;
	}
	public void setSelectedEditionDefautAcompteDisposition(TaEditionDTO selectedEditionDefautAcompteDisposition) {
		this.selectedEditionDefautAcompteDisposition = selectedEditionDefautAcompteDisposition;
	}
	public String getTypeEditionAcompte() {
		return typeEditionAcompte;
	}
	public void setTypeEditionAcompte(String typeEditionAcompte) {
		this.typeEditionAcompte = typeEditionAcompte;
	}
	public TaEditionDTO getSelectedEditionDefautAcompteImpression() {
		return selectedEditionDefautAcompteImpression;
	}
	public void setSelectedEditionDefautAcompteImpression(TaEditionDTO selectedEditionDefautAcompteImpression) {
		this.selectedEditionDefautAcompteImpression = selectedEditionDefautAcompteImpression;
	}
	public TaEditionDTO getSelectedEditionDefautAcompteMail() {
		return selectedEditionDefautAcompteMail;
	}
	public void setSelectedEditionDefautAcompteMail(TaEditionDTO selectedEditionDefautAcompteMail) {
		this.selectedEditionDefautAcompteMail = selectedEditionDefautAcompteMail;
	}
	public TaEditionDTO getSelectedEditionDefautAcompte() {
		return selectedEditionDefautAcompte;
	}
	public void setSelectedEditionDefautAcompte(TaEditionDTO selectedEditionDefautAcompte) {
		this.selectedEditionDefautAcompte = selectedEditionDefautAcompte;
	}
	public List<TaEditionDTO> getApporteurValues() {
		return apporteurValues;
	}
	public void setApporteurValues(List<TaEditionDTO> apporteurValues) {
		this.apporteurValues = apporteurValues;
	}
	public TaEditionDTO getSelectedEditionDefautApporteur() {
		return selectedEditionDefautApporteur;
	}
	public void setSelectedEditionDefautApporteur(TaEditionDTO selectedEditionDefautApporteur) {
		this.selectedEditionDefautApporteur = selectedEditionDefautApporteur;
	}
	public TaEditionDTO getSelectedEditionDefautApporteurImpression() {
		return selectedEditionDefautApporteurImpression;
	}
	public void setSelectedEditionDefautApporteurImpression(TaEditionDTO selectedEditionDefautApporteurImpression) {
		this.selectedEditionDefautApporteurImpression = selectedEditionDefautApporteurImpression;
	}
	public TaEditionDTO getSelectedEditionDefautApporteurMail() {
		return selectedEditionDefautApporteurMail;
	}
	public void setSelectedEditionDefautApporteurMail(TaEditionDTO selectedEditionDefautApporteurMail) {
		this.selectedEditionDefautApporteurMail = selectedEditionDefautApporteurMail;
	}
	public TaEditionDTO getSelectedEditionDefautApporteurDisposition() {
		return selectedEditionDefautApporteurDisposition;
	}
	public void setSelectedEditionDefautApporteurDisposition(TaEditionDTO selectedEditionDefautApporteurDisposition) {
		this.selectedEditionDefautApporteurDisposition = selectedEditionDefautApporteurDisposition;
	}
	public String getTypeEditionApporteur() {
		return typeEditionApporteur;
	}
	public void setTypeEditionApporteur(String typeEditionApporteur) {
		this.typeEditionApporteur = typeEditionApporteur;
	}
	public List<TaEditionDTO> getPrelevementValues() {
		return prelevementValues;
	}
	public void setPrelevementValues(List<TaEditionDTO> prelevementValues) {
		this.prelevementValues = prelevementValues;
	}
	public TaEditionDTO getSelectedEditionDefautPrelevement() {
		return selectedEditionDefautPrelevement;
	}
	public void setSelectedEditionDefautPrelevement(TaEditionDTO selectedEditionDefautPrelevement) {
		this.selectedEditionDefautPrelevement = selectedEditionDefautPrelevement;
	}
	public TaEditionDTO getSelectedEditionDefautPrelevementImpression() {
		return selectedEditionDefautPrelevementImpression;
	}
	public void setSelectedEditionDefautPrelevementImpression(TaEditionDTO selectedEditionDefautPrelevementImpression) {
		this.selectedEditionDefautPrelevementImpression = selectedEditionDefautPrelevementImpression;
	}
	public TaEditionDTO getSelectedEditionDefautPrelevementMail() {
		return selectedEditionDefautPrelevementMail;
	}
	public void setSelectedEditionDefautPrelevementMail(TaEditionDTO selectedEditionDefautPrelevementMail) {
		this.selectedEditionDefautPrelevementMail = selectedEditionDefautPrelevementMail;
	}
	public TaEditionDTO getSelectedEditionDefautPrelevementDisposition() {
		return selectedEditionDefautPrelevementDisposition;
	}
	public void setSelectedEditionDefautPrelevementDisposition(TaEditionDTO selectedEditionDefautPrelevementDisposition) {
		this.selectedEditionDefautPrelevementDisposition = selectedEditionDefautPrelevementDisposition;
	}
	public String getTypeEditionPrelevement() {
		return typeEditionPrelevement;
	}
	public void setTypeEditionPrelevement(String typeEditionPrelevement) {
		this.typeEditionPrelevement = typeEditionPrelevement;
	}
	public TaEditionDTO getSelectedEditionDefautAvisEcheance() {
		return selectedEditionDefautAvisEcheance;
	}
	public void setSelectedEditionDefautAvisEcheance(TaEditionDTO selectedEditionDefautAvisEcheance) {
		this.selectedEditionDefautAvisEcheance = selectedEditionDefautAvisEcheance;
	}
	public TaEditionDTO getSelectedEditionDefautAvisEcheanceImpression() {
		return selectedEditionDefautAvisEcheanceImpression;
	}
	public void setSelectedEditionDefautAvisEcheanceImpression(TaEditionDTO selectedEditionDefautAvisEcheanceImpression) {
		this.selectedEditionDefautAvisEcheanceImpression = selectedEditionDefautAvisEcheanceImpression;
	}
	public TaEditionDTO getSelectedEditionDefautAvisEcheanceMail() {
		return selectedEditionDefautAvisEcheanceMail;
	}
	public void setSelectedEditionDefautAvisEcheanceMail(TaEditionDTO selectedEditionDefautAvisEcheanceMail) {
		this.selectedEditionDefautAvisEcheanceMail = selectedEditionDefautAvisEcheanceMail;
	}
	public TaEditionDTO getSelectedEditionDefautAvisEcheanceDisposition() {
		return selectedEditionDefautAvisEcheanceDisposition;
	}
	public void setSelectedEditionDefautAvisEcheanceDisposition(TaEditionDTO selectedEditionDefautAvisEcheanceDisposition) {
		this.selectedEditionDefautAvisEcheanceDisposition = selectedEditionDefautAvisEcheanceDisposition;
	}
	public List<TaEditionDTO> getAvisEcheanceValues() {
		return avisEcheanceValues;
	}
	public void setAvisEcheanceValues(List<TaEditionDTO> avisEcheanceValues) {
		this.avisEcheanceValues = avisEcheanceValues;
	}
	public String getTypeEditionAvisEcheance() {
		return typeEditionAvisEcheance;
	}
	public void setTypeEditionAvisEcheance(String typeEditionAvisEcheance) {
		this.typeEditionAvisEcheance = typeEditionAvisEcheance;
	}
}  
