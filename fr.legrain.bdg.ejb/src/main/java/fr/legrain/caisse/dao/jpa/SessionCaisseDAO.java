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
import fr.legrain.caisse.dao.ISessionCaisseDAO;
import fr.legrain.caisse.dto.TaSessionCaisseDTO;
import fr.legrain.caisse.model.TaSessionCaisse;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaSessionCaisse.
 * @see fr.legrain.articles.dao.TaSessionCaisse
 * @author Hibernate Tools
 */
public class SessionCaisseDAO implements ISessionCaisseDAO {

	private static final Log logger = LogFactory.getLog(SessionCaisseDAO.class);
	//static Logger logger = Logger.getLogger(TaSessionCaisseDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaSessionCaisse f order by f.codeTva";
	
	public SessionCaisseDAO(){
//		this(null);
	}
	
	public void persist(TaSessionCaisse transientInstance) {
		logger.debug("persisting TaSessionCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaSessionCaisse persistentInstance) {
		logger.debug("removing TaSessionCaisse instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdSessionCaisse()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaSessionCaisse merge(TaSessionCaisse detachedInstance) {
		logger.debug("merging TaSessionCaisse instance");
		try {
			TaSessionCaisse result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaSessionCaisse findById(int id) {
		logger.debug("getting TaSessionCaisse instance with id: " + id);
		try {
			TaSessionCaisse instance = entityManager.find(TaSessionCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaSessionCaisse findByCode(String code) {
		logger.debug("getting TaSessionCaisse instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaSessionCaisse f where f.codeSessionCaisse='"+code+"'");
				TaSessionCaisse instance = (TaSessionCaisse)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaSessionCaisse findSessionCaisseActive(int idUtilisateur, String numeroCaisse) {
		logger.debug("getting TaSessionCaisse instance with idUtilisateur: " + idUtilisateur);
		try {
			Query query = entityManager.createQuery("select f from TaSessionCaisse f where f.taUtilisateur.id="+idUtilisateur+" and f.dateFinSession is null and (f.verrouillageTicketZ is null or f.verrouillageTicketZ=false)");
			TaSessionCaisse instance = (TaSessionCaisse)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaSessionCaisseDTO> findByCodeLight(String code) {
		logger.debug("getting TaSessionCaisse instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaSessionCaisse.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaSessionCaisse.QN.FIND_ALL_LIGHT);
			}

			List<TaSessionCaisseDTO> l = query.getResultList();
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
	public TaSessionCaisse findByTaux(String taux) {
		logger.debug("getting TaSessionCaisse instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaSessionCaisse f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				TaSessionCaisse instance = (TaSessionCaisse)query.getSingleResult();
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
	public List<TaSessionCaisse> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaSessionCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaSessionCaisse entity,String field) throws ExceptLgr {	
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
	public List<TaSessionCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaSessionCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaSessionCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaSessionCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaSessionCaisse value) throws Exception {
		BeanValidator<TaSessionCaisse> validator = new BeanValidator<TaSessionCaisse>(TaSessionCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaSessionCaisse value, String propertyName) throws Exception {
		BeanValidator<TaSessionCaisse> validator = new BeanValidator<TaSessionCaisse>(TaSessionCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaSessionCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
