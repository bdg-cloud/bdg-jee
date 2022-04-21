package fr.legrain.article.dao.jpa;

// Generated Aug 11, 2008 4:05:28 PM by Hibernate Tools 3.2.1.GA

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.legrain.article.dao.IMouvementDAO;
import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.dms.model.TaEtatMouvementDms;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.LibDate;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaMouvementStock;
import fr.legrain.validator.BeanValidator;
//import org.hibernate.validator.ClassValidator;
//import org.hibernate.validator.InvalidValue;

/**
 * Home object for domain model class TaMouvement.
 * @see fr.legrain.TaMouvement.dao.TaMouvement
 * @author Hibernate Tools
 */
public class MouvementStockDAO implements IMouvementDAO {

	private static final Log logger = LogFactory.getLog(MouvementStockDAO.class);
	//static Logger logger = Logger.getLogger(TaMouvementDAO.class.getName());

	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;

	
	//@PersistenceContext
	//private EntityManager entityManager = entityManager;

	//	protected ClassValidator<TaMouvement> uniteValidator = new ClassValidator<TaMouvement>( TaMouvement.class );

	private String defaultJPQLQuery = "select u from TaMouvementStock u";
	//private String defaultJPQLQuery = "select u from TaMouvement u where u.taArticle is null";

	public MouvementStockDAO(){
		//		this(null);
	}

	//	public TaMouvementDAO(EntityManager em){
	//		if(em!=null) {
	//			setEm(em);
	//		}
	////		champIdTable=ctrlGeneraux.getID_TABLE(TaMouvement.class.getSimpleName());
	//		initChampId(TaMouvement.class);
	//		setJPQLQuery(defaultJPQLQuery);
	//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
	//		initNomTable(new TaMouvement());
	//	}

	//	public IStatus validate(TaMouvement entity){
	//		ClassValidator<TaMouvement> uniteValidator = new ClassValidator<TaMouvement>( TaMouvement.class );
	//		InvalidValue[] iv = uniteValidator.getInvalidValues(entity);
	//		IStatus s = null;
	//		if(iv.length>0) {
	//			s = new Status(0,"",iv[0].getMessage());
	//		}
	//		return s;
	//	}


	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#persist(fr.legrain.articles.dao.TaMouvement)
	 */
	public void persist(TaMouvementStock transientInstance) {
		logger.debug("persisting TaMouvement instance");
		try {

			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#remove(fr.legrain.articles.dao.TaMouvement)
	 */
	public void remove(TaMouvementStock persistentInstance) {
		logger.debug("removing TaMouvementStock instance");
		try {
			TaMouvementStock e = entityManager.merge(persistentInstance);
			entityManager.remove(e);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#merge(fr.legrain.articles.dao.TaMouvement)
	 */
	public TaMouvementStock merge(TaMouvementStock detachedInstance) {
		logger.debug("merging TaMouvementStock instance");
		try {
			TaMouvementStock result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}



	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#findById(int)
	 */
	public TaMouvementStock findById(int id) {
		logger.debug("getting TaMouvementStock instance with id: " + id);
		try {
			TaMouvementStock instance = entityManager.find(TaMouvementStock.class, id);
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
	public List<TaMouvementStock> selectAll() {
		logger.debug("selectAll TaMouvementStock");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			//			System.out.println(defaultJPQLQuery);
			//			System.out.println(ejbQuery.getResultList().toString());
			//			System.out.println(ejbQuery.toString());
			List<TaMouvementStock> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	public TaMouvementStock refresh(TaMouvementStock detachedInstance) {
		logger.debug("refresh instance");
		try {
			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
			session.evict(detachedInstance);
			detachedInstance = entityManager.find(TaMouvementStock.class, detachedInstance.getIdMouvementStock());
			return detachedInstance;

		} catch (RuntimeException re) {
			logger.error("refresh failed", re);
			throw re;
		}
	}


	/*
	public List<TaMouvement> findByArticle(String codeArticle) {
		logger.debug("getting TaMouvement instance with codeArticle: " + codeArticle);
		try {
			Query query = entityManager.createNamedQuery(TaMouvement.QN.FIND_BY_ARTICLE);
			query.setParameter(1, codeArticle);
			List<TaMouvement> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}*/

	//ejb
	//	public ModelGeneralObjet<SWTUnite,TaMouvementDAO,TaMouvement> modelObjetUniteArticle(String codeArticle) {
	//		return new ModelGeneralObjet<SWTUnite,TaMouvementDAO,TaMouvement>(findByArticle(codeArticle),SWTUnite.class,entityManager);
	//	}

	public void ctrlSaisieSpecifique(TaMouvementStock entity,String field) throws ExceptLgr {	
		try {
			//			if(field.equals(Const.C_CODE_TVA)){
			//				if(!entity.getCodeTva().equals("")){
			//				}					
			//			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	//	public void ctrlSaisieSpecifique(ValeurChamps valeurChamps) throws ExceptLgr {	
	//		try{
	//			switch (this.getModeObjet().getMode()) {
	//            case C_MO_CONSULTATION: break;
	//            
	//            case C_MO_IMPORTATION:            				
	//            case C_MO_INSERTION:
	//            	TCtrlGeneraux.ctrlSaisie(ex);
	//				break;            	
	//            case C_MO_EDITION:	
	//            	TCtrlGeneraux.ctrlSaisie(ex);
	//				break;
	//            case C_MO_SUPPRESSION: 
	//            	TCtrlGeneraux.ctrlSaisie(ex);
	//				break;
	//			default:				
	//				break;
	//			}
	//		}catch (Exception e){
	//			logger.error("Erreur : ctrlSaisieSpecifique", e);
	//			throw new ExceptLgr(e.getMessage());
	//		}
	//	}

	@Override
	public List<TaMouvementStock> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaMouvementStock> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaMouvementStock> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaMouvementStock> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaMouvementStock value) throws Exception {
		BeanValidator<TaMouvementStock> validator = new BeanValidator<TaMouvementStock>(TaMouvementStock.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaMouvementStock value, String propertyName) throws Exception {
		BeanValidator<TaMouvementStock> validator = new BeanValidator<TaMouvementStock>(TaMouvementStock.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaMouvementStock transientInstance) {
		entityManager.detach(transientInstance);
	}



	@Override
	public List<Object[]> getEtatStock() {

		try {

			Query query = entityManager.createQuery("select l.taArticle.idArticle, l.idLot, a.entrepotOrg.idEntrepot, a.emplacementOrg, "
					+ "a.entrepotDest.idEntrepot, a.emplacementDest, a.qte1Stock  from TaMouvementStock a,TaLot l where l.numLot=a.numLot ");
			List<Object[]> l = query.getResultList();
			logger.debug("get successful");
			return l;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}


	}
	
	public List<String>  articleEnStock() {

		try {
			Query query = entityManager.createQuery(
					"select distinct l.taArticle.codeArticle, l.taArticle.libellecArticle "
					+ "from TaEtatStock m,TaLot l "
					+ " where l.numLot=m.numLot"
					+ " group by l.taArticle.codeArticle, l.taArticle.libellecArticle"
					+ " having sum(m.qteRef) > 0 ");
					//+ " having sum(m.qte1EtatStock) > 0 ");

//			Query query = entityManager.createQuery(
//					"select distinct m.taLot.taArticle.codeArticle, m.taLot.taArticle.libellecArticle,m.qte1EtatStock "
//					+ "from TaEtatStock m group by m.taLot.taArticle.codeArticle, m.taLot.taArticle.libellecArticle,m.qte1EtatStock having sum(m.qte1EtatStock) > 0");
////					"select distinct m.taLot.taArticle.codeArticle, m.taLot.taArticle.libellecArticle "
////					+ "from TaMouvementStock m  "
////					+ "where  ("
////					+ "select sum(m1.qte1Stock) from TaMouvementStock m1 where m1.taLot.taArticle.codeArticle = m.taLot.taArticle.codeArticle"
////					+ ") > 0");
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
	
	public List<String>  articleEnStock(Boolean mPremiere,Boolean pFini) {

		try {
			String conditionMPetPF = "";
			if(mPremiere!=null || pFini!=null) {
				conditionMPetPF+=" and ";
			}
			if(mPremiere!=null) {
				conditionMPetPF+=" l.taArticle.matierePremiere="+mPremiere;
			}
			if(mPremiere!=null && pFini!=null) {
				conditionMPetPF+=" and ";
			}
			if(pFini!=null) {
				conditionMPetPF+=" l.taArticle.produitFini="+pFini;
			}
			Query query = entityManager.createQuery(
					"select distinct l.taArticle.codeArticle, l.taArticle.libellecArticle "
					+ "from TaEtatStock m,TaLot l "
					+ " where l.numLot=m.numLot"+conditionMPetPF
					+ " group by l.taArticle.codeArticle, l.taArticle.libellecArticle"
					+ " having sum(m.qteRef) > 0 ");
					//+ " having sum(m.qte1EtatStock) > 0 ");

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
	
	public void recalculEtatStock() {
		StoredProcedureQuery query = this.entityManager.createStoredProcedureQuery("recalcul_etat_stock");
//		query.registerStoredProcedureParameter("x", Double.class, ParameterMode.PARAM_IN);
//		query.registerStoredProcedureParameter("y", Double.class, ParameterMode.PARAM_IN);
//		query.registerStoredProcedureParameter("sum", Double.class, ParameterMode.PARAM_OUT);
//		
//		// set input parameter
//		query.setParameter("x", 1.23d);
//		query.setParameter("y", 4.56d);
//
//		// call the stored procedure and get the result
		query.execute();
//		Double sum = (Double) query.getOutputParameterValue("sum");
	}
	
	public List<ArticleLotEntrepotDTO>  getQuantiteTotaleEnStockArticle(String codeArticle, Boolean utiliserLotNonConforme) {
		try {

			//TODO a supprimer, en attendant d'être sur que les différents trigger de recalcul de la table ta_etat_stock lors de la l'ajout/suppression/modification de mouvement fonctionne de façon sure.
			recalculEtatStock();
			
			Query query;
			 query = entityManager.createQuery("select a from TaArticle a where a.codeArticle like '"+codeArticle+"' ");
			 TaArticle article=(TaArticle) query.getSingleResult();
			 
			 	String jpql = "select distinct "
						+ "m.unRef, sum(m.qteRef) "
						+ " from TaEtatStock m "
						+ " where  m.idArticle=:idArticle "
						+ " and exists (select l.idLot from TaLot l where m.idArticle = l.taArticle.idArticle and l.numLot = m.numLot and l.termine <> true";
			 	if(utiliserLotNonConforme!=null) {
			 		jpql += " and l.lotConforme = :utiliserLotNonConforme ";
			 	}
			 	jpql += " ) ";
			 	jpql += " group by "
					+ "m.unRef "
						;
			 	
				 query = entityManager.createQuery(jpql);
				 query.setParameter("idArticle", article.getIdArticle());
				 if(utiliserLotNonConforme!=null) {
					 query.setParameter("utiliserLotNonConforme", !utiliserLotNonConforme);
				 }
			List<Object[]> temp =query.getResultList();
			logger.debug("get successful");
			List<ArticleLotEntrepotDTO> listDTO = new ArrayList<ArticleLotEntrepotDTO>();
			ArticleLotEntrepotDTO dto = null;
			int i=1;
			for(Object[] s : temp){
				dto = new ArticleLotEntrepotDTO();
//				dto.setIdEntrepot((Integer)s[0]);
//				dto.setCodeEntrepot((String)s[1]);
//				dto.setLibelleEntrepot((String)s[2]);
//				dto.setNumLot((String)s[3]);
//				dto.setEmplacement((String)s[4]);
//				dto.setIdUnite(null);
				dto.setCodeUnite((String)s[0]);
//				dto.setLibelleUnite(null);
				dto.setQte1((BigDecimal)s[1]);
//				dto.setLibelleLot((String)s[7]);
//				dto.setDateLot(LibDate.dateToString(new Date()));
//				dto.setDluo(LibDate.dateToString(new Date()));
				dto.setIdALE(i);
				i++;
				listDTO.add(dto);
			}
			return listDTO;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	@Override
	public List<ArticleLotEntrepotDTO>  getEtatStockByArticleVirtuel(String codeArticle, Boolean utiliserLotNonConforme) {
		try {

			//TODO a supprimer, en attendant d'être sur que les différents trigger de recalcul de la table ta_etat_stock lors de la l'ajout/suppression/modification de mouvement fonctionne de façon sure.
			recalculEtatStock();
			
			Query query;
			 query = entityManager.createQuery("select a from TaArticle a where a.codeArticle like '"+codeArticle+"' ");
			 TaArticle article=(TaArticle) query.getSingleResult();
			 
			 	String jpql = "select distinct "
						+ "e.idEntrepot, e.codeEntrepot , e.libelle, "
						+ "m.numLot, m.emplacement, "
						+ "m.unRef, m.qteRef, "
						+ " m.libelleEtatStock "
						+ " from TaEtatStock m left join m.taEntrepot e ,TaLot l "
						+ " where  m.idArticle=:idArticle and l.taArticle.idArticle=m.idArticle and l.virtuel=true and l.virtuelUnique=false and l.termine <> true "
						+ " and l.numLot = m.numLot" //ajout pour bug 2201 - nicolas
						;
			 	if(utiliserLotNonConforme!=null && utiliserLotNonConforme) {
//			 		jpql += " and l.lotConforme = :utiliserLotNonConforme ";
			 	} else if (article.getGestionLot()){
			 		jpql += " and l.lotConforme = true ";
			 	}
			 	jpql += " group by "

							+ "e.idEntrepot, e.codeEntrepot , e.libelle, "
							+ "m.numLot, m.emplacement, "
							+ "m.unRef, m.qteRef, "
							+ " m.libelleEtatStock "
							//+ " having sum(m.qte1EtatStock) > 0" //ne pas afficher les lots qui n'ont plus de stock
							+ " order by m.numLot ";
				 query = entityManager.createQuery(jpql);
				 query.setParameter("idArticle", article.getIdArticle());
//				 if(utiliserLotNonConforme!=null) {
//					 query.setParameter("utiliserLotNonConforme", !utiliserLotNonConforme);
//				 }
			List<Object[]> temp =query.getResultList();
			logger.debug("get successful");
			List<ArticleLotEntrepotDTO> listDTO = new ArrayList<ArticleLotEntrepotDTO>();
			ArticleLotEntrepotDTO dto = null;
			int i=1;
			for(Object[] s : temp){
				dto = new ArticleLotEntrepotDTO();
				dto.setIdEntrepot((Integer)s[0]);
				dto.setCodeEntrepot((String)s[1]);
				dto.setLibelleEntrepot((String)s[2]);
				dto.setNumLot((String)s[3]);
				dto.setEmplacement((String)s[4]);
				dto.setIdUnite(null);
				dto.setCodeUnite((String)s[5]);
				dto.setLibelleUnite(null);
				dto.setQte1((BigDecimal)s[6]);
				dto.setLibelleLot((String)s[7]);
//				dto.setDateLot(LibDate.dateToString(new Date()));
//				dto.setDluo(LibDate.dateToString(new Date()));
				dto.setIdALE(i);
				i++;
				listDTO.add(dto);
			}
			return listDTO;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	@Override
	public Integer  getNBEtatStockByArticleVirtuel(TaArticle article, Boolean utiliserLotNonConforme) {
		try {
			if(article !=null) {
			//TODO a supprimer, en attendant d'être sur que les différents trigger de recalcul de la table ta_etat_stock lors de la l'ajout/suppression/modification de mouvement fonctionne de façon sure.
			recalculEtatStock();
			
			Query query;
//			 query = entityManager.createQuery("select a from TaArticle a where a.codeArticle like '"+codeArticle+"' ");
//			 TaArticle article=(TaArticle) query.getSingleResult();
			 
			 	String jpql = "select cast(count(m) as integer)  "
						+ " from TaEtatStock m left join m.taEntrepot e ,TaLot l "
						+ " where  m.idArticle=:idArticle and l.taArticle.idArticle=m.idArticle and l.virtuel=true and l.virtuelUnique=false  and l.termine <> true "
						+ " and l.numLot = m.numLot" //ajout pour bug 2201 - nicolas
						;
			 	if(utiliserLotNonConforme!=null && utiliserLotNonConforme && article.getGestionLot()) {
			 		jpql += " and l.lotConforme = :utiliserLotNonConforme ";
			 	} else {
			 		//jpql += " and l.lotConforme = true ";
			 	}
			 	jpql += " group by "

							+ "e.idEntrepot, e.codeEntrepot , e.libelle, "
							+ "m.numLot, m.emplacement, "
							+ "m.unRef, m.qteRef, "
							+ " m.libelleEtatStock "
							+ " order by m.numLot ";
				 query = entityManager.createQuery(jpql);
				 query.setParameter("idArticle", article.getIdArticle());
			try {
				List<Integer> temp =query.getResultList();
				Integer nb=0;
				for (Integer i : temp) {
					nb=nb+i;
				} 				
				logger.debug("get successful");
				return nb;
			} catch (Exception e) {
				return 0;
			} 	 

			}
			return 0;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	public List<ArticleLotEntrepotDTO>  getEtatStockByArticle(String codeArticle, Boolean utiliserLotNonConforme) {
		try {

			//TODO a supprimer, en attendant d'être sur que les différents trigger de recalcul de la table ta_etat_stock lors de la l'ajout/suppression/modification de mouvement fonctionne de façon sure.
			recalculEtatStock();
			
			Query query;

			String jpql = "select distinct "
					+ "e.idEntrepot, e.codeEntrepot , e.libelle, "
					+ "l.idLot, l.numLot, m.emplacement, "
//					+ "m.un1EtatStock, m.qte1EtatStock, "
					+ "m.unRef, sum(m.qteRef), "
					+ "l.taArticle.codeArticle, l.taArticle.libellecArticle, l.libelle, "
					+ "l.dateLot,l.dluo "
					+ " from TaEtatStock m left join m.taEntrepot e,TaLot l "
					+ " where l.numLot=m.numLot and l.taArticle.codeArticle= :codeArticle and l.termine <> true "
					+ " and l.numLot = m.numLot " //ajout pour bug 2201 - nicolas
					;
			if(utiliserLotNonConforme!=null && utiliserLotNonConforme) {
		 		//jpql += " and l.lotConforme = :utiliserLotNonConforme ";
		 	} else {
		 		jpql += " and l.lotConforme = true ";
		 	}
			jpql += " group by "
					//+ " m.taLot.taArticle.codeArticle, m.taLot.taArticle.libellecArticle,m.qte1EtatStock "
					+ "e.idEntrepot, e.codeEntrepot , e.libelle, "
					+ "l.idLot, l.numLot, m.emplacement, "
//					+ "m.un1EtatStock, m.qte1EtatStock, "
					+ "m.unRef, "
					+ "l.taArticle.codeArticle, l.taArticle.libellecArticle, l.libelle "
					//+ " having sum(m.qte1EtatStock) > 0" //ne pas afficher les lots qui n'ont plus de stock
					+ " order by l.dluo,l.numLot, l.dateLot";
			 query = entityManager.createQuery(jpql);
			 query.setParameter("codeArticle", codeArticle);
//			 if(utiliserLotNonConforme!=null && utiliserLotNonConforme) {
//				 query.setParameter("utiliserLotNonConforme", false);
//			 }
			 
			List<Object[]> temp =query.getResultList();
			logger.debug("get successful");
			List<ArticleLotEntrepotDTO> listDTO = new ArrayList<ArticleLotEntrepotDTO>();
			ArticleLotEntrepotDTO dto = null;
			int i=1;
			for(Object[] s : temp){
				dto = new ArticleLotEntrepotDTO();
				dto.setIdEntrepot((Integer)s[0]);
				dto.setCodeEntrepot((String)s[1]);
				dto.setLibelleEntrepot((String)s[2]);
				dto.setIdLot((Integer)s[3]);
				dto.setNumLot((String)s[4]);
				dto.setEmplacement((String)s[5]);
				dto.setIdUnite(null);
				dto.setCodeUnite((String)s[6]);
				dto.setLibelleUnite(null);
				dto.setQte1((BigDecimal)s[7]);
				dto.setLibelleLot((String)s[10]);
				dto.setDateLot(LibDate.dateToString((Date)s[11]));
				dto.setDluo(LibDate.dateToString((Date)s[12]));
				//dto.setDluo(null);
				dto.setIdALE(i);
				i++;
				listDTO.add(dto);
			}
			return listDTO;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<String>  entrepotAvecStock() {

		try {

			Query query = entityManager.createQuery("select distinct m.entrepotDest.codeEntrepot, m.entrepotDest.libelle from TaMouvementStock m ,TaLot l  "
					+ " where l.numLot=m.numLot and  l.termine <> 'true'");
			List<Object[]> temp =query.getResultList();
			logger.debug("get successful");
			List<String> temp2 = new ArrayList<String>();
			for(Object[] s : temp){
				if (s[0] != null && s[1] != null){
					temp2.add(s[0].toString() + "~" + s[1].toString() );}
			}
			return temp2;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<String>  lotEnStock() {

		try {

			Query query = entityManager.createQuery("select distinct l.numLot from TaMouvementStock m ,TaLot l  where l.numLot=m.numLot and "
					+ "  l.termine <> 'true'");
			List<Object> temp =query.getResultList();
			logger.debug("get successful");
			List <String> list = new ArrayList<String>();
			for (Object o : temp){
				list.add(o.toString());
			}
			return list;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<String>  lotEnStockParArticle(String codeArticle) {

		try {

			Query query = entityManager.createQuery("select distinct l.numLot from TaMouvementStock m ,TaLot l  where l.numLot=m.numLot and l.termine <> 'true' "
					+ " and l.taArticle.codeArticle = '" +codeArticle +"'");
			List<Object> temp =query.getResultList();
			logger.debug("get successful");
			List <String> list = new ArrayList<String>();
			for (Object o : temp){
				list.add(o.toString());
			}
			return list;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<String>  entrepotAvecStockParArticleEtLot(String codeArticle, String numLot) {

		try {

			Query query = entityManager.createQuery("select distinct m.entrepotDest.codeEntrepot, m.entrepotDest.libelle from TaMouvementStock m ,TaLot l  "
					+ " where l.numLot=m.numLot and l.termine <> 'true' and l.numLot = '" + numLot +"' and l.taArticle.codeArticle = '" +codeArticle +"'");
			List<Object[]> temp =query.getResultList();
			logger.debug("get successful");
			List<String> temp2 = new ArrayList<String>();
			for(Object[] s : temp){
				if (s[0] != null && s[1] != null){
					temp2.add(s[0].toString() + "~" + s[1].toString() );}
			}
			return temp2;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<String>  emplacementParArticleLotEtEntrepot(String codeArticle, String numLot, String codeEntrepot) {

		try {

			Query query = entityManager.createQuery("select distinct m.emplacementDest from TaMouvementStock m ,TaLot l   where l.numLot=m.numLot and "
					+ " m.entrepotDest.codeEntrepot = '"+ 
					codeEntrepot +"' and l.numLot = '" + 
					numLot +"' and l.taArticle.codeArticle = '" +
					codeArticle +"'");
			List<Object> temp =query.getResultList();
			logger.debug("get successful");
			List <String> list = new ArrayList<String>();
			for (Object o : temp){
				list.add(o.toString());
			}
			return list;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<String>  emplacementParEntrepot( String codeEntrepot) {

		try {

			Query query = entityManager.createQuery("select distinct m.emplacement from TaMouvementStock m  where m.taEntrepot.codeEntrepot = '"+ 
					codeEntrepot +"'");
			List<Object> temp =query.getResultList();
			logger.debug("get successful");
			List <String> list = new ArrayList<String>();
			for (Object o : temp){
				if(o!=null) {
					list.add(o.toString());
				}
			}
			return list;


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public TaMouvementStock findByCode(String codeLot) {
		logger.debug("getting TaMouvementStock instance with code: " + codeLot);
		try {
			if(!codeLot.equals("")){
			Query query = entityManager.createQuery("select a from TaMouvementStock a, TaLot l where l.numLot = a.numLot and l.numLot = '"+codeLot+"'");
			TaMouvementStock instance = (TaMouvementStock)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* - Dima - Début - */
	
	@Override
	public List<TaMouvementStock> findByCodeListe(String codeLot) {
		logger.debug("getting Liste<TaMouvementStock> instance with code: " + codeLot);
		try {
			if(!codeLot.equals("")){
			Query query = entityManager.createQuery(TaMouvementStock.QN.FIND_BY_CODE_MOUVEMENT_NO_DATE);
			//TaMouvementStock instance = (TaMouvementStock)query.getSingleResult();
			query.setParameter("idMouvementStock", codeLot);
			List<TaMouvementStock> l = query.getResultList();;
			logger.debug("get successful");
			return l;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	public List <MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF){ // Etat Stock Global
		return etatStocksEntrepotEmplacement(dateD, dateF, null, null,false,false);
	}
//	public List<MouvementStocksDTO> findAllLight() {
//		try {
//			Query query = entityManager.createNamedQuery(TaMouvementStock.QN.FIND_ALL_LIGHT);
//			List<MouvementStocksDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//	
//	public List<MouvementStocksDTO> findByCodeLight(String code) {
//		logger.debug("getting MouvementStocksDTO instance");
//		try {
//			Query query = null;
//			if(code!=null && !code.equals("") && !code.equals("*")) {
//				query = entityManager.createNamedQuery(TaMouvementStock.QN.FIND_BY_CODE_LIGHT);
//				query.setParameter(1, "%"+code+"%");
//			} else {
//				query = entityManager.createNamedQuery(TaMouvementStock.QN.FIND_ALL_LIGHT);
//			}
//
//			List<MouvementStocksDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	/* - Dima -  Fin  - */

	@Override
	public List<MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF, String codeEntrepot,
			String emplacement,boolean parFamille,boolean afficherLesTermine) {
		List<MouvementStocksDTO> listStock = new ArrayList<MouvementStocksDTO>() ;
		List<Object[]> listStockTemp ;
		Integer rang;
		String where ="";
		if(codeEntrepot!=null && !codeEntrepot.equals(Const.C_CHOISIR)){
			where=" where lower(e.code_entrepot) like '"+codeEntrepot.toLowerCase()+"' ";
		}
		if(emplacement!=null && !emplacement.equals(Const.C_CHOISIR)){
			if(where.isEmpty())	where=" where lower(s.emplacement) like '"+emplacement.toLowerCase()+"' ";
			else where+=" and lower(s.emplacement) like '"+emplacement.toLowerCase()+"' ";
		}	
		if(afficherLesTermine==false) {
			if(where.isEmpty())	where=" where l.termine = false ";
			else where+=" and  l.termine = false ";
		}
//		if(afficherLesTermine==false) {
			if(where.isEmpty())	where=" where a.gestion_stock = true ";
			else where+=" and  a.gestion_stock = true ";
//		}		
		logger.debug("getting EditionEtatTracabilite instance with Dates pour les Etats des Stocks Global : ");
		try {
			String qString = 	" select  case when e.code_entrepot is not null then e.code_entrepot else ''  end ent, a.code_article, l.numlot, a.libellec_article, " +
					"(select sum(COALESCE(s1.qte_ref,0)) from ta_mouvement_stock s1 where s1.date_Stock < '"+dateD+"' and s1.id_lot =s.id_lot and ((s1.id_entrepot is null and s.id_entrepot is null) or (s.id_entrepot=s1.id_entrepot))) as depart, "+
					"(select sum(COALESCE(s1.qte_ref,0)) from ta_mouvement_stock s1 where s1.qte_ref>0 and s1.date_Stock between '"+dateD+"' and '"+dateF+"' and s1.id_lot =s.id_lot and ((s1.id_entrepot is null and s.id_entrepot is null) or (s.id_entrepot=s1.id_entrepot))) as entree, "+
					"(select sum(COALESCE(s1.qte_ref,0)) from ta_mouvement_stock s1 where s1.qte_ref<=0 and s1.date_Stock between '"+dateD+"' and '"+dateF+"' and s1.id_lot =s.id_lot and ((s1.id_entrepot is null and s.id_entrepot is null) or (s.id_entrepot=s1.id_entrepot))) as sortie "+
					" ,f.code_famille,case when (s.emplacement is not null and trim(s.emplacement) <>'') then s.emplacement else null  end emp   ,COALESCE(s.unite_ref,''),l.termine"+
				" from "+ 
					"ta_mouvement_stock s "
					+ " left join ta_lot l on s.id_lot = l.id_lot "
					+ " join ta_article a on l.id_article = a.id_article "
					+ " left join ta_famille f on a.id_famille = f.id_famille "
					+ " left join ta_entrepot e on  e.id_entrepot = s.id_entrepot ";
			qString +=where;
			qString +="  group by f.code_famille,a.code_article,l.id_lot,numlot,s.id_lot,ent,s.id_entrepot,emp,a.libellec_article,COALESCE(s.unite_ref,'')  ";
			qString +=" order by f.code_famille,a.code_article,numlot,ent,emp  ";
							
		Query query = entityManager.createNativeQuery(qString); 
		
	List<EditionEtatTracabilite> r = new ArrayList<EditionEtatTracabilite>();
	rang=1;
		listStockTemp = query.getResultList();
		for (Object[] obj : listStockTemp) {
			MouvementStocksDTO mouv=new MouvementStocksDTO();
			
			if (obj[0] != null)mouv.setCodeEntrepot((String) obj[0]);else mouv.setCodeEntrepot("");
			if (obj[1] != null)mouv.setCodeArticle((String) obj[1]);else mouv.setCodeArticle("");
			if (obj[2] != null)mouv.setNumLot((String) obj[2]);else mouv.setNumLot("");
			if (obj[3] != null)mouv.setLibelleStock((String) obj[3]);else mouv.setLibelleStock("");
			if (obj[4] != null)mouv.setDepart(((BigDecimal) obj[4]));else mouv.setDepart(BigDecimal.ZERO);
			if (obj[5] != null)mouv.setEntree(((BigDecimal) obj[5]));else mouv.setEntree(BigDecimal.ZERO);
			if (obj[6] != null)mouv.setSortie(((BigDecimal) obj[6]));else mouv.setSortie(BigDecimal.ZERO);
			if (obj[7] != null)mouv.setCodeFamille((String) obj[7]);else mouv.setCodeFamille("");
			if (obj[8] != null)mouv.setEmplacement((String) obj[8]);else mouv.setEmplacement("");
			if (obj[9] != null)mouv.setUnRef((String) obj[9]);else mouv.setUnRef("");
			if (obj[10] != null)mouv.setTermine((Boolean) obj[10]);else mouv.setTermine(false);
			mouv.setDispo(mouv.getDepart().add(mouv.getEntree().add(mouv.getSortie())));
			mouv.setId(rang);
			listStock.add(mouv);
			rang++;
		}
		
		
		logger.debug("get successful");
		return listStock;
		
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return null;
//			throw re;
		}
	}

	
	@Override
	public List<TaEtatMouvementDms> selectSumSyntheseMouvEntreesSortiesDms(TaEtatMouvementDms criteres) {
		try {
			int mouvementSens=-1;
			String mouvementSensLabel="S";
			if(criteres.getMouvementEntree()) {
				mouvementSens=1;
				mouvementSensLabel="E";
			}
			String JPQLQueryMouvDocument=  
				 "select NEW fr.legrain.dms.model.TaEtatMouvementDms(fa.codeFamille,a.codeArticle,'"+mouvementSensLabel+"',  extract(month from F.dateGrMouvStock) ," +
				 " extract(year from F.dateGrMouvStock),sum(L.qte1Stock), L.un1Stock,sum(L.qte2Stock),L.un2Stock ) "+ 
				 " from TaMouvementStock L  join L.taGrMouvStock F  join f.taTypeMouvStock tm   join L.taLot lot join lot.taArticle a  left join a.taFamille fa " +
				 " where a.codeArticle is not null and tm.dms=true and systeme=false " ;//cette ligne est inutile fu point de vue sql, mais elle permet de commencer la condition de façon sûr   
				 if(criteres.getExclusionQteSsUnite()){
					 JPQLQueryMouvDocument+=" and  (L.un1Stock in (select u.codeUnite from TaUnite u )or" +
				 		" L.un2Stock in (select u.codeUnite from TaUnite u ))" ;
				 }
				 if(criteres.getMouvementEntree()) //mouvement d'entrée ou de sortie
					 JPQLQueryMouvDocument+=" and  L.qte1Stock>=0" ;
				 else JPQLQueryMouvDocument+=" and  L.qte1Stock<0" ;
				 
			if(criteres.getMois()!=null)
				JPQLQueryMouvDocument+=" and extract(month from F.dateGrMouvStock) = '"+criteres.getMois()+"'";
			if(criteres.getAnnee()!=null)
				JPQLQueryMouvDocument+=" and extract(year from F.dateGrMouvStock) = '"+criteres.getAnnee()+"'";
			if(criteres.getDateDeb()!=null)
				JPQLQueryMouvDocument+=" and F.dateGrMouvStock >=  '"+LibDate.dateToStringSql(criteres.getDateDeb())+"'";
			if(criteres.getDateFin()!=null)
				JPQLQueryMouvDocument+=" and F.dateGrMouvStock <=  '"+LibDate.dateToStringSql(criteres.getDateFin())+"'";
//			//Clause where
			if(criteres.getCodeArticle()!=null && !criteres.getCodeArticle().equals(""))
				JPQLQueryMouvDocument+=" and  a.codeArticle like '"+criteres.getCodeArticle()+"'";
			if(criteres.getCodeFamille()!=null && !criteres.getCodeFamille().equals(""))
				JPQLQueryMouvDocument+=" and fa.codeFamille like '"+criteres.getCodeFamille()+"'";
			if(criteres.getUn1()!=null && !criteres.getUn1().equals(""))
				JPQLQueryMouvDocument+=" and L.un1Stock like '"+criteres.getUn1()+"'";
			 if(criteres.getUn2()!=null && !criteres.getUn2().equals(""))
				 JPQLQueryMouvDocument+=" and L.un2Stock like '"+criteres.getUn2()+"'";
			//Clause group by
			 JPQLQueryMouvDocument+=" group by fa.codeFamille,a.codeArticle, L.un1Stock, L.un2Stock,F.dateGrMouvStock";	
																	

		    //clause having
		    if(criteres.getQte1()!=null && !criteres.getQte1().equals(BigDecimal.valueOf(0))){
		    	if(!JPQLQueryMouvDocument.contains(" having "))
		    		JPQLQueryMouvDocument+=" having sum(L.qte1Stock) >= "+criteres.getQte1();
		    	else
		    		JPQLQueryMouvDocument+=" and sum(L.qte1Stock) >= "+criteres.getQte1();
		    }
		    if(criteres.getQte2()!=null && !criteres.getQte2().equals(BigDecimal.valueOf(0))){
		    	if(!JPQLQueryMouvDocument.contains(" having "))
		    		JPQLQueryMouvDocument+=" having sum(L.qte2Stock) >= "+criteres.getQte2();
		    	else
		    		JPQLQueryMouvDocument+=" and sum(L.qte2Stock) >= "+criteres.getQte2();		    	
		    }
		    
			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvDocument);
		    List<TaEtatMouvementDms> l = ejbQuery.getResultList();
			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumSyntheseMouvEntreesSortiesDms failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEtatMouvementDms> selectSumMouvementEntreesSortiesDms(TaEtatMouvementDms criteres) {
		try {
			int mouvementSens=-1;
			String mouvementSensLabel="S";
			if(criteres.getMouvementEntree()) {
				mouvementSens=1;
				mouvementSensLabel="E";
			}
			String JPQLQueryMouvDocument=  				
				 "select NEW fr.legrain.dms.model.TaEtatMouvementDms('NC','NC' ,F.codeGrMouvStock,F.dateGrMouvStock,a.codeArticle," +
				 "fa.codeFamille,'"+mouvementSensLabel+"', extract(month from F.dateGrMouvStock) ," +
				 " extract(year from F.dateGrMouvStock) ,sum(L.qte1Stock),"+
				 " L.un1Stock,sum(L.qte2Stock),L.un2Stock ) "+
				 " from TaMouvementStock L  join L.taGrMouvStock F  join f.taTypeMouvStock tm  join L.taLot lot join lot.taArticle a  left join a.taFamille fa " +
				 " where a.codeArticle is not null and tm.dms=true and systeme=false" ;
				 if(criteres.getExclusionQteSsUnite())
					 JPQLQueryMouvDocument+=" and  (L.un1Stock in (select u.codeUnite from TaUnite u )or" +
				 		" L.un2Stock in (select u.codeUnite from TaUnite u ))" ;
				 if(criteres.getMouvementEntree()) //mouvement d'entrée ou de sortie
					 JPQLQueryMouvDocument+=" and  L.qte1Stock>=0" ;
				 else JPQLQueryMouvDocument+=" and  L.qte1Stock<0" ;
			
				 
			if(criteres.getMois()!=null)
				JPQLQueryMouvDocument+=" and extract(month from F.dateGrMouvStock) = '"+criteres.getMois()+"'";
			if(criteres.getAnnee()!=null)
				JPQLQueryMouvDocument+=" and extract(year from F.dateGrMouvStock) = '"+criteres.getAnnee()+"'";
			if(criteres.getDateDeb()!=null)
				JPQLQueryMouvDocument+=" and F.dateGrMouvStock >=  '"+LibDate.dateToStringSql(criteres.getDateDeb())+"'";
			if(criteres.getDateFin()!=null)
				JPQLQueryMouvDocument+=" and F.dateGrMouvStock <=  '"+LibDate.dateToStringSql(criteres.getDateFin())+"'";		
			//Clause where
			if(criteres.getCodeArticle()!=null && !criteres.getCodeArticle().equals(""))
				JPQLQueryMouvDocument+=" and  a.codeArticle like '"+criteres.getCodeArticle()+"'";
			if(criteres.getCodeFamille()!=null && !criteres.getCodeFamille().equals(""))
				JPQLQueryMouvDocument+=" and fa.codeFamille like '"+criteres.getCodeFamille()+"'";
			if(criteres.getUn1()!=null && !criteres.getUn1().equals(""))
				JPQLQueryMouvDocument+=" and L.un1Stock like '"+criteres.getUn1()+"'";
			 if(criteres.getUn2()!=null && !criteres.getUn2().equals(""))
				 JPQLQueryMouvDocument+=" and L.un2Stock like '"+criteres.getUn2()+"'";
			//Clause group by
			 JPQLQueryMouvDocument+=" group by  F.dateGrMouvStock,fa.codeFamille,a.codeArticle,F.codeGrMouvStock, L.un1Stock, L.un2Stock";			
					
			 
			    //clause having
			    if(criteres.getQte1()!=null && !criteres.getQte1().equals(BigDecimal.valueOf(0))){
			    	if(!JPQLQueryMouvDocument.contains(" having "))
			    		JPQLQueryMouvDocument+=" having sum(L.qte1Stock) >= "+criteres.getQte1();
			    	else
			    		JPQLQueryMouvDocument+=" and sum(L.qte1Stock) >= "+criteres.getQte1();
			    }
			    if(criteres.getQte2()!=null && !criteres.getQte2().equals(BigDecimal.valueOf(0))){
			    	if(!JPQLQueryMouvDocument.contains(" having "))
			    		JPQLQueryMouvDocument+=" having sum(L.qte2Stock) >= "+criteres.getQte2();
			    	else
			    		JPQLQueryMouvDocument+=" and sum(L.qte2Stock) >= "+criteres.getQte2();		    	
			    }				
			
			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvDocument);
		    List<TaEtatMouvementDms> liste1 = ejbQuery.getResultList();
		    
		    JPQLQueryMouvDocument="";

		    
		    if(!criteres.getExclusionQteSsUnite()){
				JPQLQueryMouvDocument+= "  " +
				 " select NEW fr.legrain.dms.model.TaEtatMouvementDms('NC','NC' ,F.codeGrMouvStock,F.dateGrMouvStock,a.codeArticle," +
				 " fa.codeFamille,'"+mouvementSensLabel+"', extract(month from F.dateGrMouvStock) ," +
				 " extract(year from F.dateGrMouvStock) ,'0',"+
				 " L.un1Stock,'0',L.un2Stock ) "+
				 " from TaMouvementStock L  join L.taGrMouvStock F   join f.taTypeMouvStock tm  join L.taLot lot join lot.taArticle a  left join a.taFamille fa "+   //left 
				 " where  a.codeArticle is not null and (L.un1Stock='' ) and tm.dms=true and systeme=false";
				
				 if(criteres.getMouvementEntree()) //mouvement d'entrée ou de sortie
					 JPQLQueryMouvDocument+=" and  L.qte1Stock>=0" ;
				 else JPQLQueryMouvDocument+=" and  L.qte1Stock<0" ;
				 
				if(criteres.getMois()!=null)
					JPQLQueryMouvDocument+=" and extract(month from F.dateGrMouvStock) = '"+criteres.getMois()+"'";
				if(criteres.getAnnee()!=null)
					JPQLQueryMouvDocument+=" and extract(year from F.dateGrMouvStock) = '"+criteres.getAnnee()+"'";
				if(criteres.getDateDeb()!=null)
					JPQLQueryMouvDocument+=" and F.dateGrMouvStock >=  '"+LibDate.dateToStringSql(criteres.getDateDeb())+"'";
				if(criteres.getDateFin()!=null)
					JPQLQueryMouvDocument+=" and F.dateGrMouvStock <=  '"+LibDate.dateToStringSql(criteres.getDateFin())+"'";		
				//Clause where
				if(criteres.getCodeArticle()!=null && !criteres.getCodeArticle().equals(""))
					JPQLQueryMouvDocument+=" and  a.codeArticle like '"+criteres.getCodeArticle()+"'";
				if(criteres.getCodeFamille()!=null && !criteres.getCodeFamille().equals(""))
					JPQLQueryMouvDocument+=" and fa.codeFamille like '"+criteres.getCodeFamille()+"'";
				if(criteres.getUn1()!=null && !criteres.getUn1().equals(""))
					JPQLQueryMouvDocument+=" and L.un1Stock like '"+criteres.getUn1()+"'";
				 if(criteres.getUn2()!=null && !criteres.getUn2().equals(""))
					 JPQLQueryMouvDocument+=" and L.un2Stock like '"+criteres.getUn2()+"'";
				//Clause group by
				 JPQLQueryMouvDocument+=" group by  F.dateGrMouvStock,fa.codeFamille,a.codeArticle,F.codeGrMouvStock, L.un1Stock, L.un2Stock";			
		    }													
			    //clause having
			    if(criteres.getQte1()!=null && !criteres.getQte1().equals(BigDecimal.valueOf(0))){
			    	if(!JPQLQueryMouvDocument.contains(" having "))
			    		JPQLQueryMouvDocument+=" having sum(L.qte1Stock) >= "+criteres.getQte1();
			    	else
			    		JPQLQueryMouvDocument+=" and sum(L.qte1Stock) >= "+criteres.getQte1();
			    }
			    if(criteres.getQte2()!=null && !criteres.getQte2().equals(BigDecimal.valueOf(0))){
			    	if(!JPQLQueryMouvDocument.contains(" having "))
			    		JPQLQueryMouvDocument+=" having sum(L.qte2Stock) >= "+criteres.getQte2();
			    	else
			    		JPQLQueryMouvDocument+=" and sum(L.qte2Stock) >= "+criteres.getQte2();		    	
			    }				
			
			ejbQuery = entityManager.createQuery(JPQLQueryMouvDocument);
		    List<TaEtatMouvementDms> liste2 = ejbQuery.getResultList();
		    liste1.addAll(liste2);
			return liste1;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvDocumentSortiesDms failed", re);
			throw re;
		}
	}

	@Override
	public List<TaEtatStockCapsules> selectSumMouvEntreesCRD(TaEtatStockCapsules criteres,Date dateDeb,boolean strict) {
		try {				
			

			String JPQLQueryMouvEntrees="select NEW fr.legrain.article.titretransport.model.TaEtatStockCapsules(a.codeTitreTransport,a.libelleTitreTransport,  CASE tm.sens  WHEN true THEN 'E' WHEN false THEN 'S'  else 'S' END as sens , "+
		    " S.dateStock, sum(S.qteTitreTransport), '',S.libelleStock,tm.libelle,'10' )"+
		    " from TaMouvementStock S join S.taGrMouvStock gr join gr.taTypeMouvStock tm,TaTitreTransport a where a.codeTitreTransport=S.codeTitreTransport " +
		    " and tm.sens = true and tm.crd=true and tm.systeme=false  " ;//+
			
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
		    JPQLQueryMouvEntrees+=" group by a.codeTitreTransport,a.libelleTitreTransport,tm.sens, S.dateStock,S.libelleStock,tm.libelle";		

		    
		    Query ejbQuery = entityManager.createQuery(JPQLQueryMouvEntrees);
		    List<TaEtatStockCapsules> l = ejbQuery.getResultList();

			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvEntrees failed", re);
			throw re;
		}
	}
	@Override
	public List<TaEtatStockCapsules> selectSumMouvSortiesCRD(TaEtatStockCapsules criteres,Date dateDeb, boolean strict) {
		try {																				//S.mouvStock
			String JPQLQueryMouvSorties="select NEW fr.legrain.article.titretransport.model.TaEtatStockCapsules(a.codeTitreTransport,a.libelleTitreTransport,CASE tm.sens  WHEN true THEN 'E' WHEN false THEN 'S'  else 'S' END as sens, "+
		    " S.dateStock, sum(-1*S.qteTitreTransport), '',S.libelleStock,tm.libelle,'11' )"+
		    " from TaMouvementStock S join S.taGrMouvStock gr join gr.taTypeMouvStock tm,TaTitreTransport a where a.codeTitreTransport=S.codeTitreTransport " +
		    " and tm.sens = false  and tm.crd=true and tm.systeme=false " ;//+

			
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
		    JPQLQueryMouvSorties+=" group by a.codeTitreTransport,a.libelleTitreTransport,tm.sens, S.dateStock,S.libelleStock,tm.libelle";					
			

			Query ejbQuery = entityManager.createQuery(JPQLQueryMouvSorties);
		    List<TaEtatStockCapsules> l = ejbQuery.getResultList();

			return l;
		} catch (RuntimeException re) {
			logger.error("selectSumMouvSorties failed", re);
			throw re;
		}
	}


}
