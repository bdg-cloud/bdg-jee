package fr.legrain.paiement.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;

/**
 * @author nicolas
 */
@Stateless
public class LgrEnvironnementServeur {
	
	private boolean modeTestPaiementDossier = false;
	private boolean modeTestPaiementProgramme = false;
	
	private boolean modeTestEmailDossier = false;
	private boolean modeTestEmailProgramme = false;
	
	private boolean modeTestOAuth = false;
	
	private boolean serveurProd = false;
	private boolean serveurTestProd = false;
	private boolean serveurPProd = false;
	private boolean serveurDevInternet = false;
	private boolean serveurDevLocal = false;
	
	private String ip = null;
	private String env = null;
		
	private static final String ENV_DEV_LOCAL = "dev_local";
	private static final String ENV_DEV_INTERNET = "dev_internet"; //serveur de test DEV-internet devbdg.work
	private static final String ENV_TEST_PRE_PROD_INTERNET = "test_pre_prod_internet"; //serveur de test PPROD-internet pprodbdg.work
	private static final String ENV_TEST_PROD_INTERNET = "test_prod_internet"; //serveur de test PROD-internet testprodbdg.work
	private static final String ENV_PRODUCTION_INTERNET = "prod_internet"; //serveur de PRODUCTION bdg.cloud
	
	@PostConstruct
	private void init() {
		try {			
			Properties props = new Properties(); 
			String propertiesFileName = "bdg.properties";  
//			String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  
			
			String path = null;
			if(path==null) {
				if(System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY)!=null &&
						System.getProperty(BdgProperties.KEY_MODE_DOMAINE_WILDFLY).equals("true"))
					path = System.getProperty(BdgProperties.KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+propertiesFileName;
				else 
					path = System.getProperty(BdgProperties.KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+propertiesFileName;
			}
	
			if(new File(path).exists()) { 
				File f = new File(path);
				props.load(new FileInputStream(f));  
				ip = props.getProperty(BdgProperties.KEY_IP);
				env = props.getProperty(BdgProperties.KEY_TYPE_ENVIRONEMENT);
			}
			
			if(env!=null && !env.equals("") 
					&& !env.equals(ENV_DEV_LOCAL)
					&& !env.equals(ENV_DEV_INTERNET) 
					&& !env.equals(ENV_TEST_PRE_PROD_INTERNET) 
					&& !env.equals(ENV_TEST_PROD_INTERNET)
					) { //on est à prioris bien sur internet sur le serveur de production et pas sur un poste de développement ou un serveur de test
				
				serveurProd = true;
				
			} else /*if(ip.equals(IP_PRODUCTION_INTERNET))*/{	
				modeTestPaiementDossier = true;
				modeTestPaiementProgramme = true;
				
				modeTestEmailDossier = true;
				modeTestEmailProgramme = true;
				
				modeTestOAuth = true;
				
				if(env==null || env.equals(ENV_DEV_LOCAL) ) {
					serveurDevLocal = true;
				} else if(env.equals(ENV_DEV_INTERNET) ) {
					serveurDevInternet =  true;
				} else if(env.equals(ENV_TEST_PRE_PROD_INTERNET) ) {
					serveurPProd = true;
				} else if(env.equals(ENV_TEST_PROD_INTERNET) ) {
					serveurTestProd = true;
				}
			}

			//config basée sur l'IP, non valable pour dans un environnement server, remplacer par le code au dessus avec une propriété dans bdg.properties
			//TODO code a supprimer si tout à l'air de bien fonctionner avec la propriété dans les cluster et aussi sans cluster
//			if(ip!=null && !ip.equals("") 
//					&& !ip.equals(IP_LOCALE_DEVELOPPEMENT)
//					&& !ip.equals(IP_DEV_INTERNET) 
//					&& !ip.equals(IP_TEST_PPROD_INTERNET) 
//					&& !ip.equals(IP_TEST_PROD_INTERNET)
//					) { //on est à prioris bien sur internet sur le serveur de production et pas sur un poste de développement ou un serveur de test
//				
//				serveurProd = true;
//				
//			} else /*if(ip.equals(IP_PRODUCTION_INTERNET))*/{	
//				modeTestPaiementDossier = true;
//				modeTestPaiementProgramme = true;
//				
//				modeTestEmailDossier = true;
//				modeTestEmailProgramme = true;
//				
//				modeTestOAuth = true;
//				
//				if(ip.equals(IP_LOCALE_DEVELOPPEMENT) ) {
//					serveurDevLocal = true;
//				} else if(ip.equals(IP_DEV_INTERNET) ) {
//					serveurDevInternet =  true;
//				} else if(ip.equals(IP_TEST_PPROD_INTERNET) ) {
//					serveurPProd = true;
//				} else if(ip.equals(IP_TEST_PROD_INTERNET) ) {
//					serveurTestProd = true;
//				}
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	} 
	
	public String emailDefautServeur() {
		String fromEmail = null;
		if(env==null || env.equals("") 
				||  env.equals(ENV_DEV_LOCAL)) {
			fromEmail = "legrain@legrain.biz";
		} else if(env.equals(ENV_DEV_INTERNET)) {
			fromEmail = "noreply@devbdg.work";
		} else if(env.equals(ENV_TEST_PRE_PROD_INTERNET)) {
			fromEmail = "noreply@pprodbdg.work";
		} else if(env.equals(ENV_TEST_PROD_INTERNET)) {
			fromEmail = "noreply@testprodbdg.work";
		} else if(env.equals(ENV_PRODUCTION_INTERNET)) {
			fromEmail = "noreply@bdg.cloud";
		}
		
		return fromEmail;
	}

	public boolean isModeTestPaiementDossier() {
		return modeTestPaiementDossier;
	}

	public boolean isModeTestPaiementProgramme() {
		return modeTestPaiementProgramme;
	}

	public boolean isModeTestEmailDossier() {
		return modeTestEmailDossier;
	}

	public boolean isModeTestEmailProgramme() {
		return modeTestEmailProgramme;
	}

	public boolean isModeTestOAuth() {
		return modeTestOAuth;
	}

	public void setModeTestOAuth(boolean modeTestOAuth) {
		this.modeTestOAuth = modeTestOAuth;
	}

	public boolean isServeurProd() {
		return serveurProd;
	}

	public void setServeurProd(boolean serveurProd) {
		this.serveurProd = serveurProd;
	}

	public boolean isServeurTestProd() {
		return serveurTestProd;
	}

	public void setServeurTestProd(boolean serveurTestProd) {
		this.serveurTestProd = serveurTestProd;
	}

	public boolean isServeurPProd() {
		return serveurPProd;
	}

	public void setServeurPProd(boolean serveurPProd) {
		this.serveurPProd = serveurPProd;
	}

	public boolean isServeurDevInternet() {
		return serveurDevInternet;
	}

	public void setServeurDevInternet(boolean serveurDevInternet) {
		this.serveurDevInternet = serveurDevInternet;
	}

	public boolean isServeurDevLocal() {
		return serveurDevLocal;
	}

	public void setServeurDevLocal(boolean serveurDevLocal) {
		this.serveurDevLocal = serveurDevLocal;
	}
	
}
