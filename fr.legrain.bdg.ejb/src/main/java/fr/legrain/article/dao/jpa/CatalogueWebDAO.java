package fr.legrain.article.dao.jpa;

import java.util.ArrayList;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dao.ICatalogueWebDAO;
import fr.legrain.article.dao.ICategorieArticleDAO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaCatalogueWebDTO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCatalogueWeb.
 * @see fr.legrain.articles.dao.TaCatalogueWeb
 * @author Hibernate Tools
 */
public class CatalogueWebDAO implements ICatalogueWebDAO{

	private static final Log logger = LogFactory.getLog(CatalogueWebDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	@Inject private IArticleDAO daoArticle;
	@Inject private ICategorieArticleDAO daoCategorieArticle;

	private String defaultJPQLQuery = "select p from TaCatalogueWeb p";
	
	public CatalogueWebDAO(){
//		this(null);
	}

	public List<TaArticleDTO> findListeArticleCatalogue() {
////		daoArticle.findAllLight();
////		@NamedQuery(name=TaArticle.QN.FIND_ALL_LIGHT, 
//				String jpql=""
//						+ "select "
//						+ "new fr.legrain.article.dto.TaArticleDTO("
//						+ "a.idArticle,a.codeArticle,a.libellecArticle,a.libellelArticle,"
//						+ "a.numcptArticle,p.prixPrix,un.codeUnite,fam.codeFamille,"
//						+ "tva.codeTva,tva.tauxTva,ttva.codeTTva,a.codeBarre"
//						+ ") "
//						+ "from "
//						+ "TaArticle a "
//						+ "left join a.taFamille fam "
//						//+ "left join a.taCatalogueWeb cat "
//						+ "join a.taCatalogueWeb cat "
//						+ "left join a.taPrix p "
//						+ "left join a.taTva tva "
//						+ "left join a.taTTva ttva "
//						+ "left join a.taUnite1 un "
//						+ "where "
//						+ "cat.exportationCatalogueWeb = 1 "
//						+ "order by a.codeArticle";
////						);
		
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ALL_LIGHT_CATALOGUE);
//			Query query = entityManager.createQuery(jpql);
			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaArticleDTO> findListeArticleCatalogue(int idCategorie) {
		try {
			Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_ALL_LIGHT_CATALOGUE_CATEGORIE);
			List<TaCategorieArticle> liste = new ArrayList<>();
//			liste.add();
			query.setParameter("categ", idCategorie);
			List<TaArticleDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public TaArticleDTO findArticleCatalogue(int idArticle) {
		Query query = entityManager.createNamedQuery(TaArticle.QN.FIND_LIGHT_CATALOGUE);
		query.setParameter("idArticle", idArticle);
		TaArticleDTO art = (TaArticleDTO) query.getSingleResult();
		return art;
	}
	
	public List<TaCategorieArticleDTO> findListeCategorieArticleCatalogue() {
		Query query = entityManager.createQuery("select a from TaCategorieArticle a order by codeCategorieArticle");
		List<TaCategorieArticleDTO> art = query.getResultList();
		return art;
	}
	
	public TaCategorieArticleDTO findCategorieArticleCatalogue(int idCategorie) {
		return null;
	}


	public void persist(TaCatalogueWeb transientInstance) {
		logger.debug("persisting TaCatalogueWeb instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}
	
	public TaCatalogueWeb refresh(TaCatalogueWeb detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaCatalogueWeb.class, detachedInstance.getIdCatalogueWeb());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaCatalogueWeb persistentInstance) {
		logger.debug("removing TaCatalogueWeb instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaCatalogueWeb merge(TaCatalogueWeb detachedInstance) {
		logger.debug("merging TaCatalogueWeb instance");
		try {
			TaCatalogueWeb result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaCatalogueWeb findById(int id) {
		logger.debug("getting TaCatalogueWeb instance with id: " + id);
		try {
			TaCatalogueWeb instance = entityManager.find(TaCatalogueWeb.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaCatalogueWeb findByCode(String code) {
		logger.debug("getting TaCatalogueWeb instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select a from TaCatalogueWeb a where a.codeCategorieArticle='"+code+"'");
			TaCatalogueWeb instance = (TaCatalogueWeb)query.getSingleResult();
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
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaCatalogueWeb> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaCatalogueWeb> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaCatalogueWeb entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaCatalogueWeb> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaCatalogueWeb> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaCatalogueWeb> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaCatalogueWeb> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaCatalogueWeb value) throws Exception {
		BeanValidator<TaCatalogueWeb> validator = new BeanValidator<TaCatalogueWeb>(TaCatalogueWeb.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaCatalogueWeb value, String propertyName) throws Exception {
		BeanValidator<TaCatalogueWeb> validator = new BeanValidator<TaCatalogueWeb>(TaCatalogueWeb.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaCatalogueWeb transientInstance) {
		entityManager.detach(transientInstance);
	}

}

