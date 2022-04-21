package fr.legrain.documents.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaEndToEnd;

/**
 * Home object for domain model class TaEndToEnd.
 * @see fr.legrain.documents.dao.TaEndToEnd
 * @author Hibernate Tools
 */
public class TaEndToEndDAO /*extends AbstractApplicationDAO<TaEndToEnd>*/ {

	static Logger logger = Logger.getLogger(TaEndToEndDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaEndToEnd a";
	
	public TaEndToEndDAO(){
//		this(null);
	}
	
//	public TaEndToEndDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaEndToEnd.class.getSimpleName());
//		initChampId(TaEndToEnd.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaEndToEnd());
//	}
//	public TaEndToEnd refresh(TaEndToEnd detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaEndToEnd.class, detachedInstance.getIdTDoc());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaEndToEnd transientInstance) {
		logger.debug("persisting TaEndToEnd instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaEndToEnd persistentInstance) {
		logger.debug("removing TaEndToEnd instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaEndToEnd merge(TaEndToEnd detachedInstance) {
		logger.debug("merging TaEndToEnd instance");
		try {
			TaEndToEnd result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaEndToEnd findById(int id) {
		logger.debug("getting TaEndToEnd instance with id: " + id);
		try {
			TaEndToEnd instance = entityManager.find(TaEndToEnd.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaEndToEnd findByCode(String code) {
		logger.debug("getting TaEndToEnd instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaEndToEnd a where a.codeEndToEnd='"+code+"'");
			TaEndToEnd instance = (TaEndToEnd)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaEndToEnd f where f.codeEndToEnd='"+code+"'");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaEndToEnd> selectAll() {
		logger.debug("selectAll TaEndToEnd");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEndToEnd> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
}
