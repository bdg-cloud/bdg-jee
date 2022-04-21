package fr.legrain.tache.dao.jpa;


import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.model.TaFabrication;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.tache.dao.IAgendaDAO;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaTypeEvenement;
import fr.legrain.validator.BeanValidator;

public class AgendaDAO implements IAgendaDAO {

	static Logger logger = Logger.getLogger(AgendaDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaAgenda a";
	
	public AgendaDAO(){
	}

//	public TaAgenda refresh(TaAgenda detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaAgenda.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaAgenda transientInstance) {
		logger.debug("persisting TaAgenda instance");
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
	public void remove(TaAgenda persistentInstance) {
		logger.debug("removing TaAgenda instance");
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
	public TaAgenda merge(TaAgenda detachedInstance) {
		logger.debug("merging TaAgenda instance");
		try {
			TaAgenda result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaAgenda findById(int id) {
		logger.debug("getting TaAgenda instance with id: " + id);
		try {
			TaAgenda instance = entityManager.find(TaAgenda.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaAgenda> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaAgenda");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAgenda> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaAgenda> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAgenda> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAgenda> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAgenda> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAgenda value) throws Exception {
		BeanValidator<TaAgenda> validator = new BeanValidator<TaAgenda>(TaAgenda.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAgenda value, String propertyName) throws Exception {
		BeanValidator<TaAgenda> validator = new BeanValidator<TaAgenda>(TaAgenda.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAgenda transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaAgenda findByCode(String code) {
		logger.debug("getting TaTiers instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaAgenda a where a.nom='"+code+"'");
				TaAgenda instance = (TaAgenda)query.getSingleResult();
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
	
	public List<TaAgenda> findAgendaUtilisateur(TaUtilisateur utilisateur) {
		logger.debug("getting TaEvenement instance");
		try {		
			Query query = null;
			if(utilisateur!=null) {
				//query = entityManager.createNamedQuery(TaFabrication.QN.FIND_BY_DATE_LIGHT);
				String jpql = "select e from TaAgenda e where e.proprietaire.id = :idUtilisateur";
				query = entityManager.createQuery(jpql);
				query.setParameter("idUtilisateur",utilisateur.getId());
			} 
			List<TaAgenda> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaAgenda entity,String field) throws ExceptLgr {	
//		
//	}

}
