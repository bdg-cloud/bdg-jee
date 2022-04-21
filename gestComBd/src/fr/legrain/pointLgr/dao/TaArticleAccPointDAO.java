//package fr.legrain.pointLgr.dao;
//
//// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.apache.log4j.Logger;
//
//import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.lib.data.ExceptLgr;
//
//
///**
// * Home object for domain model class TaAdresse.
// * @see fr.legrain.tiers.dao.TaAdresse
// * @author Hibernate Tools
// */
//public class TaArticleAccPointDAO /*extends AbstractApplicationDAO<TaArticleAccPoint>*/{
//
//	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
//	static Logger logger = Logger.getLogger(TaArticleAccPointDAO.class);
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//	
//	private String defaultJPQLQuery = "select a from TaArticleAccPoint a";
//	
//	public TaArticleAccPointDAO(){
////		this(null);
//	}
//	
////	public TaArticleAccPointDAO(EntityManager em){
////		if(em!=null) {
////			setEm(em);
////		}
//////		champIdTable=ctrlGeneraux.getID_TABLE(TaTiersPoint.class.getSimpleName());
////		initChampId(TaArticleAccPoint.class);
////		setJPQLQuery(defaultJPQLQuery);
////		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
////		initNomTable(new TaArticleAccPoint());
////	}
//
////	public TaArticleAccPoint refresh(TaArticleAccPoint detachedInstance) {
////		logger.debug("refresh instance");
////		try {
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaArticleAccPoint.class, detachedInstance.getIdArticleAccPoint());
////		    return detachedInstance;
////		} catch (RuntimeException re) {
////			logger.error("refresh failed", re);
////			throw re;
////		}
////	}
//	
//	public void persist(TaArticleAccPoint transientInstance) {
//		logger.debug("persisting TaArticleAccPoint instance");
//		try {
//			entityManager.persist(transientInstance);
//			logger.debug("persist successful");
//		} catch (RuntimeException re) {
//			logger.error("persist failed", re);
//			throw re;
//		}
//	}
//
//	public void remove(TaArticleAccPoint persistentInstance) {
//		logger.debug("removing TaArticleAccPoint instance");
//		try {
//			entityManager.remove(persistentInstance);
//			logger.debug("remove successful");
//		} catch (RuntimeException re) {
//			logger.error("remove failed", re);
//			throw re;
//		}
//	}
//
//	public TaArticleAccPoint merge(TaArticleAccPoint detachedInstance) {
//		logger.debug("merging TaArticleAccPoint instance");
//		try {
//			TaArticleAccPoint result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public TaArticleAccPoint findById(int id) {
//		logger.debug("getting TaArticleAccPoint instance with id: " + id);
//		try {
//			TaArticleAccPoint instance = entityManager.find(TaArticleAccPoint.class, id);
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
////	@Override
//	public List<TaArticleAccPoint> selectAll() {
//		// TODO Auto-generated method stub
//		logger.debug("selectAll TaArticleAccPoint");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			List<TaArticleAccPoint> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
//	public void ctrlSaisieSpecifique(TaArticleAccPoint entity,String field) throws ExceptLgr {	
//		
//	}
//
//}
