package fr.legrain.controle.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.controle.dao.IVerrouCodeGenereDAO;
import fr.legrain.controle.model.TaVerrouCodeGenere;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaVerrouCodeGenere.
 * @see fr.legrain.tiers.model.old.TaVerrouCodeGenere
 * @author Hibernate Tools
 */
public class VerrouCodeGenereDAO implements IVerrouCodeGenereDAO {

	static Logger logger = Logger.getLogger(VerrouCodeGenereDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaVerrouCodeGenere a";
	
	public VerrouCodeGenereDAO(){
	}

//	public TaVerrouCodeGenere refresh(TaVerrouCodeGenere detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaVerrouCodeGenere.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaVerrouCodeGenere transientInstance) {
		logger.debug("persisting TaVerrouCodeGenere instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaVerrouCodeGenere persistentInstance) {
		logger.debug("removing TaVerrouCodeGenere instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaVerrouCodeGenere merge(TaVerrouCodeGenere detachedInstance) {
		logger.debug("merging TaVerrouCodeGenere instance");
		try {
			TaVerrouCodeGenere result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaVerrouCodeGenere findById(int id) {
		logger.debug("getting TaVerrouCodeGenere instance with id: " + id);
		try {
			TaVerrouCodeGenere instance = entityManager.find(TaVerrouCodeGenere.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaVerrouCodeGenere> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaVerrouCodeGenere");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaVerrouCodeGenere> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaVerrouCodeGenere> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaVerrouCodeGenere> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaVerrouCodeGenere> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaVerrouCodeGenere> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaVerrouCodeGenere value) throws Exception {
		BeanValidator<TaVerrouCodeGenere> validator = new BeanValidator<TaVerrouCodeGenere>(TaVerrouCodeGenere.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaVerrouCodeGenere value, String propertyName) throws Exception {
		BeanValidator<TaVerrouCodeGenere> validator = new BeanValidator<TaVerrouCodeGenere>(TaVerrouCodeGenere.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaVerrouCodeGenere transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaVerrouCodeGenere findByCode(String code) {
		logger.debug("getting TaVerrouCodeGenere instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaVerrouCodeGenere a where a.entite='"+code+"'");
				TaVerrouCodeGenere instance = (TaVerrouCodeGenere)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public void ctrlSaisieSpecifique(TaVerrouCodeGenere entity,String field) throws ExceptLgr {	
//		
//	}
	
	@Override
	public TaVerrouCodeGenere findVerrou(String entite, String champ, String valeur) {
		logger.debug("getting TaVerrouCodeGenere instance with entite: " + entite);
		try {
			if(!entite.equals("")){
				Query query = entityManager.createQuery("select a from TaVerrouCodeGenere a where a.entite='"+entite+"' and a.champ='"+champ+"' and a.valeur='"+valeur+"'");
				TaVerrouCodeGenere instance = (TaVerrouCodeGenere)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
	public boolean estVerrouille(String entite, String champ, String valeur) {
		logger.debug("getting TaVerrouCodeGenere instance with entite: " + entite);
		try {
			if(!entite.equals("")){
				Query query = entityManager.createQuery("select a from TaVerrouCodeGenere a where a.entite='"+entite+"' and a.champ='"+champ+"' and a.valeur='"+valeur+"'");
				TaVerrouCodeGenere instance = (TaVerrouCodeGenere)query.getSingleResult();
				logger.debug("get successful");
				if(instance!=null) {
					return true;
				}
			}
			return true;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return false;
		}
	}
	
	public void libereVerrouTout(String entite, String champ, String sessionID) {
		try {
			Query query = entityManager.createQuery("delete from TaVerrouCodeGenere a where a.entite='"+entite+"' and a.champ='"+champ+"' and a.sessionID='"+sessionID+"'");
			int retour = query.executeUpdate();
			logger.debug("retour : "+retour);
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
		}
	}
	
	public void libereVerrouTout() {
		logger.debug("Vide la table des code générés vérrouillés");
		try {
			Query query = entityManager.createQuery("delete from TaVerrouCodeGenere");
			int retour = query.executeUpdate();
			logger.debug("retour : "+retour);
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
		}
	}
	
	public void libereVerrouTout(List<String> listeSessionID) {
		logger.debug("Vide la table des code générés vérrouillés");
		try {
			if(listeSessionID!=null) {
				Query query = entityManager.createQuery("delete from TaVerrouCodeGenere v where v.sessionID not in (:liste)");
				query.setParameter("liste", listeSessionID);
				int retour = query.executeUpdate();
				System.out.println("Libération de "+retour+" codes générés.");
				logger.debug("retour : "+retour);
			}
		} catch (RuntimeException re) {
			logger.error("vide la table des code générés PLANTE !!!!!!!!!!"/*, re*/);
//			throw re;
		}
	}

}
