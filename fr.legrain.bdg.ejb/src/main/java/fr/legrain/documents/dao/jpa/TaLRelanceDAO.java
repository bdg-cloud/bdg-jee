package fr.legrain.documents.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.TaLRelanceDTO;
import fr.legrain.document.model.TaLRelance;
import fr.legrain.documents.dao.ILRelanceDAO;
import fr.legrain.lib.data.ExceptLgr;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.dao.TaTLiens
 * @author Hibernate Tools
 */
public class TaLRelanceDAO /*extends AbstractApplicationDAO<TaLRelance>*/ implements ILRelanceDAO{

	static Logger logger = Logger.getLogger(TaLRelanceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaLRelance a ";
	
	public TaLRelanceDAO() {
//		this(null);
	}

	
	public void persist(TaLRelance transientInstance) {
		logger.debug("persisting TaLRelance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLRelance persistentInstance) {
		logger.debug("removing TaLRelance instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLRelance merge(TaLRelance detachedInstance) {
		logger.debug("merging TaLRelance instance");
		try {
			TaLRelance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLRelance findById(int id) {
		logger.debug("getting TaLRelance instance with id: " + id);
		try {
			TaLRelance instance = entityManager.find(TaLRelance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaLRelance> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLRelance");
		try {  
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLRelance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	public void ctrlSaisieSpecifique(TaLRelance entity,String field) throws ExceptLgr {	
		
	}


	@Override
	public TaLRelance findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaLRelance> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<TaLRelance> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean validate(TaLRelance value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean validateField(TaLRelance value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void detach(TaLRelance transientInstance) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public List<TaLRelanceDTO> rechercheLigneRelanceDTO(String codeRelance) {
		// TODO Auto-generated method stub
		return null;
	}
}
