package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.List;

import fr.legrain.tiers.model.TaTiers;

public class PushParam implements Serializable {
	
	public static final String NOM_OBJET_EN_SESSION = "paramMessagePush";
	
	
	private String sujet;
	private String resume;
	private String contenu;
	private String url;
	private String urlImage;
	private String style;
	private List<TaTiers> destinataires;
	
	public List<TaTiers> getDestinataires() {
		return destinataires;
	}
	public void setDestinataires(List<TaTiers> destinataires) {
		this.destinataires = destinataires;
	}
	
	public String getSujet() {
		return sujet;
	}
	public void setSujet(String sujet) {
		this.sujet = sujet;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrlImage() {
		return urlImage;
	}
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	
	
}
