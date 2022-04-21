package fr.legrain.articles.dao;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;

/**
 * Home object for domain model class TaCatalogueWeb.
 * @see fr.legrain.articles.dao.TaCatalogueWeb
 * @author Hibernate Tools
 */
public class TaCatalogueWebDAO /*extends AbstractApplicationDAO<TaCatalogueWeb>*/ {

	private static final Log logger = LogFactory.getLog(TaCatalogueWebDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaCatalogueWeb p";
	
	public TaCatalogueWebDAO(){
//		this(null);
	}

//	public TaCatalogueWebDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaCatalogueWeb.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCatalogueWeb());
//	}


	protected void persist(TaCatalogueWeb transientInstance) {
		logger.debug("persisting TaCatalogueWeb instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}
	public TaCatalogueWeb refresh(TaCatalogueWeb detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaCatalogueWeb.class, detachedInstance.getIdCatalogueWeb());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	protected void remove(TaCatalogueWeb persistentInstance) {
		logger.debug("removing TaCatalogueWeb instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	protected TaCatalogueWeb merge(TaCatalogueWeb detachedInstance) {
		logger.debug("merging TaCatalogueWeb instance");
		try {
			TaCatalogueWeb result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaCatalogueWeb findById(int id) {
		logger.debug("getting TaCatalogueWeb instance with id: " + id);
		try {
			TaCatalogueWeb instance = entityManager.find(TaCatalogueWeb.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaCatalogueWeb findByCode(String code) {
		logger.debug("getting TaCatalogueWeb instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaCatalogueWeb a where a.codeCategorieArticle='"+code+"'");
			TaCatalogueWeb instance = (TaCatalogueWeb)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaCatalogueWeb> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCatalogueWeb> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaCatalogueWeb entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

