package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaParamPublipostage;
import fr.legrain.tiers.dao.IParamPublipostageDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.dao.TaTLiens
 * @author Hibernate Tools
 */
public class TaParamPublipostageDAO implements IParamPublipostageDAO {

	//private static final Log logger = LogFactory.getLog(TaTLiensDAO.class);
	static Logger logger = Logger.getLogger(TaParamPublipostageDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaParamPublipostage a ";
	
	public TaParamPublipostageDAO() {
		this(null);
	}
	
	public TaParamPublipostageDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaParamPublipostage.class.getSimpleName());
//		initChampId(TaParamPublipostage.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaParamPublipostage());
	}
	
//	public TaParamPublipostage refresh(TaParamPublipostage detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaParamPublipostage.class, 
////		    		detachedInstance.getId());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaParamPublipostage transientInstance) {
		logger.debug("persisting TaParamPublipostage instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaParamPublipostage persistentInstance) {
		logger.debug("removing TaParamPublipostage instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaParamPublipostage merge(TaParamPublipostage detachedInstance) {
		logger.debug("merging TaParamPublipostage instance");
		try {
			TaParamPublipostage result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaParamPublipostage findById(int id) {
		logger.debug("getting TaParamPublipostage instance with id: " + id);
		try {
			TaParamPublipostage instance = entityManager.find(TaParamPublipostage.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * @return - l'unique instance de TaParamPublipostage si elle existe, sinon retourne null.
	 */
	public TaParamPublipostage findInstance() {
		int premierId = 1;
		logger.debug("getting TaParamPublipostage instance with id: "+premierId);
		try {
			TaParamPublipostage instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaParamPublipostage trouve, creation d'une nouvelle instance vide");
				instance = new TaParamPublipostage();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParamPublipostage> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaParamPublipostage");
		try {  
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaParamPublipostage> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParamPublipostage> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaParamPublipostage> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaParamPublipostage> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaParamPublipostage> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaParamPublipostage value) throws Exception {
		BeanValidator<TaParamPublipostage> validator = new BeanValidator<TaParamPublipostage>(TaParamPublipostage.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaParamPublipostage value, String propertyName) throws Exception {
		BeanValidator<TaParamPublipostage> validator = new BeanValidator<TaParamPublipostage>(TaParamPublipostage.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaParamPublipostage transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaParamPublipostage findByCode(String code) {
		return null;
	}
	

}
