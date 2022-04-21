package fr.legrain.hibernate.multitenant;

import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;
import javax.transaction.TransactionSynchronizationRegistry;

public class ServerTenantInterceptor {
	
	//http://www.adam-bien.com/roller/abien/entry/how_to_pass_context_in
	//http://www.adam-bien.com/roller/abien/entry/how_to_pass_context_with

    static public final String TENANT_TOKEN_KEY = "tenant";
    static String TENANT_TOKEN_VALUE = null;
    
    @Inject private TenantRequest tenantRequest;

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        String tenant = null;
        
       // ITenantInfoService tenantInfoService = (ITenantInfoService) new InitialContext().lookup("java:module/TenantInfoService");
        TransactionSynchronizationRegistry reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry"); 
        
        Map<String, Object> contextData = invocationContext.getContextData();
        if (contextData.containsKey(TENANT_TOKEN_KEY)) {
            tenant = (String) contextData.get(TENANT_TOKEN_KEY);
            System.out.println("ServerTenantInterceptor.aroundInvoke() : "+tenant);
            if(tenant!=null && !tenant.equals("")) {
            	TENANT_TOKEN_VALUE = tenant;
            	tenantRequest.setTenantRequest(tenant);
            	
//            	TenantInfo t = new TenantInfo();
//            	t.setTenantId(authToken);
//            	tenantInfoService.setTenantInfo(t);
            	
//            	tenantInfoService.getRegistry().putResource(SECURITY_TOKEN_KEY, authToken);
            	reg.putResource(TENANT_TOKEN_KEY, tenant);
            }

        }
        return invocationContext.proceed();
    }
}