package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeAccountDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class StripeAccountDAO implements IStripeAccountDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeAccountDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeAccount u";
	//private String defaultJPQLQuery = "select u from TaStripeAccount u where u.taArticle is null";
	
	public StripeAccountDAO(){
	}
	
	/**
	 * @return - l'unique instance de TaStripeAccount si elle existe, sinon retourne null.
	 */
	public TaStripeAccount findInstance() {
		int premierId = 1;
		logger.debug("getting TaStripeAccount instance with id: "+premierId);
		try {
			TaStripeAccount instance = findById(premierId);
			if(instance == null) {
				logger.debug("Aucun objet TaStripeAccount trouve, creation d'une nouvelle instance vide");
				instance = new TaStripeAccount();
				
				instance = merge(instance);
				
				
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeAccount)
	 */
	public void persist(TaStripeAccount transientInstance) {
		logger.debug("persisting TaStripeAccount instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeAccount)
	 */
	public void remove(TaStripeAccount persistentInstance) {
		logger.debug("removing TaStripeAccount instance");
		try {
			TaStripeAccount e = entityManager.merge(findById(persistentInstance.getIdStripeAccount()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeAccount)
	 */
	public TaStripeAccount merge(TaStripeAccount detachedInstance) {
		logger.debug("merging TaStripeAccount instance");
		try {
			TaStripeAccount result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeAccount findByCode(String idExterne) {
		logger.debug("getting TaStripeAccount instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeAccount f where f.idExterne='"+idExterne+"' ");
				TaStripeAccount instance = (TaStripeAccount)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeAccount findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeAccount instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeAccount f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeAccount instance = (TaStripeAccount)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaStripeAccount findById(int id) {
		logger.debug("getting TaStripeAccount instance with id: " + id);
		try {
			TaStripeAccount instance = entityManager.find(TaStripeAccount.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeAccount> selectAll() {
		logger.debug("selectAll TaStripeAccount");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeAccount> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeAccount refresh(TaStripeAccount detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeAccount.class, detachedInstance.getIdStripeAccount());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeAccount> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeAccount instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeAccount.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeAccount> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeAccountDAO,TaStripeAccount> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeAccountDAO,TaStripeAccount>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeAccountDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeAccount.QN.FIND_ALL_LIGHT);
//			List<TaStripeAccountDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeAccountDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeAccountDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeAccount.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeAccount.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeAccountDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeAccount entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeAccount> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeAccount> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeAccount> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeAccount> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeAccount value) throws Exception {
		BeanValidator<TaStripeAccount> validator = new BeanValidator<TaStripeAccount>(TaStripeAccount.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeAccount value, String propertyName) throws Exception {
		BeanValidator<TaStripeAccount> validator = new BeanValidator<TaStripeAccount>(TaStripeAccount.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeAccount transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
