package fr.legrain.autorisations.webapp;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import fr.legrain.autorisations.autorisation.model.TaRRoleUtilisateur;
import fr.legrain.autorisations.autorisation.model.TaUtilisateur;
import fr.legrain.autorisations.autorisations.service.SessionInfo;
import fr.legrain.autorisations.autorisations.service.TenantInfo;
import fr.legrain.bdg.autorisations.service.remote.ITaUtilisateurServiceRemote;

//import fr.legrain.bdg.droits.service.remote.ITaAuthURLServiceRemote;
//import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
//import fr.legrain.droits.model.TaAuthURL;
//import fr.legrain.droits.model.TaRRoleUtilisateur;
//import fr.legrain.droits.model.TaUtilisateur;
//import fr.legrain.droits.service.SessionInfo;
//import fr.legrain.droits.service.TenantInfo;

@ManagedBean
//@ViewScoped
@SessionScoped
public class Auth implements Serializable{

	private String username;
	private String password;
	private String theme = "legrain"; //"metroui", "legrain", 
	private String originalURL;

	private String loginMaint;

	private TaUtilisateur user;
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	@EJB private ITaUtilisateurServiceRemote userService;
//	@EJB private ITaAuthURLServiceRemote authURLService;

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
	}

	//@EJB
	//private UserService userService;

	public String login() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		try {
			//log4jLogger.info("LOGIN");

			//            request.login(username, password);  
			//            user = userService.findByCode(username);

			//http://stackoverflow.com/questions/6589138/using-http-request-login-with-jboss-jaas
			Principal userPrincipal = request.getUserPrincipal();
			if (request.getUserPrincipal() != null) {
				request.logout();
			}

			boolean siteEnMaintenance = false;
			if(ConstWeb.maintenance /*&& !isDev(username)*/) {
				siteEnMaintenance = true;
			}

			if(!siteEnMaintenance) {

				request.login(username, password);
				
				HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				System.out.println("dddd : "+origRequest.getServerName());
				tenantInfo.setTenantId(origRequest.getServerName());
				
				userPrincipal = request.getUserPrincipal();
				user = userService.findByCode(userPrincipal.getName());

				sessionInfo.setUtilisateur(user);
				sessionInfo.setIpAddress(findUserIPAddress());

				System.out.println("Utilisateur "+user.getUsername()+" *** "+user.getPasswd()+" connecté avec succès ...");
				System.out.println("Utilisateur "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") "/*+user.getTaEntreprise().getNom()*/+" ** IP : "+findUserIPAddress());
				user.setDernierAcces(new Date());

				userService.enregistrerMerge(user);

				for (TaRRoleUtilisateur r : user.getRoles()) {
					System.out.println(r.getTaRole().getRole());
				}


				//externalContext.getSessionMap().put("auth", this);
				externalContext.getSessionMap().put("userSession", user);
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Site en cours de maintenance ...",""));
				return "/login/error.xhtml";
			}

			//          externalContext.redirect(originalURL);
		} catch (ServletException e) {
			e.printStackTrace();
			//log4jLogger.info("DENIED");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_FATAL, "Utilisateur ou mot de passe incorrects"/*e.getClass().getName()*/, e.getMessage()));
			return "/login/error.xhtml";
		} catch (FinderException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_FATAL, e.getClass().getName(), e.getMessage()));
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/index.xhtml";
	}

	public void logout(ActionEvent a) throws IOException {
		logout();
		//    	//public void logout() throws IOException {
		//    	//public String logout() throws IOException {
		//            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		//            externalContext.invalidateSession();
		//            externalContext.redirect(externalContext.getRequestContextPath() + "/login/login.xhtml");
		//            //return "/login/login.xhtml";
		//           // response.sendRedirect(request.getContextPath() + "/login.xhtml");
		//            //return "/login/login.xhtml?faces-redirect=true";
	}

	public void logout() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();
		System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") "/*+user.getTaEntreprise().getNom()*/+" ** IP : "+findUserIPAddress());
		externalContext.redirect(externalContext.getRequestContextPath() + "/login/login.xhtml");
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
		//    	FacesContext context = FacesContext.getCurrentInstance();
		//    	ExternalContext externalContext = context.getExternalContext();
		//    	User user = (User) externalContext.getSessionMap().get("userSession");
		return user.hasRole(role);
	}

//	public boolean isDev() {
//		return user.isDev();
//	}
//
//	public boolean isDev(String username) {
//		return user.isDev(username);
//	}
//
//	public boolean isDevLgr() {
//		return user.isDevLgr();
//	}
//
//	public boolean isDevLgr(String username) {
//		return user.isDevLgr(username);
//	}

	static public TaUtilisateur findUserInSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (TaUtilisateur) context.getApplication().evaluateExpressionGet(context,"#{auth.user}", TaUtilisateur.class);
	}

//	public List<TaAuthURL> restrictedURL() {
//		//    	FacesContext context = FacesContext.getCurrentInstance();
//		//    	ExternalContext externalContext = context.getExternalContext();
//		//    	User user = (User) externalContext.getSessionMap().get("userSession");
//
//		List<TaAuthURL> l = new ArrayList<TaAuthURL>();
//
//		for (TaRRoleUtilisateur r : user.getRoles()) {
//			//l.addAll(authURLService.findByRoleID(r.getTaRole().getId()));
//		}
//
//		return l;
//	}

	public String getLoginMaint() {
		return loginMaint;
	}

	public void setLoginMaint(String loginMaint) {
		this.loginMaint = loginMaint;
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

}