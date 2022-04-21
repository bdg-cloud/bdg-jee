package fr.legrain.documents.dao.jpa;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaTDoc;
import fr.legrain.document.model.TaTEtat;
import fr.legrain.documents.dao.IEtatDAO;
import fr.legrain.tiers.dao.ITDocDAO;
import fr.legrain.tiers.dao.ITEtatDAO;

/**
 * Home object for domain model class TaEtat.
 * @see fr.legrain.documents.dao.TaEtat
 * @author Hibernate Tools
 */
public class TaEtatDAO implements IEtatDAO {

	static Logger logger = Logger.getLogger(TaEtatDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	protected @Inject ITDocDAO taTDocDao;
	protected @Inject ITEtatDAO taTEtatDao;
	
	
	private String defaultJPQLQuery = "select a from TaEtat a order by a.codeEtat";
	
	public TaEtatDAO(){
//		this(null);
	}
	
//	public TaEtatDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaEtat.class.getSimpleName());
//		initChampId(TaEtat.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaEtat());
//	}
	
//	public TaEtat refresh(TaEtat detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaEtat.class, detachedInstance.getIdTDoc());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaEtat transientInstance) {
		logger.debug("persisting TaEtat instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaEtat persistentInstance) {
		logger.debug("removing TaEtat instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaEtat merge(TaEtat detachedInstance) {
		logger.debug("merging TaEtat instance");
		try {
			TaEtat result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaEtat findById(int id) {
		logger.debug("getting TaEtat instance with id: " + id);
		try {
			TaEtat instance = entityManager.find(TaEtat.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	
	@Override
	public List<TaEtat> findByIdentifiantAndTypeEtat( String identifiant,String typeEtat) {
		TaTEtat taTEtat = null;
		try {
			if(typeEtat==null)typeEtat="%";
			String requete="select a from TaEtat a join a.taTEtat et where "
					+ "  et.codeTEtat like '"+typeEtat+"' and a.identifiant like ('"+identifiant+"%')";
			

				Query query = entityManager.createQuery(requete);
				List<TaEtat> l = query.getResultList();
				logger.debug("get successful");
				return l;

		} catch (RuntimeException re) {
			//			logger.error("get failed", re);
			//			throw re;
			return null;
		}
	}

	


	public TaEtat findByCode(String code) {
		logger.debug("getting TaEtat instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaEtat a where a.codeEtat='"+code+"'");
			TaEtat instance = (TaEtat)query.getSingleResult();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaEtat> selectAll() {
		logger.debug("selectAll TaEtat");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEtat> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEtat> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaEtat> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaEtat value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaEtat value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaEtat transientInstance) {
		// TODO Auto-generated method stub
		
	}

}
