package fr.legrain.dossierIntelligent.dao;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaRParamDossierIntelDAO /*extends AbstractApplicationDAO<TaRParamDossierIntel>*/{

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaRParamDossierIntelDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaRParamDossierIntel a";
	
	public TaRParamDossierIntelDAO(){
//		this(null);
	}
	
//	public TaRParamDossierIntelDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaRParamDossierIntel.class.getSimpleName());
//		initChampId(TaRParamDossierIntel.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRParamDossierIntel());
//	}

//	public TaRParamDossierIntel refresh(TaRParamDossierIntel detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaRParamDossierIntel.class, detachedInstance.getId());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaRParamDossierIntel transientInstance) {
		logger.debug("persisting TaRParamDossierIntel instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRParamDossierIntel persistentInstance) {
		logger.debug("removing TaRParamDossierIntel instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRParamDossierIntel merge(TaRParamDossierIntel detachedInstance) {
		logger.debug("merging TaRParamDossierIntel instance");
		try {
			TaRParamDossierIntel result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRParamDossierIntel findById(int id) {
		logger.debug("getting TaRParamDossierIntel instance with id: " + id);
		try {
			TaRParamDossierIntel instance = entityManager.find(TaRParamDossierIntel.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaRParamDossierIntel> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaRParamDossierIntel");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRParamDossierIntel> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	public void ctrlSaisieSpecifique(TaRParamDossierIntel entity,String field) throws ExceptLgr {	
		
	}

}
