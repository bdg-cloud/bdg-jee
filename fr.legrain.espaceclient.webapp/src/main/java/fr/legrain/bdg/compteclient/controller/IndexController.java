package fr.legrain.bdg.compteclient.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.service.droits.SessionInfo;

@ManagedBean
@ViewScoped
public class IndexController implements Serializable{

	private static final long serialVersionUID = 6323631969399978161L;
	
	@Inject private	SessionInfo sessionInfo;
	@Inject private LanguageBean sessionLangue;
	
	private TaUtilisateur utilisateur;
	
	@PostConstruct	
	public void init () {
		utilisateur = sessionInfo.getUtilisateur();
		FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale)sessionLangue.getLocale());
		
		if (sessionInfo.getSessionLangue()!= null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionInfo.getSessionLangue());
		}
	}

	public TaUtilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(TaUtilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public void onIdle() {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sans activité De votre part durant 15 minutes votre session sera détruite"));
	}

	public void onActive() {

	}
	
}
