package fr.legrain.login.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.login.dao.IUtilisateurLoginDAO;
import fr.legrain.login.model.TaUtilisateurLogin;
import fr.legrain.validator.BeanValidator;


public class UtilisateurLoginDAO implements IUtilisateurLoginDAO {

	private static final Log logger = LogFactory.getLog(UtilisateurLoginDAO.class);
	
	@PersistenceContext(unitName = "login_db")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaUtilisateurLogin p";
	
	public UtilisateurLoginDAO(){
//		this(null);
	}

//	public TaTaUtilisateurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaUtilisateurLogin.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaUtilisateurLogin());
//	}


	public void persist(TaUtilisateurLogin transientInstance) {
		logger.debug("persisting TaUtilisateurLogin instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaUtilisateurLogin persistentInstance) {
		logger.debug("removing TaUtilisateurLogin instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaUtilisateurLogin merge(TaUtilisateurLogin detachedInstance) {
		logger.debug("merging TaUtilisateurLogin instance");
		try {
			TaUtilisateurLogin result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaUtilisateurLogin findById(int id) {
		logger.debug("getting TaUtilisateurLogin instance with id: " + id);
		try {
			TaUtilisateurLogin instance = entityManager.find(TaUtilisateurLogin.class, id);
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
	public List<TaUtilisateurLogin> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaUtilisateurLogin> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaUtilisateurLogin entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaUtilisateurLogin> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaUtilisateurLogin> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaUtilisateurLogin> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaUtilisateurLogin> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaUtilisateurLogin value) throws Exception {
		BeanValidator<TaUtilisateurLogin> validator = new BeanValidator<TaUtilisateurLogin>(TaUtilisateurLogin.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaUtilisateurLogin value, String propertyName) throws Exception {
		BeanValidator<TaUtilisateurLogin> validator = new BeanValidator<TaUtilisateurLogin>(TaUtilisateurLogin.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaUtilisateurLogin transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaUtilisateurLogin findByCode(String code) {
		logger.debug("getting TaUtilisateurLogin instance with username: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaUtilisateurLogin f where f.username='"+code+"'");
				TaUtilisateurLogin instance = (TaUtilisateurLogin)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
}

