package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaLPrelevement;
import fr.legrain.document.model.TaLProforma;
import fr.legrain.documents.dao.ILPrelevementDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLDevis.
 * @see fr.legrain.documents.dao.TaLDevis
 * @author Hibernate Tools
 */
public class TaLPrelevementDAO implements ILPrelevementDAO {

//	private static final Log log = LogFactory.getLog(TaLDevisDAO.class);
	static Logger logger = Logger.getLogger(TaLPrelevementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLPrelevement a";
	
	public TaLPrelevementDAO(){
//		this(null);
	}
	
//	public TaLPrelevementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLPrelevement.class.getSimpleName());
//		initChampId(TaLPrelevement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLPrelevement());
//	}
	
//	public TaLPrelevement refresh(TaLPrelevement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLPrelevement.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLPrelevement transientInstance) {
		logger.debug("persisting TaLPrelevement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLPrelevement persistentInstance) {
		logger.debug("removing TaLPrelevement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLPrelevement merge(TaLPrelevement detachedInstance) {
		logger.debug("merging TaLPrelevement instance");
		try {
			TaLPrelevement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLPrelevement findById(int id) {
		logger.debug("getting TaLPrelevement instance with id: " + id);
		try {
			TaLPrelevement instance = entityManager.find(TaLPrelevement.class, id);
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
	public List<TaLPrelevement> selectAll() {
		logger.debug("selectAll TaLPrelevement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLPrelevement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}


	@Override
	public TaLPrelevement findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLPrelevement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLPrelevement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLPrelevement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLPrelevement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLPrelevement value) throws Exception {
		BeanValidator<TaLPrelevement> validator = new BeanValidator<TaLPrelevement>(TaLPrelevement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLPrelevement value, String propertyName) throws Exception {
		BeanValidator<TaLPrelevement> validator = new BeanValidator<TaLPrelevement>(TaLPrelevement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLPrelevement transientInstance) {
		entityManager.detach(transientInstance);
		
	}
}
