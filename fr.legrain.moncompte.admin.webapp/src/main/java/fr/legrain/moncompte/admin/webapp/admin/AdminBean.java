package fr.legrain.moncompte.admin.webapp.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import fr.legrain.bdg.ws.client.Admin;
import fr.legrain.moncompte.admin.webapp.ConstWeb;

@ManagedBean
@ViewScoped
//@SessionScoped 
public class AdminBean implements Serializable {  

	private static final long serialVersionUID = 1331238708223531056L;

	private Admin adminService = new Admin();
	
	private String versionProg;
		
	private String nomDump;
	private String nomDossier;
	private List<Schema> listeBase;
	private List<Schema> listeSchema;
//	private List<String> listeSchema;
	
	private String nomDomaine;
	private String prefixeSousDomaine;
		
	public void refresh() {
		
	}
	
	@PostConstruct
	public void init() {
		versionProg = versionProg();
		//initBdd();
		//initSchema();
		try {
			initConst();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initConst() throws FileNotFoundException, IOException {
		 String propertiesFileName = "bdg.properties";  
	      
		    Properties props = new Properties();  
		    String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  
		      
		    if(new File(path).exists()) { 
		    	File f = new File(path);
		        props.load(new FileInputStream(f));  
		        nomDomaine = props.getProperty("nom_domaine");
		        prefixeSousDomaine = props.getProperty("prefixe_sous_domaine");
		        
		    } else {  
//		    	nomDomaine = "localhost";
		    }  
	}
	
	public void actualiser(ActionEvent event) {
		initBdd();
		initSchema();
	}
	
	public void dbDump(ActionEvent event) {
		
		try {
			adminService.backupDB(nomDump);
			initBdd();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); }
	}
	
	public void dumpBase(ActionEvent event) {
		
		String nomBase = (String) event.getComponent().getAttributes().get("nomBase");
		
		try {
			adminService.backupDB(nomDump,nomBase,null);
			initBdd();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); }
	}
	
	public void initBdd() {

		listeBase = new ArrayList<Schema>();
		
		List<String> l = adminService.listeBdd();
		Schema s = null;
		for (String string : l) {
			s = new Schema();
			s.setNom(string);
			s.setListeDump(initListeDump(adminService.listeFichierDump(null,s.getNom())));
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
		List<Object[]> r = adminService.listeSchemaTailleConnection();
		
		listeSchema = new ArrayList<Schema>();
		Schema s = null;
		for (Object[] object : r) {
			s = new Schema();
			s.setNom(object[0].toString());
			s.setTaille1(object[1].toString());
			s.setTaille2(object[2].toString());
			s.setListeDump(initListeDump(adminService.listeFichierDump(s.getNom(), null)));
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
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"listeSchemaTailleConnection OK"
	    		)); 
		}
	}
	
	public void rebootServeur(ActionEvent event) {
		
		adminService.rebootServeur();
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"rebootServeur OK\n"
	    		)); 
		}
	}
	
	public void ajoutDossier(ActionEvent event) {
		
		adminService.ajoutDossier(nomDossier);
		initSchema();
	
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"ajoutDossier "+nomDossier+" OK\n"
	    				+ "Penser à relancer le serveur pour prendre en compte le nouveau sous domaine."
	    		)); 
		}
	}
	
	public void supprimerDossier(ActionEvent event) {
		
		Schema schema = (Schema) event.getComponent().getAttributes().get("nomDossier");
		
		nomDossier = schema.getNom();
//		nomDossier = nomDossier.split("-")[0].trim();
		
		if(!nomDossier.equals("pg_catalog") && !nomDossier.equals("information_schema") && !nomDossier.equals("public")) {
			adminService.supprimerDossier(nomDossier);
			initSchema();
			
			if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Dev", 
		    		"supprimerDossier "+nomDossier+"  OK\n"
		    				+ "Penser à relancer le serveur pour prendre en compte la suppression du sous domaine."
		    		)); 
			}
		} else {
			System.out.println("Schema système, suppression interdite : "+nomDossier);
		}
		
	}
	
	public void dumpDossier(ActionEvent event) {
		
		Schema schema = (Schema) event.getComponent().getAttributes().get("nomDossier");
		
		nomDossier = schema.getNom();
		initSchema();
//		nomDossier = nomDossier.split("-")[0].trim();
		
		try {
			adminService.backupDB(null,nomDossier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); 
		}
		
	}
	
	public void listeDump(ActionEvent event) {
		
//		String nomDossier = (String) event.getComponent().getAttributes().get("nomDossier");
		
//		nomDossier = nomDossier.split("-")[0].trim();
		
//		try {
//		listeDump = devService.listeFichierDump(null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"listeDump OK"
	    		)); 
		}
	}
	
	public void restaureDump(ActionEvent event) {
		
		String nomDump = (String) event.getComponent().getAttributes().get("nomDump");
		String nomDossier = (String) event.getComponent().getAttributes().get("nomDossier");
		
//		nomDossier = nomDossier.split("-")[0].trim();
		
		try {
			adminService.restaureDB(nomDump,nomDossier);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"retaureDump OK : "+nomDump+" "+nomDossier
	    		)); 
		}
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
		
//		String primefacesBuildVersion = PrimeFaces.current().getConfig().getBuildVersion();
		
		String v =""
//				"* JSF Major Version : "+jsfMajorVersion+
//				"<br/> * JSF Specification Version : "+jsfSpecificationVersion+
//				"<br/> * JSF Minor Implementation Version : "+jsfMinorImplementationVersion+
//				"<br/> * JSF Name Version : "+jsfNameVersion+
//				"<br/> * Primefaces Build Version : "+primefacesBuildVersion+
//				"<br/> * Java : "+System.getProperties().getProperty("java.version")
//				
				;
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev version", 
	    		"v"
	    		));}
	    
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

	public String getNomDomaine() {
		return nomDomaine;
	}

	public void setNomDomaine(String nomDomaine) {
		this.nomDomaine = nomDomaine;
	}

	public String getPrefixeSousDomaine() {
		return prefixeSousDomaine;
	}

	public void setPrefixeSousDomaine(String prefixeSousDomaine) {
		this.prefixeSousDomaine = prefixeSousDomaine;
	}
	
}  
              