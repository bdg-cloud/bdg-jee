package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.IDossierDAO;
import fr.legrain.moncompte.dao.IPrixParUtilisateurDAO;
import fr.legrain.moncompte.dao.IPrixParUtilisateurPersoDAO;
import fr.legrain.moncompte.dao.IPrixPersoDAO;
import fr.legrain.moncompte.model.TaPrixParUtilisateurPerso;
import fr.legrain.validator.BeanValidator;


public class PrixParUtilisateurPersoDAO implements IPrixParUtilisateurPersoDAO {

	private static final Log logger = LogFactory.getLog(PrixParUtilisateurPersoDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaPrixParUtilisateurPerso p order by p.code";
	
	public PrixParUtilisateurPersoDAO(){
//		this(null);
	}

//	public TaTaPrixParUtilisateurPersoDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaPrixParUtilisateurPerso.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaPrixParUtilisateurPerso());
//	}


	public void persist(TaPrixParUtilisateurPerso transientInstance) {
		logger.debug("persisting TaPrixParUtilisateurPerso instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaPrixParUtilisateurPerso persistentInstance) {
		logger.debug("removing TaPrixParUtilisateurPerso instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaPrixParUtilisateurPerso merge(TaPrixParUtilisateurPerso detachedInstance) {
		logger.debug("merging TaPrixParUtilisateurPerso instance");
		try {
			TaPrixParUtilisateurPerso result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaPrixParUtilisateurPerso findById(int id) {
		logger.debug("getting TaPrixParUtilisateurPerso instance with id: " + id);
		try {
			TaPrixParUtilisateurPerso instance = entityManager.find(TaPrixParUtilisateurPerso.class, id);
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
	public List<TaPrixParUtilisateurPerso> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPrixParUtilisateurPerso> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaPrixParUtilisateurPerso entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaPrixParUtilisateurPerso> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPrixParUtilisateurPerso> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPrixParUtilisateurPerso> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPrixParUtilisateurPerso> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPrixParUtilisateurPerso value) throws Exception {
		BeanValidator<TaPrixParUtilisateurPerso> validator = new BeanValidator<TaPrixParUtilisateurPerso>(TaPrixParUtilisateurPerso.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPrixParUtilisateurPerso value, String propertyName) throws Exception {
		BeanValidator<TaPrixParUtilisateurPerso> validator = new BeanValidator<TaPrixParUtilisateurPerso>(TaPrixParUtilisateurPerso.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPrixParUtilisateurPerso transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaPrixParUtilisateurPerso findByCode(String code) {
		logger.debug("getting TaPrixParUtilisateurPerso instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaPrixParUtilisateurPerso f where f.code='"+code+"'");
			TaPrixParUtilisateurPerso instance = (TaPrixParUtilisateurPerso)query.getSingleResult();
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

