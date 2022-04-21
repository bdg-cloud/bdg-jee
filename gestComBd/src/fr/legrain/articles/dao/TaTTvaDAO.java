package fr.legrain.articles.dao;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;

/**
 * Home object for domain model class TaTTva.
 * @see fr.legrain.articles.dao.TaTTva
 * @author Hibernate Tools
 */
public class TaTTvaDAO /*extends AbstractApplicationDAO<TaTTva>*/ {

	private static final Log logger = LogFactory.getLog(TaTTvaDAO.class);
	//static Logger logger = Logger.getLogger(TaTTvaDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select t from TaTTva t";
	
	public TaTTvaDAO(){
//		this(null);
	}
	
//	public TaTTvaDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTTva.class.getSimpleName());
//		initChampId(TaTTva.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTTva());
//	}
	
//	public TaTTva refresh(TaTTva detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTTva.class, detachedInstance.getIdTTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	protected void persist(TaTTva transientInstance) {
		logger.debug("persisting TaTTva instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	protected void remove(TaTTva persistentInstance) {
		logger.debug("removing TaTTva instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	protected TaTTva merge(TaTTva detachedInstance) {
		logger.debug("merging TaTTva instance");
		try {
			TaTTva result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTTva findById(int id) {
		logger.debug("getting TaTTva instance with id: " + id);
		try {
			TaTTva instance = entityManager.find(TaTTva.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTTva findByCode(String code) {
		logger.debug("getting TaTTva instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTTva f where f.codeTTva='"+code+"'");
			TaTTva instance = (TaTTva)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaTTva> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTTva> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaTTva entity,String field) throws ExceptLgr {	
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
