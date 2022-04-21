package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripePlanDAO;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.abonnement.stripe.model.TaStripePlan;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscriptionItem;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

public class StripePlanDAO implements IStripePlanDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripePlanDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripePlan u";
	//private String defaultJPQLQuery = "select u from TaStripePlan u where u.taArticle is null";
	
	public StripePlanDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripePlan)
	 */
	public void persist(TaStripePlan transientInstance) {
		logger.debug("persisting TaStripePlan instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripePlan)
	 */
	public void remove(TaStripePlan persistentInstance) {
		logger.debug("removing TaStripePlan instance");
		try {
			TaStripePlan e = entityManager.merge(findById(persistentInstance.getIdStripePlan()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripePlan)
	 */
	public TaStripePlan merge(TaStripePlan detachedInstance) {
		logger.debug("merging TaStripePlan instance");
		try {
			TaStripePlan result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripePlan findByNickname(String nickname) {
		logger.debug("getting TaStripePlan instance with code: " + nickname);
		try {
			if(!nickname.equals("")){
				Query query = entityManager.createQuery("select f from TaStripePlan f where f.nickname='"+nickname+"' ");
				TaStripePlan instance = (TaStripePlan)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripePlan findByCode(String idExterne) {
		logger.debug("getting TaStripePlan instance with code: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripePlan f where f.idExterne='"+idExterne+"' ");
				TaStripePlan instance = (TaStripePlan)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
//	public TaStripePlan findByStripeSubscriptionItem(TaStripeSubscriptionItem taStripeSubscriptionItem) {
//		logger.debug("getting TaStripePlan instance with taStripeSubscriptionItem: " + taStripeSubscriptionItem.getIdExterne());
//		try {
//			Query query = entityManager.createQuery("select f.taPlan from TaStripeSubscriptionItem f join fetch f.taPlan.taArticle a where f.idStripeSubscriptionItem="+taStripeSubscriptionItem.getIdStripeSubscriptionItem()+" ");
//			TaStripePlan instance = (TaStripePlan)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
	
	public TaStripePlan findByLigneAbo(TaLAbonnement ligneAbo) {
		logger.debug("getting TaStripePlan instance with TaLAbonnement: ");
		try {
//			Query query = entityManager.createQuery("select f.taPlan from TaLAbonnement f join fetch f.taPlan.taArticle a where f.idLDocument= :idLigneAbo ");
			Query query = entityManager.createQuery("select f.taPlan from TaLAbonnement f where f.idLDocument= :idLigneAbo ");
			query.setParameter("idLigneAbo", ligneAbo.getIdLDocument());
			TaStripePlan instance = (TaStripePlan)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripePlan findByIdLigneAbo(Integer idLigne) {
		logger.debug("getting TaStripePlan instance with idLigne: ");
		try {
			Query query = entityManager.createQuery("select plan from TaLAbonnement f join  f.taPlan plan where f.idLDocument= :idLigne ");
			query.setParameter("idLigne", idLigne);
			TaStripePlan instance = (TaStripePlan)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripePlan findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripePlan instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripePlan f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripePlan instance = (TaStripePlan)query.getSingleResult();
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
	public TaStripePlan findById(int id) {
		logger.debug("getting TaStripePlan instance with id: " + id);
		try {
			TaStripePlan instance = entityManager.find(TaStripePlan.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaStripePlan> selectAll() {
		logger.debug("selectAll TaStripePlan");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripePlan> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripePlan refresh(TaStripePlan detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripePlan.class, detachedInstance.getIdStripePlan());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripePlan> findByArticle(String codeArticle) {
		logger.debug("getting TaStripePlan instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripePlan.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripePlan> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripePlanDAO,TaStripePlan> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripePlanDAO,TaStripePlan>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripePlanDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripePlan.QN.FIND_ALL_LIGHT);
//			List<TaStripePlanDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripePlanDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripePlanDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripePlan.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripePlan.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripePlanDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripePlan> findByIdProduct(int idStripePtoduct) {
		try {
			Query query = null;
			
			query = entityManager.createQuery("select u from TaStripePlan u where taStripeProduct.id = :idStripePtoduct");
			query.setParameter("idStripePtoduct", idStripePtoduct);
			
	
			List<TaStripePlan> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaStripePlanDTO> findByIdArticleDTO(int idArticle){
		try {
			Query query = null;
			
			query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripePlanDTO(u.id, u.idExterne, u.taArticle.idArticle, u.taArticle.codeArticle,"
					+ "sp.id, sp.idExterne, u.nickname, u.interval, u.intervalCount, u.currency, u.active, "
					+ "u.trialPrediodDays, u.amount, u.versionObj, u.taFrequence.idFrequence, u.taFrequence.codeFrequence, u.taFrequence.liblFrequence, u.nbMois) "
					+ "   from TaStripePlan u left join  u.taStripeProduct sp where u.taArticle.idArticle = :idArticle");
			
			query.setParameter("idArticle", idArticle);
	
			List<TaStripePlanDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaStripePlanDTO> findByIdProductDTO(int idStripePtoduct) {
		try {
			Query query = null;
			
			query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripePlanDTO(u.id, u.idExterne, u.taArticle.idArticle, u.taArticle.codeArticle,"
					+ "u.taStripeProduct.id, u.taStripeProduct.idExterne, u.nickname, u.interval, u.intervalCount, u.currency, u.active, "
					+ "u.trialPrediodDays, u.amount, u.versionObj, u.taFrequence.idFrequence, u.taFrequence.codeFrequence, u.taFrequence.liblFrequence)    from TaStripePlan u where taStripeProduct.id = :idStripePtoduct");
			
			query.setParameter("idStripePtoduct", idStripePtoduct);
	
			List<TaStripePlanDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaStripePlanDTO> findByIdProductDTOAndByIdFrequence(Integer idStripePtoduct, Integer idFrequence){
		try {
			Query query = null;
			
			query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripePlanDTO(u.id, u.idExterne, u.taArticle.idArticle, u.taArticle.codeArticle,"
					+ "u.taStripeProduct.id, u.taStripeProduct.idExterne, u.nickname, u.interval, u.intervalCount, u.currency, u.active, "
					+ "u.trialPrediodDays, u.amount, u.versionObj, u.taFrequence.idFrequence, u.taFrequence.codeFrequence, u.taFrequence.liblFrequence)"
					+ "    from TaStripePlan u where taStripeProduct.id = :idStripePtoduct and u.taFrequence.idFrequence = :idFrequence");
			
			query.setParameter("idStripePtoduct", idStripePtoduct);
			query.setParameter("idFrequence", idFrequence);
	
			List<TaStripePlanDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaStripePlanDTO> findByIdArticleDTOAndByIdFrequence(Integer idArticle, Integer idFrequence){
		try {
			Query query = null;
			
			query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripePlanDTO(u.id, u.idExterne, u.taArticle.idArticle, u.taArticle.codeArticle,"
					+ "sp.id, sp.idExterne, u.nickname, u.interval, u.intervalCount, u.currency, u.active, "
					+ "u.trialPrediodDays, u.amount, u.versionObj, u.taFrequence.idFrequence, u.taFrequence.codeFrequence, u.taFrequence.liblFrequence)"
					+ "    from TaStripePlan u left join u.taStripeProduct sp where u.taArticle.idArticle = :idArticle and u.taFrequence.idFrequence = :idFrequence");
			
			query.setParameter("idArticle", idArticle);
			query.setParameter("idFrequence", idFrequence);
	
			List<TaStripePlanDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaStripePlanDTO> findByIdArticleDTOAndByNbMois(Integer idArticle, Integer nbMois){
		try {
			Query query = null;
			
			query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripePlanDTO(u.id, u.idExterne, u.taArticle.idArticle, u.taArticle.codeArticle,"
					+ "sp.id, sp.idExterne, u.nickname, u.interval, u.intervalCount, u.currency, u.active, "
					+ "u.trialPrediodDays, u.amount, u.versionObj, u.taFrequence.idFrequence, u.taFrequence.codeFrequence, u.taFrequence.liblFrequence, u.nbMois)"
					+ "    from TaStripePlan u left join u.taStripeProduct sp where u.taArticle.idArticle = :idArticle and u.nbMois = :nbMois");
			
			query.setParameter("idArticle", idArticle);
			query.setParameter("nbMois", nbMois);
	
			List<TaStripePlanDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaStripePlan entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripePlan> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripePlan> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripePlan> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripePlan> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripePlan value) throws Exception {
		BeanValidator<TaStripePlan> validator = new BeanValidator<TaStripePlan>(TaStripePlan.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripePlan value, String propertyName) throws Exception {
		BeanValidator<TaStripePlan> validator = new BeanValidator<TaStripePlan>(TaStripePlan.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripePlan transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
