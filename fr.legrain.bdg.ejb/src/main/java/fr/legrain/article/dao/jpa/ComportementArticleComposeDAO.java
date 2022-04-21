package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import fr.legrain.article.dao.IComportementArticleComposeDAO;
import fr.legrain.article.dto.TaComportementArticleComposeDTO;
import fr.legrain.article.model.TaComportementArticleCompose;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;

/**
 * Home object for domain model class TaComportementArticleCompose.
 * @see fr.legrain.articles.dao.TaComportementArticleCompose
 * @author Hibernate Tools
 */
public class ComportementArticleComposeDAO implements IComportementArticleComposeDAO, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6843194074795338090L;

	static Logger logger = Logger.getLogger(ComportementArticleComposeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaComportementArticleCompose a ";
	
	
	public ComportementArticleComposeDAO(){
	}
	

	
	
	public void persist(TaComportementArticleCompose transientInstance) {
		logger.debug("persisting TaComportementArticleCompose instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaComportementArticleCompose persistentInstance) {
		logger.debug("removing TaComportementArticleCompose instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaComportementArticleCompose merge(TaComportementArticleCompose detachedInstance) {
		logger.debug("merging TaComportementArticleCompose instance");
		try {
			TaComportementArticleCompose result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaComportementArticleCompose findById(int id) {
		logger.debug("getting TaComportementArticleCompose instance with id: " + id);
		try {
			TaComportementArticleCompose instance = entityManager.find(TaComportementArticleCompose.class, id);
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
	public List<TaComportementArticleCompose> selectAll() {
		logger.debug("selectAll TaComportementArticleCompose");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaComportementArticleCompose> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaComportementArticleCompose refresh(TaComportementArticleCompose detachedInstance) {
		logger.debug("refresh instance");
		try {			
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaComportementArticleCompose.class, detachedInstance.getIdComportementArticleCompose());
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
	public void mergeFlush(TaComportementArticleCompose detachedInstance) {
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
	public List<TaComportementArticleCompose> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public List<TaComportementArticleCompose> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public TaComportementArticleCompose findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public boolean validate(TaComportementArticleCompose value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public boolean validateField(TaComportementArticleCompose value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}




	@Override
	public void detach(TaComportementArticleCompose transientInstance) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void deleteListDTO(List<TaComportementArticleComposeDTO> liste) {
		if(liste != null && !liste.isEmpty()) {
			try {
				
				List<Integer> ids = new ArrayList<Integer>();
				for (TaComportementArticleComposeDTO taComportementArticleComposeDTO : liste) {
					ids.add(taComportementArticleComposeDTO.getIdComportementArticleCompose());
				}
				Query query = entityManager.createQuery("delete TaComportementArticleCompose ac where ac.idComportementArticleCompose IN :ids");
				query.setParameter("ids", ids);
				query.executeUpdate();
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
		
	}
	@Override
	public void deleteList(List<TaComportementArticleCompose> liste) {
		if(liste != null && !liste.isEmpty()) {
			try {
				
				List<Integer> ids = new ArrayList<Integer>();
				for (TaComportementArticleCompose taComportementArticleCompose : liste) {
					ids.add(taComportementArticleCompose.getIdComportementArticleCompose());
				}
				Query query = entityManager.createQuery("delete TaComportementArticleCompose ac where ac.idComportementArticleCompose IN :ids");
				query.setParameter("ids", ids);
				query.executeUpdate();
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
	}
	
	@Override
	public void deleteSet(Set<TaComportementArticleCompose> set) {
		if(set != null && !set.isEmpty()) {
			try {
				
				List<Integer> ids = new ArrayList<Integer>();
				for (TaComportementArticleCompose taComportementArticleCompose : set) {
					ids.add(taComportementArticleCompose.getIdComportementArticleCompose());
				}
				Query query = entityManager.createQuery("delete TaComportementArticleCompose ac where ac.idComportementArticleCompose IN :ids");
				query.setParameter("ids", ids);
				query.executeUpdate();
			} catch (RuntimeException re) {
				logger.error("get failed", re);
				throw re;
			}
		}
	}
	


	
	
	
	

}
