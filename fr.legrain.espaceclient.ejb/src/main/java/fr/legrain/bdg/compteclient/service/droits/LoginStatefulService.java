package fr.legrain.bdg.compteclient.service.droits;

import javax.ejb.Stateful;

@Stateful  
public class LoginStatefulService implements ILoginStatefulService {  

	//@Inject 
	private	TenantInfo tenantInfo;

	public TenantInfo getTenantInfo() {
		return tenantInfo;
	}

	public void setTenantInfo(TenantInfo tenantInfo) {
		this.tenantInfo = tenantInfo;
	}

	@Override
	public String login() {
		return "";
	} 

}