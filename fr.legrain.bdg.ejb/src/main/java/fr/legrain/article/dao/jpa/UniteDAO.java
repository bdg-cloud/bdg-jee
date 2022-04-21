package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IUniteDAO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaUnite;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaUnite.
 * @see fr.legrain.articles.dao.TaUnite
 * @author Hibernate Tools
 */
public class UniteDAO implements IUniteDAO {

	private static final Log logger = LogFactory.getLog(UniteDAO.class);
	//static Logger logger = Logger.getLogger(TaUniteDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaUnite> uniteValidator = new ClassValidator<TaUnite>( TaUnite.class );
	
	private String defaultJPQLQuery = "select u from TaUnite u order by u.codeUnite";
	//private String defaultJPQLQuery = "select u from TaUnite u where u.taArticle is null";
	
	public UniteDAO(){
//		this(null);
	}
	
//	public TaUniteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaUnite.class.getSimpleName());
//		initChampId(TaUnite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaUnite());
//	}
	
//	public IStatus validate(TaUnite entity){
//		ClassValidator<TaUnite> uniteValidator = new ClassValidator<TaUnite>( TaUnite.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaUnite)
	 */
	public void persist(TaUnite transientInstance) {
		logger.debug("persisting TaUnite instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaUnite)
	 */
	public void remove(TaUnite persistentInstance) {
		logger.debug("removing TaUnite instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaUnite)
	 */
	public TaUnite merge(TaUnite detachedInstance) {
		logger.debug("merging TaUnite instance");
		try {
			TaUnite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaUnite findByCode(String code) {
		logger.debug("getting TaUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaUnite f where f.codeUnite='"+code+"' and f.taArticle is null");
			TaUnite instance = (TaUnite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			//throw re;
			return null;
		}
	}
	
	public List<TaUniteDTO> findByCodeLight(String code) {
		logger.debug("getting List<TaUniteDTO> instance with code: " + code);
		try {
			
			Query query = null;
			String code2 = "KG";
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaUnite.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeUnite", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaUnite.QN.FIND_ALL_LIGHT);
			}

			List<TaUniteDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
			
		}
	}
	
	public List<TaUniteDTO> findByCodeLightUniteStock(String codeUniteStock, String code) {
		logger.debug("getting List<TaUniteDTO> instance with code: " + code);
		try {
			
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaUnite.QN.FIND_BY_CODE_LIGHT_COEFF_DEFINIT);
				query.setParameter("codeUniteStock", codeUniteStock);
				query.setParameter("codeUnite", "%"+code+"%");
			} else {				
				query = entityManager.createNamedQuery(TaUnite.QN.FIND_ALL_LIGHT_COEFF_DEFINIT);
				query.setParameter("codeUniteStock", codeUniteStock);
			}

			List<TaUniteDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
			
		}
	}
	
	public List<TaUnite> findByCodeUniteStock(String codeUniteStock, String code) {
		logger.debug("getting List<TaUniteDTO> instance with code: " + code);
		try {
			
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaUnite.QN.FIND_BY_CODE_COEFF_DEFINIT);
				query.setParameter("codeUniteStock", codeUniteStock);
				query.setParameter("codeUnite", "%"+code+"%");
			} else {				
				query = entityManager.createNamedQuery(TaUnite.QN.FIND_ALL_COEFF_DEFINIT);
				query.setParameter("codeUniteStock", codeUniteStock);
			}

			List<TaUnite> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
			
		}
	}
	
	public TaUnite findByCode(String code, String codeArticle) {
		logger.debug("getting TaUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaUnite f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaUnite instance = (TaUnite)query.getSingleResult();
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
	public TaUnite findById(int id) {
		logger.debug("getting TaUnite instance with id: " + id);
		try {
			TaUnite instance = entityManager.find(TaUnite.class, id);
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
	public List<TaUnite> selectAll() {
		logger.debug("selectAll TaUnite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaUnite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaUnite refresh(TaUnite detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaUnite.class, detachedInstance.getIdUnite());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	public List<TaUnite> findByArticle(String codeArticle) {
		logger.debug("getting TaUnite instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaUnite.QN.FIND_BY_ARTICLE);
			query.setParameter("codeArticle", codeArticle);
			List<TaUnite> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaUniteDAO,TaUnite> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaUniteDAO,TaUnite>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaUnite entity,String field) throws ExceptLgr {	
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
	public List<TaUnite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaUnite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaUnite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaUnite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaUnite value) throws Exception {
		BeanValidator<TaUnite> validator = new BeanValidator<TaUnite>(TaUnite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaUnite value, String propertyName) throws Exception {
		BeanValidator<TaUnite> validator = new BeanValidator<TaUnite>(TaUnite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaUnite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
