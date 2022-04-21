package fr.legrain.document.model;

// Generated Apr 7, 2009 3:27:23 PM by Hibernate Tools 3.2.0.CR1

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.document.dto.IRelationDoc;
import fr.legrain.validator.LgrHibernateValidated;


@Entity

@Table(name = "ta_r_reglement_liaison")
//@NamedQueries(value = { 
//		@NamedQuery(name=TaRReglementLiaison.QN.FIND_BY_REGLEMENT_DTO, query="select new fr.legrain.document.dto.TaRReglementLiaisonDTO(rr.id,f.idDocument, f.codeDocument, r.idDocument, r.codeDocument,t.idTiers,t.codeTiers,t.nomTiers,f.libelleDocument,"
//				+ "f.dateDocument,f.netTtcCalc,(select coalesce(sum(r.affectation),0)from TaRReglementLiaison r where r.taFacture=f )as sommeReglement,(select coalesce(sum(r.affectation),0)from TaRAvoir r where r.taFacture=f )as sommeAvoir, rr.affectation,rr.dateExport,rr.dateVerrouillage, mad.accessibleSurCompteClient, mad.envoyeParEmail, mad.imprimePourClient) "
//				+ "from TaRReglementLiaison rr join rr.taReglement r join rr.taFacture f join r.taTiers t   left join rr.taMiseADisposition mad   where r.codeDocument like :codeDoc and t.codeTiers like :codeTiers order by r.id")
//})
public class TaRReglementLiaison implements java.io.Serializable,IRelationDoc {

	private static final long serialVersionUID = 1117345144579079000L;
	
	public static class QN {
		public static final String FIND_BY_REGLEMENT_DTO = "TaRReglementLiaison.findByReglementDTO";
	}
	
	private int id;
	private String version;
	private TaReglement taReglement;
	private TaFacture taFacture;
	private TaTicketDeCaisse taTicketDeCaisse;
	private TaBoncde taBoncde;
	private TaBoncdeAchat taBoncdeAchat;
	private TaAcompte taAcompte;
	private TaAvisEcheance taAvisEcheance;
	private TaAbonnement taAbonnement;
	private TaBonReception taBonReception;
	private TaPanier taPanier;
	private TaPrelevement taPrelevement;
	private TaProforma taProforma;
	private TaPreparation taPreparation;
	private TaDevis taDevis;
	private TaApporteur taApporteur;
	private TaAvoir taAvoir;
	private TaBonliv taBonliv;
	private BigDecimal affectation  = new BigDecimal(0);
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;
	@Transient
	private Boolean accepte=true;
		
	private Integer etat = 0;
	private TaMiseADisposition taMiseADisposition;
	private Date dateExport;
	private Date dateVerrouillage;
	
    @Transient
	private int EtatDeSuppression =0;
	public static final String TYPE_DOC = "RReglementLiaison";
	@Transient
	private String typeDocument;
	
	
	
	public TaRReglementLiaison() {
	}

	public TaRReglementLiaison(int idRAcompte) {
		this.id = idRAcompte;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int idRReglement) {
		this.id = idRReglement;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
//	CascadeType.PERSIST, CascadeType.MERGE, *** enlever car probleme avec remise, les rAvoir et Racompte etaient
	//déjà comme cela. Voir si pas de problème avec les modifications dans les réglements !!!
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_reglement" )
	@OrderBy("codeDocument")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaReglement getTaReglement() {
		return this.taReglement;
	}


	
	public void setTaReglement(TaReglement taReglement) {
		this.taReglement = taReglement;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_facture")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaFacture getTaFacture() {
		return this.taFacture;
	}

	public void setTaFacture(TaFacture taFacture) {
		this.taFacture = taFacture;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_ticket_caisse")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaTicketDeCaisse getTaTicketDeCaisse() {
		return taTicketDeCaisse;
	}

	public void setTaTicketDeCaisse(TaTicketDeCaisse taTicketDeCaisse) {
		this.taTicketDeCaisse = taTicketDeCaisse;
	}




	
	@Column(name = "affectation", precision = 15)
	public BigDecimal getAffectation() {
		return affectation ;
	}

	public void setAffectation(BigDecimal affectation) {
		this.affectation = affectation;
	}


	

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeRDocument) {
		this.quiCree = quiCreeRDocument;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeRDocument) {
		this.quandCree = quandCreeRDocument;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifRDocument) {
		this.quiModif = quiModifRDocument;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifRDocument) {
		this.quandModif = quandModifRDocument;
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
	
	public BigDecimal calculAffectationEnCours(TaFacture taDocument){
		if(this.getId()==0)
			return getAffectation();
			else
		return BigDecimal.valueOf(0);
	}

	@Transient
	public int getEtatDeSuppression() {
		return EtatDeSuppression;
	}
	
	@Transient
	public void setEtatDeSuppression(int etatDeSuppression) {
		EtatDeSuppression = etatDeSuppression;
	}

	@Transient
	public Boolean getAccepte() {
		return accepte;
	}

	@Transient
	public void setAccepte(Boolean accepte) {
		this.accepte = accepte;
	}
	
	@Column(name = "etat")
	public Integer getEtat() {
		return etat;
	}

	public void setEtat(Integer etat) {
		this.etat = etat;
	}
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL} , orphanRemoval=true )
	@JoinColumn(name = "id_mise_a_disposition")
	public TaMiseADisposition getTaMiseADisposition() {
		return taMiseADisposition;
	}

	public void setTaMiseADisposition(TaMiseADisposition taMiseADisposition) {
		this.taMiseADisposition = taMiseADisposition;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_export", length = 19)
	public Date getDateExport() {
		return dateExport;
	}

	public void setDateExport(Date dateExport) {
		this.dateExport = dateExport;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_verrouillage", length = 19)
	public Date getDateVerrouillage() {
		return dateVerrouillage;
	}

	public void setDateVerrouillage(Date dateVerrouillage) {
		this.dateVerrouillage = dateVerrouillage;
	}
	




	@PreRemove
	public void verifRemove()throws Exception{
		if(this.getTaFacture()!=null ){
			if(this.getTaFacture().getTaTiers().getIdTiers()!=this.getTaReglement().getTaTiers().getIdTiers()){
				throw new Exception("verifRemove");
			}
		}
	}

	
	@PrePersist
	public void verifPersist() throws Exception{

		
	}
	
//	@PreUpdate
	public void verifUpdate() throws Exception{
		if(this.getTaFacture()!=null ){
			if(this.getTaFacture().getTaTiers().getIdTiers()!=this.getTaReglement().getTaTiers().getIdTiers()){
				throw new Exception("verifUpdate");
			}
		}
//		if(this.getTaFacture().getRegleCompletDocument(this).add(this.affectation).compareTo(this.getTaFacture().getNetAPayer())>0){
//			throw new Exception("Total affectation supérieure à la facture");
//		}
//		if(this.getTaReglement().calculAffectationTotale(this).add(this.affectation).compareTo(this.getTaReglement().getNetTtcCalc())>0){
//			throw new Exception("Total affectation supérieure au règlement");
//		}
	}

	
	public boolean estDifferent(TaRReglementLiaison old) {
		if(old.equalsTraca(this))return true;
		return false;
	}

	@Transient
	public String getTypeDocument() {
		return TYPE_DOC;
	}


	public void setTypeDocument(String typeDocument) {
		this.typeDocument=typeDocument;
	}

	
	public boolean equalsTraca(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaRReglementLiaison other = (TaRReglementLiaison) obj;
		if (affectation == null) {
			if (other.affectation != null)
				return false;
		} else if (!affectation.equals(other.affectation))
			return false;
		if (etat == null) {
			if (other.etat != null)
				return false;
		} else if (!etat.equals(other.etat))
			return false;
		if (dateExport == null) {
			if (other.dateExport != null)
				return false;
		} else if (!dateExport.equals(other.dateExport))
			return false;
		if (taFacture == null) {
			if (other.taFacture != null)
				return false;
		} else if (!taFacture.equals(other.taFacture))
			return false;
		if (taReglement == null) {
			if (other.taReglement != null)
				return false;
		} else if (!taReglement.equals(other.taReglement))
			return false;
		return true;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_boncde")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaBoncde getTaBoncde() {
		return taBoncde;
	}

	public void setTaBoncde(TaBoncde taBoncde) {
		this.taBoncde = taBoncde;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_boncde_achat")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaBoncdeAchat getTaBoncdeAchat() {
		return taBoncdeAchat;
	}

	public void setTaBoncdeAchat(TaBoncdeAchat taBoncdeAchat) {
		this.taBoncdeAchat = taBoncdeAchat;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_acompte")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaAcompte getTaAcompte() {
		return taAcompte;
	}

	public void setTaAcompte(TaAcompte taAcompte) {
		this.taAcompte = taAcompte;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avis_echeance")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaAvisEcheance getTaAvisEcheance() {
		return taAvisEcheance;
	}

	public void setTaAvisEcheance(TaAvisEcheance taAvisEcheance) {
		this.taAvisEcheance = taAvisEcheance;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_abonnement")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaAbonnement getTaAbonnement() {
		return taAbonnement;
	}

	public void setTaAbonnement(TaAbonnement taAbonnement) {
		this.taAbonnement = taAbonnement;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bon_reception")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaBonReception getTaBonReception() {
		return taBonReception;
	}

	public void setTaBonReception(TaBonReception taBonReception) {
		this.taBonReception = taBonReception;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_panier")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaPanier getTaPanier() {
		return taPanier;
	}

	public void setTaPanier(TaPanier taPanier) {
		this.taPanier = taPanier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_prelevement")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaPrelevement getTaPrelevement() {
		return taPrelevement;
	}

	public void setTaPrelevement(TaPrelevement taPrelevement) {
		this.taPrelevement = taPrelevement;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_proforma")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaProforma getTaProforma() {
		return taProforma;
	}

	public void setTaProforma(TaProforma taProforma) {
		this.taProforma = taProforma;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_preparation")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaPreparation getTaPreparation() {
		return taPreparation;
	}

	public void setTaPreparation(TaPreparation taPreparation) {
		this.taPreparation = taPreparation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_devis")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaDevis getTaDevis() {
		return taDevis;
	}

	public void setTaDevis(TaDevis taDevis) {
		this.taDevis = taDevis;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_apporteur")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaApporteur getTaApporteur() {
		return taApporteur;
	}

	public void setTaApporteur(TaApporteur taApporteur) {
		this.taApporteur = taApporteur;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avoir")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaAvoir getTaAvoir() {
		return taAvoir;
	}

	public void setTaAvoir(TaAvoir taAvoir) {
		this.taAvoir = taAvoir;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bonliv")
	@XmlElement
	@XmlInverseReference(mappedBy="taRReglementLiaisons")
	public TaBonliv getTaBonliv() {
		return taBonliv;
	}

	public void setTaBonliv(TaBonliv taBonliv) {
		this.taBonliv = taBonliv;
	}

}
