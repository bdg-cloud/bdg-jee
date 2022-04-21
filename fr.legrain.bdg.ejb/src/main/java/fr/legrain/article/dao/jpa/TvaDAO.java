package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 5:00:48 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.model.TaFamille;
import fr.legrain.article.model.TaTva;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTva.
 * @see fr.legrain.articles.dao.TaTva
 * @author Hibernate Tools
 */
public class TvaDAO implements ITvaDAO {

	private static final Log logger = LogFactory.getLog(TvaDAO.class);
	//static Logger logger = Logger.getLogger(TaTvaDAO.class.getName());
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	//@PersistenceContext
	//private EntityManager entityManager = entityManager;
	
	private String defaultJPQLQuery = "select f from TaTva f order by f.codeTva";
	
	public TvaDAO(){
//		this(null);
	}
	
//	public TaTvaDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTva.class.getSimpleName());
//		initChampId(TaTva.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTva());
//	}
	
//	public TaTva refresh(TaTva detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTva.class, detachedInstance.getIdTva());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaTva transientInstance) {
		logger.debug("persisting TaTva instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTva persistentInstance) {
		logger.debug("removing TaTva instance");
		try {
			entityManager.remove(findById(persistentInstance.getIdTva()));
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaTva merge(TaTva detachedInstance) {
		logger.debug("merging TaTva instance");
		try {
			TaTva result = entityManager.merge(detachedInstance);
			//entityManager.flush();
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTva findById(int id) {
		logger.debug("getting TaTva instance with id: " + id);
		try {
			TaTva instance = entityManager.find(TaTva.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTva findByCode(String code) {
		logger.debug("getting TaTva instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select f from TaTva f where f.codeTva='"+code+"'");
				TaTva instance = (TaTva)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
		    return null;
		}
	}
	
	public List<TaTvaDTO> findByCodeLight(String code) {
		logger.debug("getting TaTVA instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaTva.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeTva", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaTva.QN.FIND_ALL_LIGHT);
			}

			List<TaTvaDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
		
	}
	
	/**
	 * Retourne un code code TVA sur vente (commence par V), pour un taux particulier
	 * @param taux
	 * @return
	 */
	public TaTva findByTaux(String taux) {
		logger.debug("getting TaTva instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaTva f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				TaTva instance = (TaTva)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne un code code TVA sur vente (commence par V), pour un taux particulier,
	 * si aucun code de TVA sur vente ne correspond à ce taux, un nouveau code est créé.
	 * @param taux
	 * @return
	 */
	public TaTva findByTauxOrCreate(String taux) {
		logger.debug("getting TaTva instance with taux: " + taux);
		try {
			if(!taux.equals("")){
				Query query = entityManager.createQuery("select f from TaTva f where f.tauxTva='"+taux+"' and f.codeTva like 'V%'");
				if(query.getResultList().isEmpty()) {
					//Aucun code de TVA de vente existe avec ce taux => création d'un nouveau code TVA
//					EntityTransaction tx = entityManager.getTransaction();
//					tx.begin();
					TaTva tva = new TaTva();
					tva.setTauxTva(LibConversion.stringToBigDecimal(taux));
					tva.setLibelleTva("TVA à "+taux);
					
					//Recherche du prochain numéro pour créer le code automatiquement V1,V2, ... V?
					Query q = entityManager.createQuery("select f from TaTva f where f.codeTva like 'V%'");
					List<TaTva> listeTvaVente = q.getResultList();
					String debut = null;
					int max = 0;
					for (TaTva taTva : listeTvaVente) {
						debut = taTva.getCodeTva().substring(1);
						int i = LibConversion.stringToInteger(debut);
						if(i>max) max=i;
					}
					max++;
					
					tva.setCodeTva("V"+max);
					tva.setNumcptTva("445");
					try {
//						enregistrerMerge(tva);
//						commit(tx);
					} catch (Exception e) {
						logger.error("", e);
					}
					return tva;
				} else {
					TaTva instance = (TaTva) query.getResultList().get(0);
					return instance;
				}

			}
			return null;
		} catch (NoResultException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaTva> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTva> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaTva entity,String field) throws ExceptLgr {	
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
	public List<TaTva> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTva> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTva> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTva> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTva value) throws Exception {
		BeanValidator<TaTva> validator = new BeanValidator<TaTva>(TaTva.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTva value, String propertyName) throws Exception {
		BeanValidator<TaTva> validator = new BeanValidator<TaTva>(TaTva.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTva transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}
