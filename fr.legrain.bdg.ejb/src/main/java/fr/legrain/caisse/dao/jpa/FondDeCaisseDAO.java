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
import fr.legrain.caisse.dao.IFondDeCaisseDAO;
import fr.legrain.caisse.dto.TaFondDeCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaFondDeCaisse.
 * @see fr.legrain.articles.dao.TaFondDeCaisse
 * @author Hibernate Tools
 */
public class FondDeCaisseDAO implements IFondDeCaisseDAO {

	private static final Log logger = LogFactory.getLog(FondDeCaisseDAO.class);
	//static Logger logger = Logger.getLogger(TaFondDeCaisseDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaFondDeCaisse f order by f.codeTva";
	
	public FondDeCaisseDAO(){
//		this(null);
	}
	
//	public TaFondDeCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaFondDeCaisse.class.getSimpleName());
//		initChampId(TaFondDeCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaFondDeCaisse());
//	}
	
//	public TaFondDeCaisse refresh(TaFondDeCaisse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaFondDeCaisse.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaFondDeCaisse transientInstance) {
		logger.debug("persisting TaFondDeCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaFondDeCaisse persistentInstance) {
		logger.debug("removing TaFondDeCaisse instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdFondDeCaisse()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaFondDeCaisse merge(TaFondDeCaisse detachedInstance) {
		logger.debug("merging TaFondDeCaisse instance");
		try {
			TaFondDeCaisse result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaFondDeCaisse findById(int id) {
		logger.debug("getting TaFondDeCaisse instance with id: " + id);
		try {
			TaFondDeCaisse instance = entityManager.find(TaFondDeCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaFondDeCaisse findByCode(String code) {
		logger.debug("getting TaFondDeCaisse instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaFondDeCaisse f where f.codeTva='"+code+"'");
				TaFondDeCaisse instance = (TaFondDeCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaFondDeCaisseDTO> findByCodeLight(String code) {
		logger.debug("getting TaFondDeCaisse instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaFondDeCaisse.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaFondDeCaisse.QN.FIND_ALL_LIGHT);
			}

			List<TaFondDeCaisseDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	
	public List<TaFondDeCaisse> findBySessionCaisseCourante() {
		List<TaFondDeCaisse> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaFondDeCaisse.QN.FIND_BY_SESSION_CAISSE_COURANTE);
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
	public TaFondDeCaisse findByTaux(String taux) {
		logger.debug("getting TaFondDeCaisse instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaFondDeCaisse f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				TaFondDeCaisse instance = (TaFondDeCaisse)query.getSingleResult();
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
	public List<TaFondDeCaisse> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFondDeCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaFondDeCaisse entity,String field) throws ExceptLgr {	
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
	public List<TaFondDeCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFondDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFondDeCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFondDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFondDeCaisse value) throws Exception {
		BeanValidator<TaFondDeCaisse> validator = new BeanValidator<TaFondDeCaisse>(TaFondDeCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFondDeCaisse value, String propertyName) throws Exception {
		BeanValidator<TaFondDeCaisse> validator = new BeanValidator<TaFondDeCaisse>(TaFondDeCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFondDeCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
