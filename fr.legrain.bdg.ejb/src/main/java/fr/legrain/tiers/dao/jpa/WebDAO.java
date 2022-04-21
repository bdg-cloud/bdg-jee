package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IWebDAO;
import fr.legrain.tiers.model.TaWeb;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaWeb.
 * @see fr.legrain.tiers.model.old.TaWeb
 * @author Hibernate Tools
 */
public class WebDAO implements IWebDAO {

	static Logger logger = Logger.getLogger(WebDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select u from TaWeb u";
	
	public WebDAO(){
	}

//	public TaWeb refresh(TaWeb detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaWeb.class, detachedInstance.getIdWeb());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaWeb transientInstance) {
		logger.debug("persisting TaWeb instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaWeb persistentInstance) {
		logger.debug("removing TaWeb instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaWeb merge(TaWeb detachedInstance) {
		logger.debug("merging TaWeb instance");
		try {
			TaWeb result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaWeb findById(int id) {
		logger.debug("getting TaWeb instance with id: " + id);
		try {
			TaWeb instance = entityManager.find(TaWeb.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaWeb> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaWeb");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaWeb> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaWeb> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaWeb> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaWeb> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaWeb> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaWeb value) throws Exception {
		BeanValidator<TaWeb> validator = new BeanValidator<TaWeb>(TaWeb.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaWeb value, String propertyName) throws Exception {
		BeanValidator<TaWeb> validator = new BeanValidator<TaWeb>(TaWeb.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaWeb transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaWeb findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaWeb entity,String field) throws ExceptLgr {	
//		
//	}
}
