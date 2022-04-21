package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.INoteTiersDAO;
import fr.legrain.tiers.model.TaNoteTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaNoteTiers.
 * @see fr.legrain.tiers.model.old.TaNoteTiers
 * @author Hibernate Tools
 */
public class NoteTiersDAO implements INoteTiersDAO {

	static Logger logger = Logger.getLogger(NoteTiersDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select u from TaNoteTiers u";
	
	public NoteTiersDAO(){
	}

//	public TaNoteTiers refresh(TaNoteTiers detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaNoteTiers.class, detachedInstance.getIdNoteTiers());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaNoteTiers transientInstance) {
		logger.debug("persisting TaNoteTiers instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaNoteTiers persistentInstance) {
		logger.debug("removing TaNoteTiers instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaNoteTiers merge(TaNoteTiers detachedInstance) {
		logger.debug("merging TaNoteTiers instance");
		try {
			TaNoteTiers result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaNoteTiers findById(int id) {
		logger.debug("getting TaNoteTiers instance with id: " + id);
		try {
			TaNoteTiers instance = entityManager.find(TaNoteTiers.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaNoteTiers> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaNoteTiers");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaNoteTiers> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaNoteTiers> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaNoteTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaNoteTiers> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaNoteTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaNoteTiers value) throws Exception {
		BeanValidator<TaNoteTiers> validator = new BeanValidator<TaNoteTiers>(TaNoteTiers.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaNoteTiers value, String propertyName) throws Exception {
		BeanValidator<TaNoteTiers> validator = new BeanValidator<TaNoteTiers>(TaNoteTiers.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaNoteTiers transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaNoteTiers findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaNoteTiers entity,String field) throws ExceptLgr {	
//		
//	}
}
