package fr.legrain.bdg.webapp.dev;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.AccountLinkCreateParams.Type;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.paiement.service.remote.ILgrStripe;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.bdg.ws.rest.client.Config;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.DevService;
import fr.legrain.paiement.service.PaiementLgr;

@Named
@ViewScoped 
public class DevStripeBean implements Serializable {  
	
	private static final long serialVersionUID = 8677377809172429592L;

	private @EJB DevService devService;
//	private @EJB GoogleOAuthClient googleOAuthClient;
	
	private @Inject TenantInfo tenantInfo;
	private @Inject SessionInfo sessionInfo;
	
	private String versionProg;
		
	private String nomDump;
	private String nomDossier;
	private List<Schema> listeBase;
	private List<Schema> listeSchema;
//	private List<String> listeSchema;
		
	public void refresh() {
		
	}
	
	@PostConstruct
	public void init() {
		versionProg = versionProg();
//		initBdd();
//		initSchema();
	}
	
	public void dbDump(ActionEvent event) {
		
		try {
			devService.dbDump(nomDump);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); 
	}
	
	public void dumpBase(ActionEvent event) {
		
		String nomBase = (String) event.getComponent().getAttributes().get("nomBase");
		
		try {
			devService.dbDump(nomDump,nomBase);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); 
	}
	
	public void initBdd() {

		listeBase = new ArrayList<Schema>();
		
		List<String> l = devService.listeBdd();
		Schema s = null;
		for (String string : l) {
			s = new Schema();
			s.setNom(string);
			s.setListeDump(initListeDump(devService.listeFichierDump(null,s.getNom())));
			listeBase.add(s);
		}
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//	    context.addMessage(null, new FacesMessage("Dev", 
//	    		"listeBdd OK"
//	    		)); 
	}
	
	public List<Dump> initListeDump(List<String> l) {
		List<Dump> lDump = new ArrayList<Dump>();
		Dump d = null;
		for (String string : l) {
			d = new Dump();
			d.setNom(string);
			lDump.add(d);
		}
		return lDump;
	}
	
	public void initSchema() {
		List<Object[]> r = devService.listeSchemaTailleConnection();
		
		listeSchema = new ArrayList<Schema>();
		Schema s = null;
		for (Object[] object : r) {
			s = new Schema();
			s.setNom(object[0].toString());
			s.setTaille1(object[1].toString());
			s.setTaille2(object[2].toString());
			s.setListeDump(initListeDump(devService.listeFichierDump(s.getNom(), null)));
			listeSchema.add(s);
		}
	}
	
	public void listeSchemaTailleConnection(ActionEvent event) {

//		devService.listeSchemaTailleConnection();
		
//		for (Object[] object : r) {
//		for (Object object2 : object) {
//			System.out.println(object2);
//		}
//		
//	}
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"listeSchemaTailleConnection OK"
	    		)); 
	}
	
	public void rebootServeur(ActionEvent event) {
		
		devService.rebootServeur();
	
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"rebootServeur OK\n"
	    		)); 
	}
	
	public void ajoutDossier(ActionEvent event) {
		
		devService.ajoutDossier(nomDossier);
		initSchema();
	
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"ajoutDossier "+nomDossier+" OK\n"
	    				+ "Penser à relancer le serveur pour prendre en compte le nouveau sous domaine."
	    		)); 
	}
	
	public void supprimerDossier(ActionEvent event) {
		
		Schema schema = (Schema) event.getComponent().getAttributes().get("nomDossier");
		
		nomDossier = schema.getNom();
//		nomDossier = nomDossier.split("-")[0].trim();
		
		if(!nomDossier.equals("pg_catalog") && !nomDossier.equals("information_schema") && !nomDossier.equals("public")) {
			devService.supprimerDossier(nomDossier);
			initSchema();
			
			FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Dev", 
		    		"supprimerDossier "+nomDossier+"  OK\n"
		    				+ "Penser à relancer le serveur pour prendre en compte la suppression du sous domaine."
		    		)); 
		} else {
			System.out.println("Schema système, suppression interdite : "+nomDossier);
		}
		
	}
	
	public void dumpDossier(ActionEvent event) {
		
		Schema schema = (Schema) event.getComponent().getAttributes().get("nomDossier");
		
		nomDossier = schema.getNom();
//		nomDossier = nomDossier.split("-")[0].trim();
		
		try {
			devService.schemaDump(null,nomDossier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"Dump Database OK"
	    		)); 
		
	}
	
	public void listeDump(ActionEvent event) {
		
//		String nomDossier = (String) event.getComponent().getAttributes().get("nomDossier");
		
//		nomDossier = nomDossier.split("-")[0].trim();
		
//		try {
//		listeDump = devService.listeFichierDump(null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"listeDump OK"
	    		)); 
		
	}
	
	public void restaureDump(ActionEvent event) {
		
		String nomDump = (String) event.getComponent().getAttributes().get("nomDump");
		String nomDossier = (String) event.getComponent().getAttributes().get("nomDossier");
		
//		nomDossier = nomDossier.split("-")[0].trim();
		
		try {
			devService.restaureDB(nomDump,nomDossier);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev", 
	    		"retaureDump OK : "+nomDump+" "+nomDossier
	    		)); 
		
	}
	
	public String versionProg() {
		//returns the major version (2.1)
		String jsfMajorVersion = FacesContext.class.getPackage().getImplementationVersion();

		//returns the specification version (2.1)
		String jsfSpecificationVersion = Package.getPackage("com.sun.faces").getSpecificationVersion();

		//returns the minor implementation version (2.1.x)
		String jsfMinorImplementationVersion = Package.getPackage("com.sun.faces").getImplementationVersion();
		String jsfNameVersion = Package.getPackage("com.sun.faces").getImplementationTitle();

		//org.primefaces.util.Constants.VERSION;
		
//		String primefacesBuildVersion = PrimeRequestContext.getCurrentInstance().getApplicationContext().getConfig().getBuildVersion();

		String v = "* JSF Major Version : "+jsfMajorVersion+
				"<br/> * JSF Specification Version : "+jsfSpecificationVersion+
				"<br/> * JSF Minor Implementation Version : "+jsfMinorImplementationVersion+
				"<br/> * JSF Name Version : "+jsfNameVersion+
//				"<br/> * Primefaces Build Version : "+primefacesBuildVersion+
				"<br/> * Java : "+System.getProperties().getProperty("java.version")
				
				;
		
		FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Dev version", 
	    		"v"
	    		));
	    
	    return v;
	}
	
//	@EJB private ILgrStripe lgrStripe;
	protected @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService lgrStripe;
	private String ibanPrelevment;
	private String nomPersonneAPrelever;
	private String idSourceStripe;
	
	public void creerSourceStripe(ActionEvent event) {
		
		//idSourceStripe = lgrStripe.creerSourcePrelevementSEPA(null, ibanPrelevment, nomPersonneAPrelever);
		sourceStripeIDPrelevement = idSourceStripe;
		sourceStripeIDLiaison = idSourceStripe;
		idSourceDefautStripeCustomerStripe = sourceStripeIDPrelevement;
	}
	
	private String emailCustomerStripe;
	private String descriptionCustomerStripe;
	private String idSourceDefautStripeCustomerStripe;
	private String idCustomerStripe;
	public void creerCustomerStripe(ActionEvent event) {	
		
		//idCustomerStripe = lgrStripe.creerCustomerStripe(null, emailCustomerStripe, idSourceDefautStripeCustomerStripe, descriptionCustomerStripe);
		customerStripeIDPrelevement = idCustomerStripe;
	}
	
	private String customerStripeIDLiaison;
	private String sourceStripeIDLiaison;
	public void attacherSourceStripe(ActionEvent event) {
//		String email = "xxxxxx@xxxxxx.com";
//		String sourceId = "src_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
//		String description = "0000 - xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		
		//idCustomerStripe = lgrStripe.attacherSourceStripe(null, sourceStripeIDLiaison, customerStripeIDLiaison);
		customerStripeIDPrelevement = idCustomerStripe;
	}
	
	private String idProductStripe;
	private String productName;
	private String productType = "service";
	public void creerProductStripe(ActionEvent event) {
		
//		idProductStripe = lgrStripe.creerProductStripe(productName,productType);
	}
	
	private BigDecimal montantPrelevement;
	private String customerStripeIDPrelevement;
	private String sourceStripeIDPrelevement;
	private String prelevementStripeIDPrelevement;
	public void prelevementSEPA(ActionEvent event) {
//		BigDecimal montant = new BigDecimal("9475");
//		String customerStripeID = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
//		String sourceStripeID = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
//		prelevementStripeIDPrelevement = lgrStripe.payerParPrelevementSEPA(montantPrelevement, customerStripeIDPrelevement, sourceStripeIDPrelevement);
		
	}
	
	/*
	 * https://stripe.com/docs/connect/enable-payment-acceptance-guide
	 * 
	 */
	
	public void createAccountAndLink(ActionEvent e) {
		Account a = createAccount();
		AccountLink ln = createAccountLink(a.getId());
		System.out.println("Le lien : "+ln.getUrl());
	}
	
	public Account createAccount() {
		// Set your secret key. Remember to switch to your live secret key in production!
		// See your keys here: https://dashboard.stripe.com/account/apikeys
		Stripe.apiKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
		
		AccountCreateParams params =
		  AccountCreateParams.builder()
		    .setType(AccountCreateParams.Type.STANDARD)
		    .build();

		try {
			Account account = Account.create(params);
			return account;
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public AccountLink createAccountLink(String idAccount) {
		// Set your secret key. Remember to switch to your live secret key in production!
		// See your keys here: https://dashboard.stripe.com/account/apikeys
		Stripe.apiKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

		AccountLinkCreateParams params =
		  AccountLinkCreateParams.builder()
		    .setAccount(idAccount) //acct_1032D82eZvKYlo2C
		    .setRefreshUrl("https://example.com/reauth")
		    .setReturnUrl("https://example.com/return?aa=eee&ee=uu")
		    .setType(Type.ACCOUNT_ONBOARDING)//"account_onboarding"
		    .build();

		try {
			AccountLink accountLink = AccountLink.create(params);
			return accountLink;
		} catch (StripeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getVersionProg() {
		return versionProg;
	}

	public void setVersionProg(String valeur1) {
		this.versionProg = valeur1;
	}

	public String getNomDump() {
		return nomDump;
	}

	public void setNomDump(String nomDump) {
		this.nomDump = nomDump;
	}

	public String getNomDossier() {
		return nomDossier;
	}

	public void setNomDossier(String nomDossier) {
		this.nomDossier = nomDossier;
	}

	public List<Schema> getListeBase() {
		return listeBase;
	}

	public void setListeBase(List<Schema> listeBase) {
		this.listeBase = listeBase;
	}

	public List<Schema> getListeSchema() {
		return listeSchema;
	}

	public void setListeSchema(List<Schema> listeSchema) {
		this.listeSchema = listeSchema;
	}

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public String getIbanPrelevment() {
		return ibanPrelevment;
	}

	public void setIbanPrelevment(String ibanPrelevment) {
		this.ibanPrelevment = ibanPrelevment;
	}

	public String getNomPersonneAPrelever() {
		return nomPersonneAPrelever;
	}

	public void setNomPersonneAPrelever(String nomPersonneAPrelever) {
		this.nomPersonneAPrelever = nomPersonneAPrelever;
	}

	public String getIdSourceStripe() {
		return idSourceStripe;
	}

	public void setIdSourceStripe(String idSourceStripe) {
		this.idSourceStripe = idSourceStripe;
	}

	public String getEmailCustomerStripe() {
		return emailCustomerStripe;
	}

	public void setEmailCustomerStripe(String emailCustomerStripe) {
		this.emailCustomerStripe = emailCustomerStripe;
	}

	public String getDescriptionCustomerStripe() {
		return descriptionCustomerStripe;
	}

	public void setDescriptionCustomerStripe(String descriptionCustomerStripe) {
		this.descriptionCustomerStripe = descriptionCustomerStripe;
	}

	public String getIdSourceDefautStripeCustomerStripe() {
		return idSourceDefautStripeCustomerStripe;
	}

	public void setIdSourceDefautStripeCustomerStripe(String idSourceDefautStripeCustomerStripe) {
		this.idSourceDefautStripeCustomerStripe = idSourceDefautStripeCustomerStripe;
	}

	public BigDecimal getMontantPrelevement() {
		return montantPrelevement;
	}

	public void setMontantPrelevement(BigDecimal montantPrelevement) {
		this.montantPrelevement = montantPrelevement;
	}

	public String getCustomerStripeIDPrelevement() {
		return customerStripeIDPrelevement;
	}

	public void setCustomerStripeIDPrelevement(String customerStripeIDPrelevement) {
		this.customerStripeIDPrelevement = customerStripeIDPrelevement;
	}

	public String getSourceStripeIDPrelevement() {
		return sourceStripeIDPrelevement;
	}

	public void setSourceStripeIDPrelevement(String sourceStripeIDPrelevement) {
		this.sourceStripeIDPrelevement = sourceStripeIDPrelevement;
	}

	public String getPrelevementStripeIDPrelevement() {
		return prelevementStripeIDPrelevement;
	}

	public void setPrelevementStripeIDPrelevement(String prelevementStripeIDPrelevement) {
		this.prelevementStripeIDPrelevement = prelevementStripeIDPrelevement;
	}

	public String getIdCustomerStripe() {
		return idCustomerStripe;
	}

	public void setIdCustomerStripe(String idCustomerStripe) {
		this.idCustomerStripe = idCustomerStripe;
	}
	
	public void appelWsRest(ActionEvent event) {
		String dossierTenant = "demo";
		
		String loginDeLaTableUtilisateurWs = "test";
		String passwordDeLaTableUtilisateurWs = "test";
		
		String loginDeLaTableEspaceClient = "nicolas@legrain.fr";
		String passwordDeLaTableEspaceClient = "pwdlgr";
		
		/*********************************************************************************************************************************
		 * Avec token JWT - login/password d'utilisateur Espace client
		 * *******************************************************************************************************************************
		 */
		System.out.println("************* TOKEN JWT ESPACE CLIENT **********************");
		//Création et configuration du client
		Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
		config.setVerificationSsl(false);
		config.setDevLegrain(true);
		BdgRestClient bdg = BdgRestClient.build(config);
		
		//Connexion du client pour une connexion par token JWT
		String accessToken = bdg.connexionEspaceClient();
		System.out.println("Access Token : "+accessToken);
//		bdg.refreshTokenEspaceClient();
		
		//Appel utilisant le token
		String listeFactJson = bdg.factures().listeFactureJson("CESCUD", "2000-01-01", "2030-12-31");
		System.out.println("Liste factures Espace Client :"+listeFactJson);
		
		
		
		
		/********************************************************************************************************************************
		 * Avec token JWT - login/password d'utilisateur web service (utilisateur du dossier + utilisateur uniquement pour les WS)
		 * ******************************************************************************************************************************
		 */
		System.out.println("************* TOKEN JWT DOSSIER **********************");
		BdgRestClient.accessToken=null;
		config = new Config(dossierTenant,loginDeLaTableUtilisateurWs, passwordDeLaTableUtilisateurWs);
		config.setVerificationSsl(false);
		config.setDevLegrain(true);
		bdg = BdgRestClient.build(config);
		//Connexion du client pour une connexion par token JWT
		accessToken = bdg.connexion();
		
		System.out.println("Access Token : "+accessToken);
		listeFactJson = bdg.factures().listeFactureJson("CESCUD", "2000-01-01", "2030-12-31");
		System.out.println("Liste factures Dossier : "+listeFactJson);
		
//		System.out.println("Access Token : "+accessToken);
//		String listeBonlivJson = bdg.bonliv().listeBonlivJson("CESCUD", "2000-01-01", "2030-12-31");
//		System.out.println("Liste bonliv Dossier : "+listeBonlivJson);
		
		TaArticleDTO dto = bdg.articles().findArticle(2);
		System.out.println("Code Article : "+dto.getCodeArticle());
		
		/********************************************************************************************************************************
		 * Sans token JWT - login/password d'utilisateur web service (utilisateur du dossier + utilisateur uniquement pour les WS)
		 * Test d'appel sans les tokens, (login/password en paramètre et vérifier à chaque appel)
		 * Ancienne méthode qui devrait surement peu à peu disparaitre
		 * ******************************************************************************************************************************
		 */
		System.out.println("************* ANCIENNE METHODE **********************");
		BdgRestClient.accessToken=null;
		dto = bdg.articles().findArticleAncienneMethode(dossierTenant,loginDeLaTableUtilisateurWs, passwordDeLaTableUtilisateurWs, 2);
		System.out.println("Code Article : "+dto.getCodeArticle());
		
		bdg.tiers().findTiersAncienneMethode(dossierTenant,loginDeLaTableUtilisateurWs, passwordDeLaTableUtilisateurWs, 2);
	}

	public String getCustomerStripeIDLiaison() {
		return customerStripeIDLiaison;
	}

	public void setCustomerStripeIDLiaison(String customerStripeIDLiaison) {
		this.customerStripeIDLiaison = customerStripeIDLiaison;
	}

	public String getSourceStripeIDLiaison() {
		return sourceStripeIDLiaison;
	}

	public void setSourceStripeIDLiaison(String sourceStripeIDLiaison) {
		this.sourceStripeIDLiaison = sourceStripeIDLiaison;
	}
	
}  
              