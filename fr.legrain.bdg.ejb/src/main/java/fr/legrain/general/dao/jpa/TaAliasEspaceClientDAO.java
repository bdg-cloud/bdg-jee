package fr.legrain.general.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.general.dao.ITaAliasEspaceClientDAO;
import fr.legrain.general.dao.ITaAliasEspaceClientDAO;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.validator.BeanValidator;

/**

 */
public class TaAliasEspaceClientDAO implements ITaAliasEspaceClientDAO {

	static Logger logger = Logger.getLogger(TaAliasEspaceClientDAO.class);
	
	@PersistenceContext(unitName = "bdg_prog")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaAliasEspaceClient a ";
	
	public TaAliasEspaceClientDAO() {
	}
	
//	public TaAliasEspaceClient refresh(TaAliasEspaceClient detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaAliasEspaceClient.class, detachedInstance.getIdTaAliasEspaceClient());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaAliasEspaceClient transientInstance) {
		logger.debug("persisting TaAliasEspaceClient instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaAliasEspaceClient persistentInstance) {
		logger.debug("removing TaAliasEspaceClient instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaAliasEspaceClient merge(TaAliasEspaceClient detachedInstance) {
		logger.debug("merging TaAliasEspaceClient instance");
		try {
			TaAliasEspaceClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaAliasEspaceClient findById(int id) {
		logger.debug("getting TaAliasEspaceClient instance with id: " + id);
		try {
			TaAliasEspaceClient instance = entityManager.find(TaAliasEspaceClient.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaAliasEspaceClient findByCode(String code) {
		logger.debug("getting TaAliasEspaceClient instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaAliasEspaceClient f where f.alias='"+code+"'");
			TaAliasEspaceClient instance = (TaAliasEspaceClient)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaAliasEspaceClient> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaAliasEspaceClient");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAliasEspaceClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaAliasEspaceClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAliasEspaceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAliasEspaceClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAliasEspaceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAliasEspaceClient value) throws Exception {
		BeanValidator<TaAliasEspaceClient> validator = new BeanValidator<TaAliasEspaceClient>(TaAliasEspaceClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAliasEspaceClient value, String propertyName) throws Exception {
		BeanValidator<TaAliasEspaceClient> validator = new BeanValidator<TaAliasEspaceClient>(TaAliasEspaceClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAliasEspaceClient transientInstance) {
		entityManager.detach(transientInstance);
	}
//	public void ctrlSaisieSpecifique(TaAliasEspaceClient entity,String field) throws ExceptLgr {	
//		
//	}
}
