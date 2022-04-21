package fr.legrain.bdg.compteclient.dao.jpa.droits;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.validators.BeanValidator;
import fr.legrain.bdg.compteclient.dao.droits.IAuthURLDAO;
import fr.legrain.bdg.compteclient.model.droits.TaAuthURL;


public class AuthURLDAO implements IAuthURLDAO {

	private static final Logger logger = Logger.getLogger(AuthURLDAO.class);
	
	@PersistenceContext(unitName = "compteclient")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaAuthURL p";
	
	public AuthURLDAO(){
//		this(null);
	}

//	public TaTaAuthURLDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaAuthURL.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaAuthURL());
//	}


	public void persist(TaAuthURL transientInstance) {
		logger.debug("persisting TaAuthURL instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaAuthURL persistentInstance) {
		logger.debug("removing TaAuthURL instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaAuthURL merge(TaAuthURL detachedInstance) {
		logger.debug("merging TaAuthURL instance");
		try {
			TaAuthURL result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaAuthURL findById(int id) {
		logger.debug("getting TaAuthURL instance with id: " + id);
		try {
			TaAuthURL instance = entityManager.find(TaAuthURL.class, id);
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
	public List<TaAuthURL> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAuthURL> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaAuthURL entity,String field) throws Exception {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaAuthURL> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAuthURL> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAuthURL> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAuthURL> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAuthURL value) throws Exception {
		BeanValidator<TaAuthURL> validator = new BeanValidator<TaAuthURL>(TaAuthURL.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAuthURL value, String propertyName) throws Exception {
		BeanValidator<TaAuthURL> validator = new BeanValidator<TaAuthURL>(TaAuthURL.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAuthURL transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaAuthURL findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaAuthURL f where f.codeFamille='"+code+"'");
			TaAuthURL instance = (TaAuthURL)query.getSingleResult();
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

