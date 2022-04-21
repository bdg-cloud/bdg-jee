package fr.legrain.bdg.compteclient.service.droits;

import javax.ejb.Stateless;
import javax.inject.Inject;

//import org.jboss.weld.context.bound.BoundSessionContext;

@Stateless 
//@Interceptors(ServerSecurityInterceptor.class)
public class TenantInfoService implements ITenantInfoService {  

	@Inject private	TenantInfo tenantInfo;
	
//	@Inject private BoundSessionContext sessionContext;

	public TenantInfo getTenantInfo() {
//		 Map<String,Object> myMap=new HashMap<String,Object>();
//	        sessionContext.associate(myMap);
//	        sessionContext.activate();
//
//	        LOG.info("session object: "+sessionObj);
//	        LOG.info("Method call: "+sessionObj.getValue());
//
//	        sessionContext.invalidate();
//	        sessionContext.deactivate();
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	} 

}