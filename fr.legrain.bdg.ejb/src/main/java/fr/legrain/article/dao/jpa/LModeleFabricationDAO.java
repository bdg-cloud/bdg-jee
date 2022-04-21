//package fr.legrain.article.dao.jpa;
//
//// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import fr.legrain.article.dao.ILModeleFabricationDAO;
//import fr.legrain.article.model.TaLModeleFabrication;
//import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
//import fr.legrain.lib.data.ExceptLgr;
//import fr.legrain.validator.BeanValidator;
////import org.hibernate.validator.ClassValidator;
////import org.hibernate.validator.InvalidValue;
//
///**
// * Home object for domain model class TaLModeleFabrication.
// * @see fr.legrain.TaLModeleFabrication.dao.TaLModeleFabrication
// * @author Hibernate Tools
// */
//public class LModeleFabricationDAO implements ILModeleFabricationDAO {
//
//	private static final Log logger = LogFactory.getLog(LModeleFabricationDAO.class);
//	//static Logger logger = Logger.getLogger(TaLModeleFabricationDAO.class.getName());
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//
//	//@PersistenceContext
//	//private EntityManager entityManager = entityManager;
//
//	//	protected ClassValidator<TaLModeleFabrication> uniteValidator = new ClassValidator<TaLModeleFabrication>( TaLModeleFabrication.class );
//	
//	private String defaultJPQLQuery = "select u from TaLModeleFabrication u";
//	//private String defaultJPQLQuery = "select u from TaLModeleFabrication u where u.taArticle is null";
//	
//	public LModeleFabricationDAO(){
////		this(null);
//	}
//	
////	public TaLModeleFabricationDAO(EntityManager em){
////		if(em!=null) {
////			setEm(em);
////		}
//////		champIdTable=ctrlGeneraux.getID_TABLE(TaLModeleFabrication.class.getSimpleName());
////		initChampId(TaLModeleFabrication.class);
////		setJPQLQuery(defaultJPQLQuery);
////		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
////		initNomTable(new TaLModeleFabrication());
////	}
//	
////	public IStatus validate(TaLModeleFabrication entity){
////		ClassValidator<TaLModeleFabrication> uniteValidator = new ClassValidator<TaLModeleFabrication>( TaLModeleFabrication.class );
////		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
////		IStatus s = null;
////		if(iv.length>0) {
////			s = new Status(0,"",iv[0].getMessage());
////		}
////		return s;
////	}
//	
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaLModeleFabrication)
//	 */
//	public void persist(TaLModeleFabrication transientInstance) {
//		logger.debug("persisting TaLModeleFabrication instance");
//		try {
//			
//			entityManager.persist(transientInstance);
//			logger.debug("persist successful");
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("persist failed", re);
//			throw re2;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaLModeleFabrication)
//	 */
//	public void remove(TaLModeleFabrication persistentInstance) {
//		logger.debug("removing TaLModeleFabrication instance");
//		try {
//			TaLModeleFabrication e = entityManager.merge(persistentInstance);
//			entityManager.remove(e);
//			logger.debug("remove successful");
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("remove failed", re);
//			throw re2;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaLModeleFabrication)
//	 */
//	public TaLModeleFabrication merge(TaLModeleFabrication detachedInstance) {
//		logger.debug("merging TaLModeleFabrication instance");
//		try {
//			TaLModeleFabrication result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("merge failed", re);
//			throw re2;
//		}
//	}
//	
//	public TaLModeleFabrication findByCode(String code) {
//		logger.debug("getting TaLModeleFabrication instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaLModeleFabrication f where f.codeBarre='"+code+"'");
//			TaLModeleFabrication instance = (TaLModeleFabrication)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			return null;
//		}
//	}
//	
////	public TaLModeleFabrication findByCode(String code, String codeArticle) {
////		logger.debug("getting TaLModeleFabrication instance with code: " + code);
////		try {
////			if(!code.equals("")){
////			Query query = entityManager.createQuery("select f from TaLModeleFabrication f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
////			TaLModeleFabrication instance = (TaLModeleFabrication)query.getSingleResult();
////			logger.debug("get successful");
////			return instance;
////			}
////			return null;
////		} catch (RuntimeException re) {
////			logger.error("get failed", re);
////			throw re;
////		}
////	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
//	 */
//	public TaLModeleFabrication findById(int id) {
//		logger.debug("getting TaLModeleFabrication instance with id: " + id);
//		try {
//			TaLModeleFabrication instance = entityManager.find(TaLModeleFabrication.class, id);
//			logger.debug("get successful");
//			return instance;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//	
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
//	 */
//	public List<TaLModeleFabrication> selectAll() {
//		logger.debug("selectAll TaLModeleFabrication");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
////			System.out.println(defaultJPQLQuery);
////			System.out.println(ejbQuery.getResultList().toString());
////			System.out.println(ejbQuery.toString());
//			List<TaLModeleFabrication> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
////	public TaLModeleFabrication refresh(TaLModeleFabrication detachedInstance) {
////		logger.debug("refresh instance");
////		try {
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLModeleFabrication.class, detachedInstance.getId());
////		    return detachedInstance;
////			
////		} catch (RuntimeException re) {
////			logger.error("refresh failed", re);
////			throw re;
////		}
////	}
//	
//	
//	/*
//	public List<TaLModeleFabrication> findByArticle(String codeArticle) {
//		logger.debug("getting TaLModeleFabrication instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaLModeleFabrication.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaLModeleFabrication> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}*/
//	
//	//ejb
////	public ModelGeneralObjet<SWTUnite,TaLModeleFabricationDAO,TaLModeleFabrication> modelObjetUniteArticle(String codeArticle) {
////		return new ModelGeneralObjet<SWTUnite,TaLModeleFabricationDAO,TaLModeleFabrication>(findByArticle(codeArticle),SWTUnite.class,entityManager);
////	}
//	
//	public void ctrlSaisieSpecifique(TaLModeleFabrication entity,String field) throws ExceptLgr {	
//		try {
////			if(field.equals(Const.C_CODE_TVA)){
////				if(!entity.getCodeTva().equals("")){
////				}					
////			}			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	
////	public void ctrlSaisieSpecifique(ValeurChamps valeurChamps) throws ExceptLgr {	
////		try{
////			switch (this.getModeObjet().getMode()) {
////            case C_MO_CONSULTATION: break;
////            
////            case C_MO_IMPORTATION:            				
////            case C_MO_INSERTION:
////            	TCtrlGeneraux.ctrlSaisie(ex);
////				break;            	
////            case C_MO_EDITION:	
////            	TCtrlGeneraux.ctrlSaisie(ex);
////				break;
////            case C_MO_SUPPRESSION: 
////            	TCtrlGeneraux.ctrlSaisie(ex);
////				break;
////			default:				
////				break;
////			}
////		}catch (Exception e){
////			logger.error("Erreur : ctrlSaisieSpecifique", e);
////			throw new ExceptLgr(e.getMessage());
////		}
////	}
//
//	@Override
//	public List<TaLModeleFabrication> findWithNamedQuery(String queryName) {
//		try {
//			Query ejbQuery = entityManager.createNamedQuery(queryName);
//			List<TaLModeleFabrication> l = ejbQuery.getResultList();
//			System.out.println("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			System.out.println("selectAll failed");
//			re.printStackTrace();
//			throw re;
//		}
//	}
//
//	@Override
//	public List<TaLModeleFabrication> findWithJPQLQuery(String JPQLquery) {
//		try {
//			Query ejbQuery = entityManager.createQuery(JPQLquery);
//			List<TaLModeleFabrication> l = ejbQuery.getResultList();
//			System.out.println("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			System.out.println("selectAll failed");
//			re.printStackTrace();
//			throw re;
//		}
//	}
//
//	@Override
//	public boolean validate(TaLModeleFabrication value) throws Exception {
//		BeanValidator<TaLModeleFabrication> validator = new BeanValidator<TaLModeleFabrication>(TaLModeleFabrication.class);
//		return validator.validate(value);
//	}
//
//	@Override
//	public boolean validateField(TaLModeleFabrication value, String propertyName) throws Exception {
//		BeanValidator<TaLModeleFabrication> validator = new BeanValidator<TaLModeleFabrication>(TaLModeleFabrication.class);
//		return validator.validateField(value,propertyName);
//	}
//
//	@Override
//	public void detach(TaLModeleFabrication transientInstance) {
//		entityManager.detach(transientInstance);
//	}
//	
//}
