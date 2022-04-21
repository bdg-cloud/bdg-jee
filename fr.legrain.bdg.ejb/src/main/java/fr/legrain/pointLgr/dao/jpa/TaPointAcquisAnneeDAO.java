package fr.legrain.pointLgr.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.pointLgr.model.TaPointAcquisAnnee;
import fr.legrain.point_Lgr.dao.IPointAcquisAnneeDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaPointAcquisAnneeDAO implements IPointAcquisAnneeDAO{

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaPointAcquisAnneeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaPointAcquisAnnee a";
	
	public TaPointAcquisAnneeDAO(){
//		this(null);
	}
	
//	public TaPointAcquisAnneeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTiersPoint.class.getSimpleName());
//		initChampId(TaPointAcquisAnnee.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaPointAcquisAnnee());
//	}
//
//	public TaPointAcquisAnnee refresh(TaPointAcquisAnnee detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaPointAcquisAnnee.class, detachedInstance.getIdPoint());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaPointAcquisAnnee transientInstance) {
		logger.debug("persisting TaPointAcquisAnnee instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaPointAcquisAnnee persistentInstance) {
		logger.debug("removing TaPointAcquisAnnee instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaPointAcquisAnnee merge(TaPointAcquisAnnee detachedInstance) {
		logger.debug("merging TaPointAcquisAnnee instance");
		try {
			TaPointAcquisAnnee result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaPointAcquisAnnee findById(int id) {
		logger.debug("getting TaPointAcquisAnnee instance with id: " + id);
		try {
			TaPointAcquisAnnee instance = entityManager.find(TaPointAcquisAnnee.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaPointAcquisAnnee> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaPointAcquisAnnee");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPointAcquisAnnee> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaPointAcquisAnnee entity,String field) throws ExceptLgr {	
		
	}

	@Override
	public TaPointAcquisAnnee findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaPointAcquisAnnee> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPointAcquisAnnee> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPointAcquisAnnee> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPointAcquisAnnee> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPointAcquisAnnee value) throws Exception {
		BeanValidator<TaPointAcquisAnnee> validator = new BeanValidator<TaPointAcquisAnnee>(TaPointAcquisAnnee.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPointAcquisAnnee value, String propertyName)
			throws Exception {
		BeanValidator<TaPointAcquisAnnee> validator = new BeanValidator<TaPointAcquisAnnee>(TaPointAcquisAnnee.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPointAcquisAnnee transientInstance) {
		// TODO Auto-generated method stub
		entityManager.detach(transientInstance);
	}

}
