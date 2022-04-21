package fr.legrain.tiers.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITTiersDAO;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.validator.BeanValidator;

public class TTiersDAO implements ITTiersDAO {
	
	static Logger logger = Logger.getLogger(TTiersDAO.class);

	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	/**
	 * Default constructor. 
	 */
	public TTiersDAO() {
		
	}

	private String defaultJPQLQuery = "select a from TaTTiers a where idTTiers > 0 ORDER BY a.codeTTiers";


	public void persist(TaTTiers transientInstance) {
		System.out.println("persisting TaTTiers instance");
		try {
			entityManager.persist(transientInstance);
			System.out.println("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTTiers persistentInstance) {
		System.out.println("removing TaTTiers instance");
		try {
			TaTTiers e = entityManager.merge(findById(persistentInstance.getIdTTiers()));
			entityManager.remove(e);
			System.out.println("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTTiers merge(TaTTiers detachedInstance) {
		System.out.println("merging TaTTiers instance");
		try {
			TaTTiers result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			System.out.println("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTTiers findById(int id) {
		System.out.println("getting TaTTiers instance with id: " + id);
		try {
			TaTTiers instance = entityManager.find(TaTTiers.class, id);
			System.out.println("get successful");
			return instance;
		} catch (RuntimeException re) {
			System.out.println("get failed");
			re.printStackTrace();
			throw re;
		}
	}
	//@Transactional(value=TxType.REQUIRES_NEW)
	public TaTTiers findByCode(String code) {
		System.out.println("getting TaTTiers instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTTiers f where f.codeTTiers='"+code+"'");
				TaTTiers instance = (TaTTiers)query.getSingleResult();
				System.out.println("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}


	public List<TaTTiers> selectAll() {
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTTiers> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTTiers> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTTiers> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTTiers value) throws Exception {
		BeanValidator<TaTTiers> validator = new BeanValidator<TaTTiers>(TaTTiers.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTTiers value, String propertyName) throws Exception {
		BeanValidator<TaTTiers> validator = new BeanValidator<TaTTiers>(TaTTiers.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTTiers transientInstance) {
		entityManager.detach(transientInstance);
	}

	//	@Override
	//	protected TaTTiers refresh(TaTTiers persistentInstance) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

}
