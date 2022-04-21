package fr.legrain.servicewebexterne.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.servicewebexterne.dao.ITaSynchroServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaSynchroServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;
import fr.legrain.validator.BeanValidator;

public class TaSynchroServiceExterneDAO implements ITaSynchroServiceWebExterneDAO {
	
static Logger logger = Logger.getLogger(TaSynchroServiceExterneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaSynchroServiceExterne a";
	
	public TaSynchroServiceExterneDAO(){
	}
	
	
	
	public List<TaSynchroServiceExterneDTO> findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
		logger.debug("findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO TaSynchroServiceExterne");
		try {
			Query query =  entityManager.createNamedQuery(TaSynchroServiceExterne.QN.FIND_ALL_BY_TYPE_ENTITE_AND_BY_ID_COMPTE_SERVICE_WEB_EXTERNE_DTO);
			query.setParameter("idCompteServiceWebExterne", idServiceWebExterne);
			query.setParameter("typeEntite", typeEntite);
			List<TaSynchroServiceExterneDTO> l =  query.getResultList();
			logger.debug("findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO failed", re);
			throw re;
		}
	}
	
	public Date findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne) {
		// TODO Auto-generated method stub
				logger.debug("findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO TaSynchroServiceExterne");
				try {
					Query query =  entityManager.createQuery("select MAX(f.derniereSynchro) from TaSynchroServiceExterne f where f.typeEntite = :typeEntite and f.taCompteServiceWebExterne.idCompteServiceWebExterne = :idCompteServiceWebExterne");
					query.setParameter("idCompteServiceWebExterne", idServiceWebExterne);
					query.setParameter("typeEntite", typeEntite);
					Date l =  (Date) query.getSingleResult();
					logger.debug("findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO failed", re);
					throw re;
				}
	}
	

	
	
	
	@Override
	public void persist(TaSynchroServiceExterne transientInstance) {
		logger.debug("persisting TaSynchroServiceExterne instance");
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
	public void remove(TaSynchroServiceExterne persistentInstance) {
		logger.debug("removing TaSynchroServiceExterne instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaSynchroServiceExterne merge(TaSynchroServiceExterne detachedInstance) {
		logger.debug("merging TaSynchroServiceExterne instance");
		try {
			TaSynchroServiceExterne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaSynchroServiceExterne findById(int id) {
		logger.debug("getting TaSynchroServiceExterne instance with id: " + id);
		try {
			TaSynchroServiceExterne instance = entityManager.find(TaSynchroServiceExterne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaSynchroServiceExterne> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaSynchroServiceExterne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaSynchroServiceExterne> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaSynchroServiceExterne> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaSynchroServiceExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaSynchroServiceExterne> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaSynchroServiceExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public boolean validate(TaSynchroServiceExterne value) throws Exception {
		BeanValidator<TaSynchroServiceExterne> validator = new BeanValidator<TaSynchroServiceExterne>(TaSynchroServiceExterne.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaSynchroServiceExterne value, String propertyName) throws Exception {
		BeanValidator<TaSynchroServiceExterne> validator = new BeanValidator<TaSynchroServiceExterne>(TaSynchroServiceExterne.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaSynchroServiceExterne transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaSynchroServiceExterne findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}



}
