package fr.legrain.article.dao.jpa;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IMarqueArticleDAO;
import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaMarqueArticle.
 * @see fr.legrain.articles.dao.TaMarqueArticle
 * @author Hibernate Tools
 */
public class MarqueArticleDAO implements IMarqueArticleDAO {

	private static final Log logger = LogFactory.getLog(MarqueArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaMarqueArticle p";
	
	public MarqueArticleDAO(){
//		this(null);
	}

//	public TaMarqueArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaMarqueArticle.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaMarqueArticle());
//	}


	public void persist(TaMarqueArticle transientInstance) {
		logger.debug("persisting TaMarqueArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaMarqueArticle refresh(TaMarqueArticle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaMarqueArticle.class, detachedInstance.getIdMarqueArticle());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaMarqueArticle persistentInstance) {
		logger.debug("removing TaMarqueArticle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaMarqueArticle merge(TaMarqueArticle detachedInstance) {
		logger.debug("merging TaMarqueArticle instance");
		try {
			TaMarqueArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaMarqueArticle findById(int id) {
		logger.debug("getting TaMarqueArticle instance with id: " + id);
		try {
			TaMarqueArticle instance = entityManager.find(TaMarqueArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaMarqueArticleDTO> findByCodeLight(String code) {
		logger.debug("getting List<TaMarqueArticleDTO> instance with code: " + code);
		try {
			
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaMarqueArticle.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeMarqueArticle", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaMarqueArticle.QN.FIND_ALL_LIGHT);
			}

			List<TaMarqueArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
			
		}
	}
	
	public TaMarqueArticle findByCode(String code) {
		logger.debug("getting TaMarqueArticle instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaMarqueArticle a where a.codeMarqueArticle='"+code+"'");
			TaMarqueArticle instance = (TaMarqueArticle)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}	
	
	/**
	 * Liste des labels créé ou modifié par partir de la date passée en paramètre.
	 * @param d
	 * @return
	 */
	public List<TaMarqueArticle> findByNewOrUpdatedAfter(Date d) {
//		logger.debug("getting TaArticle instance with quandCreeMarqueArticle or quandModifMarqueArticle >= " + d);
//		try {
//			List<TaMarqueArticle> l = null;
//			if(d!=null) {
//				Query query = entityManager.createNamedQuery(TaMarqueArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER);
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
	public List<TaMarqueArticle> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaMarqueArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaMarqueArticle entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaMarqueArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaMarqueArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaMarqueArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaMarqueArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaMarqueArticle value) throws Exception {
		BeanValidator<TaMarqueArticle> validator = new BeanValidator<TaMarqueArticle>(TaMarqueArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaMarqueArticle value, String propertyName) throws Exception {
		BeanValidator<TaMarqueArticle> validator = new BeanValidator<TaMarqueArticle>(TaMarqueArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaMarqueArticle transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}

