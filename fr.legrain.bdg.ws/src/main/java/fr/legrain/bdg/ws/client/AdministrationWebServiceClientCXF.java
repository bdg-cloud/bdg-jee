package fr.legrain.bdg.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.ws.BindingProvider;

import fr.legrain.bdg.ws.DatabaseService;
import fr.legrain.bdg.ws.DatabaseServiceService;



public class AdministrationWebServiceClientCXF {
	
	/**
	 * 
	 JAVA_HOME=/donnees/java/jdk1.7.0_40_x64
     export JAVA_HOME
	 * apache-cxf-2.7.11/bin$ ./wsdl2java -p fr.legrain.bdg.ws -d /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.ws/src/main/java http://localhost:8080/fr.legrain.bdg.ejb/DatabaseService?wsdl
	 * 
	 * http://cxf.apache.org/docs/wsdl-to-java.html
	 * 
	 * http://cxf.apache.org/docs/how-do-i-develop-a-client.html
	 * 
	 * @param codeTiers
	 * @param idProduit
	 * @return
	 * @throws RemoteException
	 * 
	 * @HandlerChain(file="handler-chain.xml")
public class ServerInfoService extends Service

	 */
	
	private String hote = "localhost";
	private String port = "8080";
	private String wsdlUrl = "";
	private boolean verifMajAuto = false;
	
	private DatabaseService databaseService;
	
	public AdministrationWebServiceClientCXF() throws FileNotFoundException, IOException {
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
		        port = props.getProperty("webservice_bdg.wsdl.port");
		        hote = props.getProperty("webservice_bdg.wsdl.host");
		        wsdlUrl = props.getProperty("webservice_bdg.wsdl.url");
		    } else {  
		        //props.load(MyClass.class.getResourceAsStream("/" + propertiesFileName));
		        hote = "localhost";
				port = "8080";
				wsdlUrl = "http://"+hote+":"+port+"/fr.legrain.bdg.ejb/DatabaseService?wsdl";
		    }  
		    
		    
			URL newEndpoint = new URL(wsdlUrl);
			DatabaseServiceService service = new DatabaseServiceService(newEndpoint);

			databaseService = service.getDatabaseServicePort();
			//System.out.println("Call Web Service Operation...");

	}
	
	public List<String> listeBdd() {
		try {
			initWSClient();
		    
			URL newEndpoint = new URL(wsdlUrl);
			DatabaseServiceService service = new DatabaseServiceService(newEndpoint);
	
			databaseService = service.getDatabaseServicePort();
			//System.out.println("Call Web Service Operation...");
			
//			 Hello proxy = new HelloService().getHelloPort();
		      //setting requestContext so that session could be maintained through client and server
		      Map requestContext = ((BindingProvider)databaseService).getRequestContext();
		      requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY,true);
	
			return databaseService.listeBdd();
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> listeFichierDump(String nomDossier, String nomBase) {
		try {
			initWSClient();
		    
			URL newEndpoint = new URL(wsdlUrl);
			DatabaseServiceService service = new DatabaseServiceService(newEndpoint);
	
			databaseService = service.getDatabaseServicePort();
			//System.out.println("Call Web Service Operation...");
			
//			 Hello proxy = new HelloService().getHelloPort();
		      //setting requestContext so that session could be maintained through client and server
		      Map requestContext = ((BindingProvider)databaseService).getRequestContext();
		      requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY,true);
	
		      return databaseService.listeFichierDump(nomDossier,nomBase);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
//	public String chargeDernierSetup(String codeTiers, String codeProduit, String versionClient) throws RemoteException {
//		try {
//			
//			initWSClient();
//		    
//			URL newEndpoint = new URL(wsdlUrl);
//
//			TaSetupServiceService service = new TaSetupServiceService(newEndpoint);
//
//			TaSetupService servicePort = service.getTaSetupServicePort();
//			System.out.println("Call Web Service Operation...");
//
//			String l = servicePort.chargeDernierSetup(codeTiers, codeProduit, versionClient);;
//			return l;
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public boolean isVerifMajAuto() {
		return verifMajAuto;
	}

	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

}
