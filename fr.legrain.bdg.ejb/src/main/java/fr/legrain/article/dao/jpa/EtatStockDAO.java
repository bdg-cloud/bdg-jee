package fr.legrain.article.dao.jpa;

import java.math.BigDecimal;

//Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IEtatStockDAO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.stock.dto.EtatStocksDTO;
import fr.legrain.stock.model.TaEtatStock;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCatalogueWeb.
 * @see fr.legrain.articles.dao.TaCatalogueWeb
 * @author Hibernate Tools
 */
public class EtatStockDAO implements IEtatStockDAO{

	private static final Log logger = LogFactory.getLog(EtatStockDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select p from TaEtatStock p";
	
	public EtatStockDAO(){
//		this(null);
	}




	public void persist(TaEtatStock transientInstance) {
		logger.debug("persisting TaEtatStock instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}
	
	public TaEtatStock refresh(TaEtatStock detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
		    session.evict(detachedInstance);
		    detachedInstance = entityManager.find(TaEtatStock.class, detachedInstance.getIdEtatStock());
		    return detachedInstance;
			
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}

	public void remove(TaEtatStock persistentInstance) {
		logger.debug("removing TaEtatStock instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaEtatStock merge(TaEtatStock detachedInstance) {
		logger.debug("merging TaEtatStock instance");
		try {
			TaEtatStock result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaEtatStock findById(int id) {
		logger.debug("getting TaEtatStock instance with id: " + id);
		try {
			TaEtatStock instance = entityManager.find(TaEtatStock.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaEtatStock findByCode(String codeLot) {
		logger.debug("getting TaEtatStock instance with code: " + codeLot);
		try {
			if(!codeLot.equals("")){
			Query query = entityManager.createQuery("select a from TaEtatStock a"
					+ " where a.numLot='"+codeLot+"'");
			TaEtatStock instance = (TaEtatStock)query.getSingleResult();
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
	public List<TaEtatStock> selectAll() {
		logger.debug("selectAll TaEtatStock");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaEtatStock> l = ejbQuery.getResultList();
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
	public List<TaEtatStock> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaEtatStock> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaEtatStock> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaEtatStock> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaEtatStock value) throws Exception {
		BeanValidator<TaEtatStock> validator = new BeanValidator<TaEtatStock>(TaEtatStock.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaEtatStock value, String propertyName) throws Exception {
		BeanValidator<TaEtatStock> validator = new BeanValidator<TaEtatStock>(TaEtatStock.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaEtatStock transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	public List<String>  articleEnStockString() {
		try {

			Query query = entityManager.createQuery(
					"select distinct l.taArticle.codeArticle, l.taArticle.libellecArticle from TaEtatStock m ,TaLot l  where l.numLot=m.numLot and  m.qte1EtatStock > 0");
			List<Object[]> temp =query.getResultList();
			logger.debug("get successful");
			List<String> temp2 = new ArrayList<String>();
			for(Object[] s : temp){
				if (s[0] != null && s[1] != null){
					temp2.add(s[0].toString() + "~" + s[1].toString() );
				}
			}
			return temp2;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne la liste de tous les articles en stock
	 * @return
	 */
	public List<TaArticle>  articleEnStock() {
		try {
			Query query = entityManager.createQuery("select distinct l.taArticle from TaEtatStock m ,TaLot l  where l.numLot=m.numLot and  m.qte1EtatStock > 0");
			List<TaArticle> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Retourne la qte en stock pour un lot, dans un entrepot à un emplacement particulier,
	 * retourne null si aucun stock ne correspond
	 * @param idLot
	 * @param idEntrepot
	 * @param emplacement
	 * @return
	 */
	public Integer qteArticleLotEnStock(Integer idLot, Integer idEntrepot, String emplacement) {
		try {
			String q = "select m.qte1EtatStock from TaEtatStock m , TaLot l  where l.numLot=m.numLot and l.idLot="+idLot;
			if(idEntrepot!=null) {
				q+=" and m.taEntrepot.idEntrepot="+idEntrepot;
			}
			if(emplacement!=null) {
				q+=" and m.emplacement='"+emplacement+"'";
			}
			Query query = entityManager.createQuery(q);
			BigDecimal temp = (BigDecimal)query.getSingleResult();
			logger.debug("get successful");
			return temp!=null?temp.intValue():null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	public Integer supprimeEtatStockLot(Integer idLot) {
		try {
			String q = "delete from TaEtatStock m , TaLot l  where l.numLot=m.numLot and l.idLot="+idLot;
			Query query = entityManager.createQuery(q);
			Integer nb = (Integer)query.executeUpdate();
			logger.debug("supprimeEtatStockLot successful");
			return nb;
		} catch (RuntimeException re) {
			logger.error("supprimeEtatStockLot failed", re);
			return null;
		}
	}
	
	/**
	 * Retourne la qte en stock pour un lot, dans un entrepot
	 * retourne null si aucun stock ne correspond
	 * @param idLot
	 * @param idEntrepot
	 * @return
	 */
	public Integer qteArticleLotEnStock(Integer idLot, Integer idEntrepot) {
		return qteArticleEnStock(idLot,idEntrepot,null);
	}
	
	/**
	 * Retourne la qte en stock pour un lot
	 * retourne null si aucun stock ne correspond
	 * @param idLot
	 * @return
	 */
	public Integer qteArticleLotEnStock(Integer idLot) {
		return qteArticleLotEnStock(idLot,null,null);
	}
	
	/**
	 * Retourne la qte en stock pour un article, dans un entrepot à un emplacement particulier,
	 * retourne null si aucun stock ne correspond
	 * @param idArticle
	 * @param idEntrepot
	 * @param emplacement
	 * @return
	 */
	public Integer qteArticleEnStock(Integer idArticle, Integer idEntrepot, String emplacement) {
		try {
			String q = "select m.qte1EtatStock from TaEtatStock m ,TaLot l join l.taArticle a where l.numLot=m.numLot and a.idArticle="+idArticle;
			if(idEntrepot!=null) {
				q+=" and m.taEntrepot.idEntrepot="+idEntrepot;
			}
			if(emplacement!=null) {
				q+=" and m.emplacement='"+emplacement+"'";
			}
			Query query = entityManager.createQuery(q);
			Integer temp = (Integer)query.getSingleResult();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	/**
	 * Retourne la qte en stock pour un article, dans un entrepot
	 * retourne null si aucun stock ne correspond
	 * @param idArticle
	 * @param idEntrepot
	 * @return
	 */
	public Integer qteArticleEnStock(Integer idArticle, Integer idEntrepot) {
		return qteArticleEnStock(idArticle,idEntrepot,null);
	}
	
	/**
	 * Retourne la qte en stock pour un article
	 * retourne null si aucun stock ne correspond
	 * @param idArticle
	 * @return
	 */
	public Integer qteArticleEnStock(Integer idArticle) {
		return qteArticleEnStock(idArticle,null,null);
	}
	
	/**
	 * Liste d'entrepot dans lequel on peut trouver c'est article
	 * @param codeArticle
	 * @param numLot
	 * @param emplacement
	 * @param termine
	 * @return
	 */
	public List<TaEntrepot> entrepotLotArticle(String codeArticle, String numLot, String emplacement, Boolean termine) {
		try {
			String q = "select distinct m.taEntrepot from TaEtatStock m  ,TaLot l  where l.numLot=m.numLot and l.taArticle.codeArticle='"+codeArticle+"'";
			if(numLot!=null) {
				q+=" and l.numLot='"+numLot+"'";
			}
			if(emplacement!=null) {
				q+=" and m.emplacement='"+emplacement+"'";
			}
			if(termine!=null) {
				if(termine) {
					q+=" and l.termine = true";
				} else {
					q+=" and l.termine = false";
				}
			}
			Query query = entityManager.createQuery(q);
			List<TaEntrepot> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<String> emplacementLotArticle(String codeArticle, String numLot, String codeEntrepot, Boolean termine) {
		try {
//			String q = "select distinct m.emplacement from TaEtatStock m join m.taEntrepot e  ,TaLot l     where l.numLot=m.numLot and  l.taArticle.codeArticle='"+codeArticle+"'";
//			if(numLot!=null) {
//				q+=" and l.numLot='"+numLot+"'";
//			}
			String q = "select distinct m.emplacement from TaEtatStock m join m.taEntrepot e    where m.emplacement is not null  ";
			if(numLot!=null) {
				q+=" and m.numLot='"+numLot+"'";
			}			
			if(codeEntrepot!=null) {
				q+=" and e.codeEntrepot='"+codeEntrepot+"'";
			}
			if(termine!=null) {
				if(termine) {
					q+=" and l.termine = true";
				} else {
					q+=" and l.termine = false";
				}
			}
			Query query = entityManager.createQuery(q);
			List<String> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<String> emplacementLotArticle(String codeArticle, String codeEntrepot) {
		return emplacementLotArticle(codeArticle, null, codeEntrepot, false);
	}
	
	
	public List<String> emplacementEntrepot(String codeEntrepot, Boolean termine) {
		try {
			String q = "select distinct m.emplacement from TaEtatStock m ,TaLot l  where l.numLot=m.numLot and  m.taEntrepot.codeEntrepot='"+codeEntrepot+"'";
			if(termine!=null) {
				if(termine) {
					q+=" and l.termine = true";
				} else {
					q+=" and l.termine = false";
				}
			}
			q+=" order by m.emplacement";
			Query query = entityManager.createQuery(q);
			List<String> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<String> emplacementEntrepot(String codeEntrepot) {
		return emplacementEntrepot(codeEntrepot, null);
	}
	
	public List<TaEtatStock> qteLotentrepotStock( String idEntrepot, String numLot, Boolean termine) {
		try {
			String q = "select m from TaEtatStock m ,TaLot l left join m.taEntrepot e where l.numLot=m.numLot and e.codeEntrepot='"+idEntrepot+"'";
			if(numLot!=null) {
				q+=" and l.numLot='"+numLot+"'";
			}
			if(termine!=null) {
				if(termine) {
					q+=" and l.termine = true";
				} else {
					q+=" and l.termine = false";
				}
			}
			q+=" order by m.taEntrepot.codeEntrepot";
			Query query = entityManager.createQuery(q);
			List<TaEtatStock> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	
	public List<TaEtatStock> qteLotentrepotStock( String idEntrepot, String numLot) {
		return qteLotentrepotStock(idEntrepot,numLot,false);
	}
	
	
	public List<EtatStocksDTO> qteLotentrepotStockLight( String idEntrepot, String numLot, Boolean termine) {
		try {
//			String q ="select new fr.legrain.stock.dto.EtatStocksDTO(a.idEtatStock,a.dateEtatStock,a.libelleEtatStock,a.qte1EtatStock,a.un1EtatStock,a.qte2EtatStock,a.un2EtatStock,e.codeEntrepot,art.codeArticle,a.emplacement,a.versionObj) from TaEtatStock a left join a.taEntrepot e ,TaLot l join l.taArticle art where l.numLot=a.numLot and e.codeEntrepot='"+idEntrepot+"'";
			String q = "select new fr.legrain.stock.dto.EtatStocksDTO(a.idEtatStock,a.dateEtatStock,a.libelleEtatStock,a.qte1EtatStock,a.un1EtatStock,a.qte2EtatStock,"
					+ "a.un2EtatStock,e.codeEntrepot,art.codeArticle,l.numLot,l.dluo,l.termine,a.emplacement,fm.codeFamille,a.versionObj,a.unRef,a.qteRef) "
					+ "from TaEtatStock a ,TaLot l left join a.taEntrepot e join l.taArticle art left join art.taFamille fm where l.numLot=a.numLot and e.codeEntrepot='"+idEntrepot+"'";
			if(numLot!=null) {
				q+=" and l.numLot='"+numLot+"'";
			}
			if(termine!=null) {
				if(termine) {
					q+=" and l.termine = true";
				} else {
					q+=" and l.termine = false";
				}
			}
			q+=" order by fm.codeFamille, art.codeArticle, a.dateEtatStock asc, l.numLot";
			Query query = entityManager.createQuery(q);
			List<EtatStocksDTO> temp =query.getResultList();
			logger.debug("get successful");
			return temp;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
		}
	}
	
	public List<EtatStocksDTO> qteLotentrepotStockLight( String idEntrepot, String numLot) {
		return qteLotentrepotStockLight(idEntrepot,numLot,false);
	}
	
	public void recalculEtatStock() {
		logger.debug("getting recalculEtatStock ");
		try {
			StoredProcedureQuery query = entityManager.createStoredProcedureQuery("recalcul_etat_stock");
			query.execute();
			logger.debug("get successful");
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}




	@Override
	public List<EtatStocksDTO> findByCodeLight(String code) {
		logger.debug("getting EtatStocksDTO instance");
		try {
			Query query = null;
			if(code!=null && !code.equals("") && !code.equals("*")) {
				query = entityManager.createNamedQuery(TaEtatStock.QN.FIND_BY_CODE_LIGHT);
				query.setParameter("code", "%"+code+"%");
			} else {
				query = entityManager.createNamedQuery(TaEtatStock.QN.FIND_ALL_LIGHT);
			}

			List<EtatStocksDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}




	@Override
	public List<EtatStocksDTO> findAllLight() {
		return findAllLight(null);
//		try {
//			Query query = entityManager.createNamedQuery(TaEtatStock.QN.FIND_ALL_LIGHT);
//			List<EtatStocksDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
	}
	
	public List<EtatStocksDTO> findAllLight(Boolean termine) {
		try {
			Query query = null;
			if(termine!=null) {
				query = entityManager.createNamedQuery(TaEtatStock.QN.FIND_ALL_LIGHT_TERMINE);
			} else {
				query = entityManager.createNamedQuery(TaEtatStock.QN.FIND_ALL_LIGHT);
			}
			query.setParameter("termine", termine);
			List<EtatStocksDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
}

