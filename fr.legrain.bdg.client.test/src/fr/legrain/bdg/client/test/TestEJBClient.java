package fr.legrain.bdg.client.test;


import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.EJBClientContext;

import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;

public class TestEJBClient {

//Dans le standalone.xml
//    <subsystem xmlns="urn:jboss:domain:remoting:2.0">
//	    <endpoint worker="default"/>
//	    <http-connector name="http-remoting-connector" connector-ref="default" security-realm="MyRealm"/>
//    </subsystem>
	
	  public static void main(String[] args) throws Exception {
		  
		
//		////////
//		//  REMOTE EJB (JNDI)
//		////////  
//		// le fichier "jboss-ejb-client.properties" doit être dans le classpath  
//		//-Djboss.ejb.client.properties.file.path=/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.bdg.client.test/src/fr/legrain/bdg/client/test/jboss-ejb-client.properties
//		
//	    Properties props = new Properties();
//	    
//		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
//	    
//		Context context = new InitialContext(props);
//
//		String jndiLookup = "ejb:fr.legrain.bdg.ear/fr.legrain.bdg.ejb//TaUniteService!fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote";
//		ITaUniteServiceRemote hello = (ITaUniteServiceRemote) context.lookup(jndiLookup);
//		System.out.println(hello.selectCount());
		
		/***************************************************************************************************************************/

		////////
		//  REMOTE NAMING (WILDFLY 8 et +)
		////////  
	    Properties props = new Properties();
	    	    
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		props.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080");
		props.put("jboss.naming.client.ejb.context", true);
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		props.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
		
		props.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS","JBOSS-LOCAL-USER");
		props.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS","true");
		props.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_ENABLED","false");
		props.put(Context.SECURITY_PRINCIPAL, "demo_demo");
		props.put(Context.SECURITY_CREDENTIALS, "demo");
	    
		Context context = new InitialContext(props);

		String jndiLookup = "/fr.legrain.bdg.ear/fr.legrain.bdg.ejb//TaUniteService!fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote";
		ITaUniteServiceRemote hello = (ITaUniteServiceRemote) context.lookup(jndiLookup);
		System.out.println(hello.selectCount());
	  
	}
	
}
