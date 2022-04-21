package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITEmailDAO;
import fr.legrain.tiers.model.TaTEmail;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTEmail.
 * @see fr.legrain.tiers.model.old.TaTEmail
 * @author Hibernate Tools
 */
public class TEmailDAO implements ITEmailDAO {

	static Logger logger = Logger.getLogger(TEmailDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTEmail a";
	
	public TEmailDAO() {
	}
	
//	public TaTEmail refresh(TaTEmail detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTEmail.class, detachedInstance.getIdTEmail());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTEmail transientInstance) {
		logger.debug("persisting TaTEmail instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTEmail persistentInstance) {
		logger.debug("removing TaTEmail instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTEmail()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTEmail merge(TaTEmail detachedInstance) {
		logger.debug("merging TaTEmail instance");
		try {
			TaTEmail result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTEmail findById(int id) {
		logger.debug("getting TaTEmail instance with id: " + id);
		try {
			TaTEmail instance = entityManager.find(TaTEmail.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTEmail findByCode(String code) {
		logger.debug("getting TaTEmail instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTEmail f where f.codeTEmail='"+code+"'");
			TaTEmail instance = (TaTEmail)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	@Override
	public List<TaTEmail> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTEmail");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTEmail> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTEmail> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTEmail> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTEmail value) throws Exception {
		BeanValidator<TaTEmail> validator = new BeanValidator<TaTEmail>(TaTEmail.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTEmail value, String propertyName) throws Exception {
		BeanValidator<TaTEmail> validator = new BeanValidator<TaTEmail>(TaTEmail.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTEmail transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTEmail entity,String field) throws ExceptLgr {	
//		
//	}
}
