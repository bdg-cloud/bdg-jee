package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITLiensDAO;
import fr.legrain.tiers.model.TaTLiens;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.model.old.TaTLiens
 * @author Hibernate Tools
 */
public class TLiensDAO implements ITLiensDAO {

	static Logger logger = Logger.getLogger(TLiensDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTLiens a";
	
	public TLiensDAO() {
	}
	
//	public TaTLiens refresh(TaTLiens detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTLiens.class, detachedInstance.getIdTLiens());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTLiens transientInstance) {
		logger.debug("persisting TaTLiens instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTLiens persistentInstance) {
		logger.debug("removing TaTLiens instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTLiens()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTLiens merge(TaTLiens detachedInstance) {
		logger.debug("merging TaTLiens instance");
		try {
			TaTLiens result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTLiens findById(int id) {
		logger.debug("getting TaTLiens instance with id: " + id);
		try {
			TaTLiens instance = entityManager.find(TaTLiens.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTLiens findByCode(String code) {
		logger.debug("getting TaTLiens instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTLiens f where f.codeTLiens='"+code+"'");
			TaTLiens instance = (TaTLiens)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}	
	
	@Override
	public List<TaTLiens> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLiens");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTLiens> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTLiens> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTLiens> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTLiens> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTLiens> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTLiens value) throws Exception {
		BeanValidator<TaTLiens> validator = new BeanValidator<TaTLiens>(TaTLiens.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTLiens value, String propertyName) throws Exception {
		BeanValidator<TaTLiens> validator = new BeanValidator<TaTLiens>(TaTLiens.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTLiens transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTLiens entity,String field) throws ExceptLgr {	
//		
//	}
}
