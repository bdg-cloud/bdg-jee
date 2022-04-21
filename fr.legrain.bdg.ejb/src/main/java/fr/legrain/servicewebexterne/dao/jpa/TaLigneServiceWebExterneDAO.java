package fr.legrain.servicewebexterne.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.servicewebexterne.dao.ITaLigneServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.validator.BeanValidator;

public class TaLigneServiceWebExterneDAO implements ITaLigneServiceWebExterneDAO {
	
static Logger logger = Logger.getLogger(TaLigneServiceWebExterneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLigneServiceWebExterne a";
	
	public TaLigneServiceWebExterneDAO(){
	}
	
	@Override
	public void persist(TaLigneServiceWebExterne transientInstance) {
		logger.debug("persisting TaLigneServiceWebExterne instance");
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
	public void remove(TaLigneServiceWebExterne persistentInstance) {
		logger.debug("removing TaLigneServiceWebExterne instance");
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
	public TaLigneServiceWebExterne merge(TaLigneServiceWebExterne detachedInstance) {
		logger.debug("merging TaLigneServiceWebExterne instance");
		try {
			TaLigneServiceWebExterne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaLigneServiceWebExterne findById(int id) {
		logger.debug("getting TaLigneServiceWebExterne instance with id: " + id);
		try {
			TaLigneServiceWebExterne instance = entityManager.find(TaLigneServiceWebExterne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<String> findAllIDExterneByIdCompteService(Integer id){
		logger.debug("getting findAllRefExterneByIdCompteService instance with id: " + id);
		try {
			Query ejbQuery = entityManager.createQuery("select li.idCommandeExterne from TaLigneServiceWebExterne li where li.taCompteServiceWebExterne.idCompteServiceWebExterne = :id");
			ejbQuery.setParameter("id", id);
			List<String> l =  ejbQuery.getResultList();
			logger.debug("get successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermine() {
		try {
			Query query = entityManager.createNamedQuery(TaLigneServiceWebExterne.QN.FIND_ALL_NON_TERMINE);
			List<TaLigneServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermine() {
		try {
			Query query = entityManager.createNamedQuery(TaLigneServiceWebExterne.QN.FIND_ALL_TERMINE);
			List<TaLigneServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdCompteServiceWebExterne(Integer id){
		try {
			Query query = entityManager.createNamedQuery(TaLigneServiceWebExterne.QN.FIND_ALL_TERMINE_BY_ID_COMPTE_SERVICE_WEB_EXTERNE);
			query.setParameter("id", id);
			List<TaLigneServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdCompteServiceWebExterne(Integer id){
		try {
			Query query = entityManager.createNamedQuery(TaLigneServiceWebExterne.QN.FIND_ALL_NON_TERMINE_BY_ID_COMPTE_SERVICE_WEB_EXTERNE);
			query.setParameter("id", id);
			List<TaLigneServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdServiceWebExterne(Integer id){
		try {
			Query query = entityManager.createNamedQuery(TaLigneServiceWebExterne.QN.FIND_ALL_TERMINE_BY_ID_SERVICE_WEB_EXTERNE);
			query.setParameter("id", id);
			List<TaLigneServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdServiceWebExterne(Integer id){
		try {
			Query query = entityManager.createNamedQuery(TaLigneServiceWebExterne.QN.FIND_ALL_NON_TERMINE_BY_ID_SERVICE_WEB_EXTERNE);
			query.setParameter("id", id);
			List<TaLigneServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaLigneServiceWebExterne> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLigneServiceWebExterne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLigneServiceWebExterne> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLigneServiceWebExterne> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLigneServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLigneServiceWebExterne> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLigneServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}


	@Override
	public boolean validate(TaLigneServiceWebExterne value) throws Exception {
		BeanValidator<TaLigneServiceWebExterne> validator = new BeanValidator<TaLigneServiceWebExterne>(TaLigneServiceWebExterne.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLigneServiceWebExterne value, String propertyName) throws Exception {
		BeanValidator<TaLigneServiceWebExterne> validator = new BeanValidator<TaLigneServiceWebExterne>(TaLigneServiceWebExterne.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLigneServiceWebExterne transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaLigneServiceWebExterne findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}



}
