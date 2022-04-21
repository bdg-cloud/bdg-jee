package fr.legrain.article.dao.jpa;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.ILInventaireDAO;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.stock.dto.LInventaireDTO;
import fr.legrain.stock.model.TaLInventaire;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCatalogueWeb.
 * @see fr.legrain.articles.dao.TaCatalogueWeb
 * @author Hibernate Tools
 */
public class LInventaireDAO implements ILInventaireDAO{

	private static final Log logger = LogFactory.getLog(LInventaireDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaLInventaire p";
	
	public LInventaireDAO(){
//		this(null);
	}




	public void persist(TaLInventaire transientInstance) {
		logger.debug("persisting TaLInventaire instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
	public TaLInventaire refresh(TaLInventaire detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaLInventaire.class, detachedInstance.getIdLInventaire());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaLInventaire persistentInstance) {
		logger.debug("removing TaLInventaire instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaLInventaire merge(TaLInventaire detachedInstance) {
		logger.debug("merging TaLInventaire instance");
		try {
			TaLInventaire result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaLInventaire findById(int id) {
		logger.debug("getting TaLInventaire instance with id: " + id);
		try {
			TaLInventaire instance = entityManager.find(TaLInventaire.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaLInventaire findByCode(String codeInventaire) {
		logger.debug("getting TaLInventaire instance with code: " + codeInventaire);
		try {
			if(!codeInventaire.equals("")){
			Query query = entityManager.createQuery("select a from TaLInventaire a "
					+ " where (a.codeInventaire)='"+codeInventaire+"'");
			TaLInventaire instance = (TaLInventaire)query.getSingleResult();
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
	public List<TaLInventaire> selectAll() {
		logger.debug("selectAll TaLInventaire");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLInventaire> l = ejbQuery.getResultList();
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
	public List<TaLInventaire> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLInventaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLInventaire> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLInventaire> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLInventaire value) throws Exception {
		BeanValidator<TaLInventaire> validator = new BeanValidator<TaLInventaire>(TaLInventaire.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLInventaire value, String propertyName) throws Exception {
		BeanValidator<TaLInventaire> validator = new BeanValidator<TaLInventaire>(TaLInventaire.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLInventaire transientInstance) {
		entityManager.detach(transientInstance);
	}

	
	public List<LInventaireDTO> findAllLight(Integer idInventaire) {
		try {
			Query query = entityManager.createQuery("select new fr.legrain.stock.dto.LInventaireDTO( a.id,  i.idInventaire,	 i.codeInventaire,  a.libelleInventaire, a.qteReelle,  a.un1Inventaire,"+
			 " a.qteTheorique,  a.qte2LInventaire,  a.un2Inventaire,  l.numLot, l.dluo, l.termine, art.codeArticle,  e.codeEntrepot,  a.emplacement, a.versionObj , f.codeFamille, a.ligneControlee )"+
			" from TaLInventaire a 	join a.taInventaire i left join a.taLot l left join l.taArticle art left join a.taEntrepot e "
			+ " left join art.taFamille f  where (i.idInventaire)="+idInventaire+" order by a.id ");			
			List<LInventaireDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}

