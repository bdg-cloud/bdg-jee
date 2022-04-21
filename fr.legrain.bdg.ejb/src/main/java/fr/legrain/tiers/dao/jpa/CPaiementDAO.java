package fr.legrain.tiers.dao.jpa;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCPaiement.
 * @see fr.legrain.tiers.dao.old.test.TaCPaiement
 * @author Hibernate Tools
 */
public class CPaiementDAO implements ICPaiementDAO {

	static Logger logger = Logger.getLogger(CPaiementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaCPaiement a";
	
	public CPaiementDAO() {
	}
	
	public void persist(TaCPaiement transientInstance) {
		logger.debug("persisting TaCPaiement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaCPaiement refresh(TaCPaiement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaCPaiement.class, detachedInstance.getIdCPaiement());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaCPaiement persistentInstance) {
		logger.debug("removing TaCPaiement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCPaiement merge(TaCPaiement detachedInstance) {
		logger.debug("merging TaCPaiement instance");
		try {
			TaCPaiement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCPaiement findById(int id) {
		logger.debug("getting TaCPaiement instance with id: " + id);
		try {
			TaCPaiement instance = entityManager.find(TaCPaiement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaCPaiement findByCode(String code) {
		logger.debug("getting TaCPaiement instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaCPaiement f where f.codeCPaiement='"+code+"'");
			TaCPaiement instance = (TaCPaiement)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaCPaiement f where f.codeCPaiement='"+code+"'");
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
	public List<TaCPaiement> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaCPaiement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCPaiement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaCPaiementDTO> findAllCPaiementTiers() {
		Query query = entityManager.createNamedQuery(TaCPaiement.QN.FIND_ALL_CPAIMENT_TIERS_LIGHT);
//		query.setParameter(1, codeType);
		List<TaCPaiementDTO> l = query.getResultList();

		return l;
	}
	
	public List<TaCPaiementDTO> findAllCPaiementDoc() {
		Query query = entityManager.createNamedQuery(TaCPaiement.QN.FIND_ALL_CPAIEMENT_DOC_LIGHT);
//		query.setParameter(1, codeType);
		List<TaCPaiementDTO> l = query.getResultList();

		return l;
	}
	
	
	
	/**
	 * Recherche les conditions de paiement d'un certain type
	 * @param codeType - le code du type
	 * @return
	 */
	public List<TaCPaiement> rechercheParType(String codeType) {
		Query query = entityManager.createNamedQuery(TaCPaiement.QN.FIND_BY_TYPE);
		query.setParameter("codeTCPaiement", codeType);
		List<TaCPaiement> l = query.getResultList();

		return l;
	}
	
	@Override
	public List<TaCPaiement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCPaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCPaiement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCPaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCPaiement value) throws Exception {
		BeanValidator<TaCPaiement> validator = new BeanValidator<TaCPaiement>(TaCPaiement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCPaiement value, String propertyName) throws Exception {
		BeanValidator<TaCPaiement> validator = new BeanValidator<TaCPaiement>(TaCPaiement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCPaiement transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaCPaiement entity,String field) throws ExceptLgr {	
//		
//	}
}
