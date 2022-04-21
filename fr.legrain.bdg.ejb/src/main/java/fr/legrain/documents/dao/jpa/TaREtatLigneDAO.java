package fr.legrain.documents.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.documents.dao.IREtatDAO;
import fr.legrain.documents.dao.IREtatLigneDAO;

/**
 * Home object for domain model class TaREtatLigneDocument.
 * @see fr.legrain.documents.dao.TaREtatLigneDocument
 * @author Hibernate Tools
 */
public class TaREtatLigneDAO implements IREtatLigneDAO {

	static Logger logger = Logger.getLogger(TaREtatLigneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	
	private String defaultJPQLQuery = "select a from TaREtatLigneDocument a ";
	
	public TaREtatLigneDAO(){
//		this(null);
	}
	

	
	public void persist(TaREtatLigneDocument transientInstance) {
		logger.debug("persisting TaREtatLigneDocument instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaREtatLigneDocument persistentInstance) {
		logger.debug("removing TaREtatLigneDocument instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaREtatLigneDocument merge(TaREtatLigneDocument detachedInstance) {
		logger.debug("merging TaREtatLigneDocument instance");
		try {
			TaREtatLigneDocument result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaREtatLigneDocument findById(int id) {
		logger.debug("getting TaREtatLigneDocument instance with id: " + id);
		try {
			TaREtatLigneDocument instance = entityManager.find(TaREtatLigneDocument.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	


	


	public TaREtatLigneDocument findByCode(String code) {
		logger.debug("getting TaREtatLigneDocument instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaREtatLigneDocument a where a.codeEtat='"+code+"'");
			TaREtatLigneDocument instance = (TaREtatLigneDocument)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	public List<TaREtatLigneDocument> selectAll() {
		logger.debug("selectAll TaREtatLigneDocument");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaREtatLigneDocument> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaREtatLigneDocument> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaREtatLigneDocument> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaREtatLigneDocument value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaREtatLigneDocument value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaREtatLigneDocument transientInstance) {
		// TODO Auto-generated method stub
		
	}

}
