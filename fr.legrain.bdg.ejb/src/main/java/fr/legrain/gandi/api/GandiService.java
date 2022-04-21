package fr.legrain.gandi.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.dossier.service.TaPreferencesService;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.gandi.service.remote.IGandiService;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;


/**
 * Session Bean implementation class TaClientBean
 */
/*
 * Sources PHP
 * svn+ssh://192.168.1.12/data3/prog/svn/php/api.gwi-hosting.fr/trunk/src/GandiBundle/Entity/InfosDomain.php
 * svn+ssh://192.168.1.12/data3/prog/svn/php/api.gwi-hosting.fr/trunk/src/GandiBundle/Controller/GandiController.php
 * svn+ssh://192.168.1.12/data3/prog/svn/php/api.gwi-hosting.fr/trunk/src/GandiBundle/ThirdParty/XmlRpcGandi.php
 * svn+ssh://192.168.1.12/data3/prog/svn/php/api.gwi-hosting.fr/trunk/src/Legrain/ApiBundle/Services/GandiService.php
 */
@Stateless
@WebService
@DeclareRoles("admin")
public class GandiService implements IGandiService  {

	static Logger logger = Logger.getLogger(GandiService.class);

	private boolean debug=false;
	
	private TaPreferences securityKeyPref;
	private TaPreferences keyProdPref;
	private TaPreferences urlProdPref;
	private TaPreferences keyTestPref;
	private TaPreferences urlTestPref;
	private TaPreferences resellerPref;
	
	private @EJB ITaPreferencesServiceRemote taPreferencesService;

//	public static String keyProd ="HgwfzRO2Dg5jgzuxMEAckzW9";
//	private static String urlProd="https://rpc.gandi.net/xmlrpc/";
//	private static String keyTest="UVcWoTCOuTtJvVDsZlRLuhnl";
//	private static String urlTest="https://rpc.ote.gandi.net/xmlrpc/";
//	private static String reseller ="GI47-GANDI" ;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	//Clé pour être sur que le programme appelant est bien un programme LEGRAIN, cette clé est privée et interne au programme, généré avec pwgen
	private static String SECURITY_KEY = null;
	

	public GandiService() {
//		initPreferencesGandi();
	}

	private Object GandiXMLRpc(String methode, Object[] params) {
		
		if(urlProdPref.getValeur() != null && resellerPref.getValeur() != null && keyProdPref.getValeur() != null) {
			try {		
				XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
//				config.setServerURL(new URL(urlProd));
				config.setServerURL(new URL(urlProdPref.getValeur()));
				config.setBasicUserName(resellerPref.getValeur());
				config.setBasicPassword(keyProdPref.getValeur());
				config.setEnabledForExtensions(true);
				XmlRpcClient client = new XmlRpcClient();
				client.setConfig(config);
				client.setTypeFactory(new MyTypeFactory(client)); 

				Object o = client.execute(methode, params);

				return o;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}else {	
			return null;	
		}
		
	}

	
	public void initPreferencesGandi() {
		try {
			taParametreService.initParamBdg();
			SECURITY_KEY = paramBdg.getTaParametre().getApiGandiBdgSecurityKey();
			
			 securityKeyPref = taPreferencesService.findByGoupeAndCle("Gwi-Hosting", TaPreferencesService.CLE_SECURITE_API_GANDI);
			keyProdPref = taPreferencesService.findByGoupeAndCle("Gwi-Hosting", TaPreferencesService.CLE_API_GANDI_PROD);
			 urlProdPref = taPreferencesService.findByGoupeAndCle("Gwi-Hosting", TaPreferencesService.URL_API_GANDI_PROD);
			 keyTestPref= taPreferencesService.findByGoupeAndCle("Gwi-Hosting", TaPreferencesService.CLE_API_GANDI_TEST);
			 urlTestPref = taPreferencesService.findByGoupeAndCle("Gwi-Hosting", TaPreferencesService.URL_API_GANDI_TEST);
			 resellerPref = taPreferencesService.findByGoupeAndCle("Gwi-Hosting", TaPreferencesService.RESELLER_GANDI);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private InfosDomain infosDomain(String domain){
		initPreferencesGandi();
		Object o = GandiXMLRpc("domain.info",new Object[]{keyProdPref.getValeur(),domain});
		InfosDomain infos = new InfosDomain((HashMap)o);
		System.out.println("GandiTest.infosDomain()");
		return infos;
	}

	/**
	 * Ajoute une ligne de DNS pour un sous domaine donné
	 * @param string $username
	 * @param string $password
	 * @param string $domain
	 * @param string $subdomain
	 * @param string $ip
	 * @return bool
	 */
	@Override
	public boolean registerDNSSubDomain(String securityKey, String domain, String subdomain, String ip){
		initPreferencesGandi();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			HashMap<String, Object> myrecord = new HashMap<String, Object>();
			myrecord.put("name", subdomain);
			myrecord.put("type", "A");
			myrecord.put("ttl", 10800);
			myrecord.put("value", ip);
	
			InfosDomain i = infosDomain(domain);
	
			int zoneId = i.zoneId;
	
			int numVersion = domainZoneVersionNew(zoneId);
			domainZoneAddRecordAction(zoneId,numVersion,myrecord);
			domainZoneVersionSet(zoneId, numVersion);
			domainZoneSet(domain, zoneId);
			return true;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return false;
		}
	}

	/**
	 * Supprime les DNS pour un sous domaine donné
	 * @param string $username
	 * @param string $password
	 * @param string $domain
	 * @param string $subdomain
	 * @return bool
	 */
	@Override
	public boolean deleteDNSSubdomain(String securityKey, String domain, String subdomain){
		initPreferencesGandi();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			HashMap<String, String> myrecord = new HashMap<String, String>();
			myrecord.put("name", subdomain);
			myrecord.put("type", "A");
	
			InfosDomain i = infosDomain(domain);
	
			int zoneId = i.zoneId;
	
			int numVersion = domainZoneVersionNew(zoneId);
			domainZoneRecordDelete(zoneId,numVersion,myrecord);
			domainZoneVersionSet(zoneId, numVersion);
			domainZoneSet(domain, zoneId);
			return true;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return false;
		}
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.record.delete#domain.zone.record.delete
	private int domainZoneRecordDelete(int zone_id, int version_id, HashMap<String, String> options){
		initPreferencesGandi();
		Object o = GandiXMLRpc("domain.zone.record.delete",new Object[]{keyProdPref.getValeur(),zone_id,version_id,options});
		return (int)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.version.new#domain.zone.version.new
	private int domainZoneVersionNew(int zone_id){
		initPreferencesGandi();
		Object o = GandiXMLRpc("domain.zone.version.new",new Object[]{keyProdPref.getValeur(),zone_id});
		return (int)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.record.add#ZoneRecordReturn
	private HashMap domainZoneAddRecordAction(int zone_id,int version_id,HashMap<String, Object> params){
		initPreferencesGandi();
		Object o = GandiXMLRpc("domain.zone.record.add",new Object[]{keyProdPref.getValeur(),zone_id,version_id,params});
		return (HashMap)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.version.set#domain.zone.version.set
	private boolean domainZoneVersionSet(int zone_id, int version_id){
		initPreferencesGandi();
		Object o = GandiXMLRpc("domain.zone.version.set",new Object[]{keyProdPref.getValeur(),zone_id,version_id});
		return (boolean)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.set#domain.zone.set
	private InfosDomain domainZoneSet(String domain, int zone_id){
		initPreferencesGandi();
		Object o = GandiXMLRpc("domain.zone.set",new Object[]{keyProdPref.getValeur(),domain,zone_id});
		return new InfosDomain((HashMap)o);
	}
	
	
	/////////////////// SERVER IAAS HOSTING ///////////////////
	@Override
	public boolean startServer(String securityKey, int vm_id) {
		initPreferencesGandi();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("hosting.vm.start",new Object[]{keyProdPref.getValeur(), vm_id});
			System.out.println("Démarage du serveur");
			return true;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return false;
		}
	}
	
	@Override
	public boolean stopServer(String securityKey, int vm_id) {
		initPreferencesGandi();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("hosting.vm.stop",new Object[]{keyProdPref.getValeur(), vm_id});
			System.out.println("Arrêt du serveur...");
			return true;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return false;
		}
	}
	
	@Override
	public List<ServerIaas> listServer(String securityKey) {
		initPreferencesGandi();
		List<ServerIaas> listserver = new ArrayList<ServerIaas>();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("hosting.vm.list",new Object[]{keyProdPref.getValeur()});
			Object[] arrayObj = (Object[]) o;
			
			for (Object object : arrayObj) {
				ServerIaas server = new ServerIaas((HashMap) object);
				listserver.add(server);
			}
			
			return listserver;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return listserver;
		}
	}
	
	/*************DOMAINES*****************/
	
	@Override
	public InfosOperationDomainRenouv renouvDomaine(String securityKey, String domain, HashMap<String, Integer> params) {
		
		initPreferencesGandi();
		
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("domain.renew",new Object[]{keyProdPref.getValeur(),domain, params});			
			InfosOperationDomainRenouv operation = new InfosOperationDomainRenouv((HashMap)o);
			return operation;

		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return null;
		}
	}
	
	
	@Override
	public List<Domain> listDomaine(String securityKey) {
		
		initPreferencesGandi();		
		List<Domain> listdomaine = new ArrayList<Domain>();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("domain.list",new Object[]{keyProdPref.getValeur()});
			Object[] arrayObj = (Object[]) o;
			
			for (Object object : arrayObj) {
				Domain domain = new Domain((HashMap) object);
				listdomaine.add(domain);
			}
			
			return listdomaine;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return listdomaine;
		}
	}
	
	@Override
	public List<InfosDomainList> listInfosDomaine(String securityKey) {
		initPreferencesGandi();
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("items_per_page", 500);
		List<InfosDomainList> listdomaine = new ArrayList<InfosDomainList>();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("domain.list",new Object[]{keyProdPref.getValeur(), options});
			Object[] arrayObj = (Object[]) o;
			
			for (Object object : arrayObj) {
				InfosDomainList domain = new InfosDomainList((HashMap) object);
				listdomaine.add(domain);
			}
			
			return listdomaine;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return listdomaine;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
		public List<InfosAvailabilityDomainList> listInfosDomaineAvailability(String securityKey, List<String> nomDomaine) {
		initPreferencesGandi();
		List<InfosAvailabilityDomainList> listdomaine = new ArrayList<InfosAvailabilityDomainList>();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("domain.available",new Object[]{keyProdPref.getValeur(), nomDomaine});
			Map arrayObj = (HashMap) o;
			
			arrayObj.forEach((key,value) -> {
			InfosAvailabilityDomainList domain = new InfosAvailabilityDomainList();
			domain.setName(key.toString());
			domain.setStatus(value.toString());
			listdomaine.add(domain);
			});
			
			return listdomaine;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return listdomaine;
		}
	}
	
	/************PACK MAIL******************/
	@Override
	public InfosPackMail infosPackMail(String securityKey, String domain) {
		
		initPreferencesGandi();
		
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("domain.packmail.info",new Object[]{keyProdPref.getValeur(),domain});			
			InfosPackMail packMail = new InfosPackMail((HashMap)o);
			return packMail;

		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return null;
		}
	}
	
	
//	@Override
//	public boolean createContact(String securityKey, HashMap<String, String> options) {
//		initPreferencesGandi();
//		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
//			Object o = GandiXMLRpc("hosting.vm.start",new Object[]{keyProdPref.getValeur(), options});
//			return true;
//		} else {
//			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
//			return false;
//		}
//	}
	
	

	/******************SIMPLE HOSTING*************************/
	@Override
	public List<InfosSimpleHostingList> listInfosSimpleHosting(String securityKey) {
		initPreferencesGandi();
		List<InfosSimpleHostingList> listInstance = new ArrayList<InfosSimpleHostingList>();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("paas.list",new Object[]{keyProdPref.getValeur()});
			Object[] arrayObj = (Object[]) o;
			
			for (Object object : arrayObj) {
				InfosSimpleHostingList instance = new InfosSimpleHostingList((HashMap) object);
				listInstance.add(instance);
			}
			
			return listInstance;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return listInstance;
		}
	}
	/************************* SSL *************************/
	@Override
	public List<InfosCertifSSL> listInfosCertifSSL(String securityKey) {
		initPreferencesGandi();
		List<InfosCertifSSL> listInstance = new ArrayList<InfosCertifSSL>();
		if(securityKey!=null && SECURITY_KEY.equals(securityKey)) {
			Object o = GandiXMLRpc("cert.list",new Object[]{keyProdPref.getValeur()});
			Object[] arrayObj = (Object[]) o;
			
			for (Object object : arrayObj) {
				InfosCertifSSL instance = new InfosCertifSSL((HashMap) object);
				listInstance.add(instance);
			}
			
			return listInstance;
		} else {
			System.out.println("La clé de sécurité interne est incorrecte, pas d'appel des API Gandi");
			return listInstance;
		}
	}


	public TaPreferences getSecurityKeyPref() {
		return securityKeyPref;
	}

	public void setSecurityKeyPref(TaPreferences securityKeyPref) {
		this.securityKeyPref = securityKeyPref;
	}

	public TaPreferences getKeyProdPref() {
		return keyProdPref;
	}

	public void setKeyProdPref(TaPreferences keyProdPref) {
		this.keyProdPref = keyProdPref;
	}

	public TaPreferences getUrlProdPref() {
		return urlProdPref;
	}

	public void setUrlProdPref(TaPreferences urlProdPref) {
		this.urlProdPref = urlProdPref;
	}

	public TaPreferences getKeyTestPref() {
		return keyTestPref;
	}

	public void setKeyTestPref(TaPreferences keyTestPref) {
		this.keyTestPref = keyTestPref;
	}

	public TaPreferences getUrlTestPref() {
		return urlTestPref;
	}

	public void setUrlTestPref(TaPreferences urlTestPref) {
		this.urlTestPref = urlTestPref;
	}

	public TaPreferences getResellerPref() {
		return resellerPref;
	}

	public void setResellerPref(TaPreferences resellerPref) {
		this.resellerPref = resellerPref;
	}

	public static String getSECURITY_KEY() {
		return SECURITY_KEY;
	}

	public static void setSECURITY_KEY(String sECURITY_KEY) {
		SECURITY_KEY = sECURITY_KEY;
	}


}
