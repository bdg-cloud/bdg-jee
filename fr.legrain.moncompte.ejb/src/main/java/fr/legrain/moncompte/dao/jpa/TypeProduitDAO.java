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
import fr.legrain.moncompte.dao.ITypeProduitDAO;
import fr.legrain.moncompte.dao.IUtilisateurDAO;
import fr.legrain.moncompte.model.TaTypeProduit;
import fr.legrain.validator.BeanValidator;


public class TypeProduitDAO implements ITypeProduitDAO {

	private static final Log logger = LogFactory.getLog(TypeProduitDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaTypeProduit p";
	
	public TypeProduitDAO(){
//		this(null);
	}

//	public TaTaTypeProduitDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaTypeProduit.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTypeProduit());
//	}


	public void persist(TaTypeProduit transientInstance) {
		logger.debug("persisting TaTypeProduit instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTypeProduit persistentInstance) {
		logger.debug("removing TaTypeProduit instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdTypeProduit()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTypeProduit merge(TaTypeProduit detachedInstance) {
		logger.debug("merging TaTypeProduit instance");
		try {
			TaTypeProduit result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTypeProduit findById(int id) {
		logger.debug("getting TaTypeProduit instance with id: " + id);
		try {
			TaTypeProduit instance = entityManager.find(TaTypeProduit.class, id);
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
	public List<TaTypeProduit> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypeProduit> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTypeProduit entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaTypeProduit> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeProduit> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeProduit> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeProduit> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypeProduit value) throws Exception {
		BeanValidator<TaTypeProduit> validator = new BeanValidator<TaTypeProduit>(TaTypeProduit.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeProduit value, String propertyName) throws Exception {
		BeanValidator<TaTypeProduit> validator = new BeanValidator<TaTypeProduit>(TaTypeProduit.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeProduit transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaTypeProduit findByCode(String code) {
		logger.debug("getting TaTypeProduit instance with username: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTypeProduit f where f.code='"+code+"'");
				TaTypeProduit instance = (TaTypeProduit)query.getSingleResult();
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

