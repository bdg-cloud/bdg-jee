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
//import fr.legrain.article.dao.ILFabricationDAO;
//import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
//import fr.legrain.lib.data.ExceptLgr;
//import fr.legrain.validator.BeanValidator;
////import org.hibernate.validator.ClassValidator;
////import org.hibernate.validator.InvalidValue;
//
///**
// * Home object for domain model class TaLFabrication.
// * @see fr.legrain.TaLFabrication.dao.TaLFabrication
// * @author Hibernate Tools
// */
//public class LFabricationDAO implements ILFabricationDAO {
//
//	private static final Log logger = LogFactory.getLog(LFabricationDAO.class);
//	//static Logger logger = Logger.getLogger(TaLFabricationDAO.class.getName());
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//
//	//@PersistenceContext
//	//private EntityManager entityManager = entityManager;
//
//	//	protected ClassValidator<TaLFabrication> uniteValidator = new ClassValidator<TaLFabrication>( TaLFabrication.class );
//	
//	private String defaultJPQLQuery = "select u from TaLFabrication u";
//	//private String defaultJPQLQuery = "select u from TaLFabrication u where u.taArticle is null";
//	
//	public LFabricationDAO(){
////		this(null);
//	}
//	
////	public TaLFabricationDAO(EntityManager em){
////		if(em!=null) {
////			setEm(em);
////		}
//////		champIdTable=ctrlGeneraux.getID_TABLE(TaLFabrication.class.getSimpleName());
////		initChampId(TaLFabrication.class);
////		setJPQLQuery(defaultJPQLQuery);
////		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
////		initNomTable(new TaLFabrication());
////	}
//	
////	public IStatus validate(TaLFabrication entity){
////		ClassValidator<TaLFabrication> uniteValidator = new ClassValidator<TaLFabrication>( TaLFabrication.class );
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaLFabrication)
//	 */
//	public void persist(TaLFabrication transientInstance) {
//		logger.debug("persisting TaLFabrication instance");
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaLFabrication)
//	 */
//	public void remove(TaLFabrication persistentInstance) {
//		logger.debug("removing TaLFabrication instance");
//		try {
//			TaLFabrication e = entityManager.merge(persistentInstance);
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaLFabrication)
//	 */
//	public TaLFabrication merge(TaLFabrication detachedInstance) {
//		logger.debug("merging TaLFabrication instance");
//		try {
//			TaLFabrication result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
//			logger.error("merge failed", re);
//			throw re2;
//		}
//	}
//	
//	public TaLFabrication findByCode(String code) {
//		logger.debug("getting TaLFabrication instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaLFabrication f where f.codeBarre='"+code+"'");
//			TaLFabrication instance = (TaLFabrication)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			return null;
//		}
//	}
//	
////	public TaLFabrication findByCode(String code, String codeArticle) {
////		logger.debug("getting TaLFabrication instance with code: " + code);
////		try {
////			if(!code.equals("")){
////			Query query = entityManager.createQuery("select f from TaLFabrication f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
////			TaLFabrication instance = (TaLFabrication)query.getSingleResult();
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
//	public TaLFabrication findById(int id) {
//		logger.debug("getting TaLFabrication instance with id: " + id);
//		try {
//			TaLFabrication instance = entityManager.find(TaLFabrication.class, id);
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
//	public List<TaLFabrication> selectAll() {
//		logger.debug("selectAll TaLFabrication");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
////			System.out.println(defaultJPQLQuery);
////			System.out.println(ejbQuery.getResultList().toString());
////			System.out.println(ejbQuery.toString());
//			List<TaLFabrication> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
////	public TaLFabrication refresh(TaLFabrication detachedInstance) {
////		logger.debug("refresh instance");
////		try {
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLFabrication.class, detachedInstance.getId());
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
//	public List<TaLFabrication> findByArticle(String codeArticle) {
//		logger.debug("getting TaLFabrication instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaLFabrication.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaLFabrication> l = query.getResultList();;
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
////	public ModelGeneralObjet<SWTUnite,TaLFabricationDAO,TaLFabrication> modelObjetUniteArticle(String codeArticle) {
////		return new ModelGeneralObjet<SWTUnite,TaLFabricationDAO,TaLFabrication>(findByArticle(codeArticle),SWTUnite.class,entityManager);
////	}
//	
//	public void ctrlSaisieSpecifique(TaLFabrication entity,String field) throws ExceptLgr {	
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
//	public List<TaLFabrication> findWithNamedQuery(String queryName) {
//		try {
//			Query ejbQuery = entityManager.createNamedQuery(queryName);
//			List<TaLFabrication> l = ejbQuery.getResultList();
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
//	public List<TaLFabrication> findWithJPQLQuery(String JPQLquery) {
//		try {
//			Query ejbQuery = entityManager.createQuery(JPQLquery);
//			List<TaLFabrication> l = ejbQuery.getResultList();
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
//	public boolean validate(TaLFabrication value) throws Exception {
//		BeanValidator<TaLFabrication> validator = new BeanValidator<TaLFabrication>(TaLFabrication.class);
//		return validator.validate(value);
//	}
//
//	@Override
//	public boolean validateField(TaLFabrication value, String propertyName) throws Exception {
//		BeanValidator<TaLFabrication> validator = new BeanValidator<TaLFabrication>(TaLFabrication.class);
//		return validator.validateField(value,propertyName);
//	}
//
//	@Override
//	public void detach(TaLFabrication transientInstance) {
//		entityManager.detach(transientInstance);
//	}
//	
//}
