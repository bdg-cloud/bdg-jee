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
import fr.legrain.moncompte.model.TaPrixParUtilisateur;
import fr.legrain.validator.BeanValidator;


public class PrixParUtilisateurDAO implements IPrixParUtilisateurDAO {

	private static final Log logger = LogFactory.getLog(PrixParUtilisateurDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaPrixParUtilisateur p order by p.nbUtilisateur";
	
	public PrixParUtilisateurDAO(){
//		this(null);
	}

//	public TaTaPrixParUtilisateurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaPrixParUtilisateur.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaPrixParUtilisateur());
//	}


	public void persist(TaPrixParUtilisateur transientInstance) {
		logger.debug("persisting TaPrixParUtilisateur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaPrixParUtilisateur persistentInstance) {
		logger.debug("removing TaPrixParUtilisateur instance");
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

	public TaPrixParUtilisateur merge(TaPrixParUtilisateur detachedInstance) {
		logger.debug("merging TaPrixParUtilisateur instance");
		try {
			TaPrixParUtilisateur result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaPrixParUtilisateur findById(int id) {
		logger.debug("getting TaPrixParUtilisateur instance with id: " + id);
		try {
			TaPrixParUtilisateur instance = entityManager.find(TaPrixParUtilisateur.class, id);
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
	public List<TaPrixParUtilisateur> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaPrixParUtilisateur> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaPrixParUtilisateur entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaPrixParUtilisateur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaPrixParUtilisateur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaPrixParUtilisateur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaPrixParUtilisateur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaPrixParUtilisateur value) throws Exception {
		BeanValidator<TaPrixParUtilisateur> validator = new BeanValidator<TaPrixParUtilisateur>(TaPrixParUtilisateur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaPrixParUtilisateur value, String propertyName) throws Exception {
		BeanValidator<TaPrixParUtilisateur> validator = new BeanValidator<TaPrixParUtilisateur>(TaPrixParUtilisateur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaPrixParUtilisateur transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaPrixParUtilisateur findByCode(String code) {
		logger.debug("getting TaPrixParUtilisateur instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaPrixParUtilisateur f where f.code='"+code+"'");
			TaPrixParUtilisateur instance = (TaPrixParUtilisateur)query.getSingleResult();
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

