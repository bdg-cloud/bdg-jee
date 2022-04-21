package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaLApporteur;
import fr.legrain.documents.dao.ILApporteurDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLApporteur.
 * @see fr.legrain.documents.dao.TaLApporteur
 * @author Hibernate Tools
 */
public class TaLApporteurDAO /*extends AbstractApplicationDAO<TaLApporteur>*/implements ILApporteurDAO {

//	private static final Log log = LogFactory.getLog(TaLApporteurDAO.class);
	static Logger logger = Logger.getLogger(TaLApporteurDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	boolean  raffraichir=false;
	private String defaultJPQLQuery = "select a from TaLApporteur a";
	
	public TaLApporteurDAO(){
//		this(null);
	}
	
//	public TaLApporteurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLApporteur.class.getSimpleName());
//		initChampId(TaLApporteur.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLApporteur());
//	}
	
//	public TaLApporteur refresh(TaLApporteur detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLApporteur.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLApporteur transientInstance) {
		logger.debug("persisting TaLApporteur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLApporteur persistentInstance) {
		logger.debug("removing TaLApporteur instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLApporteur merge(TaLApporteur detachedInstance) {
		logger.debug("merging TaLApporteur instance");
		try {
			TaLApporteur result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLApporteur findById(int id) {
		logger.debug("getting TaLApporteur instance with id: " + id);
		try {
			TaLApporteur instance = entityManager.find(TaLApporteur.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public void annuler(TaLApporteur persistentInstance) throws Exception{
		persistentInstance.setLegrain(false);
//		super.annuler(persistentInstance);
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLApporteur> selectAll() {
		logger.debug("selectAll TaLApporteur");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLApporteur> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaLApporteur findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLApporteur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLApporteur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}
	@Override
	public List<TaLApporteur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLApporteur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLApporteur value) throws Exception {
		BeanValidator<TaLApporteur> validator = new BeanValidator<TaLApporteur>(TaLApporteur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLApporteur value, String propertyName)
			throws Exception {
		BeanValidator<TaLApporteur> validator = new BeanValidator<TaLApporteur>(TaLApporteur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLApporteur transientInstance) {
		entityManager.detach(transientInstance);
	}
}
