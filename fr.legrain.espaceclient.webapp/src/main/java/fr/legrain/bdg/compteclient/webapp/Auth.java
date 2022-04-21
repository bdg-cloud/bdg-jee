package fr.legrain.bdg.compteclient.webapp;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import fr.legrain.bdg.compteclient.model.droits.TaAuthURL;
import fr.legrain.bdg.compteclient.model.droits.TaRRoleUtilisateur;
import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.service.droits.SessionInfo;
import fr.legrain.bdg.compteclient.service.droits.TenantInfo;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaAuthURLServiceRemote;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaUtilisateurServiceRemote;

@ManagedBean
//@ViewScoped
@SessionScoped
public class Auth {

	private String username;
	private String password;
	private String theme = "icarus-green"; //"metroui", "legrain", "icarus-green"
	private String themeCouleur = "green";
	private String originalURL;
	
	//private Integer sessionTimeIdle = 1800000; //milliseconde
	//Penser à changer dans le web.xml <session-config><session-timeout> avec un temps légèrement supérieur
//	private Integer sessionTimeIdle = 3500000; //milliseconde 3600000 = 60 minutes
	private Integer sessionTimeIdle = 14000000; //milliseconde 14400000 = 240 minutes
	
	private String username_logindb;

	private String loginMaint;
	private boolean versionMobile=false;
	
	@ManagedProperty ("#{c_langue}")
	private ResourceBundle cLangue;

	private TaUtilisateur user;
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	@EJB private ITaUtilisateurServiceRemote userService;
	@EJB private ITaAuthURLServiceRemote authURLService;

	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
		
		if (sessionInfo.getSessionLangue()!= null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionInfo.getSessionLangue());
		}
		if (originalURL == null) {
			originalURL = externalContext.getRequestContextPath() + "/home.xhtml";
		} else {
			String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

			if (originalQuery != null) {
				originalURL += "?" + originalQuery;
			}
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if(origRequest.getRequestURL().substring(1).contains("/m") || origRequest.getRequestURL().substring(1).contains("/m/")){
			theme="icarus-green";
			versionMobile=true;
		};
	}

	public String login() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		try {
			//log4jLogger.info("LOGIN");

			
			//http://stackoverflow.com/questions/6589138/using-http-request-login-with-jboss-jaas
			Principal userPrincipal = request.getUserPrincipal();
			if (request.getUserPrincipal() != null) {
				request.logout();
			}

			boolean siteEnMaintenance = false;
			if(ConstWeb.maintenance && !isDev(username)) {
				siteEnMaintenance = true;
			}

			if(!siteEnMaintenance) {
				
				HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				System.out.println("dddd : "+origRequest.getServerName());
				String tenant = origRequest.getServerName().substring(0,origRequest.getServerName().lastIndexOf("."));//supprime le tld (dev.demo.promethee.biz => dev.demo.promethee)
				tenant = tenant.substring(0,tenant.lastIndexOf("."));//suprime le domaine principal (dev.demo.promethee => dev.demo)
				tenant = tenant.substring(tenant.lastIndexOf(".")+1);//conserve uniquement le premier niveau de sous domaine (dev.demo => demo)
				System.out.println("Dossier/tenant : "+tenant);
				tenantInfo.setTenantId(tenant);
				

				request.login(username, password);
				
				userPrincipal = request.getUserPrincipal();
				user = userService.findByCode(username);

				sessionInfo.setUtilisateur(user);
				sessionInfo.setIpAddress(findUserIPAddress());
				sessionInfo.setSessionID(request.getSession().getId());

				System.out.println("Utilisateur "+user.getUsername()+" *** "+user.getPasswd()+" connecté avec succès ...");
				System.out.println("Utilisateur "+user.getUsername()+" ("+user.getNom()!=null?user.getNom():""+" "+user.getPrenom()!=null?user.getPrenom():""+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
				Date now = new Date();
				user.setDernierAcces(now);

				
				user.setIpAcces(sessionInfo.getIpAddress());


				userService.enregistrerMerge(user);


				for (TaRRoleUtilisateur r : user.getRoles()) {
					System.out.println(r.getTaRole().getRole());
				}
				
				
				externalContext.getSessionMap().put("userSession", user);
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, cLangue.getString("espace_client_alerte_site_en_maintenance"),""));
				return "/login/error.xhtml";
			}

			//          externalContext.redirect(originalURL);
		} catch (ServletException e) {
			e.printStackTrace();
			//log4jLogger.info("DENIED");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, cLangue.getString("espace_client_alerte_inconnu_email"),/*e.getClass().getName()*/ e.getMessage()));
			return "/login/error.xhtml";
		} catch (FinderException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_FATAL, e.getClass().getName(), e.getMessage()));
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if(versionMobile){
			return "/m/index.xhtml";
		};
		
		return "/index.xhtml";
	}

	public void logout(ActionEvent a) throws IOException {
		logout();
	}

	public void logout() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();
		//System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
		System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") ** IP : "+findUserIPAddress());
		if(versionMobile)
			externalContext.redirect(externalContext.getRequestContextPath() + "/m/login/login.xhtml");
		else externalContext.redirect(externalContext.getRequestContextPath() + "/login/login.xhtml");
	}
	
	public void timeout() throws IOException {
		System.out.println("Timeout, fermeture automatique de la session");
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();
		//System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
		System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") ** IP : "+findUserIPAddress());
		if(versionMobile)externalContext.redirect(externalContext.getRequestContextPath() + "/m/login/login.xhtml?timeout=1");
		else externalContext.redirect(externalContext.getRequestContextPath() + "/login/login.xhtml?timeout=1");
	}

	public String findUserIPAddress() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		System.out.println("ipAddress:" + ipAddress);
		return ipAddress;
	}

	public boolean hasRole(String role) {
		
		return user.hasRole(role);
	}

	public boolean isDev() {
		return user.isDev();
	}

	public boolean isDev(String username) {
		return user.isDev(username);
	}

	public boolean isDevLgr() {
		return user.isDevLgr();
	}

	public boolean isDevLgr(String username) {
		return user.isDevLgr(username);
	}

	static public TaUtilisateur findUserInSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (TaUtilisateur) context.getApplication().evaluateExpressionGet(context,"#{auth.user}", TaUtilisateur.class);
	}

	public List<TaAuthURL> restrictedURL() {
		List<TaAuthURL> l = new ArrayList<TaAuthURL>();
		return l;
	}
	
	public void retour() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath()+"/login/login.xhtml");
	}
	
	public void mdpOublie() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath()+"/login/mdp_oublie.xhtml");
	}

	public String getLoginMaint() {
		return loginMaint;
	}

	public void setLoginMaint(String loginMaint) {
		this.loginMaint = loginMaint;
	}

	public ResourceBundle getcLangue() {
		return cLangue;
	}

	public void setcLangue(ResourceBundle cLangue) {
		this.cLangue = cLangue;
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

	public Integer getSessionTimeIdle() {
		return sessionTimeIdle;
	}

	public String getThemeCouleur() {
		return themeCouleur;
	}

	public void setThemeCouleur(String themeCouleur) {
		this.themeCouleur = themeCouleur;
	}

}