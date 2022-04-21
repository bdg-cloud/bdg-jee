package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.IAutorisationDAO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.validator.BeanValidator;


public class AutorisationDAO implements IAutorisationDAO {

	private static final Log logger = LogFactory.getLog(AutorisationDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaAutorisation p";
	
	public AutorisationDAO(){
//		this(null);
	}




	public void persist(TaAutorisation transientInstance) {
		logger.debug("persisting TaAutorisation instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaAutorisation persistentInstance) {
		logger.debug("removing TaAutorisation instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdAutorisation()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaAutorisation merge(TaAutorisation detachedInstance) {
		logger.debug("merging TaAutorisation instance");
		try {
			TaAutorisation result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaAutorisation findById(int id) {
		logger.debug("getting TaAutorisation instance with id: " + id);
		try {
			TaAutorisation instance = entityManager.find(TaAutorisation.class, id);
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
	public List<TaAutorisation> selectAll() {
		logger.debug("selectAll TaAutorisation");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAutorisation> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	public List<TaAutorisation> selectAllAutorisationCategoriePro(int idCategoriePro) {
//		logger.debug("selectAll TaAutorisation");
//		try {
//			Query ejbQuery = entityManager.createQuery("select p from TaCategoriePro c join c.listeAutorisation p where c.id="+idCategoriePro);
//			List<TaAutorisation> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}

	public void ctrlSaisieSpecifique(TaAutorisation entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaAutorisation> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAutorisation> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAutorisation> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAutorisation> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAutorisation value) throws Exception {
		BeanValidator<TaAutorisation> validator = new BeanValidator<TaAutorisation>(TaAutorisation.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAutorisation value, String propertyName) throws Exception {
		BeanValidator<TaAutorisation> validator = new BeanValidator<TaAutorisation>(TaAutorisation.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAutorisation transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaAutorisation findByCode(String code) {
		return null;
//		logger.debug("getting TaAutorisation instance with username: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaAutorisation f where f.code='"+code+"'");
//			TaAutorisation instance = (TaAutorisation)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
	}
	
}

