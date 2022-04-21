package fr.legrain.moncompte.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import fr.legrain.moncompte.ws.MonCompteService;
import fr.legrain.moncompte.ws.MonCompteServiceService;


public class SetupWebServiceClientCXF implements Serializable{
	
	/**
	 * 
	 * JAVA_HOME=/donnees/java/jdk1.7.0_40_x64
     * export JAVA_HOME
	 * apache-cxf-2.7.11/bin$ ./wsdl2java -p fr.legrain.moncompte.ws -d /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E43/fr.legrain.moncompte.ws/src/main/java http://localhost:8080/fr.legrain.moncompte.ejb/MonCompteServiceService?wsdl
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
	private boolean verifMajAuto = false;
	
	public SetupWebServiceClientCXF() throws FileNotFoundException, IOException {
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
		        port = props.getProperty("webservice_moncompte.wsdl.port");
		        hote = props.getProperty("webservice_moncompte.wsdl.host");
		        wsdlUrl = props.getProperty("webservice_moncompte.wsdl.url");
		        if(props.getProperty("webservice_moncompte.verif_maj_auto")!=null) {
		        	verifMajAuto = props.getProperty("webservice_moncompte.verif_maj_auto").equals("true")?true:false;
		        }
		    } else {  
		        //props.load(MyClass.class.getResourceAsStream("/" + propertiesFileName));
		        hote = "localhost";
				port = "8080";
				wsdlUrl = "http://"+hote+":"+port+"/fr.legrain.moncompte.ejb/MonCompteService?wsdl";
		    }  
	}
	
	//public String listeModulesAutorises(String codeTiers, int idProduit) throws RemoteException {
	public String chargeDernierSetup(String codeTiers, String codeProduit, String versionClient) throws RemoteException {
		try {
			
			initWSClient();
		    
			URL newEndpoint = new URL(wsdlUrl);

			MonCompteServiceService service = new MonCompteServiceService(newEndpoint);

			MonCompteService servicePort = service.getMonCompteServicePort();
			System.out.println("Call Web Service Operation...");

			String l = servicePort.chargeDernierSetup(codeTiers, codeProduit, versionClient);;
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isVerifMajAuto() {
		return verifMajAuto;
	}

}
