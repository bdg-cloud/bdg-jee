package fr.legrain.bdg.compteclient.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;

import fr.legrain.bdg.compteclient.service.droits.SessionInfo;
import fr.legrain.bdg.ws.IOException;

@ManagedBean
@SessionScoped
public class LanguageBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject private	SessionInfo sessionInfo;
	private String localeCode = "fr";
	private Locale locale;
	
	private static Map<String,Object> countries;
	
	static{
		countries = new LinkedHashMap<String,Object>();
		countries.put("Français", Locale.FRENCH); //label, value
		countries.put("English", new Locale("en","UK")); //label, value
		//countries.put("Chinese", Locale.SIMPLIFIED_CHINESE);
	}

	public Map<String, Object> getCountriesInMap() {
		return countries;
	}

	public String getLocaleCode() {       
		return localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public void countryLocaleCodeChanged(AjaxBehaviorEvent event){

		String newLocaleValue = localeCode;

		//loop country map to compare the locale code
		for (Map.Entry<String, Object> entry : countries.entrySet()) {

			if(entry.getValue().toString().equals(newLocaleValue)){

				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale)entry.getValue());
				sessionInfo.setSessionLangue((Locale)entry.getValue());
			}
		}
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		try {
// Permet de renvoyer le nom de l'url active.			
			if(FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/index.xhtml")){
				externalContext.redirect(externalContext.getRequestContextPath()+"/index.xhtml");
			}
			else {
				externalContext.redirect(externalContext.getRequestContextPath());
			}
			
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

}
