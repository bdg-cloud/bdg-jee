package fr.legrain.general.dao.jpa;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.general.dao.ITaParametreDAO;
import fr.legrain.general.dao.ITaParametreDAO;
import fr.legrain.general.model.TaParametre;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**

 */
public class TaParametreDAO implements ITaParametreDAO {

	static Logger logger = Logger.getLogger(TaParametreDAO.class);
	
	@PersistenceContext(unitName = "bdg_prog")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaParametre a ";
	
	public TaParametreDAO() {
	}
	
//	public TaParametre refresh(TaParametre detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaParametre.class, detachedInstance.getIdTaParametre());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	/**
	 * @return - l'unique instance de TaParametre si elle existe, sinon retourne null.
	 */
	public TaParametre findInstance() {
		int premierId = 1;
		logger.debug("getting TaParametre instance with id: "+premierId);
		try {
			TaParametre instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaParametre trouve, creation d'une nouvelle instance vide");
				instance = new TaParametre();
				instance = merge(instance);	
				
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public void persist(TaParametre transientInstance) {
		logger.debug("persisting TaParametre instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaParametre persistentInstance) {
		logger.debug("removing TaParametre instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaParametre merge(TaParametre detachedInstance) {
		logger.debug("merging TaParametre instance");
		try {
			TaParametre result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaParametre findById(int id) {
		logger.debug("getting TaParametre instance with id: " + id);
		try {
			TaParametre instance = entityManager.find(TaParametre.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaParametre findByCode(String code) {
		logger.debug("getting TaParametre instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaParametre f where f.alias='"+code+"'");
			TaParametre instance = (TaParametre)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParametre> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaParametre");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaParametre> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParametre> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaParametre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaParametre> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaParametre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaParametre value) throws Exception {
		BeanValidator<TaParametre> validator = new BeanValidator<TaParametre>(TaParametre.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaParametre value, String propertyName) throws Exception {
		BeanValidator<TaParametre> validator = new BeanValidator<TaParametre>(TaParametre.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaParametre transientInstance) {
		entityManager.detach(transientInstance);
	}
//	public void ctrlSaisieSpecifique(TaParametre entity,String field) throws ExceptLgr {	
//		
//	}
}
