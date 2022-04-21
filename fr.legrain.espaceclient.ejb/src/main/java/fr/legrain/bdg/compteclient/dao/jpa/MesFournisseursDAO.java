package fr.legrain.bdg.compteclient.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.compteclient.dao.IMesFournisseursDAO;
import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.validator.BeanValidator;

public class MesFournisseursDAO implements IMesFournisseursDAO{

	static Logger logger = Logger.getLogger(MesFournisseursDAO.class);

	@PersistenceContext(unitName = "compteclient")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaMesFournisseurs a";

	public MesFournisseursDAO(){
		//this(null);
	}

	public void persist(TaMesFournisseurs transientInstance) {
		logger.debug("persisting TaMesFournisseurs instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaMesFournisseurs persistentInstance) {
		logger.debug("removing TaMesFournisseurs instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaMesFournisseurs merge(TaMesFournisseurs detachedInstance) {
		logger.debug("merging TaMesFournisseurs instance");
		try {
			TaMesFournisseurs result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaMesFournisseurs findById(int id) {
		logger.debug("getting TaMesFournisseurs instance with id: " + id);
		try {
			TaMesFournisseurs instance = entityManager.find(
					TaMesFournisseurs.class, id);
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
	public TaMesFournisseurs findInstance() {
		int premierId = 1;
		logger.debug("getting TaMesFournisseurs instance with id: "+premierId);
		try {
			TaMesFournisseurs instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaMesFournisseurs trouve, creation d'une nouvelle instance vide");
				instance = new TaMesFournisseurs();
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaMesFournisseurs> selectAll() {
		logger.debug("selectAll TaMesFournisseurs");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaMesFournisseurs> l = ejbQuery.getResultList();
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
	public List<TaMesFournisseurs> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaMesFournisseurs> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaMesFournisseurs> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaMesFournisseurs> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaMesFournisseurs value) throws Exception {
		BeanValidator<TaMesFournisseurs> validator = new BeanValidator<TaMesFournisseurs>(TaMesFournisseurs.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaMesFournisseurs value, String propertyName) throws Exception {
		BeanValidator<TaMesFournisseurs> validator = new BeanValidator<TaMesFournisseurs>(TaMesFournisseurs.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaMesFournisseurs transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaMesFournisseurs findByCode(String code) {
		return null;
	}

	public List<TaMesFournisseurs> rechercheMesFournisseurs(int idUtilisateur){
		try {
			Query ejbQuery = entityManager.createQuery("select mf from TaMesFournisseurs mf where mf.taUtilisateur.id="+idUtilisateur);
			List<TaMesFournisseurs> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	public boolean verifieSiLiaisonClientFournisseurExisteDeja(int idFournisseur,String codeClient){
		try {
			Query ejbQuery = entityManager.createQuery("select mf from TaMesFournisseurs mf where mf.taFournisseur.id="+idFournisseur+ " and mf.codeClient='"+codeClient+"'");
			List<TaMesFournisseurs> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			if (l!=null && !l.isEmpty()){
				return true;
			}else {
				return false;
			}

		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			return false;
		}
	}
}
