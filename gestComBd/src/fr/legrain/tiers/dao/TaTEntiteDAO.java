package fr.legrain.tiers.dao;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;

/**
 * Home object for domain model class TaTEntite.
 * @see fr.legrain.tiers.dao.TaTEntite
 * @author Hibernate Tools
 */
public class TaTEntiteDAO {

	//private static final Log logger = LogFactory.getLog(TaTEntiteDAO.class);
	static Logger logger = Logger.getLogger(TaTEntiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTEntite a";
	
	public TaTEntiteDAO() {
//		this(null);
	}
	
//	public TaTEntiteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTEntite.class.getSimpleName());
//		initChampId(TaTEntite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTEntite());
//	}

//	public TaTEntite refresh(TaTEntite detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTEntite.class, detachedInstance.getIdTEntite());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTEntite transientInstance) {
		logger.debug("persisting TaTEntite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaTEntite persistentInstance) {
		logger.debug("removing TaTEntite instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaTEntite merge(TaTEntite detachedInstance) {
		logger.debug("merging TaTEntite instance");
		try {
			TaTEntite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTEntite findById(int id) {
		logger.debug("getting TaTEntite instance with id: " + id);
		try {
			TaTEntite instance = entityManager.find(TaTEntite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTEntite findByCode(String code) {
		logger.debug("getting TaTEntite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTEntite f where f.codeTEntite='"+code+"'");
			TaTEntite instance = (TaTEntite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public List<TaTEntite> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTEntite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTEntite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public void ctrlSaisieSpecifique(TaTEntite entity,String field) throws ExceptLgr {	
		
	}
}
