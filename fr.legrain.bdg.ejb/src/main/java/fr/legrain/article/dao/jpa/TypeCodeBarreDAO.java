package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ITypeCodeBarreDAO;
import fr.legrain.article.model.TaTypeCodeBarre;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaTypeCodeBarre.
 * @see fr.legrain.TaTypeCodeBarre.dao.TaTypeCodeBarre
 * @author Hibernate Tools
 */
public class TypeCodeBarreDAO implements ITypeCodeBarreDAO {

	private static final Log logger = LogFactory.getLog(TypeCodeBarreDAO.class);
	//static Logger logger = Logger.getLogger(TaTypeCodeBarreDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaTypeCodeBarre> uniteValidator = new ClassValidator<TaTypeCodeBarre>( TaTypeCodeBarre.class );
	
	private String defaultJPQLQuery = "select u from TaTypeCodeBarre u order by u.codeTypeCodeBarre";
	//private String defaultJPQLQuery = "select u from TaTypeCodeBarre u where u.taArticle is null";
	
	public TypeCodeBarreDAO(){
//		this(null);
	}
	
//	public TaTypeCodeBarreDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTypeCodeBarre.class.getSimpleName());
//		initChampId(TaTypeCodeBarre.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTypeCodeBarre());
//	}
	
//	public IStatus validate(TaTypeCodeBarre entity){
//		ClassValidator<TaTypeCodeBarre> uniteValidator = new ClassValidator<TaTypeCodeBarre>( TaTypeCodeBarre.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaTypeCodeBarre)
	 */
	public void persist(TaTypeCodeBarre transientInstance) {
		logger.debug("persisting TaTypeCodeBarre instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaTypeCodeBarre)
	 */
	public void remove(TaTypeCodeBarre persistentInstance) {
		logger.debug("removing TaTypeCodeBarre instance");
		try {
			TaTypeCodeBarre e = entityManager.merge(findById(persistentInstance.getIdTypeCodeBarre()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaTypeCodeBarre)
	 */
	public TaTypeCodeBarre merge(TaTypeCodeBarre detachedInstance) {
		logger.debug("merging TaTypeCodeBarre instance");
		try {
			TaTypeCodeBarre result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaTypeCodeBarre findByCode(String code) {
		logger.debug("getting TaTypeCodeBarre instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTypeCodeBarre f where f.codeTypeCodeBarre='"+code+"'");
			TaTypeCodeBarre instance = (TaTypeCodeBarre)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaTypeCodeBarre findById(int id) {
		logger.debug("getting TaTypeCodeBarre instance with id: " + id);
		try {
			TaTypeCodeBarre instance = entityManager.find(TaTypeCodeBarre.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaTypeCodeBarre> selectAll() {
		logger.debug("selectAll TaTypeCodeBarre");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaTypeCodeBarre> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaTypeCodeBarre refresh(TaTypeCodeBarre detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaTypeCodeBarre.class, detachedInstance.getIdTypeCodeBarre());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaTypeCodeBarre> findByArticle(String codeArticle) {
		logger.debug("getting TaTypeCodeBarre instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaTypeCodeBarre.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaTypeCodeBarre> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaTypeCodeBarreDAO,TaTypeCodeBarre> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaTypeCodeBarreDAO,TaTypeCodeBarre>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaTypeCodeBarre entity,String field) throws ExceptLgr {	
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
	public List<TaTypeCodeBarre> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeCodeBarre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeCodeBarre> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeCodeBarre> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypeCodeBarre value) throws Exception {
		BeanValidator<TaTypeCodeBarre> validator = new BeanValidator<TaTypeCodeBarre>(TaTypeCodeBarre.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeCodeBarre value, String propertyName) throws Exception {
		BeanValidator<TaTypeCodeBarre> validator = new BeanValidator<TaTypeCodeBarre>(TaTypeCodeBarre.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeCodeBarre transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
