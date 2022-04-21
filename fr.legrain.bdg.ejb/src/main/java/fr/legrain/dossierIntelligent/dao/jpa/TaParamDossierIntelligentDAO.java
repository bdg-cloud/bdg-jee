package fr.legrain.dossierIntelligent.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossierIntelligent.dao.ITaParamDossierIntelligentDAO;
import fr.legrain.dossierIntelligent.model.TaParamDossierIntel;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.model.old.TaAdresse
 * @author Hibernate Tools
 */
public class TaParamDossierIntelligentDAO implements ITaParamDossierIntelligentDAO {

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaParamDossierIntelligentDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaParamDossierIntel a";
	
	public TaParamDossierIntelligentDAO(){

	}
	
	
	public void persist(TaParamDossierIntel transientInstance) {
		logger.debug("persisting TaParamDossierIntel instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaParamDossierIntel persistentInstance) {
		logger.debug("removing TaParamDossierIntel instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaParamDossierIntel merge(TaParamDossierIntel detachedInstance) {
		logger.debug("merging TaParamDossierIntel instance");
		try {
			TaParamDossierIntel result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaParamDossierIntel findById(int id) {
		logger.debug("getting TaParamDossierIntel instance with id: " + id);
		try {
			TaParamDossierIntel instance = entityManager.find(TaParamDossierIntel.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaParamDossierIntel findByCode(String code) {
		logger.debug("getting TaParamDossierIntel instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaParamDossierIntel a where upper(a.mot)='"+code.toUpperCase().replace("'", "''")+"'");
			TaParamDossierIntel instance = (TaParamDossierIntel)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaParamDossierIntel> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaParamDossierIntel");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaParamDossierIntel> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaParamDossierIntel> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaParamDossierIntel> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaParamDossierIntel> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaParamDossierIntel> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaParamDossierIntel value) throws Exception {
		BeanValidator<TaParamDossierIntel> validator = new BeanValidator<TaParamDossierIntel>(TaParamDossierIntel.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaParamDossierIntel value, String propertyName) throws Exception {
		BeanValidator<TaParamDossierIntel> validator = new BeanValidator<TaParamDossierIntel>(TaParamDossierIntel.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaParamDossierIntel transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	


}