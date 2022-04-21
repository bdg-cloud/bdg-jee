package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ICgPartenaireDAO;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.validator.BeanValidator;


public class CgPartenaireDAO implements ICgPartenaireDAO {

	private static final Log logger = LogFactory.getLog(CgPartenaireDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaCgPartenaire p order by p.idCgPartenaire";
	
	public CgPartenaireDAO(){
//		this(null);
	}

//	public TaTaCgPartenaireDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaCgPartenaire.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCgPartenaire());
//	}


	public void persist(TaCgPartenaire transientInstance) {
		logger.debug("persisting TaCgPartenaire instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaCgPartenaire persistentInstance) {
		logger.debug("removing TaCgPartenaire instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdCgPartenaire()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCgPartenaire merge(TaCgPartenaire detachedInstance) {
		logger.debug("merging TaCgPartenaire instance");
		try {
			TaCgPartenaire result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCgPartenaire findById(int id) {
		logger.debug("getting TaCgPartenaire instance with id: " + id);
		try {
			TaCgPartenaire instance = entityManager.find(TaCgPartenaire.class, id);
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
	public List<TaCgPartenaire> selectAll() {
		logger.debug("selectAll TaCgPartenaire");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCgPartenaire> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaCgPartenaire findCgPartenaireCourant() {
		logger.debug("findCgvCourant TaCgPartenaire");
		try {
			Query ejbQuery = entityManager.createQuery("select p from TaCgPartenaire p where actif = true");
			TaCgPartenaire l = (TaCgPartenaire) ejbQuery.getSingleResult();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaCgPartenaire entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaCgPartenaire> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCgPartenaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCgPartenaire> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCgPartenaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCgPartenaire value) throws Exception {
		BeanValidator<TaCgPartenaire> validator = new BeanValidator<TaCgPartenaire>(TaCgPartenaire.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCgPartenaire value, String propertyName) throws Exception {
		BeanValidator<TaCgPartenaire> validator = new BeanValidator<TaCgPartenaire>(TaCgPartenaire.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCgPartenaire transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaCgPartenaire findByCode(String code) {
		logger.debug("getting TaCgPartenaire instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaCgPartenaire f where f.code='"+code+"'");
			TaCgPartenaire instance = (TaCgPartenaire)query.getSingleResult();
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

