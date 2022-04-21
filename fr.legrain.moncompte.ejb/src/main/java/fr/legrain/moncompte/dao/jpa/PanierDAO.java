package fr.legrain.moncompte.dao.jpa;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.IPanierDAO;
import fr.legrain.moncompte.model.TaPanier;
import fr.legrain.validator.BeanValidator;


public class PanierDAO implements IPanierDAO {

	private static final Log logger = LogFactory.getLog(PanierDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaPanier p order by p.dateCreation";
	
	public PanierDAO(){
//		this(null);
	}

//	public TaTaPanierDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaPanier.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaPanier());
//	}


	public void persist(TaPanier transientInstance) {
		logger.debug("persisting TaPanier instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaPanier persistentInstance) {
		logger.debug("removing TaPanier instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdPanier()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaPanier merge(TaPanier detachedInstance) {
		logger.debug("merging TaPanier instance");
		try {
			TaPanier result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaPanier findById(int id) {
		logger.debug("getting TaPanier instance with id: " + id);
		try {
			TaPanier instance = entityManager.find(TaPanier.class, id);
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
	public List<TaPanier> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPanier> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaPanier entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaPanier> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPanier> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPanier> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPanier value) throws Exception {
		BeanValidator<TaPanier> validator = new BeanValidator<TaPanier>(TaPanier.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPanier value, String propertyName) throws Exception {
		BeanValidator<TaPanier> validator = new BeanValidator<TaPanier>(TaPanier.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPanier transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaPanier findByCode(String code) {
		logger.debug("getting TaPanier instance with username: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaPanier f where f.username='"+code+"'");
			TaPanier instance = (TaPanier)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaPanier> findPanierDossier(String codeDossier) {
		try {
			if(!codeDossier.equals("")){
				Query query = entityManager.createQuery("select f from TaPanier f where f.taDossier.code='"+codeDossier+"'");
				List<TaPanier> instance = query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaPanier> findPanierClient(String codeClient) {
		try {
			if(!codeClient.equals("")){
				Query query = entityManager.createQuery("select f from TaPanier f where f.taClient.code='"+codeClient+"'");
				List<TaPanier> instance = query.getResultList();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaPanier> findPanierDate(Date debut, Date fin) {
		try {
			String jpql = null;
				jpql = "select p from TaPanier p where p.dateCreation between :debut and :fin";
				Query query = entityManager.createQuery(jpql);
				query.setParameter("debut", debut,TemporalType.TIMESTAMP);
				query.setParameter("fin", fin,TemporalType.TIMESTAMP);
				List<TaPanier> instance = query.getResultList();
				logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaPanier> findPanier(Date debut, Date fin, boolean paye, boolean valideClient, String codeDossier, String codeClient, String codeTypePaiement) {
		try {
			String jpql = null;
			jpql = "select p from TaPanier p where p.dateCreation between :debut and :fin";
			Query query = entityManager.createQuery(jpql);
			query.setParameter("debut", debut,TemporalType.TIMESTAMP);
			query.setParameter("fin", fin,TemporalType.TIMESTAMP);
			List<TaPanier> instance = query.getResultList();
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
}

