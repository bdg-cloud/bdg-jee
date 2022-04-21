package fr.legrain.bdg.webapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import fr.legrain.autorisations.ws.ListeModules;
import fr.legrain.autorisations.ws.Module;
import fr.legrain.autorisations.ws.client.AutorisationWebServiceClientCXF;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaAuthURLServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.general.service.remote.ITaLiaisonDossierMaitreServiceRemote;
import fr.legrain.bdg.login.service.remote.ITaUtilisateurLoginServiceRemote;
import fr.legrain.bdg.webapp.app.LeftBean;
import fr.legrain.bdg.webapp.app.MenuBean;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.ws.rest.client.BdgRestClient;
import fr.legrain.bdg.ws.rest.client.Config;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaAuthURL;
import fr.legrain.droits.model.TaRRoleUtilisateur;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.model.TaLiaisonDossierMaitre;
import fr.legrain.general.model.TaParametre;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.login.model.TaUtilisateurLogin;
import fr.legrain.moncompte.ws.client.SetupWebServiceClientCXF;

@Named
//@ViewScoped
@SessionScoped
public class Auth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1732114126590310734L;
	private String username;
	private String password;
	private String theme = "icarus-green"; //"mirage-blue-light", "metroui", "legrain", "icarus-green", "primefaces-mirage-blue-light"
	private String themeCouleur = "green"; //"green" //pour icarus seulement
	private String originalURL;
	
	private boolean ouvertureAutoDashboard = true;
	
	//private Integer sessionTimeIdle = 1800000; //milliseconde
	//Penser à changer dans le web.xml <session-config><session-timeout> avec un temps légèrement supérieur
//	private Integer sessionTimeIdle = 3500000; //milliseconde 3600000 = 60 minutes
	private Integer sessionTimeIdle = 14000000; //milliseconde 14400000 = 240 minutes
	
	private String username_logindb;

	private String loginMaint;
	private boolean versionMobile=false;

	private TaUtilisateur user;
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
	@EJB private ITaUtilisateurServiceRemote userService;
	@EJB private ITaUtilisateurLoginServiceRemote utilisateurLoginService;
	@EJB private ITaAuthURLServiceRemote authURLService;
	@EJB private ITaAutorisationsDossierServiceRemote autorisationsDossierService;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;

	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLiaisonDossierMaitreServiceRemote taLiaisonDossierMaitreService;
	
	@EJB private ITaParametreServiceRemote taParametreService;
	
	private Boolean moduleBDGARenouveller = false;
	
	private String loginDeLaTableEspaceClient = null;
	private String passwordDeLaTableEspaceClient = null;
	
	String dossierTenant = "";

	@PostConstruct
	public void init() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

		if (originalURL == null) {
			originalURL = externalContext.getRequestContextPath() + "/home.xhtml";
		} else {
			String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

			if (originalQuery != null) {
				originalURL += "?" + originalQuery;
			}
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		if(origRequest.getRequestURL().substring(1).contains("/m") || origRequest.getRequestURL().substring(1).contains("/m/")){
//			theme="icarus-green";
//			versionMobile=true;
//		};

		
	}
	
	public void initIdentifiantEspaceClient() {
		TaParametre param = taParametreService.findInstance();
		dossierTenant = param.getDossierMaitre();
		loginDeLaTableEspaceClient = null;
		passwordDeLaTableEspaceClient = null;
		List<TaLiaisonDossierMaitre> listeLiaison =  taLiaisonDossierMaitreService.selectAll();
		
		if (listeLiaison != null && !listeLiaison.isEmpty()) {
			loginDeLaTableEspaceClient = listeLiaison.get(0).getEmail();
			passwordDeLaTableEspaceClient = listeLiaison.get(0).getPassword();
			if(passwordDeLaTableEspaceClient!=null) { 
				passwordDeLaTableEspaceClient = LibCrypto.decrypt(passwordDeLaTableEspaceClient);
			}
		}
		
	}
	
	public String extractTenantFromUrl(HttpServletRequest origRequest) {
		System.out.println("dddd : "+origRequest.getServerName());
		String tenant = origRequest.getServerName().substring(0,origRequest.getServerName().lastIndexOf("."));//supprime le tld (dev.demo.promethee.biz => dev.demo.promethee)
		tenant = tenant.substring(0,tenant.lastIndexOf("."));//suprime le domaine principal (dev.demo.promethee => dev.demo)
		tenant = tenant.substring(tenant.lastIndexOf(".")+1);//conserve uniquement le premier niveau de sous domaine (dev.demo => demo)
		System.out.println("Dossier/tenant : "+tenant);
		return tenant;
	}

	//@EJB
	//private UserService userService;

	public String login() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
		try {
			//log4jLogger.info("LOGIN");

			//            request.login(username, password);  
			//            user = userService.findByCode(username);

			//http://stackoverflow.com/questions/6589138/using-http-request-login-with-jboss-jaas
			Principal userPrincipal = request.getUserPrincipal();
			if (request.getUserPrincipal() != null) {
				request.logout();
			}

			boolean siteEnMaintenance = false;
			if(ConstWeb.maintenance && !isDev(username)) {
				siteEnMaintenance = true;
			}

			if(!siteEnMaintenance) {
				
				HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				String tenant = extractTenantFromUrl(origRequest);
				tenantInfo.setTenantId(tenant);
				
				username_logindb = username+"_"+tenantInfo.getTenantId();
				System.out.println("Utilisateur login_db : "+username_logindb);
				request.login(username_logindb, password);
//				request.login(username, password);
				
				userPrincipal = request.getUserPrincipal();
				user = userService.findByCode(username);
				TaUtilisateurLogin utilisateurLogin = utilisateurLoginService.findByCode(username_logindb);
//				TaUtilisateurLogin utilisateurLogin = utilisateurLoginService.findByCode(username);

				sessionInfo.setUtilisateur(user);
				sessionInfo.setIpAddress(findUserIPAddress());
				sessionInfo.setSessionID(request.getSession(false).getId());

				System.out.println("Utilisateur "+user.getUsername()+" *** "+user.getPasswd()+" connecté avec succès ...");
				System.out.println("Utilisateur "+user.getUsername()+" ("+user.getNom()!=null?user.getNom():""+" "+user.getPrenom()!=null?user.getPrenom():""+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
				Date now = new Date();
				user.setDernierAcces(now);
				utilisateurLogin.setDernierAcces(now);
				
				user.setIpAcces(sessionInfo.getIpAddress());
				utilisateurLogin.setIpAcces(sessionInfo.getIpAddress());

				userService.enregistrerMerge(user);
				utilisateurLoginService.enregistrerMerge(utilisateurLogin);
				
				initDroitModules();

				for (TaRRoleUtilisateur r : user.getRoles()) {
					System.out.println(r.getTaRole().getRole());
				}
				
				//externalContext.getSessionMap().put("auth", this);
				externalContext.getSessionMap().put("userSession", user);
				
				
				initIdentifiantEspaceClient();
				refreshmoduleBDGARenouveller();
				
				ouvertureAutoOnglet(context);
				
				
				
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Site en cours de maintenance ...",""));
				return "/login/error.xhtml";
			}

			//          externalContext.redirect(originalURL);
		} catch (ServletException e) {
			e.printStackTrace();
			//log4jLogger.info("DENIED");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_FATAL, "Utilisateur ou mot de passe incorrects"/*e.getClass().getName()*/, e.getMessage()));
			return "/login/error.xhtml";
		} catch (FinderException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_FATAL, e.getClass().getName(), e.getMessage()));
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpServletRequest origRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		if(versionMobile){
//			return "/m/index.xhtml";
//		};
		
		return "/index.xhtml?faces-redirect=true";
	}
	
	public void refreshmoduleBDGARenouveller() {
			
		if(loginDeLaTableEspaceClient != null && passwordDeLaTableEspaceClient != null) {
			System.out.println("************* TOKEN JWT ESPACE CLIENT **********************");
			//Création et configuration du client
			Config config = new Config(dossierTenant,loginDeLaTableEspaceClient, passwordDeLaTableEspaceClient);
			config.setVerificationSsl(false);
			config.setDevLegrain(true);
			BdgRestClient bdg = BdgRestClient.build(config);
			
			//Connexion du client pour une connexion par token JWT
			String codeTiers = null;
			try {
				codeTiers = bdg.connexionEspaceClient();
				System.out.println("code tier  : "+codeTiers);
				
				List<String> codeEtats = new ArrayList<String>();
				codeEtats.add("doc_suspendu");
				codeEtats.add("doc_encours");
				
				//Appel utilisant le token
				try {
					List<TaLEcheanceDTO> listeLEcheanceRenouvellement = bdg.abonnements().listeEcheance(codeTiers, codeEtats, true);
					if(listeLEcheanceRenouvellement != null && !listeLEcheanceRenouvellement.isEmpty()) {
						moduleBDGARenouveller = true;
					}else {
						moduleBDGARenouveller = false;
					}
				} catch (Exception e) {
//					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//					FacesMessage.SEVERITY_WARN, e.getMessage(),""));
					e.printStackTrace();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}else {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
//			FacesMessage.SEVERITY_WARN, "Veuillez vous connecter a l'espace client",""));
		}
		
		

			
			
			//pour dev
			//moduleBDGARenouveller = true;

	}
	
//	public void refreshmoduleBDGARenouveller(ActionEvent actionEvent) {
//		try {
//			List<TaLEcheance> listeEch = taLEcheanceService.findAllEcheanceEnCoursOuSuspendueModuleBDG();
//			
//			if(listeEch != null) {
//				moduleBDGARenouveller = true;
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
	public void ouvertureAutoOnglet(FacesContext context) {
		//Tant que qu'aucune vue ne les a appelés les ManagedBean JSF ne son pas encore créés,
		//Il faut les "appelés" pour forcer leur création dans le contexte courant
		TabListModelBean tabsCenter = (TabListModelBean) context.getApplication().evaluateExpressionGet(context, "#{tabListModelCenterBean}", TabListModelBean.class);
		TabListModelBean tabsLeft = (TabListModelBean) context.getApplication().evaluateExpressionGet(context, "#{tabListModelLeftBean}", TabListModelBean.class);
		TabListModelBean tabsRight = (TabListModelBean) context.getApplication().evaluateExpressionGet(context, "#{tabListModelRightBean}", TabListModelBean.class);
		TabListModelBean tabsBottom = (TabListModelBean) context.getApplication().evaluateExpressionGet(context, "#{tabListModelBottomBean}", TabListModelBean.class);
		LeftBean LeftBean = (LeftBean) context.getApplication().evaluateExpressionGet(context, "#{leftBean}", LeftBean.class);
		TabViewsBean tabViews = (TabViewsBean) context.getApplication().evaluateExpressionGet(context, "#{tabViewsBean}", TabViewsBean.class);
		
		MenuBean bean = (MenuBean) context.getApplication().evaluateExpressionGet(context, "#{menuBean}", MenuBean.class);
		
		
		//ouverture automatique pour saisir les infos entreprises
		TaInfoEntreprise infos = taInfoEntrepriseService.findInstance();
		// Suite aux modification faites dans taInfoEntrepriseService.findInstance(), on ne devrait jamais passer dans la première 
		// condition et ouvrir systématiquement le tableau de bord général
		if(infos==null //pas d'infos
				|| (infos!=null // infos mais un champ obligatoire est vide
					&& (infos.getNomInfoEntreprise()==null || infos.getNomInfoEntreprise().equals("")
						|| infos.getCodexoInfoEntreprise()==null || infos.getCodexoInfoEntreprise().equals("")
						|| infos.getDatedebInfoEntreprise()==null 
						|| infos.getDatefinInfoEntreprise()==null 
						)
					)
				) {
			bean.openInfoEntreprise(null);
		} else if(ouvertureAutoDashboard) {
			//ouverture automatique du tableau de bord
			bean.openTableauDeBord(null);
		}
	}

	public void logout(ActionEvent a) throws IOException {
		logout();
		//    	//public void logout() throws IOException {
		//    	//public String logout() throws IOException {
		//            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		//            externalContext.invalidateSession();
		//            externalContext.redirect(externalContext.getRequestContextPath() + "/login/login.xhtml");
		//            //return "/login/login.xhtml";
		//           // response.sendRedirect(request.getContextPath() + "/login.xhtml");
		//            //return "/login/login.xhtml?faces-redirect=true";
	}

	public void logout() throws IOException {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();
		//System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
		System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") ** IP : "+findUserIPAddress());
		if(versionMobile)
			externalContext.redirect(externalContext.getRequestContextPath() + "/m/login/login.xhtml");
		else externalContext.redirect(externalContext.getRequestContextPath() + "/login/login.xhtml");
	}
	
	public void timeout() throws IOException {
		System.out.println("Timeout, fermeture automatique de la session");
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.invalidateSession();
		//System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") "+user.getTaEntreprise()!=null?user.getTaEntreprise().getNom():""+" ** IP : "+findUserIPAddress());
		System.out.println("Logout : User "+user.getUsername()+" ("+user.getNom()+" "+user.getPrenom()+") ** IP : "+findUserIPAddress());
//		if(versionMobile) {
//			externalContext.redirect(externalContext.getRequestContextPath() + "/m/login/login.xhtml?timeout=1");
//		}
//		else {
			externalContext.redirect(externalContext.getRequestContextPath() + "/login/login.xhtml?timeout=1");
//		}
	}

	public String findUserIPAddress() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		System.out.println("ipAddress:" + ipAddress);
		return ipAddress;
	}

	public boolean hasRole(String role) {
		//    	FacesContext context = FacesContext.getCurrentInstance();
		//    	ExternalContext externalContext = context.getExternalContext();
		//    	User user = (User) externalContext.getSessionMap().get("userSession");
		return user.hasRole(role);
	}

	public boolean isDev() {
		return user.isDev();
	}

	public boolean isDev(String username) {
		return user.isDev(username);
	}

	public boolean isDevLgr() {
		return autorisationsDossierService.isDevLgr();
	}

	public boolean isDevLgr(String username) {
		return autorisationsDossierService.isDevLgr(username);
	}
	
	public boolean isAdminDossier() {
		if(user.getAdminDossier()!=null && user.getAdminDossier()) {
			return true;
		} else {
			return false;
		}
	}
	

	static public TaUtilisateur findUserInSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (TaUtilisateur) context.getApplication().evaluateExpressionGet(context,"#{auth.user}", TaUtilisateur.class);
	}

	public List<TaAuthURL> restrictedURL() {
		//    	FacesContext context = FacesContext.getCurrentInstance();
		//    	ExternalContext externalContext = context.getExternalContext();
		//    	User user = (User) externalContext.getSessionMap().get("userSession");

		List<TaAuthURL> l = new ArrayList<TaAuthURL>();

		for (TaRRoleUtilisateur r : user.getRoles()) {
			//l.addAll(authURLService.findByRoleID(r.getTaRole().getId()));
		}

		return l;
	}
	
	public void rechercheMiseAJour() {
		try {
			
		    FacesContext context = FacesContext.getCurrentInstance();
	        String revisionSVN = context.getApplication().evaluateExpressionGet(context, "#{msg['revision_svn']}", String.class);
//	        Integer date = context.getApplication().evaluateExpressionGet(context, "#{msg['build_date_millis']}", Integer.class);
//	        System.out.println(date);
//	        System.out.println(new Date(date));
			
			SetupWebServiceClientCXF test = new SetupWebServiceClientCXF();
			if(test.isVerifMajAuto()) {
				String codeTiers = "demo";
				int idProduit = 4;
				String codeProduit = "bdg";
				
				String urlMaj = test.chargeDernierSetup(codeTiers, codeProduit, revisionSVN);
				if(urlMaj!=null) {
					System.out.println("URL de MAJ : "+urlMaj);
					
					//UtilHTTP.downloadFile(urlMaj, null);
				} else {
					System.out.println("Pas de MAJ ");
				}
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initDroitModules() {
		rechercheMiseAJour();
		AutorisationWebServiceClientCXF test = new AutorisationWebServiceClientCXF();
		String codeTiers = tenantInfo.getTenantId();
		int idProduit = 4;
		
		try {
			ListeModules lm = test.listeModulesAutorises(codeTiers, idProduit);
			fr.legrain.autorisation.xml.ListeModules listeXml = new fr.legrain.autorisation.xml.ListeModules();
			if(lm!=null) {
				System.out.println("Modules autorisés pour ce dossier : ");
				
				fr.legrain.autorisation.xml.Module moduleXml = null;
				for (Module m : lm.getModules().getModule()) {
					System.out.println(m.getId());
					
					moduleXml = new fr.legrain.autorisation.xml.Module();
					moduleXml.id = m.getId();
					moduleXml.nom = m.getNom();
					moduleXml.limite = m.getLimite();
					moduleXml.dateModule = m.getDateModule();
					moduleXml.niveau = m.getNiveau();
					listeXml.module.add(moduleXml);
				}
				System.out.println("=============================");
			}
			listeXml.accessWebservice = lm.getAccesWebservice();
			listeXml.nbPosteClient = lm.getNbPosteClient();
			listeXml.nbUtilisateur = lm.getNbUtilisateur();
			
			TaAutorisationsDossier autorisations =  autorisationsDossierService.findInstance();
			autorisations.setDateDerSynchro(new Date());
			autorisations.setFichier(listeXml.creeXmlModuleString(listeXml));
			
			autorisationsDossierService.merge(autorisations,ITaAutorisationsDossierServiceRemote.validationContext);
			
			autorisationsDossierService.majDroitModuleDossierServiceWebExterne();
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getLoginMaint() {
		return loginMaint;
	}

	public void setLoginMaint(String loginMaint) {
		this.loginMaint = loginMaint;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOriginalURL() {
		return originalURL;
	}

	public void setOriginalURL(String originalURL) {
		this.originalURL = originalURL;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public TaUtilisateur getUser() {
		return user;
	}

	public void setUser(TaUtilisateur user) {
		this.user = user;
	}

	public SessionInfo getSessionInfo() {
		return sessionInfo;
	}

	public void setSessionInfo(SessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

	public Integer getSessionTimeIdle() {
		return sessionTimeIdle;
	}

	public String getThemeCouleur() {
		return themeCouleur;
	}

	public void setThemeCouleur(String themeCouleur) {
		this.themeCouleur = themeCouleur;
	}

	public Boolean getModuleBDGARenouveller() {
		return moduleBDGARenouveller;
	}

	public void setModuleBDGARenouveller(Boolean moduleBDGARenouveller) {
		this.moduleBDGARenouveller = moduleBDGARenouveller;
	}

}