package fr.legrain.autorisations.autorisations.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.autorisations.autorisation.model.TaAutorisations;
import fr.legrain.autorisations.autorisations.dao.IAutorisationsDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.model.old.TaAdresse
 * @author Hibernate Tools
 */
public class AutorisationsDAO implements IAutorisationsDAO {

	static Logger logger = Logger.getLogger(AutorisationsDAO.class);
	
	@PersistenceContext(unitName = "autorisations")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaAutorisations a";
	
	public AutorisationsDAO(){
	}



	@Override
	public void persist(TaAutorisations transientInstance) {
		logger.debug("persisting TaAutorisations instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	@Override
	public void remove(TaAutorisations persistentInstance) {
		logger.debug("removing TaAutorisations instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	@Override
	public TaAutorisations merge(TaAutorisations detachedInstance) {
		logger.debug("merging TaAutorisations instance");
		try {
			TaAutorisations result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}


	@Override
	public TaAutorisations findById(int id) {
		logger.debug("getting TaAutorisations instance with id: " + id);
		try {
			TaAutorisations instance = entityManager.find(TaAutorisations.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	



	@Override
	public List<TaAutorisations> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaAutorisations");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAutorisations> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaAutorisations> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAutorisations> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAutorisations> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAutorisations> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAutorisations value) throws Exception {
		BeanValidator<TaAutorisations> validator = new BeanValidator<TaAutorisations>(TaAutorisations.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAutorisations value, String propertyName) throws Exception {
		BeanValidator<TaAutorisations> validator = new BeanValidator<TaAutorisations>(TaAutorisations.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAutorisations transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaAutorisations findByCode(String code) {
		logger.debug("getting TaAutorisations instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaAutorisations a where a.codeDossier='"+code+"'");
			TaAutorisations instance = (TaAutorisations)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}		

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaAutorisations f where f.codeDossier='"+code+"'");
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
	
	public TaAutorisations findByDossierTypeProduit(String codeDossier, Integer idTypeProduit) {
		try {
			if( codeDossier!=null &&!codeDossier.equals("") && idTypeProduit!=null && idTypeProduit!=0){
			Query query = entityManager.createQuery("select a from TaAutorisations a where a.codeDossier='"+codeDossier+"' "
					+ " and a.taTypeProduit.idType="+idTypeProduit);
			TaAutorisations instance = (TaAutorisations)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}

}
