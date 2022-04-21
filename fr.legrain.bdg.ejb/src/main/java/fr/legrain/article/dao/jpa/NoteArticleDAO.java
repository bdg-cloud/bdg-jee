package fr.legrain.article.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.INoteArticleDAO;
import fr.legrain.article.model.TaNoteArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaNoteArticle.
 * @see fr.legrain.tiers.dao.TaNoteArticle
 * @author Hibernate Tools
 */
public class NoteArticleDAO implements INoteArticleDAO {

	private static final Log logger = LogFactory.getLog(NoteArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaNoteDAO.class.getName());
	
	private String defaultJPQLQuery = "select u from TaNoteArticle u";
	
	public NoteArticleDAO(){
//		this(null);
	}

//	public TaNoteArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaNoteArticle.class.getSimpleName());
//		initChampId(TaNoteArticle.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaNoteArticle());
//	}
	
//	public TaNoteArticle refresh(TaNoteArticle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaNoteArticle.class, detachedInstance.getIdNoteArticle());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaNoteArticle transientInstance) {
		logger.debug("persisting TaNoteArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaNoteArticle persistentInstance) {
		logger.debug("removing TaNoteArticle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaNoteArticle merge(TaNoteArticle detachedInstance) {
		logger.debug("merging TaNoteArticle instance");
		try {
			TaNoteArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaNoteArticle findById(int id) {
		logger.debug("getting TaNoteArticle instance with id: " + id);
		try {
			TaNoteArticle instance = entityManager.find(TaNoteArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaNoteArticle> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaNoteArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaNoteArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public void ctrlSaisieSpecifique(TaNoteArticle entity,String field) throws ExceptLgr {	
		
	}
	
	@Override
	public List<TaNoteArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaNoteArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaNoteArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaNoteArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaNoteArticle value) throws Exception {
		BeanValidator<TaNoteArticle> validator = new BeanValidator<TaNoteArticle>(TaNoteArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaNoteArticle value, String propertyName) throws Exception {
		BeanValidator<TaNoteArticle> validator = new BeanValidator<TaNoteArticle>(TaNoteArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaNoteArticle transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaNoteArticle findByCode(String code) {
		return null;
	}
	
}
