package fr.legrain.article.model;


import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ITaLFabrication;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.SWTLigneDocument;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaHistREtatLigneDocument;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.lib.data.LibCalcul;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.stock.model.TaMouvementStockPrev;
import fr.legrain.validator.LgrHibernateValidated;


@Entity
@Table(name = "ta_l_fabrication_mp")
public class TaLFabricationMP extends SWTLigneDocument implements java.io.Serializable, ITaLFabrication {

	private static final long serialVersionUID = -7803670973170495383L;

	static Logger logger = Logger.getLogger(TaLFabricationMP.class.getName());

//	private int idLDocument;
	protected String version;
	protected TaTLigne taTLigne;
	protected TaArticle taArticle;
	protected TaLot taLot;
	protected TaEntrepot taEntrepot;
	protected String emplacementLDocument;
	protected TaFabrication taDocument;
	protected TaMouvementStock taMouvementStock;
	protected TaMouvementStockPrev taMouvementStockPrev;
	protected Integer numLigneLDocument;
//	protected Date dluo;
//	protected String libLDocument;
	protected BigDecimal qteLDocument;
	protected BigDecimal qte2LDocument;
	protected String u1LDocument;
	protected String u2LDocument;
	protected BigDecimal prixULDocument;
	protected BigDecimal tauxTvaLDocument;
	protected String compteLDocument;
	protected String codeTvaLDocument;
	protected String codeTTvaLDocument;
	protected BigDecimal mtHtLDocument;
	protected BigDecimal mtTtcLDocument;
	protected BigDecimal remTxLDocument;
	protected BigDecimal remHtLDocument;
	protected String quiCree;
	protected Date quandCree;
	protected String quiModif;
	protected Date quandModif;

	protected String ipAcces;
	@Transient
	protected boolean legrain = false;
	@Transient
	protected BigDecimal txRemHtDocument;
	@Transient
	protected BigDecimal mtHtLApresRemiseGlobaleDocument;
	@Transient
	protected BigDecimal mtTtcLApresRemiseGlobaleDocument;
	@Transient
	protected BigDecimal prixU2LDocument;
	
	private Set<TaREtatLigneDocument> taREtatLigneDocuments = new HashSet<TaREtatLigneDocument>(0);
	private Set<TaHistREtatLigneDocument> taHistREtatLigneDocuments = new HashSet<TaHistREtatLigneDocument>(0);

	@Transient
	private List<TaLigneALigneSupplementDTO> listeSupplement=new LinkedList<>();
	@Transient
	private List<TaLigneALigneSupplementDTO> listeLigneAIntegrer=new LinkedList<>();
	

	public TaLFabricationMP() {
		taMouvementStock = new TaMouvementStock();
	}
	
	public TaLFabricationMP	(boolean legrain) {
		this.legrain = legrain;
	}
	
	public TaLFabricationMP(int idLBonliv) {
		this.idLDocument = idLBonliv;
	}

	public TaLFabricationMP(int idLBonliv, TaTLigne taTLigne, TaArticle taArticle,
			TaFabrication taBonliv, Integer numLigneLBonliv, String libLBonliv,
			BigDecimal qteLBonliv, BigDecimal qte2LBonliv, String u1LBonliv,
			String u2LBonliv, BigDecimal prixULBonliv, BigDecimal tauxTvaLBonliv,
			String compteLBonliv, String codeTvaLBonliv, String codeTTvaLBonliv,
			BigDecimal mtHtLBonliv, BigDecimal mtTtcLBonliv,
			BigDecimal remTxLBonliv, BigDecimal remHtLBonliv,
			String quiCreeLBonliv, Date quandCreeLBonliv, String quiModifLBonliv,
			Date quandModifLBonliv, String ipAcces, Integer versionObj) {
		this.idLDocument = idLBonliv;
		this.taTLigne = taTLigne;
		this.taArticle = taArticle;
		this.taDocument = taBonliv;
		this.numLigneLDocument = numLigneLBonliv;
		this.libLDocument = libLBonliv;
		this.qteLDocument = qteLBonliv;
		this.qte2LDocument = qte2LBonliv;
		this.u1LDocument = u1LBonliv;
		this.u2LDocument = u2LBonliv;
		this.prixULDocument = prixULBonliv;
		this.tauxTvaLDocument = tauxTvaLBonliv;
		this.compteLDocument = compteLBonliv;
		this.codeTvaLDocument = codeTvaLBonliv;
		this.codeTTvaLDocument = codeTTvaLBonliv;
		this.mtHtLDocument = mtHtLBonliv;
		this.mtTtcLDocument = mtTtcLBonliv;
		this.remTxLDocument = remTxLBonliv;
		this.remHtLDocument = remHtLBonliv;
		this.quiCree = quiCreeLBonliv;
		this.quandCree = quandCreeLBonliv;
		this.quiModif = quiModifLBonliv;
		this.quandModif = quandModifLBonliv;
		this.ipAcces = ipAcces;
		this.versionObj = versionObj;
	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_l_fabrication")
//	@Column(name = "id_l_document", unique = true, nullable = false)
//	public int getIdLDocument() {
//		return this.idLDocument;
//	}
//
//	public void setIdLDocument(int idLBonliv) {
//		this.idLDocument = idLBonliv;
//	}

	@Column(name = "version", length = 20)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_t_ligne")
	@LgrHibernateValidated(champBd = "id_t_ligne",table = "ta_t_ligne", champEntite="taTLigne.idTLigne", clazz = TaTLigne.class)
	public TaTLigne getTaTLigne() {
		return this.taTLigne;
	}

	public void setTaTLigne(TaTLigne taTLigne) {
		this.taTLigne = taTLigne;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_article")
	@LgrHibernateValidated(champBd = "id_article",table = "ta_article", champEntite="taArticle.idArticle", clazz = TaArticle.class)
	public TaArticle getTaArticle() {
		return this.taArticle;
	}

//	public void setTaArticle(TaArticle taArticle) {
//		this.taArticle = taArticle;
//		if(legrain) {
//			if(taArticle!=null) {
//				if(getTaTLigne()==null || getTaTLigne().getCodeTLigne()==null)
//					setTaTLigne(new TaTLigneDAO().findByCode(Const.C_CODE_T_LIGNE_H));
//				else
//					if(!getTaTLigne().getCodeTLigne().equals(Const.C_CODE_T_LIGNE_H))
//						setTaTLigne(new TaTLigneDAO().findByCode(Const.C_CODE_T_LIGNE_H));
//
//				TaPrix prix = taArticle.chercheTarif(taDocument.getTaTiers());
//				if(prix!=null) {
//					if(taDocument!=null) { //#JPA
//						if(taDocument.getTtc()==1)
//							setPrixULDocument(prix.getPrixttcPrix());
//						else
//							setPrixULDocument(prix.getPrixPrix());
//					}
//
//					if(prix.getTaUnite()!=null) {
//						setU1LDocument(prix.getTaUnite().getCodeUnite());
//					}
//				}
//
//				if(taArticle.getTaTva()!=null && taDocument.isGestionTVA()) {
//					setCodeTvaLDocument(taArticle.getTaTva().getCodeTva());
//					setTauxTvaLDocument(taArticle.getTaTva().getTauxTva());
//				}
//
//				setLibLDocument(taArticle.getLibellecArticle());
//				setQteLDocument(new BigDecimal(1));
//				setCompteLDocument(taArticle.getNumcptArticle());
//
//				calculMontant();
//			}
//			
//			
//		}
//	}

	public void setTaArticle(TaArticle taArticle) {
		this.taArticle = taArticle;

		if(legrain) {
			if(taArticle!=null) {
				
				System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
				//passage ejb => dans TaXXXService
//				if(getTaTLigne()==null || getTaTLigne().getCodeTLigne()==null)
//					setTaTLigne(new TaTLigneDAO().findByCode(SWTDocument.C_CODE_T_LIGNE_H));
//				else
//					if(!getTaTLigne().getCodeTLigne().equals(SWTDocument.C_CODE_T_LIGNE_H))
//						setTaTLigne(new TaTLigneDAO().findByCode(SWTDocument.C_CODE_T_LIGNE_H));
				
				boolean commentaire=false;
				commentaire=taArticle.getTaFamille()!=null && taArticle.getTaFamille().getCodeFamille().equals("&&&");
				if(!commentaire){
					TaPrix prix = taArticle.chercheTarif(taDocument.getTaTiers());
					if(prix!=null) {
						if(taDocument!=null) { //#JPA
							if(taDocument.getTtc()==1)
								setPrixULDocument(prix.getPrixttcPrix());
							else
								setPrixULDocument(prix.getPrixPrix());
						}

						if(taArticle.getTaUnite1()!=null) {
							setU1LDocument(taArticle.getTaUnite1().getCodeUnite());
						}
					}else{
						setPrixULDocument(BigDecimal.valueOf(0));
						setU1LDocument("");
					}

					if(taArticle.getTaTva()!=null && taDocument.isGestionTVA()) {
						setCodeTvaLDocument(taArticle.getTaTva().getCodeTva());
						setTauxTvaLDocument(taArticle.getTaTva().getTauxTva());
					}else{
						setCodeTvaLDocument("");
						setTauxTvaLDocument(BigDecimal.valueOf(0));
					}
					setQteLDocument(new BigDecimal(1));
					setCompteLDocument(taArticle.getNumcptArticle());
					calculMontant();
					setLibLDocument(taArticle.getLibellecArticle());
				}
				else {
					setLibLDocument(taArticle.getLibellecArticle());
					setTaArticle(null);
				}
			}
		}
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_document")
	@LgrHibernateValidated(champBd = "id_document",table = "ta_fabrication", champEntite="taDocument.idDocument", clazz = TaFabrication.class)
	public TaFabrication getTaDocument() {
		return this.taDocument;
	}

	public void setTaDocument(TaFabrication taBonliv) {
		this.taDocument = taBonliv;
	}
	
	public void setTaDocumentGeneral(IDocumentTiers taDocument) {
		this.taDocument = (TaFabrication) taDocument;
	}

	@Column(name = "num_ligne_l_document")
	@LgrHibernateValidated(champBd = "num_ligne_l_document",table = "ta_l_fabrication", champEntite="numLigneLDocument", clazz = TaLFabricationMP.class)
	public Integer getNumLigneLDocument() {
		return this.numLigneLDocument;
	}

	public void setNumLigneLDocument(Integer numLigneLBonliv) {
		this.numLigneLDocument = numLigneLBonliv;
	}

	@Column(name = "lib_l_document")
	@LgrHibernateValidated(champBd = "lib_l_document",table = "ta_l_fabrication", champEntite="libLDocument", clazz = TaLFabricationMP.class)
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
	@LgrHibernateValidated(champBd = "qte_l_document",table = "ta_l_fabrication", champEntite="qteLDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getQteLDocument() {
		return this.qteLDocument;
	}

	public void setQteLDocument(BigDecimal qteLBonliv) {		
		BigDecimal qteLDocumentOld = this.qteLDocument;
		this.qteLDocument = qteLBonliv;
		if(legrain) {
			calculMontant();
			firePropertyChange(new PropertyChangeEvent(this,"qteLDocument",qteLDocumentOld,qteLBonliv));
		}
	}

	@Column(name = "qte2_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "qte2_l_document",table = "ta_l_fabrication", champEntite="qte2LDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getQte2LDocument() {
		return this.qte2LDocument;
	}

	public void setQte2LDocument(BigDecimal qte2LBonliv) {
		this.qte2LDocument = qte2LBonliv;
	}

	@Column(name = "u1_l_document", length = 20)
	@LgrHibernateValidated(champBd = "u1_l_document",table = "ta_l_fabrication", champEntite="u1LDocument", clazz = TaLFabricationMP.class)
	public String getU1LDocument() {
		return this.u1LDocument;
	}

	public void setU1LDocument(String u1LBonliv) {
		this.u1LDocument = u1LBonliv;
	}

	@Column(name = "u2_l_document", length = 20)
	@LgrHibernateValidated(champBd = "u2_l_document",table = "ta_l_fabrication", champEntite="u2LDocument", clazz = TaLFabricationMP.class)
	public String getU2LDocument() {
		return this.u2LDocument;
	}

	public void setU2LDocument(String u2LBonliv) {
		this.u2LDocument = u2LBonliv;
	}

	@Column(name = "prix_u_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "prix_u_l_document",table = "ta_l_fabrication", champEntite="prixULDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getPrixULDocument() {
		return this.prixULDocument;
	}

	public void setPrixULDocument(BigDecimal prixULBonliv) {
		this.prixULDocument = prixULBonliv;
		if(legrain) {
			calculMontant();
		}
	}
	
	@Transient
	public BigDecimal getPrixU2LDocument() {
		return prixU2LDocument;
	}
	@Transient
	public void setPrixU2LDocument(BigDecimal prixU2LDocument) {
		this.prixU2LDocument = prixU2LDocument;
	}
	
	@Column(name = "taux_tva_l_document", precision = 15, scale = 4)
	@LgrHibernateValidated(champBd = "taux_tva_l_document",table = "ta_l_fabrication", champEntite="tauxTvaLDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getTauxTvaLDocument() {
		return this.tauxTvaLDocument;
	}

	public void setTauxTvaLDocument(BigDecimal tauxTvaLBonliv) {
		this.tauxTvaLDocument = tauxTvaLBonliv;
	}

	@Column(name = "compte_l_document", length = 8)
	@LgrHibernateValidated(champBd = "compte_l_document",table = "ta_l_fabrication", champEntite="compteLDocument", clazz = TaLFabricationMP.class)
	public String getCompteLDocument() {
		return this.compteLDocument;
	}

	public void setCompteLDocument(String compteLBonliv) {
		this.compteLDocument = compteLBonliv;
	}

	@Column(name = "code_tva_l_document", length = 20)
	@LgrHibernateValidated(champBd = "code_tva_l_document",table = "ta_l_fabrication", champEntite="codeTvaLDocument", clazz = TaLFabricationMP.class)
	public String getCodeTvaLDocument() {
		return this.codeTvaLDocument;
	}

	public void setCodeTvaLDocument(String codeTvaLBonliv) {
		this.codeTvaLDocument = codeTvaLBonliv;
	}

	@Column(name = "code_t_tva_l_document", length = 1)
	@LgrHibernateValidated(champBd = "code_t_tva_l_document",table = "ta_l_fabrication", champEntite="codeTTvaLDocument", clazz = TaLFabricationMP.class)
	public String getCodeTTvaLDocument() {
		return this.codeTTvaLDocument;
	}

	public void setCodeTTvaLDocument(String codeTTvaLBonliv) {
		this.codeTTvaLDocument = codeTTvaLBonliv;
	}

	@Column(name = "mt_ht_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "mt_ht_l_document",table = "ta_l_fabrication", champEntite="mtHtLDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getMtHtLDocument() {
		return this.mtHtLDocument;
	}

	public void setMtHtLDocument(BigDecimal mtHtLBonliv) {
		this.mtHtLDocument = LibCalcul.arrondi(mtHtLBonliv);
	}

	@Column(name = "mt_ttc_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "mt_ttc_l_document",table = "ta_l_fabrication", champEntite="mtTtcLDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getMtTtcLDocument() {
		return this.mtTtcLDocument;
	}

	public void setMtTtcLDocument(BigDecimal mtTtcLBonliv) {
		this.mtTtcLDocument = LibCalcul.arrondi(mtTtcLBonliv);
	}

	@Column(name = "rem_tx_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "rem_tx_l_document",table = "ta_l_fabrication", champEntite="remTxLDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getRemTxLDocument() {
		return this.remTxLDocument;
	}

	public void setRemTxLDocument(BigDecimal remTxLBonliv) {
		this.remTxLDocument = remTxLBonliv;
		if(legrain) {
			//calculMontant();
			
			System.out.println("===****=== Transfert de code metier des entites vers les services, à bien vérifier");
			//passage ejb => dans TaXXXService
//			taDocument.calculeTvaEtTotaux();
		}
		
	}

	@Column(name = "rem_ht_l_document", precision = 15)
	@LgrHibernateValidated(champBd = "rem_ht_l_document",table = "ta_l_fabrication", champEntite="remHtLDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getRemHtLDocument() {
		return this.remHtLDocument;
	}

	public void setRemHtLDocument(BigDecimal remHtLBonliv) {
		this.remHtLDocument = remHtLBonliv;
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

//	@Version
//	@Column(name = "version_obj")
//	public Integer getVersionObj() {
//		return this.versionObj;
//	}
//
//	public void setVersionObj(Integer versionObj) {
//		this.versionObj = versionObj;
//	}

//	@Override
//	public boolean ligneEstVide() {
//		if (taArticle!=null)return false;
//		if (!libLDocument.equals(""))return false;		
//		return true;
//	}
	
	public void initialiseLigne(boolean reset){
		if (reset){
			setLibLDocument("");
			setQteLDocument(null);
			setQte2LDocument(null);
			setU1LDocument("") ;
			setU2LDocument("") ;
			setPrixULDocument(null);
			setTauxTvaLDocument(null);
			setCodeTvaLDocument("");
			setCodeTTvaLDocument("");
			setMtHtLDocument(null);
			setMtTtcLDocument(null);
			setRemTxLDocument(null);
			setRemHtLDocument(null);
		}else {
			setLibLDocument("");
			setQteLDocument(new BigDecimal(0));
			setQte2LDocument(new BigDecimal(0));
			setU1LDocument("");
			setU2LDocument("");
			setPrixULDocument(new BigDecimal(0)) ;
			setTauxTvaLDocument(new BigDecimal(0));
			setCodeTvaLDocument("");
			setCodeTTvaLDocument("");
			setMtHtLDocument(new BigDecimal(0));
			setMtTtcLDocument(new BigDecimal(0));
			setRemTxLDocument(new BigDecimal(0));
			setRemHtLDocument(new BigDecimal(0)) ;
		}
		setTaArticle(null);	
	}

	public void calculMontant() {
		if(qteLDocument!=null && prixULDocument!=null) {
			if(taDocument!=null) {//#JPA
				setTxRemHtDocument(taDocument.getTxRemHtDocument());
				if(taDocument.getTtc()==1) {					
					setMtTtcLDocument(qteLDocument.multiply(prixULDocument));
					if(remTxLDocument!=null && remTxLDocument.signum()!=0){
						setMtTtcLDocument(getMtTtcLDocument().subtract(getMtTtcLDocument().multiply(remTxLDocument.divide(new BigDecimal(100)))));
					}
					if( tauxTvaLDocument!=null && tauxTvaLDocument.signum()!=0){
						setMtHtLDocument(mtTtcLDocument.divide(BigDecimal.valueOf(1).add((tauxTvaLDocument.divide(new BigDecimal(100),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP)
								)),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP));
					} else {
						setMtHtLDocument(mtTtcLDocument);
					}
					
				} else {
					setMtHtLDocument(qteLDocument.multiply(prixULDocument));
					if(remTxLDocument!=null && remTxLDocument.signum()!=0){
						setMtHtLDocument(getMtHtLDocument().subtract(getMtHtLDocument().multiply(remTxLDocument.divide(new BigDecimal(100)))));
					}
					if( tauxTvaLDocument!=null && tauxTvaLDocument.signum()!=0){
						setMtTtcLDocument(mtHtLDocument.add((mtHtLDocument.multiply(tauxTvaLDocument.divide(new BigDecimal(100))))));
					} else {
						setMtTtcLDocument(mtHtLDocument);
					}
				
				}
				setMtTtcLApresRemiseGlobaleDocument(getMtTtcLDocument());
				setMtHtLApresRemiseGlobaleDocument(getMtHtLDocument());
			}
		}
	}
	
	@Transient
	public BigDecimal getTxRemHtDocument() {
		return txRemHtDocument;
	}

	public void setTxRemHtDocument(BigDecimal txRemHtBonliv) {
		this.txRemHtDocument = txRemHtBonliv;
	}

	//@Transient
	@Column(name = "mt_ht_apr_rem_globale", precision = 15)
	@LgrHibernateValidated(champBd = "mt_apr_rem_globale",table = "ta_l_fabrication", champEntite="mtHtLApresRemiseGlobaleDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getMtHtLApresRemiseGlobaleDocument() {
		return mtHtLApresRemiseGlobaleDocument;
	}

	public void setMtHtLApresRemiseGlobaleDocument(
			BigDecimal mtHtLApresRemiseGlobaleDocument) {
		this.mtHtLApresRemiseGlobaleDocument = mtHtLApresRemiseGlobaleDocument;
	}

	//@Transient
	@Column(name = "mt_ttc_apr_rem_globale", precision = 15)
	@LgrHibernateValidated(champBd = "mt_ttc_apr_rem_globale",table = "ta_l_fabrication", champEntite="mtTtcLApresRemiseGlobaleDocument", clazz = TaLFabricationMP.class)
	public BigDecimal getMtTtcLApresRemiseGlobaleDocument() {
		return mtTtcLApresRemiseGlobaleDocument;
	}

	public void setMtTtcLApresRemiseGlobaleDocument(
			BigDecimal mtTtcLApresRemiseGlobaleDocument) {
		this.mtTtcLApresRemiseGlobaleDocument = mtTtcLApresRemiseGlobaleDocument;
	}

	public void setLegrain(boolean legrain) {
		this.legrain = legrain;
	}

	@NotNull(message="Le lot ne peut être nul")
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH})
	@JoinColumn(name = "id_lot")
	@LgrHibernateValidated(champBd = "id_lot",table = "ta_lot", champEntite="taLot.idLot", clazz = TaLot.class)
	@Valid
	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id_entrepot")
	@LgrHibernateValidated(champBd = "id_entrepot",table = "ta_entrepot", champEntite="taEntrepot.idEntrepot", clazz = TaEntrepot.class)
	public TaEntrepot getTaEntrepot() {
		return taEntrepot;
	}

	public void setTaEntrepot(TaEntrepot taEntrepot) {
		this.taEntrepot = taEntrepot;
	}

	@Column(name = "emplacement_l_document")
	@LgrHibernateValidated(champBd = "emplacement_l_document",table = "ta_l_fabrication", champEntite="emplacementLDocument", clazz = TaLFabricationMP.class)
	public String getEmplacementLDocument() {
		return this.emplacementLDocument;
	}

	public void setEmplacementLDocument(String emplacementLDocument) {
		this.emplacementLDocument = emplacementLDocument;
	}

	@NotNull
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_mouvement_stock")
	@LgrHibernateValidated(champBd = "id_mouvement_stock",table = "ta_mouvement_stock", champEntite="taMouvementStock.idMouvementStock", clazz = TaMouvementStock.class)
	public TaMouvementStock getTaMouvementStock() {
		return taMouvementStock;
	}

	public void setTaMouvementStock(TaMouvementStock taMouvementStock) {
		this.taMouvementStock = taMouvementStock;
	}


	@Transient
	public List<TaLigneALigneSupplementDTO> getListeSupplement() {
		// TODO Auto-generated method stub
		return listeSupplement;
	}

	@Transient
	public void setListeSupplement(List<TaLigneALigneSupplementDTO> listeSupplement) {
		// TODO Auto-generated method stub
		this.listeSupplement=listeSupplement;
	}

	@Transient
	public List<TaLigneALigneSupplementDTO> getListeLigneAIntegrer() {
		// TODO Auto-generated method stub
		return listeLigneAIntegrer;
	}

	@Transient
	public void setListeLigneAIntegrer(List<TaLigneALigneSupplementDTO> listeLigneAIntegrer) {
		// TODO Auto-generated method stub
		this.listeLigneAIntegrer=listeLigneAIntegrer;
	}

	//@NotNull
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_mouvement_stock_prev")
	public TaMouvementStockPrev getTaMouvementStockPrev() {
		return taMouvementStockPrev;
	}

	public void setTaMouvementStockPrev(TaMouvementStockPrev taMouvementStockPrev) {
		this.taMouvementStockPrev = taMouvementStockPrev;
	}

	@Transient
	public Set<TaLigneALigne> getTaLigneALignes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setTaLigneALignes(Set<TaLigneALigne> taLigneALignes) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public Set<TaREtatLigneDocument> getTaREtatLigneDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setTaREtatLigneDocuments(Set<TaREtatLigneDocument> taREtats) {
		// TODO Auto-generated method stub
		
	}

	
	public void addREtatLigneDoc(TaEtat taEtat) {
//		TaREtatLigneDocument rEtat=getTaREtatLigneDocument();
//		if(rEtat!=null) {
//			TaHistREtatLigneDocument hist=new TaHistREtatLigneDocument();
//			hist.setTaEtat(getTaREtatLigneDocument().getTaEtat());
//			hist.setTaLFabricationMP(this);
//			this.getTaHistREtatLigneDocuments().add(hist);
//		}else rEtat=new TaREtatLigneDocument();
//		
//		rEtat.setTaEtat(taEtat);
//		rEtat.setTaLFabricationMP(this);
//		this.setTaREtatLigneDocument(rEtat);
//		this.getTaREtatLigneDocuments().add(rEtat);
	}

	@Transient
	public Set<TaHistREtatLigneDocument> getTaHistREtatLigneDocuments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setTaHistREtatLigneDocuments(Set<TaHistREtatLigneDocument> taHistREtatLigneDocuments) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public Set<TaLigneALigneEcheance> getTaLigneALignesEcheance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setTaLigneALignesEcheance(Set<TaLigneALigneEcheance> taLigneALignes) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public Boolean getAbonnement() {
		// TODO Auto-generated method stub
		return false;
	}

	@Transient
	public void setAbonnement(Boolean abonnement) {
		// TODO Auto-generated method stub
		
	}
	
	@Transient
	public String getUSaisieLDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setUSaisieLDocument(String uSaisieLDocument) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	public BigDecimal getQteSaisieLDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	public void setQteSaisieLDocument(BigDecimal qteSaisieLDocument) {
		// TODO Auto-generated method stub
		
	}

}
