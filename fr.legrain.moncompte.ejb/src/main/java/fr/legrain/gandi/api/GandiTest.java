package fr.legrain.gandi.api;

import java.net.URL;
import java.util.HashMap;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/*
 * Sources PHP
 * svn+ssh://192.168.1.11/home/prog/svn/php/api.gwi-hosting.fr/trunk/src/GandiBundle/Entity/InfosDomain.php
 * svn+ssh://192.168.1.11/home/prog/svn/php/api.gwi-hosting.fr/trunk/src/GandiBundle/Controller/GandiController.php
 * svn+ssh://192.168.1.11/home/prog/svn/php/api.gwi-hosting.fr/trunk/src/GandiBundle/ThirdParty/XmlRpcGandi.php
 * svn+ssh://192.168.1.11/home/prog/svn/php/api.gwi-hosting.fr/trunk/src/Legrain/ApiBundle/Services/GandiService.php
 */
public class GandiTest {
	
	private boolean debug=false;

    private String keyProd ="HgwfzRO2Dg5jgzuxMEAckzW9";
    private String urlProd="https://rpc.gandi.net/xmlrpc/";
    private String keyTest="UVcWoTCOuTtJvVDsZlRLuhnl";
    private String urlTest="https://rpc.ote.gandi.net/xmlrpc/";
    private String reseller ="GI47-GANDI" ;
    
    protected String em;
    protected String gandi;
    
    public GandiTest() {
//        $this->em = $em;
//        $this->gandi = new GandiController();

    }
    
    public Object GandiXMLRpc(String methode, Object[] params) {
    	try {
    		
	    	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		    config.setServerURL(new URL(urlProd));
		    config.setBasicUserName(reseller);
		    config.setBasicPassword(keyProd);
		    config.setEnabledForExtensions(true);
		    XmlRpcClient client = new XmlRpcClient();
		    client.setConfig(config);
		    client.setTypeFactory(new MyTypeFactory(client)); 
    		
    		/*
    		  HashMap<String,String> paramsLogin = new HashMap<String, String>();
         paramsLogin.put("login", "ja...@user.de" );
         paramsLogin.put("password", "pass");
            
         HashMap loginResult = null;       
        
         try {
             loginResult = (HashMap) client.execute("User.login", new Object[] {paramsLogin}); 
    		 */
    		

//	//	    Object[] params = new Object[]{new Integer(33), new Integer(9)};
		    Object o = /*(Integer)*/ client.execute(methode, params);
    		
//		    HashMap<String,String> paramsLogin = new HashMap<String, String>();
//		    paramsLogin.put("apikey", keyProd );
//		    paramsLogin.put("domain", "legrain.work" );
//		    HashMap loginResult = null;     
//		    loginResult = (HashMap) client.execute(methode, new Object[] {paramsLogin}); 
    		
		    return o;
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		return null;
    }
    
    public InfosDomain infosDomain(/*Connect $connect,*/String domain){
//      $this->connect($connect);
    	Object o = GandiXMLRpc("domain.info",new Object[]{keyProd,domain});
    	InfosDomain infos = new InfosDomain((HashMap)o);
    	System.out.println("GandiTest.infosDomain()");
    	return infos;
    }
    
    
  /**
   * Supprime les DNS pour un sous domaine donné
   * @param string $username
   * @param string $password
   * @param string $domain
   * @param string $subdomain
   * @return bool
   */
  public boolean deleteDNSSubdomain(/*String username, String password,*/ String domain, String subdomain){


//      $connect = $this->gandi->connection($username,$password);
	  HashMap<String, String> myrecord = new HashMap<String, String>();
	  myrecord.put("name", subdomain);
	  myrecord.put("type", "A");
//      $myrecord = array('name'=> $subdomain, 'type'=> 'A');


      InfosDomain i = infosDomain(domain);


      int zoneId = i.zoneId;

      int numVersion = domainZoneVersionNew(zoneId);
      domainZoneRecordDelete(zoneId,numVersion,myrecord);
      domainZoneVersionSet(zoneId, numVersion);
      domainZoneSet(domain, zoneId);
      return true;


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
  public boolean registerDNSSubDomain(/*String username, String password,*/ String domain, String subdomain, String ip){
//      $connect = $this->gandi->connection($username,$password);
//      $myrecord = array('name'=> $subdomain, 'type'=> 'A','ttl'=>10800,'value'=>$ip);
      
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
  }
  
  public int domainZoneRecordDelete(int zone_id, int version_id, HashMap<String, String> options){
//      $gandi = new XmlRpcGandi();

//      return $gandi->request('domainZoneRecordDelete',$zone_id,$version_id,$record);
      Object o = GandiXMLRpc("domain.zone.record.delete",new Object[]{keyProd,zone_id,version_id,options});
      return (int)o;
  }
  
  public int domainZoneVersionNew(int zone_id){

//      $gandi = new XmlRpcGandi();

//      return $gandi->request('domainZoneVersionNew',$zone_id);
      Object o = GandiXMLRpc("domain.zone.version.new",new Object[]{keyProd,zone_id});
      return (int)o;
  }
  
  public HashMap domainZoneAddRecordAction(int zone_id,int version_id,HashMap<String, Object> params){
//      $gandi = new XmlRpcGandi();

//      return $gandi->request('domainZoneRecordAdd',$zone_id,$version_id,$records);
      Object o = GandiXMLRpc("domain.zone.record.add",new Object[]{keyProd,zone_id,version_id,params});
      return (HashMap)o;

  }
  
  public boolean domainZoneVersionSet(int zone_id, int version_id){
//      $gandi = new XmlRpcGandi();

//      return $gandi->request('domainZoneVersionSet',$zone_id,$version_id);
      Object o = GandiXMLRpc("domain.zone.version.set",new Object[]{keyProd,zone_id,version_id});
      return (boolean)o;
  }
  
  public InfosDomain domainZoneSet(String domain, int zone_id){
//      $gandi = new XmlRpcGandi();

//      return $gandi->request('domainZoneSet',$domain,$version_id);
      Object o = GandiXMLRpc("domain.zone.set",new Object[]{keyProd,domain,zone_id});
      return new InfosDomain((HashMap)o);
  }

	public static void main(String[] args) {
		GandiTest t = new GandiTest();
//		t.infosDomain("legrain.work");
		
		String domaine = "legrain.work";
		String sousDomaine = "demo6";
		String ip = "1.2.3.4";
		
		try {
//			Boolean r = t.registerDNSSubDomain(domaine, sousDomaine, ip);
			
			Boolean r = t.deleteDNSSubdomain(domaine, sousDomaine);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("GandiTest.main()");
	}

}
