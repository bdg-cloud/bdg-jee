package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IInfoJuridiqueDAO;
import fr.legrain.tiers.model.TaInfoJuridique;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaInfoJuridique.
 * @see fr.legrain.tiers.model.old.TaInfoJuridique
 * @author Hibernate Tools
 */
public class InfoJuridiqueDAO implements IInfoJuridiqueDAO {

	static Logger logger = Logger.getLogger(InfoJuridiqueDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfoJuridique a";
	
	public InfoJuridiqueDAO(){
	}

//	public TaInfoJuridique refresh(TaInfoJuridique detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaInfoJuridique.class, detachedInstance.getIdInfoJuridique());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfoJuridique transientInstance) {
		logger.debug("persisting TaInfoJuridique instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaInfoJuridique persistentInstance) {
		logger.debug("removing TaInfoJuridique instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaInfoJuridique merge(TaInfoJuridique detachedInstance) {
		logger.debug("merging TaInfoJuridique instance");
		try {
			TaInfoJuridique result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaInfoJuridique findById(int id) {
		logger.debug("getting TaInfoJuridique instance with id: " + id);
		try {
			TaInfoJuridique instance = entityManager.find(TaInfoJuridique.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaInfoJuridique> selectAll() {
		logger.debug("selectAll TaInfoJuridique");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfoJuridique> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaInfoJuridique> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfoJuridique> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfoJuridique> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfoJuridique> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfoJuridique value) throws Exception {
		BeanValidator<TaInfoJuridique> validator = new BeanValidator<TaInfoJuridique>(TaInfoJuridique.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInfoJuridique value, String propertyName) throws Exception {
		BeanValidator<TaInfoJuridique> validator = new BeanValidator<TaInfoJuridique>(TaInfoJuridique.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInfoJuridique transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaInfoJuridique findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaInfoJuridique entity,String field) throws ExceptLgr {	
//		
//	}

}
