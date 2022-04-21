package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.documents.dao.IInfosAbonnementDAO;
import fr.legrain.documents.dao.IInfosDevisDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaInfosAbonnement.
 * @see fr.legrain.documents.dao.TaInfosAbonnement
 * @author Hibernate Tools
 */
public class TaInfosAbonnementDAO /*extends AbstractApplicationDAO<TaInfosAbonnement>*/ implements IInfosAbonnementDAO {

//	private static final Log log = LogFactory.getLog(TaInfosAbonnementDAO.class);
	static Logger logger = Logger.getLogger(TaInfosAbonnementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosAbonnement a";

	public TaInfosAbonnementDAO(){
//		this(null);
	}
	
//	public TaInfosAbonnementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosAbonnement.class.getSimpleName());
//		initChampId(TaInfosAbonnement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosAbonnement());
//	}
	
//	public TaInfosAbonnement refresh(TaInfosAbonnement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosAbonnement.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosAbonnement transientInstance) {
		logger.debug("persisting TaInfosAbonnement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosAbonnement persistentInstance) {
		logger.debug("removing TaInfosAbonnement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosAbonnement merge(TaInfosAbonnement detachedInstance) {
		logger.debug("merging TaInfosAbonnement instance");
		try {
			TaInfosAbonnement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosAbonnement findById(int id) {
		logger.debug("getting TaInfosAbonnement instance with id: " + id);
		try {
			TaInfosAbonnement instance = entityManager.find(TaInfosAbonnement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaInfosAbonnement findByCodeDevis(String code) {
		logger.debug("getting TaInfosAbonnement instance with code document : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosAbonnement a where a.taDocument.codeDocument='"+code+"'");
				TaInfosAbonnement instance = (TaInfosAbonnement)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (NoResultException re) {
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaInfosAbonnement> selectAll() {
		logger.debug("selectAll TaInfosAbonnement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosAbonnement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaInfosAbonnement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfosAbonnement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfosAbonnement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfosAbonnement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfosAbonnement value) throws Exception {
		BeanValidator<TaInfosAbonnement> validator = new BeanValidator<TaInfosAbonnement>(TaInfosAbonnement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInfosAbonnement value, String propertyName)
			throws Exception {
		BeanValidator<TaInfosAbonnement> validator = new BeanValidator<TaInfosAbonnement>(TaInfosAbonnement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInfosAbonnement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaInfosAbonnement findByCode(String code) {
		// TODO Auto-generated method stub
		return findByCodeDevis(code);
	}
}
