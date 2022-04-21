package fr.legrain.supportNDD.dao;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.SupportAbon.dao.TaSupportAbon;
import fr.legrain.gestCom.Appli.Const;

/**
 * Home object for domain model class TaCPaiement.
 * @see fr.legrain.tiers.dao.test.TaCPaiement
 * @author Hibernate Tools
 */
public   class TaNDDDAO /*extends AbstractApplicationDAO<TaNDD>*/{

	static Logger logger = Logger.getLogger(TaNDDDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaHebergement a";
	
	public TaNDDDAO() {
//		this(null);
	}
	
//	public TaNDDDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaNDD.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaNDD());
//		initNomTableMere(new TaSupportAbon());
//	}
	
	
	public void persist(TaNDD transientInstance) {
		logger.debug("persisting TaHebergement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}
	
//	public TaNDD refresh(TaNDD detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaNDD.class, detachedInstance.getIdSupportAbon());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaNDD persistentInstance) {
		logger.debug("removing TaHebergement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaNDD merge(TaNDD detachedInstance) {
		logger.debug("merging TaHebergement instance");
		try {
			TaNDD result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaNDD findById(int id) {
		logger.debug("getting TaHebergement instance with id: " + id);
		try {
			TaNDD instance = entityManager.find(TaNDD.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaNDD findByCode(String code) {
		logger.debug("getting TaHebergement instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaHebergement f where " +
					"f.codeSupportAbon='"+code+"'");
			TaNDD instance = (TaNDD)query.getSingleResult();
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
	public List<TaNDD> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaHebergement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaNDD> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	

}
