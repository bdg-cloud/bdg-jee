package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ILimitationDAO;
import fr.legrain.moncompte.model.TaLimitation;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.validator.BeanValidator;


public class LimitationDAO implements ILimitationDAO {

	private static final Log logger = LogFactory.getLog(LimitationDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaLimitation p";
	
	public LimitationDAO(){
//		this(null);
	}



	public void persist(TaLimitation transientInstance) {
		logger.debug("persisting TaLimitation instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaLimitation persistentInstance) {
		logger.debug("removing TaLimitation instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaLimitation merge(TaLimitation detachedInstance) {
		logger.debug("merging TaLimitation instance");
		try {
			TaLimitation result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaLimitation findById(int id) {
		logger.debug("getting TaLimitation instance with id: " + id);
		try {
			TaLimitation instance = entityManager.find(TaLimitation.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLimitation> selectAll() {
		logger.debug("selectAll TaLimitation");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLimitation> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaLimitation entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaLimitation> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLimitation> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLimitation> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLimitation> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLimitation value) throws Exception {
		BeanValidator<TaLimitation> validator = new BeanValidator<TaLimitation>(TaLimitation.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLimitation value, String propertyName) throws Exception {
		BeanValidator<TaLimitation> validator = new BeanValidator<TaLimitation>(TaLimitation.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLimitation transientInstance) {
		entityManager.detach(transientInstance);
	}



	@Override
	public TaLimitation findByCode(String typeLimite) {
		logger.debug("getting TaLimitation instance with typeLimite: " + typeLimite);
		try {
			if(!typeLimite.equals("")){
			Query query = entityManager.createQuery("select f from TaLimitation f where f.typeLimite='"+typeLimite+"'");
			TaLimitation instance = (TaLimitation)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	
	
}

