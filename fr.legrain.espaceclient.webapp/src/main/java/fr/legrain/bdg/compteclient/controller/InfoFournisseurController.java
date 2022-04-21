package fr.legrain.bdg.compteclient.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.service.droits.SessionInfo;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaMesFournisseursServiceRemote;
import fr.legrain.bdg.compteclient.ws.TaFacture;

@ManagedBean //Est responsable de la gestion des opérations sur une entité notamment grâce à plusieurs méthodes
@ViewScoped
public class InfoFournisseurController implements Serializable {

	private static final long serialVersionUID = 987460187689622614L;

	@Inject private	SessionInfo sessionInfo;
	private @EJB ITaMesFournisseursServiceRemote serviceMesFournisseurs;

	private TaFournisseur taFournisseur = null;

	private StreamedContent logo;
	private String codeDossierFrs = null;
	private String id=null;
	private String codeClient = null;

	private List<TaFacture> listeFacture = null;
	private TaFacture selectedFacture = null;
	
	public InfoFournisseurController() {

	}

	@PostConstruct
	public void init() {
		try {
			System.out.println("passage init");
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String,Object> sessionMap = externalContext.getSessionMap();
			taFournisseur = (TaFournisseur) sessionMap.get("infoFrs");
			updateFrs();
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sessionInfo.getSessionLangue()!= null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionInfo.getSessionLangue());
		}
	}

	public void refresh() {
		try {
			System.out.println("TestController.init()");
			System.out.println(id);		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	} 

	public void updateFrs() {
		if(taFournisseur!=null && taFournisseur.getBlobLogo()!=null) {
			InputStream stream = new ByteArrayInputStream(taFournisseur.getBlobLogo()); 
			setLogo(new DefaultStreamedContent(stream));
		}
	}

	public TaFacture getSelectedFacture() {
		return selectedFacture;
	}

	public void setSelectedFacture(TaFacture selectedFacture) {
		this.selectedFacture = selectedFacture;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TaFournisseur getTaFournisseur() {
		return taFournisseur;
	}

	public void setTaFournisseur(TaFournisseur taFournisseur) {
		this.taFournisseur = taFournisseur;
	}

	public StreamedContent getLogo() {
		return logo;
	}

	public void setLogo(StreamedContent logo) {
		this.logo = logo;
	}
	
	public String getCodeDossierFrs() {
		return codeDossierFrs;
	}

	public void setCodeDossierFrs(String codeDossierFrs) {
		this.codeDossierFrs = codeDossierFrs;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public List<TaFacture> getListeFacture() {
		return listeFacture;
	}

	public void setListeFacture(List<TaFacture> listeFacture) {
		this.listeFacture = listeFacture;
	}
}
