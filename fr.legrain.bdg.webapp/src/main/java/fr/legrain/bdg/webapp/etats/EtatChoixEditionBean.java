package fr.legrain.bdg.webapp.etats;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped 
public class EtatChoixEditionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7349609492692872073L;
	
	private boolean choixEdition=false;
	private boolean tcheckEdition=false;
	private String identification="ListeEtatDesStocksParArticle";
	
	public EtatChoixEditionBean() {
		
	}
	
	public boolean isChoixEdition() {
		return choixEdition;
	}
	
	public void setChoixEdition(boolean choixEdition) {
		this.choixEdition = choixEdition;
	}
	
	public boolean isTcheckEdition() {
		return tcheckEdition;
	}

	public void setTcheckEdition(boolean tcheckEdition) {
		this.tcheckEdition = tcheckEdition;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}
}
