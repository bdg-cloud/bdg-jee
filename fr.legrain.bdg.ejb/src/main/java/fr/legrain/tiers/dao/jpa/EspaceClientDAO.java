package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IEspaceClientDAO;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaEspaceClient.
 * @see fr.legrain.tiers.model.old.TaEspaceClient
 * @author Hibernate Tools
 */
public class EspaceClientDAO implements IEspaceClientDAO {

	static Logger logger = Logger.getLogger(EspaceClientDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaEspaceClient a";
	
	public EspaceClientDAO(){
	}

	public TaEspaceClient login(String login, String password) {
		logger.debug("getting TaEspaceClient instance with login: " + login);
		try {
			if(!login.equals("")){
				Query query = entityManager.createQuery("select a from TaEspaceClient a where a.email='"+login+"' and a.password='"+password+"'");
				TaEspaceClient instance = (TaEspaceClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaEspaceClientDTO loginDTO(String login, String password) {
		logger.debug("getting TaEspaceClient instance with login: " + login);
		try {
			if(!login.equals("")){
				Query query = entityManager.createQuery(
						"select new fr.legrain.tiers.dto.TaEspaceClientDTO("
						+ " a.idEspaceClient, a.email, a.password, t.idTiers, t.codeTiers,"  
						+ " a.actif, a.versionObj, a.nom, a.prenom, a.dateDerniereConnexion) "
						+ " from TaEspaceClient a left join a.taTiers t where a.email='"+login+"' and a.password='"+password+"'");
				TaEspaceClientDTO instance = (TaEspaceClientDTO)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaEspaceClientDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaEspaceClient.QN.FIND_ALL_LIGHT);
			@SuppressWarnings("unchecked")
			List<TaEspaceClientDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void persist(TaEspaceClient transientInstance) {
		logger.debug("persisting TaEspaceClient instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaEspaceClient persistentInstance) {
		logger.debug("removing TaEspaceClient instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaEspaceClient merge(TaEspaceClient detachedInstance) {
		logger.debug("merging TaEspaceClient instance");
		try {
			TaEspaceClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaEspaceClient findById(int id) {
		logger.debug("getting TaEspaceClient instance with id: " + id);
		try {
			TaEspaceClient instance = entityManager.find(TaEspaceClient.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEspaceClient> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEspaceClient");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEspaceClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEspaceClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEspaceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEspaceClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEspaceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEspaceClient value) throws Exception {
		BeanValidator<TaEspaceClient> validator = new BeanValidator<TaEspaceClient>(TaEspaceClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEspaceClient value, String propertyName) throws Exception {
		BeanValidator<TaEspaceClient> validator = new BeanValidator<TaEspaceClient>(TaEspaceClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEspaceClient transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaEspaceClient findByCode(String code) {
		logger.debug("getting TaEspaceClient instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaEspaceClient a where a.email='"+code+"'");
				TaEspaceClient instance = (TaEspaceClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
	@Override
	public TaEspaceClient findByCodeTiers(String codeTiers) {
		logger.debug("getting TaEspaceClient instance with codeTiers: " + codeTiers);
		try {
			if(!codeTiers.equals("")){
				Query query = entityManager.createQuery("select a from TaEspaceClient a where a.taTiers.codeTiers='"+codeTiers+"'");
				TaEspaceClient instance = (TaEspaceClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaEspaceClient entity,String field) throws ExceptLgr {	
//		
//	}

}
