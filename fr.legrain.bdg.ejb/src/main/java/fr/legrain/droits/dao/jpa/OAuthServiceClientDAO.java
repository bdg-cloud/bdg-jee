package fr.legrain.droits.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.droits.dao.IOAuthServiceClientDAO;
import fr.legrain.droits.model.TaOAuthServiceClient;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class OAuthServiceClientDAO implements IOAuthServiceClientDAO {

	private static final Log logger = LogFactory.getLog(OAuthServiceClientDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaOAuthServiceClient p";
	
	public OAuthServiceClientDAO(){
//		this(null);
	}

//	public TaTaOAuthServiceClientDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaOAuthServiceClient.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaOAuthServiceClient());
//	}


	public void persist(TaOAuthServiceClient transientInstance) {
		logger.debug("persisting TaOAuthServiceClient instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaOAuthServiceClient persistentInstance) {
		logger.debug("removing TaOAuthServiceClient instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaOAuthServiceClient merge(TaOAuthServiceClient detachedInstance) {
		logger.debug("merging TaOAuthServiceClient instance");
		try {
			TaOAuthServiceClient result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaOAuthServiceClient findById(int id) {
		logger.debug("getting TaOAuthServiceClient instance with id: " + id);
		try {
			TaOAuthServiceClient instance = entityManager.find(TaOAuthServiceClient.class, id);
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
	public List<TaOAuthServiceClient> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaOAuthServiceClient> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaOAuthServiceClient entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaOAuthServiceClient> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaOAuthServiceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaOAuthServiceClient> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaOAuthServiceClient> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaOAuthServiceClient value) throws Exception {
		BeanValidator<TaOAuthServiceClient> validator = new BeanValidator<TaOAuthServiceClient>(TaOAuthServiceClient.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaOAuthServiceClient value, String propertyName) throws Exception {
		BeanValidator<TaOAuthServiceClient> validator = new BeanValidator<TaOAuthServiceClient>(TaOAuthServiceClient.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaOAuthServiceClient transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaOAuthServiceClient findByCode(String code) {
		logger.debug("getting TaOAuthServiceClient instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaOAuthServiceClient f where f.code='"+code+"'");
				TaOAuthServiceClient instance = (TaOAuthServiceClient)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}
//	
//	public List<TaOAuthServiceClientDTO> findByCodeLight(String code) {
//		logger.debug("select TaOAuthServiceClientDTO");
//		try {
//			Query ejbQuery = entityManager.createQuery("select new fr.legrain.droits.dto.TaOAuthServiceClientDTO(u.id, u.username, u.nom, u.prenom, u.email, u.dernierAcces, u.actif) from TaOAuthServiceClient u where u.email like '%"+code+"%' and u.actif = true");
//			List<TaOAuthServiceClientDTO> l = ejbQuery.getResultList();
//			logger.debug("select successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("select failed", re);
//			throw re;
//		}
//	}
//	
//	public List<TaOAuthServiceClientDTO> findByNomLight(String nom) {
//		logger.debug("select TaOAuthServiceClientDTO");
//		try {
//			Query ejbQuery = entityManager.createQuery("select new fr.legrain.droits.dto.TaOAuthServiceClientDTO(u.id, u.username, u.nom, u.prenom, u.email, u.dernierAcces, u.actif) from TaOAuthServiceClient u where u.nom like '%"+nom+"%' and u.actif = true");
//			List<TaOAuthServiceClientDTO> l = ejbQuery.getResultList();
//			logger.debug("select successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("select failed", re);
//			throw re;
//		}
//	}
	
}

