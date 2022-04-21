package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITTelDAO;
import fr.legrain.tiers.model.TaTTel;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTTel.
 * @see fr.legrain.tiers.model.old.TaTTel
 * @author Hibernate Tools
 */
public class TTelDAO implements ITTelDAO {

	static Logger logger = Logger.getLogger(TTelDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTTel a order by a.codeTTel";
	
	public TTelDAO() {
	}

//	public TaTTel refresh(TaTTel detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTTel.class, detachedInstance.getIdTTel());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTTel transientInstance) {
		logger.debug("persisting TaTTel instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTTel persistentInstance) {
		logger.debug("removing TaTTel instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTTel merge(TaTTel detachedInstance) {
		logger.debug("merging TaTTel instance");
		try {
			TaTTel result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTTel findById(int id) {
		logger.debug("getting TaTTel instance with id: " + id);
		try {
			TaTTel instance = entityManager.find(TaTTel.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTTel findByCode(String code) {
		logger.debug("getting TaTTel instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTTel f where UPPER(f.codeTTel)='"+code.toUpperCase()+"'");
			TaTTel instance = (TaTTel)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaTTel f where f.codeTTel='"+code+"'");
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
	
	@Override
	public List<TaTTel> selectAll() {
		logger.debug("selectAll TaTTel");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTTel> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTTel> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTTel> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTTel> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTTel> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTTel value) throws Exception {
		BeanValidator<TaTTel> validator = new BeanValidator<TaTTel>(TaTTel.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTTel value, String propertyName) throws Exception {
		BeanValidator<TaTTel> validator = new BeanValidator<TaTTel>(TaTTel.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTTel transientInstance) {
		entityManager.detach(transientInstance);
	}

//	public void ctrlSaisieSpecifique(TaTTel entity,String field) throws ExceptLgr {	
//		
//	}
}
