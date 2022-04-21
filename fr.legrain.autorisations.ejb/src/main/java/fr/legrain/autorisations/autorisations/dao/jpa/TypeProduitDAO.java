package fr.legrain.autorisations.autorisations.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.autorisations.autorisations.dao.IAutorisationsDAO;
import fr.legrain.autorisations.autorisations.dao.ITypeProduitDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.model.old.TaAdresse
 * @author Hibernate Tools
 */
public class TypeProduitDAO implements ITypeProduitDAO {

	static Logger logger = Logger.getLogger(TypeProduitDAO.class);
	
	@PersistenceContext(unitName = "autorisations")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTypeProduit a";
	
	public TypeProduitDAO(){
	}



	@Override
	public void persist(TaTypeProduit transientInstance) {
		logger.debug("persisting TaTypeProduit instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	@Override
	public void remove(TaTypeProduit persistentInstance) {
		logger.debug("removing TaTypeProduit instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	@Override
	public TaTypeProduit merge(TaTypeProduit detachedInstance) {
		logger.debug("merging TaTypeProduit instance");
		try {
			TaTypeProduit result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}


	@Override
	public TaTypeProduit findById(int id) {
		logger.debug("getting TaTypeProduit instance with id: " + id);
		try {
			TaTypeProduit instance = entityManager.find(TaTypeProduit.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	



	@Override
	public List<TaTypeProduit> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTypeProduit");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypeProduit> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTypeProduit> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeProduit> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeProduit> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeProduit> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypeProduit value) throws Exception {
		BeanValidator<TaTypeProduit> validator = new BeanValidator<TaTypeProduit>(TaTypeProduit.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeProduit value, String propertyName) throws Exception {
		BeanValidator<TaTypeProduit> validator = new BeanValidator<TaTypeProduit>(TaTypeProduit.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeProduit transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTypeProduit findByCode(String code) {
		logger.debug("getting TaTypeProduit instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaTypeProduit a where a.code='"+code+"'");
			TaTypeProduit instance = (TaTypeProduit)query.getSingleResult();
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
			Query query = entityManager.createQuery("select count(f) from TaTypeProduit f where f.code='"+code+"'");
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

}
