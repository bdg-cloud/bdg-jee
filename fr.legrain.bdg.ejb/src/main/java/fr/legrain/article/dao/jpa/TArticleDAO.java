package fr.legrain.article.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ITArticleDAO;
import fr.legrain.article.model.TaTArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTNoteArticle.
 * @see fr.legrain.tiers.dao.TaTitreTransport
 * @author Hibernate Tools
 */
public class TArticleDAO implements ITArticleDAO {

	//private static final Log logger = LogFactory.getLog(TaTNoteArticle.class);
	static Logger logger = Logger.getLogger(TArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTArticle a";
	
	public TArticleDAO() {
//		this(null);
	}
	
//	public TaTArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTNoteArticle.class.getSimpleName());
//		initChampId(TaTArticle.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTArticle());
//	}
	
//	public TaTArticle refresh(TaTArticle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTArticle.class, detachedInstance.getIdTArticle());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTArticle transientInstance) {
		logger.debug("persisting TaTArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTArticle persistentInstance) {
		logger.debug("removing TaTArticle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTArticle merge(TaTArticle detachedInstance) {
		logger.debug("merging TaTArticle instance");
		try {
			TaTArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTArticle findById(int id) {
		logger.debug("getting TaTArticle instance with id: " + id);
		try {
			TaTArticle instance = entityManager.find(TaTArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTArticle findByCode(String code) {
		logger.debug("getting TaTArticle instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTArticle f where f.codeTArticle='"+code+"'");
			TaTArticle instance = (TaTArticle)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaTArticle> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTArticle value) throws Exception {
		BeanValidator<TaTArticle> validator = new BeanValidator<TaTArticle>(TaTArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTArticle value, String propertyName) throws Exception {
		BeanValidator<TaTArticle> validator = new BeanValidator<TaTArticle>(TaTArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTArticle transientInstance) {
		entityManager.detach(transientInstance);
	}
	

}
