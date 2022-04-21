package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ICategorieProDAO;
import fr.legrain.moncompte.dao.IClientDAO;
import fr.legrain.moncompte.model.TaCategoriePro;
import fr.legrain.moncompte.model.TaCategoriePro;
import fr.legrain.validator.BeanValidator;


public class CategorieProDAO implements ICategorieProDAO {

	private static final Log logger = LogFactory.getLog(CategorieProDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaCategoriePro p order by p.idCategoriePro";
	
	public CategorieProDAO(){
//		this(null);
	}

//	public TaTaCategorieProDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaCategoriePro.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCategoriePro());
//	}


	public void persist(TaCategoriePro transientInstance) {
		logger.debug("persisting TaCategoriePro instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaCategoriePro persistentInstance) {
		logger.debug("removing TaCategoriePro instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdCategoriePro()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCategoriePro merge(TaCategoriePro detachedInstance) {
		logger.debug("merging TaCategoriePro instance");
		try {
			TaCategoriePro result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCategoriePro findById(int id) {
		logger.debug("getting TaCategoriePro instance with id: " + id);
		try {
			TaCategoriePro instance = entityManager.find(TaCategoriePro.class, id);
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
	public List<TaCategoriePro> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCategoriePro> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaCategoriePro entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaCategoriePro> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCategoriePro> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCategoriePro> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCategoriePro> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCategoriePro value) throws Exception {
		BeanValidator<TaCategoriePro> validator = new BeanValidator<TaCategoriePro>(TaCategoriePro.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCategoriePro value, String propertyName) throws Exception {
		BeanValidator<TaCategoriePro> validator = new BeanValidator<TaCategoriePro>(TaCategoriePro.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCategoriePro transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaCategoriePro findByCode(String code) {
		logger.debug("getting TaCategoriePro instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaCategoriePro f where f.code='"+code+"'");
			TaCategoriePro instance = (TaCategoriePro)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
	
}

