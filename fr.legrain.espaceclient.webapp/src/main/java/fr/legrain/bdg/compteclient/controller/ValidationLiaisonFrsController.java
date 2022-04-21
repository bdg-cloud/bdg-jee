package fr.legrain.bdg.compteclient.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.service.LgrEmail;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
//import fr.legrain.droits.model.TaUtilisateur;
//import fr.legrain.email.LgrEmail;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.data.LibExecLinux;

@ManagedBean
@ViewScoped 
public class ValidationLiaisonFrsController {

	private BdgProperties bdgProperties;

	private String motDePasseActuel = "";
	private String nouveauMotDePasse = "";
	private String confirmationMotDePasse = "";

	private String param;

	private String identifiant = null;
	private String adresseEmail = null;

	private TaUtilisateur u = null;

	private Boolean actionOk = false;

	private String separateurChaineCryptee = "~";

	private	String tenantDossier;

	@EJB private ITaUtilisateurServiceRemote userService;
	//@EJB private ITaUtilisateurLoginServiceRemote utilisateurLoginService;
	@EJB private LgrEmail lgrEmail;
	//@Inject private	TenantInfo tenantInfo;

	@PostConstruct
	public void init() {
		bdgProperties = new BdgProperties();
		initTenant();
	}

	public void onload() {
		try {
			System.out.println("param crypt : "+param);
			param = URLDecoder.decode(param, "UTF-8");
			String paramDecrypt = LibCrypto.decrypt(param);
			System.out.println("param decrypt : "+paramDecrypt);
			tenantDossier = paramDecrypt.split(separateurChaineCryptee)[0];
			identifiant = paramDecrypt.split(separateurChaineCryptee)[1];
			adresseEmail = paramDecrypt.split(separateurChaineCryptee)[2];
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void initTenant() {
		//		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		//		System.out.println("dddd : "+origRequest.getServerName());
		//		String tenant = origRequest.getServerName().substring(0,origRequest.getServerName().lastIndexOf("."));//supprime le tld (dev.demo.promethee.biz => dev.demo.promethee)
		//		tenant = tenant.substring(0,tenant.lastIndexOf("."));//suprime le domaine principal (dev.demo.promethee => dev.demo)
		//		tenant = tenant.substring(tenant.lastIndexOf(".")+1);//conserve uniquement le premier niveau de sous domaine (dev.demo => demo)
		//		System.out.println("Dossier/tenant change mdp : "+tenant);
		//		tenantDossier = tenant;
		//		tenantInfo.setTenantId(tenant);
	}

	public void demandeMotDePasse(ActionEvent event) throws Exception {

		initTenant();

		u = userService.findByCode(identifiant);

		if(u!=null) {
			System.out.println("MdpOublieBean.demandeMotDePasse() => dossier : "+tenantDossier+" ** adresseEmail : "+adresseEmail);

			String a = tenantDossier+separateurChaineCryptee+identifiant+separateurChaineCryptee+adresseEmail;
			String b = LibCrypto.encrypt(a);

			//		String subject = "mon sujet ..";
			//		String msgTxt = "mon email ...";
			//		String fromEmail = "nicolas@legrain.fr";
			//		String fromName = "Nicolas";
			//		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};

			String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Demande de changement de mot de passe";

			String utilisateur = identifiant+" "+adresseEmail;
			//		String lienCnx = "http://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+tenantDossier+"."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+":"+bdgProperties.getProperty(BdgProperties.KEY_PORT_HTTP,"")+"/login/mdp_validation.xhtml?p="+URLEncoder.encode(b, "UTF-8")+"";
			String lienCnx = bdgProperties.url("espaceclient")+"/login/mdp_validation.xhtml?p="+URLEncoder.encode(b, "UTF-8")+"";
			String lienCnxTxt = "";
			String msgTxt = "Bonjour "+utilisateur+",\n"
					+"\n"
					+"nous avons reçu une demande de modification de mot passe pour votre dossier Bureau de Gestion.\n"
					+"\n"
					+"Afin de finaliser ce changement de mot de passe, merci de cliquer sur le lien suivant :\n"
					+"\n"
					+""+lienCnx+"\n"
					+"\n"
					//+"Si vous ne pouvez pas cliquer sur le lien, copiez le entièrement et collez le dans la barre d'adresse de votre navigateur puis validez.\n"
					//+"\n"
					//+""+lienCnxTxt+"\n"
					//+"\n"
					+"Si vous n'êtes pas à l'origine de cette demande, ignorez ce message. Vous conserverez vos identifiants habituels pour vous connecter sur le Bureau de Gestion.\n"
					+"\n"
					+"Cordialement,\n"
					+"\n"
					+"Service Gestion.\n"
					+"\n"
					+"Le Bureau de Gestion est un service en ligne dédié à la gestion des entreprises proposé par Legrain SAS. \n"
					;
			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
			String[][] toEmailandName = new String[][]{ {adresseEmail,""}};

			lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);

			//envoie d'une copie à legrain
			toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};//Zee6Zasa
			lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC

			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("", 
					"Vous allez recevoir un email pour changer votre mot de passe à l'adresse suivante : "+adresseEmail
					)); 

			actionOk = true;
		} else {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aucun utilisateur avec cet identifiant n'existe dans le dossier : "+tenantDossier, 
					""
					)); 
		}

	}

	public void changeMotDePasse(ActionEvent event) throws Exception {

		String nouveauMdpGenere = LibExecLinux.pwgen();

		initTenant();
		u = userService.findByCode(identifiant);

		if(u!=null) {

			if(nouveauMotDePasse!=null && !nouveauMotDePasse.equals("") 
					&& confirmationMotDePasse!=null && !confirmationMotDePasse.equals("") 
					&& nouveauMotDePasse.equals(confirmationMotDePasse)) {
				//le nouveau mot de passe saisie est correct

				if(verifComplexiteMotDePasse(nouveauMotDePasse)) {
					u.setPasswd(u.passwordHashSHA256_Base64(nouveauMotDePasse));
					userService.enregistrerMerge(u);

					//		String subject = "mon sujet ..";
					//		String msgTxt = "mon email ...";
					//		String fromEmail = "nicolas@legrain.fr";
					//		String fromName = "Nicolas";
					//		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};

					String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de modification de votre mot de passe";

					String utilisateur = identifiant+" "+adresseEmail;
					String msgTxt = "Bonjour "+utilisateur+",\n"
							+"\n"
							+"nous vous confirmons que la modification de votre mot de passe a bien été prise en compte.\n"
							+"\n"
							+"Notez bien que seul votre mot de passe a été changé et que le nom d'utilisateur est toujours le même.\n"
							+"\n"
							+"Important !\n"
							+"\n"
							+"Si vous n'êtes pas l'origine de cette modification, veuillez contacter le plus vite possible notre support soit :\n"
							+"\n"
							+"Par téléphone : 05.63.30.31.44\n"
							+"\n"
							+"Par Email : support@legrain.fr\n"
							+"\n"
							+"En cas de communication par email, n'oubliez pas de rappeler votre nom d'utilisateur et votre code client.\n"
							+"\n"
							+"Cordialement,\n"
							+"\n"
							+"Service Gestion.\n"
							+"\n"
							+"Le Bureau de Gestion est un service en ligne dédié à la gestion des entreprises proposé par Legrain SAS. \n"
							;
					String fromEmail = "legrain@legrain.fr";
					String fromName = "Legrain";
					String[][] toEmailandName = new String[][]{ {adresseEmail,""}};

					lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);

					//envoie d'une copie à legrain
					//toEmailandName = new String[][]{ {"legrain@legrain.fr",null}};
					//lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
					//TODO faire avec BCC

					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("", 
							//"Votre nouveau mot de passe viens de vous être envoyé à l'adresse suivante : "+adresseEmail
							"Votre mot de passe a bien été changé. Un mail de confirmation a été envoyé à l'adresse suivante : "+adresseEmail+" **** "+nouveauMotDePasse
							)); 

					actionOk = true;


				} else {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Votre nouveau mot de passe est trop simple", 
							""
							)); 
				}
			} else {

				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Les valeurs saisie doivent être identique", 
						""
						)); 
			}
		} else {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aucun utilisateur avec cet identifiant n'existe dans le dossier : "+tenantDossier, 
					""
					)); 
		}
	}

	public void actFermer() {

	}

	private boolean verifComplexiteMotDePasse(String pwd) {
		return true;
	}

	public String getMotDePasseActuel() {
		return motDePasseActuel;
	}

	public void setMotDePasseActuel(String motDePasseActuel) {
		this.motDePasseActuel = motDePasseActuel;
	}

	public String getNouveauMotDePasse() {
		return nouveauMotDePasse;
	}

	public void setNouveauMotDePasse(String nouveauMotDePasse) {
		this.nouveauMotDePasse = nouveauMotDePasse;
	}

	public String getConfirmationMotDePasse() {
		return confirmationMotDePasse;
	}

	public void setConfirmationMotDePasse(String confirmationMotDePasse) {
		this.confirmationMotDePasse = confirmationMotDePasse;
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

	public String getTenantDossier() {
		return tenantDossier;
	}

	public void setTenantDossier(String tenantDossier) {
		this.tenantDossier = tenantDossier;
	}

	public Boolean getActionOk() {
		return actionOk;
	}

	public void setActionOk(Boolean actionOk) {
		this.actionOk = actionOk;
	}

}
