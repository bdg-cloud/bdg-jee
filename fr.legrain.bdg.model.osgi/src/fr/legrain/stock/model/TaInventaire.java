package fr.legrain.stock.model;

// Generated 4 juin 2009 10:11:38 by Hibernate Tools 3.2.4.GA

import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.article.model.TaEntrepot;
import fr.legrain.document.model.TaEtat;
import fr.legrain.general.model.TaObjetLgr;
import fr.legrain.validator.LgrHibernateValidated;

@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ta_inventaire")
@NamedQueries(value = { 
		@NamedQuery(name=TaInventaire.QN.FIND_ALL_LIGHT, query="select new fr.legrain.stock.dto.InventaireDTO(a.idInventaire, a.codeInventaire, a.dateInventaire, a.libelleInventaire,a.versionObj) from TaInventaire a order by a.codeInventaire"),
		@NamedQuery(name=TaInventaire.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.stock.dto.InventaireDTO(a.idInventaire, a.codeInventaire, a.dateInventaire, a.libelleInventaire,a.versionObj) from TaInventaire a  where a.codeInventaire like :codeInventaire order by a.codeInventaire")
		})
public class TaInventaire extends TaObjetLgr   implements java.io.Serializable {


	private static final long serialVersionUID = 8028801409622213656L;
	
	public static class QN {
		public static final String FIND_ALL_LIGHT = "TaInventaire.findAllLight";
		public static final String FIND_BY_CODE_LIGHT = "TaInventaire.findByCodeLight";
	}
	
	private int idInventaire;
	private String codeInventaire;
	private Date dateInventaire;
	private String libelleInventaire;
	private TaEtat taEtat;
	private String commentaire;
	private TaEntrepot taEntrepot;
	private List<TaLInventaire> lignes = new LinkedList<TaLInventaire>();
	private TaGrMouvStock taGrMouvStock;

	
//	private String quiCree;
//	private Date quandCree;
//	private String quiModif;
//	private Date quandModif;
//	private String ipAcces;
	private Integer versionObj;
//	private String version;

	public TaInventaire() {
	}

	public TaInventaire(int idGrMouvStock) {
		this.idInventaire = idGrMouvStock;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_inventaire", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_inventaire",table = "ta_inventaire", champEntite="idInventaire", clazz = TaInventaire.class)
	public int getIdInventaire() {
		return this.idInventaire;
	}

	public void setIdInventaire(int idGrMouvStock) {
		this.idInventaire = idGrMouvStock;
	}

////	@Version
//	@Column(name = "version", length = 20)
//	public String getVersion() {
//		return this.version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}



	@Temporal(TemporalType.DATE)
	@Column(name = "date_inventaire", length = 19)
	@LgrHibernateValidated(champBd = "date_inventaire",table = "ta_inventaire", champEntite="dateInventaire", clazz = TaInventaire.class)
	public Date getDateInventaire() {
		return this.dateInventaire;
	}

	public void setDateInventaire(Date dateGrMouvStock) {
		this.dateInventaire = dateGrMouvStock;
	}

	@Column(name = "libelle_inventaire")
	@LgrHibernateValidated(champBd = "libelle_inventaire",table = "ta_inventaire", champEntite="libelleInventaire", clazz = TaInventaire.class)
	public String getLibelleInventaire() {
		return this.libelleInventaire;
	}

	public void setLibelleInventaire(String libelleGrMouvStock) {
		this.libelleInventaire = libelleGrMouvStock;
	}

	


	@Column(name = "code_inventaire", length = 20)
	@LgrHibernateValidated(champBd = "code_inventaire",table = "ta_inventaire", champEntite="codeInventaire", clazz = TaInventaire.class)
	public String getCodeInventaire() {
		return codeInventaire;
	}

	public void setCodeInventaire(String codeGrMouvStock) {
		this.codeInventaire = codeGrMouvStock;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_etat")
	@LgrHibernateValidated(champBd = "id_etat",table = "ta_etat", champEntite="taEtat.idEtat", clazz = TaEtat.class)
	public TaEtat getTaEtat() {
		return this.taEtat;
	}

	public void setTaEtat(TaEtat taEtat) {
		this.taEtat = taEtat;
	}

	@Column(name = "commentaire")
	@LgrHibernateValidated(champBd = "commentaire",table = "ta_inventaire", champEntite="commentaire", clazz = TaInventaire.class)
	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_entrepot")
	@LgrHibernateValidated(champBd = "id_entrepot",table = "ta_entrepot", champEntite="taEntrepot.idEntrepot", clazz = TaEntrepot.class)
	public TaEntrepot getTaEntrepot() {
		return taEntrepot;
	}

	public void setTaEntrepot(TaEntrepot taEntrepot) {
		this.taEntrepot = taEntrepot;
	}
	
//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL ,orphanRemoval=true , mappedBy ="taInventaire")
	//Attention si FetchType.EAGER => à cause d'autre EAGER, génère des outter join et duplique les lignes,
	//si EAGER il faut donc gérer ce problème ou remplacer la List des lignes par un Set
	@JoinColumn(name = "id_gr_mouv_stock")
	@LgrHibernateValidated(champBd = "id_gr_mouvement_stock",table = "ta_gr_mouv_stock", champEntite="taGrMouvStock.idGrMouvStock", clazz = TaGrMouvStock.class)
	public TaGrMouvStock getTaGrMouvStock() {
		return taGrMouvStock;
	}
	public void setTaGrMouvStock(TaGrMouvStock taGrMouvStock) {
		this.taGrMouvStock = taGrMouvStock;
	}
	
//	@Column(name = "qui_cree", length = 50)
//	public String getQuiCree() {
//		return this.quiCree;
//	}
//
//	public void setQuiCree(String quiCreeStock) {
//		this.quiCree = quiCreeStock;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_cree", length = 19)
//	public Date getQuandCree() {
//		return this.quandCree;
//	}
//
//	public void setQuandCree(Date quandCreeStock) {
//		this.quandCree = quandCreeStock;
//	}
//
//	@Column(name = "qui_modif", length = 50)
//	public String getQuiModif() {
//		return this.quiModif;
//	}
//
//	public void setQuiModif(String quiModifStock) {
//		this.quiModif = quiModifStock;
//	}
//
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "quand_modif", length = 19)
//	public Date getQuandModif() {
//		return this.quandModif;
//	}
//
//	public void setQuandModif(Date quandModifStock) {
//		this.quandModif = quandModifStock;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taInventaire", orphanRemoval=true)
	@Fetch(FetchMode.SUBSELECT)
	public List<TaLInventaire> getLignes() {
		return lignes;
	}

	public void setLignes(List<TaLInventaire> lignes) {
		this.lignes = lignes;
	}

	
	
	public void addLigneMouvement(TaLInventaire ligne){
		this.getLignes().add(ligne);
	}
	
	public void removeLigneMouvement(TaLInventaire ligne){
		ligne.setTaInventaire(null);
		this.getLignes().remove(ligne);		
	}	
	
	public void removeAllLigneInsereAuto(){
		List<TaLInventaire>liste =new LinkedList<TaLInventaire>();
		for (TaLInventaire obj : this.getLignes()) {
			if(obj.getInsertionAuto()){
				liste.add(obj);
			}
		}
		for (TaLInventaire obj2 : liste) {
			if(obj2.getInsertionAuto()){
				obj2.setTaInventaire(null);
				this.getLignes().remove(obj2);
			}
		}
	}



	
	
}
