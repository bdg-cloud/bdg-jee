package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosPreparation;
import fr.legrain.documents.dao.IInfosPreparationDAO;

/**
 * Home object for domain model class TaInfosPreparation.
 * @see fr.legrain.documents.dao.TaInfosPreparation
 * @author Hibernate Tools
 */
public class TaInfosPreparationDAO implements IInfosPreparationDAO {

//	private static final Log log = LogFactory.getLog(TaInfosPreparationDAO.class);
	static Logger logger = Logger.getLogger(TaInfosPreparationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosPreparation a";
	
	public TaInfosPreparationDAO(){
//		this(null);
	}
	
//	public TaInfosPreparationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosPreparation.class.getSimpleName());
//		initChampId(TaInfosPreparation.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosPreparation());
//	}
	
//	public TaInfosPreparation refresh(TaInfosPreparation detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosPreparation.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosPreparation transientInstance) {
		logger.debug("persisting TaInfosPreparation instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosPreparation persistentInstance) {
		logger.debug("removing TaInfosPreparation instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosPreparation merge(TaInfosPreparation detachedInstance) {
		logger.debug("merging TaInfosPreparation instance");
		try {
			TaInfosPreparation result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosPreparation findById(int id) {
		logger.debug("getting TaInfosPreparation instance with id: " + id);
		try {
			TaInfosPreparation instance = entityManager
					.find(TaInfosPreparation.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaInfosPreparation findByCodePreparation(String code) {
		logger.debug("getting TaInfosPreparation instance with code InfosPreparation : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosPreparation a where a.taDocument.codeDocument='"+code+"'");
				TaInfosPreparation instance = (TaInfosPreparation)query.getSingleResult();
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
	public List<TaInfosPreparation> selectAll() {
		logger.debug("selectAll TaInfosPreparation");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosPreparation> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaInfosPreparation findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosPreparation> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosPreparation> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaInfosPreparation value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaInfosPreparation value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaInfosPreparation transientInstance) {
		// TODO Auto-generated method stub
		
	}


}
