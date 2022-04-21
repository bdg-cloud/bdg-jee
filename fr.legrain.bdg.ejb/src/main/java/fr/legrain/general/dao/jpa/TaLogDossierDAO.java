package fr.legrain.general.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.general.dao.ITaLogDossierDAO;
import fr.legrain.general.model.TaLogDossier;
import fr.legrain.validator.BeanValidator;

/**

 */
public class TaLogDossierDAO implements ITaLogDossierDAO {

	static Logger logger = Logger.getLogger(TaLogDossierDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaLogDossier a order by a.quand DESC";
	
	public TaLogDossierDAO() {
	}
	
//	public TaLogDossier refresh(TaLogDossier detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLogDossier.class, detachedInstance.getIdTaLogDossier());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLogDossier transientInstance) {
		logger.debug("persisting TaLogDossier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLogDossier persistentInstance) {
		logger.debug("removing TaLogDossier instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLogDossier merge(TaLogDossier detachedInstance) {
		logger.debug("merging TaLogDossier instance");
		try {
			TaLogDossier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLogDossier findById(int id) {
		logger.debug("getting TaLogDossier instance with id: " + id);
		try {
			TaLogDossier instance = entityManager.find(TaLogDossier.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLogDossier findByCode(String code) {
		logger.debug("getting TaLogDossier instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLogDossier f where f.alias='"+code+"'");
			TaLogDossier instance = (TaLogDossier)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLogDossier findByUUID(String uuid) {
		logger.debug("getting TaLogDossier instance with uuid: " + uuid);
		try {
			if(!uuid.equals("")){
			Query query = entityManager.createQuery("select f from TaLogDossier f where f.uuid='"+uuid+"'");
			TaLogDossier instance = (TaLogDossier)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	@Override
	public List<TaLogDossier> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLogDossier");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLogDossier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLogDossier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLogDossier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLogDossier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLogDossier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLogDossier value) throws Exception {
		BeanValidator<TaLogDossier> validator = new BeanValidator<TaLogDossier>(TaLogDossier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLogDossier value, String propertyName) throws Exception {
		BeanValidator<TaLogDossier> validator = new BeanValidator<TaLogDossier>(TaLogDossier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLogDossier transientInstance) {
		entityManager.detach(transientInstance);
	}
//	public void ctrlSaisieSpecifique(TaLogDossier entity,String field) throws ExceptLgr {	
//		
//	}
}
