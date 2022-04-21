package fr.legrain.servicewebexterne.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.dao.IServiceWebExterneDAO;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.validator.BeanValidator;

public class ServiceWebExterneDAO implements IServiceWebExterneDAO {

	static Logger logger = Logger.getLogger(ServiceWebExterneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaServiceWebExterne a";
	
	public ServiceWebExterneDAO(){
	}

//	public TaServiceWebExterne refresh(TaServiceWebExterne detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaServiceWebExterne.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaServiceWebExterne transientInstance) {
		logger.debug("persisting TaServiceWebExterne instance");
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
	public void remove(TaServiceWebExterne persistentInstance) {
		logger.debug("removing TaServiceWebExterne instance");
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
	public TaServiceWebExterne merge(TaServiceWebExterne detachedInstance) {
		logger.debug("merging TaServiceWebExterne instance");
		try {
			TaServiceWebExterne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaServiceWebExterne findById(int id) {
		logger.debug("getting TaServiceWebExterne instance with id: " + id);
		try {
			TaServiceWebExterne instance = entityManager.find(TaServiceWebExterne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaServiceWebExterne> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaServiceWebExterne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaServiceWebExterne> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaServiceWebExterne> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaServiceWebExterne> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaServiceWebExterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaServiceWebExterne value) throws Exception {
		BeanValidator<TaServiceWebExterne> validator = new BeanValidator<TaServiceWebExterne>(TaServiceWebExterne.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaServiceWebExterne value, String propertyName) throws Exception {
		BeanValidator<TaServiceWebExterne> validator = new BeanValidator<TaServiceWebExterne>(TaServiceWebExterne.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaServiceWebExterne transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaServiceWebExterne findByCode(String code) {
		logger.debug("getting TaServiceWebExterne instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaServiceWebExterne f where UPPER(f.codeServiceWebExterne)='"+ code.toUpperCase()+"'");
			TaServiceWebExterne instance = (TaServiceWebExterne)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	public List<TaServiceWebExterne> findAgendaUtilisateur(TaUtilisateur utilisateur) {
		logger.debug("getting TaEvenement instance");
		try {		
			Query query = null;
			if(utilisateur!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select e from TaServiceWebExterne e where e.proprietaire.id = :idUtilisateur)";
				query = entityManager.createQuery(jpql);
				query.setParameter("idUtilisateur",utilisateur.getId());
			} 
			List<TaServiceWebExterne> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaServiceWebExterneDTO> findByCodeLight(String code) {
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaServiceWebExterne.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeServiceWebExterne", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaServiceWebExterne.QN.FIND_ALL_LIGHT);
			}

			List<TaServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaServiceWebExterneDTO> findAllLightActif() {
		try {
			Query query = null;

			query = entityManager.createNamedQuery(TaServiceWebExterne.QN.FIND_ALL_LIGHT_ACTIF);

			List<TaServiceWebExterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
