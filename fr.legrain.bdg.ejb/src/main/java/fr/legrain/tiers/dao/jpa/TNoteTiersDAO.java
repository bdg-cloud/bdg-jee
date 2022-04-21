package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITNoteTiersDAO;
import fr.legrain.tiers.model.TaTNoteTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTNoteTiers.
 * @see fr.legrain.tiers.model.old.TaTNoteTiers
 * @author Hibernate Tools
 */
public class TNoteTiersDAO implements ITNoteTiersDAO {

	static Logger logger = Logger.getLogger(TaTNoteTiers.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTNoteTiers a";
	
	public TNoteTiersDAO() {
	}
	
//	public TaTNoteTiers refresh(TaTNoteTiers detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTNoteTiers.class, detachedInstance.getIdTNoteTiers());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTNoteTiers transientInstance) {
		logger.debug("persisting TaTNoteTiers instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTNoteTiers persistentInstance) {
		logger.debug("removing TaTNoteTiers instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTNoteTiers()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTNoteTiers merge(TaTNoteTiers detachedInstance) {
		logger.debug("merging TaTNoteTiers instance");
		try {
			TaTNoteTiers result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTNoteTiers findById(int id) {
		logger.debug("getting TaTNoteTiers instance with id: " + id);
		try {
			TaTNoteTiers instance = entityManager.find(TaTNoteTiers.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTNoteTiers findByCode(String code) {
		logger.debug("getting TaTNoteTiers instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTNoteTiers f where f.codeTNoteTiers='"+code+"'");
			TaTNoteTiers instance = (TaTNoteTiers)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	@Override
	public List<TaTNoteTiers> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLiens");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTNoteTiers> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTNoteTiers> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTNoteTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTNoteTiers> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTNoteTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTNoteTiers value) throws Exception {
		BeanValidator<TaTNoteTiers> validator = new BeanValidator<TaTNoteTiers>(TaTNoteTiers.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTNoteTiers value, String propertyName) throws Exception {
		BeanValidator<TaTNoteTiers> validator = new BeanValidator<TaTNoteTiers>(TaTNoteTiers.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTNoteTiers transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTNoteTiers entity,String field) throws ExceptLgr {	
//		
//	}
}
