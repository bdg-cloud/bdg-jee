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
import fr.legrain.caisse.dao.ITLSessionCaisseDAO;
import fr.legrain.caisse.dto.TaTLSessionCaisseDTO;
import fr.legrain.caisse.model.TaTLSessionCaisse;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTLSessionCaisse.
 * @see fr.legrain.articles.dao.TaTLSessionCaisse
 * @author Hibernate Tools
 */
public class TLSessionCaisseDAO implements ITLSessionCaisseDAO {

	private static final Log logger = LogFactory.getLog(TLSessionCaisseDAO.class);
	//static Logger logger = Logger.getLogger(TaTLSessionCaisseDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaTLSessionCaisse f order by f.codeTLSessionCaisse";
	
	public TLSessionCaisseDAO(){
//		this(null);
	}
	
//	public TaTLSessionCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTLSessionCaisse.class.getSimpleName());
//		initChampId(TaTLSessionCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTLSessionCaisse());
//	}
	
//	public TaTLSessionCaisse refresh(TaTLSessionCaisse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTLSessionCaisse.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTLSessionCaisse transientInstance) {
		logger.debug("persisting TaTLSessionCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTLSessionCaisse persistentInstance) {
		logger.debug("removing TaTLSessionCaisse instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTLSessionCaisse()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTLSessionCaisse merge(TaTLSessionCaisse detachedInstance) {
		logger.debug("merging TaTLSessionCaisse instance");
		try {
			TaTLSessionCaisse result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTLSessionCaisse findById(int id) {
		logger.debug("getting TaTLSessionCaisse instance with id: " + id);
		try {
			TaTLSessionCaisse instance = entityManager.find(TaTLSessionCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTLSessionCaisse findByCode(String code) {
		logger.debug("getting TaTLSessionCaisse instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTLSessionCaisse f where f.codeTLSessionCaisse='"+code+"'");
				TaTLSessionCaisse instance = (TaTLSessionCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaTLSessionCaisseDTO> findByCodeLight(String code) {
		logger.debug("getting TaTLSessionCaisse instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTLSessionCaisse.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTLSessionCaisse.QN.FIND_ALL_LIGHT);
			}

			List<TaTLSessionCaisseDTO> l = query.getResultList();
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
	public List<TaTLSessionCaisse> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTLSessionCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTLSessionCaisse entity,String field) throws ExceptLgr {	
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
	public List<TaTLSessionCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTLSessionCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTLSessionCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTLSessionCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTLSessionCaisse value) throws Exception {
		BeanValidator<TaTLSessionCaisse> validator = new BeanValidator<TaTLSessionCaisse>(TaTLSessionCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTLSessionCaisse value, String propertyName) throws Exception {
		BeanValidator<TaTLSessionCaisse> validator = new BeanValidator<TaTLSessionCaisse>(TaTLSessionCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTLSessionCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
