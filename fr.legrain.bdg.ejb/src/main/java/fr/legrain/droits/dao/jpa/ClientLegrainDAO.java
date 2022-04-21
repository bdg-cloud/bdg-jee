package fr.legrain.droits.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.droits.dao.IClientLegrainDAO;
import fr.legrain.droits.model.TaClientLegrain;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class ClientLegrainDAO implements IClientLegrainDAO {

	private static final Log logger = LogFactory.getLog(ClientLegrainDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaClientLegrain p";
	
	public ClientLegrainDAO(){
//		this(null);
	}

//	public TaTaClientLegrainDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaClientLegrain.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaClientLegrain());
//	}


	public void persist(TaClientLegrain transientInstance) {
		logger.debug("persisting TaClientLegrain instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaClientLegrain persistentInstance) {
		logger.debug("removing TaClientLegrain instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaClientLegrain merge(TaClientLegrain detachedInstance) {
		logger.debug("merging TaClientLegrain instance");
		try {
			TaClientLegrain result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaClientLegrain findById(int id) {
		logger.debug("getting TaClientLegrain instance with id: " + id);
		try {
			TaClientLegrain instance = entityManager.find(TaClientLegrain.class, id);
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
	public List<TaClientLegrain> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaClientLegrain> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaClientLegrain entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaClientLegrain> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaClientLegrain> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaClientLegrain> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaClientLegrain> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaClientLegrain value) throws Exception {
		BeanValidator<TaClientLegrain> validator = new BeanValidator<TaClientLegrain>(TaClientLegrain.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaClientLegrain value, String propertyName) throws Exception {
		BeanValidator<TaClientLegrain> validator = new BeanValidator<TaClientLegrain>(TaClientLegrain.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaClientLegrain transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaClientLegrain findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaClientLegrain f where f.codeFamille='"+code+"'");
			TaClientLegrain instance = (TaClientLegrain)query.getSingleResult();
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

