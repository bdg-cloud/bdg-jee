package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeInvoiceDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeInvoiceDAO implements IStripeInvoiceDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeInvoiceDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeInvoice u";
	//private String defaultJPQLQuery = "select u from TaStripeInvoice u where u.taArticle is null";
	
	public StripeInvoiceDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeInvoice)
	 */
	public void persist(TaStripeInvoice transientInstance) {
		logger.debug("persisting TaStripeInvoice instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeInvoice)
	 */
	public void remove(TaStripeInvoice persistentInstance) {
		logger.debug("removing TaStripeInvoice instance");
		try {
			TaStripeInvoice e = entityManager.merge(findById(persistentInstance.getIdStripeInvoice()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeInvoice)
	 */
	public TaStripeInvoice merge(TaStripeInvoice detachedInstance) {
		logger.debug("merging TaStripeInvoice instance");
		try {
			TaStripeInvoice result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeInvoice findByCode(String idExterne) {
		logger.debug("getting TaStripeInvoice instance with code: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeInvoice f where f.idExterne='"+idExterne+"' ");
				TaStripeInvoice instance = (TaStripeInvoice)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeInvoice findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeInvoice instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeInvoice f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeInvoice instance = (TaStripeInvoice)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscriptionDTO(String subscriptionStripeID, Integer limite) {
		logger.debug("getting TaStripeInvoice instance with subscriptionStripeID: " + subscriptionStripeID);
		try {
			if(!subscriptionStripeID.equals("")){
				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO("
						+ " f.id, f.idExterne, f.taCustomer.idStripeCustomer, f.taCustomer.idExterne, f.taAbonnement.idDocument, f.taAbonnement.idExterne,"
						+ " f.amountDue, f.number, f.dueDate, f.created, f.paid, f.periodStart, f.periodEnd,"
						+ " f.webhooksDeliveredAt, f.nexPaymentAttempt, f.description, f.billingReason, f.billing, f.attempted, f.attemptCount,"
						+ " ae.idDocument, ae.codeDocument, r.idDocument, r.codeDocument, f.versionObj) "
						+ " from TaStripeInvoice f left join f.taAvisEcheance ae left join f.taReglement r "
						+ " where f.taAbonnement.idExterne= :subscriptionStripeID ");
				query.setParameter("subscriptionStripeID", subscriptionStripeID);
				List<TaStripeInvoiceDTO> instance = (List<TaStripeInvoiceDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public List<TaStripeInvoice> rechercheInvoiceStripeForSubscription(String subscriptionStripeID, Integer limite) {
		logger.debug("getting TaStripeInvoice instance with subscriptionStripeID: " + subscriptionStripeID);
		try {
			if(!subscriptionStripeID.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeInvoice f where f.taAbonnement.idExterne=:subscriptionStripeID");
				query.setParameter("subscriptionStripeID", subscriptionStripeID);
				List<TaStripeInvoice> instance = (List<TaStripeInvoice>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForCustomerDTO(String customerStripeID, Integer limite) {
		logger.debug("getting TaStripeInvoice instance with idExterneInvoice: " + customerStripeID);
		try {
			if(!customerStripeID.equals("")){
				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO("
						+ " f.id, f.idExterne, f.taCustomer.idStripeCustomer, f.taCustomer.idExterne, f.taAbonnement.idDocument, f.taAbonnement.idExterne,"
						+ " f.amountDue, f.number, f.dueDate, f.created, f.paid, f.periodStart, f.periodEnd,"
						+ " f.webhooksDeliveredAt, f.nexPaymentAttempt, f.description, f.billingReason, f.billing, f.attempted, f.attemptCount,"
						+ " ae.idDocument, ae.codeDocument, r.idDocument, r.codeDocument, f.versionObj) "
						+ " from TaStripeInvoice f left join f.taAvisEcheance ae left join f.taReglement r "
						+ " where f.taCustomer.idExterne= :customerStripeID ");
				query.setParameter("customerStripeID", customerStripeID);
				List<TaStripeInvoiceDTO> instance = (List<TaStripeInvoiceDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public List<TaStripeInvoice> rechercheInvoiceStripeForCustomer(String customerStripeID, Integer limite) {
		logger.debug("getting TaStripeInvoice instance with idExterneInvoice: " + customerStripeID);
		try {
			if(!customerStripeID.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeInvoice f where f.taCustomer.idExterne='"+customerStripeID+"' ");
				List<TaStripeInvoice> instance = (List<TaStripeInvoice>)query.getResultList();
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
	public TaStripeInvoice findById(int id) {
		logger.debug("getting TaStripeInvoice instance with id: " + id);
		try {
			TaStripeInvoice instance = entityManager.find(TaStripeInvoice.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeInvoice> selectAll() {
		logger.debug("selectAll TaStripeInvoice");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeInvoice> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeInvoice refresh(TaStripeInvoice detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeInvoice.class, detachedInstance.getIdStripeInvoice());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeInvoice> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeInvoice instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeInvoice.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeInvoice> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeInvoiceDAO,TaStripeInvoice> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeInvoiceDAO,TaStripeInvoice>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeInvoiceDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeInvoice.QN.FIND_ALL_LIGHT);
//			List<TaStripeInvoiceDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeInvoiceDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeInvoiceDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeInvoice.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeInvoice.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeInvoiceDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeInvoice entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeInvoice> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeInvoice> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeInvoice> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeInvoice> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeInvoice value) throws Exception {
		BeanValidator<TaStripeInvoice> validator = new BeanValidator<TaStripeInvoice>(TaStripeInvoice.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeInvoice value, String propertyName) throws Exception {
		BeanValidator<TaStripeInvoice> validator = new BeanValidator<TaStripeInvoice>(TaStripeInvoice.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeInvoice transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
