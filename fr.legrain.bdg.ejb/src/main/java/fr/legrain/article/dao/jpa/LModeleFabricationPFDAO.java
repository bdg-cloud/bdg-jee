package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ILModeleFabricationPFDAO;
import fr.legrain.article.model.TaLModeleFabricationPF;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaLModeleFabricationPF.
 * @see fr.legrain.TaLModeleFabricationPF.dao.TaLModeleFabricationPF
 * @author Hibernate Tools
 */
public class LModeleFabricationPFDAO implements ILModeleFabricationPFDAO {

	private static final Log logger = LogFactory.getLog(LModeleFabricationPFDAO.class);
	//static Logger logger = Logger.getLogger(TaLModeleFabricationDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaLModeleFabricationPF> uniteValidator = new ClassValidator<TaLModeleFabricationPF>( TaLModeleFabricationPF.class );
	
	private String defaultJPQLQuery = "select u from TaLModeleFabricationPF u";
	//private String defaultJPQLQuery = "select u from TaLModeleFabricationPF u where u.taArticle is null";
	
	public LModeleFabricationPFDAO(){
//		this(null);
	}
	
//	public TaLModeleFabricationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLModeleFabricationPF.class.getSimpleName());
//		initChampId(TaLModeleFabricationPF.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLModeleFabricationPF());
//	}
	
//	public IStatus validate(TaLModeleFabricationPF entity){
//		ClassValidator<TaLModeleFabricationPF> uniteValidator = new ClassValidator<TaLModeleFabricationPF>( TaLModeleFabricationPF.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaLModeleFabricationPF)
	 */
	public void persist(TaLModeleFabricationPF transientInstance) {
		logger.debug("persisting TaLModeleFabricationPF instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaLModeleFabricationPF)
	 */
	public void remove(TaLModeleFabricationPF persistentInstance) {
		logger.debug("removing TaLModeleFabricationPF instance");
		try {
			TaLModeleFabricationPF e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaLModeleFabricationPF)
	 */
	public TaLModeleFabricationPF merge(TaLModeleFabricationPF detachedInstance) {
		logger.debug("merging TaLModeleFabricationPF instance");
		try {
			TaLModeleFabricationPF result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaLModeleFabricationPF findByCode(String code) {
		logger.debug("getting TaLModeleFabricationPF instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLModeleFabricationPF f where f.codeBarre='"+code+"'");
			TaLModeleFabricationPF instance = (TaLModeleFabricationPF)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
//	public TaLModeleFabricationPF findByCode(String code, String codeArticle) {
//		logger.debug("getting TaLModeleFabricationPF instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaLModeleFabricationPF f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaLModeleFabricationPF instance = (TaLModeleFabricationPF)query.getSingleResult();
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
	public TaLModeleFabricationPF findById(int id) {
		logger.debug("getting TaLModeleFabricationPF instance with id: " + id);
		try {
			TaLModeleFabricationPF instance = entityManager.find(TaLModeleFabricationPF.class, id);
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
	public List<TaLModeleFabricationPF> selectAll() {
		logger.debug("selectAll TaLModeleFabricationPF");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaLModeleFabricationPF> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	public TaLModeleFabricationPF refresh(TaLModeleFabricationPF detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLModeleFabricationPF.class, detachedInstance.getId());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	
	/*
	public List<TaLModeleFabricationPF> findByArticle(String codeArticle) {
		logger.debug("getting TaLModeleFabricationPF instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaLModeleFabricationPF.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaLModeleFabricationPF> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaLModeleFabricationDAO,TaLModeleFabricationPF> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaLModeleFabricationDAO,TaLModeleFabricationPF>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaLModeleFabricationPF entity,String field) throws ExceptLgr {	
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
	public List<TaLModeleFabricationPF> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLModeleFabricationPF> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLModeleFabricationPF> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLModeleFabricationPF> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLModeleFabricationPF value) throws Exception {
		BeanValidator<TaLModeleFabricationPF> validator = new BeanValidator<TaLModeleFabricationPF>(TaLModeleFabricationPF.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLModeleFabricationPF value, String propertyName) throws Exception {
		BeanValidator<TaLModeleFabricationPF> validator = new BeanValidator<TaLModeleFabricationPF>(TaLModeleFabricationPF.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLModeleFabricationPF transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
