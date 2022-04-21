package fr.legrain.article.model;


import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaPrix;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.SWTLigneDocument;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
@Table(name = "ta_recette")
public class TaRecette extends TaObjetLgr   implements java.io.Serializable {

	private static final long serialVersionUID = -4770880696283936523L;

	static Logger logger = Logger.getLogger(TaRecette.class.getName());
	
	private int idRecette;
	
	private TaArticle taArticle;
	private Integer qte;
	private List<TaLRecette> lignes = new ArrayList<TaLRecette>(0);
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String version;
//	private String ipAcces;
	private Integer versionObj;

	public TaRecette() {
		
	}

	public TaRecette(int idLBonliv,  TaArticle taArticle,
			TaFabrication taBonliv, 
			String quiCreeLBonliv, Date quandCreeLBonliv, String quiModifLBonliv,
			Date quandModifLBonliv, String ipAcces, Integer versionObj) {
		this.taArticle = taArticle;
		this.quiCree = quiCreeLBonliv;
		this.quandCree = quandCreeLBonliv;
		this.quiModif = quiModifLBonliv;
		this.quandModif = quandModifLBonliv;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_recette", unique = true, nullable = false)
	public int getIdRecette() {
		return idRecette;
	}

	public void setIdRecette(int idRecette) {
		this.idRecette = idRecette;
	}

//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}
	
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
	@XmlElement
	@XmlInverseReference(mappedBy="taRecetteArticle")
	public TaArticle getTaArticle() {
		return this.taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeLBonliv) {
//		this.quiCree = quiCreeLBonliv;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeLBonliv) {
//		this.quandCree = quandCreeLBonliv;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifLBonliv) {
//		this.quiModif = quiModifLBonliv;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifLBonliv) {
//		this.quandModif = quandModifLBonliv;
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

	public TaRecette clone() {
		TaRecette ligne = new TaRecette();
		try {
			ligne.setVersion(version);
			ligne.setTaArticle(taArticle);
			ligne.setQuiCree(quiCree);
			ligne.setQuandCree(quandCree);
			ligne.setQuiModif(quiModif);
			ligne.setQuandModif(quandModif);
			ligne.setIpAcces(ipAcces);
			ligne.setVersionObj(versionObj);			
		} catch(Exception cnse) {
			logger.error("",cnse);
		}
		return ligne;
	}

	@OrderBy("numLigneLDocument")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taRecette", orphanRemoval=true)
	@Fetch(FetchMode.SUBSELECT)
	public List<TaLRecette> getLignes() {
		return lignes;
	}

	public void setLignes(List<TaLRecette> lignes) {
		this.lignes = lignes;
	}

	@Column(name = "qte")
	public Integer getQte() {
		return qte;
	}

	public void setQte(Integer qte) {
		this.qte = qte;
	}

	
	
	public Object cloneDuplication() throws CloneNotSupportedException {
		TaRecette objet = new TaRecette();
		try {
			objet.setQte(qte);			
			for (TaLRecette lr : lignes) {
				TaLRecette l=(TaLRecette) lr.clone();
				objet.getLignes().add(l);
				l.setTaRecette(objet);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		// on renvoie le clone
		return objet;
	}
}
