package fr.legrain.dossier.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossier.dao.IGroupePreferenceDAO;
import fr.legrain.dossier.model.TaGroupePreference;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaGroupePreference.
 * @see fr.legrain.dossier.dao.TaGroupePreference
 * @author Hibernate Tools
 */
public class GroupePreferenceDAO implements IGroupePreferenceDAO {

	static Logger logger = Logger.getLogger(GroupePreferenceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaGroupePreference a  order by a.groupe,a.cle";
	
	public GroupePreferenceDAO(){
		//this(null);
	}
	
	public void persist(TaGroupePreference transientInstance) {
		logger.debug("persisting TaGroupePreference instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaGroupePreference persistentInstance) {
		logger.debug("removing TaGroupePreference instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaGroupePreference merge(TaGroupePreference detachedInstance) {
		logger.debug("merging TaGroupePreference instance");
		try {
			TaGroupePreference result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaGroupePreference findById(int id) {
		logger.debug("getting TaGroupePreference instance with id: " + id);
		try {
			TaGroupePreference instance = entityManager.find(
					TaGroupePreference.class, id);
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
	public TaGroupePreference findInstance() {
		int premierId = 1;
		logger.debug("getting TaGroupePreference instance with id: "+premierId);
		try {
			TaGroupePreference instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaGroupePreference trouve, creation d'une nouvelle instance vide");
				instance = new TaGroupePreference();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaGroupePreference> findByGroupe(String groupe) {
		logger.debug("getting findByGoupe instance with groupe : " + groupe);
		try {
			if(!groupe.equals("")){
			Query query = entityManager.createQuery("select a from TaGroupePreference a where a.groupe='"+groupe+"'");
			List<TaGroupePreference> liste = query.getResultList();
			logger.debug("get successful");
			return liste;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaGroupePreference findByGoupeAndCle(String groupe,String cle) {
		try {
			if(!groupe.equals("")){
			Query query = entityManager.createQuery("select a from TaGroupePreference a where lower(a.groupe)='"+groupe.toLowerCase()+"' and lower(a.cle)='"+cle.toLowerCase()+"'");
			TaGroupePreference instance = (TaGroupePreference) query.getSingleResult();
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
	public List<TaGroupePreference> selectAll() {
		logger.debug("selectAll TaGroupePreference");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaGroupePreference> l = ejbQuery.getResultList();
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
	public List<TaGroupePreference> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaGroupePreference> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaGroupePreference> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaGroupePreference> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaGroupePreference value) throws Exception {
		BeanValidator<TaGroupePreference> validator = new BeanValidator<TaGroupePreference>(TaGroupePreference.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaGroupePreference value, String propertyName) throws Exception {
		BeanValidator<TaGroupePreference> validator = new BeanValidator<TaGroupePreference>(TaGroupePreference.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaGroupePreference transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaGroupePreference findByCode(String cle) {
		logger.debug("getting TaGroupePreference instance with cle: " + cle);
		try {
			if(!cle.equals("")){
			Query query = entityManager.createQuery("select a from TaGroupePreference a where a.cle='"+cle+"'");
			TaGroupePreference instance = (TaGroupePreference)query.getSingleResult();
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
