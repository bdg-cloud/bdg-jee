package fr.legrain.documents.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaEtat;

/**
 * Home object for domain model class TaEtat.
 * @see fr.legrain.documents.dao.TaEtat
 * @author Hibernate Tools
 */
public class TaReponseDAO /*extends AbstractApplicationDAO<TaEtat>*/ {

	static Logger logger = Logger.getLogger(TaReponseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaEtat a";
	
	public TaReponseDAO(){
//		this(null);
	}
	
//	public TaReponseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaEtat.class.getSimpleName());
//		initChampId(TaEtat.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaEtat());
//	}
//	public TaEtat refresh(TaEtat detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaEtat.class, detachedInstance.getIdTDoc());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaEtat transientInstance) {
		logger.debug("persisting TaEtat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaEtat persistentInstance) {
		logger.debug("removing TaEtat instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaEtat merge(TaEtat detachedInstance) {
		logger.debug("merging TaEtat instance");
		try {
			TaEtat result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaEtat findById(int id) {
		logger.debug("getting TaEtat instance with id: " + id);
		try {
			TaEtat instance = entityManager.find(TaEtat.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaEtat findByCode(String code) {
		logger.debug("getting TaEtat instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaEtat a where a.codeEtat='"+code+"'");
			TaEtat instance = (TaEtat)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaEtat> selectAll() {
		logger.debug("selectAll TaEtat");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEtat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
}
