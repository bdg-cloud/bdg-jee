package fr.legrain.documents.dao.jpa;

import java.text.ParseException;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.dto.TaReglementTracaDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaReglementTraca;
import fr.legrain.documents.dao.IReglementDAO;
import fr.legrain.documents.dao.IReglementTracaDAO;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaRAcompte.
 * @see fr.legrain.documents.dao.TaRAcompte
 * @author Hibernate Tools
 */
public class TaReglementTracaDAO  implements IReglementTracaDAO {


	static Logger logger = Logger.getLogger(IReglementTracaDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaReglementTraca a";
	
	public TaReglementTracaDAO(){

	}
	

	
	public void persist(TaReglementTraca transientInstance) {
		logger.debug("persisting TaReglementTraca instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaReglementTraca persistentInstance) {
		logger.debug("removing TaReglementTraca instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaReglementTraca merge(TaReglementTraca detachedInstance) {
		logger.debug("merging TaReglementTraca instance");
		try {
			TaReglementTraca result = entityManager.merge(detachedInstance);
//			entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaReglementTraca findById(int id) {
		logger.debug("getting TaReglementTraca instance with id: " + id);
		try {
			TaReglementTraca instance = entityManager.find(TaReglementTraca.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}




	public TaReglementTraca findByCode(String code) {
		logger.debug("getting TaReglementTraca instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaReglementTraca a where codeReglement like :code");
			query.setParameter("code", code);
			TaReglementTraca instance = (TaReglementTraca)query.getSingleResult();
			return instance;
		} catch (RuntimeException re) {
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
	



	
//	@Override
	public List<TaReglementTraca> selectAll() {
		logger.debug("selectAll TaReglementTraca");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaReglementTraca> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	

	@Override
	public List<TaReglementTraca> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaReglementTraca> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaReglementTraca> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaReglementTraca> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaReglementTraca value) throws Exception {
		BeanValidator<TaReglementTraca> validator = new BeanValidator<TaReglementTraca>(TaReglementTraca.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaReglementTraca value, String propertyName)
			throws Exception {
		BeanValidator<TaReglementTraca> validator = new BeanValidator<TaReglementTraca>(TaReglementTraca.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaReglementTraca transientInstance) {
		entityManager.detach(transientInstance);
	}

	


	
}
