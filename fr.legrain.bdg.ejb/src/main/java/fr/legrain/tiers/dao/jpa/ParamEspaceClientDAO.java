package fr.legrain.tiers.dao.jpa;

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
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.IAdresseDAO;
import fr.legrain.tiers.dao.IEspaceClientDAO;
import fr.legrain.tiers.dao.IParamEspaceClientDAO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaParamEspaceClient.
 * @see fr.legrain.tiers.model.old.TaParamEspaceClient
 * @author Hibernate Tools
 */
public class ParamEspaceClientDAO implements IParamEspaceClientDAO {

	static Logger logger = Logger.getLogger(ParamEspaceClientDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaParamEspaceClient a";
	
	public ParamEspaceClientDAO(){
	}

//	public TaParamEspaceClient refresh(TaParamEspaceClient detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaParamEspaceClient.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public TaParamEspaceClient findInstance() {
		int premierId = 1;
		logger.debug("getting TaParamEspaceClient instance with id: "+premierId);
		try {
			TaParamEspaceClient instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaParamEspaceClient trouve, creation d'une nouvelle instance vide");
				instance = new TaParamEspaceClient();
				
				instance.setAfficheCommandes(true);
				instance.setAfficheDevis(true);
				instance.setAfficheFactures(true);
				instance.setAfficheLivraisons(true);
				instance.setEspaceClientActif(true);
				instance.setPaiementCb(true);
				instance.setLogoLogin(null);
				instance.setLogoPagesSimples(null);
				instance.setLogoFooter(null);

				instance = merge(instance);

				
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public void persist(TaParamEspaceClient transientInstance) {
		logger.debug("persisting TaParamEspaceClient instance");
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
	public void remove(TaParamEspaceClient persistentInstance) {
		logger.debug("removing TaParamEspaceClient instance");
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
	public TaParamEspaceClient merge(TaParamEspaceClient detachedInstance) {
		logger.debug("merging TaParamEspaceClient instance");
		try {
			TaParamEspaceClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaParamEspaceClient findById(int id) {
		logger.debug("getting TaParamEspaceClient instance with id: " + id);
		try {
			TaParamEspaceClient instance = entityManager.find(TaParamEspaceClient.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParamEspaceClient> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaParamEspaceClient");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaParamEspaceClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaParamEspaceClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaParamEspaceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaParamEspaceClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaParamEspaceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaParamEspaceClient value) throws Exception {
		BeanValidator<TaParamEspaceClient> validator = new BeanValidator<TaParamEspaceClient>(TaParamEspaceClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaParamEspaceClient value, String propertyName) throws Exception {
		BeanValidator<TaParamEspaceClient> validator = new BeanValidator<TaParamEspaceClient>(TaParamEspaceClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaParamEspaceClient transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaParamEspaceClient findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaParamEspaceClient entity,String field) throws ExceptLgr {	
//		
//	}

}
