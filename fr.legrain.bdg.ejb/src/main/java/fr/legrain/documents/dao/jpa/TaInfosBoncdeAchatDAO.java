package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosBoncdeAchat;
import fr.legrain.documents.dao.IInfosBoncdeAchatDAO;
import fr.legrain.validator.BeanValidator;


/**
 * Home object for domain model class TaInfosDevis.
 * @see fr.legrain.documents.dao.TaInfosDevis
 * @author Hibernate Tools
 */
public class TaInfosBoncdeAchatDAO /*extends AbstractApplicationDAO<TaInfosBoncdeAchat>*/ implements IInfosBoncdeAchatDAO {

//	private static final Log log = LogFactory.getLog(TaInfosDevisDAO.class);
	static Logger logger = Logger.getLogger(TaInfosBoncdeAchatDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosBoncdeAchat a";
	
	public TaInfosBoncdeAchatDAO(){
//		this(null);
	}
	
//	public TaInfosBoncdeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosBoncdeAchat.class.getSimpleName());
//		initChampId(TaInfosBoncdeAchat.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosBoncdeAchat());
//	}
	
//	public TaInfosBoncdeAchat refresh(TaInfosBoncdeAchat detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosBoncdeAchat.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosBoncdeAchat transientInstance) {
		logger.debug("persisting TaInfosBoncdeAchat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosBoncdeAchat persistentInstance) {
		logger.debug("removing TaInfosBoncdeAchat instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosBoncdeAchat merge(TaInfosBoncdeAchat detachedInstance) {
		logger.debug("merging TaInfosBoncdeAchat instance");
		try {
			TaInfosBoncdeAchat result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosBoncdeAchat findById(int id) {
		logger.debug("getting TaInfosBoncdeAchat instance with id: " + id);
		try {
			TaInfosBoncdeAchat instance = entityManager.find(TaInfosBoncdeAchat.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaInfosBoncdeAchat findByCodeBoncde(String code) {
		logger.debug("getting TaInfosBoncdeAchat instance with code InfosBoncde : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosBoncdeAchat a where a.taDocument.codeDocument='"+code+"'");
				TaInfosBoncdeAchat instance = (TaInfosBoncdeAchat)query.getSingleResult();
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
	public List<TaInfosBoncdeAchat> selectAll() {
		logger.debug("selectAll TaInfosBoncdeAchat");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosBoncdeAchat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	
	
	/*******************/
	

	@Override
	public TaInfosBoncdeAchat findByCode(String code) {
		return findByCodeBoncde(code);
	}

	@Override
	public List<TaInfosBoncdeAchat> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfosBoncdeAchat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfosBoncdeAchat> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfosBoncdeAchat> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfosBoncdeAchat value) throws Exception {
		BeanValidator<TaInfosBoncdeAchat> validator = new BeanValidator<TaInfosBoncdeAchat>(TaInfosBoncdeAchat.class);
		return validator.validate(value);
	}
	@Override
	public boolean validateField(TaInfosBoncdeAchat value, String propertyName) throws Exception {
		BeanValidator<TaInfosBoncdeAchat> validator = new BeanValidator<TaInfosBoncdeAchat>(TaInfosBoncdeAchat.class);
		return validator.validateField(value,propertyName);
	
	}

	@Override
	public void detach(TaInfosBoncdeAchat transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
