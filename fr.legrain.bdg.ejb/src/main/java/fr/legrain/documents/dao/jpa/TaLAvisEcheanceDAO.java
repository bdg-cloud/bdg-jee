package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.documents.dao.ILAvisEcheanceDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLAvisEcheance.
 * @see fr.legrain.documents.dao.TaLAvisEcheance
 * @author Hibernate Tools
 */
public class TaLAvisEcheanceDAO /*extends AbstractApplicationDAO<TaLAvisEcheance>*/implements ILAvisEcheanceDAO {

//	private static final Log log = LogFactory.getLog(TaLAvisEcheanceDAO.class);
	static Logger logger = Logger.getLogger(TaLAvisEcheanceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLAvisEcheance a";
	
	public TaLAvisEcheanceDAO(){
//		this(null);
	}
	
//	public TaLAvisEcheanceDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLAvisEcheance.class.getSimpleName());
//		initChampId(TaLAvisEcheance.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLAvisEcheance());
//	}
//	public TaLAvisEcheance refresh(TaLAvisEcheance detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLAvisEcheance.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLAvisEcheance transientInstance) {
		logger.debug("persisting TaLAvisEcheance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLAvisEcheance persistentInstance) {
		logger.debug("removing TaLAvisEcheance instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLAvisEcheance merge(TaLAvisEcheance detachedInstance) {
		logger.debug("merging TaLAvisEcheance instance");
		try {
			TaLAvisEcheance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLAvisEcheance findById(int id) {
		logger.debug("getting TaLAvisEcheance instance with id: " + id);
		try {
			TaLAvisEcheance instance = entityManager.find(TaLAvisEcheance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaLAvisEcheance> findAllByIdEcheance(Integer id){
		logger.debug(" findAllByIdEcheance");
		try {
			String jpql = ""
					+ "select lae from TaLAvisEcheance lae "
					+ " join lae.taLEcheance e"
					+ " where e.idLEcheance = :id";

			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLAvisEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllByIdEcheance successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByIdEcheance failed", re);
			throw re;
		}
	}
	
	public List<TaLAvisEcheance> findAllByIdEcheanceSansLigneCom(Integer id){
		logger.debug(" findAllByIdEcheance");
		try {
			String jpql = ""
					+ "select lae from TaLAvisEcheance lae "
					+ " join lae.taLEcheance e"
					+ " where e.idLEcheance = :id and lae.taArticle is not null";

			
			Query ejbQuery = entityManager.createQuery(jpql);
			ejbQuery.setParameter("id", id);
			List<TaLAvisEcheance> l = ejbQuery.getResultList();
			logger.debug("findAllByIdEcheance successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAllByIdEcheance failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLAvisEcheance> selectAll() {
		logger.debug("selectAll TaLAvisEcheance");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLAvisEcheance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaLAvisEcheance findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLAvisEcheance> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLAvisEcheance> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLAvisEcheance> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLAvisEcheance> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLAvisEcheance value) throws Exception {
		BeanValidator<TaLAvisEcheance> validator = new BeanValidator<TaLAvisEcheance>(TaLAvisEcheance.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLAvisEcheance value, String propertyName)
			throws Exception {
		BeanValidator<TaLAvisEcheance> validator = new BeanValidator<TaLAvisEcheance>(TaLAvisEcheance.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLAvisEcheance transientInstance) {
		entityManager.detach(transientInstance);
	}
}
