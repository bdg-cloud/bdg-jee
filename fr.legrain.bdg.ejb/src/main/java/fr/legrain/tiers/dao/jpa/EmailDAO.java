package fr.legrain.tiers.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.tiers.dao.IEmailDAO;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaEmail.
 * @see fr.legrain.tiers.model.old.TaEmail
 * @author Hibernate Tools
 */
public class EmailDAO implements IEmailDAO {

	//private static final Log logger = LogFactory.getLog(TaEmailDAO.class);
	static Logger logger = Logger.getLogger(EmailDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaEmail a";
	
	public EmailDAO() {
	}

//	public TaEmailDAO(String defaultJPQLQuery) {
//		champIdTable=ctrlGeneraux.getID_TABLE(TaEmail.class.getSimpleName());
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaEmail());
//	}

//	public TaEmail refresh(TaEmail detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaEmail.class, detachedInstance.getIdEmail());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaEmail transientInstance) {
		logger.debug("persisting TaEmail instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaEmail persistentInstance) {
		logger.debug("removing TaEmail instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaEmail merge(TaEmail detachedInstance) {
		logger.debug("merging TaEmail instance");
		try {
			TaEmail result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaEmail findById(int id) {
		logger.debug("getting TaEmail instance with id: " + id);
		try {
			TaEmail instance = entityManager.find(TaEmail.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Recherche les emails qui ont été importés d'un autre programme
	 * @param origineImport
	 * @param idImport - id externe (dans le programme d'origine)
	 * @return
	 */
	public List<TaEmail> rechercheParImport(String origineImport, String idImport) {
		Query query = entityManager.createNamedQuery(TaEmail.QN.FIND_BY_IMPORT);
		query.setParameter("origineImport", origineImport);
		query.setParameter("idImport", idImport);
		List<TaEmail> l = query.getResultList();

		return l;
	}

	@Override
	public List<TaEmail> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaEmail");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEmail> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEmail> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEmail> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEmail value) throws Exception {
		BeanValidator<TaEmail> validator = new BeanValidator<TaEmail>(TaEmail.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEmail value, String propertyName) throws Exception {
		BeanValidator<TaEmail> validator = new BeanValidator<TaEmail>(TaEmail.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEmail transientInstance) {
		entityManager.detach(transientInstance);
	}
	
//	public void ctrlSaisieSpecifique(TaEmail entity,String field) throws ExceptLgr {	
//		
//	}

	@Override
	public TaEmail findByCode(String code) {
		logger.debug("getting TaEmail instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaEmail a where a.adresseEmail='"+code+"'");
				TaEmail instance = (TaEmail)query.getSingleResult();
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
	
	public List<TaEmailDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaEmail.QN.FIND_ALL_LIGHT);
			@SuppressWarnings("unchecked")
			List<TaEmailDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}
