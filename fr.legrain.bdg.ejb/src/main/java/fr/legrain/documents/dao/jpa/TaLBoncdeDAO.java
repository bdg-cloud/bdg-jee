package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaLBoncde;
import fr.legrain.documents.dao.ILBoncdeDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLDevis.
 * @see fr.legrain.documents.dao.TaLDevis
 * @author Hibernate Tools
 */
public class TaLBoncdeDAO /*extends AbstractApplicationDAO<TaLBoncde>*/ implements ILBoncdeDAO{

//	private static final Log log = LogFactory.getLog(TaLDevisDAO.class);
	static Logger logger = Logger.getLogger(TaLBoncdeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLBoncde a";
	
	public TaLBoncdeDAO(){
//		this(null);
	}
	
//	public TaLBoncdeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLBoncde.class.getSimpleName());
//		initChampId(TaLBoncde.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLBoncde());
//	}
	
//	public TaLBoncde refresh(TaLBoncde detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLBoncde.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLBoncde transientInstance) {
		logger.debug("persisting TaLBoncde instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLBoncde persistentInstance) {
		logger.debug("removing TaLBoncde instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLBoncde merge(TaLBoncde detachedInstance) {
		logger.debug("merging TaLBoncde instance");
		try {
			TaLBoncde result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLBoncde findById(int id) {
		logger.debug("getting TaLBoncde instance with id: " + id);
		try {
			TaLBoncde instance = entityManager.find(TaLBoncde.class, id);
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
	public List<TaLBoncde> selectAll() {
		logger.debug("selectAll TaLBoncde");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLBoncde> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaLBoncde findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLBoncde> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLBoncde> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLBoncde> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLBoncde> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLBoncde value) throws Exception {
		BeanValidator<TaLBoncde> validator = new BeanValidator<TaLBoncde>(TaLBoncde.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLBoncde value, String propertyName)
			throws Exception {
		BeanValidator<TaLBoncde> validator = new BeanValidator<TaLBoncde>(TaLBoncde.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLBoncde transientInstance) {
		entityManager.detach(transientInstance);
	}
}
