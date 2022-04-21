package fr.legrain.droits.service;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

//import org.jboss.weld.context.bound.BoundSessionContext;

@Stateless 
//@Stateful
//@SessionScoped
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