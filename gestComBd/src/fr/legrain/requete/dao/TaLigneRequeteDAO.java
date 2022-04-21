/**
 * TaRequeteDAO.java
 */
package fr.legrain.requete.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.gestCom.Appli.Const;

/**
 * @author Nicolas²
 *
 */
public class TaLigneRequeteDAO /*extends AbstractApplicationDAO<TaLigneRequete>*/ {
	
	//private static final Log logger = LogFactory.getLog(TaRequeteDAO.class);
	static Logger logger = Logger.getLogger(TaLigneRequeteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLigneRequete a";
	
	public TaLigneRequeteDAO(){
//		this(null);
	}
	
//	public TaLigneRequeteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaRequete.class.getSimpleName());
//		initChampId(TaLigneRequete.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLigneRequete());
//	}

//	public TaLigneRequete refresh(TaLigneRequete detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLigneRequete.class, detachedInstance.getId());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLigneRequete transientInstance) {
		logger.debug("persisting TaRequete instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLigneRequete persistentInstance) {
		logger.debug("removing TaRequete instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLigneRequete merge(TaLigneRequete detachedInstance) {
		logger.debug("merging TaRequete instance");
		try {
			TaLigneRequete result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLigneRequete findById(int id) {
		logger.debug("getting TaRequete instance with id: " + id);
		try {
			TaLigneRequete instance = entityManager.find(TaLigneRequete.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	


//	@Override
	public List<TaLigneRequete> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaRequete");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLigneRequete> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaLigneRequete> selectByIdRequete(int id) {
		// TODO Auto-generated method stub
		logger.debug("selectByIdRequete TaRequete");
		try {
			String JPQLQuery = "select a from TaLigneRequete a where a.rqt.idRqt='"+id+"' order by a.idGroupe, a.idLigne";
			Query ejbQuery = entityManager.createQuery(JPQLQuery);
			List<TaLigneRequete> l = ejbQuery.getResultList();
			logger.debug("selectByIdRequete successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectByIdRequete failed", re);
			throw re;
		}
	}
	
	

}
