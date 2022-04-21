package fr.legrain.articles.dao;

// Generated 11 juin 2009 10:56:24 by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.gestCom.Appli.Const;

/**
 * Home object for domain model class TaRapportUnite.
 * @see fr.legrain.articles.dao.TaRapportUnite
 * @author Hibernate Tools
 */
public class TaRapportUniteDAO /*extends AbstractApplicationDAO<TaRapportUnite>*/ {

	private static final Log logger = LogFactory.getLog(TaRapportUniteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select p from TaRapportUnite p";
	
	public TaRapportUniteDAO(){
//		this(null);
	}
	
//	public TaRapportUniteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaRapportUnite.class.getSimpleName());
//		initChampId(TaRapportUnite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRapportUnite());
//	}

	public void persist(TaRapportUnite transientInstance) {
		logger.debug("persisting TaRapportUnite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}
	public TaRapportUnite refresh(TaRapportUnite detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaRapportUnite.class, detachedInstance.getId());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaRapportUnite persistentInstance) {
		logger.debug("removing TaRapportUnite instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRapportUnite merge(TaRapportUnite detachedInstance) {
		logger.debug("merging TaRapportUnite instance");
		try {
			TaRapportUnite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRapportUnite findById(int id) {
		logger.debug("getting TaRapportUnite instance with id: " + id);
		try {
			TaRapportUnite instance = entityManager.find(TaRapportUnite.class,
					id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public TaRapportUnite findByCode1(String code) {
		logger.debug("getting TaRapportUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaRapportUnite f " +
					"where f.taUnite1.codeUnite='"+code+"'");
			TaRapportUnite instance = (TaRapportUnite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaRapportUnite findByCode2(String code) {
		logger.debug("getting TaRapportUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaRapportUnite f " +
					"where f.taUnite2.codeUnite='"+code+"'");
			TaRapportUnite instance = (TaRapportUnite)query.getSingleResult();
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
	public List<TaRapportUnite> selectAll() {
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRapportUnite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
}
