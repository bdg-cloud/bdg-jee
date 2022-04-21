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
//import fr.legrain.article.dao.IMatierePremiereDAO;
//import fr.legrain.article.model.TaMatierePremiere;
//import fr.legrain.lib.data.ExceptLgr;
//import fr.legrain.validator.BeanValidator;
////import org.hibernate.validator.ClassValidator;
////import org.hibernate.validator.InvalidValue;
//
///**
// * Home object for domain model class TaMatierePremiere.
// * @see fr.legrain.TaMatierePremiere.dao.TaMatierePremiere
// * @author Hibernate Tools
// */
//public class MatierePremiereDAO implements IMatierePremiereDAO {
//
//	private static final Log logger = LogFactory.getLog(MatierePremiereDAO.class);
//	//static Logger logger = Logger.getLogger(TaMatierePremiereDAO.class.getName());
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//
//	//@PersistenceContext
//	//private EntityManager entityManager = entityManager;
//
//	//	protected ClassValidator<TaMatierePremiere> uniteValidator = new ClassValidator<TaMatierePremiere>( TaMatierePremiere.class );
//	
//	private String defaultJPQLQuery = "select u from TaMatierePremiere u";
//	//private String defaultJPQLQuery = "select u from TaMatierePremiere u where u.taArticle is null";
//	
//	public MatierePremiereDAO(){
////		this(null);
//	}
//	
////	public TaMatierePremiereDAO(EntityManager em){
////		if(em!=null) {
////			setEm(em);
////		}
//////		champIdTable=ctrlGeneraux.getID_TABLE(TaMatierePremiere.class.getSimpleName());
////		initChampId(TaMatierePremiere.class);
////		setJPQLQuery(defaultJPQLQuery);
////		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
////		initNomTable(new TaMatierePremiere());
////	}
//	
////	public IStatus validate(TaMatierePremiere entity){
////		ClassValidator<TaMatierePremiere> uniteValidator = new ClassValidator<TaMatierePremiere>( TaMatierePremiere.class );
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaMatierePremiere)
//	 */
//	public void persist(TaMatierePremiere transientInstance) {
//		logger.debug("persisting TaMatierePremiere instance");
//		try {
//			
//			entityManager.persist(transientInstance);
//			logger.debug("persist successful");
//		} catch (RuntimeException re) {
//			logger.error("persist failed", re);
//			throw re;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaMatierePremiere)
//	 */
//	public void remove(TaMatierePremiere persistentInstance) {
//		logger.debug("removing TaMatierePremiere instance");
//		try {
//			TaMatierePremiere e = entityManager.merge(persistentInstance);
//			entityManager.remove(e);
//			logger.debug("remove successful");
//		} catch (RuntimeException re) {
//			logger.error("remove failed", re);
//			throw re;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaMatierePremiere)
//	 */
//	public TaMatierePremiere merge(TaMatierePremiere detachedInstance) {
//		logger.debug("merging TaMatierePremiere instance");
//		try {
//			TaMatierePremiere result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("merge failed", re);
//			throw re;
//		}
//	}
//	
//	public TaMatierePremiere findByCode(String code) {
//		
//			return null;
//		
//	}
//	public List<TaMatierePremiere> findByIdFab(Integer id) {
//		
//		try {
//			if(id != null){
//			Query query = entityManager.createQuery("select f from TaMatierePremiere f where f.id_fabrication='"+id+"'");
//			List<TaMatierePremiere> instance = (List<TaMatierePremiere>)query.getResultList();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//	public TaMatierePremiere findByCode(String code, String codeArticle) {
//		logger.debug("getting TaMatierePremiere instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaMatierePremiere f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaMatierePremiere instance = (TaMatierePremiere)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
//	 */
//	public TaMatierePremiere findById(int id) {
//		logger.debug("getting TaMatierePremiere instance with id: " + id);
//		try {
//			TaMatierePremiere instance = entityManager.find(TaMatierePremiere.class, id);
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
//	public List<TaMatierePremiere> selectAll() {
//		logger.debug("selectAll TaMatierePremiere");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
////			System.out.println(defaultJPQLQuery);
////			System.out.println(ejbQuery.getResultList().toString());
////			System.out.println(ejbQuery.toString());
//			List<TaMatierePremiere> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
//	public TaMatierePremiere refresh(TaMatierePremiere detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaMatierePremiere.class, detachedInstance.getId());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
//	
//	
//	/*
//	public List<TaMatierePremiere> findByArticle(String codeArticle) {
//		logger.debug("getting TaMatierePremiere instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaMatierePremiere.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaMatierePremiere> l = query.getResultList();;
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
////	public ModelGeneralObjet<SWTUnite,TaMatierePremiereDAO,TaMatierePremiere> modelObjetUniteArticle(String codeArticle) {
////		return new ModelGeneralObjet<SWTUnite,TaMatierePremiereDAO,TaMatierePremiere>(findByArticle(codeArticle),SWTUnite.class,entityManager);
////	}
//	
//	public void ctrlSaisieSpecifique(TaMatierePremiere entity,String field) throws ExceptLgr {	
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
//	public List<TaMatierePremiere> findWithNamedQuery(String queryName) {
//		try {
//			Query ejbQuery = entityManager.createNamedQuery(queryName);
//			List<TaMatierePremiere> l = ejbQuery.getResultList();
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
//	public List<TaMatierePremiere> findWithJPQLQuery(String JPQLquery) {
//		try {
//			Query ejbQuery = entityManager.createQuery(JPQLquery);
//			List<TaMatierePremiere> l = ejbQuery.getResultList();
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
//	public boolean validate(TaMatierePremiere value) throws Exception {
//		BeanValidator<TaMatierePremiere> validator = new BeanValidator<TaMatierePremiere>(TaMatierePremiere.class);
//		return validator.validate(value);
//	}
//
//	@Override
//	public boolean validateField(TaMatierePremiere value, String propertyName) throws Exception {
//		BeanValidator<TaMatierePremiere> validator = new BeanValidator<TaMatierePremiere>(TaMatierePremiere.class);
//		return validator.validateField(value,propertyName);
//	}
//
//	@Override
//	public void detach(TaMatierePremiere transientInstance) {
//		entityManager.detach(transientInstance);
//	}
//	
//}
