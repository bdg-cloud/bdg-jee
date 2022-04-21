package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeChargeDAO;
import fr.legrain.abonnement.stripe.dao.IStripePaymentIntentDAO;
import fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.abonnement.stripe.model.TaStripeCard;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaPanier;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripePaymentIntentDAO implements IStripePaymentIntentDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripePaymentIntentDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripePaymentIntent u";
	//private String defaultJPQLQuery = "select u from TaStripePaymentIntent u where u.taArticle is null";
	
	public StripePaymentIntentDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripePaymentIntent)
	 */
	public void persist(TaStripePaymentIntent transientInstance) {
		logger.debug("persisting TaStripePaymentIntent instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripePaymentIntent)
	 */
	public void remove(TaStripePaymentIntent persistentInstance) {
		logger.debug("removing TaStripePaymentIntent instance");
		try {
			TaStripePaymentIntent e = entityManager.merge(findById(persistentInstance.getIdStripePaymentIntent()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripePaymentIntent)
	 */
	public TaStripePaymentIntent merge(TaStripePaymentIntent detachedInstance) {
		logger.debug("merging TaStripePaymentIntent instance");
		try {
			TaStripePaymentIntent result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripePaymentIntent findByCode(String idExterne) {
		logger.debug("getting TaStripePaymentIntent instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
			Query query = entityManager.createQuery("select f from TaStripePaymentIntent f left join fetch f.taReglement r where f.idExterne='"+idExterne+"' ");
			TaStripePaymentIntent instance = (TaStripePaymentIntent)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripePaymentIntent findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripePaymentIntent instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripePaymentIntent f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripePaymentIntent instance = (TaStripePaymentIntent)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripePaymentIntent findPaiementNonCapture(SWTDocument doc) {
//		logger.debug("getting TaStripePaymentIntent instance with code: " + code);
		try {
			String jpql = "select pi from TaStripePaymentIntent pi ";
			if(doc != null ) {
				if(doc instanceof TaPanier) {
					jpql += "join pi.taPanier p where p.idDocument = "+doc.getIdDocument()+" and pi.status = 'requires_capture' ";
					
					Query query = entityManager.createQuery(jpql);
					TaStripePaymentIntent instance = (TaStripePaymentIntent)query.getSingleResult();
					logger.debug("get successful");
					return instance;
				}
				if(doc instanceof TaBoncde) {
					//chercher la liaison avec un panier d'abord
					TaPanier panierCorrespondant = null;
					jpql = "select rdoc.taPanier from TaRDocument rdoc "
							+ " where rdoc.taBoncde.idDocument = "+doc.getIdDocument()+" and "
									+ " rdoc.taPanier is not null";
					Query query = entityManager.createQuery(jpql);
					panierCorrespondant = (TaPanier)query.getSingleResult();

					if(panierCorrespondant!=null) {
						return findPaiementNonCapture(panierCorrespondant);
					
					}

				}
			
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentCustomerDTO(String idExterneCustomer){
		logger.debug("getting TaStripeInvoice instance with idExterneCustomer: " + idExterneCustomer);
		try {
			if(!idExterneCustomer.equals("")){
				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO("
						+ " f.id, f.idExterne, reg.idDocument, reg.codeDocument, s.idStripeSource, s.idExterne, c.idStripeCard, c.idExterne, b.idStripeBankAccount, b.idExterne,"
						+ " f.refunded, f.paid, f.captured, i.idStripeInvoice, i.idExterne, cus.idStripeCustomer, cus.idExterne,"
						+ " f.amount, f.amountRefunded, f.description, f.status, f.dateCharge, reg.dateDocument, f.versionObj) "
						+ " from TaStripePaymentIntent f "
						+ " left join f.taReglement reg "
						+ " left join f.taStripeSource s "
						+ " left join f.taStripeCard c "
						+ " left join f.taStripeCustomer cus "
						+ " left join f.taStripeInvoice i "
						+ " left join f.taStripeBankAccount b "
						+ " where cus.idExterne='"+idExterneCustomer+"' order by reg.dateDocument desc NULLS LAST, f.dateCharge desc");
				List<TaStripePaymentIntentDTO> instance = (List<TaStripePaymentIntentDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public List<TaStripePaymentIntent> rechercherPaymentIntentCustomer(String idExterneCustomer) {
		logger.debug("getting TaStripeInvoice instance with idExterneCustomer: " + idExterneCustomer);
		try {
			if(!idExterneCustomer.equals("")){
				Query query = entityManager.createQuery("select f "
						+ " from TaStripePaymentIntent f "
						+ " left join f.taStripeSource s "
						+ " left join f.taStripeCard c "
						+ " left join f.taStripeCustomer cus "
						+ " left join f.taStripeInvoice i "
						+ " left join f.taStripeBankAccount b "
						+ " where cus.idExterne='"+idExterneCustomer+"' ");
				List<TaStripePaymentIntent> instance = (List<TaStripePaymentIntent>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	;
	
	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentDTO() {
//		logger.debug("getting TaStripeInvoice instance with idExterneInvoice: " );
		try {
//			if(!customerStripeID.equals("")){
				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO("
						+ " f.id, f.idExterne, null, null, s.idStripeSource, s.idExterne, c.idStripeCard, c.idExterne, b.idStripeBankAccount, b.idExterne,"
						+ " f.refunded, f.paid, f.captured, i.idStripeInvoice, i.idExterne, cus.idStripeCustomer, cus.idExterne,"
						+ " f.amount, f.amountRefunded, f.description, f.versionObj) "
						+ " from TaStripePaymentIntent f"
						+ " left join f.taStripeSource s "
						+ " left join f.taStripeCard c "
						+ " left join f.taStripeCard cus "
						+ " left join f.taStripeInvoice i "
						+ " left join f.taStripeBankAccount b ");
				List<TaStripePaymentIntentDTO> instance = (List<TaStripePaymentIntentDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
//			}
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public List<TaStripePaymentIntent> rechercherPaymentIntent() {
//		logger.debug("getting TaStripeInvoice instance with idExterneInvoice: " + customerStripeID);
		try {
//			if(!customerStripeID.equals("")){
				Query query = entityManager.createQuery("select f  from TaStripePaymentIntent f ");
				List<TaStripePaymentIntent> instance = (List<TaStripePaymentIntent>)query.getResultList();
				logger.debug("get successful");
				return instance;
//			}
		} catch (RuntimeException re) {
		    return null;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaStripePaymentIntent findById(int id) {
		logger.debug("getting TaStripePaymentIntent instance with id: " + id);
		try {
			TaStripePaymentIntent instance = entityManager.find(TaStripePaymentIntent.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripePaymentIntent> selectAll() {
		logger.debug("selectAll TaStripePaymentIntent");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripePaymentIntent> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripePaymentIntent refresh(TaStripePaymentIntent detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripePaymentIntent.class, detachedInstance.getIdStripePaymentIntent());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripePaymentIntent> findByArticle(String codeArticle) {
		logger.debug("getting TaStripePaymentIntent instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripePaymentIntent.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripePaymentIntent> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripePaymentIntentDAO,TaStripePaymentIntent> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripePaymentIntentDAO,TaStripePaymentIntent>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripePaymentIntentDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripePaymentIntent.QN.FIND_ALL_LIGHT);
//			List<TaStripePaymentIntentDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripePaymentIntentDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripePaymentIntentDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripePaymentIntent.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripePaymentIntent.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripePaymentIntentDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripePaymentIntent entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripePaymentIntent> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripePaymentIntent> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripePaymentIntent> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripePaymentIntent> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripePaymentIntent value) throws Exception {
		BeanValidator<TaStripePaymentIntent> validator = new BeanValidator<TaStripePaymentIntent>(TaStripePaymentIntent.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripePaymentIntent value, String propertyName) throws Exception {
		BeanValidator<TaStripePaymentIntent> validator = new BeanValidator<TaStripePaymentIntent>(TaStripePaymentIntent.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripePaymentIntent transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
