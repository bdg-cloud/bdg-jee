package fr.legrain.caisse.dao.jpa;

// Generated Aug 11, 2008 5:00:48 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.caisse.dao.IDepotRetraitCaisseDAO;
import fr.legrain.caisse.dao.ITFondDeCaisseDAO;
import fr.legrain.caisse.dto.TaTFondDeCaisseDTO;
import fr.legrain.caisse.model.TaTFondDeCaisse;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTFondDeCaisse.
 * @see fr.legrain.articles.dao.TaTFondDeCaisse
 * @author Hibernate Tools
 */
public class TFondDeCaisseDAO implements ITFondDeCaisseDAO {

	private static final Log logger = LogFactory.getLog(TFondDeCaisseDAO.class);
	//static Logger logger = Logger.getLogger(TaTFondDeCaisseDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaTFondDeCaisse f order by f.codeTFondDeCaisse";
	
	public TFondDeCaisseDAO(){
//		this(null);
	}
	
//	public TaTFondDeCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTFondDeCaisse.class.getSimpleName());
//		initChampId(TaTFondDeCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTFondDeCaisse());
//	}
	
//	public TaTFondDeCaisse refresh(TaTFondDeCaisse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTFondDeCaisse.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTFondDeCaisse transientInstance) {
		logger.debug("persisting TaTFondDeCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTFondDeCaisse persistentInstance) {
		logger.debug("removing TaTFondDeCaisse instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTFondDeCaisse()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTFondDeCaisse merge(TaTFondDeCaisse detachedInstance) {
		logger.debug("merging TaTFondDeCaisse instance");
		try {
			TaTFondDeCaisse result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTFondDeCaisse findById(int id) {
		logger.debug("getting TaTFondDeCaisse instance with id: " + id);
		try {
			TaTFondDeCaisse instance = entityManager.find(TaTFondDeCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTFondDeCaisse findByCode(String code) {
		logger.debug("getting TaTFondDeCaisse instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTFondDeCaisse f where f.codeTFondDeCaisse='"+code+"'");
				TaTFondDeCaisse instance = (TaTFondDeCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaTFondDeCaisseDTO> findByCodeLight(String code) {
		logger.debug("getting TaTFondDeCaisse instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTFondDeCaisse.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTFondDeCaisse.QN.FIND_ALL_LIGHT);
			}

			List<TaTFondDeCaisseDTO> l = query.getResultList();
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
	public List<TaTFondDeCaisse> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTFondDeCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTFondDeCaisse entity,String field) throws ExceptLgr {	
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
	public List<TaTFondDeCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTFondDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTFondDeCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTFondDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTFondDeCaisse value) throws Exception {
		BeanValidator<TaTFondDeCaisse> validator = new BeanValidator<TaTFondDeCaisse>(TaTFondDeCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTFondDeCaisse value, String propertyName) throws Exception {
		BeanValidator<TaTFondDeCaisse> validator = new BeanValidator<TaTFondDeCaisse>(TaTFondDeCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTFondDeCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
