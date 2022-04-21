package fr.legrain.lgrBackup.dao;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;

/**
 * Home object for domain model class TaCPaiement.
 * @see fr.legrain.tiers.dao.test.TaCPaiement
 * @author Hibernate Tools
 */
public   class TaLgrBackupDAO /*extends AbstractApplicationDAO<TaLgrBackup>*/{

	static Logger logger = Logger.getLogger(TaLgrBackupDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLgrBackup a";
	
	public TaLgrBackupDAO() {
//		this(null);
	}
	
//	public TaLgrBackupDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaLgrBackup.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLgrBackup());
//		initNomTableMere(new TaLgrBackup());
//	}
	
	
	public void persist(TaLgrBackup transientInstance) {
		logger.debug("persisting TaLgrBackup instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}
	
//	public TaLgrBackup refresh(TaLgrBackup detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLgrBackup.class, detachedInstance.getIdSupportAbon());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaLgrBackup persistentInstance) {
		logger.debug("removing TaLgrBackup instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLgrBackup merge(TaLgrBackup detachedInstance) {
		logger.debug("merging TaLgrBackup instance");
		try {
			TaLgrBackup result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLgrBackup findById(int id) {
		logger.debug("getting TaLgrBackup instance with id: " + id);
		try {
			TaLgrBackup instance = entityManager.find(TaLgrBackup.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLgrBackup findByCode(String code) {
		logger.debug("getting TaLgrBackup instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLgrBackup f where " +
					"f.codeSupportAbon='"+code+"'");
			TaLgrBackup instance = (TaLgrBackup)query.getSingleResult();
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
	public List<TaLgrBackup> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLgrBackup");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLgrBackup> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	

}
