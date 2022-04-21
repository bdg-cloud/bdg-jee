package fr.legrain.tiers.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.tiers.dao.ITDocDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTDoc.
 * @see fr.legrain.documents.dao.TaTDoc
 * @author Hibernate Tools
 */
public class TaTDocDAO implements ITDocDAO {

//	private static final Log log = LogFactory.getLog(TaTDocDAO.class);
	static Logger logger = Logger.getLogger(TaTDocDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTDoc a";
	
	public TaTDocDAO(){
//		this(null);
	}
	
	public TaTDocDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTDoc.class.getSimpleName());
//		initChampId(TaTDoc.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTDoc());
	}
	
//	public TaTDoc refresh(TaTDoc detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaTDoc.class, detachedInstance.getIdTDoc());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTDoc transientInstance) {
		logger.debug("persisting TaTDoc instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTDoc persistentInstance) {
		logger.debug("removing TaTDoc instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTDoc merge(TaTDoc detachedInstance) {
		logger.debug("merging TaTDoc instance");
		try {
			TaTDoc result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTDoc findById(int id) {
		logger.debug("getting TaTDoc instance with id: " + id);
		try {
			TaTDoc instance = entityManager.find(TaTDoc.class, id);
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
	public List<TaTDoc> selectAll() {
		logger.debug("selectAll TaTDoc");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTDoc> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTDoc> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTDoc> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTDoc> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTDoc> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTDoc value) throws Exception {
		BeanValidator<TaTDoc> validator = new BeanValidator<TaTDoc>(TaTDoc.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTDoc value, String propertyName) throws Exception {
		BeanValidator<TaTDoc> validator = new BeanValidator<TaTDoc>(TaTDoc.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTDoc transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTDoc findByCode(String code) {
		logger.debug("getting TaTDoc instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaTDoc a where a.codeTDoc='"+code+"'");
			TaTDoc instance = (TaTDoc)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}
