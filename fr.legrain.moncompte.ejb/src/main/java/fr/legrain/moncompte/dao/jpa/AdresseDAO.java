package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.IAdresseDAO;
import fr.legrain.moncompte.model.TaAdresse;
import fr.legrain.validator.BeanValidator;


public class AdresseDAO implements IAdresseDAO {

	private static final Log logger = LogFactory.getLog(AdresseDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaAdresse p";
	
	public AdresseDAO(){
//		this(null);
	}



	public void persist(TaAdresse transientInstance) {
		logger.debug("persisting TaAdresse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaAdresse persistentInstance) {
		logger.debug("removing TaAdresse instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdAdresse()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaAdresse merge(TaAdresse detachedInstance) {
		logger.debug("merging TaAdresse instance");
		try {
			TaAdresse result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaAdresse findById(int id) {
		logger.debug("getting TaAdresse instance with id: " + id);
		try {
			TaAdresse instance = entityManager.find(TaAdresse.class, id);
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
	public List<TaAdresse> selectAll() {
		logger.debug("selectAll TaAdresse");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAdresse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaAdresse entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaAdresse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAdresse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAdresse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAdresse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAdresse value) throws Exception {
		BeanValidator<TaAdresse> validator = new BeanValidator<TaAdresse>(TaAdresse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAdresse value, String propertyName) throws Exception {
		BeanValidator<TaAdresse> validator = new BeanValidator<TaAdresse>(TaAdresse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAdresse transientInstance) {
		entityManager.detach(transientInstance);
	}



	@Override
	public TaAdresse findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}

