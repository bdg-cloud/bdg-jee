package fr.legrain.article.titretransport.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.titretransport.dao.IStockCapsulesDAO;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.article.titretransport.model.TaStockCapsules;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaStock.
 * @see fr.legrain.stocks.dao.TaStock
 * @author Hibernate Tools
 */
public class StockCapsulesDAO  implements IStockCapsulesDAO /*extends AbstractApplicationDAO<TaStockCapsules>*/ {

	private static final Log logger = LogFactory.getLog(StockCapsulesDAO.class);//
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	private String defaultJPQLQuery = "select u from TaStockCapsules u";
	
//	private String JPQLQueryMouvEntrees="select  S.taArticle,S.mouvStock, "+
//    "S.dateStock, sum(S.qte1Stock), S.un1Stock , sum(S.qte2Stock), S.un2Stock "+
//    "from TaStock S where S.taArticle is not null and upper(S.mouvStock) = 'E' and (S.qte1Stock is not null or S.qte2Stock is not null) "
//    +"group by S.taArticle.codeArticle,S.mouvStock, S.dateStock,S.un1Stock ,S.un2Stock";
//	
//	private String JPQLQueryMouvSorties="select S.taArticle,S.mouvStock, "+
//    "S.dateStock, sum(-1*S.qte1Stock), S.un1Stock , sum(-1*S.qte2Stock), S.un2Stock "+
//    "from TaStock S where S.taArticle is not null and upper(S.mouvStock) = 'S' and (S.qte1Stock is not null or S.qte2Stock is not null) "
//    +"group by S.taArticle,S.mouvStock, S.dateStock,S.un1Stock ,S.un2Stock";
	
	public StockCapsulesDAO(){
//		this(null);
	}
	
//	public TaStockCapsulesDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaStock.class.getSimpleName());
//		initChampId(TaStockCapsules.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaStockCapsules());
//	}
	public void persist(TaStockCapsules transientInstance) {
		logger.debug("persisting TaStockCapsules instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaStockCapsules persistentInstance) {
		logger.debug("removing TaStockCapsules instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaStockCapsules merge(TaStockCapsules detachedInstance) {
		logger.debug("merging TaStockCapsules instance");
		try {
			TaStockCapsules result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaStockCapsules findById(int id) {
		logger.debug("getting TaStockCapsules instance with id: " + id);
		try {
			TaStockCapsules instance = entityManager.find(TaStockCapsules.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
//	@Override
	public List<TaStockCapsules> selectAll() {
		logger.debug("selectAll TaStockCapsules");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaStockCapsules> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public TaStockCapsules refresh(TaStockCapsules detachedInstance) {
		logger.debug("refresh instance");
		try {
////			entityManager.refresh(detachedInstance);
////			logger.debug("refresh successful");
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaStockCapsules.class, detachedInstance.getIdStock());
		    return detachedInstance;
		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}
	
	public List<TaEtatStockCapsules> selectSumMouvEntrees(TaEtatStockCapsules criteres,Date dateDeb,boolean strict) {
		try {																						//S.mouvStock
			String JPQLQueryMouvEntrees="select NEW fr.legrain.article.titretransport.model.TaEtatStockCapsules(a.codeTitreTransport,a.libelleTitreTransport, S.mouvStock , "+
		    " S.dateStock, sum(S.qte1Stock), '',S.libelleStock,'Stock','2' )"+
		    " from TaStockCapsules S join S.taTitreTransport a where  " +
		    "(upper(S.mouvStock) = 'E')  " ;//+
			
			if(dateDeb!=null) {
				String signe = ">";
				if(!strict) signe=">=";
				JPQLQueryMouvEntrees+=" and S.dateStock "+signe+" '"+LibDate.dateToStringSql(dateDeb)+"'";
			}
			
			//clause where
			if(criteres.getCodeTitreTransport()!=null && !criteres.getCodeTitreTransport().equals(""))
		    	JPQLQueryMouvEntrees+=" and a.codeTitreTransport like '"+criteres.getCodeTitreTransport()+"'";
		    if(criteres.getDateStockDeb()!=null)
		    	JPQLQueryMouvEntrees+=" and S.dateStock >= '"+LibDate.dateToStringSql(criteres.getDateStockDeb())+"'";
		    if(criteres.getDateStock()!=null)
		    	JPQLQueryMouvEntrees+=" and S.dateStock <= '"+LibDate.dateToStringSql(criteres.getDateStock())+"'";	       	
			
		    
		    //clause group by
		    JPQLQueryMouvEntrees+=" group by a.codeTitreTransport,a.libelleTitreTransport,S.mouvStock, S.dateStock,S.libelleStock";		

		    
		    Query ejbQuery = entityManager.createQuery(JPQLQueryMouvEntrees);
		    List<TaEtatStockCapsules> l = ejbQuery.getResultList();

			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvEntrees failed", re);
			throw re;
		}
	}
	public List<TaEtatStockCapsules> selectSumMouvSorties(TaEtatStockCapsules criteres,Date dateDeb, boolean strict) {
		try {																				//S.mouvStock
			String JPQLQueryMouvSorties="select NEW fr.legrain.article.titretransport.model.TaEtatStockCapsules(a.codeTitreTransport,a.libelleTitreTransport,S.mouvStock, "+
		    " S.dateStock, sum(-1*S.qte1Stock), '',S.libelleStock,'Stock','3' )"+
		    " from TaStockCapsules S join S.taTitreTransport a where  " +
		    "(upper(S.mouvStock) = 'S')  " ;//+

			
			if(dateDeb!=null) {
				String signe = ">";
				if(!strict) signe=">=";
				JPQLQueryMouvSorties+=" and S.dateStock "+signe+" '"+LibDate.dateToStringSql(dateDeb)+"'";
			}
		    
			//clause where
			if(criteres.getCodeTitreTransport()!=null && !criteres.getCodeTitreTransport().equals(""))
				JPQLQueryMouvSorties+=" and a.codeTitreTransport like '"+criteres.getCodeTitreTransport()+"'";
		    if(criteres.getDateStockDeb()!=null)
		    	JPQLQueryMouvSorties+=" and S.dateStock >= '"+LibDate.dateToStringSql(criteres.getDateStockDeb())+"'";
		    if(criteres.getDateStock()!=null)
		    	JPQLQueryMouvSorties+=" and S.dateStock <= '"+LibDate.dateToStringSql(criteres.getDateStock())+"'";
		       	
		    //clause group by
		    JPQLQueryMouvSorties+=" group by a.codeTitreTransport,a.libelleTitreTransport,S.mouvStock, S.dateStock,S.libelleStock";					
			

			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvSorties);
		    List<TaEtatStockCapsules> l = ejbQuery.getResultList();

			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvSorties failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaStockCapsules> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaStockCapsules> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaStockCapsules> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaStockCapsules> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaStockCapsules value) throws Exception {
		BeanValidator<TaStockCapsules> validator = new BeanValidator<TaStockCapsules>(TaStockCapsules.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaStockCapsules value, String propertyName) throws Exception {
		BeanValidator<TaStockCapsules> validator = new BeanValidator<TaStockCapsules>(TaStockCapsules.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaStockCapsules transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaStockCapsules findByCode(String code) {
		return null;
	}



}
