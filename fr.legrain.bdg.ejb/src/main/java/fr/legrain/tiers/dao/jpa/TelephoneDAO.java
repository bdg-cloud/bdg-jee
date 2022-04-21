package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITelephoneDAO;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTelephone.
 * @see fr.legrain.tiers.model.old.TaTelephone
 * @author Hibernate Tools
 */
public class TelephoneDAO implements ITelephoneDAO{

	static Logger logger = Logger.getLogger(TelephoneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTelephone a";
	
	public TelephoneDAO() {
	}

	
//	public TaTelephone refresh(TaTelephone detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTelephone.class, detachedInstance.getIdTelephone());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void persist(TaTelephone transientInstance) {
		logger.debug("persisting TaTelephone instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTelephone persistentInstance) {
		logger.debug("removing TaTelephone instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTelephone merge(TaTelephone detachedInstance) {
		logger.debug("merging TaTelephone instance");
		try {
			TaTelephone result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTelephone findById(int id) {
		logger.debug("getting TaTelephone instance with id: " + id);
		try {
			TaTelephone instance = entityManager.find(TaTelephone.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les telephones qui ont été importés d'un autre programme
	 * @param origineImport
	 * @param idImport - id externe (dans le programme d'origine)
	 * @return
	 */
	public List<TaTelephone> rechercheParImport(String origineImport, String idImport) {
		Query query = entityManager.createNamedQuery(TaTelephone.QN.FIND_BY_IMPORT);
		query.setParameter("origineImport", origineImport);
		query.setParameter("idImport", idImport);
		List<TaTelephone> l = query.getResultList();

		return l;
	}

	@Override
	public List<TaTelephone> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTelephone");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTelephone> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTelephone> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTelephone> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTelephone> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTelephone> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTelephone value) throws Exception {
		BeanValidator<TaTelephone> validator = new BeanValidator<TaTelephone>(TaTelephone.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTelephone value, String propertyName) throws Exception {
		BeanValidator<TaTelephone> validator = new BeanValidator<TaTelephone>(TaTelephone.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTelephone transientInstance) {
		entityManager.detach(transientInstance);
	}


	@Override
	public TaTelephone findByCode(String code) {
		return null;
	}


	@Override
	public List<TaTelephoneDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaTelephone.QN.FIND_ALL_LIGHT);
			@SuppressWarnings("unchecked")
			List<TaTelephoneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaTelephone entity,String field) throws ExceptLgr {	
//		
//	}
}
