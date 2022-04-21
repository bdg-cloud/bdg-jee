package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosTicketDeCaisse;
import fr.legrain.document.model.TaLTicketDeCaisse;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.ITicketDeCaisseDAO;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.supportAbonnements.dao.ISupportAbonnementDAO;
import fr.legrain.tiers.dao.ITiersDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaTicketDeCaisse.
 * @see fr.legrain.documents.dao.TaTicketDeCaisse
 * @author Hibernate Tools
 */



public class TaTicketDeCaisseDAO extends  AbstractDocumentPayableDAO<TaTicketDeCaisse,TaInfosTicketDeCaisse,TaLTicketDeCaisse> 
implements ITicketDeCaisseDAO {


//	private static final Log log = LogFactory.getLog(TaTicketDeCaisseDAO.class);
	static Logger logger = Logger.getLogger(TaTicketDeCaisseDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTicketDeCaisse a ";
	private String supplementJPQLQuery = " order by codeDocument";
//	private String defaultJPQLQuery = "select a from TaTicketDeCaisse a";
	public static final String FIND_BY_DATE_MAINTENANCE = "select distinct a from TaTicketDeCaisse a  join a.lignes b where  a.codeDocument like('%') "+
	" and  b.taArticle.taFamille.idFamille between 21 and 21  and a.dateDocument between ? and ?  order by a.codeDocument"; 
	public static final String FIND_BY_CODE_MAINTENANCE = "select distinct a from TaTicketDeCaisse a  join a.lignes b where  a.codeDocument like('%') "+
	" and  b.taArticle.taFamille.idFamille between 21 and 21  and a.codeDocument between ? and ?  order by a.codeDocument";
	public static final String FIND_BY_MAINTENANCE = "select distinct alias from TaTicketDeCaisse alias  join alias.lignes b where b.taArticle.taFamille.idFamille between 21 and 21 " +
	  " and (alias.dateDocument between (select datedebInfoEntreprise from TaInfoEntreprise ) and (select datefinInfoEntreprise from TaInfoEntreprise ))";
	
	@Inject ISupportAbonnementDAO daoSupport;
	@Inject ITiersDAO daoTiers;
//	@Inject IAbonnementDAO daoAbonnement;
	
	public TaTicketDeCaisseDAO(){
//		this(null);
	}
	
//	public TaTicketDeCaisseDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaTicketDeCaisse.class.getSimpleName());
//		initChampId(TaTicketDeCaisse.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaTicketDeCaisse());
//	}
	
//	public TaTicketDeCaisse refresh(TaTicketDeCaisse detachedInstance) {
//        logger.debug("refresh instance");
//        try {
//        	Map<IDocumentTiers,Boolean> listeEtatLegrain=new LinkedHashMap<IDocumentTiers, Boolean>();
//        	
//        	detachedInstance.setLegrain(false);
//        	List<TaRReglement>  listeTempReglement = new LinkedList<TaRReglement>();
//        	for (TaRReglement rReglement : detachedInstance.getTaRReglements()) {
//				if(rReglement.equals(detachedInstance.getTaRReglement()) && detachedInstance.getTaRReglement().getId()==0)
//					listeTempReglement.add(rReglement);
//			}
//
//        	for (TaRAcompte obj : detachedInstance.getTaRAcomptes()) {
//        		listeEtatLegrain.put(obj.getTaAcompte(),obj.getTaAcompte().isLegrain());
//        		obj.getTaAcompte().setLegrain(false);
//			}
//        	for (TaRAvoir obj : detachedInstance.getTaRAvoirs()) {
//        		listeEtatLegrain.put(obj.getTaAvoir(),obj.getTaAvoir().isLegrain());
//        		obj.getTaAvoir().setLegrain(false);
//			}
//        	
//			for (TaRReglement taRReglement : listeTempReglement) {
//				detachedInstance.removeReglement(taRReglement);
//			}
//        	
//			entityManager.refresh(detachedInstance);
//        	
//			for (TaRReglement taRReglement : listeTempReglement) {
//				detachedInstance.addRReglement(taRReglement);
//			}
//			
//        	for (TaRAcompte obj : detachedInstance.getTaRAcomptes()) {        		
//        		obj.getTaAcompte().setLegrain(retourneEtatLegrain(listeEtatLegrain,obj.getTaAcompte()));
//			}
//        	for (TaRAvoir obj : detachedInstance.getTaRAvoirs()) {
//        		obj.getTaAvoir().setLegrain(retourneEtatLegrain(listeEtatLegrain,obj.getTaAvoir()));
//			}
////            org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////
////            for (TaRAcompte a : detachedInstance.getTaRAcomptes()) {
//////            	new TaAcompteDAO(entityManager).refresh(a.getTaAcompte());
////                 session.evict(a.getTaAcompte().getTaRAcomptes());
////                 session.evict(a.getTaAcompte());
////            }
////            
////            for (TaRAvoir a : detachedInstance.getTaRAvoirs()) {
//////            	new TaAvoirDAO(entityManager).refresh(a.getTaAvoir());
////                session.evict(a.getTaAvoir().getTaRAvoirs());
////                session.evict(a.getTaAvoir());
////           }
////            
////            for (TaRReglement a : detachedInstance.getTaRReglements()) {
////                session.evict(a.getTaReglement().getTaRReglements());
////                session.evict(a.getTaReglement());
////                session.evict(a.getTaTicketDeCaisse().getTaRReglement());
//////                for (TaRReglement b : a.getTaTicketDeCaisse().getTaRReglements()) {
//////                	session.evict(a.getTaReglement());
//////				} 
////           }
////
////            session.evict(detachedInstance.getTaRReglement());
////            
////             
////            session.evict(detachedInstance.getTaRAcomptes());
////            session.evict(detachedInstance.getTaRAvoirs());
////            session.evict(detachedInstance.getTaRReglements());
////            session.evict(detachedInstance);
////            
////            detachedInstance = entityManager.find(TaTicketDeCaisse.class, detachedInstance.getIdDocument());
//            detachedInstance.setLegrain(true);
//            detachedInstance.calculLignesTva();
//            detachedInstance.affecteReglementFacture();
//            return detachedInstance;
//
//        } catch (RuntimeException re) {
//            logger.error("refresh failed", re);
//            throw re;
//        } catch (Exception e) {
//        	 logger.error("refresh failed", e);
//        	 return null;
//		}
//    } 
    
	public void persist(TaTicketDeCaisse transientInstance) {
		logger.debug("persisting TaTicketDeCaisse instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaTicketDeCaisse persistentInstance) {
		logger.debug("removing TaTicketDeCaisse instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}
	
//	public void annuler(TaTicketDeCaisse persistentInstance) throws Exception{
//		persistentInstance.setLegrain(false);
//		List<TaLFacture> listTemp=new ArrayList<TaLFacture>(0);
//		for (Object ligne : persistentInstance.getLignes()) {
//			if(((TaLFacture)ligne).getIdLDocument()==0)
//				listTemp.add((TaLFacture)ligne);				
//		}
//		for (TaLFacture taLFacture : listTemp) {
//			persistentInstance.getLignes().remove(taLFacture);
//		}
//		super.annuler(persistentInstance);
//	}
	

	public TaTicketDeCaisse merge(TaTicketDeCaisse detachedInstance) {
		logger.debug("merging TaTicketDeCaisse instance");
		try {
			TaTicketDeCaisse result = entityManager.merge(detachedInstance);
//			if(detachedInstance.getTaInfosDocument()==null) {
//				throw new RuntimeException("Il manque l'infoDocument du document n°: "+detachedInstance.getCodeDocument());
//			}
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaTicketDeCaisse findById(int id) {
		logger.debug("getting TaTicketDeCaisse instance with id: " + id);
		try {
			TaTicketDeCaisse instance = entityManager.find(TaTicketDeCaisse.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTicketDeCaisse> findByChamp(String champ,Object value) {
		logger.debug("getting TaTicketDeCaisse instance with champ : " + champ);
		try {
			Query query = entityManager.createQuery("select a from TaTicketDeCaisse a " +
					"where "+champ+" = '"+value+"'");
			List<TaTicketDeCaisse> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<TaTicketDeCaisse> findByCodeTiers(String codeTiers) {
		logger.debug("getting TaTicketDeCaisse instance with codeTiers: " + codeTiers);
		List<TaTicketDeCaisse> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS);
			query.setParameter("codeTiers", codeTiers);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTicketDeCaisse> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaTicketDeCaisse instance with codeTiers: " + codeTiers);
		List<TaTicketDeCaisse> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTicketDeCaisse> findBySessionCaisseCourante() {
		List<TaTicketDeCaisse> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_SESSION_CAISSE_COURANTE);
//			query.setParameter(1, codeTiers);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaTicketDeCaisse> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaTicketDeCaisse instance with codeTiers: " + codeTiers);
		List<TaTicketDeCaisse> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_DATE_POUR_COMPTE_CLIENT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * |année|codeTiers|HT|TVA|TTC
	 * @param codeTiers
	 * @param debut
	 * @param fin
	 * @return
	 */
	public List<Object> findChiffreAffaireByCodeTiers(String codeTiers,Date debut, Date fin, int precision) {
		logger.debug("getting TaTicketDeCaisse instance with codeTiers: " + codeTiers);
		List<Object> result = null;
		try {
			String jpql = null;
			String groupBy = null;
			String select = null;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaTicketDeCaisse f "
//			+"left join f.taTiers t "
//			+"where  t.codeTiers = ? and f.dateDocument between ? and ? "
//			+"group by extract(year from f.dateDocument),t.codeTiers"
//			;
			
			if(precision==0) {
				select = "'','',cast(extract(year from f.dateDocument)as string)";
				groupBy = "'','',extract(year from f.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "'',extract(month from f.dateDocument),extract(year from f.dateDocument)";
			} else {
				select = "cast(extract(day from f.dateDocument)as string),cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			}
			
			jpql = "select  "
			+select+", t.codeTiers,"
			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
			+" from TaTicketDeCaisse f "
			+"left join f.taTiers t "
			+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
			+"group by "+groupBy+",t.codeTiers"
			;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaTicketDeCaisse f where f.codeDocument='"+code+"'");
			Long instance = (Long)query.getSingleResult();
			logger.debug("get successful");
			return instance!=0;
			}
			return false;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaTicketDeCaisse findByCode(String code) {
		logger.debug("getting TaTicketDeCaisse instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaTicketDeCaisse a " +
					" left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");		
			TaTicketDeCaisse instance = (TaTicketDeCaisse)query.getSingleResult();
			instance.setLegrain(true);
//			instance.affecteReglementFacture(null);   //ejb à remettre 
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		} catch (Exception e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaTicketDeCaisse> selectAll() {
		logger.debug("selectAll TaTicketDeCaisse");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery+supplementJPQLQuery);
			List<TaTicketDeCaisse> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	

	
	/**
	 * Recherche les factures entre 2 codes factures
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 codes (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(String codeDeb, String codeFin) {
	public List<TaTicketDeCaisse> rechercheDocument(String codeDeb, String codeFin) {
		List<TaTicketDeCaisse> result = null;
		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;

	}
	
	//Fiche_CA_Articles_AnneeCourante_Tous.rptdesign
	//select a from TaTicketDeCaisse a where a.dateFacture between ? and ?
	/**
	 * select TA_FACTURE.CODE_FACTURE,TA_FACTURE.ID_TIERS,TA_FACTURE.LIBELLE_FACTURE,
	 * cast(TA_FACTURE.DATE_FACTURE as date),TA_ARTICLE.CODE_ARTICLE from TA_L_FACTURE  
	 * left join TA_FACTURE on (TA_FACTURE.ID_FACTURE = TA_L_FACTURE.ID_FACTURE )
	 * left join TA_ARTICLE on (TA_ARTICLE.ID_ARTICLE = TA_L_FACTURE.ID_ARTICLE ) 
	 * where TA_L_FACTURE.ID_ARTICLE=? And 
	 * cast(TA_FACTURE.DATE_FACTURE as date) 
	 * between ( select cast (TA_INFO_ENTREPRISE.DATEDEB_INFO_ENTREPRISE as date ) from TA_INFO_ENTREPRISE) 
	 * and ( select cast (TA_INFO_ENTREPRISE.DATEFIN_INFO_ENTREPRISE as date ) from TA_INFO_ENTREPRISE) 
	 * order by TA_FACUTRE.CODE_FACTURE,cast(TA_FACTURE.DATE_FACTURE as date)
	 */
	
	public List<Object> rechercheAllFactureBaseArticle(int idArticle,Date dateDeb, Date dateFin){
		
/***************** example entre sql et jpql******************/
//		select i
//		from Item i join i.bids b
//		where i.description like '%Foo%'
//		and b.amount > 100
//		
//		select i.DESCRIPTION, i.INITIAL_PRICE, ...
//		from ITEM i
//		inner join BID b on i.ITEM_ID = b.ITEM_ID
//		where i.DESCRIPTION like '%Foo%'
//		and b.AMOUNT > 100
/***********************************************************/
		//fr.legrain.lib.data.LibConversion.stringToInteger() as aa
		LibConversion libConversion = new LibConversion();
		//String jpql = "select tlf from TaLFacture tlf where tlf.taDocument.idDocument = 1"; 

		String jpql = "select taA from TaAdresse taA where taA.taTiers.idTiers=:idTiers and taA.taTAdr.codeTAdr=:codeTAdr";
		
		
//		Calendar calendarDateFin = Calendar.getInstance();
//		calendarDateFin.setTime(dateFin);
//		calendarDateFin.set(Calendar.YEAR, calendarDateFin.get(Calendar.YEAR)-1);
//		Date fin = calendarDateFin.getTime();
//		Timestamp timestampFin = new Timestamp(fin.getTime());
//		
//		Calendar calendarDateStart = Calendar.getInstance();
//		calendarDateStart.setTime(dateDeb);
//		calendarDateStart.set(Calendar.YEAR, calendarDateStart.get(Calendar.YEAR)-1);
//		Date deb = calendarDateStart.getTime();
//		Timestamp timestampDeb = new Timestamp(deb.getTime());
//		
//		String jpql = "select " +
//		   "tf.dateDocument " +
//		   "from TaLFacture tlf "+
//		   "left join tlf.taDocument tf ";
		   

		Query query = entityManager.createQuery(jpql);
		//Query query = entityManager.createNativeQuery(jpql);
		query.setParameter("idTiers", 1);
		query.setParameter("codeTAdr", "");
//		query.setParameter(3, dateFin);
		
//		query.setParameter(1, timestampDeb);
//		query.setParameter(2, timestampFin);
		
		
//		List<Object> listObjectQuery = new ArrayList<Object>();
//		for (int i = 0; i < query.getResultList().size(); i++) {
//			TaTicketDeCaisse TaTicketDeCaisse = (TaTicketDeCaisse) query.getResultList().get(i);
//			TaTicketDeCaisse.calculeTvaEtTotaux();
//			listObjectQuery.add(TaTicketDeCaisse);
//		}
		System.out.println(jpql);
		List<Object> listObjectQuery = query.getResultList();
		return listObjectQuery;
	}
	
	
	
	/**
	 * Recherche les factures entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaTicketDeCaisse> rechercheDocument(Date dateDeb, Date dateFin) {
		List<TaTicketDeCaisse> result = null;
		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	
//	/**
//	 * Recherche les factures light entre 2 dates
//	 * @param dateDeb - date de début
//	 * @param dateFin - date de fin
//	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
//	 */
//	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
//	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin) {
//		List<Object[]> result = null;
//		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_LIGHT);
//		query.setParameter(1,"%");
//		query.setParameter(2, dateDeb, TemporalType.DATE);
//		query.setParameter(3, dateFin, TemporalType.DATE);
//		result = query.getResultList();
//		
//		
//		return result;
//		
//	}
	
	/**
	 * Recherche les factures entre 2 dates light
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light) {
		List<Object[]> result = null;
		Query query = null;
		if(light)
			query =entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}	
	
	
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,String codeTiers) {
		List<Object[]> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	/**
	 * Recherche les factures entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaTicketDeCaisse> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {
		List<TaTicketDeCaisse> result = null;
		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	/**
	 * Recherche les factures entre 2 dates et non exportees
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaTicketDeCaisse> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaTicketDeCaisse> result = null;
		Query query =null;
		if(parDate)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT_PARDATE);
		else
		query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	/**
	 * Recherche les factures entre 2 dates et non exportees
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) {
		List<TaTicketDeCaisseDTO> result = null;
		Query query =null;
		if(parDate)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT_PARDATE_LIGHT);
		else
		query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT_LIGHT);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	
	/**
	 * Recupere la liste des facture exportees ou non exportees
	 * @param export - vrai pour le facture deja exportees, faux pour celles non exportees
	 * @return
	 */
	public List<TaTicketDeCaisse> findByExport(boolean export) {
		try {
			String requete="select a from TaTicketDeCaisse a ";
			Query query = entityManager.createQuery(requete );
			if(export)
				requete+="where a.dateExport is not null";
			else 
				requete+="where a.dateExport is null";
			List<TaTicketDeCaisse> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			logger.error("findByExport failed", re);
			throw re;
		}
	}
	
	public List<TaTicketDeCaisse> rechercheFactureMaintenance(String codeDeb, String codeFin) {
		
		List<TaTicketDeCaisse> result = null;
//		String requete ="select distinct a from TaTicketDeCaisse a " +
//		" join a.lignes b"+
//		" where  a.codeDocument like('%') "+
//		" and  b.taArticle.taFamille.idFamille between 21 and 21 "
//		+" and a.codeDocument between ? and ?  order by a.codeDocument";
//		Query query = entityManager.createQuery(requete);
		Query query =entityManager.createQuery(FIND_BY_CODE_MAINTENANCE);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;

	}
	
	public List<TaTicketDeCaisse> rechercheFactureMaintenance(Date dateDeb, Date dateFin) {
		
		List<TaTicketDeCaisse> result = null;
//		String requete ="select distinct a from TaTicketDeCaisse a " +
//				" join a.lignes b"+
//		  " where  a.codeDocument like('%') "+
//		  " and  b.taArticle.taFamille.idFamille between 21 and 21 "
//		+" and a.dateDocument between ? and ?  order by a.codeDocument";
		
		Query query =entityManager.createQuery(FIND_BY_DATE_MAINTENANCE);
//		Query query = entityManager.createQuery(requete);
		query.setParameter("dateDeb", dateDeb);
		query.setParameter("dateFin", dateFin);
		result = query.getResultList();
		return result;

	}

//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeArticle, Date debut, Date fin) {
		logger.debug("getting TaTicketDeCaisse instance with codeTiers: " + codeArticle);
		List<TaTiers> result = null;
		try {
			String jpql = null;
			jpql = "select distinct t "
				+" from TaTicketDeCaisse f "
				+"left join f.taTiers t "
				+"left join f.lignes lf "
				+"left join lf.taArticle art "
				+"where  art.codeArticle = :codeArticle and f.dateDocument between :dateDeb and :dateFin "
				;
				Query query = entityManager.createQuery(jpql);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeArticle, Date debut, Date fin, int precision) {
		logger.debug("getting ChiffreAffaire instance with codeArticle: " + codeArticle);
		List<Object> result = null;
		try {
			String jpql = null;
			String groupBy = null;
			String select = null;
			/*
select  substring(a.codepostalAdresse,1,2) as departement,
 extract(year from f.dateDocument)as annee, 
t.codeTiers as codeTiersFacture, t.nomTiers as nomTiersFacture,
 case when extract ( year from f.dateDocument) <= 2001 
then  sum(lf.mtHtLApresRemiseGlobaleDocument/6.55957) else sum(lf.mtHtLApresRemiseGlobaleDocument) end,
 case when extract ( year from f.dateDocument) <= 2001 
then  sum(lf.mtTtcLApresRemiseGlobaleDocument-lf.mtHtLApresRemiseGlobaleDocument/6.55957) else sum(lf.mtTtcLApresRemiseGlobaleDocument-lf.mtHtLApresRemiseGlobaleDocument) end,
 case when extract ( year from f.dateDocument) <= 2001 
then  sum(lf.mtTtcLApresRemiseGlobaleDocument/6.55957 ) else sum(lf.mtTtcLApresRemiseGlobaleDocument) end,
 art.codeArticle,fa.codeFamille  
from TaTicketDeCaisse f 
left join f.taTiers t 
left join t.taAdresse a 
left join f.lignes lf 
left join lf.taArticle art 
left join art.taFamille fa 
where 
lf.taArticle is not null   
and  ((t.codeTiers)  like  '00001%' or (t.codeTiers)  >=  '00001') group by substring(a.codepostalAdresse,1,2),
extract(year from f.dateDocument),
t.codeTiers,
t.nomTiers,
art.codeArticle,
fa.codeFamille
			 */
			
			if(precision==0) {
				select = "'','',cast(extract(year from f.dateDocument)as string)";
				groupBy = "'','',extract(year from f.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "'',extract(month from f.dateDocument),extract(year from f.dateDocument)";
			} else {
				select = "cast(extract(day from f.dateDocument)as string),cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			}

			jpql = "select  "
			+select+", " +
					//"t.codeTiers,"
					"art.codeArticle,"
			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(lf.mtHtLDocument) end, "
			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(lf.mtTtcLDocument-lf.mtHtLDocument) end, "
			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(lf.mtTtcLDocument) end "
			+" from TaTicketDeCaisse f "
			+"left join f.taTiers t "
			+"left join f.lignes lf "
			+"left join lf.taArticle art "
			+"where  art.codeArticle = :codeArticle and f.dateDocument between :dateDeb and :dateFin "
			+"group by "+groupBy +
//					",t.codeTiers"
					",art.codeArticle"
			;
			Query query = entityManager.createQuery(jpql);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	/**
	 * Méthode permettant de retourner le chiffre d'affaire total généré
	 * @param debut
	 * @param fin
	 * @param precision
	 * @return
	 */
	public List<Object> findChiffreAffaireTotal(Date debut, Date fin, int precision) {
		logger.debug("getting ChiffreAffaire total");
		List<Object> result = null;
		try {
			String requete = "";
			String groupBy = "";
			String select = "";
			
			if(precision==0) {
				select = "'' as jour,'' as mois,cast(extract(year from f.dateDocument)as string) as annee";
				groupBy = " cast('' as string), cast('' as string),extract(year from f.dateDocument)";
			} else if (precision==1){
				select = "'' as jour ,cast(extract(month from f.dateDocument)as string) as mois,cast(extract(year from f.dateDocument)as string) as annee";
				groupBy = " cast('' as string),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			} else {
				select = "cast(extract(day from f.dateDocument)as string) as jour,cast(extract(month from f.dateDocument)as string) as mois,cast(extract(year from f.dateDocument)as string) as annee";
				groupBy = "extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			}
			
			requete = "SELECT "+select+ ", "
//			+" case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+" case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+" case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
			+" sum(f.netHtCalc), "
			+" sum(f.netTvaCalc), "
			+" sum(f.netTtcCalc)"
			+" FROM TaTicketDeCaisse f " 
//			+" where f.dateDocument between cast('?' as date) and cast('?' as date)"
//			+" where f.dateDocument between ?1 and ?2"
			+" where f.dateDocument between :datedeb and :dateFin"
			+" group by "+groupBy
			+"order by jour, mois, annee" ; 
			Query query = entityManager.createQuery(requete);
//			query.setParameter(1, debut);
//			query.setParameter(2, fin);
			query.setParameter("datedeb", debut);
			query.setParameter("dateFin", fin);
			logger.debug(requete);
			result = query.getResultList();
			for (Object object : result) {
				String jour = (String) ((Object[])object)[0];
				while (jour.length()<2)
					jour="0"+jour;
				((Object[])object)[0] = jour;
				String mois = (String) ((Object[])object)[1];
				while (mois.length()<2)
					mois="0"+mois;
				((Object[])object)[1] = mois;
			}
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * Classe permettant d'obtenir de la base de données X clients ordonnés par chiffre d'affaires
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param leNbResult le nombre de résultats à sortir de la BD
	 * @param limiter Si limiter est vrai, alors la liste retournée retourner un nombre de résultats égal à  nbResult
	 * Si cet int est positif, il sort les leNbResult meilleurs
	 * Si cet int est négatif, il sort les leNbResult moins bons
	 * @return La requête renvoyée renvoi le CA, le codeTiers, le nom et le CP
	 */
	public List<Object> findMeilleursClientsParCA(Date debut, Date fin, int leNbResult, boolean limiter) {
		logger.debug("getting ChiffreAffaire total ordo tiers");
		
		int nbResult = leNbResult; // nombre de résultats à afficher
		String ordre = "DESC";     // Ordre d'affichage
		
		if (nbResult<0){ // On passe en paramètres une valeur négative : on souhaite les x derniers
			nbResult = Math.abs(leNbResult); // On prend la valeur absolue du nombre désiré
			ordre = "ASC";					   // On inverse l'ordre d'affichage
			
		}
		List<Object> result = null;
		try {
			String requete = "";

			
			requete = "SELECT "
			+" sum(f.mtHtCalc)"
			+", t.codeTiers"
			+", t.nomTiers"
			+", a.codepostalAdresse"
			+" FROM TaTicketDeCaisse f " 
			+" left join f.taTiers t"
			+" left join t.taAdresse a"
			+" where f.dateDocument between :dateDeb and :dateFin"
			+" group by t.codeTiers, t.nomTiers, a.codepostalAdresse"
			+" order by sum(f.mtHtCalc)"+ordre;
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			if (limiter){
				query.setMaxResults(nbResult);
			}
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	/**
	 * Classe permettant d'obtenir de la base de données X articles ordonnés par chiffre d'affaires
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param leNbResult le nombre de résultats à sortir de la BD
	 * @param limiter Si limiter est vrai, alors la liste retournée retourner un nombre de résultats égal à  nbResult
	 * Si cet int est positif, il sort les leNbResult meilleurs
	 * Si cet int est négatif, il sort les leNbResult moins bons
	 * @return La requête renvoyée renvoi le CA, le codeTiers, le nom et le CP
	 */
	public List<Object> findMeilleursArticlesParCA(Date debut, Date fin, int leNbResult, boolean limiter) {
		logger.debug("getting ChiffreAffaire total ordo articles");
		
		int nbResult = leNbResult; // nombre de résultats à afficher
		String ordre = "DESC";     // Ordre d'affichage
		
		if (nbResult<0){ // On passe en paramètres une valeur négative : on souhaite les x derniers
			nbResult = Math.abs(leNbResult); // On prend la valeur absolue du nombre désiré
			ordre = "ASC";					   // On inverse l'ordre d'affichage
			
		}
		List<Object> result = null;
		try {
			String requete = "";

			
			requete = "SELECT a.codeArticle"
			+", a.libellecArticle"
			+", p.prixPrix"
			+", sum(l.qteLDocument)"
			+", sum(l.mtHtLDocument)"
			+" FROM TaLFacture l " 
			+" left join l.taDocument f"
			+" left join l.taArticle a"
			+" left join a.taPrix p"
			+" where f.dateDocument between :dateDeb and :dateFin"
			+" group by a.codeArticle, a.libellecArticle, p.prixPrix"
			+" order by sum(l.mtHtLDocument)"+ordre;
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			if (limiter){
				query.setMaxResults(nbResult);
			}
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	/**
	 * 
	 * @param codeArticle
	 * @param debut
	 * @param fin
	 * @param qte1OuQte2 - 1 pour selectionner les quantités 1 et 2 pour selectionne les quantités 2 
	 * @return
	 */
	public List<Object> findQuantiteByCodeArticle(String codeArticle, Date debut, Date fin, int qte1OuQte2, int precision) {
		logger.debug("getting Quantite instance with codeArticle: " + codeArticle);
		List<Object> result = null;
		try {
			String jpql = null;
			String groupBy = null;
			String select = null;
			
			if(precision==0) {
				select = "'','',cast(extract(year from f.dateDocument)as string)";
				groupBy = "'','',extract(year from f.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "'',extract(month from f.dateDocument),extract(year from f.dateDocument)";
			} else {
				select = "cast(extract(day from f.dateDocument)as string),cast(extract(month from f.dateDocument)as string),cast(extract(year from f.dateDocument)as string)";
				groupBy = "extract(day from f.dateDocument),extract(month from f.dateDocument),extract(year from f.dateDocument)";
			}
			
			jpql = "select  "
			+select+", " +
					"art.codeArticle,";
			
			if(qte1OuQte2==1) {
				jpql += "lf.u1LDocument,sum(lf.qteLDocument)";
			} else {
				jpql += "lf.u2LDocument,sum(lf.qte2LDocument)";
			}

			jpql += " from TaTicketDeCaisse f "
			+"left join f.taTiers t "
			+"left join f.lignes lf "
			+"left join lf.taArticle art "
			+"where  art.codeArticle = :codeArticle and f.dateDocument between :dateDeb and :dateFin "
			+"group by "+groupBy +
					",art.codeArticle";
			if(qte1OuQte2==1) {
				jpql += ",lf.u1LDocument";
			} else {
				jpql += ",lf.u2LDocument";
			}
			
			Query query = entityManager.createQuery(jpql);
			query.setParameter("codeArticle", codeArticle);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegleReel(Date dateDeb, Date dateFin) {
		List<TaTicketDeCaisse> result = new LinkedList<TaTicketDeCaisse>();
		try {
			String codeTiers="%";
			String codeDocument="%";
			String requete="select a "
					+ " from TaTicketDeCaisse a where a.netTtcCalc-((select coalesce(sum(r.affectation),0) from TaRReglement r join r.TaTicketDeCaisse d where d is not null) + "
					+ " (select coalesce(sum(r.affectation),0) from TaRAcompte r join r.TaTicketDeCaisse d where d is not null) +"
					+ " (select coalesce(sum(r.affectation),0) from TaRAvoir r join r.TaTicketDeCaisse d where d is not null)"
					+ " ) <>0  and a.dateDocument between :deb and :fin" 
					+ " order by a.taTiers.codeTiers,a.codeDocument";
			
			Query query = entityManager.createQuery(requete);
			if(dateDeb==null)dateDeb=new Date(0);
			if(dateFin==null)dateFin=new Date("01/01/3000");

			query.setParameter("deb", dateDeb, TemporalType.DATE);
			query.setParameter("fin", dateFin, TemporalType.DATE);
			result=query.getResultList();
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;

	}
	/**
	 * Recherche les factures non totalement réglées entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @param tiers - tiers sur lequel porte la recherche, si null, alors on prend tous les tiers
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaTicketDeCaisseDTO> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin,String tiers,String document) {
		List<TaTicketDeCaisseDTO> result = new LinkedList<TaTicketDeCaisseDTO>();
		try {
			String[] lines = null;
			if(tiers!=null && !tiers.equals(""))
				 lines = tiers.split("\r\n|\r|\n");
		List<TaTicketDeCaisseDTO> resultTemp = null;
		String codeTiers="%";
		String codeDocument="%";
		String requete="select new fr.legrain.document.dto.TaTicketDeCaisseDTO(f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, "
				+ "tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc,coalesce(sum(ra.affectation),0)as sumRAvoir,"
				+ "coalesce(sum(rr.affectation),0)as sumRReglement ) "
				+ "from TaTicketDeCaisse f join f.taInfosDocument infos join f.taTiers tiers  left join f.taRAvoirs ra  "
				+ " left join f.taRReglements rr  where " ;
		if(lines==null || lines.length==0)
			requete+=" (tiers.codeTiers like '%') and ";
		else{
			requete+="(";
		for (int i = 0; i < lines.length; i++) {
			if(i>0)requete+=" or ";
			requete+=" tiers.codeTiers like upper('"+lines[i]+"') ";
		}
		requete+=") and";
		}

		requete+=" f.codeDocument like :codeDocument" +
				" and f.dateDocument between :dateDeb and :dateFin "+
				" group by f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, "
				+ "tiers.codeTiers, infos.nomTiers,f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTtcCalc"+
				" having coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0)< f.netTtcCalc "

				+ " order by tiers.codeTiers,f.codeDocument";
		Query query = entityManager.createQuery(requete);
		if(tiers != null && !tiers.equals(""))codeTiers=tiers.toUpperCase();
		if(dateDeb==null)dateDeb=new Date(0);
		if(dateFin==null)dateFin=new Date("01/01/3000");
		if(document!=null && !document.equals(""))codeDocument=document.toUpperCase();

//		query.setParameter(1, codeTiers);
		query.setParameter("codeDocument", codeDocument);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		
		result=query.getResultList();;
//		for (TaTicketDeCaisse TaTicketDeCaisse : resultTemp) {
//			TaTicketDeCaisse.calculSommeAvoir();
//			TaTicketDeCaisse.calculSommeAcomptes();
//			if(TaTicketDeCaisse.calculRegleDocumentComplet().compareTo(TaTicketDeCaisse.getNetTtcCalc())!=0)
//				result.add(TaTicketDeCaisse);
//		}
		
	} catch (Exception e) {
		logger.error("", e);
	}
		return result;
		
	}
  
//	/**
//	 * Recherche les factures non totalement réglées dont la date d'échéance rentre dans l'intervalle
//	 * des 2 dates passées en paramètres
//	 * @param dateDeb - date de début
//	 * @param dateFin - date de fin
//	 * @param tiers - tiers sur lequel porte la recherche, si null, alors on prend tous les tiers
//	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
//	 */
//	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegleAEcheance(Date dateDeb, Date dateFin,String tiers,String document,BigDecimal limite) {
//		if(limite==null)limite=BigDecimal.valueOf(0);
//		List<TaTicketDeCaisse> result = new LinkedList<TaTicketDeCaisse>();
//		List<TaTicketDeCaisse> resultTemp = null;
//		String codeTiers="%";
//		String codeDocument="%";
//		String requete="select f from TaTicketDeCaisse f " +
//				"where f.taTiers.codeTiers like ? " +
//				" and f.codeDocument like ?" +
//				" and f.dateEchDocument between ? and ? " +
//				//" and f.regleCompletDocument < f.netTtcCalc " +
//				" order by f.taTiers.codeTiers,f.codeDocument";
//		Query query = entityManager.createQuery(requete);
//		if(tiers != null && !tiers.equals(""))codeTiers=tiers.toUpperCase();
//		if(dateDeb==null)dateDeb=new Date(0);
//		if(dateFin==null)dateFin=new Date("01/01/3000");
//		if(document!=null && !document.equals(""))codeDocument=document.toUpperCase();
//
//		query.setParameter(1, codeTiers);
//		query.setParameter(2, codeDocument);
//		query.setParameter(3, dateDeb, TemporalType.DATE);
//		query.setParameter(4, dateFin, TemporalType.DATE);
//		
//		resultTemp=query.getResultList();;
//		for (TaTicketDeCaisse TaTicketDeCaisse : resultTemp) {
//			TaTicketDeCaisse.calculSommeAvoir();
//			TaTicketDeCaisse.calculSommeAcomptes();
//			if(TaTicketDeCaisse.calculRegleDocumentComplet().add(TaTicketDeCaisse.getAcomptes()).add(TaTicketDeCaisse.calculSommeAvoir()).compareTo(TaTicketDeCaisse.getNetTtcCalc())<0)
//				result.add(TaTicketDeCaisse);
//		}		
//		return result;
//		
//	}
	/**
	 * Recherche les factures non totalement réglées dont la date d'échéance rentre dans l'intervalle
	 * des 2 dates passées en paramètres
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @param tiers - tiers sur lequel porte la recherche, si null, alors on prend tous les tiers
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegleAEcheance(Date dateDeb, Date dateFin,String tiers,String document,BigDecimal limite) {
		if(limite==null)limite=BigDecimal.valueOf(0);
		List<TaTicketDeCaisse> result = new LinkedList<TaTicketDeCaisse>();
		List<TaTicketDeCaisse> resultTemp = null;
		String codeTiers="%";
		String codeDocument="%";
		String requete="select f from TaTicketDeCaisse f " +
				"where f.taTiers.codeTiers like :codeTiers " +
				" and f.codeDocument like :codeDocument" +
				" and f.dateEchDocument between :dateDeb and :dateFin " +
				//" and f.regleCompletDocument < f.netTtcCalc " +
				" order by f.taTiers.codeTiers,f.codeDocument";
		Query query = entityManager.createQuery(requete);
		if(tiers != null && !tiers.equals(""))codeTiers=tiers.toUpperCase();
		if(dateDeb==null)dateDeb=new Date(0);
		if(dateFin==null)dateFin=new Date("01/01/3000");
		if(document!=null && !document.equals(""))codeDocument=document.toUpperCase();

		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDocument", codeDocument);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		
		resultTemp=query.getResultList();
		for (TaTicketDeCaisse TaTicketDeCaisse : resultTemp) {
			TaTicketDeCaisse.calculSommeAvoir();
			TaTicketDeCaisse.calculSommeAcomptes();
			
			BigDecimal dif=TaTicketDeCaisse.getNetTtcCalc().subtract(TaTicketDeCaisse.calculRegleDocumentComplet().add(TaTicketDeCaisse.getAcomptes()).add(TaTicketDeCaisse.calculSommeAvoir()));
			if(dif.compareTo(limite)>0)
				result.add(TaTicketDeCaisse);
		}		
		return result;
		
	}
	
	/**
	 * Recherche les factures non totalement réglées dont la date d'échéance rentre dans l'intervalle
	 * des 2 dates passées en paramètres
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @param tiers - tiers sur lequel porte la recherche, si null, alors on prend tous les tiers
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	public List<Object[]> rechercheDocumentNonTotalementRegleAEcheance2(Date dateDeb, Date dateFin,String tiers,String document) {
		List<Object[]> result = new LinkedList<Object[]>();
		List<Object[]> resultTemp = null;
		String codeTiers="%";
		String codeDocument="%";
		if(tiers != null && !tiers.equals(""))codeTiers=tiers.toUpperCase();
		if(dateDeb==null)dateDeb=new Date(0);
		if(dateFin==null)dateFin=new Date("01/01/3000");
		if(document!=null && !document.equals(""))codeDocument=document.toUpperCase();
		
//		String requete="EXEC DOCNONTOTALEMENTREGLEAECHEANCE('"+codeTiers+"','"+codeDocument+"',"+dateDeb+","+dateFin+") p " ;
		String requete="select * from DOCNONTOTALEMENTREGLEAECHEANCE(?,?,?,?) p " ;
//				"where a.taTiers.codeTiers like ? " +
//				" and a.codeDocument like ?" +
//				" and a.dateEchDocument between ? and ? " +
//				//" and a.regleCompletDocument < a.netTtcCalc " +
//				" order by f.taTiers.codeTiers,f.codeDocument";
		Query query = entityManager.createNativeQuery(requete);
		if(tiers != null && !tiers.equals(""))codeTiers=tiers.toUpperCase();
		if(dateDeb==null)dateDeb=new Date(0);
		if(dateFin==null)dateFin=new Date("01/01/3000");
		if(document!=null && !document.equals(""))codeDocument=document.toUpperCase();

		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDocument", codeDocument);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		
		resultTemp=query.getResultList();
		result=resultTemp;
//		for (TaTicketDeCaisse TaTicketDeCaisse : resultTemp) {
//			if(TaTicketDeCaisse.calculRegleDocumentComplet().add(TaTicketDeCaisse.getAcomptes()).compareTo(TaTicketDeCaisse.getNetTtcCalc())<0)
//				result.add(TaTicketDeCaisse);
//		}		
		return result;
		
	}

	public List<TaTicketDeCaisse> rechercheDocumentRegle(Date dateDeb, Date dateFin,String tiers,String document) {
		List<TaTicketDeCaisse> result = new LinkedList<TaTicketDeCaisse>();
		List<TaTicketDeCaisse> resultTemp = null;
		String codeTiers="%";
		String codeDocument="%";
		String requete="select a from TaTicketDeCaisse a where a.taTiers.codeTiers like :codeTiers " +
				" and a.codeDocument like :codeDocument" +
				" and a.dateDocument between :dateDeb and :dateFin " +
				//" and a.regleCompletDocument > 0 " +
				" order by a.codeDocument";
		Query query = entityManager.createQuery(requete);
		if(tiers != null && !tiers.equals(""))codeTiers=tiers.toUpperCase();
		if(dateDeb==null)dateDeb=new Date(0);
		if(dateFin==null)dateFin=new Date("01/01/3000");
		if(document!=null && !document.equals(""))codeDocument=document.toUpperCase();

		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDocument", codeDocument);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		
		resultTemp=query.getResultList();;
		for (TaTicketDeCaisse TaTicketDeCaisse : resultTemp) {    //****** pour compile maven à remettre isa*****///
//			if(TaTicketDeCaisse.calculRegleDocumentComplet().add(TaTicketDeCaisse.getAcomptes()).add(TaTicketDeCaisse.calculSommeAvoir()).compareTo(BigDecimal.valueOf(0))>0)
//				result.add(TaTicketDeCaisse);
		}		
		return result;
		
	}
	
	/**
	 * Recherche les factures non totalement réglées entre 2 dates (light version)
	 * Version allégée pour réduire le temps de calcul
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return List<TaTicketDeCaisse> - tableau contenant les  factures entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaTicketDeCaisse> rechercheDocumentNonTotalementRegle(Date dateDeb, Date dateFin) {
		List<TaTicketDeCaisse> result = new LinkedList<TaTicketDeCaisse>();
		List<TaTicketDeCaisse> resultTemp = null;

		String requete="select a from TaTicketDeCaisse a" +
				" where a.dateDocument between :dateDeb and :dateFin ";
		Query query = entityManager.createQuery(requete);
		
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		
		resultTemp=query.getResultList();
		for (TaTicketDeCaisse TaTicketDeCaisse : resultTemp) {
			if(TaTicketDeCaisse.calculRegleDocumentComplet().compareTo(TaTicketDeCaisse.getNetTtcCalc())<0)
				result.add(TaTicketDeCaisse);
		}		
		return result;
		
	}
	
	/**
	 * Calcule le nombre de factures
	 * Version allégée pour réduire le temps de calcul
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return List<TaTicketDeCaisse> - tableau contenant les  factures entre ces 2 dates (null en cas d'erreur)
	 */
	public List<TaTicketDeCaisse> toutesFacturesEntreDeuxDates(Date dateDeb, Date dateFin) {
		List<TaTicketDeCaisse> result = new LinkedList<TaTicketDeCaisse>();

		String requete="select a from TaTicketDeCaisse a" +
				" where a.dateDocument between :dateDeb and :dateFin ";
		Query query = entityManager.createQuery(requete);
		
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		
		result=query.getResultList();
	
		return result;
		
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin,codeTiers,null);
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb,dateFin,codeTiers,null);
	}

	
	@Override
	public List<TaTicketDeCaisse> rechercheDocumentVerrouille(Date dateDeb, Date dateFin,
			String codeTiers, Boolean verrouille) {
		List<TaTicketDeCaisse> result = null;
		Query query = null;
		if(verrouille!=null && verrouille)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_VERROUILLE);
		else if(verrouille!=null && verrouille==false)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_NON_VERROUILLE);
		else query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;

	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentVerrouille(String codeDeb, String codeFin,
			String codeTiers, Boolean verrouille) {
		List<TaTicketDeCaisse> result = null;
		Query query = null;
		if(verrouille!=null && verrouille)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_CODE_VERROUILLE);
		else if(verrouille!=null && verrouille==false)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_CODE_NON_VERROUILLE);
		else query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}
	
	
	@Override
	public List<TaTicketDeCaisse> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaTicketDeCaisse> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATE_NON_EXPORT);
		else query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;

	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaTicketDeCaisse> result = null;
		Query query = null;
		if(export!=null && export)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_CODE_EXPORT);
		else if(export!=null && export==false)query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_CODE_NON_EXPORT);
		else query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentEtat(Date dateDeb, Date dateFin, String codeEtat) {
		List<TaTicketDeCaisse> result = null;
		Query query = null;
//		Date date2 = LibDate.incrementDate(dateDeb, nbJours, 0, 0);
//		System.out.println("rechercheDocumentEtat => date2 : "+date2);
		query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_ETAT_DATE);
		query.setParameter("dateDeb", dateDeb);
		query.setParameter("dateFin", dateFin);
		query.setParameter("codeEtat", codeEtat);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaTicketDeCaisse> rechercheDocumentEtat(Date dateDeb, Date dateFin,
			String codeEtat, String codeTiers) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc, String codeTiers) {
			List<Object[]> result = null;
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}
	
	@Override
	public List<TaTicketDeCaisse> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTicketDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTicketDeCaisse> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTicketDeCaisse> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTicketDeCaisse value) throws Exception {
		BeanValidator<TaTicketDeCaisse> validator = new BeanValidator<TaTicketDeCaisse>(TaTicketDeCaisse.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTicketDeCaisse value, String propertyName)
			throws Exception {
		BeanValidator<TaTicketDeCaisse> validator = new BeanValidator<TaTicketDeCaisse>(TaTicketDeCaisse.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTicketDeCaisse transientInstance) {
		entityManager.detach(transientInstance);
	}

	//@Override
	public String genereCode() {
		System.err.println("******************* NON IMPLEMENTE ****************************************");
		return null;
	}

	@Override
	public List<TaTicketDeCaisse> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TaTicketDeCaisseDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_ALL_LIGHT);
			List<TaTicketDeCaisseDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	public List<TaTicketDeCaisseDTO> findAllDTOPeriode(Date dateDebut, Date dateFin) {
		try {
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_ALL_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers","%");
			List<TaTicketDeCaisseDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<DocumentDTO> findAllDTOPeriode(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_ALL_LIGHT_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	 /**
	 * Classe permettant d'obtenir le nombre de facture dans la période
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre total de facture dans la période
	 */
	public long countDocument(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre facture dans periode");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";

			requete = "SELECT "
				+" count(d)"
				+" FROM TaTicketDeCaisse d " 
				+" where d.dateDocument between :dateDebut and :dateFin and d.taTiers.codeTiers like :codeTiers";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers",codeTiers);
			Long nbFacture = (Long)query.getSingleResult();
			result = nbFacture;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Classe permettant d'obtenir les factures non payées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de facture non payées totalement
	 */
	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre facture non paye totalement");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";

			requete="select count(f) "
					+ " from TaTicketDeCaisse f "					
					+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null "
					+ " and exists(select f2.netTtcCalc from TaTicketDeCaisse f2 "
					+ " left join f2.taRReglements rr   left join f2.taRAvoirs ra where f=f2 "
					+ " group by f2.netTtcCalc "					
					+ "  having coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0)< f2.netTtcCalc )";


			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers", codeTiers);
			Long nbDevisNonTranforme = (Long)query.getSingleResult();
			result = nbDevisNonTranforme;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Classe permettant d'obtenir les facture non payées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de facture non payées à relancer
	 */
	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
		logger.debug("getting nombre facture non payées à relancer");
		Long result = (long) 0;
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		if(codeTiers==null)codeTiers="%";
		try {
			String requete = "";
			requete="select count(f) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin  and f.dateEchDocument < :dateJour"
					+ " and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null"
					+ " and  exists(select f2.netTtcCalc from TaTicketDeCaisse f2 "
					+ " left join f2.taRReglements rr   left join f2.taRAvoirs ra where f=f2 "
					+ " group by f2.netTtcCalc "					
					+ "  having coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0)< f2.netTtcCalc )";
			

			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
//			query.setParameter("dateRef", dateRef);
			query.setParameter("datejour", dateJour);
			query.setParameter("codeTiers", codeTiers);
			Long nbFactureNonTranformeARelancer = (Long)query.getSingleResult();
			result = nbFactureNonTranformeARelancer;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	/**
	 * Classe permettant d'obtenir les factures Totalement payées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le nombre de factures Totalement payées
	 */
	public long countDocumentTransforme(Date debut, Date fin,String codeTiers) {
		logger.debug("getting nombre facture transfos");
		Long result = (long) 0;
		
		if(codeTiers==null)codeTiers="%";		
		try {
			String requete = "";

			requete="select count(f) "
					+ " from TaTicketDeCaisse f  "
					+ " where f.dateDocument between :dateDebut and :dateFin "
					+ " and f.taTiers.codeTiers like :codeTiers"
					+ " and f.taEtat is null"
					+ " and  exists(select f2.netTtcCalc from TaTicketDeCaisse f2 "
					+ " left join f2.taRReglements rr   left join f2.taRAvoirs ra where f=f2 "
					+ " group by f2.netTtcCalc "					
					+ "  having coalesce(sum(ra.affectation),0)+coalesce(sum(rr.affectation),0)>= f2.netTtcCalc )";

			
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", debut);
			query.setParameter("dateFin", fin);
			query.setParameter("codeTiers", codeTiers);
			Long nbFactureNonTranforme = (Long)query.getSingleResult();
			result = nbFactureNonTranforme;
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	
	//début ici

	 /**
	 * Classe permettant d'obtenir la listes des devis non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
	 * ne doivent pas dépasser par rapport à la date du jour 
	 * @return La requête renvoyée renvoi la liste des devis non transformés à relancer
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
		logger.debug("getting reste à règler des factures non totalement règler et qui n'ont pas d'état et donc à relancer");
		List<TaDevisDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE_A_RELANCER);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateRef", dateRef, TemporalType.DATE);
		query.setParameter("dateJour", dateJour, TemporalType.DATE);
//		query.setParameter("codeTiers","%");
		query.setParameter("codeTiers",codeTiers);
		List<DocumentChiffreAffaireDTO> l = query.getResultList();
		if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
			l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
		}
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
	}
//		return null;
	}

	/**
	 * Permet d'obtenir le ca généré par les Proforma non transformés à relancer sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total non transformés à relancer directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}
	
	/**
	 * Classe permettant d'obtenir le reste à règler des factures non totalement réglée sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des devis non transformés sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_TOTAL_LIGTH_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
//		return null;	
	}

	/**
	 * Permet d'obtenir le ca généré par les Devis non transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total NON Transformé directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les factures - les avoirs sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des factures - les avoirs sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalSansAvoirDTO(Date dateDebut, Date dateFin , String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_CA_TOTAL_SANS_AVOIR_LIGTH_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
			
	}

	/**
	 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des devis sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_CA_TOTAL_LIGTH_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
			
	}
	
	/**
	 * Classe permettant d'obtenir le ca généré par les tickets de caisse sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param typeRegroupement map a une ligne représentant en cle le type de regroupement en valeur : la valeur du regroupement (ex parFamilleArticle , 'toto')
	 * @param regrouper sert à savoir si on doit détailler ou synthétiser les résultats
	 * @return La requête renvoyée renvoi le CA des tickets de caisse sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper) {
		try {
			Query query = null;
			String requete="";
			if(codeTiers==null)codeTiers="%";
			if(typeRegroupement==null && typeRegroupement.equals("")) throw new RuntimeException("Type de regroupement invalide");
			if(valeurRegroupement==null && valeurRegroupement.equals("")) throw new RuntimeException("valeur de regroupement invalide");
//			for (String key : typeRegroupement.keySet()) {
				 switch(typeRegroupement)
			        {
			            case Const.PAR_FAMILLE_ARTICLE :
							requete="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
										+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
										+ " from TaTicketDeCaisse f  join f.lignes lf join lf.taArticle art join art.taFamille fam "
										+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers and "
										+ " fam.codeFamille like :valeur ";
							if(regrouper)requete+=" group by fam.codeFamille";
			            break;
						case Const.PAR_TAUX_TVA:
							if(valeurRegroupement instanceof String) {
								valeurRegroupement=LibConversion.stringToBigDecimal(valeurRegroupement.toString());
							}
			            	if(valeurRegroupement.equals("%")) {
			            		valeurRegroupement=listeTauxTvaExistant();
			            	}
							requete="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
										+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
										+ " from TaTicketDeCaisse f  join f.lignes lf  "
										+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers and "
										+ " lf.tauxTvaLDocument = :valeur ";
							if(regrouper)requete+=" group by lf.tauxTvaLDocument";			            	
			            break;
			            case Const.PAR_TYPE_PAIEMENT:
							requete="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
										+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
										+ " from TaTicketDeCaisse f  join f.taRReglements rr join rr.taReglement reg join reg.taTPaiement tp"
										+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers and "
										+ " tp.codeTPaiement like :valeur ";
							if(regrouper)requete+=" group by tp.codeTPaiement";
			            break;
			            case Const.PAR_VENDEUR:
							requete="select new fr.legrain.document.dto.DocumentChiffreAffaireDTO("
										+ " coalesce(sum(f.netHtCalc),0),coalesce(sum(f.netTvaCalc),0),coalesce(sum(f.netTtcCalc),0)) "
										+ " from TaTicketDeCaisse f  join f.taUtilisateurVendeur uv "
										+ " where f.dateDocument between :dateDebut and :dateFin and f.taTiers.codeTiers like :codeTiers and "
										+ " uv.username like :valeur ";
							if(regrouper)requete+=" group by uv.username";
			            break;
			            default:break;
			        }
//			}
			

					

			query = entityManager.createQuery(requete);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			query.setParameter("valeur",valeurRegroupement);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
			
	}

	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}
	
	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTOParTypeRegroupement(Date dateDebut, Date dateFin,String codeTiers,String typeRegroupement,Object valeurRegroupement,boolean regrouper){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			//par familleArticle ou tauxTva ou typePaiement ou vendeur
			infosCaTotal = listeChiffreAffaireTotalDTOParTypeRegroupement(dateDebut, dateFin,codeTiers,typeRegroupement,valeurRegroupement,regrouper).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}


	/**
	 * Classe permettant d'obtenir le ca généré par les devis transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le CA des devis transformés sur la période éclaté en fonction de la précision 
	 * Jour Mois Année
	 */
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			switch (precision) {
			case 0:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_REGLE_ANNEE_LIGTH_PERIODE_TOTALEMENTPAYE);
				
				break;

			case 1:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_REGLE_MOIS_LIGTH_PERIODE_TOTALEMENTPAYE);
				
				break;
			case 2:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_REGLE_JOUR_LIGTH_PERIODE_TOTALEMENTPAYE);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
//		return null;	
	}


	/**
	 * Permet d'obtenir le ca généré par les Devis transformés sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return Retourne les informations de CA Total Transformé directement dans un objet DocumentChiffreAffaireDTO 
	 */
	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
			DocumentChiffreAffaireDTO infosCaTotal = null;
			infosCaTotal = listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
			return infosCaTotal;
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			Query query = null;
			if(codeTiers==null)codeTiers="%";
			query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_REGLE_TOTAL_LIGTH_PERIODE_TOTALEMENTPAYE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();
			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
			}
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
		}
//		return null;	
	}

	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			switch (precision) {
			case 0:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_CA_ANNEE_LIGTH_PERIODE);
				
				break;

			case 1:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_CA_MOIS_LIGTH_PERIODE);
				
				break;
			case 2:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_CA_JOUR_LIGTH_PERIODE);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}


	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin,
			int precision,String codeTiers) {
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			switch (precision) {
			case 0:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE);
				
				break;

			case 1:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE);
				
				break;
			case 2:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers","%");
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
//		return null;	
	}


	@Override
	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin,
			int precision, int deltaNbJours,String codeTiers) {
		// TODO Auto-generated method stub
		Query query = null;
		if(codeTiers==null)codeTiers="%";
		try {
			Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
			Date dateJour = LibDate.dateDuJour();
			switch (precision) {
			case 0:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_ANNEE_LIGTH_PERIODE_A_RELANCER);
				
				break;

			case 1:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_MOIS_LIGTH_PERIODE_A_RELANCER);
			
				break;
			case 2:
				query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_RESTE_A_REGLER_JOUR_LIGTH_PERIODE_A_RELANCER);
				
				break;

			default:
				break;
			}
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
//			query.setParameter("dateRef", dateRef, TemporalType.DATE);
			query.setParameter("dateJour", dateJour, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
//		return null;	
	}
	
	
	/**
	 * Procedure permettant d'obtenir le ca généré par les facture d'un Tiers sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param codeTiers code du Tiers
	 * @return La requête renvoyée renvoi le CA des factures sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeTiersDTO(Date dateDebut, Date dateFin, String codeTiers) {
		try {
			Query query = null;
			query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_CA_TOTAL_LIGTH_PERIODE_PAR_TIERS);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers", codeTiers);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	/**
	 * Procedure permettant d'obtenir le ca généré par les facture d'un article sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param codeTiers code du Tiers
	 * @return La requête renvoyée renvoi le CA des factures sur la période 
	 */
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalParCodeArticleDTO(Date dateDebut, Date dateFin, String codeArticle) {
		try {
			Query query = null;
			query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.SUM_CA_TOTAL_LIGTH_PERIODE_PAR_ARTICLE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codearticle", codeArticle);
			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
			
	}
	
	
	 /**
	 * Classe permettant d'obtenir la listes des Factures non transformées
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
	 * ne doivent pas dépasser par rapport à la date du jour 
	 * @return La requête renvoyée renvoi la liste des Factures non transformées à relancer
	 */
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
		logger.debug("getting liste Prelevement non transfos à relancer");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Date dateRef = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
		Date dateJour = LibDate.dateDuJour();
		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_NON_PAYE_ARELANCER_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateRef", dateRef, TemporalType.DATE);
		query.setParameter("codeTiers",codeTiers);
		query.setParameter("dateJour", dateJour, TemporalType.DATE);
		List<DocumentDTO> l = query.getResultList();;
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}
	
	 /**
	 * Classe permettant d'obtenir les Factures non transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
	 */
	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		logger.debug("getting nombre Prelevement non transfos");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_NON_PAYE_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		query.setParameter("codeTiers",codeTiers);
		List<DocumentDTO> l = query.getResultList();;
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}
	
	 /**
	 * Classe permettant d'obtenir la liste des Factures transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des Prelevement non transformés
	 */
	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
		logger.debug("getting nombre Prelevement transforme");
		List<DocumentDTO> result = null;
		if(codeTiers==null)codeTiers="%";
		try {
		Query query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_PAYE_LIGHT_PERIODE);
		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		query.setParameter("codeTiers",codeTiers);
		List<DocumentDTO> l = query.getResultList();;
		logger.debug("get successful");
		return l;

	} catch (RuntimeException re) {
		logger.error("get failed", re);
		throw re;
	}
	}

	@Override
	public List<Date> findDateExport(Date dateDeb,Date dateFin) {
		try {
			Query query = entityManager.createQuery("select distinct(f.dateExport) from TaTicketDeCaisse f where f.dateExport is not null and f.dateExport between :dateDeb and :dateFin ");
			query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			List<Date> l = query.getResultList();
			return l;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	@Override
	public List<TaTicketDeCaisse> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		List<TaTicketDeCaisse> result = null;
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		Query query = null;
		if(dateExport!=null) {
			query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_DATEEXPORT);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("date", dateExport, TemporalType.TIMESTAMP);
		}
		else {query = entityManager.createNamedQuery(TaTicketDeCaisse.QN.FIND_BY_TIERS_AND_DATE);
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		}


		result = query.getResultList();
		return result;
	}





	
	
	
	///rajout des requetes par regroupement
	


	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return "select r   from TaRDocument r   where r.taTickeCaisse = "+prefixe+"   and ( r.taFacture IS NOT NULL)";
	}

	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return "doc.dateEchDocument";
	}

	@Override
	public String getRequeteSommeAffecte(String prefixe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteAffectationReglement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteAffectationAvoir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteAffectationReglementJPQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteAffectationAvoirJPQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteSommeAffecteJPQL(String prefixe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteARelancer() {
		// TODO Auto-generated method stub
		return " and doc.date_Ech_Document <= :dateRef and doc.date_Ech_Document >= :dateJour ";
	}
	
	@Override
	public String getRequeteARelancerJPQL() {
		// TODO Auto-generated method stub
		return " and doc.dateEchDocument <= :dateRef and doc.dateEchDocument >= :dateJour ";
	}

	@Override
	public String getRequeteTypePaiement(String prefixe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteTypePaiementSQL(String prefixe) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaTicketDeCaisse findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaTicketDeCaisse a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaTicketDeCaisse instance = (TaTicketDeCaisse)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}



	@Override
	public TaTicketDeCaisse findByIdFetch(int id) {
		logger.debug("getting TaTicketDeCaisse instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaTicketDeCaisse a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
//					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.idDocument="+id+" " +
					" order by l.numLigneLDocument");
			TaTicketDeCaisse instance = (TaTicketDeCaisse)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaTicketDeCaisse findByCodeFetch(String code) {
		logger.debug("getting TaTicketDeCaisse instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaTicketDeCaisse a " +
					" left join fetch  a.lignes l "
//					+" join fetch l.taDocument aa "
					
//					+" left join fetch a.taREtats re "
//					+" left join fetch a.taHistREtats hre "
//					+" left join fetch a.taRDocuments rd "
//					
//					+" left join fetch l.taREtatLigneDocuments rel "
//					+" left join fetch l.taHistREtatLigneDocuments rhel "
//					+" left join fetch l.taLigneALignes lal "
					+" where a.codeDocument='"+code+"' " +
					" order by l.numLigneLDocument");
		
			TaTicketDeCaisse instance = (TaTicketDeCaisse)query.getSingleResult();
			instance.setLegrain(true);

			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		} catch (Exception e) {
			return null;
		}
	}

}
