package fr.legrain.bdg.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

import fr.legrain.bdg.compteclient.ws.CompteClientFinalService;
import fr.legrain.bdg.compteclient.ws.CompteClientFinalServiceService;
import fr.legrain.bdg.compteclient.ws.EJBException;
import fr.legrain.bdg.compteclient.ws.EJBException_Exception;
import fr.legrain.bdg.compteclient.ws.ListeFournisseurResponse;
import fr.legrain.bdg.compteclient.ws.PaiementCarteBancaire;
import fr.legrain.bdg.compteclient.ws.RetourPaiementCarteBancaire;
import fr.legrain.bdg.compteclient.ws.TaFacture;
import fr.legrain.bdg.compteclient.ws.TaTiers;
import fr.legrain.bdg.compteclient.ws.TiersDossier;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;



public class CompteClientServiceClientCXF {
	
	/**
	 * 
	 JAVA_HOME=/donnees/java/jdk1.7.0_40_x64
     export JAVA_HOME
	 * apache-cxf-2.7.11/bin$ ./wsdl2java -p fr.legrain.bdg.compteclient.ws -d /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.ws/src/main/java http://localhost:8080/fr.legrain.bdg.ejb/CompteClientFinalService?wsdl
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
	private BdgProperties bdgProperties;
	
	private CompteClientFinalService compteClientFinalService;
	
	public CompteClientServiceClientCXF() throws FileNotFoundException, IOException {
		bdgProperties = new BdgProperties();
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
		        wsdlUrl = "http://"+hote+":"+port+"/fr.legrain.bdg.ejb/CompteClientFinalService?wsdl";
		    } else {  
		        //props.load(MyClass.class.getResourceAsStream("/" + propertiesFileName));
		        hote = "localhost";
				port = "8080";
				wsdlUrl = "http://"+hote+":"+port+"/fr.legrain.bdg.ejb/CompteClientFinalService?wsdl";
		    }  
		    
		    
			URL newEndpoint = new URL(wsdlUrl);
			CompteClientFinalServiceService service = new CompteClientFinalServiceService(newEndpoint);

			compteClientFinalService = service.getCompteClientFinalServicePort();
			//System.out.println("Call Web Service Operation...");

	}
	
	public CompteClientFinalService initServicePort() throws FileNotFoundException, IOException {
		initWSClient();
	    
		URL newEndpoint = new URL(wsdlUrl);

		CompteClientFinalServiceService service = new CompteClientFinalServiceService(newEndpoint);

		CompteClientFinalService servicePort = service.getCompteClientFinalServicePort();
		
		//setting requestContext so that session could be maintained through client and server
	    Map requestContext = ((BindingProvider)compteClientFinalService).getRequestContext();
	    requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY,true);
		
		return servicePort;
	}
	
	public RetourPaiementCarteBancaire payerFactureCB(String codeDossierFournisseur, String codeClientChezCeFournisseur, PaiementCarteBancaire cb, TaFacture facture) throws EJBException_Exception, FileNotFoundException, IOException {
		RetourPaiementCarteBancaire result = null;
//		try {
			
			CompteClientFinalService servicePort = initServicePort();
			//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
		
			result = servicePort.payerFactureCB(codeDossierFournisseur,codeClientChezCeFournisseur,cb,facture);
			return result;
		
//		} catch (Exception e) {
//			throw new Exception(e);
//		}
//		return result;
	}
	
	public boolean fournisseurPermetPaiementParCB(String codeDossierFournisseur, String codeClientChezCeFournisseur) throws FileNotFoundException, IOException {
		boolean result = false;
		
		CompteClientFinalService servicePort = initServicePort();
		//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
	
		result = servicePort.fournisseurPermetPaiementParCB(codeDossierFournisseur,codeClientChezCeFournisseur);
		return result;
	}
	
	public List<TiersDossier> listeFournisseur() throws RemoteException {
		try {
			
			CompteClientFinalService servicePort = initServicePort();
			//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
		
			@SuppressWarnings("unchecked")
			List<TiersDossier> l = (List<TiersDossier>) servicePort.listeFournisseur();
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean clientExisteChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur, String cleLiaisonCompteClient) {
		boolean result = false;
		try {
			
			CompteClientFinalService servicePort = initServicePort();
			//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
		
			result = servicePort.clientExisteChezFournisseur(codeDossierFournisseur,codeClientChezCeFournisseur,cleLiaisonCompteClient);
			return result;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public TaTiers infosClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur){
		try {
			
			CompteClientFinalService servicePort = initServicePort();
			//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
		
			TaTiers l = servicePort.infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeFournisseur);
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public File pdfClient(String codeDossierFournisseur, String codeClientChezCeFournisseur){
		try {
			
			CompteClientFinalService servicePort = initServicePort();
			//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
		
			byte[] l = servicePort.pdfClient(codeDossierFournisseur,codeClientChezCeFournisseur);
			
			String chemin = bdgProperties.osTempDirDossier(codeDossierFournisseur)+"/"+bdgProperties.tmpFileName("ws_tiers.pdf");
//			String chemin = "/tmp/ws.pdf";
			Path path = Paths.get(chemin);
			Files.write(path, l);
			
			return new File(chemin);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public File pdfFacture(String codeDossierFournisseur, String codeFactureChezCeFournisseur){
		try {
			
			CompteClientFinalService servicePort = initServicePort();
			//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
		
			byte[] l = servicePort.pdfFacture(codeDossierFournisseur,codeFactureChezCeFournisseur);
			
			String chemin = bdgProperties.osTempDirDossier(codeDossierFournisseur)+"/"+bdgProperties.tmpFileName("ws_facture.pdf");
//			String chemin = "/tmp/ws.pdf";
			Path path = Paths.get(chemin);
			Files.write(path, l);
			
			return new File(chemin);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur) throws RemoteException {
		try {
			
			CompteClientFinalService servicePort = initServicePort();
			//ListeFournisseurResponse.Return d = servicePort.listeFournisseur();
		
			@SuppressWarnings("unchecked")
			List<TaFacture> l = servicePort.facturesClientChezFournisseur(codeDossierFournisseur,codeClientChezCeFournisseur);
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur, Date debut, Date fin) throws RemoteException {
		try {
			
			CompteClientFinalService servicePort = initServicePort();
			
			GregorianCalendar gcDebut = new GregorianCalendar();
			GregorianCalendar gcfin = new GregorianCalendar();
			gcDebut.setTime(debut);
			gcfin.setTime(fin);
			XMLGregorianCalendar dateDebut = null;
			XMLGregorianCalendar dateFin = null;
			try {
				dateDebut = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcDebut);
				dateFin = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcfin);
			} catch (DatatypeConfigurationException e1) {
				e1.printStackTrace();
			}	
		
			@SuppressWarnings("unchecked")
			List<TaFacture> l = servicePort.facturesClientChezFournisseurDate(codeDossierFournisseur, codeClientChezCeForunisseur, dateDebut, dateFin);
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
