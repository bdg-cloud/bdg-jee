package fr.legrain.tiers.dao;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;

/**
 * Home object for domain model class TaTTarif.
 * @see fr.legrain.tiers.dao.TaTTarif
 * @author Hibernate Tools
 */
public class TaTTarifDAO /*extends AbstractApplicationDAO<TaTTarif>*/{

	//private static final Log logger = LogFactory.getLog(TaTTarifDAO.class);
	static Logger logger = Logger.getLogger(TaTTarifDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTTarif a";
	
	public TaTTarifDAO() {
//		this(null);
	}
	
//	public TaTTarifDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTTarif.class.getSimpleName());
//		initChampId(TaTTarif.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTTarif());
//	}
	
//	public TaTTarif refresh(TaTTarif detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTTarif.class, detachedInstance.getIdTTarif());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTTarif transientInstance) {
		logger.debug("persisting TaTTarif instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaTTarif persistentInstance) {
		logger.debug("removing TaTTarif instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaTTarif merge(TaTTarif detachedInstance) {
		logger.debug("merging TaTTarif instance");
		try {
			TaTTarif result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTTarif findById(int id) {
		logger.debug("getting TaTTarif instance with id: " + id);
		try {
			TaTTarif instance = entityManager.find(TaTTarif.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTTarif findByCode(String code) {
		logger.debug("getting TaTTarif instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaTTarif a where a.codeTTarif='"+code+"'");
			TaTTarif instance = (TaTTarif)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
//	@Override
	public List<TaTTarif> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTTarif");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTTarif> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public void ctrlSaisieSpecifique(TaTTarif entity,String field) throws ExceptLgr {	
		
	}
}
