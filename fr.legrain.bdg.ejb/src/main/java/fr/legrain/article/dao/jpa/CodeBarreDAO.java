package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ICodeBarreDAO;
import fr.legrain.article.model.TaCodeBarre;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaCodeBarre.
 * @see fr.legrain.TaCodeBarre.dao.TaCodeBarre
 * @author Hibernate Tools
 */
public class CodeBarreDAO implements ICodeBarreDAO {

	private static final Log logger = LogFactory.getLog(CodeBarreDAO.class);
	//static Logger logger = Logger.getLogger(TaCodeBarreDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaCodeBarre> uniteValidator = new ClassValidator<TaCodeBarre>( TaCodeBarre.class );
	
	private String defaultJPQLQuery = "select u from TaCodeBarre u";
	//private String defaultJPQLQuery = "select u from TaCodeBarre u where u.taArticle is null";
	
	public CodeBarreDAO(){
//		this(null);
	}
	
//	public TaCodeBarreDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaCodeBarre.class.getSimpleName());
//		initChampId(TaCodeBarre.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCodeBarre());
//	}
	
//	public IStatus validate(TaCodeBarre entity){
//		ClassValidator<TaCodeBarre> uniteValidator = new ClassValidator<TaCodeBarre>( TaCodeBarre.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaCodeBarre)
	 */
	public void persist(TaCodeBarre transientInstance) {
		logger.debug("persisting TaCodeBarre instance");
		try {
			
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaCodeBarre)
	 */
	public void remove(TaCodeBarre persistentInstance) {
		logger.debug("removing TaCodeBarre instance");
		try {
			TaCodeBarre e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaCodeBarre)
	 */
	public TaCodeBarre merge(TaCodeBarre detachedInstance) {
		logger.debug("merging TaCodeBarre instance");
		try {
			TaCodeBarre result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaCodeBarre findByCode(String code) {
		logger.debug("getting TaCodeBarre instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaCodeBarre f where f.codeBarre='"+code+"'");
			TaCodeBarre instance = (TaCodeBarre)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
//	public TaCodeBarre findByCode(String code, String codeArticle) {
//		logger.debug("getting TaCodeBarre instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaCodeBarre f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaCodeBarre instance = (TaCodeBarre)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaCodeBarre findById(int id) {
		logger.debug("getting TaCodeBarre instance with id: " + id);
		try {
			TaCodeBarre instance = entityManager.find(TaCodeBarre.class, id);
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
	public List<TaCodeBarre> selectAll() {
		logger.debug("selectAll TaCodeBarre");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaCodeBarre> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaCodeBarre refresh(TaCodeBarre detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaCodeBarre.class, detachedInstance.getId());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaCodeBarre> findByArticle(String codeArticle) {
		logger.debug("getting TaCodeBarre instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaCodeBarre.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaCodeBarre> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaCodeBarreDAO,TaCodeBarre> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaCodeBarreDAO,TaCodeBarre>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaCodeBarre entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
//	public void ctrlSaisieSpecifique(ValeurChamps valeurChamps) throws ExceptLgr {	
//		try{
//			switch (this.getModeObjet().getMode()) {
//            case C_MO_CONSULTATION: break;
//            
//            case C_MO_IMPORTATION:            				
//            case C_MO_INSERTION:
//            	TCtrlGeneraux.ctrlSaisie(ex);
//				break;            	
//            case C_MO_EDITION:	
//            	TCtrlGeneraux.ctrlSaisie(ex);
//				break;
//            case C_MO_SUPPRESSION: 
//            	TCtrlGeneraux.ctrlSaisie(ex);
//				break;
//			default:				
//				break;
//			}
//		}catch (Exception e){
//			logger.error("Erreur : ctrlSaisieSpecifique", e);
//			throw new ExceptLgr(e.getMessage());
//		}
//	}

	@Override
	public List<TaCodeBarre> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCodeBarre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCodeBarre> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCodeBarre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCodeBarre value) throws Exception {
		BeanValidator<TaCodeBarre> validator = new BeanValidator<TaCodeBarre>(TaCodeBarre.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCodeBarre value, String propertyName) throws Exception {
		BeanValidator<TaCodeBarre> validator = new BeanValidator<TaCodeBarre>(TaCodeBarre.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCodeBarre transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
