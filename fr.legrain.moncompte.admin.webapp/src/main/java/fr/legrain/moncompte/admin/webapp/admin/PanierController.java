package fr.legrain.moncompte.admin.webapp.admin;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.moncompte.service.remote.IMonCompteServiceFacadeRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaCommissionServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaTypePaiementServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.app.LgrTab;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.model.TaCommission;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaPanier;
import fr.legrain.moncompte.model.TaTypePaiement;



@ManagedBean
@ViewScoped  
public class PanierController implements Serializable {  

	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;

	private List<TaPanier> filteredValues; 
	private List<TaPanier> values;
	private List<TaTypePaiement> listeTypePaiement;
	private TaTypePaiement taTypePaiment;
	private TaPanier nouveau ;
	private TaPanier selection ;
	private Boolean tableVide;
	
	private Date debutRecherche;
	private Date finRecherche;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaPanierServiceRemote taPanierService;
	private @EJB ITaCommissionServiceRemote taCommissionService;
	private @EJB IMonCompteServiceFacadeRemote taMonCompteService;
	private @EJB ITaTypePaiementServiceRemote taTypePaiementService;

	@PostConstruct
	public void postConstruct(){
		try {
			setTableVide(false);
			refresh();
			if(values == null){
				setTableVide(true);
			}
			if(!values.isEmpty()) {
				selection = values.get(0);	
			}
			
			LocalDate maintenant = LocalDate.now();
			debutRecherche = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),1).atStartOfDay(ZoneId.systemDefault()).toInstant());
			finRecherche = Date.from(LocalDate.of(maintenant.getYear(), maintenant.getMonthValue(),maintenant.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			listeTypePaiement = taTypePaiementService.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setFilteredValues(values);
	}

	public PanierController() {  
	}

	public void refresh(){
		values = taPanierService.selectAll();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");

		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			actInserer(null);
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}

	}

	public void openPanier() {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.panier");
		b.setTitre("Panier");
		b.setTemplate("admin/panier.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PANIER);
		b.setIdTab("idTabPanier");
		tabsCenter.ajouterOnglet(b);
		
		if(selection!=null) {
			values = new ArrayList<TaPanier>();
			try {
				values.add(taPanierService.findById(selection.getIdPanier()));
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void actFermerDialog(ActionEvent actionEvent) {
		 PrimeFaces.current().dialog().closeDynamic(null);
	}
	
	public void actRecherche(ActionEvent e) {
		values = taPanierService.findPanierDate(debutRecherche,finRecherche);
	}

	public void actAnnuler(ActionEvent actionEvent){
		//		values= TaPanierService.selectAll();

		if(values.size()>= 1){
			selection = values.get(0);
		}		
		nouveau = new TaPanier();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actAfficheDialogCommentaire() { 
		//l'appel d'une action permet d'utiliser "<f:setPropertyActionListener ... >" sur la commande et d'initialisé l'objet sur le quel travaillera de dialogue
		
		System.out.println(selection.getIdPanier());
		if(selection.getTaTypePaiement()!=null) {
			for (TaTypePaiement tp : listeTypePaiement) {
				if(tp.getCode().equals(selection.getTaTypePaiement().getCode())) {
					taTypePaiment = tp;
				}
			}
			
		}
	}
	
	public void actPayerPanier(){
		if(taTypePaiment!=null) {
			selection.setTaTypePaiement(taTypePaiment);
		}
		taPanierService.merge(selection);
		
		if(selection.getPaye()==null || !selection.getPaye()) {
			selection.setPaye(true);
			selection.setDatePaiement(new Date());
			taPanierService.merge(selection);
			
			//mise à jour des droit
			TaDossier dossierCourant = taMonCompteService.majAutorisation(selection);
//				Date dateFin = selection.getDatePaiement();
//				dateFin = LibDate.incrementDate(dateFin, 0, selection.getNbMois(), 0);
//				for (TaLignePanier l : selection.getLignes()) {
//					ajouteAutorisation( l.getTaProduit(),selection.getDatePaiement(),dateFin,selection.getNbMois());
//				}
//				taMonCompteService.mergeDossier(selection.getTaDossier());
						
			//Génération des commissions partenaire
			if(selection.getCodeRevendeur()!=null && !selection.getCodeRevendeur().equals("")) {
				TaCommission com = taMonCompteService.genereCommission(selection);
			}
		}
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {

			if(taPanierService.findByCode(nouveau.getLibelle()) == null){
				TaPanier retour = taPanierService.merge(nouveau, ITaPanierServiceRemote.validationContext);
				values= taPanierService.selectAll();
				selection = values.get(0);
				nouveau = new TaPanier();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

				if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
					 PrimeFaces.current().dialog().closeDynamic(retour);
				}
			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					taPanierService.merge(nouveau, ITaPanierServiceRemote.validationContext);
					values= taPanierService.selectAll();
					selection = values.get(0);
					nouveau = new TaPanier();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void actEnregistrerCommentaire(ActionEvent e) {
		taPanierService.merge(selection);
	}

	public void actInserer(ActionEvent actionEvent) {
		nouveau = new TaPanier();

		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;

		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			taPanierService.remove(selection);
			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values = taPanierService.selectAll();
		selection = null;

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
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

	public List<TaPanier> getValues(){  
		return values;
	}

	public TaPanier getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaPanier newTaPanier) {
		this.nouveau = newTaPanier;
	}

	public TaPanier getSelection() {
		return selection;
	}

	public void setSelection(TaPanier selectedTaPanier) {
		this.selection = selectedTaPanier;
	}

	public void setValues(List<TaPanier> values) {
		this.values = values;
	}

	public List<TaPanier> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaPanier> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
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

	public List<TaTypePaiement> getListeTypePaiement() {
		return listeTypePaiement;
	}

	public void setListeTypePaiement(List<TaTypePaiement> listeTypePaiement) {
		this.listeTypePaiement = listeTypePaiement;
	}

	public Date getDebutRecherche() {
		return debutRecherche;
	}

	public void setDebutRecherche(Date debutRecherche) {
		this.debutRecherche = debutRecherche;
	}

	public Date getFinRecherche() {
		return finRecherche;
	}

	public void setFinRecherche(Date finRecherche) {
		this.finRecherche = finRecherche;
	}

	public TaTypePaiement getTaTypePaiment() {
		return taTypePaiment;
	}

	public void setTaTypePaiment(TaTypePaiement taTypePaiment) {
		this.taTypePaiment = taTypePaiment;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

}  
