package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IListeConformiteDAO;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaListeConformite;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaListeConformite.
 * @see fr.legrain.articles.dao.TaListeConformite
 * @author Hibernate Tools
 */
public class ListeConformiteDAO implements IListeConformiteDAO {

	private static final Log logger = LogFactory.getLog(ListeConformiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaListeConformiteDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaListeConformite f";
	
	public ListeConformiteDAO(){
//		this(null);
	}
	
//	public TaListeConformiteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaListeConformite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaListeConformite());
//	}
	
//	public TaListeConformite refresh(TaListeConformite detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaListeConformite.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public List<TaConformite>  findControleConformiteByCodeArticle(String codeArticle) {
		logger.debug("findByCodeTva: " + codeArticle);
		try {
			if(!codeArticle.equals("")){
				Query query = entityManager.createQuery("select a.taConformite from TaListeConformite a where a.taArticle.codeArticle='"+codeArticle+"'");
				List<TaConformite> temp = query.getResultList();
				
				logger.debug("get successful");
				return temp;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaListeConformite>  findByCodeArticle(String codeArticle) {
		logger.debug("findByCodeTva: " + codeArticle);
		try {
			if(!codeArticle.equals("")){
				Query query = entityManager.createQuery("select a from TaListeConformite a where a.taArticle.codeArticle='"+codeArticle+"'");
				List<TaListeConformite> temp = query.getResultList();
				
				logger.debug("get successful");
				return temp;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	public void persist(TaListeConformite transientInstance) {
		logger.debug("persisting TaListeConformite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaListeConformite persistentInstance) {
		logger.debug("removing TaListeConformite instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdListeConformite()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaListeConformite merge(TaListeConformite detachedInstance) {
		logger.debug("merging TaListeConformite instance");
		try {
			TaListeConformite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaListeConformite findByCode(String code) {
		logger.debug("getting TaListeConformite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaListeConformite f where f.codeFamille='"+code+"'");
			TaListeConformite instance = (TaListeConformite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaListeConformite findById(int id) {
		logger.debug("getting TaListeConformite instance with id: " + id);
		try {
			TaListeConformite instance = entityManager.find(TaListeConformite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaListeConformite> selectAll() {
		logger.debug("selectAll TaListeConformite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaListeConformite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaListeConformite entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaListeConformite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaListeConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaListeConformite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaListeConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaListeConformite value) throws Exception {
		BeanValidator<TaListeConformite> validator = new BeanValidator<TaListeConformite>(TaListeConformite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaListeConformite value, String propertyName) throws Exception {
		BeanValidator<TaListeConformite> validator = new BeanValidator<TaListeConformite>(TaListeConformite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaListeConformite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
