package fr.legrain.abonnement.stripe.dao.jpa;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.stripe.dao.IStripeTSourceDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeTSource;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class StripeTSourceDAO implements IStripeTSourceDAO, Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(StripeTSourceDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

		private String defaultJPQLQuery = "select u from TaStripeTSource u";
	//private String defaultJPQLQuery = "select u from TaStripeTSource u where u.taArticle is null";
	
	public StripeTSourceDAO(){
	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaStripeTSource)
	 */
	public void persist(TaStripeTSource transientInstance) {
		logger.debug("persisting TaStripeTSource instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaStripeTSource)
	 */
	public void remove(TaStripeTSource persistentInstance) {
		logger.debug("removing TaStripeTSource instance");
		try {
			TaStripeTSource e = entityManager.merge(findById(persistentInstance.getIdStripeTSource()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaStripeTSource)
	 */
	public TaStripeTSource merge(TaStripeTSource detachedInstance) {
		logger.debug("merging TaStripeTSource instance");
		try {
			TaStripeTSource result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaStripeTSource findByCode(String code) {
		logger.debug("getting TaStripeTSource instance with code: " + code);
		try {
			if(!code.equals("")){
				//modif yann
				//Query query = entityManager.createQuery("select f from TaStripeTSource f where f.idExterne='"+idExterne+"' ");
				Query query = entityManager.createQuery("select f from TaStripeTSource f where f.codeStripeTSource='"+code+"' ");
				TaStripeTSource instance = (TaStripeTSource)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeTSource findByCode(String code, String codeArticle) {
		logger.debug("getting TaStripeTSource instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaStripeTSource f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaStripeTSource instance = (TaStripeTSource)query.getSingleResult();
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
	public TaStripeTSource findById(int id) {
		logger.debug("getting TaStripeTSource instance with id: " + id);
		try {
			TaStripeTSource instance = entityManager.find(TaStripeTSource.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeTSource rechercherSource(TaCompteBanque compteBanque) {
		logger.debug("getting TaStripeCustomer instance with id Compte banque: " + compteBanque.getIdCompteBanque());
		try {
				Query query = entityManager.createQuery("select f from TaStripeTSource f where f.taCompteBanque.idCompteBanque='"+compteBanque.getIdCompteBanque()+"'");
				TaStripeTSource instance = (TaStripeTSource)query.getSingleResult();
				logger.debug("get successful");
				return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaStripeTSource rechercherSource(TaCarteBancaire carteBancaire) {
		return null;
	}
	public List<TaStripeTSource> rechercherSource(TaTiers tiers) {
		logger.debug("getting TaStripeTSource instance with idTiers: " + tiers.getIdTiers());
		try {
			if(tiers != null){
				Query query = entityManager.createQuery("select f from TaStripeTSource f where f.taStripeCustomer.taTiers.idTiers="+tiers.getIdTiers()+"");
				List<TaStripeTSource> l = (List<TaStripeTSource>)query.getResultList();
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
	public List<TaStripeTSource> rechercherSourceCustomer(String idExterneCustomer) {
		logger.debug("getting TaStripeTSource instance with idExterneCustomer: " + idExterneCustomer);
		try {
			if(!idExterneCustomer.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeTSource f where f.taStripeCustomer.idExterne='"+idExterneCustomer+"'");
				List<TaStripeTSource> l = (List<TaStripeTSource>)query.getResultList();
				logger.debug("get successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaStripeTSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer) {
		logger.debug("getting TaStripeTSource instance with idExterneCustomer: " + idExterneCustomer);
		try {
			if(!idExterneCustomer.equals("")){
				Query query = entityManager.createQuery("select new fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO("
						+ " s.id, s.idExterne, s.taStripeCustomer.id, s.taStripeCustomer.idExterne,"
						+ " cb.nomProprietaire, cb.numeroCarte, cb.empreinte, cb.moisExpiration, cb.anneeExpiration, cb.type, cb.paysOrigine, cb.last4, cb.cvc,"
						+ " cpt.iban, cpt.bankCode, cpt.branchCode, cpt.country, cpt.last4) "
						+ " from TaStripeTSource s left join s.taCarteBancaire cb left join s.taCompteBanque cpt "
						+ " where s.taStripeCustomer.idExterne='"+idExterneCustomer+"'");
				
				List<TaStripeTSourceDTO> l = (List<TaStripeTSourceDTO>)query.getResultList();
				logger.debug("get successful");
				return l;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaStripeTSource rechercherSource(String idExterneSource) {
		logger.debug("getting TaStripeTSource instance with idExterne: " + idExterneSource);
		try {
			if(!idExterneSource.equals("")){
				Query query = entityManager.createQuery("select f from TaStripeTSource f where f.idExterne='"+idExterneSource+"'");
				TaStripeTSource instance = (TaStripeTSource)query.getSingleResult();
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
	public List<TaStripeTSource> selectAll() {
		logger.debug("selectAll TaStripeTSource");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaStripeTSource> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStripeTSource refresh(TaStripeTSource detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaStripeTSource.class, detachedInstance.getIdStripeTSource());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaStripeTSource> findByArticle(String codeArticle) {
		logger.debug("getting TaStripeTSource instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaStripeTSource.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaStripeTSource> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaStripeTSourceDAO,TaStripeTSource> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaStripeTSourceDAO,TaStripeTSource>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaStripeTSourceDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaStripeTSource.QN.FIND_ALL_LIGHT);
//			List<TaStripeTSourceDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public List<TaStripeTSourceDTO> findByCodeLight(String code) {
//		logger.debug("getting TaStripeTSourceDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaStripeTSource.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaStripeTSource.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaStripeTSourceDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
		return null;
	}
	
	public void ctrlSaisieSpecifique(TaStripeTSource entity,String field) throws ExceptLgr {	
		try {		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	


	@Override
	public List<TaStripeTSource> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStripeTSource> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStripeTSource> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStripeTSource> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStripeTSource value) throws Exception {
		BeanValidator<TaStripeTSource> validator = new BeanValidator<TaStripeTSource>(TaStripeTSource.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStripeTSource value, String propertyName) throws Exception {
		BeanValidator<TaStripeTSource> validator = new BeanValidator<TaStripeTSource>(TaStripeTSource.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStripeTSource transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
