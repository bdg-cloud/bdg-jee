package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.primefaces.component.api.UIData;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaAdresseDTO;
import fr.legrain.tiers.dto.TaTAdrDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class AdresseController extends AbstractController implements Serializable { 
	
	private String googleMapApiUrlAndKey = "https://maps.google.com/maps/api/js?sensor=false&key=";

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	@Inject private AdresseReorderController adresseReorderController;
	
	private String paramId;
	
	private TabView tabViewTiers; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaAdresseDTO> values; 
	private List<TaAdresseDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaTAdrServiceRemote taTTiersService;

	private @EJB ITaAdresseServiceRemote taAdresseService;
	private @EJB ITaTAdrServiceRemote taTAdresseService;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	private TaAdresseDTO[] selectedTaAdresseDTOs; 
	private TaAdresse taAdresse = new TaAdresse();
	private TaAdresseDTO newTaAdresseDTO = new TaAdresseDTO();
	private TaAdresseDTO selectedTaAdresseDTO = new TaAdresseDTO();
//	private TaAdresseDTO oldSelectedTaAdresseDTO = new TaAdresseDTO();
	
	private TaTAdr taTTiers;
	
	private LgrDozerMapper<TaAdresseDTO,TaAdresse> mapperUIToModel  = new LgrDozerMapper<TaAdresseDTO,TaAdresse>();
	private LgrDozerMapper<TaAdresse,TaAdresseDTO> mapperModelToUI  = new LgrDozerMapper<TaAdresse,TaAdresseDTO>();
	
	private TaTiers masterEntity;
	//private TiersController masterController;
	private MapModel mapModel;
//	private String centerGeoMap = "41.850033, -87.6500523";
	private String centerGeoMap = "47.7657365, 9.6082103";
	private String zoomGeoMap = "4";

	private TaTAdrDTO taTAdrDTO;
	
	public AdresseController() {  
		//values = getValues();
	}  

	@PostConstruct
	public void postConstruct(){
		try {
			taParametreService.initParamBdg();
			googleMapApiUrlAndKey = googleMapApiUrlAndKey+=URLEncoder.encode(paramBdg.getTaParametre().getGoogleMapApikey(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refresh(null);
		mapModel = new DefaultMapModel();
	}

	public void refresh(ActionEvent ev){
		selectedTaAdresseDTO=null;
		 
   	    if(masterEntity!=null) {
   	    	try {
				if(masterEntity.getIdTiers()!=0) masterEntity = taTiersService.findById(masterEntity.getIdTiers());
			} catch (FinderException e) {
				
			}
			values = new ArrayList<>();
			for (TaAdresse taAdresse : masterEntity.getTaAdresses()) {
				TaAdresseDTO dto=new TaAdresseDTO();
				if(masterEntity.getTaAdresse().equals(taAdresse)){
					dto.setDefaut(true);
				}
				mapperModelToUI.map(taAdresse, dto);
				values.add(dto);
			}

			if(!values.isEmpty()&&selectedTaAdresseDTO==null)
				selectedTaAdresseDTO=values.get(0);
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaAdresseDTO> getValues(){  
		return values;
	}
	
	public void onGeocode(GeocodeEvent event) {
		List<GeocodeResult> results = event.getResults();
		mapModel = new DefaultMapModel();
        
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            centerGeoMap = center.getLat() + "," + center.getLng();
            zoomGeoMap = "15";
             
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
               // mapModel.
                mapModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
            }
        }
	}

	public void actAnnuler(ActionEvent actionEvent) {
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				
				taAdresse = new TaAdresse();
				mapperModelToUI.map(taAdresse,selectedTaAdresseDTO );
				selectedTaAdresseDTO=new TaAdresseDTO();
				
				if(!values.isEmpty()) selectedTaAdresseDTO = values.get(0);
				onRowSelect(null);
				break;
			case C_MO_EDITION:
				if(selectedTaAdresseDTO.getId()!=null && selectedTaAdresseDTO.getId()!=0){
					taAdresse = taAdresseService.findById(selectedTaAdresseDTO.getId());
					selectedTaAdresseDTO=taAdresseService.findByIdDTO(selectedTaAdresseDTO.getId());
				}				
				break;

			default:
				break;
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			mapperModelToUI.map(taAdresse, selectedTaAdresseDTO);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Tiers", "actAnnuler"));
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void autoCompleteMapUIToDTO() {
		if(taTAdrDTO!=null) {
			validateUIField(Const.C_CODE_T_ADR,taTAdrDTO.getCodeTAdr());
			selectedTaAdresseDTO.setCodeTAdr(taTAdrDTO.getCodeTAdr());
		} else {
			selectedTaAdresseDTO.setCodeTAdr(null);
		}
		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {
			taTAdrDTO = null;
			if(selectedTaAdresseDTO!=null){
				if(selectedTaAdresseDTO.getCodeTAdr()!=null && !selectedTaAdresseDTO.getCodeTAdr().equals("")) {
					taTAdrDTO=taTAdresseService.findByCodeDTO(selectedTaAdresseDTO.getCodeTAdr());
				}	
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaAdresse rechercheAdresseDansTiers(TaAdresseDTO dto){
		for (TaAdresse adr : masterEntity.getTaAdresses()) {
			if(adr.getIdAdresse()==dto.getId()) {
				return adr;
			}
		}
		return null;
	}
	
	public void onRowReorder(ReorderEvent event) {
		int i=1;
		TaAdresse adr;
        for (TaAdresseDTO obj : values) {
			obj.setOrdre(i);
			adr=rechercheAdresseDansTiers(obj);
			adr.setOrdre(i);
			i++;
		}
        modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	

	
	public void actReorder(UIData dt) {
		actModifier();
		int i=1;
		TaAdresse adr;
        DataTable table = (DataTable) dt;
        ArrayList<TaAdresseDTO> liste= (ArrayList<TaAdresseDTO>) table.getValue();
        for (Object obj : liste) {
			((TaAdresseDTO)obj).setOrdre(i);
			adr=rechercheAdresseDansTiers(((TaAdresseDTO)obj));
			adr.setOrdre(i);
			i++;
		}
        //recharger le tiers
//        try {
//        	masterEntity=taTiersService.merge(masterEntity);
//			masterEntity=taTiersService.findById(masterEntity.getIdTiers());
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        refresh();
	}
	

	
	public void actEnregistrer(ActionEvent actionEvent) {		
		//TaAdresse taAdresse = new TaAdresse();

		autoCompleteMapUIToDTO();
//		initAdresseNull();
				
		mapperUIToModel.map(selectedTaAdresseDTO, taAdresse);
		
		/*
		 * A faire, mapper les objets dans taAdresse avant le merge :
		 * mapping de tous les objets avec des "codes", typeTiers, typeCvilite, ...
		 * tous les objets imbriqués par défaut, adresse défaut, email défaut, ...
		 * 
		 * Pour les converter, essayé de faire fonctionner les injections via omniface
		 * ou, récupérer dans le client RCP le système pour générer les chaines JNDI
		 * 
		 * Ma questions sur les forums
		 * http://forum.primefaces.org/viewtopic.php?f=3&t=40677&sid=fc4d0075c1e57d0433bd8a8d3bdf0393
		 * http://stackoverflow.com/questions/27551615/primefaces-autocomplete-using-a-list-pojo-and-a-string-as-default-value
		 * 
		 */
		
		try {
			//si défaut coché rentre dans tatiers
			if(selectedTaAdresseDTO.isDefaut())masterEntity.setTaAdresse(taAdresse);
			taAdresse.setTaTiers(masterEntity);
			masterEntity.addAdresse(taAdresse);	
			
			//si null on met par défaut Fact
			if(taAdresse.getTaTAdr()==null){
				TaTAdr tAdr= taTAdresseService.findByCode(Const.C_TYPEADRESSEFACTURATION);
				taAdresse.setTaTAdr(tAdr);
			}
			
			int ordre=taAdresseService.rechercheOdrePourType(taAdresse.getTaTAdr().getCodeTAdr(),masterEntity.getCodeTiers());
			if(taAdresse.getOrdre()==null || taAdresse.getOrdre()==0)taAdresse.setOrdre(ordre);
			
			taAdresse = taAdresseService.merge(taAdresse,ITaTiersServiceRemote.validationContext);
			
			mapperModelToUI.map(taAdresse, selectedTaAdresseDTO);
			autoCompleteMapDTOtoUI();
			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				values.add(selectedTaAdresseDTO);
			}

			masterEntity = taTiersService.findById(masterEntity.getIdTiers());
//			if(selectedTaAdresseDTO.isDefaut()) masterEntity=taTiersService.merge(masterEntity);
//			refresh(null);

			adresseReorderController.setMasterEntity(masterEntity);
			adresseReorderController.refresh();
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
			selectedTaAdresseDTO = new TaAdresseDTO();
			taAdresse = new TaAdresse();
//			masterEntity.addAdresse(taAdresse);
			String codeTiersDefaut = "FACT";

//			selectedTaAdresseDTO.setCodeTAdr(codeTiersDefaut);

//			selectedTaAdresseDTO.setCodeCompta(selectedTaAdresseDTO.getCodeTiers()); //ejb
//			validateUIField(Const.C_COMPTE,selectedTaAdresseDTO.getCompte()); //ejb

			//TaTAdrDAO daoTTiers = new TaTAdrDAO(getEm());
			TaTAdr taTTiers;

			taTTiers = taTTiersService.findByCode(codeTiersDefaut);
			
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_ADRESSE_ADMINISTRATIF_DEFAUT)) {
//				taAdresse.setCommAdministratifAdresse(1);
//			} else {
				taAdresse.setCommAdministratifAdresse(0);
//			}
			
//			if(TiersPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.CORRESPONDANCE_ADRESSE_COMMERCIAL_DEFAUT)) {
//				taAdresse.setCommCommercialAdresse(1);
//			} else {
				taAdresse.setCommCommercialAdresse(0);
//			}
			taAdresse.setPaysAdresse("FRANCE");
			
			
			autoCompleteMapDTOtoUI();
			
			modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
			scrollToTop();
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Tiers", "actInserer"));
			}

		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {
		
		try {
			//taAdresse = taAdresseService.findById(selectedTaAdresseDTO.getId());
			for (TaAdresse adr : masterEntity.getTaAdresses()) {
				if(adr.getIdAdresse()==selectedTaAdresseDTO.getId()) {
					taAdresse = adr;
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
		TaAdresse taAdresse = new TaAdresse();
		try {
			if(selectedTaAdresseDTO!=null && selectedTaAdresseDTO.getId()!=null){
				taAdresse = taAdresseService.findById(selectedTaAdresseDTO.getId());
			}

			taAdresseService.remove(taAdresse);
			values.remove(selectedTaAdresseDTO);
			
			if(!values.isEmpty()) {
				selectedTaAdresseDTO = values.get(0);
			}else {
				selectedTaAdresseDTO=new TaAdresseDTO();
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
		selectedTaAdresseDTO=new TaAdresseDTO();
		selectedTaAdresseDTOs=new TaAdresseDTO[]{selectedTaAdresseDTO};
	
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
		sessionMap.put("tiers", taTiersService.findById(selectedTaAdresseDTO.getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaAdresseDTO.getId()));
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
//		taTiersService.generePDF(selectedTaAdresseDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaAdresseDTO.getId()));
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
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaAdresseDTO.getCodepostalAdresse()
				)); }
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_TIERS);
		}
	} 
	public void updateTab(){
		try {

//			if(selectedTaAdresseDTOs!=null && selectedTaAdresseDTOs.length>0) {
//				selectedTaAdresseDTO = selectedTaAdresseDTOs[0];
//			}
//			if(selectedTaAdresseDTO.getId()!=null && selectedTaAdresseDTO.getId()!=0) {
//				taAdresse = taAdresseService.findById(selectedTaAdresseDTO.getId());
//			}
//			mapperModelToUI.map(taAdresse, selectedTaAdresseDTO);			
			autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
//		LgrTab b = new LgrTab();
//		b.setTypeOnglet(LgrTab.TYPE_TAB_TIERS);
//		//b.setTitre("Tiers "+selectedTaAdresseDTO.getCodeTiers());
//		b.setTitre("Tiers");
//		b.setTemplate("tiers/tiers.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_TIERS);
//		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
//		b.setParamId(LibConversion.integerToString(selectedTaAdresseDTO.getId()));
//
//		tabsCenter.ajouterOnglet(b);
//		tabViews.selectionneOngletCentre(b);
//		
//		
		updateTab();
//		scrollToTop();
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Tiers", 
				"Chargement du tiers N°"+selectedTaAdresseDTO.getCodepostalAdresse()
				)); 
		}
	} 
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actDialogTypeAdresse(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_adresse", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	
	public void handleReturnDialogTypeAdresse(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			taTTiers = (TaTAdr) event.getObject();
			try {
				taTAdrDTO = taTTiersService.findByCodeDTO(taTTiers.getCodeTAdr());
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	
	public void handleReturnDialogAdresseReorder(SelectEvent event) {
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
				if(selectedTaAdresseDTO!=null && selectedTaAdresseDTO.getId()!=null  && selectedTaAdresseDTO.getId()!=0 ) retour = false;
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

	public void validateAdresse(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {

		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaAdresseDTO>> violations = factory.getValidator().validateValue(TaAdresseDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaAdresseDTO> cv : violations) {
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
//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
//			if(value instanceof TaTEntiteDTO && ((TaTEntiteDTO) value).getCodeTEntite()!=null && ((TaTEntiteDTO) value).getCodeTEntite().equals(Const.C_AUCUN))value=null;	
//			if(value instanceof TaTCiviliteDTO && ((TaTCiviliteDTO) value).getCodeTCivilite()!=null && ((TaTCiviliteDTO) value).getCodeTCivilite().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaCPaiementDTO && ((TaCPaiementDTO) value).getCodeCPaiement()!=null && ((TaCPaiementDTO) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaTPaiementDTO && ((TaTPaiementDTO) value).getCodeTPaiement()!=null && ((TaTPaiementDTO) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTAdrDTO && ((TaTAdrDTO) value).getCodeTAdr()!=null && ((TaTAdrDTO) value).getCodeTAdr().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaCPaiement && ((TaCPaiement) value).getCodeCPaiement()!=null && ((TaCPaiement) value).getCodeCPaiement().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaTPaiement && ((TaTPaiement) value).getCodeTPaiement()!=null && ((TaTPaiement) value).getCodeTPaiement().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaTTvaDoc && ((TaTTvaDoc) value).getCodeTTvaDoc()!=null && ((TaTTvaDoc) value).getCodeTTvaDoc().equals(Const.C_AUCUN))value=null;
//			if(value instanceof TaFamilleTiers && ((TaFamilleTiers) value).getCodeFamille()!=null && ((TaFamilleTiers) value).getCodeFamille().equals(Const.C_AUCUN))value=null;
		}
		
		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {
		
		boolean changement=false;
		
		try {				
			if(nomChamp.equals(Const.C_CODE_T_ADR)) {
				TaTAdr entity = null;
				if(value!=null){
					if(value instanceof TaTAdr){
						entity=(TaTAdr) value;
	//								entity = taTTiersService.findByCode(entity.getCodeTTiers());
					} else if(value instanceof TaTAdrDTO){
							entity = taTAdresseService.findByCode(((TaTAdrDTO) value).getCodeTAdr());
					}else{
						entity = taTAdresseService.findByCode((String)value);
					}
				} else {
					selectedTaAdresseDTO.setCodeTAdr("");
					taTAdrDTO.setCodeTAdr("");
					taTTiers=null;
				}						
				taAdresse.setTaTAdr(entity);
			} 
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean adresseEstRempli() {
		boolean retour=false;
		if(selectedTaAdresseDTO.getAdresse1Adresse()!=null && !selectedTaAdresseDTO.getAdresse1Adresse().equals(""))return true;
		if(selectedTaAdresseDTO.getAdresse2Adresse()!=null && !selectedTaAdresseDTO.getAdresse2Adresse().equals(""))return true;
		if(selectedTaAdresseDTO.getAdresse3Adresse()!=null && !selectedTaAdresseDTO.getAdresse3Adresse().equals(""))return true;
		if(selectedTaAdresseDTO.getCodepostalAdresse()!=null && !selectedTaAdresseDTO.getCodepostalAdresse().equals(""))return true;
		if(selectedTaAdresseDTO.getVilleAdresse()!=null && !selectedTaAdresseDTO.getVilleAdresse().equals(""))return true;
		if(selectedTaAdresseDTO.getPaysAdresse()!=null && !selectedTaAdresseDTO.getPaysAdresse().equals(""))return true;
		return retour;
	}
	
	public List<TaTAdrDTO> typeAdresseAutoCompleteLight(String query) {
        List<TaTAdrDTO> allValues = taTAdresseService.selectAllDTO();
        List<TaTAdrDTO> filteredValues = new ArrayList<TaTAdrDTO>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaTAdrDTO civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeTAdr().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public TaAdresseDTO[] getSelectedTaAdresseDTOs() {
		return selectedTaAdresseDTOs;
	}

	public void setSelectedTaAdresseDTOs(TaAdresseDTO[] selectedTaAdresseDTOs) {
		this.selectedTaAdresseDTOs = selectedTaAdresseDTOs;
	}

	public TaAdresseDTO getNewTaAdresseDTO() {
		return newTaAdresseDTO;
	}

	public void setNewTaAdresseDTO(TaAdresseDTO newTaAdresseDTO) {
		this.newTaAdresseDTO = newTaAdresseDTO;
	}

	public TaAdresseDTO getSelectedTaAdresseDTO() {
		return selectedTaAdresseDTO;
	}

	public void setSelectedTaAdresseDTO(TaAdresseDTO selectedTaAdresseDTO) {
		this.selectedTaAdresseDTO = selectedTaAdresseDTO;
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

	public TaTAdr getTaTAdr() {
		return taTTiers;
	}

	public void setTaTAdr(TaTAdr taTTiers) {
		this.taTTiers = taTTiers;
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
	
	public TaTAdrDTO getTaTAdrDTO() {
		return taTAdrDTO;
	}

	public void setTaTAdrDTO(TaTAdrDTO taTAdrDTO) {
		this.taTAdrDTO = taTAdrDTO;
	}

	public TaAdresse getTaTiers() {
		return taAdresse;
	}

	public void setTaTiers(TaAdresse taAdresse) {
		this.taAdresse = taAdresse;
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

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public String getGoogleMapApiUrlAndKey() {
		return googleMapApiUrlAndKey;
	}

	public void setGoogleMapApiUrlAndKey(String googleMapApiUrlAndKey) {
		this.googleMapApiUrlAndKey = googleMapApiUrlAndKey;
	}

	public String getCenterGeoMap() {
		return centerGeoMap;
	}

	public void setCenterGeoMap(String centerGeoMap) {
		this.centerGeoMap = centerGeoMap;
	}

	public String getZoomGeoMap() {
		return zoomGeoMap;
	}

	public void setZoomGeoMap(String zoomGeoMap) {
		this.zoomGeoMap = zoomGeoMap;
	}

	public List<TaAdresseDTO> getValuesFiltered() {
		return valuesFiltered;
	}

	public void setValuesFiltered(List<TaAdresseDTO> valuesFiltered) {
		this.valuesFiltered = valuesFiltered;
	}

	public AdresseReorderController getAdresseReorderController() {
		return adresseReorderController;
	}

	public void setAdresseReorderController(AdresseReorderController adresseReorderController) {
		this.adresseReorderController = adresseReorderController;
	}





//	public void actEnregistrerReorder(){
//		int i=0;
//		for (TaAdresseDTO taAdresseDTO : values) {
//			taAdresseDTO.setOrdre(i);
//			i++;
//		}
//		for (TaAdresseDTO taAdresseDTO : values) {
//			try {
//				TaAdresse adresse = taAdresseService.findById(taAdresseDTO.getId());
//				adresse.setOrdre(taAdresseDTO.getOrdre());
//				taAdresseService.merge(adresse);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		refresh();
//	}
}  
