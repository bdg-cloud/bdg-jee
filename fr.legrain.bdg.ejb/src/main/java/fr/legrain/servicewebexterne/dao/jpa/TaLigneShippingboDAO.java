package fr.legrain.servicewebexterne.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.servicewebexterne.dao.ITaLigneShippingboDAO;
import fr.legrain.servicewebexterne.model.TaLigneShippingBo;
import fr.legrain.validator.BeanValidator;

public class TaLigneShippingboDAO implements ITaLigneShippingboDAO {
	
static Logger logger = Logger.getLogger(TaLigneShippingboDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLigneShippingBo a";
	
	public TaLigneShippingboDAO(){
	}
	
	@Override
	public void persist(TaLigneShippingBo transientInstance) {
		logger.debug("persisting TaLigneShippingBo instance");
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
	public void remove(TaLigneShippingBo persistentInstance) {
		logger.debug("removing TaLigneShippingBo instance");
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
	public TaLigneShippingBo merge(TaLigneShippingBo detachedInstance) {
		logger.debug("merging TaLigneShippingBo instance");
		try {
			TaLigneShippingBo result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaLigneShippingBo findById(int id) {
		logger.debug("getting TaLigneShippingBo instance with id: " + id);
		try {
			TaLigneShippingBo instance = entityManager.find(TaLigneShippingBo.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaLigneShippingBo> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLigneShippingBo");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLigneShippingBo> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLigneShippingBo> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLigneShippingBo> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLigneShippingBo> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLigneShippingBo> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public boolean validate(TaLigneShippingBo value) throws Exception {
		BeanValidator<TaLigneShippingBo> validator = new BeanValidator<TaLigneShippingBo>(TaLigneShippingBo.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLigneShippingBo value, String propertyName) throws Exception {
		BeanValidator<TaLigneShippingBo> validator = new BeanValidator<TaLigneShippingBo>(TaLigneShippingBo.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLigneShippingBo transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaLigneShippingBo findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}



}
