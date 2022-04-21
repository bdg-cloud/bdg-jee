package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaRemise;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.dao.ICarteBancaireDAO;
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaCarteBancaire.
 * @see fr.legrain.tiers.model.old.TaCarteBancaire
 * @author Hibernate Tools
 */
public class CarteBancaireDAO implements ICarteBancaireDAO {

	static Logger logger = Logger.getLogger(CarteBancaireDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaCarteBancaire a";
	
	public CarteBancaireDAO() {
	}

	public void persist(TaCarteBancaire transientInstance) {
		logger.debug("persisting TaCarteBancaire instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
	public TaCarteBancaire refresh(TaCarteBancaire detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaCarteBancaire.class, detachedInstance.getIdCarteBancaire());
		    return detachedInstance;
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaCarteBancaire persistentInstance) {
		logger.debug("removing TaCarteBancaire instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCarteBancaire merge(TaCarteBancaire detachedInstance) {
		logger.debug("merging TaCarteBancaire instance");
		try {
			TaCarteBancaire result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCarteBancaire findById(int id) {
		logger.debug("getting TaCarteBancaire instance with id: " + id);
		try {
			TaCarteBancaire instance = entityManager.find(TaCarteBancaire.class,
					id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaCarteBancaire> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaCarteBancaire");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCarteBancaire> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaCarteBancaire> selectCompteEntreprise() {
		// TODO Auto-generated method stub
		logger.debug("selectCompteEntreprise TaCarteBancaire");
		try {
			Query ejbQuery = entityManager.createQuery("select a from TaCarteBancaire a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 order by a.idCompteBanque");
			List<TaCarteBancaire> l = ejbQuery.getResultList();
			logger.debug("selectCompteEntreprise successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectCompteEntreprise failed", re);
			throw re;
		}
	}
	
	public List<TaCarteBancaire> selectCompteTiers(TaTiers taTiers) {
		// TODO Auto-generated method stub
		logger.debug("selectCompteTiers TaCarteBancaire");
		try {
			if(taTiers!=null){
			Query ejbQuery = entityManager.createQuery("select a from TaCarteBancaire a where a.taTiers.idTiers=:idTiers");
			ejbQuery.setParameter("idTiers", taTiers.getIdTiers());
			List<TaCarteBancaire> l = ejbQuery.getResultList();
			logger.debug("selectCompteTiers successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectCompteTiers failed", re);
			throw re;
		}
	}
	
  	public TaCarteBancaire findByTiersEntreprise(String code) {
		try {
			if(code!=null && code!=""){
				Query query =entityManager.createQuery("select a from TaCarteBancaire a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1" +
				" and a.compte like :compte");
				query.setParameter("compte", code);
				if(query.getSingleResult()!=null)
					return (TaCarteBancaire) query.getSingleResult();
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
  	
	public TaCarteBancaire findByTiersEntreprise() {
		try {
			Query query =null;
					query = entityManager.createQuery("select a from TaCarteBancaire a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 order by 1" );

				
				List<TaCarteBancaire> l =query.getResultList();
				if(l.size()>0){
					return l.get(0);
				}
//			return (TaCarteBancaire) query.getSingleResult();
				return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public TaCarteBancaire findByTiersEntreprise(TaTPaiement tPaiement) {
		try {
			Query query =null;
				if(tPaiement!=null){
					query = entityManager.createQuery("select a from TaCarteBancaire a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 and a.cptcomptable like :cptcomptable order by 1" );
					query.setParameter("cptcomptable", tPaiement.getCompte());
				}else{
					query = entityManager.createQuery("select a from TaCarteBancaire a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 order by 1" );
				}
				
				List<TaCarteBancaire> l =query.getResultList();
				if(l.size()>0){
					return l.get(0);
				}else
					return findByTiersEntreprise();
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCarteBancaire> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement) {
		try {
			Query query =null;
//				if(tPaiement!=null){
//					query = getEntityManager().createQuery("select a from TaCarteBancaire a where a.taTiers.idTiers=-1 and a.taTBanque.codeTBanque='PREL'" );
					query = entityManager.createQuery("select a from TaCarteBancaire a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 " );
//					query.setParameter(1, tPaiement.getCompte());
//				}else{
//					query = getEntityManager().createQuery("select a from TaCarteBancaire a where a.taTiers.idTiers=-1 order by 1" );
//				}
				
//				Query query = getEntityManager().createQuery("select a from TaCarteBancaire a where a.taTiers.idTiers=-1 " );
				List<TaCarteBancaire> l =query.getResultList();
				return l;
//			return (TaCarteBancaire) query.getSingleResult();
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaCarteBancaire> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCarteBancaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCarteBancaire> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCarteBancaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCarteBancaire value) throws Exception {
		BeanValidator<TaCarteBancaire> validator = new BeanValidator<TaCarteBancaire>(TaCarteBancaire.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCarteBancaire value, String propertyName) throws Exception {
		BeanValidator<TaCarteBancaire> validator = new BeanValidator<TaCarteBancaire>(TaCarteBancaire.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCarteBancaire transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaCarteBancaire findByCode(String iban) {
		logger.debug("getting TaCarteBancaire instance with code: " + iban);
		try {
			if(!iban.equals("")){
			Query query = entityManager.createQuery("select f from TaCarteBancaire f where f.iban='"+iban+"'");
			TaCarteBancaire instance = (TaCarteBancaire)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	
//	public void ctrlSaisieSpecifique(TaCarteBancaire entity,String field) throws ExceptLgr {	
//		
//	}
}
