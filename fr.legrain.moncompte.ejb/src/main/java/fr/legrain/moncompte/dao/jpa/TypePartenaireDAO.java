package fr.legrain.moncompte.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.moncompte.dao.ITypePartenaireDAO;
import fr.legrain.moncompte.model.TaTypePartenaire;
import fr.legrain.validator.BeanValidator;


public class TypePartenaireDAO implements ITypePartenaireDAO {

	private static final Log logger = LogFactory.getLog(TypePartenaireDAO.class);
	
	@PersistenceContext(unitName = "moncompte")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaTypePartenaire p";
	
	public TypePartenaireDAO(){
//		this(null);
	}

//	public TaTaTypePartenaireDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaTypePartenaire.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTypePartenaire());
//	}


	public void persist(TaTypePartenaire transientInstance) {
		logger.debug("persisting TaTypePartenaire instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTypePartenaire persistentInstance) {
		logger.debug("removing TaTypePartenaire instance");
		//boolean estRefPrix=false;
		try {
			entityManager.remove(findById(persistentInstance.getIdTypePartenaire()));

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTypePartenaire merge(TaTypePartenaire detachedInstance) {
		logger.debug("merging TaTypePartenaire instance");
		try {
			TaTypePartenaire result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTypePartenaire findById(int id) {
		logger.debug("getting TaTypePartenaire instance with id: " + id);
		try {
			TaTypePartenaire instance = entityManager.find(TaTypePartenaire.class, id);
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
	public List<TaTypePartenaire> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTypePartenaire> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTypePartenaire entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaTypePartenaire> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypePartenaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypePartenaire> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypePartenaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypePartenaire value) throws Exception {
		BeanValidator<TaTypePartenaire> validator = new BeanValidator<TaTypePartenaire>(TaTypePartenaire.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypePartenaire value, String propertyName) throws Exception {
		BeanValidator<TaTypePartenaire> validator = new BeanValidator<TaTypePartenaire>(TaTypePartenaire.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypePartenaire transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaTypePartenaire findByCode(String code) {
		logger.debug("getting TaTypePartenaire instance with username: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTypePartenaire f where f.code='"+code+"'");
				TaTypePartenaire instance = (TaTypePartenaire)query.getSingleResult();
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

