package fr.legrain.tiers.dao.jpa;

// Generated 11 juin 2009 10:56:24 by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IFamilleTiersDAO;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaFamilleUnite.
 * @see fr.legrain.articles.dao.TaFamilleUnite
 * @author Hibernate Tools
 */
public class FamilleTiersDAO implements IFamilleTiersDAO {

	static Logger logger = Logger.getLogger(FamilleTiersDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaFamilleTiers p order by p.codeFamille";
	
	public FamilleTiersDAO(){
	}

	public void persist(TaFamilleTiers transientInstance) {
		logger.debug("persisting TaFamilleTiers instance");
		try {
			 entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public TaFamilleTiers refresh(TaFamilleTiers detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaFamilleTiers.class, detachedInstance.getIdFamille());
		    return detachedInstance;
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	public void remove(TaFamilleTiers persistentInstance) {
		logger.debug("removing TaFamilleTiers instance");
		try {
			 entityManager.remove(findById(persistentInstance.getIdFamille()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaFamilleTiers merge(TaFamilleTiers detachedInstance) {
		logger.debug("merging TaFamilleTiers instance");
		try {
			TaFamilleTiers result =  entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaFamilleTiers findById(int id) {
		logger.debug("getting TaFamilleTiers instance with id: " + id);
		try {
			TaFamilleTiers instance = entityManager.find(TaFamilleTiers.class,
					id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaFamilleTiers findByCode(String code) {
		logger.debug("getting TaFamilleTiers instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaFamilleTiers a where a.codeFamille='"+code+"'");
			TaFamilleTiers instance = (TaFamilleTiers)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	
	public TaFamilleTiers rechercheFamilleCogere() {
		logger.debug(" rechercheFamilleCogere" );
		try {
			Query query = entityManager.createQuery("select a from TaFamilleTiers a where a.codeFamille = 'COGERE'");
			TaFamilleTiers instance = (TaFamilleTiers)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaFamilleTiers f where f.codeFamille='"+code+"'");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaFamilleTiers> selectAll() {
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFamilleTiers> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaFamilleTiers> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFamilleTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFamilleTiers> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFamilleTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFamilleTiers value) throws Exception {
		BeanValidator<TaFamilleTiers> validator = new BeanValidator<TaFamilleTiers>(TaFamilleTiers.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFamilleTiers value, String propertyName) throws Exception {
		BeanValidator<TaFamilleTiers> validator = new BeanValidator<TaFamilleTiers>(TaFamilleTiers.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFamilleTiers transientInstance) {
		entityManager.detach(transientInstance);
	}
}
