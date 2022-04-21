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
 * Home object for domain model class TaInfoJuridique.
 * @see fr.legrain.tiers.dao.TaInfoJuridique
 * @author Hibernate Tools
 */
public class TaInfoJuridiqueDAO /*extends AbstractApplicationDAO<TaInfoJuridique>*/{

	//private static final Log logger = LogFactory.getLog(TaInfoJuridiqueDAO.class);
	static Logger logger = Logger.getLogger(TaInfoJuridiqueDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfoJuridique a";
	
	public TaInfoJuridiqueDAO(){
//		this(null);
	}
//	
//	public TaInfoJuridiqueDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfoJuridique.class.getSimpleName());
//		initChampId(TaInfoJuridique.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfoJuridique());
//	}

//	public TaInfoJuridique refresh(TaInfoJuridique detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaInfoJuridique.class, detachedInstance.getIdInfoJuridique());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfoJuridique transientInstance) {
		logger.debug("persisting TaInfoJuridique instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfoJuridique persistentInstance) {
		logger.debug("removing TaInfoJuridique instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfoJuridique merge(TaInfoJuridique detachedInstance) {
		logger.debug("merging TaInfoJuridique instance");
		try {
			TaInfoJuridique result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfoJuridique findById(int id) {
		logger.debug("getting TaInfoJuridique instance with id: " + id);
		try {
			TaInfoJuridique instance = entityManager.find(TaInfoJuridique.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaInfoJuridique> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaInfoJuridique");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfoJuridique> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaInfoJuridique entity,String field) throws ExceptLgr {	
		
	}

}
