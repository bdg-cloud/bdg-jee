package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaLDevis;
import fr.legrain.documents.dao.ILDevisDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLDevis.
 * @see fr.legrain.documents.dao.TaLDevis
 * @author Hibernate Tools
 */
public class TaLDevisDAO /*extends AbstractApplicationDAO<TaLDevis>*/ implements ILDevisDAO{

//	private static final Log log = LogFactory.getLog(TaLDevisDAO.class);
	static Logger logger = Logger.getLogger(TaLDevisDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLDevis a";
	
	public TaLDevisDAO(){
//		this(null);
	}
	
//	public TaLDevisDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLDevis.class.getSimpleName());
//		initChampId(TaLDevis.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLDevis());
//	}
	
//	public TaLDevis refresh(TaLDevis detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLDevis.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLDevis transientInstance) {
		logger.debug("persisting TaLDevis instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLDevis persistentInstance) {
		logger.debug("removing TaLDevis instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLDevis merge(TaLDevis detachedInstance) {
		logger.debug("merging TaLDevis instance");
		try {
			TaLDevis result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLDevis findById(int id) {
		logger.debug("getting TaLDevis instance with id: " + id);
		try {
			TaLDevis instance = entityManager.find(TaLDevis.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLDevis> selectAll() {
		logger.debug("selectAll TaLDevis");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLDevis> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaLDevis findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLDevis> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLDevis> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLDevis> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLDevis> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLDevis value) throws Exception {
		BeanValidator<TaLDevis> validator = new BeanValidator<TaLDevis>(TaLDevis.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLDevis value, String propertyName)
			throws Exception {
		BeanValidator<TaLDevis> validator = new BeanValidator<TaLDevis>(TaLDevis.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLDevis transientInstance) {
		entityManager.detach(transientInstance);
	}
}
