package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.ITypeMouvementDAO;
import fr.legrain.article.model.TaTypeMouvement;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTypeMouvement.
 * @see fr.legrain.TaTypeMouvement.dao.TaTypeMouvement
 * @author Hibernate Tools
 */
public class TypeMouvementDAO implements ITypeMouvementDAO ,Serializable {


	private static final long serialVersionUID = -9130654005073139044L;
	
	
	static Logger logger = Logger.getLogger(TypeMouvementDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	
	private String defaultJPQLQuery = "select u from TaTypeMouvement u";
	
	public TypeMouvementDAO(){
	}
	
//	public TaTypeMouvementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTypeMouvement.class.getSimpleName());
//		initChampId(TaTypeMouvement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTypeMouvement());
//	}
	
//	public IStatus validate(TaTypeMouvement entity){
//		ClassValidator<TaTypeMouvement> uniteValidator = new ClassValidator<TaTypeMouvement>( TaTypeMouvement.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaTypeMouvement)
	 */
	public void persist(TaTypeMouvement transientInstance) {
		logger.debug("persisting TaTypeMouvement instance");
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
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaTypeMouvement)
	 */
	public void remove(TaTypeMouvement persistentInstance) {
		logger.debug("removing TaTypeMouvement instance");
		try {
			TaTypeMouvement e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaTypeMouvement)
	 */
	public TaTypeMouvement merge(TaTypeMouvement detachedInstance) {
		logger.debug("merging TaTypeMouvement instance");
		try {
			TaTypeMouvement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaTypeMouvement findByCode(String code) {
		logger.debug("getting TaTypeMouvement instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaTypeMouvement f where f.code='"+code+"'");
			TaTypeMouvement instance = (TaTypeMouvement)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public Boolean typeMouvementReserve(String code) {
		logger.debug("getting typeMouvementReserve instance with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaTypeMouvement f where f.code='"+code+"' and f.systeme=true");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaTypeMouvement findById(int id) {
		logger.debug("getting TaTypeMouvement instance with id: " + id);
		try {
			TaTypeMouvement instance = entityManager.find(TaTypeMouvement.class, id);
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
	public List<TaTypeMouvement> selectAll() {
		logger.debug("selectAll TaTypeMouvement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaTypeMouvement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaTypeMouvement refresh(TaTypeMouvement detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaTypeMouvement.class, detachedInstance.getIdTypeMouvement());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaTypeMouvement> findByArticle(String codeArticle) {
		logger.debug("getting TaTypeMouvement instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaTypeMouvement.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaTypeMouvement> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaTypeMouvementDAO,TaTypeMouvement> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaTypeMouvementDAO,TaTypeMouvement>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public void ctrlSaisieSpecifique(TaTypeMouvement entity,String field) throws ExceptLgr {	
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
	public List<TaTypeMouvement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTypeMouvement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTypeMouvement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTypeMouvement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTypeMouvement value) throws Exception {
		BeanValidator<TaTypeMouvement> validator = new BeanValidator<TaTypeMouvement>(TaTypeMouvement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTypeMouvement value, String propertyName) throws Exception {
		BeanValidator<TaTypeMouvement> validator = new BeanValidator<TaTypeMouvement>(TaTypeMouvement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTypeMouvement transientInstance) {
		entityManager.detach(transientInstance);
	}

	
}
