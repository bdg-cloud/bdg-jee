package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
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
import fr.legrain.bdg.webapp.Auth;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bugzilla.rest.model.Bug;
import fr.legrain.bugzilla.rest.util.BugzillaUtil;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;

@Named
@ViewScoped 
public class BugsBean implements Serializable {

//	@Inject @Named(value="tabListModelCenterBean")
//	private TabListModelBean tabsCenter;
	
	private BugzillaUtil bz;
	
	public static /*final*/ String apiKey = "hYxVA0JXtBGy2s28GzhSWIyE4SjgBViWivHdeNaE"; //"zpV28PVz133uhN8aLHaDKOtRThw3nxc8UMExC6JW";
	public  static /*final*/ String bugzillaURL = "http://bugs.legrain.fr";
	
	private String login = "xxx.bugs@xxxxx.fr";
	private String password = "xxxxxxx";
	
	private String bzProduct = "Bureau de gestion Cloud"; //TestProduct 
	private String bzComponent = "Solstyce"; //TestComponent
	private String bzVersion = "non spécifiée";
//	private String bzProduct = "TestProduct"; //TestProduct 
//	private String bzComponent = "TestComponent"; //TestComponent
//	private String bzVersion = "unspecified"; //unspecified
	private String bzPlatform = "All";
	private String bzOpSys = "All";
	
	private List<Bug> listeBugs;
	private Bug bug;
	private Bug nouveauBug;
	private String nouveauCommentaire;
	
	private TaUtilisateur u = null;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	@EJB
    private ITaUtilisateurServiceRemote userService;
	
	@PostConstruct
	public void init() {
		u = Auth.findUserInSession();
		
		taParametreService.initParamBdg();
		
		bugzillaURL = paramBdg.getTaParametre().getBugzillaApiUrl();
		apiKey = paramBdg.getTaParametre().getBugzillaApiKey();
		
		bz = new BugzillaUtil(bugzillaURL,apiKey);
		listeBugs = bz.findBugs(bzProduct);
		nouveauBug = new Bug();
	}
	
	public void createBug(ActionEvent event) { 
		
//		u.setEmail("test@test1.fr");
//		u.setNom("a");
//		u.setPrenom("b");
//		u.setUsername("123456");
			
		if(bz.findUser(u.getEmail())==null) {
			//créé un utilisateur avant de poster un nouveau bug, pour pouvoir l'ajouter en CC
			//bz.createUser(u.getEmail(),u.getNom()+" "+u.getPrenom(),u.getUsername());
		}
		
		//propriétés à remplir automatiquement ET OBLIGATOIRE
		nouveauBug.setProduct(bzProduct);
		nouveauBug.setComponent(bzComponent);
		nouveauBug.setVersion(bzVersion);
		nouveauBug.setPlatform(bzPlatform);
		nouveauBug.setOpSys(bzOpSys);
		
		//propriétés à remplir automatiquement
		/*
		if(nouveauBug.getCc()==null) {
			nouveauBug.setCc(new ArrayList<Object>());
		}
		nouveauBug.getCc().add(u.getEmail());
		*/
		
		//propriétés par l'utilisateur
		//nouveauBug.setSummary("Test bug appli"+new Date().getTime());
		//nouveauBug.setDescription("sdfsfbh");
		
		//Ajout infos dans Description
		String detailDebug = "\n\n\n -----------------------------------"
				+ "\n Informations complémentaires "
				+ "\n ";
		nouveauBug.setDescription(nouveauBug.getDescription()+detailDebug);
		
		Integer bugID = bz.createBug(nouveauBug);
		
		nouveauBug = new Bug();
		listeBugs = bz.findBugs(bzProduct);
		
		Integer commentID = null;
//		if(nouveauCommentaire!=null && !nouveauCommentaire.equals("")) {
//			commentID = bz.createComment(nouveauCommentaire, bugID);
//			nouveauCommentaire = null;
//		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("createBug", 
				"Nouveau bug créé : "+bugID+" (commentaire "+commentID+")"
				)); 
	}
	
	public void findBug(ActionEvent event) { 
		
		listeBugs = bz.findBugs(bzProduct);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("findBug", 
					"Recherche dans Bugzilla pour "+bzProduct
					)); 
		}
	}

	public List<Bug> getListeBugs() {
		return listeBugs;
	}

	public void setListeBugs(List<Bug> listeBugs) {
		this.listeBugs = listeBugs;
	}

	public Bug getBug() {
		return bug;
	}

	public void setBug(Bug bug) {
		this.bug = bug;
	}

	public Bug getNouveauBug() {
		return nouveauBug;
	}

	public void setNouveauBug(Bug nouveauBug) {
		this.nouveauBug = nouveauBug;
	}

	public String getBugzillaURL() {
		return bugzillaURL;
	}

	public ITaUtilisateurServiceRemote getUserService() {
		return userService;
	}

	public void setUserService(ITaUtilisateurServiceRemote userService) {
		this.userService = userService;
	}

	public String getNouveauCommentaire() {
		return nouveauCommentaire;
	}

	public void setNouveauCommentaire(String nouveauCommentaire) {
		this.nouveauCommentaire = nouveauCommentaire;
	}
	
}
