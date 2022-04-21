package fr.legrain.moncompte.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.moncompte.ws.MonCompteService;
import fr.legrain.moncompte.ws.MonCompteServiceService;
import fr.legrain.moncompte.ws.TaCategoriePro;
import fr.legrain.moncompte.ws.TaCgPartenaire;
import fr.legrain.moncompte.ws.TaCgv;
import fr.legrain.moncompte.ws.TaClient;
import fr.legrain.moncompte.ws.TaCommission;
import fr.legrain.moncompte.ws.TaDossier;
import fr.legrain.moncompte.ws.TaPanier;
import fr.legrain.moncompte.ws.TaPartenaire;
import fr.legrain.moncompte.ws.TaPrixParUtilisateur;
import fr.legrain.moncompte.ws.TaProduit;
import fr.legrain.moncompte.ws.TaTNiveau;
import fr.legrain.moncompte.ws.TaTypePaiement;
import fr.legrain.moncompte.ws.TaTypePartenaire;



public class MonCompteWebServiceClientCXF implements Serializable{
	
	/**
	 * 
	 JAVA_HOME=/donnees/java/jdk1.7.0_40_x64
     export JAVA_HOME
	 * apache-cxf-2.7.11/bin$ ./wsdl2java -p fr.legrain.moncompte.ws -d /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.moncompte.ws/src/main/java -b /donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.moncompte.model.osgi/src/fr/legrain/moncompte/model/binding.xml http://localhost:8080/fr.legrain.moncompte.ejb/MonCompteService?wsdl
	 * 
	 * http://stackoverflow.com/questions/14844691/how-can-i-get-moxy-to-assign-a-parent-when-unmarshalling-a-child-with-an-xmlinve
	 * 
	 * Le fichier "jaxb.properties" dans le package des entités d'origine "fr.legrain.moncompte.model" qui indique qu'il faut utilisé MOXy d'EclipseLink pour
	 * le mapping JAXB peut poser problème lors de l'utilisation de "wsdl2java" ==> le déplacer le temps de la généraion du client WS
	 * 
	 * Dans la classe TaSetup générée : 
	       @XmlInverseReference(mappedBy="listeSetup")
           protected TaProduit taProduit;
     *     
     * Dans la classe TaAutorisation générée :
           @XmlInverseReference(mappedBy="listeAutorisation")
           protected TaDossier taDossier;
	 * 
	 * Dans la classe TaLignePanier générée :
           @XmlInverseReference(mappedBy="lignes")
           protected TaPanier taPanier;
     *     
     * Dans la classe TaDossier générée :
      	   @XmlElement
      	   @XmlInverseReference(mappedBy="listeDossier")
      	   protected TaClient taClient;
     *
     * Dans la classe TaLigneCommission générée :
           @XmlInverseReference(mappedBy="lignesCommission")
           protected TaCommission taCommission;
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
	
	public MonCompteWebServiceClientCXF() throws FileNotFoundException, IOException {
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
//		        if(props.getProperty("webservice_moncompte.verif_maj_auto")!=null) {
//		        	verifMajAuto = props.getProperty("webservice_moncompte.verif_maj_auto").equals("true")?true:false;
//		        }
		    } else {  
		        //props.load(MyClass.class.getResourceAsStream("/" + propertiesFileName));
		        hote = "localhost";
				port = "8080";
				wsdlUrl = "http://"+hote+":"+port+"/fr.legrain.moncompte.ejb/MonCompteService?wsdl";
		    }  
	}
	
	public MonCompteService initServicePort() throws FileNotFoundException, IOException {
		initWSClient();
	    
		URL newEndpoint = new URL(wsdlUrl);

		MonCompteServiceService service = new MonCompteServiceService(newEndpoint);

		MonCompteService servicePort = service.getMonCompteServicePort();
		
		return servicePort;
	}
	
	//public String listeModulesAutorises(String codeTiers, int idProduit) throws RemoteException {
	public List<TaProduit> listeProduit(String codeTiers, String codeProduit, String versionClient) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			List<TaProduit> l = servicePort.selectAllProduit(null);
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaProduit> listeProduitCategoriePro(int idCategoriePro) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			List<TaProduit> l = servicePort.selectAllProduitCategoriePro(idCategoriePro);
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaClient mergeClient(TaClient c) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			c = servicePort.mergeClient(c);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaPanier mergePanier(TaPanier c) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			c = servicePort.mergePanier(c);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaPanier findPanier(int idPanier) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaPanier c = servicePort.findPanier(idPanier);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean dossierExiste(String codeDossier) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.dossierExiste(codeDossier);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String genereCodeClient(String nomClient) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.genereCodeClient(nomClient);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean utilisateurExiste(String email) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.utilisateurExiste(email);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public TaDossier findDossier(String codeDossier) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.findDossier(codeDossier);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaDossier mergeDossier(TaDossier dossier) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.mergeDossier(dossier);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaClient findClientDossier(String codeDossier) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.findClientDossier(codeDossier);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaDossier> findListeDossierClient(int idClient) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.findListeDossierClient(idClient);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaDossier> findListeDossierProduit(String idProduit) throws RemoteException {
		try {
			MonCompteService servicePort = initServicePort();
			
			return servicePort.findListeDossierProduit(idProduit);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaPrixParUtilisateur> findPrixParUtilisateur() throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			List<TaPrixParUtilisateur> l = servicePort.findPrixParUtilisateur();
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaTNiveau> selectAllTNiveau() throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			List<TaTNiveau> l = servicePort.selectAllTNiveau();
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<TaCategoriePro> selectAllCategoriePro() throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			List<TaCategoriePro> l = servicePort.selectAllCategoriePro();
			return l;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaCgv findCgv(int idCgv) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaCgv c = servicePort.findCgv(idCgv);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaCgv findCgvCourant() throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaCgv c = servicePort.findCgvCourant();
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public TaCgPartenaire findCgPartenaire(int idCgPartenaire) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaCgPartenaire c = servicePort.findCgPartenaire(idCgPartenaire);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public TaCgPartenaire findCgPartenaireCourant() throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaCgPartenaire c = servicePort.findCgPartenaireCourant();
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public TaTypePartenaire findTypePartenaire(String codeTypePartenaire) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaTypePartenaire c = servicePort.findTypePartenaire(codeTypePartenaire);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public TaPartenaire findPartenaire(String codeDossier) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaPartenaire c = servicePort.findPartenaire(codeDossier);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public List<TaCommission> findCommissionPartenaire(String codePartenaire, Date debut, Date fin) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();
			List<TaCommission> c = null;
			
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

			c = servicePort.findCommissionPartenaire(codePartenaire, dateDebut, dateFin);
			
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	public TaCommission genereCommission(TaPanier d) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaCommission c = servicePort.genereCommission(d);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaTypePaiement findTypePaiement(String codeTypePaiement) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaTypePaiement c = servicePort.findTypePaiement(codeTypePaiement);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Date findDerniereCreationDossierPayantPartenaire(String codePartenaire) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			XMLGregorianCalendar c = servicePort.findDerniereCreationDossierPayantPartenaire(codePartenaire);
			return c.toGregorianCalendar().getTime();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BigDecimal findMontantVentePartenaire(String codePartenaire, Date debut, Date fin) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();
			
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

			BigDecimal c = servicePort.findMontantVentePartenaire(codePartenaire,dateDebut,dateFin);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BigDecimal findTauxCommissionPartenaireAvecDecote(String codePartenaire) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();
			
			BigDecimal c = servicePort.findTauxCommissionPartenaireAvecDecote(codePartenaire);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BigDecimal findTauxCommissionPartenaireSansDecote(String codePartenaire) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();
			
			BigDecimal c = servicePort.findTauxCommissionPartenaireSansDecote(codePartenaire);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public BigDecimal findDecoteTauxCommissionPartenaire(String codePartenaire) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();
			
			BigDecimal c = servicePort.findDecoteTauxCommissionPartenaire(codePartenaire);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaDossier majAutorisation(TaPanier panier) throws RemoteException {
		try {
			
			MonCompteService servicePort = initServicePort();

			TaDossier c = servicePort.majAutorisation(panier);
			return c;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * COPIER DE ClientController dans MonCompte à déplacer dans les services si besoin
	 * @param l
	 * @param p
	 * @return
	 */
	public fr.legrain.autorisation.xml.ListeModules ajouteAutorisation(fr.legrain.autorisation.xml.ListeModules l, TaProduit p) {
		fr.legrain.autorisation.xml.Module moduleXml = new fr.legrain.autorisation.xml.Module();
		moduleXml.id = p.getIdentifiantModule();
		if(!l.contientModule(moduleXml)) {
			l.module.add(moduleXml);
		}
		System.out.println(moduleXml.id);
		//gestion des dépendances et compostion
		for(TaProduit prod : p.getListeProduitDependant()) {
			moduleXml = new fr.legrain.autorisation.xml.Module();
			moduleXml.id = prod.getIdentifiantModule();
			if(!l.contientModule(moduleXml)) {
				l.module.add(moduleXml);
			}
			//TODO appel récursif à faire
			if(prod.getListeProduitDependant()!=null) {
				l = ajouteAutorisation(l,prod);
			}
		}
		for(TaProduit prod : p.getListeSousProduit()) {
			moduleXml = new fr.legrain.autorisation.xml.Module();
			moduleXml.id = prod.getIdentifiantModule();
			if(!l.contientModule(moduleXml)) {
				l.module.add(moduleXml);
			}
			//TODO appel récursif à faire
			if(prod.getListeSousProduit()!=null) {
				l = ajouteAutorisation(l,prod);
			}
		}
		return l;
	}

}
