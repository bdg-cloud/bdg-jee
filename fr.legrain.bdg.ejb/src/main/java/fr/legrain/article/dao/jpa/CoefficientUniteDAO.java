package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ICoefficientUniteDAO;
import fr.legrain.article.dao.IUniteDAO;
import fr.legrain.article.dto.TaCoefficientUniteDTO;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaCoefficientUnite.
 * @see fr.legrain.articles.dao.TaCoefficientUnite
 * @author Hibernate Tools
 */
public class CoefficientUniteDAO implements ICoefficientUniteDAO {

	private static final Log logger = LogFactory.getLog(CoefficientUniteDAO.class);
	//static Logger logger = Logger.getLogger(TaCoefficientUniteDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaCoefficientUnite> uniteValidator = new ClassValidator<TaCoefficientUnite>( TaCoefficientUnite.class );
	
	private String defaultJPQLQuery = "select u from TaCoefficientUnite u";
	//private String defaultJPQLQuery = "select u from TaCoefficientUnite u where u.taArticle is null";
	
	public CoefficientUniteDAO(){
//		this(null);
	}
	
//	public TaCoefficientUniteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaCoefficientUnite.class.getSimpleName());
//		initChampId(TaCoefficientUnite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCoefficientUnite());
//	}
	
//	public IStatus validate(TaCoefficientUnite entity){
//		ClassValidator<TaCoefficientUnite> uniteValidator = new ClassValidator<TaCoefficientUnite>( TaCoefficientUnite.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaCoefficientUnite)
	 */
	public void persist(TaCoefficientUnite transientInstance) {
		logger.debug("persisting TaCoefficientUnite instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaCoefficientUnite)
	 */
	public void remove(TaCoefficientUnite persistentInstance) {
		logger.debug("removing TaCoefficientUnite instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaCoefficientUnite)
	 */
	public TaCoefficientUnite merge(TaCoefficientUnite detachedInstance) {
		logger.debug("merging TaCoefficientUnite instance");
		try {
			TaCoefficientUnite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaCoefficientUnite findByCode(String code) {
		logger.debug("getting TaCoefficientUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaCoefficientUnite f where f.codeUnite='"+code+"' and f.taArticle is null");
			TaCoefficientUnite instance = (TaCoefficientUnite)query.getSingleResult();
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
	
	public TaCoefficientUnite findByCode(String code1, String code2) {

		try {
			if(!code1.equals("") && !code2.equals("")){
//			Query query = entityManager.createQuery("select f from TaCoefficientUnite f where (f.uniteA.codeUnite='"+code1+"' and f.uniteB.codeUnite='"+code2+"')"
//					+ " or (f.uniteB.codeUnite='"+code1+"' and f.uniteA.codeUnite='"+code2+"')"); /// !!!! plante si plusieurs lignes trouvées !!!
				
			Query query = entityManager.createQuery("select f from TaCoefficientUnite f where (f.uniteA.codeUnite='"+code1+"' and f.uniteB.codeUnite='"+code2+"')");
			TaCoefficientUnite instance = (TaCoefficientUnite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (Exception re) {
//			logger.error("get failed", re);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaCoefficientUnite findById(int id) {
		logger.debug("getting TaCoefficientUnite instance with id: " + id);
		try {
			TaCoefficientUnite instance = entityManager.find(TaCoefficientUnite.class, id);
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
	public List<TaCoefficientUnite> selectAll() {
		logger.debug("selectAll TaCoefficientUnite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCoefficientUnite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaCoefficientUnite refresh(TaCoefficientUnite detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaCoefficientUnite.class, detachedInstance.getId());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	public List<TaCoefficientUnite> findByArticle(String codeArticle) {
		logger.debug("getting TaCoefficientUnite instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaCoefficientUnite.QN.FIND_BY_ARTICLE);
			query.setParameter("codeArticle", codeArticle);
			List<TaCoefficientUnite> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaCoefficientUniteDAO,TaCoefficientUnite> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaCoefficientUniteDAO,TaCoefficientUnite>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaCoefficientUnite entity,String field) throws ExceptLgr {	
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
	public List<TaCoefficientUnite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCoefficientUnite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCoefficientUnite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCoefficientUnite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCoefficientUnite value) throws Exception {
		BeanValidator<TaCoefficientUnite> validator = new BeanValidator<TaCoefficientUnite>(TaCoefficientUnite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCoefficientUnite value, String propertyName) throws Exception {
		BeanValidator<TaCoefficientUnite> validator = new BeanValidator<TaCoefficientUnite>(TaCoefficientUnite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCoefficientUnite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
