package fr.legrain.documents.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.TaLRemiseDTO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaLRemise;
import fr.legrain.document.model.TaRemise;
import fr.legrain.documents.dao.ILRemiseDAO;
import fr.legrain.documents.dao.IRemiseDAO;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.tiers.dao.IDocumentDAO;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.dao.TaTLiens
 * @author Hibernate Tools
 */
public class TaLRemiseDAO /*extends AbstractApplicationDAO<TaLRemise>*/implements ILRemiseDAO{

	static Logger logger = Logger.getLogger(TaLRemiseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaLRemise a ";
	
	public TaLRemiseDAO() {
//		this(null);
	}
	

	
	public void persist(TaLRemise transientInstance) {
		logger.debug("persisting TaLRemise instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLRemise persistentInstance) {
		logger.debug("removing TaLRemise instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLRemise merge(TaLRemise detachedInstance) {
		logger.debug("merging TaLRemise instance");
		try {
			TaLRemise result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLRemise findById(int id) {
		logger.debug("getting TaLRemise instance with id: " + id);
		try {
			TaLRemise instance = entityManager.find(TaLRemise.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaLRemise> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaLRemise");
		try {  
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLRemise> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaLRemiseDTO> RechercheLigneRemiseDTO(String codeRemise) {
		try {
			List<TaLRemiseDTO> result = null;
			Query query = entityManager.createNamedQuery(TaLRemise.QN.FIND_ALL_BY_REMISE);
			query.setParameter("codeDoc", codeRemise);

			result = query.getResultList();
			logger.debug("get successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}



	@Override
	public TaLRemise findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<TaLRemise> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<TaLRemise> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean validate(TaLRemise value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean validateField(TaLRemise value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void detach(TaLRemise transientInstance) {
		// TODO Auto-generated method stub
		
	}
}
