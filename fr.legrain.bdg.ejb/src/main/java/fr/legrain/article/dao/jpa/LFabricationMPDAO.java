package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ILFabricationMPDAO;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaLFabricationMP.
 * @see fr.legrain.TaLFabricationMP.dao.TaLFabricationMP
 * @author Hibernate Tools
 */
public class LFabricationMPDAO implements ILFabricationMPDAO {

	private static final Log logger = LogFactory.getLog(LFabricationMPDAO.class);
	//static Logger logger = Logger.getLogger(TaLFabricationDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaLFabricationMP> uniteValidator = new ClassValidator<TaLFabricationMP>( TaLFabricationMP.class );
	
	private String defaultJPQLQuery = "select u from TaLFabricationMP u";
	//private String defaultJPQLQuery = "select u from TaLFabricationMP u where u.taArticle is null";
	
	public LFabricationMPDAO(){
//		this(null);
	}
	
//	public TaLFabricationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLFabricationMP.class.getSimpleName());
//		initChampId(TaLFabricationMP.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLFabricationMP());
//	}
	
//	public IStatus validate(TaLFabricationMP entity){
//		ClassValidator<TaLFabricationMP> uniteValidator = new ClassValidator<TaLFabricationMP>( TaLFabricationMP.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaLFabricationMP)
	 */
	public void persist(TaLFabricationMP transientInstance) {
		logger.debug("persisting TaLFabricationMP instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaLFabricationMP)
	 */
	public void remove(TaLFabricationMP persistentInstance) {
		logger.debug("removing TaLFabricationMP instance");
		try {
			TaLFabricationMP e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaLFabricationMP)
	 */
	public TaLFabricationMP merge(TaLFabricationMP detachedInstance) {
		logger.debug("merging TaLFabricationMP instance");
		try {
			TaLFabricationMP result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaLFabricationMP findByCode(String code) {
		logger.debug("getting TaLFabricationMP instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLFabricationMP f where f.codeBarre='"+code+"'");
			TaLFabricationMP instance = (TaLFabricationMP)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
//	public TaLFabricationMP findByCode(String code, String codeArticle) {
//		logger.debug("getting TaLFabricationMP instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaLFabricationMP f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaLFabricationMP instance = (TaLFabricationMP)query.getSingleResult();
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
	public TaLFabricationMP findById(int id) {
		logger.debug("getting TaLFabricationMP instance with id: " + id);
		try {
			TaLFabricationMP instance = entityManager.find(TaLFabricationMP.class, id);
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
	public List<TaLFabricationMP> selectAll() {
		logger.debug("selectAll TaLFabricationMP");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaLFabricationMP> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	public TaLFabricationMP refresh(TaLFabricationMP detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLFabricationMP.class, detachedInstance.getId());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	
	/*
	public List<TaLFabricationMP> findByArticle(String codeArticle) {
		logger.debug("getting TaLFabricationMP instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaLFabricationMP.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaLFabricationMP> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaLFabricationDAO,TaLFabricationMP> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaLFabricationDAO,TaLFabricationMP>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaLFabricationMP entity,String field) throws ExceptLgr {	
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
	public List<TaLFabricationMP> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLFabricationMP> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLFabricationMP> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLFabricationMP> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLFabricationMP value) throws Exception {
		BeanValidator<TaLFabricationMP> validator = new BeanValidator<TaLFabricationMP>(TaLFabricationMP.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLFabricationMP value, String propertyName) throws Exception {
		BeanValidator<TaLFabricationMP> validator = new BeanValidator<TaLFabricationMP>(TaLFabricationMP.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLFabricationMP transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
