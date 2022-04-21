package fr.legrain.article.dao.jpa;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.IRTArticleDAO;
import fr.legrain.article.model.TaRTArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCPaiement.
 * @see fr.legrain.tiers.dao.test.TaCPaiement
 * @author Hibernate Tools
 */
public class RTArticleDAO implements IRTArticleDAO{

	static Logger logger = Logger.getLogger(RTArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaRTArticle a";
	
	public RTArticleDAO() {
//		this(null);
	}
	
//	public TaRTArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaRTArticle.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRTArticle());
//	}
	
	public void persist(TaRTArticle transientInstance) {
		logger.debug("persisting TaRTArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaRTArticle refresh(TaRTArticle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaRTArticle.class, detachedInstance.getIdRTArticle());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaRTArticle persistentInstance) {
		logger.debug("removing TaRTArticle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaRTArticle merge(TaRTArticle detachedInstance) {
		logger.debug("merging TaRTArticle instance");
		try {
			TaRTArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaRTArticle findById(int id) {
		logger.debug("getting TaRTArticle instance with id: " + id);
		try {
			TaRTArticle instance = entityManager.find(TaRTArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
//	@Override
	public List<TaRTArticle> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaRTArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRTArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaRTArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRTArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRTArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRTArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRTArticle value) throws Exception {
		BeanValidator<TaRTArticle> validator = new BeanValidator<TaRTArticle>(TaRTArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRTArticle value, String propertyName) throws Exception {
		BeanValidator<TaRTArticle> validator = new BeanValidator<TaRTArticle>(TaRTArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRTArticle transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaRTArticle findByCode(String code) {
		return null;
	}
	
}
