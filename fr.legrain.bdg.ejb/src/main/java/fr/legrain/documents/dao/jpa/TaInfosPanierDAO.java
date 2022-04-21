package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.documents.dao.IInfosPanierDAO;

/**
 * Home object for domain model class TaInfosPanier.
 * @see fr.legrain.documents.dao.TaInfosPanier
 * @author Hibernate Tools
 */
public class TaInfosPanierDAO implements IInfosPanierDAO {

//	private static final Log log = LogFactory.getLog(TaInfosPanierDAO.class);
	static Logger logger = Logger.getLogger(TaInfosPanierDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosPanier a";
	
	public TaInfosPanierDAO(){
//		this(null);
	}
	
//	public TaInfosPanierDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosPanier.class.getSimpleName());
//		initChampId(TaInfosPanier.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosPanier());
//	}
	
//	public TaInfosPanier refresh(TaInfosPanier detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosPanier.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosPanier transientInstance) {
		logger.debug("persisting TaInfosPanier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosPanier persistentInstance) {
		logger.debug("removing TaInfosPanier instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosPanier merge(TaInfosPanier detachedInstance) {
		logger.debug("merging TaInfosPanier instance");
		try {
			TaInfosPanier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosPanier findById(int id) {
		logger.debug("getting TaInfosPanier instance with id: " + id);
		try {
			TaInfosPanier instance = entityManager
					.find(TaInfosPanier.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaInfosPanier findByCodePanier(String code) {
		logger.debug("getting TaInfosPanier instance with code InfosPanier : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosPanier a where a.taDocument.codeDocument='"+code+"'");
				TaInfosPanier instance = (TaInfosPanier)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaInfosPanier> selectAll() {
		logger.debug("selectAll TaInfosPanier");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosPanier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaInfosPanier findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosPanier> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosPanier> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaInfosPanier value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaInfosPanier value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaInfosPanier transientInstance) {
		// TODO Auto-generated method stub
		
	}


}
