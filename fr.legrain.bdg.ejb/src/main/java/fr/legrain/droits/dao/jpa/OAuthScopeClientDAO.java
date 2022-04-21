package fr.legrain.droits.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.droits.dao.IOAuthScopeClientDAO;
import fr.legrain.droits.model.TaOAuthScopeClient;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class OAuthScopeClientDAO implements IOAuthScopeClientDAO{

	private static final Log logger = LogFactory.getLog(OAuthScopeClientDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaOAuthScopeClient p";
	
	public OAuthScopeClientDAO(){
//		this(null);
	}

//	public TaTaOAuthScopeClientDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaOAuthScopeClient.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaOAuthScopeClient());
//	}


	public void persist(TaOAuthScopeClient transientInstance) {
		logger.debug("persisting TaOAuthScopeClient instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaOAuthScopeClient persistentInstance) {
		logger.debug("removing TaOAuthScopeClient instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaOAuthScopeClient merge(TaOAuthScopeClient detachedInstance) {
		logger.debug("merging TaOAuthScopeClient instance");
		try {
			TaOAuthScopeClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaOAuthScopeClient findById(int id) {
		logger.debug("getting TaOAuthScopeClient instance with id: " + id);
		try {
			TaOAuthScopeClient instance = entityManager.find(TaOAuthScopeClient.class, id);
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
	public List<TaOAuthScopeClient> selectAll() {
		return selectAll(null);
	}
	
	public List<TaOAuthScopeClient> selectAll(TaOAuthServiceClient taOAuthServiceClient) {
		logger.debug("selectAll TaArticle");
		try {
			String jpql = defaultJPQLQuery;
			if(taOAuthServiceClient!=null) {
				jpql += " where p.taOAuthServiceClient.id = "+taOAuthServiceClient.getId();
			}
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaOAuthScopeClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaOAuthScopeClient entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaOAuthScopeClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaOAuthScopeClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaOAuthScopeClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaOAuthScopeClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaOAuthScopeClient value) throws Exception {
		BeanValidator<TaOAuthScopeClient> validator = new BeanValidator<TaOAuthScopeClient>(TaOAuthScopeClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaOAuthScopeClient value, String propertyName) throws Exception {
		BeanValidator<TaOAuthScopeClient> validator = new BeanValidator<TaOAuthScopeClient>(TaOAuthScopeClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaOAuthScopeClient transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaOAuthScopeClient findByCode(String code) {
		logger.debug("getting TaOAuthScopeClient instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaOAuthScopeClient f where f.code='"+code+"'");
				TaOAuthScopeClient instance = (TaOAuthScopeClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
}

