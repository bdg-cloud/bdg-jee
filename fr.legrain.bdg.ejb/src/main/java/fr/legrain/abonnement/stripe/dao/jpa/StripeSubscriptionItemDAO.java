//package fr.legrain.abonnement.stripe.dao.jpa;
//
//
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.apache.log4j.Logger;
//
//import fr.legrain.abonnement.stripe.dao.IStripeSubscriptionItemDAO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem_old;
//import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
//import fr.legrain.lib.data.ExceptLgr;
//import fr.legrain.validator.BeanValidator;
//
//public class StripeSubscriptionItemDAO implements IStripeSubscriptionItemDAO, Serializable {
//
//	private static final long serialVersionUID = -7259541112640013294L;
//	
//	
//	static Logger logger = Logger.getLogger(StripeSubscriptionItemDAO.class.getName());
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//
//		private String defaultJPQLQuery = "select u from TaStripeSubscriptionItem_old u";
//	//private String defaultJPQLQuery = "select u from TaStripeSubscriptionItem_old u where u.taArticle is null";
//	
//	public StripeSubscriptionItemDAO(){
//	}
//
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeSubscriptionItem_old)
//	 */
//	public void persist(TaStripeSubscriptionItem_old transientInstance) {
//		logger.debug("persisting TaStripeSubscriptionItem_old instance");
//		try {
//			entityManager.persist(transientInstance);
//			logger.debug("persist successful");
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("persist failed", re);
//			throw re2;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeSubscriptionItem_old)
//	 */
//	public void remove(TaStripeSubscriptionItem_old persistentInstance) {
//		logger.debug("removing TaStripeSubscriptionItem_old instance");
//		try {
//			TaStripeSubscriptionItem_old e = entityManager.merge(findById(persistentInstance.getIdStripeSubscriptionItem()));
//			entityManager.remove(e);
//			logger.debug("remove successful");
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("remove failed", re);
//			throw re2;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeSubscriptionItem_old)
//	 */
//	public TaStripeSubscriptionItem_old merge(TaStripeSubscriptionItem_old detachedInstance) {
//		logger.debug("merging TaStripeSubscriptionItem_old instance");
//		try {
//			TaStripeSubscriptionItem_old result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("merge failed", re);
//			throw re2;
//		}
//	}
//	
//	public TaStripeSubscriptionItem_old findByCode(String idExterne) {
//		logger.debug("getting TaStripeSubscriptionItem_old instance with code: " + idExterne);
//		try {
//			if(!idExterne.equals("")){
//				Query query = entityManager.createQuery("select f from TaStripeSubscriptionItem_old f where f.idExterne='"+idExterne+"' ");
//				TaStripeSubscriptionItem_old instance = (TaStripeSubscriptionItem_old)query.getSingleResult();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//	
//	public List<TaStripeSubscriptionItemDTO> rechercherSubscriptionItemDTO(int idStripeSubscription) {
//		logger.debug("getting TaStripeSubscriptionItem_old instance with idStripeSubscription: " + idStripeSubscription);
//		try {
//				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO("
//						+ "f.id, f.idExterne, f.taPlan.idStripePlan, f.taPlan.idExterne," 
//						+ " f.quantity, f.taStripeSubscription.idStripeSubscription, f.taStripeSubscription.idExterne, f.taPlan.nickname, f.taPlan.interval," 
//						+ " f.taPlan.currency, f.taPlan.active, f.taPlan.amount, f.taPlan.taArticle.codeArticle, f.taPlan.taArticle.libellecArticle," 
//						+ " f.complement1, f.complement2, f.complement3, f.versionObj"
//						+ " ) "
//						+ " from TaStripeSubscriptionItem_old f where f.taStripeSubscription.idStripeSubscription="+idStripeSubscription+" ");
//				List<TaStripeSubscriptionItemDTO> instance = (List<TaStripeSubscriptionItemDTO>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//
//	public List<TaStripeSubscriptionItemDTO> rechercherSubscriptionItemDTO(String idExterneSubscription) {
//		logger.debug("getting TaStripeSubscriptionItem_old instance with idExterneSubscription: " + idExterneSubscription);
//		try {
//			if(!idExterneSubscription.equals("")){
//				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionItemDTO("
//						+ "f.id, f.idExterne, f.taPlan.idStripePlan, f.taPlan.idExterne," 
//						+ " f.quantity, f.taStripeSubscription.idStripeSubscription, f.taStripeSubscription.idExterne, f.taPlan.nickname, f.taPlan.interval," 
//						+ " f.taPlan.currency, f.taPlan.active, f.taPlan.amount, f.taPlan.taArticle.codeArticle, f.taPlan.taArticle.libellecArticle," 
//						+ " f.versionObj"
//						+ " ) "
//						+ " from TaStripeSubscriptionItem_old f where f.taStripeSubscription.idExterne='"+idExterneSubscription+"' ");
//				List<TaStripeSubscriptionItemDTO> instance = (List<TaStripeSubscriptionItemDTO>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//	
//	public List<TaStripeSubscriptionItem_old> rechercherSubscriptionItem(String idExterneSubscription) {
//		logger.debug("getting TaStripeSubscriptionItem_old instance with idExterneSubscription: " + idExterneSubscription);
//		try {
//			if(!idExterneSubscription.equals("")){
//				Query query = entityManager.createQuery("select f from TaStripeSubscriptionItem_old f where f.taStripeSubscription.idExterne='"+idExterneSubscription+"' ");
//				List<TaStripeSubscriptionItem_old> instance = (List<TaStripeSubscriptionItem_old>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//	
//	public TaStripeSubscriptionItem_old findByCode(String code, String codeArticle) {
//		logger.debug("getting TaStripeSubscriptionItem_old instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaStripeSubscriptionItem_old f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaStripeSubscriptionItem_old instance = (TaStripeSubscriptionItem_old)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
//	 */
//	public TaStripeSubscriptionItem_old findById(int id) {
//		logger.debug("getting TaStripeSubscriptionItem_old instance with id: " + id);
//		try {
//			TaStripeSubscriptionItem_old instance = entityManager.find(TaStripeSubscriptionItem_old.class, id);
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//	
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
//	 */
//	public List<TaStripeSubscriptionItem_old> selectAll() {
//		logger.debug("selectAll TaStripeSubscriptionItem_old");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
////			System.out.println(defaultJPQLQuery);
////			System.out.println(ejbQuery.getResultList().toString());
////			System.out.println(ejbQuery.toString());
//			List<TaStripeSubscriptionItem_old> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
//	public TaStripeSubscriptionItem_old refresh(TaStripeSubscriptionItem_old detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaStripeSubscriptionItem_old.class, detachedInstance.getIdStripeSubscriptionItem());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
//	
//	
//	/*
//	public List<TaStripeSubscriptionItem_old> findByArticle(String codeArticle) {
//		logger.debug("getting TaStripeSubscriptionItem_old instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeSubscriptionItem_old.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaStripeSubscriptionItem_old> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}*/
//	
//	//ejb
////	public ModelGeneralObjet<SWTUnite,TaStripeSubscriptionItemDAO,TaStripeSubscriptionItem_old> modelObjetUniteArticle(String codeArticle) {
////		return new ModelGeneralObjet<SWTUnite,TaStripeSubscriptionItemDAO,TaStripeSubscriptionItem_old>(findByArticle(codeArticle),SWTUnite.class,entityManager);
////	}
//	
//	public List<TaStripeSubscriptionItemDTO> findAllLight() {
////		try {
////			Query query = entityManager.createNamedQuery(TaStripeSubscriptionItem_old.QN.FIND_ALL_LIGHT);
////			List<TaStripeSubscriptionItemDTO> l = query.getResultList();;
////			logger.debug("get successful");
////			return l;
////
////		} catch (RuntimeException re) {
////			logger.error("get failed", re);
////			throw re;
////		}
//		return null;
//	}
//	
//	public List<TaStripeSubscriptionItemDTO> findByCodeLight(String code) {
////		logger.debug("getting TaStripeSubscriptionItemDTO instance");
////		try {
////			Query query = null;
////			if(code!=null && !code.equals("") && !code.equals("*")) {
////				query = entityManager.createNamedQuery(TaStripeSubscriptionItem_old.QN.FIND_BY_CODE_LIGHT);
////				query.setParameter(1, "%"+code+"%");
////			} else {
////				query = entityManager.createNamedQuery(TaStripeSubscriptionItem_old.QN.FIND_ALL_LIGHT);
////			}
////
////			List<TaStripeSubscriptionItemDTO> l = query.getResultList();
////			logger.debug("get successful");
////			return l;
////
////		} catch (RuntimeException re) {
////			logger.error("get failed", re);
////			throw re;
////		}
//		return null;
//	}
//	
//	public void ctrlSaisieSpecifique(TaStripeSubscriptionItem_old entity,String field) throws ExceptLgr {	
//		try {		
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	
//
//
//	@Override
//	public List<TaStripeSubscriptionItem_old> findWithNamedQuery(String queryName) {
//		try {
//			Query ejbQuery = entityManager.createNamedQuery(queryName);
//			List<TaStripeSubscriptionItem_old> l = ejbQuery.getResultList();
//			System.out.println("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			System.out.println("selectAll failed");
//			re.printStackTrace();
//			throw re;
//		}
//	}
//
//	@Override
//	public List<TaStripeSubscriptionItem_old> findWithJPQLQuery(String JPQLquery) {
//		try {
//			Query ejbQuery = entityManager.createQuery(JPQLquery);
//			List<TaStripeSubscriptionItem_old> l = ejbQuery.getResultList();
//			System.out.println("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			System.out.println("selectAll failed");
//			re.printStackTrace();
//			throw re;
//		}
//	}
//
//	@Override
//	public boolean validate(TaStripeSubscriptionItem_old value) throws Exception {
//		BeanValidator<TaStripeSubscriptionItem_old> validator = new BeanValidator<TaStripeSubscriptionItem_old>(TaStripeSubscriptionItem_old.class);
//		return validator.validate(value);
//	}
//
//	@Override
//	public boolean validateField(TaStripeSubscriptionItem_old value, String propertyName) throws Exception {
//		BeanValidator<TaStripeSubscriptionItem_old> validator = new BeanValidator<TaStripeSubscriptionItem_old>(TaStripeSubscriptionItem_old.class);
//		return validator.validateField(value,propertyName);
//	}
//
//	@Override
//	public void detach(TaStripeSubscriptionItem_old transientInstance) {
//		entityManager.detach(transientInstance);
//	}
//	
//}
