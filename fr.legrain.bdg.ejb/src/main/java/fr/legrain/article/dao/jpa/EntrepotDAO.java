package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.dao.IEntrepotDAO;
import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaEntrepot.
 * @see fr.legrain.TaEntrepot.dao.TaEntrepot
 * @author Hibernate Tools
 */
public class EntrepotDAO implements IEntrepotDAO,Serializable {

	private static final long serialVersionUID = -7259541112640013294L;
	
	
	static Logger logger = Logger.getLogger(EntrepotDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaEntrepot> uniteValidator = new ClassValidator<TaEntrepot>( TaEntrepot.class );
	
	private String defaultJPQLQuery = "select u from TaEntrepot u order by u.codeEntrepot";
	//private String defaultJPQLQuery = "select u from TaEntrepot u where u.taArticle is null";
	
	public EntrepotDAO(){
//		this(null);
	}
	
//	public TaEntrepotDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaEntrepot.class.getSimpleName());
//		initChampId(TaEntrepot.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaEntrepot());
//	}
	
//	public IStatus validate(TaEntrepot entity){
//		ClassValidator<TaEntrepot> uniteValidator = new ClassValidator<TaEntrepot>( TaEntrepot.class );
//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
//		IStatus s = null;
//		if(iv.length>0) {
//			s = new Status(0,"",iv[0].getMessage());
//		}
//		return s;
//	}
	

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaEntrepot)
	 */
	public void persist(TaEntrepot transientInstance) {
		logger.debug("persisting TaEntrepot instance");
		try {
		    if (transientInstance.getActif() == null){
			transientInstance.setActif(false);
		    	}
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaEntrepot)
	 */
	public void remove(TaEntrepot persistentInstance) {
		logger.debug("removing TaEntrepot instance");
		try {
			TaEntrepot e = entityManager.merge(findById(persistentInstance.getIdEntrepot()));
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaEntrepot)
	 */
	public TaEntrepot merge(TaEntrepot detachedInstance) {
		logger.debug("merging TaEntrepot instance");
		try {
		    	if (detachedInstance.getActif() == null){
		    	    detachedInstance.setActif(false);
		    	}
			TaEntrepot result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaEntrepot findByCode(String code) {
		logger.debug("getting TaEntrepot instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaEntrepot f where f.codeEntrepot='"+code+"' ");
			TaEntrepot instance = (TaEntrepot)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaEntrepot findByCode(String code, String codeArticle) {
		logger.debug("getting TaEntrepot instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaEntrepot f where f.codeUnite='"+code+"' and f.taArticle.codeArticle='"+codeArticle+"'");
			TaEntrepot instance = (TaEntrepot)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaEntrepot findById(int id) {
		logger.debug("getting TaEntrepot instance with id: " + id);
		try {
			TaEntrepot instance = entityManager.find(TaEntrepot.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaEntrepot> selectAll() {
		logger.debug("selectAll TaEntrepot");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
//			System.out.println(defaultJPQLQuery);
//			System.out.println(ejbQuery.getResultList().toString());
//			System.out.println(ejbQuery.toString());
			List<TaEntrepot> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaEntrepot refresh(TaEntrepot detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaEntrepot.class, detachedInstance.getIdEntrepot());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	
	/*
	public List<TaEntrepot> findByArticle(String codeArticle) {
		logger.debug("getting TaEntrepot instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaEntrepot.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaEntrepot> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/
	
	//ejb
//	public ModelGeneralObjet<SWTUnite,TaEntrepotDAO,TaEntrepot> modelObjetUniteArticle(String codeArticle) {
//		return new ModelGeneralObjet<SWTUnite,TaEntrepotDAO,TaEntrepot>(findByArticle(codeArticle),SWTUnite.class,entityManager);
//	}
	
	public List<TaEntrepotDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaEntrepot.QN.FIND_ALL_LIGHT);
			List<TaEntrepotDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaEntrepotDTO> findByCodeLight(String code) {
		logger.debug("getting TaEntrepotDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaEntrepot.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaEntrepot.QN.FIND_ALL_LIGHT);
			}

			List<TaEntrepotDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public void ctrlSaisieSpecifique(TaEntrepot entity,String field) throws ExceptLgr {	
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
	public List<TaEntrepot> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEntrepot> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEntrepot> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEntrepot> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEntrepot value) throws Exception {
		BeanValidator<TaEntrepot> validator = new BeanValidator<TaEntrepot>(TaEntrepot.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEntrepot value, String propertyName) throws Exception {
		BeanValidator<TaEntrepot> validator = new BeanValidator<TaEntrepot>(TaEntrepot.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEntrepot transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
