package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IGroupeDAO;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaGroupe.
 * @see fr.legrain.articles.dao.TaGroupe
 * @author Hibernate Tools
 */
public class GroupeDAO implements IGroupeDAO {

	private static final Log logger = LogFactory.getLog(GroupeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaGroupeDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaGroupe f order by f.codeGroupe";
	
	public GroupeDAO(){
//		this(null);
	}
	
//	public TaGroupeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaGroupe.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaGroupe());
//	}
	
//	public TaGroupe refresh(TaGroupe detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaGroupe.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaGroupe transientInstance) {
		logger.debug("persisting TaGroupe instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaGroupe persistentInstance) {
		logger.debug("removing TaGroupe instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdGroupe()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaGroupe merge(TaGroupe detachedInstance) {
		logger.debug("merging TaGroupe instance");
		try {
			TaGroupe result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaGroupe findByCode(String code) {
		logger.debug("getting TaGroupe instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaGroupe f where f.codeGroupe='"+code+"'");
			TaGroupe instance = (TaGroupe)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaGroupe findById(int id) {
		logger.debug("getting TaGroupe instance with id: " + id);
		try {
			TaGroupe instance = entityManager.find(TaGroupe.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaGroupe> selectAll() {
		logger.debug("selectAll TaGroupe");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaGroupe> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaGroupe entity,String field) throws ExceptLgr {	
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
	public List<TaGroupe> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaGroupe> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaGroupe> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaGroupe> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaGroupe value) throws Exception {
		BeanValidator<TaGroupe> validator = new BeanValidator<TaGroupe>(TaGroupe.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaGroupe value, String propertyName) throws Exception {
		BeanValidator<TaGroupe> validator = new BeanValidator<TaGroupe>(TaGroupe.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaGroupe transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
