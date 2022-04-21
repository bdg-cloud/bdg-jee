package fr.legrain.tiers.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.tiers.dao.ITEtatDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTEtat.
 * @see fr.legrain.documents.dao.TaTEtat
 * @author Hibernate Tools
 */
public class TaTEtatDAO implements ITEtatDAO {

//	private static final Log log = LogFactory.getLog(TaTEtatDAO.class);
	static Logger logger = Logger.getLogger(TaTEtatDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTEtat a";
	
	public TaTEtatDAO(){
//		this(null);
	}
	
	public TaTEtatDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTEtat.class.getSimpleName());
//		initChampId(TaTEtat.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTEtat());
	}
	
//	public TaTEtat refresh(TaTEtat detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaTEtat.class, detachedInstance.getIdTEtat());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTEtat transientInstance) {
		logger.debug("persisting TaTEtat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTEtat persistentInstance) {
		logger.debug("removing TaTEtat instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTEtat merge(TaTEtat detachedInstance) {
		logger.debug("merging TaTEtat instance");
		try {
			TaTEtat result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTEtat findById(int id) {
		logger.debug("getting TaTEtat instance with id: " + id);
		try {
			TaTEtat instance = entityManager.find(TaTEtat.class, id);
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
	public List<TaTEtat> selectAll() {
		logger.debug("selectAll TaTEtat");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTEtat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTEtat> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTEtat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTEtat> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTEtat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTEtat value) throws Exception {
		BeanValidator<TaTEtat> validator = new BeanValidator<TaTEtat>(TaTEtat.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTEtat value, String propertyName) throws Exception {
		BeanValidator<TaTEtat> validator = new BeanValidator<TaTEtat>(TaTEtat.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTEtat transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTEtat findByCode(String code) {
		logger.debug("getting TaTEtat instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaTEtat a where a.codeTEtat='"+code+"'");
			TaTEtat instance = (TaTEtat)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}
