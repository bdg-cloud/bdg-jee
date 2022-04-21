package fr.legrain.dossier.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossier.dao.IAutorisationsDossierDAO;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.model.old.TaAdresse
 * @author Hibernate Tools
 */
public class AutorisationsDossierDAO implements IAutorisationsDossierDAO {

	static Logger logger = Logger.getLogger(AutorisationsDossierDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaAutorisationsDossier a";
	
	public AutorisationsDossierDAO(){
	}



	@Override
	public void persist(TaAutorisationsDossier transientInstance) {
		logger.debug("persisting TaAutorisationsDossier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	@Override
	public void remove(TaAutorisationsDossier persistentInstance) {
		logger.debug("removing TaAutorisationsDossier instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	@Override
	public TaAutorisationsDossier merge(TaAutorisationsDossier detachedInstance) {
		logger.debug("merging TaAutorisationsDossier instance");
		try {
			TaAutorisationsDossier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}


	@Override
	public TaAutorisationsDossier findById(int id) {
		logger.debug("getting TaAutorisationsDossier instance with id: " + id);
		try {
			TaAutorisationsDossier instance = entityManager.find(TaAutorisationsDossier.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * @return - l'unique instance de taAutorisationsDossier
	 */
	public TaAutorisationsDossier findInstance() {
		int premierId = 1;
		logger.debug("getting TaAutorisationsDossier instance with id: "+premierId);
		try {
			TaAutorisationsDossier instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaAutorisationsDossier trouve, creation d'une nouvelle instance vide");
				instance = new TaAutorisationsDossier();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public List<TaAutorisationsDossier> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaAutorisationsDossier");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAutorisationsDossier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaAutorisationsDossier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAutorisationsDossier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAutorisationsDossier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAutorisationsDossier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAutorisationsDossier value) throws Exception {
		BeanValidator<TaAutorisationsDossier> validator = new BeanValidator<TaAutorisationsDossier>(TaAutorisationsDossier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAutorisationsDossier value, String propertyName) throws Exception {
		BeanValidator<TaAutorisationsDossier> validator = new BeanValidator<TaAutorisationsDossier>(TaAutorisationsDossier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAutorisationsDossier transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaAutorisationsDossier findByCode(String code) {
		logger.debug("getting TaAutorisationsDossier instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaAutorisationsDossier a where a.codeTiers='"+code+"'");
			TaAutorisationsDossier instance = (TaAutorisationsDossier)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}		

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaAutorisationsDossier f where f.codeTiers='"+code+"'");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	
	public TaAutorisationsDossier findByTiersTypeProduit(String codeTiers) {
		try {
			if(codeTiers!=null && codeTiers.equals("")){
			Query query = entityManager.createQuery("select a from TaAutorisationsDossier a where a.codeTiers='"+codeTiers+"' ");
			TaAutorisationsDossier instance = (TaAutorisationsDossier)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
}
