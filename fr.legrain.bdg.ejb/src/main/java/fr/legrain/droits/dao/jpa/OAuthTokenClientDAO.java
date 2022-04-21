package fr.legrain.droits.dao.jpa;


import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.droits.dao.IOAuthTokenClientDAO;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.droits.model.TaOAuthTokenClient;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class OAuthTokenClientDAO implements IOAuthTokenClientDAO, Serializable{

	private static final long serialVersionUID = -7377294559911543624L;

	private static final Log logger = LogFactory.getLog(OAuthTokenClientDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaOAuthTokenClient p";
	
	public OAuthTokenClientDAO(){
//		this(null);
	}

//	public TaTaOAuthTokenClientDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaOAuthTokenClient.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaOAuthTokenClient());
//	}


	public void persist(TaOAuthTokenClient transientInstance) {
		logger.debug("persisting TaOAuthTokenClient instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaOAuthTokenClient persistentInstance) {
		logger.debug("removing TaOAuthTokenClient instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaOAuthTokenClient merge(TaOAuthTokenClient detachedInstance) {
		logger.debug("merging TaOAuthTokenClient instance");
		try {
			TaOAuthTokenClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaOAuthTokenClient findById(int id) {
		logger.debug("getting TaOAuthTokenClient instance with id: " + id);
		try {
			TaOAuthTokenClient instance = entityManager.find(TaOAuthTokenClient.class, id);
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
	public List<TaOAuthTokenClient> selectAll() {
		return selectAll(null);
	}
	
	public List<TaOAuthTokenClient> selectAll(TaOAuthServiceClient taOAuthServiceClient) {
		logger.debug("selectAll TaArticle");
		try {
			String jpql = defaultJPQLQuery;
			if(taOAuthServiceClient!=null) {
				jpql += " where p.taOAuthServiceClient.id = "+taOAuthServiceClient.getId();
			}
			Query ejbQuery = entityManager.createQuery(jpql);
			List<TaOAuthTokenClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaOAuthTokenClient entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaOAuthTokenClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaOAuthTokenClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaOAuthTokenClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaOAuthTokenClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaOAuthTokenClient value) throws Exception {
		BeanValidator<TaOAuthTokenClient> validator = new BeanValidator<TaOAuthTokenClient>(TaOAuthTokenClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaOAuthTokenClient value, String propertyName) throws Exception {
		BeanValidator<TaOAuthTokenClient> validator = new BeanValidator<TaOAuthTokenClient>(TaOAuthTokenClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaOAuthTokenClient transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaOAuthTokenClient findByCode(String code) {
		logger.debug("getting TaOAuthTokenClient instance with username: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaOAuthTokenClient f where f.username='"+code+"'");
				TaOAuthTokenClient instance = (TaOAuthTokenClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	public TaOAuthTokenClient findByKey(String key) {
		return findByKey(key, null);
	}

	@Override
	public TaOAuthTokenClient findByKey(String key, TaOAuthServiceClient taOAuthServiceClient) {
		logger.debug("getting TaOAuthTokenClient instance with key: " + key);
		try {
			if(!key.equals("")){
				String jpql = "select f from TaOAuthTokenClient f where f.key='"+key+"'";
				if(taOAuthServiceClient!=null) {
					jpql += " and f.taOAuthServiceClient.id = "+taOAuthServiceClient.getId();
				}
				Query query = entityManager.createQuery(jpql);
				TaOAuthTokenClient instance = (TaOAuthTokenClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	public TaOAuthTokenClient findByAccessToken(String accessToken) {
		return findByAccessToken(accessToken, null);
	}

	@Override
	public TaOAuthTokenClient findByAccessToken(String accessToken, TaOAuthServiceClient taOAuthServiceClient) {
		logger.debug("getting TaOAuthTokenClient instance with accessToken: " + accessToken);
		try {
			if(!accessToken.equals("")){
				String jpql = "select f from TaOAuthTokenClient f where f.accessToken='"+accessToken+"'";
				if(taOAuthServiceClient!=null) {
					jpql += " and f.taOAuthServiceClient.id = "+taOAuthServiceClient.getId();
				}
				Query query = entityManager.createQuery(jpql);
				TaOAuthTokenClient instance = (TaOAuthTokenClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	public void removeKey(String key) {
		removeKey(key,null);
	}
	
	public void removeKey(String key, TaOAuthServiceClient taOAuthServiceClient) {
		try {
			if(!key.equals("")){
				String jpql = "delete from TaOAuthTokenClient f where f.key='"+key+"'";
				if(taOAuthServiceClient!=null) {
					jpql += " and f.taOAuthServiceClient.id = "+taOAuthServiceClient.getId();
				}
				Query query = entityManager.createQuery(jpql);
				int deletedCount = query.executeUpdate();
				logger.debug("get successful");
			}
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
		}
	}
	
	public void removeAll() {
		removeAll(null);
	}
	
	@Override
	public void removeAll(TaOAuthServiceClient taOAuthServiceClient) {
		List<TaOAuthTokenClient> l = selectAll(taOAuthServiceClient);
		for (TaOAuthTokenClient taOAuthTokenClient : l) {
			remove(taOAuthTokenClient);
		}
	}
	
	public Set<String> findAllKeys() {
		return findAllKeys(null);
	}

	@Override
	public Set<String> findAllKeys(TaOAuthServiceClient taOAuthServiceClient) {
		List<TaOAuthTokenClient> l = selectAll(taOAuthServiceClient);
		Set<String> s = new HashSet<>();
		for (TaOAuthTokenClient taOAuthTokenClient : l) {
			s.add(taOAuthTokenClient.getKey());
		}
		return s;
	}

	
}

