package fr.legrain.bdg.webapp.dev;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.DevService;
import fr.legrain.push.service.BdgFirebaseService;
import fr.legrain.tiers.dto.TaTiersDTO;

@Named
@ViewScoped 
public class DevBean implements Serializable {  
	
	private static final long serialVersionUID = 8677377809172429592L;

	private @EJB DevService devService;
	
	/********************************************************************************************************************************/
	private @EJB BdgFirebaseService bdgFirebaseService;
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaUtilisateurServiceRemote taUtilisateurService;
	/********************************************************************************************************************************/
	
	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
	
	private String versionProg;
		
	private String nomDump;
	private String nomDossier;
	private List<Schema> listeBase;
	private List<Schema> listeSchema;
//	private List<String> listeSchema;
	
	/********************************************************************************************************************************/
	private String titrePushUtilisateur;
	private String contenuPushUtilisateur;
	
	private String titrePushTiersEspaceClient;
	private String contenuPushTiersEspaceClient;
	private String contenuLongPushTiersEspaceClient;
	private String urlPushTiersEspaceClient;
	private String imagePushTiersEspaceClient;
	
	private List<TaTiersDTO> listeTiers;
	private List<TaUtilisateurDTO> listeUtilisateur;
	private TaTiersDTO taTiersDTO;
	private TaUtilisateurDTO taUtilisateurDTO;
	/********************************************************************************************************************************/
	
	public void refresh() {
		
	}
	
	@PostConstruct
	public void init() {
		versionProg = versionProg();
//		initBdd();
//		initSchema();
	}
	
	public void dbDump(ActionEvent event) {
		
		try {
			devService.dbDump(nomDump);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); 
	}
	
	/********************************************************************************************************************************/
	public void notifAndroidUtilisateur(ActionEvent event) {		
		bdgFirebaseService.testNotifAndroidUtilisateur(taUtilisateurDTO, titrePushUtilisateur, contenuPushUtilisateur);
		taUtilisateurDTO = null;
		titrePushUtilisateur = null;
		contenuPushUtilisateur = null;
	}
	
	public void notifAndroidUtilisateurTous(ActionEvent event) {
		bdgFirebaseService.testNotifAndroidUtilisateurTous(titrePushUtilisateur, contenuPushUtilisateur);
		taUtilisateurDTO = null;
		titrePushUtilisateur = null;
		contenuPushUtilisateur = null;
	}
	
	public void notifAndroidTiersEspaceClient(ActionEvent event) {
		bdgFirebaseService.testNotifAndroidTiersEspaceClient(taTiersDTO, titrePushTiersEspaceClient, contenuPushTiersEspaceClient
				,contenuLongPushTiersEspaceClient, urlPushTiersEspaceClient, imagePushTiersEspaceClient);
		taTiersDTO = null;
		titrePushTiersEspaceClient = null;
		contenuPushTiersEspaceClient = null;
		contenuLongPushTiersEspaceClient = null;
		urlPushTiersEspaceClient = null;
		imagePushTiersEspaceClient = null;
	}
	
	public void notifAndroidTiersEspaceClientTous(ActionEvent event) {
		bdgFirebaseService.testNotifAndroidTiersEspaceClientTous(titrePushTiersEspaceClient, contenuPushTiersEspaceClient);
		taTiersDTO = null;
		titrePushTiersEspaceClient = null;
		contenuPushTiersEspaceClient = null;
	}
	
	
	
	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}

	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}

	public TaUtilisateurDTO getTaUtilisateurDTO() {
		return taUtilisateurDTO;
	}

	public void setTaUtilisateurDTO(TaUtilisateurDTO taUtilisateurDTO) {
		this.taUtilisateurDTO = taUtilisateurDTO;
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
	
	public List<TaUtilisateurDTO> utilisateurAutoCompleteDTOLight(String query) {
		List<TaUtilisateurDTO> allValues = taUtilisateurService.findByCodeLight("*");
		List<TaUtilisateurDTO> filteredValues = new ArrayList<TaUtilisateurDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaUtilisateurDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getUsername().toLowerCase().contains(query.toLowerCase())
					|| civ.getEmail().toLowerCase().contains(query.toLowerCase())
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	/********************************************************************************************************************************/
	public void dumpBase(ActionEvent event) {
		
		String nomBase = (String) event.getComponent().getAttributes().get("nomBase");
		
		try {
			devService.dbDump(nomDump,nomBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); 
	}
	
	public void initBdd() {

		listeBase = new ArrayList<Schema>();
		
		List<String> l = devService.listeBdd();
		Schema s = null;
		for (String string : l) {
			s = new Schema();
			s.setNom(string);
			s.setListeDump(initListeDump(devService.listeFichierDump(null,s.getNom())));
			listeBase.add(s);
		}
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//	    context.addMessage(null, new FacesMessage("Dev", 
//	    		"listeBdd OK"
//	    		)); 
	}
	
	public List<Dump> initListeDump(List<String> l) {
		List<Dump> lDump = new ArrayList<Dump>();
		Dump d = null;
		for (String string : l) {
			d = new Dump();
			d.setNom(string);
			lDump.add(d);
		}
		return lDump;
	}
	
	public void initSchema() {
		List<Object[]> r = devService.listeSchemaTailleConnection();
		
		listeSchema = new ArrayList<Schema>();
		Schema s = null;
		for (Object[] object : r) {
			s = new Schema();
			s.setNom(object[0].toString());
			s.setTaille1(object[1].toString());
			s.setTaille2(object[2].toString());
			s.setListeDump(initListeDump(devService.listeFichierDump(s.getNom(), null)));
			listeSchema.add(s);
		}
	}
	
	public void listeSchemaTailleConnection(ActionEvent event) {

//		devService.listeSchemaTailleConnection();
		
//		for (Object[] object : r) {
//		for (Object object2 : object) {
//			System.out.println(object2);
//		}
//		
//	}
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"listeSchemaTailleConnection OK"
	    		)); 
	}
	
	public void rebootServeur(ActionEvent event) {
		
		devService.rebootServeur();
	
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"rebootServeur OK\n"
	    		)); 
	}
	
	public void ajoutDossier(ActionEvent event) {
		
		devService.ajoutDossier(nomDossier);
		initSchema();
	
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"ajoutDossier "+nomDossier+" OK\n"
	    				+ "Penser à relancer le serveur pour prendre en compte le nouveau sous domaine."
	    		)); 
	}
	
	public void supprimerDossier(ActionEvent event) {
		
		Schema schema = (Schema) event.getComponent().getAttributes().get("nomDossier");
		
		nomDossier = schema.getNom();
//		nomDossier = nomDossier.split("-")[0].trim();
		
		if(!nomDossier.equals("pg_catalog") && !nomDossier.equals("information_schema") && !nomDossier.equals("public")) {
			devService.supprimerDossier(nomDossier);
			initSchema();
			
			FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Dev", 
		    		"supprimerDossier "+nomDossier+"  OK\n"
		    				+ "Penser à relancer le serveur pour prendre en compte la suppression du sous domaine."
		    		)); 
		} else {
			System.out.println("Schema système, suppression interdite : "+nomDossier);
		}
		
	}
	
	public void dumpDossier(ActionEvent event) {
		
		Schema schema = (Schema) event.getComponent().getAttributes().get("nomDossier");
		
		nomDossier = schema.getNom();
//		nomDossier = nomDossier.split("-")[0].trim();
		
		try {
			devService.schemaDump(null,nomDossier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); 
		
	}
	
	public void listeDump(ActionEvent event) {
		
//		String nomDossier = (String) event.getComponent().getAttributes().get("nomDossier");
		
//		nomDossier = nomDossier.split("-")[0].trim();
		
//		try {
//		listeDump = devService.listeFichierDump(null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"listeDump OK"
	    		)); 
		
	}
	
	public void restaureDump(ActionEvent event) {
		
		String nomDump = (String) event.getComponent().getAttributes().get("nomDump");
		String nomDossier = (String) event.getComponent().getAttributes().get("nomDossier");
		
//		nomDossier = nomDossier.split("-")[0].trim();
		
		try {
			devService.restaureDB(nomDump,nomDossier);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"retaureDump OK : "+nomDump+" "+nomDossier
	    		)); 
		
	}
	
	public String versionProg() {
		//returns the major version (2.1)
		String jsfMajorVersion = FacesContext.class.getPackage().getImplementationVersion();

		//returns the specification version (2.1)
		String jsfSpecificationVersion = Package.getPackage("com.sun.faces").getSpecificationVersion();

		//returns the minor implementation version (2.1.x)
		String jsfMinorImplementationVersion = Package.getPackage("com.sun.faces").getImplementationVersion();
		String jsfNameVersion = Package.getPackage("com.sun.faces").getImplementationTitle();

		//org.primefaces.util.Constants.VERSION;
		
//		String primefacesBuildVersion = PrimeRequestContext.getCurrentInstance().getApplicationContext().getConfig().getBuildVersion();
		
		String v = "* JSF Major Version : "+jsfMajorVersion+
				"<br/> * JSF Specification Version : "+jsfSpecificationVersion+
				"<br/> * JSF Minor Implementation Version : "+jsfMinorImplementationVersion+
				"<br/> * JSF Name Version : "+jsfNameVersion+
//				"<br/> * Primefaces Build Version : "+primefacesBuildVersion+
				"<br/> * Java : "+System.getProperties().getProperty("java.version")
				
				;
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev version", 
	    		"v"
	    		));
	    
	    return v;
	}
	
	public String getVersionProg() {
		return versionProg;
	}

	public void setVersionProg(String valeur1) {
		this.versionProg = valeur1;
	}

	public String getNomDump() {
		return nomDump;
	}

	public void setNomDump(String nomDump) {
		this.nomDump = nomDump;
	}

	public String getNomDossier() {
		return nomDossier;
	}

	public void setNomDossier(String nomDossier) {
		this.nomDossier = nomDossier;
	}

	public List<Schema> getListeBase() {
		return listeBase;
	}

	public void setListeBase(List<Schema> listeBase) {
		this.listeBase = listeBase;
	}

	public List<Schema> getListeSchema() {
		return listeSchema;
	}

	public void setListeSchema(List<Schema> listeSchema) {
		this.listeSchema = listeSchema;
	}

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}
	
	public String getTitrePushUtilisateur() {
		return titrePushUtilisateur;
	}

	public void setTitrePushUtilisateur(String titrePushUtilisateur) {
		this.titrePushUtilisateur = titrePushUtilisateur;
	}

	public String getContenuPushUtilisateur() {
		return contenuPushUtilisateur;
	}

	public void setContenuPushUtilisateur(String contenuPushUtilisateur) {
		this.contenuPushUtilisateur = contenuPushUtilisateur;
	}

	public String getTitrePushTiersEspaceClient() {
		return titrePushTiersEspaceClient;
	}

	public void setTitrePushTiersEspaceClient(String titrePushTiersEspaceClient) {
		this.titrePushTiersEspaceClient = titrePushTiersEspaceClient;
	}

	public String getContenuPushTiersEspaceClient() {
		return contenuPushTiersEspaceClient;
	}

	public void setContenuPushTiersEspaceClient(String contenuPushTiersEspaceClient) {
		this.contenuPushTiersEspaceClient = contenuPushTiersEspaceClient;
	}

	public List<TaTiersDTO> getListeTiers() {
		return listeTiers;
	}

	public void setListeTiers(List<TaTiersDTO> listeTiers) {
		this.listeTiers = listeTiers;
	}

	public List<TaUtilisateurDTO> getListeUtilisateur() {
		return listeUtilisateur;
	}

	public void setListeUtilisateur(List<TaUtilisateurDTO> listeUtilisateur) {
		this.listeUtilisateur = listeUtilisateur;
	}
	
	public String getContenuLongPushTiersEspaceClient() {
		return contenuLongPushTiersEspaceClient;
	}

	public void setContenuLongPushTiersEspaceClient(String contenuLongPushTiersEspaceClient) {
		this.contenuLongPushTiersEspaceClient = contenuLongPushTiersEspaceClient;
	}

	public String getUrlPushTiersEspaceClient() {
		return urlPushTiersEspaceClient;
	}

	public void setUrlPushTiersEspaceClient(String urlPushTiersEspaceClient) {
		this.urlPushTiersEspaceClient = urlPushTiersEspaceClient;
	}

	public String getImagePushTiersEspaceClient() {
		return imagePushTiersEspaceClient;
	}

	public void setImagePushTiersEspaceClient(String imagePushTiersEspaceClient) {
		this.imagePushTiersEspaceClient = imagePushTiersEspaceClient;
	}
	
}  
              