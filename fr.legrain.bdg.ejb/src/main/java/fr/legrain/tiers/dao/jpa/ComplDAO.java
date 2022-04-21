package fr.legrain.tiers.dao.jpa;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IComplDAO;
import fr.legrain.tiers.model.TaCompl;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCompl.
 * @see fr.legrain.tiers.dao.old.test.TaCompl
 * @author Hibernate Tools
 */
public class ComplDAO implements IComplDAO {

	static Logger logger = Logger.getLogger(ComplDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaCompl a";
	
	public ComplDAO() {
	}

	public void persist(TaCompl transientInstance) {
		logger.debug("persisting TaCompl instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

//	public TaCompl refresh(TaCompl detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaCompl.class, detachedInstance.getIdCompl());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void remove(TaCompl persistentInstance) {
		logger.debug("removing TaCompl instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCompl merge(TaCompl detachedInstance) {
		logger.debug("merging TaCompl instance");
		try {
			TaCompl result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCompl findById(int id) {
		logger.debug("getting TaCompl instance with id: " + id);
		try {
			TaCompl instance = entityManager.find(TaCompl.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaCompl> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaCompl");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCompl> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaCompl> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCompl> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCompl> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCompl> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCompl value) throws Exception {
		BeanValidator<TaCompl> validator = new BeanValidator<TaCompl>(TaCompl.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCompl value, String propertyName) throws Exception {
		BeanValidator<TaCompl> validator = new BeanValidator<TaCompl>(TaCompl.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCompl transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaCompl findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaCompl entity,String field) throws ExceptLgr {	
//		
//	}
}

