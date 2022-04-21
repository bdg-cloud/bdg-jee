package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import fr.legrain.bdg.conformite.service.remote.ITaModeleConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaModeleGroupeServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.conformite.model.TaRGroupeModeleConformite;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class ModeleGroupeControleController implements Serializable { 
	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	private DualListModel<TaModeleConformite> listeModeleConformite;

	private List<TaModeleGroupe> values; 
	private List<TaModeleGroupe> filteredValues; 

	private TaModeleGroupe nouveau;
	private TaModeleGroupe selection;
	private TaModeleGroupe[] selectionS;
	private TaModeleConformite detailConformite ;
	private Boolean tableVide;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaModeleGroupeServiceRemote taModeleGroupeService;
	private @EJB ITaModeleConformiteServiceRemote taModeleConformiteService;

	@PostConstruct
	public void postConstruct(){
		try {
			setTableVide(false);
			values =  taModeleGroupeService.selectAll();
			if(values == null){
				setTableVide(true);
			}
			if(!values.isEmpty()) {
				selection = values.get(0);	
			}
			
			//Themes
	        List<TaModeleConformite> themesSource = taModeleConformiteService.selectAll();
	        List<TaModeleConformite> themesTarget = new ArrayList<TaModeleConformite>();
	         
	        listeModeleConformite = new DualListModel<TaModeleConformite>(themesSource, themesTarget);
	        
	        modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}

	public ModeleGroupeControleController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaModeleGroupe();
		
		List<TaModeleConformite> themesSource = taModeleConformiteService.selectAll();
        List<TaModeleConformite> themesTarget = new ArrayList<TaModeleConformite>();
         
        listeModeleConformite = new DualListModel<TaModeleConformite>(themesSource, themesTarget);
        
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void actSupprimer() {
		actSupprimer(null);
	}

	public void actSupprimer(ActionEvent actionEvent){
		try {
			taModeleGroupeService.remove(selection);
			selection = values.get(0);
			
		//} catch (RemoveException e) {
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur : Impossible de supprimer ce modèle de groupe de controle.", e.getMessage()));
			e.printStackTrace();
		}
		values = taModeleGroupeService.selectAll();
		selection = null;
		selectionS=new TaModeleGroupe[]{};
		if(values.size()>0)selection = values.get(0);
		nouveau=selection;	
		updateTab();
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			if(nouveau.getCodeGroupe() == null || nouveau.getCodeGroupe().equals("") ){

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ code obligatoire"));
			}
			else{ 
				if(nouveau.getLibelle() == null || nouveau.getLibelle().equals("") ){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "champ libellé obligatoire"));
				}
				
					else{ 

						if(taModeleGroupeService.findByCode(nouveau.getCodeGroupe()) == null){
													
							//ajouter les nouveaux
							int i = 1;
							for (TaModeleConformite ctrl : listeModeleConformite.getTarget()) {
								if(nouveau.chercheModeleConformite(ctrl)!=null) {
									nouveau.chercheModeleConformite(ctrl).setPosition(i);
								} else {
									nouveau.getListeModeleConformite().add(new TaRGroupeModeleConformite(nouveau, ctrl,i));
								}
								i++;
							}
							
							TaModeleGroupe retour = taModeleGroupeService.merge(nouveau, ITaModeleGroupeServiceRemote.validationContext);
							
							values.add(selection);							
							values= taModeleGroupeService.selectAll();
//							selection = values.get(0);
							nouveau = taModeleGroupeService.findById(retour.getIdModeleGroupe() );
							selection=nouveau;
							selectionS=new TaModeleGroupe[]{selection};
							modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

							if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
								PrimeFaces.current().dialog().closeDynamic(retour);
							}
						}else{
							if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
								
								//supprimer les controles non sélectionné
								List<TaModeleConformite> lasupprimer = new ArrayList<TaModeleConformite>();
								for (TaRGroupeModeleConformite c : nouveau.getListeModeleConformite()) {
									if(!listeModeleConformite.getTarget().contains(c)) {
										lasupprimer.add(c.getModeleConformite());
									}
								}
								for (TaModeleConformite taModeleConformite : lasupprimer) {
									nouveau.getListeModeleConformite().remove(nouveau.chercheModeleConformite(taModeleConformite));
								}
								
								//ajouter les nouveaux
								int i = 1;
								for (TaModeleConformite ctrl : listeModeleConformite.getTarget()) {
									if(nouveau.chercheModeleConformite(ctrl)!=null) {
										nouveau.chercheModeleConformite(ctrl).setPosition(i);
									} else {
										nouveau.getListeModeleConformite().add(new TaRGroupeModeleConformite(nouveau, ctrl,i));
									}
									i++;
								}
								
								TaModeleGroupe retour = taModeleGroupeService.merge(nouveau, ITaModeleGroupeServiceRemote.validationContext);
								values= taModeleGroupeService.selectAll();
//								selection = values.get(0);
								nouveau = taModeleGroupeService.findById(retour.getIdModeleGroupe() );
								selection=nouveau;
								selectionS=new TaModeleGroupe[]{selection};
								modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
							}
							else{
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
							}
					}
						
						updateTab();
				}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.modeleGroupeControle");
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void actAnnuler(ActionEvent actionEvent){
//		values= taTFamilleArticlesService.selectAll();
		if(values.size()>= 1){
			selection = values.get(0);
			nouveau = selection;
		}else{
			nouveau = new TaModeleGroupe();
			selection=nouveau;
		}
		updateTab();
		//		creation = false;
		//		modification = false;

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public List<TaModeleGroupe> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	//	public Boolean getModification() {
	//		return modification;
	//	}
	//	public void setModification(Boolean modification) {
	//		this.modification = modification;
	//	}

	public TaModeleGroupe getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaModeleGroupe newTaModeleGroupe) {
		this.nouveau = newTaModeleGroupe;
	}

	public TaModeleGroupe getSelection() {
		return selection;
	}

	public void setSelection(TaModeleGroupe selectedTaModeleGroupe) {
		this.selection = selectedTaModeleGroupe;
	}

	public void setValues(List<TaModeleGroupe> values) {
		this.values = values;
	}

	public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
            builder.append(((TaModeleConformite) item).getLibelleConformite()).append("<br />");
        }
         
        if(ConstWeb.DEBUG) {
	        FacesMessage msg = new FacesMessage();
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        msg.setSummary("Items Transferred");
	        msg.setDetail(builder.toString());
         
	        FacesContext.getCurrentInstance().addMessage(null, msg);
		}
    } 
 
    public void onSelect(SelectEvent event) {
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
        detailConformite = (TaModeleConformite) event.getObject();
    }
     
    public void onUnselect(UnselectEvent event) {
    	if(ConstWeb.DEBUG) {
	        FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    	}
    }
     
    public void onReorder() {
    	if(ConstWeb.DEBUG) {
	        FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    	}
    } 
    
    public void updateTab(){
    	if(selectionS!=null && selectionS.length>0) {
			selection = selectionS[0];
		}
    	
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
//		if(selection.getIdModeleGroupe()!=0) {
//			masterEntity = taDevisService.findById(selectedTaDevisDTO.getId());
//		}
		
		List<TaModeleConformite> l = new ArrayList<TaModeleConformite>();
		if(nouveau!=null){
			for (TaRGroupeModeleConformite c : nouveau.getListeModeleConformite()) {
				l.add(c.getModeleConformite());
			}
		}
		listeModeleConformite.setTarget(l);
		
		listeModeleConformite.setSource(taModeleConformiteService.selectAll());
		List<TaModeleConformite> lsuppr = new ArrayList<TaModeleConformite>();
		for(TaModeleConformite s : listeModeleConformite.getSource()) {
			for(TaModeleConformite t : l) {
				if(t.getIdModeleConformite()==s.getIdModeleConformite()) {
					lsuppr.add(s);
				}
			}
		}
		
        listeModeleConformite.getSource().removeAll(lsuppr);
    }

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.modeleGroupeControle");
		b.setTitre("Modèle de groupe de controle");
		b.setTemplate("solstyce/modele_groupe_controle.xhtml");
//		b.setIdTab(LgrTab.ID_TAB_MODELE_FABRICATION);
		b.setIdTab(LgrTab.ID_TAB_MODELE_GROUPE_CONTROLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MODELE_GROUPE_CONTROLE);
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		
//		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
//			nouveau = selection;
//		}
		
		updateTab();
        
	}
	
	public void ajouter(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.modeleGroupeControle");
		b.setTitre("Modèle de groupe de controle");
		b.setTemplate("solstyce/modele_groupe_controle.xhtml");
		b.setIdTab(LgrTab.ID_TAB_MODELE_GROUPE_CONTROLE);
		b.setStyle(LgrTab.CSS_CLASS_TAB_MODELE_GROUPE_CONTROLE);
		tabsCenter.ajouterOnglet(b);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Modèle de groupe de controle", 
					"Nouveau"
					)); 
		}	
	} 
	
	public void actImprimerListeModeleGroupeDeControle(ActionEvent actionEvent) {
		
		try {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put("listeModeleGroupeDeControle", values);

			} catch (Exception e) {
				e.printStackTrace();
			}
	}    

	//	public void onRowUnSelect(UnselectEvent event)
	//	{
	//		selectionne = false;
	//	}

	public List<TaModeleGroupe> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaModeleGroupe> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
	}

	public void refresh(){
		values = taModeleGroupeService.selectAll();
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
//        requestContext.execute("PF('wvDataTableListeModeleGroupeCtrl').filter()");
	}
	public void actFermerDialog(ActionEvent actionEvent) {
		
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
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
				if(nouveau!=null &&  nouveau.getIdModeleGroupe()!=0 ) retour = false;
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
	
	public void supprimer() {
		actSupprimer(null);
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

	public DualListModel<TaModeleConformite> getListeModeleConformite() {
		return listeModeleConformite;
	}

	public void setListeModeleConformite(
			DualListModel<TaModeleConformite> listeModeleConformite) {
		this.listeModeleConformite = listeModeleConformite;
	}

	public TaModeleConformite getDetailConformite() {
		return detailConformite;
	}

	public void setDetailConformite(TaModeleConformite detailConformite) {
		this.detailConformite = detailConformite;
	}

	public TaModeleGroupe[] getSelectionS() {
		return selectionS;
	}

	public void setSelectionS(TaModeleGroupe[] selectionS) {
		this.selectionS = selectionS;
	}
}  
