package fr.legrain.article.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IRefArticleFournisseurDAO;
import fr.legrain.article.dto.TaRefArticleFournisseurDTO;
import fr.legrain.article.model.TaRefArticleFournisseur;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaRefArticleFournisseur.
 * @see fr.legrain.articles.dao.TaRefArticleFournisseur
 * @author Hibernate Tools
 */
public class RefArticleFournisseurDAO implements IRefArticleFournisseurDAO {

	private static final Log logger = LogFactory.getLog(RefArticleFournisseurDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaRefArticleFournisseur p";
	
	public RefArticleFournisseurDAO(){
//		this(null);
	}

//	public TaRefArticleFournisseurDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaRefArticleFournisseur.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaRefArticleFournisseur());
//	}


	public void persist(TaRefArticleFournisseur transientInstance) {
		logger.debug("persisting TaRefArticleFournisseur instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	


	public void remove(TaRefArticleFournisseur persistentInstance) {
		logger.debug("removing TaRefArticleFournisseur instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaRefArticleFournisseur merge(TaRefArticleFournisseur detachedInstance) {
		logger.debug("merging TaRefArticleFournisseur instance");
		try {
			TaRefArticleFournisseur result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaRefArticleFournisseur findById(int id) {
		logger.debug("getting TaRefArticleFournisseur instance with id: " + id);
		try {
			TaRefArticleFournisseur instance = entityManager.find(TaRefArticleFournisseur.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaRefArticleFournisseur findByCode(String code) {
		logger.debug("getting TaRefArticleFournisseur instance with codeArticle: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaRefArticleFournisseur a where a.codeArticle='"+code+"'");
			TaRefArticleFournisseur instance = (TaRefArticleFournisseur)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}	

	
	public TaRefArticleFournisseurDTO findByCodeFournisseurAndCodeBarreLight(String codeBarre , String codeFournisseur) {
		try {
			if(!codeBarre.equals("") && !codeFournisseur.equals("")){
			Query query = entityManager.createNamedQuery(TaRefArticleFournisseur.QN.FIND_BY_FOURNISSEUR_AND_CODEBARRE_LIGHT);
			query.setParameter("codeBarre", codeBarre);
			query.setParameter("codeFournisseur", codeFournisseur);
			TaRefArticleFournisseurDTO instance = (TaRefArticleFournisseurDTO)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			//logger.error("get failed", re);
			return null;
		}
	}	

	
	public List<TaRefArticleFournisseurDTO> findByCodeFournisseurLight( String codeFournisseur) {
		try {
			if( !codeFournisseur.equals("")){
				Query query = entityManager.createNamedQuery(TaRefArticleFournisseur.QN.FIND_BY_FOURNISSEUR_LIGHT);
				query.setParameter("codeFournisseur", codeFournisseur);
			List<TaRefArticleFournisseurDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	public TaRefArticleFournisseur findByCodeFournisseurAndCodeBarre(String codeBarre , String codeFournisseur) {
		try {
			if(!codeBarre.equals("") && !codeFournisseur.equals("")){
			Query query = entityManager.createQuery("select a from TaRefArticleFournisseur join a.taFournisseur f a where "
					+ "a.codeBarreFournisseur='"+codeBarre+"' and f.codeTiers='"+codeFournisseur+"'");
			TaRefArticleFournisseur instance = (TaRefArticleFournisseur)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}	

	
	public List<TaRefArticleFournisseur> findByCodeFournisseur( String codeFournisseur) {
		try {
			if( !codeFournisseur.equals("")){
			Query query = entityManager.createQuery("select a from TaRefArticleFournisseur join a.taFournisseur f a where "
					+ " f.codeTiers='"+codeFournisseur+"'");
			List<TaRefArticleFournisseur> l = query.getResultList();
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaRefArticleFournisseur> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaRefArticleFournisseur> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaRefArticleFournisseur entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaRefArticleFournisseur> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaRefArticleFournisseur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaRefArticleFournisseur> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaRefArticleFournisseur> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaRefArticleFournisseur value) throws Exception {
		BeanValidator<TaRefArticleFournisseur> validator = new BeanValidator<TaRefArticleFournisseur>(TaRefArticleFournisseur.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaRefArticleFournisseur value, String propertyName) throws Exception {
		BeanValidator<TaRefArticleFournisseur> validator = new BeanValidator<TaRefArticleFournisseur>(TaRefArticleFournisseur.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaRefArticleFournisseur transientInstance) {
		entityManager.detach(transientInstance);
	}
	
}

