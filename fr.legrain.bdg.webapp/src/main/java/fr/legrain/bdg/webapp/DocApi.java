package fr.legrain.bdg.webapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import fr.legrain.autorisations.ws.ListeModules;
import fr.legrain.autorisations.ws.Module;
import fr.legrain.autorisations.ws.client.AutorisationWebServiceClientCXF;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaAuthURLServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.login.service.remote.ITaUtilisateurLoginServiceRemote;
import fr.legrain.bdg.webapp.app.LeftBean;
import fr.legrain.bdg.webapp.app.MenuBean;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaAuthURL;
import fr.legrain.droits.model.TaRRoleUtilisateur;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.login.model.TaUtilisateurLogin;
import fr.legrain.moncompte.ws.client.SetupWebServiceClientCXF;

@Named
//@ViewScoped
@SessionScoped
public class DocApi implements Serializable {

	private static final long serialVersionUID = 8813471574168999372L;
	
	private String username;
	private String password;
	
	private boolean connecte = false;
	
	private String theme = "icarus-green"; //"mirage-blue-light", "metroui", "legrain", "icarus-green", "primefaces-mirage-blue-light"
	private String themeCouleur = "green"; //"green" //pour icarus seulement
	private String originalURL;

	private TaUtilisateur user;
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	@Inject private	Auth auth;
	@EJB private ITaUtilisateurServiceRemote userService;
	@EJB private ITaUtilisateurLoginServiceRemote utilisateurLoginService;
	@EJB private ITaAuthURLServiceRemote authURLService;
	@EJB private ITaAutorisationsDossierServiceRemote autorisationsDossierService;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;

	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

		if (originalURL == null) {
			originalURL = externalContext.getRequestContextPath() + "/home.xhtml";
		} else {
			String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

			if (originalQuery != null) {
				originalURL += "?" + originalQuery;
			}
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
	}


	public String login() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
		taParametreService.initParamBdg();
		
		String login = "";
		String motDePasse = paramBdg.getTaParametre().getPasswordAccesDev();
		
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String tenant = auth.extractTenantFromUrl(origRequest);
		tenantInfo.setTenantId(tenant);
		
		//if(username.equals("test") && password.equals("1234")) {
		if(username!=null && !username.equals("") && password!=null && password.equals(motDePasse)) {
			System.out.println("Login developer "+username+" - dossier "+tenant);
			//System.out.println("Login developer : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") ** IP : "+auth.findUserIPAddress());
			connecte = true;
			externalContext.getSessionMap().put("docApi", this);
//		try {
//			//log4jLogger.info("LOGIN");
//
//			//            request.login(username, password);  
//			//            user = userService.findByCode(username);
//
//			//http://stackoverflow.com/questions/6589138/using-http-request-login-with-jboss-jaas
//			Principal userPrincipal = request.getUserPrincipal();
//			if (request.getUserPrincipal() != null) {
//				request.logout();
//			}
//
//			boolean siteEnMaintenance = false;
//			if(ConstWeb.maintenance && !isDev(username)) {
//				siteEnMaintenance = true;
//			}
//
//			if(!siteEnMaintenance) {
//				
//				HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//				System.out.println("dddd : "+origRequest.getServerName());
//				String tenant = origRequest.getServerName().substring(0,origRequest.getServerName().lastIndexOf("."));//supprime le tld (dev.demo.promethee.biz => dev.demo.promethee)
//				tenant = tenant.substring(0,tenant.lastIndexOf("."));//suprime le domaine principal (dev.demo.promethee => dev.demo)
//				tenant = tenant.substring(tenant.lastIndexOf(".")+1);//conserve uniquement le premier niveau de sous domaine (dev.demo => demo)
//				System.out.println("Dossier/tenant : "+tenant);
//				tenantInfo.setTenantId(tenant);
//				
//				username_logindb = username+"_"+tenantInfo.getTenantId();
//				System.out.println("Utilisateur login_db : "+username_logindb);
//				request.login(username_logindb, password);
////				request.login(username, password);
//				
//				userPrincipal = request.getUserPrincipal();
//				user = userService.findByCode(username);
//				TaUtilisateurLogin utilisateurLogin = utilisateurLoginService.findByCode(username_logindb);
////				TaUtilisateurLogin utilisateurLogin = utilisateurLoginService.findByCode(username);
//
//				sessionInfo.setUtilisateur(user);
//				sessionInfo.setIpAddress(findUserIPAddress());
//				sessionInfo.setSessionID(request.getSession(false).getId());
//
//				System.out.println("Utilisateur "+user.getUsername()+" *** "+user.getPasswd()+" connecté avec succès ...");
//				System.out.println("Utilisateur "+user.getUsername()+" ("+user.getNom()!=null?user.getNom():""+" "+user.getPrenom()!=null?user.getPrenom():""+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
//				Date now = new Date();
//				user.setDernierAcces(now);
//				utilisateurLogin.setDernierAcces(now);
//				
//				user.setIpAcces(sessionInfo.getIpAddress());
//				utilisateurLogin.setIpAcces(sessionInfo.getIpAddress());
//
//				userService.enregistrerMerge(user);
//				utilisateurLoginService.enregistrerMerge(utilisateurLogin);
//				
//				initDroitModules();
//
//				for (TaRRoleUtilisateur r : user.getRoles()) {
//					System.out.println(r.getTaRole().getRole());
//				}
//				
//				//externalContext.getSessionMap().put("auth", this);
//				externalContext.getSessionMap().put("userSession", user);
//				
//				
//				ouvertureAutoOnglet(context);
				
				
				
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Identifiant ou mot de passe incorrect.",""));
//				return "/login/error.xhtml";
			}

			//          externalContext.redirect(originalURL);
//		} catch (ServletException e) {
//			e.printStackTrace();
//			//log4jLogger.info("DENIED");
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//					FacesMessage.SEVERITY_FATAL, "Utilisateur ou mot de passe incorrects"/*e.getClass().getName()*/, e.getMessage()));
//			return "/login/error.xhtml";
//		} catch (FinderException e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//					FacesMessage.SEVERITY_FATAL, e.getClass().getName(), e.getMessage()));
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		if(versionMobile){
//			return "/m/index.xhtml";
//		};
		
//		return null;
		return "/developer/developer.xhtml?faces-redirect=true";
	}
	
	public String logout() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();
		//System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
		System.out.println("Logout developer ");
//		System.out.println("Logout developer : User "+user.getUsername()+" ** IP : "+auth.findUserIPAddress());
		externalContext.redirect(externalContext.getRequestContextPath() + "/developer/login.xhtml");
		return "/developer/login.xhtml?faces-redirect=true";
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public TaUtilisateur getUser() {
		return user;
	}

	public void setUser(TaUtilisateur user) {
		this.user = user;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

//	public Integer getSessionTimeIdle() {
//		return sessionTimeIdle;
//	}

	public String getThemeCouleur() {
		return themeCouleur;
	}

	public void setThemeCouleur(String themeCouleur) {
		this.themeCouleur = themeCouleur;
	}


	public boolean isConnecte() {
		return connecte;
	}


	public void setConnecte(boolean connecte) {
		this.connecte = connecte;
	}

}