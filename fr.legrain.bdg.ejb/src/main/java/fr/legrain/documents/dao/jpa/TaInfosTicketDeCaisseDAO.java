package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaInfosTicketDeCaisse;
import fr.legrain.documents.dao.IInfosFactureDAO;
import fr.legrain.documents.dao.IInfosTicketDeCaisseDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaInfosTicketDeCaisse.
 * @see fr.legrain.documents.dao.TaInfosTicketDeCaisse
 * @author Hibernate Tools
 */
public class TaInfosTicketDeCaisseDAO /*extends AbstractApplicationDAO<TaInfosTicketDeCaisse>*/ implements IInfosTicketDeCaisseDAO {

//	private static final Log log = LogFactory.getLog(TaInfosTicketDeCaisseDAO.class);
	static Logger logger = Logger.getLogger(TaInfosTicketDeCaisseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosTicketDeCaisse a";

	public TaInfosTicketDeCaisseDAO(){
//		this(null);
	}
	
//	public TaInfosTicketDeCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosTicketDeCaisse.class.getSimpleName());
//		initChampId(TaInfosTicketDeCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosTicketDeCaisse());
//	}
	
//	public TaInfosTicketDeCaisse refresh(TaInfosTicketDeCaisse detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosTicketDeCaisse.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosTicketDeCaisse transientInstance) {
		logger.debug("persisting TaInfosTicketDeCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaInfosTicketDeCaisse persistentInstance) {
		logger.debug("removing TaInfosTicketDeCaisse instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaInfosTicketDeCaisse merge(TaInfosTicketDeCaisse detachedInstance) {
		logger.debug("merging TaInfosTicketDeCaisse instance");
		try {
			TaInfosTicketDeCaisse result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaInfosTicketDeCaisse findById(int id) {
		logger.debug("getting TaInfosTicketDeCaisse instance with id: " + id);
		try {
			TaInfosTicketDeCaisse instance = entityManager.find(TaInfosTicketDeCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaInfosTicketDeCaisse findByCodeFacture(String code) {
		logger.debug("getting TaInfosTicketDeCaisse instance with code document : " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosTicketDeCaisse a where a.taDocument.codeDocument='"+code+"'");
				TaInfosTicketDeCaisse instance = (TaInfosTicketDeCaisse)query.getSingleResult();
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
	public List<TaInfosTicketDeCaisse> selectAll() {
		logger.debug("selectAll TaInfosTicketDeCaisse");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosTicketDeCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaInfosTicketDeCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInfosTicketDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInfosTicketDeCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInfosTicketDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInfosTicketDeCaisse value) throws Exception {
		BeanValidator<TaInfosTicketDeCaisse> validator = new BeanValidator<TaInfosTicketDeCaisse>(TaInfosTicketDeCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInfosTicketDeCaisse value, String propertyName)
			throws Exception {
		BeanValidator<TaInfosTicketDeCaisse> validator = new BeanValidator<TaInfosTicketDeCaisse>(TaInfosTicketDeCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInfosTicketDeCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaInfosTicketDeCaisse findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
