package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeCustomerDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class StripeCustomerDAO implements IStripeCustomerDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeCustomerDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeCustomer u";
	//private String defaultJPQLQuery = "select u from TaStripeCustomer u where u.taArticle is null";
	
	public StripeCustomerDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeCustomer)
	 */
	public void persist(TaStripeCustomer transientInstance) {
		logger.debug("persisting TaStripeCustomer instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeCustomer)
	 */
	public void remove(TaStripeCustomer persistentInstance) {
		logger.debug("removing TaStripeCustomer instance");
		try {
			TaStripeCustomer e = entityManager.merge(findById(persistentInstance.getIdStripeCustomer()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeCustomer)
	 */
	public TaStripeCustomer merge(TaStripeCustomer detachedInstance) {
		logger.debug("merging TaStripeCustomer instance");
		try {
			TaStripeCustomer result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeCustomer findByCode(String idExterne) {
		logger.debug("getting TaStripeCustomer instance with code: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeCustomer f where f.idExterne='"+idExterne+"' ");
				TaStripeCustomer instance = (TaStripeCustomer)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeCustomer findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeCustomer instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeCustomer f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeCustomer instance = (TaStripeCustomer)query.getSingleResult();
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
	public TaStripeCustomer findById(int id) {
		logger.debug("getting TaStripeCustomer instance with id: " + id);
		try {
			TaStripeCustomer instance = entityManager.find(TaStripeCustomer.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeCustomer rechercherCustomer(TaTiers tiers) {
		logger.debug("getting TaStripeCustomer instance with code tiers: " + tiers.getCodeTiers());
		try {
			if(!tiers.getCodeTiers().equals("")){
				Query query = entityManager.createQuery("select f from TaStripeCustomer f where f.taTiers.codeTiers='"+tiers.getCodeTiers()+"'");
				TaStripeCustomer instance = (TaStripeCustomer)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public TaStripeCustomer rechercherCustomer(String idExterne) {
		logger.debug("getting TaStripeCustomer instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeCustomer f where f.idExterne='"+idExterne+"'");
				TaStripeCustomer instance = (TaStripeCustomer)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeCustomer> selectAll() {
		logger.debug("selectAll TaStripeCustomer");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeCustomer> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeCustomer refresh(TaStripeCustomer detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeCustomer.class, detachedInstance.getIdStripeCustomer());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeCustomer> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeCustomer instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeCustomer.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeCustomer> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeCustomerDAO,TaStripeCustomer> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeCustomerDAO,TaStripeCustomer>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeCustomerDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeCustomer.QN.FIND_ALL_LIGHT);
//			List<TaStripeCustomerDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeCustomerDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeCustomerDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeCustomer.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeCustomer.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeCustomerDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeCustomer entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeCustomer> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeCustomer> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeCustomer> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeCustomer> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeCustomer value) throws Exception {
		BeanValidator<TaStripeCustomer> validator = new BeanValidator<TaStripeCustomer>(TaStripeCustomer.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeCustomer value, String propertyName) throws Exception {
		BeanValidator<TaStripeCustomer> validator = new BeanValidator<TaStripeCustomer>(TaStripeCustomer.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeCustomer transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
