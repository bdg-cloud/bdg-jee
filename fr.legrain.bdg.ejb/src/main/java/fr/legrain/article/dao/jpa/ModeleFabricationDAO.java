package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IModeleFabricationDAO;
import fr.legrain.article.model.TaModeleFabrication;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaModeleFabrication.
 * @see fr.legrain.TaModeleFabrication.dao.TaModeleFabrication
 * @author Hibernate Tools
 */
public class ModeleFabricationDAO implements IModeleFabricationDAO {

	private static final Log logger = LogFactory.getLog(ModeleFabricationDAO.class);
	//static Logger logger = Logger.getLogger(TaModeleFabricationDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaModeleFabrication> uniteValidator = new ClassValidator<TaModeleFabrication>( TaModeleFabrication.class );
	
	private String defaultJPQLQuery = "select u from TaModeleFabrication u order by u.dateDocument DESC, u.codeDocument DESC";
	//private String defaultJPQLQuery = "select u from TaModeleFabrication u where u.taArticle is null";
	
	public ModeleFabricationDAO(){
//		this(null);
	}
	
//	public TaModeleFabricationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaModeleFabrication.class.getSimpleName());
//		initChampId(TaModeleFabrication.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaModeleFabrication());
//	}
	
//	public IStatus validate(TaModeleFabrication entity){
//		ClassValidator<TaModeleFabrication> uniteValidator = new ClassValidator<TaModeleFabrication>( TaModeleFabrication.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaModeleFabrication)
	 */
	public void persist(TaModeleFabrication transientInstance) {
		logger.debug("persisting TaModeleFabrication instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaModeleFabrication)
	 */
	public void remove(TaModeleFabrication persistentInstance) {
		logger.debug("removing TaModeleFabrication instance");
		try {
			TaModeleFabrication e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaModeleFabrication)
	 */
	public TaModeleFabrication merge(TaModeleFabrication detachedInstance) {
		logger.debug("merging TaModeleFabrication instance");
		try {
			TaModeleFabrication result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaModeleFabrication findByCode(String code) {
		logger.debug("getting TaModeleFabrication instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaModeleFabrication f where f.codeDocument='"+code+"'");
				TaModeleFabrication instance = (TaModeleFabrication)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaModeleFabrication findById(int id) {
		logger.debug("getting TaModeleFabrication instance with id: " + id);
		try {
			TaModeleFabrication instance = entityManager.find(TaModeleFabrication.class, id);
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
	public List<TaModeleFabrication> selectAll() {
		logger.debug("selectAll TaModeleFabrication");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaModeleFabrication> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaModeleFabrication refresh(TaModeleFabrication detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaModeleFabrication.class, detachedInstance.getIdDocument());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	
//	public List<TaModeleFabrication> findByArticle(String codeArticle) {
//		logger.debug("getting TaModeleFabrication instance with codeArticle: " + codeArticle);
//		try {
//			Query query = entityManager.createNamedQuery(TaModeleFabrication.QN.FIND_BY_ARTICLE);
//			query.setParameter(1, codeArticle);
//			List<TaModeleFabrication> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaModeleFabricationDAO,TaModeleFabrication> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaModeleFabricationDAO,TaModeleFabrication>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaModeleFabrication entity,String field) throws ExceptLgr {	
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
	public List<TaModeleFabrication> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaModeleFabrication> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaModeleFabrication> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaModeleFabrication> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaModeleFabrication value) throws Exception {
		BeanValidator<TaModeleFabrication> validator = new BeanValidator<TaModeleFabrication>(TaModeleFabrication.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaModeleFabrication value, String propertyName) throws Exception {
		BeanValidator<TaModeleFabrication> validator = new BeanValidator<TaModeleFabrication>(TaModeleFabrication.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaModeleFabrication transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
