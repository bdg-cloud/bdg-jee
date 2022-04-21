package fr.legrain.article.dao.jpa;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IInventaireDAO;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.stock.dto.InventaireDTO;
import fr.legrain.stock.model.TaInventaire;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCatalogueWeb.
 * @see fr.legrain.articles.dao.TaCatalogueWeb
 * @author Hibernate Tools
 */
public class InventaireDAO implements IInventaireDAO{

	private static final Log logger = LogFactory.getLog(InventaireDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaInventaire p";
	
	public InventaireDAO(){
//		this(null);
	}




	public void persist(TaInventaire transientInstance) {
		logger.debug("persisting TaInventaire instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
	public TaInventaire refresh(TaInventaire detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaInventaire.class, detachedInstance.getIdInventaire());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaInventaire persistentInstance) {
		logger.debug("removing TaInventaire instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaInventaire merge(TaInventaire detachedInstance) {
		logger.debug("merging TaInventaire instance");
		try {
			TaInventaire result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaInventaire findById(int id) {
		logger.debug("getting TaInventaire instance with id: " + id);
		try {
			TaInventaire instance = entityManager.find(TaInventaire.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaInventaire findByCode(String codeInventaire) {
		logger.debug("getting TaInventaire instance with code: " + codeInventaire);
		try {
			if(!codeInventaire.equals("")){
			Query query = entityManager.createQuery("select a from TaInventaire a "
					+ " where (a.codeInventaire)='"+codeInventaire+"'");
			TaInventaire instance = (TaInventaire)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}	
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaInventaire> selectAll() {
		logger.debug("selectAll TaInventaire");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaInventaire> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaInventaire entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaInventaire> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaInventaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaInventaire> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaInventaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaInventaire value) throws Exception {
		BeanValidator<TaInventaire> validator = new BeanValidator<TaInventaire>(TaInventaire.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaInventaire value, String propertyName) throws Exception {
		BeanValidator<TaInventaire> validator = new BeanValidator<TaInventaire>(TaInventaire.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaInventaire transientInstance) {
		entityManager.detach(transientInstance);
	}

	public List<InventaireDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaInventaire.QN.FIND_ALL_LIGHT);
			List<InventaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public InventaireDTO findByCodeLight(String code) {
		logger.debug("getting InventaireDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaInventaire.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeInventaire", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaInventaire.QN.FIND_ALL_LIGHT);
			}

			InventaireDTO l = (InventaireDTO) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}

