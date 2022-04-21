package fr.legrain.article.model;

// Generated Sep 1, 2008 3:06:27 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
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
import javax.persistence.OrderBy;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaRPrix;
import fr.legrain.tiers.model.TaRPrixTiers;
import fr.legrain.tiers.model.TaTTarif;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.LgrHibernateValidated;





@SqlResultSetMapping(name="TaArticleDTO",
classes = {
    @ConstructorResult(
            targetClass = TaArticleDTO.class,
            columns = {
                @ColumnResult(name = "id_Article",type = int.class),
                @ColumnResult(name = "code_Article",type = String.class),
                @ColumnResult(name = "libellec_article",type = String.class),
                @ColumnResult(name = "id_Prix",type = int.class),
                @ColumnResult(name = "prix_Prix",type = BigDecimal.class),
                @ColumnResult(name = "prixttc_Prix",type = BigDecimal.class),
                @ColumnResult(name = "code_Unite",type = String.class),
                @ColumnResult(name = "code_Famille",type = String.class),
                @ColumnResult(name = "code_Tva",type = String.class),
                @ColumnResult(name = "taux_Tva",type = BigDecimal.class),
                @ColumnResult(name = "code_T_Tarif",type = String.class),
                @ColumnResult(name = "ht_calc",type = BigDecimal.class),
                @ColumnResult(name = "ttc_calc",type = BigDecimal.class)
            })   
    
})
@Entity
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
//@Inheritance(strategy=InheritanceType.JOINED)

@Table(name = "ta_article")
//@SequenceGenerator(name = "gen_article", sequenceName = "num_id_article", allocationSize = 1)
@NamedQueries(value = { 
		@NamedQuery(name=TaArticle.QN.FIND_BY_IMPORT, query="select a from TaArticle a where a.origineImport = :origineImport and a.idImport = :idImport"),
		//@NamedQuery(name=TaArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER, query="select a from TaArticle a where a.quandCreeArticle >= ? or a.quandModifArticle >= ?"),
//		@NamedQuery(name=TaArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER, 
//		query="select distinct a from TaArticle a " +
//				"left join a.taPrixes p " +
//				"left join a.taLabelArticles lab " +
//				"left join a.taCategorieArticles cat " +
//				"left join a.taImageArticles img " +
//				"left join a.taConditionnementsArticle cond " +
//				"left join a.taRapportUnites ru " +
//				"left join a.taCatalogueWeb web " +
//				"where a.quandCree >= :dateLimite or a.quandModif >= :dateLimite " +
//				"or p.quandCree >= :dateLimite or p.quandModif >= :dateLimite " +
//				"or lab.quandCree>= :dateLimite or lab.quandModif >= :dateLimite " +
//				"or cat.quandCree >= :dateLimite or cat.quandModif >= :dateLimite " +
//				"or img.quandCree >= :dateLimite or img.quandModif >= :dateLimite " +
//				"or cond.quandCree >= :dateLimite or cond.quandModif >= :dateLimite " +
//				"or ru.quandCree >= :dateLimite or ru.quandModif >= :dateLimite " +
//				"or web.quandCree >= :dateLimite or web.quandModif >= :dateLimite " +
//
//				""),
				
//				query="select distinct a from TaArticle a " +
//						"left join a.taPrixes p " +
//						"left join a.taLabelArticles lab " +
//						"left join a.taCategorieArticles cat " +
//						"left join a.taImageArticles img " +
//						"left join a.taConditionnementsArticle cond " +
//						"left join a.taRapportUnites ru " +
//						"left join a.taCatalogueWeb web " +
//						"where a.quandCreeArticle >= ? or a.quandModifArticle >= ? " +
//						"or p.quandCreePrix >= ? or p.quandModifPrix >= ? " +
//
//						"or lab.quandCreeLabelArticle >= ? or lab.quandModifLabelArticle >= ? " +
//						"or cat.quandCreeCategorieArticle >= ? or cat.quandModifCategorieArticle >= ? " +
//						"or img.quandCreeImageArticle >= ? or img.quandModifImageArticle >= ? " +
//						"or cond.quandCreeUnite >= ? or cond.quandModifUnite >= ? " +
//						"or ru.quandCree >= ? or ru.quandModif >= ? " +
//						"or web.quandCreeCatalogueWeb >= ? or web.quandModifCatalogueWeb >= ? " +
//
//						""),
		
		
		
		
		@NamedQuery(name=TaArticle.QN.FIND_BY_ACTIF, query="select a from TaArticle a where a.actif = :actif order by codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_BY_ACTIF_LIGHT, query="select a.codeArticle,a.libellecArticle,fam.codeFamille,a.numcptArticle,p.prixPrix,p.prixttcPrix,un.codeUnite,tva.codeTva,ttva.codeTTva,a.diversArticle,a.commentaireArticle from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un  where a.actif = :actif order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_BY_ECRAN_LIGHT, query="select a.codeArticle,a.libellecArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un   order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_ID_AND_CODE, query="select new map( a.idArticle  as id, a.codeArticle as code) from TaArticle a   order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_CODE_AND_LIBELLE, query="select new map( a.codeArticle  as code, a.libellecArticle as libelle) from TaArticle a   order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_BY_CODE_LIGHT, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un where  a.actif=1 and a.codeArticle like :code order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_BY_CODE_AND_LIBELLE_LIGHT, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un where  a.actif=1 and (a.codeArticle like :code or upper(a.libellecArticle) like :code) order by a.codeArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_ALL_LIGHT2, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,fam.codeFamille,a.actif,a.matierePremiere,a.produitFini,uRef.codeUnite) from TaArticle a left join a.taFamille fam left join a.taUniteReference uRef order by fam.codeFamille, a.codeArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_ALL_NON_COMPOSE, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,fam.codeFamille,a.actif,a.matierePremiere,a.produitFini,uRef.codeUnite) from TaArticle a left join a.taFamille fam left join a.taUniteReference uRef where a.compose = false order by fam.codeFamille, a.codeArticle "),
		
		@NamedQuery(name=TaArticle.QN.FIND_ARTICLE_CAISSE_LIGHT,    query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,fam.codeFamille,a.actif,a.matierePremiere,a.produitFini,uRef.codeUnite) from TaArticle a left join a.taFamille fam left join a.taUniteReference uRef where  a.actif=1 and fam.codeFamille = :codeFamille order by a.codeArticle"),

		@NamedQuery(name=TaArticle.QN.COUNT_ALL_ACTIF_ARTICLE, 
			query="select new fr.legrain.article.dto.TaArticleDTO(cast (count(*) as integer),a.actif) "
				+ "from TaArticle a where a.actif=1 "
				+ "group by a.actif"),
		@NamedQuery(name=TaArticle.QN.FIND_ALL_LIGHT_ACTIF, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.commentaireArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un where a.actif=1 order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_ALL_LIGHT, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva,a.codeBarre) from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_LIGHT_CODE_T_TARIF, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) "
				+ " from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un "
				+ " where  a.actif=1 and exists(select rp from TaRPrix rp join rp.taPrix p join p.taArticle aa join rp.taTTarif tt  where aa.idArticle=a.idArticle and tt.codeTTarif like :codeTTarif)"
				+ " order by a.codeArticle"),
		@NamedQuery(name=TaArticle.QN.FIND_LIGHT_CODE_T_TARIF_TIERS, query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva) "
				+ " from TaArticle a left join a.taFamille fam left join a.taPrix p left join a.taTva tva left join a.taTTva ttva left join a.taUnite1 un "
				+ " where  a.actif=1  and exists(select rp from TaRPrixTiers rp join rp.taPrix p join p.taArticle aa  join rp.taTTarif tt join rp.taTiers tiers  where aa.idArticle=a.idArticle and tt.codeTTarif like :codeTTarif and tiers.codeTiers like :codeTiers)"
				+ " order by a.codeArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_NOMENCLATURE_DTO_BY_ID_ARTICLE, query="select new fr.legrain.article.dto.TaArticleComposeDTO(ac.idArticleCompose,a.idArticle,a.codeArticle,a.libellecArticle,"
				+ "ac.qte, ac.qte2, ac.u1, ac.u2, p.prixPrix, p.prixttcPrix"
				+ ") "
				+ " from TaArticleCompose ac left join ac.taArticle a left join a.taPrix p "
				+ " where ac.taArticleParent.idArticle = :idArticle"
				+ " order by a.codeArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_NOMENCLATURE_BY_ID_ARTICLE, query="select ac "
				+ " from TaArticleCompose ac left join fetch ac.taArticle a left join a.taPrix p "
				+ " where ac.taArticleParent.idArticle = :idArticle"
				+ " order by a.codeArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_ARTICLE_ENFANT_BY_ID_ARTICLE_PARENT, query="select ac.taArticle "
				+ " from TaArticleCompose ac left join ac.taArticle a left join a.taPrix p "
				+ " where ac.taArticleParent.idArticle = :idArticle"
				+ " order by a.codeArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_ALL_LIGHT_CATALOGUE, 
			query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,"
					+ "a.numcptArticle,p.prixPrix,p.prixttcPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva,a.codeBarre,"
					+ "cat.idCatalogueWeb, cat.urlRewritingCatalogueWeb, cat.nouveauteCatalogueWeb, cat.descriptionLongueCatWeb,"
					+ "cat.exportationCatalogueWeb, cat.expediableCatalogueWeb, cat.specialCatalogueWeb,"
					+ "cat.libelleCatalogue,cat.nonDisponibleCatalogueWeb,cat.fraisPortAdditionnel,cat.resumeCatWeb,a.prixVariable"
					+ ") "
					+ "from TaArticle a left join a.taFamille fam left join a.taTva tva left join a.taTTva ttva "
					+ "left join a.taUnite1 un join a.taCatalogueWeb cat left join cat.taPrixCatalogueDefaut p "
					+ "where a.actif=1 and cat.exportationCatalogueWeb = true order by a.codeArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_LIGHT_CATALOGUE, 
		query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,"
				+ "a.numcptArticle,p.prixPrix,p.prixttcPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva,a.codeBarre,"
				+ "cat.idCatalogueWeb, cat.urlRewritingCatalogueWeb, cat.nouveauteCatalogueWeb, cat.descriptionLongueCatWeb, "
				+ "cat.exportationCatalogueWeb, cat.expediableCatalogueWeb, cat.specialCatalogueWeb,"
				+ "cat.libelleCatalogue,cat.nonDisponibleCatalogueWeb,cat.fraisPortAdditionnel,cat.resumeCatWeb,a.prixVariable"
				+ ") "
				+ "from TaArticle a left join a.taFamille fam left join a.taTva tva left join a.taTTva ttva "
				+ "left join a.taUnite1 un join a.taCatalogueWeb cat left join cat.taPrixCatalogueDefaut p "
				+ "where a.actif=1 and cat.exportationCatalogueWeb = true  and a.idArticle = :idArticle"),
		
		@NamedQuery(name=TaArticle.QN.FIND_ALL_LIGHT_CATALOGUE_CATEGORIE, 
		query="select new fr.legrain.article.dto.TaArticleDTO(a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,"
				+ "a.numcptArticle,p.prixPrix,p.prixttcPrix,un.codeUnite,fam.codeFamille,tva.codeTva,tva.tauxTva,ttva.codeTTva,a.codeBarre,"
				+ "cat.idCatalogueWeb, cat.urlRewritingCatalogueWeb, cat.nouveauteCatalogueWeb, cat.descriptionLongueCatWeb, "
				+ "cat.exportationCatalogueWeb, cat.expediableCatalogueWeb, cat.specialCatalogueWeb,"
				+ "cat.libelleCatalogue,cat.nonDisponibleCatalogueWeb,cat.fraisPortAdditionnel,cat.resumeCatWeb,a.prixVariable"
				+ ") "
				+ "from TaCategorieArticle categ left join categ.taArticles a left join a.taFamille fam left join a.taTva tva left join a.taTTva ttva "
				+ "left join a.taUnite1 un join a.taCatalogueWeb cat left join cat.taPrixCatalogueDefaut p "
				+ "where a.actif=1 and cat.exportationCatalogueWeb = true and categ.idCategorieArticle = :categ "),

})



public class TaArticle implements java.io.Serializable {

	private static final long serialVersionUID = -612143505127848521L;

	public static final String TYPE_DOC = "Article";
	public static final String PATH_ICONE_COULEUR = "dashboard/article.svg";
	public static final String PATH_ICONE_BLANC = "";
	public static final String PATH_ICONE_GRIS = "";
	
	public static class QN {
		public static final String FIND_BY_IMPORT = "TaArticle.findByImport";
		public static final String FIND_BY_ACTIF = "TaArticle.findByActif";
		public static final String FIND_BY_ACTIF_LIGHT = "TaArticle.findByActifLight";
		public static final String FIND_BY_ECRAN_LIGHT = "TaArticle.findByEcranLight";
		public static final String FIND_ID_AND_CODE = "TaArticle.findArticleEtCode";
		public static final String FIND_BY_NEW_OR_UPDATED_AFTER = "TaArticle.findByNewOrUpdatedAfter";
		public static final String FIND_CODE_AND_LIBELLE = "TaArticle.findCodeEtLibelle";
		public static final String FIND_BY_CODE_LIGHT = "TaArticle.findByCodeLight";
		public static final String FIND_BY_CODE_AND_LIBELLE_LIGHT= "TaArticle.findByCodeOrLibelleLight";
		public static final String FIND_ARTICLE_CAISSE_LIGHT = "TaArticle.findArticleCaisseLight";
		public static final String FIND_ALL_LIGHT = "TaArticle.findAllLight";
		public static final String FIND_ALL_LIGHT_ACTIF = "TaArticle.findAllLightActif";
		public static final String COUNT_ALL_ACTIF_ARTICLE = "TaArticle.countAllArticleActif";
		public static final String FIND_LIGHT_CODE_T_TARIF = "TaArticle.findLightCodeTTarif";
		public static final String FIND_LIGHT_CODE_T_TARIF_TIERS = "TaArticle.findLightCodeTTarifTiers";
		
		public static final String FIND_ALL_LIGHT_CATALOGUE = "TaArticle.findAllLightCatalogue";
		public static final String FIND_ALL_LIGHT_CATALOGUE_CATEGORIE = "TaArticle.findAllLightCatalogueCategorie";
		public static final String FIND_LIGHT_CATALOGUE = "TaArticle.findLightCatalogue";
		
		public static final String FIND_ALL_LIGHT2 = "TaArticle.findAllLight2";
		public static final String FIND_ALL_NON_COMPOSE = "TaArticle.findAllNonCompose";
		
		public static final String FIND_NOMENCLATURE_DTO_BY_ID_ARTICLE= "TaArticle.findNomenclatureDTOByIdArticle";
		public static final String FIND_NOMENCLATURE_BY_ID_ARTICLE= "TaArticle.findNomenclatureByIdArticle";
		public static final String FIND_ARTICLE_ENFANT_BY_ID_ARTICLE_PARENT="TaArticle.findArticleEnfantByIdArticleParent";
	}
	
	@Transient
	static Logger logger = Logger.getLogger(TaArticle.class.getName());

	private int idArticle;
	private String version;
	private TaFamille taFamille;
	private TaTTva taTTva;
	private TaTva taTva;
	private TaPrix taPrix;
	private TaUnite taUnite1;
	private TaUnite taConditionnementArticle;
	private TaUnite taUniteReference;
	private TaUnite taUniteSaisie;
	private TaImageArticle taImageArticle;
	private TaCatalogueWeb taCatalogueWeb;
	private TaRapportUnite taRapportUnite;
	private TaRecette taRecetteArticle;
	private TaMarqueArticle taMarqueArticle;

	private Boolean utiliseDlc;
	private TaUnite taUniteStock;
	
	//private TaTitreTransport taTitreTransport;
	private TaRTaTitreTransport taRTaTitreTransport;
	private String codeArticle;
	private String libellecArticle;
	private String libellelArticle;
	private String numcptArticle;
	private String diversArticle;
	private String commentaireArticle;
	private BigDecimal stockMinArticle;
	private BigDecimal longueur;
	private BigDecimal largeur;
	private BigDecimal hauteur;
	private BigDecimal poids;
	private Integer actif;
	private String idImport;
	private String origineImport;
	private String paramDluo;
	
	private String codeBarre;
	
	private Boolean matierePremiere;
	private Boolean produitFini;
	private Boolean prixVariable;
	
	private Boolean gestionLot;
	private Boolean gestionStock=true;
	private Boolean autoAlimenteFournisseurs=false;
	
	private String quiCree;
	private Date quandCree;
	private String quiModif;
	private Date quandModif;
	private String ipAcces;
	
	private String importationDivers;
	
	
	//private Set<TaRefPrix> taRefPrixes = new HashSet<TaRefPrix>(0);
	private Set<TaPrix> taPrixes = new HashSet<TaPrix>(0);
	private Set<TaUnite> taConditionnementsArticle = new HashSet<TaUnite>(0);
	private Set<TaRapportUnite> taRapportUnites = new HashSet<TaRapportUnite>(0);
	private Integer versionObj;
	private Set<TaNoteArticle> taNotes = new HashSet<TaNoteArticle>(0);
	
//	private Set<TaRChampSuppArt> taRChampSuppArtes = new HashSet<TaRChampSuppArt>(0);
	
	private Set<TaImageArticle> taImageArticles = new HashSet<TaImageArticle>(0);
	
	private Set<TaCategorieArticle> taCategorieArticles = new HashSet<TaCategorieArticle>(0);
	
//	private Set<TaTArticle> taTArticles = new HashSet<TaTArticle>(0);
	
	private Set<TaLabelArticle> taLabelArticles = new HashSet<TaLabelArticle>(0);
	
	private Set<TaRefArticleFournisseur> taRefArticleFournisseurs = new HashSet<TaRefArticleFournisseur>(0);
	
	private Set<TaTiers> taFournisseurs = new HashSet<TaTiers>(0);
	
	private Set<TaFamille> taFamilles = new HashSet<TaFamille>(0);
	
	private Set<TaConformite> taConformites = new HashSet<TaConformite>(0);

	//private Set<TaRTArticle> taRTArticles = new HashSet<TaRTArticle>(0);
	
	private Set<TaArticleCompose> nomenclature = new HashSet<TaArticleCompose>(0);
	
	//private Set<TaJourRelance> taJourRelance = new HashSet<TaJourRelance>(0);
	
	private Boolean compose;
	private Boolean abonnement;
	
	private TaComportementArticleCompose taComportementArticleCompose;
	
	private Integer delaiSurvie;
	private Integer delaiGrace;
	
	private String liblCoefMultiplicateur;
	private Boolean coefMultiplicateur;
	
	private String codeModuleBdg;
	private String liblModuleBdg;
	
	public TaArticle() {
	}

	public TaArticle(int idArticle) {
		this.idArticle = idArticle;
	}

	public TaArticle(int idArticle, TaFamille taFamille, TaTTva taTTva,
			TaTva taTva, String codeArticle, String libellecArticle,
			String libellelArticle, String numcptArticle, String diversArticle,
			String commentaireArticle, BigDecimal stockMinArticle,
			String quiCreeArticle, Date quandCreeArticle,
			String quiModifArticle, Date quandModifArticle, String ipAcces,
			Set<TaRefPrix> taRefPrixes, Set<TaPrix> taPrixes, Set<TaCategorieArticle> taCategorieArticles,
			Integer actif) {
		this.idArticle = idArticle;
		this.taFamille = taFamille;
		this.taTTva = taTTva;
		this.taTva = taTva;
		this.codeArticle = codeArticle;
		this.libellecArticle = libellecArticle;
		this.libellelArticle = libellelArticle;
		this.numcptArticle = numcptArticle;
		this.diversArticle = diversArticle;
		this.commentaireArticle = commentaireArticle;
		this.stockMinArticle = stockMinArticle;
		this.quiCree = quiCreeArticle;
		this.quandCree = quandCreeArticle;
		this.quiModif = quiModifArticle;
		this.quandModif = quandModifArticle;
		this.ipAcces = ipAcces;
		//this.taRefPrixes = taRefPrixes;
		this.taPrixes = taPrixes;
		this.taCategorieArticles = taCategorieArticles;
		this.actif = actif;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_article", unique = true, nullable = false)
	@LgrHibernateValidated(champBd = "id_article",table = "ta_article", champEntite="idArticle", clazz = TaArticle.class)
	public int getIdArticle() {
		return this.idArticle;
	}

	public void setIdArticle(int idArticle) {
		this.idArticle = idArticle;
	}

	//@Version
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

	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	//cascade = {CascadeType.PERSIST, CascadeType.MERGE}
	@JoinColumn(name = "id_famille")
	@LgrHibernateValidated(champBd = "id_famille",table = "ta_article", champEntite="taFamille.idFamille", clazz = TaFamille.class)
	public TaFamille getTaFamille() {
		return this.taFamille;
	}

	public void setTaFamille(TaFamille taFamille) {
		this.taFamille = taFamille;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_t_tva")
	@LgrHibernateValidated(champBd = "id_t_tva",table = "ta_article", champEntite="taTTva.idTTva", clazz = TaTTva.class)
	public TaTTva getTaTTva() {
		return this.taTTva;
	}

	public void setTaTTva(TaTTva taTTva) {
		this.taTTva = taTTva;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {/*CascadeType.PERSIST, CascadeType.MERGE,*/ CascadeType.REFRESH})
	@JoinColumn(name = "id_tva")
	@LgrHibernateValidated(champBd = "id_tva",table = "ta_article",champEntite="taTva.idTva", clazz = TaTva.class)
	public TaTva getTaTva() {
		return this.taTva;
	}

	public void setTaTva(TaTva taTva) {
		this.taTva = taTva;
	}

	@Column(name = "code_article", length = 20)
	@LgrHibernateValidated(champBd = "code_article",table = "ta_article",champEntite="codeArticle", clazz = TaArticle.class)
	public String getCodeArticle() {
		return this.codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	@Column(name = "libellec_article", length = 100)
	@LgrHibernateValidated(champBd = "libellec_article",table = "ta_article",champEntite="libellecArticle", clazz = TaArticle.class)
	public String getLibellecArticle() {
		return this.libellecArticle;
	}

	public void setLibellecArticle(String libellecArticle) {
		this.libellecArticle = libellecArticle;
	}

	@Column(name = "libellel_article")
	@LgrHibernateValidated(champBd = "libellel_article",table = "ta_article",champEntite="libellelArticle", clazz = TaArticle.class)
	public String getLibellelArticle() {
		return this.libellelArticle;
	}

	public void setLibellelArticle(String libellelArticle) {
		this.libellelArticle = libellelArticle;
	}

	@Column(name = "numcpt_article", length = 8)
	@LgrHibernateValidated(champBd = "numcpt_article",table = "ta_article",champEntite="numcptArticle", clazz = TaArticle.class)
	public String getNumcptArticle() {
		return this.numcptArticle;
	}

	public void setNumcptArticle(String numcptArticle) {
		this.numcptArticle = numcptArticle;
	}

	@Column(name = "divers_article")
	@LgrHibernateValidated(champBd = "divers_article",table = "ta_article",champEntite="diversArticle", clazz = TaArticle.class)
	public String getDiversArticle() {
		return this.diversArticle;
	}

	public void setDiversArticle(String diversArticle) {
		this.diversArticle = diversArticle;
	}

	@Column(name = "commentaire_article")
	@LgrHibernateValidated(champBd = "commentaire_article",table = "ta_article",champEntite="commentaireArticle", clazz = TaArticle.class)
	public String getCommentaireArticle() {
		return this.commentaireArticle;
	}

	public void setCommentaireArticle(String commentaireArticle) {
		this.commentaireArticle = commentaireArticle;
	}

	@Column(name = "stock_min_article", precision = 15)
	@LgrHibernateValidated(champBd = "stock_min_article",table = "ta_article",champEntite="stockMinArticle", clazz = TaArticle.class)
	public BigDecimal getStockMinArticle() {
		return this.stockMinArticle;
	}

	public void setStockMinArticle(BigDecimal stockMinArticle) {
		this.stockMinArticle = stockMinArticle;
	}
	
	@Column(name = "actif")
	@LgrHibernateValidated(champBd = "actif",table = "ta_article",champEntite="actif", clazz = TaArticle.class)
	public Integer getActif() {
		return this.actif;
	}

	public void setActif(Integer actif) {
		this.actif = actif;
	}
	
	@Column(name = "origine_import", length = 100)
	@LgrHibernateValidated(champBd = "origine_import",table = "ta_article",champEntite="origineImport", clazz = TaArticle.class)
	public String getOrigineImport() {
		return this.origineImport;
	}

	public void setOrigineImport(String origineImport) {
		this.origineImport = origineImport;
	}
	
	@Column(name = "id_import", length = 100)
	@LgrHibernateValidated(champBd = "id_import",table = "ta_article",champEntite="idImport", clazz = TaArticle.class)
	public String getIdImport() {
		return this.idImport;
	}

	public void setIdImport(String idImport) {
		this.idImport = idImport;
	}


	@Column(name = "qui_cree", length = 50)
	public String getQuiCree() {
		return this.quiCree;
	}

	public void setQuiCree(String quiCreeArticle) {
		this.quiCree = quiCreeArticle;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_cree", length = 19)
	public Date getQuandCree() {
		return this.quandCree;
	}

	public void setQuandCree(Date quandCreeArticle) {
		this.quandCree = quandCreeArticle;
	}

	@Column(name = "qui_modif", length = 50)
	public String getQuiModif() {
		return this.quiModif;
	}

	public void setQuiModif(String quiModifArticle) {
		this.quiModif = quiModifArticle;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "quand_modif", length = 19)
	public Date getQuandModif() {
		return this.quandModif;
	}

	public void setQuandModif(Date quandModifArticle) {
		this.quandModif = quandModifArticle;
	}

	@Column(name = "ip_acces", length = 50)
	public String getIpAcces() {
		return this.ipAcces;
	}

	public void setIpAcces(String ipAcces) {
		this.ipAcces = ipAcces;
	}

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taArticle")
//	public Set<TaRefPrix> getTaRefPrixes() {
//		return this.taRefPrixes;
//	}
//
//	public void setTaRefPrixes(Set<TaRefPrix> taRefPrixes) {
//		this.taRefPrixes = taRefPrixes;
//	}

	@OrderBy("idPrix")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taArticle", orphanRemoval=true)
	public Set<TaPrix> getTaPrixes() {
		return this.taPrixes;
	}

	public void setTaPrixes(Set<TaPrix> taPrixes) {
		this.taPrixes = taPrixes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taArticle", orphanRemoval=true)
	public Set<TaUnite> getTaConditionnementsArticle() {
		return this.taConditionnementsArticle;
	}

	public void setTaConditionnementsArticle(Set<TaUnite> taConditionnementsArticle) {
		this.taConditionnementsArticle = taConditionnementsArticle;
	}
	
	@OrderBy("id")
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taArticle", orphanRemoval=true)
	public Set<TaRapportUnite> getTaRapportUnites() {
		return this.taRapportUnites;
	}

	public void setTaRapportUnites(Set<TaRapportUnite> taRapportUnites) {
		this.taRapportUnites = taRapportUnites;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.REFRESH} , orphanRemoval=true)
//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL ,mappedBy = "taArticle")
	@JoinColumn(name = "id_prix")
	@LgrHibernateValidated(champBd = "id_prix",table = "ta_article",champEntite="taPrix.idPrix", clazz = TaPrix.class)
	public TaPrix getTaPrix() {
		return taPrix;
	}

	public void setTaPrix(TaPrix taPrix) {
		this.taPrix = taPrix;
	}
	
//	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "id_conditionnement_article")
//	@LgrHibernateValidated(champ = "id_conditionnement_article",table = "ta_article",champEntite="taConditionnementArticle", clazz = TaUnite.class)
//	public TaUnite getTaConditionnementArticle() {
//		return taConditionnementArticle;
//	}
//
//	public void setTaConditionnementArticle(TaUnite taConditionnementArticle) {
//		this.taConditionnementArticle = taConditionnementArticle;
//	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE, CascadeType.REFRESH} , orphanRemoval=true)
//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL , mappedBy = "taArticle")
	@JoinColumn(name = "id_rapport_unite")
	@LgrHibernateValidated(champBd = "id_rapport_unite",table = "ta_article",champEntite="taRapportUnite.id", clazz = TaRapportUnite.class)
	public TaRapportUnite getTaRapportUnite() {
		return taRapportUnite;
	}

	public void setTaRapportUnite(TaRapportUnite taRapportUnite) {
		this.taRapportUnite = taRapportUnite;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy = "taArticle")
	@JoinColumn(name = "id_image_article")
	@LgrHibernateValidated(champBd = "id_image_article",table = "ta_article",champEntite="taImageArticle.idImageArticle", clazz = TaImageArticle.class)
	public TaImageArticle getTaImageArticle() {
		return taImageArticle;
	}

	public void setTaImageArticle(TaImageArticle taImageArticle) {
		this.taImageArticle = taImageArticle;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taArticle", orphanRemoval=true)
	public Set<TaNoteArticle> getTaNotes() {
		return this.taNotes;
	}

	public void setTaNotes(Set<TaNoteArticle> taNotes) {
		this.taNotes = taNotes;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taArticle", orphanRemoval=true)
	@OrderBy("position")
	public Set<TaConformite> getTaConformites() {
		return this.taConformites;
	}

	public void setTaConformites(Set<TaConformite> taConformites) {
		this.taConformites = taConformites;
	}
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taArticle")
//	public Set<TaRChampSuppArt> getTaRChampSuppArtes(){
//		return this.taRChampSuppArtes;
//	}
//
//	public void setTaRChampSuppArtes(Set<TaRChampSuppArt> taRChampSuppArtes) {
//		this.taRChampSuppArtes = taRChampSuppArtes;
//	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taArticle", orphanRemoval=true)
	public Set<TaImageArticle> getTaImageArticles(){
		return this.taImageArticles;
	}

	public void setTaImageArticles(Set<TaImageArticle> taImageArticles) {
		this.taImageArticles = taImageArticles;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_categorie_article",
			joinColumns = {@JoinColumn(name = "id_article")},inverseJoinColumns = {@JoinColumn(name = "id_categorie_article")})
	public Set<TaCategorieArticle> getTaCategorieArticles(){
		return this.taCategorieArticles;
	}

	public void setTaCategorieArticles(Set<TaCategorieArticle> taCategorieArticles) {
		this.taCategorieArticles = taCategorieArticles;
	}
	
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "ta_r_t_article",
//			joinColumns = {@JoinColumn(name = "id_article")},inverseJoinColumns = {@JoinColumn(name = "id_t_article")})
//	public Set<TaTArticle> getTaTArticles(){
//		return this.taTArticles;
//	}
//
//	public void setTaTArticles(Set<TaTArticle> taTArticles) {
//		this.taTArticles = taTArticles;
//	}
	
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_fournisseur_article",
			joinColumns = {@JoinColumn(name = "id_article")},inverseJoinColumns = {@JoinColumn(name = "id_tiers")})
	public Set<TaTiers> getTaFournisseurs(){
		return this.taFournisseurs;
	}

	public void setTaFournisseurs(Set<TaTiers> taFournisseurs) {
		this.taFournisseurs = taFournisseurs;
	}
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_label_article",
			joinColumns = {@JoinColumn(name = "id_article")},inverseJoinColumns = {@JoinColumn(name = "id_label_article")})
	public Set<TaLabelArticle> getTaLabelArticles(){
		return this.taLabelArticles;
	}

	public void setTaLabelArticles(Set<TaLabelArticle> taLabelArticles) {
		this.taLabelArticles = taLabelArticles;
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_catalogue_web")
	@LgrHibernateValidated(champBd = "id_catalogue_web",table = "ta_article",champEntite="taCatalogueWeb", clazz = TaArticle.class)
	public TaCatalogueWeb getTaCatalogueWeb() {
		return this.taCatalogueWeb;
	}

	public void setTaCatalogueWeb(TaCatalogueWeb taCatalogueWeb) {
		this.taCatalogueWeb = taCatalogueWeb;
	}
	
	@Column(name = "longueur", precision = 15)
	@LgrHibernateValidated(champBd = "longueur",table = "ta_article",champEntite="longueur", clazz = TaArticle.class)
	public BigDecimal getLongueur() {
		return longueur;
	}

	public void setLongueur(BigDecimal longueur) {
		this.longueur = longueur;
	}

	@Column(name = "largeur", precision = 15)
	@LgrHibernateValidated(champBd = "largeur",table = "ta_article",champEntite="largeur", clazz = TaArticle.class)
	public BigDecimal getLargeur() {
		return largeur;
	}

	public void setLargeur(BigDecimal largeur) {
		this.largeur = largeur;
	}

	@Column(name = "hauteur", precision = 15)
	@LgrHibernateValidated(champBd = "hauteur",table = "ta_article",champEntite="hauteur", clazz = TaArticle.class)
	public BigDecimal getHauteur() {
		return hauteur;
	}

	public void setHauteur(BigDecimal hauteur) {
		this.hauteur = hauteur;
	}

	@Column(name = "poids", precision = 15)
	@LgrHibernateValidated(champBd = "poids",table = "ta_article",champEntite="poids", clazz = TaArticle.class)
	public BigDecimal getPoids() {
		return poids;
	}

	public void setPoids(BigDecimal poids) {
		this.poids = poids;
	}

	@Column(name = "param_dluo", precision = 15)
	@LgrHibernateValidated(champBd = "param_dluo",table = "ta_article",champEntite="paramDluo", clazz = TaArticle.class)
	public String getParamDluo() {
		return paramDluo;
	}

	public void setParamDluo(String paramDluo) {
		this.paramDluo = paramDluo;
	}
	
//	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "id_titre_transport")
//	@LgrHibernateValidated(champ = "id_titre_transport",table = "ta_titre_transport",champEntite="taTitreTransport", clazz = TaTitreTransport.class)
//	public TaTitreTransport getTaTitreTransport(){
//		return this.taTitreTransport;
//	}
//
//	public void setTaTitreTransport(TaTitreTransport titreTransport) {
//		this.taTitreTransport = titreTransport;
//	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL} , orphanRemoval=true)
//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "taArticle")
	@JoinColumn(name = "id_r_titre_transport")
	@LgrHibernateValidated(champBd = "id_r_titre_transport",table = "ta_article",champEntite="taRTaTitreTransport.idRTitreTransport", clazz = TaRTaTitreTransport.class)
	public TaRTaTitreTransport getTaRTaTitreTransport() {
		return taRTaTitreTransport;
	}

	public void setTaRTaTitreTransport(TaRTaTitreTransport taTitreTransport) {
		this.taRTaTitreTransport = taTitreTransport;
	}

	
	@Column(name = "matiere_premiere")
	public Boolean getMatierePremiere() {
		return matierePremiere;
	}

	public void setMatierePremiere(Boolean matierePremiere) {
		this.matierePremiere = matierePremiere;
	}

	@Column(name = "produit_fini")
	public Boolean getProduitFini() {
		return produitFini;
	}

	public void setProduitFini(Boolean produitFini) {
		this.produitFini = produitFini;
	}
	
	@Column(name = "gestion_lot")
	public Boolean getGestionLot() {
		return gestionLot;
	}

	public void setGestionLot(Boolean gestionLot) {
		this.gestionLot = gestionLot;
	}
	
	@Column(name = "code_barre")
	@LgrHibernateValidated(champBd = "code_barre",table = "ta_article",champEntite="codeBarre", clazz = TaArticle.class)
	public String getCodeBarre() {
		return codeBarre;
	}

	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}
	
	@Column(name = "utilise_dlc")
	@LgrHibernateValidated(champBd = "utilise_dlc",table = "ta_article",champEntite="utiliseDlc", clazz = TaArticle.class)
	public Boolean getUtiliseDlc() {
		return utiliseDlc;
	}

	public void setUtiliseDlc(Boolean utiliseDlc) {
		this.utiliseDlc = utiliseDlc;
	}

	//CascadeType.PERSIST, CascadeType.MERGE,
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
	@JoinColumn(name = "id_unite_stock")
	@LgrHibernateValidated(champBd = "id_unite_stock",table = "ta_article", champEntite="taUnite.idUnite", clazz = TaUnite.class)
	public TaUnite getTaUniteStock() {
		return taUniteStock;
	}

	public void setTaUniteStock(TaUnite taUniteStock) {
		this.taUniteStock = taUniteStock;
	}
	

//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taArticle")
//	public Set<TaRTArticle> getTaRTArticles() {
//		return this.taRTArticles;
//	}
//
//	public void setTaRTArticles(Set<TaRTArticle> taRTArticles) {
//		this.taRTArticles = taRTArticles;
//	}	
	


	
	public void addPrix(TaPrix taPrix){
		this.getTaPrixes().add(taPrix);
	}
	
	public void removePrix(TaPrix taPrix){
		boolean estRefPrix=false;
		if(this.taPrix!=null) estRefPrix=this.taPrix.getIdPrix()==taPrix.getIdPrix();

		this.getTaPrixes().remove(taPrix);
		if(estRefPrix && !this.getTaPrixes().isEmpty()){
			TaPrix prix =this.getTaPrixes().iterator().next();
			if(prix!=null)
				this.setTaPrix(prix);
		}
		else if(this.getTaPrixes().isEmpty()){
			this.setTaPrix(null);
		}
		
	}
	
	public void addContiditionnementArticle(TaUnite taConditionnementArticle){
		this.getTaConditionnementsArticle().add(taConditionnementArticle);
	}
	
	public void removeContiditionnementArticle(TaUnite taConditionnementArticle){
		boolean estConditionnementDefaut=false;
		if(this.taConditionnementArticle!=null) estConditionnementDefaut=this.taConditionnementArticle.getIdUnite()==taConditionnementArticle.getIdUnite();

		this.getTaConditionnementsArticle().remove(taConditionnementArticle);
//		if(estConditionnementDefaut && !this.getTaConditionnementsArticle().isEmpty()){
//			TaUnite conditionnementArticle = this.getTaConditionnementsArticle().iterator().next();
//			if(conditionnementArticle!=null)
//				this.setTaConditionnementArticle(conditionnementArticle);
//		}
//		else if(this.getTaConditionnementsArticle().isEmpty()){
//			this.setTaConditionnementArticle(null);
//		}
		
	}
	
	public void addRapportUnite(TaRapportUnite taRapportUnite){
		this.getTaRapportUnites().add(taRapportUnite);
	}
	
	
	public void removeRapportUnite(TaRapportUnite taRapportUnite){
		boolean estRefRapportUnite=false;
		if(this.taRapportUnite!=null)
			estRefRapportUnite=this.taRapportUnite.getId()==taRapportUnite.getId();

		this.getTaRapportUnites().remove(taRapportUnite);		
		if(estRefRapportUnite && !this.getTaRapportUnites().isEmpty()){
			Iterator<TaRapportUnite> iterator = this.getTaRapportUnites().iterator();
			boolean trouve = false;
			if(getTaUnite1()!=null){
			while (iterator.hasNext()&& !trouve ){
				TaRapportUnite type = (TaRapportUnite) iterator.next();
				if(type.getTaUnite1().equals(getTaUnite1()))
					this.setTaRapportUnite(type);
			}
			}
			if (!trouve)this.setTaRapportUnite(null);
		}
		else if(this.getTaRapportUnites().isEmpty()){
			this.setTaRapportUnite(null);
		}
	}

	public boolean isRapportUniteRef(TaRapportUnite taRapportUnite){
		boolean estRefRapportUnite=false;
		if(this.taRapportUnite!=null && taRapportUnite!=null)
			estRefRapportUnite= taRapportUnite.getId()==taRapportUnite.getId();
		return estRefRapportUnite;
	}
	
	
	public TaRapportUnite recupRapportUnite(){
		return taRapportUnite;
		
	}
	
	public void addNote(TaNoteArticle taNote){
		if(!this.getTaNotes().contains(taNote))
			this.getTaNotes().add(taNote);
		//if(this.getTaNote()==null)this.setTaNote(taNote);
	}
	public void removeNote(TaNoteArticle taNote){
		//boolean estDefaut=(this.taNote!=null)&& this.taNote.getIdNote()==taNote.getIdNote();

		this.getTaNotes().remove(taNote);
//		if(estDefaut && !this.getTaNotes().isEmpty())
//			this.setTaNote(this.getTaNotes().iterator().next());
//		else if(this.getTaNotes().isEmpty()){
//			this.setTaNote(null);
//		}
		
	}
	
	public void addImageArticle(TaImageArticle taImageArticle){
		this.getTaImageArticles().add(taImageArticle);
	}
	
	public void removeImageArticle(TaImageArticle taImageArticle){
		boolean estDefaut=false;
		if(this.taImageArticle!=null) estDefaut=this.taImageArticle.getIdImageArticle()==taImageArticle.getIdImageArticle();

		this.getTaImageArticles().remove(taImageArticle);
		
		if(estDefaut && !this.getTaImageArticles().isEmpty()){
			TaImageArticle image =this.getTaImageArticles().iterator().next();
			if(image!=null)
				this.setTaImageArticle(image);
		}
		else if(this.getTaImageArticles().isEmpty()){
			this.setTaImageArticle(null);
		}
		
	}
	
	public void addCategorie(TaCategorieArticle taCategorieArticle){
		if(!this.getTaCategorieArticles().contains(taCategorieArticle))
			this.getTaCategorieArticles().add(taCategorieArticle);
	}
	public void removeCategorie(TaCategorieArticle taCategorieArticle){
		this.getTaCategorieArticles().remove(taCategorieArticle);
	}
	
//	public void addTArticle(TaTArticle taTArticle){
//		if(!this.getTaTArticles().contains(taTArticle))
//			this.getTaTArticles().add(taTArticle);
//	}
//	public void removeTArticle(TaTArticle taTArticle){
//		this.getTaTArticles().remove(taTArticle);
//	}
	
//	public void addLabelArticles(TaLabelArticle taLabelArticle){
//		if(!this.getTaLabelArticles().contains(taLabelArticle))
//			this.getTaLabelArticles().add(taLabelArticle);
//	}
//	public void removeLabelArticles(TaLabelArticle taLabelArticle){
//		this.getTaLabelArticles().remove(taLabelArticle);
//	}
//	
	public void addFournisseur(TaTiers taTiers){
		this.getTaFournisseurs().add(taTiers);
	}
	
	public void removeFournisseur(TaTiers taTiers){
		this.getTaFournisseurs().remove(taTiers);		
	}

	public void addRefArticleFournisseur(TaRefArticleFournisseur taRefArticleFournisseur){
		taRefArticleFournisseurs.add(taRefArticleFournisseur);
	}
	
	public void removeRefArticleFournisseur(TaRefArticleFournisseur taRefArticleFournisseur){
		taRefArticleFournisseurs.remove(taRefArticleFournisseur);		
	}
	
	/**
	 * Retourne le prix pour le tiers passé en paramètres, ou le prix par defaut pour cet article.
	 * Le prix affecté au tiers est prioritaire par rapport au prix affecté au "type de tarif" du tiers.
	 * @param tiers
	 * @return
	 */
	public TaPrix chercheTarif(TaTiers tiers) {
		if(tiers!=null) {
			boolean trouve = false;
			Iterator<TaPrix> ite = taPrixes.iterator();
			TaRPrixTiers RPrixTiers = null;
			Iterator<TaRPrixTiers> iteTiers = null;
			Iterator<TaRPrix> iteRPrix = null;
			TaPrix p = null;
			TaTiers t =null;
			TaTTarif ttarif =null;
			TaRPrix taRPrix=null;
			//recherche d'un prix pour le tiers
			while(!trouve && ite.hasNext()) {
				p = ite.next();
				iteTiers = p.getTaRPrixesTiers().iterator();
				while(!trouve && iteTiers.hasNext()) {
					RPrixTiers = iteTiers.next();
					t=RPrixTiers.getTaTiers();
					if(t.getCodeTiers().equals(tiers.getCodeTiers()) && 
							(t.getTaTTarif()!=null && t.getTaTTarif().equals(RPrixTiers.getTaTTarif()))) {
						trouve = true;
					}
				}
			}
			if(trouve) {
				return p;
			} else {
				//aucun pour ce tiers => recherche d'un prix pour le "type tarif" du tiers
				if(tiers.getTaTTarif()!=null) {
					ite = taPrixes.iterator();
					while(!trouve && ite.hasNext()) {
						p = ite.next();
						iteRPrix = p.getTaRPrixes().iterator();
						while(!trouve && iteRPrix.hasNext()) {
							taRPrix = iteRPrix.next();
							if(taRPrix.getTaTTarif()!=null) {
								ttarif=taRPrix.getTaTTarif();
								if(ttarif.getCodeTTarif().equals(tiers.getTaTTarif().getCodeTTarif())) {
									trouve = true;
								}
							}
						}
					}
				}
			}
			if(trouve) {
				return p;
			}
		} 
		//aucun prix spécifique pour ce tiers, on prend le prix par defaut de l'article
		return taPrix;
	}
	
	public boolean possedeDejaUnPrixPourCeTiers(String codeTiers) {
		boolean trouve = false;
		Iterator<TaPrix> ite = taPrixes.iterator();
		Iterator<TaRPrixTiers> iteTiers = null;
		TaRPrixTiers rp = null;
		TaPrix p = null;
		TaTiers t =null;
		while(!trouve && ite.hasNext()) {
			p = ite.next();
			iteTiers = p.getTaRPrixesTiers().iterator();
			while(!trouve && iteTiers.hasNext()) {
				rp = iteTiers.next();
				t=rp.getTaTiers();
				if(t.getCodeTiers().equals(codeTiers)) {
					trouve = true;
				}
			}
		}
		return trouve;
	}
	
	public boolean possedeDejaUnPrixPourCeTypeDeTarif(String codeTTarif) {
		boolean trouve = false;
		Iterator<TaPrix> ite = taPrixes.iterator();
		Iterator<TaRPrix> iteRPrix = null;
		TaPrix p = null;
		TaTTarif t =null;
		TaRPrix taRPrix=null;
		while(!trouve && ite.hasNext()) {
			p = ite.next();
			iteRPrix = p.getTaRPrixes().iterator();
			while(!trouve && iteRPrix.hasNext()) {
				taRPrix = iteRPrix.next();
				if(taRPrix.getTaTTarif()!=null) {
					t=taRPrix.getTaTTarif();
					if(t.getCodeTTarif().equals(codeTTarif)) {
						trouve = true;
					}
				}
			}
		}
		return trouve;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		TaArticle article =new TaArticle();
		try {
//			article.setCodeArticle(codeArticle);
			article.setCommentaireArticle(commentaireArticle);
			article.setDiversArticle(diversArticle);
			article.setActif(actif);
//			article.setIdArticle(0);
//			article.setIdImport(null);
			article.setLibellecArticle(libellecArticle);
			article.setLibellelArticle(libellelArticle);
			article.setNumcptArticle(numcptArticle);
//			article.setOrigineImport(origineImport);
			article.setStockMinArticle(stockMinArticle);
			article.setGestionLot(gestionLot);
			article.setAutoAlimenteFournisseurs(autoAlimenteFournisseurs);
			article.setCodeBarre(codeBarre);
			article.setHauteur(hauteur);
			article.setLongueur(longueur);
			article.setParamDluo(paramDluo);
			article.setPoids(poids);
			article.setMatierePremiere(matierePremiere);
			article.setProduitFini(produitFini);

//			article.setTaFamille(taFamille);
//			article.setTaFamilles(taFamilles);
			for (TaFamille o : taFamilles) {
				article.getTaFamilles().add(o);
				if(o.equals(taFamille))article.setTaFamille(o);
			}
			
			article.setTaMarqueArticle(taMarqueArticle);			
			article.setUtiliseDlc(utiliseDlc);			
			article.setTaUnite1(taUnite1);
			article.setTaUniteStock(taUniteStock);
			article.setTaUniteReference(taUniteReference);
			article.setTaUniteSaisie(taUniteSaisie);
			article.setTaTTva(taTTva);
			article.setTaTva(taTva);
			
//			article.setTaConditionnementsArticle(taConditionnementsArticle);
			for (TaUnite o : taConditionnementsArticle) {
				article.addContiditionnementArticle(o);
			}
			
//			article.setTaFournisseurs(taFournisseurs);
			for (TaTiers o : taFournisseurs) {
				article.addFournisseur(o);
			}
			
			//
			if(taRecetteArticle!=null) {
			TaRecette r=(TaRecette) taRecetteArticle.cloneDuplication();
			r.setTaArticle(article);
			for (TaLRecette lr : r.getLignes()) {
				lr.setTaArticle(article);
			}
			article.setTaRecetteArticle(r);
			}
			
			if(taRTaTitreTransport!=null) {
				TaRTaTitreTransport r=(TaRTaTitreTransport) taRTaTitreTransport.clone();
			r.setTaArticle(article);
			article.setTaRTaTitreTransport(r);
			}
			
			//

//			article.setTaCatalogueWeb(taCatalogueWeb);			
//			article.setTaCategorieArticles(taCategorieArticles);
//			article.setTaImageArticles(taImageArticles);
//			article.setTaLabelArticles(taLabelArticles);
			
			
			for (TaNoteArticle note : taNotes) {
				TaNoteArticle noteArticle = (TaNoteArticle)note.clone();
				noteArticle.setTaArticle(article);
				article.addNote(note);
			}
			
			for (TaPrix obj : taPrixes) {
				boolean ref=obj==taPrix;
				TaPrix prixNew = (TaPrix)obj.clone();
				prixNew.setTaArticle(article);
				article.addPrix(prixNew);
				if(ref){
					article.setTaPrix(prixNew);
				}

			}

			
//			for (TaConformite l : taConformites) {
//				TaConformite c = (TaConformite) l.clone();
//				c.setTaArticle(article);
//				article.getTaConformites().add(c);
//			}
			
		} catch (Exception e) {
			logger.error("", e);
		}
		// on renvoie le clone
		return article;
	}

	//@PostPersist
	//@PostUpdate
	public void afterPost()throws Exception{
		//recalcul le prix ttc en fonction du taux de tva
		BigDecimal taux=BigDecimal.valueOf(1);
		if(this.getTaTva()!=null && this.getTaTva().getTauxTva()!=null)
			taux= this.getTaTva().getTauxTva().divide(BigDecimal.valueOf(100));
		for (TaPrix prix : this.getTaPrixes()) {
			prix.setPrixttcPrix(prix.getPrixPrix().add(prix.getPrixPrix().multiply(taux),MathContext.DECIMAL128).setScale(3,BigDecimal.ROUND_HALF_UP));
		}
	}


	public void initCodeTTva(){
		if (this.getTaTva()!=null && !LibChaine.empty(this.getTaTva().getCodeTva())){
			if( this.getTaTTva()==null) {
				this.setTaTTva(new TaTTva());  
			}
			if(LibChaine.empty(this.getTaTTva().getCodeTTva()))
				this.getTaTTva().setCodeTTva("D");
		}else
			if(this.getTaTva()==null || LibChaine.empty(this.getTaTva().getCodeTva()))
				this.setTaTTva(null);	
	}

	/**
	 * Creation d'un objet "TaRapportUnite" pour l'objet "TaArticle" gerer par cet ecran
	 * dans le cas ou la propriete TaRapportUnite de ce dernier est nulle.
	 */
	public void initRapportUniteArticle() {
		if(this.getTaRapportUnite()==null ) {
			//initialisation du prix
			TaRapportUnite r = new TaRapportUnite();
			r.setTaArticle(this);
			r.setRapport(new BigDecimal(0));
			r.setNbDecimal(0);
			r.setSens(0);
			this.setTaRapportUnite(r);
			this.getTaRapportUnites().add(r);
		}
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy = "taArticle")
	@JoinColumn(name = "id_recette")
	@LgrHibernateValidated(champBd = "id_recette",table = "ta_article",champEntite="taRecetteArticle.idRecette", clazz = TaRecette.class)
	public TaRecette getTaRecetteArticle() {
		return taRecetteArticle;
	}

	public void setTaRecetteArticle(TaRecette taRecetteArticle) {
		this.taRecetteArticle = taRecetteArticle;
	}

	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "id_marque")
	@LgrHibernateValidated(champBd = "id_marque",table = "ta_article", champEntite="taMarqueArticle.idMarqueArticle", clazz = TaMarqueArticle.class)
	public TaMarqueArticle getTaMarqueArticle() {
		return taMarqueArticle;
	}

	public void setTaMarqueArticle(TaMarqueArticle taMarqueArticle) {
		this.taMarqueArticle = taMarqueArticle;
	}
	


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ta_r_famille_article",
			joinColumns = {@JoinColumn(name = "id_article")},inverseJoinColumns = {@JoinColumn(name = "id_famille")})
	public Set<TaFamille> getTaFamilles() {
		return taFamilles;
	}

	public void setTaFamilles(Set<TaFamille> taFamilles) {
		this.taFamilles = taFamilles;
	}

	
	//CascadeType.PERSIST, CascadeType.MERGE,
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
	@JoinColumn(name = "id_unite_reference")
	@LgrHibernateValidated(champBd = "id_unite_reference",table = "ta_article",champEntite="taUniteReference", clazz = TaUnite.class)
	public TaUnite getTaUniteReference() {
		return taUniteReference;
	}

	public void setTaUniteReference(TaUnite taUniteReference) {
		this.taUniteReference = taUniteReference;
	}

	@Column(name = "auto_alimente_fournisseurs")
	@LgrHibernateValidated(champBd = "auto_alimente_fournisseurs",table = "ta_article",champEntite="autoAlimenteFournisseurs", clazz = TaArticle.class)
	public Boolean getAutoAlimenteFournisseurs() {
		return autoAlimenteFournisseurs;
	}

	public void setAutoAlimenteFournisseurs(Boolean autoAlimenteFournisseurs) {
		this.autoAlimenteFournisseurs = autoAlimenteFournisseurs;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
	@JoinColumn(name = "id_unite_1")
	@LgrHibernateValidated(champBd = "id_unite_1",table = "ta_article",champEntite="taUnite1", clazz = TaUnite.class)
	public TaUnite getTaUnite1() {
		return taUnite1;
	}

	public void setTaUnite1(TaUnite taUnite1) {
		this.taUnite1 = taUnite1;
	}
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "taArticle", orphanRemoval=true)
	public Set<TaRefArticleFournisseur> getTaRefArticleFournisseurs(){
		return this.taRefArticleFournisseurs;
	}

	public void setTaRefArticleFournisseurs(Set<TaRefArticleFournisseur> taRefArticleFournisseurs) {
		this.taRefArticleFournisseurs = taRefArticleFournisseurs;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taArticle")
	public Set<TaArticleCompose> getNomenclature() {
		return nomenclature;
	}

	public void setNomenclature(Set<TaArticleCompose> nomenclature) {
		this.nomenclature = nomenclature;
	}
	@Column(name = "compose")
	public Boolean getCompose() {
		return compose;
	}

	public void setCompose(Boolean compose) {
		this.compose = compose;
	}
	@Column(name = "abonnement")
	public Boolean getAbonnement() {
		return abonnement;
	}

	public void setAbonnement(Boolean abonnement) {
		this.abonnement = abonnement;
	}
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
	@JoinColumn(name = "id_comportement_article_compose")
	public TaComportementArticleCompose getComportementArticleCompose() {
		return taComportementArticleCompose;
	}

	public void setComportementArticleCompose(TaComportementArticleCompose taComportementArticleCompose) {
		this.taComportementArticleCompose = taComportementArticleCompose;
	}
	@Column(name = "delai_survie")
	public Integer getDelaiSurvie() {
		return delaiSurvie;
	}

	public void setDelaiSurvie(Integer delaiSurvie) {
		this.delaiSurvie = delaiSurvie;
	}
	@Column(name = "delai_grace")
	public Integer getDelaiGrace() {
		return delaiGrace;
	}

	public void setDelaiGrace(Integer delaiGrace) {
		this.delaiGrace = delaiGrace;
	}
	@Column(name = "libl_coef_multiplicateur")
	public String getLiblCoefMultiplicateur() {
		return liblCoefMultiplicateur;
	}
	
	public void setLiblCoefMultiplicateur(String liblCoefMultiplicateur) {
		this.liblCoefMultiplicateur = liblCoefMultiplicateur;
	}
	@Column(name = "coef_multiplicateur")
	public Boolean getCoefMultiplicateur() {
		return coefMultiplicateur;
	}

	public void setCoefMultiplicateur(Boolean coefMultiplicateur) {
		this.coefMultiplicateur = coefMultiplicateur;
	}

	
	
	@Column(name = "gestion_stock")
	public Boolean getGestionStock() {
		return gestionStock;
	}

	public void setGestionStock(Boolean gestionStock) {
		this.gestionStock = gestionStock;
	}
	
	
	
	@Column(name = "importation_divers")
	public String getImportationDivers() {
		return importationDivers;
	}

	public void setImportationDivers(String importationDivers) {
		this.importationDivers = importationDivers;
	}
	

	public boolean equalsCodeArticle(TaArticle obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaArticle other = (TaArticle) obj;
		if (codeArticle == null) {
			if (other.codeArticle != null)
				return false;
		} else if (!codeArticle.equals(other.codeArticle))
			return false;
		return true;
	}
  
  
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeArticle == null) ? 0 : codeArticle.hashCode());
		return result;
	}
	
	public boolean equalsCodeArticleAndUnite1(TaArticle obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaArticle other = (TaArticle) obj;
		if (codeArticle == null) {
			if (other.codeArticle != null)
				return false;
		} else if (!codeArticle.equals(other.codeArticle))
			return false;
		if (taUnite1 == null) {
			if (other.taUnite1 != null)
				return false;
		} else if (!taUnite1.getCodeUnite().equals(other.taUnite1.getCodeUnite()))
			return false;
		return true;
	}
	@Column(name = "code_module_bdg")
	public String getCodeModuleBdg() {
		return codeModuleBdg;
	}

	public void setCodeModuleBdg(String codeModuleBdg) {
		this.codeModuleBdg = codeModuleBdg;
	}
	@Column(name = "libl_module_bdg")
	public String getLiblModuleBdg() {
		return liblModuleBdg;
	}

	@Column(name = "prix_variable")
	public Boolean getPrixVariable() {
		return prixVariable;
	}

	public void setPrixVariable(Boolean prixVariable) {
		this.prixVariable = prixVariable;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH})
	@JoinColumn(name = "id_unite_saisie")
	@LgrHibernateValidated(champBd = "id_unite_saisie",table = "ta_article",champEntite="taUniteSaisie", clazz = TaUnite.class)
	public TaUnite getTaUniteSaisie() {
		return taUniteSaisie;
	}

	public void setTaUniteSaisie(TaUnite taUniteSaisie) {
		this.taUniteSaisie = taUniteSaisie;
	}
	
	public void setLiblModuleBdg(String liblModuleBdg) {
		this.liblModuleBdg = liblModuleBdg;
	}
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "taArticle")
//	public Set<TaJourRelance> getTaJourRelance() {
//		return taJourRelance;
//	}
//
//	public void setTaJourRelance(Set<TaJourRelance> taJourRelance) {
//		this.taJourRelance = taJourRelance;
//	}
	
	
	
	
}
