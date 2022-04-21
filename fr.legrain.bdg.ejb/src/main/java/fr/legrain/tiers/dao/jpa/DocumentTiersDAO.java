package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.tiers.dao.IDocumentTiersDAO;
import fr.legrain.tiers.model.TaDocumentTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.model.old.TaTLiens
 * @author Hibernate Tools
 */
public class DocumentTiersDAO implements IDocumentTiersDAO {

	static Logger logger = Logger.getLogger(DocumentTiersDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaDocumentTiers a " ;
			//"order by actif,ordreTRelance";
	
	public DocumentTiersDAO() {
	}
	
//	public TaDocumentTiers refresh(TaDocumentTiers detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaDocumentTiers.class, detachedInstance.getIdDocumentTiers());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaDocumentTiers transientInstance) {
		logger.debug("persisting TaDocumentTiers instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaDocumentTiers persistentInstance) {
		logger.debug("removing TaDocumentTiers instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaDocumentTiers merge(TaDocumentTiers detachedInstance) {
		logger.debug("merging TaDocumentTiers instance");
		try {
			TaDocumentTiers result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaDocumentTiers findById(int id) {
		logger.debug("getting TaDocumentTiers instance with id: " + id);
		try {
			TaDocumentTiers instance = entityManager.find(TaDocumentTiers.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaDocumentTiers findByCode(String code) {
		logger.debug("getting TaDocumentTiers instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaDocumentTiers f where f.codeDocumentTiers='"+code+"'");
			TaDocumentTiers instance = (TaDocumentTiers)query.getSingleResult();
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
	public List<TaDocumentTiers> selectAll() {
		logger.debug("selectAll TaDocumentTiers");
		try {  //+" order by a.ordreTRelance"
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaDocumentTiers> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	

	
	public void RAZCheminVersion(String typeLogiciel) {
		try {
			List<TaDocumentTiers> liste=selectAll();
			for (TaDocumentTiers TaDocumentTiers : liste) {
				entityManager.getTransaction().begin();
				TaDocumentTiers.setActif(LibConversion.booleanToInt(TaDocumentTiers.getTypeLogiciel().equals(typeLogiciel)));
//				TaDocumentTiers.setCheminCorrespRelance(null);
//				TaDocumentTiers.setCheminModelRelance(null);
				TaDocumentTiers=merge(TaDocumentTiers);
				entityManager.getTransaction().commit();
				
				//passage ejb
				//TaDocumentTiers=refresh(TaDocumentTiers);
			}
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaDocumentTiers> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaDocumentTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaDocumentTiers> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaDocumentTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaDocumentTiers value) throws Exception {
		BeanValidator<TaDocumentTiers> validator = new BeanValidator<TaDocumentTiers>(TaDocumentTiers.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaDocumentTiers value, String propertyName) throws Exception {
		BeanValidator<TaDocumentTiers> validator = new BeanValidator<TaDocumentTiers>(TaDocumentTiers.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaDocumentTiers transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaDocumentTiers entity,String field) throws ExceptLgr {	
//		
//	}
}
