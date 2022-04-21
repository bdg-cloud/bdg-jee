//package fr.legrain.abonnement.stripe.dao.jpa;
//
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.apache.log4j.Logger;
//
//import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
//import fr.legrain.abonnement.stripe.dao.IStripeSubscriptionDAO;
//import fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO;
//import fr.legrain.abonnement.stripe.model.TaStripePlan;
//import fr.legrain.abonnement.stripe.model.TaStripeSubscription_old;
//import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
//import fr.legrain.lib.data.ExceptLgr;
//import fr.legrain.validator.BeanValidator;
//
//public class StripeSubscriptionDAO implements IStripeSubscriptionDAO, Serializable {
//
//	private static final long serialVersionUID = -7259541112640013294L;
//	
//	
//	static Logger logger = Logger.getLogger(StripeSubscriptionDAO.class.getName());
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//
//		private String defaultJPQLQuery = "select u from TaStripeSubscription_old u";
//	//private String defaultJPQLQuery = "select u from TaStripeSubscription_old u where u.taArticle is null";
//	
//	public StripeSubscriptionDAO(){
//	}
//
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeSubscription_old)
//	 */
//	public void persist(TaStripeSubscription_old transientInstance) {
//		logger.debug("persisting TaStripeSubscription_old instance");
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeSubscription_old)
//	 */
//	public void remove(TaStripeSubscription_old persistentInstance) {
//		logger.debug("removing TaStripeSubscription_old instance");
//		try {
//			TaStripeSubscription_old e = entityManager.merge(findById(persistentInstance.getIdStripeSubscription()));
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeSubscription_old)
//	 */
//	public TaStripeSubscription_old merge(TaStripeSubscription_old detachedInstance) {
//		logger.debug("merging TaStripeSubscription_old instance");
//		try {
//			TaStripeSubscription_old result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("merge failed", re);
//			throw re2;
//		}
//	}
//	
//	public TaStripeSubscription_old findByCode(String idExterne) {
//		logger.debug("getting TaStripeSubscription_old instance with idExterne: " + idExterne);
//		try {
//			if(!idExterne.equals("")){
//				Query query = entityManager.createQuery("select f from TaStripeSubscription_old f where f.idExterne='"+idExterne+"' ");
//				TaStripeSubscription_old instance = (TaStripeSubscription_old)query.getSingleResult();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//	
//	public List<TaStripeSubscriptionDTO> rechercherSubscriptionNonStripeCustomerDTO(String idExterneCustomer) {
//		return rechercherSubscriptionCustomerDTO(idExterneCustomer, false);
//	}
//	
//	public List<TaStripeSubscriptionDTO> rechercherSubscriptionCustomerDTO(String idExterneCustomer) {
//		return rechercherSubscriptionCustomerDTO(idExterneCustomer, true);
//	}
//	//rajout yann
//	public TaStripeSubscriptionDTO findDTOByIdAbonnement(Integer idAbonnement) {
//		logger.debug("getting TaStripeSubscription_old instance with idAbonnement: " + idAbonnement);
//		try {
//			String jpql = null;
//		jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO("
//				+ " f.id, f.idExterne, f.codeDocument, sc.idStripeCustomer, sc.idExterne, tiers.codeTiers, tiers.nomTiers, "
//				+ " f.quantity, f.billing, f.status, f.dateDebut, f.dateFin, f.dateAnnulation, f.commentaireAnnulation, f.nbEcheance, f.versionObj) "
//				+ " from TaStripeSubscription_old f  left join f.taStripeCustomer sc left join f.taAbonnement abo left join abo.taTiers tiers"
//				+ " where f.taAbonnement.idDocument = :idAbonnement";
//		Query query = entityManager.createQuery(jpql);
//		query.setParameter("idAbonnement", idAbonnement);
//		TaStripeSubscriptionDTO instance = (TaStripeSubscriptionDTO)query.getSingleResult();
//		logger.debug("get successful");
//		return instance;
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
//	}
//	public List<TaStripeSubscriptionDTO> findAllDTOByIdTiers(Integer idTiers) {
//		logger.debug("getting TaStripeSubscription_old instance with idTiers: " + idTiers);
//		try {
//			String jpql = null;
//		jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO("
//				+ " f.id, f.idExterne, f.codeDocument, sc.idStripeCustomer, sc.idExterne, tiers.codeTiers, tiers.nomTiers, "
//				+ " f.quantity, f.billing, f.status, f.dateDebut, f.dateFin, f.dateAnnulation, f.commentaireAnnulation, f.nbEcheance, f.versionObj) "
//				+ " from TaStripeSubscription_old f  left join f.taStripeCustomer sc left join f.taAbonnement abo left join abo.taTiers tiers"
//				+ " where f.taAbonnement.taTiers.idTiers = :idTiers";
//		Query query = entityManager.createQuery(jpql);
//		query.setParameter("idTiers", idTiers);
//		List<TaStripeSubscriptionDTO> instance = (List<TaStripeSubscriptionDTO>)query.getResultList();
//		logger.debug("get successful");
//		return instance;
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
//	}
//	
//	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers) {
//		logger.debug("getting TaStripeSubscription_old instance with idTiers: " + idTiers);
//		try {
//			String jpql2 = null;
//			String jpql = null;
//		jpql = "select new fr.legrain.abonnement.dto.TaAbonnementFullDTO("
//				+ " f.idDocument, f.idExterne, f.codeDocument,"
////				sc.idStripeCustomer, sc.idExterne,
//				+ " tiers.codeTiers, tiers.nomTiers, tiers.prenomTiers, "
//				//f.quantity, f.status,f.billing
//				+ " f.dateDebut, f.dateFin, f.dateAnnulation,"
//				+ " f.commentaireAnnulation,"
//				+ " abo.dateSuspension, abo.suspension, freq.idFrequence, freq.codeFrequence,"
//				+ " freq.liblFrequence, plan.nickname,"
////				plan.idStripePlan
//				+ " art.idArticle, art.codeArticle, art.libellecArticle,"
//			    + " ligneAbo.u1LDocument, ligneAbo.u2LDocument, "
//				+ " ligneAbo.complement1,  ligneAbo.complement2,  ligneAbo.complement3"
//				+ " ) "
//				+ " from TaLAbonnement ligneAbo  "
//				//+ " join ligneAbo.taStripeSubscriptionItem ligneAbo"
//				+ " join ligneAbo.taPlan plan"
//				+ " join ligneAbo.taAbonnement f"
//				+ " join plan.taArticle art "
//				//+ " left join f.taStripeCustomer sc "
//				+ " join ligneAbo.taDocument abo"
//				+ " join abo.taTiers tiers"
//				+ " join abo.taFrequence freq"
//				+ " where tiers.idTiers = :idTiers and f.dateAnnulation = null";
//		jpql2 = "select new fr.legrain.abonnement.dto.TaAbonnementFullDTO("
//                + " plan.amount"
//				+ " ) "
//				+ " from TaLAbonnement ligneAbo  "
//				//+ " join ligneAbo.taStripeSubscriptionItem ligneAbo"
//				+ " join ligneAbo.taPlan plan"
//				+ " join ligneAbo.taAbonnement f"
//				+ " join plan.taArticle art "
//				//+ " left join f.taStripeCustomer sc "
//				+ " join ligneAbo.taDocument abo"
//				+ " join abo.taTiers tiers"
//				+ " join abo.taFrequence freq"
//				+ " where tiers.idTiers = :idTiers and f.dateAnnulation = null";
//		Query query = entityManager.createQuery(jpql);
//		query.setParameter("idTiers", idTiers);
//		List<TaAbonnementFullDTO> instance = (List<TaAbonnementFullDTO>)query.getResultList();
//		logger.debug("get successful");
//		return instance;
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
//	}
//	/**
//	 * @author yann
//	 * @return taStripeSubscription
//	 */
//	public TaStripeSubscription_old findByIdLEcheance(Integer idLEcheance) {
//		logger.debug("getting TaStripeSubscription_old with idLEcheance: " );
//		try {
//			String jpql = null;
//		jpql = "select sub from TaLEcheance ech join ech.taStripeSubscription sub where ech.idLEcheance = :idLEcheance" ;
//		Query query = entityManager.createQuery(jpql);
//		query.setParameter("idLEcheance", idLEcheance);
//		TaStripeSubscription_old instance = (TaStripeSubscription_old)query.getSingleResult();
//		logger.debug("get successful");
//		return instance;
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
//	}
//	
//	
//	
//	
//	
//	/**
//	 * Requête qui remonte tous les abonnements qui ont des lignes d'échéances avec date d'échéance passé ou égale à aujourd'hui
//	 * et qui ne sont pas déjà suspendu 
//	 * et qui ne sont pas annuler
//	 * et dont l'avis d'échéance n'est pas lié a une facture
//	 * Ces abonnements sont donc à suspendre (car ils n'ont pas était payé)
//	 * @author yann
//	 * @return liste de TaStripeSubscriptionDTO (abonnements)
//	 */
//	public List<TaStripeSubscriptionDTO> selectAllASuspendre(){
//		logger.debug("getting TaStripeSubscription_old instance a suspendre: " );
//		Date now = new Date();
//		try {
//			String jpql = null;
//		jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO("
//				+ " f.id, f.idExterne, f.codeDocument, sc.idStripeCustomer, sc.idExterne, tiers.codeTiers, tiers.nomTiers, "
//				+ " f.quantity, f.billing, f.status, f.dateDebut, f.dateFin, f.dateAnnulation, f.commentaireAnnulation, f.nbEcheance, f.versionObj) "
//				+ " from TaLAvisEcheance lav join lav.taDocument av join lav.taLEcheance lech join lech.taStripeSubscription f left join av.taRDocuments rdoc"
//				+ " left join rdoc.taFacture fact  left join f.taStripeCustomer sc left join f.taAbonnement abo left join abo.taTiers tiers"
//				+ " where (lech.dateEcheance < :now or lech.dateEcheance = :now) and abo.suspension != true and f.dateAnnulation = null and fact = null ";
//		Query query = entityManager.createQuery(jpql);
//		query.setParameter("now", now);
//		List<TaStripeSubscriptionDTO> instance = (List<TaStripeSubscriptionDTO>)query.getResultList();
//		logger.debug("get successful");
//		return instance;
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
//	}
//	
//	
//	
//	public List<TaStripeSubscriptionDTO> rechercherSubscriptionCustomerDTO(String idExterneCustomer, Boolean stripe) {
//		logger.debug("getting TaStripeSubscription_old instance with idExterneCustomer: " + idExterneCustomer);
//		try {
//			String jpql = null;
//			if(idExterneCustomer!=null && !idExterneCustomer.equals("")){
//				jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO("
//						+ " f.id, f.idExterne, f.codeDocument, f.taStripeCustomer.idStripeCustomer, f.taStripeCustomer.idExterne, f.taStripeCustomer.taTiers.codeTiers, f.taStripeCustomer.taTiers.nomTiers,"
//						+ " f.quantity, f.billing, f.status, f.dateDebut, f.dateFin, f.dateAnnulation, f.commentaireAnnulation, f.nbEcheance, f.versionObj) "
//						+ " from TaStripeSubscription_old f where f.taStripeCustomer.idExterne='"+idExterneCustomer+"' ";
//				if(stripe!=null) {
//					if(stripe) {
//						jpql += " and f.idExterne is not null";
//					} else {
//						jpql += " and f.idExterne is null";
//					}
//				}
//				
//			} else {
//				jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO("
//						+ " f.id, f.idExterne, f.codeDocument, sc.idStripeCustomer, sc.idExterne, tiers.codeTiers, tiers.nomTiers, "
//						+ " f.quantity, f.billing, f.status, f.dateDebut, f.dateFin, f.dateAnnulation, f.commentaireAnnulation, f.nbEcheance, f.versionObj) "
//						+ " from TaStripeSubscription_old f  left join f.taStripeCustomer sc left join f.taAbonnement abo left join abo.taTiers tiers";
//				if(stripe!=null) {
//					if(stripe) {
//						jpql += " where f.idExterne is not null";
//					} else {
//						jpql += " where f.idExterne is null";
//					}
//				}
//			}
//			
//			Query query = entityManager.createQuery(jpql);
//			List<TaStripeSubscriptionDTO> instance = (List<TaStripeSubscriptionDTO>)query.getResultList();
//			logger.debug("get successful");
//			return instance;
//			
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
//	}
//	public List<TaStripeSubscription_old> rechercherSubscriptionCustomer(String idExterneCustomer) {
//		logger.debug("getting TaStripeSubscription_old instance with idExterneCustomer: " + idExterneCustomer);
//		try {
//			if(!idExterneCustomer.equals("")){
//				Query query = entityManager.createQuery("select f from TaStripeSubscription_old f where f.taStripeCustomer.idExterne='"+idExterneCustomer+"' ");
//				List<TaStripeSubscription_old> instance = (List<TaStripeSubscription_old>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
//	}
//	
////	public TaStripeSubscriptionDTO(Integer id, String idExterne, String codeDocument,
//	//int idStripeCustomer, String idExterneCustomer,		String codeTiers, String nomTiers, 
////	Integer quantity, String billing, String status, Date dateDebut, Date dateFin, Date dateAnnulation,
////			String commentaireAnnulation, Integer nbEcheance, Integer versionObj)
//	public List<TaStripeSubscriptionDTO> rechercherSubscriptionPlanDTO(String idExternePlan) {
//		logger.debug("getting TaStripeSubscription_old instance with idExternePlan: " + idExternePlan);
//		try {
//			if(!idExternePlan.equals("")){
//				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeSubscriptionDTO("
//						+ " s.id, s.idExterne, s.codeDocument, "
//						+ " c.idStripeCustomer, c.idExterne, c.taTiers.codeTiers, c.taTiers.nomTiers,"
//						+ " s.quantity, s.billing, s.status, s.dateDebut, s.dateFin, s.dateAnnulation, s.commentaireAnnulation, s.nbEcheance, s.versionObj) "
//						+ " from TaStripeSubscriptionItem f join f.taStripeSubscription s join s.taStripeCustomer c where f.taPlan.idExterne='"+idExternePlan+"' ");
//				List<TaStripeSubscriptionDTO> instance = (List<TaStripeSubscriptionDTO>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//	
//	private TaStripePlan taPlan;
//	private Integer quantity;
//	private TaStripeSubscription_old taStripeSubscription;
//	public List<TaStripeSubscription_old> rechercherSubscriptionPlan(String idExternePlan) {
//		logger.debug("getting TaStripeSubscription_old instance with idExternePlan: " + idExternePlan);
//		try {
//			if(!idExternePlan.equals("")){
//				Query query = entityManager.createQuery("select f.taStripeSubscription from TaStripeSubscriptionItem f where f.taPlan.idExterne='"+idExternePlan+"' ");
//				List<TaStripeSubscription_old> instance = (List<TaStripeSubscription_old>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
//	
//	public TaStripeSubscription_old findByCode(String code, String codeArticle) {
//		logger.debug("getting TaStripeSubscription_old instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaStripeSubscription_old f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaStripeSubscription_old instance = (TaStripeSubscription_old)query.getSingleResult();
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
//	public TaStripeSubscription_old findById(int id) {
//		logger.debug("getting TaStripeSubscription_old instance with id: " + id);
//		try {
//			TaStripeSubscription_old instance = entityManager.find(TaStripeSubscription_old.class, id);
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
//	public List<TaStripeSubscription_old> selectAll() {
//		logger.debug("selectAll TaStripeSubscription_old");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
////			System.out.println(defaultJPQLQuery);
////			System.out.println(ejbQuery.getResultList().toString());
////			System.out.println(ejbQuery.toString());
//			List<TaStripeSubscription_old> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
//	public TaStripeSubscription_old refresh(TaStripeSubscription_old detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaStripeSubscription_old.class, detachedInstance.getIdStripeSubscription());
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
//	public List<TaStripeSubscription_old> findByArticle(String codeArticle) {
//		logger.debug("getting TaStripeSubscription_old instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeSubscription_old.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaStripeSubscription_old> l = query.getResultList();;
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
////	public ModelGeneralObjet<SWTUnite,TaStripeSubscriptionDAO,TaStripeSubscription_old> modelObjetUniteArticle(String codeArticle) {
////		return new ModelGeneralObjet<SWTUnite,TaStripeSubscriptionDAO,TaStripeSubscription_old>(findByArticle(codeArticle),SWTUnite.class,entityManager);
////	}
//	
//	public List<TaStripeSubscriptionDTO> findAllLight() {
////		try {
////			Query query = entityManager.createNamedQuery(TaStripeSubscription_old.QN.FIND_ALL_LIGHT);
////			List<TaStripeSubscriptionDTO> l = query.getResultList();;
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
//	public List<TaStripeSubscriptionDTO> findByCodeLight(String code) {
////		logger.debug("getting TaStripeSubscriptionDTO instance");
////		try {
////			Query query = null;
////			if(code!=null && !code.equals("") && !code.equals("*")) {
////				query = entityManager.createNamedQuery(TaStripeSubscription_old.QN.FIND_BY_CODE_LIGHT);
////				query.setParameter(1, "%"+code+"%");
////			} else {
////				query = entityManager.createNamedQuery(TaStripeSubscription_old.QN.FIND_ALL_LIGHT);
////			}
////
////			List<TaStripeSubscriptionDTO> l = query.getResultList();
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
//	
//	
//	public void ctrlSaisieSpecifique(TaStripeSubscription_old entity,String field) throws ExceptLgr {	
//		try {		
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	
//
//
//	@Override
//	public List<TaStripeSubscription_old> findWithNamedQuery(String queryName) {
//		try {
//			Query ejbQuery = entityManager.createNamedQuery(queryName);
//			List<TaStripeSubscription_old> l = ejbQuery.getResultList();
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
//	public List<TaStripeSubscription_old> findWithJPQLQuery(String JPQLquery) {
//		try {
//			Query ejbQuery = entityManager.createQuery(JPQLquery);
//			List<TaStripeSubscription_old> l = ejbQuery.getResultList();
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
//	public boolean validate(TaStripeSubscription_old value) throws Exception {
//		BeanValidator<TaStripeSubscription_old> validator = new BeanValidator<TaStripeSubscription_old>(TaStripeSubscription_old.class);
//		return validator.validate(value);
//	}
//
//	@Override
//	public boolean validateField(TaStripeSubscription_old value, String propertyName) throws Exception {
//		BeanValidator<TaStripeSubscription_old> validator = new BeanValidator<TaStripeSubscription_old>(TaStripeSubscription_old.class);
//		return validator.validateField(value,propertyName);
//	}
//
//	@Override
//	public void detach(TaStripeSubscription_old transientInstance) {
//		entityManager.detach(transientInstance);
//	}
//	
//}
