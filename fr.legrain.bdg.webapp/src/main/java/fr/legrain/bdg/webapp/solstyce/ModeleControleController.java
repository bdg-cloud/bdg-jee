package fr.legrain.bdg.webapp.solstyce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.conformite.service.remote.ITaGroupeServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaModeleBaremeServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaModeleConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaModeleGroupeServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaTypeConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.conformite.model.TaModeleBareme;
import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.conformite.model.TaTypeConformite;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class ModeleControleController extends AbstractController implements Serializable {  
	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;

	private List<TaModeleConformite> values; 
	private List<TaModeleConformite> filteredValues; 

	private TaModeleConformite nouveau = new TaModeleConformite();
	private TaModeleBareme nouveauBareme;
	private TaModeleConformite selection ;
	private TaModeleConformite[] selectionS ;
	private TaModeleBareme selectionBareme;
	private Boolean tableVide;
	
	private List<TaTypeConformite> listeTypeControle;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private ModeObjetEcranServeur modeEcranBareme = new ModeObjetEcranServeur();

	private @EJB ITaModeleConformiteServiceRemote taModeleConformiteService;
	private @EJB ITaModeleBaremeServiceRemote taModeleBaremeService;
	private @EJB ITaTypeConformiteServiceRemote taTypeConformiteService;
	private @EJB ITaGroupeServiceRemote taGroupeService;
	private @EJB ITaModeleGroupeServiceRemote taModeleGroupeService;
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
	
	private StreamedContent file;

	@PostConstruct
	public void postConstruct(){
		try {
			listeTypeControle = taTypeConformiteService.selectAll();
			
			setTableVide(false);
			values =  taModeleConformiteService.selectAll();
			if(values == null){
				setTableVide(true);
			}
			if(!values.isEmpty()) {
				selection = values.get(0);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}

	public ModeleControleController() {  
	}  
	
	public void actSupprimerFichier(ActionEvent actionEvent){
		nouveauBareme.setBlobFichier(null);
		nouveauBareme.setCheminDoc(null);
	}
	
	public StreamedContent getFile() {
	   // InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
       // file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
		InputStream stream = new ByteArrayInputStream(nouveauBareme.getBlobFichier()); 
		file = new DefaultStreamedContent(stream,null,nouveauBareme.getCheminDoc());
        return file;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        try {
        	
        	nouveauBareme.setCheminDoc(event.getFile().getFileName());
        	nouveauBareme.setBlobFichier(IOUtils.toByteArray(event.getFile().getInputStream()));
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        Integer idPiece = (Integer) event.getComponent().getAttributes().get("idPiece");
//    	
//    	piece = stockService.findById(idPiece);
//    	
//    	ImagePiece img = new ImagePiece();
//    	if(piece.getImages()==null) {
//    		piece.setImages(new ArrayList<ImagePiece>());
//    	}
//    	String nomFichier = "img_"+idPiece+"_"+piece.getImages().size()/*+"."*/+event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."));
//    	img.setChemin(nomFichier);
// 
//
//    	Path target3 = Paths.get("/var/careco/images/"+nomFichier);
//    	try {
//			Files.copy(event.getFile().getInputstream(), target3);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
//    	img.setIdPiece(piece);
//    	piece.getImages().add(img);
//    	
//    	piece = stockService.enregistrerMerge(piece);
    }

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaModeleConformite();
		nouveau.setTaTypeConformite(new TaTypeConformite());
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		scrollToTop();
		
		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		if(nouveau.getCode()!=null) {
			taModeleConformiteService.annuleCode(nouveau.getCode());
		}
		nouveau.setCode(taModeleConformiteService.genereCode(null));
		
	}
	
	public void actInsererBareme(ActionEvent actionEvent){
		nouveauBareme = new TaModeleBareme();
		modeEcranBareme.setMode(EnumModeObjet.C_MO_INSERTION);
		
		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		if(nouveauBareme.getCode()!=null) {
			taModeleBaremeService.annuleCode(nouveauBareme.getCode());
		}
		nouveauBareme.setCode(taModeleBaremeService.genereCode(null));
	}

	public void actCtrlBloquantChangeEvent(AjaxBehaviorEvent event){
		if(nouveau.getCtrlBloquant()) {
			nouveau.setCtrlFacultatif(false);
		}
	}

	
	public void actModifierBareme(ActionEvent actionEvent){
	
		if(selectionBareme==null && nouveau.getTaModeleBaremes().size()>0)
			selectionBareme=nouveau.getTaModeleBaremes().iterator().next();
		nouveauBareme = selectionBareme;
		modeEcranBareme.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actSupprimerBareme(){
		try {
			selection.getTaModeleBaremes().remove(selectionBareme);
			selectionBareme.setTaConformite(null);
			
			//taConformiteService.remove(selection);
			//selectionBareme = values.get(0);	
		} catch (/*Remove*/Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = taModeleConformiteService.selectAll();
		selection = null;
	}
	
	public void actEnregistrerBareme(ActionEvent actionEvent){
		if(nouveau.getTaModeleBaremes()==null) {
			nouveau.setTaModeleBaremes(new HashSet<TaModeleBareme>());
		}
		nouveau.getTaModeleBaremes().add(nouveauBareme);
		nouveauBareme.setTaConformite(nouveau);
		nouveauBareme = new TaModeleBareme();
		modeEcranBareme.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actAnnulerBareme(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}	
		if(nouveauBareme.getCode()!=null) {
			taModeleBaremeService.annuleCode(nouveauBareme.getCode());
		}	
		modeEcranBareme.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}


	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void supprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer() {
		actSupprimer(null);
	}

	public void actSupprimer(ActionEvent actionEvent){
		try {
			taModeleConformiteService.remove(selection);
			selection = values.get(0);	
		//} catch (RemoveException e) {
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur : Impossible de supprimer ce modèle.", e.getMessage()));
			e.printStackTrace();
		}
		values = taModeleConformiteService.selectAll();
		selection = null;
		if(values.size()>0)selection = values.get(0);
		nouveau=selection;
		
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {

			if(nouveau.getCode()!=null && nouveau.getCode().equals("")) {
				nouveau.setCode(null);
			}
			if(taModeleConformiteService.findById(nouveau.getIdModeleConformite()) == null){
				TaModeleConformite retour = taModeleConformiteService.merge(nouveau, ITaModeleConformiteServiceRemote.validationContext);
				values.add(selection);
				values= taModeleConformiteService.selectAll();
//				selection = values.get(0);
//
////				nouveau = new TaModeleConformite();
				
				selection = taModeleConformiteService.findById(retour.getIdModeleConformite());
				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
					PrimeFaces.current().dialog().closeDynamic(retour);
				}
			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					TaModeleConformite retour = taModeleConformiteService.merge(nouveau, ITaModeleConformiteServiceRemote.validationContext);
					values= taModeleConformiteService.selectAll();
					//selection = values.get(0);

//					nouveau = new TaModeleConformite();
					selection = taModeleConformiteService.findById(retour.getIdModeleConformite());

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			nouveau=selection;
			selectionS=new TaModeleConformite[]{selection};
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
//		values= taTFamilleArticlesService.selectAll();
		if(values.size()>= 1){
			selection = values.get(0);
			nouveau =selection;
			nouveauBareme=null;
		}else {		
		 nouveau = new TaModeleConformite();
		 selection=nouveau;
		 nouveauBareme=null;
		}
		
		if(nouveau.getCode()!=null) {
			taModeleConformiteService.annuleCode(nouveau.getCode());
		}	
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		modeEcranBareme.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actImprimerListeModeleDeControle(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeModeleDeControle", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	public List<TaModeleConformite> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	public TaModeleConformite getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaModeleConformite newTaModeleConformite) {
		this.nouveau = newTaModeleConformite;
	}

	public TaModeleConformite getSelection() {
		return selection;
	}

	public void setSelection(TaModeleConformite selectedTaModeleConformite) {
		this.selection = selectedTaModeleConformite;
	}

	public void setValues(List<TaModeleConformite> values) {
		this.values = values;
	}

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.ModeleControle");
		b.setTitre("Modèle de controle");
		b.setTemplate("solstyce/modele_controle.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MODELE_CONTROLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_CONTROLE_CONFORMITE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		
		if(selectionS!=null && selectionS.length>0) {
			selection = selectionS[0];
		}
//		if(selection.getIdModeleConformite()!=0) {
//			masterEntity = taDevisService.findById(selectedTaDevisDTO.getId());
//		}
		
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
		scrollToTop();
	}
	
	public void ajouter(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.ModeleControle");
		b.setTitre("Modèle de controle");
		b.setTemplate("solstyce/modele_controle.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MODELE_CONTROLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_CONTROLE_CONFORMITE);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Modèle de controle", 
					"Nouveau"
					)); 
		}
	} 
	
	public void onRowSelectBareme(SelectEvent event) {  
		if(modeEcranBareme.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveauBareme = selectionBareme;
		}
	}

	public List<TaModeleConformite> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaModeleConformite> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
	}

	public void refresh(){
		values = taModeleConformiteService.selectAll();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");

		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}

	}

	public void actFermer(ActionEvent actionEvent){
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
//		//gestion du filtre de la liste
//        RequestContext requestContext = RequestContext.getCurrentInstance();
//        requestContext.execute("PF('wvDataTableListeModeleControle').filter()");
	}
	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actDialogGroupeControle(ActionEvent actionEvent) {
		
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
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_groupe_controle", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogGroupeControle(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//taGroupe = (TaGroupe) event.getObject();
		}
	}
	
	public void actDialogModeleGroupeControle(ActionEvent actionEvent) {
		
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
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_modele_groupe_controle", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogModeleGroupeControle(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//taGroupe = (TaGroupe) event.getObject();
		}
	}
	
	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.ModeleControle");
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public List<TaGroupe> groupeAutoComplete(String query) {
        List<TaGroupe> allValues = taGroupeService.selectAll();
        List<TaGroupe> filteredValues = new ArrayList<TaGroupe>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaGroupe civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeGroupe().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public List<TaModeleGroupe> modeleGroupeAutoComplete(String query) {
        List<TaModeleGroupe> allValues = taModeleGroupeService.selectAll();
        List<TaModeleGroupe> filteredValues = new ArrayList<TaModeleGroupe>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaModeleGroupe civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeGroupe().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
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
				if(nouveau!=null &&  nouveau.getIdModeleConformite()!=0 ) retour = false;
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
	
	public boolean etatBoutonBareme(String bouton) {
		boolean retour = true;
		switch (modeEcranBareme.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = modeEcranBareme.dataSetEnModif()?false:true;
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
				retour = modeEcranBareme.dataSetEnModif()?false:true;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "inserer":
			case "fermer":
				retour = modeEcran.dataSetEnModif()?false:true;;
				break;
			case "supprimer":
			case "modifier":
			case "imprimer":
				if(nouveauBareme!=null && nouveauBareme.getIdModeleBareme()!=0 ) retour = false;
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
	
	public void detail() {
		detail(null);
	}
	
	public void detail(ActionEvent actionEvent) {
		onRowSelect(null);
	}
	
	public void supprimer(ActionEvent actionEvent) {
		actSupprimer(actionEvent);
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

	public List<TaTypeConformite> getListeTypeControle() {
		return listeTypeControle;
	}

	public void setListeTypeControle(List<TaTypeConformite> listeTypeControle) {
		this.listeTypeControle = listeTypeControle;
	}

	public TaModeleBareme getNouveauBareme() {
		return nouveauBareme;
	}

	public void setNouveauBareme(TaModeleBareme nouveauBareme) {
		this.nouveauBareme = nouveauBareme;
	}

	public TaModeleBareme getSelectionBareme() {
		return selectionBareme;
	}

	public void setSelectionBareme(TaModeleBareme selectionBareme) {
		this.selectionBareme = selectionBareme;
	}

	public ModeObjetEcranServeur getModeEcranBareme() {
		return modeEcranBareme;
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

	public TaModeleConformite[] getSelectionS() {
		return selectionS;
	}

	public void setSelectionS(TaModeleConformite[] selectionS) {
		this.selectionS = selectionS;
	}
}  
