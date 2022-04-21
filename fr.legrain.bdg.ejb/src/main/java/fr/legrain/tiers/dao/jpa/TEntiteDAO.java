package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITEntiteDAO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaTEntite;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTEntite.
 * @see fr.legrain.tiers.model.old.TaTEntite
 * @author Hibernate Tools
 */
public class TEntiteDAO implements ITEntiteDAO {

	static Logger logger = Logger.getLogger(TEntiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTEntite a order by a.codeTEntite";
	
	public TEntiteDAO() {
	}
	
//	public TaTEntite refresh(TaTEntite detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTEntite.class, detachedInstance.getIdTEntite());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTEntite transientInstance) {
		logger.debug("persisting TaTEntite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTEntite persistentInstance) {
		logger.debug("removing TaTEntite instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTEntite merge(TaTEntite detachedInstance) {
		logger.debug("merging TaTEntite instance");
		try {
			TaTEntite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTEntite findById(int id) {
		logger.debug("getting TaTEntite instance with id: " + id);
		try {
			TaTEntite instance = entityManager.find(TaTEntite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTEntite findByCode(String code) {
		logger.debug("getting TaTEntite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTEntite f where UPPER(f.codeTEntite)='"+code.toUpperCase()+"'");
			TaTEntite instance = (TaTEntite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}

	// Dima - Début 
	public List<TaTEntiteDTO> findByCodeLight(String code) {
		logger.debug("getting TaTEntite instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaTEntite f where UPPER(f.codeTEntite)='"+code.toUpperCase()+"'");
//			TaTEntite instance = (TaTEntite)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			return null;
//		}
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTEntite.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTEntite", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTEntite.QN.FIND_ALL_LIGHT);
			}

			List<TaTEntiteDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	// Dima -  Fin
	
	@Override
	public List<TaTEntite> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTEntite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTEntite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTEntite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTEntite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}
	
	@Override
	public List<TaTEntite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTEntite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTEntite value) throws Exception {
		BeanValidator<TaTEntite> validator = new BeanValidator<TaTEntite>(TaTEntite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTEntite value, String propertyName) throws Exception {
		BeanValidator<TaTEntite> validator = new BeanValidator<TaTEntite>(TaTEntite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTEntite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaTEntite entity,String field) throws ExceptLgr {	
//		
//	}
}
