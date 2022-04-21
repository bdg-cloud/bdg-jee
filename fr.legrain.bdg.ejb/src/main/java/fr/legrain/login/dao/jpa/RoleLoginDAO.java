package fr.legrain.login.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.login.dao.IRoleLoginDAO;
import fr.legrain.login.model.TaRoleLogin;
import fr.legrain.validator.BeanValidator;


public class RoleLoginDAO implements IRoleLoginDAO {

	private static final Log logger = LogFactory.getLog(RoleLoginDAO.class);
	
	@PersistenceContext(unitName = "login_db")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaRoleLogin p";
	
	public RoleLoginDAO(){
//		this(null);
	}

//	public TaTaRoleLoginDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaRoleLogin.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRoleLogin());
//	}


	public void persist(TaRoleLogin transientInstance) {
		logger.debug("persisting TaRoleLogin instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRoleLogin persistentInstance) {
		logger.debug("removing TaRoleLogin instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRoleLogin merge(TaRoleLogin detachedInstance) {
		logger.debug("merging TaRoleLogin instance");
		try {
			TaRoleLogin result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRoleLogin findById(int id) {
		logger.debug("getting TaRoleLogin instance with id: " + id);
		try {
			TaRoleLogin instance = entityManager.find(TaRoleLogin.class, id);
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
	public List<TaRoleLogin> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRoleLogin> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaRoleLogin entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaRoleLogin> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRoleLogin> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRoleLogin> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRoleLogin> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRoleLogin value) throws Exception {
		BeanValidator<TaRoleLogin> validator = new BeanValidator<TaRoleLogin>(TaRoleLogin.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRoleLogin value, String propertyName) throws Exception {
		BeanValidator<TaRoleLogin> validator = new BeanValidator<TaRoleLogin>(TaRoleLogin.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRoleLogin transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaRoleLogin findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaRoleLogin f where f.codeFamille='"+code+"'");
			TaRoleLogin instance = (TaRoleLogin)query.getSingleResult();
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

