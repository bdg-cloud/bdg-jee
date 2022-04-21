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
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaPrix;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFamilleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaRemise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;


@Named
@ViewScoped  
public class GrilleReferenceController extends AbstractGrilleTarifaireController{

//	@Inject @Named(value="tabListModelCenterBean")
//	protected TabListModelBean tabsCenter;
//	
//	@Inject @Named(value="tabViewsBean")
//	protected TabViewsBean tabViews;
//	private boolean cellEdit = false;

	private List<TaLMultiTarifDTOJSF> valuesLignes=new LinkedList<>();
	private List<TaLMultiTarifDTOJSF> filteredValuesLignes=new LinkedList<>();
	
	private List<TaLMultiTarifDTOJSF> valuesLignesListe=new LinkedList<>();

	
	private TaLMultiTarifDTOJSF nouvelleLigne ;
	private TaLMultiTarifDTOJSF selectionLigne  ;
	private TaLMultiTarifDTOJSF oldSelectionLigne  ;
	
	private TaLMultiTarifDTOJSF[] selectionLignes ;
	
	TaArticleDTO taArticleDTO;
	TaFamilleDTO taFamilleDTO;
	
//	private boolean saisieHT=true;
//	private boolean saisieDetail=false;
//	private String modeSaisie=Const.C_SAISIE_SIMPLE;
//	private String modeSaisiePrix=Const.C_SAISIE_HT;
//	
//	private String titreSaisieDetail="Saisie en détail";
//	private String titreSaisieRapide="Saisie rapide";
//	private Date dateDeb=null;
//	private Date dateFin=null;	
//
//	TaInfoEntreprise infos =null;
//	private String modeEcranDefaut;
//	private static final String C_DIALOG = "dialog";
	
	private String titreListeComplete="Liste complète des prix de référence";
	private String titreListeFiltree="Liste filtrée des prix de référence";
	private String titreListe=titreListeComplete;
	private String titlePartOne="Grille de référence";
	private String titlePartTwo="";
	private int nbMaxLigne=20;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private Boolean afficheCritere=false;
	
//	private String titreSaisieHt="Saisie des prix en HT";
//	private String titreSaisieTtc="Saisie des prix en TTC";
	private String titreEtape1="Etape 1/2 : Saisissez les critères de sélection des articles ou familles d'article";
	private String titreEtape2="Etape 2/2 : attribuer un prix HT ou TTC à chacun des articles de la liste";
	private String titreEtape3="";
	private String titreEcran=titreEtape2;
	
	Integer nbLigne=0;
	BigDecimal totalTTC=BigDecimal.valueOf(0);
	

	private @EJB  ITaInfoEntrepriseServiceRemote TaInfoEntrepriseService;
	private @EJB ITaPreferencesServiceRemote taPreferencesService;
	@EJB ITaArticleServiceRemote taArticleService;
	@EJB ITaTTarifServiceRemote taTTarifService;
	@EJB ITaFamilleServiceRemote taFamilleService;
	
	
	private LgrDozerMapper<TaRemiseDTO,TaRemise> mapperUIToModel  = new LgrDozerMapper<TaRemiseDTO,TaRemise>();
	private LgrDozerMapper<TaRemise,TaRemiseDTO> mapperModelToUI  = new LgrDozerMapper<TaRemise,TaRemiseDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {
			super.postConstruct();

			infos=TaInfoEntrepriseService.findInstance();

			dateDeb=infos.getDatedebInfoEntreprise();
			dateFin=infos.getDatefinInfoEntreprise();
			initEcran();
			
			refreshListOverlay();


		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValuesLignes(valuesLignes);
	}
	
	
	public GrilleReferenceController() {  
	}  


	public String titreSaisieRapideOuDetail() {
		if(isSaisieDetail())return titreSaisieDetail;
		else return titreSaisieRapide;
	}
	
	public String titreSaisie() {
		if(isSaisieHT())return titreSaisieHt;
		else return titreSaisieTtc;
	}
	
	public List<TaFamilleDTO> familleAutoComplete(String query) {
		List<TaFamilleDTO> allValues = taFamilleService.selectAllDTO();
		List<TaFamilleDTO> filteredValues = new ArrayList<TaFamilleDTO>();
		TaFamilleDTO cp = new TaFamilleDTO();
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
	
	public List<TaArticleDTO> articleAutoComplete(String query) {
		List<TaArticleDTO> allValues = taArticleService.findAllLight();
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();
		TaArticleDTO cp = new TaArticleDTO();
		cp.setCodeArticle(Const.C_AUCUN);
//		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
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
		
		reset();
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

	public TaLMultiTarifDTOJSF[] rempliSelection(List<TaLMultiTarifDTOJSF> liste){
		List<TaLMultiTarifDTOJSF> tmp = new LinkedList<>();
		for (TaLMultiTarifDTOJSF obj : liste) {
			if(obj.getDto().getAccepte())tmp.add(obj);
		}
		return selectionLignes= Arrays.copyOf(tmp.toArray(),tmp.size(),TaLMultiTarifDTOJSF[].class);		
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
		for (TaLMultiTarifDTOJSF obj : valuesLignes) {
			if(obj.isModifie()) {
				TaPrix prix = new TaPrix();
				TaArticle art;
				try {

					art = taArticleService.findByCode(obj.getArticleDTO().getCodeArticle());
					if(obj.getDto().getId()!=null && obj.getDto().getId()!=0 && art.getTaPrix()!=null && art.getTaPrix().getIdPrix()==obj.getDto().getId()) {
						prix=art.getTaPrix();
					}else {
						prix.setTaArticle(art);
						art.setTaPrix(prix);
						art.addPrix(prix);
					}
					prix.setPrixPrix(obj.getDto().getPrixPrix());
					prix.setPrixttcPrix(obj.getDto().getPrixttcPrix());
					if(isSaisieHT())prix.majPrix();
					else prix.majPrixTTC();

					if(obj.isSuppression()) {
						art.removePrix(prix);
						art.setTaPrix(null);
						prix.setTaArticle(null);
					}
					art=taArticleService.merge(art);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
			selectionLigne((TaLMultiTarifDTOJSF) event.getObject());
			cellEdit=true;
	}
	public void selectionLigne(TaLMultiTarifDTOJSF ligne){
		selectionLigne=ligne;
	}
	
	
	public void onRowUnSelectLigne(UnselectEvent event) { 

	}

	public boolean renderingCellEdit(){
		return cellEdit;
	}

	public void refreshListOverlay(){
		
		valuesLignesListe.clear();

		List<TaArticleDTO> liste=taArticleService.remonteGrilleReference("%","%");
		for (TaArticleDTO articleDTO : liste) {
			TaLMultiTarifDTOJSF multi=new TaLMultiTarifDTOJSF();
			TaPrixDTO prixDTO=new TaPrixDTO();
			prixDTO.setAccepte(true);
			prixDTO.setId(articleDTO.getIdPrix());
			prixDTO.setIdArticle(articleDTO.getId());
			prixDTO.setCodeArticle(articleDTO.getCodeArticle());
			prixDTO.setLibellecArticle(articleDTO.getLibellecArticle());
			prixDTO.setPrixPrix(articleDTO.getPrixPrix());
			if(articleDTO.getPrixttcPrix()!=null)
				prixDTO.setMtTva(articleDTO.getPrixttcPrix().subtract(articleDTO.getPrixPrix()));
			else prixDTO.setMtTva(BigDecimal.ZERO);
			prixDTO.setPrixttcPrix(articleDTO.getPrixttcPrix());
			prixDTO.setTauxTva(articleDTO.getTauxTva());
			prixDTO.setCodeTTarif(articleDTO.getCodeTTarif());
			prixDTO.setCoef(articleDTO.getCoef());
			multi.setDto(prixDTO);
			multi.setArticleDTO(articleDTO);
			articleDTO.setAccepte(true);
//			try {
//				multi.setArticle(taArticleService.findByCode(articleDTO.getCodeArticle()));
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			valuesLignesListe.add(multi);
		}
		titreListe=titreListeComplete;
	}
	
	public void refresh(){
		valuesLignesListe.clear();
		valuesLignes.clear();
		String codeArticle="%";
		String codeFamille="%";
		if(taArticleDTO!=null) {
			codeArticle=taArticleDTO.getCodeArticle();
		}
		if(taFamilleDTO!=null) {
			codeFamille=taFamilleDTO.getCodeFamille();
		}

		List<TaArticleDTO> liste=taArticleService.remonteGrilleReference(codeArticle,codeFamille);
		for (TaArticleDTO articleDTO : liste) {
			TaLMultiTarifDTOJSF multi=new TaLMultiTarifDTOJSF();
			TaPrixDTO prixDTO=new TaPrixDTO();
			prixDTO.setAccepte(true);
			prixDTO.setId(articleDTO.getIdPrix());
			prixDTO.setIdArticle(articleDTO.getId());	
			prixDTO.setCodeArticle(articleDTO.getCodeArticle());
			prixDTO.setLibellecArticle(articleDTO.getLibellecArticle());
			prixDTO.setPrixPrix(articleDTO.getPrixPrix());
			if(articleDTO.getPrixttcPrix()!=null)
				prixDTO.setMtTva(articleDTO.getPrixttcPrix().subtract(articleDTO.getPrixPrix()));
			else prixDTO.setMtTva(BigDecimal.ZERO);
			prixDTO.setPrixttcPrix(articleDTO.getPrixttcPrix());
			prixDTO.setTauxTva(articleDTO.getTauxTva());
			prixDTO.setCodeTTarif(articleDTO.getCodeTTarif());
			prixDTO.setCoef(articleDTO.getCoef());
			multi.setDto(prixDTO);
			multi.setArticleDTO(articleDTO);
			articleDTO.setAccepte(true);
//			try {
//				multi.setArticle(taArticleService.findByCode(articleDTO.getCodeArticle()));
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			valuesLignes.add(multi);

		}
		selectionLigne=new TaLMultiTarifDTOJSF();
		selectionLigne.setDto(new TaPrixDTO());
		
		if(valuesLignes!=null && !valuesLignes.isEmpty())
			selectionLigne=valuesLignes.get(0);
		
		oldSelectionLigne=selectionLigne.copy();
		
		for (TaLMultiTarifDTOJSF obj : valuesLignes) {
			valuesLignesListe.add(obj);
		}
		if(!codeArticle.equals("%")||!codeFamille.equals("%"))
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
			if(nomChamp.equals(Const.C_PRIX_PRIX)) {
				if(value==null)value=BigDecimal.ZERO;
//				if(value!=null && !value.equals("")) {
					TaPrix prix=new TaPrix();
					prix.setTaArticle(selectionLigne.getArticle());
					prix.setPrixPrix((BigDecimal) value);
					prix.setPrixttcPrix(selectionLigne.getDto().getPrixttcPrix());
					if(selectionLigne.getDto().getPrixttcPrix()==null || selectionLigne.getDto().getPrixttcPrix().equals("") ||selectionLigne.getDto().getPrixttcPrix().compareTo(BigDecimal.ZERO)<=0)
						prix.setPrixttcPrix(prix.getPrixPrix());
					prix.majPrix(selectionLigne.getDto().getTauxTva());
					selectionLigne.getDto().setPrixPrix(prix.getPrixPrix());				
					selectionLigne.getDto().setPrixttcPrix(prix.getPrixttcPrix()); 
//				}
			}else 
				if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
					if(value==null)value=BigDecimal.ZERO;
//					if(value!=null && !value.equals("")) {
						TaPrix prix=new TaPrix();
						prix.setTaArticle(selectionLigne.getArticle());
						prix.setPrixPrix(selectionLigne.getDto().getPrixPrix());
						prix.setPrixttcPrix((BigDecimal) value);
						if(selectionLigne.getDto().getPrixPrix()==null || selectionLigne.getDto().getPrixPrix().equals("") ||selectionLigne.getDto().getPrixPrix().compareTo(BigDecimal.ZERO)<=0)
							prix.setPrixttcPrix(prix.getPrixttcPrix());						
						prix.majPrixTTC(selectionLigne.getDto().getTauxTva());
						selectionLigne.getDto().setPrixPrix(prix.getPrixPrix());				
						selectionLigne.getDto().setPrixttcPrix(prix.getPrixttcPrix()); 
//					}
				}
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


	public List<TaLMultiTarifDTOJSF> getValuesLignes() {
		return valuesLignes;
	}


	public void setValuesLignes(List<TaLMultiTarifDTOJSF> valuesLignes) {
		this.valuesLignes = valuesLignes;
	}


	public List<TaLMultiTarifDTOJSF> getFilteredValuesLignes() {
		return filteredValuesLignes;
	}


	public void setFilteredValuesLignes(List<TaLMultiTarifDTOJSF> filteredValuesLignes) {
		this.filteredValuesLignes = filteredValuesLignes;
	}


	public TaLMultiTarifDTOJSF getNouvelleLigne() {
		return nouvelleLigne;
	}


	public void setNouvelleLigne(TaLMultiTarifDTOJSF nouvelleLigne) {
		this.nouvelleLigne = nouvelleLigne;
	}


	public TaLMultiTarifDTOJSF getSelectionLigne() {
		return selectionLigne;
	}


	public void setSelectionLigne(TaLMultiTarifDTOJSF selectionLigne) {
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


	public TaLMultiTarifDTOJSF[] getSelectionLignes() {
		return selectionLignes;
	}


	public void setSelectionLignes(TaLMultiTarifDTOJSF[] selectionLignes) {
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


	public TaArticleDTO getTaArticleDTO() {
		return taArticleDTO;
	}


	public void setTaArticleDTO(TaArticleDTO taArticleDTO) {
		this.taArticleDTO = taArticleDTO;
	}


	public TaFamilleDTO getTaFamilleDTO() {
		return taFamilleDTO;
	}


	public void setTaFamilleDTO(TaFamilleDTO taFamilleDTO) {
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
		valuesLignesListe.clear();
	}

	public List<TaLMultiTarifDTOJSF> getValuesLignesListe() {
		return valuesLignesListe;
	}


	public void setValuesLignesListe(List<TaLMultiTarifDTOJSF> valuesLignesListe) {
		this.valuesLignesListe = valuesLignesListe;
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


	public String getTitreSaisieDetail() {
		return titreSaisieDetail;
	}


	public String getTitreSaisieRapide() {
		return titreSaisieRapide;
	}


	public String getModeSaisie() {
		return modeSaisie;
	}


	public void setModeSaisie(String modeSaisie) {
		this.modeSaisie = modeSaisie;
	}


	public void initEcran() {
		saisieDetail=modeSaisie.equals(Const.C_SAISIE_DETAIL);
		saisieHT=modeSaisiePrix.equals(Const.C_SAISIE_HT);
	}


	public String getModeSaisiePrix() {
		return modeSaisiePrix;
	}


	public void setModeSaisiePrix(String modeSaisiePrix) {
		this.modeSaisiePrix = modeSaisiePrix;
	}


}
