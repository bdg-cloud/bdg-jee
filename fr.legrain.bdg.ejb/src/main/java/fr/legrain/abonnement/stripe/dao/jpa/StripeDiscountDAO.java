package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeDiscountDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeDiscountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeDiscount;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeDiscountDAO implements IStripeDiscountDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeDiscountDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeDiscount u";
	//private String defaultJPQLQuery = "select u from TaStripeDiscount u where u.taArticle is null";
	
	public StripeDiscountDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeDiscount)
	 */
	public void persist(TaStripeDiscount transientInstance) {
		logger.debug("persisting TaStripeDiscount instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeDiscount)
	 */
	public void remove(TaStripeDiscount persistentInstance) {
		logger.debug("removing TaStripeDiscount instance");
		try {
			TaStripeDiscount e = entityManager.merge(findById(persistentInstance.getIdStripeDiscount()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeDiscount)
	 */
	public TaStripeDiscount merge(TaStripeDiscount detachedInstance) {
		logger.debug("merging TaStripeDiscount instance");
		try {
			TaStripeDiscount result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeDiscount findByCode(String idExterne) {
		logger.debug("getting TaStripeDiscount instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeDiscount f where f.idExterne='"+idExterne+"' ");
				TaStripeDiscount instance = (TaStripeDiscount)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeDiscount findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeDiscount instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeDiscount f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeDiscount instance = (TaStripeDiscount)query.getSingleResult();
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
	public TaStripeDiscount findById(int id) {
		logger.debug("getting TaStripeDiscount instance with id: " + id);
		try {
			TaStripeDiscount instance = entityManager.find(TaStripeDiscount.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeDiscount> selectAll() {
		logger.debug("selectAll TaStripeDiscount");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeDiscount> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeDiscount refresh(TaStripeDiscount detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeDiscount.class, detachedInstance.getIdStripeDiscount());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeDiscount> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeDiscount instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeDiscount.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeDiscount> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeDiscountDAO,TaStripeDiscount> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeDiscountDAO,TaStripeDiscount>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeDiscountDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeDiscount.QN.FIND_ALL_LIGHT);
//			List<TaStripeDiscountDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeDiscountDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeDiscountDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeDiscount.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeDiscount.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeDiscountDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeDiscount entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeDiscount> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeDiscount> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeDiscount> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeDiscount> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeDiscount value) throws Exception {
		BeanValidator<TaStripeDiscount> validator = new BeanValidator<TaStripeDiscount>(TaStripeDiscount.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeDiscount value, String propertyName) throws Exception {
		BeanValidator<TaStripeDiscount> validator = new BeanValidator<TaStripeDiscount>(TaStripeDiscount.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeDiscount transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
