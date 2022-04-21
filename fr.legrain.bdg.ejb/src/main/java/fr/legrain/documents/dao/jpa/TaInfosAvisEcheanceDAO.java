package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosAvisEcheance;
import fr.legrain.documents.dao.IInfosAvisEcheanceDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaInfosAvisEcheance.
 * @see fr.legrain.documents.dao.TaInfosAvisEcheance
 * @author Hibernate Tools
 */
public class TaInfosAvisEcheanceDAO /*extends AbstractApplicationDAO<TaInfosAvisEcheance>*/ implements IInfosAvisEcheanceDAO {

//	private static final Log log = LogFactory.getLog(TaInfosDevisDAO.class);
	static Logger logger = Logger.getLogger(TaInfosAvisEcheanceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosAvisEcheance a";
	
	public TaInfosAvisEcheanceDAO(){

	}
	

	
	public void persist(TaInfosAvisEcheance transientInstance) {
		logger.debug("persisting TaInfosAvisEcheance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosAvisEcheance persistentInstance) {
		logger.debug("removing TaInfosAvisEcheance instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosAvisEcheance merge(TaInfosAvisEcheance detachedInstance) {
		logger.debug("merging TaInfosAvisEcheance instance");
		try {
			TaInfosAvisEcheance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosAvisEcheance findById(int id) {
		logger.debug("getting TaInfosAvisEcheance instance with id: " + id);
		try {
			TaInfosAvisEcheance instance = entityManager.find(TaInfosAvisEcheance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaInfosAvisEcheance findByCodeAvisEcheance(String code) {
		logger.debug("getting TaInfosAvisEcheance instance with code InfosAvisEcheance : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosAvisEcheance a where a.taDocument.codeDocument='"+code+"'");
				TaInfosAvisEcheance instance = (TaInfosAvisEcheance)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaInfosAvisEcheance> selectAll() {
		logger.debug("selectAll TaInfosAvisEcheance");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosAvisEcheance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaInfosAvisEcheance findByCode(String code) {
		// TODO Auto-generated method stub
		return findByCodeAvisEcheance(code);
	}

	@Override
	public List<TaInfosAvisEcheance> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfosAvisEcheance> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfosAvisEcheance> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfosAvisEcheance> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfosAvisEcheance value) throws Exception {
		BeanValidator<TaInfosAvisEcheance> validator = new BeanValidator<TaInfosAvisEcheance>(TaInfosAvisEcheance.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInfosAvisEcheance value, String propertyName) throws Exception {
		BeanValidator<TaInfosAvisEcheance> validator = new BeanValidator<TaInfosAvisEcheance>(TaInfosAvisEcheance.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInfosAvisEcheance transientInstance) {
		entityManager.detach(transientInstance);
		
	}
}
