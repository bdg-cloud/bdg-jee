package fr.legrain.tiers.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.ITCiviliteDAO;
import fr.legrain.tiers.model.TaTCivilite;
import fr.legrain.validator.BeanValidator;

public class TCiviliteDAO /*extends AbstractApplicationDAOServer<TaTCivilite>*/ implements ITCiviliteDAO {
	
	static Logger logger = Logger.getLogger(TCiviliteDAO.class);

	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	/**
	 * Default constructor. 
	 */
	public TCiviliteDAO() {
		// TODO Auto-generated constructor stub
	}

	private String defaultJPQLQuery = "select a from TaTCivilite a order by a.codeTCivilite";


	public void persist(TaTCivilite transientInstance) {
		System.out.println("persisting TaTCivilite instance");
		try {
			entityManager.persist(transientInstance);
			System.out.println("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTCivilite persistentInstance) {
		System.out.println("removing TaTCivilite instance");
		try {
			TaTCivilite e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			System.out.println("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTCivilite merge(TaTCivilite detachedInstance) {
		System.out.println("merging TaTCivilite instance");
		try {
			TaTCivilite result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			System.out.println("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTCivilite findById(int id) {
		System.out.println("getting TaTCivilite instance with id: " + id);
		try {
			TaTCivilite instance = entityManager.find(TaTCivilite.class, id);
			System.out.println("get successful");
			return instance;
		} catch (RuntimeException re) {
			return null;
		}
	}

	public TaTCivilite findByCode(String code) {
		System.out.println("getting TaTCivilite instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTCivilite f where f.codeTCivilite='"+code+"'");
				TaTCivilite instance = (TaTCivilite)query.getSingleResult();
				System.out.println("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}


	public List<TaTCivilite> selectAll() {
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTCivilite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTCivilite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTCivilite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTCivilite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTCivilite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTCivilite value) throws Exception {
		BeanValidator<TaTCivilite> validator = new BeanValidator<TaTCivilite>(TaTCivilite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTCivilite value, String propertyName) throws Exception {
		BeanValidator<TaTCivilite> validator = new BeanValidator<TaTCivilite>(TaTCivilite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTCivilite transientInstance) {
		entityManager.detach(transientInstance);
	}

	//	@Override
	//	protected TaTCivilite refresh(TaTCivilite persistentInstance) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

}
