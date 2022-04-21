package fr.legrain.article.titretransport.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaUnite;
import fr.legrain.article.titretransport.dao.ITitreTransportCapsulesDAO;
import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTNoteArticle.
 * @see fr.legrain.tiers.dao.TaTitreTransport
 * @author Hibernate Tools
 */
public class TitreTransportDAO implements ITitreTransportCapsulesDAO /*extends AbstractApplicationDAO<TaTitreTransport>*/{

	static Logger logger = Logger.getLogger(TaTitreTransport.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTitreTransport a";
	
	public TitreTransportDAO() {
//		this(null);
	}
	
	public List<TaTitreTransportDTO> findByCodeLight(String code) {
		logger.debug("getting List<TaTitreTransportDTO> instance with code: " + code);
		try {
			
			Query query = null;
			String code2 = "KG";
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTitreTransport.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeUnite", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTitreTransport.QN.FIND_ALL_LIGHT);
			}

			List<TaTitreTransportDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
			
		}
	}
	
	public void persist(TaTitreTransport transientInstance) {
		logger.debug("persisting TaTitreTransport instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTitreTransport persistentInstance) {
		logger.debug("removing TaTitreTransport instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTitreTransport merge(TaTitreTransport detachedInstance) {
		logger.debug("merging TaTitreTransport instance");
		try {
			TaTitreTransport result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTitreTransport findById(int id) {
		logger.debug("getting TaTitreTransport instance with id: " + id);
		try {
			TaTitreTransport instance = entityManager.find(TaTitreTransport.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	@Override
	public List<TaTitreTransport> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTitreTransport");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTitreTransport> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaTitreTransport findByCode(String code) {
		logger.debug("getting TaTitreTransport instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaTitreTransport a where a.codeTitreTransport='"+code+"'");
			TaTitreTransport instance = (TaTitreTransport)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
	public void ctrlSaisieSpecifique(TaTitreTransport entity,String field) throws ExceptLgr {	
		
	}
	
	@Override
	public List<TaTitreTransport> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTitreTransport> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTitreTransport> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTitreTransport> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTitreTransport value) throws Exception {
		BeanValidator<TaTitreTransport> validator = new BeanValidator<TaTitreTransport>(TaTitreTransport.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTitreTransport value, String propertyName) throws Exception {
		BeanValidator<TaTitreTransport> validator = new BeanValidator<TaTitreTransport>(TaTitreTransport.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTitreTransport transientInstance) {
		entityManager.detach(transientInstance);
	}


}
