package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.ITypeConformiteDAO;
import fr.legrain.conformite.model.TaTypeConformite;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTypeConformite.
 * @see fr.legrain.articles.dao.TaTypeConformite
 * @author Hibernate Tools
 */
public class TypeConformiteDAO implements ITypeConformiteDAO {

	private static final Log logger = LogFactory.getLog(TypeConformiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaTypeConformiteDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaTypeConformite f";
	
	public TypeConformiteDAO(){
//		this(null);
	}
	
//	public TaTypeConformiteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaTypeConformite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTypeConformite());
//	}
	
//	public TaTypeConformite refresh(TaTypeConformite detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTypeConformite.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaTypeConformite transientInstance) {
		logger.debug("persisting TaTypeConformite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTypeConformite persistentInstance) {
		logger.debug("removing TaTypeConformite instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTypeConformite()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTypeConformite merge(TaTypeConformite detachedInstance) {
		logger.debug("merging TaTypeConformite instance");
		try {
			TaTypeConformite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaTypeConformite findByCode(String code) {
		logger.debug("getting TaTypeConformite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTypeConformite f where f.code='"+code+"'");
			TaTypeConformite instance = (TaTypeConformite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaTypeConformite findById(int id) {
		logger.debug("getting TaTypeConformite instance with id: " + id);
		try {
			TaTypeConformite instance = entityManager.find(TaTypeConformite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTypeConformite> selectAll() {
		logger.debug("selectAll TaTypeConformite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypeConformite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTypeConformite entity,String field) throws ExceptLgr {	
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
	public List<TaTypeConformite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeConformite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypeConformite value) throws Exception {
		BeanValidator<TaTypeConformite> validator = new BeanValidator<TaTypeConformite>(TaTypeConformite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeConformite value, String propertyName) throws Exception {
		BeanValidator<TaTypeConformite> validator = new BeanValidator<TaTypeConformite>(TaTypeConformite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeConformite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
