package fr.legrain.email.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.email.dao.IEtiquetteEmailDAO;
import fr.legrain.email.model.TaEtiquetteEmail;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.validator.BeanValidator;

public class EtiquetteEmailDAO implements IEtiquetteEmailDAO {

	static Logger logger = Logger.getLogger(EtiquetteEmailDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaEtiquetteEmail a order by a.ordre";
	
	public EtiquetteEmailDAO(){
	}

//	public TaEtiquetteEmail refresh(TaEtiquetteEmail detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaEtiquetteEmail.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaEtiquetteEmail transientInstance) {
		logger.debug("persisting TaEtiquetteEmail instance");
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
	public void remove(TaEtiquetteEmail persistentInstance) {
		logger.debug("removing TaEtiquetteEmail instance");
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
	public TaEtiquetteEmail merge(TaEtiquetteEmail detachedInstance) {
		logger.debug("merging TaEtiquetteEmail instance");
		try {
			TaEtiquetteEmail result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaEtiquetteEmail findById(int id) {
		logger.debug("getting TaEtiquetteEmail instance with id: " + id);
		try {
			TaEtiquetteEmail instance = entityManager.find(TaEtiquetteEmail.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEtiquetteEmail> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEtiquetteEmail");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEtiquetteEmail> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEtiquetteEmail> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEtiquetteEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEtiquetteEmail> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEtiquetteEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEtiquetteEmail value) throws Exception {
		BeanValidator<TaEtiquetteEmail> validator = new BeanValidator<TaEtiquetteEmail>(TaEtiquetteEmail.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEtiquetteEmail value, String propertyName) throws Exception {
		BeanValidator<TaEtiquetteEmail> validator = new BeanValidator<TaEtiquetteEmail>(TaEtiquetteEmail.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEtiquetteEmail transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaEtiquetteEmail findByCode(String code) {
		logger.debug("getting TaTiers instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaEtiquetteEmail a where a.code='"+code+"'");
				TaEtiquetteEmail instance = (TaEtiquetteEmail)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}

}
