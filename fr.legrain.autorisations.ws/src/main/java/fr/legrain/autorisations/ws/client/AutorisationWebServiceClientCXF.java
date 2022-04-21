package fr.legrain.autorisations.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.namespace.QName;

import fr.legrain.autorisations.ws.ListeModules;
import fr.legrain.autorisations.ws.TaAutorisations;
import fr.legrain.autorisations.ws.TaAutorisationsService;
import fr.legrain.autorisations.ws.TaAutorisationsServiceService;


public class AutorisationWebServiceClientCXF implements Serializable{
	
	/**
	 * apache-cxf-2.7.11/bin$ ./wsdl2java -p fr.legrain.autorisations.ws -d /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.autorisations.ws/src/main/java http://localhost:8080/fr.legrain.autorisations.ejb/TaAutorisationsService?wsdl
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
		        port = props.getProperty("webservice_autorisation.wsdl.port");
		        hote = props.getProperty("webservice_autorisation.wsdl.host");
		        wsdlUrl = props.getProperty("webservice_autorisation.wsdl.url");
		    } else {  
		        //props.load(MyClass.class.getResourceAsStream("/" + propertiesFileName));
		        hote = "localhost";
				port = "8080";
				wsdlUrl = "http://"+hote+":"+port+"/fr.legrain.autorisations.ejb/TaAutorisationsService?wsdl";
		    }  
	}
	
	public ListeModules listeModulesAutorises(String codeTiers, int idProduit) throws RemoteException {
		try {
			
			initWSClient();
		    
			URL newEndpoint = new URL(wsdlUrl);
			//QName qname = new QName("http://service.autorisations.legrain.fr/","TaAutorisationsServiceService"); 

//			TaAutorisationsServiceService service = new TaAutorisationsServiceService();
//			TaAutorisationsServiceService service = new TaAutorisationsServiceService(newEndpoint, qname);
			TaAutorisationsServiceService service = new TaAutorisationsServiceService(newEndpoint);

			TaAutorisationsService servicePort = service.getTaAutorisationsServicePort();
			System.out.println("Call Web Service Operation...");

			ListeModules l = servicePort.findByDossierTypeProduit(codeTiers, idProduit);
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void createUpdateDroits(String codeTiers, int idProduit, fr.legrain.autorisation.xml.ListeModules modules) throws RemoteException {
		try {
			
			initWSClient();
		    
			URL newEndpoint = new URL(wsdlUrl);
			TaAutorisationsServiceService service = new TaAutorisationsServiceService(newEndpoint);

			TaAutorisationsService servicePort = service.getTaAutorisationsServicePort();
			System.out.println("Call Web Service Operation...");
			
			TaAutorisations autorisation = servicePort.findByCode(codeTiers);
			fr.legrain.autorisation.xml.ListeModules liste =  new fr.legrain.autorisation.xml.ListeModules();
			if(autorisation!=null){
				//liste = liste.recupModulesXml(autorisation.getModules());
				System.out.println("Autorisation récupérées");
			} else {
				System.out.println("Création d'autorisation");
				autorisation = new TaAutorisations(); 
				autorisation.setCodeDossier(codeTiers);
				//autorisation.setTaTypeProduit(idProduit);
			}
			autorisation.setModules(liste.creeXmlModuleString(modules));
			autorisation = servicePort.merge(autorisation);
			System.out.println("Nouvelle autorisation enregistrées ");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
