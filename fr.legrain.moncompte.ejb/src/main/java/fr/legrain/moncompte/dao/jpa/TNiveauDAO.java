package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ITNiveauDAO;
import fr.legrain.moncompte.model.TaTNiveau;
import fr.legrain.validator.BeanValidator;


public class TNiveauDAO implements ITNiveauDAO {

	private static final Log logger = LogFactory.getLog(TNiveauDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaTNiveau p";
	
	public TNiveauDAO(){
//		this(null);
	}

//	public TaTaTNiveauDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaTNiveau.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTNiveau());
//	}


	public void persist(TaTNiveau transientInstance) {
		logger.debug("persisting TaTNiveau instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTNiveau persistentInstance) {
		logger.debug("removing TaTNiveau instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdTNiveau()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTNiveau merge(TaTNiveau detachedInstance) {
		logger.debug("merging TaTNiveau instance");
		try {
			TaTNiveau result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTNiveau findById(int id) {
		logger.debug("getting TaTNiveau instance with id: " + id);
		try {
			TaTNiveau instance = entityManager.find(TaTNiveau.class, id);
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
	public List<TaTNiveau> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTNiveau> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTNiveau entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaTNiveau> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTNiveau> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTNiveau> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTNiveau> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTNiveau value) throws Exception {
		BeanValidator<TaTNiveau> validator = new BeanValidator<TaTNiveau>(TaTNiveau.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTNiveau value, String propertyName) throws Exception {
		BeanValidator<TaTNiveau> validator = new BeanValidator<TaTNiveau>(TaTNiveau.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTNiveau transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaTNiveau findByCode(String code) {
		logger.debug("getting TaTNiveau instance with username: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTNiveau f where f.code='"+code+"'");
				TaTNiveau instance = (TaTNiveau)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
}

