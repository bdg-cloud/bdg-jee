package fr.legrain.documents.dao.jpa;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DateType;
import org.hibernate.type.StringType;

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.article.model.TaTva;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.document.dashboard.dto.CountDocumentParTypeRegrouptement;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.IEtatDAO;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;

/**
 * Home object for domain model class TaTicketDeCaisse.
 * @see fr.legrain.documents.dao.TaTicketDeCaisse
 * @author Hibernate Tools
 */



public abstract class AbstractDocumentDAO<Entity,InfosEntity,LigneEntity>   {


	static Logger logger = Logger.getLogger(AbstractDocumentDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	protected EntityManager entityManager;
	
	protected String nameEntity;
	protected String nameEntitySQL;
	protected String nameInfosEntitySQL;
	protected String nameLignesEntitySQL;
	protected Entity entity;
	protected InfosEntity infosEntity;
	protected LigneEntity ligneEntity;
	protected @Inject ITvaDAO daoTva;
	protected @EJB ITaEtatServiceRemote serviceEtat;
	
	
	protected String prefixeDocument="doc";
	protected String prefixeLDocument="ldoc";
	
	protected String defaultJPQLQuery ;
	protected String documentLieAFacture;
	
	protected DocumentChiffreAffaireDTO documentChiffreAffaireDTO = new DocumentChiffreAffaireDTO();
	protected DocumentDTO documentDTO = new DocumentDTO();
	protected TaArticlesParTiersDTO taArticlesParTiersDTO = new TaArticlesParTiersDTO();
	
	
			public String FIND_BY_DATE ; 
			public  String FIND_BY_DATE_LIGHT;
			public  String FIND_BY_TIERS_AND_CODE_LIGHT;
			public  String FIND_BY_TIERS_AND_CODE;
			public  String FIND_BY_CODE;
			public  String FIND_BY_DATE_PARDATE;
			public  String FIND_BY_TIERS_AND_DATE ;
			public  String FIND_ALL_LIGHT ;
			public  String FIND_BY_CODE_LIGHT;

			
			public  String FIND_ALL_LIGHT_PERIODE;
			public  String FIND_ALL_LIGHT_INTERVALLE;
			public  String FIND_TRANSFORME_LIGHT_PERIODE;
			public  String FIND_NON_TRANSFORME_LIGHT_PERIODE;
			public  String FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE;
			
			
			public  String SUM_CA_JOUR_LIGTH_PERIODE;
			public  String SUM_CA_MOIS_LIGTH_PERIODE;			
			public  String SUM_CA_ANNEE_LIGTH_PERIODE;
			public  String SUM_CA_TOTAL_LIGTH_PERIODE;
					
			public  String SUM_CA_JOUR_LIGTH_PERIODE_SUR_ETAT;
			public  String SUM_CA_MOIS_LIGTH_PERIODE_SUR_ETAT;			
			public  String SUM_CA_ANNEE_LIGTH_PERIODE_SUR_ETAT;
			public  String SUM_CA_TOTAL_LIGTH_PERIODE_SUR_ETAT;

			public  String SUM_CA_JOUR_LIGTH_PERIODE_AVEC_ETAT;
			public  String SUM_CA_MOIS_LIGTH_PERIODE_AVEC_ETAT;			
			public  String SUM_CA_ANNEE_LIGTH_PERIODE_AVEC_ETAT;
			public  String SUM_CA_TOTAL_LIGTH_PERIODE_AVEC_ETAT;
			
			
			public  String SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME;
			public  String SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME;
			public  String SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME;
			public  String SUM_CA_TOTAL_LIGTH_PERIODE_NON_TRANSFORME;
			
			
			public  String SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER;
			public  String SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER;
			public  String SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER;
			public  String SUM_CA_TOTAL_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE;
			 
			
			public  String SUM_CA_JOUR_LIGTH_PERIODE_TRANSFORME;
			public  String SUM_CA_MOIS_LIGTH_PERIODE_TRANSFORME;
			public  String SUM_CA_ANNEE_LIGTH_PERIODE_TRANSFORME;
			public  String SUM_CA_TOTAL_LIGTH_PERIODE_TRANSFORME;

			
			public  String FIND_ARTICLES_PAR_TIERS_PAR_MOIS;			
			public  String FIND_ARTICLES_PAR_TIERS_TRANSFORME;			
			public  String FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME;			
			public  String FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER;
			public String FIND_ARTICLES_PAR_TIERS_PAR_MOIS_AVEC_ETAT ;


			
			public  String FIND_BY_ETAT_TIERS_ENCOURS_DATE;
			
			public String SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS ;
			public String SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE ;
			
			/**RAJOUT YANN**/
			public String LIST_SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE;
			public String LIST_SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_MOIS;
			
			
			
			public String COUNT_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE;
			public String COUNT_FAMILLE_ARTICLE_PERIODE;
			public String SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE;
			public String SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_MOINS_AVOIR;
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE;
			
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_PAR_UNITE;
			//ci-dessous  : where codeFamille like
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_WHERE_FAMILLE_LIKE;
			// requete ci-dessous pour dashboard article onglet "tous"
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_ARTICLE;
			// requete ci-dessous pour dashboard article onglet "tous" par famille article
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_FAMILLE_ARTICLE;
			
			//requete pour Dashboard Bonliv pour chargement article par transporteur
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_BONLIV_TRANSPORTEUR_GROUP_BY_ARTICLE;
			
			//requete pour Dashboard Bonliv pour chargement article détail par transporteur
			public String LIST_LIGNE_ARTICLE_PERIODE_BONLIV_TRANSPORTEUR;
			
			//requete pour Dashboard Bonliv pour chargement article par transporteur
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_BONLIV_SANS_TRANSPORTEUR_GROUP_BY_ARTICLE;
			
			//requete pour Dashboard Bonliv pour chargement article détail par transporteur
			public String LIST_LIGNE_ARTICLE_PERIODE_BONLIV_SANS_TRANSPORTEUR;
			
			/**RAJOUT YANN 2eme partie**/
			//les 4 requetes renvoient une liste de ligne d'article sur des docs (tous, transformé, non transformés, et à relancer) trié par tiers
			//notammment utilisé dans le dashboard facture onglet "detail des factures"
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS;
			public String LIST_LIGNE_ARTICLE_TRANSFO_PERIODE_PAR_TIERS;
			public String LIST_LIGNE_ARTICLE_NON_TRANSFO_PERIODE_PAR_TIERS;
			public String LIST_LIGNE_ARTICLE_A_RELANCER_PERIODE_PAR_TIERS;
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVEC_ETAT;
			
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_UNITE;

			public String SUM_CA_MOIS_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_MOINS_AVOIR;
			public String COUNT_TIER_AYANT_ACHETER_ARTICLE;
			public String LIST_TIER_AYANT_ACHETER_ARTICLE;
			
			public String LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF;
			//requete pour le Ca des devis non transformé du dash par article
			public String SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_NON_TRANSFORME;
			//requete pour le Ca des devis transformé du dash par article
			public String SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_TRANSFORME;
			//requete pour le Ca des devis à relancer du dash par article
			public String SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_A_RELANCER;
			
			/**
			 *Les 3 requetes ci-dessous : sommes des lignes du doc avec les extract des mois et année et groupé par extract MOIS
			 * Utilisé dans les graphiques du dash par article onglet devis/proforma/Commandes
			 * Elles renvoi une liste de sommes des montants des lignes d'articles, pour un article (et idéalement un tiers par la suite a rajouter)
			 * Et par période
			 * Avec un deltaJour pour les "a relancer"
			 */
			//requete pour le graphique par mois des devis non transformé du dash par article
			public String LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_NON_TRANSFORME;
			//requete pour le graphique par mois des devis transformé du dash par article
			public String LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_TRANSFORME;
			//requete pour le graphique par mois des devis à relancer du dash par article
			public String LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_A_RELANCER;
			
			
			
			
			
			/**les 4 requêtes ci dessous pour liste de doc du dash par article onglet devis/proforma/Commandes
			 * Ces requetes differe de celles utilisés par le dash par tiers car elles "join" les lignes du doc et font un "distinct"
			 * Le distinct sert à ne pas remonter deux fois un meme doc si deux lignes avec meme article
			 * ces requetes peuvent donc prendre l'article(codeArticle) en param en plus du tiers
			 * Voir à les fusionners par exemple en rajoutant à FIND_ALL_LIGHT_PERIODE un distinct et join les lignes de doc
			 * **/
			public  String FIND_ALL_LIGHT_LIGNE_PERIODE;
			public  String FIND_TRANSFORME_LIGHT_LIGNE_PERIODE;
			public  String FIND_NON_TRANSFORME_LIGHT_LIGNE_PERIODE;
			public  String FIND_NON_TRANSFORME_ARELANCER_LIGHT_LIGNE_PERIODE;
			
			public  String LIST_LIGNE_ARTICLE_PERIODE_AVOIR_FACTURE_FOURNISSEUR;
			public  String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_AVOIR_FACTURE_FOURNISSEUR_GROUP_BY_ARTICLE;
			
			// requete ci-dessous pour dashboard tiers onglet "tous"
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE_GROUP_BY_TIERS;
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_TIERS_ARTICLE;
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE;
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_ARTICLE_AVOIR_FACTURE;
			public String LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE_PAR_UNITE;
			
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_BCDE_GROUP_BY_ARTICLE;
			public String LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_BCDE_GROUP_BY_FAMILLE_ARTICLE;
			public String LIST_LIGNE_ARTICLE_PERIODE_BONCDE_DETAIL;
			
//			public String FIND_ALL_LIGHT_PERIODE_SUR_ETAT;
			public String FIND_ALL_LIGHT_PERIODE_AVEC_ETAT;

			
			
			abstract public String getRequeteDocumentTransforme(String prefixe) ;
			abstract public String getDateAVerifierSiARelancer() ;
			abstract public String getRequeteARelancer();
			abstract public String getRequeteARelancerJPQL();
			
			public String getRequeteligneArticle(String prefixe) {				
				return "select f from "+nameEntity+" f join f.lignes lf join lf.taArticle art where f="+prefixe+" and art.codeArticle like :valeur ";
			};
			public String getRequeteligneFamilleArticle(String prefixe) {		
				return "select f from "+nameEntity+" f join f.lignes lf join lf.taArticle art join art.taFamille fam where f="+prefixe+" and fam.codeFamille like :valeur ";
			};
						
			public String getRequeteligneTauxTva(String prefixe) {		
				return "select f from "+nameEntity+" f join f.lignes lf where f="+prefixe+" and lf.tauxTvaLDocument like :valeur ";
			};
						
			private Object newGenerics(int postion) {
				try {
					return ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[postion]).newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
			
			private Entity newDocument() {
				return (Entity) newGenerics(0);
			}
	
			private InfosEntity newInfosDocument() {
				return (InfosEntity) newGenerics(1);
			}
			
			private LigneEntity newLigneDocument() {
				return (LigneEntity) newGenerics(2);
			}
			/**RAJOUT YANN**/
			/**Cette methode n'est pas appellé par les documents PAYABLE**/
			/**ATTENTION : être sûr que les champs soit présents dans la requête si ajoutés ici sinon plante **/
			public Query addScalarDocumentChiffreAffaireDTO(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_jour, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mois, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_annee, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_reglementComplet, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_resteAReglerComplet, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			/**Même méthode mais avec codeDoc et dateDoc et codeTiers en plus**/
			public Query addScalarDocumentChiffreAffaireDTOcodeDocDateDoc(Query query ) {
				query.unwrap(SQLQuery.class)
				//rajout article
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_jour, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mois, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_annee, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateDocument,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_reglementComplet, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_resteAReglerComplet, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			/**Même méthode mais pour remonter les codeArticles etc car group by sur article**/
			public Query addScalarDocumentChiffreAffaireDTOGroupByArticle(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
//				/**rajout**/
//				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			
			/**Même méthode mais pour remonter les codeTiers et les montants etc car group by sur tiers**/
			public Query addScalarDocumentChiffreAffaireDTOGroupByTiersEtArticle(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/**Même méthode mais pour remonter les codeTiers et les montants etc car group by sur tiers**/
			public Query addScalarDocumentChiffreAffaireDTOGroupByTiers(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)
//				/**rajout**/
//				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/**Même méthode mais pour remonter les détails**/
			public Query addScalarDocumentChiffreAffaireDTODetailTiers(Query query ) {
				query.unwrap(SQLQuery.class)

				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)

				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)


				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}

			/**Même méthode mais pour remonter les détails**/
			public Query addScalarDocumentChiffreAffaireDTODetail(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_codeDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateDocument,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/**Même méthode mais pour remonter les détails**/
			public Query addScalarDocumentChiffreAffaireDTODetailAvecQte2(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_codeDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateDocument,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/** Méthode mais pour remonter les codeFamille et unité etc car group by sur famille article**/
			public Query addScalarDocumentChiffreAffaireDTOGroupByFamilleArticle(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			

			
			public Query addScalarDocumentArticleFournisseurDTOGroupByArticle(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_fournisseur, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomFournisseur, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/**Même méthode mais pour remonter les détails**/
			public Query addScalarDocumentArticleFournisseurDTODetail(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_codeDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateDocument,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateLivraison,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_lot, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_fournisseur, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomFournisseur, StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/**Même méthode mais pour remonter les détails**/
			public Query addScalarDocumentArticleTransporteurDTODetail(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_codeDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateDocument,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateLivraison,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_uSaisieLDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteSaisieLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_lot, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeTransporteur, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_liblTransporteur, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_utiliseUniteSaisie, BooleanType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			public Query addScalarDocumentArticleTransporteurDTOGroupByArticle(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeTransporteur, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_liblTransporteur, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_uSaisieLDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteSaisieLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_utiliseUniteSaisie, BooleanType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			public Query addScalarDocumentArticleDTOGroupByArticle(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_uSaisieLDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteSaisieLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_utiliseUniteSaisie, BooleanType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/** Méthode mais pour remonter les codeFamille et unité etc car group by sur famille article**/
			public Query addScalarDocumentArticleDTOGroupByFamilleArticle(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_uSaisieLDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteSaisieLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_utiliseUniteSaisie, BooleanType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			/**Même méthode mais pour remonter les détails**/
			public Query addScalarDocumentArticleDTODetail(Query query ) {
				query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_codeDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateDocument,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateLivraison,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
//				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				/**tiers**/
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers, StringType.INSTANCE)
				/**rajout**/
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_uSaisieLDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteSaisieLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_lot, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_utiliseUniteSaisie, BooleanType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
				return query;
			}
			
			
			public void initialiseRequetesAvecEtat() {
				
				FIND_BY_DATE ="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and  doc.dateDocument between ? and ? order by doc.codeDocument"; 
				FIND_BY_DATE_LIGHT="select doc.idDocument, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, infos.nomTiers,0,0,doc.netHtCalc,doc.netTtcCalc from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers where tiers.codeTiers like ? and doc.dateDocument between ? and ? order by doc.codeDocument";
				FIND_BY_TIERS_AND_CODE_LIGHT="select doc.idDocument, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, infos.nomTiers,0,0,doc.netHtCalc,doc.netTtcCalc from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers where tiers.codeTiers like ? and doc.codeDocument like ? order by doc.codeDocument";
				FIND_BY_TIERS_AND_CODE="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and doc.codeDocument between ? and ? order by doc.codeDocument";
				FIND_BY_CODE="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and  doc.codeDocument between ? and ? order by doc.codeDocument";
				FIND_BY_DATE_PARDATE="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and  doc.dateDocument between ? and ? order by doc.dateDocument";
				FIND_BY_TIERS_AND_DATE="select doc from "+nameEntity+" doc join doc.taTiers tiers where tiers.codeTiers like ? and doc.dateDocument between ? and ? order by tiers.codeTiers,doc.codeDocument";

				FIND_ALL_LIGHT="select new fr.legrain.document.dto.DocumentDTO(doc.idDocument, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, infos.nomTiers, infos.prenomTiers, infos.nomEntreprise, doc.dateLivDocument, doc.netHtCalc,doc.netTvaCalc,doc.netTtcCalc) from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers order by doc.codeDocument";
//				FIND_ALL_LIGHT="select new fr.legrain.document.dto.TaBonlivDTO(doc.id, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, tiers.nomTiers) from "+nameEntity+" doc left join doc.taTiers tiers order by doc.codeDocument";
				FIND_BY_CODE_LIGHT="select new fr.legrain.document.dto.DocumentDTO(doc.id, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, tiers.nomTiers) from "+nameEntity+" doc left join doc.taTiers tiers where doc.codeDocument like :code order by doc.codeDocument";

//				FIND_ALL_LIGHT_PERIODE_SUR_ETAT=
//						"select "
//								+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
//								+documentDTO.retournChampAndAlias("doc","idDocument")+", "
//								+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
//								+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
//								+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
//								+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
//								+documentDTO.retournChampAndAlias("infos","nomTiers")+","
//								+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
//								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
//								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
//								+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
//								+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
//								+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
//						+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers join doc.taREtat re join re.taEtat et "
//						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat "
//						+ " order by doc.dateDocument DESC, doc.codeDocument DESC";	

				FIND_ALL_LIGHT_PERIODE_AVEC_ETAT=
						"select "
								+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
								+documentDTO.retournChampAndAlias("et","identifiant")+", "
								+documentDTO.retournChampAndAlias("doc","idDocument")+", "
								+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
								+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
								+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
								+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
								+documentDTO.retournChampAndAlias("infos","nomTiers")+","
								+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
								+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
						+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers join doc.taREtat re join re.taEtat et "
						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat "
						+ " order by doc.dateDocument DESC, doc.codeDocument DESC";
				
				// Renvoi les totaux de l'ensemble des Bonliv sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGTH_PERIODE_SUR_ETAT=
						"select "
						+ "count(doc)as count,"
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  join doc.taREtat re join re.taEtat et "
							+ " where doc.dateDocument between :dateDebut and :dateFin "					
							+ " and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat ";
				
				// Renvoi les totaux de l'ensemble des Bonliv sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGTH_PERIODE_AVEC_ETAT=
						"select "
						+"et.identifiant  as identifiant, "
						+ "count(doc)as count,"
						+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  join doc.taREtat re join re.taEtat et "
							+ " where doc.dateDocument between :dateDebut and :dateFin "					
							+ " and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat "
							+ " group by et.identifiant,3";
				
				// Renvoi les totaux du jour de l'ensemble des Bonliv sur la période dateDebut à dateFin
				SUM_CA_JOUR_LIGTH_PERIODE_AVEC_ETAT=
						"select "
						+"et.identifiant  as identifiant, "
						+ "count(doc)as count,"
						+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  join doc.taREtat re join re.taEtat et "
							+ " where doc.dateDocument between :dateDebut and :dateFin "					
							+ " and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat "
							+ " group by et.identifiant,3,extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by et.identifiant,3,extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux du jour de l'ensemble des Bonliv sur la période dateDebut à dateFin
				SUM_CA_MOIS_LIGTH_PERIODE_AVEC_ETAT=
						"select "
						+"et.identifiant  as identifiant, "
						+ "count(doc)as count,"
						+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  join doc.taREtat re join re.taEtat et "
							+ " where doc.dateDocument between :dateDebut and :dateFin "					
							+ " and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat "
							+ " group by et.identifiant,3,extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by et.identifiant,3,extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux du jour de l'ensemble des Bonliv sur la période dateDebut à dateFin
				SUM_CA_ANNEE_LIGTH_PERIODE_AVEC_ETAT=
						"select "
						+"et.identifiant  as identifiant, "
						+ "count(doc)as count,"
						+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  join doc.taREtat re join re.taEtat et "
							+ " where doc.dateDocument between :dateDebut and :dateFin "					
							+ " and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat "
							+ " group by et.identifiant,3,extract(year from doc.dateDocument) "
							+ " order by et.identifiant,3,extract(year from doc.dateDocument)";
				
				
				/**VOIR TABLE LIGNE FACTURE**/
				// Renvoi les lignes des factures(ou autre doc) triées par tiers et par article  sur la période dateDebut à dateFin 
				LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVEC_ETAT = 
						"select "
								+"et.identifiant  as identifiant, "
								+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
								+" coalesce(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument,0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								+ documentChiffreAffaireDTO.retournChampAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.retournChampAndAlias("doc","codeDocument")+","
							    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","qteLDocument")+","
							    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","u1LDocument")+","
							+ " tiers.codeTiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							+ " infos.nomEntreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							+ " infos.nomTiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							+ " infos.prenomTiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +", "
							+ " fam.codeFamille as "+DocumentChiffreAffaireDTO.f_codeFamille +" "
							+ " from "+nameEntity+" doc   join doc.taREtat re join re.taEtat et "
							+ " join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam join doc.taInfosDocument infos"
							+ " where doc.dateDocument between :dateDebut and :dateFin and " 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle  and et.identifiant like :etat ";
							// order by variabilisé
							//+ " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";				
				
				
				FIND_ARTICLES_PAR_TIERS_PAR_MOIS_AVEC_ETAT=
						"select "
								+"et.identifiant  as identifiant, "
								+"case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer,"
								+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
								taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
								taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
								taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
								taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
								taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
								taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
								
								taArticlesParTiersDTO.retournChampAndAlias("fam", "codeFamille")+" ,"+
								taArticlesParTiersDTO.retournChampAndAlias("fam", "libcFamille")+" ,"+

								taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","+
								taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
								+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos "
								+ " left join tiers.taEntreprise ent join ldoc.taArticle art left join art.taFamille fam  "
								+ " join doc.taREtat re  "
								+ " join re.taEtat et  " 
								+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
								+"  and et.identifiant like :etat "  
								+ " order by doc.codeDocument";
			}
		
			public void initialiseRequetes() {
				

				FIND_BY_DATE ="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and  doc.dateDocument between ? and ? order by doc.codeDocument"; 
				FIND_BY_DATE_LIGHT="select doc.idDocument, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, infos.nomTiers,0,0,doc.netHtCalc,doc.netTtcCalc from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers where tiers.codeTiers like ? and doc.dateDocument between ? and ? order by doc.codeDocument";
				FIND_BY_TIERS_AND_CODE_LIGHT="select doc.idDocument, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, infos.nomTiers,0,0,doc.netHtCalc,doc.netTtcCalc from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers where tiers.codeTiers like ? and doc.codeDocument like ? order by doc.codeDocument";
				FIND_BY_TIERS_AND_CODE="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and doc.codeDocument between ? and ? order by doc.codeDocument";
				FIND_BY_CODE="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and  doc.codeDocument between ? and ? order by doc.codeDocument";
				FIND_BY_DATE_PARDATE="select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like ? and  doc.dateDocument between ? and ? order by doc.dateDocument";
				FIND_BY_TIERS_AND_DATE="select doc from "+nameEntity+" doc join doc.taTiers tiers where tiers.codeTiers like ? and doc.dateDocument between ? and ? order by tiers.codeTiers,doc.codeDocument";

				FIND_ALL_LIGHT="select new fr.legrain.document.dto.DocumentDTO(doc.idDocument, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, infos.nomTiers, infos.prenomTiers, infos.nomEntreprise, doc.dateLivDocument, doc.netHtCalc,doc.netTvaCalc,doc.netTtcCalc) from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers order by doc.codeDocument";
//				FIND_ALL_LIGHT="select new fr.legrain.document.dto.TaBonlivDTO(doc.id, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, tiers.nomTiers) from "+nameEntity+" doc left join doc.taTiers tiers order by doc.codeDocument";
				FIND_BY_CODE_LIGHT="select new fr.legrain.document.dto.DocumentDTO(doc.id, doc.codeDocument, doc.dateDocument, doc.libelleDocument, tiers.codeTiers, tiers.nomTiers) from "+nameEntity+" doc left join doc.taTiers tiers where doc.codeDocument like :code order by doc.codeDocument";

				FIND_ALL_LIGHT_PERIODE=
						"select "
								+documentDTO.retournChampAndAlias("doc","idDocument")+", "
								+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
								+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
								+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
								+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
								+documentDTO.retournChampAndAlias("infos","nomTiers")+","
								+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
								+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
						+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers "
						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " order by doc.dateDocument DESC, doc.codeDocument DESC";	
				
				FIND_ALL_LIGHT_INTERVALLE=
						"select "
								+documentDTO.retournChampAndAlias("doc","idDocument")+", "
								+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
								+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
								+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
								+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
								+documentDTO.retournChampAndAlias("infos","nomTiers")+","
								+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
								+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
						+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers "
						+ " where doc.codeDocument between :codeDebut and :codeFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " order by doc.codeDocument DESC";	
				
				FIND_TRANSFORME_LIGHT_PERIODE=
						"select "
								+documentDTO.retournChampAndAlias("doc","idDocument")+", "
								+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
								+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
								+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
								+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
								+documentDTO.retournChampAndAlias("infos","nomTiers")+","
								+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
								+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
						+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " and exists ("+getRequeteDocumentTransforme("doc")+") order by doc.dateDocument DESC, doc.codeDocument DESC";
				
				FIND_NON_TRANSFORME_LIGHT_PERIODE=
						"select "
								+documentDTO.retournChampAndAlias("doc","idDocument")+", "
								+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
								+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
								+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
								+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
								+documentDTO.retournChampAndAlias("infos","nomTiers")+","
								+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
								+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
							+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
							+ " and doc.taEtat is null "
							+ " and not exists ("+getRequeteDocumentTransforme("doc")+") order by doc.dateDocument DESC, doc.codeDocument DESC";
				
				FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE=
						"select "
								+documentDTO.retournChampAndAlias("doc","idDocument")+", "
								+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
								+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
								+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
								+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
								+documentDTO.retournChampAndAlias("infos","nomTiers")+","
								+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
								+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
								+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
								+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
								+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers "
								+ " where doc.dateDocument between :dateDebut and :dateFin "
								+ " and doc.taEtat is null "
								+ getRequeteARelancerJPQL() +" and doc.taTiers.codeTiers like :codeTiers "
								+ " and not exists ("+getRequeteDocumentTransforme("doc")+") order by doc.dateDocument DESC, doc.codeDocument DESC";
				
				SUM_CA_TOTAL_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE=
						"select "
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers "
							+ " where doc.dateDocument between :dateDebut and :dateFin "+getRequeteARelancerJPQL() 
							+ " and doc.taEtat is null "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+ " and not exists ("+getRequeteDocumentTransforme("doc")+")";
				
				SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER=
						"select "
						+ documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
						+ " from "+nameEntity+" doc  "
						+ " where doc.dateDocument between :dateDebut and :dateFin  "+getRequeteARelancerJPQL() 
						+ " and doc.taTiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null "
						+" and not exists ("+getRequeteDocumentTransforme("doc")+")" 
						+ " group by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
						+ " order by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
			// Renvoi les totaux de l'ensemble des Proforma NON transformés groupé par mois sur la période datedebut à dateFin
			SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER=
					"select "
					+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
					+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
						+ " from "+nameEntity+" doc  "
						+ " where doc.dateDocument between :dateDebut and :dateFin "+getRequeteARelancerJPQL() 
						+ " and doc.taTiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null "
						+" and not exists ("+getRequeteDocumentTransforme("doc")+")" 
						+ " group by  extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
						+ " order by extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
			
			// Renvoi les totaux de l'ensemble des Proforma NON transformés groupé par année sur la période datedebut à dateFin
			SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER=
					"select "
					+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
						+ " from "+nameEntity+" doc  "
						+ " where doc.dateDocument between :dateDebut "+getRequeteARelancerJPQL() 
						+ " and doc.taTiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null "
						+" and not exists ("+getRequeteDocumentTransforme("doc")+")" 
						+ " group by  extract(year from doc.dateDocument) "
						+ " order by extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv groupé par jour sur la période datedebut à dateFin
				SUM_CA_JOUR_LIGTH_PERIODE=
						"select "
						+ documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers"
							+ " group by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv groupé par mois sur la période dateDebut à dateFin
				SUM_CA_MOIS_LIGTH_PERIODE=
						"select "
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers"
							+ " group by  extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv groupé par année sur la période dateDebut à dateFin
				SUM_CA_ANNEE_LIGTH_PERIODE=
						"select "
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers"
							+ " group by  extract(year from doc.dateDocument) "
							+ " order by extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGTH_PERIODE=
						"select "
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "					
							+ " and doc.taTiers.codeTiers like :codeTiers";
				
				
				// Renvoi les totaux de l'ensemble des Bonliv NON transformés groupé par jour sur la période dateDebut à dateFin
				SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taEtat is null "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and not exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv NON transformés groupé par mois sur la période dateDebut à dateFin
				SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taEtat is null "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and not exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by  extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv NON transformés groupé par année sur la période dateDebut à dateFin
				SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taEtat is null "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and not exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by  extract(year from doc.dateDocument) "
							+ " order by extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv NON transformés sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGTH_PERIODE_NON_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taEtat is null "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and not exists ("+getRequeteDocumentTransforme("doc")+") ";
				 
				// Renvoi les totaux de l'ensemble des Bonliv transformés groupé par jour sur la période dateDebut à dateFin
				SUM_CA_JOUR_LIGTH_PERIODE_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by extract(day from doc.dateDocument),extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv transformés groupé par mois sur la période dateDebut à dateFin
				SUM_CA_MOIS_LIGTH_PERIODE_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by  extract(month from doc.dateDocument),extract(year from doc.dateDocument) "
							+ " order by extract(month from doc.dateDocument),extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv transformés groupé par année sur la période dateDebut à dateFin
				SUM_CA_ANNEE_LIGTH_PERIODE_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by  extract(year from doc.dateDocument) "
							+ " order by extract(year from doc.dateDocument)";
				
				// Renvoi les totaux de l'ensemble des Bonliv transformés sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGTH_PERIODE_TRANSFORME=
						"select "
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+","
						+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc  "
							+ " where doc.dateDocument between :dateDebut and :dateFin "
							+ " and doc.taTiers.codeTiers like :codeTiers"
							+" and exists ("+getRequeteDocumentTransforme("doc")+") ";

				// Renvoi les articles des docs sur la période dateDebut à dateFin
				FIND_ARTICLES_PAR_TIERS_PAR_MOIS=
						"select "
								+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "
								+taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"
								+taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
								taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
								taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"
								
								+taArticlesParTiersDTO.retournChampAndAlias("fam", "codeFamille")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("fam", "libcFamille")+" ,"
								
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art left join art.taFamille fam"
						+ " where doc.dateDocument between :dateDebut and :dateFin and tiers.codeTiers like :codeTiers ";		
				
				// Renvoi les articles de l'ensemble des Bonliv transformé sur la période dateDebut à dateFin
				FIND_ARTICLES_PAR_TIERS_TRANSFORME=
						"select "
								+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "
								+taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"
								+taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
								taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
								taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
							+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art  "
							+ " where doc.dateDocument between :dateDebut and :dateFin and tiers.codeTiers like :codeTiers "
							+ " and exists ("+getRequeteDocumentTransforme("doc")+") order by doc.codeDocument";
				
				// Renvoi les articles de l'ensemble des Bonliv non transformé sur la période dateDebut à dateFin
				FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME=
						"select "
								+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "
								+taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"
								+taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
								taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
								taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
							+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art  "
							+ " where doc.dateDocument between :dateDebut and :dateFin and tiers.codeTiers like :codeTiers "
							+ " and doc.taEtat is null "
							+ " and not exists ("+getRequeteDocumentTransforme("doc")+") order by doc.codeDocument";
				
				// Renvoi les articles de l'ensemble des Bonliv non transformé sur la période dateDebut à dateFin
				FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER=
						"select "
								+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "
								+taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"
								+taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
								taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
								taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtHtLDocument")+","
								+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "mtTtcLDocument")+" "
								+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent join ldoc.taArticle art  "
								+ " where doc.dateDocument between :dateDebut and :dateFin "+getRequeteARelancerJPQL() 
								+ " and doc.taEtat is null and tiers.codeTiers like :codeTiers"
								+ " and not exists ("+getRequeteDocumentTransforme("doc")+") order by doc.codeDocument";

				
				FIND_BY_ETAT_TIERS_ENCOURS_DATE=
						"select doc from "+nameEntity+" doc where doc.taTiers.codeTiers like :codeTiers and doc.dateLivDocument < :date "
						+ "and doc.taRDocuments is empty order by doc.dateLivDocument";
				
//				// Renvoi les totaux de l'ensemble des factures pour un tiers sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS= 
						"select "
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+" , "
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+" , "
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
								+ " from "+nameEntity+" doc  "
								+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "
								+ " group by doc.taTiers.codeTiers";
//				// Renvoi les totaux de l'ensemble des factures pour un article sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE= 
						"select "
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netHtCalc")+" , "
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTvaCalc")+" , "
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("doc", "netTtcCalc")
							+ " from "+nameEntity+" doc left join doc.taTiers tiers left join doc.lignes ldoc left join ldoc.taArticle art  "
							+ " where art.codeArticle = :codearticle and doc.dateDocument between :dateDebut and :dateFin "
							+ " group by art.codeArticle";
				
				
				
				
				/*****RAJOUT YANN***********/
				// Renvoi les totaux  de l'ensemble des lignes des factures par article et/ou par tiers sur la période dateDebut à dateFin groupé par article
				LIST_SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE= 
						"select "
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", "
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								/**AJOUT QUANTITIE YANN**/
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "qteLDocument")+", "
								/**TEST CASE WHEN UNITE**/
//								+documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "u1LDocument")+", "
								+"coalesce(ldoc.u1LDocument, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "
								/**FIN TEST CASE**/
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
							+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam"
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle "
							+ " group by art.codeArticle, art.libellecArticle, fam.libcFamille, coalesce(ldoc.u1LDocument, '') "
							+ " order by art.codeArticle ";
				
				// Renvoi les totaux  de l'ensemble des lignes des factures par article et/ou par tiers sur la période dateDebut à dateFin groupé par Mois
				LIST_SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_MOIS= 
						"select "
								+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", "
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								/**AJOUT QUANTITIE YANN**/
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "qteLDocument")+", "
								+documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "u1LDocument")+", "
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
							+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam "
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle "
							+ " group by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+", art.codeArticle, art.libellecArticle, fam.libcFamille, ldoc.u1LDocument "
							+ " order by art.codeArticle, "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+" ";
				
				/**
				 * Renvoi les totaux des lignes des docs transformés sur une période pour un article, pour un tiers, par mois et groupé par mois
				 * Notamment utilisé pour le graphique dans le dash par article onglet devis/commande/proforma
				 */
				LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_TRANSFORME= 
						"select "
								+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", "
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								/**AJOUT QUANTITIE YANN**/
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "qteLDocument")+", "
								+documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "u1LDocument")+", "
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
							+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam "
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle "
							+ " and exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+", art.codeArticle, art.libellecArticle, fam.libcFamille, ldoc.u1LDocument "
							+ " order by art.codeArticle, "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+" ";

				
				/**
				 * Renvoi les totaux des lignes des docs non transformés sur une période pour un article, pour un tiers, par mois et groupé par mois
				 * Notamment utilisé pour le graphique dans le dash par article onglet devis/commande/proforma
				 */
				LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_NON_TRANSFORME= 
						"select "
								+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", "
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								/**AJOUT QUANTITIE YANN**/
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "qteLDocument")+", "
								+documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "u1LDocument")+", "
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
							+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam "
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle "
							+ " and doc.taEtat is null"
							+ " and not exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+", art.codeArticle, art.libellecArticle, fam.libcFamille, ldoc.u1LDocument "
							+ " order by art.codeArticle, "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+" ";
				
				/**
				 * Renvoi les totaux des lignes des docs non transformés à relancer sur une période pour un article, pour un tiers, par mois et groupé par mois
				 * Notamment utilisé pour le graphique dans le dash par article onglet devis/commande/proforma
				 */
				LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_A_RELANCER= 
						"select "
								+ documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", "
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								/**AJOUT QUANTITIE YANN**/
								+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "qteLDocument")+", "
								+documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "u1LDocument")+", "
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
							+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam "
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle "
							+getRequeteARelancerJPQL()
							+ " and doc.taEtat is null"
							+ " and not exists ("+getRequeteDocumentTransforme("doc")+") "
							+ " group by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+", art.codeArticle, art.libellecArticle, fam.libcFamille, ldoc.u1LDocument "
							+ " order by art.codeArticle, "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+" ";

				
				// Renvoi le COUNT de  l'ensemble des lignes des factures par article et/ou par tiers sur la période dateDebut à dateFin par Article
				COUNT_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE= 
						"select count(distinct art.codeArticle) as "+DocumentChiffreAffaireDTO.f_count+" "
							+ " from "+nameEntity+" doc  join doc.lignes ldoc  join ldoc.taArticle art"
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle ";
				// Renvoi le COUNT du nombre de famille différentes par article et/ou par tiers sur la période dateDebut à dateFin
				COUNT_FAMILLE_ARTICLE_PERIODE= 
						"select count(distinct fam.codeFamille) as "+DocumentChiffreAffaireDTO.f_count+" "
							+ " from "+nameEntity+" doc  join doc.lignes ldoc  join ldoc.taArticle art"
							+ " left join art.taFamille fam"
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle ";
				
				// Renvoi la SUM de  l'ensemble des lignes des factures(ou autre doc) par article et/ou par tiers sur la période dateDebut à dateFin 
				SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE = 
						"select "+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
								+ " from "+nameEntity+" doc  join doc.lignes ldoc  join ldoc.taArticle art"
								+ " where doc.dateDocument between :dateDebut and :dateFin"
								//rajout pour dash par article 
								+ " and art.codeArticle like :codeArticle";
				
				// Requete  pour les devis non transformé sur les lignes des devis sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_NON_TRANSFORME = 
						"select "+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
								+ " from "+nameEntity+" doc  join doc.lignes ldoc  join ldoc.taArticle art"
								+ " where doc.dateDocument between :dateDebut and :dateFin "
								+ " and doc.taEtat is null "
								+ " and not exists ("+getRequeteDocumentTransforme("doc")+") " 
								//rajout pour dash par article 
								+ " and art.codeArticle like :codeArticle";
				
				// Requete  pour les devis transformé sur les lignes des devis sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_TRANSFORME = 
						"select "+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
								+ " from "+nameEntity+" doc  join doc.lignes ldoc  join ldoc.taArticle art"
								+ " where doc.dateDocument between :dateDebut and :dateFin "
								+ " and exists ("+getRequeteDocumentTransforme("doc")+") " 
								//rajout pour dash par article 
								+ " and art.codeArticle like :codeArticle";
				
				// Requete  pour les devis transformé sur les lignes des devis sur la période dateDebut à dateFin
				SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_A_RELANCER = 
						"select "+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
								+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
								+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
								+ " from "+nameEntity+" doc  join doc.lignes ldoc  join ldoc.taArticle art"
								+ " where doc.dateDocument between :dateDebut and :dateFin "+getRequeteARelancerJPQL()
								+ " and doc.taEtat is null "
								+ " and not exists ("+getRequeteDocumentTransforme("doc")+") " 
								//rajout pour dash par article 
								+ " and art.codeArticle like :codeArticle";
				
				
				
				
				// Renvoi la SUM - les avoirs de  l'ensemble des lignes des factures par article et/ou par tiers sur la période dateDebut à dateFin 
				SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_MOINS_AVOIR = /*SQL NATIF*/
						" select cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+","
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+","
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+","
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+","
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+","
										+ " coalesce(sum(foo.mthtcalc),0) as mtHtCalc, coalesce(sum(foo.mttvacalc),0) as mtTvaCalc, coalesce(sum(foo.mtttccalc),0) as mtTtcCalc" + 
						"   from (" + 
						"    	select  coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as mthtcalc," + 
						"	  coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as mttvacalc," + 
						"	    coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as mtttccalc" + 
						"	    from ta_facture as doc " + 
						"	     join ta_l_facture ldoc on ldoc.id_document = doc.id_document" + 
						"	     left join ta_article art on ldoc.id_article = art.id_article" + 
						"	      where doc.date_document between :dateDebut and :dateFin   " + 
						"		and art.code_article like :codeArticle" + 
						" 	UNION ALL" + 
						" 	select  coalesce(sum(al.mt_ht_apr_rem_globale * -1),0) as mthtcalc," + 
						"	   coalesce(sum(al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale * -1),0) as mttvacalc," + 
						"	    coalesce(sum(al.mt_ttc_apr_rem_globale * 1),0) as mtttccalc" + 
						"	    from ta_avoir as a " + 
						"	     left join ta_l_avoir al on al.id_document= a.id_document" + 
						"	     left join ta_article art2 on al.id_article = art2.id_article" + 
						"	      where a.date_document between :dateDebut and :dateFin " + 
						"		and art2.code_article like :codeArticle" + 
						" 	) as foo";
				
				// Renvoi la liste des lignes des factures et des avoirs par article et/ou par tiers sur la période dateDebut à dateFin 
				LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE = /*SQL NATIF*/
						
						"select "+
						"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						//rajout article
						" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//rajout tiers
						" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
						" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
						" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+" "+
			"from ("+
			 "select "+  
			 			"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						 "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
						 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						 "doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						 //rajout article
						 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//rajout tiers
						 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
						 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
						 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						 "ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 "ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 "ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
						 "from ta_facture as doc "+
						//rajout tiers
						 "join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
						 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
						 "left join ta_article art on ldoc.id_article = art.id_article "+
						 "left join ta_famille fam on art.id_famille= fam.id_famille "+
						 "where doc.date_document between :dateDebut and :dateFin " + 
						 "and art.code_article like :codeArticle "+
						//rajout tiers
//						 "and tiers.code_tiers like :codeTiers "+
						 "UNION ALL "+
						 
						 "select "+
						 
						 	"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
							+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
							+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
							" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
							" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						 "'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
						 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						 "a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						 //rajout article
						 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 //rajout tiers
						 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
						 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
						 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						 "al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "al.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 "al.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 "al.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
						 "from ta_avoir as a "+
						//rajout tiers
						 "left join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
						 "left join ta_l_avoir al on al.id_document= a.id_document "+
						 "left join ta_article art2 on al.id_article = art2.id_article "+
						 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
						 "where a.date_document between :dateDebut and :dateFin " +
						//rajout tiers
//						 "and tiers.code_tiers like :codeTiers "+
				
						 "and art2.code_article like :codeArticle) b";
				
				LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_PAR_UNITE = /*SQL NATIF*/
						"select "+
								"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
								//rajout article
								" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								//rajout tiers
								" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
								" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+" "+
					"from ("+
					 "select "+  
					 			"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								 "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
								 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 //rajout article
								 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								//rajout tiers
								 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								 "ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+" ,"+
								 "ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
								 "ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
								 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
								 "from ta_facture as doc "+
								//rajout tiers
								 "join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
								 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
								 "left join ta_article art on ldoc.id_article = art.id_article "+
								 "left join ta_famille fam on art.id_famille= fam.id_famille "+
								 "where doc.date_document between :dateDebut and :dateFin " + 
								 "and art.code_article like :codeArticle "+
								 //rajout unite
								 "and ldoc.u1_l_document like :codeUnite "+
								//rajout tiers
//								 "and tiers.code_tiers like :codeTiers "+
								 "UNION ALL "+
								 
								 "select "+
								 
								 	"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
									+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
									+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
									" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
									" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								 "'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
								 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 //rajout article
								 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								 //rajout tiers
								 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								 "al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "al.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+" ,"+
								 "al.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
								 "al.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
								 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
								 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
								 "from ta_avoir as a "+
								//rajout tiers
								 "left join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
								 "left join ta_l_avoir al on al.id_document= a.id_document "+
								 "left join ta_article art2 on al.id_article = art2.id_article "+
								 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
								 "where a.date_document between :dateDebut and :dateFin " +
								 //rajout unite
								 "and al.u1_l_document like :codeUnite "+
								//rajout tiers
//								 "and tiers.code_tiers like :codeTiers "+
						
								 "and art2.code_article like :codeArticle) b";
				
				
				LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_WHERE_FAMILLE_LIKE = 
						"select "+
						"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						//rajout article
						" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_codeFamille+" as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
						" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//rajout tiers
						" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
						" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
						" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+" "+
			"from ("+
			 "select "+  
			 			"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						 "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
						 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						 "doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						 //rajout article
						 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 " fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
						//rajout tiers
						 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
						 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
						 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						 "ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+" ,"+
						 "ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 "ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
						 "from ta_facture as doc "+
						//rajout tiers
						 "join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
						 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
						 "left join ta_article art on ldoc.id_article = art.id_article "+
						 "left join ta_famille fam on art.id_famille= fam.id_famille "+
						 "where doc.date_document between :dateDebut and :dateFin " + 
						 "and fam.code_famille like :codeFamille "+
						 "and ldoc.u1_l_document like :codeUnite "+
						//rajout tiers
//						 "and tiers.code_tiers like :codeTiers "+
						 "UNION ALL "+
						 
						 "select "+
						 
						 	"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
							+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
							+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
							" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
							" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						 "'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
						 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						 "a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						 //rajout article
						 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 " fam2.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
						 //rajout tiers
						 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
						 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
						 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						 "al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "al.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 "al.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 "al.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
						 "from ta_avoir as a "+
						//rajout tiers
						 "left join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
						 "left join ta_l_avoir al on al.id_document= a.id_document "+
						 "left join ta_article art2 on al.id_article = art2.id_article "+
						 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
						 "where a.date_document between :dateDebut and :dateFin " +
						//rajout tiers
//						 "and tiers.code_tiers like :codeTiers "+
						 "and al.u1_l_document like :codeUnite "+
						 "and fam2.code_famille like :codeFamille ) b";
				
				LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE_PAR_UNITE = /*SQL NATIF*/
						"select "+
								"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
								//rajout article
								" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								//rajout tiers
								" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
								" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+" "+
					"from ("+
					 "select "+  
					 			"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								 "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
								 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 //rajout article
								 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								//rajout tiers
								 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								 "ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								 "ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+" "+
								 "from ta_facture as doc "+
								//rajout tiers
								 "join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
								 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
								 "left join ta_article art on ldoc.id_article = art.id_article "+
								 "left join ta_famille fam on art.id_famille= fam.id_famille "+
								 "where doc.date_document between :dateDebut and :dateFin " + 
								 "and art.code_article like :codeArticle "+
								 //rajout unite
								 "and ldoc.u1_l_document like :codeUnite "+
								//rajout tiers
								 "and tiers.code_tiers like :codeTiers "+
								 "UNION ALL "+
								 
								 "select "+
								 
								 	"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
									+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
									+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
									" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
									" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								 "'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
								 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 //rajout article
								 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								 //rajout tiers
								 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								 "al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
								 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								 "al.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+" "+
								 "from ta_avoir as a "+
								//rajout tiers
								 "left join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
								 "left join ta_l_avoir al on al.id_document= a.id_document "+
								 "left join ta_article art2 on al.id_article = art2.id_article "+
								 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
								 "where a.date_document between :dateDebut and :dateFin " +
								 //rajout unite
								 "and al.u1_l_document like :codeUnite "+
								//rajout tiers
								 "and tiers2.code_tiers like :codeTiers "+
						
								 "and art2.code_article like :codeArticle) b";
				
				
				LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_ARTICLE_AVOIR_FACTURE = /*SQL NATIF*/
						"select "+
								"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
								//rajout article
								" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								//rajout tiers
								" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
								" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+" "+
					"from ("+
					 "select "+  
					 			"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								 "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
								 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 //rajout article
								 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								//rajout tiers
								 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								 "ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								 "ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+" "+
								 "from ta_facture as doc "+
								//rajout tiers
								 "join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
								 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
								 "left join ta_article art on ldoc.id_article = art.id_article "+
								 "left join ta_famille fam on art.id_famille= fam.id_famille "+
								 "where doc.date_document between :dateDebut and :dateFin " + 
								 "and art.code_article like :codeArticle "+

								//rajout tiers
								 "and tiers.code_tiers like :codeTiers "+
								 "UNION ALL "+
								 
								 "select "+
								 
								 	"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
									+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
									+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
									" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
									" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								 "'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
								 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 //rajout article
								 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
								 //rajout tiers
								 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
								 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
								 "al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
								 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								 "al.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+" "+
								 "from ta_avoir as a "+
								//rajout tiers
								 "left join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
								 "left join ta_l_avoir al on al.id_document= a.id_document "+
								 "left join ta_article art2 on al.id_article = art2.id_article "+
								 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
								 "where a.date_document between :dateDebut and :dateFin " +

								//rajout tiers
								 "and tiers2.code_tiers like :codeTiers "+
						
								 "and art2.code_article like :codeArticle) b";

				
				// Renvoi la liste des lignes des factures et des avoirs par tiers  sur la période dateDebut à dateFin 
				LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE = /*SQL NATIF*/
						
						"select "+
						"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						//rajout article
						" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//rajout tiers
						" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
						" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
						" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+" "+
			"from ("+
			 "select "+  
			 			"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
						+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
						" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						 "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
						 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						 "doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						 //rajout article
						 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//rajout tiers
						 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
						 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
						 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						 "ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 "ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
						 "from ta_facture as doc "+
						//rajout tiers
						 "join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
						 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
						 "left join ta_article art on ldoc.id_article = art.id_article "+
						 "left join ta_famille fam on art.id_famille= fam.id_famille "+
						 "where doc.date_document between :dateDebut and :dateFin " + 
						 "and art.code_article like :codeArticle "+
						//rajout tiers
						 "and tiers.code_tiers like :codeTiers "+
						 "UNION ALL "+
						 
						 "select "+
						 
						 	"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
							+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
							+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
							" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
							" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
						 "'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"+
						 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
						 "a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
						 //rajout article
						 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 //rajout tiers
						 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "al.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
						 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
						 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						 "al.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 "al.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
						 "from ta_avoir as a "+
						//rajout tiers
						 "left join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
						 "left join ta_l_avoir al on al.id_document= a.id_document "+
						 "left join ta_article art2 on al.id_article = art2.id_article "+
						 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
						 "where a.date_document between :dateDebut and :dateFin " +
						//rajout tiers
						 "and tiers2.code_tiers like :codeTiers "+
				
						 "and art2.code_article like :codeArticle) b";

//				// requete qui renvoi une liste (synthèse) de sommes des lignes d'avoir et de facture groupé par tiers et par articles
//				LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE_GROUP_BY_TIERS =
//						"select "+
//								" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
//								+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
//								+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
//								//ajout article
//								" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
//								" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
//								" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
//								//ajout TIERS
//								" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
//								" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
//								" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+						
//								//unité et quantité à rajouter
//								" coalesce(sum(b.qteLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
//								" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
//								//unité2 et quantité à rajouter
//								" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
//								" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
//					"from ("+
//					 "select "+  
//
//								 "coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "+
//								 "coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "+
//								 "coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
//								 "coalesce(sum(ldoc.qte2_l_document),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
//								 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
//								 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
//								 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
//									//rajout tiers
//									 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
//									 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
//									 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
//									 
//								 "coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
//								 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
//								 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
//								 "from ta_facture as doc "+
//								 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
//								//rajout tiers
//								"join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
//								 "left join ta_article art on ldoc.id_article = art.id_article "+
//								 "left join ta_famille fam on art.id_famille= fam.id_famille "+
//								 "where doc.date_document between :dateDebut and :dateFin " +
//									//rajout tiers
//									 "and tiers.code_tiers like :codeTiers "+
//								 "and art.code_article like :codeArticle "+
//								 "GROUP BY "+
//								 DocumentChiffreAffaireDTO.f_codeTiers+", "+
//								 DocumentChiffreAffaireDTO.f_nomTiers+", "+
//								 DocumentChiffreAffaireDTO.f_prenomTiers+", "+
//								 DocumentChiffreAffaireDTO.f_codeArticle+", "+
//								 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
//								 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
//								 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
//								 DocumentChiffreAffaireDTO.f_libcFamille+" "+
//
//								 "UNION ALL "+
//								 
//								 "select "+
//								 "coalesce(al.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "+
//								 "coalesce(sum(al.qte_l_document *-1),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+",  "+
//								 "coalesce(al.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "+
//								 "coalesce(sum(al.qte2_l_document *-1),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+",  "+
//
//								 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
//								 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
//								 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
//								 //rajout tiers
//								 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
//								 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
//								 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
//								 
//								 "coalesce(sum(al.mt_ht_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
//								 "coalesce(sum((al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale) *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
//								 "coalesce(sum(al.mt_ttc_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
//								 "from ta_avoir as a "+
//								 "left join ta_l_avoir al on al.id_document= a.id_document "+
//									//rajout tiers
//									 "join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
//								 "left join ta_article art2 on al.id_article = art2.id_article "+
//								 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
//								 "where a.date_document between :dateDebut and :dateFin " +
//									//rajout tiers
//									 "and tiers2.code_tiers like :codeTiers "+
//								 "and art2.code_article like :codeArticle "+
//								 "GROUP BY "+
//								 DocumentChiffreAffaireDTO.f_codeTiers+", "+
//								 DocumentChiffreAffaireDTO.f_nomTiers+", "+
//								 DocumentChiffreAffaireDTO.f_prenomTiers+", "+
//								 DocumentChiffreAffaireDTO.f_codeArticle+", "+
//								 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
//								 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
//								 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
//								 DocumentChiffreAffaireDTO.f_libcFamille+" "
//
//								 + ") "+
//								 
//									
//								 "b GROUP BY "+
//								 "b."+DocumentChiffreAffaireDTO.f_codeTiers+", "+
//								 "b."+DocumentChiffreAffaireDTO.f_nomTiers+", "+
//								 "b."+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
//								 "b."+DocumentChiffreAffaireDTO.f_codeArticle+", "+
//								 "b."+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
//								 "b."+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
//								 "b."+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
//								 "b."+DocumentChiffreAffaireDTO.f_libcFamille+" ";
////								 "b."+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
////								 "b."+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
////								 "b."+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
				
				
				// requete qui renvoi une liste (synthèse) de sommes des lignes d'avoir et de facture groupé par article, avec le code Article, sa famille et le libelle famille
				// va être utilisé dans le dashboard article onglet "tous"
				LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_TIERS_ARTICLE = /*SQL NATIF*/
						
						"select "+
						" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
						+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
						+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						//ajout tiers
						" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						//ajout article
						" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//unité et quantité à rajouter
						" coalesce(sum(b.qteLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						//unité 2 et quantité 2 à rajouter
						" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
			"from ("+
			 "select "+  

						 "coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "+
						 "coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "+
						 "coalesce(sum(ldoc.qte2_l_document),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
							//rajout tiers
							 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
							 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
							 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 "coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
						 "from ta_facture as doc "+
						 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
						//rajout tiers
						"join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
						 "left join ta_article art on ldoc.id_article = art.id_article "+
						 "left join ta_famille fam on art.id_famille= fam.id_famille "+
						 "where doc.date_document between :dateDebut and :dateFin " + 
						 "and tiers.code_tiers like :codeTiers "+
						 "and art.code_article like :codeArticle "+
						 "GROUP BY "+
						 DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 DocumentChiffreAffaireDTO.f_libcFamille+" "+

						 "UNION ALL "+
						 
						 "select "+
						 "coalesce(al.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "+
						 "coalesce(sum(al.qte_l_document *-1),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+",  "+
						 "coalesce(al.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "+
						 "coalesce(sum(al.qte2_l_document *-1),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+",  "+
							//rajout tiers
							 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
							 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
							 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 "coalesce(sum(al.mt_ht_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
						 "coalesce(sum((al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale) *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
						 "coalesce(sum(al.mt_ttc_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
						 "from ta_avoir as a "+
						 "left join ta_l_avoir al on al.id_document= a.id_document "+
						//rajout tiers
						"join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
						 "left join ta_article art2 on al.id_article = art2.id_article "+
						 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
						 "where a.date_document between :dateDebut and :dateFin " +
						 "and tiers2.code_tiers like :codeTiers "+
						 "and art2.code_article like :codeArticle "+
						 "GROUP BY "+
						 DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 DocumentChiffreAffaireDTO.f_prenomTiers+" ,"+
						 DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 DocumentChiffreAffaireDTO.f_libcFamille+" "
//						 +DocumentChiffreAffaireDTO.f_mtHtCalc+", "
//						 +DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
//						 "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
						 + ") "+
						 
							
						 "b GROUP BY "+
						 "b."+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "b."+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "b."+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "b."+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 "b."+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 "b."+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 "b."+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 "b."+DocumentChiffreAffaireDTO.f_libcFamille+" ";
//						 "b."+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
//						 "b."+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
//						 "b."+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
					
				
				
				// requete qui renvoi une liste (synthèse) de sommes des lignes d'avoir et de facture groupé par tiers
					LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE_GROUP_BY_TIERS = /*SQL NATIF*/
						
						"select "+

						" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
						+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
						+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						//ajout TIERS
						" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+" "+						

			"from ("+
			 "select "+  

							//rajout tiers
							 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
							 "tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
							 "tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
							 
						 "coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
						 "from ta_facture as doc "+
						 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
						//rajout tiers
						"join ta_tiers as tiers on tiers.id_tiers = doc.id_tiers "+
						"join ta_article art on ldoc.id_article = art.id_article "+
						
						 "where doc.date_document between :dateDebut and :dateFin " +
							//rajout tiers
							 "and tiers.code_tiers like :codeTiers "+
						 "and art.code_article like :codeArticle "+
						 "GROUP BY "+
						 DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 DocumentChiffreAffaireDTO.f_prenomTiers+" "+



						 "UNION ALL "+
						 
						 "select "+
						 //rajout tiers
						 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "tiers2.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "+
						 "tiers2.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 
						 "coalesce(sum(al.mt_ht_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
						 "coalesce(sum((al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale) *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
						 "coalesce(sum(al.mt_ttc_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
						 "from ta_avoir as a "+
						 "left join ta_l_avoir al on al.id_document= a.id_document "+
							//rajout tiers
							 "join ta_tiers as tiers2 on tiers2.id_tiers = a.id_tiers "+
							 "join ta_article art2 on al.id_article = art2.id_article "+
						 "where a.date_document between :dateDebut and :dateFin " +
							//rajout tiers
							 "and tiers2.code_tiers like :codeTiers "+
						 "and art2.code_article like :codeArticle "+
						 "GROUP BY "+
						 DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 DocumentChiffreAffaireDTO.f_prenomTiers+" "+

						  ") "+
						 
							
						 "b GROUP BY "+
						 "b."+DocumentChiffreAffaireDTO.f_codeTiers+", "+
						 "b."+DocumentChiffreAffaireDTO.f_nomTiers+", "+
						 "b."+DocumentChiffreAffaireDTO.f_prenomTiers+" ";


				
				// requete qui renvoi une liste (synthèse) de sommes des lignes d'avoir et de facture groupé par article, avec le code Article, sa famille et le libelle famille
				// va être utilisé dans le dashboard article onglet "tous"
					LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_ARTICLE = /*SQL NATIF*/
						
						"select "+
//						" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
//						" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
//						" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
						+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
						+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						//ajout article
						" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//unité et quantité à rajouter
						" sum(coalesce(b.qteLDocument,0)) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+" ,"+
//						//unité2 et quantité2 à rajouter
						" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+" "+
//						" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+" "+
			"from ("+
			 "select "+  

						 "coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "+
						 "coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
//						 "coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "+
						 "sum(coalesce(ldoc.qte2_l_document,0)) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 "coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
						 "from ta_facture as doc "+
						 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
						 "left join ta_article art on ldoc.id_article = art.id_article "+
						 "left join ta_famille fam on art.id_famille= fam.id_famille "+
						 "where doc.date_document between :dateDebut and :dateFin " + 
						 "and art.code_article like :codeArticle "+
						 "GROUP BY "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
//						 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 DocumentChiffreAffaireDTO.f_libcFamille+" "+
//						 +DocumentChiffreAffaireDTO.f_mtHtCalc+", "
//						 +DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
//						 "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+

						 "UNION ALL "+
						 
						 "select "+

						 "coalesce(al.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "+
						 "coalesce(sum(al.qte_l_document *-1),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+",  "+
//						 "coalesce(al.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "+
						 "sum(coalesce(al.qte2_l_document *-1,0)) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+",  "+
						 //"al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
//						 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
//						 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
//						 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
						 " art2.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art2.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 "coalesce(sum(al.mt_ht_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
						 "coalesce(sum((al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale) *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
						 "coalesce(sum(al.mt_ttc_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "+
						 "from ta_avoir as a "+
						 "left join ta_l_avoir al on al.id_document= a.id_document "+
						 "left join ta_article art2 on al.id_article = art2.id_article "+
						 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
						 "where a.date_document between :dateDebut and :dateFin " +
						 "and art2.code_article like :codeArticle "+
						 "GROUP BY "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
//						 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 DocumentChiffreAffaireDTO.f_libcFamille+" "
//						 +DocumentChiffreAffaireDTO.f_mtHtCalc+", "
//						 +DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
//						 "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
						 + ") "+
						 
							
						 "b GROUP BY b."+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 "b."+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 "b."+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
//						 "b."+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 "b."+DocumentChiffreAffaireDTO.f_libcFamille+" ";
//						 "b."+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
//						 "b."+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
//						 "b."+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
				
		// même requete qu'au dessus mais groupé par famille au lieu de par article			
		LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_FAMILLE_ARTICLE = 						
				"select "+
	//				" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
	//				" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
	//				" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
					" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
					+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
					+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
					//ajout famille article
					" b."+DocumentChiffreAffaireDTO.f_codeFamille+" as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
					//unité et quantité à rajouter
					" coalesce(sum(b.qteLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
					" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+" "+
		"from ("+
		 "select "+  
					 "coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
	//				 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
	//				 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
	//				 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
					 //ajout famille article
					 " fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
					 "coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
					 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
					 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+",  "+
					 "coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+" "+
					 "from ta_facture as doc "+
					 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
					 "left join ta_article art on ldoc.id_article = art.id_article "+
					 "left join ta_famille fam on art.id_famille= fam.id_famille "+
					 "where doc.date_document between :dateDebut and :dateFin " + 
					 "and art.code_article like :codeArticle "+
					 "GROUP BY "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 DocumentChiffreAffaireDTO.f_libcFamille+", "+
					 DocumentChiffreAffaireDTO.f_u1LDocument+" "+

	
					 "UNION ALL "+
					 
					 "select "+
					 "coalesce(al.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
	//				 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
	//				 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
	//				 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
	 				 " fam2.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 " fam2.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
					 "coalesce(sum(al.mt_ht_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
					 "coalesce(sum((al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale) *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
					 "coalesce(sum(al.mt_ttc_apr_rem_globale *-1),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+",  "+
					 "coalesce(sum(al.qte_l_document *-1),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+" "+
					 "from ta_avoir as a "+
					 "left join ta_l_avoir al on al.id_document= a.id_document "+
					 "left join ta_article art2 on al.id_article = art2.id_article "+
					 "left join ta_famille fam2 on art2.id_famille= fam2.id_famille "+
					 "where a.date_document between :dateDebut and :dateFin " +
					 "and art2.code_article like :codeArticle "+
					 "GROUP BY "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 DocumentChiffreAffaireDTO.f_libcFamille+", "+
					 DocumentChiffreAffaireDTO.f_u1LDocument+" "
					 + ") "+
					 
						
					 "b GROUP BY b."+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 "b."+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
					 "b."+DocumentChiffreAffaireDTO.f_libcFamille+" ";


				/**VOIR TABLE LIGNE FACTURE**/
				// Renvoi les lignes des factures(ou autre doc) triées par tiers et par article  sur la période dateDebut à dateFin 
				LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS = 
						"select "
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
								+" coalesce(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument,0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								+ documentChiffreAffaireDTO.retournChampAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.retournChampAndAlias("doc","codeDocument")+","
							    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","qteLDocument")+","
							    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","u1LDocument")+","
							+ " tiers.codeTiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							+ " infos.nomEntreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							+ " infos.nomTiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							+ " infos.prenomTiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +", "
							+ " fam.codeFamille as "+DocumentChiffreAffaireDTO.f_codeFamille +" "
							+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam join doc.taInfosDocument infos"
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle ";
							// order by variabilisé
							//+ " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
				
				LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_UNITE = 
						"select "
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
								+" coalesce(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument,0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
								+ documentChiffreAffaireDTO.retournChampAndAlias("doc","dateDocument")+","
								+ documentChiffreAffaireDTO.retournChampAndAlias("doc","codeDocument")+","
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc","qteLDocument")+","
								+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc","u1LDocument")+","
							    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","qte2LDocument")+","
							    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","u2LDocument")+","
							+ " tiers.codeTiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							+ " infos.nomEntreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							+ " infos.nomTiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							+ " infos.prenomTiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
							+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
							+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +", "
							+ " fam.codeFamille as "+DocumentChiffreAffaireDTO.f_codeFamille +" "
							+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam join doc.taInfosDocument infos"
							+ " where doc.dateDocument between :dateDebut and :dateFin and" 
							+ " doc.taTiers.codeTiers like :codeTiers and "
							+ " art.codeArticle like :codeArticle and"
							+ " fam.codeFamille like :codeFamille "
							+ " and ldoc.u1LDocument like :codeUnite";
				

				
				// Renvoi les lignes des factures(ou autre doc) transformées triées par tiers et par article  sur la période dateDebut à dateFin
				LIST_LIGNE_ARTICLE_TRANSFO_PERIODE_PAR_TIERS=
						"select "
						+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
						+" coalesce(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument,0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
						+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
						+ documentChiffreAffaireDTO.retournChampAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournChampAndAlias("doc","codeDocument")+","
					    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","qteLDocument")+","
					    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","u1LDocument")+","
					+ " tiers.codeTiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
					+ " infos.nomEntreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
					+ " infos.nomTiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
					+ " infos.prenomTiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
					+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
					+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
					+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
					+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam join doc.taInfosDocument infos"
					+ " where doc.dateDocument between :dateDebut and :dateFin and" 
					+ " doc.taTiers.codeTiers like :codeTiers "
					+ " and exists ("+getRequeteDocumentTransforme("doc")+") "
					+ " and art.codeArticle like :codeArticle "
					/*+ " group by art.codeArticle, doc.dateDocument, tiers.codeTiers, infos.nomTiers, infos.prenomTiers, infos.nomEntreprise, art.libellecArticle, fam.libcFamille, ldoc.mtHtLApresRemiseGlobaleDocument, ldoc.mtTtcLApresRemiseGlobaleDocument"
					*/+ " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
				
				// Renvoi les lignes des factures(ou autre doc) non transformées triées par tiers et par article  sur la période dateDebut à dateFin
				LIST_LIGNE_ARTICLE_NON_TRANSFO_PERIODE_PAR_TIERS = 
						"select "
						+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
						+" coalesce(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument,0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
						+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
						+ documentChiffreAffaireDTO.retournChampAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournChampAndAlias("doc","codeDocument")+","
					    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","qteLDocument")+","
					    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","u1LDocument")+","
					+ " tiers.codeTiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
					+ " infos.nomEntreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
					+ " infos.nomTiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
					+ " infos.prenomTiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
					+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
					+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
					+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
					+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam join doc.taInfosDocument infos"
					+ " where doc.dateDocument between :dateDebut and :dateFin and" 
					+ " doc.taTiers.codeTiers like :codeTiers "
					+ " and doc.taEtat is null "
					+ " and not exists ("+getRequeteDocumentTransforme("doc")+") "
					+ " and art.codeArticle like :codeArticle "
					/*+ " group by art.codeArticle, doc.dateDocument, tiers.codeTiers, infos.nomTiers, infos.prenomTiers, infos.nomEntreprise, art.libellecArticle, fam.libcFamille, ldoc.mtHtLApresRemiseGlobaleDocument, ldoc.mtTtcLApresRemiseGlobaleDocument"
					*/+ " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
				
				// Renvoi les lignes des factures(ou autre doc) à relancer triées par tiers et par article  sur la période dateDebut à dateFin
				LIST_LIGNE_ARTICLE_A_RELANCER_PERIODE_PAR_TIERS=
						"select "
						+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+", " 
						+" coalesce(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument,0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +", "
						+ documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+", "
						+ documentChiffreAffaireDTO.retournChampAndAlias("doc","dateDocument")+","
						+ documentChiffreAffaireDTO.retournChampAndAlias("doc","codeDocument")+","
					    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","qteLDocument")+","
					    + documentChiffreAffaireDTO.retournChampAndAlias("ldoc","u1LDocument")+","
					+ " tiers.codeTiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
					+ " infos.nomEntreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
					+ " infos.nomTiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
					+ " infos.prenomTiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
					+ " art.codeArticle as "+DocumentChiffreAffaireDTO.f_codeArticle +", "
					+ " art.libellecArticle as "+DocumentChiffreAffaireDTO.f_libellecArticle +", "
					+ " fam.libcFamille as "+DocumentChiffreAffaireDTO.f_libcFamille +" "
					+ " from "+nameEntity+" doc  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art left join art.taFamille fam join doc.taInfosDocument infos"
					+ " where doc.dateDocument between :dateDebut and :dateFin and" 
					+ " doc.taTiers.codeTiers like :codeTiers  "
					+ " and doc.taEtat is null "
					+ " and not exists ("+getRequeteDocumentTransforme("doc")+") "
					+ " and art.codeArticle like :codeArticle "
					+ getRequeteARelancerJPQL()
					/*+ " group by art.codeArticle, doc.dateDocument, tiers.codeTiers, infos.nomTiers, infos.prenomTiers, infos.nomEntreprise, art.libellecArticle, fam.libcFamille, ldoc.mtHtLApresRemiseGlobaleDocument, ldoc.mtTtcLApresRemiseGlobaleDocument"
					*/+ " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
				
				
				//Remonte une liste de somme des (CA HT Facturés - CA HT des avoir) des lignes d'un doc pour un article groupé par mois (principalement pour les graphiques)
				//Requete Native car UNION
				SUM_CA_MOIS_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_MOINS_AVOIR = 
						"select "+
								"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
								" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
					"from (select "+
								 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
								 "from ta_facture as doc "+
								 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
								 "left join ta_article art on ldoc.id_article = art.id_article "+
								 "where doc.date_document between :dateDebut and :dateFin " + 
								 "and art.code_article like :codeArticle "+
								 "UNION ALL "+
								 "select "+
								 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
								 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
								 "from ta_avoir as a "+
								 "left join ta_l_avoir al on al.id_document= a.id_document "+
								 "left join ta_article art2 on al.id_article = art2.id_article "+
								 "where a.date_document between :dateDebut and :dateFin " +
								 "and art2.code_article like :codeArticle) "+
								 "b "+
								 "GROUP BY "+documentChiffreAffaireDTO.retournExtractMonth("b","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("b","dateDocument")+", b."+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 " b.mtHtCalc , " +
									" b.mtTvaCalc, " +
									" b.mtTtcCalc, "+
									DocumentChiffreAffaireDTO.f_jour+", "+
									DocumentChiffreAffaireDTO.f_mois+", "+
									DocumentChiffreAffaireDTO.f_annee+", "+
									DocumentChiffreAffaireDTO.f_reglementComplet+", "+
									DocumentChiffreAffaireDTO.f_resteAReglerComplet+" "+
								 "ORDER BY "+documentChiffreAffaireDTO.retournExtractMonth("b","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("b","dateDocument")+", b."+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 " b.mtHtCalc , " +
									" b.mtTvaCalc, " +
									" b.mtTtcCalc, " +
									DocumentChiffreAffaireDTO.f_jour+", "+
									DocumentChiffreAffaireDTO.f_mois+", "+
									DocumentChiffreAffaireDTO.f_annee+", "+
									DocumentChiffreAffaireDTO.f_reglementComplet+", "+
									DocumentChiffreAffaireDTO.f_resteAReglerComplet+" ";
					
					//A FINIR GROUPER ET TRIER PAR MOIS LA REQUETE CI DESSUS
//					+ " group by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+", art.codeArticle, art.libellecArticle, fam.libcFamille, ldoc.u1LDocument "
//					+ " order by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument")+" ";
				
// 				Remonte le nombre de tiers ayant acheter un article
				COUNT_TIER_AYANT_ACHETER_ARTICLE=
						" select count (distinct tiers.codeTiers) as nb from  "+nameEntity+" doc "
						+ "  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art "
						+ " where art.codeArticle like :codeArticle "
						+ "and doc.dateDocument between :dateDebut and :dateFin";
				
				LIST_TIER_AYANT_ACHETER_ARTICLE=
						"select distinct "
						+ " tiers.codeTiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
						+ " infos.nomEntreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
						+ " infos.nomTiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
						+ " infos.prenomTiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+" "+
						"from  "+nameEntity+" doc "
						+ "  join doc.taTiers tiers join doc.lignes ldoc  join ldoc.taArticle art join doc.taInfosDocument infos left join tiers.taEntreprise ent "
						+ " where art.codeArticle like :codeArticle "
						+ "and doc.dateDocument between :dateDebut and :dateFin";
				
				LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF=
						"select "+
								"cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_jour+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_mois+", "
								+ "cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_annee+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_reglementComplet+", "+
								" cast('0' as varchar) as "+DocumentChiffreAffaireDTO.f_resteAReglerComplet+", "+
								" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
								" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
								" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+" "+
					"from (select "+
								 "tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "doc.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 "ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "ldoc.mt_ht_apr_rem_globale  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
								 "ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
								 "from ta_facture as doc "+
								 "join ta_l_facture ldoc on ldoc.id_document = doc.id_document "+
								 "join ta_tiers tiers on doc.id_tiers = tiers.id_tiers "+
								 "left join ta_article art on ldoc.id_article = art.id_article "+
								 "where doc.date_document between :dateDebut and :dateFin " + 
								 "and art.code_article like :codeArticle "+
								 "and tiers.code_tiers like :codeTiers "+
								 "UNION ALL "+
								 "select "+
								 "tiers2.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "+
								 "a.date_document as "+DocumentChiffreAffaireDTO.f_dateDocument+", "+
								 "a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "+
								 "al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
								 "al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+
								 "al.mt_ttc_apr_rem_globale-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+
								 "al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "+
								 "from ta_avoir as a "+
								 "left join ta_l_avoir al on al.id_document= a.id_document "+
								 "join ta_tiers tiers2 on a.id_tiers = tiers2.id_tiers "+
								 "left join ta_article art2 on al.id_article = art2.id_article "+
								 "where a.date_document between :dateDebut and :dateFin " +
								 "and art2.code_article like :codeArticle "+
								 "and tiers2.code_tiers like :codeTiers "
								 + ") "+
								 "b ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
				
				/**Remonte la liste de tout les documents (devis par exemple) trié par codeArticle ou codeTiers sur la période
				 * utilisé dans dash par article onglet devis/commande/proforma (DocumentDTO)
				 * **/
				 FIND_ALL_LIGHT_LIGNE_PERIODE ="select distinct "
				 										+documentDTO.retournChampAndAlias("doc","idDocument")+", "  
				 										+documentDTO.retournChampAndAlias("doc","codeDocument")+", "  
				 										+documentDTO.retournChampAndAlias("doc","dateDocument")+", " 
				 										+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "  
				 										+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "  
				 										+documentDTO.retournChampAndAlias("infos","nomTiers")+", "  
				 										+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","  
				 										+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+", "  
				 										+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+", "  
				 										+documentDTO.retournChampAndAlias("doc","netHtCalc")+","  
				 										+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","  
				 										+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" " + 
				 								 " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers join doc.lignes ldoc join ldoc.taArticle art left join art.taFamille fam "  
				 								+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle " 
				 								+ " order by doc.codeDocument";
				 
				 /**Remonte la liste des documents transformés par codeArticle ou codeTiers sur la période
					 * utilisé dans dash par article onglet devis/commande/proforma (DocumentDTO)
					 * **/
				 FIND_TRANSFORME_LIGHT_LIGNE_PERIODE ="select distinct "
					 										+documentDTO.retournChampAndAlias("doc","idDocument")+", "  
					 										+documentDTO.retournChampAndAlias("doc","codeDocument")+", "  
					 										+documentDTO.retournChampAndAlias("doc","dateDocument")+", " 
					 										+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "  
					 										+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "  
					 										+documentDTO.retournChampAndAlias("infos","nomTiers")+", "  
					 										+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","  
					 										+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+", "  
					 										+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+", "  
					 										+documentDTO.retournChampAndAlias("doc","netHtCalc")+","  
					 										+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","  
					 										+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" " + 
					 								 " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers join doc.lignes ldoc join ldoc.taArticle art left join art.taFamille fam "  
					 								+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle " 
					 								+ " and exists ("+getRequeteDocumentTransforme("doc")+") order by doc.codeDocument";
				 
				 FIND_NON_TRANSFORME_LIGHT_LIGNE_PERIODE = "select distinct "
														+documentDTO.retournChampAndAlias("doc","idDocument")+", "  
														+documentDTO.retournChampAndAlias("doc","codeDocument")+", "  
														+documentDTO.retournChampAndAlias("doc","dateDocument")+", " 
														+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "  
														+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "  
														+documentDTO.retournChampAndAlias("infos","nomTiers")+", "  
														+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","  
														+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+", "  
														+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+", "  
														+documentDTO.retournChampAndAlias("doc","netHtCalc")+","  
														+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","  
														+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" " + 
												 " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers join doc.lignes ldoc join ldoc.taArticle art left join art.taFamille fam "  
												+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle " 
												+ " and doc.taEtat is null "
												+ " and not exists ("+getRequeteDocumentTransforme("doc")+") order by doc.codeDocument";
				 
				 FIND_NON_TRANSFORME_ARELANCER_LIGHT_LIGNE_PERIODE = "select distinct "
														+documentDTO.retournChampAndAlias("doc","idDocument")+", "  
														+documentDTO.retournChampAndAlias("doc","codeDocument")+", "  
														+documentDTO.retournChampAndAlias("doc","dateDocument")+", " 
														+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "  
														+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "  
														+documentDTO.retournChampAndAlias("infos","nomTiers")+", "  
														+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","  
														+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+", "  
														+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+", "  
														+documentDTO.retournChampAndAlias("doc","netHtCalc")+","  
														+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","  
														+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" " + 
												 " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers join doc.lignes ldoc join ldoc.taArticle art left join art.taFamille fam "  
												+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle " 
												+ " and doc.taEtat is null "
												+ getRequeteARelancerJPQL()
												+ " and not exists ("+getRequeteDocumentTransforme("doc")+") order by doc.codeDocument";
				 
				 LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_AVOIR_FACTURE_FOURNISSEUR_GROUP_BY_ARTICLE=				
							
							"select "
							+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "

//							+" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
//							+" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
							+" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
							+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
							+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "
							+" b."+DocumentChiffreAffaireDTO.f_fournisseur+" as "+DocumentChiffreAffaireDTO.f_fournisseur+", "
							+" b."+DocumentChiffreAffaireDTO.f_nomFournisseur+" as "+DocumentChiffreAffaireDTO.f_nomFournisseur+", "
							//rajout article
							+" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
//							//rajout tiers
//							+" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
//							+" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
//							+" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
//							+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "
							
							+" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
							+" coalesce(sum(b.qteLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","
							+" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+" "
							
//							+" b."+DocumentChiffreAffaireDTO.f_lot+" as "+DocumentChiffreAffaireDTO.f_lot+","
							
							+" from ("+
							"select "+  

//							 	"doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
//							 	//+ "TO_CHAR(doc.date_document : DATE, 'dd/mm/yyyy') as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
//								+ "doc.date_document  as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
							 "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
							 //rajout article
							 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							 +" art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							 +" tfour.code_tiers as "+DocumentChiffreAffaireDTO.f_fournisseur+", "
							 +" tfour.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomFournisseur+", "
//							 +" lot.numlot as "+DocumentChiffreAffaireDTO.f_lot+", "
							 +" coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "
							 +" coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							 +" coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "
							 +" coalesce(sum(ldoc.qte2_l_document),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
							 //rajout tiers
							 //							 +"tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							 //							 +"tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							 //							 +"tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							 +"coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
							 +"coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
							 +"coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "
							
							 +" from ta_l_facture ldoc "
							 +" join ta_facture doc on doc.id_document=ldoc.id_document "
							 +" join ta_article art on art.id_article=ldoc.id_article "
							 +" left join ta_famille fam on fam.id_famille=art.id_famille "
//							 +" join ta_tiers tiers on tiers.id_tiers=doc.id_tiers "
//							 +" left join ta_entreprise on ta_entreprise.id_entreprise=tiers.id_entreprise "
//							 +" left join ta_lot lot on lot.id_lot=ldoc.id_lot "
							 +" left join ta_r_fournisseur_article rfour on rfour.id_article=ldoc.id_article "
							 +" left join ta_tiers tfour on tfour.id_tiers=rfour.id_tiers "+
							 
							 "where doc.date_document between :dateDebut and :dateFin " + 
							 "and art.code_article like :codeArticle "+
							 "GROUP BY "+DocumentChiffreAffaireDTO.f_typeDoc+", "+
							 DocumentChiffreAffaireDTO.f_codeArticle+", "+
							 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
							 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
							 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
							 DocumentChiffreAffaireDTO.f_libcFamille+", "+
							 DocumentChiffreAffaireDTO.f_fournisseur+", "+
							 DocumentChiffreAffaireDTO.f_nomFournisseur+" "+
							//rajout tiers
//							 "and tiers.code_tiers like :codeTiers "+
							 " UNION ALL "+
							 
							"select "+ 

							"'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
							//rajout article
							+" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							+" art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							+" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							+" tfour.code_tiers as "+DocumentChiffreAffaireDTO.f_fournisseur+", "
							 +" tfour.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomFournisseur+", "
							//+" lot.numlot as "+DocumentChiffreAffaireDTO.f_lot+", "
							+" coalesce(al.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "
							+" coalesce(-sum(al.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							+" coalesce(al.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "
							+" coalesce(-sum(al.qte2_l_document),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
							//rajout tiers
							//							 +"tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							//							 +"tiers.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							//							 +"tiers.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							+"coalesce(-sum(al.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
							+"coalesce(-sum((al.mt_ttc_apr_rem_globale)-(-al.mt_ht_apr_rem_globale)),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
							+"coalesce(-sum(al.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "

							 +" from ta_l_avoir al "
							 +" join ta_avoir a on a.id_document=al.id_document "
							 +" join ta_article art on art.id_article=al.id_article "
							 +" left join ta_famille fam on fam.id_famille=art.id_famille "
//							 +" join ta_tiers tiers on tiers.id_tiers=a.id_tiers "
//							 +" left join ta_entreprise on ta_entreprise.id_entreprise=tiers.id_entreprise "
//							 +" left join ta_lot lot on lot.id_lot=al.id_lot "
							 +" left join ta_r_fournisseur_article rfour on rfour.id_article=al.id_article "
							 +" left join ta_tiers tfour on tfour.id_tiers=rfour.id_tiers "+
						
							 "where a.date_document between :dateDebut and :dateFin " +
							//rajout tiers
//							 "and tiers.code_tiers like :codeTiers "+
					
							 "and art.code_article like :codeArticle "+
							 "GROUP BY "+DocumentChiffreAffaireDTO.f_typeDoc+", "+
							 DocumentChiffreAffaireDTO.f_codeArticle+", "+
							 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
							 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
							 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
							 DocumentChiffreAffaireDTO.f_libcFamille+", "+
							 DocumentChiffreAffaireDTO.f_fournisseur+", "+
							 DocumentChiffreAffaireDTO.f_nomFournisseur+" "+
							  ") b "+
								 "GROUP BY "+DocumentChiffreAffaireDTO.f_typeDoc+", "+
								 DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
								 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
								 DocumentChiffreAffaireDTO.f_libcFamille+", "+
								 DocumentChiffreAffaireDTO.f_fournisseur+", "+
								 DocumentChiffreAffaireDTO.f_nomFournisseur+" ";
					
				 LIST_LIGNE_ARTICLE_PERIODE_AVOIR_FACTURE_FOURNISSEUR = /*SQL NATIF*/
							
							"select "
							+" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_dateLivraison+" as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
							+" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
							+" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
							+" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "
							//rajout article
							+" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_codeFamille+" as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
							+" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							//rajout tiers
							+" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							+" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							+" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							+" b."+DocumentChiffreAffaireDTO.f_nomEntreprise+" as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "
							
							+" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","
							+" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
							
							+" b."+DocumentChiffreAffaireDTO.f_lot+" as "+DocumentChiffreAffaireDTO.f_lot+","
							+" b."+DocumentChiffreAffaireDTO.f_fournisseur+" as "+DocumentChiffreAffaireDTO.f_fournisseur+","
							+" b."+DocumentChiffreAffaireDTO.f_nomFournisseur+" as "+DocumentChiffreAffaireDTO.f_nomFournisseur+" "
							
							+" from ("+
							"select "+  

							 	"doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
							 	//+ "TO_CHAR(doc.date_document : DATE, 'dd/mm/yyyy') as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
								+ "doc.date_document  as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
								+ "doc.date_liv_document  as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
							+ "'"+TaFacture.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
							 //rajout article
							 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							 +" ldoc.lib_l_document as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							 +" fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
							 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							 +" tfour.code_tiers as "+DocumentChiffreAffaireDTO.f_fournisseur+", "
							 +" tfour.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomFournisseur+", "
							 +" lot.numlot as "+DocumentChiffreAffaireDTO.f_lot+", "
							 +" ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							 +" ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
							 +" ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
							 +" ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "
							 //rajout tiers
							 +"tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							 +"ta_entreprise.nom_entreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							 +"infos.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							 +"infos.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							 +"ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
							 +"ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
							 +"ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "
							
							 +" from ta_l_facture ldoc "
							 +" join ta_facture doc on doc.id_document=ldoc.id_document "
							 +" join ta_infos_facture infos on infos.id_document=doc.id_document "
							 +" join ta_article art on art.id_article=ldoc.id_article "
							 +" left join ta_famille fam on fam.id_famille=art.id_famille "
							 +" join ta_tiers tiers on tiers.id_tiers=doc.id_tiers "
							 +" left join ta_entreprise on ta_entreprise.id_entreprise=tiers.id_entreprise "
							 +" left join ta_lot lot on lot.id_lot=ldoc.id_lot "
							 +" left join ta_r_fournisseur_article rfour on rfour.id_article=ldoc.id_article "
							 +" left join ta_tiers tfour on tfour.id_tiers=rfour.id_tiers "+
							 
							 "where doc.date_document between :dateDebut and :dateFin " + 
							 "and art.code_article like :codeArticle "+
							//rajout tiers
//							 "and tiers.code_tiers like :codeTiers "+
							 "UNION ALL "+
							 
							 "select "+
							 
							 	"a.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
//								+ "TO_CHAR(a.date_document : DATE, 'dd/mm/yyyy') as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
								+ "a.date_document  as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
								+ "a.date_liv_document  as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
							 +"'"+TaAvoir.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+"  ,"
							 //rajout article
							 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							 +" al.lib_l_document as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							 +" fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
							 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							 +" tfour.code_tiers as "+DocumentChiffreAffaireDTO.f_fournisseur+", "
							 +" tfour.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomFournisseur+", "
							 +" lot.numlot as "+DocumentChiffreAffaireDTO.f_lot+", "
							 +" -al.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							 +" al.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
							 +" -al.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
							 +" al.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "
							 //rajout tiers
							 +"tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							 +"ta_entreprise.nom_entreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							 +"infos.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							 +"infos.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							 +"-al.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
							 +"(-al.mt_ttc_apr_rem_globale)-(-al.mt_ht_apr_rem_globale) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
							 +"-al.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "

							 +" from ta_l_avoir al "
							 +" join ta_avoir a on a.id_document=al.id_document "
							 +" join ta_infos_avoir infos on infos.id_document=a.id_document "
							 +" join ta_article art on art.id_article=al.id_article "
							 +" left join ta_famille fam on fam.id_famille=art.id_famille "
							 +" join ta_tiers tiers on tiers.id_tiers=a.id_tiers "
							 +" left join ta_entreprise on ta_entreprise.id_entreprise=tiers.id_entreprise "
							 +" left join ta_lot lot on lot.id_lot=al.id_lot "
							 +" left join ta_r_fournisseur_article rfour on rfour.id_article=al.id_article "
							 +" left join ta_tiers tfour on tfour.id_tiers=rfour.id_tiers "+
						
							 "where a.date_document between :dateDebut and :dateFin " +
							//rajout tiers
//							 "and tiers.code_tiers like :codeTiers "+
					
							 "and art.code_article like :codeArticle) b";	 
			
			
				 LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_BONLIV_TRANSPORTEUR_GROUP_BY_ARTICLE=				
							
							"select "
							+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "

							+" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
							+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
							+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "
							+" b."+DocumentChiffreAffaireDTO.f_codeTransporteur+" as "+DocumentChiffreAffaireDTO.f_codeTransporteur+", "
							+" b."+DocumentChiffreAffaireDTO.f_liblTransporteur+" as "+DocumentChiffreAffaireDTO.f_liblTransporteur+", "
							//rajout article
							+" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							
							+" b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
							+" coalesce(sum(b.qteSaisieLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
							+" coalesce(sum(b.qteLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","
							+" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+" ,"
							
							+" b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "

							
							+" from ("+
							"select "+  

							 "'"+TaBonliv.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
							 //rajout article
							 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							 +" art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							 +" tr.code_transporteur as "+DocumentChiffreAffaireDTO.f_codeTransporteur+", "
							 +" tr.libl_transporteur as "+DocumentChiffreAffaireDTO.f_liblTransporteur+", "

							 +" coalesce(ldoc.u_saisie_l_document, '') as  "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
							 +" coalesce(sum(ldoc.qte_Saisie_L_Document),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
							 +" coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "
							 +" coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							 +" coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "
							 +" coalesce(sum(ldoc.qte2_l_document),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "

							 +"coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
							 +"coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
							 +"coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" , "
							 +"doc.utilise_unite_saisie as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+""
							
							 +" from ta_l_bonliv ldoc "
							 +" join ta_bonliv doc on doc.id_document=ldoc.id_document "
							 +" join ta_article art on art.id_article=ldoc.id_article "
							 +" left join ta_famille fam on fam.id_famille=art.id_famille "

							 +" left join ta_transporteur tr on tr.id_transporteur=doc.id_transporteur "+

							 
							 "where doc.date_liv_document between :dateDebut and :dateFin " + 
							 "and art.code_article like :codeArticle "+
							 "and tr.code_transporteur like :codeTransporteur "+
							 "GROUP BY "+DocumentChiffreAffaireDTO.f_typeDoc+", "+
							 DocumentChiffreAffaireDTO.f_codeTransporteur+", "+
							 DocumentChiffreAffaireDTO.f_liblTransporteur+", "+
							 DocumentChiffreAffaireDTO.f_codeArticle+", "+
							 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
							 DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
							 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
							 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
							 DocumentChiffreAffaireDTO.f_libcFamille+", "+
							 DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "+
							  ") b "+
								 "GROUP BY "+DocumentChiffreAffaireDTO.f_typeDoc+", "+
								 DocumentChiffreAffaireDTO.f_codeTransporteur+", "+
								 DocumentChiffreAffaireDTO.f_liblTransporteur+", "+
								 DocumentChiffreAffaireDTO.f_codeArticle+", "+
								 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
								 DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
								 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
								 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
								 DocumentChiffreAffaireDTO.f_libcFamille+", "+
								 DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" ";

				 
				 LIST_LIGNE_ARTICLE_PERIODE_BONLIV_TRANSPORTEUR = /*SQL NATIF*/
							
							"select "
							+" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_dateLivraison+" as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
							+" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
							+" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
							+" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "
							//rajout article
							+" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							+" b."+DocumentChiffreAffaireDTO.f_codeFamille+" as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
							+" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							//rajout tiers
							+" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							+" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							+" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							+" b."+DocumentChiffreAffaireDTO.f_nomEntreprise+" as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "
							
							+" b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							+" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","
							+" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
							
							+" b."+DocumentChiffreAffaireDTO.f_lot+" as "+DocumentChiffreAffaireDTO.f_lot+","
							+" b."+DocumentChiffreAffaireDTO.f_fournisseur+" as "+DocumentChiffreAffaireDTO.f_fournisseur+","
							+" b."+DocumentChiffreAffaireDTO.f_nomFournisseur+" as "+DocumentChiffreAffaireDTO.f_nomFournisseur+", "
														
							+" b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "

							
							+" from ("+
							"select "+  

							 	"doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
								+ "doc.date_document  as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
								+ "doc.date_liv_document  as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
							+ "'"+TaBonliv.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
							 //rajout article
							 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
							 +" ldoc.lib_l_document as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
							 +" fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
							 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
							 +" tr.code_transporteur as "+DocumentChiffreAffaireDTO.f_codeTransporteur+", "
							 +" tr.libl_transporteur as "+DocumentChiffreAffaireDTO.f_liblTransporteur+", "
							 +" lot.numlot as "+DocumentChiffreAffaireDTO.f_lot+", "
							 +" ldoc.qte_saisie_l_document as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
							 +" ldoc.u_saisie_l_document as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
							 +" ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
							 +" ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
							 +" ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
							 +" ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "
							 
							 +" doc.utilise_unite_saisie as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+", "
							 //rajout tiers
							 +"tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
							 +"ta_entreprise.nom_entreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
							 +"infos.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
							 +"infos.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
							 +"ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
							 +"ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
							 +"ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "
							
							 +" from ta_l_bonliv ldoc "
							 +" join ta_bonliv doc on doc.id_document=ldoc.id_document "
							 +" join ta_infos_bonliv infos on infos.id_document=doc.id_document "
							 +" join ta_article art on art.id_article=ldoc.id_article "
							 +" left join ta_famille fam on fam.id_famille=art.id_famille "
							 +" join ta_tiers tiers on tiers.id_tiers=doc.id_tiers "
							 +" left join ta_entreprise on ta_entreprise.id_entreprise=tiers.id_entreprise "
							 +" left join ta_lot lot on lot.id_lot=ldoc.id_lot "
							 +" left join ta_transporteur rfour on rt.id_transporteur=doc.id_transporteur "+

							 
							 "where doc.date_liv_document between :dateDebut and :dateFin " + 
							 "and tr.code_transporteur like :codeTransporteur "+
							 "and art.code_article like :codeArticle ) b";	 
			
			
			 LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_BONLIV_SANS_TRANSPORTEUR_GROUP_BY_ARTICLE=				
						
						"select "
						+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "

						+" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
						+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
						+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "
						+" b."+DocumentChiffreAffaireDTO.f_codeTransporteur+" as "+DocumentChiffreAffaireDTO.f_codeTransporteur+", "
						+" b."+DocumentChiffreAffaireDTO.f_liblTransporteur+" as "+DocumentChiffreAffaireDTO.f_liblTransporteur+", "
						//rajout article
						+" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
						+" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
						+" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "

						+" b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
						+" coalesce(sum(b.qteSaisieLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "						
						+" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
						+" coalesce(sum(b.qteLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
						+" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","
						+" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+" ,"
						+" b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "

						
						+" from ("+
						"select "+  

						 "'"+TaBonliv.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
						 //rajout article
						 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
						 +" art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
						 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
						 +" tr.code_transporteur as "+DocumentChiffreAffaireDTO.f_codeTransporteur+", "
						 +" tr.libl_transporteur as "+DocumentChiffreAffaireDTO.f_liblTransporteur+", "

						 +" coalesce(ldoc.u_saisie_l_document, '') as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+",  "
						 +" coalesce(sum(ldoc.qte_saisie_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
						 +" coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "
						 +" coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
						 +" coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "
						 +" coalesce(sum(ldoc.qte2_l_document),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
						 
						 +" doc.utilise_unite_saisie as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+", "
						 
						 +"coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
						 +"coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
						 +"coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+"  "
						
						 +" from ta_l_bonliv ldoc "
						 +" join ta_bonliv doc on doc.id_document=ldoc.id_document "
						 +" join ta_article art on art.id_article=ldoc.id_article "
						 +" left join ta_famille fam on fam.id_famille=art.id_famille "

						 +" left join ta_transporteur tr on tr.id_transporteur=doc.id_transporteur "+

						 
						 "where doc.date_liv_document between :dateDebut and :dateFin " + 
						 "and art.code_article like :codeArticle "+
						 "and tr.code_transporteur is null "+
						 "GROUP BY "+DocumentChiffreAffaireDTO.f_typeDoc+", "+
						 DocumentChiffreAffaireDTO.f_codeTransporteur+", "+
						 DocumentChiffreAffaireDTO.f_liblTransporteur+", "+
						 DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
						 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 DocumentChiffreAffaireDTO.f_libcFamille+" ,"+
						 DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "+
						  ") b "+
							 "GROUP BY "+DocumentChiffreAffaireDTO.f_typeDoc+", "+
							 DocumentChiffreAffaireDTO.f_codeTransporteur+", "+
							 DocumentChiffreAffaireDTO.f_liblTransporteur+", "+
							 DocumentChiffreAffaireDTO.f_codeArticle+", "+
							 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
							 DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
							 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
							 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
							 DocumentChiffreAffaireDTO.f_libcFamille+", "+
							 DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" ";

			 
			 LIST_LIGNE_ARTICLE_PERIODE_BONLIV_SANS_TRANSPORTEUR = /*SQL NATIF*/
						
						"select "
						+" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
						+" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
						+" b."+DocumentChiffreAffaireDTO.f_dateLivraison+" as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
						+" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
						+" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
						+" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "
						//rajout article
						+" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
						+" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
						+" b."+DocumentChiffreAffaireDTO.f_codeFamille+" as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
						+" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
						//rajout tiers
						+" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
						+" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
						+" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
						+" b."+DocumentChiffreAffaireDTO.f_nomEntreprise+" as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
						+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "
						
						+" b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
						+" b."+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
						+" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
						+" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
						+" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","
						+" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
						
						+" b."+DocumentChiffreAffaireDTO.f_lot+" as "+DocumentChiffreAffaireDTO.f_lot+","
						+" b."+DocumentChiffreAffaireDTO.f_fournisseur+" as "+DocumentChiffreAffaireDTO.f_fournisseur+","
						+" b."+DocumentChiffreAffaireDTO.f_nomFournisseur+" as "+DocumentChiffreAffaireDTO.f_nomFournisseur+", "
						+" b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "
						
						+" from ("+
						"select "+  

						 	"doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
							+ "doc.date_document  as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
							+ "doc.date_liv_document  as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
						+ "'"+TaBonliv.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
						 //rajout article
						 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
						 +" ldoc.lib_l_document as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
						 +" fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
						 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
						 +" tr.code_transporteur as "+DocumentChiffreAffaireDTO.f_codeTransporteur+", "
						 +" tr.libl_transporteur as "+DocumentChiffreAffaireDTO.f_liblTransporteur+", "
						 +" lot.numlot as "+DocumentChiffreAffaireDTO.f_lot+", "
						 +" ldoc.qte_saisie_l_document as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
						 +" ldoc.u_saisie_l_document as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
						 +" ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
						 +" ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
						 +" ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
						 +" ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "
						 
						  +" doc.utiliseUniteSaisie as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+", "
						  
						 //rajout tiers
						 +"tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
						 +"ta_entreprise.nom_entreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
						 +"infos.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
						 +"infos.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
						 +"ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
						 +"ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
						 +"ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "
						
						 +" from ta_l_bonliv ldoc "
						 +" join ta_bonliv doc on doc.id_document=ldoc.id_document "
						 +" join ta_infos_bonliv infos on infos.id_document=doc.id_document "
						 +" join ta_article art on art.id_article=ldoc.id_article "
						 +" left join ta_famille fam on fam.id_famille=art.id_famille "
						 +" join ta_tiers tiers on tiers.id_tiers=doc.id_tiers "
						 +" left join ta_entreprise on ta_entreprise.id_entreprise=tiers.id_entreprise "
						 +" left join ta_lot lot on lot.id_lot=ldoc.id_lot "
						 +" left join ta_transporteur rfour on rt.id_transporteur=doc.id_transporteur "+

						 
						 "where doc.date_liv_document between :dateDebut and :dateFin " + 
						 "and tr.code_transporteur is null "+
						 "and art.code_article like :codeArticle ) b";
			 
			 
				// requete qui renvoi une liste (synthèse) de sommes des lignes de bon de cde groupé par article, avec le code Article, sa famille et le libelle famille
				// va être utilisé dans le dashboard article onglet "tous"
					LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_BCDE_GROUP_BY_ARTICLE = /*SQL NATIF*/
						
						"select "+
						" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
						+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
						+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
						//ajout article
						" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						//unité et quantité à rajouter
						" b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
						" coalesce(sum(b.qteSaisieLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "+
						" sum(coalesce(b.qteLDocument,0)) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+" ,"+
//						//unité2 et quantité2 à rajouter
						" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 "'"+TaBoncde.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"+
						 " b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "+
			"from ("+
			 "select "+  
			 			 "coalesce(ldoc.u_saisie_l_document, '') as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+",  "+
			 			 "coalesce(sum(ldoc.qte_saisie_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "+
						 "coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+",  "+
						 "coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
						 "coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+",  "+
						 "sum(coalesce(ldoc.qte2_l_document,0)) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
						 " art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 " art.libellec_article as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
						 "coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
						 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" , "+
						 "'"+TaBoncde.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"+
						 " doc.utilise_unite_saisie as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "+
						 "from ta_boncde as doc "+
						 "join ta_l_boncde ldoc on ldoc.id_document = doc.id_document "+
						 "left join ta_article art on ldoc.id_article = art.id_article "+
						 "left join ta_famille fam on art.id_famille= fam.id_famille "+
						 "where doc.date_liv_document between :dateDebut and :dateFin " + 
						 "and art.code_article like :codeArticle "+
						 "GROUP BY "+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
						 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 DocumentChiffreAffaireDTO.f_libcFamille+" ,"+
						 DocumentChiffreAffaireDTO.f_utiliseUniteSaisie
						 + ") "+					 
							
						 "b GROUP BY b."+DocumentChiffreAffaireDTO.f_codeArticle+", "+
						 "b."+DocumentChiffreAffaireDTO.f_libellecArticle+", "+
						 "b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
						 "b."+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
						 "b."+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
						 "b."+DocumentChiffreAffaireDTO.f_libcFamille+" ,"+
						 "b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie;

				
		// même requete qu'au dessus mais groupé par famille au lieu de par article			
		LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_BCDE_GROUP_BY_FAMILLE_ARTICLE = 						
				"select "+
	//				" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " +
	//				" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " +
	//				" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
					" coalesce(sum(b.mtHtCalc ),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
					+ " coalesce(sum(b.mtTvaCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
					+ " coalesce(sum(b.mtTtcCalc),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "+
					//ajout famille article
					" b."+DocumentChiffreAffaireDTO.f_codeFamille+" as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
					//unité et quantité à rajouter
					
					" b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
					" coalesce(sum(b.qteSaisieLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "+
					" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
					" coalesce(sum(b.qteLDocument),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
					" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","+
					" coalesce(sum(b.qte2LDocument),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "+
					 " b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "+
					
		"from ("+
		 "select "+  
					 " fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 " fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "+
					 "coalesce(sum(ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "+ 
					 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "+ 
					 "coalesce(sum(ldoc.mt_ttc_apr_rem_globale),0) as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+",  "+
					 
					 "coalesce(ldoc.u_saisie_l_document, '') as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
					 "coalesce(sum(ldoc.qte_saisie_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "+
					 "coalesce(ldoc.u1_l_document, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
					 "coalesce(sum(ldoc.qte_l_document),0) as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "+
					 "coalesce(ldoc.u2_l_document, '') as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
					 "coalesce(sum(ldoc.qte2_l_document),0) as "+DocumentChiffreAffaireDTO.f_qte2LDocument+" ,"+
					 " doc.utilise_unite_saisie as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "+
					
					 "from ta_boncde as doc "+
					 "join ta_l_boncde ldoc on ldoc.id_document = doc.id_document "+
					 "left join ta_article art on ldoc.id_article = art.id_article "+
					 "left join ta_famille fam on art.id_famille= fam.id_famille "+
					 "where doc.date_liv_document between :dateDebut and :dateFin " + 
					 "and art.code_article like :codeArticle "+
					 "GROUP BY "+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 DocumentChiffreAffaireDTO.f_libcFamille+", "+
					 DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
					 DocumentChiffreAffaireDTO.f_u1LDocument+", "+
					 DocumentChiffreAffaireDTO.f_u2LDocument+", "+
					  DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "
					 
					 + ") "+					 
						
					 "b GROUP BY b."+DocumentChiffreAffaireDTO.f_codeFamille+", "+
					 "b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "+
					 "b."+DocumentChiffreAffaireDTO.f_u1LDocument+", "+
					 "b."+DocumentChiffreAffaireDTO.f_u2LDocument+", "+
					 "b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+", "+
					 "b."+DocumentChiffreAffaireDTO.f_libcFamille+" ";

		
		LIST_LIGNE_ARTICLE_PERIODE_BONCDE_DETAIL = /*SQL NATIF*/
					
					"select "
					+" b."+DocumentChiffreAffaireDTO.f_codeDocument+" as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
					+" b."+DocumentChiffreAffaireDTO.f_dateDocument+" as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
					+" b."+DocumentChiffreAffaireDTO.f_dateLivraison+" as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
					+" b.mtHtCalc  as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", " 
					+" b.mtTvaCalc as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", " 
					+" b.mtTtcCalc as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+", "
					//rajout article
					+" b."+DocumentChiffreAffaireDTO.f_codeArticle+" as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
					+" b."+DocumentChiffreAffaireDTO.f_libellecArticle+" as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
					+" b."+DocumentChiffreAffaireDTO.f_codeFamille+" as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
					+" b."+DocumentChiffreAffaireDTO.f_libcFamille+" as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
					//rajout tiers
					+" b."+DocumentChiffreAffaireDTO.f_codeTiers+" as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
					+" b."+DocumentChiffreAffaireDTO.f_nomTiers+" as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
					+" b."+DocumentChiffreAffaireDTO.f_prenomTiers+" as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
					+" b."+DocumentChiffreAffaireDTO.f_nomEntreprise+" as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
					+" b."+DocumentChiffreAffaireDTO.f_typeDoc+" as "+DocumentChiffreAffaireDTO.f_typeDoc+", "
					
					+" b."+DocumentChiffreAffaireDTO.f_uSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
					+" b."+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+" as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
					+" b."+DocumentChiffreAffaireDTO.f_u1LDocument+" as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
					+" b."+DocumentChiffreAffaireDTO.f_qteLDocument+" as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
					+" b."+DocumentChiffreAffaireDTO.f_u2LDocument+" as "+DocumentChiffreAffaireDTO.f_u2LDocument+","
					+" b."+DocumentChiffreAffaireDTO.f_qte2LDocument+" as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
					
					+" b."+DocumentChiffreAffaireDTO.f_lot+" as "+DocumentChiffreAffaireDTO.f_lot+","												
					+" b."+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+" "

					
					+" from ("+
					"select "+  

					 	"doc.code_document as "+DocumentChiffreAffaireDTO.f_codeDocument+", "
						+ "doc.date_document  as "+DocumentChiffreAffaireDTO.f_dateDocument+", "
						+ "doc.date_liv_document  as "+DocumentChiffreAffaireDTO.f_dateLivraison+", "
					+ "'"+TaBoncde.TYPE_DOC+"' as "+DocumentChiffreAffaireDTO.f_typeDoc+" ,"
					 //rajout article
					 +" art.code_article as "+DocumentChiffreAffaireDTO.f_codeArticle+", "
					 +" ldoc.lib_l_document as "+DocumentChiffreAffaireDTO.f_libellecArticle+", "
					 +" fam.code_famille as "+DocumentChiffreAffaireDTO.f_codeFamille+", "
					 +" fam.libc_famille as "+DocumentChiffreAffaireDTO.f_libcFamille+", "
					 +" lot.numlot as "+DocumentChiffreAffaireDTO.f_lot+", "
					 +" ldoc.qte_saisie_l_document as "+DocumentChiffreAffaireDTO.f_qteSaisieLDocument+", "
					 +" ldoc.u_saisie_l_document as "+DocumentChiffreAffaireDTO.f_uSaisieLDocument+", "
					 +" ldoc.qte_l_document as "+DocumentChiffreAffaireDTO.f_qteLDocument+", "
					 +" ldoc.u1_l_document as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
					 +" ldoc.qte2_l_document as "+DocumentChiffreAffaireDTO.f_qte2LDocument+", "
					 +" ldoc.u2_l_document as "+DocumentChiffreAffaireDTO.f_u2LDocument+", "
					 
					 +" doc.utilise_unite_saisie as "+DocumentChiffreAffaireDTO.f_utiliseUniteSaisie+", "
					 //rajout tiers
					 +"tiers.code_tiers as "+DocumentChiffreAffaireDTO.f_codeTiers+", "
					 +"ta_entreprise.nom_entreprise as "+DocumentChiffreAffaireDTO.f_nomEntreprise+", "
					 +"infos.nom_tiers as "+DocumentChiffreAffaireDTO.f_nomTiers+", "
					 +"infos.prenom_tiers as "+DocumentChiffreAffaireDTO.f_prenomTiers+", "
					 +"ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtHtCalc+", "
					 +"ldoc.mt_ttc_apr_rem_globale-ldoc.mt_ht_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTvaCalc+", "
					 +"ldoc.mt_ttc_apr_rem_globale as "+DocumentChiffreAffaireDTO.f_mtTtcCalc+" "
					
					 +" from ta_l_boncde ldoc "
					 +" join ta_boncde doc on doc.id_document=ldoc.id_document "
					 +" join ta_infos_boncde infos on infos.id_document=doc.id_document "
					 +" join ta_article art on art.id_article=ldoc.id_article "
					 +" left join ta_famille fam on fam.id_famille=art.id_famille "
					 +" join ta_tiers tiers on tiers.id_tiers=doc.id_tiers "
					 +" left join ta_entreprise on ta_entreprise.id_entreprise=tiers.id_entreprise "
					 +" left join ta_lot lot on lot.id_lot=ldoc.id_lot "
					 
					 +"where doc.date_liv_document between :dateDebut and :dateFin " + 
					 "and art.code_article like :codeArticle ) b";	 
		
		
		
		}
			
	
			
			public String getRequeteDocumentLieAFacture() {
				return getRequeteDocumentTransforme(prefixeDocument);
			};

			
			
	public AbstractDocumentDAO(){
		entity=newDocument();
		infosEntity=newInfosDocument();
		ligneEntity=newLigneDocument();
		
		nameEntity=entity.getClass().getName();
		
		nameEntitySQL = entity.getClass().getAnnotation(Table.class).name();
		nameLignesEntitySQL = ligneEntity.getClass().getAnnotation(Table.class).name();	
		nameInfosEntitySQL = infosEntity.getClass().getAnnotation(Table.class).name();				
		initialiseRequetes();
		initialiseRequetesAvecEtat();
	}
	
	
	/**
	 * Classe permettant d'obtenir le ca généré par les Prelevement Non transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des Prelevement Non transformés sur la période éclaté en fonction de la précision 
	 * Jour Mois Année
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			switch (precision) {
			case 0:
				query = entityManager.createQuery(SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME);
				
				break;

			case 1:
				query = entityManager.createQuery(SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME);
				
				break;
			case 2:
				query = entityManager.createQuery(SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	 /**
	 * Classe permettant d'obtenir la listes des Prelevement non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
	 * ne doivent pas dépasser par rapport à la date du jour 
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés à relancer
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,int precision, int deltaNbJours,String codeTiers) {
		Query query = null;
		try {
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		if(codeTiers==null)codeTiers="%";
		switch (precision) {
		case 0:
			query = entityManager.createQuery(SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
			
			break;

		case 1:
			query = entityManager.createQuery(SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
			
			break;
		case 2:
			query = entityManager.createQuery(SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
			
			break;

		default:
			break;
		}
		
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		for (Parameter<?> l : query.getParameters()) {
			if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
			if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
		}
		query.setParameter("codeTiers",codeTiers);
		List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//		List<DocumentChiffreAffaireDTO> l = query.getResultList();;
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}
	 /**
	 * Classe permettant d'obtenir la listes des Bons de livraison non transformés à relancer
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
	 * ne doivent pas dépasser par rapport à la date du jour 
	 * @return La requête renvoyée renvoi la liste des Bons de livraison non transformés à relancer
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut,
			Date dateFin, int deltaNbJours,String codeTiers) {
		logger.debug("getting ca des Bons de livraison non transfos à relancer");
		try {
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		if(codeTiers==null)codeTiers="%";
		Query query = entityManager.createQuery(SUM_CA_TOTAL_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		for (Parameter<?> l : query.getParameters()) {
			if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
			if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
		}
		query.setParameter("codeTiers",codeTiers);
		List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//		List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//		if (query.getFirstResult() == 0){
		if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
			l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
		}
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
	}
	}

	
	/**
	 * Classe permettant d'obtenir le ca généré par les Prelevement transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des Prelevement transformés sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE_TRANSFORME);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	/**
	 * Classe permettant d'obtenir le ca généré par les Prelevement transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des Prelevement transformés sur la période éclaté en fonction de la précision 
	 * Jour Mois Année
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			switch (precision) {
			case 0:
				query = entityManager.createQuery(SUM_CA_ANNEE_LIGTH_PERIODE_TRANSFORME);
				
				break;

			case 1:
				query = entityManager.createQuery(SUM_CA_MOIS_LIGTH_PERIODE_TRANSFORME);
				
				break;
			case 2:
				query = entityManager.createQuery(SUM_CA_JOUR_LIGTH_PERIODE_TRANSFORME);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	/**
	 * Classe permettant d'obtenir le ca généré par les Prelevement sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des Prelevement sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré  sur une période donnée par article
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO sur la période et par article
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	/******RAJOUT YANN ************************/
	/**
	 * Classe permettant d'obtenir le ca généré  sur une période donnée par tiers, par articles groupé par articles
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO sur la période et par article/tiers et groupé par articles
	 */
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle, String codeTiers) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(LIST_SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	/******RAJOUT YANN ************************/
	/**
	 * Classe permettant d'obtenir le ca généré  sur une période donnée par tiers, par articles groupé par Mois
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO sur la période et par article/tiers et groupé par Mois
	 */
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(LIST_SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_MOIS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	/**
	 * Méthode permettant d'obtenir une liste de sommes de lignes des docs transformés sur une période donnée par tiers, par articles groupé par Mois
	 * Cette méthode est notamment utilisé pour remplir les graphiques des onglets devis/commande/proforma du dash par article
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param codeArticle
	 * @param codeTiers
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO sur la période et par article/tiers et groupé par Mois
	 */
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTransformeDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_TRANSFORME);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	/**
	 * Méthode permettant d'obtenir une liste de sommes de lignes des docs non transformés sur une période donnée par tiers, par articles groupé par Mois
	 * Cette méthode est notamment utilisé pour remplir les graphiques des onglets devis/commande/proforma du dash par article
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param codeArticle
	 * @param codeTiers
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO sur la période et par article/tiers et groupé par Mois
	 */
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireNonTransformeDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_NON_TRANSFORME);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	/**
	 * Méthode permettant d'obtenir une liste de sommes de lignes des docs non transformés à relancer sur une période donnée par tiers, par articles groupé par Mois
	 * Cette méthode est notamment utilisé pour remplir les graphiques des onglets devis/commande/proforma du dash par article
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param codeArticle
	 * @param codeTiers
	 * @param deltaNbJours
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO sur la période et par article/tiers et groupé par Mois
	 */
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireNonTransformeARelancerDTOArticleMois(Date dateDebut,
			Date dateFin, int deltaNbJours,String codeArticle,String codeTiers) {
		try {
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		if(codeTiers==null)codeTiers="%";
		if(codeArticle==null)codeArticle="%";
		Query query = entityManager.createQuery(LIST_SUM_CA_MOIS_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_A_RELANCER);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		for (Parameter<?> l : query.getParameters()) {
			if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
			if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
		}
		query.setParameter("codeTiers",codeTiers);
		query.setParameter("codeArticle",codeArticle);
		List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//		List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//		if (query.getFirstResult() == 0){
		if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
			l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
		}
		logger.debug("get successful");
		return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
	}
	
	
	/******RAJOUT YANN ************************/
	/**
	 * Classe permettant d'obtenir le nombre de ligne d'article (COUNT) sur une période donnée par tiers, par articles groupé par Article
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO (avec le count, on ne prend que le 1er resultat(get(0)) sur la période et par article/tiers et groupé par Article
	 */
	public List<DocumentChiffreAffaireDTO> countChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle, String codeTiers) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(COUNT_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	/**
	 * Méthode permettant d'obtenir le nombre de familles d'articles (COUNT) sur une période donnée pour un tiers, ou un articles 
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO (avec le count, on ne prend que le 1er resultat(get(0)) sur la période et par article/tiers 
	 */
	public List<DocumentChiffreAffaireDTO> countFamilleArticleDTOArticle(Date dateDebut, Date dateFin, String codeArticle, String codeTiers) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(COUNT_FAMILLE_ARTICLE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
		
			
	}
	
	
	/******RAJOUT YANN ************************/
	/**
	 * Classe permettant d'obtenir la somme totale (CA) des lignes d'articles (SUM) sur une période donnée.
	 * Le résultat doit être le même que la requete qui intérroge les totaux dans l'entête facture.
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO (avec le SUM, on ne prend que le 1er resultat(get(0)) sur la période
	 */
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin) {
//		try {
//			Query query = null;
////			if(codeArticle==null)codeArticle="%";
////			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createQuery(SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
////			query.setParameter("codeArticle",codeArticle);
////			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return sumChiffreAffaireTotalDTOArticle( dateDebut,  dateFin,  null);
			
	}
	/*****RAJOUT POUR DASH PAR ARTICLE*******/
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticle(Date dateDebut, Date dateFin, String codeArticle) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
//			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
//			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
		/*****RAJOUT POUR DASH PAR ARTICLE MOINS AVOIR*******/
		public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle) {
			try {
				Query query = null;
				if(codeArticle==null)codeArticle="%";
//				if(codeTiers==null)codeTiers="%";
				query = entityManager.createNativeQuery(SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_MOINS_AVOIR);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeArticle",codeArticle);
//				query.setParameter("codeTiers",codeTiers);
				addScalarDocumentChiffreAffaireDTO(query);
				//List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				List<DocumentChiffreAffaireDTO> l = query.getResultList();
//				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
	}
		/**
		 * Requête permettant d'obtenir la somme totale (CA) des lignes d'articles (SUM) sur une période donnée sur les documents non transformé.
		 * Cette requête est utiliser dans l'onglet Devis du dash par article.
		 * @param debut date de début période
		 * @param fin date de fin période
		 * @param codeArticle
		 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO (avec le SUM, on ne prend que le 1er resultat(get(0)) sur la période
		 */
		public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleNonTransforme(Date dateDebut, Date dateFin, String codeArticle) {
			try {
				Query query = null;
				if(codeArticle==null)codeArticle="%";
//				if(codeTiers==null)codeTiers="%";
				query = entityManager.createQuery(SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_NON_TRANSFORME);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeArticle",codeArticle);
//				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		/**
		 * Requête permettant d'obtenir la somme totale (CA) des lignes d'articles (SUM) sur une période donnée sur les documents transformé.
		 * Cette requête est utiliser dans l'onglet Devis du dash par article.
		 * @param debut date de début période
		 * @param fin date de fin période
		 * @param codeArticle
		 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO (avec le SUM, on ne prend que le 1er resultat(get(0)) sur la période
		 */
		public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleTransforme(Date dateDebut, Date dateFin, String codeArticle) {
			try {
				Query query = null;
				if(codeArticle==null)codeArticle="%";
//				if(codeTiers==null)codeTiers="%";
				query = entityManager.createQuery(SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_TRANSFORME);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeArticle",codeArticle);
//				query.setParameter("codeTiers",codeTiers);
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		/**
		 * Requête permettant d'obtenir la somme totale (CA) des lignes d'articles (SUM) sur une période donnée sur les documents a relancer.
		 * Cette requête est utiliser dans l'onglet Devis du dash par article.
		 * @param debut date de début période
		 * @param fin date de fin période
		 * @param codeArticle
		 * @param deltaNbJours nombre de jours
		 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO (avec le SUM, on ne prend que le 1er resultat(get(0)) sur la période
		 */
		public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleARelancer(Date dateDebut,
				Date dateFin, int deltaNbJours,String codeArticle) {
			try {
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			if(codeArticle==null)codeArticle="%";
//			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createQuery(SUM_CA_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_A_RELANCER);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeArticle",codeArticle);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			if (query.getFirstResult() == 0){
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
		}
		
				
		
		
		
//		public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
//				int precision, int deltaNbJours,String codeTiers) {
//			// TODO Auto-generated method stub
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			try {
//				Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//				Date dateJour = LibDate.dateDuJour();
//				switch (precision) {
//				case 0:
//					query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER);
//					
//					break;
//
//				case 1:
//					query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER);
//				
//					break;
//				case 2:
//					query = entityManager.createNativeQuery(SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER);
//					
//					break;
//
//				default:
//					break;
//				}
//				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//				query.setParameter("dateFin", dateFin, TemporalType.DATE);
//				for (Parameter<?> l : query.getParameters()) {
//					if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
//					if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
//				}
//				query.setParameter("codeTiers",codeTiers);
//				addScalarDocumentChiffreAffaireDTO(query);
//				List<DocumentChiffreAffaireDTO> l = query.getResultList();
//
//				
//				logger.debug("get successful");
//				return l;
//
//			} catch (RuntimeException re) {
//				logger.error("get failed", re);
//				throw re;
//			}
////			return null;	
//		}
	
	
	/******RAJOUT YANN ligne articles pour un tiers ************************/
		public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUnite(Date dateDebut, Date dateFin, String codeArticle, String codeUnite, String codeTiers, String orderBy) {
			return listLigneArticleDTOTiersUniteFamille(dateDebut, dateFin, codeArticle, codeUnite, "%", codeTiers, orderBy);
		}
		
		/**
		 * Méthode permettant d'obtenir une liste de ligne d'article (Détail)  sur une période donnée pour un tiers, ou pour un article, ou pour une famille, et par unité, trié par defaut par TIERS
		 * On peut choisir le trie
		 * @param debut date de début période
		 * @param fin date de fin période
		 * @param String codeArticle
		 * @param String codeUnite
		 * @param String codeTiers
		 * @param String codeFamille
		 * @param String orderBy
		 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture, pour un tiers/article/famille et unité, trié par defaut par Tiers
		 */
		public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUniteFamille(Date dateDebut, Date dateFin, String codeArticle, String codeUnite,String codeFamille, String codeTiers, String orderBy) {
			try {
				Query query = null;
				if(codeArticle==null)codeArticle="%";
				if(codeTiers==null)codeTiers="%";
				if(codeFamille==null)codeFamille="%";
				String sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_UNITE;

				
				if(codeUnite==null) {
					
					sql = sql.replace("ldoc.u1LDocument like :codeUnite", "ldoc.u1LDocument IS NULL");
				}else if(codeUnite.isEmpty()) {
					sql = sql.replace("ldoc.u1LDocument like :codeUnite", " (ldoc.u1LDocument like :codeUnite OR ldoc.u1LDocument IS NULL)");
				}
				
				
				
				
				if(orderBy == null) {//trie par defaut par tiers
					sql +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
				}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT) {
					sql +=  " order by doc.dateDocument ";
				}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE) {
					sql +=  " order by art.codeArticle ";
				}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE) {
					sql +=  " order by  fam.codeFamille ";
				}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_LIBL_FAMILLE_ARTICLE) {
					sql +=  " order by  fam.libcFamille ";
				}else {// trie par defaut par tiers
					sql +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
				}
					
				
				query = entityManager.createQuery(sql);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeArticle",codeArticle);
				query.setParameter("codeTiers",codeTiers);
				query.setParameter("codeFamille",codeFamille);
				if(codeUnite != null) {
					query.setParameter("codeUnite",codeUnite);
				}
				
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//				}
				logger.debug("get successful");
				return l;

			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle, String codeTiers) {
		return listLigneArticleDTOTiers( dateDebut,  dateFin,  codeArticle,  codeTiers, null);
	}
	/**
	 * Méthode permettant d'obtenir une liste de ligne d'article (Détail)  sur une période donnée pour un tiers, ou pour un article trié par dafut par TIERS
	 * On peut choisir le trie
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @param String orderBy
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture, pour un tiers/article et trié par defaut par Tiers
	 */
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String orderBy) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			String sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS;
			if(orderBy == null) {//trie par defaut par tiers
				sql +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT) {
				sql +=  " order by doc.dateDocument ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE) {
				sql +=  " order by art.codeArticle ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE) {
				sql +=  " order by  fam.codeFamille ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_LIBL_FAMILLE_ARTICLE) {
				sql +=  " order by  fam.libcFamille ";
			}else {// trie par defaut par tiers
				sql +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
			}
				
			
			query = entityManager.createQuery(sql);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	/**
	 * Classe permettant d'obtenir une liste de ligne d'article  sur une période donnée par tiers, par articles trié par TIERS et par type etat doc (transformé, non transformé, a relancer)
	 * Notamment utilisée dans le dashboard facture onglet détail 
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @param String typeEtatDoc  
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture, par tiers/article et groupé par Tiers
	 */
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersParTypeEtatDoc(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String typeEtatDoc, int deltaNbJours) {
		try {
			String queryConst ="LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS";
			queryConst +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
			if(typeEtatDoc == null) {
			 return	listLigneArticleDTOTiers( dateDebut,  dateFin,  codeArticle,  codeTiers);
//			}else if(typeEtatDoc.equals(TaEtat.ETAT_TRANSFORME)){	
			// ISA  le 08/08/2019 suite à prise en charge des différents états
			}else if(typeEtatDoc.equals(TaEtat.TERMINE_TOTALEMENT_TRANSFORME)){
				queryConst = LIST_LIGNE_ARTICLE_TRANSFO_PERIODE_PAR_TIERS;
//			}else if(typeEtatDoc.equals(TaEtat.ETAT_NON_TRANSFORME)){
				// ISA  le 08/08/2019 suite à prise en charge des différents états
			}else if(typeEtatDoc.equals(TaEtat.ENCOURS)){
				queryConst = LIST_LIGNE_ARTICLE_NON_TRANSFO_PERIODE_PAR_TIERS;
			}else if(typeEtatDoc.equals(TaEtat.ETAT_NON_TRANSFORME_A_RELANCER)){
				queryConst = LIST_LIGNE_ARTICLE_A_RELANCER_PERIODE_PAR_TIERS;
				if(deltaNbJours == 0) {
					deltaNbJours = 15;
				}
				Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
				Date dateJour = LibDate.dateDuJour();
				Query query = null;
				if(codeArticle==null)codeArticle="%";
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createQuery(queryConst);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeArticle",codeArticle);
				query.setParameter("codeTiers",codeTiers);
				for (Parameter<?> l : query.getParameters()) {
					if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
					if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
				}
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//				}
				logger.debug("get successful");
				return l;
			}
			
			
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(queryConst);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle){
		return listLigneArticlePeriodeParArticleAvoirFactureDTO(dateDebut, dateFin, codeArticle, null);
	}
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese) {
		return listLigneArticlePeriodeParArticleAvoirFactureDTO( dateDebut,  dateFin,  codeArticle,  synthese,  null);
	}
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleParUniteAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeUnite, String orderBy) {
		try {
			Query query = null;
			//String codeTiers = "%";
			String sql = "";
			if(codeArticle==null)codeArticle="%";
			
			//if(codeTiers==null)codeTiers="%";
			// si on veut le detail (lignes de docs facture et avoirs)
				sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_PAR_UNITE;
				
				if(codeUnite==null) {
					
					sql = sql.replace("ldoc.u1_l_document like :codeUnite", "u1_l_document IS NULL");
					sql = sql.replace("al.u1_l_document like :codeUnite", "u1_l_document IS NULL");
				}else if(codeUnite.isEmpty()) {
					sql = sql.replace("ldoc.u1_l_document like :codeUnite", " (ldoc.u1_l_document like :codeUnite OR u1_l_document IS NULL)");
					sql = sql.replace("al.u1_l_document like :codeUnite", " (al.u1_l_document like :codeUnite OR u1_l_document IS NULL)");
				}
				 
			
			if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeArticle+" ";
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}else {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}
			
			query = entityManager.createNativeQuery(sql);
			
			//test
//			query = entityManager.createNativeQuery(LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF);
			
			
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			
			if(codeUnite!=null) {
				query.setParameter("codeUnite",codeUnite);
			}
			
//			query.setParameter("codeTiers",codeTiers);
			
			// si on veut le detail (lignes de docs facture et avoirs)
			addScalarDocumentChiffreAffaireDTODetailAvecQte2(query);
			
			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	//Méthode pour l'onglet "Factures et avoirs" d'article.xhtml et aussi pour le dashboard article onglet "tous"
	/**
	 * Méthode permettant d'obtenir une liste de ligne d'article des factures et des avoirs  sur une période donnée  par articles ordonnée par date document
	 * On peut choisir d'avoir le détail ou la synthese
	 * On peut choisir le trie (orderBy)
	 * @param Boolean synthese
	 * @param String orderBy
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture et avoirs, par article et ordonnée par date document
	 */
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, Boolean synthese, String orderBy) {
		try {
			Query query = null;
			if(codeTiers==null) codeTiers = "%";
			String sql = "";
			if(codeArticle==null)codeArticle="%";
			//if(codeTiers==null)codeTiers="%";
			if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs facture et avoirs
				sql = LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE_GROUP_BY_TIERS;
			}else {// si on veut le detail (lignes de docs facture et avoirs)
				sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE; 
				 
			}
			if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeTiers+" ";
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}else {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}
			
			query = entityManager.createNativeQuery(sql);
			
			//test
//			query = entityManager.createNativeQuery(LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF);
			
			
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			if(synthese == null || synthese == false)
				query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			
			if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs facture et avoirs
				addScalarDocumentChiffreAffaireDTOGroupByTiers(query);
			}else if(synthese != null && synthese == false){// si on veut le detail (lignes de docs facture et avoirs)
//				addScalarDocumentChiffreAffaireDTOGroupByArticle(query);
				addScalarDocumentChiffreAffaireDTODetailAvecQte2(query);
			}else {
				addScalarDocumentChiffreAffaireDTOcodeDocDateDoc(query);
			}
			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}	
		
	}
	
	
	//Méthode pour l'onglet "Factures et avoirs" d'article.xhtml et aussi pour le dashboard article onglet "tous"
	/**
	 * Méthode permettant d'obtenir une liste de ligne d'article des factures et des avoirs  sur une période donnée  par articles ordonnée par date document
	 * On peut choisir d'avoir le détail ou la synthese
	 * On peut choisir le trie (orderBy)
	 * @param Boolean synthese
	 * @param String orderBy
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture et avoirs, par article et ordonnée par date document
	 */
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersEtArticlesAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers, Boolean synthese, String orderBy) {
		try {
			Query query = null;
			if(codeTiers==null) codeTiers = "%";
			String sql = "";
			if(codeArticle==null)codeArticle="%";
			//if(codeTiers==null)codeTiers="%";
			if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs facture et avoirs
				sql = LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_TIERS_ARTICLE;
			}else {// si on veut le detail (lignes de docs facture et avoirs)
				sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_PAR_ARTICLE_AVOIR_FACTURE; 
				 
			}
			if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS_ARTICLE)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeTiers+",b."+DocumentChiffreAffaireDTO.f_codeArticle+" ";
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}else {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}
			
			query = entityManager.createNativeQuery(sql);
			
			//test
//			query = entityManager.createNativeQuery(LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF);
			
			
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			
			if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs facture et avoirs
				addScalarDocumentChiffreAffaireDTOGroupByTiersEtArticle(query);
			}else if(synthese != null && synthese == false){// si on veut le detail (lignes de docs facture et avoirs)
//				addScalarDocumentChiffreAffaireDTOGroupByArticle(query);
				addScalarDocumentChiffreAffaireDTODetailAvecQte2(query);
			}else {
				addScalarDocumentChiffreAffaireDTOcodeDocDateDoc(query);
			}
			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}	
		
	}

	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParTiersParUniteAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle,String codeTiers,String codeUnite, String orderBy) {
		try {
			Query query = null;
			if(codeTiers==null) codeTiers = "%";
			String sql = "";
			if(codeArticle==null)codeArticle="%";
			
			// si on veut le detail (lignes de docs facture et avoirs)
				sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVOIR_FACTURE_PAR_UNITE;
				
				if(codeUnite==null) {
					
					sql = sql.replace("ldoc.u1_l_document like :codeUnite", "u1_l_document IS NULL");
					sql = sql.replace("al.u1_l_document like :codeUnite", "u1_l_document IS NULL");
				}else if(codeUnite.isEmpty()) {
					sql = sql.replace("ldoc.u1_l_document like :codeUnite", " (ldoc.u1_l_document like :codeUnite OR u1_l_document IS NULL)");
					sql = sql.replace("al.u1_l_document like :codeUnite", " (al.u1_l_document like :codeUnite OR u1_l_document IS NULL)");
				}
				 
			
				if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_TIERS)) {
					sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeTiers+",b."+DocumentChiffreAffaireDTO.f_codeArticle+" ";
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}else {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}
			
			query = entityManager.createNativeQuery(sql);
			
			//test
//			query = entityManager.createNativeQuery(LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF);
			
			
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			
			if(codeUnite!=null) {
				query.setParameter("codeUnite",codeUnite);
			}
			
//			query.setParameter("codeTiers",codeTiers);
			
			// si on veut le detail (lignes de docs facture et avoirs)
			addScalarDocumentChiffreAffaireDTODetailAvecQte2(query);
			
			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	//Méthode pour l'onglet "Factures et avoirs" d'article.xhtml et aussi pour le dashboard article onglet "tous"
	/**
	 * Méthode permettant d'obtenir une liste de ligne d'article des factures et des avoirs  sur une période donnée  par articles ordonnée par date document
	 * On peut choisir d'avoir le détail ou la synthese
	 * On peut choisir le trie (orderBy)
	 * @param Boolean synthese
	 * @param String orderBy
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture et avoirs, par article et ordonnée par date document
	 */
//		public List<DocumentDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle){
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy) {
		try {
			Query query = null;
			String codeTiers = "%";
			String sql = "";
			if(codeArticle==null)codeArticle="%";
			//if(codeTiers==null)codeTiers="%";
			if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs facture et avoirs
				sql = LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_ARTICLE;
			}else {// si on veut le detail (lignes de docs facture et avoirs)
				sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE;
				 
			}
			if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeArticle+" ";
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}else {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}
			
			query = entityManager.createNativeQuery(sql);
			
			//test
//			query = entityManager.createNativeQuery(LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF);
			
			
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
//			query.setParameter("codeTiers",codeTiers);
			
			if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs facture et avoirs
				addScalarDocumentChiffreAffaireDTOGroupByArticle(query);
			}else if(synthese != null && synthese == false){// si on veut le detail (lignes de docs facture et avoirs)
//				addScalarDocumentChiffreAffaireDTOGroupByArticle(query);
				addScalarDocumentChiffreAffaireDTODetailAvecQte2(query);
			}else {
				addScalarDocumentChiffreAffaireDTOcodeDocDateDoc(query);
			}
			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	//Méthode pour le dashboard article onglet "tous", groupé par famille
	/**
	 * Méthode permettant d'obtenir une liste de ligne d'article des factures et des avoirs  sur une période donnée  par famille d'articles ordonnée par ce que l'on souhaite
	 * On peut choisir le trie (orderBy)
	 * @param String orderBy
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture et avoirs, par famille article et ordonnée par ce que l'on souhaite
	 */
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeParFamilleArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeArticle, String orderBy){
		try {
			Query query = null;
			String codeTiers = "%";
			String sql = "";
			if(codeArticle==null)codeArticle="%";
			//if(codeTiers==null)codeTiers="%";
			sql = LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_GROUP_BY_FAMILLE_ARTICLE;

			if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeArticle+" ";
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_LIBL_FAMILLE_ARTICLE)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_libcFamille+" ";
			}else {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeFamille+" ";
			}
			query = entityManager.createNativeQuery(sql);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
//			query.setParameter("codeTiers",codeTiers);
			
			addScalarDocumentChiffreAffaireDTOGroupByFamilleArticle(query);

			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<DocumentChiffreAffaireDTO>  detailParFamilleArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeFamille, String orderBy){
		return detailParFamilleUniteArticleAvoirFactureDTO(dateDebut, dateFin, codeFamille, "%", orderBy);
	}

	public List<DocumentChiffreAffaireDTO>  detailParFamilleUniteArticleAvoirFactureDTO(Date dateDebut, Date dateFin, String codeFamille, String codeUnite, String orderBy){
		
		try {
			Query query = null;
			String codeTiers = "%";
			String sql = "";
			if(codeFamille==null)codeFamille="%";
			//if(codeTiers==null)codeTiers="%";
			sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_AVOIR_FACTURE_WHERE_FAMILLE_LIKE;
			
			if(codeUnite==null) {
				
				sql = sql.replace("ldoc.u1_l_document like :codeUnite", "ldoc.u1_l_document IS NULL");
				sql = sql.replace("al.u1_l_document like :codeUnite", "al.u1_l_document IS NULL");
			}else if(codeUnite.isEmpty()) {
				sql = sql.replace("ldoc.u1_l_document like :codeUnite", " (ldoc.u1_l_document like :codeUnite OR ldoc.u1_l_document IS NULL)");
				sql = sql.replace("al.u1_l_document like :codeUnite", " (al.u1_l_document like :codeUnite OR al.u1_l_document IS NULL)");
			}

			if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeArticle+" ";
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_LIBL_FAMILLE_ARTICLE)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_libcFamille+" ";
				
			}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}else {
				sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+" ";
			}
			query = entityManager.createNativeQuery(sql);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeFamille",codeFamille);
			if(codeUnite!=null) {
				query.setParameter("codeUnite",codeUnite);
			}
			
//			query.setParameter("codeTiers",codeTiers);
			
			query.unwrap(SQLQuery.class)
				.addScalar(DocumentChiffreAffaireDTO.f_codeDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_dateDocument,DateType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libcFamille, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_libellecArticle, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_typeDoc,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_codeTiers,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_prenomTiers,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_nomTiers,StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u1LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qteLDocument, BigDecimalType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_u2LDocument, StringType.INSTANCE)
				.addScalar(DocumentChiffreAffaireDTO.f_qte2LDocument, BigDecimalType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
			

			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	//Méthode pour l'onglet "Factures et avoirs" d'article.xhtml, pour le graphique par mois
	/**
	 * Méthode permettant d'obtenir une liste de sommes  des lignes de factures et des avoirs  sur une période donnée pour un article groupé par mois
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux sommes  des lignes de factures et des avoirs  sur une période donnée pour un article groupé par mois
	 */	
	public List<DocumentChiffreAffaireDTO> listSumCAMoisTotalLigneArticlePeriodeParArticleMoinsAvoir(Date dateDebut, Date dateFin, String codeArticle) {
		;
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			//if(codeTiers==null)codeTiers="%";
			query = entityManager.createNativeQuery(SUM_CA_MOIS_TOTAL_LIGNE_ARTICLE_PERIODE_PAR_ARTICLE_MOINS_AVOIR);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
//			query.setParameter("codeTiers","3P");
			addScalarDocumentChiffreAffaireDTO(query);
			//a remettre la ligne ci-dessous 
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();

//			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	
	
	
	
	
	//Méthode pour l'onglet "tiers ayant acheter cet article" d'article.xhtml
	/**
	 * Méthode permettant d'obtenir un nombre de d'article de tiers ayant acheter un article  sur une période donnée
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @return long La requête renvoyée renvoi un long
	 */
	public long countTiersAyantAcheterArticleSurPeriode(Date dateDebut, Date dateFin, String codeArticle) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			//if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(COUNT_TIER_AYANT_ACHETER_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			//query.setParameter("codeTiers",codeTiers);
//			addScalarDocumentChiffreAffaireDTO(query);
			//a remettre la ligne ci-dessous 
			long i = (long) query.getSingleResult();


			logger.debug("get successful");
			return i;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	//Méthode pour la liste de tiers dans l'onglet "tiers ayant acheter cet article" d'article.xhtml
	 /** Méthode permettant d'obtenir une liste de tiers sur une période donnée ayant acheter cet article
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO avec le Tiers et d'autre informations sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listTiersAyantAcheterArticleDTO(Date dateDebut, Date dateFin, String codeArticle) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			query = entityManager.createQuery(LIST_TIER_AYANT_ACHETER_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	 /** Méthode permettant d'obtenir une liste de tous les documents sur une période pour un tiers(ou tous) et pour un article (ou tous)
	 * Cette méthode est utilisé dans le dash par article sur les onglets Devis/Commandes/Proforma
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @return List<DocumentDTO> La méthode renvoie une liste de DocumentDTO 
	 */
	public List<DocumentDTO> findAllLigneDTOPeriode(Date dateDebut, Date dateFin,String codeTiers, String codeArticle) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(FIND_ALL_LIGHT_LIGNE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("codeArticle",codeArticle);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	 /** Méthode permettant d'obtenir une liste des  documents transfomés sur une période pour un tiers(ou tous) et pour un article (ou tous)
	 * Cette méthode est utilisé dans le dash par article sur les onglets Devis/Commandes/Proforma
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @return List<DocumentDTO> La méthode renvoie une liste de DocumentDTO 
	 */
	public List<DocumentDTO> findDocumentTransfosLigneDTO(Date dateDebut, Date dateFin,String codeTiers, String codeArticle) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(FIND_TRANSFORME_LIGHT_LIGNE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("codeArticle",codeArticle);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	 /** Méthode permettant d'obtenir une liste des  documents non transfomés sur une période pour un tiers(ou tous) et pour un article (ou tous)
	 * Cette méthode est utilisé dans le dash par article sur les onglets Devis/Commandes/Proforma
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @return List<DocumentDTO> La méthode renvoie une liste de DocumentDTO 
	 */
	public List<DocumentDTO> findDocumentNonTransfosLigneDTO(Date dateDebut, Date dateFin,String codeTiers, String codeArticle) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(FIND_NON_TRANSFORME_LIGHT_LIGNE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("codeArticle",codeArticle);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	 /** Méthode permettant d'obtenir une liste des  documents non transfomés à relancer sur une période pour un tiers(ou tous) et pour un article (ou tous)
	 * Cette méthode est utilisé dans le dash par article sur les onglets Devis/Commandes/Proforma
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @param int deltaNbJours
	 * @return List<DocumentDTO> La méthode renvoie une liste de DocumentDTO 
	 */
	public List<DocumentDTO> findDocumentNonTransfosARelancerLigneDTO(Date dateDebut, Date dateFin,int deltaNbJours, String codeTiers, String codeArticle) {
		try {
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(FIND_NON_TRANSFORME_ARELANCER_LIGHT_LIGNE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("codeArticle",codeArticle);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	
	
	
	
	
	
	
	
	
	/**-------------------------------------------------------------------------------------
	 * Classe permettant d'obtenir le ca généré par les Bons de livraison sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des bonLiv sur la période éclaté en fonction de la précision 
	 * Jour Mois Année
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			switch (precision) {
			case 0:
				query = entityManager.createQuery(SUM_CA_ANNEE_LIGTH_PERIODE);
				
				break;

			case 1:
				query = entityManager.createQuery(SUM_CA_MOIS_LIGTH_PERIODE);
				
				break;
			case 2:
				query = entityManager.createQuery(SUM_CA_JOUR_LIGTH_PERIODE);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les Prelevement non transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des Prelevement non transformés sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE_NON_TRANSFORME);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	
	/**
	 * Permet d'obtenir le ca généré par les bons de livraison non transformés à relancer sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total non transformés à relancer directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}	
	
	/**
	 * Permet d'obtenir le ca généré par les bons de livraison non transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total NON Transformé directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}
	
	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}
	
	/**
	 * Permet d'obtenir le ca généré par les bons de livraison transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total Transformé directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}

	
	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			//par familleArticle ou tauxTva ou typePaiement ou vendeur
			infosCaTotal = listeChiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin,codeTiers,typeRegroupement,valeurRegroupement,regrouper).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}
	
	
	 /**
		 * Méthode permettant d'obtenir le nombre de documents non transformé selon le codeTiers et le codeArticle
		 * Notamment utilisé dans le dash par article, onglet devis, nb devis non transformées
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param codeTiers
		 * @return La requête renvoyée renvoi le nombre total de documents non transformé dans la période
		 */
		//RAJOUT YANN
		public List<DocumentChiffreAffaireDTO> countDocumentAvecEtat(Date debut, Date fin, String codeTiers , String codeArticle, String etat) {
			logger.debug("getting doc dans periode");
//			List<DocumentChiffreAffaireDTO> result =null;
			
			if(codeTiers==null)codeTiers="%";
			if(codeArticle==null)codeArticle="%";
			try {
				String requete = "";

				Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), 15, 0, 0);
				Date dateJour = LibDate.dateDuJour();

				
				requete = "SELECT "
					+" count(distinct doc) as count,et.identifiant  as identifiant,case  when "+getRequeteARelancerJPQL().replaceFirst("and", "")+" then true else false   END as relancer"
					+" FROM "+nameEntity+" doc " 
					+" join doc.lignes ldoc "
					+" join ldoc.taArticle art "
					+" join doc.taREtat re join re.taEtat et "
					+" where doc.dateDocument between :datedeb and :dateFin and doc.taTiers.codeTiers like :codeTiers and et.identifiant like :etat  and art.codeArticle like :codeArticle"
					+ " group by 2,3";
				requete=requete.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)");
				Query query = entityManager.createQuery(requete);
				query.setParameter("datedeb", debut);
				query.setParameter("dateFin", fin);
				query.setParameter("codeTiers", codeTiers);
				query.setParameter("codeArticle", codeArticle);
				query.setParameter("etat", etat);
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(debut);				
				}
				return l;
				
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
	
	 /**
	 * Méthode permettant d'obtenir les documents non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param codeTiers
	 * @return La requête renvoyée renvoi le nombre total de documents dans la période
	 */
	public long countDocument(Date debut, Date fin,String codeTiers) {
		logger.debug("getting doc dans periode");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";

			requete = "SELECT "
				+" count(doc)"
				+" FROM "+nameEntity+" doc " 
				+" where doc.dateDocument between :datedeb and :dateFin and doc.taTiers.codeTiers like :codeTiers ";
			Query query = entityManager.createQuery(requete);
			query.setParameter("datedeb", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers", codeTiers);
			Long nbBonliv = (Long)query.getSingleResult();
			result = nbBonliv;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	 /**
		 * Méthode permettant d'obtenir le nombre de documents selon le codeTiers et le codeArticle
		 * Notamment utilisé dans le dash par article, onglet devis, nb devis réalisés
		 * @param debut date de début des données
		 * @param fin date de fin des données
		 * @param codeTiers
		 * @return La requête renvoyée renvoi le nombre total de documents dans la période
		 */
		//RAJOUT YANN
		public long countDocument(Date debut, Date fin, String codeTiers , String codeArticle) {
			logger.debug("getting doc dans periode");
			Long result = (long) 0;
			
			if(codeTiers==null)codeTiers="%";
			if(codeArticle==null)codeArticle="%";
			try {
				String requete = "";

				requete = "SELECT "
					+" count(distinct doc)"
					+" FROM "+nameEntity+" doc " 
					+" join doc.lignes ldoc "
					+" join ldoc.taArticle art "
					+" where doc.dateDocument between :datedeb and :dateFin and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle";
				Query query = entityManager.createQuery(requete);
				query.setParameter("datedeb", debut);
				query.setParameter("dateFin", fin);
				query.setParameter("codeTiers", codeTiers);
				query.setParameter("codeArticle", codeArticle);
				Long nbBonliv = (Long)query.getSingleResult();
				result = nbBonliv;
				return result;
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
		 /**
			 * Méthode permettant d'obtenir le nombre de documents non transformé selon le codeTiers et le codeArticle
			 * Notamment utilisé dans le dash par article, onglet devis, nb devis non transformées
			 * @param debut date de début des données
			 * @param fin date de fin des données
			 * @param codeTiers
			 * @return La requête renvoyée renvoi le nombre total de documents non transformé dans la période
			 */
			//RAJOUT YANN
			public long countDocumentNonTransforme(Date debut, Date fin, String codeTiers , String codeArticle) {
				logger.debug("getting doc dans periode");
				Long result = (long) 0;
				
				if(codeTiers==null)codeTiers="%";
				if(codeArticle==null)codeArticle="%";
				try {
					String requete = "";

					requete = "SELECT "
						+" count(distinct doc)"
						+" FROM "+nameEntity+" doc " 
						+" join doc.lignes ldoc "
						+" join ldoc.taArticle art "
						+" where doc.dateDocument between :datedeb and :dateFin and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle"
						+ " and doc.taEtat is null "				
						+" and not exists ("+getRequeteDocumentTransforme("doc")+")";
					Query query = entityManager.createQuery(requete);
					query.setParameter("datedeb", debut);
					query.setParameter("dateFin", fin);
					query.setParameter("codeTiers", codeTiers);
					query.setParameter("codeArticle", codeArticle);
					Long nbBonliv = (Long)query.getSingleResult();
					result = nbBonliv;
					return result;
				} catch (RuntimeException re) {
					logger.error("get failed", re);
					throw re;
				}
			}
			 /**
				 * Méthode permettant d'obtenir le nombre de documents transformé selon le codeTiers et le codeArticle
				 * Notamment utilisé dans le dash par article, onglet devis, nb devis non transformées
				 * @param debut date de début des données
				 * @param fin date de fin des données
				 * @param codeTiers
				 * @return La requête renvoyée renvoi le nombre total de document transformé dans la période
				 */
				//RAJOUT YANN
				public long countDocumentTransforme(Date debut, Date fin, String codeTiers , String codeArticle) {
					logger.debug("getting doc dans periode");
					Long result = (long) 0;
					
					if(codeTiers==null)codeTiers="%";
					if(codeArticle==null)codeArticle="%";
					try {
						String requete = "";

						requete = "SELECT "
							+" count(distinct doc)"
							+" FROM "+nameEntity+" doc " 
							+" join doc.lignes ldoc "
							+" join ldoc.taArticle art "
							+" where doc.dateDocument between :datedeb and :dateFin and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle"
							+ " and doc.taEtat is null "				
							+" and exists ("+getRequeteDocumentTransforme("doc")+")";
						Query query = entityManager.createQuery(requete);
						query.setParameter("datedeb", debut);
						query.setParameter("dateFin", fin);
						query.setParameter("codeTiers", codeTiers);
						query.setParameter("codeArticle", codeArticle);
						Long nbBonliv = (Long)query.getSingleResult();
						result = nbBonliv;
						return result;
					} catch (RuntimeException re) {
						logger.error("get failed", re);
						throw re;
					}
				}
				/**
				 * Méthode permettant d'obtenir le nombre de documents transformé selon le codeTiers et le codeArticle
				 * Notamment utilisé dans le dash par article, onglet devis, nb devis non transformées
				 * @param debut date de début des données
				 * @param fin date de fin des données
				 * @param codeTiers
				 * @return La requête renvoyée renvoi le nombre total de document transformé dans la période
				 */
				//RAJOUT YANN
				public long countDocumentNonTransformeARelancer(Date debut, Date fin,int deltaNbJours, String codeTiers , String codeArticle) {
					logger.debug("getting doc dans periode");
					Long result = (long) 0;
					Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
					Date dateJour = LibDate.dateDuJour();
					
					if(codeTiers==null)codeTiers="%";
					if(codeArticle==null)codeArticle="%";
					try {
						String requete = "";

						requete = "SELECT "
							+" count(distinct doc)"
							+" FROM "+nameEntity+" doc " 
							+" join doc.lignes ldoc "
							+" join ldoc.taArticle art "
							+" where doc.dateDocument between :datedeb and :dateFin and doc.taTiers.codeTiers like :codeTiers and art.codeArticle like :codeArticle"
							+ " and doc.taEtat is null "+getRequeteARelancerJPQL()				
							+" and not exists ("+getRequeteDocumentTransforme("doc")+")";
						Query query = entityManager.createQuery(requete);
						query.setParameter("datedeb", debut);
						query.setParameter("dateFin", fin);
						for (Parameter<?> l : query.getParameters()) {
							if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
							if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
						}
						query.setParameter("codeTiers", codeTiers);
						query.setParameter("codeArticle", codeArticle);
						Long nbBonliv = (Long)query.getSingleResult();
						result = nbBonliv;
						return result;
					} catch (RuntimeException re) {
						logger.error("get failed", re);
						throw re;
					}
				}
	
	/**
	 * Classe permettant d'obtenir les Prelevement non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de Prelevement non transformés
	 */
	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre doc non transfos");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";

			requete = "SELECT "
				+" count(doc)"
				+" FROM "+nameEntity+" doc " 
				+" where doc.dateDocument between :datedeb and :dateFin and doc.taTiers.codeTiers like :codeTiers"
				+ " and doc.taEtat is null "				
				+" and not exists ("+getRequeteDocumentTransforme("doc")+")";
//						+" order by doc.mtHtCalc DESC";;
			Query query = entityManager.createQuery(requete);
			query.setParameter("datedeb", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers", codeTiers);
			Long nbBonlivNonTranforme = (Long)query.getSingleResult();
			result = nbBonlivNonTranforme;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	/**
	 * Classe permettant d'obtenir les Prelevement non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de Prelevement non transformés à relancer
	 */
	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
		logger.debug("getting nombre doc non transfos à relancer");
		Long result = (long) 0;
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";

			requete = "SELECT "
				+" count(doc)"
				+" FROM "+nameEntity+" doc " 
				+" where doc.dateDocument between :datedeb and :dateFin"
				+ " and doc.taEtat is null "+getRequeteARelancerJPQL() 
				+" and not exists ("+getRequeteDocumentTransforme("doc")+") and doc.taTiers.codeTiers like :codeTiers";;
//						+" order by d.mtHtCalc DESC";;
			Query query = entityManager.createQuery(requete);
			query.setParameter("datedeb", debut);
			query.setParameter("dateFin", fin);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
			query.setParameter("codeTiers", codeTiers);
			Long nbBonlivNonTranformeARelancer = (Long)query.getSingleResult();
			result = nbBonlivNonTranformeARelancer;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	/**
	 * Classe permettant d'obtenir les Prelevement non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de Prelevement transformés
	 */
	public long countDocumentTransforme(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre doc transfos");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";

			requete = "SELECT "
				+" count(doc)"
				+" FROM "+nameEntity+" doc " 
				+" where doc.dateDocument between :datedeb and :dateFin "
				+ "  and doc.taEtat is null "
				+" and exists ("+getRequeteDocumentTransforme("doc")+") and doc.taTiers.codeTiers like :codeTiers";
//						+" order by d.mtHtCalc DESC";;
			Query query = entityManager.createQuery(requete);
			query.setParameter("datedeb", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers", codeTiers);
			Long nbBonlivNonTranforme = (Long)query.getSingleResult();
			result = nbBonlivNonTranforme;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
		return findArticlesParTiersParMois(debut, fin,null);
	}
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_PAR_MOIS);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE); //TaArticlesParTiersDTO
			query.setParameter("codeTiers",codeTiers);
//			List<TaArticlesParTiersDTO> l = query.getResultList();
			List<TaArticlesParTiersDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
		return findArticlesParTiersTransforme(debut,fin,null);
		
	}
	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_TRANSFORME);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
//			List<TaArticlesParTiersDTO> l = query.getResultList();
			List<TaArticlesParTiersDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
		return findArticlesParTiersNonTransforme(debut, fin, null);
	}
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
//			List<TaArticlesParTiersDTO> l = query.getResultList();
			List<TaArticlesParTiersDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();

			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours) {
		return findArticlesParTiersNonTransformeARelancer(debut, fin, deltaNbJours,null);
	}
	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			Query query = entityManager.createQuery(FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			for (Parameter<?> l : query.getParameters()) {
				if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
				if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
			}
//			List<TaArticlesParTiersDTO> l = query.getResultList();
			List<TaArticlesParTiersDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createQuery(FIND_ALL_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
//			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Utilisé dans les autocomplete de la vu pour imprimer des documents en série
	 * @author nicolas
	 */
	public List<DocumentDTO> findAllDTOPeriodeSimple(Date dateDebut, Date dateFin,String codeTiers) {
		return findAllDTOPeriode(dateDebut,dateFin,codeTiers);
	}
	
	/**
	 * Utilisé dans les autocomplete de la vu pour imprimer des documents en série
	 * @author nicolas
	 */
	public List<DocumentDTO> findAllDTOIntervalle(String codeDebut, String codeFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createQuery(FIND_ALL_LIGHT_INTERVALLE);
			query.setParameter("codeDebut", codeDebut);
			query.setParameter("codeFin", codeFin);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
//			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	 /**
	 * Classe permettant d'obtenir la listes des Prelevement non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
	 * ne doivent pas dépasser par rapport à la date du jour 
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés à relancer
	 */
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
		logger.debug("getting liste doc non transfos à relancer");
		List<DocumentDTO> result = null;
		try {
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		if(codeTiers==null)codeTiers="%";
		Query query = entityManager.createQuery(FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		for (Parameter<?> l : query.getParameters()) {
			if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
			if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
		}
		query.setParameter("codeTiers",codeTiers);
		List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
//		List<DocumentDTO> l = query.getResultList();
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}

	
	
	 /**
	 * Classe permettant d'obtenir les Prelevement non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
	 */
	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		logger.debug("getting nombre doc non transfos");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Query query = entityManager.createQuery(FIND_NON_TRANSFORME_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		query.setParameter("codeTiers",codeTiers);
		List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
//		List<DocumentDTO> l = query.getResultList();
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}
	
	
	 /**
	 * Classe permettant d'obtenir la liste des Prelevement transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
	 */
	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		logger.debug("getting nombre doc transforme");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Query query = entityManager.createQuery(FIND_TRANSFORME_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		query.setParameter("codeTiers",codeTiers);
		List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
//		List<DocumentDTO> l = query.getResultList();
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}
	
	/**
	 * Procedure permettant d'obtenir le ca généré par les facture d'un Tiers sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param codeTiers code du Tiers
	 * @return La requête renvoyée renvoi le CA des factures sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeTiersDTO(Date dateDebut, Date dateFin, String codeTiers) {
		try {
			Query query = null;
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers", codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	/**
	 * Procedure permettant d'obtenir le ca généré par les facture d'un article sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param codeTiers code du Tiers
	 * @return La requête renvoyée renvoi le CA des factures sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeArticleDTO(Date dateDebut, Date dateFin, String codeArticle) {
		try {
			Query query = null;
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codearticle", codeArticle);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}

	/**
	 * Classe permettant d'obtenir le ca généré par les factures sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA total ht des avoirs sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date dateDebut, Date dateFin) {
		try {
			Query query = null;
			query = entityManager.createNamedQuery(SUM_CA_TOTAL_LIGTH_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			List<DocumentChiffreAffaireDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	
	public List<BigDecimal> listeTauxTvaExistant(){
			List<TaTva> tiers= daoTva.selectAll();
			LinkedList<BigDecimal> retour = new LinkedList<BigDecimal>();
			for (TaTva taTva : tiers) {
				retour.add(taTva.getTauxTva());
			}
			return retour;
	}
	
	public List<DocumentChiffreAffaireDTO> chiffreAffaireParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		logger.debug("getting nombre document dans periode");
		
		if(codeTiers==null)codeTiers="%";
		Query query = null;
		String requete="";
		boolean execute = true;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
        	
			 switch(typeRegroupement)
		        {
	            case Const.PAR_TIERS :
	    			requete = "select count(doc) as count,tiers.codeTiers as codeTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
	    					" join doc.lignes ldoc " + 
	    					" join doc.taTiers tiers " + 
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
	    					" group by tiers.codeTiers" + 
	    					" order by tiers.codeTiers";
	            break;
	            case Const.PAR_ARTICLE :
	    			requete = "select count(doc) as count,art.codeArticle as codeArticle,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
	    					" join doc.lignes ldoc " + 
	    					" join ldoc.taArticle art " + 
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
	    					" and art.codeArticle like :valeur " + 
	    					" group by art.codeArticle" + 
	    					" order by art.codeArticle";
	            break;	            
	            case Const.PAR_FAMILLE_TIERS :

	    			requete = "select count(doc) as count,fam.codeFamille as codeFamilleTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
	    					" join doc.lignes ldoc " + 
	    					" join doc.taTiers tiers " + 
	    					" join tiers.taFamilleTiers fam " + 
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
	    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
	    					" group by fam.codeFamille" + 
	    					" order by fam.codeFamille";
	            break;
	            
	            case Const.PAR_FAMILLE_ARTICLE :
		    			requete = "select count(doc) as count,fam.codeFamille as codeFamille,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.lignes ldoc " + 
		    					" join ldoc.taArticle art " + 
		    					" left join art.taFamille fam " + 
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
		    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
		    					" group by fam.codeFamille" + 
		    					" order by fam.codeFamille";
		            break;
		            
				case Const.PAR_TAUX_TVA:
	            	if(valeurRegroupement.equals("%")) {
	            		valeurRegroupement=listeTauxTvaExistant();
	            	}
					if(valeurRegroupement instanceof String) {
						valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
					}
		    			requete = "select count(doc) as count,ldoc.tauxTvaLDocument as tauxTva,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.lignes ldoc " + 
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
		    					" and (ldoc.tauxTvaLDocument is null or ldoc.tauxTvaLDocument in :valeur )" + 
		    					" group by ldoc.tauxTvaLDocument" + 
		    					" order by ldoc.tauxTvaLDocument";	            	
		            break;
		            case Const.PAR_TYPE_PAIEMENT:

		            	requete=" select count(doc) as count,tp.codeTPaiement as codeTPaiement,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.lignes ldoc " + 
		            			" left join doc.taRReglements rr  "+
		    					" left join rr.taReglement reg  "+
		    					" left join reg.taTPaiement tp  "+
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and (tp.codeTPaiement is null or tp.codeTPaiement like :valeur )" + 
		    					" group by tp.codeTPaiement" + 
		    					" order by tp.codeTPaiement";
		            break;
		            case Const.PAR_VENDEUR:
		            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
		            	requete=" select count(doc) as count,ut.username as vendeur,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.taUtilisateurVendeur ut "+
		    					" join doc.lignes ldoc " + 		            			
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and ut.username like :valeur " + 
		    					" group by ut.username" + 
		    					" order by ut.username";
		            	}else execute=false;
		            break;
		            default:execute=false;break;
		}


			 if(execute) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			if(!typeRegroupement.equals(Const.PAR_TIERS))query.setParameter("valeur",valeurRegroupement);
			
//			List<CountDocumentParTypeRegrouptement> l = query.getResultList();
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			return l;
			 }
			 return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<DocumentChiffreAffaireDTO> countDocumentParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		logger.debug("getting nombre document dans periode");

		List<DocumentChiffreAffaireDTO> l = null;
		if(codeTiers==null)codeTiers="%";
		Query query = null;
		String requete="";
		boolean execute=true;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			
			requete="select  "
					+"count(doc) as "+documentChiffreAffaireDTO.f_count+","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
					+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+",";
 
			
			
			switch(typeRegroupement)
			{
			case Const.PAR_ETAT :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("etat", "codeEtat")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
						" join doc.taEtat etat " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "
						+ " and etat.codeEtat  like :valeur " +
						" group by etat.codeEtat" + 
						" order by etat.codeEtat";
				break;					
			case Const.PAR_TIERS :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("tiers", "codeTiers")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
						" join doc.taTiers tiers " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
						" group by tiers.codeTiers" + 
						" order by tiers.codeTiers";
				break;
			case Const.PAR_ARTICLE :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("art", "codeArticle")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
						" join ldoc.taArticle art " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
						" and art.codeArticle like :valeur " + 
						" group by art.codeArticle" + 
						" order by art.codeArticle";
				break;	 

			case Const.PAR_FAMILLE_ARTICLE :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("fam", "codeFamille")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
						" join ldoc.taArticle art " + 
						" join art.taFamille fam " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
						" and fam.codeFamille like :valeur " + 
						" group by fam.codeFamille" + 
						" order by fam.codeFamille";
				break;
			case Const.PAR_FAMILLE_TIERS :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("famTiers", "codeFamille")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
						" join doc.taTiers tiers " + 
						" join tiers.taFamilleTiers famTiers " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
						" and (famTiers.codeFamille is null or famTiers.codeFamille like :valeur )" + 
						" group by famTiers.codeFamille" + 
						" order by famTiers.codeFamille";
				break;		            
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "tauxTvaLDocument")+ 
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
						" and ldoc.tauxTvaLDocument in :valeur " + 
						" group by ldoc.tauxTvaLDocument" + 
						" order by ldoc.tauxTvaLDocument";		            	
				break;

			default:execute=false;break;
			}
			if(execute) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("valeur",valeurRegroupement);
			l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(debut);				
			}
			}
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	
	/**
	 * Classe permettant d'obtenir le ca généré par les tickets de caisse sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param typeRegroupement map art une ligne représentant en cle le type de regroupement en valeur : la valeur du regroupement (ex parFamilleArticle , 'toto')
	 * @param regrouper sert à savoir si on doit détailler ou synthétiser les résultats
	 * @return La requête renvoyée renvoi le CA des tickets de caisse sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper) {
		try {
			Query query = null;
			String requete="";
			boolean execute = true;
			if(codeTiers==null)codeTiers="%";
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			
					
			switch(typeRegroupement)
			{
			case Const.PAR_TIERS :
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("tiers", "codeTiers")+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+" from "+nameEntity+" doc"  
						+" join doc.lignes ldoc "  
						+" join doc.taTiers tiers "  
						+" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers ";
				if(regrouper)requete+=" group by tiers.codeTiers";				
				break;	
			case Const.PAR_ARTICLE :
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("art", "codeArticle")+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+ " from "+nameEntity+" doc  join doc.lignes ldoc join ldoc.taArticle art  "
						+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers  ";
				if(regrouper)requete+=" group by art.codeArticle";
				break;
			case Const.PAR_FAMILLE_TIERS :
				requete="select  "
						+" famTiers.codeFamille as "+documentChiffreAffaireDTO.f_codeFamilleTiers+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+" from "+nameEntity+" doc"  
						+" join doc.lignes ldoc "  
						+" join doc.taTiers tiers "  
						+" join tiers.taFamilleTiers famTiers "  
						+" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "
						+" and (famTiers.codeFamille is null or famTiers.codeFamille like :valeur )"  
						+" group by famTiers.codeFamille"  ;
				if(regrouper)requete+=" group by famTiers.codeFamille";				
				break;	
			case Const.PAR_FAMILLE_ARTICLE :
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("fam", "codeFamille")+","
						
						+documentChiffreAffaireDTO.retournChampAndAlias("fam", "libcFamille")+","
						
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+ " from "+nameEntity+" doc  join doc.lignes ldoc join ldoc.taArticle art join art.taFamille fam "
						+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers and "
						+ " fam.codeFamille like :valeur ";
				if(regrouper)requete+=" group by fam.codeFamille, fam.libcFamille";
//				if(regrouper)requete+=" group by fam.codeFamille";
				break;
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "tauxTvaLDocument")+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+ " from "+nameEntity+" doc  join doc.lignes ldoc  "
						+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers and "
						+ " ldoc.tauxTvaLDocument in :valeur ";
				if(regrouper)requete+=" group by ldoc.tauxTvaLDocument";			            	
				break;
			default:execute=false;break;
			}

			if(execute) {
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				query.setParameter("valeur", valeurRegroupement);
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;
			}
			return null;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}

	}
	/**ATTENTION : Contrairement à son nom, il semble que cette méthode ne trie pas ni ne groupe par mois
	 */
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date debut, Date fin,String typeRegroupement,Object valeurRegroupement) {
		return findArticlesParTiersParMoisParTypeRegroupement( debut,  fin,null, typeRegroupement, valeurRegroupement, false);
	}
	/**ATTENTION : Contrairement à son nom, il semble que cette méthode ne trie pas ni ne groupe par mois
	 */
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement, boolean regroupee) {
		Query query = null;
		String requete="";
		String groupBy="";
		String orderBy="";
		boolean execute = true;
		if(codeTiers==null)codeTiers="%";
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			requete =
			"select "
					+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
//					taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
//					taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
//					taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
//					taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
					//MODIF YANN
					//taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
					//taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
					
//					taArticlesParTiersDTO.retournChampAndAlias("fam", "libcFamille")+" ,"+
					
					taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLDocument")+","+
					taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLDocument")+" ";
			
			switch(typeRegroupement)
			{
			case Const.PAR_ARTICLE :
//				requete+= " "
//						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
//						+ " join ldoc.taArticle art "
//						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
//						+ " and doc.taEtat is null"  
//						+ " and art.codeArticle like :valeur"
//						+ " group by art.codeArticle , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
//						+ " order by art.codeArticle,doc.codeDocument ";
				requete+=","+taArticlesParTiersDTO.retournChampAndAlias("fam", "libcFamille")+" "
						//MODIF YANN
						+","+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" "
						+","+taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" "
						
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
						+ " join ldoc.taArticle art left join art.taFamille fam"
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null"  
						+ " and art.codeArticle like :valeur";
						
						if(regroupee) {
							groupBy=" group by art.codeArticle , tiers.codeTiers,art.libellecArticle, fam.libcFamille";
						}else {
							groupBy=" group by art.codeArticle , tiers.codeTiers,art.libellecArticle, doc.codeDocument, fam.libcFamille";
						}
						orderBy = " order by art.codeArticle";
						
				break;
			case Const.PAR_TIERS :
				requete+= //MODIF YANN
						","+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" "
						+","+taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" "
						
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
						+ " join ldoc.taArticle art "
						+ " where doc.dateDocument between :dateDebut and :dateFin "
						+ " and doc.taEtat is null" 
						+ " and tiers.codeTiers like :valeur"
						+ " group by  tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by tiers.codeTiers,doc.codeDocument ";
				break;
			case Const.PAR_FAMILLE_ARTICLE :
				if(codeTiers.equals("%")) {
					requete =
							"select "
									+taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "qteLDocument")+", "
									+"coalesce(ldoc.u1LDocument, '') as "+DocumentChiffreAffaireDTO.f_u1LDocument+", "
									+taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLDocument")+","+
									taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLDocument")+" ";
				}
				requete+=","+taArticlesParTiersDTO.retournChampAndAlias("fam", "codeFamille")+" "
						+","+taArticlesParTiersDTO.retournChampAndAlias("fam", "libcFamille")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
						+ " join ldoc.taArticle art join art.taFamille fam "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null"  
						+ " and fam.codeFamille like :valeur";
				
						if(regroupee) {
							if(codeTiers.equals("%")) {
								groupBy=" group by fam.codeFamille, fam.libcFamille, coalesce(ldoc.u1LDocument, '')";
							}else {
								groupBy=" group by fam.codeFamille , tiers.codeTiers, fam.libcFamille";
							}
							
						}else {
							groupBy=" group by art.codeArticle , tiers.codeTiers,art.libellecArticle, doc.codeDocument, fam.libcFamille, fam.codeFamille";
						}
						orderBy = " order by fam.codeFamille";
				break;			
				
			case Const.PAR_FAMILLE_TIERS :
				requete+=","+taArticlesParTiersDTO.retournChampAndAlias("famTiers", "codeFamille")+" "
						//MODIF YANN
						+","+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" "
						+","+taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" "
								
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
						+ " join ldoc.taArticle art join tiers.taFamille famTiers "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null"  
						+ " and famTiers.codeFamille like :valeur"
						+ " group by famTiers.codeFamille , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by famTiers.codeFamille,doc.codeDocument ";
				break;
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete+=","+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "tauxTvaLDocument")+" "
						//MODIF YANN
						+","+taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" "
						+","+taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" "
								
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
						+ " join ldoc.taArticle art  "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
						+ " and doc.taEtat is null"  
						+ " and ldoc.tauxTvaLDocument in :valeur"
						+ " group by ldoc.tauxTvaLDocument , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by ldoc.tauxTvaLDocument,doc.codeDocument ";		            	
				break;		            

			default:execute=false;break;
			}

			if(execute) {
				requete+= groupBy + orderBy;
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				for (Parameter<?> l : query.getParameters()) {
					if(l.getName().equals("codeTiers"))query.setParameter("codeTiers", codeTiers);
				}				
				query.setParameter("valeur", valeurRegroupement);

				List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
				//			List<TaArticlesParTiersDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;
			}
			return null;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public List<DocumentDTO> findAllDTOPeriodeParTypeRegroupement(Date debut, Date fin,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		Query query = null;
		String requete="";
		if(codeTiers==null)codeTiers="%";
		boolean execute = false;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			requete =
					"select "
							+documentDTO.retournChampAndAlias("doc","idDocument")+", "
							+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
							+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
							+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
							+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
							+documentDTO.retournChampAndAlias("infos","nomTiers")+","
							+documentDTO.retournChampAndAlias("infos","nomEntreprise")+","
							+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
							+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
							+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
							+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
							+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
							+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
					+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers ";
			
			switch(typeRegroupement)
			{
			case Const.PAR_ARTICLE :
				requete+= " join doc.lignes ldoc join ldoc.taArticle art "
						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " and doc.taEtat is null"  
						+ " and art.codeArticle like :valeur"
						+ " group by art.codeArticle , tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by art.codeArticle,doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_TIERS :
				requete+=" "
						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " and doc.taEtat is null" 
						+ " and tiers.codeTiers like :valeur"
						+ " group by  tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.codeDocument,doc.idDocument,doc.dateDocument "
						+ " order by tiers.codeTiers,doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_FAMILLE_ARTICLE :
				requete+=" join doc.lignes ldoc  join ldoc.taArticle art join art.taFamille fam "
						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " and doc.taEtat is null"  
						+ " and fam.codeFamille like :valeur"
						+ " group by fam.codeFamille , tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by fam.codeFamille,doc.codeDocument ";
				execute=true;
				break;			
				
			case Const.PAR_FAMILLE_TIERS :
				requete+=" join tiers.taFamilleTiers famTiers "
						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " and doc.taEtat is null"  
						+ " and famTiers.codeFamille like :valeur"
						+ " group by famTiers.codeFamille , tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by famTiers.codeFamille,doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete+=" join doc.lignes ldoc "
						+ " where doc.dateDocument between :dateDebut and :dateFin   and doc.taTiers.codeTiers like :codeTiers  "
						+ " and doc.taEtat is null"  
						+ " and ldoc.tauxTvaLDocument in :valeur"
						+ " group by ldoc.tauxTvaLDocument , tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by ldoc.tauxTvaLDocument,doc.codeDocument ";		            	
				execute=true;
				break;		            

			default:execute=false;break;
			}

			if(execute) {
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				query.setParameter("valeur",valeurRegroupement);

				List<DocumentDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
				logger.debug("get successful");
				return l;
			}
			return null;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<DocumentDTO> findAllDTOPeriodeAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement) {
		Query query = null;
		String requete="";
		if(codeTiers==null)codeTiers="%";
		if(codeEtat==null ||codeEtat.equals("%")) {
			List<TaEtat> listEtat =serviceEtat.selectAll();
			codeEtat=new LinkedList<String>();
			for (TaEtat taEtat : listEtat) {
				((List<String>)codeEtat).add(taEtat.getCodeEtat());
			}
		}
		boolean execute = false;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			requete =
					"select "
							+documentDTO.retournChampAndAlias("doc","idDocument")+", "
							+documentDTO.retournChampAndAlias("doc","codeDocument")+", "
							+documentDTO.retournChampAndAlias("doc","dateDocument")+", "
							+documentDTO.retournChampAndAlias("doc","libelleDocument")+", "
							+documentDTO.retournChampAndAlias("tiers","codeTiers")+", "
							+documentDTO.retournChampAndAlias("infos","nomTiers")+","
							+documentDTO.retournChampAndAlias("infos","nomEntreprise")+","
							+documentDTO.retournChampAndAlias("doc","dateLivDocument")+","
							+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateEchDocument",entity)+","
							+documentDTO.retournChampAndAliasSuivantTypeDocument("doc","dateExport",entity)+","
							+documentDTO.retournChampAndAlias("doc","netHtCalc")+","
							+documentDTO.retournChampAndAlias("doc","netTvaCalc")+","
							+documentDTO.retournChampAndAlias("doc","netTtcCalc")+" "
					+ " from "+nameEntity+" doc join doc.taInfosDocument infos join doc.taTiers tiers ";
			
			switch(typeRegroupement)
			{
			case Const.PAR_ARTICLE :
				requete+= "  "
    					+" join doc.taEtat etat "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers "
    					+" and etat.codeEtat in :codeEtat "
						+ " and  exists("+getRequeteligneArticle("doc")+")"
						+ " group by tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_TIERS :
				requete+=" "
    					+" join doc.taEtat etat "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers "
    					+" and etat.codeEtat in :codeEtat "
						+ " and tiers.codeTiers like :valeur"
						+ " group by tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_FAMILLE_ARTICLE :
				requete+=" join doc.lignes ldoc  join ldoc.taArticle art join art.taFamille fam "
    					+" join doc.taEtat etat "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers "
    					+" and etat.codeEtat in :codeEtat "
						+ " and exists("+getRequeteligneFamilleArticle("doc")+")"
						+ " group by tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by doc.codeDocument ";
				execute=true;
				break;			
				
			case Const.PAR_FAMILLE_TIERS :
				requete+=" join tiers.taFamilleTiers famTiers "
    					+" join doc.taEtat etat "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers "
    					+" and etat.codeEtat in :codeEtat "
						+ " and famTiers.codeFamille like :valeur"
						+ " group by tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete+=" join doc.lignes ldoc "
    					+" join doc.taEtat etat "
						+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers "
    					+" and etat.codeEtat in :codeEtat "
						+ " and exists("+getRequeteligneTauxTva("doc")+")"
						+ " group by tiers.codeTiers, infos.nomTiers,infos.nomEntreprise,doc.idDocument,doc.codeDocument,doc.dateDocument "
						+ " order by doc.codeDocument ";
				execute=true;
				break;		            

			default:execute=false;break;
			}

			if(execute) {
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				query.setParameter("codeEtat",codeEtat);
				query.setParameter("valeur",valeurRegroupement);

				List<DocumentDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
				logger.debug("get successful");
				return l;
			}
			return null;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTOParRegroupement(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		
		Query query = null;
		List<DocumentChiffreAffaireDTO> l = null;
		if(codeTiers==null)codeTiers="%";
		String orderBy="";
		String groupBy="";
		String requete=	"select ";
		boolean execute=true;
		try {
			switch (precision) {
			case 0:
				requete+=documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+"," ;
				groupBy+= " group by "+documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument") ;
				orderBy+= " order by "+documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument");
				break;

			case 1:
				requete+=documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+"," ;
				requete+=documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+"," ;
				groupBy+= " group by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument") ;
				orderBy+= " order by "+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument");

				break;
			case 2:
				requete+=documentChiffreAffaireDTO.retournExtractDayAndAlias("doc","dateDocument")+"," ;
				requete+=documentChiffreAffaireDTO.retournExtractMonthAndAlias("doc","dateDocument")+"," ;
				requete+=documentChiffreAffaireDTO.retournExtractYearAndAlias("doc","dateDocument")+"," ;
				groupBy+= " group by "+documentChiffreAffaireDTO.retournExtractDay("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument") ;
				orderBy+= " order by "+documentChiffreAffaireDTO.retournExtractDay("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractMonth("doc","dateDocument")+","+documentChiffreAffaireDTO.retournExtractYear("doc","dateDocument");

				break;

			default:execute=false;break;
			}
			requete+=documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAliasMtTva("ldoc",documentChiffreAffaireDTO.f_mtTvaCalc,"mtHtLApresRemiseGlobaleDocument","mtTtcLApresRemiseGlobaleDocument")+","
					+ documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument");
		switch(typeRegroupement)
		{
		case Const.PAR_ARTICLE :
			requete+= listeChiffreAffaireTotalJmaDTOParArticle(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			break;
		case Const.PAR_TIERS :
			return listeChiffreAffaireTotalJmaDTO(dateDebut, dateFin, precision, codeTiers);
		case Const.PAR_FAMILLE_ARTICLE :
			requete+= listeChiffreAffaireTotalJmaDTOParFamilleArticle(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			break;			
		case Const.PAR_FAMILLE_TIERS :
			requete+= listeChiffreAffaireTotalJmaDTOParFamilleTiers(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			break;
		case Const.PAR_TAUX_TVA:
        	if(valeurRegroupement.equals("%")) {
        		valeurRegroupement=listeTauxTvaExistant();
        	}
        	if(valeurRegroupement instanceof String) {
				valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
			}

        	requete+= listeChiffreAffaireTotalJmaDTOParTauxTva(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			break;
		case Const.PAR_TYPE_PAIEMENT :
			requete+= listeChiffreAffaireTotalJmaDTOParTypePaiement(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			break;
		case Const.PAR_VENDEUR :
			requete+= listeChiffreAffaireTotalJmaDTOParVendeur(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			break;
		case Const.PAR_ETAT :
			requete+= listeChiffreAffaireTotalJmaDTOParEtat(dateDebut, dateFin, precision, codeTiers, typeRegroupement, valeurRegroupement);
			break;
		default:break;
		}
		
		
		
		if(execute) {
			requete+=groupBy+orderBy;
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("valeur",valeurRegroupement);
			l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
		}
		return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public String listeChiffreAffaireTotalJmaDTOParFamilleTiers(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		if(codeTiers==null)codeTiers="%";

			return  " from "+nameEntity+" doc  join doc.lignes ldoc join doc.taTiers tiers  "
					+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
					+ " and exists (select t from TaTiers t join t.taFamilleTiers famTiers where t=tiers and famTiers.codeFamille like :valeur)";
			
	}
	
	public String listeChiffreAffaireTotalJmaDTOParVendeur(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		if(codeTiers==null)codeTiers="%";

	
			return  " from "+nameEntity+" doc  join doc.lignes ldoc join doc.taTiers tiers  join doc.taUtilisateurVendeur ut "
					+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
					+ " and ut.username like :valeur ";
			
	}
	
	public String listeChiffreAffaireTotalJmaDTOParEtat(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		if(codeTiers==null)codeTiers="%";

	
			return  " from "+nameEntity+" doc  join doc.lignes ldoc join doc.taTiers tiers  join doc.taEtat etat "
					+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
					+ " and etat.codeEtat like :valeur ";
			
	}	
	public String listeChiffreAffaireTotalJmaDTOParTypePaiement(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		if(codeTiers==null)codeTiers="%";

	
			return " from "+nameEntity+" doc  join doc.lignes ldoc join doc.taTiers tiers  "
					+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
					+ " and exists (select doc from TaFacture doc join doc.taRReglements rr join rr.taReglement r join r.taTPaiement tp"
					+ " where tp.codeTPaiement like :valeur)";
			
	}	
	
	public String listeChiffreAffaireTotalJmaDTOParFamilleArticle(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		if(codeTiers==null)codeTiers="%";

			return  " from "+nameEntity+" doc  join doc.lignes ldoc join doc.taTiers tiers  join ldoc.taArticle art "
					+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
					+ " and exists (select art from TaArticle art join art.taFamille fam where art=art and fam.codeFamille like :valeur)";
			
	}
	
	public String listeChiffreAffaireTotalJmaDTOParTauxTva(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		if(codeTiers==null)codeTiers="%";

		return " from "+nameEntity+" doc  join doc.lignes ldoc join doc.taTiers tiers   "
					+ " where doc.dateDocument between :dateDebut and :dateFin  and tiers.codeTiers like :codeTiers "
					+ " and ldoc.tauxTvaLDocument in :valeur";

			
	}
	
	public String listeChiffreAffaireTotalJmaDTOParArticle(Date dateDebut, Date dateFin, 
			int precision,String codeTiers,String typeRegroupement,Object valeurRegroupement) {
		if(codeTiers==null)codeTiers="%";

		return  " from "+nameEntity+" doc  join doc.lignes ldoc join ldoc.taArticle art "
					+ " where doc.dateDocument between :dateDebut and :dateFin  and doc.taTiers.codeTiers like :codeTiers "
					+ " and art.codeArticle like :valeur ";



			
	}
	

	public List<DocumentChiffreAffaireDTO> chiffreAffaireParEtat(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement) {
		logger.debug("getting nombre document dans periode");
		
		if(codeTiers==null)codeTiers="%";
		if(codeEtat==null ||codeEtat.equals("%")) {
			List<TaEtat> listEtat =serviceEtat.selectAll();
			codeEtat=new LinkedList<String>();
			for (TaEtat taEtat : listEtat) {
				((List<String>)codeEtat).add(taEtat.getCodeEtat());
			}
		}
		Query query = null;
		String requete="";
		boolean execute = true;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			
			 switch(typeRegroupement)
		        {
	            case Const.PAR_TIERS :
	    			requete = "select count(doc) as count,t.codeTiers as codeTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
	    					" join doc.lignes ldoc " + 
	    					" join doc.taTiers t " + 
	    					" join doc.taEtat etat "+
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
	    					" and etat.codeEtat in :codeEtat "+
	    					" group by t.codeTiers" + 
	    					" order by t.codeTiers";
	            break;
	            case Const.PAR_ARTICLE :
	    			requete = "select count(doc) as count,art.codeArticle as codeArticle,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
	    					" join doc.lignes ldoc " + 
	    					" join ldoc.taArticle art " + 
	    					" join doc.taEtat etat "+
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
	    					" and etat.codeEtat in :codeEtat "+
	    					" and art.codeArticle like :valeur " + 
	    					" group by art.codeArticle" + 
	    					" order by art.codeArticle";
	            break;	 
	            case Const.PAR_FAMILLE_TIERS :
	    			requete = "select count(doc) as count,fam.codeFamille as codeFamilleTiers,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
	    					" join doc.lignes ldoc " + 
	    					" join doc.taTiers t " + 
	    					" join doc.taEtat etat "+
	    					" join t.taFamilleTiers fam " + 
	    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
	    					" and etat.codeEtat in :codeEtat "+
	    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
	    					" group by fam.codeFamille" + 
	    					" order by fam.codeFamille";
	            break;
	            
		            case Const.PAR_FAMILLE_ARTICLE :
		    			requete = "select count(doc) as count,fam.codeFamille as codeFamille,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.lignes ldoc " + 
		    					" join doc.taEtat etat "+
		    					" join ldoc.taArticle art " + 
		    					" left join art.taFamille fam " + 
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
		    					" and etat.codeEtat in :codeEtat "+
		    					" and (fam.codeFamille is null or fam.codeFamille like :valeur )" + 
		    					" group by fam.codeFamille" + 
		    					" order by fam.codeFamille";
		            break;
		            
					case Const.PAR_TAUX_TVA:
		            	if(valeurRegroupement.equals("%")) {
		            		valeurRegroupement=listeTauxTvaExistant();
		            	}
						if(valeurRegroupement instanceof String) {
							valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
						}
		    			requete = "select count(doc) as count,ldoc.tauxTvaLDocument as tauxTva,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.taEtat etat "+
		    					" join doc.lignes ldoc " + 
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
		    					" and etat.codeEtat in :codeEtat "+
		    					" and (ldoc.tauxTvaLDocument is null or ldoc.tauxTvaLDocument in :valeur )" + 
		    					" group by ldoc.tauxTvaLDocument" + 
		    					" order by ldoc.tauxTvaLDocument";	            	
		            break;
		            case Const.PAR_TYPE_PAIEMENT:

		            	requete=" select count(doc) as count,tp.codeTPaiement as codeTPaiement,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.taEtat etat "+
		    					" join doc.lignes ldoc " + 
		            			" left join doc.taRReglements rr  "+
		    					" left join rr.taReglement reg  "+
		    					" left join reg.taTPaiement tp  "+
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and etat.codeEtat in :codeEtat "+
		    					" and (tp.codeTPaiement is null or tp.codeTPaiement like :valeur )" + 
		    					" group by tp.codeTPaiement" + 
		    					" order by tp.codeTPaiement";
		            break;
		            case Const.PAR_VENDEUR:
		            	if(entity.getClass()==(TaTicketDeCaisse.class)) {
		            	requete=" select count(doc) as count,ut.username as vendeur,coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0)as mtHtCalc ,(coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)-coalesce(sum(ldoc.mtHtLApresRemiseGlobaleDocument),0))as mtTvaCalc, coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument),0)as mtTtcCalc from "+nameEntity+" doc" + 
		    					" join doc.taEtat etat "+
		    					" join doc.taUtilisateurVendeur ut "+
		    					" join doc.lignes ldoc " + 		            			
		    					" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
		    					" and etat.codeEtat in :codeEtat "+
		    					" and ut.username like :valeur " + 
		    					" group by ut.username" + 
		    					" order by ut.username";
		            	}else execute=false;
		            break;
		            default:execute=false;break;
		}


			 if(execute) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			if(!typeRegroupement.equals(Const.PAR_TIERS))query.setParameter("valeur",valeurRegroupement);
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			return l;
			 }
			 return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<DocumentChiffreAffaireDTO> countDocumentAndCodeEtatParTypeRegroupement(Date debut, Date fin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement) {
		logger.debug("getting nombre document dans periode");

		List<DocumentChiffreAffaireDTO> l = null;
		if(codeTiers==null)codeTiers="%";
		if(codeEtat==null ||codeEtat.equals("%")) {
			List<TaEtat> listEtat =serviceEtat.selectAll();
			codeEtat=new LinkedList<String>();
			for (TaEtat taEtat : listEtat) {
				((List<String>)codeEtat).add(taEtat.getCodeEtat());
			}
		}
		Query query = null;
		String requete="";
		boolean execute=true;
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			
			requete="select  "
					+"count(doc) as "+documentChiffreAffaireDTO.f_count+","
					+documentChiffreAffaireDTO.retournChampAndAlias("etat", "codeEtat")+","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
					+" coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
					+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+",";
 
			
			
			switch(typeRegroupement)
			{					
			case Const.PAR_TIERS :
				codeTiers="%";//on force le codetiers à tous et on prend la valeur de regroupement comme filtre
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("tiers", "codeTiers")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
    					" join doc.taEtat etat "+
						" join doc.taTiers tiers " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
						" and tiers.codeTiers like :valeur " + 
    					" and etat.codeEtat in :codeEtat "+
						" group by tiers.codeTiers,etat.codeEtat " + 
						" order by tiers.codeTiers";
				break;
			case Const.PAR_ARTICLE :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("art", "codeArticle")+
						" from "+nameEntity+" doc"+  
    					" join doc.taEtat etat "+
						" join doc.lignes ldoc "+ 				
						" join ldoc.taArticle art " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
						" and art.codeArticle like :valeur " + 
    					" and etat.codeEtat in :codeEtat "+
						" group by art.codeArticle,etat.codeEtat " + 
						" order by art.codeArticle";
				break;	 

			case Const.PAR_FAMILLE_ARTICLE :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("fam", "codeFamille")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
    					" join doc.taEtat etat "+
						" join ldoc.taArticle art " + 
						" join art.taFamille fam " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
    					" and etat.codeEtat in :codeEtat "+
						" and fam.codeFamille like :valeur " + 
						" group by fam.codeFamille,etat.codeEtat " + 
						" order by fam.codeFamille";
				break;
			case Const.PAR_FAMILLE_TIERS :
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("famTiers", "codeFamille")+
						" from "+nameEntity+" doc"+  
						" join doc.lignes ldoc "+ 				
    					" join doc.taEtat etat "+
						" join doc.taTiers tiers " + 
						" join tiers.taFamilleTiers famTiers " + 
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "+
    					" and etat.codeEtat in :codeEtat "+
						" and (famTiers.codeFamille is null or famTiers.codeFamille like :valeur )" + 
						" group by famTiers.codeFamille,etat.codeEtat " + 
						" order by famTiers.codeFamille";
				break;		            
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete+=documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "tauxTvaLDocument")+ 
						" from "+nameEntity+" doc"+  
    					" join doc.taEtat etat "+
						" join doc.lignes ldoc "+ 				
						" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers" + 
    					" and etat.codeEtat in :codeEtat "+
						" and ldoc.tauxTvaLDocument in :valeur " + 
						" group by ldoc.tauxTvaLDocument,etat.codeEtat " + 
						" order by ldoc.tauxTvaLDocument";		            	
				break;

			default:execute=false;break;
			}
			if(execute) {
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("codeEtat",codeEtat);
			query.setParameter("valeur",valeurRegroupement);
			l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(debut);				
			}
			}
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	
	public List<TaArticlesParTiersDTO> findArticlesParTiersMoisAndCodeEtatParTypeRegroupement(Date debut, Date fin,Object codeEtat,String typeRegroupement,Object valeurRegroupement) {
		Query query = null;
		String requete="";
		boolean execute = false;
		if(codeEtat==null ||codeEtat.equals("%")) {
			List<TaEtat> listEtat =serviceEtat.selectAll();
			codeEtat=new LinkedList<String>();
			for (TaEtat taEtat : listEtat) {
				((List<String>)codeEtat).add(taEtat.getCodeEtat());
			}
		}
		try {
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			requete =
			"select "
					+taArticlesParTiersDTO.retournChampAndAlias("tiers", "codeTiers")+" , "+
					taArticlesParTiersDTO.retournChampAndAlias("infos", "nomTiers")+" ,	"+
					taArticlesParTiersDTO.retournChampAndAlias("ent", "nomEntreprise")+","+
					taArticlesParTiersDTO.retournChampAndAlias("doc", "codeDocument")+","+
					taArticlesParTiersDTO.retournChampAndAlias("doc", "dateDocument")+" ,"+
					taArticlesParTiersDTO.retournChampAndAlias("art", "codeArticle")+" ,"+
					taArticlesParTiersDTO.retournChampAndAlias("art", "libellecArticle")+" ,"+
					taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLDocument")+","+
					taArticlesParTiersDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLDocument")+" ";
			
			switch(typeRegroupement)
			{
			case Const.PAR_ARTICLE :
				requete+= " "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
    					+" join doc.taEtat etat "
						+ " join ldoc.taArticle art "
						+ " where doc.dateDocument between :dateDebut and :dateFin "
    					+" and etat.codeEtat in :codeEtat "
						+ " and art.codeArticle like :valeur"
						+ " group by art.codeArticle , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by art.codeArticle,doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_TIERS :
				requete+= " "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
    					+" join doc.taEtat etat "
						+ " join ldoc.taArticle art "
						+ " where doc.dateDocument between :dateDebut and :dateFin "
    					+" and etat.codeEtat in :codeEtat "
						+ " and tiers.codeTiers like :valeur"
						+ " group by  tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by tiers.codeTiers,doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_FAMILLE_ARTICLE :
				requete+=","+taArticlesParTiersDTO.retournChampAndAlias("fam", "codeFamille")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
    					+" join doc.taEtat etat "
						+ " join ldoc.taArticle art join art.taFamille fam "
						+ " where doc.dateDocument between :dateDebut and :dateFin "
    					+" and etat.codeEtat in :codeEtat "
						+ " and fam.codeFamille like :valeur"
						+ " group by fam.codeFamille , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by fam.codeFamille,doc.codeDocument ";
				execute=true;
				break;			
				
			case Const.PAR_FAMILLE_TIERS :
				requete+=","+taArticlesParTiersDTO.retournChampAndAlias("famTiers", "codeFamille")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
    					+" join doc.taEtat etat "
						+ " join ldoc.taArticle art join tiers.taFamille famTiers "
						+ " where doc.dateDocument between :dateDebut and :dateFin "
    					+" and etat.codeEtat in :codeEtat "
						+ " and famTiers.codeFamille like :valeur"
						+ " group by famTiers.codeFamille , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by famTiers.codeFamille,doc.codeDocument ";
				execute=true;
				break;
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete+=","+taArticlesParTiersDTO.retournChampAndAlias("ldoc", "tauxTvaLDocument")+" "
						+ " from "+nameEntity+" doc join doc.lignes ldoc join doc.taTiers tiers join doc.taInfosDocument infos left join tiers.taEntreprise ent "
    					+" join doc.taEtat etat "
						+ " join ldoc.taArticle art  "
						+ " where doc.dateDocument between :dateDebut and :dateFin "
    					+" and etat.codeEtat in :codeEtat "
						+ " and ldoc.tauxTvaLDocument in :valeur"
						+ " group by ldoc.tauxTvaLDocument , tiers.codeTiers, infos.nomTiers,ent.nomEntreprise,doc.codeDocument,doc.dateDocument,art.codeArticle,art.libellecArticle,ldoc.mtHtLDocument,ldoc.mtTtcLDocument "
						+ " order by ldoc.tauxTvaLDocument,doc.codeDocument ";		            	
				execute=true;
				break;		            

			default:execute=false;break;
			}

			if(execute) {
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", debut, TemporalType.DATE);
				query.setParameter("dateFin", fin, TemporalType.DATE);
				query.setParameter("codeEtat",codeEtat);
				query.setParameter("valeur",valeurRegroupement);

				List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
				//			List<TaArticlesParTiersDTO> l = query.getResultList();
				logger.debug("get successful");
				return l;
			}
			return null;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	/**
	 * Classe permettant d'obtenir le ca généré par les tickets de caisse sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param typeRegroupement map a une ligne représentant en cle le type de regroupement en valeur : la valeur du regroupement (ex parFamilleArticle , 'toto')
	 * @param regrouper sert à savoir si on doit détailler ou synthétiser les résultats
	 * @return La requête renvoyée renvoi le CA des tickets de caisse sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOAndCodeEtatParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,Object codeEtat,String typeRegroupement,Object valeurRegroupement,boolean regrouper) {
		try {
			Query query = null;
			String requete="";
			boolean execute = true;
			if(codeTiers==null)codeTiers="%";
			if(codeEtat==null ||codeEtat.equals("%")) {
				List<TaEtat> listEtat =serviceEtat.selectAll();
				codeEtat=new LinkedList<String>();
				for (TaEtat taEtat : listEtat) {
					((List<String>)codeEtat).add(taEtat.getCodeEtat());
				}
			}			
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
			switch(typeRegroupement)
			{
			case Const.PAR_TIERS :
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("tiers", "codeTiers")+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+" from "+nameEntity+" doc"  
    					+" join doc.taEtat etat "
						+" join doc.lignes ldoc "  
						+" join doc.taTiers tiers "  
						+" where doc.dateDocument between :dateDebut and :dateFin "
    					+" and etat.codeEtat in :codeEtat "
						+ " and doc.taTiers.codeTiers like :codeTiers ";
				if(regrouper)requete+=" group by etat.codeEtat,tiers.codeTiers";				
				break;	
			case Const.PAR_ARTICLE :
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("art", "codeArticle")+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+ " from "+nameEntity+" doc  "
    					+" join doc.taEtat etat "
						+ " join doc.lignes ldoc join ldoc.taArticle art  "
						+ " where doc.dateDocument between :dateDebut and :dateFin "
    					+" and etat.codeEtat in :codeEtat "
						+ " and doc.taTiers.codeTiers like :codeTiers  ";
				if(regrouper)requete+=" group by etat.codeEtat,art.codeArticle";
				break;
			case Const.PAR_FAMILLE_TIERS :
				requete="select  "
						+" famTiers.codeFamille as "+documentChiffreAffaireDTO.f_codeFamilleTiers+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+" from "+nameEntity+" doc"  
    					+" join doc.taEtat etat "
						+" join doc.lignes ldoc "  
						+" join doc.taTiers tiers "  
						+" join tiers.taFamilleTiers famTiers "  
						+" where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers "
    					+" and etat.codeEtat in :codeEtat "
						+" and (famTiers.codeFamille is null or famTiers.codeFamille like :valeur )"  
						+" group by famTiers.codeFamille"  ;
				if(regrouper)requete+=" group by etat.codeEtat,famTiers.codeFamille";				
				break;	
			case Const.PAR_FAMILLE_ARTICLE :
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("fam", "codeFamille")+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+ " from "+nameEntity+" doc "
    					+" join doc.taEtat etat "
						+ " join doc.lignes ldoc join ldoc.taArticle art join art.taFamille fam "
						+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers  "
    					+" and etat.codeEtat in :codeEtat "
						+ " and fam.codeFamille like :valeur ";
				if(regrouper)requete+=" group by etat.codeEtat,fam.codeFamille";
				break;
			case Const.PAR_TAUX_TVA:
            	if(valeurRegroupement.equals("%")) {
            		valeurRegroupement=listeTauxTvaExistant();
            	}
				if(valeurRegroupement instanceof String) {
					valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
				}
				requete="select  "
						+documentChiffreAffaireDTO.retournChampAndAlias("ldoc", "tauxTvaLDocument")+","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtHtLApresRemiseGlobaleDocument")+","
						+ " coalesce(sum(ldoc.mtTtcLApresRemiseGlobaleDocument-ldoc.mtHtLApresRemiseGlobaleDocument),0) as "+DocumentChiffreAffaireDTO.f_mtTvaCalc +","
						+documentChiffreAffaireDTO.construitSumCoallesceAsAlias("ldoc", "mtTtcLApresRemiseGlobaleDocument")+""
						+ " from "+nameEntity+" doc "
    					+" join doc.taEtat etat "
						+ " join doc.lignes ldoc  "
						+ " where doc.dateDocument between :dateDebut and :dateFin and doc.taTiers.codeTiers like :codeTiers  "
    					+" and etat.codeEtat in :codeEtat "
						+ " and ldoc.tauxTvaLDocument in :valeur ";
				if(regrouper)requete+=" group by etat.codeEtat,ldoc.tauxTvaLDocument";			            	
				break;
			default:execute=false;break;
			}

			if(execute) {
				query = entityManager.createQuery(requete);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeTiers",codeTiers);
				query.setParameter("codeEtat",codeEtat);
				query.setParameter("valeur",valeurRegroupement);
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
				//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
				}
				logger.debug("get successful");
				return l;
			}
			return null;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}

	}

	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle){
		return listLigneArticlePeriodeParArticleAvoirFactureDTO(dateDebut, dateFin, codeArticle, null);
	}
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese) {
		return listLigneArticlePeriodeParArticleAvoirFactureDTO( dateDebut,  dateFin,  codeArticle,  synthese,  null);
	}
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeAvoirFactureFournisseurDTO(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy) {
					try {
						Query query = null;
						String codeTiers = "%";
						String sql = "";
						if(codeArticle==null)codeArticle="%";
						if(synthese != null && synthese == true) {//si on veut une synthese donc group by et sum sur les lignes de docs facture et avoirs
							sql = LIST_SUM_LIGNE_ARTICLE_PERIODE_PAR_AVOIR_FACTURE_FOURNISSEUR_GROUP_BY_ARTICLE;
						}else {// si on veut le detail (lignes de docs facture et avoirs)
							sql = LIST_LIGNE_ARTICLE_PERIODE_AVOIR_FACTURE_FOURNISSEUR;
							 
						}
//						sql = LIST_LIGNE_ARTICLE_PERIODE_AVOIR_FACTURE_FOURNISSEUR;
						
						if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_CODE_DOCUMENT)) {
							sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeDocument+", b."+DocumentChiffreAffaireDTO.f_codeArticle;
						}else if(orderBy != null && orderBy.equals(DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT)) {
							sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_dateDocument+", b."+DocumentChiffreAffaireDTO.f_codeDocument+", b."+DocumentChiffreAffaireDTO.f_codeArticle;
						}else {
							sql += " ORDER BY b."+DocumentChiffreAffaireDTO.f_codeDocument+", b."+DocumentChiffreAffaireDTO.f_codeArticle;
						}
			
			query = entityManager.createNativeQuery(sql);
			
			//test
//			query = entityManager.createNativeQuery(LIST_TIER_AYANT_ACHETER_ARTICLE_TEST_NATIF);
			
			
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			
			
			// si on veut le detail (lignes de docs facture et avoirs)
			if(synthese)addScalarDocumentArticleFournisseurDTOGroupByArticle(query);
			else addScalarDocumentArticleFournisseurDTODetail(query);
			
			
			
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();

//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}


	

	
	public int findDocByLDocDTO(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a.idDocument from "+nameEntity+" a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			int instance = (int)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	

	
	//////////////******* AVEC ETAT ************////////////////
	
	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat) {
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTotalSurEtatDTO(dateDebut, dateFin,codeTiers,etat).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}

//	public List<DocumentDTO> findAllDTOPeriodeSurEtat(Date dateDebut, Date dateFin,String codeTiers, String etat) {
//		try {
//			if(codeTiers==null)codeTiers="%";
//			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), 15, 0, 0);
//			Date dateJour = LibDate.dateDuJour();
//			Query query = entityManager.createQuery(FIND_ALL_LIGHT_PERIODE_SUR_ETAT.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)"));
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			query.setParameter("etat",etat);
//			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
////			List<DocumentDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

	/**
	 * Classe permettant d'obtenir le ca généré par les Prelevement sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des Prelevement sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSurEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(SUM_CA_TOTAL_LIGTH_PERIODE_SUR_ETAT);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("etat",etat);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
	public List<DocumentChiffreAffaireDTO> chiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat) {
		List<DocumentChiffreAffaireDTO> infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTotalAvecEtatDTO(dateDebut, dateFin,codeTiers,etat);
			for (DocumentChiffreAffaireDTO l : infosCaTotal) {
				l.setMtHtCalc(l.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
				l.setMtTvaCalc(l.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
				l.setMtTtcCalc(l.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			}

			return infosCaTotal;
	}

	public List<DocumentDTO> findAllDTOPeriodeAvecEtat(Date dateDebut, Date dateFin,String codeTiers, String etat) {
		try {
			if(codeTiers==null)codeTiers="%";
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), 15, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			Query query = entityManager.createQuery(FIND_ALL_LIGHT_PERIODE_AVEC_ETAT.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)"));
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("etat",etat);
			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentDTO.class)).list();
//			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Classe permettant d'obtenir le ca généré par les Prelevement sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des Prelevement sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalAvecEtatDTO(Date dateDebut, Date dateFin,String codeTiers, String etat) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), 15, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			String requete=SUM_CA_TOTAL_LIGTH_PERIODE_AVEC_ETAT.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)");
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("etat",etat);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}	 

	
	public Query addScalarDocumentChiffreAffaireAvecEtatDTO(Query query ) {
		query.unwrap(SQLQuery.class)
		.addScalar(DocumentChiffreAffaireDTO.f_identifiantEtat, StringType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_relancer, BooleanType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_jour, StringType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mois, StringType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_annee, StringType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mtHtCalc, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mtTvaCalc, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_mtTtcCalc, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_reglementComplet, BigDecimalType.INSTANCE)
		.addScalar(DocumentChiffreAffaireDTO.f_resteAReglerComplet, BigDecimalType.INSTANCE)
		.setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class));
		return query;
	}
	
	
	/**-------------------------------------------------------------------------------------
	 * Classe permettant d'obtenir le ca généré par les Bons de livraison sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des bonLiv sur la période éclaté en fonction de la précision 
	 * Jour Mois Année
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaAvecEtatDTO(Date dateDebut, Date dateFin, int precision,String codeTiers,String etat) {
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), 15, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		String requete="";
		try {
			switch (precision) {
			case 0:
				requete=SUM_CA_ANNEE_LIGTH_PERIODE.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)");
				query = entityManager.createQuery(requete);
				
				break;

			case 1:
				requete=SUM_CA_MOIS_LIGTH_PERIODE.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)");
				query = entityManager.createQuery(requete);
				
				break;
			case 2:
				requete=SUM_CA_JOUR_LIGTH_PERIODE.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)");
				query = entityManager.createQuery(requete);
				
				break;

			default:
				break;
			}
			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("etat",etat);

			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	

	
	
	/**
	 * Classe permettant d'obtenir une liste de ligne d'article  sur une période donnée par tiers, par articles trié par TIERS et par type etat doc (transformé, non transformé, a relancer)
	 * Notamment utilisée dans le dashboard facture onglet détail 
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @param String typeEtatDoc  
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture, par tiers/article et groupé par Tiers
	 */
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtatDoc(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String typeEtatDoc, int deltaNbJours) {
		try {
			String queryConst ="LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS";
			queryConst +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
			if(typeEtatDoc == null) {
			 return	listLigneArticleDTOTiers( dateDebut,  dateFin,  codeArticle,  codeTiers);
//			}else if(typeEtatDoc.equals(TaEtat.ETAT_TRANSFORME)){	
			// ISA  le 08/08/2019 suite à prise en charge des différents états
			}else if(typeEtatDoc.equals(TaEtat.TERMINE_TOTALEMENT_TRANSFORME)){
				queryConst = LIST_LIGNE_ARTICLE_TRANSFO_PERIODE_PAR_TIERS;
//			}else if(typeEtatDoc.equals(TaEtat.ETAT_NON_TRANSFORME)){
				// ISA  le 08/08/2019 suite à prise en charge des différents états
			}else if(typeEtatDoc.equals(TaEtat.ENCOURS)){
				queryConst = LIST_LIGNE_ARTICLE_NON_TRANSFO_PERIODE_PAR_TIERS;
			}else if(typeEtatDoc.equals(TaEtat.ETAT_NON_TRANSFORME_A_RELANCER)){
				queryConst = LIST_LIGNE_ARTICLE_A_RELANCER_PERIODE_PAR_TIERS;
				if(deltaNbJours == 0) {
					deltaNbJours = 15;
				}
				Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
				Date dateJour = LibDate.dateDuJour();
				Query query = null;
				if(codeArticle==null)codeArticle="%";
				if(codeTiers==null)codeTiers="%";
				query = entityManager.createQuery(queryConst);
				query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
				query.setParameter("dateFin", dateFin, TemporalType.DATE);
				query.setParameter("codeArticle",codeArticle);
				query.setParameter("codeTiers",codeTiers);
				for (Parameter<?> l : query.getParameters()) {
					if(l.getName().equals("dateRef"))query.setParameter("dateRef", dateRef, TemporalType.DATE);
					if(l.getName().equals("dateJour"))query.setParameter("dateJour", dateJour, TemporalType.DATE);
				}
				List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//				if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//					l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//				}
				logger.debug("get successful");
				return l;
			}
			
			
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createQuery(queryConst);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}

	
	
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin, String codeArticle, String codeTiers,String etat,int deltaNbJours) {
		return listLigneArticleDTOTiersAvecEtat( dateDebut,  dateFin,  codeArticle,  codeTiers,etat, null,deltaNbJours);
	}
	/**
	 * Méthode permettant d'obtenir une liste de ligne d'article (Détail)  sur une période donnée pour un tiers, ou pour un article trié par dafut par TIERS
	 * On peut choisir le trie
	 * @param debut date de début période
	 * @param fin date de fin période
	 * @param String codeArticle
	 * @param String codeTiers
	 * @param String orderBy
	 * @return List<DocumentChiffreAffaireDTO> La requête renvoyée renvoi une liste de DocumentChiffreAffaireDTO  sur la période correspondant aux lignes facture, pour un tiers/article et trié par defaut par Tiers
	 */
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersAvecEtat(Date dateDebut, Date dateFin, String codeArticle, String codeTiers,String etat, String orderBy,int deltaNbJours) {
		try {
			Query query = null;
			if(codeArticle==null)codeArticle="%";
			if(codeTiers==null)codeTiers="%";
			if(etat==null)etat="%";
			
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			
			String sql = LIST_LIGNE_ARTICLE_PERIODE_PAR_TIERS_AVEC_ETAT.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)");

			if(orderBy == null) {//trie par defaut par tiers
				sql +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_DATE_DOCUMENT) {
				sql +=  " order by doc.dateDocument ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_CODE_ARTICLE) {
				sql +=  " order by art.codeArticle ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_CODE_FAMILLE_ARTICLE) {
				sql +=  " order by  fam.codeFamille ";
			}else if(orderBy == DocumentChiffreAffaireDTO.ORDER_BY_LIBL_FAMILLE_ARTICLE) {
				sql +=  " order by  fam.libcFamille ";
			}else {// trie par defaut par tiers
				sql +=  " order by tiers.codeTiers, art.codeArticle,doc.dateDocument ";
			}
				
			
			query = entityManager.createQuery(sql);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeArticle",codeArticle);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("etat",etat);
			List<DocumentChiffreAffaireDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			List<DocumentDTO> l = query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(DocumentChiffreAffaireDTO.class)).list();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}

	
	public List<TaArticlesParTiersDTO> findArticlesParTiersParMoisAvecEtat(Date debut, Date fin,String codeTiers, String etat,int deltaNbJours) {
		try {
			if(codeTiers==null)codeTiers="%";
			if(etat==null)etat="%";
			
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			
			String sql = FIND_ARTICLES_PAR_TIERS_PAR_MOIS_AVEC_ETAT.replace(":dateRef", "cast('"+LibDate.dateToString(dateRef)+"' as date)").replace(":dateJour", "cast('"+LibDate.dateToString(dateJour)+"' as date)");

			
			Query query = entityManager.createQuery(sql);
			query.setParameter("dateDebut", debut, TemporalType.DATE);
			query.setParameter("dateFin", fin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("etat",etat);
			List<TaArticlesParTiersDTO> l= query.unwrap(org.hibernate.Query.class).setResultTransformer(Transformers.aliasToBean(TaArticlesParTiersDTO.class)).list();
//			List<TaArticlesParTiersDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}

	 

