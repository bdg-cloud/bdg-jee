package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IResultatCorrectionDAO;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaResultatCorrection.
 * @see fr.legrain.articles.dao.TaResultatCorrection
 * @author Hibernate Tools
 */
public class ResultatCorrectionDAO implements IResultatCorrectionDAO {

	private static final Log logger = LogFactory.getLog(ResultatCorrectionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaResultatCorrectionDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaResultatCorrection f";
	
	public ResultatCorrectionDAO(){
//		this(null);
	}
	
//	public TaResultatCorrectionDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaResultatCorrection.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaResultatCorrection());
//	}
	
//	public TaResultatCorrection refresh(TaResultatCorrection detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaResultatCorrection.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaResultatCorrection transientInstance) {
		logger.debug("persisting TaResultatCorrection instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaResultatCorrection persistentInstance) {
		logger.debug("removing TaResultatCorrection instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdResultatCorrection()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaResultatCorrection merge(TaResultatCorrection detachedInstance) {
		logger.debug("merging TaResultatCorrection instance");
		try {
			TaResultatCorrection result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaResultatCorrection findByCode(String code) {
		logger.debug("getting TaResultatCorrection instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaResultatCorrection f where f.codeFamille='"+code+"'");
			TaResultatCorrection instance = (TaResultatCorrection)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaResultatCorrection findById(int id) {
		logger.debug("getting TaResultatCorrection instance with id: " + id);
		try {
			TaResultatCorrection instance = entityManager.find(TaResultatCorrection.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaResultatCorrection> selectAll() {
		logger.debug("selectAll TaResultatCorrection");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaResultatCorrection> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaResultatCorrection entity,String field) throws ExceptLgr {	
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
	public List<TaResultatCorrection> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaResultatCorrection> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaResultatCorrection> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaResultatCorrection> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaResultatCorrection value) throws Exception {
		BeanValidator<TaResultatCorrection> validator = new BeanValidator<TaResultatCorrection>(TaResultatCorrection.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaResultatCorrection value, String propertyName) throws Exception {
		BeanValidator<TaResultatCorrection> validator = new BeanValidator<TaResultatCorrection>(TaResultatCorrection.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaResultatCorrection transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
