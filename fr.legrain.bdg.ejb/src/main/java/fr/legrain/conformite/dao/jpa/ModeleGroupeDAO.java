package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IModeleGroupeDAO;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaModeleGroupe.
 * @see fr.legrain.articles.dao.TaModeleGroupe
 * @author Hibernate Tools
 */
public class ModeleGroupeDAO implements IModeleGroupeDAO {

	private static final Log logger = LogFactory.getLog(ModeleGroupeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaModeleGroupeDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaModeleGroupe f";
	
	public ModeleGroupeDAO(){
//		this(null);
	}
	
//	public TaModeleGroupeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaModeleGroupe.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaModeleGroupe());
//	}
	
//	public TaModeleGroupe refresh(TaModeleGroupe detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaModeleGroupe.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaModeleGroupe transientInstance) {
		logger.debug("persisting TaModeleGroupe instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaModeleGroupe persistentInstance) {
		logger.debug("removing TaModeleGroupe instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdModeleGroupe()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaModeleGroupe merge(TaModeleGroupe detachedInstance) {
		logger.debug("merging TaModeleGroupe instance");
		try {
			TaModeleGroupe result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaModeleGroupe findByCode(String code) {
		logger.debug("getting TaModeleGroupe instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaModeleGroupe f where f.codeGroupe='"+code+"'");
			TaModeleGroupe instance = (TaModeleGroupe)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaModeleGroupe findById(int id) {
		logger.debug("getting TaModeleGroupe instance with id: " + id);
		try {
			TaModeleGroupe instance = entityManager.find(TaModeleGroupe.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaModeleGroupe> selectAll() {
		logger.debug("selectAll TaModeleGroupe");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaModeleGroupe> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaModeleGroupe entity,String field) throws ExceptLgr {	
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
	public List<TaModeleGroupe> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaModeleGroupe> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaModeleGroupe> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaModeleGroupe> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaModeleGroupe value) throws Exception {
		BeanValidator<TaModeleGroupe> validator = new BeanValidator<TaModeleGroupe>(TaModeleGroupe.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaModeleGroupe value, String propertyName) throws Exception {
		BeanValidator<TaModeleGroupe> validator = new BeanValidator<TaModeleGroupe>(TaModeleGroupe.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaModeleGroupe transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
