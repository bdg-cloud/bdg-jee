package fr.legrain.bdg.webapp.paiement;

import java.io.Serializable;

import fr.legrain.document.model.SWTDocument;

public class PaiementParamWs extends PaiementParam implements Serializable{
	
	private SWTDocument swtDocumentPayableCB;
	
	private String codeDocument;
	private String typeDocmument; //ex:: TaFacture.TYPE_DOC
	
	private String loginDeLaTableEspaceClient;
	private String passwordDeLaTableEspaceClient;
	private String dossierTenant ;

	private boolean verificationSsl = true;
	private boolean devLegrain = false;
	
	public String getLoginDeLaTableEspaceClient() {
		return loginDeLaTableEspaceClient;
	}
	public void setLoginDeLaTableEspaceClient(String loginDeLaTableEspaceClient) {
		this.loginDeLaTableEspaceClient = loginDeLaTableEspaceClient;
	}
	public String getPasswordDeLaTableEspaceClient() {
		return passwordDeLaTableEspaceClient;
	}
	public void setPasswordDeLaTableEspaceClient(String passwordDeLaTableEspaceClient) {
		this.passwordDeLaTableEspaceClient = passwordDeLaTableEspaceClient;
	}
	public String getDossierTenant() {
		return dossierTenant;
	}
	public void setDossierTenant(String dossierTenant) {
		this.dossierTenant = dossierTenant;
	}
	public boolean isVerificationSsl() {
		return verificationSsl;
	}
	public void setVerificationSsl(boolean verificationSsl) {
		this.verificationSsl = verificationSsl;
	}
	public boolean isDevLegrain() {
		return devLegrain;
	}
	public void setDevLegrain(boolean devLegrain) {
		this.devLegrain = devLegrain;
	}
	public SWTDocument getSwtDocumentPayableCB() {
		return swtDocumentPayableCB;
	}
	public void setSwtDocumentPayableCB(SWTDocument swtDocumentPayableCB) {
		this.swtDocumentPayableCB = swtDocumentPayableCB;
	}
	public String getCodeDocument() {
		return codeDocument;
	}
	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}
	public String getTypeDocmument() {
		return typeDocmument;
	}
	public void setTypeDocmument(String typeDocmument) {
		this.typeDocmument = typeDocmument;
	}
	

}
