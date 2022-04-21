package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeProductDTO;
import fr.legrain.abonnement.stripe.model.TaStripeProduct;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeProductDAO implements IStripeProductDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeProductDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeProduct u";
	//private String defaultJPQLQuery = "select u from TaStripeProduct u where u.taArticle is null";
	
	public StripeProductDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeProduct)
	 */
	public void persist(TaStripeProduct transientInstance) {
		logger.debug("persisting TaStripeProduct instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeProduct)
	 */
	public void remove(TaStripeProduct persistentInstance) {
		logger.debug("removing TaStripeProduct instance");
		try {
			TaStripeProduct e = entityManager.merge(findById(persistentInstance.getIdStripeProduct()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeProduct)
	 */
	public TaStripeProduct merge(TaStripeProduct detachedInstance) {
		logger.debug("merging TaStripeProduct instance");
		try {
			TaStripeProduct result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeProduct findByCode(String idExterne) {
		logger.debug("getting TaStripeProduct instance with code: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeProduct f where f.idExterne='"+idExterne+"' ");
				TaStripeProduct instance = (TaStripeProduct)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeProduct findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeProduct instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeProduct f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
				TaStripeProduct instance = (TaStripeProduct)query.getSingleResult();
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
	public TaStripeProduct findById(int id) {
		logger.debug("getting TaStripeProduct instance with id: " + id);
		try {
			TaStripeProduct instance = entityManager.find(TaStripeProduct.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeProduct rechercherProduct(TaArticle article) {
		logger.debug("getting TaStripeProduct instance with code article: " + article.getCodeArticle());
		try {
			if(!article.getCodeArticle().equals("")){
				Query query = entityManager.createQuery("select f from TaStripeProduct f where f.taArticle.codeArticle='"+article.getCodeArticle()+"'");
				TaStripeProduct instance = (TaStripeProduct)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripeProductDTO> selectAllDTOAvecPlan() {
		logger.debug("getting Liste TaStripeProductDTO instance with plan: ");
		try {
			String jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeProductDTO("
					+ " f.id, f.active, f.caption, f.description, f.idExterne, f.productType, art.codeArticle, art.idArticle, art.libellecArticle ) "
					+ " from TaStripeProduct f join f.taArticle art where EXISTS "
					+ " ( select plan from TaStripePlan plan where plan.taStripeProduct = f)";
				Query query = entityManager.createQuery(jpql);
				List<TaStripeProductDTO> instance = (List<TaStripeProductDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/**
	 * @author yann
	 * @return une liste de TaStripeProductDTO mais qui correspond en fait a des articles
	 * Requete à déplacer dans TaArticleDAO pour qu'elle remonte directement des article
	 */
	public List<TaStripeProductDTO> selectAllDTOAvecPlanByIdFrequence(Integer idFrequence){
		logger.debug("getting Liste TaStripeProductDTO instance with plan: ");
		try {
			String jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeProductDTO("
					+ " f.id, f.active, f.caption, f.description, f.idExterne, f.productType, art.codeArticle, art.idArticle, art.libellecArticle ) "
					+ " from TaStripeProduct f join f.taArticle art where EXISTS "
					+ " ( select plan from TaStripePlan plan where plan.taStripeProduct = f and plan.taFrequence.idFrequence = :idFrequence)";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("idFrequence", idFrequence);
				List<TaStripeProductDTO> instance = (List<TaStripeProductDTO>)query.getResultList();
				logger.debug("get successful");
				return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	
	public TaStripeProduct rechercherProduct(String idExterne) {
		logger.debug("getting TaStripeProduct instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeProduct f where f.idExterne='"+idExterne+"'");
				TaStripeProduct instance = (TaStripeProduct)query.getSingleResult();
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
	public List<TaStripeProduct> selectAll() {
		logger.debug("selectAll TaStripeProduct");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeProduct> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeProduct refresh(TaStripeProduct detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeProduct.class, detachedInstance.getIdStripeProduct());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeProduct> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeProduct instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeProduct.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeProduct> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeProductDAO,TaStripeProduct> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeProductDAO,TaStripeProduct>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeProductDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeProduct.QN.FIND_ALL_LIGHT);
//			List<TaStripeProductDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeProductDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeProductDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeProduct.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeProduct.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeProductDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeProduct entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeProduct> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeProduct> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeProduct> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeProduct> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeProduct value) throws Exception {
		BeanValidator<TaStripeProduct> validator = new BeanValidator<TaStripeProduct>(TaStripeProduct.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeProduct value, String propertyName) throws Exception {
		BeanValidator<TaStripeProduct> validator = new BeanValidator<TaStripeProduct>(TaStripeProduct.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeProduct transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
