package fr.legrain.article.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.stock.model.TaInventaire;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_lot")

/* - Dima - Début - */
@NamedQueries(value = { 
		@NamedQuery(name=TaLot.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaLotDTO(a.idLot, a.numLot,a.libelle, art.codeArticle, a.unite1, a.unite2, a.rapport, a.nbDecimal, a.termine,a.versionObj) from TaLot a join a.taArticle art order by a.numLot"),
		@NamedQuery(name=TaLot.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaLotDTO(a.idLot, a.numLot,a.libelle, art.codeArticle, a.unite1, a.unite2, a.rapport,  a.nbDecimal, a.termine,a.versionObj) from TaLot a join a.taArticle art  where a.numLot like :numLot order by a.numLot"),
		@NamedQuery(name=TaLot.QN.FIND_BY_NON_CONFORME_NO_DATE, query="select distinct a from TaLot a"), 
		//@NamedQuery(name=TaLot.QN.FIND_BY_NON_CONFORME, query="select a from TaLot a where a.dateDebR between ? and ? order by a.dateDebR"), 
		@NamedQuery(name=TaLot.QN.FIND_BY_NON_CONFORME_CORRECTION, query="select distinct rc from TaLot a join a.taResultatConformites tr join tr.taResultatCorrections rc where rc.valide = FALSE"),
})
/* - Dima -  Fin  - */

public class TaLot extends TaObjetLgr   implements java.io.Serializable{

	private static final long serialVersionUID = -3694507021999904313L;
	
	/* - Dima - Début - */
	public static final String TYPE_DOC = "TaLot";
	
	public static class QN {
		public static final String FIND_ALL_LIGHT = "TaLot.findAllLight";
		public static final String FIND_BY_CODE_LIGHT = "TaLot.findByCodeLight";
		public static final String FIND_BY_NON_CONFORME_NO_DATE = "TaLot.findNonConformeNoDate";
		public static final String FIND_BY_NON_CONFORME_CORRECTION = "TaLot.findNonConformeCorrection";
		//public static final String FIND_BY_NON_CONFORME = "TaLot.findNonConforme";
	}
	/* - Dima -  Fin  - */
	
	private int idLot ;
	private String numLot ;
	private String libelle;
	private TaCodeBarre taCodeBarre;
	private TaArticle taArticle;
	private Boolean termine = false;
	private Boolean virtuel = false;
	private Boolean lotConforme = false;
	private Boolean presenceDeNonConformite = false;
	private Boolean lotCompletDefinitivementInvalide = false;
	private Date dluo;
	private Date dateLot;
	private String unite1;
	private String unite2;
	private BigDecimal rapport;
	private Integer nbDecimal;
	private Integer sens;
	private List<TaResultatConformite> taResultatConformites =new LinkedList<TaResultatConformite>();
	private Set<TaLabelArticle> taLabelArticles;
	private TaTiers taTiersPrestationService;
	private Boolean virtuelUnique = false;
	private TaStatusConformite taStatusConformite;

	private Integer versionObj;
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
//	private String version;
	
	
	@Transient
	private String retourControlLot;
	@Transient
	private Boolean aSupprimer=false;
	
	@Transient
	static Logger logger = Logger.getLogger(TaLot.class.getName());

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_lot", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_lot",table = "ta_lot", champEntite="idLot", clazz = TaLot.class)
	public int getIdLot() {
		return idLot;
	}

	public void setIdLot(int idLot) {
		this.idLot = idLot;
	}
	
	public TaLot() {
		super();
	}

//	public boolean controle() {
//		boolean controlesOK = false;
//		//Certains controle sont faits
//		//controlesOK = (taResultatConformites!=null && taResultatConformites.size()>0);
//		
//		//Tous les controles sont fait (pas forcement OK)
//		controlesOK = (taResultatConformites!=null 
//				&& taResultatConformites.size()>0
//				&& taArticle.getTaConformites()!=null
//				&& taArticle.getTaConformites().size()>0
//				&& taResultatConformites.size() == taArticle.getTaConformites().size()
//				);
//		
//		if(controlesOK) {
//			//tous les controles sont fait
//			boolean unControleFaux = false;
//			for (TaResultatConformite rc : taResultatConformites) {
//				for (TaResultatCorrection rcorr : rc.getTaResultatCorrections()) {
//					//vérification des actions correctives pour les controles "constatés faux"
//					if(!rcorr.getValide()) {
//						unControleFaux = true; 
//					}
//				}
//			}
//			if(unControleFaux) {
//				// au moins une action corrective n'a pas été effectué =>> au moins un controle est dans un état "faux"
//				controlesOK = false;
//			}
//		}
//		return controlesOK;
//	}
	
	public Boolean auMoinsUnControleFaux() {
		Boolean auMoinsUnControleFaux = null;
		//Certains controle sont faits
		//controlesOK = (taResultatConformites!=null && taResultatConformites.size()>0);
		if(taResultatConformites.size()>0) auMoinsUnControleFaux=false;

			boolean unControleFaux = false;
			for (TaResultatConformite rc : taResultatConformites) {
				for (TaResultatCorrection rcorr : rc.getTaResultatCorrections()) {
					//vérification des actions correctives pour les controles "constatés faux"
					if(!rcorr.getValide()) {
						unControleFaux = true; 
					}
				}
			}
			if(unControleFaux) {
				// au moins un controle est dans un état "faux"
				auMoinsUnControleFaux = true;
			}
		
		return auMoinsUnControleFaux;
	}
	
	public Boolean auMoinsUnControleFauxEffectue() {
		Boolean auMoinsUnControleFaux = null;
		//Certains controle sont faits
		//controlesOK = (taResultatConformites!=null && taResultatConformites.size()>0);
		if(taResultatConformites.size()>0) auMoinsUnControleFaux=false;

			boolean unControleFaux = false;
			for (TaResultatConformite rc : taResultatConformites) {
				for (TaResultatCorrection rcorr : rc.getTaResultatCorrections()) {
					//vérification des actions correctives pour les controles "constatés faux"
					if(!rcorr.getValide() || (rcorr.getEffectuee()!=null && rcorr.getEffectuee())) {
						unControleFaux = true; 
					}
				}
			}
			if(unControleFaux) {
				// au moins un controle est dans un état "faux"
				auMoinsUnControleFaux = true;
			}
		
		return auMoinsUnControleFaux;
	}
//	public String retourControl() {
//		String retour = "";
//		//Certains controle sont faits
//		//controlesOK = (taResultatConformites!=null && taResultatConformites.size()>0);
////		if(this.getTaArticle().getTaConformites()!=null && this.getTaArticle().getTaConformites().size()>0)retour="V";
//		
//		if(this.getTaArticle().getTaConformites()!=null && this.getTaArticle().getTaConformites().size()>0)retour="";		
//		if(taResultatConformites.size()>0) retour="OK";
//
//			boolean unControleFaux = false;
//			for (TaResultatConformite rc : taResultatConformites) {
//				for (TaResultatCorrection rcorr : rc.getTaResultatCorrections()) {
//					//vérification des actions correctives pour les controles "constatés faux"
//					if(!rcorr.getValide()) {
//						unControleFaux = true; 
//					}
//				}
//			}
//			if(unControleFaux) {
//				// au moins un controle est dans un état "faux"
//				retour = "N";
//			}
//		
//		return retour;
//	}
	
//	public TaLot initListeResultatControle(List<TaConformite> l) {
//		if(this.taArticle.getTaConformites()!=null) {
//			this.taResultatConformites = new LinkedList<TaResultatConformite>();
//			TaResultatConformite r = null;
//			if(l!=null) {
//				for (TaConformite ctrl : l) {
//					r = new TaResultatConformite();
//					r.setTaConformite(ctrl);
//					r.setTaLot(this);
//					this.taResultatConformites.add(r);
//				}
//			} else {
//				for (TaConformite ctrl : this.taArticle.getTaConformites()) {
//					r = new TaResultatConformite();
//					r.setTaConformite(ctrl);
//					r.setTaLot(this);
//					this.taResultatConformites.add(r);
//				}
//			}
//		}
//		return this;
//	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_code_barre")
	@LgrHibernateValidated(champBd = "id_code_barre",table = "ta_code_barre", champEntite="taCodeBarre.id", clazz = TaCodeBarre.class)
	public TaCodeBarre getTaCodeBarre() {
		return this.taCodeBarre;
	}

	public void setTaCodeBarre(TaCodeBarre taCodeBarre) {
		this.taCodeBarre = taCodeBarre;
	}

	@Column(name = "numlot", length = 50)
	@LgrHibernateValidated(champBd = "numlot",table = "ta_lot", champEntite="numLot", clazz = TaLot.class)
	public String getNumLot() {
		return numLot;
	}

	public void setNumLot(String numLot) {
		this.numLot = numLot;
	}

	@Column(name = "termine")
	@LgrHibernateValidated(champBd = "termine",table = "ta_lot", champEntite="termine", clazz = TaLot.class)
	public Boolean getTermine() {
		return termine;
	}

	public void setTermine(Boolean termine) {
		this.termine = termine;
	}
	
	@Column(name = "virtuel")
	@LgrHibernateValidated(champBd = "virtuel",table = "ta_lot", champEntite="virtuel", clazz = TaLot.class)
	public Boolean getVirtuel() {
		return virtuel;
	}

	public void setVirtuel(Boolean virtuel) {
		this.virtuel = virtuel;
	}
	
	@Column(name = "virtuel_unique")
	@LgrHibernateValidated(champBd = "virtuel_unique",table = "ta_lot", champEntite="virtuelUnique", clazz = TaLot.class)
	public Boolean getVirtuelUnique() {
		return virtuelUnique;
	}

	public void setVirtuelUnique(Boolean virtuelUnique) {
		this.virtuelUnique = virtuelUnique;
	}
	
	
	@Column(name = "dluo")
	@Temporal(TemporalType.TIMESTAMP)
	@LgrHibernateValidated(champBd = "dluo",table = "ta_lot", champEntite="dluo", clazz = TaLot.class)
	public Date getDluo() {
		return dluo;
	}

	public void setDluo(Date dluo) {
		this.dluo = dluo;
	}	
	
	
	@Column(name = "date_lot")
	@Temporal(TemporalType.TIMESTAMP)
	@LgrHibernateValidated(champBd = "date_lot",table = "ta_lot", champEntite="dateLot", clazz = TaLot.class)
	public Date getDateLot() {
		return dateLot;
	}

	public void setDateLot(Date dateLot) {
		this.dateLot = dateLot;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_article")
	@LgrHibernateValidated(champBd = "id_article",table = "ta_article", champEntite="taArticle.idArticle", clazz = TaArticle.class)
	public TaArticle getTaArticle() {
		return taArticle;
	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;
	}
	
	@Column(name = "unite1")
	public String getUnite1() {
		return unite1;
	}

	public void setUnite1(String unite1) {
		this.unite1 = unite1;
	}
	
	@Column(name = "unite2")
	public String getUnite2() {
		return unite2;
	}

	public void setUnite2(String unite2) {
		this.unite2 = unite2;
	}
	
	@Column(name = "rapport", precision = 15)
	@LgrHibernateValidated(champBd = "rapport",table = "ta_lot", champEntite="rapport", clazz = TaLot.class)
	public BigDecimal getRapport() {
		return this.rapport;
	}

	public void setRapport(BigDecimal rapport) {
		this.rapport = rapport;
	}

	@Column(name = "nb_decimal")
	@LgrHibernateValidated(champBd = "nb_decimal",table = "ta_lot", champEntite="nbDecimal", clazz = TaLot.class)
	public Integer getNbDecimal() {
		return this.nbDecimal;
	}

	public void setNbDecimal(Integer nbDecimal) {
		this.nbDecimal = nbDecimal;
	}
	
	@Column(name = "sens")
	@LgrHibernateValidated(champBd = "sens",table = "ta_lot", champEntite="sens", clazz = TaLot.class)
	public Integer getSens() {
		return this.sens;
	}

	public void setSens(Integer sens) {
		this.sens = sens;
	}
	
	@XmlTransient
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taLot", orphanRemoval=true)
	public List<TaResultatConformite> getTaResultatConformites() {
		return this.taResultatConformites;
	}

	public void setTaResultatConformites(List<TaResultatConformite> taResultatConformites) {
		this.taResultatConformites = taResultatConformites;
	}
	
	
	@Version
	@Column(name = "version_obj")
	public Integer getVersionObj() {
		return this.versionObj;
	}

	public void setVersionObj(Integer versionObj) {
		this.versionObj = versionObj;
	}

//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCree) {
//		this.quiCree = quiCree;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCree) {
//		this.quandCree = quandCree;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModif) {
//		this.quiModif = quiModif;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModif) {
//		this.quandModif = quandModif;
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
//
//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	@Column(name = "libelle")
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	@Column(name = "lot_conforme")
	public Boolean getLotConforme() {
		return lotConforme;
	}

	public void setLotConforme(Boolean lotConforme) {
		this.lotConforme = lotConforme;
	}

	@Column(name = "presence_de_non_conformite")
	public Boolean getPresenceDeNonConformite() {
		return presenceDeNonConformite;
	}

	public void setPresenceDeNonConformite(Boolean presenceDeNonConformite) {
		this.presenceDeNonConformite = presenceDeNonConformite;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_tiers_prestation_de_service")
	public TaTiers getTaTiersPrestationService() {
		return taTiersPrestationService;
	}

	public void setTaTiersPrestationService(TaTiers taTiersPrestationService) {
		this.taTiersPrestationService = taTiersPrestationService;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_status_conformite")
	public TaStatusConformite getTaStatusConformite() {
		return taStatusConformite;
	}

	public void setTaStatusConformite(TaStatusConformite taStatusConformite) {
		this.taStatusConformite = taStatusConformite;
	}

//	@Fetch(FetchMode.SELECT)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_label_lot",
			joinColumns = {@JoinColumn(name = "id_lot")},inverseJoinColumns = {@JoinColumn(name = "id_label_article")})
	public Set<TaLabelArticle> getTaLabelArticles(){
		return this.taLabelArticles;
	}

	public void setTaLabelArticles(Set<TaLabelArticle> taLabelArticles) {
		this.taLabelArticles = taLabelArticles;
	}

	@Column(name = "lot_complet_definitivement_invalide")
	public Boolean getLotCompletDefinitivementInvalide() {
		return lotCompletDefinitivementInvalide;
	}

	public void setLotCompletDefinitivementInvalide(Boolean lotCompletDefinitivementInvalide) {
		this.lotCompletDefinitivementInvalide = lotCompletDefinitivementInvalide;
	}

	@Transient
	public String getRetourControlLot() {
		return retourControlLot;
	}

	@Transient
	public void setRetourControlLot(String retourControlLot) {
		this.retourControlLot = retourControlLot;
	}

	@Transient
	public Boolean getaSupprimer() {
		return aSupprimer;
	}

	@Transient
	public void setaSupprimer(Boolean aSupprimer) {
		this.aSupprimer = aSupprimer;
	}

}
