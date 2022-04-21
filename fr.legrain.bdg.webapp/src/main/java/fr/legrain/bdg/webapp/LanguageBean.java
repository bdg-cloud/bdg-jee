package fr.legrain.bdg.webapp;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

@Named
@SessionScoped
public class LanguageBean implements Serializable{

	private static final long serialVersionUID = -1364949788296971344L;
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

			}
		}
	}


	public Locale getLocale() {
		return locale;
	}


	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}