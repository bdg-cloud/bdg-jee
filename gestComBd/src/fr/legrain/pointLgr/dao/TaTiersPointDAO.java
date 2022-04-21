//package fr.legrain.pointLgr.dao;
//
//// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1
//
//import java.util.LinkedList;
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
//import fr.legrain.tiers.dao.TaTiers;
//import fr.legrain.tiers.dao.TaTiersDAO;
//
//
///**
// * Home object for domain model class TaAdresse.
// * @see fr.legrain.tiers.dao.TaAdresse
// * @author Hibernate Tools
// */
//public class TaTiersPointDAO /*extends AbstractApplicationDAO<TaTiersPoint>*/{
//
//	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
//	static Logger logger = Logger.getLogger(TaTiersPointDAO.class);
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//	
//	private String defaultJPQLQuery = "select a from TaTiersPoint a";
//	
//	public TaTiersPointDAO(){
////		this(null);
//	}
//	
////	public TaTiersPointDAO(EntityManager em){
////		if(em!=null) {
////			setEm(em);
////		}
//////		champIdTable=ctrlGeneraux.getID_TABLE(TaTiersPoint.class.getSimpleName());
////		initChampId(TaTiersPoint.class);
////		setJPQLQuery(defaultJPQLQuery);
////		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
////		initNomTable(new TaTiersPoint());
////	}
//
////	public TaTiersPoint refresh(TaTiersPoint detachedInstance) {
////		logger.debug("refresh instance");
////		try {
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaTiersPoint.class, detachedInstance.getIdTiersPoint());
////		    return detachedInstance;
////		} catch (RuntimeException re) {
////			logger.error("refresh failed", re);
////			throw re;
////		}
////	}
//	
//	public void persist(TaTiersPoint transientInstance) {
//		logger.debug("persisting TaTiersPoint instance");
//		try {
//			entityManager.persist(transientInstance);
//			logger.debug("persist successful");
//		} catch (RuntimeException re) {
//			logger.error("persist failed", re);
//			throw re;
//		}
//	}
//
//	public void remove(TaTiersPoint persistentInstance) {
//		logger.debug("removing TaTiersPoint instance");
//		try {
//			entityManager.remove(persistentInstance);
//			logger.debug("remove successful");
//		} catch (RuntimeException re) {
//			logger.error("remove failed", re);
//			throw re;
//		}
//	}
//
//	public TaTiersPoint merge(TaTiersPoint detachedInstance) {
//		logger.debug("merging TaTiersPoint instance");
//		try {
//			TaTiersPoint result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("merge failed", re);
//			throw re;
//		}
//	}
//
//	public TaTiersPoint findById(int id) {
//		logger.debug("getting TaTiersPoint instance with id: " + id);
//		try {
//			TaTiersPoint instance = entityManager.find(TaTiersPoint.class, id);
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
////	@Override
//	public List<TaTiersPoint> selectAll() {
//		// TODO Auto-generated method stub
//		logger.debug("selectAll TaTiersPoint");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			List<TaTiersPoint> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
//	public List<TaTiersPoint> calculPointPeriode() {
//		try {
//			
//			List<TaTiersPoint> lTiersPoint = new LinkedList<TaTiersPoint>();
//			TaTiersDAO daoTiers=new TaTiersDAO(/*entityManager*/);
//			List<TaTiers> lTiers = daoTiers.selectAll();
//			
//			for (TaTiers taTiers : lTiers) {
//				//pour chaque tiers, regarder si a acheter des articles qui genere des points
//				
//			}
//			
//			logger.debug("calculPointPeriode successful");
//			return lTiersPoint;
//		} catch (RuntimeException re) {
//			logger.error("calculPointPeriode failed", re);
//			throw re;
//		}
//	}
//	
//	public void ctrlSaisieSpecifique(TaTiersPoint entity,String field) throws ExceptLgr {	
//		
//	}
//
//}
