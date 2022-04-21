package fr.legrain.article.dao.jpa;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IGrMouvStockDAO;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.stock.dto.GrMouvStockDTO;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCatalogueWeb.
 * @see fr.legrain.articles.dao.TaCatalogueWeb
 * @author Hibernate Tools
 */
public class GrMouvStockDAO implements IGrMouvStockDAO{

	private static final Log logger = LogFactory.getLog(GrMouvStockDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaGrMouvStock p";
	
	public GrMouvStockDAO(){
//		this(null);
	}




	public void persist(TaGrMouvStock transientInstance) {
		logger.debug("persisting TaGrMouvStock instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
	public TaGrMouvStock refresh(TaGrMouvStock detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaGrMouvStock.class, detachedInstance.getIdGrMouvStock());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaGrMouvStock persistentInstance) {
		logger.debug("removing TaGrMouvStock instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaGrMouvStock merge(TaGrMouvStock detachedInstance) {
		logger.debug("merging TaGrMouvStock instance");
		try {
			TaGrMouvStock result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaGrMouvStock findById(int id) {
		logger.debug("getting TaGrMouvStock instance with id: " + id);
		try {
			TaGrMouvStock instance = entityManager.find(TaGrMouvStock.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaGrMouvStock findByCode(String codeInventaire) {
		logger.debug("getting TaGrMouvStock instance with code: " + codeInventaire);
		try {
			if(!codeInventaire.equals("")){
			Query query = entityManager.createQuery("select a from TaGrMouvStock a "
					+ " where (a.codeInventaire)='"+codeInventaire+"'");
			TaGrMouvStock instance = (TaGrMouvStock)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
	// - Dima - Début
	public List<TaMouvementStock> findAllWithDates(Date dDebut, Date dFin) { // recherche des tous les élements dans les écart des dates données
	
		logger.debug("getting TaMouvementStock instance with detes : " + dDebut + " ET " + dFin );
		try {
			Query query = entityManager.createNamedQuery(TaMouvementStock.QN.FIND_BY_MOUVEMENT);
			query.setParameter("dateDebut", dDebut);
			query.setParameter("dateFin", dFin);
			
			List<TaMouvementStock> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
//		return null; // tmp
	}	
	// - Dima -  Fin
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaGrMouvStock> selectAll() {
		logger.debug("selectAll TaGrMouvStock");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaGrMouvStock> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaCatalogueWeb entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaGrMouvStock> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaGrMouvStock> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaGrMouvStock> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaGrMouvStock> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaGrMouvStock value) throws Exception {
		BeanValidator<TaGrMouvStock> validator = new BeanValidator<TaGrMouvStock>(TaGrMouvStock.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaGrMouvStock value, String propertyName) throws Exception {
		BeanValidator<TaGrMouvStock> validator = new BeanValidator<TaGrMouvStock>(TaGrMouvStock.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaGrMouvStock transientInstance) {
		entityManager.detach(transientInstance);
	}

	public List<GrMouvStockDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaGrMouvStock.QN.FIND_ALL_LIGHT);
			List<GrMouvStockDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<GrMouvStockDTO> findByCodeLight(String code) {
		logger.debug("getting GrMouvStockDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaGrMouvStock.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeGrMouvStock", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaGrMouvStock.QN.FIND_ALL_LIGHT);
			}

			List<GrMouvStockDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
}

