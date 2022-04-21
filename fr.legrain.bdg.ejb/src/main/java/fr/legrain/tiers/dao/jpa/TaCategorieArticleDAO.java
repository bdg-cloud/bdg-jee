package fr.legrain.tiers.dao.jpa;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ICategorieArticleDAO;
import fr.legrain.article.dto.TaArticleComposeDTO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaArticleCompose;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
//import fr.legrain.gestCom.Appli.Const;
//import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCategorieArticle.
 * @see fr.legrain.articles.dao.TaCategorieArticle
 * @author Hibernate Tools
 */
public class TaCategorieArticleDAO implements ICategorieArticleDAO /*extends AbstractApplicationDAO<TaCategorieArticle>*/ {

//	private static final Log logger = LogFactory.getLog(TaCategorieArticleDAO.class);
	static final Log logger = LogFactory.getLog(TaCategorieArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaCategorieArticle p order by p.codeCategorieArticle";
	
	public TaCategorieArticleDAO(){
//		this(null);
	}

	public TaCategorieArticleDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaCategorieArticle.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaCategorieArticle());
	}
	
	public List<TaCategorieArticleDTO> findByCodeLight(String code) {
		logger.debug("getting TaCategorieArticle instance with code: " + code);
		try {
			Query query = null;

			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaCategorieArticle.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("codeCategorieArticle", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaCategorieArticle.QN.FIND_ALL_LIGHT);
			}

			List<TaCategorieArticleDTO> l = query.getResultList();
			//		List<Object[]> lm = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCategorieArticleDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaCategorieArticle.QN.FIND_ALL_LIGHT);
			List<TaCategorieArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	public void persist(TaCategorieArticle transientInstance) {
		logger.debug("persisting TaCategorieArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
//	public TaCategorieArticle refresh(TaCategorieArticle detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaCategorieArticle.class, detachedInstance.getIdCategorieArticle());
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaCategorieArticle persistentInstance) {
		logger.debug("removing TaCategorieArticle instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaCategorieArticle merge(TaCategorieArticle detachedInstance) {
		logger.debug("merging TaCategorieArticle instance");
		try {
			TaCategorieArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCategorieArticle findById(int id) {
		logger.debug("getting TaCategorieArticle instance with id: " + id);
		try {
			TaCategorieArticle instance = entityManager.find(TaCategorieArticle.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaCategorieArticle findByCode(String code) {
		logger.debug("getting TaCategorieArticle instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaCategorieArticle a where a.codeCategorieArticle='"+code+"'");
				TaCategorieArticle instance = (TaCategorieArticle)query.getSingleResult();
				logger.debug("get successful");
				return instance;
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
	public List<TaCategorieArticle> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCategorieArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaCategorieArticle> findByCodeCategorieParent(String codeCategorieParent) {
		logger.debug("getting TaCategorieArticle instance with codeCategorieParent: " + codeCategorieParent);
		List<TaCategorieArticle> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaCategorieArticle.QN.FIND_BY_PARENT);
			query.setParameter("codeCategorieParent", codeCategorieParent);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCategorieArticle> findCategorieMere() {
		logger.debug("getting TaCategorieArticle instance");
		List<TaCategorieArticle> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaCategorieArticle.QN.FIND_CATEGORIE_MERE);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Liste des categories d'article créé ou modifié par partir de la date passée en paramètre.
	 * @param d
	 * @return
	 */
	public List<TaCategorieArticle> findByNewOrUpdatedAfter(Date d) {
		logger.debug("getting TaArticle instance with quandCreeCategorieArticle or quandModifCategorieArticle >= " + d);
		try {
			List<TaCategorieArticle> l = null;
			if(d!=null) {
				Query query = entityManager.createNamedQuery(TaCategorieArticle.QN.FIND_BY_NEW_OR_UPDATED_AFTER);
				query.setParameter("d1", d);
				query.setParameter("d2", d);
				l = query.getResultList();
				logger.debug("get successful");
			}
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	public void ctrlSaisieSpecifique(TaCategorieArticle entity,String field) throws ExceptLgr {	
//		try {
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}

	@Override
	public List<TaCategorieArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCategorieArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCategorieArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCategorieArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCategorieArticle value) throws Exception {
		BeanValidator<TaCategorieArticle> validator = new BeanValidator<TaCategorieArticle>(TaCategorieArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCategorieArticle value, String propertyName)
			throws Exception {
		BeanValidator<TaCategorieArticle> validator = new BeanValidator<TaCategorieArticle>(TaCategorieArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCategorieArticle transientInstance) {
		// TODO Auto-generated method stub
		entityManager.detach(transientInstance);
	}
	
	public List<TaCategorieArticleDTO> findCategorieDTOByIdArticle(int idArticle){
		//logger.debug("getting TaArticle instance with actif: " + actif);
		try {
			//Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_NOMENCLATURE_DTO_BY_ID_ARTICLE);
			Query query = entityManager.createQuery("select new fr.legrain.article.dto.TaCategorieArticleDTO("
					+ "f.idCategorieArticle,f.codeCategorieArticle,cm.codeCategorieArticle,f.libelleCategorieArticle,f.desciptionCategorieArticle,"
					+ "f.urlRewritingCategorieArticle,f.cheminImageCategorieArticle,f.nomImageCategorieArticle) "
					+ "from TaArticle art join art.taCategorieArticles f left join f.categorieMereArticle cm "
					+ " where  art.idArticle = :idArticle order by f.codeCategorieArticle");
			query.setParameter("idArticle", idArticle);
			List<TaCategorieArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaCategorieArticle> findCategorieByIdArticle(int idArticle){
		//logger.debug("getting TaArticle instance with actif: " + actif);
		try {
			//Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_NOMENCLATURE_BY_ID_ARTICLE);
			Query query = entityManager.createQuery("select f "
					+ "from TaArticle art join art.taCategorieArticles f left join f.categorieMereArticle cm "
					+ " where  art.idArticle = :idArticle order by f.codeCategorieArticle");
			query.setParameter("idArticle", idArticle);
			List<TaCategorieArticle> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}

