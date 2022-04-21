package fr.legrain.bdg.webapp.dev;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import fr.legrain.lib.script.LibScript;

@Named
@ViewScoped
public class ScriptsBean implements Serializable {


	private static final long serialVersionUID = 3184907343419769746L;
	
	private LibScript libScript;
	
	private String langageScript;
	private String txtScript;
	private String resultScript;

	@PostConstruct
	public void init() {
		libScript = new LibScript();
	}
	
	public void execScript(ActionEvent actionEvent){
		try {
			if(langageScript.equals("javascript")) {
				resultScript = libScript.evalScript(txtScript, LibScript.SCRIPT_JAVASCRIPT).toString();
			} else if(langageScript.equals("groovy")) {
				resultScript = libScript.evalScript(txtScript, LibScript.SCRIPT_JAVASCRIPT).toString();
			} else if(langageScript.equals("ruby")) {
				resultScript = libScript.evalScript(txtScript, LibScript.SCRIPT_JAVASCRIPT).toString();
			} else if(langageScript.equals("python")) {
				resultScript = libScript.evalScript(txtScript, LibScript.SCRIPT_JAVASCRIPT).toString();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getTxtScript() {
		return txtScript;
	}

	public void setTxtScript(String txtScript) {
		this.txtScript = txtScript;
	}

	public String getResultScript() {
		return resultScript;
	}

	public void setResultScript(String resultScript) {
		this.resultScript = resultScript;
	}

	public String getLangageScript() {
		return langageScript;
	}

	public void setLangageScript(String langageScript) {
		this.langageScript = langageScript;
	}

}
