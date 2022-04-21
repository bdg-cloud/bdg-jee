package fr.legrain.documents.dao.jpa;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import com.ibm.icu.math.BigDecimal;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSubscription_old;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.IAbonnementDAO;
//import fr.legrain.document.model.TaProforma;
import fr.legrain.documents.dao.IDevisDAO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaAbonnement.
 * @see fr.legrain.documents.dao.TaAbonnementOld
 * @author Hibernate Tools
 */
public class TaAbonnementDAO extends AbstractDocumentDAO<TaAbonnement,TaInfosAbonnement,TaLAbonnement> /*extends AbstractApplicationDAO<TaAbonnement> */
implements IAbonnementDAO/*, IDocumentDAO<TaAbonnement>,IDocumentTiersStatistiquesDAO<TaAbonnement>,IDocumentTiersEtatDAO<TaAbonnement>*/ {

//	private static final Log log = LogFactory.getLog(TaAbonnementDAO.class);
	static Logger logger = Logger.getLogger(TaAbonnementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaAbonnement a";
	
	public TaAbonnementDAO(){
//		this(null);
	}
	
//	public TaAbonnementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaAbonnement.class.getSimpleName());
//		initChampId(TaAbonnement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaAbonnement());
//	}
	
//	public TaAbonnement refresh(TaAbonnement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			Map<IDocumentTiers,Boolean> listeEtatLegrain=new LinkedHashMap<IDocumentTiers, Boolean>();
//			detachedInstance.setLegrain(false);
//			
//        	for (TaRAcompte obj : detachedInstance.getTaRAcomptes()) {
//        		listeEtatLegrain.put(obj.getTaAcompte(),obj.getTaAcompte().isLegrain());
//        		obj.getTaAcompte().setLegrain(false);
//			}
//        	
//        	entityManager.refresh(detachedInstance);
//        	
//        	for (TaRAcompte obj : detachedInstance.getTaRAcomptes()) {        		
//        		obj.getTaAcompte().setLegrain(retourneEtatLegrain(listeEtatLegrain,obj.getTaAcompte()));
//			}
////			detachedInstance.setLegrain(false);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////
////			for (TaRAcompte a : detachedInstance.getTaRAcomptes()) {
////				session.evict(a.getTaAcompte().getTaRAcomptes());
////				session.evict(a.getTaAcompte());
////				session.evict(a);
////			}
////			session.evict(detachedInstance.getTaRAcomptes());
////			session.evict(detachedInstance);
////
////			detachedInstance = entityManager.find(TaAbonnement.class, detachedInstance.getIdDocument());
//			detachedInstance.setLegrain(true);
//			detachedInstance.calculLignesTva();
//			return detachedInstance;
//
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaAbonnement transientInstance) {
		logger.debug("persisting TaAbonnement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	public void remove(TaAbonnement persistentInstance) {
		logger.debug("removing TaAbonnement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	public TaAbonnement merge(TaAbonnement detachedInstance) {
		logger.debug("merging TaAbonnement instance");
		try {
			TaAbonnement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}

	public TaAbonnement findById(int id) {
		logger.debug("getting TaAbonnement instance with id: " + id);
		try {
			TaAbonnement instance = entityManager.find(TaAbonnement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public Boolean exist(String code) {
		logger.debug("exist with code: " + code);
		try {
			if(code!=null && !code.equals("")){
			Query query = entityManager.createQuery("select count(f) from TaAbonnement f where f.codeDocument='"+code+"'");
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
	
	public TaAbonnement findByCode(String code) {
		logger.debug("getting TaAbonnement instance with code: " + code);
		try {
			Query query = entityManager.createQuery("select a from TaAbonnement a " +
					" left join FETCH a.lignes l"+
					" where a.codeDocument='"+code+"' " +
							" order by l.numLigneLDocument");
			TaAbonnement instance = (TaAbonnement)query.getSingleResult();
			instance.setLegrain(true);
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
	public List<TaAbonnement> selectAll() {
		logger.debug("selectAll TaAbonnement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAbonnement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnement> selectAllAvecFetchLignesEtPlan() {
		logger.debug("selectAll TaAbonnement");
		try {
			Query ejbQuery = entityManager.createQuery("select a from TaAbonnement a "
					+ "	join fetch a.lignes i "
					+ "	join fetch i.taPlan ");
			List<TaAbonnement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnement> findByCodeTiers(String codeTiers) {
		logger.debug("getting TaAbonnement instance with codeTiers: " + codeTiers);
		List<TaAbonnement> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_TIERS);
			query.setParameter("codeTiers", codeTiers);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnement> findByCodeTiersAndDate(String codeTiers, Date debut, Date fin) {
		logger.debug("getting TaAbonnement instance with codeTiers: " + codeTiers);
		List<TaAbonnement> result = null;
		try {
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_TIERS_AND_DATE);
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
		logger.debug("getting TaAbonnement instance with codeTiers: " + codeTiers);
		List<Object> result = null;
		try {
			String jpql = null;
//			precision = 0;
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
				+select+", t.codeTiers,"
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
				+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
				+" from TaAbonnement f "
				+"left join f.taTiers t "
				+"where  t.codeTiers = :codeTiers and f.dateDocument between :dateDeb and :dateFin "
				+"group by "+groupBy+",t.codeTiers"
				;

//			jpql = "select  "
//			+"cast(extract(year from f.dateDocument)as string), t.codeTiers,"
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netHtCalc/6.55957) else sum(f.netHtCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTvaCalc/6.55957) else sum(f.netTvaCalc) end, "
//			+"case when extract ( year from f.dateDocument) <= 2001 then  sum(f.netTtcCalc/6.55957 ) else sum(f.netTtcCalc) end "
//			+" from TaAbonnement f "
//			+"left join f.taTiers t "
//			+"where  t.codeTiers = ? and f.dateDocument between ? and ? "
//			+"group by extract(year from f.dateDocument),t.codeTiers"
//			;
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
	
	/**
	 * Recherche les devis entre 2 dates
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaAbonnement> rechercheDocument(Date dateDeb, Date dateFin) {

		List<TaAbonnement> result = null;
		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		return result;
	}
	
	
	/**
	 * Recherche les devis entre 2 dates ordonnées par date
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<TaAbonnement> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin) {

		List<TaAbonnement> result = null;
		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_DATE_PARDATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		return result;
	}
	/**
	 * Recherche les devis entre 2 codes devis
	 * @param codeDeb - code de début
	 * @param codeFin - code de fin
	 * @return String[] - tableau contenant les IDs des devis entre ces 2 codes (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(String codeDeb, String codeFin) {
	public List<TaAbonnement> rechercheDocument(String codeDeb, String codeFin) {		
		List<TaAbonnement> result = null;
		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_CODE);
		query.setParameter("codeTiers","%");
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;

	}
//	@Override
	public int selectCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public List<TaTiers> findTiersByCodeArticleAndDate(String codeTiers, Date debut,
			Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> findChiffreAffaireByCodeArticle(String codeTiers,
			Date debut, Date fin, int precision) {
		// TODO Auto-generated method stub
		return null;
	}

//	/**
//	 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des devis sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_ANNEE_LIGTH_PERIODE);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_MOIS_LIGTH_PERIODE);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_JOUR_LIGTH_PERIODE);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
//
//	/**
//	 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des devis sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_TOTAL_LIGTH_PERIODE);
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}
//			
//	}

//	// On récupère les informations de CA HT Total directement dans un objet DocumentChiffreAffaireDTO
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}
//		
//
//	/**
//	 * Classe permettant d'obtenir le ca généré par les devis transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des devis transformés sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_ANNEE_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_MOIS_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_JOUR_LIGTH_PERIODE_TRANSFORME);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
//
//
//	/**
//	 * Permet d'obtenir le ca généré par les Devis transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return Retourne les informations de CA Total Transformé directement dans un objet DocumentChiffreAffaireDTO 
//	 */
//	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}	
//	/**
//	 * Classe permettant d'obtenir le ca généré par les devis transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des devis transformés sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_TOTAL_LIGTH_PERIODE_TRANSFORME);
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}
//			
//	}

	

//	/**
//	 * Classe permettant d'obtenir le ca généré par les devis Non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des devis Non transformés sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date dateDebut, Date dateFin, int precision,String codeTiers) {
//		Query query = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
//	
//	/**
//	 * Classe permettant d'obtenir le ca généré par les devis Non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des devis Non transformés sur la période éclaté en fonction de la précision 
//	 * Jour Mois Année
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date dateDebut, Date dateFin, int precision, int deltaNbJours,String codeTiers) {
//		Query query = null;
//		try {
//			Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//			Date datejour = LibDate.dateDuJour();
//			if(codeTiers==null)codeTiers="%";
//			switch (precision) {
//			case 0:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_ANNEE_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//
//			case 1:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_MOIS_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//			case 2:
//				query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_JOUR_LIGTH_PERIODE_NON_TRANSFORME_A_RELANCER);
//				
//				break;
//
//			default:
//				break;
//			}
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("dateref", dateref, TemporalType.DATE);
//			query.setParameter("datejour", datejour, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();;
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//			
//	}
//	
//	/**
//	 * Classe permettant d'obtenir le ca généré par les devis non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le CA des devis non transformés sur la période 
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		try {
//			Query query = null;
//			if(codeTiers==null)codeTiers="%";
//			query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_TOTAL_LIGTH_PERIODE_NON_TRANSFORME);
//			query.setParameter("datedebut", dateDebut, TemporalType.DATE);
//			query.setParameter("datefin", dateFin, TemporalType.DATE);
//			query.setParameter("codeTiers",codeTiers);
//			List<DocumentChiffreAffaireDTO> l = query.getResultList();
//			if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//				l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//			}
//			logger.debug("get successful");
//			return l;
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//		}
//			
//	}
//
//	/**
//	 * Permet d'obtenir le ca généré par les Devis non transformés sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return Retourne les informations de CA Total NON Transformé directement dans un objet DocumentChiffreAffaireDTO 
//	 */
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireNonTransformeTotalDTO(dateDebut, dateFin,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}	
	/**
	 * Classe permettant d'obtenir le ca généré par les devis sur une période donnée
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi le montant des devis sur la période
	 */
//	public List<Object> findCADevisSurPeriode(Date debut, Date fin) {
//		logger.debug("NE PLUS UTLISER : getting nombre ca des devis");
//		logger.debug("UTLISER plutot : findChiffreAffaireTotalDTO dans TaAbonnementDAO");
//		List<Object> result = null;
//		
//		
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" sum(d.netHtCalc)"
//				+" FROM TaAbonnement d " 
//				+" where d.dateDocument between ? and ?";
//			Query query = entityManager.createQuery(requete);
//			query.setParameter(1, debut);
//			query.setParameter(2, fin);
//			result = query.getResultList();
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//	

	
	public List<Object> findChiffreAffaireTotalTransfo(Date debut, Date fin, int precision) {
		logger.debug("NE PLUS UTILISER : getting ChiffreAffaire total transfo");
		logger.debug("UTLISER plutot : findChiffreAffaireTransformeJmaDTO dans TaAbonnementDAO");
		List<Object> result = null;
		try {
			String requete = "";
			String groupBy = "";
			String select = "";
			
			if(precision==0) {
				select = "'','',cast(extract(year from d.dateDocument)as string)";
				groupBy = "'','',extract(year from d.dateDocument)";
			} else if (precision==1){
				select = "'',cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "'',extract(month from d.dateDocument),extract(year from d.dateDocument)";
			} else {
				select = "cast(extract(day from d.dateDocument)as string),cast(extract(month from d.dateDocument)as string),cast(extract(year from d.dateDocument)as string)";
				groupBy = "extract(day from d.dateDocument),extract(month from d.dateDocument),extract(year from d.dateDocument)";
			}
			
			requete = "SELECT "+select+ ", "
			+" sum(d.netHtCalc), "
			+" sum(d.netTvaCalc), "
			+" sum(d.netTtcCalc) "
			+" FROM TaAbonnement d " 
			+" where d.dateDocument between :dateDeb and :dateFin "
			+" and exists (select r " 
			+" from TaRDocument r " 
			+" where r.taDevis = d " 
			+" and ( r.taFacture IS NOT NULL " 
			+" OR r.taBonliv IS NOT NULL " 
			+" OR r.taBoncde IS NOT NULL " 
			+" OR r.taProforma IS NOT NULL )) "
			+" group by "+groupBy; 
			Query query = entityManager.createQuery(requete);
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
	 * Classe permettant d'obtenir les devis transformés
	 * @param debut date de début des données
	 * @param fin date de fin des données
	 * @return La requête renvoyée renvoi la liste des devis transformés
	 */
	public List<TaAbonnement> findDevisTransfos(Date debut, Date fin) {
		logger.debug("getting nombre devis non transfos");
		List<TaAbonnement> result = null;
		
		
		try {
			String requete = "";

			requete = "SELECT "
				+" d"
				+" FROM TaAbonnement d " 
				+" where d.dateDocument between :dateDeb and :dateFin"
				+" and  exists (select r " +
						"from TaRDocument r " +
						" where r.taDevis = d" +
						" and ( taFacture IS NOT NULL" +
						" OR taBonliv IS NOT NULL " +
						" OR taBoncde IS NOT NULL " +
						" OR taProforma IS NOT NULL))"
				+" order by d.mtHtCalc DESC";
			Query query = entityManager.createQuery(requete);
			query.setParameter("dateDeb", debut);
			query.setParameter("dateFin", fin);
			result = query.getResultList();
			return result;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
//	 /**
//	 * Classe permettant d'obtenir les devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi la liste des devis non transformés
//	 */
//	public List<DocumentDTO> findDocumentNonTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		logger.debug("getting nombre devis non transfos");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_NON_TRANSFORME_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentDTO> l = query.getResultList();;
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//	}

//	 /**
//	 * Classe permettant d'obtenir la liste des devis transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi la liste des devis non transformés
//	 */
//	public List<DocumentDTO> findDocumentTransfosDTO(Date dateDebut, Date dateFin,String codeTiers) {
//		logger.debug("getting nombre devis transforme");
//		List<DocumentDTO> result = null;
//		if(codeTiers==null)codeTiers="%";
//		try {
//		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_TRANSFORME_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentDTO> l = query.getResultList();;
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//	}
//	
//	 /**
//	 * Classe permettant d'obtenir la listes des devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
//	 * ne doivent pas dépasser par rapport à la date du jour 
//	 * @return La requête renvoyée renvoi la liste des devis non transformés à relancer
//	 */
//	public List<DocumentDTO> findDocumentNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting liste devis non transfos à relancer");
//		List<DocumentDTO> result = null;
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateref", dateref, TemporalType.DATE);
//		query.setParameter("datejour", datejour, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentDTO> l = query.getResultList();;
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//	}
	
//	 /**
//	 * Classe permettant d'obtenir la listes des devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @param deltaNbJours Permet de calculer la date de référence que les dates d'échéances 
//	 * ne doivent pas dépasser par rapport à la date du jour 
//	 * @return La requête renvoyée renvoi la liste des devis non transformés à relancer
//	 */
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting ca des devis non transfos à relancer");
//		List<TaAbonnementDTO> result = null;
//		try {
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date datejour = LibDate.dateDuJour();
//		if(codeTiers==null)codeTiers="%";
//		Query query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_TOTAL_NON_TRANSFORME_ARELANCER_LIGHT_PERIODE);
//		query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//		query.setParameter("dateFin", dateFin, TemporalType.DATE);
//		query.setParameter("dateref", dateref, TemporalType.DATE);
//		query.setParameter("datejour", datejour, TemporalType.DATE);
//		query.setParameter("codeTiers",codeTiers);
//		List<DocumentChiffreAffaireDTO> l = query.getResultList();
//		if (l == null || l.isEmpty() || l.get(0).getMtHtCalc() == null) {
//			l = DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);				
//		}
//		logger.debug("get successful");
//		return l;
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		return DocumentChiffreAffaireDTO.initialiseAZero(dateDebut);
//	}
//	}
//
//	/**
//	 * Permet d'obtenir le ca généré par les Proforma non transformés à relancer sur une période donnée
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return Retourne les informations de CA Total non transformés à relancer directement dans un objet DocumentChiffreAffaireDTO 
//	 */
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers){
//			DocumentChiffreAffaireDTO infosCaTotal = null;
//			infosCaTotal = listeChiffreAffaireNonTransformeARelancerTotalDTO(dateDebut, dateFin, deltaNbJours,codeTiers).get(0);
//			infosCaTotal.setMtHtCalc(infosCaTotal.getMtHtCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTvaCalc(infosCaTotal.getMtTvaCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			infosCaTotal.setMtTtcCalc(infosCaTotal.getMtTtcCalc().setScale(2,BigDecimal.ROUND_HALF_UP));
//			return infosCaTotal;
//	}	
	
	//	/**
//	 * Classe permettant d'obtenir les devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi la liste des devis non transformés
//	 */
//	public List<TaAbonnement> findDevisNonTransfos(Date debut, Date fin) {
//		logger.debug("getting nombre devis non transfos");
//		List<TaAbonnement> result = null;
//		
//		
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" d"
//				+" FROM TaAbonnement d " 
//				+" where d.dateDocument between ? and ?"
//				+" and not exists (select r " +
//						"from TaRDocument r " +
//						" where r.taDevis = d" +
//						" and ( taFacture IS NOT NULL" +
//						" OR taBonliv IS NOT NULL " +
//						" OR taBoncde IS NOT NULL " +
//						" OR taProforma IS NOT NULL))"
//						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter(1, debut);
//			query.setParameter(2, fin);
//			result = query.getResultList();
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
	
//	 /**
//	 * Classe permettant d'obtenir les devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre total de devis dans la période
//	 */
//	public long countDocument(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre devis dans periode");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaAbonnement d " 
//				+" where d.dateDocument between :datedeb and :datefin and d.taTiers.codeTiers like :codeTiers";
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbDevis = (Long)query.getSingleResult();
//			result = nbDevis;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

//	/**
//	 * Classe permettant d'obtenir les devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de devis non transformés
//	 */
//	public long countDocumentNonTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre devis non transfos");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaAbonnement d " 
//				+" where d.dateDocument between :datedeb and :datefin"
//				+" and not exists (select r " +
//						"from TaRDocument r " +
//						" where r.taDevis = d" +
//						" and ( taFacture IS NOT NULL" +
//						" OR taBonliv IS NOT NULL " +
//						" OR taBoncde IS NOT NULL " +
//						" OR taProforma IS NOT NULL)) and d.taTiers.codeTiers like :codeTiers";;
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbDevisNonTranforme = (Long)query.getSingleResult();
//			result = nbDevisNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	/**
//	 * Classe permettant d'obtenir les devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de devis non transformés à relancer
//	 */
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours,String codeTiers) {
//		logger.debug("getting nombre devis non transfos à relancer");
//		Long result = (long) 0;
//		Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//		Date dateJour = LibDate.dateDuJour();
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaAbonnement d " 
//				+" where d.dateDocument between :datedeb and :datefin"
//				+" and d.dateEchDocument <= :dateref"
//				+" and d.dateEchDocument >= :datejour"
//				+" and not exists (select r " +
//						"from TaRDocument r " +
//						" where r.taDevis = d" +
//						" and ( taFacture IS NOT NULL" +
//						" OR taBonliv IS NOT NULL " +
//						" OR taBoncde IS NOT NULL " +
//						" OR taProforma IS NOT NULL)) and d.taTiers.codeTiers like :codeTiers";;
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("dateref", dateref);
//			query.setParameter("datejour", dateJour);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbDevisNonTranformeARelancer = (Long)query.getSingleResult();
//			result = nbDevisNonTranformeARelancer;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	/**
//	 * Classe permettant d'obtenir les devis non transformés
//	 * @param debut date de début des données
//	 * @param fin date de fin des données
//	 * @return La requête renvoyée renvoi le nombre de devis transformés
//	 */
//	public long countDocumentTransforme(Date debut, Date fin,String codeTiers) {
//		logger.debug("getting nombre devis transfos");
//		Long result = (long) 0;
//		
//		if(codeTiers==null)codeTiers="%";
//		try {
//			String requete = "";
//
//			requete = "SELECT "
//				+" count(d)"
//				+" FROM TaAbonnement d " 
//				+" where d.dateDocument between :datedeb and :datefin"
//				+" and exists (select r " +
//						"from TaRDocument r " +
//						" where r.taDevis = d" +
//						" and ( taFacture IS NOT NULL" +
//						" OR taBonliv IS NOT NULL " +
//						" OR taBoncde IS NOT NULL " +
//						" OR taProforma IS NOT NULL)) and d.taTiers.codeTiers like :codeTiers";
////						+" order by d.mtHtCalc DESC";;
//			Query query = entityManager.createQuery(requete);
//			query.setParameter("datedeb", debut);
//			query.setParameter("datefin", fin);
//			query.setParameter("codeTiers", codeTiers);
//			Long nbDevisNonTranforme = (Long)query.getSingleResult();
//			result = nbDevisNonTranforme;
//			return result;
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}
//
//	//----------------------------
//
//		@Override
//		public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin) {
//			try {
//				Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ARTICLES_PAR_TIERS_PAR_MOIS);
//				query.setParameter("dateDebut", debut, TemporalType.DATE);
//				query.setParameter("dateFin", fin, TemporalType.DATE);
//				List<TaArticlesParTiersDTO> l = query.getResultList();
//				logger.debug("get successful");
//				return l;
//
//			} catch (RuntimeException re) {
//				logger.error("get failed", re);
//				throw re;
//			}
//		}
//
//		@Override
//		public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin) {
//			try {
//				Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ARTICLES_PAR_TIERS_TRANSFORME);
//				query.setParameter("dateDebut", debut, TemporalType.DATE);
//				query.setParameter("dateFin", fin, TemporalType.DATE);
//				List<TaArticlesParTiersDTO> l = query.getResultList();
//				logger.debug("get successful");
//				return l;
//
//			} catch (RuntimeException re) {
//				logger.error("get failed", re);
//				throw re;
//			}
//		}
//
//		@Override
//		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin) {
//			try {
//				Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME);
//				query.setParameter("dateDebut", debut, TemporalType.DATE);
//				query.setParameter("dateFin", fin, TemporalType.DATE);
//				List<TaArticlesParTiersDTO> l = query.getResultList();
//				logger.debug("get successful");
//				return l;
//
//			} catch (RuntimeException re) {
//				logger.error("get failed", re);
//				throw re;
//			}
//		}
//
//		@Override
//		public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours) {
//			try {
//				Date dateref = LibDate.incrementDate(LibDate.dateDuJour(), deltaNbJours, 0, 0);
//				Date datejour = LibDate.dateDuJour();
//				Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ARTICLES_PAR_TIERS_NON_TRANSFORME_ARELANCER);
//				query.setParameter("dateDebut", debut, TemporalType.DATE);
//				query.setParameter("dateFin", fin, TemporalType.DATE);
//				query.setParameter("dateref", dateref, TemporalType.DATE);
//				query.setParameter("datejour", datejour, TemporalType.DATE);
//				List<TaArticlesParTiersDTO> l = query.getResultList();
//				logger.debug("get successful");
//				return l;
//
//			} catch (RuntimeException re) {
//				logger.error("get failed", re);
//				throw re;
//			}
//		}
//	
@Override
	public List<TaAbonnement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers) {
		return rechercheDocument(codeDeb, codeFin, codeTiers, null);
	}

	@Override
	public List<TaAbonnement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers) {
		return rechercheDocument(dateDeb, dateFin, codeTiers, null);
	}

	@Override
	public List<TaAbonnement> rechercheDocument(Date dateDeb, Date dateFin,
			String codeTiers, Boolean export) {
		List<TaAbonnement> result = null;
		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_TIERS_AND_DATE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaAbonnement> rechercheDocument(String codeDeb, String codeFin,
			String codeTiers, Boolean export) {
		List<TaAbonnement> result = null;
		Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_TIERS_AND_CODE);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("codeDeb", codeDeb);
		query.setParameter("codeFin", codeFin);
		result = query.getResultList();
		return result;
	}	
	
	@Override
	public List<TaAbonnement> rechercheDocumentEtat(Date dateDeb, Date datefin, String codeEtat) {
		List<TaAbonnement> result = null;
		Query query = null;
		if(codeEtat!=null) {
			query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_ETAT_DATE);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("codeEtat", codeEtat);
		} else {
			query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_ETAT_ENCOURS_DATE);
			query.setParameter("date", datefin);
		}
		
		result = query.getResultList();
		return result;
	}

	@Override
	public List<TaAbonnement> rechercheDocumentEtat(Date dateDeb, Date datefin,
			String codeEtat, String codeTiers) {
		List<TaAbonnement> result = null;
		Query query = null;
		if(codeEtat!=null) {
			query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_ETAT_TIERS_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateDeb", dateDeb);
			query.setParameter("dateFin", datefin);
			query.setParameter("codeEtat", codeEtat);		
		} else {
			query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_ETAT_TIERS_ENCOURS_DATE);
			query.setParameter("codeTiers", codeTiers);
			query.setParameter("dateFin", datefin);
		}
		
		result = query.getResultList();
		return result;
	}

	/**
	 * Recherche les devis entre 2 dates light
	 * @param dateDeb - date de début
	 * @param dateFin - date de fin
	 * @return String[] - tableau contenant les IDs des factures entre ces 2 dates (null en cas d'erreur)
	 */
	//public String[] rechercheFacture(Date dateDeb, Date dateFin) {
	public List<Object[]> rechercheDocument(Date dateDeb, Date dateFin,Boolean light) {
		List<Object[]> result = null;
		Query query = null;
		if(light)
			query =entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_DATE_LIGHT);
		else query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_DATE);
		query.setParameter("codeTiers","%");
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		
		
		return result;
		
	}
	@Override
	public List<Object[]> rechercheDocumentLight(Date dateDeb, Date dateFin,String codeTiers) {
		List<Object[]> result = null;
		Query query = null;
		query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_DATE_LIGHT);
		if(codeTiers==null || codeTiers.equals(""))codeTiers="%";
		query.setParameter("codeTiers", codeTiers);
		query.setParameter("dateDeb", dateDeb, TemporalType.DATE);
		query.setParameter("dateFin", dateFin, TemporalType.DATE);
		result = query.getResultList();
		return result;
	}
	@Override
	public List<Object[]> rechercheDocumentLight(String codeDoc,String codeTiers) {
			List<Object[]> result = null;
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_BY_TIERS_AND_CODE_LIGHT);
			query.setParameter("codeTiers","%");
			query.setParameter("codeDocument", codeDoc+"%");
			result = query.getResultList();
			return result;

		}

	@Override
	public List<TaAbonnement> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAbonnement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAbonnement> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAbonnement> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAbonnement value) throws Exception {
		BeanValidator<TaAbonnement> validator = new BeanValidator<TaAbonnement>(TaAbonnement.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAbonnement value, String propertyName)
			throws Exception {
		BeanValidator<TaAbonnement> validator = new BeanValidator<TaAbonnement>(TaAbonnement.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAbonnement transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public String genereCode() {
		System.err.println("******************* NON IMPLEMENTE ****************************************");
		return null;
	}

	@Override
	public List<TaAbonnement> selectAll(IDocumentTiers taDocument, Date dateDeb,
			Date dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<TaAbonnementDTO> findAllLight() {
		try {
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ALL_LIGHT);
			List<TaAbonnementDTO> l = query.getResultList();;
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/**
	 * @author yann
	 * @param Integer idStripeSubscription
	 * @return TaAbonnement
	 */
	public TaAbonnement findByIdSubscription(Integer idStripeSubscription) {
//		logger.debug("getting TaAbonnement with idStripeSubscription: " );
//		try {
//			String jpql = null;
//		jpql = "select abo from TaStripeSubscription sub join sub.taAbonnement abo where sub.idStripeSubscription = :idStripeSubscription" ;
//		Query query = entityManager.createQuery(jpql);
//		query.setParameter("idStripeSubscription", idStripeSubscription);
//		TaAbonnement instance = (TaAbonnement)query.getSingleResult();
//		logger.debug("get successful");
//		return instance;
//		} catch (RuntimeException re) {
//			logger.debug(re);
//		    return null;
//		}
		return null;
	}
	
	
	public List<TaAbonnementDTO> findAllEnCoursDTOArrivantAEcheanceDansXJours(Integer nbJours, String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ALL_LIGHT_EN_COURS_DTO_ARRIVANT_A_ECHEANCE_DANS_X_JOURS);
			Date now = new Date();
			LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime localDateTimeNowPlusXJours = localDateTimeNow.plusDays(nbJours);
			Date nowPlusXJours = Date.from(localDateTimeNowPlusXJours.atZone(ZoneId.systemDefault()).toInstant());
			System.out.println("*****Recherche d'abonnement qui ont la date fin période au "+nowPlusXJours+" ******");
			query.setParameter("now", now, TemporalType.DATE);
			query.setParameter("nowPlusXJours", nowPlusXJours, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<TaAbonnementDTO> l = (List<TaAbonnementDTO>) query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnementDTO> findAllEnCoursDTOPeriode(Date dateDebut, Date dateFin,String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ALL_LIGHT_EN_COURS_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<TaAbonnementDTO> l = (List<TaAbonnementDTO>) query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public DocumentChiffreAffaireDTO chiffreAffaireTotalEnCoursDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_TOTAL_LIGHT_EN_COURS_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			DocumentChiffreAffaireDTO l = (DocumentChiffreAffaireDTO) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public long countDocumentEnCours(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.COUNT_TOTAL_LIGHT_EN_COURS_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			Long l = (Long) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public List<TaAbonnementDTO> findAllSuspenduDTOPeriode(Date dateDebut, Date dateFin,String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ALL_LIGHT_SUSPENDU_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<TaAbonnementDTO> l = (List<TaAbonnementDTO>) query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSuspenduDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_TOTAL_LIGHT_SUSPENDU_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			DocumentChiffreAffaireDTO l = (DocumentChiffreAffaireDTO) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public long countDocumentSuspendu(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.COUNT_TOTAL_LIGHT_SUSPENDU_PERIODE);
//			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
//			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			Long l = (Long) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	
	
	
	
	public List<TaAbonnementDTO> findAllAnnuleDTOPeriode(Date dateDebut, Date dateFin,String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ALL_LIGHT_ANNULE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			List<TaAbonnementDTO> l = (List<TaAbonnementDTO>) query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnementDTO> findAllAnnuleDTO(String codeTiers){
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ALL_LIGHT_ANNULE);
			query.setParameter("codeTiers",codeTiers);
			List<TaAbonnementDTO> l = (List<TaAbonnementDTO>) query.getResultList();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public long countDocumentAnnule(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.COUNT_TOTAL_LIGHT_ANNULE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			Long l = (Long) query.getSingleResult();
			logger.debug("get successful");
			return l;

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	public DocumentChiffreAffaireDTO chiffreAffaireTotalAnnuleDTO(Date dateDebut, Date dateFin,String codeTiers) {
		try {
			if(codeTiers==null)codeTiers="%";
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.SUM_CA_TOTAL_LIGHT_ANNULE_PERIODE);
			query.setParameter("dateDebut", dateDebut, TemporalType.DATE);
			query.setParameter("dateFin", dateFin, TemporalType.DATE);
			query.setParameter("codeTiers",codeTiers);
			DocumentChiffreAffaireDTO l = (DocumentChiffreAffaireDTO) query.getSingleResult();
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
			Query query = entityManager.createNamedQuery(TaAbonnement.QN.FIND_ALL_LIGHT_PERIODE);
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
	
//	public List<TaAbonnementDTO> rechercherSubscriptionPlanDTO(String idExternePlan) {
//		logger.debug("getting TaStripeSubscription_old instance with idExternePlan: " + idExternePlan);
//		try {
//			if(!idExternePlan.equals("")){
//				Query query = entityManager.createQuery("select new fr.legrain.document.dto.TaAbonnementDTO("
//						+ " s.id, s.idExterne, s.codeDocument, "
//						+ " c.idStripeCustomer, c.idExterne, c.taTiers.codeTiers, c.taTiers.nomTiers,"
//						+ " s.quantity, s.billing, s.status, s.dateDebut, s.dateFin, s.dateAnnulation, s.commentaireAnnulation, s.nbEcheance, s.versionObj) "
//						+ " from TaStripeSubscriptionItem f join f.taStripeSubscription s join s.taStripeCustomer c where f.taPlan.idExterne='"+idExternePlan+"' ");
//				List<TaAbonnementDTO> instance = (List<TaAbonnementDTO>)query.getResultList();
//				logger.debug("get successful");
//				return instance;
//			}
//			return null;
//		} catch (RuntimeException re) {
//		    return null;
//		}
//	}
	
	/**
	 * @author yann
	 * @return taAbonnement
	 */
	public TaAbonnement findByIdLEcheance(Integer idLEcheance) {
		logger.debug("getting TaAbonnement with idLEcheance: " );
		try {
			String jpql = null;
		jpql = "select sub from TaLEcheance ech join ech.taAbonnement sub where ech.idLEcheance = :idLEcheance" ;
		Query query = entityManager.createQuery(jpql);
		query.setParameter("idLEcheance", idLEcheance);
		TaAbonnement instance = (TaAbonnement)query.getSingleResult();
		logger.debug("get successful");
		return instance;
		} catch (RuntimeException re) {
			logger.debug(re);
		    return null;
		}
	}
	
	/**
	 * @author yann
	 * @return taAbonnement
	 */
	public List<TaAbonnement> findAllEnCours() {
		logger.debug("findAllEnCours " );
		Date now = new Date();
		try {
			String jpql = null;
		jpql = "select abo from TaAbonnement abo "
				+ " where abo.dateAnnulation = null "
				+ " and (abo.dateFin = null or abo.dateFin > :now)"
				+ " and abo.dateDebut < :now"
				+ " and abo.suspension != true" ;
		Query query = entityManager.createQuery(jpql);
		query.setParameter("now", now, TemporalType.DATE);
		List<TaAbonnement> instance = (List<TaAbonnement>)query.getResultList();
		logger.debug("get successful");
		return instance;
		} catch (RuntimeException re) {
			logger.debug(re);
		    return null;
		}
	}
	
	
	/**
	 * Requête qui remonte tous les abonnements qui ont des lignes d'échéances avec date d'échéance passé ou égale à aujourd'hui
	 * et qui ne sont pas déjà suspendu 
	 * et qui ne sont pas annuler
	 * et dont l'avis d'échéance n'est pas lié a une facture
	 * Ces abonnements sont donc à suspendre (car ils n'ont pas était payé)
	 * @author yann
	 * @return liste de TaAbonnementDTO (abonnements)
	 */
	public List<TaAbonnementDTO> selectAllASuspendre(){
		logger.debug("getting TaAbonnementDTO instance a suspendre: " );
		Date now = new Date();
		try {
			String jpql = null;
		jpql = "select new fr.legrain.document.dto.TaAbonnementDTO("
				+ " f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,infos.prenomTiers, infos.nomEntreprise,"
				+ " f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTvaCalc,f.netTtcCalc, f.dateSuspension, f.suspension, f.dateFin, f.dateDebut,f.dateAnnulation, f.commentaireAnnulation)"
				+ " from TaLAvisEcheance lav join lav.taDocument av join lav.taLEcheance lech join lech.taAbonnement f left join av.taRDocuments rdoc"
				+ " left join rdoc.taFacture fact"
				+ " join f.taInfosDocument infos "
				+ " left join f.taStripeCustomer sc "
				+ " join f.taTiers tiers"
				+ " where (lech.dateEcheance < :now or lech.dateEcheance = :now) and f.suspension != true and f.dateAnnulation = null and fact = null ";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("now", now);
		List<TaAbonnementDTO> instance = (List<TaAbonnementDTO>)query.getResultList();
		logger.debug("get successful");
		return instance;
		} catch (RuntimeException re) {
			logger.debug(re);
		    return null;
		}
	}
	
	public List<TaAbonnementDTO> rechercherAbonnementNonStripeCustomerDTO(String idExterneCustomer) {
		return rechercherAbonnementCustomerDTO(idExterneCustomer, false);
	}
	
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer) {
		return rechercherAbonnementCustomerDTO(idExterneCustomer, true);
	}
	
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer, Boolean stripe) {
		logger.debug("getting TaAbonnementDTO instance with idExterneCustomer: " + idExterneCustomer);
		try {
			String jpql = null;
			if(idExterneCustomer!=null && !idExterneCustomer.equals("")){
				jpql = "select new fr.legrain.document.dto.TaAbonnementDTO("
						+ " f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,infos.prenomTiers, infos.nomEntreprise,"
						+ " f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTvaCalc,f.netTtcCalc, f.dateSuspension, f.suspension, f.dateFin, f.dateDebut,f.dateAnnulation, f.commentaireAnnulation, f.typeAbonnement, f.dateDebutPeriodeActive, f.dateFinPeriodeActive, f.reconductionTacite)"
						+ " from TaAbonnement f join f.taInfosDocument infos join f.taTiers tiers"
						+ " where f.taStripeCustomer.idExterne='"+idExterneCustomer+"' ";
				if(stripe!=null) {
					if(stripe) {
						jpql += " and f.idExterne is not null";
					} else {
						jpql += " and f.idExterne is null";
					}
				}
				
			} else {
				jpql = "select new fr.legrain.document.dto.TaAbonnementDTO("
						+ " f.idDocument, f.codeDocument, f.dateDocument, f.libelleDocument, tiers.codeTiers, infos.nomTiers,infos.prenomTiers, infos.nomEntreprise,"
						+ " f.dateEchDocument,f.dateExport,f.netHtCalc,f.netTvaCalc,f.netTtcCalc, f.dateSuspension, f.suspension, f.dateFin, f.dateDebut,f.dateAnnulation, f.commentaireAnnulation, f.typeAbonnement, f.dateDebutPeriodeActive, f.dateFinPeriodeActive, f.reconductionTacite)"
						+ " from TaAbonnement f left join f.taStripeCustomer sc join f.taInfosDocument infos join f.taTiers tiers";
				if(stripe!=null) {
					if(stripe) {
						jpql += " where f.idExterne is not null";
					} else {
						jpql += " where f.idExterne is null";
					}
				}
			}
			
			Query query = entityManager.createQuery(jpql);
			List<TaAbonnementDTO> instance = (List<TaAbonnementDTO>)query.getResultList();
			logger.debug("get successful");
			return instance;
			
		} catch (RuntimeException re) {
			logger.debug(re);
		    return null;
		}
	}
	
	public List<TaAbonnement>  findAllSansDatesPeriode(){
		logger.debug("findAllSansDatesPeriode " );
		try {
			String jpql = null;
		jpql = "select abo from TaAbonnement abo "
				+ " where abo.dateDebutPeriodeActive = null "
				+ " and abo.dateFinPeriodeActive = null"
				+ " and abo.dateAnnulation = null";
		Query query = entityManager.createQuery(jpql);
		List<TaAbonnement> instance = (List<TaAbonnement>)query.getResultList();
		logger.debug("get successful");
		return instance;
		} catch (RuntimeException re) {
			logger.debug(re);
		    return null;
		}
	}
	public List<TaAbonnement> selectAllPourInsertionPeriodeV2(){
		try {
			String jpql = "select abo.id_document "
					+ "from ta_abonnement as abo, ta_l_abonnement labo "
					+ "where labo.id_l_document in ( "
					+ "select ligneAbo.id_l_document "
					+ "from ta_l_echeance as ech  "
					+ "left join ta_r_etat_ligne_doc as retat on retat.id_r_etat_ligne_doc = ech.id_r_etat "
					+ "left join ta_etat as etat on etat.id_etat = retat.id_etat "
					+ "left join ta_l_abonnement as ligneAbo on ligneAbo.id_l_document = ech.id_l_abonnement "
					+ "left join ta_abonnement as abo on abo.id_document = ligneAbo.id_document "
					+ "left join ta_tiers as tiers on tiers.id_tiers = abo.id_tiers "
					+ "left join ta_r_etat_ligne_doc as retatAbo on retatAbo.id_r_etat_ligne_doc = ligneAbo.id_r_etat "
					+ "left join ta_etat as etatAbo on etatAbo.id_etat = retatAbo.id_etat "
					+ "where etat.code_etat like 'doc_encours' and etatAbo.code_etat like 'doc_abandonne' "
					+ "order by tiers.code_tiers)"
					+ "and abo.id_document = labo.id_document";
			Query query = entityManager.createNativeQuery(jpql);
			List<Integer> ids = (List<Integer>)query.getResultList();
			List<TaAbonnement> instance = new ArrayList<TaAbonnement>();
			for (Integer integer : ids) {
				instance.add(findById(integer));
			}
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException e) {
			// TODO: handle exception
			return null;
		}
	}
	
	
	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers) {
		logger.debug("getting TaAbonnementFullDTO instance with idTiers: " + idTiers);
		try {
			String jpql2 = null;
			String jpql = null;
		jpql = "select new fr.legrain.abonnement.dto.TaAbonnementFullDTO("
				+ " abo.idDocument, abo.idExterne, abo.codeDocument,"
//				sc.idStripeCustomer, sc.idExterne,
				+ " tiers.codeTiers, tiers.nomTiers, tiers.prenomTiers, "
				//f.quantity, f.status,f.billing
				+ " abo.dateDebut, abo.dateFin, abo.dateAnnulation,"
				+ " abo.commentaireAnnulation,"
				+ " abo.dateSuspension, abo.suspension, freq.idFrequence, freq.codeFrequence,"
				+ " freq.liblFrequence, plan.nickname,"
//				plan.idStripePlan
				+ " art.idArticle, art.codeArticle, art.libellecArticle,"
			    + " ligneAbo.u1LDocument, ligneAbo.u2LDocument, "
				+ " ligneAbo.complement1,  ligneAbo.complement2,  ligneAbo.complement3,"
				+ " et.identifiant"
				+ " ) "
				+ " from TaLAbonnement ligneAbo  "
				//+ " join ligneAbo.taStripeSubscriptionItem item"
				+ " join ligneAbo.taPlan plan"
				//+ " join item.taStripeSubscription f"
				+ " join plan.taArticle art "
				//+ " left join f.taStripeCustomer sc "
				+ " join ligneAbo.taDocument abo"
				+ " join abo.taTiers tiers"
				+ " join abo.taFrequence freq"
				+ " join ligneAbo.taREtatLigneDocument re join re.taEtat et"
				+ " where tiers.idTiers = :idTiers and abo.dateAnnulation = null";
//		jpql2 = "select new fr.legrain.abonnement.dto.TaAbonnementFullDTO("
//                + " plan.amount"
//				+ " ) "
//				+ " from TaLAbonnement ligneAbo  "
//				+ " join ligneAbo.taStripeSubscriptionItem item"
//				+ " join item.taPlan plan"
//				+ " join item.taStripeSubscription f"
//				+ " join plan.taArticle art "
//				//+ " left join f.taStripeCustomer sc "
//				+ " join ligneAbo.taDocument abo"
//				+ " join abo.taTiers tiers"
//				+ " join abo.taFrequence freq"
//				+ " where tiers.idTiers = :idTiers and f.dateAnnulation = null";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("idTiers", idTiers);
		List<TaAbonnementFullDTO> instance = (List<TaAbonnementFullDTO>)query.getResultList();
		logger.debug("get successful");
		return instance;
		} catch (RuntimeException re) {
			logger.debug(re);
		    return null;
		}
	}

	@Override
	public List<TaAbonnement> rechercheDocumentVerrouille(Date dateDeb, Date dateFin, String codeTiers, Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaAbonnement> rechercheDocumentVerrouille(String codeDeb, String codeFin, String codeTiers,
			Boolean verrouille) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<Date> findDateExport(Date DateDeb, Date DateFin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaAbonnement> rechercheDocument(Date dateExport,String codeTiers,Date dateDeb, Date dateFin ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequeteDocumentTransforme(String prefixe) {
		// TODO Auto-generated method stub
		return "select r from TaRDocument r  where r.taDevis = "+prefixe+" and ( taFacture IS NOT NULL "
				+ " OR taBonliv IS NOT NULL  OR taBoncde IS NOT NULL  OR taProforma IS NOT NULL)";
	}

	@Override
	public String getDateAVerifierSiARelancer() {
		// TODO Auto-generated method stub
		return "doc.dateEchDocument";
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
	public TaAbonnement findDocByLDoc(ILigneDocumentTiers lDoc) {
		try {
			Query query = entityManager.createQuery("select a from TaAbonnement a " +
					"  join  a.lignes l"+
					" where l=:ldoc " +
							" order by l.numLigneLDocument");
			query.setParameter("ldoc", lDoc);
			TaAbonnement instance = (TaAbonnement)query.getSingleResult();
//			instance.setLegrain(true);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}


	
	
	@Override
	public TaAbonnement findByIdFetch(int id) {
		logger.debug("getting TaAbonnement instance with id: " + id);
		try {
			Query query = entityManager.createQuery("select a from TaAbonnement a " +
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
			TaAbonnement instance = (TaAbonnement)query.getSingleResult();			
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}


	@Override
	public TaAbonnement findByCodeFetch(String code) {
		logger.debug("getting TaAbonnement instance with code: " + code);
		try {
			
			Query query = entityManager.createQuery("select a from TaAbonnement a " +
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
		
			TaAbonnement instance = (TaAbonnement)query.getSingleResult();
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
