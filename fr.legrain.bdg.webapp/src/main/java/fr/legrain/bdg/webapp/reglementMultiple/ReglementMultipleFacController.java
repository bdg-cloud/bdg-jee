package fr.legrain.bdg.webapp.reglementMultiple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
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
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.documents.Verrouillage;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaRAcompte;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.model.TaCompteBanque;


@Named
@ViewScoped  
public class ReglementMultipleFacController extends AbstractController{

	private List<TaReglementDTOJSF> valuesAffecte=new LinkedList<>(); 
	private List<TaReglementDTOJSF> valuesExported=new LinkedList<>(); 
	private List<TaReglementDTOJSF> values=new LinkedList<>();
	private List<TaReglementDTOJSF> filteredValues=new LinkedList<>();
	private TaReglementDTOJSF old ;
	private TaReglementDTOJSF nouveau =new TaReglementDTOJSF();
	private TaReglementDTOJSF selection ;
	private TaReglementDTOJSF selectionExported ;
	
	private TaFacture masterEntity;
	private TaFactureDTO masterEntityDTO;
	private Date dateDeb=null;
	private Date dateFin=null;
	
	protected String typePaiementDefaut=null;
	
	private TaRReglement taRReglement ;
	private TaRAvoir taRAvoir ;
	private TaAvoir avoir;
	private TaAcompte acompte;
	TaReglement reglement;
	TaInfoEntreprise infos =null;
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	private String titlePartOne="Gestion des règlements de la facture n° : ";
	private String titlePartTwo="";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaRReglementServiceRemote taRReglementService;
	private @EJB  ITaInfoEntrepriseServiceRemote daoInfos;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaFactureServiceRemote taFactureService;
	protected @EJB ITaTPaiementServiceRemote taTPaiementService;
	protected @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
	private @EJB ITaRAvoirServiceRemote taRAvoirService;
	private @EJB ITaAvoirServiceRemote taAvoirService;
	private @EJB ITaAcompteServiceRemote taAcompteService;
//	private @EJB ITaRAcompteServiceRemote taRAcompteService;
	
	@Inject @Named(value="modelReglement")
	private ModelReglement modelReglement;
	
	private Boolean readOnly=false;
	
	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	private LgrDozerMapper<TaRReglementDTO,TaRReglement> mapperUIToModel  = new LgrDozerMapper<TaRReglementDTO,TaRReglement>();
	private LgrDozerMapper<TaRReglement,TaRReglementDTO> mapperModelToUI  = new LgrDozerMapper<TaRReglement,TaRReglementDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {

			infos=daoInfos.findInstance();

			dateDeb=infos.getDatedebInfoEntreprise();
			dateFin=infos.getDatefinInfoEntreprise();

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			if(sessionMap.get("mapDialogue")!=null) {
				Map<String,Object>	mapDialogue = (Map<String, Object>) sessionMap.get("mapDialogue");
				if(mapDialogue!=null && mapDialogue.size()>0){
					if(mapDialogue.get("masterEntity")!=null){
						if(mapDialogue.get("masterEntity") instanceof TaFacture){
							TaFacture fac=(TaFacture) mapDialogue.get("masterEntity");
							masterEntityDTO= new LgrDozerMapper<TaFacture, TaFactureDTO>().map(fac, TaFactureDTO.class);
						}
						else{
							masterEntityDTO=(TaFactureDTO) mapDialogue.get("masterEntity");
//							masterEntity=taFactureService.findById(factureDTO.getId());
						}
					}
					if(mapDialogue.get("readOnly")!=null){
						readOnly=(Boolean) mapDialogue.get("readOnly");
					}
				}
				cleanAppelDialogue();
			}
			if(masterEntityDTO!=null)titlePartTwo=masterEntityDTO.getCodeDocument();
			refresh();


		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}
	
	private void cleanAppelDialogue() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		sessionMap.remove("mapDialogue");
	}
	
	public ReglementMultipleFacController() {  
	}  

	public List<TaTPaiement> typePaiementAutoComplete(String query) {
		List<TaTPaiement> allValues = taTPaiementService.selectAll();
		List<TaTPaiement> filteredValues = new ArrayList<TaTPaiement>();
		TaTPaiement cp = new TaTPaiement();
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
	
	public List<TaCompteBanque> compteBancaireAutoComplete(String query) {
		List<TaCompteBanque> allValues = taCompteBanqueService.selectCompteEntreprise();
		List<TaCompteBanque> filteredValues = new ArrayList<TaCompteBanque>();
		TaCompteBanque cp = new TaCompteBanque();
		cp.setIban(Const.C_AUCUN);
		cp.setCodeBanque(Const.C_AUCUN);
		cp.setNomBanque(Const.C_AUCUN);
		cp.setCompte(Const.C_AUCUN);
		cp.setCptcomptable(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getIban().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		
		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaCompteBanque &&((((TaCompteBanque) value).getIban()==null)|| ((TaCompteBanque) value).getIban()!=null && ((TaCompteBanque) value).getIban().equals(Const.C_AUCUN)))value=null;
			if(value instanceof TaTPaiement &&((((TaTPaiement) value).getCodeTPaiement()==null)||  ((TaTPaiement) value).getCodeTPaiement()!=null && ((TaTPaiement) value).getCodeTPaiement().equals(Const.C_AUCUN)))value=null;	
		}		
		validateUIField(nomChamp,value);
		if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)){
			selectionTPaiement();
		}
	}
	
	private void selectionTPaiement(){
		if(nouveau!=null && nouveau.getTaTPaiement()!=null)
			nouveau.setTaCompteBanque(taCompteBanqueService.findByTiersEntreprise(nouveau.getTaTPaiement()));
		if(nouveau.getTaCompteBanque()!=null){
			nouveau.getDto().setIban(nouveau.getTaCompteBanque().getIban());
			nouveau.getDto().setIdCompteBanque(nouveau.getTaCompteBanque().getIdCompteBanque());
			nouveau.getDto().setCodeBic(nouveau.getTaCompteBanque().getCodeBIC());
		}
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaReglementDTOJSF();
		nouveau.setTypeDocument(TaReglement.TYPE_DOC);
		selection=nouveau;
		try {
			masterEntity= taFactureService.findById(masterEntityDTO.getId());
			typePaiementDefaut = taPreferencesService.findByCode(TaTPaiement.getCodeTPaiementDefaut()).getValeur();
			if(masterEntity.getTaTiers()!=null && masterEntity.getTaTiers().getTaTPaiement()!=null) {
				typePaiementDefaut = masterEntity.getTaTiers().getTaTPaiement().getCodeTPaiement();
			}
			if (typePaiementDefaut == null || typePaiementDefaut=="") {
				typePaiementDefaut="C";
			}
			nouveau.setTaTPaiement(taTPaiementService.findByCode(typePaiementDefaut));
			
			selectionTPaiement();
		
		} catch (FinderException e1) {
		}

		

		TaRReglement taRReglementTmp=null;
		
			try {
				taRReglement=taFactureService.creeRReglement(masterEntity,taRReglementTmp,false,typePaiementDefaut);
//			validateUIField(Const.C_CODE_DOCUMENT,taRReglement.getTaReglement().getCodeDocument());//permet de verrouiller le code genere			
			if(taFactureService.calculResteAReglerComplet(masterEntity).signum()>0){
				taRReglement.getTaReglement().setNetTtcCalc(taFactureService.calculResteAReglerComplet(masterEntity));
				taRReglement.setAffectation(taFactureService.calculResteAReglerComplet(masterEntity));
			}
			
			masterEntity=taFactureService.addRReglement(masterEntity,taRReglement);
			masterEntity=taFactureService.affecteReglementFacture(masterEntity,typePaiementDefaut);
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			masterEntity=taFactureService.calculRegleDocument(masterEntity);
			TaReglementDTO reglementDTO = new TaReglementDTO();
			new LgrDozerMapper<TaRReglement, TaReglementDTO>().map(taRReglement, reglementDTO);
			if(nouveau.getTaTPaiement()!=null)
				reglementDTO.setLibelleDocument(nouveau.getTaTPaiement().getLibTPaiement());
			nouveau.setDto(reglementDTO);
			modelReglement.getListeObjet().add(nouveau);
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Règlement multiple", e.getMessage()));

			}
	}

	public TaRReglement rechercheDansDocument(TaReglementDTOJSF selection){
		TaRReglement retour=null;
		for (TaRReglement ligne : masterEntity.getTaRReglements()) {
			if (ligne.getTaReglement().getIdDocument()==selection.getDto().getId())
				retour= ligne;
		}
		if(retour!=null){
			reglement=retour.getTaReglement();
			try {
				reglement=taReglementService.findById(reglement.getIdDocument());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (TaRReglement ligne : reglement.getTaRReglements()) {
				if (ligne.getId()==retour.getId())
					return ligne;
			}
		}
		//si pas dans facture et réglement existant(iddocument!=0), on crée le rReglement et on affecte le réglement
		if(selection.getDto().getId()!=null && selection.getDto().getId()!=0){
			try {
				reglement= taReglementService.findById(selection.getDto().getId());
				taRReglement=taFactureService.creeRReglement(masterEntity,null,false,typePaiementDefaut,reglement);
				reglement.addRReglement(taRReglement);
				taRReglement.setTaReglement(reglement);
				if(taFactureService.calculResteAReglerComplet(masterEntity).signum()>0){
					taRReglement.setAffectation(taFactureService.calculResteAReglerComplet(masterEntity));
				}
				
				masterEntity=taFactureService.addRReglement(masterEntity,taRReglement);
				masterEntity=taFactureService.affecteReglementFacture(masterEntity,typePaiementDefaut);
				masterEntity=taFactureService.calculRegleDocument(masterEntity);
				return taRReglement;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	return retour;
	}
	
	public TaRAvoir rechercheDansDocumentAvoir(TaReglementDTOJSF selection){
		
		TaRAvoir retour=null;

		for (TaRAvoir ligne : masterEntity.getTaRAvoirs()) {
			if (ligne.getTaAvoir().getIdDocument()==selection.getDto().getId())
				retour= ligne;
		}
		
		if(retour!=null){
			avoir=retour.getTaAvoir();
			try {
				avoir=taAvoirService.findByIDFetch(avoir.getIdDocument());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (TaRAvoir ligne : avoir.getTaRAvoirs()) {
				if (ligne.getId()==retour.getId())
					return ligne;
			}
		}
		//si pas dans facture et réglement existant(iddocument!=0), on crée le rReglement et on affecte le réglement
		if(retour==null && selection.getDto().getId()!=null && selection.getDto().getId()!=0){
			try {
				taRAvoir = new TaRAvoir();
				avoir=taAvoirService.findByIDFetch(selection.getDto().getId());
				taRAvoir.setTaAvoir(avoir);
				taRAvoir.setTaFacture(masterEntity);
				avoir.addRAvoir(taRAvoir);
				if(taFactureService.calculResteAReglerComplet(masterEntity).signum()>0){
					taRAvoir.setAffectation(taFactureService.calculResteAReglerComplet(masterEntity));
				}
				
				taFactureService.addRAvoir(masterEntity,taRAvoir);
				masterEntity=taFactureService.affecteReglementFacture(masterEntity,typePaiementDefaut);
				masterEntity=taFactureService.calculRegleDocument(masterEntity);
				return taRAvoir;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	return retour;
	}
	public void actModifier(ActionEvent actionEvent){
		try {
			masterEntity= taFactureService.findByIDFetch(masterEntityDTO.getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
		nouveau = selection;
		if(selection.getTypeDocument().equals(TaAvoir.TYPE_DOC))
			taRAvoir=rechercheDansDocumentAvoir(selection);
		else
			taRReglement=rechercheDansDocument(selection);
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(ActionEvent actionEvent){
		TaReglement taReglement = new TaReglement();
		boolean trouveAutreFacture=false;
		try {
			if(selection!=null && selection.getDto().getId()!=null){
				taReglement = taReglementService.findById(selection.getDto().getId());
			}
			if(verifSiEstModifiable(taReglement)) {
				if(selection.getDto().getDateExport()==null){
					if(selection.getTypeDocument().equals(TaReglement.TYPE_DOC) && selection.getDto().getCodeAcompte()==null){//on ne peut pas supprimer les avoirs à partir de cet écran !!!
						if(selection!=null && selection.getDto().getId()!=null){
							taReglement = taReglementService.findById(selection.getDto().getId());
						}
						for (TaRReglement ligne : taReglement.getTaRReglements()) {
							if(!trouveAutreFacture)
								trouveAutreFacture=ligne.getTaFacture()!=null && !ligne.getTaFacture().getCodeDocument().equals(masterEntityDTO.getCodeDocument());
						}
						if(!trouveAutreFacture){
							taReglementService.remove(taReglement);

							values.remove(selection);
							if(!values.isEmpty()) {
								selection = values.get(0);
							}else {
								selection=new TaReglementDTOJSF();
							}

							refresh();
						}else{
							PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Règlement", "Ce règlement est associé à d'autres factures."
									+ "\r\nVous ne pouvez pas le supprimer à partir de cet écran."));
						}
					}
				}else{
					PrimeFaces.current().ajax().addCallbackParam("Règlement déjà exporté", true);
					PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Règlement", "Ce règlement est déjà exporté,"
							+ "\r\nVous ne pouvez pas le supprimer à partir de cet écran."));
				}

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			}
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Etat document", "actSupprimer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Etat document", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void actAffecterAuto(ActionEvent actionEvent){
		actModifier(actionEvent);
		if(selection.getTypeDocument().equals(TaAvoir.TYPE_DOC)){
			taRAvoir.setAffectation(avoir.getNetTtcCalc());
			validateUIField(Const.C_MONTANT_AFFECTE, taRAvoir.getAffectation());
		}
		if(selection.getTypeDocument().equals(TaReglement.TYPE_DOC)){
			taRReglement.setAffectation(taRReglement.getTaReglement().getNetTtcCalc());
			validateUIField(Const.C_MONTANT_AFFECTE, taRReglement.getAffectation());
		}
		actEnregistrer(actionEvent);
	}
	
	public void actSupprimerAffectation(ActionEvent actionEvent){
		Verrouillage FactureModifiable = null;
		Verrouillage avoirModifiable = null;
		Verrouillage reglementModifiable = null;
		try {
			try {
				masterEntity= taFactureService.findById(masterEntityDTO.getId());
				FactureModifiable=documentEstVerrouille(masterEntity);
			} catch (Exception e) {
				// TODO: handle exception
			}


			if(selection.getTypeDocument().equals(TaAvoir.TYPE_DOC)){				
				if(selection!=null && selection.getDto().getId()!=null){
					taRAvoir=rechercheDansDocumentAvoir(selection);
				}
				avoirModifiable=documentEstVerrouille(taRAvoir.getTaAvoir());
				if((FactureModifiable.isVerrouille() || avoirModifiable.isVerrouille()) && taRAvoir.getEtat()==IHMEtat.integre) {
					PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Affectation", "Cette affectation est verrouillée."
							+ "\r\nVous ne pouvez pas la modifier ou la supprimer."));
				}else {
					avoir.removeRAvoir(taRAvoir);
					avoir=taAvoirService.enregistrerMerge(avoir);
				}
			}else
				if(selection.getTypeDocument().equals(TaReglement.TYPE_DOC)){
					if(selection!=null && selection.getDto().getId()!=null){
						taRReglement=rechercheDansDocument(selection);
					}
					reglementModifiable=documentEstVerrouille(taRReglement.getTaReglement());
					if((FactureModifiable.isVerrouille() || reglementModifiable.isVerrouille()) && taRReglement.getEtat()==IHMEtat.integre) {
						PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Affectation", "Cette affectation est verrouillée."
								+ "\r\nVous ne pouvez pas la modifier ou la supprimer."));
					}else {					
						reglement.removeReglement(taRReglement);
						reglement=taReglementService.enregistrerMerge(reglement);
					}
				}
			values.remove(selection);
			if(!values.isEmpty()) {
				selection = values.get(0);
			}else {
				selection=new TaReglementDTOJSF();
			}
			refresh();


			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Etat document", "actSupprimerAffectation"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Etat document", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public TaRReglement enregistreTaReglement( TaRReglement taRReglement) throws Exception{	
		
		TaReglement reglement =taRReglement.getTaReglement();

		taRReglement.getTaReglement().removeReglement(taRReglement);

		taRReglement.setTaReglement(taReglementService.enregistrerMerge(taRReglement.getTaReglement()));

		reglement.addRReglement(taRReglement);
		taRReglement.setTaFacture(masterEntity);
		return taRReglement;
	}
	
	public void supprimeTaRReglement(TaRReglement taRReglement) throws Exception{		
		supprimeTaRReglement(taRReglement,true);
	}
	public void supprimeTaRReglement(TaRReglement taRReglement,Boolean commit) throws Exception{		
		taFactureService.removeReglement(masterEntity,taRReglement);
		taRReglementService.supprimer(taRReglement);
		taRReglement.setAffectation(BigDecimal.valueOf(0));
		taRReglement.getTaReglement().removeReglement(taRReglement);
		taRReglement.setTaFacture(null);
		taRReglement.setTaReglement(null);
		taRReglement=null;

	}
	
	public void supprimeTaRAcompte(TaRAcompte taRAcompte) throws Exception {
//		masterEntity.removeRAcompte(taRAcompte);
//		taRAcompteService.supprimer(taRAcompte);
//		taRAcompte.setAffectation(BigDecimal.valueOf(0));
//		taRAcompte.getTaAcompte().setResteARegler(
//				taRAcompte.getTaAcompte().calculResteARegler());
//		taRAcompte.getTaAcompte().removeRAcompte(taRAcompte);
//		taRAcompte.setTaFacture(null);
//		taRAcompte.setTaAcompte(null);
//		taRAcompte = null;
	}
	public void supprimeTaRAvoir(TaRAvoir taRAvoir) throws Exception {
//		masterEntity.removeRAvoir(taRAvoir);
//		taRAvoirService.supprimer(taRAvoir);
//		taRAvoir.setAffectation(BigDecimal.valueOf(0));
//		taRAvoir.getTaAvoir().setResteAReglerComplet(
//				taRAvoir.getTaAvoir().calculResteARegler());
//		taRAvoir.getTaAvoir().removeRAvoir(taRAvoir);
//		taRAvoir.getTaFacture().removeRAvoir(taRAvoir);
//		taRAvoir.setTaFacture(null);
//		taRAvoir.setTaAvoir(null);
//		taRAvoir = null;
	}
	
	public void updateUIDTO() {
		try {
			if(nouveau.getTypeDocument().equals(TaAvoir.TYPE_DOC)){
				//si il s'agit d'un avoir
				taRAvoir.setAffectation(nouveau.getDto().getAffectation());
				avoir=taRAvoir.getTaAvoir();
			}else{
				taRReglement.getTaReglement().setLibelleDocument(nouveau.getDto().getLibelleDocument());
				if(taRReglement.getTaReglement().getLibelleDocument()==null || taRReglement.getTaReglement().getLibelleDocument().isEmpty())
					taRReglement.getTaReglement().setLibelleDocument("paiement");
				taRReglement.getTaReglement().setDateDocument(nouveau.getDto().getDateDocument());
				taRReglement.getTaReglement().setDateLivDocument(nouveau.getDto().getDateLivDocument());
				taRReglement.getTaReglement().setNetTtcCalc(nouveau.getDto().getNetTtcCalc());
				if(nouveau.getTaTPaiement()!=null){
					taRReglement.getTaReglement().setTaTPaiement(nouveau.getTaTPaiement());

//					if(nouveau.getTaCompteBanque()==null){
//						TaCompteBanque compte=null;
//
//						compte=taCompteBanqueService.findByTiersEntreprise(taTPaiementService.findByCode(nouveau.getTaTPaiement().getCodeTPaiement()));
//
//						taRReglement.getTaReglement().setTaCompteBanque(compte);
//					}
				}
				taRReglement.setAffectation(nouveau.getDto().getAffectation());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			TaRReglementDTO retour = null;
			updateUIDTO();	
			int etat=0;
//			if(!masterEntity.getMiseADispo()&& masterEntity.getDateVerrouillage()==null)etat=IHMEtat.integre;
			if(!masterEntity.getMiseADispo())etat=IHMEtat.integre;
			
			if(nouveau.getTypeDocument().equals(TaAvoir.TYPE_DOC)){
				taRAvoir.setEtat(taRAvoir.getEtat()|etat);
				if(taRAvoir.getAffectation().compareTo(BigDecimal.ZERO)==0){
					avoir.removeRAvoir(taRAvoir);
				}				
				avoir=taAvoirService.enregistrerMerge(avoir);

			}else{
				taRReglement.setEtat(taRReglement.getEtat()|etat);

						taReglementService.annuleCode(nouveau.getDto().getCodeDocument());
					if(taRReglement.getAffectation().compareTo(BigDecimal.ZERO)==0){
						TaReglement reglement =taRReglement.getTaReglement();
						reglement.removeReglement(taRReglement);
						taRReglement.setTaReglement(null);
						taRReglement.setTaFacture(null);
						reglement=taReglementService.enregistrerMerge(reglement);
					}
					else{
//						if(taRReglement.getTaReglement()!=null && taRReglement.getTaReglement().getIdDocument()==0)
//						taRReglement=enregistreTaReglement(taRReglement);
						TaReglement reglement =taRReglement.getTaReglement();
						reglement.addRReglement(taRReglement);
						taRReglement.setTaFacture(masterEntity);
						reglement=taReglementService.enregistrerMerge(reglement);
					}
			}
			

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			old=nouveau;
			refresh();
			if(old!=null){
				selection=recherche(old);
			}
			nouveau=selection;
//			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
//				PrimeFaces.current().dialog().closeDynamic(retour);
//			}
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Règlement multiple", e.getMessage()));
		}
	}
	
	public TaReglementDTOJSF recherche(TaReglementDTOJSF courant){
		for (TaReglementDTOJSF taRReglementDTOJSF : values) {
			if(courant.getDto().getCodeDocument().equals(taRReglementDTOJSF.getDto().getCodeDocument()))
				return taRReglementDTOJSF;
		}
		return courant;
	}
	
	public void actAnnuler(ActionEvent actionEvent){
		taReglementService.annuleCode(nouveau.getDto().getCodeDocument());
		TaReglementDTOJSF old=nouveau;
		refresh();
		if(old!=null && old.getDto().getId()!=0)
			selection=recherche(old);
		nouveau=selection;
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}



	public TaReglementDTOJSF getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaReglementDTOJSF newTaRReglement) {
		this.nouveau = newTaRReglement;
	}

	public TaReglementDTOJSF getSelection() {
		return selection;
	}

	public void setSelection(TaReglementDTOJSF selectedTaRReglement) {
		this.selection = selectedTaRReglement;
	}

	public void onRowSelectExported(SelectEvent event) {  

	}

	public void onRowSelect(SelectEvent event) { 
		avoir=null;
		reglement=null;
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}



	public void refresh(){
		try {
			if(masterEntityDTO!=null){
				masterEntity=taFactureService.findById(masterEntityDTO.getId());
				masterEntityDTO=new LgrDozerMapper<TaFacture,TaFactureDTO>().map(masterEntity, TaFactureDTO.class);
				
				valuesAffecte.clear();
				valuesExported.clear();
				values.clear();

				modelReglement.remplirListeReglementsNonTotalementRegles(masterEntityDTO, dateDeb, dateFin);
				for (TaReglementDTOJSF taRReglementDTOJSF : modelReglement.getListeObjet()) {
					taRReglementDTOJSF.setTypeDocument(TaReglement.TYPE_DOC);
					if(taRReglementDTOJSF.getDto().getAffectation().signum()!=0)
						valuesAffecte.add(taRReglementDTOJSF);
					else	
					values.add(taRReglementDTOJSF);
				}

				modelReglement.remplirListeAvoirsNonTotalementRegles(masterEntityDTO, dateDeb, dateFin);
				for (TaReglementDTOJSF taRReglementDTOJSF : modelReglement.getListeObjet()) {
					taRReglementDTOJSF.setTypeDocument(TaAvoir.TYPE_DOC);
					if(taRReglementDTOJSF.getDto().getAffectation().signum()!=0)
						valuesAffecte.add(taRReglementDTOJSF);
					else
						values.add(taRReglementDTOJSF);
				}
				modelReglement.remplirListeAvoirs(masterEntityDTO, dateDeb, dateFin);
				for (TaReglementDTOJSF taRReglementDTOJSF : modelReglement.getListeObjet()) {
					taRReglementDTOJSF.setTypeDocument(TaAvoir.TYPE_DOC);
					if(taRReglementDTOJSF.getDto().getAffectation().signum()!=0)
						valuesAffecte.add(taRReglementDTOJSF);
					else
						values.add(taRReglementDTOJSF);
				}
				modelReglement.remplirListeReglements(masterEntityDTO, dateDeb, dateFin);
				for (TaReglementDTOJSF taRReglementDTOJSF : modelReglement.getListeObjet()) {
					taRReglementDTOJSF.setTypeDocument(TaReglement.TYPE_DOC);
					if(taRReglementDTOJSF.getDto().getAffectation().signum()!=0)
						valuesAffecte.add(taRReglementDTOJSF);
					else
						values.add(taRReglementDTOJSF);
				}
				for (TaReglementDTOJSF taReglementDTOJSF : valuesAffecte) {
					values.add(0, taReglementDTOJSF);
				}
				
				if(values!=null && !values.isEmpty())selection=values.get(0);

				nouveau=selection;
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean readOnlyReglement(TaReglementDTOJSF obj){
		if(obj!=null && obj.getDto()!=null){
		return(obj.getDto().getDateExport()!=null || obj.getDto().getMulti() || (obj.getTypeDocument()==null 
				|| !obj.getTypeDocument().equals(TaReglement.TYPE_DOC)) || (obj.getDto().getCodeAcompte()!=null && !obj.getDto().getCodeAcompte().isEmpty()));
	}
		return true;
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

	// Dima - Début
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaReglementDTO>> violations = factory.getValidator().validateValue(TaReglementDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaReglementDTO> cv : violations) {
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
			if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
				if(value!=null && value instanceof TaTPaiement){
					TaTPaiement obj=(TaTPaiement)value;
					changement=!nouveau.getDto().getCodeTPaiement().equals(obj.getCodeTPaiement());
					if(changement){
						//remplir libellé paiement
						nouveau.getDto().setLibelleDocument(obj.getLibTPaiement());
						//remplir le compte bancaire en fonction du type de paiement
						TaCompteBanque compte=null;
						if(obj.getCodeTPaiement()!=null){
							compte=taCompteBanqueService.findByTiersEntreprise(taTPaiementService.findByCode(obj.getCodeTPaiement()));
						}
						if(compte!=null)validateUIField(Const.C_COMPTE, compte.getCompte());
						taRReglement.getTaReglement().setTaCompteBanque(compte);
						nouveau.getDto().setIban(compte.getIban());
					}
				}
			}
			if(nomChamp.equals(Const.C_COMPTE_BANQUE)) {
				if(value!=null && value instanceof TaCompteBanque){
					TaCompteBanque obj=(TaCompteBanque)value;
					changement=nouveau.getDto().getIban()==null ||!nouveau.getDto().getIban().equals(obj.getIban());
					if(changement){
						taRReglement.getTaReglement().setTaCompteBanque(obj);
						nouveau.setTaCompteBanque(obj);
						nouveau.getDto().setIban(obj.getIban());
					}
				}else{
					taRReglement.getTaReglement().setTaCompteBanque(null);
					nouveau.setTaCompteBanque(null);
					nouveau.getDto().setIban(null);					
				}
			}			
			if (nomChamp.equals(Const.C_NET_TTC_CALC) ) {
				TaReglement u =new TaReglement();
				
				if(value==null)value=BigDecimal.ZERO; //le réglement ne peut pas être null
				
				PropertyUtils.setSimpleProperty(taRReglement.getTaReglement(), nomChamp, value);
					if(taRReglement.getAffectation().compareTo(taRReglement.getTaReglement().getNetTtcCalc())>0){
						taRReglement.setAffectation(taRReglement.getTaReglement().getNetTtcCalc());
						masterEntity.calculRegleDocument();
						nouveau.getDto().setAffectation(taRReglement.getAffectation());
					}
				
			}
			if(nomChamp.equals(Const.C_DATE_REGLEMENT)) {
				if(value!=null) {
					if(value instanceof Date) {
						if(((Date) value).after(taRReglement.getTaReglement().getDateLivDocument())) {
							nouveau.getDto().setDateLivDocument((Date) value);
						}
					}
				}
			}
			if(nomChamp.equals(Const.C_DATE_ENCAISSEMENT)) {
				if(value!=null) {
					if(value instanceof Date) {
						if(((Date) value).before(taRReglement.getTaReglement().getDateDocument())) {
							nouveau.getDto().setDateLivDocument(nouveau.getDto().getDateDocument());
						}
					}
				}
			}
			if(nomChamp.equals(Const.C_MONTANT_AFFECTE)) {
				if(selection.getTypeDocument().equals(TaReglement.TYPE_DOC)){
					if(value!=null && value instanceof BigDecimal){
						taRReglement.setAffectation((BigDecimal) value);
					}
					//si affectation supérieure au reste à regler de la facture
					if(masterEntity.getRegleCompletDocument(taRReglement).add(taRReglement.getAffectation()).
							compareTo(masterEntity.getNetAPayer())>0){
						taRReglement.setAffectation(masterEntity.getNetAPayer().subtract(masterEntity.getRegleCompletDocument(taRReglement)));
					}
					//si affectation supérieure au reste à regler du règlement
					if(taRReglement.getTaReglement().calculAffectationTotale(taRReglement).add(taRReglement.getAffectation()).
							compareTo(taRReglement.getTaReglement().getNetTtcCalc())>0){
						taRReglement.setAffectation(taRReglement.getTaReglement().getNetTtcCalc().subtract(taRReglement.getTaReglement().calculAffectationTotale(taRReglement)));
					}
					if(taRReglement.getTaReglement().getNetTtcCalc()==BigDecimal.valueOf(0))
						taRReglement.getTaReglement().setNetTtcCalc(taRReglement.getAffectation());
					if(taRReglement.getAffectation().compareTo(taRReglement.getTaReglement().getNetTtcCalc())>0)
						taRReglement.setAffectation(taRReglement.getTaReglement().getNetTtcCalc());
					BigDecimal valeurTemp = masterEntity.getNetAPayer().subtract(masterEntity.getAcomptes()).subtract(masterEntity.getAvoirs());
					if(valeurTemp.signum()<0 )
						valeurTemp=valeurTemp.valueOf(0);
					if(valeurTemp.compareTo(taRReglement.getAffectation())<0)
						taRReglement.setAffectation(valeurTemp);
					BigDecimal dif=masterEntity.getNetTtcCalc().
							subtract(masterEntity.calculRegleDocumentComplet());
					if(dif.signum()<0)
						taRReglement.setAffectation(taRReglement.getAffectation().
								subtract(dif.abs()));
					nouveau.getDto().setAffectation(taRReglement.getAffectation());
				}
				if(selection.getTypeDocument().equals(TaAvoir.TYPE_DOC)){
					if(value!=null && value instanceof BigDecimal){
						taRAvoir.setAffectation((BigDecimal) value);
					}
					//si affectation supérieure au reste à regler de la facture
					if(masterEntity.getRegleCompletDocument(taRAvoir).add(taRAvoir.getAffectation()).
							compareTo(masterEntity.getNetAPayer())>0){
						taRAvoir.setAffectation(masterEntity.getNetAPayer().subtract(masterEntity.getRegleCompletDocument(taRAvoir)));
					}
					//si affectation supérieure au reste à regler du règlement
					if(taRAvoir.getTaAvoir().calculAffectationTotale(taRAvoir).add(taRAvoir.getAffectation()).
							compareTo(taRAvoir.getTaAvoir().getNetTtcCalc())>0){
						taRAvoir.setAffectation(taRAvoir.getTaAvoir().getNetTtcCalc().subtract(taRAvoir.getTaAvoir().calculAffectationTotale(taRAvoir)));
					}
					if(taRAvoir.getTaAvoir().getNetTtcCalc()==BigDecimal.valueOf(0))
						taRAvoir.getTaAvoir().setNetTtcCalc(taRAvoir.getAffectation());
					if(taRAvoir.getAffectation().compareTo(taRAvoir.getTaAvoir().getNetTtcCalc())>0)
						taRAvoir.setAffectation(taRAvoir.getTaAvoir().getNetTtcCalc());
					BigDecimal valeurTemp = masterEntity.getNetAPayer().subtract(masterEntity.getAcomptes()).subtract(masterEntity.getAvoirs());
					if(valeurTemp.signum()<0 )
						valeurTemp=valeurTemp.valueOf(0);
					if(valeurTemp.compareTo(taRAvoir.getAffectation())<0)
						taRAvoir.setAffectation(valeurTemp);
					BigDecimal dif=masterEntity.getNetTtcCalc().
							subtract(masterEntity.calculRegleDocumentComplet());
					if(dif.signum()<0)
						taRAvoir.setAffectation(taRAvoir.getAffectation().
								subtract(dif.abs()));
					nouveau.getDto().setAffectation(taRAvoir.getAffectation());
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<TaReglementDTOJSF> getValuesExported() {
		return valuesExported;
	}

	public void setValuesExported(List<TaReglementDTOJSF> valuesExported) {
		this.valuesExported = valuesExported;
	}

	public List<TaReglementDTOJSF> getValues() {
		return values;
	}

	public void setValues(List<TaReglementDTOJSF> values) {
		this.values = values;
	}

	public List<TaReglementDTOJSF> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaReglementDTOJSF> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public ModelReglement getModelReglement() {
		return modelReglement;
	}

	public void setModelReglement(ModelReglement modelReglement) {
		this.modelReglement = modelReglement;
	}

	public TaFacture getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaFacture masterEntity) {
		this.masterEntity = masterEntity;
	}

	public TaReglementDTOJSF getSelectionExported() {
		return selectionExported;
	}

	public void setSelectionExported(TaReglementDTOJSF selectionExported) {
		this.selectionExported = selectionExported;
	}

	public TaRAvoir getTaRAvoir() {
		return taRAvoir;
	}

	public void setTaRAvoir(TaRAvoir taRAvoir) {
		this.taRAvoir = taRAvoir;
	}

	public TaReglement getReglement() {
		return reglement;
	}

	public void setReglement(TaReglement taReglement) {
		this.reglement = taReglement;
	}

	public String getTitlePartOne() {
		return titlePartOne;
	}

	public void setTitlePartOne(String titlePartOne) {
		this.titlePartOne = titlePartOne;
	}

	public String getTitlePartTwo() {
		return titlePartTwo;
	}

	public void setTitlePartTwo(String titlePartTwo) {
		this.titlePartTwo = titlePartTwo;
	}

	public TaFactureDTO getMasterEntityDTO() {
		return masterEntityDTO;
	}

	public void setMasterEntityDTO(TaFactureDTO masterEntityDTO) {
		this.masterEntityDTO = masterEntityDTO;
	}


	public IDocumentTiers recupCodeDocumentAcompte(String code){
		FacesContext context = FacesContext.getCurrentInstance();
		if(code!=null && !code.isEmpty())
			return documentValide(code);
		return null;
	}

	private IDocumentTiers documentValide(String code){
		try {	
			acompte=taAcompteService.findByCode(code);
			return acompte;
		} catch (FinderException e) {
			return null;
		}
	}

	public TaFactureDTO rempliDTO(){
		if(masterEntity!=null){			
			try {
				masterEntity=taFactureService.findByCode(masterEntity.getCodeDocument());
				masterEntityDTO= new LgrDozerMapper<TaFacture, TaFactureDTO>().map(masterEntity, TaFactureDTO.class);
				if(values.contains(selection))values.add(selection);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return masterEntityDTO;
	}



	
	
	public Verrouillage documentEstVerrouille(IDocumentTiers courant) {
		if(courant!=null ) {
			return Verrouillage.determineVerrouillage(courant.getTaMiseADisposition(), courant.getDateExport(), courant.getDateVerrouillage());
		}
		return null;
	}
	public Verrouillage documentEstVerrouille() {
		return documentEstVerrouille(reglement);
	}

	public Verrouillage masterEntityDTOEstVerrouille() {
		return masterEntityDTOEstVerrouille(masterEntityDTO);
	}
	public Verrouillage masterEntityDTOEstVerrouille(TaFactureDTO courant) {
		if(courant!=null ) {
			return Verrouillage.determineVerrouillage(courant.getEstMisADisposition(), courant.getDateExport(), courant.getDateVerrouillage());
		}
		return null;
	}
	
	public Verrouillage documentDTOEstVerrouille() {
		return documentDTOEstVerrouille(selection);
	}
	public Verrouillage documentDTOEstVerrouille(TaReglementDTOJSF courant) {
		if(courant!=null ) {
			return Verrouillage.determineVerrouillage(courant.getDto().getEstMisADisposition(), courant.getDto().getDateExport(), courant.getDto().getDateVerrouillage());
		}
		return null;
	}
	
	public Verrouillage rAffectationDTOEstVerrouille() {
		return rAffectationDTOEstVerrouille(selection);
	}
	public Verrouillage rAffectationDTOEstVerrouille(TaReglementDTOJSF courant) {
		if(courant!=null ) {
//			return Verrouillage.determineVerrouillage(courant.getDto().getEstMisADisposition(), courant.getDto().getDateExport(), courant.getDto().getDateVerrouillage());
			return Verrouillage.determineVerrouillageAffectation(courant.getDto().getEstMisADispositionAffectation(),courant.getDto().getDateExportAffectation(), courant.getDto().getDateVerrouillageAffectation());
		}
		return null;
	}	
	
	
	protected boolean verifSiEstModifiable() {
		return verifSiEstModifiable(reglement);
	}
	protected boolean verifSiEstModifiable(TaReglement courant) {
		Verrouillage estVerrouille=documentEstVerrouille(courant);
		if(!estVerrouille.isVerrouille())return true;
		PrimeFaces.current().ajax().addCallbackParam("verifSiEstModifiable", estVerrouille);
		if(estVerrouille.isVerrouille())
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Document",estVerrouille.getMessage() ));
		return false;
	}

	public List<TaReglementDTOJSF> getValuesAffecte() {
		return valuesAffecte;
	}

	public void setValuesAffecte(List<TaReglementDTOJSF> valuesAffecte) {
		this.valuesAffecte = valuesAffecte;
	}

	public void controleDate(SelectEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp, event.getObject());
		
	}
	public void controleDate(AjaxBehaviorEvent event) {
		String nomChamp =  (String)event.getComponent().getAttributes().get("nomChamp");
		validateUIField(nomChamp,  ((UIOutput) event.getSource()).getValue());
	}

	public boolean resteAAffecter(TaReglementDTO courant) {		
		return courant.getResteAAffecter().signum()>0;
	}
}
