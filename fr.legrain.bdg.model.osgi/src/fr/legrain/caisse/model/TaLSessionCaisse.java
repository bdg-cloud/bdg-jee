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

import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_l_session_caisse")
//@NamedQueries(value = { 
//		@NamedQuery(name=TaLSessionCaisse.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f where f.codeTTva like ? order by f.codeTTva"),
//		@NamedQuery(name=TaLSessionCaisse.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f order by f.codeTTva")	
//})
public class TaLSessionCaisse implements java.io.Serializable {

	private static final long serialVersionUID = -1531693174653959235L;

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaLSessionCaisse.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaLSessionCaisse.findAllTaTTvaLight";
	}
	
	private int idLSessionCaisse;
	private TaSessionCaisse taSessionCaisse;

	private TaTLSessionCaisse taTLSessionCaisse;
	private String libelle;
	private String code;
	private BigDecimal taux;
	private Integer idExt; //id tx tva ou id article ou id TaTPaiement ou ...

	private BigDecimal montantHtSession;
	private BigDecimal montantHtCumulMensuel;
	private BigDecimal montantHtCumulAnnuel;
	private BigDecimal montantHtCumulExercice;

	private BigDecimal montantTtcSession;
	private BigDecimal montantTtcCumulMensuel;
	private BigDecimal montantTtcCumulAnnuel;
	private BigDecimal montantTtcCumulExercice;

	private BigDecimal montantTvaSession;
	private BigDecimal montantTvaCumulMensuel;
	private BigDecimal montantTvaCumulAnnuel;
	private BigDecimal montantTvaCumulExercice;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private String version;
	private Integer versionObj;

	public TaLSessionCaisse() {
	}

	public TaLSessionCaisse(int idLSessionCaisse) {
		this.idLSessionCaisse = idLSessionCaisse;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_l_session_caisse", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_l_session_caisse",table = "ta_l_session_caisse", champEntite="idLSessionCaisse", clazz = TaLSessionCaisse.class)
	public int getIdLSessionCaisse() {
		return this.idLSessionCaisse;
	}

	public void setIdLSessionCaisse(int idTTva) {
		this.idLSessionCaisse = idTTva;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_session_caisse")
	public TaSessionCaisse getTaSessionCaisse() {
		return taSessionCaisse;
	}

	public void setTaSessionCaisse(TaSessionCaisse taSessionCaisse) {
		this.taSessionCaisse = taSessionCaisse;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_t_l_session_caisse")
	public TaTLSessionCaisse getTaTLSessionCaisse() {
		return taTLSessionCaisse;
	}

	public void setTaTLSessionCaisse(TaTLSessionCaisse taTLSessionCaisse) {
		this.taTLSessionCaisse = taTLSessionCaisse;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "taux")
	public BigDecimal getTaux() {
		return taux;
	}

	public void setTaux(BigDecimal taux) {
		this.taux = taux;
	}

	@Column(name = "id_ext")
	public Integer getIdExt() {
		return idExt;
	}

	public void setIdExt(Integer idExt) {
		this.idExt = idExt;
	}

	@Column(name = "montant_ht_session")
	public BigDecimal getMontantHtSession() {
		return montantHtSession;
	}

	public void setMontantHtSession(BigDecimal montant_ht_session) {
		this.montantHtSession = montant_ht_session;
	}

	@Column(name = "montant_ht_cumul_mensuel")
	public BigDecimal getMontantHtCumulMensuel() {
		return montantHtCumulMensuel;
	}

	public void setMontantHtCumulMensuel(BigDecimal montant_ht_cumul_mensuel) {
		this.montantHtCumulMensuel = montant_ht_cumul_mensuel;
	}

	@Column(name = "montant_ht_cumul_annuel")
	public BigDecimal getMontantHtCumulAnnuel() {
		return montantHtCumulAnnuel;
	}

	public void setMontantHtCumulAnnuel(BigDecimal montant_ht_cumul_annuel) {
		this.montantHtCumulAnnuel = montant_ht_cumul_annuel;
	}

	@Column(name = "montant_ht_cumul_exercice")
	public BigDecimal getMontantHtCumulExercice() {
		return montantHtCumulExercice;
	}

	public void setMontantHtCumulExercice(BigDecimal montant_ht_cumul_exercice) {
		this.montantHtCumulExercice = montant_ht_cumul_exercice;
	}

	@Column(name = "montant_ttc_session")
	public BigDecimal getMontantTtcSession() {
		return montantTtcSession;
	}

	public void setMontantTtcSession(BigDecimal montant_ttc_session) {
		this.montantTtcSession = montant_ttc_session;
	}

	@Column(name = "montant_ttc_cumul_mensuel")
	public BigDecimal getMontantTtcCumulMensuel() {
		return montantTtcCumulMensuel;
	}

	public void setMontantTtcCumulMensuel(BigDecimal montant_ttc_cumul_mensuel) {
		this.montantTtcCumulMensuel = montant_ttc_cumul_mensuel;
	}

	@Column(name = "montant_ttc_cumul_annuel")
	public BigDecimal getMontantTtcCumulAnnuel() {
		return montantTtcCumulAnnuel;
	}

	public void setMontantTtcCumulAnnuel(BigDecimal montant_ttc_cumul_annuel) {
		this.montantTtcCumulAnnuel = montant_ttc_cumul_annuel;
	}

	@Column(name = "montant_ttc_cumul_exercice")
	public BigDecimal getMontantTtcCumulExercice() {
		return montantTtcCumulExercice;
	}

	public void setMontantTtcCumulExercice(BigDecimal montant_ttc_cumul_exercice) {
		this.montantTtcCumulExercice = montant_ttc_cumul_exercice;
	}

	@Column(name = "montant_tva_session")
	public BigDecimal getMontantTvaSession() {
		return montantTvaSession;
	}

	public void setMontantTvaSession(BigDecimal montant_tva_session) {
		this.montantTvaSession = montant_tva_session;
	}

	@Column(name = "montant_tva_cumul_mensuel")
	public BigDecimal getMontantTvaCumulMensuel() {
		return montantTvaCumulMensuel;
	}

	public void setMontantTvaCumulMensuel(BigDecimal montant_tva_cumul_mensuel) {
		this.montantTvaCumulMensuel = montant_tva_cumul_mensuel;
	}

	@Column(name = "montant_tva_cumul_annuel")
	public BigDecimal getMontantTvaCumulAnnuel() {
		return montantTvaCumulAnnuel;
	}

	public void setMontantTvaCumulAnnuel(BigDecimal montant_tva_cumul_annuel) {
		this.montantTvaCumulAnnuel = montant_tva_cumul_annuel;
	}

	@Column(name = "montant_tva_cumul_exercice")
	public BigDecimal getMontantTvaCumulExercice() {
		return montantTvaCumulExercice;
	}

	public void setMontantTvaCumulExercice(BigDecimal montant_tva_cumul_exercice) {
		this.montantTvaCumulExercice = montant_tva_cumul_exercice;
	}

}
