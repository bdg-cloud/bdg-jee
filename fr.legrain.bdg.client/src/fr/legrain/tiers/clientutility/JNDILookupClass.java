package fr.legrain.tiers.clientutility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginContext;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import org.jboss.security.auth.callback.AppCallbackHandler;

import fr.legrain.bdg.client.Activator;
import fr.legrain.bdg.client.preferences.PreferenceConstants;

 
public class JNDILookupClass {
 
	/*
	 * Le fichier jboss-ejb-client.properties doit être dans le classpath,
	 * la séparation des classpath des plugins eclipse peut poser des problèmes
	 *  -Djboss.ejb.client.properties.file.path=/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_10a_E4/fr.legrain.bdg.client/src/fr/legrain/tiers/clientutility/jboss-ejb-client.properties
	 */
    private static Context initialContext;
 
    private static final String PKG_INTERFACES = "org.jboss.ejb.client.naming";
    
    private static final boolean UTILISER_EJB_CLIENT = true; // si vrai utilise les API EJB client de JBOSS, si faux utilise les appel JNDI standard (remote-naming)
 
    public static Context getInitialContext() throws NamingException {
    	
    	String serveur = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SERVEUR); //localhost
    	String port = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SERVEUR_PORT); //8080
    	String login = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SERVEUR_LOGIN); //demo => demo_demo (login_dossier)
    	String password = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SERVEUR_PASSWORD); //demo
    	String dossier = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.SERVEUR_DOSSIER); //demo
    	
    	try {
    		Context initialContext = null;
//    		MyJAASCallbackHandler callbackHandler = new MyJAASCallbackHandler();
//    		//MyJAASCallbackHandler.setCredential("nicolas", "lgrXXXXXXX"); //admin
//    		MyJAASCallbackHandler.setCredential("demo_demo", "demo"); //admin
////    		MyJAASCallbackHandler.setCredential("demo_demo", "KpdRbDVLaISM29j1SiJqClWyHtE44getbFy7nACqWXXXXXXXXo="); //admin
//    		//callbackHandler.setCredential("legrain", "lgr8"); //faux
//    		//callbackHandler.setCredential("legrain", "lgr"); //utilisateur
//    		LoginContext loginContext = null;
//    		
//    		URL urlReportFile;
//    		urlReportFile = FileLocator.find(Activator.getDefault().getBundle(),new Path("META-INF/auth.conf"),null);
//    		String pathFile = null;
//    		if(urlReportFile!=null){
//    			try {
//    				urlReportFile = FileLocator.toFileURL(urlReportFile);
//    				
//    				URI uriReportFile = new URI("file", urlReportFile.getAuthority(),
//    						urlReportFile.getPath(), urlReportFile.getQuery(), null);
//    				File reportFileEdition = new File(uriReportFile);
//    				Context ctx =
//    				pathFile = reportFileEdition.getAbsolutePath();
//    			} catch (Exception e) {
//    				e.printStackTrace();
//    			}
//    		}
//    		System.out.println(pathFile);
//    		System.setProperty("java.security.auth.login.config", pathFile);
//
//    		loginContext = new LoginContext ("name_from_auth_conf", callbackHandler);
//    		loginContext.login();
//    		
//    		for(Principal p : loginContext.getSubject().getPrincipals()) {
//    			System.out.println(p.getName());
//    		}
    		
//    		AppCallbackHandler callbackHandler = new AppCallbackHandler("demo_demo", "demo".toCharArray());
//            LoginContext loginContext = new LoginContext("name_from_auth_conf", callbackHandler);
//            loginContext.login();
    		
    		
    		//http://blog.jonasbandi.net/2013/08/jboss-remote-ejb-invocation-unexpected.html
    		//https://gist.github.com/jbandi/6287518#file-remoteejbclient-java
    		if(UTILISER_EJB_CLIENT) { //jboss-ejb-client.properties
    			 if (initialContext == null) {
    		        	
    		        	//https://developer.jboss.org/thread/255911?tstart=0
    		        	
    		        	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    		        	// EJB Client - API propriétaire JBoss/Wildfly, optimisé et obligatoire pour Interceptor, cluster et loadbalancing
 		        		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    		        	Properties ejbCliProperties = new Properties();
    		        	ejbCliProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED","false");
    		        	ejbCliProperties.put("remote.connections","default");
    		        	ejbCliProperties.put(Context.URL_PKG_PREFIXES, PKG_INTERFACES);
    		        	ejbCliProperties.put("remote.connection.default.host",serveur);
    		        	ejbCliProperties.put("remote.connection.default.port",port);
    		        	ejbCliProperties.put("remote.connection.default.username",login+"_"+dossier);
    		        	ejbCliProperties.put("remote.connection.default.password",password);
    		        	ejbCliProperties.put("endpoint.name","client-endpoint");
    		        	ejbCliProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS","true");
    		        	ejbCliProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");
    		        	ejbCliProperties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT","false");
    		        	
    		        	initialContext = new InitialContext(ejbCliProperties);
    		        	
    		        	final EJBClientConfiguration ejbClientConfiguration = new PropertiesBasedEJBClientConfiguration(ejbCliProperties);
    	                final ConfigBasedEJBClientContextSelector selector = new ConfigBasedEJBClientContextSelector(ejbClientConfiguration);
    	                EJBClientContext.setSelector(selector);
    	                EJBClientContext.getCurrent().registerInterceptor(0, new ClientTenantInterceptor());
    		
    			 } 
    		} else { //jndi.properties
    			
        		/////////////////////////////////////////
                // Lookup standard JNDI : remote-naming
        		/////////////////////////////////////////
    			
    			if (initialContext == null) {
	        		Properties jndiProperties = new Properties();
	        		jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
	        		jndiProperties.put(Context.PROVIDER_URL, "http-remoting://"+serveur+":"+port);
	        		jndiProperties.put("jboss.naming.client.ejb.context", true);
	        		jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
	        		jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
	        		
	        		jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");
	        		jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS","true");
	        		jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_ENABLED","false");
	        		jndiProperties.put(Context.SECURITY_PRINCIPAL, login+"_"+dossier);
	        		jndiProperties.put(Context.SECURITY_CREDENTIALS, password);
	    			
	    			initialContext = new InitialContext(jndiProperties);
    			}
    		}	
    		
            return initialContext;
		} catch(Exception e) {
    		e.printStackTrace();
    	}
        return initialContext;
    }
}
