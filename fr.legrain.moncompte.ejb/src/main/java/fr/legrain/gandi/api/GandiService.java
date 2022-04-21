package fr.legrain.gandi.api;

import java.net.URL;
import java.util.HashMap;

import javax.annotation.security.DeclareRoles;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import fr.legrain.angdi.service.remote.IGandiService;


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

	private static String keyProd ="HgwfzRO2Dg5jgzuxMEAckzW9";
	private static String urlProd="https://rpc.gandi.net/xmlrpc/";
	private static String keyTest="UVcWoTCOuTtJvVDsZlRLuhnl";
	private static String urlTest="https://rpc.ote.gandi.net/xmlrpc/";
	private static String reseller ="GI47-GANDI" ;
	
	//Clé pour être sur que le programme appelant est bien un programme LEGRAIN, cette clé est privée et interne au programme, généré avec pwgen
	private static String SECURITY_KEY = "Thai8Saisothoo3m";

	public GandiService() {
	}

	private Object GandiXMLRpc(String methode, Object[] params) {
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(urlProd));
			config.setBasicUserName(reseller);
			config.setBasicPassword(keyProd);
			config.setEnabledForExtensions(true);
			XmlRpcClient client = new XmlRpcClient();
			client.setConfig(config);
			client.setTypeFactory(new MyTypeFactory(client)); 

			Object o = client.execute(methode, params);

			return o;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private InfosDomain infosDomain(String domain){
		Object o = GandiXMLRpc("domain.info",new Object[]{keyProd,domain});
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
		Object o = GandiXMLRpc("domain.zone.record.delete",new Object[]{keyProd,zone_id,version_id,options});
		return (int)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.version.new#domain.zone.version.new
	private int domainZoneVersionNew(int zone_id){
		Object o = GandiXMLRpc("domain.zone.version.new",new Object[]{keyProd,zone_id});
		return (int)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.record.add#ZoneRecordReturn
	private HashMap domainZoneAddRecordAction(int zone_id,int version_id,HashMap<String, Object> params){
		Object o = GandiXMLRpc("domain.zone.record.add",new Object[]{keyProd,zone_id,version_id,params});
		return (HashMap)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.version.set#domain.zone.version.set
	private boolean domainZoneVersionSet(int zone_id, int version_id){
		Object o = GandiXMLRpc("domain.zone.version.set",new Object[]{keyProd,zone_id,version_id});
		return (boolean)o;
	}

	//http://doc.rpc.gandi.net/domain/reference.html?highlight=domain.zone.set#domain.zone.set
	private InfosDomain domainZoneSet(String domain, int zone_id){
		Object o = GandiXMLRpc("domain.zone.set",new Object[]{keyProd,domain,zone_id});
		return new InfosDomain((HashMap)o);
	}


}
