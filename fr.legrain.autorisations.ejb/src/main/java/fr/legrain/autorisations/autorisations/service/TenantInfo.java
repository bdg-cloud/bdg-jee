package fr.legrain.autorisations.autorisations.service;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class TenantInfo implements Serializable{

	private static final long serialVersionUID = 638568713647982854L;
	
	private String tenantId;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
