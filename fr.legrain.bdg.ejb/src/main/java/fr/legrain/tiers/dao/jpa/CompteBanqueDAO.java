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
import fr.legrain.tiers.dao.ICompteBanqueDAO;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaCompteBanque.
 * @see fr.legrain.tiers.model.old.TaCompteBanque
 * @author Hibernate Tools
 */
public class CompteBanqueDAO implements ICompteBanqueDAO {

	static Logger logger = Logger.getLogger(CompteBanqueDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaCompteBanque a";
	
	public CompteBanqueDAO() {
	}

	public void persist(TaCompteBanque transientInstance) {
		logger.debug("persisting TaCompteBanque instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
	public TaCompteBanque refresh(TaCompteBanque detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaCompteBanque.class, detachedInstance.getIdCompteBanque());
		    return detachedInstance;
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaCompteBanque persistentInstance) {
		logger.debug("removing TaCompteBanque instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCompteBanque merge(TaCompteBanque detachedInstance) {
		logger.debug("merging TaCompteBanque instance");
		try {
			TaCompteBanque result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCompteBanque findById(int id) {
		logger.debug("getting TaCompteBanque instance with id: " + id);
		try {
			TaCompteBanque instance = entityManager.find(TaCompteBanque.class,
					id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaCompteBanque> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaCompteBanque");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCompteBanque> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaCompteBanque> selectCompteEntreprise() {
		// TODO Auto-generated method stub
		logger.debug("selectCompteEntreprise TaCompteBanque");
		try {
			Query ejbQuery = entityManager.createQuery("select a from TaCompteBanque a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 order by a.idCompteBanque");
			List<TaCompteBanque> l = ejbQuery.getResultList();
			logger.debug("selectCompteEntreprise successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectCompteEntreprise failed", re);
			throw re;
		}
	}
	
	public List<TaCompteBanque> selectCompteTiers(TaTiers taTiers) {
		// TODO Auto-generated method stub
		logger.debug("selectCompteTiers TaCompteBanque");
		try {
			if(taTiers!=null){
			Query ejbQuery = entityManager.createQuery("select a from TaCompteBanque a where a.taTiers.idTiers=:idTiers");
			ejbQuery.setParameter("idTiers", taTiers.getIdTiers());
			List<TaCompteBanque> l = ejbQuery.getResultList();
			logger.debug("selectCompteTiers successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("selectCompteTiers failed", re);
			throw re;
		}
	}
	
  	public TaCompteBanque findByTiersEntreprise(String code) {
		try {
			if(code!=null && code!=""){
				Query query =entityManager.createQuery("select a from TaCompteBanque a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1" +
				" and a.compte like :compte");
				query.setParameter("compte", code);
				if(query.getSingleResult()!=null)
					return (TaCompteBanque) query.getSingleResult();
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
  	
	public TaCompteBanque findByTiersEntreprise() {
		try {
			Query query =null;
					query = entityManager.createQuery("select a from TaCompteBanque a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 order by 1" );

				
				List<TaCompteBanque> l =query.getResultList();
				if(l.size()>0){
					return l.get(0);
				}
//			return (TaCompteBanque) query.getSingleResult();
				return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public TaCompteBanque findByTiersEntreprise(TaTPaiement tPaiement) {
		try {
			Query query =null;
				if(tPaiement!=null){
					query = entityManager.createQuery("select a from TaCompteBanque a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 and a.cptcomptable like :cptcomptable order by 1" );
					query.setParameter("cptcomptable", tPaiement.getCompte());
				}else{
					query = entityManager.createQuery("select a from TaCompteBanque a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 order by 1" );
				}
				
				List<TaCompteBanque> l =query.getResultList();
				if(l.size()>0){
					return l.get(0);
				}else
					return findByTiersEntreprise();
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCompteBanque> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement) {
		try {
			Query query =null;
//				if(tPaiement!=null){
//					query = getEntityManager().createQuery("select a from TaCompteBanque a where a.taTiers.idTiers=-1 and a.taTBanque.codeTBanque='PREL'" );
					query = entityManager.createQuery("select a from TaCompteBanque a join a.taTiers t join t.taTTiers tt where tt.idTTiers=-1 " );
//					query.setParameter(1, tPaiement.getCompte());
//				}else{
//					query = getEntityManager().createQuery("select a from TaCompteBanque a where a.taTiers.idTiers=-1 order by 1" );
//				}
				
//				Query query = getEntityManager().createQuery("select a from TaCompteBanque a where a.taTiers.idTiers=-1 " );
				List<TaCompteBanque> l =query.getResultList();
				return l;
//			return (TaCompteBanque) query.getSingleResult();
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaCompteBanque> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCompteBanque> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCompteBanque> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCompteBanque> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCompteBanque value) throws Exception {
		BeanValidator<TaCompteBanque> validator = new BeanValidator<TaCompteBanque>(TaCompteBanque.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCompteBanque value, String propertyName) throws Exception {
		BeanValidator<TaCompteBanque> validator = new BeanValidator<TaCompteBanque>(TaCompteBanque.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCompteBanque transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaCompteBanque findByCode(String iban) {
		logger.debug("getting TaCompteBanque instance with code: " + iban);
		try {
			if(!iban.equals("")){
			Query query = entityManager.createQuery("select f from TaCompteBanque f where f.iban='"+iban+"'");
			TaCompteBanque instance = (TaCompteBanque)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
	
//	public void ctrlSaisieSpecifique(TaCompteBanque entity,String field) throws ExceptLgr {	
//		
//	}
}
