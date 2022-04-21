package fr.legrain.pointLgr.dao;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.lib.data.ExceptLgr;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaPointHistoriqueDAO /*extends AbstractApplicationDAO<TaPointHistorique>*/{

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaPointHistoriqueDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaPointHistorique a";
	
	public TaPointHistoriqueDAO(){
//		this(null);
	}
	
//	public TaPointHistoriqueDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTiersPoint.class.getSimpleName());
//		initChampId(TaPointHistorique.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaPointHistorique());
//	}
//
//	public TaPointHistorique refresh(TaPointHistorique detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaPointHistorique.class, detachedInstance.getIdPoint());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaPointHistorique transientInstance) {
		logger.debug("persisting TaPointHistorique instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaPointHistorique persistentInstance) {
		logger.debug("removing TaPointHistorique instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaPointHistorique merge(TaPointHistorique detachedInstance) {
		logger.debug("merging TaPointHistorique instance");
		try {
			TaPointHistorique result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaPointHistorique findById(int id) {
		logger.debug("getting TaPointHistorique instance with id: " + id);
		try {
			TaPointHistorique instance = entityManager.find(TaPointHistorique.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaPointHistorique> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaPointHistorique");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPointHistorique> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaPointHistorique entity,String field) throws ExceptLgr {	
		
	}

}
