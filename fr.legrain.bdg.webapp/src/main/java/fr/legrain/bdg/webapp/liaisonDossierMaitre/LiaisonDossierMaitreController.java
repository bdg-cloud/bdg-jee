package fr.legrain.bdg.webapp.liaisonDossierMaitre;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.general.service.remote.ITaLiaisonDossierMaitreServiceRemote;
import fr.legrain.bdg.rest.model.ParamCompteEspaceClient;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.bdg.ws.rest.client.Config;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaPanier;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.general.model.TaLiaisonDossierMaitre;
import fr.legrain.generation.service.CreationDocumentLigneEcheanceAbonnementMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;

@Named
@ViewScoped
public class LiaisonDossierMaitreController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1653801083238227860L;
	
	private String loginDeLaTableEspaceClient = null;
	private String passwordDeLaTableEspaceClient = null;
	
	private String loginCreation = null;
	private String passwordCreation = null;
	
	private String codeTiersCreation = null;
	private List<TaTiersDTO> listeTiersDTO = null;
	private TaEspaceClientDTO espaceClientDTO = null;
	@EJB private ITaLiaisonDossierMaitreServiceRemote taLiaisonDossierMaitreService;
	
	//String dossierTenant = "demo_legrain82_dev_17mars21";
	String dossierTenant = "legrain82";
	
	boolean tiersExiste = false;
	
	String messageInfoSaisieTiers = "Nous avons besoin de votre code Tiers pour pouvoir finaliser la création de votre espace client. Si vous connaissez votre code tiers, veuillez le saisir et enregistrer."
			+ " Si vous ne connaissez pas votre code tiers, contacter Legrain informatique 05 63 30 31 44 .";
	
	Config config;
	
	@PostConstruct
	public void init() {

	}
	public void loginCreation() {
		if(loginCreation != null && passwordCreation != null ) {
			//String dossierTenant = "demo";
			
//			loginDeLaTableEspaceClient = "gestion@melietdev.com";
//			passwordDeLaTableEspaceClient = "pwdlgr";
			
			/*********************************************************************************************************************************
			 * Avec token JWT - login/password d'utilisateur Espace client
			 * *******************************************************************************************************************************
			 */
			System.out.println("************* TOKEN JWT ESPACE CLIENT **********************");
			//Création et configuration du client
			config = new Config(dossierTenant,null, null);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			
			//Connexion du client pour une connexion par token JWT
//			String codeTiers = bdg.connexionEspaceClient();
//			System.out.println("code tier  : "+codeTiers);
			
			ParamCompteEspaceClient param = new ParamCompteEspaceClient();
			param.setEmail(loginCreation);
			param.setPassword(passwordCreation);
			param.crypte();
			
			
			ObjectMapper mapper = new ObjectMapper();
			try {
				String paramStr = mapper.writeValueAsString(param);
				//Appel utilisant le token
				try {
					espaceClientDTO = bdg.espaceClient().creerCompteEspaceClient(paramStr);
					if(espaceClientDTO != null) {
						recupTiers();
						liaisonNouveauCompteTiers();
					}else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Une erreur inconnue est survenue pendant l'enregistrement du compte espace client.",""));
					}
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, e.getMessage(),""));
					e.printStackTrace();
				}
				
				
				
				
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Formulaire incomplet",""));
		}
		
		
	}
	public void abandonLiaisonCompteExistant() {
		tiersExiste = false;
	}
	public void liaisonCompteExistant() {
		String codeChoisi = null;
		if(codeTiersCreation != null && espaceClientDTO != null) {
			for (TaTiersDTO taTiersDTO : listeTiersDTO) {
				if(taTiersDTO.getCodeTiers().equals(codeTiersCreation)) {
					codeChoisi = codeTiersCreation;
				}
			}
			if(codeChoisi != null) {
				//Création et configuration du client
				config = new Config(dossierTenant,loginCreation, passwordCreation);
				config.setVerificationSsl(false);
				config.setDevLegrain(true);
				BdgRestClient bdg = BdgRestClient.build(config);
				
				//Connexion du client pour une connexion par token JWT
				String codeTiers = bdg.connexionEspaceClient();
				
				ParamCompteEspaceClient param = new ParamCompteEspaceClient();
				param.setEmail(loginCreation);
				param.setPassword(passwordCreation);
				param.setId(espaceClientDTO.getId());
				param.setCodeTiers(codeChoisi);
				param.crypte();
				
				
				ObjectMapper mapper = new ObjectMapper();
				String paramStr;
				try {
					paramStr = mapper.writeValueAsString(param);
					espaceClientDTO = bdg.espaceClient().liaisonNouveauCompteEspaceClient(paramStr);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Votre espace client lié au code tiers "+codeTiersCreation+" à bien été créer.",""));
				tiersExiste = false;
				
			}else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_WARN, "Le code tiers "+codeTiersCreation+" ne correspond à aucun tiers connu. Veuillez recommencer.",""));
				
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_WARN, "Veuillez saisir un code tiers pour pouvoir lié ce code tiers a votre compte espace client.",""));
		}
	}
	public void liaisonNouveauCompteTiers() {
		if(listeTiersDTO == null || listeTiersDTO.isEmpty()) {
			//Création et configuration du client
			config = new Config(dossierTenant,loginCreation, passwordCreation);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			//Connexion du client pour une connexion par token JWT
			String codeTiers = bdg.connexionEspaceClient();
			
			ParamCompteEspaceClient param = new ParamCompteEspaceClient();
			param.setEmail(loginCreation);
			param.setPassword(passwordCreation);
			param.setId(espaceClientDTO.getId());
			param.crypte();
			
			
			ObjectMapper mapper = new ObjectMapper();
			String paramStr;
			try {
				paramStr = mapper.writeValueAsString(param);
				espaceClientDTO = bdg.espaceClient().liaisonNouveauCompteEspaceClient(paramStr);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
		}else {
			tiersExiste = true;
		}
	}
	public void recupTiers() {

		//Création et configuration du client
		//Config config = new Config(dossierTenant,null, null);
		config = new Config(dossierTenant,loginCreation, passwordCreation);
		config.setVerificationSsl(false);
		config.setDevLegrain(true);
		BdgRestClient bdg = BdgRestClient.build(config);
		String codeTiers = bdg.connexionEspaceClient();
		
		listeTiersDTO = bdg.espaceClient().listeTiersEspaceClient(espaceClientDTO.getId());
		if(listeTiersDTO != null && !listeTiersDTO.isEmpty()) {
			tiersExiste = true;
		}
	}
	
	public void login() {
		if(loginDeLaTableEspaceClient != null && passwordDeLaTableEspaceClient != null) {
			//String dossierTenant = "demo";
			
//			loginDeLaTableEspaceClient = "gestion@melietdev.com";
//			passwordDeLaTableEspaceClient = "pwdlgr";
			
			//passwordDeLaTableEspaceClient = LibCrypto.encrypt(passwordDeLaTableEspaceClient);

			//Création et configuration du client
			config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			
			//Connexion du client pour une connexion par token JWT
			try {
				String codeTiers = bdg.connexionEspaceClient();
				
				if(codeTiers != null) {
					TaLiaisonDossierMaitre liaison = taLiaisonDossierMaitreService.findInstance(loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient,codeTiers );
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_INFO, "Connexion réussie.",""));
					
				}else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Identifiant/Mot de passe espace client incorrect.",""));
				}
				
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_WARN, "Identifiant/Mot de passe espace client incorrect.",""));
			}
			
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
			FacesMessage.SEVERITY_WARN, "Formulaire incomplet",""));
		}
	}
	
	public void actFermer(ActionEvent actionEvent) {


	}
	

	public String getLoginDeLaTableEspaceClient() {
		return loginDeLaTableEspaceClient;
	}


	public void setLoginDeLaTableEspaceClient(String loginDeLaTableEspaceClient) {
		this.loginDeLaTableEspaceClient = loginDeLaTableEspaceClient;
	}


	public String getPasswordDeLaTableEspaceClient() {
		return passwordDeLaTableEspaceClient;
	}


	public void setPasswordDeLaTableEspaceClient(String passwordDeLaTableEspaceClient) {
		this.passwordDeLaTableEspaceClient = passwordDeLaTableEspaceClient;
	}

	public String getLoginCreation() {
		return loginCreation;
	}

	public void setLoginCreation(String loginCreation) {
		this.loginCreation = loginCreation;
	}

	public String getPasswordCreation() {
		return passwordCreation;
	}

	public void setPasswordCreation(String passwordCreation) {
		this.passwordCreation = passwordCreation;
	}
	public String getCodeTiersCreation() {
		return codeTiersCreation;
	}
	public void setCodeTiersCreation(String codeTiersCreation) {
		this.codeTiersCreation = codeTiersCreation;
	}
	public List<TaTiersDTO> getListeTiersDTO() {
		return listeTiersDTO;
	}
	public void setListeTiersDTO(List<TaTiersDTO> listeTiersDTO) {
		this.listeTiersDTO = listeTiersDTO;
	}
	public TaEspaceClientDTO getEspaceClientDTO() {
		return espaceClientDTO;
	}
	public void setEspaceClientDTO(TaEspaceClientDTO espaceClientDTO) {
		this.espaceClientDTO = espaceClientDTO;
	}
	public boolean isTiersExiste() {
		return tiersExiste;
	}
	public void setTiersExiste(boolean tiersExiste) {
		this.tiersExiste = tiersExiste;
	}
	public String getMessageInfoSaisieTiers() {
		return messageInfoSaisieTiers;
	}
	public void setMessageInfoSaisieTiers(String messageInfoSaisieTiers) {
		this.messageInfoSaisieTiers = messageInfoSaisieTiers;
	}
	
	
	

}
