package fr.legrain.article.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ITReceptionDAO;
import fr.legrain.article.dto.TaTReceptionDTO;
import fr.legrain.article.model.TaTReception;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTReception.
 * @see fr.legrain.tiers.model.old.TaTReception
 * @author Hibernate Tools
 */
public class TReceptionDAO implements ITReceptionDAO {

	static Logger logger = Logger.getLogger(TReceptionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTReception a order by a.codeTReception";
	
	public TReceptionDAO() {
	}

//	public TaTReception refresh(TaTReception detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTReception.class, detachedInstance.getIdTReception());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTReception transientInstance) {
		logger.debug("persisting TaTReception instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTReception persistentInstance) {
		logger.debug("removing TaTReception instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTReception merge(TaTReception detachedInstance) {
		logger.debug("merging TaTReception instance");
		try {
			TaTReception result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTReception findById(int id) {
		logger.debug("getting TaTReception instance with id: " + id);
		try {
			TaTReception instance = entityManager.find(TaTReception.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTReception findByCode(String code) {
		logger.debug("getting TaTReception instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTReception f where UPPER(f.codeTReception)='"+code.toUpperCase()+"'");
			TaTReception instance = (TaTReception)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaTReception f where f.codeTReception='"+code+"'");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTReception> selectAll() {
		logger.debug("selectAll TaTReception");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTReception> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaTReceptionDTO> findByCodeLight(String code) {
		logger.debug("getting TaTFabricationDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTReception.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTReception", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTReception.QN.FIND_ALL_LIGHT);
			}

			List<TaTReceptionDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTReception> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTReception> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTReception value) throws Exception {
		BeanValidator<TaTReception> validator = new BeanValidator<TaTReception>(TaTReception.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTReception value, String propertyName) throws Exception {
		BeanValidator<TaTReception> validator = new BeanValidator<TaTReception>(TaTReception.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTReception transientInstance) {
		entityManager.detach(transientInstance);
	}

//	public void ctrlSaisieSpecifique(TaTReception entity,String field) throws ExceptLgr {	
//		
//	}
}
