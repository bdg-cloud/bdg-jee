package fr.legrain.bdg.rest.model;

import java.util.Date;

import fr.legrain.lib.data.LibCrypto;

public class ParamCompteEspaceClient {
	private Integer id;
	private String hostname;
	private String port;
	private String codeDossierFournisseur;
	private String codeClientChezCeFournisseur; 
	private String email;
	private String password;
	private String passwordConf;
	private Boolean actif;
	
	public String codeTiers;
    public String nom;
    public String prenom;
    
    public String dateLimiteLienMillis;
    public Date dateLimiteLien;
    
    public String infoCompteCryptees;
    
    public String token;
    public String accessToken;
    public String refreshToken;
    
    public void crypte() {
    	
    	if(dateLimiteLien==null) {
    		dateLimiteLien = new Date();
    	}
    	dateLimiteLienMillis = ""+dateLimiteLien.getTime();
    	String a = getNom()+IConstRestModel.separateurChaineCryptee
				+getPrenom()+IConstRestModel.separateurChaineCryptee
				+getEmail()+IConstRestModel.separateurChaineCryptee
				+getPassword()+IConstRestModel.separateurChaineCryptee
				+getCodeTiers()+IConstRestModel.separateurChaineCryptee
				+getDateLimiteLienMillis()+IConstRestModel.separateurChaineCryptee
				+getCodeDossierFournisseur();
    	String b = LibCrypto.encrypt(a);
		setInfoCompteCryptees(b);
    }
    
    public void decrypte() {
    	String paramDecrypt = LibCrypto.decrypt(getInfoCompteCryptees());
		System.out.println("param decrypt : "+paramDecrypt);
		
		setNom(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[0]);
		setPrenom(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[1]);
		setEmail(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[2]);
		setPassword(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[3]);
//		setPasswordConf(paramDecrypt.split(IEspaceClientRestMultitenantProxy.separateurChaineCryptee)[3]);
		setCodeTiers(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[4]);
		setDateLimiteLienMillis(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[5]);
		setCodeDossierFournisseur(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[6]);
		
		setDateLimiteLien(new Date(new Long(dateLimiteLienMillis)));
    }
    
    public void decrypteDateLimite() {
    	String paramDecrypt = LibCrypto.decrypt(getInfoCompteCryptees());
    	setDateLimiteLienMillis(paramDecrypt.split(IConstRestModel.separateurChaineCryptee)[5]);
    	setDateLimiteLien(new Date(new Long(dateLimiteLienMillis)));
    }
	
	public String getCodeDossierFournisseur() {
		return codeDossierFournisseur;
	}
	public void setCodeDossierFournisseur(String codeDossierFournisseur) {
		this.codeDossierFournisseur = codeDossierFournisseur;
	}
	public String getCodeClientChezCeFournisseur() {
		return codeClientChezCeFournisseur;
	}
	public void setCodeClientChezCeFournisseur(String codeClientChezCeFournisseur) {
		this.codeClientChezCeFournisseur = codeClientChezCeFournisseur;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String login) {
		this.email = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getActif() {
		return actif;
	}
	public void setActif(Boolean actif) {
		this.actif = actif;
	}
	public String getCodeTiers() {
		return codeTiers;
	}
	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getPasswordConf() {
		return passwordConf;
	}
	public void setPasswordConf(String passwordConf) {
		this.passwordConf = passwordConf;
	}
	public String getInfoCompteCryptees() {
		return infoCompteCryptees;
	}
	public void setInfoCompteCryptees(String infoCompteCryptees) {
		this.infoCompteCryptees = infoCompteCryptees;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDateLimiteLienMillis() {
		return dateLimiteLienMillis;
	}

	public void setDateLimiteLienMillis(String dateLimiteLienMillis) {
		this.dateLimiteLienMillis = dateLimiteLienMillis;
	}

	public Date getDateLimiteLien() {
		return dateLimiteLien;
	}

	public void setDateLimiteLien(Date dateLimiteLien) {
		this.dateLimiteLien = dateLimiteLien;
	}
	
}
