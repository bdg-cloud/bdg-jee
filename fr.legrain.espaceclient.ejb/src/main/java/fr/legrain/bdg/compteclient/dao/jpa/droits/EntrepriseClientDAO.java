package fr.legrain.bdg.compteclient.dao.jpa.droits;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.validator.internal.util.logging.Log;

import fr.legrain.validators.BeanValidator;
import fr.legrain.bdg.compteclient.dao.droits.IEntrepriseClientDAO;
import fr.legrain.bdg.compteclient.model.droits.TaEntrepriseClient;


public class EntrepriseClientDAO implements IEntrepriseClientDAO {

	private static final Logger logger = Logger.getLogger(EntrepriseClientDAO.class);
	
	@PersistenceContext(unitName = "compteclient")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaEntrepriseClient p";
	
	public EntrepriseClientDAO(){
//		this(null);
	}

//	public TaTaEntrepriseClientDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaEntrepriseClient.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaEntrepriseClient());
//	}


	public void persist(TaEntrepriseClient transientInstance) {
		logger.debug("persisting TaEntrepriseClient instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaEntrepriseClient persistentInstance) {
		logger.debug("removing TaEntrepriseClient instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaEntrepriseClient merge(TaEntrepriseClient detachedInstance) {
		logger.debug("merging TaEntrepriseClient instance");
		try {
			TaEntrepriseClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaEntrepriseClient findById(int id) {
		logger.debug("getting TaEntrepriseClient instance with id: " + id);
		try {
			TaEntrepriseClient instance = entityManager.find(TaEntrepriseClient.class, id);
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
	public List<TaEntrepriseClient> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEntrepriseClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaEntrepriseClient entity,String field) throws Exception {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaEntrepriseClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEntrepriseClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEntrepriseClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEntrepriseClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEntrepriseClient value) throws Exception {
		BeanValidator<TaEntrepriseClient> validator = new BeanValidator<TaEntrepriseClient>(TaEntrepriseClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEntrepriseClient value, String propertyName) throws Exception {
		BeanValidator<TaEntrepriseClient> validator = new BeanValidator<TaEntrepriseClient>(TaEntrepriseClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEntrepriseClient transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaEntrepriseClient findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaEntrepriseClient f where f.codeFamille='"+code+"'");
			TaEntrepriseClient instance = (TaEntrepriseClient)query.getSingleResult();
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

