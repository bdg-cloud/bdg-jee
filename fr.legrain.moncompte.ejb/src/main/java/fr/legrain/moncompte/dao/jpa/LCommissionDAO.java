package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ILCommissionDAO;
import fr.legrain.moncompte.dao.ILPanierDAO;
import fr.legrain.moncompte.model.TaLigneCommission;
import fr.legrain.moncompte.model.TaLigneCommission;
import fr.legrain.validator.BeanValidator;


public class LCommissionDAO implements ILCommissionDAO {

	private static final Log logger = LogFactory.getLog(LCommissionDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaLigneCommission p";
	
	public LCommissionDAO(){
//		this(null);
	}

//	public TaTaLigneCommissionDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaLigneCommission.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLigneCommission());
//	}


	public void persist(TaLigneCommission transientInstance) {
		logger.debug("persisting TaLigneCommission instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaLigneCommission persistentInstance) {
		logger.debug("removing TaLigneCommission instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaLigneCommission merge(TaLigneCommission detachedInstance) {
		logger.debug("merging TaLigneCommission instance");
		try {
			TaLigneCommission result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaLigneCommission findById(int id) {
		logger.debug("getting TaLigneCommission instance with id: " + id);
		try {
			TaLigneCommission instance = entityManager.find(TaLigneCommission.class, id);
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
	public List<TaLigneCommission> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLigneCommission> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaLigneCommission entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaLigneCommission> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLigneCommission> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLigneCommission> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLigneCommission> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLigneCommission value) throws Exception {
		BeanValidator<TaLigneCommission> validator = new BeanValidator<TaLigneCommission>(TaLigneCommission.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLigneCommission value, String propertyName) throws Exception {
		BeanValidator<TaLigneCommission> validator = new BeanValidator<TaLigneCommission>(TaLigneCommission.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLigneCommission transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaLigneCommission findByCode(String code) {
		logger.debug("getting TaLigneCommission instance with username: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLigneCommission f where f.username='"+code+"'");
			TaLigneCommission instance = (TaLigneCommission)query.getSingleResult();
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

