package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITTarifDAO;
import fr.legrain.tiers.model.TaTTarif;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTTarif.
 * @see fr.legrain.tiers.model.old.TaTTarif
 * @author Hibernate Tools
 */
public class TTarifDAO implements ITTarifDAO {

	static Logger logger = Logger.getLogger(TTarifDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTTarif a order by a.codeTTarif";
	
	public TTarifDAO() {
	}
	
//	public TaTTarif refresh(TaTTarif detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTTarif.class, detachedInstance.getIdTTarif());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTTarif transientInstance) {
		logger.debug("persisting TaTTarif instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTTarif persistentInstance) {
		logger.debug("removing TaTTarif instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTTarif()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTTarif merge(TaTTarif detachedInstance) {
		logger.debug("merging TaTTarif instance");
		try {
			TaTTarif result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTTarif findById(int id) {
		logger.debug("getting TaTTarif instance with id: " + id);
		try {
			TaTTarif instance = entityManager.find(TaTTarif.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTTarif findByCode(String code) {
		logger.debug("getting TaTTarif instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaTTarif a where a.codeTTarif='"+code+"'");
			TaTTarif instance = (TaTTarif)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			
			return null;
		}
	}	
	
	@Override
	public List<TaTTarif> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTTarif");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTTarif> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTTarif> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTTarif> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTTarif> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTTarif> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTTarif value) throws Exception {
		BeanValidator<TaTTarif> validator = new BeanValidator<TaTTarif>(TaTTarif.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTTarif value, String propertyName) throws Exception {
		BeanValidator<TaTTarif> validator = new BeanValidator<TaTTarif>(TaTTarif.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTTarif transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTTarif entity,String field) throws ExceptLgr {	
//		
//	}
}
