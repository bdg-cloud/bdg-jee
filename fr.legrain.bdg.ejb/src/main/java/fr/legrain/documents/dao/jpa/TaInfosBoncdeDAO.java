package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosBoncde;
import fr.legrain.document.model.TaInfosDevis;
import fr.legrain.documents.dao.IInfosBoncdeDAO;
import fr.legrain.documents.dao.IInfosDevisDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaInfosDevis.
 * @see fr.legrain.documents.dao.TaInfosDevis
 * @author Hibernate Tools
 */
public class TaInfosBoncdeDAO /*extends AbstractApplicationDAO<TaInfosBoncde>*/ implements IInfosBoncdeDAO {

//	private static final Log log = LogFactory.getLog(TaInfosDevisDAO.class);
	static Logger logger = Logger.getLogger(TaInfosBoncdeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosBoncde a";
	
	public TaInfosBoncdeDAO(){
//		this(null);
	}
	
//	public TaInfosBoncdeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosBoncde.class.getSimpleName());
//		initChampId(TaInfosBoncde.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosBoncde());
//	}
	
//	public TaInfosBoncde refresh(TaInfosBoncde detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosBoncde.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosBoncde transientInstance) {
		logger.debug("persisting TaInfosBoncde instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosBoncde persistentInstance) {
		logger.debug("removing TaInfosBoncde instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosBoncde merge(TaInfosBoncde detachedInstance) {
		logger.debug("merging TaInfosBoncde instance");
		try {
			TaInfosBoncde result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosBoncde findById(int id) {
		logger.debug("getting TaInfosBoncde instance with id: " + id);
		try {
			TaInfosBoncde instance = entityManager.find(TaInfosBoncde.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaInfosBoncde findByCodeBoncde(String code) {
		logger.debug("getting TaInfosBoncde instance with code InfosBoncde : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosBoncde a where a.taDocument.codeDocument='"+code+"'");
				TaInfosBoncde instance = (TaInfosBoncde)query.getSingleResult();
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
	public List<TaInfosBoncde> selectAll() {
		logger.debug("selectAll TaInfosBoncde");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosBoncde> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	
	
	/*******************/
	

	@Override
	public TaInfosBoncde findByCode(String code) {
		return findByCodeBoncde(code);
	}

	@Override
	public List<TaInfosBoncde> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfosBoncde> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfosBoncde> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfosBoncde> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfosBoncde value) throws Exception {
		BeanValidator<TaInfosBoncde> validator = new BeanValidator<TaInfosBoncde>(TaInfosBoncde.class);
		return validator.validate(value);
	}
	@Override
	public boolean validateField(TaInfosBoncde value, String propertyName) throws Exception {
		BeanValidator<TaInfosBoncde> validator = new BeanValidator<TaInfosBoncde>(TaInfosBoncde.class);
		return validator.validateField(value,propertyName);
	
	}

	@Override
	public void detach(TaInfosBoncde transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
