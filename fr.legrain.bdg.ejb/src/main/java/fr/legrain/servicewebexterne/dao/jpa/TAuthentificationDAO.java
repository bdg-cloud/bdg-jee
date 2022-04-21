package fr.legrain.servicewebexterne.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.dao.ITAuthentificationDAO;
import fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTAuthentification;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTAuthentification;
import fr.legrain.validator.BeanValidator;

public class TAuthentificationDAO implements ITAuthentificationDAO {

	static Logger logger = Logger.getLogger(TAuthentificationDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTAuthentification a";
	
	public TAuthentificationDAO(){
	}

//	public TaTAuthentification refresh(TaTAuthentification detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTAuthentification.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaTAuthentification transientInstance) {
		logger.debug("persisting TaTAuthentification instance");
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
	public void remove(TaTAuthentification persistentInstance) {
		logger.debug("removing TaTAuthentification instance");
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
	public TaTAuthentification merge(TaTAuthentification detachedInstance) {
		logger.debug("merging TaTAuthentification instance");
		try {
			TaTAuthentification result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaTAuthentification findById(int id) {
		logger.debug("getting TaTAuthentification instance with id: " + id);
		try {
			TaTAuthentification instance = entityManager.find(TaTAuthentification.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaTAuthentification> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTAuthentification");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTAuthentification> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaTAuthentification> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTAuthentification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTAuthentification> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTAuthentification> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTAuthentification value) throws Exception {
		BeanValidator<TaTAuthentification> validator = new BeanValidator<TaTAuthentification>(TaTAuthentification.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTAuthentification value, String propertyName) throws Exception {
		BeanValidator<TaTAuthentification> validator = new BeanValidator<TaTAuthentification>(TaTAuthentification.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTAuthentification transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTAuthentification findByCode(String code) {
		logger.debug("getting TaTAuthentification instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTAuthentification f where UPPER(f.codeTAuthentification)='"+ code.toUpperCase()+"'");
			TaTAuthentification instance = (TaTAuthentification)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	public List<TaTAuthentification> findAgendaUtilisateur(TaUtilisateur utilisateur) {
		logger.debug("getting TaEvenement instance");
		try {		
			Query query = null;
			if(utilisateur!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select e from TaTAuthentification e where e.proprietaire.id = :idUtilisateur)";
				query = entityManager.createQuery(jpql);
				query.setParameter("idUtilisateur",utilisateur.getId());
			} 
			List<TaTAuthentification> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTAuthentificationDTO> findByCodeLight(String code) {
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTAuthentification.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTAuthentification", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTAuthentification.QN.FIND_ALL_LIGHT);
			}

			List<TaTAuthentificationDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}
