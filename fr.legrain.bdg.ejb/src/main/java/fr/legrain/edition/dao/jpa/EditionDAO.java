package fr.legrain.edition.dao.jpa;


import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.edition.dao.IEditionDAO;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.edition.model.TaTEditionCatalogue;
import fr.legrain.validator.BeanValidator;

public class EditionDAO implements IEditionDAO {

	static Logger logger = Logger.getLogger(EditionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaEdition a";
	
	public EditionDAO(){
	}

//	public TaEdition refresh(TaEdition detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaEdition.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaEdition transientInstance) {
		logger.debug("persisting TaEdition instance");
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
	public void remove(TaEdition persistentInstance) {
		logger.debug("removing TaEdition instance");
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
	public TaEdition merge(TaEdition detachedInstance) {
		logger.debug("merging TaEdition instance");
		try {
			TaEdition result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaEdition findById(int id) {
		logger.debug("getting TaEdition instance with id: " + id);
		try {
			TaEdition instance = entityManager.find(TaEdition.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEdition> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEdition");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEdition> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	
	public List<TaEdition> rechercheEditionDisponible(String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImpor) {
		logger.debug("selectAll TaEdition");
		try {
			Query ejbQuery = entityManager.createQuery("select e from TaEdition e join e.actionInterne act where act.idAction=:action");
			ejbQuery.setParameter("action", idAction);
			
			List<TaEdition> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEdition> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEdition> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEdition> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEdition> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEdition value) throws Exception {
		BeanValidator<TaEdition> validator = new BeanValidator<TaEdition>(TaEdition.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEdition value, String propertyName) throws Exception {
		BeanValidator<TaEdition> validator = new BeanValidator<TaEdition>(TaEdition.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEdition transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaEdition findByCode(String code) {
		logger.debug("getting TaTEditionCatalogue instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaEdition a where a.codeEdition='"+code+"'");
				TaEdition instance = (TaEdition)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
	public List<TaEditionDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			query = entityManager.createNamedQuery(TaEdition.QN.FIND_ALL_LIGHT);
			
			List<TaEditionDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaEditionDTO findByCodeLight(String code) {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			query = entityManager.createNamedQuery(TaEdition.QN.FIND_BY_CODE_LIGHT);
			query.setParameter("codeEdition", code);
			
			TaEditionDTO l = (TaEditionDTO) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<TaEditionDTO> findByIdTypeDTO(int idTEdition) {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			query = entityManager.createNamedQuery(TaEdition.QN.FIND_BY_ID_TYPE_DTO);
			query.setParameter("idTEdition", idTEdition);
			List<TaEditionDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaEditionDTO> findByCodeTypeDTO(String codeTEdition) {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			query = entityManager.createNamedQuery(TaEdition.QN.FIND_BY_CODE_TYPE_DTO);
			query.setParameter("codeTEdition", codeTEdition);
			List<TaEditionDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaEdition> findByIdType(int idTEdition) {
		logger.debug("getting TaTiers instance");
		try {
			Query query = null;

			query = entityManager.createQuery("select e from TaEdition e left join e.taTEdition t where t.idTEdition = :idTEdition order by e.codeEdition");
			query.setParameter("idTEdition", idTEdition);
			List<TaEdition> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaEdition> findByCodeType(String codeTEdition) {
		logger.debug("getting TaTiers instance");
		try {
			Query query = null;

			query = entityManager.createQuery("select e from TaEdition e left join e.taTEdition t where t.codeTEdition like :codeTEdition order by e.codeEdition");
			query.setParameter("codeTEdition", codeTEdition);
			List<TaEdition> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
