package fr.legrain.article.dao.jpa;

import java.util.LinkedList;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.IParamEan128DAO;
import fr.legrain.article.model.TaParamEan128;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaTAdr.
 * @see fr.legrain.tiers.model.old.TaTAdr
 * @author Hibernate Tools
 */
public class ParamEan128DAO implements IParamEan128DAO {

	static Logger logger = Logger.getLogger(ParamEan128DAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaParamEan128 a order by a.code128";
	
	public ParamEan128DAO() {
	}
	

	
	public void persist(TaParamEan128 transientInstance) {
		logger.debug("persisting TaParamEan128 instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaParamEan128 persistentInstance) {
		logger.debug("removing TaParamEan128 instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaParamEan128 merge(TaParamEan128 detachedInstance) {
		logger.debug("merging TaParamEan128 instance");
		try {
			TaParamEan128 result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaParamEan128 findById(int id) {
		logger.debug("getting TaParamEan128 instance with id: " + id);
		try {
			TaParamEan128 instance = entityManager.find(TaParamEan128.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	




	
	@Override
	public List<TaParamEan128> selectAll() {
		logger.debug("selectAll TaParamEan128");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaParamEan128> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParamEan128> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaParamEan128> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaParamEan128> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaParamEan128> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaParamEan128 value) throws Exception {
		BeanValidator<TaParamEan128> validator = new BeanValidator<TaParamEan128>(TaParamEan128.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaParamEan128 value, String propertyName) throws Exception {
		BeanValidator<TaParamEan128> validator = new BeanValidator<TaParamEan128>(TaParamEan128.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaParamEan128 transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaParamEan128 findByCode(String code) {
		logger.debug("getting TaParamEan128 instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaParamEan128 a where a.code128 = '"+code+"'");
			TaParamEan128 instance = (TaParamEan128)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	public TaParamEan128 findByCodeLike(String debutCodeAi) {
		logger.debug("getting TaParamEan128 instance with debutCodeAi: " + debutCodeAi);
		try {
			if(!debutCodeAi.equals("")){
			Query query = entityManager.createQuery("select a from TaParamEan128 a where a.code128 like '"+debutCodeAi+"%'");
			TaParamEan128 instance = (TaParamEan128)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	public List<String> recupListIdentifiant(){
		List<String> liste = new LinkedList<>();
		Query query = entityManager.createQuery("select a.code128 from TaParamEan128 a ");
		List<Object> l = query.getResultList();
		for (Object object : l) {
			liste.add((String) object);
		}
		return liste;
	
	}
}
