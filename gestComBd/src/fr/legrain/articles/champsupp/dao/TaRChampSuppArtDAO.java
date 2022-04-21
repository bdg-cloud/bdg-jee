package fr.legrain.articles.champsupp.dao;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;

/**
 * Home object for domain model class TaRChampSuppArt.
 * @see fr.legrain.documents.dao.TaRChampSuppArt
 * @author Hibernate Tools
 */
public class TaRChampSuppArtDAO /*extends AbstractApplicationDAO<TaRChampSuppArt>*/ {

//	private static final Log log = LogFactory.getLog(TaRChampSuppArtDAO.class);
	static Logger logger = Logger.getLogger(TaRChampSuppArtDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaRChampSuppArt a";
	
	public TaRChampSuppArtDAO(){
//		this(null);
	}
	
//	public TaRChampSuppArtDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaRChampSuppArt.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRChampSuppArt());
//	}
	
	public TaRChampSuppArt refresh(TaRChampSuppArt detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaRChampSuppArt.class, detachedInstance.getIdRChampSuppArt());
		    return detachedInstance;

		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	public void persist(TaRChampSuppArt transientInstance) {
		logger.debug("persisting TaRChampSuppArt instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRChampSuppArt persistentInstance) {
		logger.debug("removing TaRChampSuppArt instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRChampSuppArt merge(TaRChampSuppArt detachedInstance) {
		logger.debug("merging TaRChampSuppArt instance");
		try {
			TaRChampSuppArt result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRChampSuppArt findById(int id) {
		logger.debug("getting TaRChampSuppArt instance with id: " + id);
		try {
			TaRChampSuppArt instance = entityManager.find(TaRChampSuppArt.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
//	 */
//	public List<TaRChampSuppArt> selectAll(TaAcompte taAcompte) {
//		logger.debug("selectAll TaRChampSuppArt");
//		try {
//			if(taAcompte!=null){
//			Query ejbQuery = entityManager.createQuery("select a from TaRChampSuppArt a where a.taAcompte=?");
//			ejbQuery.setParameter(1, taAcompte);
//			List<TaRChampSuppArt> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
	
//	@Override
	public List<TaRChampSuppArt> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
