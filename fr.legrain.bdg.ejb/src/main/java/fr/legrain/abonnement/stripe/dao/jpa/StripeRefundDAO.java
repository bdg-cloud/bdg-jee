package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeRefundDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeRefundDTO;
import fr.legrain.abonnement.stripe.model.TaStripeRefund;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeRefundDAO implements IStripeRefundDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeRefundDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeRefund u";
	//private String defaultJPQLQuery = "select u from TaStripeRefund u where u.taArticle is null";
	
	public StripeRefundDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeRefund)
	 */
	public void persist(TaStripeRefund transientInstance) {
		logger.debug("persisting TaStripeRefund instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeRefund)
	 */
	public void remove(TaStripeRefund persistentInstance) {
		logger.debug("removing TaStripeRefund instance");
		try {
			TaStripeRefund e = entityManager.merge(findById(persistentInstance.getIdStripeRefund()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeRefund)
	 */
	public TaStripeRefund merge(TaStripeRefund detachedInstance) {
		logger.debug("merging TaStripeRefund instance");
		try {
			TaStripeRefund result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeRefund findByCode(String idExterne) {
		logger.debug("getting TaStripeRefund instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeRefund f where f.idExterne='"+idExterne+"' ");
			TaStripeRefund instance = (TaStripeRefund)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeRefund findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeRefund instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeRefund f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeRefund instance = (TaStripeRefund)query.getSingleResult();
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
	public TaStripeRefund findById(int id) {
		logger.debug("getting TaStripeRefund instance with id: " + id);
		try {
			TaStripeRefund instance = entityManager.find(TaStripeRefund.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeRefund> selectAll() {
		logger.debug("selectAll TaStripeRefund");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeRefund> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeRefund refresh(TaStripeRefund detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeRefund.class, detachedInstance.getIdStripeRefund());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeRefund> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeRefund instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeRefund.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeRefund> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeRefundDAO,TaStripeRefund> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeRefundDAO,TaStripeRefund>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeRefundDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeRefund.QN.FIND_ALL_LIGHT);
//			List<TaStripeRefundDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeRefundDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeRefundDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeRefund.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeRefund.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeRefundDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeRefund entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeRefund> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeRefund> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeRefund> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeRefund> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeRefund value) throws Exception {
		BeanValidator<TaStripeRefund> validator = new BeanValidator<TaStripeRefund>(TaStripeRefund.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeRefund value, String propertyName) throws Exception {
		BeanValidator<TaStripeRefund> validator = new BeanValidator<TaStripeRefund>(TaStripeRefund.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeRefund transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
