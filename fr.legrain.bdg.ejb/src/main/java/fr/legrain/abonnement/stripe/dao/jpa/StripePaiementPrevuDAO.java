package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripePaiementPrevuDAO;
import fr.legrain.abonnement.stripe.dao.IStripeSourceDAO;
import fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class StripePaiementPrevuDAO implements IStripePaiementPrevuDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripePaiementPrevuDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripePaiementPrevu u";
	//private String defaultJPQLQuery = "select u from TaStripePaiementPrevu u where u.taArticle is null";
	
	public StripePaiementPrevuDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripePaiementPrevu)
	 */
	public void persist(TaStripePaiementPrevu transientInstance) {
		logger.debug("persisting TaStripePaiementPrevu instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripePaiementPrevu)
	 */
	public void remove(TaStripePaiementPrevu persistentInstance) {
		logger.debug("removing TaStripePaiementPrevu instance");
		try {
			TaStripePaiementPrevu e = entityManager.merge(findById(persistentInstance.getIdStripePaiementPrevu()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripePaiementPrevu)
	 */
	public TaStripePaiementPrevu merge(TaStripePaiementPrevu detachedInstance) {
		logger.debug("merging TaStripePaiementPrevu instance");
		try {
			TaStripePaiementPrevu result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripePaiementPrevu findByCode(String idExterne) {
		logger.debug("getting TaStripePaiementPrevu instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripePaiementPrevu f where f.idExterne='"+idExterne+"' ");
				TaStripePaiementPrevu instance = (TaStripePaiementPrevu)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripePaiementPrevu findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripePaiementPrevu instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripePaiementPrevu f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripePaiementPrevu instance = (TaStripePaiementPrevu)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripePaiementPrevu> findByAbonnementAndDate(TaAbonnement taAbonnement, Date dateEcheance) {
		logger.debug("getting TaStripePaiementPrevu instance with date : " + dateEcheance);
		try {
			Query query = entityManager.createQuery("select f from TaStripePaiementPrevu f "
					+ " where f.taAbonnement.id= :idAbonnement "
					+ " and f.dateDeclenchement = :dateEcheance");
			query.setParameter("idAbonnement", taAbonnement.getIdDocument());
			query.setParameter("dateEcheance", dateEcheance);
			List<TaStripePaiementPrevu> instance = (List<TaStripePaiementPrevu>) query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaStripePaiementPrevu findById(int id) {
		logger.debug("getting TaStripePaiementPrevu instance with id: " + id);
		try {
			TaStripePaiementPrevu instance = entityManager.find(TaStripePaiementPrevu.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	


	@Override
	public List<TaStripePaiementPrevu> rechercherPaiementPrevuCustomer(String idExterneCustomer) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	public List<TaStripePaiementPrevu> rechercherSourceCustomer(String idExterneCustomer) {
//		logger.debug("getting TaStripePaiementPrevu instance with idExterneCustomer: " + idExterneCustomer);
//		try {
//			if(!idExterneCustomer.equals("")){
//				Query query = entityManager.createQuery("select f from TaStripePaiementPrevu f where f.taStripeCustomer.idExterne='"+idExterneCustomer+"'");
//				List<TaStripePaiementPrevu> l = (List<TaStripePaiementPrevu>)query.getResultList();
//				logger.debug("get successful");
//				return l;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}

	@Override
	public List<TaStripePaiementPrevuDTO> rechercherPaiementPrevuCustomerDTO(String idExterneCustomer) {
		logger.debug("getting TaStripePaiementPrevu instance with idExterneCustomer: " + idExterneCustomer);
		try {
			
				
				String jpql  = "select new fr.legrain.abonnement.stripe.dto.TaStripePaiementPrevuDTO("
						+ " pp.id, src.idStripeSource, src.idExterne, ch.idStripeCharge, ch.idExterne, ch.status, ch.paid, reg.idDocument, reg.codeDocument, "
						+ " sub.idDocument, sub.idExterne, sub.codeDocument, ae.idDocument, ae.codeDocument,"
						+ " inv.idStripeInvoice, inv.idExterne, cus.idStripeCustomer, cus.idExterne, t.codeTiers, t.nomTiers, "
						+ " pp.annule, pp.capture, pp.montant, pp.dateDeclenchement, pp.raisonAnnulation, "
						+ " pp.raisonPaiement, pp.commentaire, pp.versionObj) "
						+ " from TaStripePaiementPrevu pp "
						+ " left join pp.taStripeSource src "
						+ " left join pp.taStripeCharge ch "
						+ " left join ch.taReglement reg "
						+ " left join pp.taStripeCustomer cus "
						+ " left join cus.taTiers t "
						+ " left join pp.taAbonnement sub "
						+ " left join pp.taAvisEcheance ae "
						+ " left join pp.taStripeInvoice inv ";
				if(idExterneCustomer!=null && !idExterneCustomer.equals("")){
					jpql += " where cus.idExterne='"+idExterneCustomer+"'";
				}
				jpql += " order by pp.dateDeclenchement";
				Query query = entityManager.createQuery(jpql);
				
				List<TaStripePaiementPrevuDTO> l = (List<TaStripePaiementPrevuDTO>)query.getResultList();
				logger.debug("get successful");
				return l;
			
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripePaiementPrevu rechercherSource(String idExterneSource) {
		logger.debug("getting TaStripePaiementPrevu instance with idExterne: " + idExterneSource);
		try {
			if(!idExterneSource.equals("")){
				Query query = entityManager.createQuery("select f from TaStripePaiementPrevu f where f.idExterne='"+idExterneSource+"'");
				TaStripePaiementPrevu instance = (TaStripePaiementPrevu)query.getSingleResult();
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
	public List<TaStripePaiementPrevu> selectAll() {
		logger.debug("selectAll TaStripePaiementPrevu");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripePaiementPrevu> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripePaiementPrevu refresh(TaStripePaiementPrevu detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripePaiementPrevu.class, detachedInstance.getIdStripePaiementPrevu());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripePaiementPrevu> findByArticle(String codeArticle) {
		logger.debug("getting TaStripePaiementPrevu instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripePaiementPrevu.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripePaiementPrevu> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripePaiementPrevuDAO,TaStripePaiementPrevu> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripePaiementPrevuDAO,TaStripePaiementPrevu>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripePaiementPrevuDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripePaiementPrevu.QN.FIND_ALL_LIGHT);
//			List<TaStripePaiementPrevuDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripePaiementPrevuDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripePaiementPrevuDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripePaiementPrevu.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripePaiementPrevu.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripePaiementPrevuDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripePaiementPrevu entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripePaiementPrevu> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripePaiementPrevu> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripePaiementPrevu> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripePaiementPrevu> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripePaiementPrevu value) throws Exception {
		BeanValidator<TaStripePaiementPrevu> validator = new BeanValidator<TaStripePaiementPrevu>(TaStripePaiementPrevu.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripePaiementPrevu value, String propertyName) throws Exception {
		BeanValidator<TaStripePaiementPrevu> validator = new BeanValidator<TaStripePaiementPrevu>(TaStripePaiementPrevu.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripePaiementPrevu transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
