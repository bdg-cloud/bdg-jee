package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.Map;

public class EditionParam implements Serializable {
	
public static final String NOM_OBJET_EN_SESSION = "paramEdition";
	
	private String codeTypeEdition;
	private String idActionInterne;
	private Map<String, Object> mapParametreEdition;
	
	
	public String getCodeTypeEdition() {
		return codeTypeEdition;
	}
	public void setCodeTypeEdition(String codeTypeEdition) {
		this.codeTypeEdition = codeTypeEdition;
	}
	public String getIdActionInterne() {
		return idActionInterne;
	}
	public void setIdActionInterne(String idActionInterne) {
		this.idActionInterne = idActionInterne;
	}
	public Map<String, Object> getMapParametreEdition() {
		return mapParametreEdition;
	}
	public void setMapParametreEdition(Map<String, Object> mapParametreEdition) {
		this.mapParametreEdition = mapParametreEdition;
	}
}
