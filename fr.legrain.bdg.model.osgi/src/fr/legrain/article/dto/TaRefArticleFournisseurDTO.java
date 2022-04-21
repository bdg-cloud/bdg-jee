package fr.legrain.article.dto;

import java.util.Date;

import fr.legrain.bdg.model.ModelObject;



public class TaRefArticleFournisseurDTO extends ModelObject implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1617666095334774413L;
	
	
	private int id;
	private String version;
	private String codeFournisseur;
	private Integer idFournisseur;
	private String codeArticle;
	private Integer idArticle;
	private String codeBarreFournisseur;
	private String codeArticleFournisseur;
	private String descriptif;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	
	
	
	
	public TaRefArticleFournisseurDTO() {

	}

	public TaRefArticleFournisseurDTO(int id, String codeFournisseur, Integer idFournisseur, String codeArticle,
			Integer idArticle, String codeBarreFournisseur, String codeArticleFournisseur, String descriptif) {
		super();
		this.id = id;
		this.codeFournisseur = codeFournisseur;
		this.idFournisseur = idFournisseur;
		this.codeArticle = codeArticle;
		this.idArticle = idArticle;
		this.codeBarreFournisseur = codeBarreFournisseur;
		this.codeArticleFournisseur = codeArticleFournisseur;
		this.descriptif = descriptif;
	}

	public boolean estVide() {
		if(this.getCodeFournisseur()==null || this.getCodeFournisseur().isEmpty())return true;
		if(this.getCodeArticleFournisseur()!=null && !this.getCodeArticleFournisseur().isEmpty())return false;
		if(this.getCodeBarreFournisseur()!=null && !this.getCodeBarreFournisseur().isEmpty())return false;

		return true;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCodeBarreFournisseur() {
		return codeBarreFournisseur;
	}
	public void setCodeBarreFournisseur(String codeBarreFournisseur) {
		this.codeBarreFournisseur = codeBarreFournisseur;
	}
	public String getCodeArticleFournisseur() {
		return codeArticleFournisseur;
	}
	public void setCodeArticleFournisseur(String codeArticleFournisseur) {
		this.codeArticleFournisseur = codeArticleFournisseur;
	}
	public String getDescriptif() {
		return descriptif;
	}
	public void setDescriptif(String descriptif) {
		this.descriptif = descriptif;
	}
	public String getQuiCree() {
		return quiCree;
	}
	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}
	public Date getQuandCree() {
		return quandCree;
	}
	public void setQuandCree(Date quandCree) {
		this.quandCree = quandCree;
	}
	public String getQuiModif() {
		return quiModif;
	}
	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}
	public Date getQuandModif() {
		return quandModif;
	}
	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}
	public String getIpAcces() {
		return ipAcces;
	}
	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	public Integer getVersionObj() {
		return versionObj;
	}
	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	public String getCodeFournisseur() {
		return codeFournisseur;
	}

	public void setCodeFournisseur(String codeFournisseur) {
		this.codeFournisseur = codeFournisseur;
	}

	public Integer getIdFournisseur() {
		return idFournisseur;
	}

	public void setIdFournisseur(Integer idFournisseur) {
		this.idFournisseur = idFournisseur;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public Integer getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(Integer idArticle) {
		this.idArticle = idArticle;
	}

	
	
	
	
	
}
