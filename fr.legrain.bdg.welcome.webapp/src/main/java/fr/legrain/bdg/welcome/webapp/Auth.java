package fr.legrain.bdg.welcome.webapp;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
//@ViewScoped
@SessionScoped
public class Auth implements Serializable {

	private String username;
	private String password;
	private String theme = "icarus-green"; //"metroui", "legrain", "icarus-green"
	private String themeCouleur = "green";
	private String originalURL;
	
	private String loginMaint;

	

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
		if(origRequest.getRequestURL().substring(1).contains("/m") || origRequest.getRequestURL().substring(1).contains("/m/")){
			theme="icarus-green";
		};

		
	}

	
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

	public String getThemeCouleur() {
		return themeCouleur;
	}

	public void setThemeCouleur(String themeCouleur) {
		this.themeCouleur = themeCouleur;
	}

}