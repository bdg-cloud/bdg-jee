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
 * Home object for domain model class TaTAdr.
 * @see fr.legrain.tiers.dao.TaTAdr
 * @author Hibernate Tools
 */
public class TaTAdrDAO /*extends AbstractApplicationDAO<TaTAdr>*/{

	//private static final Log logger = LogFactory.getLog(TaTAdrDAO.class);
	static Logger logger = Logger.getLogger(TaTAdrDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTAdr a order by a.codeTAdr";
	
	public TaTAdrDAO() {
//		this(null);
	}
	
//	public TaTAdrDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTAdr.class.getSimpleName());
//		initChampId(TaTAdr.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTAdr());
//	}
//	public TaTAdr refresh(TaTAdr detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTAdr.class, detachedInstance.getIdTAdr());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTAdr transientInstance) {
		logger.debug("persisting TaTAdr instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaTAdr persistentInstance) {
		logger.debug("removing TaTAdr instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaTAdr merge(TaTAdr detachedInstance) {
		logger.debug("merging TaTAdr instance");
		try {
			TaTAdr result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTAdr findById(int id) {
		logger.debug("getting TaTAdr instance with id: " + id);
		try {
			TaTAdr instance = entityManager.find(TaTAdr.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTAdr findByCode(String code) {
		logger.debug("getting TaTAdr instance with code: " + code);
		try {
        			if(!code.equals("")){
        			Query query = entityManager.createQuery("select f from TaTAdr f where f.codeTAdr='"+code+"'");
        			TaTAdr instance = (TaTAdr)query.getSingleResult();
        			logger.debug("get successful");
        			return instance;
        			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

//	@Override
	public List<TaTAdr> selectAll() {
		logger.debug("selectAll TaTAdr");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTAdr> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public void ctrlSaisieSpecifique(TaTAdr entity,String field) throws ExceptLgr {	
		
	}
}
