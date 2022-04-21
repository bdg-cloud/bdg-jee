package fr.legrain.bdg.compteclient.dao.jpa.droits;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.validator.internal.util.logging.Log;

import fr.legrain.validators.BeanValidator;
import fr.legrain.bdg.compteclient.dao.droits.IAuthViewDAO;
import fr.legrain.bdg.compteclient.model.droits.TaAuthView;


public class AuthViewDAO implements IAuthViewDAO {

	private static final Logger logger = Logger.getLogger(AuthViewDAO.class);
	
	@PersistenceContext(unitName = "compteclient")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaAuthView p";
	
	public AuthViewDAO(){
//		this(null);
	}

//	public TaTaAuthViewDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaAuthView.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaAuthView());
//	}


	public void persist(TaAuthView transientInstance) {
		logger.debug("persisting TaAuthView instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaAuthView persistentInstance) {
		logger.debug("removing TaAuthView instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaAuthView merge(TaAuthView detachedInstance) {
		logger.debug("merging TaAuthView instance");
		try {
			TaAuthView result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaAuthView findById(int id) {
		logger.debug("getting TaAuthView instance with id: " + id);
		try {
			TaAuthView instance = entityManager.find(TaAuthView.class, id);
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
	public List<TaAuthView> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAuthView> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaAuthView entity,String field) throws Exception {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaAuthView> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAuthView> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAuthView> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAuthView> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAuthView value) throws Exception {
		BeanValidator<TaAuthView> validator = new BeanValidator<TaAuthView>(TaAuthView.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAuthView value, String propertyName) throws Exception {
		BeanValidator<TaAuthView> validator = new BeanValidator<TaAuthView>(TaAuthView.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAuthView transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaAuthView findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaAuthView f where f.codeFamille='"+code+"'");
			TaAuthView instance = (TaAuthView)query.getSingleResult();
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

