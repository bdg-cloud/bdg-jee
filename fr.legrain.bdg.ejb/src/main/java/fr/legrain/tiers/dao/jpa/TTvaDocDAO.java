package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITTvaDocDAO;
import fr.legrain.tiers.model.TaTTvaDoc;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTTvaDoc.
 * @see fr.legrain.tiers.model.old.TaTTvaDoc
 * @author Hibernate Tools
 */
public class TTvaDocDAO implements ITTvaDocDAO {

	static Logger logger = Logger.getLogger(TTvaDocDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTTvaDoc a ";
	
	public TTvaDocDAO() {
	}
	
//	public TaTTvaDoc refresh(TaTTvaDoc detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTTvaDoc.class, detachedInstance.getIdTTvaDoc());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTTvaDoc transientInstance) {
		logger.debug("persisting TaTTvaDoc instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTTvaDoc persistentInstance) {
		logger.debug("removing TaTTvaDoc instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTTvaDoc merge(TaTTvaDoc detachedInstance) {
		logger.debug("merging TaTTvaDoc instance");
		try {
			TaTTvaDoc result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTTvaDoc findById(int id) {
		logger.debug("getting TaTTvaDoc instance with id: " + id);
		try {
			TaTTvaDoc instance = entityManager.find(TaTTvaDoc.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTTvaDoc findByCode(String code) {
		logger.debug("getting TaTTvaDoc instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTTvaDoc f where f.codeTTvaDoc='"+code+"'");
			TaTTvaDoc instance = (TaTTvaDoc)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	@Override
	public List<TaTTvaDoc> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTTvaDoc");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTTvaDoc> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTTvaDoc> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTTvaDoc> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTTvaDoc> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTTvaDoc> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTTvaDoc value) throws Exception {
		BeanValidator<TaTTvaDoc> validator = new BeanValidator<TaTTvaDoc>(TaTTvaDoc.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTTvaDoc value, String propertyName) throws Exception {
		BeanValidator<TaTTvaDoc> validator = new BeanValidator<TaTTvaDoc>(TaTTvaDoc.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTTvaDoc transientInstance) {
		entityManager.detach(transientInstance);
	}
//	public void ctrlSaisieSpecifique(TaTTvaDoc entity,String field) throws ExceptLgr {	
//		
//	}
}
