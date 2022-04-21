package fr.legrain.servicewebexterne.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.dao.ITServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaTAdr;
import fr.legrain.tiers.model.TaTEntite;
import fr.legrain.validator.BeanValidator;

public class TServiceWebExterneDAO implements ITServiceWebExterneDAO {

	static Logger logger = Logger.getLogger(TServiceWebExterneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTServiceWebExterne a";
	
	public TServiceWebExterneDAO(){
	}

//	public TaTServiceWebExterne refresh(TaTServiceWebExterne detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTServiceWebExterne.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaTServiceWebExterne transientInstance) {
		logger.debug("persisting TaTServiceWebExterne instance");
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
	public void remove(TaTServiceWebExterne persistentInstance) {
		logger.debug("removing TaTServiceWebExterne instance");
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
	public TaTServiceWebExterne merge(TaTServiceWebExterne detachedInstance) {
		logger.debug("merging TaTServiceWebExterne instance");
		try {
			TaTServiceWebExterne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaTServiceWebExterne findById(int id) {
		logger.debug("getting TaTServiceWebExterne instance with id: " + id);
		try {
			TaTServiceWebExterne instance = entityManager.find(TaTServiceWebExterne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaTServiceWebExterne> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTServiceWebExterne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTServiceWebExterne> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTServiceWebExterne> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTServiceWebExterne> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTServiceWebExterne value) throws Exception {
		BeanValidator<TaTServiceWebExterne> validator = new BeanValidator<TaTServiceWebExterne>(TaTServiceWebExterne.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTServiceWebExterne value, String propertyName) throws Exception {
		BeanValidator<TaTServiceWebExterne> validator = new BeanValidator<TaTServiceWebExterne>(TaTServiceWebExterne.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTServiceWebExterne transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTServiceWebExterne findByCode(String code) {
		logger.debug("getting TaTServiceWebExterne instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTServiceWebExterne f where UPPER(f.codeTServiceWebExterne)='"+ code.toUpperCase()+"'");
			TaTServiceWebExterne instance = (TaTServiceWebExterne)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	public List<TaTServiceWebExterneDTO> findByCodeLight(String code) {
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTServiceWebExterne.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTServiceWebExterne", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTServiceWebExterne.QN.FIND_ALL_LIGHT);
			}

			List<TaTServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
