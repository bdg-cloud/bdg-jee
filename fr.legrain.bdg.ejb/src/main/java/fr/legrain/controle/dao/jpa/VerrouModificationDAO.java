package fr.legrain.controle.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.controle.dao.IVerrouModificationDAO;
import fr.legrain.controle.model.TaVerrouModification;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaVerrouModification.
 * @see fr.legrain.tiers.model.old.TaVerrouModification
 * @author Hibernate Tools
 */
public class VerrouModificationDAO implements IVerrouModificationDAO {

	static Logger logger = Logger.getLogger(VerrouModificationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaVerrouModification a";
	
	public VerrouModificationDAO(){
	}

//	public TaVerrouModification refresh(TaVerrouModification detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaVerrouModification.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaVerrouModification transientInstance) {
		logger.debug("persisting TaVerrouModification instance");
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
	public void remove(TaVerrouModification persistentInstance) {
		logger.debug("removing TaVerrouModification instance");
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
	public TaVerrouModification merge(TaVerrouModification detachedInstance) {
		logger.debug("merging TaVerrouModification instance");
		try {
			TaVerrouModification result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaVerrouModification findById(int id) {
		logger.debug("getting TaVerrouModification instance with id: " + id);
		try {
			TaVerrouModification instance = entityManager.find(TaVerrouModification.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaVerrouModification> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaVerrouModification");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaVerrouModification> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaVerrouModification> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaVerrouModification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaVerrouModification> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaVerrouModification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaVerrouModification value) throws Exception {
		BeanValidator<TaVerrouModification> validator = new BeanValidator<TaVerrouModification>(TaVerrouModification.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaVerrouModification value, String propertyName) throws Exception {
		BeanValidator<TaVerrouModification> validator = new BeanValidator<TaVerrouModification>(TaVerrouModification.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaVerrouModification transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaVerrouModification findByCode(String code) {
		logger.debug("getting TaVerrouModification instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaVerrouModification a where a.entite='"+code+"'");
				TaVerrouModification instance = (TaVerrouModification)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaVerrouModification entity,String field) throws ExceptLgr {	
//		
//	}

}
