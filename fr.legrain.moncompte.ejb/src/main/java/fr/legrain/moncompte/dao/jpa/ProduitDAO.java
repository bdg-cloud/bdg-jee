package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.IProduitDAO;
import fr.legrain.moncompte.model.TaProduit;
import fr.legrain.validator.BeanValidator;


public class ProduitDAO implements IProduitDAO {

	private static final Log logger = LogFactory.getLog(ProduitDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaProduit p order by code";
	
	public ProduitDAO(){
//		this(null);
	}

//	public TaTaProduitDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaProduit.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaProduit());
//	}


	public void persist(TaProduit transientInstance) {
		logger.debug("persisting TaProduit instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaProduit persistentInstance) {
		logger.debug("removing TaProduit instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdProduit()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaProduit merge(TaProduit detachedInstance) {
		logger.debug("merging TaProduit instance");
		try {
			TaProduit result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaProduit findById(int id) {
		logger.debug("getting TaProduit instance with id: " + id);
		try {
			TaProduit instance = entityManager.find(TaProduit.class, id);
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
	public List<TaProduit> selectAll() {
		logger.debug("selectAll TaProduit");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaProduit> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaProduit> selectAllProduitCategoriePro(int idCategoriePro) {
		logger.debug("selectAll TaProduit");
		try {
			Query ejbQuery = entityManager.createQuery("select p from TaCategoriePro c join c.listeProduit p where c.id="+idCategoriePro);
			List<TaProduit> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaProduit entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaProduit> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaProduit> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaProduit> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaProduit> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaProduit value) throws Exception {
		BeanValidator<TaProduit> validator = new BeanValidator<TaProduit>(TaProduit.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaProduit value, String propertyName) throws Exception {
		BeanValidator<TaProduit> validator = new BeanValidator<TaProduit>(TaProduit.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaProduit transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaProduit findByCode(String code) {
		logger.debug("getting TaProduit instance with username: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaProduit f where f.code='"+code+"'");
			TaProduit instance = (TaProduit)query.getSingleResult();
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

