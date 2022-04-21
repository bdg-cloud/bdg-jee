package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.documents.dao.IInfosFactureDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaInfosFacture.
 * @see fr.legrain.documents.dao.TaInfosFacture
 * @author Hibernate Tools
 */
public class TaInfosFactureDAO /*extends AbstractApplicationDAO<TaInfosFacture>*/ implements IInfosFactureDAO {

//	private static final Log log = LogFactory.getLog(TaInfosFactureDAO.class);
	static Logger logger = Logger.getLogger(TaInfosFactureDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosFacture a";

	public TaInfosFactureDAO(){
//		this(null);
	}
	
//	public TaInfosFactureDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosFacture.class.getSimpleName());
//		initChampId(TaInfosFacture.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosFacture());
//	}
	
//	public TaInfosFacture refresh(TaInfosFacture detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosFacture.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosFacture transientInstance) {
		logger.debug("persisting TaInfosFacture instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosFacture persistentInstance) {
		logger.debug("removing TaInfosFacture instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosFacture merge(TaInfosFacture detachedInstance) {
		logger.debug("merging TaInfosFacture instance");
		try {
			TaInfosFacture result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosFacture findById(int id) {
		logger.debug("getting TaInfosFacture instance with id: " + id);
		try {
			TaInfosFacture instance = entityManager.find(TaInfosFacture.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaInfosFacture findByCodeFacture(String code) {
		logger.debug("getting TaInfosFacture instance with code document : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosFacture a where a.taDocument.codeDocument='"+code+"'");
				TaInfosFacture instance = (TaInfosFacture)query.getSingleResult();
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
	public List<TaInfosFacture> selectAll() {
		logger.debug("selectAll TaInfosFacture");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosFacture> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaInfosFacture> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfosFacture> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfosFacture> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfosFacture> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfosFacture value) throws Exception {
		BeanValidator<TaInfosFacture> validator = new BeanValidator<TaInfosFacture>(TaInfosFacture.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInfosFacture value, String propertyName)
			throws Exception {
		BeanValidator<TaInfosFacture> validator = new BeanValidator<TaInfosFacture>(TaInfosFacture.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInfosFacture transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaInfosFacture findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
