package fr.legrain.bdg.webapp.relance;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLRelanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaNiveauRelanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaRelanceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLRelanceDTO;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaRelanceDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLRelance;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaRelance;
import fr.legrain.document.model.TaTRelance;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class RelanceController extends AbstractController{

	@Inject @Named(value="tabListModelCenterBean")
	protected TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	protected TabViewsBean tabViews;
	
	private List<TaLRelanceDTOJSF> valuesLignes = new LinkedList<>();
	
	private TaLRelanceDTOJSF nouvelleLigne ;
	private TaLRelanceDTOJSF selectionLigne ;
	private List<TaLRelanceDTOJSF> selectionLignes = new ArrayList<>();
	
	private StreamedContent exportation;
	
	private TaRelance taRelance;
	private TaTiers taTiers;
	private TaTiersDTO taTiersDTO;
	private Date dateDeb = null;
	private Date dateFin = null;	
	private String codeTiers;
	private String limite;
	private String codeRelance;
	private List<TaFacture> listeDocumentRelance;
	private List<TaRelance> values;
	private boolean tousLesTiers = false;
	private List<TaTRelance> listeNiveauRelance;
	private TaRelance selection;
	private Map<String,TabNiveauRelanceJSF> mapRelanceParNiveau = new HashMap<>();

	private TaInfoEntreprise infos = null;
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	private int nbMaxLigne=20;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private Boolean afficheCritere=false;

	
	private Integer nbLigne = 0;
	private BigDecimal totalTTC = BigDecimal.valueOf(0);
	
	private @EJB ITaReglementServiceRemote taReglementService;
	private @EJB ITaRelanceServiceRemote taRelanceService;
	private @EJB ITaLRelanceServiceRemote taLRelanceServiceRemote;
	private @EJB ITaNiveauRelanceServiceRemote niveauRelanceService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaFactureServiceRemote taFactureService;
	
	private LgrDozerMapper<TaRelanceDTO,TaRelance> mapperUIToModel  = new LgrDozerMapper<TaRelanceDTO,TaRelance>();
	private LgrDozerMapper<TaRelance,TaRelanceDTO> mapperModelToUI  = new LgrDozerMapper<TaRelance,TaRelanceDTO>();
	
	public RelanceController() {}  
	
	@PostConstruct
	public void postConstruct(){
		try {
			infos = taInfoEntrepriseService.findInstance();

			dateDeb = infos.getDatedebInfoEntreprise();
			dateFin = infos.getDatefinInfoEntreprise();
			
			listeNiveauRelance = niveauRelanceService.selectAll();
			
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//this.setFilteredValues(values);
	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		
		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaCompteBanque &&((((TaCompteBanque) value).getIban()==null)|| ((TaCompteBanque) value).getIban()!=null && ((TaCompteBanque) value).getIban().equals(Const.C_AUCUN)))value=null;
//			if(value instanceof TaTPaiement &&((((TaTPaiement) value).getCodeTPaiement()==null)||  ((TaTPaiement) value).getCodeTPaiement()!=null && ((TaTPaiement) value).getCodeTPaiement().equals(Const.C_AUCUN)))value=null;	
		}
		
		validateUIField(nomChamp,value);
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
	
	public void actRechercherDocument(ActionEvent actionEvent){
		Date deb = new Date();
		mapRelanceParNiveau = new HashMap<>();
		listeDocumentRelance = taFactureService.rechercheDocumentNonTotalementRegleAEcheance(
				dateDeb,
				dateFin,
				taTiersDTO!=null?taTiersDTO.getCodeTiers():"",null,LibConversion.stringToBigDecimal(limite, BigDecimal.valueOf(0)) );
		
		taRelance=new TaRelance();
		Date dateRelance=new Date();
		taRelance.setCodeRelance("Relance du "+LibDate.dateToString(dateRelance)+" à "
									+ LibDate.getHeure(dateRelance)+"h "
									+ LibDate.getMinute(dateRelance)+"mn "
									+ LibDate.getSeconde(dateRelance)+"s");
		taRelance.setDateDebut(dateDeb);
		taRelance.setDateFin(dateFin);
		taRelance.setCodeTiers(codeTiers!=null?codeTiers:"");
		taRelance.setDateRelance(dateRelance);
		valuesLignes = new ArrayList<>();
		TaTRelance taTRelance = null;
		TaLRelanceDTOJSF r2 = null;
		TaLRelance r = null;
		for (TaFacture taFacture : listeDocumentRelance) {
			taTRelance = taRelanceService.maxTaTRelance(taFacture);
			if(taTRelance!=null){
				r2 = new TaLRelanceDTOJSF();
				r = new TaLRelance();
				r.setTypeDocument(TypeDoc.TYPE_FACTURE);
				r.setCodeDocument(taFacture.getCodeDocument());
				r.setCodeTiers(taFacture.getTaTiers().getCodeTiers());
				r.setNomTiers(taFacture.getTaTiers().getNomTiers());
				r.setDateEcheance(taFacture.getDateEchDocument());
				r.setNetTTC(taFacture.getNetTtcCalc());
				r.setResteARegler(taFacture.getResteAReglerComplet());
				r.setTaRelance(taRelance);
				r.setTaTRelance(taTRelance);
				r.setAccepte(true);
				taRelance.getTaLRelances().add(r);
				
				r2.setTaFacture(taFacture);
				r2.setTaTiers(taFacture.getTaTiers());
				r2.setTaLRelance(r);
				valuesLignes.add(r2);
				
				if(mapRelanceParNiveau.get(taTRelance.getCodeTRelance())==null) {
					mapRelanceParNiveau.put(taTRelance.getCodeTRelance(), new TabNiveauRelanceJSF(taTiersService));
				} 
				mapRelanceParNiveau.get(taTRelance.getCodeTRelance()).getListeRelance().add(r2);
			}
		}
		codeRelance = taRelance.getCodeRelance();
//		values = new LinkedList<TaRelance>();
//		values.add(taRelance);
		
//		modelRelance.setListeEntity(values);
//		actRefresh();
		initTotaux();
		
		Date fin = new Date();
		Long duree = fin.getTime()-deb.getTime();
		System.out.println("***** durée : "+duree );
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actInitInserer(ActionEvent actionEvent){
		if(selection!=null && selection!=null){
			//taRelanceService.annuleCode(selection.getCodeRelance());
		}
//		afficheCritere=true;	
//		afficheTitre();
		selection = new TaRelance();
		taRelance = new TaRelance();
		taRelance.setCodeRelance("");
		codeRelance = "";
		valuesLignes = IHMmodel();
//		valuesLignes.clear();
//		selectionLignes=rempliSelection(valuesLignes);
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		initTotaux();
	}
	
	public void actInserer(ActionEvent actionEvent){
		try {
//
//			if(selection!=null && selection.getDto()!=null){
//				taRelanceService.annuleCode(selection.getDto().getCodeRelance());
//			}		
//		valuesLignes.clear();
//		
//		//créer une nouvelle remise
//		selection=new TaRelanceDTOJSF();
//		selection.setDto(new TaRelanceDTO());
//		selection.getDto().setCodeRelance(taRelanceService.genereCode(null));
//
//		selection.getDto().setDateRelance(taInfoEntrepriseService.dateDansExercice(new Date()));
//
//		selection.getDto().setLibelleDocument("Remise de réglement n° :"+selection.getDto().getCodeDocument());
//		
		selection = new TaRelance();
		taRelance = new TaRelance();
		valuesLignes = IHMmodel();
		taRelance.setCodeRelance("");
		codeRelance = "";
			
//		String codeTPaiement = "%";
//		String iban =null;
//		
//		//insérer les lignes de réglements qui peuvent rentrer dans la remise
//		List<TaReglement> listeReglement=taReglementService.rechercheReglementNonRemises(
//				null,dateDeb,dateFin,
//				false,codeTPaiement,
//				iban,false,nbMaxLigne);
//		for (TaReglement reglement : listeReglement) {
//			TaLRelanceDTOJSF obj =new TaLRelanceDTOJSF();
//			obj.setDto(new TaLRelanceDTO());
//			obj.getDto().setId(reglement.getIdDocument());
//			obj.getDto().setTypeDocument(TaReglement.TYPE_DOC);
//			obj.getDto().setCodeRelance(reglement.getCodeDocument());
//			obj.getDto().setDateReglement(reglement.getDateDocument());
//			if(reglement.getTaTiers()!=null){
//				obj.getDto().setCodeTiers(reglement.getTaTiers().getCodeTiers());
//				obj.getDto().setNomTiers(reglement.getTaTiers().getNomTiers());
//			}
//			obj.getDto().setNetTTC(reglement.getNetTtcCalc());
//			
//			if(!verifDocDejaExporte(reglement))obj.getDto().setAccepte(true);
//			valuesLignes.add(obj);				
//		}
//		
//		if(valuesLignes!=null && !valuesLignes.isEmpty())selectionLigne=valuesLignes.get(0);		
//		nouvelleLigne=selectionLigne;
//		
//		selectionLignes=rempliSelection(valuesLignes);
//		afficheCritere=true;	
////		afficheTitre();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		initTotaux();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TaLRelanceDTOJSF[] rempliSelection(List<TaLRelanceDTOJSF> liste){
		return null;
//		List<TaLRelanceDTOJSF> tmp = new LinkedList<>();
//		for (TaLRelanceDTOJSF obj : liste) {
//			if(obj.getDto().getAccepte())tmp.add(obj);
//		}
//		return selectionLignes= Arrays.copyOf(tmp.toArray(),tmp.size(),TaLRelanceDTOJSF[].class);		
	}
	
	
	public void actModifier(ActionEvent actionEvent){
//		try {
//			if(modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION)){
//				taRelance=taRelanceService.findById(selection.getDto().getId());
//				if(verifSiEstModifiable()) {
//					modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//				}
//			}
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void actSupprimer(ActionEvent actionEvent){
		try{
			if(selection!=null){
				taRelance = taRelanceService.findById(selection.getIdRelance());
//				if(verifSiEstModifiable(taRelance)) {
//					if(selection.getDto().getDateExport()==null){
						taRelanceService.remove(taRelance);
//					}
//					else{
//						RequestContext context = RequestContext.getCurrentInstance();
//						PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Remise", "Cette remise est exportée."
//								+ "\r\nVous ne pouvez pas la supprimer."));
//					}


//					taRelanceService.annuleCode(selection.getDto().getCodeRelance());

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
					selection=null;
//					old=null;
					refresh();
					
					if(!values.isEmpty()) {
						selection = values.get(0);
//						selectedDocumentDTOs = new TaFactureDTO[]{selectedDocumentDTO};
						updateTab();
					}else{
						selection = new TaRelance();
//						selectedDocumentDTOs = new TaFactureDTO[]{};
					}
//				}
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
	
	public StreamedContent getExportation() {
		File f = null;
		InputStream stream = null;
		try {	
//			f = taTiersService.exportToCSV(values);
			stream = new FileInputStream(f); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		exportation = new DefaultStreamedContent(stream,null,"tiers.csv");
		
		return exportation;
	}
	
	public void actImprimer(ActionEvent actionEvent) {
//		if(ConstWeb.DEBUG) {
//			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("remise", "actImprimer")); 
//		}
//		try {
//
//			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//			Map<String, Object> sessionMap = externalContext.getSessionMap();
//
//			Map<String,Object> mapEdition = new HashMap<String,Object>();
//
//			TaRelance doc = taRelanceService.findById(selection.getDto().getId());
////			doc.setTaTPaiement(doc.getTaTPaiement());
//
//			mapEdition.put("doc",doc );
//
//			mapEdition.put("taInfoEntreprise", taInfoEntrepriseService.findInstance());
//
//
////			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
////			List<TaPreferences> values= taPreferencesService.findByGroupe("remise");
////			mapParametreEdition.put("preferences", values);
////			mapEdition.put("param", mapParametreEdition);
//
//			sessionMap.put("edition", mapEdition);
//
//			System.out.println("RemiseController.actImprimer()");
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
	}
	
	public void actImprimerListeDesRelances(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeDesRelances", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	} 

	public void actSupprimerLigne(){
		try{
//			if(verifSiEstModifiable()) {
//				actModifier(null);
//				if(selectionLigne!=null){
//					valuesLignes.remove(selectionLigne);
//					TaLRelance l=rechercheLRelance(selectionLigne);
//					if(l!=null){
//						taRelance.getTaLRelances().remove(l);
//					}
//				}
//
//				if(ConstWeb.DEBUG) {
//					FacesContext context = FacesContext.getCurrentInstance();  
//					context.addMessage(null, new FacesMessage("remise", "actSupprimerLigne"));
//				}
//			}
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
	
	public void actExporter(ActionEvent actionEvent){
		actEnregistrer(null);
	}
	
	public void actEnregistrer(){
		actEnregistrer(null);
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			if(taRelance!=null && (taRelance.getIdRelance()==null || taRelance.getIdRelance()==0)){
	//			taRelance=new TaRelance();
	////			mapperUIToModel.map(selection.getDto(), taRelance);
	//			taRelance.setCodeRelance(selection.getDto().getCodeRelance());
	////			taRelance.setLibelleDocument(selection.getDto().getLibelleDocument());
	//			taRelance.setDateRelance(selection.getDto().getDateRelance());
				taRelance.setTaLRelances(new ArrayList<TaLRelance>());
				
				for (String codeNiveau : mapRelanceParNiveau.keySet()) {
					if(mapRelanceParNiveau.get(codeNiveau).getListeSelectionRelance()!=null) {
						for (TaLRelanceDTOJSF taLRelanceDTOJSF : mapRelanceParNiveau.get(codeNiveau).getListeSelectionRelance()) {
							taRelance.getTaLRelances().add(taLRelanceDTOJSF.getTaLRelance());
							taLRelanceDTOJSF.getTaLRelance().setTaRelance(taRelance);
						}
					}
				}
			}else if(taRelance!=null && taRelance.getIdRelance()!=null){
				//déjà existant
//				taRelance=taRelanceService.findById(selection.getDto().getId());
//				mapperUIToModel.map(selection.getDto(), taRelance);
			}
//			old=selection;
			taRelance = taRelanceService.enregistrerMerge(taRelance);
//			taRelanceService.annuleCode(selection.getDto().getCodeRelance());
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			refresh();
//			valuesLignes = IHMmodel();
//			if(old!=null){
//				selection=recherche(old);
//			}

		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Relance", e.getMessage()));
		}
	}
	
	public TaLRelance rechercheLRelance(TaLRelanceDTOJSF courant){
//		for (TaLRelance ligne : taRelance.getTaLRelances()) {
//			if(courant.getDto().getCodeReglement().equals(ligne.getTaReglement().getCodeDocument()))
//				return ligne;
//		}
		return null;
	}
	
//	public TaRelanceDTOJSF recherche(TaRelanceDTOJSF courant){
//		for (TaRelanceDTOJSF taRReglementDTOJSF : values) {
//			if(courant.getDto().getCodeRelance().equals(taRReglementDTOJSF.getDto().getCodeRelance()))
//				return taRReglementDTOJSF;
//		}
//		return courant;
//	}
	
	public void actAnnuler(ActionEvent actionEvent){
		if(selection!=null){
//			taRelanceService.annuleCode(selection.getDto().getCodeRelance());

			TaRelance old = selection;
			selection=null;
			
			if(values!=null && !values.isEmpty()) {
				selection = values.get(0);
			}
			onRowSelect(null);
//			if(old!=null && old.getIdRelance()!=null && old.getIdRelance()!=0)
//				selection = values.get(0);
////			nouveau=selection;
		}
//		afficheCritere=false;
//		afficheTitre();
		valuesLignes = IHMmodel();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
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
	
	public void onRowSelect(SelectEvent event) { 
		try{
			LgrTab b = new LgrTab();
			b.setTypeOnglet(LgrTab.TYPE_TAB_RELANCE);
			b.setTitre("Relances");
			b.setTemplate("relance/relance.xhtml");
			b.setIdTab(LgrTab.ID_TAB_RELANCE);
			b.setStyle(LgrTab.CSS_CLASS_TAB_RELANCE);
			tabsCenter.ajouterOnglet(b);
			tabViews.selectionneOngletCentre(b);
//			afficheCritere=false;
			
//			taRelance = taRelanceService.findById(selection.getIdRelance());
			
//			refreshLigne();
			
			scrollToTop();
			updateTab();

		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void updateTab(){
		try {		
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			

//			if(selectedDocumentDTOs!=null && selectedDocumentDTOs.length>0) {
//				selectedDocumentDTO = selectedDocumentDTOs[0];
//			}
//			if(selectedDocumentDTO.getId()!=null && selectedDocumentDTO.getId()!=0) {
//				taRelance = taDocumentService.findById(selectedDocumentDTO.getId());
//			}
			if(selection.getIdRelance()!=null && selection.getIdRelance()!=0) {
				taRelance = taRelanceService.findById(selection.getIdRelance());
			}
			//masterEntity.calculeTvaEtTotaux(); // pour que tous les totaux en transient soient remplis aussi
			codeRelance = taRelance.getCodeRelance();
			valuesLignes = IHMmodel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<TaLRelanceDTOJSF> IHMmodel() {
		LinkedList<TaLRelance> ldao = new LinkedList<TaLRelance>();
		LinkedList<TaLRelanceDTOJSF> lswt = new LinkedList<TaLRelanceDTOJSF>();

		mapRelanceParNiveau = new HashMap<>();
		
		if(taRelance!=null && !taRelance.getTaLRelances().isEmpty()) {

			ldao.addAll(taRelance.getTaLRelances());

			LgrDozerMapper<TaLRelance,TaLRelanceDTO> mapper = new LgrDozerMapper<TaLRelance,TaLRelanceDTO>();
			TaLRelanceDTO dto = null;
			for (TaLRelance o : ldao) {
				TaLRelanceDTOJSF t = new TaLRelanceDTOJSF();
				dto = (TaLRelanceDTO) mapper.map(o, TaLRelanceDTO.class);
				
				t.setTaLRelance(o);
				
				if(mapRelanceParNiveau.get(o.getTaTRelance().getCodeTRelance())==null) {
					mapRelanceParNiveau.put(o.getTaTRelance().getCodeTRelance(), new TabNiveauRelanceJSF(taTiersService));
				} 
				mapRelanceParNiveau.get(o.getTaTRelance().getCodeTRelance()).getListeRelance().add(t);
				
//				dto.setEmplacement(o.getEmplacementLDocument());
//				t.setDto(dto);
//				t.setTaLot(o.getTaLot());
//				if(t.getArticleLotEntrepotDTO()==null) {
//					t.setArticleLotEntrepotDTO(new ArticleLotEntrepotDTO());
//				}
//				if(o.getTaLot()!=null) {
//					t.getArticleLotEntrepotDTO().setNumLot(o.getTaLot().getNumLot());
//				}
//				t.setTaArticle(o.getTaArticle());
//
//				if(o.getTaArticle()!=null){
//					t.setTaRapport(o.getTaArticle().getTaRapportUnite());
//				}
//				t.setTaEntrepot(o.getTaEntrepot());
//				if(o.getU1LDocument()!=null) {
//					t.setTaUnite1(new TaUnite());
//					t.getTaUnite1().setCodeUnite(o.getU1LDocument());
//				}
//				if(o.getU2LDocument()!=null) {
//					t.setTaUnite2(new TaUnite());
//					t.getTaUnite2().setCodeUnite(o.getU2LDocument());
//				}
//				if(o.getCodeTitreTransport()!=null) {
//					t.setTaTitreTransport(new TaTitreTransport());
//					t.getTaTitreTransport().setCodeTitreTransport(o.getCodeTitreTransport());
//				}
//				t.updateDTO(false);
				lswt.add(t);
			}

		}
		return lswt;
	}
	
	public void onRowSelectLigne(SelectEvent event) { 
		initTotaux();
	}
	
	public void onRowUnSelectLigne(UnselectEvent event) { 
		initTotaux();
	}

	public void refreshLigne(){
		try {
//			valuesLignes.clear();
//
//			if(selection!=null && selection.getDto()!=null && selection.getDto().getId()!=0){
//				//récupérer les lignes de la remise
//				List<TaLRelanceDTO> liste = taLRelanceServiceRemote.RechercheLigneRemiseDTO(selection.getDto().getCodeRelance());
//				for (TaLRelanceDTO taLRelanceDTO : liste) {
//					TaLRelanceDTOJSF obj = new TaLRelanceDTOJSF();
////					if(obj.getDto().getCodeTPaiement()!=null){
////						taTPaiment=taTPaiementService.findByCode(obj.getDto().getCodeTPaiement());
////					}
////					if(obj.getDto().getIdCompteBanque()!=null){
////						taCompteBanque=taCompteBanqueService.findById(obj.getDto().getIdCompteBanque());
////					}					
//					obj.setDto(taLRelanceDTO);
//					valuesLignes.add(obj);
//				}
//			}
//			if(valuesLignes!=null && !valuesLignes.isEmpty())selectionLigne=valuesLignes.get(0);
//			selectionLignes=rempliSelection(valuesLignes);
//
//			nouvelleLigne=selectionLigne;
//			initTotaux();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void refresh(){
		try {
		
		values = taRelanceService.selectAll();
//		selectionLignes = new ArrayList<>();
		valuesLignes = IHMmodel();
		//remplir values
	
//		List<TaRelanceDTO> liste= taRelanceService.rechercheDocumentDTO(dateDeb, dateFin);
//		for (TaRelanceDTO taRemiseDTO : liste) {
//			TaRelanceDTOJSF obj = new TaRelanceDTOJSF();
//			obj.setDto(taRemiseDTO);
//
//			values.add(obj);
//		}
//
//		if(values!=null && !values.isEmpty())selection=values.get(0);
//		if(old!=null){
//			selection=recherche(old);
//		}
////		nouveau=selection;
//		afficheCritere=false;
//		afficheTitre();
//		refreshLigne();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actFermer(ActionEvent actionEvent) {
//		if(selection!=null && selection.getDto()!=null){
//			taRelanceService.annuleCode(selection.getDto().getCodeRelance());
//		}
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
//				if(value!=null && value instanceof TaCompteBanque){
//					TaCompteBanque obj=(TaCompteBanque)value;
//					taCompteBanque=obj;
//				}else if(value==null){
//					taCompteBanque=null;
//				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<TaLRelanceDTOJSF> getValuesLignes() {
		return valuesLignes;
	}

	public void setValuesLignes(List<TaLRelanceDTOJSF> valuesLignes) {
		this.valuesLignes = valuesLignes;
	}

	public TaLRelanceDTOJSF getNouvelleLigne() {
		return nouvelleLigne;
	}

	public void setNouvelleLigne(TaLRelanceDTOJSF nouvelleLigne) {
		this.nouvelleLigne = nouvelleLigne;
	}

	public TaLRelanceDTOJSF getSelectionLigne() {
		return selectionLigne;
	}

	public void setSelectionLigne(TaLRelanceDTOJSF selectionLigne) {
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

	public List<TaLRelanceDTOJSF> getSelectionLignes() {
		return selectionLignes;
	}

	public void setSelectionLignes(List<TaLRelanceDTOJSF> selectionLignes) {
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

	public void actSelectLigne( ToggleSelectEvent event){
		initTotaux();
	}
	
	public void initTotaux() {
		nbLigne = 0;
		totalTTC = BigDecimal.valueOf(0);
//		if(selection.getIdRelance()==null ||selection.getIdRelance()==0){
//			for (TaLRelanceDTOJSF ligne : selectionLignes) {
//				nbLigne++;
//				totalTTC=totalTTC.add(ligne.getTaLRelance().getNetTTC());
//			}
//		}else{
			for (TaLRelanceDTOJSF ligne : valuesLignes) {
				nbLigne++;
				totalTTC=totalTTC.add(ligne.getTaLRelance().getNetTTC());
			}	
//		}
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

	

	public TaRelance getTaRelance() {
		return taRelance;
	}

	public void setTaRelance(TaRelance taRemise) {
		this.taRelance = taRemise;
	}

//	public TaRelanceDTOJSF rempliDTO(){
//		if(taRelance!=null){			
//			try {
//				taRelance = taRelanceService.findByCode(taRelance.getCodeRelance());
//				selection=new TaRelanceDTOJSF();
//				mapperModelToUI.map(taRelance, selection.getDto());
//				if(values.contains(selection))values.add(selection);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return selection;
//	}
	


	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public String getCodeTiers() {
		return codeTiers;
	}

	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

	public String getLimite() {
		return limite;
	}

	public void setLimite(String limite) {
		this.limite = limite;
	}

	public String getCodeRelance() {
		return codeRelance;
	}

	public void setCodeRelance(String codeRelance) {
		this.codeRelance = codeRelance;
	}

	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}

	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}

	public boolean isTousLesTiers() {
		return tousLesTiers;
	}

	public void setTousLesTiers(boolean tousLesTiers) {
		this.tousLesTiers = tousLesTiers;
	}

	public List<TaTRelance> getListeNiveauRelance() {
		return listeNiveauRelance;
	}

	public void setListeNiveauRelance(List<TaTRelance> listeNiveauRelance) {
		this.listeNiveauRelance = listeNiveauRelance;
	}

	public List<TaRelance> getValues() {
		return values;
	}

	public void setValues(List<TaRelance> values) {
		this.values = values;
	}

	public TaRelance getSelection() {
		return selection;
	}

	public void setSelection(TaRelance selection) {
		this.selection = selection;
	}

	public Map<String, TabNiveauRelanceJSF> getMapRelanceParNiveau() {
		return mapRelanceParNiveau;
	}

	public void setMapRelanceParNiveau(Map<String, TabNiveauRelanceJSF> mapRelanceParNiveau) {
		this.mapRelanceParNiveau = mapRelanceParNiveau;
	}

}
