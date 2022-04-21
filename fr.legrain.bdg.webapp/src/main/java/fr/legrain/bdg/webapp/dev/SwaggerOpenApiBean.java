package fr.legrain.bdg.webapp.dev;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.lib.script.LibScript;

@Named
@ViewScoped
public class SwaggerOpenApiBean implements Serializable {

	private static final long serialVersionUID = 2635209798770183258L;
	
	@Inject protected TenantInfo tenantInfo;
	@Inject protected SessionInfo sessionInfo;

	private BdgProperties bdgProperties;
	
	private String urlOpenApiV1;
	private String urlOpenApiDossierV1;
	private String urlOpenApiMetaInf;
	
	@PostConstruct
	public void init() {
		bdgProperties = new BdgProperties();
		
		String api = "/v1/openapi.json";
		
		//https://dev.demo.promethee.biz:8443/v1/openapi.json
		//urlOpenApiV1 = bdgProperties.url(tenantInfo.getTenantId(), true)+"/v1/openapi.json";
		urlOpenApiV1 = bdgProperties.urlApiGlobal(true)+api;
		urlOpenApiDossierV1 = bdgProperties.urlApiDossier(tenantInfo.getTenantId(),true)+api;
		urlOpenApiMetaInf = bdgProperties.urlApiDossier(tenantInfo.getTenantId(),true)+"/open-api"+api;
	}

	public String getUrlOpenApiV1() {
		return urlOpenApiV1;
	}

	public String getUrlOpenApiDossierV1() {
		return urlOpenApiDossierV1;
	}

	public String getUrlOpenApiMetaInf() {
		return urlOpenApiMetaInf;
	}

}
