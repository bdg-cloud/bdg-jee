package fr.legrain.documents.dao.jpa;

import java.io.Serializable;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaFlashDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaLigneALigne;
import fr.legrain.documents.dao.IFlashDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaFlash.
 * @see fr.legrain.documents.dao.TaFlash
 * @author Hibernate Tools
 */
public class TaFlashDAO  implements IFlashDAO, Serializable {

//	private static final Log log = LogFactory.getLog(TaFlashDAO.class);
	static Logger logger = Logger.getLogger(TaFlashDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	

	private String defaultJPQLQuery = "select a from TaFlash a";
	public TaFlashDAO(){

	}
	
	@Override
	public List<TaFlash> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaFlash");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFlash> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void persist(TaFlash transientInstance) {
		logger.debug("persisting TaFlash instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaFlash persistentInstance) {
		logger.debug("removing TaFlash instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaFlash merge(TaFlash detachedInstance) {
		logger.debug("merging TaFlash instance");
		try {

			TaFlash result = entityManager.merge(detachedInstance);

			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaFlash findById(int id) {
		logger.debug("getting TaFlash instance with id: " + id);
		try {
			TaFlash instance = entityManager.find(TaFlash.class, id);
			for (TaLFlash l : instance.getLignes()) {
				for (TaLigneALigne ll : l.getTaLigneALignes()) {
					ll.getId();
				}
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaFlash findByCode(String code) {
		logger.debug("getting TaFlash instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaFlash a " +
					"  left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");			
			TaFlash instance = (TaFlash)query.getSingleResult();

			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	
	
	
//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}


	

	@Override
	public List<TaFlash> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFlash> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFlash> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFlash> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFlash value) throws Exception {
		BeanValidator<TaFlash> validator = new BeanValidator<TaFlash>(TaFlash.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFlash value, String propertyName)
			throws Exception {
		BeanValidator<TaFlash> validator = new BeanValidator<TaFlash>(TaFlash.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFlash transientInstance) {
		entityManager.detach(transientInstance);
	}
	


	public TaFlash findDocByLDoc(TaLFlash lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaFlash a " +
					"  join  a.lignes l"+
					" where l=:ldoc ");
			query.setParameter("ldoc", lDoc);
			TaFlash instance = (TaFlash)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}	

}


	


