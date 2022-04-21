package fr.legrain.hibernate.multitenant;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import fr.legrain.bdg.rest.filters.request.JWTTokenGlobalFilter;
import fr.legrain.droits.service.ITenantInfoService;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.general.service.TenantSoapHeaderHandler;
import fr.legrain.general.service.ThreadLocalContextHolder;

//import org.jboss.weld.context.bound.BoundSessionContext;

//@Stateless
public class SchemaResolver implements CurrentTenantIdentifierResolver {
	
	//@Inject private TenantInfo tenantInfo;
	private	TenantInfo tenantInfo;
	
//	@Inject private BoundSessionContext sessionContext;
	
//	@Resource
//    private TransactionSynchronizationRegistry tsr;
	
	static private String staticTenantValue = null;
	static private Boolean dynamicTenant = null;
	
	public void init() /*throws FileNotFoundException, IOException*/ {
		String propertiesFileName = "bdg.properties";  
	      
	    Properties props = new Properties();  
	    //String path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;  
//	    String path = System.getProperty("jboss.domain.config.dir")+"/"+propertiesFileName;
	    String path = null;
	    if(System.getProperty("mode.domaine")!=null &&
				System.getProperty("mode.domaine").equals("true"))
			path = System.getProperty("jboss.domain.config.dir")+"/"+propertiesFileName;
		else 
			path = System.getProperty("jboss.server.config.dir")+"/"+propertiesFileName;
	      
	    try {
		    if(new File(path).exists()) { 
		    	File f = new File(path);
		        props.load(new FileInputStream(f));  
		        if(props.getProperty("defaultTenant")!=null) {
		        	SchemaResolver.dynamicTenant = false;
		        	SchemaResolver.staticTenantValue = props.getProperty("defaultTenant");
		        } else {
		        	SchemaResolver.staticTenantValue = null;
		        	SchemaResolver.dynamicTenant = true;
		        }
		    } 
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public String resolveCurrentTenantIdentifier() {

		boolean debug = true;
		String tenantTransaction = null;
		
//		 Map<String,Object> myMap=new HashMap<String,Object>();
//	        sessionContext.associate(myMap);
//	        sessionContext.activate();

//	        LOG.info("session object: "+sessionObj);
//	        LOG.info("Method call: "+sessionObj.getValue());
			
				if(SchemaResolver.dynamicTenant == null) {
					init();
				}

				if(SchemaResolver.dynamicTenant) {
					try {
						TransactionSynchronizationRegistry reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry"); 
						if(reg!=null && reg.getResource(ServerTenantInterceptor.TENANT_TOKEN_KEY)!=null ) {
							tenantTransaction = (String) reg.getResource(ServerTenantInterceptor.TENANT_TOKEN_KEY);
							System.out.println("resolveCurrentTenantIdentifier ********REMOTE EJB ********* via TransactionSynchronizationRegistry !!!! "+tenantTransaction);
							if(tenantTransaction!=null) {
								//il y a une "information de tenant" lié à la transaction, c'est un appel d'EJB via le client lourd ou un appel interne (@Schedule EJB ou multitenant proxy d'un WS)
								return nettoyageNomSchema(tenantTransaction);
							}
							// else, pas d' "information de tenant" lié à la transaction, c'est un appel client léger, on cherche le tenant dans la session
						}
						if(ThreadLocalContextHolder.get(TenantSoapHeaderHandler.TENANT_SOAP_TOKEN_KEY)!=null) {
							//tenantTransaction = (String) reg.getResource(TenantSoapHeaderHandler.TENANT_TOKEN_KEY);
							tenantTransaction = (String) ThreadLocalContextHolder.get(TenantSoapHeaderHandler.TENANT_SOAP_TOKEN_KEY);
							//Pour les appels SOAP on ne peu pas utiliser TransactionSynchronizationRegistry car la transaction n'est pas encore démarer  quand le SOAPHandler s'execute
							System.out.println("resolveCurrentTenantIdentifier ********SOAP********* via ThreadLocalContextHolder !!!! "+tenantTransaction);
							return nettoyageNomSchema(ThreadLocalContextHolder.get(TenantSoapHeaderHandler.TENANT_SOAP_TOKEN_KEY).toString());
						}
						if(ThreadLocalContextHolder.get(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY)!=null) {
							//tenantTransaction = (String) reg.getResource(TenantSoapHeaderHandler.TENANT_TOKEN_KEY);
							tenantTransaction = (String) ThreadLocalContextHolder.get(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY);
							//Pour les appels SOAP on ne peu pas utiliser TransactionSynchronizationRegistry car la transaction n'est pas encore démarer  quand le SOAPHandler s'execute
							System.out.println("resolveCurrentTenantIdentifier ********REST********* via ThreadLocalContextHolder !!!! "+tenantTransaction);
							return nettoyageNomSchema(ThreadLocalContextHolder.get(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY).toString());
						}
						ITenantInfoService tenantInfoService = (ITenantInfoService) new InitialContext().lookup("java:module/TenantInfoService"); 
						if(tenantInfoService!=null && tenantInfoService.getTenantInfo()!=null) {
							tenantInfo = tenantInfoService.getTenantInfo();
							if(debug) {
								System.out.println("resolveCurrentTenantIdentifier ******************************** --- : "+tenantInfo.getTenantId());
							}
						} else {
							System.out.println("resolveCurrentTenantIdentifier ******************************** ### tenantInfoService est NULL après le lookup ");
						}

					} catch (NamingException e) {
						e.printStackTrace();
					}
		
					if(debug) {
						System.out.println("Tenant (resolveCurrentTenantIdentifier) : "+tenantInfo.getTenantId());
					}
//					sessionContext.invalidate();
//			        sessionContext.deactivate();
					return nettoyageNomSchema(tenantInfo.getTenantId());
				} else {
//					return "demo"; 
//					sessionContext.invalidate();
//			        sessionContext.deactivate();
					return nettoyageNomSchema(staticTenantValue);
				}	
		
		//TODO: Implement service to identify tenant like: userService.getCurrentlyAuthUser().getTenantId();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return false;
	}
	
	private String nettoyageNomSchema(String tenant) {
		if(tenant!=null) {
			tenant = tenant.replaceAll("-", "_");
		}
		return tenant;
	}
}