package fr.legrain.tiers.model;

// Generated Mar 25, 2009 10:06:49 AM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

/**
 * Utiliser de préférence TaTMoyenPaiement pour les nouvelles classes
 */
@Deprecated
@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_t_banque")
//@SequenceGenerator(name = "gen_t_banque", sequenceName = "num_id_t_banque", allocationSize = 1)
public class TaTBanque extends TaObjetLgr   implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3588016151879074643L;
	private int idTBanque;
//	private String version;
	private String codeTBanque;
	private String liblTBanque;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
//	private Set<TaCompteBanque> taCompteBanques = new HashSet<TaCompteBanque>(0);

	public TaTBanque() {
	}

	public TaTBanque(int idTBanque) {
		this.idTBanque = idTBanque;
	}

	public TaTBanque(int idTBanque, String codeTBanque, String liblTBanque,
			String quiCreeTBanque, Date quandCreeTBanque,
			String quiModifTBanque, Date quandModifTBanque, String ipAcces,
			Integer versionObj/*, Set<TaCompteBanque> taCompteBanques*/) {
		this.idTBanque = idTBanque;
		this.codeTBanque = codeTBanque;
		this.liblTBanque = liblTBanque;
		this.quiCree = quiCreeTBanque;
		this.quandCree = quandCreeTBanque;
		this.quiModif = quiModifTBanque;
		this.quandModif = quandModifTBanque;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
//		this.taCompteBanques = taCompteBanques;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_t_banque", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_t_banque",table = "ta_t_banque",champEntite="idTBanque", clazz = TaTBanque.class)
	public int getIdTBanque() {
		return this.idTBanque;
	}

	public void setIdTBanque(int idTBanque) {
		this.idTBanque = idTBanque;
	}

	
//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	@Column(name = "code_t_banque", length = 20)
	@LgrHibernateValidated(champBd = "code_t_banque",table = "ta_t_banque",champEntite="codeTBanque",clazz = TaTBanque.class)
	public String getCodeTBanque() {
		return this.codeTBanque;
	}

	public void setCodeTBanque(String codeTBanque) {
		this.codeTBanque = codeTBanque;
	}

	@Column(name = "libl_t_banque", length = 100)
	@LgrHibernateValidated(champBd = "libl_t_banque",table = "ta_t_banque",champEntite="liblTBanque",clazz = TaTBanque.class)
	public String getLiblTBanque() {
		return this.liblTBanque;
	}

	public void setLiblTBanque(String liblTBanque) {
		this.liblTBanque = liblTBanque;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeTBanque) {
//		this.quiCree = quiCreeTBanque;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeTBanque) {
//		this.quandCree = quandCreeTBanque;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifTBanque) {
//		this.quiModif = quiModifTBanque;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifTBanque) {
//		this.quandModif = quandModifTBanque;
//	}
//
//	@Column(name = "ip_acces", length = 50)
//	public String getIpAcces() {
//		return this.ipAcces;
//	}
//
//	public void setIpAcces(String ipAcces) {
//		this.ipAcces = ipAcces;
//	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

//	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "taTBanque")
//	public Set<TaCompteBanque> getTaCompteBanques() {
//		return this.taCompteBanques;
//	}
//
//	public void setTaCompteBanques(Set<TaCompteBanque> taCompteBanques) {
//		this.taCompteBanques = taCompteBanques;
//	}


//	@PrePersist
//	@PreUpdate
	public void beforePost ()throws Exception{
		this.setCodeTBanque(codeTBanque.toUpperCase());
	}
}
