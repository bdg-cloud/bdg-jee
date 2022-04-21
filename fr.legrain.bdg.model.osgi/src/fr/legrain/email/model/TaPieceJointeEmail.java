package fr.legrain.email.model;

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
@Table(name = "ta_piece_jointe_email")
public class TaPieceJointeEmail implements java.io.Serializable {

	private static final long serialVersionUID = -841985444192109353L;

	private int idLogPieceJointeEmail;
	
	private TaMessageEmail taMessageEmail;
	private String nomFichier;
	private Integer taille;
	private String typeMime;
	private byte[] fichier;
	private byte[] fichierImageMiniature;
	
	private String version;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
		
	public TaPieceJointeEmail() {
	}

	public TaPieceJointeEmail(int idLogPaiementInternet) {
		this.idLogPieceJointeEmail = idLogPaiementInternet;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_piece_jointe_email", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_piece_jointe_email",table = "ta_piece_jointe_email",champEntite="idLogPieceJointeEmail", clazz = TaPieceJointeEmail.class)
	public int getIdLogPieceJointeEmail() {
		return this.idLogPieceJointeEmail;
	}

	public void setIdLogPieceJointeEmail(int idLogPaiementInternet) {
		this.idLogPieceJointeEmail = idLogPaiementInternet;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_email")
//	@XmlElement
//	@XmlInverseReference(mappedBy="taAdresses")
	public TaMessageEmail getTaMessageEmail() {
		return taMessageEmail;
	}

	public void setTaMessageEmail(TaMessageEmail taLogEmail) {
		this.taMessageEmail = taLogEmail;
	}

	@Column(name = "nom_fichier")
	public String getNomFichier() {
		return nomFichier;
	}

	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

	@Column(name = "taille")
	public Integer getTaille() {
		return taille;
	}

	public void setTaille(Integer taille) {
		this.taille = taille;
	}

	@Column(name = "type_mime")
	public String getTypeMime() {
		return typeMime;
	}

	public void setTypeMime(String typeMime) {
		this.typeMime = typeMime;
	}

	@Column(name = "fichier")
	public byte[] getFichier() {
		return fichier;
	}

	public void setFichier(byte[] fichier) {
		this.fichier = fichier;
	}

	@Column(name = "fichier_miniature")
	public byte[] getFichierImageMiniature() {
		return fichierImageMiniature;
	}

	public void setFichierImageMiniature(byte[] fichierImageMiniature) {
		this.fichierImageMiniature = fichierImageMiniature;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeAdresse) {
		this.quiCree = quiCreeAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeAdresse) {
		this.quandCree = quandCreeAdresse;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifAdresse) {
		this.quiModif = quiModifAdresse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifAdresse) {
		this.quandModif = quandModifAdresse;
	}
	

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}
}
