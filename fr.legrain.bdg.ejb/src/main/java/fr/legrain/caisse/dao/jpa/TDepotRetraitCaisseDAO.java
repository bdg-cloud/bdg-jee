package fr.legrain.caisse.dao.jpa;

// Generated Aug 11, 2008 5:00:48 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.caisse.dao.IDepotRetraitCaisseDAO;
import fr.legrain.caisse.dao.ITDepotRetraitCaisseDAO;
import fr.legrain.caisse.dto.TaTDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaTDepotRetraitCaisse;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTDepotRetraitCaisse.
 * @see fr.legrain.articles.dao.TaTDepotRetraitCaisse
 * @author Hibernate Tools
 */
public class TDepotRetraitCaisseDAO implements ITDepotRetraitCaisseDAO {

	private static final Log logger = LogFactory.getLog(TDepotRetraitCaisseDAO.class);
	//static Logger logger = Logger.getLogger(TaTDepotRetraitCaisseDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaTDepotRetraitCaisse f order by f.codeTDepotRetraitCaisse";
	
	public TDepotRetraitCaisseDAO(){
//		this(null);
	}
	
//	public TaTDepotRetraitCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTDepotRetraitCaisse.class.getSimpleName());
//		initChampId(TaTDepotRetraitCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTDepotRetraitCaisse());
//	}
	
//	public TaTDepotRetraitCaisse refresh(TaTDepotRetraitCaisse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTDepotRetraitCaisse.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTDepotRetraitCaisse transientInstance) {
		logger.debug("persisting TaTDepotRetraitCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTDepotRetraitCaisse persistentInstance) {
		logger.debug("removing TaTDepotRetraitCaisse instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTDepotRetraitCaisse()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTDepotRetraitCaisse merge(TaTDepotRetraitCaisse detachedInstance) {
		logger.debug("merging TaTDepotRetraitCaisse instance");
		try {
			TaTDepotRetraitCaisse result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTDepotRetraitCaisse findById(int id) {
		logger.debug("getting TaTDepotRetraitCaisse instance with id: " + id);
		try {
			TaTDepotRetraitCaisse instance = entityManager.find(TaTDepotRetraitCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTDepotRetraitCaisse findByCode(String code) {
		logger.debug("getting TaTDepotRetraitCaisse instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTDepotRetraitCaisse f where f.codeTDepotRetraitCaisse='"+code+"'");
				TaTDepotRetraitCaisse instance = (TaTDepotRetraitCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaTDepotRetraitCaisseDTO> findByCodeLight(String code) {
		logger.debug("getting TaTDepotRetraitCaisse instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTDepotRetraitCaisse.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTDepotRetraitCaisse.QN.FIND_ALL_LIGHT);
			}

			List<TaTDepotRetraitCaisseDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaTDepotRetraitCaisse> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTDepotRetraitCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTDepotRetraitCaisse entity,String field) throws ExceptLgr {	
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
	public List<TaTDepotRetraitCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTDepotRetraitCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTDepotRetraitCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTDepotRetraitCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTDepotRetraitCaisse value) throws Exception {
		BeanValidator<TaTDepotRetraitCaisse> validator = new BeanValidator<TaTDepotRetraitCaisse>(TaTDepotRetraitCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTDepotRetraitCaisse value, String propertyName) throws Exception {
		BeanValidator<TaTDepotRetraitCaisse> validator = new BeanValidator<TaTDepotRetraitCaisse>(TaTDepotRetraitCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTDepotRetraitCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
