package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeInvoiceItemDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripeInvoiceItemDAO implements IStripeInvoiceItemDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeInvoiceItemDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeInvoiceItem u";
	//private String defaultJPQLQuery = "select u from TaStripeInvoiceItem u where u.taArticle is null";
	
	public StripeInvoiceItemDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeInvoiceItem)
	 */
	public void persist(TaStripeInvoiceItem transientInstance) {
		logger.debug("persisting TaStripeInvoiceItem instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeInvoiceItem)
	 */
	public void remove(TaStripeInvoiceItem persistentInstance) {
		logger.debug("removing TaStripeInvoiceItem instance");
		try {
			TaStripeInvoiceItem e = entityManager.merge(findById(persistentInstance.getIdStripeInvoiceItem()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeInvoiceItem)
	 */
	public TaStripeInvoiceItem merge(TaStripeInvoiceItem detachedInstance) {
		logger.debug("merging TaStripeInvoiceItem instance");
		try {
			TaStripeInvoiceItem result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeInvoiceItem findByCode(String idExterne) {
		logger.debug("getting TaStripeInvoiceItem instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeInvoiceItem f where f.codeEntrepot='"+idExterne+"' ");
				TaStripeInvoiceItem instance = (TaStripeInvoiceItem)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeInvoiceItem findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeInvoiceItem instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeInvoiceItem f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeInvoiceItem instance = (TaStripeInvoiceItem)query.getSingleResult();
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
	public TaStripeInvoiceItem findById(int id) {
		logger.debug("getting TaStripeInvoiceItem instance with id: " + id);
		try {
			TaStripeInvoiceItem instance = entityManager.find(TaStripeInvoiceItem.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripeInvoiceItem> selectAll() {
		logger.debug("selectAll TaStripeInvoiceItem");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeInvoiceItem> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeInvoiceItem refresh(TaStripeInvoiceItem detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeInvoiceItem.class, detachedInstance.getIdStripeInvoiceItem());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	public List<TaStripeInvoiceItemDTO> rechercherInvoiceItemDTO(String idExterneInvoice) {
		logger.debug("getting TaStripeInvoiceItem instance with idExterneInvoice: " + idExterneInvoice);
//		try {
//			if(!idExterneInvoice.equals("")){
////				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO("
////						+ " f.id, f.idExterne, f.taStripeInvoice.idStripeInvoice, f.taStripeInvoice.idExterne, f.versionObj) "
////						+ " from TaStripeSubscription f where f.taStripeInvoice.idExterne='"+idExterneInvoice+"' ");
//				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeInvoiceItemDTO("
//						+ " f.idDocument, f.idExterne, f.taStripeInvoice.idStripeInvoice, f.taStripeInvoice.idExterne, f.versionObj) "
//						+ " from TaAbonnement f where f.taStripeInvoice.idExterne='"+idExterneInvoice+"' ");
//				List<TaStripeInvoiceItemDTO> instance = (List<TaStripeInvoiceItemDTO>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
		return null;
	}
	
	public List<TaStripeInvoiceItem> rechercherInvoiceItem(String idExterneInvoice) {
		logger.debug("getting TaStripeInvoiceItem instance with idExterneInvoice: " + idExterneInvoice);
		try {
			if(!idExterneInvoice.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeInvoiceItem f where f.taStripeInvoice.idExterne='"+idExterneInvoice+"' ");
				List<TaStripeInvoiceItem> instance = (List<TaStripeInvoiceItem>)query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	
	/*
	public List<TaStripeInvoiceItem> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeInvoiceItem instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeInvoiceItem.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeInvoiceItem> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeInvoiceItemDAO,TaStripeInvoiceItem> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeInvoiceItemDAO,TaStripeInvoiceItem>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeInvoiceItemDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeInvoiceItem.QN.FIND_ALL_LIGHT);
//			List<TaStripeInvoiceItemDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeInvoiceItemDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeInvoiceItemDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeInvoiceItem.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeInvoiceItem.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeInvoiceItemDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeInvoiceItem entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeInvoiceItem> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeInvoiceItem> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeInvoiceItem> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeInvoiceItem> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeInvoiceItem value) throws Exception {
		BeanValidator<TaStripeInvoiceItem> validator = new BeanValidator<TaStripeInvoiceItem>(TaStripeInvoiceItem.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeInvoiceItem value, String propertyName) throws Exception {
		BeanValidator<TaStripeInvoiceItem> validator = new BeanValidator<TaStripeInvoiceItem>(TaStripeInvoiceItem.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeInvoiceItem transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
