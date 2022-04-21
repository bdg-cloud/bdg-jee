package fr.legrain.tiers.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.dao.ITPaiementDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTPaiement.
 * @see fr.legrain.documents.dao.TaTPaiement
 * @author Hibernate Tools
 */
public class TaTPaiementDAO implements ITPaiementDAO {

//	private static final Log log = LogFactory.getLog(TaTPaiementDAO.class);
	static Logger logger = Logger.getLogger(TaTPaiementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTPaiement a";
	
	public TaTPaiementDAO(){
		//this(null);
	}
	
	public TaTPaiementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTPaiement.class.getSimpleName());
//		initChampId(TaTPaiement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTPaiement());
	}
	
//	public TaTPaiement refresh(TaTPaiement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaTPaiement.class, detachedInstance.getIdTPaiement());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTPaiement transientInstance) {
		logger.debug("persisting TaTPaiement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTPaiement persistentInstance) {
		logger.debug("removing TaTPaiement instance");
		try {
			entityManager.remove(merge(persistentInstance));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTPaiement merge(TaTPaiement detachedInstance) {
		logger.debug("merging TaTPaiement instance");
		try {
			TaTPaiement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTPaiement findById(int id) {
		logger.debug("getting TaTPaiement instance with id: " + id);
		try {
			TaTPaiement instance = entityManager.find(TaTPaiement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTPaiement findByCode(String code) {
		logger.debug("getting TaTPaiement instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaTPaiement a where a.codeTPaiement='"+code+"'");
			TaTPaiement instance = (TaTPaiement)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}	
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaTPaiement> selectAll() {
		logger.debug("selectAll TaTPaiement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTPaiement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTPaiement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTPaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTPaiement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTPaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTPaiement value) throws Exception {
		BeanValidator<TaTPaiement> validator = new BeanValidator<TaTPaiement>(TaTPaiement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTPaiement value, String propertyName) throws Exception {
		BeanValidator<TaTPaiement> validator = new BeanValidator<TaTPaiement>(TaTPaiement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTPaiement transientInstance) {
		entityManager.detach(transientInstance);
	}

}
