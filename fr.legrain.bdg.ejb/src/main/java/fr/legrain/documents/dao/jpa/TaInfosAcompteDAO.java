package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaInfosAcompte;
import fr.legrain.documents.dao.IInfosAcompteDAO;
import fr.legrain.documents.dao.IInfosDevisDAO;


/**
 * Home object for domain model class TaInfosAcompte.
 * @see fr.legrain.documents.dao.TaInfosAcompte
 * @author Hibernate Tools
 */
public class TaInfosAcompteDAO /*extends AbstractApplicationDAO<TaInfosAcompte>*/ implements IInfosAcompteDAO {

//	private static final Log log = LogFactory.getLog(TaInfosAcompteDAO.class);
	static Logger logger = Logger.getLogger(TaInfosAcompteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaInfosAcompte a";
	
	public TaInfosAcompteDAO(){
//		this(null);
	}
	
//	public TaInfosAcompteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaInfosAcompte.class.getSimpleName());
//		initChampId(TaInfosAcompte.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaInfosAcompte());
//	}
	
//	public TaInfosAcompte refresh(TaInfosAcompte detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaInfosAcompte.class, detachedInstance.getIdInfosDocument());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaInfosAcompte transientInstance) {
		logger.debug("persisting TaInfosAcompte instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaInfosAcompte persistentInstance) {
		logger.debug("removing TaInfosAcompte instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaInfosAcompte merge(TaInfosAcompte detachedInstance) {
		logger.debug("merging TaInfosAcompte instance");
		try {
			TaInfosAcompte result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaInfosAcompte findById(int id) {
		logger.debug("getting TaInfosAcompte instance with id: " + id);
		try {
			TaInfosAcompte instance = entityManager.find(TaInfosAcompte.class,
					id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaInfosAcompte findByCodeAcompte(String code) {
		logger.debug("getting TaInfosAcompte instance with code Document: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaInfosAcompte " +
						"a where a.taDocument.codeDocument='"+code+"'");
				TaInfosAcompte instance = (TaInfosAcompte)query.getSingleResult();
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
	public List<TaInfosAcompte> selectAll() {
		logger.debug("selectAll TaInfosAcompte");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInfosAcompte> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public TaInfosAcompte findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosAcompte> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaInfosAcompte> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaInfosAcompte value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaInfosAcompte value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaInfosAcompte transientInstance) {
		// TODO Auto-generated method stub
		
	}

}
