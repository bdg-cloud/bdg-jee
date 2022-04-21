package fr.legrain.conformite.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.conformite.dao.IConformiteDAO;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaConformite.
 * @see fr.legrain.articles.dao.TaConformite
 * @author Hibernate Tools
 */
public class ConformiteDAO implements IConformiteDAO {

	private static final Log logger = LogFactory.getLog(ConformiteDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaConformiteDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaConformite f order by f.idConformite";
	
	public ConformiteDAO(){
//		this(null);
	}
	
//	public TaConformiteDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaConformite.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaConformite());
//	}
	
//	public TaConformite refresh(TaConformite detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaConformite.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaConformite transientInstance) {
		logger.debug("persisting TaConformite instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaConformite persistentInstance) {
		logger.debug("removing TaConformite instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdConformite()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaConformite merge(TaConformite detachedInstance) {
		logger.debug("merging TaConformite instance");
		try {
			TaConformite result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaConformite findByCode(String code) {
		logger.debug("getting TaConformite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaConformite f where f.codeFamille='"+code+"'");
			TaConformite instance = (TaConformite)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public Boolean controleUtiliseDansUnLot(int idControleConformite) {
		logger.debug("getting TaResultatConformite instance with idControleConformite: " + idControleConformite);
		try {
				Query query = entityManager.createQuery("select rc from TaResultatConformite rc join rc.taConformite c where c.idConformite="+idControleConformite+"").setMaxResults(1);
				TaResultatConformite instance = (TaResultatConformite)query.getSingleResult();
				logger.debug("get successful");
				if(instance!=null) {
					return true;
				} else {
					return false;
				}
		} catch (RuntimeException re) {
		    return false;
		}
	}
	
	public List<TaConformite> controleArticleDerniereVersion(int idArticle) {
		logger.debug("getting TaConformite instance with idArticle: " + idArticle);
		try {

			Query query = entityManager.createQuery(
					"select c from TaConformite c "
					+ " where ("
						+ "c.idConformite "
						+ " in ("
						+ "	select c1 from TaConformite c1 "
							+ "	where c1.taArticle.idArticle="+idArticle+" and c1.taConformiteParentAvantModif is null "
							+ " and not exists ("
								+ "	select c4 from TaConformite c4 "
									+ " where c4.taConformiteParentAvantModif is not null "
									+ " and c4.taConformiteParentAvantModif.idConformite = c1.idConformite"
								+ ") "
							+ ")"
						+ " or c.idConformite "
						+ " in ("
						+ " select c2 from TaConformite c2 "
							+ " where c2.idConformite "
							+ " in ("
								+ " select cc.idConformite from TaConformite cc join cc.taConformiteParentAvantModif c3 "
									+ " where cc.taArticle.idArticle="+idArticle+" "
									+ " and cc.taConformiteParentAvantModif is not null "
									+ " and cc.versionCtrl = ("
										+ "select max(z.versionCtrl) from  TaConformite z "
										+ " where z.taConformiteParentAvantModif is not null "
										+ " and z.taConformiteParentAvantModif.idConformite = cc.taConformiteParentAvantModif.idConformite"
										+ ")"
								+ ")"
						+ ")"
						+ ") and c.supprime=false order by c.position"
						);
				
				List<TaConformite> instance = (List<TaConformite>)query.getResultList();
				logger.debug("get successful");
				return instance;
		} catch (RuntimeException re) {
		    return null;
		}
	}

	public TaConformite findById(int id) {
		logger.debug("getting TaConformite instance with id: " + id);
		try {
			TaConformite instance = entityManager.find(TaConformite.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaConformite> selectAll() {
		logger.debug("selectAll TaConformite");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaConformite> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaConformite entity,String field) throws ExceptLgr {	
		try {
//			if(field.equals(Const.C_CODE_TVA)){
//				if(!entity.getCodeTva().equals("")){
//				}					
//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaConformite> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaConformite> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaConformite> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaConformite value) throws Exception {
		BeanValidator<TaConformite> validator = new BeanValidator<TaConformite>(TaConformite.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaConformite value, String propertyName) throws Exception {
		BeanValidator<TaConformite> validator = new BeanValidator<TaConformite>(TaConformite.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaConformite transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
