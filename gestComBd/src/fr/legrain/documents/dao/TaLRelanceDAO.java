package fr.legrain.documents.dao;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.dao.TaTLiens
 * @author Hibernate Tools
 */
public class TaLRelanceDAO /*extends AbstractApplicationDAO<TaLRelance>*/{

	static Logger logger = Logger.getLogger(TaLRelanceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaLRelance a ";
	
	public TaLRelanceDAO() {
//		this(null);
	}
	
//	public TaLRelanceDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLRelance.class.getSimpleName());
//		initChampId(TaLRelance.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLRelance());
//	}
	
//	public TaLRelance refresh(TaLRelance detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLRelance.class, detachedInstance.getIdLRelance());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLRelance transientInstance) {
		logger.debug("persisting TaLRelance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLRelance persistentInstance) {
		logger.debug("removing TaLRelance instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLRelance merge(TaLRelance detachedInstance) {
		logger.debug("merging TaLRelance instance");
		try {
			TaLRelance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLRelance findById(int id) {
		logger.debug("getting TaLRelance instance with id: " + id);
		try {
			TaLRelance instance = entityManager.find(TaLRelance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaLRelance> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLRelance");
		try {  
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLRelance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	public void ctrlSaisieSpecifique(TaLRelance entity,String field) throws ExceptLgr {	
		
	}
}
