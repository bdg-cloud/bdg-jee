package fr.legrain.paiement.dao.jpa;


import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.paiement.dao.ILogPaiementInternetDAO;
import fr.legrain.paiement.dto.TaLogPaiementInternetDTO;
import fr.legrain.paiement.model.TaLogPaiementInternet;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class LogPaiementInternetDAO implements ILogPaiementInternetDAO {

	static Logger logger = Logger.getLogger(LogPaiementInternetDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLogPaiementInternet a";
	
	public LogPaiementInternetDAO(){
	}

//	public TaLogPaiementInternet refresh(TaLogPaiementInternet detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLogPaiementInternet.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaLogPaiementInternet transientInstance) {
		logger.debug("persisting TaLogPaiementInternet instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaLogPaiementInternet persistentInstance) {
		logger.debug("removing TaLogPaiementInternet instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaLogPaiementInternet merge(TaLogPaiementInternet detachedInstance) {
		logger.debug("merging TaLogPaiementInternet instance");
		try {
			TaLogPaiementInternet result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaLogPaiementInternet findById(int id) {
		logger.debug("getting TaLogPaiementInternet instance with id: " + id);
		try {
			TaLogPaiementInternet instance = entityManager.find(TaLogPaiementInternet.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaLogPaiementInternet> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLogPaiementInternet");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLogPaiementInternet> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLogPaiementInternet> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLogPaiementInternet> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLogPaiementInternet> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLogPaiementInternet> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLogPaiementInternet value) throws Exception {
		BeanValidator<TaLogPaiementInternet> validator = new BeanValidator<TaLogPaiementInternet>(TaLogPaiementInternet.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLogPaiementInternet value, String propertyName) throws Exception {
		BeanValidator<TaLogPaiementInternet> validator = new BeanValidator<TaLogPaiementInternet>(TaLogPaiementInternet.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLogPaiementInternet transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaLogPaiementInternet findByCode(String code) {
		return null;
	}

}
