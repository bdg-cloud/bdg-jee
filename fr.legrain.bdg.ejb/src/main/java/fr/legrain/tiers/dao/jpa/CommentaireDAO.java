package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ICommentaireDAO;
import fr.legrain.tiers.model.TaCommentaire;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCommentaire.
 * @see fr.legrain.tiers.model.old.TaCommentaire
 * @author Hibernate Tools
 */
public class CommentaireDAO implements ICommentaireDAO {

	static Logger logger = Logger.getLogger(CommentaireDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaCommentaire a";
	
	public CommentaireDAO() {
	}
	
	public void persist(TaCommentaire transientInstance) {
		logger.debug("persisting TaCommentaire instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaCommentaire refresh(TaCommentaire detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaCommentaire.class, detachedInstance.getIdCommentaire());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaCommentaire persistentInstance) {
		logger.debug("removing TaCommentaire instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCommentaire merge(TaCommentaire detachedInstance) {
		logger.debug("merging TaCommentaire instance");
		try {
			TaCommentaire result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCommentaire findById(int id) {
		logger.debug("getting TaCommentaire instance with id: " + id);
		try {
			TaCommentaire instance = entityManager.find(TaCommentaire.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaCommentaire> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaCommentaire");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCommentaire> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaCommentaire> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCommentaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCommentaire> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCommentaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCommentaire value) throws Exception {
		BeanValidator<TaCommentaire> validator = new BeanValidator<TaCommentaire>(TaCommentaire.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCommentaire value, String propertyName) throws Exception {
		BeanValidator<TaCommentaire> validator = new BeanValidator<TaCommentaire>(TaCommentaire.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCommentaire transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaCommentaire findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaCommentaire entity,String field) throws ExceptLgr {	
//		
//	}
}
