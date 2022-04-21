package fr.legrain.tiers.dao.jpa;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITBanqueDAO;
import fr.legrain.tiers.model.TaTBanque;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTBanque.
 * @see fr.legrain.tiers.dao.old.test.TaTBanque
 * @author Hibernate Tools
 */
public class TBanqueDAO implements ITBanqueDAO {

	static Logger logger = Logger.getLogger(TBanqueDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTBanque a order by a.codeTBanque";
	
	public TBanqueDAO() {
	}

//	public TaTBanque refresh(TaTBanque detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTBanque.class, detachedInstance.getIdTBanque());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void persist(TaTBanque transientInstance) {
		logger.debug("persisting TaTBanque instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTBanque persistentInstance) {
		logger.debug("removing TaTBanque instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTBanque merge(TaTBanque detachedInstance) {
		logger.debug("merging TaTBanque instance");
		try {
			TaTBanque result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTBanque findById(int id) {
		logger.debug("getting TaTBanque instance with id: " + id);
		try {
			TaTBanque instance = entityManager.find(TaTBanque.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTBanque findByCode(String code) {
		logger.debug("getting TaTBanque instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaTBanque a where a.codeTBanque='"+code+"'");
			TaTBanque instance = (TaTBanque)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	@Override
	public List<TaTBanque> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTBanque");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTBanque> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTBanque> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTBanque> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTBanque> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTBanque> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTBanque value) throws Exception {
		BeanValidator<TaTBanque> validator = new BeanValidator<TaTBanque>(TaTBanque.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTBanque value, String propertyName) throws Exception {
		BeanValidator<TaTBanque> validator = new BeanValidator<TaTBanque>(TaTBanque.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTBanque transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTBanque entity,String field) throws ExceptLgr {	
//		
//	}
}
