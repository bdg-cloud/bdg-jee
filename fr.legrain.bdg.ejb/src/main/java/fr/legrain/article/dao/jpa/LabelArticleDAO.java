package fr.legrain.article.dao.jpa;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ILabelArticleDAO;
import fr.legrain.article.model.TaLabelArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLabelArticle.
 * @see fr.legrain.articles.dao.TaLabelArticle
 * @author Hibernate Tools
 */
public class LabelArticleDAO implements ILabelArticleDAO {

	private static final Log logger = LogFactory.getLog(LabelArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaLabelArticle p";
	
	public LabelArticleDAO(){
//		this(null);
	}

//	public TaLabelArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaLabelArticle.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLabelArticle());
//	}


	public void persist(TaLabelArticle transientInstance) {
		logger.debug("persisting TaLabelArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaLabelArticle refresh(TaLabelArticle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLabelArticle.class, detachedInstance.getIdLabelArticle());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaLabelArticle persistentInstance) {
		logger.debug("removing TaLabelArticle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaLabelArticle merge(TaLabelArticle detachedInstance) {
		logger.debug("merging TaLabelArticle instance");
		try {
			TaLabelArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaLabelArticle findById(int id) {
		logger.debug("getting TaLabelArticle instance with id: " + id);
		try {
			TaLabelArticle instance = entityManager.find(TaLabelArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLabelArticle findByCode(String code) {
		logger.debug("getting TaLabelArticle instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaLabelArticle a where a.codeLabelArticle='"+code+"'");
			TaLabelArticle instance = (TaLabelArticle)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}	
	
	/**
	 * Liste des labels créé ou modifié par partir de la date passée en paramètre.
	 * @param d
	 * @return
	 */
	public List<TaLabelArticle> findByNewOrUpdatedAfter(Date d) {
//		logger.debug("getting TaArticle instance with quandCreeLabelArticle or quandModifLabelArticle >= " + d);
//		try {
//			List<TaLabelArticle> l = null;
//			if(d!=null) {
//				Query query = entityManager.createNamedQuery(TaLabelArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER);
//				query.setParameter("dateDebut", d);
//				query.setParameter("dateFin", d);
//				l = query.getResultList();
//				logger.debug("get successful");
//			}
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLabelArticle> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLabelArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaLabelArticle entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaLabelArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLabelArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLabelArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLabelArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLabelArticle value) throws Exception {
		BeanValidator<TaLabelArticle> validator = new BeanValidator<TaLabelArticle>(TaLabelArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLabelArticle value, String propertyName) throws Exception {
		BeanValidator<TaLabelArticle> validator = new BeanValidator<TaLabelArticle>(TaLabelArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLabelArticle transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}

