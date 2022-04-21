package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.model.TaTCPaiement;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTCPaiement.
 * @see fr.legrain.tiers.model.old.TaTCPaiement
 * @author Hibernate Tools
 */
public class TCPaiementDAO implements ITCPaiementDAO {

	static Logger logger = Logger.getLogger(TCPaiementDAO.class);

	private String defaultJPQLQuery = "select a from TaTCPaiement a";
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	public TCPaiementDAO() {
	}
	
//	public TaTCPaiement refresh(TaTCPaiement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTCPaiement.class, detachedInstance.getIdTCPaiement());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTCPaiement transientInstance) {
		logger.debug("persisting TaTCPaiement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTCPaiement persistentInstance) {
		logger.debug("removing TaTCPaiement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTCPaiement merge(TaTCPaiement detachedInstance) {
		logger.debug("merging TaTCPaiement instance");
		try {
			TaTCPaiement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTCPaiement findById(int id) {
		logger.debug("getting TaTCPaiement instance with id: " + id);
		try {
			TaTCPaiement instance = entityManager.find(TaTCPaiement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTCPaiement findByCode(String code) {
		logger.debug("getting TaTCPaiement instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTCPaiement f where f.codeTCPaiement='"+code+"'");
			TaTCPaiement instance = (TaTCPaiement)query.getSingleResult();
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
			Query query = entityManager.createQuery("select count(f) from TaTCPaiement f where f.codeTCPaiement='"+code+"'");
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
	public List<TaTCPaiement> selectAll() {
		logger.debug("selectAll TaTCPaiement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTCPaiement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTCPaiement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTCPaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTCPaiement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTCPaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTCPaiement value) throws Exception {
		BeanValidator<TaTCPaiement> validator = new BeanValidator<TaTCPaiement>(TaTCPaiement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTCPaiement value, String propertyName) throws Exception {
		BeanValidator<TaTCPaiement> validator = new BeanValidator<TaTCPaiement>(TaTCPaiement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTCPaiement transientInstance) {
		entityManager.detach(transientInstance);
	}

//	public void ctrlSaisieSpecifique(TaTCPaiement entity,String field) throws ExceptLgr {	
//		
//	}
}
