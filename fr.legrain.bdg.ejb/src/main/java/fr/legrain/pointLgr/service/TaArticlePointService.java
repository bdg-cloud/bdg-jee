package fr.legrain.pointLgr.service;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.pointLgr.model.TaArticlePoint;
import fr.legrain.point_Lgr.dao.IArticlePointDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaArticlePointService implements IArticlePointDAO{

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaArticlePointService.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaArticlePoint a";
	
	public TaArticlePointService(){
//		this(null);
	}
	
//	public TaArticlePointDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTiersPoint.class.getSimpleName());
//		initChampId(TaTiersPoint.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaArticlePoint());
//	}

//	public TaArticlePoint refresh(TaArticlePoint detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaArticlePoint.class, detachedInstance.getIdArticlePoint());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaArticlePoint transientInstance) {
		logger.debug("persisting TaArticlePoint instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaArticlePoint persistentInstance) {
		logger.debug("removing TaArticlePoint instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaArticlePoint merge(TaArticlePoint detachedInstance) {
		logger.debug("merging TaArticlePoint instance");
		try {
			TaArticlePoint result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaArticlePoint findById(int id) {
		logger.debug("getting TaArticlePoint instance with id: " + id);
		try {
			TaArticlePoint instance = entityManager.find(TaArticlePoint.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaArticlePoint> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaArticlePoint");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaArticlePoint> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	

	public TaArticlePoint findByArticle(Integer idArticle) {
		logger.debug("getting TaArticlePoint  with id: " + idArticle);
		try {
			TaArticlePoint instance=null;
			Query query = entityManager.createQuery("select count(a) from TaArticlePoint a join a.taArticle t where t.idArticle = ?");
			query.setParameter(1, idArticle);
			Long count= (Long) query.getSingleResult();
			if(count>0){
				query = entityManager.createQuery("select a from TaArticlePoint a join a.taArticle t where t.idArticle = ?");
				query.setParameter(1, idArticle);
				instance= (TaArticlePoint) query.getSingleResult();
			}
			return instance;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void ctrlSaisieSpecifique(TaArticlePoint entity,String field) throws ExceptLgr {	
		
	}

	@Override
	public TaArticlePoint findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaArticlePoint> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaArticlePoint> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaArticlePoint> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaArticlePoint> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaArticlePoint value) throws Exception {
		BeanValidator<TaArticlePoint> validator = new BeanValidator<TaArticlePoint>(TaArticlePoint.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaArticlePoint value, String propertyName)
			throws Exception {
		BeanValidator<TaArticlePoint> validator = new BeanValidator<TaArticlePoint>(TaArticlePoint.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaArticlePoint transientInstance) {
		// TODO Auto-generated method stub
		entityManager.detach(transientInstance);
	}

}
