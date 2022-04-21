package fr.legrain.tache.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tache.dao.INotificationDAO;
import fr.legrain.tache.model.TaNotification;
import fr.legrain.tache.model.TaTypeNotification;
import fr.legrain.validator.BeanValidator;

public class NotificationDAO implements INotificationDAO {

	static Logger logger = Logger.getLogger(NotificationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaNotification a";
	
	public NotificationDAO(){
	}

//	public TaNotification refresh(TaNotification detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaNotification.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaNotification transientInstance) {
		logger.debug("persisting TaNotification instance");
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
	public void remove(TaNotification persistentInstance) {
		logger.debug("removing TaNotification instance");
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
	public TaNotification merge(TaNotification detachedInstance) {
		logger.debug("merging TaNotification instance");
		try {
			TaNotification result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaNotification findById(int id) {
		logger.debug("getting TaNotification instance with id: " + id);
		try {
			TaNotification instance = entityManager.find(TaNotification.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaNotification> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaNotification");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaNotification> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaNotification> findNotificationInWebAppEnvoyee(Integer idCalendrier, Integer utilisateur, Boolean lu) {
		try {
	
//			String jpql = "select n from TaNotification n join n.taEvenementUtilisateur evt join evt.taAgenda ag where "+utilisateur+" in n.listeUtilisateur.id and ag = "+idCalendrier;
			String jpql = "select n from TaNotification n join n.taEvenementUtilisateur evt join n.listeUtilisateur u join n.taTypeNotification type where u.id="+utilisateur+" and n.notificationEnvoyee = true and type.codeTypeNotification='"+TaTypeNotification.CODE_TYPE_NOTIFICATION_WEBAPP+"'";
			if(lu!=null) {
				if(lu) {
					jpql += " and n.lu=true";
				} else {
					jpql += " and n.lu=false";
				}
			}
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaNotification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	public List<TaNotification> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaNotification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaNotification> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaNotification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaNotification value) throws Exception {
		BeanValidator<TaNotification> validator = new BeanValidator<TaNotification>(TaNotification.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaNotification value, String propertyName) throws Exception {
		BeanValidator<TaNotification> validator = new BeanValidator<TaNotification>(TaNotification.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaNotification transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaNotification findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaNotification entity,String field) throws ExceptLgr {	
//		
//	}

}
