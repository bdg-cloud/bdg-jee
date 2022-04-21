package fr.legrain.bdg.webapp.multi_tarifs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
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
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaFamilleTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaRemise;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaFamilleTiersDTO;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTTarif;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped  
public class RemplacementGrilleTarifaireTiersController extends AbstractController{

	@Inject @Named(value="tabListModelCenterBean")
	protected TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	protected TabViewsBean tabViews;
	private boolean cellEdit = false;

	private List<TaGrilleTarifTiersDTOJSF> valuesLignes=new LinkedList<>();
	private List<TaGrilleTarifTiersDTOJSF> filteredValuesLignes=new LinkedList<>();
	
//	private List<TaGrilleTarifTiersDTOJSF> valuesLignesListe=new LinkedList<>();

	
	private TaGrilleTarifTiersDTOJSF nouvelleLigne ;
	private TaGrilleTarifTiersDTOJSF selectionLigne  ;
	private TaGrilleTarifTiersDTOJSF oldSelectionLigne  ;
	
	private TaGrilleTarifTiersDTOJSF[] selectionLignes ;
	
	TaTTarifDTO taTTarifDTO;
	TaTTarifDTO taTTarifRemplacementDTO;
	TaTiersDTO taTiersDTO;
	TaFamilleTiersDTO taFamilleDTO;
	
	private boolean saisieHT=true;
	private boolean saisieDetail=false;
	private String titreSaisieDetail="Saisie en détail";
	private String titreSaisieRapide="Saisie rapide";
	
	private Date dateDeb=null;
	private Date dateFin=null;	

	TaInfoEntreprise infos =null;
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	
	private String titreListeComplete="Liste complète des prix de référence";
	private String titreListeFiltree="Liste filtrée des prix de référence";
	private String titreListe=titreListeComplete;
	private String titlePartOne="Grille de référence";
	private String titlePartTwo="";
	private int nbMaxLigne=20;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private Boolean afficheCritere=false;
	
	private String titreMessage="Etes vous sur de vouloir enlever les grilles tarifaires sur cette sélection de tiers ?";
	private String titreSaisieHt="Saisie des prix en HT";
	private String titreSaisieTtc="Saisie des prix en TTC";
	private String titreEtape1="Etape 1/3 : Saisissez les critères de sélection des grilles tarifaires ou familles de tiers";
	private String titreEtape2="Etape 2/3 : selectionner dans la liste, les tiers pour lesquels vous souhaitez attribuer une nouvelle grille tarifaire";
	private String titreEtape3="";
	private String titreEcran=titreEtape2;
	
	Integer nbLigne=0;
	BigDecimal totalTTC=BigDecimal.valueOf(0);
	

	private @EJB  ITaInfoEntrepriseServiceRemote TaInfoEntrepriseService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB ITaTiersServiceRemote taTiersService;
	@EJB ITaTTarifServiceRemote taTTarifService;
	@EJB ITaFamilleTiersServiceRemote taFamilleService;
	
	
	private LgrDozerMapper<TaRemiseDTO,TaRemise> mapperUIToModel  = new LgrDozerMapper<TaRemiseDTO,TaRemise>();
	private LgrDozerMapper<TaRemise,TaRemiseDTO> mapperModelToUI  = new LgrDozerMapper<TaRemise,TaRemiseDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {

			infos=TaInfoEntrepriseService.findInstance();

			dateDeb=infos.getDatedebInfoEntreprise();
			dateFin=infos.getDatefinInfoEntreprise();
			
			
//			refreshListOverlay();


		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValuesLignes(valuesLignes);
	}
	
	
	public RemplacementGrilleTarifaireTiersController() {  
	}  


	public String titreSaisieRapideOuDetail() {
		if(isSaisieDetail())return titreSaisieDetail;
		else return titreSaisieRapide;
	}
	
	public String titreSaisie() {
		if(isSaisieHT())return titreSaisieHt;
		else return titreSaisieTtc;
	}
	
	public List<TaFamilleTiersDTO> familleAutoComplete(String query) {
		List<TaFamilleTiersDTO> allValues = taFamilleService.selectAllDTO();
		List<TaFamilleTiersDTO> filteredValues = new ArrayList<TaFamilleTiersDTO>();
		TaFamilleTiersDTO cp = new TaFamilleTiersDTO();
		cp.setCodeFamille(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeFamille().toLowerCase().contains(query.toLowerCase())) {
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
			if(query.equals("*") || cp.getCodeTiers().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	public List<TaTTarifDTO> tTarifAutoComplete(String query) {
		List<TaTTarifDTO> allValues = taTTarifService.selectAllDTO();
		List<TaTTarifDTO> filteredValues = new ArrayList<TaTTarifDTO>();
		TaTTarifDTO cp = new TaTTarifDTO();
		cp.setCodeTTarif(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTTarif().toLowerCase().contains(query.toLowerCase())) {
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
		}
		
		if(!nomChamp.equals(Const.C_CODE_T_TARIF_REMPLACEMENT))
			reset();
		if(nomChamp.equals(Const.C_CODE_T_TARIF_REMPLACEMENT)) {
			if(taTTarifRemplacementDTO!=null) {
				titreMessage="Etes vous sur de vouloir enregistrer la grille tarifaire <"+taTTarifRemplacementDTO.getCodeTTarif()+"> sur cette sélection de tiers ?";
			}else {
				titreMessage="Etes vous sur de vouloir enlever les grilles tarifaires sur cette sélection de tiers ?";
			}
		}
		validateUIField(nomChamp,value);

	}
	

	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actInitInserer(ActionEvent actionEvent){
		
	}
	public void actInserer(ActionEvent actionEvent){
		try {

	
		valuesLignes.clear();
		
		//créer une nouvelle de tarif
		
//			valuesLignes.add(obj);				

		
		if(valuesLignes!=null && !valuesLignes.isEmpty())selectionLigne=valuesLignes.get(0);		
		nouvelleLigne=selectionLigne;
		
		selectionLignes=rempliSelection(valuesLignes);
		afficheCritere=true;	
		afficheTitre();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TaGrilleTarifTiersDTOJSF[] rempliSelection(List<TaGrilleTarifTiersDTOJSF> liste){
		List<TaGrilleTarifTiersDTOJSF> tmp = new LinkedList<>();
		for (TaGrilleTarifTiersDTOJSF obj : liste) {
			if(obj.getDto().getAccepte())tmp.add(obj);
		}
		return selectionLignes= Arrays.copyOf(tmp.toArray(),tmp.size(),TaGrilleTarifTiersDTOJSF[].class);		
	}
	
	
	public void actModifier(ActionEvent actionEvent){
		try {
			if(modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION)){
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actSupprimer(ActionEvent actionEvent){

	}
	public void actSupprimer(){
		actSupprimer(null);
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		
	}
	public void actSupprimerLigne(){
		selectionLigne.setSuppression(true);
		selectionLigne.setModifie(true);

		actModifier(null);
	}

	
	public void updateUIDTO() {
		try {


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void actEnregistrer(ActionEvent actionEvent){
		TaTTarif Ttarif=null;
		String codeTTarif="";
		try {
		if(taTTarifRemplacementDTO!=null) {
			Ttarif=taTTarifService.findByCode(taTTarifRemplacementDTO.getCodeTTarif());
			codeTTarif=Ttarif.getCodeTTarif();
			titreMessage="Etes vous sur de vouloir enregistrer la grille tarifaire "+codeTTarif+" sur cette sélection de tiers ?";
		}else {
			titreMessage="Etes vous sur de vouloir enlever les grilles tarifaires sur cette sélection de tiers ?";
		}

		
		for (TaGrilleTarifTiersDTOJSF obj : filteredValuesLignes) {
//			if(taTTarifRemplacementDTO!=null) {
				TaTiers tiers;
					
					tiers = taTiersService.findByCode(obj.getDto().getCodeTiers());
					if(Ttarif!=null)tiers.setTaTTarif(Ttarif);
					else tiers.setTaTTarif(null);
					tiers=taTiersService.merge(tiers);


//			}
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		refresh();
		
	}
	

	public void actAnnuler(ActionEvent actionEvent){

		afficheCritere=false;
		afficheTitre();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		refresh();
	}




	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GRILLE_REFERENCE_MT);
		b.setTitre("Grille de référence");
		b.setTemplate("multi_tarifs/grille_reference.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GRILLE_REFERENCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GRILLE_REFERENCE);
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

	}
	public void onRowSelect(SelectEvent event) { 
		try{
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_GRILLE_REFERENCE_MT);
		b.setTitre("Grille de référence");
		b.setTemplate("multi_tarifs/grille_reference.xhtml");
		b.setIdTab(LgrTab.ID_TAB_GRILLE_REFERENCE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_GRILLE_REFERENCE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		afficheCritere=false;
		afficheTitre();
		
//		if(selectionLigne!=null) {
//			taArticleDTO=selectionLigne.getArticleDTO();
//		}
		refresh();
		
		PrimeFaces.current().executeScript("window.scrollTo(0,0);");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void onRowSelectLigne(SelectEvent event) { 
			selectionLigne((TaGrilleTarifTiersDTOJSF) event.getObject());
			cellEdit=true;
	}
	public void selectionLigne(TaGrilleTarifTiersDTOJSF ligne){
		selectionLigne=ligne;
	}
	
	
	public void onRowUnSelectLigne(UnselectEvent event) { 

	}

	public boolean renderingCellEdit(){
		return cellEdit;
	}

//	public void refreshListOverlay(){
//		
//		valuesLignesListe.clear();
//
//		List<TaTiersDTO> liste=taTiersService.remonteGrilleReference("%","%");
//		for (TaTiersDTO articleDTO : liste) {
//			TaGrilleTarifTiersDTOJSF multi=new TaGrilleTarifTiersDTOJSF();
//			TaPrixDTO prixDTO=new TaPrixDTO();
//			prixDTO.setAccepte(true);
//			prixDTO.setId(articleDTO.getIdPrix());
//			prixDTO.setIdArticle(articleDTO.getId());
//			prixDTO.setCodeArticle(articleDTO.getCodeArticle());
//			prixDTO.setLibellecArticle(articleDTO.getLibellecArticle());
//			prixDTO.setPrixPrix(articleDTO.getPrixPrix());
//			if(articleDTO.getPrixttcPrix()!=null)
//				prixDTO.setMtTva(articleDTO.getPrixttcPrix().subtract(articleDTO.getPrixPrix()));
//			else prixDTO.setMtTva(BigDecimal.ZERO);
//			prixDTO.setPrixttcPrix(articleDTO.getPrixttcPrix());
//			prixDTO.setTauxTva(articleDTO.getTauxTva());
//			prixDTO.setCodeTTarif(articleDTO.getCodeTTarif());
//			prixDTO.setCoef(articleDTO.getCoef());
//			multi.setDto(prixDTO);
//			multi.setArticleDTO(articleDTO);
//			articleDTO.setAccepte(true);
////			try {
////				multi.setArticle(taArticleService.findByCode(articleDTO.getCodeArticle()));
////			} catch (FinderException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
//			valuesLignesListe.add(multi);
//		}
//		titreListe=titreListeComplete;
//	}
	
	public void refresh(){
//		valuesLignesListe.clear();
		reset();
		String codeTTarif="%";
		String codeFamille="%";
		if(taTTarifDTO!=null) {
			codeTTarif=taTTarifDTO.getCodeTTarif();
		}
		if(taFamilleDTO!=null) {
			codeFamille=taFamilleDTO.getCodeFamille();
		}

		List<TaTiersDTO> liste=taTiersService.findLightTTarifFamille(codeTTarif,codeFamille);
		for (TaTiersDTO tiersDTO : liste) {
			TaGrilleTarifTiersDTOJSF multi=new TaGrilleTarifTiersDTOJSF();
//			TaPrixDTO prixDTO=new TaPrixDTO();
//			prixDTO.setAccepte(true);
//			prixDTO.setId(articleDTO.getIdPrix());
//			prixDTO.setIdArticle(articleDTO.getId());	
//			prixDTO.setCodeArticle(articleDTO.getCodeArticle());
//			prixDTO.setLibellecArticle(articleDTO.getLibellecArticle());
//			prixDTO.setPrixPrix(articleDTO.getPrixPrix());
//			if(articleDTO.getPrixttcPrix()!=null)
//				prixDTO.setMtTva(articleDTO.getPrixttcPrix().subtract(articleDTO.getPrixPrix()));
//			else prixDTO.setMtTva(BigDecimal.ZERO);
//			prixDTO.setPrixttcPrix(articleDTO.getPrixttcPrix());
//			prixDTO.setTauxTva(articleDTO.getTauxTva());
//			prixDTO.setCodeTTarif(articleDTO.getCodeTTarif());
//			prixDTO.setCoef(articleDTO.getCoef());
//			multi.setDto(prixDTO);
			multi.setDto(tiersDTO);
			tiersDTO.setAccepte(true);
//			try {
//				multi.setArticle(taArticleService.findByCode(articleDTO.getCodeArticle()));
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			valuesLignes.add(multi);

		}
		selectionLigne=new TaGrilleTarifTiersDTOJSF();
		selectionLigne.setDto(new TaTiersDTO());
		
		if(valuesLignes!=null && !valuesLignes.isEmpty())
			selectionLigne=valuesLignes.get(0);
		
		oldSelectionLigne=selectionLigne.copy();
		
		if(!codeTTarif.equals("%")||!codeFamille.equals("%"))
			titreListe=titreListeFiltree;
		else titreListe=titreListeComplete;
	}
	
	public void onRowCancel(RowEditEvent event) {
		
		actAnnulerLigne(null);
    }
	
	public void actAnnulerLigne(ActionEvent actionEvent) {
		int rg = 0;
		if(valuesLignes!=null)rg=valuesLignes.indexOf(selectionLigne);
		valuesLignes.remove(selectionLigne);
		selectionLigne=oldSelectionLigne;
		if(valuesLignes!=null)valuesLignes.add(rg, selectionLigne);
	}
	
	public void actFermer(ActionEvent actionEvent) {
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

	public void majPrixHT(BigDecimal value) {
		validateUIField(Const.C_PRIX_PRIX,value);
	}
	public void majPrixTTC(BigDecimal value) {
		validateUIField(Const.C_PRIXTTC_PRIX,value);
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


			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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


	public List<TaGrilleTarifTiersDTOJSF> getValuesLignes() {
		return valuesLignes;
	}


	public void setValuesLignes(List<TaGrilleTarifTiersDTOJSF> valuesLignes) {
		this.valuesLignes = valuesLignes;
	}


	public List<TaGrilleTarifTiersDTOJSF> getFilteredValuesLignes() {
		return filteredValuesLignes;
	}


	public void setFilteredValuesLignes(List<TaGrilleTarifTiersDTOJSF> filteredValuesLignes) {
		this.filteredValuesLignes = filteredValuesLignes;
	}


	public TaGrilleTarifTiersDTOJSF getNouvelleLigne() {
		return nouvelleLigne;
	}


	public void setNouvelleLigne(TaGrilleTarifTiersDTOJSF nouvelleLigne) {
		this.nouvelleLigne = nouvelleLigne;
	}


	public TaGrilleTarifTiersDTOJSF getSelectionLigne() {
		return selectionLigne;
	}


	public void setSelectionLigne(TaGrilleTarifTiersDTOJSF selectionLigne) {
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


	


	

    public void detail() {
    	detail(null);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }


	public TaGrilleTarifTiersDTOJSF[] getSelectionLignes() {
		return selectionLignes;
	}


	public void setSelectionLignes(TaGrilleTarifTiersDTOJSF[] selectionLignes) {
		this.selectionLignes = selectionLignes;
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


	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}


	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}


	public TaFamilleTiersDTO getTaFamilleDTO() {
		return taFamilleDTO;
	}


	public void setTaFamilleDTO(TaFamilleTiersDTO taFamilleDTO) {
		this.taFamilleDTO = taFamilleDTO;
	}


	
	public void onRowEdit(RowEditEvent event) {
		actEnregistrerLigne(null);
		cellEdit=false;
	}
	
	public void actEnregistrerLigne(ActionEvent actionEvent) {
		
		try {
			selectionLigne.updateDTO();
			selectionLigne.setModifie(true);
			oldSelectionLigne=selectionLigne.copy();
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public boolean isSaisieHT() {
		return saisieHT;
	}


	public void setSaisieHT(boolean saisieHT) {
		this.saisieHT = saisieHT;
	}

	
	public void onCellEdit(CellEditEvent  event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		selectionLigne=valuesLignes.get(event.getRowIndex());

		if(newValue != null 
//				&& !newValue.equals(oldValue)
				) {
			try {
				actEnregistrerLigne(null);
				cellEdit=false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void reset() {
		valuesLignes.clear();
		filteredValuesLignes.clear();
	}



	public String getTitreSaisieHt() {
		return titreSaisieHt;
	}


	public void setTitreSaisieHt(String titreSaisieHt) {
		this.titreSaisieHt = titreSaisieHt;
	}


	public String getTitreSaisieTtc() {
		return titreSaisieTtc;
	}


	public void setTitreSaisieTtc(String titreSaisieTtc) {
		this.titreSaisieTtc = titreSaisieTtc;
	}


	public String getTitreListeComplete() {
		return titreListeComplete;
	}


	public void setTitreListeComplete(String titreListeComplete) {
		this.titreListeComplete = titreListeComplete;
	}


	public String getTitreFiltree() {
		return titreListeFiltree;
	}


	public void setTitreFiltree(String titreFiltree) {
		this.titreListeFiltree = titreFiltree;
	}


	public String getTitreListe() {
		return titreListe;
	}


	public void setTitreListe(String titreListe) {
		this.titreListe = titreListe;
	}


	public boolean isSaisieDetail() {
		return saisieDetail;
	}


	public void setSaisieDetail(boolean saisieDetail) {
		this.saisieDetail = saisieDetail;
	}


	public TaTTarifDTO getTaTTarifDTO() {
		return taTTarifDTO;
	}


	public void setTaTTarifDTO(TaTTarifDTO taTTarifDTO) {
		this.taTTarifDTO = taTTarifDTO;
	}


	public TaTTarifDTO getTaTTarifRemplacementDTO() {
		return taTTarifRemplacementDTO;
	}


	public void setTaTTarifRemplacementDTO(TaTTarifDTO taTTarifRemplacementDTO) {
		this.taTTarifRemplacementDTO = taTTarifRemplacementDTO;
	}


	public String getTitreMessage() {
		return titreMessage;
	}


	public void setTitreMessage(String titreMessage) {
		this.titreMessage = titreMessage;
	}


	


}
