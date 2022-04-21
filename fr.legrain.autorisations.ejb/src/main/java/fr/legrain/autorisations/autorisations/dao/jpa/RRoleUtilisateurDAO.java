package fr.legrain.autorisations.autorisations.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.autorisations.autorisation.model.TaRRoleUtilisateur;
import fr.legrain.autorisations.autorisations.dao.IRRoleUtilisateurDAO;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class RRoleUtilisateurDAO implements IRRoleUtilisateurDAO {

	private static final Log logger = LogFactory.getLog(RRoleUtilisateurDAO.class);
	
	@PersistenceContext(unitName = "autorisations")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaRRoleUtilisateur p";
	
	public RRoleUtilisateurDAO(){
//		this(null);
	}

//	public TaTaRRoleUtilisateurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaRRoleUtilisateur.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRRoleUtilisateur());
//	}


	public void persist(TaRRoleUtilisateur transientInstance) {
		logger.debug("persisting TaRRoleUtilisateur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaRRoleUtilisateur persistentInstance) {
		logger.debug("removing TaRRoleUtilisateur instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaRRoleUtilisateur merge(TaRRoleUtilisateur detachedInstance) {
		logger.debug("merging TaRRoleUtilisateur instance");
		try {
			TaRRoleUtilisateur result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaRRoleUtilisateur findById(int id) {
		logger.debug("getting TaRRoleUtilisateur instance with id: " + id);
		try {
			TaRRoleUtilisateur instance = entityManager.find(TaRRoleUtilisateur.class, id);
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
	public List<TaRRoleUtilisateur> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRRoleUtilisateur> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaRRoleUtilisateur entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaRRoleUtilisateur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRRoleUtilisateur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRRoleUtilisateur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRRoleUtilisateur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRRoleUtilisateur value) throws Exception {
		BeanValidator<TaRRoleUtilisateur> validator = new BeanValidator<TaRRoleUtilisateur>(TaRRoleUtilisateur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRRoleUtilisateur value, String propertyName) throws Exception {
		BeanValidator<TaRRoleUtilisateur> validator = new BeanValidator<TaRRoleUtilisateur>(TaRRoleUtilisateur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRRoleUtilisateur transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaRRoleUtilisateur findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaRRoleUtilisateur f where f.codeFamille='"+code+"'");
			TaRRoleUtilisateur instance = (TaRRoleUtilisateur)query.getSingleResult();
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

