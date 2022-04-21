package fr.legrain.bdg.compteclient.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.bdg.compteclient.service.LgrEmail;
import fr.legrain.bdg.compteclient.service.droits.SessionInfo;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaMesFournisseursServiceRemote;
import fr.legrain.bdg.ws.client.CompteClientServiceClientCXF;

@ManagedBean //Est responsable de la gestion des opérations sur une entité notamment grâce à plusieurs méthodes
@ViewScoped
public class MesFournisseursController implements Serializable{

	private static final long serialVersionUID = -269658616678934960L;

	private TaMesFournisseurs mesFournisseurs = new TaMesFournisseurs();
	private TaFournisseur f = new TaFournisseur();

	private CompteClientServiceClientCXF wsCompteClient;
	private String codeClient;
	private String cleFournisseur;
	
	@ManagedProperty ("#{c_langue}")
	private ResourceBundle cLangue;

	@Inject private	SessionInfo sessionInfo;	
	private List<TaMesFournisseurs> listeMesFournisseurs = new ArrayList<TaMesFournisseurs> ();
	
	private @EJB ITaMesFournisseursServiceRemote serviceMesFournisseurs;
	private @EJB LgrEmail lgrEmail;

	@PostConstruct //La méthode est invoquée après que l'instance est créée et que les dépendances sont injectées
	public void debut() {
		
		if (sessionInfo.getSessionLangue()!= null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionInfo.getSessionLangue());
		}
		listeMesFournisseurs = serviceMesFournisseurs.rechercheMesFournisseurs(sessionInfo.getUtilisateur().getId());
	}

	public void ajouteMonFournisseur(TaFournisseur f)throws Exception {
		boolean b =true;

		b = serviceMesFournisseurs.verifieSiLiaisonClientFournisseurExisteDeja(f.getIdFournisseur(), codeClient);
		
		if (b){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", cLangue.getString("popup_info_fournisseur_alerte_code_existe_deja")));
		} else {

			wsCompteClient = new CompteClientServiceClientCXF();

			b = wsCompteClient.clientExisteChezFournisseur(f.getCodeDossier(), codeClient, cleFournisseur);
			if(b) {
				
//				TaTiers client = wsCompteClient.infosClientChezFournisseur(f.getCodeDossier(), codeClient);
//				lgrEmail.emailPourLaValidationDuneLiaisonAvecUnFournisseur(f, client);
				
				TaMesFournisseurs newFournisseurs = new TaMesFournisseurs();
				newFournisseurs.setTaFournisseur(f);
				newFournisseurs.setCodeClient(codeClient);
				newFournisseurs.setTaUtilisateur(sessionInfo.getUtilisateur());
				listeMesFournisseurs.add(newFournisseurs);
				serviceMesFournisseurs.merge(newFournisseurs);
				for (TaMesFournisseurs element : listeMesFournisseurs) {
					System.out.println(element.getIdFournisseur());
				}
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", cLangue.getString("popup_info_fournisseur_alerte_succes_liaison")));
			}
			else {        	
				System.out.println("Échec de l'inscription.");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!", cLangue.getString("popup_info_fournisseur_alerte_erreur_code")));
			}
		}
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msgInfoMesFournisseur = new FacesMessage( cLangue.getString("popup_info_mes_fournisseurs_header_titre"), ((TaMesFournisseurs) event.getObject()).getTaFournisseur().getRaisonSocialeFournisseur());
		FacesContext.getCurrentInstance().addMessage(null, msgInfoMesFournisseur);
	}
	
	public List<TaMesFournisseurs> getListeMesFournisseurs() {
		return listeMesFournisseurs;
	}

	public void setListeMesFournisseurs(List<TaMesFournisseurs> mesFournisseurs) {
		this.listeMesFournisseurs = mesFournisseurs;
	}

	public TaFournisseur getF() {
		return f;
	}

	public void setF(TaFournisseur f) {
		this.f = f;
	}

	public String getCodeClient() {
		return codeClient;
	}
	public String getCleFournisseur() {
		return cleFournisseur;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	
	public void setCleFournisseur(String cleFournisseur) {
		this.cleFournisseur = cleFournisseur;
	}

	public TaMesFournisseurs getMesFournisseurs() {
		return mesFournisseurs;
	}

	public void setMesFournisseurs(TaMesFournisseurs mesFournisseurs) {
		this.mesFournisseurs = mesFournisseurs;
	}
	
	public ResourceBundle getcLangue() {
		return cLangue;
	}

	public void setcLangue(ResourceBundle cLangue) {
		this.cLangue = cLangue;
	}

}