package fr.legrain.tache.model;


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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import fr.legrain.article.model.TaFabrication;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_r_document_evenement")
public class TaRDocumentEvenement implements java.io.Serializable {
	
	private static final long serialVersionUID = 3013174389930244539L;
	
	private int idRDocument;
	private String version;
	private TaEvenement taEvenement;
	private TaFacture taFacture;
	private TaBoncde taBoncde;
	private TaProforma taProforma;
	private TaBonliv taBonliv;
	private TaAvoir taAvoir;
	private TaApporteur taApporteur;
	private TaDevis taDevis;
	private TaAcompte taAcompte;
	private TaPrelevement taPrelevement;
	private TaAvisEcheance taAvisEcheance;
	
	private TaBonReception taBonReception;
	private TaFabrication taFabrication;
	
//	private TaRDocumentEvenement taDocumentGenere;
//	private TaTDoc taTypeDoc;
//	private TaReglement taReglement;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaRDocumentEvenement() {
	}

	public TaRDocumentEvenement(int idRDocument) {
		this.idRDocument = idRDocument;
	}

	public TaRDocumentEvenement(int idRDocument, String version, TaFacture taFacture,
			TaBoncde taBoncde, TaProforma taProforma, TaBonliv taBonliv,
			TaAvoir taAvoir, TaApporteur taApporteur, TaDevis taDevis,
			TaAcompte taAcompte, TaPrelevement taPrelevement,
			TaAvisEcheance taAvisEcheance, TaRDocumentEvenement taDocumentGenere,
			TaTDoc taTypeDoc, String quiCreeRDocument, Date quandCreeRDocument,
			String quiModifRDocument, Date quandModifRDocument, String ipAcces,
			Integer versionObj) {
		super();
		this.idRDocument = idRDocument;
		this.version = version;
		this.taFacture = taFacture;
		this.taBoncde = taBoncde;
		this.taProforma = taProforma;
		this.taBonliv = taBonliv;
		this.taAvoir = taAvoir;
		this.taApporteur = taApporteur;
		this.taDevis = taDevis;
		this.taAcompte = taAcompte;
		this.taPrelevement = taPrelevement;
		this.taAvisEcheance = taAvisEcheance;
//		this.taDocumentGenere = taDocumentGenere;
//		this.taTypeDoc = taTypeDoc;
		this.quiCree = quiCreeRDocument;
		this.quandCree = quandCreeRDocument;
		this.quiModif = quiModifRDocument;
		this.quandModif = quandModifRDocument;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}
	
	public void affecteDocument(SWTDocument swtDocument) throws Exception {
		if(swtDocument instanceof TaFacture) {
			this.setTaFacture((TaFacture) swtDocument);
		} else if(swtDocument instanceof TaDevis) {
			this.setTaDevis((TaDevis) swtDocument);
		} else if(swtDocument instanceof TaBoncde) {
			this.setTaBoncde((TaBoncde) swtDocument);
		} else if(swtDocument instanceof TaProforma) {
			this.setTaProforma((TaProforma) swtDocument);
		} else if(swtDocument instanceof TaBonliv) {
			this.setTaBonliv((TaBonliv) swtDocument);
		} else if(swtDocument instanceof TaAvoir) {
			this.setTaAvoir((TaAvoir) swtDocument);
		} else if(swtDocument instanceof TaApporteur) {
			this.setTaApporteur((TaApporteur) swtDocument);
		} else if(swtDocument instanceof TaAcompte) {
			this.setTaAcompte((TaAcompte) swtDocument);
		} else if(swtDocument instanceof TaPrelevement) {
			this.setTaPrelevement((TaPrelevement) swtDocument);
		} else if(swtDocument instanceof TaAvisEcheance) {
			this.setTaAvisEcheance((TaAvisEcheance) swtDocument);
		} else if(swtDocument instanceof TaBonReception) {
			this.setTaBonReception((TaBonReception) swtDocument);
		} else if(swtDocument instanceof TaFabrication) {
			this.setTaFabrication((TaFabrication) swtDocument);
		}else {
			throw new Exception("Le document n'est pas du bon type (SWTDocument");
		}
	}
	
	@Transient
	public SWTDocument getSWTDocument() {
		SWTDocument retour = null;
		
		if(retour==null)
			retour = getTaAcompte();
		
		if(retour==null)
			retour = getTaApporteur();
		
		if(retour==null)
			retour = getTaAvisEcheance();
		
		if(retour==null)
			retour = getTaAvoir();
		
		if(retour==null)
			retour = getTaBoncde();
		
		if(retour==null)
			retour = getTaBonliv();
		
		if(retour==null)
			retour = getTaFacture();
		
		if(retour==null)
			retour = getTaPrelevement();
		
		if(retour==null)
			retour = getTaProforma();
		
		if(retour==null)
			retour = getTaDevis();
		
		return retour;
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_r_document_evenement", unique = true, nullable = false)
	public int getIdRDocument() {
		return this.idRDocument;
	}

	public void setIdRDocument(int idRDocument) {
		this.idRDocument = idRDocument;
	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "id_evenement")
	public TaEvenement getTaEvenement() {
		return taEvenement;
	}
	public void setTaEvenement(TaEvenement taEvenement) {
		this.taEvenement = taEvenement;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_facture")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_facture", champEntite="taFacture.idDocument", clazz = TaFacture.class)
	public TaFacture getTaFacture() {
		return this.taFacture;
	}

	public void setTaFacture(TaFacture taFacture) {
		this.taFacture = taFacture;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_prelevement")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_prelevement", champEntite="taPrelevement.idDocument", clazz = TaPrelevement.class)
	public TaPrelevement getTaPrelevement() {
		return taPrelevement;
	}

	public void setTaPrelevement(TaPrelevement taPrelevement) {
		this.taPrelevement = taPrelevement;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avis_echeance")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_avis_echeance", champEntite="taAvisEcheance.idDocument", clazz = TaAvisEcheance.class)
	public TaAvisEcheance getTaAvisEcheance() {
		return taAvisEcheance;
	}

	public void setTaAvisEcheance(TaAvisEcheance taAvisEcheance) {
		this.taAvisEcheance = taAvisEcheance;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_boncde")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_boncde", champEntite="taBoncde.idDocument", clazz = TaBoncde.class)
	public TaBoncde getTaBoncde() {
		return this.taBoncde;
	}

	public void setTaBoncde(TaBoncde taBoncde) {
		this.taBoncde = taBoncde;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_proforma")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_proforma", champEntite="taProforma.idDocument", clazz = TaProforma.class)
	public TaProforma getTaProforma() {
		return this.taProforma;
	}

	public void setTaProforma(TaProforma taProforma) {
		this.taProforma = taProforma;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bonliv")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_bonliv", champEntite="taBonliv.idDocument", clazz = TaBonliv.class)
	public TaBonliv getTaBonliv() {
		return this.taBonliv;
	}

	public void setTaBonliv(TaBonliv taBonliv) {
		this.taBonliv = taBonliv;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_avoir")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_avoir", champEntite="taAvoir.idDocument", clazz = TaAvoir.class)
	public TaAvoir getTaAvoir() {
		return this.taAvoir;
	}

	public void setTaAvoir(TaAvoir taAvoir) {
		this.taAvoir = taAvoir;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_apporteur")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_apporteur", champEntite="taApporteur.idDocument", clazz = TaApporteur.class)
	public TaApporteur getTaApporteur() {
		return this.taApporteur;
	}

	public void setTaApporteur(TaApporteur taApporteur) {
		this.taApporteur = taApporteur;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_devis")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_devis", champEntite="taDevis.idDocument", clazz = TaApporteur.class)
	public TaDevis getTaDevis() {
		return this.taDevis;
	}

	public void setTaDevis(TaDevis idDevis) {
		this.taDevis = idDevis;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_acompte")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_acompte", champEntite="taAcompte.idDocument", clazz = TaAcompte.class)
	public TaAcompte getTaAcompte() {
		return taAcompte;
	}

	public void setTaAcompte(TaAcompte taAcompte) {
		this.taAcompte = taAcompte;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fabrication")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_fabrication", champEntite="taFabrication.idDocument", clazz = TaFabrication.class)
	public TaFabrication getTaFabrication() {
		return taFabrication;
	}

	public void setTaFabrication(TaFabrication taFabrication) {
		this.taFabrication = taFabrication;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_bon_reception")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_bon_reception", champEntite="taBonReception.idDocument", clazz = TaBonReception.class)
	public TaBonReception getTaBonReception() {
		return taBonReception;
	}

	public void setTaBonReception(TaBonReception taBonReception) {
		this.taBonReception = taBonReception;
	}
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_reglement")
//	@LgrHibernateValidated(champ = "id_reglement",table = "ta_reglement",clazz = TaReglement.class)
//	public TaReglement getTaReglement() {
//		return taReglement;
//	}
//
//	public void setTaReglement(TaReglement taReglement) {
//		this.taReglement = taReglement;
//	}


//	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//	@JoinColumn(name = "id_doc_genere")
////	@LgrHibernateValidated(champ = "id_document",table = "ta_acompte",clazz = TaAcompte.class)
//	public TaRDocumentEvenement getTaDocumentGenere() {
//		return taDocumentGenere;
//	}
//
//	public void setTaDocumentGenere(TaRDocumentEvenement taDocumentGenere) {
//		this.taDocumentGenere = taDocumentGenere;
//	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "id_t_doc_genere")
//	public TaTDoc getTaTypeDoc() {
//		return taTypeDoc;
//	}
//
//	public void setTaTypeDoc(TaTDoc taTypeDoc) {
//		this.taTypeDoc = taTypeDoc;
//	}
//	
	
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



	public boolean verifRemoveAfter(){
		//passage ejb => dans TaXXXService
		System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
//		try {
//			TaRDocumentDAO daoRDocument = new TaRDocumentDAO();
//			TaRDocument rdocLiaison=null;
//			if(daoRDocument.exist(this)) rdocLiaison=daoRDocument.findByIdGenere(this);
//			if(rdocLiaison!=null){
//				daoRDocument.remove(rdocLiaison);
//			}
			return true;
//		} catch (Exception e) {
//			//logger.error("",e);
//			return false;
//		}
	}

	
	

	
}
