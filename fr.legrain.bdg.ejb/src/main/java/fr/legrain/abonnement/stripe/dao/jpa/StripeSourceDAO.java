package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeSourceDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class StripeSourceDAO implements IStripeSourceDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeSourceDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeSource u";
	//private String defaultJPQLQuery = "select u from TaStripeSource u where u.taArticle is null";
	
	public StripeSourceDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeSource)
	 */
	public void persist(TaStripeSource transientInstance) {
		logger.debug("persisting TaStripeSource instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeSource)
	 */
	public void remove(TaStripeSource persistentInstance) {
		logger.debug("removing TaStripeSource instance");
		try {
			TaStripeSource e = entityManager.merge(findById(persistentInstance.getIdStripeSource()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeSource)
	 */
	public TaStripeSource merge(TaStripeSource detachedInstance) {
		logger.debug("merging TaStripeSource instance");
		try {
			TaStripeSource result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeSource findByCode(String idExterne) {
		logger.debug("getting TaStripeSource instance with idExterne: " + idExterne);
		try {
			if(!idExterne.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeSource f where f.idExterne='"+idExterne+"' ");
				TaStripeSource instance = (TaStripeSource)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeSource findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeSource instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeSource f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeSource instance = (TaStripeSource)query.getSingleResult();
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
	public TaStripeSource findById(int id) {
		logger.debug("getting TaStripeSource instance with id: " + id);
		try {
			TaStripeSource instance = entityManager.find(TaStripeSource.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeSource rechercherSource(TaCompteBanque compteBanque) {
		logger.debug("getting TaStripeCustomer instance with id Compte banque: " + compteBanque.getIdCompteBanque());
		try {
				Query query = entityManager.createQuery("select f from TaStripeSource f where f.taCompteBanque.idCompteBanque='"+compteBanque.getIdCompteBanque()+"'");
				TaStripeSource instance = (TaStripeSource)query.getSingleResult();
				logger.debug("get successful");
				return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaStripeSource rechercherSource(TaCarteBancaire carteBancaire) {
		return null;
	}
	public List<TaStripeSource> rechercherSource(TaTiers tiers) {
		logger.debug("getting TaStripeSource instance with idTiers: " + tiers.getIdTiers());
		try {
			if(tiers != null){
				Query query = entityManager.createQuery("select f from TaStripeSource f where f.taStripeCustomer.taTiers.idTiers="+tiers.getIdTiers()+"");
				List<TaStripeSource> l = (List<TaStripeSource>)query.getResultList();
				logger.debug("get successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	/*
	 * 	private int idStripeCustomer;
	private String idExterne;
	private TaTiers taTiers;
	 */
	public List<TaStripeSource> rechercherSourceCustomer(String idExterneCustomer) {
		logger.debug("getting TaStripeSource instance with idExterneCustomer: " + idExterneCustomer);
		try {
			if(!idExterneCustomer.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeSource f where f.taStripeCustomer.idExterne='"+idExterneCustomer+"'");
				List<TaStripeSource> l = (List<TaStripeSource>)query.getResultList();
				logger.debug("get successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripeSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer) {
		//logger.debug("getting TaStripeSource instance with idExterneCustomer: " + idExterneCustomer);
		try {
				String jpql = "select new fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO("
						+ " s.id, s.idExterne, cus.id, cus.idExterne,"
						+ " cb.nomProprietaire, cb.numeroCarte, cb.empreinte, cb.moisExpiration, cb.anneeExpiration, cb.type, cb.paysOrigine, cb.last4, cb.cvc,"
						+ " cpt.iban, cpt.bankCode, cpt.branchCode, cpt.country, cpt.last4,"
						+ " s.taStripeTSource.codeStripeTSource, s.taStripeTSource.liblStripeTSource, s.taStripeTSource.automatique) "
						+ " from TaStripeSource s left join s.taCarteBancaire cb left join s.taCompteBanque cpt left join s.taStripeCustomer cus "
						+ " left join s.taStripeTSource ts "
						+ " where (ts is not null and  ts.automatique = false)";
				if(idExterneCustomer!=null) {
					jpql += " or ( cus.idExterne='"+idExterneCustomer+"')";
				}
				Query query = entityManager.createQuery(jpql);
				
				List<TaStripeSourceDTO> l = (List<TaStripeSourceDTO>)query.getResultList();
				logger.debug("get successful");
				return l;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeSource rechercherSource(String idExterneSource) {
		logger.debug("getting TaStripeSource instance with idExterne: " + idExterneSource);
		try {
			if(!idExterneSource.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeSource f where f.idExterne='"+idExterneSource+"'");
				TaStripeSource instance = (TaStripeSource)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	

	public TaStripeSource rechercherSourceManuelle(String codeStripeTSource) {
		logger.debug("getting TaStripeSource instance with codeStripeTSource: " + codeStripeTSource);
		try {
			if(!codeStripeTSource.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeSource f where f.taStripeTSource.codeStripeTSource='"+codeStripeTSource+"' "
						+ " and f.taStripeTSource.automatique = false");
				TaStripeSource instance = (TaStripeSource)query.getSingleResult();
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
	public List<TaStripeSource> selectAll() {
		logger.debug("selectAll TaStripeSource");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeSource> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeSource refresh(TaStripeSource detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeSource.class, detachedInstance.getIdStripeSource());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeSource> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeSource instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeSource.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeSource> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeSourceDAO,TaStripeSource> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeSourceDAO,TaStripeSource>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeSourceDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeSource.QN.FIND_ALL_LIGHT);
//			List<TaStripeSourceDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeSourceDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeSourceDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeSource.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeSource.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeSourceDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeSource entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeSource> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeSource> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeSource> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeSource> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeSource value) throws Exception {
		BeanValidator<TaStripeSource> validator = new BeanValidator<TaStripeSource>(TaStripeSource.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeSource value, String propertyName) throws Exception {
		BeanValidator<TaStripeSource> validator = new BeanValidator<TaStripeSource>(TaStripeSource.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeSource transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
