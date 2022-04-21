package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITWebDAO;
import fr.legrain.tiers.model.TaTWeb;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTWeb.
 * @see fr.legrain.tiers.model.old.TaTWeb
 * @author Hibernate Tools
 */
public class TWebDAO implements ITWebDAO {

	static Logger logger = Logger.getLogger(TaTWeb.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTWeb a order by a.codeTWeb";
	
	public TWebDAO() {
	}
	
//	public TaTWeb refresh(TaTWeb detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTWeb.class, detachedInstance.getIdTWeb());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTWeb transientInstance) {
		logger.debug("persisting TaTWeb instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTWeb persistentInstance) {
		logger.debug("removing TaTWeb instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTWeb()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTWeb merge(TaTWeb detachedInstance) {
		logger.debug("merging TaTWeb instance");
		try {
			TaTWeb result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTWeb findById(int id) {
		logger.debug("getting TaTWeb instance with id: " + id);
		try {
			TaTWeb instance = entityManager.find(TaTWeb.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTWeb findByCode(String code) {
		logger.debug("getting TaTWeb instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTWeb f where f.codeTWeb='"+code+"'");
			TaTWeb instance = (TaTWeb)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	@Override
	public List<TaTWeb> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLiens");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTWeb> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTWeb> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTWeb> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTWeb> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTWeb> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTWeb value) throws Exception {
		BeanValidator<TaTWeb> validator = new BeanValidator<TaTWeb>(TaTWeb.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTWeb value, String propertyName) throws Exception {
		BeanValidator<TaTWeb> validator = new BeanValidator<TaTWeb>(TaTWeb.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTWeb transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTWeb entity,String field) throws ExceptLgr {	
//		
//	}
}
