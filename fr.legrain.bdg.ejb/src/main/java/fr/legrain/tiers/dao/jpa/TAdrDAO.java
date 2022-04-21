package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITAdrDAO;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaTAdr.
 * @see fr.legrain.tiers.model.old.TaTAdr
 * @author Hibernate Tools
 */
public class TAdrDAO implements ITAdrDAO {

	static Logger logger = Logger.getLogger(TAdrDAO.class);

	private String defaultJPQLQuery = "select a from TaTAdr a";
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	public TAdrDAO() {
		
	}
	
	
//	public TaTAdr refresh(TaTAdr detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTAdr.class, detachedInstance.getIdTAdr());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTAdr transientInstance) {
		logger.debug("persisting TaTAdr instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTAdr persistentInstance) {
		logger.debug("removing TaTAdr instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTAdr merge(TaTAdr detachedInstance) {
		logger.debug("merging TaTAdr instance");
		try {
			TaTAdr result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTAdr findById(int id) {
		logger.debug("getting TaTAdr instance with id: " + id);
		try {
			TaTAdr instance = entityManager.find(TaTAdr.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTAdr findByCode(String code) {
		logger.debug("getting TaTAdr instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTAdr f where UPPER(f.codeTAdr)='"+ code.toUpperCase()+"'");
			TaTAdr instance = (TaTAdr)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}

	@Override
	public List<TaTAdr> selectAll() {
		logger.debug("selectAll TaTAdr");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTAdr> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTAdr> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTAdr> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTAdr> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTAdr> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTAdr value) throws Exception {
		BeanValidator<TaTAdr> validator = new BeanValidator<TaTAdr>(TaTAdr.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTAdr value, String propertyName) throws Exception {
		BeanValidator<TaTAdr> validator = new BeanValidator<TaTAdr>(TaTAdr.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTAdr transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTAdr entity,String field) throws ExceptLgr {	
//		
//	}
}
