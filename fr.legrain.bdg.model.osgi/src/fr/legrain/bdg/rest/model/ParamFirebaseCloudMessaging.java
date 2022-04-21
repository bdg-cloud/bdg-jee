package fr.legrain.bdg.rest.model;

import java.io.Serializable;

public class ParamFirebaseCloudMessaging implements Serializable {

	private static final long serialVersionUID = 1700193895203834539L;
	
	private String ancienToken;
	private String nouveauToken;

	public String getAncienToken() {
		return ancienToken;
	}

	public void setAncienToken(String ancienToken) {
		this.ancienToken = ancienToken;
	}

	public String getNouveauToken() {
		return nouveauToken;
	}

	public void setNouveauToken(String nouveauToken) {
		this.nouveauToken = nouveauToken;
	}
}