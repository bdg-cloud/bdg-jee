package fr.legrain.bdg.lib.server.osgi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class BdgProperties implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1904176953114944475L;

	private static final String propertiesFileName = "bdg.properties";  
	
	private Properties props = new Properties();  
	
	public static final String KEY_JDBC_USER_NAME = "jdbc.username";
	public static final String KEY_JDBC_PASSWORD = "jdbc.password";
	public static final String KEY_JDBC_URL = "jdbc.url";
	public static final String KEY_JDBC_DRIVER = "jdbc.driver";
	
	public static final String KEY_BDG_FILESYSTEM_PATH = "bdg.filesystem.path";
	
	public static final String KEY_DEFAULT_TENANT = "defaultTenant";
	
	public static final String KEY_PREFIXE_SOUS_DOMAINE = "prefixe_sous_domaine";
	public static final String KEY_NOM_DOMAINE = "nom_domaine";
	
	public static final String KEY_IP = "ip";
	public static final String KEY_PORT_HTTP = "port_http";
	public static final String KEY_PORT_HTTPS = "port_https";
	
	public static final String KEY_WS_MONCOMPTE_VERIF_MAJ_AUTO = "webservice_moncompte.verif_maj_auto";
	
	public static final String KEY_WS_AUTORISATION_PORT = "webservice_autorisation.wsdl.port";
	public static final String KEY_WS_AUTORISATION_HOST = "webservice_autorisation.wsdl.host";
	public static final String KEY_WS_AUTORISATION_URL = "webservice_autorisation.wsdl.url";
	
	public static final String KEY_WS_MONCOMPTE_PORT = "webservice_moncompte.wsdl.port";
	public static final String KEY_WS_MONCOMPTE_HOST = "webservice_moncompte.wsdl.host";
	public static final String KEY_WS_MONCOMPTE_URL = "webservice_moncompte.wsdl.url";
	
	public static final String KEY_WS_MONCOMPTE_GANDI_PORT = "webservice_moncompte.gandi.wsdl.port";
	public static final String KEY_WS_MONCOMPTE_GANDI_HOST = "webservice_moncompte.gandi.wsdl.host";
	public static final String KEY_WS_MONCOMPTE_GANDI_URL = "webservice_moncompte.gandi.wsdl.url";
	
	public static final String KEY_WS_BDG_PORT = "webservice_bdg.wsdl.port";
	public static final String KEY_WS_BDG_HOST = "webservice_bdg.wsdl.host";
	public static final String KEY_WS_BDG_URL = "webservice_bdg.wsdl.url";
	
	public static final String KEY_SMTP_HOSTNAME = "smtp.hostname";
	public static final String KEY_SMTP_PORT = "smtp.port";
	public static final String KEY_SMTP_SSL = "smtp.ssl";
	public static final String KEY_SMTP_LOGIN = "smtp.login";
	public static final String KEY_SMTP_PASSWORD = "smtp.password";
	
	public static final String KEY_LOGIN_LGR = "lgr.login";
	public static final String KEY_PASSWORD_LGR = "lgr.password";
	
	public static final String KEY_JBOSS_SERVER_CONFIG_DIR = "jboss.server.config.dir";
	public static final String KEY_JBOSS_DOMAIN_CONFIG_DIR = "jboss.domain.config.dir";
	
	public static final String KEY_MODE_DOMAINE_WILDFLY = "mode.domaine";
	
	public static final String KEY_TYPE_ENVIRONEMENT = "type_environnement";

	public BdgProperties() {
		this(null);
	}
	
	public BdgProperties(String cheminFichierProperties) {
		try {
	
			String path = cheminFichierProperties;
			if(cheminFichierProperties==null) {
				if(System.getProperty(KEY_MODE_DOMAINE_WILDFLY)!=null &&
						System.getProperty(KEY_MODE_DOMAINE_WILDFLY).equals("true"))
					path = System.getProperty(KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+propertiesFileName;
				else 
					path = System.getProperty(KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+propertiesFileName;
			}
	
			if(new File(path).exists()) { 
				File f = new File(path);
				props.load(new FileInputStream(f));  
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public InputStream getConfigFileInputStream(String fileName) throws FileNotFoundException, IOException {
		if(fileName==null) {
			if(System.getProperty(KEY_MODE_DOMAINE_WILDFLY)!=null &&
					System.getProperty(KEY_MODE_DOMAINE_WILDFLY).equals("true"))
				fileName = System.getProperty(KEY_JBOSS_DOMAIN_CONFIG_DIR)+"/"+fileName;
			else 
				fileName = System.getProperty(KEY_JBOSS_SERVER_CONFIG_DIR)+"/"+fileName;
		}
		try(FileInputStream fis = new FileInputStream(fileName)) {
		  return fis;
		}
	}
	
	public String getProperty(String key) {
		return getProperty(key, null);
	}
	
	public String getProperty(String key, String def) {
		if(props!=null) {
			return props.getProperty(key,def);
		} else {
			return def;
		}
	}
	
	public String urlDemo() {
		return url("demo",false);
	}
	
	public String urlDemoHttps() {
		return url("demo",true);
	}
	
	public String url(String nomDossier) {
		return url(nomDossier,false);
	}
	
	public String urlOAuth(boolean https) {
		String url = "";
		if(https) {
			url += "https://";
		} else {
			url += "http://";
		}
		
		url += getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+"oauth."+getProperty(BdgProperties.KEY_NOM_DOMAINE);
		
		if(https) {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTPS,"")+"";
		} else {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTP,"")+"";
		}
		
		return url;
	}
	
	public String url(String nomDossier, boolean https) {
		String url = "";
		if(https) {
			url += "https://";
		} else {
			url += "http://";
		}
		
		url += getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+nomDossier+"."+getProperty(BdgProperties.KEY_NOM_DOMAINE);
		
		if(https) {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTPS,"")+"";
		} else {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTP,"")+"";
		}
		
		return url;
	}
	
	public String urlEspaceClient(String nomDossier, boolean https) {
		String url = "";
		if(https) {
			url += "https://";
		} else {
			url += "http://";
		}
		
		url += getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+nomDossier+".espace-client."+getProperty(BdgProperties.KEY_NOM_DOMAINE);
		
		if(https) {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTPS,"")+"";
		} else {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTP,"")+"";
		}
		
		return url;
	}
	
	public String urlApiGlobal(boolean https) {
		String url = "";
		if(https) {
			url += "https://";
		} else {
			url += "http://";
		}
		
		String prefixeSousDomaine = getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"");
		if(!prefixeSousDomaine.trim().equals("") && !prefixeSousDomaine.endsWith(".")) {
			prefixeSousDomaine += ".";
		}
		url += prefixeSousDomaine+"api."+getProperty(BdgProperties.KEY_NOM_DOMAINE);
		
		if(https) {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTPS,"")+"";
		} else {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTP,"")+"";
		}
		
		return url;
	}
	
	public String urlApiDossier(String nomDossier, boolean https) {
		String url = "";
		if(https) {
			url += "https://";
		} else {
			url += "http://";
		}
		
		url += getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+nomDossier+"."+getProperty(BdgProperties.KEY_NOM_DOMAINE);
		
		if(https) {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTPS,"")+"";
		} else {
			url += ":"+getProperty(BdgProperties.KEY_PORT_HTTP,"")+"";
		}
		
		return url;
	}
	
	public String osTempDir() {
		return osTempDir(false);
	}
	
	public String osTempDir(boolean fileProtocol) {
		String osTempDir = System.getProperty("java.io.tmpdir")+"/bdg";
		File f = new File(osTempDir);
		if(!f.exists()) {
			f.mkdirs();
		}
		if(fileProtocol) {
			osTempDir = osTempDir.replaceAll("\\", "/");
			osTempDir = osTempDir.replaceAll("//", "/");
			osTempDir = "file:///"+osTempDir;
		}
		return osTempDir;
	}
	
	public String osTempDirDossier(String codeDossier) {
		return osTempDirDossier(codeDossier,false);
	}
	
	public String osTempDirDossier(String codeDossier,boolean fileProtocol) {
		String osTempDir = osTempDir(fileProtocol);
		String osTempDirDossier = osTempDir+"/"+codeDossier;
		File f = new File(osTempDirDossier);
		if(!f.exists()) {
			f.mkdirs();
		}
		return osTempDirDossier;
	}
	
	public String tmpFileNameInterne(String fileName) {
		return tmpFileName(null,null,fileName,true);
	}
	
	public String tmpFileName(String fileName) {
		return tmpFileName(null,fileName);
	}
	
	public String tmpFileName(String fileName, String codeFacture) {
		return tmpFileName(codeFacture,fileName,null,false);
	}
	
	public String tmpFileName(String codeFacture, String codeTiers, String fileName) {
		return tmpFileName(codeFacture,codeTiers,fileName,false);
	}
	
	public String tmpFileName(String codeFacture, String codeTiers, String fileName,boolean utilisationInterne) {
		Date d = new Date();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_ms").format(d);
		if(utilisationInterne) {
			timeStamp = timeStamp+d.getTime();
		}
		String tmpFileName = timeStamp
				+(codeFacture!=null?"_"+codeFacture:"")
				+(codeTiers!=null?"-codeTiers-"+codeTiers:"")
				+(fileName!=null?"-fileName-"+fileName:"");
		
		return tmpFileName;
	}
}
