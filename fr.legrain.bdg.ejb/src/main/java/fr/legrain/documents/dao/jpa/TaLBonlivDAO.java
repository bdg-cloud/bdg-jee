package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.article.model.TaEtatStock;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.documents.dao.ILBonlivDAO;
import fr.legrain.lib.data.LibDate;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLBonliv.
 * @see fr.legrain.documents.dao.TaLBonliv
 * @author Hibernate Tools
 */
public class TaLBonlivDAO /*extends AbstractApplicationDAO<TaLBonliv>*/ implements ILBonlivDAO{

//	private static final Log log = LogFactory.getLog(TaLBonlivDAO.class);
	static Logger logger = Logger.getLogger(TaLBonlivDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
//	private String JPQLQueryMouvDocumentSorties=
//	 "select L.taDocument.dateDocument,L.taArticle.codeArticle,sum(-1*L.qteLDocument),"+
//	 " L.u1LDocument,sum(-1*L.qte2LDocument),L.u2LDocument  "+
//	 "from TaLBonliv L "+
//	 " where L.taDocument.idDocument not in (select rdoc.taBonliv.idDocument from TaRDocument rdoc) "+
//	 " and (L.u1LDocument='' "+
//	 " or L.u1LDocument in (select u.codeUnite from TaUnite u)) "+
//	 " and L.taArticle.codeArticle is not null  "+ 
//	 " group by  L.taDocument.dateDocument,L.taArticle.codeArticle, L.u1LDocument, L.u2LDocument";

	private String defaultJPQLQuery = "select a from TaLBonliv a";
	
	public TaLBonlivDAO(){
//		this(null);
	}
	
//	public TaLBonlivDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLBonliv.class.getSimpleName());
//		initChampId(TaLBonliv.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLBonliv());
//	}
	
//	public TaLBonliv refresh(TaLBonliv detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLBonliv.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLBonliv transientInstance) {
		logger.debug("persisting TaLBonliv instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLBonliv persistentInstance) {
		logger.debug("removing TaLBonliv instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLBonliv merge(TaLBonliv detachedInstance) {
		logger.debug("merging TaLBonliv instance");
		try {
			TaLBonliv result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLBonliv findById(int id) {
		logger.debug("getting TaLBonliv instance with id: " + id);
		try {
			TaLBonliv instance = entityManager.find(TaLBonliv.class, id);
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
	public List<TaLBonliv> selectAll() {
		logger.debug("selectAll TaLBonliv");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLBonliv> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	public List<TaEtatStock> selectSumMouvDocumentSorties(TaEtatStock criteres,Date dateDeb) {
		try {
			String JPQLQueryMouvDocument=
				 "select NEW fr.legrain.stocks.dao.TaEtatStock(a.codeArticle,'S',f.dateDocument,sum(-1*L.qteLDocument),"+
				 " L.u1LDocument,sum(-1*L.qte2LDocument),L.u2LDocument,f.codeDocument,L.libLDocument,'BonLiv' ,'8') "+
				 " from TaLBonliv L join L.taDocument f join L.taArticle a "+
				 " where f.idDocument not in " +
				 " (select rdoc.taFacture.idDocument from TaRDocument rdoc where id_bonliv is not null) ";
			 if(criteres.getExclusionQteSsUnite())
				 JPQLQueryMouvDocument+=" and  (L.u1LDocument in (select u.codeUnite from TaUnite u )or" +
			 		" L.u2LDocument in (select u.codeUnite from TaUnite u ))" ;
			 
			 JPQLQueryMouvDocument+=" and  ((L.qteLDocument is not null and L.qteLDocument >0)" +
		 		"or(L.qte2LDocument is not null and L.qte2LDocument >0)) "  ;
			 
			if(dateDeb!=null)
				JPQLQueryMouvDocument+=" and f.dateDocument > '"+LibDate.dateToStringSql(dateDeb)+"'";

			//Clause where
			if(criteres.getCodeArticle()!=null && !criteres.getCodeArticle().equals(""))
				JPQLQueryMouvDocument+=" and a.codeArticle like '"+criteres.getCodeArticle()+"'";
			if(criteres.getDateStock()!=null)
				JPQLQueryMouvDocument+=" and f.dateDocument <= '"+LibDate.dateToStringSql(criteres.getDateStock())+"'";
			if(criteres.getUn1Stock()!=null && !criteres.getUn1Stock().equals(""))
				JPQLQueryMouvDocument+=" and L.u1LDocument like '"+criteres.getUn1Stock()+"'";
			 if(criteres.getUn2Stock()!=null && !criteres.getUn2Stock().equals(""))
				 JPQLQueryMouvDocument+=" and L.u2LDocument like '"+criteres.getUn2Stock()+"'";
			//Clause group by
			 JPQLQueryMouvDocument+=" group by f.dateDocument,a.codeArticle, L.u1LDocument, L.u2LDocument,f.codeDocument,L.libLDocument";				
			
		    //clause having
		    if(criteres.getQte1Stock()!=null && !criteres.getQte1Stock().equals("")){
		    	if(!JPQLQueryMouvDocument.contains(" having "))
		    		JPQLQueryMouvDocument+=" having sum(-1*L.qteLDocument) >= "+criteres.getQte1Stock();
		    	else
		    		JPQLQueryMouvDocument+=" and sum(-1*L.qteLDocument) >= "+criteres.getQte1Stock();
		    }
		    if(criteres.getQte2Stock()!=null && !criteres.getQte2Stock().equals("")){
		    	if(!JPQLQueryMouvDocument.contains(" having "))
		    		JPQLQueryMouvDocument+=" having sum(-1*L.qte2LDocument) >= "+criteres.getQte2Stock();
		    	else
		    		JPQLQueryMouvDocument+=" and sum(-1*L.qte2LDocument) >= "+criteres.getQte2Stock();		    	
		    } 

			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvDocument);
		    List<TaEtatStock> l = ejbQuery.getResultList();
		    for (TaEtatStock taEtatStock : l) {
				if(taEtatStock.getQte1Stock()!=null && taEtatStock.getQte1Stock().signum()==0)taEtatStock.setUn1Stock(null);
				if(taEtatStock.getQte2Stock()!=null && taEtatStock.getQte2Stock().signum()==0)taEtatStock.setUn2Stock(null);
			}
			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvDocumentSorties failed", re);
			throw re;
		}
	}
	public List<TaEtatStock> selectSumMouvDocumentEntrees(TaEtatStock criteres,Date dateDeb) {
		try {
			String JPQLQueryMouvDocument=
				 "select NEW fr.legrain.stocks.dao.TaEtatStock(a.codeArticle,'E',f.dateDocument,sum(-1*L.qteLDocument),"+
				 " L.u1LDocument,sum(-1*L.qte2LDocument),L.u2LDocument,f.codeDocument,L.libLDocument,'BonLiv','5' ) "+
				 " from TaLBonliv L join L.taDocument f join L.taArticle a "+
				 " where f.idDocument not in " +
				 " (select rdoc.taFacture.idDocument from TaRDocument rdoc where id_bonliv is not null) ";
			 if(criteres.getExclusionQteSsUnite())
				 JPQLQueryMouvDocument+=" and  (L.u1LDocument in (select u.codeUnite from TaUnite u )or" +
			 		" L.u2LDocument in (select u.codeUnite from TaUnite u ))" ;
			 
			 JPQLQueryMouvDocument+=" and  ((L.qteLDocument is not null and L.qteLDocument <0)" +
		 		"or(L.qte2LDocument is not null and L.qte2LDocument <0)) "  ;
			 
			if(dateDeb!=null)
				JPQLQueryMouvDocument+=" and f.dateDocument > '"+LibDate.dateToStringSql(dateDeb)+"'";

			//Clause where
			if(criteres.getCodeArticle()!=null && !criteres.getCodeArticle().equals(""))
				JPQLQueryMouvDocument+=" and a.codeArticle like '"+criteres.getCodeArticle()+"'";
			if(criteres.getDateStock()!=null)
				JPQLQueryMouvDocument+=" and f.dateDocument <= '"+LibDate.dateToStringSql(criteres.getDateStock())+"'";
			if(criteres.getUn1Stock()!=null && !criteres.getUn1Stock().equals(""))
				JPQLQueryMouvDocument+=" and L.u1LDocument like '"+criteres.getUn1Stock()+"'";
			 if(criteres.getUn2Stock()!=null && !criteres.getUn2Stock().equals(""))
				 JPQLQueryMouvDocument+=" and L.u2LDocument like '"+criteres.getUn2Stock()+"'";
			//Clause group by
			 JPQLQueryMouvDocument+=" group by f.dateDocument,a.codeArticle, L.u1LDocument, L.u2LDocument,f.codeDocument,L.libLDocument";				
			
		    //clause having
		    if(criteres.getQte1Stock()!=null && !criteres.getQte1Stock().equals("")){
		    	if(!JPQLQueryMouvDocument.contains(" having "))
		    		JPQLQueryMouvDocument+=" having sum(-1*L.qteLDocument) >= "+criteres.getQte1Stock();
		    	else
		    		JPQLQueryMouvDocument+=" and sum(-1*L.qteLDocument) >= "+criteres.getQte1Stock();
		    }
		    if(criteres.getQte2Stock()!=null && !criteres.getQte2Stock().equals("")){
		    	if(!JPQLQueryMouvDocument.contains(" having "))
		    		JPQLQueryMouvDocument+=" having sum(-1*L.qte2LDocument) >= "+criteres.getQte2Stock();
		    	else
		    		JPQLQueryMouvDocument+=" and sum(-1*L.qte2LDocument) >= "+criteres.getQte2Stock();		    	
		    } 

			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvDocument);
		    List<TaEtatStock> l = ejbQuery.getResultList();
		    for (TaEtatStock taEtatStock : l) {
				if(taEtatStock.getQte1Stock()!=null && taEtatStock.getQte1Stock().signum()==0)taEtatStock.setUn1Stock(null);
				if(taEtatStock.getQte2Stock()!=null && taEtatStock.getQte2Stock().signum()==0)taEtatStock.setUn2Stock(null);
			}
			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvDocumentSorties failed", re);
			throw re;
		}
	}

	@Override
	public TaLBonliv findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLBonliv> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLBonliv> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLBonliv> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLBonliv> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLBonliv value) throws Exception {
		BeanValidator<TaLBonliv> validator = new BeanValidator<TaLBonliv>(TaLBonliv.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLBonliv value, String propertyName)
			throws Exception {
		BeanValidator<TaLBonliv> validator = new BeanValidator<TaLBonliv>(TaLBonliv.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLBonliv transientInstance) {
		entityManager.detach(transientInstance);
	}
	
	@Override
	public List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesSorties(TaEtatStockCapsules criteres,Date dateDeb, boolean strict) {
		try {
			String JPQLQueryMouvDocument=
				 "select NEW fr.legrain.article.titretransport.model.TaEtatStockCapsules(L.codeTitreTransport,t.libelleTitreTransport,'S',f.dateDocument,sum(-1*L.qteTitreTransport)," +
				 "f.codeDocument,L.libLDocument,'Bon de livraison','17' ) "+
				 " from TaLBonliv L join L.taDocument f , TaTitreTransport t"+
				 " where  ((L.qteTitreTransport is not null and L.qteTitreTransport >0)) ";// +

			
			if(dateDeb!=null) {
				String signe = ">";
				if(!strict) signe=">=";
				JPQLQueryMouvDocument+=" and f.dateDocument "+signe+" '"+LibDate.dateToStringSql(dateDeb)+"'";
			}
			//Clause where
			if(criteres.getCodeTitreTransport()!=null && !criteres.getCodeTitreTransport().equals(""))
				JPQLQueryMouvDocument+=" and L.codeTitreTransport like '"+criteres.getCodeTitreTransport()+"'";
		    if(criteres.getDateStockDeb()!=null)
		    	JPQLQueryMouvDocument+=" and f.dateDocument >= '"+LibDate.dateToStringSql(criteres.getDateStockDeb())+"'";
			if(criteres.getDateStock()!=null)
				JPQLQueryMouvDocument+=" and f.dateDocument <= '"+LibDate.dateToStringSql(criteres.getDateStock())+"'";
			
			JPQLQueryMouvDocument+=" and L.codeTitreTransport = t.codeTitreTransport ";

			 
			 
			//Clause group by
			JPQLQueryMouvDocument+=" group by  f.dateDocument,L.codeTitreTransport,t.libelleTitreTransport,f.codeDocument,L.libLDocument";
			 JPQLQueryMouvDocument+=" order by  f.dateDocument,L.codeTitreTransport,t.libelleTitreTransport,f.codeDocument";			


			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvDocument);
		    List<TaEtatStockCapsules> l = ejbQuery.getResultList();

			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvDocumentSorties failed", re);
			throw re;
		}
	}
	
	@Override
	public List<TaEtatStockCapsules> selectSumMouvDocumentStocksCapsulesEntree(TaEtatStockCapsules criteres,Date dateDeb, boolean strict) {
		try {
			String JPQLQueryMouvDocument=
				 "select NEW fr.legrain.article.titretransport.model.TaEtatStockCapsules(L.codeTitreTransport,t.libelleTitreTransport,'E',f.dateDocument" +
				 ",sum(-1*L.qteTitreTransport)," +
				 "f.codeDocument,L.libLDocument,'Bon de livraison','18' ) "+
				 " from TaLBonliv L join L.taDocument f , TaTitreTransport t"+
				 " where   ((L.qteTitreTransport is not null and L.qteTitreTransport <0)) ";

			
			if(dateDeb!=null) {
				String signe = ">";
				if(!strict) signe=">=";
				JPQLQueryMouvDocument+=" and f.dateDocument "+signe+" '"+LibDate.dateToStringSql(dateDeb)+"'";
			}
			//Clause where
			if(criteres.getCodeTitreTransport()!=null && !criteres.getCodeTitreTransport().equals(""))
				JPQLQueryMouvDocument+=" and L.codeTitreTransport like '"+criteres.getCodeTitreTransport()+"'";
		    if(criteres.getDateStockDeb()!=null)
		    	JPQLQueryMouvDocument+=" and f.dateDocument >= '"+LibDate.dateToStringSql(criteres.getDateStockDeb())+"'";
			if(criteres.getDateStock()!=null)
				JPQLQueryMouvDocument+=" and f.dateDocument <= '"+LibDate.dateToStringSql(criteres.getDateStock())+"'";
			
			JPQLQueryMouvDocument+=" and L.codeTitreTransport = t.codeTitreTransport";

			 
			 
			//Clause group by
			 JPQLQueryMouvDocument+=" group by  f.dateDocument,L.codeTitreTransport,t.libelleTitreTransport,f.codeDocument,L.libLDocument";			
			 JPQLQueryMouvDocument+=" order by  f.dateDocument,L.codeTitreTransport,t.libelleTitreTransport,f.codeDocument";			



			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvDocument);
		    List<TaEtatStockCapsules> l = ejbQuery.getResultList();

			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvDocumentSorties failed", re);
			throw re;
		}
	}


}
