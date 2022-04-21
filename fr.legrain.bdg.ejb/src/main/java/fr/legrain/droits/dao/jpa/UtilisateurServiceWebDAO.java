package fr.legrain.droits.dao.jpa;


import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.droits.dao.IUtilisateurDAO;
import fr.legrain.droits.dao.IUtilisateurWebServiceDAO;
import fr.legrain.droits.dto.TaUtilisateurWebServiceDTO;
import fr.legrain.droits.model.TaUtilisateurWebService;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class UtilisateurServiceWebDAO implements IUtilisateurWebServiceDAO {

	private static final Log logger = LogFactory.getLog(UtilisateurServiceWebDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaUtilisateurWebService p where p.systeme is null or p.systeme = false";
	
	public UtilisateurServiceWebDAO(){
//		this(null);
	}

	public TaUtilisateurWebService login(String login, String password) {
		logger.debug("getting TaUtilisateurWebService instance with login: " + login);
		try {
			if(!login.equals("")){
				Query query = entityManager.createQuery("select a from TaUtilisateurWebService a where a.login='"+login+"' and a.passwd='"+password+"'");
				TaUtilisateurWebService instance = (TaUtilisateurWebService)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}

	public TaUtilisateurWebServiceDTO loginDTO(String login, String password) {
		logger.debug("getting TaUtilisateurWebService instance with login: " + login);
		try {
			if(!login.equals("")){
				Query query = entityManager.createQuery(
						"select new fr.legrain.droits.dto.TaUtilisateurWebServiceDTO("
						+ " a.id, a.login, a.passwd, a.description, a.email, a.autorisations,"  
						+ " a.dernierAcces, a.accessToken, a.refreshToken, a.actif, a.systeme) "
						+ " from TaUtilisateurWebService a where a.login='"+login+"' and a.passwd='"+password+"'");
				TaUtilisateurWebServiceDTO instance = (TaUtilisateurWebServiceDTO)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}


	public void persist(TaUtilisateurWebService transientInstance) {
		logger.debug("persisting TaUtilisateurWebService instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaUtilisateurWebService persistentInstance) {
		logger.debug("removing TaUtilisateurWebService instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaUtilisateurWebService merge(TaUtilisateurWebService detachedInstance) {
		logger.debug("merging TaUtilisateurWebService instance");
		try {
			TaUtilisateurWebService result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaUtilisateurWebService findById(int id) {
		logger.debug("getting TaUtilisateurWebService instance with id: " + id);
		try {
			TaUtilisateurWebService instance = entityManager.find(TaUtilisateurWebService.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaUtilisateurWebService> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaUtilisateurWebService> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaUtilisateurWebService entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaUtilisateurWebService> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaUtilisateurWebService> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaUtilisateurWebService> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaUtilisateurWebService> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaUtilisateurWebService value) throws Exception {
		BeanValidator<TaUtilisateurWebService> validator = new BeanValidator<TaUtilisateurWebService>(TaUtilisateurWebService.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaUtilisateurWebService value, String propertyName) throws Exception {
		BeanValidator<TaUtilisateurWebService> validator = new BeanValidator<TaUtilisateurWebService>(TaUtilisateurWebService.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaUtilisateurWebService transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaUtilisateurWebService findByCode(String code) {
		logger.debug("getting TaUtilisateurWebService instance with login: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaUtilisateurWebService f where f.login='"+code+"'");
				TaUtilisateurWebService instance = (TaUtilisateurWebService)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
	public List<TaUtilisateurWebServiceDTO> findByCodeLight(String code) {
		logger.debug("select TaUtilisateurWebServiceDTO");
		try {
			Query ejbQuery = entityManager.createQuery("select new fr.legrain.droits.dto.TaUtilisateurWebServiceDTO(u.id, u.login, u.nom, u.prenom, u.email, u.dernierAcces, u.actif) from TaUtilisateurWebService u where u.email like '%"+code+"%' and u.actif = true");
			List<TaUtilisateurWebServiceDTO> l = ejbQuery.getResultList();
			logger.debug("select successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("select failed", re);
			throw re;
		}
	}
	
	public List<TaUtilisateurWebServiceDTO> findByNomLight(String nom) {
		logger.debug("select TaUtilisateurWebServiceDTO");
		try {
			Query ejbQuery = entityManager.createQuery("select new fr.legrain.droits.dto.TaUtilisateurWebServiceDTO(u.id, u.login, u.nom, u.prenom, u.email, u.dernierAcces, u.actif) from TaUtilisateurWebService u where u.nom like '%"+nom+"%' and u.actif = true");
			List<TaUtilisateurWebServiceDTO> l = ejbQuery.getResultList();
			logger.debug("select successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("select failed", re);
			throw re;
		}
	}

	@Override
	public void synchroniseCompteUtilisateurDossierEtWebService() {
		// TODO Auto-generated method stub
		
	}
	
}

