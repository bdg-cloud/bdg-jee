package fr.legrain.moncompte.admin.webapp.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
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
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

import fr.legrain.bdg.moncompte.service.remote.IMonCompteServiceFacadeRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaAdresseServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaDossierServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.ws.client.Admin;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.moncompte.admin.webapp.ConstWeb;
import fr.legrain.moncompte.admin.webapp.app.LgrTab;
import fr.legrain.moncompte.admin.webapp.app.TabListModelBean;
import fr.legrain.moncompte.admin.webapp.app.TabViewsBean;
import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaClientDTO;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.dto.TaUtilisateurDTO;
import fr.legrain.moncompte.model.TaAdresse;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.moncompte.model.mapping.LgrDozerMapper;


@ManagedBean
@ViewScoped  
public class ClientController implements Serializable {  
	
	@ManagedProperty(value="#{tabListModelCenterBean}")
	private TabListModelBean tabsCenter;
	
	@ManagedProperty(value="#{tabViewsBean}")
	private TabViewsBean tabViews;
	
	private List<TaClientDTO> values; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	private @EJB ITaClientServiceRemote taClientService;
	@EJB private ITaDossierServiceRemote taDossierService;
	private @EJB ITaAdresseServiceRemote taAdresseService;
	private @EJB ITaProduitServiceRemote taProduitService;
	private @EJB IMonCompteServiceFacadeRemote monCompteService;	
	private Admin adminBddService = new Admin();
	


	private TaClientDTO[] selectedTaClientDTOs; 
    private TaClientDTO newTaClientDTO = new TaClientDTO();
    private TaClientDTO selectedTaClientDTO = new TaClientDTO();
    private TaClient taClient = new TaClient();
    private TaDossier taDossier ;
    private TaDossierDTO selectedTaDossierDTO = new TaDossierDTO();
    
    private List<TaDossier> listeDossier;
    private List<TaAutorisation> listeAutorisationRemove;
    private List<TaPrixPerso> listePrixPersoRemove;
    
    private TaAdresseDTO taAdresseDTO1 = new TaAdresseDTO();
    private TaAdresseDTO taAdresseDTO2 = new TaAdresseDTO();
    private TaAdresseDTO taAdresseDTO3 = new TaAdresseDTO();
//    private TaAdresse taAdresse1 = new TaAdresse();
//    private TaAdresse taAdresse2 = new TaAdresse();
//    private TaAdresse taAdresse3 = new TaAdresse();
    private TaProduit taProduit;
    
    
    private TaAutorisation taAutorisation;
    private TaPrixPerso taPrixPerso;
    private TaDossier taDossierClient = new TaDossier();
//    private TaAutorisationDTO selectedAutorisationDTO;
    
//    private TaClientDTO OldSelectedTaClientDTO = new TaClientDTO();
    
//	private TaUnite taUnite1;
//	private TaUnite taUnite2;
//	private TaFamille taFamille;
	
//	private Boolean afficheMatierePremiere=false;
//	private Boolean afficheUtilisateurFini=false;
////	private Boolean afficheTous=true;
	
	private String urlPourEdition;
	
	private TabView tabViewClient; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach
    
    private LgrDozerMapper<TaClientDTO,TaClient> mapperUIToModel  = new LgrDozerMapper<TaClientDTO,TaClient>();
	private LgrDozerMapper<TaClient,TaClientDTO> mapperModelToUI  = new LgrDozerMapper<TaClient,TaClientDTO>();
	
    private LgrDozerMapper<TaDossierDTO,TaDossier> mapperUIToModelDossier  = new LgrDozerMapper<TaDossierDTO,TaDossier>();
	private LgrDozerMapper<TaDossier,TaDossierDTO> mapperModelToUIDossier  = new LgrDozerMapper<TaDossier,TaDossierDTO>();
	
    private LgrDozerMapper<TaAdresseDTO,TaAdresse> mapperUIToModelAdresse  = new LgrDozerMapper<TaAdresseDTO,TaAdresse>();
	private LgrDozerMapper<TaAdresse,TaAdresseDTO> mapperModelToUIAdresse   = new LgrDozerMapper<TaAdresse,TaAdresseDTO>();
	
	public ClientController() {  
		//values = getValues();
	}  
	
	@PostConstruct
	public void postConstruct(){
		refresh();
				
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		String path = request.getRequestURI().substring(request.getContextPath().length());
		urlPourEdition = request.getRequestURL().substring(0,request.getRequestURL().length()-path.length());
	}

	public void refresh(){
		values = taClientService.selectAllDTO();
		

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		//updateTabs();
	}
	
	public void updateTabs() {
		try {
			taClient = taClientService.findById(selectedTaClientDTO.getId());
			listeDossier = taDossierService.findListeDossierClient(taClient.getId());
			if(listeDossier!=null && listeDossier.size()>0) {
				taDossier=listeDossier.get(0);
			}

		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openClient() {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet("fr.legrain.onglet.client");
		b.setTitre("Client");
		b.setTemplate("admin/client.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_CLIENT);
		b.setIdTab("idTabClient");
		tabsCenter.ajouterOnglet(b);
		
		if(taClient!=null) {
			mapperModelToUI.map(taClient, selectedTaClientDTO);
			//updateTabs();
			onRowSelect(null);
		}
	}
	
	public List<TaClientDTO> getValues(){  
		return values;
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public boolean editableLigne() {
		return modeEcran.dataSetEnModif();
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
			case "inserer":	
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
	 
	public void actAnnuler(ActionEvent actionEvent) {
		
		try {
			switch (modeEcran.getMode()) {
			case C_MO_INSERTION:
				taClient=null;
				
				break;
			case C_MO_EDITION:
				if(selectedTaClientDTO.getId()!=null && selectedTaClientDTO.getId()!=0){
					taClient = taClientService.findById(selectedTaClientDTO.getId());
					}				
				break;

			default:
				break;
			}			
				
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		mapperModelToUI.map(taClient,selectedTaClientDTO );
		
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Client", "actAnnuler")); 
		}
	    
	} catch (FinderException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
	public void autoCompleteMapUIToDTO() {
//		if(taAdresse1!=null) {
//			mapperModelToUIAdresse.map(taAdresse1, selectedTaClientDTO.getAdresse1());
//			selectedTaClientDTO.setAdresse1(selectedTaClientDTO.getAdresse1());
//			taClient.setAdresse1(taAdresse1);
//		}
//		if(taAdresse2!=null) {
//			mapperModelToUIAdresse.map(taAdresse2, selectedTaClientDTO.getAdresse2());
//			selectedTaClientDTO.setAdresse2(selectedTaClientDTO.getAdresse2());
//			taClient.setAdresse2(taAdresse2);
//		}
//		if(taAdresse3!=null) {
//			mapperModelToUIAdresse.map(taAdresse3, selectedTaClientDTO.getAdresse3());
//			selectedTaClientDTO.setAdresse3(selectedTaClientDTO.getAdresse3());
//			taClient.setAdresse3(taAdresse3);
//		}
	}
	
	public void autoCompleteMapDTOtoUI() {
//		try {
//			taAdresse1 = null;
//			if(selectedTaClientDTO.getAdresse1()!=null && selectedTaClientDTO.getAdresse1().getId()!=0) {
//				taAdresse1 = taAdresseService.findById(selectedTaClientDTO.getAdresse1().getId());
//			}
//			taAdresse2 = null;
//			if(selectedTaClientDTO.getAdresse2()!=null && selectedTaClientDTO.getAdresse2().getId()!=0) {
//				taAdresse2 = taAdresseService.findById(selectedTaClientDTO.getAdresse2().getId());
//			}
//			taAdresse3 = null;
//			if(selectedTaClientDTO.getAdresse3()!=null && selectedTaClientDTO.getAdresse3().getId()!=0) {
//				taAdresse3 = taAdresseService.findById(selectedTaClientDTO.getAdresse3().getId());
//			}			
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
	}
	
	public boolean adresseEstVide(TaAdresseDTO adresseDTO){
		if(adresseDTO!=null){
		if(adresseDTO.getAdresse1()!=null  && !adresseDTO.getAdresse1().equals(""))return false;
		if(adresseDTO.getAdresse2()!=null  && !adresseDTO.getAdresse2().equals(""))return false;
		if(adresseDTO.getAdresse3()!=null  && !adresseDTO.getAdresse3().equals(""))return false;
		if(adresseDTO.getCodePostal()!=null  && !adresseDTO.getCodePostal().equals(""))return false;
		if(adresseDTO.getVille()!=null  && !adresseDTO.getVille().equals(""))return false;
		if(adresseDTO.getPays()!=null  && !adresseDTO.getPays().equals(""))return false;
		if(adresseDTO.getEmail()!=null  && !adresseDTO.getEmail().equals(""))return false;
		if(adresseDTO.getNomEntreprise()!=null  && !adresseDTO.getNomEntreprise().equals(""))return false;
		if(adresseDTO.getNumFax()!=null  && !adresseDTO.getNumFax().equals(""))return false;
		if(adresseDTO.getWeb()!=null  && !adresseDTO.getWeb().equals(""))return false;
		if(adresseDTO.getNumPort()!=null  && !adresseDTO.getNumPort().equals(""))return false;
		if(adresseDTO.getNumTel1()!=null  && !adresseDTO.getNumTel1().equals(""))return false;
		if(adresseDTO.getNumTel2()!=null  && !adresseDTO.getNumTel2().equals(""))return false;
		}
		return true;
	}
	
    public void onTabChange(TabChangeEvent event) {
        if(event.getData()!=null && event.getData() instanceof TaDossier)
        	taDossier=(TaDossier) event.getData();
    }
    
	public void actEnregistrer(ActionEvent actionEvent) {
		
//		TaClient taClient = new TaClient();
		autoCompleteMapUIToDTO();
		mapperUIToModel.map(selectedTaClientDTO, taClient);
		
		
		if(!adresseEstVide(taAdresseDTO1)) {
			if(taClient.getAdresse1()==null)taClient.setAdresse1(new TaAdresse());
			mapperUIToModelAdresse.map(taAdresseDTO1, taClient.getAdresse1());
		}
		if(!adresseEstVide(taAdresseDTO2)) {
			if(taClient.getAdresse2()==null)taClient.setAdresse2(new TaAdresse());
			mapperUIToModelAdresse.map(taAdresseDTO2, taClient.getAdresse2());
		}
		if(!adresseEstVide(taAdresseDTO3)) {
			if(taClient.getAdresse3()==null)taClient.setAdresse3(new TaAdresse());
			mapperUIToModelAdresse.map(taAdresseDTO3, taClient.getAdresse3());
		}
		try {		
			List<TaAutorisation> listTemp = new LinkedList<TaAutorisation>();
			//listeDossier = taDossierService.findListeDossierClient(taClient.getId());
			if(listeDossier!=null){
				for (TaDossier taDossier : listeDossier) {
					for (TaAutorisation taAutorisation : taDossier.getListeAutorisation()) { //TODO Gere le multi dossier
						if(taAutorisation==null ||taAutorisation.getTaDossier()==null || taAutorisation.getTaProduit()==null)
							listTemp.add(taAutorisation);
					}

					for (TaAutorisation taAutorisation : listTemp) {
						taDossier.getListeAutorisation().remove(taAutorisation);
					}
				}
			}
			
			//idem pour les prix perso au dossier
			List<TaPrixPerso> listTempPrix = new LinkedList<TaPrixPerso>();
			if(listeDossier!=null){
				for (TaDossier taDossier : listeDossier) {
					for (TaPrixPerso taPrix : taDossier.getListePrixPerso()) { //TODO Gere le multi dossier
						if(taPrix==null ||taPrix.getTaDossier()==null || taPrix.getTaProduit()==null)
							listTempPrix.add(taPrix);
					}

					for (TaPrixPerso taPrix : listTempPrix) {
						taDossier.getListePrixPerso().remove(taPrix);
					}
				}
			}			

			taClient = taClientService.merge(taClient,ITaClientServiceRemote.validationContext);
			for (TaDossier dossier : listeDossier) {
				taDossierService.merge(dossier);
				if(listeAutorisationRemove!=null){
					for (TaAutorisation taAutorisation : listeAutorisationRemove) {
						if(taAutorisation.getTaDossier().getCode().equals(dossier.getCode()))
							if(taAutorisation.getIdAutorisation()!=null && taAutorisation.getIdAutorisation()!=0)
								taDossierService.removeLigneAutorisation(dossier, taAutorisation);
					}
				}
				if(listePrixPersoRemove!=null){
					for (TaPrixPerso taPrixPerso : listePrixPersoRemove) {
						if(taPrixPerso.getTaDossier().getCode().equals(dossier.getCode()))
							if(taPrixPerso.getId()!=null && taPrixPerso.getId()!=0)
								taDossierService.removeLignePrixPerso(dossier, taPrixPerso);
					}	
				}			
			}
			
			
			taClient= taClientService.findById(taClient.getId());
			mapperModelToUI.map(taClient, selectedTaClientDTO);
			selectedTaClientDTO.setId(taClient.getId());		

			
			updateTabs();
			
			autoCompleteMapDTOtoUI();
			
			if ((modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0)) {
				values.add(selectedTaClientDTO);
			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Client", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Clients", e.getMessage()));
		}
	}
	
	public void actEnregistrerPrix(ActionEvent actionEvent) {
		taPrixPerso.setTaDossier(taDossier);
		if(!taDossier.getListePrixPerso().contains(taPrixPerso)) 
			taDossier.getListePrixPerso().add(taPrixPerso);
		
		try{
			
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Client", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Clients", e.getMessage()));
		}
	}
	
	public void ajoutDossier(TaDossier taDossier) {
		try {
			/*
			 * Vérication que le nom du dossier et le compte utilisateur(email) n'existe pas
			 */

			adminBddService.ajoutDossier(taDossier.getCode());
			
						
			
			/*
			 * Affectation module
			 */
			GregorianCalendar d = new GregorianCalendar();
			GregorianCalendar d2 = new GregorianCalendar();
			Date maintenant = new Date();
			d.setTime(maintenant);
			d2.setTime(LibDate.incrementDate(maintenant, 45, 0, 0)); //+45 jours
			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
			XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(d2);
	
			List<TaAutorisation> aut = new ArrayList<TaAutorisation>();
			TaAutorisation a1 = null;
			List<TaProduit> selectedModules = taProduitService.selectAll();
			for (TaProduit p : selectedModules) {
				a1 = new TaAutorisation();
				a1.setDateAchat(d.getTime());
				a1.setDateFin(d2.getTime());
				a1.setTaProduit(p);
				a1.setTaDossier(taDossier);
//				if(!c.getListeAutorisation().contains(a1)) {
				taDossier.getListeAutorisation().add(a1);
//				};
				aut.add(a1);
			}
			
			/*
			 * Enregistrement du client avec ses autorisations
			 */
			taDossierService.merge(taDossier);
			
			
			/*
			 * MAJ autorisations
			 */
			taDossierService.initDroitModules(taDossier);
			

			
			
		}  catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dossier créé ", 
	    		"Le dossier '"+taDossier.getCode()+"' a bien été créé.\n "
	    				+ "Penser à relancer le serveur pour prendre en compte le nouveau sous domaine."
	    		)); 
		}
	}
	
	public void actEnregistrerDossier(ActionEvent actionEvent) {
		try{
		taDossierService.merge(taDossierClient);
		ajoutDossier(taDossierClient);
		updateTabs();

			
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Client", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Clients", e.getMessage()));
		}
	}
	
	public void actEnregistrerAutorisation(ActionEvent actionEvent) {
//		mapperUIToModelDossier.map(selectedTaDossierDTO, taDossier);
		taAutorisation.setTaDossier(taDossier); 
		if(!taDossier.getListeAutorisation().contains(taAutorisation)) 
			taDossier.getListeAutorisation().add(taAutorisation);
		try{
			
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Client", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Clients", e.getMessage()));
		}
	}
	
	public void actInserer(ActionEvent actionEvent) {
		selectedTaClientDTO = new TaClientDTO();
		taClient = new TaClient();

		taAdresseDTO1 = new TaAdresseDTO();
		taAdresseDTO2 = new TaAdresseDTO();
		taAdresseDTO3 = new TaAdresseDTO();
		
//		selectedTaClientDTO.setPrixHT(new BigDecimal(0));
//		selectedTaClientDTO.setPrixTTC(new BigDecimal(0));
//		selectedTaClientDTO.setCodeClient(taClientService.genereCode());
//		selectedTaClientDTO.setActif(true);
//		selectedTaClientDTO.setStockMinClient(new BigDecimal(-1));
		
		autoCompleteMapDTOtoUI();
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Client", "actInserer")); 
		}
	}
	
	public void actInsererLigneDossier(ActionEvent actionEvent) {

		taDossierClient = new TaDossier();
		taDossierClient.setId(0);
		taDossierClient.setActif(true);
		taDossierClient.setAccesWs(false);
		taDossierClient.setNbUtilisateur(1);
		taDossierClient.setNbPosteInstalle(0);
		taDossierClient.setTaClient(taClient);
		listeDossier.add(taDossierClient);

		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Autorisation", "actInserer"));
		}
	}
	
	public void actInsererLigneAutorisation(ActionEvent actionEvent) {
//		selectedAutorisationDTO = new TaAutorisationDTO();
		TaDossier d =  (TaDossier) actionEvent.getComponent().getAttributes().get("dos");
		taDossier=d;
		taAutorisation = new TaAutorisation();
		taAutorisation.setIdAutorisation(0);
		taAutorisation.setTaDossier(taDossier);
		
		if(taDossier.getListeAutorisation()==null)taDossier.setListeAutorisation(new LinkedList<TaAutorisation>()); //TODO Gere le multi dossier
		taDossier.getListeAutorisation().add(taAutorisation);
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Autorisation", "actInserer"));
		}
	}
	
	public void actInsererLignePrix(ActionEvent actionEvent) {
		TaDossier d =  (TaDossier) actionEvent.getComponent().getAttributes().get("dos");
		taDossier=d;
		taPrixPerso = new TaPrixPerso();
		taPrixPerso.setId(0);
		taPrixPerso.setTaDossier(taDossier);
		
		if(taDossier.getListePrixPerso()==null)taDossier.setListePrixPerso(new LinkedList<TaPrixPerso>()); //TODO Gere le multi dossier
		taDossier.getListePrixPerso().add(taPrixPerso);
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
		if(ConstWeb.DEBUG) {
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("prix perso", "actInserer"));
		}
	}
	
	public void actModifier(ActionEvent actionEvent) {
		
		try {
			taClient = taClientService.findById(selectedTaClientDTO.getId());
			
			
		
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			
			if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Client", "actModifier"));
			}
		    
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actModifierLigne(ActionEvent actionEvent) {
		
		try {	
						
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			
			if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Client", "actModifierLigne"));
			}
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void actSupprimerLignes(ActionEvent actionEvent) {
		try {
//			if(selectedAutorisationDTO!=null && selectedAutorisationDTO.getId()!=null){
//				for (TaAutorisation autorisation : taDossier.getListeAutorisation()) { //TODO Gere le multi dossier
//					if(autorisation.getIdAutorisation()==selectedAutorisationDTO.getId())
//						taAutorisation=autorisation;
//				}
//			}
			if(taAutorisation!=null){
//				taAutorisation.setTaDossier(null);
				taDossier.getListeAutorisation().remove(taAutorisation);
				if(listeAutorisationRemove==null)listeAutorisationRemove=new LinkedList<TaAutorisation>();
				listeAutorisationRemove.add(taAutorisation);
			}
			
			

			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Autorisation", "actSupprimer"));
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}	    
	}
	
	public void actSupprimerLignesPrix(ActionEvent actionEvent) {
		try {

			if(taPrixPerso!=null){
//				taPrixPerso.setTaDossier(null);
				taDossier.getListePrixPerso().remove(taPrixPerso);
				if(listePrixPersoRemove==null)listePrixPersoRemove=new LinkedList<TaPrixPerso>();
				listePrixPersoRemove.add(taPrixPerso);
			}	
			

			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("prix perso", "actSupprimer"));
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}	    
	}
	public void actSupprimer(ActionEvent actionEvent) {
		TaClient taClient = new TaClient();
		try {
			if(selectedTaClientDTO!=null && selectedTaClientDTO.getId()!=null){
				taClient = taClientService.findById(selectedTaClientDTO.getId());
			}

			taClientService.remove(taClient);
			values.remove(selectedTaClientDTO);
			
			if(!values.isEmpty()) {
				selectedTaClientDTO = values.get(0);
			}else selectedTaClientDTO=new TaClientDTO();

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Client", "actSupprimer"));
			}
		} catch (RemoveException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}	    
	}
	
	public void actFermer(ActionEvent actionEvent) {
		LgrTab typeOngletDejaOuvert = null;
		for (LgrTab o : tabsCenter.getOnglets()) {
			if(LgrTab.TYPE_TAB_ARTICLE.equals(o.getTypeOnglet())) {
				typeOngletDejaOuvert = o;
			}
		}
		
		if(typeOngletDejaOuvert!=null) {
//			if(!values.isEmpty()) {
//				selectedTaTiersDTO = values.get(0);
//			}
			tabsCenter.supprimerOnglet(typeOngletDejaOuvert);
		}
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Client", "actFermer"));
		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Client", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("client", taUtilisateurService.findById(selectedTaClientDTO.getId()));
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("ClientController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
    
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet("fr.legrain.onglet.client");
		b.setTitre("Nouveau");
		b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setTemplate("admin/client.xhtml");
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Client", 
	    		"Nouveau"
	    		)); 
		}
    } 
    
    public void supprimer(ActionEvent actionEvent) {
    	actSupprimer(actionEvent);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }
    
	public boolean pasDejaOuvert() {
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			
			onRowSelect(event);
			
//			autoCompleteMapDTOtoUI();
//			
//			updateTabs();

			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Client", 
					"Chargement de l'client N°"+selectedTaClientDTO.getNom()
					)); 
			}
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_ARTICLE);
		}
			
	} 
	
    public void onRowSelect(SelectEvent event) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet("fr.legrain.onglet.client");
		b.setTitre("Client "+selectedTaClientDTO.getNom());
		b.setTemplate("admin/client.xhtml");
		b.setStyle(LgrTab.CSS_CLASS_TAB_PARAM);
		b.setParamId(LibConversion.integerToString(selectedTaClientDTO.getId()));
		
		int pos = 0;
		tabsCenter.ajouterOnglet(b);
		pos = tabViewClient!=null?tabViewClient.getActiveIndex():0;
		tabViews.selectionneOngletCentre(b);
		if(tabViewClient!=null) {
			tabViewClient.setActiveIndex(pos);
		}
		
		taAdresseDTO1= new TaAdresseDTO();
		taAdresseDTO2= new TaAdresseDTO();
		taAdresseDTO3= new TaAdresseDTO();
		if(selectedTaClientDTO.getAdresse1()!=null) taAdresseDTO1= selectedTaClientDTO.getAdresse1();
		if(selectedTaClientDTO.getAdresse2()!=null) taAdresseDTO2= selectedTaClientDTO.getAdresse2();
		if(selectedTaClientDTO.getAdresse3()!=null) taAdresseDTO3= selectedTaClientDTO.getAdresse3();
		
		autoCompleteMapDTOtoUI();
		
		updateTabs();
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Client", 
	    		"Chargement du client N°"+selectedTaClientDTO.getNom()
	    		)); 
		}
    } 
    
	public TaClientDTO[] getSelectedTaClientDTOs() {
		return selectedTaClientDTOs;
	}

	public void setSelectedTaClientDTOs(TaClientDTO[] selectedTaClientDTOs) {
		this.selectedTaClientDTOs = selectedTaClientDTOs;
	}

	public TaClientDTO getNewTaClientDTO() {
		return newTaClientDTO;
	}

	public void setNewTaClientDTO(TaClientDTO newTaClientDTO) {
		this.newTaClientDTO = newTaClientDTO;
	}

	public TaClientDTO getSelectedTaClientDTO() {
		return selectedTaClientDTO;
	}

	public void setSelectedTaClientDTO(TaClientDTO selectedTaClientDTO) {
		this.selectedTaClientDTO = selectedTaClientDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public void validateAdresse(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		String nomChamp = null;
		try {
			nomChamp =  (String) component.getAttributes().get("nomChamp");

			validateUIField(nomChamp,value);
			TaAdresseDTO dtoTemp = new TaAdresseDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taAdresseService.validateDTOProperty(dtoTemp, nomChamp, ITaAdresseServiceRemote.validationContext );
		} catch(NoSuchMethodException ex) {
			try {

			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateDossier(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		String nomChamp = null;
		try {
			nomChamp =  (String) component.getAttributes().get("nomChamp");

			validateUIField(nomChamp,value);
			TaDossierDTO dtoTemp = new TaDossierDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taDossierService.validateDTOProperty(dtoTemp, nomChamp, ITaDossierServiceRemote.validationContext );
		} catch(NoSuchMethodException ex) {
			try {

			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	public void validateClient(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		String nomChamp = null;
		try {
			nomChamp =  (String) component.getAttributes().get("nomChamp");

			validateUIField(nomChamp,value);
			TaClientDTO dtoTemp = new TaClientDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taClientService.validateDTOProperty(dtoTemp, nomChamp, ITaClientServiceRemote.validationContext );
		} catch(NoSuchMethodException ex) {
			try {
				TaAdresseDTO dtoTemp = new TaAdresseDTO();
				PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
				taAdresseService.validateDTOProperty(dtoTemp, nomChamp, ITaClientServiceRemote.validationContext );
			} catch(Exception e) {
				e.printStackTrace();
			}
		} catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}
	
	public void validateUtilisateur(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		String msg = "";
		String nomChamp = null;
		try {
			nomChamp =  (String) component.getAttributes().get("nomChamp");

			validateUIField(nomChamp,value);
			TaUtilisateurDTO dtoTemp = new TaUtilisateurDTO();
			PropertyUtils.setSimpleProperty(dtoTemp, nomChamp, value);
			taUtilisateurService.validateDTOProperty(dtoTemp, nomChamp, ITaUtilisateurServiceRemote.validationContext );
		}  catch(Exception e) {
			msg += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		validateUIField(nomChamp,value);
		taAutorisation.setTaProduit((TaProduit)event.getObject());
		
	}

	
	public List<TaProduit> produitAutoComplete(String query) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Query", query));
		if(query!=null && query.equals("")){
			taProduit=null;
		}
	        List<TaProduit> allValues = taProduitService.selectAll();
	        List<TaProduit> filteredValues = new ArrayList<TaProduit>();
	         
	        for (int i = 0; i < allValues.size(); i++) {
	        	TaProduit civ = allValues.get(i);
	            if(query.equals("*") || civ.getCode().toLowerCase().contains(query.toLowerCase())) {
	                filteredValues.add(civ);
	            }
	        }
	         
	        return filteredValues;
		
    }

//	public List<TaTypeUtilisateur> typeUtilisateurAutoComplete(String query) {
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Query", query));
//		if(query!=null && query.equals("")){
//			taTypeUtilisateur=null;
//		}
//	        List<TaTypeUtilisateur> allValues = taTypeUtilisateurService.selectAll();
//	        List<TaTypeUtilisateur> filteredValues = new ArrayList<TaTypeUtilisateur>();
//	         
//	        for (int i = 0; i < allValues.size(); i++) {
//	        	TaTypeUtilisateur civ = allValues.get(i);
//	            if(query.equals("*") || civ.getCode().toLowerCase().contains(query.toLowerCase())) {
//	                filteredValues.add(civ);
//	            }
//	        }
//	         
//	        return filteredValues;
//		
//    }

	

	public boolean validateUIField(String nomChamp,Object value) {
//		try {
//			
//			if(nomChamp.equals(Const.C_CODE_FAMILLE)) {
//					TaFamille entity =null;
//					if(value!=null){
//						if(value instanceof TaFamille){
//						entity=(TaFamille) value;
////						entity = taFamilleService.findByCode(entity.getCodeFamille());
//						}else{
//							entity = taFamilleService.findByCode((String)value);
//						}
//					}
//					taClient.setTaFamille(entity);
//			
//			} else if(nomChamp.equals(Const.C_CODE_TVA)) {
//					TaTva entity = null;
//					if(value!=null){
//						if(value instanceof TaTva){
//						entity=(TaTva) value;
////						entity = taTvaService.findByCode(entity.getCodeTva());
//						}else{
//							entity = taTvaService.findByCode((String)value);
//						}
//					}					
//					taClient.setTaTva(entity);
//					taClient.initCodeTTva();
//					if(taClient.getTaTTva()!=null)
//						selectedTaClientDTO.setCodeTTva(taClient.getTaTTva().getCodeTTva());
//				else selectedTaClientDTO.setCodeTTva(null);					
//			
//			} else if(nomChamp.equals(Const.C_CODE_T_TVA)) {
//					TaTTva entity = null;
//					if(value!=null){
//						if(value instanceof TaTTva){
//						entity=(TaTTva) value;
////						entity = taTTvaService.findByCode(entity.getCodeTTva());
//						}else{
//							entity = taTTvaService.findByCode((String)value);
//						}
//					}					
//					taClient.setTaTTva(entity);
//					taClient.initCodeTTva();
//					if(taClient.getTaTTva()!=null)
//						selectedTaClientDTO.setCodeTTva(taClient.getTaTTva().getCodeTTva());
//				else selectedTaClientDTO.setCodeTTva(null);					
//				
//			} else if(nomChamp.equals(Const.C_CODE_UNITE2)) {				
//					TaUnite entity = null;
//					if(value!=null){
//						if(taClient.getTaRapportUnite()==null) taClient.setTaRapportUnite(new TaRapportUnite());
//						if(value instanceof TaUnite){
//						entity=(TaUnite) value;
////						entity = taUniteService.findByCode(entity.getCodeUnite());
//						}else{
//							entity = taUniteService.findByCode((String)value);
//						}
//					}
//					
//					if(entity!=null){
//					taClient.getTaRapportUnite().setTaUnite1(taClient.getTaPrix().getTaUnite());
//					taClient.getTaRapportUnite().setTaUnite2(entity);
//					}else
//					if(taClient.getTaRapportUnite()!=null){
//						taClient.removeRapportUnite(taClient.getTaRapportUnite());
//					}
//				initEtatRapportUniteUI();
//				
//			} else if(nomChamp.equals(Const.C_RAPPORT)) {
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taClientService.rapportUniteEstRempli(selectedTaClientDTO)){
//					taClient.getTaRapportUnite().setRapport(dto.getRapport());
//				}
//				
//			}  else if(nomChamp.equals(Const.C_SENS_RAPPORT)) {
//
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taClientService.rapportUniteEstRempli(selectedTaClientDTO)){
//					taClient.getTaRapportUnite().setSens(LibConversion.booleanToInt(dto.getSens()));
//				}
//			} else if(nomChamp.equals(Const.C_NB_DECIMAL)) {
//				TaRapportUniteDTO dto = new TaRapportUniteDTO();
//				PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//				if (taClientService.rapportUniteEstRempli(selectedTaClientDTO)){
//					taClient.getTaRapportUnite().setNbDecimal(dto.getNbDecimal());
//				}
//
//			} else if(nomChamp.equals(Const.C_CODE_UNITE)) {
//				
//					TaUnite entity = null;
//					if(value!=null){
//						if(value instanceof TaUnite){
//						entity=(TaUnite) value;
////						entity = taUniteService.findByCode(entity.getCodeUnite());
//						}else{
//							entity = taUniteService.findByCode((String)value);
//						}
//					} 
//					if(entity!=null){
//						taClient=taClientService.initPrixClient(taClient,selectedTaClientDTO);
//						taClient.getTaPrix().setTaUnite(entity);
//						}else{
//							if (taClient.getTaPrix()!=null)
//							{
//								taClient.getTaPrix().setTaUnite(null);
//								if(taClient.getTaPrix().getPrixPrix()!=null && 
//										taClient.getTaPrix().getPrixPrix().equals(0)&& 
//										taClient.getTaPrix().getPrixttcPrix()!=null && 
//										taClient.getTaPrix().getPrixttcPrix().equals(0))
//									taClient.removePrix(taClient.getTaPrix());
//							}
//						}
//						initEtatRapportUniteUI();
//				
//			} else if(nomChamp.equals(Const.C_PRIX_PRIX)) {
//
//				TaPrixDTO dto = new TaPrixDTO();
//				TaPrix f = new TaPrix();
//				int change =0;
//
//						
//						if(value!=null && !value.equals(""))taClientService.initPrixClient(taClient,selectedTaClientDTO);
//						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//						PropertyUtils.setSimpleProperty(f, nomChamp, value);
//						if(value!=null && selectedTaClientDTO.getPrixPrix()!=null)
//							change =dto.getPrixPrix().compareTo(selectedTaClientDTO.getPrixPrix());
//						else change=-1;
//						dto.setPrixttcPrix(selectedTaClientDTO.getPrixttcPrix());							
////						validationClient.validate(dto,nomChamp,ITaClientServiceRemote.validationContext);
//						
//
//				if (change!=0||dto.getPrixPrix().compareTo(new BigDecimal(0))==0){
//					if((value==null||value.equals("")||value.equals(0))&& taClient.getTaPrix()!=null){
//					//taClient.setTaPrix(null);
//					taClient.removePrix(taClient.getTaPrix());
//				}else{
//					if((value!=null && !value.equals(""))&& taClient.getTaPrix()!=null){
//						taClientService.initPrixClient(taClient,selectedTaClientDTO);						
//						taClient.getTaPrix().setPrixPrix(dto.getPrixPrix());
//						taClient.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
//						taClient.getTaPrix().majPrix();
//					}
//				}
//					selectedTaClientDTO.setPrixPrix(taClient.getTaPrix().getPrixPrix());				
//					selectedTaClientDTO.setPrixttcPrix(taClient.getTaPrix().getPrixttcPrix());	 						
//				}
//			}else if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
//				TaPrixDTO dto = new TaPrixDTO();
//				int change =0;
//
//						if(value!=null && !value.equals(""))taClientService.initPrixClient(taClient,selectedTaClientDTO);
//						PropertyUtils.setSimpleProperty(dto, nomChamp, value);
//						if(value!=null && selectedTaClientDTO.getPrixttcPrix()!=null)
//						change =dto.getPrixttcPrix().compareTo(selectedTaClientDTO.getPrixttcPrix());
//					else change=-1;
//						dto.setPrixPrix(selectedTaClientDTO.getPrixPrix());						
////						validationClient.validate(dto,nomChamp,ITaClientServiceRemote.validationContext);
//
//
//				if(change!=0||dto.getPrixttcPrix().compareTo(new BigDecimal(0))==0){
//					if((value==null||value.equals("")||value.equals(0))&& taClient.getTaPrix()!=null){
//					//taClient.setTaPrix(null);
//					taClient.removePrix(taClient.getTaPrix());
//				}else{
//					if((value!=null && !value.equals(""))&& taClient.getTaPrix()!=null){
//						taClientService.initPrixClient(taClient,selectedTaClientDTO);						
//						taClient.getTaPrix().setPrixPrix(dto.getPrixPrix());
//						taClient.getTaPrix().setPrixttcPrix(dto.getPrixttcPrix());
//						taClient.getTaPrix().majPrixTTC();
//					}
//				}
//					selectedTaClientDTO.setPrixPrix(taClient.getTaPrix().getPrixPrix());				
//					selectedTaClientDTO.setPrixttcPrix(taClient.getTaPrix().getPrixttcPrix()); 						
//				}
//			} else if(nomChamp.equals(Const.C_QTE_TITRE_TRANSPORT_L_DOCUMENT)) {
//
//				if(!((value==null || value.equals("")) && (!taClientService.crdEstRempli(selectedTaClientDTO)))){	
//					if((value==null || value.equals("") || ((BigDecimal)value).doubleValue()<=0) && taClientService.crdEstRempli(selectedTaClientDTO))
//						value="1";							
//					value=LibConversion.stringToBigDecimal(value.toString());
//				}							
//
//
//					TaRTaTitreTransport entity = new TaRTaTitreTransport();
//					if(value!=null){
//						if(value instanceof TaRTaTitreTransport){
//						entity=(TaRTaTitreTransport) value;
//						}else{
//							entity = taRTitreTransportService.findByCode((String)value);
//						}
//					}
//					
//
//					if(taClientService.crdEstRempli(selectedTaClientDTO) && value!=null){
//						taClientService.initTaRTaTitreTransport(taClient);
//						taClient.getTaRTaTitreTransport().setQteTitreTransport(entity.getQteTitreTransport());
//					} else {
//						if(taClient.getTaRTaTitreTransport()!=null) {
//							taClient.setTaRTaTitreTransport(null);
//						}
//					}						
//
//			} else if(nomChamp.equals(Const.C_CODE_TITRE_TRANSPORT)) {
//
//
//					TaTitreTransport entity = new TaTitreTransport();
//					if(value!=null){
//						if(value instanceof TaTitreTransport){
//						entity=(TaTitreTransport) value;
//						}else{
//							entity = taTitreTransportService.findByCode((String)value);
//						}
//					}					
//					
//					if (!value.equals("")){
//						taClientService.initTaRTaTitreTransport(taClient);
//					taClient.getTaRTaTitreTransport().setTaTitreTransport(entity);
//				} else {
//					if(taClient.getTaRTaTitreTransport()!=null) {
//						taClient.setTaRTaTitreTransport(null);
//					}
//				}						
//			
//			} else {
//					TaClientDTO dto = new TaClientDTO();
//					TaClient entity = new TaClient();
//					if(nomChamp.equals(Const.C_ACTIF_ARTICLE)) { //traitement des booleens
//						if(value instanceof Integer) 
//							if((Integer)value==1) value=new Boolean(true); else new Boolean(false);
//					}
//					if(selectedTaClientDTO!=null
//							&& selectedTaClientDTO !=null
//							&& selectedTaClientDTO.getId()!=null) {
//						entity.setIdClient(selectedTaClientDTO.getId());
//					}
//
//
//					if(taClient!=null) {
//						if(taClient.getTaCatalogueWeb()==null) {
//							taClient.setTaCatalogueWeb(new TaCatalogueWeb());
//						}
//						if(nomChamp.equals(Const.C_LIBELLEC_ARTICLE)) {
//							if(LibChaine.empty(taClient.getTaCatalogueWeb().getUrlRewritingCatalogueWeb())
//									|| ConstPreferencesClient.REGENERATION_URL_REWRITING_POUR_CATALOGUE_WEB_A_PARTIR_ECRAN_PRINCIPAL) {
//								taClient.getTaCatalogueWeb().setUrlRewritingCatalogueWeb(LibChaine.toUrlRewriting((String)value));
//							}
//						} else if(nomChamp.equals(Const.C_LIBELLEL_ARTICLE)) {
//							if(LibChaine.empty(taClient.getTaCatalogueWeb().getDescriptionLongueCatWeb())
//									|| ConstPreferencesClient.REGENERATION_DESCRIPTION_POUR_CATALOGUE_WEB_A_PARTIR_ECRAN_PRINCIPAL) {
//								taClient.getTaCatalogueWeb().setDescriptionLongueCatWeb((String)value);
//							}
//						}
//					}
//					
//			}
//			return false;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return false;
	}
	public void actMajDroitsDossierCourant(ActionEvent actionEvent) {
//		initDroitModules(taDossier.getCode());
		taDossierService.initDroitModules(taDossier);
	}
	
	public void actMajDroitsDossiersClient(ActionEvent actionEvent) {
		if(taClient!=null){
			listeDossier = taDossierService.findListeDossierClient(taClient.getId());
			for (TaDossier dos : listeDossier) {
				taDossierService.initDroitModules(dos);
			}
		}
	}
	
	public void actMajDroitsDossiersClientAll(ActionEvent actionEvent) {
		for (TaClientDTO c : values) {
			TaClient client;
			try {
				client = taClientService.findByCode(c.getCode());
				if(client!=null){
					listeDossier = taDossierService.findListeDossierClient(taClient.getId());
					for (TaDossier dos : listeDossier) {
						taDossierService.initDroitModules(dos);
					}
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
//	public void initDroitModules(TaDossier dossier) {
//		AutorisationWebServiceClientCXF wsClient = new AutorisationWebServiceClientCXF();
//		//String codeTiers = taClient.getCode();//"demo"
//		//codeTiers="aaa";
//		int idProduit = 4;
//		
//		try {
//			ListeModules lm = wsClient.listeModulesAutorises(dossier.getCode(), idProduit);
//			fr.legrain.autorisation.xml.ListeModules listeXml = new fr.legrain.autorisation.xml.ListeModules();
//			if(lm!=null) {
//				System.out.println("Modules autorisés pour ce dossier avant update: ");
//				
//				fr.legrain.autorisation.xml.Module moduleXml = null;
//				for (Module m : lm.getModules().getModule()) {
//					System.out.println(m.getId());
//					
//					moduleXml = new fr.legrain.autorisation.xml.Module();
//					moduleXml.id = m.getId();
//					moduleXml.nom = m.getNom();
//					listeXml.module.add(moduleXml);
//				}
//				System.out.println("=============================");
//			}
//			
//			System.out.println("Modules autorisés pour ce dossier après update: ");
//			
//			List<TaAutorisation> aut = dossier.getListeAutorisation();
//			
//			
//			listeXml = new fr.legrain.autorisation.xml.ListeModules();
//			fr.legrain.autorisation.xml.Module moduleXml = null;
//			for (TaAutorisation taAutorisation : aut) {
//				listeXml = ajouteAutorisation(listeXml,taAutorisation.getTaProduit());
//			}
//			
//			listeXml.nbPosteClient = LibConversion.integerToString(dossier.getNbPosteInstalle());
//			listeXml.nbUtilisateur = LibConversion.integerToString(dossier.getNbUtilisateur());
//			listeXml.accessWebservice = LibConversion.booleanToString(dossier.getAccesWs());
//			
//			wsClient.createUpdateDroits(dossier.getCode(), idProduit, listeXml);
//			System.out.println("createUpdateDroits ok ");
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public fr.legrain.autorisation.xml.ListeModules ajouteAutorisation(fr.legrain.autorisation.xml.ListeModules l, TaProduit p) {
//		fr.legrain.autorisation.xml.Module moduleXml = new fr.legrain.autorisation.xml.Module();
//		moduleXml.id = p.getIdentifiantModule();
//		moduleXml.nom = p.getLibelle();
//		if(!l.contientModule(moduleXml)) {
//			l.module.add(moduleXml);
//		}
//		System.out.println(moduleXml.id);
//		//gestion des dépendances et compostion
//		for(TaProduit prod : p.getListeProduitDependant()) {
//			moduleXml = new fr.legrain.autorisation.xml.Module();
//			moduleXml.id = prod.getIdentifiantModule();
//			if(!l.contientModule(moduleXml)) {
//				l.module.add(moduleXml);
//			}
//			//appel récursif à faire
//			if(prod.getListeProduitDependant()!=null) {
//				l = ajouteAutorisation(l,prod);
//			}
//		}
//		for(TaProduit prod : p.getListeSousProduit()) {
//			moduleXml = new fr.legrain.autorisation.xml.Module();
//			moduleXml.id = prod.getIdentifiantModule();
//			moduleXml.nom = prod.getLibelle();
//			if(!l.contientModule(moduleXml)) {
//				l.module.add(moduleXml);
//			}
//			//appel récursif à faire
//			if(prod.getListeSousProduit()!=null) {
//				l = ajouteAutorisation(l,prod);
//			}
//		}
//		return l;
//	}

	public String getUrlPourEdition() {
		return urlPourEdition;
	}

	public void setUrlPourEdition(String urlPourEdition) {
		this.urlPourEdition = urlPourEdition;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public TabView getTabViewClient() {
		return tabViewClient;
	}

	public void setTabViewClient(TabView tabViewClient) {
		this.tabViewClient = tabViewClient;
	}

	public TaAdresseDTO getTaAdresseDTO1() {
		return taAdresseDTO1;
	}

	public void setTaAdresseDTO1(TaAdresseDTO taAdresseDTO1) {
		this.taAdresseDTO1 = taAdresseDTO1;
	}

	public TaAdresseDTO getTaAdresseDTO2() {
		return taAdresseDTO2;
	}

	public void setTaAdresseDTO2(TaAdresseDTO taAdresseDTO2) {
		this.taAdresseDTO2 = taAdresseDTO2;
	}

	public TaAdresseDTO getTaAdresseDTO3() {
		return taAdresseDTO3;
	}

	public void setTaAdresseDTO3(TaAdresseDTO taAdresseDTO3) {
		this.taAdresseDTO3 = taAdresseDTO3;
	}

	public TaClient getTaClient() {
		return taClient;
	}

	public void setTaClient(TaClient taClient) {
		this.taClient = taClient;
	}

	public TaAutorisation getTaAutorisation() {
		return taAutorisation;
	}

	public void setTaAutorisation(TaAutorisation taAutorisation) {
		this.taAutorisation = taAutorisation;
	}

//	public TaAutorisationDTO getSelectedAutorisationDTO() {
//		return selectedAutorisationDTO;
//	}
//
//	public void setSelectedAutorisationDTO(TaAutorisationDTO selectedAutorisationDTO) {
//		this.selectedAutorisationDTO = selectedAutorisationDTO;
//	}

	public void onRowEdit(RowEditEvent event) {
		try {
			actEnregistrerAutorisation(null);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Autorisation", e.getMessage()));	
			context.validationFailed();
		}
}
	public void onRowEditDossier(RowEditEvent event) {
		try {
			//actEnregistrerDossier(null);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "dossier", e.getMessage()));	
			context.validationFailed();
		}
}
	public void onRowEditPrix(RowEditEvent event) {
		try {
//			if(modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION))
//				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
			actEnregistrerPrix(null);
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Autorisation", e.getMessage()));	
			context.validationFailed();
		}
}
	public void onRowEditInit(RowEditEvent event) {
//		TaDossier d =  (TaDossier) event.getComponent().getAttributes().get("dos");
//		taDossier=d;
		try {
			if(modeEcran.getMode().equals(EnumModeObjet.C_MO_CONSULTATION))
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	public void onRowSelectLigne(SelectEvent event) {

	}

	public void onRowCancel(RowEditEvent event) {		
		actAnnulerLigne(null);
    }
	
	public void onRowSelectLignePrix(SelectEvent event) {

	}

	public void onRowCancelPrix(RowEditEvent event) {		
		actAnnulerLignePrix(null);
    }
	
	public void actAnnulerLignePrix(ActionEvent actionEvent) {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);

		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("prix perso", "actAnnulerLignePrix"));
		}
	}
	public void actAnnulerLigne(ActionEvent actionEvent) {
		


		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Autorisation", "actAnnulerLigne"));
		}
	}

	public TaDossier getTaDossier() {
		return taDossier;
	}

	public void setTaDossier(TaDossier taDossier) {
		this.taDossier = taDossier;
	}

	public TaDossierDTO getSelectedTaDossierDTO() {
		return selectedTaDossierDTO;
	}

	public void setSelectedTaDossierDTO(TaDossierDTO selectedTaDossierDTO) {
		this.selectedTaDossierDTO = selectedTaDossierDTO;
	}

	public TaPrixPerso getTaPrixPerso() {
		return taPrixPerso;
	}

	public void setTaPrixPerso(TaPrixPerso taPrixPerso) {
		this.taPrixPerso = taPrixPerso;
	}

	public List<TaDossier> getListeDossier() {
		return listeDossier;
	}

	public void setListeDossier(List<TaDossier> listeDossier) {
		this.listeDossier = listeDossier;
	}

	public TaDossier getTaDossierClient() {
		return taDossierClient;
	}

	public void setTaDossierClient(TaDossier taDossierClient) {
		this.taDossierClient = taDossierClient;
	}
}  
