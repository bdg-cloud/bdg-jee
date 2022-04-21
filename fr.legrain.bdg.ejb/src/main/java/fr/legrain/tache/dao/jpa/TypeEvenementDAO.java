package fr.legrain.tache.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tache.dao.ITypeEvenementDAO;
import fr.legrain.tache.model.TaTypeEvenement;
import fr.legrain.tache.model.TaTypeNotification;
import fr.legrain.validator.BeanValidator;

public class TypeEvenementDAO implements ITypeEvenementDAO {

	static Logger logger = Logger.getLogger(TypeEvenementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTypeEvenement a";
	
	public TypeEvenementDAO(){
	}

//	public TaTypeEvenement refresh(TaTypeEvenement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTypeEvenement.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaTypeEvenement transientInstance) {
		logger.debug("persisting TaTypeEvenement instance");
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
	public void remove(TaTypeEvenement persistentInstance) {
		logger.debug("removing TaTypeEvenement instance");
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
	public TaTypeEvenement merge(TaTypeEvenement detachedInstance) {
		logger.debug("merging TaTypeEvenement instance");
		try {
			TaTypeEvenement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaTypeEvenement findById(int id) {
		logger.debug("getting TaTypeEvenement instance with id: " + id);
		try {
			TaTypeEvenement instance = entityManager.find(TaTypeEvenement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaTypeEvenement> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTypeEvenement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypeEvenement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTypeEvenement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeEvenement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeEvenement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypeEvenement value) throws Exception {
		BeanValidator<TaTypeEvenement> validator = new BeanValidator<TaTypeEvenement>(TaTypeEvenement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeEvenement value, String propertyName) throws Exception {
		BeanValidator<TaTypeEvenement> validator = new BeanValidator<TaTypeEvenement>(TaTypeEvenement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeEvenement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTypeEvenement findByCode(String code) {
		logger.debug("getting TaTiers instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaTypeEvenement a where a.codeTypeEvenement='"+code+"'");
				TaTypeEvenement instance = (TaTypeEvenement)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaTypeEvenement entity,String field) throws ExceptLgr {	
//		
//	}

}
