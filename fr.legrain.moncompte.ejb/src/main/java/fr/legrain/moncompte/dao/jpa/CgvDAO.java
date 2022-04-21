package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ICgvDAO;
import fr.legrain.moncompte.dao.IClientDAO;
import fr.legrain.moncompte.model.TaCgv;
import fr.legrain.moncompte.model.TaCgv;
import fr.legrain.validator.BeanValidator;


public class CgvDAO implements ICgvDAO {

	private static final Log logger = LogFactory.getLog(CgvDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaCgv p order by p.idCgv";
	
	public CgvDAO(){
//		this(null);
	}

//	public TaTaCgvDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaCgv.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCgv());
//	}


	public void persist(TaCgv transientInstance) {
		logger.debug("persisting TaCgv instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaCgv persistentInstance) {
		logger.debug("removing TaCgv instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdCgv()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCgv merge(TaCgv detachedInstance) {
		logger.debug("merging TaCgv instance");
		try {
			TaCgv result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCgv findById(int id) {
		logger.debug("getting TaCgv instance with id: " + id);
		try {
			TaCgv instance = entityManager.find(TaCgv.class, id);
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
	public List<TaCgv> selectAll() {
		logger.debug("selectAll TaCgv");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCgv> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaCgv findCgvCourant() {
		logger.debug("findCgvCourant TaCgv");
		try {
			Query ejbQuery = entityManager.createQuery("select p from TaCgv p where actif = true");
			TaCgv l = (TaCgv) ejbQuery.getSingleResult();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaCgv entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaCgv> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCgv> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCgv> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCgv> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCgv value) throws Exception {
		BeanValidator<TaCgv> validator = new BeanValidator<TaCgv>(TaCgv.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCgv value, String propertyName) throws Exception {
		BeanValidator<TaCgv> validator = new BeanValidator<TaCgv>(TaCgv.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCgv transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaCgv findByCode(String code) {
		logger.debug("getting TaCgv instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaCgv f where f.code='"+code+"'");
			TaCgv instance = (TaCgv)query.getSingleResult();
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

