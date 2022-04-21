package fr.legrain.article.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ITReceptionDAO;
import fr.legrain.article.dao.ITransporteurDAO;
import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

public class TransporteurDAO implements ITransporteurDAO {

	static Logger logger = Logger.getLogger(TransporteurDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTransporteur a order by a.codeTransporteur";
	
	public TransporteurDAO() {
	}

//	public TaTransporteur refresh(TaTransporteur detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTransporteur.class, detachedInstance.getIdTReception());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTransporteur transientInstance) {
		logger.debug("persisting TaTransporteur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTransporteur persistentInstance) {
		logger.debug("removing TaTransporteur instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTransporteur merge(TaTransporteur detachedInstance) {
		logger.debug("merging TaTransporteur instance");
		try {
			TaTransporteur result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTransporteur findById(int id) {
		logger.debug("getting TaTransporteur instance with id: " + id);
		try {
			TaTransporteur instance = entityManager.find(TaTransporteur.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTransporteur findByCode(String code) {
		logger.debug("getting TaTransporteur instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTransporteur f where UPPER(f.codeTransporteur)='"+code.toUpperCase()+"'");
			TaTransporteur instance = (TaTransporteur)query.getSingleResult();
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
			Query query = entityManager.createQuery("select count(f) from TaTransporteur f where f.codeTransporteur='"+code+"'");
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
	public List<TaTransporteur> selectAll() {
		logger.debug("selectAll TaTransporteur");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTransporteur> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaTransporteurDTO> findByCodeLight(String code) {
		logger.debug("getting TaTFabricationDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTransporteur.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTransporteur", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTransporteur.QN.FIND_ALL_LIGHT);
			}

			List<TaTransporteurDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTransporteur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTransporteur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTransporteur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTransporteur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTransporteur value) throws Exception {
		BeanValidator<TaTransporteur> validator = new BeanValidator<TaTransporteur>(TaTransporteur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTransporteur value, String propertyName) throws Exception {
		BeanValidator<TaTransporteur> validator = new BeanValidator<TaTransporteur>(TaTransporteur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTransporteur transientInstance) {
		entityManager.detach(transientInstance);
	}

//	public void ctrlSaisieSpecifique(TaTransporteur entity,String field) throws ExceptLgr {	
//		
//	}
}
