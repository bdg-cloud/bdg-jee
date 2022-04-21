package fr.legrain.pointLgr.dao;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.lib.data.ExceptLgr;


/**
 * Home object for domain model class TaAdresse.
 * @see fr.legrain.tiers.dao.TaAdresse
 * @author Hibernate Tools
 */
public class TaPourcGroupeDAO /*extends AbstractApplicationDAO<TaPourcGroupe>*/{

	//private static final Log logger = LogFactory.getLog(TaAdresseDAO.class);
	static Logger logger = Logger.getLogger(TaPourcGroupeDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaPourcGroupe a";
	
	public TaPourcGroupeDAO(){
//		this(null);
	}
	
//	public TaPourcGroupeDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTiersPoint.class.getSimpleName());
//		initChampId(TaPourcGroupe.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaPourcGroupe());
//	}
//
//	public TaPourcGroupe refresh(TaPourcGroupe detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaPourcGroupe.class, detachedInstance.getIdPourcGroupe());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaPourcGroupe transientInstance) {
		logger.debug("persisting TaPourcGroupe instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaPourcGroupe persistentInstance) {
		logger.debug("removing TaPourcGroupe instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaPourcGroupe merge(TaPourcGroupe detachedInstance) {
		logger.debug("merging TaTypeMouvPoint instance");
		try {
			TaPourcGroupe result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaPourcGroupe findById(int id) {
		logger.debug("getting TaPourcGroupe instance with id: " + id);
		try {
			TaPourcGroupe instance = entityManager.find(TaPourcGroupe.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public  TaPourcGroupe findByCodeAbonnementGoupe(String code, String groupe) {
		logger.debug("instance TaPourcGroupe with codeTAbonnement: " + code +" and groupe : "+groupe);
		try {
			if(!code.equals("")){
				
			Query query = entityManager.createQuery("select f from TaPourcGroupe f join f.taTAbonnement a join f.taFamilleTiers g where a.codeTAbonnement='"+
			code+"' and g.codeFamille='"+groupe+"'");
			TaPourcGroupe instance = (TaPourcGroupe)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public  List<TaPourcGroupe> findByCodeAbonnement(String code) {
		logger.debug("selectAll TaPourcGroupe with codeTAbonnement: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaPourcGroupe f join f.taTAbonnement a where a.codeTAbonnement='"+code+"'");
			List<TaPourcGroupe> l = query.getResultList();
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public  List<TaPourcGroupe> findByGroupe(String groupe) {
		logger.debug("selectAll TaPourcGroupe with groupe: " + groupe);
		try {
			if(!groupe.equals("")){
			Query query = entityManager.createQuery("select f from TaPourcGroupe f join f.taFamilleTiers g where g.codeFamille='"+groupe+"'");
			List<TaPourcGroupe> l = query.getResultList();
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public  TaPourcGroupe findByTAbonnementFamille(int idFamille,int idTAbonnement) {
		logger.debug("selectAll findByTAbonnementFamille ");
		try {
			if(idFamille!=0 && idTAbonnement!=0){
			Query query = entityManager.createQuery("select f from TaPourcGroupe f join f.taFamilleTiers g join f.taTAbonnement ta  where g.idFamille="+idFamille +
					" and ta.idTAbonnement="+idTAbonnement);
			TaPourcGroupe instance = (TaPourcGroupe) query.getSingleResult();
			logger.debug("get successful findByTAbonnementFamille");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
//	@Override
	public List<TaPourcGroupe> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaPourcGroupe");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPourcGroupe> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	
	public void ctrlSaisieSpecifique(TaComptePoint entity,String field) throws ExceptLgr {	
		
	}

}
