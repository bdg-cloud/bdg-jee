package fr.legrain.articles.champsupp.dao;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;
import fr.legrain.lib.data.ExceptLgr;

/**
 * Home object for domain model class TaChampSuppArt.
 * @see fr.legrain.articles.dao.TaChampSuppArt
 * @author Hibernate Tools
 */
public class TaChampSuppArtDAO /*extends AbstractApplicationDAO<TaChampSuppArt>*/ {

	//private static final Log logger = LogFactory.getLog(TaChampSuppArtDAO.class);
	static Logger logger = Logger.getLogger(TaChampSuppArtDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaChampSuppArt a";
	
	public TaChampSuppArtDAO(){
//		this(null);
	}
	
//	public TaChampSuppArtDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaChampSuppArt.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaChampSuppArt());
//	}
	

	protected void persist(TaChampSuppArt transientInstance) {
		logger.debug("persisting TaChampSuppArt instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	protected void remove(TaChampSuppArt persistentInstance) {
		logger.debug("removing TaChampSuppArt instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	protected TaChampSuppArt merge(TaChampSuppArt detachedInstance) {
		logger.debug("merging TaChampSuppArt instance");
		try {
			TaChampSuppArt result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaChampSuppArt findById(int id) {
		logger.debug("getting TaChampSuppArt instance with id: " + id);
		try {
			TaChampSuppArt instance = entityManager.find(TaChampSuppArt.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	public TaChampSuppArt findByIdEM(int id) {
//		logger.debug("getting TaChampSuppArt instance with id: " + id);
//		try {
//			EntityManager em = new JPABdLgr().entityManager;
//			TaChampSuppArt instance = em.find(TaChampSuppArt.class, id);
////			em.close();
//			instance=entityManager.merge(instance);
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
//	public TaChampSuppArt findByCode(String code) {
//		logger.debug("getting TaChampSuppArt instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select a from TaChampSuppArt a where a.codeArticle='"+code+"'");
//			TaChampSuppArt instance = (TaChampSuppArt)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}	
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaChampSuppArt> selectAll() {
		logger.debug("selectAll TaChampSuppArt");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaChampSuppArt> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaChampSuppArt refresh(TaChampSuppArt detachedInstance) {
		logger.debug("refresh instance");
		try {			
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaChampSuppArt.class, detachedInstance.getIdChampSuppArt());
		    return detachedInstance;		    
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	

	
	public void ctrlSaisieSpecifique(TaChampSuppArt entity,String field) throws ExceptLgr {	
//		try {
//			if(field.equals(
//					Const.C_CODE_UNITE)){	
//			}
//			if(field.equals(Const.C_CODE_TVA )||field.equals(Const.C_CODE_T_TVA )){
//				initCodeTTva(entity);
//				if(field.equals(Const.C_CODE_TVA ))
//					initPrixTTC(entity);
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//		}
	}
}
