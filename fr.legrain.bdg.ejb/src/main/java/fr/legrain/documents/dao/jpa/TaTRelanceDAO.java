package fr.legrain.documents.dao.jpa;

// Generated Mar 24, 2009 4:44:04 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.document.model.TaTRelance;
import fr.legrain.documents.dao.ITRelanceDAO;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;

/**
 * Home object for domain model class TaTLiens.
 * @see fr.legrain.tiers.dao.TaTLiens
 * @author Hibernate Tools
 */
public class TaTRelanceDAO /*extends AbstractApplicationDAO<TaTRelance>*/ implements ITRelanceDAO{

	//private static final Log logger = LogFactory.getLog(TaTLiensDAO.class);
	static Logger logger = Logger.getLogger(TaTRelanceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select a from TaTRelance a where a.typeLogiciel = 'OpenOffice'" ;
			//"order by actif,ordreTRelance";
	
	public TaTRelanceDAO() {
//		this(null);
	}
	
//	public TaTRelanceDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTRelance.class.getSimpleName());
//		initChampId(TaTRelance.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTRelance());
//	}
	
//	public TaTRelance refresh(TaTRelance detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaTRelance.class, detachedInstance.getIdTRelance());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTRelance transientInstance) {
		logger.debug("persisting TaTRelance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaTRelance persistentInstance) {
		logger.debug("removing TaTRelance instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaTRelance merge(TaTRelance detachedInstance) {
		logger.debug("merging TaTRelance instance");
		try {
			TaTRelance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaTRelance findById(int id) {
		logger.debug("getting TaTRelance instance with id: " + id);
		try {
			TaTRelance instance = entityManager.find(TaTRelance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTRelance findByCode(String code) {
		logger.debug("getting TaTRelance instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTRelance f where f.codeTRelance='"+code+"'");
			TaTRelance instance = (TaTRelance)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
//	@Override
	public List<TaTRelance> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTRelance");
		try {  //+" order by a.ordreTRelance"
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTRelance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public int maxOrdreRelance() {
		try {
			int ordreMax=0;
			Query query = entityManager.createQuery("select max(f.ordreTRelance) from TaTRelance f " +
					" where actif=1");
			if(query.getResultList().size()>0 && query.getResultList().get(0)!=null){
			ordreMax = (Integer)query.getResultList().get(0);
			}
			return ordreMax+1;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public Boolean OrdreRelanceUtilise(Integer ordre) {
		try {
			if(ordre==null)throw new RuntimeException();
			Query query = entityManager.createQuery("select f.ordreTRelance from TaTRelance f " +
					" where actif=1 and f.ordreTRelance="+ordre.intValue());
			if(query.getResultList().size()>0 && query.getResultList().get(0)!=null){
			return true;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public void RAZCheminVersion(String typeLogiciel) {
		try {
			List<TaTRelance> liste=selectAll();
			for (TaTRelance taTRelance : liste) {
				entityManager.getTransaction().begin();
				taTRelance.setActif(LibConversion.booleanToInt(taTRelance.getTypeLogiciel().equals(typeLogiciel)));
//				taTRelance.setCheminCorrespRelance(null);
//				taTRelance.setCheminModelRelance(null);
				taTRelance=merge(taTRelance);
				entityManager.getTransaction().commit();
//				taTRelance=refresh(taTRelance);
			}
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public TaTRelance taTRelanceSuperieur(int ordre) {
		try {
			Integer maxOrdre=null;
			Query query = entityManager.createQuery("select min(f.ordreTRelance) from TaTRelance f" +
					" where f.ordreTRelance > :ordreTRelance and actif=1");
			query.setParameter("ordreTRelance", ordre);
			if(query.getSingleResult()!=null){
				maxOrdre= (Integer)query.getSingleResult();
			}
			if(maxOrdre!=null){
				ordre=maxOrdre;
			}
			query = entityManager.createQuery("select f from TaTRelance f " +
					" where f.ordreTRelance = :ordreTRelance and actif=1");
			query.setParameter("ordreTRelance", ordre);
			if(query.getResultList()!=null && query.getResultList().size()>0)
				return (TaTRelance)query.getResultList().get(0);
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public void ctrlSaisieSpecifique(TaTRelance entity,String field) throws ExceptLgr {	
		
	}

	@Override
	public List<TaTRelance> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaTRelance> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaTRelance value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaTRelance value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaTRelance transientInstance) {
		// TODO Auto-generated method stub
		
	}
}
