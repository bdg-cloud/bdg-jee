package fr.legrain.bdg.rest.model;

public class ParamDemandeRenseignement {
	private Integer id;
	
	private Integer idTiers;
	private String codeTiers;
	private String email;
	private Integer idcompteEspaceClient;
	private String emailCompteEspaceClient;
	private String texteDemande;
	private Integer idArticle;
	private String codeArticle;
	private String libelleArticle;
	
	public Integer getIdTiers() {
		return idTiers;
	}
	public void setIdTiers(Integer idTiers) {
		this.idTiers = idTiers;
	}
	public String getCodeTiers() {
		return codeTiers;
	}
	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getIdcompteEspaceClient() {
		return idcompteEspaceClient;
	}
	public void setIdcompteEspaceClient(Integer idcompteEspaceClient) {
		this.idcompteEspaceClient = idcompteEspaceClient;
	}
	public String getEmailCompteEspaceClient() {
		return emailCompteEspaceClient;
	}
	public void setEmailCompteEspaceClient(String emailCompteEspaceClient) {
		this.emailCompteEspaceClient = emailCompteEspaceClient;
	}
	public String getTexteDemande() {
		return texteDemande;
	}
	public void setTexteDemande(String texteDemande) {
		this.texteDemande = texteDemande;
	}
	public Integer getIdArticle() {
		return idArticle;
	}
	public void setIdArticle(Integer idArticle) {
		this.idArticle = idArticle;
	}
	public String getCodeArticle() {
		return codeArticle;
	}
	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public String getLibelleArticle() {
		return libelleArticle;
	}
	public void setLibelleArticle(String libelleArticle) {
		this.libelleArticle = libelleArticle;
	}
	
}
