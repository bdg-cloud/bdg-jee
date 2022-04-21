package fr.legrain.servicewebexterne.dto;




import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.model.ModelObject;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.tiers.dto.TaTiersDTO;

public class TaLiaisonServiceExterneDTO  extends ModelObject implements java.io.Serializable{





	/**
	 * 
	 */
	private static final long serialVersionUID = -4458860403726469191L;

	private Integer id;
	
	private Integer idEntite;
	private String refExterne;
	private String typeEntite;
	private Integer idServiceExterne;
	private String codeServiceExterne;
	private String libelleServiceExterne;
	
	private String codeTiers;
	private String nomTiers;
	private String prenomTiers;
	
	private String libelleArticle;
	private String codeArticle;
	
	private String codeTPaiement;
	private String libTPaiement;
	
	
	private String codeDocument;
	
	private TaTiersDTO tiersDTO;
	private TaArticleDTO articleDTO;
	private TaTPaiementDTO taTPaiementDTO;
	
	
	

	private Integer versionObj;
	
	public TaLiaisonServiceExterneDTO() {
	}
	//pour tiers
	public TaLiaisonServiceExterneDTO(Integer id, 
			Integer idEntite,
			String refExterne,
			String typeEntite,
			Integer idServiceExterne,
			String codeServiceExterne,
			String libelleServiceExterne,
			String codeTiers,
			String nomTiers,
			String prenomTiers) {
		this.id = id;
		this.idEntite = idEntite;
		this.refExterne = refExterne;
		this. typeEntite = typeEntite;
		this.idServiceExterne = idServiceExterne;
		this.codeServiceExterne = codeServiceExterne;
		this.libelleServiceExterne = libelleServiceExterne;
		this.codeTiers = codeTiers;
		this.nomTiers = nomTiers;
		this.prenomTiers = prenomTiers;
		

	}
	
	//pour article et type paiement
	public TaLiaisonServiceExterneDTO(Integer id, 
			Integer idEntite,
			String refExterne,
			String typeEntite,
			Integer idServiceExterne,
			String codeServiceExterne,
			String libelleServiceExterne,
			String code,
			String libelle
			) {
		this.id = id;
		this.idEntite = idEntite;
		this.refExterne = refExterne;
		this.typeEntite = typeEntite;
		this.idServiceExterne = idServiceExterne;
		this.codeServiceExterne = codeServiceExterne;
		this.libelleServiceExterne = libelleServiceExterne;

		
		if(typeEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE)) {
			this.codeArticle = code;
			this.libelleArticle = libelle;
		}else if(typeEntite.equals(TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT)){
			this.codeTPaiement = code;
			this.libTPaiement = libelle;
		}
		
		

	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getIdEntite() {
		return idEntite;
	}



	public void setIdEntite(Integer idEntite) {
		this.idEntite = idEntite;
	}



	public String getRefExterne() {
		return refExterne;
	}



	public void setRefExterne(String refExterne) {
		this.refExterne = refExterne;
	}



	public String getTypeEntite() {
		return typeEntite;
	}



	public void setTypeEntite(String typeEntite) {
		this.typeEntite = typeEntite;
	}



	public Integer getVersionObj() {
		return versionObj;
	}



	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}



	public String getCodeTiers() {
		return codeTiers;
	}



	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}



	public String getNomTiers() {
		return nomTiers;
	}



	public void setNomTiers(String nomTiers) {
		this.nomTiers = nomTiers;
	}



	public String getPrenomTiers() {
		return prenomTiers;
	}



	public void setPrenomTiers(String prenomTiers) {
		this.prenomTiers = prenomTiers;
	}



	public String getLibelleArticle() {
		return libelleArticle;
	}



	public void setLibelleArticle(String libelleArticle) {
		this.libelleArticle = libelleArticle;
	}



	public String getCodeArticle() {
		return codeArticle;
	}



	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}



	public String getCodeDocument() {
		return codeDocument;
	}



	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}



	public Integer getIdServiceExterne() {
		return idServiceExterne;
	}



	public void setIdServiceExterne(Integer idServiceExterne) {
		this.idServiceExterne = idServiceExterne;
	}



	public String getCodeServiceExterne() {
		return codeServiceExterne;
	}



	public void setCodeServiceExterne(String codeServiceExterne) {
		this.codeServiceExterne = codeServiceExterne;
	}



	public String getLibelleServiceExterne() {
		return libelleServiceExterne;
	}



	public void setLibelleServiceExterne(String libelleServiceExterne) {
		this.libelleServiceExterne = libelleServiceExterne;
	}
	public TaTiersDTO getTiersDTO() {
		return tiersDTO;
	}
	public void setTiersDTO(TaTiersDTO tiersDTO) {
		this.tiersDTO = tiersDTO;
	}
	public TaArticleDTO getArticleDTO() {
		return articleDTO;
	}
	public void setArticleDTO(TaArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}
	public String getCodeTPaiement() {
		return codeTPaiement;
	}
	public void setCodeTPaiement(String codeTPaiement) {
		this.codeTPaiement = codeTPaiement;
	}
	public String getLibTPaiement() {
		return libTPaiement;
	}
	public void setLibTPaiement(String libTPaiement) {
		this.libTPaiement = libTPaiement;
	}
	public TaTPaiementDTO getTaTPaiementDTO() {
		return taTPaiementDTO;
	}
	public void setTaTPaiementDTO(TaTPaiementDTO taTPaiementDTO) {
		this.taTPaiementDTO = taTPaiementDTO;
	}





}

