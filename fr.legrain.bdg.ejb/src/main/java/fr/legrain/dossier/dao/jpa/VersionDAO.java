package fr.legrain.dossier.dao.jpa;

// Generated 14 mai 2009 11:08:14 by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossier.dao.IVersionDAO;
import fr.legrain.dossier.model.TaVersion;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaVersion.
 * @see fr.legrain.dossier.dao.TaVersion
 * @author Hibernate Tools
 */
public class VersionDAO implements IVersionDAO {

	static Logger logger = Logger.getLogger(VersionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaVersion a";
	
	public VersionDAO(){
		//this(null);
	}
	
	public void persist(TaVersion transientInstance) {
		logger.debug("persisting TaVersion instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaVersion persistentInstance) {
		logger.debug("removing TaVersion instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaVersion merge(TaVersion detachedInstance) {
		logger.debug("merging TaVersion instance");
		try {
			TaVersion result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaVersion findById(int id) {
		logger.debug("getting TaVersion instance with id: " + id);
		try {
			TaVersion instance = entityManager.find(
					TaVersion.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * @return - l'unique instance de taInfoEntreprise si elle existe, sinon retourne null.
	 */
	public TaVersion findInstance() {
		int premierId = 1;
		logger.debug("getting TaVersion instance with id: "+premierId);
		try {
			TaVersion instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaVersion trouve, creation d'une nouvelle instance vide");
				instance = new TaVersion();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaVersion> selectAll() {
		logger.debug("selectAll TaVersion");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaVersion> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public int selectCount() {
		// dans ce cas on peu faire un selectAll car il ne doit y avoir qu'un seul enregistrement au maximum
		//return 1;
		return selectAll().size();
	}
	
	@Override
	public List<TaVersion> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaVersion> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaVersion> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaVersion> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaVersion value) throws Exception {
		BeanValidator<TaVersion> validator = new BeanValidator<TaVersion>(TaVersion.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaVersion value, String propertyName) throws Exception {
		BeanValidator<TaVersion> validator = new BeanValidator<TaVersion>(TaVersion.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaVersion transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaVersion findByCode(String code) {
		return null;
	}
}
