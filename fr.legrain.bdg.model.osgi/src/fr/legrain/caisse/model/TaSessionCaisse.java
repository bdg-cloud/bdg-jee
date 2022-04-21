package fr.legrain.caisse.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.Valid;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.document.model.TaLFacture;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_session_caisse")
//@NamedQueries(value = { 
//		@NamedQuery(name=TaSessionCaisse.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f where f.codeTTva like ? order by f.codeTTva"),
//		@NamedQuery(name=TaSessionCaisse.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaTTvaDTO(f.idTTva,f.codeTTva,f.libTTva) from TaTTva f order by f.codeTTva")	
//})
public class TaSessionCaisse implements java.io.Serializable {
	
	private static final long serialVersionUID = -411972364025316266L;

	public static class QN {
		public static final String FIND_BY_CODE_LIGHT = "TaSessionCaisse.findByCodeLight";
		public static final String FIND_ALL_LIGHT = "TaSessionCaisse.findAllTaTTvaLight";
	}
	
	private int idSessionCaisse;
	
	private String codeSessionCaisse;
	private String numeroCaisse;
	private TaUtilisateur taUtilisateur;
	private Date dateSession;
	private Date dateDebutSession;
	private Date dateFinSession;
	private String libelle;
	private Boolean automatique;
	private Boolean verrouillageTicketZ;
  
	private BigDecimal montantFondDeCaisseOuverture;
	private TaFondDeCaisse taFondDeCaisseOuverture;

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

	private BigDecimal montantRemiseSession;

	private BigDecimal montantFondDeCaisseCloture;
	private TaFondDeCaisse taFondDeCaisseCloture;
	
	private List<TaLSessionCaisse> lignes;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private String version;
	private Integer versionObj;

	public TaSessionCaisse() {
	}

	public TaSessionCaisse(int idSessionCaisse) {
		this.idSessionCaisse = idSessionCaisse;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_session_caisse", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_session_caisse",table = "ta_session_caisse", champEntite="idSessionCaisse", clazz = TaSessionCaisse.class)
	public int getIdSessionCaisse() {
		return this.idSessionCaisse;
	}

	public void setIdSessionCaisse(int idTTva) {
		this.idSessionCaisse = idTTva;
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

	@Column(name = "code_session_caisse")
	public String getCodeSessionCaisse() {
		return codeSessionCaisse;
	}

	public void setCodeSessionCaisse(String code_session_caisse) {
		this.codeSessionCaisse = code_session_caisse;
	}

	@Column(name = "numero_caisse")
	public String getNumeroCaisse() {
		return numeroCaisse;
	}

	public void setNumeroCaisse(String numero_caisse) {
		this.numeroCaisse = numero_caisse;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_vendeur")
	public TaUtilisateur getTaUtilisateur() {
		return taUtilisateur;
	}

	public void setTaUtilisateur(TaUtilisateur id_vendeur) {
		this.taUtilisateur = id_vendeur;
	}

	@Column(name = "date_session")
	public Date getDateSession() {
		return dateSession;
	}

	public void setDateSession(Date date_session) {
		this.dateSession = date_session;
	}

	@Column(name = "date_debut_session")
	public Date getDateDebutSession() {
		return dateDebutSession;
	}

	public void setDateDebutSession(Date date_debut_session) {
		this.dateDebutSession = date_debut_session;
	}

	@Column(name = "date_fin_session")
	public Date getDateFinSession() {
		return dateFinSession;
	}

	public void setDateFinSession(Date date_fin_session) {
		this.dateFinSession = date_fin_session;
	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "automatique")
	public Boolean getAutomatique() {
		return automatique;
	}

	public void setAutomatique(Boolean automatique) {
		this.automatique = automatique;
	}

	@Column(name = "montant_fond_de_caisse_ouverture")
	public BigDecimal getMontantFondDeCaisseOuverture() {
		return montantFondDeCaisseOuverture;
	}

	public void setMontantFondDeCaisseOuverture(BigDecimal montant_fond_de_caisse_ouverture) {
		this.montantFondDeCaisseOuverture = montant_fond_de_caisse_ouverture;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_fond_de_caisse_ouverture")
	public TaFondDeCaisse getTaFondDeCaisseOuverture() {
		return taFondDeCaisseOuverture;
	}

	public void setTaFondDeCaisseOuverture(TaFondDeCaisse id_fond_de_caisse_session) {
		this.taFondDeCaisseOuverture = id_fond_de_caisse_session;
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

	@Column(name = "montant_remise_session")
	public BigDecimal getMontantRemiseSession() {
		return montantRemiseSession;
	}

	public void setMontantRemiseSession(BigDecimal montant_remise_session) {
		this.montantRemiseSession = montant_remise_session;
	}

	@Column(name = "montant_fond_de_caisse_cloture")
	public BigDecimal getMontantFondDeCaisseCloture() {
		return montantFondDeCaisseCloture;
	}

	public void setMontantFondDeCaisseCloture(BigDecimal montant_fond_de_caisse_session) {
		this.montantFondDeCaisseCloture = montant_fond_de_caisse_session;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_fond_de_caisse_cloture")
	public TaFondDeCaisse getTaFondDeCaisseCloture() {
		return taFondDeCaisseCloture;
	}

	public void setTaFondDeCaisseCloture(TaFondDeCaisse id_fond_de_caisse_session) {
		this.taFondDeCaisseCloture = id_fond_de_caisse_session;
	}

	@Column(name = "verrouillage_ticket_z")
	public Boolean getVerrouillageTicketZ() {
		return verrouillageTicketZ;
	}

	public void setVerrouillageTicketZ(Boolean verrouillageTicketZ) {
		this.verrouillageTicketZ = verrouillageTicketZ;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taSessionCaisse", orphanRemoval=true)
	//@OrderBy("numLigneLDocument")
	@Fetch(FetchMode.SUBSELECT)
	@Valid
	public List<TaLSessionCaisse> getLignes() {
		return this.lignes;
	}

	public void setLignes(List<TaLSessionCaisse> lignes) {
		this.lignes = lignes;
	}
}
