package fr.legrain.article.dao.jpa;

import java.util.ArrayList;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dao.IImageArticleDAO;
import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaImageArticle;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaImageArticle.
 * @see fr.legrain.articles.dao.TaImageArticle
 * @author Hibernate Tools
 */
public class ImageArticleDAO implements IImageArticleDAO {

	private static final Log logger = LogFactory.getLog(ImageArticleDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaImageArticle p";
	
	public ImageArticleDAO(){
	}

	public void persist(TaImageArticle transientInstance) {
		logger.debug("persisting TaImageArticle instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	public TaImageArticle refresh(TaImageArticle detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaImageArticle.class, detachedInstance.getIdImageArticle());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaImageArticle persistentInstance) {
		logger.debug("removing TaImageArticle instance");
		//boolean estRefPrix=false;
		try {
			/*
			 * Firebird ne supporte pas les "self referencing foreign keys" (ou reflexive foreign keys),
			 * càd une clé étrangère qui pointe sur la même table. Sans définition de foreign key pour le champ "imageOrigine"
			 * il est impossible pour JPA/Hibernate de faire le delete en cascade, il faut donc le faire manuellement.
			 */
			for(TaImageArticle img : persistentInstance.getTaImagesGenere()) {
				//img.setImageOrigine(null);
				//img.setTaArticle(null);
				entityManager.remove(img);
				
			}
			persistentInstance.getTaImagesGenere().clear();
			
			entityManager.remove(persistentInstance);

			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaImageArticle merge(TaImageArticle detachedInstance) {
		logger.debug("merging TaImageArticle instance");
		try {
			TaImageArticle result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaImageArticle findById(int id) {
		logger.debug("getting TaImageArticle instance with id: " + id);
		try {
			TaImageArticle instance = entityManager.find(TaImageArticle.class, id);
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
	public List<TaImageArticle> findByArticle(int id) {
		logger.debug("findByIdArticle");
		try {
			Query ejbQuery = entityManager.createQuery("select img from TaImageArticle img where img.taArticle.idArticle = "+id+"order by img.position");
			List<TaImageArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public List<TaImageArticleDTO> findByArticleLight(int id) {
		logger.debug("findByIdArticle");
		try {
			Query ejbQuery = entityManager.createQuery(""
					+ "select new fr.legrain.article.dto.TaImageArticleDTO("
					+ "img.idImageArticle, img.nomImageArticle,"
					+ "img.blobImageOrigine, img.blobImageGrand, img.blobImageMoyen, img.blobImagePetit,"
					//+ "img.defautImageArticle,"
					+ " img.description,img.position) "
					+ " from TaImageArticle img "
					+ " where img.taArticle.idArticle = "+id+"order by img.position");
			List<TaImageArticleDTO> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Inject IArticleDAO doaArticle;
	public void changeImageDefaut(TaImageArticle image) {
		try {
//			List<Integer> l = new ArrayList<>();
//			l.add(image.getTaArticle().getIdArticle());
//			final int changes =
////					entityManager.createQuery("update TaImageArticle img set img.defautImageArticle = false where img.taArticle.idArticle = "+image.getTaArticle().getIdArticle())
////			        .executeUpdate();
////			
////					entityManager.createQuery("update TaImageArticle img set img.defautImageArticle = true where img.idImageArticle = "+image.getIdImageArticle())
////					.executeUpdate();
//					
//					
//					
//					entityManager.createQuery("update TaArticle art set art.taImageArticle = :image where art.idArticle in :liste")
//					.setParameter("image", image)
//					.setParameter("liste", l)
//					.executeUpdate();
			
//				image.getTaArticle().setTaImageArticle(image);
//				merge(image);
				
				TaArticle art = doaArticle.findById(image.getTaArticle().getIdArticle());
				
				for (TaImageArticle i : art.getTaImageArticles()) {
					if(i.getIdImageArticle()==image.getIdImageArticle()) {
						image = i;
					}
				}
				
				//image = findById(image.getIdImageArticle());
				
				art.setTaImageArticle(image);
				image.setTaArticle(art);
				
				for (TaImageArticle i : art.getTaImageArticles()) {
					i.setTaArticle(art);
				}
				doaArticle.merge(art);
			} catch (Exception re) {
				logger.error("get failed", re);
				throw re;
			}
	}
	
	public List<TaImageArticle> selectAll() {
		logger.debug("selectAll TaArticle");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaImageArticle> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public void ctrlSaisieSpecifique(TaImageArticle entity,String field) throws ExceptLgr {	
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<TaImageArticle> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaImageArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaImageArticle> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaImageArticle> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaImageArticle value) throws Exception {
		BeanValidator<TaImageArticle> validator = new BeanValidator<TaImageArticle>(TaImageArticle.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaImageArticle value, String propertyName) throws Exception {
		BeanValidator<TaImageArticle> validator = new BeanValidator<TaImageArticle>(TaImageArticle.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaImageArticle transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public TaImageArticle findByCode(String code) {
		logger.debug("getting TaFamilleUnite instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaImageArticle f where f.codeFamille='"+code+"'");
			TaImageArticle instance = (TaImageArticle)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
}

