package fr.legrain.articles.dao;

// Generated Aug 11, 2008 5:00:48 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;

/**
 * Home object for domain model class TaTva.
 * @see fr.legrain.articles.dao.TaTva
 * @author Hibernate Tools
 */
public class TaTvaDAO /* extends AbstractApplicationDAO<TaTva>*/ {

	private static final Log logger = LogFactory.getLog(TaTvaDAO.class);
	//static Logger logger = Logger.getLogger(TaTvaDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaTva f";
	
	public TaTvaDAO(){
//		this(null);
	}
	
//	public TaTvaDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTva.class.getSimpleName());
//		initChampId(TaTva.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTva());
//	}
	
//	public TaTva refresh(TaTva detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTva.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	protected void persist(TaTva transientInstance) {
		logger.debug("persisting TaTva instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	protected void remove(TaTva persistentInstance) {
		logger.debug("removing TaTva instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	protected TaTva merge(TaTva detachedInstance) {
		logger.debug("merging TaTva instance");
		try {
			TaTva result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTva findById(int id) {
		logger.debug("getting TaTva instance with id: " + id);
		try {
			TaTva instance = entityManager.find(TaTva.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTva findByCode(String code) {
		logger.debug("getting TaTva instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTva f where f.codeTva='"+code+"'");
				TaTva instance = (TaTva)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne un code code TVA sur vente (commence par V), pour un taux particulier
	 * @param taux
	 * @return
	 */
	public TaTva findByTaux(String taux) {
		logger.debug("getting TaTva instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaTva f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				TaTva instance = (TaTva)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne un code code TVA sur vente (commence par V), pour un taux particulier,
	 * si aucun code de TVA sur vente ne correspond à ce taux, un nouveau code est créé.
	 * @param taux
	 * @return
	 */
	public TaTva findByTauxOrCreate(String taux) {
		logger.debug("getting TaTva instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaTva f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				if(query.getResultList().isEmpty()) {
					//Aucun code de TVA de vente existe avec ce taux => création d'un nouveau code TVA
//					EntityTransaction tx = entityManager.getTransaction();
//					tx.begin();
					TaTva tva = new TaTva();
					tva.setTauxTva(LibConversion.stringToBigDecimal(taux));
					tva.setLibelleTva("TVA à "+taux);
					
					//Recherche du prochain numéro pour créer le code automatiquement V1,V2, ... V?
					Query q = entityManager.createQuery("select f from TaTva f where f.codeTva like 'V%'");
					List<TaTva> listeTvaVente = q.getResultList();
					String debut = null;
					int max = 0;
					for (TaTva taTva : listeTvaVente) {
						debut = taTva.getCodeTva().substring(1);
						int i = LibConversion.stringToInteger(debut);
						if(i>max) max=i;
					}
					max++;
					
					tva.setCodeTva("V"+max);
					tva.setNumcptTva("445");
					try {
//						enregistrerMerge(tva);
//						commit(tx);
					} catch (Exception e) {
						logger.error("", e);
					}
					return tva;
				} else {
					TaTva instance = (TaTva) query.getResultList().get(0);
					return instance;
				}

			}
			return null;
		} catch (NoResultException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaTva> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTva> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTva entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
