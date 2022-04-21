package fr.legrain.article.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.legrain.bdg.model.ModelObject;
import fr.legrain.validator.LgrHibernateValidated;

public class TaLotDTO extends ModelObject implements java.io.Serializable {


	private static final long serialVersionUID = -8710119668549035786L;
	
	private Integer id;
	private Integer idCodeBarre;
	private String codeBarre;
	private String numLot;
	private String libelle;
	private Date dluo;
	private Date dateLot;
	private Integer idArticle;
	private String codeArticle;	
	private String libelleArticle;

	private String codeUnite1;
	private String codeUnite2;
	private String codeUniteRef;
	private Integer idUnite1;
	private Integer idUnite2;
	private Integer idUniteRef;
	private BigDecimal rapport;
	private String liblUnite1;
	private String liblUnite2;
	private String liblUniteRef;
	
	private BigDecimal qteRef;
	private Boolean utiliseDlc;
	
	private Boolean sens;
	private Integer nbDecimal;
	private Boolean termine;
	
	private Boolean matierePremiere=false;
	private Boolean produitFini=false;

	private Boolean virtuel = false;
	private Boolean virtuelUnique = false;
	private Boolean lotConforme = false;
	private Boolean presenceDeNonConformite = false;
	
	private Integer idStatusConformite;
	private String codeStatusConformite;
	private String libelleStatusConformiteLot;	

	private Integer versionObj;

	public TaLotDTO() {
		super();
	}

	public TaLotDTO(String numLot, String libelle, Boolean matierePremiere,
			Boolean produitFini) {
		super();
		this.numLot = numLot;
		this.libelle = libelle;
		this.matierePremiere = matierePremiere;
		this.produitFini = produitFini;
	}

	public TaLotDTO(Integer id, String numLot, String codeArticle,
			String codeUnite1, String codeUnite2, BigDecimal rapport,
			 Integer nbDecimal, Boolean termine, Integer versionObj) {
		super();
		this.id = id;
		this.numLot = numLot;
		this.codeArticle = codeArticle;
		this.codeUnite1 = codeUnite1;
		this.codeUnite2 = codeUnite2;
		this.rapport = rapport;
		this.nbDecimal = nbDecimal;
		this.termine = termine;
		this.versionObj = versionObj;
	}

	public TaLotDTO(Integer id, String numLot, String libelle, String codeArticle,
			String codeUnite1, String codeUnite2, BigDecimal rapport,
			 Integer nbDecimal, Boolean termine, Integer versionObj) {
		super();
		this.id = id;
		this.numLot = numLot;
		this.libelle = libelle;
		this.codeArticle = codeArticle;
		this.codeUnite1 = codeUnite1;
		this.codeUnite2 = codeUnite2;
		this.rapport = rapport;
		this.nbDecimal = nbDecimal;
		this.termine = termine;
		this.versionObj = versionObj;
	}
	
	public TaLotDTO(Integer id, String numLot, Date dateLot, String libelle, String codeArticle,
			/*String codeUnite1, String codeUnite2, BigDecimal rapport,
			 Integer nbDecimal, */Boolean termine, Boolean lotConforme, Boolean presenceDeNonConformite, Integer idStatusConformite,
				String codeStatusConformite,
				String libelleStatusConformiteLot,	Integer versionObj) {
		super();
		this.id = id;
		this.numLot = numLot;
		this.dateLot = dateLot;
		this.libelle = libelle;
		this.codeArticle = codeArticle;
//		this.codeUnite1 = codeUnite1;
//		this.codeUnite2 = codeUnite2;
//		this.rapport = rapport;
//		this.nbDecimal = nbDecimal;
		this.termine = termine;
		this.lotConforme = lotConforme;
		this.presenceDeNonConformite = presenceDeNonConformite;
		this.idStatusConformite = idStatusConformite;
		this.codeStatusConformite = codeStatusConformite;
		this.libelleStatusConformiteLot = libelleStatusConformiteLot;	
		this.versionObj = versionObj;
	}
	
	public TaLotDTO(Integer id, String numLot, Date dateLot, String libelle, String codeArticle,
			Boolean termine, Boolean lotConforme, Boolean presenceDeNonConformite, Integer idStatusConformite,
				String codeStatusConformite,
				String libelleStatusConformiteLot, 
				String codeUniteRef, Integer idUniteRef, String liblUniteRef, BigDecimal qteRef,Boolean utiliseDlc,	Date dluo,
				Integer versionObj) {
		super();
		this.id = id;
		this.numLot = numLot;
		this.dateLot = dateLot;
		this.libelle = libelle;
		this.codeArticle = codeArticle;
		this.termine = termine;
		this.lotConforme = lotConforme;
		this.presenceDeNonConformite = presenceDeNonConformite;
		this.idStatusConformite = idStatusConformite;
		this.codeStatusConformite = codeStatusConformite;
		this.libelleStatusConformiteLot = libelleStatusConformiteLot;
		this.codeUniteRef =  codeUniteRef;
		this.idUniteRef =  idUniteRef;
		this.liblUniteRef =  liblUniteRef;
		this.qteRef = qteRef;
		this.utiliseDlc = 	utiliseDlc;
		this.dluo = dluo;
		this.versionObj = versionObj;
	}

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "numlot",table = "ta_lot", champEntite="numLot", clazz = TaLotDTO.class)
	public String getNumLot() {
		return numLot;
	}

	public void setNumLot(String numLot) {
		firePropertyChange("numLot", this.numLot, this.numLot = numLot);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	@LgrHibernateValidated(champBd = "dluo",table = "ta_lot", champEntite="dluo", clazz = TaLotDTO.class)
	public Date getDluo() {
		return dluo;
	}

	public void setDluo(Date dluo) {
		firePropertyChange("dluo", this.dluo, this.dluo = dluo);
	}

	@LgrHibernateValidated(champBd = "date_lot",table = "ta_lot", champEntite="dateLot", clazz = TaLotDTO.class)
	public Date getDateLot() {
		return dateLot;
	}

	public void setDateLot(Date dateLot) {
		firePropertyChange("dateLot", this.dateLot, this.dateLot = dateLot);
	}

	public Integer getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(Integer idArticle) {
		firePropertyChange("idArticle", this.idArticle, this.idArticle = idArticle);
	}

	@NotNull
	@Size(min=1, max=20)
	@LgrHibernateValidated(champBd = "code_article",table = "ta_article", champEntite="codeArticle", clazz = TaLotDTO.class)
	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		firePropertyChange("codeArticle", this.codeArticle, this.codeArticle = codeArticle);
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		firePropertyChange("versionObj", this.versionObj, this.versionObj = versionObj);
	}

	@LgrHibernateValidated(champBd = "libellec_article",table = "ta_article", champEntite="libelleArticle", clazz = TaLotDTO.class)
	public String getLibelleArticle() {
		return libelleArticle;
	}

	public void setLibelleArticle(String libelleArticle) {
		firePropertyChange("libelleArticle", this.libelleArticle, this.libelleArticle = libelleArticle);
	}

	public Integer getIdCodeBarre() {
		return idCodeBarre;
	}

	public void setIdCodeBarre(Integer idCodeBarre) {
		firePropertyChange("idCodeBarre", this.idCodeBarre, this.idCodeBarre = idCodeBarre);
	}

	@LgrHibernateValidated(champBd = "code_barre",table = "ta_code_barre", champEntite="codeBarre", clazz = TaLotDTO.class)
	public String getCodeBarre() {
		return codeBarre;
	}

	public void setCodeBarre(String codeBarre) {
		firePropertyChange("codeBarre", this.codeBarre, this.codeBarre = codeBarre);
	}

	@Size(min=0, max=20)
	@LgrHibernateValidated(champBd = "code_unite",table = "ta_unite", champEntite="codeUnite", clazz = TaLotDTO.class)
	public String getCodeUnite1() {
		return codeUnite1;
	}

	public void setCodeUnite1(String codeUnite1) {
		firePropertyChange("codeUnite1", this.codeUnite1, this.codeUnite1 = codeUnite1);
	}

	@Size(min=0, max=20)
	@LgrHibernateValidated(champBd = "code_unite",table = "ta_unite", champEntite="codeUnite", clazz = TaLotDTO.class)
	public String getCodeUnite2() {
		return codeUnite2;
	}

	public void setCodeUnite2(String codeUnite2) {
		firePropertyChange("codeUnite2", this.codeUnite2, this.codeUnite2 = codeUnite2);
	}

	public Integer getIdUnite1() {
		return idUnite1;
	}

	public void setIdUnite1(Integer idUnite1) {
		firePropertyChange("idUnite1", this.idUnite1, this.idUnite1 = idUnite1);
	}

	public Integer getIdUnite2() {
		return idUnite2;
	}

	public void setIdUnite2(Integer idUnite2) {
		firePropertyChange("idUnite2", this.idUnite2, this.idUnite2 = idUnite2);
	}

	public BigDecimal getRapport() {
		return rapport;
	}

	public void setRapport(BigDecimal rapport) {
		firePropertyChange("rapport", this.rapport, this.rapport = rapport);
	}

	public String getLiblUnite1() {
		return liblUnite1;
	}

	public void setLiblUnite1(String liblUnite1) {
		firePropertyChange("liblUnite1", this.liblUnite1, this.liblUnite1 = liblUnite1);
	}

	public String getLiblUnite2() {
		return liblUnite2;
	}

	public void setLiblUnite2(String liblUnite2) {
		firePropertyChange("liblUnite2", this.liblUnite2, this.liblUnite2 = liblUnite2);
	}

	public Boolean getSens() {
		return sens;
	}

	public void setSens(Boolean sens) {
		firePropertyChange("sens", this.sens, this.sens = sens);
	}

	public Integer getNbDecimal() {
		return nbDecimal;
	}

	public void setNbDecimal(Integer nbDecimal) {
		firePropertyChange("nbDecimal", this.nbDecimal, this.nbDecimal = nbDecimal);
	}

	public Boolean getTermine() {
		return termine;
	}

	public void setTermine(Boolean termine) {
		firePropertyChange("termine", this.termine, this.termine = termine);
	}

	@LgrHibernateValidated(champBd = "libelle",table = "ta_lot", champEntite="libelle", clazz = TaLotDTO.class)
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		firePropertyChange("libelle", this.libelle, this.libelle = libelle);
	}

	public Boolean getMatierePremiere() {
		return matierePremiere;
	}

	public void setMatierePremiere(Boolean matierePremiere) {
		firePropertyChange("matierePremiere", this.matierePremiere, this.matierePremiere = matierePremiere);
	}

	public Boolean getProduitFini() {
		return produitFini;
	}

	public void setProduitFini(Boolean produitFini) {
		firePropertyChange("produitFini", this.produitFini, this.produitFini = produitFini);
	}

	public Boolean getVirtuel() {
		return virtuel;
	}

	public void setVirtuel(Boolean virtuel) {
		firePropertyChange("virtuel", this.virtuel, this.virtuel = virtuel);
	}

	public Boolean getVirtuelUnique() {
		return virtuelUnique;
	}

	public void setVirtuelUnique(Boolean virtuelUnique) {
		firePropertyChange("virtuelUnique", this.virtuelUnique, this.virtuelUnique = virtuelUnique);
	}
	
	public Boolean getLotConforme() {
		return lotConforme;
	}

	public void setLotConforme(Boolean lotConforme) {
		this.lotConforme = lotConforme;
	}

	public Boolean getPresenceDeNonConformite() {
		return presenceDeNonConformite;
	}

	public void setPresenceDeNonConformite(Boolean presenceDeNonConformite) {
		this.presenceDeNonConformite = presenceDeNonConformite;
	}

	public int getIdStatusConformite() {
		return idStatusConformite;
	}

	public void setIdStatusConformite(int idStatusConformite) {
		this.idStatusConformite = idStatusConformite;
	}

	public String getCodeStatusConformite() {
		return codeStatusConformite;
	}

	public void setCodeStatusConformite(String codeStatusConformite) {
		this.codeStatusConformite = codeStatusConformite;
	}

	public String getLibelleStatusConformiteLot() {
		return libelleStatusConformiteLot;
	}

	public void setLibelleStatusConformiteLot(String libelleStatusConformiteLot) {
		this.libelleStatusConformiteLot = libelleStatusConformiteLot;
	}

	public String getCodeUniteRef() {
		return codeUniteRef;
	}

	public void setCodeUniteRef(String codeUniteRef) {
		this.codeUniteRef = codeUniteRef;
	}

	public Integer getIdUniteRef() {
		return idUniteRef;
	}

	public void setIdUniteRef(Integer idUniteRef) {
		this.idUniteRef = idUniteRef;
	}

	public String getLiblUniteRef() {
		return liblUniteRef;
	}

	public void setLiblUniteRef(String liblUniteRef) {
		this.liblUniteRef = liblUniteRef;
	}

	public BigDecimal getQteRef() {
		return qteRef;
	}

	public void setQteRef(BigDecimal qteRef) {
		this.qteRef = qteRef;
	}

	public Boolean getUtiliseDlc() {
		return utiliseDlc;
	}

	public void setUtiliseDlc(Boolean utiliseDlc) {
		this.utiliseDlc = utiliseDlc;
	}

	public void setIdStatusConformite(Integer idStatusConformite) {
		this.idStatusConformite = idStatusConformite;
	}

}
