package fr.legrain.abonnement.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.abonnement.dao.jpa.IJourRelanceDAO;
import fr.legrain.abonnement.dto.TaJourRelanceDTO;
import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.abonnement.stripe.dto.TaStripePlanDTO;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;

public class JourRelanceDAO implements IJourRelanceDAO{
	
	private static final Log logger = LogFactory.getLog(JourRelanceDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	
	private String defaultJPQLQuery = "select f from TaJourRelance f order by f.idJourRelance";
	
	public JourRelanceDAO(){
//		this(null);
	}

	public void persist(TaJourRelance transientInstance) {
		logger.debug("persisting TaJourRelance instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaJourRelance persistentInstance) {
		logger.debug("removing TaJourRelance instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdJourRelance()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaJourRelance merge(TaJourRelance detachedInstance) {
		logger.debug("merging TaJourRelance instance");
		try {
			TaJourRelance result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	

	@Override
	public TaJourRelance findById(int id) {
		logger.debug("getting TaJourRelance instance with id: " + id);
		try {
			TaJourRelance instance = entityManager.find(TaJourRelance.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
//	public TaJourRelance findByCode(String code) {
//		logger.debug("getting TaJourRelance instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaJourRelance f where f.codeJourRelance='"+code+"'");
//			TaJourRelance instance = (TaJourRelance)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}

	@Override
	public List<TaJourRelance> selectAll() {
		logger.debug("selectAll TaJourRelance");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaJourRelance> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	@Override
	public List<TaJourRelance> findWithNamedQuery(String queryName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaJourRelance> findWithJPQLQuery(String JPQLquery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validate(TaJourRelance value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateField(TaJourRelance value, String propertyName) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void detach(TaJourRelance transientInstance) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public List<TaJourRelanceDTO> findByCodeLight(String code) {
//		logger.debug("getting TaJourRelance instance with code: " + code);
//		try {
//			Query query = null;
//
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaJourRelance.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter("codeJourRelance", "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaJourRelance.QN.FIND_ALL_LIGHT);
//			}
//
//			List<TaJourRelanceDTO> l = query.getResultList();
//			//		List<Object[]> lm = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	public List<TaJourRelanceDTO> findByIdArticleDTO(int idArticle){
		try {
			Query query = null;
			
			query = entityManager.createQuery("select new fr.legrain.abonnement.dto.TaJourRelanceDTO("
					+ " f.idJourRelance, f.taArticle.codeArticle, f.taArticle.idArticle,f.taArticle.libellecArticle, f.nbJour)"	
					+ "  from TaJourRelance f where f.taArticle.idArticle = :idArticle");
			
			query.setParameter("idArticle", idArticle);
	
			List<TaJourRelanceDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
	
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	@Override
	public List<TaJourRelanceDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaJourRelance.QN.FIND_ALL_LIGHT);
			List<TaJourRelanceDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public TaJourRelance findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaJourRelanceDTO> findByCodeLight(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
