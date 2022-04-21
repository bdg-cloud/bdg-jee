package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosBonReception;
import fr.legrain.documents.dao.IInfosBonReceptionDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaInfosBonReception.
 * @see fr.legrain.documents.dao.TaInfosBonReception
 * @author Hibernate Tools
 */
public class TaInfosBonReceptionDAO /*extends AbstractApplicationDAO<TaInfosBonReception>*/ implements IInfosBonReceptionDAO {

//	private static final Log log = LogFactory.getLog(TaInfosBonReceptionDAO.class);
	static Logger logger = Logger.getLogger(TaInfosBonReceptionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosBonReception a";

	public TaInfosBonReceptionDAO(){
//		this(null);
	}
	
//	public TaInfosBonReceptionDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosBonReception.class.getSimpleName());
//		initChampId(TaInfosBonReception.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosBonReception());
//	}
	
//	public TaInfosBonReception refresh(TaInfosBonReception detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosBonReception.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosBonReception transientInstance) {
		logger.debug("persisting TaInfosBonReception instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosBonReception persistentInstance) {
		logger.debug("removing TaInfosBonReception instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosBonReception merge(TaInfosBonReception detachedInstance) {
		logger.debug("merging TaInfosBonReception instance");
		try {
			TaInfosBonReception result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosBonReception findById(int id) {
		logger.debug("getting TaInfosBonReception instance with id: " + id);
		try {
			TaInfosBonReception instance = entityManager.find(TaInfosBonReception.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaInfosBonReception findByCodeBonReception(String code) {
		logger.debug("getting TaInfosBonReception instance with code document : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosBonReception a where a.taDocument.codeDocument='"+code+"'");
				TaInfosBonReception instance = (TaInfosBonReception)query.getSingleResult();
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
	public List<TaInfosBonReception> selectAll() {
		logger.debug("selectAll TaInfosBonReception");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosBonReception> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaInfosBonReception> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfosBonReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfosBonReception> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfosBonReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfosBonReception value) throws Exception {
		BeanValidator<TaInfosBonReception> validator = new BeanValidator<TaInfosBonReception>(TaInfosBonReception.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInfosBonReception value, String propertyName)
			throws Exception {
		BeanValidator<TaInfosBonReception> validator = new BeanValidator<TaInfosBonReception>(TaInfosBonReception.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInfosBonReception transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaInfosBonReception findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
