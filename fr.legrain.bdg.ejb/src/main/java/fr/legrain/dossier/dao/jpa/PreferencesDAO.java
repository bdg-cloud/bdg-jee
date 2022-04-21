package fr.legrain.dossier.dao.jpa;

// Generated 14 mai 2009 11:08:14 by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossier.dao.IPreferencesDAO;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaPreferences.
 * @see fr.legrain.dossier.dao.TaPreferences
 * @author Hibernate Tools
 */
public class PreferencesDAO implements IPreferencesDAO {

	static Logger logger = Logger.getLogger(PreferencesDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaPreferences a  order by a.position,a.libelle";
	
	public PreferencesDAO(){
		//this(null);
	}
	
	public void persist(TaPreferences transientInstance) {
		logger.debug("persisting TaPreferences instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaPreferences persistentInstance) {
		logger.debug("removing TaPreferences instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaPreferences merge(TaPreferences detachedInstance) {
		logger.debug("merging TaPreferences instance");
		try {
			TaPreferences result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaPreferences findById(int id) {
		logger.debug("getting TaPreferences instance with id: " + id);
		try {
			TaPreferences instance = entityManager.find(
					TaPreferences.class, id);
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
	public TaPreferences findInstance() {
		int premierId = 1;
		logger.debug("getting TaPreferences instance with id: "+premierId);
		try {
			TaPreferences instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaPreferences trouve, creation d'une nouvelle instance vide");
				instance = new TaPreferences();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaPreferences> findByGroupe(String groupe) {
		logger.debug("getting findByGoupe instance with groupe : " + groupe);
		try {
			if(!groupe.equals("")){
			Query query = entityManager.createQuery("select a from TaPreferences a where a.groupe='"+groupe+"'");
			List<TaPreferences> liste = query.getResultList();
			logger.debug("get successful");
			return liste;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaPreferences findByGoupeAndCle(String groupe,String cle) {
		try {
			if(!groupe.equals("")){
			Query query = entityManager.createQuery("select a from TaPreferences a where lower(a.groupe)='"+groupe.toLowerCase()+"' and lower(a.cle)='"+cle.toLowerCase()+"'");
			TaPreferences instance = (TaPreferences) query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed findByGoupeAndCle - clé : "+cle, re );
			return null;
		}
	}
	
	public List<TaPreferences> findByCategorie(int idCategorie) {
		try {
			Query query = entityManager.createNamedQuery(TaPreferences.QN.FIND_BY_CATEGORIE);
			query.setParameter("idCategorie", idCategorie);
			List<TaPreferences> l = (List<TaPreferences>) query.getResultList();
			logger.debug("get successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	@Override
	public List<TaPreferences> selectAll() {
		logger.debug("selectAll TaPreferences");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPreferences> l = ejbQuery.getResultList();
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
	public List<TaPreferences> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPreferences> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPreferences> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPreferences> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPreferences value) throws Exception {
		BeanValidator<TaPreferences> validator = new BeanValidator<TaPreferences>(TaPreferences.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPreferences value, String propertyName) throws Exception {
		BeanValidator<TaPreferences> validator = new BeanValidator<TaPreferences>(TaPreferences.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPreferences transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaPreferences findByCode(String cle) {
		logger.debug("getting TaPreferences instance with cle: " + cle);
		try {
			if(!cle.equals("")){
				Query query = entityManager.createQuery("select a from TaPreferences a where a.cle='"+cle+"'");
				TaPreferences instance = (TaPreferences)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}


}
