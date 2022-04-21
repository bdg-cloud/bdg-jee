package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IModeleBaremeDAO;
import fr.legrain.conformite.model.TaModeleBareme;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaModeleBareme.
 * @see fr.legrain.articles.dao.TaModeleBareme
 * @author Hibernate Tools
 */
public class ModeleBaremeDAO implements IModeleBaremeDAO {

	private static final Log logger = LogFactory.getLog(ModeleBaremeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaModeleBaremeDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaModeleBareme f";
	
	public ModeleBaremeDAO(){
//		this(null);
	}
	
//	public TaModeleBaremeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaModeleBareme.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaModeleBareme());
//	}
	
//	public TaModeleBareme refresh(TaModeleBareme detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaModeleBareme.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaModeleBareme transientInstance) {
		logger.debug("persisting TaModeleBareme instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaModeleBareme persistentInstance) {
		logger.debug("removing TaModeleBareme instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdModeleBareme()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaModeleBareme merge(TaModeleBareme detachedInstance) {
		logger.debug("merging TaModeleBareme instance");
		try {
			TaModeleBareme result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaModeleBareme findByCode(String code) {
		logger.debug("getting TaModeleBareme instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaModeleBareme f where f.codeFamille='"+code+"'");
			TaModeleBareme instance = (TaModeleBareme)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaModeleBareme findById(int id) {
		logger.debug("getting TaModeleBareme instance with id: " + id);
		try {
			TaModeleBareme instance = entityManager.find(TaModeleBareme.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaModeleBareme> selectAll() {
		logger.debug("selectAll TaModeleBareme");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaModeleBareme> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaModeleBareme entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaModeleBareme> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaModeleBareme> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaModeleBareme> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaModeleBareme> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaModeleBareme value) throws Exception {
		BeanValidator<TaModeleBareme> validator = new BeanValidator<TaModeleBareme>(TaModeleBareme.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaModeleBareme value, String propertyName) throws Exception {
		BeanValidator<TaModeleBareme> validator = new BeanValidator<TaModeleBareme>(TaModeleBareme.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaModeleBareme transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
