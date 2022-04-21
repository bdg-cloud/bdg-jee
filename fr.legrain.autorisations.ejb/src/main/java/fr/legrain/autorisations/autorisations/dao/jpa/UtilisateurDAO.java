package fr.legrain.autorisations.autorisations.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.autorisations.autorisation.model.TaUtilisateur;
import fr.legrain.autorisations.autorisations.dao.IUtilisateurDAO;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class UtilisateurDAO implements IUtilisateurDAO {

	private static final Log logger = LogFactory.getLog(UtilisateurDAO.class);
	
	@PersistenceContext(unitName = "autorisations")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaUtilisateur p";
	
	public UtilisateurDAO(){
//		this(null);
	}

//	public TaTaUtilisateurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaUtilisateur.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaUtilisateur());
//	}


	public void persist(TaUtilisateur transientInstance) {
		logger.debug("persisting TaUtilisateur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaUtilisateur persistentInstance) {
		logger.debug("removing TaUtilisateur instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaUtilisateur merge(TaUtilisateur detachedInstance) {
		logger.debug("merging TaUtilisateur instance");
		try {
			TaUtilisateur result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaUtilisateur findById(int id) {
		logger.debug("getting TaUtilisateur instance with id: " + id);
		try {
			TaUtilisateur instance = entityManager.find(TaUtilisateur.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaUtilisateur> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaUtilisateur> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaUtilisateur entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaUtilisateur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaUtilisateur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaUtilisateur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaUtilisateur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaUtilisateur value) throws Exception {
		BeanValidator<TaUtilisateur> validator = new BeanValidator<TaUtilisateur>(TaUtilisateur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaUtilisateur value, String propertyName) throws Exception {
		BeanValidator<TaUtilisateur> validator = new BeanValidator<TaUtilisateur>(TaUtilisateur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaUtilisateur transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaUtilisateur findByCode(String code) {
		logger.debug("getting TaUtilisateur instance with username: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaUtilisateur f where f.username='"+code+"'");
			TaUtilisateur instance = (TaUtilisateur)query.getSingleResult();
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

