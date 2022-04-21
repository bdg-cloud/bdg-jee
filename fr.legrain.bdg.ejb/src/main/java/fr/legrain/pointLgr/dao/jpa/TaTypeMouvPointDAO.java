package fr.legrain.pointLgr.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.pointLgr.model.TaComptePoint;
import fr.legrain.pointLgr.model.TaTypeMouvPoint;
import fr.legrain.point_Lgr.dao.ITypeMouvPointDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaTypeMouvPointDAO implements ITypeMouvPointDAO {

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaTypeMouvPointDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTypeMouvPoint a";
	
	public TaTypeMouvPointDAO(){
	}
	

	@Override
	public void persist(TaTypeMouvPoint transientInstance) {
		logger.debug("persisting TaTypeMouvPoint instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	@Override
	public void remove(TaTypeMouvPoint persistentInstance) {
		logger.debug("removing TaTypeMouvPoint instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}
	
	@Override
	public TaTypeMouvPoint merge(TaTypeMouvPoint detachedInstance) {
		logger.debug("merging TaTypeMouvPoint instance");
		try {
			TaTypeMouvPoint result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTypeMouvPoint findById(int id) {
		logger.debug("getting TaTypeMouvPoint instance with id: " + id);
		try {
			TaTypeMouvPoint instance = entityManager.find(TaTypeMouvPoint.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public TaTypeMouvPoint findByCode(String code) {
		logger.debug("getting TaTypeMouvPoint instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTypeMouvPoint f where f.typeMouv='"+code+"'");
			TaTypeMouvPoint instance = (TaTypeMouvPoint)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaTypeMouvPoint> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTypeMouvPoint");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypeMouvPoint> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	public void ctrlSaisieSpecifique(TaComptePoint entity,String field) throws ExceptLgr {	
		
	}



	@Override
	public List<TaTypeMouvPoint> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeMouvPoint> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeMouvPoint> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeMouvPoint> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public boolean validate(TaTypeMouvPoint value) throws Exception {
		BeanValidator<TaTypeMouvPoint> validator = new BeanValidator<TaTypeMouvPoint>(TaTypeMouvPoint.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeMouvPoint value, String propertyName)
			throws Exception {
		BeanValidator<TaTypeMouvPoint> validator = new BeanValidator<TaTypeMouvPoint>(TaTypeMouvPoint.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeMouvPoint transientInstance) {
		// TODO Auto-generated method stub
		entityManager.detach(transientInstance);
	}

}
