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

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.general.dao.ILiaisonDossierMaitreDAO;
import fr.legrain.general.dto.TaLiaisonDossierMaitreDTO;
import fr.legrain.general.model.TaLiaisonDossierMaitre;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaLiaisonDossierMaitre.
 * @see fr.legrain.tiers.model.old.TaLiaisonDossierMaitre
 * @author Hibernate Tools
 */
public class TaLiaisonDossierMaitreDAO implements ILiaisonDossierMaitreDAO {

	static Logger logger = Logger.getLogger(TaLiaisonDossierMaitreDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLiaisonDossierMaitre a";
	
	public TaLiaisonDossierMaitreDAO(){
	}

	public TaLiaisonDossierMaitre login(String login, String password) {
		logger.debug("getting TaLiaisonDossierMaitre instance with login: " + login);
		try {
			if(!login.equals("")){
				Query query = entityManager.createQuery("select a from TaLiaisonDossierMaitre a where a.email='"+login+"' and a.password='"+password+"'");
				TaLiaisonDossierMaitre instance = (TaLiaisonDossierMaitre)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLiaisonDossierMaitreDTO loginDTO(String login, String password) {
		logger.debug("getting TaLiaisonDossierMaitre instance with login: " + login);
		try {
			if(!login.equals("")){
				Query query = entityManager.createQuery(
						"select new fr.legrain.general.dto.TaLiaisonDossierMaitreDTO("
						+ " a.idLiaisonDossierMaitre, a.email, a.password, a.versionObj) "
						+ " from TaLiaisonDossierMaitre a'");
				TaLiaisonDossierMaitreDTO instance = (TaLiaisonDossierMaitreDTO)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLiaisonDossierMaitreDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaLiaisonDossierMaitre.QN.FIND_ALL_LIGHT);
			@SuppressWarnings("unchecked")
			List<TaLiaisonDossierMaitreDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void persist(TaLiaisonDossierMaitre transientInstance) {
		logger.debug("persisting TaLiaisonDossierMaitre instance");
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
	public void remove(TaLiaisonDossierMaitre persistentInstance) {
		logger.debug("removing TaLiaisonDossierMaitre instance");
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
	public TaLiaisonDossierMaitre merge(TaLiaisonDossierMaitre detachedInstance) {
		logger.debug("merging TaLiaisonDossierMaitre instance");
		try {
			TaLiaisonDossierMaitre result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaLiaisonDossierMaitre findById(int id) {
		logger.debug("getting TaLiaisonDossierMaitre instance with id: " + id);
		try {
			TaLiaisonDossierMaitre instance = entityManager.find(TaLiaisonDossierMaitre.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLiaisonDossierMaitre> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLiaisonDossierMaitre");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLiaisonDossierMaitre> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaLiaisonDossierMaitre> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLiaisonDossierMaitre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLiaisonDossierMaitre> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLiaisonDossierMaitre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLiaisonDossierMaitre value) throws Exception {
		BeanValidator<TaLiaisonDossierMaitre> validator = new BeanValidator<TaLiaisonDossierMaitre>(TaLiaisonDossierMaitre.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLiaisonDossierMaitre value, String propertyName) throws Exception {
		BeanValidator<TaLiaisonDossierMaitre> validator = new BeanValidator<TaLiaisonDossierMaitre>(TaLiaisonDossierMaitre.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLiaisonDossierMaitre transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaLiaisonDossierMaitre findByCode(String code) {
		logger.debug("getting TaLiaisonDossierMaitre instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaLiaisonDossierMaitre a where a.email='"+code+"'");
				TaLiaisonDossierMaitre instance = (TaLiaisonDossierMaitre)query.getSingleResult();
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
	
	/**
	 * @return - l'unique instance de TaLiaisonDossierMaitre si elle existe, sinon retourne null.
	 */
	public TaLiaisonDossierMaitre findInstance() {

		logger.debug("getting TaLiaisonDossierMaitre instance ");
		try {
			TaLiaisonDossierMaitre instance = null;
			List<TaLiaisonDossierMaitre> liste =   selectAll();
			if(liste != null && !liste.isEmpty()) {
				 instance = liste.get(0);
			}
			
//			if(instance == null) {
//				logger.debug("Aucun objet TaLiaisonDossierMaitre trouve, creation d'une nouvelle instance vide");
//				instance = new TaLiaisonDossierMaitre();
//				instance = merge(instance);
//			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	/**
	 * @return - l'unique instance de TaLiaisonDossierMaitre si elle existe, sinon on en crée une avec email et mot de passe.
	 */
	public TaLiaisonDossierMaitre findInstance(String email, String password, String codeTiers) {
		logger.debug("getting TaLiaisonDossierMaitre instance ");
		try {
			TaLiaisonDossierMaitre instance = null;
			List<TaLiaisonDossierMaitre> liste =   selectAll();
			if(liste != null && !liste.isEmpty()) {
				 instance = liste.get(0);
			}
			if(instance == null) {
				logger.debug("Aucun objet TaLiaisonDossierMaitre trouve, creation d'une nouvelle instance vide");
				instance = new TaLiaisonDossierMaitre();
				instance.setEmail(email);
				password = LibCrypto.encrypt(password);
				instance.setPassword(password);
				instance.setCodeTiers(codeTiers);
				instance = merge(instance);
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
//	public void ctrlSaisieSpecifique(TaLiaisonDossierMaitre entity,String field) throws ExceptLgr {	
//		
//	}

}
