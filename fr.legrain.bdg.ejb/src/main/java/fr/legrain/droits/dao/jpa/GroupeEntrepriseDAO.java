package fr.legrain.droits.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.droits.dao.IGroupeEntrepriseDAO;
import fr.legrain.droits.model.TaGroupeEntreprise;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;


public class GroupeEntrepriseDAO implements IGroupeEntrepriseDAO {

	private static final Log logger = LogFactory.getLog(GroupeEntrepriseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaGroupeEntreprise p";
	
	public GroupeEntrepriseDAO(){
//		this(null);
	}

//	public TaTaGroupeEntrepriseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaGroupeEntreprise.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaGroupeEntreprise());
//	}


	public void persist(TaGroupeEntreprise transientInstance) {
		logger.debug("persisting TaGroupeEntreprise instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaGroupeEntreprise persistentInstance) {
		logger.debug("removing TaGroupeEntreprise instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getId()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaGroupeEntreprise merge(TaGroupeEntreprise detachedInstance) {
		logger.debug("merging TaGroupeEntreprise instance");
		try {
			TaGroupeEntreprise result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaGroupeEntreprise findById(int id) {
		logger.debug("getting TaGroupeEntreprise instance with id: " + id);
		try {
			TaGroupeEntreprise instance = entityManager.find(TaGroupeEntreprise.class, id);
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
	public List<TaGroupeEntreprise> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaGroupeEntreprise> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaGroupeEntreprise entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaGroupeEntreprise> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaGroupeEntreprise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaGroupeEntreprise> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaGroupeEntreprise> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaGroupeEntreprise value) throws Exception {
		BeanValidator<TaGroupeEntreprise> validator = new BeanValidator<TaGroupeEntreprise>(TaGroupeEntreprise.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaGroupeEntreprise value, String propertyName) throws Exception {
		BeanValidator<TaGroupeEntreprise> validator = new BeanValidator<TaGroupeEntreprise>(TaGroupeEntreprise.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaGroupeEntreprise transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaGroupeEntreprise findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaGroupeEntreprise f where f.codeFamille='"+code+"'");
			TaGroupeEntreprise instance = (TaGroupeEntreprise)query.getSingleResult();
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

