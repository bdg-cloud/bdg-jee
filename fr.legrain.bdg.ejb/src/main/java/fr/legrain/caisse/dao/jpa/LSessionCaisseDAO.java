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
import fr.legrain.caisse.dao.ILSessionCaisseDAO;
import fr.legrain.caisse.dto.TaLSessionCaisseDTO;
import fr.legrain.caisse.model.TaLSessionCaisse;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLSessionCaisse.
 * @see fr.legrain.articles.dao.TaLSessionCaisse
 * @author Hibernate Tools
 */
public class LSessionCaisseDAO implements ILSessionCaisseDAO {

	private static final Log logger = LogFactory.getLog(LSessionCaisseDAO.class);
	//static Logger logger = Logger.getLogger(TaLSessionCaisseDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaLSessionCaisse f order by f.codeTva";
	
	public LSessionCaisseDAO(){
//		this(null);
	}
	
//	public TaLSessionCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLSessionCaisse.class.getSimpleName());
//		initChampId(TaLSessionCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLSessionCaisse());
//	}
	
//	public TaLSessionCaisse refresh(TaLSessionCaisse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLSessionCaisse.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLSessionCaisse transientInstance) {
		logger.debug("persisting TaLSessionCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaLSessionCaisse persistentInstance) {
		logger.debug("removing TaLSessionCaisse instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdLSessionCaisse()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaLSessionCaisse merge(TaLSessionCaisse detachedInstance) {
		logger.debug("merging TaLSessionCaisse instance");
		try {
			TaLSessionCaisse result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaLSessionCaisse findById(int id) {
		logger.debug("getting TaLSessionCaisse instance with id: " + id);
		try {
			TaLSessionCaisse instance = entityManager.find(TaLSessionCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLSessionCaisse findByCode(String code) {
		logger.debug("getting TaLSessionCaisse instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaLSessionCaisse f where f.codeTva='"+code+"'");
				TaLSessionCaisse instance = (TaLSessionCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaLSessionCaisseDTO> findByCodeLight(String code) {
		logger.debug("getting TaLSessionCaisse instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaLSessionCaisse.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaLSessionCaisse.QN.FIND_ALL_LIGHT);
			}

			List<TaLSessionCaisseDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	
	/**
	 * Retourne un code code TVA sur vente (commence par V), pour un taux particulier
	 * @param taux
	 * @return
	 */
	public TaLSessionCaisse findByTaux(String taux) {
		logger.debug("getting TaLSessionCaisse instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaLSessionCaisse f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				TaLSessionCaisse instance = (TaLSessionCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLSessionCaisse> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLSessionCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaLSessionCaisse entity,String field) throws ExceptLgr {	
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
	public List<TaLSessionCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLSessionCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLSessionCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLSessionCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLSessionCaisse value) throws Exception {
		BeanValidator<TaLSessionCaisse> validator = new BeanValidator<TaLSessionCaisse>(TaLSessionCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLSessionCaisse value, String propertyName) throws Exception {
		BeanValidator<TaLSessionCaisse> validator = new BeanValidator<TaLSessionCaisse>(TaLSessionCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLSessionCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
