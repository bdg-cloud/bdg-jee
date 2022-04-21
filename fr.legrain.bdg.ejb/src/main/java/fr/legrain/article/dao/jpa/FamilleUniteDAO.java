package fr.legrain.article.dao.jpa;

// Generated 11 juin 2009 10:56:24 by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IFamilleUniteDAO;
import fr.legrain.article.model.TaFamilleUnite;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaFamilleUnite.
 * @see fr.legrain.articles.dao.TaFamilleUnite
 * @author Hibernate Tools
 */
public class FamilleUniteDAO implements IFamilleUniteDAO{

	private static final Log logger = LogFactory.getLog(FamilleUniteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaFamilleUnite p";
	
	public FamilleUniteDAO(){
	}


	public void persist(TaFamilleUnite transientInstance) {
		logger.debug("persisting TaFamilleUnite instance");
		try {
			 entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	
	public void remove(TaFamilleUnite persistentInstance) {
		logger.debug("removing TaFamilleUnite instance");
		try {
			 entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaFamilleUnite merge(TaFamilleUnite detachedInstance) {
		logger.debug("merging TaFamilleUnite instance");
		try {
			TaFamilleUnite result =  entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaFamilleUnite findById(int id) {
		logger.debug("getting TaFamilleUnite instance with id: " + id);
		try {
			TaFamilleUnite instance = entityManager.find(TaFamilleUnite.class,
					id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaFamilleUnite findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaFamilleUnite f where f.codeFamille='"+code+"'");
			TaFamilleUnite instance = (TaFamilleUnite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
//	@Override
	public List<TaFamilleUnite> selectAll() {
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFamilleUnite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	@Override
	public List<TaFamilleUnite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFamilleUnite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFamilleUnite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFamilleUnite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFamilleUnite value) throws Exception {
		BeanValidator<TaFamilleUnite> validator = new BeanValidator<TaFamilleUnite>(TaFamilleUnite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFamilleUnite value, String propertyName) throws Exception {
		BeanValidator<TaFamilleUnite> validator = new BeanValidator<TaFamilleUnite>(TaFamilleUnite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFamilleUnite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
