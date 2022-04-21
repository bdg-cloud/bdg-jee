package fr.legrain.article.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ITNoteArticleDAO;
import fr.legrain.article.model.TaTNoteArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTNoteArticle.
 * @see fr.legrain.tiers.dao.TaTitreTransport
 * @author Hibernate Tools
 */
public class TNoteArticleDAO implements ITNoteArticleDAO{

	//private static final Log logger = LogFactory.getLog(TaTNoteArticle.class);
	static Logger logger = Logger.getLogger(TaTNoteArticle.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTNoteArticle a order by a.codeTNoteArticle";
	
	public TNoteArticleDAO() {
//		this(null);
	}
	
//	public TaTNoteArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTNoteArticle.class.getSimpleName());
//		initChampId(TaTNoteArticle.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTNoteArticle());
//	}
	
//	public TaTNoteArticle refresh(TaTNoteArticle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTNoteArticle.class, detachedInstance.getIdTNoteArticle());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTNoteArticle transientInstance) {
		logger.debug("persisting TaTNoteArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTNoteArticle persistentInstance) {
		logger.debug("removing TaTNoteArticle instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTNoteArticle()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTNoteArticle merge(TaTNoteArticle detachedInstance) {
		logger.debug("merging TaTNoteArticle instance");
		try {
			TaTNoteArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTNoteArticle findById(int id) {
		logger.debug("getting TaTNoteArticle instance with id: " + id);
		try {
			TaTNoteArticle instance = entityManager.find(TaTNoteArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTNoteArticle findByCode(String code) {
		logger.debug("getting TaTNoteArticle instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTNoteArticle f where f.codeTNoteArticle='"+code+"'");
			TaTNoteArticle instance = (TaTNoteArticle)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
//	@Override
	public List<TaTNoteArticle> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLiens");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTNoteArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public void ctrlSaisieSpecifique(TaTNoteArticle entity,String field) throws ExceptLgr {	
		
	}
	
	@Override
	public List<TaTNoteArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTNoteArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTNoteArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTNoteArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTNoteArticle value) throws Exception {
		BeanValidator<TaTNoteArticle> validator = new BeanValidator<TaTNoteArticle>(TaTNoteArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTNoteArticle value, String propertyName) throws Exception {
		BeanValidator<TaTNoteArticle> validator = new BeanValidator<TaTNoteArticle>(TaTNoteArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTNoteArticle transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
