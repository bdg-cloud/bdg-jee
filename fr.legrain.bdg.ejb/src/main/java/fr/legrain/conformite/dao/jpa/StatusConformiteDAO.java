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
import fr.legrain.conformite.dao.IStatusConformiteDAO;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaStatusConformite.
 * @see fr.legrain.articles.dao.TaStatusConformite
 * @author Hibernate Tools
 */
public class StatusConformiteDAO implements IStatusConformiteDAO {

	private static final Log logger = LogFactory.getLog(StatusConformiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaStatusConformiteDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaStatusConformite f order by f.codeStatusConformite";
	
	public StatusConformiteDAO(){
//		this(null);
	}
	
//	public TaStatusConformiteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaStatusConformite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaStatusConformite());
//	}
	
//	public TaStatusConformite refresh(TaStatusConformite detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaStatusConformite.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaStatusConformite transientInstance) {
		logger.debug("persisting TaStatusConformite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaStatusConformite persistentInstance) {
		logger.debug("removing TaStatusConformite instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdStatusConformite()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaStatusConformite merge(TaStatusConformite detachedInstance) {
		logger.debug("merging TaStatusConformite instance");
		try {
			TaStatusConformite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStatusConformite findByCode(String code) {
		logger.debug("getting TaStatusConformite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStatusConformite f where f.codeStatusConformite='"+code+"'");
			TaStatusConformite instance = (TaStatusConformite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaStatusConformite findById(int id) {
		logger.debug("getting TaStatusConformite instance with id: " + id);
		try {
			TaStatusConformite instance = entityManager.find(TaStatusConformite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaStatusConformite> selectAll() {
		logger.debug("selectAll TaStatusConformite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaStatusConformite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaStatusConformite entity,String field) throws ExceptLgr {	
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
	public List<TaStatusConformite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStatusConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStatusConformite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStatusConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStatusConformite value) throws Exception {
		BeanValidator<TaStatusConformite> validator = new BeanValidator<TaStatusConformite>(TaStatusConformite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStatusConformite value, String propertyName) throws Exception {
		BeanValidator<TaStatusConformite> validator = new BeanValidator<TaStatusConformite>(TaStatusConformite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStatusConformite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
