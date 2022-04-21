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
//import fr.legrain.article.dao.IProduitDAO;
//import fr.legrain.article.model.TaMatierePremiere;
//import fr.legrain.article.model.TaProduit;
//import fr.legrain.data.IGenCode;
//import fr.legrain.lib.data.ExceptLgr;
//import fr.legrain.validator.BeanValidator;
////import org.hibernate.validator.ClassValidator;
////import org.hibernate.validator.InvalidValue;
//
///**
// * Home object for domain model class TaProduit.
// * @see fr.legrain.TaProduit.dao.TaProduit
// * @author Hibernate Tools
// */
//public class ProduitDAO implements IProduitDAO {
//
//	private static final Log logger = LogFactory.getLog(ProduitDAO.class);
//	//static Logger logger = Logger.getLogger(TaProduitDAO.class.getName());
//	
//	@PersistenceContext(unitName = "bdg")
//	private EntityManager entityManager;
//
//	//@PersistenceContext
//	//private EntityManager entityManager = entityManager;
//
//	//	protected ClassValidator<TaProduit> uniteValidator = new ClassValidator<TaProduit>( TaProduit.class );
//	
//	private String defaultJPQLQuery = "select u from TaProduit u";
//	//private String defaultJPQLQuery = "select u from TaProduit u where u.taArticle is null";
//	
//	public ProduitDAO(){
////		this(null);
//	}
//	
////	public TaProduitDAO(EntityManager em){
////		if(em!=null) {
////			setEm(em);
////		}
//////		champIdTable=ctrlGeneraux.getID_TABLE(TaProduit.class.getSimpleName());
////		initChampId(TaProduit.class);
////		setJPQLQuery(defaultJPQLQuery);
////		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
////		initNomTable(new TaProduit());
////	}
//	
////	public IStatus validate(TaProduit entity){
////		ClassValidator<TaProduit> uniteValidator = new ClassValidator<TaProduit>( TaProduit.class );
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaProduit)
//	 */
//	public void persist(TaProduit transientInstance) {
//		logger.debug("persisting TaProduit instance");
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
//	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaProduit)
//	 */
//	public void remove(TaProduit persistentInstance) {
//		logger.debug("removing TaProduit instance");
//		try {
//			TaProduit e = entityManager.merge(persistentInstance);
//			entityManager.remove(e);
//			logger.debug("remove successful");
//		} catch (RuntimeException re) {
//			logger.error("remove failed", re);
//			throw re;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaProduit)
//	 */
//	public TaProduit merge(TaProduit detachedInstance) {
//		logger.debug("merging TaProduit instance");
//		try {
//			TaProduit result = entityManager.merge(detachedInstance);
//			logger.debug("merge successful");
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("merge failed", re);
//			throw re;
//		}
//	}
//	
//	public TaProduit findByCode(String code) {
//		logger.debug("getting TaProduit instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaProduit f where f.codeUnite='"+code+"' and f.taArticle is null");
//			TaProduit instance = (TaProduit)query.getSingleResult();
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
//	public TaProduit findByCode(String code, String codeArticle) {
//		logger.debug("getting TaProduit instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaProduit f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaProduit instance = (TaProduit)query.getSingleResult();
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
//	public TaProduit findById(int id) {
//		logger.debug("getting TaProduit instance with id: " + id);
//		try {
//			TaProduit instance = entityManager.find(TaProduit.class, id);
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
//	public List<TaProduit> selectAll() {
//		logger.debug("selectAll TaProduit");
//		try {
//			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
////			System.out.println(defaultJPQLQuery);
////			System.out.println(ejbQuery.getResultList().toString());
////			System.out.println(ejbQuery.toString());
//			List<TaProduit> l = ejbQuery.getResultList();
//			logger.debug("selectAll successful");
//			return l;
//		} catch (RuntimeException re) {
//			logger.error("selectAll failed", re);
//			throw re;
//		}
//	}
//	
//	public TaProduit refresh(TaProduit detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaProduit.class, detachedInstance.getId());
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
//	public List<TaProduit> findByArticle(String codeArticle) {
//		logger.debug("getting TaProduit instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaProduit.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaProduit> l = query.getResultList();;
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
////	public ModelGeneralObjet<SWTUnite,TaProduitDAO,TaProduit> modelObjetUniteArticle(String codeArticle) {
////		return new ModelGeneralObjet<SWTUnite,TaProduitDAO,TaProduit>(findByArticle(codeArticle),SWTUnite.class,entityManager);
////	}
//	
//	public void ctrlSaisieSpecifique(TaProduit entity,String field) throws ExceptLgr {	
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
//	public List<TaProduit> findWithNamedQuery(String queryName) {
//		try {
//			Query ejbQuery = entityManager.createNamedQuery(queryName);
//			List<TaProduit> l = ejbQuery.getResultList();
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
//	public List<TaProduit> findWithJPQLQuery(String JPQLquery) {
//		try {
//			Query ejbQuery = entityManager.createQuery(JPQLquery);
//			List<TaProduit> l = ejbQuery.getResultList();
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
//	public boolean validate(TaProduit value) throws Exception {
//		BeanValidator<TaProduit> validator = new BeanValidator<TaProduit>(TaProduit.class);
//		return validator.validate(value);
//	}
//
//	@Override
//	public boolean validateField(TaProduit value, String propertyName) throws Exception {
//		BeanValidator<TaProduit> validator = new BeanValidator<TaProduit>(TaProduit.class);
//		return validator.validateField(value,propertyName);
//	}
//
//	@Override
//	public void detach(TaProduit transientInstance) {
//		entityManager.detach(transientInstance);
//	}
//
//	@Override
//	public List<TaProduit> findByIdFab(Integer id) {
//	try {
//		if(id != null){
//		Query query = entityManager.createQuery("select f from TaProduit f where f.id_fabrication='"+id+"'");
//		List<TaProduit> instance = (List<TaProduit>)query.getResultList();
//		logger.debug("get successful");
//		return instance;
//		}
//		return null;
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//		
//	}
//	
//}
