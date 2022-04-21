package fr.legrain.email.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.email.dao.IMessageEmailDAO;
import fr.legrain.email.dao.IPiecesJointesEmailDAO;
import fr.legrain.email.model.TaPieceJointeEmail;
import fr.legrain.validator.BeanValidator;

public class PiecesJointesEmailDAO implements IPiecesJointesEmailDAO {

	static Logger logger = Logger.getLogger(PiecesJointesEmailDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaPieceJointeEmail a";
	
	public PiecesJointesEmailDAO(){
	}

//	public TaPieceJointeEmail refresh(TaPieceJointeEmail detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaPieceJointeEmail.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaPieceJointeEmail transientInstance) {
		logger.debug("persisting TaPieceJointeEmail instance");
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
	public void remove(TaPieceJointeEmail persistentInstance) {
		logger.debug("removing TaPieceJointeEmail instance");
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
	public TaPieceJointeEmail merge(TaPieceJointeEmail detachedInstance) {
		logger.debug("merging TaPieceJointeEmail instance");
		try {
			TaPieceJointeEmail result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaPieceJointeEmail findById(int id) {
		logger.debug("getting TaPieceJointeEmail instance with id: " + id);
		try {
			TaPieceJointeEmail instance = entityManager.find(TaPieceJointeEmail.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaPieceJointeEmail> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaPieceJointeEmail");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPieceJointeEmail> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaPieceJointeEmail> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPieceJointeEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPieceJointeEmail> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPieceJointeEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPieceJointeEmail value) throws Exception {
		BeanValidator<TaPieceJointeEmail> validator = new BeanValidator<TaPieceJointeEmail>(TaPieceJointeEmail.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPieceJointeEmail value, String propertyName) throws Exception {
		BeanValidator<TaPieceJointeEmail> validator = new BeanValidator<TaPieceJointeEmail>(TaPieceJointeEmail.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPieceJointeEmail transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaPieceJointeEmail findByCode(String code) {
		return null;
	}

}
