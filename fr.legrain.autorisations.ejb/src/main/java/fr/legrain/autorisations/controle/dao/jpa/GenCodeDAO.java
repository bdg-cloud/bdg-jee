package fr.legrain.autorisations.controle.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.autorisations.controle.dao.IGenCodeDAO;
import fr.legrain.autorisations.controle.model.TaGenCode;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaGenCode.
 * @see fr.legrain.tiers.model.old.TaGenCode
 * @author Hibernate Tools
 */
public class GenCodeDAO implements IGenCodeDAO {

	static Logger logger = Logger.getLogger(GenCodeDAO.class);
	
	@PersistenceContext(unitName = "autorisations")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaGenCode a";
	
	public GenCodeDAO(){
	}

//	public TaGenCode refresh(TaGenCode detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaGenCode.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaGenCode transientInstance) {
		logger.debug("persisting TaGenCode instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	@Override
	public void remove(TaGenCode persistentInstance) {
		logger.debug("removing TaGenCode instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	@Override
	public TaGenCode merge(TaGenCode detachedInstance) {
		logger.debug("merging TaGenCode instance");
		try {
			TaGenCode result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}


	@Override
	public TaGenCode findById(int id) {
		logger.debug("getting TaGenCode instance with id: " + id);
		try {
			TaGenCode instance = entityManager.find(TaGenCode.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaGenCode> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaGenCode");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaGenCode> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaGenCode> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaGenCode> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaGenCode> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaGenCode> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaGenCode value) throws Exception {
		BeanValidator<TaGenCode> validator = new BeanValidator<TaGenCode>(TaGenCode.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaGenCode value, String propertyName) throws Exception {
		BeanValidator<TaGenCode> validator = new BeanValidator<TaGenCode>(TaGenCode.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaGenCode transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaGenCode findByCode(String code) {
		logger.debug("getting TaGenCode instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaGenCode a where a.entite='"+code+"'");
				TaGenCode instance = (TaGenCode)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaGenCode entity,String field) throws ExceptLgr {	
//		
//	}

}
