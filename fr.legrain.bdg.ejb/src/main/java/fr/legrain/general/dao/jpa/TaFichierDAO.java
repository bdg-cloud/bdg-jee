package fr.legrain.general.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.general.dao.ITaFichierDAO;
import fr.legrain.general.model.TaFichier;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaFichier.
 * @see fr.legrain.tiers.model.old.TaFichier
 * @author Hibernate Tools
 * @modifier par Dima
 */
public class TaFichierDAO implements ITaFichierDAO {

	static Logger logger = Logger.getLogger(TaFichierDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaFichier a ";
	
	public TaFichierDAO() {
	}
	
//	public TaFichier refresh(TaFichier detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaFichier.class, detachedInstance.getIdTaFichier());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaFichier transientInstance) {
		logger.debug("persisting TaFichier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaFichier persistentInstance) {
		logger.debug("removing TaFichier instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaFichier merge(TaFichier detachedInstance) {
		logger.debug("merging TaFichier instance");
		try {
			TaFichier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaFichier findById(int id) {
		logger.debug("getting TaFichier instance with id: " + id);
		try {
			TaFichier instance = entityManager.find(TaFichier.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaFichier findByCode(String code) {
		logger.debug("getting TaFichier instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaFichier f where f.codeTaFichier='"+code+"'");
			TaFichier instance = (TaFichier)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaFichier> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaFichier");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFichier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaFichier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFichier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFichier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFichier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFichier value) throws Exception {
		BeanValidator<TaFichier> validator = new BeanValidator<TaFichier>(TaFichier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFichier value, String propertyName) throws Exception {
		BeanValidator<TaFichier> validator = new BeanValidator<TaFichier>(TaFichier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFichier transientInstance) {
		entityManager.detach(transientInstance);
	}
//	public void ctrlSaisieSpecifique(TaFichier entity,String field) throws ExceptLgr {	
//		
//	}
}
