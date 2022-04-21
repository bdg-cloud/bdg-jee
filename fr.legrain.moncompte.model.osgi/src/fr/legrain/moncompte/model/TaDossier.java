package fr.legrain.moncompte.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "ta_dossier")
@NamedQueries(value = { 
		@NamedQuery(name=TaDossier.QN.FIND_BY_CODE, query="select f from TaDossier f where f.code= :code"),
		@NamedQuery(name=TaDossier.QN.FIND_ALL_LIGHT, query="select new fr.legrain.moncompte.dto.TaDossierDTO(d.id,d.code,d.description,d.actif,d.nbUtilisateur,d.nbPosteInstalle,d.accesWs,c.nom,c.code) from TaDossier d join d.taClient c order by d.code"),
		})
@XmlAccessorType(XmlAccessType.FIELD)
public class TaDossier implements Serializable {
	
	private static final long serialVersionUID = -569443777253534183L;

	public static class QN {
		public static final String FIND_BY_CODE = "TaDossier.findByCode";
		public static final String FIND_ALL_LIGHT = "TaDossier.findAllLight";
	}
	
	public void afterUnmarshal(Object target, Object parent) {
		System.out.println("TaDossier");
	}

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="code",unique=true)
	private String code;
	
	@Column(name="description")
	private String description;
	
	@Column(name="actif")
	private boolean actif;
	
	@Column(name="mode_locatif")
	private Boolean modeLocatif;
	
	@Column(name="nb_utilisateur")
	private Integer nbUtilisateur;
	
	@Column(name="nb_poste_installe")
	private Integer nbPosteInstalle;
	
	@Column(name="acces_ws")
	private Boolean accesWs;
	
	@Column(name="code_revendeur")
	private String codePartenaire;
	
	@Column(name="taux_reduction")
	private BigDecimal tauxReduction;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_client", referencedColumnName="id")
	@XmlElement
	@XmlInverseReference(mappedBy="listeDossier")
	private TaClient taClient;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taDossier"/*, orphanRemoval=true*/)
	@Fetch(FetchMode.SUBSELECT)
	@XmlElement
	private List<TaAutorisation> listeAutorisation;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taDossier"/*, orphanRemoval=true*/)
	@Fetch(FetchMode.SUBSELECT)
	@XmlElement
	private List<TaPrixPerso> listePrixPerso;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taDossier"/*, orphanRemoval=true*/)
	@Fetch(FetchMode.SUBSELECT)
	private List<TaPrixParUtilisateurPerso> listePrixParUtilisateurPerso;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_categorie_pro")
	public TaCategoriePro taCategoriePro;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_niveau")
	private TaTNiveau taTNiveau;
	
	@Column(name="quand_cree")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandCree;
	
	@Column(name="quand_modif")
	@Temporal(TemporalType.TIMESTAMP)
	private Date quandModif;
	
	@Column(name="qui_cree")
	private String quiCree;
	
	@Column(name="qui_modif")
	private String quiModif;
	
//	@Column(name = "ip_acces")
//	private String ipAcces;
	
	@Version
	@Column(name = "version_obj")
	private Integer versionObj;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getQuandCree() {
		return quandCree;
	}

	public Date getQuandModif() {
		return quandModif;
	}

	public String getQuiCree() {
		return quiCree;
	}

	public String getQuiModif() {
		return quiModif;
	}

	public Integer getVersionObj() {
		return versionObj;
	}

	public void setQuandCree(Date quanCree) {
		this.quandCree = quanCree;
	}

	public void setQuandModif(Date quandModif) {
		this.quandModif = quandModif;
	}

	public void setQuiCree(String quiCree) {
		this.quiCree = quiCree;
	}

	public void setQuiModif(String quiModif) {
		this.quiModif = quiModif;
	}


	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<TaAutorisation> getListeAutorisation() {
		return listeAutorisation;
	}

	public void setListeAutorisation(List<TaAutorisation> listeAutorisation) {
		this.listeAutorisation = listeAutorisation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getActif() {
		return actif;
	}

	public void setActif(boolean actif) {
		this.actif = actif;
	}

	public TaClient getTaClient() {
		return taClient;
	}

	public void setTaClient(TaClient taClient) {
		this.taClient = taClient;
	}

	public List<TaPrixPerso> getListePrixPerso() {
		return listePrixPerso;
	}

	public void setListePrixPerso(List<TaPrixPerso> listePrixPerso) {
		this.listePrixPerso = listePrixPerso;
	}

	public List<TaPrixParUtilisateurPerso> getListePrixParUtilisateurPerso() {
		return listePrixParUtilisateurPerso;
	}

	public void setListePrixParUtilisateurPerso(
			List<TaPrixParUtilisateurPerso> listePrixParUtilisateurPerso) {
		this.listePrixParUtilisateurPerso = listePrixParUtilisateurPerso;
	}

	public Boolean getModeLocatif() {
		return modeLocatif;
	}

	public void setModeLocatif(Boolean modeLocatif) {
		this.modeLocatif = modeLocatif;
	}

	public Integer getNbUtilisateur() {
		return nbUtilisateur;
	}

	public void setNbUtilisateur(Integer nbUtilisateur) {
		this.nbUtilisateur = nbUtilisateur;
	}

	public Integer getNbPosteInstalle() {
		return nbPosteInstalle;
	}

	public void setNbPosteInstalle(Integer nbPosteInstalle) {
		this.nbPosteInstalle = nbPosteInstalle;
	}

	public Boolean getAccesWs() {
		return accesWs;
	}

	public void setAccesWs(Boolean accesWs) {
		this.accesWs = accesWs;
	}

	public String getCodePartenaire() {
		return codePartenaire;
	}

	public void setCodePartenaire(String codePartenaire) {
		this.codePartenaire = codePartenaire;
	}

	public BigDecimal getTauxReduction() {
		return tauxReduction;
	}

	public void setTauxReduction(BigDecimal tauxReduction) {
		this.tauxReduction = tauxReduction;
	}
	
	public TaCategoriePro getTaCategoriePro() {
		return taCategoriePro;
	}

	public void setTaCategoriePro(TaCategoriePro taCategoriePro) {
		this.taCategoriePro = taCategoriePro;
	}
	
	public TaTNiveau getTaTNiveau() {
		return taTNiveau;
	}

	public void setTaTNiveau(TaTNiveau taTNiveau) {
		this.taTNiveau = taTNiveau;
	}
}
