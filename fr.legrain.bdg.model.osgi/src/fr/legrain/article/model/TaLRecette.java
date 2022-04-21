package fr.legrain.article.model;


import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.math.MathContext;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.SWTLigneDocument;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_l_recette")
public class TaLRecette implements java.io.Serializable {

	private static final long serialVersionUID = 2111081123658608207L;

	static Logger logger = Logger.getLogger(TaLRecette.class.getName());
	
	private int idLRecette;
	private String version;
	private TaArticle taArticle;
//	private TaLot taLot;
//	private TaEntrepot taEntrepot;
	private String emplacementLDocument;
	private TaRecette taRecette;
	private Integer numLigneLDocument;
	private String libLDocument;
	private BigDecimal qteLDocument;
	private BigDecimal qte2LDocument;
	private String u1LDocument;
	private String u2LDocument;
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	private Integer versionObj;

	public TaLRecette() {
		
	}
	
	public TaLRecette(int idLBonliv) {
		this.idLRecette = idLBonliv;
	}

	public TaLRecette(int idLBonliv, TaTLigne taTLigne, TaArticle taArticle,
			TaRecette taBonliv, Integer numLigneLBonliv, String libLBonliv,
			BigDecimal qteLBonliv, BigDecimal qte2LBonliv, String u1LBonliv,
			String u2LBonliv, BigDecimal prixULBonliv, BigDecimal tauxTvaLBonliv,
			String compteLBonliv, String codeTvaLBonliv, String codeTTvaLBonliv,
			BigDecimal mtHtLBonliv, BigDecimal mtTtcLBonliv,
			BigDecimal remTxLBonliv, BigDecimal remHtLBonliv,
			String quiCreeLBonliv, Date quandCreeLBonliv, String quiModifLBonliv,
			Date quandModifLBonliv, String ipAcces, Integer versionObj) {
		this.idLRecette = idLBonliv;
		this.taArticle = taArticle;
		this.taRecette = taBonliv;
		this.numLigneLDocument = numLigneLBonliv;
		this.libLDocument = libLBonliv;
		this.qteLDocument = qteLBonliv;
		this.qte2LDocument = qte2LBonliv;
		this.u1LDocument = u1LBonliv;
		this.u2LDocument = u2LBonliv;
		this.quiCree = quiCreeLBonliv;
		this.quandCree = quandCreeLBonliv;
		this.quiModif = quiModifLBonliv;
		this.quandModif = quandModifLBonliv;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_l_recette", unique = true, nullable = false)
	public int getIdLRecette() {
		return idLRecette;
	}

	public void setIdLRecette(int idLRecette) {
		this.idLRecette = idLRecette;
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

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_article")
	@LgrHibernateValidated(champBd = "id_article",table = "ta_article", champEntite="taArticle.idArticle", clazz = TaArticle.class)
	public TaArticle getTaArticle() {
		return this.taArticle;
	}



	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;

		if(taArticle!=null && taArticle.getTaRapportUnite()!=null && taArticle.getTaRapportUnite().getTaUnite1()!=null) {
			setU1LDocument(taArticle.getTaRapportUnite().getTaUnite1().getCodeUnite());
		}
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_recette")
	@LgrHibernateValidated(champBd = "id_recette",table = "ta_recette", champEntite="taRecette.idRecette", clazz = TaRecette.class)
	@XmlElement
	@XmlInverseReference(mappedBy="lignes")
	public TaRecette getTaRecette() {
		return this.taRecette;
	}



	@Column(name = "num_ligne_l_document")
	@LgrHibernateValidated(champBd = "num_ligne_l_document",table = "ta_l_recette", champEntite="numLigneLDocument", clazz = TaLRecette.class)
	public Integer getNumLigneLDocument() {
		return this.numLigneLDocument;
	}

	public void setNumLigneLDocument(Integer numLigneLBonliv) {
		this.numLigneLDocument = numLigneLBonliv;
	}

	@Column(name = "lib_l_document")
	@LgrHibernateValidated(champBd = "lib_l_document",table = "ta_l_recette", champEntite="libLDocument", clazz = TaLRecette.class)
	public String getLibLDocument() {
		return this.libLDocument;
	}

	public void setLibLBonliv(String libLBonliv) {
		setLibLDocument(libLBonliv);
	}

	public void setLibLDocument(String libLBonliv) {
		this.libLDocument = libLBonliv;
	}

	@Column(name = "qte_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "qte_l_document",table = "ta_l_recette", champEntite="qteLDocument", clazz = TaLRecette.class)
	public BigDecimal getQteLDocument() {
		return this.qteLDocument;
	}

	public void setQteLDocument(BigDecimal qteLBonliv) {		
		this.qteLDocument = qteLBonliv;
	}

	@Column(name = "qte2_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "qte2_l_document",table = "ta_l_recette", champEntite="qte2LDocument", clazz = TaLRecette.class)
	public BigDecimal getQte2LDocument() {
		return this.qte2LDocument;
	}

	public void setQte2LDocument(BigDecimal qte2LBonliv) {
		this.qte2LDocument = qte2LBonliv;
	}

	@Column(name = "u1_l_document", length = 20)
	@LgrHibernateValidated(champBd = "u1_l_document",table = "ta_l_recette", champEntite="u1LDocument", clazz = TaLRecette.class)
	public String getU1LDocument() {
		return this.u1LDocument;
	}

	public void setU1LDocument(String u1LBonliv) {
		this.u1LDocument = u1LBonliv;
	}

	@Column(name = "u2_l_document", length = 20)
	@LgrHibernateValidated(champBd = "u2_l_document",table = "ta_l_recette", champEntite="u2LDocument", clazz = TaLRecette.class)
	public String getU2LDocument() {
		return this.u2LDocument;
	}

	public void setU2LDocument(String u2LBonliv) {
		this.u2LDocument = u2LBonliv;
	}

	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeLBonliv) {
		this.quiCree = quiCreeLBonliv;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeLBonliv) {
		this.quandCree = quandCreeLBonliv;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifLBonliv) {
		this.quiModif = quiModifLBonliv;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifLBonliv) {
		this.quandModif = quandModifLBonliv;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}
	
	public boolean ligneEstVide() {
		if (taArticle!=null)return false;
		if (libLDocument!=null && !libLDocument.equals(""))return false;		
		return true;
	}
	
	public void initialiseLigne(boolean reset){
		if (reset){
			setLibLDocument("");
			setQteLDocument(null);
			setQte2LDocument(null);
			setU1LDocument("") ;
			setU2LDocument("") ;
		}else {
			setLibLDocument("");
			setQteLDocument(new BigDecimal(0));
			setQte2LDocument(new BigDecimal(0));
			setU1LDocument("");
			setU2LDocument("");
		}
		setTaArticle(null);	
	}

//	public TaLRecette clone() {
//		TaLRecette ligne = new TaLRecette();
//		try {
//			//ligne = (TaLFacture)super.clone();
//			ligne.setIdLRecette(0);
//			ligne.setVersion(version);
//			ligne.setTaRecette(taRecette);
//			ligne.setTaArticle(taArticle);
//			ligne.setNumLigneLDocument(numLigneLDocument);
//			ligne.setLibLDocument(libLDocument);
//			ligne.setQteLDocument(qteLDocument);
//			ligne.setQte2LDocument(qte2LDocument);
//			ligne.setU1LDocument(u1LDocument);
//			ligne.setU2LDocument(u2LDocument);
//			ligne.setQuiCree(quiCree);
//			ligne.setQuandCree(quandCree);
//			ligne.setQuiModif(quiModif);
//			ligne.setQuandModif(quandModif);
//			ligne.setIpAcces(ipAcces);
//			ligne.setVersionObj(versionObj);			
//		} catch(Exception cnse) {
//			logger.error("",cnse);
//		}
//		// on renvoie le clone
//		return ligne;
//	}

//	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
//	@JoinColumn(name = "id_lot")
//	@LgrHibernateValidated(champBd = "id_lot",table = "ta_lot", champEntite="taLot.idLot", clazz = TaLot.class)
//	public TaLot getTaLot() {
//		return taLot;
//	}
//
//	public void setTaLot(TaLot taLot) {
//		this.taLot = taLot;
//	}
//
//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
//	@JoinColumn(name = "id_entrepot")
//	@LgrHibernateValidated(champBd = "id_entrepot",table = "ta_entrepot", champEntite="taEntrepot.idEntrepot", clazz = TaEntrepot.class)
//	public TaEntrepot getTaEntrepot() {
//		return taEntrepot;
//	}
//
//	public void setTaEntrepot(TaEntrepot taEntrepot) {
//		this.taEntrepot = taEntrepot;
//	}

	@Column(name = "emplacement_l_document")
	@LgrHibernateValidated(champBd = "emplacement_l_document",table = "ta_l_fabrication", champEntite="emplacementLDocument", clazz = TaLRecette.class)
	public String getEmplacementLDocument() {
		return this.emplacementLDocument;
	}

	public void setEmplacementLDocument(String emplacementLDocument) {
		this.emplacementLDocument = emplacementLDocument;
	}

	public void setTaRecette(TaRecette taRecette) {
		this.taRecette = taRecette;
	}

	
	@Override
	public Object clone() throws CloneNotSupportedException {
		TaLRecette objet = new TaLRecette();
		try {
			objet.setEmplacementLDocument(emplacementLDocument);
			objet.setLibLDocument(libLDocument);
			objet.setNumLigneLDocument(numLigneLDocument);
			objet.setQte2LDocument(qte2LDocument);
			objet.setQteLDocument(qteLDocument);
			objet.setU1LDocument(u1LDocument);
			objet.setU2LDocument(u2LDocument);
		} catch (Exception e) {
			logger.error("", e);
		}
		// on renvoie le clone
		return objet;
	}
}
