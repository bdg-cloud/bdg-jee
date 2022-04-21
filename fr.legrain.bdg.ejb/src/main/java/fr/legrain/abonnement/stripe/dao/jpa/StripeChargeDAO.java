package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeChargeDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.abonnement.stripe.model.TaStripeCard;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeChargeDAO implements IStripeChargeDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeChargeDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeCharge u";
	//private String defaultJPQLQuery = "select u from TaStripeCharge u where u.taArticle is null";
	
	public StripeChargeDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeCharge)
	 */
	public void persist(TaStripeCharge transientInstance) {
		logger.debug("persisting TaStripeCharge instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeCharge)
	 */
	public void remove(TaStripeCharge persistentInstance) {
		logger.debug("removing TaStripeCharge instance");
		try {
			TaStripeCharge e = entityManager.merge(findById(persistentInstance.getIdStripeCharge()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeCharge)
	 */
	public TaStripeCharge merge(TaStripeCharge detachedInstance) {
		logger.debug("merging TaStripeCharge instance");
		try {
			TaStripeCharge result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeCharge findByCode(String idExterne) {
		logger.debug("getting TaStripeCharge instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeCharge f left join fetch f.taReglement r where f.idExterne='"+idExterne+"' ");
			TaStripeCharge instance = (TaStripeCharge)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeCharge findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeCharge instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeCharge f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeCharge instance = (TaStripeCharge)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripeChargeDTO> rechercherChargeCustomerDTO(String idExterneCustomer){
		logger.debug("getting TaStripeInvoice instance with idExterneCustomer: " + idExterneCustomer);
		try {
			if(!idExterneCustomer.equals("")){
				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO("
						+ " f.id, f.idExterne, reg.idDocument, reg.codeDocument, s.idStripeSource, s.idExterne, c.idStripeCard, c.idExterne, b.idStripeBankAccount, b.idExterne,"
						+ " f.refunded, f.paid, f.captured, i.idStripeInvoice, i.idExterne, cus.idStripeCustomer, cus.idExterne,"
						+ " f.amount, f.amountRefunded, f.description, f.status, f.dateCharge, reg.dateDocument, f.versionObj) "
						+ " from TaStripeCharge f "
						+ " left join f.taReglement reg "
						+ " left join f.taStripeSource s "
						+ " left join f.taStripeCard c "
						+ " left join f.taStripeCustomer cus "
						+ " left join f.taStripeInvoice i "
						+ " left join f.taStripeBankAccount b "
						+ " where cus.idExterne='"+idExterneCustomer+"' order by reg.dateDocument desc NULLS LAST, f.dateCharge desc");
				List<TaStripeChargeDTO> instance = (List<TaStripeChargeDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public List<TaStripeCharge> rechercherChargeCustomer(String idExterneCustomer) {
		logger.debug("getting TaStripeInvoice instance with idExterneCustomer: " + idExterneCustomer);
		try {
			if(!idExterneCustomer.equals("")){
				Query query = entityManager.createQuery("select f "
						+ " from TaStripeCharge f "
						+ " left join f.taStripeSource s "
						+ " left join f.taStripeCard c "
						+ " left join f.taStripeCustomer cus "
						+ " left join f.taStripeInvoice i "
						+ " left join f.taStripeBankAccount b "
						+ " where cus.idExterne='"+idExterneCustomer+"' ");
				List<TaStripeCharge> instance = (List<TaStripeCharge>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	;
	
	public List<TaStripeChargeDTO> rechercherChargeDTO() {
//		logger.debug("getting TaStripeInvoice instance with idExterneInvoice: " );
		try {
//			if(!customerStripeID.equals("")){
				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO("
						+ " f.id, f.idExterne, null, null, s.idStripeSource, s.idExterne, c.idStripeCard, c.idExterne, b.idStripeBankAccount, b.idExterne,"
						+ " f.refunded, f.paid, f.captured, i.idStripeInvoice, i.idExterne, cus.idStripeCustomer, cus.idExterne,"
						+ " f.amount, f.amountRefunded, f.description, f.versionObj) "
						+ " from TaStripeCharge f"
						+ " left join f.taStripeSource s "
						+ " left join f.taStripeCard c "
						+ " left join f.taStripeCard cus "
						+ " left join f.taStripeInvoice i "
						+ " left join f.taStripeBankAccount b ");
				List<TaStripeChargeDTO> instance = (List<TaStripeChargeDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
//			}
		} catch (RuntimeException re) {
		    return null;
		}
	}
	public List<TaStripeCharge> rechercherCharge() {
//		logger.debug("getting TaStripeInvoice instance with idExterneInvoice: " + customerStripeID);
		try {
//			if(!customerStripeID.equals("")){
				Query query = entityManager.createQuery("select f  from TaStripeCharge f ");
				List<TaStripeCharge> instance = (List<TaStripeCharge>)query.getResultList();
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
	public TaStripeCharge findById(int id) {
		logger.debug("getting TaStripeCharge instance with id: " + id);
		try {
			TaStripeCharge instance = entityManager.find(TaStripeCharge.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeCharge> selectAll() {
		logger.debug("selectAll TaStripeCharge");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeCharge> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeCharge refresh(TaStripeCharge detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeCharge.class, detachedInstance.getIdStripeCharge());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeCharge> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeCharge instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeCharge.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeCharge> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeChargeDAO,TaStripeCharge> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeChargeDAO,TaStripeCharge>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeChargeDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeCharge.QN.FIND_ALL_LIGHT);
//			List<TaStripeChargeDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeChargeDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeChargeDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeCharge.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeCharge.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeChargeDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeCharge entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeCharge> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeCharge> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeCharge> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeCharge> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeCharge value) throws Exception {
		BeanValidator<TaStripeCharge> validator = new BeanValidator<TaStripeCharge>(TaStripeCharge.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeCharge value, String propertyName) throws Exception {
		BeanValidator<TaStripeCharge> validator = new BeanValidator<TaStripeCharge>(TaStripeCharge.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeCharge transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
