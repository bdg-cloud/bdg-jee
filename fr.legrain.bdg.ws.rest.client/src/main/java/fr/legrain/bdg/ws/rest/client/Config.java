package fr.legrain.bdg.ws.rest.client;

public class Config {

	private String dossier;
	private String login;
	private String password;
	
	private boolean verificationSsl = true;
	private boolean devLegrain = false;
	private String baseUrl;
	private String version;
	
	public Config() {
		
	}
	
	public Config(String dossier, String login, String password) {
		super();
		this.login = login;
		this.password = password;
		this.dossier = dossier;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDossier() {
		return dossier;
	}
	public void setDossier(String dossier) {
		this.dossier = dossier;
	}

	public boolean isVerificationSsl() {
		return verificationSsl;
	}

	public void setVerificationSsl(boolean verificationSsl) {
		this.verificationSsl = verificationSsl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public boolean isDevLegrain() {
		return devLegrain;
	}

	public void setDevLegrain(boolean devLegrain) {
		this.devLegrain = devLegrain;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
}
