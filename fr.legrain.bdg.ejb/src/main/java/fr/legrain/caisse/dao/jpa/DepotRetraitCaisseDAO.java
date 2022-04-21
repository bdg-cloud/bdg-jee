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
import fr.legrain.caisse.dto.TaDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaDepotRetraitCaisse.
 * @see fr.legrain.articles.dao.TaDepotRetraitCaisse
 * @author Hibernate Tools
 */
public class DepotRetraitCaisseDAO implements IDepotRetraitCaisseDAO {

	private static final Log logger = LogFactory.getLog(DepotRetraitCaisseDAO.class);
	//static Logger logger = Logger.getLogger(TaDepotRetraitCaisseDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaDepotRetraitCaisse f order by f.codeTva";
	
	public DepotRetraitCaisseDAO(){
//		this(null);
	}
	
//	public TaDepotRetraitCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaDepotRetraitCaisse.class.getSimpleName());
//		initChampId(TaDepotRetraitCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaDepotRetraitCaisse());
//	}
	
//	public TaDepotRetraitCaisse refresh(TaDepotRetraitCaisse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaDepotRetraitCaisse.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaDepotRetraitCaisse transientInstance) {
		logger.debug("persisting TaDepotRetraitCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaDepotRetraitCaisse persistentInstance) {
		logger.debug("removing TaDepotRetraitCaisse instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdDepotRetraitCaisse()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaDepotRetraitCaisse merge(TaDepotRetraitCaisse detachedInstance) {
		logger.debug("merging TaDepotRetraitCaisse instance");
		try {
			TaDepotRetraitCaisse result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaDepotRetraitCaisse findById(int id) {
		logger.debug("getting TaDepotRetraitCaisse instance with id: " + id);
		try {
			TaDepotRetraitCaisse instance = entityManager.find(TaDepotRetraitCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaDepotRetraitCaisse findByCode(String code) {
		logger.debug("getting TaDepotRetraitCaisse instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaDepotRetraitCaisse f where f.codeTva='"+code+"'");
				TaDepotRetraitCaisse instance = (TaDepotRetraitCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaDepotRetraitCaisseDTO> findByCodeLight(String code) {
		logger.debug("getting TaDepotRetraitCaisse instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaDepotRetraitCaisse.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaDepotRetraitCaisse.QN.FIND_ALL_LIGHT);
			}

			List<TaDepotRetraitCaisseDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	
	public List<TaDepotRetraitCaisse> findBySessionCaisseCourante() {
		List<TaDepotRetraitCaisse> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaDepotRetraitCaisse.QN.FIND_BY_SESSION_CAISSE_COURANTE);
//			query.setParameter(1, codeTiers);
			result = query.getResultList();
			return result;
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
	public TaDepotRetraitCaisse findByTaux(String taux) {
		logger.debug("getting TaDepotRetraitCaisse instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaDepotRetraitCaisse f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				TaDepotRetraitCaisse instance = (TaDepotRetraitCaisse)query.getSingleResult();
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
	public List<TaDepotRetraitCaisse> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaDepotRetraitCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaDepotRetraitCaisse entity,String field) throws ExceptLgr {	
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
	public List<TaDepotRetraitCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaDepotRetraitCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaDepotRetraitCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaDepotRetraitCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaDepotRetraitCaisse value) throws Exception {
		BeanValidator<TaDepotRetraitCaisse> validator = new BeanValidator<TaDepotRetraitCaisse>(TaDepotRetraitCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaDepotRetraitCaisse value, String propertyName) throws Exception {
		BeanValidator<TaDepotRetraitCaisse> validator = new BeanValidator<TaDepotRetraitCaisse>(TaDepotRetraitCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaDepotRetraitCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
