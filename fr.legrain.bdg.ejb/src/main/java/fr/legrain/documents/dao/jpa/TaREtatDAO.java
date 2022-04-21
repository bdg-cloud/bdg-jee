package fr.legrain.documents.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.RetourMajDossier;
import fr.legrain.document.model.TaREtat;
import fr.legrain.documents.dao.IREtatDAO;

/**
 * Home object for domain model class TaREtat.
 * @see fr.legrain.documents.dao.TaREtat
 * @author Hibernate Tools
 */
public class TaREtatDAO implements IREtatDAO {

	static Logger logger = Logger.getLogger(TaREtatDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	
	private String defaultJPQLQuery = "select a from TaREtat a ";
	
	public TaREtatDAO(){
//		this(null);
	}
	

	
	public void persist(TaREtat transientInstance) {
		logger.debug("persisting TaREtat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaREtat persistentInstance) {
		logger.debug("removing TaREtat instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaREtat merge(TaREtat detachedInstance) {
		logger.debug("merging TaREtat instance");
		try {
			TaREtat result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaREtat findById(int id) {
		logger.debug("getting TaREtat instance with id: " + id);
		try {
			TaREtat instance = entityManager.find(TaREtat.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	


	


	public TaREtat findByCode(String code) {
		logger.debug("getting TaREtat instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaREtat a where a.codeEtat='"+code+"'");
			TaREtat instance = (TaREtat)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	public List<TaREtat> selectAll() {
		logger.debug("selectAll TaREtat");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaREtat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaREtat> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaREtat> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaREtat value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaREtat value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaREtat transientInstance) {
		// TODO Auto-generated method stub
		
	}

	
	public RetourMajDossier updateEtatEncoursTousDocs() {
		RetourMajDossier retour = new RetourMajDossier(true);		
		logger.debug("getting updateEtatEncoursTousDocs ");
		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("update_etat_encours_tout_docs");
			retour.setRetour(query.execute());
			logger.debug("get successful");
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			retour.setRetour(false);
			retour.setMessage(re.getMessage());
		}
		return retour;
	}



	@Override
	public RetourMajDossier updateEtatTousDocs() {
		RetourMajDossier retour = new RetourMajDossier(true);		
		logger.debug("getting updateEtatTousDocs ");
		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("update_etat_tout_doc_lie");
			retour.setRetour(query.execute());
			logger.debug("get successful");
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			retour.setRetour(false);
			retour.setMessage(re.getMessage());
		}
		return retour;
	}



	@Override
	public RetourMajDossier majLigneALigneProcedureStockee() {
		RetourMajDossier retour = new RetourMajDossier(true);		
		logger.debug("getting majLigneALigneProcedureStockee ");
		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("maj_ligne_a_ligne");
			retour.setRetour(query.execute());
			logger.debug("get successful");
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			retour.setRetour(false);
			retour.setMessage(re.getMessage());
		}
		return retour;
	}

}
