package fr.legrain.bdg.webapp.remise;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLRemiseServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaCompteBanqueServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.documents.Verrouillage;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLRemiseDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaLRemise;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRemise;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class RemiseController extends AbstractController{

	@Inject @Named(value="tabListModelCenterBean")
	protected TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	protected TabViewsBean tabViews;
	
	private List<TaRemiseDTOJSF> values=new LinkedList<>();
	private List<TaRemiseDTOJSF> filteredValues=new LinkedList<>();
	private List<TaLRemiseDTOJSF> valuesLignes=new LinkedList<>();
	private List<TaLRemiseDTOJSF> filteredValuesLignes=new LinkedList<>();
	
	private TaRemiseDTOJSF old ;
//	private TaRelanceDTOJSF nouveau ;
	private TaRemiseDTOJSF selection ;
	
	private TaLRemiseDTOJSF nouvelleLigne ;
	private TaLRemiseDTOJSF selectionLigne ;
	private TaLRemiseDTOJSF[] selectionLignes ;
	
	private TaRemise taRemise;
	private TaTPaiement taTPaiment;
	private TaCompteBanque taCompteBanque;
	private TaCompteBanque taCompteBanqueFinal;
	private TaTiers taTiers;
	protected String typePaiementDefaut=null;
	private Date dateDeb=null;
	private Date dateFin=null;	

	TaInfoEntreprise infos =null;
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	private String titlePartOne="Gestion des remises";
	private String titlePartTwo="";
	private int nbMaxLigne=20;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private Boolean afficheCritere=false;
	
	private String titreEtape1="Etape 1/2 : Saisissez les critères de sélection des Règlements";
	private String titreEtape2="Etape 2/2 : Sélectionnez les règlements à intégrer à la remise dans la liste";
	private String titreEtape3="Détail de la remise n° : ";
	private String titreEcran=titreEtape2;
	
	Integer nbLigne=0;
	BigDecimal totalTTC=BigDecimal.valueOf(0);
	
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaRemiseServiceRemote taRemiseServiceRemote;
	private @EJB ITaLRemiseServiceRemote taLRemiseServiceRemote;
	private @EJB  ITaInfoEntrepriseServiceRemote TaInfoEntrepriseService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected @EJB ITaTPaiementServiceRemote taTPaiementService;
	protected @EJB ITaTiersServiceRemote taTiersService;
	protected @EJB ITaCompteBanqueServiceRemote taCompteBanqueService;
	protected @EJB ITaGenCodeExServiceRemote taGenCodeExService;
	
	
	private LgrDozerMapper<TaRemiseDTO,TaRemise> mapperUIToModel  = new LgrDozerMapper<TaRemiseDTO,TaRemise>();
	private LgrDozerMapper<TaRemise,TaRemiseDTO> mapperModelToUI  = new LgrDozerMapper<TaRemise,TaRemiseDTO>();
	
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
		this.setFilteredValues(values);
	}
	
	
	public RemiseController() {  
	}  

	public List<TaTPaiement> typePaiementAutoComplete(String query) {
		List<TaTPaiement> allValues = taTPaiementService.selectAll();
		List<TaTPaiement> filteredValues = new ArrayList<TaTPaiement>();
		TaTPaiement cp = new TaTPaiement();
		cp.setCodeTPaiement(Const.C_AUCUN);
//		filteredValues.add(cp);
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
		taCompteBanque=taCompteBanqueService.findByTiersEntreprise(taTPaiment);
		taCompteBanqueFinal=taCompteBanque;
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actInitInserer(ActionEvent actionEvent){
		if(selection!=null && selection.getDto()!=null){
			taRemiseServiceRemote.annuleCode(selection.getDto().getCodeDocument());
		}
		afficheCritere=true;	
		afficheTitre();
		selection=new TaRemiseDTOJSF();
		taCompteBanqueFinal=null;
		valuesLignes.clear();
		selectionLignes=rempliSelection(valuesLignes);
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		initTotaux();
	}
	public void actInserer(ActionEvent actionEvent){
		try {

			if(selection!=null && selection.getDto()!=null){
				taRemiseServiceRemote.annuleCode(selection.getDto().getCodeDocument());
			}		
		valuesLignes.clear();
		
		//créer une nouvelle remise
		selection=new TaRemiseDTOJSF();
		selection.setDto(new TaRemiseDTO());

		selection.getDto().setDateDocument(TaInfoEntrepriseService.dateDansExercice(new Date()));
		
		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		Map<String, String> paramsCode = new LinkedHashMap<>();
		paramsCode.put(Const.C_DATEDOCUMENT, LibDate.dateToString(selection.getDto().getDateDocument()));
		
		selection.getDto().setCodeDocument(taRemiseServiceRemote.genereCode(paramsCode));


		selection.getDto().setLibelleDocument("Remise de réglement n° :"+selection.getDto().getCodeDocument());

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
		if(taCompteBanque!=null && taCompteBanque.getIban()!=null){
			iban=taCompteBanque.getIban();
		}
		//insérer les lignes de réglements qui peuvent rentrer dans la remise
		List<TaReglement> listeReglement=taReglementService.rechercheReglementNonRemises(
				null,dateDeb,dateFin,
				false,codeTPaiement,
				iban,false,nbMaxLigne);
		for (TaReglement reglement : listeReglement) {
			TaLRemiseDTOJSF obj =new TaLRemiseDTOJSF();
			obj.setDto(new TaLRemiseDTO());
			obj.getDto().setId(reglement.getIdDocument());
			obj.getDto().setTypeDocument(TaReglement.TYPE_DOC);
			obj.getDto().setCodeReglement(reglement.getCodeDocument());
			obj.getDto().setDateReglement(reglement.getDateDocument());
			if(reglement.getTaTiers()!=null){
				obj.getDto().setCodeTiers(reglement.getTaTiers().getCodeTiers());
				obj.getDto().setNomTiers(reglement.getTaTiers().getNomTiers());
			}
			obj.getDto().setNetTTC(reglement.getNetTtcCalc());
			obj.getDto().setLibelleReglement(reglement.getLibelleDocument());
			if(reglement.getTaCompteBanque()!=null){
				obj.getDto().setNomBanque(reglement.getTaCompteBanque().getNomBanque());
				obj.getDto().setIban(reglement.getTaCompteBanque().getIban());
				obj.setTaCompteBanque(reglement.getTaCompteBanque());
			}
			if(reglement.getTaTPaiement()!=null){
				obj.getDto().setCodeTPaiement(reglement.getTaTPaiement().getCodeTPaiement());
				obj.setTaTPaiement(reglement.getTaTPaiement());
			}
			if(!verifDocDejaExporte(reglement))obj.getDto().setAccepte(true);
			valuesLignes.add(obj);				
		}
		
		if(valuesLignes!=null && !valuesLignes.isEmpty())selectionLigne=valuesLignes.get(0);		
		nouvelleLigne=selectionLigne;
		
		selectionLignes=rempliSelection(valuesLignes);
		afficheCritere=true;	
		afficheTitre();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		initTotaux();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TaLRemiseDTOJSF[] rempliSelection(List<TaLRemiseDTOJSF> liste){
		List<TaLRemiseDTOJSF> tmp = new LinkedList<>();
		for (TaLRemiseDTOJSF obj : liste) {
			if(obj.getDto().getAccepte())tmp.add(obj);
		}
		return selectionLignes= Arrays.copyOf(tmp.toArray(),tmp.size(),TaLRemiseDTOJSF[].class);		
	}
	
	
	public void actModifier(ActionEvent actionEvent){
		try {
			if(modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION)){
				taRemise=taRemiseServiceRemote.findById(selection.getDto().getId());
				if(verifSiEstModifiable()) {
					modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
				}
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actSupprimer(ActionEvent actionEvent){
		try{
			if(selection!=null){
				taRemise=taRemiseServiceRemote.findById(selection.getDto().getId());
				if(verifSiEstModifiable(taRemise)) {
					if(selection.getDto().getDateExport()==null){
						taRemiseServiceRemote.remove(taRemise);
					}
					else{
						PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Remise", "Cette remise est exportée."
								+ "\r\nVous ne pouvez pas la supprimer."));
					}


					taRemiseServiceRemote.annuleCode(selection.getDto().getCodeDocument());

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
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
			context.addMessage(null, new FacesMessage("remise", "actImprimer")); 
		}
		try {

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();

			Map<String,Object> mapEdition = new HashMap<String,Object>();

			TaRemise doc =taRemiseServiceRemote.findById(selection.getDto().getId());
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
	
	public void actImprimerListeDesRemises(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesRemises", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public void actSupprimerLigne(){
		try{
			if(verifSiEstModifiable()) {
				actModifier(null);
				if(selectionLigne!=null){
					valuesLignes.remove(selectionLigne);
					TaLRemise l=rechercheLRemise(selectionLigne);
					if(l!=null){
						taRemise.getTaLRemises().remove(l);
					}
				}

				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("remise", "actSupprimerLigne"));
				}
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
			if(selection!=null && selection.getDto()!=null && (selection.getDto().getId()==null  || selection.getDto().getId()==0)){
			taRemise=new TaRemise();
//			mapperUIToModel.map(selection.getDto(), taRemise);
			taRemise.setCodeDocument(selection.getDto().getCodeDocument());
			taRemise.setLibelleDocument(selection.getDto().getLibelleDocument());
			taRemise.setDateDocument(selection.getDto().getDateDocument());
			if(taCompteBanqueFinal==null){
				taCompteBanqueFinal=taCompteBanqueService.findByTiersEntreprise(taTPaiment);
			}
			taRemise.setTaCompteBanque(taCompteBanqueFinal);
			taRemise.setTaTPaiement(taTPaiment);
			
			for (TaLRemiseDTOJSF taRemiseDTOJSF : selectionLignes) {
				if(taRemiseDTOJSF.getDto().getAccepte()){
					TaLRemise r =new TaLRemise();
					r.setTaDocument(taRemise);
					r.setTypeDocument(TaReglement.TYPE_DOC);
					r.setCodeDocument(taRemiseDTOJSF.getDto().getCodeReglement());
					r.setTaReglement(taReglementService.findById(taRemiseDTOJSF.getDto().getId()));
					r.setAccepte(true);
					if(!verifDocDejaExporte(r.getTaReglement()))
						taRemise.getTaLRemises().add(r);
					else{
						//message
					}
				}
			}
			}else if(selection!=null && selection.getDto().getId()!=null && selection.getDto().getId()!=0){
				//déjà existant
//				taRemise=taRemiseServiceRemote.findById(selection.getDto().getId());
				mapperUIToModel.map(selection.getDto(), taRemise);
			}
			old=selection;
			taRemise=taRemiseServiceRemote.enregistrerMerge(taRemise);
			taRemiseServiceRemote.annuleCode(selection.getDto().getCodeDocument());
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			refresh();
			if(old!=null){
				selection=recherche(old);
			}

		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remise", e.getMessage()));
		}
	}
	
	public TaLRemise rechercheLRemise(TaLRemiseDTOJSF courant){
		for (TaLRemise ligne : taRemise.getTaLRemises()) {
			if(courant.getDto().getCodeReglement().equals(ligne.getTaReglement().getCodeDocument()))
				return ligne;
		}
		return null;
	}
	
	public TaRemiseDTOJSF recherche(TaRemiseDTOJSF courant){
		for (TaRemiseDTOJSF taRReglementDTOJSF : values) {
			if(courant.getDto().getCodeDocument().equals(taRReglementDTOJSF.getDto().getCodeDocument()))
				return taRReglementDTOJSF;
		}
		return courant;
	}
	
	public void actAnnuler(ActionEvent actionEvent){
		if(selection!=null && selection.getDto()!=null){
			taRemiseServiceRemote.annuleCode(selection.getDto().getCodeDocument());

			TaRemiseDTOJSF old=selection;
			selection=null;
			refresh();
			if(old!=null && old.getDto()!=null && old.getDto().getId()!=null && old.getDto().getId()!=0)
				selection=recherche(old);
//			nouveau=selection;
		}
		afficheCritere=false;
		afficheTitre();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}



//	public TaRelanceDTOJSF getNouveau() {
//		return nouveau;
//	}
//
//	public void setNouveau(TaRelanceDTOJSF newTaRReglement) {
//		this.nouveau = newTaRReglement;
//	}

	public TaRemiseDTOJSF getSelection() {
		return selection;
	}

	public void setSelection(TaRemiseDTOJSF selectedTaRReglement) {
		this.selection = selectedTaRReglement;
	}


	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_REMISE);
		b.setTitre("Remise");
		b.setTemplate("remise/remise.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REMISE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_REMISE);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		tabViews.selectionneOngletCentre(b);
		actInitInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Prelevement", 
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
		b.setTypeOnglet(LgrTab.TYPE_TAB_REMISE);
		b.setTitre("Remises");
		b.setTemplate("remise/remise.xhtml");
		b.setIdTab(LgrTab.ID_TAB_REMISE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_REMISE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		afficheCritere=false;
		afficheTitre();
		
		taRemise=taRemiseServiceRemote.findById(selection.getDto().getId());
		
		if(selection.getDto().getCodeTPaiement()!=null){
			taTPaiment= taTPaiementService.findByCode(selection.getDto().getCodeTPaiement());
		}
		if(selection.getDto().getCodeTiers()!=null){
			taTiers= taTiersService.findByCode(selection.getDto().getCodeTiers());
		}
		if(selection.getDto().getIban()!=null){
			taCompteBanqueFinal= taCompteBanqueService.findByCode(selection.getDto().getIban());
		}
		refreshLigne();
		
		PrimeFaces.current().executeScript("window.scrollTo(0,0);");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void onRowSelectLigne(SelectEvent event) { 
		initTotaux();
	}
	
	public void onRowUnSelectLigne(UnselectEvent event) { 
		initTotaux();
	}

	public void refreshLigne(){
		try {
			valuesLignes.clear();

			if(selection!=null && selection.getDto()!=null && selection.getDto().getId()!=0){
				//récupérer les lignes de la remise
				List<TaLRemiseDTO> liste=taLRemiseServiceRemote.RechercheLigneRemiseDTO(selection.getDto().getCodeDocument());
				for (TaLRemiseDTO taLRemiseDTO : liste) {
					TaLRemiseDTOJSF obj = new TaLRemiseDTOJSF();
//					if(obj.getDto().getCodeTPaiement()!=null){
//						taTPaiment=taTPaiementService.findByCode(obj.getDto().getCodeTPaiement());
//					}
//					if(obj.getDto().getIdCompteBanque()!=null){
//						taCompteBanque=taCompteBanqueService.findById(obj.getDto().getIdCompteBanque());
//					}					
					obj.setDto(taLRemiseDTO);
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
	
		List<TaRemiseDTO> liste= taRemiseServiceRemote.rechercheDocumentDTO(dateDeb, dateFin);
		for (TaRemiseDTO taRemiseDTO : liste) {
			TaRemiseDTOJSF obj = new TaRemiseDTOJSF();
			obj.setDto(taRemiseDTO);

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
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actFermer(ActionEvent actionEvent) {
		if(selection!=null && selection.getDto()!=null){
			taRemiseServiceRemote.annuleCode(selection.getDto().getCodeDocument());
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
			if(nomChamp.equals(Const.C_COMPTE_BANQUE)) {
				if(value!=null && value instanceof TaCompteBanque){
					TaCompteBanque obj=(TaCompteBanque)value;
					taCompteBanque=obj;
				}else if(value==null){
					taCompteBanque=null;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}



	public List<TaRemiseDTOJSF> getValues() {
		return values;
	}

	public void setValues(List<TaRemiseDTOJSF> values) {
		this.values = values;
	}

	public List<TaRemiseDTOJSF> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaRemiseDTOJSF> filteredValues) {
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


	public List<TaLRemiseDTOJSF> getValuesLignes() {
		return valuesLignes;
	}


	public void setValuesLignes(List<TaLRemiseDTOJSF> valuesLignes) {
		this.valuesLignes = valuesLignes;
	}


	public List<TaLRemiseDTOJSF> getFilteredValuesLignes() {
		return filteredValuesLignes;
	}


	public void setFilteredValuesLignes(List<TaLRemiseDTOJSF> filteredValuesLignes) {
		this.filteredValuesLignes = filteredValuesLignes;
	}


	public TaLRemiseDTOJSF getNouvelleLigne() {
		return nouvelleLigne;
	}


	public void setNouvelleLigne(TaLRemiseDTOJSF nouvelleLigne) {
		this.nouvelleLigne = nouvelleLigne;
	}


	public TaLRemiseDTOJSF getSelectionLigne() {
		return selectionLigne;
	}


	public void setSelectionLigne(TaLRemiseDTOJSF selectionLigne) {
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


	public TaCompteBanque getTaCompteBanque() {
		return taCompteBanque;
	}


	public void setTaCompteBanque(TaCompteBanque taCompteBanque) {
		this.taCompteBanque = taCompteBanque;
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
			try {
				return taReglementService.findByCode(code);
			} catch (FinderException e) {
				context.addMessage(null, new FacesMessage("erreur", "code règlement vide")); 	
			}
		return null;
	}

    public void detail() {
    	detail(null);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }


	public TaLRemiseDTOJSF[] getSelectionLignes() {
		return selectionLignes;
	}


	public void setSelectionLignes(TaLRemiseDTOJSF[] selectionLignes) {
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
	initTotaux();
}
	public void initTotaux() {
		nbLigne=0;
		totalTTC=BigDecimal.valueOf(0);
		if(selection.getDto().getId()==null ||selection.getDto().getId()==0){
			for (TaLRemiseDTOJSF ligne : selectionLignes) {
				nbLigne++;
				totalTTC=totalTTC.add(ligne.getDto().getNetTTC());
			}
		}else{
			for (TaLRemiseDTOJSF ligne : valuesLignes) {
				nbLigne++;
				totalTTC=totalTTC.add(ligne.getDto().getNetTTC());
			}	
		}
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
			if(selection.getDto().getId()!=null && selection.getDto().getId()!=0)
				return true;
		}
		return false;
	}


	public boolean getRemiseExporte() {
		if(selection!=null && selection.getDto()!=null){
			if(selection.getDto().getId()!=null && selection.getDto().getId()!=0)
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


	public TaRemise getTaRemise() {
		return taRemise;
	}


	public void setTaRemise(TaRemise taRemise) {
		this.taRemise = taRemise;
	}

	public TaRemiseDTOJSF rempliDTO(){
		if(taRemise!=null){			
			try {
				taRemise=taRemiseServiceRemote.findByCode(taRemise.getCodeDocument());
				selection=new TaRemiseDTOJSF();
				mapperModelToUI.map(taRemise, selection.getDto());
				if(values.contains(selection))values.add(selection);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selection;
	}
	
	
	public Verrouillage documentDTOEstVerrouille() {
		return documentDTOEstVerrouille(selection);
	}
	public Verrouillage documentDTOEstVerrouille(TaRemiseDTOJSF courant) {
		if(courant!=null) {
			return Verrouillage.determineVerrouillage(false,courant.getDto().getDateExport(), courant.getDto().getDateVerrouillage());
		}
		return null;
	}

	public Verrouillage documentEstVerrouille(TaRemise courant) {
		if(courant!=null) {
			return Verrouillage.determineVerrouillage(null, courant.getDateExport(), courant.getDateVerrouillage());
		}
		return null;
	}

	public Verrouillage documentEstVerrouille() {
		return documentEstVerrouille(taRemise);	
	}

	
	
	protected boolean verifSiEstModifiable() {
		return verifSiEstModifiable(taRemise);
	}
	protected boolean verifSiEstModifiable(TaRemise courant) {
		Verrouillage estVerrouille=documentEstVerrouille(courant);
		if(!estVerrouille.isVerrouille())return true;
		PrimeFaces.current().ajax().addCallbackParam("verifSiEstModifiable", estVerrouille);
		if(estVerrouille.isVerrouille())
			PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Document",estVerrouille.getMessage() ));
		return false;
	}
}
