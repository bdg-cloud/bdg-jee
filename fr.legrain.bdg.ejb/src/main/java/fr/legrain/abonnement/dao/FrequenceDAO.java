package fr.legrain.abonnement.dao;
//test git
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.abonnement.dao.jpa.IFrequenceDAO;
import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;

public class FrequenceDAO implements IFrequenceDAO{
	
	private static final Log logger = LogFactory.getLog(FrequenceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	
	private String defaultJPQLQuery = "select f from TaFrequence f order by f.codeFrequence";
	
	public FrequenceDAO(){
//		this(null);
	}

	public void persist(TaFrequence transientInstance) {
		logger.debug("persisting TaFrequence instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaFrequence persistentInstance) {
		logger.debug("removing TaFrequence instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdFrequence()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaFrequence merge(TaFrequence detachedInstance) {
		logger.debug("merging TaFrequence instance");
		try {
			TaFrequence result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	

	@Override
	public TaFrequence findById(int id) {
		logger.debug("getting TaFrequence instance with id: " + id);
		try {
			TaFrequence instance = entityManager.find(TaFrequence.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public TaFrequence findByCode(String code) {
		logger.debug("getting TaFrequence instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaFrequence f where f.codeFrequence='"+code+"'");
			TaFrequence instance = (TaFrequence)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	@Override
	public List<TaFrequence> selectAll() {
		logger.debug("selectAll TaFrequence");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFrequence> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaFrequence> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaFrequence> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaFrequence value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaFrequence value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaFrequence transientInstance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TaFrequenceDTO> findByCodeLight(String code) {
		logger.debug("getting TaFrequence instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaFrequence.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeFrequence", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaFrequence.QN.FIND_ALL_LIGHT);
			}

			List<TaFrequenceDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaFrequenceDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaFrequence.QN.FIND_ALL_LIGHT);
			List<TaFrequenceDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
