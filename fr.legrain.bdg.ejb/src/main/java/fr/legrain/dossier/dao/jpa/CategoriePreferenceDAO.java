package fr.legrain.dossier.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossier.dao.ICategoriePreferenceDAO;
import fr.legrain.dossier.model.TaCategoriePreference;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCategoriePreference.
 * @see fr.legrain.dossier.dao.TaCategoriePreference
 * @author Hibernate Tools
 */
public class CategoriePreferenceDAO implements ICategoriePreferenceDAO {

	static Logger logger = Logger.getLogger(CategoriePreferenceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaCategoriePreference a  order by a.position, a.libelleCategoriePreference";
	
	public CategoriePreferenceDAO(){
		//this(null);
	}
	
	public void persist(TaCategoriePreference transientInstance) {
		logger.debug("persisting TaCategoriePreference instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaCategoriePreference persistentInstance) {
		logger.debug("removing TaCategoriePreference instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaCategoriePreference merge(TaCategoriePreference detachedInstance) {
		logger.debug("merging TaCategoriePreference instance");
		try {
			TaCategoriePreference result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaCategoriePreference findById(int id) {
		logger.debug("getting TaCategoriePreference instance with id: " + id);
		try {
			TaCategoriePreference instance = entityManager.find(
					TaCategoriePreference.class, id);
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
	public TaCategoriePreference findInstance() {
		int premierId = 1;
		logger.debug("getting TaCategoriePreference instance with id: "+premierId);
		try {
			TaCategoriePreference instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaCategoriePreference trouve, creation d'une nouvelle instance vide");
				instance = new TaCategoriePreference();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaCategoriePreference> findByGroupe(String groupe) {
		logger.debug("getting findByGoupe instance with groupe : " + groupe);
		try {
			if(!groupe.equals("")){
			Query query = entityManager.createQuery("select a from TaCategoriePreference a where a.groupe='"+groupe+"'");
			List<TaCategoriePreference> liste = query.getResultList();
			logger.debug("get successful");
			return liste;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaCategoriePreference findByGoupeAndCle(String groupe,String cle) {
		try {
			if(!groupe.equals("")){
			Query query = entityManager.createQuery("select a from TaCategoriePreference a where lower(a.groupe)='"+groupe.toLowerCase()+"' and lower(a.cle)='"+cle.toLowerCase()+"'");
			TaCategoriePreference instance = (TaCategoriePreference) query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	@Override
	public List<TaCategoriePreference> selectAll() {
		logger.debug("selectAll TaCategoriePreference");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCategoriePreference> l = ejbQuery.getResultList();
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
	public List<TaCategoriePreference> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCategoriePreference> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCategoriePreference> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCategoriePreference> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCategoriePreference value) throws Exception {
		BeanValidator<TaCategoriePreference> validator = new BeanValidator<TaCategoriePreference>(TaCategoriePreference.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCategoriePreference value, String propertyName) throws Exception {
		BeanValidator<TaCategoriePreference> validator = new BeanValidator<TaCategoriePreference>(TaCategoriePreference.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCategoriePreference transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaCategoriePreference findByCode(String cle) {
		logger.debug("getting TaCategoriePreference instance with cle: " + cle);
		try {
			if(!cle.equals("")){
			Query query = entityManager.createQuery("select a from TaCategoriePreference a where a.cle='"+cle+"'");
			TaCategoriePreference instance = (TaCategoriePreference)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


}
