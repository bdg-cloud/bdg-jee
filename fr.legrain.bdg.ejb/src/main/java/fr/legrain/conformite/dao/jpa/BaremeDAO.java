package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IBaremeDAO;
import fr.legrain.conformite.model.TaBareme;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaBareme.
 * @see fr.legrain.articles.dao.TaBareme
 * @author Hibernate Tools
 */
public class BaremeDAO implements IBaremeDAO {

	private static final Log logger = LogFactory.getLog(BaremeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaBaremeDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaBareme f";
	
	public BaremeDAO(){
//		this(null);
	}
	
//	public TaBaremeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaBareme.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaBareme());
//	}
	
//	public TaBareme refresh(TaBareme detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaBareme.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaBareme transientInstance) {
		logger.debug("persisting TaBareme instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaBareme persistentInstance) {
		logger.debug("removing TaBareme instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdBareme()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaBareme merge(TaBareme detachedInstance) {
		logger.debug("merging TaBareme instance");
		try {
			TaBareme result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaBareme findByCode(String code) {
		logger.debug("getting TaBareme instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaBareme f where f.codeFamille='"+code+"'");
			TaBareme instance = (TaBareme)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaBareme findById(int id) {
		logger.debug("getting TaBareme instance with id: " + id);
		try {
			TaBareme instance = entityManager.find(TaBareme.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaBareme> selectAll() {
		logger.debug("selectAll TaBareme");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaBareme> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaBareme entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaBareme> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaBareme> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaBareme> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaBareme> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaBareme value) throws Exception {
		BeanValidator<TaBareme> validator = new BeanValidator<TaBareme>(TaBareme.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaBareme value, String propertyName) throws Exception {
		BeanValidator<TaBareme> validator = new BeanValidator<TaBareme>(TaBareme.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaBareme transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public void removeOID(TaBareme b) {
		String selectOID = "select blob_fichier from ta_bareme where id_bareme = "+b.getIdBareme();
		Query selectOIDQuery= entityManager.createNativeQuery(selectOID); 
		
		//List<Object[]> oid = selectOIDQuery.getResultList();
		List<Object> oid = selectOIDQuery.getResultList();
		
		if(oid!=null && !oid.isEmpty()) {
			//BigInteger i = (BigInteger) oid.get(0)[0];
			BigInteger i = (BigInteger) oid.get(0);
			String unlinkOID = "select lo_unlink("+i.intValue()+")";   // deletes large object with OID 173454
			
			Query unlinkOIDQuery = entityManager.createNativeQuery(unlinkOID); 
			unlinkOIDQuery.getResultList();
		}
	}
	
}
