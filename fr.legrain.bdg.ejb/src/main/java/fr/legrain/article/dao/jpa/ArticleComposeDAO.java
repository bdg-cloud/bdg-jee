package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.IArticleComposeDAO;
import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;

/**
 * Home object for domain model class TaArticleCompose.
 * @see fr.legrain.articles.dao.TaArticleCompose
 * @author Hibernate Tools
 */
public class ArticleComposeDAO implements IArticleComposeDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9152544684256741169L;
	

	
	

	//private static final Log logger = LogFactory.getLog(TaArticleDAO.class);
	static Logger logger = Logger.getLogger(ArticleComposeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaArticleCompose a ";
	
	//private String defaultLightJPQLQueryEcran = TaArticleCompose.QN.FIND_BY_ECRAN_LIGHT;
	
	public ArticleComposeDAO(){
	}
	

	
	
	public void persist(TaArticleCompose transientInstance) {
		logger.debug("persisting TaArticleCompose instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaArticleCompose persistentInstance) {
		logger.debug("removing TaArticleCompose instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaArticleCompose merge(TaArticleCompose detachedInstance) {
		logger.debug("merging TaArticleCompose instance");
		try {
			TaArticleCompose result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaArticleCompose findById(int id) {
		logger.debug("getting TaArticleCompose instance with id: " + id);
		try {
			TaArticleCompose instance = entityManager.find(TaArticleCompose.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaArticleCompose> selectAll() {
		logger.debug("selectAll TaArticleCompose");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaArticleCompose> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaArticleCompose refresh(TaArticleCompose detachedInstance) {
		logger.debug("refresh instance");
		try {			
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaArticleCompose.class, detachedInstance.getIdArticleCompose());
		    return detachedInstance;		    
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	/**
	 * Pour insertion batch
	 * http://docs.jboss.org/hibernate/core/3.3/reference/en/html/batch.html
	 * @param detachedInstance
	 */
	public void mergeFlush(TaArticleCompose detachedInstance) {
		logger.debug("refresh instance");
		try {			
			merge(detachedInstance);
			entityManager.flush();
			entityManager.clear();
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.flush();
//		    session.clear();		    
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	

	

	public String getDefaultJPQLQuery() {
		return defaultJPQLQuery;
	}

	public void setDefaultJPQLQuery(String defaultJPQLQuery) {
		this.defaultJPQLQuery = defaultJPQLQuery;
	}




	



	@Override
	public List<TaArticleCompose> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<TaArticleCompose> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public TaArticleCompose findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public boolean validate(TaArticleCompose value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean validateField(TaArticleCompose value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public void detach(TaArticleCompose transientInstance) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void deleteListDTO(List<TaArticleComposeDTO> liste) {
		if(liste != null && !liste.isEmpty()) {
			try {
				
				List<Integer> ids = new ArrayList<Integer>();
				for (TaArticleComposeDTO taArticleComposeDTO : liste) {
					ids.add(taArticleComposeDTO.getIdArticleCompose());
				}
				Query query = entityManager.createQuery("delete TaArticleCompose ac where ac.idArticleCompose IN :ids");
				query.setParameter("ids", ids);
				query.executeUpdate();
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
	}
	@Override
	public void deleteList(List<TaArticleCompose> liste) {
		if(liste != null && !liste.isEmpty()) {
			try {
				
				List<Integer> ids = new ArrayList<Integer>();
				for (TaArticleCompose taArticleCompose : liste) {
					ids.add(taArticleCompose.getIdArticleCompose());
				}
				Query query = entityManager.createQuery("delete TaArticleCompose ac where ac.idArticleCompose IN :ids");
				query.setParameter("ids", ids);
				query.executeUpdate();
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
	}
	
	@Override
	public void deleteSet(Set<TaArticleCompose> set) {
		if(set != null && !set.isEmpty()) {
			try {
				
				List<Integer> ids = new ArrayList<Integer>();
				for (TaArticleCompose taArticleCompose : set) {
					ids.add(taArticleCompose.getIdArticleCompose());
				}
				Query query = entityManager.createQuery("delete TaArticleCompose ac where ac.idArticleCompose IN :ids");
				query.setParameter("ids", ids);
				query.executeUpdate();
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
	}
	
	public List<TaArticleCompose> findAllByIdArticleEnfant(int idArticleEnfant){
		try {
			Query query = entityManager.createNamedQuery(TaArticleCompose.QN.FIND_ALL_BY_ID_ARTICLE_ENFANT);
			query.setParameter("idArticleEnfant", idArticleEnfant);
			List<TaArticleCompose> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaArticleCompose findByIdArticleParentAndByIdArticleEnfant(int idArticleParent, int idArticleEnfant) {
		try {
			
			Query query = entityManager.createNamedQuery(TaArticleCompose.QN.FIND_BY_ID_ARTICLE_PARENT_AND_BY_ID_ARTICLE_ENFANT);
			query.setParameter("idArticleParent", idArticleParent);
			query.setParameter("idArticleEnfant", idArticleEnfant);
			TaArticleCompose ac = (TaArticleCompose) query.getSingleResult();
			return ac;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	public String getDefaultLightJPQLQueryEcran() {
//		return defaultLightJPQLQueryEcran;
//	}
//
//	public void setDefaultLightJPQLQueryEcran(String defaultLightJPQLQueryEcran) {
//		this.defaultLightJPQLQueryEcran = defaultLightJPQLQueryEcran;
//	}
	
	
	
	
	

}
