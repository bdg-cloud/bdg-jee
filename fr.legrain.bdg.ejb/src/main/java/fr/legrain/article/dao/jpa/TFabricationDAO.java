package fr.legrain.article.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ITFabricationDAO;
import fr.legrain.article.dto.TaTFabricationDTO;
import fr.legrain.article.model.TaTFabrication;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTFabrication.
 * @see fr.legrain.tiers.model.old.TaTFabrication
 * @author Hibernate Tools
 */
public class TFabricationDAO implements ITFabricationDAO {

	static Logger logger = Logger.getLogger(TFabricationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTFabrication a order by a.codeTFabrication";
	
	public TFabricationDAO() {
	}

//	public TaTFabrication refresh(TaTFabrication detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTFabrication.class, detachedInstance.getIdTFabrication());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTFabrication transientInstance) {
		logger.debug("persisting TaTFabrication instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTFabrication persistentInstance) {
		logger.debug("removing TaTFabrication instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTFabrication merge(TaTFabrication detachedInstance) {
		logger.debug("merging TaTFabrication instance");
		try {
			TaTFabrication result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTFabrication findById(int id) {
		logger.debug("getting TaTFabrication instance with id: " + id);
		try {
			TaTFabrication instance = entityManager.find(TaTFabrication.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTFabrication findByCode(String code) {
		logger.debug("getting TaTFabrication instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTFabrication f where UPPER(f.codeTFabrication)='"+code.toUpperCase()+"'");
			TaTFabrication instance = (TaTFabrication)query.getSingleResult();
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
			Query query = entityManager.createQuery("select count(f) from TaTFabrication f where f.codeTFabrication='"+code+"'");
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
	public List<TaTFabrication> selectAll() {
		logger.debug("selectAll TaTFabrication");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTFabrication> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaTFabricationDTO> findByCodeLight(String code) {
		logger.debug("getting TaTFabricationDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTFabrication.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTFabrication", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTFabrication.QN.FIND_ALL_LIGHT);
			}

			List<TaTFabricationDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTFabrication> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTFabrication> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTFabrication> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTFabrication> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTFabrication value) throws Exception {
		BeanValidator<TaTFabrication> validator = new BeanValidator<TaTFabrication>(TaTFabrication.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTFabrication value, String propertyName) throws Exception {
		BeanValidator<TaTFabrication> validator = new BeanValidator<TaTFabrication>(TaTFabrication.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTFabrication transientInstance) {
		entityManager.detach(transientInstance);
	}

//	public void ctrlSaisieSpecifique(TaTFabrication entity,String field) throws ExceptLgr {	
//		
//	}
}
