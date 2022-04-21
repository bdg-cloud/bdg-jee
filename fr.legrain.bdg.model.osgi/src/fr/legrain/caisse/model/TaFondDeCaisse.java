package fr.legrain.caisse.model;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_fond_de_caisse")
@NamedQueries(value = { 
		@NamedQuery(name=TaFondDeCaisse.QN.FIND_BY_SESSION_CAISSE_COURANTE, query="select f from TaFondDeCaisse f where f.taSessionCaisse is null")
//		@NamedQuery(name=TaFondDeCaisse.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f where f.codeTTva like ? order by f.codeTTva"),
//		@NamedQuery(name=TaFondDeCaisse.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f order by f.codeTTva")	
})
public class TaFondDeCaisse implements java.io.Serializable {

	private static final long serialVersionUID = 2737328011387161536L;

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaFondDeCaisse.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaFondDeCaisse.findAllTaTTvaLight";
		
		public static final String FIND_BY_SESSION_CAISSE_COURANTE = "TaFondDeCaisse.findBySessionCaisseCourante";
	}
	
	private int idFondDeCaisse;

	private String numeroCaisse;
	private TaUtilisateur taUtilisateur;
	private Date date;

	private TaTFondDeCaisse taTFondDeCaisse;
	private String libelle;
	private BigDecimal montant;

	private TaSessionCaisse taSessionCaisse;

	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private String version;
	private Integer versionObj;

	public TaFondDeCaisse() {
	}

	public TaFondDeCaisse(int idFondDeCaisse) {
		this.idFondDeCaisse = idFondDeCaisse;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_fond_de_caisse", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_fond_de_caisse",table = "ta_fond_de_caisse", champEntite="idFondDeCaisse", clazz = TaFondDeCaisse.class)
	public int getIdFondDeCaisse() {
		return this.idFondDeCaisse;
	}

	public void setIdFondDeCaisse(int idTTva) {
		this.idFondDeCaisse = idTTva;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Version
	@Column(name = "version_obj", precision = 15)
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeTTva) {
		this.quiCree = quiCreeTTva;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeTTva) {
		this.quandCree = quandCreeTTva;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifTTva) {
		this.quiModif = quiModifTTva;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifTTva) {
		this.quandModif = quandModifTTva;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Column(name = "numero_caisse")
	public String getNumeroCaisse() {
		return numeroCaisse;
	}

	public void setNumeroCaisse(String numero_caisse) {
		this.numeroCaisse = numero_caisse;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_vendeur")
	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur id_vendeur) {
		this.taUtilisateur = id_vendeur;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_t_fond_de_caisse")
	public TaTFondDeCaisse getTaTFondDeCaisse() {
		return taTFondDeCaisse;
	}

	public void setTaTFondDeCaisse(TaTFondDeCaisse taTFondDeCaisse) {
		this.taTFondDeCaisse = taTFondDeCaisse;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "montant")
	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_session_caisse")
	public TaSessionCaisse getTaSessionCaisse() {
		return taSessionCaisse;
	}

	public void setTaSessionCaisse(TaSessionCaisse taClotureCaisse) {
		this.taSessionCaisse = taClotureCaisse;
	}

}
