package fr.legrain.bdg.webapp.reglementMultiple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.documents.Verrouillage;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.IHMEtat;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class ReglementController extends AbstractController{

	@Inject @Named(value="tabListModelCenterBean")
	protected TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	protected TabViewsBean tabViews;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;
	
	private List<TaReglementDTOJSF> values=new LinkedList<>();
	private List<TaReglementDTOJSF> filteredValues=new LinkedList<>();
	private List<TaRReglementDTOJSF> valuesLignes=new LinkedList<>();
	private List<TaRReglementDTOJSF> valuesLignesARegler=new LinkedList<>();
	private List<TaRReglementDTOJSF> filteredValuesLignes=new LinkedList<>();
	
	private TaReglementDTOJSF old ;
//	private TaReglementDTOJSF nouveau ;
	private TaReglementDTOJSF selection ;
	
	private TaRReglementDTOJSF nouvelleLigne ;
	private TaRReglementDTOJSF selectionLigne ;
	private TaRReglementDTOJSF selectionLigneARegler ;
	private TaRReglementDTOJSF[] selectionLignes ;
	
	private List<TaFactureDTO> listeDocumentDTO;
	private TaReglement taReglement;
	private TaTPaiement taTPaiment;
//	private TaCompteBanque taCompteBanque;
	private TaCompteBanque taCompteBanqueFinal;
	private TaTiersDTO taTiersDTO;
	private TaTiers tiersCourant;
	private TaFacture taFacture;
	protected String typePaiementDefaut=null;
	private Date dateDeb=null;
	private Date dateFin=null;	

	TaInfoEntreprise infos =null;
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	private String titlePartOne="Gestion des remises";
	private String titlePartTwo="";
	private int nbMaxLigne=20;
	
	private boolean preInsertion=false;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private Boolean afficheCritere=false;
	
	private String titreEtape1="Etape 1/2 : Saisissez les critères de sélection des factures";
	private String titreEtape2="Etape 2/2 : Sélectionnez les factures à intégrer au règlement dans la liste";
	private String titreEtape3="Détail du règlement n° : ";
	private String titreEcran=titreEtape2;
	
	Integer nbLigne=0;
	BigDecimal totalTTC=BigDecimal.valueOf(0);
	BigDecimal montantReglementInitial=BigDecimal.valueOf(0);
	BigDecimal resteAAffecter=BigDecimal.valueOf(0);
	
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaRReglementServiceRemote taRReglementService;
	private @EJB  ITaInfoEntrepriseServiceRemote TaInfoEntrepriseService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected @EJB ITaTPaiementServiceRemote taTPaiementService;
	protected @EJB ITaTiersServiceRemote taTiersService;
	protected @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
	private @EJB ITaFactureServiceRemote taFactureService;
	
	private String raisonSocialeTiers = null; 
	private String nomTiers = null; 
	private String prenomTiers = null; 
	private String telephoneTiers = null; 
	private String emailTiers = null; 
	private String villeTiers = null; 
	private String cpTiers = null; 
	private String codeTiers = null; 
	private BigDecimal montantTotalTTC=BigDecimal.valueOf(0);
	private BigDecimal montantTotalHT=BigDecimal.valueOf(0);

	
	private LgrDozerMapper<TaReglementDTO,TaReglement> mapperUIToModel  = new LgrDozerMapper<TaReglementDTO,TaReglement>();
	private LgrDozerMapper<TaReglement,TaReglementDTO> mapperModelToUI  = new LgrDozerMapper<TaReglement,TaReglementDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {

			infos=TaInfoEntrepriseService.findInstance();

			dateDeb=infos.getDatedebInfoEntreprise();
			dateFin=infos.getDatefinInfoEntreprise();
			
			typePaiementDefaut = taPreferencesService.findByCode(TaTPaiement.getCodeTPaiementDefaut()).getValeur();
			if (typePaiementDefaut == null || typePaiementDefaut=="")
				typePaiementDefaut="C";
			taTPaiment=taTPaiementService.findByCode(typePaiementDefaut);
			
			if(taTPaiment!=null){
				selectionTPaiement();
			}
			
			refresh();


		} catch (Exception e) {
			e.printStackTrace();
		}
//		this.setFilteredValues(values);
	}
	
	public ReglementController() {  
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
	
	public List<TaTiersDTO> tiersAutoComplete(String query) {
		List<TaTiersDTO> allValues = taTiersService.findAllLight();
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();
		TaTiersDTO cp = new TaTiersDTO();
		cp.setCodeTiers(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			cp.setNomEntreprise(LibChaine.lgrStringNonNull(cp.getNomEntreprise()));
			if(query.equals("*") || cp.getCodeTiers().toLowerCase().contains(query.toLowerCase())|| cp.getNomEntreprise().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
			
		}
		return filteredValues;
	}
	
	public List<TaCompteBanque> compteBancaireAutoComplete(String query) {
		List<TaCompteBanque> allValues = taCompteBanqueService.selectCompteEntreprise();
		List<TaCompteBanque> filteredValues = new ArrayList<TaCompteBanque>();
		TaCompteBanque cp = new TaCompteBanque();
		cp.setCompte(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCompte().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	
	public List<TaCompteBanque> compteBancaireAutoCompleteFinal(String query) {
		List<TaCompteBanque> allValues = taCompteBanqueService.selectCompteEntreprise();
		List<TaCompteBanque> filteredValues = new ArrayList<TaCompteBanque>();
		TaCompteBanque cp = new TaCompteBanque();
		cp.setCompte(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCompte().toLowerCase().contains(query.toLowerCase())) {
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
		taCompteBanqueFinal=taCompteBanqueService.findByTiersEntreprise(taTPaiment);
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actInitInserer(ActionEvent actionEvent){
		if(selection!=null && selection.getDto()!=null){
			taReglementService.annuleCode(selection.getDto().getCodeDocument());
		}
		taTiersDTO=null;
		afficheCritere=true;	
		afficheTitre();
		selection=new TaReglementDTOJSF();
		taCompteBanqueFinal=null;
		valuesLignes.clear();
		selectionLignes=rempliSelection(valuesLignes);
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		initTotaux();
		preInsertion=true;
	}
	
	
	public void actInserer(ActionEvent actionEvent){
		try {

			if(selection!=null && selection.getDto()!=null){
				taReglementService.annuleCode(selection.getDto().getCodeDocument());
			}		
		valuesLignes.clear();
		taReglement=new TaReglement();
		
		//créer une nouvelle remise
		selection=new TaReglementDTOJSF();
		selection.setDto(new TaReglementDTO());
		selection.getDto().setCodeDocument(taReglementService.genereCode(null));

		selection.getDto().setDateDocument(TaInfoEntrepriseService.dateDansExercice(new Date()));
		selection.getDto().setDateLivDocument(selection.getDto().getDateDocument());

		selection.getDto().setLibelleDocument("Réglement n° : "+selection.getDto().getCodeDocument());
		
		if(taTiersDTO!=null)selection.getDto().setCodeTiers(taTiersDTO.getCodeTiers());
		
		if(taTPaiment!=null){
			selection.getDto().setCodeTPaiement(taTPaiment.getCodeTPaiement());
			if(taCompteBanqueFinal==null){
				taCompteBanqueFinal=taCompteBanqueService.findByTiersEntreprise(taTPaiment);
			}
		}
		if(taCompteBanqueFinal!=null){
			selection.getDto().setIban(taCompteBanqueFinal.getCompte());		
		}
		
		selection=selection;
		String codeTPaiement = "%";
		String iban =null;
		if(taTPaiment!=null){
			codeTPaiement=taTPaiment.getCodeTPaiement();
		}
		if(taCompteBanqueFinal!=null && taCompteBanqueFinal.getIban()!=null){
			iban=taCompteBanqueFinal.getIban();
		}

		//insérer les lignes de réglements qui peuvent rentrer dans la remise
		rajouteFactureNonTotalementReglees();
		
		initTotaux();
		if(valuesLignes!=null && !valuesLignes.isEmpty())selectionLigne=valuesLignes.get(0);		
		nouvelleLigne=selectionLigne;
		
		selectionLignes=rempliSelection(valuesLignes);
		afficheCritere=true;	
		afficheTitre();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		selection.getDto().setNetTtcCalc(BigDecimal.ZERO);

		selection.getDto().setNetTtcCalc(montantReglementInitial);
		preInsertion=false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TaRReglementDTOJSF[] rempliSelection(List<TaRReglementDTOJSF> liste){
		List<TaRReglementDTOJSF> tmp = new LinkedList<>();
		for (TaRReglementDTOJSF obj : liste) {
			if(obj.getDto().getAccepte())tmp.add(obj);
		}
		return selectionLignes= Arrays.copyOf(tmp.toArray(),tmp.size(),TaRReglementDTOJSF[].class);		
	}
	
	private void rajouteFactureNonTotalementReglees(){
		String codeTiers = null;
		if(taTiersDTO!=null)codeTiers=taTiersDTO.getCodeTiers();
		//rechercher toutes les factures non totalement règlées
		listeDocumentDTO= taFactureService.rechercheDocumentNonTotalementRegle(null,
				null,codeTiers,"%");
		for (TaFactureDTO ligne : listeDocumentDTO) {
			TaRReglementDTOJSF rr =new TaRReglementDTOJSF();
			rr.setDto(new TaRReglementDTO());
			rr.getDto().setAccepte(false);
			rr.getDto().setCodeFacture(ligne.getCodeDocument());
			rr.getDto().setCodeTiers(ligne.getCodeTiers());
			//			rr.getDto().setCodeReglement(taReglement.getCodeDocument());
			rr.getDto().setDateFacture(ligne.getDateDocument());
			rr.getDto().setDateExport(null);
			rr.getDto().setIdFacture(ligne.getId());
			rr.getDto().setIdTiers(ligne.getIdTiers());
			rr.getDto().setLibelleFacture(ligne.getLibelleDocument());
			rr.getDto().setNetTtcCalc(ligne.getNetTtcCalc());
			rr.getDto().setSommeAvoir(ligne.getAvoirs());
			rr.getDto().setSommeReglement(ligne.getMontantReglement());
			rr.getDto().setResteAReglerFacture(ligne.getResteAReglerComplet());
			if(recherche(rr)==null)	valuesLignes.add(rr);
		}
	}
	
	public void actModifier(ActionEvent actionEvent){
		try {
			if(modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION)){
				taReglement=taReglementService.findById(selection.getDto().getIdDocument());					
//					if(verifSiEstModifiable()) {
						rajouteFactureNonTotalementReglees();
						modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//					}				
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actSupprimer(ActionEvent actionEvent){
		try{
			if(selection!=null){
				taReglement=taReglementService.findById(selection.getDto().getIdDocument());
				if(verifSiEstModifiable(taReglement)) {
					if(selection.getDto().getDateExport()==null){				
						taReglementService.remove(taReglement);
					}
					else{
						PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Remise", "Cette remise est exportée."
								+ "\r\nVous ne pouvez pas la supprimer."));
					}

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
					taReglementService.annuleCode(selection.getDto().getCodeDocument());
					selection=null;
					old=null;
					refresh();
				}
			}
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("remise", "actSupprimer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "remise", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}
	
	public void actSupprimer(){
		actSupprimer(null);
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("reglement", "actImprimer")); 
		}
		try {

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaReglement doc =taReglementService.findById(selection.getDto().getIdDocument());
			doc.setTaTPaiement(doc.getTaTPaiement());

			mapEdition.put("doc",doc );

			mapEdition.put("taInfoEntreprise", TaInfoEntrepriseService.findInstance());


//			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
//			List<TaPreferences> liste= taPreferencesService.findByGroupe("remise");
//			mapParametreEdition.put("preferences", liste);
//			mapEdition.put("param", mapParametreEdition);

			sessionMap.put("edition", mapEdition);

			System.out.println("RemiseController.actImprimer()");
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void actImprimerListeDesReglements(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesReglements", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public void actSupprimerLigne(){
		try{
			actModifier(null);
			if(selectionLigne!=null){
			
				
				
				TaRReglement rr= rechercheRReglement(selectionLigne);
				if( verifSiAffectationEstModifiable(rr)) {
					selectionLigne.getDto().setAffectationCourante(selectionLigne.getDto().getAffectation());
					validateUIField(Const.C_MONTANT_AFFECTE, BigDecimal.ZERO);
					selectionLigne.getDto().setAffectation(BigDecimal.ZERO);
					selectionLigne.getDto().setAffectationCourante(BigDecimal.ZERO);
					initTotaux();
				}
			}

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("remise", "actSupprimerLigne"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "remise", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}

	
	public void updateUIDTO() {
		try {


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void actEnregistrer(ActionEvent actionEvent){
		try {

			if(selection!=null && selection.getDto()!=null && (selection.getDto().getIdDocument()==null  || selection.getDto().getIdDocument()==0)){
				//si nouveau règlement
				taReglement=new TaReglement();
				//			mapperUIToModel.map(selection.getDto(), taReglement);
				taReglement.setCodeDocument(selection.getDto().getCodeDocument());
				taReglement.setLibelleDocument(selection.getDto().getLibelleDocument());
				taReglement.setDateDocument(selection.getDto().getDateDocument());
				taReglement.setDateLivDocument(selection.getDto().getDateLivDocument());
				taReglement.setNetTtcCalc(selection.getDto().getNetTtcCalc());
				
				if(taCompteBanqueFinal==null){
					taCompteBanqueFinal=taCompteBanqueService.findByTiersEntreprise(taTPaiment);
				}
				taReglement.setTaCompteBanque(taCompteBanqueFinal);
				taReglement.setTaTPaiement(taTPaiment);
				taReglement.setTaTiers(taTiersService.findById(taTiersDTO.getId()));
				if(taReglement.getDateLivDocument()==null)taReglement.setDateLivDocument(taReglement.getDateDocument());

			}else if(selection!=null && selection.getDto().getIdDocument()!=null && selection.getDto().getIdDocument()!=0){
				//si règlement existant
				taReglement=taReglementService.findById(selection.getDto().getIdDocument());
				if(!documentEstVerrouille(taReglement).isVerrouille()) {
					if(taReglement.getDateLivDocument()==null)taReglement.setDateLivDocument(taReglement.getDateDocument());
					mapperUIToModel.map(selection.getDto(), taReglement);
				}
				if(taCompteBanqueFinal==null){
					taCompteBanqueFinal=taCompteBanqueService.findByTiersEntreprise(taTPaiment);
				}
				taReglement.setTaCompteBanque(taCompteBanqueFinal);
				taReglement.setTaTPaiement(taTPaiment);
			}
			
			TaFacture master;
			for (TaRReglementDTOJSF ligne : valuesLignes) {
				int etat=0;
				selectionLigne=ligne;
				TaRReglement rr =new TaRReglement();
				if(ligne.getDto().getId()!=0){//ligne existante
					rr=rechercheRReglement(ligne);
					if(!affectationEstVerrouille(rr).isVerrouille()) {
						if(ligne.getDto().getAffectation().signum()!=0){						
							rr.setAffectation(ligne.getDto().getAffectation());
							master=rr.getTaFacture();
							if(!master.getMiseADispo())etat=IHMEtat.integre;
							rr.setEtat(etat);
						}else{
							taReglement.removeReglement(rr);
						}
					}
				}else{//nouvelle ligne
					if(ligne.getDto().getAffectation().signum()!=0){
						rr.setAffectation(ligne.getDto().getAffectation());						
						rr.setTaFacture(taFactureService.findByIdDocument((ligne.getDto().getIdFacture())));
						rr.setTaReglement(taReglement);
						taReglement.addRReglement(rr);
						master=rr.getTaFacture();
						if(!master.getMiseADispo())etat=IHMEtat.integre;
						rr.setEtat(etat);
					}
				}

			}
			
			
			old=selection;
			taReglement=taReglementService.enregistrerMerge(taReglement);
			taReglementService.annuleCode(selection.getDto().getCodeDocument());
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			refresh();
			if(old!=null){
				selection=recherche(old);
			}

		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Règlement", e.getMessage()));
		}
	}
	
	public TaRReglement rechercheRReglement(TaRReglementDTOJSF courant){
		for (TaRReglement ligne : taReglement.getTaRReglements()) {
			if(courant.getDto().getId().equals(ligne.getId()))
				return ligne;
		}
		return null;
	}
	
	public TaReglementDTOJSF recherche(TaReglementDTOJSF courant){
		for (TaReglementDTOJSF ligne : values) {
			if(courant.getDto().getCodeDocument().equals(ligne.getDto().getCodeDocument()))
				return ligne;
		}
		return courant;
	}
	
	public TaRReglementDTOJSF recherche(TaRReglementDTOJSF courant){
		for (TaRReglementDTOJSF ligne : valuesLignes) {
			if(courant.getDto().getCodeFacture().equals(ligne.getDto().getCodeFacture()))
				return ligne;
		}
		return null;
	}
	public void actAnnuler(ActionEvent actionEvent){
		if(selection!=null && selection.getDto()!=null){
			taReglementService.annuleCode(selection.getDto().getCodeDocument());

//			TaReglementDTOJSF old=selection;
//			selection=null;
			refresh();
//			if(old!=null && old.getDto()!=null && old.getDto().getIdDocument()!=null && old.getDto().getIdDocument()!=0)
//				selection=recherche(old);
//			nouveau=selection;
		}
		afficheCritere=false;
		afficheTitre();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}



//	public TaReglementDTOJSF getNouveau() {
//		return nouveau;
//	}
//
//	public void setNouveau(TaReglementDTOJSF newTaRReglement) {
//		this.nouveau = newTaRReglement;
//	}

	public TaReglementDTOJSF getSelection() {
		return selection;
	}

	public void setSelection(TaReglementDTOJSF selectedTaRReglement) {
		this.selection = selectedTaRReglement;
	}


	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_REGLEMENT);
		b.setTitre("Règlement");
		b.setTemplate("reglement/multiple/reglement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REGLEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_REGLEMENT);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		tabViews.selectionneOngletCentre(b);
		actInitInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Règlement", 
					"Nouveau"
					)); 
		}
	}
	public void afficheTitre(){
		
		titreEcran="";
		if(afficheCritere){
			titreEcran=titreEtape2;
		}else{
		if(selection!=null && selection.getDto()!=null)
			titreEcran=titreEtape3+selection.getDto().getCodeDocument();
		}
	}
	public void onRowSelect(SelectEvent event) { 
		try{
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_REGLEMENT);
		b.setTitre("Règlement");
		b.setTemplate("reglement/multiple/reglement.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REGLEMENT);
		b.setStyle(LgrTab.CSS_CLASS_TAB_REGLEMENT);
		tabsCenter.ajouterOnglet(b);;
		tabViews.selectionneOngletCentre(b);
		afficheCritere=false;
		if(filteredValues!=null && filteredValues.size()>0) {
			selection = filteredValues.get(0);
		}
		
		afficheTitre();


		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
//			nouveau = selection;
		}
		if(selection.getDto().getCodeTPaiement()!=null){
			taTPaiment= taTPaiementService.findByCode(selection.getDto().getCodeTPaiement());
		}
		if(selection.getDto().getCodeTiers()!=null){
			List<TaTiersDTO> liste= taTiersService.findByCodeLight(selection.getDto().getCodeTiers());
			if(liste!=null && !liste.isEmpty())taTiersDTO= liste.get(0);
		}
		if(selection.getDto().getIban()!=null){
			taCompteBanqueFinal= taCompteBanqueService.findByCode(selection.getDto().getIban());
		}
		refreshLigne();
		preInsertion=false;
		PrimeFaces.current().executeScript("window.scrollTo(0,0);");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void onRowSelectLigne(SelectEvent event) { 

	}
	
	public void onRowUnSelectLigne(UnselectEvent event) { 
		initTotaux();
	}

	public void refreshLigne(){
		try {
			valuesLignes.clear();
			
			if(selection!=null && selection.getDto()!=null && selection.getDto().getIdDocument()!=0){
				if(selection.getDto().getCodeTiers()!=null){
					List<TaTiersDTO> liste= taTiersService.findByCodeLight(selection.getDto().getCodeTiers());
					if(liste!=null && !liste.isEmpty())taTiersDTO= liste.get(0);
				}
				if(selection.getDto().getCodeTPaiement()!=null){
					taTPaiment= taTPaiementService.findByCode(selection.getDto().getCodeTPaiement());
				}
				if(selection.getDto().getIdCompteBanque()!=null){
					taCompteBanqueFinal= taCompteBanqueService.findById(selection.getDto().getIdCompteBanque());
				}				
				//récupérer les lignes de la remise
				List<TaRReglementDTO> liste=taRReglementService.rechercheDocumentDTO(selection.getDto().getCodeDocument(),selection.getDto().getCodeTiers());
				for (TaRReglementDTO rr : liste) {
					TaRReglementDTOJSF obj = new TaRReglementDTOJSF();
					
					obj.setDto(rr);
					valuesLignes.add(obj);
				}
			}
			if(valuesLignes!=null && !valuesLignes.isEmpty())selectionLigne=valuesLignes.get(0);
			selectionLignes=rempliSelection(valuesLignes);

			nouvelleLigne=selectionLigne;
			initTotaux();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	

	public void refresh(){
		try {
			old=selection;
		values.clear();

		//remplir values
		List<TaReglementDTO> liste= taReglementService.rechercheDocumentDTO("%");
		for (TaReglementDTO ligne : liste) {
			TaReglementDTOJSF obj = new TaReglementDTOJSF();
			obj.setDto(ligne);

			values.add(obj);
		}

		if(values!=null && !values.isEmpty())selection=values.get(0);
		if(old!=null){
			selection=recherche(old);
		}
//		nouveau=selection;
		afficheCritere=false;
		afficheTitre();
		refreshLigne();
		filteredValues.clear();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actFermer(ActionEvent actionEvent) {
		if(selection!=null && selection.getDto()!=null){
			taReglementService.annuleCode(selection.getDto().getCodeDocument());
		}
		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION))
			actAnnuler(null);
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
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
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
				retour = modeEcran.dataSetEnModif()?true:false;
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
			case "rowEditor":
				retour = modeEcran.dataSetEnModif()?true:false;
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
			if(nomChamp.equals(Const.C_COMPTE_BANQUE)) {
				if(value!=null && value instanceof TaCompteBanque){
					TaCompteBanque obj=(TaCompteBanque)value;
					taCompteBanqueFinal=obj;
				}else if(value==null){
					taCompteBanqueFinal=null;
				}
			}
			if(nomChamp.equals(Const.C_CODE_T_PAIEMENT)) {
				
				if(value!=null && value instanceof TaTPaiement){
					TaTPaiement obj=(TaTPaiement)value;
					changement=taTPaiment==null || !taTPaiment.getCodeTPaiement().equals(obj.getCodeTPaiement() );
					taTPaiment=obj;
				}else if(value==null){
					changement=taTPaiment!=null;
					taTPaiment=null;
				}
//				if(changement && taTPaiment!=null)
					selection.getDto().setLibelleDocument(taTPaiment.getLibTPaiement());
			}
			if (nomChamp.equals(Const.C_MONTANT_REGLEMENT) ) {
				
				if(value==null ||((BigDecimal) value).compareTo(totalTTC)<0){
					selection.getDto().setNetTtcCalc(totalTTC);
				}
				taReglement.setNetTtcCalc(selection.getDto().getNetTtcCalc());
				initTotaux();
			}
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				if(value!=null && value instanceof TaTiersDTO){					
					codeTiers = ((TaTiersDTO) value).getCodeTiers();
					raisonSocialeTiers = ((TaTiersDTO) value).getNomEntreprise();
					nomTiers = ((TaTiersDTO) value).getNomTiers();
					prenomTiers = ((TaTiersDTO) value).getPrenomTiers();
					telephoneTiers = ((TaTiersDTO) value).getNumeroTelephone();
					emailTiers = ((TaTiersDTO) value).getAdresseEmail();
					villeTiers = ((TaTiersDTO) value).getVilleAdresse();
					cpTiers = ((TaTiersDTO) value).getCodepostalAdresse();
					
				}
//				taTiersService.findByCodeLight(((TaTiersDTO) value).getCodeTiers());
				
				
			}
			if(nomChamp.equals(Const.C_MONTANT_AFFECTE)) {
					//on enlève l'affectation avant la modif, on rajoute la modif + les avoirs
					if(selectionLigne!=null){
						BigDecimal dejaAffecteFacture=selectionLigne.getDto().getSommeReglement().add(selectionLigne.getDto().getSommeAvoir());
						BigDecimal affectationTotaleFacture=dejaAffecteFacture.add((BigDecimal) value);
						BigDecimal resteReelFacture=selectionLigne.getDto().getNetTtcCalc().subtract(dejaAffecteFacture);
						selectionLigne.getDto().setAffectation((BigDecimal) value);
						

						TaFacture fac=taFactureService.findById(selectionLigne.getDto().getIdFacture());
						TaRReglement taRReglement = null ;
						if(selectionLigne.getDto().getId()!=null && selectionLigne.getDto().getId()!=0)
							taRReglement = taRReglementService.findById(selectionLigne.getDto().getId());
						if(taRReglement==null){
							taRReglement = new TaRReglement();
							taRReglement.setTaReglement(taReglement);
							taReglement.addRReglement(taRReglement);
						}
						//si affectation supérieure au reste à regler de la facture
						if(fac.getRegleCompletDocument(taRReglement).subtract(BigDecimal.ZERO).add(selectionLigne.getDto().getAffectation()).
								compareTo(fac.getNetAPayer())>0){
							taRReglement.setAffectation(fac.getNetAPayer().subtract(fac.getRegleCompletDocument(taRReglement)));
							selectionLigne.getDto().setAffectation(taRReglement.getAffectation());
						}
						

//						//si affectation supérieure au reste à regler de la facture
//						if(affectationTotaleFacture.compareTo(selectionLigne.getDto().getNetTtcCalc())>0){
//							selectionLigne.getDto().setAffectation(resteReelFacture);
//						}
						//si affectation supérieure au reste à regler du règlement
						BigDecimal resteReelReglement=taRReglement.getTaReglement().getNetTtcCalc().subtract(renvoiAffectationReglement(selectionLigne));
//						BigDecimal resteReelReglement=taRReglement.getTaReglement().getNetTtcCalc().subtract(taRReglement.getTaReglement().calculAffectationTotale(taRReglement));							
						if(selectionLigne.getDto().getAffectation().compareTo(resteReelReglement)>0){
							selectionLigne.getDto().setAffectation(resteReelReglement);
						}
						selectionLigne.getDto().setResteAReglerFacture(selectionLigne.getDto().getNetTtcCalc().subtract(dejaAffecteFacture.add(selectionLigne.getDto().getAffectation())) );
						taRReglement.setAffectation(selectionLigne.getDto().getAffectation());
					}
				}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

  public void actToutAffecter(ActionEvent event){
	  for (TaRReglementDTOJSF ligne : valuesLignes) {
		ligne.getDto().setAffectation(ligne.getDto().getResteAReglerFacture());
		validateUIField(Const.C_MONTANT_AFFECTE, ligne.getDto().getAffectation());		
	}
  }
  public void actAffecter(TaRReglementDTOJSF ligne){
	  actModifier(null);
	  ligne.getDto().setAffectationCourante(ligne.getDto().getAffectation());
	  ligne.getDto().setAffectation(ligne.getDto().getResteAReglerFacture().add(ligne.getDto().getAffectation()));
		validateUIField(Const.C_MONTANT_AFFECTE, ligne.getDto().getAffectation());		
		initTotaux();
		  ligne.getDto().setAffectationCourante(ligne.getDto().getAffectation());
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


	public List<TaRReglementDTOJSF> getValuesLignes() {
		return valuesLignes;
	}


	public void setValuesLignes(List<TaRReglementDTOJSF> valuesLignes) {
		this.valuesLignes = valuesLignes;
	}


	public List<TaRReglementDTOJSF> getFilteredValuesLignes() {
		return filteredValuesLignes;
	}


	public void setFilteredValuesLignes(List<TaRReglementDTOJSF> filteredValuesLignes) {
		this.filteredValuesLignes = filteredValuesLignes;
	}


	public TaRReglementDTOJSF getNouvelleLigne() {
		return nouvelleLigne;
	}


	public void setNouvelleLigne(TaRReglementDTOJSF nouvelleLigne) {
		this.nouvelleLigne = nouvelleLigne;
	}


	public TaRReglementDTOJSF getSelectionLigne() {
		return selectionLigne;
	}


	public void setSelectionLigne(TaRReglementDTOJSF selectionLigne) {
		this.selectionLigne = selectionLigne;
	}


	public Date getDateDeb() {
		return dateDeb;
	}


	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
	}


	public Date getDateFin() {
		return dateFin;
	}


	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}


	public TaTPaiement getTaTPaiment() {
		return taTPaiment;
	}


	public void setTaTPaiment(TaTPaiement taTPaiment) {
		this.taTPaiment = taTPaiment;
	}



	public TaTiers recupCodetiers(String code){
		FacesContext context = FacesContext.getCurrentInstance();
		if(code!=null && !code.isEmpty())
			try {
				return taTiersService.findByCode(code);
			} catch (FinderException e) {
				context.addMessage(null, new FacesMessage("erreur", "code tiers vide")); 	
			}
		return null;
	}

	public IDocumentTiers recupCodeDocument(String code){
		FacesContext context = FacesContext.getCurrentInstance();
		if(code!=null && !code.isEmpty())
			return documentValide(code);
		return null;
	}

    public void detail() {
    	filteredValues.clear();
    	filteredValues.add(selection);
    	detail(null);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }


	public TaRReglementDTOJSF[] getSelectionLignes() {
		return selectionLignes;
	}


	public void setSelectionLignes(TaRReglementDTOJSF[] selectionLignes) {
		this.selectionLignes = selectionLignes;
	}

	public boolean verifDocDejaExporte(IDocumentTiers document){
		try {		
			if(document!=null){
				if(((IDocumentTiers)document).getTypeDocument().equals(TaReglement.TYPE_DOC))
					document=taReglementService.findByCode(((TaReglement)document).getCodeDocument());
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(document!=null)return (document.getDateExport()!=null);
		return false;
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


	public Boolean getAfficheCritere() {
		return afficheCritere;
	}


	public void setAfficheCritere(Boolean afficheCritere) {
		this.afficheCritere = afficheCritere;
	}


	public String getTitreEtape1() {
		return titreEtape1;
	}


	public void setTitreEtape1(String titreEtape1) {
		this.titreEtape1 = titreEtape1;
	}


	public String getTitreEtape2() {
		return titreEtape2;
	}


	public void setTitreEtape2(String titreEtape2) {
		this.titreEtape2 = titreEtape2;
	}


	public String getTitreEtape3() {
		return titreEtape3;
	}


	public void setTitreEtape3(String titreEtape3) {
		this.titreEtape3 = titreEtape3;
	}


	public String getTitreEcran() {
		return titreEcran;
	}


	public void setTitreEcran(String titreEcran) {
		this.titreEcran = titreEcran;
	}

public void actSelectLigne( ToggleSelectEvent event){

}
	public void initTotaux() {
		nbLigne=0;
		totalTTC=BigDecimal.valueOf(0);
		montantReglementInitial=BigDecimal.valueOf(0);
		
			for (TaRReglementDTOJSF ligne : valuesLignes) {
				nbLigne++;
				totalTTC=totalTTC.add(ligne.getDto().getAffectation());
				montantReglementInitial=montantReglementInitial.add(ligne.getDto().getResteAReglerFacture());
			}	
			if(selection.getDto().getNetTtcCalc().signum()==0) {
				selection.getDto().setNetTtcCalc(montantReglementInitial);
			}
		setResteAAffecter(selection.getDto().getNetTtcCalc().subtract(totalTTC));
		if(taReglement!=null)taReglement.setNetTtcCalc(selection.getDto().getNetTtcCalc());
	}

 public BigDecimal renvoiAffectationReglement(TaRReglementDTOJSF courant) {
	 BigDecimal totalTTC=BigDecimal.valueOf(0);
		for (TaRReglementDTOJSF ligne : valuesLignes) {
			if(!ligne.equals(courant)) {
				totalTTC=totalTTC.add(ligne.getDto().getAffectation());
			}
		}	
	 return totalTTC;
 }
	public Integer getNbLigne() {
		return nbLigne;
	}


	public void setNbLigne(Integer nbLigne) {
		this.nbLigne = nbLigne;
	}


	public BigDecimal getTotalTTC() {
		return totalTTC;
	}


	public void setTotalTTC(BigDecimal totalTTC) {
		this.totalTTC = totalTTC;
	}


	public int getNbMaxLigne() {
		return nbMaxLigne;
	}


	public void setNbMaxLigne(int nbMaxLigne) {
		this.nbMaxLigne = nbMaxLigne;
	}


	public boolean getRemiseExistante() {
		if(selection!=null && selection.getDto()!=null){
			if(selection.getDto().getIdDocument()!=null && selection.getDto().getIdDocument()!=0)
				return true;
		}
		return false;
	}


	public boolean getRemiseExporte() {
		if(selection!=null && selection.getDto()!=null){
			if(selection.getDto().getIdDocument()!=null && selection.getDto().getIdDocument()!=0)
				return selection.getDto().getDateExport()!=null;
		}
		return false;
	}


	public TaCompteBanque getTaCompteBanqueFinal() {
		return taCompteBanqueFinal;
	}


	public void setTaCompteBanqueFinal(TaCompteBanque taCompteBanqueFinal) {
		this.taCompteBanqueFinal = taCompteBanqueFinal;
	}


	

	public TaReglementDTOJSF rempliDTO(){
		if(taReglement!=null){			
			try {
				taReglement=taReglementService.findByCode(taReglement.getCodeDocument());
				selection=new TaReglementDTOJSF();
				List<TaReglementDTO> l=taReglementService.rechercheDocumentDTO(taReglement.getCodeDocument(), taReglement.getCodeDocument());
				selection.setDto(l.get(0));
		    	filteredValues.clear();
		    	filteredValues.add(selection);
//				mapperModelToUI.map(taReglement, selection.getDto());
				if(values.contains(selection))values.add(selection);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selection;
	}

	public TaReglement getTaReglement() {
		return taReglement;
	}

	public void setTaReglement(TaReglement taReglement) {
		this.taReglement = taReglement;
	}
	
	private IDocumentTiers documentValide(String code){
		try {	
			taFacture=taFactureService.findByCode(code);
			return taFacture;
		} catch (FinderException e) {
			return null;
		}
	}

	public TaTiersDTO getTaTiers() {
		return taTiersDTO;
	}

	public void setTaTiers(TaTiersDTO taTiers) {
		this.taTiersDTO = taTiers;
	}

	public TaFacture getTaFacture() {
		return taFacture;
	}

	public void setTaFacture(TaFacture taFacture) {
		this.taFacture = taFacture;
	}
	
	public void onRowEdit(RowEditEvent event) {
//		if(selectionLigne!=null)
//			validateUIField(Const.C_MONTANT_AFFECTE, selectionLigne.getDto().getAffectation());
		initTotaux();
	}
	public void onRowEdit(TaRReglementDTOJSF ligne) {
		if(ligne!=null)
			selectionLigne=ligne;
			validateUIField(Const.C_MONTANT_AFFECTE, ligne.getDto().getAffectation());
		initTotaux();
	}

	public void onRowEditInit(RowEditEvent event) {
		if(event.getObject()!=null && !event.getObject().equals(selectionLigne))
			selectionLigne= (TaRReglementDTOJSF) event.getObject();
		selectionLigne.getDto().setAffectationCourante(selectionLigne.getDto().getAffectation());
}

	public List<TaRReglementDTOJSF> getValuesLignesARegler() {
		return valuesLignesARegler;
	}

	public void setValuesLignesARegler(List<TaRReglementDTOJSF> valuesLignesARegler) {
		this.valuesLignesARegler = valuesLignesARegler;
	}

	public TaRReglementDTOJSF getSelectionLigneARegler() {
		return selectionLigneARegler;
	}

	public void setSelectionLigneARegler(TaRReglementDTOJSF selectionLigneARegler) {
		this.selectionLigneARegler = selectionLigneARegler;
	}

	public BigDecimal getResteAAffecter() {
		return resteAAffecter;
	}

	public void setResteAAffecter(BigDecimal resteAAffecter) {
		this.resteAAffecter = resteAAffecter;
	}
	
	public void actDialogGenerationReglement() {
		try {	

				Map<String,Object> options = new HashMap<String, Object>();
				options.put("modal", true);
				options.put("draggable", false);
				options.put("closable", false);
				options.put("resizable", true);
				options.put("contentHeight", 710);
				options.put("contentWidth", "100%");
				options.put("width", 1024);

				Map<String,Object> mapDialogue = new HashMap<String,Object>();

				mapDialogue.put("masterEntity",recupCodeDocument(selectionLigne.getDto().getCodeFacture()) );
				mapDialogue.put("readOnly",true);

				Map<String,List<String>> params = new HashMap<String,List<String>>();
				List<String> list = new ArrayList<String>();
				list.add(modeEcran.modeString(EnumModeObjet.C_MO_CONSULTATION));
				params.put("modeEcranDefaut", list);


				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				sessionMap.put("mapDialogue", mapDialogue);

				PrimeFaces.current().dialog().openDynamic("reglement/multiple/reglement_multiple_facture.xhtml", options, params);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void handleReturnDialogGestionReglement(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
	 
		}
//		if(docEnregistre)updateTab();
	}

	public String getNomTiers() {
		return nomTiers;
	}

	public String getRaisonSocialeTiers() {
		return raisonSocialeTiers;
	}

	public String getPrenomTiers() {
		return prenomTiers;
	}

	public String getTelephoneTiers() {
		return telephoneTiers;
	}

	public String getEmailTiers() {
		return emailTiers;
	}

	public BigDecimal getMontantTotalTTC() {
		return montantTotalTTC;
	}

	public BigDecimal getMontantTotalHT() {
		return montantTotalHT;
	}
	
	public void actDialogEmail(ActionEvent actionEvent) {		
		EmailParam email = new EmailParam();
		email.setEmailExpediteur(null);
		email.setNomExpediteur(infos.getNomInfoEntreprise()); 
		email.setSubject(infos.getNomInfoEntreprise()); 
		email.setBodyPlain("Votre message ici");
		email.setBodyHtml("Votre message ici");
		email.setDestinataires(new String[]{getEmailTiers()});
		try {
			tiersCourant = taTiersService.findByCode(codeTiers);
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		email.setEmailDestinataires(new TaEmail[]{tiersCourant.getTaEmail()});
		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File((taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		actDialogEmail(email);
	}	
	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//List<ControleConformiteJSF> listeControle = (List<ControleConformiteJSF>) event.getObject();
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", 
					"Email envoyée "
					)); 
		}
	}

	public String getVilleTiers() {
		return villeTiers;
	}

	public String getCpTiers() {
		return cpTiers;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public boolean isPreInsertion() {
		return preInsertion;
	}

	public void setPreInsertion(boolean preInsertion) {
		this.preInsertion = preInsertion;
	}
	
	
	public Verrouillage affectationEstVerrouille(TaRReglement courant) {
		if(courant!=null ) {
			return Verrouillage.determineVerrouillageAffectation(courant.getTaMiseADisposition(), courant.getDateExport(), courant.getDateVerrouillage());
		}
		return null;
	}
	
	public Verrouillage documentEstVerrouille(IDocumentTiers courant) {
		if(courant!=null ) {
			return Verrouillage.determineVerrouillage(courant.getTaMiseADisposition(), courant.getDateExport(), courant.getDateVerrouillage());
		}
		return null;
	}
	public Verrouillage documentEstVerrouille() {
		return documentEstVerrouille(taReglement);
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
	
	public Verrouillage rAffectaionDTOEstVerrouille() {
		return rAffectaionDTOEstVerrouille(selectionLigne);
	}
	public Verrouillage rAffectaionDTOEstVerrouille(TaRReglementDTOJSF courant) {
		if(courant!=null ) {
			return Verrouillage.determineVerrouillageAffectation(courant.getDto().getEstMisADisposition(), courant.getDto().getDateExport(), courant.getDto().getDateVerrouillage());
		}
		return null;
	}
	

	protected boolean verifSiAffectationEstModifiable(TaRReglement courant) {
		Verrouillage estVerrouille=affectationEstVerrouille(courant);
		if(!estVerrouille.isVerrouille())return true;
		PrimeFaces.current().ajax().addCallbackParam("verifSiEstModifiable", estVerrouille);
		if(estVerrouille.isVerrouille())
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Document",estVerrouille.getMessage() ));
		return false;
	}
	
	protected boolean verifSiEstModifiable() {
		return verifSiEstModifiable(taReglement);
	}
	protected boolean verifSiEstModifiable(TaReglement courant) {
		Verrouillage estVerrouille=documentEstVerrouille(courant);
		if(!estVerrouille.isVerrouille())return true;
		PrimeFaces.current().ajax().addCallbackParam("verifSiEstModifiable", estVerrouille);
		if(estVerrouille.isVerrouille())
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Document",estVerrouille.getMessage() ));
		return false;
	}

	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
	}
}
