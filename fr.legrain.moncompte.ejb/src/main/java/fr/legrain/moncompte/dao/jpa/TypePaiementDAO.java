package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ITypePaiementDAO;
import fr.legrain.moncompte.model.TaTypePaiement;
import fr.legrain.validator.BeanValidator;


public class TypePaiementDAO implements ITypePaiementDAO {

	private static final Log logger = LogFactory.getLog(TypePaiementDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaTypePaiement p";
	
	public TypePaiementDAO(){
//		this(null);
	}

//	public TaTaTypePaiementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaTypePaiement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTypePaiement());
//	}


	public void persist(TaTypePaiement transientInstance) {
		logger.debug("persisting TaTypePaiement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTypePaiement persistentInstance) {
		logger.debug("removing TaTypePaiement instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdTypePaiement()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTypePaiement merge(TaTypePaiement detachedInstance) {
		logger.debug("merging TaTypePaiement instance");
		try {
			TaTypePaiement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTypePaiement findById(int id) {
		logger.debug("getting TaTypePaiement instance with id: " + id);
		try {
			TaTypePaiement instance = entityManager.find(TaTypePaiement.class, id);
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
	public List<TaTypePaiement> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypePaiement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTypePaiement entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaTypePaiement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypePaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypePaiement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypePaiement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypePaiement value) throws Exception {
		BeanValidator<TaTypePaiement> validator = new BeanValidator<TaTypePaiement>(TaTypePaiement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypePaiement value, String propertyName) throws Exception {
		BeanValidator<TaTypePaiement> validator = new BeanValidator<TaTypePaiement>(TaTypePaiement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypePaiement transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaTypePaiement findByCode(String code) {
		logger.debug("getting TaTypePaiement instance with username: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTypePaiement f where f.code='"+code+"'");
				TaTypePaiement instance = (TaTypePaiement)query.getSingleResult();
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

