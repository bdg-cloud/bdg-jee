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
 * Home object for domain model class TaEntreprise.
 * @see fr.legrain.tiers.dao.TaEntreprise
 * @author Hibernate Tools
 */
public class TaEntrepriseDAO /*extends AbstractApplicationDAO<TaEntreprise>*/{

	static Logger logger = Logger.getLogger(TaEntrepriseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaEntreprise a";
	
	public TaEntrepriseDAO() {
//		this(null);
	}
	
//	public TaEntrepriseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaEntreprise.class.getSimpleName());
//		initChampId(TaEntreprise.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaEntreprise());
//	}

	public void persist(TaEntreprise transientInstance) {
		logger.debug("persisting TaEntreprise instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}
	
//	public TaEntreprise refresh(TaEntreprise detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaEntreprise.class, detachedInstance.getIdEntreprise());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaEntreprise persistentInstance) {
		logger.debug("removing TaEntreprise instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaEntreprise merge(TaEntreprise detachedInstance) {
		logger.debug("merging TaEntreprise instance");
		try {
			TaEntreprise result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaEntreprise findById(int id) {
		logger.debug("getting TaEntreprise instance with id: " + id);
		try {
			TaEntreprise instance = entityManager.find(TaEntreprise.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaEntreprise> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEntreprise");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEntreprise> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaEntreprise entity,String field) throws ExceptLgr {	
		
	}
}
