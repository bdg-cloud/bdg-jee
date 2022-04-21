package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ILiensDAO;
import fr.legrain.tiers.model.TaLiens;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLiens.
 * @see fr.legrain.tiers.model.old.TaLiens
 * @author Hibernate Tools
 */
public class LiensDAO implements ILiensDAO {

	static Logger logger = Logger.getLogger(LiensDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaLiens a";
	
	public LiensDAO() {
	}

//	public TaLiens refresh(TaLiens detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLiens.class, detachedInstance.getIdLiens());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLiens transientInstance) {
		logger.debug("persisting TaLiens instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaLiens persistentInstance) {
		logger.debug("removing TaLiens instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaLiens merge(TaLiens detachedInstance) {
		logger.debug("merging TaLiens instance");
		try {
			TaLiens result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaLiens findById(int id) {
		logger.debug("getting TaLiens instance with id: " + id);
		try {
			TaLiens instance = entityManager.find(TaLiens.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaLiens> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLiens");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLiens> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne pour un tiers données, un liens correspondant au type de liens passé en paramètre.
	 * Il ne doit existé qu'un seul liens de ce type pour ce tiers (0 ou 1)
	 * @param codeTiers
	 * @param codeTypeLiens
	 * @return
	 */
	public TaLiens findLiensTiers(String codeTiers, String codeTypeLiens) {
		logger.debug("getting TaLiens instance with codeTiers: " + codeTiers+" and codeTypeLiens: "+codeTypeLiens);
		try {
			if(!codeTiers.equals("")){
				Query query = entityManager.createQuery("select a from TaLiens a where a.taTiers.codeTiers='"+codeTiers+"' and a.taTLiens.codeTLiens='"+codeTypeLiens+"'");
				TaLiens instance = (TaLiens)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (NoResultException re) {
			logger.debug("Pas de liens de type "+codeTypeLiens+" pour le tiers "+codeTiers);
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLiens> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLiens> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLiens> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLiens> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLiens value) throws Exception {
		BeanValidator<TaLiens> validator = new BeanValidator<TaLiens>(TaLiens.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLiens value, String propertyName) throws Exception {
		BeanValidator<TaLiens> validator = new BeanValidator<TaLiens>(TaLiens.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLiens transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaLiens findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaLiens entity,String field) throws ExceptLgr {	
//		
//	}
}
