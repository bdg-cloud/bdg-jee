package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:57:14 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IFamilleDAO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaFamille;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaFamille.
 * @see fr.legrain.articles.dao.TaFamille
 * @author Hibernate Tools
 */
public class FamilleDAO implements IFamilleDAO {

	private static final Log logger = LogFactory.getLog(FamilleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	//static Logger logger = Logger.getLogger(TaFamilleDAO.class.getName());

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaFamille f order by f.codeFamille";
	
	public FamilleDAO(){
//		this(null);
	}
	
//	public TaFamilleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaFamille.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaFamille());
//	}
	
//	public TaFamille refresh(TaFamille detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaFamille.class, detachedInstance.getIdFamille());
//		    return detachedInstance;
//				//entityManager.refresh(detachedInstance);
//			//logger.debug("refresh successful");
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}


	public void persist(TaFamille transientInstance) {
		logger.debug("persisting TaFamille instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaFamille persistentInstance) {
		logger.debug("removing TaFamille instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdFamille()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaFamille merge(TaFamille detachedInstance) {
		logger.debug("merging TaFamille instance");
		try {
			TaFamille result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}
	
	public TaFamille findByLibelle(String libelle) {
		logger.debug("getting TaFamille instance with libelle: " + libelle);
		try {
			if(!libelle.equals("")){
			Query query = entityManager.createQuery("select f from TaFamille f where f.libcFamille='"+libelle+"'");
			TaFamille instance = (TaFamille)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public TaFamille findByCode(String code) {
		logger.debug("getting TaFamille instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaFamille f where f.codeFamille='"+code+"'");
			TaFamille instance = (TaFamille)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaFamilleDTO> findByCodeLight(String code) {
		logger.debug("getting TaFamille instance with code: " + code);
//		try {
//			if(!code.equals("")){
//			Query query = entityManager.createQuery("select f from TaFamille f where f.codeFamille='"+code+"'");
//			TaFamille instance = (TaFamille)query.getSingleResult();
//			logger.debug("get successful");
//			return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaFamille.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeFamille", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaFamille.QN.FIND_ALL_LIGHT);
			}

			List<TaFamilleDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}

	public TaFamille findById(int id) {
		logger.debug("getting TaFamille instance with id: " + id);
		try {
			TaFamille instance = entityManager.find(TaFamille.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFamille> selectAll() {
		logger.debug("selectAll TaFamille");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaFamille> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaFamille entity,String field) throws ExceptLgr {	
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
	public List<TaFamille> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaFamille> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaFamille> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaFamille> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaFamille value) throws Exception {
		BeanValidator<TaFamille> validator = new BeanValidator<TaFamille>(TaFamille.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaFamille value, String propertyName) throws Exception {
		BeanValidator<TaFamille> validator = new BeanValidator<TaFamille>(TaFamille.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaFamille transientInstance) {
		entityManager.detach(transientInstance);
	}

	
	
	public List<TaFamilleDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaFamille.QN.FIND_ALL_LIGHT);
			List<TaFamilleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	
	public List<TaFamilleDTO> findLightSurCodeTTarif(String codeTTarif) {
		try {
			Query query = entityManager.createNamedQuery(TaFamille.QN.FIND_LIGHT_CODE_T_TARIF);
			query.setParameter("codeTTarif", codeTTarif);
			List<TaFamilleDTO> l = query.getResultList();

			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaFamilleDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers) {
		try {
			Query query = entityManager.createNamedQuery(TaFamille.QN.FIND_LIGHT_CODE_T_TARIF_TIERS);

			query.setParameter("codeTTarif", codeTTarif);
			query.setParameter("codeTiers", codeTiers);
			List<TaFamilleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
}
