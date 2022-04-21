package fr.legrain.bdg.compteclient.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.service.LgrEmail;
import fr.legrain.bdg.compteclient.service.droits.SessionInfo;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaUtilisateurServiceRemote;

@ManagedBean
@ViewScoped
public class CreationCompteController implements Serializable{

	private static final long serialVersionUID = -4475837394030326020L;

	// Pour enregistrer le chemin de la vue affichant les msg d'erreurs, non utilisé en JSF
	public static final String VUE             = "/creation-utilisateurs.xhtml";

	// Pour enregistrer les erreurs de champ
	public static final String CHAMP_NOM       = "nom";

	public static final String CHAMP_PRENOM    = "prenom";
	public static final String CHAMP_EMAIL     = "email";
	public static final String CHAMP_PASS      = "motdepasse";
	public static final String CHAMP_CONF      = "confirmation";
	public static final String ATT_ERREURS     = "erreurs";
	public static final String ATT_RESULTAT    = "resultat";

	private String resultat;

	// Map pour stocker les erreurs
	private Map<String, String> erreurs = new HashMap<String, String>();

	// ************************************************************************	
	//private boolean oK=true;

	//@NotNull(message = "Les mots de passe entrés sont différents, merci de les saisir à nouveau.")
	private String confirmpasswd;
	
	@ManagedProperty ("#{c_langue}")
	private ResourceBundle cLangue;
	
	private TaUtilisateur monUtilisateur = new TaUtilisateur();
	// 	
	@Inject private	SessionInfo sessionInfo;
	private @EJB ITaUtilisateurServiceRemote service;
	private @EJB LgrEmail lgrEmail;

	public String getConfirmPasswd() {
		return confirmpasswd;	
	}

	public void setConfirmPasswd(String confirmpasswd) {
		this.confirmpasswd = confirmpasswd;
	}
	
	public ResourceBundle getcLangue() {
		return cLangue;
	}

	public void setcLangue(ResourceBundle cLangue) {
		this.cLangue = cLangue;
	}
	
	public void init() throws IOException {
		
		if (sessionInfo.getSessionLangue()!= null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionInfo.getSessionLangue());
		}
		
		monUtilisateur = new TaUtilisateur();
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath()+"/creation-utilisateurs.xhtml");

	}
	public TaUtilisateur getMonUtilisateur() {
		return monUtilisateur;
	}

	public void setMonUtilisateur(TaUtilisateur monUtilisateur) {
		this.monUtilisateur = monUtilisateur;
	}

	// Methode ajoute un utilisateur
	public void ajouteUtilisateur() throws Exception {

		// Controle de l'existance de l'adresse email dans la bdd
		TaUtilisateur u = service.ctrlSaisieEmail(monUtilisateur.getEmail());

		if (u == null) {
			// je vide la Map des erreurs antérieure
			erreurs.clear();

			// Si adresse inexistante appel des methodes de verification des champs du formulaire
			try {
				verifieNom(monUtilisateur.getNom());
			} catch ( Exception e ) {
				// Ajoute l'erreur a la MAP
				erreurs.put( CHAMP_NOM, e.getMessage() );

				// Fait remonter l'erreur à la vue avec PrimeFaces          
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur!", e.getMessage()));
			}

			try {
				verifiePrenom(monUtilisateur.getPrenom());
			} catch ( Exception e ) {
				// Ajoute l'erreur a la MAP
				erreurs.put( CHAMP_PRENOM, e.getMessage() );

				// Fait remonter l'erreur à la vue avec PrimeFaces        
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur!", e.getMessage()));
			}

			try {
				verifieEmail(monUtilisateur.getEmail());
			} catch ( Exception e ) {
				// Ajoute l'erreur a la MAP
				erreurs.put( CHAMP_EMAIL, e.getMessage() );

				// Fait remonter l'erreur à la vue avec PrimeFaces           
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur!", e.getMessage()));
			}

			try {
				verifieMotsDePasse(monUtilisateur.getPasswd(),getConfirmPasswd());
			} catch ( Exception e ) {
				// Ajoute l'erreur a la MAP
				erreurs.put( CHAMP_PASS, e.getMessage() );

				// Fait remonter l'erreur à la vue avec PrimeFaces             
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur!", e.getMessage()));
			}

			// Initialisation du résultat global de la validation. 
			if ( erreurs.isEmpty() ) {
				lgrEmail.emailPourLaValidationDeCreationDunCompteClient(monUtilisateur);
				
				resultat = cLangue.getString("creer_un_compt_alerte_succes_inscription");
				System.out.println("Succès de la demande d'inscription.");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!",
						cLangue.getString("creer_un_compte_alerte_succes_email")+monUtilisateur.getEmail()));
											
//				// Copie la valeur Email du formulaire dans Username pour correspondre à la table
//				monUtilisateur.setUsername(monUtilisateur.getEmail());
//
//				// Crypte le string passwd en Hash
//				String mdp= monUtilisateur.getPasswd();
//				monUtilisateur.setPasswd(monUtilisateur.passwordHashSHA256_Base64(monUtilisateur.getPasswd()));
//
//				// Enregistre dans bdd le nouvel utilisateur
//				monUtilisateur = service.merge(monUtilisateur);
//
//				// Vide le formulaire
//
//				//	monUtilisateur = new TaUtilisateur();
//				//setoK(false);
//
//				// Prepare la redirection de la page " voir plus d'info avec Nicolas "
//
//				FacesContext context = FacesContext.getCurrentInstance();
//				ExternalContext externalContext = context.getExternalContext();
//				HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
//
//				// Teste si une session est en cours, si oui appel de la methode logout destruction de la session
//				if (request.getUserPrincipal() != null) {
//					request.logout();
//				}
//				// Affecte à login le email et le mot de passe du nouvel utilisateur
//				request.login(monUtilisateur.getUsername(), mdp);
//
//				// Recréer une session avec nouvel utilisateur en lui donnant l'objet monUtilisateur
//				externalContext.getSessionMap().put("userSession", monUtilisateur);
//				sessionInfo.setUtilisateur(monUtilisateur);
//
//				// Redirige l'utilisateur vers page Espace utilisateur via index.html si session inexistante redirection login.xhtml
//				externalContext.redirect(externalContext.getRequestContextPath()+"/index.xhtml");	
			} else {        	
				resultat = cLangue.getString("creer_un_compte_alerte_echec_inscription");
				System.out.println("Échec de l'inscription.");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erreur!", cLangue.getString("creer_un_compte_alerte_echec_inscription")));
				//setoK(true);
			}
		} else {
			// Affichage en console si adresse email existe déja dans bdd
			System.out.println("Adresse email "+ u.getEmail()+ " déja existante");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur!", cLangue.getString("creer_un_compte_alerte_adresse_email")+ u.getEmail()+ cLangue.getString("creer_un_compte_alerte_existe_deja")));
		}
	}

	// méthode de verification des champs du formulaire nouvel utilisateur 
	private void verifieNom(String nom) throws Exception {
		if (monUtilisateur.getNom().isEmpty()) {
			throw new Exception( cLangue.getString("creer_un_compte_alerte_nom_vide") );
		}
	}

	private void verifiePrenom(String prenom) throws Exception {
		if (monUtilisateur.getPrenom().isEmpty()) {
			throw new Exception( cLangue.getString("creer_un_compte_alerte_prenom_vide") );
		}
	}

	public void verifieEmail( String email ) throws Exception {
		if (!monUtilisateur.getEmail().isEmpty()) {
			if (!monUtilisateur.getEmail().matches( "^[_a-z0-9-\\.\\+]+(\\[_a-z0-9-]+)*@"
					+ "[a-z0-9-]+(\\[a-z0-9]+)*(\\.[a-z]{2,})$" ) ) {
				throw new Exception( cLangue.getString("creer_un_compte_alerte_erreur_email") );
			}
		} else {
			throw new Exception( cLangue.getString("creer_un_compte_alerte_email_vide") );
		}
	}

	private void verifieMotsDePasse(String passwd, String champConf) throws Exception{
		if (monUtilisateur.getPasswd() != null 
				&& monUtilisateur.getPasswd().trim().length() != 0 
				&& getConfirmPasswd() != null 
				&& confirmpasswd.trim().length() != 0) {
			if (!monUtilisateur.getPasswd().equals(getConfirmPasswd())) {
				throw new Exception(cLangue.getString("creer_un_compte_alerte_erreur_confirmation_mdp"));
			} else if (monUtilisateur.getPasswd().trim().length() < 6) {
				throw new Exception(cLangue.getString("creer_un_compte_alerte_erreur_mdp_inf_6_caract"));
			} else if (!monUtilisateur.getPasswd().matches( "^(?=(.*[a-zA-Z]))(?=(.*[A-Z]){1})(?=(.*[0-9]){1}).{6,}"
					) ) {
				throw new Exception(cLangue.getString("creer_un_compte_alerte_erreur_mdp__maj_chiffre"));
			}
		} else {
			throw new Exception(cLangue.getString("creer_un_compte_alerte_mdp_vide"));
		}

	}
}
