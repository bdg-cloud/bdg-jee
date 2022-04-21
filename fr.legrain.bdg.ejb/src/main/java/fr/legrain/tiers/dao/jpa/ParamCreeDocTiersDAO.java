package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IParamCreeDocTiersDAO;
import fr.legrain.tiers.model.TaParamCreeDocTiers;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaTAdr.
 * @see fr.legrain.tiers.model.old.TaTAdr
 * @author Hibernate Tools
 */
public class ParamCreeDocTiersDAO implements IParamCreeDocTiersDAO {

	static Logger logger = Logger.getLogger(ParamCreeDocTiersDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaParamCreeDocTiers a";
	
	public ParamCreeDocTiersDAO() {
	}
	
//	public TaParamCreeDocTiers refresh(TaParamCreeDocTiers detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaParamCreeDocTiers.class, detachedInstance.getIdParamCreeDocTiers());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaParamCreeDocTiers transientInstance) {
		logger.debug("persisting TaParamCreeDocTiers instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaParamCreeDocTiers persistentInstance) {
		logger.debug("removing TaParamCreeDocTiers instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaParamCreeDocTiers merge(TaParamCreeDocTiers detachedInstance) {
		logger.debug("merging TaParamCreeDocTiers instance");
		try {
			TaParamCreeDocTiers result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaParamCreeDocTiers findById(int id) {
		logger.debug("getting TaParamCreeDocTiers instance with id: " + id);
		try {
			TaParamCreeDocTiers instance = entityManager.find(TaParamCreeDocTiers.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	//passage ejb, findByCode => findByCodeListe pour ne pas créer de conflit avec la methode de l'interface
	public List<TaParamCreeDocTiers> findByCodeListe(String codeTiers) {
		logger.debug("getting TaParamCreeDocTiers instance with codeTiers: " + codeTiers);
		try {
			if(!codeTiers.equals("")){
			Query query = entityManager.createQuery("select f from TaParamCreeDocTiers f" +
					" join f.taTiers t  where t.codeTiers ='"+codeTiers+"'");
			List<TaParamCreeDocTiers> l =query.getResultList();
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaParamCreeDocTiers> findByCodeTypeDoc(String codeTiers,String typeDoc) {
		logger.debug("getting TaParamCreeDocTiers instance with codeTiers: " + codeTiers);
		try {
			if(!codeTiers.equals("") && !typeDoc.equals("")){
			Query query = entityManager.createQuery("select f from TaParamCreeDocTiers f" +
					" join f.taTiers t  join f.taTDoc td where td.codeTDoc = '"+typeDoc+"' and t.codeTiers ='"+codeTiers+"'");
			List<TaParamCreeDocTiers> l = query.getResultList();
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaParamCreeDocTiers> findByTypeDoc(String typeDoc) {
		logger.debug("getting TaParamCreeDocTiers instance with typeDoc: " + typeDoc);
		try {
			if(!typeDoc.equals("")){
			Query query = entityManager.createQuery("select f from TaParamCreeDocTiers f" +
					"  where f.typeDoc = "+typeDoc+"'");
			List<TaParamCreeDocTiers> l = query.getResultList();
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParamCreeDocTiers> selectAll() {
		logger.debug("selectAll TaParamCreeDocTiers");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaParamCreeDocTiers> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParamCreeDocTiers> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaParamCreeDocTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaParamCreeDocTiers> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaParamCreeDocTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaParamCreeDocTiers value) throws Exception {
		BeanValidator<TaParamCreeDocTiers> validator = new BeanValidator<TaParamCreeDocTiers>(TaParamCreeDocTiers.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaParamCreeDocTiers value, String propertyName) throws Exception {
		BeanValidator<TaParamCreeDocTiers> validator = new BeanValidator<TaParamCreeDocTiers>(TaParamCreeDocTiers.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaParamCreeDocTiers transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaParamCreeDocTiers findByCode(String code) {
		//==> cf findByCodeListe()
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaTAdr entity,String field) throws ExceptLgr {	
//		
//	}
}
