package fr.legrain.article.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IRTaTitreTransportDAO;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaRTaTitreTransport.
 * @see fr.legrain.articles.dao.TaRTaTitreTransport
 * @author Hibernate Tools
 */
public class RTaTitreTransportDAO implements IRTaTitreTransportDAO {

	private static final Log logger = LogFactory.getLog(RTaTitreTransportDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select p from TaRTaTitreTransport p";
	
	public RTaTitreTransportDAO(){
//		this(null);
	}
	
//	public TaRTaTitreTransportDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaRTaTitreTransport.class.getSimpleName());
//		initChampId(TaRTaTitreTransport.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRTaTitreTransport());
//	}

	public void persist(TaRTaTitreTransport transientInstance) {
		logger.debug("persisting TaRTaTitreTransport instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
//	public TaRTaTitreTransport refresh(TaRTaTitreTransport detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaRTaTitreTransport.class, detachedInstance.getIdRTitreTransport());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaRTaTitreTransport persistentInstance) {
		logger.debug("removing TaRTaTitreTransport instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaRTaTitreTransport merge(TaRTaTitreTransport detachedInstance) {
		logger.debug("merging TaRTaTitreTransport instance");
		try {
			TaRTaTitreTransport result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaRTaTitreTransport findById(int id) {
		logger.debug("getting TaRTaTitreTransport instance with id: " + id);
		try {
			TaRTaTitreTransport instance = entityManager.find(TaRTaTitreTransport.class,
					id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaRTaTitreTransport findByIdArticle(int idArticle) {
		logger.debug("getting TaRTaTitreTransport instance with idArticle: " + idArticle);
		try {
			//Query ejbQuery = entityManager.createQuery("select p from TaRTaTitreTransport p.taArticle.idArticle = "+ idArticle);
			TaRTaTitreTransport instance = null;
			Query ejbQuery = entityManager.createQuery("select p from TaRTaTitreTransport p join p.taArticle a left join fetch p.taTitreTransport t  where a.idArticle = "+ idArticle);
			
			List<TaRTaTitreTransport> l = ejbQuery.getResultList();
			if(!l.isEmpty())
				return instance = l.get(0);
			
			//TaRTaTitreTransport instance = (TaRTaTitreTransport)ejbQuery.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaRTaTitreTransport> selectAll() {
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRTaTitreTransport> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaRTaTitreTransport> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRTaTitreTransport> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRTaTitreTransport> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRTaTitreTransport> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRTaTitreTransport value) throws Exception {
		BeanValidator<TaRTaTitreTransport> validator = new BeanValidator<TaRTaTitreTransport>(TaRTaTitreTransport.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRTaTitreTransport value, String propertyName) throws Exception {
		BeanValidator<TaRTaTitreTransport> validator = new BeanValidator<TaRTaTitreTransport>(TaRTaTitreTransport.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRTaTitreTransport transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaRTaTitreTransport findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
