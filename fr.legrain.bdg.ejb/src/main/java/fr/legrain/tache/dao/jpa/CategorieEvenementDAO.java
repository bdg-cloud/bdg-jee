package fr.legrain.tache.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tache.dao.ICategorieEvenementDAO;
import fr.legrain.tache.model.TaCategorieEvenement;
import fr.legrain.validator.BeanValidator;

public class CategorieEvenementDAO implements ICategorieEvenementDAO {

	static Logger logger = Logger.getLogger(CategorieEvenementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaCategorieEvenement a";
	
	public CategorieEvenementDAO(){
	}

//	public TaCategorieEvenement refresh(TaCategorieEvenement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaCategorieEvenement.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaCategorieEvenement transientInstance) {
		logger.debug("persisting TaCategorieEvenement instance");
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
	public void remove(TaCategorieEvenement persistentInstance) {
		logger.debug("removing TaCategorieEvenement instance");
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
	public TaCategorieEvenement merge(TaCategorieEvenement detachedInstance) {
		logger.debug("merging TaCategorieEvenement instance");
		try {
			TaCategorieEvenement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaCategorieEvenement findById(int id) {
		logger.debug("getting TaCategorieEvenement instance with id: " + id);
		try {
			TaCategorieEvenement instance = entityManager.find(TaCategorieEvenement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaCategorieEvenement> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaCategorieEvenement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCategorieEvenement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaCategorieEvenement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCategorieEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCategorieEvenement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCategorieEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCategorieEvenement value) throws Exception {
		BeanValidator<TaCategorieEvenement> validator = new BeanValidator<TaCategorieEvenement>(TaCategorieEvenement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCategorieEvenement value, String propertyName) throws Exception {
		BeanValidator<TaCategorieEvenement> validator = new BeanValidator<TaCategorieEvenement>(TaCategorieEvenement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCategorieEvenement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaCategorieEvenement findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaCategorieEvenement entity,String field) throws ExceptLgr {	
//		
//	}

}
