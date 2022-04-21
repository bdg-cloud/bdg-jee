package fr.legrain.gwihostingfr.api.ws.client;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.namespace.QName;

import fr.legrain.gwihostingfr.api.ws.GandiServicePort;
import fr.legrain.gwihostingfr.api.ws.GandiServiceService;


public class GwiWebServiceClientCXF {
	
	/**
	 * http://api.gwi-hosting.fr/ws/GandiApi?wsdl
	 * 
	 * apache-cxf-2.7.11/bin$ .//wsdl2java -p fr.legrain.gwihostingfr.api.ws -d /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.gwihostingfr.api.ws/src/main/java http://api.gwi-hosting.fr/ws/GandiApi?wsdl
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
	
	private String hote = "api.gwi-hosting.fr";
	private String port = "80";
//	private String wsdlUrl = "http://api.gwi-hosting.fr/ws/GandiApi?wsdl";
	private String wsdlUrl = "http://api.gwi-hosting.fr/api/soap/gandi?wsdl";
	
	private static final String usrGandi = "hohloobeen1quaez7eis8eiBaiNgeita";
	private static final String passwordGandi = "cooBeeNgeijaerie9aibae0ohxootee5";
	
	public void initWSClient() throws FileNotFoundException, IOException {
//		String propertiesFileName = "bdg.properties";  
//
//		Properties props = new Properties();  
//		String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  
//
//		if(new File(path).exists()) { 
//			File f = new File(path);
//			props.load(new FileInputStream(f));  
//			port = props.getProperty("webservice_autorisation.wsdl.port");
//			hote = props.getProperty("webservice_autorisation.wsdl.host");
//			wsdlUrl = props.getProperty("webservice_autorisation.wsdl.url");
//		} else {  
			//props.load(MyClass.class.getResourceAsStream("/" + propertiesFileName));
			hote = "api.gwi-hosting.fr";
			port = "80";
			wsdlUrl = "http://"+hote+":"+port+"/api/soap/gandi?wsdl";
//		}  
			
	}

	public Boolean creerSousDomaine(String domaine, String sousDomaine, String ip) throws RemoteException {
		try {
			
			initWSClient();
		    
			URL newEndpoint = new URL(wsdlUrl);
			//QName qname = new QName("http://service.autorisations.legrain.fr/","TaAutorisationsServiceService"); 

//			TaAutorisationsServiceService service = new TaAutorisationsServiceService();
//			TaAutorisationsServiceService service = new TaAutorisationsServiceService(newEndpoint, qname);
			GandiServiceService service = new GandiServiceService(newEndpoint);

			GandiServicePort servicePort = service.getGandiServicePort();
			System.out.println("Call Web Service Operation...");
			
			return servicePort.registerDNSSubDomain(usrGandi, passwordGandi , domaine, sousDomaine, ip);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Boolean supprimerSousDomaine(String domaine, String sousDomaine) throws RemoteException {
		try {
			
			initWSClient();
		    
			URL newEndpoint = new URL(wsdlUrl);
			//QName qname = new QName("http://service.autorisations.legrain.fr/","TaAutorisationsServiceService"); 

//			TaAutorisationsServiceService service = new TaAutorisationsServiceService();
//			TaAutorisationsServiceService service = new TaAutorisationsServiceService(newEndpoint, qname);
			GandiServiceService service = new GandiServiceService(newEndpoint);

			GandiServicePort servicePort = service.getGandiServicePort();
			System.out.println("Call Web Service Operation...");
			
			return servicePort.deleteDNSSubdomain(usrGandi, passwordGandi , domaine, sousDomaine);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
