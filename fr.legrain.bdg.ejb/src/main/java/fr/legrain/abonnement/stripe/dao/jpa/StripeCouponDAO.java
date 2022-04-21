package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeCouponDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeCouponDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCoupon;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeCouponDAO implements IStripeCouponDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeCouponDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeCoupon u";
	//private String defaultJPQLQuery = "select u from TaStripeCoupon u where u.taArticle is null";
	
	public StripeCouponDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeCoupon)
	 */
	public void persist(TaStripeCoupon transientInstance) {
		logger.debug("persisting TaStripeCoupon instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeCoupon)
	 */
	public void remove(TaStripeCoupon persistentInstance) {
		logger.debug("removing TaStripeCoupon instance");
		try {
			TaStripeCoupon e = entityManager.merge(findById(persistentInstance.getIdStripeCoupon()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeCoupon)
	 */
	public TaStripeCoupon merge(TaStripeCoupon detachedInstance) {
		logger.debug("merging TaStripeCoupon instance");
		try {
			TaStripeCoupon result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeCoupon findByCode(String idExterne) {
		logger.debug("getting TaStripeCoupon instance with code: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeCoupon f where f.idExterne='"+idExterne+"' ");
				TaStripeCoupon instance = (TaStripeCoupon)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeCoupon findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeCoupon instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeCoupon f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeCoupon instance = (TaStripeCoupon)query.getSingleResult();
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
	public TaStripeCoupon findById(int id) {
		logger.debug("getting TaStripeCoupon instance with id: " + id);
		try {
			TaStripeCoupon instance = entityManager.find(TaStripeCoupon.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeCoupon> selectAll() {
		logger.debug("selectAll TaStripeCoupon");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeCoupon> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeCoupon refresh(TaStripeCoupon detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeCoupon.class, detachedInstance.getIdStripeCoupon());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeCoupon> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeCoupon instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeCoupon.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeCoupon> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeCouponDAO,TaStripeCoupon> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeCouponDAO,TaStripeCoupon>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeCouponDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeCoupon.QN.FIND_ALL_LIGHT);
//			List<TaStripeCouponDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeCouponDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeCouponDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeCoupon.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeCoupon.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeCouponDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeCoupon entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeCoupon> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeCoupon> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeCoupon> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeCoupon> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeCoupon value) throws Exception {
		BeanValidator<TaStripeCoupon> validator = new BeanValidator<TaStripeCoupon>(TaStripeCoupon.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeCoupon value, String propertyName) throws Exception {
		BeanValidator<TaStripeCoupon> validator = new BeanValidator<TaStripeCoupon>(TaStripeCoupon.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeCoupon transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
