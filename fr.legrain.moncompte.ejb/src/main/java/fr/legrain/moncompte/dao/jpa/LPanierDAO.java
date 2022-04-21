package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ILPanierDAO;
import fr.legrain.moncompte.model.TaLignePanier;
import fr.legrain.validator.BeanValidator;


public class LPanierDAO implements ILPanierDAO {

	private static final Log logger = LogFactory.getLog(LPanierDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaLignePanier p";
	
	public LPanierDAO(){
//		this(null);
	}

//	public TaTaLignePanierDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaLignePanier.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLignePanier());
//	}


	public void persist(TaLignePanier transientInstance) {
		logger.debug("persisting TaLignePanier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaLignePanier persistentInstance) {
		logger.debug("removing TaLignePanier instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdLignePanier()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaLignePanier merge(TaLignePanier detachedInstance) {
		logger.debug("merging TaLignePanier instance");
		try {
			TaLignePanier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaLignePanier findById(int id) {
		logger.debug("getting TaLignePanier instance with id: " + id);
		try {
			TaLignePanier instance = entityManager.find(TaLignePanier.class, id);
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
	public List<TaLignePanier> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLignePanier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaLignePanier entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaLignePanier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLignePanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLignePanier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLignePanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLignePanier value) throws Exception {
		BeanValidator<TaLignePanier> validator = new BeanValidator<TaLignePanier>(TaLignePanier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLignePanier value, String propertyName) throws Exception {
		BeanValidator<TaLignePanier> validator = new BeanValidator<TaLignePanier>(TaLignePanier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLignePanier transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaLignePanier findByCode(String code) {
		logger.debug("getting TaLignePanier instance with username: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLignePanier f where f.username='"+code+"'");
			TaLignePanier instance = (TaLignePanier)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
}

