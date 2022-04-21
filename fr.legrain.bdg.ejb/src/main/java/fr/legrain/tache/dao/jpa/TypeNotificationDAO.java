package fr.legrain.tache.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tache.dao.ITypeNotificationDAO;
import fr.legrain.tache.model.TaTypeNotification;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class TypeNotificationDAO implements ITypeNotificationDAO {

	static Logger logger = Logger.getLogger(TypeNotificationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTypeNotification a";
	
	public TypeNotificationDAO(){
	}

//	public TaTypeNotification refresh(TaTypeNotification detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTypeNotification.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaTypeNotification transientInstance) {
		logger.debug("persisting TaTypeNotification instance");
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
	public void remove(TaTypeNotification persistentInstance) {
		logger.debug("removing TaTypeNotification instance");
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
	public TaTypeNotification merge(TaTypeNotification detachedInstance) {
		logger.debug("merging TaTypeNotification instance");
		try {
			TaTypeNotification result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaTypeNotification findById(int id) {
		logger.debug("getting TaTypeNotification instance with id: " + id);
		try {
			TaTypeNotification instance = entityManager.find(TaTypeNotification.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaTypeNotification> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTypeNotification");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypeNotification> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTypeNotification> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeNotification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeNotification> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeNotification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypeNotification value) throws Exception {
		BeanValidator<TaTypeNotification> validator = new BeanValidator<TaTypeNotification>(TaTypeNotification.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeNotification value, String propertyName) throws Exception {
		BeanValidator<TaTypeNotification> validator = new BeanValidator<TaTypeNotification>(TaTypeNotification.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeNotification transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTypeNotification findByCode(String code) {
		logger.debug("getting TaTiers instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaTypeNotification a where a.codeTypeNotification='"+code+"'");
				TaTypeNotification instance = (TaTypeNotification)query.getSingleResult();
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
	
//	public void ctrlSaisieSpecifique(TaTypeNotification entity,String field) throws ExceptLgr {	
//		
//	}

}
