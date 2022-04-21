package fr.legrain.moncompte.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import fr.legrain.gandi.api.ws.GandiService;
import fr.legrain.gandi.api.ws.GandiServiceService;



public class GandiAPIWebServiceClientCXF implements Serializable{
	
	/**
	 * 
	 * JAVA_HOME=/donnees/java/jdk1.7.0_40_x64
     * export JAVA_HOME
	 * apache-cxf-2.7.11/bin$ ./wsdl2java -p fr.legrain.gandi.api.ws -d /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.moncompte.ws/src/main/java http://localhost:8080/fr.legrain.moncompte.ejb/GandiService?wsdl
	 * 
	 * 
	 * http://cxf.apache.org/docs/wsdl-to-java.html
	 * 
	 * http://cxf.apache.org/docs/how-do-i-develop-a-client.html
	 * 
	 * @param codeTiers
	 * @param idProduit
	 * @return
	 * @throws RemoteException
	 */
	
	private String hote = "localhost";
	private String port = "8080";
	private String wsdlUrl = "";
//	private boolean verifMajAuto = false;
	
	private static String SECURITY_KEY = "Thai8Saisothoo3m";
	
	public GandiAPIWebServiceClientCXF() throws FileNotFoundException, IOException {
		initWSClient();
	}
	
	public void initWSClient() throws FileNotFoundException, IOException {
		 String propertiesFileName = "bdg.properties";  
	      
		    Properties props = new Properties();  
//		    String path = System.getProperty("jboss.domain.config.dir")+"/"+propertiesFileName;
		    String path = null;
		    if(System.getProperty("mode.domaine")!=null &&
					System.getProperty("mode.domaine").equals("true"))
				path = System.getProperty("jboss.domain.config.dir")+"/"+propertiesFileName;
			else 
				path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;
		      
		    if(new File(path).exists()) { 
		    	File f = new File(path);
		        props.load(new FileInputStream(f));  
		        port = props.getProperty("webservice_moncompte.gandi.wsdl.port");
		        hote = props.getProperty("webservice_moncompte.gandi.wsdl.host");
		        wsdlUrl = props.getProperty("webservice_moncompte.gandi.wsdl.url");
//		        if(props.getProperty("webservice_moncompte.verif_maj_auto")!=null) {
//		        	verifMajAuto = props.getProperty("webservice_moncompte.verif_maj_auto").equals("true")?true:false;
//		        }
		    } else {  
		        //props.load(MyClass.class.getResourceAsStream("/" + propertiesFileName));
		        hote = "localhost";
				port = "8080";
				wsdlUrl = "http://"+hote+":"+port+"/fr.legrain.moncompte.ejb/GandiService?wsdl";
		    }  
	}
	
	public GandiService initServicePort() throws FileNotFoundException, IOException {
		initWSClient();
	    
		URL newEndpoint = new URL(wsdlUrl);

		GandiServiceService service = new GandiServiceService(newEndpoint);

		GandiService servicePort = service.getGandiServicePort();
		
		return servicePort;
	}
	
	public boolean registerDNSSubDomain(String domain, String subdomain, String ip) throws RemoteException {
		try {
			
			GandiService servicePort = initServicePort();

			boolean r = servicePort.registerDNSSubDomain(SECURITY_KEY, domain, subdomain, ip);
			return r;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteDNSSubdomain(String domain, String subdomain) throws RemoteException {
		try {
			
			GandiService servicePort = initServicePort();

			boolean r = servicePort.deleteDNSSubdomain(SECURITY_KEY, domain, subdomain);
			return r;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
