package fr.legrain.bdg.compteclient.dao.jpa.droits;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.validator.internal.util.logging.Log;

import fr.legrain.validators.BeanValidator;
import fr.legrain.bdg.compteclient.dao.droits.IRoleDAO;
import fr.legrain.bdg.compteclient.model.droits.TaRole;


public class RoleDAO implements IRoleDAO {

	private static final Logger logger = Logger.getLogger(RoleDAO.class);
	
	@PersistenceContext(unitName = "compteclient")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaRole p";
	
	public RoleDAO(){
//		this(null);
	}

//	public TaTaRoleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaRole.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRole());
//	}


	public void persist(TaRole transientInstance) {
		logger.debug("persisting TaRole instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRole persistentInstance) {
		logger.debug("removing TaRole instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRole merge(TaRole detachedInstance) {
		logger.debug("merging TaRole instance");
		try {
			TaRole result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRole findById(int id) {
		logger.debug("getting TaRole instance with id: " + id);
		try {
			TaRole instance = entityManager.find(TaRole.class, id);
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
	public List<TaRole> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRole> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaRole entity,String field) throws Exception {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaRole> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRole> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRole> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRole> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRole value) throws Exception {
		BeanValidator<TaRole> validator = new BeanValidator<TaRole>(TaRole.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRole value, String propertyName) throws Exception {
		BeanValidator<TaRole> validator = new BeanValidator<TaRole>(TaRole.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRole transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaRole findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaRole f where f.role='"+code+"'");
			TaRole instance = (TaRole)query.getSingleResult();
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

