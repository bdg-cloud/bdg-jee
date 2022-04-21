package fr.legrain.hibernate.multitenant;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class TenantRequest implements Serializable{

	private String tenantRequest;

	public String getTenantRequest() {
		return tenantRequest;
	}

	public void setTenantRequest(String tenantRequest) {
		this.tenantRequest = tenantRequest;
	}
	
	
}
