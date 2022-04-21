package fr.legrain.login.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.login.dao.IRRoleUtilisateurLoginDAO;
import fr.legrain.login.model.TaRRoleUtilisateurLogin;
import fr.legrain.validator.BeanValidator;


public class RRoleUtilisateurLoginDAO implements IRRoleUtilisateurLoginDAO {

	private static final Log logger = LogFactory.getLog(RRoleUtilisateurLoginDAO.class);
	
	@PersistenceContext(unitName = "login_db")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaRRoleUtilisateurLogin p";
	
	public RRoleUtilisateurLoginDAO(){
//		this(null);
	}

//	public TaTaRRoleUtilisateurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaRRoleUtilisateurLogin.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRRoleUtilisateurLogin());
//	}


	public void persist(TaRRoleUtilisateurLogin transientInstance) {
		logger.debug("persisting TaRRoleUtilisateurLogin instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRRoleUtilisateurLogin persistentInstance) {
		logger.debug("removing TaRRoleUtilisateurLogin instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRRoleUtilisateurLogin merge(TaRRoleUtilisateurLogin detachedInstance) {
		logger.debug("merging TaRRoleUtilisateurLogin instance");
		try {
			TaRRoleUtilisateurLogin result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRRoleUtilisateurLogin findById(int id) {
		logger.debug("getting TaRRoleUtilisateurLogin instance with id: " + id);
		try {
			TaRRoleUtilisateurLogin instance = entityManager.find(TaRRoleUtilisateurLogin.class, id);
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
	public List<TaRRoleUtilisateurLogin> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRRoleUtilisateurLogin> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaRRoleUtilisateurLogin entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaRRoleUtilisateurLogin> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRRoleUtilisateurLogin> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRRoleUtilisateurLogin> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRRoleUtilisateurLogin> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRRoleUtilisateurLogin value) throws Exception {
		BeanValidator<TaRRoleUtilisateurLogin> validator = new BeanValidator<TaRRoleUtilisateurLogin>(TaRRoleUtilisateurLogin.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRRoleUtilisateurLogin value, String propertyName) throws Exception {
		BeanValidator<TaRRoleUtilisateurLogin> validator = new BeanValidator<TaRRoleUtilisateurLogin>(TaRRoleUtilisateurLogin.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRRoleUtilisateurLogin transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaRRoleUtilisateurLogin findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaRRoleUtilisateurLogin f where f.codeFamille='"+code+"'");
			TaRRoleUtilisateurLogin instance = (TaRRoleUtilisateurLogin)query.getSingleResult();
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

