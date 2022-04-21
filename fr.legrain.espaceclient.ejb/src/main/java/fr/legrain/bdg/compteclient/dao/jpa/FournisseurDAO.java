package fr.legrain.bdg.compteclient.dao.jpa;

import javax.persistence.PersistenceContext;

import fr.legrain.bdg.compteclient.dao.IFournisseurDAO;
import fr.legrain.bdg.compteclient.model.TaFournisseur;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import fr.legrain.validator.BeanValidator;

/**
 *  Implémentation de l'interface IFournisseurDAO à la classe FournisseurDAO
 */
public class FournisseurDAO implements IFournisseurDAO {

	static Logger logger = Logger.getLogger(FournisseurDAO.class);

	@PersistenceContext(unitName = "compteclient")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaFournisseur a";

	/**
	 *  Constructeur par défaut
	 */
	public FournisseurDAO(){
		//this(null);
	}

	/**
	 *  Méthode d'enregistrement d'un fournisseur
	 */
	public void persist(TaFournisseur transientInstance) {
		logger.debug("persisting TaFournisseur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	/**
	 *  Méthode d'effacement d' un fournisseur passer en paramètre
	 */
	public void remove(TaFournisseur persistentInstance) {
		logger.debug("removing TaFournisseur instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	/**
	 *  Méthode de modification ou d'ajout d'un fournisseur passer en paramètre
	 */
	public TaFournisseur merge(TaFournisseur detachedInstance) {
		logger.debug("merging TaFournisseur instance");
		try {
			TaFournisseur result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	/**
	 *  Méthode de recherche d'un fournisseur par son id
	 */
	public TaFournisseur findById(int id) {
		logger.debug("getting TaFournisseur instance with id: " + id);
		try {
			TaFournisseur instance = entityManager.find(
					TaFournisseur.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 *  Méthode de recherche d'un fournisseur par son codeDossier
	 */
	public TaFournisseur findByCodeDossier(String codeDossier) {
		logger.debug("getting TaFournisseur instance with username: " + codeDossier);
		try {
			if(!codeDossier.equals("")&& codeDossier !=null){
				Query query = entityManager.createQuery("select a from TaFournisseur a where a.codeDossier='"+codeDossier+"'");
				TaFournisseur instance = (TaFournisseur)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}

	/**
	 *  @return - l'unique instance de taFournisseur si elle existe, sinon retourne null.
	 */
	public TaFournisseur findInstance() {
		int premierId = 1;
		logger.debug("getting TaFournisseur instance with id: "+premierId);
		try {
			TaFournisseur instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaFournisseur trouver, creation d'une nouvelle instance vide");
				instance = new TaFournisseur();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 *  Méthode de redéfinition de la sélection complète de la liste
	 */
	@Override
	public List<TaFournisseur> selectAll() {
		logger.debug("selectAll TaFournisseur");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFournisseur> l = ejbQuery.getResultList();
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
	public List<TaFournisseur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFournisseur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFournisseur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFournisseur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFournisseur value) throws Exception {
		BeanValidator<TaFournisseur> validator = new BeanValidator<TaFournisseur>(TaFournisseur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFournisseur value, String propertyName) throws Exception {
		BeanValidator<TaFournisseur> validator = new BeanValidator<TaFournisseur>(TaFournisseur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFournisseur transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaFournisseur findByCode(String code) {
		return null;
	}

}
