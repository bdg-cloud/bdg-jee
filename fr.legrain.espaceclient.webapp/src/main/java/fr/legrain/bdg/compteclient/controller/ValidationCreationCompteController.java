package fr.legrain.bdg.compteclient.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.service.LgrEmail;
import fr.legrain.bdg.compteclient.service.droits.SessionInfo;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
//import fr.legrain.droits.model.TaUtilisateur;
//import fr.legrain.email.LgrEmail;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.data.LibExecLinux;

@ManagedBean
@ViewScoped 
public class ValidationCreationCompteController {

	private BdgProperties bdgProperties;

	private TaUtilisateur monUtilisateur = new TaUtilisateur();
	@Inject private	SessionInfo sessionInfo;

	private String param;

	private String identifiant = null;
	private String adresseEmail = null;

	private TaUtilisateur u = null;

	private Boolean actionOk = false;

	private String separateurChaineCryptee = "~";
	
	@ManagedProperty ("#{c_langue}")
	private ResourceBundle cLangue;
	
	public ResourceBundle getcLangue() {
		return cLangue;
	}

	public void setcLangue(ResourceBundle cLangue) {
		this.cLangue = cLangue;
	}

	@EJB private ITaUtilisateurServiceRemote userService;
	@EJB private LgrEmail lgrEmail;

	@PostConstruct
	public void init() {
		bdgProperties = new BdgProperties();
	}

	public void onload() {
//		try {
			System.out.println("param crypt : "+param);
			//param = URLDecoder.decode(param, "UTF-8");
			//System.out.println(Base64.class.getProtectionDomain().getCodeSource().getLocation());
			String paramDecrypt = LibCrypto.decrypt(param);
			System.out.println("param decrypt : "+paramDecrypt);
			monUtilisateur.setNom(paramDecrypt.split(separateurChaineCryptee)[0]);
			monUtilisateur.setPrenom(paramDecrypt.split(separateurChaineCryptee)[1]);
			monUtilisateur.setEmail(paramDecrypt.split(separateurChaineCryptee)[2]);
			monUtilisateur.setPasswd(paramDecrypt.split(separateurChaineCryptee)[3]);
			
			creationDuCompte();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	}
	
	public void creationDuCompte() {
		try {
			TaUtilisateur u = userService.ctrlSaisieEmail(monUtilisateur.getEmail());

			if (u == null) {
				System.out.println("Succès de l'inscription.");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", cLangue.getString("creer_un_compt_alerte_succes_inscription")));
		
				// Copie la valeur Email du formulaire dans Username pour correspondre à la table
				monUtilisateur.setUsername(monUtilisateur.getEmail());
		
				// Crypte le string passwd en Hash
				String mdp= monUtilisateur.getPasswd();
				monUtilisateur.setPasswd(monUtilisateur.passwordHashSHA256_Base64(monUtilisateur.getPasswd()));
		
				// Enregistre dans bdd le nouvel utilisateur
				monUtilisateur = userService.merge(monUtilisateur);
		
				// Vide le formulaire
		
				//	monUtilisateur = new TaUtilisateur();
				//setoK(false);
		
				// Prepare la redirection de la page " voir plus d'info avec Nicolas "
		
				FacesContext context = FacesContext.getCurrentInstance();
				ExternalContext externalContext = context.getExternalContext();
				HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		
				// Teste si une session est en cours, si oui appel de la methode logout destruction de la session
				if (request.getUserPrincipal() != null) {
					request.logout();
				}
				// Affecte à login le email et le mot de passe du nouvel utilisateur
				request.login(monUtilisateur.getUsername(), mdp);
		
				// Recréer une session avec nouvel utilisateur en lui donnant l'objet monUtilisateur
				externalContext.getSessionMap().put("userSession", monUtilisateur);
				sessionInfo.setUtilisateur(monUtilisateur);
		
				// Redirige l'utilisateur vers page Espace utilisateur via index.html si session inexistante redirection login.xhtml
				externalContext.redirect(externalContext.getRequestContextPath()+"/index.xhtml");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Info!", cLangue.getString("creer_un_compte_alerte_compte_existe_deja")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actFermer() {

	}

	private boolean verifComplexiteMotDePasse(String pwd) {
		return true;
	}

	public TaUtilisateur getU() {
		return u;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getAdresseEmail() {
		return adresseEmail;
	}

	public void setAdresseEmail(String adresseEmail) {
		this.adresseEmail = adresseEmail;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Boolean getActionOk() {
		return actionOk;
	}

	public void setActionOk(Boolean actionOk) {
		this.actionOk = actionOk;
	}

}
