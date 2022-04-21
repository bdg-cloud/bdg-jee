package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IAdresseDAO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.model.old.TaAdresse
 * @author Hibernate Tools
 */
public class AdresseDAO implements IAdresseDAO {

	static Logger logger = Logger.getLogger(AdresseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaAdresse a";
	
	public AdresseDAO(){
	}

//	public TaAdresse refresh(TaAdresse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaAdresse.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaAdresse transientInstance) {
		logger.debug("persisting TaAdresse instance");
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
	public void remove(TaAdresse persistentInstance) {
		logger.debug("removing TaAdresse instance");
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
	public TaAdresse merge(TaAdresse detachedInstance) {
		logger.debug("merging TaAdresse instance");
		try {
			TaAdresse result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaAdresse findById(int id) {
		logger.debug("getting TaAdresse instance with id: " + id);
		try {
			TaAdresse instance = entityManager.find(TaAdresse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaAdresse> rechercheParType(String codeType) {
		Query query = entityManager.createNamedQuery(TaAdresse.QN.FIND_BY_TYPE);
		query.setParameter("code", codeType);
		List<TaAdresse> l = query.getResultList();

		return l;
	}

	public List<TaAdresse> rechercheParImport(String origineImport, String idImport) {
		Query query = entityManager.createNamedQuery(TaAdresse.QN.FIND_BY_IMPORT);
		query.setParameter("origineImport", origineImport);
		query.setParameter("idImport", idImport);
		List<TaAdresse> l = query.getResultList();

		return l;
	}

	public int rechercheOdrePourType(String codeType,String codeTiers) {
		try {
			Query query = entityManager.createNamedQuery(TaAdresse.QN.FIND_BY_TYPE_ORDRE_TIERS);
			query.setParameter("code", codeType);
			query.setParameter("codeTiers", codeTiers);
			int l = (int) query.getSingleResult();
			

			return l+1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 1;
	}
	

	@Override
	public List<TaAdresse> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaAdresse");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAdresse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaAdresse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAdresse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAdresse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAdresse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAdresse value) throws Exception {
		BeanValidator<TaAdresse> validator = new BeanValidator<TaAdresse>(TaAdresse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAdresse value, String propertyName) throws Exception {
		BeanValidator<TaAdresse> validator = new BeanValidator<TaAdresse>(TaAdresse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAdresse transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaAdresse findByCode(String code) {
		return null;
	}
	
//	public void ctrlSaisieSpecifique(TaAdresse entity,String field) throws ExceptLgr {	
//		
//	}

}
