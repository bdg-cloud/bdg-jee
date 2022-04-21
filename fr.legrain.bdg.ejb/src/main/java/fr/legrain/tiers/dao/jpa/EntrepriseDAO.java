package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IEntrepriseDAO;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaEntreprise.
 * @see fr.legrain.tiers.model.old.TaEntreprise
 * @author Hibernate Tools
 */
public class EntrepriseDAO implements IEntrepriseDAO {

	static Logger logger = Logger.getLogger(EntrepriseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaEntreprise a";
	
	public EntrepriseDAO() {
	}

	public void persist(TaEntreprise transientInstance) {
		logger.debug("persisting TaEntreprise instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaEntreprise refresh(TaEntreprise detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaEntreprise.class, detachedInstance.getIdEntreprise());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaEntreprise persistentInstance) {
		logger.debug("removing TaEntreprise instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaEntreprise merge(TaEntreprise detachedInstance) {
		logger.debug("merging TaEntreprise instance");
		try {
			TaEntreprise result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaEntreprise findById(int id) {
		logger.debug("getting TaEntreprise instance with id: " + id);
		try {
			TaEntreprise instance = entityManager.find(TaEntreprise.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEntreprise> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEntreprise");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEntreprise> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEntreprise> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEntreprise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEntreprise> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEntreprise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEntreprise value) throws Exception {
		BeanValidator<TaEntreprise> validator = new BeanValidator<TaEntreprise>(TaEntreprise.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEntreprise value, String propertyName) throws Exception {
		BeanValidator<TaEntreprise> validator = new BeanValidator<TaEntreprise>(TaEntreprise.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEntreprise transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaEntreprise findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaEntreprise entity,String field) throws ExceptLgr {	
//		
//	}
}
