package fr.legrain.article.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ITTransportDAO;
import fr.legrain.article.dto.TaTTransportDTO;
import fr.legrain.article.model.TaTTransport;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

public class TTransportDAO implements ITTransportDAO {

	static Logger logger = Logger.getLogger(TTransportDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTTransport a order by a.codeTTransport";
	
	public TTransportDAO() {
	}

//	public TaTTransport refresh(TaTTransport detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTTransport.class, detachedInstance.getIdTReception());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTTransport transientInstance) {
		logger.debug("persisting TaTTransport instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTTransport persistentInstance) {
		logger.debug("removing TaTTransport instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTTransport merge(TaTTransport detachedInstance) {
		logger.debug("merging TaTTransport instance");
		try {
			TaTTransport result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTTransport findById(int id) {
		logger.debug("getting TaTTransport instance with id: " + id);
		try {
			TaTTransport instance = entityManager.find(TaTTransport.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTTransport findByCode(String code) {
		logger.debug("getting TaTTransport instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTTransport f where UPPER(f.codeTTransport)='"+code.toUpperCase()+"'");
			TaTTransport instance = (TaTTransport)query.getSingleResult();
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
			Query query = entityManager.createQuery("select count(f) from TaTTransport f where f.codeTTransport='"+code+"'");
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
	public List<TaTTransport> selectAll() {
		logger.debug("selectAll TaTTransport");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTTransport> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaTTransportDTO> findByCodeLight(String code) {
		logger.debug("getting TaTFabricationDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTTransport.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTTransport", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTTransport.QN.FIND_ALL_LIGHT);
			}

			List<TaTTransportDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTTransport> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTTransport> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTTransport> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTTransport> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTTransport value) throws Exception {
		BeanValidator<TaTTransport> validator = new BeanValidator<TaTTransport>(TaTTransport.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTTransport value, String propertyName) throws Exception {
		BeanValidator<TaTTransport> validator = new BeanValidator<TaTTransport>(TaTTransport.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTTransport transientInstance) {
		entityManager.detach(transientInstance);
	}

//	public void ctrlSaisieSpecifique(TaTTransport entity,String field) throws ExceptLgr {	
//		
//	}
}
