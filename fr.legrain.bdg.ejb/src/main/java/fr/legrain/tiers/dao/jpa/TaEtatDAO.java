package fr.legrain.tiers.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaEtat;
import fr.legrain.tiers.dao.IEtatDAO;
import fr.legrain.validator.BeanValidator;

public class TaEtatDAO implements IEtatDAO {

	static Logger logger = Logger.getLogger(TaEtatDAO.class);

	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaEtat a";

	public TaEtatDAO(){
		//this(null);
	}

	public TaEtatDAO(EntityManager em){}

	@Override
	public void persist(TaEtat transientInstance) {
		// TODO Auto-generated method stub
		logger.debug("persisting TaEtat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaEtat persistentInstance) {
		// TODO Auto-generated method stub
		logger.debug("removing TaEtat instance");
		try {
			entityManager.remove(merge(persistentInstance));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaEtat merge(TaEtat detachedInstance) {
		// TODO Auto-generated method stub
		logger.debug("merging TaEtat instance");
		try {
			TaEtat result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
		//		return null;
	}

	@Override
	public TaEtat findById(int id) {
		// TODO Auto-generated method stub
		logger.debug("getting TaEtat instance with id: " + id);
		try {
			TaEtat instance = entityManager.find(TaEtat.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		//		return null;
	}

	@Override
	public TaEtat findByCode(String code) {
		// TODO Auto-generated method stub
		logger.debug("getting TaEtat instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaEtat a where a.codeTPaiement='"+code+"'");
				TaEtat instance = (TaEtat)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//			logger.error("get failed", re);
			//			throw re;
			return null;
		}
		//		return null;
	}

	@Override
	public List<TaEtat> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEtat");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEtat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
		//		return null;
	}

	@Override
	public List<TaEtat> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEtat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
		//		return null;
	}

	@Override
	public List<TaEtat> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEtat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
//		return null;
	}

	@Override
	public boolean validate(TaEtat value) throws Exception {
		// TODO Auto-generated method stub
		BeanValidator<TaEtat> validator = new BeanValidator<TaEtat>(TaEtat.class);
		return validator.validate(value);
//		return false;
	}

	@Override
	public boolean validateField(TaEtat value, String propertyName)
			throws Exception {
		// TODO Auto-generated method stub
		BeanValidator<TaEtat> validator = new BeanValidator<TaEtat>(TaEtat.class);
		return validator.validateField(value,propertyName);
//		return false;
	}

	@Override
	public void detach(TaEtat transientInstance) {
		// TODO Auto-generated method stub
		entityManager.detach(transientInstance);
	}

}
