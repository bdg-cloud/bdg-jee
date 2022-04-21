package fr.legrain.dossierIntelligent.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossierIntelligent.dao.ITaTypeDonneeDAO;
import fr.legrain.dossierIntelligent.model.TaTypeDonnee;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.model.old.TaAdresse
 * @author Hibernate Tools
 */
public class TaTypeDonneeDAO implements ITaTypeDonneeDAO {

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaTypeDonneeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTypeDonnee a";
	
	public TaTypeDonneeDAO(){
	}
	
	
	public void persist(TaTypeDonnee transientInstance) {
		logger.debug("persisting TaTypeDonnee instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaTypeDonnee persistentInstance) {
		logger.debug("removing TaTypeDonnee instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaTypeDonnee merge(TaTypeDonnee detachedInstance) {
		logger.debug("merging TaTypeDonnee instance");
		try {
			TaTypeDonnee result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTypeDonnee findById(int id) {
		logger.debug("getting TaTypeDonnee instance with id: " + id);
		try {
			TaTypeDonnee instance = entityManager.find(TaTypeDonnee.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public TaTypeDonnee findByCode(String code) {
		logger.debug("getting TaTypeDonnee instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaTypeDonnee a where upper(a.typeDonnee)='"+code.toUpperCase()+"'");
			TaTypeDonnee instance = (TaTypeDonnee)query.getSingleResult();
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
	public List<TaTypeDonnee> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTypeDonnee");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypeDonnee> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}


	@Override
	public List<TaTypeDonnee> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeDonnee> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public List<TaTypeDonnee> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeDonnee> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public boolean validate(TaTypeDonnee value) throws Exception {
		BeanValidator<TaTypeDonnee> validator = new BeanValidator<TaTypeDonnee>(TaTypeDonnee.class);
		return validator.validate(value);
	}


	@Override
	public boolean validateField(TaTypeDonnee value, String propertyName) throws Exception {
		BeanValidator<TaTypeDonnee> validator = new BeanValidator<TaTypeDonnee>(TaTypeDonnee.class);
		return validator.validateField(value,propertyName);
	}


	@Override
	public void detach(TaTypeDonnee transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	


}
