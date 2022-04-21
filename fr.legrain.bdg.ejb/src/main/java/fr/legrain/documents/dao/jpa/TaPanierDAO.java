package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.documents.dao.IPanierDAO;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaPanier.
 * @see fr.legrain.documents.dao.TaPanier
 * @author Hibernate Tools
 */
public class TaPanierDAO extends AbstractDocumentDAO<TaPanier,TaInfosPanier,TaLPanier>  implements IPanierDAO, IDocumentDAO<TaPanier>,IDocumentTiersStatistiquesDAO<TaPanier> {

//	private static final Log log = LogFactory.getLog(TaPanierDAO.class);
	static Logger logger = Logger.getLogger(TaPanierDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaPanier a";
	public TaPanierDAO(){
		initialiseRequetes();
//		this(null);
	}
	
	public TaBoncde findCommandePanier(String codePanier) {
		try {
			Query ejbQuery = entityManager.createQuery("select cmd from TaRDocument r join r.taBoncde cmd join r.taPanier panier where panier.codeDocument = :codePanier");
			ejbQuery.setParameter("codePanier", codePanier);
			TaBoncde instance = (TaBoncde) ejbQuery.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
	public TaReglement findReglementPourPanier(String codePanier) {
		try {
			Query ejbQuery = entityManager.createQuery("select reg from TaStripePaymentIntent pi "
					+ "join pi.taPanier p join pi.taReglement reg "
					+ "where p.codeDocument = :codePanier and (pi.status='succeeded' or pi.status='requires_capture')");
			ejbQuery.setParameter("codePanier", codePanier);
			TaReglement instance = (TaReglement) ejbQuery.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
	public TaStripePaymentIntent findReglementStripePanier(String codePanier) {
		try {
			Query ejbQuery = entityManager.createQuery("select pi from TaStripePaymentIntent pi "
					+ "join pi.taPanier p join pi.taReglement reg "
					+ "where p.codeDocument = :codePanier and (pi.status='succeeded' or pi.status='requires_capture')");
			ejbQuery.setParameter("codePanier", codePanier);
			TaStripePaymentIntent instance = (TaStripePaymentIntent) ejbQuery.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
	public TaPanier findByActif(String codeTiers) {
		logger.debug("getting TaPanier instance with id: ");
		try {
			Query ejbQuery = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_ACTIF);
			ejbQuery.setParameter("codeTiers", codeTiers);
			List<TaPanier> liste = (List<TaPanier>) ejbQuery.getResultList();
			TaPanier instance = null;
			if(liste != null && !liste.isEmpty()) {
				instance = liste.get(liste.size() - 1);
			}
			
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
	public TaPanier findByActifAvecLiaisonReglement(String codeTiers) {
		logger.debug("getting TaPanier instance with id: ");
		try {
			Query ejbQuery = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_ACTIF_AVEC_LIAISON_REGLEMENT);
			ejbQuery.setParameter("codeTiers", codeTiers);
			TaPanier instance = (TaPanier) ejbQuery.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
	public void persist(TaPanier transientInstance) {
		logger.debug("persisting TaPanier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaPanier persistentInstance) {
		logger.debug("removing TaPanier instance");
		try {
			
			entityManager.remove(persistentInstance);
			
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaPanier merge(TaPanier detachedInstance) {
		logger.debug("merging TaPanier instance");
		try {
//			if(detachedInstance.getTaInfosDocument()==null) {
//				throw new RuntimeException("Il manque l'infoDocument du document n°: "+detachedInstance.getCodeDocument());
//			}
			TaPanier result = entityManager.merge(detachedInstance);
//			if(result.getTaInfosDocument()==null) {
//				throw new RuntimeException("Il manque l'infoDocument du document n°: "+result.getCodeDocument());
//			}
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaPanier findById(int id) {
		logger.debug("getting TaPanier instance with id: " + id);
		try {
			TaPanier instance = entityManager.find(TaPanier.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaPanier findByCode(String code) {
		logger.debug("getting TaPanier instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaPanier a " +
					"  left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");			
			TaPanier instance = (TaPanier)query.getSingleResult();
			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaPanier> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaPanier instance with codeTiers: " + codeTiers);
		List<TaPanier> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_TIERS_AND_DATE);
			if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin) {
		// TODO Auto-generated method stub
		logger.debug("selectDocNonTransformeRequete ");
		try {			
			String requete = "select a from Ta"+typeOrigine+" a join a.taTiers t where " ;
			if(codeTiers!=null ) //&& !codeTiers.equals("")
				requete+=" t.codeTiers like '"+codeTiers+"' and ";
			if(doc!=null)
				requete+=" a.idDocument = "+doc.getIdDocument()+" and ";
			requete+=" a.dateDocument between :dateDeb and :dateFin and not exists(select r from TaRDocument r " +
					"join r.ta"+typeOrigine+" org " +
					"join r.ta"+typeDest+" dest " +
					" where org.idDocument" +
					" = a.idDocument and (dest.idDocument is not null or dest.idDocument!=0))";
			requete+="  order by a.dateDocument,a.codeDocument";
			Query ejbQuery = entityManager.createQuery(requete);
			ejbQuery.setParameter("dateDeb", debut);
			ejbQuery.setParameter("dateFin", fin);
			List<IDocumentTiers> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * |année|codeTiers|HT|TVA|TTC
	 * @param codeTiers
	 * @param debut
	 * @param fin
	 * @return
	 */
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers,Date debut, Date fin, int precision) {
		logger.debug("getting TaPanier instance with codeTiers: " + codeTiers);
		List<Object> result = null;
		try {
			String jpql = null;
//			precision = 0;
			String groupBy = null;
			String select = null;
			
			if(precision==0) {
				select = "'','',cast(extract(year from f.dateDocument)as string)";
				groupBy = "'','',extract(year from f.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "'',extract(month from f.dateDocument),extract(year from f.dateDocument)";
			} else {
				select = "cast(extract(day from f.dateDocument)as string),cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			}
			
			jpql = "select  "
				+select+", t.codeTiers,"
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
				+" from TaPanier f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
				+"group by "+groupBy+",t.codeTiers"
				;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaPanier f "
//			+"left join f.taTiers t "
//			+"where  t.codeTiers = ? and f.dateDocument between ? and ? "
//			+"group by extract(year from f.dateDocument),t.codeTiers"
//			;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaPanier> selectAll() {
		logger.debug("selectAll TaPanier");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPanier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les bon de livraison entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des bon de livraison entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaPanier> rechercheDocument(Date dateDeb, Date dateFin) {
		List<TaPanier> result = null;
		Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	
	
	/**
	 * Recherche les bon de livraison entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des bon de livraison entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaPanier> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		List<TaPanier> result = null;
		Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	/**
	 * Recherche les bon de livraison entre 2 codes bon de livraison
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des bon de livraison entre ces 2 codes (null en cas d'erreur)
	 */
	public List<TaPanier> rechercheDocument(String codeDeb, String codeFin) {		
		List<TaPanier> result = null;
		Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;

	}
	
	
//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeTiers,
			Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les bons de livraison sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoi le montant des par les BL sur la période
	 */
	public List<Object> findCABLSurPeriode(Date debut, Date fin) {
		logger.debug("getting nombre ca des bc");
		List<Object> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" sum(d.netHtCalc)"
				+" FROM TaPanier d " 
				+" where d.dateDocument between :dateDeb and :dateFin";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir les livraisons non transformées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des livraisons non transformées
	 */
	public List<TaPanier> findLivraisonsNonTransfos(Date debut, Date fin) {
		logger.debug("getting nombre livraisons non transfos");
		List<TaPanier> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
			+" d"
			+" FROM TaPanier d " 
			+" where d.dateDocument between :dateDeb and :dateFin"
			+" and not exists (select r " +
					"from TaRDocument r " +
					" where r.taPanier = d" +
					" and ( taFacture IS NOT NULL))"
					+" order by d.mtHtCalc DESC";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<Object> findChiffreAffaireTotalTransfo(Date debut, Date fin, int precision) {
		logger.debug("getting ChiffreAffaire total transfo");
		List<Object> result = null;
		try {
			String requete = "";
			String groupBy = "";
			String select = "";
			
			if(precision==0) {
				select = "'','',cast(extract(year from d.dateDocument)as string)";
				groupBy = "'','',extract(year from d.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "'',extract(month from d.dateDocument),extract(year from d.dateDocument)";
			} else {
				select = "cast(extract(day from d.dateDocument)as string),cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "extract(day from d.dateDocument),extract(month from d.dateDocument),extract(year from d.dateDocument)";
			}
			
			requete = "SELECT "+select+ ", "
			+" sum(d.netHtCalc), "
			+" sum(d.netTvaCalc), "
			+" sum(d.netTtcCalc) "
			+" FROM TaPanier d " 
			+" where d.dateDocument between :dateDeb and :dateFin "
			+" and exists (select r " 
			+" from TaRDocument r " 
			+" where r.taPanier = d " 
			+" and ( r.taFacture IS NOT NULL)) "
			+" group by "+groupBy; 
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir les livraisons  transformées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des livraisons transformées
	 */
	public List<TaPanier> findLivraisonsTransfos(Date debut, Date fin) {
		logger.debug("getting nombre livraisons transfos");
		List<TaPanier> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
			+" d"
			+" FROM TaPanier d " 
			+" where d.dateDocument between :dateDeb and :dateFin"
			+" and exists (select r " +
					"from TaRDocument r " +
					" where r.taPanier = d" +
					" and ( taFacture IS NOT NULL))"
			+" order by d.mtHtCalc DESC";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaPanier> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers, null);
	}

	@Override
	public List<TaPanier> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers, null);
	}

	@Override
	public List<TaPanier> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaPanier> result = null;
		Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaPanier> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaPanier> result = null;
		Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}
	
	/**
	 * Recherche les factures entre 2 dates light
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light) {
		List<Object[]> result = null;
		Query query = null;
		if(light)
			query =entityManager.createNamedQuery(TaPanier.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,String codeTiers) {
		List<Object[]> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers) {
			List<Object[]> result = null;
			Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}

	@Override
	public List<TaPanier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPanier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPanier value) throws Exception {
		BeanValidator<TaPanier> validator = new BeanValidator<TaPanier>(TaPanier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPanier value, String propertyName)
			throws Exception {
		BeanValidator<TaPanier> validator = new BeanValidator<TaPanier>(TaPanier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPanier transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public List<TaPanierDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaPanier.QN.FIND_ALL_LIGHT);
			List<TaPanierDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaPanierDTO> findByCodeLight(String code) {
		logger.debug("getting TaPanierDTO instance");
		try {		
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaPanier.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeDocument", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaPanier.QN.FIND_ALL_LIGHT);
			}

			List<TaPanierDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaPanier> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<TaPanier> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaPanier> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Date> findDateExport(Date DateDeb, Date DateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaPanier> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return "select r   from TaRDocument r   where r.taPanier = "+prefixe+"   and ( r.taFacture IS NOT NULL)";
	}

	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return "doc.dateLivDocument";
	}

	@Override
	public String getRequeteARelancer() {
		// TODO Auto-generated method stub
		return " and doc.date_Liv_Document <= :dateRef and doc.date_Liv_Document >= :dateJour ";
	}
	
	@Override
	public String getRequeteARelancerJPQL() {
		// TODO Auto-generated method stub
		return " and doc.dateLivDocument <= :dateRef and doc.dateLivDocument >= :dateJour ";
	}


	public TaPanier findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaPanier a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaPanier instance = (TaPanier)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public IDocumentTiers findByCodeFetch(String code) {
		logger.debug("getting TaPanier instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaPanier a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.codeDocument='"+code+"' " +
					" order by l.numLigneLDocument");
		
			TaPanier instance = (TaPanier)query.getSingleResult();
			instance.setLegrain(true);

			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public IDocumentTiers findByIdFetch(int id) {
		logger.debug("getting TaPanier instance with id: " + id);
		try {
			
			TaPanier p = entityManager.find(TaPanier.class, id);
			if(p!=null) {
				/*
				 * Lors des paiements Stripe, entre autre paiement d'un panier via la boutique et webhook stripe,
				 * quand on accède à l'objet TaPanier à partir de l'objet TaStripePayementIntent, 
				 * la première lignes du panier est "doublée" les find qui arrive après renvoit le même objet panier
				 * puisque c'est la meme transaction et le meme entityManager, donc cela ne permet pas de corriger cette ligne en double.
				 * En détachant l'objet cela permet surement de forcer une nouvelle requete au leiu d'utiliser le cacje de l'entityManager 
				 * et dans cas, il n'y a plus la ligne en double.
				 * TODO voir pourquoi les lignes sont doublés dans ce cas là, alors que les annotations sur TaPanier sont les mêmes que celles des autres document.
				 * Peut être par ce que le premier accès au panier dans le webhook se fait a partir d'un TaStripePayementIntent avec un panier en LAZY ?
				 */
				entityManager.detach(p); 
			}
			
			Query query = entityManager.createQuery("select a from TaPanier a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
//					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.idDocument="+id+" " +
					" order by l.numLigneLDocument");
			TaPanier instance = (TaPanier)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	

}

