package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ILFabricationPFDAO;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaLFabricationPF.
 * @see fr.legrain.TaLFabricationPF.dao.TaLFabricationPF
 * @author Hibernate Tools
 */
public class LFabricationPFDAO implements ILFabricationPFDAO {

	private static final Log logger = LogFactory.getLog(LFabricationPFDAO.class);
	//static Logger logger = Logger.getLogger(TaLFabricationDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaLFabricationPF> uniteValidator = new ClassValidator<TaLFabricationPF>( TaLFabricationPF.class );
	
	private String defaultJPQLQuery = "select u from TaLFabricationPF u";
	//private String defaultJPQLQuery = "select u from TaLFabricationPF u where u.taArticle is null";
	
	public LFabricationPFDAO(){
//		this(null);
	}
	
//	public TaLFabricationDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLFabricationPF.class.getSimpleName());
//		initChampId(TaLFabricationPF.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLFabricationPF());
//	}
	
//	public IStatus validate(TaLFabricationPF entity){
//		ClassValidator<TaLFabricationPF> uniteValidator = new ClassValidator<TaLFabricationPF>( TaLFabricationPF.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaLFabricationPF)
	 */
	public void persist(TaLFabricationPF transientInstance) {
		logger.debug("persisting TaLFabricationPF instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaLFabricationPF)
	 */
	public void remove(TaLFabricationPF persistentInstance) {
		logger.debug("removing TaLFabricationPF instance");
		try {
			TaLFabricationPF e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaLFabricationPF)
	 */
	public TaLFabricationPF merge(TaLFabricationPF detachedInstance) {
		logger.debug("merging TaLFabricationPF instance");
		try {
			TaLFabricationPF result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaLFabricationPF findByCode(String code) {
		logger.debug("getting TaLFabricationPF instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaLFabricationPF f where f.codeBarre='"+code+"'");
			TaLFabricationPF instance = (TaLFabricationPF)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			return null;
		}
	}
	
//	public TaLFabricationPF findByCode(String code, String codeArticle) {
//		logger.debug("getting TaLFabricationPF instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaLFabricationPF f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
//			TaLFabricationPF instance = (TaLFabricationPF)query.getSingleResult();
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
	public TaLFabricationPF findById(int id) {
		logger.debug("getting TaLFabricationPF instance with id: " + id);
		try {
			TaLFabricationPF instance = entityManager.find(TaLFabricationPF.class, id);
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
	public List<TaLFabricationPF> selectAll() {
		logger.debug("selectAll TaLFabricationPF");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaLFabricationPF> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	public TaLFabricationPF refresh(TaLFabricationPF detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaLFabricationPF.class, detachedInstance.getId());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	
	/*
	public List<TaLFabricationPF> findByArticle(String codeArticle) {
		logger.debug("getting TaLFabricationPF instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaLFabricationPF.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaLFabricationPF> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaLFabricationDAO,TaLFabricationPF> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaLFabricationDAO,TaLFabricationPF>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaLFabricationPF entity,String field) throws ExceptLgr {	
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
	public List<TaLFabricationPF> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLFabricationPF> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLFabricationPF> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLFabricationPF> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLFabricationPF value) throws Exception {
		BeanValidator<TaLFabricationPF> validator = new BeanValidator<TaLFabricationPF>(TaLFabricationPF.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLFabricationPF value, String propertyName) throws Exception {
		BeanValidator<TaLFabricationPF> validator = new BeanValidator<TaLFabricationPF>(TaLFabricationPF.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLFabricationPF transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
