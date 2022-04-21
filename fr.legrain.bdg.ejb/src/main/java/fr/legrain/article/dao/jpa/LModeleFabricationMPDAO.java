package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ILModeleFabricationMPDAO;
import fr.legrain.article.model.TaLModeleFabricationMP;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaLModeleFabricationMP.
 * @see fr.legrain.TaLModeleFabricationMP.dao.TaLModeleFabricationMP
 * @author Hibernate Tools
 */
public class LModeleFabricationMPDAO implements ILModeleFabricationMPDAO {

	private static final Log logger = LogFactory.getLog(LModeleFabricationMPDAO.class);
	//static Logger logger = Logger.getLogger(TaLModeleFabricationDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaLModeleFabricationMP> uniteValidator = new ClassValidator<TaLModeleFabricationMP>( TaLModeleFabricationMP.class );
	
	private String defaultJPQLQuery = "select u from TaLModeleFabricationMP u";
	//private String defaultJPQLQuery = "select u from TaLModeleFabricationMP u where u.taArticle is null";
	
	public LModeleFabricationMPDAO(){
//		this(null);
	}
	
//	public TaLModeleFabricationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLModeleFabricationMP.class.getSimpleName());
//		initChampId(TaLModeleFabricationMP.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLModeleFabricationMP());
//	}
	
//	public IStatus validate(TaLModeleFabricationMP entity){
//		ClassValidator<TaLModeleFabricationMP> uniteValidator = new ClassValidator<TaLModeleFabricationMP>( TaLModeleFabricationMP.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaLModeleFabricationMP)
	 */
	public void persist(TaLModeleFabricationMP transientInstance) {
		logger.debug("persisting TaLModeleFabricationMP instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaLModeleFabricationMP)
	 */
	public void remove(TaLModeleFabricationMP persistentInstance) {
		logger.debug("removing TaLModeleFabricationMP instance");
		try {
			TaLModeleFabricationMP e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaLModeleFabricationMP)
	 */
	public TaLModeleFabricationMP merge(TaLModeleFabricationMP detachedInstance) {
		logger.debug("merging TaLModeleFabricationMP instance");
		try {
			TaLModeleFabricationMP result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaLModeleFabricationMP findByCode(String code) {
		logger.debug("getting TaLModeleFabricationMP instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLModeleFabricationMP f where f.codeBarre='"+code+"'");
			TaLModeleFabricationMP instance = (TaLModeleFabricationMP)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
//	public TaLModeleFabricationMP findByCode(String code, String codeArticle) {
//		logger.debug("getting TaLModeleFabricationMP instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaLModeleFabricationMP f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaLModeleFabricationMP instance = (TaLModeleFabricationMP)query.getSingleResult();
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
	public TaLModeleFabricationMP findById(int id) {
		logger.debug("getting TaLModeleFabricationMP instance with id: " + id);
		try {
			TaLModeleFabricationMP instance = entityManager.find(TaLModeleFabricationMP.class, id);
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
	public List<TaLModeleFabricationMP> selectAll() {
		logger.debug("selectAll TaLModeleFabricationMP");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaLModeleFabricationMP> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	public TaLModeleFabricationMP refresh(TaLModeleFabricationMP detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLModeleFabricationMP.class, detachedInstance.getId());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	
	/*
	public List<TaLModeleFabricationMP> findByArticle(String codeArticle) {
		logger.debug("getting TaLModeleFabricationMP instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaLModeleFabricationMP.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaLModeleFabricationMP> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaLModeleFabricationDAO,TaLModeleFabricationMP> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaLModeleFabricationDAO,TaLModeleFabricationMP>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaLModeleFabricationMP entity,String field) throws ExceptLgr {	
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
	public List<TaLModeleFabricationMP> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLModeleFabricationMP> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLModeleFabricationMP> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLModeleFabricationMP> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLModeleFabricationMP value) throws Exception {
		BeanValidator<TaLModeleFabricationMP> validator = new BeanValidator<TaLModeleFabricationMP>(TaLModeleFabricationMP.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLModeleFabricationMP value, String propertyName) throws Exception {
		BeanValidator<TaLModeleFabricationMP> validator = new BeanValidator<TaLModeleFabricationMP>(TaLModeleFabricationMP.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLModeleFabricationMP transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
