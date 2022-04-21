package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.documents.dao.IInfosBonReceptionDAO;
import fr.legrain.documents.dao.IInfosBonlivDAO;

/**
 * Home object for domain model class TaInfosBonliv.
 * @see fr.legrain.documents.dao.TaInfosBonliv
 * @author Hibernate Tools
 */
public class TaInfosBonlivDAO implements IInfosBonlivDAO {

//	private static final Log log = LogFactory.getLog(TaInfosBonlivDAO.class);
	static Logger logger = Logger.getLogger(TaInfosBonlivDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosBonliv a";
	
	public TaInfosBonlivDAO(){
//		this(null);
	}
	
//	public TaInfosBonlivDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosBonliv.class.getSimpleName());
//		initChampId(TaInfosBonliv.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosBonliv());
//	}
	
//	public TaInfosBonliv refresh(TaInfosBonliv detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosBonliv.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosBonliv transientInstance) {
		logger.debug("persisting TaInfosBonliv instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosBonliv persistentInstance) {
		logger.debug("removing TaInfosBonliv instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosBonliv merge(TaInfosBonliv detachedInstance) {
		logger.debug("merging TaInfosBonliv instance");
		try {
			TaInfosBonliv result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosBonliv findById(int id) {
		logger.debug("getting TaInfosBonliv instance with id: " + id);
		try {
			TaInfosBonliv instance = entityManager
					.find(TaInfosBonliv.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaInfosBonliv findByCodeBonliv(String code) {
		logger.debug("getting TaInfosBonliv instance with code InfosBonliv : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosBonliv a where a.taDocument.codeDocument='"+code+"'");
				TaInfosBonliv instance = (TaInfosBonliv)query.getSingleResult();
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
	public List<TaInfosBonliv> selectAll() {
		logger.debug("selectAll TaInfosBonliv");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosBonliv> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaInfosBonliv findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosBonliv> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosBonliv> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaInfosBonliv value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaInfosBonliv value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaInfosBonliv transientInstance) {
		// TODO Auto-generated method stub
		
	}


}
