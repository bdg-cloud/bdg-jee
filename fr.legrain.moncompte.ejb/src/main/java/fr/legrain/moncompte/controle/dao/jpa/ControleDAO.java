package fr.legrain.moncompte.controle.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.moncompte.controle.dao.IControleDAO;
import fr.legrain.moncompte.controle.model.TaControle;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaControle.
 * @see fr.legrain.tiers.model.old.TaControle
 * @author Hibernate Tools
 */
public class ControleDAO implements IControleDAO {

	static Logger logger = Logger.getLogger(ControleDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaControle a";
	
	public ControleDAO(){
	}

//	public TaControle refresh(TaControle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaControle.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaControle transientInstance) {
		logger.debug("persisting TaControle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	@Override
	public void remove(TaControle persistentInstance) {
		logger.debug("removing TaControle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	@Override
	public TaControle merge(TaControle detachedInstance) {
		logger.debug("merging TaControle instance");
		try {
			TaControle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}


	@Override
	public TaControle findById(int id) {
		logger.debug("getting TaControle instance with id: " + id);
		try {
			TaControle instance = entityManager.find(TaControle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaControle> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaControle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaControle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaControle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaControle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaControle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaControle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaControle value) throws Exception {
		BeanValidator<TaControle> validator = new BeanValidator<TaControle>(TaControle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaControle value, String propertyName) throws Exception {
		BeanValidator<TaControle> validator = new BeanValidator<TaControle>(TaControle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaControle transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaControle findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaControle entity,String field) throws ExceptLgr {	
//		
//	}

}
