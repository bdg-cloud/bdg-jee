package fr.legrain.article.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IMouvementPrevDAO;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.stock.model.TaMouvementStockPrev;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaMouvement.
 * @see fr.legrain.TaMouvement.dao.TaMouvement
 * @author Hibernate Tools
 */
public class MouvementStockPrevDAO implements IMouvementPrevDAO {

	private static final Log logger = LogFactory.getLog(MouvementStockPrevDAO.class);

	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	

	private String defaultJPQLQuery = "select u from TaMouvementStockPrev u";

	public MouvementStockPrevDAO(){
	}

	


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaMouvement)
	 */
	public void persist(TaMouvementStockPrev transientInstance) {
		logger.debug("persisting TaMouvement instance");
		try {

			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaMouvement)
	 */
	public void remove(TaMouvementStockPrev persistentInstance) {
		logger.debug("removing TaMouvementStockPrev instance");
		try {
			TaMouvementStockPrev e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaMouvement)
	 */
	public TaMouvementStockPrev merge(TaMouvementStockPrev detachedInstance) {
		logger.debug("merging TaMouvementStockPrev instance");
		try {
			TaMouvementStockPrev result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}



	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaMouvementStockPrev findById(int id) {
		logger.debug("getting TaMouvementStockPrev instance with id: " + id);
		try {
			TaMouvementStockPrev instance = entityManager.find(TaMouvementStockPrev.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaMouvementStockPrev> selectAll() {
		logger.debug("selectAll TaMouvementStockPrev");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaMouvementStockPrev> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public TaMouvementStockPrev refresh(TaMouvementStockPrev detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
			session.evict(detachedInstance);
			detachedInstance = entityManager.find(TaMouvementStockPrev.class, detachedInstance.getIdMouvementStock());
			return detachedInstance;

		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}



	public void ctrlSaisieSpecifique(TaMouvementStockPrev entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	@Override
	public List<TaMouvementStockPrev> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaMouvementStockPrev> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaMouvementStockPrev> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaMouvementStockPrev> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaMouvementStockPrev value) throws Exception {
		BeanValidator<TaMouvementStockPrev> validator = new BeanValidator<TaMouvementStockPrev>(TaMouvementStockPrev.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaMouvementStockPrev value, String propertyName) throws Exception {
		BeanValidator<TaMouvementStockPrev> validator = new BeanValidator<TaMouvementStockPrev>(TaMouvementStockPrev.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaMouvementStockPrev transientInstance) {
		entityManager.detach(transientInstance);
	}




	@Override
	public TaMouvementStockPrev findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}





	


	
}
