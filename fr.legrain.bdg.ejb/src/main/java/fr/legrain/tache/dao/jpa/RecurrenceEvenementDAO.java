package fr.legrain.tache.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tache.dao.IRecurrenceEvenementDAO;
import fr.legrain.tache.model.TaRecurrenceEvenement;
import fr.legrain.validator.BeanValidator;

public class RecurrenceEvenementDAO implements IRecurrenceEvenementDAO {

	static Logger logger = Logger.getLogger(RecurrenceEvenementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaRecurrenceEvenement a";
	
	public RecurrenceEvenementDAO(){
	}

//	public TaRecurrenceEvenement refresh(TaRecurrenceEvenement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaRecurrenceEvenement.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaRecurrenceEvenement transientInstance) {
		logger.debug("persisting TaRecurrenceEvenement instance");
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
	public void remove(TaRecurrenceEvenement persistentInstance) {
		logger.debug("removing TaRecurrenceEvenement instance");
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
	public TaRecurrenceEvenement merge(TaRecurrenceEvenement detachedInstance) {
		logger.debug("merging TaRecurrenceEvenement instance");
		try {
			TaRecurrenceEvenement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaRecurrenceEvenement findById(int id) {
		logger.debug("getting TaRecurrenceEvenement instance with id: " + id);
		try {
			TaRecurrenceEvenement instance = entityManager.find(TaRecurrenceEvenement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaRecurrenceEvenement> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaRecurrenceEvenement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRecurrenceEvenement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaRecurrenceEvenement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRecurrenceEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRecurrenceEvenement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRecurrenceEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRecurrenceEvenement value) throws Exception {
		BeanValidator<TaRecurrenceEvenement> validator = new BeanValidator<TaRecurrenceEvenement>(TaRecurrenceEvenement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRecurrenceEvenement value, String propertyName) throws Exception {
		BeanValidator<TaRecurrenceEvenement> validator = new BeanValidator<TaRecurrenceEvenement>(TaRecurrenceEvenement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRecurrenceEvenement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaRecurrenceEvenement findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaRecurrenceEvenement entity,String field) throws ExceptLgr {	
//		
//	}

}
